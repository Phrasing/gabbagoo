/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.Verticle
 */
package io.trickle.util.analytics.cli;

import io.trickle.core.VertxSingleton;
import io.trickle.util.analytics.cli.CLI$CLIUpdater;
import io.vertx.core.Verticle;

public class CLI {
    public static boolean LOAD_GATE = true;
    public static String listenerID = null;

    public static void lambda$deployObserver$0(String string) {
        listenerID = string;
    }

    public static void setTitle(String string) {
    }

    public static void destroyObserver() {
        VertxSingleton.INSTANCE.get().undeploy(listenerID);
    }

    public static void deployObserver() {
        VertxSingleton.INSTANCE.get().deployVerticle((Verticle)new CLI$CLIUpdater()).onSuccess(CLI::lambda$deployObserver$0);
    }
}

