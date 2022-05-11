/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.util.concurrent.ContextCompletableFuture
 */
package io.trickle.util.concurrent;

import io.trickle.util.concurrent.ContextCompletableFuture;
import java.util.Objects;

public class VertxUtil$SignalEntry {
    public long timerId = 0L;
    public String signal;
    public ContextCompletableFuture<Object> call;

    public static VertxUtil$SignalEntry fromSignal(String string) {
        return new VertxUtil$SignalEntry(string, new ContextCompletableFuture());
    }

    public void complete() {
        this.cancelTimer();
        this.call.complete(null);
    }

    public int hashCode() {
        return Objects.hash(this.signal, this.call);
    }

    public void complete(Object object) {
        this.cancelTimer();
        this.call.complete(object);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof VertxUtil$SignalEntry)) {
            return false;
        }
        VertxUtil$SignalEntry vertxUtil$SignalEntry = (VertxUtil$SignalEntry)object;
        return this.signal.equals(vertxUtil$SignalEntry.signal) && this.call.equals(vertxUtil$SignalEntry.call);
    }

    public void cancelTimer() {
        try {
            this.call.getCtx().owner().cancelTimer(this.timerId);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public VertxUtil$SignalEntry(String string, ContextCompletableFuture contextCompletableFuture) {
        this.signal = string;
        this.call = contextCompletableFuture;
    }
}
