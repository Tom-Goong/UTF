package com.yaoting.utf.domain.job.executor;

import com.yaoting.utf.domain.job.Job;
import com.yaoting.utf.domain.job.Result;

public interface Listener {

    void jobCallBack(Job job, Result result);
}
