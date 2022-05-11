package io.trickle.util.analytics;

import io.trickle.core.Engine;
import io.trickle.task.Task;
import io.trickle.util.analytics.webhook.Metric;
import io.trickle.util.analytics.webhook.WebhookUtils;
import io.trickle.webclient.CookieJar;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.time.Instant;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.LongAdder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Analytics {
   public static Pattern PRODUCT_TITLE_PATTERN = Pattern.compile("\"og:title\" content=\"(.*?)\" />");
   public static ConcurrentLinkedDeque embedQueue = new ConcurrentLinkedDeque();
   public static LongAdder fakePasses = new LongAdder();
   public static Pattern ORDER_NUMBER_PATTERN = Pattern.compile("<p class=\"notice__text\">(.*?)</p>");
   public static LongAdder carts = new LongAdder();
   public static ConcurrentLinkedQueue metricsQueue = new ConcurrentLinkedQueue();
   public static LongAdder queuePasses = new LongAdder();
   public static LongAdder success = new LongAdder();
   public static Pattern IMAGE_URL_PATTERN = Pattern.compile("\"og:image\" content=\"(.*?)\" />");
   public static LongAdder fails = new LongAdder();

   public static JsonObject exportVT(CookieJar var0) {
      JsonObject var1 = new JsonObject();
      var1.put("name", "vt");
      var1.put("value", var0.getCookieValue("vt"));
      var1.put("domain", ".bestbuy.com");
      return var1;
   }

   public static String parseTitle(String var0) {
      Matcher var1 = PRODUCT_TITLE_PATTERN.matcher(var0);
      return var1.find() ? var1.group(1) : null;
   }

   public static void failure(String var0, Task var1, String var2, String var3) {
      emit(false, var0, var1, createCheckoutJsonShopify(var2), var3);
   }

   public static void emit(boolean var0, String var1, Task var2, JsonObject var3, String var4) {
      EmbedContainer var5 = new EmbedContainer(var0, WebhookUtils.buildEmbed(var0, var2, var1, var3, var4));
      if (var0) {
         success.increment();
         embedQueue.addFirst(var5);
         metricsQueue.add(Metric.create(var2, var3, var4));
      } else if (!var1.contains("items are no longer available")) {
         fails.increment();
         embedQueue.add(var5);
      }

   }

   public static void success(Task var0, CookieJar var1, String var2) {
      emit(true, (String)null, var0, exportVT(var1), var2);
   }

   public static void warning(String var0, Task var1) {
      EmbedContainer var2 = new EmbedContainer(WebhookUtils.buildBasicEmbed(var1, var0));
      embedQueue.add(var2);
   }

   public static void failure(String var0, Task var1, JsonObject var2, String var3) {
      emit(false, var0, var1, var2, var3);
   }

   public static void warningLogged(String var0, Task var1, Buffer var2) {
      warning(var0, var1);
   }

   public static void success(Task var0, JsonObject var1, String var2) {
      emit(true, (String)null, var0, var1, var2);
   }

   public static String parseOrderId(String var0) {
      Matcher var1 = ORDER_NUMBER_PATTERN.matcher(var0);
      return var1.find() ? var1.group(1) : null;
   }

   public static void log(String var0, Task var1, Object... var2) {
      try {
         JsonObject var3 = new JsonObject();
         var3.put("taskHash", var1.hashCode());
         var3.put("site", var1.getSite());
         var3.put("sku", var1.getKeywords()[0]);
         var3.put("size", var1.getSize());
         var3.put("mode", var1.getMode());
         var3.put("monitorDelay", var1.getMonitorDelay());
         var3.put("retryDelay", var1.getRetryDelay());
         var3.put("time", Instant.now().toString());
         JsonObject var4 = new JsonObject();
         var4.put("message", var0);
         var4.put("meta", var3);
         if (var2 != null && var2.length > 0) {
            JsonArray var5 = new JsonArray();
            Object[] var6 = var2;
            int var7 = var2.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               Object var9 = var6[var8];
               var5.add(var9);
            }

            var4.put("extras", var5);
         }

         Engine.get().getClient().socketLog(6, (Buffer)var4.toBuffer());
      } catch (Throwable var10) {
      }

   }

   public static String parseImage(String var0) {
      Matcher var1 = IMAGE_URL_PATTERN.matcher(var0);
      return var1.find() ? var1.group(1) : null;
   }

   public static void success(Task var0, String var1, String var2) {
      emit(true, (String)null, var0, createCheckoutJsonShopify(var1), var2);
   }

   public static JsonObject createCheckoutJsonShopify(String var0) {
      JsonObject var1 = new JsonObject();
      var1.put("image", parseImage(var0));
      var1.put("title", parseTitle(var0));
      var1.put("orderId", parseOrderId(var0));
      return var1;
   }
}
