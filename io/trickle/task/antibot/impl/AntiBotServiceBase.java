/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.core.Controller
 *  io.trickle.core.actor.TaskActor
 *  io.trickle.task.antibot.AntiBotService
 *  io.trickle.util.concurrent.ContextCompletableFuture
 *  io.trickle.webclient.ClientType
 *  io.trickle.webclient.RealClient
 *  io.trickle.webclient.RealClientFactory
 *  io.vertx.core.AbstractVerticle
 *  io.vertx.core.Vertx
 *  org.apache.logging.log4j.Logger
 */
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

public abstract class AntiBotServiceBase
extends AbstractVerticle
implements AntiBotService {
    public T value;
    public RealClient client;
    public Logger logger;
    public CompletableFuture<Boolean> initFuture;

    public Object getValue() {
        return this.value;
    }

    public void restartClient(RealClient realClient) {
        try {
            RealClient realClient2 = RealClientFactory.fromOther((Vertx)Vertx.currentContext().owner(), (RealClient)realClient, (ClientType)this.client.type());
            this.client.close();
            this.client = realClient2;
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public void lambda$start$0(Boolean bl, Throwable throwable) {
        if (bl == null) return;
        this.initFuture.complete(bl);
    }

    public AntiBotServiceBase(TaskActor taskActor) {
        this.logger = taskActor.getLogger();
        this.client = taskActor.getClient().getWebClient();
        this.initFuture = new ContextCompletableFuture();
    }

    public void start() {
        super.start();
        this.initialize().whenComplete(this::lambda$start$0);
    }

    public void stop() {
        try {
            super.stop();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public AntiBotServiceBase(TaskActor taskActor, ClientType clientType) {
        this.logger = taskActor.getLogger();
        this.client = RealClientFactory.fromOther((Vertx)taskActor.getVertx(), (RealClient)taskActor.getClient().getWebClient(), (ClientType)clientType);
        this.initFuture = new ContextCompletableFuture();
    }

    public AntiBotServiceBase(TaskActor taskActor, ClientType clientType, Controller controller) {
        this.logger = taskActor.getLogger();
        this.client = RealClientFactory.buildProxied((Vertx)taskActor.getVertx(), (ClientType)clientType, (Controller)controller);
        this.initFuture = new ContextCompletableFuture();
    }
}
