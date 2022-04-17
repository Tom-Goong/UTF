package com.yaoting.utf.domain.coordinator;

import com.yaoting.utf.domain.node.Node;

public interface LocalCoordinator extends Coordinator {

    void registerNode(Node node);

    void unregisterNode(Node node);
}
