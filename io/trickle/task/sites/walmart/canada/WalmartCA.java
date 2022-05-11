package io.trickle.task.sites.walmart.canada;

import io.trickle.core.actor.TaskActor;
import io.trickle.task.Task;
import io.trickle.task.sites.walmart.util.PaymentToken;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.codec.BodyCodec;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class WalmartCA extends TaskActor {
   public PaymentToken token;
   public Task task;
   public String instanceSignal;
   public WalmartCanadaAPI api;
   public PaymentInstance payment;

   public CompletableFuture generateToken() {
      HttpRequest var1 = this.api.getWebClient().getAbs("https://securedataweb.walmart.com/pie/v1/wmcom_us_vtg_pie/getkey.js?bust=" + System.currentTimeMillis()).timeout(TimeUnit.SECONDS.toMillis(15L)).as(BodyCodec.string());

      try {
         CompletableFuture var10000 = Request.execute(var1, 5);
         if (!var10000.isDone()) {
            CompletableFuture var3 = var10000;
            return var3.exceptionally(Function.identity()).thenCompose(WalmartCA::async$generateToken);
         } else {
            String var2 = (String)var10000.join();
            this.token = PaymentToken.prepareAndGenerate(var2, this.task.getProfile().getCardNumber(), this.task.getProfile().getCvv());
            return CompletableFuture.completedFuture(true);
         }
      } catch (Exception var4) {
         this.logger.warn("Failed to generate payment token: {}", var4.getMessage());
         return CompletableFuture.completedFuture(false);
      }
   }

   public static CompletableFuture async$generateToken(WalmartCA var0, HttpRequest var1, CompletableFuture var2, int var3, Object var4) {
      Exception var10;
      label28: {
         CompletableFuture var10000;
         boolean var10001;
         switch (var3) {
            case 0:
               var1 = var0.api.getWebClient().getAbs("https://securedataweb.walmart.com/pie/v1/wmcom_us_vtg_pie/getkey.js?bust=" + System.currentTimeMillis()).timeout(TimeUnit.SECONDS.toMillis(15L)).as(BodyCodec.string());

               try {
                  var10000 = Request.execute(var1, 5);
                  if (!var10000.isDone()) {
                     CompletableFuture var9 = var10000;
                     return var9.exceptionally(Function.identity()).thenCompose(WalmartCA::async$generateToken);
                  }
                  break;
               } catch (Exception var6) {
                  var10 = var6;
                  var10001 = false;
                  break label28;
               }
            case 1:
               var10000 = var2;
               break;
            default:
               throw new IllegalArgumentException();
         }

         try {
            String var8 = (String)var10000.join();
            var0.token = PaymentToken.prepareAndGenerate(var8, var0.task.getProfile().getCardNumber(), var0.task.getProfile().getCvv());
            return CompletableFuture.completedFuture(true);
         } catch (Exception var5) {
            var10 = var5;
            var10001 = false;
         }
      }

      Exception var7 = var10;
      var0.logger.warn("Failed to generate payment token: {}", var7.getMessage());
      return CompletableFuture.completedFuture(false);
   }

   public WalmartCA(Task var1, int var2) {
      super(var2);
      this.task = var1;
      this.api = new WalmartCanadaAPI(this.task);
      super.setClient(this.api);
      this.instanceSignal = this.task.getKeywords()[0];
   }

   public CompletableFuture initCheckout() {
      this.payment = PaymentInstance.get(this, this.task, this.token);
      return this.payment.init();
   }

   public static CompletableFuture async$addToCart(WalmartCA param0, Buffer param1, int param2, HttpRequest param3, CompletableFuture param4, HttpResponse param5, int param6, Throwable param7, int param8, Object param9) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture run() {
      CompletableFuture var10000 = this.generateToken();
      CompletableFuture var1;
      if (!var10000.isDone()) {
         var1 = var10000;
         return var1.exceptionally(Function.identity()).thenCompose(WalmartCA::async$run);
      } else {
         if ((Boolean)var10000.join()) {
            var10000 = this.tryCart();
            if (!var10000.isDone()) {
               var1 = var10000;
               return var1.exceptionally(Function.identity()).thenCompose(WalmartCA::async$run);
            }

            var10000.join();
         } else {
            this.logger.error("Failed to initialize...");
         }

         return CompletableFuture.completedFuture((Object)null);
      }
   }

   public CompletableFuture addToCart() {
      this.logger.info("Attempting add to cart");
      Buffer var1 = this.api.atcForm().toBuffer();
      int var2 = 0;

      while(super.running && var2++ <= 50) {
         HttpRequest var3 = this.api.addToCart();

         CompletableFuture var6;
         CompletableFuture var8;
         try {
            var8 = Request.send(var3, var1);
            if (!var8.isDone()) {
               var6 = var8;
               return var6.exceptionally(Function.identity()).thenCompose(WalmartCA::async$addToCart);
            }

            HttpResponse var4 = (HttpResponse)var8.join();
            if (var4 != null) {
               if (var4.statusCode() == 200) {
                  this.logger.info("Successfully added to cart: status:'{}'", var4.statusCode());
                  return CompletableFuture.completedFuture(true);
               }

               this.logger.warn("Failed to ATC: status:'{}'", var4.statusCode());
               var8 = this.api.handleBadResponse(var4.statusCode());
               if (!var8.isDone()) {
                  var6 = var8;
                  return var6.exceptionally(Function.identity()).thenCompose(WalmartCA::async$addToCart);
               }

               byte var5 = (Boolean)var8.join();
               if (var4.statusCode() != 412) {
                  var8 = VertxUtil.randomSignalSleep(this.instanceSignal, (long)this.task.getMonitorDelay());
                  if (!var8.isDone()) {
                     var6 = var8;
                     return var6.exceptionally(Function.identity()).thenCompose(WalmartCA::async$addToCart);
                  }

                  var8.join();
               } else if (var5 == 0) {
                  var8 = super.randomSleep(12000);
                  if (!var8.isDone()) {
                     var6 = var8;
                     return var6.exceptionally(Function.identity()).thenCompose(WalmartCA::async$addToCart);
                  }

                  var8.join();
               }
            } else {
               this.logger.error("Retrying to ATC");
            }
         } catch (Throwable var7) {
            var8 = super.randomSleep(12000);
            if (!var8.isDone()) {
               var6 = var8;
               return var6.exceptionally(Function.identity()).thenCompose(WalmartCA::async$addToCart);
            }

            var8.join();
         }
      }

      return CompletableFuture.completedFuture(false);
   }

   public static CompletableFuture async$run(WalmartCA var0, CompletableFuture var1, int var2, Object var3) {
      CompletableFuture var10000;
      label31: {
         switch (var2) {
            case 0:
               var10000 = var0.generateToken();
               if (!var10000.isDone()) {
                  var1 = var10000;
                  return var1.exceptionally(Function.identity()).thenCompose(WalmartCA::async$run);
               }
               break;
            case 1:
               var10000 = var1;
               break;
            case 2:
               var10000 = var1;
               break label31;
            default:
               throw new IllegalArgumentException();
         }

         if (!(Boolean)var10000.join()) {
            var0.logger.error("Failed to initialize...");
            return CompletableFuture.completedFuture((Object)null);
         }

         var10000 = var0.tryCart();
         if (!var10000.isDone()) {
            var1 = var10000;
            return var1.exceptionally(Function.identity()).thenCompose(WalmartCA::async$run);
         }
      }

      var10000.join();
      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture tryCart() {
      byte var1 = 0;
      PaymentInstance$State var2 = null;

      while(super.running) {
         CompletableFuture var10000;
         CompletableFuture var4;
         if (var1 == 0) {
            try {
               var10000 = this.addToCart();
               if (!var10000.isDone()) {
                  var4 = var10000;
                  return var4.exceptionally(Function.identity()).thenCompose(WalmartCA::async$tryCart);
               }

               var1 = (Boolean)var10000.join();
            } catch (Exception var5) {
               var5.printStackTrace();
               continue;
            }
         }

         if (var1 != 0) {
            if (var2 != null && !var2.equals(PaymentInstance$State.FAILED_INIT)) {
               var10000 = this.payment.processOrder();
               if (!var10000.isDone()) {
                  var4 = var10000;
                  return var4.exceptionally(Function.identity()).thenCompose(WalmartCA::async$tryCart);
               }

               int var3 = (Integer)var10000.join();
               if (var3 == 200) {
                  break;
               }

               if (var3 == 405) {
                  this.api.getWebClient().cookieStore().clear();
                  var1 = 0;
               }
            } else {
               var10000 = this.initCheckout();
               if (!var10000.isDone()) {
                  var4 = var10000;
                  return var4.exceptionally(Function.identity()).thenCompose(WalmartCA::async$tryCart);
               }

               var2 = (PaymentInstance$State)var10000.join();
            }
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$tryCart(WalmartCA param0, int param1, PaymentInstance$State param2, CompletableFuture param3, int param4, Object param5) {
      // $FF: Couldn't be decompiled
   }
}
