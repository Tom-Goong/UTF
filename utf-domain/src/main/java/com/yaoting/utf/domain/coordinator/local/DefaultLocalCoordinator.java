package com.yaoting.utf.domain.coordinator.local;


import com.yaoting.utf.common.context.AppContext;
import com.yaoting.utf.domain.common.ServiceState;
import com.yaoting.utf.domain.coordinator.remote.HTTPRemoteCoordinator;
import com.yaoting.utf.domain.coordinator.remote.RemoteCoordinator;
import com.yaoting.utf.domain.executor.JobExecutor;
import com.yaoting.utf.domain.executor.Listener;
import com.yaoting.utf.domain.job.Job;
import com.yaoting.utf.domain.job.State;
import com.yaoting.utf.domain.job.task.Result;
import com.yaoting.utf.domain.job.vo.SyncJobStateVO;
import com.yaoting.utf.domain.node.Node;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultLocalCoordinator implements LocalCoordinator, Listener {

    @Getter @Setter
    private ServiceState state = ServiceState.READY;

    private int port;

    @Resource
    private JobExecutor jobExecutor;
    @Resource
    private AppContext appContext;

    // hostname-RemoteCoordinator
    private final Map<String, RemoteCoordinator> remoteCoordinators = new ConcurrentHashMap<>();

    // jobId-RemoteCoordinator
    private final Map<Long, RemoteCoordinator> jobAndCoordinators = new ConcurrentHashMap<>();

    @Override
    public void start() {
        if (isStateOf(ServiceState.WORKING)) {
            log.warn("LocalCoordinator has started, no need to start again");
            return;
        }

        if (isStateOf(ServiceState.STARTING) || isStateOf(ServiceState.STOPPING)) {
            throw new IllegalStateException(String.format("LocalCoordinator is %s, illegal invoking", getState()));
        }

        toState(ServiceState.STARTING);

        log.info("Begin to start JobExecutor Engine");
        jobExecutor.start();
        log.info("Succeed to start JobExecutor Engine");

        toState(ServiceState.WORKING);
    }

    @Override
    public void stop() {
        toState(ServiceState.STOPPING);

        // do stop
        jobExecutor.stop();

        // todo delete and sync remote coordinator

        toState(ServiceState.STOPPED);
    }

    @Override
    public void registerNode(Node node) {
        if (node.getHostname().equals(appContext.getHostname())) {
            return;
        }

        RemoteCoordinator remote = remoteCoordinators.get(node.getHostname());
        if (remote == null) {
            remote = new HTTPRemoteCoordinator(node.getHostname(), port);
            remoteCoordinators.put(remote.hostname(), remote);
        }
    }

    @Override
    public void unregisterNode(Node node) {
        if (node.getHostname().equals(appContext.getHostname())) {
            return;
        }

        RemoteCoordinator remoteCoordinator = remoteCoordinators.get(node.getHostname());
        if (remoteCoordinator != null) {
            jobAndCoordinators.forEach((jobId, coordinator) -> {
                if (coordinator == remoteCoordinator) {
                    jobAndCoordinators.remove(jobId);
                }
            });
            remoteCoordinators.remove(node.getHostname());
        }
    }

    @Override
    public boolean addJob(Job job) {
        if (!isWorking()) {
            log.warn("LocalCoordinator isn't working, try latter, state: {}", getState());
            return false;
        }

        if (!jobExecutor.isWorking()) {
            log.info("JobExecutor hasn't been working, try latter");
            return false;
        }

        if (jobAndCoordinators.containsKey(job.getId())) {
            log.error("Job has been submitted, id:{}, name:{}, state: {}", job.getId(), job.getName(), job.getState());
            return false;
        }

        State state = job.getState();
        if (!(state == State.Ready || state == State.Frozen)) {
            log.warn("Only accept Ready or Frozen job, this job will be refused, id: {}, state: {}", job.getId(), state);
            return false;
        }

        // add to queue or direct submit
        log.info("Receive a job, id: {}, name:{}, state: {}", job.getId(), job.getName(), state);
        if (!job.toState(State.Submitting)) {
            log.warn("Failed to submit，jobId: {}", job.getId());
            return false;
        }

        // 同步消息到其他机器
        sync2Remote(job);

        return jobExecutor.submitJob(job);
    }

    private void sync2Remote(Job job) {
        SyncJobStateVO syncJobStateVO = new SyncJobStateVO()
                .setState(job.getState())
                .setJobId(job.getId())
                .setHostname(appContext.getHostname());

        remoteCoordinators.values().forEach(remoteCoordinator -> remoteCoordinator.syncInOtherJobState(syncJobStateVO));
    }

    @Override
    public void syncInOtherJobState(SyncJobStateVO vo) {
        String remoteHostname = vo.getHostname();
        // TODO if remoteCoordinator == null, refresh them
        RemoteCoordinator remoteCoordinator = remoteCoordinators.get(remoteHostname);
        jobAndCoordinators.put(vo.getJobId(), remoteCoordinator);
    }

    @Override
    public void jobCallBack(Job job, Result result) {
        if (result.getIsSucceed()) {
            log.info("Job has been run succeed, remove from cache。id:{}, name: {}", job.getId(), job.getName());
            jobAndCoordinators.remove(job.getId());
        }
    }
}
