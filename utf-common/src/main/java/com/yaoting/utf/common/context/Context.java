package com.yaoting.utf.common.context;

import java.util.Date;

public interface Context {

    ContextStatus getStatus();

    void toFailed();

    void toCompleted();

    Context setStatus(ContextStatus contextStatus);

    Date getStartTime();

    default long duration() {
        return System.currentTimeMillis() - getStartTime().getTime();
    }
}
