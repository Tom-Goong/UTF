package com.yaoting.utf.infrastructure.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class GapRun {

    public static void run(LoopRun loopRun, long gapMils) {
        Preconditions.checkState(gapMils > 0, "Gap must be a positive num");
        while (!loopRun.run()) {
            try {
                Thread.sleep(gapMils);
            } catch (InterruptedException e) {
                log.error("{} run failed", loopRun.name(), e);
            }
        }
    }


    @FunctionalInterface
    public interface LoopRun {
         boolean run();

         default String name() {
             return getClass().getName();
         }
    }
}
