package io.trickle.task.sites.shopify;

import io.trickle.task.Task;
import io.trickle.task.sites.Site;
import io.trickle.task.sites.shopify.util.ShippingRateSupplier;
import io.trickle.task.sites.shopify.util.SiteParser;
import io.trickle.task.sites.shopify.util.Triplet;
import io.trickle.task.sites.shopify.util.VariantHandler;
import io.trickle.util.analytics.Analytics;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import org.apache.logging.log4j.Logger;

public class ShopifySafe extends Shopify {
   public boolean isPreload;
   public boolean fastPreload;
   public int id;
   public boolean isContactpreload;

   public Triplet findPreset() {
      return this.task.getSite() == Site.HUMANMADE ? new Triplet("40044994953252", true, (Object)null) : null;
   }

   public CompletableFuture preloadContactOnly() {
      CompletableFuture var10000 = this.fetchProductsJSON(false);
      CompletableFuture var5;
      if (!var10000.isDone()) {
         var5 = var10000;
         return var5.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadContactOnly);
      } else {
         JsonArray var1 = ((JsonObject)var10000.join()).getJsonArray("products");
         var10000 = VariantHandler.findPrecartVariant(var1);
         if (!var10000.isDone()) {
            var5 = var10000;
            return var5.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadContactOnly);
         } else {
            Triplet var2 = (Triplet)var10000.join();
            if (var2 == null) {
               this.isContactpreload = false;
               this.logger.error("There is no item to preload. Handling...");
               return CompletableFuture.completedFuture((Object)null);
            } else {
               this.shippingRate = new CompletableFuture();
               String var3 = (String)var2.first;
               this.precartItemName = var3;
               if (this.task.getSite() == Site.KITH) {
                  var10000 = this.atcBasic(var3);
                  if (!var10000.isDone()) {
                     var5 = var10000;
                     return var5.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadContactOnly);
                  }

                  var10000.join();
               } else {
                  var10000 = this.atcAJAX(var3, false);
                  if (!var10000.isDone()) {
                     var5 = var10000;
                     return var5.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadContactOnly);
                  }

                  var10000.join();
               }

               var10000 = this.genCheckoutURL(false);
               if (!var10000.isDone()) {
                  var5 = var10000;
                  return var5.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadContactOnly);
               } else {
                  String var4 = (String)var10000.join();
                  var10000 = this.handlePreload(var4);
                  if (!var10000.isDone()) {
                     var5 = var10000;
                     return var5.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadContactOnly);
                  } else {
                     var4 = (String)var10000.join();
                     if (!(Boolean)var2.second) {
                        this.api.setOOS();
                        this.isContactpreload = false;
                     } else {
                        var10000 = this.getCheckoutURL(var4);
                        if (!var10000.isDone()) {
                           var5 = var10000;
                           return var5.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadContactOnly);
                        }

                        var10000.join();
                        if (this.api.getCookies().contains("_shopify_checkpoint")) {
                           var10000 = this.checkCaptcha(var4);
                           if (!var10000.isDone()) {
                              var5 = var10000;
                              return var5.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadContactOnly);
                           }

                           var10000.join();
                        }

                        var10000 = this.submitContact(var4);
                        if (!var10000.isDone()) {
                           var5 = var10000;
                           return var5.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadContactOnly);
                        }

                        var10000.join();
                        this.isContactpreload = true;
                     }

                     var10000 = this.clearWithChangeJS();
                     if (!var10000.isDone()) {
                        var5 = var10000;
                        return var5.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadContactOnly);
                     } else {
                        var10000.join();
                        var10000 = this.checkCart();
                        if (!var10000.isDone()) {
                           var5 = var10000;
                           return var5.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadContactOnly);
                        } else {
                           var10000.join();
                           var10000 = this.confirmClear(var4);
                           if (!var10000.isDone()) {
                              var5 = var10000;
                              return var5.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadContactOnly);
                           } else {
                              var10000.join();
                              this.api.checkIsOOS();
                              this.configureShippingRate();
                              return CompletableFuture.completedFuture(var4);
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public CompletableFuture preload() {
      CompletableFuture var10000 = this.fetchProductsJSON(false);
      CompletableFuture var8;
      if (!var10000.isDone()) {
         var8 = var10000;
         return var8.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
      } else {
         JsonArray var1 = ((JsonObject)var10000.join()).getJsonArray("products");
         String var2 = null;
         int var3 = 0;

         while(true) {
            if (this.api.getWebClient().isActive() && var3++ < 5000000) {
               Triplet var14;
               if (var3 == 1 && this.findPreset() != null) {
                  var14 = this.findPreset();
               } else {
                  var10000 = VariantHandler.findPrecartVariant(var1);
                  if (!var10000.isDone()) {
                     var8 = var10000;
                     return var8.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                  }

                  var14 = (Triplet)var10000.join();
               }

               Triplet var4 = var14;
               if (var4 == null) {
                  this.isPreload = false;
                  this.logger.error("There is no item to preload. Handling...");
                  return CompletableFuture.completedFuture((Object)null);
               }

               this.shippingRate = new CompletableFuture();
               String var5 = (String)var4.first;
               this.precartItemName = var5;
               if (this.task.getSite() == Site.KITH) {
                  var10000 = this.atcBasic(var5);
                  if (!var10000.isDone()) {
                     var8 = var10000;
                     return var8.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                  }

                  var10000.join();
               } else {
                  var10000 = this.atcAJAX(var5, false);
                  if (!var10000.isDone()) {
                     var8 = var10000;
                     return var8.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                  }

                  var10000.join();
               }

               var10000 = this.genCheckoutURLViaCart();
               if (!var10000.isDone()) {
                  var8 = var10000;
                  return var8.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
               }

               var2 = (String)var10000.join();
               var10000 = this.handlePreload(var2);
               if (!var10000.isDone()) {
                  var8 = var10000;
                  return var8.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
               }

               var2 = (String)var10000.join();
               if (!(Boolean)var4.second) {
                  this.isPreload = false;
               } else {
                  var10000 = this.getCheckoutURL(var2);
                  if (!var10000.isDone()) {
                     var8 = var10000;
                     return var8.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                  }

                  var10000.join();
                  if (this.api.getCookies().contains("_shopify_checkpoint")) {
                     var10000 = this.checkCaptcha(var2);
                     if (!var10000.isDone()) {
                        var8 = var10000;
                        return var8.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var10000.join();
                  }

                  var10000 = this.submitContact(var2);
                  if (!var10000.isDone()) {
                     var8 = var10000;
                     return var8.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                  }

                  var10000.join();
                  CompletableFuture var6 = this.getShippingRateOld();
                  var10000 = CompletableFuture.allOf(var6, this.getShippingPage(var2));
                  if (!var10000.isDone()) {
                     var8 = var10000;
                     return var8.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                  }

                  var10000.join();
                  var10000 = this.shippingRate;
                  if (!var10000.isDone()) {
                     var8 = var10000;
                     return var8.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                  }

                  if (!((ShippingRateSupplier)var10000.join()).get().equals(this.task.getShippingRate()) && this.task.getShippingRate().length() > 3) {
                     Logger var15 = this.logger;
                     CompletableFuture var10002 = this.shippingRate;
                     if (!var10002.isDone()) {
                        CompletableFuture var10 = var10002;
                        String var9 = "The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]";
                        Logger var12 = var15;
                        return var10.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var15.error("The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]", ((ShippingRateSupplier)var10002.join()).get(), this.task.getShippingRate());
                     var10000 = this.clearWithChangeJS();
                     if (!var10000.isDone()) {
                        var8 = var10000;
                        return var8.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var10000.join();
                     this.shippingRate = new CompletableFuture();
                     if (var1.isEmpty()) {
                        this.logger.info("There are no items with matching shipping rate. Handling...");
                        this.api.checkIsOOS();
                        this.configureShippingRate();
                        this.isPreload = false;
                        this.isContactpreload = true;
                        return CompletableFuture.completedFuture(var2);
                     }

                     try {
                        var1.remove(var4.third);
                     } catch (Exception var11) {
                     }
                     continue;
                  }

                  var10000 = this.submitShipping(var2);
                  if (!var10000.isDone()) {
                     var8 = var10000;
                     return var8.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                  }

                  var10000.join();
               }
            }

            var10000 = this.clearWithChangeJS();
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
            }

            var10000.join();
            var10000 = this.checkCart();
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
            }

            var10000.join();
            this.api.checkIsOOS();
            this.configureShippingRate();
            return CompletableFuture.completedFuture(var2);
         }
      }
   }

   public CompletableFuture preloadWallets() {
      CompletableFuture var10000 = this.fetchProductsJSON(false);
      CompletableFuture var6;
      if (!var10000.isDone()) {
         var6 = var10000;
         return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
      } else {
         JsonArray var1 = ((JsonObject)var10000.join()).getJsonArray("products");
         String var2 = null;

         while(true) {
            if (this.api.getWebClient().isActive()) {
               var10000 = VariantHandler.findPrecartVariant(var1);
               if (!var10000.isDone()) {
                  var6 = var10000;
                  return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
               }

               Triplet var3 = (Triplet)var10000.join();
               if (var3 == null) {
                  var10000 = this.waitTilCartCookie();
                  if (!var10000.isDone()) {
                     var6 = var10000;
                     return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                  }

                  var10000.join();
                  var10000 = this.walletsGenCheckout();
                  if (!var10000.isDone()) {
                     var6 = var10000;
                     return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                  }

                  var2 = (String)var10000.join();
                  var10000 = this.handlePreload(var2);
                  if (!var10000.isDone()) {
                     var6 = var10000;
                     return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                  }

                  var2 = (String)var10000.join();
                  this.isPreload = false;
                  this.isContactpreload = false;
                  return CompletableFuture.completedFuture(var2);
               }

               this.shippingRate = new CompletableFuture();
               String var4 = (String)var3.first;
               this.precartItemName = var4;
               if (this.task.getSite() == Site.KITH) {
                  var10000 = this.atcBasic(var4);
                  if (!var10000.isDone()) {
                     var6 = var10000;
                     return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                  }

                  var10000.join();
               } else {
                  var10000 = this.atcAJAX(var4, false);
                  if (!var10000.isDone()) {
                     var6 = var10000;
                     return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                  }

                  var10000.join();
               }

               var10000 = this.walletsGenCheckout();
               if (!var10000.isDone()) {
                  var6 = var10000;
                  return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
               }

               var2 = (String)var10000.join();
               var10000 = this.handlePreload(var2);
               if (!var10000.isDone()) {
                  var6 = var10000;
                  return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
               }

               var2 = (String)var10000.join();
               if (!(Boolean)var3.second) {
                  this.isPreload = false;
               } else {
                  var10000 = this.getCheckoutURL(var2);
                  if (!var10000.isDone()) {
                     var6 = var10000;
                     return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                  }

                  var10000.join();
                  if (this.api.getCookies().contains("_shopify_checkpoint")) {
                     var10000 = this.checkCaptcha(var2);
                     if (!var10000.isDone()) {
                        var6 = var10000;
                        return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                     }

                     var10000.join();
                  }

                  var10000 = this.submitContact(var2);
                  if (!var10000.isDone()) {
                     var6 = var10000;
                     return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                  }

                  var10000.join();
                  CompletableFuture var5 = this.getShippingRateOld();
                  var10000 = CompletableFuture.allOf(var5, this.getShippingPage(var2));
                  if (!var10000.isDone()) {
                     var6 = var10000;
                     return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                  }

                  var10000.join();
                  var10000 = this.shippingRate;
                  if (!var10000.isDone()) {
                     var6 = var10000;
                     return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                  }

                  if (!((ShippingRateSupplier)var10000.join()).get().equals(this.task.getShippingRate()) && this.task.getShippingRate().length() > 3) {
                     Logger var10 = this.logger;
                     CompletableFuture var10002 = this.shippingRate;
                     if (!var10002.isDone()) {
                        CompletableFuture var8 = var10002;
                        String var7 = "The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]";
                        Logger var9 = var10;
                        return var8.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                     }

                     var10.error("The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]", ((ShippingRateSupplier)var10002.join()).get(), this.task.getShippingRate());
                     var10000 = this.clearWithChangeJS();
                     if (!var10000.isDone()) {
                        var6 = var10000;
                        return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                     }

                     var10000.join();
                     this.shippingRate = new CompletableFuture();
                     if (var1.isEmpty()) {
                        this.logger.info("There are no items with matching shipping rate. Handling...");
                        this.api.checkIsOOS();
                        this.configureShippingRate();
                        this.isPreload = false;
                        this.isContactpreload = true;
                        return CompletableFuture.completedFuture(var2);
                     }

                     var1.remove(var3.third);
                     continue;
                  }

                  var10000 = this.submitShipping(var2);
                  if (!var10000.isDone()) {
                     var6 = var10000;
                     return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                  }

                  var10000.join();
               }
            }

            var10000 = this.clearWithChangeJS();
            if (!var10000.isDone()) {
               var6 = var10000;
               return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
            }

            var10000.join();
            var10000 = this.checkCart();
            if (!var10000.isDone()) {
               var6 = var10000;
               return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
            }

            var10000.join();
            this.api.checkIsOOS();
            this.configureShippingRate();
            return CompletableFuture.completedFuture(var2);
         }
      }
   }

   public CompletableFuture run() {
      byte var1 = 1;
      String var2 = null;
      if (!this.isRestockMode) {
         try {
            super.vertx.setPeriodic(TimeUnit.SECONDS.toMillis(this.isPreload ? 8L : 15L), this::lambda$run$0);
         } catch (Exception var7) {
            var7.printStackTrace();
         }
      }

      CompletableFuture var6;
      CompletableFuture var10000;
      try {
         var10000 = initHarvesters();
         if (!var10000.isDone()) {
            var6 = var10000;
            return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$run);
         } else {
            var10000.join();
            this.paymentGateway = SiteParser.getGatewayFromSite(this.task.getSite(), this.isCod);
            this.fastPreload = this.task.getMode().contains("fast") && this.paymentGateway != null;
            var10000 = this.initShopDetails();
            if (!var10000.isDone()) {
               var6 = var10000;
               return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$run);
            } else {
               var10000.join();
               if (this.task.getMode().contains("login")) {
                  this.logger.info("Login required. Logging in...");
                  var10000 = this.login();
                  if (!var10000.isDone()) {
                     var6 = var10000;
                     return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$run);
                  }

                  var10000.join();
               }

               this.presolver = new Presolver(this);
               if (!this.isExtra) {
                  if (this.isPreload) {
                     var10000 = this.preload();
                     if (!var10000.isDone()) {
                        var6 = var10000;
                        return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$run);
                     }

                     var2 = (String)var10000.join();
                  } else {
                     var10000 = this.preloadContactOnly();
                     if (!var10000.isDone()) {
                        var6 = var10000;
                        return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$run);
                     }

                     var2 = (String)var10000.join();
                  }
               } else if (this.isPreload) {
                  var10000 = this.preloadWallets();
                  if (!var10000.isDone()) {
                     var6 = var10000;
                     return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$run);
                  }

                  var2 = (String)var10000.join();
               }

               if (!this.api.getCookies().contains("_shopify_checkpoint") && HARVESTERS.length > 0) {
                  this.cpMonitor = this.presolver.run();
               }

               if ((this.isPreload || this.isContactpreload) && this.isSmart) {
                  this.monitorQueue(var2);
               }

               String var3;
               JsonObject var10;
               if (this.isKeyword) {
                  var3 = null;

                  while(var3 == null) {
                     var10000 = this.fetchProductsJSON(true);
                     if (!var10000.isDone()) {
                        var6 = var10000;
                        return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$run);
                     }

                     JsonObject var4 = (JsonObject)var10000.join();
                     var3 = VariantHandler.selectVariantFromKeyword(var4, this.task.getSize(), this.positiveKeywords, this.negativeKeywords, this);
                     if (var3 == null) {
                        var10000 = VertxUtil.signalSleep(this.instanceSignal, (long)this.task.getMonitorDelay());
                        if (!var10000.isDone()) {
                           var6 = var10000;
                           return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$run);
                        }

                        Object var5 = var10000.join();
                        if (var5 != null) {
                           var3 = VariantHandler.selectVariantFromKeyword((JsonObject)var5, this.task.getSize(), this.positiveKeywords, this.negativeKeywords, this);
                        }
                     } else {
                        VertxUtil.sendSignal(this.instanceSignal, var4);
                     }
                  }

                  this.instanceSignal = var3;
               } else if (this.isEL) {
                  var10000 = this.fetchELJS();
                  if (!var10000.isDone()) {
                     var6 = var10000;
                     return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$run);
                  }

                  var10 = (JsonObject)var10000.join();
                  this.instanceSignal = VariantHandler.selectVariantFromLink(var10, this.task.getSize(), this);
               }

               this.isSmart = false;
               this.setProductPickupTime();
               this.genPaymentToken();

               while(this.api.getWebClient().isActive()) {
                  try {
                     if (var2 == null && (this.isExtra || var1 != 0)) {
                        var10000 = this.shippingAndBillingPost();
                        if (!var10000.isDone()) {
                           var6 = var10000;
                           return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$run);
                        }

                        var2 = (String)var10000.join();
                        Analytics.carts.increment();
                        var10000 = this.handlePreload(var2);
                        if (!var10000.isDone()) {
                           var6 = var10000;
                           return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$run);
                        }

                        var2 = (String)var10000.join();
                        var10000 = this.getCheckoutURL(var2);
                        if (!var10000.isDone()) {
                           var6 = var10000;
                           return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$run);
                        }

                        var10000.join();
                        this.shutOffPresolver(false);
                        var10000 = this.submitContact(var2);
                        if (!var10000.isDone()) {
                           var6 = var10000;
                           return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$run);
                        }

                        var10000.join();
                        var10000 = this.walletsSubmitShipping(var2);
                        if (!var10000.isDone()) {
                           var6 = var10000;
                           return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$run);
                        }

                        var10000.join();
                     } else {
                        var10000 = this.smoothCart();
                        if (!var10000.isDone()) {
                           var6 = var10000;
                           return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$run);
                        }

                        var10000.join();
                        Analytics.carts.increment();
                        if (var2 == null) {
                           this.genPaymentToken();
                           var10000 = this.genCheckoutURLViaCart();
                           if (!var10000.isDone()) {
                              var6 = var10000;
                              return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$run);
                           }

                           var2 = (String)var10000.join();
                           var10000 = this.handlePreload(var2);
                           if (!var10000.isDone()) {
                              var6 = var10000;
                              return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$run);
                           }

                           var2 = (String)var10000.join();
                           var10000 = this.getCheckoutURL(var2);
                           if (!var10000.isDone()) {
                              var6 = var10000;
                              return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$run);
                           }

                           var10000.join();
                           this.shutOffPresolver(false);
                           var10000 = this.submitContact(var2);
                           if (!var10000.isDone()) {
                              var6 = var10000;
                              return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$run);
                           }

                           var10000.join();
                           var10000 = this.walletsSubmitShipping(var2);
                           if (!var10000.isDone()) {
                              var6 = var10000;
                              return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$run);
                           }

                           var10000.join();
                        } else if (!this.isPreload && !this.isContactpreload) {
                           var10000 = this.getCheckoutURL(var2);
                           if (!var10000.isDone()) {
                              var6 = var10000;
                              return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$run);
                           }

                           var3 = (String)var10000.join();
                           this.shutOffPresolver(false);
                           if (this.precartItemName != null && !this.precartItemName.equals(this.instanceSignal) && var3.contains(this.precartItemName)) {
                              this.logger.error("Please notify the developer of cart bug. Thank you [{}]!", this.precartItemName);
                              var10000 = VertxUtil.hardCodedSleep(2147483647L);
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$run);
                              }

                              var10000.join();
                              var10000 = VertxUtil.hardCodedSleep(2147483647L);
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$run);
                              }

                              var10000.join();
                           }

                           var10000 = this.submitContact(var2);
                           if (!var10000.isDone()) {
                              var6 = var10000;
                              return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$run);
                           }

                           var10000.join();
                           var10000 = this.walletsSubmitShipping(var2);
                           if (!var10000.isDone()) {
                              var6 = var10000;
                              return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$run);
                           }

                           var10000.join();
                        }
                     }

                     if (this.isContactpreload) {
                        var10000 = this.genCheckoutURLViaCart();
                        if (!var10000.isDone()) {
                           var6 = var10000;
                           return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$run);
                        }

                        var10000.join();
                        this.shutOffPresolver(false);
                        var10000 = this.walletsSubmitShipping(var2);
                        if (!var10000.isDone()) {
                           var6 = var10000;
                           return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$run);
                        }

                        var3 = (String)var10000.join();
                        if (!this.precartItemName.equals(this.instanceSignal) && var3.contains(this.precartItemName)) {
                           this.logger.error("Please notify the developer of cart bug. Thank you [{}]!", this.precartItemName);
                           var10000 = VertxUtil.hardCodedSleep(2147483647L);
                           if (!var10000.isDone()) {
                              var6 = var10000;
                              return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$run);
                           }

                           var10000.join();
                           var10000 = VertxUtil.hardCodedSleep(2147483647L);
                           if (!var10000.isDone()) {
                              var6 = var10000;
                              return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$run);
                           }

                           var10000.join();
                        }
                     }

                     if (this.isPreload || this.paymentGateway == null || this.api.getCookies().contains("_shopify_checkpoint") || this.isEarly || !this.isPreload && !this.isContactpreload || this.isRestockMode) {
                        var10000 = this.getProcessingPage(var2, false);
                        if (!var10000.isDone()) {
                           var6 = var10000;
                           return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$run);
                        }

                        var3 = (String)var10000.join();
                        if (!this.precartItemName.equals(this.instanceSignal) && var3.contains(this.precartItemName)) {
                           this.logger.error("Please notify the developer of cart bug. Thank you [{}]!", this.precartItemName);
                           var10000 = VertxUtil.hardCodedSleep(2147483647L);
                           if (!var10000.isDone()) {
                              var6 = var10000;
                              return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$run);
                           }

                           var10000.join();
                           var10000 = VertxUtil.hardCodedSleep(2147483647L);
                           if (!var10000.isDone()) {
                              var6 = var10000;
                              return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$run);
                           }

                           var10000.join();
                        }
                     }

                     String var11;
                     do {
                        var10000 = this.processPayment(var2);
                        if (!var10000.isDone()) {
                           var6 = var10000;
                           return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$run);
                        }

                        var10000.join();
                        var10000 = this.checkOrderAPI(var2);
                        if (!var10000.isDone()) {
                           var6 = var10000;
                           return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$run);
                        }

                        var10 = (JsonObject)var10000.join();
                        var11 = parseDecline(var10);
                        if (var11 != null && !var11.equals("success")) {
                           Analytics.failure(var11, this.task, var10, this.api.proxyStringSafe());
                           this.logger.info("Checkout fail -> " + var11);
                        }
                     } while(var11 != null && !var11.equals("success"));

                     this.logger.info("Successfully checked out.");
                     Analytics.success(this.task, var10, this.api.proxyStringSafe());
                     break;
                  } catch (Throwable var8) {
                     this.logger.error(var8.getMessage());
                     this.setAttributes();
                     this.isPreload = false;
                     this.isContactpreload = false;
                     var1 = 0;
                     var2 = null;
                  }
               }

               return null;
            }
         }
      } catch (Throwable var9) {
         this.logger.error("Task interrupted: " + var9.getMessage());
         this.setAttributes();
         this.isPreload = false;
         this.isContactpreload = false;
         var10000 = VertxUtil.hardCodedSleep(1000L);
         if (!var10000.isDone()) {
            var6 = var10000;
            return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$run);
         } else {
            var10000.join();
            return this.run();
         }
      }
   }

   public void lambda$run$0(Long var1) {
      Request.execute(this.api.getWebClient().getAbs("https://www.google.com/favicon.ico").putHeader("user-agent", this.api.UA));
   }

   public CompletableFuture createQueue() {
      int var1 = 0;

      while(var1++ < Integer.MAX_VALUE) {
         CompletableFuture var3 = this.attemptGenCheckoutUrl(true);
         if (!var3.isDone()) {
            CompletableFuture var2 = var3;
            return var2.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$createQueue);
         }

         var3.join();
         this.api.getCookies().clear();
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$run(ShopifySafe param0, int param1, String param2, CompletableFuture param3, String param4, JsonObject param5, Throwable param6, int param7, Object param8) {
      // $FF: Couldn't be decompiled
   }

   public ShopifySafe(Task var1, int var2) {
      super(var1, var2);
      this.id = var2;
      this.isPreload = this.task.getMode().contains("preload");
      this.isSmart = this.task.getMode().contains("smart");
      this.isContactpreload = false;
   }

   public static CompletableFuture async$preload(ShopifySafe var0, CompletableFuture var1, JsonArray var2, String var3, int var4, Triplet var5, String var6, CompletableFuture var7, Logger var8, String var9, int var10, Object var11) {
      CompletableFuture var10000;
      String var26;
      label1023: {
         JsonArray var25;
         int var27;
         CompletableFuture var31;
         label1002: {
            Triplet var28;
            String var29;
            CompletableFuture var30;
            label991: {
               CompletableFuture var32;
               Logger var34;
               CompletableFuture var35;
               switch (var10) {
                  case 0:
                     var10000 = var0.fetchProductsJSON(false);
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var25 = ((JsonObject)var10000.join()).getJsonArray("products");
                     var26 = null;
                     var27 = 0;
                     if (!var0.api.getWebClient().isActive() || var27++ >= 5000000) {
                        break label1002;
                     }
                     break;
                  case 1:
                     var25 = ((JsonObject)var1.join()).getJsonArray("products");
                     var26 = null;
                     var27 = 0;
                     if (!var0.api.getWebClient().isActive() || var27++ >= 5000000) {
                        break label1002;
                     }
                     break;
                  case 2:
                     var27 = var4;
                     var25 = var2;
                     var28 = (Triplet)var1.join();
                     if (var28 == null) {
                        var0.isPreload = false;
                        var0.logger.error("There is no item to preload. Handling...");
                        return CompletableFuture.completedFuture((Object)null);
                     }

                     var0.shippingRate = new CompletableFuture();
                     var29 = (String)var28.first;
                     var0.precartItemName = var29;
                     if (var0.task.getSite() == Site.KITH) {
                        var10000 = var0.atcBasic(var29);
                        if (!var10000.isDone()) {
                           var31 = var10000;
                           return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                        }

                        var10000.join();
                     } else {
                        var10000 = var0.atcAJAX(var29, false);
                        if (!var10000.isDone()) {
                           var31 = var10000;
                           return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                        }

                        var10000.join();
                     }

                     var10000 = var0.genCheckoutURLViaCart();
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var26 = (String)var10000.join();
                     var10000 = var0.handlePreload(var26);
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var26 = (String)var10000.join();
                     if (!(Boolean)var28.second) {
                        var0.isPreload = false;
                        break label1002;
                     }

                     var10000 = var0.getCheckoutURL(var26);
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var10000.join();
                     if (var0.api.getCookies().contains("_shopify_checkpoint")) {
                        var10000 = var0.checkCaptcha(var26);
                        if (!var10000.isDone()) {
                           var31 = var10000;
                           return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                        }

                        var10000.join();
                     }

                     var10000 = var0.submitContact(var26);
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var10000.join();
                     var30 = var0.getShippingRateOld();
                     var10000 = CompletableFuture.allOf(var30, var0.getShippingPage(var26));
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var10000.join();
                     var10000 = var0.shippingRate;
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     if (((ShippingRateSupplier)var10000.join()).get().equals(var0.task.getShippingRate()) || var0.task.getShippingRate().length() <= 3) {
                        break label991;
                     }

                     var34 = var0.logger;
                     var35 = var0.shippingRate;
                     if (!var35.isDone()) {
                        var32 = var35;
                        var9 = "The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]";
                        var8 = var34;
                        return var32.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var34.error("The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]", ((ShippingRateSupplier)var35.join()).get(), var0.task.getShippingRate());
                     var10000 = var0.clearWithChangeJS();
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var10000.join();
                     var0.shippingRate = new CompletableFuture();
                     if (var2.isEmpty()) {
                        var0.logger.info("There are no items with matching shipping rate. Handling...");
                        var0.api.checkIsOOS();
                        var0.configureShippingRate();
                        var0.isPreload = false;
                        var0.isContactpreload = true;
                        return CompletableFuture.completedFuture(var26);
                     }

                     try {
                        var25.remove(var28.third);
                     } catch (Exception var24) {
                     }

                     if (!var0.api.getWebClient().isActive() || var27++ >= 5000000) {
                        break label1002;
                     }
                     break;
                  case 3:
                     var29 = var6;
                     var28 = var5;
                     var27 = var4;
                     var25 = var2;
                     var1.join();
                     var10000 = var0.genCheckoutURLViaCart();
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var26 = (String)var10000.join();
                     var10000 = var0.handlePreload(var26);
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var26 = (String)var10000.join();
                     if (!(Boolean)var5.second) {
                        var0.isPreload = false;
                        break label1002;
                     }

                     var10000 = var0.getCheckoutURL(var26);
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var10000.join();
                     if (var0.api.getCookies().contains("_shopify_checkpoint")) {
                        var10000 = var0.checkCaptcha(var26);
                        if (!var10000.isDone()) {
                           var31 = var10000;
                           return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                        }

                        var10000.join();
                     }

                     var10000 = var0.submitContact(var26);
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var10000.join();
                     var30 = var0.getShippingRateOld();
                     var10000 = CompletableFuture.allOf(var30, var0.getShippingPage(var26));
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var10000.join();
                     var10000 = var0.shippingRate;
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     if (((ShippingRateSupplier)var10000.join()).get().equals(var0.task.getShippingRate()) || var0.task.getShippingRate().length() <= 3) {
                        break label991;
                     }

                     var34 = var0.logger;
                     var35 = var0.shippingRate;
                     if (!var35.isDone()) {
                        var32 = var35;
                        var9 = "The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]";
                        var8 = var34;
                        return var32.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var34.error("The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]", ((ShippingRateSupplier)var35.join()).get(), var0.task.getShippingRate());
                     var10000 = var0.clearWithChangeJS();
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var10000.join();
                     var0.shippingRate = new CompletableFuture();
                     if (var2.isEmpty()) {
                        var0.logger.info("There are no items with matching shipping rate. Handling...");
                        var0.api.checkIsOOS();
                        var0.configureShippingRate();
                        var0.isPreload = false;
                        var0.isContactpreload = true;
                        return CompletableFuture.completedFuture(var26);
                     }

                     try {
                        var25.remove(var28.third);
                     } catch (Exception var23) {
                     }

                     if (!var0.api.getWebClient().isActive() || var27++ >= 5000000) {
                        break label1002;
                     }
                     break;
                  case 4:
                     var29 = var6;
                     var28 = var5;
                     var27 = var4;
                     var25 = var2;
                     var1.join();
                     var10000 = var0.genCheckoutURLViaCart();
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var26 = (String)var10000.join();
                     var10000 = var0.handlePreload(var26);
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var26 = (String)var10000.join();
                     if (!(Boolean)var5.second) {
                        var0.isPreload = false;
                        break label1002;
                     }

                     var10000 = var0.getCheckoutURL(var26);
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var10000.join();
                     if (var0.api.getCookies().contains("_shopify_checkpoint")) {
                        var10000 = var0.checkCaptcha(var26);
                        if (!var10000.isDone()) {
                           var31 = var10000;
                           return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                        }

                        var10000.join();
                     }

                     var10000 = var0.submitContact(var26);
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var10000.join();
                     var30 = var0.getShippingRateOld();
                     var10000 = CompletableFuture.allOf(var30, var0.getShippingPage(var26));
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var10000.join();
                     var10000 = var0.shippingRate;
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     if (((ShippingRateSupplier)var10000.join()).get().equals(var0.task.getShippingRate()) || var0.task.getShippingRate().length() <= 3) {
                        break label991;
                     }

                     var34 = var0.logger;
                     var35 = var0.shippingRate;
                     if (!var35.isDone()) {
                        var32 = var35;
                        var9 = "The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]";
                        var8 = var34;
                        return var32.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var34.error("The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]", ((ShippingRateSupplier)var35.join()).get(), var0.task.getShippingRate());
                     var10000 = var0.clearWithChangeJS();
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var10000.join();
                     var0.shippingRate = new CompletableFuture();
                     if (var2.isEmpty()) {
                        var0.logger.info("There are no items with matching shipping rate. Handling...");
                        var0.api.checkIsOOS();
                        var0.configureShippingRate();
                        var0.isPreload = false;
                        var0.isContactpreload = true;
                        return CompletableFuture.completedFuture(var26);
                     }

                     try {
                        var25.remove(var28.third);
                     } catch (Exception var22) {
                     }

                     if (!var0.api.getWebClient().isActive() || var27++ >= 5000000) {
                        break label1002;
                     }
                     break;
                  case 5:
                     var29 = var6;
                     var28 = var5;
                     var27 = var4;
                     var25 = var2;
                     var26 = (String)var1.join();
                     var10000 = var0.handlePreload(var26);
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var26 = (String)var10000.join();
                     if (!(Boolean)var5.second) {
                        var0.isPreload = false;
                        break label1002;
                     }

                     var10000 = var0.getCheckoutURL(var26);
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var10000.join();
                     if (var0.api.getCookies().contains("_shopify_checkpoint")) {
                        var10000 = var0.checkCaptcha(var26);
                        if (!var10000.isDone()) {
                           var31 = var10000;
                           return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                        }

                        var10000.join();
                     }

                     var10000 = var0.submitContact(var26);
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var10000.join();
                     var30 = var0.getShippingRateOld();
                     var10000 = CompletableFuture.allOf(var30, var0.getShippingPage(var26));
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var10000.join();
                     var10000 = var0.shippingRate;
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     if (((ShippingRateSupplier)var10000.join()).get().equals(var0.task.getShippingRate()) || var0.task.getShippingRate().length() <= 3) {
                        break label991;
                     }

                     var34 = var0.logger;
                     var35 = var0.shippingRate;
                     if (!var35.isDone()) {
                        var32 = var35;
                        var9 = "The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]";
                        var8 = var34;
                        return var32.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var34.error("The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]", ((ShippingRateSupplier)var35.join()).get(), var0.task.getShippingRate());
                     var10000 = var0.clearWithChangeJS();
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var10000.join();
                     var0.shippingRate = new CompletableFuture();
                     if (var2.isEmpty()) {
                        var0.logger.info("There are no items with matching shipping rate. Handling...");
                        var0.api.checkIsOOS();
                        var0.configureShippingRate();
                        var0.isPreload = false;
                        var0.isContactpreload = true;
                        return CompletableFuture.completedFuture(var26);
                     }

                     try {
                        var25.remove(var28.third);
                     } catch (Exception var21) {
                     }

                     if (!var0.api.getWebClient().isActive() || var27++ >= 5000000) {
                        break label1002;
                     }
                     break;
                  case 6:
                     var29 = var6;
                     var28 = var5;
                     var27 = var4;
                     var25 = var2;
                     var26 = (String)var1.join();
                     if (!(Boolean)var5.second) {
                        var0.isPreload = false;
                        break label1002;
                     }

                     var10000 = var0.getCheckoutURL(var26);
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var10000.join();
                     if (var0.api.getCookies().contains("_shopify_checkpoint")) {
                        var10000 = var0.checkCaptcha(var26);
                        if (!var10000.isDone()) {
                           var31 = var10000;
                           return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                        }

                        var10000.join();
                     }

                     var10000 = var0.submitContact(var26);
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var10000.join();
                     var30 = var0.getShippingRateOld();
                     var10000 = CompletableFuture.allOf(var30, var0.getShippingPage(var26));
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var10000.join();
                     var10000 = var0.shippingRate;
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     if (((ShippingRateSupplier)var10000.join()).get().equals(var0.task.getShippingRate()) || var0.task.getShippingRate().length() <= 3) {
                        break label991;
                     }

                     var34 = var0.logger;
                     var35 = var0.shippingRate;
                     if (!var35.isDone()) {
                        var32 = var35;
                        var9 = "The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]";
                        var8 = var34;
                        return var32.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var34.error("The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]", ((ShippingRateSupplier)var35.join()).get(), var0.task.getShippingRate());
                     var10000 = var0.clearWithChangeJS();
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var10000.join();
                     var0.shippingRate = new CompletableFuture();
                     if (var2.isEmpty()) {
                        var0.logger.info("There are no items with matching shipping rate. Handling...");
                        var0.api.checkIsOOS();
                        var0.configureShippingRate();
                        var0.isPreload = false;
                        var0.isContactpreload = true;
                        return CompletableFuture.completedFuture(var26);
                     }

                     try {
                        var25.remove(var28.third);
                     } catch (Exception var20) {
                     }

                     if (!var0.api.getWebClient().isActive() || var27++ >= 5000000) {
                        break label1002;
                     }
                     break;
                  case 7:
                     var29 = var6;
                     var28 = var5;
                     var27 = var4;
                     var26 = var3;
                     var25 = var2;
                     var1.join();
                     if (var0.api.getCookies().contains("_shopify_checkpoint")) {
                        var10000 = var0.checkCaptcha(var3);
                        if (!var10000.isDone()) {
                           var31 = var10000;
                           return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                        }

                        var10000.join();
                     }

                     var10000 = var0.submitContact(var3);
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var10000.join();
                     var30 = var0.getShippingRateOld();
                     var10000 = CompletableFuture.allOf(var30, var0.getShippingPage(var3));
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var10000.join();
                     var10000 = var0.shippingRate;
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     if (((ShippingRateSupplier)var10000.join()).get().equals(var0.task.getShippingRate()) || var0.task.getShippingRate().length() <= 3) {
                        break label991;
                     }

                     var34 = var0.logger;
                     var35 = var0.shippingRate;
                     if (!var35.isDone()) {
                        var32 = var35;
                        var9 = "The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]";
                        var8 = var34;
                        return var32.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var34.error("The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]", ((ShippingRateSupplier)var35.join()).get(), var0.task.getShippingRate());
                     var10000 = var0.clearWithChangeJS();
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var10000.join();
                     var0.shippingRate = new CompletableFuture();
                     if (var2.isEmpty()) {
                        var0.logger.info("There are no items with matching shipping rate. Handling...");
                        var0.api.checkIsOOS();
                        var0.configureShippingRate();
                        var0.isPreload = false;
                        var0.isContactpreload = true;
                        return CompletableFuture.completedFuture(var3);
                     }

                     try {
                        var25.remove(var28.third);
                     } catch (Exception var19) {
                     }

                     if (!var0.api.getWebClient().isActive() || var27++ >= 5000000) {
                        break label1002;
                     }
                     break;
                  case 8:
                     var29 = var6;
                     var28 = var5;
                     var27 = var4;
                     var26 = var3;
                     var25 = var2;
                     var1.join();
                     var10000 = var0.submitContact(var3);
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var10000.join();
                     var30 = var0.getShippingRateOld();
                     var10000 = CompletableFuture.allOf(var30, var0.getShippingPage(var3));
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var10000.join();
                     var10000 = var0.shippingRate;
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     if (((ShippingRateSupplier)var10000.join()).get().equals(var0.task.getShippingRate()) || var0.task.getShippingRate().length() <= 3) {
                        break label991;
                     }

                     var34 = var0.logger;
                     var35 = var0.shippingRate;
                     if (!var35.isDone()) {
                        var32 = var35;
                        var9 = "The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]";
                        var8 = var34;
                        return var32.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var34.error("The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]", ((ShippingRateSupplier)var35.join()).get(), var0.task.getShippingRate());
                     var10000 = var0.clearWithChangeJS();
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var10000.join();
                     var0.shippingRate = new CompletableFuture();
                     if (var2.isEmpty()) {
                        var0.logger.info("There are no items with matching shipping rate. Handling...");
                        var0.api.checkIsOOS();
                        var0.configureShippingRate();
                        var0.isPreload = false;
                        var0.isContactpreload = true;
                        return CompletableFuture.completedFuture(var3);
                     }

                     try {
                        var25.remove(var28.third);
                     } catch (Exception var18) {
                     }

                     if (!var0.api.getWebClient().isActive() || var27++ >= 5000000) {
                        break label1002;
                     }
                     break;
                  case 9:
                     var29 = var6;
                     var28 = var5;
                     var27 = var4;
                     var26 = var3;
                     var25 = var2;
                     var1.join();
                     var30 = var0.getShippingRateOld();
                     var10000 = CompletableFuture.allOf(var30, var0.getShippingPage(var3));
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var10000.join();
                     var10000 = var0.shippingRate;
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     if (((ShippingRateSupplier)var10000.join()).get().equals(var0.task.getShippingRate()) || var0.task.getShippingRate().length() <= 3) {
                        break label991;
                     }

                     var34 = var0.logger;
                     var35 = var0.shippingRate;
                     if (!var35.isDone()) {
                        var32 = var35;
                        var9 = "The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]";
                        var8 = var34;
                        return var32.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var34.error("The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]", ((ShippingRateSupplier)var35.join()).get(), var0.task.getShippingRate());
                     var10000 = var0.clearWithChangeJS();
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var10000.join();
                     var0.shippingRate = new CompletableFuture();
                     if (var2.isEmpty()) {
                        var0.logger.info("There are no items with matching shipping rate. Handling...");
                        var0.api.checkIsOOS();
                        var0.configureShippingRate();
                        var0.isPreload = false;
                        var0.isContactpreload = true;
                        return CompletableFuture.completedFuture(var3);
                     }

                     try {
                        var25.remove(var28.third);
                     } catch (Exception var17) {
                     }

                     if (!var0.api.getWebClient().isActive() || var27++ >= 5000000) {
                        break label1002;
                     }
                     break;
                  case 10:
                     var30 = var1;
                     var29 = var6;
                     var28 = var5;
                     var27 = var4;
                     var26 = var3;
                     var25 = var2;
                     var7.join();
                     var10000 = var0.shippingRate;
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     if (((ShippingRateSupplier)var10000.join()).get().equals(var0.task.getShippingRate()) || var0.task.getShippingRate().length() <= 3) {
                        break label991;
                     }

                     var34 = var0.logger;
                     var35 = var0.shippingRate;
                     if (!var35.isDone()) {
                        var32 = var35;
                        var9 = "The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]";
                        var8 = var34;
                        return var32.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var34.error("The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]", ((ShippingRateSupplier)var35.join()).get(), var0.task.getShippingRate());
                     var10000 = var0.clearWithChangeJS();
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var10000.join();
                     var0.shippingRate = new CompletableFuture();
                     if (var2.isEmpty()) {
                        var0.logger.info("There are no items with matching shipping rate. Handling...");
                        var0.api.checkIsOOS();
                        var0.configureShippingRate();
                        var0.isPreload = false;
                        var0.isContactpreload = true;
                        return CompletableFuture.completedFuture(var3);
                     }

                     try {
                        var25.remove(var28.third);
                     } catch (Exception var16) {
                     }

                     if (!var0.api.getWebClient().isActive() || var27++ >= 5000000) {
                        break label1002;
                     }
                     break;
                  case 11:
                     var30 = var1;
                     var29 = var6;
                     var28 = var5;
                     var27 = var4;
                     var26 = var3;
                     var25 = var2;
                     if (((ShippingRateSupplier)var7.join()).get().equals(var0.task.getShippingRate()) || var0.task.getShippingRate().length() <= 3) {
                        break label991;
                     }

                     var34 = var0.logger;
                     var35 = var0.shippingRate;
                     if (!var35.isDone()) {
                        var32 = var35;
                        var9 = "The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]";
                        var8 = var34;
                        return var32.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var34.error("The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]", ((ShippingRateSupplier)var35.join()).get(), var0.task.getShippingRate());
                     var10000 = var0.clearWithChangeJS();
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var10000.join();
                     var0.shippingRate = new CompletableFuture();
                     if (var2.isEmpty()) {
                        var0.logger.info("There are no items with matching shipping rate. Handling...");
                        var0.api.checkIsOOS();
                        var0.configureShippingRate();
                        var0.isPreload = false;
                        var0.isContactpreload = true;
                        return CompletableFuture.completedFuture(var3);
                     }

                     try {
                        var25.remove(var28.third);
                     } catch (Exception var15) {
                     }

                     if (!var0.api.getWebClient().isActive() || var27++ >= 5000000) {
                        break label1002;
                     }
                     break;
                  case 12:
                     var28 = var5;
                     var27 = var4;
                     var26 = var3;
                     var25 = var2;
                     var8.error(var9, ((ShippingRateSupplier)var7.join()).get(), var0.task.getShippingRate());
                     var10000 = var0.clearWithChangeJS();
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var10000.join();
                     var0.shippingRate = new CompletableFuture();
                     if (var2.isEmpty()) {
                        var0.logger.info("There are no items with matching shipping rate. Handling...");
                        var0.api.checkIsOOS();
                        var0.configureShippingRate();
                        var0.isPreload = false;
                        var0.isContactpreload = true;
                        return CompletableFuture.completedFuture(var3);
                     }

                     try {
                        var25.remove(var28.third);
                     } catch (Exception var14) {
                     }

                     if (!var0.api.getWebClient().isActive() || var27++ >= 5000000) {
                        break label1002;
                     }
                     break;
                  case 13:
                     var28 = var5;
                     var27 = var4;
                     var26 = var3;
                     var25 = var2;
                     var7.join();
                     var0.shippingRate = new CompletableFuture();
                     if (var2.isEmpty()) {
                        var0.logger.info("There are no items with matching shipping rate. Handling...");
                        var0.api.checkIsOOS();
                        var0.configureShippingRate();
                        var0.isPreload = false;
                        var0.isContactpreload = true;
                        return CompletableFuture.completedFuture(var3);
                     }

                     try {
                        var25.remove(var28.third);
                     } catch (Exception var13) {
                     }

                     if (!var0.api.getWebClient().isActive() || var27++ >= 5000000) {
                        break label1002;
                     }
                     break;
                  case 14:
                     var27 = var4;
                     var26 = var3;
                     var25 = var2;
                     var7.join();
                     break label1002;
                  case 15:
                     var26 = var3;
                     var1.join();
                     var10000 = var0.checkCart();
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }
                     break label1023;
                  case 16:
                     var10000 = var1;
                     var26 = var3;
                     break label1023;
                  default:
                     throw new IllegalArgumentException();
               }

               while(true) {
                  Triplet var39;
                  if (var27 == 1) {
                     if (var0.findPreset() != null) {
                        var39 = var0.findPreset();
                     } else {
                        var10000 = VariantHandler.findPrecartVariant(var25);
                        if (!var10000.isDone()) {
                           var31 = var10000;
                           return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                        }

                        var39 = (Triplet)var10000.join();
                     }
                  } else {
                     var10000 = VariantHandler.findPrecartVariant(var25);
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var39 = (Triplet)var10000.join();
                  }

                  var28 = var39;
                  if (var28 == null) {
                     var0.isPreload = false;
                     var0.logger.error("There is no item to preload. Handling...");
                     return CompletableFuture.completedFuture((Object)null);
                  }

                  var0.shippingRate = new CompletableFuture();
                  var29 = (String)var28.first;
                  var0.precartItemName = var29;
                  if (var0.task.getSite() == Site.KITH) {
                     var10000 = var0.atcBasic(var29);
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var10000.join();
                  } else {
                     var10000 = var0.atcAJAX(var29, false);
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var10000.join();
                  }

                  var10000 = var0.genCheckoutURLViaCart();
                  if (!var10000.isDone()) {
                     var31 = var10000;
                     return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                  }

                  var26 = (String)var10000.join();
                  var10000 = var0.handlePreload(var26);
                  if (!var10000.isDone()) {
                     var31 = var10000;
                     return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                  }

                  var26 = (String)var10000.join();
                  if (!(Boolean)var28.second) {
                     var0.isPreload = false;
                     break label1002;
                  }

                  var10000 = var0.getCheckoutURL(var26);
                  if (!var10000.isDone()) {
                     var31 = var10000;
                     return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                  }

                  var10000.join();
                  if (var0.api.getCookies().contains("_shopify_checkpoint")) {
                     var10000 = var0.checkCaptcha(var26);
                     if (!var10000.isDone()) {
                        var31 = var10000;
                        return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                     }

                     var10000.join();
                  }

                  var10000 = var0.submitContact(var26);
                  if (!var10000.isDone()) {
                     var31 = var10000;
                     return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                  }

                  var10000.join();
                  var30 = var0.getShippingRateOld();
                  var10000 = CompletableFuture.allOf(var30, var0.getShippingPage(var26));
                  if (!var10000.isDone()) {
                     var31 = var10000;
                     return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                  }

                  var10000.join();
                  var10000 = var0.shippingRate;
                  if (!var10000.isDone()) {
                     var31 = var10000;
                     return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                  }

                  if (((ShippingRateSupplier)var10000.join()).get().equals(var0.task.getShippingRate()) || var0.task.getShippingRate().length() <= 3) {
                     break;
                  }

                  var34 = var0.logger;
                  var35 = var0.shippingRate;
                  if (!var35.isDone()) {
                     var32 = var35;
                     var9 = "The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]";
                     var8 = var34;
                     return var32.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                  }

                  var34.error("The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]", ((ShippingRateSupplier)var35.join()).get(), var0.task.getShippingRate());
                  var10000 = var0.clearWithChangeJS();
                  if (!var10000.isDone()) {
                     var31 = var10000;
                     return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
                  }

                  var10000.join();
                  var0.shippingRate = new CompletableFuture();
                  if (var25.isEmpty()) {
                     var0.logger.info("There are no items with matching shipping rate. Handling...");
                     var0.api.checkIsOOS();
                     var0.configureShippingRate();
                     var0.isPreload = false;
                     var0.isContactpreload = true;
                     return CompletableFuture.completedFuture(var26);
                  }

                  try {
                     var25.remove(var28.third);
                  } catch (Exception var12) {
                  }

                  if (!var0.api.getWebClient().isActive() || var27++ >= 5000000) {
                     break label1002;
                  }
               }
            }

            var10000 = var0.submitShipping(var26);
            if (!var10000.isDone()) {
               var31 = var10000;
               return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
            }

            var10000.join();
         }

         var10000 = var0.clearWithChangeJS();
         if (!var10000.isDone()) {
            var31 = var10000;
            return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
         }

         var10000.join();
         var10000 = var0.checkCart();
         if (!var10000.isDone()) {
            var31 = var10000;
            return var31.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preload);
         }
      }

      var10000.join();
      var0.api.checkIsOOS();
      var0.configureShippingRate();
      return CompletableFuture.completedFuture(var26);
   }

   public static CompletableFuture async$preloadWallets(ShopifySafe var0, CompletableFuture var1, JsonArray var2, String var3, Triplet var4, String var5, CompletableFuture var6, Logger var7, String var8, int var9, Object var10) {
      String var12;
      CompletableFuture var10000;
      label905: {
         label877: {
            JsonArray var11;
            label906: {
               Triplet var13;
               label907: {
                  label908: {
                     String var14;
                     CompletableFuture var15;
                     label895: {
                        String var16;
                        JsonArray var10001;
                        CompletableFuture var17;
                        String var10002;
                        Logger var18;
                        Logger var19;
                        CompletableFuture var20;
                        String var10004;
                        Triplet var21;
                        switch (var9) {
                           case 0:
                              var10000 = var0.fetchProductsJSON(false);
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var11 = ((JsonObject)var10000.join()).getJsonArray("products");
                              var12 = null;
                              if (!var0.api.getWebClient().isActive()) {
                                 break label906;
                              }

                              var10000 = VariantHandler.findPrecartVariant(var11);
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }
                              break;
                           case 1:
                              var11 = ((JsonObject)var1.join()).getJsonArray("products");
                              var12 = null;
                              if (!var0.api.getWebClient().isActive()) {
                                 break label906;
                              }

                              var10000 = VariantHandler.findPrecartVariant(var11);
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }
                              break;
                           case 2:
                              var10000 = var1;
                              var10001 = var2;
                              var12 = var3;
                              var11 = var10001;
                              break;
                           case 3:
                              var10000 = var1;
                              var10001 = var2;
                              var10002 = var3;
                              var13 = var4;
                              var12 = var10002;
                              var11 = var10001;
                              break label908;
                           case 4:
                              var10000 = var1;
                              var13 = var4;
                              var11 = var2;
                              break label907;
                           case 5:
                              var10000 = var1;
                              break label905;
                           case 6:
                              var10000 = var1;
                              var10001 = var2;
                              var10002 = var3;
                              var21 = var4;
                              var14 = var5;
                              var13 = var21;
                              var12 = var10002;
                              var11 = var10001;
                              var10000.join();
                              var10000 = var0.walletsGenCheckout();
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var12 = (String)var10000.join();
                              var10000 = var0.handlePreload(var12);
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var12 = (String)var10000.join();
                              if (!(Boolean)var13.second) {
                                 var0.isPreload = false;
                                 break label906;
                              }

                              var10000 = var0.getCheckoutURL(var12);
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var10000.join();
                              if (var0.api.getCookies().contains("_shopify_checkpoint")) {
                                 var10000 = var0.checkCaptcha(var12);
                                 if (!var10000.isDone()) {
                                    var6 = var10000;
                                    return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                                 }

                                 var10000.join();
                              }

                              var10000 = var0.submitContact(var12);
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var10000.join();
                              var15 = var0.getShippingRateOld();
                              var10000 = CompletableFuture.allOf(var15, var0.getShippingPage(var12));
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var10000.join();
                              var10000 = var0.shippingRate;
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              if (((ShippingRateSupplier)var10000.join()).get().equals(var0.task.getShippingRate()) || var0.task.getShippingRate().length() <= 3) {
                                 break label895;
                              }

                              var19 = var0.logger;
                              var20 = var0.shippingRate;
                              if (!var20.isDone()) {
                                 var17 = var20;
                                 var16 = "The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]";
                                 var18 = var19;
                                 return var17.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var19.error("The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]", ((ShippingRateSupplier)var20.join()).get(), var0.task.getShippingRate());
                              var10000 = var0.clearWithChangeJS();
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var10000.join();
                              var0.shippingRate = new CompletableFuture();
                              if (var11.isEmpty()) {
                                 var0.logger.info("There are no items with matching shipping rate. Handling...");
                                 var0.api.checkIsOOS();
                                 var0.configureShippingRate();
                                 var0.isPreload = false;
                                 var0.isContactpreload = true;
                                 return CompletableFuture.completedFuture(var12);
                              }

                              var11.remove(var13.third);
                              if (!var0.api.getWebClient().isActive()) {
                                 break label906;
                              }

                              var10000 = VariantHandler.findPrecartVariant(var11);
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }
                              break;
                           case 7:
                              var10000 = var1;
                              var10001 = var2;
                              var10002 = var3;
                              var21 = var4;
                              var14 = var5;
                              var13 = var21;
                              var12 = var10002;
                              var11 = var10001;
                              var10000.join();
                              var10000 = var0.walletsGenCheckout();
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var12 = (String)var10000.join();
                              var10000 = var0.handlePreload(var12);
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var12 = (String)var10000.join();
                              if (!(Boolean)var13.second) {
                                 var0.isPreload = false;
                                 break label906;
                              }

                              var10000 = var0.getCheckoutURL(var12);
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var10000.join();
                              if (var0.api.getCookies().contains("_shopify_checkpoint")) {
                                 var10000 = var0.checkCaptcha(var12);
                                 if (!var10000.isDone()) {
                                    var6 = var10000;
                                    return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                                 }

                                 var10000.join();
                              }

                              var10000 = var0.submitContact(var12);
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var10000.join();
                              var15 = var0.getShippingRateOld();
                              var10000 = CompletableFuture.allOf(var15, var0.getShippingPage(var12));
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var10000.join();
                              var10000 = var0.shippingRate;
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              if (((ShippingRateSupplier)var10000.join()).get().equals(var0.task.getShippingRate()) || var0.task.getShippingRate().length() <= 3) {
                                 break label895;
                              }

                              var19 = var0.logger;
                              var20 = var0.shippingRate;
                              if (!var20.isDone()) {
                                 var17 = var20;
                                 var16 = "The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]";
                                 var18 = var19;
                                 return var17.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var19.error("The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]", ((ShippingRateSupplier)var20.join()).get(), var0.task.getShippingRate());
                              var10000 = var0.clearWithChangeJS();
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var10000.join();
                              var0.shippingRate = new CompletableFuture();
                              if (var11.isEmpty()) {
                                 var0.logger.info("There are no items with matching shipping rate. Handling...");
                                 var0.api.checkIsOOS();
                                 var0.configureShippingRate();
                                 var0.isPreload = false;
                                 var0.isContactpreload = true;
                                 return CompletableFuture.completedFuture(var12);
                              }

                              var11.remove(var13.third);
                              if (!var0.api.getWebClient().isActive()) {
                                 break label906;
                              }

                              var10000 = VariantHandler.findPrecartVariant(var11);
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }
                              break;
                           case 8:
                              var10000 = var1;
                              var21 = var4;
                              var14 = var5;
                              var13 = var21;
                              var11 = var2;
                              var12 = (String)var10000.join();
                              var10000 = var0.handlePreload(var12);
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var12 = (String)var10000.join();
                              if (!(Boolean)var13.second) {
                                 var0.isPreload = false;
                                 break label906;
                              }

                              var10000 = var0.getCheckoutURL(var12);
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var10000.join();
                              if (var0.api.getCookies().contains("_shopify_checkpoint")) {
                                 var10000 = var0.checkCaptcha(var12);
                                 if (!var10000.isDone()) {
                                    var6 = var10000;
                                    return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                                 }

                                 var10000.join();
                              }

                              var10000 = var0.submitContact(var12);
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var10000.join();
                              var15 = var0.getShippingRateOld();
                              var10000 = CompletableFuture.allOf(var15, var0.getShippingPage(var12));
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var10000.join();
                              var10000 = var0.shippingRate;
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              if (((ShippingRateSupplier)var10000.join()).get().equals(var0.task.getShippingRate()) || var0.task.getShippingRate().length() <= 3) {
                                 break label895;
                              }

                              var19 = var0.logger;
                              var20 = var0.shippingRate;
                              if (!var20.isDone()) {
                                 var17 = var20;
                                 var16 = "The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]";
                                 var18 = var19;
                                 return var17.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var19.error("The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]", ((ShippingRateSupplier)var20.join()).get(), var0.task.getShippingRate());
                              var10000 = var0.clearWithChangeJS();
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var10000.join();
                              var0.shippingRate = new CompletableFuture();
                              if (var11.isEmpty()) {
                                 var0.logger.info("There are no items with matching shipping rate. Handling...");
                                 var0.api.checkIsOOS();
                                 var0.configureShippingRate();
                                 var0.isPreload = false;
                                 var0.isContactpreload = true;
                                 return CompletableFuture.completedFuture(var12);
                              }

                              var11.remove(var13.third);
                              if (!var0.api.getWebClient().isActive()) {
                                 break label906;
                              }

                              var10000 = VariantHandler.findPrecartVariant(var11);
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }
                              break;
                           case 9:
                              var10000 = var1;
                              var21 = var4;
                              var14 = var5;
                              var13 = var21;
                              var11 = var2;
                              var12 = (String)var10000.join();
                              if (!(Boolean)var13.second) {
                                 var0.isPreload = false;
                                 break label906;
                              }

                              var10000 = var0.getCheckoutURL(var12);
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var10000.join();
                              if (var0.api.getCookies().contains("_shopify_checkpoint")) {
                                 var10000 = var0.checkCaptcha(var12);
                                 if (!var10000.isDone()) {
                                    var6 = var10000;
                                    return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                                 }

                                 var10000.join();
                              }

                              var10000 = var0.submitContact(var12);
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var10000.join();
                              var15 = var0.getShippingRateOld();
                              var10000 = CompletableFuture.allOf(var15, var0.getShippingPage(var12));
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var10000.join();
                              var10000 = var0.shippingRate;
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              if (((ShippingRateSupplier)var10000.join()).get().equals(var0.task.getShippingRate()) || var0.task.getShippingRate().length() <= 3) {
                                 break label895;
                              }

                              var19 = var0.logger;
                              var20 = var0.shippingRate;
                              if (!var20.isDone()) {
                                 var17 = var20;
                                 var16 = "The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]";
                                 var18 = var19;
                                 return var17.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var19.error("The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]", ((ShippingRateSupplier)var20.join()).get(), var0.task.getShippingRate());
                              var10000 = var0.clearWithChangeJS();
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var10000.join();
                              var0.shippingRate = new CompletableFuture();
                              if (var11.isEmpty()) {
                                 var0.logger.info("There are no items with matching shipping rate. Handling...");
                                 var0.api.checkIsOOS();
                                 var0.configureShippingRate();
                                 var0.isPreload = false;
                                 var0.isContactpreload = true;
                                 return CompletableFuture.completedFuture(var12);
                              }

                              var11.remove(var13.third);
                              if (!var0.api.getWebClient().isActive()) {
                                 break label906;
                              }

                              var10000 = VariantHandler.findPrecartVariant(var11);
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }
                              break;
                           case 10:
                              var10000 = var1;
                              var10001 = var2;
                              var10002 = var3;
                              var21 = var4;
                              var14 = var5;
                              var13 = var21;
                              var12 = var10002;
                              var11 = var10001;
                              var10000.join();
                              if (var0.api.getCookies().contains("_shopify_checkpoint")) {
                                 var10000 = var0.checkCaptcha(var12);
                                 if (!var10000.isDone()) {
                                    var6 = var10000;
                                    return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                                 }

                                 var10000.join();
                              }

                              var10000 = var0.submitContact(var12);
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var10000.join();
                              var15 = var0.getShippingRateOld();
                              var10000 = CompletableFuture.allOf(var15, var0.getShippingPage(var12));
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var10000.join();
                              var10000 = var0.shippingRate;
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              if (((ShippingRateSupplier)var10000.join()).get().equals(var0.task.getShippingRate()) || var0.task.getShippingRate().length() <= 3) {
                                 break label895;
                              }

                              var19 = var0.logger;
                              var20 = var0.shippingRate;
                              if (!var20.isDone()) {
                                 var17 = var20;
                                 var16 = "The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]";
                                 var18 = var19;
                                 return var17.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var19.error("The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]", ((ShippingRateSupplier)var20.join()).get(), var0.task.getShippingRate());
                              var10000 = var0.clearWithChangeJS();
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var10000.join();
                              var0.shippingRate = new CompletableFuture();
                              if (var11.isEmpty()) {
                                 var0.logger.info("There are no items with matching shipping rate. Handling...");
                                 var0.api.checkIsOOS();
                                 var0.configureShippingRate();
                                 var0.isPreload = false;
                                 var0.isContactpreload = true;
                                 return CompletableFuture.completedFuture(var12);
                              }

                              var11.remove(var13.third);
                              if (!var0.api.getWebClient().isActive()) {
                                 break label906;
                              }

                              var10000 = VariantHandler.findPrecartVariant(var11);
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }
                              break;
                           case 11:
                              var10000 = var1;
                              var10001 = var2;
                              var10002 = var3;
                              var21 = var4;
                              var14 = var5;
                              var13 = var21;
                              var12 = var10002;
                              var11 = var10001;
                              var10000.join();
                              var10000 = var0.submitContact(var12);
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var10000.join();
                              var15 = var0.getShippingRateOld();
                              var10000 = CompletableFuture.allOf(var15, var0.getShippingPage(var12));
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var10000.join();
                              var10000 = var0.shippingRate;
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              if (((ShippingRateSupplier)var10000.join()).get().equals(var0.task.getShippingRate()) || var0.task.getShippingRate().length() <= 3) {
                                 break label895;
                              }

                              var19 = var0.logger;
                              var20 = var0.shippingRate;
                              if (!var20.isDone()) {
                                 var17 = var20;
                                 var16 = "The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]";
                                 var18 = var19;
                                 return var17.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var19.error("The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]", ((ShippingRateSupplier)var20.join()).get(), var0.task.getShippingRate());
                              var10000 = var0.clearWithChangeJS();
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var10000.join();
                              var0.shippingRate = new CompletableFuture();
                              if (var11.isEmpty()) {
                                 var0.logger.info("There are no items with matching shipping rate. Handling...");
                                 var0.api.checkIsOOS();
                                 var0.configureShippingRate();
                                 var0.isPreload = false;
                                 var0.isContactpreload = true;
                                 return CompletableFuture.completedFuture(var12);
                              }

                              var11.remove(var13.third);
                              if (!var0.api.getWebClient().isActive()) {
                                 break label906;
                              }

                              var10000 = VariantHandler.findPrecartVariant(var11);
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }
                              break;
                           case 12:
                              var10000 = var1;
                              var10001 = var2;
                              var10002 = var3;
                              var21 = var4;
                              var14 = var5;
                              var13 = var21;
                              var12 = var10002;
                              var11 = var10001;
                              var10000.join();
                              var15 = var0.getShippingRateOld();
                              var10000 = CompletableFuture.allOf(var15, var0.getShippingPage(var12));
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var10000.join();
                              var10000 = var0.shippingRate;
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              if (((ShippingRateSupplier)var10000.join()).get().equals(var0.task.getShippingRate()) || var0.task.getShippingRate().length() <= 3) {
                                 break label895;
                              }

                              var19 = var0.logger;
                              var20 = var0.shippingRate;
                              if (!var20.isDone()) {
                                 var17 = var20;
                                 var16 = "The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]";
                                 var18 = var19;
                                 return var17.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var19.error("The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]", ((ShippingRateSupplier)var20.join()).get(), var0.task.getShippingRate());
                              var10000 = var0.clearWithChangeJS();
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var10000.join();
                              var0.shippingRate = new CompletableFuture();
                              if (var11.isEmpty()) {
                                 var0.logger.info("There are no items with matching shipping rate. Handling...");
                                 var0.api.checkIsOOS();
                                 var0.configureShippingRate();
                                 var0.isPreload = false;
                                 var0.isContactpreload = true;
                                 return CompletableFuture.completedFuture(var12);
                              }

                              var11.remove(var13.third);
                              if (!var0.api.getWebClient().isActive()) {
                                 break label906;
                              }

                              var10000 = VariantHandler.findPrecartVariant(var11);
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }
                              break;
                           case 13:
                              var10001 = var2;
                              var10002 = var3;
                              var21 = var4;
                              var10004 = var5;
                              var15 = var1;
                              var14 = var10004;
                              var13 = var21;
                              var12 = var10002;
                              var11 = var10001;
                              var6.join();
                              var10000 = var0.shippingRate;
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              if (((ShippingRateSupplier)var10000.join()).get().equals(var0.task.getShippingRate()) || var0.task.getShippingRate().length() <= 3) {
                                 break label895;
                              }

                              var19 = var0.logger;
                              var20 = var0.shippingRate;
                              if (!var20.isDone()) {
                                 var17 = var20;
                                 var16 = "The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]";
                                 var18 = var19;
                                 return var17.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var19.error("The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]", ((ShippingRateSupplier)var20.join()).get(), var0.task.getShippingRate());
                              var10000 = var0.clearWithChangeJS();
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var10000.join();
                              var0.shippingRate = new CompletableFuture();
                              if (var11.isEmpty()) {
                                 var0.logger.info("There are no items with matching shipping rate. Handling...");
                                 var0.api.checkIsOOS();
                                 var0.configureShippingRate();
                                 var0.isPreload = false;
                                 var0.isContactpreload = true;
                                 return CompletableFuture.completedFuture(var12);
                              }

                              var11.remove(var13.third);
                              if (!var0.api.getWebClient().isActive()) {
                                 break label906;
                              }

                              var10000 = VariantHandler.findPrecartVariant(var11);
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }
                              break;
                           case 14:
                              var10001 = var2;
                              var10002 = var3;
                              var21 = var4;
                              var10004 = var5;
                              var15 = var1;
                              var14 = var10004;
                              var13 = var21;
                              var12 = var10002;
                              var11 = var10001;
                              if (((ShippingRateSupplier)var6.join()).get().equals(var0.task.getShippingRate()) || var0.task.getShippingRate().length() <= 3) {
                                 break label895;
                              }

                              var19 = var0.logger;
                              var20 = var0.shippingRate;
                              if (!var20.isDone()) {
                                 var17 = var20;
                                 var16 = "The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]";
                                 var18 = var19;
                                 return var17.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var19.error("The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]", ((ShippingRateSupplier)var20.join()).get(), var0.task.getShippingRate());
                              var10000 = var0.clearWithChangeJS();
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var10000.join();
                              var0.shippingRate = new CompletableFuture();
                              if (var11.isEmpty()) {
                                 var0.logger.info("There are no items with matching shipping rate. Handling...");
                                 var0.api.checkIsOOS();
                                 var0.configureShippingRate();
                                 var0.isPreload = false;
                                 var0.isContactpreload = true;
                                 return CompletableFuture.completedFuture(var12);
                              }

                              var11.remove(var13.third);
                              if (!var0.api.getWebClient().isActive()) {
                                 break label906;
                              }

                              var10000 = VariantHandler.findPrecartVariant(var11);
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }
                              break;
                           case 15:
                              JsonArray var10003 = var2;
                              var10004 = var3;
                              Triplet var10005 = var4;
                              String var10006 = var5;
                              var15 = var1;
                              var14 = var10006;
                              var13 = var10005;
                              var12 = var10004;
                              var11 = var10003;
                              var7.error(var8, ((ShippingRateSupplier)var6.join()).get(), var0.task.getShippingRate());
                              var10000 = var0.clearWithChangeJS();
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var10000.join();
                              var0.shippingRate = new CompletableFuture();
                              if (var11.isEmpty()) {
                                 var0.logger.info("There are no items with matching shipping rate. Handling...");
                                 var0.api.checkIsOOS();
                                 var0.configureShippingRate();
                                 var0.isPreload = false;
                                 var0.isContactpreload = true;
                                 return CompletableFuture.completedFuture(var12);
                              }

                              var11.remove(var13.third);
                              if (!var0.api.getWebClient().isActive()) {
                                 break label906;
                              }

                              var10000 = VariantHandler.findPrecartVariant(var11);
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }
                              break;
                           case 16:
                              var10001 = var2;
                              var12 = var3;
                              var11 = var10001;
                              var6.join();
                              var0.shippingRate = new CompletableFuture();
                              if (var11.isEmpty()) {
                                 var0.logger.info("There are no items with matching shipping rate. Handling...");
                                 var0.api.checkIsOOS();
                                 var0.configureShippingRate();
                                 var0.isPreload = false;
                                 var0.isContactpreload = true;
                                 return CompletableFuture.completedFuture(var3);
                              }

                              var11.remove(var4.third);
                              if (!var0.api.getWebClient().isActive()) {
                                 break label906;
                              }

                              var10000 = VariantHandler.findPrecartVariant(var11);
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }
                              break;
                           case 17:
                              var10001 = var2;
                              var12 = var3;
                              var11 = var10001;
                              var6.join();
                              break label906;
                           case 18:
                              var10000 = var1;
                              var10001 = var2;
                              var12 = var3;
                              var11 = var10001;
                              var10000.join();
                              var10000 = var0.checkCart();
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }
                              break label877;
                           case 19:
                              var10000 = var1;
                              var12 = var3;
                              break label877;
                           default:
                              throw new IllegalArgumentException();
                        }

                        do {
                           var13 = (Triplet)var10000.join();
                           if (var13 == null) {
                              var10000 = var0.waitTilCartCookie();
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }
                              break label908;
                           }

                           var0.shippingRate = new CompletableFuture();
                           var14 = (String)var13.first;
                           var0.precartItemName = var14;
                           if (var0.task.getSite() == Site.KITH) {
                              var10000 = var0.atcBasic(var14);
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var10000.join();
                           } else {
                              var10000 = var0.atcAJAX(var14, false);
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var10000.join();
                           }

                           var10000 = var0.walletsGenCheckout();
                           if (!var10000.isDone()) {
                              var6 = var10000;
                              return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                           }

                           var12 = (String)var10000.join();
                           var10000 = var0.handlePreload(var12);
                           if (!var10000.isDone()) {
                              var6 = var10000;
                              return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                           }

                           var12 = (String)var10000.join();
                           if (!(Boolean)var13.second) {
                              var0.isPreload = false;
                              break label906;
                           }

                           var10000 = var0.getCheckoutURL(var12);
                           if (!var10000.isDone()) {
                              var6 = var10000;
                              return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                           }

                           var10000.join();
                           if (var0.api.getCookies().contains("_shopify_checkpoint")) {
                              var10000 = var0.checkCaptcha(var12);
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                              }

                              var10000.join();
                           }

                           var10000 = var0.submitContact(var12);
                           if (!var10000.isDone()) {
                              var6 = var10000;
                              return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                           }

                           var10000.join();
                           var15 = var0.getShippingRateOld();
                           var10000 = CompletableFuture.allOf(var15, var0.getShippingPage(var12));
                           if (!var10000.isDone()) {
                              var6 = var10000;
                              return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                           }

                           var10000.join();
                           var10000 = var0.shippingRate;
                           if (!var10000.isDone()) {
                              var6 = var10000;
                              return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                           }

                           if (((ShippingRateSupplier)var10000.join()).get().equals(var0.task.getShippingRate()) || var0.task.getShippingRate().length() <= 3) {
                              break label895;
                           }

                           var19 = var0.logger;
                           var20 = var0.shippingRate;
                           if (!var20.isDone()) {
                              var17 = var20;
                              var16 = "The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]";
                              var18 = var19;
                              return var17.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                           }

                           var19.error("The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]", ((ShippingRateSupplier)var20.join()).get(), var0.task.getShippingRate());
                           var10000 = var0.clearWithChangeJS();
                           if (!var10000.isDone()) {
                              var6 = var10000;
                              return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                           }

                           var10000.join();
                           var0.shippingRate = new CompletableFuture();
                           if (var11.isEmpty()) {
                              var0.logger.info("There are no items with matching shipping rate. Handling...");
                              var0.api.checkIsOOS();
                              var0.configureShippingRate();
                              var0.isPreload = false;
                              var0.isContactpreload = true;
                              return CompletableFuture.completedFuture(var12);
                           }

                           var11.remove(var13.third);
                           if (!var0.api.getWebClient().isActive()) {
                              break label906;
                           }

                           var10000 = VariantHandler.findPrecartVariant(var11);
                        } while(var10000.isDone());

                        var6 = var10000;
                        return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                     }

                     var10000 = var0.submitShipping(var12);
                     if (!var10000.isDone()) {
                        var6 = var10000;
                        return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                     }

                     var10000.join();
                     break label906;
                  }

                  var10000.join();
                  var10000 = var0.walletsGenCheckout();
                  if (!var10000.isDone()) {
                     var6 = var10000;
                     return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
                  }
               }

               var12 = (String)var10000.join();
               var10000 = var0.handlePreload(var12);
               if (!var10000.isDone()) {
                  var6 = var10000;
                  return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
               }
               break label905;
            }

            var10000 = var0.clearWithChangeJS();
            if (!var10000.isDone()) {
               var6 = var10000;
               return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
            }

            var10000.join();
            var10000 = var0.checkCart();
            if (!var10000.isDone()) {
               var6 = var10000;
               return var6.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadWallets);
            }
         }

         var10000.join();
         var0.api.checkIsOOS();
         var0.configureShippingRate();
         return CompletableFuture.completedFuture(var12);
      }

      var12 = (String)var10000.join();
      var0.isPreload = false;
      var0.isContactpreload = false;
      return CompletableFuture.completedFuture(var12);
   }

   public static CompletableFuture async$preloadContactOnly(ShopifySafe var0, CompletableFuture var1, JsonArray var2, Triplet var3, String var4, String var5, int var6, Object var7) {
      CompletableFuture var10000;
      label163: {
         JsonArray var8;
         Triplet var9;
         String var10;
         CompletableFuture var11;
         label142: {
            label164: {
               label139: {
                  label165: {
                     label136: {
                        label166: {
                           label167: {
                              label168: {
                                 label128: {
                                    label127: {
                                       label169: {
                                          label150: {
                                             label122: {
                                                JsonArray var10001;
                                                Triplet var10002;
                                                String var10003;
                                                switch (var6) {
                                                   case 0:
                                                      var10000 = var0.fetchProductsJSON(false);
                                                      if (!var10000.isDone()) {
                                                         var11 = var10000;
                                                         return var11.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadContactOnly);
                                                      }
                                                      break;
                                                   case 1:
                                                      var10000 = var1;
                                                      break;
                                                   case 2:
                                                      var10000 = var1;
                                                      var8 = var2;
                                                      break label122;
                                                   case 3:
                                                      var10000 = var1;
                                                      var10001 = var2;
                                                      var10002 = var3;
                                                      var10 = var4;
                                                      var9 = var10002;
                                                      var8 = var10001;
                                                      break label169;
                                                   case 4:
                                                      var10000 = var1;
                                                      var10001 = var2;
                                                      var10002 = var3;
                                                      var10 = var4;
                                                      var9 = var10002;
                                                      var8 = var10001;
                                                      break label150;
                                                   case 5:
                                                      var10000 = var1;
                                                      var10001 = var2;
                                                      var10002 = var3;
                                                      var10 = var4;
                                                      var9 = var10002;
                                                      var8 = var10001;
                                                      break label128;
                                                   case 6:
                                                      var10000 = var1;
                                                      var10001 = var2;
                                                      var10002 = var3;
                                                      var10 = var4;
                                                      var9 = var10002;
                                                      var8 = var10001;
                                                      break label168;
                                                   case 7:
                                                      var10000 = var1;
                                                      var10001 = var2;
                                                      var10002 = var3;
                                                      var10003 = var4;
                                                      var4 = var5;
                                                      var10 = var10003;
                                                      var9 = var10002;
                                                      var8 = var10001;
                                                      break label167;
                                                   case 8:
                                                      var10000 = var1;
                                                      var10001 = var2;
                                                      var10002 = var3;
                                                      var10003 = var4;
                                                      var4 = var5;
                                                      var10 = var10003;
                                                      var9 = var10002;
                                                      var8 = var10001;
                                                      break label166;
                                                   case 9:
                                                      var10000 = var1;
                                                      var10001 = var2;
                                                      var10002 = var3;
                                                      var10003 = var4;
                                                      var4 = var5;
                                                      var10 = var10003;
                                                      var9 = var10002;
                                                      var8 = var10001;
                                                      break label165;
                                                   case 10:
                                                      var10000 = var1;
                                                      var10001 = var2;
                                                      var10002 = var3;
                                                      var10003 = var4;
                                                      var4 = var5;
                                                      var10 = var10003;
                                                      var9 = var10002;
                                                      var8 = var10001;
                                                      break label164;
                                                   case 11:
                                                      var10000 = var1;
                                                      var10001 = var2;
                                                      var10002 = var3;
                                                      var10003 = var4;
                                                      var4 = var5;
                                                      var10 = var10003;
                                                      var9 = var10002;
                                                      var8 = var10001;
                                                      break label142;
                                                   case 12:
                                                      var10000 = var1;
                                                      var4 = var5;
                                                      break label163;
                                                   default:
                                                      throw new IllegalArgumentException();
                                                }

                                                var8 = ((JsonObject)var10000.join()).getJsonArray("products");
                                                var10000 = VariantHandler.findPrecartVariant(var8);
                                                if (!var10000.isDone()) {
                                                   var11 = var10000;
                                                   return var11.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadContactOnly);
                                                }
                                             }

                                             var9 = (Triplet)var10000.join();
                                             if (var9 == null) {
                                                var0.isContactpreload = false;
                                                var0.logger.error("There is no item to preload. Handling...");
                                                return CompletableFuture.completedFuture((Object)null);
                                             }

                                             var0.shippingRate = new CompletableFuture();
                                             var10 = (String)var9.first;
                                             var0.precartItemName = var10;
                                             if (var0.task.getSite() == Site.KITH) {
                                                var10000 = var0.atcBasic(var10);
                                                if (!var10000.isDone()) {
                                                   var11 = var10000;
                                                   return var11.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadContactOnly);
                                                }
                                                break label169;
                                             }

                                             var10000 = var0.atcAJAX(var10, false);
                                             if (!var10000.isDone()) {
                                                var11 = var10000;
                                                return var11.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadContactOnly);
                                             }
                                          }

                                          var10000.join();
                                          break label127;
                                       }

                                       var10000.join();
                                    }

                                    var10000 = var0.genCheckoutURL(false);
                                    if (!var10000.isDone()) {
                                       var11 = var10000;
                                       return var11.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadContactOnly);
                                    }
                                 }

                                 var4 = (String)var10000.join();
                                 var10000 = var0.handlePreload(var4);
                                 if (!var10000.isDone()) {
                                    var11 = var10000;
                                    return var11.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadContactOnly);
                                 }
                              }

                              var4 = (String)var10000.join();
                              if (!(Boolean)var9.second) {
                                 var0.api.setOOS();
                                 var0.isContactpreload = false;
                                 break label139;
                              }

                              var10000 = var0.getCheckoutURL(var4);
                              if (!var10000.isDone()) {
                                 var11 = var10000;
                                 return var11.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadContactOnly);
                              }
                           }

                           var10000.join();
                           if (!var0.api.getCookies().contains("_shopify_checkpoint")) {
                              break label136;
                           }

                           var10000 = var0.checkCaptcha(var4);
                           if (!var10000.isDone()) {
                              var11 = var10000;
                              return var11.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadContactOnly);
                           }
                        }

                        var10000.join();
                     }

                     var10000 = var0.submitContact(var4);
                     if (!var10000.isDone()) {
                        var11 = var10000;
                        return var11.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadContactOnly);
                     }
                  }

                  var10000.join();
                  var0.isContactpreload = true;
               }

               var10000 = var0.clearWithChangeJS();
               if (!var10000.isDone()) {
                  var11 = var10000;
                  return var11.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadContactOnly);
               }
            }

            var10000.join();
            var10000 = var0.checkCart();
            if (!var10000.isDone()) {
               var11 = var10000;
               return var11.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadContactOnly);
            }
         }

         var10000.join();
         var10000 = var0.confirmClear(var4);
         if (!var10000.isDone()) {
            var11 = var10000;
            return var11.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$preloadContactOnly);
         }
      }

      var10000.join();
      var0.api.checkIsOOS();
      var0.configureShippingRate();
      return CompletableFuture.completedFuture(var4);
   }

   public static CompletableFuture async$createQueue(ShopifySafe var0, int var1, CompletableFuture var2, int var3, Object var4) {
      switch (var3) {
         case 0:
            var1 = 0;
            break;
         case 1:
            var2.join();
            var0.api.getCookies().clear();
            break;
         default:
            throw new IllegalArgumentException();
      }

      while(var1++ < Integer.MAX_VALUE) {
         CompletableFuture var5 = var0.attemptGenCheckoutUrl(true);
         if (!var5.isDone()) {
            var2 = var5;
            return var2.exceptionally(Function.identity()).thenCompose(ShopifySafe::async$createQueue);
         }

         var5.join();
         var0.api.getCookies().clear();
      }

      return CompletableFuture.completedFuture((Object)null);
   }
}
