package io.trickle.task.antibot.impl.px.payload.captcha;

import io.netty.util.AsciiString;
import io.trickle.core.actor.TaskActor;
import io.trickle.task.antibot.impl.px.PerimeterX;
import io.trickle.task.sites.shopify.ShopifyAPI;
import io.trickle.util.Utils;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.trickle.webclient.ClientType;
import io.trickle.webclient.RealClient;
import io.trickle.webclient.TaskApiClient;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.impl.HttpRequestImpl;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import io.vertx.ext.web.codec.BodyCodec;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;

public class DesktopPXNEW extends PerimeterX {
   public static CharSequence ONE_VALUE = AsciiString.cached("1");
   public String sid;
   public String userAgent;
   public static CharSequence FP_VALUE = AsciiString.cached("_pxff_fp");
   public static String ENCODING = "gzip, deflate, br";
   public long waitTime;
   public static CharSequence CTS = AsciiString.cached("CTS");
   public static CharSequence VID_COOKIE = AsciiString.cached("_pxvid");
   public JsonArray performance;
   public static CharSequence CTS_VALUE = AsciiString.cached("pxcts");
   public static CharSequence RF_VALUE = AsciiString.cached("_pxff_rf");
   public static CharSequence PX3_VALUE = AsciiString.cached("_px3");
   public static CharSequence DEFAULT_SEC_UA = AsciiString.cached("\"Google Chrome\";v=\"95\", \"Chromium\";v=\"95\", \";Not A Brand\";v=\"99\"");
   public static CharSequence COMPLETION = AsciiString.cached("completionEpoch");
   public TaskApiClient delegate;
   public static CharSequence VID = AsciiString.cached("vid");
   public static String LANGUAGE = "en-US,en;q=0.9";
   public static CharSequence PXHD_VALUE = AsciiString.cached("_pxhd");
   public static CharSequence QUERY_MOBILE_PARAM = AsciiString.cached("mobile");
   public static CharSequence DEVICE = AsciiString.cached("device");
   public String uuid;
   public static String DEFAULT_DEVICE = "undefined";
   public static CharSequence PERFORMANCE = AsciiString.cached("performance");
   public String secUA;
   public static CharSequence DEFAULT_UA = AsciiString.cached("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.45 Safari/537.36");
   public static CharSequence PXDE_VALUE = AsciiString.cached("_pxde");
   public static CharSequence CFP_VALUE = AsciiString.cached("_pxff_cfp");
   public static CharSequence UUID = AsciiString.cached("uuid");
   public static CharSequence SID = AsciiString.cached("sid");
   public static CharSequence SEC_UA = AsciiString.cached("sec-ua");
   public String vid;
   public static CharSequence QUERY_PARAM = AsciiString.cached("captcha");
   public String deviceNumber = "undefined";

   public void reset() {
      this.secUA = null;
      this.userAgent = null;
      this.uuid = null;
      this.sid = null;
      this.vid = null;
      this.performance = null;
      this.waitTime = 0L;
      this.delegate.rotateProxy();
      this.delegate.getCookies().removeAnyMatch(PXHD_VALUE.toString());
      this.delegate.getCookies().removeAnyMatch(PX3_VALUE.toString());
      this.delegate.getCookies().removeAnyMatch(PXDE_VALUE.toString());
      this.delegate.getCookies().removeAnyMatch(VID_COOKIE.toString());
      this.delegate.getCookies().removeAnyMatch(CTS_VALUE.toString());
      this.deviceNumber = "undefined";
   }

   public String getDeviceSecUA() {
      return this.secUA == null ? DEFAULT_SEC_UA.toString() : this.secUA;
   }

   public boolean needsDevice() {
      return this.userAgent == null || this.secUA == null || this.deviceNumber.equals("undefined");
   }

   public CompletableFuture fetchFreshPxhd() {
      int var1 = 0;
      if (var1++ < 10) {
         CompletableFuture var3 = Request.send(this.homepage());
         if (!var3.isDone()) {
            CompletableFuture var2 = var3;
            return var2.exceptionally(Function.identity()).thenCompose(DesktopPXNEW::async$fetchFreshPxhd);
         } else {
            var3.join();
            return CompletableFuture.completedFuture((Object)null);
         }
      } else {
         return CompletableFuture.completedFuture((Object)null);
      }
   }

   public Supplier apiRequest(int var1, boolean var2) {
      switch (var1) {
         case -1:
            return this::lambda$apiRequest$0;
         case 0:
         default:
            return DesktopPXNEW::lambda$apiRequest$5;
         case 1:
            return this::lambda$apiRequest$1;
         case 2:
            return this::lambda$apiRequest$2;
         case 3:
            return this::lambda$apiRequest$3;
         case 4:
            return this::lambda$apiRequest$4;
      }
   }

   public static CompletableFuture async$initialise(DesktopPXNEW var0, DesktopPXNEW var1, CompletableFuture var2, int var3, Object var4) {
      DesktopPXNEW var10000;
      CompletableFuture var10001;
      switch (var3) {
         case 0:
            if (!var0.needsDevice()) {
               return CompletableFuture.completedFuture(true);
            }

            var10000 = var0;
            var10001 = var0.executeAPI("Initialising", var0.apiRequest(-1, false), (Object)null, HttpResponse::bodyAsString);
            if (!var10001.isDone()) {
               var2 = var10001;
               return var2.exceptionally(Function.identity()).thenCompose(DesktopPXNEW::async$initialise);
            }
            break;
         case 1:
            var10000 = var1;
            var10001 = var2;
            break;
         default:
            throw new IllegalArgumentException();
      }

      var10000.userAgent = (String)var10001.join();
      if (var0.logger.isDebugEnabled()) {
         var0.logger.debug("Fetched device: '{}' with UA: '{}'", var0.deviceNumber, var0.userAgent);
      }

      return CompletableFuture.completedFuture(true);
   }

   public CompletableFuture executeAPI(String var1, Supplier var2, Object var3, DesktopPXNEW$ResponseHandler var4) {
      return this._execute(var1, var2, var3, var4, true);
   }

   public static HttpRequest lambda$apiRequest$5() {
      return null;
   }

   public String getVid() {
      return this.vid;
   }

   public HttpRequest collectorReq() {
      HttpRequest var1 = this.delegate.getWebClient().postAbs("https://collector-pxu6b0qd2s.px-cloud.net/api/v2/collector").expect(ResponsePredicate.SC_OK).as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("sec-ch-ua", this.getDeviceSecUA());
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", this.getDeviceUA());
      var1.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var1.putHeader("content-type", "application/x-www-form-urlencoded");
      var1.putHeader("accept", "*/*");
      var1.putHeader("origin", "https://www.walmart.com");
      var1.putHeader("sec-fetch-site", "cross-site");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.walmart.com/");
      var1.putHeader("accept-encoding", this.getDeviceAcceptEncoding());
      var1.putHeader("accept-language", this.getDeviceLang());
      return var1;
   }

   public String getDeviceAcceptEncoding() {
      return "gzip, deflate, br";
   }

   public CompletableFuture initialise() {
      if (this.needsDevice()) {
         CompletableFuture var10001 = this.executeAPI("Initialising", this.apiRequest(-1, false), (Object)null, HttpResponse::bodyAsString);
         if (!var10001.isDone()) {
            CompletableFuture var2 = var10001;
            return var2.exceptionally(Function.identity()).thenCompose(DesktopPXNEW::async$initialise);
         }

         this.userAgent = (String)var10001.join();
         if (this.logger.isDebugEnabled()) {
            this.logger.debug("Fetched device: '{}' with UA: '{}'", this.deviceNumber, this.userAgent);
         }
      }

      return CompletableFuture.completedFuture(true);
   }

   public static CompletableFuture async$_execute(DesktopPXNEW param0, String param1, Supplier param2, Object param3, DesktopPXNEW$ResponseHandler param4, int param5, int param6, HttpRequest param7, CompletableFuture param8, HttpResponse param9, Throwable param10, int param11, Object param12) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$fetchFreshPxhd(DesktopPXNEW var0, int var1, CompletableFuture var2, int var3, Object var4) {
      CompletableFuture var10000;
      switch (var3) {
         case 0:
            byte var5 = 0;
            byte var6 = var5;
            var1 = var5 + 1;
            if (var6 >= 10) {
               return CompletableFuture.completedFuture((Object)null);
            }

            var10000 = Request.send(var0.homepage());
            if (!var10000.isDone()) {
               var2 = var10000;
               return var2.exceptionally(Function.identity()).thenCompose(DesktopPXNEW::async$fetchFreshPxhd);
            }
            break;
         case 1:
            var10000 = var2;
            break;
         default:
            throw new IllegalArgumentException();
      }

      var10000.join();
      return CompletableFuture.completedFuture((Object)null);
   }

   public String getDeviceLang() {
      return "en-US,en;q=0.9";
   }

   public boolean metaHandler(HttpResponse var1) {
      try {
         if (var1.statusCode() == 200) {
            MultiMap var2 = var1.headers();
            this.deviceNumber = var2.get(DEVICE);
            Objects.requireNonNull(this.deviceNumber);
            this.secUA = var2.get(SEC_UA);
            if (this.secUA == null) {
               String var3 = Utils.quickParseFirst(var1.bodyAsString(), ShopifyAPI.VER_PATTERN);
               if (var3 != null) {
                  this.secUA = "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"" + var3 + "\", \"Google Chrome\";v=\"" + var3 + "\"";
               } else {
                  this.secUA = "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"96\", \"Google Chrome\";v=\"96\"";
               }
            }

            Objects.requireNonNull(this.secUA);
            if (var2.contains(PERFORMANCE)) {
               this.uuid = var2.get(UUID);
               this.sid = var2.get(SID);
               this.vid = var2.get(VID);
               this.waitTime = Long.parseLong(var2.get(COMPLETION)) - System.currentTimeMillis();
               this.performance = new JsonArray(var2.get(PERFORMANCE));
               if (var2.contains(CTS)) {
                  this.delegate.getCookies().put(String.valueOf(CTS_VALUE), var2.get(CTS), ".walmart.com");
               }
            }

            return true;
         }
      } catch (Throwable var4) {
         this.logger.warn("Failed to parse meta: {}", var4.getMessage());
         if (this.logger.isDebugEnabled()) {
            var4.printStackTrace();
         }
      }

      return false;
   }

   public static CompletableFuture async$solve(DesktopPXNEW param0, int param1, CompletableFuture param2, Buffer param3, String param4, Buffer param5, Throwable param6, int param7, Object param8) {
      // $FF: Couldn't be decompiled
   }

   public HttpRequest lambda$apiRequest$2(boolean var1) {
      return this.defaultApiRequest("2", var1);
   }

   public HttpRequest lambda$apiRequest$3(boolean var1) {
      return this.defaultApiRequest("img", var1);
   }

   public HttpRequest lambda$apiRequest$4(boolean var1) {
      return this.defaultApiRequest("3", var1);
   }

   public String getDeviceUA() {
      return this.userAgent == null ? DEFAULT_UA.toString() : this.userAgent;
   }

   public void restartClient(RealClient var1) {
   }

   public HttpRequest lambda$apiRequest$1(boolean var1) {
      return this.defaultApiRequest("1", var1);
   }

   public CompletableFuture solveCaptcha(String var1, String var2, String var3) {
      this.uuid = var2;
      this.vid = var1;

      for(int var4 = 0; var4 <= ThreadLocalRandom.current().nextInt(2, 6); ++var4) {
         CompletableFuture var14;
         CompletableFuture var10000;
         try {
            this.performance = null;
            var10000 = this.executeAPI("API 1", this.apiRequest(1, true), this.buildRequestBody((String)null, (JsonArray)null), HttpResponse::body);
            if (!var10000.isDone()) {
               var14 = var10000;
               return var14.exceptionally(Function.identity()).thenCompose(DesktopPXNEW::async$solveCaptcha);
            }

            Buffer var5 = (Buffer)var10000.join();
            var10000 = VertxUtil.hardCodedSleep(this.waitTime);
            if (!var10000.isDone()) {
               var14 = var10000;
               return var14.exceptionally(Function.identity()).thenCompose(DesktopPXNEW::async$solveCaptcha);
            }

            var10000.join();
            var10000 = this.executePX("Sensor 1/4", this::bundleReq, var5, HttpResponse::bodyAsString);
            if (!var10000.isDone()) {
               var14 = var10000;
               return var14.exceptionally(Function.identity()).thenCompose(DesktopPXNEW::async$solveCaptcha);
            }

            String var6 = (String)var10000.join();
            var10000 = this.executeAPI("API 2", this.apiRequest(2, true), this.buildRequestBody(var6, this.performance), HttpResponse::body);
            if (!var10000.isDone()) {
               var14 = var10000;
               return var14.exceptionally(Function.identity()).thenCompose(DesktopPXNEW::async$solveCaptcha);
            }

            Buffer var7 = (Buffer)var10000.join();
            var10000 = VertxUtil.hardCodedSleep(this.waitTime);
            if (!var10000.isDone()) {
               var14 = var10000;
               return var14.exceptionally(Function.identity()).thenCompose(DesktopPXNEW::async$solveCaptcha);
            }

            var10000.join();
            var10000 = this.executePX("Sensor 2/4", this::bundleReq, var7, HttpResponse::bodyAsString);
            if (!var10000.isDone()) {
               var14 = var10000;
               return var14.exceptionally(Function.identity()).thenCompose(DesktopPXNEW::async$solveCaptcha);
            }

            String var8 = (String)var10000.join();
            var10000 = this.executeAPI("API 3", this.apiRequest(3, true), this.buildRequestBody(var6, this.performance), HttpResponse::body);
            if (!var10000.isDone()) {
               var14 = var10000;
               return var14.exceptionally(Function.identity()).thenCompose(DesktopPXNEW::async$solveCaptcha);
            }

            Buffer var9 = (Buffer)var10000.join();
            var10000 = VertxUtil.hardCodedSleep(this.waitTime);
            if (!var10000.isDone()) {
               var14 = var10000;
               return var14.exceptionally(Function.identity()).thenCompose(DesktopPXNEW::async$solveCaptcha);
            }

            var10000.join();
            var10000 = this.executePX("Sensor 3/4", this::lambda$solveCaptcha$6, (Object)null, HttpResponse::bodyAsString);
            if (!var10000.isDone()) {
               var14 = var10000;
               return var14.exceptionally(Function.identity()).thenCompose(DesktopPXNEW::async$solveCaptcha);
            }

            String var10 = (String)var10000.join();
            var10000 = this.executeAPI("API 4", this.apiRequest(4, true), this.buildRequestBody(var6, this.performance), HttpResponse::body);
            if (!var10000.isDone()) {
               var14 = var10000;
               return var14.exceptionally(Function.identity()).thenCompose(DesktopPXNEW::async$solveCaptcha);
            }

            Buffer var11 = (Buffer)var10000.join();
            var10000 = VertxUtil.hardCodedSleep(this.waitTime);
            if (!var10000.isDone()) {
               var14 = var10000;
               return var14.exceptionally(Function.identity()).thenCompose(DesktopPXNEW::async$solveCaptcha);
            }

            var10000.join();
            var10000 = this.executePX("Sensor 4/4", this::bundleReq, var11, HttpResponse::bodyAsString);
            if (!var10000.isDone()) {
               var14 = var10000;
               return var14.exceptionally(Function.identity()).thenCompose(DesktopPXNEW::async$solveCaptcha);
            }

            String var12 = (String)var10000.join();
            if (var12 != null && var12.contains("cv|0")) {
               this.logger.info("Successfully solved captcha!");
               MultiMap var13 = MultiMap.caseInsensitiveMultiMap();
               this.parseResultCookies(var12, var13);
               if (this.getVid() != null && !this.getVid().isBlank()) {
                  var13.add(VID_COOKIE, this.getVid());
               }

               var13.add(RF_VALUE, ONE_VALUE);
               var13.add(FP_VALUE, ONE_VALUE);
               var13.add(CFP_VALUE, ONE_VALUE);
               if (this.logger.isDebugEnabled()) {
                  this.logger.debug("Parsed cookies from captcha solve: {}", var13);
               }

               return CompletableFuture.completedFuture(var13);
            }

            if (var4 % 2 == 1) {
               this.reset();
               var10000 = this.initialise();
               if (!var10000.isDone()) {
                  var14 = var10000;
                  return var14.exceptionally(Function.identity()).thenCompose(DesktopPXNEW::async$solveCaptcha);
               }

               var10000.join();
               var10000 = this.fetchFreshPxhd();
               if (!var10000.isDone()) {
                  var14 = var10000;
                  return var14.exceptionally(Function.identity()).thenCompose(DesktopPXNEW::async$solveCaptcha);
               }

               var10000.join();
            }

            this.sid = null;
            var10000 = VertxUtil.randomSleep(1000L);
            if (!var10000.isDone()) {
               var14 = var10000;
               return var14.exceptionally(Function.identity()).thenCompose(DesktopPXNEW::async$solveCaptcha);
            }

            var10000.join();
         } catch (Throwable var15) {
            this.logger.warn("Error solving(2) sensor: {}. Retrying...", var15.getMessage());
            if (this.logger.isDebugEnabled()) {
               var15.printStackTrace();
            }

            var10000 = VertxUtil.randomSleep(5000L);
            if (!var10000.isDone()) {
               var14 = var10000;
               return var14.exceptionally(Function.identity()).thenCompose(DesktopPXNEW::async$solveCaptcha);
            }

            var10000.join();
         }
      }

      this.delegate.getCookies().removeAnyMatch(PXHD_VALUE.toString());
      return CompletableFuture.completedFuture(MultiMap.caseInsensitiveMultiMap());
   }

   public void resetRetainDevice() {
      this.uuid = null;
      this.sid = null;
      this.vid = null;
      this.performance = null;
      this.waitTime = 0L;
      this.delegate.rotateProxy();
      this.delegate.getCookies().removeAnyMatch(PXHD_VALUE.toString());
      this.delegate.getCookies().removeAnyMatch(PX3_VALUE.toString());
      this.delegate.getCookies().removeAnyMatch(PXDE_VALUE.toString());
      this.delegate.getCookies().removeAnyMatch(VID_COOKIE.toString());
      this.delegate.getCookies().removeAnyMatch(CTS_VALUE.toString());
   }

   public HttpRequest homepage() {
      HttpRequest var1 = this.delegate.getWebClient().getAbs("https://www.walmart.com/").as(BodyCodec.buffer());
      var1.putHeader("sec-ch-ua", this.getDeviceSecUA());
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("sec-ch-ua-platform", "\"macOS\"");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("user-agent", this.getDeviceUA());
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("service-worker-navigation-preload", "true");
      var1.putHeader("sec-fetch-site", "none");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("accept-encoding", this.getDeviceAcceptEncoding());
      var1.putHeader("accept-language", this.getDeviceLang());
      return var1;
   }

   public void parseResultCookies(String var1, MultiMap var2) {
      try {
         Matcher var3 = PXDE_PATTERN.matcher(var1);
         Matcher var4 = BAKE_PATTERN.matcher(var1);
         if (var4.find()) {
            var2.add(PX3_VALUE, var4.group(1));
         }

         if (var3.find()) {
            var2.add(PXDE_VALUE, var3.group(1));
         }
      } catch (Throwable var5) {
         if (this.logger.isDebugEnabled()) {
            this.logger.warn("Failed to find px3 or pxde");
            var5.printStackTrace();
         }
      }

   }

   public HttpRequest lambda$solveCaptcha$6(Buffer var1) {
      return this.imageReq(var1);
   }

   public HttpRequest lambda$apiRequest$0() {
      return this.client.getAbs(getApiURI("ua"));
   }

   public CompletableFuture _execute(String var1, Supplier var2, Object var3, DesktopPXNEW$ResponseHandler var4, boolean var5) {
      int var6 = 0;

      while(var6++ < 10) {
         CompletableFuture var11;
         CompletableFuture var13;
         try {
            HttpRequest var7 = (HttpRequest)var2.get();
            HttpResponse var8;
            if (((HttpRequestImpl)var7).method().equals(HttpMethod.POST)) {
               var13 = Request.send(var7, var3);
               if (!var13.isDone()) {
                  var11 = var13;
                  return var11.exceptionally(Function.identity()).thenCompose(DesktopPXNEW::async$_execute);
               }

               var8 = (HttpResponse)var13.join();
            } else {
               var13 = Request.send(var7);
               if (!var13.isDone()) {
                  var11 = var13;
                  return var11.exceptionally(Function.identity()).thenCompose(DesktopPXNEW::async$_execute);
               }

               var8 = (HttpResponse)var13.join();
            }

            if (var8 != null) {
               Optional var9 = var5 != 0 ? (this.metaHandler(var8) ? Optional.ofNullable(var4.handle(var8)) : Optional.empty()) : Optional.ofNullable(var4.handle(var8));
               if (var9.isPresent()) {
                  return CompletableFuture.completedFuture(var9.get());
               }

               int var10 = var8.statusCode();
               this.logger.warn("Failed {}: '{}'", var1.toLowerCase(Locale.ROOT), var10);
            } else {
               this.logger.error("Failed to execute: {}", var1.toLowerCase(Locale.ROOT));
            }

            var13 = VertxUtil.randomSleep(5000L);
            if (!var13.isDone()) {
               var11 = var13;
               return var11.exceptionally(Function.identity()).thenCompose(DesktopPXNEW::async$_execute);
            }

            var13.join();
         } catch (Throwable var12) {
            this.logger.error("Error " + var1.toLowerCase(Locale.ROOT) + ": {}", var12.getMessage());
            if (this.logger.isDebugEnabled()) {
               var12.printStackTrace();
            }

            var13 = VertxUtil.randomSleep(5000L);
            if (!var13.isDone()) {
               var11 = var13;
               return var11.exceptionally(Function.identity()).thenCompose(DesktopPXNEW::async$_execute);
            }

            var13.join();
         }
      }

      return CompletableFuture.failedFuture(new Exception("Failed to execute " + var1));
   }

   public static CompletableFuture async$solveCaptcha(DesktopPXNEW param0, String param1, String param2, String param3, int param4, CompletableFuture param5, Buffer param6, String param7, Buffer param8, String param9, Buffer param10, String param11, Buffer param12, String param13, Throwable param14, int param15, Object param16) {
      // $FF: Couldn't be decompiled
   }

   public HttpRequest defaultApiRequest(String var1, boolean var2) {
      HttpRequest var3 = this.client.postAbs(getApiURI(var1)).addQueryParam("mobile", "false");
      if (var2) {
         var3.addQueryParam("captcha", "hold");
      }

      return var3;
   }

   public DesktopPXNEW(TaskActor var1) {
      super(var1, (ClientType)null);
      this.delegate = var1.getClient();
   }

   public CompletableFuture solve() {
      int var1 = 0;

      while(var1 < 10) {
         CompletableFuture var10000;
         CompletableFuture var7;
         try {
            if (this.needsDevice()) {
               var10000 = this.initialise();
               if (!var10000.isDone()) {
                  var7 = var10000;
                  return var7.exceptionally(Function.identity()).thenCompose(DesktopPXNEW::async$solve);
               }

               var10000.join();
            }

            var10000 = this.executeAPI("API 1", this.apiRequest(1, false), this.buildRequestBody((String)null, (JsonArray)null), HttpResponse::body);
            if (!var10000.isDone()) {
               var7 = var10000;
               return var7.exceptionally(Function.identity()).thenCompose(DesktopPXNEW::async$solve);
            }

            Buffer var2 = (Buffer)var10000.join();
            var10000 = VertxUtil.hardCodedSleep(this.waitTime);
            if (!var10000.isDone()) {
               var7 = var10000;
               return var7.exceptionally(Function.identity()).thenCompose(DesktopPXNEW::async$solve);
            }

            var10000.join();
            var10000 = this.executePX("Sensor 1/2", this::collectorReq, var2, HttpResponse::bodyAsString);
            if (!var10000.isDone()) {
               var7 = var10000;
               return var7.exceptionally(Function.identity()).thenCompose(DesktopPXNEW::async$solve);
            }

            String var3 = (String)var10000.join();
            var10000 = this.executeAPI("API 2", this.apiRequest(2, false), this.buildRequestBody(var3, this.performance), HttpResponse::body);
            if (!var10000.isDone()) {
               var7 = var10000;
               return var7.exceptionally(Function.identity()).thenCompose(DesktopPXNEW::async$solve);
            }

            Buffer var4 = (Buffer)var10000.join();
            var10000 = VertxUtil.hardCodedSleep(this.waitTime);
            if (!var10000.isDone()) {
               var7 = var10000;
               return var7.exceptionally(Function.identity()).thenCompose(DesktopPXNEW::async$solve);
            }

            var10000.join();
            var10000 = this.executePX("Sensor 2/2", this::collectorReq, var4, HttpResponse::bodyAsString);
            if (!var10000.isDone()) {
               var7 = var10000;
               return var7.exceptionally(Function.identity()).thenCompose(DesktopPXNEW::async$solve);
            }

            String var5 = (String)var10000.join();
            MultiMap var6 = MultiMap.caseInsensitiveMultiMap();
            this.parseResultCookies(var5, var6);
            if (var6.isEmpty()) {
               return CompletableFuture.completedFuture(var6);
            }

            if (this.getVid() != null && !this.getVid().isBlank()) {
               var6.add(VID_COOKIE, this.getVid());
            }

            var6.add(RF_VALUE, ONE_VALUE);
            var6.add(FP_VALUE, ONE_VALUE);
            var6.add(CFP_VALUE, ONE_VALUE);
            if (this.logger.isDebugEnabled()) {
               this.logger.debug("Parsed cookies from normal solve: {}", var6);
            }

            return CompletableFuture.completedFuture(var6);
         } catch (Throwable var8) {
            this.logger.warn("Error solving(1) sensor: {}. Retrying...", var8.getMessage());
            if (this.logger.isDebugEnabled()) {
               var8.printStackTrace();
            }

            var10000 = VertxUtil.randomSleep(5000L);
            if (!var10000.isDone()) {
               var7 = var10000;
               return var7.exceptionally(Function.identity()).thenCompose(DesktopPXNEW::async$solve);
            }

            var10000.join();
            ++var1;
         }
      }

      return CompletableFuture.completedFuture(MultiMap.caseInsensitiveMultiMap());
   }

   public static String getApiURI(String var0) {
      boolean var1 = false;
      return String.format(var1 ? "http://localhost:8080/gen/%s.json" : "https://trickle-px-oygn7nn37q-uc.a.run.app/gen/%s.json", var0);
   }

   public HttpRequest imageReq(Buffer var1) {
      HttpRequest var2 = this.delegate.getWebClient().getAbs("https://collector-pxu6b0qd2s.px-client.net/b/g?" + var1.toString()).expect(ResponsePredicate.SC_OK).as(BodyCodec.buffer());
      var2.putHeader("sec-ch-ua", this.getDeviceSecUA());
      var2.putHeader("sec-ch-ua-mobile", "?0");
      var2.putHeader("user-agent", this.getDeviceUA());
      var2.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var2.putHeader("accept", "*/*");
      var2.putHeader("origin", "https://www.walmart.com");
      var2.putHeader("sec-fetch-site", "cross-site");
      var2.putHeader("sec-fetch-mode", "cors");
      var2.putHeader("sec-fetch-dest", "empty");
      var2.putHeader("referer", "https://www.walmart.com/");
      var2.putHeader("accept-encoding", this.getDeviceAcceptEncoding());
      var2.putHeader("accept-language", this.getDeviceLang());
      return var2;
   }

   public CompletableFuture executePX(String var1, Supplier var2, Object var3, DesktopPXNEW$ResponseHandler var4) {
      return this._execute(var1, var2, var3, var4, false);
   }

   public JsonObject buildRequestBody(String var1, JsonArray var2) {
      JsonObject var3 = new JsonObject();
      var3.put("pxhd", this.delegate.getCookies().getCookieValue("_pxhd"));
      var3.put("uuid", this.uuid);
      var3.put("sid", this.sid);
      var3.put("vid", this.vid);
      var3.put("firstResponse", var1);
      var3.put("performance", var2);
      var3.put("cts", this.delegate.getCookies().getCookieValue(String.valueOf(CTS_VALUE)));
      var3.put("device", this.deviceNumber);
      return var3;
   }

   public HttpRequest bundleReq() {
      HttpRequest var1 = this.delegate.getWebClient().postAbs("https://collector-pxu6b0qd2s.px-cloud.net/assets/js/bundle").expect(ResponsePredicate.SC_OK).as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("sec-ch-ua", this.getDeviceSecUA());
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", this.getDeviceUA());
      var1.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var1.putHeader("content-type", "application/x-www-form-urlencoded");
      var1.putHeader("accept", "*/*");
      var1.putHeader("origin", "https://www.walmart.com");
      var1.putHeader("sec-fetch-site", "cross-site");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.walmart.com/");
      var1.putHeader("accept-encoding", this.getDeviceAcceptEncoding());
      var1.putHeader("accept-language", this.getDeviceLang());
      return var1;
   }
}
