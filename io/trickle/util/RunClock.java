/*
 * Decompiled with CFR 0.152.
 */
package io.trickle.util;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

public class RunClock {
    public long timeTillRun;
    public boolean stopped;
    public long finishTime;

    public long getTimeTillRun() {
        return this.timeTillRun;
    }

    public RunClock() {
        LocalTime localTime = LocalTime.now();
        int n = localTime.getMinute();
        this.timeTillRun = n == 45 || n == 15 ? 2L : (n > 45 ? TimeUnit.MINUTES.toMillis(74 - n) : (n < 15 ? TimeUnit.MINUTES.toMillis(14 - n) : TimeUnit.MINUTES.toMillis(44 - n)));
        this.stopped = true;
        this.finishTime = 0L;
    }

    public boolean isStopped() {
        if (this.stopped) return this.stopped;
        if (System.currentTimeMillis() < this.finishTime) return this.stopped;
        this.stopped = true;
        return this.stopped;
    }

    public void start() {
        this.finishTime = System.currentTimeMillis() + 300000L;
        this.stopped = false;
    }

    public static RunClock create() {
        return new RunClock();
    }
}
