package io.trickle.task.sites.shopify;

import io.trickle.harvester.CaptchaToken;
import io.trickle.util.Utils;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class Presolver {
   public boolean isRunning = true;
   public ShopifyAPI api;
   public Shopify shopifyTask;

   public CompletableFuture monitorCP() {
      int var1 = 0;

      while(var1++ < Integer.MAX_VALUE && this.isRunning && this.api.getWebClient().isActive()) {
         CompletableFuture var6;
         CompletableFuture var8;
         try {
            HttpRequest var2 = this.api.checkpointPage(false);
            var8 = Request.send(var2);
            if (!var8.isDone()) {
               var6 = var8;
               return var6.exceptionally(Function.identity()).thenCompose(Presolver::async$monitorCP);
            }

            HttpResponse var3 = (HttpResponse)var8.join();
            if (var3 != null) {
               String var4 = var3.bodyAsString();
               if (var3.statusCode() == 200 && (var4.contains("content_checkpoint") || this.shopifyTask.task.getMode().contains("testing"))) {
                  VertxUtil.sendSignal("CPStatus" + this.api.getSiteURL());
                  return CompletableFuture.completedFuture(var4);
               }

               if (var3.statusCode() != 200 && (var3.statusCode() != 302 || !var3.getHeader("location").contains("password"))) {
                  this.shopifyTask.getLogger().error("Failed CPStatus: {}", var3.statusCode());
               }

               if (this.shopifyTask.isSmart) {
                  int var5 = Utils.calculateMSLeftUntilHour();
                  if (var5 - this.shopifyTask.task.getMonitorDelay() < 0 && var5 < 10000) {
                     this.shopifyTask.getLogger().info("Preparing smart release");
                     var8 = VertxUtil.signalSleep("CPStatus" + this.api.getSiteURL(), (long)var5);
                     if (!var8.isDone()) {
                        var6 = var8;
                        return var6.exceptionally(Function.identity()).thenCompose(Presolver::async$monitorCP);
                     }

                     var8.join();
                     continue;
                  }
               }

               var8 = VertxUtil.randomSignalSleep("CPStatus" + this.api.getSiteURL(), (long)this.shopifyTask.task.getMonitorDelay());
               if (!var8.isDone()) {
                  var6 = var8;
                  return var6.exceptionally(Function.identity()).thenCompose(Presolver::async$monitorCP);
               }

               var8.join();
            }
         } catch (Throwable var7) {
            if (!var7.getMessage().contains("closed")) {
               this.shopifyTask.getLogger().error("Failed CPStatus: {}", var7.getMessage());
            }

            var8 = VertxUtil.randomSleep(3000L);
            if (!var8.isDone()) {
               var6 = var8;
               return var6.exceptionally(Function.identity()).thenCompose(Presolver::async$monitorCP);
            }

            var8.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$atcAJAX(Presolver param0, String param1, int param2, HttpRequest param3, CompletableFuture param4, HttpResponse param5, Throwable param6, int param7, Object param8) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture fetchRandItem() {
      int var1 = 0;

      while(var1++ < Integer.MAX_VALUE && this.isRunning && this.api.getWebClient().isActive()) {
         CompletableFuture var4;
         CompletableFuture var6;
         try {
            HttpRequest var2 = this.api.productsJSON(true);
            var6 = Request.send(var2);
            if (!var6.isDone()) {
               var4 = var6;
               return var4.exceptionally(Function.identity()).thenCompose(Presolver::async$fetchRandItem);
            }

            HttpResponse var3 = (HttpResponse)var6.join();
            if (var3 != null) {
               if (var3.statusCode() == 200) {
                  VertxUtil.sendSignal("CPProd" + this.api.getSiteURL());
                  return CompletableFuture.completedFuture(var3.bodyAsJsonObject().getJsonArray("products"));
               }

               if (var3.statusCode() == 401) {
                  this.shopifyTask.getLogger().info("Password detected. Sleeping...");
                  var6 = VertxUtil.signalSleep("CPProd" + this.api.getSiteURL(), (long)this.shopifyTask.task.getMonitorDelay());
                  if (!var6.isDone()) {
                     var4 = var6;
                     return var4.exceptionally(Function.identity()).thenCompose(Presolver::async$fetchRandItem);
                  }

                  var6.join();
               } else {
                  this.shopifyTask.getLogger().error("Failed CPProd: {}", var3.statusCode());
               }

               var6 = VertxUtil.signalSleep("CPProd" + this.api.getSiteURL(), (long)this.shopifyTask.task.getMonitorDelay());
               if (!var6.isDone()) {
                  var4 = var6;
                  return var4.exceptionally(Function.identity()).thenCompose(Presolver::async$fetchRandItem);
               }

               var6.join();
            }
         } catch (Throwable var5) {
            if (!var5.getMessage().contains("closed")) {
               this.shopifyTask.getLogger().error("Failed CPProd: {}", var5.getMessage());
            }

            var6 = VertxUtil.randomSleep(3000L);
            if (!var6.isDone()) {
               var4 = var6;
               return var4.exceptionally(Function.identity()).thenCompose(Presolver::async$fetchRandItem);
            }

            var6.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture run() {
      CompletableFuture var10000 = this.monitorCP();
      CompletableFuture var3;
      if (!var10000.isDone()) {
         var3 = var10000;
         return var3.exceptionally(Function.identity()).thenCompose(Presolver::async$run);
      } else {
         String var1 = (String)var10000.join();
         if (this.shopifyTask.isCPMonitorTriggered.compareAndSet(false, true) && this.isRunning && this.api.getWebClient().isActive()) {
            var10000 = this.shopifyTask.solveCheckpointCaptcha(var1);
            if (!var10000.isDone()) {
               var3 = var10000;
               return var3.exceptionally(Function.identity()).thenCompose(Presolver::async$run);
            }

            CaptchaToken var2 = (CaptchaToken)var10000.join();
            var10000 = this.submitCheckpoint(var2);
            if (!var10000.isDone()) {
               var3 = var10000;
               return var3.exceptionally(Function.identity()).thenCompose(Presolver::async$run);
            }

            var10000.join();
            if (this.api.checkpointClient.cookieStore().getCookieValue("_shopify_checkpoint") == null) {
               this.shopifyTask.getLogger().error("CP cookie not found. Severe error");
            } else {
               this.api.getCookies().put("_shopify_checkpoint", this.api.checkpointClient.cookieStore().getCookieValue("_shopify_checkpoint"), this.api.getSiteURL());
            }
         }

         return CompletableFuture.completedFuture((Object)null);
      }
   }

   public static CompletableFuture async$run(Presolver var0, CompletableFuture var1, String var2, CaptchaToken var3, int var4, Object var5) {
      CompletableFuture var10000;
      label44: {
         String var6;
         CompletableFuture var8;
         label49: {
            switch (var4) {
               case 0:
                  var10000 = var0.monitorCP();
                  if (!var10000.isDone()) {
                     var8 = var10000;
                     return var8.exceptionally(Function.identity()).thenCompose(Presolver::async$run);
                  }
                  break;
               case 1:
                  var10000 = var1;
                  break;
               case 2:
                  var10000 = var1;
                  var6 = var2;
                  break label49;
               case 3:
                  var10000 = var1;
                  break label44;
               default:
                  throw new IllegalArgumentException();
            }

            var6 = (String)var10000.join();
            if (!var0.shopifyTask.isCPMonitorTriggered.compareAndSet(false, true) || !var0.isRunning || !var0.api.getWebClient().isActive()) {
               return CompletableFuture.completedFuture((Object)null);
            }

            var10000 = var0.shopifyTask.solveCheckpointCaptcha(var6);
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(Presolver::async$run);
            }
         }

         CaptchaToken var7 = (CaptchaToken)var10000.join();
         var10000 = var0.submitCheckpoint(var7);
         if (!var10000.isDone()) {
            var8 = var10000;
            return var8.exceptionally(Function.identity()).thenCompose(Presolver::async$run);
         }
      }

      var10000.join();
      if (var0.api.checkpointClient.cookieStore().getCookieValue("_shopify_checkpoint") == null) {
         var0.shopifyTask.getLogger().error("CP cookie not found. Severe error");
      } else {
         var0.api.getCookies().put("_shopify_checkpoint", var0.api.checkpointClient.cookieStore().getCookieValue("_shopify_checkpoint"), var0.api.getSiteURL());
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public Presolver(Shopify var1) {
      this.api = var1.api;
      this.shopifyTask = var1;
   }

   public static CompletableFuture async$submitCheckpoint(Presolver param0, CaptchaToken param1, int param2, HttpRequest param3, CompletableFuture param4, HttpResponse param5, Throwable param6, int param7, Object param8) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$monitorCP(Presolver param0, int param1, HttpRequest param2, CompletableFuture param3, HttpResponse param4, String param5, int param6, Throwable param7, int param8, Object param9) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture submitCheckpoint(CaptchaToken var1) {
      this.shopifyTask.getLogger().info("Submitting checkpoint...");
      int var2 = 0;

      while(var2++ < Integer.MAX_VALUE && this.isRunning && this.api.getWebClient().isActive()) {
         CompletableFuture var5;
         CompletableFuture var7;
         try {
            HttpRequest var3 = this.api.submitCheckpoint();
            var7 = Request.send(var3, Shopify.checkpointForm(var1));
            if (!var7.isDone()) {
               var5 = var7;
               return var5.exceptionally(Function.identity()).thenCompose(Presolver::async$submitCheckpoint);
            }

            HttpResponse var4 = (HttpResponse)var7.join();
            if (var4 != null) {
               if (var4.statusCode() == 302) {
                  return CompletableFuture.completedFuture((Object)null);
               }

               this.shopifyTask.getLogger().warn("Retrying submitting checkpoint: status: '{}'", var4.statusCode());
               var7 = VertxUtil.sleep((long)this.shopifyTask.task.getRetryDelay());
               if (!var7.isDone()) {
                  var5 = var7;
                  return var5.exceptionally(Function.identity()).thenCompose(Presolver::async$submitCheckpoint);
               }

               var7.join();
            }
         } catch (Throwable var6) {
            this.shopifyTask.getLogger().error("Error with checkpoint submission: {}", var6.getMessage());
            var7 = VertxUtil.randomSleep(3000L);
            if (!var7.isDone()) {
               var5 = var7;
               return var5.exceptionally(Function.identity()).thenCompose(Presolver::async$submitCheckpoint);
            }

            var7.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public void close() {
      this.isRunning = false;
   }

   public static CompletableFuture async$fetchRandItem(Presolver param0, int param1, HttpRequest param2, CompletableFuture param3, HttpResponse param4, Throwable param5, int param6, Object param7) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture atcAJAX(String var1) {
      int var2 = 0;

      while(var2++ < Integer.MAX_VALUE && this.isRunning && this.api.getWebClient().isActive()) {
         CompletableFuture var5;
         CompletableFuture var7;
         try {
            HttpRequest var3 = this.api.atcAJAX(var1);
            var7 = Request.send(var3);
            if (!var7.isDone()) {
               var5 = var7;
               return var5.exceptionally(Function.identity()).thenCompose(Presolver::async$atcAJAX);
            }

            HttpResponse var4 = (HttpResponse)var7.join();
            if (var4 != null) {
               if (var4.statusCode() == 200) {
                  VertxUtil.sendSignal("CPCart" + this.api.getSiteURL());
                  return CompletableFuture.completedFuture((Object)null);
               }

               this.shopifyTask.getLogger().error("Failed CPCart: {}", var4.statusCode());
               var7 = VertxUtil.signalSleep("CPCart" + this.api.getSiteURL(), (long)this.shopifyTask.task.getMonitorDelay());
               if (!var7.isDone()) {
                  var5 = var7;
                  return var5.exceptionally(Function.identity()).thenCompose(Presolver::async$atcAJAX);
               }

               var7.join();
            }
         } catch (Throwable var6) {
            if (!var6.getMessage().contains("closed")) {
               this.shopifyTask.getLogger().error("Failed CPCart: {}", var6.getMessage());
            }

            var7 = VertxUtil.randomSleep(3000L);
            if (!var7.isDone()) {
               var5 = var7;
               return var5.exceptionally(Function.identity()).thenCompose(Presolver::async$atcAJAX);
            }

            var7.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }
}
