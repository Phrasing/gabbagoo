/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.Future
 *  io.vertx.core.Promise
 *  io.vertx.core.Vertx
 *  io.vertx.core.buffer.Buffer
 *  io.vertx.core.http.HttpVersion
 *  io.vertx.core.json.JsonObject
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.client.HttpResponse
 *  io.vertx.ext.web.client.WebClient
 *  io.vertx.ext.web.client.WebClientOptions
 *  io.vertx.ext.web.codec.BodyCodec
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package io.trickle.util;

import io.trickle.util.Storage;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.trickle.webclient.ClientType;
import io.trickle.webclient.RealClientFactory;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpVersion;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.ext.web.codec.BodyCodec;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LocalClient {
    public WebClient client;
    public static Logger logger = LogManager.getLogger((String)"CLIENT");
    public WebClient chromeClient;

    public CompletableFuture fetchDeviceFromAPI(String string, String string2) {
        int n = 0;
        while (n++ < 99999999) {
            try {
                CompletableFuture completableFuture = Request.send(string2 == null ? this.fetchAPI(string).as(BodyCodec.buffer()) : this.fetchAPI(string).putHeader("ua", string2).as(BodyCodec.buffer()));
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> LocalClient.async$fetchDeviceFromAPI(this, string, string2, n, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 200 && httpResponse.body() != null) {
                    JsonObject jsonObject = httpResponse.bodyAsJsonObject();
                    return CompletableFuture.completedFuture(jsonObject);
                }
                logger.info(((Buffer)httpResponse.body()).toString());
                logger.warn("Waiting for sensor: status:'{}'", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture3 = VertxUtil.hardCodedSleep(3000L);
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> LocalClient.async$fetchDeviceFromAPI(this, string, string2, n, completableFuture4, httpResponse, null, 2, arg_0));
                }
                completableFuture3.join();
            }
            catch (Throwable throwable) {
                CompletableFuture completableFuture = VertxUtil.hardCodedSleep(3000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture5 = completableFuture;
                    return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> LocalClient.async$fetchDeviceFromAPI(this, string, string2, n, completableFuture5, null, throwable, 3, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public static void lambda$fetchUpdates$0(Promise promise, HttpResponse httpResponse) {
        try {
            if (httpResponse.statusCode() != 200) return;
            Buffer buffer = (Buffer)httpResponse.body();
            if (buffer == null) return;
            promise.tryComplete((Object)buffer.toJsonObject());
            return;
        }
        catch (Throwable throwable) {
            logger.warn("Failed to fetch updates: {}", (Object)throwable.getMessage());
            promise.fail(throwable);
        }
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$deleteDeviceFromAPI(LocalClient var0, String var1_1, JsonObject var2_2, int var3_3, CompletableFuture var4_4, HttpResponse var5_6, Throwable var6_7, int var7_8, Object var8_9) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [7[CATCHBLOCK]], but top level block is 11[UNCONDITIONALDOLOOP]
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

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$fetchDeviceFromAPI(LocalClient var0, String var1_1, String var2_2, int var3_3, CompletableFuture var4_4, HttpResponse var5_6, Throwable var6_7, int var7_8, Object var8_9) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [7[CATCHBLOCK]], but top level block is 11[UNCONDITIONALDOLOOP]
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

    public WebClient getChromeClient() {
        return this.chromeClient;
    }

    public HttpRequest postAPI(String string) {
        HttpRequest httpRequest = this.client.postAbs(string).as(BodyCodec.jsonObject());
        httpRequest.putHeader("accept", "application/json");
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("user-agent", "tomato-agent");
        httpRequest.putHeader("key", Storage.ACCESS_KEY);
        return httpRequest;
    }

    public CompletableFuture fetchDeviceFromAPI(String string) {
        return this.fetchDeviceFromAPI(string, null);
    }

    public LocalClient(Vertx vertx) {
        this.chromeClient = RealClientFactory.createWebClient(vertx, ClientType.CHROME, null);
        this.client = WebClient.create((Vertx)vertx, (WebClientOptions)new WebClientOptions().setLogActivity(false).setUserAgentEnabled(false).setProtocolVersion(HttpVersion.HTTP_2).setUseAlpn(true).setTrustAll(false).setConnectTimeout(150000).setSslHandshakeTimeoutUnit(TimeUnit.SECONDS).setSslHandshakeTimeout(150L).setIdleTimeoutUnit(TimeUnit.SECONDS).setIdleTimeout(150).setKeepAlive(true).setKeepAliveTimeout(30).setHttp2KeepAliveTimeout(100).setHttp2MaxPoolSize(150).setHttp2MultiplexingLimit(200).setPoolCleanerPeriod(15000).setMaxPoolSize(150).setTryUseCompression(true).setTcpFastOpen(true).setTcpKeepAlive(true).setTcpNoDelay(true).setTcpQuickAck(true).setFollowRedirects(false));
    }

    public WebClient getClient() {
        return this.client;
    }

    public static void lambda$fetchUpdates$1(Promise promise, Throwable throwable) {
        logger.warn("Failed to fetch updates: {}", (Object)throwable.getMessage());
        promise.fail(throwable);
    }

    public Future fetchUpdates() {
        HttpRequest httpRequest = this.client.getAbs("https://loudounchris.xyz/api/patch").putHeader("accept", "application/json").putHeader("content-type", "application/json").putHeader("user-agent", "tomato-agent").putHeader("key", Storage.ACCESS_KEY).as(BodyCodec.buffer());
        Promise promise = Promise.promise();
        httpRequest.send().onSuccess(arg_0 -> LocalClient.lambda$fetchUpdates$0(promise, arg_0)).onFailure(arg_0 -> LocalClient.lambda$fetchUpdates$1(promise, arg_0));
        return promise.future();
    }

    public CompletableFuture deleteDeviceFromAPI(String string, JsonObject jsonObject) {
        int n = 0;
        while (n++ < 99999999) {
            try {
                CompletableFuture completableFuture = Request.send(this.postAPI(string).as(BodyCodec.buffer()), jsonObject);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> LocalClient.async$deleteDeviceFromAPI(this, string, jsonObject, n, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 200) {
                    return CompletableFuture.completedFuture(null);
                }
                logger.warn("Posting device: status:'{}'", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture3 = VertxUtil.hardCodedSleep(30000L);
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> LocalClient.async$deleteDeviceFromAPI(this, string, jsonObject, n, completableFuture4, httpResponse, null, 2, arg_0));
                }
                completableFuture3.join();
            }
            catch (Throwable throwable) {
                CompletableFuture completableFuture = VertxUtil.hardCodedSleep(3000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture5 = completableFuture;
                    return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> LocalClient.async$deleteDeviceFromAPI(this, string, jsonObject, n, completableFuture5, null, throwable, 3, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public HttpRequest fetchAPI(String string) {
        HttpRequest httpRequest = this.client.getAbs(string).as(BodyCodec.jsonObject());
        httpRequest.putHeader("accept", "application/json");
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("user-agent", "tomato-agent");
        httpRequest.putHeader("key", Storage.ACCESS_KEY);
        return httpRequest;
    }
}

