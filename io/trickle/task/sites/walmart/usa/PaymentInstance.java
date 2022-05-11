package io.trickle.task.sites.walmart.usa;

import io.trickle.core.actor.TaskActor;
import io.trickle.task.Task;
import io.trickle.task.sites.walmart.Mode;
import io.trickle.task.sites.walmart.util.PaymentToken;
import io.trickle.util.analytics.Analytics;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import org.apache.logging.log4j.Logger;

public class PaymentInstance {
   public PaymentToken token;
   public JsonObject cartInfo;
   public boolean griefAlt;
   public int previousResponseHash = 0;
   public int previousResponseLen = 0;
   public boolean griefMode;
   public Logger logger;
   public Mode mode;
   public String instanceSignal;
   public TaskActor parent;
   public Task task;
   public boolean isCheckout;
   public API api;

   public static CompletableFuture async$preload(PaymentInstance param0, CompletableFuture param1, PaymentInstance param2, int param3, Object param4) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$submitShipping(PaymentInstance param0, Buffer param1, HttpRequest param2, CompletableFuture param3, HttpResponse param4, Exception param5, int param6, Object param7) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture checkout() {
      this.instanceSignal = this.task.getKeywords()[0];

      int var1;
      try {
         var1 = Integer.parseInt(this.task.getKeywords()[1]);
      } catch (NumberFormatException var10) {
         this.logger.warn("Missing price-check (limit) for product: '{}'. Skipping...", this.instanceSignal);
         var1 = Integer.MAX_VALUE;
      }

      CompletableFuture var9;
      CompletableFuture var10001;
      try {
         var10001 = this.getPCID();
         if (!var10001.isDone()) {
            var9 = var10001;
            return var9.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$checkout);
         }

         this.cartInfo = (JsonObject)var10001.join();
      } catch (Throwable var11) {
         return CompletableFuture.completedFuture(-1);
      }

      if (this.cartInfo != null && this.cartInfo.containsKey("items")) {
         double var2 = this.cartInfo.getJsonArray("items").getJsonObject(0).getDouble("unitPrice");
         if (var2 > (double)var1) {
            return CompletableFuture.failedFuture(new Exception("Price exceeds limit of $" + var1));
         } else {
            this.logger.info("Attempting to checkout");
            CompletableFuture var8;
            CompletableFuture var10000;
            if (this.griefMode) {
               if (this.griefAlt) {
                  CompletableFuture var13 = this.processPayment();
                  CompletableFuture var5 = this.api.getWebClient().windowUpdateCallback();
                  if (var5 != null) {
                     this.selectShipping();
                     var10000 = VertxUtil.handleEagerFuture(var5);
                     if (!var10000.isDone()) {
                        var8 = var10000;
                        return var8.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$checkout);
                     }

                     var10000.join();
                  } else {
                     var10000 = this.selectShipping();
                     if (!var10000.isDone()) {
                        var8 = var10000;
                        return var8.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$checkout);
                     }

                     var10000.join();
                  }

                  var5 = this.api.getWebClient().windowUpdateCallback();
                  if (var5 != null) {
                     long var14 = Instant.now().toEpochMilli();
                     this.submitShipping();
                     var10000 = VertxUtil.handleEagerFuture(var5);
                     if (!var10000.isDone()) {
                        var8 = var10000;
                        return var8.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$checkout);
                     }

                     var10000.join();
                     var10000 = VertxUtil.hardCodedSleep(300L - (Instant.now().toEpochMilli() - var14));
                     if (!var10000.isDone()) {
                        var8 = var10000;
                        return var8.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$checkout);
                     }

                     var10000.join();
                  } else {
                     var10000 = this.submitShipping();
                     if (!var10000.isDone()) {
                        var8 = var10000;
                        return var8.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$checkout);
                     }

                     var10000.join();
                  }

                  var10001 = this.submitPayment();
                  if (!var10001.isDone()) {
                     var9 = var10001;
                     return var9.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$checkout);
                  } else {
                     this.cartInfo = (JsonObject)var10001.join();
                     if (!var13.isDone()) {
                        return var13.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$checkout);
                     } else {
                        int var15 = (Integer)var13.join();
                        return CompletableFuture.completedFuture(var15);
                     }
                  }
               } else {
                  CompletableFuture var6 = this.api.getWebClient().windowUpdateCallback();
                  long var12;
                  if (var6 != null) {
                     var12 = Instant.now().toEpochMilli();
                     this.selectShipping();
                     var10000 = VertxUtil.handleEagerFuture(var6);
                     if (!var10000.isDone()) {
                        var8 = var10000;
                        return var8.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$checkout);
                     }

                     var10000.join();
                     var10000 = VertxUtil.hardCodedSleep(50L - (Instant.now().toEpochMilli() - var12));
                     if (!var10000.isDone()) {
                        var8 = var10000;
                        return var8.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$checkout);
                     }

                     var10000.join();
                  } else {
                     var10000 = this.selectShipping();
                     if (!var10000.isDone()) {
                        var8 = var10000;
                        return var8.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$checkout);
                     }

                     var10000.join();
                  }

                  var6 = this.api.getWebClient().windowUpdateCallback();
                  if (var6 != null) {
                     var12 = Instant.now().toEpochMilli();
                     this.submitShipping();
                     var10000 = VertxUtil.handleEagerFuture(var6);
                     if (!var10000.isDone()) {
                        var8 = var10000;
                        return var8.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$checkout);
                     }

                     var10000.join();
                     var10000 = VertxUtil.hardCodedSleep(300L - (Instant.now().toEpochMilli() - var12));
                     if (!var10000.isDone()) {
                        var8 = var10000;
                        return var8.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$checkout);
                     }

                     var10000.join();
                  } else {
                     var10000 = this.submitShipping();
                     if (!var10000.isDone()) {
                        var8 = var10000;
                        return var8.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$checkout);
                     }

                     var10000.join();
                  }

                  var10001 = this.submitPayment();
                  if (!var10001.isDone()) {
                     var9 = var10001;
                     return var9.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$checkout);
                  } else {
                     this.cartInfo = (JsonObject)var10001.join();
                     var10000 = this.processPayment();
                     if (!var10000.isDone()) {
                        var8 = var10000;
                        return var8.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$checkout);
                     } else {
                        int var7 = (Integer)var10000.join();
                        return CompletableFuture.completedFuture(var7);
                     }
                  }
               }
            } else {
               var10001 = this.selectShipping();
               if (!var10001.isDone()) {
                  var9 = var10001;
                  return var9.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$checkout);
               } else {
                  this.cartInfo = (JsonObject)var10001.join();
                  var10001 = this.submitShipping();
                  if (!var10001.isDone()) {
                     var9 = var10001;
                     return var9.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$checkout);
                  } else {
                     this.cartInfo = (JsonObject)var10001.join();
                     var10001 = this.submitPayment();
                     if (!var10001.isDone()) {
                        var9 = var10001;
                        return var9.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$checkout);
                     } else {
                        this.cartInfo = (JsonObject)var10001.join();
                        var10000 = this.processPayment();
                        if (!var10000.isDone()) {
                           var8 = var10000;
                           return var8.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$checkout);
                        } else {
                           int var4 = (Integer)var10000.join();
                           return CompletableFuture.completedFuture(var4);
                        }
                     }
                  }
               }
            }
         }
      } else {
         return CompletableFuture.failedFuture(new Exception("Failed to get PCID"));
      }
   }

   public void checkBadStatus(int var1) {
      switch (var1) {
         case 307:
            this.logger.warn("PX Block with status:'307'. Retrying...");
            break;
         case 412:
            this.logger.warn("PX Block with status:'412'. Retrying...");
            break;
         case 444:
            this.logger.warn("Failed to execute due to status '444': PROXY_BAN");
            break;
         default:
            this.logger.error("Failed to execute due to status '{}'", var1);
      }

   }

   public CompletableFuture preload() {
      this.logger.info("Generating...");
      this.instanceSignal = this.task.getKeywords()[0];

      try {
         CompletableFuture var10000 = this.addToCart();
         if (!var10000.isDone()) {
            CompletableFuture var2 = var10000;
            return var2.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$preload);
         }

         var10000.join();
         CompletableFuture var10001 = this.getPCID();
         CompletableFuture var3;
         if (!var10001.isDone()) {
            var3 = var10001;
            return var3.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$preload);
         }

         this.cartInfo = (JsonObject)var10001.join();
         if (this.cartInfo != null && this.cartInfo.containsKey("items")) {
            try {
               var10001 = this.selectShipping();
               if (!var10001.isDone()) {
                  var3 = var10001;
                  return var3.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$preload);
               }

               this.cartInfo = (JsonObject)var10001.join();
               if (this.cartInfo != null) {
                  var10001 = this.submitShipping();
                  if (!var10001.isDone()) {
                     var3 = var10001;
                     return var3.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$preload);
                  }

                  this.cartInfo = (JsonObject)var10001.join();
                  if (this.cartInfo != null) {
                     var10001 = this.submitBilling();
                     if (!var10001.isDone()) {
                        var3 = var10001;
                        return var3.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$preload);
                     }

                     this.cartInfo = (JsonObject)var10001.join();
                     if (this.cartInfo != null) {
                        if (this.cartInfo.containsKey("piHash")) {
                           return CompletableFuture.completedFuture(this.cartInfo.getString("piHash"));
                        }

                        this.cartInfo = null;
                     }
                  }
               }
            } catch (Exception var4) {
               this.logger.error("Error while generating checkout: {}", var4.getMessage());
               return CompletableFuture.completedFuture("");
            }
         }
      } catch (Throwable var5) {
      }

      this.logger.warn("Failed to generate checkout.");
      return CompletableFuture.completedFuture("");
   }

   public CompletableFuture addToCart() {
      Buffer var1 = this.api.getAtcForm("47CDCD2F7ED24EC2BA9F74BEAE3C151B", 1).put("unitPrice", 5).toBuffer();
      int var2 = 0;

      while(this.api.getWebClient().isActive() && var2 < 3) {
         HttpRequest var3 = this.api.addToCart();

         CompletableFuture var10000;
         CompletableFuture var6;
         try {
            var10000 = Request.send(var3, var1);
            if (!var10000.isDone()) {
               var6 = var10000;
               return var6.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$addToCart);
            }

            HttpResponse var4 = (HttpResponse)var10000.join();
            if (var4 != null) {
               if (var4.statusCode() == 201 || var4.statusCode() == 200 || var4.statusCode() == 206) {
                  return CompletableFuture.completedFuture((Object)null);
               }

               ++var2;
               var10000 = this.api.handleBadResponse(var4.statusCode(), var4);
               if (!var10000.isDone()) {
                  var6 = var10000;
                  return var6.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$addToCart);
               }

               byte var5 = (Boolean)var10000.join();
               if (var5 == 0) {
                  var10000 = VertxUtil.randomSleep((long)this.task.getMonitorDelay());
                  if (!var10000.isDone()) {
                     var6 = var10000;
                     return var6.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$addToCart);
                  }

                  var10000.join();
               }
            }
         } catch (Exception var7) {
            this.logger.error("Error occurred on preload: '{}'", var7.getMessage());
            var10000 = VertxUtil.randomSleep(12000L);
            if (!var10000.isDone()) {
               var6 = var10000;
               return var6.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$addToCart);
            }

            var10000.join();
         }
      }

      return CompletableFuture.failedFuture(new Exception("Failed to cart preload"));
   }

   public static CompletableFuture async$selectShipping(PaymentInstance param0, Buffer param1, HttpRequest param2, CompletableFuture param3, HttpResponse param4, Exception param5, int param6, Object param7) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture submitPayment(boolean var1) {
      Buffer var2 = this.api.getPaymentForm(this.token).toBuffer();

      while(this.api.getWebClient().isActive()) {
         HttpRequest var3 = this.api.submitPayment();
         this.logger.info("Submitting payment");

         CompletableFuture var10000;
         CompletableFuture var5;
         try {
            var10000 = Request.send(var3, var2);
            if (!var10000.isDone()) {
               var5 = var10000;
               return var5.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$submitPayment$1);
            }

            HttpResponse var4 = (HttpResponse)var10000.join();
            if (var4 != null) {
               if (var4.statusCode() == 200) {
                  var10000 = this.processPayment((boolean)var1);
                  if (!var10000.isDone()) {
                     var5 = var10000;
                     return var5.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$submitPayment$1);
                  }

                  return CompletableFuture.completedFuture((Integer)var10000.join());
               }

               this.logger.warn("Failed to submit payment: status'{}'", var4.statusCode());
               var10000 = this.api.handleBadResponse(var4.statusCode(), var4);
               if (!var10000.isDone()) {
                  var5 = var10000;
                  return var5.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$submitPayment$1);
               }

               var10000.join();
            }

            if (!this.griefMode && var4 != null && var4.statusCode() != 412 && var4.statusCode() != 307) {
               var10000 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
               if (!var10000.isDone()) {
                  var5 = var10000;
                  return var5.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$submitPayment$1);
               }

               var10000.join();
            }
         } catch (Exception var6) {
            this.logger.error("Error occurred submitting payment: '{}'", var6.getMessage());
            if (var6.getMessage().contains("Unexpected character")) {
               var10000 = VertxUtil.randomSleep(12000L);
               if (!var10000.isDone()) {
                  var5 = var10000;
                  return var5.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$submitPayment$1);
               }

               var10000.join();
            }
         }
      }

      return CompletableFuture.completedFuture(-1);
   }

   public static CompletableFuture async$getPCID(PaymentInstance param0, int param1, Buffer param2, HttpRequest param3, CompletableFuture param4, HttpResponse param5, Exception param6, int param7, Object param8) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$submitPayment(PaymentInstance param0, Buffer param1, HttpRequest param2, CompletableFuture param3, HttpResponse param4, Exception param5, int param6, Object param7) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture checkout(Walmart var0, Task var1, PaymentToken var2, Mode var3) {
      return get(var0, var1, var2, var3, true).checkout();
   }

   public static CompletableFuture async$checkout(PaymentInstance var0, int var1, PaymentInstance var2, CompletableFuture var3, double var4, CompletableFuture var6, CompletableFuture var7, long var8, int var10, Object var11) {
      CompletableFuture var10000;
      label282: {
         label256: {
            label300: {
               double var15;
               CompletableFuture var10001;
               CompletableFuture var20;
               PaymentInstance var23;
               label283: {
                  CompletableFuture var5;
                  CompletableFuture var17;
                  label284: {
                     label285: {
                        CompletableFuture var9;
                        label286: {
                           label287: {
                              label242: {
                                 label241: {
                                    label240: {
                                       label288: {
                                          label289: {
                                             label290: {
                                                long var19;
                                                label291: {
                                                   long var16;
                                                   label292: {
                                                      label262: {
                                                         label225: {
                                                            label224: {
                                                               label223: {
                                                                  label293: {
                                                                     label220: {
                                                                        label263: {
                                                                           double var24;
                                                                           boolean var25;
                                                                           switch (var10) {
                                                                              case 0:
                                                                                 var0.instanceSignal = var0.task.getKeywords()[0];

                                                                                 try {
                                                                                    var1 = Integer.parseInt(var0.task.getKeywords()[1]);
                                                                                 } catch (NumberFormatException var12) {
                                                                                    var0.logger.warn("Missing price-check (limit) for product: '{}'. Skipping...", var0.instanceSignal);
                                                                                    var1 = Integer.MAX_VALUE;
                                                                                 }

                                                                                 try {
                                                                                    var23 = var0;
                                                                                    var10001 = var0.getPCID();
                                                                                    if (!var10001.isDone()) {
                                                                                       var9 = var10001;
                                                                                       return var9.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$checkout);
                                                                                    }
                                                                                    break;
                                                                                 } catch (Throwable var14) {
                                                                                    var25 = false;
                                                                                    return CompletableFuture.completedFuture(-1);
                                                                                 }
                                                                              case 1:
                                                                                 var23 = var2;
                                                                                 var10001 = var3;
                                                                                 break;
                                                                              case 2:
                                                                                 var10000 = var7;
                                                                                 var24 = var4;
                                                                                 var17 = var3;
                                                                                 var15 = var24;
                                                                                 break label224;
                                                                              case 3:
                                                                                 var10000 = var7;
                                                                                 var24 = var4;
                                                                                 var17 = var3;
                                                                                 var15 = var24;
                                                                                 break label223;
                                                                              case 4:
                                                                                 var10000 = var7;
                                                                                 var24 = var4;
                                                                                 CompletableFuture var10003 = var6;
                                                                                 var19 = var8;
                                                                                 var5 = var10003;
                                                                                 var17 = var3;
                                                                                 var15 = var24;
                                                                                 break label291;
                                                                              case 5:
                                                                                 var10000 = var7;
                                                                                 var24 = var4;
                                                                                 var5 = var6;
                                                                                 var17 = var3;
                                                                                 var15 = var24;
                                                                                 break label290;
                                                                              case 6:
                                                                                 var10000 = var7;
                                                                                 var24 = var4;
                                                                                 var5 = var6;
                                                                                 var17 = var3;
                                                                                 var15 = var24;
                                                                                 break label241;
                                                                              case 7:
                                                                                 var23 = var2;
                                                                                 var10001 = var7;
                                                                                 double var10002 = var4;
                                                                                 var5 = var6;
                                                                                 var17 = var3;
                                                                                 var15 = var10002;
                                                                                 break label284;
                                                                              case 8:
                                                                                 var10000 = var7;
                                                                                 break label282;
                                                                              case 9:
                                                                                 var10000 = var6;
                                                                                 var24 = var4;
                                                                                 var6 = var3;
                                                                                 var16 = var8;
                                                                                 var15 = var24;
                                                                                 break label263;
                                                                              case 10:
                                                                                 var10000 = var6;
                                                                                 var15 = var4;
                                                                                 break label293;
                                                                              case 11:
                                                                                 var10000 = var6;
                                                                                 var15 = var4;
                                                                                 break label220;
                                                                              case 12:
                                                                                 var10000 = var6;
                                                                                 var24 = var4;
                                                                                 var6 = var3;
                                                                                 var16 = var8;
                                                                                 var15 = var24;
                                                                                 break label292;
                                                                              case 13:
                                                                                 var10000 = var6;
                                                                                 var6 = var3;
                                                                                 var15 = var4;
                                                                                 break label240;
                                                                              case 14:
                                                                                 var10000 = var6;
                                                                                 var6 = var3;
                                                                                 var15 = var4;
                                                                                 break label289;
                                                                              case 15:
                                                                                 var23 = var2;
                                                                                 var10001 = var6;
                                                                                 var6 = var3;
                                                                                 var15 = var4;
                                                                                 break label285;
                                                                              case 16:
                                                                                 var10000 = var6;
                                                                                 break label300;
                                                                              case 17:
                                                                                 var23 = var2;
                                                                                 var10001 = var3;
                                                                                 var15 = var4;
                                                                                 break label288;
                                                                              case 18:
                                                                                 var23 = var2;
                                                                                 var10001 = var3;
                                                                                 var15 = var4;
                                                                                 break label286;
                                                                              case 19:
                                                                                 var23 = var2;
                                                                                 var10001 = var3;
                                                                                 var15 = var4;
                                                                                 break label283;
                                                                              case 20:
                                                                                 var10000 = var3;
                                                                                 break label256;
                                                                              default:
                                                                                 throw new IllegalArgumentException();
                                                                           }

                                                                           try {
                                                                              var23.cartInfo = (JsonObject)var10001.join();
                                                                           } catch (Throwable var13) {
                                                                              var25 = false;
                                                                              return CompletableFuture.completedFuture(-1);
                                                                           }

                                                                           if (var0.cartInfo == null || !var0.cartInfo.containsKey("items")) {
                                                                              return CompletableFuture.failedFuture(new Exception("Failed to get PCID"));
                                                                           }

                                                                           var15 = var0.cartInfo.getJsonArray("items").getJsonObject(0).getDouble("unitPrice");
                                                                           if (var15 > (double)var1) {
                                                                              return CompletableFuture.failedFuture(new Exception("Price exceeds limit of $" + var1));
                                                                           }

                                                                           var0.logger.info("Attempting to checkout");
                                                                           if (!var0.griefMode) {
                                                                              var23 = var0;
                                                                              var10001 = var0.selectShipping();
                                                                              if (!var10001.isDone()) {
                                                                                 var9 = var10001;
                                                                                 return var9.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$checkout);
                                                                              }
                                                                              break label288;
                                                                           }

                                                                           if (var0.griefAlt) {
                                                                              var17 = var0.processPayment();
                                                                              var5 = var0.api.getWebClient().windowUpdateCallback();
                                                                              if (var5 != null) {
                                                                                 var0.selectShipping();
                                                                                 var10000 = VertxUtil.handleEagerFuture(var5);
                                                                                 if (!var10000.isDone()) {
                                                                                    var20 = var10000;
                                                                                    return var20.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$checkout);
                                                                                 }
                                                                                 break label224;
                                                                              }

                                                                              var10000 = var0.selectShipping();
                                                                              if (!var10000.isDone()) {
                                                                                 var20 = var10000;
                                                                                 return var20.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$checkout);
                                                                              }
                                                                              break label223;
                                                                           }

                                                                           var6 = var0.api.getWebClient().windowUpdateCallback();
                                                                           if (var6 == null) {
                                                                              var10000 = var0.selectShipping();
                                                                              if (!var10000.isDone()) {
                                                                                 var20 = var10000;
                                                                                 return var20.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$checkout);
                                                                              }
                                                                              break label220;
                                                                           }

                                                                           var16 = Instant.now().toEpochMilli();
                                                                           var0.selectShipping();
                                                                           var10000 = VertxUtil.handleEagerFuture(var6);
                                                                           if (!var10000.isDone()) {
                                                                              var20 = var10000;
                                                                              return var20.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$checkout);
                                                                           }
                                                                        }

                                                                        var10000.join();
                                                                        var10000 = VertxUtil.hardCodedSleep(50L - (Instant.now().toEpochMilli() - var16));
                                                                        if (!var10000.isDone()) {
                                                                           var20 = var10000;
                                                                           return var20.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$checkout);
                                                                        }
                                                                        break label293;
                                                                     }

                                                                     var10000.join();
                                                                     break label225;
                                                                  }

                                                                  var10000.join();
                                                                  break label225;
                                                               }

                                                               var10000.join();
                                                               break label262;
                                                            }

                                                            var10000.join();
                                                            break label262;
                                                         }

                                                         var6 = var0.api.getWebClient().windowUpdateCallback();
                                                         if (var6 == null) {
                                                            var10000 = var0.submitShipping();
                                                            if (!var10000.isDone()) {
                                                               var20 = var10000;
                                                               return var20.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$checkout);
                                                            }
                                                            break label289;
                                                         }

                                                         var16 = Instant.now().toEpochMilli();
                                                         var0.submitShipping();
                                                         var10000 = VertxUtil.handleEagerFuture(var6);
                                                         if (!var10000.isDone()) {
                                                            var20 = var10000;
                                                            return var20.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$checkout);
                                                         }
                                                         break label292;
                                                      }

                                                      var5 = var0.api.getWebClient().windowUpdateCallback();
                                                      if (var5 == null) {
                                                         var10000 = var0.submitShipping();
                                                         if (!var10000.isDone()) {
                                                            var20 = var10000;
                                                            return var20.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$checkout);
                                                         }
                                                         break label241;
                                                      }

                                                      var19 = Instant.now().toEpochMilli();
                                                      var0.submitShipping();
                                                      var10000 = VertxUtil.handleEagerFuture(var5);
                                                      if (!var10000.isDone()) {
                                                         var20 = var10000;
                                                         return var20.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$checkout);
                                                      }
                                                      break label291;
                                                   }

                                                   var10000.join();
                                                   var10000 = VertxUtil.hardCodedSleep(300L - (Instant.now().toEpochMilli() - var16));
                                                   if (!var10000.isDone()) {
                                                      var20 = var10000;
                                                      return var20.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$checkout);
                                                   }
                                                   break label240;
                                                }

                                                var10000.join();
                                                var10000 = VertxUtil.hardCodedSleep(300L - (Instant.now().toEpochMilli() - var19));
                                                if (!var10000.isDone()) {
                                                   var20 = var10000;
                                                   return var20.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$checkout);
                                                }
                                             }

                                             var10000.join();
                                             break label287;
                                          }

                                          var10000.join();
                                          break label242;
                                       }

                                       var23.cartInfo = (JsonObject)var10001.join();
                                       var23 = var0;
                                       var10001 = var0.submitShipping();
                                       if (!var10001.isDone()) {
                                          var9 = var10001;
                                          return var9.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$checkout);
                                       }
                                       break label286;
                                    }

                                    var10000.join();
                                    break label242;
                                 }

                                 var10000.join();
                                 break label287;
                              }

                              var23 = var0;
                              var10001 = var0.submitPayment();
                              if (!var10001.isDone()) {
                                 var9 = var10001;
                                 return var9.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$checkout);
                              }
                              break label285;
                           }

                           var23 = var0;
                           var10001 = var0.submitPayment();
                           if (!var10001.isDone()) {
                              var9 = var10001;
                              return var9.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$checkout);
                           }
                           break label284;
                        }

                        var23.cartInfo = (JsonObject)var10001.join();
                        var23 = var0;
                        var10001 = var0.submitPayment();
                        if (!var10001.isDone()) {
                           var9 = var10001;
                           return var9.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$checkout);
                        }
                        break label283;
                     }

                     var23.cartInfo = (JsonObject)var10001.join();
                     var10000 = var0.processPayment();
                     if (!var10000.isDone()) {
                        var20 = var10000;
                        return var20.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$checkout);
                     }
                     break label300;
                  }

                  var23.cartInfo = (JsonObject)var10001.join();
                  var10000 = var17;
                  if (!var17.isDone()) {
                     return var17.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$checkout);
                  }
                  break label282;
               }

               var23.cartInfo = (JsonObject)var10001.join();
               var10000 = var0.processPayment();
               if (!var10000.isDone()) {
                  var20 = var10000;
                  return var20.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$checkout);
               }
               break label256;
            }

            int var18 = (Integer)var10000.join();
            return CompletableFuture.completedFuture(var18);
         }

         int var21 = (Integer)var10000.join();
         return CompletableFuture.completedFuture(var21);
      }

      int var22 = (Integer)var10000.join();
      return CompletableFuture.completedFuture(var22);
   }

   public CompletableFuture submitPayment() {
      Buffer var1 = this.api.getPaymentForm(this.token).toBuffer();

      while(this.api.getWebClient().isActive()) {
         HttpRequest var2 = this.api.submitPayment();
         this.logger.info("Submitting payment");

         CompletableFuture var10000;
         CompletableFuture var4;
         try {
            var10000 = Request.send(var2, var1);
            if (!var10000.isDone()) {
               var4 = var10000;
               return var4.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$submitPayment);
            }

            HttpResponse var3 = (HttpResponse)var10000.join();
            if (var3 != null) {
               if (this.logger.isDebugEnabled()) {
                  this.logger.debug("submitPayment responded with: [{}] {}", var3.statusCode(), var3.bodyAsString());
               }

               if (var3.statusCode() == 200) {
                  return CompletableFuture.completedFuture(var3.bodyAsJsonObject());
               }

               this.logger.warn("Failed to submit payment: status'{}'", var3.statusCode());
               var10000 = this.api.handleBadResponse(var3.statusCode(), var3);
               if (!var10000.isDone()) {
                  var4 = var10000;
                  return var4.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$submitPayment);
               }

               var10000.join();
            }

            if (!this.griefMode && var3 != null && var3.statusCode() != 412 && var3.statusCode() != 307) {
               var10000 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
               if (!var10000.isDone()) {
                  var4 = var10000;
                  return var4.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$submitPayment);
               }

               var10000.join();
            }
         } catch (Exception var5) {
            this.logger.error("Error occurred submitting payment: '{}'", var5.getMessage());
            if (var5.getMessage().contains("Unexpected character")) {
               var10000 = VertxUtil.randomSleep(12000L);
               if (!var10000.isDone()) {
                  var4 = var10000;
                  return var4.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$submitPayment);
               }

               var10000.join();
            }
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture submitShipping() {
      Buffer var1 = this.api.getShippingForm(this.cartInfo).toBuffer();
      this.logger.info("Generating checkout step #3");

      while(this.api.getWebClient().isActive()) {
         HttpRequest var2 = this.api.submitShipping();

         CompletableFuture var10000;
         CompletableFuture var4;
         try {
            var10000 = Request.send(var2, var1);
            if (!var10000.isDone()) {
               var4 = var10000;
               return var4.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$submitShipping);
            }

            HttpResponse var3 = (HttpResponse)var10000.join();
            if (var3 != null) {
               if (this.logger.isDebugEnabled()) {
                  this.logger.debug("submitShipping responded with: [{}] {}", var3.statusCode(), var3.bodyAsString());
               }

               if (var3.statusCode() == 200) {
                  return CompletableFuture.completedFuture(var3.bodyAsJsonObject());
               }

               this.logger.warn("Failed to generate checkout step #3: status:'{}'", var3.statusCode());
               if (!this.isCheckout) {
                  return CompletableFuture.completedFuture((Object)null);
               }

               var10000 = this.api.handleBadResponse(var3.statusCode(), var3);
               if (!var10000.isDone()) {
                  var4 = var10000;
                  return var4.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$submitShipping);
               }

               var10000.join();
            }

            if (!this.griefMode && var3 != null && var3.statusCode() != 412 && var3.statusCode() != 307) {
               var10000 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
               if (!var10000.isDone()) {
                  var4 = var10000;
                  return var4.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$submitShipping);
               }

               var10000.join();
            }
         } catch (Exception var5) {
            this.logger.error("Error occurred on checkout step #3: '{}'", var5.getMessage());
            if (!this.isCheckout) {
               break;
            }

            if (var5.getMessage().contains("Unexpected character") || var5 instanceof DecodeException) {
               var10000 = VertxUtil.randomSleep(12000L);
               if (!var10000.isDone()) {
                  var4 = var10000;
                  return var4.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$submitShipping);
               }

               var10000.join();
            }
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture preload(Walmart var0, Task var1, PaymentToken var2, Mode var3) {
      return get(var0, var1, var2, var3, false).preload();
   }

   public CompletableFuture processPayment(boolean var1) {
      Buffer var2 = this.api.getProcessingForm(this.token).toBuffer();
      byte var3 = 0;
      int var4 = var1 != 0 ? 0 : 47;

      while(this.api.getWebClient().isActive() && var4 <= 50) {
         HttpRequest var5 = this.api.processPayment(this.token);
         this.logger.info("Processing...");

         CompletableFuture var10000;
         CompletableFuture var8;
         try {
            var10000 = Request.send(var5, var2);
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$processPayment);
            }

            HttpResponse var6 = (HttpResponse)var10000.join();
            if (var6 != null) {
               String var7 = var6.bodyAsString().toLowerCase();
               if (this.logger.isDebugEnabled()) {
                  this.logger.debug("processPayment responded with: [{}] {}", var6.statusCode(), var6.bodyAsString());
               }

               if (var6.statusCode() == 200) {
                  if (var7.contains("orderid")) {
                     if (var3 != 0) {
                        VertxUtil.sendSignal(this.instanceSignal);
                     }

                     this.logger.info("Successfully checked out with status '{}' ", var6.statusCode());
                     Analytics.success(this.task, var6.bodyAsJsonObject(), this.api.proxyStringSafe());
                     return CompletableFuture.completedFuture(200);
                  }

                  this.logger.warn("Something went wrong while processing: status'{}'", var6.bodyAsString().toLowerCase());
                  this.handleFailureWebhooks("Misc Error", (Buffer)var6.body());
                  return CompletableFuture.completedFuture(-1);
               }

               if (var7.contains("missing")) {
                  this.logger.warn("Missing payment info. Re-submitting...");
                  ++var4;
                  if (this.griefAlt && var4 <= 15 || this.griefMode && var4 <= 8) {
                     continue;
                  }
               } else if (var7.contains("different payment")) {
                  ++var4;
                  this.logger.warn("Card Decline[Invalid/FailedCharge] with status '{}'. Retrying...", var6.statusCode());
                  this.handleFailureWebhooks("Card decline (Invalid/FailedCharge)", (Buffer)var6.body());
                  if (this.griefMode && var4 <= 3) {
                     continue;
                  }

                  this.handleFailureWebhooks("Card decline (FailedCharge)", (Buffer)var6.body());
               } else if (var7.contains("different card")) {
                  ++var4;
                  this.logger.warn("Card Decline[FailedCharge] with status '{}'. Retrying..", var6.statusCode());
                  if (this.griefMode && var4 <= 3) {
                     continue;
                  }

                  this.handleFailureWebhooks("Card decline (FailedCharge)", (Buffer)var6.body());
               } else if (var7.contains("stock")) {
                  var3 = 1;
                  this.logger.info("OOS on checkout with status '{}'. Retrying...", var6.statusCode());
                  if (this.griefMode && var4++ <= 6) {
                     continue;
                  }

                  this.handleFailureWebhooks("Out Of Stock", (Buffer)var6.body());
               } else {
                  if (var6.statusCode() == 405 || var7.contains("contract has expired")) {
                     this.logger.warn("Cart has expired with status'{}'. Re-submitting.", var6.bodyAsString().toLowerCase());
                     return CompletableFuture.completedFuture(-1);
                  }

                  this.checkBadStatus(var6.statusCode());
                  var10000 = this.api.handleBadResponse(var6.statusCode(), var6);
                  if (!var10000.isDone()) {
                     var8 = var10000;
                     return var8.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$processPayment);
                  }

                  var10000.join();
               }
            }

            if (var6 != null && var6.statusCode() != 412) {
               var10000 = VertxUtil.randomSignalSleep(this.instanceSignal, (long)this.task.getRetryDelay());
               if (!var10000.isDone()) {
                  var8 = var10000;
                  return var8.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$processPayment);
               }

               var10000.join();
            }
         } catch (Exception var9) {
            this.logger.error("Error occurred while processing payment: '{}'", var9.getMessage());
            if (var9.getMessage().contains("Unexpected character")) {
               var10000 = VertxUtil.randomSleep(12000L);
               if (!var10000.isDone()) {
                  var8 = var10000;
                  return var8.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$processPayment);
               }

               var10000.join();
            }
         }
      }

      return CompletableFuture.completedFuture(-1);
   }

   public static CompletableFuture async$submitPayment$1(PaymentInstance param0, int param1, Buffer param2, HttpRequest param3, CompletableFuture param4, HttpResponse param5, Exception param6, int param7, Object param8) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$addToCart(PaymentInstance param0, Buffer param1, int param2, HttpRequest param3, CompletableFuture param4, HttpResponse param5, int param6, Exception param7, int param8, Object param9) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture getPCID() {
      int var1 = 0;
      Buffer var2 = this.api.PCIDForm().toBuffer();
      this.logger.info("Generating checkout step #1");

      while(true) {
         if (this.api.getWebClient().isActive() && var1 <= 50) {
            HttpRequest var3 = this.api.getPCID(this.token);

            CompletableFuture var10000;
            CompletableFuture var5;
            try {
               var10000 = Request.send(var3, var2);
               if (!var10000.isDone()) {
                  var5 = var10000;
                  return var5.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$getPCID);
               }

               HttpResponse var4 = (HttpResponse)var10000.join();
               if (var4 != null) {
                  if (this.logger.isDebugEnabled()) {
                     this.logger.debug("PCID responded with: [{}] {}", var4.statusCode(), var4.bodyAsString());
                  }

                  if (var4.statusCode() == 201) {
                     if (this.isCheckout) {
                        VertxUtil.sendSignal(this.instanceSignal);
                     }

                     return CompletableFuture.completedFuture(var4.bodyAsJsonObject());
                  }

                  if (var4.statusCode() == 405) {
                     throw new Throwable("405 error. Restarting session...");
                  }

                  this.logger.warn("Failed to generate checkout step #1: status:'{}'", var4.statusCode());
                  if (this.isCheckout && !var4.bodyAsString().contains("cart_empty") && var1++ <= 5) {
                     var10000 = this.api.handleBadResponse(var4.statusCode(), var4);
                     if (!var10000.isDone()) {
                        var5 = var10000;
                        return var5.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$getPCID);
                     }

                     var10000.join();
                     if (var4.statusCode() != 412 && var4.statusCode() != 307) {
                        var10000 = VertxUtil.randomSignalSleep(this.instanceSignal, (long)this.task.getRetryDelay());
                        if (!var10000.isDone()) {
                           var5 = var10000;
                           return var5.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$getPCID);
                        }

                        var10000.join();
                     }
                     continue;
                  }

                  return CompletableFuture.completedFuture((Object)null);
               }

               ++var1;
               continue;
            } catch (Exception var6) {
               if (this.isCheckout) {
                  this.logger.error("Error occurred on checkout step #1: '{}'", var6.getMessage());
                  if (!var6.getMessage().contains("Unexpected character") && !(var6 instanceof DecodeException)) {
                     continue;
                  }

                  var10000 = VertxUtil.randomSleep(12000L);
                  if (!var10000.isDone()) {
                     var5 = var10000;
                     return var5.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$getPCID);
                  }

                  var10000.join();
                  continue;
               }
            }
         }

         return CompletableFuture.completedFuture((Object)null);
      }
   }

   public PaymentInstance(Walmart var1, Task var2, PaymentToken var3, Mode var4, boolean var5) {
      this.parent = var1;
      this.api = (API)var1.getClient();
      this.logger = var1.getLogger();
      this.task = var2;
      this.token = var3;
      this.isCheckout = var5;
      this.mode = var4;
      if (this.mode == Mode.DESKTOP) {
         this.griefMode = this.griefAlt = false;
      } else {
         this.griefMode = this.task.getMode().toLowerCase().contains("grief");
         this.griefAlt = this.task.getMode().toLowerCase().contains("griefalt");
      }

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

   public CompletableFuture processPayment() {
      return this.processPayment(true);
   }

   public static PaymentInstance get(Walmart var0, Task var1, PaymentToken var2, Mode var3, boolean var4) {
      return new PaymentInstance(var0, var1, var2, var3, var4);
   }

   public CompletableFuture selectShipping() {
      Buffer var1 = this.api.getShippingRateForm(this.cartInfo).toBuffer();
      this.logger.info("Generating checkout step #2");

      while(this.api.getWebClient().isActive()) {
         HttpRequest var2 = this.api.selectShipping();

         CompletableFuture var10000;
         CompletableFuture var4;
         try {
            var10000 = Request.send(var2, var1);
            if (!var10000.isDone()) {
               var4 = var10000;
               return var4.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$selectShipping);
            }

            HttpResponse var3 = (HttpResponse)var10000.join();
            if (var3 != null) {
               if (this.logger.isDebugEnabled()) {
                  this.logger.debug("selectShipping responded with: [{}] {}", var3.statusCode(), var3.bodyAsString());
               }

               if (var3.statusCode() == 200) {
                  return CompletableFuture.completedFuture(var3.bodyAsJsonObject());
               }

               this.logger.warn("Failed to generate checkout step #2: status:'{}'", var3.statusCode());
               if (!this.isCheckout) {
                  return CompletableFuture.completedFuture((Object)null);
               }

               var10000 = this.api.handleBadResponse(var3.statusCode(), var3);
               if (!var10000.isDone()) {
                  var4 = var10000;
                  return var4.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$selectShipping);
               }

               var10000.join();
            }

            if (!this.griefMode && var3 != null && var3.statusCode() != 412 && var3.statusCode() != 307) {
               var10000 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
               if (!var10000.isDone()) {
                  var4 = var10000;
                  return var4.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$selectShipping);
               }

               var10000.join();
            }
         } catch (Exception var5) {
            this.logger.error("Error occurred on checkout step #2: '{}'", var5.getMessage());
            if (!this.isCheckout) {
               break;
            }

            if (var5.getMessage().contains("Unexpected character") || var5 instanceof DecodeException) {
               var10000 = VertxUtil.randomSleep(12000L);
               if (!var10000.isDone()) {
                  var4 = var10000;
                  return var4.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$selectShipping);
               }

               var10000.join();
            }
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture submitBilling() {
      Buffer var1 = this.api.getBillingForm(this.token).toBuffer();
      this.logger.info("Generating checkout step #4");

      while(this.api.getWebClient().isActive()) {
         HttpRequest var2 = this.api.submitBilling();

         CompletableFuture var10000;
         CompletableFuture var4;
         try {
            var10000 = Request.send(var2, var1);
            if (!var10000.isDone()) {
               var4 = var10000;
               return var4.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$submitBilling);
            }

            HttpResponse var3 = (HttpResponse)var10000.join();
            if (var3 != null) {
               if (this.logger.isDebugEnabled()) {
                  this.logger.debug("submitBilling responded with: [{}] {}", var3.statusCode(), var3.bodyAsString());
               }

               if (var3.statusCode() == 200) {
                  return CompletableFuture.completedFuture(var3.bodyAsJsonObject());
               }

               this.logger.warn("Failed to generate checkout step #4: status:'{}'", var3.statusCode());
               if (!this.isCheckout) {
                  return CompletableFuture.completedFuture((Object)null);
               }

               var10000 = this.api.handleBadResponse(var3.statusCode(), var3);
               if (!var10000.isDone()) {
                  var4 = var10000;
                  return var4.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$submitBilling);
               }

               var10000.join();
            }

            if (!this.griefMode && var3 != null && var3.statusCode() != 412 && var3.statusCode() != 307) {
               var10000 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
               if (!var10000.isDone()) {
                  var4 = var10000;
                  return var4.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$submitBilling);
               }

               var10000.join();
            }
         } catch (Exception var5) {
            this.logger.error("Error occurred on checkout step #4: '{}'", var5.getMessage());
            if (!this.isCheckout) {
               break;
            }

            if (var5.getMessage().contains("Unexpected character") || var5 instanceof DecodeException) {
               var10000 = VertxUtil.randomSleep(12000L);
               if (!var10000.isDone()) {
                  var4 = var10000;
                  return var4.exceptionally(Function.identity()).thenCompose(PaymentInstance::async$submitBilling);
               }

               var10000.join();
            }
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$processPayment(PaymentInstance param0, int param1, Buffer param2, int param3, int param4, HttpRequest param5, CompletableFuture param6, HttpResponse param7, String param8, Exception param9, int param10, Object param11) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$submitBilling(PaymentInstance param0, Buffer param1, HttpRequest param2, CompletableFuture param3, HttpResponse param4, Exception param5, int param6, Object param7) {
      // $FF: Couldn't be decompiled
   }
}
