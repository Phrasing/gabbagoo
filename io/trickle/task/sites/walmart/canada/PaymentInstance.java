package io.trickle.task.sites.walmart.canada;

import io.trickle.core.actor.TaskActor;
import io.trickle.task.Task;
import io.trickle.task.sites.walmart.util.PaymentToken;
import io.trickle.util.analytics.Analytics;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;
import org.apache.logging.log4j.Logger;

public class PaymentInstance {
   public Task task;
   public PaymentToken token;
   public int previousResponseLen = 0;
   public int previousResponseHash = 0;
   public String paymentId;
   public WalmartCanadaAPI api;
   public TaskActor parent;
   public String instanceSignal;
   public Logger logger;

   public CompletableFuture init() {
      try {
         WalmartCanadaAPI var10001 = this.api;
         Objects.requireNonNull(var10001);
         CompletableFuture var10000 = this.sendAndHandle(var10001::initCheckout);
         CompletableFuture var5;
         if (!var10000.isDone()) {
            var5 = var10000;
            return var5.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$init);
         }

         JsonObject var1 = (JsonObject)var10000.join();
         if (var1 != null && !var1.getString("status", "error").contains("error")) {
            try {
               var10001 = this.api;
               Objects.requireNonNull(var10001);
               var10000 = this.sendAndHandle(var10001::submitEmail, this.api.submitEmailForm());
               if (!var10000.isDone()) {
                  var5 = var10000;
                  return var5.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$init);
               }

               var1 = (JsonObject)var10000.join();
               if (var1 != null) {
                  var10001 = this.api;
                  Objects.requireNonNull(var10001);
                  var10000 = this.sendAndHandle(var10001::submitAddress, this.api.addressForm());
                  if (!var10000.isDone()) {
                     var5 = var10000;
                     return var5.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$init);
                  }

                  var1 = (JsonObject)var10000.join();
                  if (var1 != null) {
                     var10001 = this.api;
                     Objects.requireNonNull(var10001);
                     var10000 = this.sendAndHandle(var10001::submitCard, this.api.cardForm(this.token));
                     if (!var10000.isDone()) {
                        var5 = var10000;
                        return var5.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$init);
                     }

                     JsonObject var2 = (JsonObject)var10000.join();
                     if (var2 != null && var2.getBoolean("isSuccess", false) && var2.containsKey("paymentMethods")) {
                        JsonObject var3 = var2.getJsonArray("paymentMethods").getJsonObject(0);
                        this.token.setPiHash(var3.getString("piHash"));
                        var10001 = this.api;
                        Objects.requireNonNull(var10001);
                        var10000 = this.sendAndHandle(var10001::submitPayment, this.api.paymentForm(this.token));
                        if (!var10000.isDone()) {
                           var5 = var10000;
                           return var5.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$init);
                        }

                        var1 = (JsonObject)var10000.join();
                        if (var1 != null && var1.containsKey("paymentInfo")) {
                           JsonObject var4 = var1.getJsonArray("paymentInfo").getJsonObject(0);
                           if (var4.containsKey("paymentId") && !var4.getString("paymentId", "").isBlank()) {
                              this.paymentId = var4.getString("paymentId");
                              if (var1.toString().contains("IN_STOCK")) {
                                 return CompletableFuture.completedFuture(PaymentInstance$State.PROCEED_CHECKOUT);
                              }

                              return CompletableFuture.completedFuture(PaymentInstance$State.NO_STOCK);
                           }
                        }
                     }
                  }
               }
            } catch (Throwable var6) {
               this.logger.error("Error while generating checkout: {}", var6.getMessage());
               return CompletableFuture.completedFuture(PaymentInstance$State.FAILED_INIT);
            }
         }
      } catch (Throwable var7) {
         var7.printStackTrace();
      }

      this.logger.warn("Failed to generate checkout");
      return CompletableFuture.completedFuture(PaymentInstance$State.FAILED_INIT);
   }

   public CompletableFuture processOrder() {
      Buffer var1 = this.api.placeOrderForm(this.token, this.paymentId);
      byte var2 = 0;
      int var3 = 0;

      while(this.api.getWebClient().isActive() && var3 <= 100) {
         this.logger.info("Processing...");
         HttpRequest var4 = this.api.placeOrder();

         CompletableFuture var10000;
         CompletableFuture var7;
         try {
            var10000 = Request.send(var4, var1);
            if (!var10000.isDone()) {
               var7 = var10000;
               return var7.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$processOrder);
            }

            HttpResponse var5 = (HttpResponse)var10000.join();
            if (var5 != null) {
               if (var5.statusCode() == 200) {
                  if (var2 != 0) {
                     VertxUtil.sendSignal(this.instanceSignal);
                  }

                  this.logger.info("Successfully checked out with status '{}' ", var5.statusCode());
                  Analytics.success(this.task, var5.bodyAsJsonObject(), this.api.proxyStringSafe());
                  return CompletableFuture.completedFuture(200);
               }

               if (var5.statusCode() == 405) {
                  this.logger.warn("Cart has expired with status'{}'. Re-submitting.", var5.bodyAsString().toLowerCase());
                  return CompletableFuture.completedFuture(405);
               }

               String var6 = var5.bodyAsString();
               if (var6.contains("Availability for some of the items")) {
                  if (var2 != 0) {
                     this.logger.info("Waiting for restock with status '{}'", var5.statusCode());
                  } else {
                     var2 = 1;
                     this.logger.warn("OOS on checkout with status '{}'", var5.statusCode());
                     this.handleFailureWebhooks("Out Of Stock", (Buffer)var5.body());
                  }

                  var10000 = VertxUtil.randomSignalSleep(this.instanceSignal, (long)this.task.getMonitorDelay());
                  if (!var10000.isDone()) {
                     var7 = var10000;
                     return var7.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$processOrder);
                  }

                  var10000.join();
               } else {
                  if (!var6.contains("Auth Declined") && !var6.contains("Invalid Card") && !var6.contains("payment_service_authorization_decline")) {
                     this.logger.info("Failed to submit order with status: '{}'", var5.statusCode());
                     var10000 = this.api.handleBadResponse(var5.statusCode());
                     if (!var10000.isDone()) {
                        var7 = var10000;
                        return var7.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$processOrder);
                     }

                     var10000.join();
                  } else {
                     ++var3;
                     this.logger.warn("Card Decline with status: '{}'", var5.statusCode());
                     this.handleFailureWebhooks("Card decline (FailedCharge)", (Buffer)var5.body());
                  }

                  var10000 = VertxUtil.randomSignalSleep(this.instanceSignal, (long)this.task.getRetryDelay());
                  if (!var10000.isDone()) {
                     var7 = var10000;
                     return var7.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$processOrder);
                  }

                  var10000.join();
               }
            }
         } catch (Throwable var8) {
            this.logger.error("Error occurred while processing payment: '{}'", var8.getMessage());
            if (var8.getMessage().contains("Unexpected character")) {
               var10000 = VertxUtil.randomSleep(12000L);
               if (!var10000.isDone()) {
                  var7 = var10000;
                  return var7.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$processOrder);
               }

               var10000.join();
            }
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$init(PaymentInstance param0, CompletableFuture param1, JsonObject param2, JsonObject param3, JsonObject param4, int param5, Object param6) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$processOrder(PaymentInstance param0, Buffer param1, int param2, int param3, HttpRequest param4, CompletableFuture param5, HttpResponse param6, String param7, Throwable param8, int param9, Object param10) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture sendAndHandle(Supplier var1, Buffer var2) {
      int var3 = 0;

      while(this.api.getWebClient().isActive() && var3++ <= 100) {
         CompletableFuture var5;
         CompletableFuture var7;
         try {
            HttpResponse var4;
            if (var2 == null) {
               var7 = Request.send((HttpRequest)var1.get());
               if (!var7.isDone()) {
                  var5 = var7;
                  return var5.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$sendAndHandle);
               }

               var4 = (HttpResponse)var7.join();
            } else {
               var7 = Request.send((HttpRequest)var1.get(), var2);
               if (!var7.isDone()) {
                  var5 = var7;
                  return var5.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$sendAndHandle);
               }

               var4 = (HttpResponse)var7.join();
            }

            if (var4 != null) {
               if (var4.statusCode() != 412 && var4.statusCode() != 444) {
                  return CompletableFuture.completedFuture(var4.bodyAsJsonObject());
               }

               var7 = this.api.handleBadResponse(var4.statusCode());
               if (!var7.isDone()) {
                  var5 = var7;
                  return var5.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$sendAndHandle);
               }

               var7.join();
            } else {
               this.logger.warn("Retrying...");
               var7 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
               if (!var7.isDone()) {
                  var5 = var7;
                  return var5.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$sendAndHandle);
               }

               var7.join();
            }
         } catch (Throwable var6) {
            this.logger.error("Error occurred executing: {}", var6.getMessage());
            if (var6.getMessage().contains("Unexpected character")) {
               var7 = VertxUtil.randomSleep(12000L);
               if (!var7.isDone()) {
                  var5 = var7;
                  return var5.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$sendAndHandle);
               }

               var7.join();
            }
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public PaymentInstance(WalmartCA var1, Task var2, PaymentToken var3) {
      this.parent = var1;
      this.api = (WalmartCanadaAPI)var1.getClient();
      this.logger = var1.getLogger();
      this.task = var2;
      this.token = var3;
      this.instanceSignal = this.task.getKeywords()[0];
   }

   public CompletableFuture sendAndHandle(Supplier var1) {
      return this.sendAndHandle(var1, (Buffer)null);
   }

   public void handleFailureWebhooks(String var1, Buffer var2) {
      if (this.previousResponseHash == 0 || this.previousResponseLen != var2.length() || this.previousResponseHash != var2.hashCode()) {
         try {
            Analytics.failure(var1, this.task, var2.toJsonObject(), this.api.proxyStringSafe());
            this.previousResponseHash = var2.hashCode();
            this.previousResponseLen = var2.length();
         } catch (Throwable var4) {
         }
      }

   }

   public static CompletableFuture async$sendAndHandle(PaymentInstance param0, Supplier param1, Buffer param2, int param3, CompletableFuture param4, HttpResponse param5, Throwable param6, int param7, Object param8) {
      // $FF: Couldn't be decompiled
   }

   public static PaymentInstance get(WalmartCA var0, Task var1, PaymentToken var2) {
      return new PaymentInstance(var0, var1, var2);
   }
}
