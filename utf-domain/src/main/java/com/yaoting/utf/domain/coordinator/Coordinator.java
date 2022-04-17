package com.yaoting.utf.domain.coordinator;

import com.yaoting.utf.domain.job.Job;
import com.yaoting.utf.domain.common.LifeCycle;
import com.yaoting.utf.domain.job.vo.SyncJobStateVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public interface Coordinator extends LifeCycle {
    Logger log = LoggerFactory.getLogger(Coordinator.class);

    boolean addJob(Job job);

    void syncInOtherJobState(SyncJobStateVO vo);
}
