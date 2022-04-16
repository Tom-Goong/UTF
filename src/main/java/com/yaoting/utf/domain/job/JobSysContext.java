package com.yaoting.utf.domain.job;

import com.yaoting.utf.domain.job.nm.Node;
import com.yaoting.utf.infrastructure.context.Context;
import com.yaoting.utf.infrastructure.context.ContextStatus;
import com.yaoting.utf.infrastructure.context.PermanentContext;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Getter
@ToString
@Component
public class JobSysContext implements PermanentContext {

    private ContextStatus status = ContextStatus.running;
    private final Date startTime = new Date();

    @Setter
    private volatile Node node;


    @Override
    public void toFailed() {

    }

    @Override
    public void toCompleted() {

    }

    @Override
    public Context setStatus(ContextStatus contextStatus) {
        throw new IllegalStateException("AppContext 不支持调整状态");
    }
}
