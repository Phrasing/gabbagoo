package io.trickle.util.concurrent;

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class ContextCompletableFuture extends CompletableFuture implements CompletionStage {
   public Context ctx;

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof ContextCompletableFuture)) {
         return false;
      } else {
         ContextCompletableFuture var2 = (ContextCompletableFuture)var1;
         return Objects.equals(this.ctx, var2.ctx);
      }
   }

   public Context getCtx() {
      return this.ctx;
   }

   public void lambda$complete$0(Object var1, Void var2) {
      super.complete(var1);
   }

   public ContextCompletableFuture() {
      this.ctx = Vertx.currentContext();
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.ctx});
   }

   public ContextCompletableFuture(Context var1) {
      this.ctx = var1;
   }

   public boolean complete(Object var1) {
      try {
         this.ctx.runOnContext(this::lambda$complete$0);
         return true;
      } catch (Exception var3) {
         return false;
      }
   }
}
