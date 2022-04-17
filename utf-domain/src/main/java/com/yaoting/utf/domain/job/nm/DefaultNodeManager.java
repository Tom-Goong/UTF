package com.yaoting.utf.domain.job.nm;

import com.yaoting.utf.common.context.AppContext;
import com.yaoting.utf.common.trace.Trace;
import com.yaoting.utf.common.utils.ValidateUtils;
import com.yaoting.utf.domain.job.JobSysContext;
import com.yaoting.utf.domain.job.coordinator.LocalCoordinator;
import com.yaoting.utf.domain.job.repo.NodeRepo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class DefaultNodeManager implements NodeManager {

    @Resource
    private AppContext appContext;
    @Resource
    private JobSysContext jobSysContext;
    @Resource
    private LocalCoordinator localCoordinator;

    private final Map<String, Node> activeNodes = new ConcurrentHashMap<>();

    @Resource
    private NodeRepo nodeRepo;

    @Trace
    @Scheduled(cron="0/3 * * * * ?")
    public void registerSelfAndDiscovery() {
        Node self;
        // register self
        String selfHostname = appContext.getHostname();
        if (activeNodes.containsKey(selfHostname)) {
            self = activeNodes.get(selfHostname);
            self.setHeartTime(System.currentTimeMillis());
            updateOrInsert(self);
        } else {
            self = addSelf();
            log.info("Has registered self, let's start LocalCoordinator");
            localCoordinator.start();
            activeNodes.put(selfHostname, self);
            jobSysContext.setNode(self);
        }

        // discovery
        List<Node> allNodes = nodeRepo.listAll();
        for (Node node : allNodes) {
            if (isDeadNode(node)) {
                deleteNode(node);
            } else if (isInActiveNode(node)) {
                offlineNode(node);
            } else {
                onlineNode(node);
            }
        }

        // self check
        for (Node node : activeNodes.values()) {
            if (isInActiveNode(node)) {
                dropActive(node);
            }
        }
    }

    private void onlineNode(Node node) {
        if (!activeNodes.containsKey(node.getHostname())) {
            localCoordinator.registerNode(node);
        }

        activeNodes.put(node.getHostname(), node);
    }

    private void offlineNode(Node node) {
        dropActive(node);
        node.setNodeState(NodeState.OFFLINE);

        updateOrInsert(node);
    }

    private void deleteNode(Node node) {
        dropActive(node);
        nodeRepo.delete(node);
    }

    private void dropActive(Node node) {
        if (localCoordinator.unregisterNode(node)) {
            activeNodes.remove(node.getHostname());
        }
    }

    private boolean isInActiveNode(Node node) {
        return node.getNodeState() == NodeState.OFFLINE || node.getHeartTime() < System.currentTimeMillis() - 60_000;
    }

    private boolean isDeadNode(Node node) {
        return node.getNodeState() == NodeState.DEAD || node.getHeartTime() < System.currentTimeMillis() - 10 * 60_000;
    }

    private void updateOrInsert(Node node) {
        Node nodeOfDB = nodeRepo.queryByHostname(node.getHostname());
        if (ValidateUtils.isNull(nodeOfDB)) {
            nodeRepo.save(node);
        } else {
            nodeRepo.update(node);
        }
    }

    private Node addSelf() {
        Node self = nodeRepo.queryByHostname(appContext.getHostname());
        if (self == null) {
            self = new Node()
                    .setHostname(appContext.getHostname())
                    .setEnv(appContext.getEnv())
                    .setNodeState(NodeState.ONLINE)
                    .setNodeType(NodeType.BossAndWorker)
                    .setHeartTime(System.currentTimeMillis());
        } else {
            self.setNodeState(NodeState.ONLINE);
            self.setNodeType(NodeType.BossAndWorker);
            self.setHeartTime(System.currentTimeMillis());
        }

        updateOrInsert(self);
        return self;
    }
}
