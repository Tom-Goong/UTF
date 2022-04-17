package com.yaoting.utf.domain.node;

import com.yaoting.utf.common.constant.EnvEnum;
import com.yaoting.utf.common.utils.SpringTool;
import com.yaoting.utf.domain.common.entity.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Node extends BaseEntity {
    private String hostname;
    private EnvEnum env;
    private NodeType nodeType;
    private NodeState nodeState;
    private long heartTime;

    public void updateHeartbeat() {
        setHeartTime(System.currentTimeMillis());

        SpringTool.getBean(NodeRepo.class).persist(this);
    }
}
