package com.yaoting.utf.domain.coordinator;

import com.yaoting.utf.domain.coordinator.local.LocalCoordinator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;

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
