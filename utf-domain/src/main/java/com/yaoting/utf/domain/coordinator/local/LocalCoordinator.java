package com.yaoting.utf.domain.coordinator.local;

import com.yaoting.utf.domain.coordinator.Coordinator;
import com.yaoting.utf.domain.node.Node;

public interface LocalCoordinator extends Coordinator {

    void registerNode(Node node);

    void unregisterNode(Node node);
}
