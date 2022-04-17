package com.yaoting.utf.domain.job;

import com.yaoting.utf.common.utils.SpringTool;
import com.yaoting.utf.domain.common.BaseEntity;
import com.yaoting.utf.domain.job.repo.JobRepo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Task extends BaseEntity {

    protected String name;

    protected Long jobId;

    protected Long preTask;

    protected String funcName;

    protected String parameter;

    protected boolean isIdempotent;

    protected String callBackUrl;

    protected State state = State.Ready;

    @Setter(AccessLevel.NONE)
    @JsonIgnore
    private JobRepo repo = SpringTool.getBean(JobRepo.class);

    public boolean toState(State state) {
        setState(state);
        repo.updateTask(this);
        return true;
    }

    public boolean isStateOf(State state) {
        return getState() == state;
    }

    @JsonIgnore
    public boolean isSuccessful() {
        return state == State.Succeed;
    }
}
