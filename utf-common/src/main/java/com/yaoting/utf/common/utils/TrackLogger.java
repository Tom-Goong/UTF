package com.yaoting.utf.common.utils;

import org.slf4j.MDC;

import java.util.UUID;

import static com.yaoting.utf.common.utils.ValidateUtils.isBlank;
import static com.yaoting.utf.common.utils.ValidateUtils.isNotBlank;


public abstract class TrackLogger {
    private static final String TRACE_ID = "traceId";
    private static final String SPAN_ID = "spanId";

    public static void putTraceId(String defaultTraceId) {
        String traceId = isBlank(defaultTraceId) ? uuid() : defaultTraceId;
        putMDC(TRACE_ID, traceId);
    }

    public static void putSpanId(String spanId) {
        putMDC(SPAN_ID, spanId);
    }

    public static String getTraceId() {
        return MDC.get(TRACE_ID);
    }

    public static String getSpanId() {
        return MDC.get(SPAN_ID);
    }

    public static void putTraceId() {
        putMDC(TRACE_ID, uuid());
    }

    public static void putSpanId() {
        putMDC(SPAN_ID, uuid());
    }

    public static void clear() {
        remoteMDC(TRACE_ID);
        remoteMDC(SPAN_ID);
    }

    private static void putMDC(String key, String value) {
        if (isNotBlank(value)) {
            MDC.put(key, value);
        }
    }

    private static void remoteMDC(String key) {
        MDC.remove(key);
    }

    private static String uuid() {
        return UUID.randomUUID().toString();
    }
}
