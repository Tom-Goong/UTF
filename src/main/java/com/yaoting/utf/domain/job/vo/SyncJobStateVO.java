package com.yaoting.utf.domain.job.vo;

import com.yaoting.utf.domain.job.State;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SyncJobStateVO {
    private Long jobId;
    private String hostname;
    private State state;
}
