package io.trickle.harvester.pooled;

import io.vertx.core.AsyncResult;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BrowserHarvesterController extends AbstractSharedHarvesterController {
   public static Predicate removePredicate = BrowserHarvesterController::lambda$static$0;
   public Vertx vertx;
   public int count;
   public List passedHarvesters;
   public static Logger logger = LogManager.getLogger(BrowserHarvesterController.class);
   public AtomicInteger passedCounter;

   public Optional shouldSwap(String var1) {
      if (!this.passedHarvesters.isEmpty()) {
         try {
            Iterator var2 = this.passedHarvesters.iterator();

            while(var2.hasNext()) {
               SharedHarvester var3 = (SharedHarvester)var2.next();
               if (var3.id().equalsIgnoreCase(var1)) {
                  return Optional.empty();
               }
            }
         } catch (Throwable var4) {
            logger.warn("Error occurred on distribution: {}", var4.getMessage());
         }
      }

      int var5 = this.passedHarvesters.size();
      return var5 == 0 ? Optional.empty() : Optional.of(((SharedHarvester)this.passedHarvesters.get(this.passedCounter.getAndUpdate(BrowserHarvesterController::lambda$shouldSwap$2))).id());
   }

   public static boolean lambda$static$0(SharedHarvester var0) {
      return (long)var0.passCount() <= 0L;
   }

   public BrowserHarvesterController(Vertx var1, int var2) {
      this.count = var2;
      this.vertx = var1;
      this.passedCounter = new AtomicInteger(0);
      this.passedHarvesters = new ArrayList();
      this.startCounter();
   }

   public static int lambda$shouldSwap$2(int var0, int var1) {
      ++var1;
      return var1 < var0 ? var1 : 0;
   }

   public CompletableFuture initHarvesters() {
      if (this.count == this.harvesters.size()) {
         return CompletableFuture.completedFuture((Object)null);
      } else {
         int var1 = this.count - this.harvesters.size();
         logger.info("Initialising {} harvesters", var1);

         for(int var2 = 0; var2 < var1; ++var2) {
            try {
               BrowserSharedHarvester var3 = new BrowserSharedHarvester(var2);
               this.harvesters.add(var3);
               CompletableFuture var4 = this.vertx.deployVerticle(var3).onComplete(this::lambda$initHarvesters$3).toCompletionStage().toCompletableFuture();
               logger.info("Waiting for harvester[{}] to start...", var2);
               if (!var4.isDone()) {
                  return var4.exceptionally(Function.identity()).thenCompose(BrowserHarvesterController::async$initHarvesters);
               }

               var4.join();
            } catch (Throwable var6) {
               logger.warn("Error occurred on initialisation stage: {}", var6.getMessage());
               return CompletableFuture.failedFuture(var6);
            }
         }

         return CompletableFuture.completedFuture((Object)null);
      }
   }

   public void lambda$initHarvesters$3(BrowserSharedHarvester var1, AsyncResult var2) {
      if (var2.succeeded()) {
         logger.info("Harvester deployed with worker id: {}", var2.result());
      } else {
         logger.warn("Failed to launch harvester: {}", var2.cause().getMessage());

         try {
            this.harvesters.remove(var1);
            var1.stop(Promise.promise());
         } catch (Throwable var4) {
            logger.warn("Error cleaning up staled harvester: {}", var4.getMessage());
         }
      }

   }

   public void lambda$startCounter$1(Long var1) {
      try {
         this.passedHarvesters.removeIf(removePredicate);
         Iterator var2 = super.harvesters.iterator();

         while(var2.hasNext()) {
            SharedHarvester var3 = (SharedHarvester)var2.next();
            if (var3.passCount() > 0 && !this.passedHarvesters.contains(var3)) {
               this.passedHarvesters.add(var3);
            }
         }
      } catch (Throwable var4) {
         logger.error("Count assigment error: {}", var4.getMessage());
      }

   }

   public static CompletableFuture async$initHarvesters(BrowserHarvesterController var0, int var1, int var2, BrowserSharedHarvester var3, CompletableFuture var4, CompletableFuture var5, int var6, Object var7) {
      Throwable var12;
      label53: {
         CompletableFuture var10000;
         boolean var10001;
         switch (var6) {
            case 0:
               if (var0.count == var0.harvesters.size()) {
                  return CompletableFuture.completedFuture((Object)null);
               }

               var1 = var0.count - var0.harvesters.size();
               logger.info("Initialising {} harvesters", var1);
               var2 = 0;
               break;
            case 1:
               var10000 = var5;

               try {
                  var10000.join();
               } catch (Throwable var10) {
                  var12 = var10;
                  var10001 = false;
                  break label53;
               }

               ++var2;
               break;
            default:
               throw new IllegalArgumentException();
         }

         for(; var2 < var1; ++var2) {
            try {
               var3 = new BrowserSharedHarvester(var2);
               var0.harvesters.add(var3);
               var4 = var0.vertx.deployVerticle(var3).onComplete(var0::lambda$initHarvesters$3).toCompletionStage().toCompletableFuture();
               logger.info("Waiting for harvester[{}] to start...", var2);
               var10000 = var4;
               if (!var4.isDone()) {
                  return var4.exceptionally(Function.identity()).thenCompose(BrowserHarvesterController::async$initHarvesters);
               }
            } catch (Throwable var9) {
               var12 = var9;
               var10001 = false;
               break label53;
            }

            try {
               var10000.join();
            } catch (Throwable var8) {
               var12 = var8;
               var10001 = false;
               break label53;
            }
         }

         return CompletableFuture.completedFuture((Object)null);
      }

      Throwable var11 = var12;
      logger.warn("Error occurred on initialisation stage: {}", var11.getMessage());
      return CompletableFuture.failedFuture(var11);
   }

   public void startCounter() {
      this.vertx.setPeriodic(5000L, this::lambda$startCounter$1);
   }

   public CompletableFuture initialise() {
      logger.info("Waiting to start!");
      return this.initHarvesters();
   }
}
