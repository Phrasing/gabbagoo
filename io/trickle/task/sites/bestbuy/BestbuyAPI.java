package io.trickle.task.sites.bestbuy;

import io.trickle.account.Account;
import io.trickle.profile.Profile;
import io.trickle.task.Task;
import io.trickle.task.antibot.impl.akamai.HawkAPI;
import io.trickle.util.Utils;
import io.trickle.webclient.TaskApiClient;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.codec.BodyCodec;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class BestbuyAPI extends TaskApiClient {
   public HawkAPI hawkAPI;
   public String paymentId;
   public String orderId;
   public String sensorUrl = "https://www.bestbuy.com/2yECBVJi5xf17/zRM/BInoAzUb5KA/YDGOzGht/VjtXHEtQXw/Wmt4/NTM9SV8";
   public Login login;
   public String id;
   public boolean isInstore;
   public String store;
   public Task task;
   public String cartId;
   public String SKU;
   public static DateTimeFormatter BASIC_ISO_DATE;
   public String encryptedCard;
   public String userAgent;
   public static String QUEUE_URL = "https://www.bestbuy.com/cart/api/v1/addToCart";

   public JsonObject atcForm() {
      JsonObject var1 = new JsonObject();
      if (this.isInstore) {
         var1.put("items", (new JsonArray()).add((new JsonObject()).put("skuId", this.SKU).put("storeId", this.store).put("quantity", 1)));
      } else {
         var1.put("items", (new JsonArray()).add((new JsonObject()).put("skuId", this.SKU)));
      }

      return var1;
   }

   public HttpRequest refreshCheckout() {
      HttpRequest var1 = this.client.getAbs("https://www.bestbuy.com/checkout/orders/refreshToken/").as(BodyCodec.buffer());
      var1.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"96\", \"Google Chrome\";v=\"96\"");
      var1.putHeader("x-user-interface", "DotCom-Optimized");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", this.userAgent);
      var1.putHeader("content-type", "application/json");
      var1.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
      var1.putHeader("x-native-checkout-version", "__VERSION__");
      var1.putHeader("sec-ch-ua-platform", "\"macOS\"");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.bestbuy.com/checkout/r/payment");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public HttpRequest ciaGrid(String var1) {
      HttpRequest var2 = this.client.getAbs("https://www.bestbuy.com/api/csiservice/v2/key/cia-grid").as(BodyCodec.buffer());
      var2.putHeader("sec-ch-ua", "\"Chromium\";v=\"94\", \"Google Chrome\";v=\"94\", \";Not A Brand\";v=\"99\"");
      var2.putHeader("sec-ch-ua-mobile", "?0");
      var2.putHeader("user-agent", this.userAgent);
      var2.putHeader("sec-ch-ua-platform", "\"macOS\"");
      var2.putHeader("content-type", "application/json");
      var2.putHeader("accept", "*/*");
      var2.putHeader("sec-fetch-site", "same-origin");
      var2.putHeader("sec-fetch-mode", "cors");
      var2.putHeader("sec-fetch-dest", "empty");
      var2.putHeader("referer", var1);
      var2.putHeader("accept-encoding", "gzip, deflate, br");
      var2.putHeader("accept-language", "en-US,en;q=0.9");
      return var2;
   }

   public HttpRequest submit3dAuth() {
      HttpRequest var1 = this.client.postAbs("https://www.bestbuy.com/checkout/api/1.0/paysecure/submitCardAuthentication").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"93\", \" Not;A Brand\";v=\"99\", \"Chromium\";v=\"93\"");
      var1.putHeader("x-user-interface", "DotCom-Optimized");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", this.userAgent);
      var1.putHeader("content-type", "application/json");
      var1.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
      var1.putHeader("x-native-checkout-version", "__VERSION__");
      var1.putHeader("sec-ch-ua-platform", "\"macOS\"");
      var1.putHeader("origin", "https://www.bestbuy.com");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.bestbuy.com/checkout/r/payment");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public HttpRequest homepage() {
      HttpRequest var1 = this.client.getAbs("https://www.bestbuy.com/").as(BodyCodec.buffer());
      var1.putHeader("cache-control", "max-age=0");
      var1.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"96\", \"Google Chrome\";v=\"96\"");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("sec-ch-ua-platform", "\"macOS\"");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("user-agent", this.userAgent);
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("sec-fetch-site", "none");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public JsonObject submit3dJson(String var1) {
      JsonObject var2 = new JsonObject();
      var2.put("orderId", this.id);
      var2.put("threeDSecureStatus", (new JsonObject()).put("threeDSReferenceId", var1));
      return var2;
   }

   public BestbuyAPI(Task var1) {
      this.task = var1;
      this.hawkAPI = new HawkAPI();
      this.SKU = this.task.getKeywords()[0].split("skuId=")[1].split("&")[0];
      this.isInstore = var1.getMode().contains("store");
   }

   public HttpRequest setAsDelivery() {
      HttpRequest var1 = this.client.putAbs("https://www.bestbuy.com/cart/item/" + this.id + "/fulfillment").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"93\", \" Not;A Brand\";v=\"99\", \"Chromium\";v=\"93\"");
      var1.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
      var1.putHeader("content-type", "application/json");
      var1.putHeader("x-order-id", this.orderId);
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", this.userAgent);
      var1.putHeader("sec-ch-ua-platform", "\"macOS\"");
      var1.putHeader("origin", "https://www.bestbuy.com");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.bestbuy.com/cart");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public HttpRequest getCartItems() {
      HttpRequest var1 = this.client.getAbs("https://www.bestbuy.com/cart/json?isDeviceApplePayEligible=false").as(BodyCodec.buffer());
      var1.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"96\", \"Google Chrome\";v=\"96\"");
      var1.putHeader("dnt", "1");
      var1.putHeader("x-order-id", "");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", this.userAgent);
      var1.putHeader("content-type", "application/json");
      var1.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
      var1.putHeader("sec-ch-ua-platform", "\"macOS\"");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.bestbuy.com/cart");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   static {
      BASIC_ISO_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX").withZone(ZoneOffset.UTC);
   }

   public HttpRequest checkoutPage() {
      HttpRequest var1 = this.client.getAbs("https://www.bestbuy.com/checkout/r/fast-track").as(BodyCodec.buffer());
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("user-agent", this.userAgent);
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"93\", \" Not;A Brand\";v=\"99\", \"Chromium\";v=\"93\"");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("sec-ch-ua-platform", "\"macOS\"");
      var1.putHeader("referer", "https://www.bestbuy.com/identity/signin?token=tid%3Ad800c595-102b-11ec-98b5-005056aea131");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public JsonObject storeIdForm() {
      JsonObject var1 = new JsonObject();
      var1.put("zipCode", this.task.getProfile().getZip());
      var1.put("showOnShelf", true);
      var1.put("lookupInStoreQuantity", false);
      var1.put("xboxAllAccess", false);
      var1.put("consolidated", false);
      var1.put("showOnlyOnShelf", false);
      var1.put("showInStore", false);
      var1.put("onlyBestBuyLocations", true);
      JsonObject var2 = new JsonObject();
      var2.put("sku", this.SKU);
      var2.put("condition", (Object)null);
      var2.put("quantity", 1);
      var2.put("selectedServices", new JsonArray());
      var2.put("requiredAccessories", new JsonArray());
      var2.put("isTradeIn", false);
      var2.put("isLeased", false);
      var1.put("items", (new JsonArray()).add(var2));
      return var1;
   }

   public HttpRequest pickVerification() {
      HttpRequest var1 = this.client.postAbs("https://www.bestbuy.com/identity/account/recovery/code").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("sec-ch-ua", "\" Not;A Brand\";v=\"99\", \"Google Chrome\";v=\"97\", \"Chromium\";v=\"97\"");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", this.userAgent);
      var1.putHeader("content-type", "application/json");
      var1.putHeader("accept", "application/json");
      var1.putHeader("sec-ch-ua-platform", "\"macOS\"");
      var1.putHeader("origin", "https://www.bestbuy.com");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.bestbuy.com/identity/signin/recoveryOptions?token=" + this.login.initData.getString("token"));
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public HttpRequest placeOrder() {
      HttpRequest var1 = this.client.postAbs("https://www.bestbuy.com/checkout/orders/" + this.id + "/").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"93\", \" Not;A Brand\";v=\"99\", \"Chromium\";v=\"93\"");
      var1.putHeader("accept", "application/com.bestbuy.order+json");
      var1.putHeader("content-type", "application/json");
      var1.putHeader("x-user-interface", "DotCom-Optimized");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", this.userAgent);
      var1.putHeader("sec-ch-ua-platform", "\"macOS\"");
      var1.putHeader("origin", "https://www.bestbuy.com");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.bestbuy.com/checkout/r/payment");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public HttpRequest atc(String var1, String var2) {
      HttpRequest var3 = this.client.postAbs("https://www.bestbuy.com/cart/api/v1/addToCart").as(BodyCodec.buffer());
      var3.putHeader("content-length", "DEFAULT_VALUE");
      var3.putHeader("sec-ch-ua", "\"Chromium\";v=\"92\", \" Not A;Brand\";v=\"99\", \"Google Chrome\";v=\"92\"");
      var3.putHeader("accept", "application/json");
      var3.putHeader("a2ctransactioncode", var1);
      var3.putHeader("a2ctransactionreferenceid", var2);
      var3.putHeader("sec-ch-ua-mobile", "?0");
      var3.putHeader("user-agent", this.userAgent);
      var3.putHeader("content-type", "application/json; charset=UTF-8");
      var3.putHeader("origin", "https://www.bestbuy.com");
      var3.putHeader("sec-fetch-site", "same-origin");
      var3.putHeader("sec-fetch-mode", "cors");
      var3.putHeader("sec-fetch-dest", "empty");
      var3.putHeader("referer", this.task.getKeywords()[0]);
      var3.putHeader("accept-encoding", "gzip, deflate, br");
      var3.putHeader("accept-language", "en-US,en;q=0.9");
      return var3;
   }

   public HttpRequest submitContact() {
      HttpRequest var1 = this.client.patchAbs(this.isInstore ? "https://www.bestbuy.com/checkout/orders/" + this.id + "/items" : "https://www.bestbuy.com/checkout/orders/" + this.id).as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"93\", \" Not;A Brand\";v=\"99\", \"Chromium\";v=\"93\"");
      var1.putHeader("accept", "application/com.bestbuy.order+json");
      var1.putHeader("content-type", "application/json");
      var1.putHeader("x-user-interface", "DotCom-Optimized");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", this.userAgent);
      var1.putHeader("sec-ch-ua-platform", "\"macOS\"");
      var1.putHeader("origin", "https://www.bestbuy.com");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.bestbuy.com/checkout/r/fulfillment");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public HttpRequest accountGen() {
      HttpRequest var1 = this.client.postAbs("https://www.bestbuy.com/identity/createAccount").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"96\", \"Google Chrome\";v=\"96\"");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", this.userAgent);
      var1.putHeader("content-type", "application/json");
      var1.putHeader("accept", "application/json");
      var1.putHeader("sec-ch-ua-platform", "\"macOS\"");
      var1.putHeader("origin", "https://www.bestbuy.com");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.bestbuy.com/identity/newAccount?token=" + this.login.initData.getString("token"));
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public JsonObject deliveryJson() {
      JsonObject var1 = new JsonObject();
      var1.put("selected", "SHIPPING");
      return var1;
   }

   public HttpRequest emailGrid() {
      HttpRequest var1 = this.client.getAbs("https://www.bestbuy.com/api/csiservice/v2/key/cia-email").as(BodyCodec.buffer());
      var1.putHeader("sec-ch-ua", "\"Chromium\";v=\"94\", \"Google Chrome\";v=\"94\", \";Not A Brand\";v=\"99\"");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", this.userAgent);
      var1.putHeader("sec-ch-ua-platform", "\"macOS\"");
      var1.putHeader("content-type", "application/json");
      var1.putHeader("accept", "*/*");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.bestbuy.com/identity/signin?token=tid%3A05a6a146-287a-11ec-93ba-0ac72817b4c3");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public JsonObject accountLoginForm(Account var1, JsonObject var2, JsonObject var3) {
      String var10000 = var2.getString("publicKey");
      String var10001 = BASIC_ISO_DATE.format(Instant.now());
      String var4 = Encryption.encryptGeneral(var10000, "{\"mouseMoved\":true,\"keyboardUsed\":false,\"fieldReceivedInput\":true,\"fieldReceivedFocus\":true,\"timestamp\":\"" + var10001 + "\",\"email\":\"" + var1.getUser() + "\"}");
      JsonObject var5 = new JsonObject();
      var5.put("token", this.login.initData.getString("token"));
      String var10002 = var2.getString("keyId");
      var5.put("activity", "1:" + var10002 + ":" + var4);
      var5.put("loginMethod", "UID_PASSWORD");
      var5.put("flowOptions", this.login.initData.getString("flowOptions"));
      var5.put("alpha", this.login.getAlpha());
      var5.put("Salmon", this.login.initData.getString("Salmon"));
      var10002 = var3.getString("keyId");
      var5.put("encryptedEmail", "1:" + var10002 + ":" + Encryption.encryptGeneral(var3.getString("publicKey"), var1.getUser()));
      var5.put(this.login.getCode(), var1.getPass());
      var10002 = var2.getString("keyId");
      var5.put("info", "1:" + var10002 + ":" + Encryption.encryptGeneral(var2.getString("publicKey"), "{\"userAgent\":\"" + this.userAgent + "\"}"));
      var5.put(this.login.initData.getString("emailFieldName"), var1.getUser());
      return var5;
   }

   public JsonObject vaultJson() {
      Profile var1 = this.task.getProfile();
      JsonObject var2 = new JsonObject();
      var2.put("billingAddress", (new JsonObject()).put("country", "US").put("useAddressAsBilling", true).put("middleInitial", "").put("lastName", var1.getLastName()).put("isWishListAddress", false).put("city", var1.getCity()).put("state", var1.getState()).put("firstName", var1.getFirstName()).put("addressLine1", var1.getAddress1()).put("addressLine2", var1.getAddress2()).put("dayPhone", var1.getPhone()).put("postalCode", var1.getZip()).put("standardized", true).put("userOverridden", true));
      var2.put("creditCard", (new JsonObject()).put("hasCID", false).put("invalidCard", false).put("isCustomerCard", false).put("isNewCard", true).put("isVisaCheckout", false).put("govPurchaseCard", false).put("number", this.encryptedCard).put("binNumber", var1.getCardNumber().substring(0, 6)).put("isPWPRegistered", false).put("expMonth", var1.getExpiryMonth()).put("expYear", var1.getExpiryYear()).put("cvv", var1.getCvv()).put("orderId", this.orderId).put("saveToProfile", false).put("type", var1.getCardType().name()).put("international", false).put("virtualCard", false));
      return var2;
   }

   public HttpRequest verificationCode() {
      HttpRequest var1 = this.client.postAbs("https://www.bestbuy.com/identity/unlock").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"96\", \"Google Chrome\";v=\"96\"");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", this.userAgent);
      var1.putHeader("content-type", "application/json");
      var1.putHeader("accept", "application/json");
      var1.putHeader("sec-ch-ua-platform", "\"macOS\"");
      var1.putHeader("origin", "https://www.bestbuy.com");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.bestbuy.com/identity/signin/verificationCode?token=" + this.login.initData.getString("token"));
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public HttpRequest submitEmail() {
      HttpRequest var1 = this.client.patchAbs("https://www.bestbuy.com/checkout/orders/" + this.id).as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"93\", \" Not;A Brand\";v=\"99\", \"Chromium\";v=\"93\"");
      var1.putHeader("accept", "application/com.bestbuy.order+json");
      var1.putHeader("content-type", "application/json");
      var1.putHeader("x-user-interface", "DotCom-Optimized");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", this.userAgent);
      var1.putHeader("sec-ch-ua-platform", "\"macOS\"");
      var1.putHeader("origin", "https://www.bestbuy.com");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.bestbuy.com/checkout/r/fulfillment");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public JsonObject verificationJson(String var1) {
      JsonObject var2 = new JsonObject();
      var2.put("token", this.login.initData.getString("token"));
      var2.put("isResetFlow", false);
      var2.put("challengeType", this.login.challengeType);
      var2.put("flowOptions", this.login.flowOptions);
      var2.put(this.login.initData.getString("emailFieldName"), this.task.getProfile().getEmail());
      var2.put(this.login.initData.getString("verificationCodeFieldName"), var1);
      return var2;
   }

   public HttpRequest initCheckout() {
      HttpRequest var1 = this.client.postAbs("https://www.bestbuy.com/cart/checkout").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"93\", \" Not;A Brand\";v=\"99\", \"Chromium\";v=\"93\"");
      var1.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
      var1.putHeader("content-type", "application/json");
      var1.putHeader("x-order-id", "b160dbe0-fc6a-11eb-9e46-a593e4b8bf6a");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", this.userAgent);
      var1.putHeader("sec-ch-ua-platform", "\"macOS\"");
      var1.putHeader("origin", "https://www.bestbuy.com");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.bestbuy.com/cart");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public HttpRequest deleteItem(String var1) {
      HttpRequest var2 = this.client.deleteAbs("https://www.bestbuy.com/cart/item/" + var1).as(BodyCodec.buffer());
      var2.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"96\", \"Google Chrome\";v=\"96\"");
      var2.putHeader("dnt", "1");
      var2.putHeader("x-order-id", this.orderId);
      var2.putHeader("sec-ch-ua-mobile", "?0");
      var2.putHeader("user-agent", this.userAgent);
      var2.putHeader("content-type", "application/json");
      var2.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
      var2.putHeader("sec-ch-ua-platform", "\"macOS\"");
      var2.putHeader("origin", "https://www.bestbuy.com");
      var2.putHeader("sec-fetch-site", "same-origin");
      var2.putHeader("sec-fetch-mode", "cors");
      var2.putHeader("sec-fetch-dest", "empty");
      var2.putHeader("referer", "https://www.bestbuy.com/cart");
      var2.putHeader("accept-encoding", "gzip, deflate, br");
      var2.putHeader("accept-language", "en-US,en;q=0.9");
      return var2;
   }

   public JsonObject fetch3dJson() {
      JsonObject var1 = new JsonObject();
      var1.put("binNumber", this.task.getProfile().getCardNumber().substring(0, 6));
      var1.put("orderId", this.orderId);
      var1.put("paymentId", this.paymentId);
      var1.put("browserInfo", (new JsonObject()).put("javaEnabled", false).put("language", "en-US").put("userAgent", this.userAgent).put("height", "1097").put("width", "1097").put("timeZone", "240").put("colorDepth", "30"));
      return var1;
   }

   public HttpRequest productPage() {
      HttpRequest var1 = this.client.getAbs(this.task.getKeywords()[0]).as(BodyCodec.buffer());
      var1.putHeader("sec-ch-ua", "\"Chromium\";v=\"92\", \" Not A;Brand\";v=\"99\", \"Google Chrome\";v=\"92\"");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("user-agent", this.userAgent);
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("sec-fetch-site", "none");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public HttpRequest getStoreId() {
      HttpRequest var1 = this.client.postAbs("https://www.bestbuy.com/productfulfillment/com/api/2.0/storeAvailability").as(BodyCodec.buffer());
      var1.putHeader("Content-Length", "DEFAULT_VALUE");
      var1.putHeader("cache-control", "no-cache");
      var1.putHeader("sec-ch-ua", "\" Not;A Brand\";v=\"99\", \"Google Chrome\";v=\"91\", \"Chromium\";v=\"91\"");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", this.userAgent);
      var1.putHeader("accept", "*/*");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.bestbuy.com/");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      var1.putHeader("Cookie", "DEFAULT_VALUE");
      var1.putHeader("Content-Type", "application/json");
      return var1;
   }

   public JsonObject pickVerificationJson() {
      JsonObject var1 = new JsonObject();
      var1.put("token", this.login.initData.getString("token"));
      var1.put("recoveryOptionType", "email");
      var1.put("email", this.task.getProfile().getEmail());
      var1.put("smsDigits", "");
      var1.put("isResetFlow", false);
      var1.put("challengeType", this.login.challengeType);
      var1.put("flowOptions", this.login.flowOptions);
      return var1;
   }

   public HttpRequest refreshPayment() {
      HttpRequest var1 = this.client.postAbs("https://www.bestbuy.com/checkout/orders/" + this.id + "/paymentMethods/refreshPayment").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"93\", \" Not;A Brand\";v=\"99\", \"Chromium\";v=\"93\"");
      var1.putHeader("x-user-interface", "DotCom-Optimized");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", this.userAgent);
      var1.putHeader("content-type", "application/json");
      var1.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
      var1.putHeader("x-native-checkout-version", "__VERSION__");
      var1.putHeader("sec-ch-ua-platform", "\"macOS\"");
      var1.putHeader("origin", "https://www.bestbuy.com");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.bestbuy.com/checkout/r/payment");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public JsonObject atcForm(String var1) {
      JsonObject var2 = new JsonObject();
      if (this.isInstore) {
         var2.put("items", (new JsonArray()).add((new JsonObject()).put("skuId", var1).put("storeId", this.store).put("quantity", 1)));
      } else {
         var2.put("items", (new JsonArray()).add((new JsonObject()).put("skuId", var1)));
      }

      return var2;
   }

   public JsonObject accountGenForm() {
      JsonObject var1 = new JsonObject();
      var1.put("firstName", this.task.getProfile().getFirstName());
      var1.put("lastName", this.task.getProfile().getLastName());
      var1.put("phone", this.task.getProfile().getPhone());
      var1.put("token", this.login.initData.getString("token"));
      var1.put("memberId", "");
      var1.put("isRecoveryPhone", false);
      var1.put("addressLine1", (Object)null);
      var1.put("addressLine2", (Object)null);
      var1.put("city", (Object)null);
      var1.put("state", (Object)null);
      var1.put("zip", (Object)null);
      var1.put("businessName", (Object)null);
      var1.put("businessPhone", (Object)null);
      var1.put("alpha", this.login.getAlpha());
      var1.put("Roe", this.login.initData.getString("Roe"));
      String var10001 = this.login.initData.getString("emailFieldName");
      String var10002 = Utils.generateStrongString();
      var1.put(var10001, var10002 + "@" + this.task.getProfile().getEmail().split("@")[1]);
      String var2 = Utils.generateStrongString();
      var1.put(this.login.initData.getString("hash"), var2);
      var1.put(this.login.initData.getString("reenterHash"), var2);
      return var1;
   }

   public void setSensorUrl(String var1) {
      this.sensorUrl = var1;
   }

   public HttpRequest login() {
      HttpRequest var1 = this.client.postAbs("https://www.bestbuy.com/identity/authenticate").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"96\", \"Google Chrome\";v=\"96\"");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", this.userAgent);
      var1.putHeader("content-type", "application/json");
      var1.putHeader("accept", "application/json");
      var1.putHeader("sec-ch-ua-platform", "\"macOS\"");
      var1.putHeader("origin", "https://www.bestbuy.com");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.bestbuy.com/identity/signin?token=" + this.login.initData.getString("token"));
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public HttpRequest ciaUserActivity() {
      HttpRequest var1 = this.client.getAbs("https://www.bestbuy.com/api/csiservice/v2/key/cia-user-activity").as(BodyCodec.buffer());
      var1.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"96\", \"Google Chrome\";v=\"96\"");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", this.userAgent);
      var1.putHeader("sec-ch-ua-platform", "\"macOS\"");
      var1.putHeader("content-type", "application/json");
      var1.putHeader("accept", "*/*");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.bestbuy.com/identity/signin?token=" + this.login.initData.getString("token"));
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public HttpRequest vaultCC() {
      HttpRequest var1 = this.client.putAbs("https://www.bestbuy.com/payment/api/v1/payment/" + this.paymentId + "/creditCard").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"93\", \" Not;A Brand\";v=\"99\", \"Chromium\";v=\"93\"");
      var1.putHeader("x-context-id", this.orderId);
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", this.userAgent);
      var1.putHeader("content-type", "application/json");
      var1.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
      var1.putHeader("x-client", "CHECKOUT");
      var1.putHeader("sec-ch-ua-platform", "\"macOS\"");
      var1.putHeader("origin", "https://www.bestbuy.com");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.bestbuy.com/checkout/r/payment");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public HttpRequest loginRedirectFetch() {
      HttpRequest var1 = this.client.getAbs("https://www.bestbuy.com/identity/global/signin").as(BodyCodec.buffer());
      var1.putHeader("sec-ch-ua", "\"Chromium\";v=\"94\", \"Google Chrome\";v=\"94\", \";Not A Brand\";v=\"99\"");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("sec-ch-ua-platform", "\"macOS\"");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Safari/537.36");
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("referer", "https://www.bestbuy.com/");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      var1.putHeader("cookie", "DEFAULT_VALUE");
      return var1;
   }

   public HttpRequest loginPage(String var1) {
      HttpRequest var2 = this.client.getAbs(var1).as(BodyCodec.buffer());
      var2.putHeader("upgrade-insecure-requests", "1");
      var2.putHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Safari/537.36");
      var2.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var2.putHeader("sec-fetch-site", "same-origin");
      var2.putHeader("sec-fetch-mode", "navigate");
      var2.putHeader("sec-fetch-user", "?1");
      var2.putHeader("sec-fetch-dest", "document");
      var2.putHeader("sec-ch-ua", "\"Chromium\";v=\"94\", \"Google Chrome\";v=\"94\", \";Not A Brand\";v=\"99\"");
      var2.putHeader("sec-ch-ua-mobile", "?0");
      var2.putHeader("sec-ch-ua-platform", "\"macOS\"");
      var2.putHeader("referer", "https://www.bestbuy.com/");
      var2.putHeader("accept-encoding", "gzip, deflate, br");
      var2.putHeader("accept-language", "en-US,en;q=0.9");
      var2.putHeader("cookie", "DEFAULT_VALUE");
      return var2;
   }

   public HttpRequest signupRedirectFetch() {
      HttpRequest var1 = this.client.getAbs("https://www.bestbuy.com/identity/global/createAccount").as(BodyCodec.buffer());
      var1.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"96\", \"Google Chrome\";v=\"96\"");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("sec-ch-ua-platform", "\"macOS\"");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("user-agent", this.userAgent);
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("referer", "https://www.bestbuy.com/");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public JsonObject emailJson() {
      Profile var1 = this.task.getProfile();
      JsonObject var2 = new JsonObject();
      var2.put("phoneNumber", var1.getPhone());
      var2.put("smsNotifyNumber", "");
      var2.put("smsOptIn", false);
      var2.put("emailAddress", var1.getEmail());
      return var2;
   }

   public JsonObject placeOrderForm() {
      JsonObject var1 = new JsonObject();
      var1.put("browserInfo", (new JsonObject()).put("javaEnabled", false).put("language", "en-US").put("userAgent", this.userAgent).put("height", "1097").put("width", "1097").put("timeZone", "240").put("colorDepth", "30"));
      return var1;
   }

   public Object contactJson() {
      Profile var1 = this.task.getProfile();
      JsonObject var2;
      if (this.isInstore) {
         var2 = new JsonObject();
         var2.put("id", this.cartId);
         var2.put("type", "DEFAULT");
         var2.put("storeFulfillmentType", "InStore");
         var2.put("selectedFulfillment", (new JsonObject()).put("inStorePickup", (new JsonObject()).put("pickupStoreId", this.store)));
         return (new JsonArray()).add(var2);
      } else {
         var2 = new JsonObject();
         var2.put("id", this.cartId);
         var2.put("type", "DEFAULT");
         var2.put("selectedFulfillment", (new JsonObject()).put("shipping", (new JsonObject()).put("address", (new JsonObject()).put("country", "US").put("saveToProfile", false).put("street2", var1.getAddress2()).put("useAddressAsBilling", true).put("middleInitial", "").put("lastName", var1.getLastName()).put("street", var1.getAddress1()).put("city", var1.getCity()).put("override", true).put("zipcode", var1.getZip()).put("state", var1.getState()).put("firstName", var1.getFirstName()))));
         JsonArray var3 = (new JsonArray()).add(var2);
         return (new JsonObject()).put("items", var3);
      }
   }

   public HttpRequest cartDetails() {
      HttpRequest var1 = this.client.getAbs("https://www.bestbuy.com/cart/json?isDeviceApplePayEligible=false").as(BodyCodec.buffer());
      var1.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"93\", \" Not;A Brand\";v=\"99\", \"Chromium\";v=\"93\"");
      var1.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
      var1.putHeader("content-type", "application/json");
      var1.putHeader("x-order-id", "");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", this.userAgent);
      var1.putHeader("sec-ch-ua-platform", "\"macOS\"");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.bestbuy.com/cart");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public HttpRequest fetchPublicKey() {
      HttpRequest var1 = this.client.getAbs("https://www.bestbuy.com/api/csiservice/v2/key/tas").as(BodyCodec.buffer());
      var1.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"93\", \" Not;A Brand\";v=\"99\", \"Chromium\";v=\"93\"");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", this.userAgent);
      var1.putHeader("sec-ch-ua-platform", "\"macOS\"");
      var1.putHeader("content-type", "application/json");
      var1.putHeader("accept", "*/*");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.bestbuy.com/checkout/r/payment");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public HttpRequest sendSensor() {
      HttpRequest var1 = this.client.postAbs(this.sensorUrl).as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("sec-ch-ua", "\"Chromium\";v=\"92\", \" Not A;Brand\";v=\"99\", \"Google Chrome\";v=\"92\"");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", this.userAgent);
      var1.putHeader("content-type", "text/plain;charset=UTF-8");
      var1.putHeader("accept", "*/*");
      var1.putHeader("origin", "https://www.bestbuy.com");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", this.task.getKeywords()[0]);
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public HttpRequest fetch3dsParams() {
      HttpRequest var1 = this.client.postAbs("https://www.bestbuy.com/payment/api/v1/payment/" + this.paymentId + "/threeDSecure/preLookup").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"93\", \" Not;A Brand\";v=\"99\", \"Chromium\";v=\"93\"");
      var1.putHeader("x-client", "CHECKOUT");
      var1.putHeader("content-type", "application/json");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", this.userAgent);
      var1.putHeader("sec-ch-ua-platform", "\"macOS\"");
      var1.putHeader("accept", "*/*");
      var1.putHeader("origin", "https://www.bestbuy.com");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.bestbuy.com/checkout/r/payment");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }
}
