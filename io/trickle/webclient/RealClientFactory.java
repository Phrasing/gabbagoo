/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.Vertx
 *  io.vertx.core.net.ProxyOptions
 *  io.vertx.ext.web.client.WebClient
 *  io.vertx.ext.web.client.WebClientOptions
 */
package io.trickle.webclient;

import io.trickle.core.Controller;
import io.trickle.core.Engine;
import io.trickle.proxy.ProxyController;
import io.trickle.webclient.ClientType;
import io.trickle.webclient.CookieJar;
import io.trickle.webclient.RealClient;
import io.vertx.core.Vertx;
import io.vertx.core.net.ProxyOptions;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import java.util.Objects;

public class RealClientFactory {
    public static WebClient createWebClient(Vertx vertx, ClientType clientType, ProxyOptions proxyOptions) {
        return WebClient.create((Vertx)vertx, (WebClientOptions)clientType.options().setProxyOptions(proxyOptions));
    }

    public static RealClient build(Vertx vertx, ClientType clientType) {
        Objects.requireNonNull(vertx);
        Objects.requireNonNull(clientType);
        return RealClient.create(RealClientFactory.createWebClient(vertx, clientType, null), clientType);
    }

    public static RealClient fromOther(Vertx vertx, RealClient realClient) {
        Objects.requireNonNull(vertx);
        Objects.requireNonNull(realClient);
        return RealClientFactory.fromOther(vertx, realClient, realClient.type());
    }

    public static WebClient createWebClient(Vertx vertx, ProxyOptions proxyOptions) {
        return RealClientFactory.createWebClient(vertx, ClientType.CHROME, proxyOptions);
    }

    public static RealClient fromOther(Vertx vertx, RealClient realClient, ClientType clientType) {
        Objects.requireNonNull(vertx);
        Objects.requireNonNull(realClient);
        Objects.requireNonNull(clientType);
        ProxyOptions proxyOptions = realClient.getOptions().getProxyOptions();
        CookieJar cookieJar = realClient.cookieStore();
        return RealClient.create(RealClientFactory.createWebClient(vertx, clientType, proxyOptions), cookieJar, clientType);
    }

    public static RealClient buildProxied(Vertx vertx, ClientType clientType, Controller controller) {
        Objects.requireNonNull(vertx);
        Objects.requireNonNull(clientType);
        ProxyOptions proxyOptions = ((ProxyController)Engine.get().getModule(controller)).getProxyCyclic().getAsVertx();
        return RealClient.create(RealClientFactory.createWebClient(vertx, clientType, proxyOptions), clientType);
    }

    public static RealClient buildRandomProxied(Vertx vertx) {
        Objects.requireNonNull(vertx);
        ProxyOptions proxyOptions = ((ProxyController)Engine.get().getModule(Controller.PROXY)).getProxyRandomCyclic().getAsVertx();
        return RealClient.create(RealClientFactory.createWebClient(vertx, proxyOptions), ClientType.CHROME);
    }

    public static RealClient fromOtherFreshCookie(Vertx vertx, RealClient realClient) {
        Objects.requireNonNull(vertx);
        Objects.requireNonNull(realClient);
        ProxyOptions proxyOptions = realClient.getOptions().getProxyOptions();
        CookieJar cookieJar = new CookieJar(realClient.cookieStore());
        return RealClient.create(RealClientFactory.createWebClient(vertx, realClient.type(), proxyOptions), cookieJar, realClient.type());
    }

    public static RealClient buildProxied(Vertx vertx, ClientType clientType) {
        Objects.requireNonNull(vertx);
        Objects.requireNonNull(clientType);
        ProxyOptions proxyOptions = ((ProxyController)Engine.get().getModule(Controller.PROXY)).getProxyCyclic().getAsVertx();
        return RealClient.create(RealClientFactory.createWebClient(vertx, clientType, proxyOptions), clientType);
    }

    public static RealClient buildProxied(Vertx vertx, ClientType clientType, ProxyOptions proxyOptions) {
        Objects.requireNonNull(vertx);
        Objects.requireNonNull(clientType);
        return RealClient.create(RealClientFactory.createWebClient(vertx, clientType, proxyOptions), clientType);
    }

    public static RealClient rotateProxy(Vertx vertx, RealClient realClient, Controller controller) {
        ProxyOptions proxyOptions = ((ProxyController)Engine.get().getModule(controller)).getProxyCyclic().getAsVertx();
        Objects.requireNonNull(vertx);
        Objects.requireNonNull(realClient);
        CookieJar cookieJar = realClient.cookieStore();
        return RealClient.create(RealClientFactory.createWebClient(vertx, realClient.type(), proxyOptions), cookieJar, realClient.type());
    }

    public static RealClient fromOther(Vertx vertx, RealClient realClient, ProxyOptions proxyOptions) {
        Objects.requireNonNull(vertx);
        Objects.requireNonNull(realClient);
        Objects.requireNonNull(proxyOptions);
        CookieJar cookieJar = realClient.cookieStore();
        return RealClient.create(RealClientFactory.createWebClient(vertx, realClient.type(), proxyOptions), cookieJar, realClient.type());
    }

    public static RealClient rotateProxy(Vertx vertx, RealClient realClient) {
        ProxyOptions proxyOptions = ((ProxyController)Engine.get().getModule(Controller.PROXY)).getProxyCyclic().getAsVertx();
        Objects.requireNonNull(vertx);
        Objects.requireNonNull(realClient);
        CookieJar cookieJar = realClient.cookieStore();
        return RealClient.create(RealClientFactory.createWebClient(vertx, realClient.type(), proxyOptions), cookieJar, realClient.type());
    }

    public static RealClient buildProxied(Vertx vertx) {
        Objects.requireNonNull(vertx);
        ProxyOptions proxyOptions = ((ProxyController)Engine.get().getModule(Controller.PROXY)).getProxyCyclic().getAsVertx();
        return RealClient.create(RealClientFactory.createWebClient(vertx, proxyOptions), ClientType.CHROME);
    }
}

