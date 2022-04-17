package com.yaoting.utf.domain.job.func;

import com.yaoting.utf.domain.job.Task;
import com.yaoting.utf.domain.job.Result;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SleepFunc implements Func<Void> {
    private final long sleepTime;

    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

//    @Override
    public Class<Void> returnType() {
        return Void.class;
    }

    @Override
    public Result<Void> apply(Task task) {
        log.info("Start to run {}", name());
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            return Result.failed(task.getJobId(), task.getId(), e.getMessage());
        }

        log.info("{} has run over", name());
        return Result.voidData(task.getJobId(), task.getId());
    }
}
