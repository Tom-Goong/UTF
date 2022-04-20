package com.yaoting.utf.domain.executor;

import com.yaoting.utf.common.base.Tuple2;
import com.yaoting.utf.common.utils.GapRun;
import com.yaoting.utf.common.utils.JSONUtils;
import com.yaoting.utf.common.utils.ValidateUtils;
import com.yaoting.utf.domain.common.ServiceState;
import com.yaoting.utf.domain.dag.Vertex;
import com.yaoting.utf.domain.func.Func;
import com.yaoting.utf.domain.func.FuncFactory;
import com.yaoting.utf.domain.job.State;
import com.yaoting.utf.domain.job.task.Result;
import com.yaoting.utf.domain.job.task.Task;
import com.yaoting.utf.domain.resource.ThreadPool;
import jakarta.annotation.Resource;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;


@Slf4j
public class TaskExecutor implements Executor {

    @Resource
    private FuncFactory factory;
    @Resource
    private JobExecutor jobExecutor;

    @Getter @Setter
    private ServiceState state = ServiceState.READY;

    private final ExecutorService taskHandler = Executors.newSingleThreadExecutor();

    private final ScheduledExecutorService taskFutureHandler = Executors.newSingleThreadScheduledExecutor();

    private final BlockingQueue<Tuple2<Vertex, Func<?>>> taskQueue = new LinkedBlockingQueue<>(128);

    /**
     * task - taskFuture
     */
    private final Map<Vertex, FutureTask<Result<?>>> futureTasks = new ConcurrentHashMap<>();

    public boolean submitTask(Vertex vertex) {
        if (!isWorking()) {
            log.warn("this node can't accept task, state: {}", state);
            return false;
        }

        Optional<Func<?>> funcOptional = factory.findFunc(vertex.getFuncName());
        if (!funcOptional.isPresent()) {
            log.error("Func is not existed, vertex:{}", vertex);
            return false;
        }

        Task task = vertex.getTask();

        Func<?> func = funcOptional.get();
        Optional<String> validateResult = func.parseAndValidate(task);
        if (validateResult.isPresent()) {
            log.error("Func validate failed, funcName:{}, reason: {}", func.name(), validateResult.get());

            return false;
        }

        return taskQueue.add(Tuple2.of(vertex, func));
    }

    private void handleTask() {
        while (true) {
            if (!isWorking()) {
                log.warn("Task-Executor has been working");
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                }
            }
            try {
                log.info("Try to take a task from queue, queueSize: {}", taskQueue.size());
                Tuple2<Vertex, Func<?>> ele = taskQueue.take();
                Vertex vertex = ele.getT1();
                Task task = vertex.getTask();
                task.toState(State.Running);

                FutureTask<Result<?>> futureTask = new FutureTask<>(() -> ele.getT2().apply(task));
                ThreadPool.submitWorkThread(futureTask);
                futureTasks.put(vertex, futureTask);
            } catch (Throwable e) {
                log.error("handle task exception", e);
            }
        }
    }

    public void futureInspect() {
        log.info("Start to inspect futures of task, futureSize: {}", futureTasks.size());
        futureTasks.forEach((vertex, future) -> {
            if (future.isDone()) {
                Task task = vertex.getTask();
                try {
                    Result<?> result = future.get();
                    futureTasks.remove(vertex);
                    log.info("{} of taskId: {}, jobId:{} has been run, go to handle result: {}", task.getFuncName(), task.getId(), task.getJobId(), result);
                    ThreadPool.submitBossThread(() -> handleResult(vertex, result));
                } catch (Throwable e) {
                    log.error("failed to get result of task, jobId:{}, taskId:{}", task.getJobId(), task.getId(), e);
                }
            }
        });
    }

    private void handleResult(Vertex vertex, Result<?> result) {
        Task task = vertex.getTask();
        if (!result.getIsSucceed()) {
            log.error("task has failed, result:{}, vertex:{}", result, vertex);
            if (task.isIdempotent()) {
                log.info("retry to exec, taskId:{}", task.getName());
                submitTask(vertex);
            }
            return;
        }

        task.toState(State.Succeed);
        log.info("Task has been handled succeed, result:{}, vertex: {}", result, vertex);
        if (ValidateUtils.notNull(task.getCallBackUrl())) {
            callback(task.getCallBackUrl(), result);
        }

        // 往上层提交结果
        jobExecutor.handleTaskResult(vertex, result);
    }

    // TODO use Event
    private void callback(String callBackUrl, Result<?> result) {
        log.info("Start to callback, url:{}, result of task:{}", callBackUrl, result);
        try {
            String body = JSONUtils.objectToString(result);

            HttpResponse httpResponse = Unirest.post(callBackUrl)
                    .header("Content-Type", "application/json")
                    .body(body)
                    .asEmpty();
            if (!httpResponse.isSuccess()) {
                log.error("callback failed, url:{}, httpCode:{}, req-body:{}, resp-body:{}", callBackUrl, httpResponse.getStatus(),  body, httpResponse.getBody());
            }
        } catch (Exception e) {
            log.error("failed to callback, url: {}, task-result: {}", callBackUrl, result, e);
        }
    }

    @Override
    public synchronized void start() {
        log.info("this node of TaskExecutor has to start");

        switch (state) {
            case READY: log.info("Ready to work, let's start TaskExecutor"); break;
            case STARTING: log.warn("This node is starting, can not start again"); return;
            case STOPPING: log.warn("This node is stopping, can not start"); return;
            case STOPPED: log.warn("This node is starting, can not start"); return;
            case WORKING: log.warn("This node has started, can not start again"); return;
            default: throw new IllegalStateException("Unsupported NodeType need to be handled");
        }

        toState(ServiceState.STARTING);
        // prepare something

        taskHandler.submit(this::handleTask);
        taskFutureHandler.scheduleAtFixedRate(this::futureInspect, 0L, 2L, TimeUnit.SECONDS);

        toState(ServiceState.WORKING);
    }

    @Override
    public void stop() {
        log.info("Start to stop Task-Executor");
        toState(ServiceState.STOPPING);

        // stop
        GapRun.loopRun(this::tryStop, 200L);

        toState(ServiceState.STOPPED);

        log.info("Task-Executor has been stopped");
    }


    private boolean tryStop() {
        if (ValidateUtils.isNotEmpty(taskQueue)) {
            log.warn("Task-Executor has tasks which are running, will try to stop latter, taskQueue: {}", taskQueue.size());
            return false;
        } else {
            if (!taskHandler.isShutdown()) {
                taskHandler.shutdown();
            }
        }

        if (ValidateUtils.isNotEmpty(futureTasks)) {
            log.warn("Task-Executor has tasks which are running, will try to stop latter, futureTask: {}", futureTasks.size());
            return false;
        } else {
            if (ValidateUtils.isEmpty(taskQueue)) {
                taskFutureHandler.shutdown();
            }
        }

        return ValidateUtils.isEmpty(taskQueue) && ValidateUtils.isEmpty(futureTasks);
    }
}
