package com.yaoting.utf.domain.job.func;

import com.yaoting.utf.domain.job.Task;
import com.yaoting.utf.domain.job.Result;

import java.util.Optional;

public class EndFunc implements Func<Void> {
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
