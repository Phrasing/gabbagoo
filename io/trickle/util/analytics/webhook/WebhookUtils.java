package io.trickle.util.analytics.webhook;

import io.trickle.task.Task;
import io.trickle.task.sites.Site;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.time.Instant;
import java.util.Arrays;

public class WebhookUtils {
   public static String PUB_SUCCESS = "https://webhooks.aycd.io/webhooks/api/v1/send/14890/aa27307c-00f8-4e74-a10f-626f63998187";
   public static String PUB_SPAM = "https://webhooks.aycd.io/webhooks/api/v1/send/10414/b8c8e7d7-321c-4a80-acec-2c9c85acec8a";
   public static String PUB_TIDAL = "https://webhooks.tidalmarket.com/e55301de-9d9c-11ec-82d2-42010aa80013/e55302b0-9d9c-11ec-82d2-42010aa80013/redirect";
   public static String PUB_DECLINE = "https://webhooks.aycd.io/webhooks/api/v1/send/14892/e4919db9-f1c9-4f94-b796-71b93acfc116";

   public static JsonObject baseEmbed() {
      return (new JsonObject()).put("timestamp", Instant.now().toString()).put("footer", (new JsonObject()).put("text", String.format("Trickle v%d.%d.%d", 1, 0, 278)).put("icon_url", getIcon()));
   }

   public static JsonObject buildEmbed(boolean var0, Task var1, String var2, JsonObject var3, String var4) {
      JsonArray var5 = new JsonArray();
      OrderDetails var6 = (OrderDetails)OrderDetails.getDetailsParser(var1.getSite()).apply(var3);

      JsonObject var10001;
      try {
         var10001 = (new JsonObject()).put("name", "Product");
         String var10003 = (String)var6.productName().orElseGet(WebhookUtils::lambda$buildEmbed$0);
         var5.add(var10001.put("value", "||" + var10003 + "||"));
      } catch (Throwable var10) {
         var5.add((new JsonObject()).put("name", "Product").put("value", "||" + Arrays.toString(var1.getKeywords()) + "||"));
      }

      if (var6.cookie != null) {
         var5.add((new JsonObject()).put("name", "Cookie").put("value", "```" + var6.cookie + "```").put("inline", true));
      } else {
         var5.add((new JsonObject()).put("name", "Size/Quantity").put("value", var1.getSize()).put("inline", true));
         var5.add((new JsonObject()).put("name", "Tasks").put("value", var1.getTaskQuantity()).put("inline", true));
         var5.add((new JsonObject()).put("name", "Keywords").put("value", "||" + Arrays.toString(var1.getKeywords()) + "||").put("inline", true));
         var10001 = (new JsonObject()).put("name", "Delay");
         int var11 = var1.getRetryDelay();
         var5.add(var10001.put("value", "||" + var11 + ":" + var1.getMonitorDelay() + "||").put("inline", true));
         var5.add((new JsonObject()).put("name", "Email").put("value", "||" + var1.getProfile().getEmail() + "||").put("inline", true));
         var5.add((new JsonObject()).put("name", "Proxy").put("value", "||" + var4 + "||").put("inline", true));
      }

      var5.add((new JsonObject()).put("name", "Mode").put("value", "||" + var1.getMode() + "||").put("inline", true));
      JsonObject var7;
      if (var0) {
         var7 = successEmbed(var1.getSite());
         if (var6.orderNumber != null) {
            var5.add((new JsonObject()).put("name", "Order #").put("value", "||" + var6.orderNumber + "||").put("inline", true));
         }
      } else {
         var7 = failureEmbed(var1.getSite());
         var5.add((new JsonObject()).put("name", "Reason").put("value", var2));
      }

      var7.put("fields", var5);

      try {
         String var8 = var6.imageURI;
         if (var8 != null) {
            var7.put("thumbnail", (new JsonObject()).put("url", var8));
         }
      } catch (Throwable var9) {
      }

      return var7;
   }

   public static JsonObject buildWebhook(JsonObject... var0) {
      JsonArray var1 = new JsonArray();
      JsonObject[] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         JsonObject var5 = var2[var4];
         if (var5 != null && !var5.isEmpty()) {
            var1.add(var5);
         }
      }

      return (new JsonObject()).put("username", "Trickle").put("avatar_url", getIcon()).put("embeds", var1);
   }

   public static JsonObject buildBasicEmbed(Task var0, String var1) {
      JsonArray var2 = new JsonArray();
      var2.add((new JsonObject()).put("name", "Product").put("value", "||" + Arrays.toString(var0.getKeywords()) + "||"));
      var2.add((new JsonObject()).put("name", "Size/Quantity").put("value", var0.getSize()).put("inline", true));
      var2.add((new JsonObject()).put("name", "Tasks").put("value", var0.getTaskQuantity()).put("inline", true));
      var2.add((new JsonObject()).put("name", "Keywords").put("value", "||" + Arrays.toString(var0.getKeywords()) + "||").put("inline", true));
      JsonObject var10001 = (new JsonObject()).put("name", "Delay");
      int var10003 = var0.getRetryDelay();
      var2.add(var10001.put("value", "||" + var10003 + ":" + var0.getMonitorDelay() + "||").put("inline", true));
      var2.add((new JsonObject()).put("name", "Mode").put("value", "||" + var0.getMode() + "||").put("inline", true));
      var2.add((new JsonObject()).put("name", "Email").put("value", "||" + var0.getProfile().getEmail() + "||").put("inline", true));
      JsonObject var3 = warningEmbed(var1, var0.getSite());
      var3.put("fields", var2);
      return var3;
   }

   public static JsonObject failureEmbed(Site var0) {
      JsonObject var10000 = baseEmbed();
      String var10002 = var0.toString();
      return var10000.put("title", "Trickle Failed Checkout || " + var10002.replace("_", " ")).put("color", 14943015);
   }

   public static JsonObject successEmbed(Site var0) {
      JsonObject var10000 = baseEmbed();
      String var10002 = var0.toString();
      return var10000.put("title", "Trickle Successful Checkout || " + var10002.replace("_", " ")).put("color", 1439489);
   }

   public static String getIcon() {
      return "https://media.discordapp.net/attachments/427572016246292483/951231318514098236/vzFQZT_E_400x400_f51d7ec0-d8d6-4286-8c4d-5c43dcb7c4b1.jpg";
   }

   public static String lambda$buildEmbed$0(Task var0) {
      return Arrays.toString(var0.getKeywords());
   }

   public static JsonObject warningEmbed(String var0, Site var1) {
      return baseEmbed().put("title", var0 + " || " + var1.toString().replace("_", " ")).put("color", 16776960);
   }
}
