/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.MultiMap
 *  io.vertx.core.Vertx
 *  io.vertx.core.json.JsonObject
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.client.HttpResponse
 *  io.vertx.ext.web.codec.BodyCodec
 */
package io.trickle.task.antibot.impl.px;

import io.trickle.core.Controller;
import io.trickle.core.actor.TaskActor;
import io.trickle.task.antibot.impl.px.PXToken$1;
import io.trickle.task.antibot.impl.px.PXTokenBase;
import io.trickle.task.antibot.impl.px.Types;
import io.trickle.task.antibot.impl.px.payload.captcha.PXCaptcha;
import io.trickle.task.antibot.impl.px.payload.token.Devices$Device;
import io.trickle.task.antibot.impl.px.payload.token.FirstPayload;
import io.trickle.task.antibot.impl.px.payload.token.InitPayload;
import io.trickle.task.antibot.impl.px.payload.token.SecondPayload;
import io.trickle.task.sites.Site;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Headers$Pseudo;
import io.trickle.util.request.Request;
import io.trickle.webclient.ClientType;
import io.trickle.webclient.RealClient;
import io.trickle.webclient.RealClientFactory;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.codec.BodyCodec;
import java.lang.invoke.LambdaMetafactory;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PXToken
extends PXTokenBase {
    public int rotates = 0;
    public InitPayload initPayload;
    public int expiryCount;
    public Site SITE;
    public long timer;
    public boolean isTokenCaptcha;
    public long requestTime = 0L;
    public SecondPayload secondPayload;
    public boolean isFirstTime = true;
    public PXCaptcha captchaHandler;
    public boolean stopKeepalive = false;
    public long restartTime = 0L;
    public static Pattern BAKE_PATTERN = Pattern.compile("bake\\|.*?\\|.*?\\|(.*?)\\|");
    public boolean hasVidSolved;
    public int failedCaptchaSolves = 0;
    public long expiryTime;

    @Override
    public CompletableFuture solveCaptcha(String string, String string2) {
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture checkOrUpdate() {
        if (!this.isExpired()) return CompletableFuture.completedFuture(null);
        ++this.expiryCount;
        try {
            PXToken pXToken;
            CompletableFuture completableFuture = this.sendPayload(this.secondPayload.asKeepAliveForm());
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                PXToken pXToken2 = this;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> PXToken.async$checkOrUpdate(this, pXToken2, completableFuture2, null, 1, arg_0));
            }
            Optional optional = pXToken.parseResult(((JsonObject)completableFuture.join()).toString());
            if (optional.isPresent()) {
                this.setExpiryTime();
                this.value = optional.get();
                this.isTokenCaptcha = false;
                return CompletableFuture.completedFuture(null);
            }
            CompletableFuture completableFuture3 = VertxUtil.randomSleep(10000L);
            if (!completableFuture3.isDone()) {
                CompletableFuture completableFuture4 = completableFuture3;
                return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> PXToken.async$checkOrUpdate(this, null, completableFuture4, optional, 2, arg_0));
            }
            completableFuture3.join();
            return this.checkOrUpdate();
        }
        catch (Exception exception) {
            this.logger.error("Unable to keep-alive post: " + exception.getMessage());
        }
        return CompletableFuture.completedFuture(null);
    }

    public void rotateAndRegen() {
        this.rotateProxy();
        this.initialize();
    }

    public void rotateProxy() {
        try {
            RealClient realClient = RealClientFactory.rotateProxy(Vertx.currentContext().owner(), this.client, Controller.PROXY_RESIDENTIAL);
            this.client.close();
            this.client = realClient;
            return;
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public String getDeviceLang() {
        return this.captchaHandler.getDevice().getAcceptLanguage();
    }

    public void setExpiryTime() {
        this.expiryTime = (long)((double)Instant.now().getEpochSecond() + Double.longBitsToDouble(4642627155242306765L));
        this.startTimer();
    }

    @Override
    public String getVid() {
        return this.secondPayload.VID_HEADER;
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$checkOrUpdate(PXToken var0, PXToken var1_1, CompletableFuture var2_3, Optional var3_4, int var4_5, Object var5_6) {
        switch (var4_5) {
            case 0: {
                if (var0.isExpired() == false) return CompletableFuture.completedFuture(null);
                ++var0.expiryCount;
                try {
                    v0 = var0;
                    v1 = var0.sendPayload(var0.secondPayload.asKeepAliveForm());
                    if (!v1.isDone()) {
                        var3_4 = v1;
                        var2_3 = v0;
                        return var3_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$checkOrUpdate(io.trickle.task.antibot.impl.px.PXToken io.trickle.task.antibot.impl.px.PXToken java.util.concurrent.CompletableFuture java.util.Optional int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PXToken)var0, (PXToken)var2_3, (CompletableFuture)var3_4, null, (int)1));
                    }
lbl12:
                    // 3 sources

                    while (true) {
                        var1_1 = v0.parseResult(((JsonObject)v1.join()).toString());
                        if (var1_1.isPresent()) {
                            var0.setExpiryTime();
                            var0.value = var1_1.get();
                            var0.isTokenCaptcha = false;
                            return CompletableFuture.completedFuture(null);
                        }
                        v2 = VertxUtil.randomSleep(10000L);
                        if (!v2.isDone()) {
                            var2_3 = v2;
                            return var2_3.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$checkOrUpdate(io.trickle.task.antibot.impl.px.PXToken io.trickle.task.antibot.impl.px.PXToken java.util.concurrent.CompletableFuture java.util.Optional int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PXToken)var0, null, (CompletableFuture)var2_3, (Optional)var1_1, (int)2));
                        }
                        ** GOTO lbl34
                        break;
                    }
                }
                catch (Exception var1_2) {
                    var0.logger.error("Unable to keep-alive post: " + var1_2.getMessage());
                }
                return CompletableFuture.completedFuture(null);
            }
            case 1: {
                v0 = var1_1;
                v1 = var2_3;
                ** continue;
            }
            case 2: {
                v2 = var2_3;
                var1_1 = var3_4;
lbl34:
                // 2 sources

                v2.join();
                return var0.checkOrUpdate();
            }
        }
        throw new IllegalArgumentException();
    }

    public void startTimer() {
        this.stopTimerEager();
        this.restartClient(this.client);
        if (this.vertx == null) {
            return;
        }
        this.timer = this.vertx.setTimer((this.expiryTime - Instant.now().getEpochSecond()) * 1000L, this::lambda$startTimer$0);
    }

    public Optional parseResult(String string) {
        Matcher matcher = BAKE_PATTERN.matcher(string);
        if (!matcher.find()) return Optional.empty();
        return Optional.of("3:" + matcher.group(1));
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$solveCaptchaDesktop(PXToken var0, String var1_1, String var2_2, String var3_3, CompletableFuture var4_4, int var5_5, Object var6_6) {
        switch (var5_5) {
            case 0: {
                v0 = VertxUtil.hardCodedSleep(7000L);
                if (!v0.isDone()) {
                    var4_4 = v0;
                    return var4_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solveCaptchaDesktop(io.trickle.task.antibot.impl.px.PXToken java.lang.String java.lang.String java.lang.String java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PXToken)var0, (String)var1_1, (String)var2_2, (String)var3_3, (CompletableFuture)var4_4, (int)1));
                }
                ** GOTO lbl10
            }
            case 1: {
                v0 = var4_4;
lbl10:
                // 2 sources

                v0.join();
                return CompletableFuture.completedFuture(null);
            }
        }
        throw new IllegalArgumentException();
    }

    public boolean isExpired() {
        if (Instant.now().getEpochSecond() < this.expiryTime) return false;
        return true;
    }

    public CompletableFuture sendInit(InitPayload initPayload) {
        HttpRequest httpRequest = this.initReq();
        while (this.client.isActive()) {
            try {
                CompletableFuture completableFuture = Request.send(httpRequest, initPayload.asBuffer(this.SITE));
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> PXToken.async$sendInit(this, initPayload, httpRequest, completableFuture2, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse != null) {
                    return CompletableFuture.completedFuture((JsonObject)httpResponse.body());
                }
                CompletableFuture completableFuture3 = VertxUtil.randomSleep(10000L);
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> PXToken.async$sendInit(this, initPayload, httpRequest, completableFuture4, httpResponse, 2, arg_0));
                }
                completableFuture3.join();
            }
            catch (Throwable throwable) {
                // empty catch block
                return CompletableFuture.failedFuture(new Exception("Failed to send payload"));
            }
        }
        return CompletableFuture.failedFuture(new Exception("Failed to send payload"));
    }

    public void setExpiryTimeAfterCap() {
        this.expiryTime = Instant.now().getEpochSecond() + 300L;
        this.startTimer();
    }

    public String getDeviceAcceptEncoding() {
        return this.captchaHandler.getDevice().getAcceptEncoding();
    }

    @Override
    public CompletableFuture awaitInit() {
        return this.initFuture;
    }

    public CompletableFuture sendPayload(MultiMap multiMap) {
        HttpRequest httpRequest = this.collectorRequest();
        while (this.client.isActive()) {
            try {
                long l = System.currentTimeMillis();
                CompletableFuture completableFuture = Request.send(httpRequest, multiMap);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> PXToken.async$sendPayload(this, multiMap, httpRequest, l, completableFuture2, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse != null) {
                    this.requestTime = System.currentTimeMillis() - l;
                    if (this.secondPayload == null) return CompletableFuture.completedFuture((JsonObject)httpResponse.body());
                    this.secondPayload.updatePX349(this.requestTime);
                    return CompletableFuture.completedFuture((JsonObject)httpResponse.body());
                }
                CompletableFuture completableFuture3 = VertxUtil.randomSleep(60000L);
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> PXToken.async$sendPayload(this, multiMap, httpRequest, l, completableFuture4, httpResponse, 2, arg_0));
                }
                completableFuture3.join();
            }
            catch (Throwable throwable) {
                // empty catch block
                return CompletableFuture.failedFuture(new Exception("Failed to send payload"));
            }
        }
        return CompletableFuture.failedFuture(new Exception("Failed to send payload"));
    }

    @Override
    public boolean isTokenCaptcha() {
        return this.isTokenCaptcha;
    }

    public PXToken(TaskActor taskActor, Site site) {
        super(taskActor, ClientType.PX_SDK_PIXEL_3, Controller.PROXY_RESIDENTIAL);
        this.SITE = site;
    }

    public PXCaptcha getCaptchaHandler() {
        return this.captchaHandler;
    }

    @Override
    public String getSid() {
        return this.secondPayload.SID_HEADER;
    }

    public HttpRequest collectorRequest() {
        String string = "";
        Object object = "";
        switch (PXToken$1.$SwitchMap$io$trickle$task$sites$Site[this.SITE.ordinal()]) {
            case 1: {
                string = "PX9Qx3Rve4";
                object = "PerimeterX Android SDK/" + "v1.13.2".substring(1);
                break;
            }
            case 2: {
                string = "PXUArm9B04";
                object = "PerimeterX Android SDK/" + "v1.8.0".substring(1);
                break;
            }
        }
        HttpRequest httpRequest = this.client.postAbs("https://collector-" + string.toLowerCase() + ".perimeterx.net/api/v1/collector/mobile").timeout(TimeUnit.SECONDS.toMillis(15L)).as(BodyCodec.jsonObject());
        httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
        httpRequest.putHeader("user-agent", (String)object);
        httpRequest.putHeader("content-type", "application/x-www-form-urlencoded");
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("accept-encoding", "gzip");
        return httpRequest;
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$reInit(PXToken var0, int var1_1, FirstPayload var2_2, CompletableFuture var3_4, JsonObject var4_5, Throwable var5_6, int var6_7, Object var7_11) {
        switch (var6_7) {
            case 0: {
                var1_1 = 0;
                block8: while (var1_1 < 1) {
                    try {
                        var2_2 = new FirstPayload(var0.secondPayload, var0.secondPayload.sdkInitCount, var0.requestTime, var0.SITE);
                        v0 = var0.sendPayload(var2_2.asForm());
                        if (!v0.isDone()) {
                            var6_8 = v0;
                            return var6_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$reInit(io.trickle.task.antibot.impl.px.PXToken int io.trickle.task.antibot.impl.px.payload.token.FirstPayload java.util.concurrent.CompletableFuture io.vertx.core.json.JsonObject java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PXToken)var0, (int)var1_1, (FirstPayload)var2_2, (CompletableFuture)var6_8, null, null, (int)1));
                        }
lbl11:
                        // 3 sources

                        while (true) {
                            var3_4 = (JsonObject)v0.join();
                            var0.secondPayload = new SecondPayload((FirstPayload)var2_2, (JsonObject)var3_4, var0.requestTime, var0.SITE);
                            v1 = var0.sendPayload(var0.secondPayload.asForm());
                            if (!v1.isDone()) {
                                var6_9 = v1;
                                return var6_9.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$reInit(io.trickle.task.antibot.impl.px.PXToken int io.trickle.task.antibot.impl.px.payload.token.FirstPayload java.util.concurrent.CompletableFuture io.vertx.core.json.JsonObject java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PXToken)var0, (int)var1_1, (FirstPayload)var2_2, (CompletableFuture)var6_9, (JsonObject)var3_4, null, (int)2));
                            }
                            ** GOTO lbl36
                            break;
                        }
                    }
                    catch (Throwable var2_3) {
                        v2 = VertxUtil.randomSleep(10000L);
                        if (!v2.isDone()) {
                            var6_10 = v2;
                            return var6_10.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$reInit(io.trickle.task.antibot.impl.px.PXToken int io.trickle.task.antibot.impl.px.payload.token.FirstPayload java.util.concurrent.CompletableFuture io.vertx.core.json.JsonObject java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PXToken)var0, (int)var1_1, null, (CompletableFuture)var6_10, null, (Throwable)var2_3, (int)3));
                        }
lbl24:
                        // 3 sources

                        while (true) {
                            v2.join();
                            ++var1_1;
                            continue block8;
                            break;
                        }
                    }
                }
                return CompletableFuture.completedFuture(false);
            }
            case 1: {
                v0 = var3_4;
                ** continue;
            }
            case 2: {
                v1 = var3_4;
                var3_4 = var4_5;
lbl36:
                // 2 sources

                if ((var5_6 = var0.parseResult((var4_5 = (JsonObject)v1.join()).toString())).isPresent() == false) throw new Exception("Failed generating token");
                var0.setExpiryTime();
                var0.value = var5_6.get();
                var0.isTokenCaptcha = false;
                var0.expiryCount = 0;
                return CompletableFuture.completedFuture(true);
            }
            case 3: {
                v2 = var3_4;
                var2_2 = var5_6;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$sendPayload(PXToken var0, MultiMap var1_1, HttpRequest var2_2, long var3_3, CompletableFuture var5_5, HttpResponse var6_6, int var7_7, Object var8_8) {
        switch (var7_7) {
            case 0: {
                var2_2 = var0.collectorRequest();
                block7: while (var0.client.isActive() != false) {
                    try {
                        var3_3 = System.currentTimeMillis();
                        v0 = Request.send(var2_2, var1_1);
                        if (!v0.isDone()) {
                            var6_6 = v0;
                            return var6_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$sendPayload(io.trickle.task.antibot.impl.px.PXToken io.vertx.core.MultiMap io.vertx.ext.web.client.HttpRequest long java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PXToken)var0, (MultiMap)var1_1, (HttpRequest)var2_2, (long)var3_3, (CompletableFuture)var6_6, null, (int)1));
                        }
lbl11:
                        // 3 sources

                        while (true) {
                            var5_5 = (HttpResponse)v0.join();
                            if (var5_5 != null) {
                                var0.requestTime = System.currentTimeMillis() - var3_3;
                                if (var0.secondPayload == null) return CompletableFuture.completedFuture((JsonObject)var5_5.body());
                                var0.secondPayload.updatePX349(var0.requestTime);
                                return CompletableFuture.completedFuture((JsonObject)var5_5.body());
                            }
                            v1 = VertxUtil.randomSleep(60000L);
                            if (!v1.isDone()) {
                                var6_6 = v1;
                                return var6_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$sendPayload(io.trickle.task.antibot.impl.px.PXToken io.vertx.core.MultiMap io.vertx.ext.web.client.HttpRequest long java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PXToken)var0, (MultiMap)var1_1, (HttpRequest)var2_2, (long)var3_3, (CompletableFuture)var6_6, (HttpResponse)var5_5, (int)2));
                            }
lbl22:
                            // 3 sources

                            while (true) {
                                v1.join();
                                continue block7;
                                break;
                            }
                            break;
                        }
                    }
                    catch (Throwable var3_4) {
                        // empty catch block
                        return CompletableFuture.failedFuture(new Exception("Failed to send payload"));
                    }
                }
                return CompletableFuture.failedFuture(new Exception("Failed to send payload"));
            }
            case 1: {
                v0 = var5_5;
                ** continue;
            }
            case 2: {
                v1 = var5_5;
                var5_5 = var6_6;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$sendInit(PXToken var0, InitPayload var1_1, HttpRequest var2_2, CompletableFuture var3_3, HttpResponse var4_5, int var5_6, Object var6_7) {
        switch (var5_6) {
            case 0: {
                var2_2 = var0.initReq();
                block7: while (var0.client.isActive() != false) {
                    try {
                        v0 = Request.send(var2_2, var1_1.asBuffer(var0.SITE));
                        if (!v0.isDone()) {
                            var4_5 = v0;
                            return var4_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$sendInit(io.trickle.task.antibot.impl.px.PXToken io.trickle.task.antibot.impl.px.payload.token.InitPayload io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PXToken)var0, (InitPayload)var1_1, (HttpRequest)var2_2, (CompletableFuture)var4_5, null, (int)1));
                        }
lbl10:
                        // 3 sources

                        while (true) {
                            var3_3 = (HttpResponse)v0.join();
                            if (var3_3 != null) {
                                return CompletableFuture.completedFuture((JsonObject)var3_3.body());
                            }
                            v1 = VertxUtil.randomSleep(10000L);
                            if (!v1.isDone()) {
                                var4_5 = v1;
                                return var4_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$sendInit(io.trickle.task.antibot.impl.px.PXToken io.trickle.task.antibot.impl.px.payload.token.InitPayload io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PXToken)var0, (InitPayload)var1_1, (HttpRequest)var2_2, (CompletableFuture)var4_5, (HttpResponse)var3_3, (int)2));
                            }
lbl18:
                            // 3 sources

                            while (true) {
                                v1.join();
                                continue block7;
                                break;
                            }
                            break;
                        }
                    }
                    catch (Throwable var3_4) {
                        // empty catch block
                        return CompletableFuture.failedFuture(new Exception("Failed to send payload"));
                    }
                }
                return CompletableFuture.failedFuture(new Exception("Failed to send payload"));
            }
            case 1: {
                v0 = var3_3;
                ** continue;
            }
            case 2: {
                v1 = var3_3;
                var3_3 = var4_5;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture initBrowserDevice(boolean bl) {
        if (!bl) {
            if (this.captchaHandler != null) return this.captchaHandler.prepareDevice();
        }
        this.captchaHandler = new PXCaptcha(this.logger, this.client, Types.CAPTCHA_DESKTOP, null, null, this.SITE);
        return this.captchaHandler.prepareDevice();
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$initialize(PXToken var0, CompletableFuture var1_1, int var2_4, PXToken var3_9, FirstPayload var4_10, JsonObject var5_11, Throwable var6_12, int var7_13, Object var8_15) {
        switch (var7_13) {
            case 0: {
                v0 = var0.initBrowserDevice(true);
                if (!v0.isDone()) {
                    var6_12 = v0;
                    return var6_12.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$initialize(io.trickle.task.antibot.impl.px.PXToken java.util.concurrent.CompletableFuture int io.trickle.task.antibot.impl.px.PXToken io.trickle.task.antibot.impl.px.payload.token.FirstPayload io.vertx.core.json.JsonObject java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PXToken)var0, (CompletableFuture)var6_12, (int)0, null, null, null, null, (int)1));
                }
lbl7:
                // 3 sources

                while (true) {
                    v0.join();
                    var1_2 = 0;
                    block12: while (var1_2 < 5) {
                        try {
                            var0.hasVidSolved = false;
                            v1 = var0;
                            v2 = null;
                            v3 = null;
                            v4 = InitPayload.getDeviceFromAPI();
                            if (!v4.isDone()) {
                                var7_14 = v4;
                                var6_12 = v1;
                                return var7_14.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$initialize(io.trickle.task.antibot.impl.px.PXToken java.util.concurrent.CompletableFuture int io.trickle.task.antibot.impl.px.PXToken io.trickle.task.antibot.impl.px.payload.token.FirstPayload io.vertx.core.json.JsonObject java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PXToken)var0, (CompletableFuture)var7_14, (int)var1_2, (PXToken)var6_12, null, null, null, (int)2));
                            }
lbl22:
                            // 3 sources

                            while (true) {
                                var6_12 = (Devices$Device)v4.join();
                                v1.initPayload = new InitPayload((Devices$Device)var6_12);
                                v5 = var0.sendInit(var0.initPayload);
                                if (!v5.isDone()) {
                                    var6_12 = v5;
                                    return var6_12.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$initialize(io.trickle.task.antibot.impl.px.PXToken java.util.concurrent.CompletableFuture int io.trickle.task.antibot.impl.px.PXToken io.trickle.task.antibot.impl.px.payload.token.FirstPayload io.vertx.core.json.JsonObject java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PXToken)var0, (CompletableFuture)var6_12, (int)var1_2, null, null, null, null, (int)3));
                                }
lbl29:
                                // 3 sources

                                while (true) {
                                    v5.join();
                                    var2_5 = new FirstPayload(var0.initPayload, var0.SITE);
                                    v6 = var0.sendPayload(var2_5.asForm());
                                    if (!v6.isDone()) {
                                        var6_12 = v6;
                                        return var6_12.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$initialize(io.trickle.task.antibot.impl.px.PXToken java.util.concurrent.CompletableFuture int io.trickle.task.antibot.impl.px.PXToken io.trickle.task.antibot.impl.px.payload.token.FirstPayload io.vertx.core.json.JsonObject java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PXToken)var0, (CompletableFuture)var6_12, (int)var1_2, null, (FirstPayload)var2_5, null, null, (int)4));
                                    }
lbl37:
                                    // 3 sources

                                    while (true) {
                                        var3_9 = (JsonObject)v6.join();
                                        var0.secondPayload = new SecondPayload(var2_5, (JsonObject)var3_9, var0.requestTime, var0.SITE);
                                        v7 = var0.sendPayload(var0.secondPayload.asForm());
                                        if (!v7.isDone()) {
                                            var6_12 = v7;
                                            return var6_12.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$initialize(io.trickle.task.antibot.impl.px.PXToken java.util.concurrent.CompletableFuture int io.trickle.task.antibot.impl.px.PXToken io.trickle.task.antibot.impl.px.payload.token.FirstPayload io.vertx.core.json.JsonObject java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PXToken)var0, (CompletableFuture)var6_12, (int)var1_2, null, (FirstPayload)var2_5, (JsonObject)var3_9, null, (int)5));
                                        }
                                        ** GOTO lbl80
                                        break;
                                    }
                                    break;
                                }
                                break;
                            }
                        }
                        catch (Throwable var2_6) {
                            v8 = VertxUtil.randomSleep(10000L);
                            if (!v8.isDone()) {
                                var6_12 = v8;
                                return var6_12.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$initialize(io.trickle.task.antibot.impl.px.PXToken java.util.concurrent.CompletableFuture int io.trickle.task.antibot.impl.px.PXToken io.trickle.task.antibot.impl.px.payload.token.FirstPayload io.vertx.core.json.JsonObject java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PXToken)var0, (CompletableFuture)var6_12, (int)var1_2, null, null, null, (Throwable)var2_6, (int)6));
                            }
lbl50:
                            // 3 sources

                            while (true) {
                                v8.join();
                                ++var1_2;
                                continue block12;
                                break;
                            }
                        }
                    }
                    return CompletableFuture.completedFuture(false);
                }
            }
            case 1: {
                v0 = var1_1;
                ** continue;
            }
            case 2: {
                v1 = var3_9;
                v3 = null;
                v2 = null;
                v4 = var1_1;
                var1_2 = var2_4;
                ** continue;
            }
            case 3: {
                v5 = var1_1;
                var1_2 = var2_4;
                ** continue;
            }
            case 4: {
                v6 = var1_1;
                var2_5 = var4_10;
                var1_2 = var2_4;
                ** continue;
            }
            case 5: {
                v7 = var1_1;
                var3_9 = var5_11;
                var2_8 = var4_10;
                var1_3 = var2_4;
lbl80:
                // 2 sources

                if ((var5_11 = var0.parseResult((var4_10 = (JsonObject)v7.join()).toString())).isPresent() == false) throw new Exception("Failed generating token");
                var0.setExpiryTime();
                var0.value = var5_11.get();
                var0.isTokenCaptcha = false;
                var0.expiryCount = 0;
                return CompletableFuture.completedFuture(true);
            }
            case 6: {
                v8 = var1_1;
                var2_7 = var6_12;
                var1_2 = var2_4;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public String getDeviceSecUA() {
        return this.captchaHandler.getDevice().getSecUA();
    }

    @Override
    public CompletableFuture reInit() {
        int n = 0;
        while (n < 1) {
            try {
                FirstPayload firstPayload = new FirstPayload(this.secondPayload, this.secondPayload.sdkInitCount, this.requestTime, this.SITE);
                CompletableFuture completableFuture = this.sendPayload(firstPayload.asForm());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> PXToken.async$reInit(this, n, firstPayload, completableFuture2, null, null, 1, arg_0));
                }
                JsonObject jsonObject = (JsonObject)completableFuture.join();
                this.secondPayload = new SecondPayload(firstPayload, jsonObject, this.requestTime, this.SITE);
                CompletableFuture completableFuture3 = this.sendPayload(this.secondPayload.asForm());
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> PXToken.async$reInit(this, n, firstPayload, completableFuture4, jsonObject, null, 2, arg_0));
                }
                JsonObject jsonObject2 = (JsonObject)completableFuture3.join();
                Optional optional = this.parseResult(jsonObject2.toString());
                if (!optional.isPresent()) throw new Exception("Failed generating token");
                this.setExpiryTime();
                this.value = optional.get();
                this.isTokenCaptcha = false;
                this.expiryCount = 0;
                return CompletableFuture.completedFuture(true);
            }
            catch (Throwable throwable) {
                CompletableFuture completableFuture = VertxUtil.randomSleep(10000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture5 = completableFuture;
                    return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> PXToken.async$reInit(this, n, null, completableFuture5, null, throwable, 3, arg_0));
                }
                completableFuture.join();
                ++n;
            }
        }
        return CompletableFuture.completedFuture(false);
    }

    @Override
    public CompletableFuture initialize() {
        CompletableFuture completableFuture = this.initBrowserDevice(true);
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> PXToken.async$initialize(this, completableFuture2, 0, null, null, null, null, 1, arg_0));
        }
        completableFuture.join();
        int n = 0;
        while (n < 5) {
            try {
                this.hasVidSolved = false;
                CompletableFuture completableFuture3 = InitPayload.getDeviceFromAPI();
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    PXToken pXToken = this;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> PXToken.async$initialize(this, completableFuture4, n, pXToken, null, null, null, 2, arg_0));
                }
                Object object = (Devices$Device)completableFuture3.join();
                this.initPayload = new InitPayload((Devices$Device)object);
                CompletableFuture completableFuture5 = this.sendInit(this.initPayload);
                if (!completableFuture5.isDone()) {
                    object = completableFuture5;
                    return ((CompletableFuture)((CompletableFuture)object).exceptionally(Function.identity())).thenCompose(arg_0 -> PXToken.async$initialize(this, (CompletableFuture)object, n, null, null, null, null, 3, arg_0));
                }
                completableFuture5.join();
                FirstPayload firstPayload = new FirstPayload(this.initPayload, this.SITE);
                CompletableFuture completableFuture6 = this.sendPayload(firstPayload.asForm());
                if (!completableFuture6.isDone()) {
                    object = completableFuture6;
                    return ((CompletableFuture)((CompletableFuture)object).exceptionally(Function.identity())).thenCompose(arg_0 -> PXToken.async$initialize(this, (CompletableFuture)object, n, null, firstPayload, null, null, 4, arg_0));
                }
                JsonObject jsonObject = (JsonObject)completableFuture6.join();
                this.secondPayload = new SecondPayload(firstPayload, jsonObject, this.requestTime, this.SITE);
                CompletableFuture completableFuture7 = this.sendPayload(this.secondPayload.asForm());
                if (!completableFuture7.isDone()) {
                    object = completableFuture7;
                    return ((CompletableFuture)((CompletableFuture)object).exceptionally(Function.identity())).thenCompose(arg_0 -> PXToken.async$initialize(this, (CompletableFuture)object, n, null, firstPayload, jsonObject, null, 5, arg_0));
                }
                JsonObject jsonObject2 = (JsonObject)completableFuture7.join();
                Optional optional = this.parseResult(jsonObject2.toString());
                if (!optional.isPresent()) throw new Exception("Failed generating token");
                this.setExpiryTime();
                this.value = optional.get();
                this.isTokenCaptcha = false;
                this.expiryCount = 0;
                return CompletableFuture.completedFuture(true);
            }
            catch (Throwable throwable) {
                CompletableFuture completableFuture8 = VertxUtil.randomSleep(10000L);
                if (!completableFuture8.isDone()) {
                    CompletableFuture completableFuture9 = completableFuture8;
                    return ((CompletableFuture)completableFuture9.exceptionally(Function.identity())).thenCompose(arg_0 -> PXToken.async$initialize(this, completableFuture9, n, null, null, null, throwable, 6, arg_0));
                }
                completableFuture8.join();
                ++n;
            }
        }
        return CompletableFuture.completedFuture(false);
    }

    public void stopTimerEager() {
        if (this.timer == 0L) return;
        this.vertx.cancelTimer(this.timer);
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$handleAfterCap(PXToken var0, FirstPayload var1_1, CompletableFuture var2_3, JsonObject var3_4, int var4_5, Object var5_8) {
        switch (var4_5) {
            case 0: {
                try {
                    var1_1 = new FirstPayload(var0.secondPayload, var0.secondPayload.sdkInitCount, var0.requestTime, var0.SITE);
                    v0 = var0.sendPayload(var1_1.asForm());
                    if (!v0.isDone()) {
                        var4_6 = v0;
                        return var4_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$handleAfterCap(io.trickle.task.antibot.impl.px.PXToken io.trickle.task.antibot.impl.px.payload.token.FirstPayload java.util.concurrent.CompletableFuture io.vertx.core.json.JsonObject int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PXToken)var0, (FirstPayload)var1_1, (CompletableFuture)var4_6, null, (int)1));
                    }
lbl9:
                    // 3 sources

                    while (true) {
                        var2_3 = (JsonObject)v0.join();
                        var0.secondPayload = new SecondPayload(var1_1, (JsonObject)var2_3, var0.requestTime, var0.SITE);
                        v1 = var0.sendPayload(var0.secondPayload.asForm());
                        if (!v1.isDone()) {
                            var4_7 = v1;
                            return var4_7.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$handleAfterCap(io.trickle.task.antibot.impl.px.PXToken io.trickle.task.antibot.impl.px.payload.token.FirstPayload java.util.concurrent.CompletableFuture io.vertx.core.json.JsonObject int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PXToken)var0, (FirstPayload)var1_1, (CompletableFuture)var4_7, (JsonObject)var2_3, (int)2));
                        }
                        ** GOTO lbl26
                        break;
                    }
                }
                catch (Throwable var1_2) {
                    var0.logger.warn("Error on A-CP: {}", (Object)var1_2.getMessage());
                }
                return CompletableFuture.completedFuture(null);
            }
            case 1: {
                v0 = var2_3;
                ** continue;
            }
            case 2: {
                v1 = var2_3;
                var2_3 = var3_4;
lbl26:
                // 2 sources

                var3_4 = (JsonObject)v1.join();
                return CompletableFuture.completedFuture(null);
            }
        }
        throw new IllegalArgumentException();
    }

    public void lambda$retryCheck$1(Long l) {
        Objects.requireNonNull(this);
        if (this.isExpired()) {
            this.checkOrUpdate();
            return;
        }
        this.retryCheck();
    }

    public HttpRequest initReq() {
        HttpRequest httpRequest = this.client.postAbs("https://px-conf.perimeterx.net/api/v1/mobile").timeout(TimeUnit.SECONDS.toMillis(15L)).as(BodyCodec.jsonObject());
        httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
        httpRequest.putHeader("content-type", "application/json; charset=utf-8");
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("accept-encoding", "gzip");
        switch (PXToken$1.$SwitchMap$io$trickle$task$sites$Site[this.SITE.ordinal()]) {
            case 1: {
                httpRequest.putHeader("user-agent", "okhttp/4.9.0");
                return httpRequest;
            }
            case 2: {
                httpRequest.putHeader("user-agent", "okhttp/3.12.1");
                return httpRequest;
            }
        }
        return httpRequest;
    }

    public CompletableFuture handleAfterCap() {
        try {
            FirstPayload firstPayload = new FirstPayload(this.secondPayload, this.secondPayload.sdkInitCount, this.requestTime, this.SITE);
            CompletableFuture completableFuture = this.sendPayload(firstPayload.asForm());
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> PXToken.async$handleAfterCap(this, firstPayload, completableFuture2, null, 1, arg_0));
            }
            JsonObject jsonObject = (JsonObject)completableFuture.join();
            this.secondPayload = new SecondPayload(firstPayload, jsonObject, this.requestTime, this.SITE);
            CompletableFuture completableFuture3 = this.sendPayload(this.secondPayload.asForm());
            if (!completableFuture3.isDone()) {
                CompletableFuture completableFuture4 = completableFuture3;
                return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> PXToken.async$handleAfterCap(this, firstPayload, completableFuture4, jsonObject, 2, arg_0));
            }
            JsonObject jsonObject2 = (JsonObject)completableFuture3.join();
            return CompletableFuture.completedFuture(null);
        }
        catch (Throwable throwable) {
            this.logger.warn("Error on A-CP: {}", (Object)throwable.getMessage());
        }
        return CompletableFuture.completedFuture(null);
    }

    public void setValue(String string) {
        this.value = string;
    }

    public void retryCheck() {
        this.vertx.setTimer(500L, this::lambda$retryCheck$1);
    }

    public boolean hasExpiredOnce() {
        if (this.expiryCount <= 0) return false;
        return true;
    }

    public String getDeviceSecUAMobile() {
        return this.captchaHandler.getDevice().getSecUAMobile();
    }

    public String getDeviceUA() {
        try {
            return this.captchaHandler.getDevice().getUserAgent();
        }
        catch (Throwable throwable) {
            return "";
        }
    }

    public void setTokenCaptcha(boolean bl) {
        this.isTokenCaptcha = bl;
    }

    public void lambda$startTimer$0(Long l) {
        Objects.requireNonNull(this);
        if (this.isExpired()) {
            this.checkOrUpdate();
            return;
        }
        this.retryCheck();
    }

    public CompletableFuture solveCaptchaDesktop(String string, String string2, String string3) {
        CompletableFuture completableFuture = VertxUtil.hardCodedSleep(7000L);
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> PXToken.async$solveCaptchaDesktop(this, string, string2, string3, completableFuture2, 1, arg_0));
        }
        completableFuture.join();
        return CompletableFuture.completedFuture(null);
    }
}

