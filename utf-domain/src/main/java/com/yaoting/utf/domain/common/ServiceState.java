package com.yaoting.utf.domain.common;

public enum ServiceState {
    READY,            // 就绪，未准备完成
    STARTING,         // 启动中
    WORKING,          // 工作中
    STOPPING,
    STOPPED
}
