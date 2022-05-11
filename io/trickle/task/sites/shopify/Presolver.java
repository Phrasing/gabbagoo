/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.harvester.CaptchaToken
 *  io.trickle.task.sites.shopify.Shopify
 *  io.trickle.task.sites.shopify.ShopifyAPI
 *  io.trickle.util.Utils
 *  io.trickle.util.concurrent.VertxUtil
 *  io.trickle.util.request.Request
 *  io.vertx.core.MultiMap
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.client.HttpResponse
 */
package io.trickle.task.sites.shopify;

import io.trickle.harvester.CaptchaToken;
import io.trickle.task.sites.shopify.Shopify;
import io.trickle.task.sites.shopify.ShopifyAPI;
import io.trickle.util.Utils;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.vertx.core.MultiMap;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import java.lang.invoke.LambdaMetafactory;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class Presolver {
    public boolean isRunning = true;
    public ShopifyAPI api;
    public Shopify shopifyTask;

    public CompletableFuture monitorCP() {
        int n = 0;
        while (n++ < Integer.MAX_VALUE) {
            if (!this.isRunning) return CompletableFuture.completedFuture(null);
            if (!this.api.getWebClient().isActive()) return CompletableFuture.completedFuture(null);
            try {
                int n2;
                HttpRequest httpRequest = this.api.checkpointPage(false);
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Presolver.async$monitorCP(this, n, httpRequest, completableFuture2, null, null, 0, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                String string = httpResponse.bodyAsString();
                if (httpResponse.statusCode() == 200 && (string.contains("content_checkpoint") || this.shopifyTask.task.getMode().contains("testing"))) {
                    VertxUtil.sendSignal((String)("CPStatus" + this.api.getSiteURL()));
                    return CompletableFuture.completedFuture(string);
                }
                if (!(httpResponse.statusCode() == 200 || httpResponse.statusCode() == 302 && httpResponse.getHeader("location").contains("password"))) {
                    this.shopifyTask.getLogger().error("Failed CPStatus: {}", (Object)httpResponse.statusCode());
                }
                if (this.shopifyTask.isSmart && (n2 = Utils.calculateMSLeftUntilHour()) - this.shopifyTask.task.getMonitorDelay() < 0 && n2 < 10000) {
                    this.shopifyTask.getLogger().info("Preparing smart release");
                    CompletableFuture completableFuture3 = VertxUtil.signalSleep((String)("CPStatus" + this.api.getSiteURL()), (long)n2);
                    if (!completableFuture3.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture3;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Presolver.async$monitorCP(this, n, httpRequest, completableFuture4, httpResponse, string, n2, null, 2, arg_0));
                    }
                    completableFuture3.join();
                    continue;
                }
                CompletableFuture completableFuture5 = VertxUtil.randomSignalSleep((String)("CPStatus" + this.api.getSiteURL()), (long)this.shopifyTask.task.getMonitorDelay());
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Presolver.async$monitorCP(this, n, httpRequest, completableFuture6, httpResponse, string, 0, null, 3, arg_0));
                }
                completableFuture5.join();
            }
            catch (Throwable throwable) {
                if (!throwable.getMessage().contains("closed")) {
                    this.shopifyTask.getLogger().error("Failed CPStatus: {}", (Object)throwable.getMessage());
                }
                CompletableFuture completableFuture = VertxUtil.randomSleep((long)3000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> Presolver.async$monitorCP(this, n, null, completableFuture7, null, null, 0, throwable, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$atcAJAX(Presolver var0, String var1_1, int var2_2, HttpRequest var3_3, CompletableFuture var4_5, HttpResponse var5_6, Throwable var6_7, int var7_8, Object var8_9) {
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

    public CompletableFuture fetchRandItem() {
        int n = 0;
        while (n++ < Integer.MAX_VALUE) {
            if (!this.isRunning) return CompletableFuture.completedFuture(null);
            if (!this.api.getWebClient().isActive()) return CompletableFuture.completedFuture(null);
            try {
                HttpRequest httpRequest = this.api.productsJSON(true);
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Presolver.async$fetchRandItem(this, n, httpRequest, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 200) {
                    VertxUtil.sendSignal((String)("CPProd" + this.api.getSiteURL()));
                    return CompletableFuture.completedFuture(httpResponse.bodyAsJsonObject().getJsonArray("products"));
                }
                if (httpResponse.statusCode() == 401) {
                    this.shopifyTask.getLogger().info("Password detected. Sleeping...");
                    CompletableFuture completableFuture3 = VertxUtil.signalSleep((String)("CPProd" + this.api.getSiteURL()), (long)this.shopifyTask.task.getMonitorDelay());
                    if (!completableFuture3.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture3;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Presolver.async$fetchRandItem(this, n, httpRequest, completableFuture4, httpResponse, null, 2, arg_0));
                    }
                    completableFuture3.join();
                } else {
                    this.shopifyTask.getLogger().error("Failed CPProd: {}", (Object)httpResponse.statusCode());
                }
                CompletableFuture completableFuture5 = VertxUtil.signalSleep((String)("CPProd" + this.api.getSiteURL()), (long)this.shopifyTask.task.getMonitorDelay());
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Presolver.async$fetchRandItem(this, n, httpRequest, completableFuture6, httpResponse, null, 3, arg_0));
                }
                completableFuture5.join();
            }
            catch (Throwable throwable) {
                if (!throwable.getMessage().contains("closed")) {
                    this.shopifyTask.getLogger().error("Failed CPProd: {}", (Object)throwable.getMessage());
                }
                CompletableFuture completableFuture = VertxUtil.randomSleep((long)3000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> Presolver.async$fetchRandItem(this, n, null, completableFuture7, null, throwable, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture run() {
        CompletableFuture completableFuture = this.monitorCP();
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Presolver.async$run(this, completableFuture2, null, null, 1, arg_0));
        }
        String string = (String)completableFuture.join();
        if (!this.shopifyTask.isCPMonitorTriggered.compareAndSet(false, true)) return CompletableFuture.completedFuture(null);
        if (!this.isRunning) return CompletableFuture.completedFuture(null);
        if (!this.api.getWebClient().isActive()) return CompletableFuture.completedFuture(null);
        CompletableFuture completableFuture3 = this.shopifyTask.solveCheckpointCaptcha(string);
        if (!completableFuture3.isDone()) {
            CompletableFuture completableFuture4 = completableFuture3;
            return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Presolver.async$run(this, completableFuture4, string, null, 2, arg_0));
        }
        CaptchaToken captchaToken = (CaptchaToken)completableFuture3.join();
        CompletableFuture completableFuture5 = this.submitCheckpoint(captchaToken);
        if (!completableFuture5.isDone()) {
            CompletableFuture completableFuture6 = completableFuture5;
            return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Presolver.async$run(this, completableFuture6, string, captchaToken, 3, arg_0));
        }
        completableFuture5.join();
        if (this.api.checkpointClient.cookieStore().getCookieValue("_shopify_checkpoint") == null) {
            this.shopifyTask.getLogger().error("CP cookie not found. Severe error");
        } else {
            this.api.getCookies().put("_shopify_checkpoint", this.api.checkpointClient.cookieStore().getCookieValue("_shopify_checkpoint"), this.api.getSiteURL());
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$run(Presolver var0, CompletableFuture var1_1, String var2_2, CaptchaToken var3_3, int var4_4, Object var5_5) {
        switch (var4_4) {
            case 0: {
                v0 = var0.monitorCP();
                if (!v0.isDone()) {
                    var3_3 = v0;
                    return var3_3.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.shopify.Presolver java.util.concurrent.CompletableFuture java.lang.String io.trickle.harvester.CaptchaToken int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Presolver)var0, (CompletableFuture)var3_3, null, null, (int)1));
                }
                ** GOTO lbl10
            }
            case 1: {
                v0 = var1_1;
lbl10:
                // 2 sources

                var1_1 = (String)v0.join();
                if (var0.shopifyTask.isCPMonitorTriggered.compareAndSet(false, true) == false) return CompletableFuture.completedFuture(null);
                if (var0.isRunning == false) return CompletableFuture.completedFuture(null);
                if (var0.api.getWebClient().isActive() == false) return CompletableFuture.completedFuture(null);
                v1 = var0.shopifyTask.solveCheckpointCaptcha((String)var1_1);
                if (!v1.isDone()) {
                    var3_3 = v1;
                    return var3_3.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.shopify.Presolver java.util.concurrent.CompletableFuture java.lang.String io.trickle.harvester.CaptchaToken int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Presolver)var0, (CompletableFuture)var3_3, (String)var1_1, null, (int)2));
                }
                ** GOTO lbl22
            }
            case 2: {
                v1 = var1_1;
                var1_1 = var2_2;
lbl22:
                // 2 sources

                if (!(v2 = var0.submitCheckpoint((CaptchaToken)(var2_2 = (CaptchaToken)v1.join()))).isDone()) {
                    var3_3 = v2;
                    return var3_3.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.shopify.Presolver java.util.concurrent.CompletableFuture java.lang.String io.trickle.harvester.CaptchaToken int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Presolver)var0, (CompletableFuture)var3_3, (String)var1_1, (CaptchaToken)var2_2, (int)3));
                }
                ** GOTO lbl31
            }
            case 3: {
                v2 = var1_1;
                v3 = var2_2;
                var2_2 = var3_3;
                var1_1 = v3;
lbl31:
                // 2 sources

                v2.join();
                if (var0.api.checkpointClient.cookieStore().getCookieValue("_shopify_checkpoint") == null) {
                    var0.shopifyTask.getLogger().error("CP cookie not found. Severe error");
                } else {
                    var0.api.getCookies().put("_shopify_checkpoint", var0.api.checkpointClient.cookieStore().getCookieValue("_shopify_checkpoint"), var0.api.getSiteURL());
                }
                return CompletableFuture.completedFuture(null);
            }
        }
        throw new IllegalArgumentException();
    }

    public Presolver(Shopify shopify) {
        this.api = shopify.api;
        this.shopifyTask = shopify;
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$submitCheckpoint(Presolver var0, CaptchaToken var1_1, int var2_2, HttpRequest var3_3, CompletableFuture var4_5, HttpResponse var5_6, Throwable var6_7, int var7_8, Object var8_9) {
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

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$monitorCP(Presolver var0, int var1_1, HttpRequest var2_2, CompletableFuture var3_4, HttpResponse var4_5, String var5_6, int var6_8, Throwable var7_13, int var8_14, Object var9_15) {
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

    public CompletableFuture submitCheckpoint(CaptchaToken captchaToken) {
        this.shopifyTask.getLogger().info("Submitting checkpoint...");
        int n = 0;
        while (n++ < Integer.MAX_VALUE) {
            if (!this.isRunning) return CompletableFuture.completedFuture(null);
            if (!this.api.getWebClient().isActive()) return CompletableFuture.completedFuture(null);
            try {
                HttpRequest httpRequest = this.api.submitCheckpoint();
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest, (MultiMap)Shopify.checkpointForm((CaptchaToken)captchaToken));
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Presolver.async$submitCheckpoint(this, captchaToken, n, httpRequest, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 302) {
                    return CompletableFuture.completedFuture(null);
                }
                this.shopifyTask.getLogger().warn("Retrying submitting checkpoint: status: '{}'", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture3 = VertxUtil.sleep((long)this.shopifyTask.task.getRetryDelay());
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Presolver.async$submitCheckpoint(this, captchaToken, n, httpRequest, completableFuture4, httpResponse, null, 2, arg_0));
                }
                completableFuture3.join();
            }
            catch (Throwable throwable) {
                this.shopifyTask.getLogger().error("Error with checkpoint submission: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = VertxUtil.randomSleep((long)3000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture5 = completableFuture;
                    return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> Presolver.async$submitCheckpoint(this, captchaToken, n, null, completableFuture5, null, throwable, 3, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public void close() {
        this.isRunning = false;
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$fetchRandItem(Presolver var0, int var1_1, HttpRequest var2_2, CompletableFuture var3_4, HttpResponse var4_5, Throwable var5_6, int var6_7, Object var7_8) {
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

    public CompletableFuture atcAJAX(String string) {
        int n = 0;
        while (n++ < Integer.MAX_VALUE) {
            if (!this.isRunning) return CompletableFuture.completedFuture(null);
            if (!this.api.getWebClient().isActive()) return CompletableFuture.completedFuture(null);
            try {
                HttpRequest httpRequest = this.api.atcAJAX(string);
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Presolver.async$atcAJAX(this, string, n, httpRequest, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 200) {
                    VertxUtil.sendSignal((String)("CPCart" + this.api.getSiteURL()));
                    return CompletableFuture.completedFuture(null);
                }
                this.shopifyTask.getLogger().error("Failed CPCart: {}", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture3 = VertxUtil.signalSleep((String)("CPCart" + this.api.getSiteURL()), (long)this.shopifyTask.task.getMonitorDelay());
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Presolver.async$atcAJAX(this, string, n, httpRequest, completableFuture4, httpResponse, null, 2, arg_0));
                }
                completableFuture3.join();
            }
            catch (Throwable throwable) {
                if (!throwable.getMessage().contains("closed")) {
                    this.shopifyTask.getLogger().error("Failed CPCart: {}", (Object)throwable.getMessage());
                }
                CompletableFuture completableFuture = VertxUtil.randomSleep((long)3000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture5 = completableFuture;
                    return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> Presolver.async$atcAJAX(this, string, n, null, completableFuture5, null, throwable, 3, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }
}
