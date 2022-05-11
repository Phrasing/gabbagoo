package io.trickle.core.producers;

import io.vertx.core.Future;
import io.vertx.core.Promise;

public abstract class AsyncProducer {
   public Promise callback = Promise.promise();

   public Future getProduct() {
      return this.callback.future();
   }

   public void fail(Throwable var1) {
      this.callback.fail(var1);
   }

   public void fail(String var1) {
      this.callback.fail(var1);
   }

   public void produce(Object var1) {
      this.callback.complete(var1);
   }
}
