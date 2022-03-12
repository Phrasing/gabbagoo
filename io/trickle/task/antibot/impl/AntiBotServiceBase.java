/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
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
    public CompletableFuture<Boolean> initFuture;
    public T value;
    public Logger logger;
    public RealClient client;

    public Object getValue() {
        return this.value;
    }

    public AntiBotServiceBase(TaskActor taskActor, ClientType clientType, Controller controller) {
        this.logger = taskActor.getLogger();
        this.client = RealClientFactory.buildProxied(taskActor.getVertx(), clientType, controller);
        this.initFuture = new ContextCompletableFuture();
    }

    public void lambda$start$0(Boolean bl, Throwable throwable) {
        if (bl == null) return;
        this.initFuture.complete(bl);
    }

    public void start() {
        super.start();
        this.initialize().whenComplete(this::lambda$start$0);
    }

    public void stop() {
        try {
            super.stop();
            return;
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public void restartClient(RealClient realClient) {
        try {
            RealClient realClient2 = RealClientFactory.fromOther(Vertx.currentContext().owner(), realClient, this.client.type());
            this.client.close();
            this.client = realClient2;
            return;
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public AntiBotServiceBase(TaskActor taskActor, ClientType clientType) {
        this.logger = taskActor.getLogger();
        this.client = RealClientFactory.fromOther(taskActor.getVertx(), taskActor.getClient().getWebClient(), clientType);
        this.initFuture = new ContextCompletableFuture();
    }

    public AntiBotServiceBase(TaskActor taskActor) {
        this.logger = taskActor.getLogger();
        this.client = taskActor.getClient().getWebClient();
        this.initFuture = new ContextCompletableFuture();
    }
}

