package com.yaoting.utf.domain.resource;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool {
    private static final ExecutorService JOB_BOSS_POOL = new ThreadPoolExecutor(
            8,
            8,
            120L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(),
            new DefaultThreadFactory("Job-Boss-Thread")
    );

    public static void submitBossThread(Runnable task) {
        JOB_BOSS_POOL.submit(task);
    }


    private static final ExecutorService JOB_WORK_POOL = new ThreadPoolExecutor(
            32,
            32,
            120L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(),
            new DefaultThreadFactory("Job-Work-Thread")
    );

    public static void submitWorkThread(Runnable task) {
        JOB_WORK_POOL.submit(task);
    }
}
