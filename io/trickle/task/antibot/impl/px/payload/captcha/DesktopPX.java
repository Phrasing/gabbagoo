package io.trickle.task.antibot.impl.px.payload.captcha;

import io.netty.util.AsciiString;
import io.trickle.core.VertxSingleton;
import io.trickle.core.actor.TaskActor;
import io.trickle.task.antibot.impl.px.PerimeterX;
import io.trickle.task.sites.Site;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.trickle.webclient.ClientType;
import io.trickle.webclient.TaskApiClient;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.impl.HttpRequestImpl;
import io.vertx.ext.web.codec.BodyCodec;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;

public class DesktopPX extends PerimeterX {
   public Function okayAndConversionFunc;
   public Function setupDevice;
   public static Site site;
   public TaskApiClient delegate;
   public static CharSequence DEFAULT_UA = AsciiString.cached("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.45 Safari/537.36");
   public long lockoutTiming = -1L;
   public String userAgent;
   public static CharSequence CTS_VALUE = AsciiString.cached("pxcts");
   public static CharSequence PXHD_VALUE = AsciiString.cached("_pxhd");
   public static CharSequence PXDE_VALUE = AsciiString.cached("_pxde");
   public static CharSequence FP_VALUE = AsciiString.cached("_pxff_fp");
   public String deviceNumber;
   public MultiMap cachedResponse;
   public String sid;
   public static Function okayStatusFunc;
   public static CharSequence DEFAULT_SEC_UA = AsciiString.cached("\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"96\", \"Google Chrome\";v=\"96\"");
   public static CharSequence PX3_VALUE = AsciiString.cached("_px3");
   public static CharSequence CFP_VALUE = AsciiString.cached("_pxff_cfp");
   public static CharSequence ONE_VALUE = AsciiString.cached("1");
   public JsonArray performance;
   public String uuid;
   public String vid;
   public long waitTime;
   public JsonObject cookieSession;
   public static CharSequence VID_COOKIE = AsciiString.cached("_pxvid");
   public static CharSequence RF_VALUE = AsciiString.cached("_pxff_rf");
   public String secUA;

   public static CompletableFuture async$execute(DesktopPX param0, String param1, Function param2, Supplier param3, Object param4, int param5, HttpRequest param6, CompletableFuture param7, HttpResponse param8, int param9, Optional param10, Throwable param11, int param12, Object param13) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture solve() {
      byte var1 = 1;
      if (var1 <= 30) {
         CompletableFuture var10000 = this.execute("API 1", this.okayAndConversionFunc, this::lambda$solve$3, this.buildReqBody((String)null, (JsonArray)null));
         CompletableFuture var7;
         if (!var10000.isDone()) {
            var7 = var10000;
            return var7.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solve);
         } else {
            Buffer var2 = (Buffer)var10000.join();
            var10000 = VertxUtil.hardCodedSleep(this.waitTime);
            if (!var10000.isDone()) {
               var7 = var10000;
               return var7.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solve);
            } else {
               var10000.join();
               var10000 = this.execute("Sensor 1/2", okayStatusFunc, this::collectorReq, var2);
               if (!var10000.isDone()) {
                  var7 = var10000;
                  return var7.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solve);
               } else {
                  String var3 = (String)var10000.join();
                  var10000 = this.execute("API 2", this.okayAndConversionFunc, this::lambda$solve$4, this.buildReqBody(var3, this.performance));
                  if (!var10000.isDone()) {
                     var7 = var10000;
                     return var7.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solve);
                  } else {
                     Buffer var4 = (Buffer)var10000.join();
                     var10000 = VertxUtil.hardCodedSleep(this.waitTime);
                     if (!var10000.isDone()) {
                        var7 = var10000;
                        return var7.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solve);
                     } else {
                        var10000.join();
                        var10000 = this.execute("Sensor 2/2", okayStatusFunc, this::collectorReq, var4);
                        if (!var10000.isDone()) {
                           var7 = var10000;
                           return var7.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solve);
                        } else {
                           String var5 = (String)var10000.join();
                           MultiMap var6 = MultiMap.caseInsensitiveMultiMap();
                           if (this.getVid() != null && !this.getVid().isBlank()) {
                              var6.add(VID_COOKIE, this.getVid());
                           }

                           var6.add(RF_VALUE, ONE_VALUE);
                           var6.add(FP_VALUE, ONE_VALUE);
                           var6.add(CFP_VALUE, ONE_VALUE);
                           this.parseResultCookies(var5, var6);
                           return CompletableFuture.completedFuture(var6);
                        }
                     }
                  }
               }
            }
         }
      } else {
         return CompletableFuture.completedFuture(MultiMap.caseInsensitiveMultiMap());
      }
   }

   public HttpRequest lambda$solve$4() {
      return this.apiRequest("2", (Boolean)null);
   }

   public static String lambda$static$0(HttpResponse var0) {
      return var0.statusCode() == 200 ? var0.bodyAsString() : null;
   }

   public HttpRequest bundleReq() {
      HttpRequest var1 = this.client.postAbs("https://collector-pxu6b0qd2s.px-cloud.net/assets/js/bundle").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("sec-ch-ua", this.getDeviceSecUA());
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", this.getDeviceUA());
      var1.putHeader("sec-ch-ua-platform", "\"macOS\"");
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

   public String getVid() {
      return this.cookieSession.getString("vid", (String)null);
   }

   public Buffer lambda$new$1(HttpResponse var1) {
      if (var1.statusCode() == 200) {
         this.userAgent = var1.bodyAsString();
         this.secUA = var1.getHeader("sec-ua");
         this.deviceNumber = var1.getHeader("device");
         return var1.bodyAsBuffer();
      } else {
         return null;
      }
   }

   public Buffer lambda$new$2(HttpResponse var1) {
      if (var1.statusCode() == 200 && var1.getHeader("performance") != null) {
         this.uuid = var1.getHeader("uuid");
         this.sid = var1.getHeader("sid");
         this.vid = var1.getHeader("vid");
         this.waitTime = Long.parseLong(var1.getHeader("completionEpoch")) - System.currentTimeMillis();
         this.performance = new JsonArray(var1.getHeader("performance"));
         if (var1.getHeader("cts") != null) {
            this.delegate.getCookies().put(String.valueOf(CTS_VALUE), var1.getHeader("cts"), ".walmart.com");
         }

         this.deviceNumber = var1.getHeader("device");
         return var1.bodyAsBuffer();
      } else {
         return null;
      }
   }

   public CompletableFuture execute(String var1, Function var2, Supplier var3, Object var4) {
      int var5 = 0;

      while(var5++ < Integer.MAX_VALUE) {
         CompletableFuture var10;
         CompletableFuture var12;
         try {
            HttpRequest var7 = (HttpRequest)var3.get();
            HttpResponse var6;
            if (((HttpRequestImpl)var7).method().equals(HttpMethod.POST)) {
               var12 = Request.send(var7, var4);
               if (!var12.isDone()) {
                  var10 = var12;
                  return var10.exceptionally(Function.identity()).thenCompose(DesktopPX::async$execute);
               }

               var6 = (HttpResponse)var12.join();
            } else {
               var12 = Request.send(var7);
               if (!var12.isDone()) {
                  var10 = var12;
                  return var10.exceptionally(Function.identity()).thenCompose(DesktopPX::async$execute);
               }

               var6 = (HttpResponse)var12.join();
            }

            if (var6 != null) {
               int var8 = var6.statusCode();
               Optional var9 = Optional.ofNullable(var2.apply(var6));
               if (var9.isPresent()) {
                  return CompletableFuture.completedFuture(var9.get());
               }

               this.logger.warn("Failed {}: '{}'", var1.toLowerCase(Locale.ROOT), var8);
               var12 = VertxUtil.randomSleep(5000L);
               if (!var12.isDone()) {
                  var10 = var12;
                  return var10.exceptionally(Function.identity()).thenCompose(DesktopPX::async$execute);
               }

               var12.join();
            } else {
               var12 = VertxUtil.randomSleep(5000L);
               if (!var12.isDone()) {
                  var10 = var12;
                  return var10.exceptionally(Function.identity()).thenCompose(DesktopPX::async$execute);
               }

               var12.join();
            }
         } catch (Throwable var11) {
            this.logger.error("Error " + var1.toLowerCase(Locale.ROOT) + ": {}", var11.getMessage());
            if (this.logger.isDebugEnabled()) {
               this.logger.debug(var11);
            }

            var12 = VertxUtil.randomSleep(5000L);
            if (!var12.isDone()) {
               var10 = var12;
               return var10.exceptionally(Function.identity()).thenCompose(DesktopPX::async$execute);
            }

            var12.join();
         }
      }

      return CompletableFuture.failedFuture(new Exception("Failed to execute " + var1));
   }

   public JsonObject buildReqBody(String var1, JsonArray var2) {
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

   public HttpRequest lambda$initialise$10() {
      return this.apiRequest("ua", (Boolean)null).method(HttpMethod.GET);
   }

   public CompletableFuture solveCaptcha(String var1, String var2, String var3) {
      this.uuid = var2;
      this.vid = var1;

      for(int var4 = 1; var4 <= ThreadLocalRandom.current().nextInt(1, 4); ++var4) {
         this.performance = null;
         CompletableFuture var10000 = this.execute("API 1", this.okayAndConversionFunc, this::lambda$solveCaptcha$5, this.buildReqBody((String)null, (JsonArray)null));
         CompletableFuture var14;
         if (!var10000.isDone()) {
            var14 = var10000;
            return var14.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
         }

         Buffer var5 = (Buffer)var10000.join();
         var10000 = VertxUtil.hardCodedSleep(this.waitTime);
         if (!var10000.isDone()) {
            var14 = var10000;
            return var14.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
         }

         var10000.join();
         var10000 = this.execute("Sensor 1/4", okayStatusFunc, this::bundleReq, var5);
         if (!var10000.isDone()) {
            var14 = var10000;
            return var14.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
         }

         String var6 = (String)var10000.join();
         var10000 = this.execute("API 2", this.okayAndConversionFunc, this::lambda$solveCaptcha$6, this.buildReqBody(var6, this.performance));
         if (!var10000.isDone()) {
            var14 = var10000;
            return var14.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
         }

         Buffer var7 = (Buffer)var10000.join();
         var10000 = VertxUtil.hardCodedSleep(this.waitTime);
         if (!var10000.isDone()) {
            var14 = var10000;
            return var14.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
         }

         var10000.join();
         var10000 = this.execute("Sensor 2/4", okayStatusFunc, this::bundleReq, var7);
         if (!var10000.isDone()) {
            var14 = var10000;
            return var14.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
         }

         String var8 = (String)var10000.join();
         var10000 = this.execute("API 3", this.okayAndConversionFunc, this::lambda$solveCaptcha$7, this.buildReqBody(var6, this.performance));
         if (!var10000.isDone()) {
            var14 = var10000;
            return var14.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
         }

         Buffer var9 = (Buffer)var10000.join();
         var10000 = VertxUtil.hardCodedSleep(this.waitTime);
         if (!var10000.isDone()) {
            var14 = var10000;
            return var14.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
         }

         var10000.join();
         var10000 = this.execute("Sensor 3/4", okayStatusFunc, this::lambda$solveCaptcha$8, (Object)null);
         if (!var10000.isDone()) {
            var14 = var10000;
            return var14.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
         }

         String var10 = (String)var10000.join();
         var10000 = this.execute("API 4", this.okayAndConversionFunc, this::lambda$solveCaptcha$9, this.buildReqBody(var6, this.performance));
         if (!var10000.isDone()) {
            var14 = var10000;
            return var14.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
         }

         Buffer var11 = (Buffer)var10000.join();
         var10000 = VertxUtil.hardCodedSleep(this.waitTime);
         if (!var10000.isDone()) {
            var14 = var10000;
            return var14.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
         }

         var10000.join();
         var10000 = this.execute("Sensor 4/4", okayStatusFunc, this::bundleReq, var11);
         if (!var10000.isDone()) {
            var14 = var10000;
            return var14.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
         }

         String var12 = (String)var10000.join();
         if (var12.contains("cv|0")) {
            this.logger.info("Successfully solved captcha");
            MultiMap var13 = MultiMap.caseInsensitiveMultiMap();
            if (this.getVid() != null && !this.getVid().isBlank()) {
               var13.add(VID_COOKIE, this.getVid());
            }

            var13.add(RF_VALUE, ONE_VALUE);
            var13.add(FP_VALUE, ONE_VALUE);
            var13.add(CFP_VALUE, ONE_VALUE);
            this.parseResultCookies(var12, var13);
            return CompletableFuture.completedFuture(var13);
         }

         this.logger.error("Retrying captcha solve...");
         this.sid = null;
         var10000 = VertxUtil.randomSleep(1000L);
         if (!var10000.isDone()) {
            var14 = var10000;
            return var14.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
         }

         var10000.join();
      }

      this.delegate.getCookies().removeAnyMatch("_pxhd");
      return CompletableFuture.completedFuture(MultiMap.caseInsensitiveMultiMap());
   }

   public HttpRequest imageReq(Buffer var1) {
      HttpRequest var2 = this.client.getAbs("https://collector-pxu6b0qd2s.px-client.net/b/g?" + var1.toString()).as(BodyCodec.buffer());
      var2.putHeader("sec-ch-ua", this.getDeviceSecUA());
      var2.putHeader("sec-ch-ua-mobile", "?0");
      var2.putHeader("user-agent", this.getDeviceUA());
      var2.putHeader("sec-ch-ua-platform", "\"macOS\"");
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

   public HttpRequest collectorReq() {
      HttpRequest var1 = this.client.postAbs("https://collector-pxu6b0qd2s.px-cloud.net/api/v2/collector").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("sec-ch-ua", this.getDeviceSecUA());
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", this.getDeviceUA());
      var1.putHeader("sec-ch-ua-platform", "\"macOS\"");
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

   public HttpRequest lambda$solveCaptcha$7() {
      return this.apiRequest("img", true);
   }

   public DesktopPX(TaskActor var1) {
      super(var1, ClientType.CHROME);
      this.lockoutTiming = -1L;
      this.cachedResponse = null;
      this.setupDevice = this::lambda$new$1;
      this.okayAndConversionFunc = this::lambda$new$2;
      this.deviceNumber = "undefined";
      this.cookieSession = new JsonObject();
      this.delegate = var1.getClient();
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
            this.logger.debug("Failed to find px3 or pxde ", var5);
         }
      }

   }

   public static CompletableFuture async$solve(DesktopPX var0, int var1, CompletableFuture var2, Buffer var3, String var4, Buffer var5, int var6, Object var7) {
      CompletableFuture var10000;
      label76: {
         Buffer var8;
         String var9;
         Buffer var10;
         CompletableFuture var13;
         label77: {
            label78: {
               label61: {
                  label60: {
                     Buffer var10001;
                     switch (var6) {
                        case 0:
                           var1 = 1;
                           if (var1 > 30) {
                              return CompletableFuture.completedFuture(MultiMap.caseInsensitiveMultiMap());
                           }

                           var10000 = var0.execute("API 1", var0.okayAndConversionFunc, var0::lambda$solve$3, var0.buildReqBody((String)null, (JsonArray)null));
                           if (!var10000.isDone()) {
                              var13 = var10000;
                              return var13.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solve);
                           }
                           break;
                        case 1:
                           var10000 = var2;
                           break;
                        case 2:
                           var10000 = var2;
                           var8 = var3;
                           break label60;
                        case 3:
                           var10000 = var2;
                           var8 = var3;
                           break label61;
                        case 4:
                           var10000 = var2;
                           var10001 = var3;
                           var9 = var4;
                           var8 = var10001;
                           break label78;
                        case 5:
                           var10000 = var2;
                           var10001 = var3;
                           String var10002 = var4;
                           var10 = var5;
                           var9 = var10002;
                           var8 = var10001;
                           break label77;
                        case 6:
                           var10000 = var2;
                           break label76;
                        default:
                           throw new IllegalArgumentException();
                     }

                     var8 = (Buffer)var10000.join();
                     var10000 = VertxUtil.hardCodedSleep(var0.waitTime);
                     if (!var10000.isDone()) {
                        var13 = var10000;
                        return var13.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solve);
                     }
                  }

                  var10000.join();
                  var10000 = var0.execute("Sensor 1/2", okayStatusFunc, var0::collectorReq, var8);
                  if (!var10000.isDone()) {
                     var13 = var10000;
                     return var13.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solve);
                  }
               }

               var9 = (String)var10000.join();
               var10000 = var0.execute("API 2", var0.okayAndConversionFunc, var0::lambda$solve$4, var0.buildReqBody(var9, var0.performance));
               if (!var10000.isDone()) {
                  var13 = var10000;
                  return var13.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solve);
               }
            }

            var10 = (Buffer)var10000.join();
            var10000 = VertxUtil.hardCodedSleep(var0.waitTime);
            if (!var10000.isDone()) {
               var13 = var10000;
               return var13.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solve);
            }
         }

         var10000.join();
         var10000 = var0.execute("Sensor 2/2", okayStatusFunc, var0::collectorReq, var10);
         if (!var10000.isDone()) {
            var13 = var10000;
            return var13.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solve);
         }
      }

      String var11 = (String)var10000.join();
      MultiMap var12 = MultiMap.caseInsensitiveMultiMap();
      if (var0.getVid() != null && !var0.getVid().isBlank()) {
         var12.add(VID_COOKIE, var0.getVid());
      }

      var12.add(RF_VALUE, ONE_VALUE);
      var12.add(FP_VALUE, ONE_VALUE);
      var12.add(CFP_VALUE, ONE_VALUE);
      var0.parseResultCookies(var11, var12);
      return CompletableFuture.completedFuture(var12);
   }

   public HttpRequest lambda$solveCaptcha$8(Buffer var1) {
      return this.imageReq(var1);
   }

   public static CompletableFuture async$solveCaptcha(DesktopPX var0, String var1, String var2, String var3, int var4, CompletableFuture var5, Buffer var6, String var7, Buffer var8, String var9, Buffer var10, String var11, Buffer var12, String var13, int var14, Object var15) {
      Buffer var16;
      CompletableFuture var10000;
      String var17;
      Buffer var18;
      String var19;
      Buffer var20;
      String var21;
      Buffer var22;
      CompletableFuture var25;
      switch (var14) {
         case 0:
            var0.uuid = var2;
            var0.vid = var1;
            var4 = 1;
            if (var4 > ThreadLocalRandom.current().nextInt(1, 4)) {
               var0.delegate.getCookies().removeAnyMatch("_pxhd");
               return CompletableFuture.completedFuture(MultiMap.caseInsensitiveMultiMap());
            }

            var0.performance = null;
            var10000 = var0.execute("API 1", var0.okayAndConversionFunc, var0::lambda$solveCaptcha$5, var0.buildReqBody((String)null, (JsonArray)null));
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var16 = (Buffer)var10000.join();
            var10000 = VertxUtil.hardCodedSleep(var0.waitTime);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var10000.join();
            var10000 = var0.execute("Sensor 1/4", okayStatusFunc, var0::bundleReq, var16);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var17 = (String)var10000.join();
            var10000 = var0.execute("API 2", var0.okayAndConversionFunc, var0::lambda$solveCaptcha$6, var0.buildReqBody(var17, var0.performance));
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var18 = (Buffer)var10000.join();
            var10000 = VertxUtil.hardCodedSleep(var0.waitTime);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var10000.join();
            var10000 = var0.execute("Sensor 2/4", okayStatusFunc, var0::bundleReq, var18);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var19 = (String)var10000.join();
            var10000 = var0.execute("API 3", var0.okayAndConversionFunc, var0::lambda$solveCaptcha$7, var0.buildReqBody(var17, var0.performance));
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var20 = (Buffer)var10000.join();
            var10000 = VertxUtil.hardCodedSleep(var0.waitTime);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var10000.join();
            var10000 = var0.execute("Sensor 3/4", okayStatusFunc, var0::lambda$solveCaptcha$8, (Object)null);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var21 = (String)var10000.join();
            var10000 = var0.execute("API 4", var0.okayAndConversionFunc, var0::lambda$solveCaptcha$9, var0.buildReqBody(var17, var0.performance));
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var22 = (Buffer)var10000.join();
            var10000 = VertxUtil.hardCodedSleep(var0.waitTime);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var10000.join();
            var10000 = var0.execute("Sensor 4/4", okayStatusFunc, var0::bundleReq, var22);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }
            break;
         case 1:
            var16 = (Buffer)var5.join();
            var10000 = VertxUtil.hardCodedSleep(var0.waitTime);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var10000.join();
            var10000 = var0.execute("Sensor 1/4", okayStatusFunc, var0::bundleReq, var16);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var17 = (String)var10000.join();
            var10000 = var0.execute("API 2", var0.okayAndConversionFunc, var0::lambda$solveCaptcha$6, var0.buildReqBody(var17, var0.performance));
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var18 = (Buffer)var10000.join();
            var10000 = VertxUtil.hardCodedSleep(var0.waitTime);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var10000.join();
            var10000 = var0.execute("Sensor 2/4", okayStatusFunc, var0::bundleReq, var18);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var19 = (String)var10000.join();
            var10000 = var0.execute("API 3", var0.okayAndConversionFunc, var0::lambda$solveCaptcha$7, var0.buildReqBody(var17, var0.performance));
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var20 = (Buffer)var10000.join();
            var10000 = VertxUtil.hardCodedSleep(var0.waitTime);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var10000.join();
            var10000 = var0.execute("Sensor 3/4", okayStatusFunc, var0::lambda$solveCaptcha$8, (Object)null);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var21 = (String)var10000.join();
            var10000 = var0.execute("API 4", var0.okayAndConversionFunc, var0::lambda$solveCaptcha$9, var0.buildReqBody(var17, var0.performance));
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var22 = (Buffer)var10000.join();
            var10000 = VertxUtil.hardCodedSleep(var0.waitTime);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var10000.join();
            var10000 = var0.execute("Sensor 4/4", okayStatusFunc, var0::bundleReq, var22);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }
            break;
         case 2:
            var16 = var6;
            var5.join();
            var10000 = var0.execute("Sensor 1/4", okayStatusFunc, var0::bundleReq, var6);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var17 = (String)var10000.join();
            var10000 = var0.execute("API 2", var0.okayAndConversionFunc, var0::lambda$solveCaptcha$6, var0.buildReqBody(var17, var0.performance));
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var18 = (Buffer)var10000.join();
            var10000 = VertxUtil.hardCodedSleep(var0.waitTime);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var10000.join();
            var10000 = var0.execute("Sensor 2/4", okayStatusFunc, var0::bundleReq, var18);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var19 = (String)var10000.join();
            var10000 = var0.execute("API 3", var0.okayAndConversionFunc, var0::lambda$solveCaptcha$7, var0.buildReqBody(var17, var0.performance));
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var20 = (Buffer)var10000.join();
            var10000 = VertxUtil.hardCodedSleep(var0.waitTime);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var10000.join();
            var10000 = var0.execute("Sensor 3/4", okayStatusFunc, var0::lambda$solveCaptcha$8, (Object)null);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var21 = (String)var10000.join();
            var10000 = var0.execute("API 4", var0.okayAndConversionFunc, var0::lambda$solveCaptcha$9, var0.buildReqBody(var17, var0.performance));
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var22 = (Buffer)var10000.join();
            var10000 = VertxUtil.hardCodedSleep(var0.waitTime);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var10000.join();
            var10000 = var0.execute("Sensor 4/4", okayStatusFunc, var0::bundleReq, var22);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }
            break;
         case 3:
            var16 = var6;
            var17 = (String)var5.join();
            var10000 = var0.execute("API 2", var0.okayAndConversionFunc, var0::lambda$solveCaptcha$6, var0.buildReqBody(var17, var0.performance));
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var18 = (Buffer)var10000.join();
            var10000 = VertxUtil.hardCodedSleep(var0.waitTime);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var10000.join();
            var10000 = var0.execute("Sensor 2/4", okayStatusFunc, var0::bundleReq, var18);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var19 = (String)var10000.join();
            var10000 = var0.execute("API 3", var0.okayAndConversionFunc, var0::lambda$solveCaptcha$7, var0.buildReqBody(var17, var0.performance));
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var20 = (Buffer)var10000.join();
            var10000 = VertxUtil.hardCodedSleep(var0.waitTime);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var10000.join();
            var10000 = var0.execute("Sensor 3/4", okayStatusFunc, var0::lambda$solveCaptcha$8, (Object)null);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var21 = (String)var10000.join();
            var10000 = var0.execute("API 4", var0.okayAndConversionFunc, var0::lambda$solveCaptcha$9, var0.buildReqBody(var17, var0.performance));
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var22 = (Buffer)var10000.join();
            var10000 = VertxUtil.hardCodedSleep(var0.waitTime);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var10000.join();
            var10000 = var0.execute("Sensor 4/4", okayStatusFunc, var0::bundleReq, var22);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }
            break;
         case 4:
            var17 = var7;
            var16 = var6;
            var18 = (Buffer)var5.join();
            var10000 = VertxUtil.hardCodedSleep(var0.waitTime);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var10000.join();
            var10000 = var0.execute("Sensor 2/4", okayStatusFunc, var0::bundleReq, var18);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var19 = (String)var10000.join();
            var10000 = var0.execute("API 3", var0.okayAndConversionFunc, var0::lambda$solveCaptcha$7, var0.buildReqBody(var7, var0.performance));
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var20 = (Buffer)var10000.join();
            var10000 = VertxUtil.hardCodedSleep(var0.waitTime);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var10000.join();
            var10000 = var0.execute("Sensor 3/4", okayStatusFunc, var0::lambda$solveCaptcha$8, (Object)null);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var21 = (String)var10000.join();
            var10000 = var0.execute("API 4", var0.okayAndConversionFunc, var0::lambda$solveCaptcha$9, var0.buildReqBody(var7, var0.performance));
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var22 = (Buffer)var10000.join();
            var10000 = VertxUtil.hardCodedSleep(var0.waitTime);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var10000.join();
            var10000 = var0.execute("Sensor 4/4", okayStatusFunc, var0::bundleReq, var22);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }
            break;
         case 5:
            var18 = var8;
            var17 = var7;
            var16 = var6;
            var5.join();
            var10000 = var0.execute("Sensor 2/4", okayStatusFunc, var0::bundleReq, var8);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var19 = (String)var10000.join();
            var10000 = var0.execute("API 3", var0.okayAndConversionFunc, var0::lambda$solveCaptcha$7, var0.buildReqBody(var7, var0.performance));
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var20 = (Buffer)var10000.join();
            var10000 = VertxUtil.hardCodedSleep(var0.waitTime);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var10000.join();
            var10000 = var0.execute("Sensor 3/4", okayStatusFunc, var0::lambda$solveCaptcha$8, (Object)null);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var21 = (String)var10000.join();
            var10000 = var0.execute("API 4", var0.okayAndConversionFunc, var0::lambda$solveCaptcha$9, var0.buildReqBody(var7, var0.performance));
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var22 = (Buffer)var10000.join();
            var10000 = VertxUtil.hardCodedSleep(var0.waitTime);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var10000.join();
            var10000 = var0.execute("Sensor 4/4", okayStatusFunc, var0::bundleReq, var22);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }
            break;
         case 6:
            var18 = var8;
            var17 = var7;
            var16 = var6;
            var19 = (String)var5.join();
            var10000 = var0.execute("API 3", var0.okayAndConversionFunc, var0::lambda$solveCaptcha$7, var0.buildReqBody(var7, var0.performance));
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var20 = (Buffer)var10000.join();
            var10000 = VertxUtil.hardCodedSleep(var0.waitTime);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var10000.join();
            var10000 = var0.execute("Sensor 3/4", okayStatusFunc, var0::lambda$solveCaptcha$8, (Object)null);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var21 = (String)var10000.join();
            var10000 = var0.execute("API 4", var0.okayAndConversionFunc, var0::lambda$solveCaptcha$9, var0.buildReqBody(var7, var0.performance));
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var22 = (Buffer)var10000.join();
            var10000 = VertxUtil.hardCodedSleep(var0.waitTime);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var10000.join();
            var10000 = var0.execute("Sensor 4/4", okayStatusFunc, var0::bundleReq, var22);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }
            break;
         case 7:
            var19 = var9;
            var18 = var8;
            var17 = var7;
            var16 = var6;
            var20 = (Buffer)var5.join();
            var10000 = VertxUtil.hardCodedSleep(var0.waitTime);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var10000.join();
            var10000 = var0.execute("Sensor 3/4", okayStatusFunc, var0::lambda$solveCaptcha$8, (Object)null);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var21 = (String)var10000.join();
            var10000 = var0.execute("API 4", var0.okayAndConversionFunc, var0::lambda$solveCaptcha$9, var0.buildReqBody(var7, var0.performance));
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var22 = (Buffer)var10000.join();
            var10000 = VertxUtil.hardCodedSleep(var0.waitTime);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var10000.join();
            var10000 = var0.execute("Sensor 4/4", okayStatusFunc, var0::bundleReq, var22);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }
            break;
         case 8:
            var20 = var10;
            var19 = var9;
            var18 = var8;
            var17 = var7;
            var16 = var6;
            var5.join();
            var10000 = var0.execute("Sensor 3/4", okayStatusFunc, var0::lambda$solveCaptcha$8, (Object)null);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var21 = (String)var10000.join();
            var10000 = var0.execute("API 4", var0.okayAndConversionFunc, var0::lambda$solveCaptcha$9, var0.buildReqBody(var7, var0.performance));
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var22 = (Buffer)var10000.join();
            var10000 = VertxUtil.hardCodedSleep(var0.waitTime);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var10000.join();
            var10000 = var0.execute("Sensor 4/4", okayStatusFunc, var0::bundleReq, var22);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }
            break;
         case 9:
            var20 = var10;
            var19 = var9;
            var18 = var8;
            var17 = var7;
            var16 = var6;
            var21 = (String)var5.join();
            var10000 = var0.execute("API 4", var0.okayAndConversionFunc, var0::lambda$solveCaptcha$9, var0.buildReqBody(var7, var0.performance));
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var22 = (Buffer)var10000.join();
            var10000 = VertxUtil.hardCodedSleep(var0.waitTime);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var10000.join();
            var10000 = var0.execute("Sensor 4/4", okayStatusFunc, var0::bundleReq, var22);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }
            break;
         case 10:
            var21 = var11;
            var20 = var10;
            var19 = var9;
            var18 = var8;
            var17 = var7;
            var16 = var6;
            var22 = (Buffer)var5.join();
            var10000 = VertxUtil.hardCodedSleep(var0.waitTime);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var10000.join();
            var10000 = var0.execute("Sensor 4/4", okayStatusFunc, var0::bundleReq, var22);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }
            break;
         case 11:
            var22 = var12;
            var21 = var11;
            var20 = var10;
            var19 = var9;
            var18 = var8;
            var17 = var7;
            var16 = var6;
            var5.join();
            var10000 = var0.execute("Sensor 4/4", okayStatusFunc, var0::bundleReq, var12);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }
            break;
         case 12:
            var10000 = var5;
            var22 = var12;
            var21 = var11;
            var20 = var10;
            var19 = var9;
            var18 = var8;
            var17 = var7;
            var16 = var6;
            break;
         case 13:
            var5.join();
            ++var4;
            if (var4 > ThreadLocalRandom.current().nextInt(1, 4)) {
               var0.delegate.getCookies().removeAnyMatch("_pxhd");
               return CompletableFuture.completedFuture(MultiMap.caseInsensitiveMultiMap());
            }

            var0.performance = null;
            var10000 = var0.execute("API 1", var0.okayAndConversionFunc, var0::lambda$solveCaptcha$5, var0.buildReqBody((String)null, (JsonArray)null));
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var16 = (Buffer)var10000.join();
            var10000 = VertxUtil.hardCodedSleep(var0.waitTime);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var10000.join();
            var10000 = var0.execute("Sensor 1/4", okayStatusFunc, var0::bundleReq, var16);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var17 = (String)var10000.join();
            var10000 = var0.execute("API 2", var0.okayAndConversionFunc, var0::lambda$solveCaptcha$6, var0.buildReqBody(var17, var0.performance));
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var18 = (Buffer)var10000.join();
            var10000 = VertxUtil.hardCodedSleep(var0.waitTime);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var10000.join();
            var10000 = var0.execute("Sensor 2/4", okayStatusFunc, var0::bundleReq, var18);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var19 = (String)var10000.join();
            var10000 = var0.execute("API 3", var0.okayAndConversionFunc, var0::lambda$solveCaptcha$7, var0.buildReqBody(var17, var0.performance));
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var20 = (Buffer)var10000.join();
            var10000 = VertxUtil.hardCodedSleep(var0.waitTime);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var10000.join();
            var10000 = var0.execute("Sensor 3/4", okayStatusFunc, var0::lambda$solveCaptcha$8, (Object)null);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var21 = (String)var10000.join();
            var10000 = var0.execute("API 4", var0.okayAndConversionFunc, var0::lambda$solveCaptcha$9, var0.buildReqBody(var17, var0.performance));
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var22 = (Buffer)var10000.join();
            var10000 = VertxUtil.hardCodedSleep(var0.waitTime);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }

            var10000.join();
            var10000 = var0.execute("Sensor 4/4", okayStatusFunc, var0::bundleReq, var22);
            if (!var10000.isDone()) {
               var25 = var10000;
               return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
            }
            break;
         default:
            throw new IllegalArgumentException();
      }

      do {
         String var23 = (String)var10000.join();
         if (var23.contains("cv|0")) {
            var0.logger.info("Successfully solved captcha");
            MultiMap var24 = MultiMap.caseInsensitiveMultiMap();
            if (var0.getVid() != null && !var0.getVid().isBlank()) {
               var24.add(VID_COOKIE, var0.getVid());
            }

            var24.add(RF_VALUE, ONE_VALUE);
            var24.add(FP_VALUE, ONE_VALUE);
            var24.add(CFP_VALUE, ONE_VALUE);
            var0.parseResultCookies(var23, var24);
            return CompletableFuture.completedFuture(var24);
         }

         var0.logger.error("Retrying captcha solve...");
         var0.sid = null;
         var10000 = VertxUtil.randomSleep(1000L);
         if (!var10000.isDone()) {
            var25 = var10000;
            return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
         }

         var10000.join();
         ++var4;
         if (var4 > ThreadLocalRandom.current().nextInt(1, 4)) {
            var0.delegate.getCookies().removeAnyMatch("_pxhd");
            return CompletableFuture.completedFuture(MultiMap.caseInsensitiveMultiMap());
         }

         var0.performance = null;
         var10000 = var0.execute("API 1", var0.okayAndConversionFunc, var0::lambda$solveCaptcha$5, var0.buildReqBody((String)null, (JsonArray)null));
         if (!var10000.isDone()) {
            var25 = var10000;
            return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
         }

         var16 = (Buffer)var10000.join();
         var10000 = VertxUtil.hardCodedSleep(var0.waitTime);
         if (!var10000.isDone()) {
            var25 = var10000;
            return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
         }

         var10000.join();
         var10000 = var0.execute("Sensor 1/4", okayStatusFunc, var0::bundleReq, var16);
         if (!var10000.isDone()) {
            var25 = var10000;
            return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
         }

         var17 = (String)var10000.join();
         var10000 = var0.execute("API 2", var0.okayAndConversionFunc, var0::lambda$solveCaptcha$6, var0.buildReqBody(var17, var0.performance));
         if (!var10000.isDone()) {
            var25 = var10000;
            return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
         }

         var18 = (Buffer)var10000.join();
         var10000 = VertxUtil.hardCodedSleep(var0.waitTime);
         if (!var10000.isDone()) {
            var25 = var10000;
            return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
         }

         var10000.join();
         var10000 = var0.execute("Sensor 2/4", okayStatusFunc, var0::bundleReq, var18);
         if (!var10000.isDone()) {
            var25 = var10000;
            return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
         }

         var19 = (String)var10000.join();
         var10000 = var0.execute("API 3", var0.okayAndConversionFunc, var0::lambda$solveCaptcha$7, var0.buildReqBody(var17, var0.performance));
         if (!var10000.isDone()) {
            var25 = var10000;
            return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
         }

         var20 = (Buffer)var10000.join();
         var10000 = VertxUtil.hardCodedSleep(var0.waitTime);
         if (!var10000.isDone()) {
            var25 = var10000;
            return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
         }

         var10000.join();
         var10000 = var0.execute("Sensor 3/4", okayStatusFunc, var0::lambda$solveCaptcha$8, (Object)null);
         if (!var10000.isDone()) {
            var25 = var10000;
            return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
         }

         var21 = (String)var10000.join();
         var10000 = var0.execute("API 4", var0.okayAndConversionFunc, var0::lambda$solveCaptcha$9, var0.buildReqBody(var17, var0.performance));
         if (!var10000.isDone()) {
            var25 = var10000;
            return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
         }

         var22 = (Buffer)var10000.join();
         var10000 = VertxUtil.hardCodedSleep(var0.waitTime);
         if (!var10000.isDone()) {
            var25 = var10000;
            return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
         }

         var10000.join();
         var10000 = var0.execute("Sensor 4/4", okayStatusFunc, var0::bundleReq, var22);
      } while(var10000.isDone());

      var25 = var10000;
      return var25.exceptionally(Function.identity()).thenCompose(DesktopPX::async$solveCaptcha);
   }

   public HttpRequest lambda$solveCaptcha$9() {
      return this.apiRequest("3", true);
   }

   public String getDeviceSecUA() {
      return this.secUA == null ? DEFAULT_SEC_UA.toString() : this.secUA;
   }

   public static CompletableFuture async$initialise(DesktopPX var0, CompletableFuture var1, int var2, Object var3) {
      CompletableFuture var10000;
      switch (var2) {
         case 0:
            var10000 = var0.execute("Initiating...", var0.setupDevice, var0::lambda$initialise$10, (Object)null);
            if (!var10000.isDone()) {
               var1 = var10000;
               return var1.exceptionally(Function.identity()).thenCompose(DesktopPX::async$initialise);
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

   public String getDeviceAcceptEncoding() {
      return "gzip, deflate, br";
   }

   public HttpRequest lambda$solveCaptcha$6() {
      return this.apiRequest("2", true);
   }

   public String getDeviceLang() {
      return "en-US,en;q=0.9";
   }

   public boolean isBool(JsonObject var1) {
      Object var2 = var1.getValue("error", (Object)null);
      return var2 != null ? var2 instanceof Boolean : false;
   }

   public CompletableFuture initialise() {
      CompletableFuture var10000 = this.execute("Initiating...", this.setupDevice, this::lambda$initialise$10, (Object)null);
      if (!var10000.isDone()) {
         CompletableFuture var1 = var10000;
         return var1.exceptionally(Function.identity()).thenCompose(DesktopPX::async$initialise);
      } else {
         var10000.join();
         return CompletableFuture.completedFuture(true);
      }
   }

   public String getDeviceUA() {
      return this.userAgent == null ? DEFAULT_UA.toString() : this.userAgent;
   }

   public HttpRequest apiRequest(String var1, Boolean var2) {
      HttpRequest var3 = VertxSingleton.INSTANCE.getLocalClient().getClient().postAbs("https://trickle-px-oygn7nn37q-uc.a.run.app/gen/" + var1 + ".json").as(BodyCodec.buffer()).addQueryParam("mobile", "false");
      if (var2 != null) {
         var3.addQueryParam("captcha", var2 ? "hold" : "recaptcha");
      }

      return var3;
   }

   static {
      site = Site.WALMART;
      okayStatusFunc = DesktopPX::lambda$static$0;
   }

   public HttpRequest lambda$solve$3() {
      return this.apiRequest("1", (Boolean)null);
   }

   public HttpRequest lambda$solveCaptcha$5() {
      return this.apiRequest("1", true);
   }

   public void reset() {
      this.cookieSession = new JsonObject();
      this.userAgent = null;
      this.deviceNumber = "undefined";
   }
}
