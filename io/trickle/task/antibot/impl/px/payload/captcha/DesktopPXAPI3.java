/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.netty.util.AsciiString
 *  io.vertx.core.MultiMap
 *  io.vertx.core.json.JsonObject
 *  io.vertx.core.net.ProxyOptions
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.client.HttpResponse
 *  io.vertx.ext.web.client.predicate.ResponsePredicate
 *  io.vertx.ext.web.codec.BodyCodec
 */
package io.trickle.task.antibot.impl.px.payload.captcha;

import io.netty.util.AsciiString;
import io.trickle.core.actor.TaskActor;
import io.trickle.task.antibot.impl.px.PerimeterX;
import io.trickle.util.Utils;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.trickle.webclient.RealClient;
import io.trickle.webclient.TaskApiClient;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.ProxyOptions;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import io.vertx.ext.web.codec.BodyCodec;
import java.lang.invoke.LambdaMetafactory;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class DesktopPXAPI3
extends PerimeterX {
    public TaskApiClient delegate;
    public static CharSequence CFP_VALUE;
    public static int RETRY;
    public String secUserAgent;
    public String cookie;
    public static CharSequence RF_VALUE;
    public static CharSequence FP_VALUE;
    public static CharSequence PX3_VALUE;
    public String userAgent;
    public static CharSequence ONE_VALUE;
    public static int TIMEOUT;

    public JsonObject apiBody(String string) {
        return new JsonObject().put("url", (Object)string).put("proxy", (Object)this.getProxyString());
    }

    public DesktopPXAPI3(TaskActor taskActor) {
        super(taskActor, null);
        this.delegate = taskActor.getClient();
    }

    @Override
    public String getDeviceUA() {
        return this.userAgent;
    }

    @Override
    public String getDeviceSecUA() {
        return this.secUserAgent;
    }

    public HttpRequest apiRequest(String string) {
        return this.client.postAbs("https://px-gateway-dsor72du.uc.gateway.dev/" + string).timeout(120000L).expect(ResponsePredicate.SC_OK).putHeader("x-api-key", "AIzaSyCxU4Wyg87uArwRL0NdlKb5oOAGTNTcX9Q").as(BodyCodec.jsonObject());
    }

    public void parseSecUA(String string) {
        String string2 = Utils.parseChromeVer(this.userAgent);
        this.secUserAgent = "\"Google Chrome\";v=\"" + string2 + "\", \"Chromium\";v=\"" + string2 + "\", \";Not A Brand\";v=\"99\"";
    }

    public CompletableFuture execute(String string, String string2) {
        int n = 0;
        while (n < 10) {
            block12: {
                if (!this.delegate.getWebClient().isActive()) return CompletableFuture.failedFuture(new Exception("Exceeded retries"));
                try {
                    CompletableFuture completableFuture = Request.send(this.apiRequest(string), this.apiBody(string2));
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture2 = completableFuture;
                        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXAPI3.async$execute(this, string, string2, n, completableFuture2, 1, arg_0));
                    }
                    HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                    if (httpResponse != null) {
                        String string3;
                        JsonObject jsonObject = (JsonObject)httpResponse.body();
                        Objects.requireNonNull(jsonObject, "No Result");
                        if (this.logger.isDebugEnabled()) {
                            this.logger.debug("Sensor API for step={} responded with body={}", (Object)string, (Object)jsonObject.encode());
                        }
                        if (jsonObject.getBoolean("ok", Boolean.valueOf(false)).booleanValue()) {
                            string3 = jsonObject.getString("cookie");
                            Objects.requireNonNull(string3, "Sensor response none");
                            String string4 = jsonObject.getString("ua");
                            Objects.requireNonNull(string4, "Sensor data none");
                            this.userAgent = string4;
                            this.parseSecUA(this.userAgent);
                            return CompletableFuture.completedFuture(string3);
                        }
                        if (this.logger.isDebugEnabled()) {
                            this.logger.debug("Sensor request not OK: {}", (Object)jsonObject.encode());
                        }
                        if ((string3 = jsonObject.getString("err", "request failed")).equalsIgnoreCase("request failed") || string3.equalsIgnoreCase("exhausted")) {
                            boolean bl = this.delegate.rotateProxy();
                            if (this.logger.isDebugEnabled()) {
                                this.logger.debug("Rotated proxy after sensor fail ok={}", (Object)bl);
                            }
                        }
                    } else {
                        this.logger.warn("Failed to get sensor. Retrying...");
                    }
                }
                catch (Throwable throwable) {
                    this.logger.error("Failed to solve sensor: {}. Retrying...", (Object)throwable.getMessage());
                    if (!this.logger.isDebugEnabled()) break block12;
                    this.logger.debug((Object)throwable);
                }
            }
            CompletableFuture completableFuture = VertxUtil.randomSleep(3000L);
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture3 = completableFuture;
                return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXAPI3.async$execute(this, string, string2, n, completableFuture3, 2, arg_0));
            }
            completableFuture.join();
        }
        return CompletableFuture.failedFuture(new Exception("Exceeded retries"));
    }

    @Override
    public String getDeviceLang() {
        return "en-GB,en;q=0.9";
    }

    @Override
    public CompletableFuture solveCaptcha(String string, String string2, String string3) {
        CompletableFuture completableFuture = this.execute("hold", "https://www.walmart.com/");
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            DesktopPXAPI3 desktopPXAPI3 = this;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXAPI3.async$solveCaptcha(this, string, string2, string3, desktopPXAPI3, completableFuture2, 1, arg_0));
        }
        this.cookie = (String)completableFuture.join();
        MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
        multiMap.add(PX3_VALUE, (CharSequence)this.cookie);
        return CompletableFuture.completedFuture(multiMap);
    }

    public String getProxyString() {
        ProxyOptions proxyOptions = this.delegate.getWebClient().getOptions().getProxyOptions();
        if (proxyOptions != null) return String.format("%s:%s:%s:%s", proxyOptions.getHost(), proxyOptions.getPort(), proxyOptions.getUsername(), proxyOptions.getPassword());
        return "";
    }

    @Override
    public String getDeviceAcceptEncoding() {
        return "gzip, deflate";
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$solveCaptcha(DesktopPXAPI3 var0, String var1_1, String var2_2, String var3_3, DesktopPXAPI3 var4_4, CompletableFuture var5_5, int var6_6, Object var7_8) {
        switch (var6_6) {
            case 0: {
                v0 = var0;
                v1 = var0.execute("hold", "https://www.walmart.com/");
                if (!v1.isDone()) {
                    var6_7 = v1;
                    var5_5 = v0;
                    return var6_7.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solveCaptcha(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPXAPI3 java.lang.String java.lang.String java.lang.String io.trickle.task.antibot.impl.px.payload.captcha.DesktopPXAPI3 java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPXAPI3)var0, (String)var1_1, (String)var2_2, (String)var3_3, (DesktopPXAPI3)var5_5, (CompletableFuture)var6_7, (int)1));
                }
                ** GOTO lbl13
            }
            case 1: {
                v0 = var4_4;
                v1 = var5_5;
lbl13:
                // 2 sources

                v0.cookie = (String)v1.join();
                var4_4 = MultiMap.caseInsensitiveMultiMap();
                var4_4.add(DesktopPXAPI3.PX3_VALUE, var0.cookie);
                return CompletableFuture.completedFuture(var4_4);
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public CompletableFuture solve() {
        MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
        multiMap.add(RF_VALUE, ONE_VALUE);
        multiMap.add(FP_VALUE, ONE_VALUE);
        multiMap.add(CFP_VALUE, ONE_VALUE);
        multiMap.add(PX3_VALUE, (CharSequence)this.cookie);
        return CompletableFuture.completedFuture(multiMap);
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$execute(DesktopPXAPI3 var0, String var1_1, String var2_2, int var3_3, CompletableFuture var4_4, int var5_6, Object var6_8) {
        switch (var5_6) {
            case 0: {
                var3_3 = 0;
                block7: while (var3_3 < 10) {
                    if (var0.delegate.getWebClient().isActive() == false) return CompletableFuture.failedFuture(new Exception("Exceeded retries"));
                    try {
                        v0 = Request.send(var0.apiRequest(var1_1), var0.apiBody(var2_2));
                        if (!v0.isDone()) {
                            var8_11 = v0;
                            return var8_11.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$execute(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPXAPI3 java.lang.String java.lang.String int java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPXAPI3)var0, (String)var1_1, (String)var2_2, (int)var3_3, (CompletableFuture)var8_11, (int)1));
                        }
lbl11:
                        // 3 sources

                        while (true) {
                            var4_4 = (HttpResponse)v0.join();
                            if (var4_4 != null) {
                                var5_7 = (JsonObject)var4_4.body();
                                Objects.requireNonNull(var5_7, "No Result");
                                if (var0.logger.isDebugEnabled()) {
                                    var0.logger.debug("Sensor API for step={} responded with body={}", (Object)var1_1, (Object)var5_7.encode());
                                }
                                if (var5_7.getBoolean("ok", Boolean.valueOf(false)).booleanValue()) {
                                    var6_8 = var5_7.getString("cookie");
                                    Objects.requireNonNull(var6_8, "Sensor response none");
                                    var7_10 = var5_7.getString("ua");
                                    Objects.requireNonNull(var7_10, "Sensor data none");
                                    var0.userAgent = var7_10;
                                    var0.parseSecUA(var0.userAgent);
                                    return CompletableFuture.completedFuture(var6_8);
                                }
                                if (var0.logger.isDebugEnabled()) {
                                    var0.logger.debug("Sensor request not OK: {}", (Object)var5_7.encode());
                                }
                                if ((var6_8 = var5_7.getString("err", "request failed")).equalsIgnoreCase("request failed") || var6_8.equalsIgnoreCase("exhausted")) {
                                    var7_9 = var0.delegate.rotateProxy();
                                    if (var0.logger.isDebugEnabled()) {
                                        var0.logger.debug("Rotated proxy after sensor fail ok={}", (Object)var7_9);
                                    }
                                }
                            } else {
                                var0.logger.warn("Failed to get sensor. Retrying...");
                            }
                            break;
                        }
                    }
                    catch (Throwable var4_5) {
                        var0.logger.error("Failed to solve sensor: {}. Retrying...", (Object)var4_5.getMessage());
                        if (!var0.logger.isDebugEnabled()) ** GOTO lbl42
                        var0.logger.debug((Object)var4_5);
                    }
lbl42:
                    // 4 sources

                    if (!(v1 = VertxUtil.randomSleep(3000L)).isDone()) {
                        var8_12 = v1;
                        return var8_12.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$execute(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPXAPI3 java.lang.String java.lang.String int java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPXAPI3)var0, (String)var1_1, (String)var2_2, (int)var3_3, (CompletableFuture)var8_12, (int)2));
                    }
lbl45:
                    // 3 sources

                    while (true) {
                        v1.join();
                        continue block7;
                        break;
                    }
                }
                return CompletableFuture.failedFuture(new Exception("Exceeded retries"));
            }
            case 1: {
                v0 = var4_4;
                ** continue;
            }
            case 2: {
                v1 = var4_4;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    static {
        TIMEOUT = 120000;
        RETRY = 3000;
        RF_VALUE = AsciiString.cached((String)"_pxff_rf");
        FP_VALUE = AsciiString.cached((String)"_pxff_fp");
        PX3_VALUE = AsciiString.cached((String)"_px3");
        CFP_VALUE = AsciiString.cached((String)"_pxff_cfp");
        ONE_VALUE = AsciiString.cached((String)"1");
    }

    @Override
    public void reset() {
    }

    @Override
    public String getVid() {
        return "";
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$initialise(DesktopPXAPI3 var0, DesktopPXAPI3 var1_1, CompletableFuture var2_2, int var3_3, Object var4_4) {
        switch (var3_3) {
            case 0: {
                v0 = var0;
                v1 = var0.execute("init", "https://www.walmart.com/");
                if (!v1.isDone()) {
                    var2_2 = v1;
                    var1_1 = v0;
                    return var2_2.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$initialise(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPXAPI3 io.trickle.task.antibot.impl.px.payload.captcha.DesktopPXAPI3 java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPXAPI3)var0, (DesktopPXAPI3)var1_1, (CompletableFuture)var2_2, (int)1));
                }
                ** GOTO lbl13
            }
            case 1: {
                v0 = var1_1;
                v1 = var2_2;
lbl13:
                // 2 sources

                v0.cookie = (String)v1.join();
                return CompletableFuture.completedFuture(true);
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public CompletableFuture initialise() {
        CompletableFuture completableFuture = this.execute("init", "https://www.walmart.com/");
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            DesktopPXAPI3 desktopPXAPI3 = this;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXAPI3.async$initialise(this, desktopPXAPI3, completableFuture2, 1, arg_0));
        }
        this.cookie = (String)completableFuture.join();
        return CompletableFuture.completedFuture(true);
    }

    @Override
    public void restartClient(RealClient realClient) {
    }
}

