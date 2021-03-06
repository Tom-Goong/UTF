package com.yaoting.utf.common.context;

import java.util.Date;

public interface TemporaryContext extends Context {

    Date getEndTime();

    @Override
    default long duration() {
        return getEndTime().getTime() - getStartTime().getTime();
    }
}
