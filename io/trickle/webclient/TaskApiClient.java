/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.core.VertxSingleton
 *  io.trickle.webclient.ClientType
 *  io.trickle.webclient.CookieJar
 *  io.trickle.webclient.RealClient
 *  io.trickle.webclient.RealClientFactory
 *  io.vertx.core.Vertx
 *  io.vertx.core.net.ProxyOptions
 */
package io.trickle.webclient;

import io.trickle.core.VertxSingleton;
import io.trickle.webclient.ClientType;
import io.trickle.webclient.CookieJar;
import io.trickle.webclient.RealClient;
import io.trickle.webclient.RealClientFactory;
import io.vertx.core.Vertx;
import io.vertx.core.net.ProxyOptions;

public abstract class TaskApiClient {
    public RealClient client;

    public RealClient getWebClient() {
        return this.client;
    }

    public CookieJar getCookies() {
        return this.client.cookieStore();
    }

    public TaskApiClient() {
        this.client = RealClientFactory.buildProxied((Vertx)VertxSingleton.INSTANCE.get());
    }

    public String proxyStringFull() {
        ProxyOptions proxyOptions = this.client.getOptions().getProxyOptions();
        if (proxyOptions == null) {
            return "no-proxy";
        }
        if (proxyOptions.getPassword() != null) return String.format("%s:%d:%s:%s", proxyOptions.getHost(), proxyOptions.getPort(), proxyOptions.getUsername(), proxyOptions.getPassword());
        return String.format("%s:%d", proxyOptions.getHost(), proxyOptions.getPort());
    }

    public void close() {
        this.client.close();
    }

    public String proxyStringSafe() {
        ProxyOptions proxyOptions = this.client.getOptions().getProxyOptions();
        if (proxyOptions != null) return String.format("%s:%d", proxyOptions.getHost(), proxyOptions.getPort());
        return "no-proxy";
    }

    public boolean rotateProxy() {
        if (this.client == null) return false;
        if (!this.client.isActive()) return false;
        try {
            RealClient realClient = RealClientFactory.rotateProxy((Vertx)VertxSingleton.INSTANCE.get(), (RealClient)this.client);
            this.client.close();
            this.client = realClient;
            return true;
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return false;
    }

    public TaskApiClient(ClientType clientType) {
        this.client = RealClientFactory.buildProxied((Vertx)VertxSingleton.INSTANCE.get(), (ClientType)clientType);
    }

    public TaskApiClient(RealClient realClient) {
        this.client = realClient;
    }
}
