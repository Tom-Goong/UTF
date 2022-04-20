package com.yaoting.utf.domain.dag;

import com.yaoting.utf.domain.job.Job;
import com.yaoting.utf.domain.job.task.Task;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Optional;

@Data
@Accessors(chain = true)
@EqualsAndHashCode
public class DAG {
    public static final Long START_VERTEX_ID = 0L;
    public static final Long END_VERTEX_ID = Long.MAX_VALUE;

    private String name;
    private Job job;
    private boolean hasStabled = false; // if true, Dag can't change anymore

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
            throw new IllegalStateException("DAG has been stabled, can't add task more");
        }

        Optional<Vertex> vertex = find(task.getPreTask());
        if (!vertex.isPresent()) {
            throw new IllegalArgumentException("Can't find pre task，taskId: " + task.getId());
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
