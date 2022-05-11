package io.trickle.task.sites.walmart.usa;

import io.trickle.account.Account;
import io.trickle.core.Controller;
import io.trickle.core.Engine;
import io.trickle.profile.Profile;
import io.trickle.task.Task;
import io.trickle.task.antibot.impl.px.PerimeterX;
import io.trickle.task.sites.walmart.util.PaymentToken;
import io.trickle.task.sites.walmart.util.RefererStoreController;
import io.trickle.util.Utils;
import io.trickle.util.request.Headers$Pseudo;
import io.trickle.webclient.ClientType;
import io.trickle.webclient.CookieJar;
import io.trickle.webclient.RealClient;
import io.trickle.webclient.RealClientFactory;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.codec.BodyCodec;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WalmartAPIDesktop extends API {
   public boolean isLoggedIn;
   public String dateOfPrevReq;
   public static Pattern VID_LOCATION_PATTERN;
   public String referer;
   public boolean isZipLocated;
   public Task task;
   public JsonArray storeIds;
   public String searchQuery;
   public String crossSite;
   public static DateTimeFormatter GMT_CHROME_RFC1123;
   public PerimeterX pxAPI = null;
   public int cartLogic;

   public HttpRequest createAccount() {
      HttpRequest var1 = this.client.postAbs("https://www.walmart.com/account/electrode/api/signup?tid=0&returnUrl=%2F").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
      var1.putHeader("sec-ch-ua-mobile", "0?");
      var1.putHeader("user-agent", this.pxAPI.getDeviceUA());
      var1.putHeader("content-type", "application/json");
      var1.putHeader("accept", "*/*");
      var1.putHeader("origin", "https://www.walmart.com");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.walmart.com/account/signup?tid=0&returnUrl=%2Fcart%3Faction%3DSignIn%26rm%3Dtrue");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", this.pxAPI.getDeviceLang());
      return var1;
   }

   public HttpRequest submitBilling() {
      HttpRequest var1 = this.client.postAbs("https://www.walmart.com/api/checkout-customer/:CID/credit-card").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("accept", "application/json");
      var1.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
      var1.putHeader("sec-ch-ua-mobile", "0?");
      var1.putHeader("user-agent", this.pxAPI.getDeviceUA());
      var1.putHeader("content-type", "application/json");
      var1.putHeader("origin", "https://www.walmart.com");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.walmart.com/checkout/");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", this.pxAPI.getDeviceLang());
      return var1;
   }

   public HttpRequest electrode() {
      HttpRequest var1 = this.client.postAbs("https://www.walmart.com/electrode/api/logger").as(BodyCodec.none());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("accept", "application/json");
      var1.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
      var1.putHeader("sec-ch-ua-mobile", "0?");
      var1.putHeader("user-agent", this.pxAPI.getDeviceUA());
      var1.putHeader("content-type", "application/json");
      var1.putHeader("origin", "https://www.walmart.com");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.walmart.com/");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", this.pxAPI.getDeviceLang());
      return var1;
   }

   public boolean isLoggedIn() {
      return this.isLoggedIn;
   }

   public PerimeterX getPxAPI() {
      return this.pxAPI;
   }

   public CompletableFuture initialisePX() {
      CompletableFuture var10000 = this.pxAPI.initialise();
      if (!var10000.isDone()) {
         CompletableFuture var1 = var10000;
         return var1.exceptionally(Function.identity()).thenCompose(WalmartAPIDesktop::async$initialisePX);
      } else {
         var10000.join();
         return CompletableFuture.completedFuture(true);
      }
   }

   public Task getTask() {
      return this.task;
   }

   public HttpRequest affilBuyNowHardRefresh(String var1) {
      HttpRequest var2 = this.client.getAbs(var1 + "?items=" + this.task.getKeywords()[0] + "|" + this.task.getSize()).as(BodyCodec.none());
      var2.putHeader("pragma", "no-cache");
      var2.putHeader("cache-control", "no-cache");
      var2.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
      var2.putHeader("sec-ch-ua-mobile", "0?");
      var2.putHeader("upgrade-insecure-requests", "1");
      var2.putHeader("user-agent", this.pxAPI.getDeviceUA());
      var2.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var2.putHeader("sec-fetch-site", "cross-site");
      var2.putHeader("sec-fetch-mode", "navigate");
      var2.putHeader("sec-fetch-user", "?1");
      var2.putHeader("sec-fetch-dest", "document");
      var2.putHeader("accept-encoding", "gzip, deflate, br");
      var2.putHeader("accept-language", this.pxAPI.getDeviceLang());
      return var2;
   }

   public JsonObject atcForm() {
      switch (this.cartLogic) {
         case 0:
            return this.productPageAtcForm(this.task.getKeywords()[0], Integer.parseInt(this.task.getSize()));
         case 1:
            return this.getAtcForm(this.task.getKeywords()[0], Integer.parseInt(this.task.getSize()));
         default:
            return this.getAtcForm(this.task.getKeywords()[0], Integer.parseInt(this.task.getSize()));
      }
   }

   public JsonObject getShippingRateForm(JsonObject var1) {
      String var2 = var1.getJsonArray("items").getJsonObject(0).getString("id");
      JsonArray var3 = var1.getJsonArray("items");
      String var4 = var3.getJsonObject(0).getJsonObject("fulfillmentSelection").getString("shipMethod");
      JsonObject var5 = new JsonObject();
      var5.put("fulfillmentOption", "S2H");
      var5.put("itemIds", (new JsonArray()).add(var2));
      var5.put("shipMethod", var4);
      JsonObject var6 = new JsonObject();
      var6.put("groups", (new JsonArray()).add(var5));
      return var6;
   }

   public void swapClient() {
      try {
         RealClient var1 = RealClientFactory.fromOther(Vertx.currentContext().owner(), super.client, this.client.type());
         super.client.close();
         super.client = var1;
      } catch (Throwable var2) {
      }

   }

   public JsonObject PCIDForm() {
      JsonObject var1 = new JsonObject();
      var1.put("storeList", new JsonArray());
      var1.put("postalCode", this.task.getProfile().getZip());
      var1.put("city", this.task.getProfile().getCity());
      var1.put("state", this.task.getProfile().getState());
      var1.put("isZipLocated", this.isZipLocated);
      var1.put("crt:CRT", "");
      var1.put("customerId:CID", "");
      var1.put("customerType:type", "");
      var1.put("affiliateInfo:com.wm.reflector", "");
      return var1;
   }

   public HttpRequest submitPayment() {
      HttpRequest var1 = this.client.postAbs("https://www.walmart.com/api/checkout/v3/contract/:PCID/payment").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
      var1.putHeader("inkiru_precedence", "false");
      var1.putHeader("wm_cvv_in_session", "true");
      var1.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
      var1.putHeader("sec-ch-ua-mobile", "0?");
      var1.putHeader("user-agent", this.pxAPI.getDeviceUA());
      var1.putHeader("wm_vertical_id", "0");
      var1.putHeader("content-type", "application/json");
      var1.putHeader("origin", "https://www.walmart.com");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.walmart.com/checkout/");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", this.pxAPI.getDeviceLang());
      return var1;
   }

   public JsonObject getShippingForm(JsonObject var1) {
      JsonObject var2 = new JsonObject();
      var2.put("addressLineOne", this.task.getProfile().getAddress1());
      var2.put("addressLineTwo", this.task.getProfile().getAddress2());
      var2.put("city", this.task.getProfile().getCity().toUpperCase());
      var2.put("firstName", this.task.getProfile().getFirstName());
      var2.put("lastName", this.task.getProfile().getLastName());
      var2.put("phone", this.task.getProfile().getPhone());
      var2.put("email", this.task.getProfile().getEmail());
      var2.put("marketingEmailPref", false);
      var2.put("postalCode", this.task.getProfile().getZip());
      var2.put("state", this.task.getProfile().getState());
      var2.put("countryCode", "USA");
      var2.put("changedFields", new JsonArray());
      var2.put("storeList", new JsonArray());
      return var2;
   }

   public HttpRequest terraFirma(String var1, boolean var2) {
      String var3 = "https://www.walmart.com/terra-firma/graphql?v=2&options=timing%2Cnonnull%2Cerrors%2Ccontext&id=FullProductHolidaysRoute-web";
      if (var2) {
         var3 = "https://www.walmart.com/terra-firma/graphql?options=timing,nonnull,context&v=2&id=FullProductRoute-web";
      }

      HttpRequest var4 = this.client.postAbs(var3).as(BodyCodec.buffer());
      var4.putHeader("content-length", "DEFAULT_VALUE");
      var4.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
      var4.putHeader("sec-ch-ua-mobile", "0?");
      var4.putHeader("user-agent", this.pxAPI.getDeviceUA());
      var4.putHeader("credentials", "include");
      var4.putHeader("omitcorrelationid", "true");
      var4.putHeader("content-type", "application/json; charset=utf-8");
      var4.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
      var4.putHeader("omitcsrfjwt", "true");
      var4.putHeader("origin", "https://www.walmart.com");
      var4.putHeader("referer", "https://www.walmart.com/search/?query=" + this.searchQuery);
      var4.putHeader("accept-encoding", "gzip, deflate, br");
      var4.putHeader("accept-language", this.pxAPI.getDeviceLang());
      return var4;
   }

   public HttpRequest getCheckoutPage() {
      HttpRequest var1 = this.client.getAbs("https://www.walmart.com/checkout/").as(BodyCodec.none());
      var1.putHeaders(Headers$Pseudo.MASP.get());
      var1.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
      var1.putHeader("sec-ch-ua-mobile", "0?");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("user-agent", this.pxAPI.getDeviceUA());
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("service-worker-navigation-preload", "true");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("referer", "https://www.walmart.com/");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", this.pxAPI.getDeviceLang());
      return var1;
   }

   public HttpRequest addToCart() {
      if (this.isLoggedIn && !this.cookieStore().contains("CRT")) {
         this.cookieStore().put("CRT", UUID.randomUUID().toString(), ".walmart.com");
         this.cookieStore().put("hasCRT", "1", ".walmart.com");
      }

      switch (this.cartLogic) {
         case 0:
            return this.cartProdGuestCID();
         case 1:
            return this.cartSearchLiteATC();
         default:
            return this.cartSearchLiteATC();
      }
   }

   public CookieJar cookieStore() {
      return this.getWebClient().cookieStore();
   }

   public JsonObject getPaymentForm(PaymentToken var1) {
      Profile var2 = this.task.getProfile();
      JsonObject var3 = new JsonObject();
      var3.put("paymentType", "CREDITCARD");
      var3.put("cardType", var2.getPaymentMethod().get());
      var3.put("firstName", var2.getFirstName());
      var3.put("lastName", var2.getLastName());
      var3.put("addressLineOne", var2.getAddress1());
      var3.put("addressLineTwo", var2.getAddress2());
      var3.put("city", var2.getCity().toUpperCase());
      var3.put("state", var2.getState());
      var3.put("postalCode", var2.getZip());
      var3.put("expiryMonth", var2.getExpiryMonth());
      var3.put("expiryYear", var2.getExpiryYear());
      var3.put("email", var2.getEmail());
      var3.put("phone", var2.getPhone());
      var3.put("encryptedPan", var1.getEncryptedPan());
      var3.put("encryptedCvv", var1.getEncryptedCvv());
      var3.put("integrityCheck", var1.getIntegrityCheck());
      var3.put("keyId", var1.getKeyId());
      var3.put("phase", var1.getPhase());
      var3.put("piHash", var1.getPiHash());
      JsonObject var4 = new JsonObject();
      var4.put("payments", (new JsonArray()).add(var3));
      var4.put("cvvInSession", true);
      return var4;
   }

   public HttpRequest cartSearchLiteATC() {
      HttpRequest var1 = this.client.postAbs("https://www.walmart.com/api/v3/cart/lite/" + (this.isLoggedIn ? "customer/:CRT/items" : "guest/:CID/items")).as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
      var1.putHeader("omitcsrfjwt", "true");
      var1.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
      var1.putHeader("sec-ch-ua-mobile", "0?");
      var1.putHeader("user-agent", this.pxAPI.getDeviceUA());
      var1.putHeader("credentials", "include");
      var1.putHeader("omitcorrelationid", "true");
      var1.putHeader("content-type", "application/json");
      var1.putHeader("origin", "https://www.walmart.com");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.walmart.com/search/?query=" + this.searchQuery);
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", this.pxAPI.getDeviceLang());
      return var1;
   }

   public HttpRequest productPage() {
      HttpRequest var1 = this.client.getAbs(this.referer).as(BodyCodec.buffer());
      var1.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
      var1.putHeader("sec-ch-ua-mobile", "0?");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("user-agent", this.pxAPI.getDeviceUA());
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("sec-fetch-site", "cross-site");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", this.pxAPI.getDeviceLang());
      return var1;
   }

   public HttpRequest homepage() {
      HttpRequest var1 = this.client.getAbs("https://www.walmart.com/").as(BodyCodec.buffer());
      var1.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
      var1.putHeader("sec-ch-ua-mobile", "0?");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("user-agent", this.pxAPI.getDeviceUA());
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("sec-fetch-site", "cross-site");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("referer", "https://www.google.com/");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", this.pxAPI.getDeviceLang());
      return var1;
   }

   public CompletableFuture handleBadResponse(int var1, HttpResponse var2) {
      String var4;
      CompletableFuture var8;
      CompletableFuture var9;
      CompletableFuture var10000;
      CompletableFuture var10001;
      switch (var1) {
         case 307:
            try {
               if (this.task.getMode().contains("skip")) {
                  var10000 = this.generatePX(true);
                  if (!var10000.isDone()) {
                     var8 = var10000;
                     return var8.exceptionally(Function.identity()).thenCompose(WalmartAPIDesktop::async$handleBadResponse);
                  }

                  var10000.join();
                  return CompletableFuture.completedFuture(false);
               } else {
                  String var11 = "https://affil.walmart.com" + var2.getHeader("location");
                  var4 = var11.split("uuid=")[1].split("&")[0];
                  Matcher var12 = VID_LOCATION_PATTERN.matcher(var11);
                  String var13;
                  if (var12.find()) {
                     var13 = var12.group(1).isBlank() ? null : var12.group(1);
                  } else {
                     var13 = null;
                  }

                  var10001 = this.pxAPI.solveCaptcha(var13, var4, var11);
                  if (!var10001.isDone()) {
                     var9 = var10001;
                     return var9.exceptionally(Function.identity()).thenCompose(WalmartAPIDesktop::async$handleBadResponse);
                  } else {
                     byte var7 = this.parseMapIntoCookieJar((MultiMap)var10001.join());
                     var10001 = this.pxAPI.solve();
                     if (!var10001.isDone()) {
                        var9 = var10001;
                        return var9.exceptionally(Function.identity()).thenCompose(WalmartAPIDesktop::async$handleBadResponse);
                     }

                     this.parseMapIntoCookieJar((MultiMap)var10001.join());
                     return CompletableFuture.completedFuture(Boolean.valueOf((boolean)var7));
                  }
               }
            } catch (Throwable var10) {
               System.out.println("Error solving captcha [DESKTOP] " + var10.getMessage());
            }
         case 444:
            if (super.rotateProxy()) {
               this.pxAPI.restartClient(super.client);
            }
         default:
            return CompletableFuture.completedFuture(true);
         case 412:
            if (!this.task.getMode().contains("skip") && var2 != null) {
               JsonObject var3 = var2.bodyAsJsonObject();
               var4 = var3.getString("uuid");
               String var5 = var3.getString("vid");
               var10001 = this.pxAPI.solveCaptcha(var5, var4, (String)null);
               if (!var10001.isDone()) {
                  var9 = var10001;
                  return var9.exceptionally(Function.identity()).thenCompose(WalmartAPIDesktop::async$handleBadResponse);
               } else {
                  byte var6 = this.parseMapIntoCookieJar((MultiMap)var10001.join());
                  var10001 = this.pxAPI.solve();
                  if (!var10001.isDone()) {
                     var9 = var10001;
                     return var9.exceptionally(Function.identity()).thenCompose(WalmartAPIDesktop::async$handleBadResponse);
                  } else {
                     this.parseMapIntoCookieJar((MultiMap)var10001.join());
                     return CompletableFuture.completedFuture(Boolean.valueOf((boolean)var6));
                  }
               }
            } else {
               var10000 = this.generatePX(true);
               if (!var10000.isDone()) {
                  var8 = var10000;
                  return var8.exceptionally(Function.identity()).thenCompose(WalmartAPIDesktop::async$handleBadResponse);
               } else {
                  var10000.join();
                  return CompletableFuture.completedFuture(false);
               }
            }
      }
   }

   public HttpRequest cartProdGuestCID() {
      HttpRequest var1 = this.client.postAbs("https://www.walmart.com/api/v3/cart/" + (this.isLoggedIn ? "customer/:CRT/items" : "guest/:CID/items")).as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("accept", "application/json");
      var1.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
      var1.putHeader("sec-ch-ua-mobile", "0?");
      var1.putHeader("user-agent", this.pxAPI.getDeviceUA());
      var1.putHeader("content-type", "application/json");
      var1.putHeader("origin", "https://www.walmart.com");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", this.referer);
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", this.pxAPI.getDeviceLang());
      return var1;
   }

   public void setAPI(PerimeterX var1) {
      this.pxAPI = var1;
   }

   public JsonObject electrodeJson() {
      JsonObject var1 = new JsonObject();
      var1.put("0", (new JsonObject()).put("tags", "[\"info\",\"home-app, scus-prod-a6, PROD\"]").put("data", (new JsonObject()).put("_type", "fetch").put("extras", (new JsonObject()).put("response", (new JsonObject()).put("status", (Object)null)))));
      var1.put("1", (new JsonObject()).put("tags", "[\"info\",\"home-app, scus-prod-a6, PROD\"]").put("data", (new JsonObject()).put("_type", "fetch").put("extras", (new JsonObject()).put("response", (new JsonObject()).put("status", (Object)null)))));
      return var1;
   }

   public JsonObject productPageAtcForm(String var1, int var2) {
      JsonObject var3 = new JsonObject();
      var3.put("offerId", var1);
      var3.put("quantity", var2);
      var3.put("storeIds", this.storeIds);
      var3.put("location", (new JsonObject()).put("postalCode", Integer.parseInt(this.task.getProfile().getZip())).put("city", this.task.getProfile().getCity()).put("state", this.task.getProfile().getState()).put("isZipLocated", this.isZipLocated));
      var3.put("shipMethodDefaultRule", "SHIP_RULE_1");
      return var3;
   }

   public HttpRequest loginAccount() {
      HttpRequest var1 = this.client.postAbs("https://www.walmart.com/account/electrode/api/signin?tid=0&returnUrl=%2F").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("accept", "*/*");
      var1.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
      var1.putHeader("sec-ch-ua-mobile", "0?");
      var1.putHeader("user-agent", this.pxAPI.getDeviceUA());
      var1.putHeader("content-type", "application/json");
      var1.putHeader("origin", "https://www.walmart.com");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.walmart.com/account/login?tid=0&returnUrl=%2F");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", this.pxAPI.getDeviceLang());
      return var1;
   }

   public HttpRequest affilCrossSite(String var1) {
      this.getCookies().removeAnyMatch("CRT");
      this.getCookies().removeAnyMatch("cart-item-count");
      this.getCookies().removeAnyMatch("hasCRT");
      HttpRequest var2 = this.client.getAbs("https://affil.walmart.com/cart/" + var1 + "?items=" + this.task.getKeywords()[0] + "|" + this.task.getSize()).as(BodyCodec.none());
      var2.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
      var2.putHeader("sec-ch-ua-mobile", "0?");
      var2.putHeader("upgrade-insecure-requests", "1");
      var2.putHeader("user-agent", this.pxAPI.getDeviceUA());
      var2.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var2.putHeader("sec-fetch-site", "cross-site");
      var2.putHeader("sec-fetch-mode", "navigate");
      var2.putHeader("sec-fetch-user", "?1");
      var2.putHeader("sec-fetch-dest", "document");
      if (this.crossSite != null) {
         var2.putHeader("referer", "https://" + this.crossSite + "/");
         if (this.crossSite.equals("t.co")) {
            var2.headers().remove("sec-fetch-user");
         }
      }

      var2.putHeader("accept-encoding", this.pxAPI.getDeviceAcceptEncoding());
      var2.putHeader("accept-language", this.pxAPI.getDeviceLang());
      var2.putHeader("cookie", "DEFAULT_VALUE");
      if (this.dateOfPrevReq != null) {
         var2.putHeader("if-modified-since", this.dateOfPrevReq);
      }

      this.dateOfPrevReq = GMT_CHROME_RFC1123.format(ZonedDateTime.now(ZoneOffset.UTC));
      return var2;
   }

   public HttpRequest cartSearchGuestCRT() {
      HttpRequest var1 = this.client.postAbs("https://www.walmart.com/api/v3/cart/lite/:CRT/items").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
      var1.putHeader("omitcsrfjwt", "true");
      var1.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
      var1.putHeader("sec-ch-ua-mobile", "0?");
      var1.putHeader("user-agent", this.pxAPI.getDeviceUA());
      var1.putHeader("credentials", "include");
      var1.putHeader("omitcorrelationid", "true");
      var1.putHeader("content-type", "application/json");
      var1.putHeader("origin", "https://www.walmart.com");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.walmart.com/search/?query=panini");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", this.pxAPI.getDeviceLang());
      return var1;
   }

   public int getCartLogic() {
      return this.cartLogic;
   }

   public HttpRequest getPCID(PaymentToken var1) {
      HttpRequest var2 = this.client.postAbs("https://www.walmart.com/api/checkout/v3/contract?page=CHECKOUT_VIEW").as(BodyCodec.buffer());
      var2.putHeader("content-length", "DEFAULT_VALUE");
      var2.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
      var2.putHeader("wm_cvv_in_session", "true");
      var2.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
      var2.putHeader("sec-ch-ua-mobile", "0?");
      var2.putHeader("user-agent", this.pxAPI.getDeviceUA());
      var2.putHeader("wm_vertical_id", "0");
      var2.putHeader("content-type", "application/json");
      var2.putHeader("origin", "https://www.walmart.com");
      var2.putHeader("sec-fetch-site", "same-origin");
      var2.putHeader("sec-fetch-mode", "cors");
      var2.putHeader("sec-fetch-dest", "empty");
      var2.putHeader("referer", "https://www.walmart.com/checkout/");
      var2.putHeader("accept-encoding", "gzip, deflate, br");
      var2.putHeader("accept-language", this.pxAPI.getDeviceLang());
      return var2;
   }

   public HttpRequest affilRefresh(String var1) {
      HttpRequest var2 = this.client.getAbs("https://affil.walmart.com/cart/" + var1 + "?items=" + this.task.getKeywords()[0] + "|" + this.task.getSize()).as(BodyCodec.none());
      var2.putHeader("cache-control", "max-age=0");
      var2.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
      var2.putHeader("sec-ch-ua-mobile", "0?");
      var2.putHeader("upgrade-insecure-requests", "1");
      var2.putHeader("user-agent", this.pxAPI.getDeviceUA());
      var2.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var2.putHeader("sec-fetch-site", "cross-site");
      var2.putHeader("sec-fetch-mode", "navigate");
      var2.putHeader("sec-fetch-user", "?1");
      var2.putHeader("sec-fetch-dest", "document");
      var2.putHeader("accept-encoding", "gzip, deflate, br");
      var2.putHeader("accept-language", this.pxAPI.getDeviceLang());
      var2.putHeader("cookie", "DEFAULT_VALUE");
      if (this.dateOfPrevReq != null) {
         var2.putHeader("if-modified-since", this.dateOfPrevReq);
      }

      this.dateOfPrevReq = GMT_CHROME_RFC1123.format(ZonedDateTime.now(ZoneOffset.UTC));
      return var2;
   }

   public HttpRequest cartProdGuestCRT() {
      HttpRequest var1 = this.client.postAbs("https://www.walmart.com/api/v3/cart/:CRT/items").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("accept", "application/json");
      var1.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
      var1.putHeader("sec-ch-ua-mobile", "0?");
      var1.putHeader("user-agent", this.pxAPI.getDeviceUA());
      var1.putHeader("content-type", "application/json");
      var1.putHeader("origin", "https://www.walmart.com");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.walmart.com/ip/2020-21-Panini-Hoops-NBA-Basketball-Trading-Cards-Holiday-Blaster-Box-88-Cards-Retail-Exclusives/377461077");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", this.pxAPI.getDeviceLang());
      return var1;
   }

   public JsonObject getBillingForm(PaymentToken var1) {
      Profile var2 = this.task.getProfile();
      JsonObject var3 = new JsonObject();
      var3.put("encryptedPan", var1.getEncryptedPan());
      var3.put("encryptedCvv", var1.getEncryptedCvv());
      var3.put("integrityCheck", var1.getIntegrityCheck());
      var3.put("keyId", var1.getKeyId());
      var3.put("phase", var1.getPhase());
      var3.put("state", var2.getState());
      var3.put("postalCode", var2.getZip());
      var3.put("addressLineOne", var2.getAddress1());
      var3.put("addressLineTwo", var2.getAddress2());
      var3.put("city", var2.getCity().toUpperCase());
      var3.put("firstName", var2.getFirstName());
      var3.put("lastName", var2.getLastName());
      var3.put("expiryMonth", var2.getExpiryMonth());
      var3.put("expiryYear", var2.getExpiryYear());
      var3.put("phone", var2.getPhone());
      var3.put("cardType", var2.getPaymentMethod().get());
      var3.put("isGuest", true);
      return var3;
   }

   static {
      GMT_CHROME_RFC1123 = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH);
      VID_LOCATION_PATTERN = Pattern.compile("vid=(.*?)&");
   }

   public HttpRequest savedCart() {
      HttpRequest var1 = super.client.postAbs("https://www.walmart.com/api/v3/saved/:CID/items").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("accept", "application/json");
      var1.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
      var1.putHeader("sec-ch-ua-mobile", "0?");
      var1.putHeader("user-agent", this.pxAPI.getDeviceUA());
      var1.putHeader("content-type", "application/json");
      var1.putHeader("origin", "https://www.walmart.com");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.walmart.com/checkout/");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", this.pxAPI.getDeviceLang());
      return var1;
   }

   public HttpRequest fetchAffiliateCaptcha(String var1) {
      HttpRequest var2 = this.client.getAbs(var1).as(BodyCodec.buffer());
      var2.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
      var2.putHeader("sec-ch-ua-mobile", "0?");
      var2.putHeader("upgrade-insecure-requests", "1");
      var2.putHeader("user-agent", this.pxAPI.getDeviceUA());
      var2.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var2.putHeader("sec-fetch-site", "none");
      var2.putHeader("sec-fetch-mode", "navigate");
      var2.putHeader("sec-fetch-user", "?1");
      var2.putHeader("sec-fetch-dest", "document");
      var2.putHeader("accept-encoding", "gzip, deflate, br");
      var2.putHeader("accept-language", this.pxAPI.getDeviceLang());
      return var2;
   }

   public HttpRequest submitShipping() {
      HttpRequest var1 = this.client.postAbs("https://www.walmart.com/api/checkout/v3/contract/:PCID/shipping-address").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
      var1.putHeader("inkiru_precedence", "false");
      var1.putHeader("wm_cvv_in_session", "true");
      var1.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
      var1.putHeader("sec-ch-ua-mobile", "0?");
      var1.putHeader("user-agent", this.pxAPI.getDeviceUA());
      var1.putHeader("wm_vertical_id", "0");
      var1.putHeader("content-type", "application/json");
      var1.putHeader("origin", "https://www.walmart.com");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.walmart.com/checkout/");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", this.pxAPI.getDeviceLang());
      return var1;
   }

   public HttpRequest getCart() {
      HttpRequest var1 = this.client.getAbs("https://www.walmart.com/cart").as(BodyCodec.buffer());
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
      var1.putHeader("sec-ch-ua-mobile", "0?");
      var1.putHeader("user-agent", this.pxAPI.getDeviceUA());
      var1.putHeader("origin", "https://www.walmart.com");
      var1.putHeader("referer", "https://www.walmart.com/");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", this.pxAPI.getDeviceLang());
      return var1;
   }

   public JsonObject accountLoginForm(Account var1) {
      JsonObject var2 = new JsonObject();
      var2.put("username", var1.getUser());
      var2.put("password", var1.getPass());
      var2.put("rememberme", true);
      var2.put("showRememberme", "true");
      var2.put("captcha", (new JsonObject()).put("sensorData", ""));
      return var2;
   }

   public HttpRequest cart() {
      HttpRequest var1 = this.client.getAbs("https://www.walmart.com/cart").as(BodyCodec.none());
      var1.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
      var1.putHeader("sec-ch-ua-mobile", "0?");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("user-agent", this.pxAPI.getDeviceUA());
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("service-worker-navigation-preload", "true");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("referer", "https://www.walmart.com/");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", this.pxAPI.getDeviceLang());
      return var1;
   }

   public HttpRequest googlePrefetch() {
      HttpRequest var1 = this.client.getAbs("https://www.walmart.com/").as(BodyCodec.none());
      var1.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
      var1.putHeader("sec-ch-ua-mobile", "0?");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("user-agent", this.pxAPI.getDeviceUA());
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("purpose", "prefetch");
      var1.putHeader("sec-fetch-site", "cross-site");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("referer", "https://www.google.com/");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", this.pxAPI.getDeviceLang());
      return var1;
   }

   public HttpRequest cartSuggestedCartPage() {
      HttpRequest var1 = this.client.postAbs("https://www.walmart.com/api/v3/cart/:CRT/items").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
      var1.putHeader("sec-ch-ua-mobile", "0?");
      var1.putHeader("user-agent", this.pxAPI.getDeviceUA());
      var1.putHeader("credentials", "include");
      var1.putHeader("content-type", "application/json");
      var1.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
      var1.putHeader("omitcsrfjwt", "true");
      var1.putHeader("wm_qos.correlation_id", "df2291f5-73f5-494a-bfa8-dd0dbdc3f585");
      var1.putHeader("origin", "https://www.walmart.com");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.walmart.com/cart");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", this.pxAPI.getDeviceLang());
      return var1;
   }

   public static CompletableFuture async$generatePX(WalmartAPIDesktop var0, int var1, WalmartAPIDesktop var2, CompletableFuture var3, int var4, Object var5) {
      Exception var10;
      label30: {
         boolean var11;
         WalmartAPIDesktop var10000;
         CompletableFuture var10001;
         switch (var4) {
            case 0:
               try {
                  if (var1 != 0) {
                     var0.pxAPI.reset();
                  }

                  var10000 = var0;
                  var10001 = var0.pxAPI.solve();
                  if (!var10001.isDone()) {
                     CompletableFuture var9 = var10001;
                     return var9.exceptionally(Function.identity()).thenCompose(WalmartAPIDesktop::async$generatePX);
                  }
                  break;
               } catch (Exception var7) {
                  var10 = var7;
                  var11 = false;
                  break label30;
               }
            case 1:
               var10000 = var2;
               var10001 = var3;
               break;
            default:
               throw new IllegalArgumentException();
         }

         try {
            return CompletableFuture.completedFuture(var10000.parseMapIntoCookieJar((MultiMap)var10001.join()));
         } catch (Exception var6) {
            var10 = var6;
            var11 = false;
         }
      }

      Exception var8 = var10;
      System.out.println("Error generating desktop session: " + var8.getMessage());
      return CompletableFuture.completedFuture(true);
   }

   public JsonObject getAtcForm(String var1, int var2) {
      this.isZipLocated = this.client.cookieStore().asString().contains(this.task.getProfile().getZip());
      JsonObject var3 = new JsonObject();
      var3.put("quantity", var2);
      var3.put("actionType", "INCREASE");
      var3.put("customAttributes", new JsonArray("[{\"type\":\"NON_DISPLAY\",\"name\":\"pita\",\"value\":0}]"));
      var3.put("location", (new JsonObject()).put("postalCode", Integer.parseInt(this.task.getProfile().getZip())).put("city", this.task.getProfile().getCity()).put("state", this.task.getProfile().getState()).put("isZipLocated", this.isZipLocated));
      var3.put("storeIds", this.storeIds);
      var3.put("offerId", var1);
      return var3;
   }

   public HttpRequest transferCart(String var1) {
      HttpRequest var2 = super.client.postAbs("https://www.walmart.com/api//saved/:CID/items/" + var1 + "/transfer").as(BodyCodec.buffer());
      var2.putHeader("content-length", "DEFAULT_VALUE");
      var2.putHeader("accept", "application/json");
      var2.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
      var2.putHeader("sec-ch-ua-mobile", "0?");
      var2.putHeader("user-agent", this.pxAPI.getDeviceUA());
      var2.putHeader("content-type", "application/json");
      var2.putHeader("origin", "https://www.walmart.com");
      var2.putHeader("sec-fetch-site", "same-origin");
      var2.putHeader("sec-fetch-mode", "cors");
      var2.putHeader("sec-fetch-dest", "empty");
      var2.putHeader("referer", "https://www.walmart.com/checkout/");
      var2.putHeader("accept-encoding", "gzip, deflate, br");
      var2.putHeader("accept-language", this.pxAPI.getDeviceLang());
      return var2;
   }

   public JsonObject accountCreateForm() {
      JsonObject var1 = new JsonObject();
      var1.put("personName", (new JsonObject()).put("firstName", this.task.getProfile().getFirstName()).put("lastName", this.task.getProfile().getLastName()));
      String var10002 = this.task.getProfile().getEmail().split("@")[0];
      var1.put("email", var10002 + "+" + ThreadLocalRandom.current().nextInt(999999999) + "@" + this.task.getProfile().getEmail().split("@")[1]);
      var1.put("password", Utils.generateStrongString());
      var1.put("rememberme", true);
      var1.put("showRememberme", "true");
      var1.put("emailNotificationAccepted", true);
      var1.put("captcha", (new JsonObject()).put("sensorData", "2a25G2m84Vrp0o9c4229201.12-1,8,-36,-890,Mozilla/9.8 (Macintosh; Intel Mac OS X 74_96_3) AppleWebKit/227.39 (KHTML, like Gecko) Chrome/99.3.4477.577 Safari/523.90,uaend,15246,46458276,en-US,Gecko,4,9,7,3,496163,5670088,2788,7415,8699,4229,3176,055,8565,,cpen:8,i0:7,dm:9,cwen:1,non:4,opc:6,fc:7,sc:3,wrc:7,isc:9,vib:1,bat:3,x80:7,x22:7,4393,7.763700514938,983668060469.9,loc:-8,5,-80,-529,do_en,dm_en,t_en-0,9,-04,-369,8,9,0,1,629,784,8;8,0,0,2,745,201,7;0,-4,0,6,-5,-2,9;8,-2,9,2,-3,-8,0;0,3,9,3,5472,834,3;0,-3,4,9,8072,131,6;6,4,1,0,490,516,6;3,1,9,8,4269,047,8;7,2,0,2,631,948,7;0,-4,0,6,-5,-2,9;8,-2,9,2,-3,-8,0;0,3,9,3,000,540,0;2,9,2,5,9223,586,0;6,-5,8,7,-8,-2,9;3,-3,1,9,-1,-1,6;6,4,1,0,8460,899,4;8,-0,7,4,1285,249,7;0,2,1,0,109,337,0;1,1,9,3,5589,780,3;0,8,6,3,2904,551,9;3,4,8,8,1270,879,6;3,1,9,8,4515,047,8;7,2,0,2,649,278,7;0,2,1,0,3713,929,7;3,2,6,7,3470,1858,0;6,-5,8,7,2188,516,6;3,1,9,7,5125,047,8;7,-8,3,0,8320,337,0;1,3,9,3,6461,780,3;1,-3,4,9,9178,193,6;7,2,1,0,8521,745,4;-2,1,-97,-064,4,0,7,1,551,482,4;9,7,0,1,982,047,8;7,-8,3,0,-3,-3,1;0,-1,1,9,-7,-9,7;0,2,2,0,3833,006,7;3,-0,3,5,9244,524,0;6,8,3,2,694,830,6;7,2,2,0,8191,745,4;8,9,0,1,878,784,8;7,-8,3,0,-3,-3,1;0,-1,1,9,-7,-9,7;0,2,1,0,846,667,0;1,1,9,3,5335,780,3;0,-3,4,8,-0,-1,1;0,-7,2,1,-1,-4,0;6,8,2,2,0409,036,2;4,-2,9,8,4422,085,8;7,2,0,2,807,948,7;1,0,1,0,3940,952,7;3,2,6,7,3021,590,1;0,2,4,9,8474,193,6;7,2,1,0,8447,745,4;8,9,0,1,886,014,8;7,2,0,2,0797,031,9;7,5,0,7,7081,8052,3;0,-3,4,8,9382,830,6;7,2,1,9,9057,745,4;8,-0,7,3,2028,948,7;1,2,1,0,4822,952,7;4,-0,2,5,0340,586,0;7,6,2,2,0560,982,2;-3,3,-91,-207,2,5,0123,-2,1,9,191;9,0,2757,-1,2,4,744;2,1,3079,-6,8,7,907;4,2,4625,-9,0,0,036;6,5,0285,-2,1,9,191;3,0,2819,-1,2,4,744;6,2,3128,-6,8,7,907;8,1,4847,-9,0,0,036;0,5,0386,-2,1,9,191;7,0,2911,-1,2,4,744;17,5,2182,-4,1,9,630;10,3,6412,-2,0,1,899;54,4,1201,-2,6,6,118;03,2,3262,-6,8,7,907;24,7,8607,-1,7,3,923;79,1,9186,-3,9,2,355;85,9,5448,-8,2,1,834;48,1,4923,-9,0,0,036;34,3,3164,9,1,9,191;90,1,9579,8,2,4,638;27,4,2616,-4,1,9,524;20,5,6935,-2,0,1,783;64,2,1884,-2,6,6,002;13,3,3800,-6,8,7,891;34,8,8284,-1,7,3,817;89,9,9764,-3,9,2,249;95,0,5966,-8,2,1,728;58,0,4400,-9,0,0,920;44,5,3558,-5,0,6,463;36,1,2183,-4,4,8,511;33,3,8038,-3,9,7,131;93,6,0950,-2,1,9,085;13,1,9957,-8,6,2,990;03,3,1555,-0,7,0,852;20,8,4759,-9,3,0,447;53,8,2687,-1,2,4,638;33,6,2846,-4,1,9,524;36,3,6170,-2,0,1,783;70,4,2070,-2,6,6,002;29,1,4097,-6,8,7,891;50,9,9429,-1,7,3,817;05,9,0912,-3,9,2,249;11,0,6114,-8,2,1,728;74,0,5665,-9,0,0,920;60,5,4713,-5,0,6,463;52,2,3378,-4,4,8,511;49,2,9222,-3,9,7,131;09,6,1149,-2,1,9,085;29,1,0014,-8,6,2,990;19,3,2603,-0,7,0,852;46,7,5138,9,0,1,783;93,3,2474,0,9,2,948;21,8,6805,-8,2,1,427;84,2,5348,-9,0,0,629;70,3,4447,-5,0,6,162;62,3,3067,-4,4,8,210;59,1,9930,-3,9,7,830;19,7,1845,-2,1,9,784;39,0,0718,-8,6,2,699;29,4,2304,-0,7,0,551;56,8,5610,-9,3,0,146;89,8,3518,-1,2,4,337;69,6,3777,-4,1,9,223;62,4,7012,-2,0,1,482;06,3,2805,-2,6,6,701;55,2,4842,-6,8,7,590;76,8,9277,-1,7,3,516;21,9,0829,-3,9,2,948;37,0,6021,-8,2,1,427;90,0,5575,-9,0,0,629;96,5,4623,-5,0,6,162;88,1,3258,-4,4,8,210;75,3,9102,-3,9,7,830;35,5,1018,-2,1,9,784;55,2,0970,-8,6,2,699;45,2,2573,-0,7,0,551;62,9,5764,-9,3,0,146;95,9,3682,-1,2,4,337;75,5,3841,-4,1,9,223;78,4,7171,-2,0,1,482;22,3,2967,-2,6,6,701;71,2,4983,-6,8,7,590;92,7,0485,-1,7,3,516;47,1,1964,-3,9,2,948;53,8,7173,-8,2,1,427;16,2,6617,-9,0,0,629;02,3,5765,-5,0,6,162;94,3,4385,-4,4,8,210;81,1,0246,-3,9,7,830;41,7,2151,-2,1,9,784;71,0,1021,-8,6,2,699;61,4,3616,-0,7,0,551;88,8,6925,-9,3,0,146;11,9,4825,-1,2,4,337;91,5,4004,-4,1,9,223;94,4,8326,-2,0,1,482;38,3,3119,-2,6,6,701;87,1,5323,70,8,5,590;08,7,0795,-1,7,1,516;53,1,1274,-3,9,0,948;890,2,5416,70,8,7,590;200,4,8621,-2,0,1,482;520,8,4303,-1,2,4,337;173,4,3227,-0,7,0,551;066,6,2879,-2,1,9,784;912,1,4019,-4,4,8,210;137,2,6341,-9,0,0,629;361,0,1672,-3,9,2,948;898,1,5834,-6,8,7,590;208,5,8029,-2,0,1,482;538,8,4608,-1,2,4,337;181,4,3521,-0,7,0,551;074,6,2017,-2,1,9,784;920,2,4285,-4,4,8,210;145,0,7789,-9,0,0,629;379,1,2044,-3,9,2,948;806,2,6266,-6,8,7,590;216,3,9829,-2,0,1,482;536,0,5341,-1,2,4,337;189,3,4340,-0,7,0,551;082,5,3864,-2,1,9,784;938,3,5005,-4,4,8,210;153,1,7407,-9,0,0,629;387,9,2803,-3,9,2,948;814,3,6992,-6,8,7,590;224,3,9178,-2,0,1,482;544,0,5690,-1,2,4,337;197,3,5687,-0,7,0,551;080,6,4271,-2,1,9,784;936,1,6529,5,6,2,699;833,2,2446,7,7,0,814;093,5,4634,86,1,7,047;949,1,6115,-4,4,6,573;164,2,8447,-9,0,8,982;398,9,3759,-3,9,0,201;825,3,7848,-6,8,5,853;235,3,0041,-2,0,9,745;555,0,6563,-1,2,2,690;108,3,5403,-0,7,8,814;091,6,4968,-2,1,7,047;957,2,6213,78,4,8,573;172,1,8563,-9,0,0,982;306,9,3849,-3,9,2,201;-0,9,-04,-374,8,8,387,2282,747;2,0,020,1150,523;9,1,375,0926,386;2,8,670,7564,913;4,1,476,3838,281;2,4,380,7647,850;6,2,372,5331,119;0,1,043,3808,222;9,0,607,9192,992;9,7,003,057,028;19,3,895,658,373;07,7,674,858,669;74,5,328,911,479;39,3,696,637,377;78,9,234,958,258;57,2,468,230,036;34,8,547,014,689;98,0,297,995,083;25,1,642,866,876;88,8,937,528,648;10,1,742,165,289;28,4,657,542,545;92,2,539,217,168;26,1,210,079,388;55,0,975,602,454;47,9,563,916,249,-0;55,6,061,549,532,-5;21,1,695,832,188,-8;93,6,8393,117,469,-4;97,6,2675,696,089,-3;31,3,7464,898,933,-2;-1,2,-93,-759,-8,2,-25,-737,-9,9,-64,-195,-5,0,-84,-424,-3,6,-01,-806,-0,4,-12,-019,-2,1,-58,-284,387567,00139,7,0,0,1,621711,9189,1939259142701,1322496127433,99,87740,769,381,1975,5,9,0143,787515,2,fqyq1a9pf0du6kpbu6e0_3626,2828,892,189335680,61257857-9,9,-64,-192,-5,8-8,2,-25,-32,-3086997364;9,39,33,85,88,30,80,61,41,32,67,23,92,21,33,47,6,3,4;,5,5;true;true;true;147;true;56;58;true;false;-5-9,9,-64,-89,9874-2,1,-97,-078,09316063-4,2,-10,-925,245534-7,8,-75,-191,;13;2;9"));
      return var1;
   }

   public void setLoggedIn(boolean var1) {
      this.isLoggedIn = var1;
   }

   public HttpRequest selectShipping() {
      HttpRequest var1 = this.client.postAbs("https://www.walmart.com/api/checkout/v3/contract/:PCID/fulfillment").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
      var1.putHeader("inkiru_precedence", "false");
      var1.putHeader("wm_cvv_in_session", "true");
      var1.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
      var1.putHeader("sec-ch-ua-mobile", "0?");
      var1.putHeader("user-agent", this.pxAPI.getDeviceUA());
      var1.putHeader("wm_vertical_id", "0");
      var1.putHeader("content-type", "application/json");
      var1.putHeader("origin", "https://www.walmart.com");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.walmart.com/checkout/");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", this.pxAPI.getDeviceLang());
      return var1;
   }

   public HttpRequest getCartV3(String var1) {
      HttpRequest var2 = this.client.postAbs("https://www.walmart.com/api/v3/cart/" + var1).as(BodyCodec.buffer());
      var2.putHeader("content-length", "DEFAULT_VALUE");
      var2.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
      var2.putHeader("sec-ch-ua-mobile", "0?");
      var2.putHeader("user-agent", this.pxAPI.getDeviceUA());
      var2.putHeader("credentials", "include");
      var2.putHeader("omitcorrelationid", "true");
      var2.putHeader("content-type", "application/json");
      var2.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
      var2.putHeader("omitcsrfjwt", "true");
      var2.putHeader("origin", "https://www.walmart.com");
      var2.putHeader("referer", "https://www.walmart.com/search/?query=" + this.searchQuery);
      var2.putHeader("accept-encoding", "gzip, deflate, br");
      var2.putHeader("accept-language", this.pxAPI.getDeviceLang());
      return var2;
   }

   public JsonObject getProcessingForm() {
      return new JsonObject();
   }

   public HttpRequest processPayment(PaymentToken var1) {
      HttpRequest var2 = this.client.putAbs("https://www.walmart.com/api/checkout/v3/contract/:PCID/order").as(BodyCodec.buffer());
      var2.putHeader("content-length", "DEFAULT_VALUE");
      var2.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
      var2.putHeader("inkiru_precedence", "false");
      var2.putHeader("wm_cvv_in_session", "true");
      var2.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
      var2.putHeader("sec-ch-ua-mobile", "0?");
      var2.putHeader("user-agent", this.pxAPI.getDeviceUA());
      var2.putHeader("wm_vertical_id", "0");
      var2.putHeader("content-type", "application/json");
      var2.putHeader("origin", "https://www.walmart.com");
      var2.putHeader("sec-fetch-site", "same-origin");
      var2.putHeader("sec-fetch-mode", "cors");
      var2.putHeader("sec-fetch-dest", "empty");
      var2.putHeader("referer", "https://www.walmart.com/checkout/");
      var2.putHeader("accept-encoding", "gzip, deflate, br");
      var2.putHeader("accept-language", this.pxAPI.getDeviceLang());
      return var2;
   }

   public HttpRequest pxScript() {
      HttpRequest var1 = this.client.getAbs("https://www.walmart.com/px/PXu6b0qd2S/init.js").as(BodyCodec.buffer());
      var1.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
      var1.putHeader("sec-ch-ua-mobile", "0?");
      var1.putHeader("user-agent", this.pxAPI.getDeviceUA());
      var1.putHeader("accept", "application/signed-exchange;v=b3;q=0.9,*/*;q=0.8");
      var1.putHeader("purpose", "prefetch");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "no-cors");
      var1.putHeader("sec-fetch-dest", "script");
      var1.putHeader("referer", "https://www.walmart.com/");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", this.pxAPI.getDeviceLang());
      return var1;
   }

   public WalmartAPIDesktop(Task var1) {
      super(ClientType.CHROME);
      this.task = var1;
      this.crossSite = crossSiteList[ThreadLocalRandom.current().nextInt(crossSiteList.length)];
      this.searchQuery = searchQueries[ThreadLocalRandom.current().nextInt(searchQueries.length)];
      this.storeIds = (new JsonArray()).add(ThreadLocalRandom.current().nextInt(1904, 5969)).add(ThreadLocalRandom.current().nextInt(1904, 5969)).add(ThreadLocalRandom.current().nextInt(1904, 5969)).add(ThreadLocalRandom.current().nextInt(1904, 5969)).add(ThreadLocalRandom.current().nextInt(1904, 5969));
      this.referer = ((RefererStoreController)Engine.get().getModule(Controller.REFERER)).getRandomReferer();
      this.cartLogic = ThreadLocalRandom.current().nextInt(2);
      this.isLoggedIn = false;
   }

   public JsonObject getProcessingForm(PaymentToken var1) {
      JsonObject var2 = new JsonObject();
      var2.put("paymentType", "CREDITCARD");
      var2.put("encryptedCvv", var1.getEncryptedCvv());
      var2.put("encryptedPan", var1.getEncryptedPan());
      var2.put("integrityCheck", var1.getIntegrityCheck());
      var2.put("keyId", var1.getKeyId());
      var2.put("phase", var1.getPhase());
      JsonObject var3 = new JsonObject();
      var3.put("cvvInSession", true);
      var3.put("voltagePayments", (new JsonArray()).add(var2));
      return var3;
   }

   public void close() {
      if (this.pxAPI != null) {
         this.pxAPI.close();
      }

      super.close();
   }

   public static CompletableFuture async$initialisePX(WalmartAPIDesktop var0, CompletableFuture var1, int var2, Object var3) {
      CompletableFuture var10000;
      switch (var2) {
         case 0:
            var10000 = var0.pxAPI.initialise();
            if (!var10000.isDone()) {
               var1 = var10000;
               return var1.exceptionally(Function.identity()).thenCompose(WalmartAPIDesktop::async$initialisePX);
            }
            break;
         case 1:
            var10000 = var1;
            break;
         default:
            throw new IllegalArgumentException();
      }

      var10000.join();
      return CompletableFuture.completedFuture(true);
   }

   public CompletableFuture generatePX(boolean var1) {
      try {
         if (var1 != 0) {
            this.pxAPI.reset();
         }

         CompletableFuture var10001 = this.pxAPI.solve();
         if (!var10001.isDone()) {
            CompletableFuture var4 = var10001;
            return var4.exceptionally(Function.identity()).thenCompose(WalmartAPIDesktop::async$generatePX);
         } else {
            return CompletableFuture.completedFuture(this.parseMapIntoCookieJar((MultiMap)var10001.join()));
         }
      } catch (Exception var5) {
         System.out.println("Error generating desktop session: " + var5.getMessage());
         return CompletableFuture.completedFuture(true);
      }
   }

   public boolean parseMapIntoCookieJar(MultiMap var1) {
      if (var1 == null) {
         return false;
      } else {
         Iterator var2 = var1.entries().iterator();

         while(var2.hasNext()) {
            Map.Entry var3 = (Map.Entry)var2.next();
            this.getWebClient().cookieStore().put((String)var3.getKey(), (String)var3.getValue(), ".walmart.com");
         }

         return !var1.isEmpty();
      }
   }

   public static CompletableFuture async$handleBadResponse(WalmartAPIDesktop var0, int var1, HttpResponse var2, CompletableFuture var3, JsonObject var4, String var5, String var6, WalmartAPIDesktop var7, int var8, Matcher var9, String var10, int var11, Object var12) {
      CompletableFuture var27;
      label136: {
         WalmartAPIDesktop var10000;
         CompletableFuture var10001;
         int var23;
         label119: {
            JsonObject var19;
            String var20;
            CompletableFuture var26;
            label145: {
               label116: {
                  Throwable var30;
                  label115: {
                     int var24;
                     boolean var29;
                     label138: {
                        label112: {
                           String var18;
                           Matcher var21;
                           String var10003;
                           label101:
                           switch (var11) {
                              case 0:
                                 CompletableFuture var25;
                                 switch (var1) {
                                    case 307:
                                       try {
                                          if (var0.task.getMode().contains("skip")) {
                                             var27 = var0.generatePX(true);
                                             if (!var27.isDone()) {
                                                var25 = var27;
                                                return var25.exceptionally(Function.identity()).thenCompose(WalmartAPIDesktop::async$handleBadResponse);
                                             }
                                             break label112;
                                          }
                                       } catch (Throwable var17) {
                                          var30 = var17;
                                          var29 = false;
                                          break label115;
                                       }

                                       try {
                                          var18 = "https://affil.walmart.com" + var2.getHeader("location");
                                          var20 = var18.split("uuid=")[1].split("&")[0];
                                          var21 = VID_LOCATION_PATTERN.matcher(var18);
                                          if (var21.find()) {
                                             var6 = var21.group(1).isBlank() ? null : var21.group(1);
                                          } else {
                                             var6 = null;
                                          }

                                          var10000 = var0;
                                          var10001 = var0.pxAPI.solveCaptcha(var6, var20, var18);
                                          if (!var10001.isDone()) {
                                             var26 = var10001;
                                             return var26.exceptionally(Function.identity()).thenCompose(WalmartAPIDesktop::async$handleBadResponse);
                                          }
                                          break label101;
                                       } catch (Throwable var16) {
                                          var30 = var16;
                                          var29 = false;
                                          break label115;
                                       }
                                    case 412:
                                       if (!var0.task.getMode().contains("skip") && var2 != null) {
                                          var19 = var2.bodyAsJsonObject();
                                          var20 = var19.getString("uuid");
                                          var5 = var19.getString("vid");
                                          var10000 = var0;
                                          var10001 = var0.pxAPI.solveCaptcha(var5, var20, (String)null);
                                          if (!var10001.isDone()) {
                                             var26 = var10001;
                                             return var26.exceptionally(Function.identity()).thenCompose(WalmartAPIDesktop::async$handleBadResponse);
                                          }
                                          break label145;
                                       }

                                       var27 = var0.generatePX(true);
                                       if (!var27.isDone()) {
                                          var25 = var27;
                                          return var25.exceptionally(Function.identity()).thenCompose(WalmartAPIDesktop::async$handleBadResponse);
                                       }
                                       break label136;
                                    case 444:
                                       break label116;
                                    default:
                                       return CompletableFuture.completedFuture(true);
                                 }
                              case 1:
                                 var27 = var3;
                                 break label136;
                              case 2:
                                 var10000 = var7;
                                 var10001 = var3;
                                 JsonObject var28 = var4;
                                 var10003 = var5;
                                 var5 = var6;
                                 var20 = var10003;
                                 var19 = var28;
                                 break label145;
                              case 3:
                                 var10000 = var7;
                                 var10001 = var3;
                                 var23 = var8;
                                 break label119;
                              case 4:
                                 var27 = var3;
                                 break label112;
                              case 5:
                                 var10000 = var7;
                                 var10001 = var3;
                                 String var10002 = var5;
                                 var10003 = var6;
                                 var6 = var10;
                                 var21 = var9;
                                 var20 = var10003;
                                 var18 = var10002;
                                 break;
                              case 6:
                                 var10000 = var7;
                                 var10001 = var3;
                                 var24 = var8;
                                 break label138;
                              default:
                                 throw new IllegalArgumentException();
                           }

                           try {
                              var24 = var10000.parseMapIntoCookieJar((MultiMap)var10001.join());
                              var10000 = var0;
                              var10001 = var0.pxAPI.solve();
                              if (!var10001.isDone()) {
                                 var26 = var10001;
                                 return var26.exceptionally(Function.identity()).thenCompose(WalmartAPIDesktop::async$handleBadResponse);
                              }
                              break label138;
                           } catch (Throwable var15) {
                              var30 = var15;
                              var29 = false;
                              break label115;
                           }
                        }

                        try {
                           var27.join();
                           return CompletableFuture.completedFuture(false);
                        } catch (Throwable var14) {
                           var30 = var14;
                           var29 = false;
                           break label115;
                        }
                     }

                     try {
                        var10000.parseMapIntoCookieJar((MultiMap)var10001.join());
                        return CompletableFuture.completedFuture(Boolean.valueOf((boolean)var24));
                     } catch (Throwable var13) {
                        var30 = var13;
                        var29 = false;
                     }
                  }

                  Throwable var22 = var30;
                  System.out.println("Error solving captcha [DESKTOP] " + var22.getMessage());
               }

               if (var0.rotateProxy()) {
                  var0.pxAPI.restartClient(var0.client);
               }

               return CompletableFuture.completedFuture(true);
            }

            var23 = var10000.parseMapIntoCookieJar((MultiMap)var10001.join());
            var10000 = var0;
            var10001 = var0.pxAPI.solve();
            if (!var10001.isDone()) {
               var26 = var10001;
               return var26.exceptionally(Function.identity()).thenCompose(WalmartAPIDesktop::async$handleBadResponse);
            }
         }

         var10000.parseMapIntoCookieJar((MultiMap)var10001.join());
         return CompletableFuture.completedFuture(Boolean.valueOf((boolean)var23));
      }

      var27.join();
      return CompletableFuture.completedFuture(false);
   }
}
