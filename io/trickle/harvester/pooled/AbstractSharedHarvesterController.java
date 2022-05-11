package io.trickle.harvester.pooled;

import io.vertx.core.Vertx;
import io.vertx.core.shareddata.Lock;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractSharedHarvesterController {
   public AtomicInteger counter = new AtomicInteger(0);
   public String identity = "HARVESTER_MANAGER_SHARED_LOCK_" + UUID.randomUUID();
   public static Logger logger = LogManager.getLogger(AbstractSharedHarvesterController.class);
   public List harvesters = new ArrayList();

   public String allocate() {
      int var1 = this.harvesters.size();
      if (var1 == 0) {
         throw new Exception("No harvesters available. Did you configure the harvester count correctly?");
      } else {
         return var1 == 1 ? ((SharedHarvester)this.harvesters.get(0)).id() : ((SharedHarvester)this.harvesters.get(this.counter.getAndUpdate(AbstractSharedHarvesterController::lambda$allocate$0))).id();
      }
   }

   public abstract Optional shouldSwap(String var1);

   public CompletableFuture start() {
      try {
         CompletableFuture var10000 = Vertx.currentContext().owner().sharedData().getLocalLockWithTimeout(this.identity, TimeUnit.HOURS.toMillis(1L)).toCompletionStage().toCompletableFuture();
         CompletableFuture var4;
         if (!var10000.isDone()) {
            var4 = var10000;
            return var4.exceptionally(Function.identity()).thenCompose(AbstractSharedHarvesterController::async$start);
         }

         Lock var1 = (Lock)var10000.join();

         try {
            logger.info("Waiting to start!");
            var10000 = this.initialise();
            if (!var10000.isDone()) {
               var4 = var10000;
               return var4.exceptionally(Function.identity()).thenCompose(AbstractSharedHarvesterController::async$start);
            }

            var10000.join();
            logger.info("Started!");
         } catch (Throwable var8) {
            logger.error("Start error on harvester controller {}", var8.getMessage());
         } finally {
            var1.release();
         }
      } catch (Throwable var10) {
         logger.error("Lock error on harvester controller {}", var10.getMessage());
      }

      return CompletableFuture.completedFuture(true);
   }

   public abstract CompletableFuture initialise();

   public static CompletableFuture async$start(AbstractSharedHarvesterController param0, CompletableFuture param1, Lock param2, int param3, Object param4) {
      // $FF: Couldn't be decompiled
   }

   public static int lambda$allocate$0(int var0, int var1) {
      ++var1;
      return var1 < var0 ? var1 : 0;
   }
}
