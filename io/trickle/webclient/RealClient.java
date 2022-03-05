/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.http.HttpConnection
 *  io.vertx.core.http.impl.Http2ClientConnection
 *  io.vertx.ext.web.client.WebClient
 *  io.vertx.ext.web.client.impl.WebClientSessionAware
 *  io.vertx.ext.web.client.spi.CookieStore
 */
package io.trickle.webclient;

import io.trickle.util.concurrent.ContextCompletableFuture;
import io.trickle.webclient.ClientType;
import io.trickle.webclient.CookieJar;
import io.vertx.core.http.HttpConnection;
import io.vertx.core.http.impl.Http2ClientConnection;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.impl.WebClientSessionAware;
import io.vertx.ext.web.client.spi.CookieStore;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public class RealClient
extends WebClientSessionAware {
    public AtomicReference<Http2ClientConnection> connectionRef = new AtomicReference<Object>(null);
    public boolean active;
    public CookieJar cookies;
    public ClientType type;

    public CompletableFuture windowUpdateCallback() {
        Http2ClientConnection http2ClientConnection = this.connectionRef.get();
        if (http2ClientConnection == null) return CompletableFuture.completedFuture(null);
        ContextCompletableFuture contextCompletableFuture = new ContextCompletableFuture();
        http2ClientConnection.onWindowUpdate((CompletableFuture)contextCompletableFuture);
        return contextCompletableFuture;
    }

    public CookieStore cookieStore() {
        return this.cookieStore();
    }

    public CompletableFuture headersCallback() {
        Http2ClientConnection http2ClientConnection = this.connectionRef.get();
        if (http2ClientConnection == null) return CompletableFuture.completedFuture(null);
        ContextCompletableFuture contextCompletableFuture = new ContextCompletableFuture();
        http2ClientConnection.onHeaders((CompletableFuture)contextCompletableFuture);
        return contextCompletableFuture;
    }

    public void lambda$new$0(HttpConnection httpConnection) {
        try {
            this.connectionRef.set((Http2ClientConnection)httpConnection);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        httpConnection.setWindowSize(httpConnection.getWindowSize() + this.type.getWindowUpdate());
    }

    public boolean isActive() {
        return this.active;
    }

    public static RealClient create(WebClient webClient, ClientType clientType) {
        return RealClient.create(webClient, new CookieJar(), clientType);
    }

    public RealClient(WebClient webClient, CookieJar cookieJar, ClientType clientType) {
        super(webClient, (CookieStore)cookieJar);
        this.cookies = cookieJar;
        this.active = true;
        this.type = clientType;
        this.getClient().connectionHandler(this::lambda$new$0);
    }

    public void close() {
        this.active = false;
        super.close();
    }

    public CookieJar cookieStore() {
        return this.cookies;
    }

    public ClientType type() {
        return this.type;
    }

    public static RealClient create(WebClient webClient, CookieJar cookieJar, ClientType clientType) {
        return new RealClient(webClient, cookieJar, clientType);
    }
}

