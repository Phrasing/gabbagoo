package io.trickle.task.sites.finishline;

import io.trickle.core.actor.TaskActor;
import io.trickle.task.Task;
import io.trickle.util.request.Request;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class Finishline extends TaskActor {
   public FinishlineAPI api;
   public Task task;

   public CompletableFuture getCartSession() {
      int var1 = 0;

      while(super.running && var1 < 5) {
         ++var1;
         CompletableFuture var10000 = super.sleep(3000);
         if (!var10000.isDone()) {
            CompletableFuture var2 = var10000;
            return var2.exceptionally(Function.identity()).thenCompose(Finishline::async$getCartSession);
         }

         var10000.join();
         System.out.println("Donezo");
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$getCartSession(Finishline var0, int var1, CompletableFuture var2, int var3, Object var4) {
      switch (var3) {
         case 0:
            var1 = 0;
            break;
         case 1:
            var2.join();
            System.out.println("Donezo");
            break;
         default:
            throw new IllegalArgumentException();
      }

      while(var0.running && var1 < 5) {
         ++var1;
         CompletableFuture var10000 = var0.sleep(3000);
         if (!var10000.isDone()) {
            var2 = var10000;
            return var2.exceptionally(Function.identity()).thenCompose(Finishline::async$getCartSession);
         }

         var10000.join();
         System.out.println("Donezo");
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture run() {
      CompletableFuture var10000 = super.randomSleep(5000);
      CompletableFuture var1;
      if (!var10000.isDone()) {
         var1 = var10000;
         return var1.exceptionally(Function.identity()).thenCompose(Finishline::async$run);
      } else {
         var10000.join();
         var10000 = this.visitHomePage();
         if (!var10000.isDone()) {
            var1 = var10000;
            return var1.exceptionally(Function.identity()).thenCompose(Finishline::async$run);
         } else {
            var10000.join();
            return CompletableFuture.completedFuture((Object)null);
         }
      }
   }

   public static CompletableFuture async$visitHomePage(Finishline param0, int param1, HttpRequest param2, CompletableFuture param3, int param4, Object param5) {
      // $FF: Couldn't be decompiled
   }

   public Finishline(Task var1, int var2) {
      super(var2);
      this.task = var1;
      this.api = new FinishlineAPI(this.task, this.task.getSite());
      super.setClient(this.api);
   }

   public CompletableFuture visitHomePage() {
      int var1 = 0;

      while(super.running && var1 < 10000) {
         CompletableFuture var10000;
         CompletableFuture var5;
         try {
            HttpRequest var2 = this.api.homePage();
            var10000 = Request.send(var2);
            if (!var10000.isDone()) {
               var5 = var10000;
               return var5.exceptionally(Function.identity()).thenCompose(Finishline::async$visitHomePage);
            }

            HttpResponse var3 = (HttpResponse)var10000.join();
            if (var3 != null) {
               String var4 = (String)var3.body();
               if (var4.contains("<title>WAITING</title>")) {
                  this.logger.warn("Waiting in Queue!");
               } else {
                  if (!var4.contains("ACCESS DENIED")) {
                     this.logger.info("Passed queue (?) {}", var3.statusCode());
                     break;
                  }

                  this.logger.error("Banned!");
               }
            }
         } catch (Exception var6) {
            this.logger.error("Error occurred on queue {}", var6.getMessage());
         }

         ++var1;
         var10000 = this.sleep(31000);
         if (!var10000.isDone()) {
            var5 = var10000;
            return var5.exceptionally(Function.identity()).thenCompose(Finishline::async$visitHomePage);
         }

         var10000.join();
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$run(Finishline var0, CompletableFuture var1, int var2, Object var3) {
      CompletableFuture var10000;
      label18: {
         switch (var2) {
            case 0:
               var10000 = var0.randomSleep(5000);
               if (!var10000.isDone()) {
                  var1 = var10000;
                  return var1.exceptionally(Function.identity()).thenCompose(Finishline::async$run);
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
         var10000 = var0.visitHomePage();
         if (!var10000.isDone()) {
            var1 = var10000;
            return var1.exceptionally(Function.identity()).thenCompose(Finishline::async$run);
         }
      }

      var10000.join();
      return CompletableFuture.completedFuture((Object)null);
   }
}
