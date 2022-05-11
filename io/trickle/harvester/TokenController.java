package io.trickle.harvester;

import io.trickle.core.Controller;
import io.trickle.core.Engine;
import io.trickle.core.api.LoadableAsync;
import io.trickle.core.api.Module;
import io.vertx.core.Future;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TokenController implements Module, LoadableAsync {
   public AtomicReference waitingList = new AtomicReference();
   public AtomicInteger solveCount;
   public static Logger logger = LogManager.getLogger(TokenController.class);

   public TokenController() {
      this.waitingList.set(new ArrayBlockingQueue(1500));
      this.solveCount = new AtomicInteger(0);
   }

   public void initialise() {
      logger.debug("Initialised.");
   }

   public void terminate() {
   }

   public SolveFuture solve(CaptchaToken var1) {
      SolveFuture var2 = new SolveFuture(var1);
      if (!((ArrayBlockingQueue)this.waitingList.get()).offer(var2)) {
         throw new Exception("Too many tokens to solve!!!");
      } else {
         return var2;
      }
   }

   public SolveFuture pollWaitingList() {
      this.solveCount.incrementAndGet();
      return (SolveFuture)((ArrayBlockingQueue)this.waitingList.get()).take();
   }

   public static CompletableFuture solveBasicCaptcha(String var0) {
      try {
         logger.info("Captcha needs solving");
         CaptchaToken var1 = new CaptchaToken(var0);
         SolveFuture var2 = ((TokenController)Engine.get().getModule(Controller.TOKEN)).solve(var1);
         if (!var2.toCompletableFuture().isDone()) {
            return var2.exceptionally(Function.identity()).thenCompose(TokenController::async$solveBasicCaptcha).toCompletableFuture();
         } else {
            var2.toCompletableFuture().join();
            return CompletableFuture.completedFuture(var1);
         }
      } catch (Throwable var4) {
         logger.error("HARVEST ERR: {}", var4.getMessage());
         return CompletableFuture.completedFuture((Object)null);
      }
   }

   public Future load() {
      return Future.succeededFuture();
   }

   public static CompletableFuture async$solveBasicCaptcha(String var0, CaptchaToken var1, SolveFuture var2, SolveFuture var3, int var4, Object var5) {
      Throwable var9;
      label26: {
         SolveFuture var10000;
         boolean var10001;
         switch (var4) {
            case 0:
               try {
                  logger.info("Captcha needs solving");
                  var1 = new CaptchaToken(var0);
                  var2 = ((TokenController)Engine.get().getModule(Controller.TOKEN)).solve(var1);
                  var10000 = var2;
                  if (!var2.toCompletableFuture().isDone()) {
                     return var2.exceptionally(Function.identity()).thenCompose(TokenController::async$solveBasicCaptcha).toCompletableFuture();
                  }
                  break;
               } catch (Throwable var7) {
                  var9 = var7;
                  var10001 = false;
                  break label26;
               }
            case 1:
               var10000 = var3;
               break;
            default:
               throw new IllegalArgumentException();
         }

         try {
            var10000.toCompletableFuture().join();
            return CompletableFuture.completedFuture(var1);
         } catch (Throwable var6) {
            var9 = var6;
            var10001 = false;
         }
      }

      Throwable var8 = var9;
      logger.error("HARVEST ERR: {}", var8.getMessage());
      return CompletableFuture.completedFuture((Object)null);
   }
}
