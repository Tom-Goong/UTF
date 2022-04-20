package com.yaoting.utf.domain.func;

import com.yaoting.utf.domain.job.task.Task;
import com.yaoting.utf.domain.job.task.Result;

import java.util.Optional;

public class EndFunc extends DefaultFunc<Void> {

    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    @Override
    public Optional<String> parseAndValidate(Task task) {
        log.info("{} start to validate", name());
        return Optional.empty();
    }

    @Override
    public Result<Void> apply(Task task) {
        log.info("{} start to apply", name());
        return Result.voidData(task.getJobId(), task.getId());
    }
}
