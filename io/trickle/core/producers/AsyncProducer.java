/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.Future
 *  io.vertx.core.Promise
 */
package io.trickle.core.producers;

import io.vertx.core.Future;
import io.vertx.core.Promise;

public abstract class AsyncProducer {
    public Promise<T> callback = Promise.promise();

    public void produce(Object object) {
        this.callback.complete(object);
    }

    public void fail(String string) {
        this.callback.fail(string);
    }

    public Future getProduct() {
        return this.callback.future();
    }

    public void fail(Throwable throwable) {
        this.callback.fail(throwable);
    }
}

