/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.json.JsonArray
 *  io.vertx.core.json.JsonObject
 */
package io.trickle.util.analytics.webhook;

import io.trickle.task.Task;
import io.trickle.task.sites.Site;
import io.trickle.util.analytics.webhook.OrderDetails;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.time.Instant;
import java.util.Arrays;

public class WebhookUtils {
    public static String PUB_DECLINE = "https://discord.com/api/webhooks/822113112126914581/auCyRr4ObwUWqx2XCynGd_B_Yr6ZKmVZ_eIYI83Y_YpBZCua5Al_b9pkC3LGVrjR_Pfj";
    public static String PUB_SUCCESS = "https://discord.com/api/webhooks/822112940714098719/9IO77xtVfjoad2wWTPpz3rU1AdWqVOP1WdZ_ruRC6ysRI1OeMemxXAOgEVEeXb6NCeVb";
    public static String PUB_SPAM = "https://webhooks.aycd.io/webhooks/api/v1/send/10414/b8c8e7d7-321c-4a80-acec-2c9c85acec8a";

    public static JsonObject buildBasicEmbed(Task task, String string) {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add((Object)new JsonObject().put("name", (Object)"Product").put("value", (Object)("||" + Arrays.toString(task.getKeywords()) + "||")));
        jsonArray.add((Object)new JsonObject().put("name", (Object)"Size/Quantity").put("value", (Object)task.getSize()).put("inline", (Object)true));
        jsonArray.add((Object)new JsonObject().put("name", (Object)"Tasks").put("value", (Object)task.getTaskQuantity()).put("inline", (Object)true));
        jsonArray.add((Object)new JsonObject().put("name", (Object)"Keywords").put("value", (Object)("||" + Arrays.toString(task.getKeywords()) + "||")).put("inline", (Object)true));
        jsonArray.add((Object)new JsonObject().put("name", (Object)"Delay").put("value", (Object)("||" + task.getRetryDelay() + ":" + task.getMonitorDelay() + "||")).put("inline", (Object)true));
        jsonArray.add((Object)new JsonObject().put("name", (Object)"Mode").put("value", (Object)("||" + task.getMode() + "||")).put("inline", (Object)true));
        jsonArray.add((Object)new JsonObject().put("name", (Object)"Email").put("value", (Object)("||" + task.getProfile().getEmail() + "||")).put("inline", (Object)true));
        JsonObject jsonObject = WebhookUtils.warningEmbed(string, task.getSite());
        jsonObject.put("fields", (Object)jsonArray);
        return jsonObject;
    }

    public static JsonObject warningEmbed(String string, Site site) {
        return WebhookUtils.baseEmbed().put("title", (Object)(string + " || " + site.toString().replace("_", " "))).put("color", (Object)0xFFFF00);
    }

    public static JsonObject failureEmbed(Site site) {
        return WebhookUtils.baseEmbed().put("title", (Object)("Trickle Failed Checkout || " + site.toString().replace("_", " "))).put("color", (Object)14943015);
    }

    public static JsonObject baseEmbed() {
        return new JsonObject().put("timestamp", (Object)Instant.now().toString()).put("footer", (Object)new JsonObject().put("text", (Object)String.format("Trickle v%d.%d.%d", 1, 0, 238)).put("icon_url", (Object)WebhookUtils.getIcon()));
    }

    public static String lambda$buildEmbed$0(Task task) {
        return Arrays.toString(task.getKeywords());
    }

    public static String getIcon() {
        return "https://cdn.shopify.com/s/files/1/2919/7736/files/vzFQZT_E_400x400_f51d7ec0-d8d6-4286-8c4d-5c43dcb7c4b1.jpg?v=1616019005";
    }

    public static JsonObject successEmbed(Site site) {
        return WebhookUtils.baseEmbed().put("title", (Object)("Trickle Successful Checkout || " + site.toString().replace("_", " "))).put("color", (Object)1439489);
    }

    public static JsonObject buildWebhook(JsonObject ... jsonObjectArray) {
        JsonArray jsonArray = new JsonArray();
        JsonObject[] jsonObjectArray2 = jsonObjectArray;
        int n = jsonObjectArray2.length;
        int n2 = 0;
        while (n2 < n) {
            JsonObject jsonObject = jsonObjectArray2[n2];
            if (jsonObject != null && !jsonObject.isEmpty()) {
                jsonArray.add((Object)jsonObject);
            }
            ++n2;
        }
        return new JsonObject().put("username", (Object)"Trickle").put("avatar_url", (Object)WebhookUtils.getIcon()).put("embeds", (Object)jsonArray);
    }

    public static JsonObject buildEmbed(boolean bl, Task task, String string, JsonObject jsonObject, String string2) {
        JsonObject jsonObject2;
        JsonArray jsonArray = new JsonArray();
        OrderDetails orderDetails = (OrderDetails)OrderDetails.getDetailsParser(task.getSite()).apply(jsonObject);
        try {
            jsonArray.add((Object)new JsonObject().put("name", (Object)"Product").put("value", (Object)("||" + orderDetails.productName().orElseGet(() -> WebhookUtils.lambda$buildEmbed$0(task)) + "||")));
        }
        catch (Throwable throwable) {
            jsonArray.add((Object)new JsonObject().put("name", (Object)"Product").put("value", (Object)("||" + Arrays.toString(task.getKeywords()) + "||")));
        }
        if (orderDetails.cookie != null) {
            jsonArray.add((Object)new JsonObject().put("name", (Object)"Cookie").put("value", (Object)("```" + orderDetails.cookie + "```")).put("inline", (Object)true));
        } else {
            jsonArray.add((Object)new JsonObject().put("name", (Object)"Size/Quantity").put("value", (Object)task.getSize()).put("inline", (Object)true));
            jsonArray.add((Object)new JsonObject().put("name", (Object)"Tasks").put("value", (Object)task.getTaskQuantity()).put("inline", (Object)true));
            jsonArray.add((Object)new JsonObject().put("name", (Object)"Keywords").put("value", (Object)("||" + Arrays.toString(task.getKeywords()) + "||")).put("inline", (Object)true));
            jsonArray.add((Object)new JsonObject().put("name", (Object)"Delay").put("value", (Object)("||" + task.getRetryDelay() + ":" + task.getMonitorDelay() + "||")).put("inline", (Object)true));
            jsonArray.add((Object)new JsonObject().put("name", (Object)"Email").put("value", (Object)("||" + task.getProfile().getEmail() + "||")).put("inline", (Object)true));
            jsonArray.add((Object)new JsonObject().put("name", (Object)"Proxy").put("value", (Object)("||" + string2 + "||")).put("inline", (Object)true));
        }
        jsonArray.add((Object)new JsonObject().put("name", (Object)"Mode").put("value", (Object)("||" + task.getMode() + "||")).put("inline", (Object)true));
        if (bl) {
            jsonObject2 = WebhookUtils.successEmbed(task.getSite());
            if (orderDetails.orderNumber != null) {
                jsonArray.add((Object)new JsonObject().put("name", (Object)"Order #").put("value", (Object)("||" + orderDetails.orderNumber + "||")).put("inline", (Object)true));
            }
        } else {
            jsonObject2 = WebhookUtils.failureEmbed(task.getSite());
            jsonArray.add((Object)new JsonObject().put("name", (Object)"Reason").put("value", (Object)string));
        }
        jsonObject2.put("fields", (Object)jsonArray);
        try {
            String string3 = orderDetails.imageURI;
            if (string3 == null) return jsonObject2;
            jsonObject2.put("thumbnail", (Object)new JsonObject().put("url", (Object)string3));
            return jsonObject2;
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return jsonObject2;
    }
}

