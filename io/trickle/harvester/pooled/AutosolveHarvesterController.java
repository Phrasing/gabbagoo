package io.trickle.harvester.pooled;

import com.fuzzy.aycd.autosolve.AbstractAutoSolveManager;
import com.fuzzy.aycd.autosolve.model.AutoSolveAccount;
import io.trickle.harvester.autosolve.AutoSolve;
import io.trickle.util.Storage;
import io.trickle.util.concurrent.ContextCompletableFuture;
import io.vertx.core.Vertx;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import okhttp3.OkHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AutosolveHarvesterController extends AbstractSharedHarvesterController {
   public Vertx vertx;
   public static boolean $assertionsDisabled = !AutosolveHarvesterController.class.desiredAssertionStatus();
   public static Logger logger = LogManager.getLogger(AutosolveHarvesterController.class);
   public AtomicInteger c = new AtomicInteger(0);
   public static int SHARED_TOKEN_RATIO = 500;
   public ContextCompletableFuture startFuture;
   public AbstractAutoSolveManager autoSolve;

   public void addNewHarvester() {
      AutosolveHarvester var1 = new AutosolveHarvester(this.vertx, this.autoSolve);
      this.vertx.eventBus().localConsumer(var1.id(), var1);
      this.harvesters.add(var1);
      logger.info("Added new autosolve harvester: {}", this.harvesters.size());
   }

   public CompletableFuture initAutosolve() {
      if (this.autoSolve.isActive()) {
         return CompletableFuture.completedFuture((Object)null);
      } else {
         logger.info("Connecting to AutoSolve");
         if (this.startFuture == null || this.startFuture.isDone()) {
            this.startFuture = new ContextCompletableFuture();
            this.connect();
            ContextCompletableFuture var10000 = this.startFuture;
            if (!var10000.toCompletableFuture().isDone()) {
               ContextCompletableFuture var1 = var10000;
               return var1.exceptionally(Function.identity()).thenCompose(AutosolveHarvesterController::async$initAutosolve).toCompletableFuture();
            }

            var10000.toCompletableFuture().join();
            logger.info("Connected Successfully");
         }

         return CompletableFuture.completedFuture((Object)null);
      }
   }

   public Optional shouldSwap(String var1) {
      return Optional.empty();
   }

   public static CompletableFuture async$initAutosolve(AutosolveHarvesterController var0, ContextCompletableFuture var1, int var2, Object var3) {
      ContextCompletableFuture var10000;
      switch (var2) {
         case 0:
            if (var0.autoSolve.isActive()) {
               return CompletableFuture.completedFuture((Object)null);
            }

            logger.info("Connecting to AutoSolve");
            if (var0.startFuture != null && !var0.startFuture.isDone()) {
               return CompletableFuture.completedFuture((Object)null);
            }

            var0.startFuture = new ContextCompletableFuture();
            var0.connect();
            var10000 = var0.startFuture;
            if (!var10000.toCompletableFuture().isDone()) {
               var1 = var10000;
               return var1.exceptionally(Function.identity()).thenCompose(AutosolveHarvesterController::async$initAutosolve).toCompletableFuture();
            }
            break;
         case 1:
            var10000 = var1;
            break;
         default:
            throw new IllegalArgumentException();
      }

      var10000.toCompletableFuture().join();
      logger.info("Connected Successfully");
      return CompletableFuture.completedFuture((Object)null);
   }

   public void lambda$connect$0(String var1, String var2) {
      AutoSolveAccount var3 = AutoSolveAccount.of(var1, var2);
      if (!$assertionsDisabled && !var3.isValid()) {
         throw new AssertionError();
      } else {
         for(int var4 = 0; var4 < 10; ++var4) {
            try {
               logger.info("Connecting AutoSolve...");
               this.autoSolve.load(var3);
               if (this.autoSolve.isConnected()) {
                  this.startFuture.complete((Object)null);
                  return;
               }

               Thread.sleep(3000L);
            } catch (Throwable var6) {
               logger.error("Failed to connect to autosolve: {}", var6.getMessage());
            }
         }

         logger.warn("Failed to connect to AutoSolve");
         this.startFuture.completeExceptionally(new Exception("Failed to connect to AutoSolve"));
         System.exit(-1);
      }
   }

   public CompletableFuture initialise() {
      if (this.c.getAndIncrement() % 500 == 0) {
         this.addNewHarvester();
      }

      return this.initAutosolve();
   }

   public AutosolveHarvesterController(Vertx var1) {
      this.vertx = var1;
      this.autoSolve = new AutoSolve(new OkHttpClient.Builder(), "Trickle-4ae3fa8b-26fa-4001-8582-8fd27a7beb7e");
   }

   public void connect() {
      String var1 = Storage.AYCD_ACCESS_TOKEN;
      String var2 = Storage.AYCD_API_KEY;
      CompletableFuture.runAsync(this::lambda$connect$0);
   }
}
