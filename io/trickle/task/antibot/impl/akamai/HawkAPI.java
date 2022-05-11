package io.trickle.task.antibot.impl.akamai;

import io.trickle.core.VertxSingleton;
import io.trickle.task.antibot.impl.akamai.pixel.Pixel;
import io.trickle.util.Utils;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import java.io.PrintStream;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.regex.Pattern;

public class HawkAPI implements Pixel {
   public String useragent = null;
   public static Pattern UA_MAJOR_VERSION_PATTERN = Pattern.compile("Chrome/([0-9]*)");
   public static String[] api_ua = new String[]{"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.74 Safari/537.36", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.82 Safari/537.36", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.51 Safari/537.36", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.51 Safari/537.36"};

   public HttpRequest hawkUserAgent() {
      return this.client().getAbs("https://ak01-eu.hwkapi.com/akamai/ua").putHeader("X-Api-Key", "ce3cabed-e10f-43b1-a2a2-8d2a9c2a212f").putHeader("X-Sec", "new").timeout(50000L).as(BodyCodec.string());
   }

   public CompletableFuture getPixelReqString(String var1, String var2, String var3) {
      HttpRequest var4 = this.hawkPixel();
      Buffer var5 = this.hawkPixelForm(var1, var3);
      int var6 = 0;

      while(var6++ <= 1000) {
         try {
            CompletableFuture var12 = Request.send(var4, var5);
            CompletableFuture var9;
            if (!var12.isDone()) {
               var9 = var12;
               return var9.exceptionally(Function.identity()).thenCompose(HawkAPI::async$getPixelReqString);
            }

            HttpResponse var7 = (HttpResponse)var12.join();
            if (var7 != null) {
               String var8 = var7.bodyAsString();
               return CompletableFuture.completedFuture(var8);
            }

            var12 = VertxUtil.randomSleep(10000L);
            if (!var12.isDone()) {
               var9 = var12;
               return var9.exceptionally(Function.identity()).thenCompose(HawkAPI::async$getPixelReqString);
            }

            var12.join();
         } catch (Exception var10) {
            if (!var10.getMessage().toLowerCase().contains("timeout")) {
               PrintStream var11 = System.out;
               String var10001 = var10.getMessage();
               var11.println("API[H] err: " + var10001.replace("ak01-eu.hwkapi.com", "remote"));
            }
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$getSensorPayload$1(HawkAPI param0, String param1, String param2, String param3, int param4, HttpRequest param5, JsonObject param6, int param7, CompletableFuture param8, HttpResponse param9, int param10, Object param11) {
      // $FF: Couldn't be decompiled
   }

   public void setUseragent(String var1) {
      this.useragent = var1;
   }

   public CompletableFuture getSensorPayload(String var1) {
      HttpRequest var2 = this.hawkSensor();
      JsonObject var3 = this.hawkSensorForm(var1);
      int var4 = 0;

      while(var4++ <= 1000) {
         try {
            CompletableFuture var9 = Request.send(var2, var3);
            CompletableFuture var7;
            if (!var9.isDone()) {
               var7 = var9;
               return var7.exceptionally(Function.identity()).thenCompose(HawkAPI::async$getSensorPayload);
            }

            HttpResponse var5 = (HttpResponse)var9.join();
            if (var5 != null) {
               String var6 = var5.bodyAsString("UTF-8").split("\\*")[0];
               return CompletableFuture.completedFuture(var6);
            }

            var9 = VertxUtil.randomSleep(10000L);
            if (!var9.isDone()) {
               var7 = var9;
               return var7.exceptionally(Function.identity()).thenCompose(HawkAPI::async$getSensorPayload);
            }

            var9.join();
         } catch (Exception var8) {
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$updateUserAgent(HawkAPI param0, int param1, CompletableFuture param2, String param3, int param4, Object param5) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture updateUserAgent() {
      int var1 = 0;

      while(var1++ <= 100) {
         try {
            CompletableFuture var5 = Request.executeTillOk(this.hawkUserAgent(), 5000);
            CompletableFuture var3;
            if (!var5.isDone()) {
               var3 = var5;
               return var3.exceptionally(Function.identity()).thenCompose(HawkAPI::async$updateUserAgent);
            }

            String var2 = (String)var5.join();
            if (Integer.parseInt((String)Objects.requireNonNull(Utils.quickParseFirst(var2, UA_MAJOR_VERSION_PATTERN))) >= 98) {
               this.useragent = var2;
               break;
            }

            var5 = VertxUtil.sleep(300L);
            if (!var5.isDone()) {
               var3 = var5;
               return var3.exceptionally(Function.identity()).thenCompose(HawkAPI::async$updateUserAgent);
            }

            var5.join();
         } catch (Throwable var4) {
         }
      }

      return CompletableFuture.completedFuture(this.useragent);
   }

   public CompletableFuture getPixelReqForm(String var1, String var2, String var3) {
      return CompletableFuture.failedFuture(new Exception("Unsupported method"));
   }

   public JsonObject hawkSensorForm(String var1, String var2, String var3, boolean var4) {
      return (new JsonObject()).put("site", "https://www.yeezysupply.com/products/" + var3).put("abck", var1).put("bm_sz", var2).put("type", "sensor").put("events", var4 ? (ThreadLocalRandom.current().nextInt(5) == 4 ? "1,1" : "1,0") : "1,0").put("user_agent", this.useragent);
   }

   public Buffer hawkPixelForm(String var1, String var2) {
      return (new JsonObject()).put("user_agent", this.useragent).put("script_id", var1).put("script_secret", var2).toBuffer();
   }

   public HttpRequest hawkPixel() {
      return this.client().postAbs("https://ak01-eu.hwkapi.com/akamai/pixel").putHeader("X-Api-Key", "ce3cabed-e10f-43b1-a2a2-8d2a9c2a212f").putHeader("X-Sec", "high").timeout(50000L).as(BodyCodec.buffer());
   }

   public CompletableFuture getSensorPayload(String var1, String var2, String var3, boolean var4) {
      HttpRequest var5 = this.hawkSensor();
      JsonObject var6 = this.hawkSensorForm(var1, var2, var3, (boolean)var4);
      int var7 = 0;

      while(var7++ <= 1000) {
         try {
            CompletableFuture var12 = Request.send(var5, var6);
            CompletableFuture var10;
            if (!var12.isDone()) {
               var10 = var12;
               return var10.exceptionally(Function.identity()).thenCompose(HawkAPI::async$getSensorPayload$1);
            }

            HttpResponse var8 = (HttpResponse)var12.join();
            if (var8 != null) {
               String var9 = var8.bodyAsString("UTF-8").split("\\*\\*\\*")[0];
               return CompletableFuture.completedFuture(var9);
            }

            var12 = VertxUtil.randomSleep(10000L);
            if (!var12.isDone()) {
               var10 = var12;
               return var10.exceptionally(Function.identity()).thenCompose(HawkAPI::async$getSensorPayload$1);
            }

            var12.join();
         } catch (Exception var11) {
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public HttpRequest hawkSensor() {
      return this.client().postAbs("https://ak01-eu.hwkapi.com/akamai/generate").putHeader("X-Api-Key", "ce3cabed-e10f-43b1-a2a2-8d2a9c2a212f").putHeader("X-Sec", "high").timeout(50000L).as(BodyCodec.buffer());
   }

   public static CompletableFuture async$getSensorPayload(HawkAPI param0, String param1, HttpRequest param2, JsonObject param3, int param4, CompletableFuture param5, HttpResponse param6, int param7, Object param8) {
      // $FF: Couldn't be decompiled
   }

   public String getUseragent() {
      return this.useragent;
   }

   public WebClient client() {
      return VertxSingleton.INSTANCE.getLocalClient().getClient();
   }

   public JsonObject hawkSensorForm(String var1) {
      return (new JsonObject()).put("site", "https://www.bestbuy.com/").put("abck", var1).put("type", "sensor").put("events", "1,1").put("user_agent", this.useragent);
   }

   public static CompletableFuture async$getPixelReqString(HawkAPI param0, String param1, String param2, String param3, HttpRequest param4, Buffer param5, int param6, CompletableFuture param7, HttpResponse param8, int param9, Object param10) {
      // $FF: Couldn't be decompiled
   }
}
