/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.netty.util.AsciiString
 *  io.vertx.core.MultiMap
 *  io.vertx.core.Vertx
 *  io.vertx.core.buffer.Buffer
 *  io.vertx.core.http.HttpHeaders
 *  io.vertx.core.http.HttpMethod
 *  io.vertx.core.http.RequestOptions
 *  io.vertx.core.json.JsonObject
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.client.HttpResponse
 *  io.vertx.ext.web.codec.BodyCodec
 *  org.apache.logging.log4j.Logger
 */
package io.trickle.task.antibot.impl.px.payload.captcha;

import io.netty.util.AsciiString;
import io.trickle.core.actor.TaskActor;
import io.trickle.task.antibot.impl.px.PerimeterX;
import io.trickle.util.Utils;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.trickle.webclient.TaskApiClient;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.RequestOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.codec.BodyCodec;
import java.lang.invoke.LambdaMetafactory;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Matcher;
import org.apache.logging.log4j.Logger;

public class DesktopPXAPI2
extends PerimeterX {
    public TaskApiClient delegate;
    public static CharSequence KEY;
    public static CharSequence PX3_VALUE;
    public long timerId;
    public String secUA;
    public static CharSequence ONE_VALUE;
    public String userAgent;
    public String pxURI;
    public long expiryTime = 0L;
    public JsonObject metaPayload;
    public static CharSequence CFP_VALUE;
    public String referrer;
    public int failedSolves = 0;
    public static CharSequence DEFAULT_UA;
    public static CharSequence PXDE_VALUE;
    public int delay;
    public static CharSequence FP_VALUE;
    public String currentPayload;
    public static String baseURI;
    public static CharSequence VID_COOKIE;
    public static CharSequence RF_VALUE;
    public JsonObject pxResponse;
    public boolean solvingCaptcha;

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$postPayloadPX(DesktopPXAPI2 var0, int var1_1, RequestOptions var2_2, int var3_3, HttpRequest var4_4, CompletableFuture var5_6, Throwable var6_7, int var7_8, Object var8_9) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [2[CASE]], but top level block is 8[WHILELOOP]
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

    public void scheduleKeepAlive(long l) {
        this.expiryTime = l;
        this.startTimer();
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$solveCaptcha(DesktopPXAPI2 var0, String var1_1, String var2_2, String var3_3, int var4_4, int var5_5, CompletableFuture var6_6, Throwable var7_8, int var8_9, Object var9_16) {
        switch (var8_9) {
            case 0: {
                var0.solvingCaptcha = true;
                var0.pxURI = DesktopPXAPI2.baseURI + "/assets/js/bundle";
                var0.referrer = "https://www.walmart.com/account/login?vid=oaoh&returnUrl=%2F";
                var4_4 = 0;
                var5_5 = 0;
lbl8:
                // 2 sources

                while (var5_5 < 4) {
                    if (var0.failedSolves < 3) ** GOTO lbl19
                    var4_4 = 0;
                    var0.reset();
                    if (var0.userAgent != null) ** GOTO lbl80
                    v0 = var0.initialise();
                    if (!v0.isDone()) {
                        var8_10 = v0;
                        return var8_10.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solveCaptcha(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPXAPI2 java.lang.String java.lang.String java.lang.String int int java.util.concurrent.CompletableFuture java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPXAPI2)var0, (String)var1_1, (String)var2_2, (String)var3_3, (int)var4_4, (int)var5_5, (CompletableFuture)var8_10, null, (int)1));
                    }
                    ** GOTO lbl29
lbl19:
                    // 1 sources

                    var0.mapBlockValues("vid", var1_1);
                    var0.mapBlockValues("uuid", var2_2);
                    v1 = var0.solve1();
                    if (!v1.isDone()) {
                        var8_11 = v1;
                        return var8_11.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solveCaptcha(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPXAPI2 java.lang.String java.lang.String java.lang.String int int java.util.concurrent.CompletableFuture java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPXAPI2)var0, (String)var1_1, (String)var2_2, (String)var3_3, (int)var4_4, (int)var5_5, (CompletableFuture)var8_11, null, (int)2));
                    }
                    ** GOTO lbl33
                }
                ** GOTO lbl80
            }
            {
                default: {
                    throw new IllegalArgumentException();
                }
lbl29:
                // 2 sources

                while (true) {
                    v0.join();
                    ** GOTO lbl80
                    break;
                }
lbl33:
                // 2 sources

                while (true) {
                    v1.join();
                    if (var4_4 != 0) ** GOTO lbl51
                    v2 = var0.solve2();
                    if (!v2.isDone()) {
                        var8_12 = v2;
                        return var8_12.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solveCaptcha(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPXAPI2 java.lang.String java.lang.String java.lang.String int int java.util.concurrent.CompletableFuture java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPXAPI2)var0, (String)var1_1, (String)var2_2, (String)var3_3, (int)var4_4, (int)var5_5, (CompletableFuture)var8_12, null, (int)3));
                    }
lbl41:
                    // 3 sources

                    while (true) {
                        v2.join();
                        v3 = var0.solveImage();
                        if (!v3.isDone()) {
                            var8_13 = v3;
                            return var8_13.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solveCaptcha(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPXAPI2 java.lang.String java.lang.String java.lang.String int int java.util.concurrent.CompletableFuture java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPXAPI2)var0, (String)var1_1, (String)var2_2, (String)var3_3, (int)var4_4, (int)var5_5, (CompletableFuture)var8_13, null, (int)4));
                        }
lbl48:
                        // 3 sources

                        while (true) {
                            v3.join();
lbl51:
                            // 2 sources

                            if (!(v4 = var0.solveHold()).isDone()) {
                                var8_14 = v4;
                                return var8_14.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solveCaptcha(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPXAPI2 java.lang.String java.lang.String java.lang.String int int java.util.concurrent.CompletableFuture java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPXAPI2)var0, (String)var1_1, (String)var2_2, (String)var3_3, (int)var4_4, (int)var5_5, (CompletableFuture)var8_14, null, (int)5));
                            }
lbl54:
                            // 3 sources

                            while (true) {
                                v4.join();
                                var6_6 = var0.onResult(var0.pxResponse.encode());
                                if (var6_6.isPresent()) {
                                    var0.logger.info("Successfully solved captcha");
                                    var0.failedSolves = 0;
                                    var7_8 = MultiMap.caseInsensitiveMultiMap();
                                    var0.parseResultCookies(var0.pxResponse.encode(), (MultiMap)var7_8);
                                    return CompletableFuture.completedFuture(var7_8);
                                }
                                var4_4 = 1;
                                throw new Exception("Invalid captcha base token.");
                            }
                            break;
                        }
                        break;
                    }
                    break;
                }
            }
            catch (Throwable var6_7) {
                ++var0.failedSolves;
                if (!var6_7.getMessage().contains("Invalid captcha base token.")) {
                    var0.logger.warn("Error solving captcha: {}. Retrying...", (Object)var6_7.getMessage());
                    if (var0.logger.isDebugEnabled()) {
                        var0.logger.debug((Object)var6_7);
                    }
                }
                if (!(v5 = VertxUtil.randomSleep(8000L)).isDone()) {
                    var8_15 = v5;
                    return var8_15.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solveCaptcha(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPXAPI2 java.lang.String java.lang.String java.lang.String int int java.util.concurrent.CompletableFuture java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPXAPI2)var0, (String)var1_1, (String)var2_2, (String)var3_3, (int)var4_4, (int)var5_5, (CompletableFuture)var8_15, (Throwable)var6_7, (int)6));
                }
lbl75:
                // 3 sources

                while (true) {
                    v5.join();
                    ++var5_5;
                    ** GOTO lbl8
                    break;
                }
            }
lbl80:
            // 3 sources

            var0.logger.warn("Failed to solve captcha!");
            return CompletableFuture.completedFuture(MultiMap.caseInsensitiveMultiMap());
            case 1: {
                v0 = var6_6;
                ** continue;
            }
            case 2: {
                v1 = var6_6;
                ** continue;
            }
            case 3: {
                v2 = var6_6;
                ** continue;
            }
            case 4: {
                v3 = var6_6;
                ** continue;
            }
            case 5: {
                v4 = var6_6;
                ** continue;
            }
            case 6: 
        }
        v5 = var6_6;
        var6_6 = var7_8;
        ** while (true)
    }

    public DesktopPXAPI2(TaskActor taskActor) {
        super(taskActor, null);
        this.delegate = taskActor.getClient();
        this.reset();
    }

    public CompletableFuture solve2() {
        try {
            CompletableFuture completableFuture = this.getPayload(2);
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXAPI2.async$solve2(this, completableFuture2, 1, arg_0));
            }
            completableFuture.join();
            CompletableFuture completableFuture3 = this.postPayloadPX();
            if (!completableFuture3.isDone()) {
                CompletableFuture completableFuture4 = completableFuture3;
                return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXAPI2.async$solve2(this, completableFuture4, 2, arg_0));
            }
            completableFuture3.join();
            return CompletableFuture.completedFuture(null);
        }
        catch (Throwable throwable) {
            if (!this.logger.isDebugEnabled()) return CompletableFuture.failedFuture(throwable);
            this.logger.debug("Error sending sensor step[2]: {}. Retrying...", (Object)throwable.getMessage(), (Object)throwable);
            return CompletableFuture.failedFuture(throwable);
        }
    }

    public void mapBlockValues(String string, String string2) {
        Objects.requireNonNull(string);
        if (string2 == null) return;
        if (string2.isEmpty()) return;
        if (string2.isBlank()) return;
        JsonObject jsonObject = this.getMetaPayload();
        if (jsonObject == null) return;
        jsonObject.put(string, (Object)string2);
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$solve(DesktopPXAPI2 var0, int var1_1, CompletableFuture var2_2, Throwable var3_4, int var4_5, Object var5_6) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [8[CATCHBLOCK]], but top level block is 13[UNCONDITIONALDOLOOP]
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

    public MultiMap getParams() {
        MultiMap multiMap = HttpHeaders.headers().set((CharSequence)"auth", KEY).set("appId", "PXu6b0qd2S");
        if (this.solvingCaptcha) {
            multiMap.add("captcha", "hold");
        }
        if (this.referrer != null && !this.referrer.isEmpty()) {
            multiMap.add("domain", this.referrer);
            return multiMap;
        }
        multiMap.add("domain", "https://www.walmart.com/");
        return multiMap;
    }

    public HttpRequest getApiRequest(int n) {
        String string = this.getEndpoint(n);
        HttpRequest httpRequest = this.client.postAbs(string).as(BodyCodec.buffer());
        httpRequest.queryParams().setAll(this.getParams());
        httpRequest.putHeader("User-Agent", this.getDeviceUA());
        httpRequest.putHeader("Accept-Encoding", "gzip, deflate");
        httpRequest.putHeader("Accept", "*/*");
        httpRequest.putHeader("Connection", "keep-alive");
        httpRequest.putHeader("Content-Type", "application/json");
        return httpRequest;
    }

    public void startTimer() {
        if (this.vertx == null) {
            if (!this.logger.isDebugEnabled()) return;
            this.logger.debug("Unable to start keepalive timer: Vertx context is null");
            return;
        }
        this.cancelKeepalive();
        this.timerId = this.vertx.setTimer(this.expiryTime, this::lambda$startTimer$0);
        if (this.logger.isDebugEnabled()) return;
        this.logger.debug("Scheduled keep-alive to be sent in {}ms", (Object)this.expiryTime);
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$solveImage(DesktopPXAPI2 var0, CompletableFuture var1_1, int var2_3, Object var3_5) {
        switch (var2_3) {
            case 0: {
                try {
                    v0 = var0.getPayload(3);
                    if (!v0.isDone()) {
                        var4_7 = v0;
                        return var4_7.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solveImage(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPXAPI2 java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPXAPI2)var0, (CompletableFuture)var4_7, (int)1));
                    }
lbl8:
                    // 3 sources

                    while (true) {
                        v0.join();
                        var0.pxURI = DesktopPXAPI2.baseURI + "/b/g";
                        v1 = var0.postPayloadPX(true);
                        if (!v1.isDone()) {
                            var4_8 = v1;
                            return var4_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solveImage(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPXAPI2 java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPXAPI2)var0, (CompletableFuture)var4_8, (int)2));
                        }
                        ** GOTO lbl30
                        break;
                    }
                }
                catch (Throwable var1_2) {
                    if (var0.logger.isDebugEnabled()) {
                        var0.logger.debug("Error sending sensor step[3]: {}. Retrying...", (Object)var1_2.getMessage(), (Object)var1_2);
                    }
                    var2_4 = CompletableFuture.failedFuture(var1_2);
                    return var2_4;
                }
                finally {
                    var0.pxURI = DesktopPXAPI2.baseURI + "/assets/js/bundle";
                }
            }
            case 1: {
                v0 = var1_1;
                ** continue;
            }
            case 2: {
                v1 = var1_1;
lbl30:
                // 2 sources

                v1.join();
                return CompletableFuture.completedFuture(null);
            }
        }
        throw new IllegalArgumentException();
    }

    public Optional onResult(String string) {
        if (!string.contains("cv|0")) {
            return Optional.empty();
        }
        Matcher matcher = BAKE_PATTERN.matcher(string);
        if (!matcher.find()) return Optional.empty();
        return Optional.of("3:" + matcher.group(1));
    }

    public CompletableFuture postPayloadPX() {
        return this.postPayloadPX(false);
    }

    @Override
    public CompletableFuture initialise() {
        int n = 0;
        while (n < 10) {
            try {
                if (this.userAgent == null) {
                    CompletableFuture completableFuture = this.fetchUA();
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture2 = completableFuture;
                        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXAPI2.async$initialise(this, n, completableFuture2, 1, arg_0));
                    }
                    String string = (String)completableFuture.join();
                    if (string != null && !string.isEmpty() && !string.equalsIgnoreCase("error")) {
                        this.userAgent = string;
                        String string2 = Utils.parseChromeVer(string);
                        this.secUA = "\"Google Chrome\";v=\"" + string2 + "\", \" Not;A Brand\";v=\"99\", \"Chromium\";v=\"" + string2 + "\"";
                        this.metaPayload = new JsonObject().put("ua", (Object)this.getDeviceUA());
                        if (!this.logger.isDebugEnabled()) return CompletableFuture.completedFuture(true);
                        this.logger.debug("Initialised API with userAgent: {}", (Object)this.userAgent);
                        return CompletableFuture.completedFuture(true);
                    }
                }
            }
            catch (Throwable throwable) {
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Failed to initialise: {}", (Object)throwable.getMessage());
                }
                this.logger.warn("Failed to initialise API. Retrying...");
            }
            ++n;
        }
        return CompletableFuture.completedFuture(true);
    }

    @Override
    public String getVid() {
        if (this.metaPayload != null) return this.metaPayload.getString("vid", null);
        return null;
    }

    public CompletableFuture solveImage() {
        try {
            CompletableFuture completableFuture = this.getPayload(3);
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXAPI2.async$solveImage(this, completableFuture2, 1, arg_0));
            }
            completableFuture.join();
            this.pxURI = baseURI + "/b/g";
            CompletableFuture completableFuture3 = this.postPayloadPX(true);
            if (!completableFuture3.isDone()) {
                CompletableFuture completableFuture4 = completableFuture3;
                return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXAPI2.async$solveImage(this, completableFuture4, 2, arg_0));
            }
            completableFuture3.join();
            CompletableFuture<Object> completableFuture5 = CompletableFuture.completedFuture(null);
            return completableFuture5;
        }
        catch (Throwable throwable) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Error sending sensor step[3]: {}. Retrying...", (Object)throwable.getMessage(), (Object)throwable);
            }
            CompletableFuture completableFuture = CompletableFuture.failedFuture(throwable);
            return completableFuture;
        }
        finally {
            this.pxURI = baseURI + "/assets/js/bundle";
        }
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$getPayload(DesktopPXAPI2 var0, int var1_1, HttpRequest var2_2, int var3_3, CompletableFuture var4_4, int var5_6, Object var6_8) {
        switch (var5_6) {
            case 0: {
                var2_2 = var0.getApiRequest(var1_1);
                var3_3 = 0;
                while (var3_3 < 30) {
                    try {
                        if (var0.logger.isDebugEnabled()) {
                            var0.logger.debug("Fetching payload step '{}' with meta: {}", (Object)var1_1, (Object)var0.getMetaPayload().encode());
                        }
                        if (!(v0 = Request.send(var2_2, var0.getMetaPayload().toBuffer())).isDone()) {
                            var6_8 = v0;
                            return var6_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$getPayload(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPXAPI2 int io.vertx.ext.web.client.HttpRequest int java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPXAPI2)var0, (int)var1_1, (HttpRequest)var2_2, (int)var3_3, (CompletableFuture)var6_8, (int)1));
                        }
lbl12:
                        // 3 sources

                        while (true) {
                            var4_4 = (HttpResponse)v0.join();
                            if (var4_4 != null) {
                                var5_7 = var4_4.bodyAsJsonObject();
                                if (var0.logger.isDebugEnabled()) {
                                    var0.logger.debug("API payload response received: {}", (Object)var5_7.encode());
                                }
                                var0.currentPayload = var5_7.getString("result");
                                var0.flatMapMeta(var5_7.getJsonObject("meta"));
                                if (var5_7.containsKey("delay") == false) return CompletableFuture.completedFuture(null);
                                var0.delay = (int)(var5_7.getDouble("delay", Double.valueOf(Double.longBitsToDouble(0x3FF0000000000000L))) * Double.longBitsToDouble(4652007308841189376L));
                                return CompletableFuture.completedFuture(null);
                            }
                            break;
                        }
                    }
                    catch (Throwable var4_5) {
                        if (var4_5.getMessage().contains("Unrecognized")) ** GOTO lbl29
                        var0.logger.warn("Error fetching api params: {}. Retrying...", (Object)var4_5.getMessage());
                        if (!var0.logger.isDebugEnabled()) ** GOTO lbl29
                        var0.logger.debug((Object)var4_5);
                    }
lbl29:
                    // 4 sources

                    ++var3_3;
                }
                return CompletableFuture.failedFuture(new Exception("Failed to solve sensor"));
            }
            case 1: {
                v0 = var4_4;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public void scheduleKeepalive() {
        this.scheduleKeepAlive(TimeUnit.MINUTES.toMillis(4L));
    }

    @Override
    public String getDeviceAcceptEncoding() {
        return "gzip";
    }

    @Override
    public CompletableFuture solve() {
        this.solvingCaptcha = false;
        this.pxURI = baseURI + "/api/v2/collector";
        int n = 0;
        while (n < 10) {
            try {
                if (this.userAgent == null) {
                    CompletableFuture completableFuture = this.initialise();
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture2 = completableFuture;
                        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXAPI2.async$solve(this, n, completableFuture2, null, 1, arg_0));
                    }
                    completableFuture.join();
                }
                CompletableFuture completableFuture = this.solve1();
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture3 = completableFuture;
                    return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXAPI2.async$solve(this, n, completableFuture3, null, 2, arg_0));
                }
                completableFuture.join();
                CompletableFuture completableFuture4 = this.solve2();
                if (!completableFuture4.isDone()) {
                    CompletableFuture completableFuture5 = completableFuture4;
                    return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXAPI2.async$solve(this, n, completableFuture5, null, 3, arg_0));
                }
                completableFuture4.join();
                MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
                if (this.getVid() != null && !this.getVid().isBlank()) {
                    multiMap.add(VID_COOKIE, (CharSequence)this.getVid());
                }
                multiMap.add(RF_VALUE, ONE_VALUE);
                multiMap.add(FP_VALUE, ONE_VALUE);
                multiMap.add(CFP_VALUE, ONE_VALUE);
                this.parseResultCookies(this.pxResponse.encode(), multiMap);
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Parsed cookies from normal solve: {}", (Object)multiMap);
                }
                if (!multiMap.isEmpty()) {
                    return CompletableFuture.completedFuture(multiMap);
                }
            }
            catch (Throwable throwable) {
                this.logger.warn("Error solving sensor: {}. Retrying...", (Object)throwable.getMessage());
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug((Object)throwable);
                }
                CompletableFuture completableFuture = VertxUtil.randomSleep(5000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXAPI2.async$solve(this, n, completableFuture6, throwable, 4, arg_0));
                }
                completableFuture.join();
            }
            ++n;
        }
        return CompletableFuture.completedFuture(MultiMap.caseInsensitiveMultiMap());
    }

    public JsonObject getMetaPayload() {
        String string;
        if (this.metaPayload == null) {
            this.metaPayload = new JsonObject().put("ua", (Object)this.getDeviceUA());
        }
        if ((string = this.delegate.getCookies().getCookieValue("_pxhd")) == null) return this.metaPayload;
        if (string.isEmpty()) return this.metaPayload;
        if (string.isBlank()) return this.metaPayload;
        this.metaPayload.put("pxhd", (Object)string);
        if (!this.logger.isDebugEnabled()) return this.metaPayload;
        this.logger.debug("Found '_pxhd' value: {}", (Object)string);
        return this.metaPayload;
    }

    static {
        VID_COOKIE = AsciiString.cached((String)"_pxvid");
        RF_VALUE = AsciiString.cached((String)"_pxff_rf");
        FP_VALUE = AsciiString.cached((String)"_pxff_fp");
        ONE_VALUE = AsciiString.cached((String)"1");
        CFP_VALUE = AsciiString.cached((String)"_pxff_cfp");
        PX3_VALUE = AsciiString.cached((String)"_px3");
        PXDE_VALUE = AsciiString.cached((String)"_pxde");
        DEFAULT_UA = AsciiString.cached((String)"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36");
        KEY = AsciiString.cached((String)"ce3cabed-e10f-43b1-a2a2-8d2a9c2a212f");
        baseURI = "https://collector-" + "PXu6b0qd2S".toLowerCase() + ".px-cloud.net";
    }

    @Override
    public void reset() {
        this.userAgent = null;
        this.referrer = null;
        this.solvingCaptcha = false;
        this.currentPayload = null;
        this.metaPayload = null;
        this.pxResponse = null;
        this.delay = 100;
        this.failedSolves = 0;
    }

    @Override
    public String getDeviceSecUA() {
        return this.secUA;
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$solveHold(DesktopPXAPI2 var0, CompletableFuture var1_1, int var2_3, Object var3_7) {
        switch (var2_3) {
            case 0: {
                try {
                    v0 = var0.getPayload(4);
                    if (!v0.isDone()) {
                        var2_4 = v0;
                        return var2_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solveHold(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPXAPI2 java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPXAPI2)var0, (CompletableFuture)var2_4, (int)1));
                    }
lbl8:
                    // 3 sources

                    while (true) {
                        v0.join();
                        if (var0.logger.isDebugEnabled()) {
                            var0.logger.debug("Sleeping for {}ms before sending payload", (Object)var0.delay);
                        }
                        if (!(v1 = VertxUtil.hardCodedSleep(var0.delay)).isDone()) {
                            var2_5 = v1;
                            return var2_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solveHold(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPXAPI2 java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPXAPI2)var0, (CompletableFuture)var2_5, (int)2));
                        }
lbl16:
                        // 3 sources

                        while (true) {
                            v1.join();
                            v2 = var0.postPayloadPX();
                            if (!v2.isDone()) {
                                var2_6 = v2;
                                return var2_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solveHold(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPXAPI2 java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPXAPI2)var0, (CompletableFuture)var2_6, (int)3));
                            }
                            ** GOTO lbl36
                            break;
                        }
                        break;
                    }
                }
                catch (Throwable var1_2) {
                    if (var0.logger.isDebugEnabled() == false) return CompletableFuture.failedFuture(var1_2);
                    var0.logger.debug("Error sending sensor step[4]: {}. Retrying...", (Object)var1_2.getMessage(), (Object)var1_2);
                    return CompletableFuture.failedFuture(var1_2);
                }
            }
            case 1: {
                v0 = var1_1;
                ** continue;
            }
            case 2: {
                v1 = var1_1;
                ** continue;
            }
            case 3: {
                v2 = var1_1;
lbl36:
                // 2 sources

                v2.join();
                return CompletableFuture.completedFuture(null);
            }
        }
        throw new IllegalArgumentException();
    }

    public void parseResultCookies(String string, MultiMap multiMap) {
        try {
            Matcher matcher = PXDE_PATTERN.matcher(string);
            Matcher matcher2 = BAKE_PATTERN.matcher(string);
            if (matcher2.find()) {
                multiMap.add(PX3_VALUE, (CharSequence)matcher2.group(1));
            }
            if (!matcher.find()) return;
            multiMap.add(PXDE_VALUE, (CharSequence)matcher.group(1));
            return;
        }
        catch (Throwable throwable) {
            if (!this.logger.isDebugEnabled()) return;
            this.logger.debug("Failed to find px3 or pxde ", throwable);
        }
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$initialise(DesktopPXAPI2 var0, int var1_1, CompletableFuture var2_2, int var3_4, Object var4_6) {
        switch (var3_4) {
            case 0: {
                var1_1 = 0;
                while (var1_1 < 10) {
                    try {
                        if (var0.userAgent == null) {
                            v0 = var0.fetchUA();
                            if (!v0.isDone()) {
                                var4_6 = v0;
                                return var4_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$initialise(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPXAPI2 int java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPXAPI2)var0, (int)var1_1, (CompletableFuture)var4_6, (int)1));
                            }
lbl11:
                            // 3 sources

                            while (true) {
                                var2_2 = (String)v0.join();
                                if (var2_2 != null && !var2_2.isEmpty() && !var2_2.equalsIgnoreCase("error")) {
                                    var0.userAgent = var2_2;
                                    var3_5 = Utils.parseChromeVer((String)var2_2);
                                    var0.secUA = "\"Google Chrome\";v=\"" + var3_5 + "\", \" Not;A Brand\";v=\"99\", \"Chromium\";v=\"" + var3_5 + "\"";
                                    var0.metaPayload = new JsonObject().put("ua", (Object)var0.getDeviceUA());
                                    if (var0.logger.isDebugEnabled() == false) return CompletableFuture.completedFuture(true);
                                    var0.logger.debug("Initialised API with userAgent: {}", (Object)var0.userAgent);
                                    return CompletableFuture.completedFuture(true);
                                }
                                break;
                            }
                        }
                    }
                    catch (Throwable var2_3) {
                        if (var0.logger.isDebugEnabled()) {
                            var0.logger.debug("Failed to initialise: {}", (Object)var2_3.getMessage());
                        }
                        var0.logger.warn("Failed to initialise API. Retrying...");
                    }
                    ++var1_1;
                }
                return CompletableFuture.completedFuture(true);
            }
            case 1: {
                v0 = var2_2;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public String getDeviceLang() {
        return "en-GB,en;q=0.9";
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$onKeepalive(DesktopPXAPI2 var0, CompletableFuture var1_1, int var2_3, Object var3_5) {
        switch (var2_3) {
            case 0: {
                try {
                    v0 = var0.solve();
                    if (!v0.isDone()) {
                        var4_6 = v0;
                        return var4_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$onKeepalive(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPXAPI2 java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPXAPI2)var0, (CompletableFuture)var4_6, (int)1));
                    }
lbl8:
                    // 3 sources

                    while (true) {
                        var1_1 = (MultiMap)v0.join();
                        if (var1_1 != null && !var1_1.isEmpty()) {
                            var0.logger.info("Received cookies after keepalive: {}", (Object)var1_1);
                            for (Object var3_5 : var1_1.entries()) {
                                var0.delegate.getWebClient().cookieStore().put((String)var3_5.getKey(), (String)var3_5.getValue(), ".walmart.com");
                            }
                        }
                        var0.scheduleKeepalive();
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

    public CompletableFuture fetchUA() {
        HttpRequest httpRequest = this.client.postAbs("https://px.hwkapi.com/px/ua").as(BodyCodec.string());
        httpRequest.queryParams().setAll(this.getParams());
        if (!this.logger.isDebugEnabled()) return Request.executeTillOk(httpRequest);
        this.logger.debug("Fetching new User-Agent");
        return Request.executeTillOk(httpRequest);
    }

    @Override
    public CompletableFuture solveCaptcha(String string, String string2, String string3) {
        this.solvingCaptcha = true;
        this.pxURI = baseURI + "/assets/js/bundle";
        this.referrer = "https://www.walmart.com/account/login?vid=oaoh&returnUrl=%2F";
        int n = 0;
        for (int i = 0; i < 4; ++i) {
            try {
                if (this.failedSolves >= 3) {
                    n = 0;
                    this.reset();
                    if (this.userAgent != null) break;
                    CompletableFuture completableFuture = this.initialise();
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture2 = completableFuture;
                        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXAPI2.async$solveCaptcha(this, string, string2, string3, n, i, completableFuture2, null, 1, arg_0));
                    }
                    completableFuture.join();
                    break;
                }
                this.mapBlockValues("vid", string);
                this.mapBlockValues("uuid", string2);
                CompletableFuture completableFuture = this.solve1();
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture3 = completableFuture;
                    return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXAPI2.async$solveCaptcha(this, string, string2, string3, n, i, completableFuture3, null, 2, arg_0));
                }
                completableFuture.join();
                if (n == 0) {
                    CompletableFuture completableFuture4 = this.solve2();
                    if (!completableFuture4.isDone()) {
                        CompletableFuture completableFuture5 = completableFuture4;
                        return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXAPI2.async$solveCaptcha(this, string, string2, string3, n, i, completableFuture5, null, 3, arg_0));
                    }
                    completableFuture4.join();
                    CompletableFuture completableFuture6 = this.solveImage();
                    if (!completableFuture6.isDone()) {
                        CompletableFuture completableFuture7 = completableFuture6;
                        return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXAPI2.async$solveCaptcha(this, string, string2, string3, n, i, completableFuture7, null, 4, arg_0));
                    }
                    completableFuture6.join();
                }
                CompletableFuture completableFuture8 = this.solveHold();
                if (!completableFuture8.isDone()) {
                    CompletableFuture completableFuture9 = completableFuture8;
                    return ((CompletableFuture)completableFuture9.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXAPI2.async$solveCaptcha(this, string, string2, string3, n, i, completableFuture9, null, 5, arg_0));
                }
                completableFuture8.join();
                Optional optional = this.onResult(this.pxResponse.encode());
                if (optional.isPresent()) {
                    this.logger.info("Successfully solved captcha");
                    this.failedSolves = 0;
                    MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
                    this.parseResultCookies(this.pxResponse.encode(), multiMap);
                    return CompletableFuture.completedFuture(multiMap);
                }
                n = 1;
                throw new Exception("Invalid captcha base token.");
            }
            catch (Throwable throwable) {
                ++this.failedSolves;
                if (!throwable.getMessage().contains("Invalid captcha base token.")) {
                    this.logger.warn("Error solving captcha: {}. Retrying...", (Object)throwable.getMessage());
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug((Object)throwable);
                    }
                }
                CompletableFuture completableFuture = VertxUtil.randomSleep(8000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture10 = completableFuture;
                    return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXAPI2.async$solveCaptcha(this, string, string2, string3, n, i, completableFuture10, throwable, 6, arg_0));
                }
                completableFuture.join();
                continue;
            }
        }
        this.logger.warn("Failed to solve captcha!");
        return CompletableFuture.completedFuture(MultiMap.caseInsensitiveMultiMap());
    }

    public void cancelKeepalive() {
        if (this.vertx == null) return;
        if (this.timerId == -1L) return;
        if (this.vertx.cancelTimer(this.timerId) && !this.logger.isDebugEnabled()) {
            this.logger.debug("Cancelled keep-alive timer of id '{}' early", (Object)this.timerId);
        }
        this.timerId = -1L;
    }

    public void flatMapMeta(JsonObject jsonObject) {
        if (jsonObject == null) return;
        if (jsonObject.isEmpty()) return;
        jsonObject.stream().forEach(this::lambda$flatMapMeta$1);
    }

    public CompletableFuture postPayloadPX(boolean bl) {
        RequestOptions requestOptions = new RequestOptions();
        if (bl) {
            requestOptions.setAbsoluteURI(this.pxURI);
            requestOptions.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"93\", \" Not;A Brand\";v=\"99\", \"Chromium\";v=\"93\"");
            requestOptions.putHeader("sec-ch-ua-mobile", "?0");
            requestOptions.putHeader("user-agent", this.getDeviceUA());
            requestOptions.putHeader("accept", "*/*");
            requestOptions.putHeader("origin", "https://www.walmart.com");
            requestOptions.putHeader("sec-fetch-site", "cross-site");
            requestOptions.putHeader("sec-fetch-mode", "no-cors");
            requestOptions.putHeader("sec-fetch-dest", "script");
        } else {
            requestOptions.setAbsoluteURI(this.pxURI);
            requestOptions.putHeader("content-length", "DEFAULT_VALUE");
            requestOptions.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"93\", \" Not;A Brand\";v=\"99\", \"Chromium\";v=\"93\"");
            requestOptions.putHeader("sec-ch-ua-mobile", "?1");
            requestOptions.putHeader("user-agent", this.getDeviceUA());
            requestOptions.putHeader("content-type", "application/x-www-form-urlencoded");
            requestOptions.putHeader("accept", "*/*");
            requestOptions.putHeader("origin", "https://www.walmart.com");
            requestOptions.putHeader("sec-fetch-site", "cross-site");
            requestOptions.putHeader("sec-fetch-mode", "cors");
            requestOptions.putHeader("sec-fetch-dest", "empty");
        }
        requestOptions.putHeader("referer", "https://www.walmart.com/");
        requestOptions.putHeader("accept-encoding", "gzip, deflate, br");
        requestOptions.putHeader("accept-language", this.getDeviceLang());
        requestOptions.setTimeout(TimeUnit.SECONDS.toMillis(15L));
        int n = 0;
        while (n < 30) {
            try {
                HttpResponse httpResponse;
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Posting payload: {} to PX via URI: {}", (Object)this.currentPayload, (Object)requestOptions.getURI());
                }
                if (bl) {
                    HttpRequest httpRequest = this.delegate.getWebClient().request(HttpMethod.GET, requestOptions);
                    for (String string : this.currentPayload.split("&")) {
                        String[] stringArray = string.split("=");
                        httpRequest.addQueryParam(stringArray[0], stringArray[1]);
                    }
                    CompletableFuture completableFuture = Request.send(httpRequest);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture2 = completableFuture;
                        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXAPI2.async$postPayloadPX(this, (int)(bl ? 1 : 0), requestOptions, n, httpRequest, completableFuture2, null, 1, arg_0));
                    }
                    httpResponse = (HttpResponse)completableFuture.join();
                } else {
                    CompletableFuture completableFuture = Request.send(this.delegate.getWebClient().request(HttpMethod.POST, requestOptions), Buffer.buffer((String)this.currentPayload));
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture3 = completableFuture;
                        return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXAPI2.async$postPayloadPX(this, (int)(bl ? 1 : 0), requestOptions, n, null, completableFuture3, null, 2, arg_0));
                    }
                    httpResponse = (HttpResponse)completableFuture.join();
                }
                if (httpResponse != null && httpResponse.statusCode() == 200) {
                    if (bl) return CompletableFuture.completedFuture(null);
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug("PX response received: {}", (Object)httpResponse.bodyAsString());
                    }
                    this.pxResponse = httpResponse.bodyAsJsonObject();
                    return CompletableFuture.completedFuture(null);
                }
            }
            catch (Throwable throwable) {
                this.logger.warn("Error sending sensor: {}. Retrying...", (Object)throwable.getMessage());
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug((Object)throwable);
                }
                CompletableFuture completableFuture = VertxUtil.randomSleep(3000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXAPI2.async$postPayloadPX(this, (int)(bl ? 1 : 0), requestOptions, n, null, completableFuture4, throwable, 3, arg_0));
                }
                completableFuture.join();
            }
            ++n;
        }
        return CompletableFuture.failedFuture(new Exception("Failed to send sensor"));
    }

    public void lambda$startTimer$0(Long l) {
        this.onKeepalive();
    }

    public CompletableFuture solveHold() {
        try {
            CompletableFuture completableFuture = this.getPayload(4);
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXAPI2.async$solveHold(this, completableFuture2, 1, arg_0));
            }
            completableFuture.join();
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Sleeping for {}ms before sending payload", (Object)this.delay);
            }
            CompletableFuture completableFuture3 = VertxUtil.hardCodedSleep(this.delay);
            if (!completableFuture3.isDone()) {
                CompletableFuture completableFuture4 = completableFuture3;
                return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXAPI2.async$solveHold(this, completableFuture4, 2, arg_0));
            }
            completableFuture3.join();
            CompletableFuture completableFuture5 = this.postPayloadPX();
            if (!completableFuture5.isDone()) {
                CompletableFuture completableFuture6 = completableFuture5;
                return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXAPI2.async$solveHold(this, completableFuture6, 3, arg_0));
            }
            completableFuture5.join();
            return CompletableFuture.completedFuture(null);
        }
        catch (Throwable throwable) {
            if (!this.logger.isDebugEnabled()) return CompletableFuture.failedFuture(throwable);
            this.logger.debug("Error sending sensor step[4]: {}. Retrying...", (Object)throwable.getMessage(), (Object)throwable);
            return CompletableFuture.failedFuture(throwable);
        }
    }

    public CompletableFuture solve1() {
        try {
            CompletableFuture completableFuture = this.getPayload(1);
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXAPI2.async$solve1(this, completableFuture2, 1, arg_0));
            }
            completableFuture.join();
            CompletableFuture completableFuture3 = this.postPayloadPX();
            if (!completableFuture3.isDone()) {
                CompletableFuture completableFuture4 = completableFuture3;
                return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXAPI2.async$solve1(this, completableFuture4, 2, arg_0));
            }
            completableFuture3.join();
            this.metaPayload.put("a", (Object)this.pxResponse);
            return CompletableFuture.completedFuture(null);
        }
        catch (Throwable throwable) {
            if (!this.logger.isDebugEnabled()) return CompletableFuture.failedFuture(throwable);
            this.logger.debug("Error sending sensor step[1]: {}. Retrying...", (Object)throwable.getMessage(), (Object)throwable);
            return CompletableFuture.failedFuture(throwable);
        }
    }

    public CompletableFuture getPayload(int n) {
        HttpRequest httpRequest = this.getApiRequest(n);
        int n2 = 0;
        while (n2 < 30) {
            block7: {
                try {
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug("Fetching payload step '{}' with meta: {}", (Object)n, (Object)this.getMetaPayload().encode());
                    }
                    CompletableFuture completableFuture = Request.send(httpRequest, this.getMetaPayload().toBuffer());
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture2 = completableFuture;
                        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXAPI2.async$getPayload(this, n, httpRequest, n2, completableFuture2, 1, arg_0));
                    }
                    HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                    if (httpResponse != null) {
                        JsonObject jsonObject = httpResponse.bodyAsJsonObject();
                        if (this.logger.isDebugEnabled()) {
                            this.logger.debug("API payload response received: {}", (Object)jsonObject.encode());
                        }
                        this.currentPayload = jsonObject.getString("result");
                        this.flatMapMeta(jsonObject.getJsonObject("meta"));
                        if (!jsonObject.containsKey("delay")) return CompletableFuture.completedFuture(null);
                        this.delay = (int)(jsonObject.getDouble("delay", Double.valueOf(Double.longBitsToDouble(0x3FF0000000000000L))) * Double.longBitsToDouble(4652007308841189376L));
                        return CompletableFuture.completedFuture(null);
                    }
                }
                catch (Throwable throwable) {
                    if (throwable.getMessage().contains("Unrecognized")) break block7;
                    this.logger.warn("Error fetching api params: {}. Retrying...", (Object)throwable.getMessage());
                    if (!this.logger.isDebugEnabled()) break block7;
                    this.logger.debug((Object)throwable);
                }
            }
            ++n2;
        }
        return CompletableFuture.failedFuture(new Exception("Failed to solve sensor"));
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$solve2(DesktopPXAPI2 var0, CompletableFuture var1_1, int var2_3, Object var3_6) {
        switch (var2_3) {
            case 0: {
                try {
                    v0 = var0.getPayload(2);
                    if (!v0.isDone()) {
                        var2_4 = v0;
                        return var2_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solve2(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPXAPI2 java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPXAPI2)var0, (CompletableFuture)var2_4, (int)1));
                    }
lbl8:
                    // 3 sources

                    while (true) {
                        v0.join();
                        v1 = var0.postPayloadPX();
                        if (!v1.isDone()) {
                            var2_5 = v1;
                            return var2_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solve2(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPXAPI2 java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPXAPI2)var0, (CompletableFuture)var2_5, (int)2));
                        }
                        ** GOTO lbl25
                        break;
                    }
                }
                catch (Throwable var1_2) {
                    if (var0.logger.isDebugEnabled() == false) return CompletableFuture.failedFuture(var1_2);
                    var0.logger.debug("Error sending sensor step[2]: {}. Retrying...", (Object)var1_2.getMessage(), (Object)var1_2);
                    return CompletableFuture.failedFuture(var1_2);
                }
            }
            case 1: {
                v0 = var1_1;
                ** continue;
            }
            case 2: {
                v1 = var1_1;
lbl25:
                // 2 sources

                v1.join();
                return CompletableFuture.completedFuture(null);
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public String getDeviceUA() {
        if (this.userAgent != null) return this.userAgent;
        return DEFAULT_UA.toString();
    }

    public String getEndpoint(int n) {
        switch (n) {
            case 1: {
                return "https://px.hwkapi.com/px/1";
            }
            case 2: {
                return "https://px.hwkapi.com/px/2";
            }
            case 3: {
                return "https://px.hwkapi.com/px/captcha/15";
            }
            case 4: {
                return "https://px.hwkapi.com/px/captcha/hold";
            }
        }
        throw new Exception("Invalid Step: " + n);
    }

    public void scheduleKeepaliveAfterCaptcha() {
        this.scheduleKeepAlive(TimeUnit.MINUTES.toMillis(5L));
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$solve1(DesktopPXAPI2 var0, CompletableFuture var1_1, int var2_3, Object var3_6) {
        switch (var2_3) {
            case 0: {
                try {
                    v0 = var0.getPayload(1);
                    if (!v0.isDone()) {
                        var2_4 = v0;
                        return var2_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solve1(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPXAPI2 java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPXAPI2)var0, (CompletableFuture)var2_4, (int)1));
                    }
lbl8:
                    // 3 sources

                    while (true) {
                        v0.join();
                        v1 = var0.postPayloadPX();
                        if (!v1.isDone()) {
                            var2_5 = v1;
                            return var2_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solve1(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPXAPI2 java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPXAPI2)var0, (CompletableFuture)var2_5, (int)2));
                        }
                        ** GOTO lbl25
                        break;
                    }
                }
                catch (Throwable var1_2) {
                    if (var0.logger.isDebugEnabled() == false) return CompletableFuture.failedFuture(var1_2);
                    var0.logger.debug("Error sending sensor step[1]: {}. Retrying...", (Object)var1_2.getMessage(), (Object)var1_2);
                    return CompletableFuture.failedFuture(var1_2);
                }
            }
            case 1: {
                v0 = var1_1;
                ** continue;
            }
            case 2: {
                v1 = var1_1;
lbl25:
                // 2 sources

                v1.join();
                var0.metaPayload.put("a", (Object)var0.pxResponse);
                return CompletableFuture.completedFuture(null);
            }
        }
        throw new IllegalArgumentException();
    }

    public DesktopPXAPI2(Vertx vertx, Logger logger, TaskApiClient taskApiClient) {
        super(vertx, logger, null);
        this.delegate = taskApiClient;
        this.reset();
    }

    public CompletableFuture onKeepalive() {
        try {
            CompletableFuture completableFuture = this.solve();
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXAPI2.async$onKeepalive(this, completableFuture2, 1, arg_0));
            }
            MultiMap multiMap = (MultiMap)completableFuture.join();
            if (multiMap != null && !multiMap.isEmpty()) {
                this.logger.info("Received cookies after keepalive: {}", (Object)multiMap);
                for (Map.Entry entry : multiMap.entries()) {
                    this.delegate.getWebClient().cookieStore().put((String)entry.getKey(), (String)entry.getValue(), ".walmart.com");
                }
            }
            this.scheduleKeepalive();
            return CompletableFuture.completedFuture(null);
        }
        catch (Throwable throwable) {
            this.logger.warn("Error on keepalive: {}", (Object)throwable.getMessage());
            if (!this.logger.isDebugEnabled()) return CompletableFuture.completedFuture(null);
            this.logger.debug((Object)throwable);
        }
        return CompletableFuture.completedFuture(null);
    }

    public void lambda$flatMapMeta$1(Map.Entry entry) {
        this.metaPayload.put((String)entry.getKey(), entry.getValue());
    }
}

