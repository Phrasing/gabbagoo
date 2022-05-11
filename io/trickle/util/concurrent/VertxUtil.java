package io.trickle.util.concurrent;

import io.trickle.core.VertxSingleton;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class VertxUtil {
   public static Set SIGNAL_ENTRIES = ConcurrentHashMap.newKeySet();

   public static CompletableFuture randomSignalSleep(String var0, long var1) {
      if (var1 > 1L) {
         long var3 = ThreadLocalRandom.current().nextLong(var1, Math.round((double)var1 * Double.longBitsToDouble(4609434218613702656L)));
         return signalSleep(var0, var3);
      } else {
         return CompletableFuture.completedFuture((Object)null);
      }
   }

   public static CompletableFuture handleEagerFuture(CompletableFuture var0) {
      if (!var0.isDone()) {
         return var0.exceptionally(Function.identity()).thenCompose(VertxUtil::async$handleEagerFuture);
      } else {
         var0.join();
         CompletableFuture var10000 = hardCodedSleep(10L);
         if (!var10000.isDone()) {
            CompletableFuture var1 = var10000;
            return var1.exceptionally(Function.identity()).thenCompose(VertxUtil::async$handleEagerFuture);
         } else {
            var10000.join();
            return CompletableFuture.completedFuture((Object)null);
         }
      }
   }

   public static CompletableFuture hardCodedSleep(long var0) {
      if (var0 > 1L) {
         CompletableFuture var2 = new CompletableFuture();
         VertxSingleton.INSTANCE.get().setTimer(var0, VertxUtil::lambda$hardCodedSleep$1);
         return var2;
      } else {
         return CompletableFuture.completedFuture((Object)null);
      }
   }

   public static CompletableFuture toStage(Future var0) {
      return var0.toCompletionStage().toCompletableFuture();
   }

   public static void lambda$signalSleep$2(VertxUtil$SignalEntry var0, Long var1) {
      var0.complete();
      SIGNAL_ENTRIES.remove(var0);
   }

   public static void sendSignal() {
      try {
         Iterator var0 = SIGNAL_ENTRIES.iterator();

         while(var0.hasNext()) {
            VertxUtil$SignalEntry var1 = (VertxUtil$SignalEntry)var0.next();
            if (!var1.call.isDone()) {
               var1.complete();
               var0.remove();
            }
         }
      } catch (Throwable var2) {
      }

   }

   public static CompletableFuture async$handleEagerFuture(CompletableFuture var0, CompletableFuture var1, int var2, Object var3) {
      CompletableFuture var10000;
      label18: {
         switch (var2) {
            case 0:
               var10000 = var0;
               if (!var0.isDone()) {
                  return var0.exceptionally(Function.identity()).thenCompose(VertxUtil::async$handleEagerFuture);
               }
               break;
            case 1:
               var10000 = var1;
               break;
            case 2:
               var10000 = var1;
               break label18;
            default:
               throw new IllegalArgumentException();
         }

         var10000.join();
         var10000 = hardCodedSleep(10L);
         if (!var10000.isDone()) {
            var1 = var10000;
            return var1.exceptionally(Function.identity()).thenCompose(VertxUtil::async$handleEagerFuture);
         }
      }

      var10000.join();
      return CompletableFuture.completedFuture((Object)null);
   }

   public static void sendSignal(String var0) {
      try {
         Iterator var1 = SIGNAL_ENTRIES.iterator();

         while(var1.hasNext()) {
            VertxUtil$SignalEntry var2 = (VertxUtil$SignalEntry)var1.next();
            if (!var2.call.isDone() && var2.signal.equals(var0)) {
               var2.complete();
               var1.remove();
            }
         }
      } catch (Throwable var3) {
      }

   }

   public static CompletableFuture sleep(long var0) {
      return signalSleep("massChange", var0);
   }

   public static CompletableFuture yield() {
      CompletableFuture var0 = new CompletableFuture();
      Vertx.currentContext().runOnContext(VertxUtil::lambda$yield$0);
      return var0;
   }

   public static CompletableFuture signalSleep(String var0, long var1) {
      if (var1 > 1L) {
         VertxUtil$SignalEntry var3 = VertxUtil$SignalEntry.fromSignal(var0);

         try {
            var3.timerId = var3.call.getCtx().owner().setTimer(var1, VertxUtil::lambda$signalSleep$2);
            SIGNAL_ENTRIES.add(var3);
         } catch (Throwable var5) {
            return CompletableFuture.completedFuture((Object)null);
         }

         return var3.call;
      } else {
         return CompletableFuture.completedFuture((Object)null);
      }
   }

   public static void lambda$yield$0(CompletableFuture var0, Void var1) {
      var0.complete((Object)null);
   }

   public static void lambda$hardCodedSleep$1(CompletableFuture var0, Long var1) {
      var0.complete((Object)null);
   }

   public static void sendSignal(String var0, Object var1) {
      try {
         Iterator var2 = SIGNAL_ENTRIES.iterator();

         while(var2.hasNext()) {
            VertxUtil$SignalEntry var3 = (VertxUtil$SignalEntry)var2.next();
            if (!var3.call.isDone() && var3.signal.equals(var0)) {
               var3.complete(var1);
               var2.remove();
            }
         }
      } catch (Throwable var4) {
      }

   }

   public static CompletableFuture randomSleep(long var0) {
      return randomSignalSleep("massChange", var0);
   }

   public static CompletableFuture lock(String var0) {
      return toStage(Vertx.currentContext().owner().sharedData().getLock(var0));
   }
}
