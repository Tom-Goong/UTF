package com.yaoting.utf.domain.job;

import com.yaoting.utf.infrastructure.utils.ValidateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.ArrayList;
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

    @Data
    @EqualsAndHashCode(exclude = "ins")
    @ToString(exclude = "ins")
    @Accessors(chain = true)
    public static final class Vertex {
        private Long id;
        private VertexType vertexType;
        private Task task;

        private List<Vertex> ins = new ArrayList<>();
        private List<Vertex> outs = new ArrayList<>();

        public static Vertex newStartVertex(Long jobId) {
            return new Vertex(jobId, START_VERTEX_ID, VertexType.START_VERTEX);
        }

        public static Vertex newEndVertex(Long jobId) {
            return new Vertex(jobId, END_VERTEX_ID, VertexType.END_VERTEX);
        }

        private Vertex(Long jodId, long id, VertexType type) {
            this.id = id;
            vertexType = type;
            if (type == VertexType.START_VERTEX) {
                task = new StartTask(jodId);
            } else if (type == VertexType.END_VERTEX) {
                task = new EndTask(jodId);
            }
        }

        private Vertex(Task task) {
            this.id = task.getId();
            this.task = task;
            vertexType = VertexType.TASK_VERTEX;
        }

        public String getFuncName() {
            return task.getFuncName();
        }

        public boolean isStartVertex() {
            return vertexType == VertexType.START_VERTEX;
        }

        public boolean isEndVertex() {
            return vertexType == VertexType.END_VERTEX;
        }

        public boolean isTaskVertex() {
            return vertexType == VertexType.TASK_VERTEX;
        }

        public Boolean isReady() {
            return task.isStateOf(State.Ready) && ins.stream().allMatch(Vertex::isSuccessful);
        }

        /**
         * 包括自己
         */
        public List<Vertex> runnableVertexes() {
            List<Vertex> list = new ArrayList<>();

            if (this.isReady()) {
                list.add(this);
            } else if (this.isSuccessful()) {
                outs.forEach(vertex -> list.addAll(vertex.runnableVertexes()));
            }

            return list;
        }

        public boolean isSuccessful() {
            return task.isSuccessful();
        }

        public List<Task> flat() {
            if (isEndVertex()) {
                return new ArrayList<>();
            }

            List<Task> vertexes = new ArrayList<>();
            if (isTaskVertex()) {
                vertexes.add(task);
            }

            if (ValidateUtils.isNotEmpty(outs)) {
                outs.forEach(vertex -> vertexes.addAll(vertex.flat()));
            }

            return vertexes;
        }

        private boolean appendTask(Task task) {
            Vertex nextVertex = new Vertex(task);
            outs.add(nextVertex);

            nextVertex.ins.add(this);
            return true;
        }

        private void appendEndVertex(Vertex endVertex) {
            if (ValidateUtils.isEmpty(outs)) {
                outs.add(endVertex);
                endVertex.ins.add(this);
            } else {
                outs.forEach(ele -> ele.appendEndVertex(endVertex));
            }
        }

        public Optional<Vertex> find(Long id) {
           if (this.id.equals(id)) {
               return Optional.of(this);
           }

            for (Vertex out : outs) {
                Optional<Vertex> opt = out.find(id);
                if (opt.isPresent()) {
                    return opt;
                }
            }

            return Optional.empty();
        }

        public Long childrenVertexNum() {
            if (isEndVertex()) {
                return 0L;
            }

            return (isStartVertex() ? 0L : 1)
                    + getOuts().stream().mapToLong(Vertex::childrenVertexNum).sum();
        }
    }

    public enum VertexType {
        START_VERTEX,
        END_VERTEX,
        TASK_VERTEX
    }
}
