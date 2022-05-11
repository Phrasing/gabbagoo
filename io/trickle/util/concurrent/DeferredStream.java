/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.util.concurrent.DeferredStream$Priority
 *  io.vertx.core.Context
 *  io.vertx.core.Handler
 *  io.vertx.core.Vertx
 *  io.vertx.core.streams.ReadStream
 *  io.vertx.core.streams.StreamBase
 */
package io.trickle.util.concurrent;

import io.trickle.util.concurrent.DeferredStream;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.streams.ReadStream;
import io.vertx.core.streams.StreamBase;
import java.util.Objects;

/*
 * Duplicate member names - consider using --renamedupmembers true
 * Exception performing whole class analysis ignored.
 */
public class DeferredStream
implements ReadStream {
    public boolean stopped;
    public Context context;
    public int priority;
    public ReadStream<T> stream;

    public void lambda$handler$0(Handler handler, Object object) {
        handler.handle(object);
        if (this.stopped) return;
        this.scheduleNext();
    }

    public ReadStream pause() {
        this.stopped = true;
        return this.stream.pause();
    }

    public ReadStream fetch(long l) {
        return this.fetch(l);
    }

    public ReadStream handler(Handler handler) {
        return this.stream.handler(arg_0 -> this.lambda$handler$0(handler, arg_0));
    }

    public DeferredStream(ReadStream readStream) {
        this(readStream, Vertx.currentContext());
    }

    public ReadStream exceptionHandler(Handler handler) {
        return this.stream.exceptionHandler(handler);
    }

    public DeferredStream lazyFetch() {
        this.stopped = false;
        this.scheduleNext();
        return this;
    }

    public ReadStream endHandler(Handler handler) {
        return this.stream.endHandler(handler);
    }

    public ReadStream resume() {
        this.stopped = false;
        this.scheduleNext();
        return this;
    }

    public DeferredStream fetch(long l) {
        this.stopped = false;
        this.stream.fetch(l);
        return this;
    }

    public DeferredStream priority(Priority priority) {
        this.priority(Priority.convert((Priority)priority));
        return this;
    }

    public void lambda$scheduleNext$1(Void void_) {
        this.stream.fetch((long)this.priority);
    }

    public StreamBase exceptionHandler(Handler handler) {
        return this.exceptionHandler(handler);
    }

    public DeferredStream(ReadStream readStream, Context context) {
        Objects.requireNonNull(context, "Must be on Vert.x context");
        Objects.requireNonNull(readStream, "Stream can not be null");
        this.stream = readStream;
        this.stream.pause();
        this.context = context;
        this.priority = Priority.convert((Priority)Priority.MEDIUM);
        this.stopped = false;
    }

    public void scheduleNext() {
        this.context.runOnContext(this::lambda$scheduleNext$1);
    }

    public DeferredStream priority(int n) {
        this.priority = n;
        return this;
    }
}
