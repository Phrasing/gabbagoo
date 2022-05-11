package io.trickle.harvester;

import com.teamdev.jxbrowser.js.JsAccessible;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class Harvester$SolveFunction {
   public AtomicReference latch;
   public SolveFuture callback;

   public Harvester$SolveFunction(SolveFuture var1, AtomicReference var2) {
      this.callback = var1;
      this.latch = var2;
   }

   @JsAccessible
   public void completed(String var1) {
      CaptchaToken var2 = this.callback.getEmptyCaptchaToken();
      if (var2 != null) {
         var2.setTokenValues(var1);
         SolveFuture var3 = this.callback;
         if (var3 != null && !var3.isDone()) {
            Harvester.logger.info("Received valid token [V2][CALLBACK]");
            var3.complete(var2);
            ((CountDownLatch)this.latch.get()).countDown();
            this.callback.imageFuture.complete((Object)null);
         }
      }

   }
}
