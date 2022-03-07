/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.Context
 *  io.vertx.core.Vertx
 */
package io.trickle.util.concurrent;

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class ContextCompletableFuture
extends CompletableFuture
implements CompletionStage {
    public Context ctx;

    public ContextCompletableFuture(Context context) {
        this.ctx = context;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof ContextCompletableFuture)) {
            return false;
        }
        ContextCompletableFuture contextCompletableFuture = (ContextCompletableFuture)object;
        return Objects.equals(this.ctx, contextCompletableFuture.ctx);
    }

    public Context getCtx() {
        return this.ctx;
    }

    public void lambda$complete$0(Object object, Void void_) {
        super.complete(object);
    }

    public int hashCode() {
        return Objects.hash(this.ctx);
    }

    public boolean complete(Object object) {
        try {
            this.ctx.runOnContext(arg_0 -> this.lambda$complete$0(object, arg_0));
            return true;
        }
        catch (Exception exception) {
            return false;
        }
    }

    public ContextCompletableFuture() {
        this.ctx = Vertx.currentContext();
    }
}

