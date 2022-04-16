package com.yaoting.utf.domain.job.func;

import com.yaoting.utf.domain.job.Result;
import com.yaoting.utf.domain.job.Task;

import java.util.Optional;

public class StartFunc implements Func<Void> {

//    @Override
    public Class<Void> returnType() {
        return Void.class;
    }

    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    @Override
    public Optional<String> validate(Task task) {
        log.info("{} start to validate", name());
        return Optional.empty();
    }

    @Override
    public Result<Void> apply(Task task) {
        log.info("{} start to apply", name());
        return Result.voidData(task.getJobId(), task.getId());
    }
}
