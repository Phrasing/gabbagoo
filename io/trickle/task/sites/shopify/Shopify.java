package io.trickle.task.sites.shopify;

import io.netty.handler.codec.http.cookie.ClientCookieDecoder;
import io.netty.handler.codec.http.cookie.Cookie;
import io.trickle.account.Account;
import io.trickle.account.AccountController;
import io.trickle.core.Controller;
import io.trickle.core.Engine;
import io.trickle.core.VertxSingleton;
import io.trickle.core.actor.TaskActor;
import io.trickle.harvester.CaptchaToken;
import io.trickle.harvester.Harvester;
import io.trickle.harvester.SolveFuture;
import io.trickle.harvester.TokenController;
import io.trickle.task.Task;
import io.trickle.task.sites.Site;
import io.trickle.task.sites.shopify.util.PaymentTokenSupplier;
import io.trickle.task.sites.shopify.util.REDIRECT_STATUS;
import io.trickle.task.sites.shopify.util.ShippingRateSupplier;
import io.trickle.task.sites.shopify.util.SiteParser;
import io.trickle.task.sites.shopify.util.VariantHandler;
import io.trickle.util.Pair;
import io.trickle.util.Storage;
import io.trickle.util.Utils;
import io.trickle.util.analytics.Analytics;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.trickle.webclient.RealClientFactory;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.core.shareddata.Lock;
import io.vertx.core.shareddata.SharedData;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.ext.web.multipart.MultipartForm;
import java.io.PrintStream;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Shopify extends TaskActor {
   public static Pattern SHIPPINGRATE_PATTERN = Pattern.compile("data-shipping-method=\"(.*?)\"");
   public Pair propertiesPair;
   public List TEXTAREA_PARAMS = new LinkedList();
   public static boolean queueLogic = true;
   public CompletableFuture shippingRate;
   public boolean isSmart;
   public Presolver presolver;
   public String size;
   public AtomicBoolean isCPMonitorTriggered = new AtomicBoolean(false);
   public boolean isRestockMode;
   public Long productPickupTime;
   public static Pattern FIELD_ERR_PATTERN;
   public List negativeKeywords = new LinkedList();
   public static Harvester[] HARVESTERS;
   public static Pattern AUTHENTICITY_CHECKOUT_TOKEN_PATTERN = Pattern.compile("data-payment-form=\"\" action=\".*?\" accept-charset=\".*?\" method=\"post\"><input type=\"hidden\" name=\"_method\" value=\"patch\" /><input type=\"hidden\" name=\"authenticity_token\" value=\"(.*?)\"");
   public boolean isKeyword = false;
   public static Pattern GATEWAY_CARD_PATTERN = Pattern.compile("data-gateway-name=\"credit_card\".*?data-select-gateway=\"(.*?)\"", 32);
   public String instanceSignal;
   public boolean noProcessOnFirstTry = true;
   public String lockKey;
   public static Pattern TEXTAREA_PARAMS_PATTERN = Pattern.compile("textarea name=\"(.*?)\" id=\"");
   public boolean isExtra;
   public CompletableFuture paymentToken;
   public boolean isCod;
   public ShopifyAPI api;
   public static Pattern CHECK_VARIANT_PATTERN = Pattern.compile("^[0-9]+$");
   public long prevTime;
   public static AtomicBoolean isHarvesterInited;
   public boolean isShippingSubmittedWithWallets;
   public static Pattern TOTAL_PRICE_PATTERN = Pattern.compile("checkout_total_price\" value=\"(.*?)\"");
   public static Pattern CHECK_EL_PATTERN = Pattern.compile("(https.*)", 2);
   public String BROWSER_HEIGHT;
   public Task task;
   public CompletableFuture cpMonitor;
   public static Pattern AUTHENTICITY_TOKEN_PATTERN = Pattern.compile("authenticity_token\" value=\"(.*?)\"");
   public Mode MODE;
   public static ConcurrentHashMap lockMap;
   public static CompletableFuture[] harvesterCFs;
   public List positiveKeywords = new LinkedList();
   public static Pattern SITE_URL_PATTERN;
   public String precartItemName = "123456789";
   public boolean isEarly;
   public Flow$Actions FLOW_ACTIONS;
   public String BROWSER_WIDTH;
   public int counter429;
   public Integer price;
   public boolean hasShippingAlreadySubmittedAPI;
   public static Pattern TOTAL_PRICE_BACKUP_PATTERN = Pattern.compile("data-checkout-payment-due-target=\"(.*?)\"");
   public String authenticity;
   public String paymentGateway;
   public boolean isEL = false;
   public static Instant instanceTime;
   public static int TIMEZONE_OFFSET;

   public void shutOffPresolver(boolean var1) {
      if (var1 || !this.isEarly) {
         if (this.presolver != null) {
            this.presolver.close();
         }

         if (this.cpMonitor != null) {
            this.cpMonitor.complete((Object)null);
         }
      }

   }

   public boolean updatePrice(String var1) {
      Matcher var2 = TOTAL_PRICE_PATTERN.matcher(var1);
      Matcher var3 = TOTAL_PRICE_BACKUP_PATTERN.matcher(var1);
      if (!var1.contains("Calculating taxes")) {
         if (var2.find()) {
            this.price = Integer.parseInt(var2.group(1));
         } else if (var3.find()) {
            this.price = Integer.parseInt(var3.group(1));
         } else {
            this.logger.info("No price found");
         }

         return true;
      } else {
         return false;
      }
   }

   public CompletableFuture attemptGenCheckoutUrl(boolean var1) {
      int var2 = 0;
      this.logger.info("Generating checkout URL [l]...");

      while(super.running && var2++ < Integer.MAX_VALUE) {
         CompletableFuture var7;
         CompletableFuture var9;
         try {
            HttpRequest var3 = this.api.emptyCheckoutViaCart();
            var9 = Request.send(var3, this.api.emptyCheckoutViaCartForm());
            if (!var9.isDone()) {
               var7 = var9;
               return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$attemptGenCheckoutUrl);
            }

            HttpResponse var4 = (HttpResponse)var9.join();
            if (var4 != null && (var4.statusCode() == 302 || var4.statusCode() == 301)) {
               String var5 = var4.getHeader("location");
               REDIRECT_STATUS var6 = REDIRECT_STATUS.checkRedirectStatus(var5);
               if (var6 == REDIRECT_STATUS.PASSWORD) {
                  return CompletableFuture.completedFuture((Object)null);
               }

               if (var1 != 0 && var6 == REDIRECT_STATUS.CHECKPOINT) {
                  return CompletableFuture.completedFuture((Object)null);
               }

               var9 = this.handleRedirect(var4, true);
               if (!var9.isDone()) {
                  var7 = var9;
                  return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$attemptGenCheckoutUrl);
               }

               var9.join();
               return CompletableFuture.completedFuture(var5);
            }

            if (var4 != null) {
               this.logger.info("Unable to generate checkout url. Handling - {}", var4.statusCode());
               var9 = VertxUtil.sleep((long)this.task.getRetryDelay());
               if (!var9.isDone()) {
                  var7 = var9;
                  return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$attemptGenCheckoutUrl);
               }

               var9.join();
            }
         } catch (Throwable var8) {
            if (!var8.getMessage().contains("Connection was closed")) {
               this.logger.error("Failed genning checkoutURL: {}", var8.getMessage());
            }

            var9 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
            if (!var9.isDone()) {
               var7 = var9;
               return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$attemptGenCheckoutUrl);
            }

            var9.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture atcAJAX(String var1, boolean var2) {
      String var3 = this.api.getCookies().getCookieValue("cart_sig");
      int var4 = 0;
      this.logger.info("Attempting ATC [AJAX]");

      while(super.running && var4++ < Integer.MAX_VALUE) {
         if ((this.isKeyword || this.isEL) && Utils.isInteger(this.task.getKeywords()[0])) {
            var1 = this.task.getKeywords()[0];
         }

         CompletableFuture var8;
         CompletableFuture var11;
         try {
            HttpRequest var5 = this.api.atcAJAX(var1);
            var11 = Request.send(var5);
            if (!var11.isDone()) {
               var8 = var11;
               return var8.exceptionally(Function.identity()).thenCompose(Shopify::async$atcAJAX);
            }

            HttpResponse var6 = (HttpResponse)var11.join();
            if (var6 != null) {
               if (var6.statusCode() == 200) {
                  VertxUtil.sendSignal(this.api.getSiteURL());
                  if (this.hasTrulyCarted(var3)) {
                     VertxUtil.sendSignal(var1);
                     JsonObject var7 = var6.bodyAsJsonObject();
                     return CompletableFuture.completedFuture(var7);
                  }

                  this.logger.warn("Item is not yet cartable! Please wait for sale to go live");
               } else {
                  if (var2 != 0 && var6.statusCode() == 401) {
                     return CompletableFuture.completedFuture((Object)null);
                  }

                  if (var6.statusCode() == 302 || var6.statusCode() == 301 || var6.statusCode() == 401) {
                     var11 = this.handleRedirect(var6, false);
                     if (!var11.isDone()) {
                        var8 = var11;
                        return var8.exceptionally(Function.identity()).thenCompose(Shopify::async$atcAJAX);
                     }

                     boolean var10 = (Boolean)var11.join();
                     if (!var10) {
                        continue;
                     }
                  }

                  this.logger.warn("Failed attempting ATC [AJAX]: status: '{}'", var6.statusCode());
               }

               var11 = VertxUtil.signalSleep(var1, (long)this.task.getMonitorDelay());
               if (!var11.isDone()) {
                  var8 = var11;
                  return var8.exceptionally(Function.identity()).thenCompose(Shopify::async$atcAJAX);
               }

               var11.join();
            }
         } catch (Throwable var9) {
            this.logger.error("Failed attempting ATC [AJAX]]: {}", var9.getMessage());
            var11 = super.randomSleep(3000);
            if (!var11.isDone()) {
               var8 = var11;
               return var8.exceptionally(Function.identity()).thenCompose(Shopify::async$atcAJAX);
            }

            var11.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture clearWithChangeJS() {
      int var1 = 0;

      while(super.running && var1++ < Integer.MAX_VALUE) {
         CompletableFuture var5;
         CompletableFuture var7;
         try {
            HttpRequest var2 = this.api.changeJS();
            var7 = Request.send(var2, this.changeForm());
            if (!var7.isDone()) {
               var5 = var7;
               return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$clearWithChangeJS);
            }

            HttpResponse var3 = (HttpResponse)var7.join();
            if (var3 != null) {
               JsonObject var4;
               if (var3.statusCode() == 200 && (var4 = var3.bodyAsJsonObject()).getInteger("item_count") == 0) {
                  return CompletableFuture.completedFuture(var4);
               }

               if (var3.statusCode() == 302 || var3.statusCode() == 301) {
                  var7 = this.handleRedirect(var3, false);
                  if (!var7.isDone()) {
                     var5 = var7;
                     return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$clearWithChangeJS);
                  }

                  var7.join();
               }

               this.logger.warn("Failed changing cart: status: '{}'", var3.statusCode());
               var7 = VertxUtil.sleep((long)this.task.getRetryDelay());
               if (!var7.isDone()) {
                  var5 = var7;
                  return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$clearWithChangeJS);
               }

               var7.join();
            }
         } catch (Throwable var6) {
            this.logger.error("Failed changing cart: {}", var6.getMessage());
            var7 = super.randomSleep(3000);
            if (!var7.isDone()) {
               var5 = var7;
               return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$clearWithChangeJS);
            }

            var7.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$shippingAndBillingPostAlreadyCarted(Shopify param0, int param1, HttpRequest param2, CompletableFuture param3, HttpResponse param4, Throwable param5, int param6, Object param7) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$handleOOS(Shopify param0, String param1, int param2, HttpRequest param3, CompletableFuture param4, HttpResponse param5, Throwable param6, int param7, Object param8) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture submitCheckpoint(CaptchaToken var1) {
      this.logger.info("Submitting checkpoint...");
      int var2 = 0;

      while(super.running && var2++ < Integer.MAX_VALUE) {
         CompletableFuture var5;
         CompletableFuture var7;
         try {
            HttpRequest var3 = this.api.submitCheckpoint();
            var7 = Request.send(var3, checkpointForm(var1));
            if (!var7.isDone()) {
               var5 = var7;
               return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$submitCheckpoint);
            }

            HttpResponse var4 = (HttpResponse)var7.join();
            if (var4 != null) {
               if ((var4.statusCode() == 302 || var4.statusCode() == 301) && this.api.checkpointClient.cookieStore().contains("_shopify_checkpoint")) {
                  this.api.getCookies().put("_shopify_checkpoint", this.api.checkpointClient.cookieStore().getCookieValue("_shopify_checkpoint"), this.api.getSiteURL());
                  return CompletableFuture.completedFuture((Object)null);
               }

               this.logger.warn("Retrying submitting checkpoint: status: '{}'", var4.statusCode());
               var7 = VertxUtil.sleep((long)this.task.getRetryDelay());
               if (!var7.isDone()) {
                  var5 = var7;
                  return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$submitCheckpoint);
               }

               var7.join();
            }
         } catch (Throwable var6) {
            this.logger.error("Error with checkpoint submission: {}", var6.getMessage());
            var7 = super.randomSleep(3000);
            if (!var7.isDone()) {
               var5 = var7;
               return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$submitCheckpoint);
            }

            var7.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture fetchProductsJSON(boolean var1) {
      int var2 = 0;
      if (var1 != 0) {
         this.logger.info("Monitoring [KEYWORD]");
      } else {
         this.logger.info("Preparing precart...");
      }

      Lock var3 = null;

      while(super.running && var2++ < Integer.MAX_VALUE) {
         CompletableFuture var10;
         CompletableFuture var15;
         try {
            CompletionStage var14;
            CompletionStage var16;
            if (var1 == 0) {
               var16 = (CompletionStage)lockMap.get(this.lockKey);
               if (!var16.toCompletableFuture().isDone()) {
                  var14 = var16;
                  return var14.exceptionally(Function.identity()).thenCompose(Shopify::async$fetchProductsJSON).toCompletableFuture();
               }

               var3 = (Lock)var16.toCompletableFuture().join();
               SharedData var4 = super.vertx.sharedData();
               LocalMap var5 = var4.getLocalMap(this.task.getSiteUserEntry());
               if (var5.containsKey("products")) {
                  var3.release();
                  return CompletableFuture.completedFuture((JsonObject)var5.get("products"));
               }
            }

            HttpRequest var12 = this.api.productsJSON((boolean)var1);
            var15 = Request.send(var12);
            if (!var15.isDone()) {
               var10 = var15;
               return var10.exceptionally(Function.identity()).thenCompose(Shopify::async$fetchProductsJSON);
            }

            HttpResponse var13 = (HttpResponse)var15.join();
            if (var13 != null) {
               if (var13.statusCode() == 200) {
                  this.initKeywords();
                  VertxUtil.sendSignal(this.api.getSiteURL());
                  JsonObject var6 = var13.bodyAsJsonObject();
                  if (var1 == 0) {
                     SharedData var7 = super.vertx.sharedData();
                     var7.getLocalMap(this.task.getSiteUserEntry()).put("products", var6);
                     if (var3 != null) {
                        Iterator var8 = lockMap.keys().asIterator();

                        while(var8.hasNext()) {
                           String var9 = (String)var8.next();
                           var16 = (CompletionStage)lockMap.get(var9);
                           if (!var16.toCompletableFuture().isDone()) {
                              var14 = var16;
                              return var14.exceptionally(Function.identity()).thenCompose(Shopify::async$fetchProductsJSON).toCompletableFuture();
                           }

                           ((Lock)var16.toCompletableFuture().join()).release();
                        }
                     }
                  }

                  return CompletableFuture.completedFuture(var6);
               }

               if (var1 == 0 && var13.statusCode() == 401) {
                  return CompletableFuture.completedFuture(new JsonObject());
               }

               if (var13.statusCode() != 302 && var13.statusCode() != 301 && var13.statusCode() != 401) {
                  this.logger.warn("Failed waiting for restock [KEYWORD]: status: '{}'", var13.statusCode());
                  var15 = VertxUtil.signalSleep(this.instanceSignal, (long)this.task.getRetryDelay());
                  if (!var15.isDone()) {
                     var10 = var15;
                     return var10.exceptionally(Function.identity()).thenCompose(Shopify::async$fetchProductsJSON);
                  }

                  var15.join();
               } else {
                  var15 = this.handleRedirect(var13, false);
                  if (!var15.isDone()) {
                     var10 = var15;
                     return var10.exceptionally(Function.identity()).thenCompose(Shopify::async$fetchProductsJSON);
                  }

                  var15.join();
               }
            }
         } catch (Throwable var11) {
            if (var3 != null) {
               var3.release();
            }

            this.logger.error("Failed waiting for restock [KEYWORD]: {}", var11.getMessage());
            var15 = super.randomSleep(3000);
            if (!var15.isDone()) {
               var10 = var15;
               return var10.exceptionally(Function.identity()).thenCompose(Shopify::async$fetchProductsJSON);
            }

            var15.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture processPayment(String var1) {
      this.logger.info("Processing...");
      int var2 = 0;

      while(super.running && var2++ < Integer.MAX_VALUE) {
         CompletableFuture var11;
         CompletableFuture var14;
         try {
            MultiMap var15;
            if (this.isRestockMode) {
               var14 = this.processingFormRestockMode(var1);
               if (!var14.isDone()) {
                  var11 = var14;
                  return var11.exceptionally(Function.identity()).thenCompose(Shopify::async$processPayment);
               }

               var15 = (MultiMap)var14.join();
            } else {
               var14 = this.processingForm(var1);
               if (!var14.isDone()) {
                  var11 = var14;
                  return var11.exceptionally(Function.identity()).thenCompose(Shopify::async$processPayment);
               }

               var15 = (MultiMap)var14.join();
            }

            MultiMap var3 = var15;
            long var4 = System.currentTimeMillis();
            HttpRequest var6 = this.api.postPayment(var1);
            var14 = Request.send(var6, var3);
            if (!var14.isDone()) {
               var11 = var14;
               return var11.exceptionally(Function.identity()).thenCompose(Shopify::async$processPayment);
            }

            HttpResponse var7 = (HttpResponse)var14.join();
            if (var7 != null) {
               if (!this.api.getCookies().contains("_shopify_checkpoint") && (System.currentTimeMillis() - var4 > 30000L || var7.statusCode() == 403)) {
                  this.logger.info("Rotating proxy");
                  this.api.rotateProxy();
               }

               if (this.isRestockMode && var7.statusCode() == 302) {
                  var14 = this.paymentToken;
                  if (!var14.isDone()) {
                     var11 = var14;
                     return var11.exceptionally(Function.identity()).thenCompose(Shopify::async$processPayment);
                  }

                  ((PaymentTokenSupplier)var14.join()).setSubmittedSuccessfully();
               } else {
                  this.genPaymentToken();
               }

               String var8;
               if (var7.statusCode() != 302 && var7.statusCode() != 301) {
                  if (var7.statusCode() == 429 && ++this.counter429 > 8) {
                     throw new Throwable("Checkout expired. Restarting task...");
                  }

                  if (var7.statusCode() == 200) {
                     var8 = var7.bodyAsString();
                     String var13 = Analytics.parseOrderId(var8);
                     if (var13 != null) {
                        this.logger.error("Processing error: {}", var13);
                     }

                     if (this.task.getSite() != Site.KITH) {
                        this.updateGateway(var8);
                     }

                     this.updateCheckoutParams(var8);
                     this.updatePrice(var8);
                  } else {
                     this.logger.warn("Error processing: status: '{}'", var7.statusCode());
                  }

                  if (this.noProcessOnFirstTry) {
                     this.noProcessOnFirstTry = false;
                  } else {
                     var14 = VertxUtil.sleep((long)this.task.getRetryDelay());
                     if (!var14.isDone()) {
                        var11 = var14;
                        return var11.exceptionally(Function.identity()).thenCompose(Shopify::async$processPayment);
                     }

                     var14.join();
                  }
               } else {
                  var8 = var7.getHeader("location");
                  REDIRECT_STATUS var9 = REDIRECT_STATUS.checkRedirectStatus(var8);
                  var14 = this.handleRedirect(var7, true);
                  if (!var14.isDone()) {
                     var11 = var14;
                     return var11.exceptionally(Function.identity()).thenCompose(Shopify::async$processPayment);
                  }

                  Boolean var10 = (Boolean)var14.join();
                  this.shutOffPresolver(true);
                  if (var10 == null || var10) {
                     if (var8.contains("/processing")) {
                        VertxUtil.sendSignal("processing" + this.instanceSignal);
                        return CompletableFuture.completedFuture(var8);
                     }

                     if (var9 != REDIRECT_STATUS.QUEUE_NEW) {
                        if (!var8.contains("/stock_problems?previous_step=payment_method")) {
                           this.logger.warn("Error processing: status: '{}'", var8);
                           var14 = this.getProcessingPage(var1, false);
                           if (!var14.isDone()) {
                              var11 = var14;
                              return var11.exceptionally(Function.identity()).thenCompose(Shopify::async$processPayment);
                           }

                           var14.join();
                           if (this.noProcessOnFirstTry) {
                              this.noProcessOnFirstTry = false;
                              continue;
                           }
                        } else {
                           this.logger.info("Processing. " + (var8.contains("stock_problem") ? "OKAY" : "Possible error."));
                           this.counter429 = 0;
                        }
                     }

                     if (var8.contains("stock_problem")) {
                        if (!this.isShippingSubmittedWithWallets) {
                           var14 = this.walletsSubmitShipping(var1);
                           if (!var14.isDone()) {
                              var11 = var14;
                              return var11.exceptionally(Function.identity()).thenCompose(Shopify::async$processPayment);
                           }

                           var14.join();
                           var14 = this.getProcessingPage(var1, true);
                           if (!var14.isDone()) {
                              var11 = var14;
                              return var11.exceptionally(Function.identity()).thenCompose(Shopify::async$processPayment);
                           }

                           var14.join();
                        } else if (this.api.getCookies().contains("_shopify_checkpoint") || this.price == null) {
                           var14 = this.getProcessingPage(var1, true);
                           if (!var14.isDone()) {
                              var11 = var14;
                              return var11.exceptionally(Function.identity()).thenCompose(Shopify::async$processPayment);
                           }

                           var14.join();
                        }
                     }

                     var14 = VertxUtil.signalSleep("processing" + this.instanceSignal, (long)this.task.getMonitorDelay());
                     if (!var14.isDone()) {
                        var11 = var14;
                        return var11.exceptionally(Function.identity()).thenCompose(Shopify::async$processPayment);
                     }

                     var14.join();
                  }
               }
            }
         } catch (Exception var12) {
            if (!var12.getMessage().equals("io.vertx.core.VertxException: Connection was closed")) {
               this.logger.error("Error processing: {}", var12.getMessage());
               var14 = super.randomSleep(3000);
               if (!var14.isDone()) {
                  var11 = var14;
                  return var11.exceptionally(Function.identity()).thenCompose(Shopify::async$processPayment);
               }

               var14.join();
            }
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture handlePreload(String var1) {
      if (var1.contains("checkout")) {
         boolean var2 = var1.contains("?");
         String[] var3 = var1.split("\\?");
         if (var2 && var3[1].contains("key")) {
            this.api.key = var3[1].split("key=")[1];
            this.api.key = this.api.key.split("&")[0];
         }

         var1 = var2 ? var3[0] : var1;
         var1 = var1.split("checkouts/")[1];
         if (var1.startsWith("c/")) {
            this.api.graphUnstable = true;
            throw new Exception("Restarting task.");
         } else {
            return CompletableFuture.completedFuture(var1);
         }
      } else {
         throw new Exception("Bad checkout URL.");
      }
   }

   public JsonObject walletsGenCheckoutUrlForm() {
      JsonObject var1 = new JsonObject();
      JsonObject var2 = (new JsonObject()).put("is_upstream_button", false).put("page_type", "product").put("presentment_currency", this.api.getCookies().getCookieValue("cart_currency")).put("secret", true).put("wallet_name", "Checkout").put("cart_token", this.api.getCookies().getCookieValue("cart"));
      var1.put("checkout", var2);
      return var1;
   }

   public CompletableFuture run() {
      CompletableFuture var10000 = initHarvesters();
      CompletableFuture var7;
      if (!var10000.isDone()) {
         var7 = var10000;
         return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$run);
      } else {
         var10000.join();

         try {
            this.paymentGateway = SiteParser.getGatewayFromSite(this.task.getSite(), this.isCod);
            var10000 = this.initShopDetails();
            if (!var10000.isDone()) {
               var7 = var10000;
               return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$run);
            }

            var10000.join();
            if (this.task.getMode().contains("login")) {
               this.logger.info("Login required. Logging in...");
               var10000 = this.login();
               if (!var10000.isDone()) {
                  var7 = var10000;
                  return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$run);
               }

               var10000.join();
            }

            while(this.api.getWebClient().isActive()) {
               byte var1 = 0;

               try {
                  String var2;
                  if (!this.isExtra) {
                     var10000 = this.genCheckoutURL(ThreadLocalRandom.current().nextBoolean());
                     if (!var10000.isDone()) {
                        var7 = var10000;
                        return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$run);
                     }

                     var2 = (String)var10000.join();
                     var10000 = this.handlePreload(var2);
                     if (!var10000.isDone()) {
                        var7 = var10000;
                        return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$run);
                     }

                     var2 = (String)var10000.join();
                  } else {
                     var10000 = this.waitTilCartCookie();
                     if (!var10000.isDone()) {
                        var7 = var10000;
                        return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$run);
                     }

                     var1 = (Boolean)var10000.join();
                     var10000 = this.walletsGenCheckout();
                     if (!var10000.isDone()) {
                        var7 = var10000;
                        return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$run);
                     }

                     var2 = (String)var10000.join();
                     var10000 = this.handlePreload(var2);
                     if (!var10000.isDone()) {
                        var7 = var10000;
                        return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$run);
                     }

                     var2 = (String)var10000.join();
                  }

                  CompletableFuture var3 = this.checkCaptcha(var2);
                  if (ThreadLocalRandom.current().nextBoolean()) {
                     if (!var3.isDone()) {
                        return var3.exceptionally(Function.identity()).thenCompose(Shopify::async$run);
                     }

                     var3.join();
                  } else {
                     var10000 = VertxUtil.hardCodedSleep((long)ThreadLocalRandom.current().nextInt(75, 150));
                     if (!var10000.isDone()) {
                        var7 = var10000;
                        return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$run);
                     }

                     var10000.join();
                  }

                  var10000 = this.shippingAndBillingPut(var2);
                  if (!var10000.isDone()) {
                     var7 = var10000;
                     return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$run);
                  }

                  var10000.join();
                  JsonObject var5;
                  if (this.isKeyword) {
                     String var4 = null;

                     while(var4 == null) {
                        var10000 = this.fetchProductsJSON(true);
                        if (!var10000.isDone()) {
                           var7 = var10000;
                           return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$run);
                        }

                        var5 = (JsonObject)var10000.join();
                        var4 = VariantHandler.selectVariantFromKeyword(var5, this.task.getSize(), this.positiveKeywords, this.negativeKeywords, this);
                        if (var4 == null) {
                           var10000 = VertxUtil.signalSleep(this.instanceSignal, (long)this.task.getMonitorDelay());
                           if (!var10000.isDone()) {
                              var7 = var10000;
                              return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$run);
                           }

                           Object var6 = var10000.join();
                           if (var6 != null) {
                              var4 = VariantHandler.selectVariantFromKeyword(new JsonObject(var6.toString()), this.task.getSize(), this.positiveKeywords, this.negativeKeywords, this);
                           }
                        } else {
                           VertxUtil.sendSignal(this.instanceSignal, var5.encode());
                        }
                     }

                     this.instanceSignal = var4;
                  } else if (this.isEL) {
                     var10000 = this.fetchELJS();
                     if (!var10000.isDone()) {
                        var7 = var10000;
                        return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$run);
                     }

                     JsonObject var10 = (JsonObject)var10000.join();
                     this.instanceSignal = VariantHandler.selectVariantFromLink(var10, this.task.getSize(), this);
                  }

                  this.setProductPickupTime();
                  this.genPaymentToken();
                  byte var11 = 1;
                  if (!this.shippingRate.isDone()) {
                     if (this.isExtra) {
                        if (var1 == 0) {
                           var10000 = this.atcAJAX(this.instanceSignal, false);
                           if (!var10000.isDone()) {
                              var7 = var10000;
                              return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$run);
                           }

                           var10000.join();
                           Analytics.carts.increment();
                           var10000 = this.getShippingRateOld();
                           if (!var10000.isDone()) {
                              var7 = var10000;
                              return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$run);
                           }

                           var10000.join();
                           var10000 = this.walletPut(var2, true, true);
                           if (!var10000.isDone()) {
                              var7 = var10000;
                              return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$run);
                           }

                           var11 = (Boolean)var10000.join();
                        } else {
                           var10000 = this.walletPut(var2, false, false);
                           if (!var10000.isDone()) {
                              var7 = var10000;
                              return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$run);
                           }

                           var11 = (Boolean)var10000.join();
                           var10000 = this.walletsSubmitShipping(var2);
                           if (!var10000.isDone()) {
                              var7 = var10000;
                              return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$run);
                           }

                           var10000.join();
                        }
                     } else {
                        var10000 = this.atcAJAX(this.instanceSignal, false);
                        if (!var10000.isDone()) {
                           var7 = var10000;
                           return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$run);
                        }

                        var10000.join();
                        if (ThreadLocalRandom.current().nextBoolean()) {
                           var10000 = this.genCheckoutURL(ThreadLocalRandom.current().nextBoolean());
                           if (!var10000.isDone()) {
                              var7 = var10000;
                              return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$run);
                           }

                           var10000.join();
                        } else {
                           var10000 = this.genCheckoutURLViaCart();
                           if (!var10000.isDone()) {
                              var7 = var10000;
                              return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$run);
                           }

                           var10000.join();
                        }

                        Analytics.carts.increment();
                        var10000 = this.walletsSubmitShipping(var2);
                        if (!var10000.isDone()) {
                           var7 = var10000;
                           return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$run);
                        }

                        var10000.join();
                     }
                  } else {
                     var10000 = this.walletPut(var2, true, false);
                     if (!var10000.isDone()) {
                        var7 = var10000;
                        return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$run);
                     }

                     var11 = (Boolean)var10000.join();
                     Analytics.carts.increment();
                  }

                  if (var11 == 0) {
                     var10000 = this.getShippingPage(var2);
                     if (!var10000.isDone()) {
                        var7 = var10000;
                        return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$run);
                     }

                     var10000.join();
                     var10000 = this.submitShipping(var2);
                     if (!var10000.isDone()) {
                        var7 = var10000;
                        return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$run);
                     }

                     var10000.join();
                     var10000 = this.getProcessingPage(var2, false);
                     if (!var10000.isDone()) {
                        var7 = var10000;
                        return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$run);
                     }

                     var10000.join();
                  } else {
                     if (!var3.isDone()) {
                        return var3.exceptionally(Function.identity()).thenCompose(Shopify::async$run);
                     }

                     var3.join();
                  }

                  String var12;
                  do {
                     var10000 = this.processPayment(var2);
                     if (!var10000.isDone()) {
                        var7 = var10000;
                        return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$run);
                     }

                     var10000.join();
                     var10000 = this.checkOrderAPI(var2);
                     if (!var10000.isDone()) {
                        var7 = var10000;
                        return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$run);
                     }

                     var5 = (JsonObject)var10000.join();
                     var12 = parseDecline(var5);
                     if (var12 != null && !var12.equals("success")) {
                        Analytics.failure(var12, this.task, var5, this.api.proxyStringSafe());
                        this.logger.info("Checkout fail -> " + var12);
                     }
                  } while(var12 != null && !var12.equals("success"));

                  this.logger.info("Successfully checked out.");
                  Analytics.success(this.task, var5, this.api.proxyStringSafe());
                  break;
               } catch (Throwable var8) {
                  this.logger.error("Error handling creation: {}", var8.getMessage());
                  this.setAttributes();
               }
            }
         } catch (Exception var9) {
            this.logger.error("Task interrupted: " + var9.getMessage());
         }

         return null;
      }
   }

   public static CompletableFuture async$send(Shopify param0, HttpRequest param1, Object param2, int param3, CompletableFuture param4, int param5, Object param6) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$visitHomepage(Shopify param0, int param1, HttpRequest param2, CompletableFuture param3, Throwable param4, int param5, Object param6) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture waitTilCartCookie() {
      this.logger.info("Setting up cart...");
      int var1 = 0;

      while(super.running && var1++ < Integer.MAX_VALUE) {
         HttpRequest var2 = this.api.updateJS();
         CompletableFuture var5 = this.send(var2, Buffer.buffer("{}"));
         CompletableFuture var4;
         if (!var5.isDone()) {
            var4 = var5;
            return var4.exceptionally(Function.identity()).thenCompose(Shopify::async$waitTilCartCookie);
         }

         HttpResponse var3 = (HttpResponse)var5.join();
         if (this.api.getCookies().contains("cart")) {
            return CompletableFuture.completedFuture(var3.statusCode() == 401);
         }

         this.logger.info("Unable to properly setup cart {}", var3.statusCode());
         var5 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
         if (!var5.isDone()) {
            var4 = var5;
            return var4.exceptionally(Function.identity()).thenCompose(Shopify::async$waitTilCartCookie);
         }

         var5.join();
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture getProcessingPage(String var1, boolean var2) {
      if (this.api.isFirstProcessingPageVisit) {
         this.api.setInstock();
      }

      if (var2 == 0) {
         this.logger.info("Checking billing...");
      }

      int var3 = 0;

      while(super.running && var3++ < Integer.MAX_VALUE) {
         CompletableFuture var9;
         CompletableFuture var13;
         try {
            HttpRequest var4 = this.api.paymentPage(var1);
            var13 = this.safeCompleteRequest(var4, "checking billing");
            if (!var13.isDone()) {
               var9 = var13;
               return var9.exceptionally(Function.identity()).thenCompose(Shopify::async$getProcessingPage);
            }

            HttpResponse var5 = (HttpResponse)var13.join();
            if (var5 != null) {
               if (var5.statusCode() == 200) {
                  boolean var11 = this.api.checkIsOOS();
                  boolean var12 = this.api.markProcessingPageAsVisited();
                  String var8 = var5.bodyAsString();
                  if (this.paymentGateway == null) {
                     this.updateGateway(var8);
                  }

                  this.updateCheckoutParams(var8);
                  if (this.updatePrice(var8) || var11 || !this.api.getCookies().contains("_shopify_checkpoint")) {
                     return CompletableFuture.completedFuture(var8);
                  }

                  this.logger.info("Calculating taxes...");
               } else if (var5.statusCode() == 302 || var5.statusCode() == 301) {
                  int var6 = this.isEarly && !this.api.getCookies().contains("_shopify_checkpoint") ? 1 : 0;
                  var13 = this.handleRedirect((HttpResponse)var5, (boolean)var6);
                  if (!var13.isDone()) {
                     var9 = var13;
                     return var9.exceptionally(Function.identity()).thenCompose(Shopify::async$getProcessingPage);
                  }

                  Boolean var7 = (Boolean)var13.join();
                  if (var6 != 0 && var7 == null) {
                     var7 = false;
                  }

                  if (var7 == null) {
                     return CompletableFuture.completedFuture((Object)null);
                  }

                  if (var7) {
                     this.logger.info("Unknown redirect. Handling..");
                     var13 = VertxUtil.randomSleep((long)this.task.getMonitorDelay());
                     if (!var13.isDone()) {
                        var9 = var13;
                        return var9.exceptionally(Function.identity()).thenCompose(Shopify::async$getProcessingPage);
                     }

                     var13.join();
                  } else if (var6 != 0 && !this.cpMonitor.isDone()) {
                     if (!this.isShippingSubmittedWithWallets) {
                        var13 = this.walletsSubmitShipping(var1);
                        if (!var13.isDone()) {
                           var9 = var13;
                           return var9.exceptionally(Function.identity()).thenCompose(Shopify::async$getProcessingPage);
                        }

                        var13.join();
                     }

                     this.logger.info("Waiting for release [early]");
                     var13 = CompletableFuture.anyOf(VertxUtil.randomSignalSleep("waitForCPPreload", (long)this.task.getMonitorDelay()), this.cpMonitor);
                     if (!var13.isDone()) {
                        var9 = var13;
                        return var9.exceptionally(Function.identity()).thenCompose(Shopify::async$getProcessingPage);
                     }

                     var13.join();
                     if (this.isCPMonitorTriggered.compareAndSet(true, false) && !this.cpMonitor.isDone()) {
                        this.logger.info("Checkpoint detected. Waiting for presolve [EARLY-PRE]");
                        var13 = this.cpMonitor;
                        if (!var13.isDone()) {
                           var9 = var13;
                           return var9.exceptionally(Function.identity()).thenCompose(Shopify::async$getProcessingPage);
                        }

                        var13.join();
                     }

                     this.api.setInstock();
                  }
               }
            }
         } catch (Throwable var10) {
            this.logger.error("Error checking billing: {}", var10.getMessage());
            var13 = VertxUtil.randomSleep(3000L);
            if (!var13.isDone()) {
               var9 = var13;
               return var9.exceptionally(Function.identity()).thenCompose(Shopify::async$getProcessingPage);
            }

            var13.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$handleQueueOld(Shopify param0, int param1, HttpRequest param2, CompletableFuture param3, HttpResponse param4, Throwable param5, int param6, Object param7) {
      // $FF: Couldn't be decompiled
   }

   public MultipartForm changeForm() {
      MultipartForm var1 = MultipartForm.create();
      var1.attribute("quantity", "0");
      var1.attribute("line", "1");
      return var1;
   }

   public CompletableFuture genAcc() {
      Account var1 = rotateAccount();
      CompletableFuture var10000 = initHarvesters();
      CompletableFuture var6;
      if (!var10000.isDone()) {
         var6 = var10000;
         return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$genAcc);
      } else {
         var10000.join();
         MultiMap var2 = MultiMap.caseInsensitiveMultiMap();
         var2.set("form_type", "create_customer");
         var2.set("utf8", "✓");
         var2.set("customer[first_name]", this.task.getProfile().getFirstName());
         var2.set("customer[last_name]", this.task.getProfile().getLastName());
         String var10002 = var1.getUser();
         ThreadLocalRandom var10004 = ThreadLocalRandom.current();
         var2.set("customer[email]", var10002.replace("@", "+" + var10004.nextInt(32767) + "@"));
         var2.set("customer[password]", var1.getPass());
         this.logger.info("Creating account");
         int var3 = 0;

         while(super.running && var3++ < Integer.MAX_VALUE) {
            CompletableFuture var11 = TokenController.solveBasicCaptcha("https://" + this.api.getSiteURL() + "/account/register");
            if (!var11.isDone()) {
               CompletableFuture var8 = var11;
               String var7 = "recaptcha-v3-token";
               return var8.exceptionally(Function.identity()).thenCompose(Shopify::async$genAcc);
            }

            var2.set("recaptcha-v3-token", ((CaptchaToken)var11.join()).getToken());

            try {
               HttpRequest var4 = this.api.genAcc();
               var10000 = Request.send(var4, var2);
               if (!var10000.isDone()) {
                  var6 = var10000;
                  return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$genAcc);
               }

               HttpResponse var5 = (HttpResponse)var10000.join();
               if (var5 != null) {
                  if (var5.statusCode() == 302 || var5.statusCode() == 301) {
                     var10000 = this.handleRedirect(var5, false);
                     if (!var10000.isDone()) {
                        var6 = var10000;
                        return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$genAcc);
                     }

                     if ((Boolean)var10000.join()) {
                        return CompletableFuture.completedFuture((Object)null);
                     }
                  }

                  var10000 = VertxUtil.sleep((long)this.task.getRetryDelay());
                  if (!var10000.isDone()) {
                     var6 = var10000;
                     return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$genAcc);
                  }

                  var10000.join();
               }
            } catch (Throwable var9) {
               this.logger.error("Error genning account: {}", var9.getMessage());
               var10000 = super.randomSleep(3000);
               if (!var10000.isDone()) {
                  var6 = var10000;
                  return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$genAcc);
               }

               var10000.join();
            }
         }

         return CompletableFuture.completedFuture((Object)null);
      }
   }

   public static CompletableFuture async$submitPassword(Shopify param0, int param1, HttpRequest param2, CompletableFuture param3, HttpResponse param4, int param5, Exception param6, int param7, Object param8) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$checkOrderAPI(Shopify param0, String param1, CompletableFuture param2, int param3, HttpRequest param4, HttpResponse param5, Throwable param6, int param7, Object param8) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$waitTilCartCookie(Shopify var0, int var1, HttpRequest var2, CompletableFuture var3, HttpResponse var4, int var5, Object var6) {
      CompletableFuture var10000;
      HttpResponse var7;
      CompletableFuture var8;
      switch (var5) {
         case 0:
            var0.logger.info("Setting up cart...");
            var1 = 0;
            break;
         case 1:
            var7 = (HttpResponse)var3.join();
            if (var0.api.getCookies().contains("cart")) {
               return CompletableFuture.completedFuture(var7.statusCode() == 401);
            }

            var0.logger.info("Unable to properly setup cart {}", var7.statusCode());
            var10000 = VertxUtil.randomSleep((long)var0.task.getRetryDelay());
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(Shopify::async$waitTilCartCookie);
            }

            var10000.join();
            break;
         case 2:
            var3.join();
            break;
         default:
            throw new IllegalArgumentException();
      }

      while(var0.running && var1++ < Integer.MAX_VALUE) {
         var2 = var0.api.updateJS();
         var10000 = var0.send(var2, Buffer.buffer("{}"));
         if (!var10000.isDone()) {
            var8 = var10000;
            return var8.exceptionally(Function.identity()).thenCompose(Shopify::async$waitTilCartCookie);
         }

         var7 = (HttpResponse)var10000.join();
         if (var0.api.getCookies().contains("cart")) {
            return CompletableFuture.completedFuture(var7.statusCode() == 401);
         }

         var0.logger.info("Unable to properly setup cart {}", var7.statusCode());
         var10000 = VertxUtil.randomSleep((long)var0.task.getRetryDelay());
         if (!var10000.isDone()) {
            var8 = var10000;
            return var8.exceptionally(Function.identity()).thenCompose(Shopify::async$waitTilCartCookie);
         }

         var10000.join();
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture handleOOS(String var1) {
      int var2 = 0;

      while(super.running && var2++ < Integer.MAX_VALUE) {
         CompletableFuture var6;
         CompletableFuture var9;
         try {
            HttpRequest var3 = this.api.oosPage(var1);
            var9 = Request.send(var3);
            if (!var9.isDone()) {
               var6 = var9;
               return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$handleOOS);
            }

            HttpResponse var4 = (HttpResponse)var9.join();
            if (var4 != null) {
               if (var4.statusCode() == 200) {
                  String var8 = var4.bodyAsString();
                  if (this.task.getSite() != Site.KITH) {
                     this.updateGateway(var8);
                  }

                  this.updateShippingRate(var8);
                  this.updateCheckoutParams(var8);
                  return CompletableFuture.completedFuture(var8);
               }

               if (var4.statusCode() != 302 && var4.statusCode() != 301) {
                  this.logger.warn("Error handling: status: '{}'", var4.statusCode());
                  var9 = VertxUtil.sleep((long)this.task.getRetryDelay());
                  if (!var9.isDone()) {
                     var6 = var9;
                     return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$handleOOS);
                  }

                  var9.join();
               } else {
                  var9 = this.handleRedirect(var4, true);
                  if (!var9.isDone()) {
                     var6 = var9;
                     return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$handleOOS);
                  }

                  Boolean var5 = (Boolean)var9.join();
                  if (var5 == null) {
                     return CompletableFuture.completedFuture((Object)null);
                  }

                  if (var5) {
                     return CompletableFuture.completedFuture((Object)null);
                  }
               }
            }
         } catch (Throwable var7) {
            this.logger.error("Error handling: {}", var7.getMessage());
            var9 = super.randomSleep(3000);
            if (!var9.isDone()) {
               var6 = var9;
               return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$handleOOS);
            }

            var9.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture shippingAndBillingPut(String var1) {
      this.logger.info("Submitting contact [API]...");
      int var2 = 0;

      while(super.running && var2++ < Integer.MAX_VALUE) {
         CompletableFuture var5;
         CompletableFuture var7;
         try {
            HttpRequest var3 = this.api.walletsPutDetails(var1);
            var7 = Request.send(var3, this.contactAPIForm().toBuffer());
            if (!var7.isDone()) {
               var5 = var7;
               return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$shippingAndBillingPut);
            }

            HttpResponse var4 = (HttpResponse)var7.join();
            if (var4 != null) {
               if (var4.statusCode() == 200) {
                  return CompletableFuture.completedFuture((Buffer)var4.body());
               }

               if (var4.statusCode() == 202) {
                  if (var4.bodyAsString().contains(this.task.getProfile().getAddress1())) {
                     return CompletableFuture.completedFuture((Buffer)var4.body());
                  }
               } else if (var4.statusCode() != 409 || var2 >= 5) {
                  this.logger.warn("Retrying submitting contact [API]: status: '{}'", var4.statusCode());
                  var7 = VertxUtil.sleep((long)this.task.getRetryDelay());
                  if (!var7.isDone()) {
                     var5 = var7;
                     return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$shippingAndBillingPut);
                  }

                  var7.join();
               }
            }
         } catch (Throwable var6) {
            this.logger.error("Error submitting contact [API]: {}", var6.getMessage());
            var7 = super.randomSleep(3000);
            if (!var7.isDone()) {
               var5 = var7;
               return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$shippingAndBillingPut);
            }

            var7.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture genPaymentToken() {
      this.paymentToken = new CompletableFuture();
      if (this.isCod) {
         this.paymentToken.complete(new PaymentTokenSupplier(""));
         return CompletableFuture.completedFuture((Object)null);
      } else {
         JsonObject var1 = new JsonObject();
         JsonObject var10002 = (new JsonObject()).put("number", this.task.getProfile().splitCard());
         String var10004 = Task.randomizeCase(this.task.getProfile().getFirstName());
         var1.put("credit_card", var10002.put("name", var10004 + " " + Task.randomizeCase(this.task.getProfile().getLastName())).put("month", Integer.parseInt(this.task.getProfile().getExpiryMonth())).put("year", Integer.parseInt(this.task.getProfile().getExpiryYear())).put("verification_value", this.task.getProfile().getCvv()));
         var1.put("payment_session_scope", this.api.getSiteURL());
         int var2 = 0;

         while(super.running && var2++ < Integer.MAX_VALUE) {
            WebClient var3 = VertxSingleton.INSTANCE.getProxyPoolClient().getClient();

            CompletableFuture var6;
            CompletableFuture var8;
            try {
               HttpRequest var4 = this.api.paymentToken(var3).timeout((long)ThreadLocalRandom.current().nextInt(3500, 4250));
               var8 = Request.send(var4, var1.toBuffer());
               if (!var8.isDone()) {
                  var6 = var8;
                  return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$genPaymentToken);
               }

               HttpResponse var5 = (HttpResponse)var8.join();
               if (var5 != null) {
                  if (var5.statusCode() == 200) {
                     this.paymentToken.complete(new PaymentTokenSupplier(var5.bodyAsJsonObject().getString("id")));
                     return CompletableFuture.completedFuture((Object)null);
                  }

                  var8 = VertxUtil.hardCodedSleep(100L);
                  if (!var8.isDone()) {
                     var6 = var8;
                     return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$genPaymentToken);
                  }

                  var8.join();
               }
            } catch (Throwable var7) {
               this.logger.error("Failed generating payment token: {}", var7.getMessage().contains("timeout period of") ? "Timeout" : var7.getMessage());
               VertxSingleton.INSTANCE.getProxyPoolClient().removeBad(var3);
               var8 = VertxUtil.hardCodedSleep(100L);
               if (!var8.isDone()) {
                  var6 = var8;
                  return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$genPaymentToken);
               }

               var8.join();
            }
         }

         return CompletableFuture.completedFuture((Object)null);
      }
   }

   public CompletableFuture getCPHtml(boolean var1) {
      this.logger.info("Visiting checkpoint");
      int var2 = 0;

      while(var2++ < Integer.MAX_VALUE && this.api.getWebClient().isActive()) {
         CompletableFuture var5;
         CompletableFuture var7;
         try {
            HttpRequest var3 = this.api.checkpointPage((boolean)var1);
            var7 = Request.send(var3);
            if (!var7.isDone()) {
               var5 = var7;
               return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$getCPHtml);
            }

            HttpResponse var4 = (HttpResponse)var7.join();
            if (var4 != null && var4.statusCode() == 200) {
               return CompletableFuture.completedFuture(var4.bodyAsString());
            }
         } catch (Throwable var6) {
            if (!var6.getMessage().contains("Connection was closed")) {
               this.logger.error("Failed hitting CP: {}", var6.getMessage());
            }

            var7 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
            if (!var7.isDone()) {
               var5 = var7;
               return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$getCPHtml);
            }

            var7.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$genCheckoutURLIgnorePassword(Shopify param0, int param1, int param2, HttpRequest param3, CompletableFuture param4, HttpResponse param5, Throwable param6, int param7, Object param8) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture aiAPIReq(HttpRequest var1, JsonObject var2) {
      int var3 = 0;

      do {
         CompletableFuture var5;
         CompletableFuture var7;
         try {
            var7 = Request.send(var1, var2);
            if (!var7.isDone()) {
               var5 = var7;
               return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$aiAPIReq);
            }

            HttpResponse var4 = (HttpResponse)var7.join();
            if (var4 != null && var4.statusCode() == 200) {
               return CompletableFuture.completedFuture(var4);
            }
         } catch (Exception var6) {
            PrintStream var10000 = System.out;
            String var10001 = var6.getMessage();
            var10000.println("AI req poll err " + var10001.replace("34.66.51.234", "remote"));
         }

         var7 = VertxUtil.sleep(250L);
         if (!var7.isDone()) {
            var5 = var7;
            return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$aiAPIReq);
         }

         var7.join();
         ++var3;
      } while(var3 < Integer.MAX_VALUE);

      return CompletableFuture.completedFuture((Object)null);
   }

   public void updateGateway(String var1) {
      Matcher var2 = GATEWAY_CARD_PATTERN.matcher(var1);
      if (var2.find()) {
         this.paymentGateway = var2.group(1);
      } else {
         this.logger.debug("No gateway found");
      }

   }

   public static CompletableFuture async$processingAPIForm(Shopify var0, JsonObject var1, JsonObject var2, String var3, CompletableFuture var4, JsonObject var5, String var6, JsonObject var7, String var8, int var9, Object var10) {
      JsonObject var10000;
      String var10001;
      JsonObject var10002;
      String var10003;
      JsonObject var10004;
      String var10005;
      CompletableFuture var10006;
      label30: {
         CompletableFuture var16;
         switch (var9) {
            case 0:
               var1 = new JsonObject();
               var1.put("complete", "1");
               var1.put("field_type", "hidden");
               var10000 = var1;
               var10001 = "s";
               var16 = var0.paymentToken;
               if (!var16.isDone()) {
                  var4 = var16;
                  var3 = "s";
                  return var4.exceptionally(Function.identity()).thenCompose(Shopify::async$processingAPIForm);
               }
               break;
            case 1:
               var10000 = var2;
               var10001 = var3;
               var16 = var4;
               break;
            case 2:
               var10000 = var2;
               var10001 = var3;
               var10002 = var5;
               var10003 = var6;
               var10004 = var7;
               var10005 = var8;
               var10006 = var4;
               break label30;
            default:
               throw new IllegalArgumentException();
         }

         var10000.put(var10001, ((PaymentTokenSupplier)var16.join()).get());
         if (var0.hasShippingAlreadySubmittedAPI) {
            return CompletableFuture.completedFuture(var1);
         }

         var10000 = var1;
         var10001 = "checkout";
         var10002 = new JsonObject();
         var10003 = "shipping_rate";
         var10004 = new JsonObject();
         var10005 = "id";
         var10006 = var0.shippingRate;
         if (!var10006.isDone()) {
            CompletableFuture var15 = var10006;
            String var14 = "id";
            JsonObject var13 = var10004;
            String var12 = "shipping_rate";
            JsonObject var11 = var10002;
            var3 = "checkout";
            return var15.exceptionally(Function.identity()).thenCompose(Shopify::async$processingAPIForm);
         }
      }

      var10000.put(var10001, var10002.put(var10003, var10004.put(var10005, ((ShippingRateSupplier)var10006.join()).get())));
      return CompletableFuture.completedFuture(var1);
   }

   public static CompletableFuture async$getShippingRateAPI(Shopify param0, String param1, int param2, HttpRequest param3, CompletableFuture param4, HttpResponse param5, JsonObject param6, JsonArray param7, Exception param8, int param9, Object param10) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$getCheckoutURL(Shopify param0, String param1, int param2, HttpRequest param3, CompletableFuture param4, HttpResponse param5, Boolean param6, Exception param7, int param8, Object param9) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$checkQueueThrottle(Shopify param0, int param1, HttpRequest param2, CompletableFuture param3, HttpResponse param4, Throwable param5, int param6, Object param7) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$processPaymentFakeToken(Shopify param0, String param1, int param2, CompletableFuture param3, MultiMap param4, HttpRequest param5, HttpResponse param6, Exception param7, int param8, Object param9) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture parseNewQueueJson(JsonObject var1) {
      String var2 = var1.getJsonObject("data").getJsonObject("poll").getString("__typename");
      String var3 = var1.getJsonObject("data").getJsonObject("poll").getString("token");
      this.api.getWebClient().cookieStore().removeAnyMatch("_checkout_queue_token");
      this.api.getWebClient().cookieStore().put("_checkout_queue_token", var3, this.api.getSiteURL());
      CompletableFuture var10000;
      CompletableFuture var8;
      if (var2.equals("PollContinue")) {
         long var4 = var1.getJsonObject("data").getJsonObject("poll").getLong("queueEtaSeconds");
         String var6 = Instant.ofEpochSecond(Instant.now().getEpochSecond() + var4).atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("HH:mm:ss"));
         String var7 = var1.getJsonObject("data").getJsonObject("poll").getString("pollAfter");
         this.logger.info("Waiting in new queue. Expected wait time -> ({}) [{}][{}]", var4, var6, true);
         if (this.prevTime == var4) {
            var10000 = VertxUtil.hardCodedSleep(this.calculatePollTime(var7) + 500L);
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(Shopify::async$parseNewQueueJson);
            }

            var10000.join();
         } else {
            this.prevTime = var4;
            var10000 = VertxUtil.hardCodedSleep(this.calculatePollTime(var7));
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(Shopify::async$parseNewQueueJson);
            }

            var10000.join();
         }

         var10000 = VertxUtil.hardCodedSleep(1250L);
         if (!var10000.isDone()) {
            var8 = var10000;
            return var8.exceptionally(Function.identity()).thenCompose(Shopify::async$parseNewQueueJson);
         } else {
            var10000.join();
            return CompletableFuture.completedFuture(false);
         }
      } else if (var2.equals("PollComplete")) {
         this.logger.info("Passed new queue.");
         Analytics.queuePasses.increment();
         return CompletableFuture.completedFuture(true);
      } else {
         this.logger.info("Error with new queue. Please wait.");
         var10000 = VertxUtil.randomSleep(3000L);
         if (!var10000.isDone()) {
            var8 = var10000;
            return var8.exceptionally(Function.identity()).thenCompose(Shopify::async$parseNewQueueJson);
         } else {
            var10000.join();
            return CompletableFuture.completedFuture(false);
         }
      }
   }

   public boolean hasTrulyCarted(String var1) {
      String var2 = this.api.getCookies().getCookieValue("cart_sig");
      if (var2 == null) {
         return false;
      } else if (var1 == null) {
         return true;
      } else {
         return !var1.equals(var2);
      }
   }

   public static CompletableFuture async$confirmClear(Shopify param0, String param1, int param2, HttpRequest param3, CompletableFuture param4, HttpResponse param5, REDIRECT_STATUS param6, Exception param7, int param8, Object param9) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$submitShipping(Shopify param0, String param1, int param2, int param3, CompletableFuture param4, MultiMap param5, HttpRequest param6, HttpResponse param7, String param8, Throwable param9, int param10, Object param11) {
      // $FF: Couldn't be decompiled
   }

   public void setProductPickupTime() {
      if (this.productPickupTime == null) {
         this.productPickupTime = System.currentTimeMillis();
      }

   }

   public static CompletableFuture async$processFakeAsPatch(Shopify param0, String param1, int param2, CompletableFuture param3, MultiMap param4, HttpRequest param5, HttpResponse param6, Exception param7, int param8, Object param9) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture checkOrderAPI(String var1) {
      this.logger.info("Checking order [API]");
      CompletableFuture var10000 = VertxUtil.randomSleep(3000L);
      CompletableFuture var6;
      if (!var10000.isDone()) {
         var6 = var10000;
         return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$checkOrderAPI);
      } else {
         var10000.join();
         int var2 = 0;

         while(super.running && var2++ < Integer.MAX_VALUE) {
            try {
               HttpRequest var3 = this.api.paymentStatusAPI(var1);
               var10000 = Request.send(var3);
               if (!var10000.isDone()) {
                  var6 = var10000;
                  return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$checkOrderAPI);
               }

               HttpResponse var4 = (HttpResponse)var10000.join();
               if (var4 != null) {
                  JsonArray var5;
                  if (var4.statusCode() == 200 && ((var5 = var4.bodyAsJsonObject().getJsonArray("payments")).getJsonObject(var5.size() - 1).getString("payment_processing_error_message") != null || var5.getJsonObject(var5.size() - 1).getValue("transaction") != null && var5.getJsonObject(var5.size() - 1).getJsonObject("transaction").getValue("message") != null)) {
                     return CompletableFuture.completedFuture(var4.bodyAsJsonObject());
                  }

                  this.logger.warn("Waiting to check order [API]: status: '{}'", var4.statusCode());
                  var10000 = VertxUtil.hardCodedSleep(5000L);
                  if (!var10000.isDone()) {
                     var6 = var10000;
                     return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$checkOrderAPI);
                  }

                  var10000.join();
               }
            } catch (Throwable var7) {
               this.logger.error("Error checking order: {}", var7.getMessage());
               var10000 = VertxUtil.randomSleep(3000L);
               if (!var10000.isDone()) {
                  var6 = var10000;
                  return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$checkOrderAPI);
               }

               var10000.join();
            }
         }

         return CompletableFuture.completedFuture((Object)null);
      }
   }

   public CompletableFuture checkNewQueueThrottle() {
      this.logger.info("Checking queue [NEW]...");
      int var1 = 0;

      while(super.running && var1++ < Integer.MAX_VALUE) {
         CompletableFuture var4;
         CompletableFuture var6;
         try {
            HttpRequest var2 = this.api.fetchNewQueueUrl();
            var6 = Request.send(var2);
            if (!var6.isDone()) {
               var4 = var6;
               return var4.exceptionally(Function.identity()).thenCompose(Shopify::async$checkNewQueueThrottle);
            }

            HttpResponse var3 = (HttpResponse)var6.join();
            if (var3 != null) {
               if (var3.statusCode() == 200) {
                  return CompletableFuture.completedFuture(true);
               }

               if ((var3.statusCode() == 302 || var3.statusCode() == 301) && var3.getHeader("location").contains("/checkout")) {
                  return CompletableFuture.completedFuture(false);
               }

               if (var3.statusCode() == 302) {
                  this.logger.info(var3.getHeader("location"));
               }

               this.logger.warn("Waiting to check queue [NEW]: status: '{}'", var3.statusCode());
               var6 = VertxUtil.hardCodedSleep(3000L);
               if (!var6.isDone()) {
                  var4 = var6;
                  return var4.exceptionally(Function.identity()).thenCompose(Shopify::async$checkNewQueueThrottle);
               }

               var6.join();
            }
         } catch (Throwable var5) {
            this.logger.error("Error checking queue: {}", var5.getMessage());
            var6 = super.randomSleep(3000);
            if (!var6.isDone()) {
               var4 = var6;
               return var4.exceptionally(Function.identity()).thenCompose(Shopify::async$checkNewQueueThrottle);
            }

            var6.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture smoothCart() {
      CompletableFuture var10000;
      CompletableFuture var1;
      if (this.propertiesPair != null) {
         var10000 = this.atcUpsell(this.instanceSignal);
         if (!var10000.isDone()) {
            var1 = var10000;
            return var1.exceptionally(Function.identity()).thenCompose(Shopify::async$smoothCart);
         }

         var10000.join();
      } else {
         var10000 = this.atcAJAX(this.instanceSignal, false);
         if (!var10000.isDone()) {
            var1 = var10000;
            return var1.exceptionally(Function.identity()).thenCompose(Shopify::async$smoothCart);
         }

         var10000.join();
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public Shopify(Task var1, int var2) {
      super(var2);
      this.task = var1;
      super.task = var1;
      this.api = new ShopifyAPI(this.task);
      super.setClient(this.api);
      this.instanceSignal = this.task.getKeywords()[0];
      Matcher var3 = CHECK_VARIANT_PATTERN.matcher(this.instanceSignal);
      Matcher var4 = CHECK_EL_PATTERN.matcher(this.instanceSignal);
      if (var3.find()) {
         this.logger.warn("Using variant -> " + this.instanceSignal);
      } else if (var4.find()) {
         this.isEL = true;
         this.task.getKeywords()[0] = (this.instanceSignal.contains("?") ? this.instanceSignal.split("\\?")[0] : this.instanceSignal).toLowerCase();
         this.task.getKeywords()[0] = this.task.getKeywords()[0].replaceAll("collections/.*?/", "");
         this.instanceSignal = this.task.getKeywords()[0];
         this.logger.warn("Using EL -> " + this.instanceSignal);
      } else {
         this.isKeyword = true;
         this.instanceSignal = Arrays.toString(this.task.getKeywords()).replace("[", "").replace("]", "").replace(",", "");
         this.logger.warn("Using keywords -> " + this.instanceSignal);
      }

      this.BROWSER_HEIGHT = String.valueOf(ThreadLocalRandom.current().nextInt(720, 1800));
      this.BROWSER_WIDTH = String.valueOf(ThreadLocalRandom.current().nextInt(1280, 3200));
      this.FLOW_ACTIONS = Flow.getFlow(this.task.getSite()).get();
      this.MODE = SiteParser.getMode(this.task.getMode());
      this.setCurrency();
      this.isCod = this.task.getMode().contains("cod");
      this.isRestockMode = this.task.getMode().contains("restock");
      this.isEarly = this.task.getMode().contains("early");
      this.isExtra = this.task.getMode().contains("extra");
      this.propertiesPair = SiteParser.getProperties(this.task.getSite());
      this.setAttributes();
      String var10001 = var1.getSiteUserEntry();
      this.lockKey = var10001 + var2 % 50;
      if (!lockMap.containsKey(this.lockKey)) {
         lockMap.put(this.lockKey, VertxSingleton.INSTANCE.get().sharedData().getLocalLockWithTimeout(this.lockKey, 2147483647L).toCompletionStage());
      }

   }

   public CompletableFuture checkCart() {
      int var1 = 0;

      while(super.running && var1++ < Integer.MAX_VALUE) {
         CompletableFuture var4;
         CompletableFuture var6;
         try {
            HttpRequest var2 = this.api.cartJS();
            var6 = Request.send(var2);
            if (!var6.isDone()) {
               var4 = var6;
               return var4.exceptionally(Function.identity()).thenCompose(Shopify::async$checkCart);
            }

            HttpResponse var3 = (HttpResponse)var6.join();
            if (var3 != null) {
               if (var3.statusCode() == 200) {
                  if (var3.bodyAsString().contains("\"item_count\":0")) {
                     return CompletableFuture.completedFuture((Object)null);
                  }

                  this.logger.error("Please notify the developer of cart bug. Thank you!");
                  var6 = VertxUtil.hardCodedSleep(2147483647L);
                  if (!var6.isDone()) {
                     var4 = var6;
                     return var4.exceptionally(Function.identity()).thenCompose(Shopify::async$checkCart);
                  }

                  var6.join();
               } else {
                  if (var3.statusCode() == 302 || var3.statusCode() == 301) {
                     var6 = this.handleRedirect(var3, false);
                     if (!var6.isDone()) {
                        var4 = var6;
                        return var4.exceptionally(Function.identity()).thenCompose(Shopify::async$checkCart);
                     }

                     var6.join();
                  }

                  this.logger.warn("Failed checking cart: status: '{}'", var3.statusCode());
                  var6 = VertxUtil.sleep((long)this.task.getRetryDelay());
                  if (!var6.isDone()) {
                     var4 = var6;
                     return var4.exceptionally(Function.identity()).thenCompose(Shopify::async$checkCart);
                  }

                  var6.join();
               }
            }
         } catch (Throwable var5) {
            this.logger.error("Failed checking cart: {}", var5.getMessage());
            var6 = super.randomSleep(3000);
            if (!var6.isDone()) {
               var4 = var6;
               return var4.exceptionally(Function.identity()).thenCompose(Shopify::async$checkCart);
            }

            var6.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture handleRedirect(HttpResponse var1, boolean var2) {
      if (var1.statusCode() == 401) {
         return this.handleRedirect("/password", var2);
      } else if (var1.statusCode() == 301) {
         this.api.SITE_URL = Utils.quickParseFirst(var1.getHeader("location"), SITE_URL_PATTERN);
         return CompletableFuture.completedFuture(false);
      } else {
         return var1.statusCode() != 302 ? CompletableFuture.completedFuture(false) : this.handleRedirect(var1.getHeader("location"), var2);
      }
   }

   public static String parseDecline(JsonObject var0) {
      if (var0.encode().contains("ending the Cash on Delivery")) {
         return "success";
      } else {
         JsonArray var1 = var0.getJsonArray("payments");
         JsonObject var2 = var1.getJsonObject(var1.size() - 1);
         JsonObject var3 = var2.getJsonObject("transaction");
         if (var3 == null) {
            String var4 = var2.getString("payment_processing_error_message");
            return var4 != null ? var4 : "Unknown Payment Failure";
         } else {
            return var3.getString("status").equals("success") ? "success" : var2.getJsonObject("transaction").getString("message");
         }
      }
   }

   public static CompletableFuture async$checkCart(Shopify param0, int param1, HttpRequest param2, CompletableFuture param3, HttpResponse param4, Throwable param5, int param6, Object param7) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$safeCompleteRequest(Shopify param0, HttpRequest param1, String param2, int param3, CompletableFuture param4, HttpResponse param5, int param6, Throwable param7, int param8, Object param9) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture handleQueueNew() {
      this.logger.info("Waiting in queue [NEW]...");
      int var1 = 0;

      while(super.running && var1++ < Integer.MAX_VALUE) {
         CompletableFuture var4;
         CompletableFuture var6;
         try {
            HttpRequest var2 = this.api.newQueue().as(BodyCodec.buffer());
            var6 = Request.send(var2, this.newQueueBody());
            if (!var6.isDone()) {
               var4 = var6;
               return var4.exceptionally(Function.identity()).thenCompose(Shopify::async$handleQueueNew);
            }

            HttpResponse var3 = (HttpResponse)var6.join();
            if (var3 != null) {
               if (var3.statusCode() == 200) {
                  var6 = this.parseNewQueueJson(var3.bodyAsJsonObject());
                  if (!var6.isDone()) {
                     var4 = var6;
                     return var4.exceptionally(Function.identity()).thenCompose(Shopify::async$handleQueueNew);
                  }

                  if ((Boolean)var6.join()) {
                     return CompletableFuture.completedFuture((Object)null);
                  }
               }

               if (var3.statusCode() != 200) {
                  this.logger.warn("Waiting in queue [NEW]: status: '{}'", var3.statusCode());
                  var6 = VertxUtil.randomSleep(3000L);
                  if (!var6.isDone()) {
                     var4 = var6;
                     return var4.exceptionally(Function.identity()).thenCompose(Shopify::async$handleQueueNew);
                  }

                  var6.join();
               }
            }
         } catch (Throwable var5) {
            this.logger.error("Error waiting in queue [NEW]: {}", var5.getMessage());
            var6 = VertxUtil.randomSleep(3000L);
            if (!var6.isDone()) {
               var4 = var6;
               return var4.exceptionally(Function.identity()).thenCompose(Shopify::async$handleQueueNew);
            }

            var6.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture getShippingRateAPI(String var1) {
      this.logger.info("Fetching rate... [API]");
      int var2 = 0;

      while(super.running && var2++ < Integer.MAX_VALUE && !this.shippingRate.isDone()) {
         CompletableFuture var7;
         CompletableFuture var9;
         try {
            HttpRequest var3 = this.api.shippingRateAPI(var1);
            var9 = Request.send(var3);
            if (!var9.isDone()) {
               var7 = var9;
               return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$getShippingRateAPI);
            }

            HttpResponse var4 = (HttpResponse)var9.join();
            if (var4 != null) {
               if (var4.statusCode() == 200) {
                  JsonObject var5 = var4.bodyAsJsonObject();
                  JsonArray var6 = var5.getJsonArray("shipping_rates");
                  if (var6.isEmpty()) {
                     throw new Exception("No shipping available. Check your shipping address / country!");
                  }

                  if (!this.shippingRate.isDone()) {
                     this.shippingRate.complete(new ShippingRateSupplier(var6.getJsonObject(0).getString("id")));
                  } else {
                     var9 = this.shippingRate;
                     if (!var9.isDone()) {
                        var7 = var9;
                        return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$getShippingRateAPI);
                     }

                     ((ShippingRateSupplier)var9.join()).updateRate(var6.getJsonObject(0).getString("id"));
                  }

                  return CompletableFuture.completedFuture(var5);
               }

               if (var4.statusCode() == 202) {
                  var9 = VertxUtil.randomSleep(100L);
                  if (!var9.isDone()) {
                     var7 = var9;
                     return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$getShippingRateAPI);
                  }

                  var9.join();
               } else {
                  this.logger.warn("Waiting to fetch rate [API]: status: '{}'", var4.statusCode());
                  var9 = VertxUtil.sleep((long)this.task.getRetryDelay());
                  if (!var9.isDone()) {
                     var7 = var9;
                     return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$getShippingRateAPI);
                  }

                  var9.join();
               }
            }
         } catch (Exception var8) {
            this.logger.error("Error fetching rate [API]: {}", var8.getMessage());
            var9 = super.randomSleep(3000);
            if (!var9.isDone()) {
               var7 = var9;
               return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$getShippingRateAPI);
            }

            var9.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public void configureShippingRate() {
      if (this.shippingRate == null || this.shippingRate.isDone()) {
         this.shippingRate = new CompletableFuture();
      }

      if (this.task.getShippingRate().length() > 3) {
         this.shippingRate.complete(new ShippingRateSupplier(this.task.getShippingRate()));
      }

   }

   public static CompletableFuture async$initShopDetails(Shopify var0, CompletableFuture var1, int var2, Object var3) {
      CompletableFuture var10000;
      switch (var2) {
         case 0:
            var10000 = var0.fetchMeta();
            if (!var10000.isDone()) {
               CompletableFuture var6 = var10000;
               return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$initShopDetails);
            }
            break;
         case 1:
            var10000 = var1;
            break;
         default:
            throw new IllegalArgumentException();
      }

      JsonObject var5 = ((JsonObject)var10000.join()).getJsonObject("paymentInstruments");
      if (var5 != null) {
         ShopifyAPI var4 = var0.api;
         JsonObject var10001 = var5.getJsonObject("checkoutConfig");
         var4.setShopID("" + var10001.getNumber("shopId"));
         var0.api.setAPIToken(var5.getString("accessToken"));
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture handleQueueOld() {
      this.logger.info("Waiting in queue [OLD]...");
      int var1 = 0;

      while(super.running && var1++ < Integer.MAX_VALUE) {
         CompletableFuture var4;
         CompletableFuture var6;
         try {
            HttpRequest var2 = this.api.oldQueue();
            var6 = Request.send(var2);
            if (!var6.isDone()) {
               var4 = var6;
               return var4.exceptionally(Function.identity()).thenCompose(Shopify::async$handleQueueOld);
            }

            HttpResponse var3 = (HttpResponse)var6.join();
            if (var3 != null) {
               if (var3.statusCode() == 200) {
                  return CompletableFuture.completedFuture((Object)null);
               }

               this.logger.warn("Waiting in queue [OLD]: status: '{}'", var3.statusCode());
               var6 = VertxUtil.hardCodedSleep(5000L);
               if (!var6.isDone()) {
                  var4 = var6;
                  return var4.exceptionally(Function.identity()).thenCompose(Shopify::async$handleQueueOld);
               }

               var6.join();
            }
         } catch (Throwable var5) {
            this.logger.error("Error waiting in queue [OLD]: {}", var5.getMessage());
            var6 = super.randomSleep(3000);
            if (!var6.isDone()) {
               var4 = var6;
               return var4.exceptionally(Function.identity()).thenCompose(Shopify::async$handleQueueOld);
            }

            var6.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture walletPut(String var1, boolean var2, Boolean var3) {
      this.logger.info("Adjusting checkout...");
      int var4 = 0;
      if (var3 == null) {
         var3 = false;
      }

      while(super.running && var4++ < Integer.MAX_VALUE) {
         CompletableFuture var7;
         CompletableFuture var10;
         try {
            HttpRequest var5 = this.api.walletsPutDetails(var1);
            CompletableFuture var10001 = this.walletsCart((boolean)var2, var3);
            if (!var10001.isDone()) {
               CompletableFuture var8 = var10001;
               return var8.exceptionally(Function.identity()).thenCompose(Shopify::async$walletPut);
            }

            var10 = Request.send(var5, ((JsonObject)var10001.join()).toBuffer());
            if (!var10.isDone()) {
               var7 = var10;
               return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$walletPut);
            }

            HttpResponse var6 = (HttpResponse)var10.join();
            if (var6 != null) {
               switch (var6.statusCode()) {
                  case 200:
                     return CompletableFuture.completedFuture(true);
                  case 202:
                     if (var6.bodyAsString().contains(this.task.getProfile().getAddress1())) {
                        return CompletableFuture.completedFuture(true);
                     }
                     break;
                  case 409:
                     if (var4 < 10) {
                        break;
                     }
                  default:
                     this.logger.warn("Retrying adjusting checkout [API]: status: '{}'", var6.statusCode());
                     var10 = VertxUtil.sleep((long)this.task.getRetryDelay());
                     if (!var10.isDone()) {
                        var7 = var10;
                        return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$walletPut);
                     }

                     var10.join();
                     break;
                  case 422:
                     if (var3) {
                        return CompletableFuture.completedFuture(false);
                     }

                     var10 = this.smoothCart();
                     if (!var10.isDone()) {
                        var7 = var10;
                        return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$walletPut);
                     }

                     var10.join();
                     var3 = true;
                     break;
                  case 429:
                     if (this.api.getCookies().contains("_checkout_queue_token")) {
                        var10 = this.handleQueueNew();
                        if (!var10.isDone()) {
                           var7 = var10;
                           return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$walletPut);
                        }

                        var10.join();
                     }
               }
            }
         } catch (Throwable var9) {
            this.logger.error("Error adjusting checkout: {}", var9.getMessage());
            var10 = super.randomSleep(3000);
            if (!var10.isDone()) {
               var7 = var10;
               return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$walletPut);
            }

            var10.join();
         }
      }

      return CompletableFuture.completedFuture(false);
   }

   public CompletableFuture checkCaptcha(String var1) {
      this.logger.info("Creating session");
      int var2 = 0;

      while(super.running && var2++ < Integer.MAX_VALUE) {
         MultiMap var3 = this.checkCaptchaForm();

         CompletableFuture var8;
         CompletableFuture var10;
         try {
            HttpRequest var4 = this.api.postContact(var1);
            var10 = Request.send(var4, var3);
            if (!var10.isDone()) {
               var8 = var10;
               return var8.exceptionally(Function.identity()).thenCompose(Shopify::async$checkCaptcha);
            }

            HttpResponse var5 = (HttpResponse)var10.join();
            if (var5 != null) {
               if (var5.statusCode() != 302 && var5.statusCode() != 301) {
                  if (var5.statusCode() == 200) {
                     this.updateCheckoutParams(var5.bodyAsString());
                     return CompletableFuture.completedFuture(var5.bodyAsString());
                  }
               } else {
                  String var6 = var5.getHeader("location");
                  var10 = this.handleRedirect(var5, true);
                  if (!var10.isDone()) {
                     var8 = var10;
                     return var8.exceptionally(Function.identity()).thenCompose(Shopify::async$checkCaptcha);
                  }

                  Boolean var7 = (Boolean)var10.join();
                  if (var7 == null) {
                     return CompletableFuture.completedFuture(var6);
                  }

                  if (!var7) {
                     continue;
                  }

                  this.logger.error("Unknown redirect! - " + var5);
               }
            }

            var10 = VertxUtil.sleep((long)this.task.getRetryDelay());
            if (!var10.isDone()) {
               var8 = var10;
               return var8.exceptionally(Function.identity()).thenCompose(Shopify::async$checkCaptcha);
            }

            var10.join();
         } catch (Throwable var9) {
            this.logger.error("Error checking checkout captcha: {}", var9.getMessage());
            var10 = super.randomSleep(3000);
            if (!var10.isDone()) {
               var8 = var10;
               return var8.exceptionally(Function.identity()).thenCompose(Shopify::async$checkCaptcha);
            }

            var10.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$atcUpsell(Shopify param0, String param1, int param2, HttpRequest param3, CompletableFuture param4, HttpResponse param5, Throwable param6, int param7, Object param8) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$checkCaptcha(Shopify param0, String param1, int param2, MultiMap param3, HttpRequest param4, CompletableFuture param5, HttpResponse param6, String param7, Throwable param8, int param9, Object param10) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture fakeProcessingForm(String var1) {
      MultiMap var2 = MultiMap.caseInsensitiveMultiMap();
      var2.add("_method", "patch");
      var2.add("authenticity_token", this.authenticity);
      var2.add("previous_step", "payment_method");
      var2.add("step", "");
      var2.add("s", "west-" + this.getFakeTokenSalt());
      Iterator var3 = this.TEXTAREA_PARAMS.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         var2.add(var4, "");
      }

      if (!this.TEXTAREA_PARAMS.isEmpty()) {
         var2.add(var1 + "-count", "" + this.TEXTAREA_PARAMS.size());
         var2.add(var1 + "-count", "fs_count");
      }

      var2.add("checkout[payment_gateway]", this.paymentGateway);
      var2.add("checkout[credit_card][vault]", "false");
      var2.add("checkout[different_billing_address]", "false");
      if (this.task.getSite() == Site.SHOPNICEKICKS) {
         var2.add("checkout[attributes][I-agree-to-the-Terms-and-Conditions]", "Yes");
      }

      if (this.task.getSite() == Site.KITH) {
         var2.add("checkout[remember_me]", "false");
      }

      if (this.task.getSite() != Site.SHOPNICEKICKS) {
         var2.add("checkout[remember_me]", "0");
      }

      var2.add("checkout[vault_phone]", this.task.getProfile().getCountry().equals("JAPAN") ? "+81" + this.task.getProfile().getPhone() : "+1" + this.task.getProfile().getPhone());
      if (this.task.getSite() == Site.SHOEPALACE) {
         var2.add("checkout[attributes][I-agree-to-the-Terms-and-Conditions]", "Yes");
      }

      ThreadLocalRandom var10002 = ThreadLocalRandom.current();
      var2.add("checkout[total_price]", var10002.nextInt(50, 300) + "00");
      var2.add("complete", "1");
      var2.add("checkout[client_details][browser_width]", this.BROWSER_WIDTH);
      var2.add("checkout[client_details][browser_height]", this.BROWSER_HEIGHT);
      var2.add("checkout[client_details][javascript_enabled]", "1");
      var2.add("checkout[client_details][color_depth]", "24");
      var2.add("checkout[client_details][java_enabled]", "false");
      var2.add("checkout[client_details][browser_tz]", "" + TIMEZONE_OFFSET);
      return CompletableFuture.completedFuture(var2);
   }

   public static CompletableFuture async$processingFormRestockMode(Shopify var0, String var1, MultiMap var2, CompletableFuture var3, int var4, Object var5) {
      CompletableFuture var10000;
      switch (var4) {
         case 0:
            var2 = MultiMap.caseInsensitiveMultiMap();
            var2.add("_method", "patch");
            var2.add("authenticity_token", var0.authenticity);
            var2.add("previous_step", "payment_method");
            var2.add("step", "");
            var10000 = var0.paymentToken;
            if (!var10000.isDone()) {
               CompletableFuture var6 = var10000;
               return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$processingFormRestockMode);
            }
            break;
         case 1:
            var10000 = var3;
            break;
         default:
            throw new IllegalArgumentException();
      }

      PaymentTokenSupplier var7 = (PaymentTokenSupplier)var10000.join();
      var2.add("s", var7.get());
      Iterator var8 = var0.TEXTAREA_PARAMS.iterator();

      while(var8.hasNext()) {
         String var9 = (String)var8.next();
         var2.add(var9, "");
      }

      if (!var0.TEXTAREA_PARAMS.isEmpty()) {
         var2.add(var1 + "-count", "" + var0.TEXTAREA_PARAMS.size());
         var2.add(var1 + "-count", "fs_count");
      }

      if (var7.isVaulted()) {
         var2.add("checkout[payment_gateway]", var0.paymentGateway);
         var2.add("checkout[credit_card][vault]", "false");
         var2.add("checkout[different_billing_address]", "false");
         if (var0.task.getSite() == Site.SHOPNICEKICKS) {
            var2.add("checkout[attributes][I-agree-to-the-Terms-and-Conditions]", "Yes");
         }

         if (var0.task.getSite() == Site.KITH) {
            var2.add("checkout[remember_me]", "false");
         }

         if (var0.task.getSite() != Site.SHOPNICEKICKS) {
            var2.add("checkout[remember_me]", "0");
         }

         var2.add("checkout[vault_phone]", var0.task.getProfile().getCountry().equals("JAPAN") ? "+81" + var0.task.getProfile().getPhone() : "+1" + var0.task.getProfile().getPhone());
         if (var0.task.getSite() == Site.SHOEPALACE) {
            var2.add("checkout[attributes][I-agree-to-the-Terms-and-Conditions]", "Yes");
         }

         if (var0.price != null) {
            var2.add("checkout[total_price]", String.valueOf(var0.price));
         }

         var2.add("complete", "1");
      }

      var2.add("checkout[client_details][browser_width]", var0.BROWSER_WIDTH);
      var2.add("checkout[client_details][browser_height]", var0.BROWSER_HEIGHT);
      var2.add("checkout[client_details][javascript_enabled]", "1");
      var2.add("checkout[client_details][color_depth]", "24");
      var2.add("checkout[client_details][java_enabled]", "false");
      var2.add("checkout[client_details][browser_tz]", "" + TIMEZONE_OFFSET);
      return CompletableFuture.completedFuture(var2);
   }

   public CompletableFuture walletsCart(boolean var1, boolean var2) {
      JsonObject var3 = new JsonObject();
      if (var2 != 0) {
         var3.put("cart_token", this.api.getCookies().getCookieValue("cart"));
      } else {
         var3.put("line_items", (new JsonArray()).add((new JsonObject()).put("variant_id", Long.parseLong(this.instanceSignal)).put("quantity", 1)));
      }

      var3.put("secret", true);
      if (var1 != 0) {
         JsonObject var10002 = new JsonObject();
         CompletableFuture var10004 = this.shippingRate;
         if (!var10004.isDone()) {
            CompletableFuture var8 = var10004;
            String var7 = "handle";
            JsonObject var6 = var10002;
            String var5 = "shipping_line";
            return var8.exceptionally(Function.identity()).thenCompose(Shopify::async$walletsCart);
         }

         var3.put("shipping_line", var10002.put("handle", ((ShippingRateSupplier)var10004.join()).get()));
      }

      return CompletableFuture.completedFuture((new JsonObject()).put("checkout", var3));
   }

   public static CompletableFuture async$submitCheckpoint(Shopify param0, CaptchaToken param1, int param2, HttpRequest param3, CompletableFuture param4, HttpResponse param5, Throwable param6, int param7, Object param8) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture submitPassword() {
      this.logger.info("Submitting password");
      int var1 = 0;

      while(super.running && var1++ < Integer.MAX_VALUE) {
         CompletableFuture var5;
         CompletableFuture var7;
         try {
            HttpRequest var2 = this.api.submitPassword();
            var7 = Request.send(var2, this.passwordForm());
            if (!var7.isDone()) {
               var5 = var7;
               return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$submitPassword);
            }

            HttpResponse var3 = (HttpResponse)var7.join();
            if (var3 != null) {
               int var4 = var3.statusCode();
               if (var4 == 302 && REDIRECT_STATUS.checkRedirectStatus(var3.getHeader("location")) == REDIRECT_STATUS.HOMEPAGE) {
                  return CompletableFuture.completedFuture((Object)null);
               }

               if (var4 == 302 && REDIRECT_STATUS.checkRedirectStatus(var3.getHeader("location")) == REDIRECT_STATUS.PASSWORD) {
                  this.logger.error("Wrong password: {}", this.task.getPassword());
                  var7 = VertxUtil.hardCodedSleep(8000L);
                  if (!var7.isDone()) {
                     var5 = var7;
                     return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$submitPassword);
                  }

                  var7.join();
                  return CompletableFuture.completedFuture((Object)null);
               }

               this.logger.warn("Failed submitting password: status: '{}'", var4);
               var7 = VertxUtil.randomSleep((long)this.task.getRetryDelay() * 2L);
               if (!var7.isDone()) {
                  var5 = var7;
                  return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$submitPassword);
               }

               var7.join();
            }
         } catch (Exception var6) {
            this.logger.error("Error submitting password: {}", var6.getMessage());
            var7 = VertxUtil.randomSleep(3000L);
            if (!var7.isDone()) {
               var5 = var7;
               return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$submitPassword);
            }

            var7.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public JsonObject contactAPIForm() {
      JsonObject var1 = new JsonObject();
      JsonObject var2 = (new JsonObject()).put("secret", true).put("field_type", "hidden").put("email", this.task.getProfile().getEmail()).put("shipping_address", (new JsonObject()).put("first_name", this.task.getProfile().getFirstName()).put("last_name", this.task.getProfile().getLastName()).put("address1", this.task.getProfile().getAddress1()).put("address2", this.task.getProfile().getAddress2()).put("city", this.task.getProfile().getCity()).put("country", this.task.getProfile().getFullCountry()).put("province", this.task.getProfile().getFullState()).put("state", this.task.getProfile().getFullState()).put("zip", this.task.getProfile().getZip()).put("phone", this.task.getProfile().getPhone())).put("billing_address", (new JsonObject()).put("first_name", this.task.getProfile().getFirstName()).put("last_name", this.task.getProfile().getLastName()).put("address1", this.task.getProfile().getAddress1()).put("address2", this.task.getProfile().getAddress2()).put("city", this.task.getProfile().getCity()).put("country", this.task.getProfile().getFullCountry()).put("province", this.task.getProfile().getFullState()).put("state", this.task.getProfile().getFullState()).put("zip", this.task.getProfile().getZip()).put("phone", this.task.getProfile().getPhone()));
      var1.put("checkout", var2);
      return var1;
   }

   public void updateShippingRate(String var1) {
      Matcher var2 = SHIPPINGRATE_PATTERN.matcher(var1);
      if (var2.find()) {
         if (!this.shippingRate.isDone()) {
            this.shippingRate.complete(new ShippingRateSupplier(var2.group(1)));
         } else {
            try {
               ((ShippingRateSupplier)this.shippingRate.get()).updateRate(var2.group(1));
            } catch (Exception var4) {
               this.logger.info("Error updating rate: {}", var4.getMessage());
            }
         }
      } else if (this.logger.isDebugEnabled()) {
         this.logger.debug("No shipping rate found");
      }

   }

   public CompletableFuture processingFormRestockMode(String var1) {
      MultiMap var2 = MultiMap.caseInsensitiveMultiMap();
      var2.add("_method", "patch");
      var2.add("authenticity_token", this.authenticity);
      var2.add("previous_step", "payment_method");
      var2.add("step", "");
      CompletableFuture var10000 = this.paymentToken;
      if (!var10000.isDone()) {
         CompletableFuture var6 = var10000;
         return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$processingFormRestockMode);
      } else {
         PaymentTokenSupplier var3 = (PaymentTokenSupplier)var10000.join();
         var2.add("s", var3.get());
         Iterator var4 = this.TEXTAREA_PARAMS.iterator();

         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            var2.add(var5, "");
         }

         if (!this.TEXTAREA_PARAMS.isEmpty()) {
            var2.add(var1 + "-count", "" + this.TEXTAREA_PARAMS.size());
            var2.add(var1 + "-count", "fs_count");
         }

         if (var3.isVaulted()) {
            var2.add("checkout[payment_gateway]", this.paymentGateway);
            var2.add("checkout[credit_card][vault]", "false");
            var2.add("checkout[different_billing_address]", "false");
            if (this.task.getSite() == Site.SHOPNICEKICKS) {
               var2.add("checkout[attributes][I-agree-to-the-Terms-and-Conditions]", "Yes");
            }

            if (this.task.getSite() == Site.KITH) {
               var2.add("checkout[remember_me]", "false");
            }

            if (this.task.getSite() != Site.SHOPNICEKICKS) {
               var2.add("checkout[remember_me]", "0");
            }

            var2.add("checkout[vault_phone]", this.task.getProfile().getCountry().equals("JAPAN") ? "+81" + this.task.getProfile().getPhone() : "+1" + this.task.getProfile().getPhone());
            if (this.task.getSite() == Site.SHOEPALACE) {
               var2.add("checkout[attributes][I-agree-to-the-Terms-and-Conditions]", "Yes");
            }

            if (this.price != null) {
               var2.add("checkout[total_price]", String.valueOf(this.price));
            }

            var2.add("complete", "1");
         }

         var2.add("checkout[client_details][browser_width]", this.BROWSER_WIDTH);
         var2.add("checkout[client_details][browser_height]", this.BROWSER_HEIGHT);
         var2.add("checkout[client_details][javascript_enabled]", "1");
         var2.add("checkout[client_details][color_depth]", "24");
         var2.add("checkout[client_details][java_enabled]", "false");
         var2.add("checkout[client_details][browser_tz]", "" + TIMEZONE_OFFSET);
         return CompletableFuture.completedFuture(var2);
      }
   }

   public CompletableFuture atcUpsell(String var1) {
      int var2 = 0;
      this.logger.info("Attempting ATC [Upsell]");

      while(super.running && var2++ < Integer.MAX_VALUE) {
         CompletableFuture var6;
         CompletableFuture var8;
         try {
            HttpRequest var3 = this.api.upsellATC();
            var8 = Request.send(var3, this.upsellForm(var1));
            if (!var8.isDone()) {
               var6 = var8;
               return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$atcUpsell);
            }

            HttpResponse var4 = (HttpResponse)var8.join();
            if (var4 != null) {
               if (var4.statusCode() == 200) {
                  JsonObject var5 = var4.bodyAsJsonObject();
                  return CompletableFuture.completedFuture(var5);
               }

               if (var4.statusCode() == 302 || var4.statusCode() == 301) {
                  var8 = this.handleRedirect(var4, false);
                  if (!var8.isDone()) {
                     var6 = var8;
                     return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$atcUpsell);
                  }

                  var8.join();
               }

               this.logger.warn("Failed attempting ATC [Upsell]: status: '{}'", var4.statusCode());
               var8 = VertxUtil.sleep((long)this.task.getRetryDelay());
               if (!var8.isDone()) {
                  var6 = var8;
                  return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$atcUpsell);
               }

               var8.join();
            }
         } catch (Throwable var7) {
            this.logger.error("Failed attempting ATC [Upsell]]: {}", var7.getMessage());
            var8 = super.randomSleep(3000);
            if (!var8.isDone()) {
               var6 = var8;
               return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$atcUpsell);
            }

            var8.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture processPaymentAPI(String var1) {
      this.logger.info("Processing [API]...");
      int var2 = 0;

      while(super.running && var2++ < Integer.MAX_VALUE) {
         CompletableFuture var7;
         CompletableFuture var12;
         try {
            HttpRequest var3 = this.api.processAPI(var1);
            CompletableFuture var10001 = this.processingAPIForm();
            if (!var10001.isDone()) {
               CompletableFuture var8 = var10001;
               return var8.exceptionally(Function.identity()).thenCompose(Shopify::async$processPaymentAPI);
            }

            var12 = Request.send(var3, ((JsonObject)var10001.join()).toBuffer());
            if (!var12.isDone()) {
               var7 = var12;
               return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$processPaymentAPI);
            }

            HttpResponse var4 = (HttpResponse)var12.join();
            if (var4 != null) {
               this.genPaymentToken();
               if (var4.statusCode() != 302 && var4.statusCode() != 301) {
                  if (var4.statusCode() == 429) {
                     String var10 = var4.bodyAsString();
                     int var11 = var10 != null && var10.contains("Too many attempts: Please try again in a few minutes") ? 1 : 0;
                     if (var11 != 0 && ++this.counter429 > 8) {
                        throw new Throwable("Checkout expired. Restarting task...");
                     }

                     if (var11 == 0) {
                        if (var2 > 4) {
                           var12 = VertxUtil.randomSleep((long)this.task.getMonitorDelay());
                           if (!var12.isDone()) {
                              var7 = var12;
                              return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$processPaymentAPI);
                           }

                           var12.join();
                        }
                        continue;
                     }
                  } else {
                     this.logger.warn("Retrying process payment [API]: status: '{}'", var4.statusCode());
                     if (this.noProcessOnFirstTry) {
                        this.noProcessOnFirstTry = false;
                        continue;
                     }
                  }
               } else {
                  var12 = this.handleRedirect(var4, true);
                  if (!var12.isDone()) {
                     var7 = var12;
                     return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$processPaymentAPI);
                  }

                  Boolean var5 = (Boolean)var12.join();
                  if (var5 != null && !var5) {
                     continue;
                  }

                  String var6 = var4.getHeader("location");
                  if (var6.contains("/stock_problem")) {
                     this.logger.warn("Retrying process payment [API]: status: '{}'", "OOS");
                  } else if (var6.contains("processing")) {
                     this.hasShippingAlreadySubmittedAPI = true;
                     return CompletableFuture.completedFuture((Buffer)var4.body());
                  }
               }

               var12 = VertxUtil.sleep((long)this.task.getMonitorDelay());
               if (!var12.isDone()) {
                  var7 = var12;
                  return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$processPaymentAPI);
               }

               var12.join();
            }
         } catch (Exception var9) {
            this.logger.error("Error processing payment [API]: {}", var9.getMessage());
            var12 = super.randomSleep(3000);
            if (!var12.isDone()) {
               var7 = var12;
               return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$processPaymentAPI);
            }

            var12.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   static {
      instanceTime = Instant.now().plus((long)ThreadLocalRandom.current().nextInt(-20, 1), ChronoUnit.DAYS);
      isHarvesterInited = new AtomicBoolean(false);
      FIELD_ERR_PATTERN = Pattern.compile("field__message--error.*?>(.*?)<");
      SITE_URL_PATTERN = Pattern.compile("://(.*?)/");
      lockMap = new ConcurrentHashMap();
      ZoneId var0 = ZoneId.systemDefault();
      String var1 = var0.getRules().getOffset(Instant.now()).toString().replace(":", "");
      TimeZone var2 = TimeZone.getDefault();
      if (var1.contains("+")) {
         TIMEZONE_OFFSET = var2.getOffset(Instant.now().toEpochMilli()) / 1000 / 60;
      } else {
         TIMEZONE_OFFSET = -var2.getOffset(Instant.now().toEpochMilli()) / 1000 / 60;
      }

      int var3 = Storage.getHarvesterCountYs();
      harvesterCFs = new CompletableFuture[var3];
      HARVESTERS = new Harvester[var3];

      for(int var4 = 0; var4 < HARVESTERS.length; ++var4) {
         HARVESTERS[var4] = new Harvester();
      }

   }

   public JsonObject ajaxJsonForm(String var1) {
      JsonObject var2 = new JsonObject();
      var2.put("quantity", 1);
      var2.put("id", var1);
      var2.put("properties", new JsonObject());
      return var2;
   }

   public CompletableFuture visitHomepage() {
      int var1 = 0;
      this.logger.info("Visiting homepage");

      while(super.running && var1++ < Integer.MAX_VALUE) {
         CompletableFuture var4;
         CompletableFuture var6;
         try {
            HttpRequest var2 = this.api.homepage();
            var6 = Request.send(var2);
            if (!var6.isDone()) {
               var4 = var6;
               return var4.exceptionally(Function.identity()).thenCompose(Shopify::async$visitHomepage);
            }

            HttpResponse var3 = (HttpResponse)var6.join();
            if (var3 != null) {
               return CompletableFuture.completedFuture(var3.bodyAsString());
            }
         } catch (Throwable var5) {
            this.logger.error("Failed visiting homepage: {}", var5.getMessage());
            var6 = super.randomSleep(3000);
            if (!var6.isDone()) {
               var4 = var6;
               return var4.exceptionally(Function.identity()).thenCompose(Shopify::async$visitHomepage);
            }

            var6.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$walletsSubmitShipping(Shopify param0, String param1, int param2, HttpRequest param3, CompletableFuture param4, HttpResponse param5, JsonObject param6, JsonObject param7, Exception param8, int param9, Object param10) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$handleQueueNew(Shopify param0, int param1, HttpRequest param2, CompletableFuture param3, HttpResponse param4, Throwable param5, int param6, Object param7) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$submitContact(Shopify param0, String param1, int param2, MultiMap param3, HttpRequest param4, CompletableFuture param5, HttpResponse param6, String param7, Throwable param8, int param9, Object param10) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$solveCheckpointCaptcha(Shopify var0, String var1, CaptchaToken var2, SolveFuture var3, SolveFuture var4, int var5, Object var6) {
      Throwable var10;
      label26: {
         SolveFuture var10000;
         boolean var10001;
         switch (var5) {
            case 0:
               try {
                  var0.logger.info("Checkpoint needs solving");
                  var2 = new CaptchaToken("https://" + var0.api.getSiteURL() + "/checkpoint?return_to=https%3A%2F%2F" + var0.api.getSiteURL() + "%2Fcheckout", true, var0.api.proxyStringFull(), var1, var0);
                  var3 = ((TokenController)Engine.get().getModule(Controller.TOKEN)).solve(var2);
                  var10000 = var3;
                  if (!var3.toCompletableFuture().isDone()) {
                     return var3.exceptionally(Function.identity()).thenCompose(Shopify::async$solveCheckpointCaptcha).toCompletableFuture();
                  }
                  break;
               } catch (Throwable var8) {
                  var10 = var8;
                  var10001 = false;
                  break label26;
               }
            case 1:
               var10000 = var4;
               break;
            default:
               throw new IllegalArgumentException();
         }

         try {
            var10000.toCompletableFuture().join();
            return CompletableFuture.completedFuture(var2);
         } catch (Throwable var7) {
            var10 = var7;
            var10001 = false;
         }
      }

      Throwable var9 = var10;
      var0.logger.error("HARVEST ERR: {}", var9.getMessage());
      return CompletableFuture.completedFuture((Object)null);
   }

   public MultiMap passwordForm() {
      MultiMap var1 = MultiMap.caseInsensitiveMultiMap();
      var1.set("form_type", "storefront_password");
      var1.set("utf8", "✓");
      var1.set("password", this.task.getPassword());
      var1.set("commit", "");
      return var1;
   }

   public static CompletableFuture async$genCheckoutURL(Shopify param0, int param1, int param2, HttpRequest param3, CompletableFuture param4, HttpResponse param5, Throwable param6, int param7, Object param8) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$getShippingRateOld(Shopify param0, int param1, HttpRequest param2, CompletableFuture param3, HttpResponse param4, Throwable param5, int param6, Object param7) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture confirmClear(String var1) {
      this.logger.info("Confirming cart...");
      int var2 = 0;

      while(super.running && var2++ < Integer.MAX_VALUE) {
         CompletableFuture var6;
         CompletableFuture var8;
         try {
            HttpRequest var3 = this.api.rawCheckoutUrl(var1);
            var8 = this.safeCompleteRequest(var3, "checking checkout session");
            if (!var8.isDone()) {
               var6 = var8;
               return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$confirmClear);
            }

            HttpResponse var4 = (HttpResponse)var8.join();
            if (var4 != null) {
               if (var4.statusCode() != 302 && var4.statusCode() != 301) {
                  this.logger.info("Unable to properly confirm cart {}", var4.statusCode());
                  var8 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
                  if (!var8.isDone()) {
                     var6 = var8;
                     return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$confirmClear);
                  }

                  var8.join();
               } else {
                  REDIRECT_STATUS var5 = REDIRECT_STATUS.checkRedirectStatus(var4.getHeader("location"));
                  if (var5 == REDIRECT_STATUS.CART) {
                     return CompletableFuture.completedFuture((Object)null);
                  }

                  if (var5 == REDIRECT_STATUS.CHECKPOINT) {
                     var8 = VertxUtil.hardCodedSleep(2147483647L);
                     if (!var8.isDone()) {
                        var6 = var8;
                        return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$confirmClear);
                     }

                     var8.join();
                  } else {
                     var8 = this.handleRedirect(var4, true);
                     if (!var8.isDone()) {
                        var6 = var8;
                        return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$confirmClear);
                     }

                     var8.join();
                     var8 = VertxUtil.sleep((long)this.task.getMonitorDelay());
                     if (!var8.isDone()) {
                        var6 = var8;
                        return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$confirmClear);
                     }

                     var8.join();
                  }
               }
            }
         } catch (Exception var7) {
            this.logger.error("Error checking checkout session: {}", var7.getMessage());
            var8 = VertxUtil.randomSleep(3000L);
            if (!var8.isDone()) {
               var6 = var8;
               return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$confirmClear);
            }

            var8.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$processPaymentAPI(Shopify param0, String param1, int param2, HttpRequest param3, HttpRequest param4, CompletableFuture param5, HttpResponse param6, String param7, int param8, Exception param9, int param10, Object param11) {
      // $FF: Couldn't be decompiled
   }

   public long calculatePollTime(String var1) {
      try {
         Instant var2 = OffsetDateTime.parse(var1, Utils.ISO_8901_US).toInstant();
         long var3 = Duration.between(var2, Instant.now()).toMillis();
         return var3 > 0L ? var3 : 0L;
      } catch (NullPointerException | DateTimeParseException var5) {
         return 0L;
      }
   }

   public static CompletableFuture async$walletsCart(Shopify var0, int var1, int var2, JsonObject var3, JsonObject var4, String var5, JsonObject var6, String var7, CompletableFuture var8, int var9, Object var10) {
      JsonObject var10000;
      String var10001;
      JsonObject var10002;
      String var10003;
      CompletableFuture var10004;
      switch (var9) {
         case 0:
            var3 = new JsonObject();
            if (var2 != 0) {
               var3.put("cart_token", var0.api.getCookies().getCookieValue("cart"));
            } else {
               var3.put("line_items", (new JsonArray()).add((new JsonObject()).put("variant_id", Long.parseLong(var0.instanceSignal)).put("quantity", 1)));
            }

            var3.put("secret", true);
            if (var1 == 0) {
               return CompletableFuture.completedFuture((new JsonObject()).put("checkout", var3));
            }

            var10000 = var3;
            var10001 = "shipping_line";
            var10002 = new JsonObject();
            var10003 = "handle";
            var10004 = var0.shippingRate;
            if (!var10004.isDone()) {
               var8 = var10004;
               var7 = "handle";
               var6 = var10002;
               var5 = "shipping_line";
               return var8.exceptionally(Function.identity()).thenCompose(Shopify::async$walletsCart);
            }
            break;
         case 1:
            var10000 = var4;
            var10001 = var5;
            var10002 = var6;
            var10003 = var7;
            var10004 = var8;
            break;
         default:
            throw new IllegalArgumentException();
      }

      var10000.put(var10001, var10002.put(var10003, ((ShippingRateSupplier)var10004.join()).get()));
      return CompletableFuture.completedFuture((new JsonObject()).put("checkout", var3));
   }

   public CompletableFuture send(HttpRequest var1, Object var2) {
      int var3 = 0;

      while(var3++ < Integer.MAX_VALUE && this.running) {
         try {
            CompletableFuture var6;
            CompletableFuture var8;
            HttpResponse var9;
            if (var2 == null) {
               var8 = Request.send(var1);
               if (!var8.isDone()) {
                  var6 = var8;
                  return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$send);
               }

               var9 = (HttpResponse)var8.join();
            } else {
               var8 = Request.send(var1, var2);
               if (!var8.isDone()) {
                  var6 = var8;
                  return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$send);
               }

               var9 = (HttpResponse)var8.join();
            }

            HttpResponse var4 = var9;
            if (var4 != null) {
               return CompletableFuture.completedFuture(var4);
            }
         } catch (Exception var7) {
            String var5 = var7.getMessage();
            if (var5.contains("timeout period")) {
               this.logger.error("Request timeout. No response");
            } else {
               this.logger.error("Network Error [{}]", var7.getMessage());
            }
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$submitContactAPI(Shopify param0, String param1, int param2, HttpRequest param3, CompletableFuture param4, HttpResponse param5, Throwable param6, int param7, Object param8) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture getShippingRateOld() {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture getCheckoutURL(String var1) {
      this.logger.info("Checking checkout session...");
      int var2 = 0;

      while(super.running && var2++ < Integer.MAX_VALUE) {
         CompletableFuture var7;
         CompletableFuture var9;
         try {
            HttpRequest var3 = this.api.contactPage(var1);
            var9 = this.safeCompleteRequest(var3, "checking checkout session");
            if (!var9.isDone()) {
               var7 = var9;
               return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$getCheckoutURL);
            }

            HttpResponse var4 = (HttpResponse)var9.join();
            if (var4 != null) {
               if (var4.statusCode() == 200) {
                  VertxUtil.sendSignal("waitForCP");
                  this.updateCheckoutParams(var4.bodyAsString());
                  if (!this.authenticity.isBlank()) {
                     return CompletableFuture.completedFuture(var4.bodyAsString());
                  }

                  this.logger.info("Checkout expired. Handling...");
                  if (var2 >= 10) {
                     throw new Throwable("Checkout expired. Restarting task...");
                  }
               } else if (var4.statusCode() == 302 || var4.statusCode() == 301) {
                  var9 = this.handleRedirect(var4, true);
                  if (!var9.isDone()) {
                     var7 = var9;
                     return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$getCheckoutURL);
                  }

                  Boolean var5 = (Boolean)var9.join();
                  if (var5 == null) {
                     if (!this.isEarly || this.api.getCookies().contains("_shopify_checkpoint")) {
                        VertxUtil.sendSignal("waitForCP");
                        var9 = this.checkCaptcha(var1);
                        if (!var9.isDone()) {
                           var7 = var9;
                           return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$getCheckoutURL);
                        }

                        String var6 = (String)var9.join();
                        return CompletableFuture.completedFuture(var6);
                     }

                     this.logger.info("Waiting for checkpoint");
                     if (this.cpMonitor != null) {
                        var9 = CompletableFuture.anyOf(VertxUtil.randomSignalSleep("waitForCP", (long)this.task.getMonitorDelay()), this.cpMonitor);
                        if (!var9.isDone()) {
                           var7 = var9;
                           return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$getCheckoutURL);
                        }

                        var9.join();
                        if (this.isCPMonitorTriggered.compareAndSet(true, false) && !this.cpMonitor.isDone()) {
                           this.logger.info("Checkpoint detected. Waiting for presolve [EARLY]");
                           var9 = this.cpMonitor;
                           if (!var9.isDone()) {
                              var7 = var9;
                              return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$getCheckoutURL);
                           }

                           var9.join();
                        }
                     } else {
                        var9 = VertxUtil.randomSignalSleep("waitForCP", (long)this.task.getMonitorDelay());
                        if (!var9.isDone()) {
                           var7 = var9;
                           return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$getCheckoutURL);
                        }

                        var9.join();
                     }

                     this.api.setInstock();
                  } else if (var5) {
                     this.logger.info("Unknown redirect. Handling..");
                     var9 = VertxUtil.randomSleep((long)this.task.getMonitorDelay());
                     if (!var9.isDone()) {
                        var7 = var9;
                        return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$getCheckoutURL);
                     }

                     var9.join();
                  }
               }
            }
         } catch (Exception var8) {
            this.logger.error("Error checking checkout session: {}", var8.getMessage());
            var9 = VertxUtil.randomSleep(3000L);
            if (!var9.isDone()) {
               var7 = var9;
               return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$getCheckoutURL);
            }

            var9.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static MultiMap checkpointForm(CaptchaToken var0) {
      JsonObject var1 = new JsonObject(var0.getToken());
      MultiMap var2 = MultiMap.caseInsensitiveMultiMap();
      Iterator var3 = var1.fieldNames().iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         var2.add(var4, var1.getString(var4));
      }

      return var2;
   }

   public static CompletableFuture async$atcAJAX(Shopify param0, String param1, int param2, String param3, int param4, HttpRequest param5, CompletableFuture param6, HttpResponse param7, Throwable param8, int param9, Object param10) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture genCheckoutURLIgnorePassword(boolean var1) {
      int var2 = 0;
      this.logger.info("Generating checkout URL...");

      while(super.running && var2++ < Integer.MAX_VALUE) {
         CompletableFuture var5;
         CompletableFuture var7;
         try {
            HttpRequest var3 = this.api.emptyCheckout((boolean)var1);
            HttpResponse var8;
            if (var1 != 0) {
               var7 = Request.send(var3, Buffer.buffer("{}"));
               if (!var7.isDone()) {
                  var5 = var7;
                  return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$genCheckoutURLIgnorePassword);
               }

               var8 = (HttpResponse)var7.join();
            } else {
               var7 = Request.send(var3);
               if (!var7.isDone()) {
                  var5 = var7;
                  return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$genCheckoutURLIgnorePassword);
               }

               var8 = (HttpResponse)var7.join();
            }

            HttpResponse var4 = var8;
            if (var4 != null) {
               if (var4.statusCode() == 302 || var4.statusCode() == 301) {
                  var7 = this.handleRedirect(var4, true);
                  if (!var7.isDone()) {
                     var5 = var7;
                     return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$genCheckoutURLIgnorePassword);
                  }

                  if ((Boolean)var7.join()) {
                     return CompletableFuture.completedFuture(var4.getHeader("location"));
                  }
               }

               if (var4.statusCode() != 302) {
                  this.logger.warn("Failed generating checkout URL: status: '{}'", var4.statusCode());
                  var7 = VertxUtil.sleep((long)this.task.getRetryDelay());
                  if (!var7.isDone()) {
                     var5 = var7;
                     return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$genCheckoutURLIgnorePassword);
                  }

                  var7.join();
               } else if (var4.getHeader("location").contains("password")) {
                  return CompletableFuture.completedFuture((Object)null);
               }
            }
         } catch (Throwable var6) {
            this.logger.error("Failed generating checkout URL: {}", var6.getMessage());
            var7 = super.randomSleep(3000);
            if (!var7.isDone()) {
               var5 = var7;
               return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$genCheckoutURLIgnorePassword);
            }

            var7.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture processFakeAsPatch(String var1) {
      this.logger.info("Adjusting for restock #2...");
      int var2 = 0;

      while(super.running && var2++ < Integer.MAX_VALUE) {
         CompletableFuture var6;
         CompletableFuture var8;
         try {
            var8 = this.fakeProcessingForm(var1);
            if (!var8.isDone()) {
               var6 = var8;
               return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$processFakeAsPatch);
            }

            MultiMap var3 = (MultiMap)var8.join();
            HttpRequest var4 = this.api.fakePatchPayment(var1);
            var8 = Request.send(var4, var3);
            if (!var8.isDone()) {
               var6 = var8;
               return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$processFakeAsPatch);
            }

            HttpResponse var5 = (HttpResponse)var8.join();
            if (var5 != null) {
               if (var5.statusCode() == 302 || var5.statusCode() == 301) {
                  var8 = this.handleRedirect(var5, true);
                  if (!var8.isDone()) {
                     var6 = var8;
                     return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$processFakeAsPatch);
                  }

                  var8.join();
                  return CompletableFuture.completedFuture((Object)null);
               }

               this.logger.warn("Error adjusting for restock #2: status: '{}'", var5.statusCode());
               var8 = VertxUtil.sleep((long)this.task.getRetryDelay());
               if (!var8.isDone()) {
                  var6 = var8;
                  return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$processFakeAsPatch);
               }

               var8.join();
            }
         } catch (Exception var7) {
            if (!var7.getMessage().equals("io.vertx.core.VertxException: Connection was closed")) {
               this.logger.error("Error adjusting for restock #2: {}", var7.getMessage());
               var8 = super.randomSleep(3000);
               if (!var8.isDone()) {
                  var6 = var8;
                  return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$processFakeAsPatch);
               }

               var8.join();
            }
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture processingForm(String var1) {
      MultiMap var2 = MultiMap.caseInsensitiveMultiMap();
      var2.add("_method", "patch");
      var2.add("authenticity_token", this.authenticity);
      var2.add("previous_step", "payment_method");
      var2.add("step", "");
      CompletableFuture var10000 = this.paymentToken;
      CompletableFuture var6;
      if (!var10000.isDone()) {
         var6 = var10000;
         return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$processingForm);
      } else {
         PaymentTokenSupplier var3 = (PaymentTokenSupplier)var10000.join();
         var2.add("s", var3.get());
         Iterator var4 = this.TEXTAREA_PARAMS.iterator();

         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            var2.add(var5, "");
         }

         if (!this.TEXTAREA_PARAMS.isEmpty()) {
            var2.add(var1 + "-count", "" + this.TEXTAREA_PARAMS.size());
            var2.add(var1 + "-count", "fs_count");
         }

         if (this.paymentGateway == null) {
            var10000 = this.getProcessingPage(var1, false);
            if (!var10000.isDone()) {
               var6 = var10000;
               return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$processingForm);
            }

            var10000.join();
         }

         var2.add("checkout[payment_gateway]", this.paymentGateway);
         var2.add("checkout[credit_card][vault]", "false");
         var2.add("checkout[different_billing_address]", "false");
         if (this.task.getSite() == Site.SHOPNICEKICKS) {
            var2.add("checkout[attributes][I-agree-to-the-Terms-and-Conditions]", "Yes");
         }

         if (this.task.getSite() == Site.KITH) {
            var2.add("checkout[remember_me]", "false");
         }

         if (this.task.getSite() != Site.SHOPNICEKICKS) {
            var2.add("checkout[remember_me]", "0");
         }

         var2.add("checkout[vault_phone]", this.task.getProfile().getCountry().equals("JAPAN") ? "+81" + this.task.getProfile().getPhone() : "+1" + this.task.getProfile().getPhone());
         if (this.task.getSite() == Site.SHOEPALACE) {
            var2.add("checkout[attributes][I-agree-to-the-Terms-and-Conditions]", "Yes");
         }

         if (this.price != null) {
            var2.add("checkout[total_price]", String.valueOf(this.price));
         }

         var2.add("complete", "1");
         var2.add("checkout[client_details][browser_width]", this.BROWSER_WIDTH);
         var2.add("checkout[client_details][browser_height]", this.BROWSER_HEIGHT);
         var2.add("checkout[client_details][javascript_enabled]", "1");
         var2.add("checkout[client_details][color_depth]", "24");
         var2.add("checkout[client_details][java_enabled]", "false");
         var2.add("checkout[client_details][browser_tz]", "" + TIMEZONE_OFFSET);
         return CompletableFuture.completedFuture(var2);
      }
   }

   public static CompletableFuture async$clearWithChangeJS(Shopify param0, int param1, HttpRequest param2, CompletableFuture param3, HttpResponse param4, Throwable param5, int param6, Object param7) {
      // $FF: Couldn't be decompiled
   }

   public void setCurrency() {
      switch (this.task.getProfile().getCountry()) {
         case "JP":
            this.api.getWebClient().cookieStore().put("cart_currency", "JPY", this.api.getSiteURL());
            break;
         case "CA":
            this.api.getWebClient().cookieStore().put("cart_currency", "CAD", this.api.getSiteURL());
            break;
         case "US":
            this.api.getWebClient().cookieStore().put("cart_currency", "USD", this.api.getSiteURL());
      }

   }

   public static CompletableFuture async$genCheckoutURLViaCart(Shopify param0, int param1, HttpRequest param2, CompletableFuture param3, HttpResponse param4, Throwable param5, int param6, Object param7) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$getCPHtml(Shopify param0, int param1, int param2, HttpRequest param3, CompletableFuture param4, Throwable param5, int param6, Object param7) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture sessionLogon(Account var1) {
      String var2 = var1.lookupSession();
      if (!var2.isBlank()) {
         JsonArray var3 = new JsonArray(var2);
         if (!var3.isEmpty()) {
            for(int var4 = 0; var4 < var3.size(); ++var4) {
               try {
                  String var5 = var3.getString(var4);
                  Cookie var6 = ClientCookieDecoder.STRICT.decode(var5);
                  if (var6 != null && var6.name().equals("_secure_session_id")) {
                     this.api.getCookies().put(var6);
                  }
               } catch (Throwable var8) {
                  this.logger.warn("Error parsing session state: {}", var8.getMessage());
               }
            }

            try {
               CompletableFuture var10000 = this.GETREQ("Validating Session", this.api.accountConfirmPage(), new Integer[]{200, 302, 401}, new String[]{""});
               if (!var10000.isDone()) {
                  CompletableFuture var7 = var10000;
                  return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$sessionLogon);
               }

               String var10 = (String)var10000.join();
               if (!var10.contains("order-history") && !var10.contains("/account/logout") && !var10.endsWith("/password")) {
                  return CompletableFuture.completedFuture(false);
               }

               return CompletableFuture.completedFuture(true);
            } catch (Throwable var9) {
               this.logger.warn("Error validating session: {}", var9.getMessage());
            }
         }
      }

      return CompletableFuture.completedFuture(false);
   }

   public static CompletableFuture async$walletPut(Shopify param0, String param1, int param2, Boolean param3, int param4, HttpRequest param5, HttpRequest param6, CompletableFuture param7, HttpResponse param8, Throwable param9, int param10, Object param11) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$initHarvesters(CompletionStage var0, Lock var1, CompletableFuture var2, int var3, Object var4) {
      CompletableFuture var10000;
      Lock var5;
      label40: {
         CompletionStage var8;
         switch (var3) {
            case 0:
               if (harvesterCFs.length == 0) {
                  return CompletableFuture.completedFuture((Object)null);
               }

               var8 = Vertx.currentContext().owner().sharedData().getLockWithTimeout("harvesterinitshopify", 1800000L).toCompletionStage();
               if (!var8.toCompletableFuture().isDone()) {
                  CompletionStage var7 = var8;
                  return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$initHarvesters).toCompletableFuture();
               }
               break;
            case 1:
               var8 = var0;
               break;
            case 2:
               var10000 = var2;
               var5 = var1;
               break label40;
            default:
               throw new IllegalArgumentException();
         }

         var5 = (Lock)var8.toCompletableFuture().join();

         for(int var6 = 0; var6 < HARVESTERS.length && !isHarvesterInited.get(); ++var6) {
            harvesterCFs[var6] = HARVESTERS[var6].start();
         }

         var10000 = CompletableFuture.allOf(harvesterCFs);
         if (!var10000.isDone()) {
            var2 = var10000;
            return var2.exceptionally(Function.identity()).thenCompose(Shopify::async$initHarvesters);
         }
      }

      var10000.join();
      isHarvesterInited.set(true);
      var5.release();
      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture handleRedirect(String var1, boolean var2) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$processPayment(Shopify param0, String param1, int param2, CompletableFuture param3, MultiMap param4, long param5, HttpRequest param7, HttpResponse param8, String param9, REDIRECT_STATUS param10, Exception param11, int param12, Object param13) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture submitContactAPI(String var1) {
      this.logger.info("Submitting contact...");
      int var2 = 0;

      while(super.running && var2++ < Integer.MAX_VALUE) {
         CompletableFuture var5;
         CompletableFuture var7;
         try {
            HttpRequest var3 = this.api.contactAPI(var1);
            var7 = Request.send(var3, this.contactAPIForm().toBuffer());
            if (!var7.isDone()) {
               var5 = var7;
               return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$submitContactAPI);
            }

            HttpResponse var4 = (HttpResponse)var7.join();
            if (var4 != null) {
               if (var4.statusCode() == 302 || var4.statusCode() == 301) {
                  return CompletableFuture.completedFuture((Buffer)var4.body());
               }

               if (var4.statusCode() == 429) {
                  if (var4.bodyAsString().contains("Too many attempts: Please try again in a few minutes") && ++this.counter429 > 8) {
                     throw new Throwable("Checkout expired. Restarting task...");
                  }
               } else {
                  this.logger.warn("Waiting to submit contact: status: '{}'", var4.statusCode());
               }

               var7 = VertxUtil.sleep((long)this.task.getRetryDelay());
               if (!var7.isDone()) {
                  var5 = var7;
                  return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$submitContactAPI);
               }

               var7.join();
            }
         } catch (Throwable var6) {
            this.logger.error("Error submitting contact: {}", var6.getMessage());
            var7 = super.randomSleep(3000);
            if (!var7.isDone()) {
               var5 = var7;
               return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$submitContactAPI);
            }

            var7.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$fetchELJS(Shopify param0, int param1, HttpRequest param2, CompletableFuture param3, HttpResponse param4, Throwable param5, int param6, Object param7) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture loginV2() {
      CompletableFuture var10000 = this.GETREQ("Checking login page", this.api.challengePage(), 200, new String[]{"authenticity"});
      CompletableFuture var8;
      if (!var10000.isDone()) {
         var8 = var10000;
         return var8.exceptionally(Function.identity()).thenCompose(Shopify::async$loginV2);
      } else {
         String var1 = (String)var10000.join();
         String var2 = Utils.quickParseFirst(var1, AUTHENTICITY_TOKEN_PATTERN);
         var10000 = TokenController.solveBasicCaptcha("https://" + this.api.getSiteURL() + "/challenge");
         if (!var10000.isDone()) {
            var8 = var10000;
            return var8.exceptionally(Function.identity()).thenCompose(Shopify::async$loginV2);
         } else {
            String var3 = ((CaptchaToken)var10000.join()).getToken();
            MultiMap var4 = this.api.challengeForm(var2, var3);
            int var5 = 0;

            while(super.running && var5++ < Integer.MAX_VALUE) {
               try {
                  HttpRequest var6 = this.api.submitChallenge();
                  var10000 = Request.send(var6, var4);
                  if (!var10000.isDone()) {
                     var8 = var10000;
                     return var8.exceptionally(Function.identity()).thenCompose(Shopify::async$loginV2);
                  }

                  HttpResponse var7 = (HttpResponse)var10000.join();
                  if (var7 != null) {
                     if (var7.statusCode() == 302 || var7.statusCode() == 301) {
                        var10000 = this.handleRedirect(var7, false);
                        if (!var10000.isDone()) {
                           var8 = var10000;
                           return var8.exceptionally(Function.identity()).thenCompose(Shopify::async$loginV2);
                        }

                        if ((Boolean)var10000.join()) {
                           return CompletableFuture.completedFuture((Buffer)var7.body());
                        }
                     }

                     if (var7.statusCode() != 302) {
                        this.logger.warn("Retrying login V2 challenge: status: '{}'", var7.statusCode());
                        var10000 = VertxUtil.sleep((long)this.task.getRetryDelay());
                        if (!var10000.isDone()) {
                           var8 = var10000;
                           return var8.exceptionally(Function.identity()).thenCompose(Shopify::async$loginV2);
                        }

                        var10000.join();
                     }
                  }
               } catch (Throwable var9) {
                  this.logger.error("Error logging in V2: {}", var9.getMessage());
                  var10000 = super.randomSleep(3000);
                  if (!var10000.isDone()) {
                     var8 = var10000;
                     return var8.exceptionally(Function.identity()).thenCompose(Shopify::async$loginV2);
                  }

                  var10000.join();
               }
            }

            return CompletableFuture.completedFuture((Object)null);
         }
      }
   }

   public static CompletableFuture async$getPaymentStatus(Shopify param0, String param1, int param2, HttpRequest param3, CompletableFuture param4, HttpResponse param5, Throwable param6, int param7, Object param8) {
      // $FF: Couldn't be decompiled
   }

   public void initKeywords() {
      this.negativeKeywords.clear();
      this.positiveKeywords.clear();
      String[] var1 = this.task.getKeywords();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         String var4 = var1[var3];
         var4 = var4.toLowerCase();
         if (var4.charAt(0) == '-') {
            this.negativeKeywords.add(var4.substring(1));
         } else {
            this.positiveKeywords.add(var4);
         }
      }

   }

   public CompletableFuture shippingForm(String var1) {
      MultiMap var2 = MultiMap.caseInsensitiveMultiMap();
      var2.add("_method", "patch");
      var2.add("authenticity_token", this.authenticity);
      var2.add("previous_step", "shipping_method");
      var2.add("step", "payment_method");
      CompletableFuture var10002 = this.shippingRate;
      if (!var10002.isDone()) {
         CompletableFuture var7 = var10002;
         String var6 = "checkout[shipping_rate][id]";
         return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$shippingForm);
      } else {
         var2.add("checkout[shipping_rate][id]", ((ShippingRateSupplier)var10002.join()).get());
         Iterator var3 = this.TEXTAREA_PARAMS.iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            var2.add(var4, "");
         }

         if (!this.TEXTAREA_PARAMS.isEmpty()) {
            var2.add(var1 + "-count", "" + this.TEXTAREA_PARAMS.size());
            var2.add(var1 + "-count", "fs_count");
         }

         var2.add("checkout[client_details][browser_width]", this.BROWSER_WIDTH);
         var2.add("checkout[client_details][browser_height]", this.BROWSER_HEIGHT);
         var2.add("checkout[client_details][javascript_enabled]", "1");
         var2.add("checkout[client_details][color_depth]", "24");
         var2.add("checkout[client_details][java_enabled]", "false");
         var2.add("checkout[client_details][browser_tz]", "" + TIMEZONE_OFFSET);
         return CompletableFuture.completedFuture(var2);
      }
   }

   public static CompletableFuture async$run(Shopify param0, CompletableFuture param1, int param2, String param3, CompletableFuture param4, String param5, JsonObject param6, int param7, int param8, Object param9) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture fetchELJS() {
      int var1 = 0;
      this.logger.info("Monitoring [EL]");

      while(super.running && var1++ < Integer.MAX_VALUE) {
         this.instanceSignal = this.task.getKeywords()[0].replaceAll("collections/.*?/", "");

         CompletableFuture var5;
         CompletableFuture var7;
         try {
            HttpRequest var2 = this.api.elJS(this.instanceSignal);
            var7 = Request.send(var2);
            if (!var7.isDone()) {
               var5 = var7;
               return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$fetchELJS);
            }

            HttpResponse var3 = (HttpResponse)var7.join();
            if (var3 != null) {
               if (var3.statusCode() == 200) {
                  VertxUtil.sendSignal(this.api.getSiteURL());
                  VertxUtil.sendSignal(this.instanceSignal, var3.bodyAsString());
                  return CompletableFuture.completedFuture(var3.bodyAsJsonObject());
               }

               if (var3.statusCode() != 302 && var3.statusCode() != 301 && var3.statusCode() != 401) {
                  this.logger.warn("Failed waiting for restock [EL]: status: '{}'", var3.statusCode());
                  var7 = VertxUtil.signalSleep(this.instanceSignal, (long)this.task.getMonitorDelay());
                  if (!var7.isDone()) {
                     var5 = var7;
                     return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$fetchELJS);
                  }

                  Object var4 = var7.join();
                  if (var4 != null) {
                     return CompletableFuture.completedFuture(new JsonObject(var4.toString()));
                  }
               } else {
                  var7 = this.handleRedirect(var3, false);
                  if (!var7.isDone()) {
                     var5 = var7;
                     return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$fetchELJS);
                  }

                  var7.join();
               }
            }
         } catch (Throwable var6) {
            this.logger.error("Failed waiting for restock [EL]: {}", var6.getMessage());
            var7 = super.randomSleep(3000);
            if (!var7.isDone()) {
               var5 = var7;
               return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$fetchELJS);
            }

            var7.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$loginV2(Shopify param0, CompletableFuture param1, String param2, String param3, String param4, MultiMap param5, int param6, HttpRequest param7, HttpResponse param8, Throwable param9, int param10, Object param11) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture getShippingPage(String var1) {
      this.logger.info("Checking shipping...");
      int var2 = 0;

      while(super.running && var2++ < Integer.MAX_VALUE) {
         CompletableFuture var6;
         CompletableFuture var8;
         try {
            HttpRequest var3 = this.api.shippingPage(var1);
            var8 = this.safeCompleteRequest(var3, "checking shipping");
            if (!var8.isDone()) {
               var6 = var8;
               return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$getShippingPage);
            }

            HttpResponse var4 = (HttpResponse)var8.join();
            if (var4 != null) {
               if (var4.statusCode() == 200) {
                  this.updateCheckoutParams(var4.bodyAsString());
                  this.updateShippingRate(var4.bodyAsString());
                  return CompletableFuture.completedFuture(var4.bodyAsString());
               }

               if (var4.statusCode() == 302 || var4.statusCode() == 301) {
                  var8 = this.handleRedirect(var4, false);
                  if (!var8.isDone()) {
                     var6 = var8;
                     return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$getShippingPage);
                  }

                  Boolean var5 = (Boolean)var8.join();
                  if (var5 == null) {
                     return CompletableFuture.completedFuture((Object)null);
                  }

                  if (var5) {
                     this.logger.info("Unknown redirect. Handling..");
                     var8 = VertxUtil.randomSleep((long)this.task.getMonitorDelay());
                     if (!var8.isDone()) {
                        var6 = var8;
                        return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$getShippingPage);
                     }

                     var8.join();
                  }
               }
            }
         } catch (Throwable var7) {
            this.logger.error("Checking shipping: {}", var7.getMessage());
            var8 = super.randomSleep(3000);
            if (!var8.isDone()) {
               var6 = var8;
               return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$getShippingPage);
            }

            var8.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$getProcessingPage(Shopify param0, String param1, int param2, int param3, HttpRequest param4, CompletableFuture param5, HttpResponse param6, int param7, Boolean param8, Throwable param9, int param10, Object param11) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$monitorQueue(Shopify param0, String param1, CompletableFuture param2, HttpRequest param3, HttpResponse param4, REDIRECT_STATUS param5, Exception param6, int param7, Object param8) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture monitorQueue(String var1) {
      this.logger.info("Monitoring queue...");

      while(super.running && Utils.calculateMSLeftUntilHour() > 15000 && (this.cpMonitor == null || !this.cpMonitor.isDone()) && this.isSmart) {
         CompletableFuture var10000;
         CompletableFuture var5;
         try {
            var10000 = this.atcAJAX(this.precartItemName, false);
            if (!var10000.isDone()) {
               var5 = var10000;
               return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$monitorQueue);
            }

            var10000.join();
            HttpRequest var2 = this.api.emptyCheckout(false);
            var10000 = this.safeCompleteRequest(var2, "monitoring queue");
            if (!var10000.isDone()) {
               var5 = var10000;
               return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$monitorQueue);
            }

            HttpResponse var3 = (HttpResponse)var10000.join();
            var10000 = this.clearCart();
            if (!var10000.isDone()) {
               var5 = var10000;
               return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$monitorQueue);
            }

            var10000.join();
            if (var3 != null) {
               if (var3.statusCode() != 302 && var3.statusCode() != 301) {
                  this.logger.info("Unable to properly monitoring queue {}", var3.statusCode());
                  var10000 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
                  if (!var10000.isDone()) {
                     var5 = var10000;
                     return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$monitorQueue);
                  }

                  var10000.join();
               } else {
                  REDIRECT_STATUS var4 = REDIRECT_STATUS.checkRedirectStatus(var3.getHeader("location"));
                  if (var4 == REDIRECT_STATUS.CHECKPOINT || var4 == REDIRECT_STATUS.PASSWORD) {
                     return CompletableFuture.completedFuture((Object)null);
                  }

                  if (var4 != REDIRECT_STATUS.CART) {
                     var10000 = this.handleRedirect(var3, true);
                     if (!var10000.isDone()) {
                        var5 = var10000;
                        return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$monitorQueue);
                     }

                     var10000.join();
                  }

                  this.logger.info("Monitoring queue...");
                  var10000 = VertxUtil.randomSleep((long)this.task.getMonitorDelay());
                  if (!var10000.isDone()) {
                     var5 = var10000;
                     return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$monitorQueue);
                  }

                  var10000.join();
               }
            }
         } catch (Exception var6) {
            this.logger.error("Error monitoring queue: {}", var6.getMessage());
            var10000 = VertxUtil.randomSleep(3000L);
            if (!var10000.isDone()) {
               var5 = var10000;
               return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$monitorQueue);
            }

            var10000.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture shippingAndBillingPost() {
      this.logger.info("Generating checkout [API]...");
      byte var1 = 0;
      int var2 = 0;

      while(super.running && var2++ < Integer.MAX_VALUE) {
         CompletableFuture var7;
         CompletableFuture var9;
         try {
            HttpRequest var3 = this.api.atcWallets();
            var9 = Request.send(var3, this.contactAPIAndCartForm((boolean)var1));
            if (!var9.isDone()) {
               var7 = var9;
               return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$shippingAndBillingPost);
            }

            HttpResponse var4 = (HttpResponse)var9.join();
            if (var4 != null) {
               if (var4.statusCode() == 200 || var4.statusCode() == 201) {
                  return CompletableFuture.completedFuture(var4.bodyAsJsonObject().getJsonObject("checkout").getString("web_url"));
               }

               if (var4.statusCode() == 202) {
                  if (var4.bodyAsString().contains(this.task.getProfile().getAddress1())) {
                     return CompletableFuture.completedFuture(var4.bodyAsJsonObject().getJsonObject("checkout").getString("web_url"));
                  }
               } else if (var4.statusCode() != 409 || var2 >= 5) {
                  if (var4.statusCode() == 429 && this.api.getCookies().contains("_checkout_queue_token")) {
                     CompletableFuture var5 = null;
                     if (var1 == 0) {
                        var5 = this.atcAJAX(this.instanceSignal, true);
                     }

                     var9 = this.handleQueueNew();
                     if (!var9.isDone()) {
                        var7 = var9;
                        return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$shippingAndBillingPost);
                     }

                     var9.join();
                     if (var5 != null) {
                        if (!var5.isDone()) {
                           return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$shippingAndBillingPost);
                        }

                        JsonObject var6 = (JsonObject)var5.join();
                        if (var6 != null) {
                           var1 = 1;
                        }
                     }
                  } else if (var4.statusCode() == 422) {
                     if (var1 == 0) {
                        var1 = 1;
                        var9 = this.smoothCart();
                        if (!var9.isDone()) {
                           var7 = var9;
                           return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$shippingAndBillingPost);
                        }

                        var9.join();
                     } else {
                        this.logger.warn("Retrying generating checkout [API]: status: '{}'", var4.statusCode());
                        var9 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
                        if (!var9.isDone()) {
                           var7 = var9;
                           return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$shippingAndBillingPost);
                        }

                        var9.join();
                     }
                  } else {
                     this.logger.warn("Retrying generating checkout [API]: status: '{}'", var4.statusCode());
                     var9 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
                     if (!var9.isDone()) {
                        var7 = var9;
                        return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$shippingAndBillingPost);
                     }

                     var9.join();
                  }
               }
            }
         } catch (Throwable var8) {
            this.logger.error("Error generating checkout [API]: {}", var8.getMessage());
            var9 = super.randomSleep(3000);
            if (!var9.isDone()) {
               var7 = var9;
               return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$shippingAndBillingPost);
            }

            var9.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public void updateCheckoutParams(String var1) {
      Matcher var2 = AUTHENTICITY_TOKEN_PATTERN.matcher(var1);
      if (var2.find()) {
         this.authenticity = var2.group(1);
      } else {
         this.logger.info("No checkout token found");
      }

      this.TEXTAREA_PARAMS.clear();
      Matcher var3 = TEXTAREA_PARAMS_PATTERN.matcher(var1);

      while(var3.find()) {
         this.TEXTAREA_PARAMS.add(var3.group(1));
      }

   }

   public CompletableFuture initShopDetails() {
      CompletableFuture var10000 = this.fetchMeta();
      if (!var10000.isDone()) {
         CompletableFuture var2 = var10000;
         return var2.exceptionally(Function.identity()).thenCompose(Shopify::async$initShopDetails);
      } else {
         JsonObject var1 = ((JsonObject)var10000.join()).getJsonObject("paymentInstruments");
         if (var1 != null) {
            ShopifyAPI var3 = this.api;
            JsonObject var10001 = var1.getJsonObject("checkoutConfig");
            var3.setShopID("" + var10001.getNumber("shopId"));
            this.api.setAPIToken(var1.getString("accessToken"));
         }

         return CompletableFuture.completedFuture((Object)null);
      }
   }

   public static CompletableFuture async$fetchProductsJSON(Shopify param0, int param1, int param2, Lock param3, CompletionStage param4, HttpRequest param5, CompletableFuture param6, HttpResponse param7, JsonObject param8, SharedData param9, Iterator param10, String param11, Throwable param12, int param13, Object param14) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$atcBasic(Shopify param0, String param1, int param2, HttpRequest param3, CompletableFuture param4, HttpResponse param5, Throwable param6, int param7, Object param8) {
      // $FF: Couldn't be decompiled
   }

   public Buffer newQueueBody() {
      String var1 = this.api.getWebClient().cookieStore().getCookieValue("_checkout_queue_token");
      return Buffer.buffer("{\"query\":\"\\n      {\\n        poll(token: $token) {\\n          token\\n          pollAfter\\n          queueEtaSeconds\\n          productVariantAvailability {\\n            id\\n            available\\n          }\\n        }\\n      }\\n    \",\"variables\":{\"token\":\"" + var1.replace("%3D", "=") + "\"}}");
   }

   public static CompletableFuture async$parseNewQueueJson(Shopify var0, JsonObject var1, String var2, String var3, long var4, String var6, String var7, CompletableFuture var8, int var9, Object var10) {
      CompletableFuture var10000;
      label64: {
         label53: {
            label52: {
               label51: {
                  switch (var9) {
                     case 0:
                        var2 = var1.getJsonObject("data").getJsonObject("poll").getString("__typename");
                        var3 = var1.getJsonObject("data").getJsonObject("poll").getString("token");
                        var0.api.getWebClient().cookieStore().removeAnyMatch("_checkout_queue_token");
                        var0.api.getWebClient().cookieStore().put("_checkout_queue_token", var3, var0.api.getSiteURL());
                        if (!var2.equals("PollContinue")) {
                           if (var2.equals("PollComplete")) {
                              var0.logger.info("Passed new queue.");
                              Analytics.queuePasses.increment();
                              return CompletableFuture.completedFuture(true);
                           }

                           var0.logger.info("Error with new queue. Please wait.");
                           var10000 = VertxUtil.randomSleep(3000L);
                           if (!var10000.isDone()) {
                              var8 = var10000;
                              return var8.exceptionally(Function.identity()).thenCompose(Shopify::async$parseNewQueueJson);
                           }
                           break label64;
                        }

                        var4 = var1.getJsonObject("data").getJsonObject("poll").getLong("queueEtaSeconds");
                        var6 = Instant.ofEpochSecond(Instant.now().getEpochSecond() + var4).atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                        var7 = var1.getJsonObject("data").getJsonObject("poll").getString("pollAfter");
                        var0.logger.info("Waiting in new queue. Expected wait time -> ({}) [{}][{}]", var4, var6, true);
                        if (var0.prevTime == var4) {
                           var10000 = VertxUtil.hardCodedSleep(var0.calculatePollTime(var7) + 500L);
                           if (!var10000.isDone()) {
                              var8 = var10000;
                              return var8.exceptionally(Function.identity()).thenCompose(Shopify::async$parseNewQueueJson);
                           }
                           break label51;
                        }

                        var0.prevTime = var4;
                        var10000 = VertxUtil.hardCodedSleep(var0.calculatePollTime(var7));
                        if (!var10000.isDone()) {
                           var8 = var10000;
                           return var8.exceptionally(Function.identity()).thenCompose(Shopify::async$parseNewQueueJson);
                        }
                        break;
                     case 1:
                        var10000 = var8;
                        break label51;
                     case 2:
                        var10000 = var8;
                        break;
                     case 3:
                        var10000 = var8;
                        break label53;
                     case 4:
                        var10000 = var8;
                        break label64;
                     default:
                        throw new IllegalArgumentException();
                  }

                  var10000.join();
                  break label52;
               }

               var10000.join();
            }

            var10000 = VertxUtil.hardCodedSleep(1250L);
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(Shopify::async$parseNewQueueJson);
            }
         }

         var10000.join();
         return CompletableFuture.completedFuture(false);
      }

      var10000.join();
      return CompletableFuture.completedFuture(false);
   }

   public CompletableFuture login() {
      AccountController var1 = (AccountController)Engine.get().getModule(Controller.ACCOUNT);
      CompletableFuture var10000 = var1.findAccount(this.task.getProfile().getEmail(), true).toCompletionStage().toCompletableFuture();
      CompletableFuture var7;
      if (!var10000.isDone()) {
         var7 = var10000;
         return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$login);
      } else {
         Account var2 = (Account)var10000.join();
         if (var2 != null) {
            var2.setSite(this.api.getSiteURL());
            var10000 = this.sessionLogon(var2);
            if (!var10000.isDone()) {
               var7 = var10000;
               return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$login);
            }

            boolean var3 = (Boolean)var10000.join();
            if (var3) {
               return CompletableFuture.completedFuture((Object)null);
            }
         } else {
            var2 = rotateAccount();
            var2.setSite(this.api.getSiteURL());
         }

         MultiMap var11 = MultiMap.caseInsensitiveMultiMap();
         var11.set("form_type", "customer_login");
         var11.set("utf8", "✓");
         var11.set("customer[email]", var2.getUser());
         var11.set("customer[password]", var2.getPass());
         var11.set("return_url", "/account");
         this.logger.info("Logging in.");
         int var4 = 0;

         while(super.running && var4++ < Integer.MAX_VALUE) {
            CompletableFuture var10002 = TokenController.solveBasicCaptcha("https://" + this.api.getSiteURL() + "/account/login?return_url=%2Faccount");
            if (!var10002.isDone()) {
               CompletableFuture var9 = var10002;
               String var8 = "recaptcha-v3-token";
               return var9.exceptionally(Function.identity()).thenCompose(Shopify::async$login);
            }

            var11.set("recaptcha-v3-token", ((CaptchaToken)var10002.join()).getToken());

            try {
               HttpRequest var5 = this.api.login();
               var10000 = Request.send(var5, var11);
               if (!var10000.isDone()) {
                  var7 = var10000;
                  return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$login);
               }

               HttpResponse var6 = (HttpResponse)var10000.join();
               if (var6 != null) {
                  if (var6.statusCode() == 302 || var6.statusCode() == 301) {
                     var10000 = this.handleRedirect(var6, false);
                     if (!var10000.isDone()) {
                        var7 = var10000;
                        return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$login);
                     }

                     if ((Boolean)var10000.join()) {
                        var2.setSessionString(this.api.getCookies().asJson().encode());
                        super.vertx.eventBus().send("accounts.writer.session", var2);
                        return CompletableFuture.completedFuture((Buffer)var6.body());
                     }
                  }

                  if (var6.statusCode() == 403) {
                     this.logger.warn("Cloudflare block! Retrying...");
                     var10000 = VertxUtil.sleep((long)this.task.getRetryDelay());
                     if (!var10000.isDone()) {
                        var7 = var10000;
                        return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$login);
                     }

                     var10000.join();
                  } else if (var6.statusCode() != 302) {
                     this.logger.warn("Retrying login: status: '{}'", var6.statusCode());
                     var10000 = VertxUtil.sleep((long)this.task.getRetryDelay());
                     if (!var10000.isDone()) {
                        var7 = var10000;
                        return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$login);
                     }

                     var10000.join();
                  }
               }
            } catch (Throwable var10) {
               this.logger.error("Error logging in: {}", var10.getMessage());
               var10000 = super.randomSleep(3000);
               if (!var10000.isDone()) {
                  var7 = var10000;
                  return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$login);
               }

               var10000.join();
            }
         }

         return CompletableFuture.completedFuture((Object)null);
      }
   }

   public MultiMap upsellForm(String var1) {
      MultiMap var2 = MultiMap.caseInsensitiveMultiMap();
      var2.set("properties[" + (String)this.propertiesPair.first + "]", (String)this.propertiesPair.second);
      if (((String)this.propertiesPair.first).contains("upsell")) {
         var2.set("option-0", this.size);
      }

      var2.set("id", var1);
      var2.set("quantity", "1");
      return var2;
   }

   public CompletableFuture solveCheckpointCaptcha(String var1) {
      try {
         this.logger.info("Checkpoint needs solving");
         CaptchaToken var2 = new CaptchaToken("https://" + this.api.getSiteURL() + "/checkpoint?return_to=https%3A%2F%2F" + this.api.getSiteURL() + "%2Fcheckout", true, this.api.proxyStringFull(), var1, this);
         SolveFuture var3 = ((TokenController)Engine.get().getModule(Controller.TOKEN)).solve(var2);
         if (!var3.toCompletableFuture().isDone()) {
            return var3.exceptionally(Function.identity()).thenCompose(Shopify::async$solveCheckpointCaptcha).toCompletableFuture();
         } else {
            var3.toCompletableFuture().join();
            return CompletableFuture.completedFuture(var2);
         }
      } catch (Throwable var5) {
         this.logger.error("HARVEST ERR: {}", var5.getMessage());
         return CompletableFuture.completedFuture((Object)null);
      }
   }

   public CompletableFuture atcBasic(String var1) {
      int var2 = 0;
      this.logger.info("Attempting ATC [Basic]");

      while(super.running && var2++ < Integer.MAX_VALUE) {
         CompletableFuture var5;
         CompletableFuture var7;
         try {
            HttpRequest var3 = this.api.basicATC();
            var7 = Request.send(var3, this.atcBasicForm(var1));
            if (!var7.isDone()) {
               var5 = var7;
               return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$atcBasic);
            }

            HttpResponse var4 = (HttpResponse)var7.join();
            if (var4 != null) {
               if ((var4.statusCode() == 302 || var4.statusCode() == 301) && var4.getHeader("location").endsWith("/cart")) {
                  return CompletableFuture.completedFuture((Object)null);
               }

               if (var4.statusCode() == 302 || var4.statusCode() == 301) {
                  var7 = this.handleRedirect(var4, false);
                  if (!var7.isDone()) {
                     var5 = var7;
                     return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$atcBasic);
                  }

                  var7.join();
               }

               this.logger.warn("Failed attempting ATC [Basic]: status: '{}'", var4.statusCode());
               var7 = VertxUtil.sleep((long)this.task.getRetryDelay());
               if (!var7.isDone()) {
                  var5 = var7;
                  return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$atcBasic);
               }

               var7.join();
            }
         } catch (Throwable var6) {
            this.logger.error("Failed attempting ATC [Basic]: {}", var6.getMessage());
            var7 = super.randomSleep(3000);
            if (!var7.isDone()) {
               var5 = var7;
               return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$atcBasic);
            }

            var7.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public String getFakeTokenSalt() {
      String var1 = "abcde0123456789";
      StringBuilder var2 = new StringBuilder();

      while(var2.length() < 32) {
         int var3 = (int)(ThreadLocalRandom.current().nextFloat() * (float)"abcde0123456789".length());
         var2.append("abcde0123456789".charAt(var3));
      }

      return var2.toString();
   }

   public static CompletableFuture async$shippingAndBillingPut(Shopify param0, String param1, int param2, HttpRequest param3, CompletableFuture param4, HttpResponse param5, Throwable param6, int param7, Object param8) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture genCheckoutURLViaCart() {
      int var1 = 0;
      this.logger.info("Generating checkout URL [c]...");

      while(super.running && var1++ < Integer.MAX_VALUE) {
         CompletableFuture var4;
         CompletableFuture var6;
         try {
            HttpRequest var2 = this.api.emptyCheckoutViaCart();
            var6 = Request.send(var2, this.api.emptyCheckoutViaCartForm());
            if (!var6.isDone()) {
               var4 = var6;
               return var4.exceptionally(Function.identity()).thenCompose(Shopify::async$genCheckoutURLViaCart);
            }

            HttpResponse var3 = (HttpResponse)var6.join();
            if (var3 != null) {
               if (var3.statusCode() == 302 || var3.statusCode() == 301) {
                  var6 = this.handleRedirect(var3, true);
                  if (!var6.isDone()) {
                     var4 = var6;
                     return var4.exceptionally(Function.identity()).thenCompose(Shopify::async$genCheckoutURLViaCart);
                  }

                  if ((Boolean)var6.join()) {
                     return CompletableFuture.completedFuture(var3.getHeader("location"));
                  }
               }

               if (var3.statusCode() != 302) {
                  this.logger.warn("Failed generating checkout URL [c]: status: '{}'", var3.statusCode());
                  var6 = VertxUtil.sleep((long)this.task.getRetryDelay());
                  if (!var6.isDone()) {
                     var4 = var6;
                     return var4.exceptionally(Function.identity()).thenCompose(Shopify::async$genCheckoutURLViaCart);
                  }

                  var6.join();
               }
            }
         } catch (Throwable var5) {
            this.logger.error("Failed generating checkout URL [c]: {}", var5.getMessage());
            var6 = VertxUtil.randomSleep(3000L);
            if (!var6.isDone()) {
               var4 = var6;
               return var4.exceptionally(Function.identity()).thenCompose(Shopify::async$genCheckoutURLViaCart);
            }

            var6.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$genPaymentToken(Shopify param0, JsonObject param1, int param2, WebClient param3, HttpRequest param4, CompletableFuture param5, HttpResponse param6, Throwable param7, int param8, Object param9) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$clearCart(Shopify param0, int param1, HttpRequest param2, CompletableFuture param3, HttpResponse param4, Throwable param5, int param6, Object param7) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$processingForm(Shopify var0, String var1, MultiMap var2, CompletableFuture var3, PaymentTokenSupplier var4, int var5, Object var6) {
      label65: {
         CompletableFuture var10000;
         label69: {
            CompletableFuture var10;
            switch (var5) {
               case 0:
                  var2 = MultiMap.caseInsensitiveMultiMap();
                  var2.add("_method", "patch");
                  var2.add("authenticity_token", var0.authenticity);
                  var2.add("previous_step", "payment_method");
                  var2.add("step", "");
                  var10000 = var0.paymentToken;
                  if (!var10000.isDone()) {
                     var10 = var10000;
                     return var10.exceptionally(Function.identity()).thenCompose(Shopify::async$processingForm);
                  }
                  break;
               case 1:
                  var10000 = var3;
                  break;
               case 2:
                  var10000 = var3;
                  break label69;
               default:
                  throw new IllegalArgumentException();
            }

            PaymentTokenSupplier var7 = (PaymentTokenSupplier)var10000.join();
            var2.add("s", var7.get());
            Iterator var8 = var0.TEXTAREA_PARAMS.iterator();

            while(var8.hasNext()) {
               String var9 = (String)var8.next();
               var2.add(var9, "");
            }

            if (!var0.TEXTAREA_PARAMS.isEmpty()) {
               var2.add(var1 + "-count", "" + var0.TEXTAREA_PARAMS.size());
               var2.add(var1 + "-count", "fs_count");
            }

            if (var0.paymentGateway != null) {
               break label65;
            }

            var10000 = var0.getProcessingPage(var1, false);
            if (!var10000.isDone()) {
               var10 = var10000;
               return var10.exceptionally(Function.identity()).thenCompose(Shopify::async$processingForm);
            }
         }

         var10000.join();
      }

      var2.add("checkout[payment_gateway]", var0.paymentGateway);
      var2.add("checkout[credit_card][vault]", "false");
      var2.add("checkout[different_billing_address]", "false");
      if (var0.task.getSite() == Site.SHOPNICEKICKS) {
         var2.add("checkout[attributes][I-agree-to-the-Terms-and-Conditions]", "Yes");
      }

      if (var0.task.getSite() == Site.KITH) {
         var2.add("checkout[remember_me]", "false");
      }

      if (var0.task.getSite() != Site.SHOPNICEKICKS) {
         var2.add("checkout[remember_me]", "0");
      }

      var2.add("checkout[vault_phone]", var0.task.getProfile().getCountry().equals("JAPAN") ? "+81" + var0.task.getProfile().getPhone() : "+1" + var0.task.getProfile().getPhone());
      if (var0.task.getSite() == Site.SHOEPALACE) {
         var2.add("checkout[attributes][I-agree-to-the-Terms-and-Conditions]", "Yes");
      }

      if (var0.price != null) {
         var2.add("checkout[total_price]", String.valueOf(var0.price));
      }

      var2.add("complete", "1");
      var2.add("checkout[client_details][browser_width]", var0.BROWSER_WIDTH);
      var2.add("checkout[client_details][browser_height]", var0.BROWSER_HEIGHT);
      var2.add("checkout[client_details][javascript_enabled]", "1");
      var2.add("checkout[client_details][color_depth]", "24");
      var2.add("checkout[client_details][java_enabled]", "false");
      var2.add("checkout[client_details][browser_tz]", "" + TIMEZONE_OFFSET);
      return CompletableFuture.completedFuture(var2);
   }

   public static Account rotateAccount() {
      return ((AccountController)Engine.get().getModule(Controller.ACCOUNT)).getAccountCyclic();
   }

   public MultiMap contactForm(String var1) {
      MultiMap var2 = MultiMap.caseInsensitiveMultiMap();
      var2.add("_method", "patch");
      var2.add("authenticity_token", this.authenticity);
      var2.add("previous_step", "contact_information");
      var2.add("step", "shipping_method");
      var2.add("checkout[email]", this.task.getProfile().getEmail());
      var2.add("checkout[buyer_accepts_marketing]", "0");
      var2.add("checkout[shipping_address][first_name]", "");
      var2.add("checkout[shipping_address][last_name]", "");
      if (this.task.getSite() == Site.SHOPNICEKICKS) {
         var2.add("checkout[shipping_address][company]", "");
      }

      var2.add("checkout[shipping_address][address1]", "");
      var2.add("checkout[shipping_address][address2]", "");
      var2.add("checkout[shipping_address][city]", "");
      var2.add("checkout[shipping_address][country]", "");
      var2.add("checkout[shipping_address][province]", "");
      var2.add("checkout[shipping_address][zip]", "");
      var2.add("checkout[shipping_address][phone]", "");
      var2.add("checkout[shipping_address][first_name]", this.task.getProfile().getFirstName());
      var2.add("checkout[shipping_address][last_name]", this.task.getProfile().getLastName());
      if (this.task.getSite() == Site.SHOPNICEKICKS) {
         var2.add("checkout[shipping_address][company]", "");
      }

      var2.add("checkout[shipping_address][address1]", this.task.getProfile().getAddress1());
      var2.add("checkout[shipping_address][address2]", this.task.getProfile().getAddress2());
      var2.add("checkout[shipping_address][city]", this.task.getProfile().getCity());
      var2.add("checkout[shipping_address][country]", this.task.getProfile().getFullCountry());
      var2.add("checkout[shipping_address][province]", this.task.getProfile().getCountry().equals("US") ? this.task.getProfile().getState() : this.task.getProfile().getFullState());
      var2.add("checkout[shipping_address][zip]", this.task.getProfile().getZip());
      var2.add("checkout[shipping_address][phone]", this.task.getProfile().getPhone());
      if (this.task.getSite() == Site.SHOPNICEKICKS) {
         var2.add("checkout[remember_me]", "");
         var2.add("checkout[remember_me]", "0");
      }

      Iterator var3 = this.TEXTAREA_PARAMS.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         var2.add(var4, "");
      }

      if (!this.TEXTAREA_PARAMS.isEmpty()) {
         var2.add(var1 + "-count", "" + this.TEXTAREA_PARAMS.size());
         var2.add(var1 + "-count", "fs_count");
      }

      var2.add("checkout[client_details][browser_width]", this.BROWSER_WIDTH);
      var2.add("checkout[client_details][browser_height]", this.BROWSER_HEIGHT);
      var2.add("checkout[client_details][javascript_enabled]", "1");
      var2.add("checkout[client_details][color_depth]", "24");
      var2.add("checkout[client_details][java_enabled]", "false");
      var2.add("checkout[client_details][browser_tz]", "" + TIMEZONE_OFFSET);
      return var2;
   }

   public CompletableFuture processingAPIForm() {
      JsonObject var1 = new JsonObject();
      var1.put("complete", "1");
      var1.put("field_type", "hidden");
      CompletableFuture var10002 = this.paymentToken;
      String var3;
      if (!var10002.isDone()) {
         CompletableFuture var9 = var10002;
         var3 = "s";
         return var9.exceptionally(Function.identity()).thenCompose(Shopify::async$processingAPIForm);
      } else {
         var1.put("s", ((PaymentTokenSupplier)var10002.join()).get());
         if (!this.hasShippingAlreadySubmittedAPI) {
            JsonObject var10 = new JsonObject();
            JsonObject var10004 = new JsonObject();
            CompletableFuture var10006 = this.shippingRate;
            if (!var10006.isDone()) {
               CompletableFuture var8 = var10006;
               String var7 = "id";
               JsonObject var6 = var10004;
               String var5 = "shipping_rate";
               JsonObject var4 = var10;
               var3 = "checkout";
               return var8.exceptionally(Function.identity()).thenCompose(Shopify::async$processingAPIForm);
            }

            var1.put("checkout", var10.put("shipping_rate", var10004.put("id", ((ShippingRateSupplier)var10006.join()).get())));
         }

         return CompletableFuture.completedFuture(var1);
      }
   }

   public static CompletableFuture async$smoothCart(Shopify var0, CompletableFuture var1, int var2, Object var3) {
      CompletableFuture var10000;
      label37: {
         switch (var2) {
            case 0:
               if (var0.propertiesPair != null) {
                  var10000 = var0.atcUpsell(var0.instanceSignal);
                  if (!var10000.isDone()) {
                     var1 = var10000;
                     return var1.exceptionally(Function.identity()).thenCompose(Shopify::async$smoothCart);
                  }
                  break label37;
               }

               var10000 = var0.atcAJAX(var0.instanceSignal, false);
               if (!var10000.isDone()) {
                  var1 = var10000;
                  return var1.exceptionally(Function.identity()).thenCompose(Shopify::async$smoothCart);
               }
               break;
            case 1:
               var10000 = var1;
               break label37;
            case 2:
               var10000 = var1;
               break;
            default:
               throw new IllegalArgumentException();
         }

         var10000.join();
         return CompletableFuture.completedFuture((Object)null);
      }

      var10000.join();
      return CompletableFuture.completedFuture((Object)null);
   }

   public MultiMap atcBasicForm(String var1) {
      MultiMap var2 = MultiMap.caseInsensitiveMultiMap();
      var2.set("id", var1);
      var2.set("add", "");
      return var2;
   }

   public static CompletableFuture async$shippingAndBillingPost(Shopify param0, int param1, int param2, HttpRequest param3, CompletableFuture param4, HttpResponse param5, CompletableFuture param6, Throwable param7, int param8, Object param9) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$sessionLogon(Shopify var0, Account var1, String var2, JsonArray var3, CompletableFuture var4, int var5, Object var6) {
      Throwable var17;
      label72: {
         CompletableFuture var10000;
         boolean var10001;
         switch (var5) {
            case 0:
               var2 = var1.lookupSession();
               if (var2.isBlank()) {
                  return CompletableFuture.completedFuture(false);
               }

               var3 = new JsonArray(var2);
               if (var3.isEmpty()) {
                  return CompletableFuture.completedFuture(false);
               }

               for(int var12 = 0; var12 < var3.size(); ++var12) {
                  try {
                     String var15 = var3.getString(var12);
                     Cookie var16 = ClientCookieDecoder.STRICT.decode(var15);
                     if (var16 != null && var16.name().equals("_secure_session_id")) {
                        var0.api.getCookies().put(var16);
                     }
                  } catch (Throwable var8) {
                     var0.logger.warn("Error parsing session state: {}", var8.getMessage());
                  }
               }

               try {
                  var10000 = var0.GETREQ("Validating Session", var0.api.accountConfirmPage(), new Integer[]{200, 302, 401}, new String[]{""});
                  if (!var10000.isDone()) {
                     CompletableFuture var7 = var10000;
                     return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$sessionLogon);
                  }
                  break;
               } catch (Throwable var10) {
                  var17 = var10;
                  var10001 = false;
                  break label72;
               }
            case 1:
               var10000 = var4;
               break;
            default:
               throw new IllegalArgumentException();
         }

         try {
            String var13 = (String)var10000.join();
            if (var13.contains("order-history") || var13.contains("/account/logout") || var13.endsWith("/password")) {
               return CompletableFuture.completedFuture(true);
            }
         } catch (Throwable var11) {
            var17 = var11;
            var10001 = false;
            break label72;
         }

         try {
            return CompletableFuture.completedFuture(false);
         } catch (Throwable var9) {
            var17 = var9;
            var10001 = false;
         }
      }

      Throwable var14 = var17;
      var0.logger.warn("Error validating session: {}", var14.getMessage());
      return CompletableFuture.completedFuture(false);
   }

   public CompletableFuture walletsSubmitShipping(String var1) {
      this.logger.info("Loading shipping... [API]");
      int var2 = 0;

      while(super.running && var2++ < Integer.MAX_VALUE) {
         CompletableFuture var7;
         CompletableFuture var9;
         try {
            HttpRequest var3 = this.api.calculateTaxesWallets(var1);
            var9 = Request.send(var3);
            if (!var9.isDone()) {
               var7 = var9;
               return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$walletsSubmitShipping);
            }

            HttpResponse var4 = (HttpResponse)var9.join();
            if (var4 != null) {
               if (var4.statusCode() != 200 && var4.statusCode() != 202) {
                  if (var4.statusCode() == 429 && this.api.getCookies().contains("_checkout_queue_token")) {
                     var9 = this.handleQueueNew();
                     if (!var9.isDone()) {
                        var7 = var9;
                        return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$walletsSubmitShipping);
                     }

                     var9.join();
                  } else {
                     this.logger.warn("Waiting to load shipping [API]: status: '{}'", var4.statusCode());
                     var9 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
                     if (!var9.isDone()) {
                        var7 = var9;
                        return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$walletsSubmitShipping);
                     }

                     var9.join();
                  }
               } else {
                  JsonObject var5 = var4.bodyAsJsonObject().getJsonObject("checkout");
                  JsonObject var6 = var5;
                  if (var5 != null) {
                     var6 = var5.getJsonObject("shipping_line");
                  }

                  if (var6 != null && !var6.isEmpty()) {
                     this.isShippingSubmittedWithWallets = true;
                     return CompletableFuture.completedFuture(var5.getJsonArray("line_items").encode());
                  }

                  this.logger.info("Refetching shipping rate [API]");
                  if (var2 > 5) {
                     var9 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
                     if (!var9.isDone()) {
                        var7 = var9;
                        return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$walletsSubmitShipping);
                     }

                     var9.join();
                  }
               }
            }
         } catch (Exception var8) {
            this.logger.error("Error loading shipping [API]: {}", var8.getMessage());
            var9 = VertxUtil.randomSleep(3000L);
            if (!var9.isDone()) {
               var7 = var9;
               return var7.exceptionally(Function.identity()).thenCompose(Shopify::async$walletsSubmitShipping);
            }

            var9.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture submitShipping(String var1) {
      byte var2 = 1;
      this.logger.info("Submitting shipping...");
      int var3 = 0;

      while(super.running && var3++ < Integer.MAX_VALUE) {
         CompletableFuture var11 = this.shippingForm(var1);
         CompletableFuture var9;
         if (!var11.isDone()) {
            var9 = var11;
            return var9.exceptionally(Function.identity()).thenCompose(Shopify::async$submitShipping);
         }

         MultiMap var4 = (MultiMap)var11.join();

         try {
            HttpRequest var5 = this.api.postShippingRate(var1);
            var11 = Request.send(var5, var4);
            if (!var11.isDone()) {
               var9 = var11;
               return var9.exceptionally(Function.identity()).thenCompose(Shopify::async$submitShipping);
            }

            HttpResponse var6 = (HttpResponse)var11.join();
            if (var6 != null) {
               if (var6.statusCode() != 302 && var6.statusCode() != 301) {
                  if (var6.statusCode() == 200) {
                     this.updateShippingRate(var6.bodyAsString());
                     this.updateCheckoutParams(var6.bodyAsString());
                     this.logger.info("Invalid previous shipping. Updating rates.");
                     if (var2 != 0) {
                        var2 = 0;
                        continue;
                     }
                  } else {
                     this.logger.warn("Submitting shipping: status: '{}'", var6.statusCode());
                     if (var3 < 2) {
                        continue;
                     }
                  }
               } else {
                  String var7 = var6.getHeader("location");
                  var11 = this.handleRedirect(var6, true);
                  if (!var11.isDone()) {
                     var9 = var11;
                     return var9.exceptionally(Function.identity()).thenCompose(Shopify::async$submitShipping);
                  }

                  Boolean var8 = (Boolean)var11.join();
                  if (var8 == null) {
                     return CompletableFuture.completedFuture(var7);
                  }

                  if (!var8) {
                     continue;
                  }

                  if (var7.contains("previous_step=shipping_method")) {
                     return CompletableFuture.completedFuture(var7);
                  }

                  this.logger.error("Unknown redirect! - " + var6);
               }

               var11 = VertxUtil.sleep((long)this.task.getRetryDelay());
               if (!var11.isDone()) {
                  var9 = var11;
                  return var9.exceptionally(Function.identity()).thenCompose(Shopify::async$submitShipping);
               }

               var11.join();
            }
         } catch (Throwable var10) {
            this.logger.error("Error submitting shipping: {}", var10.getMessage());
            var11 = super.randomSleep(3000);
            if (!var11.isDone()) {
               var9 = var11;
               return var9.exceptionally(Function.identity()).thenCompose(Shopify::async$submitShipping);
            }

            var11.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$genAcc(Shopify param0, Account param1, CompletableFuture param2, MultiMap param3, int param4, MultiMap param5, String param6, HttpRequest param7, HttpResponse param8, Throwable param9, int param10, Object param11) {
      // $FF: Couldn't be decompiled
   }

   public void setAttributes() {
      this.configureShippingRate();
      this.TEXTAREA_PARAMS.clear();
      this.api.getCookies().removeAnyMatch("cart");
      this.api.getCookies().removeAnyMatch("cart_sig");
      this.api.getCookies().put("_landing_page", "%2Fproducts%2F" + ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE), this.api.getSiteURL());
      this.api.getCookies().put("_orig_referrer", "", this.api.getSiteURL());
      this.api.getCookies().put("_shopify_sa_p", "", this.api.getSiteURL());
      this.api.getCookies().put("_shopify_country", "United+States", this.api.getSiteURL());
      this.api.getCookies().put("_shopify_sa_t", Utils.encodedDateISO(Instant.now()), this.api.getSiteURL());
      this.api.getCookies().put("_shopify_fs", Utils.encodedDateISO(instanceTime), this.api.getSiteURL());
      this.api.getCookies().put("GlobalE_Welcome_Data", "%7B%22showWelcome%22%3Afalse%7D", "kith.com");
      this.api.getCookies().put("GlobalE_CT_Data", "%7B%22CUID%22%3A%22561354664.541010022.583%22%2C%22CHKCUID%22%3Anull%7D", "kith.com");
      this.api.getCookies().put("GlobalE_SupportThirdPartCookies", "true", "kith.com");
      this.api.getCookies().put("GlobalE_Full_Redirect", "false", "kith.com");
      this.api.getCookies().put("GlobalE_Data", "%7B%22countryISO%22%3A%22US%22%2C%22currencyCode%22%3A%22USD%22%2C%22cultureCode%22%3A%22en-US%22%7D", "kith.com");
      this.api.getCookies().put("dynamic_checkout_shown_on_cart", "1", "kith.com");
      this.api.getCookies().put("acceptedCookies", "yes", "kith.com");
      this.size = null;
      this.authenticity = "";
      this.price = null;
      this.paymentToken = new CompletableFuture();
      this.prevTime = -1L;
      this.noProcessOnFirstTry = true;
      this.counter429 = 0;
      this.hasShippingAlreadySubmittedAPI = false;
      this.isShippingSubmittedWithWallets = false;
      this.api.isOOS = false;
      this.api.isFirstProcessingPageVisit = true;
      this.api.key = null;
      this.api.graphUnstable = false;
   }

   public CompletableFuture submitContact(String var1) {
      this.logger.info("Submitting contact info...");
      int var2 = 0;

      while(super.running && var2++ < Integer.MAX_VALUE) {
         MultiMap var3 = this.contactForm(var1);

         CompletableFuture var8;
         CompletableFuture var10;
         try {
            HttpRequest var4 = this.api.postContact(var1);
            var10 = Request.send(var4, var3);
            if (!var10.isDone()) {
               var8 = var10;
               return var8.exceptionally(Function.identity()).thenCompose(Shopify::async$submitContact);
            }

            HttpResponse var5 = (HttpResponse)var10.join();
            if (var5 != null) {
               if (var5.statusCode() != 302 && var5.statusCode() != 301) {
                  if (var5.statusCode() != 302) {
                     if (var5.statusCode() == 200) {
                        this.logger.error("Submitting contact info: err: '{}'", Utils.quickParseFirst(var5.bodyAsString(), FIELD_ERR_PATTERN));
                     } else {
                        this.logger.warn("Submitting contact info: status: '{}'", var5.statusCode());
                     }

                     if (var2 < 2) {
                        continue;
                     }
                  }
               } else {
                  String var6 = var5.getHeader("location");
                  var10 = this.handleRedirect(var5, true);
                  if (!var10.isDone()) {
                     var8 = var10;
                     return var8.exceptionally(Function.identity()).thenCompose(Shopify::async$submitContact);
                  }

                  Boolean var7 = (Boolean)var10.join();
                  if (var7 == null) {
                     return CompletableFuture.completedFuture(var6);
                  }

                  if (!var7 && var6.contains("stock_problem")) {
                     return CompletableFuture.completedFuture(var6);
                  }

                  if (!var7) {
                     continue;
                  }

                  if (var6.contains("previous_step=contact_information")) {
                     return CompletableFuture.completedFuture(var6);
                  }

                  this.logger.error("Unknown redirect! - " + var5);
               }
            }

            var10 = VertxUtil.sleep((long)this.task.getRetryDelay());
            if (!var10.isDone()) {
               var8 = var10;
               return var8.exceptionally(Function.identity()).thenCompose(Shopify::async$submitContact);
            }

            var10.join();
         } catch (Throwable var9) {
            this.logger.error("Error submitting contact info: {}", var9.getMessage());
            var10 = super.randomSleep(3000);
            if (!var10.isDone()) {
               var8 = var10;
               return var8.exceptionally(Function.identity()).thenCompose(Shopify::async$submitContact);
            }

            var10.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public MultiMap checkCaptchaForm() {
      MultiMap var1 = MultiMap.caseInsensitiveMultiMap();
      var1.add("_method", "patch");
      var1.add("checkout[email]", "53");
      return var1;
   }

   public CompletableFuture clearCart() {
      int var1 = 0;
      this.logger.info("Preparing preload...");

      while(super.running && var1++ < Integer.MAX_VALUE) {
         CompletableFuture var4;
         CompletableFuture var6;
         try {
            HttpRequest var2 = this.api.clearCart();
            var6 = Request.send(var2);
            if (!var6.isDone()) {
               var4 = var6;
               return var4.exceptionally(Function.identity()).thenCompose(Shopify::async$clearCart);
            }

            HttpResponse var3 = (HttpResponse)var6.join();
            if (var3 != null) {
               if (var3.statusCode() == 200) {
                  VertxUtil.sendSignal(this.api.getSiteURL());
                  return CompletableFuture.completedFuture((Object)null);
               }

               if (var3.statusCode() == 302 || var3.statusCode() == 301) {
                  var6 = this.handleRedirect(var3, false);
                  if (!var6.isDone()) {
                     var4 = var6;
                     return var4.exceptionally(Function.identity()).thenCompose(Shopify::async$clearCart);
                  }

                  var6.join();
               }

               this.logger.warn("Failed preparing preload: status: '{}'", var3.statusCode());
               var6 = VertxUtil.sleep((long)this.task.getRetryDelay());
               if (!var6.isDone()) {
                  var4 = var6;
                  return var4.exceptionally(Function.identity()).thenCompose(Shopify::async$clearCart);
               }

               var6.join();
            }
         } catch (Throwable var5) {
            this.logger.error("Failed preparing preload: {}", var5.getMessage());
            var6 = super.randomSleep(3000);
            if (!var6.isDone()) {
               var4 = var6;
               return var4.exceptionally(Function.identity()).thenCompose(Shopify::async$clearCart);
            }

            var6.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture fetchMeta() {
      int var1 = 0;
      this.logger.info("Getting details [BACKUP]");

      while(super.running && var1++ < Integer.MAX_VALUE) {
         CompletableFuture var4;
         CompletableFuture var6;
         try {
            HttpRequest var2 = this.api.meta();
            var6 = Request.send(var2);
            if (!var6.isDone()) {
               var4 = var6;
               return var4.exceptionally(Function.identity()).thenCompose(Shopify::async$fetchMeta);
            }

            HttpResponse var3 = (HttpResponse)var6.join();
            if (var3 != null) {
               if (var3.statusCode() == 200) {
                  this.api.checkpointClient = RealClientFactory.createChild(super.vertx, this.api.getWebClient());
                  return CompletableFuture.completedFuture(var3.bodyAsJsonObject());
               }

               if (var3.statusCode() == 302 || var3.statusCode() == 301) {
                  var6 = this.handleRedirect(var3, false);
                  if (!var6.isDone()) {
                     var4 = var6;
                     return var4.exceptionally(Function.identity()).thenCompose(Shopify::async$fetchMeta);
                  }

                  if (!(Boolean)var6.join()) {
                     continue;
                  }
               }

               if (var3.bodyAsString().contains("You do not have permission to access this website")) {
                  this.logger.warn("Proxy is permanently banned from the site.");
                  this.api.rotateProxy();
               } else {
                  this.logger.warn("Getting details [BACKUP]: status: '{}'", var3.statusCode());
               }

               var6 = VertxUtil.sleep((long)this.task.getRetryDelay());
               if (!var6.isDone()) {
                  var4 = var6;
                  return var4.exceptionally(Function.identity()).thenCompose(Shopify::async$fetchMeta);
               }

               var6.join();
            }
         } catch (Throwable var5) {
            this.logger.error("Getting details [BACKUP]: {}", var5.getMessage());
            var6 = super.randomSleep(3000);
            if (!var6.isDone()) {
               var4 = var6;
               return var4.exceptionally(Function.identity()).thenCompose(Shopify::async$fetchMeta);
            }

            var6.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture shippingAndBillingPostAlreadyCarted() {
      this.logger.info("Generating checkout [API]...");
      int var1 = 0;

      while(super.running && var1++ < Integer.MAX_VALUE) {
         CompletableFuture var4;
         CompletableFuture var6;
         try {
            HttpRequest var2 = this.api.atcWallets();
            var6 = Request.send(var2, this.contactAPIAndCartForm(true));
            if (!var6.isDone()) {
               var4 = var6;
               return var4.exceptionally(Function.identity()).thenCompose(Shopify::async$shippingAndBillingPostAlreadyCarted);
            }

            HttpResponse var3 = (HttpResponse)var6.join();
            if (var3 != null) {
               if (var3.statusCode() == 200 || var3.statusCode() == 201) {
                  return CompletableFuture.completedFuture(var3.bodyAsJsonObject().getJsonObject("checkout").getString("web_url"));
               }

               if (var3.statusCode() == 202) {
                  if (var3.bodyAsString().contains(this.task.getProfile().getAddress1())) {
                     return CompletableFuture.completedFuture(var3.bodyAsJsonObject().getJsonObject("checkout").getString("web_url"));
                  }
               } else if (var3.statusCode() != 409 || var1 >= 5) {
                  if (var3.statusCode() == 429 && this.api.getCookies().contains("_checkout_queue_token")) {
                     var6 = this.handleQueueNew();
                     if (!var6.isDone()) {
                        var4 = var6;
                        return var4.exceptionally(Function.identity()).thenCompose(Shopify::async$shippingAndBillingPostAlreadyCarted);
                     }

                     var6.join();
                  } else if (var3.statusCode() == 422) {
                     this.logger.warn("Retrying generating checkout [API]: status: '{}'", var3.statusCode());
                     var6 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
                     if (!var6.isDone()) {
                        var4 = var6;
                        return var4.exceptionally(Function.identity()).thenCompose(Shopify::async$shippingAndBillingPostAlreadyCarted);
                     }

                     var6.join();
                  } else {
                     this.logger.warn("Retrying generating checkout [API]: status: '{}'", var3.statusCode());
                     var6 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
                     if (!var6.isDone()) {
                        var4 = var6;
                        return var4.exceptionally(Function.identity()).thenCompose(Shopify::async$shippingAndBillingPostAlreadyCarted);
                     }

                     var6.join();
                  }
               }
            }
         } catch (Throwable var5) {
            this.logger.error("Error generating checkout [API]: {}", var5.getMessage());
            var6 = super.randomSleep(3000);
            if (!var6.isDone()) {
               var4 = var6;
               return var4.exceptionally(Function.identity()).thenCompose(Shopify::async$shippingAndBillingPostAlreadyCarted);
            }

            var6.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture getPaymentStatus(String var1) {
      this.logger.info("Checking order...");
      int var2 = 0;

      while(super.running && var2++ < Integer.MAX_VALUE) {
         CompletableFuture var6;
         CompletableFuture var8;
         try {
            HttpRequest var3 = this.api.getProcessingRedirect(var1);
            var8 = Request.send(var3);
            if (!var8.isDone()) {
               var6 = var8;
               return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$getPaymentStatus);
            }

            HttpResponse var4 = (HttpResponse)var8.join();
            if (var4 != null) {
               if (var4.statusCode() == 200) {
                  String var5 = var4.bodyAsString();
                  this.updateGateway(var5);
                  this.updateCheckoutParams(var5);
                  this.updatePrice(var5);
                  return CompletableFuture.completedFuture(var5);
               }

               if (var4.statusCode() == 302 || var4.statusCode() == 301) {
                  var8 = this.handleRedirect(var4, false);
                  if (!var8.isDone()) {
                     var6 = var8;
                     return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$getPaymentStatus);
                  }

                  var8.join();
               }

               if ((var4.statusCode() == 302 || var4.statusCode() == 301) && var4.getHeader("location").contains("thank_you")) {
                  var1 = var4.getHeader("location");
               }

               this.logger.warn("Waiting for order status: status: '{}'", var4.statusCode());
               var8 = VertxUtil.sleep((long)this.task.getRetryDelay());
               if (!var8.isDone()) {
                  var6 = var8;
                  return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$getPaymentStatus);
               }

               var8.join();
            }
         } catch (Throwable var7) {
            this.logger.error("Error checking payment status: {}", var7.getMessage());
            var8 = super.randomSleep(3000);
            if (!var8.isDone()) {
               var6 = var8;
               return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$getPaymentStatus);
            }

            var8.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public JsonObject contactAPIAndCartForm(boolean var1) {
      JsonObject var2 = new JsonObject();
      JsonObject var3 = (new JsonObject()).put("is_upstream_button", false).put("page_type", "product").put("presentment_currency", this.api.getCookies().getCookieValue("cart_currency")).put("secret", true).put("wallet_name", "Checkout");
      if (var1) {
         var3.put("cart_token", this.api.getCookies().getCookieValue("cart"));
      } else {
         JsonObject var4 = (new JsonObject()).put("variant_id", Long.parseLong(this.instanceSignal)).put("quantity", 1);
         if (this.propertiesPair != null) {
            var4.put("properties", (new JsonObject()).put((String)this.propertiesPair.first, this.propertiesPair.second));
         }

         var3.put("line_items", (new JsonArray()).add(var4));
      }

      var2.put("checkout", var3);
      return var2;
   }

   public static CompletableFuture async$fetchMeta(Shopify param0, int param1, HttpRequest param2, CompletableFuture param3, HttpResponse param4, Throwable param5, int param6, Object param7) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$checkNewQueueThrottle(Shopify param0, int param1, HttpRequest param2, CompletableFuture param3, HttpResponse param4, Throwable param5, int param6, Object param7) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$login(Shopify param0, AccountController param1, CompletableFuture param2, Account param3, MultiMap param4, int param5, MultiMap param6, String param7, HttpRequest param8, HttpResponse param9, Throwable param10, int param11, Object param12) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$attemptGenCheckoutUrl(Shopify param0, int param1, int param2, HttpRequest param3, CompletableFuture param4, HttpResponse param5, String param6, REDIRECT_STATUS param7, Throwable param8, int param9, Object param10) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture safeCompleteRequest(HttpRequest var1, String var2) {
      int var3 = 0;

      while(var3++ < Integer.MAX_VALUE && this.api.getWebClient().isActive()) {
         CompletableFuture var6;
         CompletableFuture var8;
         try {
            var8 = Request.send(var1);
            if (!var8.isDone()) {
               var6 = var8;
               return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$safeCompleteRequest);
            }

            HttpResponse var4 = (HttpResponse)var8.join();
            if (var4 != null) {
               int var5 = var4.statusCode();
               if (var5 == 200 || var4.statusCode() == 302 || var4.statusCode() == 301) {
                  return CompletableFuture.completedFuture(var4);
               }

               if (var5 != 202 && var3 > 3) {
                  this.logger.error("Failed " + var2 + ": status: {}", var5);
                  var8 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
                  if (!var8.isDone()) {
                     var6 = var8;
                     return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$safeCompleteRequest);
                  }

                  var8.join();
               }
            }
         } catch (Throwable var7) {
            if (!var7.getMessage().contains("Connection was closed")) {
               this.logger.error("Failed " + var2 + ": status: {}", var7.getMessage());
            }

            var8 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
            if (!var8.isDone()) {
               var6 = var8;
               return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$safeCompleteRequest);
            }

            var8.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture checkQueueThrottle() {
      this.logger.info("Checking queue...");
      int var1 = 0;

      while(super.running && var1++ < Integer.MAX_VALUE) {
         CompletableFuture var4;
         CompletableFuture var6;
         try {
            HttpRequest var2 = this.api.fetchQueueUrl();
            var6 = Request.send(var2);
            if (!var6.isDone()) {
               var4 = var6;
               return var4.exceptionally(Function.identity()).thenCompose(Shopify::async$checkQueueThrottle);
            }

            HttpResponse var3 = (HttpResponse)var6.join();
            if (var3 != null) {
               if (var3.statusCode() == 200) {
                  return CompletableFuture.completedFuture(true);
               }

               this.logger.warn("Waiting to check queue: status: '{}'", var3.statusCode());
               var6 = VertxUtil.randomSleep(3000L);
               if (!var6.isDone()) {
                  var4 = var6;
                  return var4.exceptionally(Function.identity()).thenCompose(Shopify::async$checkQueueThrottle);
               }

               var6.join();
            }
         } catch (Throwable var5) {
            this.logger.error("Error checking queue: {}", var5.getMessage());
            var6 = super.randomSleep(3000);
            if (!var6.isDone()) {
               var4 = var6;
               return var4.exceptionally(Function.identity()).thenCompose(Shopify::async$checkQueueThrottle);
            }

            var6.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$getShippingPage(Shopify param0, String param1, int param2, HttpRequest param3, CompletableFuture param4, HttpResponse param5, Boolean param6, Throwable param7, int param8, Object param9) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture processPaymentFakeToken(String var1) {
      this.logger.info("Adjusting for restock...");
      int var2 = 0;

      while(super.running && var2++ < Integer.MAX_VALUE) {
         CompletableFuture var6;
         CompletableFuture var8;
         try {
            var8 = this.fakeProcessingForm(var1);
            if (!var8.isDone()) {
               var6 = var8;
               return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$processPaymentFakeToken);
            }

            MultiMap var3 = (MultiMap)var8.join();
            HttpRequest var4 = this.api.postPayment(var1);
            var8 = Request.send(var4, var3);
            if (!var8.isDone()) {
               var6 = var8;
               return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$processPaymentFakeToken);
            }

            HttpResponse var5 = (HttpResponse)var8.join();
            if (var5 != null) {
               if (var5.statusCode() == 200) {
                  return CompletableFuture.completedFuture((Object)null);
               }

               if (var5.statusCode() != 302 && var5.statusCode() != 301) {
                  this.logger.warn("Error adjusting for restock: status: '{}'", var5.statusCode());
               } else {
                  var8 = this.handleRedirect(var5, false);
                  if (!var8.isDone()) {
                     var6 = var8;
                     return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$processPaymentFakeToken);
                  }

                  var8.join();
               }

               var8 = VertxUtil.sleep((long)this.task.getRetryDelay());
               if (!var8.isDone()) {
                  var6 = var8;
                  return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$processPaymentFakeToken);
               }

               var8.join();
            }
         } catch (Exception var7) {
            if (!var7.getMessage().equals("io.vertx.core.VertxException: Connection was closed")) {
               this.logger.error("Error adjusting for restock: {}", var7.getMessage());
               var8 = super.randomSleep(3000);
               if (!var8.isDone()) {
                  var6 = var8;
                  return var6.exceptionally(Function.identity()).thenCompose(Shopify::async$processPaymentFakeToken);
               }

               var8.join();
            }
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture walletsGenCheckout() {
      this.logger.info("Generating checkout [API-CART]...");
      int var1 = 0;

      while(super.running && var1++ < Integer.MAX_VALUE) {
         CompletableFuture var4;
         CompletableFuture var6;
         try {
            HttpRequest var2 = this.api.atcWallets();
            var6 = Request.send(var2, this.walletsGenCheckoutUrlForm());
            if (!var6.isDone()) {
               var4 = var6;
               return var4.exceptionally(Function.identity()).thenCompose(Shopify::async$walletsGenCheckout);
            }

            HttpResponse var3 = (HttpResponse)var6.join();
            if (var3 != null) {
               if (var3.statusCode() == 200 || var3.statusCode() == 201) {
                  return CompletableFuture.completedFuture(var3.bodyAsJsonObject().getJsonObject("checkout").getString("web_url"));
               }

               if (var3.statusCode() == 202) {
                  if (var3.bodyAsString().contains(this.task.getProfile().getAddress1())) {
                     return CompletableFuture.completedFuture(var3.bodyAsJsonObject().getJsonObject("checkout").getString("web_url"));
                  }
               } else if (var3.statusCode() != 409 || var1 >= 5) {
                  if (var3.statusCode() == 429 && this.api.getCookies().contains("_checkout_queue_token")) {
                     var6 = this.handleQueueNew();
                     if (!var6.isDone()) {
                        var4 = var6;
                        return var4.exceptionally(Function.identity()).thenCompose(Shopify::async$walletsGenCheckout);
                     }

                     var6.join();
                  } else {
                     this.logger.warn("Retrying generating checkout [API-CART]: status: '{}'", var3.statusCode() == 422 ? "OOS" : var3.statusCode());
                     var6 = VertxUtil.sleep((long)this.task.getRetryDelay());
                     if (!var6.isDone()) {
                        var4 = var6;
                        return var4.exceptionally(Function.identity()).thenCompose(Shopify::async$walletsGenCheckout);
                     }

                     var6.join();
                  }
               }
            }
         } catch (Throwable var5) {
            this.logger.error("Error generating checkout [API-CART]: {}", var5.getMessage());
            var6 = super.randomSleep(3000);
            if (!var6.isDone()) {
               var4 = var6;
               return var4.exceptionally(Function.identity()).thenCompose(Shopify::async$walletsGenCheckout);
            }

            var6.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$walletsGenCheckout(Shopify param0, int param1, HttpRequest param2, CompletableFuture param3, HttpResponse param4, Throwable param5, int param6, Object param7) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$handleRedirect(Shopify var0, String var1, int var2, REDIRECT_STATUS var3, CompletableFuture var4, int var5, String var6, CaptchaToken var7, int var8, Object var9) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture initHarvesters() {
      if (harvesterCFs.length == 0) {
         return CompletableFuture.completedFuture((Object)null);
      } else {
         CompletionStage var10000 = Vertx.currentContext().owner().sharedData().getLockWithTimeout("harvesterinitshopify", 1800000L).toCompletionStage();
         if (!var10000.toCompletableFuture().isDone()) {
            CompletionStage var3 = var10000;
            return var3.exceptionally(Function.identity()).thenCompose(Shopify::async$initHarvesters).toCompletableFuture();
         } else {
            Lock var0 = (Lock)var10000.toCompletableFuture().join();

            for(int var1 = 0; var1 < HARVESTERS.length && !isHarvesterInited.get(); ++var1) {
               harvesterCFs[var1] = HARVESTERS[var1].start();
            }

            CompletableFuture var4 = CompletableFuture.allOf(harvesterCFs);
            if (!var4.isDone()) {
               CompletableFuture var2 = var4;
               return var2.exceptionally(Function.identity()).thenCompose(Shopify::async$initHarvesters);
            } else {
               var4.join();
               isHarvesterInited.set(true);
               var0.release();
               return CompletableFuture.completedFuture((Object)null);
            }
         }
      }
   }

   public static CompletableFuture async$shippingForm(Shopify var0, String var1, MultiMap var2, MultiMap var3, String var4, CompletableFuture var5, int var6, Object var7) {
      MultiMap var10000;
      String var10001;
      CompletableFuture var10002;
      switch (var6) {
         case 0:
            var2 = MultiMap.caseInsensitiveMultiMap();
            var2.add("_method", "patch");
            var2.add("authenticity_token", var0.authenticity);
            var2.add("previous_step", "shipping_method");
            var2.add("step", "payment_method");
            var10000 = var2;
            var10001 = "checkout[shipping_rate][id]";
            var10002 = var0.shippingRate;
            if (!var10002.isDone()) {
               CompletableFuture var10 = var10002;
               String var9 = "checkout[shipping_rate][id]";
               return var10.exceptionally(Function.identity()).thenCompose(Shopify::async$shippingForm);
            }
            break;
         case 1:
            var10000 = var3;
            var10001 = var4;
            var10002 = var5;
            break;
         default:
            throw new IllegalArgumentException();
      }

      var10000.add(var10001, ((ShippingRateSupplier)var10002.join()).get());
      Iterator var8 = var0.TEXTAREA_PARAMS.iterator();

      while(var8.hasNext()) {
         var4 = (String)var8.next();
         var2.add(var4, "");
      }

      if (!var0.TEXTAREA_PARAMS.isEmpty()) {
         var2.add(var1 + "-count", "" + var0.TEXTAREA_PARAMS.size());
         var2.add(var1 + "-count", "fs_count");
      }

      var2.add("checkout[client_details][browser_width]", var0.BROWSER_WIDTH);
      var2.add("checkout[client_details][browser_height]", var0.BROWSER_HEIGHT);
      var2.add("checkout[client_details][javascript_enabled]", "1");
      var2.add("checkout[client_details][color_depth]", "24");
      var2.add("checkout[client_details][java_enabled]", "false");
      var2.add("checkout[client_details][browser_tz]", "" + TIMEZONE_OFFSET);
      return CompletableFuture.completedFuture(var2);
   }

   public static CompletableFuture async$aiAPIReq(Shopify param0, HttpRequest param1, JsonObject param2, int param3, CompletableFuture param4, int param5, Object param6) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture genCheckoutURL(boolean var1) {
      int var2 = 0;
      this.logger.info("Generating checkout URL...");

      while(super.running && var2++ < Integer.MAX_VALUE) {
         CompletableFuture var5;
         CompletableFuture var7;
         try {
            HttpRequest var3 = this.api.emptyCheckout((boolean)var1);
            HttpResponse var8;
            if (var1 != 0) {
               var7 = Request.send(var3, Buffer.buffer("{}"));
               if (!var7.isDone()) {
                  var5 = var7;
                  return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$genCheckoutURL);
               }

               var8 = (HttpResponse)var7.join();
            } else {
               var7 = Request.send(var3);
               if (!var7.isDone()) {
                  var5 = var7;
                  return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$genCheckoutURL);
               }

               var8 = (HttpResponse)var7.join();
            }

            HttpResponse var4 = var8;
            if (var4 != null) {
               if (var4.statusCode() == 302 || var4.statusCode() == 301) {
                  var7 = this.handleRedirect(var4, true);
                  if (!var7.isDone()) {
                     var5 = var7;
                     return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$genCheckoutURL);
                  }

                  if ((Boolean)var7.join()) {
                     return CompletableFuture.completedFuture(var4.getHeader("location"));
                  }
               }

               if (var4.statusCode() != 302) {
                  this.logger.warn("Failed generating checkout URL: status: '{}'", var4.statusCode());
                  var7 = VertxUtil.sleep((long)this.task.getRetryDelay());
                  if (!var7.isDone()) {
                     var5 = var7;
                     return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$genCheckoutURL);
                  }

                  var7.join();
               }
            }
         } catch (Throwable var6) {
            this.logger.error("Failed generating checkout URL: {}", var6.getMessage());
            var7 = super.randomSleep(3000);
            if (!var7.isDone()) {
               var5 = var7;
               return var5.exceptionally(Function.identity()).thenCompose(Shopify::async$genCheckoutURL);
            }

            var7.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }
}
