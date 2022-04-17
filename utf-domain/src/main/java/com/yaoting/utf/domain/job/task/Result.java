package com.yaoting.utf.domain.job.task;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Result<T> {

    private Long jobId;

    private Long taskId;

    private Boolean isSucceed = true;

    private T data;

    private String errMsg;

    public Result(Long jobId, Long taskId) {
        this.jobId = jobId;
        this.taskId = taskId;
    }

    public static Result<Void> voidData(Long jobId, Long taskId) {
        return new Result<Void>(jobId, taskId)
                .setData(null);
    }

    @SuppressWarnings("rawtypes")
    public static <T> Result<T> of(Long jobId, Long taskId, T date) {
        return new Result(jobId, taskId)
                .setData(date);
    }

    @SuppressWarnings("rawtypes")
    public static <T> Result<T> failed(Long jobId, Long taskId, String errMsg) {
        return new Result(jobId, taskId)
                .setIsSucceed(false)
                .setErrMsg(errMsg);
    }
}
