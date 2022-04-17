package com.yaoting.utf.domain.node;

import com.yaoting.utf.domain.common.repo.BaseRepo;

public interface NodeRepo extends BaseRepo<Node> {

    Node queryByHostname(String hostname);

    void updateHeartbeat(Node node);
}
