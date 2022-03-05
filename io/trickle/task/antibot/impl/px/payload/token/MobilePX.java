/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.MultiMap
 *  io.vertx.core.buffer.Buffer
 *  io.vertx.core.http.HttpMethod
 *  io.vertx.core.http.RequestOptions
 *  io.vertx.core.json.JsonObject
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.client.HttpResponse
 *  io.vertx.ext.web.client.impl.HttpRequestImpl
 *  io.vertx.ext.web.codec.BodyCodec
 */
package io.trickle.task.antibot.impl.px.payload.token;

import io.trickle.core.actor.TaskActor;
import io.trickle.task.antibot.impl.px.PerimeterX;
import io.trickle.task.antibot.impl.px.payload.Payload;
import io.trickle.task.antibot.impl.px.payload.captcha.DesktopPX;
import io.trickle.task.antibot.impl.px.payload.token.FirstPayload;
import io.trickle.task.antibot.impl.px.payload.token.InitPayload;
import io.trickle.task.antibot.impl.px.payload.token.MobilePX$1;
import io.trickle.task.antibot.impl.px.payload.token.SecondPayload;
import io.trickle.task.sites.Site;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Headers$Pseudo;
import io.trickle.util.request.Request;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.RequestOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.impl.HttpRequestImpl;
import io.vertx.ext.web.codec.BodyCodec;
import java.lang.invoke.LambdaMetafactory;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Matcher;

public class MobilePX
extends PerimeterX {
    public long timerId = -1L;
    public SecondPayload secondPayload;
    public long expiryTime = -1L;
    public DesktopPX captchaDelegate = null;
    public Site site = Site.WALMART;
    public long requestTiming;
    public boolean hasSolvedOnCurrentVid = false;

    public static RequestOptions collectorRequest(Site site) {
        String string = "";
        Object object = "";
        switch (MobilePX$1.$SwitchMap$io$trickle$task$sites$Site[site.ordinal()]) {
            case 1: {
                string = "PX9Qx3Rve4";
                object = "PerimeterX Android SDK/" + "v1.13.2".substring(1);
                return new RequestOptions().setAbsoluteURI("https://collector-" + string.toLowerCase() + ".perimeterx.net/api/v1/collector/mobile").setMethod(HttpMethod.POST).setTimeout(TimeUnit.SECONDS.toMillis(30L)).setHeaders(Headers$Pseudo.MPAS.get()).putHeader("user-agent", (String)object).putHeader("content-type", "application/x-www-form-urlencoded").putHeader("content-length", "DEFAULT_VALUE").putHeader("accept-encoding", "gzip");
            }
            case 2: {
                string = "PXUArm9B04";
                object = "PerimeterX Android SDK/" + "v1.8.0".substring(1);
                return new RequestOptions().setAbsoluteURI("https://collector-" + string.toLowerCase() + ".perimeterx.net/api/v1/collector/mobile").setMethod(HttpMethod.POST).setTimeout(TimeUnit.SECONDS.toMillis(30L)).setHeaders(Headers$Pseudo.MPAS.get()).putHeader("user-agent", (String)object).putHeader("content-type", "application/x-www-form-urlencoded").putHeader("content-length", "DEFAULT_VALUE").putHeader("accept-encoding", "gzip");
            }
        }
        return new RequestOptions().setAbsoluteURI("https://collector-" + string.toLowerCase() + ".perimeterx.net/api/v1/collector/mobile").setMethod(HttpMethod.POST).setTimeout(TimeUnit.SECONDS.toMillis(30L)).setHeaders(Headers$Pseudo.MPAS.get()).putHeader("user-agent", (String)object).putHeader("content-type", "application/x-www-form-urlencoded").putHeader("content-length", "DEFAULT_VALUE").putHeader("accept-encoding", "gzip");
    }

    @Override
    public String getDeviceSecUA() {
        return "\"Google Chrome\";v=\"94\", \" Not;A Brand\";v=\"99\", \"Chromium\";v=\"94\"";
    }

    public void scheduleKeepAlive(long l) {
        this.expiryTime = l;
        this.startTimer();
    }

    public MobilePX(TaskActor taskActor) {
        super(taskActor);
    }

    @Override
    public CompletableFuture initialise() {
        this.requestTiming = 0L;
        InitPayload initPayload = null;
        int n = 0;
        while (n < 5) {
            try {
                if (initPayload == null) {
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug("Fetching init payload from device API");
                    }
                    CompletableFuture completableFuture = InitPayload.fetch();
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture2 = completableFuture;
                        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> MobilePX.async$initialise(this, initPayload, n, completableFuture2, null, null, null, 1, arg_0));
                    }
                    initPayload = (InitPayload)completableFuture.join();
                    CompletableFuture completableFuture3 = this.sendInit(initPayload);
                    if (!completableFuture3.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture3;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> MobilePX.async$initialise(this, initPayload, n, completableFuture4, null, null, null, 2, arg_0));
                    }
                    completableFuture3.join();
                } else {
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug("Sending first payload.");
                    }
                    FirstPayload firstPayload = new FirstPayload(initPayload, this.site);
                    CompletableFuture completableFuture = this.sendPayload(firstPayload);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture5 = completableFuture;
                        return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> MobilePX.async$initialise(this, initPayload, n, completableFuture5, firstPayload, null, null, 3, arg_0));
                    }
                    JsonObject jsonObject = (JsonObject)completableFuture.join();
                    Objects.requireNonNull(jsonObject);
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug("Fetched first payload: {}", (Object)jsonObject.encodePrettily());
                    }
                    this.secondPayload = new SecondPayload(firstPayload, jsonObject, this.requestTiming, this.site);
                    CompletableFuture completableFuture6 = this.sendPayload(this.secondPayload);
                    if (!completableFuture6.isDone()) {
                        CompletableFuture completableFuture7 = completableFuture6;
                        return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> MobilePX.async$initialise(this, initPayload, n, completableFuture7, firstPayload, jsonObject, null, 4, arg_0));
                    }
                    JsonObject jsonObject2 = (JsonObject)completableFuture6.join();
                    Objects.requireNonNull(jsonObject2);
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug("Fetched second payload: {}", (Object)jsonObject2.encodePrettily());
                    }
                    if (this.onResult(jsonObject2.toString())) {
                        return CompletableFuture.completedFuture(true);
                    }
                    initPayload = null;
                    this.secondPayload = null;
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug("Reset API state. Will re-initialise device and payloads on next attempt");
                    }
                    this.logger.warn("Failed to initialise API. Resetting & retrying...");
                }
            }
            catch (Throwable throwable) {
                this.logger.warn("Error initialising api: {}. Retrying...", (Object)throwable.getMessage());
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug((Object)throwable);
                }
                CompletableFuture completableFuture = VertxUtil.randomSleep(10000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture8 = completableFuture;
                    return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> MobilePX.async$initialise(this, initPayload, n, completableFuture8, null, null, throwable, 5, arg_0));
                }
                completableFuture.join();
            }
            ++n;
        }
        return CompletableFuture.failedFuture(new Exception("Failed to initialise antibot API"));
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$solveCaptcha(MobilePX var0, String var1_1, String var2_2, String var3_3, CompletableFuture var4_4, String var5_5, int var6_6, Object var7_7) {
        switch (var6_6) {
            case 0: {
                v0 = VertxUtil.hardCodedSleep(15000L);
                if (!v0.isDone()) {
                    var5_5 = v0;
                    return var5_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solveCaptcha(io.trickle.task.antibot.impl.px.payload.token.MobilePX java.lang.String java.lang.String java.lang.String java.util.concurrent.CompletableFuture java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((MobilePX)var0, (String)var1_1, (String)var2_2, (String)var3_3, (CompletableFuture)var5_5, null, (int)1));
                }
                ** GOTO lbl10
            }
            case 1: {
                v0 = var4_4;
lbl10:
                // 2 sources

                v0.join();
                if (var0.captchaDelegate == null) {
                    var0.captchaDelegate = new DesktopPX(null);
                }
                if ((var4_4 = null) == null) return CompletableFuture.completedFuture(false);
                var0.logger.info("Successfully solved captcha!");
                if (var0.logger.isDebugEnabled()) {
                    var0.logger.debug("Mobile captcha response: {}", var4_4);
                }
                var0.scheduleKeepaliveAfterCaptcha();
                var0.result = var4_4;
                var0.hasSolvedOnCurrentVid = true;
                v1 = var0.solve();
                if (!v1.isDone()) {
                    var5_5 = v1;
                    return var5_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solveCaptcha(io.trickle.task.antibot.impl.px.payload.token.MobilePX java.lang.String java.lang.String java.lang.String java.util.concurrent.CompletableFuture java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((MobilePX)var0, (String)var1_1, (String)var2_2, (String)var3_3, (CompletableFuture)var5_5, (String)var4_4, (int)2));
                }
                ** GOTO lbl29
            }
            case 2: {
                v1 = var4_4;
                var4_4 = var5_5;
lbl29:
                // 2 sources

                v1.join();
                return CompletableFuture.completedFuture(true);
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public String getDeviceUA() {
        if (!this.logger.isDebugEnabled()) return "Mozilla/5.0 (Linux; Android 11; Pixel 5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.85 Mobile Safari/537.36";
        this.logger.debug("Device UA should not be used with MobilePX");
        return "Mozilla/5.0 (Linux; Android 11; Pixel 5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.85 Mobile Safari/537.36";
    }

    @Override
    public String getVid() {
        if (this.secondPayload != null) return this.secondPayload.VID_HEADER;
        return null;
    }

    public static RequestOptions initRequest(Site site) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.setAbsoluteURI("https://px-conf.perimeterx.net/api/v1/mobile").setMethod(HttpMethod.POST).setTimeout(TimeUnit.SECONDS.toMillis(30L)).putHeader("content-type", "application/json; charset=utf-8").putHeader("content-length", "DEFAULT_VALUE").putHeader("accept-encoding", "gzip");
        switch (MobilePX$1.$SwitchMap$io$trickle$task$sites$Site[site.ordinal()]) {
            case 1: {
                requestOptions.putHeader("user-agent", "okhttp/4.9.0");
                return requestOptions;
            }
            case 2: {
                requestOptions.putHeader("user-agent", "okhttp/3.12.1");
                return requestOptions;
            }
        }
        return requestOptions;
    }

    @Override
    public void close() {
        this.cancelTimer();
        super.close();
    }

    public void scheduleKeepaliveAfterCaptcha() {
        this.scheduleKeepAlive(TimeUnit.MINUTES.toMillis(5L));
    }

    @Override
    public String getDeviceAcceptEncoding() {
        if (!this.logger.isDebugEnabled()) return "gzip, deflate, br";
        this.logger.debug("Device encoding should not be used with MobilePX");
        return "gzip, deflate, br";
    }

    public CompletableFuture onKeepalive() {
        try {
            CompletableFuture completableFuture = this.sendKeepAlive();
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> MobilePX.async$onKeepalive(this, completableFuture2, 1, arg_0));
            }
            JsonObject jsonObject = (JsonObject)completableFuture.join();
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Received keep-alive response: {}", (Object)jsonObject.encodePrettily());
            }
            if (this.onResult(jsonObject.toString())) {
                if (!this.logger.isDebugEnabled()) return CompletableFuture.completedFuture(null);
                this.logger.debug("Solved keepalive: {}", this.result);
                return CompletableFuture.completedFuture(null);
            }
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Failed to solve keepalive. Retrying in 5s");
            }
            this.scheduleKeepAlive(5000L);
            return CompletableFuture.completedFuture(null);
        }
        catch (Throwable throwable) {
            this.logger.warn("Error on keepalive: {}", (Object)throwable.getMessage());
            if (!this.logger.isDebugEnabled()) return CompletableFuture.completedFuture(null);
            this.logger.debug((Object)throwable);
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture sendInit(InitPayload initPayload) {
        if (initPayload == null) {
            return CompletableFuture.failedFuture(new Exception("Initialisation payload is null"));
        }
        HttpRequest httpRequest = this.client.request(HttpMethod.POST, MobilePX.initRequest(this.site)).as(BodyCodec.jsonObject());
        return this.execute(httpRequest, initPayload.asBuffer(this.site));
    }

    public CompletableFuture sendPayload(Payload payload) {
        if (payload == null) {
            return CompletableFuture.failedFuture(new Exception("Provided payload is null"));
        }
        HttpRequest httpRequest = this.client.request(HttpMethod.POST, MobilePX.collectorRequest(this.site)).as(BodyCodec.jsonObject());
        return this.execute(httpRequest, payload.asForm());
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$onKeepalive(MobilePX var0, CompletableFuture var1_1, int var2_3, Object var3_5) {
        switch (var2_3) {
            case 0: {
                try {
                    v0 = var0.sendKeepAlive();
                    if (!v0.isDone()) {
                        var2_4 = v0;
                        return var2_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$onKeepalive(io.trickle.task.antibot.impl.px.payload.token.MobilePX java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((MobilePX)var0, (CompletableFuture)var2_4, (int)1));
                    }
lbl8:
                    // 3 sources

                    while (true) {
                        var1_1 = (JsonObject)v0.join();
                        if (var0.logger.isDebugEnabled()) {
                            var0.logger.debug("Received keep-alive response: {}", (Object)var1_1.encodePrettily());
                        }
                        if (var0.onResult(var1_1.toString())) {
                            if (var0.logger.isDebugEnabled() == false) return CompletableFuture.completedFuture(null);
                            var0.logger.debug("Solved keepalive: {}", var0.result);
                            return CompletableFuture.completedFuture(null);
                        }
                        if (var0.logger.isDebugEnabled()) {
                            var0.logger.debug("Failed to solve keepalive. Retrying in 5s");
                        }
                        var0.scheduleKeepAlive(5000L);
                        return CompletableFuture.completedFuture(null);
                    }
                }
                catch (Throwable var1_2) {
                    var0.logger.warn("Error on keepalive: {}", (Object)var1_2.getMessage());
                    if (var0.logger.isDebugEnabled() == false) return CompletableFuture.completedFuture(null);
                    var0.logger.debug((Object)var1_2);
                }
                return CompletableFuture.completedFuture(null);
            }
            case 1: {
                v0 = var1_1;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture execute(HttpRequest httpRequest, Object object) {
        int n = 0;
        while (this.client.isActive()) {
            if (n++ > 15) return CompletableFuture.failedFuture(new Exception("Failed to send payload"));
            try {
                HttpResponse httpResponse;
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Executing payload request '{}'", (Object)((HttpRequestImpl)httpRequest).uri());
                }
                if (object == null) {
                    CompletableFuture completableFuture = Request.send(httpRequest);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture2 = completableFuture;
                        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> MobilePX.async$execute(this, httpRequest, object, n, completableFuture2, 0L, null, null, 1, arg_0));
                    }
                    httpResponse = (HttpResponse)completableFuture.join();
                } else if (object instanceof Buffer) {
                    CompletableFuture completableFuture = Request.send(httpRequest, (Buffer)object);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture3 = completableFuture;
                        return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> MobilePX.async$execute(this, httpRequest, object, n, completableFuture3, 0L, null, null, 2, arg_0));
                    }
                    httpResponse = (HttpResponse)completableFuture.join();
                } else {
                    if (!(object instanceof MultiMap)) throw new Exception("Unsupported body format for payload request");
                    long l = System.currentTimeMillis();
                    CompletableFuture completableFuture = Request.send(httpRequest, (MultiMap)object);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> MobilePX.async$execute(this, httpRequest, object, n, completableFuture4, l, null, null, 3, arg_0));
                    }
                    httpResponse = (HttpResponse)completableFuture.join();
                    this.requestTiming = System.currentTimeMillis() - l;
                    if (this.secondPayload != null) {
                        this.secondPayload.updatePX349(this.requestTiming);
                    }
                }
                if (httpResponse != null) {
                    if (!this.logger.isDebugEnabled()) return CompletableFuture.completedFuture(httpResponse.body());
                    this.logger.debug("Received payload response from '{}' : {}", (Object)((HttpRequestImpl)httpRequest).uri(), httpResponse.body());
                    return CompletableFuture.completedFuture(httpResponse.body());
                }
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Failed to receive payload response from '{}'", (Object)((HttpRequestImpl)httpRequest).uri());
                } else {
                    this.logger.warn("Error on payload execution. Retrying...");
                }
                CompletableFuture completableFuture = VertxUtil.randomSleep(10000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture5 = completableFuture;
                    return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> MobilePX.async$execute(this, httpRequest, object, n, completableFuture5, 0L, httpResponse, null, 4, arg_0));
                }
                completableFuture.join();
            }
            catch (Throwable throwable) {
                this.logger.warn("Error sending payload: {}", (Object)throwable.getMessage());
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug((Object)throwable);
                }
                CompletableFuture completableFuture = VertxUtil.randomSleep(10000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> MobilePX.async$execute(this, httpRequest, object, n, completableFuture6, 0L, null, throwable, 5, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.failedFuture(new Exception("Failed to send payload"));
    }

    public void startTimer() {
        if (this.vertx == null) {
            if (!this.logger.isDebugEnabled()) return;
            this.logger.debug("Unable to start keepalive timer: Vertx context is null");
            return;
        }
        this.cancelTimer();
        this.timerId = this.vertx.setTimer(this.expiryTime, this::lambda$startTimer$0);
        if (!this.logger.isDebugEnabled()) return;
        this.logger.debug("Scheduled keep-alive to be sent in {}ms", (Object)this.expiryTime);
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$solve(MobilePX var0, FirstPayload var1_1, CompletableFuture var2_3, JsonObject var3_4, Throwable var4_5, int var5_6, Object var6_7) {
        switch (var5_6) {
            case 0: {
                if (var0.secondPayload == null) {
                    if (var0.logger.isDebugEnabled() == false) return var0.initialise();
                    var0.logger.debug("Second payload is null on solve(). Performing hard reinitialization (full-reset)");
                    return var0.initialise();
                }
                try {
                    var1_1 = new FirstPayload(var0.secondPayload, var0.secondPayload.sdkInitCount, var0.requestTiming, var0.site);
                    v0 = var0.sendPayload((Payload)var1_1);
                    if (!v0.isDone()) {
                        var4_5 = v0;
                        return var4_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solve(io.trickle.task.antibot.impl.px.payload.token.MobilePX io.trickle.task.antibot.impl.px.payload.token.FirstPayload java.util.concurrent.CompletableFuture io.vertx.core.json.JsonObject java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((MobilePX)var0, (FirstPayload)var1_1, (CompletableFuture)var4_5, null, null, (int)1));
                    }
lbl13:
                    // 3 sources

                    while (true) {
                        var2_3 = (JsonObject)v0.join();
                        if (var0.logger.isDebugEnabled()) {
                            var0.logger.debug("Fetched first payload: {}", (Object)var2_3.encodePrettily());
                        }
                        var0.secondPayload = new SecondPayload((FirstPayload)var1_1, (JsonObject)var2_3, var0.requestTiming, var0.site);
                        v1 = var0.sendPayload(var0.secondPayload);
                        if (!v1.isDone()) {
                            var4_5 = v1;
                            return var4_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solve(io.trickle.task.antibot.impl.px.payload.token.MobilePX io.trickle.task.antibot.impl.px.payload.token.FirstPayload java.util.concurrent.CompletableFuture io.vertx.core.json.JsonObject java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((MobilePX)var0, (FirstPayload)var1_1, (CompletableFuture)var4_5, (JsonObject)var2_3, null, (int)2));
                        }
lbl22:
                        // 3 sources

                        while (true) {
                            var3_4 = (JsonObject)v1.join();
                            if (var0.logger.isDebugEnabled()) {
                                var0.logger.debug("Fetched second payload: {}", (Object)var3_4.encodePrettily());
                            }
                            if (var0.onResult(var3_4.toString()) == false) throw new Exception("Failed solving sensor");
                            return CompletableFuture.completedFuture(true);
                        }
                        break;
                    }
                }
                catch (Throwable var1_2) {
                    var0.logger.warn("Error solving sensor: {}. Retrying...", (Object)var1_2.getMessage());
                    if (var0.logger.isDebugEnabled()) {
                        var0.logger.debug((Object)var1_2);
                    }
                    if (!(v2 = VertxUtil.randomSleep(10000L)).isDone()) {
                        var4_5 = v2;
                        return var4_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solve(io.trickle.task.antibot.impl.px.payload.token.MobilePX io.trickle.task.antibot.impl.px.payload.token.FirstPayload java.util.concurrent.CompletableFuture io.vertx.core.json.JsonObject java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((MobilePX)var0, null, (CompletableFuture)var4_5, null, (Throwable)var1_2, (int)3));
                    }
lbl35:
                    // 3 sources

                    while (true) {
                        v2.join();
                        return CompletableFuture.completedFuture(false);
                    }
                }
            }
            case 1: {
                v0 = var2_3;
                ** continue;
            }
            case 2: {
                v1 = var2_3;
                var2_3 = var3_4;
                ** continue;
            }
            case 3: {
                v2 = var2_3;
                var1_1 = var4_5;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public String getDeviceLang() {
        if (!this.logger.isDebugEnabled()) return "en-GB,en;q=0.9";
        this.logger.debug("Device lang should not be used with MobilePX");
        return "en-GB,en;q=0.9";
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$execute(MobilePX var0, HttpRequest var1_1, Object var2_2, int var3_3, CompletableFuture var4_4, long var5_6, HttpResponse var7_7, Throwable var8_8, int var9_9, Object var10_10) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [9[CATCHBLOCK]], but top level block is 15[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:845)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1042)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:929)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:73)
         *     at org.benf.cfr.reader.Main.main(Main.java:49)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:303)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$null$5(ResourceDecompiling.java:158)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public boolean onResult(String string) {
        Matcher matcher = BAKE_PATTERN.matcher(string);
        if (!matcher.find()) return false;
        this.result = "3:" + matcher.group(1);
        this.scheduleKeepalive();
        if (!this.logger.isDebugEnabled()) return true;
        this.logger.debug("New PX token value: {}", this.result);
        return true;
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$initialise(MobilePX var0, InitPayload var1_1, int var2_2, CompletableFuture var3_3, FirstPayload var4_5, JsonObject var5_6, Throwable var6_7, int var7_8, Object var8_9) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [9[CATCHBLOCK]], but top level block is 15[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:845)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1042)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:929)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:73)
         *     at org.benf.cfr.reader.Main.main(Main.java:49)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:303)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$null$5(ResourceDecompiling.java:158)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    @Override
    public CompletableFuture solve() {
        if (this.secondPayload == null) {
            if (!this.logger.isDebugEnabled()) return this.initialise();
            this.logger.debug("Second payload is null on solve(). Performing hard reinitialization (full-reset)");
            return this.initialise();
        }
        try {
            FirstPayload firstPayload = new FirstPayload(this.secondPayload, this.secondPayload.sdkInitCount, this.requestTiming, this.site);
            CompletableFuture completableFuture = this.sendPayload(firstPayload);
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> MobilePX.async$solve(this, firstPayload, completableFuture2, null, null, 1, arg_0));
            }
            JsonObject jsonObject = (JsonObject)completableFuture.join();
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Fetched first payload: {}", (Object)jsonObject.encodePrettily());
            }
            this.secondPayload = new SecondPayload(firstPayload, jsonObject, this.requestTiming, this.site);
            CompletableFuture completableFuture3 = this.sendPayload(this.secondPayload);
            if (!completableFuture3.isDone()) {
                CompletableFuture completableFuture4 = completableFuture3;
                return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> MobilePX.async$solve(this, firstPayload, completableFuture4, jsonObject, null, 2, arg_0));
            }
            JsonObject jsonObject2 = (JsonObject)completableFuture3.join();
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Fetched second payload: {}", (Object)jsonObject2.encodePrettily());
            }
            if (!this.onResult(jsonObject2.toString())) throw new Exception("Failed solving sensor");
            return CompletableFuture.completedFuture(true);
        }
        catch (Throwable throwable) {
            this.logger.warn("Error solving sensor: {}. Retrying...", (Object)throwable.getMessage());
            if (this.logger.isDebugEnabled()) {
                this.logger.debug((Object)throwable);
            }
            CompletableFuture completableFuture = VertxUtil.randomSleep(10000L);
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture5 = completableFuture;
                return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> MobilePX.async$solve(this, null, completableFuture5, null, throwable, 3, arg_0));
            }
            completableFuture.join();
            return CompletableFuture.completedFuture(false);
        }
    }

    @Override
    public CompletableFuture solveCaptcha(String string, String string2, String string3) {
        String string4;
        CompletableFuture completableFuture = VertxUtil.hardCodedSleep(15000L);
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> MobilePX.async$solveCaptcha(this, string, string2, string3, completableFuture2, null, 1, arg_0));
        }
        completableFuture.join();
        if (this.captchaDelegate == null) {
            this.captchaDelegate = new DesktopPX(null);
        }
        if ((string4 = null) == null) return CompletableFuture.completedFuture(false);
        this.logger.info("Successfully solved captcha!");
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Mobile captcha response: {}", string4);
        }
        this.scheduleKeepaliveAfterCaptcha();
        this.result = string4;
        this.hasSolvedOnCurrentVid = true;
        CompletableFuture completableFuture3 = this.solve();
        if (!completableFuture3.isDone()) {
            CompletableFuture completableFuture4 = completableFuture3;
            return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> MobilePX.async$solveCaptcha(this, string, string2, string3, completableFuture4, string4, 2, arg_0));
        }
        completableFuture3.join();
        return CompletableFuture.completedFuture(true);
    }

    @Override
    public void reset() {
        this.cancelTimer();
        this.secondPayload = null;
        this.expiryTime = -1L;
        this.timerId = -1L;
        this.hasSolvedOnCurrentVid = false;
        if (this.captchaDelegate == null) return;
        this.captchaDelegate.reset();
    }

    public CompletableFuture sendKeepAlive() {
        if (this.secondPayload == null) {
            return CompletableFuture.failedFuture(new Exception("Data payload is null"));
        }
        HttpRequest httpRequest = this.client.request(HttpMethod.POST, MobilePX.collectorRequest(this.site)).as(BodyCodec.jsonObject());
        return this.execute(httpRequest, this.secondPayload.asKeepAliveForm());
    }

    public void lambda$startTimer$0(Long l) {
        this.onKeepalive();
    }

    public void cancelTimer() {
        if (this.vertx == null) return;
        if (this.timerId == -1L) return;
        if (this.vertx.cancelTimer(this.timerId) && this.logger.isDebugEnabled()) {
            this.logger.debug("Cancelled keep-alive timer of id '{}' early", (Object)this.timerId);
        }
        this.timerId = -1L;
    }

    public void scheduleKeepalive() {
        this.scheduleKeepAlive(TimeUnit.MINUTES.toMillis(4L));
    }
}

