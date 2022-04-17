package com.yaoting.utf.domain.job;

import com.yaoting.utf.domain.common.repo.BaseRepo;
import com.yaoting.utf.domain.job.Job;
import com.yaoting.utf.domain.job.task.Task;
import com.yaoting.utf.domain.job.State;

import java.util.List;

public interface JobRepo extends BaseRepo<Job> {

    List<Job> listBrief();

    List<Job> listJobsByStates(State... states);

    Job appendTask(Job job, Task task);

    void update(Job job);

    void updateOnlyJob(Job job);

    boolean updateStateWhen(Job job, State old);

    void updateTask(Task task);
}
