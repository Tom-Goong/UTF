package com.yaoting.utf.domain.job;


import com.yaoting.utf.common.utils.SpringTool;
import com.yaoting.utf.domain.job.repo.JobRepo;

import java.util.List;

public class Jobs {

    public static Job load(Long jobId) {
        JobRepo repo = SpringTool.getBean(JobRepo.class);

        return repo.load(jobId);
    }

    public static List<Job> listBrief() {
        JobRepo repo = SpringTool.getBean(JobRepo.class);
        return repo.listBrief();
    }

    public static List<Job> listReadyJobs() {
        return listJobsByStates(State.Ready);
    }

    public static List<Job> listJobsByStates(State... states) {
        JobRepo repo = SpringTool.getBean(JobRepo.class);
        return repo.listJobsByStates(states);
    }
}
