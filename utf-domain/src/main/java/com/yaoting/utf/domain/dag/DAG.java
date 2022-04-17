package com.yaoting.utf.domain.dag;

import com.yaoting.utf.domain.job.Job;
import com.yaoting.utf.domain.job.task.Task;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Optional;

/**
 * 简易版本，需使用 Edge
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode
public class DAG {
    public static final Long START_VERTEX_ID = 0L;
    public static final Long END_VERTEX_ID = Long.MAX_VALUE;

    private String name;
    private Job job;
    private boolean hasStabled = false; // 表示是否已经定型，定型后，不允许在添加节点

    private Vertex startVertex;
    private Vertex endVertex;

    public DAG(Job job) {
        this.name = job.getName();
        this.job = job;

        startVertex = Vertex.newStartVertex(job.getId());
        endVertex = Vertex.newEndVertex(job.getId());
    }

    public DAG pushTask(Task task) {
        if (hasStabled) {
            throw new IllegalStateException("DAG 已经固话，无法再添加节点");
        }

        Optional<Vertex> vertex = find(task.getPreTask());
        if (!vertex.isPresent()) {
            throw new IllegalArgumentException("找不到前置节点，taskId: " + task.getId());
        }

        vertex.get().appendTask(task);

        return this;
    }

    public DAG stable() {
        startVertex.appendEndVertex(endVertex);
        hasStabled = true;
        return this;
    }

    public List<Task> flat() {
        return startVertex.flat();
    }

    public Optional<Vertex> find(Long id) {
        return startVertex.find(id);
    }

    public Long vertexNum() {
        return startVertex.childrenVertexNum();
    }

}
