package io.trickle.task.antibot.impl.px;

import io.trickle.core.Controller;
import io.trickle.core.actor.TaskActor;
import io.trickle.task.antibot.impl.px.payload.captcha.PXCaptcha;
import io.trickle.task.antibot.impl.px.payload.token.FirstPayload;
import io.trickle.task.antibot.impl.px.payload.token.InitPayload;
import io.trickle.task.antibot.impl.px.payload.token.SecondPayload;
import io.trickle.task.sites.Site;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.trickle.webclient.RealClient;
import io.trickle.webclient.RealClientFactory;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.ProxyOptions;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.codec.BodyCodec;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.WeakHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PXTokenAPI2 extends PXTokenBase {
   public PXCaptcha captchaHandler;
   public InitPayload initPayload;
   public long restartTime;
   public boolean hasVidSolved;
   public int expiryCount;
   public String userAgent;
   public int failedCaptchaSolves;
   public long expiryTime;
   public int rotates;
   public String deviceNumber;
   public boolean stopKeepalive = false;
   public JsonObject cookieSesion;
   public SecondPayload secondPayload;
   public long timer;
   public boolean isTokenCaptcha;
   public long requestTime;
   public static Pattern BAKE_PATTERN = Pattern.compile("bake\\|.*?\\|.*?\\|(.*?)\\|");
   public boolean isFirstTime;
   public Site SITE;

   public void startTimer() {
      this.stopTimerEager();
      this.restartClient(this.client);
      if (super.vertx != null) {
         this.timer = super.vertx.setTimer((this.expiryTime - Instant.now().getEpochSecond()) * 1000L, this::lambda$startTimer$0);
      }
   }

   public void setTokenCaptcha(boolean var1) {
      this.isTokenCaptcha = var1;
   }

   public boolean isTokenCaptcha() {
      return this.isTokenCaptcha;
   }

   public void setValue(String var1) {
      super.value = var1;
   }

   public HttpRequest apiRequest() {
      return this.client.getAbs("http://94.16.107.91/gen").as(BodyCodec.jsonObject()).addQueryParam("authToken", "PX-2598A000-3595-4305-9244-7C6940349759").addQueryParam("site", this.SITE.toString().toLowerCase()).addQueryParam("region", "com").addQueryParam("proxy", this.getProxyString()).addQueryParam("deviceNumber", this.deviceNumber);
   }

   public String getDeviceUA() {
      return this.userAgent;
   }

   public String getVid() {
      return this.secondPayload.VID_HEADER;
   }

   public static CompletableFuture async$sendPayload(PXTokenAPI2 param0, MultiMap param1, HttpRequest param2, long param3, CompletableFuture param5, HttpResponse param6, int param7, Object param8) {
      // $FF: Couldn't be decompiled
   }

   public Optional parseResult(String var1) {
      Matcher var2 = BAKE_PATTERN.matcher(var1);
      return var2.find() ? Optional.of("3:" + var2.group(1)) : Optional.empty();
   }

   public CompletableFuture initialize() {
      this.logger.info("Fast init of API[2]");
      return CompletableFuture.completedFuture(true);
   }

   public CompletableFuture solveCaptchaDesktop(String var1, String var2, String var3) {
      CompletableFuture var10000 = VertxUtil.hardCodedSleep(7000L);
      CompletableFuture var6;
      if (!var10000.isDone()) {
         var6 = var10000;
         return var6.exceptionally(Function.identity()).thenCompose(PXTokenAPI2::async$solveCaptchaDesktop);
      } else {
         var10000.join();
         this.captchaHandler.updateVIDandUUID(this.client, var1, var2);
         this.captchaHandler.setType(Types.CAPTCHA_DESKTOP);
         var10000 = this.captchaHandler.solveCaptcha(var3);
         if (!var10000.isDone()) {
            var6 = var10000;
            return var6.exceptionally(Function.identity()).thenCompose(PXTokenAPI2::async$solveCaptchaDesktop);
         } else {
            String var4 = (String)var10000.join();
            if (var4 != null) {
               this.setExpiryTimeAfterCap();
               this.value = var4;
               this.isTokenCaptcha = true;
               this.hasVidSolved = true;
               this.rotates = 0;
               this.logger.info("Solved captcha successfully!");
               WeakHashMap var5 = new WeakHashMap();
               var5.put("_pxff_rf", "1");
               var5.put("_pxff_fp", "1");
               var5.put("_pxff_cfp", "1");
               var5.put("_pxvid", this.captchaHandler.getParentVID());
               PXCaptcha.parseCookiesFromResp(var4, var5);
               if (!var5.isEmpty()) {
                  this.client.close();
                  return CompletableFuture.completedFuture(var5);
               } else {
                  return CompletableFuture.completedFuture((Object)null);
               }
            } else {
               this.logger.warn("Failed to solve captcha!");
               ++this.failedCaptchaSolves;
               if (this.failedCaptchaSolves >= 4) {
                  this.rotates = 0;
                  this.failedCaptchaSolves = 0;
                  this.rotateProxy();
                  var10000 = this.initialize();
                  if (!var10000.isDone()) {
                     var6 = var10000;
                     return var6.exceptionally(Function.identity()).thenCompose(PXTokenAPI2::async$solveCaptchaDesktop);
                  }

                  var10000.join();
                  this.restartTime = System.currentTimeMillis();
               }

               return CompletableFuture.completedFuture((Object)null);
            }
         }
      }
   }

   public void setExpiryTime() {
      this.expiryTime = (long)((double)Instant.now().getEpochSecond() + Double.longBitsToDouble(4642627155242306765L));
      this.startTimer();
   }

   public CompletableFuture solveCaptchaMobile(String var1, String var2, String var3) {
      this.captchaHandler.updateVIDandUUID(this.client, var1, var2);
      this.captchaHandler.setType(Types.CAPTCHA_MOBILE);
      this.stopTimerEager();
      CompletableFuture var10000;
      String var4;
      CompletableFuture var5;
      if (this.SITE == Site.WALMART) {
         if (this.isFirstTime) {
            var10000 = VertxUtil.hardCodedSleep(15000L);
            if (!var10000.isDone()) {
               var5 = var10000;
               return var5.exceptionally(Function.identity()).thenCompose(PXTokenAPI2::async$solveCaptchaMobile);
            }

            var10000.join();
            this.isFirstTime = false;
         }

         var10000 = this.captchaHandler.solveCaptcha(var3);
         if (!var10000.isDone()) {
            var5 = var10000;
            return var5.exceptionally(Function.identity()).thenCompose(PXTokenAPI2::async$solveCaptchaMobile);
         }

         var4 = (String)var10000.join();
         if (var4 != null) {
            this.setExpiryTimeAfterCap();
            this.value = var4;
            this.isTokenCaptcha = true;
            this.hasVidSolved = true;
            this.rotates = 0;
            this.logger.info("Solved captcha successfully!");
            this.logger.info((String)this.value);
            var10000 = VertxUtil.hardCodedSleep(2222L);
            if (!var10000.isDone()) {
               var5 = var10000;
               return var5.exceptionally(Function.identity()).thenCompose(PXTokenAPI2::async$solveCaptchaMobile);
            }

            var10000.join();
            return CompletableFuture.completedFuture(true);
         }

         this.logger.warn("Failed to solve captcha!");
         ++this.failedCaptchaSolves;
         if (this.failedCaptchaSolves >= 4) {
            this.rotates = 0;
            this.failedCaptchaSolves = 0;
            this.rotateProxy();
            var10000 = this.initialize();
            if (!var10000.isDone()) {
               var5 = var10000;
               return var5.exceptionally(Function.identity()).thenCompose(PXTokenAPI2::async$solveCaptchaMobile);
            }

            var10000.join();
            this.restartTime = System.currentTimeMillis();
         }
      } else {
         var10000 = this.captchaHandler.solveCaptcha(var3);
         if (!var10000.isDone()) {
            var5 = var10000;
            return var5.exceptionally(Function.identity()).thenCompose(PXTokenAPI2::async$solveCaptchaMobile);
         }

         var4 = (String)var10000.join();
         if (var4 != null) {
            this.setExpiryTimeAfterCap();
            this.value = var4;
            this.isTokenCaptcha = true;
            this.restartTime = 0L;
            this.logger.info("Sent sensor successfully!");
            return CompletableFuture.completedFuture(true);
         }

         this.logger.warn("Failed to send sensor!");
         var10000 = this.initialize();
         if (!var10000.isDone()) {
            var5 = var10000;
            return var5.exceptionally(Function.identity()).thenCompose(PXTokenAPI2::async$solveCaptchaMobile);
         }

         var10000.join();
      }

      return CompletableFuture.completedFuture(false);
   }

   public CompletableFuture initBrowserDevice(boolean var1) {
      return CompletableFuture.completedFuture((Object)null);
   }

   public String getDeviceSecUA() {
      return " Not;A Brand\";v=\"99\", \"Google Chrome\";v=\"91\", \"Chromium\";v=\"91";
   }

   public String getProxyString() {
      ProxyOptions var1 = this.client.getOptions().getProxyOptions();
      if (var1 == null) {
         return "";
      } else {
         String var10000 = var1.getUsername();
         return "http://" + var10000 + ":" + var1.getPassword() + "@" + var1.getHost() + ":" + var1.getPort();
      }
   }

   public PXTokenAPI2(TaskActor var1, Site var2) {
      super(var1);
      this.stopKeepalive = false;
      this.failedCaptchaSolves = 0;
      this.rotates = 0;
      this.restartTime = 0L;
      this.userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko)";
      this.deviceNumber = "undefined";
      this.isFirstTime = true;
      this.requestTime = 0L;
      this.SITE = var2;
   }

   public static CompletableFuture async$solveCaptchaMobile(PXTokenAPI2 var0, String var1, String var2, String var3, CompletableFuture var4, String var5, int var6, Object var7) {
      CompletableFuture var10000;
      label103: {
         label104: {
            label85: {
               String var8;
               CompletableFuture var9;
               label94: {
                  label74: {
                     label73: {
                        switch (var6) {
                           case 0:
                              var0.captchaHandler.updateVIDandUUID(var0.client, var1, var2);
                              var0.captchaHandler.setType(Types.CAPTCHA_MOBILE);
                              var0.stopTimerEager();
                              if (var0.SITE != Site.WALMART) {
                                 var10000 = var0.captchaHandler.solveCaptcha(var3);
                                 if (!var10000.isDone()) {
                                    var9 = var10000;
                                    return var9.exceptionally(Function.identity()).thenCompose(PXTokenAPI2::async$solveCaptchaMobile);
                                 }
                                 break label74;
                              }

                              if (!var0.isFirstTime) {
                                 break label73;
                              }

                              var10000 = VertxUtil.hardCodedSleep(15000L);
                              if (!var10000.isDone()) {
                                 var9 = var10000;
                                 return var9.exceptionally(Function.identity()).thenCompose(PXTokenAPI2::async$solveCaptchaMobile);
                              }
                              break;
                           case 1:
                              var10000 = var4;
                              break;
                           case 2:
                              var10000 = var4;
                              break label94;
                           case 3:
                              var10000 = var4;
                              break label103;
                           case 4:
                              var10000 = var4;
                              break label104;
                           case 5:
                              var10000 = var4;
                              break label74;
                           case 6:
                              var10000 = var4;
                              break label85;
                           default:
                              throw new IllegalArgumentException();
                        }

                        var10000.join();
                        var0.isFirstTime = false;
                     }

                     var10000 = var0.captchaHandler.solveCaptcha(var3);
                     if (!var10000.isDone()) {
                        var9 = var10000;
                        return var9.exceptionally(Function.identity()).thenCompose(PXTokenAPI2::async$solveCaptchaMobile);
                     }
                     break label94;
                  }

                  var8 = (String)var10000.join();
                  if (var8 != null) {
                     var0.setExpiryTimeAfterCap();
                     var0.value = var8;
                     var0.isTokenCaptcha = true;
                     var0.restartTime = 0L;
                     var0.logger.info("Sent sensor successfully!");
                     return CompletableFuture.completedFuture(true);
                  }

                  var0.logger.warn("Failed to send sensor!");
                  var10000 = var0.initialize();
                  if (!var10000.isDone()) {
                     var9 = var10000;
                     return var9.exceptionally(Function.identity()).thenCompose(PXTokenAPI2::async$solveCaptchaMobile);
                  }
                  break label85;
               }

               var8 = (String)var10000.join();
               if (var8 != null) {
                  var0.setExpiryTimeAfterCap();
                  var0.value = var8;
                  var0.isTokenCaptcha = true;
                  var0.hasVidSolved = true;
                  var0.rotates = 0;
                  var0.logger.info("Solved captcha successfully!");
                  var0.logger.info((String)var0.value);
                  var10000 = VertxUtil.hardCodedSleep(2222L);
                  if (!var10000.isDone()) {
                     var9 = var10000;
                     return var9.exceptionally(Function.identity()).thenCompose(PXTokenAPI2::async$solveCaptchaMobile);
                  }
                  break label103;
               }

               var0.logger.warn("Failed to solve captcha!");
               ++var0.failedCaptchaSolves;
               if (var0.failedCaptchaSolves < 4) {
                  return CompletableFuture.completedFuture(false);
               }

               var0.rotates = 0;
               var0.failedCaptchaSolves = 0;
               var0.rotateProxy();
               var10000 = var0.initialize();
               if (!var10000.isDone()) {
                  var9 = var10000;
                  return var9.exceptionally(Function.identity()).thenCompose(PXTokenAPI2::async$solveCaptchaMobile);
               }
               break label104;
            }

            var10000.join();
            return CompletableFuture.completedFuture(false);
         }

         var10000.join();
         var0.restartTime = System.currentTimeMillis();
         return CompletableFuture.completedFuture(false);
      }

      var10000.join();
      return CompletableFuture.completedFuture(true);
   }

   public static CompletableFuture async$solveCaptchaDesktop(PXTokenAPI2 var0, String var1, String var2, String var3, CompletableFuture var4, String var5, int var6, Object var7) {
      CompletableFuture var10000;
      label44: {
         CompletableFuture var10;
         label39: {
            switch (var6) {
               case 0:
                  var10000 = VertxUtil.hardCodedSleep(7000L);
                  if (!var10000.isDone()) {
                     var10 = var10000;
                     return var10.exceptionally(Function.identity()).thenCompose(PXTokenAPI2::async$solveCaptchaDesktop);
                  }
                  break;
               case 1:
                  var10000 = var4;
                  break;
               case 2:
                  var10000 = var4;
                  break label39;
               case 3:
                  var10000 = var4;
                  break label44;
               default:
                  throw new IllegalArgumentException();
            }

            var10000.join();
            var0.captchaHandler.updateVIDandUUID(var0.client, var1, var2);
            var0.captchaHandler.setType(Types.CAPTCHA_DESKTOP);
            var10000 = var0.captchaHandler.solveCaptcha(var3);
            if (!var10000.isDone()) {
               var10 = var10000;
               return var10.exceptionally(Function.identity()).thenCompose(PXTokenAPI2::async$solveCaptchaDesktop);
            }
         }

         String var8 = (String)var10000.join();
         if (var8 != null) {
            var0.setExpiryTimeAfterCap();
            var0.value = var8;
            var0.isTokenCaptcha = true;
            var0.hasVidSolved = true;
            var0.rotates = 0;
            var0.logger.info("Solved captcha successfully!");
            WeakHashMap var9 = new WeakHashMap();
            var9.put("_pxff_rf", "1");
            var9.put("_pxff_fp", "1");
            var9.put("_pxff_cfp", "1");
            var9.put("_pxvid", var0.captchaHandler.getParentVID());
            PXCaptcha.parseCookiesFromResp(var8, var9);
            if (!var9.isEmpty()) {
               var0.client.close();
               return CompletableFuture.completedFuture(var9);
            }

            return CompletableFuture.completedFuture((Object)null);
         }

         var0.logger.warn("Failed to solve captcha!");
         ++var0.failedCaptchaSolves;
         if (var0.failedCaptchaSolves < 4) {
            return CompletableFuture.completedFuture((Object)null);
         }

         var0.rotates = 0;
         var0.failedCaptchaSolves = 0;
         var0.rotateProxy();
         var10000 = var0.initialize();
         if (!var10000.isDone()) {
            var10 = var10000;
            return var10.exceptionally(Function.identity()).thenCompose(PXTokenAPI2::async$solveCaptchaDesktop);
         }
      }

      var10000.join();
      var0.restartTime = System.currentTimeMillis();
      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture solveCaptcha(String var1, String var2) {
      return this.solveCaptchaMobile(var1, var2, (String)null);
   }

   public boolean hasExpiredOnce() {
      return this.expiryCount > 0;
   }

   public void rotateProxy() {
      try {
         RealClient var1 = RealClientFactory.rotateProxy(Vertx.currentContext().owner(), this.client, Controller.PROXY_RESIDENTIAL);
         this.client.close();
         this.client = var1;
      } catch (Throwable var2) {
      }

   }

   public void rotateAndRegen() {
      this.rotateProxy();
      this.initialize();
   }

   public CompletableFuture desktopSolve(String var1, String var2) {
      CompletableFuture var10000 = this.apiSolve();
      if (!var10000.isDone()) {
         CompletableFuture var3 = var10000;
         return var3.exceptionally(Function.identity()).thenCompose(PXTokenAPI2::async$desktopSolve);
      } else {
         return CompletableFuture.completedFuture((Map)var10000.join());
      }
   }

   public static CompletableFuture async$sendInit(PXTokenAPI2 param0, InitPayload param1, HttpRequest param2, CompletableFuture param3, HttpResponse param4, int param5, Object param6) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture awaitInit() {
      return super.initFuture;
   }

   public String getDeviceSecUAMobile() {
      return "?0";
   }

   public static CompletableFuture async$checkOrUpdate(PXTokenAPI2 var0, PXTokenAPI2 var1, CompletableFuture var2, Optional var3, int var4, Object var5) {
      Exception var14;
      label52: {
         boolean var15;
         CompletableFuture var10000;
         label56: {
            PXTokenAPI2 var13;
            CompletableFuture var10001;
            switch (var4) {
               case 0:
                  if (!var0.isExpired()) {
                     return CompletableFuture.completedFuture((Object)null);
                  }

                  ++var0.expiryCount;

                  try {
                     var13 = var0;
                     var10001 = var0.sendPayload(var0.secondPayload.asKeepAliveForm());
                     if (!var10001.isDone()) {
                        CompletableFuture var12 = var10001;
                        return var12.exceptionally(Function.identity()).thenCompose(PXTokenAPI2::async$checkOrUpdate);
                     }
                     break;
                  } catch (Exception var8) {
                     var14 = var8;
                     var15 = false;
                     break label52;
                  }
               case 1:
                  var13 = var1;
                  var10001 = var2;
                  break;
               case 2:
                  var10000 = var2;
                  break label56;
               default:
                  throw new IllegalArgumentException();
            }

            Optional var10;
            try {
               var10 = var13.parseResult(((JsonObject)var10001.join()).toString());
               if (var10.isPresent()) {
                  var0.setExpiryTime();
                  var0.value = var10.get();
                  var0.isTokenCaptcha = false;
                  return CompletableFuture.completedFuture((Object)null);
               }
            } catch (Exception var9) {
               var14 = var9;
               var15 = false;
               break label52;
            }

            try {
               var10000 = VertxUtil.randomSleep(10000L);
               if (!var10000.isDone()) {
                  var2 = var10000;
                  return var2.exceptionally(Function.identity()).thenCompose(PXTokenAPI2::async$checkOrUpdate);
               }
            } catch (Exception var7) {
               var14 = var7;
               var15 = false;
               break label52;
            }
         }

         try {
            var10000.join();
            return var0.checkOrUpdate();
         } catch (Exception var6) {
            var14 = var6;
            var15 = false;
         }
      }

      Exception var11 = var14;
      var0.logger.error("Unable to keep-alive post: " + var11.getMessage());
      return CompletableFuture.completedFuture((Object)null);
   }

   public String getDeviceLang() {
      return "en-GB,en;q=0.9,en-US;q=0.8,lt;q=0.7";
   }

   public static CompletableFuture async$desktopSolve(PXTokenAPI2 var0, String var1, String var2, CompletableFuture var3, int var4, Object var5) {
      CompletableFuture var10000;
      switch (var4) {
         case 0:
            var10000 = var0.apiSolve();
            if (!var10000.isDone()) {
               var3 = var10000;
               return var3.exceptionally(Function.identity()).thenCompose(PXTokenAPI2::async$desktopSolve);
            }
            break;
         case 1:
            var10000 = var3;
            break;
         default:
            throw new IllegalArgumentException();
      }

      return CompletableFuture.completedFuture((Map)var10000.join());
   }

   public String getSid() {
      return this.secondPayload.SID_HEADER;
   }

   public CompletableFuture handleAfterCap() {
      try {
         FirstPayload var1 = new FirstPayload(this.secondPayload, this.secondPayload.sdkInitCount, this.requestTime, this.SITE);
         CompletableFuture var10000 = this.sendPayload(var1.asForm());
         CompletableFuture var4;
         if (!var10000.isDone()) {
            var4 = var10000;
            return var4.exceptionally(Function.identity()).thenCompose(PXTokenAPI2::async$handleAfterCap);
         }

         JsonObject var2 = (JsonObject)var10000.join();
         this.secondPayload = new SecondPayload(var1, var2, this.requestTime, this.SITE);
         var10000 = this.sendPayload(this.secondPayload.asForm());
         if (!var10000.isDone()) {
            var4 = var10000;
            return var4.exceptionally(Function.identity()).thenCompose(PXTokenAPI2::async$handleAfterCap);
         }

         JsonObject var3 = (JsonObject)var10000.join();
      } catch (Throwable var5) {
         this.logger.warn("Error on A-CP: {}", var5.getMessage());
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$handleAfterCap(PXTokenAPI2 var0, FirstPayload var1, CompletableFuture var2, JsonObject var3, int var4, Object var5) {
      Throwable var12;
      label36: {
         CompletableFuture var10000;
         boolean var10001;
         label35: {
            CompletableFuture var11;
            switch (var4) {
               case 0:
                  try {
                     var1 = new FirstPayload(var0.secondPayload, var0.secondPayload.sdkInitCount, var0.requestTime, var0.SITE);
                     var10000 = var0.sendPayload(var1.asForm());
                     if (!var10000.isDone()) {
                        var11 = var10000;
                        return var11.exceptionally(Function.identity()).thenCompose(PXTokenAPI2::async$handleAfterCap);
                     }
                     break;
                  } catch (Throwable var8) {
                     var12 = var8;
                     var10001 = false;
                     break label36;
                  }
               case 1:
                  var10000 = var2;
                  break;
               case 2:
                  var10000 = var2;
                  break label35;
               default:
                  throw new IllegalArgumentException();
            }

            try {
               JsonObject var10 = (JsonObject)var10000.join();
               var0.secondPayload = new SecondPayload(var1, var10, var0.requestTime, var0.SITE);
               var10000 = var0.sendPayload(var0.secondPayload.asForm());
               if (!var10000.isDone()) {
                  var11 = var10000;
                  return var11.exceptionally(Function.identity()).thenCompose(PXTokenAPI2::async$handleAfterCap);
               }
            } catch (Throwable var7) {
               var12 = var7;
               var10001 = false;
               break label36;
            }
         }

         try {
            var3 = (JsonObject)var10000.join();
            return CompletableFuture.completedFuture((Object)null);
         } catch (Throwable var6) {
            var12 = var6;
            var10001 = false;
         }
      }

      Throwable var9 = var12;
      var0.logger.warn("Error on A-CP: {}", var9.getMessage());
      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture checkOrUpdate() {
      if (this.isExpired()) {
         ++this.expiryCount;

         try {
            CompletableFuture var10001 = this.sendPayload(this.secondPayload.asKeepAliveForm());
            if (!var10001.isDone()) {
               CompletableFuture var3 = var10001;
               return var3.exceptionally(Function.identity()).thenCompose(PXTokenAPI2::async$checkOrUpdate);
            }

            Optional var1 = this.parseResult(((JsonObject)var10001.join()).toString());
            if (var1.isPresent()) {
               this.setExpiryTime();
               super.value = var1.get();
               this.isTokenCaptcha = false;
               return CompletableFuture.completedFuture((Object)null);
            }

            CompletableFuture var10000 = VertxUtil.randomSleep(10000L);
            if (!var10000.isDone()) {
               CompletableFuture var2 = var10000;
               return var2.exceptionally(Function.identity()).thenCompose(PXTokenAPI2::async$checkOrUpdate);
            }

            var10000.join();
            return this.checkOrUpdate();
         } catch (Exception var4) {
            this.logger.error("Unable to keep-alive post: " + var4.getMessage());
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public String getDeviceAcceptEncoding() {
      return "gzip, deflate, br";
   }

   public void lambda$startTimer$0(Long var1) {
      Objects.requireNonNull(this);
      if (this.isExpired()) {
         this.checkOrUpdate();
      } else {
         this.retryCheck();
      }

   }

   public void stopTimerEager() {
      if (this.timer != 0L) {
         super.vertx.cancelTimer(this.timer);
      }

   }

   public CompletableFuture sendPayload(MultiMap var1) {
      HttpRequest var2 = this.collectorRequest();

      while(super.client.isActive()) {
         try {
            long var3 = System.currentTimeMillis();
            CompletableFuture var10000 = Request.send(var2, var1);
            CompletableFuture var6;
            if (!var10000.isDone()) {
               var6 = var10000;
               return var6.exceptionally(Function.identity()).thenCompose(PXTokenAPI2::async$sendPayload);
            }

            HttpResponse var5 = (HttpResponse)var10000.join();
            if (var5 != null) {
               this.requestTime = System.currentTimeMillis() - var3;
               if (this.secondPayload != null) {
                  this.secondPayload.updatePX349(this.requestTime);
               }

               return CompletableFuture.completedFuture((JsonObject)var5.body());
            }

            var10000 = VertxUtil.randomSleep(60000L);
            if (!var10000.isDone()) {
               var6 = var10000;
               return var6.exceptionally(Function.identity()).thenCompose(PXTokenAPI2::async$sendPayload);
            }

            var10000.join();
         } catch (Throwable var7) {
            break;
         }
      }

      return CompletableFuture.failedFuture(new Exception("Failed to send payload"));
   }

   public CompletableFuture apiSolve() {
      int var1 = 0;

      while(this.client.isActive() && var1++ <= 100) {
         this.logger.info("Genning...");

         try {
            CompletableFuture var8 = Request.send(this.apiRequest());
            if (!var8.isDone()) {
               CompletableFuture var6 = var8;
               return var6.exceptionally(Function.identity()).thenCompose(PXTokenAPI2::async$apiSolve);
            }

            HttpResponse var2 = (HttpResponse)var8.join();
            if (var2 != null) {
               JsonObject var3 = (JsonObject)var2.body();
               this.logger.info(var3);
               this.cookieSesion = var3.getJsonObject("data");
               this.userAgent = this.cookieSesion.getString("UserAgent");
               this.deviceNumber = this.cookieSesion.getString("deviceNumber");
               WeakHashMap var4 = new WeakHashMap();
               String var5 = var3.getString("cookie").split("=")[0];
               var4.put(var5, var3.getString("cookie").replace(var5 + "=", ""));
               return CompletableFuture.completedFuture(var4);
            }
         } catch (Throwable var7) {
            var7.printStackTrace();
         }
      }

      return CompletableFuture.completedFuture(new WeakHashMap());
   }

   public void setNeedsDesktopAppID(boolean var1) {
   }

   public void retryCheck() {
      super.vertx.setTimer(500L, this::lambda$retryCheck$1);
   }

   public CompletableFuture reInit() {
      this.deviceNumber = "undefined";
      return CompletableFuture.completedFuture(true);
   }

   public void lambda$retryCheck$1(Long var1) {
      Objects.requireNonNull(this);
      if (this.isExpired()) {
         this.checkOrUpdate();
      } else {
         this.retryCheck();
      }

   }

   public HttpRequest collectorRequest() {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$apiSolve(PXTokenAPI2 param0, int param1, CompletableFuture param2, int param3, Object param4) {
      // $FF: Couldn't be decompiled
   }

   public void setExpiryTimeAfterCap() {
      this.expiryTime = Instant.now().getEpochSecond() + 300L;
      this.startTimer();
   }

   public CompletableFuture sendInit(InitPayload var1) {
      HttpRequest var2 = this.initReq();

      while(this.client.isActive()) {
         try {
            CompletableFuture var10000 = Request.send(var2, var1.asBuffer(this.SITE));
            CompletableFuture var4;
            if (!var10000.isDone()) {
               var4 = var10000;
               return var4.exceptionally(Function.identity()).thenCompose(PXTokenAPI2::async$sendInit);
            }

            HttpResponse var3 = (HttpResponse)var10000.join();
            if (var3 != null) {
               return CompletableFuture.completedFuture((JsonObject)var3.body());
            }

            var10000 = VertxUtil.randomSleep(10000L);
            if (!var10000.isDone()) {
               var4 = var10000;
               return var4.exceptionally(Function.identity()).thenCompose(PXTokenAPI2::async$sendInit);
            }

            var10000.join();
         } catch (Throwable var5) {
            break;
         }
      }

      return CompletableFuture.failedFuture(new Exception("Failed to send payload"));
   }

   public boolean isExpired() {
      return Instant.now().getEpochSecond() >= this.expiryTime;
   }

   public HttpRequest initReq() {
      // $FF: Couldn't be decompiled
   }
}
