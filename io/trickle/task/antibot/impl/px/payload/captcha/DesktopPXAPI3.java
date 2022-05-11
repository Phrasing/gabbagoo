package io.trickle.task.antibot.impl.px.payload.captcha;

import io.netty.util.AsciiString;
import io.trickle.core.actor.TaskActor;
import io.trickle.task.antibot.impl.px.PerimeterX;
import io.trickle.util.Utils;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.trickle.webclient.ClientType;
import io.trickle.webclient.RealClient;
import io.trickle.webclient.TaskApiClient;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.ProxyOptions;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import io.vertx.ext.web.codec.BodyCodec;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class DesktopPXAPI3 extends PerimeterX {
   public TaskApiClient delegate;
   public static CharSequence ONE_VALUE = AsciiString.cached("1");
   public String cookie;
   public static CharSequence RF_VALUE = AsciiString.cached("_pxff_rf");
   public static CharSequence CFP_VALUE = AsciiString.cached("_pxff_cfp");
   public static CharSequence FP_VALUE = AsciiString.cached("_pxff_fp");
   public String secUserAgent;
   public static int TIMEOUT = 120000;
   public static CharSequence PX3_VALUE = AsciiString.cached("_px3");
   public String userAgent;
   public static int RETRY = 3000;

   public String getProxyString() {
      ProxyOptions var1 = this.delegate.getWebClient().getOptions().getProxyOptions();
      return var1 == null ? "" : String.format("%s:%s:%s:%s", var1.getHost(), var1.getPort(), var1.getUsername(), var1.getPassword());
   }

   public static CompletableFuture async$solveCaptcha(DesktopPXAPI3 var0, String var1, String var2, String var3, DesktopPXAPI3 var4, CompletableFuture var5, int var6, Object var7) {
      DesktopPXAPI3 var10000;
      CompletableFuture var10001;
      switch (var6) {
         case 0:
            var10000 = var0;
            var10001 = var0.execute("hold", "https://www.walmart.com/");
            if (!var10001.isDone()) {
               CompletableFuture var9 = var10001;
               return var9.exceptionally(Function.identity()).thenCompose(DesktopPXAPI3::async$solveCaptcha);
            }
            break;
         case 1:
            var10000 = var4;
            var10001 = var5;
            break;
         default:
            throw new IllegalArgumentException();
      }

      var10000.cookie = (String)var10001.join();
      MultiMap var8 = MultiMap.caseInsensitiveMultiMap();
      var8.add(PX3_VALUE, var0.cookie);
      return CompletableFuture.completedFuture(var8);
   }

   public void restartClient(RealClient var1) {
   }

   public JsonObject apiBody(String var1) {
      return (new JsonObject()).put("url", var1).put("proxy", this.getProxyString());
   }

   public CompletableFuture solveCaptcha(String var1, String var2, String var3) {
      CompletableFuture var10001 = this.execute("hold", "https://www.walmart.com/");
      if (!var10001.isDone()) {
         CompletableFuture var6 = var10001;
         return var6.exceptionally(Function.identity()).thenCompose(DesktopPXAPI3::async$solveCaptcha);
      } else {
         this.cookie = (String)var10001.join();
         MultiMap var4 = MultiMap.caseInsensitiveMultiMap();
         var4.add(PX3_VALUE, this.cookie);
         return CompletableFuture.completedFuture(var4);
      }
   }

   public String getDeviceUA() {
      return this.userAgent;
   }

   public static CompletableFuture async$initialise(DesktopPXAPI3 var0, DesktopPXAPI3 var1, CompletableFuture var2, int var3, Object var4) {
      DesktopPXAPI3 var10000;
      CompletableFuture var10001;
      switch (var3) {
         case 0:
            var10000 = var0;
            var10001 = var0.execute("init", "https://www.walmart.com/");
            if (!var10001.isDone()) {
               var2 = var10001;
               return var2.exceptionally(Function.identity()).thenCompose(DesktopPXAPI3::async$initialise);
            }
            break;
         case 1:
            var10000 = var1;
            var10001 = var2;
            break;
         default:
            throw new IllegalArgumentException();
      }

      var10000.cookie = (String)var10001.join();
      return CompletableFuture.completedFuture(true);
   }

   public void reset() {
   }

   public DesktopPXAPI3(TaskActor var1) {
      super(var1, (ClientType)null);
      this.delegate = var1.getClient();
   }

   public static CompletableFuture async$execute(DesktopPXAPI3 param0, String param1, String param2, int param3, CompletableFuture param4, int param5, Object param6) {
      // $FF: Couldn't be decompiled
   }

   public String getDeviceLang() {
      return "en-GB,en;q=0.9";
   }

   public String getDeviceAcceptEncoding() {
      return "gzip, deflate";
   }

   public String getDeviceSecUA() {
      return this.secUserAgent;
   }

   public CompletableFuture initialise() {
      CompletableFuture var10001 = this.execute("init", "https://www.walmart.com/");
      if (!var10001.isDone()) {
         CompletableFuture var2 = var10001;
         return var2.exceptionally(Function.identity()).thenCompose(DesktopPXAPI3::async$initialise);
      } else {
         this.cookie = (String)var10001.join();
         return CompletableFuture.completedFuture(true);
      }
   }

   public HttpRequest apiRequest(String var1) {
      return this.client.postAbs("https://px-gateway-dsor72du.uc.gateway.dev/" + var1).timeout(var1.equals("init") ? TimeUnit.SECONDS.toMillis(60L) : TimeUnit.SECONDS.toMillis(120L)).expect(ResponsePredicate.SC_OK).putHeader("x-api-key", "AIzaSyCxU4Wyg87uArwRL0NdlKb5oOAGTNTcX9Q").as(BodyCodec.jsonObject());
   }

   public CompletableFuture execute(String var1, String var2) {
      byte var3 = 0;

      while(var3 < 10 && this.delegate.getWebClient().isActive()) {
         CompletableFuture var10000;
         CompletableFuture var8;
         try {
            var10000 = Request.send(this.apiRequest(var1), this.apiBody(var2));
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(DesktopPXAPI3::async$execute);
            }

            HttpResponse var4 = (HttpResponse)var10000.join();
            if (var4 != null) {
               if (var4.statusCode() == 504) {
                  this.logger.warn("Unreachable proxy. Rotating");
                  this.delegate.rotateProxy();
               } else {
                  JsonObject var5 = (JsonObject)var4.body();
                  Objects.requireNonNull(var5, "No Result");
                  if (this.logger.isDebugEnabled()) {
                     this.logger.debug("Sensor API for step={} responded with body={}", var1, var5.encode());
                  }

                  String var6;
                  if (var5.getBoolean("ok", false)) {
                     var6 = var5.getString("cookie");
                     Objects.requireNonNull(var6, "Sensor response none");
                     String var10 = var5.getString("ua");
                     Objects.requireNonNull(var10, "Sensor data none");
                     this.userAgent = var10;
                     this.parseSecUA(this.userAgent);
                     return CompletableFuture.completedFuture(var6);
                  }

                  if (this.logger.isDebugEnabled()) {
                     this.logger.debug("Sensor request not OK: {}", var5.encode());
                  }

                  var6 = var5.getString("err", "request failed");
                  if (var6.equalsIgnoreCase("request failed") || var6.equalsIgnoreCase("exhausted")) {
                     boolean var7 = this.delegate.rotateProxy();
                     if (this.logger.isDebugEnabled()) {
                        this.logger.debug("Rotated proxy after sensor fail ok={}", var7);
                     }
                  }
               }
            } else {
               this.logger.warn("Failed to get sensor. Retrying...");
            }
         } catch (Throwable var9) {
            this.logger.error("Failed to solve sensor: {}. Retrying...", var9.getMessage());
            if (this.logger.isDebugEnabled()) {
               this.logger.debug(var9);
            }
         }

         var10000 = VertxUtil.randomSleep(3000L);
         if (!var10000.isDone()) {
            var8 = var10000;
            return var8.exceptionally(Function.identity()).thenCompose(DesktopPXAPI3::async$execute);
         }

         var10000.join();
      }

      return CompletableFuture.failedFuture(new Exception("Exceeded retries"));
   }

   public CompletableFuture solve() {
      MultiMap var1 = MultiMap.caseInsensitiveMultiMap();
      var1.add(RF_VALUE, ONE_VALUE);
      var1.add(FP_VALUE, ONE_VALUE);
      var1.add(CFP_VALUE, ONE_VALUE);
      var1.add(PX3_VALUE, this.cookie);
      return CompletableFuture.completedFuture(var1);
   }

   public String getVid() {
      return "";
   }

   public void parseSecUA(String var1) {
      String var2 = Utils.parseChromeVer(this.userAgent);
      this.secUserAgent = "\"Google Chrome\";v=\"" + var2 + "\", \"Chromium\";v=\"" + var2 + "\", \";Not A Brand\";v=\"99\"";
   }
}
