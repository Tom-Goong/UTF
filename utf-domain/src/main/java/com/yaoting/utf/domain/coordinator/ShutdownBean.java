package com.yaoting.utf.domain.coordinator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;

/**
 * 更新节点状态
 */
@Slf4j
public class ShutdownBean implements DisposableBean {

    private final LocalCoordinator localCoordinator;

    public ShutdownBean(LocalCoordinator localCoordinator) {
        this.localCoordinator = localCoordinator;
    }

    @Override
    public void destroy() {
        log.info("Start to stop Job-Engine");

        localCoordinator.stop();

        log.info("Job-Engine has been stopped");
    }
}
