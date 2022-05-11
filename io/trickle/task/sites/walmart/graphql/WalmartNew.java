package io.trickle.task.sites.walmart.graphql;

import io.netty.handler.codec.http.cookie.ClientCookieDecoder;
import io.netty.handler.codec.http.cookie.Cookie;
import io.trickle.account.Account;
import io.trickle.account.AccountController;
import io.trickle.core.Controller;
import io.trickle.core.Engine;
import io.trickle.core.VertxSingleton;
import io.trickle.core.actor.TaskActor;
import io.trickle.task.Task;
import io.trickle.task.antibot.impl.px.PerimeterX;
import io.trickle.task.antibot.impl.px.payload.captcha.DesktopPXAPI3;
import io.trickle.task.sites.Site;
import io.trickle.task.sites.walmart.Mode;
import io.trickle.task.sites.walmart.util.PaymentToken;
import io.trickle.util.Utils;
import io.trickle.util.analytics.Analytics;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.trickle.webclient.CookieJar;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.impl.ConcurrentHashSet;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.impl.HttpRequestImpl;
import io.vertx.ext.web.codec.BodyCodec;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import org.apache.logging.log4j.Logger;

public class WalmartNew extends TaskActor {
   public boolean queue;
   public static Pattern PAYMENT_PREFERENCE_PATTERN = Pattern.compile("\"paymentPreferenceId\":\"(.*?)\"");
   public long sessionTimestamp = 0L;
   public static Predicate DESKTOP_CHECKOUT_FLAG = WalmartNew::lambda$static$1;
   public static Pattern EXPECTED_TIME_PATTERN = Pattern.compile("\"expectedTurnTimeUnixTimestamp\":([0-9]*)");
   public CookieJar sessionCookies;
   public static Pattern PAYMENT_ID_PATTERN = Pattern.compile("CreditCard\".*?\"id\":\"(.*?)\"");
   public static Pattern ADDRESS_ID_PATTERN = Pattern.compile("newAddress.*?\"id\":\"(.*?)\"", 32);
   public static ConcurrentHashSet GLOBAL_COOKIES = new ConcurrentHashSet();
   public static Pattern TICKET_PATTERN = Pattern.compile("\"url\":\"(.*?)\"");
   public int desiredUnitPrice = -1;
   public static Pattern APPL_VER_PATTERN = Pattern.compile("appVersion\":\"(.*?)\"");
   public boolean buyNow;
   public String lineItemId;
   public static Pattern TENDER_PATTERN = Pattern.compile("\"tenderPlanId\":\"(.*?)\"");
   public PaymentToken token;
   public String lastReason = "";
   public String instanceSignal;
   public static Pattern CART_ID_PATTERN = Pattern.compile("Cart\".*?\"id\":\"(.*?)\"");
   public static Pattern ID_PATTERN = Pattern.compile("\"id\":\"(.*?-.*?-.*?-.*?-.*?)\"");
   public API api;
   public static Pattern ADDRESS_PATTERN = Pattern.compile("\"addressLineOne\":\".*?\"");
   public long keepAliveWorker;
   public static Pattern REFRESH_TIME_PATTERN = Pattern.compile("\"nextRefreshUnixTimestamp\":([0-9]*)");
   public Task task;
   public static Pattern PRICE_PATTERN = Pattern.compile("subTotal.*?\"value\":([0-9]*)");
   public static Pattern STORE_ID_PATTERN = Pattern.compile("storeId\":\"([0-9]*)");
   public static Pattern KEYWORD_PID_PATTERN = Pattern.compile("/([0-9][0-9][0-9]*)");
   public static Pattern PRODUCTS_HOME_PATTERN = Pattern.compile("canonicalUrl\":\"/ip/(.*?/[0-9]*)");
   public Mode mode;
   public static Predicate MOBILE_CHECKOUT_FLAG = WalmartNew::lambda$static$0;

   public static CompletableFuture async$execute(WalmartNew param0, String param1, Function param2, Supplier param3, Object param4, HttpRequest param5, CompletableFuture param6, HttpResponse param7, int param8, int param9, Optional param10, String param11, Throwable param12, int param13, Object param14) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$doPost(WalmartNew param0, String param1, Supplier param2, Object param3, Integer param4, String[] param5, HttpRequest param6, CompletableFuture param7, HttpResponse param8, String param9, int param10, String param11, int param12, Throwable param13, int param14, Object param15) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture submitBilling() {
      try {
         API var10000 = this.api;
         Function var10003 = quickFunctionParse(PAYMENT_ID_PATTERN, 200);
         API var10004 = this.api;
         Objects.requireNonNull(var10004);
         CompletableFuture var10001 = this.execute("Submitting billing", var10003, (Supplier)(var10004::submitBilling), (Object)this.api.getBillingForm(this.token));
         if (!var10001.isDone()) {
            CompletableFuture var3 = var10001;
            API var2 = var10000;
            return var3.exceptionally(Function.identity()).thenCompose(WalmartNew::async$submitBilling);
         } else {
            var10000.paymentId = (String)var10001.join();
            return CompletableFuture.completedFuture(!this.api.paymentId.isBlank());
         }
      } catch (Throwable var4) {
         this.logger.error("Error updating payment id: {}", var4.getMessage());
         return CompletableFuture.failedFuture(new Exception("payment_update_failed"));
      }
   }

   public CompletableFuture doPost(String var1, Supplier var2, Object var3, Integer var4, String... var5) {
      this.logger.info(var1);

      while(super.running) {
         CompletableFuture var11;
         CompletableFuture var10000;
         try {
            HttpRequest var6 = (HttpRequest)var2.get();
            var10000 = Request.send(var6, var3);
            if (!var10000.isDone()) {
               var11 = var10000;
               return var11.exceptionally(Function.identity()).thenCompose(WalmartNew::async$doPost);
            }

            HttpResponse var7 = (HttpResponse)var10000.join();
            if (var7 == null) {
               var10000 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
               if (!var10000.isDone()) {
                  var11 = var10000;
                  return var11.exceptionally(Function.identity()).thenCompose(WalmartNew::async$doPost);
               }

               var10000.join();
            } else {
               String var8;
               int var9;
               if (var7.statusCode() >= 300 && var7.statusCode() < 400) {
                  var8 = var7.getHeader("location");
                  var9 = var5 != null && !Utils.containsAllWords(var8, var5) ? 0 : 1;
               } else {
                  var8 = var7.bodyAsString();
                  var9 = var5 != null && !Utils.containsAllWords(var8, var5) ? 0 : 1;
               }

               if ((var4 == null || var7.statusCode() == var4) && var9 != 0) {
                  return CompletableFuture.completedFuture(var8);
               }

               String var10 = Util.parseErrorMessage(var8);
               if (var10 == null) {
                  int var14 = var7.statusCode();
                  var10 = "" + var14 + var7.statusMessage();
               }

               this.logger.warn("Failed {}: '{}'", var1.toLowerCase(Locale.ROOT), var10);
               var10000 = this.api.handleBadResponse(var7, Request.getURI(var6));
               if (!var10000.isDone()) {
                  var11 = var10000;
                  return var11.exceptionally(Function.identity()).thenCompose(WalmartNew::async$doPost);
               }

               byte var13 = (Boolean)var10000.join();
               if (var13 == 0) {
                  var10000 = VertxUtil.sleep((long)this.task.getMonitorDelay());
                  if (!var10000.isDone()) {
                     var11 = var10000;
                     return var11.exceptionally(Function.identity()).thenCompose(WalmartNew::async$doPost);
                  }

                  var10000.join();
               }
            }
         } catch (Throwable var12) {
            this.logger.error("Error {}: {}", var1.toLowerCase(Locale.ROOT), var12.getMessage());
            if (this.logger.isDebugEnabled()) {
               this.logger.debug(var12);
            }

            var10000 = super.randomSleep(this.task.getRetryDelay());
            if (!var10000.isDone()) {
               var11 = var10000;
               return var11.exceptionally(Function.identity()).thenCompose(WalmartNew::async$doPost);
            }

            var10000.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$run(WalmartNew param0, CompletableFuture param1, int param2, Function param3, Function param4, int param5, int param6, int param7, Object param8) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$submitShipping(WalmartNew var0, CompletableFuture var1, String var2, JsonObject var3, JsonObject var4, int var5, Object var6) {
      CompletableFuture var10000;
      label41: {
         API var10002;
         CompletableFuture var9;
         switch (var5) {
            case 0:
               if (var0.api.addressId != null && !var0.api.addressId.isBlank() && var0.api.accessPointId != null && !var0.api.accessPointId.isBlank()) {
                  return CompletableFuture.completedFuture(true);
               }

               var10002 = var0.api;
               Objects.requireNonNull(var10002);
               var10000 = var0.doPost("Submitting shipping", var10002::getPCID, var0.api.PCIDForm(), 200, "newAddress");
               if (!var10000.isDone()) {
                  var9 = var10000;
                  return var9.exceptionally(Function.identity()).thenCompose(WalmartNew::async$submitShipping);
               }
               break;
            case 1:
               var10000 = var1;
               break;
            case 2:
               var10000 = var1;
               break label41;
            default:
               throw new IllegalArgumentException();
         }

         String var7 = (String)var10000.join();
         JsonObject var8 = (new JsonObject(var7)).getJsonObject("data").getJsonObject("createAccountAddress").getJsonObject("newAddress");
         if (!var8.containsKey("id")) {
            return CompletableFuture.completedFuture(false);
         }

         var0.api.addressId = var8.getString("id");
         if (var8.containsKey("accessPoint")) {
            var3 = var8.getJsonObject("accessPoint");
            if (var3.containsKey("id")) {
               var0.api.accessPointId = var3.getString("id");
               var10002 = var0.api;
               Objects.requireNonNull(var10002);
               var10000 = var0.doPost("Submitting fulfilment", var10002::fulfillment, var0.api.fulfillmentBody(), 200, "\"intent\":\"SHIPPING\"");
               if (!var10000.isDone()) {
                  var9 = var10000;
                  return var9.exceptionally(Function.identity()).thenCompose(WalmartNew::async$submitShipping);
               }
               break label41;
            }
         }

         return CompletableFuture.failedFuture(new Exception("shipping_update_failed"));
      }

      var10000.join();
      return CompletableFuture.completedFuture(true);
   }

   public void reset() {
      this.api.addressId = null;
      this.api.accessPointId = null;
      this.api.paymentId = null;
   }

   public CompletableFuture execute(String var1, Function var2, Supplier var3, Object var4) {
      this.logger.info(var1);

      while(super.running) {
         CompletableFuture var10;
         CompletableFuture var10000;
         try {
            HttpRequest var6 = (HttpRequest)var3.get();
            HttpResponse var5;
            if (((HttpRequestImpl)var6).method().equals(HttpMethod.POST)) {
               var10000 = Request.send(var6, var4);
               if (!var10000.isDone()) {
                  var10 = var10000;
                  return var10.exceptionally(Function.identity()).thenCompose(WalmartNew::async$execute);
               }

               var5 = (HttpResponse)var10000.join();
            } else {
               var10000 = Request.send(var6);
               if (!var10000.isDone()) {
                  var10 = var10000;
                  return var10.exceptionally(Function.identity()).thenCompose(WalmartNew::async$execute);
               }

               var5 = (HttpResponse)var10000.join();
            }

            if (var5 != null) {
               int var7 = var5.statusCode();
               if (var7 != 412 && var7 != 444 && var7 != 307) {
                  Optional var12 = Optional.ofNullable(var2.apply(var5));
                  if (var12.isPresent()) {
                     return CompletableFuture.completedFuture(var12.get());
                  }

                  String var9 = Util.parseErrorMessage(var5.bodyAsString());
                  if (var9 == null) {
                     int var13 = var5.statusCode();
                     var9 = "" + var13 + var5.statusMessage();
                  }

                  this.logger.warn("Failed {}: '{}'", var1.toLowerCase(Locale.ROOT), var9);
                  var10000 = VertxUtil.randomSleep((long)this.task.getMonitorDelay());
                  if (!var10000.isDone()) {
                     var10 = var10000;
                     return var10.exceptionally(Function.identity()).thenCompose(WalmartNew::async$execute);
                  }

                  var10000.join();
               } else {
                  this.logger.info("Failed {}. Blocked!", var1.toLowerCase(Locale.ROOT));
                  var10000 = this.api.handleBadResponse(var5, Request.getURI(var6));
                  if (!var10000.isDone()) {
                     var10 = var10000;
                     return var10.exceptionally(Function.identity()).thenCompose(WalmartNew::async$execute);
                  }

                  byte var8 = (Boolean)var10000.join();
                  if (var8 == 0) {
                     var10000 = VertxUtil.sleep((long)this.task.getMonitorDelay());
                     if (!var10000.isDone()) {
                        var10 = var10000;
                        return var10.exceptionally(Function.identity()).thenCompose(WalmartNew::async$execute);
                     }

                     var10000.join();
                  }
               }
            } else {
               var10000 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
               if (!var10000.isDone()) {
                  var10 = var10000;
                  return var10.exceptionally(Function.identity()).thenCompose(WalmartNew::async$execute);
               }

               var10000.join();
            }
         } catch (Throwable var11) {
            this.logger.error("Error " + var1.toLowerCase(Locale.ROOT) + ": {}", var11.getMessage());
            if (this.logger.isDebugEnabled()) {
               this.logger.debug(var11);
            }

            var10000 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
            if (!var10000.isDone()) {
               var10 = var10000;
               return var10.exceptionally(Function.identity()).thenCompose(WalmartNew::async$execute);
            }

            var10000.join();
         }
      }

      return CompletableFuture.failedFuture(new Exception("Failed to execute " + var1));
   }

   public String lambda$checkout$7(HttpResponse var1) {
      String var2 = var1.bodyAsString();
      if (Utils.containsIgnoreCase(var2, "created")) {
         return this.api.loggedIn ? Utils.quickParseFirst(var2, TENDER_PATTERN) : "";
      } else {
         return null;
      }
   }

   public static CompletableFuture async$waitForQueue(WalmartNew param0, String param1, HttpRequest param2, CompletableFuture param3, JsonObject param4, HttpResponse param5, int param6, Throwable param7, int param8, Object param9) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture checkoutBuyNow() {
      CompletableFuture var10000 = this.buyNow();
      if (!var10000.isDone()) {
         CompletableFuture var2 = var10000;
         return var2.exceptionally(Function.identity()).thenCompose(WalmartNew::async$checkoutBuyNow);
      } else {
         String var1 = (String)var10000.join();
         if (var1 != null && !var1.isBlank()) {
            this.lineItemId = var1.split("\"itemIds\":\\[\"")[1].split("\"")[0];
            this.api.contractId = var1.split("\"id\":\"")[1].split("\"")[0];
         }

         return this.processBuyNow();
      }
   }

   public CompletableFuture updateOrCreateSession() {
      try {
         if (this.sessionCookies != null) {
            this.sessionCookies.clear();
         }

         Function var1 = WalmartNew::lambda$updateOrCreateSession$9;
         CompletableFuture var10000;
         CompletableFuture var3;
         if (this.mode.equals(Mode.MOBILE)) {
            String var2 = this.task.getKeywords().length > 2 ? this.task.getKeywords()[2] : "https://www.walmart.com/ip/Mario-Kart-8-Deluxe-Edition-Nintendo-Switch/55432571";
            var10000 = this.execute("Configuring...", var1, (Object)((WalmartNewAPIMobile)this.api).terraFirmaForm(Utils.quickParseFirst(var2, KEYWORD_PID_PATTERN)), (Supplier)(this::lambda$updateOrCreateSession$10));
            if (!var10000.isDone()) {
               var3 = var10000;
               return var3.exceptionally(Function.identity()).thenCompose(WalmartNew::async$updateOrCreateSession);
            }

            var10000.join();
         } else {
            API var10004 = this.api;
            Objects.requireNonNull(var10004);
            var10000 = this.execute("Configuring...", var1, (Object)null, (Supplier)(var10004::getCheckoutPage));
            if (!var10000.isDone()) {
               var3 = var10000;
               return var3.exceptionally(Function.identity()).thenCompose(WalmartNew::async$updateOrCreateSession);
            }

            var10000.join();
         }

         this.sessionTimestamp = System.currentTimeMillis();
         this.sessionCookies = new CookieJar(this.api.getWebClient().cookieStore());
         this.sessionCookies.get(true, ".walmart.com", "/").forEach(WalmartNew::lambda$updateOrCreateSession$11);
         this.fetchGlobalCookies();
         this.api.getWebClient().cookieStore().putFromOther(this.sessionCookies);
         this.logger.info("Updated session...");
      } catch (Throwable var4) {
         this.logger.error("Error occurred updating session: {}", var4.getMessage());
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static String lambda$run$3(HttpResponse var0) {
      Boolean var1 = true;
      return var1 != null && var0.bodyAsString().contains("pageProps") ? var0.bodyAsString() : null;
   }

   public static CompletableFuture async$queue(WalmartNew param0, String param1, String param2, String param3, int param4, int param5, CompletableFuture param6, String param7, String param8, long param9, Throwable param11, int param12, Object param13) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture submitShipping() {
      if (this.api.addressId != null && !this.api.addressId.isBlank() && this.api.accessPointId != null && !this.api.accessPointId.isBlank()) {
         return CompletableFuture.completedFuture(true);
      } else {
         API var10002 = this.api;
         Objects.requireNonNull(var10002);
         CompletableFuture var10000 = this.doPost("Submitting shipping", var10002::getPCID, this.api.PCIDForm(), 200, "newAddress");
         CompletableFuture var4;
         if (!var10000.isDone()) {
            var4 = var10000;
            return var4.exceptionally(Function.identity()).thenCompose(WalmartNew::async$submitShipping);
         } else {
            String var1 = (String)var10000.join();
            JsonObject var2 = (new JsonObject(var1)).getJsonObject("data").getJsonObject("createAccountAddress").getJsonObject("newAddress");
            if (var2.containsKey("id")) {
               this.api.addressId = var2.getString("id");
               if (var2.containsKey("accessPoint")) {
                  JsonObject var3 = var2.getJsonObject("accessPoint");
                  if (var3.containsKey("id")) {
                     this.api.accessPointId = var3.getString("id");
                     var10002 = this.api;
                     Objects.requireNonNull(var10002);
                     var10000 = this.doPost("Submitting fulfilment", var10002::fulfillment, this.api.fulfillmentBody(), 200, "\"intent\":\"SHIPPING\"");
                     if (!var10000.isDone()) {
                        var4 = var10000;
                        return var4.exceptionally(Function.identity()).thenCompose(WalmartNew::async$submitShipping);
                     }

                     var10000.join();
                     return CompletableFuture.completedFuture(true);
                  }
               }

               return CompletableFuture.failedFuture(new Exception("shipping_update_failed"));
            } else {
               return CompletableFuture.completedFuture(false);
            }
         }
      }
   }

   public static CompletableFuture async$sessionLogon(WalmartNew var0, Account var1, String var2, JsonArray var3, CompletableFuture var4, int var5, Object var6) {
      Throwable var16;
      label63: {
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

               for(int var11 = 0; var11 < var3.size(); ++var11) {
                  try {
                     String var14 = var3.getString(var11);
                     Cookie var15 = ClientCookieDecoder.STRICT.decode(var14);
                     if (var15 != null) {
                        var0.api.getCookies().put(var15);
                     }
                  } catch (Throwable var8) {
                     var0.logger.warn("Error parsing session state: {}", var8.getMessage());
                  }
               }

               try {
                  API var10002 = var0.api;
                  Objects.requireNonNull(var10002);
                  var10000 = var0.doPost("Validating Session", var10002::getAccountPage, var0.api.getAccountPageForm(), 200, (String[])null);
                  if (!var10000.isDone()) {
                     CompletableFuture var7 = var10000;
                     return var7.exceptionally(Function.identity()).thenCompose(WalmartNew::async$sessionLogon);
                  }
                  break;
               } catch (Throwable var10) {
                  var16 = var10;
                  var10001 = false;
                  break label63;
               }
            case 1:
               var10000 = var4;
               break;
            default:
               throw new IllegalArgumentException();
         }

         try {
            String var13 = (String)var10000.join();
            if (var13.contains(var1.getUser())) {
               return CompletableFuture.completedFuture(true);
            }

            return CompletableFuture.completedFuture(false);
         } catch (Throwable var9) {
            var16 = var9;
            var10001 = false;
         }
      }

      Throwable var12 = var16;
      var0.logger.warn("Error validating session: {}", var12.getMessage());
      return CompletableFuture.completedFuture(false);
   }

   public CompletableFuture createCart() {
      Function var1 = WalmartNew::lambda$createCart$6;
      JsonObject var10003 = this.api.getCartQuery();
      API var10004 = this.api;
      Objects.requireNonNull(var10004);
      return this.execute("Fetching cart", var1, (Object)var10003, (Supplier)(var10004::getCart));
   }

   public Boolean lambda$processBuyNow$8(HttpResponse var1) {
      JsonObject var2 = var1.bodyAsJsonObject();
      String var3 = Util.parseErrorMessage(var1.bodyAsString());
      if (var3 != null) {
         if (var3.contains("pc_expired")) {
            this.logger.info("Checkout session expired. Handling...");
            return false;
         }

         this.logger.error("Alt processing failed [{}]", var3);
         this.handleFailureWebhooks(var3, var1.bodyAsJsonObject());
      } else if (Utils.containsAnyWords(var1.bodyAsString(), "\"OrderInfo\",\"id\":\"", "{\"data\":{\"placeOrder\":{\"id\"")) {
         this.logger.info("Successfully checked out [ALT]");
         Analytics.success(this.task, var2, this.api.proxyStringSafe());
         return true;
      }

      return null;
   }

   public CompletableFuture createAccount(boolean var1) {
      this.logger.info("Creating account");
      int var2 = 0;

      while(super.running && var2 <= 500) {
         ++var2;
         this.api.setLoggedIn(false);
         this.task.getProfile().setAccountEmail((String)null);
         this.task.getProfile().setAccountPassword((String)null);

         CompletableFuture var10;
         CompletableFuture var10000;
         try {
            JsonObject var3 = this.api.accountCreateForm();
            HttpRequest var4 = this.api.createAccount();
            var10000 = Request.send(var4, var3);
            if (!var10000.isDone()) {
               var10 = var10000;
               return var10.exceptionally(Function.identity()).thenCompose(WalmartNew::async$createAccount);
            }

            HttpResponse var5 = (HttpResponse)var10000.join();
            if (var5 != null) {
               if (var5.statusCode() == 201 || var5.statusCode() == 200 || var5.statusCode() == 206) {
                  if (var1 != 0) {
                     Account var12 = new Account(var3.getString("email"), var3.getString("password"), Site.WALMART.name());
                     if (!var5.cookies().isEmpty()) {
                        JsonArray var7 = new JsonArray();
                        Iterator var8 = var5.cookies().iterator();

                        while(var8.hasNext()) {
                           String var9 = (String)var8.next();
                           var7.add(var9);
                        }

                        var12.setSessionString(var7.encode());
                     }

                     super.vertx.eventBus().send("accounts.writer", var12);
                     super.vertx.eventBus().send("accounts.writer.session", var12);
                     if (this.logger.isDebugEnabled()) {
                        this.logger.debug("Account create responded with: [{}]{}", var5.statusCode(), var5.bodyAsString());
                     }
                  }

                  this.task.getProfile().setAccountEmail(var3.getString("email", (String)null));
                  this.task.getProfile().setAccountPassword(var3.getString("password", (String)null));
                  this.api.setLoggedIn(true);
                  this.logger.info("Created account successfully!");
                  return CompletableFuture.completedFuture((Object)null);
               }

               this.logger.warn("Creating account: status:'{}'", var5.statusCode());
               var10000 = this.api.handleBadResponse(var5, Request.getURI(var4));
               if (!var10000.isDone()) {
                  var10 = var10000;
                  return var10.exceptionally(Function.identity()).thenCompose(WalmartNew::async$createAccount);
               }

               byte var6 = (Boolean)var10000.join();
               if (var6 == 0) {
                  var10000 = VertxUtil.sleep((long)this.task.getRetryDelay());
                  if (!var10000.isDone()) {
                     var10 = var10000;
                     return var10.exceptionally(Function.identity()).thenCompose(WalmartNew::async$createAccount);
                  }

                  var10000.join();
               }
            }
         } catch (Throwable var11) {
            this.logger.error("Error occurred creating account: {}", var11.getMessage());
            var10000 = super.randomSleep(12000);
            if (!var10000.isDone()) {
               var10 = var10000;
               return var10.exceptionally(Function.identity()).thenCompose(WalmartNew::async$createAccount);
            }

            var10000.join();
         }
      }

      return CompletableFuture.failedFuture(new Exception());
   }

   public CompletableFuture queue() {
      String var1 = null;
      String var2 = null;
      String var3 = "";
      byte var4 = 0;
      byte var5 = 0;

      while(super.running && var5 == 0) {
         CompletableFuture var12;
         CompletableFuture var10000;
         try {
            if (var1 != null && !var1.isBlank()) {
               String var6;
               if (!this.api.getWebClient().cookieStore().contains("wr")) {
                  if (this.mode.equals(Mode.DESKTOP)) {
                     var6 = URLDecoder.decode(var1, StandardCharsets.UTF_8);
                     String var16 = Utils.quickParseFirst(var6, TICKET_PATTERN);
                     var10000 = this.doGet("Fetching queue", this::lambda$queue$13, 200, "\"queue\":\"");
                     if (!var10000.isDone()) {
                        var12 = var10000;
                        return var12.exceptionally(Function.identity()).thenCompose(WalmartNew::async$queue);
                     }

                     var10000.join();
                  } else {
                     var10000 = this.doGet("Fetching queue", this::lambda$queue$14, 200, "\"queue\":\"");
                     if (!var10000.isDone()) {
                        var12 = var10000;
                        return var12.exceptionally(Function.identity()).thenCompose(WalmartNew::async$queue);
                     }

                     var10000.join();
                  }
               } else {
                  String var10001 = "Waiting in queue. Salepaused -> " + var4 + " " + var3;
                  API var10002 = this.api;
                  Objects.requireNonNull(var10002);
                  var10000 = this.doGet(var10001, var10002::validate, 200, "\"state\"");
                  if (!var10000.isDone()) {
                     var12 = var10000;
                     return var12.exceptionally(Function.identity()).thenCompose(WalmartNew::async$queue);
                  }

                  var2 = (String)var10000.join();
                  if (var2 == null) {
                     this.logger.warn("Failed to load queue. Retrying...");
                  } else if (!var2.contains("\"state\":\"valid\"")) {
                     var6 = Utils.quickParseFirst(var2, REFRESH_TIME_PATTERN);
                     if (var6 == null) {
                        this.logger.info("Queue finished.");
                        return CompletableFuture.completedFuture(false);
                     }

                     long var7 = Long.parseLong(var6);

                     try {
                        String var9 = Utils.quickParseFirst(var2, EXPECTED_TIME_PATTERN);
                        long var10 = Long.parseLong(var9);
                        ZonedDateTime var17 = Instant.ofEpochMilli(var10).atZone(ZoneId.systemDefault());
                        var3 = "Expected exit time: " + var17.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                        var4 = var2.contains("state\":\"paused");
                     } catch (Exception var13) {
                        if (this.logger.isDebugEnabled()) {
                           this.logger.debug(var13);
                        }
                     }

                     var10000 = VertxUtil.hardCodedSleep(var7 - System.currentTimeMillis());
                     if (!var10000.isDone()) {
                        var12 = var10000;
                        return var12.exceptionally(Function.identity()).thenCompose(WalmartNew::async$queue);
                     }

                     var10000.join();
                  } else {
                     boolean var15 = true;
                     Analytics.queuePasses.increment();
                     this.logger.info("Passed queue! Proceeding");
                     break;
                  }
               }
            } else {
               this.logger.info("Fetching queue details...");
               var10000 = this.waitForQueue();
               if (!var10000.isDone()) {
                  var12 = var10000;
                  return var12.exceptionally(Function.identity()).thenCompose(WalmartNew::async$queue);
               }

               var1 = (String)var10000.join();
            }
         } catch (Throwable var14) {
            this.logger.error("Error in queue: {}", var14.getMessage());
            if (this.logger.isDebugEnabled()) {
               this.logger.debug(var14);
            }

            var10000 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
            if (!var10000.isDone()) {
               var12 = var10000;
               return var12.exceptionally(Function.identity()).thenCompose(WalmartNew::async$queue);
            }

            var10000.join();
         }
      }

      return CompletableFuture.completedFuture(true);
   }

   public static String lambda$createCart$6(HttpResponse var0) {
      String var1 = var0.bodyAsString();
      String var2 = Utils.quickParseFirst(var1, CART_ID_PATTERN);
      return var2;
   }

   public static boolean lambda$static$1(String var0) {
      return var0.contains("{\"data\":{\"placeOrder\":{\"id\"") && var0.contains("\"order\":{\"id\"");
   }

   public CompletableFuture addToCart() {
      int var1 = 0;

      while(super.running && var1++ <= 50) {
         CompletableFuture var9;
         CompletableFuture var16;
         try {
            if (this.api.cartId != null && !this.api.cartId.isBlank()) {
               this.fetchGlobalCookies();
               Buffer var12 = this.api.atcForm().toBuffer();
               HttpRequest var13 = this.api.addToCart();
               var16 = Request.send(var13, var12);
               if (!var16.isDone()) {
                  var9 = var16;
                  return var9.exceptionally(Function.identity()).thenCompose(WalmartNew::async$addToCart);
               }

               HttpResponse var4 = (HttpResponse)var16.join();
               if (var4 != null) {
                  if (this.logger.isDebugEnabled()) {
                     this.logger.debug("ATC Responded: {}", var4.bodyAsJsonObject().encodePrettily());
                  }

                  if (var4.statusCode() >= 200 && var4.statusCode() <= 300) {
                     String var14 = var4.bodyAsString();
                     if (var14.contains("\"lineItems\":[{\"")) {
                        VertxUtil.sendSignal(this.instanceSignal);
                        if (this.priceCheck(var4.bodyAsString())) {
                           Analytics.carts.increment();
                           this.logger.debug("Successfully added to cart!");
                           return CompletableFuture.completedFuture(true);
                        }

                        var16 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
                        if (!var16.isDone()) {
                           var9 = var16;
                           return var9.exceptionally(Function.identity()).thenCompose(WalmartNew::async$addToCart);
                        }

                        var16.join();
                     } else if (!var14.contains("\"code\":\"out_of_stock\"") && !var14.contains("\"lineItems\":[]")) {
                        if (var14.contains("{\"errors\":[{")) {
                           JsonArray var6 = var4.bodyAsJsonObject().getJsonArray("errors");
                           JsonObject var7 = var6.getJsonObject(0);
                           String var8 = var7.getString("message", "").toLowerCase();
                           if (var8.contains("unauthorized access to cart")) {
                              this.logger.info("Invalid cart. Resetting...");
                              this.reset();
                              this.api.cartId = null;
                           } else {
                              if (var8.contains("item unavailable")) {
                                 this.logger.warn("Waiting for product to be live: '{}'", var4.statusCode());
                              } else if (var8.contains("unauthenticated")) {
                                 this.logger.warn("Unauthenticated product access. Likely account required: '{}'", var4.statusCode());
                              } else {
                                 this.logger.warn("Failed to atc: {}", var8);
                              }

                              var16 = VertxUtil.randomSignalSleep(this.instanceSignal, (long)this.task.getMonitorDelay());
                              if (!var16.isDone()) {
                                 var9 = var16;
                                 return var9.exceptionally(Function.identity()).thenCompose(WalmartNew::async$addToCart);
                              }

                              var16.join();
                           }
                        }
                     } else {
                        this.logger.warn("Waiting for restock: '{}'", "OOS");
                        var16 = VertxUtil.randomSignalSleep(this.instanceSignal, (long)this.task.getMonitorDelay());
                        if (!var16.isDone()) {
                           var9 = var16;
                           return var9.exceptionally(Function.identity()).thenCompose(WalmartNew::async$addToCart);
                        }

                        var16.join();
                     }
                  } else {
                     this.logger.warn("Handling status on atc: {}", var4.statusCode());
                     var16 = this.api.handleBadResponse(var4, Request.getURI(var13));
                     if (!var16.isDone()) {
                        var9 = var16;
                        return var9.exceptionally(Function.identity()).thenCompose(WalmartNew::async$addToCart);
                     }

                     byte var5 = (Boolean)var16.join();
                     if (var5 == 0) {
                        var16 = VertxUtil.sleep((long)this.task.getMonitorDelay());
                        if (!var16.isDone()) {
                           var9 = var16;
                           return var9.exceptionally(Function.identity()).thenCompose(WalmartNew::async$addToCart);
                        }

                        var16.join();
                     }
                  }
               } else {
                  this.logger.warn("Failed to add to cart. Retrying...");
                  var16 = VertxUtil.sleep((long)this.task.getRetryDelay());
                  if (!var16.isDone()) {
                     var9 = var16;
                     return var9.exceptionally(Function.identity()).thenCompose(WalmartNew::async$addToCart);
                  }

                  var16.join();
               }
            } else {
               API var17 = this.api;
               CompletableFuture var10001 = this.createCart();
               if (!var10001.isDone()) {
                  CompletableFuture var10 = var10001;
                  API var15 = var17;
                  return var10.exceptionally(Function.identity()).thenCompose(WalmartNew::async$addToCart);
               }

               var17.cartId = (String)var10001.join();
               if (this.task.getMode().contains("fast")) {
                  if (this.api.isLoggedIn()) {
                     CompletableFuture var2 = this.submitShipping();
                     CompletableFuture var3 = this.submitBilling();
                     var16 = CompletableFuture.allOf(var2, var3);
                     if (!var16.isDone()) {
                        var9 = var16;
                        return var9.exceptionally(Function.identity()).thenCompose(WalmartNew::async$addToCart);
                     }

                     var16.join();
                  } else {
                     var16 = this.submitShipping();
                     if (!var16.isDone()) {
                        var9 = var16;
                        return var9.exceptionally(Function.identity()).thenCompose(WalmartNew::async$addToCart);
                     }

                     var16.join();
                  }
               }
            }
         } catch (Throwable var11) {
            this.logger.error("Error adding to cart: {}", var11.getMessage());
            if (this.logger.isDebugEnabled()) {
               this.logger.debug(var11);
            }

            var16 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
            if (!var16.isDone()) {
               var9 = var16;
               return var9.exceptionally(Function.identity()).thenCompose(WalmartNew::async$addToCart);
            }

            var16.join();
         }
      }

      return CompletableFuture.completedFuture(false);
   }

   public static CompletableFuture async$loginAccount(WalmartNew param0, int param1, AccountController param2, Account param3, CompletableFuture param4, JsonObject param5, HttpRequest param6, HttpResponse param7, int param8, Throwable param9, int param10, Object param11) {
      // $FF: Couldn't be decompiled
   }

   public HttpRequest lambda$queue$14(String var1) {
      return this.api.issueTicket(var1);
   }

   public CompletableFuture waitForQueue() {
      String var1 = this.instanceSignal + "queue";

      while(super.running) {
         CompletableFuture var10000;
         CompletableFuture var5;
         try {
            HttpRequest var2;
            HttpResponse var3;
            if (this.mode.equals(Mode.DESKTOP)) {
               var2 = this.api.product(this.task.getKeywords()[2].toLowerCase(Locale.ROOT));
               var10000 = Request.send(var2);
               if (!var10000.isDone()) {
                  var5 = var10000;
                  return var5.exceptionally(Function.identity()).thenCompose(WalmartNew::async$waitForQueue);
               }

               var3 = (HttpResponse)var10000.join();
            } else {
               JsonObject var4 = ((WalmartNewAPIMobile)this.api).terraFirmaForm(Utils.quickParseFirst(this.task.getKeywords()[2], KEYWORD_PID_PATTERN));
               var2 = this.api.terraFirma(Utils.quickParseFirst(this.task.getKeywords()[2], KEYWORD_PID_PATTERN), false);
               var10000 = Request.send(var2, var4);
               if (!var10000.isDone()) {
                  var5 = var10000;
                  return var5.exceptionally(Function.identity()).thenCompose(WalmartNew::async$waitForQueue);
               }

               var3 = (HttpResponse)var10000.join();
            }

            if (var3 != null) {
               if (var3.statusCode() == 210) {
                  this.api.getPxAPI().reset();
                  var10000 = this.api.initialisePX();
                  if (!var10000.isDone()) {
                     var5 = var10000;
                     return var5.exceptionally(Function.identity()).thenCompose(WalmartNew::async$waitForQueue);
                  }

                  var10000.join();
               }

               if (this.mode.equals(Mode.DESKTOP)) {
                  if (var3.statusCode() >= 300 && var3.statusCode() < 400 && var3.headers().contains("location")) {
                     String var7 = var3.getHeader("location");
                     if (var7.contains("qpdata=")) {
                        VertxUtil.sendSignal(var1);
                        this.logger.info("Queue started! Proceeding...");
                        return CompletableFuture.completedFuture(var7);
                     }
                  }
               } else if (var3.bodyAsString().contains("api.waiting-room")) {
                  VertxUtil.sendSignal(var1);
                  this.logger.info("Queue started! Proceeding...");
                  return CompletableFuture.completedFuture(var3.bodyAsJsonObject().getString("url", (String)null));
               }

               var10000 = this.api.handleBadResponse(var3, Request.getURI(var2));
               if (!var10000.isDone()) {
                  var5 = var10000;
                  return var5.exceptionally(Function.identity()).thenCompose(WalmartNew::async$waitForQueue);
               }

               byte var8 = (Boolean)var10000.join();
               if (var8 == 0) {
                  var10000 = VertxUtil.signalSleep(var1, (long)this.task.getMonitorDelay());
                  if (!var10000.isDone()) {
                     var5 = var10000;
                     return var5.exceptionally(Function.identity()).thenCompose(WalmartNew::async$waitForQueue);
                  }

                  var10000.join();
               }

               this.logger.info("Waiting for queue to start [{}]", var3.statusCode());
            } else {
               this.logger.warn("Failed to wait for queue. Retrying...");
               var10000 = VertxUtil.signalSleep(var1, (long)this.task.getRetryDelay());
               if (!var10000.isDone()) {
                  var5 = var10000;
                  return var5.exceptionally(Function.identity()).thenCompose(WalmartNew::async$waitForQueue);
               }

               var10000.join();
            }
         } catch (Throwable var6) {
            this.logger.warn("Error occurred waiting for queue: {}", var6.getMessage());
            var10000 = VertxUtil.randomSleep(10000L);
            if (!var10000.isDone()) {
               var5 = var10000;
               return var5.exceptionally(Function.identity()).thenCompose(WalmartNew::async$waitForQueue);
            }

            var10000.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture run() {
      try {
         if (this.mode.equals(Mode.DESKTOP)) {
            if (this.task.getMode().contains("testing")) {
               this.api.setAPI(PerimeterX.createDesktop(this));
            } else {
               this.api.setAPI(new DesktopPXAPI3(this));
            }
         } else {
            this.api.setAPI(PerimeterX.createMobile(this));
         }

         CompletableFuture var10000 = this.generatePaymentToken();
         CompletableFuture var7;
         if (!var10000.isDone()) {
            var7 = var10000;
            return var7.exceptionally(Function.identity()).thenCompose(WalmartNew::async$run);
         }

         var10000.join();
         var10000 = this.api.initialisePX();
         if (!var10000.isDone()) {
            var7 = var10000;
            return var7.exceptionally(Function.identity()).thenCompose(WalmartNew::async$run);
         }

         byte var1 = (Boolean)var10000.join();
         if (var1 == 0) {
            this.logger.warn("Failed to initialise and configure task. Stopping...");
            return CompletableFuture.completedFuture((Object)null);
         }

         Function var2 = WalmartNew::lambda$run$3;
         Function var3 = this::lambda$run$4;

         try {
            if (this.mode == Mode.DESKTOP) {
               var10000 = this.api.generatePX(false);
               if (!var10000.isDone()) {
                  var7 = var10000;
                  return var7.exceptionally(Function.identity()).thenCompose(WalmartNew::async$run);
               }

               var10000.join();
               API var10003 = this.api;
               Objects.requireNonNull(var10003);
               var10000 = this.execute("Checking homepage", var2, (Supplier)(var10003::homepage), (int)200);
               if (!var10000.isDone()) {
                  var7 = var10000;
                  return var7.exceptionally(Function.identity()).thenCompose(WalmartNew::async$run);
               }

               String var4 = (String)var10000.join();
               this.api.platformVersion = Utils.quickParseFirst(var4, APPL_VER_PATTERN);
               this.api.storeID = Integer.valueOf(Utils.quickParseFirst(var4, STORE_ID_PATTERN));
               List var5 = Utils.quickParseAll(var4, PRODUCTS_HOME_PATTERN);
               API var14 = this.api;
               Object var10001 = var5.get(ThreadLocalRandom.current().nextInt(var5.size()));
               var14.productReferer = "https://www.walmart.com/" + (String)var10001;
               if (this.api.platformVersion == null) {
                  this.api.platformVersion = "main-110-891adc";
               }

               if (this.api.storeID == null) {
                  this.api.storeID = 5880;
               }
            } else {
               this.api.cookieStore().put("WLM", "1", ".walmart.com");
               JsonObject var15 = ((WalmartNewAPIMobile)this.api).homepageJson();
               API var10004 = this.api;
               Objects.requireNonNull(var10004);
               var10000 = this.execute("Checking homepage", var3, (Object)var15, (Supplier)(var10004::homepage));
               if (!var10000.isDone()) {
                  var7 = var10000;
                  return var7.exceptionally(Function.identity()).thenCompose(WalmartNew::async$run);
               }

               var10000.join();
            }
         } catch (Throwable var8) {
            this.logger.warn("Failed to parse session vars: {}", var8.getMessage());
         }

         if (this.keepAliveWorker == 0L) {
            this.keepAliveWorker = super.vertx.setPeriodic(TimeUnit.SECONDS.toMillis(100L), this::lambda$run$5);
         }

         var10000 = this.sleep(ThreadLocalRandom.current().nextInt(300, 1500));
         if (!var10000.isDone()) {
            var7 = var10000;
            return var7.exceptionally(Function.identity()).thenCompose(WalmartNew::async$run);
         }

         var10000.join();
         if (this.task.getMode().contains("login")) {
            var10000 = this.createAccount(false);
            if (!var10000.isDone()) {
               var7 = var10000;
               return var7.exceptionally(Function.identity()).thenCompose(WalmartNew::async$run);
            }

            var10000.join();
         } else if (this.task.getMode().contains("account")) {
            var10000 = this.loginAccount();
            if (!var10000.isDone()) {
               var7 = var10000;
               return var7.exceptionally(Function.identity()).thenCompose(WalmartNew::async$run);
            }

            int var11 = (Integer)var10000.join();
            if (var11 < 1) {
               this.logger.info("No accounts available in storage. Creating new...");
               var10000 = this.createAccount(true);
               if (!var10000.isDone()) {
                  var7 = var10000;
                  return var7.exceptionally(Function.identity()).thenCompose(WalmartNew::async$run);
               }

               var10000.join();
            }
         }

         byte var12 = 0;
         byte var13 = 0;

         label164:
         while(true) {
            while(true) {
               if (!super.running) {
                  break label164;
               }

               if (this.sessionTimestamp == 0L || System.currentTimeMillis() - this.sessionTimestamp >= (long)ThreadLocalRandom.current().nextInt(120000, 300000)) {
                  var10000 = this.updateOrCreateSession();
                  if (!var10000.isDone()) {
                     var7 = var10000;
                     return var7.exceptionally(Function.identity()).thenCompose(WalmartNew::async$run);
                  }

                  var10000.join();
               }

               if (this.api.isLoggedIn() && this.queue && var13 == 0) {
                  if (this.task.getKeywords().length <= 2) {
                     this.logger.error("You need to include the item link in your keyword for queue mode.");
                     continue;
                  }

                  var10000 = this.queue();
                  if (!var10000.isDone()) {
                     var7 = var10000;
                     return var7.exceptionally(Function.identity()).thenCompose(WalmartNew::async$run);
                  }

                  var13 = (Boolean)var10000.join();
               }

               boolean var6;
               if (var12 == 0) {
                  if (this.api.isLoggedIn() && this.buyNow) {
                     var10000 = this.checkoutBuyNow();
                     if (!var10000.isDone()) {
                        var7 = var10000;
                        return var7.exceptionally(Function.identity()).thenCompose(WalmartNew::async$run);
                     }

                     var6 = (Boolean)var10000.join();
                     if (var6) {
                        return CompletableFuture.completedFuture((Object)null);
                     }
                  } else {
                     var10000 = this.addToCart();
                     if (!var10000.isDone()) {
                        var7 = var10000;
                        return var7.exceptionally(Function.identity()).thenCompose(WalmartNew::async$run);
                     }

                     var12 = (Boolean)var10000.join();
                  }
               } else {
                  try {
                     var10000 = this.checkout();
                     if (!var10000.isDone()) {
                        var7 = var10000;
                        return var7.exceptionally(Function.identity()).thenCompose(WalmartNew::async$run);
                     }

                     var6 = (Boolean)var10000.join();
                     if (var6) {
                        break label164;
                     }
                  } catch (Throwable var9) {
                  }
               }
            }
         }

         if (this.keepAliveWorker != 0L) {
            super.vertx.cancelTimer(this.keepAliveWorker);
         }
      } catch (Throwable var10) {
         var10.printStackTrace();
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$generatePaymentToken(WalmartNew var0, HttpRequest var1, CompletableFuture var2, int var3, Object var4) {
      Exception var10;
      label30: {
         CompletableFuture var10000;
         boolean var10001;
         switch (var3) {
            case 0:
               var1 = VertxSingleton.INSTANCE.getLocalClient().getClient().getAbs("https://securedataweb.walmart.com/pie/v1/wmcom_us_vtg_pie/getkey.js?bust=" + System.currentTimeMillis()).timeout(TimeUnit.SECONDS.toMillis(15L)).as(BodyCodec.string());

               try {
                  var10000 = Request.execute(var1, 5);
                  if (!var10000.isDone()) {
                     CompletableFuture var9 = var10000;
                     return var9.exceptionally(Function.identity()).thenCompose(WalmartNew::async$generatePaymentToken);
                  }
                  break;
               } catch (Exception var6) {
                  var10 = var6;
                  var10001 = false;
                  break label30;
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
            var0.token.set4111Encrypted(PaymentToken.prepareAndGenerate(var8, "4111111111111111", var0.task.getProfile().getCvv()).getEncryptedCvv());
            return CompletableFuture.completedFuture((Object)null);
         } catch (Exception var5) {
            var10 = var5;
            var10001 = false;
         }
      }

      Exception var7 = var10;
      var0.logger.warn("Failed to generate payment token: {}", var7.getMessage());
      return CompletableFuture.completedFuture((Object)null);
   }

   public boolean priceCheck(String var1) {
      if (this.desiredUnitPrice == -1) {
         try {
            this.desiredUnitPrice = Integer.parseInt(this.task.getKeywords()[1]);
         } catch (NumberFormatException var5) {
            this.logger.warn("Missing price-check (limit) for product: '{}'. Skipping...", this.instanceSignal);
            this.desiredUnitPrice = Integer.MAX_VALUE;
         }
      }

      try {
         double var2 = Double.parseDouble(Utils.quickParseFirst(var1, PRICE_PATTERN)) / (double)Integer.parseInt(this.task.getSize());
         if (var2 <= (double)this.desiredUnitPrice) {
            return true;
         }

         this.logger.warn("Price exceeds limit of ${}", this.desiredUnitPrice);
      } catch (Throwable var4) {
         this.logger.error("Error occurred on price-check: {}", var4.getMessage());
      }

      return false;
   }

   public CompletableFuture checkout() {
      byte var1 = 0;
      byte var2 = 0;
      String var3 = null;

      while(super.running && var1 <= 10) {
         try {
            CompletableFuture var9;
            CompletableFuture var10000;
            API var10002;
            if (var2 == 0) {
               var10000 = this.submitShipping();
               if (!var10000.isDone()) {
                  var9 = var10000;
                  return var9.exceptionally(Function.identity()).thenCompose(WalmartNew::async$checkout);
               }

               var10000.join();
               Function var11 = this::lambda$checkout$7;
               API var10003 = this.api;
               Objects.requireNonNull(var10003);
               var10000 = this.execute("Creating contract", var11, (Supplier)(var10003::createContract), (Object)this.api.createContractBody());
               if (!var10000.isDone()) {
                  var9 = var10000;
                  return var9.exceptionally(Function.identity()).thenCompose(WalmartNew::async$checkout);
               }

               var3 = (String)var10000.join();
               this.api.contractId = this.api.cookieStore().getCookieValue("AZ_ST_PC").split("%7C")[0];
               if (this.api.paymentId == null || !this.api.isLoggedIn()) {
                  var10000 = this.submitBilling();
                  if (!var10000.isDone()) {
                     var9 = var10000;
                     return var9.exceptionally(Function.identity()).thenCompose(WalmartNew::async$checkout);
                  }

                  var10000.join();
               }

               var2 = 1;
               var10002 = this.api;
               Objects.requireNonNull(var10002);
               var10000 = this.doPost("Updating billing", var10002::updateTender, this.api.tenderUpdateForm(var3), 200, "{\"data\":{\"updateTenderPlan");
               if (!var10000.isDone()) {
                  var9 = var10000;
                  return var9.exceptionally(Function.identity()).thenCompose(WalmartNew::async$checkout);
               }

               var10000.join();
            } else {
               var10002 = this.api;
               Objects.requireNonNull(var10002);
               var10000 = this.doPost("Processing...", var10002::submitPayment, this.api.getPaymentForm(this.token), 200, (String[])null);
               if (!var10000.isDone()) {
                  var9 = var10000;
                  return var9.exceptionally(Function.identity()).thenCompose(WalmartNew::async$checkout);
               }

               String var4 = (String)var10000.join();
               if (this.logger.isDebugEnabled()) {
                  this.logger.debug("processPayment responded with: {}", var4);
               }

               if (var4 != null) {
                  label115: {
                     if (this.mode.equals(Mode.DESKTOP)) {
                        if (!DESKTOP_CHECKOUT_FLAG.test(var4)) {
                           break label115;
                        }
                     } else if (!MOBILE_CHECKOUT_FLAG.test(var4)) {
                        break label115;
                     }

                     VertxUtil.sendSignal(this.instanceSignal);
                     this.logger.info("Successfully checked out!");
                     Analytics.success(this.task, new JsonObject(var4), this.api.proxyStringSafe());
                     return CompletableFuture.completedFuture(true);
                  }

                  if (var4.contains("{\"errors\":[{\"message\":\"")) {
                     JsonObject var5 = new JsonObject(var4);
                     JsonObject var6 = var5.getJsonArray("errors").getJsonObject(0);
                     this.logger.info(var6.getString("message"));
                     if (var6.containsKey("extensions")) {
                        JsonObject var7 = var6.getJsonObject("extensions");
                        if (var7.containsKey("code")) {
                           String var8 = var7.getString("code");
                           if (!var8.contains("pc_expired") && !var8.contains("Purchase Contract has expired")) {
                              if (var8.contains("unexpected_error") || var8.contains("INTERNAL_SERVER_ERROR") || var8.contains("Something went wrong while processing the query.")) {
                                 this.logger.info("Error on checkout with code: {}", var8);
                                 continue;
                              }

                              if (var8.contains("contract_done")) {
                                 return CompletableFuture.completedFuture(true);
                              }

                              this.logger.info("Failed with code: {}", var8);
                              this.handleFailureWebhooks(var8, var5);
                              if (!var8.contains("out_of_stock") && !var8.contains("Item is no longer in stock.")) {
                                 var10002 = this.api;
                                 Objects.requireNonNull(var10002);
                                 var10000 = this.doPost("Updating billing", var10002::updateTender, this.api.tenderUpdateForm(var3), 200, "{\"data\":{\"updateTenderPlan");
                                 if (!var10000.isDone()) {
                                    var9 = var10000;
                                    return var9.exceptionally(Function.identity()).thenCompose(WalmartNew::async$checkout);
                                 }

                                 var10000.join();
                              }
                           } else {
                              this.logger.info("Session expiry detected. Resetting!");
                              var2 = 0;
                           }
                        }
                     } else if (var6.containsKey("already been completed for this contract")) {
                        Analytics.success(this.task, new JsonObject(var4), this.api.proxyStringSafe());
                        return CompletableFuture.completedFuture(true);
                     }
                  }
               }

               var10000 = VertxUtil.sleep((long)this.task.getRetryDelay());
               if (!var10000.isDone()) {
                  var9 = var10000;
                  return var9.exceptionally(Function.identity()).thenCompose(WalmartNew::async$checkout);
               }

               var10000.join();
            }
         } catch (Throwable var10) {
            var2 = 0;
            this.reset();
         }
      }

      this.reset();
      return CompletableFuture.completedFuture(false);
   }

   public CompletableFuture buyNow() {
      this.logger.info("Waiting for restock");
      byte var1 = 1;
      String var2 = null;
      int var3 = 0;

      while(super.running && var3++ <= Integer.MAX_VALUE) {
         CompletableFuture var10;
         CompletableFuture var16;
         try {
            CompletableFuture var11;
            API var15;
            API var17;
            CompletableFuture var10001;
            if (this.api.cartId == null || this.api.cartId.isBlank()) {
               var17 = this.api;
               var10001 = this.createCart();
               if (!var10001.isDone()) {
                  var11 = var10001;
                  var15 = var17;
                  return var11.exceptionally(Function.identity()).thenCompose(WalmartNew::async$buyNow);
               }

               var17.cartId = (String)var10001.join();
            }

            this.fetchGlobalCookies();
            HttpResponse var4;
            if (var1 == 0 && var2 != null) {
               var16 = Request.send(this.api.getContract(), this.api.getContractForm(var2));
               if (!var16.isDone()) {
                  var10 = var16;
                  return var10.exceptionally(Function.identity()).thenCompose(WalmartNew::async$buyNow);
               }

               var4 = (HttpResponse)var16.join();
            } else {
               var16 = Request.send(this.api.buyNow(), var1 != 0 ? this.api.buyNowPreloadBody() : this.api.buyNowBody());
               if (!var16.isDone()) {
                  var10 = var16;
                  return var10.exceptionally(Function.identity()).thenCompose(WalmartNew::async$buyNow);
               }

               var4 = (HttpResponse)var16.join();
            }

            if (var4 != null) {
               String var5 = var4.bodyAsString();
               if (var1 == 0 && Utils.containsIgnoreCase(var5, "mario")) {
                  if (var2 != null) {
                     var2 = null;
                  } else {
                     this.logger.error("Preload error");
                  }
               } else if (var4.statusCode() == 200 && !Utils.containsAnyWords(var5, "no longer in stock", "item unavailable", "expired") && (Utils.containsAllWords(var5, "contract", "created") || Utils.containsAllWords(var5, "contract", "set_payment"))) {
                  if (var1 == 0) {
                     VertxUtil.sendSignal(this.instanceSignal);
                  }

                  var2 = Utils.quickParseFirst(var5, ID_PATTERN);
                  int var6 = !var5.contains("\"cvvRequired\":false") ? 1 : 0;
                  int var7 = !Utils.hasPattern(var5, ADDRESS_PATTERN) ? 1 : 0;
                  if (var6 != 0 || var7 != 0) {
                     API var10002;
                     if (var7 != 0) {
                        var17 = this.api;
                        Function var10003 = quickFunctionParse(ADDRESS_ID_PATTERN, 200);
                        API var10004 = this.api;
                        Objects.requireNonNull(var10004);
                        var10001 = this.execute("Adding shipping method", var10003, (Supplier)(var10004::saveAddress), (Object)this.api.saveAddressJson());
                        if (!var10001.isDone()) {
                           var11 = var10001;
                           var15 = var17;
                           return var11.exceptionally(Function.identity()).thenCompose(WalmartNew::async$buyNow);
                        }

                        var17.addressId = (String)var10001.join();
                        var10002 = this.api;
                        Objects.requireNonNull(var10002);
                        CompletableFuture var8 = this.doPost("Setting preferred method", var10002::setAddressID, this.api.setAddressIdForm(var2, this.api.addressId), 200, "UPDATE_SHIPPING_ADDRESS");
                        var10002 = this.api;
                        Objects.requireNonNull(var10002);
                        CompletableFuture var9 = this.doPost("Submitting fulfilment", var10002::fulfillment, this.api.fulfillmentBody(), 200, "\"intent\":\"SHIPPING\"");
                        var16 = CompletableFuture.allOf(var8, var9);
                        if (!var16.isDone()) {
                           var10 = var16;
                           return var10.exceptionally(Function.identity()).thenCompose(WalmartNew::async$buyNow);
                        }

                        var16.join();
                     }

                     if (var6 != 0) {
                        var16 = this.submitBilling();
                        if (!var16.isDone()) {
                           var10 = var16;
                           return var10.exceptionally(Function.identity()).thenCompose(WalmartNew::async$buyNow);
                        }

                        var16.join();
                        if (this.api.paymentId != null && !var5.contains("\"payments\":[]") && !var5.contains("\"paymentPreferenceId\":\"" + this.api.paymentId + "\"")) {
                           var10002 = this.api;
                           Objects.requireNonNull(var10002);
                           var16 = this.doPost("Updating payment preference", var10002::setPayment, this.api.setPaymentForm(var2), 200, this.api.paymentId);
                           if (!var16.isDone()) {
                              var10 = var16;
                              return var10.exceptionally(Function.identity()).thenCompose(WalmartNew::async$buyNow);
                           }

                           var16.join();
                           var1 = 0;
                        }
                     }
                     continue;
                  }

                  if (var1 != 0) {
                     var1 = 0;
                     continue;
                  }

                  if (this.priceCheck(var5)) {
                     return CompletableFuture.completedFuture(var5);
                  }
               }

               String var13 = Util.parseErrorMessage(var5);
               if (var13 == null) {
                  int var10000 = var4.statusCode();
                  var13 = "" + var10000 + var4.statusMessage();
               }

               this.logger.warn("Waiting for restock: '{}'", var13);
               if (var4.statusCode() == 400) {
                  var16 = VertxUtil.hardCodedSleep((long)this.task.getRetryDelay());
                  if (!var16.isDone()) {
                     var10 = var16;
                     return var10.exceptionally(Function.identity()).thenCompose(WalmartNew::async$buyNow);
                  }

                  var16.join();
               } else if (var4.statusCode() >= 307) {
                  var16 = this.api.handleBadResponse(var4, (String)null);
                  if (!var16.isDone()) {
                     var10 = var16;
                     return var10.exceptionally(Function.identity()).thenCompose(WalmartNew::async$buyNow);
                  }

                  byte var14 = (Boolean)var16.join();
                  if (var14 == 0) {
                     var16 = VertxUtil.sleep((long)this.task.getMonitorDelay());
                     if (!var16.isDone()) {
                        var10 = var16;
                        return var10.exceptionally(Function.identity()).thenCompose(WalmartNew::async$buyNow);
                     }

                     var16.join();
                  }
               } else {
                  var16 = VertxUtil.signalSleep(this.instanceSignal, (long)this.task.getMonitorDelay());
                  if (!var16.isDone()) {
                     var10 = var16;
                     return var10.exceptionally(Function.identity()).thenCompose(WalmartNew::async$buyNow);
                  }

                  var16.join();
               }
            }
         } catch (Throwable var12) {
            this.logger.error("Error waiting for restock: {}", var12.getMessage());
            var16 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
            if (!var16.isDone()) {
               var10 = var16;
               return var10.exceptionally(Function.identity()).thenCompose(WalmartNew::async$buyNow);
            }

            var16.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public void lambda$run$5(Long var1) {
      Request.execute(this.api.getWebClient().getAbs("https://www.walmart.com/favicon.ico").putHeader("user-agent", this.api.getPxAPI().getDeviceUA()));
   }

   public void fetchGlobalCookies() {
      GLOBAL_COOKIES.forEach(this::lambda$fetchGlobalCookies$12);
   }

   public CompletableFuture generatePaymentToken() {
      HttpRequest var1 = VertxSingleton.INSTANCE.getLocalClient().getClient().getAbs("https://securedataweb.walmart.com/pie/v1/wmcom_us_vtg_pie/getkey.js?bust=" + System.currentTimeMillis()).timeout(TimeUnit.SECONDS.toMillis(15L)).as(BodyCodec.string());

      try {
         CompletableFuture var10000 = Request.execute(var1, 5);
         if (!var10000.isDone()) {
            CompletableFuture var3 = var10000;
            return var3.exceptionally(Function.identity()).thenCompose(WalmartNew::async$generatePaymentToken);
         }

         String var2 = (String)var10000.join();
         this.token = PaymentToken.prepareAndGenerate(var2, this.task.getProfile().getCardNumber(), this.task.getProfile().getCvv());
         this.token.set4111Encrypted(PaymentToken.prepareAndGenerate(var2, "4111111111111111", this.task.getProfile().getCvv()).getEncryptedCvv());
      } catch (Exception var4) {
         this.logger.warn("Failed to generate payment token: {}", var4.getMessage());
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture loginAccount() {
      int var1 = 0;
      AccountController var2 = (AccountController)Engine.get().getModule(Controller.ACCOUNT);
      Account var3 = null;

      while(super.running && var1 <= 500) {
         ++var1;

         CompletableFuture var10;
         CompletableFuture var10000;
         try {
            if (var3 == null) {
               var10000 = var2.findAccount(this.task.getProfile().getEmail(), false).toCompletionStage().toCompletableFuture();
               if (!var10000.isDone()) {
                  var10 = var10000;
                  return var10.exceptionally(Function.identity()).thenCompose(WalmartNew::async$loginAccount);
               }

               var3 = (Account)var10000.join();
            }

            if (var3 == null) {
               this.logger.warn("No accounts available...");
               return CompletableFuture.completedFuture(0);
            }

            this.logger.info("Logging in to account '{}'", var3.getUser());
            var3.setSite(Site.WALMART.name());
            var10000 = this.sessionLogon(var3);
            if (!var10000.isDone()) {
               var10 = var10000;
               return var10.exceptionally(Function.identity()).thenCompose(WalmartNew::async$loginAccount);
            }

            if ((Boolean)var10000.join()) {
               this.logger.info("Logged in successfully to account[-] '{}'", var3.getUser());
               this.task.getProfile().setAccountEmail(var3.getUser());
               this.task.getProfile().setAccountPassword(var3.getPass());
               this.api.setLoggedIn(true);
               return CompletableFuture.completedFuture(1);
            }

            JsonObject var4 = this.api.accountLoginForm(var3);
            HttpRequest var5 = this.api.loginAccount();
            var10000 = Request.send(var5, var4);
            if (!var10000.isDone()) {
               var10 = var10000;
               return var10.exceptionally(Function.identity()).thenCompose(WalmartNew::async$loginAccount);
            }

            HttpResponse var6 = (HttpResponse)var10000.join();
            if (var6 != null) {
               if (var6.statusCode() == 201 || var6.statusCode() == 200 || var6.statusCode() == 206) {
                  if (this.logger.isDebugEnabled()) {
                     this.logger.debug("Account login responded with: [{}]{}", var6.statusCode(), var6.bodyAsString());
                  }

                  this.logger.info("Logged in successfully to account '{}'", var3.getUser());
                  this.task.getProfile().setAccountEmail(var3.getUser());
                  this.task.getProfile().setAccountPassword(var3.getPass());
                  this.api.setLoggedIn(true);
                  if (!var6.cookies().isEmpty()) {
                     JsonArray var12 = new JsonArray();
                     Iterator var8 = var6.cookies().iterator();

                     while(var8.hasNext()) {
                        String var9 = (String)var8.next();
                        var12.add(var9);
                     }

                     var3.setSessionString(var12.encode());
                  }

                  super.vertx.eventBus().send("accounts.writer.session", var3);
                  return CompletableFuture.completedFuture(1);
               }

               this.logger.warn("Account login: status:'{}'", var6.statusCode());
               var10000 = this.api.handleBadResponse(var6, Request.getURI(var5));
               if (!var10000.isDone()) {
                  var10 = var10000;
                  return var10.exceptionally(Function.identity()).thenCompose(WalmartNew::async$loginAccount);
               }

               byte var7 = (Boolean)var10000.join();
               if (var7 == 0) {
                  var10000 = VertxUtil.sleep((long)this.task.getRetryDelay());
                  if (!var10000.isDone()) {
                     var10 = var10000;
                     return var10.exceptionally(Function.identity()).thenCompose(WalmartNew::async$loginAccount);
                  }

                  var10000.join();
               }
            }
         } catch (Throwable var11) {
            this.logger.error("Error occurred logging in account: {}", var11.getMessage());
            var10000 = super.randomSleep(12000);
            if (!var10000.isDone()) {
               var10 = var10000;
               return var10.exceptionally(Function.identity()).thenCompose(WalmartNew::async$loginAccount);
            }

            var10000.join();
         }
      }

      this.logger.warn("Failed to login to account. Max retries exceeded...");
      return CompletableFuture.completedFuture(-1);
   }

   public static CompletableFuture async$checkoutBuyNow(WalmartNew var0, CompletableFuture var1, int var2, Object var3) {
      CompletableFuture var10000;
      switch (var2) {
         case 0:
            var10000 = var0.buyNow();
            if (!var10000.isDone()) {
               CompletableFuture var5 = var10000;
               return var5.exceptionally(Function.identity()).thenCompose(WalmartNew::async$checkoutBuyNow);
            }
            break;
         case 1:
            var10000 = var1;
            break;
         default:
            throw new IllegalArgumentException();
      }

      String var4 = (String)var10000.join();
      if (var4 != null && !var4.isBlank()) {
         var0.lineItemId = var4.split("\"itemIds\":\\[\"")[1].split("\"")[0];
         var0.api.contractId = var4.split("\"id\":\"")[1].split("\"")[0];
      }

      return var0.processBuyNow();
   }

   public CompletableFuture processBuyNow() {
      Function var1 = this::lambda$processBuyNow$8;
      API var10003 = this.api;
      Objects.requireNonNull(var10003);
      return this.execute("Processing...", var1, (Supplier)(var10003::buyNowSubmitPayment), (Object)this.api.buyNowSubmitPaymentBody(this.api.contractId));
   }

   public HttpRequest lambda$queue$13(String var1) {
      return this.api.issueTicket(var1);
   }

   public CompletableFuture doGet(String var1, Supplier var2, Integer var3, String... var4) {
      this.logger.info(var1);

      while(super.running) {
         CompletableFuture var10;
         CompletableFuture var10000;
         try {
            HttpRequest var5 = (HttpRequest)var2.get();
            var10000 = Request.send(var5);
            if (!var10000.isDone()) {
               var10 = var10000;
               return var10.exceptionally(Function.identity()).thenCompose(WalmartNew::async$doGet);
            }

            HttpResponse var6 = (HttpResponse)var10000.join();
            if (var6 != null) {
               if (var6.statusCode() == 210) {
                  this.api.getPxAPI().reset();
                  var10000 = this.api.initialisePX();
                  if (!var10000.isDone()) {
                     var10 = var10000;
                     return var10.exceptionally(Function.identity()).thenCompose(WalmartNew::async$doGet);
                  }

                  var10000.join();
               }

               String var7;
               int var8;
               if (var6.statusCode() >= 300 && var6.statusCode() < 400) {
                  var7 = var6.getHeader("location");
                  var8 = var4 != null && !Utils.containsAllWords(var7, var4) ? 0 : 1;
               } else {
                  var7 = var6.bodyAsString();
                  var8 = var4 != null && !Utils.containsAllWords(var7, var4) ? 0 : 1;
               }

               if ((var3 == null || var6.statusCode() == var3) && var8 != 0) {
                  return CompletableFuture.completedFuture(var7);
               }

               Logger var12 = this.logger;
               String var10002 = var1.toLowerCase(Locale.ROOT);
               int var10003 = var6.statusCode();
               var12.warn("Failed {}: '{}'", var10002, "" + var10003 + var6.statusMessage());
               var10000 = this.api.handleBadResponse(var6, Request.getURI(var5));
               if (!var10000.isDone()) {
                  var10 = var10000;
                  return var10.exceptionally(Function.identity()).thenCompose(WalmartNew::async$doGet);
               }

               byte var9 = (Boolean)var10000.join();
               if (var9 == 0) {
                  var10000 = VertxUtil.sleep((long)this.task.getMonitorDelay());
                  if (!var10000.isDone()) {
                     var10 = var10000;
                     return var10.exceptionally(Function.identity()).thenCompose(WalmartNew::async$doGet);
                  }

                  var10000.join();
               }
            } else {
               var10000 = VertxUtil.sleep((long)this.task.getRetryDelay());
               if (!var10000.isDone()) {
                  var10 = var10000;
                  return var10.exceptionally(Function.identity()).thenCompose(WalmartNew::async$doGet);
               }

               var10000.join();
            }
         } catch (Throwable var11) {
            this.logger.error("Error {}: {}", var1.toLowerCase(Locale.ROOT), var11.getMessage());
            if (this.logger.isDebugEnabled()) {
               this.logger.debug(var11);
            }

            var10000 = super.randomSleep(this.task.getRetryDelay());
            if (!var10000.isDone()) {
               var10 = var10000;
               return var10.exceptionally(Function.identity()).thenCompose(WalmartNew::async$doGet);
            }

            var10000.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$addToCart(WalmartNew param0, int param1, API param2, CompletableFuture param3, CompletableFuture param4, CompletableFuture param5, Buffer param6, HttpRequest param7, HttpResponse param8, String param9, JsonArray param10, JsonObject param11, String param12, int param13, Throwable param14, int param15, Object param16) {
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
                  if (var6 != null) {
                     this.api.getCookies().put(var6);
                  }
               } catch (Throwable var9) {
                  this.logger.warn("Error parsing session state: {}", var9.getMessage());
               }
            }

            try {
               API var10002 = this.api;
               Objects.requireNonNull(var10002);
               CompletableFuture var10000 = this.doPost("Validating Session", var10002::getAccountPage, this.api.getAccountPageForm(), 200, (String[])null);
               if (!var10000.isDone()) {
                  CompletableFuture var7 = var10000;
                  return var7.exceptionally(Function.identity()).thenCompose(WalmartNew::async$sessionLogon);
               }

               String var10 = (String)var10000.join();
               if (var10.contains(var1.getUser())) {
                  return CompletableFuture.completedFuture(true);
               }
            } catch (Throwable var8) {
               this.logger.warn("Error validating session: {}", var8.getMessage());
            }
         }
      }

      return CompletableFuture.completedFuture(false);
   }

   public static String lambda$quickFunctionParse$2(Integer var0, Pattern var1, HttpResponse var2) {
      if (var0 != null && var2.statusCode() != var0) {
         return null;
      } else {
         String var3 = var2.bodyAsString();
         return Utils.quickParseFirst(var3, var1);
      }
   }

   public CompletableFuture execute(String var1, Function var2, Object var3, Supplier var4) {
      return this.execute(var1, var2, var4, var3);
   }

   public static void lambda$updateOrCreateSession$11(Cookie var0) {
      if (var0.value().equalsIgnoreCase("1") && !var0.name().equals("g")) {
         GLOBAL_COOKIES.add(var0);
      }

   }

   public static CompletableFuture async$checkout(WalmartNew param0, int param1, int param2, String param3, CompletableFuture param4, Function param5, String param6, JsonObject param7, JsonObject param8, JsonObject param9, String param10, int param11, Object param12) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$buyNow(WalmartNew param0, int param1, String param2, int param3, API param4, CompletableFuture param5, HttpResponse param6, String param7, int param8, int param9, CompletableFuture param10, CompletableFuture param11, String param12, Throwable param13, int param14, Object param15) {
      // $FF: Couldn't be decompiled
   }

   public WalmartNew(Task var1, int var2) {
      super(var2);
      this.task = var1;
      this.mode = Mode.getMode(this.task.getMode());
      if (this.mode.equals(Mode.DESKTOP)) {
         this.api = new WalmartNewAPI(this.task);
      } else {
         this.api = new WalmartNewAPIMobile(this.task);
      }

      super.setClient(this.api);
      this.instanceSignal = this.task.getKeywords()[0];
      this.keepAliveWorker = 0L;
      this.buyNow = var1.getMode().contains("alt");
      this.queue = var1.getMode().contains("queue");
   }

   public static boolean lambda$static$0(String var0) {
      return var0.contains("{\"data\":{\"placeOrder\":{\"__typename\":\"PurchaseContract\",\"id\"") && var0.contains("\"order\":{\"__typename\":\"OrderInfo\",\"id\"");
   }

   public static CompletableFuture async$doGet(WalmartNew param0, String param1, Supplier param2, Integer param3, String[] param4, HttpRequest param5, CompletableFuture param6, HttpResponse param7, String param8, int param9, int param10, Throwable param11, int param12, Object param13) {
      // $FF: Couldn't be decompiled
   }

   public String lambda$run$4(HttpResponse var1) {
      return this.api.cookieStore().contains("xptwg") ? var1.bodyAsString() : null;
   }

   public static CompletableFuture async$createAccount(WalmartNew param0, int param1, int param2, JsonObject param3, HttpRequest param4, CompletableFuture param5, HttpResponse param6, int param7, Throwable param8, int param9, Object param10) {
      // $FF: Couldn't be decompiled
   }

   public static Function quickFunctionParse(Pattern var0, Integer var1) {
      return WalmartNew::lambda$quickFunctionParse$2;
   }

   public static String lambda$updateOrCreateSession$9(HttpResponse var0) {
      if (var0 != null && var0.statusCode() == 200) {
         return "";
      } else {
         return var0 != null && var0.statusCode() == 442 ? var0.bodyAsJsonObject().getString("url") : null;
      }
   }

   public static CompletableFuture async$updateOrCreateSession(WalmartNew var0, Function var1, String var2, CompletableFuture var3, int var4, Object var5) {
      Throwable var11;
      label54: {
         boolean var10001;
         label53: {
            CompletableFuture var10000;
            label52: {
               switch (var4) {
                  case 0:
                     try {
                        if (var0.sessionCookies != null) {
                           var0.sessionCookies.clear();
                        }

                        var1 = WalmartNew::lambda$updateOrCreateSession$9;
                        if (!var0.mode.equals(Mode.MOBILE)) {
                           API var10004 = var0.api;
                           Objects.requireNonNull(var10004);
                           var10000 = var0.execute("Configuring...", var1, (Object)null, (Supplier)(var10004::getCheckoutPage));
                           if (!var10000.isDone()) {
                              var3 = var10000;
                              return var3.exceptionally(Function.identity()).thenCompose(WalmartNew::async$updateOrCreateSession);
                           }
                           break label52;
                        }

                        var2 = var0.task.getKeywords().length > 2 ? var0.task.getKeywords()[2] : "https://www.walmart.com/ip/Mario-Kart-8-Deluxe-Edition-Nintendo-Switch/55432571";
                        var10000 = var0.execute("Configuring...", var1, (Object)((WalmartNewAPIMobile)var0.api).terraFirmaForm(Utils.quickParseFirst(var2, KEYWORD_PID_PATTERN)), (Supplier)(var0::lambda$updateOrCreateSession$10));
                        if (!var10000.isDone()) {
                           var3 = var10000;
                           return var3.exceptionally(Function.identity()).thenCompose(WalmartNew::async$updateOrCreateSession);
                        }
                        break;
                     } catch (Throwable var9) {
                        var11 = var9;
                        var10001 = false;
                        break label54;
                     }
                  case 1:
                     var10000 = var3;
                     break;
                  case 2:
                     var10000 = var3;
                     break label52;
                  default:
                     throw new IllegalArgumentException();
               }

               try {
                  var10000.join();
                  break label53;
               } catch (Throwable var8) {
                  var11 = var8;
                  var10001 = false;
                  break label54;
               }
            }

            try {
               var10000.join();
            } catch (Throwable var7) {
               var11 = var7;
               var10001 = false;
               break label54;
            }
         }

         try {
            var0.sessionTimestamp = System.currentTimeMillis();
            var0.sessionCookies = new CookieJar(var0.api.getWebClient().cookieStore());
            var0.sessionCookies.get(true, ".walmart.com", "/").forEach(WalmartNew::lambda$updateOrCreateSession$11);
            var0.fetchGlobalCookies();
            var0.api.getWebClient().cookieStore().putFromOther(var0.sessionCookies);
            var0.logger.info("Updated session...");
            return CompletableFuture.completedFuture((Object)null);
         } catch (Throwable var6) {
            var11 = var6;
            var10001 = false;
         }
      }

      Throwable var10 = var11;
      var0.logger.error("Error occurred updating session: {}", var10.getMessage());
      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$submitBilling(WalmartNew var0, API var1, CompletableFuture var2, int var3, Object var4) {
      Throwable var10;
      label31: {
         boolean var11;
         API var10000;
         CompletableFuture var10001;
         switch (var3) {
            case 0:
               try {
                  var10000 = var0.api;
                  Function var10003 = quickFunctionParse(PAYMENT_ID_PATTERN, 200);
                  API var10004 = var0.api;
                  Objects.requireNonNull(var10004);
                  var10001 = var0.execute("Submitting billing", var10003, (Supplier)(var10004::submitBilling), (Object)var0.api.getBillingForm(var0.token));
                  if (!var10001.isDone()) {
                     CompletableFuture var9 = var10001;
                     API var8 = var10000;
                     return var9.exceptionally(Function.identity()).thenCompose(WalmartNew::async$submitBilling);
                  }
                  break;
               } catch (Throwable var6) {
                  var10 = var6;
                  var11 = false;
                  break label31;
               }
            case 1:
               var10000 = var1;
               var10001 = var2;
               break;
            default:
               throw new IllegalArgumentException();
         }

         try {
            var10000.paymentId = (String)var10001.join();
            return CompletableFuture.completedFuture(!var0.api.paymentId.isBlank());
         } catch (Throwable var5) {
            var10 = var5;
            var11 = false;
         }
      }

      Throwable var7 = var10;
      var0.logger.error("Error updating payment id: {}", var7.getMessage());
      return CompletableFuture.failedFuture(new Exception("payment_update_failed"));
   }

   public HttpRequest lambda$updateOrCreateSession$10(String var1) {
      return this.api.terraFirma(Utils.quickParseFirst(var1, KEYWORD_PID_PATTERN), false);
   }

   public void handleFailureWebhooks(String var1, JsonObject var2) {
      if (var1 != null && !this.lastReason.equalsIgnoreCase(var1)) {
         try {
            Analytics.failure(var1, this.task, var2, this.api.proxyStringSafe());
            this.lastReason = var1;
         } catch (Throwable var4) {
         }
      }

   }

   public void lambda$fetchGlobalCookies$12(Cookie var1) {
      if (!this.sessionCookies.contains(var1.name())) {
         this.sessionCookies.put(var1);
      }

   }
}
