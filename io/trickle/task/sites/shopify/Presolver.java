/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.json.JsonArray
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.client.HttpResponse
 */
package io.trickle.task.sites.shopify;

import io.trickle.core.VertxSingleton;
import io.trickle.harvester.CaptchaToken;
import io.trickle.task.sites.shopify.Shopify;
import io.trickle.task.sites.shopify.ShopifyAPI;
import io.trickle.task.sites.shopify.util.Triplet;
import io.trickle.task.sites.shopify.util.VariantHandler;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.trickle.webclient.CookieJar;
import io.trickle.webclient.RealClientFactory;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import java.lang.invoke.LambdaMetafactory;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class Presolver {
    public ShopifyAPI api;
    public CookieJar taskCookieJarWriteOnly;
    public Shopify shopifyTask;

    public CompletableFuture monitorCP() {
        int n = 0;
        while (n++ < Integer.MAX_VALUE) {
            if (!this.api.getWebClient().isActive()) return CompletableFuture.completedFuture(null);
            try {
                HttpRequest httpRequest = this.api.checkpointPage();
                CompletableFuture completableFuture = Request.send(httpRequest);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Presolver.async$monitorCP(this, n, httpRequest, completableFuture2, null, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                String string = httpResponse.bodyAsString();
                if (httpResponse.statusCode() == 200 && string.contains("content_checkpoint") || this.shopifyTask.task.getMode().contains("testing")) {
                    VertxUtil.sendSignal("CPStatus" + this.api.getSiteURL());
                    return CompletableFuture.completedFuture(string);
                }
                if (httpResponse.statusCode() != 200) {
                    this.shopifyTask.getLogger().error("Failed CPStatus: {}", (Object)httpResponse.statusCode());
                }
                CompletableFuture completableFuture3 = VertxUtil.randomSignalSleep("CPStatus" + this.api.getSiteURL(), this.shopifyTask.task.getMonitorDelay());
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Presolver.async$monitorCP(this, n, httpRequest, completableFuture4, httpResponse, string, null, 2, arg_0));
                }
                completableFuture3.join();
            }
            catch (Throwable throwable) {
                if (!throwable.getMessage().contains("closed")) {
                    this.shopifyTask.getLogger().error("Failed CPStatus: {}", (Object)throwable.getMessage());
                }
                CompletableFuture completableFuture = VertxUtil.randomSleep(3000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture5 = completableFuture;
                    return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> Presolver.async$monitorCP(this, n, null, completableFuture5, null, null, throwable, 3, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
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
    public static CompletableFuture async$submitCheckpoint(Presolver var0, CaptchaToken var1_1, int var2_2, HttpRequest var3_3, CompletableFuture var4_5, HttpResponse var5_6, Throwable var6_7, int var7_8, Object var8_9) {
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
    public static CompletableFuture async$atcAJAX(Presolver var0, String var1_1, int var2_2, HttpRequest var3_3, CompletableFuture var4_5, HttpResponse var5_6, Throwable var6_7, int var7_8, Object var8_9) {
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

    public CompletableFuture run() {
        CompletableFuture completableFuture = this.fetchRandItem();
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Presolver.async$run(this, completableFuture2, null, null, null, null, 1, arg_0));
        }
        JsonArray jsonArray = (JsonArray)completableFuture.join();
        CompletableFuture completableFuture3 = VariantHandler.findPrecartVariantOOS(jsonArray);
        if (!completableFuture3.isDone()) {
            CompletableFuture completableFuture4 = completableFuture3;
            return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Presolver.async$run(this, completableFuture4, jsonArray, null, null, null, 2, arg_0));
        }
        Triplet triplet = (Triplet)completableFuture3.join();
        if (triplet == null) {
            return CompletableFuture.completedFuture(null);
        }
        CompletableFuture completableFuture5 = this.atcAJAX((String)triplet.first);
        if (!completableFuture5.isDone()) {
            CompletableFuture completableFuture6 = completableFuture5;
            return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Presolver.async$run(this, completableFuture6, jsonArray, triplet, null, null, 3, arg_0));
        }
        completableFuture5.join();
        CompletableFuture completableFuture7 = this.monitorCP();
        if (!completableFuture7.isDone()) {
            CompletableFuture completableFuture8 = completableFuture7;
            return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Presolver.async$run(this, completableFuture8, jsonArray, triplet, null, null, 4, arg_0));
        }
        String string = (String)completableFuture7.join();
        if (!this.shopifyTask.isCPMonitorTriggered.compareAndSet(false, true)) return CompletableFuture.completedFuture(null);
        if (!this.api.getWebClient().isActive()) return CompletableFuture.completedFuture(null);
        CompletableFuture completableFuture9 = this.shopifyTask.solveCaptcha(true, string);
        if (!completableFuture9.isDone()) {
            CompletableFuture completableFuture10 = completableFuture9;
            return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> Presolver.async$run(this, completableFuture10, jsonArray, triplet, string, null, 5, arg_0));
        }
        CaptchaToken captchaToken = (CaptchaToken)completableFuture9.join();
        CompletableFuture completableFuture11 = this.submitCheckpoint(captchaToken);
        if (!completableFuture11.isDone()) {
            CompletableFuture completableFuture12 = completableFuture11;
            return ((CompletableFuture)completableFuture12.exceptionally(Function.identity())).thenCompose(arg_0 -> Presolver.async$run(this, completableFuture12, jsonArray, triplet, string, captchaToken, 6, arg_0));
        }
        completableFuture11.join();
        try {
            this.taskCookieJarWriteOnly.put("_shopify_checkpoint", this.api.getCookies().getCookieValue("_shopify_checkpoint"), this.api.getSiteURL());
            return CompletableFuture.completedFuture(null);
        }
        catch (Exception exception) {
            this.shopifyTask.getLogger().error("CP cookie not found. Severe error " + exception.getMessage());
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture atcAJAX(String string) {
        int n = 0;
        while (n++ < Integer.MAX_VALUE) {
            if (!this.api.getWebClient().isActive()) return CompletableFuture.completedFuture(null);
            try {
                HttpRequest httpRequest = this.api.atcAJAX(string);
                CompletableFuture completableFuture = Request.send(httpRequest);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Presolver.async$atcAJAX(this, string, n, httpRequest, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 200) {
                    VertxUtil.sendSignal("CPCart" + this.api.getSiteURL());
                    return CompletableFuture.completedFuture(null);
                }
                this.shopifyTask.getLogger().error("Failed CPCart: {}", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture3 = VertxUtil.signalSleep("CPCart" + this.api.getSiteURL(), this.shopifyTask.task.getMonitorDelay());
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
                CompletableFuture completableFuture = VertxUtil.randomSleep(3000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture5 = completableFuture;
                    return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> Presolver.async$atcAJAX(this, string, n, null, completableFuture5, null, throwable, 3, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public Presolver(Shopify shopify) {
        this.taskCookieJarWriteOnly = shopify.api.getCookies();
        this.api = new ShopifyAPI(shopify.task, RealClientFactory.fromOtherFreshCookie(VertxSingleton.INSTANCE.get(), shopify.api.getWebClient()));
        this.shopifyTask = shopify;
    }

    public CompletableFuture submitCheckpoint(CaptchaToken captchaToken) {
        this.shopifyTask.getLogger().info("Submitting checkpoint...");
        int n = 0;
        while (n++ < Integer.MAX_VALUE) {
            if (!this.api.getWebClient().isActive()) return CompletableFuture.completedFuture(null);
            try {
                HttpRequest httpRequest = this.api.submitCheckpoint();
                CompletableFuture completableFuture = Request.send(httpRequest, Shopify.checkpointForm(captchaToken));
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
                CompletableFuture completableFuture3 = VertxUtil.sleep(this.shopifyTask.task.getRetryDelay());
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Presolver.async$submitCheckpoint(this, captchaToken, n, httpRequest, completableFuture4, httpResponse, null, 2, arg_0));
                }
                completableFuture3.join();
            }
            catch (Throwable throwable) {
                this.shopifyTask.getLogger().error("Error with checkpoint submission: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = VertxUtil.randomSleep(3000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture5 = completableFuture;
                    return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> Presolver.async$submitCheckpoint(this, captchaToken, n, null, completableFuture5, null, throwable, 3, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    public static CompletableFuture async$run(Presolver var0, CompletableFuture var1_1, JsonArray var2_2, Triplet var3_3, String var4_4, CaptchaToken var5_5, int var6_7, Object var7_14) {
        switch (var6_7) {
            case 0: {
                v0 = var0.fetchRandItem();
                if (!v0.isDone()) {
                    var6_8 = v0;
                    return var6_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.shopify.Presolver java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray io.trickle.task.sites.shopify.util.Triplet java.lang.String io.trickle.harvester.CaptchaToken int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Presolver)var0, (CompletableFuture)var6_8, null, null, null, null, (int)1));
                }
                ** GOTO lbl10
            }
            case 1: {
                v0 = var1_1;
lbl10:
                // 2 sources

                if (!(v1 = VariantHandler.findPrecartVariantOOS((JsonArray)(var1_1 = (JsonArray)v0.join()))).isDone()) {
                    var6_9 = v1;
                    return var6_9.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.shopify.Presolver java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray io.trickle.task.sites.shopify.util.Triplet java.lang.String io.trickle.harvester.CaptchaToken int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Presolver)var0, (CompletableFuture)var6_9, (JsonArray)var1_1, null, null, null, (int)2));
                }
                ** GOTO lbl17
            }
            case 2: {
                v1 = var1_1;
                var1_1 = var2_2 /* !! */ ;
lbl17:
                // 2 sources

                if ((var2_2 /* !! */  = (Triplet)v1.join()) == null) {
                    return CompletableFuture.completedFuture(null);
                }
                v2 = var0.atcAJAX((String)var2_2 /* !! */ .first);
                if (!v2.isDone()) {
                    var6_10 = v2;
                    return var6_10.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.shopify.Presolver java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray io.trickle.task.sites.shopify.util.Triplet java.lang.String io.trickle.harvester.CaptchaToken int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Presolver)var0, (CompletableFuture)var6_10, (JsonArray)var1_1, (Triplet)var2_2 /* !! */ , null, null, (int)3));
                }
                ** GOTO lbl29
            }
            case 3: {
                v2 = var1_1;
                v3 = var2_2 /* !! */ ;
                var2_2 /* !! */  = var3_3;
                var1_1 = v3;
lbl29:
                // 2 sources

                v2.join();
                v4 = var0.monitorCP();
                if (!v4.isDone()) {
                    var6_11 = v4;
                    return var6_11.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.shopify.Presolver java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray io.trickle.task.sites.shopify.util.Triplet java.lang.String io.trickle.harvester.CaptchaToken int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Presolver)var0, (CompletableFuture)var6_11, (JsonArray)var1_1, (Triplet)var2_2 /* !! */ , null, null, (int)4));
                }
                ** GOTO lbl41
            }
            case 4: {
                v4 = var1_1;
                v5 = var2_2 /* !! */ ;
                var2_2 /* !! */  = var3_3;
                var1_1 = v5;
lbl41:
                // 2 sources

                var3_3 = (String)v4.join();
                if (var0.shopifyTask.isCPMonitorTriggered.compareAndSet(false, true) == false) return CompletableFuture.completedFuture(null);
                if (var0.api.getWebClient().isActive() == false) return CompletableFuture.completedFuture(null);
                v6 = var0.shopifyTask.solveCaptcha(true, (String)var3_3);
                if (!v6.isDone()) {
                    var6_12 = v6;
                    return var6_12.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.shopify.Presolver java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray io.trickle.task.sites.shopify.util.Triplet java.lang.String io.trickle.harvester.CaptchaToken int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Presolver)var0, (CompletableFuture)var6_12, (JsonArray)var1_1, (Triplet)var2_2 /* !! */ , (String)var3_3, null, (int)5));
                }
                ** GOTO lbl56
            }
            case 5: {
                v6 = var1_1;
                v7 = var2_2 /* !! */ ;
                v8 = var3_3;
                var3_3 = var4_4;
                var2_2 /* !! */  = v8;
                var1_1 = v7;
lbl56:
                // 2 sources

                if (!(v9 = var0.submitCheckpoint((CaptchaToken)(var4_4 = (CaptchaToken)v6.join()))).isDone()) {
                    var6_13 = v9;
                    return var6_13.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.shopify.Presolver java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray io.trickle.task.sites.shopify.util.Triplet java.lang.String io.trickle.harvester.CaptchaToken int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Presolver)var0, (CompletableFuture)var6_13, (JsonArray)var1_1, (Triplet)var2_2 /* !! */ , (String)var3_3, (CaptchaToken)var4_4, (int)6));
                }
                ** GOTO lbl69
            }
            case 6: {
                v9 = var1_1;
                v10 = var2_2 /* !! */ ;
                v11 = var3_3;
                v12 = var4_4;
                var4_4 = var5_5;
                var3_3 = v12;
                var2_2 /* !! */  = v11;
                var1_1 = v10;
lbl69:
                // 2 sources

                v9.join();
                try {
                    var0.taskCookieJarWriteOnly.put("_shopify_checkpoint", var0.api.getCookies().getCookieValue("_shopify_checkpoint"), var0.api.getSiteURL());
                    return CompletableFuture.completedFuture(null);
                }
                catch (Exception var5_6) {
                    var0.shopifyTask.getLogger().error("CP cookie not found. Severe error " + var5_6.getMessage());
                }
                return CompletableFuture.completedFuture(null);
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture fetchRandItem() {
        int n = 0;
        while (n++ < Integer.MAX_VALUE) {
            if (!this.api.getWebClient().isActive()) return CompletableFuture.completedFuture(null);
            try {
                HttpRequest httpRequest = this.api.productsJSON(true);
                CompletableFuture completableFuture = Request.send(httpRequest);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Presolver.async$fetchRandItem(this, n, httpRequest, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 200) {
                    VertxUtil.sendSignal("CPProd" + this.api.getSiteURL());
                    return CompletableFuture.completedFuture(httpResponse.bodyAsJsonObject().getJsonArray("products"));
                }
                if (httpResponse.statusCode() == 401) {
                    CompletableFuture completableFuture3 = VertxUtil.signalSleep("CPProd" + this.api.getSiteURL(), this.shopifyTask.task.getMonitorDelay());
                    if (!completableFuture3.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture3;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Presolver.async$fetchRandItem(this, n, httpRequest, completableFuture4, httpResponse, null, 2, arg_0));
                    }
                    completableFuture3.join();
                } else {
                    this.shopifyTask.getLogger().error("Failed CPProd: {}", (Object)httpResponse.statusCode());
                }
                CompletableFuture completableFuture5 = VertxUtil.signalSleep("CPProd" + this.api.getSiteURL(), this.shopifyTask.task.getMonitorDelay());
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
                CompletableFuture completableFuture = VertxUtil.randomSleep(3000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> Presolver.async$fetchRandItem(this, n, null, completableFuture7, null, throwable, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$monitorCP(Presolver var0, int var1_1, HttpRequest var2_2, CompletableFuture var3_4, HttpResponse var4_5, String var5_6, Throwable var6_7, int var7_8, Object var8_9) {
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
}

