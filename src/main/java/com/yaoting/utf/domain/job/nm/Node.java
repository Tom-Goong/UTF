package com.yaoting.utf.domain.job.nm;

import com.yaoting.utf.infrastructure.constant.EnvEnum;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Node {
    private String hostname;
    private EnvEnum env;
    private NodeState nodeState;
    private NodeType nodeType;
    private long heartTime;
}
