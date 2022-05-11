/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.core.producers.AsyncProducer
 *  io.vertx.core.AsyncResult
 *  io.vertx.core.Handler
 *  io.vertx.core.shareddata.Lock
 */
package io.trickle.core.producers;

import io.trickle.core.producers.AsyncProducer;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.shareddata.Lock;

public abstract class AsyncLockingProducer
extends AsyncProducer
implements Handler {
    public void handle(Object object) {
        this.handle((AsyncResult)object);
    }

    public abstract void handle();

    public void handle(AsyncResult asyncResult) {
        if (asyncResult.succeeded()) {
            Lock lock = (Lock)asyncResult.result();
            this.handle();
            lock.release();
        } else if (asyncResult.cause() != null) {
            super.fail(asyncResult.cause());
        } else {
            super.fail("Failed to get lock");
        }
    }
}
