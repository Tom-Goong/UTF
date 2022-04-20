package com.yaoting.utf.domain.func;

import com.yaoting.utf.domain.job.task.Task;
import com.yaoting.utf.domain.job.task.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public interface Func<T> {
    Logger log = LoggerFactory.getLogger(Func.class);
    String START_FUNC_NAME = "StartFunc";
    String END_FUNC_NAME = "EndFunc";

    Namespace namespace();

    default String name() {
        return this.getClass().getName();
    }

    default Optional<String> parseAndValidate(Task task) {
        return Optional.empty();
    }

    default Result<T> apply(Task task) {
        return null;
    }
}
