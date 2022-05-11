/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.core.Controller
 *  io.trickle.core.actor.TaskActor
 *  io.trickle.task.antibot.impl.px.PXTokenAPI2$1
 *  io.trickle.task.antibot.impl.px.PXTokenBase
 *  io.trickle.task.antibot.impl.px.Types
 *  io.trickle.task.antibot.impl.px.payload.captcha.PXCaptcha
 *  io.trickle.task.antibot.impl.px.payload.token.FirstPayload
 *  io.trickle.task.antibot.impl.px.payload.token.InitPayload
 *  io.trickle.task.antibot.impl.px.payload.token.SecondPayload
 *  io.trickle.task.sites.Site
 *  io.trickle.util.concurrent.VertxUtil
 *  io.trickle.util.request.Headers$Pseudo
 *  io.trickle.util.request.Request
 *  io.trickle.webclient.RealClient
 *  io.trickle.webclient.RealClientFactory
 *  io.vertx.core.MultiMap
 *  io.vertx.core.Vertx
 *  io.vertx.core.buffer.Buffer
 *  io.vertx.core.json.JsonObject
 *  io.vertx.core.net.ProxyOptions
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.client.HttpResponse
 *  io.vertx.ext.web.codec.BodyCodec
 */
package io.trickle.task.antibot.impl.px;

import io.trickle.core.Controller;
import io.trickle.core.actor.TaskActor;
import io.trickle.task.antibot.impl.px.PXTokenAPI2;
import io.trickle.task.antibot.impl.px.PXTokenBase;
import io.trickle.task.antibot.impl.px.Types;
import io.trickle.task.antibot.impl.px.payload.captcha.PXCaptcha;
import io.trickle.task.antibot.impl.px.payload.token.FirstPayload;
import io.trickle.task.antibot.impl.px.payload.token.InitPayload;
import io.trickle.task.antibot.impl.px.payload.token.SecondPayload;
import io.trickle.task.sites.Site;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Headers;
import io.trickle.util.request.Request;
import io.trickle.webclient.RealClient;
import io.trickle.webclient.RealClientFactory;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.ProxyOptions;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.codec.BodyCodec;
import java.lang.invoke.LambdaMetafactory;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.WeakHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PXTokenAPI2
extends PXTokenBase {
    public PXCaptcha captchaHandler;
    public InitPayload initPayload;
    public long restartTime = 0L;
    public boolean hasVidSolved;
    public int expiryCount;
    public String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko)";
    public int failedCaptchaSolves = 0;
    public long expiryTime;
    public int rotates = 0;
    public String deviceNumber = "undefined";
    public boolean stopKeepalive = false;
    public JsonObject cookieSesion;
    public SecondPayload secondPayload;
    public long timer;
    public boolean isTokenCaptcha;
    public long requestTime = 0L;
    public static Pattern BAKE_PATTERN = Pattern.compile("bake\\|.*?\\|.*?\\|(.*?)\\|");
    public boolean isFirstTime = true;
    public Site SITE;

    public void startTimer() {
        this.stopTimerEager();
        this.restartClient(this.client);
        if (this.vertx == null) {
            return;
        }
        this.timer = this.vertx.setTimer((this.expiryTime - Instant.now().getEpochSecond()) * 1000L, this::lambda$startTimer$0);
    }

    public void setTokenCaptcha(boolean bl) {
        this.isTokenCaptcha = bl;
    }

    public boolean isTokenCaptcha() {
        return this.isTokenCaptcha;
    }

    public void setValue(String string) {
        this.value = string;
    }

    public HttpRequest apiRequest() {
        return this.client.getAbs("http://94.16.107.91/gen").as(BodyCodec.jsonObject()).addQueryParam("authToken", "PX-2598A000-3595-4305-9244-7C6940349759").addQueryParam("site", this.SITE.toString().toLowerCase()).addQueryParam("region", "com").addQueryParam("proxy", this.getProxyString()).addQueryParam("deviceNumber", this.deviceNumber);
    }

    public String getDeviceUA() {
        return this.userAgent;
    }

    public String getVid() {
        return this.secondPayload.VID_HEADER;
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$sendPayload(PXTokenAPI2 var0, MultiMap var1_1, HttpRequest var2_2, long var3_3, CompletableFuture var5_5, HttpResponse var6_6, int var7_7, Object var8_8) {
        switch (var7_7) {
            case 0: {
                var2_2 = var0.collectorRequest();
                block7: while (var0.client.isActive() != false) {
                    try {
                        var3_3 = System.currentTimeMillis();
                        v0 = Request.send((HttpRequest)var2_2, (MultiMap)var1_1);
                        if (!v0.isDone()) {
                            var6_6 = v0;
                            return var6_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$sendPayload(io.trickle.task.antibot.impl.px.PXTokenAPI2 io.vertx.core.MultiMap io.vertx.ext.web.client.HttpRequest long java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PXTokenAPI2)var0, (MultiMap)var1_1, (HttpRequest)var2_2, (long)var3_3, (CompletableFuture)var6_6, null, (int)1));
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
                            v1 = VertxUtil.randomSleep((long)60000L);
                            if (!v1.isDone()) {
                                var6_6 = v1;
                                return var6_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$sendPayload(io.trickle.task.antibot.impl.px.PXTokenAPI2 io.vertx.core.MultiMap io.vertx.ext.web.client.HttpRequest long java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PXTokenAPI2)var0, (MultiMap)var1_1, (HttpRequest)var2_2, (long)var3_3, (CompletableFuture)var6_6, (HttpResponse)var5_5, (int)2));
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
                        break;
                    }
                }
                return CompletableFuture.failedFuture(new Exception("Failed to send payload"));
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

    public Optional parseResult(String string) {
        Matcher matcher = BAKE_PATTERN.matcher(string);
        if (!matcher.find()) return Optional.empty();
        return Optional.of("3:" + matcher.group(1));
    }

    public CompletableFuture initialize() {
        this.logger.info("Fast init of API[2]");
        return CompletableFuture.completedFuture(true);
    }

    public CompletableFuture solveCaptchaDesktop(String string, String string2, String string3) {
        CompletableFuture completableFuture = VertxUtil.hardCodedSleep((long)7000L);
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> PXTokenAPI2.async$solveCaptchaDesktop(this, string, string2, string3, completableFuture2, null, 1, arg_0));
        }
        completableFuture.join();
        this.captchaHandler.updateVIDandUUID(this.client, string, string2);
        this.captchaHandler.setType(Types.CAPTCHA_DESKTOP);
        CompletableFuture completableFuture3 = this.captchaHandler.solveCaptcha(string3);
        if (!completableFuture3.isDone()) {
            CompletableFuture completableFuture4 = completableFuture3;
            return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> PXTokenAPI2.async$solveCaptchaDesktop(this, string, string2, string3, completableFuture4, null, 2, arg_0));
        }
        String string4 = (String)completableFuture3.join();
        if (string4 != null) {
            this.setExpiryTimeAfterCap();
            this.value = string4;
            this.isTokenCaptcha = true;
            this.hasVidSolved = true;
            this.rotates = 0;
            this.logger.info("Solved captcha successfully!");
            WeakHashMap<String, String> weakHashMap = new WeakHashMap<String, String>();
            weakHashMap.put("_pxff_rf", "1");
            weakHashMap.put("_pxff_fp", "1");
            weakHashMap.put("_pxff_cfp", "1");
            weakHashMap.put("_pxvid", this.captchaHandler.getParentVID());
            PXCaptcha.parseCookiesFromResp((String)string4, weakHashMap);
            if (weakHashMap.isEmpty()) return CompletableFuture.completedFuture(null);
            this.client.close();
            return CompletableFuture.completedFuture(weakHashMap);
        }
        this.logger.warn("Failed to solve captcha!");
        ++this.failedCaptchaSolves;
        if (this.failedCaptchaSolves < 4) return CompletableFuture.completedFuture(null);
        this.rotates = 0;
        this.failedCaptchaSolves = 0;
        this.rotateProxy();
        CompletableFuture completableFuture5 = this.initialize();
        if (!completableFuture5.isDone()) {
            CompletableFuture completableFuture6 = completableFuture5;
            return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> PXTokenAPI2.async$solveCaptchaDesktop(this, string, string2, string3, completableFuture6, string4, 3, arg_0));
        }
        completableFuture5.join();
        this.restartTime = System.currentTimeMillis();
        return CompletableFuture.completedFuture(null);
    }

    public void setExpiryTime() {
        this.expiryTime = (long)((double)Instant.now().getEpochSecond() + Double.longBitsToDouble(4642627155242306765L));
        this.startTimer();
    }

    public CompletableFuture solveCaptchaMobile(String string, String string2, String string3) {
        this.captchaHandler.updateVIDandUUID(this.client, string, string2);
        this.captchaHandler.setType(Types.CAPTCHA_MOBILE);
        this.stopTimerEager();
        if (this.SITE == Site.WALMART) {
            if (this.isFirstTime) {
                CompletableFuture completableFuture = VertxUtil.hardCodedSleep((long)15000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> PXTokenAPI2.async$solveCaptchaMobile(this, string, string2, string3, completableFuture2, null, 1, arg_0));
                }
                completableFuture.join();
                this.isFirstTime = false;
            }
            CompletableFuture completableFuture = this.captchaHandler.solveCaptcha(string3);
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture3 = completableFuture;
                return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> PXTokenAPI2.async$solveCaptchaMobile(this, string, string2, string3, completableFuture3, null, 2, arg_0));
            }
            String string4 = (String)completableFuture.join();
            if (string4 != null) {
                this.setExpiryTimeAfterCap();
                this.value = string4;
                this.isTokenCaptcha = true;
                this.hasVidSolved = true;
                this.rotates = 0;
                this.logger.info("Solved captcha successfully!");
                this.logger.info((String)this.value);
                CompletableFuture completableFuture4 = VertxUtil.hardCodedSleep((long)2222L);
                if (!completableFuture4.isDone()) {
                    CompletableFuture completableFuture5 = completableFuture4;
                    return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> PXTokenAPI2.async$solveCaptchaMobile(this, string, string2, string3, completableFuture5, string4, 3, arg_0));
                }
                completableFuture4.join();
                return CompletableFuture.completedFuture(true);
            }
            this.logger.warn("Failed to solve captcha!");
            ++this.failedCaptchaSolves;
            if (this.failedCaptchaSolves < 4) return CompletableFuture.completedFuture(false);
            this.rotates = 0;
            this.failedCaptchaSolves = 0;
            this.rotateProxy();
            CompletableFuture completableFuture6 = this.initialize();
            if (!completableFuture6.isDone()) {
                CompletableFuture completableFuture7 = completableFuture6;
                return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> PXTokenAPI2.async$solveCaptchaMobile(this, string, string2, string3, completableFuture7, string4, 4, arg_0));
            }
            completableFuture6.join();
            this.restartTime = System.currentTimeMillis();
        } else {
            CompletableFuture completableFuture = this.captchaHandler.solveCaptcha(string3);
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture8 = completableFuture;
                return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> PXTokenAPI2.async$solveCaptchaMobile(this, string, string2, string3, completableFuture8, null, 5, arg_0));
            }
            String string5 = (String)completableFuture.join();
            if (string5 != null) {
                this.setExpiryTimeAfterCap();
                this.value = string5;
                this.isTokenCaptcha = true;
                this.restartTime = 0L;
                this.logger.info("Sent sensor successfully!");
                return CompletableFuture.completedFuture(true);
            }
            this.logger.warn("Failed to send sensor!");
            CompletableFuture completableFuture9 = this.initialize();
            if (!completableFuture9.isDone()) {
                CompletableFuture completableFuture10 = completableFuture9;
                return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> PXTokenAPI2.async$solveCaptchaMobile(this, string, string2, string3, completableFuture10, string5, 6, arg_0));
            }
            completableFuture9.join();
        }
        return CompletableFuture.completedFuture(false);
    }

    public CompletableFuture initBrowserDevice(boolean bl) {
        return CompletableFuture.completedFuture(null);
    }

    public String getDeviceSecUA() {
        return " Not;A Brand\";v=\"99\", \"Google Chrome\";v=\"91\", \"Chromium\";v=\"91";
    }

    public String getProxyString() {
        ProxyOptions proxyOptions = this.client.getOptions().getProxyOptions();
        if (proxyOptions != null) return "http://" + proxyOptions.getUsername() + ":" + proxyOptions.getPassword() + "@" + proxyOptions.getHost() + ":" + proxyOptions.getPort();
        return "";
    }

    public PXTokenAPI2(TaskActor taskActor, Site site) {
        super(taskActor);
        this.SITE = site;
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$solveCaptchaMobile(PXTokenAPI2 var0, String var1_1, String var2_2, String var3_3, CompletableFuture var4_4, String var5_5, int var6_6, Object var7_7) {
        switch (var6_6) {
            case 0: {
                var0.captchaHandler.updateVIDandUUID(var0.client, var1_1, var2_2);
                var0.captchaHandler.setType(Types.CAPTCHA_MOBILE);
                var0.stopTimerEager();
                if (var0.SITE != Site.WALMART) ** GOTO lbl13
                if (!var0.isFirstTime) ** GOTO lbl23
                v0 = VertxUtil.hardCodedSleep((long)15000L);
                if (!v0.isDone()) {
                    var5_5 = v0;
                    return var5_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solveCaptchaMobile(io.trickle.task.antibot.impl.px.PXTokenAPI2 java.lang.String java.lang.String java.lang.String java.util.concurrent.CompletableFuture java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PXTokenAPI2)var0, (String)var1_1, (String)var2_2, (String)var3_3, (CompletableFuture)var5_5, null, (int)1));
                }
                ** GOTO lbl20
lbl13:
                // 1 sources

                v1 = var0.captchaHandler.solveCaptcha(var3_3);
                if (!v1.isDone()) {
                    var5_5 = v1;
                    return var5_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solveCaptchaMobile(io.trickle.task.antibot.impl.px.PXTokenAPI2 java.lang.String java.lang.String java.lang.String java.util.concurrent.CompletableFuture java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PXTokenAPI2)var0, (String)var1_1, (String)var2_2, (String)var3_3, (CompletableFuture)var5_5, null, (int)5));
                }
                ** GOTO lbl69
            }
            case 1: {
                v0 = var4_4;
lbl20:
                // 2 sources

                v0.join();
                var0.isFirstTime = false;
lbl23:
                // 2 sources

                if (!(v2 = var0.captchaHandler.solveCaptcha(var3_3)).isDone()) {
                    var5_5 = v2;
                    return var5_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solveCaptchaMobile(io.trickle.task.antibot.impl.px.PXTokenAPI2 java.lang.String java.lang.String java.lang.String java.util.concurrent.CompletableFuture java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PXTokenAPI2)var0, (String)var1_1, (String)var2_2, (String)var3_3, (CompletableFuture)var5_5, null, (int)2));
                }
                ** GOTO lbl29
            }
            case 2: {
                v2 = var4_4;
lbl29:
                // 2 sources

                if ((var4_4 = (String)v2.join()) == null) ** GOTO lbl42
                var0.setExpiryTimeAfterCap();
                var0.value = var4_4;
                var0.isTokenCaptcha = true;
                var0.hasVidSolved = true;
                var0.rotates = 0;
                var0.logger.info("Solved captcha successfully!");
                var0.logger.info((String)var0.value);
                v3 = VertxUtil.hardCodedSleep((long)2222L);
                if (!v3.isDone()) {
                    var5_5 = v3;
                    return var5_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solveCaptchaMobile(io.trickle.task.antibot.impl.px.PXTokenAPI2 java.lang.String java.lang.String java.lang.String java.util.concurrent.CompletableFuture java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PXTokenAPI2)var0, (String)var1_1, (String)var2_2, (String)var3_3, (CompletableFuture)var5_5, (String)var4_4, (int)3));
                }
                ** GOTO lbl56
lbl42:
                // 1 sources

                var0.logger.warn("Failed to solve captcha!");
                ++var0.failedCaptchaSolves;
                if (var0.failedCaptchaSolves < 4) return CompletableFuture.completedFuture(false);
                var0.rotates = 0;
                var0.failedCaptchaSolves = 0;
                var0.rotateProxy();
                v4 = var0.initialize();
                if (!v4.isDone()) {
                    var5_5 = v4;
                    return var5_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solveCaptchaMobile(io.trickle.task.antibot.impl.px.PXTokenAPI2 java.lang.String java.lang.String java.lang.String java.util.concurrent.CompletableFuture java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PXTokenAPI2)var0, (String)var1_1, (String)var2_2, (String)var3_3, (CompletableFuture)var5_5, (String)var4_4, (int)4));
                }
                ** GOTO lbl62
            }
            case 3: {
                v3 = var4_4;
                var4_4 = var5_5;
lbl56:
                // 2 sources

                v3.join();
                return CompletableFuture.completedFuture(true);
            }
            case 4: {
                v4 = var4_4;
                var4_4 = var5_5;
lbl62:
                // 2 sources

                v4.join();
                var0.restartTime = System.currentTimeMillis();
lbl65:
                // 2 sources

                return CompletableFuture.completedFuture(false);
            }
            case 5: {
                v1 = var4_4;
lbl69:
                // 2 sources

                if ((var4_4 = (String)v1.join()) != null) {
                    var0.setExpiryTimeAfterCap();
                    var0.value = var4_4;
                    var0.isTokenCaptcha = true;
                    var0.restartTime = 0L;
                    var0.logger.info("Sent sensor successfully!");
                    return CompletableFuture.completedFuture(true);
                }
                var0.logger.warn("Failed to send sensor!");
                v5 = var0.initialize();
                if (!v5.isDone()) {
                    var5_5 = v5;
                    return var5_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solveCaptchaMobile(io.trickle.task.antibot.impl.px.PXTokenAPI2 java.lang.String java.lang.String java.lang.String java.util.concurrent.CompletableFuture java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PXTokenAPI2)var0, (String)var1_1, (String)var2_2, (String)var3_3, (CompletableFuture)var5_5, (String)var4_4, (int)6));
                }
                ** GOTO lbl85
            }
            case 6: {
                v5 = var4_4;
                var4_4 = var5_5;
lbl85:
                // 2 sources

                v5.join();
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$solveCaptchaDesktop(PXTokenAPI2 var0, String var1_1, String var2_2, String var3_3, CompletableFuture var4_4, String var5_5, int var6_6, Object var7_10) {
        switch (var6_6) {
            case 0: {
                v0 = VertxUtil.hardCodedSleep((long)7000L);
                if (!v0.isDone()) {
                    var6_7 = v0;
                    return var6_7.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solveCaptchaDesktop(io.trickle.task.antibot.impl.px.PXTokenAPI2 java.lang.String java.lang.String java.lang.String java.util.concurrent.CompletableFuture java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PXTokenAPI2)var0, (String)var1_1, (String)var2_2, (String)var3_3, (CompletableFuture)var6_7, null, (int)1));
                }
                ** GOTO lbl10
            }
            case 1: {
                v0 = var4_4;
lbl10:
                // 2 sources

                v0.join();
                var0.captchaHandler.updateVIDandUUID(var0.client, var1_1, var2_2);
                var0.captchaHandler.setType(Types.CAPTCHA_DESKTOP);
                v1 = var0.captchaHandler.solveCaptcha(var3_3);
                if (!v1.isDone()) {
                    var6_8 = v1;
                    return var6_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solveCaptchaDesktop(io.trickle.task.antibot.impl.px.PXTokenAPI2 java.lang.String java.lang.String java.lang.String java.util.concurrent.CompletableFuture java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PXTokenAPI2)var0, (String)var1_1, (String)var2_2, (String)var3_3, (CompletableFuture)var6_8, null, (int)2));
                }
                ** GOTO lbl21
            }
            case 2: {
                v1 = var4_4;
lbl21:
                // 2 sources

                if ((var4_4 = (String)v1.join()) != null) {
                    var0.setExpiryTimeAfterCap();
                    var0.value = var4_4;
                    var0.isTokenCaptcha = true;
                    var0.hasVidSolved = true;
                    var0.rotates = 0;
                    var0.logger.info("Solved captcha successfully!");
                    var5_5 = new WeakHashMap<K, V>();
                    var5_5.put("_pxff_rf", "1");
                    var5_5.put("_pxff_fp", "1");
                    var5_5.put("_pxff_cfp", "1");
                    var5_5.put("_pxvid", var0.captchaHandler.getParentVID());
                    PXCaptcha.parseCookiesFromResp((String)var4_4, (Map)var5_5);
                    if (var5_5.isEmpty() != false) return CompletableFuture.completedFuture(null);
                    var0.client.close();
                    return CompletableFuture.completedFuture(var5_5);
                }
                var0.logger.warn("Failed to solve captcha!");
                ++var0.failedCaptchaSolves;
                if (var0.failedCaptchaSolves < 4) return CompletableFuture.completedFuture(null);
                var0.rotates = 0;
                var0.failedCaptchaSolves = 0;
                var0.rotateProxy();
                v2 = var0.initialize();
                if (!v2.isDone()) {
                    var6_9 = v2;
                    return var6_9.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solveCaptchaDesktop(io.trickle.task.antibot.impl.px.PXTokenAPI2 java.lang.String java.lang.String java.lang.String java.util.concurrent.CompletableFuture java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PXTokenAPI2)var0, (String)var1_1, (String)var2_2, (String)var3_3, (CompletableFuture)var6_9, (String)var4_4, (int)3));
                }
                ** GOTO lbl55
            }
            case 3: {
                v2 = var4_4;
                var4_4 = var5_5;
lbl55:
                // 2 sources

                v2.join();
                var0.restartTime = System.currentTimeMillis();
                return CompletableFuture.completedFuture(null);
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture solveCaptcha(String string, String string2) {
        return this.solveCaptchaMobile(string, string2, null);
    }

    public boolean hasExpiredOnce() {
        return this.expiryCount > 0;
    }

    public void rotateProxy() {
        try {
            RealClient realClient = RealClientFactory.rotateProxy((Vertx)Vertx.currentContext().owner(), (RealClient)this.client, (Controller)Controller.PROXY_RESIDENTIAL);
            this.client.close();
            this.client = realClient;
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public void rotateAndRegen() {
        this.rotateProxy();
        this.initialize();
    }

    public CompletableFuture desktopSolve(String string, String string2) {
        CompletableFuture completableFuture = this.apiSolve();
        if (completableFuture.isDone()) return CompletableFuture.completedFuture((Map)completableFuture.join());
        CompletableFuture completableFuture2 = completableFuture;
        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> PXTokenAPI2.async$desktopSolve(this, string, string2, completableFuture2, 1, arg_0));
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$sendInit(PXTokenAPI2 var0, InitPayload var1_1, HttpRequest var2_2, CompletableFuture var3_3, HttpResponse var4_5, int var5_6, Object var6_7) {
        switch (var5_6) {
            case 0: {
                var2_2 = var0.initReq();
                block7: while (var0.client.isActive() != false) {
                    try {
                        v0 = Request.send((HttpRequest)var2_2, (Buffer)var1_1.asBuffer(var0.SITE));
                        if (!v0.isDone()) {
                            var4_5 = v0;
                            return var4_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$sendInit(io.trickle.task.antibot.impl.px.PXTokenAPI2 io.trickle.task.antibot.impl.px.payload.token.InitPayload io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PXTokenAPI2)var0, (InitPayload)var1_1, (HttpRequest)var2_2, (CompletableFuture)var4_5, null, (int)1));
                        }
lbl10:
                        // 3 sources

                        while (true) {
                            var3_3 = (HttpResponse)v0.join();
                            if (var3_3 != null) {
                                return CompletableFuture.completedFuture((JsonObject)var3_3.body());
                            }
                            v1 = VertxUtil.randomSleep((long)10000L);
                            if (!v1.isDone()) {
                                var4_5 = v1;
                                return var4_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$sendInit(io.trickle.task.antibot.impl.px.PXTokenAPI2 io.trickle.task.antibot.impl.px.payload.token.InitPayload io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PXTokenAPI2)var0, (InitPayload)var1_1, (HttpRequest)var2_2, (CompletableFuture)var4_5, (HttpResponse)var3_3, (int)2));
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
                        break;
                    }
                }
                return CompletableFuture.failedFuture(new Exception("Failed to send payload"));
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

    public CompletableFuture awaitInit() {
        return this.initFuture;
    }

    public String getDeviceSecUAMobile() {
        return "?0";
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    public static CompletableFuture async$checkOrUpdate(PXTokenAPI2 var0, PXTokenAPI2 var1_1, CompletableFuture var2_3, Optional var3_4, int var4_5, Object var5_6) {
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
                        return var3_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$checkOrUpdate(io.trickle.task.antibot.impl.px.PXTokenAPI2 io.trickle.task.antibot.impl.px.PXTokenAPI2 java.util.concurrent.CompletableFuture java.util.Optional int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PXTokenAPI2)var0, (PXTokenAPI2)var2_3, (CompletableFuture)var3_4, null, (int)1));
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
                        v2 = VertxUtil.randomSleep((long)10000L);
                        if (!v2.isDone()) {
                            var2_3 = v2;
                            return var2_3.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$checkOrUpdate(io.trickle.task.antibot.impl.px.PXTokenAPI2 io.trickle.task.antibot.impl.px.PXTokenAPI2 java.util.concurrent.CompletableFuture java.util.Optional int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PXTokenAPI2)var0, null, (CompletableFuture)var2_3, (Optional)var1_1, (int)2));
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

    public String getDeviceLang() {
        return "en-GB,en;q=0.9,en-US;q=0.8,lt;q=0.7";
    }

    public static CompletableFuture async$desktopSolve(PXTokenAPI2 pXTokenAPI2, String string, String string2, CompletableFuture completableFuture, int n, Object object) {
        switch (n) {
            case 0: {
                CompletableFuture completableFuture2 = pXTokenAPI2.apiSolve();
                CompletableFuture completableFuture3 = completableFuture2;
                if (completableFuture2.isDone()) return CompletableFuture.completedFuture((Map)completableFuture3.join());
                completableFuture = completableFuture3;
                return ((CompletableFuture)completableFuture.exceptionally(Function.identity())).thenCompose(arg_0 -> PXTokenAPI2.async$desktopSolve(pXTokenAPI2, string, string2, completableFuture, 1, arg_0));
            }
            case 1: {
                CompletableFuture completableFuture3 = completableFuture;
                return CompletableFuture.completedFuture((Map)completableFuture3.join());
            }
        }
        throw new IllegalArgumentException();
    }

    public String getSid() {
        return this.secondPayload.SID_HEADER;
    }

    public CompletableFuture handleAfterCap() {
        try {
            FirstPayload firstPayload = new FirstPayload(this.secondPayload, this.secondPayload.sdkInitCount, this.requestTime, this.SITE);
            CompletableFuture completableFuture = this.sendPayload(firstPayload.asForm());
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> PXTokenAPI2.async$handleAfterCap(this, firstPayload, completableFuture2, null, 1, arg_0));
            }
            JsonObject jsonObject = (JsonObject)completableFuture.join();
            this.secondPayload = new SecondPayload(firstPayload, jsonObject, this.requestTime, this.SITE);
            CompletableFuture completableFuture3 = this.sendPayload(this.secondPayload.asForm());
            if (!completableFuture3.isDone()) {
                CompletableFuture completableFuture4 = completableFuture3;
                return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> PXTokenAPI2.async$handleAfterCap(this, firstPayload, completableFuture4, jsonObject, 2, arg_0));
            }
            JsonObject jsonObject2 = (JsonObject)completableFuture3.join();
        }
        catch (Throwable throwable) {
            this.logger.warn("Error on A-CP: {}", (Object)throwable.getMessage());
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$handleAfterCap(PXTokenAPI2 var0, FirstPayload var1_1, CompletableFuture var2_3, JsonObject var3_4, int var4_5, Object var5_8) {
        switch (var4_5) {
            case 0: {
                try {
                    var1_1 = new FirstPayload(var0.secondPayload, var0.secondPayload.sdkInitCount, var0.requestTime, var0.SITE);
                    v0 = var0.sendPayload(var1_1.asForm());
                    if (!v0.isDone()) {
                        var4_6 = v0;
                        return var4_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$handleAfterCap(io.trickle.task.antibot.impl.px.PXTokenAPI2 io.trickle.task.antibot.impl.px.payload.token.FirstPayload java.util.concurrent.CompletableFuture io.vertx.core.json.JsonObject int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PXTokenAPI2)var0, (FirstPayload)var1_1, (CompletableFuture)var4_6, null, (int)1));
                    }
lbl9:
                    // 3 sources

                    while (true) {
                        var2_3 = (JsonObject)v0.join();
                        var0.secondPayload = new SecondPayload(var1_1, (JsonObject)var2_3, var0.requestTime, var0.SITE);
                        v1 = var0.sendPayload(var0.secondPayload.asForm());
                        if (!v1.isDone()) {
                            var4_7 = v1;
                            return var4_7.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$handleAfterCap(io.trickle.task.antibot.impl.px.PXTokenAPI2 io.trickle.task.antibot.impl.px.payload.token.FirstPayload java.util.concurrent.CompletableFuture io.vertx.core.json.JsonObject int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PXTokenAPI2)var0, (FirstPayload)var1_1, (CompletableFuture)var4_7, (JsonObject)var2_3, (int)2));
                        }
lbl16:
                        // 3 sources

                        while (true) {
                            var3_4 = (JsonObject)v1.join();
                            break;
                        }
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
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture checkOrUpdate() {
        if (!this.isExpired()) return CompletableFuture.completedFuture(null);
        ++this.expiryCount;
        try {
            PXTokenAPI2 pXTokenAPI2;
            CompletableFuture completableFuture = this.sendPayload(this.secondPayload.asKeepAliveForm());
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                PXTokenAPI2 pXTokenAPI22 = this;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> PXTokenAPI2.async$checkOrUpdate(this, pXTokenAPI22, completableFuture2, null, 1, arg_0));
            }
            Optional optional = pXTokenAPI2.parseResult(((JsonObject)completableFuture.join()).toString());
            if (optional.isPresent()) {
                this.setExpiryTime();
                this.value = optional.get();
                this.isTokenCaptcha = false;
                return CompletableFuture.completedFuture(null);
            }
            CompletableFuture completableFuture3 = VertxUtil.randomSleep((long)10000L);
            if (!completableFuture3.isDone()) {
                CompletableFuture completableFuture4 = completableFuture3;
                return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> PXTokenAPI2.async$checkOrUpdate(this, null, completableFuture4, optional, 2, arg_0));
            }
            completableFuture3.join();
            return this.checkOrUpdate();
        }
        catch (Exception exception) {
            this.logger.error("Unable to keep-alive post: " + exception.getMessage());
        }
        return CompletableFuture.completedFuture(null);
    }

    public String getDeviceAcceptEncoding() {
        return "gzip, deflate, br";
    }

    public void lambda$startTimer$0(Long l) {
        Objects.requireNonNull(this);
        if (this.isExpired()) {
            this.checkOrUpdate();
        } else {
            this.retryCheck();
        }
    }

    public void stopTimerEager() {
        if (this.timer == 0L) return;
        this.vertx.cancelTimer(this.timer);
    }

    public CompletableFuture sendPayload(MultiMap multiMap) {
        HttpRequest httpRequest = this.collectorRequest();
        while (this.client.isActive()) {
            try {
                long l = System.currentTimeMillis();
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest, (MultiMap)multiMap);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> PXTokenAPI2.async$sendPayload(this, multiMap, httpRequest, l, completableFuture2, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse != null) {
                    this.requestTime = System.currentTimeMillis() - l;
                    if (this.secondPayload == null) return CompletableFuture.completedFuture((JsonObject)httpResponse.body());
                    this.secondPayload.updatePX349(this.requestTime);
                    return CompletableFuture.completedFuture((JsonObject)httpResponse.body());
                }
                CompletableFuture completableFuture3 = VertxUtil.randomSleep((long)60000L);
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> PXTokenAPI2.async$sendPayload(this, multiMap, httpRequest, l, completableFuture4, httpResponse, 2, arg_0));
                }
                completableFuture3.join();
            }
            catch (Throwable throwable) {
                // empty catch block
                break;
            }
        }
        return CompletableFuture.failedFuture(new Exception("Failed to send payload"));
    }

    public CompletableFuture apiSolve() {
        int n = 0;
        while (this.client.isActive()) {
            if (n++ > 100) return CompletableFuture.completedFuture(new WeakHashMap());
            this.logger.info("Genning...");
            try {
                CompletableFuture completableFuture = Request.send((HttpRequest)this.apiRequest());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> PXTokenAPI2.async$apiSolve(this, n, completableFuture2, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                JsonObject jsonObject = (JsonObject)httpResponse.body();
                this.logger.info((Object)jsonObject);
                this.cookieSesion = jsonObject.getJsonObject("data");
                this.userAgent = this.cookieSesion.getString("UserAgent");
                this.deviceNumber = this.cookieSesion.getString("deviceNumber");
                WeakHashMap<String, String> weakHashMap = new WeakHashMap<String, String>();
                String string = jsonObject.getString("cookie").split("=")[0];
                weakHashMap.put(string, jsonObject.getString("cookie").replace(string + "=", ""));
                return CompletableFuture.completedFuture(weakHashMap);
            }
            catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return CompletableFuture.completedFuture(new WeakHashMap());
    }

    public void setNeedsDesktopAppID(boolean bl) {
    }

    public void retryCheck() {
        this.vertx.setTimer(500L, this::lambda$retryCheck$1);
    }

    public CompletableFuture reInit() {
        this.deviceNumber = "undefined";
        return CompletableFuture.completedFuture(true);
    }

    public void lambda$retryCheck$1(Long l) {
        Objects.requireNonNull(this);
        if (this.isExpired()) {
            this.checkOrUpdate();
        } else {
            this.retryCheck();
        }
    }

    public HttpRequest collectorRequest() {
        String string = "";
        Object object = "";
        switch (1.$SwitchMap$io$trickle$task$sites$Site[this.SITE.ordinal()]) {
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
        httpRequest.putHeaders(Headers.Pseudo.MPAS.get());
        httpRequest.putHeader("user-agent", (String)object);
        httpRequest.putHeader("content-type", "application/x-www-form-urlencoded");
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("accept-encoding", "gzip");
        return httpRequest;
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$apiSolve(PXTokenAPI2 var0, int var1_1, CompletableFuture var2_2, int var3_4, Object var4_6) {
        switch (var3_4) {
            case 0: {
                var1_1 = 0;
                block6: while (var0.client.isActive() != false) {
                    if (var1_1++ > 100) return CompletableFuture.completedFuture(new WeakHashMap<K, V>());
                    var0.logger.info("Genning...");
                    try {
                        v0 = Request.send((HttpRequest)var0.apiRequest());
                        if (!v0.isDone()) {
                            var6_7 = v0;
                            return var6_7.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$apiSolve(io.trickle.task.antibot.impl.px.PXTokenAPI2 int java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PXTokenAPI2)var0, (int)var1_1, (CompletableFuture)var6_7, (int)1));
                        }
lbl12:
                        // 3 sources

                        while (true) {
                            var2_2 = (HttpResponse)v0.join();
                            if (var2_2 == null) continue block6;
                            var3_5 = (JsonObject)var2_2.body();
                            var0.logger.info((Object)var3_5);
                            var0.cookieSesion = var3_5.getJsonObject("data");
                            var0.userAgent = var0.cookieSesion.getString("UserAgent");
                            var0.deviceNumber = var0.cookieSesion.getString("deviceNumber");
                            var4_6 = new WeakHashMap<String, String>();
                            var5_8 = var3_5.getString("cookie").split("=")[0];
                            var4_6.put(var5_8, var3_5.getString("cookie").replace(var5_8 + "=", ""));
                            return CompletableFuture.completedFuture(var4_6);
                        }
                    }
                    catch (Throwable var2_3) {
                        var2_3.printStackTrace();
                    }
                }
                return CompletableFuture.completedFuture(new WeakHashMap<K, V>());
            }
            case 1: {
                v0 = var2_2;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public void setExpiryTimeAfterCap() {
        this.expiryTime = Instant.now().getEpochSecond() + 300L;
        this.startTimer();
    }

    public CompletableFuture sendInit(InitPayload initPayload) {
        HttpRequest httpRequest = this.initReq();
        while (this.client.isActive()) {
            try {
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest, (Buffer)initPayload.asBuffer(this.SITE));
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> PXTokenAPI2.async$sendInit(this, initPayload, httpRequest, completableFuture2, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse != null) {
                    return CompletableFuture.completedFuture((JsonObject)httpResponse.body());
                }
                CompletableFuture completableFuture3 = VertxUtil.randomSleep((long)10000L);
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> PXTokenAPI2.async$sendInit(this, initPayload, httpRequest, completableFuture4, httpResponse, 2, arg_0));
                }
                completableFuture3.join();
            }
            catch (Throwable throwable) {
                // empty catch block
                break;
            }
        }
        return CompletableFuture.failedFuture(new Exception("Failed to send payload"));
    }

    public boolean isExpired() {
        return Instant.now().getEpochSecond() >= this.expiryTime;
    }

    public HttpRequest initReq() {
        HttpRequest httpRequest = this.client.postAbs("https://px-conf.perimeterx.net/api/v1/mobile").timeout(TimeUnit.SECONDS.toMillis(15L)).as(BodyCodec.jsonObject());
        httpRequest.putHeaders(Headers.Pseudo.MPAS.get());
        httpRequest.putHeader("content-type", "application/json; charset=utf-8");
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("accept-encoding", "gzip");
        switch (1.$SwitchMap$io$trickle$task$sites$Site[this.SITE.ordinal()]) {
            case 1: {
                httpRequest.putHeader("user-agent", "okhttp/4.9.0");
                break;
            }
            case 2: {
                httpRequest.putHeader("user-agent", "okhttp/3.12.1");
                break;
            }
        }
        return httpRequest;
    }
}
