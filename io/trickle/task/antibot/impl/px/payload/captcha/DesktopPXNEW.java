/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.util.AsciiString
 *  io.trickle.core.actor.TaskActor
 *  io.trickle.task.antibot.impl.px.PerimeterX
 *  io.trickle.task.antibot.impl.px.payload.captcha.DesktopPXNEW$ResponseHandler
 *  io.trickle.task.sites.shopify.ShopifyAPI
 *  io.trickle.util.Utils
 *  io.trickle.util.concurrent.VertxUtil
 *  io.trickle.util.request.Request
 *  io.trickle.webclient.RealClient
 *  io.trickle.webclient.TaskApiClient
 *  io.vertx.core.MultiMap
 *  io.vertx.core.buffer.Buffer
 *  io.vertx.core.http.HttpMethod
 *  io.vertx.core.json.JsonArray
 *  io.vertx.core.json.JsonObject
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.client.HttpResponse
 *  io.vertx.ext.web.client.impl.HttpRequestImpl
 *  io.vertx.ext.web.client.predicate.ResponsePredicate
 *  io.vertx.ext.web.codec.BodyCodec
 */
package io.trickle.task.antibot.impl.px.payload.captcha;

import io.netty.util.AsciiString;
import io.trickle.core.actor.TaskActor;
import io.trickle.task.antibot.impl.px.PerimeterX;
import io.trickle.task.antibot.impl.px.payload.captcha.DesktopPXNEW;
import io.trickle.task.sites.shopify.ShopifyAPI;
import io.trickle.util.Utils;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.trickle.webclient.RealClient;
import io.trickle.webclient.TaskApiClient;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.impl.HttpRequestImpl;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import io.vertx.ext.web.codec.BodyCodec;
import java.lang.invoke.LambdaMetafactory;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DesktopPXNEW
extends PerimeterX {
    public static CharSequence ONE_VALUE;
    public String sid;
    public String userAgent;
    public static CharSequence FP_VALUE;
    public static String ENCODING;
    public long waitTime;
    public static CharSequence CTS;
    public static CharSequence VID_COOKIE;
    public JsonArray performance;
    public static CharSequence CTS_VALUE;
    public static CharSequence RF_VALUE;
    public static CharSequence PX3_VALUE;
    public static CharSequence DEFAULT_SEC_UA;
    public static CharSequence COMPLETION;
    public TaskApiClient delegate;
    public static CharSequence VID;
    public static String LANGUAGE;
    public static CharSequence PXHD_VALUE;
    public static CharSequence QUERY_MOBILE_PARAM;
    public static CharSequence DEVICE;
    public String uuid;
    public static String DEFAULT_DEVICE;
    public static CharSequence PERFORMANCE;
    public String secUA;
    public static CharSequence DEFAULT_UA;
    public static CharSequence PXDE_VALUE;
    public static CharSequence CFP_VALUE;
    public static CharSequence UUID;
    public static CharSequence SID;
    public static CharSequence SEC_UA;
    public String vid;
    public static CharSequence QUERY_PARAM;
    public String deviceNumber = "undefined";

    public void reset() {
        this.secUA = null;
        this.userAgent = null;
        this.uuid = null;
        this.sid = null;
        this.vid = null;
        this.performance = null;
        this.waitTime = 0L;
        this.delegate.rotateProxy();
        this.delegate.getCookies().removeAnyMatch(PXHD_VALUE.toString());
        this.delegate.getCookies().removeAnyMatch(PX3_VALUE.toString());
        this.delegate.getCookies().removeAnyMatch(PXDE_VALUE.toString());
        this.delegate.getCookies().removeAnyMatch(VID_COOKIE.toString());
        this.delegate.getCookies().removeAnyMatch(CTS_VALUE.toString());
        this.deviceNumber = "undefined";
    }

    public String getDeviceSecUA() {
        if (this.secUA != null) return this.secUA;
        return DEFAULT_SEC_UA.toString();
    }

    public boolean needsDevice() {
        return this.userAgent == null || this.secUA == null || this.deviceNumber.equals("undefined");
    }

    public CompletableFuture fetchFreshPxhd() {
        int n = 0;
        if (n++ >= 10) return CompletableFuture.completedFuture(null);
        CompletableFuture completableFuture = Request.send((HttpRequest)this.homepage());
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXNEW.async$fetchFreshPxhd(this, n, completableFuture2, 1, arg_0));
        }
        completableFuture.join();
        return CompletableFuture.completedFuture(null);
    }

    public Supplier apiRequest(int n, boolean bl) {
        switch (n) {
            case -1: {
                return this::lambda$apiRequest$0;
            }
            case 1: {
                return () -> this.lambda$apiRequest$1(bl);
            }
            case 2: {
                return () -> this.lambda$apiRequest$2(bl);
            }
            case 3: {
                return () -> this.lambda$apiRequest$3(bl);
            }
            case 4: {
                return () -> this.lambda$apiRequest$4(bl);
            }
        }
        return DesktopPXNEW::lambda$apiRequest$5;
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$initialise(DesktopPXNEW var0, DesktopPXNEW var1_1, CompletableFuture var2_2, int var3_3, Object var4_4) {
        switch (var3_3) {
            case 0: {
                if (var0.needsDevice() == false) return CompletableFuture.completedFuture(true);
                v0 = var0;
                v1 = var0.executeAPI("Initialising", var0.apiRequest(-1, false), null, (ResponseHandler)LambdaMetafactory.metafactory(null, null, null, (Lio/vertx/ext/web/client/HttpResponse;)Ljava/lang/Object;, bodyAsString(), (Lio/vertx/ext/web/client/HttpResponse;)Ljava/lang/String;)());
                if (!v1.isDone()) {
                    var2_2 = v1;
                    var1_1 = v0;
                    return var2_2.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$initialise(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPXNEW io.trickle.task.antibot.impl.px.payload.captcha.DesktopPXNEW java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPXNEW)var0, (DesktopPXNEW)var1_1, (CompletableFuture)var2_2, (int)1));
                }
                ** GOTO lbl14
            }
            case 1: {
                v0 = var1_1;
                v1 = var2_2;
lbl14:
                // 2 sources

                v0.userAgent = (String)v1.join();
                if (var0.logger.isDebugEnabled() == false) return CompletableFuture.completedFuture(true);
                var0.logger.debug("Fetched device: '{}' with UA: '{}'", (Object)var0.deviceNumber, (Object)var0.userAgent);
                return CompletableFuture.completedFuture(true);
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture executeAPI(String string, Supplier supplier, Object object, ResponseHandler responseHandler) {
        return this._execute(string, supplier, object, responseHandler, true);
    }

    public static HttpRequest lambda$apiRequest$5() {
        return null;
    }

    public String getVid() {
        return this.vid;
    }

    public HttpRequest collectorReq() {
        HttpRequest httpRequest = this.delegate.getWebClient().postAbs("https://collector-pxu6b0qd2s.px-cloud.net/api/v2/collector").expect(ResponsePredicate.SC_OK).as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("sec-ch-ua", this.getDeviceSecUA());
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.getDeviceUA());
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("content-type", "application/x-www-form-urlencoded");
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("origin", "https://www.walmart.com");
        httpRequest.putHeader("sec-fetch-site", "cross-site");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.walmart.com/");
        httpRequest.putHeader("accept-encoding", this.getDeviceAcceptEncoding());
        httpRequest.putHeader("accept-language", this.getDeviceLang());
        return httpRequest;
    }

    public String getDeviceAcceptEncoding() {
        return "gzip, deflate, br";
    }

    public CompletableFuture initialise() {
        if (!this.needsDevice()) return CompletableFuture.completedFuture(true);
        CompletableFuture completableFuture = this.executeAPI("Initialising", this.apiRequest(-1, false), null, HttpResponse::bodyAsString);
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            DesktopPXNEW desktopPXNEW = this;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXNEW.async$initialise(this, desktopPXNEW, completableFuture2, 1, arg_0));
        }
        this.userAgent = (String)completableFuture.join();
        if (!this.logger.isDebugEnabled()) return CompletableFuture.completedFuture(true);
        this.logger.debug("Fetched device: '{}' with UA: '{}'", (Object)this.deviceNumber, (Object)this.userAgent);
        return CompletableFuture.completedFuture(true);
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$_execute(DesktopPXNEW var0, String var1_1, Supplier var2_2, Object var3_3, ResponseHandler var4_4, int var5_5, int var6_6, HttpRequest var7_7, CompletableFuture var8_9, HttpResponse var9_10, Throwable var10_11, int var11_13, Object var12_18) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [8[CATCHBLOCK]], but top level block is 13[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$fetchFreshPxhd(DesktopPXNEW var0, int var1_1, CompletableFuture var2_2, int var3_3, Object var4_4) {
        switch (var3_3) {
            case 0: {
                var1_1 = 0;
                if (var1_1++ >= 10) return CompletableFuture.completedFuture(null);
                v0 = Request.send((HttpRequest)var0.homepage());
                if (!v0.isDone()) {
                    var2_2 = v0;
                    return var2_2.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$fetchFreshPxhd(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPXNEW int java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPXNEW)var0, (int)var1_1, (CompletableFuture)var2_2, (int)1));
                }
                ** GOTO lbl12
            }
            case 1: {
                v0 = var2_2;
lbl12:
                // 2 sources

                v0.join();
                return CompletableFuture.completedFuture(null);
            }
        }
        throw new IllegalArgumentException();
    }

    public String getDeviceLang() {
        return "en-US,en;q=0.9";
    }

    public boolean metaHandler(HttpResponse httpResponse) {
        try {
            if (httpResponse.statusCode() != 200) return false;
            MultiMap multiMap = httpResponse.headers();
            this.deviceNumber = multiMap.get(DEVICE);
            Objects.requireNonNull(this.deviceNumber);
            this.secUA = multiMap.get(SEC_UA);
            if (this.secUA == null) {
                String string = Utils.quickParseFirst((String)httpResponse.bodyAsString(), (Pattern[])new Pattern[]{ShopifyAPI.VER_PATTERN});
                this.secUA = string != null ? "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"" + string + "\", \"Google Chrome\";v=\"" + string + "\"" : "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"96\", \"Google Chrome\";v=\"96\"";
            }
            Objects.requireNonNull(this.secUA);
            if (!multiMap.contains(PERFORMANCE)) return true;
            this.uuid = multiMap.get(UUID);
            this.sid = multiMap.get(SID);
            this.vid = multiMap.get(VID);
            this.waitTime = Long.parseLong(multiMap.get(COMPLETION)) - System.currentTimeMillis();
            this.performance = new JsonArray(multiMap.get(PERFORMANCE));
            if (!multiMap.contains(CTS)) return true;
            this.delegate.getCookies().put(String.valueOf(CTS_VALUE), multiMap.get(CTS), ".walmart.com");
            return true;
        }
        catch (Throwable throwable) {
            this.logger.warn("Failed to parse meta: {}", (Object)throwable.getMessage());
            if (!this.logger.isDebugEnabled()) return false;
            throwable.printStackTrace();
        }
        return false;
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    public static CompletableFuture async$solve(DesktopPXNEW var0, int var1_1, CompletableFuture var2_2, Buffer var3_4, String var4_5, Buffer var5_6, Throwable var6_7, int var7_8, Object var8_17) {
        switch (var7_8) {
            case 0: {
                var1_1 = 0;
                block13: while (var1_1 < 10) {
                    try {
                        if (!var0.needsDevice()) ** GOTO lbl14
                        v0 = var0.initialise();
                        if (!v0.isDone()) {
                            var7_9 = v0;
                            return var7_9.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solve(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPXNEW int java.util.concurrent.CompletableFuture io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPXNEW)var0, (int)var1_1, (CompletableFuture)var7_9, null, null, null, null, (int)1));
                        }
lbl11:
                        // 3 sources

                        while (true) {
                            v0.join();
lbl14:
                            // 2 sources

                            if (!(v1 = var0.executeAPI("API 1", var0.apiRequest(1, false), var0.buildRequestBody(null, null), (ResponseHandler)LambdaMetafactory.metafactory(null, null, null, (Lio/vertx/ext/web/client/HttpResponse;)Ljava/lang/Object;, body(), (Lio/vertx/ext/web/client/HttpResponse;)Lio/vertx/core/buffer/Buffer;)())).isDone()) {
                                var7_10 = v1;
                                return var7_10.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solve(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPXNEW int java.util.concurrent.CompletableFuture io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPXNEW)var0, (int)var1_1, (CompletableFuture)var7_10, null, null, null, null, (int)2));
                            }
lbl17:
                            // 3 sources

                            while (true) {
                                var2_2 = (Buffer)v1.join();
                                v2 = VertxUtil.hardCodedSleep((long)var0.waitTime);
                                if (!v2.isDone()) {
                                    var7_11 = v2;
                                    return var7_11.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solve(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPXNEW int java.util.concurrent.CompletableFuture io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPXNEW)var0, (int)var1_1, (CompletableFuture)var7_11, (Buffer)var2_2, null, null, null, (int)3));
                                }
lbl23:
                                // 3 sources

                                while (true) {
                                    v2.join();
                                    v3 = var0.executePX("Sensor 1/2", (Supplier<HttpRequest>)LambdaMetafactory.metafactory(null, null, null, ()Ljava/lang/Object;, collectorReq(), ()Lio/vertx/ext/web/client/HttpRequest;)((DesktopPXNEW)var0), var2_2, (ResponseHandler)LambdaMetafactory.metafactory(null, null, null, (Lio/vertx/ext/web/client/HttpResponse;)Ljava/lang/Object;, bodyAsString(), (Lio/vertx/ext/web/client/HttpResponse;)Ljava/lang/String;)());
                                    if (!v3.isDone()) {
                                        var7_12 = v3;
                                        return var7_12.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solve(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPXNEW int java.util.concurrent.CompletableFuture io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPXNEW)var0, (int)var1_1, (CompletableFuture)var7_12, (Buffer)var2_2, null, null, null, (int)4));
                                    }
lbl30:
                                    // 3 sources

                                    while (true) {
                                        var3_4 /* !! */  = (String)v3.join();
                                        v4 = var0.executeAPI("API 2", var0.apiRequest(2, false), var0.buildRequestBody((String)var3_4 /* !! */ , var0.performance), (ResponseHandler)LambdaMetafactory.metafactory(null, null, null, (Lio/vertx/ext/web/client/HttpResponse;)Ljava/lang/Object;, body(), (Lio/vertx/ext/web/client/HttpResponse;)Lio/vertx/core/buffer/Buffer;)());
                                        if (!v4.isDone()) {
                                            var7_13 = v4;
                                            return var7_13.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solve(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPXNEW int java.util.concurrent.CompletableFuture io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPXNEW)var0, (int)var1_1, (CompletableFuture)var7_13, (Buffer)var2_2, (String)var3_4 /* !! */ , null, null, (int)5));
                                        }
lbl36:
                                        // 3 sources

                                        while (true) {
                                            var4_5 = (Buffer)v4.join();
                                            v5 = VertxUtil.hardCodedSleep((long)var0.waitTime);
                                            if (!v5.isDone()) {
                                                var7_14 = v5;
                                                return var7_14.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solve(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPXNEW int java.util.concurrent.CompletableFuture io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPXNEW)var0, (int)var1_1, (CompletableFuture)var7_14, (Buffer)var2_2, (String)var3_4 /* !! */ , (Buffer)var4_5, null, (int)6));
                                            }
lbl42:
                                            // 3 sources

                                            while (true) {
                                                v5.join();
                                                v6 = var0.executePX("Sensor 2/2", (Supplier<HttpRequest>)LambdaMetafactory.metafactory(null, null, null, ()Ljava/lang/Object;, collectorReq(), ()Lio/vertx/ext/web/client/HttpRequest;)((DesktopPXNEW)var0), var4_5, (ResponseHandler)LambdaMetafactory.metafactory(null, null, null, (Lio/vertx/ext/web/client/HttpResponse;)Ljava/lang/Object;, bodyAsString(), (Lio/vertx/ext/web/client/HttpResponse;)Ljava/lang/String;)());
                                                if (!v6.isDone()) {
                                                    var7_15 = v6;
                                                    return var7_15.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solve(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPXNEW int java.util.concurrent.CompletableFuture io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPXNEW)var0, (int)var1_1, (CompletableFuture)var7_15, (Buffer)var2_2, (String)var3_4 /* !! */ , (Buffer)var4_5, null, (int)7));
                                                }
lbl49:
                                                // 3 sources

                                                while (true) {
                                                    var5_6 /* !! */  = (String)v6.join();
                                                    var6_7 = MultiMap.caseInsensitiveMultiMap();
                                                    var0.parseResultCookies((String)var5_6 /* !! */ , (MultiMap)var6_7);
                                                    if (var6_7.isEmpty() != false) return CompletableFuture.completedFuture(var6_7);
                                                    if (var0.getVid() != null && !var0.getVid().isBlank()) {
                                                        var6_7.add(DesktopPXNEW.VID_COOKIE, var0.getVid());
                                                    }
                                                    var6_7.add(DesktopPXNEW.RF_VALUE, DesktopPXNEW.ONE_VALUE);
                                                    var6_7.add(DesktopPXNEW.FP_VALUE, DesktopPXNEW.ONE_VALUE);
                                                    var6_7.add(DesktopPXNEW.CFP_VALUE, DesktopPXNEW.ONE_VALUE);
                                                    if (var0.logger.isDebugEnabled() == false) return CompletableFuture.completedFuture(var6_7);
                                                    var0.logger.debug("Parsed cookies from normal solve: {}", (Object)var6_7);
                                                    return CompletableFuture.completedFuture(var6_7);
                                                }
                                                break;
                                            }
                                            break;
                                        }
                                        break;
                                    }
                                    break;
                                }
                                break;
                            }
                            break;
                        }
                    }
                    catch (Throwable var2_3) {
                        var0.logger.warn("Error solving(1) sensor: {}. Retrying...", (Object)var2_3.getMessage());
                        if (var0.logger.isDebugEnabled()) {
                            var2_3.printStackTrace();
                        }
                        if (!(v7 = VertxUtil.randomSleep((long)5000L)).isDone()) {
                            var7_16 = v7;
                            return var7_16.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solve(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPXNEW int java.util.concurrent.CompletableFuture io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPXNEW)var0, (int)var1_1, (CompletableFuture)var7_16, null, null, null, (Throwable)var2_3, (int)8));
                        }
lbl73:
                        // 3 sources

                        while (true) {
                            v7.join();
                            ++var1_1;
                            continue block13;
                            break;
                        }
                    }
                }
                return CompletableFuture.completedFuture(MultiMap.caseInsensitiveMultiMap());
            }
            case 1: {
                v0 = var2_2;
                ** continue;
            }
            case 2: {
                v1 = var2_2;
                ** continue;
            }
            case 3: {
                v2 = var2_2;
                var2_2 = var3_4 /* !! */ ;
                ** continue;
            }
            case 4: {
                v3 = var2_2;
                var2_2 = var3_4 /* !! */ ;
                ** continue;
            }
            case 5: {
                v4 = var2_2;
                v8 = var3_4 /* !! */ ;
                var3_4 /* !! */  = var4_5;
                var2_2 = v8;
                ** continue;
            }
            case 6: {
                v5 = var2_2;
                v9 = var3_4 /* !! */ ;
                v10 = var4_5;
                var4_5 = var5_6 /* !! */ ;
                var3_4 /* !! */  = v10;
                var2_2 = v9;
                ** continue;
            }
            case 7: {
                v6 = var2_2;
                v11 = var3_4 /* !! */ ;
                v12 = var4_5;
                var4_5 = var5_6 /* !! */ ;
                var3_4 /* !! */  = v12;
                var2_2 = v11;
                ** continue;
            }
            case 8: {
                v7 = var2_2;
                var2_2 = var6_7;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public HttpRequest lambda$apiRequest$2(boolean bl) {
        return this.defaultApiRequest("2", bl);
    }

    public HttpRequest lambda$apiRequest$3(boolean bl) {
        return this.defaultApiRequest("img", bl);
    }

    public HttpRequest lambda$apiRequest$4(boolean bl) {
        return this.defaultApiRequest("3", bl);
    }

    public String getDeviceUA() {
        if (this.userAgent != null) return this.userAgent;
        return DEFAULT_UA.toString();
    }

    public void restartClient(RealClient realClient) {
    }

    public HttpRequest lambda$apiRequest$1(boolean bl) {
        return this.defaultApiRequest("1", bl);
    }

    public CompletableFuture solveCaptcha(String string, String string2, String string3) {
        this.uuid = string2;
        this.vid = string;
        int n = 0;
        while (true) {
            if (n > ThreadLocalRandom.current().nextInt(2, 6)) {
                this.delegate.getCookies().removeAnyMatch(PXHD_VALUE.toString());
                return CompletableFuture.completedFuture(MultiMap.caseInsensitiveMultiMap());
            }
            try {
                this.performance = null;
                CompletableFuture completableFuture = this.executeAPI("API 1", this.apiRequest(1, true), this.buildRequestBody(null, null), HttpResponse::body);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXNEW.async$solveCaptcha(this, string, string2, string3, n, completableFuture2, null, null, null, null, null, null, null, null, null, 1, arg_0));
                }
                Buffer buffer = (Buffer)completableFuture.join();
                CompletableFuture completableFuture3 = VertxUtil.hardCodedSleep((long)this.waitTime);
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXNEW.async$solveCaptcha(this, string, string2, string3, n, completableFuture4, buffer, null, null, null, null, null, null, null, null, 2, arg_0));
                }
                completableFuture3.join();
                CompletableFuture completableFuture5 = this.executePX("Sensor 1/4", this::bundleReq, buffer, HttpResponse::bodyAsString);
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXNEW.async$solveCaptcha(this, string, string2, string3, n, completableFuture6, buffer, null, null, null, null, null, null, null, null, 3, arg_0));
                }
                String string4 = (String)completableFuture5.join();
                CompletableFuture completableFuture7 = this.executeAPI("API 2", this.apiRequest(2, true), this.buildRequestBody(string4, this.performance), HttpResponse::body);
                if (!completableFuture7.isDone()) {
                    CompletableFuture completableFuture8 = completableFuture7;
                    return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXNEW.async$solveCaptcha(this, string, string2, string3, n, completableFuture8, buffer, string4, null, null, null, null, null, null, null, 4, arg_0));
                }
                Buffer buffer2 = (Buffer)completableFuture7.join();
                CompletableFuture completableFuture9 = VertxUtil.hardCodedSleep((long)this.waitTime);
                if (!completableFuture9.isDone()) {
                    CompletableFuture completableFuture10 = completableFuture9;
                    return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXNEW.async$solveCaptcha(this, string, string2, string3, n, completableFuture10, buffer, string4, buffer2, null, null, null, null, null, null, 5, arg_0));
                }
                completableFuture9.join();
                CompletableFuture completableFuture11 = this.executePX("Sensor 2/4", this::bundleReq, buffer2, HttpResponse::bodyAsString);
                if (!completableFuture11.isDone()) {
                    CompletableFuture completableFuture12 = completableFuture11;
                    return ((CompletableFuture)completableFuture12.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXNEW.async$solveCaptcha(this, string, string2, string3, n, completableFuture12, buffer, string4, buffer2, null, null, null, null, null, null, 6, arg_0));
                }
                String string5 = (String)completableFuture11.join();
                CompletableFuture completableFuture13 = this.executeAPI("API 3", this.apiRequest(3, true), this.buildRequestBody(string4, this.performance), HttpResponse::body);
                if (!completableFuture13.isDone()) {
                    CompletableFuture completableFuture14 = completableFuture13;
                    return ((CompletableFuture)completableFuture14.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXNEW.async$solveCaptcha(this, string, string2, string3, n, completableFuture14, buffer, string4, buffer2, string5, null, null, null, null, null, 7, arg_0));
                }
                Buffer buffer3 = (Buffer)completableFuture13.join();
                CompletableFuture completableFuture15 = VertxUtil.hardCodedSleep((long)this.waitTime);
                if (!completableFuture15.isDone()) {
                    CompletableFuture completableFuture16 = completableFuture15;
                    return ((CompletableFuture)completableFuture16.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXNEW.async$solveCaptcha(this, string, string2, string3, n, completableFuture16, buffer, string4, buffer2, string5, buffer3, null, null, null, null, 8, arg_0));
                }
                completableFuture15.join();
                CompletableFuture completableFuture17 = this.executePX("Sensor 3/4", () -> this.lambda$solveCaptcha$6(buffer3), null, HttpResponse::bodyAsString);
                if (!completableFuture17.isDone()) {
                    CompletableFuture completableFuture18 = completableFuture17;
                    return ((CompletableFuture)completableFuture18.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXNEW.async$solveCaptcha(this, string, string2, string3, n, completableFuture18, buffer, string4, buffer2, string5, buffer3, null, null, null, null, 9, arg_0));
                }
                String string6 = (String)completableFuture17.join();
                CompletableFuture completableFuture19 = this.executeAPI("API 4", this.apiRequest(4, true), this.buildRequestBody(string4, this.performance), HttpResponse::body);
                if (!completableFuture19.isDone()) {
                    CompletableFuture completableFuture20 = completableFuture19;
                    return ((CompletableFuture)completableFuture20.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXNEW.async$solveCaptcha(this, string, string2, string3, n, completableFuture20, buffer, string4, buffer2, string5, buffer3, string6, null, null, null, 10, arg_0));
                }
                Buffer buffer4 = (Buffer)completableFuture19.join();
                CompletableFuture completableFuture21 = VertxUtil.hardCodedSleep((long)this.waitTime);
                if (!completableFuture21.isDone()) {
                    CompletableFuture completableFuture22 = completableFuture21;
                    return ((CompletableFuture)completableFuture22.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXNEW.async$solveCaptcha(this, string, string2, string3, n, completableFuture22, buffer, string4, buffer2, string5, buffer3, string6, buffer4, null, null, 11, arg_0));
                }
                completableFuture21.join();
                CompletableFuture completableFuture23 = this.executePX("Sensor 4/4", this::bundleReq, buffer4, HttpResponse::bodyAsString);
                if (!completableFuture23.isDone()) {
                    CompletableFuture completableFuture24 = completableFuture23;
                    return ((CompletableFuture)completableFuture24.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXNEW.async$solveCaptcha(this, string, string2, string3, n, completableFuture24, buffer, string4, buffer2, string5, buffer3, string6, buffer4, null, null, 12, arg_0));
                }
                String string7 = (String)completableFuture23.join();
                if (string7 != null && string7.contains("cv|0")) {
                    this.logger.info("Successfully solved captcha!");
                    MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
                    this.parseResultCookies(string7, multiMap);
                    if (this.getVid() != null && !this.getVid().isBlank()) {
                        multiMap.add(VID_COOKIE, (CharSequence)this.getVid());
                    }
                    multiMap.add(RF_VALUE, ONE_VALUE);
                    multiMap.add(FP_VALUE, ONE_VALUE);
                    multiMap.add(CFP_VALUE, ONE_VALUE);
                    if (!this.logger.isDebugEnabled()) return CompletableFuture.completedFuture(multiMap);
                    this.logger.debug("Parsed cookies from captcha solve: {}", (Object)multiMap);
                    return CompletableFuture.completedFuture(multiMap);
                }
                if (n % 2 == 1) {
                    this.reset();
                    CompletableFuture completableFuture25 = this.initialise();
                    if (!completableFuture25.isDone()) {
                        CompletableFuture completableFuture26 = completableFuture25;
                        return ((CompletableFuture)completableFuture26.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXNEW.async$solveCaptcha(this, string, string2, string3, n, completableFuture26, buffer, string4, buffer2, string5, buffer3, string6, buffer4, string7, null, 13, arg_0));
                    }
                    completableFuture25.join();
                    CompletableFuture completableFuture27 = this.fetchFreshPxhd();
                    if (!completableFuture27.isDone()) {
                        CompletableFuture completableFuture28 = completableFuture27;
                        return ((CompletableFuture)completableFuture28.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXNEW.async$solveCaptcha(this, string, string2, string3, n, completableFuture28, buffer, string4, buffer2, string5, buffer3, string6, buffer4, string7, null, 14, arg_0));
                    }
                    completableFuture27.join();
                }
                this.sid = null;
                CompletableFuture completableFuture29 = VertxUtil.randomSleep((long)1000L);
                if (!completableFuture29.isDone()) {
                    CompletableFuture completableFuture30 = completableFuture29;
                    return ((CompletableFuture)completableFuture30.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXNEW.async$solveCaptcha(this, string, string2, string3, n, completableFuture30, buffer, string4, buffer2, string5, buffer3, string6, buffer4, string7, null, 15, arg_0));
                }
                completableFuture29.join();
            }
            catch (Throwable throwable) {
                this.logger.warn("Error solving(2) sensor: {}. Retrying...", (Object)throwable.getMessage());
                if (this.logger.isDebugEnabled()) {
                    throwable.printStackTrace();
                }
                CompletableFuture completableFuture = VertxUtil.randomSleep((long)5000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture31 = completableFuture;
                    return ((CompletableFuture)completableFuture31.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXNEW.async$solveCaptcha(this, string, string2, string3, n, completableFuture31, null, null, null, null, null, null, null, null, throwable, 16, arg_0));
                }
                completableFuture.join();
            }
            ++n;
        }
    }

    public void resetRetainDevice() {
        this.uuid = null;
        this.sid = null;
        this.vid = null;
        this.performance = null;
        this.waitTime = 0L;
        this.delegate.rotateProxy();
        this.delegate.getCookies().removeAnyMatch(PXHD_VALUE.toString());
        this.delegate.getCookies().removeAnyMatch(PX3_VALUE.toString());
        this.delegate.getCookies().removeAnyMatch(PXDE_VALUE.toString());
        this.delegate.getCookies().removeAnyMatch(VID_COOKIE.toString());
        this.delegate.getCookies().removeAnyMatch(CTS_VALUE.toString());
    }

    public HttpRequest homepage() {
        HttpRequest httpRequest = this.delegate.getWebClient().getAbs("https://www.walmart.com/").as(BodyCodec.buffer());
        httpRequest.putHeader("sec-ch-ua", this.getDeviceSecUA());
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.getDeviceUA());
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("service-worker-navigation-preload", "true");
        httpRequest.putHeader("sec-fetch-site", "none");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("accept-encoding", this.getDeviceAcceptEncoding());
        httpRequest.putHeader("accept-language", this.getDeviceLang());
        return httpRequest;
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
        }
        catch (Throwable throwable) {
            if (!this.logger.isDebugEnabled()) return;
            this.logger.warn("Failed to find px3 or pxde");
            throwable.printStackTrace();
        }
    }

    public HttpRequest lambda$solveCaptcha$6(Buffer buffer) {
        return this.imageReq(buffer);
    }

    public HttpRequest lambda$apiRequest$0() {
        return this.client.getAbs(DesktopPXNEW.getApiURI("ua"));
    }

    static {
        DEFAULT_DEVICE = "undefined";
        LANGUAGE = "en-US,en;q=0.9";
        ENCODING = "gzip, deflate, br";
        QUERY_PARAM = AsciiString.cached((String)"captcha");
        QUERY_MOBILE_PARAM = AsciiString.cached((String)"mobile");
        PERFORMANCE = AsciiString.cached((String)"performance");
        UUID = AsciiString.cached((String)"uuid");
        VID = AsciiString.cached((String)"vid");
        SID = AsciiString.cached((String)"sid");
        CTS = AsciiString.cached((String)"CTS");
        COMPLETION = AsciiString.cached((String)"completionEpoch");
        DEVICE = AsciiString.cached((String)"device");
        SEC_UA = AsciiString.cached((String)"sec-ua");
        DEFAULT_UA = AsciiString.cached((String)"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.45 Safari/537.36");
        DEFAULT_SEC_UA = AsciiString.cached((String)"\"Google Chrome\";v=\"95\", \"Chromium\";v=\"95\", \";Not A Brand\";v=\"99\"");
        CTS_VALUE = AsciiString.cached((String)"pxcts");
        VID_COOKIE = AsciiString.cached((String)"_pxvid");
        RF_VALUE = AsciiString.cached((String)"_pxff_rf");
        FP_VALUE = AsciiString.cached((String)"_pxff_fp");
        ONE_VALUE = AsciiString.cached((String)"1");
        CFP_VALUE = AsciiString.cached((String)"_pxff_cfp");
        PXHD_VALUE = AsciiString.cached((String)"_pxhd");
        PX3_VALUE = AsciiString.cached((String)"_px3");
        PXDE_VALUE = AsciiString.cached((String)"_pxde");
    }

    public CompletableFuture _execute(String string, Supplier supplier, Object object, ResponseHandler responseHandler, boolean bl) {
        int n = 0;
        while (n++ < 10) {
            try {
                HttpResponse httpResponse;
                HttpRequest httpRequest = (HttpRequest)supplier.get();
                if (((HttpRequestImpl)httpRequest).method().equals((Object)HttpMethod.POST)) {
                    CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest, (Object)object);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture2 = completableFuture;
                        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXNEW.async$_execute(this, string, (Supplier)supplier, object, responseHandler, (int)(bl ? 1 : 0), n, httpRequest, completableFuture2, null, null, 1, arg_0));
                    }
                    httpResponse = (HttpResponse)completableFuture.join();
                } else {
                    CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture3 = completableFuture;
                        return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXNEW.async$_execute(this, string, (Supplier)supplier, object, responseHandler, (int)(bl ? 1 : 0), n, httpRequest, completableFuture3, null, null, 2, arg_0));
                    }
                    httpResponse = (HttpResponse)completableFuture.join();
                }
                if (httpResponse != null) {
                    Optional<Object> optional;
                    Optional<Object> optional2 = bl ? (this.metaHandler(httpResponse) ? Optional.ofNullable(responseHandler.handle(httpResponse)) : Optional.empty()) : (optional = Optional.ofNullable(responseHandler.handle(httpResponse)));
                    if (optional.isPresent()) {
                        return CompletableFuture.completedFuture(optional.get());
                    }
                    int n2 = httpResponse.statusCode();
                    this.logger.warn("Failed {}: '{}'", (Object)string.toLowerCase(Locale.ROOT), (Object)n2);
                } else {
                    this.logger.error("Failed to execute: {}", (Object)string.toLowerCase(Locale.ROOT));
                }
                CompletableFuture completableFuture = VertxUtil.randomSleep((long)5000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXNEW.async$_execute(this, string, (Supplier)supplier, object, responseHandler, (int)(bl ? 1 : 0), n, httpRequest, completableFuture4, httpResponse, null, 3, arg_0));
                }
                completableFuture.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error " + string.toLowerCase(Locale.ROOT) + ": {}", (Object)throwable.getMessage());
                if (this.logger.isDebugEnabled()) {
                    throwable.printStackTrace();
                }
                CompletableFuture completableFuture = VertxUtil.randomSleep((long)5000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture5 = completableFuture;
                    return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXNEW.async$_execute(this, string, (Supplier)supplier, object, responseHandler, (int)(bl ? 1 : 0), n, null, completableFuture5, null, throwable, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.failedFuture(new Exception("Failed to execute " + string));
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$solveCaptcha(DesktopPXNEW var0, String var1_1, String var2_2, String var3_3, int var4_4, CompletableFuture var5_5, Buffer var6_7, String var7_8, Buffer var8_9, String var9_10, Buffer var10_11, String var11_12, Buffer var12_13, String var13_14, Throwable var14_15, int var15_16, Object var16_17) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [20[CATCHBLOCK]], but top level block is 37[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public HttpRequest defaultApiRequest(String string, boolean bl) {
        HttpRequest httpRequest = this.client.postAbs(DesktopPXNEW.getApiURI(string)).addQueryParam("mobile", "false");
        if (!bl) return httpRequest;
        httpRequest.addQueryParam("captcha", "hold");
        return httpRequest;
    }

    public DesktopPXNEW(TaskActor taskActor) {
        super(taskActor, null);
        this.delegate = taskActor.getClient();
    }

    public CompletableFuture solve() {
        int n = 0;
        while (n < 10) {
            try {
                if (this.needsDevice()) {
                    CompletableFuture completableFuture = this.initialise();
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture2 = completableFuture;
                        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXNEW.async$solve(this, n, completableFuture2, null, null, null, null, 1, arg_0));
                    }
                    completableFuture.join();
                }
                CompletableFuture completableFuture = this.executeAPI("API 1", this.apiRequest(1, false), this.buildRequestBody(null, null), HttpResponse::body);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture3 = completableFuture;
                    return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXNEW.async$solve(this, n, completableFuture3, null, null, null, null, 2, arg_0));
                }
                Buffer buffer = (Buffer)completableFuture.join();
                CompletableFuture completableFuture4 = VertxUtil.hardCodedSleep((long)this.waitTime);
                if (!completableFuture4.isDone()) {
                    CompletableFuture completableFuture5 = completableFuture4;
                    return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXNEW.async$solve(this, n, completableFuture5, buffer, null, null, null, 3, arg_0));
                }
                completableFuture4.join();
                CompletableFuture completableFuture6 = this.executePX("Sensor 1/2", this::collectorReq, buffer, HttpResponse::bodyAsString);
                if (!completableFuture6.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture6;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXNEW.async$solve(this, n, completableFuture7, buffer, null, null, null, 4, arg_0));
                }
                String string = (String)completableFuture6.join();
                CompletableFuture completableFuture8 = this.executeAPI("API 2", this.apiRequest(2, false), this.buildRequestBody(string, this.performance), HttpResponse::body);
                if (!completableFuture8.isDone()) {
                    CompletableFuture completableFuture9 = completableFuture8;
                    return ((CompletableFuture)completableFuture9.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXNEW.async$solve(this, n, completableFuture9, buffer, string, null, null, 5, arg_0));
                }
                Buffer buffer2 = (Buffer)completableFuture8.join();
                CompletableFuture completableFuture10 = VertxUtil.hardCodedSleep((long)this.waitTime);
                if (!completableFuture10.isDone()) {
                    CompletableFuture completableFuture11 = completableFuture10;
                    return ((CompletableFuture)completableFuture11.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXNEW.async$solve(this, n, completableFuture11, buffer, string, buffer2, null, 6, arg_0));
                }
                completableFuture10.join();
                CompletableFuture completableFuture12 = this.executePX("Sensor 2/2", this::collectorReq, buffer2, HttpResponse::bodyAsString);
                if (!completableFuture12.isDone()) {
                    CompletableFuture completableFuture13 = completableFuture12;
                    return ((CompletableFuture)completableFuture13.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXNEW.async$solve(this, n, completableFuture13, buffer, string, buffer2, null, 7, arg_0));
                }
                String string2 = (String)completableFuture12.join();
                MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
                this.parseResultCookies(string2, multiMap);
                if (multiMap.isEmpty()) return CompletableFuture.completedFuture(multiMap);
                if (this.getVid() != null && !this.getVid().isBlank()) {
                    multiMap.add(VID_COOKIE, (CharSequence)this.getVid());
                }
                multiMap.add(RF_VALUE, ONE_VALUE);
                multiMap.add(FP_VALUE, ONE_VALUE);
                multiMap.add(CFP_VALUE, ONE_VALUE);
                if (!this.logger.isDebugEnabled()) return CompletableFuture.completedFuture(multiMap);
                this.logger.debug("Parsed cookies from normal solve: {}", (Object)multiMap);
                return CompletableFuture.completedFuture(multiMap);
            }
            catch (Throwable throwable) {
                this.logger.warn("Error solving(1) sensor: {}. Retrying...", (Object)throwable.getMessage());
                if (this.logger.isDebugEnabled()) {
                    throwable.printStackTrace();
                }
                CompletableFuture completableFuture = VertxUtil.randomSleep((long)5000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture14 = completableFuture;
                    return ((CompletableFuture)completableFuture14.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXNEW.async$solve(this, n, completableFuture14, null, null, null, throwable, 8, arg_0));
                }
                completableFuture.join();
                ++n;
            }
        }
        return CompletableFuture.completedFuture(MultiMap.caseInsensitiveMultiMap());
    }

    public static String getApiURI(String string) {
        boolean bl = false;
        return String.format(bl ? "http://localhost:8080/gen/%s.json" : "https://trickle-px-oygn7nn37q-uc.a.run.app/gen/%s.json", string);
    }

    public HttpRequest imageReq(Buffer buffer) {
        HttpRequest httpRequest = this.delegate.getWebClient().getAbs("https://collector-pxu6b0qd2s.px-client.net/b/g?" + buffer.toString()).expect(ResponsePredicate.SC_OK).as(BodyCodec.buffer());
        httpRequest.putHeader("sec-ch-ua", this.getDeviceSecUA());
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.getDeviceUA());
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("origin", "https://www.walmart.com");
        httpRequest.putHeader("sec-fetch-site", "cross-site");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.walmart.com/");
        httpRequest.putHeader("accept-encoding", this.getDeviceAcceptEncoding());
        httpRequest.putHeader("accept-language", this.getDeviceLang());
        return httpRequest;
    }

    public CompletableFuture executePX(String string, Supplier supplier, Object object, ResponseHandler responseHandler) {
        return this._execute(string, supplier, object, responseHandler, false);
    }

    public JsonObject buildRequestBody(String string, JsonArray jsonArray) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("pxhd", (Object)this.delegate.getCookies().getCookieValue("_pxhd"));
        jsonObject.put("uuid", (Object)this.uuid);
        jsonObject.put("sid", (Object)this.sid);
        jsonObject.put("vid", (Object)this.vid);
        jsonObject.put("firstResponse", (Object)string);
        jsonObject.put("performance", (Object)jsonArray);
        jsonObject.put("cts", (Object)this.delegate.getCookies().getCookieValue(String.valueOf(CTS_VALUE)));
        jsonObject.put("device", (Object)this.deviceNumber);
        return jsonObject;
    }

    public HttpRequest bundleReq() {
        HttpRequest httpRequest = this.delegate.getWebClient().postAbs("https://collector-pxu6b0qd2s.px-cloud.net/assets/js/bundle").expect(ResponsePredicate.SC_OK).as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("sec-ch-ua", this.getDeviceSecUA());
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.getDeviceUA());
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("content-type", "application/x-www-form-urlencoded");
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("origin", "https://www.walmart.com");
        httpRequest.putHeader("sec-fetch-site", "cross-site");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.walmart.com/");
        httpRequest.putHeader("accept-encoding", this.getDeviceAcceptEncoding());
        httpRequest.putHeader("accept-language", this.getDeviceLang());
        return httpRequest;
    }
}
