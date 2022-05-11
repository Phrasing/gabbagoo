/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.util.internal.PlatformDependent
 *  io.trickle.util.analytics.Analytics
 *  io.vertx.core.AbstractVerticle
 *  io.vertx.core.buffer.Buffer
 */
package io.trickle.util.analytics.cli;

import io.netty.util.internal.PlatformDependent;
import io.trickle.util.analytics.Analytics;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;

public class CLIMonitor
extends AbstractVerticle {
    public long timerID;

    public static void lambda$run$0(Throwable throwable) {
    }

    public void stop() {
        this.vertx.cancelTimer(this.timerID);
    }

    public String version() {
        return String.format("%d.%d.%d", 1, 0, 278);
    }

    public void start() {
        this.timerID = this.vertx.setPeriodic(4500L, this::run);
        this.run(-1L);
    }

    public void run(long l) {
        long l2 = Analytics.carts.sum();
        long l3 = Analytics.success.sum();
        long l4 = Analytics.fails.sum();
        long l5 = Analytics.queuePasses.sum();
        long l6 = Analytics.fakePasses.sum();
        String string = l5 == 0L && l6 == 0L ? String.format("Trickle %s || Carts %d || Checkouts %d || Fails %d", this.version(), l2, l3, l4) : (l5 == 0L ? String.format("Trickle %s || Carts %d || Checkouts %d || Fails %d || Fake Passes %d", this.version(), l2, l3, l4, l6) : (l6 == 0L ? String.format("Trickle %s || Carts %d || Checkouts %d || Fails %d || Queue Passes %d", this.version(), l2, l3, l4, l5) : String.format("Trickle %s || Carts %d || Checkouts %d || Fails %d || Queue Passes %d || Fake Passes %d", this.version(), l2, l3, l4, l5, l6)));
        this.vertx.fileSystem().writeFile(PlatformDependent.tmpdir().getPath() + "/.jp/state.lib", Buffer.buffer((String)string)).onFailure(CLIMonitor::lambda$run$0);
    }
}
