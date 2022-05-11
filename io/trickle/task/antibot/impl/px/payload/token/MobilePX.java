package io.trickle.task.antibot.impl.px.payload.token;

import io.trickle.core.actor.TaskActor;
import io.trickle.task.antibot.impl.px.PerimeterX;
import io.trickle.task.antibot.impl.px.payload.Payload;
import io.trickle.task.antibot.impl.px.payload.captcha.DesktopPX;
import io.trickle.task.sites.Site;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.RequestOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.impl.HttpRequestImpl;
import io.vertx.ext.web.codec.BodyCodec;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Matcher;

public class MobilePX extends PerimeterX {
   public DesktopPX captchaDelegate;
   public SecondPayload secondPayload;
   public Site site;
   public long expiryTime;
   public long requestTiming;
   public boolean hasSolvedOnCurrentVid;
   public long timerId;

   public static CompletableFuture async$initialise(MobilePX param0, InitPayload param1, int param2, CompletableFuture param3, FirstPayload param4, JsonObject param5, Throwable param6, int param7, Object param8) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture sendInit(InitPayload var1) {
      if (var1 == null) {
         return CompletableFuture.failedFuture(new Exception("Initialisation payload is null"));
      } else {
         HttpRequest var2 = this.client.request(HttpMethod.POST, initRequest(this.site)).as(BodyCodec.jsonObject());
         return this.execute(var2, var1.asBuffer(this.site));
      }
   }

   public MobilePX(TaskActor var1) {
      super(var1);
      this.site = Site.WALMART;
      this.captchaDelegate = null;
      this.expiryTime = -1L;
      this.timerId = -1L;
      this.hasSolvedOnCurrentVid = false;
   }

   public String getDeviceAcceptEncoding() {
      if (this.logger.isDebugEnabled()) {
         this.logger.debug("Device encoding should not be used with MobilePX");
      }

      return "gzip, deflate, br";
   }

   public static RequestOptions collectorRequest(Site var0) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$solveCaptcha(MobilePX var0, String var1, String var2, String var3, CompletableFuture var4, String var5, int var6, Object var7) {
      CompletableFuture var10000;
      label36: {
         CompletableFuture var8;
         switch (var6) {
            case 0:
               var10000 = VertxUtil.hardCodedSleep(15000L);
               if (!var10000.isDone()) {
                  var8 = var10000;
                  return var8.exceptionally(Function.identity()).thenCompose(MobilePX::async$solveCaptcha);
               }
               break;
            case 1:
               var10000 = var4;
               break;
            case 2:
               var10000 = var4;
               break label36;
            default:
               throw new IllegalArgumentException();
         }

         var10000.join();
         if (var0.captchaDelegate == null) {
            var0.captchaDelegate = new DesktopPX((TaskActor)null);
         }

         var4 = null;
         if (var4 == null) {
            return CompletableFuture.completedFuture(false);
         }

         var0.logger.info("Successfully solved captcha!");
         if (var0.logger.isDebugEnabled()) {
            var0.logger.debug("Mobile captcha response: {}", var4);
         }

         var0.scheduleKeepaliveAfterCaptcha();
         var0.result = var4;
         var0.hasSolvedOnCurrentVid = true;
         var10000 = var0.solve();
         if (!var10000.isDone()) {
            var8 = var10000;
            return var8.exceptionally(Function.identity()).thenCompose(MobilePX::async$solveCaptcha);
         }
      }

      var10000.join();
      return CompletableFuture.completedFuture(true);
   }

   public static CompletableFuture async$onKeepalive(MobilePX var0, CompletableFuture var1, int var2, Object var3) {
      Throwable var9;
      label43: {
         CompletableFuture var10000;
         boolean var10001;
         switch (var2) {
            case 0:
               try {
                  var10000 = var0.sendKeepAlive();
                  if (!var10000.isDone()) {
                     CompletableFuture var8 = var10000;
                     return var8.exceptionally(Function.identity()).thenCompose(MobilePX::async$onKeepalive);
                  }
                  break;
               } catch (Throwable var5) {
                  var9 = var5;
                  var10001 = false;
                  break label43;
               }
            case 1:
               var10000 = var1;
               break;
            default:
               throw new IllegalArgumentException();
         }

         try {
            JsonObject var7 = (JsonObject)var10000.join();
            if (var0.logger.isDebugEnabled()) {
               var0.logger.debug("Received keep-alive response: {}", var7.encodePrettily());
            }

            if (var0.onResult(var7.toString())) {
               if (var0.logger.isDebugEnabled()) {
                  var0.logger.debug("Solved keepalive: {}", var0.result);
                  return CompletableFuture.completedFuture((Object)null);
               }
            } else {
               if (var0.logger.isDebugEnabled()) {
                  var0.logger.debug("Failed to solve keepalive. Retrying in 5s");
               }

               var0.scheduleKeepAlive(5000L);
            }

            return CompletableFuture.completedFuture((Object)null);
         } catch (Throwable var4) {
            var9 = var4;
            var10001 = false;
         }
      }

      Throwable var6 = var9;
      var0.logger.warn("Error on keepalive: {}", var6.getMessage());
      if (var0.logger.isDebugEnabled()) {
         var0.logger.debug(var6);
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public String getDeviceSecUA() {
      return "\"Google Chrome\";v=\"94\", \" Not;A Brand\";v=\"99\", \"Chromium\";v=\"94\"";
   }

   public static CompletableFuture async$execute(MobilePX param0, HttpRequest param1, Object param2, int param3, CompletableFuture param4, long param5, HttpResponse param7, Throwable param8, int param9, Object param10) {
      // $FF: Couldn't be decompiled
   }

   public void scheduleKeepAlive(long var1) {
      this.expiryTime = var1;
      this.startTimer();
   }

   public CompletableFuture solve() {
      if (this.secondPayload == null) {
         if (this.logger.isDebugEnabled()) {
            this.logger.debug("Second payload is null on solve(). Performing hard reinitialization (full-reset)");
         }

         return this.initialise();
      } else {
         CompletableFuture var10000;
         CompletableFuture var4;
         try {
            FirstPayload var1 = new FirstPayload(this.secondPayload, this.secondPayload.sdkInitCount, this.requestTiming, this.site);
            var10000 = this.sendPayload(var1);
            if (!var10000.isDone()) {
               var4 = var10000;
               return var4.exceptionally(Function.identity()).thenCompose(MobilePX::async$solve);
            } else {
               JsonObject var2 = (JsonObject)var10000.join();
               if (this.logger.isDebugEnabled()) {
                  this.logger.debug("Fetched first payload: {}", var2.encodePrettily());
               }

               this.secondPayload = new SecondPayload(var1, var2, this.requestTiming, this.site);
               var10000 = this.sendPayload(this.secondPayload);
               if (!var10000.isDone()) {
                  var4 = var10000;
                  return var4.exceptionally(Function.identity()).thenCompose(MobilePX::async$solve);
               } else {
                  JsonObject var3 = (JsonObject)var10000.join();
                  if (this.logger.isDebugEnabled()) {
                     this.logger.debug("Fetched second payload: {}", var3.encodePrettily());
                  }

                  if (this.onResult(var3.toString())) {
                     return CompletableFuture.completedFuture(true);
                  } else {
                     throw new Exception("Failed solving sensor");
                  }
               }
            }
         } catch (Throwable var5) {
            this.logger.warn("Error solving sensor: {}. Retrying...", var5.getMessage());
            if (this.logger.isDebugEnabled()) {
               this.logger.debug(var5);
            }

            var10000 = VertxUtil.randomSleep(10000L);
            if (!var10000.isDone()) {
               var4 = var10000;
               return var4.exceptionally(Function.identity()).thenCompose(MobilePX::async$solve);
            } else {
               var10000.join();
               return CompletableFuture.completedFuture(false);
            }
         }
      }
   }

   public CompletableFuture sendKeepAlive() {
      if (this.secondPayload == null) {
         return CompletableFuture.failedFuture(new Exception("Data payload is null"));
      } else {
         HttpRequest var1 = this.client.request(HttpMethod.POST, collectorRequest(this.site)).as(BodyCodec.jsonObject());
         return this.execute(var1, this.secondPayload.asKeepAliveForm());
      }
   }

   public CompletableFuture onKeepalive() {
      try {
         CompletableFuture var10000 = this.sendKeepAlive();
         if (!var10000.isDone()) {
            CompletableFuture var2 = var10000;
            return var2.exceptionally(Function.identity()).thenCompose(MobilePX::async$onKeepalive);
         }

         JsonObject var1 = (JsonObject)var10000.join();
         if (this.logger.isDebugEnabled()) {
            this.logger.debug("Received keep-alive response: {}", var1.encodePrettily());
         }

         if (this.onResult(var1.toString())) {
            if (this.logger.isDebugEnabled()) {
               this.logger.debug("Solved keepalive: {}", super.result);
            }
         } else {
            if (this.logger.isDebugEnabled()) {
               this.logger.debug("Failed to solve keepalive. Retrying in 5s");
            }

            this.scheduleKeepAlive(5000L);
         }
      } catch (Throwable var3) {
         this.logger.warn("Error on keepalive: {}", var3.getMessage());
         if (this.logger.isDebugEnabled()) {
            this.logger.debug(var3);
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public void scheduleKeepaliveAfterCaptcha() {
      this.scheduleKeepAlive(TimeUnit.MINUTES.toMillis(5L));
   }

   public void cancelTimer() {
      if (this.vertx != null && this.timerId != -1L) {
         if (this.vertx.cancelTimer(this.timerId) && this.logger.isDebugEnabled()) {
            this.logger.debug("Cancelled keep-alive timer of id '{}' early", this.timerId);
         }

         this.timerId = -1L;
      }

   }

   public CompletableFuture solveCaptcha(String var1, String var2, String var3) {
      CompletableFuture var10000 = VertxUtil.hardCodedSleep(15000L);
      CompletableFuture var5;
      if (!var10000.isDone()) {
         var5 = var10000;
         return var5.exceptionally(Function.identity()).thenCompose(MobilePX::async$solveCaptcha);
      } else {
         var10000.join();
         if (this.captchaDelegate == null) {
            this.captchaDelegate = new DesktopPX((TaskActor)null);
         }

         Object var4 = null;
         if (var4 != null) {
            this.logger.info("Successfully solved captcha!");
            if (this.logger.isDebugEnabled()) {
               this.logger.debug("Mobile captcha response: {}", var4);
            }

            this.scheduleKeepaliveAfterCaptcha();
            this.result = var4;
            this.hasSolvedOnCurrentVid = true;
            var10000 = this.solve();
            if (!var10000.isDone()) {
               var5 = var10000;
               return var5.exceptionally(Function.identity()).thenCompose(MobilePX::async$solveCaptcha);
            } else {
               var10000.join();
               return CompletableFuture.completedFuture(true);
            }
         } else {
            return CompletableFuture.completedFuture(false);
         }
      }
   }

   public void lambda$startTimer$0(Long var1) {
      this.onKeepalive();
   }

   public static CompletableFuture async$solve(MobilePX var0, FirstPayload var1, CompletableFuture var2, JsonObject var3, Throwable var4, int var5, Object var6) {
      CompletableFuture var10000;
      label87: {
         CompletableFuture var13;
         Throwable var14;
         label81: {
            boolean var10001;
            label72: {
               switch (var5) {
                  case 0:
                     if (var0.secondPayload == null) {
                        if (var0.logger.isDebugEnabled()) {
                           var0.logger.debug("Second payload is null on solve(). Performing hard reinitialization (full-reset)");
                        }

                        return var0.initialise();
                     }

                     try {
                        var1 = new FirstPayload(var0.secondPayload, var0.secondPayload.sdkInitCount, var0.requestTiming, var0.site);
                        var10000 = var0.sendPayload(var1);
                        if (!var10000.isDone()) {
                           var13 = var10000;
                           return var13.exceptionally(Function.identity()).thenCompose(MobilePX::async$solve);
                        }
                        break;
                     } catch (Throwable var10) {
                        var14 = var10;
                        var10001 = false;
                        break label81;
                     }
                  case 1:
                     var10000 = var2;
                     break;
                  case 2:
                     var10000 = var2;
                     break label72;
                  case 3:
                     var10000 = var2;
                     break label87;
                  default:
                     throw new IllegalArgumentException();
               }

               try {
                  JsonObject var12 = (JsonObject)var10000.join();
                  if (var0.logger.isDebugEnabled()) {
                     var0.logger.debug("Fetched first payload: {}", var12.encodePrettily());
                  }

                  var0.secondPayload = new SecondPayload(var1, var12, var0.requestTiming, var0.site);
                  var10000 = var0.sendPayload(var0.secondPayload);
                  if (!var10000.isDone()) {
                     var13 = var10000;
                     return var13.exceptionally(Function.identity()).thenCompose(MobilePX::async$solve);
                  }
               } catch (Throwable var9) {
                  var14 = var9;
                  var10001 = false;
                  break label81;
               }
            }

            try {
               var3 = (JsonObject)var10000.join();
               if (var0.logger.isDebugEnabled()) {
                  var0.logger.debug("Fetched second payload: {}", var3.encodePrettily());
               }

               if (var0.onResult(var3.toString())) {
                  return CompletableFuture.completedFuture(true);
               }
            } catch (Throwable var8) {
               var14 = var8;
               var10001 = false;
               break label81;
            }

            try {
               throw new Exception("Failed solving sensor");
            } catch (Throwable var7) {
               var14 = var7;
               var10001 = false;
            }
         }

         Throwable var11 = var14;
         var0.logger.warn("Error solving sensor: {}. Retrying...", var11.getMessage());
         if (var0.logger.isDebugEnabled()) {
            var0.logger.debug(var11);
         }

         var10000 = VertxUtil.randomSleep(10000L);
         if (!var10000.isDone()) {
            var13 = var10000;
            return var13.exceptionally(Function.identity()).thenCompose(MobilePX::async$solve);
         }
      }

      var10000.join();
      return CompletableFuture.completedFuture(false);
   }

   public void close() {
      this.cancelTimer();
      super.close();
   }

   public String getDeviceLang() {
      if (this.logger.isDebugEnabled()) {
         this.logger.debug("Device lang should not be used with MobilePX");
      }

      return "en-GB,en;q=0.9";
   }

   public static RequestOptions initRequest(Site var0) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture initialise() {
      this.requestTiming = 0L;
      InitPayload var1 = null;

      for(int var2 = 0; var2 < 5; ++var2) {
         CompletableFuture var10000;
         CompletableFuture var6;
         try {
            if (var1 == null) {
               if (this.logger.isDebugEnabled()) {
                  this.logger.debug("Fetching init payload from device API");
               }

               var10000 = InitPayload.fetch();
               if (!var10000.isDone()) {
                  var6 = var10000;
                  return var6.exceptionally(Function.identity()).thenCompose(MobilePX::async$initialise);
               }

               var1 = (InitPayload)var10000.join();
               var10000 = this.sendInit(var1);
               if (!var10000.isDone()) {
                  var6 = var10000;
                  return var6.exceptionally(Function.identity()).thenCompose(MobilePX::async$initialise);
               }

               var10000.join();
            } else {
               if (this.logger.isDebugEnabled()) {
                  this.logger.debug("Sending first payload.");
               }

               FirstPayload var3 = new FirstPayload(var1, this.site);
               var10000 = this.sendPayload(var3);
               if (!var10000.isDone()) {
                  var6 = var10000;
                  return var6.exceptionally(Function.identity()).thenCompose(MobilePX::async$initialise);
               }

               JsonObject var4 = (JsonObject)var10000.join();
               Objects.requireNonNull(var4);
               if (this.logger.isDebugEnabled()) {
                  this.logger.debug("Fetched first payload: {}", var4.encodePrettily());
               }

               this.secondPayload = new SecondPayload(var3, var4, this.requestTiming, this.site);
               var10000 = this.sendPayload(this.secondPayload);
               if (!var10000.isDone()) {
                  var6 = var10000;
                  return var6.exceptionally(Function.identity()).thenCompose(MobilePX::async$initialise);
               }

               JsonObject var5 = (JsonObject)var10000.join();
               Objects.requireNonNull(var5);
               if (this.logger.isDebugEnabled()) {
                  this.logger.debug("Fetched second payload: {}", var5.encodePrettily());
               }

               if (this.onResult(var5.toString())) {
                  return CompletableFuture.completedFuture(true);
               }

               var1 = null;
               this.secondPayload = null;
               if (this.logger.isDebugEnabled()) {
                  this.logger.debug("Reset API state. Will re-initialise device and payloads on next attempt");
               }

               this.logger.warn("Failed to initialise API. Resetting & retrying...");
            }
         } catch (Throwable var7) {
            this.logger.warn("Error initialising api: {}. Retrying...", var7.getMessage());
            if (this.logger.isDebugEnabled()) {
               this.logger.debug(var7);
            }

            var10000 = VertxUtil.randomSleep(10000L);
            if (!var10000.isDone()) {
               var6 = var10000;
               return var6.exceptionally(Function.identity()).thenCompose(MobilePX::async$initialise);
            }

            var10000.join();
         }
      }

      return CompletableFuture.failedFuture(new Exception("Failed to initialise antibot API"));
   }

   public void startTimer() {
      if (this.vertx == null) {
         if (this.logger.isDebugEnabled()) {
            this.logger.debug("Unable to start keepalive timer: Vertx context is null");
         }

      } else {
         this.cancelTimer();
         this.timerId = this.vertx.setTimer(this.expiryTime, this::lambda$startTimer$0);
         if (this.logger.isDebugEnabled()) {
            this.logger.debug("Scheduled keep-alive to be sent in {}ms", this.expiryTime);
         }

      }
   }

   public String getDeviceUA() {
      if (this.logger.isDebugEnabled()) {
         this.logger.debug("Device UA should not be used with MobilePX");
      }

      return "Mozilla/5.0 (Linux; Android 11; Pixel 5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.85 Mobile Safari/537.36";
   }

   public CompletableFuture execute(HttpRequest var1, Object var2) {
      int var3 = 0;

      while(this.client.isActive() && var3++ <= 15) {
         CompletableFuture var7;
         CompletableFuture var9;
         try {
            if (this.logger.isDebugEnabled()) {
               this.logger.debug("Executing payload request '{}'", ((HttpRequestImpl)var1).uri());
            }

            HttpResponse var4;
            if (var2 == null) {
               var9 = Request.send(var1);
               if (!var9.isDone()) {
                  var7 = var9;
                  return var7.exceptionally(Function.identity()).thenCompose(MobilePX::async$execute);
               }

               var4 = (HttpResponse)var9.join();
            } else if (var2 instanceof Buffer) {
               var9 = Request.send(var1, (Buffer)var2);
               if (!var9.isDone()) {
                  var7 = var9;
                  return var7.exceptionally(Function.identity()).thenCompose(MobilePX::async$execute);
               }

               var4 = (HttpResponse)var9.join();
            } else {
               if (!(var2 instanceof MultiMap)) {
                  throw new Exception("Unsupported body format for payload request");
               }

               long var5 = System.currentTimeMillis();
               var9 = Request.send(var1, (MultiMap)var2);
               if (!var9.isDone()) {
                  var7 = var9;
                  return var7.exceptionally(Function.identity()).thenCompose(MobilePX::async$execute);
               }

               var4 = (HttpResponse)var9.join();
               this.requestTiming = System.currentTimeMillis() - var5;
               if (this.secondPayload != null) {
                  this.secondPayload.updatePX349(this.requestTiming);
               }
            }

            if (var4 != null) {
               if (this.logger.isDebugEnabled()) {
                  this.logger.debug("Received payload response from '{}' : {}", ((HttpRequestImpl)var1).uri(), var4.body());
               }

               return CompletableFuture.completedFuture(var4.body());
            }

            if (this.logger.isDebugEnabled()) {
               this.logger.debug("Failed to receive payload response from '{}'", ((HttpRequestImpl)var1).uri());
            } else {
               this.logger.warn("Error on payload execution. Retrying...");
            }

            var9 = VertxUtil.randomSleep(10000L);
            if (!var9.isDone()) {
               var7 = var9;
               return var7.exceptionally(Function.identity()).thenCompose(MobilePX::async$execute);
            }

            var9.join();
         } catch (Throwable var8) {
            this.logger.warn("Error sending payload: {}", var8.getMessage());
            if (this.logger.isDebugEnabled()) {
               this.logger.debug(var8);
            }

            var9 = VertxUtil.randomSleep(10000L);
            if (!var9.isDone()) {
               var7 = var9;
               return var7.exceptionally(Function.identity()).thenCompose(MobilePX::async$execute);
            }

            var9.join();
         }
      }

      return CompletableFuture.failedFuture(new Exception("Failed to send payload"));
   }

   public boolean onResult(String var1) {
      Matcher var2 = BAKE_PATTERN.matcher(var1);
      if (var2.find()) {
         super.result = "3:" + var2.group(1);
         this.scheduleKeepalive();
         if (this.logger.isDebugEnabled()) {
            this.logger.debug("New PX token value: {}", super.result);
         }

         return true;
      } else {
         return false;
      }
   }

   public void reset() {
      this.cancelTimer();
      this.secondPayload = null;
      this.expiryTime = -1L;
      this.timerId = -1L;
      this.hasSolvedOnCurrentVid = false;
      if (this.captchaDelegate != null) {
         this.captchaDelegate.reset();
      }

   }

   public String getVid() {
      return this.secondPayload == null ? null : this.secondPayload.VID_HEADER;
   }

   public CompletableFuture sendPayload(Payload var1) {
      if (var1 == null) {
         return CompletableFuture.failedFuture(new Exception("Provided payload is null"));
      } else {
         HttpRequest var2 = this.client.request(HttpMethod.POST, collectorRequest(this.site)).as(BodyCodec.jsonObject());
         return this.execute(var2, var1.asForm());
      }
   }

   public void scheduleKeepalive() {
      this.scheduleKeepAlive(TimeUnit.MINUTES.toMillis(4L));
   }
}
