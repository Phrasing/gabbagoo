/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.core.Controller
 *  io.trickle.core.Engine
 *  io.trickle.core.VertxSingleton
 *  io.trickle.proxy.ProxyController
 *  io.trickle.webclient.RealClientFactory
 *  io.vertx.core.Vertx
 *  io.vertx.core.impl.utils.ConcurrentCyclicSequence
 *  io.vertx.ext.web.client.WebClient
 */
package io.trickle.webclient;

import io.trickle.core.Controller;
import io.trickle.core.Engine;
import io.trickle.core.VertxSingleton;
import io.trickle.proxy.ProxyController;
import io.trickle.webclient.RealClientFactory;
import io.vertx.core.Vertx;
import io.vertx.core.impl.utils.ConcurrentCyclicSequence;
import io.vertx.ext.web.client.WebClient;
import java.util.concurrent.CompletableFuture;

public class ProxyPoolClient {
    public ConcurrentCyclicSequence<WebClient> clientSequence = new ConcurrentCyclicSequence();

    public CompletableFuture removeBad(WebClient webClient) {
        this.clientSequence = this.clientSequence.remove((Object)webClient);
        return CompletableFuture.completedFuture(null);
    }

    public WebClient getClient() {
        while ((double)this.clientSequence.size() < Math.ceil((double)((ProxyController)Engine.get().getModule(Controller.PROXY)).loadedProxies() / Double.longBitsToDouble(4629137466983448576L))) {
            this.clientSequence = this.clientSequence.add((Object)RealClientFactory.buildRandomProxied((Vertx)VertxSingleton.INSTANCE.get()));
        }
        return (WebClient)this.clientSequence.next();
    }
}
