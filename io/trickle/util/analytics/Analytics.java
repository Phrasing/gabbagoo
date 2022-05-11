/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.core.Engine
 *  io.trickle.task.Task
 *  io.trickle.util.analytics.EmbedContainer
 *  io.trickle.util.analytics.webhook.Metric
 *  io.trickle.util.analytics.webhook.WebhookUtils
 *  io.trickle.webclient.CookieJar
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
import java.util.concurrent.atomic.LongAdder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Analytics {
    public static Pattern PRODUCT_TITLE_PATTERN;
    public static ConcurrentLinkedDeque<EmbedContainer> embedQueue;
    public static LongAdder fakePasses;
    public static Pattern ORDER_NUMBER_PATTERN;
    public static LongAdder carts;
    public static ConcurrentLinkedQueue<Metric> metricsQueue;
    public static LongAdder queuePasses;
    public static LongAdder success;
    public static Pattern IMAGE_URL_PATTERN;
    public static LongAdder fails;

    public static JsonObject exportVT(CookieJar cookieJar) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("name", (Object)"vt");
        jsonObject.put("value", (Object)cookieJar.getCookieValue("vt"));
        jsonObject.put("domain", (Object)".bestbuy.com");
        return jsonObject;
    }

    public static String parseTitle(String string) {
        Matcher matcher = PRODUCT_TITLE_PATTERN.matcher(string);
        if (!matcher.find()) return null;
        return matcher.group(1);
    }

    public static void failure(String string, Task task, String string2, String string3) {
        Analytics.emit(false, string, task, Analytics.createCheckoutJsonShopify(string2), string3);
    }

    public static void emit(boolean bl, String string, Task task, JsonObject jsonObject, String string2) {
        EmbedContainer embedContainer = new EmbedContainer(bl, WebhookUtils.buildEmbed((boolean)bl, (Task)task, (String)string, (JsonObject)jsonObject, (String)string2));
        if (bl) {
            success.increment();
            embedQueue.addFirst(embedContainer);
            metricsQueue.add(Metric.create((Task)task, (JsonObject)jsonObject, (String)string2));
        } else {
            if (string.contains("items are no longer available")) return;
            fails.increment();
            embedQueue.add(embedContainer);
        }
    }

    public static void success(Task task, CookieJar cookieJar, String string) {
        Analytics.emit(true, null, task, Analytics.exportVT(cookieJar), string);
    }

    static {
        queuePasses = new LongAdder();
        fakePasses = new LongAdder();
        carts = new LongAdder();
        success = new LongAdder();
        fails = new LongAdder();
        embedQueue = new ConcurrentLinkedDeque();
        metricsQueue = new ConcurrentLinkedQueue();
        PRODUCT_TITLE_PATTERN = Pattern.compile("\"og:title\" content=\"(.*?)\" />");
        IMAGE_URL_PATTERN = Pattern.compile("\"og:image\" content=\"(.*?)\" />");
        ORDER_NUMBER_PATTERN = Pattern.compile("<p class=\"notice__text\">(.*?)</p>");
    }

    public static void warning(String string, Task task) {
        EmbedContainer embedContainer = new EmbedContainer(WebhookUtils.buildBasicEmbed((Task)task, (String)string));
        embedQueue.add(embedContainer);
    }

    public static void failure(String string, Task task, JsonObject jsonObject, String string2) {
        Analytics.emit(false, string, task, jsonObject, string2);
    }

    public static void warningLogged(String string, Task task, Buffer buffer) {
        Analytics.warning(string, task);
    }

    public static void success(Task task, JsonObject jsonObject, String string) {
        Analytics.emit(true, null, task, jsonObject, string);
    }

    public static String parseOrderId(String string) {
        Matcher matcher = ORDER_NUMBER_PATTERN.matcher(string);
        if (!matcher.find()) return null;
        return matcher.group(1);
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
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public static String parseImage(String string) {
        Matcher matcher = IMAGE_URL_PATTERN.matcher(string);
        if (!matcher.find()) return null;
        return matcher.group(1);
    }

    public static void success(Task task, String string, String string2) {
        Analytics.emit(true, null, task, Analytics.createCheckoutJsonShopify(string), string2);
    }

    public static JsonObject createCheckoutJsonShopify(String string) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("image", (Object)Analytics.parseImage(string));
        jsonObject.put("title", (Object)Analytics.parseTitle(string));
        jsonObject.put("orderId", (Object)Analytics.parseOrderId(string));
        return jsonObject;
    }
}
