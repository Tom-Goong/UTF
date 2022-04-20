package com.yaoting.utf.common.context;

import com.yaoting.utf.common.utils.TrackLogger;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class BasicTempContext implements TemporaryContext {
    protected Date startTime = new Date();
    protected Date endTime;
    protected String traceId = TrackLogger.getTraceId();
    protected String spanId = TrackLogger.getSpanId();

    protected ContextStatus status = ContextStatus.running;

    public Date getEndTime() {
        return endTime == null ? new Date() : endTime;
    }

    @Override
    public void toFailed() {
        setStatus(ContextStatus.failed);
    }

    @Override
    public void toCompleted() {
        setStatus(ContextStatus.successful);
    }
}
