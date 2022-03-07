/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.AbstractVerticle
 *  io.vertx.core.AsyncResult
 *  io.vertx.core.CompositeFuture
 *  io.vertx.core.Future
 *  io.vertx.core.Promise
 *  io.vertx.core.Vertx
 *  io.vertx.core.json.JsonObject
 *  io.vertx.ext.web.client.HttpResponse
 *  io.vertx.ext.web.client.WebClient
 */
package io.trickle.util.analytics;

import io.trickle.util.Storage;
import io.trickle.util.analytics.Analytics;
import io.trickle.util.analytics.EmbedContainer;
import io.trickle.util.analytics.webhook.WebhookUtils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

public class WebhookWorker
extends AbstractVerticle {
    public long timerId;
    public long NEXT_FETCH = 500L;
    public Iterator<Map.Entry<Integer, JsonObject>> iterator;
    public WebClient client;
    public long LOOP_TIME = 10000L;
    public List<Map.Entry<Integer, JsonObject>> messageQueue = new ArrayList<Map.Entry<Integer, JsonObject>>();
    public Promise<Void> continuation;

    public void writeToQueue(ConcurrentLinkedDeque concurrentLinkedDeque) {
        if (concurrentLinkedDeque.isEmpty()) return;
        int n = 0;
        while (!concurrentLinkedDeque.isEmpty()) {
            if (n >= 3) return;
            ++n;
            EmbedContainer embedContainer = (EmbedContainer)concurrentLinkedDeque.poll();
            if (embedContainer == null) continue;
            JsonObject jsonObject = WebhookUtils.buildWebhook(embedContainer.webhook);
            if (embedContainer.isMeta) {
                this.messageQueue.add(Map.entry(3, jsonObject));
                continue;
            }
            this.messageQueue.add(Map.entry(embedContainer.isSuccess ? 1 : 0, jsonObject));
        }
    }

    public void lambda$scheduleNextLater$2(AsyncResult asyncResult) {
        this.vertx.setTimer(10000L, this::lambda$scheduleNextLater$1);
    }

    public void lambda$start$0(Long l) {
        this.process();
    }

    public static void lambda$sendWebhook$4(Promise promise, AsyncResult asyncResult) {
        if (asyncResult.succeeded()) {
            promise.tryComplete();
            return;
        }
        promise.tryFail(asyncResult.cause());
    }

    public void fireNext() {
        this.vertx.setTimer(500L, this::lambda$fireNext$7);
    }

    public void lambda$scheduleNextLater$1(Long l) {
        this.process();
    }

    public void lambda$sendWebhook$6(String string, Map.Entry entry, Promise promise, AsyncResult asyncResult) {
        if (!asyncResult.succeeded()) return;
        HttpResponse httpResponse = (HttpResponse)asyncResult.result();
        if (httpResponse.statusCode() != 429) {
            promise.tryComplete();
            return;
        }
        try {
            JsonObject jsonObject = httpResponse.bodyAsJsonObject();
            if (!jsonObject.containsKey("retry_after")) return;
            int n = jsonObject.getInteger("retry_after", Integer.valueOf(1000)) + 5;
            this.vertx.setTimer((long)n, arg_0 -> this.lambda$sendWebhook$5(string, entry, promise, arg_0));
            return;
        }
        catch (Exception exception) {
            promise.tryFail((Throwable)exception);
            return;
        }
    }

    public void lambda$handle$3(AsyncResult asyncResult) {
        this.fireNext();
    }

    public void stop() {
        this.vertx.cancelTimer(this.timerId);
        if (this.client != null) {
            this.client.close();
        }
        super.stop();
    }

    public void scheduleNextLater() {
        this.continuation = Promise.promise();
        this.continuation.future().onComplete(this::lambda$scheduleNextLater$2);
    }

    public void trampoline() {
        if (!this.iterator.hasNext()) {
            this.iterator = null;
            this.continuation.tryComplete();
            return;
        }
        Map.Entry<Integer, JsonObject> entry = this.iterator.next();
        if (entry != null) {
            this.handle(entry);
        }
        this.iterator.remove();
    }

    public void handle(Map.Entry entry) {
        String string = (Integer)entry.getKey() != 3 ? ((Integer)entry.getKey() == 1 ? "https://webhooks.aycd.io/webhooks/api/v1/send/14890/aa27307c-00f8-4e74-a10f-626f63998187" : "https://webhooks.aycd.io/webhooks/api/v1/send/14892/e4919db9-f1c9-4f94-b796-71b93acfc116") : "https://webhooks.aycd.io/webhooks/api/v1/send/10414/b8c8e7d7-321c-4a80-acec-2c9c85acec8a";
        CompositeFuture compositeFuture = CompositeFuture.all((Future)this.sendWebhook(string, entry), (Future)this.sendWebhook("https://webhooks.tidalmarket.com/e55301de-9d9c-11ec-82d2-42010aa80013/e55302b0-9d9c-11ec-82d2-42010aa80013/redirect", entry), (Future)this.handleUserWebhook(entry));
        compositeFuture.onComplete(this::lambda$handle$3);
    }

    public void start() {
        super.start();
        this.client = WebClient.create((Vertx)this.vertx);
        this.timerId = this.vertx.setTimer(10000L, this::lambda$start$0);
    }

    public Future sendWebhook(String string, Map.Entry entry) {
        Promise promise = Promise.promise();
        this.client.postAbs(string).timeout(10000L).sendJson(entry.getValue()).onComplete(arg_0 -> this.lambda$sendWebhook$6(string, entry, promise, arg_0));
        return promise.future();
    }

    public void process() {
        this.scheduleNextLater();
        this.writeToQueue(Analytics.embedQueue);
        this.iterator = this.messageQueue.iterator();
        this.trampoline();
    }

    public Future handleUserWebhook(Map.Entry entry) {
        if (Storage.DISCORD_WEBHOOK.isEmpty()) return Future.succeededFuture();
        if ((Integer)entry.getKey() == 3) return Future.succeededFuture();
        String string = Storage.DISCORD_WEBHOOK;
        return this.sendWebhook(string, entry);
    }

    public void lambda$sendWebhook$5(String string, Map.Entry entry, Promise promise, Long l) {
        this.sendWebhook(string, entry).onComplete(arg_0 -> WebhookWorker.lambda$sendWebhook$4(promise, arg_0));
    }

    public void lambda$fireNext$7(Long l) {
        this.trampoline();
    }
}

