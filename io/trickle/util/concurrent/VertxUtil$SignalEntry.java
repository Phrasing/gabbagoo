package io.trickle.util.concurrent;

import java.util.Objects;

public class VertxUtil$SignalEntry {
   public long timerId = 0L;
   public String signal;
   public ContextCompletableFuture call;

   public static VertxUtil$SignalEntry fromSignal(String var0) {
      return new VertxUtil$SignalEntry(var0, new ContextCompletableFuture());
   }

   public void complete() {
      this.cancelTimer();
      this.call.complete((Object)null);
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.signal, this.call});
   }

   public void complete(Object var1) {
      this.cancelTimer();
      this.call.complete(var1);
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof VertxUtil$SignalEntry)) {
         return false;
      } else {
         VertxUtil$SignalEntry var2 = (VertxUtil$SignalEntry)var1;
         return this.signal.equals(var2.signal) && this.call.equals(var2.call);
      }
   }

   public void cancelTimer() {
      try {
         this.call.getCtx().owner().cancelTimer(this.timerId);
      } catch (Throwable var2) {
      }

   }

   public VertxUtil$SignalEntry(String var1, ContextCompletableFuture var2) {
      this.signal = var1;
      this.call = var2;
   }
}
