package io.trickle.task.antibot.impl;

import io.trickle.core.Controller;
import io.trickle.core.actor.TaskActor;
import io.trickle.task.antibot.AntiBotService;
import io.trickle.util.concurrent.ContextCompletableFuture;
import io.trickle.webclient.ClientType;
import io.trickle.webclient.RealClient;
import io.trickle.webclient.RealClientFactory;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import java.util.concurrent.CompletableFuture;
import org.apache.logging.log4j.Logger;

public abstract class AntiBotServiceBase extends AbstractVerticle implements AntiBotService {
   public Object value;
   public RealClient client;
   public Logger logger;
   public CompletableFuture initFuture;

   public Object getValue() {
      return this.value;
   }

   public void restartClient(RealClient var1) {
      try {
         RealClient var2 = RealClientFactory.fromOther(Vertx.currentContext().owner(), var1, this.client.type());
         this.client.close();
         this.client = var2;
      } catch (Throwable var3) {
      }

   }

   public void lambda$start$0(Boolean var1, Throwable var2) {
      if (var1 != null) {
         this.initFuture.complete(var1);
      }

   }

   public AntiBotServiceBase(TaskActor var1) {
      this.logger = var1.getLogger();
      this.client = var1.getClient().getWebClient();
      this.initFuture = new ContextCompletableFuture();
   }

   public void start() {
      super.start();
      this.initialize().whenComplete(this::lambda$start$0);
   }

   public void stop() {
      try {
         super.stop();
      } catch (Exception var2) {
      }

   }

   public AntiBotServiceBase(TaskActor var1, ClientType var2) {
      this.logger = var1.getLogger();
      this.client = RealClientFactory.fromOther(var1.getVertx(), var1.getClient().getWebClient(), var2);
      this.initFuture = new ContextCompletableFuture();
   }

   public AntiBotServiceBase(TaskActor var1, ClientType var2, Controller var3) {
      this.logger = var1.getLogger();
      this.client = RealClientFactory.buildProxied(var1.getVertx(), var2, var3);
      this.initFuture = new ContextCompletableFuture();
   }
}
