package com.yaoting.utf.domain.node;

import com.yaoting.utf.common.trace.Trace;

public interface NodeManager {

    @Trace
    void registerSelfAndDiscovery();
}
