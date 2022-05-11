package io.trickle.util.analytics;

import io.trickle.util.Storage;
import io.trickle.util.analytics.webhook.Metric;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.FileSystem;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.client.WebClient;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MetricsWorker extends AbstractVerticle {
   public List messageQueue;
   public Iterator iterator;
   public long POLL_TIME = 10000L;
   public WebClient client;
   public long NEXT_FETCH = 2500L;
   public int failSend;
   public String checkoutsFile;
   public long timerId;
   public Promise continuation;

   public void lambda$handleCheckouts$9(Metric[] var1, Promise var2, FileSystem var3, Boolean var4) {
      if (var4) {
         this.writeToCheckouts(var1, false).onComplete(MetricsWorker::lambda$handleCheckouts$6);
      } else {
         Future var10000 = var3.createFile(this.checkoutsFile).onSuccess(this::lambda$handleCheckouts$8);
         Objects.requireNonNull(var2);
         var10000.onFailure(var2::tryFail);
      }

   }

   public void lambda$fireNext$11(Long var1) {
      this.trampoline();
   }

   public void lambda$sendToApi$5(Promise var1, JsonArray var2, AsyncResult var3) {
      if (!var3.succeeded() && this.failSend < 5) {
         ++this.failSend;
         super.vertx.setTimer(3000L, this::lambda$sendToApi$4);
      } else {
         this.failSend = 0;
         var1.tryComplete();
      }

   }

   public void stop() {
      super.vertx.cancelTimer(this.timerId);
      if (this.client != null) {
         this.client.close();
      }

      super.stop();
   }

   public MetricsWorker() {
      this.POLL_TIME = 10000L;
      this.NEXT_FETCH = 2500L;
      this.failSend = 0;
      String var10001 = Storage.CONFIG_PATH;
      this.checkoutsFile = var10001 + "/checkouts-" + System.currentTimeMillis() + ".csv";
      this.messageQueue = new ArrayList();
   }

   public void start() {
      super.start();
      this.client = WebClient.create(super.vertx);
      this.timerId = super.vertx.setTimer(10000L, this::lambda$start$0);
   }

   public static void lambda$handleCheckouts$6(Promise var0, AsyncResult var1) {
      var0.tryComplete();
   }

   public void lambda$handle$3(AsyncResult var1) {
      this.fireNext();
   }

   public void lambda$handleCheckouts$8(Metric[] var1, Promise var2, Void var3) {
      Future var10000 = this.writeToCheckouts(var1, true).onSuccess(MetricsWorker::lambda$handleCheckouts$7);
      Objects.requireNonNull(var2);
      var10000.onFailure(var2::tryFail);
   }

   public void process() {
      this.scheduleNextLater();
      this.writeToQueue(Analytics.metricsQueue);
      this.iterator = this.messageQueue.iterator();
      this.trampoline();
   }

   public void fireNext() {
      super.vertx.setTimer(2500L, this::lambda$fireNext$11);
   }

   public static void lambda$writeToCheckouts$10(boolean var0, Metric[] var1, Promise var2, AsyncResult var3) {
      if (var3.succeeded()) {
         AsyncFile var4 = (AsyncFile)var3.result();
         if (var0) {
            var4.write(Buffer.buffer("site,product,sku,size/qty,delays,mode,proxy,email,account,ordernumber\n"));
         }

         Metric[] var5 = var1;
         int var6 = var1.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            Metric var8 = var5[var7];

            try {
               if (var8 != null) {
                  var4.write(Buffer.buffer(var8.asCsvEntry()));
               }
            } catch (Throwable var10) {
               var10.printStackTrace();
            }
         }

         var2.tryComplete();
      }

   }

   public void trampoline() {
      if (this.iterator.hasNext()) {
         Metric[] var1 = (Metric[])this.iterator.next();
         if (var1 != null && var1.length > 0) {
            this.handle(var1);
         }

         this.iterator.remove();
      } else {
         this.iterator = null;
         this.continuation.tryComplete();
      }

   }

   public static void lambda$handleCheckouts$7(Promise var0, Void var1) {
      var0.tryComplete();
   }

   public void scheduleNextLater() {
      this.continuation = Promise.promise();
      this.continuation.future().onComplete(this::lambda$scheduleNextLater$2);
   }

   public Future writeToCheckouts(Metric[] var1, boolean var2) {
      Promise var3 = Promise.promise();
      super.vertx.fileSystem().open(this.checkoutsFile, (new OpenOptions()).setAppend(true), MetricsWorker::lambda$writeToCheckouts$10);
      return var3.future();
   }

   public Future handleCheckouts(Metric[] var1) {
      Promise var2 = Promise.promise();
      FileSystem var3 = super.vertx.fileSystem();
      var3.exists(this.checkoutsFile).onSuccess(this::lambda$handleCheckouts$9);
      return var2.future();
   }

   public void writeToQueue(ConcurrentLinkedQueue var1) {
      if (!var1.isEmpty()) {
         int var2 = 0;

         while(!var1.isEmpty() && var2 < 10) {
            ++var2;
            Metric[] var3 = new Metric[10];

            for(int var4 = 0; var4 < 10; ++var4) {
               Metric var5 = (Metric)var1.poll();
               if (var5 != null) {
                  var3[var4] = var5;
               }
            }

            this.messageQueue.add(var3);
         }
      }

   }

   public void lambda$sendToApi$4(JsonArray var1, Long var2) {
      this.sendToApi(var1);
   }

   public Future sendToApi(JsonArray var1) {
      Promise var2 = Promise.promise();
      this.client.postAbs("https://metrics-api.z.redapt.io/api?type=checkouts&action=many").sendJson(var1).onComplete(this::lambda$sendToApi$5);
      return var2.future();
   }

   public void lambda$scheduleNextLater$1(Long var1) {
      this.process();
   }

   public JsonArray arrayToJson(Metric[] var1) {
      if (var1.length == 0) {
         return new JsonArray();
      } else {
         JsonArray var2 = new JsonArray();
         Metric[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Metric var6 = var3[var5];
            if (var6 != null) {
               var2.add(var6.asApiForm());
            }
         }

         return var2;
      }
   }

   public void lambda$scheduleNextLater$2(AsyncResult var1) {
      super.vertx.setTimer(10000L, this::lambda$scheduleNextLater$1);
   }

   public void handle(Metric[] var1) {
      CompositeFuture.all(this.sendToApi(this.arrayToJson(var1)), this.handleCheckouts(var1)).onComplete(this::lambda$handle$3);
   }

   public void lambda$start$0(Long var1) {
      this.process();
   }
}
