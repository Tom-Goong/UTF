package com.yaoting.utf.infrastructure.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AudiLogUtils {
    private static final Logger audiLogger = LoggerFactory.getLogger("AUDI_LOG");

    public static void generateAudiLog(String account, Long startTime, String traceId, String recordMsg, String optResult, String optModule, String optService) {
        audiLogger.info("audit log||account={}||startTime={}||traceId={}||optEndpoint={}||optResult={}||optModule={}||optService={}",
                account, startTime, traceId, recordMsg, optResult, optModule, optService);
    }
}
