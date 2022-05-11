package io.trickle.task.sites.yeezy;

import io.netty.handler.codec.http.cookie.ClientCookieDecoder;
import io.netty.handler.codec.http.cookie.Cookie;
import io.trickle.core.Engine;
import io.trickle.core.VertxSingleton;
import io.trickle.profile.Profile;
import io.trickle.task.Task;
import io.trickle.task.antibot.impl.akamai.GaneshAPI;
import io.trickle.task.antibot.impl.akamai.HawkAPI;
import io.trickle.task.antibot.impl.akamai.pixel.Pixel;
import io.trickle.task.antibot.impl.akamai.pixel.TrickleAPI;
import io.trickle.task.antibot.impl.akamai.sensor.Bmak;
import io.trickle.task.sites.yeezy.util.Sizes;
import io.trickle.task.sites.yeezy.util.Sizes$SizePair;
import io.trickle.task.sites.yeezy.util.Utag;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.webclient.CookieJar$ExpirableCookie;
import io.trickle.webclient.TaskApiClient;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.StreamPriority;
import io.vertx.core.http.impl.HttpClientImpl;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.impl.NetClientImpl;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.impl.ClientPhase;
import io.vertx.ext.web.client.impl.HttpContext;
import io.vertx.ext.web.codec.BodyCodec;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class YeezyAPI extends TaskApiClient {
   public Task task;
   public String sensorUrl = "https://www.yeezysupply.com/QvDG3p/zKgz/wMSc/6Rgu/84e_NO/5t5ESppzm3fi/Qk1Q/QGIM/LzdYPyQ";
   public static String QUEUE_URL = Engine.get().getClientConfiguration().getString("queueUrl", "https://www.yeezysupply.com/__queue/yzysply");
   public String pixelURI = null;
   public Utag utag;
   public Pixel pixelAPI;
   public Bmak bmak;
   public String userAgent;
   public JsonObject taskDevice;
   public int sensorCounter;

   public HttpRequest akamaiScript(boolean var1) {
      HttpRequest var2 = super.client.getAbs(this.sensorUrl).as(BodyCodec.string());
      var2.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"99\", \"Google Chrome\";v=\"99\"");
      var2.putHeader("sec-ch-ua-mobile", "?0");
      var2.putHeader("user-agent", this.userAgent);
      var2.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var2.putHeader("accept", "*/*");
      var2.putHeader("sec-fetch-site", "same-origin");
      var2.putHeader("sec-fetch-mode", "no-cors");
      var2.putHeader("sec-fetch-dest", "script");
      var2.putHeader("referer", var1 ? "https://www.yeezysupply.com/products/" + this.getSKU() : "https://www.yeezysupply.com/");
      var2.putHeader("accept-encoding", "gzip, deflate, br");
      var2.putHeader("accept-language", "en-US,en;q=0.9");
      return var2;
   }

   public HttpRequest processPayment(String var1) {
      HttpRequest var2 = super.client.postAbs("https://www.yeezysupply.com/api/checkout/orders").as(BodyCodec.string());
      String var3 = this.getInstanaID();
      var2.putHeader("content-length", "DEFAULT_VALUE");
      var2.putHeader("x-instana-t", var3);
      var2.putHeader("sec-ch-ua-mobile", "?0");
      var2.putHeader("user-agent", this.userAgent);
      var2.putHeader("x-instana-l", "1,correlationType=web;correlationId=" + var3);
      var2.putHeader("x-instana-s", var3);
      var2.putHeader("content-type", "application/json");
      var2.putHeader("checkout-authorization", var1);
      var2.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"95\", \"Chromium\";v=\"95\", \";Not A Brand\";v=\"99\"");
      var2.putHeader("accept", "*/*");
      var2.putHeader("origin", "https://www.yeezysupply.com");
      var2.putHeader("sec-fetch-site", "same-origin");
      var2.putHeader("sec-fetch-mode", "cors");
      var2.putHeader("sec-fetch-dest", "empty");
      var2.putHeader("referer", "https://www.yeezysupply.com/payment");
      var2.putHeader("accept-encoding", "gzip, deflate, br");
      var2.putHeader("accept-language", "en-US,en;q=0.9");
      return var2;
   }

   public HttpRequest generalProdAPI() {
      HttpRequest var1 = super.client.getAbs("https://www.yeezysupply.com/api/products/" + this.getSKU() + "/availability").as(BodyCodec.string());
      String var2 = this.getInstanaID();
      var1.putHeader("x-instana-t", var2);
      var1.putHeader("content-type", "application/json");
      var1.putHeader("x-instana-s", var2);
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", this.userAgent);
      var1.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"99\", \"Google Chrome\";v=\"99\"");
      var1.putHeader("x-instana-l", "1,correlationType=web;correlationId=" + var2);
      var1.putHeader("accept", "*/*");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.yeezysupply.com/product/" + this.getSKU());
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public void lambda$enableWeightAdjust$0(HttpContext var1) {
      if (this.pixelURI != null && var1.phase().equals(ClientPhase.SEND_REQUEST) && var1.clientRequest() != null) {
         try {
            if (this.pixelURI.endsWith(var1.clientRequest().getURI())) {
               var1.clientRequest().setStreamPriority((new StreamPriority()).setWeight((short)147));
            }
         } catch (Throwable var3) {
         }
      }

      var1.next();
   }

   public HttpRequest deleteCoupon(String var1, String var2, String var3) {
      HttpRequest var4 = this.client.deleteAbs("https://www.yeezysupply.com/api/checkout/baskets/" + var1 + "/coupons/" + var3).as(BodyCodec.buffer());
      String var5 = this.getInstanaID();
      var4.putHeader("x-instana-t", var5);
      var4.putHeader("sec-ch-ua-mobile", "?0");
      var4.putHeader("x-instana-l", "1,correlationType=web;correlationId=" + var5);
      var4.putHeader("content-type", "application/json");
      var4.putHeader("user-agent", this.userAgent);
      var4.putHeader("x-instana-s", var5);
      var4.putHeader("checkout-authorization", var2);
      var4.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var4.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"95\", \"Chromium\";v=\"95\", \";Not A Brand\";v=\"99\"");
      var4.putHeader("accept", "*/*");
      var4.putHeader("origin", "https://www.yeezysupply.com");
      var4.putHeader("sec-fetch-site", "same-origin");
      var4.putHeader("sec-fetch-mode", "cors");
      var4.putHeader("sec-fetch-dest", "empty");
      var4.putHeader("referer", "https://www.yeezysupply.com/payment");
      var4.putHeader("accept-encoding", "gzip, deflate, br");
      var4.putHeader("accept-language", "en-US,en;q=0.9");
      return var4;
   }

   public HttpRequest getPixel(String var1) {
      this.pixelURI = var1;
      HttpRequest var2 = super.client.getAbs(var1).as(BodyCodec.string());
      var2.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"99\", \"Google Chrome\";v=\"99\"");
      var2.putHeader("sec-ch-ua-mobile", "?0");
      var2.putHeader("user-agent", this.userAgent);
      var2.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var2.putHeader("accept", "*/*");
      var2.putHeader("sec-fetch-site", "same-origin");
      var2.putHeader("sec-fetch-mode", "no-cors");
      var2.putHeader("sec-fetch-dest", "script");
      var2.putHeader("referer", "https://www.yeezysupply.com/");
      var2.putHeader("accept-encoding", "gzip, deflate, br");
      var2.putHeader("accept-language", "en-US,en;q=0.9");
      return var2;
   }

   public Buffer billingBody(Profile var1) {
      String var10000 = var1.getEmail();
      String var2 = "{\"customer\":{\"email\":\"" + var10000 + "\",\"receiveSmsUpdates\":false},\"shippingAddress\":{\"country\":\"US\",\"firstName\":\"" + var1.getFirstName().substring(0, 1).toUpperCase() + var1.getFirstName().substring(1).toLowerCase() + "\",\"lastName\":\"" + var1.getLastName().substring(0, 1).toUpperCase() + var1.getLastName().substring(1).toLowerCase() + "\",\"address1\":\"" + var1.getAddress1() + "\",\"address2\":\"" + var1.getAddress2() + "\",\"city\":\"" + var1.getCity().substring(0, 1).toUpperCase() + var1.getCity().substring(1).toLowerCase() + "\",\"stateCode\":\"" + var1.getState() + "\",\"zipcode\":\"" + var1.getZip() + "\",\"phoneNumber\":\"" + var1.getPhone() + "\"},\"billingAddress\":{\"country\":\"US\",\"firstName\":\"" + var1.getFirstName().substring(0, 1).toUpperCase() + var1.getFirstName().substring(1).toLowerCase() + "\",\"lastName\":\"" + var1.getLastName().substring(0, 1).toUpperCase() + var1.getLastName().substring(1).toLowerCase() + "\",\"address1\":\"" + var1.getAddress1() + "\",\"address2\":\"" + var1.getAddress2() + "\",\"city\":\"" + var1.getCity().substring(0, 1).toUpperCase() + var1.getCity().substring(1).toLowerCase() + "\",\"stateCode\":\"" + var1.getState() + "\",\"zipcode\":\"" + var1.getZip() + "\",\"phoneNumber\":\"" + var1.getPhone() + "\"},\"methodList\":[{\"id\":\"2ndDay-1\",\"shipmentId\":\"me\",\"collectionPeriod\":\"\",\"deliveryPeriod\":\"\"}],\"newsletterSubscription\":true}";
      return Buffer.buffer(var2);
   }

   public JsonArray atcForm(String var1) {
      JsonObject var2 = new JsonObject();
      String var10000 = this.getSKU();
      String var3 = var10000 + Sizes.getSize(var1);
      var2.put("product_id", this.getSKU());
      var2.put("product_variation_sku", var3);
      var2.put("productId", var3);
      var2.put("quantity", 1);
      var2.put("size", var1);
      var2.put("displaySize", var1);
      return (new JsonArray()).add(var2);
   }

   public static CompletableFuture async$init(YeezyAPI param0, int param1, HawkAPI param2, YeezyAPI param3, CompletableFuture param4, GaneshAPI param5, Throwable param6, int param7, Object param8) {
      // $FF: Couldn't be decompiled
   }

   public HttpRequest secondaryProdAPI() {
      HttpRequest var1 = super.client.getAbs("https://www.yeezysupply.com/api/products/" + this.getSKU()).as(BodyCodec.string());
      String var2 = this.getInstanaID();
      var1.putHeader("x-instana-t", var2);
      var1.putHeader("content-type", "application/json");
      var1.putHeader("x-instana-s", var2);
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", this.userAgent);
      var1.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"99\", \"Google Chrome\";v=\"99\"");
      var1.putHeader("x-instana-l", "1,correlationType=web;correlationId=" + var2);
      var1.putHeader("accept", "*/*");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.yeezysupply.com/product/" + this.getSKU());
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public void enableWeightAdjust() {
      super.client.addInterceptor(this::lambda$enableWeightAdjust$0);
   }

   public void lambda$handlePush$2(HttpClientRequest var1, String var2) {
      Cookie var3 = ClientCookieDecoder.STRICT.decode(var2);
      if (var3 != null) {
         if (var3.domain() == null) {
            var3.setDomain(var1.getHost());
         }

         super.getCookies().put(var3);
      }

   }

   public void lambda$handlePush$3(HttpClientRequest var1, HttpClientResponse var2) {
      List var3 = var2.cookies();
      if (var3 != null && !var3.isEmpty()) {
         var3.forEach(this::lambda$handlePush$2);
      }

   }

   public HttpRequest getPaymentMethod(String var1, String var2) {
      HttpRequest var3 = super.client.getAbs("https://www.yeezysupply.com/api/checkout/baskets/" + var1 + "/payment_methods").as(BodyCodec.string());
      String var4 = this.getInstanaID();
      var3.putHeader("x-instana-t", var4);
      var3.putHeader("sec-ch-ua-mobile", "?0");
      var3.putHeader("user-agent", this.userAgent);
      var3.putHeader("x-instana-l", "1,correlationType=web;correlationId=" + var4);
      var3.putHeader("x-instana-s", var4);
      var3.putHeader("content-type", "application/json");
      var3.putHeader("checkout-authorization", var2);
      var3.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"95\", \"Chromium\";v=\"95\", \";Not A Brand\";v=\"99\"");
      var3.putHeader("accept", "*/*");
      var3.putHeader("sec-fetch-site", "same-origin");
      var3.putHeader("sec-fetch-mode", "cors");
      var3.putHeader("sec-fetch-dest", "empty");
      var3.putHeader("referer", "https://www.yeezysupply.com/payment");
      var3.putHeader("accept-encoding", "gzip, deflate, br");
      var3.putHeader("accept-language", "en-US,en;q=0.9");
      return var3;
   }

   public HttpRequest productPage(boolean var1) {
      HttpRequest var2 = super.client.getAbs(this.getSKU().equals("BY9611") ? "https://www.yeezysupply.com/archive/" + this.getSKU() : "https://www.yeezysupply.com/product/" + this.getSKU()).as(BodyCodec.string());
      if (var1) {
         var2.putHeader("cache-control", "max-age=0");
      }

      var2.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"99\", \"Google Chrome\";v=\"99\"");
      var2.putHeader("sec-ch-ua-mobile", "?0");
      var2.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var2.putHeader("upgrade-insecure-requests", "1");
      var2.putHeader("user-agent", this.userAgent);
      var2.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var2.putHeader("sec-fetch-site", "same-origin");
      var2.putHeader("sec-fetch-mode", "navigate");
      if (!var1) {
         var2.putHeader("sec-fetch-user", "?1");
      }

      var2.putHeader("sec-fetch-dest", "document");
      if (var1) {
         var2.putHeader("referer", "https://www.yeezysupply.com/product/" + this.getSKU());
      } else {
         var2.putHeader("referer", "https://www.yeezysupply.com/");
      }

      var2.putHeader("accept-encoding", "gzip, deflate, br");
      var2.putHeader("accept-language", "en-US,en;q=0.9");
      return var2;
   }

   public static CompletableFuture async$getTrickleSensor(YeezyAPI var0, String var1, CompletableFuture var2, int var3, Object var4) {
      CompletableFuture var10000;
      label25:
      switch (var3) {
         case 0:
            var0.bmak.updateDocumentUrl("https://www.yeezysupply.com/product/" + var0.getSKU());
            var1 = "";

            try {
               var1 = var0.getCookies().getCookieValue("_abck");
            } catch (Exception var5) {
               var5.printStackTrace();
            }

            switch (var0.sensorCounter++) {
               case 0:
                  return CompletableFuture.completedFuture(var0.bmak.genPageLoadSensor(var1));
               case 1:
                  return CompletableFuture.completedFuture(var0.bmak.genSecondSensor(var1));
               default:
                  var10000 = var0.reInit();
                  if (!var10000.isDone()) {
                     CompletableFuture var6 = var10000;
                     return var6.exceptionally(Function.identity()).thenCompose(YeezyAPI::async$getTrickleSensor);
                  }
                  break label25;
            }
         case 1:
            var10000 = var2;
            break;
         default:
            throw new IllegalArgumentException();
      }

      var10000.join();
      var0.sensorCounter = 1;
      return CompletableFuture.completedFuture(var0.bmak.genPageLoadSensor(var1));
   }

   public HttpRequest submitShippingAndBilling(String var1, String var2) {
      HttpRequest var3 = super.client.patchAbs("https://www.yeezysupply.com/api/checkout/baskets/" + var1).as(BodyCodec.string());
      String var4 = this.getInstanaID();
      var3.putHeader("x-instana-t", var4);
      var3.putHeader("content-length", "DEFAULT_VALUE");
      var3.putHeader("sec-ch-ua-mobile", "?0");
      var3.putHeader("user-agent", this.userAgent);
      var3.putHeader("x-instana-l", "1,correlationType=web;correlationId=" + var4);
      var3.putHeader("x-instana-s", var4);
      var3.putHeader("content-type", "application/json");
      var3.putHeader("checkout-authorization", var2);
      var3.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var3.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"95\", \"Chromium\";v=\"95\", \";Not A Brand\";v=\"99\"");
      var3.putHeader("accept", "*/*");
      var3.putHeader("origin", "https://www.yeezysupply.com");
      var3.putHeader("sec-fetch-site", "same-origin");
      var3.putHeader("sec-fetch-mode", "cors");
      var3.putHeader("sec-fetch-dest", "empty");
      var3.putHeader("referer", "https://www.yeezysupply.com/delivery");
      var3.putHeader("accept-encoding", "gzip, deflate, br");
      var3.putHeader("accept-language", "en-US,en;q=0.9");
      return var3;
   }

   public YeezyAPI(Task var1) {
      this.task = var1;
      this.sensorCounter = 0;
      this.enablePush();
      this.enableWeightAdjust();
   }

   public void enablePush() {
      super.client.addInterceptor(this::lambda$enablePush$1);
   }

   public HttpRequest verifyPage() {
      HttpRequest var1 = this.client.getAbs("https://www.yeezysupply.com/_sec/cp_challenge/verify").as(BodyCodec.buffer());
      String var2 = this.getInstanaID();
      var1.putHeader("x-instana-t", var2);
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", this.userAgent);
      var1.putHeader("x-instana-l", "1,correlationType=web;correlationId=" + var2);
      var1.putHeader("x-sec-clge-req-type", "ajax");
      var1.putHeader("x-instana-s", var2);
      var1.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var1.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"95\", \"Chromium\";v=\"95\", \";Not A Brand\";v=\"99\"");
      var1.putHeader("accept", "*/*");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.yeezysupply.com/product/" + this.getSKU());
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public HttpRequest bloom() {
      HttpRequest var1 = super.client.getAbs("https://www.yeezysupply.com/api/yeezysupply/products/bloom").as(BodyCodec.string());
      var1.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"99\", \"Google Chrome\";v=\"99\"");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", this.userAgent);
      var1.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var1.putHeader("content-type", "application/json");
      var1.putHeader("accept", "*/*");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.yeezysupply.com/");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public String fetchUtag() {
      return this.utag.genUtagMain();
   }

   public HttpRequest getAdiSensor() {
      HttpRequest var1 = this.client.getAbs("https://www.adidas.com" + this.sensorUrl).as(BodyCodec.buffer());
      var1.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"95\", \"Chromium\";v=\"95\", \";Not A Brand\";v=\"99\"");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", this.userAgent);
      var1.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var1.putHeader("accept", "*/*");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "no-cors");
      var1.putHeader("sec-fetch-dest", "script");
      var1.putHeader("referer", "https://www.adidas.com/us/ultraboost-21-shoes/FY0378.html");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      var1.putHeader("cookie", "DEFAULT_VALUE");
      return var1;
   }

   public HttpRequest postSensor(boolean var1) {
      HttpRequest var2 = super.client.postAbs(this.sensorUrl).as(BodyCodec.buffer());
      String var3 = this.getInstanaID();
      var2.putHeader("content-length", "DEFAULT_VALUE");
      var2.putHeader("x-instana-t", var3);
      var2.putHeader("sec-ch-ua-mobile", "?0");
      var2.putHeader("user-agent", this.userAgent);
      var2.putHeader("x-instana-l", "1,correlationType=web;correlationId=" + var3);
      var2.putHeader("x-instana-s", var3);
      var2.putHeader("content-type", "text/plain;charset=UTF-8");
      var2.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var2.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"99\", \"Google Chrome\";v=\"99\"");
      var2.putHeader("accept", "*/*");
      var2.putHeader("origin", "https://www.yeezysupply.com");
      var2.putHeader("sec-fetch-site", "same-origin");
      var2.putHeader("sec-fetch-mode", "cors");
      var2.putHeader("sec-fetch-dest", "empty");
      var2.putHeader("referer", var1 ? "https://www.yeezysupply.com/products/" + this.getSKU() : "https://www.yeezysupply.com/");
      var2.putHeader("accept-encoding", "gzip, deflate, br");
      var2.putHeader("accept-language", "en-US,en;q=0.9");
      return var2;
   }

   public void updateDocumentUrl(String var1) {
      if (this.bmak != null) {
         this.bmak.updateDocumentUrl(var1);
      }

      this.updateUtagUrl(var1);
   }

   public void handlePush(HttpClientRequest var1) {
      var1.response().onSuccess(this::lambda$handlePush$3);
   }

   public HttpRequest postSensorAdi() {
      HttpRequest var1 = this.client.postAbs("https://www.adidas.com" + this.sensorUrl).as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("x-instana-t", "d7ef9c8b4030bf01");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("x-instana-l", "1,correlationType=web;correlationId=d7ef9c8b4030bf01");
      var1.putHeader("content-type", "text/plain;charset=UTF-8");
      var1.putHeader("x-sec-clge-req-type", "ajax");
      var1.putHeader("user-agent", this.userAgent);
      var1.putHeader("x-instana-s", "d7ef9c8b4030bf01");
      var1.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var1.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"95\", \"Chromium\";v=\"95\", \";Not A Brand\";v=\"99\"");
      var1.putHeader("accept", "*/*");
      var1.putHeader("origin", "https://www.adidas.com");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.adidas.com/us/ultraboost-21-shoes/FY0378.html");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public HttpRequest ushpl() {
      HttpRequest var1 = super.client.getAbs("https://www.yeezysupply.com/hpl/content/yeezy-supply/releases/" + this.getSKU() + "/en_US.json").as(BodyCodec.string());
      var1.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"99\", \"Google Chrome\";v=\"99\"");
      var1.putHeader("accept", "application/json, text/plain, */*");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", this.userAgent);
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.yeezysupply.com/product/" + this.getSKU());
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public CompletableFuture init() {
      int var1 = 0;

      while(var1++ < 350000) {
         try {
            CompletableFuture var10001;
            CompletableFuture var4;
            if (this.task.getMode().contains("2")) {
               HawkAPI var2 = new HawkAPI();
               this.pixelAPI = var2;
               var10001 = var2.updateUserAgent();
               if (!var10001.isDone()) {
                  var4 = var10001;
                  return var4.exceptionally(Function.identity()).thenCompose(YeezyAPI::async$init);
               }

               this.updateUserAgent((String)var10001.join());
               if (this.userAgent != null) {
                  this.utag = new Utag(this.userAgent, this.getSKU());
                  return CompletableFuture.completedFuture(true);
               }
            } else if (this.task.getMode().contains("3")) {
               GaneshAPI var6 = new GaneshAPI();
               this.pixelAPI = var6;
               var10001 = var6.updateUserAgent();
               if (!var10001.isDone()) {
                  var4 = var10001;
                  return var4.exceptionally(Function.identity()).thenCompose(YeezyAPI::async$init);
               }

               this.updateUserAgent((String)var10001.join());
               if (this.userAgent != null) {
                  this.utag = new Utag(this.userAgent, this.getSKU());
                  return CompletableFuture.completedFuture(true);
               }
            } else {
               var10001 = VertxSingleton.INSTANCE.getLocalClient().fetchDeviceFromAPI("https://loudounchris.xyz/api/device/browserDevice.json?mobile=false&ak=true");
               if (!var10001.isDone()) {
                  var4 = var10001;
                  return var4.exceptionally(Function.identity()).thenCompose(YeezyAPI::async$init);
               }

               this.taskDevice = (JsonObject)var10001.join();
               if (this.taskDevice != null && !this.taskDevice.isEmpty()) {
                  this.bmak = new Bmak(this.taskDevice);
                  this.utag = new Utag(this.bmak, this.getSKU());
                  this.updateUserAgent(this.bmak.getUA());
                  this.pixelAPI = new TrickleAPI(this.taskDevice);
                  if (this.bmak != null && this.userAgent != null && this.pixelAPI != null) {
                     return CompletableFuture.completedFuture(true);
                  }
               }
            }
         } catch (Throwable var5) {
            CompletableFuture var7 = VertxUtil.hardCodedSleep(15000L);
            if (!var7.isDone()) {
               CompletableFuture var3 = var7;
               return var3.exceptionally(Function.identity()).thenCompose(YeezyAPI::async$init);
            }

            var7.join();
         }
      }

      return CompletableFuture.completedFuture(false);
   }

   public HttpRequest newsletter(String var1) {
      HttpRequest var2 = this.client.getAbs("https://www.yeezysupply.com/api/signup/" + var1 + "?trigger=").as(BodyCodec.buffer());
      String var3 = this.getInstanaID();
      var2.putHeader("x-instana-t", var3);
      var2.putHeader("sec-ch-ua-mobile", "?0");
      var2.putHeader("user-agent", this.userAgent);
      var2.putHeader("x-instana-l", "1,correlationType=web;correlationId=" + var3);
      var2.putHeader("x-instana-s", var3);
      var2.putHeader("content-type", "application/json");
      var2.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var2.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"99\", \"Google Chrome\";v=\"99\"");
      var2.putHeader("accept", "*/*");
      var2.putHeader("sec-fetch-site", "same-origin");
      var2.putHeader("sec-fetch-mode", "cors");
      var2.putHeader("sec-fetch-dest", "empty");
      var2.putHeader("referer", "https://www.yeezysupply.com/product/" + this.getSKU());
      var2.putHeader("accept-encoding", "gzip, deflate, br");
      var2.putHeader("accept-language", "en-US,en;q=0.9");
      return var2;
   }

   public CompletableFuture getTrickleSensor() {
      this.bmak.updateDocumentUrl("https://www.yeezysupply.com/product/" + this.getSKU());
      String var1 = "";

      try {
         var1 = this.getCookies().getCookieValue("_abck");
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      switch (this.sensorCounter++) {
         case 0:
            return CompletableFuture.completedFuture(this.bmak.genPageLoadSensor(var1));
         case 1:
            return CompletableFuture.completedFuture(this.bmak.genSecondSensor(var1));
         default:
            CompletableFuture var10000 = this.reInit();
            if (!var10000.isDone()) {
               CompletableFuture var3 = var10000;
               return var3.exceptionally(Function.identity()).thenCompose(YeezyAPI::async$getTrickleSensor);
            } else {
               var10000.join();
               this.sensorCounter = 1;
               return CompletableFuture.completedFuture(this.bmak.genPageLoadSensor(var1));
            }
      }
   }

   public void updateUserAgent(String var1) {
      this.userAgent = var1;

      try {
         ((NetClientImpl)((HttpClientImpl)this.client.getClient()).getNetClient()).getOptions().setConnectUserAgent(this.userAgent);
      } catch (Throwable var3) {
      }

   }

   public JsonObject couponForm(String var1) {
      return (new JsonObject()).put("couponCode", var1);
   }

   public HttpRequest submitShippingAndBillingSpecial(String var1, String var2) {
      int var3 = ThreadLocalRandom.current().nextInt(1, 6000);

      CookieJar$ExpirableCookie var5;
      for(Iterator var4 = this.getCookies().getAll().iterator(); var4.hasNext(); this.getCookies().put(var5.getWrappedCookie())) {
         var5 = (CookieJar$ExpirableCookie)var4.next();
         Cookie var6 = var5.getWrappedCookie();
         if (var6.domain() != null && !var6.domain().isBlank()) {
            var6.setDomain("a" + var3 + ".g.akamai.net");
         }
      }

      HttpRequest var7 = super.client.patchAbs("https://a" + var3 + ".g.akamai.net/api/checkout/baskets/" + var1).as(BodyCodec.string());
      String var8 = this.getInstanaID();
      var7.putHeader("host", "www.yeezysupply.com");
      var7.putHeader("x-instana-t", var8);
      var7.putHeader("content-length", "DEFAULT_VALUE");
      var7.putHeader("sec-ch-ua-mobile", "?0");
      var7.putHeader("user-agent", this.userAgent);
      var7.putHeader("x-instana-l", "1,correlationType=web;correlationId=" + var8);
      var7.putHeader("x-instana-s", var8);
      var7.putHeader("content-type", "application/json");
      var7.putHeader("checkout-authorization", var2);
      var7.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var7.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"95\", \"Chromium\";v=\"95\", \";Not A Brand\";v=\"99\"");
      var7.putHeader("accept", "*/*");
      var7.putHeader("origin", "https://www.yeezysupply.com");
      var7.putHeader("sec-fetch-site", "same-origin");
      var7.putHeader("sec-fetch-mode", "cors");
      var7.putHeader("sec-fetch-dest", "empty");
      var7.putHeader("referer", "https://www.yeezysupply.com/delivery");
      var7.putHeader("accept-encoding", "gzip, deflate, br");
      var7.putHeader("accept-language", "en-US,en;q=0.9");
      return var7;
   }

   public void lambda$enablePush$1(HttpContext var1) {
      if (var1.phase().equals(ClientPhase.SEND_REQUEST) && var1.clientRequest() != null) {
         var1.clientRequest().pushHandler(this::handlePush);
      }

      var1.next();
   }

   public HttpRequest adiAtc() {
      HttpRequest var1 = this.client.postAbs("https://www.adidas.com/api/chk/baskets/-/items?sitePath=us").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("x-instana-t", "c6953e7ece2d4c44");
      var1.putHeader("glassversion", "db136f4");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("x-instana-l", "1,correlationType=web;correlationId=c6953e7ece2d4c44");
      var1.putHeader("content-type", "application/json");
      var1.putHeader("x-sec-clge-req-type", "ajax");
      var1.putHeader("user-agent", this.userAgent);
      var1.putHeader("x-instana-s", "c6953e7ece2d4c44");
      var1.putHeader("checkout-authorization", "null");
      var1.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"95\", \"Chromium\";v=\"95\", \";Not A Brand\";v=\"99\"");
      var1.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var1.putHeader("accept", "*/*");
      var1.putHeader("origin", "https://www.adidas.com");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.adidas.com/us/ultraboost-21-shoes/FY0378.html");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      var1.putHeader("cookie", "DEFAULT_VALUE");
      return var1;
   }

   public Pixel getPixelAPI() {
      return this.pixelAPI;
   }

   public HttpRequest adiProductPage() {
      HttpRequest var1 = this.client.getAbs("https://www.adidas.com/us/ultraboost-21-shoes/FY0378.html").as(BodyCodec.buffer());
      var1.putHeader("cache-control", "max-age=0");
      var1.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"95\", \"Chromium\";v=\"95\", \";Not A Brand\";v=\"99\"");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("user-agent", this.userAgent);
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("sec-fetch-site", "none");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      var1.putHeader("cookie", "DEFAULT_VALUE");
      return var1;
   }

   public HttpRequest auth3DS(String var1) {
      HttpRequest var2 = this.client.postAbs(var1).as(BodyCodec.buffer());
      var2.putHeader("content-length", "DEFAULT_VALUE");
      var2.putHeader("cache-control", "no-cache");
      var2.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"90\", \"Chromium\";v=\"90\", \";Not A Brand\";v=\"99\"");
      var2.putHeader("sec-ch-ua-mobile", "?0");
      var2.putHeader("upgrade-insecure-requests", "1");
      var2.putHeader("origin", "https://www.yeezysupply.com");
      var2.putHeader("content-type", "application/x-www-form-urlencoded");
      var2.putHeader("user-agent", this.userAgent);
      var2.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var2.putHeader("sec-fetch-site", "cross-site");
      var2.putHeader("sec-fetch-mode", "navigate");
      var2.putHeader("sec-fetch-user", "?1");
      var2.putHeader("sec-fetch-dest", "document");
      var2.putHeader("referer", "https://www.yeezysupply.com/");
      var2.putHeader("accept-encoding", "gzip, deflate, br");
      var2.putHeader("accept-language", "en-US,en;q=0.9");
      var2.putHeader("pragma", "no-cache");
      return var2;
   }

   public JsonArray atcForm(JsonObject var1) {
      JsonObject var2 = new JsonObject();
      var2.put("product_id", this.getSKU());
      var2.put("product_variation_sku", var1.getString("sku"));
      var2.put("productId", var1.getString("sku"));
      var2.put("quantity", 1);
      var2.put("size", var1.getString("size"));
      var2.put("displaySize", var1.getString("size"));
      return (new JsonArray()).add(var2);
   }

   public HttpRequest addToCart() {
      HttpRequest var1 = super.client.postAbs("https://www.yeezysupply.com/api/checkout/baskets/-/items").as(BodyCodec.string());
      String var2 = this.getInstanaID();
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("x-instana-t", var2);
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("x-instana-l", "1,correlationType=web;correlationId=" + var2);
      var1.putHeader("content-type", "application/json");
      var1.putHeader("user-agent", this.userAgent);
      var1.putHeader("x-instana-s", var2);
      var1.putHeader("checkout-authorization", "null");
      var1.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var1.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"99\", \"Google Chrome\";v=\"99\"");
      var1.putHeader("accept", "*/*");
      var1.putHeader("origin", "https://www.yeezysupply.com");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.yeezysupply.com/product/" + this.getSKU());
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public HttpRequest homePage() {
      HttpRequest var1 = super.client.getAbs("https://www.yeezysupply.com/").as(BodyCodec.string());
      var1.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"99\", \"Google Chrome\";v=\"99\"");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("sec-ch-ua-platform", "\"Windows\"");
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

   public HttpRequest atcSpecial() {
      int var1 = ThreadLocalRandom.current().nextInt(1, 6000);

      CookieJar$ExpirableCookie var3;
      for(Iterator var2 = this.getCookies().getAll().iterator(); var2.hasNext(); this.getCookies().put(var3.getWrappedCookie())) {
         var3 = (CookieJar$ExpirableCookie)var2.next();
         Cookie var4 = var3.getWrappedCookie();
         if (var4.domain() != null && !var4.domain().isBlank()) {
            var4.setDomain("a" + var1 + ".g.akamai.net");
         }
      }

      HttpRequest var5 = super.client.postAbs("https://a" + var1 + ".g.akamai.net/api/checkout/baskets/-/items").as(BodyCodec.string());
      String var6 = this.getInstanaID();
      var5.putHeader("host", "www.yeezysupply.com");
      var5.putHeader("content-length", "DEFAULT_VALUE");
      var5.putHeader("x-instana-t", var6);
      var5.putHeader("sec-ch-ua-mobile", "?0");
      var5.putHeader("x-instana-l", "1,correlationType=web;correlationId=" + var6);
      var5.putHeader("content-type", "application/json");
      var5.putHeader("user-agent", this.userAgent);
      var5.putHeader("x-instana-s", var6);
      var5.putHeader("checkout-authorization", "null");
      var5.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var5.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"99\", \"Google Chrome\";v=\"99\"");
      var5.putHeader("accept", "*/*");
      var5.putHeader("origin", "https://www.yeezysupply.com");
      var5.putHeader("sec-fetch-site", "same-origin");
      var5.putHeader("sec-fetch-mode", "cors");
      var5.putHeader("sec-fetch-dest", "empty");
      var5.putHeader("referer", "https://www.yeezysupply.com/product/" + this.getSKU());
      var5.putHeader("accept-encoding", "gzip, deflate, br");
      var5.putHeader("accept-language", "en-US,en;q=0.9");
      return var5;
   }

   public HttpRequest emptyBasket(boolean var1, String var2) {
      HttpRequest var3 = super.client.getAbs("https://www.yeezysupply.com/api/checkout/customer/baskets").as(BodyCodec.none());
      var3.putHeader("user-agent", this.userAgent);
      var3.putHeader("content-type", "application/json");
      var3.putHeader("checkout-authorization", var2);
      var3.putHeader("accept", "*/*");
      var3.putHeader("sec-fetch-site", "same-origin");
      var3.putHeader("sec-fetch-mode", "cors");
      var3.putHeader("sec-fetch-dest", "empty");
      var3.putHeader("referer", var1 ? "https://www.yeezysupply.com/product/" + this.getSKU() : "https://www.yeezysupply.com/");
      var3.putHeader("accept-encoding", "gzip, deflate, br");
      var3.putHeader("accept-language", "en-US,en;q=0.9");
      return var3;
   }

   public Buffer atcFormAdidas() {
      JsonObject var1 = new JsonObject();
      var1.put("product_id", "FY0378");
      var1.put("quantity", 1);
      var1.put("product_variation_sku", "FY0378_610");
      var1.put("productId", "FY0378_610");
      var1.put("size", "8");
      var1.put("displaySize", "8");
      var1.put("specialLaunchProduct", false);
      return Buffer.buffer((new JsonArray()).add(var1).toString());
   }

   public String getInstanaID() {
      String var1 = "abcdef0123456789";
      StringBuilder var2 = new StringBuilder();

      while(var2.length() < 16) {
         int var3 = (int)(ThreadLocalRandom.current().nextFloat() * (float)"abcdef0123456789".length());
         var2.append("abcdef0123456789".charAt(var3));
      }

      return var2.toString();
   }

   public void setSensorUrl(String var1) {
      this.sensorUrl = var1;
   }

   public HttpRequest handleAfter3DS(String var1, String var2, String var3) {
      HttpRequest var4 = this.client.postAbs("https://www.yeezysupply.com/api/checkout/payment-verification/" + var1).as(BodyCodec.buffer());
      String var5 = this.getInstanaID();
      var4.putHeader("content-length", "DEFAULT_VALUE");
      var4.putHeader("x-instana-t", var5);
      var4.putHeader("sec-ch-ua-mobile", "?0");
      var4.putHeader("user-agent", this.userAgent);
      var4.putHeader("x-instana-l", "1,correlationType=web;correlationId=" + var5);
      var4.putHeader("x-instana-s", var5);
      var4.putHeader("content-type", "application/json");
      var4.putHeader("checkout-authorization", var2);
      var4.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"95\", \"Chromium\";v=\"95\", \";Not A Brand\";v=\"99\"");
      var4.putHeader("accept", "*/*");
      var4.putHeader("origin", "https://www.yeezysupply.com");
      var4.putHeader("sec-fetch-site", "same-origin");
      var4.putHeader("sec-fetch-mode", "cors");
      var4.putHeader("sec-fetch-dest", "empty");
      var4.putHeader("referer", var3);
      var4.putHeader("accept-encoding", "gzip, deflate, br");
      var4.putHeader("accept-language", "en-US,en;q=0.9");
      var4.putHeader("cookie", "DEFAULT_VALUE");
      return var4;
   }

   public HttpRequest postPixel(String var1) {
      HttpRequest var2 = super.client.postAbs(var1).as(BodyCodec.none());
      String var3 = this.getInstanaID();
      var2.putHeader("content-length", "DEFAULT_VALUE");
      var2.putHeader("x-instana-t", var3);
      var2.putHeader("sec-ch-ua-mobile", "?0");
      var2.putHeader("user-agent", this.userAgent);
      var2.putHeader("x-instana-l", "1,correlationType=web;correlationId=" + var3);
      var2.putHeader("x-instana-s", var3);
      var2.putHeader("content-type", "application/x-www-form-urlencoded");
      var2.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var2.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"99\", \"Google Chrome\";v=\"99\"");
      var2.putHeader("accept", "*/*");
      var2.putHeader("origin", "https://www.yeezysupply.com");
      var2.putHeader("sec-fetch-site", "same-origin");
      var2.putHeader("sec-fetch-mode", "cors");
      var2.putHeader("sec-fetch-dest", "empty");
      var2.putHeader("referer", "https://www.yeezysupply.com/");
      var2.putHeader("accept-encoding", "gzip, deflate, br");
      var2.putHeader("accept-language", "en-US,en;q=0.9");
      return var2;
   }

   public String getSKU() {
      return this.task.getKeywords()[0].trim();
   }

   public static CompletableFuture async$reInit(YeezyAPI param0, int param1, YeezyAPI param2, CompletableFuture param3, Throwable param4, int param5, Object param6) {
      // $FF: Couldn't be decompiled
   }

   public void setUtagProductName(String var1) {
      this.utag.setName(var1);
   }

   public HttpRequest queue() {
      HttpRequest var1 = this.client.getAbs(QUEUE_URL).as(BodyCodec.none());
      var1.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"99\", \"Google Chrome\";v=\"99\"");
      var1.putHeader("accept", "application/json, text/plain, */*");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", this.userAgent);
      var1.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.yeezysupply.com/product/" + this.getSKU());
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public void updateUtagUrl(String var1) {
      this.utag.updateDocumentUrl(var1);
   }

   public JsonArray atcForm() {
      Sizes$SizePair var1 = Sizes.getSize(this.task.getSize());
      JsonObject var2 = new JsonObject();
      var2.put("product_id", this.getSKU());
      String var10002 = this.getSKU();
      var2.put("product_variation_sku", var10002 + var1);
      var10002 = this.getSKU();
      var2.put("productId", var10002 + var1);
      var2.put("quantity", 1);
      var2.put("size", var1.sizeNum);
      var2.put("displaySize", var1.sizeNum);
      return (new JsonArray()).add(var2);
   }

   public boolean rotateProxy() {
      if (super.rotateProxy()) {
         this.enablePush();
         return true;
      } else {
         return false;
      }
   }

   public HttpRequest sharedHpl() {
      HttpRequest var1 = super.client.getAbs("https://www.yeezysupply.com/hpl/content/yeezy-supply/releases/" + this.getSKU() + "/shared.json").as(BodyCodec.string());
      var1.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"99\", \"Google Chrome\";v=\"99\"");
      var1.putHeader("accept", "application/json, text/plain, */*");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", this.userAgent);
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.yeezysupply.com/product/" + this.getSKU());
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public HttpRequest postSensorSpecial(boolean var1) {
      int var2 = ThreadLocalRandom.current().nextInt(1, 6000);

      CookieJar$ExpirableCookie var4;
      for(Iterator var3 = this.getCookies().getAll().iterator(); var3.hasNext(); this.getCookies().put(var4.getWrappedCookie())) {
         var4 = (CookieJar$ExpirableCookie)var3.next();
         Cookie var5 = var4.getWrappedCookie();
         if (var5.domain() != null && !var5.domain().isBlank()) {
            var5.setDomain("a" + var2 + ".g.akamai.net");
         }
      }

      HttpRequest var6 = super.client.postAbs("https://a" + var2 + ".g.akamai.net" + this.sensorUrl.replace("https://www.yeezysupply.com", "")).as(BodyCodec.buffer());
      String var7 = this.getInstanaID();
      var6.putHeader("host", "www.yeezysupply.com");
      var6.putHeader("content-length", "DEFAULT_VALUE");
      var6.putHeader("x-instana-t", var7);
      var6.putHeader("sec-ch-ua-mobile", "?0");
      var6.putHeader("user-agent", this.userAgent);
      var6.putHeader("x-instana-l", "1,correlationType=web;correlationId=" + var7);
      var6.putHeader("x-instana-s", var7);
      var6.putHeader("content-type", "text/plain;charset=UTF-8");
      var6.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var6.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"99\", \"Google Chrome\";v=\"99\"");
      var6.putHeader("accept", "*/*");
      var6.putHeader("origin", "https://www.yeezysupply.com");
      var6.putHeader("sec-fetch-site", "same-origin");
      var6.putHeader("sec-fetch-mode", "cors");
      var6.putHeader("sec-fetch-dest", "empty");
      var6.putHeader("referer", var1 ? "https://www.yeezysupply.com/products/" + this.getSKU() : "https://www.yeezysupply.com/");
      var6.putHeader("accept-encoding", "gzip, deflate, br");
      var6.putHeader("accept-language", "en-US,en;q=0.9");
      return var6;
   }

   public HttpRequest waitingRoomConfig() {
      HttpRequest var1 = super.client.getAbs("https://www.yeezysupply.com/hpl/content/yeezy-supply/config/US/waitingRoomConfig.json").as(BodyCodec.string());
      var1.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"99\", \"Google Chrome\";v=\"99\"");
      var1.putHeader("accept", "application/json, text/plain, */*");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", this.userAgent);
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.yeezysupply.com/product/" + this.getSKU());
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public CompletableFuture reInit() {
      int var1 = 0;

      while(var1++ < 350000) {
         try {
            if (this.task.getMode().contains("2")) {
               return CompletableFuture.completedFuture(true);
            }

            if (this.task.getMode().contains("3")) {
               return CompletableFuture.completedFuture(true);
            }

            CompletableFuture var10001 = VertxSingleton.INSTANCE.getLocalClient().fetchDeviceFromAPI("https://loudounchris.xyz/api/device/browserDeviceQueried.json?mobile=false&ak=true", this.userAgent);
            if (!var10001.isDone()) {
               CompletableFuture var4 = var10001;
               return var4.exceptionally(Function.identity()).thenCompose(YeezyAPI::async$reInit);
            }

            this.taskDevice = (JsonObject)var10001.join();
            if (this.taskDevice != null && !this.taskDevice.isEmpty()) {
               this.bmak = new Bmak(this.taskDevice);
               this.pixelAPI = new TrickleAPI(this.taskDevice);
               if (this.bmak != null && this.userAgent != null) {
                  return CompletableFuture.completedFuture(true);
               }
            }
         } catch (Throwable var5) {
            CompletableFuture var6 = VertxUtil.hardCodedSleep(15000L);
            if (!var6.isDone()) {
               CompletableFuture var3 = var6;
               return var3.exceptionally(Function.identity()).thenCompose(YeezyAPI::async$reInit);
            }

            var6.join();
         }
      }

      return CompletableFuture.completedFuture(false);
   }

   public HttpRequest submitCoupon(String var1, String var2) {
      HttpRequest var3 = this.client.postAbs("https://www.yeezysupply.com/api/checkout/baskets/" + var1 + "/coupons/").as(BodyCodec.buffer());
      String var4 = this.getInstanaID();
      var3.putHeader("content-length", "DEFAULT_VALUE");
      var3.putHeader("x-instana-t", var4);
      var3.putHeader("sec-ch-ua-mobile", "?0");
      var3.putHeader("x-instana-l", "1,correlationType=web;correlationId=" + var4);
      var3.putHeader("content-type", "application/json");
      var3.putHeader("user-agent", this.userAgent);
      var3.putHeader("x-instana-s", var4);
      var3.putHeader("checkout-authorization", var2);
      var3.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var3.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"95\", \"Chromium\";v=\"95\", \";Not A Brand\";v=\"99\"");
      var3.putHeader("accept", "*/*");
      var3.putHeader("origin", "https://www.yeezysupply.com");
      var3.putHeader("sec-fetch-site", "same-origin");
      var3.putHeader("sec-fetch-mode", "cors");
      var3.putHeader("sec-fetch-dest", "empty");
      var3.putHeader("referer", "https://www.yeezysupply.com/payment");
      var3.putHeader("accept-encoding", "gzip, deflate, br");
      var3.putHeader("accept-language", "en-US,en;q=0.9");
      return var3;
   }

   public HttpRequest getShippingMethod(String var1, String var2) {
      HttpRequest var3 = super.client.getAbs("https://www.yeezysupply.com/api/checkout/baskets/" + var1 + "/shipping_methods").as(BodyCodec.string());
      var3.putHeader("sec-ch-ua-mobile", "?0");
      var3.putHeader("user-agent", this.userAgent);
      var3.putHeader("content-type", "application/json");
      var3.putHeader("checkout-authorization", var2);
      var3.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"99\", \"Google Chrome\";v=\"99\"");
      var3.putHeader("accept", "*/*");
      var3.putHeader("sec-fetch-site", "same-origin");
      var3.putHeader("sec-fetch-mode", "cors");
      var3.putHeader("sec-fetch-dest", "empty");
      var3.putHeader("referer", "https://www.yeezysupply.com/delivery");
      var3.putHeader("accept-encoding", "gzip, deflate, br");
      var3.putHeader("accept-language", "en-US,en;q=0.9");
      return var3;
   }

   public HttpRequest abckFromStore(String var1, String var2) {
      return super.client.getAbs("https://sensor-store-oygn7nn37q-uc.a.run.app/store/get.json").addQueryParam("os", var1).addQueryParam("version", var2).as(BodyCodec.jsonObject());
   }

   public HttpRequest powPage(String var1) {
      HttpRequest var2 = this.client.getAbs(var1).as(BodyCodec.buffer());
      var2.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"95\", \"Chromium\";v=\"95\", \";Not A Brand\";v=\"99\"");
      var2.putHeader("sec-ch-ua-mobile", "?0");
      var2.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var2.putHeader("upgrade-insecure-requests", "1");
      var2.putHeader("user-agent", this.userAgent);
      var2.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var2.putHeader("sec-fetch-site", "same-origin");
      var2.putHeader("sec-fetch-mode", "navigate");
      var2.putHeader("sec-fetch-dest", "iframe");
      var2.putHeader("referer", "https://www.yeezysupply.com/product/" + this.getSKU());
      var2.putHeader("accept-encoding", "gzip, deflate, br");
      var2.putHeader("accept-language", "en-US,en;q=0.9");
      var2.putHeader("cookie", "DEFAULT_VALUE");
      return var2;
   }

   public HttpRequest processPaymentSpecial(String var1) {
      int var2 = ThreadLocalRandom.current().nextInt(1, 6000);

      CookieJar$ExpirableCookie var4;
      for(Iterator var3 = this.getCookies().getAll().iterator(); var3.hasNext(); this.getCookies().put(var4.getWrappedCookie())) {
         var4 = (CookieJar$ExpirableCookie)var3.next();
         Cookie var5 = var4.getWrappedCookie();
         if (var5.domain() != null && !var5.domain().isBlank()) {
            var5.setDomain("a" + var2 + ".g.akamai.net");
         }
      }

      HttpRequest var6 = super.client.postAbs("https://a" + var2 + ".g.akamai.net/api/checkout/orders").as(BodyCodec.string());
      String var7 = this.getInstanaID();
      var6.putHeader("host", "www.yeezysupply.com");
      var6.putHeader("content-length", "DEFAULT_VALUE");
      var6.putHeader("x-instana-t", var7);
      var6.putHeader("sec-ch-ua-mobile", "?0");
      var6.putHeader("user-agent", this.userAgent);
      var6.putHeader("x-instana-l", "1,correlationType=web;correlationId=" + var7);
      var6.putHeader("x-instana-s", var7);
      var6.putHeader("content-type", "application/json");
      var6.putHeader("checkout-authorization", var1);
      var6.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"95\", \"Chromium\";v=\"95\", \";Not A Brand\";v=\"99\"");
      var6.putHeader("accept", "*/*");
      var6.putHeader("origin", "https://www.yeezysupply.com");
      var6.putHeader("sec-fetch-site", "same-origin");
      var6.putHeader("sec-fetch-mode", "cors");
      var6.putHeader("sec-fetch-dest", "empty");
      var6.putHeader("referer", "https://www.yeezysupply.com/payment");
      var6.putHeader("accept-encoding", "gzip, deflate, br");
      var6.putHeader("accept-language", "en-US,en;q=0.9");
      return var6;
   }
}
