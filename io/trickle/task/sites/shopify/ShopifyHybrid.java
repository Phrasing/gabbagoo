package io.trickle.task.sites.shopify;

import com.teamdev.jxbrowser.net.UploadData;
import io.trickle.basicgui.LinkOverrideListener;
import io.trickle.harvester.HybridHarvester;
import io.trickle.task.Task;
import io.trickle.util.Pair;
import io.trickle.util.Utils;
import io.trickle.util.analytics.Analytics;
import io.trickle.util.concurrent.ContextCompletableFuture;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.http.RequestOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import java.time.Instant;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class ShopifyHybrid extends Shopify {
   public int id;
   public MessageConsumer cartDataConsumer;
   public MessageConsumer linkChangeConsumer;

   public CompletableFuture waitForLink() {
      ContextCompletableFuture var1 = new ContextCompletableFuture();
      this.linkChangeConsumer = super.vertx.eventBus().localConsumer(LinkOverrideListener.MASS_LINK_CHANGE_ADDRESS, ShopifyHybrid::lambda$waitForLink$0);
      return var1;
   }

   public CompletableFuture send(RequestOptions var1, Optional var2) {
      this.logger.info("Adding to cart");
      int var3 = 0;

      while(var3++ < Integer.MAX_VALUE) {
         CompletableFuture var7;
         CompletableFuture var10;
         try {
            HttpRequest var9 = this.api.getWebClient().request(var1.getMethod(), var1);
            HttpResponse var11;
            if (var2.isEmpty()) {
               var10 = Request.send(var9);
               if (!var10.isDone()) {
                  var7 = var10;
                  return var7.exceptionally(Function.identity()).thenCompose(ShopifyHybrid::async$send);
               }

               var11 = (HttpResponse)var10.join();
            } else {
               var10 = Request.send(var9, Buffer.buffer(((UploadData)var2.get()).bytes()));
               if (!var10.isDone()) {
                  var7 = var10;
                  return var7.exceptionally(Function.identity()).thenCompose(ShopifyHybrid::async$send);
               }

               var11 = (HttpResponse)var10.join();
            }

            HttpResponse var6 = var11;
            if (var6 != null && this.api.getCookies().contains("cart")) {
               return CompletableFuture.completedFuture(var6);
            }

            if (var6 != null) {
               this.logger.error("Bad atc response {}", var6.statusCode());
            } else {
               this.logger.error("No response {}", var1.getURI());
            }
         } catch (Throwable var8) {
            String var5 = var8.getMessage();
            if (var5.contains("timeout period")) {
               this.logger.error("request timeout. no response");
            } else if (var5.contains("proxy")) {
               this.logger.error("unable to connect to proxy - {}", var5);
            } else {
               this.logger.error("network error " + var8.getMessage());
            }
         }

         var10 = VertxUtil.randomSleep(1000L);
         if (!var10.isDone()) {
            var7 = var10;
            return var7.exceptionally(Function.identity()).thenCompose(ShopifyHybrid::async$send);
         }

         var10.join();
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture waitForCartData() {
      ContextCompletableFuture var1 = new ContextCompletableFuture();
      this.cartDataConsumer = super.vertx.eventBus().localConsumer(LinkOverrideListener.CART_ADDRESS, ShopifyHybrid::lambda$waitForCartData$1);
      return var1;
   }

   public static CompletableFuture async$run(ShopifyHybrid param0, int param1, CompletableFuture param2, CompletableFuture param3, HybridHarvester param4, CompletableFuture param5, String param6, String[] param7, int param8, Pair param9, int param10, Object param11) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture run() {
      byte var1 = 1;
      String var2 = null;

      try {
         CompletableFuture var3 = this.waitForLink();
         CompletableFuture var4 = this.waitForCartData();
         CompletableFuture var8;
         CompletableFuture var10000;
         if (this.id == 1) {
            HybridHarvester var5 = new HybridHarvester(super.vertx);
            var10000 = var5.start();
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(ShopifyHybrid::async$run);
            }

            var10000.join();
         }

         var10000 = initHarvesters();
         if (!var10000.isDone()) {
            var8 = var10000;
            return var8.exceptionally(Function.identity()).thenCompose(ShopifyHybrid::async$run);
         } else {
            var10000.join();
            String[] var14 = this.task.getKeywords();
            if (!var3.isDone()) {
               byte var9 = 0;
               String[] var13 = var14;
               return var3.exceptionally(Function.identity()).thenCompose(ShopifyHybrid::async$run);
            } else {
               var14[0] = (String)var3.join();
               this.task.getKeywords()[0] = this.task.getKeywords()[0].toLowerCase(Locale.ROOT);
               this.linkChangeConsumer.unregister();
               this.linkChangeConsumer = null;
               this.api.SITE_URL = this.task.getKeywords()[0].toLowerCase(Locale.ROOT).split("https://")[1].split("/")[0];
               var10000 = this.initShopDetails();
               if (!var10000.isDone()) {
                  var8 = var10000;
                  return var8.exceptionally(Function.identity()).thenCompose(ShopifyHybrid::async$run);
               } else {
                  var10000.join();
                  this.api.getCookies().put("_landing_page", "%2Fproducts%2F" + ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE), this.api.getSiteURL());
                  this.api.getCookies().put("_orig_referrer", "", this.api.getSiteURL());
                  this.api.getCookies().put("_shopify_sa_p", "", this.api.getSiteURL());
                  this.api.getCookies().put("_shopify_country", "United+States", this.api.getSiteURL());
                  this.api.getCookies().put("_shopify_sa_t", Utils.encodedDateISO(Instant.now()), this.api.getSiteURL());
                  this.api.getCookies().put("_shopify_fs", Utils.encodedDateISO(instanceTime), this.api.getSiteURL());
                  this.setProductPickupTime();
                  this.genPaymentToken();
                  this.presolver = new Presolver(this);
                  this.cpMonitor = this.presolver.run();
                  if (this.task.getMode().contains("login")) {
                     this.logger.info("Login required. Logging in...");
                     var10000 = this.login();
                     if (!var10000.isDone()) {
                        var8 = var10000;
                        return var8.exceptionally(Function.identity()).thenCompose(ShopifyHybrid::async$run);
                     }

                     var10000.join();
                  }

                  var10000 = this.waitTilCartCookie();
                  if (!var10000.isDone()) {
                     var8 = var10000;
                     return var8.exceptionally(Function.identity()).thenCompose(ShopifyHybrid::async$run);
                  } else {
                     var10000.join();
                     var10000 = this.walletsGenCheckout();
                     if (!var10000.isDone()) {
                        var8 = var10000;
                        return var8.exceptionally(Function.identity()).thenCompose(ShopifyHybrid::async$run);
                     } else {
                        var2 = (String)var10000.join();
                        var10000 = this.handlePreload(var2);
                        if (!var10000.isDone()) {
                           var8 = var10000;
                           return var8.exceptionally(Function.identity()).thenCompose(ShopifyHybrid::async$run);
                        } else {
                           var2 = (String)var10000.join();
                           if (!var4.isDone()) {
                              return var4.exceptionally(Function.identity()).thenCompose(ShopifyHybrid::async$run);
                           } else {
                              Pair var12 = (Pair)var4.join();
                              this.cartDataConsumer.unregister();
                              this.cartDataConsumer = null;
                              var10000 = this.send((RequestOptions)var12.first, (Optional)var12.second);
                              if (!var10000.isDone()) {
                                 var8 = var10000;
                                 return var8.exceptionally(Function.identity()).thenCompose(ShopifyHybrid::async$run);
                              } else {
                                 var10000.join();
                                 var10000 = this.getCheckoutURL(var2);
                                 if (!var10000.isDone()) {
                                    var8 = var10000;
                                    return var8.exceptionally(Function.identity()).thenCompose(ShopifyHybrid::async$run);
                                 } else {
                                    var10000.join();
                                    this.shutOffPresolver(false);
                                    var10000 = this.submitContact(var2);
                                    if (!var10000.isDone()) {
                                       var8 = var10000;
                                       return var8.exceptionally(Function.identity()).thenCompose(ShopifyHybrid::async$run);
                                    } else {
                                       var10000.join();
                                       var10000 = this.walletsSubmitShipping(var2);
                                       if (!var10000.isDone()) {
                                          var8 = var10000;
                                          return var8.exceptionally(Function.identity()).thenCompose(ShopifyHybrid::async$run);
                                       } else {
                                          var10000.join();
                                          var10000 = this.getProcessingPage(var2, false);
                                          if (!var10000.isDone()) {
                                             var8 = var10000;
                                             return var8.exceptionally(Function.identity()).thenCompose(ShopifyHybrid::async$run);
                                          } else {
                                             var10000.join();

                                             JsonObject var6;
                                             String var7;
                                             do {
                                                var10000 = this.processPayment(var2);
                                                if (!var10000.isDone()) {
                                                   var8 = var10000;
                                                   return var8.exceptionally(Function.identity()).thenCompose(ShopifyHybrid::async$run);
                                                }

                                                var10000.join();
                                                var10000 = this.checkOrderAPI(var2);
                                                if (!var10000.isDone()) {
                                                   var8 = var10000;
                                                   return var8.exceptionally(Function.identity()).thenCompose(ShopifyHybrid::async$run);
                                                }

                                                var6 = (JsonObject)var10000.join();
                                                var7 = parseDecline(var6);
                                                if (var7 != null && !var7.equals("success")) {
                                                   Analytics.failure(var7, this.task, var6, this.api.proxyStringSafe());
                                                   this.logger.info("Checkout fail -> " + var7);
                                                }
                                             } while(var7 != null && !var7.equals("success"));

                                             this.logger.info("Successfully checked out.");
                                             Analytics.success(this.task, var6, this.api.proxyStringSafe());
                                             return null;
                                          }
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      } catch (Throwable var11) {
         this.logger.error("Task interrupted: " + var11.getMessage());
         this.setAttributes();
         return this.run();
      }
   }

   public ShopifyHybrid(Task var1, int var2) {
      super(var1, var2);
      this.id = var2;
   }

   public static void lambda$waitForCartData$1(ContextCompletableFuture var0, Message var1) {
      if (!var0.isDone() && var1.body() != null) {
         var0.complete((Pair)var1.body());
      }

   }

   public static void lambda$waitForLink$0(ContextCompletableFuture var0, Message var1) {
      if (!var0.isDone() && var1.body() != null && !((String)var1.body()).isBlank()) {
         var0.complete((String)var1.body());
      }

   }

   public static CompletableFuture async$send(ShopifyHybrid param0, RequestOptions param1, Optional param2, int param3, RequestOptions param4, HttpRequest param5, CompletableFuture param6, int param7, Object param8) {
      // $FF: Couldn't be decompiled
   }
}
