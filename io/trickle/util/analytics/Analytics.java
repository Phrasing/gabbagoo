/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.buffer.Buffer
 *  io.vertx.core.json.JsonArray
 *  io.vertx.core.json.JsonObject
 */
package io.trickle.util.analytics;

import io.trickle.core.Engine;
import io.trickle.task.Task;
import io.trickle.util.analytics.EmbedContainer;
import io.trickle.util.analytics.webhook.Metric;
import io.trickle.util.analytics.webhook.WebhookUtils;
import io.trickle.webclient.CookieJar;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.time.Instant;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Analytics {
    public static AtomicInteger fails;
    public static Pattern ORDER_NUMBER_PATTERN;
    public static Pattern IMAGE_URL_PATTERN;
    public static ConcurrentLinkedDeque<EmbedContainer> embedQueue;
    public static AtomicInteger carts;
    public static AtomicInteger success;
    public static Pattern PRODUCT_TITLE_PATTERN;
    public static ConcurrentLinkedQueue<Metric> metricsQueue;

    public static void emit(boolean bl, String string, Task task, JsonObject jsonObject, String string2) {
        EmbedContainer embedContainer = new EmbedContainer(bl, WebhookUtils.buildEmbed(bl, task, string, jsonObject, string2));
        if (bl) {
            success.incrementAndGet();
            embedQueue.addFirst(embedContainer);
            metricsQueue.add(Metric.create(task, jsonObject, string2));
            return;
        }
        if (string.contains("items are no longer available")) {
            return;
        }
        fails.incrementAndGet();
        embedQueue.add(embedContainer);
    }

    public static String parseOrderId(String string) {
        Matcher matcher = ORDER_NUMBER_PATTERN.matcher(string);
        if (!matcher.find()) return null;
        return matcher.group(1);
    }

    public static void success(Task task, String string, String string2) {
        Analytics.emit(true, null, task, Analytics.createCheckoutJsonShopify(string), string2);
    }

    public static void warningLogged(String string, Task task, Buffer buffer) {
        Analytics.warning(string, task);
    }

    public static String parseImage(String string) {
        Matcher matcher = IMAGE_URL_PATTERN.matcher(string);
        if (!matcher.find()) return null;
        return matcher.group(1);
    }

    public static String parseTitle(String string) {
        Matcher matcher = PRODUCT_TITLE_PATTERN.matcher(string);
        if (!matcher.find()) return null;
        return matcher.group(1);
    }

    public static void success(Task task, CookieJar cookieJar, String string) {
        Analytics.emit(true, null, task, Analytics.exportVT(cookieJar), string);
    }

    public static void failure(String string, Task task, String string2, String string3) {
        Analytics.emit(false, string, task, Analytics.createCheckoutJsonShopify(string2), string3);
    }

    public static void log(String string, Task task, Object ... objectArray) {
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.put("taskHash", (Object)task.hashCode());
            jsonObject.put("site", (Object)task.getSite());
            jsonObject.put("sku", (Object)task.getKeywords()[0]);
            jsonObject.put("size", (Object)task.getSize());
            jsonObject.put("mode", (Object)task.getMode());
            jsonObject.put("monitorDelay", (Object)task.getMonitorDelay());
            jsonObject.put("retryDelay", (Object)task.getRetryDelay());
            jsonObject.put("time", (Object)Instant.now().toString());
            JsonObject jsonObject2 = new JsonObject();
            jsonObject2.put("message", (Object)string);
            jsonObject2.put("meta", (Object)jsonObject);
            if (objectArray != null && objectArray.length > 0) {
                JsonArray jsonArray = new JsonArray();
                for (Object object : objectArray) {
                    jsonArray.add(object);
                }
                jsonObject2.put("extras", (Object)jsonArray);
            }
            Engine.get().getClient().socketLog(6, jsonObject2.toBuffer());
            return;
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public static JsonObject exportVT(CookieJar cookieJar) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("name", (Object)"vt");
        jsonObject.put("value", (Object)cookieJar.getCookieValue("vt"));
        jsonObject.put("domain", (Object)".bestbuy.com");
        return jsonObject;
    }

    public static void warning(String string, Task task) {
        EmbedContainer embedContainer = new EmbedContainer(WebhookUtils.buildBasicEmbed(task, string));
        embedQueue.add(embedContainer);
    }

    public static void failure(String string, Task task, JsonObject jsonObject, String string2) {
        Analytics.emit(false, string, task, jsonObject, string2);
    }

    public static void success(Task task, JsonObject jsonObject, String string) {
        Analytics.emit(true, null, task, jsonObject, string);
    }

    static {
        success = new AtomicInteger(0);
        carts = new AtomicInteger(0);
        fails = new AtomicInteger(0);
        embedQueue = new ConcurrentLinkedDeque();
        metricsQueue = new ConcurrentLinkedQueue();
        PRODUCT_TITLE_PATTERN = Pattern.compile("\"og:title\" content=\"(.*?)\" />");
        IMAGE_URL_PATTERN = Pattern.compile("\"og:image\" content=\"(.*?)\" />");
        ORDER_NUMBER_PATTERN = Pattern.compile("<p class=\"notice__text\">(.*?)</p>");
    }

    public static JsonObject createCheckoutJsonShopify(String string) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("image", (Object)Analytics.parseImage(string));
        jsonObject.put("title", (Object)Analytics.parseTitle(string));
        jsonObject.put("orderId", (Object)Analytics.parseOrderId(string));
        return jsonObject;
    }
}

