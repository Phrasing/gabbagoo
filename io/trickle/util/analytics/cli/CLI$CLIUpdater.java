/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.AbstractVerticle
 */
package io.trickle.util.analytics.cli;

import io.trickle.util.analytics.Analytics;
import io.trickle.util.analytics.cli.CLI;
import io.vertx.core.AbstractVerticle;

public class CLI$CLIUpdater
extends AbstractVerticle {
    public long timerID;

    public void start() {
        this.timerID = this.vertx.setPeriodic(2000L, this::run);
    }

    public void run(long l) {
        CLI.setTitle(String.format("Carts %d || Checkouts %d || Fails %d", Analytics.carts.get(), Analytics.success.get(), Analytics.fails.get()));
    }

    public void stop() {
        this.vertx.cancelTimer(this.timerID);
    }
}

