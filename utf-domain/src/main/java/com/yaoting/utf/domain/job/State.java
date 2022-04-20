package com.yaoting.utf.domain.job;

public enum State {
    Ready,
    Submitting,
    Submitted,
    Running,
    Frozen,      // 冻结的，任务可以被其他实例获取并处理，效果类似 Ready
    Failed,
    Succeed;

    public boolean hasFinished() {
        return this == Failed || this == Succeed;
    }
}
