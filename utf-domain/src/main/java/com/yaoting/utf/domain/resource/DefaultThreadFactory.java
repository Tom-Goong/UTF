package com.yaoting.utf.domain.resource;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultThreadFactory implements ThreadFactory {
    private static final AtomicInteger POOL_ID = new AtomicInteger();
    private final AtomicInteger        nextId;
    private final String               prefix;
    private final boolean              daemon;
    private final int                  priority;

    public DefaultThreadFactory(String poolName) {
        this(poolName, false, 5);
    }

    public DefaultThreadFactory(String poolName, boolean daemon, int priority) {
        this.nextId = new AtomicInteger();
        if (poolName == null) {
            throw new NullPointerException("poolName");
        } else if (priority >= 1 && priority <= 10) {
            this.prefix = poolName + '-' + POOL_ID.incrementAndGet() + '-';
            this.daemon = daemon;
            this.priority = priority;
        } else {
            throw new IllegalArgumentException(
                "priority: " + priority
                        + " (expected: Thread.MIN_PRIORITY <= priority <= Thread.MAX_PRIORITY)");
        }
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, this.prefix + this.nextId.incrementAndGet());
        try {
            if (t.isDaemon()) {
                if (!this.daemon) {
                    t.setDaemon(false);
                }
            } else if (this.daemon) {
                t.setDaemon(true);
            }

            if (t.getPriority() != this.priority) {
                t.setPriority(this.priority);
            }
        } catch (Exception e) {
            //ignore
        }
        return t;
    }
}
