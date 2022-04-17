package com.yaoting.utf.domain.node;

import com.yaoting.utf.common.context.AppContext;
import com.yaoting.utf.common.trace.Trace;
import com.yaoting.utf.domain.coordinator.LocalCoordinator;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class DefaultNodeManager implements NodeManager {

    private Node self;
    @Resource
    private AppContext appContext;
    @Resource
    private LocalCoordinator localCoordinator;

    private final ScheduledExecutorService heartbeatExec = Executors.newSingleThreadScheduledExecutor();

    @PostConstruct
    private void init() {
        heartbeatExec.scheduleAtFixedRate(this::registerSelfAndDiscovery, 0L, 2L, TimeUnit.SECONDS);
    }

    @Trace
    @Override
    public void registerSelfAndDiscovery() {

        if (self == null) {
            self = Nodes.apply(appContext.getHostname(), appContext.getEnv())
                    .setNodeState(NodeState.STARTING)
                    .setHeartTime(System.currentTimeMillis());

            self.persist();

            log.info("Has registered self, let's start LocalCoordinator");
            localCoordinator.start();

            self.setNodeState(NodeState.ONLINE);
            self.persist();
        } else {
            self.updateHeartbeat();
        }

        // discovery new or offline node, notify local coordinator
        List<Node> allNodes = Nodes.list();
        for (Node node : allNodes) {
            if (isDeadNode(node)) {
                node.delete();
            } else if (isInActiveNode(node)) {
                localCoordinator.unregisterNode(node);
            } else {
                localCoordinator.registerNode(node);
            }
        }
    }

    private boolean isInActiveNode(Node node) {
        return node.getNodeState() == NodeState.OFFLINE || node.getHeartTime() < System.currentTimeMillis() - 60_000;
    }

    private boolean isDeadNode(Node node) {
        return node.getNodeState() == NodeState.DEAD || node.getHeartTime() < System.currentTimeMillis() - 10 * 60_000;
    }
}
