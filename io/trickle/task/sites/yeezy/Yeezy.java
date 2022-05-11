package io.trickle.task.sites.yeezy;

import io.netty.handler.codec.http.cookie.Cookie;
import io.trickle.core.Engine;
import io.trickle.core.VertxSingleton;
import io.trickle.core.actor.TaskActor;
import io.trickle.harvester.pooled.AbstractSharedHarvesterController;
import io.trickle.harvester.pooled.AutosolveHarvesterController;
import io.trickle.harvester.pooled.BrowserHarvesterController;
import io.trickle.harvester.pooled.SharedCaptchaToken;
import io.trickle.profile.Profile;
import io.trickle.proxy.ProxyController;
import io.trickle.task.Task;
import io.trickle.task.TaskController;
import io.trickle.task.antibot.impl.akamai.GaneshAPI;
import io.trickle.task.antibot.impl.akamai.HawkAPI;
import io.trickle.task.antibot.impl.akamai.pixel.Pixel;
import io.trickle.task.sites.yeezy.util.AdyenFingerprints;
import io.trickle.task.sites.yeezy.util.AdyenForm;
import io.trickle.task.sites.yeezy.util.CodeScreen;
import io.trickle.task.sites.yeezy.util.RT;
import io.trickle.task.sites.yeezy.util.Sizes;
import io.trickle.task.sites.yeezy.util.Sizes$NoAvailableSizeException;
import io.trickle.task.sites.yeezy.util.Utag;
import io.trickle.task.sites.yeezy.util.Window3DS2;
import io.trickle.task.sites.yeezy.util.rotator.ZipCodeGroup;
import io.trickle.util.Storage;
import io.trickle.util.Utils;
import io.trickle.util.analytics.Analytics;
import io.trickle.util.concurrent.ContextCompletableFuture;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.trickle.webclient.CookieJar;
import io.trickle.webclient.RealClient;
import io.trickle.webclient.RealClientFactory;
import io.vertx.core.AsyncResult;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.spi.CookieStore;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.ext.web.multipart.MultipartForm;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Yeezy extends TaskActor {
   public static String V3_COOKIE_NAME;
   public String basketID;
   public String instanceSignal;
   public boolean visitedBaskets = false;
   public boolean isPreload;
   public boolean isPassedFW = true;
   public boolean isSent = false;
   public boolean isTurboQueue;
   public int signUpSaveCount = 0;
   public static Pattern SENSOR_URL_PATTERN = Pattern.compile("<script type=\"text/javascript\"\\s\\ssrc=\"(.*?)\"");
   public boolean isBannedOnShipping = false;
   public YeezyAPI api;
   public static Pattern POW_PAGE_PATTERN;
   public static AbstractSharedHarvesterController autoSolveHarvesters;
   public int previousResponseHash = 0;
   public static int QUEUE_POOL_INTERVAL;
   public String harvesterID = null;
   public long pixelPostTs;
   public boolean isFirstIteration = true;
   public static int successfulCartCount;
   public int sensorPosts = 0;
   public boolean rotatedProfile = false;
   public AbstractSharedHarvesterController harvesters;
   public String authorization;
   public Task task;
   public ZipCodeGroup profilesOfZipCode;
   public static int fwCartCount;
   public List availableSizes = new ArrayList();
   public static Pattern CHROME_VERSION_PATTERN = Pattern.compile("Chrome/(.*?) ");
   public String orderID;
   public static Pattern SIGNUP_ID_PATTERN;
   public AtomicReference tokenRef = new AtomicReference((Object)null);
   public ArrayList queueSessions;
   public boolean safeToRetain = true;
   public static boolean RATIO_FW_SKIP_ELIGIBLE;
   public boolean isNoProtection;
   public static int RETRY_BOUND = 3000;
   public static AbstractSharedHarvesterController browserHarvesters;
   public Profile instanceProfile;
   public HawkAPI tempHawk = new HawkAPI();
   public int previousResponseLen = 0;
   public boolean shouldRotate = false;

   public CompletableFuture waitForSale() {
      HttpRequest var1 = this.api.waitingRoomConfig();
      this.logger.info("Fetching sale status.");
      this.netLogInfo("Fetching sale status.");

      while(super.running) {
         CompletableFuture var10000;
         CompletableFuture var5;
         try {
            var10000 = Request.send(var1);
            if (!var10000.isDone()) {
               var5 = var10000;
               return var5.exceptionally(Function.identity()).thenCompose(Yeezy::async$waitForSale);
            }

            HttpResponse var2 = (HttpResponse)var10000.join();
            if (Engine.get().getClientConfiguration().getBoolean("forceQueue", false) || this.task.getMode().contains("skip")) {
               long var3 = Engine.get().getClientConfiguration().getLong("queueTime", 0L) - System.currentTimeMillis();
               if (var3 >= 5L && var3 <= 30000L) {
                  var10000 = VertxUtil.hardCodedSleep(var3);
                  if (!var10000.isDone()) {
                     var5 = var10000;
                     return var5.exceptionally(Function.identity()).thenCompose(Yeezy::async$waitForSale);
                  }

                  var10000.join();
                  return CompletableFuture.completedFuture((Object)null);
               }

               if (var3 < 5L) {
                  return CompletableFuture.completedFuture((Object)null);
               }
            }

            if (var2 != null) {
               if (((String)var2.body()).contains("HTTP 403 - Forbidden")) {
                  this.logger.warn("Failed to check sale status: state=PROXY_BAN");
                  var10000 = VertxUtil.sleep((long)this.task.getMonitorDelay());
                  if (!var10000.isDone()) {
                     var5 = var10000;
                     return var5.exceptionally(Function.identity()).thenCompose(Yeezy::async$waitForSale);
                  }

                  var10000.join();
                  return CompletableFuture.failedFuture(new Exception("403 ERROR"));
               }

               if (var2.statusCode() == 200) {
                  if (((String)var2.body()).toLowerCase().contains("sale_started") || this.task.getMode().contains("noqueue")) {
                     return CompletableFuture.completedFuture((Object)null);
                  }

                  this.logger.info("Sale not live yet. Waiting...");
                  var10000 = VertxUtil.randomSleep(8000L);
                  if (!var10000.isDone()) {
                     var5 = var10000;
                     return var5.exceptionally(Function.identity()).thenCompose(Yeezy::async$waitForSale);
                  }

                  var10000.join();
               }
            } else {
               this.logger.warn("Failed to fetch sale status: state=NO_REPLY");
            }
         } catch (Exception var6) {
            this.logger.warn("Failed to fetch sale status: {}", var6.getMessage());
            this.netLogError("Failed to fetch sale status: message='" + var6.getMessage() + "'");
            var10000 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
            if (!var10000.isDone()) {
               var5 = var10000;
               return var5.exceptionally(Function.identity()).thenCompose(Yeezy::async$waitForSale);
            }

            var10000.join();
         }
      }

      return CompletableFuture.failedFuture(new Exception("403 ERROR"));
   }

   public static CompletableFuture async$smartUA(Yeezy var0, YeezyAPI var1, CompletableFuture var2, int var3, Object var4) {
      YeezyAPI var10000;
      CompletableFuture var10001;
      label48: {
         switch (var3) {
            case 0:
               if (var0.isFirewallOn()) {
                  return CompletableFuture.completedFuture((Object)null);
               }

               if (var0.api.getPixelAPI() instanceof HawkAPI) {
                  var10000 = var0.api;
                  var10001 = ((HawkAPI)var0.api.getPixelAPI()).updateUserAgent();
                  if (!var10001.isDone()) {
                     var2 = var10001;
                     var1 = var10000;
                     return var2.exceptionally(Function.identity()).thenCompose(Yeezy::async$smartUA);
                  }
                  break label48;
               }

               if (!(var0.api.getPixelAPI() instanceof GaneshAPI)) {
                  return CompletableFuture.completedFuture((Object)null);
               }

               var10000 = var0.api;
               var10001 = ((GaneshAPI)var0.api.getPixelAPI()).updateUserAgent();
               if (!var10001.isDone()) {
                  var2 = var10001;
                  var1 = var10000;
                  return var2.exceptionally(Function.identity()).thenCompose(Yeezy::async$smartUA);
               }
               break;
            case 1:
               var10000 = var1;
               var10001 = var2;
               break label48;
            case 2:
               var10000 = var1;
               var10001 = var2;
               break;
            default:
               throw new IllegalArgumentException();
         }

         var10000.updateUserAgent((String)var10001.join());
         return CompletableFuture.completedFuture((Object)null);
      }

      var10000.updateUserAgent((String)var10001.join());
      return CompletableFuture.completedFuture((Object)null);
   }

   public static int lambda$fillSizes$5(Object var0, Object var1) {
      Integer var2 = ((JsonObject)var0).getInteger("availability", 0);
      Integer var3 = ((JsonObject)var1).getInteger("availability", 0);
      return var3.compareTo(var2);
   }

   public boolean smartSwapEnabled() {
      return Engine.get().getClientConfiguration().getBoolean("smartRotate", false);
   }

   public CompletableFuture postSensors() {
      if (!Engine.get().getClientConfiguration().getBoolean("noProtect", false) && !this.isNoProtection) {
         String var1 = this.api.userAgent;
         CookieJar var2 = this.api.getCookies();
         if (!this.isFirewallOn()) {
            this.api.getWebClient().resetCookieStore();
            Iterator var3 = var2.get(true, ".yeezysupply.com", "/").iterator();

            label100:
            while(true) {
               Cookie var4;
               do {
                  if (!var3.hasNext()) {
                     break label100;
                  }

                  var4 = (Cookie)var3.next();
               } while(!var4.name().contains("_abck") && !var4.name().startsWith("bm_") && !var4.name().equals("ak_bmsc") && !var4.name().equalsIgnoreCase("RT"));

               this.api.getCookies().put(var4);
               var2.remove(var4);
            }
         }

         CompletableFuture var10000;
         CompletableFuture var6;
         for(int var7 = 0; var7 < ThreadLocalRandom.current().nextInt(2, 4); ++var7) {
            var10000 = this.sendSensor(this.api.postSensor(true), false);
            if (!var10000.isDone()) {
               var6 = var10000;
               return var6.exceptionally(Function.identity()).thenCompose(Yeezy::async$postSensors);
            }

            var10000.join();
            var10000 = this.smartSleep(500L);
            if (!var10000.isDone()) {
               var6 = var10000;
               return var6.exceptionally(Function.identity()).thenCompose(Yeezy::async$postSensors);
            }

            var10000.join();
         }

         String var8 = "";
         int var9 = 0;

         for(int var5 = ThreadLocalRandom.current().nextInt(9, 16); var9++ < var5; ++this.sensorPosts) {
            var10000 = this.smartSleep(750L);
            if (!var10000.isDone()) {
               var6 = var10000;
               return var6.exceptionally(Function.identity()).thenCompose(Yeezy::async$postSensors);
            }

            var10000.join();
            var10000 = this.smartUA();
            if (!var10000.isDone()) {
               var6 = var10000;
               return var6.exceptionally(Function.identity()).thenCompose(Yeezy::async$postSensors);
            }

            var10000.join();
            var10000 = this.sendSensor(this.api.postSensor(true), true);
            if (!var10000.isDone()) {
               var6 = var10000;
               return var6.exceptionally(Function.identity()).thenCompose(Yeezy::async$postSensors);
            }

            var8 = (String)var10000.join();
            if (var9 > 6 && var8 != null && var8.contains("false")) {
               this.logger.info("YS site down!");
               break;
            }

            if (this.api.getCookies().getCookieValue("_abck").length() >= 561 && !this.api.getCookies().getCookieValue("_abck").contains("||")) {
               this.logger.info("Passed sensor validation step -> Handling sensor step='{}/{}' - len={}", var9, var5, this.api.getCookies().getCookieValue("_abck").length());
               if (var9 > 4 && this.isBannedOnShipping) {
                  break;
               }
            } else {
               this.logger.info("Handling sensor step='{}/{}' len={}", var9, var5, this.api.getCookies().getCookieValue("_abck").length());
            }
         }

         if (var8 != null) {
            this.logger.info("Posted sensor: c={} l={} val={}", var9, this.api.getCookies().getCookieValue("_abck").length(), var8.replace("\n", " "));
         } else {
            this.logger.info("Sensor not posted properly. Continuing.");
         }

         if (!this.isFirewallOn()) {
            this.api.getCookies().putFromOther(var2);
            this.api.updateUserAgent(var1);
         }

         return CompletableFuture.completedFuture((Object)null);
      } else {
         return CompletableFuture.completedFuture((Object)null);
      }
   }

   public void lambda$requestCaptcha$3(String var1) {
      this.logger.info("Swapped from harvester={} to new_harvester={}", this.harvesterID, var1);
      this.harvesterID = var1;
   }

   public static CompletableFuture async$postSensorsTilNoChallenge(Yeezy var0, String var1, CompletableFuture var2, int var3, Object var4) {
      CompletableFuture var10000;
      switch (var3) {
         case 0:
            var1 = "";
            var10000 = var0.sendSensor(var0.api.postSensor(true), true);
            if (!var10000.isDone()) {
               var2 = var10000;
               return var2.exceptionally(Function.identity()).thenCompose(Yeezy::async$postSensorsTilNoChallenge);
            }

            var10000.join();
            var10000 = VertxUtil.randomSleep(1000L);
            if (!var10000.isDone()) {
               var2 = var10000;
               return var2.exceptionally(Function.identity()).thenCompose(Yeezy::async$postSensorsTilNoChallenge);
            }

            var10000.join();
            var10000 = var0.sendSensor(var0.api.postSensor(true), true);
            if (!var10000.isDone()) {
               var2 = var10000;
               return var2.exceptionally(Function.identity()).thenCompose(Yeezy::async$postSensorsTilNoChallenge);
            }

            var10000.join();
            break;
         case 1:
            var2.join();
            var10000 = VertxUtil.randomSleep(1000L);
            if (!var10000.isDone()) {
               var2 = var10000;
               return var2.exceptionally(Function.identity()).thenCompose(Yeezy::async$postSensorsTilNoChallenge);
            }

            var10000.join();
            var10000 = var0.sendSensor(var0.api.postSensor(true), true);
            if (!var10000.isDone()) {
               var2 = var10000;
               return var2.exceptionally(Function.identity()).thenCompose(Yeezy::async$postSensorsTilNoChallenge);
            }

            var10000.join();
            break;
         case 2:
            var2.join();
            var10000 = var0.sendSensor(var0.api.postSensor(true), true);
            if (!var10000.isDone()) {
               var2 = var10000;
               return var2.exceptionally(Function.identity()).thenCompose(Yeezy::async$postSensorsTilNoChallenge);
            }

            var10000.join();
            break;
         case 3:
            var2.join();
            break;
         case 4:
            var2.join();
            var10000 = var0.sendSensor(var0.api.postSensor(true), true);
            if (!var10000.isDone()) {
               var2 = var10000;
               return var2.exceptionally(Function.identity()).thenCompose(Yeezy::async$postSensorsTilNoChallenge);
            }

            var1 = (String)var10000.join();
            if (var0.logger.isDebugEnabled()) {
               var0.logger.debug(var1);
            } else {
               if (var1 != null && var1.contains("false")) {
                  var0.logger.info("YS site down!");
                  return CompletableFuture.completedFuture((Object)null);
               }

               if (!var0.api.getCookies().getCookieValue("_abck").contains("||")) {
                  var0.logger.info("Passed challenge l={}", var0.api.getCookies().getCookieValue("_abck").length());
                  return CompletableFuture.completedFuture((Object)null);
               }

               var0.logger.info("Handling sensor step - l={}", var0.api.getCookies().getCookieValue("_abck").length());
            }

            ++var0.sensorPosts;
            break;
         case 5:
            var1 = (String)var2.join();
            if (var0.logger.isDebugEnabled()) {
               var0.logger.debug(var1);
            } else {
               if (var1 != null && var1.contains("false")) {
                  var0.logger.info("YS site down!");
                  return CompletableFuture.completedFuture((Object)null);
               }

               if (!var0.api.getCookies().getCookieValue("_abck").contains("||")) {
                  var0.logger.info("Passed challenge l={}", var0.api.getCookies().getCookieValue("_abck").length());
                  return CompletableFuture.completedFuture((Object)null);
               }

               var0.logger.info("Handling sensor step - l={}", var0.api.getCookies().getCookieValue("_abck").length());
            }

            ++var0.sensorPosts;
            break;
         default:
            throw new IllegalArgumentException();
      }

      for(; var0.api.getCookies().getCookieValue("_abck").contains("||"); ++var0.sensorPosts) {
         var10000 = var0.smartSleep(1000L);
         if (!var10000.isDone()) {
            var2 = var10000;
            return var2.exceptionally(Function.identity()).thenCompose(Yeezy::async$postSensorsTilNoChallenge);
         }

         var10000.join();
         var10000 = var0.sendSensor(var0.api.postSensor(true), true);
         if (!var10000.isDone()) {
            var2 = var10000;
            return var2.exceptionally(Function.identity()).thenCompose(Yeezy::async$postSensorsTilNoChallenge);
         }

         var1 = (String)var10000.join();
         if (var0.logger.isDebugEnabled()) {
            var0.logger.debug(var1);
         } else {
            if (var1 != null && var1.contains("false")) {
               var0.logger.info("YS site down!");
               break;
            }

            if (!var0.api.getCookies().getCookieValue("_abck").contains("||")) {
               var0.logger.info("Passed challenge l={}", var0.api.getCookies().getCookieValue("_abck").length());
               break;
            }

            var0.logger.info("Handling sensor step - l={}", var0.api.getCookies().getCookieValue("_abck").length());
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture smartUA() {
      if (!this.isFirewallOn()) {
         YeezyAPI var10000;
         CompletableFuture var10001;
         YeezyAPI var1;
         CompletableFuture var2;
         if (this.api.getPixelAPI() instanceof HawkAPI) {
            var10000 = this.api;
            var10001 = ((HawkAPI)this.api.getPixelAPI()).updateUserAgent();
            if (!var10001.isDone()) {
               var2 = var10001;
               var1 = var10000;
               return var2.exceptionally(Function.identity()).thenCompose(Yeezy::async$smartUA);
            }

            var10000.updateUserAgent((String)var10001.join());
         } else if (this.api.getPixelAPI() instanceof GaneshAPI) {
            var10000 = this.api;
            var10001 = ((GaneshAPI)this.api.getPixelAPI()).updateUserAgent();
            if (!var10001.isDone()) {
               var2 = var10001;
               var1 = var10000;
               return var2.exceptionally(Function.identity()).thenCompose(Yeezy::async$smartUA);
            }

            var10000.updateUserAgent((String)var10001.join());
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$preloadBuffer(Yeezy var0, String var1, String var2, CompletableFuture var3, int var4, Object var5) {
      CompletableFuture var10000;
      switch (var4) {
         case 0:
            try {
               var1 = var0.api.getCookies().getCookieValue("_abck");
               var2 = var0.api.getCookies().getCookieValue("bm_sz");
            } catch (Throwable var6) {
               var0.logger.error("Failed to set value for sensor api: {}", var6.getMessage());
               var1 = "";
               var2 = "";
            }

            if (var0.tempHawk == null) {
               var0.tempHawk = new HawkAPI();
            }

            var0.tempHawk.setUseragent(var0.api.userAgent);
            var10000 = var0.tempHawk.getSensorPayload(var1, var2, var0.task.getKeywords()[0].toUpperCase(), true);
            if (!var10000.isDone()) {
               CompletableFuture var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(Yeezy::async$preloadBuffer);
            }
            break;
         case 1:
            var10000 = var3;
            break;
         default:
            throw new IllegalArgumentException();
      }

      String var7 = (String)var10000.join();
      return CompletableFuture.completedFuture((new JsonObject()).put("sensor_data", var7).toBuffer());
   }

   public static CompletableFuture async$sendPixel(Yeezy param0, HttpRequest param1, String param2, String param3, String param4, CompletableFuture param5, String param6, MultiMap param7, HttpResponse param8, Exception param9, int param10, Object param11) {
      // $FF: Couldn't be decompiled
   }

   public boolean isCodeRequired(String var1) {
      try {
         JsonObject var2 = new JsonObject(var1);
         if (var2.containsKey("metashared")) {
            return var2.getBoolean("metashared", false);
         }
      } catch (Throwable var3) {
         this.logger.warn("Failed to parse if code is requested: {}", var3.getMessage());
      }

      return false;
   }

   public static CompletableFuture async$fillSizes(Yeezy var0, CompletableFuture var1, int var2, Object var3) {
      CompletableFuture var10000;
      switch (var2) {
         case 0:
            var10000 = var0.getSizeJson(!var0.availableSizes.isEmpty());
            if (!var10000.isDone()) {
               CompletableFuture var4 = var10000;
               return var4.exceptionally(Function.identity()).thenCompose(Yeezy::async$fillSizes);
            }
            break;
         case 1:
            var10000 = var1;
            break;
         default:
            throw new IllegalArgumentException();
      }

      String var6 = (String)var10000.join();

      try {
         JsonArray var7 = (new JsonObject(var6)).getJsonArray("variation_list");
         List var8 = (List)var7.stream().sorted(Yeezy::lambda$fillSizes$5).map(Yeezy::lambda$fillSizes$6).collect(Collectors.toList());
         if (!var8.isEmpty()) {
            var0.availableSizes.clear();
            var0.availableSizes.addAll(var8);
         }
      } catch (Throwable var5) {
         var0.logger.error("Could not order sizes (Normal. Don't panic): {}", var5.getMessage());
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$getBloom(Yeezy var0, HttpRequest var1, CompletableFuture var2, HttpResponse var3, int var4, Object var5) {
      CompletableFuture var10000;
      label94: {
         CompletableFuture var6;
         switch (var4) {
            case 0:
               var1 = var0.api.bloom();
               var0.logger.info("Fetching bloom");
               var0.netLogInfo("Fetching bloom");
               var10000 = Request.send(var1);
               if (!var10000.isDone()) {
                  var6 = var10000;
                  return var6.exceptionally(Function.identity()).thenCompose(Yeezy::async$getBloom);
               }
               break;
            case 1:
               var10000 = var2;
               break;
            case 2:
               var10000 = var2;
               break label94;
            case 3:
               var2.join();
               var10000 = Request.send(var1);
               if (!var10000.isDone()) {
                  var6 = var10000;
                  return var6.exceptionally(Function.identity()).thenCompose(Yeezy::async$getBloom);
               }
               break;
            default:
               throw new IllegalArgumentException();
         }

         do {
            HttpResponse var8 = (HttpResponse)var10000.join();
            if (var8 != null) {
               if (((String)var8.body()).contains("HTTP 403 - Forbidden")) {
                  var0.logger.warn("Failed to fetch bloom: state=PROXY_BAN");
                  var10000 = VertxUtil.randomSleep(3000L);
                  if (!var10000.isDone()) {
                     var6 = var10000;
                     return var6.exceptionally(Function.identity()).thenCompose(Yeezy::async$getBloom);
                  }
                  break label94;
               }

               if (var8.statusCode() == 200) {
                  var0.logger.info("Fetched bloom: state=OK");

                  try {
                     JsonArray var9 = new JsonArray((String)var8.body());

                     for(var4 = 0; var4 < var9.size(); ++var4) {
                        JsonObject var10 = var9.getJsonObject(var4);
                        if (var10.getString("product_id").equals(var0.api.getSKU())) {
                           return CompletableFuture.completedFuture(var10.getString("product_name"));
                        }
                     }
                  } catch (Exception var7) {
                     var0.logger.error("Unable to find tag? Continuing... " + var7.getMessage());
                  }

                  return CompletableFuture.completedFuture("YEEZY BOOST 350 V2");
               }

               if (var8.statusCode() == 500) {
                  var0.logger.error("Bloom site down continuing...");
                  return CompletableFuture.completedFuture("YEEZY SLIDE ADULT");
               }

               var0.logger.warn("Failed to fetch bloom: status={}", var8.statusCode());
               var0.netLogWarn("Failed to fetch bloom: status=" + var8.statusCode());
               var10000 = VertxUtil.sleep((long)var0.task.getRetryDelay());
               if (!var10000.isDone()) {
                  var6 = var10000;
                  return var6.exceptionally(Function.identity()).thenCompose(Yeezy::async$getBloom);
               }

               var10000.join();
            } else {
               var0.logger.warn("Failed to fetch bloom: state=NO_REPLY");
            }

            var10000 = Request.send(var1);
         } while(var10000.isDone());

         var6 = var10000;
         return var6.exceptionally(Function.identity()).thenCompose(Yeezy::async$getBloom);
      }

      var10000.join();
      return CompletableFuture.failedFuture(new Exception("403 BLOOM ERROR"));
   }

   public CompletableFuture sendSensor(HttpRequest var1, boolean var2) {
      if (!Engine.get().getClientConfiguration().getBoolean("noProtect", false) && !this.isNoProtection) {
         CompletableFuture var10000;
         CompletableFuture var5;
         try {
            var10000 = this.sensorReqBuffer((boolean)var2);
            if (!var10000.isDone()) {
               var5 = var10000;
               return var5.exceptionally(Function.identity()).thenCompose(Yeezy::async$sendSensor);
            }

            Buffer var3 = (Buffer)var10000.join();
            var10000 = Request.send(var1, var3);
            if (!var10000.isDone()) {
               var5 = var10000;
               return var5.exceptionally(Function.identity()).thenCompose(Yeezy::async$sendSensor);
            }

            HttpResponse var4 = (HttpResponse)var10000.join();
            if (var4 != null && var4.statusCode() != 201) {
               this.logger.warn("Failed to send sensor: status={}", var4.statusCode());
               return CompletableFuture.completedFuture((Object)null);
            }

            if (var4 != null) {
               return CompletableFuture.completedFuture(var4.bodyAsString());
            }

            var10000 = VertxUtil.randomSignalSleep(this.instanceSignal, (long)this.task.getRetryDelay());
            if (!var10000.isDone()) {
               var5 = var10000;
               return var5.exceptionally(Function.identity()).thenCompose(Yeezy::async$sendSensor);
            }

            var10000.join();
         } catch (Exception var6) {
            this.logger.error("Error occurred on sending sensor: '{}'", var6.getMessage());
            var10000 = VertxUtil.randomSleep(3000L);
            if (!var10000.isDone()) {
               var5 = var10000;
               return var5.exceptionally(Function.identity()).thenCompose(Yeezy::async$sendSensor);
            }

            var10000.join();
         }

         return CompletableFuture.completedFuture((Object)null);
      } else {
         return CompletableFuture.completedFuture("");
      }
   }

   public static CompletableFuture async$handlePOW(Yeezy var0, String var1, Function var2, String var3, CompletableFuture var4, String var5, Yeezy var6, String var7, Supplier var8, int var9, Object var10) {
      CompletableFuture var10000;
      label100: {
         String var11;
         CompletableFuture var12;
         String var13;
         CompletableFuture var14;
         Supplier var10003;
         CompletableFuture var10004;
         label96: {
            switch (var9) {
               case 0:
                  var2 = Yeezy::lambda$handlePOW$0;
                  String var15 = Utils.quickParseFirst(var1, POW_PAGE_PATTERN);
                  var3 = "https://www.yeezysupply.com" + var15;
                  var10000 = var0.execute("POW page", var2, var0::lambda$handlePOW$1, (Object)null);
                  if (!var10000.isDone()) {
                     var12 = var10000;
                     return var12.exceptionally(Function.identity()).thenCompose(Yeezy::async$handlePOW);
                  }

                  var10000.join();
                  var11 = var0.api.getCookies().getCookieValue("sec_cpt");
                  break;
               case 1:
                  var4.join();
                  var11 = var0.api.getCookies().getCookieValue("sec_cpt");
                  break;
               case 2:
                  var10004 = var4;
                  var11 = var5;
                  var10000 = var6.execute(var7, (Function)null, var8, var10004.join());
                  if (!var10000.isDone()) {
                     var12 = var10000;
                     return var12.exceptionally(Function.identity()).thenCompose(Yeezy::async$handlePOW);
                  }

                  var10000.join();
                  var10000 = VertxUtil.hardCodedSleep(2000L);
                  if (!var10000.isDone()) {
                     var12 = var10000;
                     return var12.exceptionally(Function.identity()).thenCompose(Yeezy::async$handlePOW);
                  }
                  break label96;
               case 3:
                  var10000 = var4;
                  var11 = var5;
                  var10000.join();
                  var10000 = VertxUtil.hardCodedSleep(2000L);
                  if (!var10000.isDone()) {
                     var12 = var10000;
                     return var12.exceptionally(Function.identity()).thenCompose(Yeezy::async$handlePOW);
                  }
                  break label96;
               case 4:
                  var10000 = var4;
                  var11 = var5;
                  break label96;
               case 5:
                  var10000 = var4;
                  break label100;
               default:
                  throw new IllegalArgumentException();
            }

            var10003 = var0::lambda$handlePOW$2;
            var10004 = var0.sensorReqBuffer(false);
            if (!var10004.isDone()) {
               var14 = var10004;
               var8 = var10003;
               var7 = null;
               var13 = "sensor";
               return var14.exceptionally(Function.identity()).thenCompose(Yeezy::async$handlePOW);
            }

            var10000 = var0.execute("sensor", (Function)null, var10003, var10004.join());
            if (!var10000.isDone()) {
               var12 = var10000;
               return var12.exceptionally(Function.identity()).thenCompose(Yeezy::async$handlePOW);
            }

            var10000.join();
            var10000 = VertxUtil.hardCodedSleep(2000L);
            if (!var10000.isDone()) {
               var12 = var10000;
               return var12.exceptionally(Function.identity()).thenCompose(Yeezy::async$handlePOW);
            }
         }

         do {
            var10000.join();
            YeezyAPI var16;
            if (var0.api.getCookies().getCookieValue("sec_cpt") == null) {
               var16 = var0.api;
               Objects.requireNonNull(var16);
               var10000 = var0.execute("POW completion", var2, var16::verifyPage, (Object)null);
               if (!var10000.isDone()) {
                  var12 = var10000;
                  return var12.exceptionally(Function.identity()).thenCompose(Yeezy::async$handlePOW);
               }
               break label100;
            }

            if (!var0.api.getCookies().getCookieValue("sec_cpt").equals(var11)) {
               var16 = var0.api;
               Objects.requireNonNull(var16);
               var10000 = var0.execute("POW completion", var2, var16::verifyPage, (Object)null);
               if (!var10000.isDone()) {
                  var12 = var10000;
                  return var12.exceptionally(Function.identity()).thenCompose(Yeezy::async$handlePOW);
               }
               break label100;
            }

            var10003 = var0::lambda$handlePOW$2;
            var10004 = var0.sensorReqBuffer(false);
            if (!var10004.isDone()) {
               var14 = var10004;
               var8 = var10003;
               var7 = null;
               var13 = "sensor";
               return var14.exceptionally(Function.identity()).thenCompose(Yeezy::async$handlePOW);
            }

            var10000 = var0.execute("sensor", (Function)null, var10003, var10004.join());
            if (!var10000.isDone()) {
               var12 = var10000;
               return var12.exceptionally(Function.identity()).thenCompose(Yeezy::async$handlePOW);
            }

            var10000.join();
            var10000 = VertxUtil.hardCodedSleep(2000L);
         } while(var10000.isDone());

         var12 = var10000;
         return var12.exceptionally(Function.identity()).thenCompose(Yeezy::async$handlePOW);
      }

      var10000.join();
      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture preloadSensor() {
      if (this.isPreload) {
         do {
            CompletableFuture var10000 = this.sendPreloadSensor(this.api.postSensor(true));
            if (!var10000.isDone()) {
               CompletableFuture var1 = var10000;
               return var1.exceptionally(Function.identity()).thenCompose(Yeezy::async$preloadSensor);
            }

            var10000.join();
            this.smartSleep(1000L);
         } while(this.api.getCookies().getCookieValue("_abck").contains("||"));
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$sendPreloadSensor(Yeezy var0, HttpRequest var1, CompletableFuture var2, Buffer var3, HttpResponse var4, Exception var5, int var6, Object var7) {
      CompletableFuture var10000;
      label79: {
         CompletableFuture var17;
         Exception var18;
         label88: {
            boolean var10001;
            label83: {
               Buffer var14;
               label74: {
                  switch (var6) {
                     case 0:
                        try {
                           var10000 = var0.preloadBuffer();
                           if (!var10000.isDone()) {
                              var17 = var10000;
                              return var17.exceptionally(Function.identity()).thenCompose(Yeezy::async$sendPreloadSensor);
                           }
                           break;
                        } catch (Exception var12) {
                           var18 = var12;
                           var10001 = false;
                           break label88;
                        }
                     case 1:
                        var10000 = var2;
                        break;
                     case 2:
                        var10000 = var2;
                        var14 = var3;
                        break label74;
                     case 3:
                        var10000 = var2;
                        break label83;
                     case 4:
                        var10000 = var2;
                        break label79;
                     default:
                        throw new IllegalArgumentException();
                  }

                  try {
                     var14 = (Buffer)var10000.join();
                     var10000 = Request.send(var1, var14);
                     if (!var10000.isDone()) {
                        var17 = var10000;
                        return var17.exceptionally(Function.identity()).thenCompose(Yeezy::async$sendPreloadSensor);
                     }
                  } catch (Exception var11) {
                     var18 = var11;
                     var10001 = false;
                     break label88;
                  }
               }

               HttpResponse var16;
               try {
                  var16 = (HttpResponse)var10000.join();
                  if (var16 != null && var16.statusCode() != 201) {
                     var0.logger.warn("Failed to send sensor: status={}", var16.statusCode());
                     return CompletableFuture.completedFuture((Object)null);
                  }
               } catch (Exception var10) {
                  var18 = var10;
                  var10001 = false;
                  break label88;
               }

               try {
                  if (var16 != null) {
                     return CompletableFuture.completedFuture(var16.bodyAsString());
                  }
               } catch (Exception var13) {
                  var18 = var13;
                  var10001 = false;
                  break label88;
               }

               try {
                  var10000 = VertxUtil.randomSignalSleep(var0.instanceSignal, (long)var0.task.getRetryDelay());
                  if (!var10000.isDone()) {
                     var17 = var10000;
                     return var17.exceptionally(Function.identity()).thenCompose(Yeezy::async$sendPreloadSensor);
                  }
               } catch (Exception var9) {
                  var18 = var9;
                  var10001 = false;
                  break label88;
               }
            }

            try {
               var10000.join();
               return CompletableFuture.completedFuture((Object)null);
            } catch (Exception var8) {
               var18 = var8;
               var10001 = false;
            }
         }

         Exception var15 = var18;
         var0.logger.error("Error occurred on sending sensor: '{}'", var15.getMessage());
         var10000 = VertxUtil.randomSleep(3000L);
         if (!var10000.isDone()) {
            var17 = var10000;
            return var17.exceptionally(Function.identity()).thenCompose(Yeezy::async$sendPreloadSensor);
         }
      }

      var10000.join();
      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$getHomePage(Yeezy param0, HttpRequest param1, int param2, CompletableFuture param3, HttpResponse param4, String param5, int param6, int param7, Throwable param8, int param9, Object param10) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture postSensorsTilNoChallenge() {
      String var1 = "";
      CompletableFuture var10000 = this.sendSensor(this.api.postSensor(true), true);
      CompletableFuture var2;
      if (!var10000.isDone()) {
         var2 = var10000;
         return var2.exceptionally(Function.identity()).thenCompose(Yeezy::async$postSensorsTilNoChallenge);
      } else {
         var10000.join();
         var10000 = VertxUtil.randomSleep(1000L);
         if (!var10000.isDone()) {
            var2 = var10000;
            return var2.exceptionally(Function.identity()).thenCompose(Yeezy::async$postSensorsTilNoChallenge);
         } else {
            var10000.join();
            var10000 = this.sendSensor(this.api.postSensor(true), true);
            if (!var10000.isDone()) {
               var2 = var10000;
               return var2.exceptionally(Function.identity()).thenCompose(Yeezy::async$postSensorsTilNoChallenge);
            } else {
               var10000.join();

               while(true) {
                  label48: {
                     if (this.api.getCookies().getCookieValue("_abck").contains("||")) {
                        var10000 = this.smartSleep(1000L);
                        if (!var10000.isDone()) {
                           var2 = var10000;
                           return var2.exceptionally(Function.identity()).thenCompose(Yeezy::async$postSensorsTilNoChallenge);
                        }

                        var10000.join();
                        var10000 = this.sendSensor(this.api.postSensor(true), true);
                        if (!var10000.isDone()) {
                           var2 = var10000;
                           return var2.exceptionally(Function.identity()).thenCompose(Yeezy::async$postSensorsTilNoChallenge);
                        }

                        var1 = (String)var10000.join();
                        if (this.logger.isDebugEnabled()) {
                           this.logger.debug(var1);
                           break label48;
                        }

                        if (var1 != null && var1.contains("false")) {
                           this.logger.info("YS site down!");
                        } else {
                           if (this.api.getCookies().getCookieValue("_abck").contains("||")) {
                              this.logger.info("Handling sensor step - l={}", this.api.getCookies().getCookieValue("_abck").length());
                              break label48;
                           }

                           this.logger.info("Passed challenge l={}", this.api.getCookies().getCookieValue("_abck").length());
                        }
                     }

                     return CompletableFuture.completedFuture((Object)null);
                  }

                  ++this.sensorPosts;
               }
            }
         }
      }
   }

   public static String decode(String var0) {
      String var1 = "fHTDGRSHTRGSEeyew5uryettuytSEJUYRTSETHTGRFE";
      StringBuilder var2 = new StringBuilder();

      for(int var3 = 0; var3 < var0.length(); ++var3) {
         var2.append((char)(var0.charAt(var3) ^ var1.charAt(var3 % var1.length())));
      }

      return var2.toString();
   }

   public static CompletableFuture async$processPayment(Yeezy param0, int param1, HttpRequest param2, String param3, CompletableFuture param4, String param5, HttpResponse param6, String param7, JsonObject param8, Window3DS2 param9, int param10, Exception param11, int param12, Object param13) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$sensorReqBuffer(Yeezy var0, int var1, String var2, String var3, CompletableFuture var4, int var5, Object var6) {
      CompletableFuture var10000;
      Buffer var9;
      String var11;
      label64: {
         label65: {
            switch (var5) {
               case 0:
                  String var10;
                  CompletableFuture var12;
                  if (!var0.task.getMode().contains("2")) {
                     if (var0.task.getMode().contains("3")) {
                        try {
                           var3 = var0.api.getCookies().getCookieValue("_abck");
                           var10 = var0.api.getCookies().getCookieValue("bm_sz");
                        } catch (Throwable var7) {
                           var0.logger.error("Failed to set value for sensor api: {}", var7.getMessage());
                           var3 = "";
                           var10 = "";
                        }

                        var10000 = ((GaneshAPI)var0.api.getPixelAPI()).getSensorPayload(var3, var10, var0.task.getKeywords()[0].toUpperCase(), (boolean)var1);
                        if (!var10000.isDone()) {
                           var12 = var10000;
                           return var12.exceptionally(Function.identity()).thenCompose(Yeezy::async$sensorReqBuffer);
                        }
                        break label64;
                     }

                     var10000 = var0.api.getTrickleSensor();
                     if (!var10000.isDone()) {
                        var12 = var10000;
                        return var12.exceptionally(Function.identity()).thenCompose(Yeezy::async$sensorReqBuffer);
                     }
                     break label65;
                  }

                  try {
                     var3 = var0.api.getCookies().getCookieValue("_abck");
                     var10 = var0.api.getCookies().getCookieValue("bm_sz");
                  } catch (Throwable var8) {
                     var0.logger.error("Failed to set value for sensor api: {}", var8.getMessage());
                     var3 = "";
                     var10 = "";
                  }

                  var10000 = ((HawkAPI)var0.api.getPixelAPI()).getSensorPayload(var3, var10, var0.task.getKeywords()[0].toUpperCase(), (boolean)var1);
                  if (!var10000.isDone()) {
                     var12 = var10000;
                     return var12.exceptionally(Function.identity()).thenCompose(Yeezy::async$sensorReqBuffer);
                  }
                  break;
               case 1:
                  var10000 = var4;
                  break;
               case 2:
                  var10000 = var4;
                  break label64;
               case 3:
                  var10000 = var4;
                  break label65;
               default:
                  throw new IllegalArgumentException();
            }

            var11 = (String)var10000.join();
            var9 = (new JsonObject()).put("sensor_data", var11).toBuffer();
            return CompletableFuture.completedFuture(var9);
         }

         var3 = (String)var10000.join();
         var9 = (new JsonObject(var3)).toBuffer();
         return CompletableFuture.completedFuture(var9);
      }

      var11 = (String)var10000.join();
      var9 = (new JsonObject()).put("sensor_data", var11).toBuffer();
      return CompletableFuture.completedFuture(var9);
   }

   public CompletableFuture handlePOW(String var1) {
      Function var2 = Yeezy::lambda$handlePOW$0;
      String var10000 = Utils.quickParseFirst(var1, POW_PAGE_PATTERN);
      String var3 = "https://www.yeezysupply.com" + var10000;
      CompletableFuture var10 = this.execute("POW page", var2, this::lambda$handlePOW$1, (Object)null);
      CompletableFuture var5;
      if (!var10.isDone()) {
         var5 = var10;
         return var5.exceptionally(Function.identity()).thenCompose(Yeezy::async$handlePOW);
      } else {
         var10.join();
         String var4 = this.api.getCookies().getCookieValue("sec_cpt");

         do {
            Supplier var10003 = this::lambda$handlePOW$2;
            CompletableFuture var10004 = this.sensorReqBuffer(false);
            if (!var10004.isDone()) {
               CompletableFuture var9 = var10004;
               Supplier var8 = var10003;
               Object var7 = null;
               String var6 = "sensor";
               return var9.exceptionally(Function.identity()).thenCompose(Yeezy::async$handlePOW);
            }

            var10 = super.execute("sensor", (Function)null, var10003, var10004.join());
            if (!var10.isDone()) {
               var5 = var10;
               return var5.exceptionally(Function.identity()).thenCompose(Yeezy::async$handlePOW);
            }

            var10.join();
            var10 = VertxUtil.hardCodedSleep(2000L);
            if (!var10.isDone()) {
               var5 = var10;
               return var5.exceptionally(Function.identity()).thenCompose(Yeezy::async$handlePOW);
            }

            var10.join();
         } while(this.api.getCookies().getCookieValue("sec_cpt") != null && this.api.getCookies().getCookieValue("sec_cpt").equals(var4));

         YeezyAPI var11 = this.api;
         Objects.requireNonNull(var11);
         var10 = this.execute("POW completion", var2, var11::verifyPage, (Object)null);
         if (!var10.isDone()) {
            var5 = var10;
            return var5.exceptionally(Function.identity()).thenCompose(Yeezy::async$handlePOW);
         } else {
            var10.join();
            return CompletableFuture.completedFuture((Object)null);
         }
      }
   }

   public HttpRequest lambda$handlePOW$1(String var1) {
      return this.api.powPage(var1);
   }

   public Profile lambda$submitShippingAndBilling$7() {
      this.rotatedProfile = false;
      return this.task.getProfile();
   }

   public CompletableFuture getHomePage() {
      HttpRequest var1 = this.api.homePage();
      this.logger.info("Visiting homepage");
      this.netLogInfo("Visiting homepage");
      int var2 = 0;

      while(super.running) {
         CompletableFuture var10000;
         String var4;
         CompletableFuture var7;
         try {
            this.api.getCookies().put("RT", RT.getRT(), ".yeezysupply.com");
            var10000 = Request.send(var1);
            if (!var10000.isDone()) {
               var7 = var10000;
               return var7.exceptionally(Function.identity()).thenCompose(Yeezy::async$getHomePage);
            }

            HttpResponse var3 = (HttpResponse)var10000.join();
            if (var3 == null) {
               this.logger.warn("Failed to visit homepage: state=NO_REPLY");
            } else {
               var4 = (String)var3.body();
               byte var5 = var4.contains("HTTP 403 - Forbidden");
               int var6 = !var4.contains("text/javascript") && !var4.contains(".js\">") ? 0 : 1;
               if (var5 != 0 && var6 == 0) {
                  var10000 = VertxUtil.sleep((long)this.task.getMonitorDelay());
                  if (!var10000.isDone()) {
                     var7 = var10000;
                     return var7.exceptionally(Function.identity()).thenCompose(Yeezy::async$getHomePage);
                  }

                  var10000.join();
                  if (var2++ > 3) {
                     this.logger.warn("Failed to visit homepage: state=PROXY_BAN -> RESTARTING TASK");
                     return CompletableFuture.failedFuture(new Exception("403 ERROR"));
                  }

                  this.logger.warn("Failed to visit homepage: state=PROXY_BAN");
                  var10000 = this.api.init();
                  if (!var10000.isDone()) {
                     var7 = var10000;
                     return var7.exceptionally(Function.identity()).thenCompose(Yeezy::async$getHomePage);
                  }

                  var10000.join();
                  this.api.getCookies().clear();
               } else if (var3.statusCode() == 200) {
                  this.logger.info("Visited home page: state=OK");
                  return CompletableFuture.completedFuture(var4);
               }
            }
         } catch (Throwable var8) {
            var4 = var8.getMessage();
            if (var4.contains("Proxy Authentication Required") || var4.contains("HttpProxyConnectException")) {
               return CompletableFuture.failedFuture(new Exception("Wrong proxy credentials. Rotating..."));
            }

            this.logger.warn("Failed to visit homepage: {}", var4);
            this.netLogError("Failed to visit homepage: message='" + var4 + "'");
            var10000 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
            if (!var10000.isDone()) {
               var7 = var10000;
               return var7.exceptionally(Function.identity()).thenCompose(Yeezy::async$getHomePage);
            }

            var10000.join();
         }
      }

      return CompletableFuture.completedFuture("");
   }

   public CompletableFuture getProductPage(boolean var1) {
      HttpRequest var2 = this.api.productPage((boolean)var1).timeout(30000L);
      this.logger.info("Visiting product page.");
      this.netLogInfo("Visiting product page.");
      int var3 = 0;

      while(super.running) {
         CompletableFuture var8;
         CompletableFuture var10000;
         try {
            var10000 = Request.send(var2);
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(Yeezy::async$getProductPage);
            }

            HttpResponse var4 = (HttpResponse)var10000.join();
            if (var4 != null) {
               CookieJar var10 = this.api.getCookies();
               int var10004 = this.signUpSaveCount + 1;
               var10.put("UserSignUpAndSave", "" + (this.signUpSaveCount = var10004), ".yeezysupply.com");
               if (var1 != 0 && !this.isSent) {
                  this.isSent = true;
                  var10000 = this.logRawPage("after-splash-product-page", Buffer.buffer((String)var4.body()));
                  if (!var10000.isDone()) {
                     var8 = var10000;
                     return var8.exceptionally(Function.identity()).thenCompose(Yeezy::async$getProductPage);
                  }

                  var10000.join();
               }

               String var5 = (String)var4.body();
               byte var6 = var5.contains("HTTP 403 - Forbidden");
               int var7 = !var5.contains("text/javascript") && !var5.contains(".js\">") ? 0 : 1;
               if (var4.statusCode() >= 500) {
                  ++var3;
                  if (var3 >= 2) {
                     this.logger.info("Product page dead. Continuing...");
                     return CompletableFuture.completedFuture((Object)null);
                  }
               }

               if (var4.statusCode() < 500 && var6 != 0 && var7 == 0) {
                  this.logger.warn("Failed to visit product page: state=PROXY_BAN");
                  var10000 = VertxUtil.sleep((long)this.task.getMonitorDelay());
                  if (!var10000.isDone()) {
                     var8 = var10000;
                     return var8.exceptionally(Function.identity()).thenCompose(Yeezy::async$getProductPage);
                  }

                  var10000.join();
                  return CompletableFuture.failedFuture(new Exception("403 PRODPAGE ERROR"));
               }

               if (var4.statusCode() == 200) {
                  if (var1 != 0 && var5.contains("wrgen_orig_assets/")) {
                     this.logger.warn("Fake pass WR_ORIGIN");
                  }

                  if (var1 != 0 || var5.contains("wrgen_orig_assets/") || this.task.getMode().contains("noqueue") || Engine.get().getClientConfiguration().getBoolean("forceQueue", false)) {
                     return CompletableFuture.completedFuture(var5);
                  }

                  this.logger.warn("Waiting for waiting room to be live site=product_page");
                  var10000 = VertxUtil.randomSleep((long)this.task.getMonitorDelay());
                  if (!var10000.isDone()) {
                     var8 = var10000;
                     return var8.exceptionally(Function.identity()).thenCompose(Yeezy::async$getProductPage);
                  }

                  var10000.join();
               } else {
                  this.logger.error("Product page error visiting {}", var4.statusCode());
                  var10000 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
                  if (!var10000.isDone()) {
                     var8 = var10000;
                     return var8.exceptionally(Function.identity()).thenCompose(Yeezy::async$getProductPage);
                  }

                  var10000.join();
               }
            } else {
               this.logger.warn("Failed to visit product page: state=NO_REPLY");
               var10000 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
               if (!var10000.isDone()) {
                  var8 = var10000;
                  return var8.exceptionally(Function.identity()).thenCompose(Yeezy::async$getProductPage);
               }

               var10000.join();
            }
         } catch (Exception var9) {
            this.logger.warn("Failed to visit product page: {}", var9.getMessage());
            this.netLogError("Failed to visit product page: message='" + var9.getMessage() + "'");
            var10000 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(Yeezy::async$getProductPage);
            }

            var10000.join();
            if (var9.getMessage().toLowerCase(Locale.ROOT).contains("timeout")) {
               this.logger.info("Product page not responding. Continuing...");
               return CompletableFuture.completedFuture((Object)null);
            }
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture fillSizes() {
      CompletableFuture var10000 = this.getSizeJson(!this.availableSizes.isEmpty());
      if (!var10000.isDone()) {
         CompletableFuture var4 = var10000;
         return var4.exceptionally(Function.identity()).thenCompose(Yeezy::async$fillSizes);
      } else {
         String var1 = (String)var10000.join();

         try {
            JsonArray var2 = (new JsonObject(var1)).getJsonArray("variation_list");
            List var3 = (List)var2.stream().sorted(Yeezy::lambda$fillSizes$5).map(Yeezy::lambda$fillSizes$6).collect(Collectors.toList());
            if (!var3.isEmpty()) {
               this.availableSizes.clear();
               this.availableSizes.addAll(var3);
            }
         } catch (Throwable var5) {
            this.logger.error("Could not order sizes (Normal. Don't panic): {}", var5.getMessage());
         }

         return CompletableFuture.completedFuture((Object)null);
      }
   }

   public static CompletableFuture async$cart(Yeezy param0, int param1, CompletableFuture param2, Buffer param3, HttpRequest param4, HttpResponse param5, String param6, int param7, Object param8) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture preloadBuffer() {
      String var1;
      String var2;
      try {
         var1 = this.api.getCookies().getCookieValue("_abck");
         var2 = this.api.getCookies().getCookieValue("bm_sz");
      } catch (Throwable var5) {
         this.logger.error("Failed to set value for sensor api: {}", var5.getMessage());
         var1 = "";
         var2 = "";
      }

      if (this.tempHawk == null) {
         this.tempHawk = new HawkAPI();
      }

      this.tempHawk.setUseragent(this.api.userAgent);
      CompletableFuture var10000 = this.tempHawk.getSensorPayload(var1, var2, this.task.getKeywords()[0].toUpperCase(), true);
      if (!var10000.isDone()) {
         CompletableFuture var4 = var10000;
         return var4.exceptionally(Function.identity()).thenCompose(Yeezy::async$preloadBuffer);
      } else {
         String var3 = (String)var10000.join();
         return CompletableFuture.completedFuture((new JsonObject()).put("sensor_data", var3).toBuffer());
      }
   }

   public static CompletableFuture async$preloadSensor(Yeezy var0, CompletableFuture var1, int var2, Object var3) {
      switch (var2) {
         case 0:
            if (!var0.isPreload) {
               return CompletableFuture.completedFuture((Object)null);
            }
            break;
         case 1:
            var1.join();
            var0.smartSleep(1000L);
            if (!var0.api.getCookies().getCookieValue("_abck").contains("||")) {
               return CompletableFuture.completedFuture((Object)null);
            }
            break;
         default:
            throw new IllegalArgumentException();
      }

      do {
         CompletableFuture var10000 = var0.sendPreloadSensor(var0.api.postSensor(true));
         if (!var10000.isDone()) {
            var1 = var10000;
            return var1.exceptionally(Function.identity()).thenCompose(Yeezy::async$preloadSensor);
         }

         var10000.join();
         var0.smartSleep(1000L);
      } while(var0.api.getCookies().getCookieValue("_abck").contains("||"));

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$run(Yeezy param0, CompletableFuture param1, int param2, String param3, int param4, Object param5) {
      // $FF: Couldn't be decompiled
   }

   public void lambda$requestCaptcha$4(ContextCompletableFuture var1, AsyncResult var2) {
      if (var2.succeeded()) {
         this.tokenRef.set((SharedCaptchaToken)((Message)var2.result()).body());
         var1.complete((Object)null);
      }

   }

   public static CompletableFuture async$sendSensor(Yeezy var0, HttpRequest var1, int var2, CompletableFuture var3, Buffer var4, HttpResponse var5, Exception var6, int var7, Object var8) {
      CompletableFuture var10000;
      label79: {
         CompletableFuture var18;
         Exception var19;
         label78: {
            boolean var10001;
            label84: {
               Buffer var15;
               label76: {
                  switch (var7) {
                     case 0:
                        if (Engine.get().getClientConfiguration().getBoolean("noProtect", false) || var0.isNoProtection) {
                           return CompletableFuture.completedFuture("");
                        }

                        try {
                           var10000 = var0.sensorReqBuffer((boolean)var2);
                           if (!var10000.isDone()) {
                              var18 = var10000;
                              return var18.exceptionally(Function.identity()).thenCompose(Yeezy::async$sendSensor);
                           }
                           break;
                        } catch (Exception var13) {
                           var19 = var13;
                           var10001 = false;
                           break label78;
                        }
                     case 1:
                        var10000 = var3;
                        break;
                     case 2:
                        var10000 = var3;
                        var15 = var4;
                        break label76;
                     case 3:
                        var10000 = var3;
                        break label84;
                     case 4:
                        var10000 = var3;
                        break label79;
                     default:
                        throw new IllegalArgumentException();
                  }

                  try {
                     var15 = (Buffer)var10000.join();
                     var10000 = Request.send(var1, var15);
                     if (!var10000.isDone()) {
                        var18 = var10000;
                        return var18.exceptionally(Function.identity()).thenCompose(Yeezy::async$sendSensor);
                     }
                  } catch (Exception var12) {
                     var19 = var12;
                     var10001 = false;
                     break label78;
                  }
               }

               HttpResponse var17;
               try {
                  var17 = (HttpResponse)var10000.join();
                  if (var17 != null && var17.statusCode() != 201) {
                     var0.logger.warn("Failed to send sensor: status={}", var17.statusCode());
                     return CompletableFuture.completedFuture((Object)null);
                  }
               } catch (Exception var11) {
                  var19 = var11;
                  var10001 = false;
                  break label78;
               }

               try {
                  if (var17 != null) {
                     return CompletableFuture.completedFuture(var17.bodyAsString());
                  }
               } catch (Exception var14) {
                  var19 = var14;
                  var10001 = false;
                  break label78;
               }

               try {
                  var10000 = VertxUtil.randomSignalSleep(var0.instanceSignal, (long)var0.task.getRetryDelay());
                  if (!var10000.isDone()) {
                     var18 = var10000;
                     return var18.exceptionally(Function.identity()).thenCompose(Yeezy::async$sendSensor);
                  }
               } catch (Exception var10) {
                  var19 = var10;
                  var10001 = false;
                  break label78;
               }
            }

            try {
               var10000.join();
               return CompletableFuture.completedFuture((Object)null);
            } catch (Exception var9) {
               var19 = var9;
               var10001 = false;
            }
         }

         Exception var16 = var19;
         var0.logger.error("Error occurred on sending sensor: '{}'", var16.getMessage());
         var10000 = VertxUtil.randomSleep(3000L);
         if (!var10000.isDone()) {
            var18 = var10000;
            return var18.exceptionally(Function.identity()).thenCompose(Yeezy::async$sendSensor);
         }
      }

      var10000.join();
      return CompletableFuture.completedFuture((Object)null);
   }

   public static String lambda$processPayment$8() {
      return "Unknown payment error";
   }

   public void handleFailureWebhooks(String var1, String var2) {
      if (this.previousResponseHash == 0 || this.previousResponseLen != var2.length() || this.previousResponseHash != var2.hashCode()) {
         try {
            Analytics.failure(var1, this.task, new JsonObject(var2), this.api.proxyStringSafe());
            this.previousResponseHash = var2.hashCode();
            this.previousResponseLen = var2.length();
         } catch (Throwable var4) {
         }
      }

   }

   public CompletableFuture getSizeJson(boolean var1) {
      HttpRequest var2 = this.api.generalProdAPI();
      this.logger.info("Fetching sizes");
      int var3 = 0;

      while(var3 < (this.isBannedOnShipping ? 1 : ThreadLocalRandom.current().nextInt(4, 10))) {
         ++var3;

         try {
            CompletableFuture var10000 = Request.send(var2);
            CompletableFuture var7;
            if (!var10000.isDone()) {
               var7 = var10000;
               return var7.exceptionally(Function.identity()).thenCompose(Yeezy::async$getSizeJson);
            }

            HttpResponse var4 = (HttpResponse)var10000.join();
            if (var4 != null) {
               String var5 = (String)var4.body();
               if (var1 != 0 || this.api.getSKU().equals("BY9611")) {
                  return CompletableFuture.completedFuture((Object)null);
               }

               if (var5.contains("HTTP 403 - Forbidden")) {
                  this.logger.warn("Failed to fetch sizes: state=BAN");
               } else {
                  if (var4.statusCode() == 200) {
                     this.logger.info("Fetched sizes: state=OK");
                     VertxUtil.sendSignal("sizes" + this.api.getSKU(), var5);
                     return CompletableFuture.completedFuture(var5);
                  }

                  this.logger.warn("Failed to fetch sizes: state={}", (var4.statusCode() + var4.statusMessage()).replaceAll(" ", "_"));
               }

               var10000 = VertxUtil.signalSleep("sizes" + this.api.getSKU(), 1000L);
               if (!var10000.isDone()) {
                  var7 = var10000;
                  return var7.exceptionally(Function.identity()).thenCompose(Yeezy::async$getSizeJson);
               }

               Object var6 = var10000.join();
               if (var6 != null) {
                  return CompletableFuture.completedFuture((String)var6);
               }
            } else {
               this.logger.warn("Failed to fetch sizes: state=NO_REPLY");
               this.netLogWarn("Failed to fetch sizes: state=NO_REPLY");
            }
         } catch (Exception var8) {
            this.logger.error("Could not view sizes. Retrying... {}", var8.getMessage());
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$sendBasic(Yeezy param0, HttpRequest param1, String param2, int param3, CompletableFuture param4, HttpResponse param5, Throwable param6, int param7, Object param8) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture requestCaptcha() {
      if (this.harvesterID == null) {
         this.harvesterID = this.harvesters.allocate();
         this.logger.info("Got harvester id={}", this.harvesterID);
      } else {
         this.harvesters.shouldSwap(this.harvesterID).ifPresent(this::lambda$requestCaptcha$3);
      }

      this.logger.info("Requesting captcha token from harvester={}", this.harvesterID);
      ContextCompletableFuture var1 = new ContextCompletableFuture();
      super.vertx.eventBus().request(this.harvesterID, "https://www.yeezysupply.com/products/" + this.api.getSKU(), this::lambda$requestCaptcha$4);
      return var1;
   }

   public CompletableFuture handleAfter3DS(Window3DS2 var1) {
      int var2 = 0;

      while(this.running && var2 <= 10) {
         CompletableFuture var10000;
         CompletableFuture var7;
         try {
            JsonObject var3 = new JsonObject();
            var3.put("orderId", this.orderID);
            HashMap var9 = var1.getUploadValues();
            Objects.requireNonNull(var3);
            var9.forEach(var3::put);
            HttpRequest var4 = this.api.handleAfter3DS(var1.getEncodedData(), this.authorization, var1.getTermURI());
            var10000 = Request.send(var4, var3.toBuffer());
            if (!var10000.isDone()) {
               var7 = var10000;
               return var7.exceptionally(Function.identity()).thenCompose(Yeezy::async$handleAfter3DS);
            }

            HttpResponse var5 = (HttpResponse)var10000.join();
            if (var5 != null) {
               this.logger.info("After 3DS status={} : r={}", var5.statusCode(), var5.bodyAsString());
               String var6 = var5.bodyAsString();
               if (var6 != null) {
                  if (var6.contains("orderToken")) {
                     this.logger.info("Successfully checked out!");
                     Analytics.success(this.task, new JsonObject(var6), this.api.proxyStringSafe());
                     Analytics.log("Successfully Checked Out", this.task, this.api.getCookies().getCookieValueLength("_abck"), this.sensorPosts, this.visitedBaskets, this.api.userAgent);
                     return CompletableFuture.completedFuture(true);
                  }

                  if (var6.contains("basket_not_found_exception") || var6.contains("{\"invalidFields\":[\"Product items\"]")) {
                     if (var6.contains("basket_not_found_exception")) {
                        this.logger.warn("Failed to process payment: state='{}' - status={}", "Cart expiry", var5.statusCode());
                        this.netLogWarn("Failed to process payment: state='Cart expiry' status=" + var5.statusCode());
                     } else {
                        this.logger.warn("Failed to process payment: state='{}' - status={}", "Cart jacked", var5.statusCode());
                        this.netLogWarn("Failed to process payment: state='Cart Jacked' status=" + var5.statusCode());
                     }

                     var10000 = this.prepareCart();
                     if (!var10000.isDone()) {
                        var7 = var10000;
                        return var7.exceptionally(Function.identity()).thenCompose(Yeezy::async$handleAfter3DS);
                     }

                     var10000.join();
                     var10000 = this.cart();
                     if (!var10000.isDone()) {
                        var7 = var10000;
                        return var7.exceptionally(Function.identity()).thenCompose(Yeezy::async$handleAfter3DS);
                     }

                     var10000.join();
                     this.logger.info("Recovered cart jack (3DS)");
                     this.netLogInfo("Recovered cart jack (3DS)");
                     Analytics.log("Recovered cart jack (mid 3ds)", this.task, this.api.getCookies().getCookieValueLength("_abck"), this.sensorPosts, this.visitedBaskets, this.api.userAgent);
                     var10000 = this.checkout();
                     if (!var10000.isDone()) {
                        var7 = var10000;
                        return var7.exceptionally(Function.identity()).thenCompose(Yeezy::async$handleAfter3DS);
                     }

                     var10000.join();
                     return CompletableFuture.completedFuture((Object)null);
                  }

                  if (var6.contains("HTTP 403 - Forbidden")) {
                     this.logger.warn("Failed to process payment: state='{}' - status={}", "Blocked by 403 body", var5.statusCode());
                     this.netLogWarn("Failed to process payment: state='Blocked by 403 body' status=" + var5.statusCode());
                     var10000 = this.postSensors();
                     if (!var10000.isDone()) {
                        var7 = var10000;
                        return var7.exceptionally(Function.identity()).thenCompose(Yeezy::async$handleAfter3DS);
                     }

                     var10000.join();
                     ++var2;
                  } else {
                     if (var6.length() == 0) {
                        this.logger.warn("Failed to process payment: state='{}' - status={}", "Blocked by FW", var5.statusCode());
                        this.netLogWarn("Failed to process payment: state='Blocked by FW' status=" + var5.statusCode());
                        return CompletableFuture.completedFuture(false);
                     }

                     if (var5.statusCode() == 403) {
                        this.logger.warn("Failed to process payment: state='{}' - status={}", "Bad cookie", var5.statusCode());
                        this.netLogWarn("Failed to process payment: state='Bad cookie' status=" + var5.statusCode());
                        var10000 = this.postSensors();
                        if (!var10000.isDone()) {
                           var7 = var10000;
                           return var7.exceptionally(Function.identity()).thenCompose(Yeezy::async$handleAfter3DS);
                        }

                        var10000.join();
                        ++var2;
                     } else {
                        if (var6.contains("confirm.error.paymentdeclined.fraud") || var6.contains("hook_status_exception")) {
                           this.handleFailureWebhooks("Fraud", var6);
                           this.logger.warn("Failed to process payment: state='{}' - status={}", "Fraud Decline", var5.statusCode());
                           this.netLogWarn("Failed to process payment: state='Fraud Decline' status=" + var5.statusCode());
                           if (this.smartSwapEnabled()) {
                              this.shouldRotate = true;
                           }

                           return CompletableFuture.completedFuture(false);
                        }

                        if (var6.contains("confirm.error.paymentdeclined.not_enough_balance")) {
                           this.handleFailureWebhooks("Card Decline (balance)", var6);
                           this.logger.warn("Failed to process payment: state='{}' - status={}", "Card Decline (balance)", var5.statusCode());
                           this.netLogWarn("Failed to process payment: state='Card Decline (balance)' status=" + var5.statusCode());
                           if (this.smartSwapEnabled()) {
                              this.shouldRotate = true;
                           }

                           return CompletableFuture.completedFuture(false);
                        }

                        if (var6.contains("paymentdeclined")) {
                           this.logger.warn("Failed to process payment: state='{}' - status={}", "Card Decline", var5.statusCode());
                           this.netLogWarn("Failed to process payment: state='Card Decline' status=" + var5.statusCode());
                           this.handleFailureWebhooks("Card Decline", var6);
                           return CompletableFuture.completedFuture(false);
                        }

                        if (var6.contains("missing properties")) {
                           this.logger.warn("Failed to process payment: state='{}' - status={}", "Invalid shipping or billing", var5.statusCode());
                           this.netLogWarn("Failed to process payment: state='Invalid shipping or billing' status=" + var5.statusCode());
                           this.handleFailureWebhooks("Invalid shipping or billing", var6);
                           return CompletableFuture.completedFuture(false);
                        }

                        if (var6.contains("Invalid payment verification request") && var6.contains("InvalidDataException")) {
                           this.logger.warn("Failed to process payment: state='{}' - status={}", "3DS Error", var6);
                           this.handleFailureWebhooks("3DS Auth Fail", var6);
                           return CompletableFuture.completedFuture(false);
                        }

                        if (!var6.contains("Product item not available") && !var6.contains("Basket has been removed")) {
                           this.logger.warn("Failed to process payment: state='{}' - status={}", "Unknown payment error", var6);
                           this.handleFailureWebhooks("Unknown", var6);
                           return CompletableFuture.completedFuture(false);
                        }
                     }
                  }

                  if (var5.statusCode() >= 500) {
                     this.logger.warn("Failed to process payment: state='{}' - status={}", "Site down", var5.statusCode());
                     this.netLogWarn("Failed to process payment: state='Site down' status=" + var5.statusCode());
                  } else {
                     if (var5.statusCode() != 400) {
                        this.handleFailureWebhooks("Out of stock", var6);
                        this.logger.warn("Failed to process payment: state='{}' - status={}", "Out of stock", var5.statusCode());
                        return CompletableFuture.completedFuture(false);
                     }

                     this.logger.warn("Failed processing 3ds cookie");
                     var10000 = this.postSensors();
                     if (!var10000.isDone()) {
                        var7 = var10000;
                        return var7.exceptionally(Function.identity()).thenCompose(Yeezy::async$handleAfter3DS);
                     }

                     var10000.join();
                     var10000 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
                     if (!var10000.isDone()) {
                        var7 = var10000;
                        return var7.exceptionally(Function.identity()).thenCompose(Yeezy::async$handleAfter3DS);
                     }

                     var10000.join();
                  }
               }
            }
         } catch (Throwable var8) {
            this.logger.error("Error occurred handling after confirmation: {}", var8.getMessage());
            var10000 = VertxUtil.randomSleep(3000L);
            if (!var10000.isDone()) {
               var7 = var10000;
               return var7.exceptionally(Function.identity()).thenCompose(Yeezy::async$handleAfter3DS);
            }

            var10000.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public void checkHmac() {
      boolean var1 = this.api.getCookies().contains(V3_COOKIE_NAME + "_u") && this.api.getCookies().asString().contains("hmac");
      if (!var1 && !this.task.getMode().contains("noqueue")) {
         throw new Throwable("HMAC expired.");
      }
   }

   public boolean isFirewallOn() {
      return Engine.get().getClientConfiguration().getBoolean("firewall", true) || RATIO_FW_SKIP_ELIGIBLE;
   }

   public static CompletableFuture async$smartSleep(Yeezy var0, long var1, CompletableFuture var3, int var4, Object var5) {
      CompletableFuture var10000;
      switch (var4) {
         case 0:
            var10000 = VertxUtil.randomSleep(var1);
            if (!var10000.isDone()) {
               var3 = var10000;
               return var3.exceptionally(Function.identity()).thenCompose(Yeezy::async$smartSleep);
            }
            break;
         case 1:
            var10000 = var3;
            break;
         default:
            throw new IllegalArgumentException();
      }

      var10000.join();
      return CompletableFuture.completedFuture((Object)null);
   }

   public HttpRequest lambda$handlePOW$2() {
      return this.api.postSensor(true);
   }

   public CompletableFuture processPayment() {
      int var1 = 0;

      while(var1 < 250) {
         HttpRequest var2 = this.api.processPayment(this.authorization);
         this.checkHmac();

         CompletableFuture var9;
         CompletableFuture var10000;
         try {
            this.api.updateUtagUrl("https://www.yeezysupply.com/payment");
            this.api.getCookies().put("utag_main", this.api.fetchUtag(), ".yeezysupply.com");
            String var3 = AdyenForm.getCSEToken(this.instanceProfile);
            if (var3 != null && !var3.isBlank()) {
               String var4 = "{\"basketId\":\"" + this.basketID + "\",\"encryptedInstrument\":\"" + var3 + "\",\"paymentInstrument\":{\"holder\":\"" + this.instanceProfile.getFirstName().substring(0, 1).toUpperCase() + this.instanceProfile.getFirstName().substring(1).toLowerCase() + " " + this.instanceProfile.getLastName().substring(0, 1).toUpperCase() + this.instanceProfile.getLastName().substring(1).toLowerCase() + "\",\"expirationMonth\":" + (this.instanceProfile.getExpiryMonth().charAt(0) == '0' ? this.instanceProfile.getExpiryMonth().substring(1) : this.instanceProfile.getExpiryMonth()) + ",\"expirationYear\":" + this.instanceProfile.getExpiryYear() + ",\"lastFour\":\"" + this.instanceProfile.getCardNumber().substring(this.instanceProfile.getCardNumber().length() - 4) + "\",\"paymentMethodId\":\"CREDIT_CARD\",\"cardType\":\"" + this.instanceProfile.getPaymentMethod().get().replace("MASTERCARD", "MASTER") + "\"},\"fingerprint\":\"" + AdyenFingerprints.get() + "\"}";
               var10000 = Request.send(var2, Buffer.buffer(var4));
               if (!var10000.isDone()) {
                  var9 = var10000;
                  return var9.exceptionally(Function.identity()).thenCompose(Yeezy::async$processPayment);
               }

               HttpResponse var5 = (HttpResponse)var10000.join();
               if (var5 != null) {
                  this.logger.info("Processing payment [{}]", var5.statusCode());
                  if (var5.body() != null) {
                     if (var5.statusCode() != 201) {
                        String var11 = ((String)var5.body()).replace("\n", "");
                        this.logger.info(var11);
                        if (var11.contains("HTTP 403 - Forbidden")) {
                           this.logger.warn("Failed to process payment: state='{}' - status={}", "Blocked by 403 body", var5.statusCode());
                           this.netLogWarn("Failed to process payment: state='Blocked by 403 body' status=" + var5.statusCode());
                        } else if (var11.length() == 0) {
                           this.logger.warn("Failed to process payment: state='{}' - status={}", "Blocked by FW", var5.statusCode());
                           this.netLogWarn("Failed to process payment: state='Blocked by FW' status=" + var5.statusCode());
                        } else if (var5.statusCode() == 403) {
                           this.logger.warn("Failed to process payment: state='{}' - status={}", "Bad cookie", var5.statusCode());
                           this.netLogWarn("Failed to process payment: state='Bad cookie' status=" + var5.statusCode());
                        } else if (!var11.contains("confirm.error.paymentdeclined.fraud") && !var11.contains("hook_status_exception")) {
                           if (var11.contains("basket_not_found_exception") || var11.contains("{\"invalidFields\":[\"Product items\"]")) {
                              if (var11.contains("basket_not_found_exception")) {
                                 this.logger.warn("Failed to process payment: state='{}' - status={}", "Cart expiry", var5.statusCode());
                                 this.netLogWarn("Failed to process payment: state='Cart expiry' status=" + var5.statusCode());
                              } else {
                                 this.logger.warn("Failed to process payment: state='{}' - status={}", "Cart jacked", var5.statusCode());
                                 this.netLogWarn("Failed to process payment: state='Cart Jacked' status=" + var5.statusCode());
                              }

                              if (this.task.getMode().contains("noqueuetesting")) {
                                 var10000 = VertxUtil.sleep(999999L);
                                 if (!var10000.isDone()) {
                                    var9 = var10000;
                                    return var9.exceptionally(Function.identity()).thenCompose(Yeezy::async$processPayment);
                                 }

                                 var10000.join();
                              }

                              var10000 = this.prepareCart();
                              if (!var10000.isDone()) {
                                 var9 = var10000;
                                 return var9.exceptionally(Function.identity()).thenCompose(Yeezy::async$processPayment);
                              }

                              var10000.join();
                              var10000 = this.cart();
                              if (!var10000.isDone()) {
                                 var9 = var10000;
                                 return var9.exceptionally(Function.identity()).thenCompose(Yeezy::async$processPayment);
                              }

                              var10000.join();
                              this.logger.info("Recovered cart jack");
                              this.netLogInfo("Recovered cart jack");
                              Analytics.log("Recovered cart jack", this.task, this.api.getCookies().getCookieValueLength("_abck"), this.sensorPosts, this.visitedBaskets, this.api.userAgent);
                              var10000 = this.checkout();
                              if (!var10000.isDone()) {
                                 var9 = var10000;
                                 return var9.exceptionally(Function.identity()).thenCompose(Yeezy::async$processPayment);
                              }

                              var10000.join();
                              return CompletableFuture.completedFuture((Object)null);
                           }

                           if (var11.contains("confirm.error.paymentdeclined.not_enough_balance")) {
                              this.handleFailureWebhooks("Card Decline (balance)", (String)var5.body());
                              this.logger.warn("Failed to process payment: state='{}' - status={}", "Card Decline (balance)", var5.statusCode());
                              this.netLogWarn("Failed to process payment: state='Card Decline (balance)' status=" + var5.statusCode());
                              if (this.smartSwapEnabled()) {
                                 this.shouldRotate = true;
                                 var10000 = this.checkout();
                                 if (!var10000.isDone()) {
                                    var9 = var10000;
                                    return var9.exceptionally(Function.identity()).thenCompose(Yeezy::async$processPayment);
                                 }

                                 var10000.join();
                                 return CompletableFuture.completedFuture((Object)null);
                              }

                              ++var1;
                           } else if (var11.contains("paymentdeclined")) {
                              this.logger.warn("Failed to process payment: state='{}' - status={}", "Card Decline", var5.statusCode());
                              this.netLogWarn("Failed to process payment: state='Card Decline' status=" + var5.statusCode());
                              this.handleFailureWebhooks("Card Decline", (String)var5.body());
                              ++var1;
                           } else if (var11.contains("missing properties")) {
                              this.logger.warn("Failed to process payment: state='{}' - status={}", "Invalid shipping or billing", var5.statusCode());
                              this.netLogWarn("Failed to process payment: state='Invalid shipping or billing' status=" + var5.statusCode());
                              this.handleFailureWebhooks("Invalid shipping or billing", (String)var5.body());
                              ++var1;
                           } else if (var11.contains("<H1>Invalid URL</H1>")) {
                              this.logger.warn("Failed to process payment: state='{}'", "Blocked by 400 (bad session)");
                              this.netLogWarn("Failed to process payment: state='Blocked by 400 (bad session)'");
                              this.handleFailureWebhooks("Bad session", (String)var5.body());
                           } else if (var5.statusCode() == 200 && var5.getHeader("authorization") == null) {
                              this.logger.warn("Fake processing (animal)");
                              this.netLogWarn("Fake processing (animal)");
                              Analytics.log("Fake processing (animal)", this.task, this.api.getCookies().getCookieValueLength("_abck"), this.sensorPosts, this.visitedBaskets, this.api.userAgent);
                           } else if (!var11.contains("Product item not available") && !var11.contains("Basket has been removed")) {
                              String var12 = Utils.parseSafe((String)var5.body(), "message", Yeezy::lambda$processPayment$8);
                              this.logger.warn("Failed to process payment: state='{}' - r={}", var12, var11);
                              this.netLogWarn("Failed to process payment: state='" + var12 + "' body='" + var11 + "'");
                              this.handleFailureWebhooks(var12, (String)var5.body());
                              ++var1;
                           } else if (var5.statusCode() >= 500) {
                              this.logger.warn("Failed to process payment: state='{}' - status={}", "Site down", var5.statusCode());
                              this.netLogWarn("Failed to process payment: state='Site down' status=" + var5.statusCode());
                           } else if (var11.contains("invalidFields\":[\"Product items\"")) {
                              this.handleFailureWebhooks("Out of stock", (String)var5.body());
                              this.logger.warn("Failed to process payment: state='{}' - status={}", "Out of stock", var5.statusCode());
                              this.netLogWarn("Failed to process payment: state='Out of stock' status=" + var5.statusCode());
                              ++var1;
                           } else if (var11.contains("<H1>Invalid URL</H1>")) {
                              this.logger.warn("Failed to process payment: state='{}' - status={}", "URL Block (cookies)", var5.statusCode());
                              this.netLogWarn("Failed to process payment: state='URL Block (cookies)' status=" + var5.statusCode());
                           } else {
                              this.logger.warn("Failed to process payment: state='{}' - status={}", "Unknown Error", var5.statusCode());
                              this.netLogWarn("Failed to process payment: state='Unknown Error' status=" + var5.statusCode());
                           }
                        } else {
                           this.handleFailureWebhooks("Fraud", (String)var5.body());
                           this.logger.warn("Failed to process payment: state='{}' - status={}", "Fraud Decline", var5.statusCode());
                           this.netLogWarn("Failed to process payment: state='Fraud Decline' status=" + var5.statusCode());
                           if (this.smartSwapEnabled()) {
                              this.shouldRotate = true;
                              var10000 = this.checkout();
                              if (!var10000.isDone()) {
                                 var9 = var10000;
                                 return var9.exceptionally(Function.identity()).thenCompose(Yeezy::async$processPayment);
                              }

                              var10000.join();
                              return CompletableFuture.completedFuture((Object)null);
                           }

                           ++var1;
                        }
                     } else {
                        JsonObject var6 = new JsonObject((String)var5.body());
                        if (var6.containsKey("orderToken") && !var6.getString("orderToken").isEmpty()) {
                           this.logger.info("Successfully checked out!: r={}", var5.body());
                           this.netLogInfo("Successfully checked out!: body='" + (String)var5.body() + "'");
                           Analytics.success(this.task, new JsonObject((String)var5.body()), this.api.proxyStringSafe());
                           Analytics.log("Successfully Checked Out", this.task, this.api.getCookies().getCookieValueLength("_abck"), this.sensorPosts, this.visitedBaskets, this.api.userAgent);
                           return CompletableFuture.completedFuture(var5.bodyAsString());
                        }

                        if (!var6.getString("paymentStatus").equalsIgnoreCase("not_paid") || !var6.getString("authorizationType").contains("3ds")) {
                           this.logger.info("Successfully checked out!");
                           this.netLogInfo("Successfully checked out!: body='" + (String)var5.body() + "'");
                           Analytics.success(this.task, new JsonObject((String)var5.body()), this.api.proxyStringSafe());
                           Analytics.log("Successfully Checked Out", this.task, this.api.getCookies().getCookieValueLength("_abck"), this.sensorPosts, this.visitedBaskets, this.api.userAgent);
                           return CompletableFuture.completedFuture(var5.bodyAsString());
                        }

                        var10000 = this.postSensorsTilNoChallenge();
                        if (!var10000.isDone()) {
                           var9 = var10000;
                           return var9.exceptionally(Function.identity()).thenCompose(Yeezy::async$processPayment);
                        }

                        var10000.join();
                        this.logger.info("Payment authentication required: r={}", var5.body());
                        this.netLogInfo("Payment authentication required: body='" + (String)var5.body() + "'");
                        var10000 = this.get3DS2Page(var6);
                        if (!var10000.isDone()) {
                           var9 = var10000;
                           return var9.exceptionally(Function.identity()).thenCompose(Yeezy::async$processPayment);
                        }

                        Window3DS2 var7 = (Window3DS2)var10000.join();
                        if (var7 != null) {
                           Analytics.warning("Starting 3DS Processing!", this.task);
                           this.logger.info("Awaiting confirmation...");
                           var10000 = var7.invoke();
                           if (!var10000.isDone()) {
                              var9 = var10000;
                              return var9.exceptionally(Function.identity()).thenCompose(Yeezy::async$processPayment);
                           }

                           var10000.join();
                           Analytics.warning("3DS Instance Closed. Proceeding?", this.task);
                           var10000 = this.handleAfter3DS(var7);
                           if (!var10000.isDone()) {
                              var9 = var10000;
                              return var9.exceptionally(Function.identity()).thenCompose(Yeezy::async$processPayment);
                           }

                           byte var8 = (Boolean)var10000.join();
                           if (var8 != 0) {
                              return CompletableFuture.completedFuture("");
                           }

                           if (this.shouldRotate && this.smartSwapEnabled()) {
                              var10000 = this.checkout();
                              if (!var10000.isDone()) {
                                 var9 = var10000;
                                 return var9.exceptionally(Function.identity()).thenCompose(Yeezy::async$processPayment);
                              }

                              var10000.join();
                              return CompletableFuture.completedFuture((Object)null);
                           }
                        }
                     }
                  }
               }

               var10000 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
               if (!var10000.isDone()) {
                  var9 = var10000;
                  return var9.exceptionally(Function.identity()).thenCompose(Yeezy::async$processPayment);
               }

               var10000.join();
               if (var5 != null && var5.statusCode() == 403) {
                  var10000 = this.prepareCart();
                  if (!var10000.isDone()) {
                     var9 = var10000;
                     return var9.exceptionally(Function.identity()).thenCompose(Yeezy::async$processPayment);
                  }

                  var10000.join();
               } else {
                  var10000 = this.postSensors();
                  if (!var10000.isDone()) {
                     var9 = var10000;
                     return var9.exceptionally(Function.identity()).thenCompose(Yeezy::async$processPayment);
                  }

                  var10000.join();
               }
            } else {
               this.logger.warn("Crypt error. Retrying...");
               var10000 = VertxUtil.sleep(300L);
               if (!var10000.isDone()) {
                  var9 = var10000;
                  return var9.exceptionally(Function.identity()).thenCompose(Yeezy::async$processPayment);
               }

               var10000.join();
            }
         } catch (Exception var10) {
            this.logger.error("Error occurred on processing payment: '{}'", var10.getMessage());
            this.netLogError("Error occurred on processing payment: message=" + var10.getMessage());
            var10000 = VertxUtil.randomSleep(3000L);
            if (!var10000.isDone()) {
               var9 = var10000;
               return var9.exceptionally(Function.identity()).thenCompose(Yeezy::async$processPayment);
            }

            var10000.join();
         }
      }

      return CompletableFuture.failedFuture(new Exception("Failed to process payment"));
   }

   public static CompletableFuture async$getAkamaiScript(Yeezy var0, HttpRequest var1, CompletableFuture var2, HttpResponse var3, int var4, Object var5) {
      CompletableFuture var10000;
      CompletableFuture var7;
      switch (var4) {
         case 0:
            var1 = var0.api.akamaiScript(false);
            var0.logger.info("Visiting FW");
            var0.netLogInfo("Visiting FW");
            var10000 = Request.send(var1);
            if (!var10000.isDone()) {
               var7 = var10000;
               return var7.exceptionally(Function.identity()).thenCompose(Yeezy::async$getAkamaiScript);
            }
            break;
         case 1:
            var10000 = var2;
            break;
         case 2:
            var2.join();
            var10000 = Request.send(var1);
            if (!var10000.isDone()) {
               var7 = var10000;
               return var7.exceptionally(Function.identity()).thenCompose(Yeezy::async$getAkamaiScript);
            }
            break;
         default:
            throw new IllegalArgumentException();
      }

      do {
         HttpResponse var6 = (HttpResponse)var10000.join();
         if (var6 != null) {
            if (((String)var6.body()).contains("HTTP 403 - Forbidden")) {
               var0.logger.warn("Failed to visit FW: state=PROXY_BAN");
               var0.netLogWarn("Failed to visit FW: state=PROXY_BAN");
               var10000 = VertxUtil.sleep((long)var0.task.getMonitorDelay());
               if (!var10000.isDone()) {
                  var7 = var10000;
                  return var7.exceptionally(Function.identity()).thenCompose(Yeezy::async$getAkamaiScript);
               }

               var10000.join();
            } else if (var6.statusCode() == 200) {
               var0.netLogWarn("Visited FW: state=OK");
               return CompletableFuture.completedFuture((Object)null);
            }
         } else {
            var0.logger.warn("Failed to visit FW: state=NO_REPLY");
            var0.netLogWarn("Failed to visit FW: state=NO_REPLY");
         }

         var10000 = Request.send(var1);
      } while(var10000.isDone());

      var7 = var10000;
      return var7.exceptionally(Function.identity()).thenCompose(Yeezy::async$getAkamaiScript);
   }

   public CompletableFuture sendBasic(HttpRequest var1, String var2) {
      int var3 = 0;

      while(super.running && var3 <= 2) {
         ++var3;

         CompletableFuture var10000;
         CompletableFuture var5;
         try {
            var10000 = Request.send(var1);
            if (!var10000.isDone()) {
               var5 = var10000;
               return var5.exceptionally(Function.identity()).thenCompose(Yeezy::async$sendBasic);
            }

            HttpResponse var4 = (HttpResponse)var10000.join();
            if (var4 != null && var4.statusCode() < 300 & !((String)var4.body()).contains("HTTP 403 - Forbidden")) {
               this.logger.info("Fetched step='{}': status={}", var2, var4.statusCode());
               return CompletableFuture.completedFuture((String)var4.body());
            }

            if (var4 != null && ((String)var4.body()).contains("HTTP 403 - Forbidden")) {
               this.logger.warn("BAN at step='{}'", var2);
               return CompletableFuture.completedFuture((Object)null);
            }

            if (var4 != null && var4.statusCode() == 404) {
               this.logger.warn("Unable to find page. Will continue step='{}'", var2);
               return CompletableFuture.completedFuture((Object)null);
            }

            this.logger.warn("Retrying to fetch step='{}'", var2);
            var10000 = VertxUtil.randomSleep(3000L);
            if (!var10000.isDone()) {
               var5 = var10000;
               return var5.exceptionally(Function.identity()).thenCompose(Yeezy::async$sendBasic);
            }

            var10000.join();
         } catch (Throwable var6) {
            this.logger.warn("Error in sending {}", var6.getMessage());
            var10000 = VertxUtil.sleep((long)this.task.getRetryDelay());
            if (!var10000.isDone()) {
               var5 = var10000;
               return var5.exceptionally(Function.identity()).thenCompose(Yeezy::async$sendBasic);
            }

            var10000.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$waitForSale(Yeezy param0, HttpRequest param1, CompletableFuture param2, HttpResponse param3, long param4, Exception param6, int param7, Object param8) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture getBloom() {
      HttpRequest var1 = this.api.bloom();
      this.logger.info("Fetching bloom");
      this.netLogInfo("Fetching bloom");

      while(true) {
         CompletableFuture var10000 = Request.send(var1);
         CompletableFuture var6;
         if (!var10000.isDone()) {
            var6 = var10000;
            return var6.exceptionally(Function.identity()).thenCompose(Yeezy::async$getBloom);
         }

         HttpResponse var2 = (HttpResponse)var10000.join();
         if (var2 != null) {
            if (((String)var2.body()).contains("HTTP 403 - Forbidden")) {
               this.logger.warn("Failed to fetch bloom: state=PROXY_BAN");
               var10000 = VertxUtil.randomSleep(3000L);
               if (!var10000.isDone()) {
                  var6 = var10000;
                  return var6.exceptionally(Function.identity()).thenCompose(Yeezy::async$getBloom);
               }

               var10000.join();
               return CompletableFuture.failedFuture(new Exception("403 BLOOM ERROR"));
            }

            if (var2.statusCode() == 200) {
               this.logger.info("Fetched bloom: state=OK");

               try {
                  JsonArray var3 = new JsonArray((String)var2.body());

                  for(int var4 = 0; var4 < var3.size(); ++var4) {
                     JsonObject var5 = var3.getJsonObject(var4);
                     if (var5.getString("product_id").equals(this.api.getSKU())) {
                        return CompletableFuture.completedFuture(var5.getString("product_name"));
                     }
                  }
               } catch (Exception var7) {
                  this.logger.error("Unable to find tag? Continuing... " + var7.getMessage());
               }

               return CompletableFuture.completedFuture("YEEZY BOOST 350 V2");
            }

            if (var2.statusCode() == 500) {
               this.logger.error("Bloom site down continuing...");
               return CompletableFuture.completedFuture("YEEZY SLIDE ADULT");
            }

            this.logger.warn("Failed to fetch bloom: status={}", var2.statusCode());
            this.netLogWarn("Failed to fetch bloom: status=" + var2.statusCode());
            var10000 = VertxUtil.sleep((long)this.task.getRetryDelay());
            if (!var10000.isDone()) {
               var6 = var10000;
               return var6.exceptionally(Function.identity()).thenCompose(Yeezy::async$getBloom);
            }

            var10000.join();
         } else {
            this.logger.warn("Failed to fetch bloom: state=NO_REPLY");
         }
      }
   }

   public void updateSensorUrlFromHTML(String var1) {
      if (var1 == null) {
         this.logger.error("Unable to update sensor URL. Continuing");
      } else {
         Matcher var2 = SENSOR_URL_PATTERN.matcher(var1);

         String var3;
         for(var3 = null; var2.find(); var3 = var2.group(1)) {
         }

         if (var3 != null) {
            this.api.setSensorUrl("https://www.yeezysupply.com" + var3);
            this.logger.info("Found sensor_url='{}'", this.api.sensorUrl);
         } else {
            this.logger.warn("No sensor found on page. Using backup...");
         }

      }
   }

   static {
      RATIO_FW_SKIP_ELIGIBLE = (double)ProxyController.PROXY_COUNT * Double.longBitsToDouble(4608083138725491507L) <= (double)TaskController.TASK_COUNT;
      V3_COOKIE_NAME = Engine.get().getClientConfiguration().getString("cookieV3", "xhwUqgFqfW88H50");
      QUEUE_POOL_INTERVAL = Engine.get().getClientConfiguration().getInteger("queuePollTime", 1250);
      browserHarvesters = new BrowserHarvesterController(VertxSingleton.INSTANCE.get(), Storage.HARVESTER_COUNT_YS);
      autoSolveHarvesters = new AutosolveHarvesterController(VertxSingleton.INSTANCE.get());
      POW_PAGE_PATTERN = Pattern.compile("branding_url_content\":\"(.*?)\"");
      SIGNUP_ID_PATTERN = Pattern.compile("yeezySupplySignupFormComponentId\\\\\":\\\\\"(.*?)\\\\\"");
      successfulCartCount = 0;
      fwCartCount = 0;
   }

   public static CompletableFuture async$prepareCart(Yeezy var0, CompletableFuture var1, String var2, int var3, Object var4) {
      label113: {
         CompletableFuture var10000;
         boolean var10001;
         label130: {
            String var9;
            CompletableFuture var10;
            label131: {
               label132: {
                  label133: {
                     label134: {
                        label101: {
                           label120: {
                              switch (var3) {
                                 case 0:
                                    var10000 = var0.getProductPage(true);
                                    if (!var10000.isDone()) {
                                       var10 = var10000;
                                       return var10.exceptionally(Function.identity()).thenCompose(Yeezy::async$prepareCart);
                                    }
                                    break;
                                 case 1:
                                    var10000 = var1;
                                    break;
                                 case 2:
                                    var10000 = var1;
                                    var9 = var2;
                                    break label120;
                                 case 3:
                                    var10000 = var1;
                                    var9 = var2;
                                    break label101;
                                 case 4:
                                    var10000 = var1;
                                    var9 = var2;
                                    break label134;
                                 case 5:
                                    var10000 = var1;
                                    var9 = var2;
                                    break label133;
                                 case 6:
                                    var10000 = var1;
                                    var9 = var2;
                                    break label131;
                                 case 7:
                                    var10000 = var1;
                                    break label130;
                                 default:
                                    throw new IllegalArgumentException();
                              }

                              var9 = (String)var10000.join();
                              if (var9 != null) {
                                 var0.updateSensorUrlFromHTML(var9);
                              }

                              var0.api.updateDocumentUrl("https://www.yeezysupply.com/product/" + var0.api.getSKU());
                              var0.api.getCookies().put("utag_main", var0.api.fetchUtag(), ".yeezysupply.com");
                              var10000 = Request.execute(var0.api.akamaiScript(true));
                              if (!var10000.isDone()) {
                                 var10 = var10000;
                                 return var10.exceptionally(Function.identity()).thenCompose(Yeezy::async$prepareCart);
                              }
                           }

                           var10000.join();
                           var10000 = var0.postSensors();
                           if (!var10000.isDone()) {
                              var10 = var10000;
                              return var10.exceptionally(Function.identity()).thenCompose(Yeezy::async$prepareCart);
                           }
                        }

                        var10000.join();
                        var10000 = var0.sendBasic(var0.api.secondaryProdAPI().timeout(15000L), "Prod Info #1");
                        if (!var10000.isDone()) {
                           var10 = var10000;
                           return var10.exceptionally(Function.identity()).thenCompose(Yeezy::async$prepareCart);
                        }
                     }

                     var10000.join();
                     if (!var0.isFirstIteration) {
                        break label132;
                     }

                     var0.isFirstIteration = false;

                     try {
                        var10000 = Request.send(var0.api.newsletter(Utils.quickParseFirst(var9, SIGNUP_ID_PATTERN)).timeout(15000L));
                        if (!var10000.isDone()) {
                           var10 = var10000;
                           return var10.exceptionally(Function.identity()).thenCompose(Yeezy::async$prepareCart);
                        }
                     } catch (Exception var8) {
                        var10001 = false;
                        break label132;
                     }
                  }

                  try {
                     var10000.join();
                  } catch (Exception var7) {
                     var10001 = false;
                  }
               }

               var10000 = var0.fillSizes();
               if (!var10000.isDone()) {
                  var10 = var10000;
                  return var10.exceptionally(Function.identity()).thenCompose(Yeezy::async$prepareCart);
               }
            }

            var10000.join();
            if (var0.isPassedFW) {
               return CompletableFuture.completedFuture((Object)null);
            }

            var0.logger.info("Preparing to cart with pre-emptive step=x00433");

            try {
               var10000 = Request.send(var0.api.emptyBasket(true, var0.authorization).timeout(20000L));
               if (!var10000.isDone()) {
                  var10 = var10000;
                  return var10.exceptionally(Function.identity()).thenCompose(Yeezy::async$prepareCart);
               }
            } catch (Exception var6) {
               var10001 = false;
               break label113;
            }
         }

         try {
            var10000.join();
         } catch (Exception var5) {
            var10001 = false;
         }
      }

      var0.visitedBaskets = true;
      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$getGenned(Yeezy param0, int param1, String param2, String param3, CompletableFuture param4, Throwable param5, int param6, Object param7) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$prepareSplash(Yeezy var0, CompletableFuture var1, String var2, String[] var3, String var4, String var5, String[] var6, int var7, String var8, Object var9, long var10, long var12, long var14, int var16, Object var17) {
      CompletableFuture var10000;
      label140: {
         CompletableFuture var11;
         String var20;
         String[] var21;
         String var22;
         long var23;
         long var26;
         long var28;
         label141: {
            label142: {
               label143: {
                  CookieJar var33;
                  label144: {
                     label145: {
                        label114: {
                           label146: {
                              label111: {
                                 label110: {
                                    label109: {
                                       boolean var29;
                                       label130: {
                                          String var10001;
                                          String[] var10002;
                                          String var10003;
                                          String var10004;
                                          switch (var16) {
                                             case 0:
                                                var10000 = var0.getHomePage();
                                                if (!var10000.isDone()) {
                                                   var11 = var10000;
                                                   return var11.exceptionally(Function.identity()).thenCompose(Yeezy::async$prepareSplash);
                                                }
                                                break;
                                             case 1:
                                                var10000 = var1;
                                                break;
                                             case 2:
                                                var10000 = var1;
                                                var10001 = var2;
                                                var21 = var3;
                                                var20 = var10001;
                                                var22 = null;
                                                break label130;
                                             case 3:
                                                var10000 = var1;
                                                var10001 = var2;
                                                var10002 = var3;
                                                var22 = var4;
                                                var21 = var10002;
                                                var20 = var10001;
                                                break label111;
                                             case 4:
                                                var10000 = var1;
                                                var10001 = var2;
                                                var10002 = var3;
                                                var22 = var4;
                                                var21 = var10002;
                                                var20 = var10001;
                                                break label114;
                                             case 5:
                                                var10000 = var1;
                                                var10001 = var2;
                                                var10002 = var3;
                                                var10003 = var4;
                                                var4 = var5;
                                                var22 = var10003;
                                                var21 = var10002;
                                                var20 = var10001;
                                                break label145;
                                             case 6:
                                                var10000 = var1;
                                                var10001 = var2;
                                                var10002 = var3;
                                                var10003 = var4;
                                                var10004 = var5;
                                                var28 = var14;
                                                var26 = var12;
                                                var23 = var10;
                                                var4 = var10004;
                                                var22 = var10003;
                                                var21 = var10002;
                                                var20 = var10001;
                                                break label143;
                                             case 7:
                                                var10000 = var1;
                                                var10001 = var2;
                                                var10002 = var3;
                                                var10003 = var4;
                                                var10004 = var5;
                                                var28 = var14;
                                                var26 = var12;
                                                var23 = var10;
                                                var4 = var10004;
                                                var22 = var10003;
                                                var21 = var10002;
                                                var20 = var10001;
                                                break label142;
                                             case 8:
                                                var10000 = var1;
                                                var10001 = var2;
                                                var10002 = var3;
                                                var10003 = var4;
                                                var10004 = var5;
                                                var28 = var14;
                                                var26 = var12;
                                                var23 = var10;
                                                var4 = var10004;
                                                var22 = var10003;
                                                var21 = var10002;
                                                var20 = var10001;
                                                break label141;
                                             case 9:
                                                var10000 = var1;
                                                break label140;
                                             default:
                                                throw new IllegalArgumentException();
                                          }

                                          var20 = (String)var10000.join();
                                          var0.updateSensorUrlFromHTML(var20);
                                          var21 = null;
                                          var22 = null;
                                          if (Engine.get().getClientConfiguration().getBoolean("noProtect", false) || var0.isNoProtection) {
                                             break label146;
                                          }

                                          try {
                                             var21 = var0.getPixelParams(var20);
                                             var10000 = Request.executeTillOk(var0.api.getPixel(var21[1]));
                                             if (!var10000.isDone()) {
                                                var11 = var10000;
                                                return var11.exceptionally(Function.identity()).thenCompose(Yeezy::async$prepareSplash);
                                             }
                                          } catch (Exception var19) {
                                             var29 = false;
                                             break label109;
                                          }
                                       }

                                       try {
                                          var22 = (String)var10000.join();
                                          break label110;
                                       } catch (Exception var18) {
                                          var29 = false;
                                       }
                                    }

                                    var0.logger.error("Fallback no baza");
                                 }

                                 var10000 = var0.getAkamaiScript();
                                 if (!var10000.isDone()) {
                                    var11 = var10000;
                                    return var11.exceptionally(Function.identity()).thenCompose(Yeezy::async$prepareSplash);
                                 }
                              }

                              var10000.join();
                           }

                           var10000 = var0.getBloom();
                           if (!var10000.isDone()) {
                              var11 = var10000;
                              return var11.exceptionally(Function.identity()).thenCompose(Yeezy::async$prepareSplash);
                           }
                        }

                        var4 = (String)var10000.join();
                        var0.api.setUtagProductName(var4);
                        var33 = var0.api.getCookies();
                        int var32 = var0.signUpSaveCount + 1;
                        var33.put("UserSignUpAndSave", "" + (var0.signUpSaveCount = var32), ".yeezysupply.com");
                        var0.api.getCookies().put("UserSignUpAndSaveOverlay", "0", ".yeezysupply.com");
                        var0.api.getCookies().put("default_searchTerms_CustomizeSearch", "%5B%5D", ".yeezysupply.com");
                        var0.api.getCookies().put("geoRedirectionAlreadySuggested", "false", ".yeezysupply.com");
                        var0.api.getCookies().put("wishlist", "%5B%5D", ".yeezysupply.com");
                        var0.api.getCookies().put("persistentBasketCount", "0", ".yeezysupply.com");
                        var0.api.getCookies().put("userBasketCount", "0", ".yeezysupply.com");
                        if (Engine.get().getClientConfiguration().getBoolean("noProtect", false) || var0.isNoProtection || var21 == null) {
                           break label144;
                        }

                        String[] var24 = Pixel.parseHexArray(var22);
                        int var25 = Pixel.parseGIndex(var22);
                        String var27 = Pixel.parseTValue(var21[2]);
                        var8 = var24[var25];
                        var10000 = var0.sendPixel(var0.api.postPixel(var21[2].split("\\?")[0]), var21[0], var27, var8);
                        if (!var10000.isDone()) {
                           var11 = var10000;
                           return var11.exceptionally(Function.identity()).thenCompose(Yeezy::async$prepareSplash);
                        }
                     }

                     var10000.join();
                  }

                  var0.pixelPostTs = System.currentTimeMillis();
                  var0.safeToRetain = false;
                  var0.api.getCookies().put("geo_country", "US", ".yeezysupply.com");
                  var0.api.getCookies().put("utag_main", var0.api.fetchUtag(), ".yeezysupply.com");
                  var0.api.getCookies().put("_ga", "GA1.2." + ThreadLocalRandom.current().nextInt(1207338862, 1992599043) + "." + System.currentTimeMillis(), ".yeezysupply.com");
                  var0.api.getCookies().put("_gid", "GA1.2." + ThreadLocalRandom.current().nextInt(120016221, 190016221) + "." + System.currentTimeMillis(), ".yeezysupply.com");
                  var0.api.getCookies().put("_gat_tealium_0", "1", ".yeezysupply.com");
                  var33 = var0.api.getCookies();
                  long var30 = Instant.now().toEpochMilli();
                  var33.put("_fbp", "fb.1." + var30 + ThreadLocalRandom.current().nextInt(1000) + "." + Instant.now().toEpochMilli(), ".yeezysupply.com");
                  var0.api.getCookies().put("_gcl_au", "1.1." + System.currentTimeMillis() + "." + System.currentTimeMillis(), ".yeezysupply.com");
                  var0.api.getCookies().put("AMCVS_7ADA401053CCF9130A490D4C%40AdobeOrg", "1", ".yeezysupply.com");
                  var23 = Instant.now().getEpochSecond() + 7200L;
                  var26 = var23 + 597600L;
                  var33 = var0.api.getCookies();
                  int var31 = Utag.H();
                  var33.put("AMCV_7ADA401053CCF9130A490D4C%40AdobeOrg", "-227196251%7CMCIDTS%7C" + var31 + "%7CMCMID%7C" + Utag.r() + "%7CMCAAMLH-" + var26 + "%7C7%7CMCAAMB-" + var26 + "%7CRKhpRz8krg2tLO6pguXWp5olkAcUniQYPHaMWWgdJ3xzPWQmdj0y%7CMCOPTOUT-" + var23 + "s%7CNONE%7CMCAID%7CNONE", ".yeezysupply.com");
                  var0.api.getCookies().put("s_cc", "true", ".yeezysupply.com");
                  var28 = ThreadLocalRandom.current().nextLong(1625112000771L, 1625112000876L);
                  var0.api.getCookies().put("s_pers", "%20s_vnum%3D" + var28 + "%2526vn%253D1%7C" + var28 + "%3B%20s_invisit%3Dtrue%7C" + (Instant.now().toEpochMilli() + 1800000L) + "%3B", ".yeezysupply.com");
                  var10000 = var0.preloadSensor();
                  if (!var10000.isDone()) {
                     var11 = var10000;
                     return var11.exceptionally(Function.identity()).thenCompose(Yeezy::async$prepareSplash);
                  }
               }

               var10000.join();
               var10000 = VertxUtil.randomSleep(3000L);
               if (!var10000.isDone()) {
                  var11 = var10000;
                  return var11.exceptionally(Function.identity()).thenCompose(Yeezy::async$prepareSplash);
               }
            }

            var10000.join();
            var10000 = var0.getProductPage(false);
            if (!var10000.isDone()) {
               var11 = var10000;
               return var11.exceptionally(Function.identity()).thenCompose(Yeezy::async$prepareSplash);
            }
         }

         var10000.join();
         var10000 = var0.waitForSale();
         if (!var10000.isDone()) {
            var11 = var10000;
            return var11.exceptionally(Function.identity()).thenCompose(Yeezy::async$prepareSplash);
         }
      }

      var10000.join();
      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture sendPixel(HttpRequest var1, String var2, String var3, String var4) {
      this.logger.info("Sending pixel");

      while(super.running) {
         CompletableFuture var10000;
         CompletableFuture var7;
         try {
            HttpResponse var5;
            String var6;
            if (this.task.getMode().contains("2")) {
               var10000 = this.api.getPixelAPI().getPixelReqString(var2, var3, var4);
               if (!var10000.isDone()) {
                  var7 = var10000;
                  return var7.exceptionally(Function.identity()).thenCompose(Yeezy::async$sendPixel);
               }

               var6 = (String)var10000.join();
               var10000 = Request.send(var1, Buffer.buffer(var6));
               if (!var10000.isDone()) {
                  var7 = var10000;
                  return var7.exceptionally(Function.identity()).thenCompose(Yeezy::async$sendPixel);
               }

               var5 = (HttpResponse)var10000.join();
            } else if (this.task.getMode().contains("3")) {
               var10000 = this.api.getPixelAPI().getPixelReqString(var2, var3, var4);
               if (!var10000.isDone()) {
                  var7 = var10000;
                  return var7.exceptionally(Function.identity()).thenCompose(Yeezy::async$sendPixel);
               }

               var6 = (String)var10000.join();
               var10000 = Request.send(var1, Buffer.buffer(var6));
               if (!var10000.isDone()) {
                  var7 = var10000;
                  return var7.exceptionally(Function.identity()).thenCompose(Yeezy::async$sendPixel);
               }

               var5 = (HttpResponse)var10000.join();
            } else {
               var10000 = this.api.getPixelAPI().getPixelReqForm(var2, var3, var4);
               if (!var10000.isDone()) {
                  var7 = var10000;
                  return var7.exceptionally(Function.identity()).thenCompose(Yeezy::async$sendPixel);
               }

               MultiMap var9 = (MultiMap)var10000.join();
               var10000 = Request.send(var1, var9);
               if (!var10000.isDone()) {
                  var7 = var10000;
                  return var7.exceptionally(Function.identity()).thenCompose(Yeezy::async$sendPixel);
               }

               var5 = (HttpResponse)var10000.join();
            }

            if (var5 != null) {
               if (var5.statusCode() == 200) {
                  this.logger.info("Handled FW ok!");
                  return CompletableFuture.completedFuture((Object)null);
               }

               this.logger.warn("Failed to send FW: status={}", var5.statusCode());
            }

            var10000 = VertxUtil.randomSignalSleep(this.instanceSignal, (long)this.task.getRetryDelay());
            if (!var10000.isDone()) {
               var7 = var10000;
               return var7.exceptionally(Function.identity()).thenCompose(Yeezy::async$sendPixel);
            }

            var10000.join();
         } catch (Exception var8) {
            this.logger.error("Error occurred on sending FW: '{}'", var8.getMessage());
            var10000 = VertxUtil.randomSleep(3000L);
            if (!var10000.isDone()) {
               var7 = var10000;
               return var7.exceptionally(Function.identity()).thenCompose(Yeezy::async$sendPixel);
            }

            var10000.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture checkout() {
      CompletableFuture var10000;
      CompletableFuture var2;
      if (this.visitedBaskets) {
         var10000 = this.postSensors();
         if (!var10000.isDone()) {
            var2 = var10000;
            return var2.exceptionally(Function.identity()).thenCompose(Yeezy::async$checkout);
         }

         var10000.join();
      }

      var10000 = this.postSensorsTilNoChallenge();
      if (!var10000.isDone()) {
         var2 = var10000;
         return var2.exceptionally(Function.identity()).thenCompose(Yeezy::async$checkout);
      } else {
         var10000.join();
         var10000 = this.submitShippingAndBilling();
         if (!var10000.isDone()) {
            var2 = var10000;
            return var2.exceptionally(Function.identity()).thenCompose(Yeezy::async$checkout);
         } else {
            String var1 = (String)var10000.join();
            if (this.isCodeRequired(var1)) {
               var10000 = this.handleCode();
               if (!var10000.isDone()) {
                  var2 = var10000;
                  return var2.exceptionally(Function.identity()).thenCompose(Yeezy::async$checkout);
               }

               var10000.join();
            }

            this.api.getCookies().put("UserSignUpAndSaveOverlay", "1", ".yeezysupply.com");
            this.api.getCookies().put("pagecontext_cookies", "", ".yeezysupply.com");
            this.api.getCookies().put("pagecontext_secure_cookies", "", ".yeezysupply.com");
            var10000 = this.postSensorsTilNoChallenge();
            if (!var10000.isDone()) {
               var2 = var10000;
               return var2.exceptionally(Function.identity()).thenCompose(Yeezy::async$checkout);
            } else {
               var10000.join();
               var10000 = this.processPayment();
               if (!var10000.isDone()) {
                  var2 = var10000;
                  return var2.exceptionally(Function.identity()).thenCompose(Yeezy::async$checkout);
               } else {
                  var10000.join();
                  return CompletableFuture.completedFuture((Object)null);
               }
            }
         }
      }
   }

   public CompletableFuture prepareSplash() {
      CompletableFuture var10000 = this.getHomePage();
      CompletableFuture var11;
      if (!var10000.isDone()) {
         var11 = var10000;
         return var11.exceptionally(Function.identity()).thenCompose(Yeezy::async$prepareSplash);
      } else {
         String var1 = (String)var10000.join();
         this.updateSensorUrlFromHTML(var1);
         String[] var2 = null;
         String var3 = null;
         if (!Engine.get().getClientConfiguration().getBoolean("noProtect", false) && !this.isNoProtection) {
            try {
               var2 = this.getPixelParams(var1);
               var10000 = Request.executeTillOk(this.api.getPixel(var2[1]));
               if (!var10000.isDone()) {
                  var11 = var10000;
                  return var11.exceptionally(Function.identity()).thenCompose(Yeezy::async$prepareSplash);
               }

               var3 = (String)var10000.join();
            } catch (Exception var12) {
               this.logger.error("Fallback no baza");
            }

            var10000 = this.getAkamaiScript();
            if (!var10000.isDone()) {
               var11 = var10000;
               return var11.exceptionally(Function.identity()).thenCompose(Yeezy::async$prepareSplash);
            }

            var10000.join();
         }

         var10000 = this.getBloom();
         if (!var10000.isDone()) {
            var11 = var10000;
            return var11.exceptionally(Function.identity()).thenCompose(Yeezy::async$prepareSplash);
         } else {
            String var4 = (String)var10000.join();
            this.api.setUtagProductName(var4);
            CookieJar var15 = this.api.getCookies();
            int var10004 = this.signUpSaveCount + 1;
            var15.put("UserSignUpAndSave", "" + (this.signUpSaveCount = var10004), ".yeezysupply.com");
            this.api.getCookies().put("UserSignUpAndSaveOverlay", "0", ".yeezysupply.com");
            this.api.getCookies().put("default_searchTerms_CustomizeSearch", "%5B%5D", ".yeezysupply.com");
            this.api.getCookies().put("geoRedirectionAlreadySuggested", "false", ".yeezysupply.com");
            this.api.getCookies().put("wishlist", "%5B%5D", ".yeezysupply.com");
            this.api.getCookies().put("persistentBasketCount", "0", ".yeezysupply.com");
            this.api.getCookies().put("userBasketCount", "0", ".yeezysupply.com");
            if (!Engine.get().getClientConfiguration().getBoolean("noProtect", false) && !this.isNoProtection && var2 != null) {
               String[] var5 = Pixel.parseHexArray(var3);
               int var6 = Pixel.parseGIndex(var3);
               String var7 = Pixel.parseTValue(var2[2]);
               String var8 = var5[var6];
               var10000 = this.sendPixel(this.api.postPixel(var2[2].split("\\?")[0]), var2[0], var7, var8);
               if (!var10000.isDone()) {
                  var11 = var10000;
                  return var11.exceptionally(Function.identity()).thenCompose(Yeezy::async$prepareSplash);
               }

               var10000.join();
            }

            this.pixelPostTs = System.currentTimeMillis();
            this.safeToRetain = false;
            this.api.getCookies().put("geo_country", "US", ".yeezysupply.com");
            this.api.getCookies().put("utag_main", this.api.fetchUtag(), ".yeezysupply.com");
            this.api.getCookies().put("_ga", "GA1.2." + ThreadLocalRandom.current().nextInt(1207338862, 1992599043) + "." + System.currentTimeMillis(), ".yeezysupply.com");
            this.api.getCookies().put("_gid", "GA1.2." + ThreadLocalRandom.current().nextInt(120016221, 190016221) + "." + System.currentTimeMillis(), ".yeezysupply.com");
            this.api.getCookies().put("_gat_tealium_0", "1", ".yeezysupply.com");
            var15 = this.api.getCookies();
            long var10002 = Instant.now().toEpochMilli();
            var15.put("_fbp", "fb.1." + var10002 + ThreadLocalRandom.current().nextInt(1000) + "." + Instant.now().toEpochMilli(), ".yeezysupply.com");
            this.api.getCookies().put("_gcl_au", "1.1." + System.currentTimeMillis() + "." + System.currentTimeMillis(), ".yeezysupply.com");
            this.api.getCookies().put("AMCVS_7ADA401053CCF9130A490D4C%40AdobeOrg", "1", ".yeezysupply.com");
            long var13 = Instant.now().getEpochSecond() + 7200L;
            long var14 = var13 + 597600L;
            var15 = this.api.getCookies();
            int var16 = Utag.H();
            var15.put("AMCV_7ADA401053CCF9130A490D4C%40AdobeOrg", "-227196251%7CMCIDTS%7C" + var16 + "%7CMCMID%7C" + Utag.r() + "%7CMCAAMLH-" + var14 + "%7C7%7CMCAAMB-" + var14 + "%7CRKhpRz8krg2tLO6pguXWp5olkAcUniQYPHaMWWgdJ3xzPWQmdj0y%7CMCOPTOUT-" + var13 + "s%7CNONE%7CMCAID%7CNONE", ".yeezysupply.com");
            this.api.getCookies().put("s_cc", "true", ".yeezysupply.com");
            long var9 = ThreadLocalRandom.current().nextLong(1625112000771L, 1625112000876L);
            this.api.getCookies().put("s_pers", "%20s_vnum%3D" + var9 + "%2526vn%253D1%7C" + var9 + "%3B%20s_invisit%3Dtrue%7C" + (Instant.now().toEpochMilli() + 1800000L) + "%3B", ".yeezysupply.com");
            var10000 = this.preloadSensor();
            if (!var10000.isDone()) {
               var11 = var10000;
               return var11.exceptionally(Function.identity()).thenCompose(Yeezy::async$prepareSplash);
            } else {
               var10000.join();
               var10000 = VertxUtil.randomSleep(3000L);
               if (!var10000.isDone()) {
                  var11 = var10000;
                  return var11.exceptionally(Function.identity()).thenCompose(Yeezy::async$prepareSplash);
               } else {
                  var10000.join();
                  var10000 = this.getProductPage(false);
                  if (!var10000.isDone()) {
                     var11 = var10000;
                     return var11.exceptionally(Function.identity()).thenCompose(Yeezy::async$prepareSplash);
                  } else {
                     var10000.join();
                     var10000 = this.waitForSale();
                     if (!var10000.isDone()) {
                        var11 = var10000;
                        return var11.exceptionally(Function.identity()).thenCompose(Yeezy::async$prepareSplash);
                     } else {
                        var10000.join();
                        return CompletableFuture.completedFuture((Object)null);
                     }
                  }
               }
            }
         }
      }
   }

   public CompletableFuture getGenned() {
      int var1 = 0;

      while(super.running && var1++ <= 3) {
         CompletableFuture var8;
         CompletableFuture var10;
         try {
            String var2 = Utils.quickParseFirst(this.api.userAgent, CHROME_VERSION_PATTERN);
            if (var2 == null) {
               break;
            }

            String var3 = this.api.userAgent.toLowerCase(Locale.ROOT).contains("windows") ? "win" : "mac";
            var10 = Request.send(this.api.abckFromStore(var3, var2));
            if (!var10.isDone()) {
               var8 = var10;
               return var8.exceptionally(Function.identity()).thenCompose(Yeezy::async$getGenned);
            }

            HttpResponse var4 = (HttpResponse)var10.join();
            if (var4 != null) {
               JsonObject var5 = (JsonObject)var4.body();
               if (var5.containsKey("error") && var5.getString("error", "").equalsIgnoreCase("no value available")) {
                  break;
               }

               if (var5.containsKey("result")) {
                  JsonObject var6 = var5.getJsonObject("result");
                  String var7 = var6.getString("value", (String)null);
                  if (var7 != null) {
                     return CompletableFuture.completedFuture(var7);
                  }
               }
            }
         } catch (Throwable var9) {
            this.logger.error("Failed fetching startup token");
            var10 = VertxUtil.randomSleep(1234L);
            if (!var10.isDone()) {
               var8 = var10;
               return var8.exceptionally(Function.identity()).thenCompose(Yeezy::async$getGenned);
            }

            var10.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture run() {
      CompletableFuture var10000;
      CompletableFuture var4;
      byte var6;
      label82: {
         if (!this.task.getMode().contains("noqueue")) {
            var10000 = this.harvesters.start();
            if (!var10000.isDone()) {
               var4 = var10000;
               return var4.exceptionally(Function.identity()).thenCompose(Yeezy::async$run);
            }

            if (!(Boolean)var10000.join()) {
               var6 = 0;
               break label82;
            }
         }

         var6 = 1;
      }

      byte var1 = var6;
      String var2 = null;

      while(var1 != 0) {
         try {
            var10000 = this.api.init();
            if (!var10000.isDone()) {
               var4 = var10000;
               return var4.exceptionally(Function.identity()).thenCompose(Yeezy::async$run);
            }

            var10000.join();
            if (this.task.getMode().contains("test")) {
               if (var2 != null && this.safeToRetain) {
                  this.api.getCookies().put("_abck", var2, ".yeezysupply.com");
               } else {
                  var10000 = this.getGenned();
                  if (!var10000.isDone()) {
                     var4 = var10000;
                     return var4.exceptionally(Function.identity()).thenCompose(Yeezy::async$run);
                  }

                  var2 = (String)var10000.join();
                  if (var2 != null && !var2.isBlank()) {
                     this.api.getCookies().put("_abck", var2, ".yeezysupply.com");
                     this.safeToRetain = true;
                  }
               }
            }

            var10000 = this.prepareSplash();
            if (!var10000.isDone()) {
               var4 = var10000;
               return var4.exceptionally(Function.identity()).thenCompose(Yeezy::async$run);
            }

            var10000.join();
            var10000 = this.queue();
            if (!var10000.isDone()) {
               var4 = var10000;
               return var4.exceptionally(Function.identity()).thenCompose(Yeezy::async$run);
            }

            var10000.join();
            var10000 = this.prepareCart();
            if (!var10000.isDone()) {
               var4 = var10000;
               return var4.exceptionally(Function.identity()).thenCompose(Yeezy::async$run);
            }

            var10000.join();
            var10000 = this.cart();
            if (!var10000.isDone()) {
               var4 = var10000;
               return var4.exceptionally(Function.identity()).thenCompose(Yeezy::async$run);
            }

            var10000.join();
            this.api.getCookies().put("userBasketCount", "1", ".yeezysupply.com");
            this.api.getCookies().put("persistentBasketCount", "1", ".yeezysupply.com");
            this.api.getCookies().put("restoreBasketUrl", "%2Fon%2Fdemandware.store%2FSites-ys-US-Site%2Fen_US%2FCart-UpdateItems%3Fpid_0%3D" + this.api.getSKU() + Sizes.getSize(this.task.getSize()) + "%26qty_0%3D1%26", ".yeezysupply.com");
            var10000 = this.checkout();
            if (!var10000.isDone()) {
               var4 = var10000;
               return var4.exceptionally(Function.identity()).thenCompose(Yeezy::async$run);
            }

            var10000.join();
            var1 = 0;
         } catch (Throwable var5) {
            if (!var5.getMessage().contains("403")) {
               this.logger.error(var5.getMessage());
            } else if (var5.getMessage().contains("FW")) {
               this.logger.error(var5.getMessage());
            }

            this.sensorPosts = 0;
            this.visitedBaskets = false;
            this.api.close();
            this.api = new YeezyAPI(this.task);
            super.setClient(this.api);
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$queue(Yeezy param0, int param1, int param2, int param3, HttpRequest param4, CompletableFuture param5, SharedCaptchaToken param6, HttpResponse param7, int param8, String param9, int param10, Exception param11, int param12, Object param13) {
      // $FF: Couldn't be decompiled
   }

   public static Boolean lambda$handlePOW$0(HttpResponse var0) {
      return var0.statusCode() == 200 ? true : null;
   }

   public void setZipAsBanned() {
      try {
         if (this.profilesOfZipCode != null) {
            this.profilesOfZipCode.markBanned();
            this.profilesOfZipCode = null;
         }
      } catch (Throwable var2) {
      }

   }

   public static CompletableFuture async$getProductPage(Yeezy param0, int param1, HttpRequest param2, int param3, CompletableFuture param4, HttpResponse param5, String param6, int param7, int param8, Exception param9, int param10, Object param11) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$getSizeJson(Yeezy param0, int param1, HttpRequest param2, int param3, CompletableFuture param4, HttpResponse param5, String param6, int param7, Object param8) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture submitShippingAndBilling() {
      byte var1 = 0;

      while(var1 <= 50) {
         HttpRequest var2 = this.api.submitShippingAndBilling(this.basketID, this.authorization);
         this.checkHmac();
         this.instanceProfile = (Profile)this.getOrLoadProfile().orElseGet(this::lambda$submitShippingAndBilling$7);
         Buffer var3 = this.api.billingBody(this.instanceProfile);

         CompletableFuture var10000;
         CompletableFuture var6;
         try {
            this.api.updateUtagUrl("https://www.yeezysupply.com/delivery");
            this.api.getCookies().put("utag_main", this.api.fetchUtag(), ".yeezysupply.com");
            var10000 = Request.send(var2, var3);
            if (!var10000.isDone()) {
               var6 = var10000;
               return var6.exceptionally(Function.identity()).thenCompose(Yeezy::async$submitShippingAndBilling);
            }

            HttpResponse var4 = (HttpResponse)var10000.join();
            if (var4 != null && var4.statusCode() == 501) {
               this.logger.info("ShippingBilling (weak task proxy): status={} r={}", var4.statusCode(), ((String)var4.body()).replace("\n", ""));
               this.isPassedFW = true;
               this.isBannedOnShipping = true;
               var10000 = this.prepareCart();
               if (!var10000.isDone()) {
                  var6 = var10000;
                  return var6.exceptionally(Function.identity()).thenCompose(Yeezy::async$submitShippingAndBilling);
               }

               var10000.join();
               var10000 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
               if (!var10000.isDone()) {
                  var6 = var10000;
                  return var6.exceptionally(Function.identity()).thenCompose(Yeezy::async$submitShippingAndBilling);
               }

               var10000.join();
            } else {
               int var10001;
               if (var4 != null && var4.statusCode() != 200) {
                  this.logger.warn("Failed to submit shipping & billing: status={} r={}", var4.statusCode(), var4.body());
                  var10001 = var4.statusCode();
                  this.netLogWarn("Failed to submit shipping & billing: status=" + var10001 + " body='" + (String)var4.body() + "'");
                  if (((String)var4.body()).contains("The shipping address postal code is blacklisted")) {
                     this.logger.info("Banned zipcode found -> zipcode={}", this.instanceProfile.getZip());
                     this.shouldRotate = true;
                     this.setZipAsBanned();
                  }
               } else if (var4 != null) {
                  this.isBannedOnShipping = false;
                  String var5 = (String)var4.body();
                  this.logger.info("ShippingBilling: status={} r={}", var4.statusCode(), ((String)var4.body()).replace("\n", ""));
                  var10001 = var4.statusCode();
                  this.netLogInfo("ShippingBilling: status=" + var10001 + " body='" + (String)var4.body() + "'");
                  if (var4.getHeader("authorization") == null) {
                     this.logger.warn("Fake shipping (animal)");
                     this.netLogInfo("Fake shipping (animal) body='" + (String)var4.body() + "'");
                     Analytics.log("Fake shipping (animal)", this.task, this.api.getCookies().getCookieValueLength("_abck"), this.sensorPosts, this.visitedBaskets, this.api.userAgent);
                  } else {
                     if (!var5.contains("\"total\":0") && var5.contains("basketId")) {
                        this.logger.info("Submit shipping & billing SUCCESSFUL!: r={}", var5);
                        this.netLogInfo("Submit shipping & billing SUCCESSFUL! body='" + (String)var4.body() + "'");
                        return CompletableFuture.completedFuture(var5);
                     }

                     if (var5.contains("\"total\":0")) {
                        this.logger.warn("Error shipping & billing:  reason={}", "cart jacked");
                        var10000 = this.prepareCart();
                        if (!var10000.isDone()) {
                           var6 = var10000;
                           return var6.exceptionally(Function.identity()).thenCompose(Yeezy::async$submitShippingAndBilling);
                        }

                        var10000.join();
                        var10000 = this.cart();
                        if (!var10000.isDone()) {
                           var6 = var10000;
                           return var6.exceptionally(Function.identity()).thenCompose(Yeezy::async$submitShippingAndBilling);
                        }

                        var10000.join();
                     } else {
                        this.logger.warn("Error shipping & billing:  reason={} headers={}", var5.contains("HTTP 403 - Forbidden"), var4.headers().toString().replace("\n", ""));
                     }
                  }
               }

               var10000 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
               if (!var10000.isDone()) {
                  var6 = var10000;
                  return var6.exceptionally(Function.identity()).thenCompose(Yeezy::async$submitShippingAndBilling);
               }

               var10000.join();
               var10000 = this.postSensors();
               if (!var10000.isDone()) {
                  var6 = var10000;
                  return var6.exceptionally(Function.identity()).thenCompose(Yeezy::async$submitShippingAndBilling);
               }

               var10000.join();
            }
         } catch (Exception var7) {
            this.logger.error("Error occurred on sending shipping and billing: '{}'", var7.getMessage());
            this.netLogError("Error occurred on sending shipping and billing: message=" + var7.getMessage());
            var10000 = VertxUtil.randomSleep(3000L);
            if (!var10000.isDone()) {
               var6 = var10000;
               return var6.exceptionally(Function.identity()).thenCompose(Yeezy::async$submitShippingAndBilling);
            }

            var10000.join();
         }
      }

      return CompletableFuture.failedFuture(new Exception("Cycle Error"));
   }

   public static CompletableFuture async$handleCode(Yeezy param0, HttpRequest param1, String param2, int param3, CompletableFuture param4, Buffer param5, HttpResponse param6, Throwable param7, int param8, Object param9) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$submitShippingAndBilling(Yeezy param0, int param1, HttpRequest param2, Buffer param3, CompletableFuture param4, HttpResponse param5, String param6, Exception param7, int param8, Object param9) {
      // $FF: Couldn't be decompiled
   }

   public void swapClient() {
      try {
         RealClient var1 = RealClientFactory.fromOther(Vertx.currentContext().owner(), this.api.getWebClient(), this.api.getWebClient().type());
         this.api.client.close();
         this.api.client = var1;
      } catch (Throwable var2) {
      }

   }

   public CompletableFuture cart() {
      this.logger.info("Proceeding to cart...");

      try {
         this.logger.info("Using available sizes for sku={} : sizes={}", this.api.getSKU(), this.availableSizes);
      } catch (Throwable var8) {
      }

      byte var1 = 0;
      this.netLogInfo("Adding to cart");

      while(super.running) {
         CompletableFuture var10000;
         CompletableFuture var7;
         if (!this.instanceSignal.equals(this.api.getSKU())) {
            this.logger.info("Fetching updated item {}", this.instanceSignal = this.api.getSKU());
            var10000 = this.fillSizes();
            if (!var10000.isDone()) {
               var7 = var10000;
               return var7.exceptionally(Function.identity()).thenCompose(Yeezy::async$cart);
            }

            var10000.join();
         }

         Buffer var2 = this.atcForm();
         this.checkHmac();
         this.logger.info("Adding to cart");

         try {
            HttpRequest var3 = this.api.addToCart();
            var10000 = Request.send(var3, var2);
            if (!var10000.isDone()) {
               var7 = var10000;
               return var7.exceptionally(Function.identity()).thenCompose(Yeezy::async$cart);
            }

            HttpResponse var4 = (HttpResponse)var10000.join();
            if (var4 == null) {
               this.logger.warn("Failed to ATC: state=NO_RESPONSE");
            } else {
               this.logger.info("ATC Responded with status={}", var4.statusCode());
               if (var4.statusCode() >= 500) {
                  this.logger.info("Site dead on ATC");
               } else if (var4.statusCode() == 403) {
                  this.isPassedFW = false;
                  var1 = 0;
                  this.logger.warn("FW Blocked on ATC [{}]", fwCartCount++);
                  this.netLogWarn("Failed to ATC: state='FW' status=" + var4.statusCode());
                  Analytics.log("ATC FW Blocked", this.task, this.api.getCookies().getCookieValueLength("_abck"), this.sensorPosts, this.visitedBaskets, this.api.userAgent);
               } else {
                  this.isPassedFW = true;
                  String var5 = ((String)var4.body()).replace("\n", "");
                  var1 = 1;
                  if (var5.contains("branding_url_content")) {
                     var10000 = this.handlePOW(var5);
                     if (!var10000.isDone()) {
                        var7 = var10000;
                        return var7.exceptionally(Function.identity()).thenCompose(Yeezy::async$cart);
                     }

                     var10000.join();
                     continue;
                  }

                  if (var5.contains("basketId")) {
                     this.authorization = var4.getHeader("authorization");
                     if (this.authorization == null) {
                        this.logger.error("Fake cart (animal)");
                        this.netLogWarn("Fake cart (animal) body='" + var5 + "'");
                        Analytics.log("Fake cart (animal)", this.task, this.api.getCookies().getCookieValueLength("_abck"), this.sensorPosts, this.visitedBaskets, this.api.userAgent);
                     } else {
                        if (!var5.contains("\"total\":0") || this.task.getMode().contains("noqueue")) {
                           Analytics.carts.increment();
                           this.logger.info("ATC SUCCESSFUL count={} time={}: {}", successfulCartCount++, System.currentTimeMillis() - this.pixelPostTs, var5);
                           this.basketID = (new JsonObject(var5)).getString("basketId");
                           this.netLogInfo("ATC SUCCESSFUL body='" + var5 + "'");
                           Analytics.log("ATC OK", this.task, this.api.getCookies().getCookieValueLength("_abck"), this.sensorPosts, this.visitedBaskets, this.api.userAgent);
                           if (this.tokenRef.get() != null) {
                              SharedCaptchaToken var6 = (SharedCaptchaToken)this.tokenRef.get();
                              var6.markPassed();
                              this.tokenRef.set((Object)null);
                           }
                           break;
                        }

                        this.logger.info("EMPTY_CART (OOS): {}", var5);
                        this.netLogInfo("EMPTY_CART (OOS): body='" + var5 + "'");
                     }
                  } else if (var5.length() == 0) {
                     this.logger.warn("Failed to ATC: state={} status={}", fwCartCount++, var4.statusCode());
                     this.netLogWarn("Failed to ATC: state='FW Blocked' status=" + var4.statusCode());
                  } else if (var5.contains("HTTP 403 - Forbidden")) {
                     var1 = 0;
                     this.logger.warn("Failed to ATC: state={} status={}", "Blocked by 403 body", var4.statusCode());
                     this.netLogWarn("Failed to ATC: state='Blocked by 403 body' status=" + var4.statusCode());
                  } else if (var4.statusCode() == 400) {
                     this.logger.warn("Failed to ATC: state={} status={}", "Blocked by 400 (bad session)", var4.statusCode());
                     this.netLogWarn("Failed to ATC: state='Blocked by 400 (bad session)' status=" + var4.statusCode());
                  } else {
                     this.logger.warn("Failed to ATC: state={} status={} body={}", "No Basket", var4.statusCode(), var4.body());
                     int var10001 = var4.statusCode();
                     this.netLogWarn("Failed to ATC: state='No Basket' status=" + var10001 + " body='" + var5 + "'");
                  }
               }

               if (var1 != 0 && this.tokenRef.get() != null) {
                  SharedCaptchaToken var10 = (SharedCaptchaToken)this.tokenRef.get();
                  var10.markPassed();
                  this.tokenRef.set((Object)null);
               }
            }
         } catch (Throwable var9) {
            this.logger.error("Error adding to cart: {}", var9.getMessage());
            this.netLogError("Error adding to cart: message=" + var9.getMessage());
         }

         var10000 = this.smartSleep((long)this.task.getRetryDelay());
         if (!var10000.isDone()) {
            var7 = var10000;
            return var7.exceptionally(Function.identity()).thenCompose(Yeezy::async$cart);
         }

         var10000.join();
         if (this.api.getCookies().getCookieValue("_abck").length() < 561) {
            var10000 = this.postSensors();
            if (!var10000.isDone()) {
               var7 = var10000;
               return var7.exceptionally(Function.identity()).thenCompose(Yeezy::async$cart);
            }

            var10000.join();
         } else if (this.isPassedFW) {
            var10000 = this.postSensorsTilNoChallenge();
            if (!var10000.isDone()) {
               var7 = var10000;
               return var7.exceptionally(Function.identity()).thenCompose(Yeezy::async$cart);
            }

            var10000.join();
         } else {
            var10000 = this.prepareCart();
            if (!var10000.isDone()) {
               var7 = var10000;
               return var7.exceptionally(Function.identity()).thenCompose(Yeezy::async$cart);
            }

            var10000.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture prepareCart() {
      CompletableFuture var10000 = this.getProductPage(true);
      CompletableFuture var3;
      if (!var10000.isDone()) {
         var3 = var10000;
         return var3.exceptionally(Function.identity()).thenCompose(Yeezy::async$prepareCart);
      } else {
         String var1 = (String)var10000.join();
         if (var1 != null) {
            this.updateSensorUrlFromHTML(var1);
         }

         this.api.updateDocumentUrl("https://www.yeezysupply.com/product/" + this.api.getSKU());
         this.api.getCookies().put("utag_main", this.api.fetchUtag(), ".yeezysupply.com");
         var10000 = Request.execute(this.api.akamaiScript(true));
         if (!var10000.isDone()) {
            var3 = var10000;
            return var3.exceptionally(Function.identity()).thenCompose(Yeezy::async$prepareCart);
         } else {
            var10000.join();
            var10000 = this.postSensors();
            if (!var10000.isDone()) {
               var3 = var10000;
               return var3.exceptionally(Function.identity()).thenCompose(Yeezy::async$prepareCart);
            } else {
               var10000.join();
               var10000 = this.sendBasic(this.api.secondaryProdAPI().timeout(15000L), "Prod Info #1");
               if (!var10000.isDone()) {
                  var3 = var10000;
                  return var3.exceptionally(Function.identity()).thenCompose(Yeezy::async$prepareCart);
               } else {
                  var10000.join();
                  if (this.isFirstIteration) {
                     this.isFirstIteration = false;

                     try {
                        var10000 = Request.send(this.api.newsletter(Utils.quickParseFirst(var1, SIGNUP_ID_PATTERN)).timeout(15000L));
                        if (!var10000.isDone()) {
                           var3 = var10000;
                           return var3.exceptionally(Function.identity()).thenCompose(Yeezy::async$prepareCart);
                        }

                        var10000.join();
                     } catch (Exception var5) {
                     }
                  }

                  var10000 = this.fillSizes();
                  if (!var10000.isDone()) {
                     var3 = var10000;
                     return var3.exceptionally(Function.identity()).thenCompose(Yeezy::async$prepareCart);
                  } else {
                     var10000.join();
                     if (!this.isPassedFW) {
                        this.logger.info("Preparing to cart with pre-emptive step=x00433");

                        try {
                           var10000 = Request.send(this.api.emptyBasket(true, this.authorization).timeout(20000L));
                           if (!var10000.isDone()) {
                              var3 = var10000;
                              return var3.exceptionally(Function.identity()).thenCompose(Yeezy::async$prepareCart);
                           }

                           var10000.join();
                        } catch (Exception var4) {
                        }

                        this.visitedBaskets = true;
                     }

                     return CompletableFuture.completedFuture((Object)null);
                  }
               }
            }
         }
      }
   }

   public Buffer atcForm() {
      Buffer var1;
      if (this.task.getSize().equalsIgnoreCase("random") && !this.availableSizes.isEmpty()) {
         this.logger.info("Picking any available size...");

         try {
            var1 = this.api.atcForm(Sizes.findAnyAvailableJSON(this.availableSizes)).toBuffer();
         } catch (Sizes$NoAvailableSizeException var6) {
            this.logger.info("All sizes reported out of stock. Trying anyways...");
            var1 = this.api.atcForm(Sizes.findAnyJSON(this.availableSizes)).toBuffer();
         }
      } else if (this.task.getSize().contains("&")) {
         try {
            List var2 = Arrays.asList(this.task.getSize().toLowerCase().split("&"));
            if (this.availableSizes.isEmpty()) {
               this.logger.info("No pre-fetched sizes available. Picking any within range...");
               String var3 = (String)var2.get(ThreadLocalRandom.current().nextInt(var2.size()));
               var1 = this.api.atcForm(var3).toBuffer();
            } else {
               this.logger.info("Picking any available size within range...");
               var1 = this.api.atcForm(Sizes.findAnyAvailableOfRangeJSON(var2, this.availableSizes)).toBuffer();
            }
         } catch (Throwable var5) {
            this.logger.warn("Failed to pick sizes within range. Recovering: {}", var5.getMessage());
            var1 = this.api.atcForm().toBuffer();
         }
      } else if (!this.task.getSize().contains("random") && !this.availableSizes.isEmpty()) {
         this.logger.info("Selecting specific size [dyn-list-iter]");

         try {
            var1 = this.api.atcForm(Sizes.findAnyAvailableOfRangeJSON(List.of(this.task.getSize()), this.availableSizes)).toBuffer();
         } catch (Throwable var4) {
            this.logger.warn("Failed to pick size. Recovering: {}", var4.getMessage());
            var1 = this.api.atcForm().toBuffer();
         }
      } else {
         this.logger.info("Using size choice of size={}", this.task.getSize());
         var1 = this.api.atcForm().toBuffer();
      }

      return var1;
   }

   public static CompletableFuture async$postSensors(Yeezy var0, String var1, CookieJar var2, int var3, CompletableFuture var4, String var5, int var6, int var7, Object var8) {
      String var9;
      int var10;
      label172: {
         int var12;
         CompletableFuture var15;
         CompletableFuture var10000;
         label171: {
            switch (var7) {
               case 0:
                  if (Engine.get().getClientConfiguration().getBoolean("noProtect", false) || var0.isNoProtection) {
                     return CompletableFuture.completedFuture((Object)null);
                  }

                  var1 = var0.api.userAgent;
                  var2 = var0.api.getCookies();
                  if (!var0.isFirewallOn()) {
                     var0.api.getWebClient().resetCookieStore();
                     Iterator var11 = var2.get(true, ".yeezysupply.com", "/").iterator();

                     while(var11.hasNext()) {
                        Cookie var13 = (Cookie)var11.next();
                        if (!var13.name().contains("_abck")) {
                           if (!var13.name().startsWith("bm_")) {
                              if (!var13.name().equals("ak_bmsc")) {
                                 if (var13.name().equalsIgnoreCase("RT")) {
                                    var0.api.getCookies().put(var13);
                                    var2.remove(var13);
                                 }
                              } else {
                                 var0.api.getCookies().put(var13);
                                 var2.remove(var13);
                              }
                           } else {
                              var0.api.getCookies().put(var13);
                              var2.remove(var13);
                           }
                        } else {
                           var0.api.getCookies().put(var13);
                           var2.remove(var13);
                        }
                     }

                     var3 = 0;
                  } else {
                     var3 = 0;
                  }
                  break;
               case 1:
                  var4.join();
                  var10000 = var0.smartSleep(500L);
                  if (!var10000.isDone()) {
                     var15 = var10000;
                     return var15.exceptionally(Function.identity()).thenCompose(Yeezy::async$postSensors);
                  }

                  var10000.join();
                  ++var3;
                  break;
               case 2:
                  var4.join();
                  ++var3;
                  break;
               case 3:
                  var12 = var6;
                  var10 = var3;
                  var4.join();
                  var10000 = var0.smartUA();
                  if (!var10000.isDone()) {
                     var15 = var10000;
                     return var15.exceptionally(Function.identity()).thenCompose(Yeezy::async$postSensors);
                  }

                  var10000.join();
                  var10000 = var0.sendSensor(var0.api.postSensor(true), true);
                  if (!var10000.isDone()) {
                     var15 = var10000;
                     return var15.exceptionally(Function.identity()).thenCompose(Yeezy::async$postSensors);
                  }
                  break label171;
               case 4:
                  var12 = var6;
                  var10 = var3;
                  var4.join();
                  var10000 = var0.sendSensor(var0.api.postSensor(true), true);
                  if (!var10000.isDone()) {
                     var15 = var10000;
                     return var15.exceptionally(Function.identity()).thenCompose(Yeezy::async$postSensors);
                  }
                  break label171;
               case 5:
                  var10000 = var4;
                  var12 = var6;
                  var10 = var3;
                  break label171;
               default:
                  throw new IllegalArgumentException();
            }

            while(true) {
               if (var3 >= ThreadLocalRandom.current().nextInt(2, 4)) {
                  var9 = "";
                  byte var14 = 0;
                  var12 = ThreadLocalRandom.current().nextInt(9, 16);
                  var10 = var14 + 1;
                  if (var14 >= var12) {
                     break label172;
                  }

                  var10000 = var0.smartSleep(750L);
                  if (!var10000.isDone()) {
                     var15 = var10000;
                     return var15.exceptionally(Function.identity()).thenCompose(Yeezy::async$postSensors);
                  }

                  var10000.join();
                  var10000 = var0.smartUA();
                  if (!var10000.isDone()) {
                     var15 = var10000;
                     return var15.exceptionally(Function.identity()).thenCompose(Yeezy::async$postSensors);
                  }

                  var10000.join();
                  var10000 = var0.sendSensor(var0.api.postSensor(true), true);
                  if (!var10000.isDone()) {
                     var15 = var10000;
                     return var15.exceptionally(Function.identity()).thenCompose(Yeezy::async$postSensors);
                  }
                  break;
               }

               var10000 = var0.sendSensor(var0.api.postSensor(true), false);
               if (!var10000.isDone()) {
                  var15 = var10000;
                  return var15.exceptionally(Function.identity()).thenCompose(Yeezy::async$postSensors);
               }

               var10000.join();
               var10000 = var0.smartSleep(500L);
               if (!var10000.isDone()) {
                  var15 = var10000;
                  return var15.exceptionally(Function.identity()).thenCompose(Yeezy::async$postSensors);
               }

               var10000.join();
               ++var3;
            }
         }

         while(true) {
            var9 = (String)var10000.join();
            if (var10 > 6 && var9 != null && var9.contains("false")) {
               var0.logger.info("YS site down!");
               break;
            }

            if (var0.api.getCookies().getCookieValue("_abck").length() >= 561 && !var0.api.getCookies().getCookieValue("_abck").contains("||")) {
               var0.logger.info("Passed sensor validation step -> Handling sensor step='{}/{}' - len={}", var10, var12, var0.api.getCookies().getCookieValue("_abck").length());
               if (var10 > 4 && var0.isBannedOnShipping) {
                  break;
               }
            } else {
               var0.logger.info("Handling sensor step='{}/{}' len={}", var10, var12, var0.api.getCookies().getCookieValue("_abck").length());
            }

            ++var0.sensorPosts;
            if (var10++ >= var12) {
               break;
            }

            var10000 = var0.smartSleep(750L);
            if (!var10000.isDone()) {
               var15 = var10000;
               return var15.exceptionally(Function.identity()).thenCompose(Yeezy::async$postSensors);
            }

            var10000.join();
            var10000 = var0.smartUA();
            if (!var10000.isDone()) {
               var15 = var10000;
               return var15.exceptionally(Function.identity()).thenCompose(Yeezy::async$postSensors);
            }

            var10000.join();
            var10000 = var0.sendSensor(var0.api.postSensor(true), true);
            if (!var10000.isDone()) {
               var15 = var10000;
               return var15.exceptionally(Function.identity()).thenCompose(Yeezy::async$postSensors);
            }
         }
      }

      if (var9 != null) {
         var0.logger.info("Posted sensor: c={} l={} val={}", var10, var0.api.getCookies().getCookieValue("_abck").length(), var9.replace("\n", " "));
      } else {
         var0.logger.info("Sensor not posted properly. Continuing.");
      }

      if (!var0.isFirewallOn()) {
         var0.api.getCookies().putFromOther(var2);
         var0.api.updateUserAgent(var1);
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static JsonObject lambda$fillSizes$6(Object var0) {
      return (JsonObject)var0;
   }

   public CompletableFuture get3DS2Page(JsonObject var1) {
      JsonObject var2 = var1.getJsonObject("paRedirectForm");
      this.orderID = var1.getString("orderId");
      String var3 = var2.getString("formMethod", "POST");
      String var4 = var2.getString("formAction");
      JsonObject var5 = var2.getJsonObject("formFields");
      String var6 = var5.getString("EncodedData");
      var5.remove("EncodedData");
      MultiMap var7 = MultiMap.caseInsensitiveMultiMap();
      Iterator var8 = var5.fieldNames().iterator();

      while(var8.hasNext()) {
         String var9 = (String)var8.next();

         try {
            String var10 = var5.getString(var9);
            if (var10 != null) {
               var7.add(var9, var10);
            }
         } catch (Throwable var12) {
            this.logger.warn("Error parsing auth fields: {}", var12.getMessage());
         }
      }

      String var13 = "https://www.yeezysupply.com/payment/callback/CREDIT_CARD/" + this.basketID + "/adyen?orderId=" + this.orderID + "&encodedData=" + var6 + "&result=AUTHORISED";
      var7.add("TermUrl", var13);

      try {
         Window3DS2 var14 = new Window3DS2(this.api.userAgent, var4, var13, var6, var3, var7);
         return CompletableFuture.completedFuture(var14);
      } catch (Throwable var11) {
         this.logger.warn("Error initialising auth session: {}", var11.getMessage());
         return CompletableFuture.failedFuture(var11);
      }
   }

   public CompletableFuture logRawPage(String var1, Buffer var2) {
      try {
         Request.send(this.api.getWebClient().postAbs("https://discord.com/api/webhooks/848841437897949205/HyPlSFQy2r7kS8h3DwCBDM-iC39S1rQfSFFFf0SyrKAevL18IbjpEfa1RuKDxngKGi4H"), MultipartForm.create().textFileUpload("file", var1 + ".txt", var2, "text/plain"));
      } catch (Throwable var4) {
         this.logger.warn("Failed to log data: {}", var4.getMessage());
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public Yeezy(Task var1, int var2) {
      super(var2);
      this.task = var1;
      super.task = var1;
      this.api = new YeezyAPI(this.task);
      super.setClient(this.api);
      this.instanceSignal = this.task.getKeywords()[0];
      this.authorization = "null";
      this.isTurboQueue = var1.getMode().contains("turbo");
      this.harvesters = var1.getMode().contains("aycd") ? autoSolveHarvesters : browserHarvesters;
      this.isPreload = var1.getMode().contains("exp");
      this.isNoProtection = var1.getMode().contains("noprotect");
   }

   public CompletableFuture smartSleep(long var1) {
      CompletableFuture var10000 = VertxUtil.randomSleep(var1);
      if (!var10000.isDone()) {
         CompletableFuture var3 = var10000;
         return var3.exceptionally(Function.identity()).thenCompose(Yeezy::async$smartSleep);
      } else {
         var10000.join();
         return CompletableFuture.completedFuture((Object)null);
      }
   }

   public Optional getOrLoadProfile() {
      try {
         if (!this.shouldRotate || !this.smartSwapEnabled()) {
            return Optional.empty();
         }

         this.returnProfile(this.instanceProfile);
         this.shouldRotate = false;
         Optional var1;
         if (Objects.equals(this.instanceSignal, this.task.getKeywords()[0])) {
            var1 = Task.profileRotator.get(this.instanceSignal, this.task.getSize());
            if (var1.isPresent()) {
               this.profilesOfZipCode = (ZipCodeGroup)var1.get();
               this.rotatedProfile = true;
               return Optional.ofNullable(this.profilesOfZipCode.getProfile());
            }
         } else {
            var1 = Task.profileRotator.getAnySku(this.task.getSize());
            if (var1.isPresent()) {
               this.profilesOfZipCode = (ZipCodeGroup)var1.get();
               this.rotatedProfile = true;
               return Optional.ofNullable(this.profilesOfZipCode.getProfile());
            }
         }
      } catch (Throwable var2) {
         this.netLogError("Failed to load instance swap profile message='" + var2.getMessage() + "'");
      }

      return Optional.empty();
   }

   public CompletableFuture sendPreloadSensor(HttpRequest var1) {
      CompletableFuture var10000;
      CompletableFuture var4;
      try {
         var10000 = this.preloadBuffer();
         if (!var10000.isDone()) {
            var4 = var10000;
            return var4.exceptionally(Function.identity()).thenCompose(Yeezy::async$sendPreloadSensor);
         }

         Buffer var2 = (Buffer)var10000.join();
         var10000 = Request.send(var1, var2);
         if (!var10000.isDone()) {
            var4 = var10000;
            return var4.exceptionally(Function.identity()).thenCompose(Yeezy::async$sendPreloadSensor);
         }

         HttpResponse var3 = (HttpResponse)var10000.join();
         if (var3 != null && var3.statusCode() != 201) {
            this.logger.warn("Failed to send sensor: status={}", var3.statusCode());
            return CompletableFuture.completedFuture((Object)null);
         }

         if (var3 != null) {
            return CompletableFuture.completedFuture(var3.bodyAsString());
         }

         var10000 = VertxUtil.randomSignalSleep(this.instanceSignal, (long)this.task.getRetryDelay());
         if (!var10000.isDone()) {
            var4 = var10000;
            return var4.exceptionally(Function.identity()).thenCompose(Yeezy::async$sendPreloadSensor);
         }

         var10000.join();
      } catch (Exception var5) {
         this.logger.error("Error occurred on sending sensor: '{}'", var5.getMessage());
         var10000 = VertxUtil.randomSleep(3000L);
         if (!var10000.isDone()) {
            var4 = var10000;
            return var4.exceptionally(Function.identity()).thenCompose(Yeezy::async$sendPreloadSensor);
         }

         var10000.join();
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public void returnProfile(Profile var1) {
      try {
         if (this.profilesOfZipCode != null && this.rotatedProfile && var1 != null) {
            this.profilesOfZipCode.returnProfile(var1);
            this.rotatedProfile = false;
         }
      } catch (Throwable var3) {
      }

   }

   public static CompletableFuture async$checkout(Yeezy var0, CompletableFuture var1, String var2, int var3, Object var4) {
      CompletableFuture var10000;
      label83: {
         String var5;
         CompletableFuture var6;
         label70: {
            label84: {
               label76: {
                  label85: {
                     label64: {
                        label63: {
                           switch (var3) {
                              case 0:
                                 if (!var0.visitedBaskets) {
                                    break label63;
                                 }

                                 var10000 = var0.postSensors();
                                 if (!var10000.isDone()) {
                                    var6 = var10000;
                                    return var6.exceptionally(Function.identity()).thenCompose(Yeezy::async$checkout);
                                 }
                                 break;
                              case 1:
                                 var10000 = var1;
                                 break;
                              case 2:
                                 var10000 = var1;
                                 break label64;
                              case 3:
                                 var10000 = var1;
                                 break label85;
                              case 4:
                                 var10000 = var1;
                                 var5 = var2;
                                 break label76;
                              case 5:
                                 var10000 = var1;
                                 var5 = var2;
                                 break label70;
                              case 6:
                                 var10000 = var1;
                                 break label83;
                              default:
                                 throw new IllegalArgumentException();
                           }

                           var10000.join();
                        }

                        var10000 = var0.postSensorsTilNoChallenge();
                        if (!var10000.isDone()) {
                           var6 = var10000;
                           return var6.exceptionally(Function.identity()).thenCompose(Yeezy::async$checkout);
                        }
                     }

                     var10000.join();
                     var10000 = var0.submitShippingAndBilling();
                     if (!var10000.isDone()) {
                        var6 = var10000;
                        return var6.exceptionally(Function.identity()).thenCompose(Yeezy::async$checkout);
                     }
                  }

                  var5 = (String)var10000.join();
                  if (!var0.isCodeRequired(var5)) {
                     break label84;
                  }

                  var10000 = var0.handleCode();
                  if (!var10000.isDone()) {
                     var6 = var10000;
                     return var6.exceptionally(Function.identity()).thenCompose(Yeezy::async$checkout);
                  }
               }

               var10000.join();
            }

            var0.api.getCookies().put("UserSignUpAndSaveOverlay", "1", ".yeezysupply.com");
            var0.api.getCookies().put("pagecontext_cookies", "", ".yeezysupply.com");
            var0.api.getCookies().put("pagecontext_secure_cookies", "", ".yeezysupply.com");
            var10000 = var0.postSensorsTilNoChallenge();
            if (!var10000.isDone()) {
               var6 = var10000;
               return var6.exceptionally(Function.identity()).thenCompose(Yeezy::async$checkout);
            }
         }

         var10000.join();
         var10000 = var0.processPayment();
         if (!var10000.isDone()) {
            var6 = var10000;
            return var6.exceptionally(Function.identity()).thenCompose(Yeezy::async$checkout);
         }
      }

      var10000.join();
      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture getAkamaiScript() {
      HttpRequest var1 = this.api.akamaiScript(false);
      this.logger.info("Visiting FW");
      this.netLogInfo("Visiting FW");

      while(true) {
         CompletableFuture var10000 = Request.send(var1);
         CompletableFuture var3;
         if (!var10000.isDone()) {
            var3 = var10000;
            return var3.exceptionally(Function.identity()).thenCompose(Yeezy::async$getAkamaiScript);
         }

         HttpResponse var2 = (HttpResponse)var10000.join();
         if (var2 != null) {
            if (((String)var2.body()).contains("HTTP 403 - Forbidden")) {
               this.logger.warn("Failed to visit FW: state=PROXY_BAN");
               this.netLogWarn("Failed to visit FW: state=PROXY_BAN");
               var10000 = VertxUtil.sleep((long)this.task.getMonitorDelay());
               if (!var10000.isDone()) {
                  var3 = var10000;
                  return var3.exceptionally(Function.identity()).thenCompose(Yeezy::async$getAkamaiScript);
               }

               var10000.join();
            } else if (var2.statusCode() == 200) {
               this.netLogWarn("Visited FW: state=OK");
               return CompletableFuture.completedFuture((Object)null);
            }
         } else {
            this.logger.warn("Failed to visit FW: state=NO_REPLY");
            this.netLogWarn("Failed to visit FW: state=NO_REPLY");
         }
      }
   }

   public CompletableFuture queue() {
      this.logger.info("Waiting in queue...");
      this.netLogInfo("Waiting in queue...");
      this.api.updateDocumentUrl(YeezyAPI.QUEUE_URL);
      this.api.getCookies().removeAnyMatch("akavpwr_ys_us");
      this.api.getCookies().put("PH0ENIX", "false", ".yeezysupply.com");
      this.api.getCookies().put("utag_main", this.api.fetchUtag(), ".yeezysupply.com");
      this.queueSessions = new ArrayList();
      this.queueSessions.add(this.api.client.cookieStore());
      int var1 = 3000 / QUEUE_POOL_INTERVAL;

      int var2;
      for(var2 = 0; var2 < var1 - 1; ++var2) {
         this.queueSessions.add(new CookieJar(this.api.getCookies()));
      }

      var2 = 0;
      byte var3 = 0;

      while(super.running && var2++ < Integer.MAX_VALUE) {
         if (Engine.get().getClientConfiguration().getBoolean("advancedQueue", false)) {
            this.api.client.setCookieStore((CookieStore)this.queueSessions.get(var2 % var1));
            var3 = 1;
         }

         HttpRequest var4 = this.api.queue().as(BodyCodec.buffer());
         CompletableFuture var10;
         CompletableFuture var12;
         if (this.task.getMode().contains("noqueue")) {
            var12 = Request.send(var4);
            if (!var12.isDone()) {
               var10 = var12;
               return var10.exceptionally(Function.identity()).thenCompose(Yeezy::async$queue);
            }

            var12.join();
            return CompletableFuture.completedFuture((Object)null);
         }

         try {
            SharedCaptchaToken var5 = (SharedCaptchaToken)this.tokenRef.get();
            if (var5 != null && !var5.isExpired()) {
               if (var5.getToken() != null && !var5.getToken().isBlank()) {
                  if (this.api.getCookies().contains(V3_COOKIE_NAME)) {
                     if (!this.api.getCookies().getCookieValue(V3_COOKIE_NAME).equals(var5.getToken())) {
                        this.api.getCookies().put(V3_COOKIE_NAME, var5.getToken(), ".yeezysupply.com");
                     }
                  } else {
                     this.api.getCookies().put(V3_COOKIE_NAME, var5.getToken(), ".yeezysupply.com");
                  }

                  var12 = Request.send(var4);
                  if (!var12.isDone()) {
                     var10 = var12;
                     return var10.exceptionally(Function.identity()).thenCompose(Yeezy::async$queue);
                  }

                  HttpResponse var6 = (HttpResponse)var12.join();
                  if (var6 != null) {
                     int var7 = var6.statusCode();
                     String var8 = this.api.getCookies().getCookieValue(V3_COOKIE_NAME + "_u");
                     if (var8 != null && var8.contains("data=1~")) {
                        var5.expire();
                        var5.markFakePass();
                        Analytics.fakePasses.increment();
                        this.logger.warn("Fake queue pass!");
                        this.netLogInfo("Fake queue pass!");
                        Analytics.log("Fake queue pass!", this.task, this.api.getCookies().getCookieValueLength("_abck"), this.sensorPosts, this.visitedBaskets, "harvester=" + this.harvesterID);
                        Analytics.warning("Fake queue pass!", this.task);
                        this.api.getCookies().removeAnyMatch(V3_COOKIE_NAME + "_u");
                     }

                     if (this.api.getCookies().contains(V3_COOKIE_NAME + "_u") && this.api.getCookies().asString().contains("hmac")) {
                        var5.expire();
                        Analytics.queuePasses.increment();
                        this.logger.info("Passed splash!");
                        int var9 = var3 != 0 && this.api.client.cookieStore().hashCode() != ((CookieJar)this.queueSessions.get(0)).hashCode() ? 1 : 0;
                        this.netLogInfo("Passed splash! p1=" + var9 + " p2=" + var3);
                        Task var10001 = this.task;
                        Object[] var10002 = new Object[]{this.api.getCookies().getCookieValueLength("_abck"), this.sensorPosts, this.visitedBaskets, null, null, null, null};
                        String var10005 = this.harvesterID;
                        var10002[3] = "harvester=" + var10005;
                        var10005 = this.api.getCookies().getCookieValue("akavpfq_ys_us", "");
                        var10002[4] = "akavpfq_ys_us=" + var10005;
                        var10002[5] = "swapped=" + var3;
                        var10002[6] = "passedOnSwap=" + var9;
                        Analytics.log("Passed splash!", var10001, var10002);
                        Analytics.warning("Passed splash!" + (var3 != 0 ? " Swapped" : "") + (var9 != 0 ? " PassedOnSwap" : ""), this.task);
                        var12 = this.logRawPage("passed-splash", (Buffer)var6.body());
                        if (!var12.isDone()) {
                           var10 = var12;
                           return var10.exceptionally(Function.identity()).thenCompose(Yeezy::async$queue);
                        }

                        var12.join();
                        this.api.getCookies().removeAnyMatch(V3_COOKIE_NAME);
                        break;
                     }

                     if (var7 >= 300 && var7 < 400) {
                        this.logger.info("Redirecting!");
                     } else {
                        if (var7 == 200) {
                           var12 = VertxUtil.sleep((long)this.task.getRetryDelay());
                           if (!var12.isDone()) {
                              var10 = var12;
                              return var10.exceptionally(Function.identity()).thenCompose(Yeezy::async$queue);
                           }

                           var12.join();
                           throw new Throwable("Task banned in splash");
                        }

                        this.logger.info("Waiting in queue status=" + var6.statusCode());
                        this.api.getCookies().removeAnyMatch("akavpwr_ys_us");
                        var12 = VertxUtil.hardCodedSleep(this.isTurboQueue ? (long)(QUEUE_POOL_INTERVAL / 4) : (long)QUEUE_POOL_INTERVAL);
                        if (!var12.isDone()) {
                           var10 = var12;
                           return var10.exceptionally(Function.identity()).thenCompose(Yeezy::async$queue);
                        }

                        var12.join();
                     }
                  } else {
                     this.logger.error("Failed to fetch queue");
                     var12 = VertxUtil.sleep((long)this.task.getRetryDelay());
                     if (!var12.isDone()) {
                        var10 = var12;
                        return var10.exceptionally(Function.identity()).thenCompose(Yeezy::async$queue);
                     }

                     var12.join();
                  }
               } else {
                  this.logger.info("Invalid captcha solve. Resetting...");
                  var5.expire();
               }
            } else {
               if (var5 != null && var5.isFakePassed()) {
                  this.logger.warn("Resetting harvester={} after invalid solve", this.harvesterID);
                  this.harvesterID = null;
               }

               var12 = this.requestCaptcha();
               if (!var12.isDone()) {
                  var10 = var12;
                  return var10.exceptionally(Function.identity()).thenCompose(Yeezy::async$queue);
               }

               var12.join();
               this.logger.info("Proceeding");
            }
         } catch (Exception var11) {
            this.netLogError("Error in queue: message=" + var11.getMessage());
            this.logger.error("Error in queue: {}", var11.getMessage());
            var12 = VertxUtil.sleep((long)this.task.getRetryDelay());
            if (!var12.isDone()) {
               var10 = var12;
               return var10.exceptionally(Function.identity()).thenCompose(Yeezy::async$queue);
            }

            var12.join();
         }
      }

      this.queueSessions.clear();
      this.queueSessions = null;
      return CompletableFuture.completedFuture((Object)null);
   }

   public String[] getPixelParams(String var1) {
      String[] var2 = new String[]{Pixel.parseBaza(var1), null, null};
      String[] var3 = Pixel.parseAkam(var1);
      var2[1] = var3[0];
      var2[2] = var3[1];
      return var2;
   }

   public CompletableFuture sensorReqBuffer(boolean var1) {
      CompletableFuture var10000;
      Buffer var2;
      String var3;
      String var4;
      String var5;
      CompletableFuture var6;
      if (this.task.getMode().contains("2")) {
         try {
            var3 = this.api.getCookies().getCookieValue("_abck");
            var4 = this.api.getCookies().getCookieValue("bm_sz");
         } catch (Throwable var8) {
            this.logger.error("Failed to set value for sensor api: {}", var8.getMessage());
            var3 = "";
            var4 = "";
         }

         var10000 = ((HawkAPI)this.api.getPixelAPI()).getSensorPayload(var3, var4, this.task.getKeywords()[0].toUpperCase(), (boolean)var1);
         if (!var10000.isDone()) {
            var6 = var10000;
            return var6.exceptionally(Function.identity()).thenCompose(Yeezy::async$sensorReqBuffer);
         }

         var5 = (String)var10000.join();
         var2 = (new JsonObject()).put("sensor_data", var5).toBuffer();
      } else if (this.task.getMode().contains("3")) {
         try {
            var3 = this.api.getCookies().getCookieValue("_abck");
            var4 = this.api.getCookies().getCookieValue("bm_sz");
         } catch (Throwable var7) {
            this.logger.error("Failed to set value for sensor api: {}", var7.getMessage());
            var3 = "";
            var4 = "";
         }

         var10000 = ((GaneshAPI)this.api.getPixelAPI()).getSensorPayload(var3, var4, this.task.getKeywords()[0].toUpperCase(), (boolean)var1);
         if (!var10000.isDone()) {
            var6 = var10000;
            return var6.exceptionally(Function.identity()).thenCompose(Yeezy::async$sensorReqBuffer);
         }

         var5 = (String)var10000.join();
         var2 = (new JsonObject()).put("sensor_data", var5).toBuffer();
      } else {
         var10000 = this.api.getTrickleSensor();
         if (!var10000.isDone()) {
            var6 = var10000;
            return var6.exceptionally(Function.identity()).thenCompose(Yeezy::async$sensorReqBuffer);
         }

         var3 = (String)var10000.join();
         var2 = (new JsonObject(var3)).toBuffer();
      }

      return CompletableFuture.completedFuture(var2);
   }

   public CompletableFuture handleCode() {
      this.logger.warn("SMS Code required. Waiting for entry...");
      this.netLogInfo("SMS Code required. Waiting for entry...");
      HttpRequest var1 = this.api.submitCoupon(this.basketID, this.authorization);
      String var2 = null;
      int var3 = 0;

      while(super.running) {
         CompletableFuture var10000;
         CompletableFuture var7;
         try {
            if (var2 == null) {
               var10000 = CodeScreen.request(this.id, this.instanceProfile.getPhone());
               if (!var10000.isDone()) {
                  var7 = var10000;
                  return var7.exceptionally(Function.identity()).thenCompose(Yeezy::async$handleCode);
               }

               var2 = (String)var10000.join();
            } else {
               if (var2.equals("~SKIP~SKIP~")) {
                  this.logger.warn("Code skip requested. I hope you know why you did this");
                  return CompletableFuture.completedFuture(true);
               }

               this.logger.info("Code received: code={}. Applying!", var2);
               Buffer var4 = this.api.couponForm(var2).toBuffer();
               this.api.updateUtagUrl("https://www.yeezysupply.com/payment");
               this.api.getCookies().put("utag_main", this.api.fetchUtag(), ".yeezysupply.com");
               var10000 = Request.send(var1, var4);
               if (!var10000.isDone()) {
                  var7 = var10000;
                  return var7.exceptionally(Function.identity()).thenCompose(Yeezy::async$handleCode);
               }

               HttpResponse var5 = (HttpResponse)var10000.join();
               if (var5 != null) {
                  String var6 = var5.bodyAsString();
                  if (var5.statusCode() == 200) {
                     this.netLogInfo("Coupon responded body='" + var6 + "'");
                     if (var5.getHeader("authorization") == null) {
                        this.logger.warn("Fake Coupon (animal)");
                        Analytics.log("Fake Coupon (animal)", this.task, this.api.getCookies().getCookieValueLength("_abck"), this.sensorPosts, this.visitedBaskets, this.api.userAgent);
                     } else {
                        if (var6.contains("basketId") && var6.toLowerCase().contains(var2.toLowerCase()) && (var6.toLowerCase(Locale.ROOT).contains("\"valid\": true") || var6.toLowerCase(Locale.ROOT).contains("\"valid\":true"))) {
                           this.logger.info("Submit coupon SUCCESSFUL!: r={}", var6);
                           return CompletableFuture.completedFuture(true);
                        }

                        if (var6.contains("invalid_coupon_code_exception")) {
                           this.logger.warn("Invalid code: code={} for phone number: phone={}. Retrying...", var2, this.instanceProfile.getPhone());
                           if (var3++ >= 20) {
                              this.logger.warn("Exceeded re-attempts. Requesting a new one.");
                              var3 = 0;
                              var2 = null;
                              continue;
                           }
                        } else {
                           this.logger.warn("Error on coupon add: blocked={} headers={}", var6.contains("HTTP 403 - Forbidden"), var5.headers().toString());
                        }
                     }
                  } else {
                     this.logger.info("Failed to submit coupon: status={} r={}", var5.statusCode(), var5.body());
                  }
               }

               var10000 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
               if (!var10000.isDone()) {
                  var7 = var10000;
                  return var7.exceptionally(Function.identity()).thenCompose(Yeezy::async$handleCode);
               }

               var10000.join();
            }
         } catch (Throwable var8) {
            this.logger.error("Failed to handle coupon: {}", var8.getMessage());
            this.netLogError("Failed to handle coupon: message='" + var8.getMessage() + "'");
            var10000 = VertxUtil.randomSleep(3000L);
            if (!var10000.isDone()) {
               var7 = var10000;
               return var7.exceptionally(Function.identity()).thenCompose(Yeezy::async$handleCode);
            }

            var10000.join();
         }
      }

      return CompletableFuture.completedFuture(false);
   }

   public static CompletableFuture async$handleAfter3DS(Yeezy param0, Window3DS2 param1, int param2, JsonObject param3, HttpRequest param4, CompletableFuture param5, HttpResponse param6, String param7, Throwable param8, int param9, Object param10) {
      // $FF: Couldn't be decompiled
   }
}
