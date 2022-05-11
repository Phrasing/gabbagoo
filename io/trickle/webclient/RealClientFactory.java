/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.core.Controller
 *  io.trickle.core.Engine
 *  io.trickle.proxy.ProxyController
 *  io.trickle.webclient.ClientType
 *  io.trickle.webclient.CookieJar
 *  io.trickle.webclient.RealClient
 *  io.vertx.core.Vertx
 *  io.vertx.core.http.HttpClient
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
import io.vertx.core.http.HttpClient;
import io.vertx.core.net.ProxyOptions;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import java.util.Objects;

public class RealClientFactory {
    public static WebClient createWebClient(Vertx vertx, ProxyOptions proxyOptions) {
        return RealClientFactory.createWebClient(vertx, ClientType.CHROME, proxyOptions);
    }

    public static RealClient build(Vertx vertx, ClientType clientType) {
        Objects.requireNonNull(vertx);
        Objects.requireNonNull(clientType);
        return RealClient.create((WebClient)RealClientFactory.createWebClient(vertx, clientType, null), (ClientType)clientType);
    }

    public static RealClient fromOther(Vertx vertx, RealClient realClient, ProxyOptions proxyOptions) {
        Objects.requireNonNull(vertx);
        Objects.requireNonNull(realClient);
        Objects.requireNonNull(proxyOptions);
        CookieJar cookieJar = realClient.cookieStore();
        return RealClient.create((WebClient)RealClientFactory.createWebClient(vertx, realClient.type(), proxyOptions), (CookieJar)cookieJar, (ClientType)realClient.type());
    }

    public static RealClient buildProxied(Vertx vertx, ClientType clientType) {
        Objects.requireNonNull(vertx);
        Objects.requireNonNull(clientType);
        ProxyOptions proxyOptions = ((ProxyController)Engine.get().getModule(Controller.PROXY)).getProxyCyclic().getAsVertx();
        return RealClient.create((WebClient)RealClientFactory.createWebClient(vertx, clientType, proxyOptions), (ClientType)clientType);
    }

    public static WebClient createWebClient(Vertx vertx, ClientType clientType, ProxyOptions proxyOptions) {
        return WebClient.create((Vertx)vertx, (WebClientOptions)clientType.options().setProxyOptions(proxyOptions));
    }

    public static RealClient fromOther(Vertx vertx, RealClient realClient, ClientType clientType) {
        Objects.requireNonNull(vertx);
        Objects.requireNonNull(realClient);
        Objects.requireNonNull(clientType);
        ProxyOptions proxyOptions = realClient.getOptions().getProxyOptions();
        CookieJar cookieJar = realClient.cookieStore();
        return RealClient.create((WebClient)RealClientFactory.createWebClient(vertx, clientType, proxyOptions), (CookieJar)cookieJar, (ClientType)clientType);
    }

    public static RealClient fromOtherFreshCookie(Vertx vertx, RealClient realClient) {
        Objects.requireNonNull(vertx);
        Objects.requireNonNull(realClient);
        ProxyOptions proxyOptions = realClient.getOptions().getProxyOptions();
        CookieJar cookieJar = new CookieJar(realClient.cookieStore());
        return RealClient.create((WebClient)RealClientFactory.createWebClient(vertx, realClient.type(), proxyOptions), (CookieJar)cookieJar, (ClientType)realClient.type());
    }

    public static RealClient createChild(Vertx vertx, RealClient realClient) {
        Objects.requireNonNull(vertx);
        Objects.requireNonNull(realClient);
        return RealClient.create((WebClient)WebClient.wrap((HttpClient)realClient.getClient(), (WebClientOptions)realClient.getOptions()), (CookieJar)new CookieJar(realClient.cookieStore()), (ClientType)realClient.type());
    }

    public static RealClient buildProxied(Vertx vertx, ClientType clientType, Controller controller) {
        Objects.requireNonNull(vertx);
        Objects.requireNonNull(clientType);
        ProxyOptions proxyOptions = ((ProxyController)Engine.get().getModule(controller)).getProxyCyclic().getAsVertx();
        return RealClient.create((WebClient)RealClientFactory.createWebClient(vertx, clientType, proxyOptions), (ClientType)clientType);
    }

    public static RealClient buildRandomProxied(Vertx vertx) {
        Objects.requireNonNull(vertx);
        ProxyOptions proxyOptions = ((ProxyController)Engine.get().getModule(Controller.PROXY)).getProxyRandomCyclic().getAsVertx();
        return RealClient.create((WebClient)RealClientFactory.createWebClient(vertx, proxyOptions), (ClientType)ClientType.CHROME);
    }

    public static RealClient buildProxied(Vertx vertx) {
        Objects.requireNonNull(vertx);
        ProxyOptions proxyOptions = ((ProxyController)Engine.get().getModule(Controller.PROXY)).getProxyCyclic().getAsVertx();
        return RealClient.create((WebClient)RealClientFactory.createWebClient(vertx, proxyOptions), (ClientType)ClientType.CHROME);
    }

    public static RealClient rotateProxy(Vertx vertx, RealClient realClient) {
        ProxyOptions proxyOptions = ((ProxyController)Engine.get().getModule(Controller.PROXY)).getProxyCyclic().getAsVertx();
        Objects.requireNonNull(vertx);
        Objects.requireNonNull(realClient);
        CookieJar cookieJar = realClient.cookieStore();
        return RealClient.create((WebClient)RealClientFactory.createWebClient(vertx, realClient.type(), proxyOptions), (CookieJar)cookieJar, (ClientType)realClient.type());
    }

    public static RealClient rotateProxy(Vertx vertx, RealClient realClient, Controller controller) {
        ProxyOptions proxyOptions = ((ProxyController)Engine.get().getModule(controller)).getProxyCyclic().getAsVertx();
        Objects.requireNonNull(vertx);
        Objects.requireNonNull(realClient);
        CookieJar cookieJar = realClient.cookieStore();
        return RealClient.create((WebClient)RealClientFactory.createWebClient(vertx, realClient.type(), proxyOptions), (CookieJar)cookieJar, (ClientType)realClient.type());
    }

    public static RealClient buildProxied(Vertx vertx, ClientType clientType, ProxyOptions proxyOptions) {
        Objects.requireNonNull(vertx);
        Objects.requireNonNull(clientType);
        return RealClient.create((WebClient)RealClientFactory.createWebClient(vertx, clientType, proxyOptions), (ClientType)clientType);
    }

    public static RealClient fromOther(Vertx vertx, RealClient realClient) {
        Objects.requireNonNull(vertx);
        Objects.requireNonNull(realClient);
        return RealClientFactory.fromOther(vertx, realClient, realClient.type());
    }
}
