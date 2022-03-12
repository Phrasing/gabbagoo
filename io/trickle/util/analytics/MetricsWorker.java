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
 *  io.vertx.core.buffer.Buffer
 *  io.vertx.core.file.AsyncFile
 *  io.vertx.core.file.FileSystem
 *  io.vertx.core.file.OpenOptions
 *  io.vertx.core.json.JsonArray
 *  io.vertx.ext.web.client.WebClient
 */
package io.trickle.util.analytics;

import io.trickle.util.Storage;
import io.trickle.util.analytics.Analytics;
import io.trickle.util.analytics.webhook.Metric;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.FileSystem;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.client.WebClient;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MetricsWorker
extends AbstractVerticle {
    public Promise<Void> continuation;
    public long POLL_TIME = 10000L;
    public long timerId;
    public Iterator<Metric[]> iterator;
    public long NEXT_FETCH = 2500L;
    public WebClient client;
    public List<Metric[]> messageQueue;
    public int failSend = 0;
    public String checkoutsFile = Storage.CONFIG_PATH + "/checkouts-" + System.currentTimeMillis() + ".csv";

    public void handle(Metric[] metricArray) {
        CompositeFuture.all((Future)this.sendToApi(this.arrayToJson(metricArray)), (Future)this.handleCheckouts(metricArray)).onComplete(this::lambda$handle$3);
    }

    public void fireNext() {
        this.vertx.setTimer(2500L, this::lambda$fireNext$11);
    }

    public JsonArray arrayToJson(Metric[] metricArray) {
        if (metricArray.length == 0) {
            return new JsonArray();
        }
        JsonArray jsonArray = new JsonArray();
        Metric[] metricArray2 = metricArray;
        int n = metricArray2.length;
        int n2 = 0;
        while (n2 < n) {
            Metric metric = metricArray2[n2];
            if (metric != null) {
                jsonArray.add((Object)metric.asApiForm());
            }
            ++n2;
        }
        return jsonArray;
    }

    public void lambda$scheduleNextLater$2(AsyncResult asyncResult) {
        this.vertx.setTimer(10000L, this::lambda$scheduleNextLater$1);
    }

    public void lambda$scheduleNextLater$1(Long l) {
        this.process();
    }

    public Future sendToApi(JsonArray jsonArray) {
        Promise promise = Promise.promise();
        this.client.postAbs("https://metrics-api.z.redapt.io/api?type=checkouts&action=many").sendJson((Object)jsonArray).onComplete(arg_0 -> this.lambda$sendToApi$5(promise, jsonArray, arg_0));
        return promise.future();
    }

    public void trampoline() {
        if (!this.iterator.hasNext()) {
            this.iterator = null;
            this.continuation.tryComplete();
            return;
        }
        Metric[] metricArray = this.iterator.next();
        if (metricArray != null && metricArray.length > 0) {
            this.handle(metricArray);
        }
        this.iterator.remove();
    }

    public void stop() {
        this.vertx.cancelTimer(this.timerId);
        if (this.client != null) {
            this.client.close();
        }
        super.stop();
    }

    public void lambda$handleCheckouts$9(Metric[] metricArray, Promise promise, FileSystem fileSystem, Boolean bl) {
        if (bl.booleanValue()) {
            this.writeToCheckouts(metricArray, false).onComplete(arg_0 -> MetricsWorker.lambda$handleCheckouts$6(promise, arg_0));
            return;
        }
        fileSystem.createFile(this.checkoutsFile).onSuccess(arg_0 -> this.lambda$handleCheckouts$8(metricArray, promise, arg_0)).onFailure(arg_0 -> ((Promise)promise).tryFail(arg_0));
    }

    public void lambda$sendToApi$4(JsonArray jsonArray, Long l) {
        this.sendToApi(jsonArray);
    }

    public static void lambda$writeToCheckouts$10(boolean bl, Metric[] metricArray, Promise promise, AsyncResult asyncResult) {
        if (!asyncResult.succeeded()) return;
        AsyncFile asyncFile = (AsyncFile)asyncResult.result();
        if (bl) {
            asyncFile.write((Object)Buffer.buffer((String)"site,product,sku,size/qty,delays,mode,proxy,email,account,ordernumber\n"));
        }
        Metric[] metricArray2 = metricArray;
        int n = metricArray2.length;
        int n2 = 0;
        while (true) {
            if (n2 >= n) {
                promise.tryComplete();
                return;
            }
            Metric metric = metricArray2[n2];
            try {
                if (metric != null) {
                    asyncFile.write((Object)Buffer.buffer((String)metric.asCsvEntry()));
                }
            }
            catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            ++n2;
        }
    }

    public Future writeToCheckouts(Metric[] metricArray, boolean bl) {
        Promise promise = Promise.promise();
        this.vertx.fileSystem().open(this.checkoutsFile, new OpenOptions().setAppend(true), arg_0 -> MetricsWorker.lambda$writeToCheckouts$10(bl, metricArray, promise, arg_0));
        return promise.future();
    }

    public void lambda$start$0(Long l) {
        this.process();
    }

    public static void lambda$handleCheckouts$7(Promise promise, Void void_) {
        promise.tryComplete();
    }

    public void lambda$fireNext$11(Long l) {
        this.trampoline();
    }

    public Future handleCheckouts(Metric[] metricArray) {
        Promise promise = Promise.promise();
        FileSystem fileSystem = this.vertx.fileSystem();
        fileSystem.exists(this.checkoutsFile).onSuccess(arg_0 -> this.lambda$handleCheckouts$9(metricArray, promise, fileSystem, arg_0));
        return promise.future();
    }

    public void lambda$sendToApi$5(Promise promise, JsonArray jsonArray, AsyncResult asyncResult) {
        if (!asyncResult.succeeded() && this.failSend < 5) {
            ++this.failSend;
            this.vertx.setTimer(3000L, arg_0 -> this.lambda$sendToApi$4(jsonArray, arg_0));
            return;
        }
        this.failSend = 0;
        promise.tryComplete();
    }

    public MetricsWorker() {
        this.messageQueue = new ArrayList<Metric[]>();
    }

    public void start() {
        super.start();
        this.client = WebClient.create((Vertx)this.vertx);
        this.timerId = this.vertx.setTimer(10000L, this::lambda$start$0);
    }

    public static void lambda$handleCheckouts$6(Promise promise, AsyncResult asyncResult) {
        promise.tryComplete();
    }

    public void scheduleNextLater() {
        this.continuation = Promise.promise();
        this.continuation.future().onComplete(this::lambda$scheduleNextLater$2);
    }

    public void lambda$handleCheckouts$8(Metric[] metricArray, Promise promise, Void void_) {
        this.writeToCheckouts(metricArray, true).onSuccess(arg_0 -> MetricsWorker.lambda$handleCheckouts$7(promise, arg_0)).onFailure(arg_0 -> ((Promise)promise).tryFail(arg_0));
    }

    public void lambda$handle$3(AsyncResult asyncResult) {
        this.fireNext();
    }

    public void writeToQueue(ConcurrentLinkedQueue concurrentLinkedQueue) {
        if (concurrentLinkedQueue.isEmpty()) return;
        int n = 0;
        while (!concurrentLinkedQueue.isEmpty()) {
            if (n >= 10) return;
            ++n;
            Metric[] metricArray = new Metric[10];
            for (int i = 0; i < 10; ++i) {
                Metric metric = (Metric)concurrentLinkedQueue.poll();
                if (metric == null) continue;
                metricArray[i] = metric;
            }
            this.messageQueue.add(metricArray);
        }
    }

    public void process() {
        this.scheduleNextLater();
        this.writeToQueue(Analytics.metricsQueue);
        this.iterator = this.messageQueue.iterator();
        this.trampoline();
    }
}

