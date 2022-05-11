/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.util.concurrent.ContextCompletableFuture
 *  io.trickle.webclient.ClientType
 *  io.trickle.webclient.CookieJar
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
    public boolean active = true;
    public AtomicReference<Http2ClientConnection> connectionRef = new AtomicReference<Object>(null);
    public ClientType type;

    public CompletableFuture windowUpdateCallback() {
        Http2ClientConnection http2ClientConnection = this.connectionRef.get();
        if (http2ClientConnection == null) return CompletableFuture.completedFuture(null);
        ContextCompletableFuture contextCompletableFuture = new ContextCompletableFuture();
        http2ClientConnection.onWindowUpdate((CompletableFuture)contextCompletableFuture);
        return contextCompletableFuture;
    }

    public boolean isActive() {
        return this.active;
    }

    public CompletableFuture headersCallback() {
        Http2ClientConnection http2ClientConnection = this.connectionRef.get();
        if (http2ClientConnection == null) return CompletableFuture.completedFuture(null);
        ContextCompletableFuture contextCompletableFuture = new ContextCompletableFuture();
        http2ClientConnection.onHeaders((CompletableFuture)contextCompletableFuture);
        return contextCompletableFuture;
    }

    public static RealClient create(WebClient webClient, ClientType clientType) {
        return RealClient.create(webClient, new CookieJar(), clientType);
    }

    public RealClient(WebClient webClient, CookieJar cookieJar, ClientType clientType) {
        super(webClient, (CookieStore)cookieJar);
        this.type = clientType;
        this.getClient().connectionHandler(this::lambda$new$0);
    }

    public ClientType type() {
        return this.type;
    }

    public CookieJar cookieStore() {
        return (CookieJar)super.cookieStore();
    }

    public CookieStore cookieStore() {
        return this.cookieStore();
    }

    public static RealClient create(WebClient webClient, CookieJar cookieJar, ClientType clientType) {
        return new RealClient(webClient, cookieJar, clientType);
    }

    public void close() {
        this.active = false;
        super.close();
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

    public void resetCookieStore() {
        this.cookieStore().clear();
    }
}
