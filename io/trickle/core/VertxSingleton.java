/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.Vertx
 *  io.vertx.core.VertxOptions
 *  io.vertx.core.buffer.Buffer
 *  io.vertx.core.eventbus.Message
 *  io.vertx.core.eventbus.MessageCodec
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.codec.BodyCodec
 */
package io.trickle.core;

import io.trickle.account.Account;
import io.trickle.harvester.pooled.SharedCaptchaToken;
import io.trickle.util.DirectObjectCodec;
import io.trickle.util.LocalClient;
import io.trickle.util.analytics.Graphing;
import io.trickle.util.request.Request;
import io.trickle.webclient.ProxyPoolClient;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.codec.BodyCodec;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public enum VertxSingleton {
    INSTANCE;

    public Vertx vertx;
    public Map<String, Buffer> cachedCaptchaBodies;
    public LocalClient client;
    public ProxyPoolClient proxyPoolClient;
    public String[] captchaEps = new String[0];

    public void loginHandler(Message message) {
        Graphing.analyse((String)message.body());
    }

    public void lambda$initCachedCaptchaBodies$1(String string, Buffer buffer) {
        System.out.println("put it in");
        this.cachedCaptchaBodies.put(string, buffer);
    }

    public void register() {
        this.vertx.eventBus().localConsumer("login.loader", this::loginHandler);
    }

    public static void lambda$new$0(Throwable throwable) {
    }

    public void initCachedCaptchaBodies() {
        String[] stringArray = this.captchaEps;
        int n = stringArray.length;
        int n2 = 0;
        while (n2 < n) {
            String string = stringArray[n2];
            HttpRequest httpRequest = this.client.getClient().getAbs(string).as(BodyCodec.buffer());
            httpRequest.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"98\", \"Google Chrome\";v=\"98\"");
            httpRequest.putHeader("sec-ch-ua-mobile", "?0");
            httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
            httpRequest.putHeader("upgrade-insecure-requests", "1");
            httpRequest.putHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.113 Safari/537.36");
            httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
            httpRequest.putHeader("sec-fetch-site", "cross-site");
            httpRequest.putHeader("sec-fetch-mode", "navigate");
            httpRequest.putHeader("sec-fetch-dest", "iframe");
            httpRequest.putHeader("referer", "https://packershoes.com/");
            httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
            httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
            Request.executeTillOk(httpRequest).thenAccept(arg_0 -> this.lambda$initCachedCaptchaBodies$1(string, arg_0));
            ++n2;
        }
    }

    public ProxyPoolClient getProxyPoolClient() {
        return this.proxyPoolClient;
    }

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    public VertxSingleton() {
        this.vertx = Vertx.vertx((VertxOptions)new VertxOptions().setWorkerPoolSize(2).setMaxWorkerExecuteTime(10L).setBlockedThreadCheckInterval(1L).setBlockedThreadCheckIntervalUnit(TimeUnit.HOURS).setMaxWorkerExecuteTimeUnit(TimeUnit.MINUTES).setPreferNativeTransport(true));
        this.vertx.eventBus().registerDefaultCodec(SharedCaptchaToken.class, (MessageCodec)new DirectObjectCodec(SharedCaptchaToken.class));
        this.vertx.eventBus().registerDefaultCodec(Account.class, (MessageCodec)new DirectObjectCodec(Account.class));
        this.vertx.exceptionHandler(VertxSingleton::lambda$new$0);
        this.register();
        this.client = new LocalClient(this.vertx);
        this.proxyPoolClient = new ProxyPoolClient();
        this.cachedCaptchaBodies = new ConcurrentHashMap<String, Buffer>();
        this.initCachedCaptchaBodies();
    }

    public LocalClient getLocalClient() {
        return this.client;
    }

    public Vertx get() {
        return this.vertx;
    }
}

