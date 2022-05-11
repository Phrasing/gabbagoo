package io.trickle.task.antibot.impl.akamai;

import io.trickle.core.VertxSingleton;
import io.trickle.task.antibot.impl.akamai.pixel.Pixel;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.vertx.core.MultiMap;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class GaneshAPI implements Pixel {
   public String useragent = null;
   public static String[] api_ua = new String[]{"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.75 Safari/537.36", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.88 Safari/537.36", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.88 Safari/537.36", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.75 Safari/537.36", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.60 Safari/537.36"};

   public static String decode(String var0) {
      String var1 = "56h56hERH%^£H$%H%£^YHGTERherthrtwh";
      StringBuilder var2 = new StringBuilder();

      for(int var3 = 0; var3 < var0.length(); ++var3) {
         var2.append((char)(var0.charAt(var3) ^ var1.charAt(var3 % var1.length())));
      }

      return var2.toString();
   }

   public CompletableFuture getPixelReqForm(String var1, String var2, String var3) {
      return CompletableFuture.failedFuture(new Exception("Unsupported method"));
   }

   public MultiMap getPixelForm(String var1, String var2) {
      return MultiMap.caseInsensitiveMultiMap().set("scriptVal", var1).set("key", "dwayn-hrrth56JH%^JNHRTTHtjrtj56jhrthrtwhrthr").set("mode", "PIXEL").set("ua", this.useragent).set("pixelID", var2);
   }

   public String getUseragent() {
      return this.useragent;
   }

   public WebClient client() {
      return VertxSingleton.INSTANCE.getLocalClient().getClient();
   }

   public void setUseragent(String var1) {
      this.useragent = var1;
   }

   public CompletableFuture updateUserAgent() {
      this.useragent = api_ua[ThreadLocalRandom.current().nextInt(api_ua.length)];
      return CompletableFuture.completedFuture(this.useragent);
   }

   public static CompletableFuture async$getSensorPayload(GaneshAPI param0, String param1, String param2, String param3, int param4, HttpRequest param5, MultiMap param6, int param7, CompletableFuture param8, HttpResponse param9, int param10, Object param11) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$getPixelReqString(GaneshAPI param0, String param1, String param2, String param3, HttpRequest param4, MultiMap param5, int param6, CompletableFuture param7, HttpResponse param8, int param9, Object param10) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture getSensorPayload(String var1, String var2, String var3, boolean var4) {
      HttpRequest var5 = this.ganeshAPI();
      MultiMap var6 = this.getSensorForm(var1, var2, var3, (boolean)var4);
      int var7 = 0;

      while(var7++ <= 1000) {
         try {
            CompletableFuture var12 = Request.send(var5, var6);
            CompletableFuture var10;
            if (!var12.isDone()) {
               var10 = var12;
               return var10.exceptionally(Function.identity()).thenCompose(GaneshAPI::async$getSensorPayload);
            }

            HttpResponse var8 = (HttpResponse)var12.join();
            if (var8 != null) {
               String var9 = decode(var8.bodyAsString("UTF-8"));
               return CompletableFuture.completedFuture(var9);
            }

            var12 = VertxUtil.randomSleep(10000L);
            if (!var12.isDone()) {
               var10 = var12;
               return var10.exceptionally(Function.identity()).thenCompose(GaneshAPI::async$getSensorPayload);
            }

            var12.join();
         } catch (Exception var11) {
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public HttpRequest ganeshAPI() {
      return this.client().postAbs("https://akam-b429.ganeshbot.cloud/Akamai").putHeader("accept", "*/*").putHeader("accept-encoding", "gzip, deflate, br").putHeader("content-type", "application/x-www-form-urlencoded").timeout(50000L).as(BodyCodec.buffer());
   }

   public CompletableFuture getPixelReqString(String var1, String var2, String var3) {
      HttpRequest var4 = this.ganeshAPI();
      MultiMap var5 = this.getPixelForm(var3, var1);
      int var6 = 0;

      while(var6++ <= 1000) {
         try {
            CompletableFuture var11 = Request.send(var4, var5);
            CompletableFuture var9;
            if (!var11.isDone()) {
               var9 = var11;
               return var9.exceptionally(Function.identity()).thenCompose(GaneshAPI::async$getPixelReqString);
            }

            HttpResponse var7 = (HttpResponse)var11.join();
            if (var7 != null) {
               String var8 = var7.bodyAsString().split("\\*")[0];
               return CompletableFuture.completedFuture(var8);
            }

            var11 = VertxUtil.randomSleep(10000L);
            if (!var11.isDone()) {
               var9 = var11;
               return var9.exceptionally(Function.identity()).thenCompose(GaneshAPI::async$getPixelReqString);
            }

            var11.join();
         } catch (Exception var10) {
            if (!var10.getMessage().toLowerCase().contains("ganesh")) {
               System.out.println("API[G] err: " + var10.getMessage());
            }
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public MultiMap getSensorForm(String var1, String var2, String var3, boolean var4) {
      MultiMap var5 = MultiMap.caseInsensitiveMultiMap().set("site", "https://www.yeezysupply.com/products/" + var3).set("key", "dwayn-hrrth56JH%^JNHRTTHtjrtj56jhrthrtwhrthr").set("mode", "API").set("ua", this.useragent).set("abck", var1).set("bmsz", var2);
      if (!var4) {
         var5.set("events", "false");
      }

      return var5;
   }
}
