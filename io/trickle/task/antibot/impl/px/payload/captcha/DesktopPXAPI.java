package io.trickle.task.antibot.impl.px.payload.captcha;

import io.netty.util.AsciiString;
import io.trickle.core.actor.TaskActor;
import io.trickle.task.antibot.impl.px.PerimeterX;
import io.trickle.task.sites.Site;
import io.trickle.util.Utils;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.trickle.webclient.ClientType;
import io.trickle.webclient.TaskApiClient;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.ProxyOptions;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.codec.BodyCodec;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class DesktopPXAPI extends PerimeterX {
   public static CharSequence VID_COOKIE = AsciiString.cached("_pxvid");
   public JsonObject cookieSession = new JsonObject();
   public static CharSequence PXHD_VALUE = AsciiString.cached("_pxhd");
   public String deviceNumber = "undefined";
   public TaskApiClient delegate;
   public static CharSequence DEFAULT_UA = AsciiString.cached("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.54 Safari/537.36");
   public MultiMap cachedResponse = null;
   public String userAgent;
   public static CharSequence DEFAULT_SEC_UA = AsciiString.cached("\"Google Chrome\";v=\"95\", \"Chromium\";v=\"95\", \";Not A Brand\";v=\"99\"");
   public String secUA;
   public static CharSequence FP_VALUE = AsciiString.cached("_pxff_fp");
   public static Site site;
   public static CharSequence RF_VALUE = AsciiString.cached("_pxff_rf");
   public long lockoutTiming = -1L;
   public static CharSequence ONE_VALUE = AsciiString.cached("1");
   public static CharSequence CFP_VALUE = AsciiString.cached("_pxff_cfp");

   public String getDeviceSecUA() {
      return this.secUA == null ? DEFAULT_SEC_UA.toString() : this.secUA;
   }

   public String getVid() {
      return this.cookieSession.getString("vid", (String)null);
   }

   public HttpRequest solveRequest() {
      return this.client.getAbs("https://pxgen-bmii2thzea-uc.a.run.app/gen").as(BodyCodec.jsonObject()).addQueryParam("authToken", "PX-8E7DF802-E106-44D7-8E32-C26E7F8FE976").addQueryParam("site", site.toString().toLowerCase()).addQueryParam("region", "com").addQueryParam("proxy", this.getProxyString()).addQueryParam("deviceNumber", this.deviceNumber);
   }

   public String getDeviceLang() {
      return "en-GB,en;q=0.9";
   }

   public static CompletableFuture async$solveCaptcha(DesktopPXAPI param0, String param1, String param2, String param3, int param4, CompletableFuture param5, Throwable param6, int param7, Object param8) {
      // $FF: Couldn't be decompiled
   }

   static {
      site = Site.WALMART;
   }

   public void reset() {
      this.cookieSession = new JsonObject();
      this.userAgent = null;
      this.deviceNumber = "undefined";
   }

   public String getProxyString() {
      ProxyOptions var1 = this.delegate.getWebClient().getOptions().getProxyOptions();
      if (var1 == null) {
         return "";
      } else {
         String var10000 = var1.getUsername();
         return "http://" + var10000 + ":" + var1.getPassword() + "@" + var1.getHost() + ":" + var1.getPort();
      }
   }

   public MultiMap handleResponse(String var1) {
      if (var1.contains("=")) {
         String var2 = var1.split("=")[0];
         String var3 = var1.replace(var2 + "=", "");
         return MultiMap.caseInsensitiveMultiMap().add(var2, var3);
      } else {
         return MultiMap.caseInsensitiveMultiMap();
      }
   }

   public CompletableFuture initialise() {
      return CompletableFuture.completedFuture(true);
   }

   public String getDeviceAcceptEncoding() {
      return "gzip, deflate";
   }

   public static CompletableFuture async$solve(DesktopPXAPI param0, int param1, CompletableFuture param2, Throwable param3, int param4, Object param5) {
      // $FF: Couldn't be decompiled
   }

   public HttpRequest captchaRequest() {
      return this.client.getAbs("https://pxgen-bmii2thzea-uc.a.run.app/holdcaptcha").as(BodyCodec.jsonObject()).addQueryParam("authToken", "PX-8E7DF802-E106-44D7-8E32-C26E7F8FE976").addQueryParam("site", site.toString().toLowerCase()).addQueryParam("region", "com").addQueryParam("proxy", this.getProxyString()).addQueryParam("deviceNumber", this.deviceNumber).addQueryParam("captchaData", this.cookieSession.encode());
   }

   public String getDeviceUA() {
      return this.userAgent == null ? DEFAULT_UA.toString() : this.userAgent;
   }

   public boolean validateError(String var1) {
      if (var1 == null) {
         return true;
      } else {
         String var2 = var1.toLowerCase(Locale.ROOT);
         return var2.contains("not valid") || var2.contains("proxy");
      }
   }

   public CompletableFuture solve() {
      if (this.cachedResponse == null && System.currentTimeMillis() - this.lockoutTiming > 5000L) {
         for(int var13 = 1; var13 <= 30; ++var13) {
            CompletableFuture var11;
            CompletableFuture var10000;
            try {
               if (this.logger.isDebugEnabled()) {
                  this.logger.debug("Generating PX session via API attempt {}", var13);
               }

               var10000 = Request.send(this.solveRequest());
               if (!var10000.isDone()) {
                  var11 = var10000;
                  return var11.exceptionally(Function.identity()).thenCompose(DesktopPXAPI::async$solve);
               }

               HttpResponse var2 = (HttpResponse)var10000.join();
               if (var2 != null) {
                  JsonObject var3 = (JsonObject)var2.body();
                  if (this.logger.isDebugEnabled()) {
                     this.logger.debug("API sensor response received: {}", var3.encodePrettily());
                  }

                  String var4 = var3.getString("cookie", (String)null);
                  boolean var5 = this.isBool(var3) ? var3.getBoolean("error", false) : this.validateError(var4);
                  if (!var5 && var4 != null && !var4.isEmpty()) {
                     if (var3.containsKey("data")) {
                        this.cookieSession = var3.getJsonObject("data");
                        this.userAgent = this.cookieSession.getString("UserAgent");
                        String var6 = Utils.parseChromeVer(this.userAgent);
                        this.secUA = "\"Google Chrome\";v=\"" + var6 + "\", \"Chromium\";v=\"" + var6 + "\", \";Not A Brand\";v=\"99\"";
                        this.deviceNumber = this.cookieSession.getString("deviceNumber");
                        MultiMap var7 = MultiMap.caseInsensitiveMultiMap();
                        if (this.getVid() != null && !this.getVid().isBlank()) {
                           var7.add(VID_COOKIE, this.getVid());
                        }

                        var7.add(RF_VALUE, ONE_VALUE);
                        var7.add(FP_VALUE, ONE_VALUE);
                        var7.add(CFP_VALUE, ONE_VALUE);
                        String var8 = this.cookieSession.getString("pxhdCookie", (String)null);
                        if (var8 != null && !var8.isBlank()) {
                           var7.add(PXHD_VALUE, var8);
                        }

                        if (var4.contains("=")) {
                           String var9 = var4.split("=")[0];
                           String var10 = var4.replace(var9 + "=", "");
                           var7.add(var9, var10);
                        }

                        return CompletableFuture.completedFuture(this.handleResponse(var4));
                     }
                  } else if (this.logger.isDebugEnabled()) {
                     this.logger.debug("Invalid sensor api response: {}", var3.encodePrettily());
                  }
               } else if (this.logger.isDebugEnabled()) {
                  this.logger.debug("Solving sensor via API failed. No response received. Retrying...");
               } else {
                  this.logger.warn("Failed to solve sensor. Retrying...");
               }
            } catch (Throwable var12) {
               this.logger.warn("Error solving sensor: {}. Retrying...", var12.getMessage());
               if (this.logger.isDebugEnabled()) {
                  this.logger.debug(var12);
               }

               var10000 = VertxUtil.randomSleep(3000L);
               if (!var10000.isDone()) {
                  var11 = var10000;
                  return var11.exceptionally(Function.identity()).thenCompose(DesktopPXAPI::async$solve);
               }

               var10000.join();
            }
         }

         return CompletableFuture.completedFuture(MultiMap.caseInsensitiveMultiMap());
      } else {
         MultiMap var1 = this.cachedResponse;
         this.lockoutTiming = 0L;
         this.cachedResponse = null;
         if (this.logger.isDebugEnabled()) {
            this.logger.debug("Double api sensor solving lockout hit. Returning cached response: {}", var1);
         }

         return CompletableFuture.completedFuture(var1);
      }
   }

   public DesktopPXAPI(TaskActor var1) {
      super(var1, (ClientType)null);
      this.delegate = var1.getClient();
   }

   public boolean isBool(JsonObject var1) {
      Object var2 = var1.getValue("error", (Object)null);
      return var2 != null ? var2 instanceof Boolean : false;
   }

   public CompletableFuture solveCaptcha(String var1, String var2, String var3) {
      for(int var4 = 1; var4 <= 30; ++var4) {
         CompletableFuture var10000;
         CompletableFuture var9;
         try {
            if (this.deviceNumber.equalsIgnoreCase("undefined")) {
               return this.solve();
            }

            if (this.logger.isDebugEnabled()) {
               this.logger.debug("Solving PX captcha via API attempt {}", var4);
            }

            var10000 = Request.send(this.captchaRequest());
            if (!var10000.isDone()) {
               var9 = var10000;
               return var9.exceptionally(Function.identity()).thenCompose(DesktopPXAPI::async$solveCaptcha);
            }

            HttpResponse var5 = (HttpResponse)var10000.join();
            if (var5 != null) {
               JsonObject var6 = (JsonObject)var5.body();
               if (this.logger.isDebugEnabled()) {
                  this.logger.debug("API captcha response received: {}", var6.encodePrettily());
               }

               String var7 = var6.getString("cookie", (String)null);
               boolean var8 = this.isBool(var6) ? var6.getBoolean("error", false) : this.validateError(var7);
               if (!var8 && var7 != null && !var7.isEmpty()) {
                  if (var6.containsKey("data")) {
                     this.cookieSession = var6.getJsonObject("data");
                     this.lockoutTiming = System.currentTimeMillis();
                     this.logger.info("Solved captcha successfully!");
                     this.cachedResponse = this.handleResponse(var7);
                     return CompletableFuture.completedFuture(this.cachedResponse);
                  }
               } else if (this.logger.isDebugEnabled()) {
                  this.logger.debug("Invalid sensor api response: {}", var6.encodePrettily());
               }
            } else if (this.logger.isDebugEnabled()) {
               this.logger.debug("Solving sensor via API failed. No response received. Retrying...");
            } else {
               this.logger.warn("Failed to solve captcha. Retrying...");
            }
         } catch (Throwable var10) {
            this.logger.warn("Error solving sensor: {}. Retrying...", var10.getMessage());
            if (this.logger.isDebugEnabled()) {
               this.logger.debug(var10);
            }

            var10000 = VertxUtil.randomSleep(3000L);
            if (!var10000.isDone()) {
               var9 = var10000;
               return var9.exceptionally(Function.identity()).thenCompose(DesktopPXAPI::async$solveCaptcha);
            }

            var10000.join();
         }
      }

      return CompletableFuture.completedFuture(MultiMap.caseInsensitiveMultiMap());
   }
}
