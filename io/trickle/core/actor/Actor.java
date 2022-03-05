/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.core.actor;

import java.util.concurrent.CompletableFuture;

public interface Actor {
    public CompletableFuture run();
}

