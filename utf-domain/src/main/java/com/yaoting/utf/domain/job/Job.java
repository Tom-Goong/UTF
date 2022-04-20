package com.yaoting.utf.domain.job;

import com.yaoting.utf.common.utils.Preconditions;
import com.yaoting.utf.common.utils.SpringTool;
import com.yaoting.utf.common.utils.ValidateUtils;
import com.yaoting.utf.domain.common.entity.BaseEntity;
import com.yaoting.utf.domain.dag.DAG;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yaoting.utf.domain.job.task.Task;
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

            if (getTasks()
                    .stream()
                    .anyMatch(task -> !task.isStateOf(State.Ready))) {
                dag.getStartVertex().getTask().toState(State.Succeed);
            }
        }

        Long vertexNum = dag.vertexNum();
        Preconditions.checkState(tasks.size() == vertexNum, String.format("DAG structured failed, real num of task : %s, num of task in topology：%s", tasks.size(), vertexNum));

        return dag.stable();
    }

    private void pushTasks(List<Task> tasks, Map<Long, List<Task>> tasksGrouped, DAG dag) {
        if (ValidateUtils.isEmpty(tasks)) {
            return;
        }

        for (Task task : tasks) {
            dag.pushTask(task);

            // handle next level
            pushTasks(tasksGrouped.get(task.getId()), tasksGrouped, dag);
        }
    }

    @Override
    public void persist() {
        JobRepo repo = SpringTool.getBean(JobRepo.class);
        repo.persist(this);
    }
}
