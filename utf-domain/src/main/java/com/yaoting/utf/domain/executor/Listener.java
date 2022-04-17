package com.yaoting.utf.domain.executor;

import com.yaoting.utf.domain.job.Job;
import com.yaoting.utf.domain.job.task.Result;

public interface Listener {

    void jobCallBack(Job job, Result result);
}
