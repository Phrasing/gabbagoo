package io.trickle.task.sites.shopify;

import io.trickle.task.Task;
import io.trickle.task.sites.shopify.util.SiteParser;
import io.trickle.task.sites.shopify.util.VariantHandler;
import io.trickle.util.analytics.Analytics;
import io.trickle.util.concurrent.VertxUtil;
import io.vertx.core.json.JsonObject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class ShopifyBackend extends Shopify {
   public boolean isPassword = true;

   public static CompletableFuture async$run(ShopifyBackend param0, CompletableFuture param1, String param2, String param3, JsonObject param4, int param5, int param6, Object param7) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture run() {
      CompletableFuture var10000 = initHarvesters();
      CompletableFuture var5;
      if (!var10000.isDone()) {
         var5 = var10000;
         return var5.exceptionally(Function.identity()).thenCompose(ShopifyBackend::async$run);
      } else {
         var10000.join();
         this.paymentGateway = SiteParser.getGatewayFromSite(this.task.getSite(), this.isCod);
         var10000 = this.initShopDetails();
         if (!var10000.isDone()) {
            var5 = var10000;
            return var5.exceptionally(Function.identity()).thenCompose(ShopifyBackend::async$run);
         } else {
            var10000.join();
            if (this.task.getMode().contains("login")) {
               this.logger.info("Login required. Logging in...");
               var10000 = this.login();
               if (!var10000.isDone()) {
                  var5 = var10000;
                  return var5.exceptionally(Function.identity()).thenCompose(ShopifyBackend::async$run);
               }

               var10000.join();
            }

            while(this.api.getWebClient().isActive()) {
               try {
                  String var1;
                  if (!this.isExtra) {
                     var10000 = this.genCheckoutURLIgnorePassword(true);
                     if (!var10000.isDone()) {
                        var5 = var10000;
                        return var5.exceptionally(Function.identity()).thenCompose(ShopifyBackend::async$run);
                     }

                     var1 = (String)var10000.join();
                     if (var1 != null) {
                        this.isPassword = false;
                        var10000 = this.handlePreload(var1);
                        if (!var10000.isDone()) {
                           var5 = var10000;
                           return var5.exceptionally(Function.identity()).thenCompose(ShopifyBackend::async$run);
                        }

                        var1 = (String)var10000.join();
                        var10000 = this.shippingAndBillingPut(var1);
                        if (!var10000.isDone()) {
                           var5 = var10000;
                           return var5.exceptionally(Function.identity()).thenCompose(ShopifyBackend::async$run);
                        }

                        var10000.join();
                     }
                  } else {
                     this.isPassword = false;
                     var10000 = this.waitTilCartCookie();
                     if (!var10000.isDone()) {
                        var5 = var10000;
                        return var5.exceptionally(Function.identity()).thenCompose(ShopifyBackend::async$run);
                     }

                     var10000.join();
                     var10000 = this.walletsGenCheckout();
                     if (!var10000.isDone()) {
                        var5 = var10000;
                        return var5.exceptionally(Function.identity()).thenCompose(ShopifyBackend::async$run);
                     }

                     var1 = (String)var10000.join();
                     var10000 = this.handlePreload(var1);
                     if (!var10000.isDone()) {
                        var5 = var10000;
                        return var5.exceptionally(Function.identity()).thenCompose(ShopifyBackend::async$run);
                     }

                     var1 = (String)var10000.join();
                     var10000 = this.shippingAndBillingPut(var1);
                     if (!var10000.isDone()) {
                        var5 = var10000;
                        return var5.exceptionally(Function.identity()).thenCompose(ShopifyBackend::async$run);
                     }

                     var10000.join();
                  }

                  JsonObject var7;
                  if (this.isKeyword) {
                     String var2 = null;

                     while(var2 == null) {
                        var10000 = this.fetchProductsJSON(true);
                        if (!var10000.isDone()) {
                           var5 = var10000;
                           return var5.exceptionally(Function.identity()).thenCompose(ShopifyBackend::async$run);
                        }

                        JsonObject var3 = (JsonObject)var10000.join();
                        var2 = VariantHandler.selectVariantFromKeyword(var3, this.task.getSize(), this.positiveKeywords, this.negativeKeywords, this);
                        if (var2 == null) {
                           var10000 = VertxUtil.signalSleep(this.instanceSignal, (long)this.task.getMonitorDelay());
                           if (!var10000.isDone()) {
                              var5 = var10000;
                              return var5.exceptionally(Function.identity()).thenCompose(ShopifyBackend::async$run);
                           }

                           Object var4 = var10000.join();
                           if (var4 != null) {
                              var2 = VariantHandler.selectVariantFromKeyword(new JsonObject(var4.toString()), this.task.getSize(), this.positiveKeywords, this.negativeKeywords, this);
                           }
                        } else {
                           VertxUtil.sendSignal(this.instanceSignal, var3.encode());
                        }
                     }

                     this.instanceSignal = var2;
                  } else if (this.isEL) {
                     var10000 = this.fetchELJS();
                     if (!var10000.isDone()) {
                        var5 = var10000;
                        return var5.exceptionally(Function.identity()).thenCompose(ShopifyBackend::async$run);
                     }

                     var7 = (JsonObject)var10000.join();
                     this.instanceSignal = VariantHandler.selectVariantFromLink(var7, this.task.getSize(), this);
                  }

                  this.setProductPickupTime();
                  this.genPaymentToken();
                  if (this.isPassword) {
                     var10000 = this.shippingAndBillingPost();
                     if (!var10000.isDone()) {
                        var5 = var10000;
                        return var5.exceptionally(Function.identity()).thenCompose(ShopifyBackend::async$run);
                     }

                     var1 = (String)var10000.join();
                     Analytics.carts.increment();
                     var10000 = this.handlePreload(var1);
                     if (!var10000.isDone()) {
                        var5 = var10000;
                        return var5.exceptionally(Function.identity()).thenCompose(ShopifyBackend::async$run);
                     }

                     var1 = (String)var10000.join();
                     var10000 = this.shippingAndBillingPut(var1);
                     if (!var10000.isDone()) {
                        var5 = var10000;
                        return var5.exceptionally(Function.identity()).thenCompose(ShopifyBackend::async$run);
                     }

                     var10000.join();
                  } else {
                     var10000 = this.walletPut(var1, false, false);
                     if (!var10000.isDone()) {
                        var5 = var10000;
                        return var5.exceptionally(Function.identity()).thenCompose(ShopifyBackend::async$run);
                     }

                     byte var8 = (Boolean)var10000.join();
                     Analytics.carts.increment();
                     if (var8 == 0) {
                        if (ThreadLocalRandom.current().nextBoolean()) {
                           var10000 = this.genCheckoutURL(ThreadLocalRandom.current().nextBoolean());
                           if (!var10000.isDone()) {
                              var5 = var10000;
                              return var5.exceptionally(Function.identity()).thenCompose(ShopifyBackend::async$run);
                           }

                           var10000.join();
                        } else {
                           var10000 = this.genCheckoutURLViaCart();
                           if (!var10000.isDone()) {
                              var5 = var10000;
                              return var5.exceptionally(Function.identity()).thenCompose(ShopifyBackend::async$run);
                           }

                           var10000.join();
                        }
                     }
                  }

                  if (!this.shippingRate.isDone()) {
                     this.getShippingRateOld();
                     this.getShippingRateAPI(var1);
                  }

                  String var9;
                  do {
                     var10000 = this.processPaymentAPI(var1);
                     if (!var10000.isDone()) {
                        var5 = var10000;
                        return var5.exceptionally(Function.identity()).thenCompose(ShopifyBackend::async$run);
                     }

                     var10000.join();
                     var10000 = this.checkOrderAPI(var1);
                     if (!var10000.isDone()) {
                        var5 = var10000;
                        return var5.exceptionally(Function.identity()).thenCompose(ShopifyBackend::async$run);
                     }

                     var7 = (JsonObject)var10000.join();
                     var9 = parseDecline(var7);
                     if (var9 != null && (var9.toLowerCase().contains("card was declined") || var9.toLowerCase().contains("transaction has been declined") || var9.toLowerCase().contains("couldn't be verified") || var9.toLowerCase().contains("an issue processing your pay"))) {
                        Analytics.failure(var9, this.task, var7, this.api.proxyStringSafe());
                     }

                     this.logger.info("Checkout -> " + var9);
                  } while(var9 != null && !var9.equals("success"));

                  this.logger.info("Successfully checked out.");
                  Analytics.success(this.task, var7, this.api.proxyStringSafe());
                  break;
               } catch (Throwable var6) {
                  this.logger.error("Task interrupted: " + var6.getMessage());
                  this.setAttributes();
               }
            }

            return null;
         }
      }
   }

   public ShopifyBackend(Task var1, int var2) {
      super(var1, var2);
   }
}
