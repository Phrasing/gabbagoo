package io.trickle.core.producers;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.shareddata.Lock;

public abstract class AsyncLockingProducer extends AsyncProducer implements Handler {
   public void handle(Object var1) {
      this.handle((AsyncResult)var1);
   }

   public abstract void handle();

   public void handle(AsyncResult var1) {
      if (var1.succeeded()) {
         Lock var2 = (Lock)var1.result();
         this.handle();
         var2.release();
      } else if (var1.cause() != null) {
         super.fail(var1.cause());
      } else {
         super.fail("Failed to get lock");
      }

   }
}
