package com.yaoting.utf.domain.job;

import com.yaoting.utf.domain.common.BaseEntity;
import com.yaoting.utf.domain.job.repo.JobRepo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yaoting.utf.infrastructure.utils.Preconditions;
import com.yaoting.utf.infrastructure.utils.SpringTool;
import com.yaoting.utf.infrastructure.utils.ValidateUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class Job extends BaseEntity {

    private String name;

    private String owner;

    private String callBackUrl;
    /**
     * 自动执行
     */
    private boolean autoExec;

    private State state = State.Ready;

    private String description;

    private List<Task> tasks = new ArrayList<>();

    public void addJob() {
        persist();
    }

    public void update() {
        persist();
    }

    public boolean toState(State state) {
        State old = this.state;

        this.state = state;
        return SpringTool.getBean(JobRepo.class).updateStateWhen(this, old);
    }

    public boolean isStateOf(State state) {
        return getState() == state;
    }

    @JsonIgnore
    public DAG getTasksDAG() {
        if (ValidateUtils.isEmpty(tasks)) {
            throw new IllegalStateException("Brief Job or Empty-Job can't build DAG, Please Do check, jobId: " + getId());
        }

        DAG dag = new DAG(this);

        if (ValidateUtils.isNotEmpty(tasks)) {
            Map<Long, List<Task>> tasksGrouped = tasks.stream()
                    .collect(Collectors.groupingBy(Task::getPreTask));

            pushTasks(tasksGrouped.get(DAG.START_VERTEX_ID), tasksGrouped, dag);

            // 如果有任何 task 任务不是 ready 状态，则说明之前运行过，task 节点应该是成功的
            if (getTasks()
                    .stream()
                    .anyMatch(task -> !task.isStateOf(State.Ready))) {
                dag.getStartVertex().getTask().toState(State.Succeed);
            }
        }

        Long vertexNum = dag.vertexNum();
        Preconditions.checkState(tasks.size() == vertexNum, String.format("拓扑图结构异常, 实际任务数: %s, 拓扑图任务数：%s", tasks.size(), vertexNum));

        return dag.stable();
    }

    private void pushTasks(List<Task> tasks, Map<Long, List<Task>> tasksGrouped, DAG dag) {
        if (ValidateUtils.isEmpty(tasks)) {
            return;
        }

        for (Task task : tasks) {
            dag.pushTask(task);

            // 处理下一层
            pushTasks(tasksGrouped.get(task.getId()), tasksGrouped, dag);
        }
    }

    @Override
    protected void persist() {
        JobRepo repo = SpringTool.getBean(JobRepo.class);
        repo.persist(this);
    }
}
