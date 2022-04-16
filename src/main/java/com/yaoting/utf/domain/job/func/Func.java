package com.yaoting.utf.domain.job.func;

import com.yaoting.utf.domain.job.Task;
import com.yaoting.utf.domain.job.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public interface Func<T> {
    Logger log = LoggerFactory.getLogger(Func.class);
    String START_FUNC_NAME = "StartFunc";
    String END_FUNC_NAME = "EndFunc";

    default String name() {
        return this.getClass().getName();
    }

    default Optional<String> validate(Task task) {
        return Optional.empty();
    }

    default Result<T> apply(Task task) {
        return null;
    }
}
