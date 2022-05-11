package io.trickle.harvester;

import io.trickle.core.Controller;
import io.trickle.core.Engine;
import io.trickle.core.api.LoadableAsync;
import io.trickle.core.api.Module;
import io.trickle.webclient.CookieJar;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginController implements Module, LoadableAsync {
   public AtomicReference waitingList = new AtomicReference();
   public static Logger logger = LogManager.getLogger(TokenController.class);
   public String LOCK_IDENTITY;
   public AtomicInteger solveCount;
   public Vertx vertx;

   public void terminate() {
   }

   public LoginController(Vertx var1) {
      this.waitingList.set(new ArrayBlockingQueue(300));
      this.vertx = var1;
      this.LOCK_IDENTITY = UUID.randomUUID().toString();
      this.solveCount = new AtomicInteger(0);
   }

   public static CompletableFuture initBrowserLogin(String var0, Iterable var1, String var2, CookieJar var3, String var4) {
      try {
         logger.info("Browser needs attention.");
         LoginToken var5 = new LoginToken(var0, var1, var2, var3, var4);
         LoginFuture var6 = ((LoginController)Engine.get().getModule(Controller.LOGIN)).solve(var5);
         if (!var6.toCompletableFuture().isDone()) {
            return var6.exceptionally(Function.identity()).thenCompose(LoginController::async$initBrowserLogin).toCompletableFuture();
         } else {
            var6.toCompletableFuture().join();
            return CompletableFuture.completedFuture(var5.getCookieJar());
         }
      } catch (Throwable var8) {
         var8.printStackTrace();
         logger.error("HARVEST ERR: {}", var8.getMessage());
         return CompletableFuture.completedFuture((Object)null);
      }
   }

   public Future load() {
      return Future.succeededFuture();
   }

   public LoginFuture solve(LoginToken var1) {
      LoginFuture var2 = new LoginFuture(var1);
      if (!((ArrayBlockingQueue)this.waitingList.get()).offer(var2)) {
         throw new Exception("Too many tokens to solve!!!");
      } else {
         return var2;
      }
   }

   public static CompletableFuture async$initBrowserLogin(String var0, Iterable var1, String var2, CookieJar var3, String var4, LoginToken var5, LoginFuture var6, LoginFuture var7, int var8, Object var9) {
      Throwable var12;
      label26: {
         LoginFuture var10000;
         boolean var10001;
         switch (var8) {
            case 0:
               try {
                  logger.info("Browser needs attention.");
                  var5 = new LoginToken(var0, var1, var2, var3, var4);
                  var6 = ((LoginController)Engine.get().getModule(Controller.LOGIN)).solve(var5);
                  var10000 = var6;
                  if (!var6.toCompletableFuture().isDone()) {
                     return var6.exceptionally(Function.identity()).thenCompose(LoginController::async$initBrowserLogin).toCompletableFuture();
                  }
                  break;
               } catch (Throwable var11) {
                  var12 = var11;
                  var10001 = false;
                  break label26;
               }
            case 1:
               var10000 = var7;
               break;
            default:
               throw new IllegalArgumentException();
         }

         try {
            var10000.toCompletableFuture().join();
            return CompletableFuture.completedFuture(var5.getCookieJar());
         } catch (Throwable var10) {
            var12 = var10;
            var10001 = false;
         }
      }

      Throwable var13 = var12;
      var13.printStackTrace();
      logger.error("HARVEST ERR: {}", var13.getMessage());
      return CompletableFuture.completedFuture((Object)null);
   }

   public void initialise() {
      logger.debug("Initialised.");
   }

   public LoginFuture pollWaitingList() {
      this.solveCount.incrementAndGet();
      return (LoginFuture)((ArrayBlockingQueue)this.waitingList.get()).take();
   }
}
