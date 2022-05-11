package io.trickle.task.antibot.impl.px;

import io.trickle.core.Controller;
import io.trickle.core.actor.TaskActor;
import io.trickle.task.antibot.impl.px.payload.captcha.PXCaptcha;
import io.trickle.task.antibot.impl.px.payload.token.Devices$Device;
import io.trickle.task.antibot.impl.px.payload.token.FirstPayload;
import io.trickle.task.antibot.impl.px.payload.token.InitPayload;
import io.trickle.task.antibot.impl.px.payload.token.SecondPayload;
import io.trickle.task.sites.Site;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.trickle.webclient.ClientType;
import io.trickle.webclient.RealClient;
import io.trickle.webclient.RealClientFactory;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PXToken extends PXTokenBase {
   public boolean stopKeepalive = false;
   public SecondPayload secondPayload;
   public PXCaptcha captchaHandler;
   public long restartTime = 0L;
   public int rotates = 0;
   public Site SITE;
   public static Pattern BAKE_PATTERN = Pattern.compile("bake\\|.*?\\|.*?\\|(.*?)\\|");
   public boolean isFirstTime = true;
   public long expiryTime;
   public boolean hasVidSolved;
   public boolean isTokenCaptcha;
   public int expiryCount;
   public long requestTime;
   public int failedCaptchaSolves = 0;
   public long timer;
   public InitPayload initPayload;

   public static CompletableFuture async$initialize(PXToken param0, CompletableFuture param1, int param2, PXToken param3, FirstPayload param4, JsonObject param5, Throwable param6, int param7, Object param8) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$checkOrUpdate(PXToken var0, PXToken var1, CompletableFuture var2, Optional var3, int var4, Object var5) {
      Exception var14;
      label52: {
         boolean var15;
         CompletableFuture var10000;
         label56: {
            PXToken var13;
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
                        return var12.exceptionally(Function.identity()).thenCompose(PXToken::async$checkOrUpdate);
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
                  return var2.exceptionally(Function.identity()).thenCompose(PXToken::async$checkOrUpdate);
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

   public boolean isExpired() {
      return Instant.now().getEpochSecond() >= this.expiryTime;
   }

   public String getDeviceAcceptEncoding() {
      return this.captchaHandler.getDevice().getAcceptEncoding();
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
               return var6.exceptionally(Function.identity()).thenCompose(PXToken::async$sendPayload);
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
               return var6.exceptionally(Function.identity()).thenCompose(PXToken::async$sendPayload);
            }

            var10000.join();
         } catch (Throwable var7) {
            break;
         }
      }

      return CompletableFuture.failedFuture(new Exception("Failed to send payload"));
   }

   public void setTokenCaptcha(boolean var1) {
      this.isTokenCaptcha = var1;
   }

   public CompletableFuture solveCaptchaDesktop(String var1, String var2, String var3) {
      CompletableFuture var10000 = VertxUtil.hardCodedSleep(7000L);
      if (!var10000.isDone()) {
         CompletableFuture var4 = var10000;
         return var4.exceptionally(Function.identity()).thenCompose(PXToken::async$solveCaptchaDesktop);
      } else {
         var10000.join();
         return CompletableFuture.completedFuture((Object)null);
      }
   }

   public CompletableFuture sendInit(InitPayload var1) {
      HttpRequest var2 = this.initReq();

      while(this.client.isActive()) {
         try {
            CompletableFuture var10000 = Request.send(var2, var1.asBuffer(this.SITE));
            CompletableFuture var4;
            if (!var10000.isDone()) {
               var4 = var10000;
               return var4.exceptionally(Function.identity()).thenCompose(PXToken::async$sendInit);
            }

            HttpResponse var3 = (HttpResponse)var10000.join();
            if (var3 != null) {
               return CompletableFuture.completedFuture((JsonObject)var3.body());
            }

            var10000 = VertxUtil.randomSleep(10000L);
            if (!var10000.isDone()) {
               var4 = var10000;
               return var4.exceptionally(Function.identity()).thenCompose(PXToken::async$sendInit);
            }

            var10000.join();
         } catch (Throwable var5) {
            break;
         }
      }

      return CompletableFuture.failedFuture(new Exception("Failed to send payload"));
   }

   public String getVid() {
      return this.secondPayload.VID_HEADER;
   }

   public CompletableFuture solveCaptcha(String var1, String var2) {
      return CompletableFuture.completedFuture((Object)null);
   }

   public HttpRequest collectorRequest() {
      // $FF: Couldn't be decompiled
   }

   public String getSid() {
      return this.secondPayload.SID_HEADER;
   }

   public boolean isTokenCaptcha() {
      return this.isTokenCaptcha;
   }

   public Optional parseResult(String var1) {
      Matcher var2 = BAKE_PATTERN.matcher(var1);
      return var2.find() ? Optional.of("3:" + var2.group(1)) : Optional.empty();
   }

   public HttpRequest initReq() {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture initialize() {
      CompletableFuture var10000 = this.initBrowserDevice(true);
      CompletableFuture var6;
      if (!var10000.isDone()) {
         var6 = var10000;
         return var6.exceptionally(Function.identity()).thenCompose(PXToken::async$initialize);
      } else {
         var10000.join();
         int var1 = 0;

         while(var1 < 5) {
            try {
               this.hasVidSolved = false;
               Object var10002 = null;
               CompletableFuture var10003 = InitPayload.getDeviceFromAPI();
               if (!var10003.isDone()) {
                  CompletableFuture var7 = var10003;
                  return var7.exceptionally(Function.identity()).thenCompose(PXToken::async$initialize);
               }

               Devices$Device var9 = (Devices$Device)var10003.join();
               this.initPayload = new InitPayload(var9);
               var10000 = this.sendInit(this.initPayload);
               if (!var10000.isDone()) {
                  var6 = var10000;
                  return var6.exceptionally(Function.identity()).thenCompose(PXToken::async$initialize);
               }

               var10000.join();
               FirstPayload var2 = new FirstPayload(this.initPayload, this.SITE);
               var10000 = this.sendPayload(var2.asForm());
               if (!var10000.isDone()) {
                  var6 = var10000;
                  return var6.exceptionally(Function.identity()).thenCompose(PXToken::async$initialize);
               }

               JsonObject var3 = (JsonObject)var10000.join();
               this.secondPayload = new SecondPayload(var2, var3, this.requestTime, this.SITE);
               var10000 = this.sendPayload(this.secondPayload.asForm());
               if (!var10000.isDone()) {
                  var6 = var10000;
                  return var6.exceptionally(Function.identity()).thenCompose(PXToken::async$initialize);
               }

               JsonObject var4 = (JsonObject)var10000.join();
               Optional var5 = this.parseResult(var4.toString());
               if (var5.isPresent()) {
                  this.setExpiryTime();
                  super.value = var5.get();
                  this.isTokenCaptcha = false;
                  this.expiryCount = 0;
                  return CompletableFuture.completedFuture(true);
               }

               throw new Exception("Failed generating token");
            } catch (Throwable var8) {
               var10000 = VertxUtil.randomSleep(10000L);
               if (!var10000.isDone()) {
                  var6 = var10000;
                  return var6.exceptionally(Function.identity()).thenCompose(PXToken::async$initialize);
               }

               var10000.join();
               ++var1;
            }
         }

         return CompletableFuture.completedFuture(false);
      }
   }

   public PXToken(TaskActor var1, Site var2) {
      super(var1, ClientType.PX_SDK_PIXEL_3, Controller.PROXY_RESIDENTIAL);
      this.stopKeepalive = false;
      this.failedCaptchaSolves = 0;
      this.rotates = 0;
      this.restartTime = 0L;
      this.isFirstTime = true;
      this.requestTime = 0L;
      this.SITE = var2;
   }

   public void lambda$retryCheck$1(Long var1) {
      Objects.requireNonNull(this);
      if (this.isExpired()) {
         this.checkOrUpdate();
      } else {
         this.retryCheck();
      }

   }

   public CompletableFuture reInit() {
      int var1 = 0;

      while(var1 < 1) {
         CompletableFuture var10000;
         CompletableFuture var6;
         try {
            FirstPayload var2 = new FirstPayload(this.secondPayload, this.secondPayload.sdkInitCount, this.requestTime, this.SITE);
            var10000 = this.sendPayload(var2.asForm());
            if (!var10000.isDone()) {
               var6 = var10000;
               return var6.exceptionally(Function.identity()).thenCompose(PXToken::async$reInit);
            }

            JsonObject var3 = (JsonObject)var10000.join();
            this.secondPayload = new SecondPayload(var2, var3, this.requestTime, this.SITE);
            var10000 = this.sendPayload(this.secondPayload.asForm());
            if (!var10000.isDone()) {
               var6 = var10000;
               return var6.exceptionally(Function.identity()).thenCompose(PXToken::async$reInit);
            }

            JsonObject var4 = (JsonObject)var10000.join();
            Optional var5 = this.parseResult(var4.toString());
            if (var5.isPresent()) {
               this.setExpiryTime();
               super.value = var5.get();
               this.isTokenCaptcha = false;
               this.expiryCount = 0;
               return CompletableFuture.completedFuture(true);
            }

            throw new Exception("Failed generating token");
         } catch (Throwable var7) {
            var10000 = VertxUtil.randomSleep(10000L);
            if (!var10000.isDone()) {
               var6 = var10000;
               return var6.exceptionally(Function.identity()).thenCompose(PXToken::async$reInit);
            }

            var10000.join();
            ++var1;
         }
      }

      return CompletableFuture.completedFuture(false);
   }

   public CompletableFuture awaitInit() {
      return super.initFuture;
   }

   public CompletableFuture checkOrUpdate() {
      if (this.isExpired()) {
         ++this.expiryCount;

         try {
            CompletableFuture var10001 = this.sendPayload(this.secondPayload.asKeepAliveForm());
            if (!var10001.isDone()) {
               CompletableFuture var3 = var10001;
               return var3.exceptionally(Function.identity()).thenCompose(PXToken::async$checkOrUpdate);
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
               return var2.exceptionally(Function.identity()).thenCompose(PXToken::async$checkOrUpdate);
            }

            var10000.join();
            return this.checkOrUpdate();
         } catch (Exception var4) {
            this.logger.error("Unable to keep-alive post: " + var4.getMessage());
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture initBrowserDevice(boolean var1) {
      if (var1 || this.captchaHandler == null) {
         this.captchaHandler = new PXCaptcha(this.logger, this.client, Types.CAPTCHA_DESKTOP, (String)null, (String)null, this.SITE);
      }

      return this.captchaHandler.prepareDevice();
   }

   public void startTimer() {
      this.stopTimerEager();
      this.restartClient(this.client);
      if (super.vertx != null) {
         this.timer = super.vertx.setTimer((this.expiryTime - Instant.now().getEpochSecond()) * 1000L, this::lambda$startTimer$0);
      }
   }

   public static CompletableFuture async$sendInit(PXToken param0, InitPayload param1, HttpRequest param2, CompletableFuture param3, HttpResponse param4, int param5, Object param6) {
      // $FF: Couldn't be decompiled
   }

   public void retryCheck() {
      super.vertx.setTimer(500L, this::lambda$retryCheck$1);
   }

   public void setExpiryTime() {
      this.expiryTime = (long)((double)Instant.now().getEpochSecond() + Double.longBitsToDouble(4642627155242306765L));
      this.startTimer();
   }

   public static CompletableFuture async$solveCaptchaDesktop(PXToken var0, String var1, String var2, String var3, CompletableFuture var4, int var5, Object var6) {
      CompletableFuture var10000;
      switch (var5) {
         case 0:
            var10000 = VertxUtil.hardCodedSleep(7000L);
            if (!var10000.isDone()) {
               var4 = var10000;
               return var4.exceptionally(Function.identity()).thenCompose(PXToken::async$solveCaptchaDesktop);
            }
            break;
         case 1:
            var10000 = var4;
            break;
         default:
            throw new IllegalArgumentException();
      }

      var10000.join();
      return CompletableFuture.completedFuture((Object)null);
   }

   public String getDeviceLang() {
      return this.captchaHandler.getDevice().getAcceptLanguage();
   }

   public void stopTimerEager() {
      if (this.timer != 0L) {
         super.vertx.cancelTimer(this.timer);
      }

   }

   public static CompletableFuture async$handleAfterCap(PXToken var0, FirstPayload var1, CompletableFuture var2, JsonObject var3, int var4, Object var5) {
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
                        return var11.exceptionally(Function.identity()).thenCompose(PXToken::async$handleAfterCap);
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
                  return var11.exceptionally(Function.identity()).thenCompose(PXToken::async$handleAfterCap);
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

   public void setValue(String var1) {
      super.value = var1;
   }

   public String getDeviceUA() {
      try {
         return this.captchaHandler.getDevice().getUserAgent();
      } catch (Throwable var2) {
         return "";
      }
   }

   public PXCaptcha getCaptchaHandler() {
      return this.captchaHandler;
   }

   public CompletableFuture handleAfterCap() {
      try {
         FirstPayload var1 = new FirstPayload(this.secondPayload, this.secondPayload.sdkInitCount, this.requestTime, this.SITE);
         CompletableFuture var10000 = this.sendPayload(var1.asForm());
         CompletableFuture var4;
         if (!var10000.isDone()) {
            var4 = var10000;
            return var4.exceptionally(Function.identity()).thenCompose(PXToken::async$handleAfterCap);
         }

         JsonObject var2 = (JsonObject)var10000.join();
         this.secondPayload = new SecondPayload(var1, var2, this.requestTime, this.SITE);
         var10000 = this.sendPayload(this.secondPayload.asForm());
         if (!var10000.isDone()) {
            var4 = var10000;
            return var4.exceptionally(Function.identity()).thenCompose(PXToken::async$handleAfterCap);
         }

         JsonObject var3 = (JsonObject)var10000.join();
      } catch (Throwable var5) {
         this.logger.warn("Error on A-CP: {}", var5.getMessage());
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public String getDeviceSecUAMobile() {
      return this.captchaHandler.getDevice().getSecUAMobile();
   }

   public void lambda$startTimer$0(Long var1) {
      Objects.requireNonNull(this);
      if (this.isExpired()) {
         this.checkOrUpdate();
      } else {
         this.retryCheck();
      }

   }

   public static CompletableFuture async$sendPayload(PXToken param0, MultiMap param1, HttpRequest param2, long param3, CompletableFuture param5, HttpResponse param6, int param7, Object param8) {
      // $FF: Couldn't be decompiled
   }

   public String getDeviceSecUA() {
      return this.captchaHandler.getDevice().getSecUA();
   }

   public void rotateAndRegen() {
      this.rotateProxy();
      this.initialize();
   }

   public void setExpiryTimeAfterCap() {
      this.expiryTime = Instant.now().getEpochSecond() + 300L;
      this.startTimer();
   }

   public static CompletableFuture async$reInit(PXToken param0, int param1, FirstPayload param2, CompletableFuture param3, JsonObject param4, Throwable param5, int param6, Object param7) {
      // $FF: Couldn't be decompiled
   }

   public void rotateProxy() {
      try {
         RealClient var1 = RealClientFactory.rotateProxy(Vertx.currentContext().owner(), this.client, Controller.PROXY_RESIDENTIAL);
         this.client.close();
         this.client = var1;
      } catch (Throwable var2) {
      }

   }

   public boolean hasExpiredOnce() {
      return this.expiryCount > 0;
   }
}
