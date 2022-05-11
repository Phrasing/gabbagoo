package io.trickle.util.analytics;

import io.trickle.util.Storage;
import io.trickle.util.analytics.webhook.WebhookUtils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

public class WebhookWorker extends AbstractVerticle {
   public WebClient client;
   public Iterator iterator;
   public Promise continuation;
   public long NEXT_FETCH = 500L;
   public long timerId;
   public List messageQueue;
   public long LOOP_TIME = 10000L;

   public void lambda$sendWebhook$5(String var1, Map.Entry var2, Promise var3, Long var4) {
      this.sendWebhook(var1, var2).onComplete(WebhookWorker::lambda$sendWebhook$4);
   }

   public void process() {
      this.scheduleNextLater();
      this.writeToQueue(Analytics.embedQueue);
      this.iterator = this.messageQueue.iterator();
      this.trampoline();
   }

   public void trampoline() {
      if (this.iterator.hasNext()) {
         Map.Entry var1 = (Map.Entry)this.iterator.next();
         if (var1 != null) {
            this.handle(var1);
         }

         this.iterator.remove();
      } else {
         this.iterator = null;
         this.continuation.tryComplete();
      }

   }

   public void scheduleNextLater() {
      this.continuation = Promise.promise();
      this.continuation.future().onComplete(this::lambda$scheduleNextLater$2);
   }

   public void stop() {
      super.vertx.cancelTimer(this.timerId);
      if (this.client != null) {
         this.client.close();
      }

      super.stop();
   }

   public void lambda$scheduleNextLater$1(Long var1) {
      this.process();
   }

   public void handle(Map.Entry var1) {
      String var2;
      if ((Integer)var1.getKey() != 3) {
         var2 = (Integer)var1.getKey() == 1 ? "https://webhooks.aycd.io/webhooks/api/v1/send/14890/aa27307c-00f8-4e74-a10f-626f63998187" : "https://webhooks.aycd.io/webhooks/api/v1/send/14892/e4919db9-f1c9-4f94-b796-71b93acfc116";
      } else {
         var2 = "https://webhooks.aycd.io/webhooks/api/v1/send/10414/b8c8e7d7-321c-4a80-acec-2c9c85acec8a";
      }

      CompositeFuture var3 = CompositeFuture.all(this.sendWebhook(var2, var1), this.sendWebhook("https://webhooks.tidalmarket.com/e55301de-9d9c-11ec-82d2-42010aa80013/e55302b0-9d9c-11ec-82d2-42010aa80013/redirect", var1), this.handleUserWebhook(var1));
      var3.onComplete(this::lambda$handle$3);
   }

   public static void lambda$sendWebhook$4(Promise var0, AsyncResult var1) {
      if (var1.succeeded()) {
         var0.tryComplete();
      } else {
         var0.tryFail(var1.cause());
      }

   }

   public Future handleUserWebhook(Map.Entry var1) {
      if (!Storage.DISCORD_WEBHOOK.isEmpty() && (Integer)var1.getKey() != 3) {
         String var2 = Storage.DISCORD_WEBHOOK;
         return this.sendWebhook(var2, var1);
      } else {
         return Future.succeededFuture();
      }
   }

   public void lambda$scheduleNextLater$2(AsyncResult var1) {
      super.vertx.setTimer(10000L, this::lambda$scheduleNextLater$1);
   }

   public void lambda$sendWebhook$6(String var1, Map.Entry var2, Promise var3, AsyncResult var4) {
      if (var4.succeeded()) {
         HttpResponse var5 = (HttpResponse)var4.result();
         if (var5.statusCode() == 429) {
            try {
               JsonObject var6 = var5.bodyAsJsonObject();
               if (var6.containsKey("retry_after")) {
                  int var7 = var6.getInteger("retry_after", 1000) + 5;
                  super.vertx.setTimer((long)var7, this::lambda$sendWebhook$5);
               }
            } catch (Exception var8) {
               var3.tryFail(var8);
            }
         } else {
            var3.tryComplete();
         }
      }

   }

   public void lambda$start$0(Long var1) {
      this.process();
   }

   public void lambda$fireNext$7(Long var1) {
      this.trampoline();
   }

   public WebhookWorker() {
      this.LOOP_TIME = 10000L;
      this.NEXT_FETCH = 500L;
      this.messageQueue = new ArrayList();
   }

   public Future sendWebhook(String var1, Map.Entry var2) {
      Promise var3 = Promise.promise();
      this.client.postAbs(var1).timeout(10000L).sendJson(var2.getValue()).onComplete(this::lambda$sendWebhook$6);
      return var3.future();
   }

   public void start() {
      super.start();
      this.client = WebClient.create(super.vertx);
      this.timerId = super.vertx.setTimer(10000L, this::lambda$start$0);
   }

   public void lambda$handle$3(AsyncResult var1) {
      this.fireNext();
   }

   public void fireNext() {
      super.vertx.setTimer(500L, this::lambda$fireNext$7);
   }

   public void writeToQueue(ConcurrentLinkedDeque var1) {
      if (!var1.isEmpty()) {
         int var2 = 0;

         while(!var1.isEmpty() && var2 < 3) {
            ++var2;
            EmbedContainer var3 = (EmbedContainer)var1.poll();
            if (var3 != null) {
               JsonObject var4 = WebhookUtils.buildWebhook(var3.webhook);
               if (var3.isMeta) {
                  this.messageQueue.add(Map.entry(3, var4));
               } else {
                  this.messageQueue.add(Map.entry(var3.isSuccess ? 1 : 0, var4));
               }
            }
         }
      }

   }
}
