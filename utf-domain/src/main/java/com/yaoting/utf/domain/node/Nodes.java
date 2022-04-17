package com.yaoting.utf.domain.node;

import com.yaoting.utf.common.constant.EnvEnum;
import com.yaoting.utf.common.utils.SpringTool;

import java.util.List;

public class Nodes {
    static public Node apply(String hostname, EnvEnum envEnum) {
        return new Node()
                .setHostname(hostname)
                .setEnv(envEnum)
                .setNodeType(NodeType.BOSS_AND_WORKER);
    }

    static public List<Node> list() {
        return SpringTool.getBean(NodeRepo.class)
                .listAll();
    }
}
