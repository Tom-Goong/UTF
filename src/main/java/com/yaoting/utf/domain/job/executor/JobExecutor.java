package com.yaoting.utf.domain.job.executor;


import com.yaoting.utf.domain.job.*;
import com.yaoting.utf.infrastructure.utils.ValidateUtils;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
@Component
public class JobExecutor implements Executor {

    @Resource
    private WalleTaskExecutor walleTaskExecutor;

    @Resource
    private List<Listener> listeners;

    @Getter @Setter
    private ServiceState state = ServiceState.READY;

    private final ExecutorService jobHandler = Executors.newSingleThreadExecutor();

    private final BlockingQueue<Job> jobQueue = new LinkedBlockingQueue<>(128);

    private Map<Long, Job> jobInRunning = new ConcurrentHashMap<>();
//    private final Map<Long, DAG> dags = new ConcurrentHashMap<>();

    public boolean submitJob(Job job) {
        if (!isWorking()) {
            log.warn("this node can't accept job, state: {}", state);
            return false;
        }

        State state = job.getState();
        if (!(state == State.Submitting )) {
            log.error("Job-Executor can only accept job with state of Submitting, jobId: {}, state: {}", job.getId(), state);
            return false;
        }

        if (job.isStateOf(State.Submitting)) {
            if (!job.toState(State.Submitted)) {
                return false;
            }
        }

        boolean isSubmitted = jobQueue.add(job);
        if (!isSubmitted) {
            job.toState(State.Failed);
        }

        return isSubmitted;
    }

    @Override
    public void start() {
        switch (getState()) {
            case READY: log.info("Ready to work, let's start JobExecutor"); break;
            case STARTING: log.warn("This node is starting, can not start again"); return;
            case STOPPING: log.warn("This node is stopping, can not start"); return;
            case STOPPED: log.warn("This node is starting, can not start"); return;
            case WORKING: log.warn("This node has started, can not start again"); return;
            default: throw new IllegalStateException("Unsupported NodeType need to be handled");
        }

        toState(ServiceState.STARTING);

        walleTaskExecutor.start();
        // prepare something

        jobHandler.submit(this::handlerJob);

        // 变更状态
        toState(ServiceState.WORKING);
    }

    private void handlerJob() {
        while (true) {
            if (!isWorking()) {
                log.warn("Job-Executor has been working");
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                }
            }

            try {
                Job job = jobQueue.take();

                log.info("Take a job from queue, jobId: {}, {}", job.getId(), job);
                job.toState(State.Running);
                jobInRunning.put(job.getId(), job);

                job.getTasksDAG().getStartVertex()
                        .runnableVertexes().forEach(vertex -> {
                            vertex.getTask().toState(State.Submitting);
                            if (walleTaskExecutor.submitTask(vertex)) {
                                vertex.getTask().toState(State.Submitted);
                                log.info("Submitted StartVertex to Engine, jobId:{}, jobName:{}", job.getId(), job.getName());
                            }
                        });
            } catch (Throwable e) {
                log.error("handle task exception", e);
            }
        }
    }

    public void handleTaskResult(DAG.Vertex vertex, Result<?> result) {

        if (result.getIsSucceed()) {
            // if is endVertex
            if (vertex.isEndVertex()) {
                Long jobId = vertex.getTask().getJobId();
                Job job = jobInRunning.get(jobId);
                job.toState(State.Succeed);

                // TODO sync to 协调器
                log.info("EndVertex has succeed, notify Coordinate");
                sync2Listeners(job, result);
//                dags.remove(jobId);
                jobInRunning.remove(jobId);
                return;
            }

            if (!isWorking()) {
                // 暂停任务，不可提交新任务
                return;
            }

            vertex.getOuts().stream()
                    .filter(DAG.Vertex::isReady)
                    .forEach(ele -> {
                        Task task = ele.getTask();
                        task.toState(State.Submitting);
                        walleTaskExecutor.submitTask(ele);
                        task.toState(State.Submitted);
                        log.info("Some task is ready, has to submit it, jobId:{}, task:{}", task.getJobId(), task.getId());
                    });
        } else {
            // handle failed

            if (vertex.isEndVertex()) {
                Long jobId = vertex.getTask().getJobId();
                jobInRunning.remove(jobId);
//                dags.remove(jobId);
            }

        }
    }

    private void sync2Listeners(Job job, Result<?> result) {
        if (ValidateUtils.isNotEmpty(listeners)) {
            listeners.forEach(listener -> listener.jobCallBack(job, result));
        }
    }

    @Override
    public void stop() {
        log.info("Start to stop Job-Executor");
        toState(ServiceState.STOPPING);

        while (ValidateUtils.isNotEmpty(jobQueue)) {
            try {
                Job job = jobQueue.take();
                // 回退状态，让其他节点可以获取
                job.toState(State.Ready);
            } catch (InterruptedException e) {
                // 正常不会发生
                log.error(e.getMessage(), e);
            }
        }

        // 关闭异步线程
        jobHandler.shutdown();

        walleTaskExecutor.stop();

        // 入口和taskExecutor都已关闭，running中的不会变更状态，直接修改成
        jobInRunning.values().forEach(job -> {
            log.info("Engine starts to exit, uncompleted job will be frozen, jobId: {}, jobName: {}", job.getId(), job.getName());
            job.toState(State.Frozen);
        });

        toState(ServiceState.STOPPED);

        log.info("Job-Executor has been stopped");
    }
}
