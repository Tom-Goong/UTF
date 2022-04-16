package com.yaoting.utf.infrastructure.context;

import com.yaoting.utf.infrastructure.utils.TrackLogger;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class BasicTempContext implements TemporaryContext {
    /**
     * 将项目内所有操作，账号化。包括项目内本身的调用。为系统本身分配账号
     *
     * 这样所有的上下文都可以携带账号信息了
     */
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
