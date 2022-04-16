package com.yaoting.utf.domain.job.coordinator;

import com.yaoting.utf.domain.job.nm.Node;

public interface LocalCoordinator extends Coordinator {

    void registerNode(Node node);

    boolean unregisterNode(Node node);
}
