package io.trickle.task.sites.walmart.usa;

import io.trickle.account.Account;
import io.trickle.profile.Profile;
import io.trickle.task.Task;
import io.trickle.task.antibot.impl.px.PerimeterX;
import io.trickle.task.sites.walmart.util.PaymentToken;
import io.trickle.util.Utils;
import io.trickle.util.request.Headers$Pseudo;
import io.trickle.webclient.ClientType;
import io.trickle.webclient.CookieJar;
import io.trickle.webclient.RealClient;
import io.trickle.webclient.RealClientFactory;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.codec.BodyCodec;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class WalmartAPI extends API {
   public static int EXCEPTION_RETRY_DELAY = 12000;
   public boolean ios;
   public Task task;
   public String crossSite;
   public static DateTimeFormatter GMT_CHROME_RFC1123;
   public boolean loggedIn;
   public String dateOfPrevReq;
   public static String BYPASS_PREFIX = "";
   public PerimeterX pxAPI = null;
   public static String DID = UUID.randomUUID().toString();
   public String searchQuery;
   public static String PX_TOKEN = "3";

   public HttpRequest terraFirma(String var1, boolean var2) {
      String var3 = "https://www.walmart.com/terra-firma/graphql?v=2&options=timing%2Cnonnull%2Cerrors%2Ccontext&id=FullProductHolidaysRoute-" + (this.ios ? "ios" : "android");
      if (var2) {
         var3 = "https://www.walmart.com/terra-firma/graphql?options=timing,nonnull,context&v=2&id=FullProductRoute-" + (this.ios ? "ios" : "android");
      }

      HttpRequest var4 = this.client.postAbs(var3).as(BodyCodec.buffer());
      if (this.ios) {
         var4.putHeaders(Headers$Pseudo.MSPA.get());
         var4.putHeader("content-type", "application/json");
         var4.putHeader("wm_ul_plus", "0");
         var4.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
         var4.putHeader("wm_consumer.id", UUID.randomUUID().toString());
         var4.putHeader("accept", "*/*");
         var4.putHeader("mobile-app-version", "21.18.3");
         var4.putHeader("wm_site_mode", "0");
         var4.putHeader("accept-language", "en-us");
         var4.putHeader("accept-encoding", "gzip, deflate, br");
         var4.putHeader("mobile-platform", "ios");
         var4.putHeader("content-length", "DEFAULT_VALUE");
         var4.putHeader("itemid", var1);
         var4.putHeader("did", DID.replace("-", "").concat("00000000"));
         var4.putHeader("user-agent", "Walmart/2107091553 CFNetwork/1128.0.1 Darwin/19.6.0");
         var4.putHeader("cookie", "DEFAULT_VALUE");
      } else {
         var4.putHeaders(Headers$Pseudo.MPAS.get());
         var4.putHeader("wm_ul_plus", "0");
         var4.putHeader("wm_consumer.id", UUID.randomUUID().toString());
         var4.putHeader("wm_site_mode", "0");
         var4.putHeader("itemid", var1);
         var4.putHeader("user-agent", WalmartConstants.ANDROID_OLD_UA);
         var4.putHeader("did", DID);
         var4.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
         var4.putHeader("content-type", "application/json; charset=utf-8");
         var4.putHeader("content-length", "DEFAULT_VALUE");
         var4.putHeader("accept-encoding", "gzip");
         var4.putHeader("cookie", "DEFAULT_VALUE");
      }

      return var4;
   }

   public JsonObject loginForm() {
      try {
         Account var1 = rotateAccount();
         if (var1 == null) {
            return null;
         } else {
            JsonObject var2 = new JsonObject();
            var2.put("email", var1.getUser());
            var2.put("password", var1.getPass());
            return var2;
         }
      } catch (Throwable var3) {
         return null;
      }
   }

   public CompletableFuture handleBadResponse(int var1, HttpResponse var2) {
      switch (var1) {
         case 412:
            try {
               if (this.task.getMode().contains("skip")) {
                  CompletableFuture var10000 = this.generatePX(false);
                  if (!var10000.isDone()) {
                     CompletableFuture var6 = var10000;
                     return var6.exceptionally(Function.identity()).thenCompose(WalmartAPI::async$handleBadResponse);
                  }

                  var10000.join();
                  return CompletableFuture.completedFuture(false);
               }

               JsonObject var3 = var2.bodyAsJsonObject();
               String var4 = var3.getString("uuid");
               String var5 = this.pxAPI.getVid();
               return this.pxAPI.solveCaptcha(var5, var4, "https://www.perimeterx.com/");
            } catch (Throwable var7) {
               return this.pxAPI.solveCaptcha(this.pxAPI.getVid(), (String)null, "https://www.perimeterx.com/");
            }
         case 444:
            if (super.rotateProxy()) {
               this.pxAPI.restartClient(super.client);
            }
         default:
            return CompletableFuture.completedFuture(true);
      }
   }

   public HttpRequest getCartV3(String var1) {
      return this.getCartMobile("https://www.walmart.com/api/v3/cart/" + var1);
   }

   public JsonObject getAtcForm(String var1, int var2) {
      return (new JsonObject()).put("offerId", var1).put("quantity", var2);
   }

   public JsonObject getProcessingForm() {
      return new JsonObject();
   }

   public boolean isLoggedIn() {
      return this.loggedIn;
   }

   public JsonObject getPaymentForm(PaymentToken var1) {
      JsonObject var2 = (new JsonObject()).put("payments", new JsonArray());
      JsonObject var3 = new JsonObject();
      Profile var4 = this.task.getProfile();
      var3.put("addressLineOne", var4.getAddress1());
      var3.put("addressLineTwo", var4.getAddress2());
      var3.put("cardType", var4.getPaymentMethod().get());
      var3.put("city", var4.getCity());
      var3.put("encryptedCvv", var1.getEncryptedCvv());
      var3.put("encryptedPan", var1.getEncryptedPan());
      var3.put("expiryMonth", var4.getExpiryMonth());
      if (var4.getExpiryYear().length() > 2) {
         var3.put("expiryYear", var4.getExpiryYear());
      } else {
         var3.put("expiryYear", "20" + var4.getExpiryYear());
      }

      var3.put("firstName", var4.getFirstName());
      var3.put("integrityCheck", var1.getIntegrityCheck());
      var3.put("keyId", var1.getKeyId());
      var3.put("lastName", var4.getLastName());
      var3.put("paymentType", "CREDITCARD");
      var3.put("phase", var1.getPhase());
      var3.put("phone", var4.getPhone());
      var3.put("piHash", var1.getPiHash());
      var3.put("postalCode", var4.getZip());
      var3.put("state", var4.getState().toUpperCase());
      var3.put("email", var4.getEmail());
      var2.getJsonArray("payments").add(var3);
      var2.put("cvvInSession", true);
      return var2;
   }

   public void setAPI(PerimeterX var1) {
      this.pxAPI = var1;
   }

   public HttpRequest getPCID(PaymentToken var1) {
      HttpRequest var2 = this.client.postAbs("https://www.walmart.com/api/checkout/v3/contract").as(BodyCodec.buffer());
      if (this.ios) {
         var2.putHeaders(Headers$Pseudo.MSPA.get());
         var2.putHeader("content-type", "application/json");
         var2.putHeader("x-px-ab", "2");
         var2.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
         var2.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
         var2.putHeader("wm_vertical_id", "0");
         var2.putHeader("mobile-app-version", "21.18.3");
         var2.putHeader("vid", var1.getVid().toUpperCase());
         var2.putHeader("wm_cvv_in_session", "true");
         var2.putHeader("accept-language", "en-us");
         var2.putHeader("inkiru_precedence", "false");
         var2.putHeader("sid", var1.getSid().toUpperCase());
         var2.putHeader("mobile-platform", "ios");
         var2.putHeader("did", DID.replace("-", "").concat("00000000"));
         var2.putHeader("user-agent", "Walmart/2107091553 Walmart WMTAPP v21.18.3");
         var2.putHeader("content-length", "DEFAULT_VALUE");
         var2.putHeader("accept-encoding", "gzip, deflate, br");
         var2.putHeader("cookie", "DEFAULT_VALUE");
      } else {
         var2.putHeaders(Headers$Pseudo.MPAS.get());
         var2.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
         var2.putHeader("wm_vertical_id", "0");
         var2.putHeader("inkiru_precedence", "false");
         var2.putHeader("wm_cvv_in_session", "true");
         var2.putHeader("x-px-ab", "2");
         var2.putHeader("sid", var1.getSid());
         var2.putHeader("vid", var1.getVid());
         var2.putHeader("user-agent", WalmartConstants.ANDROID_OLD_WEB_UA);
         var2.putHeader("did", DID);
         var2.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
         var2.putHeader("content-type", "application/json");
         var2.putHeader("content-length", "DEFAULT_VALUE");
         var2.putHeader("accept-encoding", "gzip");
         var2.putHeader("cookie", "DEFAULT_VALUE");
      }

      return var2;
   }

   public HttpRequest mobileAtcAffiliate() {
      String var10001 = this.task.getKeywords()[0];
      return this.atcAffiliate("https://affil.walmart.com/cart/addToCart?items=" + var10001 + "|" + this.task.getSize());
   }

   public HttpRequest submitBilling() {
      HttpRequest var1 = this.client.postAbs("https://www.walmart.com//api/checkout-customer/:CID/credit-card").as(BodyCodec.buffer());
      if (this.ios) {
         var1.putHeaders(Headers$Pseudo.MSPA.get());
         var1.putHeader("content-type", "application/json");
         var1.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
         var1.putHeader("accept", "application/json");
         var1.putHeader("mobile-app-version", "21.18.3");
         var1.putHeader("accept-language", "en-us");
         var1.putHeader("accept-encoding", "gzip, deflate, br");
         var1.putHeader("mobile-platform", "ios");
         var1.putHeader("content-length", "DEFAULT_VALUE");
         var1.putHeader("did", DID.replace("-", "").concat("00000000"));
         var1.putHeader("user-agent", "Walmart/2107091553 Walmart WMTAPP v21.18.3");
         var1.putHeader("cookie", "DEFAULT_VALUE");
         var1.putHeader("x-px-ab", "2");
      } else {
         var1.putHeaders(Headers$Pseudo.MPAS.get());
         var1.putHeader("Accept", "application/json");
         var1.putHeader("x-px-ab", "2");
         var1.putHeader("User-Agent", WalmartConstants.ANDROID_OLD_WEB_UA);
         var1.putHeader("did", DID);
         var1.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
         var1.putHeader("Content-Type", "application/json");
         var1.putHeader("Content-Length", "DEFAULT_VALUE");
         var1.putHeader("Accept-Encoding", "gzip");
         var1.putHeader("Cookie", "DEFAULT_VALUE");
      }

      return var1;
   }

   public HttpRequest addToCart(String var1) {
      HttpRequest var2 = super.client.postAbs(var1).as(BodyCodec.buffer());
      if (this.ios) {
         var2.putHeaders(Headers$Pseudo.MSPA.get());
         var2.putHeader("content-type", "application/json");
         var2.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
         var2.putHeader("accept", "*/*");
         var2.putHeader("mobile-app-version", "21.18.3");
         var2.putHeader("accept-language", "en-us");
         var2.putHeader("accept-encoding", "gzip, deflate, br");
         var2.putHeader("mobile-platform", "ios");
         var2.putHeader("content-length", "DEFAULT_VALUE");
         var2.putHeader("did", DID.replace("-", "").concat("00000000"));
         var2.putHeader("user-agent", "Walmart/2107091553 CFNetwork/1128.0.1 Darwin/19.6.0");
         var2.putHeader("cookie", "DEFAULT_VALUE");
      } else {
         var2.putHeaders(Headers$Pseudo.MPAS.get());
         var2.putHeader("user-agent", WalmartConstants.ANDROID_OLD_UA);
         var2.putHeader("did", DID);
         var2.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
         var2.putHeader("content-type", "application/json; charset=utf-8");
         var2.putHeader("content-length", "DEFAULT_VALUE");
         var2.putHeader("accept-encoding", "gzip");
         var2.putHeader("cookie", "DEFAULT_VALUE");
      }

      return var2;
   }

   public HttpRequest homepage() {
      HttpRequest var1 = this.client.getAbs("https://www.walmart.com/").as(BodyCodec.buffer());
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("user-agent", WalmartConstants.ANDROID_OLD_WEB_UA);
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("referer", "https://www.google.com/");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-us");
      return var1;
   }

   public PerimeterX getPxAPI() {
      return this.pxAPI;
   }

   public HttpRequest terraFirma(boolean var1) {
      return this.task.getMode().toLowerCase().contains("desktop") ? this.terraFirmaDesktop("152481472") : this.terraFirma("152481472", var1);
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

   public HttpRequest putCartAndroid(String var1) {
      HttpRequest var2 = this.client.putAbs("https://www.walmart.com/api/v3/cart/" + var1).timeout(TimeUnit.SECONDS.toMillis(10L)).as(BodyCodec.buffer());
      var2.putHeaders(Headers$Pseudo.MPAS.get());
      var2.putHeader("User-Agent", WalmartConstants.ANDROID_OLD_UA);
      var2.putHeader("did", DID);
      var2.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
      var2.putHeader("Content-Type", "application/json; charset=utf-8");
      var2.putHeader("Content-Length", "DEFAULT_VALUE");
      var2.putHeader("Accept-Encoding", "gzip");
      var2.putHeader("Cookie", "DEFAULT_VALUE");
      return var2;
   }

   public HttpRequest atcAffiliate() {
      return ThreadLocalRandom.current().nextBoolean() ? this.mobileAtcAffiliate() : this.desktopAtcAffiliate();
   }

   public Task getTask() {
      return this.task;
   }

   public void setLoggedIn(boolean var1) {
      this.loggedIn = var1;
   }

   public HttpRequest getCartMobile(String var1) {
      HttpRequest var2 = this.client.getAbs(var1).timeout(TimeUnit.SECONDS.toMillis(10L)).as(BodyCodec.buffer());
      if (this.ios) {
         var2.putHeaders(Headers$Pseudo.MSPA.get());
         var2.putHeader("accept", "*/*");
         var2.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
         var2.putHeader("did", DID.replace("-", "").concat("00000000"));
         var2.putHeader("mobile-app-version", "21.18.3");
         var2.putHeader("mobile-platform", "ios");
         var2.putHeader("cookie", "DEFAULT_VALUE");
         var2.putHeader("accept-language", "en-us");
         var2.putHeader("user-agent", "Walmart/2107091553 CFNetwork/1128.0.1 Darwin/19.6.0");
         var2.putHeader("accept-encoding", "gzip, deflate, br");
      } else {
         var2.putHeaders(Headers$Pseudo.MPAS.get());
         var2.putHeader("User-Agent", WalmartConstants.ANDROID_OLD_UA);
         var2.putHeader("did", DID);
         var2.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
         var2.putHeader("Accept-Encoding", "gzip");
         var2.putHeader("Cookie", "DEFAULT_VALUE");
      }

      return var2;
   }

   public JsonObject getShippingForm(JsonObject var1) {
      String var2 = var1.getJsonArray("items").getJsonObject(0).getString("id");
      JsonObject var3 = (new JsonObject()).put("changedFields", new JsonArray());
      Profile var4 = this.task.getProfile();
      var3.put("addressLineOne", var4.getAddress1());
      var3.put("addressLineTwo", var4.getAddress2());
      var3.put("addressType", "RESIDENTIAL");
      var3.put("city", var4.getCity());
      var3.put("firstName", var4.getFirstName());
      var3.put("lastName", var4.getLastName());
      var3.put("phone", var4.getPhone());
      var3.put("postalCode", var4.getZip());
      var3.put("countryCode", "USA");
      var3.put("marketingEmailPref", false);
      var3.put("preferenceId", var2);
      var3.put("state", var4.getState());
      var3.put("email", var4.getEmail());
      return var3;
   }

   public CompletableFuture generatePX(boolean var1) {
      try {
         if (var1) {
            this.pxAPI.reset();
         }

         return this.pxAPI.solve();
      } catch (Exception var3) {
         System.out.println("Error generating mobile session: " + var3.getMessage());
         return CompletableFuture.completedFuture(false);
      }
   }

   public HttpRequest login() {
      HttpRequest var1 = this.client.postAbs("https://www.walmart.com/account/electrode/api/identity/password").as(BodyCodec.buffer());
      var1.putHeaders(Headers$Pseudo.MPAS.get());
      var1.putHeader("x-wm-auth", "null");
      var1.putHeader("x-wm-cid", "null");
      var1.putHeader("x-wm-spid", "null");
      var1.putHeader("x-wm-xpa", "GNsCQ|PPWSy");
      var1.putHeader("user-agent", WalmartConstants.ANDROID_OLD_UA);
      var1.putHeader("did", DID);
      var1.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
      var1.putHeader("content-type", "text/plain;charset=UTF-8");
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("accept-encoding", "gzip");
      return var1;
   }

   public JsonObject accountCreateForm() {
      JsonObject var1 = new JsonObject();
      String var10002 = this.task.getProfile().getEmail().split("@")[0];
      var1.put("email", var10002 + "+" + ThreadLocalRandom.current().nextInt(999999) + "@" + this.task.getProfile().getEmail().split("@")[1]);
      var1.put("emailAccepted", false);
      var1.put("firstName", this.task.getProfile().getFirstName());
      var1.put("lastName", this.task.getProfile().getLastName());
      var1.put("password", Utils.generateStrongString());
      return var1;
   }

   public HttpRequest createAccount() {
      HttpRequest var1 = this.client.postAbs("https://www.walmart.com/account/electrode/api/identity/sign-up").as(BodyCodec.buffer());
      if (this.ios) {
         var1.putHeaders(Headers$Pseudo.MSPA.get());
         var1.putHeader("content-type", "application/json");
         var1.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
         var1.putHeader("mobile-app-version", "21.18.3");
         var1.putHeader("accept-language", "en-us");
         var1.putHeader("accept-encoding", "gzip, deflate, br");
         var1.putHeader("mobile-platform", "ios");
         var1.putHeader("content-length", "DEFAULT_VALUE");
         var1.putHeader("did", DID.replace("-", "").concat("00000000"));
         var1.putHeader("user-agent", "Walmart/2107091553 Walmart WMTAPP v21.18.3");
         var1.putHeader("cookie", "DEFAULT_VALUE");
         var1.putHeader("x-px-ab", "2");
      } else {
         var1.putHeaders(Headers$Pseudo.MPAS.get());
         var1.putHeader("x-wm-auth", "null");
         var1.putHeader("x-wm-cid", "null");
         var1.putHeader("x-wm-spid", "null");
         var1.putHeader("x-wm-xpa", "null");
         var1.putHeader("user-agent", WalmartConstants.ANDROID_OLD_UA);
         var1.putHeader("did", DID);
         var1.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
         var1.putHeader("content-type", "text/plain;charset=UTF-8");
         var1.putHeader("content-length", "DEFAULT_VALUE");
         var1.putHeader("accept-encoding", "gzip");
      }

      return var1;
   }

   public HttpRequest updateCheck() {
      HttpRequest var1 = this.client.getAbs("https://ota.walmart.com/v0.1/public/codepush/update_check?deployment_key=rULtKsvrPnyMXbxEHQnxoRPJJWnTOxlUbjxIOSAt&app_version=21.5.5&client_unique_id=99155d91e8fc6bf5").timeout(TimeUnit.SECONDS.toMillis(10L)).as(BodyCodec.none());
      if (this.ios) {
         var1.putHeaders(Headers$Pseudo.MSPA.get());
         var1.putHeader("content-type", "application/json");
         var1.putHeader("accept", "application/json");
         var1.putHeader("mobile-app-version", "21.18.3");
         var1.putHeader("accept-language", "en-us");
         var1.putHeader("x-codepush-plugin-name", "react-native-code-push");
         var1.putHeader("x-codepush-sdk-version", "^3.1.0");
         var1.putHeader("mobile-platform", "ios");
         var1.putHeader("accept-encoding", "gzip, deflate, br");
         var1.putHeader("did", DID.replace("-", "").concat("00000000"));
         var1.putHeader("user-agent", "Walmart/2107091553 CFNetwork/1128.0.1 Darwin/19.6.0");
         var1.putHeader("cookie", "DEFAULT_VALUE");
         var1.putHeader("x-codepush-plugin-version", "6.2.1");
      } else {
         var1.putHeaders(Headers$Pseudo.MPAS.get());
         var1.putHeader("User-Agent", WalmartConstants.ANDROID_OLD_UA);
         var1.putHeader("did", DID);
         var1.putHeader("Accept-Encoding", "gzip");
         var1.putHeader("Cookie", "DEFAULT_VALUE");
      }

      return var1;
   }

   public HttpRequest savedCart() {
      HttpRequest var1 = super.client.postAbs("https://www.walmart.com/api/v3/saved/:CRT/items").as(BodyCodec.buffer());
      if (this.ios) {
         var1.putHeaders(Headers$Pseudo.MSPA.get());
         var1.putHeader("content-type", "application/json");
         var1.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
         var1.putHeader("accept", "*/*");
         var1.putHeader("mobile-app-version", "21.18.3");
         var1.putHeader("accept-language", "en-us");
         var1.putHeader("accept-encoding", "gzip, deflate, br");
         var1.putHeader("mobile-platform", "ios");
         var1.putHeader("content-length", "DEFAULT_VALUE");
         var1.putHeader("did", DID.replace("-", "").concat("00000000"));
         var1.putHeader("credentials", "include");
         var1.putHeader("omitcsrfjwt", "true");
         var1.putHeader("user-agent", "Walmart/2107091553 CFNetwork/1128.0.1 Darwin/19.6.0");
         var1.putHeader("cookie", "DEFAULT_VALUE");
      } else {
         var1.putHeaders(Headers$Pseudo.MPAS.get());
         var1.putHeader("user-agent", WalmartConstants.ANDROID_OLD_UA);
         var1.putHeader("did", DID);
         var1.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
         var1.putHeader("content-type", "application/json; charset=utf-8");
         var1.putHeader("content-length", "DEFAULT_VALUE");
         var1.putHeader("credentials", "include");
         var1.putHeader("omitcsrfjwt", "true");
         var1.putHeader("accept-encoding", "gzip");
         var1.putHeader("cookie", "DEFAULT_VALUE");
      }

      return var1;
   }

   public HttpRequest submitPayment() {
      HttpRequest var1 = this.client.postAbs("https://www.walmart.com/api/checkout/v3/contract/:PCID/payment").as(BodyCodec.buffer());
      if (this.ios) {
         var1.putHeaders(Headers$Pseudo.MSPA.get());
         var1.putHeader("content-type", "application/json");
         var1.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
         var1.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
         var1.putHeader("wm_vertical_id", "0");
         var1.putHeader("mobile-app-version", "21.18.3");
         var1.putHeader("wm_cvv_in_session", "true");
         var1.putHeader("accept-language", "en-us");
         var1.putHeader("accept-encoding", "gzip, deflate, br");
         var1.putHeader("inkiru_precedence", "false");
         var1.putHeader("mobile-platform", "ios");
         var1.putHeader("content-length", "DEFAULT_VALUE");
         var1.putHeader("did", DID.replace("-", "").concat("00000000"));
         var1.putHeader("user-agent", "Walmart/2107091553 Walmart WMTAPP v21.18.3");
         var1.putHeader("cookie", "DEFAULT_VALUE");
         var1.putHeader("x-px-ab", "2");
      } else {
         var1.putHeaders(Headers$Pseudo.MPAS.get());
         var1.putHeader("Accept", "application/json, text/javascript, */*; q=0.01");
         var1.putHeader("wm_vertical_id", "0");
         var1.putHeader("inkiru_precedence", "false");
         var1.putHeader("wm_cvv_in_session", "true");
         var1.putHeader("x-px-ab", "2");
         var1.putHeader("User-Agent", WalmartConstants.ANDROID_OLD_WEB_UA);
         var1.putHeader("did", DID);
         var1.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
         var1.putHeader("Content-Type", "application/json");
         var1.putHeader("Content-Length", "DEFAULT_VALUE");
         var1.putHeader("Accept-Encoding", "gzip");
         var1.putHeader("Cookie", "DEFAULT_VALUE");
      }

      return var1;
   }

   public static CompletableFuture async$handleBadResponse(WalmartAPI var0, int var1, HttpResponse var2, CompletableFuture var3, int var4, Object var5) {
      CompletableFuture var10000;
      boolean var10001;
      label45:
      switch (var4) {
         case 0:
            switch (var1) {
               case 412:
                  try {
                     if (var0.task.getMode().contains("skip")) {
                        var10000 = var0.generatePX(false);
                        if (!var10000.isDone()) {
                           CompletableFuture var6 = var10000;
                           return var6.exceptionally(Function.identity()).thenCompose(WalmartAPI::async$handleBadResponse);
                        }
                        break label45;
                     }
                  } catch (Throwable var9) {
                     var10001 = false;
                     return var0.pxAPI.solveCaptcha(var0.pxAPI.getVid(), (String)null, "https://www.perimeterx.com/");
                  }

                  try {
                     JsonObject var10 = var2.bodyAsJsonObject();
                     String var11 = var10.getString("uuid");
                     String var12 = var0.pxAPI.getVid();
                     return var0.pxAPI.solveCaptcha(var12, var11, "https://www.perimeterx.com/");
                  } catch (Throwable var8) {
                     var10001 = false;
                     return var0.pxAPI.solveCaptcha(var0.pxAPI.getVid(), (String)null, "https://www.perimeterx.com/");
                  }
               case 444:
                  if (var0.rotateProxy()) {
                     var0.pxAPI.restartClient(var0.client);
                  }
               default:
                  return CompletableFuture.completedFuture(true);
            }
         case 1:
            var10000 = var3;
            break;
         default:
            throw new IllegalArgumentException();
      }

      try {
         var10000.join();
         return CompletableFuture.completedFuture(false);
      } catch (Throwable var7) {
         var10001 = false;
         return var0.pxAPI.solveCaptcha(var0.pxAPI.getVid(), (String)null, "https://www.perimeterx.com/");
      }
   }

   public HttpRequest terraFirmaDesktop(String var1) {
      HttpRequest var2 = this.client.getAbs("https://www.walmart.com/terra-firma/item/" + var1).as(BodyCodec.buffer());
      var2.putHeaders(Headers$Pseudo.MPAS.get());
      var2.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"89\", \"Chromium\";v=\"89\", \";Not A Brand\";v=\"99\"");
      var2.putHeader("sec-ch-ua-mobile", "?0");
      var2.putHeader("upgrade-insecure-requests", "1");
      var2.putHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.114 Safari/537.36");
      var2.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var2.putHeader("service-worker-navigation-preload", "true");
      var2.putHeader("sec-fetch-site", "none");
      var2.putHeader("sec-fetch-mode", "navigate");
      var2.putHeader("sec-fetch-user", "?1");
      var2.putHeader("sec-fetch-dest", "document");
      var2.putHeader("accept-encoding", "gzip, deflate, br");
      var2.putHeader("accept-language", "en-US,en;q=0.9");
      var2.putHeader("cookie", "DEFAULT_VALUE");
      return var2;
   }

   public HttpRequest desktopAtc() {
      double var1 = ThreadLocalRandom.current().nextDouble();
      String var3;
      if (var1 < Double.longBitsToDouble(4598175219545276416L)) {
         var3 = "https://www.walmart.com/api/v3/cart/guest/:CID/items";
      } else if (var1 < Double.longBitsToDouble(4602678819172646912L)) {
         var3 = "https://www.walmart.com/api/v3/cart/guest/:CRT/items";
      } else if (var1 < Double.longBitsToDouble(4604930618986332160L)) {
         var3 = "https://www.walmart.com/api/v3/cart/:CRT/items";
      } else {
         var3 = "https://www.walmart.com/api/v3/cart/:CID/items";
      }

      return this.addToCart(var3);
   }

   public HttpRequest processPayment(PaymentToken var1) {
      HttpRequest var2 = this.client.putAbs("https://www.walmart.com/api/checkout/v3/contract/:PCID/order").as(BodyCodec.buffer());
      if (this.ios) {
         var2.putHeader("cookie", "DEFAULT_VALUE");
         var2.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
         var2.putHeader("mobile-app-version", "21.18.3");
         var2.putHeader("inkiru_precedence", "false");
         var2.putHeader("mobile-platform", "ios");
         var2.putHeader("vid", var1.getVid().toUpperCase());
         var2.putHeader("did", DID.replace("-", "").concat("00000000"));
         var2.putHeader("user-agent", "Walmart/2107091553 Walmart WMTAPP v21.18.3");
         var2.putHeader("sid", var1.getSid().toUpperCase());
         var2.putHeader("content-length", "DEFAULT_VALUE");
         var2.putHeader("x-px-ab", "2");
         var2.putHeader("x-o-segment", "blue");
         var2.putHeader("accept-language", "en-us");
         var2.putHeader("wm_cvv_in_session", "true");
         var2.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
         var2.putHeader("content-type", "application/json");
         var2.putHeader("accept-encoding", "gzip, deflate, br");
         var2.putHeader("wm_vertical_id", "0");
      } else {
         var2.putHeaders(Headers$Pseudo.MPAS.get());
         var2.putHeader("Accept", "application/json, text/javascript, */*; q=0.01");
         var2.putHeader("wm_vertical_id", "0");
         var2.putHeader("inkiru_precedence", "false");
         var2.putHeader("wm_cvv_in_session", "true");
         var2.putHeader("x-o-segment", "blue");
         var2.putHeader("x-px-ab", "2");
         var2.putHeader("sid", var1.getSid());
         var2.putHeader("vid", var1.getVid());
         var2.putHeader("User-Agent", WalmartConstants.ANDROID_OLD_WEB_UA);
         var2.putHeader("did", DID);
         var2.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
         var2.putHeader("Content-Type", "application/json");
         var2.putHeader("Content-Length", "DEFAULT_VALUE");
         var2.putHeader("Accept-Encoding", "gzip");
         var2.putHeader("Cookie", "DEFAULT_VALUE");
      }

      return var2;
   }

   public JsonObject accountLoginForm(Account var1) {
      JsonObject var2 = new JsonObject();
      var2.put("email", var1.getUser());
      var2.put("password", var1.getPass());
      return var2;
   }

   public HttpRequest selectShipping() {
      HttpRequest var1 = this.client.postAbs("https://www.walmart.com/api/checkout/v3/contract/:PCID/fulfillment").as(BodyCodec.buffer());
      if (this.ios) {
         var1.putHeaders(Headers$Pseudo.MSPA.get());
         var1.putHeader("content-type", "application/json");
         var1.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
         var1.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
         var1.putHeader("wm_vertical_id", "0");
         var1.putHeader("mobile-app-version", "21.18.3");
         var1.putHeader("wm_cvv_in_session", "true");
         var1.putHeader("accept-language", "en-us");
         var1.putHeader("accept-encoding", "gzip, deflate, br");
         var1.putHeader("inkiru_precedence", "false");
         var1.putHeader("mobile-platform", "ios");
         var1.putHeader("content-length", "DEFAULT_VALUE");
         var1.putHeader("did", DID.replace("-", "").concat("00000000"));
         var1.putHeader("user-agent", "Walmart/2107091553 Walmart WMTAPP v21.18.3");
         var1.putHeader("cookie", "DEFAULT_VALUE");
         var1.putHeader("x-px-ab", "2");
      } else {
         var1.putHeaders(Headers$Pseudo.MPAS.get());
         var1.putHeader("Accept", "application/json, text/javascript, */*; q=0.01");
         var1.putHeader("wm_vertical_id", "0");
         var1.putHeader("inkiru_precedence", "false");
         var1.putHeader("wm_cvv_in_session", "true");
         var1.putHeader("x-px-ab", "2");
         var1.putHeader("User-Agent", WalmartConstants.ANDROID_OLD_WEB_UA);
         var1.putHeader("did", DID);
         var1.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
         var1.putHeader("Content-Type", "application/json");
         var1.putHeader("Content-Length", "DEFAULT_VALUE");
         var1.putHeader("Accept-Encoding", "gzip");
         var1.putHeader("Cookie", "DEFAULT_VALUE");
      }

      return var1;
   }

   public JsonObject getShippingRateForm(JsonObject var1) {
      String var2 = var1.getJsonArray("items").getJsonObject(0).getString("id");
      JsonArray var3 = var1.getJsonArray("items");
      String var4 = var3.getJsonObject(0).getJsonObject("fulfillmentSelection").getString("shipMethod");
      JsonObject var5 = (new JsonObject()).put("fulfillmentOption", "S2H").put("itemIds", (new JsonArray()).add(var2)).put("shipMethod", var4);
      JsonArray var6 = (new JsonArray()).add(var5);
      return (new JsonObject()).put("groups", var6);
   }

   public HttpRequest midasScan() {
      String var10001 = this.ios ? "iPhone" : "android";
      HttpRequest var1 = this.client.getAbs("https://www.walmart.com/midas/srv/v3/hl/item/506574645?type=product&pageType=item&platform=app&module=wpa&mloc=middle&min=2&max=20&placementId=480x212_B-C-OG_TI_2-20_PDP-m-app-" + var10001 + "&pageId=506574645&taxonomy=0%3A4171%3A4191%3A9807313%3A6249075%3A1619679&keyword=20%20PANINI%20PLAYBOOK%20FOOTBALL%20HANGER%20BOX%20(%2030%20cards%20per%20box)&zipCode=" + this.getTask().getProfile().getZip() + "&isZipLocated=false").timeout(TimeUnit.SECONDS.toMillis(10L)).as(BodyCodec.none());
      if (this.ios) {
         var1.putHeaders(Headers$Pseudo.MSPA.get());
         var1.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
         var1.putHeader("accept", "*/*");
         var1.putHeader("mobile-app-version", "21.18.3");
         var1.putHeader("wm_site_mode", "0");
         var1.putHeader("accept-language", "en-us");
         var1.putHeader("accept-encoding", "gzip, deflate, br");
         var1.putHeader("mobile-platform", "ios");
         var1.putHeader("did", DID.replace("-", "").concat("00000000"));
         var1.putHeader("user-agent", "Walmart/2107091553 CFNetwork/1128.0.1 Darwin/19.6.0");
         var1.putHeader("cookie", "DEFAULT_VALUE");
      } else {
         var1.putHeaders(Headers$Pseudo.MPAS.get());
         var1.putHeader("wm_site_mode", "0");
         var1.putHeader("user-agent", WalmartConstants.ANDROID_OLD_UA);
         var1.putHeader("did", DID);
         var1.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
         var1.putHeader("accept-encoding", "gzip");
         var1.putHeader("cookie", "DEFAULT_VALUE");
      }

      return var1;
   }

   public HttpRequest loginAccount() {
      HttpRequest var1 = this.client.postAbs("https://www.walmart.com/account/electrode/api/identity/password").as(BodyCodec.buffer());
      if (this.ios) {
         var1.putHeaders(Headers$Pseudo.MSPA.get());
         var1.putHeader("content-type", "text/plain;charset=UTF-8");
         var1.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
         var1.putHeader("x-wm-auth", "null");
         var1.putHeader("accept", "*/*");
         var1.putHeader("x-wm-xpa", "null");
         var1.putHeader("mobile-app-version", "21.18.3");
         var1.putHeader("x-wm-cid", "");
         var1.putHeader("accept-language", "en-us");
         var1.putHeader("accept-encoding", "gzip, deflate, br");
         var1.putHeader("mobile-platform", "ios");
         var1.putHeader("content-length", "DEFAULT_VALUE");
         var1.putHeader("did", DID.replace("-", "").concat("00000000"));
         var1.putHeader("x-wm-spid", "");
         var1.putHeader("user-agent", "Walmart/2107091553 CFNetwork/1128.0.1 Darwin/19.6.0");
      } else {
         var1.putHeaders(Headers$Pseudo.MPAS.get());
         var1.putHeader("x-wm-auth", "null");
         var1.putHeader("x-wm-cid", "null");
         var1.putHeader("x-wm-spid", "null");
         var1.putHeader("x-wm-xpa", "null");
         var1.putHeader("user-agent", WalmartConstants.ANDROID_OLD_UA);
         var1.putHeader("did", DID);
         var1.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
         var1.putHeader("content-type", "text/plain;charset=UTF-8");
         var1.putHeader("content-length", "DEFAULT_VALUE");
         var1.putHeader("accept-encoding", "gzip");
      }

      return var1;
   }

   public HttpRequest affilCrossSite(String var1) {
      this.getCookies().removeAnyMatch("CRT");
      this.getCookies().removeAnyMatch("cart-item-count");
      this.getCookies().removeAnyMatch("hasCRT");
      HttpRequest var2 = this.client.getAbs("https://affil.walmart.com/cart/" + var1 + "?items=" + this.task.getKeywords()[0] + "|" + this.task.getSize()).as(BodyCodec.none());
      var2.putHeader("upgrade-insecure-requests", "1");
      var2.putHeader("user-agent", WalmartConstants.ANDROID_OLD_UA);
      var2.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      if (this.crossSite != null) {
         var2.putHeader("referer", "https://" + this.crossSite + "/");
         if (this.crossSite.equals("t.co")) {
            var2.headers().remove("sec-fetch-user");
         }
      }

      var2.putHeader("accept-encoding", "gzip");
      var2.putHeader("cookie", "DEFAULT_VALUE");
      if (this.dateOfPrevReq != null) {
         var2.putHeader("if-modified-since", this.dateOfPrevReq);
      }

      this.dateOfPrevReq = GMT_CHROME_RFC1123.format(ZonedDateTime.now(ZoneOffset.UTC));
      return var2;
   }

   public HttpRequest putLocation() {
      HttpRequest var1 = this.client.putAbs("https://www.walmart.com/account/api/location?skipCache=true").timeout(TimeUnit.SECONDS.toMillis(10L)).as(BodyCodec.none());
      var1.putHeaders(Headers$Pseudo.MPAS.get());
      var1.putHeader("User-Agent", WalmartConstants.ANDROID_OLD_UA);
      var1.putHeader("did", DID);
      var1.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
      var1.putHeader("Content-Type", "application/json; charset=utf-8");
      var1.putHeader("Content-Length", "DEFAULT_VALUE");
      var1.putHeader("Accept-Encoding", "gzip");
      var1.putHeader("Cookie", "DEFAULT_VALUE");
      return var1;
   }

   static {
      GMT_CHROME_RFC1123 = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH);
   }

   public HttpRequest presoSearch() {
      HttpRequest var1 = this.client.getAbs("https://www.walmart.com/preso/search?prg=mWeb&wpm=0&assetProtocol=secure&query=panini&page=1&spelling=true&preciseSearch=true&vertical_whitelist=home%2Cfanatics%2Cfashion&pref_store=5880%2C2015%2C5936%2C5969%2C5227&zipcode=22124&applyDealsRedirect=true").timeout(TimeUnit.SECONDS.toMillis(10L)).as(BodyCodec.none());
      if (this.ios) {
         var1.putHeaders(Headers$Pseudo.MSPA.get());
         var1.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
         var1.putHeader("accept", "*/*");
         var1.putHeader("mobile-app-version", "21.18.3");
         var1.putHeader("wm_site_mode", "0");
         var1.putHeader("accept-encoding", "gzip, deflate, br");
         var1.putHeader("accept-language", "en-us");
         var1.putHeader("access_key", "532c28d5412dd75bf975fb951c740a30");
         var1.putHeader("mobile-platform", "ios");
         var1.putHeader("cid", "");
         var1.putHeader("did", DID.replace("-", "").concat("00000000"));
         var1.putHeader("user-agent", "Walmart/2107091553 CFNetwork/1128.0.1 Darwin/19.6.0");
         var1.putHeader("cookie", "DEFAULT_VALUE");
      } else {
         var1.putHeaders(Headers$Pseudo.MPAS.get());
         var1.putHeader("access_key", "532c28d5412dd75bf975fb951c740a30");
         var1.putHeader("cid", "");
         ThreadLocalRandom var10002 = ThreadLocalRandom.current();
         var1.putHeader("client-ip", "192.168.0." + var10002.nextInt(1, 255));
         var1.putHeader("wm_site_mode", "0");
         var1.putHeader("User-Agent", WalmartConstants.ANDROID_OLD_UA);
         var1.putHeader("did", DID);
         var1.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
         var1.putHeader("Accept-Encoding", "gzip");
         var1.putHeader("Cookie", "DEFAULT_VALUE");
      }

      return var1;
   }

   public HttpRequest atcAffiliate(String var1) {
      HttpRequest var2 = super.client.getAbs(var1).as(BodyCodec.none());
      var2.putHeader("Pragma", "no-cache");
      var2.putHeader("Cache-Control", "no-cache");
      var2.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"88\", \" Not;A Brand\";v=\"99\", \"Chromium\";v=\"88\"");
      var2.putHeader("sec-ch-ua-mobile", "?0");
      var2.putHeader("Upgrade-Insecure-Requests", "1");
      var2.putHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.182 Safari/537.36");
      var2.putHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var2.putHeader("Sec-Fetch-Site", "none");
      var2.putHeader("Sec-Fetch-Mode", "navigate");
      var2.putHeader("Sec-Fetch-User", "?1");
      var2.putHeader("Sec-Fetch-Dest", "document");
      var2.putHeader("Accept-Encoding", "gzip, deflate, br");
      var2.putHeader("Accept-Language", "en-US,en;q=0.9");
      if (this.dateOfPrevReq != null) {
         var2.putHeader("if-modified-since", this.dateOfPrevReq);
      }

      this.dateOfPrevReq = GMT_CHROME_RFC1123.format(ZonedDateTime.now(ZoneOffset.UTC));
      return var2;
   }

   public CookieJar cookieStore() {
      return this.getWebClient().cookieStore();
   }

   public JsonObject PCIDForm() {
      Profile var1 = this.task.getProfile();
      return (new JsonObject()).put("city", var1.getCity()).put("crt:CRT", "").put("customerId:CID", "").put("customerType:type", "").put("isZipLocated", true).put("postalCode", var1.getZip()).put("state", var1.getState()).put("storeList", new JsonArray());
   }

   public HttpRequest getCartV2(String var1) {
      return this.getCartMobile("https://www.walmart.com/api/v2/cart/" + var1);
   }

   public HttpRequest getCart() {
      return this.getCartMobile("https://api.mobile.walmart.com/v1/cart/items");
   }

   public HttpRequest submitShipping() {
      HttpRequest var1 = this.client.postAbs("https://www.walmart.com/api/checkout/v3/contract/:PCID/shipping-address").as(BodyCodec.buffer());
      if (this.ios) {
         var1.putHeaders(Headers$Pseudo.MSPA.get());
         var1.putHeader("content-type", "application/json");
         var1.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
         var1.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
         var1.putHeader("mobile-app-version", "21.18.3");
         var1.putHeader("accept-language", "en-us");
         var1.putHeader("accept-encoding", "gzip, deflate, br");
         var1.putHeader("mobile-platform", "ios");
         var1.putHeader("content-length", "DEFAULT_VALUE");
         var1.putHeader("did", DID.replace("-", "").concat("00000000"));
         var1.putHeader("user-agent", "Walmart/2107091553 Walmart WMTAPP v21.18.3");
         var1.putHeader("cookie", "DEFAULT_VALUE");
         var1.putHeader("x-px-ab", "2");
      } else {
         var1.putHeaders(Headers$Pseudo.MPAS.get());
         var1.putHeader("Accept", "application/json, text/javascript, */*; q=0.01");
         var1.putHeader("wm_vertical_id", "0");
         var1.putHeader("inkiru_precedence", "false");
         var1.putHeader("wm_cvv_in_session", "true");
         var1.putHeader("x-px-ab", "2");
         var1.putHeader("User-Agent", WalmartConstants.ANDROID_OLD_WEB_UA);
         var1.putHeader("did", DID);
         var1.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
         var1.putHeader("Content-Type", "application/json");
         var1.putHeader("Content-Length", "DEFAULT_VALUE");
         var1.putHeader("Accept-Encoding", "gzip");
         var1.putHeader("Cookie", "DEFAULT_VALUE");
      }

      return var1;
   }

   public void swapClient() {
      try {
         RealClient var1 = RealClientFactory.fromOther(Vertx.currentContext().owner(), super.client, this.client.type());
         super.client.close();
         super.client = var1;
      } catch (Throwable var2) {
      }

   }

   public static CompletableFuture async$initialisePX(WalmartAPI var0, CompletableFuture var1, int var2, Object var3) {
      CompletableFuture var10000;
      switch (var2) {
         case 0:
            var10000 = var0.pxAPI.initialise();
            if (!var10000.isDone()) {
               var1 = var10000;
               return var1.exceptionally(Function.identity()).thenCompose(WalmartAPI::async$initialisePX);
            }
            break;
         case 1:
            var10000 = var1;
            break;
         default:
            throw new IllegalArgumentException();
      }

      return (Boolean)var10000.join() ? CompletableFuture.completedFuture(true) : CompletableFuture.completedFuture(false);
   }

   public JsonObject atcForm() {
      return this.getAtcForm(this.task.getKeywords()[0], Integer.parseInt(this.task.getSize()));
   }

   public HttpRequest mobileAtc() {
      return this.task.getMode().contains("other") ? this.addToCart("https://www.walmart.com/api/v1/cart/items") : this.addToCart("https://api.mobile.walmart.com/v1/cart/items");
   }

   public HttpRequest putCart() {
      return this.putCartAndroid("476e4599-389d-458a-94dd-c18c583686d0");
   }

   public HttpRequest getLocation() {
      HttpRequest var1 = this.client.postAbs("https://www.walmart.com/account/api/location?skipCache=true").timeout(TimeUnit.SECONDS.toMillis(10L)).as(BodyCodec.none());
      if (this.ios) {
         var1.putHeaders(Headers$Pseudo.MSPA.get());
         var1.putHeader("content-type", "application/json");
         var1.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
         var1.putHeader("accept", "*/*");
         var1.putHeader("app-source-event", "");
         var1.putHeader("x-o-platform", "ios");
         var1.putHeader("mobile-app-version", "21.18.3");
         var1.putHeader("accept-language", "en-us");
         var1.putHeader("accept-encoding", "gzip, deflate, br");
         var1.putHeader("x-o-platform-version", "21.18.3");
         var1.putHeader("mobile-platform", "ios");
         var1.putHeader("did", DID.replace("-", "").concat("00000000"));
         var1.putHeader("content-length", "DEFAULT_VALUE");
         var1.putHeader("user-agent", "Walmart/2107091553 CFNetwork/1128.0.1 Darwin/19.6.0");
         var1.putHeader("Cookie", "DEFAULT_VALUE");
      } else {
         var1.putHeaders(Headers$Pseudo.MPAS.get());
         var1.putHeader("User-Agent", WalmartConstants.ANDROID_OLD_UA);
         var1.putHeader("did", DID);
         var1.putHeader("Content-Type", "application/json; charset=utf-8");
         var1.putHeader("Content-Length", "DEFAULT_VALUE");
         var1.putHeader("Accept-Encoding", "gzip");
         var1.putHeader("Cookie", "DEFAULT_VALUE");
      }

      return var1;
   }

   public HttpRequest getCheckoutPage() {
      HttpRequest var1 = this.client.getAbs("https://www.walmart.com/checkout/").timeout(TimeUnit.SECONDS.toMillis(10L)).as(BodyCodec.none());
      if (this.ios) {
         var1.putHeaders(Headers$Pseudo.MSPA.get());
         var1.putHeader("accept", "*/*");
         var1.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
         var1.putHeader("did", DID.replace("-", "").concat("00000000"));
         var1.putHeader("mobile-app-version", "21.18.3");
         var1.putHeader("mobile-platform", "ios");
         var1.putHeader("cookie", "DEFAULT_VALUE");
         var1.putHeader("accept-language", "en-us");
         var1.putHeader("user-agent", "Walmart/2107091553 CFNetwork/1128.0.1 Darwin/19.6.0");
         var1.putHeader("accept-encoding", "gzip, deflate, br");
      } else {
         var1.putHeaders(Headers$Pseudo.MPAS.get());
         var1.putHeader("User-Agent", WalmartConstants.ANDROID_OLD_UA);
         var1.putHeader("did", DID);
         var1.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
         var1.putHeader("Accept-Encoding", "gzip");
         var1.putHeader("Cookie", "DEFAULT_VALUE");
      }

      return var1;
   }

   public WalmartAPI(Task var1) {
      super(ClientType.WALMART_PIXEL_3);
      this.task = var1;
      if (this.task.getMode().contains("desktop")) {
         this.ios = false;
      } else {
         this.ios = var1.getMode().contains("2");
      }

      this.crossSite = crossSiteList[ThreadLocalRandom.current().nextInt(crossSiteList.length)];
      this.searchQuery = searchQueries[ThreadLocalRandom.current().nextInt(searchQueries.length)];
      this.loggedIn = false;
   }

   public void close() {
      if (this.pxAPI != null) {
         this.pxAPI.close();
      }

      super.close();
   }

   public HttpRequest transferCart(String var1) {
      HttpRequest var2 = super.client.postAbs("https://www.walmart.com/api//saved/:CID/items/" + var1 + "/transfer").as(BodyCodec.buffer());
      if (this.ios) {
         var2.putHeaders(Headers$Pseudo.MSPA.get());
         var2.putHeader("content-type", "application/json");
         var2.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
         var2.putHeader("accept", "*/*");
         var2.putHeader("mobile-app-version", "21.18.3");
         var2.putHeader("accept-language", "en-us");
         var2.putHeader("accept-encoding", "gzip, deflate, br");
         var2.putHeader("mobile-platform", "ios");
         var2.putHeader("content-length", "DEFAULT_VALUE");
         var2.putHeader("did", DID.replace("-", "").concat("00000000"));
         var2.putHeader("credentials", "include");
         var2.putHeader("omitcsrfjwt", "true");
         var2.putHeader("user-agent", "Walmart/2107091553 CFNetwork/1128.0.1 Darwin/19.6.0");
         var2.putHeader("cookie", "DEFAULT_VALUE");
      } else {
         var2.putHeaders(Headers$Pseudo.MPAS.get());
         var2.putHeader("user-agent", WalmartConstants.ANDROID_OLD_UA);
         var2.putHeader("did", DID);
         var2.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
         var2.putHeader("content-type", "application/json; charset=utf-8");
         var2.putHeader("content-length", "DEFAULT_VALUE");
         var2.putHeader("credentials", "include");
         var2.putHeader("omitcsrfjwt", "true");
         var2.putHeader("accept-encoding", "gzip");
         var2.putHeader("cookie", "DEFAULT_VALUE");
      }

      return var2;
   }

   public JsonObject getBillingForm(PaymentToken var1) {
      JsonObject var2 = new JsonObject();
      Profile var3 = this.task.getProfile();
      var2.put("addressLineOne", var3.getAddress1());
      var2.put("addressLineTwo", var3.getAddress2());
      var2.put("cardType", var3.getPaymentMethod().get());
      var2.put("city", var3.getCity());
      var2.put("encryptedCvv", var1.getEncryptedCvv());
      var2.put("encryptedPan", var1.getEncryptedPan());
      var2.put("expiryMonth", var3.getExpiryMonth());
      var2.put("expiryYear", var3.getExpiryYear());
      var2.put("firstName", var3.getFirstName());
      var2.put("integrityCheck", var1.getIntegrityCheck());
      var2.put("keyId", var1.getKeyId());
      var2.put("lastName", var3.getLastName());
      var2.put("phase", var1.getPhase());
      var2.put("phone", var3.getPhone());
      var2.put("postalCode", var3.getZip());
      var2.put("state", var3.getState().toUpperCase());
      var2.put("isGuest", true);
      return var2;
   }

   public CompletableFuture initialisePX() {
      CompletableFuture var10000 = this.pxAPI.initialise();
      if (!var10000.isDone()) {
         CompletableFuture var1 = var10000;
         return var1.exceptionally(Function.identity()).thenCompose(WalmartAPI::async$initialisePX);
      } else {
         return (Boolean)var10000.join() ? CompletableFuture.completedFuture(true) : CompletableFuture.completedFuture(false);
      }
   }

   public HttpRequest desktopAtcAffiliate() {
      String var10001 = this.task.getKeywords()[0];
      return this.atcAffiliate("https://affil.walmart.com/cart/buynow?items=" + var10001 + "|" + this.task.getSize());
   }

   public HttpRequest addToCart() {
      return this.mobileAtc();
   }

   public HttpRequest otherAtc() {
      return this.addToCart("https://www.walmart.com/api/v2/cart/guest/:CID/items");
   }
}
