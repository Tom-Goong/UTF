package com.yaoting.utf.domain.coordinator;

import com.yaoting.utf.domain.executor.Listener;
import com.yaoting.utf.domain.job.Job;
import com.yaoting.utf.domain.job.task.Result;
import com.yaoting.utf.domain.common.ServiceState;
import com.yaoting.utf.domain.job.vo.SyncJobStateVO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@RequiredArgsConstructor
public class HTTPRemoteCoordinator implements RemoteCoordinator, Listener {
    private final String hostname;
    private final int port;

    @Getter @Setter
    private ServiceState state = ServiceState.WORKING;

    @Override
    public boolean addJob(Job job) {
        throw new IllegalStateException("Remote Coordinator do not accept Job, hostname: " + hostname);
    }

    @Override
    public void jobCallBack(Job job, Result result) {
        throw new IllegalStateException("Remote Coordinator do not need to sync, hostname: " + hostname);
    }

    /**
     * 异步化 同步job 状态给实际的应用实例
     */
    @Override
    public void syncInOtherJobState(SyncJobStateVO vo) {

    }

    @Override
    public String hostname() {
        return hostname;
    }
}
