/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.App
 *  io.trickle.core.actor.Actor
 *  io.trickle.task.Task
 *  io.trickle.util.Storage
 *  io.trickle.util.Utils
 *  io.trickle.util.concurrent.ContextCompletableFuture
 *  io.trickle.util.concurrent.VertxUtil
 *  io.trickle.util.request.Request
 *  io.trickle.webclient.TaskApiClient
 *  io.vertx.core.AbstractVerticle
 *  io.vertx.core.http.HttpMethod
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.client.HttpResponse
 *  io.vertx.ext.web.client.impl.HttpRequestImpl
 *  io.vertx.ext.web.codec.BodyCodec
 *  org.apache.logging.log4j.Level
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package io.trickle.core.actor;

import io.trickle.App;
import io.trickle.core.actor.Actor;
import io.trickle.task.Task;
import io.trickle.util.Storage;
import io.trickle.util.Utils;
import io.trickle.util.concurrent.ContextCompletableFuture;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.trickle.webclient.TaskApiClient;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.impl.HttpRequestImpl;
import io.vertx.ext.web.codec.BodyCodec;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Supplier;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MDC;

public abstract class TaskActor
extends AbstractVerticle
implements Actor {
    public static Logger netLogger = LogManager.getLogger((String)"NET_LOGGER");
    public Task task;
    public boolean running = false;
    public int id;
    public Logger logger;
    public CompletableFuture<Void> sleepFuture = null;
    public N apiClient;

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$GETREQ$1(TaskActor var0, String var1_1, HttpRequest var2_2, Integer[] var3_3, String[] var4_4, CompletableFuture var5_5, HttpResponse var6_7, Throwable var7_9, int var8_11, Object var9_15) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [7[CATCHBLOCK]], but top level block is 11[UNCONDITIONALDOLOOP]
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

    public void netLogWarn(String string) {
        this.netLog(Level.WARN, string);
    }

    public void stop() {
        this.running = false;
        try {
            this.apiClient.close();
        }
        catch (Exception exception) {
            this.logger.error("Error on stop: {}", (Object)exception.getMessage());
        }
        this.logger.warn("Stopped.");
        super.stop();
    }

    public CompletableFuture execute(String string, Function function, Supplier supplier, Object object) {
        this.logger.info(string);
        while (this.running) {
            try {
                HttpResponse httpResponse;
                HttpRequest httpRequest = (HttpRequest)supplier.get();
                if (((HttpRequestImpl)httpRequest).method().equals((Object)HttpMethod.POST)) {
                    CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest, (Object)object);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture2 = completableFuture;
                        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> TaskActor.async$execute(this, string, function, (Supplier)supplier, object, httpRequest, completableFuture2, null, 0, null, null, null, 1, arg_0));
                    }
                    httpResponse = (HttpResponse)completableFuture.join();
                } else {
                    CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture3 = completableFuture;
                        return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> TaskActor.async$execute(this, string, function, (Supplier)supplier, object, httpRequest, completableFuture3, null, 0, null, null, null, 2, arg_0));
                    }
                    httpResponse = (HttpResponse)completableFuture.join();
                }
                if (httpResponse != null) {
                    int n = httpResponse.statusCode();
                    if (function == null) {
                        return CompletableFuture.completedFuture(null);
                    }
                    Optional optional = Optional.ofNullable(function.apply(httpResponse));
                    if (optional.isPresent()) {
                        return CompletableFuture.completedFuture(optional.get());
                    }
                    String string2 = httpResponse.statusCode() + httpResponse.statusMessage();
                    this.logger.warn("Failed " + string.toLowerCase(Locale.ROOT) + ": '{}'", (Object)string2);
                    CompletableFuture completableFuture = VertxUtil.randomSleep((long)this.task.getMonitorDelay());
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> TaskActor.async$execute(this, string, function, (Supplier)supplier, object, httpRequest, completableFuture4, httpResponse, n, optional, string2, null, 3, arg_0));
                    }
                    completableFuture.join();
                    continue;
                }
                CompletableFuture completableFuture = VertxUtil.randomSleep((long)this.task.getRetryDelay());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture5 = completableFuture;
                    return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> TaskActor.async$execute(this, string, function, (Supplier)supplier, object, httpRequest, completableFuture5, httpResponse, 0, null, null, null, 4, arg_0));
                }
                completableFuture.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error " + string.toLowerCase(Locale.ROOT) + ": {}", (Object)throwable.getMessage());
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug((Object)throwable);
                }
                CompletableFuture completableFuture = VertxUtil.randomSleep((long)this.task.getRetryDelay());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> TaskActor.async$execute(this, string, function, (Supplier)supplier, object, null, completableFuture6, null, 0, null, null, throwable, 5, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.failedFuture(new Exception("Failed to execute " + string));
    }

    public static boolean lambda$GETREQ$5(int n, Integer n2) {
        return n2 == n;
    }

    public void netLog(Level level, String string) {
        if (string.contains("Client is closed")) return;
        if (string.contains("VertxException")) return;
        if (string.contains("SSLHandshakeException")) return;
        if (string.contains("SslClosedEngineException")) return;
        if (string.contains("ProxyConnectException")) return;
        if (string.contains("HttpProxyConnectException")) return;
        if (string.contains("UnknownHostException")) return;
        if (string.contains("AnnotatedSocketException")) return;
        if (string.contains("AnnotatedConnectException")) {
            return;
        }
        netLogger.log(level, "{} {}", (Object)this.logger.getName(), (Object)string);
    }

    public Logger getLogger() {
        return this.logger;
    }

    public CompletableFuture GETREQ(String string, HttpRequest httpRequest, Integer n, String ... stringArray) {
        this.logger.info(string);
        while (this.running) {
            try {
                HttpResponse httpResponse;
                HttpResponse httpResponse2;
                int n2;
                int n3 = n2 = stringArray == null || n != null && n == 302 ? 1 : 0;
                if (n2 != 0) {
                    CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest.as(BodyCodec.none()));
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture2 = completableFuture;
                        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> TaskActor.async$GETREQ(this, string, httpRequest, n, stringArray, n2, completableFuture2, null, null, 1, arg_0));
                    }
                    httpResponse2 = (HttpResponse)completableFuture.join();
                } else {
                    CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture3 = completableFuture;
                        return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> TaskActor.async$GETREQ(this, string, httpRequest, n, stringArray, n2, completableFuture3, null, null, 2, arg_0));
                    }
                    httpResponse2 = httpResponse = (HttpResponse)completableFuture.join();
                }
                if (httpResponse != null) {
                    boolean bl;
                    if (httpResponse.statusCode() == 302) {
                        bl = stringArray == null || httpResponse.getHeader("location").contains(stringArray[0]);
                    } else {
                        boolean bl2 = bl = stringArray == null || Utils.containsAllWords((String)httpResponse.bodyAsString(), (String[])stringArray);
                    }
                    if ((n == null || httpResponse.statusCode() == n.intValue()) && bl) {
                        return CompletableFuture.completedFuture(n2 != 0 ? httpResponse.getHeader("location") : httpResponse.bodyAsString());
                    }
                    this.logger.warn("Failed " + string.toLowerCase(Locale.ROOT) + ": '{}'", (Object)(httpResponse.statusCode() + httpResponse.statusMessage()));
                }
                CompletableFuture completableFuture = VertxUtil.randomSleep((long)this.task.getMonitorDelay());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> TaskActor.async$GETREQ(this, string, httpRequest, n, stringArray, n2, completableFuture4, httpResponse, null, 3, arg_0));
                }
                completableFuture.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error " + string.toLowerCase(Locale.ROOT) + ": {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = VertxUtil.randomSleep((long)this.task.getRetryDelay());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture5 = completableFuture;
                    return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> TaskActor.async$GETREQ(this, string, httpRequest, n, stringArray, 0, completableFuture5, null, throwable, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public TaskActor(int n) {
        this.id = n;
        this.logger = LogManager.getLogger((String)String.format("[%s][TASK-%s]", ((Object)((Object)this)).getClass().getSimpleName().toUpperCase(), String.format("%04d", this.id)));
    }

    public CompletableFuture GETREQ(String string, HttpRequest httpRequest, Integer[] integerArray, String ... stringArray) {
        this.logger.info(string);
        while (this.running) {
            try {
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> TaskActor.async$GETREQ$1(this, string, httpRequest, integerArray, stringArray, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse != null) {
                    boolean bl;
                    int n = httpResponse.statusCode();
                    if (n == 302) {
                        bl = stringArray == null || Arrays.stream(stringArray).anyMatch(arg_0 -> TaskActor.lambda$GETREQ$4(httpResponse, arg_0));
                    } else {
                        boolean bl2 = bl = stringArray == null || Utils.containsAllWords((String)httpResponse.bodyAsString(), (String[])stringArray);
                    }
                    if ((integerArray == null || Arrays.stream(integerArray).anyMatch(arg_0 -> TaskActor.lambda$GETREQ$5(n, arg_0))) && bl) {
                        return CompletableFuture.completedFuture(n == 302 ? httpResponse.getHeader("location") : httpResponse.bodyAsString());
                    }
                    this.logger.warn("Failed " + string.toLowerCase(Locale.ROOT) + ": '{}'", (Object)(httpResponse.statusCode() + httpResponse.statusMessage()));
                }
                CompletableFuture completableFuture3 = VertxUtil.randomSleep((long)this.task.getMonitorDelay());
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> TaskActor.async$GETREQ$1(this, string, httpRequest, integerArray, stringArray, completableFuture4, httpResponse, null, 2, arg_0));
                }
                completableFuture3.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error " + string.toLowerCase(Locale.ROOT) + ": {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = VertxUtil.randomSleep((long)this.task.getRetryDelay());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture5 = completableFuture;
                    return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> TaskActor.async$GETREQ$1(this, string, httpRequest, integerArray, stringArray, completableFuture5, null, throwable, 3, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public TaskApiClient getClient() {
        return this.apiClient;
    }

    public CompletableFuture sleep(int n) {
        if (this.sleepFuture != null) {
            this.sleepFuture = null;
        }
        this.sleepFuture = new ContextCompletableFuture();
        this.vertx.setTimer((long)n, this::lambda$sleep$3);
        return this.sleepFuture;
    }

    public void lambda$start$2(Void void_) {
        ((CompletableFuture)this.run().whenComplete(this::lambda$start$0)).exceptionally(TaskActor::lambda$start$1);
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$execute(TaskActor var0, String var1_1, Function var2_2, Supplier var3_3, Object var4_4, HttpRequest var5_5, CompletableFuture var6_7, HttpResponse var7_8, int var8_10, Optional var9_12, String var10_13, Throwable var11_14, int var12_15, Object var13_16) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [9[CATCHBLOCK]], but top level block is 15[UNCONDITIONALDOLOOP]
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

    public static boolean lambda$GETREQ$4(HttpResponse httpResponse, String string) {
        return httpResponse.getHeader("location").contains(string);
    }

    public void netLogError(String string) {
        this.netLog(Level.ERROR, string);
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$GETREQ(TaskActor var0, String var1_1, HttpRequest var2_2, Integer var3_3, String[] var4_4, int var5_5, CompletableFuture var6_8, HttpResponse var7_9, Throwable var8_11, int var9_12, Object var10_13) {
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

    public static Void lambda$start$1(Throwable throwable) {
        return null;
    }

    public CompletableFuture randomSleep(int n) {
        try {
            if (n <= 0) return CompletableFuture.completedFuture(null);
            int n2 = ThreadLocalRandom.current().nextInt((int)Math.min((double)n, (double)n / Double.longBitsToDouble(4608308318706860032L)), (int)((double)n * Double.longBitsToDouble(4608308318706860032L)));
            return this.sleep(n2);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return CompletableFuture.completedFuture(null);
    }

    public void lambda$sleep$3(Long l) {
        try {
            this.sleepFuture.complete(null);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public void start() {
        try {
            MDC.put("version", "1.0.278");
            MDC.put("user", Storage.ACCESS_KEY);
            MDC.put("session", App.SESSION_HASH);
            this.logger.info("Starting.");
            this.running = true;
            this.context.runOnContext(this::lambda$start$2);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public void setClient(TaskApiClient taskApiClient) {
        this.apiClient = taskApiClient;
    }

    public void netLogInfo(String string) {
        this.netLog(Level.INFO, string);
    }

    public void lambda$start$0(Void void_, Throwable throwable) {
        this.vertx.undeploy(super.deploymentID());
    }
}
