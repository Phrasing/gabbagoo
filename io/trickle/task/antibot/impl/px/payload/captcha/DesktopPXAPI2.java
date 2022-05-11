package io.trickle.task.antibot.impl.px.payload.captcha;

import io.netty.util.AsciiString;
import io.trickle.core.actor.TaskActor;
import io.trickle.task.antibot.impl.px.PerimeterX;
import io.trickle.util.Utils;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.trickle.webclient.ClientType;
import io.trickle.webclient.TaskApiClient;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.RequestOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.codec.BodyCodec;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Matcher;
import org.apache.logging.log4j.Logger;

public class DesktopPXAPI2 extends PerimeterX {
   public boolean solvingCaptcha;
   public String pxURI;
   public static String baseURI = "https://collector-" + "PXu6b0qd2S".toLowerCase() + ".px-cloud.net";
   public static CharSequence ONE_VALUE = AsciiString.cached("1");
   public String secUA;
   public JsonObject metaPayload;
   public static CharSequence CFP_VALUE = AsciiString.cached("_pxff_cfp");
   public static CharSequence DEFAULT_UA = AsciiString.cached("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36");
   public int failedSolves = 0;
   public static CharSequence KEY = AsciiString.cached("ce3cabed-e10f-43b1-a2a2-8d2a9c2a212f");
   public TaskApiClient delegate;
   public String userAgent;
   public JsonObject pxResponse;
   public int delay;
   public long timerId;
   public String referrer;
   public String currentPayload;
   public static CharSequence VID_COOKIE = AsciiString.cached("_pxvid");
   public static CharSequence PXDE_VALUE = AsciiString.cached("_pxde");
   public static CharSequence FP_VALUE = AsciiString.cached("_pxff_fp");
   public long expiryTime = 0L;
   public static CharSequence PX3_VALUE = AsciiString.cached("_px3");
   public static CharSequence RF_VALUE = AsciiString.cached("_pxff_rf");

   public void lambda$flatMapMeta$1(Map.Entry var1) {
      this.metaPayload.put((String)var1.getKey(), var1.getValue());
   }

   public static CompletableFuture async$solveImage(DesktopPXAPI2 param0, CompletableFuture param1, int param2, Object param3) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture solveCaptcha(String var1, String var2, String var3) {
      this.solvingCaptcha = true;
      this.pxURI = baseURI + "/assets/js/bundle";
      this.referrer = "https://www.walmart.com/account/login?vid=oaoh&returnUrl=%2F";
      byte var4 = 0;
      int var5 = 0;

      while(var5 < 4) {
         CompletableFuture var10000;
         CompletableFuture var8;
         try {
            if (this.failedSolves < 3) {
               this.mapBlockValues("vid", var1);
               this.mapBlockValues("uuid", var2);
               var10000 = this.solve1();
               if (!var10000.isDone()) {
                  var8 = var10000;
                  return var8.exceptionally(Function.identity()).thenCompose(DesktopPXAPI2::async$solveCaptcha);
               }

               var10000.join();
               if (var4 == 0) {
                  var10000 = this.solve2();
                  if (!var10000.isDone()) {
                     var8 = var10000;
                     return var8.exceptionally(Function.identity()).thenCompose(DesktopPXAPI2::async$solveCaptcha);
                  }

                  var10000.join();
                  var10000 = this.solveImage();
                  if (!var10000.isDone()) {
                     var8 = var10000;
                     return var8.exceptionally(Function.identity()).thenCompose(DesktopPXAPI2::async$solveCaptcha);
                  }

                  var10000.join();
               }

               var10000 = this.solveHold();
               if (!var10000.isDone()) {
                  var8 = var10000;
                  return var8.exceptionally(Function.identity()).thenCompose(DesktopPXAPI2::async$solveCaptcha);
               }

               var10000.join();
               Optional var6 = this.onResult(this.pxResponse.encode());
               if (var6.isPresent()) {
                  this.logger.info("Successfully solved captcha");
                  this.failedSolves = 0;
                  MultiMap var7 = MultiMap.caseInsensitiveMultiMap();
                  this.parseResultCookies(this.pxResponse.encode(), var7);
                  return CompletableFuture.completedFuture(var7);
               }

               boolean var10 = true;
               throw new Exception("Invalid captcha base token.");
            }

            var4 = 0;
            this.reset();
            if (this.userAgent == null) {
               var10000 = this.initialise();
               if (!var10000.isDone()) {
                  var8 = var10000;
                  return var8.exceptionally(Function.identity()).thenCompose(DesktopPXAPI2::async$solveCaptcha);
               }

               var10000.join();
            }
            break;
         } catch (Throwable var9) {
            ++this.failedSolves;
            if (!var9.getMessage().contains("Invalid captcha base token.")) {
               this.logger.warn("Error solving captcha: {}. Retrying...", var9.getMessage());
               if (this.logger.isDebugEnabled()) {
                  this.logger.debug(var9);
               }
            }

            var10000 = VertxUtil.randomSleep(8000L);
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(DesktopPXAPI2::async$solveCaptcha);
            }

            var10000.join();
            ++var5;
         }
      }

      this.logger.warn("Failed to solve captcha!");
      return CompletableFuture.completedFuture(MultiMap.caseInsensitiveMultiMap());
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

   public CompletableFuture onKeepalive() {
      try {
         CompletableFuture var10000 = this.solve();
         if (!var10000.isDone()) {
            CompletableFuture var4 = var10000;
            return var4.exceptionally(Function.identity()).thenCompose(DesktopPXAPI2::async$onKeepalive);
         }

         MultiMap var1 = (MultiMap)var10000.join();
         if (var1 != null && !var1.isEmpty()) {
            this.logger.info("Received cookies after keepalive: {}", var1);
            Iterator var2 = var1.entries().iterator();

            while(var2.hasNext()) {
               Map.Entry var3 = (Map.Entry)var2.next();
               this.delegate.getWebClient().cookieStore().put((String)var3.getKey(), (String)var3.getValue(), ".walmart.com");
            }
         }

         this.scheduleKeepalive();
      } catch (Throwable var5) {
         this.logger.warn("Error on keepalive: {}", var5.getMessage());
         if (this.logger.isDebugEnabled()) {
            this.logger.debug(var5);
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public void mapBlockValues(String var1, String var2) {
      Objects.requireNonNull(var1);
      if (var2 != null && !var2.isEmpty() && !var2.isBlank()) {
         JsonObject var3 = this.getMetaPayload();
         if (var3 != null) {
            var3.put(var1, var2);
         }
      }

   }

   public MultiMap getParams() {
      MultiMap var1 = HttpHeaders.headers().set("auth", KEY).set("appId", "PXu6b0qd2S");
      if (this.solvingCaptcha) {
         var1.add("captcha", "hold");
      }

      if (this.referrer != null && !this.referrer.isEmpty()) {
         var1.add("domain", this.referrer);
      } else {
         var1.add("domain", "https://www.walmart.com/");
      }

      return var1;
   }

   public static CompletableFuture async$onKeepalive(DesktopPXAPI2 var0, CompletableFuture var1, int var2, Object var3) {
      Throwable var11;
      label45: {
         CompletableFuture var10000;
         boolean var10001;
         switch (var2) {
            case 0:
               try {
                  var10000 = var0.solve();
                  if (!var10000.isDone()) {
                     CompletableFuture var4 = var10000;
                     return var4.exceptionally(Function.identity()).thenCompose(DesktopPXAPI2::async$onKeepalive);
                  }
                  break;
               } catch (Throwable var6) {
                  var11 = var6;
                  var10001 = false;
                  break label45;
               }
            case 1:
               var10000 = var1;
               break;
            default:
               throw new IllegalArgumentException();
         }

         try {
            MultiMap var8 = (MultiMap)var10000.join();
            if (var8 != null && !var8.isEmpty()) {
               var0.logger.info("Received cookies after keepalive: {}", var8);
               Iterator var9 = var8.entries().iterator();

               while(var9.hasNext()) {
                  Map.Entry var10 = (Map.Entry)var9.next();
                  var0.delegate.getWebClient().cookieStore().put((String)var10.getKey(), (String)var10.getValue(), ".walmart.com");
               }
            }

            var0.scheduleKeepalive();
            return CompletableFuture.completedFuture((Object)null);
         } catch (Throwable var5) {
            var11 = var5;
            var10001 = false;
         }
      }

      Throwable var7 = var11;
      var0.logger.warn("Error on keepalive: {}", var7.getMessage());
      if (var0.logger.isDebugEnabled()) {
         var0.logger.debug(var7);
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$solveHold(DesktopPXAPI2 var0, CompletableFuture var1, int var2, Object var3) {
      Throwable var10;
      label61: {
         CompletableFuture var10000;
         boolean var10001;
         label52: {
            CompletableFuture var9;
            label51: {
               switch (var2) {
                  case 0:
                     try {
                        var10000 = var0.getPayload(4);
                        if (!var10000.isDone()) {
                           var9 = var10000;
                           return var9.exceptionally(Function.identity()).thenCompose(DesktopPXAPI2::async$solveHold);
                        }
                        break;
                     } catch (Throwable var7) {
                        var10 = var7;
                        var10001 = false;
                        break label61;
                     }
                  case 1:
                     var10000 = var1;
                     break;
                  case 2:
                     var10000 = var1;
                     break label51;
                  case 3:
                     var10000 = var1;
                     break label52;
                  default:
                     throw new IllegalArgumentException();
               }

               try {
                  var10000.join();
                  if (var0.logger.isDebugEnabled()) {
                     var0.logger.debug("Sleeping for {}ms before sending payload", var0.delay);
                  }

                  var10000 = VertxUtil.hardCodedSleep((long)var0.delay);
                  if (!var10000.isDone()) {
                     var9 = var10000;
                     return var9.exceptionally(Function.identity()).thenCompose(DesktopPXAPI2::async$solveHold);
                  }
               } catch (Throwable var6) {
                  var10 = var6;
                  var10001 = false;
                  break label61;
               }
            }

            try {
               var10000.join();
               var10000 = var0.postPayloadPX();
               if (!var10000.isDone()) {
                  var9 = var10000;
                  return var9.exceptionally(Function.identity()).thenCompose(DesktopPXAPI2::async$solveHold);
               }
            } catch (Throwable var5) {
               var10 = var5;
               var10001 = false;
               break label61;
            }
         }

         try {
            var10000.join();
            return CompletableFuture.completedFuture((Object)null);
         } catch (Throwable var4) {
            var10 = var4;
            var10001 = false;
         }
      }

      Throwable var8 = var10;
      if (var0.logger.isDebugEnabled()) {
         var0.logger.debug("Error sending sensor step[4]: {}. Retrying...", var8.getMessage(), var8);
      }

      return CompletableFuture.failedFuture(var8);
   }

   public void cancelKeepalive() {
      if (this.vertx != null && this.timerId != -1L) {
         if (this.vertx.cancelTimer(this.timerId) && !this.logger.isDebugEnabled()) {
            this.logger.debug("Cancelled keep-alive timer of id '{}' early", this.timerId);
         }

         this.timerId = -1L;
      }

   }

   public CompletableFuture solveHold() {
      try {
         CompletableFuture var10000 = this.getPayload(4);
         CompletableFuture var2;
         if (!var10000.isDone()) {
            var2 = var10000;
            return var2.exceptionally(Function.identity()).thenCompose(DesktopPXAPI2::async$solveHold);
         } else {
            var10000.join();
            if (this.logger.isDebugEnabled()) {
               this.logger.debug("Sleeping for {}ms before sending payload", this.delay);
            }

            var10000 = VertxUtil.hardCodedSleep((long)this.delay);
            if (!var10000.isDone()) {
               var2 = var10000;
               return var2.exceptionally(Function.identity()).thenCompose(DesktopPXAPI2::async$solveHold);
            } else {
               var10000.join();
               var10000 = this.postPayloadPX();
               if (!var10000.isDone()) {
                  var2 = var10000;
                  return var2.exceptionally(Function.identity()).thenCompose(DesktopPXAPI2::async$solveHold);
               } else {
                  var10000.join();
                  return CompletableFuture.completedFuture((Object)null);
               }
            }
         }
      } catch (Throwable var3) {
         if (this.logger.isDebugEnabled()) {
            this.logger.debug("Error sending sensor step[4]: {}. Retrying...", var3.getMessage(), var3);
         }

         return CompletableFuture.failedFuture(var3);
      }
   }

   public void flatMapMeta(JsonObject var1) {
      if (var1 != null && !var1.isEmpty()) {
         var1.stream().forEach(this::lambda$flatMapMeta$1);
      }

   }

   public void scheduleKeepAlive(long var1) {
      this.expiryTime = var1;
      this.startTimer();
   }

   public static CompletableFuture async$getPayload(DesktopPXAPI2 param0, int param1, HttpRequest param2, int param3, CompletableFuture param4, int param5, Object param6) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$initialise(DesktopPXAPI2 param0, int param1, CompletableFuture param2, int param3, Object param4) {
      // $FF: Couldn't be decompiled
   }

   public String getDeviceLang() {
      return "en-GB,en;q=0.9";
   }

   public DesktopPXAPI2(Vertx var1, Logger var2, TaskApiClient var3) {
      super(var1, var2, (ClientType)null);
      this.delegate = var3;
      this.reset();
   }

   public String getDeviceUA() {
      return this.userAgent == null ? DEFAULT_UA.toString() : this.userAgent;
   }

   public CompletableFuture fetchUA() {
      HttpRequest var1 = this.client.postAbs("https://px.hwkapi.com/px/ua").as(BodyCodec.string());
      var1.queryParams().setAll(this.getParams());
      if (this.logger.isDebugEnabled()) {
         this.logger.debug("Fetching new User-Agent");
      }

      return Request.executeTillOk(var1);
   }

   public CompletableFuture solve1() {
      try {
         CompletableFuture var10000 = this.getPayload(1);
         CompletableFuture var2;
         if (!var10000.isDone()) {
            var2 = var10000;
            return var2.exceptionally(Function.identity()).thenCompose(DesktopPXAPI2::async$solve1);
         } else {
            var10000.join();
            var10000 = this.postPayloadPX();
            if (!var10000.isDone()) {
               var2 = var10000;
               return var2.exceptionally(Function.identity()).thenCompose(DesktopPXAPI2::async$solve1);
            } else {
               var10000.join();
               this.metaPayload.put("a", this.pxResponse);
               return CompletableFuture.completedFuture((Object)null);
            }
         }
      } catch (Throwable var3) {
         if (this.logger.isDebugEnabled()) {
            this.logger.debug("Error sending sensor step[1]: {}. Retrying...", var3.getMessage(), var3);
         }

         return CompletableFuture.failedFuture(var3);
      }
   }

   public void scheduleKeepaliveAfterCaptcha() {
      this.scheduleKeepAlive(TimeUnit.MINUTES.toMillis(5L));
   }

   public void startTimer() {
      if (this.vertx == null) {
         if (this.logger.isDebugEnabled()) {
            this.logger.debug("Unable to start keepalive timer: Vertx context is null");
         }

      } else {
         this.cancelKeepalive();
         this.timerId = this.vertx.setTimer(this.expiryTime, this::lambda$startTimer$0);
         if (!this.logger.isDebugEnabled()) {
            this.logger.debug("Scheduled keep-alive to be sent in {}ms", this.expiryTime);
         }

      }
   }

   public static CompletableFuture async$solveCaptcha(DesktopPXAPI2 param0, String param1, String param2, String param3, int param4, int param5, CompletableFuture param6, Throwable param7, int param8, Object param9) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture getPayload(int var1) {
      HttpRequest var2 = this.getApiRequest(var1);

      for(int var3 = 0; var3 < 30; ++var3) {
         try {
            if (this.logger.isDebugEnabled()) {
               this.logger.debug("Fetching payload step '{}' with meta: {}", var1, this.getMetaPayload().encode());
            }

            CompletableFuture var10000 = Request.send(var2, this.getMetaPayload().toBuffer());
            if (!var10000.isDone()) {
               CompletableFuture var6 = var10000;
               return var6.exceptionally(Function.identity()).thenCompose(DesktopPXAPI2::async$getPayload);
            }

            HttpResponse var4 = (HttpResponse)var10000.join();
            if (var4 != null) {
               JsonObject var5 = var4.bodyAsJsonObject();
               if (this.logger.isDebugEnabled()) {
                  this.logger.debug("API payload response received: {}", var5.encode());
               }

               this.currentPayload = var5.getString("result");
               this.flatMapMeta(var5.getJsonObject("meta"));
               if (var5.containsKey("delay")) {
                  this.delay = (int)(var5.getDouble("delay", Double.longBitsToDouble(4607182418800017408L)) * Double.longBitsToDouble(4652007308841189376L));
               }

               return CompletableFuture.completedFuture((Object)null);
            }
         } catch (Throwable var7) {
            if (!var7.getMessage().contains("Unrecognized")) {
               this.logger.warn("Error fetching api params: {}. Retrying...", var7.getMessage());
               if (this.logger.isDebugEnabled()) {
                  this.logger.debug(var7);
               }
            }
         }
      }

      return CompletableFuture.failedFuture(new Exception("Failed to solve sensor"));
   }

   public String getEndpoint(int var1) {
      switch (var1) {
         case 1:
            return "https://px.hwkapi.com/px/1";
         case 2:
            return "https://px.hwkapi.com/px/2";
         case 3:
            return "https://px.hwkapi.com/px/captcha/15";
         case 4:
            return "https://px.hwkapi.com/px/captcha/hold";
         default:
            throw new Exception("Invalid Step: " + var1);
      }
   }

   public HttpRequest getApiRequest(int var1) {
      String var2 = this.getEndpoint(var1);
      HttpRequest var3 = this.client.postAbs(var2).as(BodyCodec.buffer());
      var3.queryParams().setAll(this.getParams());
      var3.putHeader("User-Agent", this.getDeviceUA());
      var3.putHeader("Accept-Encoding", "gzip, deflate");
      var3.putHeader("Accept", "*/*");
      var3.putHeader("Connection", "keep-alive");
      var3.putHeader("Content-Type", "application/json");
      return var3;
   }

   public static CompletableFuture async$solve(DesktopPXAPI2 param0, int param1, CompletableFuture param2, Throwable param3, int param4, Object param5) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture solveImage() {
      CompletableFuture var1;
      try {
         CompletableFuture var10000 = this.getPayload(3);
         CompletableFuture var4;
         if (!var10000.isDone()) {
            var4 = var10000;
            return var4.exceptionally(Function.identity()).thenCompose(DesktopPXAPI2::async$solveImage);
         }

         var10000.join();
         this.pxURI = baseURI + "/b/g";
         var10000 = this.postPayloadPX(true);
         if (!var10000.isDone()) {
            var4 = var10000;
            return var4.exceptionally(Function.identity()).thenCompose(DesktopPXAPI2::async$solveImage);
         }

         var10000.join();
         var1 = CompletableFuture.completedFuture((Object)null);
      } catch (Throwable var7) {
         if (this.logger.isDebugEnabled()) {
            this.logger.debug("Error sending sensor step[3]: {}. Retrying...", var7.getMessage(), var7);
         }

         CompletableFuture var2 = CompletableFuture.failedFuture(var7);
         return var2;
      } finally {
         this.pxURI = baseURI + "/assets/js/bundle";
      }

      return var1;
   }

   public static CompletableFuture async$solve1(DesktopPXAPI2 var0, CompletableFuture var1, int var2, Object var3) {
      Throwable var9;
      label35: {
         CompletableFuture var10000;
         boolean var10001;
         label34: {
            CompletableFuture var8;
            switch (var2) {
               case 0:
                  try {
                     var10000 = var0.getPayload(1);
                     if (!var10000.isDone()) {
                        var8 = var10000;
                        return var8.exceptionally(Function.identity()).thenCompose(DesktopPXAPI2::async$solve1);
                     }
                     break;
                  } catch (Throwable var6) {
                     var9 = var6;
                     var10001 = false;
                     break label35;
                  }
               case 1:
                  var10000 = var1;
                  break;
               case 2:
                  var10000 = var1;
                  break label34;
               default:
                  throw new IllegalArgumentException();
            }

            try {
               var10000.join();
               var10000 = var0.postPayloadPX();
               if (!var10000.isDone()) {
                  var8 = var10000;
                  return var8.exceptionally(Function.identity()).thenCompose(DesktopPXAPI2::async$solve1);
               }
            } catch (Throwable var5) {
               var9 = var5;
               var10001 = false;
               break label35;
            }
         }

         try {
            var10000.join();
            var0.metaPayload.put("a", var0.pxResponse);
            return CompletableFuture.completedFuture((Object)null);
         } catch (Throwable var4) {
            var9 = var4;
            var10001 = false;
         }
      }

      Throwable var7 = var9;
      if (var0.logger.isDebugEnabled()) {
         var0.logger.debug("Error sending sensor step[1]: {}. Retrying...", var7.getMessage(), var7);
      }

      return CompletableFuture.failedFuture(var7);
   }

   public JsonObject getMetaPayload() {
      if (this.metaPayload == null) {
         this.metaPayload = (new JsonObject()).put("ua", this.getDeviceUA());
      }

      String var1 = this.delegate.getCookies().getCookieValue("_pxhd");
      if (var1 != null && !var1.isEmpty() && !var1.isBlank()) {
         this.metaPayload.put("pxhd", var1);
         if (this.logger.isDebugEnabled()) {
            this.logger.debug("Found '_pxhd' value: {}", var1);
         }
      }

      return this.metaPayload;
   }

   public static CompletableFuture async$postPayloadPX(DesktopPXAPI2 param0, int param1, RequestOptions param2, int param3, HttpRequest param4, CompletableFuture param5, Throwable param6, int param7, Object param8) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture initialise() {
      for(int var1 = 0; var1 < 10; ++var1) {
         try {
            if (this.userAgent == null) {
               CompletableFuture var10000 = this.fetchUA();
               if (!var10000.isDone()) {
                  CompletableFuture var4 = var10000;
                  return var4.exceptionally(Function.identity()).thenCompose(DesktopPXAPI2::async$initialise);
               }

               String var2 = (String)var10000.join();
               if (var2 != null && !var2.isEmpty() && !var2.equalsIgnoreCase("error")) {
                  this.userAgent = var2;
                  String var3 = Utils.parseChromeVer(var2);
                  this.secUA = "\"Google Chrome\";v=\"" + var3 + "\", \" Not;A Brand\";v=\"99\", \"Chromium\";v=\"" + var3 + "\"";
                  this.metaPayload = (new JsonObject()).put("ua", this.getDeviceUA());
                  if (this.logger.isDebugEnabled()) {
                     this.logger.debug("Initialised API with userAgent: {}", this.userAgent);
                  }
                  break;
               }
            }
         } catch (Throwable var5) {
            if (this.logger.isDebugEnabled()) {
               this.logger.debug("Failed to initialise: {}", var5.getMessage());
            } else {
               this.logger.warn("Failed to initialise API. Retrying...");
            }
         }
      }

      return CompletableFuture.completedFuture(true);
   }

   public CompletableFuture solve() {
      this.solvingCaptcha = false;
      this.pxURI = baseURI + "/api/v2/collector";

      for(int var1 = 0; var1 < 10; ++var1) {
         CompletableFuture var10000;
         CompletableFuture var3;
         try {
            if (this.userAgent == null) {
               var10000 = this.initialise();
               if (!var10000.isDone()) {
                  var3 = var10000;
                  return var3.exceptionally(Function.identity()).thenCompose(DesktopPXAPI2::async$solve);
               }

               var10000.join();
            }

            var10000 = this.solve1();
            if (!var10000.isDone()) {
               var3 = var10000;
               return var3.exceptionally(Function.identity()).thenCompose(DesktopPXAPI2::async$solve);
            }

            var10000.join();
            var10000 = this.solve2();
            if (!var10000.isDone()) {
               var3 = var10000;
               return var3.exceptionally(Function.identity()).thenCompose(DesktopPXAPI2::async$solve);
            }

            var10000.join();
            MultiMap var2 = MultiMap.caseInsensitiveMultiMap();
            if (this.getVid() != null && !this.getVid().isBlank()) {
               var2.add(VID_COOKIE, this.getVid());
            }

            var2.add(RF_VALUE, ONE_VALUE);
            var2.add(FP_VALUE, ONE_VALUE);
            var2.add(CFP_VALUE, ONE_VALUE);
            this.parseResultCookies(this.pxResponse.encode(), var2);
            if (this.logger.isDebugEnabled()) {
               this.logger.debug("Parsed cookies from normal solve: {}", var2);
            }

            if (!var2.isEmpty()) {
               return CompletableFuture.completedFuture(var2);
            }
         } catch (Throwable var4) {
            this.logger.warn("Error solving sensor: {}. Retrying...", var4.getMessage());
            if (this.logger.isDebugEnabled()) {
               this.logger.debug(var4);
            }

            var10000 = VertxUtil.randomSleep(5000L);
            if (!var10000.isDone()) {
               var3 = var10000;
               return var3.exceptionally(Function.identity()).thenCompose(DesktopPXAPI2::async$solve);
            }

            var10000.join();
         }
      }

      return CompletableFuture.completedFuture(MultiMap.caseInsensitiveMultiMap());
   }

   public CompletableFuture postPayloadPX(boolean var1) {
      RequestOptions var2 = new RequestOptions();
      if (var1 != 0) {
         var2.setAbsoluteURI(this.pxURI);
         var2.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"93\", \" Not;A Brand\";v=\"99\", \"Chromium\";v=\"93\"");
         var2.putHeader("sec-ch-ua-mobile", "?0");
         var2.putHeader("user-agent", this.getDeviceUA());
         var2.putHeader("accept", "*/*");
         var2.putHeader("origin", "https://www.walmart.com");
         var2.putHeader("sec-fetch-site", "cross-site");
         var2.putHeader("sec-fetch-mode", "no-cors");
         var2.putHeader("sec-fetch-dest", "script");
      } else {
         var2.setAbsoluteURI(this.pxURI);
         var2.putHeader("content-length", "DEFAULT_VALUE");
         var2.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"93\", \" Not;A Brand\";v=\"99\", \"Chromium\";v=\"93\"");
         var2.putHeader("sec-ch-ua-mobile", "?1");
         var2.putHeader("user-agent", this.getDeviceUA());
         var2.putHeader("content-type", "application/x-www-form-urlencoded");
         var2.putHeader("accept", "*/*");
         var2.putHeader("origin", "https://www.walmart.com");
         var2.putHeader("sec-fetch-site", "cross-site");
         var2.putHeader("sec-fetch-mode", "cors");
         var2.putHeader("sec-fetch-dest", "empty");
      }

      var2.putHeader("referer", "https://www.walmart.com/");
      var2.putHeader("accept-encoding", "gzip, deflate, br");
      var2.putHeader("accept-language", this.getDeviceLang());
      var2.setTimeout(TimeUnit.SECONDS.toMillis(15L));

      for(int var3 = 0; var3 < 30; ++var3) {
         CompletableFuture var11;
         CompletableFuture var10000;
         try {
            if (this.logger.isDebugEnabled()) {
               this.logger.debug("Posting payload: {} to PX via URI: {}", this.currentPayload, var2.getURI());
            }

            HttpResponse var4;
            if (var1 != 0) {
               HttpRequest var5 = this.delegate.getWebClient().request(HttpMethod.GET, var2);
               String[] var6 = this.currentPayload.split("&");
               int var7 = var6.length;

               for(int var8 = 0; var8 < var7; ++var8) {
                  String var9 = var6[var8];
                  String[] var10 = var9.split("=");
                  var5.addQueryParam(var10[0], var10[1]);
               }

               var10000 = Request.send(var5);
               if (!var10000.isDone()) {
                  var11 = var10000;
                  return var11.exceptionally(Function.identity()).thenCompose(DesktopPXAPI2::async$postPayloadPX);
               }

               var4 = (HttpResponse)var10000.join();
            } else {
               var10000 = Request.send(this.delegate.getWebClient().request(HttpMethod.POST, var2), Buffer.buffer(this.currentPayload));
               if (!var10000.isDone()) {
                  var11 = var10000;
                  return var11.exceptionally(Function.identity()).thenCompose(DesktopPXAPI2::async$postPayloadPX);
               }

               var4 = (HttpResponse)var10000.join();
            }

            if (var4 != null && var4.statusCode() == 200) {
               if (var1 == 0) {
                  if (this.logger.isDebugEnabled()) {
                     this.logger.debug("PX response received: {}", var4.bodyAsString());
                  }

                  this.pxResponse = var4.bodyAsJsonObject();
               }

               return CompletableFuture.completedFuture((Object)null);
            }
         } catch (Throwable var12) {
            this.logger.warn("Error sending sensor: {}. Retrying...", var12.getMessage());
            if (this.logger.isDebugEnabled()) {
               this.logger.debug(var12);
            }

            var10000 = VertxUtil.randomSleep(3000L);
            if (!var10000.isDone()) {
               var11 = var10000;
               return var11.exceptionally(Function.identity()).thenCompose(DesktopPXAPI2::async$postPayloadPX);
            }

            var10000.join();
         }
      }

      return CompletableFuture.failedFuture(new Exception("Failed to send sensor"));
   }

   public String getVid() {
      return this.metaPayload == null ? null : this.metaPayload.getString("vid", (String)null);
   }

   public CompletableFuture solve2() {
      try {
         CompletableFuture var10000 = this.getPayload(2);
         CompletableFuture var2;
         if (!var10000.isDone()) {
            var2 = var10000;
            return var2.exceptionally(Function.identity()).thenCompose(DesktopPXAPI2::async$solve2);
         } else {
            var10000.join();
            var10000 = this.postPayloadPX();
            if (!var10000.isDone()) {
               var2 = var10000;
               return var2.exceptionally(Function.identity()).thenCompose(DesktopPXAPI2::async$solve2);
            } else {
               var10000.join();
               return CompletableFuture.completedFuture((Object)null);
            }
         }
      } catch (Throwable var3) {
         if (this.logger.isDebugEnabled()) {
            this.logger.debug("Error sending sensor step[2]: {}. Retrying...", var3.getMessage(), var3);
         }

         return CompletableFuture.failedFuture(var3);
      }
   }

   public static CompletableFuture async$solve2(DesktopPXAPI2 var0, CompletableFuture var1, int var2, Object var3) {
      Throwable var9;
      label35: {
         CompletableFuture var10000;
         boolean var10001;
         label34: {
            CompletableFuture var8;
            switch (var2) {
               case 0:
                  try {
                     var10000 = var0.getPayload(2);
                     if (!var10000.isDone()) {
                        var8 = var10000;
                        return var8.exceptionally(Function.identity()).thenCompose(DesktopPXAPI2::async$solve2);
                     }
                     break;
                  } catch (Throwable var6) {
                     var9 = var6;
                     var10001 = false;
                     break label35;
                  }
               case 1:
                  var10000 = var1;
                  break;
               case 2:
                  var10000 = var1;
                  break label34;
               default:
                  throw new IllegalArgumentException();
            }

            try {
               var10000.join();
               var10000 = var0.postPayloadPX();
               if (!var10000.isDone()) {
                  var8 = var10000;
                  return var8.exceptionally(Function.identity()).thenCompose(DesktopPXAPI2::async$solve2);
               }
            } catch (Throwable var5) {
               var9 = var5;
               var10001 = false;
               break label35;
            }
         }

         try {
            var10000.join();
            return CompletableFuture.completedFuture((Object)null);
         } catch (Throwable var4) {
            var9 = var4;
            var10001 = false;
         }
      }

      Throwable var7 = var9;
      if (var0.logger.isDebugEnabled()) {
         var0.logger.debug("Error sending sensor step[2]: {}. Retrying...", var7.getMessage(), var7);
      }

      return CompletableFuture.failedFuture(var7);
   }

   public String getDeviceAcceptEncoding() {
      return "gzip";
   }

   public DesktopPXAPI2(TaskActor var1) {
      super(var1, (ClientType)null);
      this.delegate = var1.getClient();
      this.reset();
   }

   public void scheduleKeepalive() {
      this.scheduleKeepAlive(TimeUnit.MINUTES.toMillis(4L));
   }

   public void reset() {
      this.userAgent = null;
      this.referrer = null;
      this.solvingCaptcha = false;
      this.currentPayload = null;
      this.metaPayload = null;
      this.pxResponse = null;
      this.delay = 100;
      this.failedSolves = 0;
   }

   public CompletableFuture postPayloadPX() {
      return this.postPayloadPX(false);
   }

   public void lambda$startTimer$0(Long var1) {
      this.onKeepalive();
   }

   public String getDeviceSecUA() {
      return this.secUA;
   }

   public Optional onResult(String var1) {
      if (!var1.contains("cv|0")) {
         return Optional.empty();
      } else {
         Matcher var2 = BAKE_PATTERN.matcher(var1);
         return var2.find() ? Optional.of("3:" + var2.group(1)) : Optional.empty();
      }
   }
}
