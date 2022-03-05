/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
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
    public CompletableFuture<Void> sleepFuture = null;
    public boolean running = false;
    public int id;
    public static Logger netLogger = LogManager.getLogger((String)"NET_LOGGER");
    public N apiClient;
    public Logger logger;
    public Task task;

    public void lambda$start$0(Void void_, Throwable throwable) {
        this.vertx.undeploy(super.deploymentID());
    }

    public void setClient(TaskApiClient taskApiClient) {
        this.apiClient = taskApiClient;
    }

    public TaskApiClient getClient() {
        return this.apiClient;
    }

    public void lambda$sleep$2(Long l) {
        try {
            this.sleepFuture.complete(null);
            return;
        }
        catch (Throwable throwable) {
            // empty catch block
        }
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

    public static Void lambda$start$1(Throwable throwable) {
        return null;
    }

    public void netLogError(String string) {
        this.netLog(Level.ERROR, string);
    }

    public void netLog(Level level, String string) {
        if (string.contains("HttpProxyConnectException")) {
            return;
        }
        netLogger.log(level, "{} {}", (Object)this.logger.getName(), (Object)string);
    }

    public void netLogWarn(String string) {
        this.netLog(Level.WARN, string);
    }

    public void start() {
        try {
            MDC.put("version", "1.0.238");
            MDC.put("user", Storage.ACCESS_KEY);
            MDC.put("session", App.SESSION_HASH);
            this.logger.info("Starting.");
            this.running = true;
            ((CompletableFuture)this.run().whenComplete(this::lambda$start$0)).exceptionally(TaskActor::lambda$start$1);
            return;
        }
        catch (Throwable throwable) {
            // empty catch block
        }
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
                    CompletableFuture completableFuture = Request.send(httpRequest.as(BodyCodec.none()));
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture2 = completableFuture;
                        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> TaskActor.async$GETREQ(this, string, httpRequest, n, stringArray, n2, completableFuture2, null, null, 1, arg_0));
                    }
                    httpResponse2 = (HttpResponse)completableFuture.join();
                } else {
                    CompletableFuture completableFuture = Request.send(httpRequest);
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
                        boolean bl2 = bl = stringArray == null || Utils.containsAllWords(httpResponse.bodyAsString(), stringArray);
                    }
                    if ((n == null || httpResponse.statusCode() == n.intValue()) && bl) {
                        String string2;
                        if (n2 != 0) {
                            string2 = httpResponse.getHeader("location");
                            return CompletableFuture.completedFuture(string2);
                        }
                        string2 = httpResponse.bodyAsString();
                        return CompletableFuture.completedFuture(string2);
                    }
                    this.logger.warn("Failed " + string.toLowerCase(Locale.ROOT) + ": '{}'", (Object)(httpResponse.statusCode() + httpResponse.statusMessage()));
                }
                CompletableFuture completableFuture = VertxUtil.randomSleep(this.task.getMonitorDelay());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> TaskActor.async$GETREQ(this, string, httpRequest, n, stringArray, n2, completableFuture4, httpResponse, null, 3, arg_0));
                }
                completableFuture.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error " + string.toLowerCase(Locale.ROOT) + ": {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = VertxUtil.randomSleep(this.task.getRetryDelay());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture5 = completableFuture;
                    return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> TaskActor.async$GETREQ(this, string, httpRequest, n, stringArray, 0, completableFuture5, null, throwable, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture sleep(int n) {
        if (this.sleepFuture != null) {
            this.sleepFuture = null;
        }
        this.sleepFuture = new ContextCompletableFuture();
        this.vertx.setTimer((long)n, this::lambda$sleep$2);
        return this.sleepFuture;
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

    public TaskActor(int n) {
        this.id = n;
        this.logger = LogManager.getLogger((String)String.format("[%s][TASK-%s]", this.getClass().getSimpleName().toUpperCase(), String.format("%04d", this.id)));
    }

    public void stop() {
        this.running = false;
        try {
            ((TaskApiClient)this.apiClient).close();
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
                    CompletableFuture completableFuture = Request.send(httpRequest, object);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture2 = completableFuture;
                        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> TaskActor.async$execute(this, string, function, (Supplier)supplier, object, httpRequest, completableFuture2, null, 0, null, null, null, 1, arg_0));
                    }
                    httpResponse = (HttpResponse)completableFuture.join();
                } else {
                    CompletableFuture completableFuture = Request.send(httpRequest);
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
                    CompletableFuture completableFuture = VertxUtil.randomSleep(this.task.getMonitorDelay());
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> TaskActor.async$execute(this, string, function, (Supplier)supplier, object, httpRequest, completableFuture4, httpResponse, n, optional, string2, null, 3, arg_0));
                    }
                    completableFuture.join();
                    continue;
                }
                CompletableFuture completableFuture = VertxUtil.randomSleep(this.task.getRetryDelay());
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
                CompletableFuture completableFuture = VertxUtil.randomSleep(this.task.getRetryDelay());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> TaskActor.async$execute(this, string, function, (Supplier)supplier, object, null, completableFuture6, null, 0, null, null, throwable, 5, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.failedFuture(new Exception("Failed to execute " + string));
    }

    public Logger getLogger() {
        return this.logger;
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

    public void netLogInfo(String string) {
        this.netLog(Level.INFO, string);
    }
}

