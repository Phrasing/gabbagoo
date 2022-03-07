/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.util.concurrent;

import io.trickle.util.concurrent.ContextCompletableFuture;
import java.util.Objects;

public class VertxUtil$SignalEntry {
    public ContextCompletableFuture<String> call;
    public String signal;
    public long timerId = 0L;

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof VertxUtil$SignalEntry)) {
            return false;
        }
        VertxUtil$SignalEntry vertxUtil$SignalEntry = (VertxUtil$SignalEntry)object;
        if (!this.signal.equals(vertxUtil$SignalEntry.signal)) return false;
        if (!this.call.equals(vertxUtil$SignalEntry.call)) return false;
        return true;
    }

    public void cancelTimer() {
        try {
            this.call.getCtx().owner().cancelTimer(this.timerId);
            return;
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public static VertxUtil$SignalEntry fromSignal(String string) {
        return new VertxUtil$SignalEntry(string, new ContextCompletableFuture());
    }

    public int hashCode() {
        return Objects.hash(this.signal, this.call);
    }

    public void complete() {
        this.cancelTimer();
        this.call.complete(null);
    }

    public void complete(String string) {
        this.cancelTimer();
        this.call.complete((Object)string);
    }

    public VertxUtil$SignalEntry(String string, ContextCompletableFuture contextCompletableFuture) {
        this.signal = string;
        this.call = contextCompletableFuture;
    }
}

