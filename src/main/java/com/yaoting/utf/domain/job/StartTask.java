package com.yaoting.utf.domain.job;

import com.yaoting.utf.domain.job.func.Func;

public class StartTask extends Task {

    public StartTask(Long jobId) {
        this.jobId = jobId;

        setId(DAG.START_VERTEX_ID);
        funcName = Func.START_FUNC_NAME;
        isIdempotent = true;
        state = State.Ready;
    }

    @Override
    public boolean toState(State state) {
        setState(state);
        return true;
    }
}
