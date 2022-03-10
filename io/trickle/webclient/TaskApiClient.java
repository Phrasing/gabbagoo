/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.net.ProxyOptions
 */
package io.trickle.webclient;

import io.trickle.core.VertxSingleton;
import io.trickle.webclient.ClientType;
import io.trickle.webclient.CookieJar;
import io.trickle.webclient.RealClient;
import io.trickle.webclient.RealClientFactory;
import io.vertx.core.net.ProxyOptions;

public abstract class TaskApiClient {
    public RealClient client;

    public CookieJar getCookies() {
        return this.client.cookieStore();
    }

    public TaskApiClient(ClientType clientType) {
        this.client = RealClientFactory.buildProxied(VertxSingleton.INSTANCE.get(), clientType);
    }

    public boolean rotateProxy() {
        if (this.client == null) return false;
        if (!this.client.isActive()) return false;
        try {
            RealClient realClient = RealClientFactory.rotateProxy(VertxSingleton.INSTANCE.get(), this.client);
            this.client.close();
            this.client = realClient;
            return true;
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return false;
    }

    public TaskApiClient() {
        this.client = RealClientFactory.buildProxied(VertxSingleton.INSTANCE.get());
    }

    public void close() {
        this.client.close();
    }

    public TaskApiClient(RealClient realClient) {
        this.client = realClient;
    }

    public String proxyString() {
        ProxyOptions proxyOptions = this.client.getOptions().getProxyOptions();
        if (proxyOptions == null) {
            return "no-proxy";
        }
        if (proxyOptions.getPassword() != null) return String.format("%s:%d:%s:%s", proxyOptions.getHost(), proxyOptions.getPort(), proxyOptions.getUsername(), proxyOptions.getPassword());
        return String.format("%s:%d", proxyOptions.getHost(), proxyOptions.getPort());
    }

    public RealClient getWebClient() {
        return this.client;
    }
}

