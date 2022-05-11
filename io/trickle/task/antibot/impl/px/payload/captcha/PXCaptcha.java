package io.trickle.task.antibot.impl.px.payload.captcha;

import io.trickle.task.antibot.impl.px.Types;
import io.trickle.task.antibot.impl.px.payload.ExtendedPayload;
import io.trickle.task.antibot.impl.px.payload.captcha.util.DeviceHeaderParsers;
import io.trickle.task.sites.Site;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Headers$Pseudo;
import io.trickle.util.request.Request;
import io.trickle.webclient.ClientType;
import io.trickle.webclient.RealClient;
import io.trickle.webclient.RealClientFactory;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.codec.BodyCodec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.Logger;
import org.conscrypt.Conscrypt;

public class PXCaptcha {
   public String parentUUID;
   public boolean mobile;
   public static Pattern BAKE_PATTERN = Pattern.compile("bake\\|.*?\\|.*?\\|(.*?)\\|");
   public static Pattern WCS_PATTERN = Pattern.compile("wcs\\|([0-z]*?)\"");
   public Devices$DeviceImpl device;
   public Types type;
   public long vidAge;
   public Site SITE;
   public static Pattern SID_UUID_PATTERN = Pattern.compile("sid\\|(.*?)\"");
   public static Pattern CI_PATTERN = Pattern.compile("ci\\|.*?\\|.*?\\|([0-9]*?)\\|");
   public boolean isFirstTry = true;
   public Logger logger;
   public static Pattern VID_UUID_PATTERN = Pattern.compile("vid\\|(.*?)\\|");
   public String sid;
   public static Pattern CP_PATTERN = Pattern.compile("cp\\|.*?\\|(.*?)\"");
   public static Pattern STS_PATTERN = Pattern.compile("sts\\|([0-z]*?)\"");
   public static Pattern CI_UUID_PATTERN = Pattern.compile("ci\\|.*?\\|(.*?)\\|");
   public int failedSolves = 0;
   public static Pattern DRC_PATTERN = Pattern.compile("drc\\|([0-9]*?)\"");
   public String referer;
   public RealClient client;
   public boolean needsDesktopAppID;
   public static Pattern CTS_PATTERN = Pattern.compile("cts\\|([\\--z]*)");
   public static Pattern CLS_PATTERN = Pattern.compile("cls\\|(.*?)\"");
   public static Pattern CI_TOKEN_PATTERN = Pattern.compile("ci\\|.*?\\|.*?\\|.*?\\|(.*?)\"");
   public String parentVID;
   public static Pattern DOLLAR_SCRIPT_VAL_PATTERN = Pattern.compile("(\\$[0-9][0-9]*)");
   public static Pattern PXDE_PATTERN = Pattern.compile("_pxde\\|330\\|(.*?)\\|");
   public static Pattern SFF_SCS_PATTERN = Pattern.compile("sff\\|scs\\|300\\|1,(.*?)\"");
   public String origin;
   public static Pattern CS_PATTERN = Pattern.compile("cs\\|([0-z]*?)\"");

   public Devices$DeviceImpl getDevice() {
      return this.device;
   }

   public static CompletableFuture async$setBrowserType(PXCaptcha var0, PXCaptcha var1, CompletableFuture var2, int var3, Object var4) {
      PXCaptcha var10000;
      Devices$DeviceImpl var5;
      label33: {
         CompletableFuture var10001;
         label32: {
            switch (var3) {
               case 0:
                  if (var0.device != null) {
                     return CompletableFuture.completedFuture((Object)null);
                  }

                  var0.mobile = false;
                  var10000 = var0;
                  if (var0.mobile) {
                     var10001 = Devices.randomMobileBrowser();
                     if (!var10001.isDone()) {
                        var2 = var10001;
                        return var2.exceptionally(Function.identity()).thenCompose(PXCaptcha::async$setBrowserType);
                     }
                     break label32;
                  }

                  var10001 = Devices.randomDesktopBrowser();
                  if (!var10001.isDone()) {
                     var2 = var10001;
                     return var2.exceptionally(Function.identity()).thenCompose(PXCaptcha::async$setBrowserType);
                  }
                  break;
               case 1:
                  var10000 = var1;
                  var10001 = var2;
                  break label32;
               case 2:
                  var10000 = var1;
                  var10001 = var2;
                  break;
               default:
                  throw new IllegalArgumentException();
            }

            var5 = (Devices$DeviceImpl)var10001.join();
            break label33;
         }

         var5 = (Devices$DeviceImpl)var10001.join();
      }

      var10000.device = var5;
      return CompletableFuture.completedFuture((Object)null);
   }

   public static String getSIDUUID(JsonObject var0) {
      return parseDO(var0, SID_UUID_PATTERN);
   }

   public HttpRequest getCaptchaJS(String var1, String var2) {
      // $FF: Couldn't be decompiled
   }

   public static String getCI(JsonObject var0) {
      return parseDO(var0, CI_PATTERN);
   }

   public void setChromeHeaders(HttpRequest var1) {
      var1.putHeader("content-length", "DEFAULT_VALUE");
      if (this.device.getSecUA() != null && this.device.getSecUAMobile() != null) {
         var1.putHeader("sec-ch-ua", this.device.getSecUA());
         var1.putHeader("sec-ch-ua-mobile", this.device.getSecUAMobile());
      }

      var1.putHeader("user-agent", this.device.getUserAgent());
      var1.putHeader("content-type", "application/x-www-form-urlencoded");
      var1.putHeader("accept", "*/*");
      var1.putHeader("origin", this.origin);
      var1.putHeader("sec-fetch-site", "cross-site");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", this.origin + "/");
      var1.putHeader("accept-encoding", this.device.getAcceptEncoding() == null ? "gzip, deflate, br" : this.device.getAcceptEncoding());
      var1.putHeader("accept-language", this.device.getAcceptLanguage());
   }

   public static void lambda$main$2(RealClient var0, HttpServerRequest var1) {
      var1.body(PXCaptcha::lambda$main$1);
   }

   public void setWebviewHeaders(HttpRequest var1) {
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("user-agent", this.device.getUserAgent());
      var1.putHeader("content-type", "application/x-www-form-urlencoded");
      var1.putHeader("accept", "*/*");
      var1.putHeader("origin", this.origin);
      var1.putHeader("x-requested-with", this.getPackageName());
      var1.putHeader("sec-fetch-site", "cross-site");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", this.origin + "/");
      var1.putHeader("accept-encoding", this.device.getAcceptEncoding() == null ? "gzip, deflate" : this.device.getAcceptEncoding());
      var1.putHeader("accept-language", this.device.getAcceptLanguage());
   }

   public static void lambda$sendPayload$5(StringBuilder var0, Map.Entry var1) {
      if (var0.length() > 0) {
         var0.append("&");
      }

      var0.append((String)var1.getKey()).append("=").append((String)var1.getValue());
   }

   public CompletableFuture setBrowserType() {
      if (this.device == null) {
         this.mobile = false;
         CompletableFuture var10001;
         CompletableFuture var2;
         Devices$DeviceImpl var3;
         if (this.mobile) {
            var10001 = Devices.randomMobileBrowser();
            if (!var10001.isDone()) {
               var2 = var10001;
               return var2.exceptionally(Function.identity()).thenCompose(PXCaptcha::async$setBrowserType);
            }

            var3 = (Devices$DeviceImpl)var10001.join();
         } else {
            var10001 = Devices.randomDesktopBrowser();
            if (!var10001.isDone()) {
               var2 = var10001;
               return var2.exceptionally(Function.identity()).thenCompose(PXCaptcha::async$setBrowserType);
            }

            var3 = (Devices$DeviceImpl)var10001.join();
         }

         this.device = var3;
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static HttpRequest bundleReq(String var0, RealClient var1) {
      HttpRequest var2 = var1.postAbs("https://collector-" + "PX9Qx3Rve4".toLowerCase() + ".px-cloud.net/assets/js/bundle").timeout(TimeUnit.SECONDS.toMillis(15L)).as(BodyCodec.jsonObject());
      var2.putHeaders(Headers$Pseudo.MASP.get());
      var2.putHeader("content-length", "DEFAULT_VALUE");
      var2.putHeader("user-agent", var0);
      var2.putHeader("content-type", "application/x-www-form-urlencoded");
      var2.putHeader("accept", "*/*");
      var2.putHeader("origin", "https://www.perimeterx.com");
      var2.putHeader("sec-fetch-site", "cross-site");
      var2.putHeader("sec-fetch-mode", "cors");
      var2.putHeader("sec-fetch-dest", "empty");
      var2.putHeader("referer", "https://www.perimeterx.com/");
      var2.putHeader("accept-encoding", "gzip, deflate, br");
      var2.putHeader("accept-language", "en-US,en;q=0.9");
      return var2;
   }

   public static String getWCS(JsonObject var0) {
      return parseDO(var0, WCS_PATTERN);
   }

   public static Future sendPayload(HttpServerRequest var0, Buffer var1, RealClient var2) {
      Promise var3 = Promise.promise();
      HttpRequest var4 = atc(var0, var2);
      var4.sendBuffer(var1, PXCaptcha::lambda$sendPayload$3);
      return var3.future();
   }

   public static String parseCS(JsonObject var0, Pattern var1) {
      String var2 = var0.getJsonArray("do").toString().replace("wcs", "");
      Matcher var3 = var1.matcher(var2);
      var3.find();
      return var3.group(1);
   }

   public HttpRequest getScriptRequest(String var1) {
      HttpRequest var2 = this.client.getAbs(var1).as(BodyCodec.buffer());
      this.setChromeScriptHeaders(var2);
      return var2;
   }

   public static void main(String[] var0) {
      String var1 = ExtendedPayload.decode("aUkQRhAIEGJqABAeEFYQCEkQYmoLBBAIEFpGRkJBCB0dRUVFHEJXQFtfV0ZXQEocUV1fHRAeEGJqBAEQCBB/U1F7XEZXXhAeEGJqAwsDEAgCHhBiagoHAhAIAh4QYmoKBwMQCAMKBgEeEGJqAwICChAIAQQCAh4QYmoDAgcHEAgDBAAHAwUFAQALAgYEHhBiagMCBwQQCAMEAAcDBQUBAAsCBgoeEGJqAwIBChAIEFcLBQIAAFYCH1ZTUAofAwNXUB8KA1NUHwoFCgBUBVYBBlEFBBAeEGJqAQUDEAhUU15BVx4QYmoABwIQCBBiagcEAhAeEGJqBQIKEAgQQkpaURBPT28=", 50);
      System.setProperty("vertx.disableHttpHeadersValidation", "true");
      Conscrypt.setUseEngineSocketByDefault(false);
      Security.insertProviderAt(Conscrypt.newProvider(), 1);
      Conscrypt.setUseEngineSocketByDefault(false);
      Vertx var2 = Vertx.vertx();
      HttpServer var3 = var2.createHttpServer();
      RealClient var4 = RealClientFactory.buildProxied(var2, ClientType.CHROME);
      var3.requestHandler(PXCaptcha::lambda$main$2);
      var3.listen(8890);
   }

   public void setNeedsDesktopAppID(boolean var1) {
      this.needsDesktopAppID = var1;
   }

   public static void lambda$main$1(HttpServerRequest var0, RealClient var1, AsyncResult var2) {
      if (var2.succeeded()) {
         sendPayload(var0, (Buffer)var2.result(), var1).onSuccess(PXCaptcha::lambda$main$0);
      }

   }

   public HttpRequest collectorReq() {
      HttpRequest var1 = this.client.postAbs("https://collector-" + "PXu6b0qd2S".toLowerCase() + ".px-cloud.net/api/v2/collector").timeout(TimeUnit.SECONDS.toMillis(15L)).as(BodyCodec.jsonObject());
      this.setChromeHeaders(var1);
      return var1;
   }

   public void close() {
   }

   public static String getSID(ExtendedPayload var0, JsonObject var1) {
      return var1.toString().contains("sid|") ? getSIDUUID(var1) : var0.getSID();
   }

   public void reset() {
      this.parentVID = null;
      this.parentUUID = null;
   }

   public static CompletableFuture async$sendPayload(PXCaptcha param0, ExtendedPayload param1, HttpRequest param2, MultiMap param3, StringBuilder param4, Buffer param5, CompletableFuture param6, HttpResponse param7, int param8, Object param9) {
      // $FF: Couldn't be decompiled
   }

   public String getSiteID() {
      // $FF: Couldn't be decompiled
   }

   public HttpRequest imageReq(MultiMap var1) {
      String var2 = this.getSiteID();
      String var3 = "https://collector-" + var2 + ".perimeterx.net/b/g?";
      StringBuilder var4 = new StringBuilder();
      var1.forEach(PXCaptcha::lambda$imageReq$4);
      var3 = var3.concat(var4.toString().trim());
      HttpRequest var5 = this.client.getAbs(var3).timeout(TimeUnit.SECONDS.toMillis(15L)).as(BodyCodec.buffer());
      this.setChromeHeaders(var5);
      return var5;
   }

   public static String getCLS(JsonObject var0) {
      return parseDO(var0, CLS_PATTERN);
   }

   public void setSafariHeaders(HttpRequest var1) {
      var1.putHeaders(Headers$Pseudo.MSPA.get());
      var1.putHeader("accept", "*/*");
      var1.putHeader("content-type", "application/x-www-form-urlencoded");
      var1.putHeader("origin", this.origin);
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("accept-language", this.device.getAcceptLanguage());
      var1.putHeader("user-agent", this.device.getUserAgent());
      var1.putHeader("referer", this.origin + "/");
      var1.putHeader("accept-encoding", this.device.getAcceptEncoding() == null ? "gzip, deflate, br" : this.device.getAcceptEncoding());
   }

   public String getParentUUID() {
      return this.parentUUID;
   }

   public static String getDRC(JsonObject var0) {
      return parseDO(var0, DRC_PATTERN);
   }

   public Optional parseResult(String var1) {
      if (!this.type.equals(Types.DESKTOP) && !var1.contains("cv|0")) {
         return Optional.empty();
      } else {
         Matcher var2 = BAKE_PATTERN.matcher(var1);
         return var2.find() ? Optional.of("3:" + var2.group(1)) : Optional.empty();
      }
   }

   public void setSafariImageHeaders(HttpRequest var1) {
      var1.putHeaders(Headers$Pseudo.MSPA.get());
      var1.putHeader("accept", "*/*");
      var1.putHeader("origin", this.origin);
      var1.putHeader("accept-encoding", this.device.getAcceptEncoding() == null ? "gzip, deflate, br" : this.device.getAcceptEncoding());
      var1.putHeader("user-agent", this.device.getUserAgent());
      var1.putHeader("accept-language", this.device.getAcceptLanguage());
      var1.putHeader("referer", this.origin + "/");
   }

   public static String parseDO(JsonObject var0, Pattern var1) {
      String var2 = var0.getJsonArray("do").toString();
      Matcher var3 = var1.matcher(var2);
      return !var3.find() ? null : var3.group(1);
   }

   public Types getType() {
      return this.type;
   }

   public PXCaptcha(Logger var1, RealClient var2, Types var3, String var4, String var5, Site var6) {
      this.isFirstTry = true;
      this.failedSolves = 0;
      this.sid = null;
      this.logger = var1;
      this.client = RealClientFactory.fromOther(Vertx.currentContext().owner(), var2, ClientType.CHROME);
      this.type = var3;
      this.parentVID = var4;
      this.parentUUID = var5;
      this.SITE = var6;
      this.vidAge = System.currentTimeMillis();
      this.needsDesktopAppID = false;
   }

   public static String getCIUUID(JsonObject var0) {
      return parseDO(var0, CI_UUID_PATTERN);
   }

   public static HttpRequest atc(HttpServerRequest var0, RealClient var1) {
      HttpRequest var2 = var1.postAbs("https://hibbett-mobileapi.prolific.io/ecommerce/cart/e6c613832fb68eac5dbe2ba856/items?skuIds=39553459&customerId=efKbcteBKcyafdIW7n6Sk3XCSi").as(BodyCodec.buffer());
      var2.putHeaders(Headers$Pseudo.MPAS.get());
      var2.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
      var2.putHeader("version", "4.15.0");
      var2.putHeader("platform", "android");
      var2.putHeader("user-agent", var0.headers().get("user-agent"));
      var2.putHeader("x-px-authorization", var0.headers().get("x-px-authorization"));
      var2.putHeader("authorization", var0.headers().get("authorization"));
      var2.putHeader("content-type", "application/json; charset=UTF-8");
      var2.putHeader("content-length", "DEFAULT_VALUE");
      var2.putHeader("accept-encoding", "gzip");
      return var2;
   }

   public CompletableFuture solveCaptcha(String var1) {
      Objects.requireNonNull(this);
      if (0 >= 1 && System.currentTimeMillis() - this.vidAge <= 120000L) {
         Objects.requireNonNull(this);
         long var10000;
         if (0 <= 6) {
            Objects.requireNonNull(this);
            var10000 = 5000L * 0L;
         } else {
            var10000 = 30000L;
         }

         CompletableFuture var3 = VertxUtil.hardCodedSleep(var10000);
         if (!var3.isDone()) {
            CompletableFuture var2 = var3;
            return var2.exceptionally(Function.identity()).thenCompose(PXCaptcha::async$solveCaptcha);
         }

         var3.join();
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public HttpRequest getMainMinJS() {
      // $FF: Couldn't be decompiled
   }

   public void setRefererAndOrigin(String var1) {
      // $FF: Couldn't be decompiled
   }

   public void setFirefoxScriptHeaders(HttpRequest var1) {
      var1.putHeaders(Headers$Pseudo.MPAS.get());
      var1.putHeader("user-agent", this.device.getUserAgent());
      var1.putHeader("accept", "*/*");
      var1.putHeader("accept-language", this.device.getAcceptLanguage());
      var1.putHeader("accept-encoding", this.device.getAcceptEncoding() == null ? "gzip, deflate, br" : this.device.getAcceptEncoding());
      var1.putHeader("referer", this.origin + "/");
      var1.putHeader("te", "trailers");
   }

   public static String getCP(JsonObject var0) {
      return parseDO(var0, CP_PATTERN);
   }

   public String getPackageName() {
      // $FF: Couldn't be decompiled
   }

   public static void parseCookiesFromResp(String var0, Map var1) {
      try {
         Matcher var2 = PXDE_PATTERN.matcher(var0);
         Matcher var3 = BAKE_PATTERN.matcher(var0);
         if (var3.find()) {
            var1.put("_px3", var3.group(1));
         }

         if (var2.find()) {
            var1.put("_pxde", var2.group(1));
         }
      } catch (Throwable var4) {
      }

   }

   public static String getSFFTOKEN(JsonObject var0) {
      return parseDO(var0, SFF_SCS_PATTERN);
   }

   public void setWebviewImageHeaders(HttpRequest var1) {
      var1.putHeader("user-agent", this.device.getUserAgent());
      var1.putHeader("accept", "*/*");
      var1.putHeader("origin", this.origin);
      var1.putHeader("x-requested-with", this.getPackageName());
      var1.putHeader("sec-fetch-site", "cross-site");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", this.origin + "/");
      var1.putHeader("accept-encoding", this.device.getAcceptEncoding() == null ? "gzip, deflate" : this.device.getAcceptEncoding());
      var1.putHeader("accept-language", this.device.getAcceptLanguage());
   }

   public CompletableFuture prepareDevice() {
      return this.setBrowserType();
   }

   public String getUseragent() {
      return this.device.getUserAgent();
   }

   public HttpRequest bundleReq() {
      String var1 = this.getSiteID();
      HttpRequest var2 = this.client.postAbs("https://collector-" + var1 + ".px-cloud.net/assets/js/bundle").timeout(TimeUnit.SECONDS.toMillis(15L)).as(BodyCodec.jsonObject());
      this.setChromeHeaders(var2);
      return var2;
   }

   public static String getSTSTOKEN(JsonObject var0) {
      return parseDO(var0, STS_PATTERN);
   }

   public HttpRequest getFavicon() {
      this.client.cookieStore().put("_pxvid", this.parentVID, "www.perimeterx.com");
      HttpRequest var1 = this.client.getAbs("https://www.perimeterx.com/favicon.ico").as(BodyCodec.buffer());
      var1.putHeader("user-agent", this.device.getUserAgent());
      var1.putHeader("accept", "image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8");
      var1.putHeader("x-requested-with", this.getPackageName());
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "no-cors");
      var1.putHeader("sec-fetch-dest", "image");
      var1.putHeader("referer", this.origin + "/");
      var1.putHeader("accept-encoding", this.device.getAcceptEncoding() == null ? "gzip, deflate" : this.device.getAcceptEncoding());
      var1.putHeader("accept-language", this.device.getAcceptLanguage());
      return var1;
   }

   public void updateVIDandUUID(RealClient var1, String var2, String var3) {
      this.client = RealClientFactory.fromOther(Vertx.currentContext().owner(), var1, ClientType.CHROME);
      if (var2 != null && (this.parentVID == null || !this.parentVID.equals(var2))) {
         this.vidAge = System.currentTimeMillis();
      }

      this.parentVID = var2;
      this.parentUUID = var3;
   }

   public static String getCTSValue(JsonObject var0) {
      return parseDO(var0, CTS_PATTERN);
   }

   public String getParentVID() {
      return this.parentVID;
   }

   public void setSafariScriptHeaders(HttpRequest var1) {
      var1.putHeaders(Headers$Pseudo.MSPA.get());
      var1.putHeader("accept", "*/*");
      var1.putHeader("accept-encoding", this.device.getAcceptEncoding() == null ? "gzip, deflate, br" : this.device.getAcceptEncoding());
      var1.putHeader("user-agent", this.device.getUserAgent());
      var1.putHeader("accept-language", this.device.getAcceptLanguage());
      var1.putHeader("referer", this.origin + "/");
   }

   public void setChromeImageHeaders(HttpRequest var1) {
      if (this.device.getSecUA() != null && this.device.getSecUAMobile() != null) {
         var1.putHeader("sec-ch-ua", this.device.getSecUA());
         var1.putHeader("sec-ch-ua-mobile", this.device.getSecUAMobile());
      }

      var1.putHeader("user-agent", this.device.getUserAgent());
      var1.putHeader("accept", "*/*");
      var1.putHeader("origin", this.origin);
      var1.putHeader("sec-fetch-site", "cross-site");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", this.origin + "/");
      var1.putHeader("accept-encoding", this.device.getAcceptEncoding() == null ? "gzip, deflate, br" : this.device.getAcceptEncoding());
      var1.putHeader("accept-language", this.device.getAcceptLanguage());
   }

   public static void lambda$sendPayload$3(Promise var0, AsyncResult var1) {
      if (var1.succeeded()) {
         Buffer var2 = (Buffer)((HttpResponse)var1.result()).body();
         System.out.println("Resp: " + var2);
         var0.tryComplete((Buffer)((HttpResponse)var1.result()).body());
      } else {
         var0.tryFail("Failed req");
      }

   }

   public void setFirefoxHeaders(HttpRequest var1) {
      var1.putHeaders(Headers$Pseudo.MPAS.get());
      var1.putHeader("user-agent", this.device.getUserAgent());
      var1.putHeader("accept", "*/*");
      var1.putHeader("accept-language", this.device.getAcceptLanguage());
      var1.putHeader("accept-encoding", this.device.getAcceptEncoding() == null ? "gzip, deflate, br" : this.device.getAcceptEncoding());
      var1.putHeader("referer", this.origin + "/");
      var1.putHeader("content-type", "application/x-www-form-urlencoded");
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("origin", this.origin);
      var1.putHeader("te", "trailers");
   }

   public void setWebviewScriptHeaders(HttpRequest var1) {
      var1.putHeader("user-agent", this.device.getUserAgent());
      var1.putHeader("accept", "*/*");
      var1.putHeader("x-requested-with", this.getPackageName());
      var1.putHeader("sec-fetch-site", "cross-site");
      var1.putHeader("sec-fetch-mode", "no-cors");
      var1.putHeader("sec-fetch-dest", "script");
      var1.putHeader("referer", this.origin + "/");
      var1.putHeader("accept-encoding", this.device.getAcceptEncoding() == null ? "gzip, deflate" : this.device.getAcceptEncoding());
      var1.putHeader("accept-language", this.device.getAcceptLanguage());
   }

   public HttpRequest hibbettCSS() {
      HttpRequest var1 = this.client.getAbs("https://www.hibbett.com/on/demandware.static/Sites-Hibbett-US-Site/-/default/css/pxchallenge.csss").as(BodyCodec.buffer());
      var1.putHeader("user-agent", this.device.getUserAgent());
      var1.putHeader("accept", "text/css,*/*;q=0.1");
      var1.putHeader("x-requested-with", this.getPackageName());
      var1.putHeader("sec-fetch-site", "cross-site");
      var1.putHeader("sec-fetch-mode", "no-cors");
      var1.putHeader("sec-fetch-dest", "style");
      var1.putHeader("referer", this.origin + "/");
      var1.putHeader("accept-encoding", this.device.getAcceptEncoding() == null ? "gzip, deflate" : DeviceHeaderParsers.getAcceptLanguage(this.device.getArr("PX313")));
      var1.putHeader("accept-language", this.device.getAcceptLanguage());
      return var1;
   }

   public static String getCSValue(JsonObject var0) {
      return parseCS(var0, CS_PATTERN);
   }

   public static CompletableFuture async$solveCaptcha(PXCaptcha var0, String var1, CompletableFuture var2, int var3, Object var4) {
      CompletableFuture var10000;
      switch (var3) {
         case 0:
            Objects.requireNonNull(var0);
            if (0 < 1 || System.currentTimeMillis() - var0.vidAge > 120000L) {
               return CompletableFuture.completedFuture((Object)null);
            }

            Objects.requireNonNull(var0);
            long var5;
            if (0 <= 6) {
               Objects.requireNonNull(var0);
               var5 = 5000L * 0L;
            } else {
               var5 = 30000L;
            }

            var10000 = VertxUtil.hardCodedSleep(var5);
            if (!var10000.isDone()) {
               var2 = var10000;
               return var2.exceptionally(Function.identity()).thenCompose(PXCaptcha::async$solveCaptcha);
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

   public static String getVID(ExtendedPayload var0, JsonObject var1) {
      return var1.toString().contains("vid|") ? getVIDUUID(var1) : var0.getVID();
   }

   public CompletableFuture sendPayload(ExtendedPayload var1) {
      HttpRequest var2;
      if (!var1.getType().equals(Types.DESKTOP)) {
         var2 = this.bundleReq();
      } else {
         var2 = this.collectorReq();
      }

      MultiMap var3 = var1.asForm();
      StringBuilder var4 = new StringBuilder();
      var3.forEach(PXCaptcha::lambda$sendPayload$5);
      Buffer var5 = Buffer.buffer(var4.toString().trim());

      while(this.client.isActive()) {
         try {
            CompletableFuture var10000 = Request.send(var2, var5);
            CompletableFuture var7;
            if (!var10000.isDone()) {
               var7 = var10000;
               return var7.exceptionally(Function.identity()).thenCompose(PXCaptcha::async$sendPayload);
            }

            HttpResponse var6 = (HttpResponse)var10000.join();
            if (var6 != null) {
               return CompletableFuture.completedFuture((JsonObject)var6.body());
            }

            var10000 = VertxUtil.randomSleep(10000L);
            if (!var10000.isDone()) {
               var7 = var10000;
               return var7.exceptionally(Function.identity()).thenCompose(PXCaptcha::async$sendPayload);
            }

            var10000.join();
         } catch (Throwable var8) {
            if (!this.client.isActive()) {
               break;
            }

            this.logger.warn("Error to send payload: {}", var8.getMessage());
         }
      }

      return CompletableFuture.failedFuture(new Exception("Failed to send payload. Active? " + this.client.isActive()));
   }

   public void setChromeScriptHeaders(HttpRequest var1) {
      if (this.device.getSecUA() != null && this.device.getSecUAMobile() != null) {
         var1.putHeader("sec-ch-ua", this.device.getSecUA());
         var1.putHeader("sec-ch-ua-mobile", this.device.getSecUAMobile());
      }

      var1.putHeader("user-agent", this.device.getUserAgent());
      var1.putHeader("accept", "*/*");
      var1.putHeader("sec-fetch-site", "cross-site");
      var1.putHeader("sec-fetch-mode", "no-cors");
      var1.putHeader("sec-fetch-dest", "script");
      var1.putHeader("referer", this.origin + "/");
      var1.putHeader("accept-encoding", this.device.getAcceptEncoding() == null ? "gzip, deflate, br" : this.device.getAcceptEncoding());
      var1.putHeader("accept-language", this.device.getAcceptLanguage() == null ? "en-US" : this.device.getAcceptLanguage());
   }

   public static String getCITOKEN(JsonObject var0) {
      return parseDO(var0, CI_TOKEN_PATTERN);
   }

   public static String getVIDUUID(JsonObject var0) {
      return parseDO(var0, VID_UUID_PATTERN);
   }

   public static void lambda$main$0(HttpServerRequest var0, Buffer var1) {
      var0.response().setStatusCode(200).putHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8").putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true").putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET,HEAD,PUT,PATCH,POST,DELETE").putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*").putHeader("timing-allow-origin", "*").putHeader("via", "1.1 google").putHeader("alt-svc", "clear").putHeader("expires", "0").putHeader("cache-control", "no-cache").end();
   }

   public void setType(Types var1) {
      this.type = var1;
   }

   public static void lambda$imageReq$4(StringBuilder var0, Map.Entry var1) {
      if (var0.length() > 0) {
         var0.append("&");
      }

      if (((String)var1.getValue()).contains("\udd31")) {
         var0.append((String)var1.getKey()).append("=").append(URLEncoder.encode((String)var1.getValue(), StandardCharsets.UTF_8));
      } else {
         var0.append((String)var1.getKey()).append("=").append((String)var1.getValue());
      }

   }

   public void setFirefoxImageHeaders(HttpRequest var1) {
      var1.putHeaders(Headers$Pseudo.MPAS.get());
      var1.putHeader("user-agent", this.device.getUserAgent());
      var1.putHeader("accept", "*/*");
      var1.putHeader("accept-language", this.device.getAcceptLanguage());
      var1.putHeader("accept-encoding", this.device.getAcceptEncoding() == null ? "gzip, deflate, br" : this.device.getAcceptEncoding());
      var1.putHeader("referer", this.origin + "/");
      var1.putHeader("origin", this.origin);
      var1.putHeader("te", "trailers");
   }
}
