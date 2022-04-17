package com.yaoting.utf.common.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class GapRun {

    public static void loopRun(GapRunnable gapRunnable, long gapMils) {
        Preconditions.checkState(gapMils > 0, "Gap must be a positive num");
        while (!gapRunnable.run()) {
            try {
                Thread.sleep(gapMils);
            } catch (InterruptedException e) {
                log.error("{} run failed", gapRunnable.name(), e);
            }
        }
    }


    @FunctionalInterface
    public interface GapRunnable {
         boolean run();

         default String name() {
             return getClass().getName();
         }
    }
}
