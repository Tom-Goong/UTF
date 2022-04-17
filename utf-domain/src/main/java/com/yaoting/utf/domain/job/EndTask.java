package com.yaoting.utf.domain.job;

import com.yaoting.utf.domain.job.func.Func;

public class EndTask extends Task {

    public EndTask(Long jobId) {
        this.jobId = jobId;

        setId(DAG.END_VERTEX_ID);
        funcName = Func.END_FUNC_NAME;
        isIdempotent = true;
        state = State.Ready;
    }


    @Override
    public boolean toState(State state) {
        setState(state);
        return true;
    }
}
