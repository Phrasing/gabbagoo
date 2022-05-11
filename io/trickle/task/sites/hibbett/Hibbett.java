/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.core.VertxSingleton
 *  io.trickle.core.actor.TaskActor
 *  io.trickle.profile.Profile
 *  io.trickle.task.Task
 *  io.trickle.task.antibot.impl.px.PXToken
 *  io.trickle.task.antibot.impl.px.PXTokenAPI
 *  io.trickle.task.antibot.impl.px.PXTokenBase
 *  io.trickle.task.sites.Site
 *  io.trickle.task.sites.hibbett.HibbettAPI
 *  io.trickle.task.sites.hibbett.SessionPreload
 *  io.trickle.util.RunClock
 *  io.trickle.util.analytics.Analytics
 *  io.trickle.util.concurrent.VertxUtil
 *  io.trickle.util.request.Request
 *  io.trickle.webclient.TaskApiClient
 *  io.vertx.core.Verticle
 *  io.vertx.core.buffer.Buffer
 *  io.vertx.core.json.JsonArray
 *  io.vertx.core.json.JsonObject
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.client.HttpResponse
 */
package io.trickle.task.sites.hibbett;

import io.trickle.core.VertxSingleton;
import io.trickle.core.actor.TaskActor;
import io.trickle.profile.Profile;
import io.trickle.task.Task;
import io.trickle.task.antibot.impl.px.PXToken;
import io.trickle.task.antibot.impl.px.PXTokenAPI;
import io.trickle.task.antibot.impl.px.PXTokenBase;
import io.trickle.task.sites.Site;
import io.trickle.task.sites.hibbett.HibbettAPI;
import io.trickle.task.sites.hibbett.SessionPreload;
import io.trickle.util.RunClock;
import io.trickle.util.analytics.Analytics;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.trickle.webclient.TaskApiClient;
import io.vertx.core.Verticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import java.lang.invoke.LambdaMetafactory;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Hibbett
extends TaskActor {
    public List<PXToken> tokens;
    public int previousResponseLen = 0;
    public int previousResponseHash = 0;
    public String instanceSignal;
    public boolean griefMode;
    public RunClock clock;
    public boolean shardMode;
    public Task task;
    public boolean scheduledMode;
    public String itemKeyword;
    public HibbettAPI api;
    public static Pattern ITEM_ID_PATTERN = Pattern.compile("([0-z]*?)\\.HTML");

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$processPayment(Hibbett var0, String var1_1, String var2_2, String var3_3, int var4_4, HttpRequest var5_5, CompletableFuture var6_7, HttpResponse var7_8, JsonObject var8_9, int var9_11, Throwable var10_16, int var11_17, Object var12_18) {
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

    public CompletableFuture getSizes(String string, String string2) {
        int n = 0;
        this.logger.info("Waiting for restock");
        while (this.running) {
            if (n++ >= 99999999) return CompletableFuture.completedFuture(null);
            if (!this.shouldRunOnSchedule()) return CompletableFuture.completedFuture(null);
            try {
                HttpRequest httpRequest = this.api.checkStock(string, string2, this.itemKeyword);
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Hibbett.async$getSizes(this, string, string2, n, httpRequest, completableFuture2, null, null, 0, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                JsonObject jsonObject = httpResponse.bodyAsJsonObject();
                if (httpResponse.statusCode() == 200) {
                    if (jsonObject.containsKey("skus")) {
                        JsonArray jsonArray = jsonObject.getJsonArray("skus");
                        if (jsonArray.size() == 0) continue;
                        return CompletableFuture.completedFuture(jsonArray);
                    }
                    this.logger.info("No sizes available (p)");
                    CompletableFuture completableFuture3 = VertxUtil.sleep((long)this.task.getMonitorDelay());
                    if (!completableFuture3.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture3;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Hibbett.async$getSizes(this, string, string2, n, httpRequest, completableFuture4, httpResponse, jsonObject, 0, null, 2, arg_0));
                    }
                    completableFuture3.join();
                    continue;
                }
                this.logger.warn("Waiting for restock (p): status:'{}'", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture5 = this.api.handleBadResponse(httpResponse.statusCode(), jsonObject.containsKey("vid") ? jsonObject.getString("vid") : this.api.getPXToken().getVid(), jsonObject.containsKey("uuid") ? jsonObject.getString("uuid") : null);
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Hibbett.async$getSizes(this, string, string2, n, httpRequest, completableFuture6, httpResponse, jsonObject, 0, null, 3, arg_0));
                }
                int n2 = ((Boolean)completableFuture5.join()).booleanValue() ? 1 : 0;
                if (n2 != 0 || !this.api.isSkip()) continue;
                CompletableFuture completableFuture7 = VertxUtil.randomSleep((long)5000L);
                if (!completableFuture7.isDone()) {
                    CompletableFuture completableFuture8 = completableFuture7;
                    return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Hibbett.async$getSizes(this, string, string2, n, httpRequest, completableFuture8, httpResponse, jsonObject, n2, null, 4, arg_0));
                }
                completableFuture7.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error occurred waiting for restock (p): {}", (Object)throwable.getMessage());
                if (!this.shouldRunOnSchedule()) {
                    return CompletableFuture.completedFuture(null);
                }
                CompletableFuture completableFuture = super.randomSleep(5000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture9 = completableFuture;
                    return ((CompletableFuture)completableFuture9.exceptionally(Function.identity())).thenCompose(arg_0 -> Hibbett.async$getSizes(this, string, string2, n, null, completableFuture9, null, null, 0, throwable, 5, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$run(Hibbett var0, CompletableFuture var1_1, int var2_3, Object var3_6) {
        switch (var2_3) {
            case 0: {
                if (var0.scheduledMode) {
                    var0.clock = RunClock.create();
                    var0.logger.info("Scheduled run planned in {}minute(s)", (Object)TimeUnit.MINUTES.convert(var0.clock.getTimeTillRun(), TimeUnit.MILLISECONDS));
                    v0 = VertxUtil.hardCodedSleep((long)var0.clock.getTimeTillRun());
                    if (!v0.isDone()) {
                        var3_6 = v0;
                        return var3_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.hibbett.Hibbett java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Hibbett)var0, (CompletableFuture)var3_6, (int)1));
                    }
lbl10:
                    // 3 sources

                    while (true) {
                        v0.join();
                        var0.clock.start();
                        var0.logger.info("Running on schedule...");
                        try {
                            v1 = var0.runNormal();
                            if (!v1.isDone()) {
                                var3_6 = v1;
                                return var3_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.hibbett.Hibbett java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Hibbett)var0, (CompletableFuture)var3_6, (int)2));
                            }
lbl20:
                            // 3 sources

                            while (true) {
                                var1_2 = (Boolean)v1.join();
                                break;
                            }
                        }
                        catch (Throwable var2_4) {
                            var2_4.printStackTrace();
                            var1_2 = false;
                        }
                        if (var1_2 != false) return CompletableFuture.completedFuture(null);
                        var2_5 = var0.api.getPXToken();
                        if (var2_5 != null && var2_5.deploymentID() != null && !var2_5.deploymentID().isEmpty()) {
                            var0.vertx.undeploy(var2_5.deploymentID());
                        }
                        var0.api.setPxToken(null);
                        return var0.run();
                    }
                }
                v2 = var0.runNormal();
                if (!v2.isDone()) {
                    var3_6 = v2;
                    return var3_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.hibbett.Hibbett java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Hibbett)var0, (CompletableFuture)var3_6, (int)3));
                }
                ** GOTO lbl46
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
lbl46:
                // 2 sources

                v2.join();
                return CompletableFuture.completedFuture(null);
            }
        }
        throw new IllegalArgumentException();
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$getSizes(Hibbett var0, String var1_1, String var2_2, int var3_3, HttpRequest var4_4, CompletableFuture var5_6, HttpResponse var6_7, JsonObject var7_8, int var8_10, Throwable var9_16, int var10_17, Object var11_18) {
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

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$submitPayment(Hibbett var0, String var1_1, String var2_2, String var3_3, Integer var4_4, String var5_5, String var6_6, JsonObject var7_7, HttpRequest var8_8, CompletableFuture var9_10, HttpResponse var10_11, JsonObject var11_12, int var12_14, Throwable var13_19, int var14_20, Object var15_21) {
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

    public void handleFailureWebhooks(String string, Buffer buffer) {
        if (this.previousResponseHash != 0 && this.previousResponseLen == buffer.length()) {
            if (this.previousResponseHash == buffer.hashCode()) return;
        }
        try {
            Analytics.failure((String)string, (Task)this.task, (JsonObject)buffer.toJsonObject(), (String)this.api.proxyStringSafe());
            this.previousResponseHash = buffer.hashCode();
            this.previousResponseLen = buffer.length();
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public CompletableFuture processPayment(String string, String string2, String string3) {
        int n = 0;
        this.logger.info("Processing...");
        while (this.running) {
            if (n++ >= 99999) return CompletableFuture.completedFuture(false);
            if (!this.shouldRunOnSchedule()) return CompletableFuture.completedFuture(false);
            try {
                HttpRequest httpRequest = this.api.processPayment(string, string2, string3);
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest, (Buffer)Buffer.buffer((String)""));
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Hibbett.async$processPayment(this, string, string2, string3, n, httpRequest, completableFuture2, null, null, 0, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                JsonObject jsonObject = httpResponse.bodyAsJsonObject();
                this.logger.info("Payment resp: {}", (Object)(jsonObject.toString().contains("uuid") ? "CAPTCHA" : jsonObject));
                if (jsonObject.toString().contains("orderDate")) {
                    this.logger.info("Successfully checked out!");
                    this.logger.info(jsonObject.toString());
                    Analytics.success((Task)this.task, (JsonObject)httpResponse.bodyAsJsonObject(), (String)this.api.proxyStringSafe());
                    return CompletableFuture.completedFuture(true);
                }
                if (jsonObject.toString().contains("has been declined")) {
                    this.logger.info("Card declined. Retrying...");
                    this.handleFailureWebhooks("Card decline", (Buffer)httpResponse.body());
                    return CompletableFuture.completedFuture(false);
                }
                if (!jsonObject.toString().contains("captcha") && !jsonObject.toString().contains("block")) {
                    this.logger.warn("Processing payment failed: status:'{}'", (Object)((Buffer)httpResponse.body()).toString());
                    this.handleFailureWebhooks("Misc failure", (Buffer)httpResponse.body());
                } else {
                    this.logger.warn("Processing payment failed: status: CAPTCHA");
                }
                CompletableFuture completableFuture3 = this.api.handleBadResponse(httpResponse.statusCode(), jsonObject.containsKey("vid") ? jsonObject.getString("vid") : this.api.getPXToken().getVid(), jsonObject.containsKey("uuid") ? jsonObject.getString("uuid") : null);
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Hibbett.async$processPayment(this, string, string2, string3, n, httpRequest, completableFuture4, httpResponse, jsonObject, 0, null, 2, arg_0));
                }
                int n2 = ((Boolean)completableFuture3.join()).booleanValue() ? 1 : 0;
                if (n2 != 0 || !this.api.isSkip()) continue;
                CompletableFuture completableFuture5 = VertxUtil.sleep((long)this.task.getRetryDelay());
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Hibbett.async$processPayment(this, string, string2, string3, n, httpRequest, completableFuture6, httpResponse, jsonObject, n2, null, 3, arg_0));
                }
                completableFuture5.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error occurred processing payment: {}", (Object)throwable.getMessage());
                if (!this.shouldRunOnSchedule()) {
                    return CompletableFuture.completedFuture(false);
                }
                CompletableFuture completableFuture = super.randomSleep(5000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> Hibbett.async$processPayment(this, string, string2, string3, n, null, completableFuture7, null, null, 0, throwable, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(false);
    }

    public CompletableFuture runNormal() {
        Object object;
        if (this.task.getMode().contains("api")) {
            object = new PXTokenAPI((TaskActor)this);
            this.api.setS((PXTokenAPI)object);
            this.vertx.deployVerticle((Verticle)object);
        }
        this.api.setPxToken(new PXToken((TaskActor)this, Site.HIBBETT));
        this.vertx.deployVerticle((Verticle)this.api.getPXToken());
        CompletableFuture completableFuture = this.api.getPXToken().awaitInit();
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Hibbett.async$runNormal(this, completableFuture2, null, null, null, null, 0, null, 1, arg_0));
        }
        if (!((Boolean)completableFuture.join()).booleanValue()) {
            this.logger.warn("Failed to initialise and configure task. Stopping...");
        } else {
            HibbettAPI hibbettAPI = this.api;
            CompletableFuture completableFuture3 = VertxSingleton.INSTANCE.getLocalClient().fetchDeviceFromAPI("https://loudounchris.xyz/api/device/mobileDeviceWithOS.json");
            if (!completableFuture3.isDone()) {
                CompletableFuture completableFuture4 = completableFuture3;
                HibbettAPI hibbettAPI2 = hibbettAPI;
                return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Hibbett.async$runNormal(this, completableFuture4, hibbettAPI2, null, null, null, 0, null, 2, arg_0));
            }
            hibbettAPI.setDevice((JsonObject)completableFuture3.join());
            this.logger.info("Getting pages");
            CompletableFuture completableFuture5 = SessionPreload.createSession((Hibbett)this);
            if (!completableFuture5.isDone()) {
                CompletableFuture completableFuture6 = completableFuture5;
                return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Hibbett.async$runNormal(this, completableFuture6, null, null, null, null, 0, null, 3, arg_0));
            }
            object = (Map)completableFuture5.join();
            if (this.griefMode) {
                this.api.swapClient();
            }
            CompletableFuture completableFuture7 = this.getSizes((String)object.get("authorization"), (String)object.get("customerId"));
            if (!completableFuture7.isDone()) {
                CompletableFuture completableFuture8 = completableFuture7;
                return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Hibbett.async$runNormal(this, completableFuture8, null, (Map)object, null, null, 0, null, 4, arg_0));
            }
            JsonArray jsonArray = (JsonArray)completableFuture7.join();
            while (!jsonArray.toString().contains("\"isAvailable\":true")) {
                CompletableFuture completableFuture9 = VertxUtil.sleep((long)this.task.getMonitorDelay());
                if (!completableFuture9.isDone()) {
                    CompletableFuture completableFuture10 = completableFuture9;
                    return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> Hibbett.async$runNormal(this, completableFuture10, null, (Map)object, jsonArray, null, 0, null, 5, arg_0));
                }
                completableFuture9.join();
                CompletableFuture completableFuture11 = this.getSizes((String)object.get("authorization"), (String)object.get("customerId"));
                if (!completableFuture11.isDone()) {
                    CompletableFuture completableFuture12 = completableFuture11;
                    return ((CompletableFuture)completableFuture12.exceptionally(Function.identity())).thenCompose(arg_0 -> Hibbett.async$runNormal(this, completableFuture12, null, (Map)object, jsonArray, null, 0, null, 6, arg_0));
                }
                jsonArray = (JsonArray)completableFuture11.join();
            }
            JsonArray jsonArray2 = this.sortSizes(jsonArray);
            try {
                CompletableFuture completableFuture13 = this.atc(jsonArray2, (String)object.get("authorization"), (String)object.get("customerId"), (String)object.get("cartId"));
                if (!completableFuture13.isDone()) {
                    CompletableFuture completableFuture14 = completableFuture13;
                    return ((CompletableFuture)completableFuture14.exceptionally(Function.identity())).thenCompose(arg_0 -> Hibbett.async$runNormal(this, completableFuture14, null, (Map)object, jsonArray, jsonArray2, 0, null, 7, arg_0));
                }
                int n = (Integer)completableFuture13.join();
                if (this.griefMode) {
                    this.submitPayment((String)object.get("authorization"), (String)object.get("customerId"), (String)object.get("cartId"), n, (String)object.get("encryptedCVNValue"), (String)object.get("ccToken"));
                    CompletableFuture completableFuture15 = this.api.getWebClient().windowUpdateCallback();
                    CompletableFuture completableFuture16 = VertxUtil.handleEagerFuture((CompletableFuture)completableFuture15);
                    if (!completableFuture16.isDone()) {
                        CompletableFuture completableFuture17 = completableFuture16;
                        return ((CompletableFuture)completableFuture17.exceptionally(Function.identity())).thenCompose(arg_0 -> Hibbett.async$runNormal(this, completableFuture15, null, (Map)object, jsonArray, jsonArray2, n, completableFuture17, 8, arg_0));
                    }
                    completableFuture16.join();
                } else {
                    CompletableFuture completableFuture18 = this.submitPayment((String)object.get("authorization"), (String)object.get("customerId"), (String)object.get("cartId"), n, (String)object.get("encryptedCVNValue"), (String)object.get("ccToken"));
                    if (!completableFuture18.isDone()) {
                        CompletableFuture completableFuture19 = completableFuture18;
                        return ((CompletableFuture)completableFuture19.exceptionally(Function.identity())).thenCompose(arg_0 -> Hibbett.async$runNormal(this, completableFuture19, null, (Map)object, jsonArray, jsonArray2, n, null, 9, arg_0));
                    }
                    completableFuture18.join();
                }
                CompletableFuture completableFuture20 = this.processPayment((String)object.get("authorization"), (String)object.get("customerId"), (String)object.get("cartId"));
                if (!completableFuture20.isDone()) {
                    CompletableFuture completableFuture21 = completableFuture20;
                    return ((CompletableFuture)completableFuture21.exceptionally(Function.identity())).thenCompose(arg_0 -> Hibbett.async$runNormal(this, completableFuture21, null, (Map)object, jsonArray, jsonArray2, n, null, 10, arg_0));
                }
                boolean bl = (Boolean)completableFuture20.join();
                return CompletableFuture.completedFuture(bl);
            }
            catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return CompletableFuture.completedFuture(false);
    }

    public Hibbett(Task task, int n) {
        super(n);
        String string;
        this.task = task;
        this.api = new HibbettAPI(this.task);
        super.setClient((TaskApiClient)this.api);
        Matcher matcher = ITEM_ID_PATTERN.matcher(this.task.getKeywords()[0]);
        if (matcher.find()) {
            string = matcher.group(1);
        } else {
            this.logger.error("Error parsing keyword: {}", (Object)this.task.getKeywords()[0]);
            this.logger.warn("Defaulting to '{}' as keyword", (Object)this.task.getKeywords()[0]);
            string = this.task.getKeywords()[0];
        }
        this.itemKeyword = this.instanceSignal = string;
        this.griefMode = false;
        this.scheduledMode = this.task.getMode().contains("schedule");
        this.shardMode = false;
    }

    public CompletableFuture run() {
        if (this.scheduledMode) {
            boolean bl;
            this.clock = RunClock.create();
            this.logger.info("Scheduled run planned in {}minute(s)", (Object)TimeUnit.MINUTES.convert(this.clock.getTimeTillRun(), TimeUnit.MILLISECONDS));
            CompletableFuture completableFuture = VertxUtil.hardCodedSleep((long)this.clock.getTimeTillRun());
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Hibbett.async$run(this, completableFuture2, 1, arg_0));
            }
            completableFuture.join();
            this.clock.start();
            this.logger.info("Running on schedule...");
            try {
                CompletableFuture completableFuture3 = this.runNormal();
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Hibbett.async$run(this, completableFuture4, 2, arg_0));
                }
                bl = (Boolean)completableFuture3.join();
            }
            catch (Throwable throwable) {
                throwable.printStackTrace();
                bl = false;
            }
            if (bl) return CompletableFuture.completedFuture(null);
            PXTokenBase pXTokenBase = this.api.getPXToken();
            if (pXTokenBase != null && pXTokenBase.deploymentID() != null && !pXTokenBase.deploymentID().isEmpty()) {
                this.vertx.undeploy(pXTokenBase.deploymentID());
            }
            this.api.setPxToken(null);
            return this.run();
        }
        CompletableFuture completableFuture = this.runNormal();
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture5 = completableFuture;
            return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> Hibbett.async$run(this, completableFuture5, 3, arg_0));
        }
        completableFuture.join();
        return CompletableFuture.completedFuture(null);
    }

    public Profile getProfile() {
        return this.task.getProfile();
    }

    public JsonObject atcPayload(JsonArray jsonArray, String string, int n) {
        String string2 = this.task.getSize().equals("random") ? jsonArray.getJsonObject(n % jsonArray.size()).getString("id") : this.selectSpecificSize(jsonArray);
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("customerId", (Object)string);
        jsonObject.put("personalizations", (Object)new JsonArray());
        jsonObject.put("product", (Object)new JsonObject().put("id", (Object)this.itemKeyword).put("isRaffle", (Object)false));
        jsonObject.put("quantity", (Object)1);
        jsonObject.put("sku", (Object)new JsonObject().put("id", (Object)string2));
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.put("cartItems", (Object)new JsonArray().add((Object)jsonObject));
        return jsonObject2;
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$atc(Hibbett var0, JsonArray var1_1, String var2_2, String var3_3, String var4_4, int var5_5, JsonObject var6_6, String var7_8, HttpRequest var8_9, CompletableFuture var9_10, HttpResponse var10_11, JsonObject var11_12, int var12_14, Throwable var13_20, int var14_21, Object var15_22) {
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

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    public static CompletableFuture async$runNormal(Hibbett var0, CompletableFuture var1_1, HibbettAPI var2_2, Map var3_3, JsonArray var4_4, JsonArray var5_8, int var6_10, CompletableFuture var7_21, int var8_22, Object var9_23) {
        switch (var8_22) {
            case 0: {
                if (var0.task.getMode().contains("api")) {
                    var1_1 = new PXTokenAPI((TaskActor)var0);
                    var0.api.setS((PXTokenAPI)var1_1);
                    var0.vertx.deployVerticle((Verticle)var1_1);
                }
                var0.api.setPxToken(new PXToken((TaskActor)var0, Site.HIBBETT));
                var0.vertx.deployVerticle((Verticle)var0.api.getPXToken());
                v0 = var0.api.getPXToken().awaitInit();
                if (!v0.isDone()) {
                    var6_11 = v0;
                    return var6_11.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$runNormal(io.trickle.task.sites.hibbett.Hibbett java.util.concurrent.CompletableFuture io.trickle.task.sites.hibbett.HibbettAPI java.util.Map io.vertx.core.json.JsonArray io.vertx.core.json.JsonArray int java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Hibbett)var0, (CompletableFuture)var6_11, null, null, null, null, (int)0, null, (int)1));
                }
lbl15:
                // 3 sources

                while (true) {
                    if (!((Boolean)v0.join()).booleanValue()) ** GOTO lbl90
                    v1 = var0.api;
                    v2 = VertxSingleton.INSTANCE.getLocalClient().fetchDeviceFromAPI("https://loudounchris.xyz/api/device/mobileDeviceWithOS.json");
                    if (!v2.isDone()) {
                        var7_21 = v2;
                        var6_12 = v1;
                        return var7_21.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$runNormal(io.trickle.task.sites.hibbett.Hibbett java.util.concurrent.CompletableFuture io.trickle.task.sites.hibbett.HibbettAPI java.util.Map io.vertx.core.json.JsonArray io.vertx.core.json.JsonArray int java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Hibbett)var0, (CompletableFuture)var7_21, (HibbettAPI)var6_12, null, null, null, (int)0, null, (int)2));
                    }
lbl23:
                    // 3 sources

                    while (true) {
                        v1.setDevice((JsonObject)v2.join());
                        var0.logger.info("Getting pages");
                        v3 = SessionPreload.createSession((Hibbett)var0);
                        if (!v3.isDone()) {
                            var6_13 = v3;
                            return var6_13.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$runNormal(io.trickle.task.sites.hibbett.Hibbett java.util.concurrent.CompletableFuture io.trickle.task.sites.hibbett.HibbettAPI java.util.Map io.vertx.core.json.JsonArray io.vertx.core.json.JsonArray int java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Hibbett)var0, (CompletableFuture)var6_13, null, null, null, null, (int)0, null, (int)3));
                        }
lbl30:
                        // 3 sources

                        while (true) {
                            var1_1 = (Map)v3.join();
                            if (var0.griefMode) {
                                var0.api.swapClient();
                            }
                            if (!(v4 = var0.getSizes((String)var1_1.get("authorization"), (String)var1_1.get("customerId"))).isDone()) {
                                var6_14 = v4;
                                return var6_14.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$runNormal(io.trickle.task.sites.hibbett.Hibbett java.util.concurrent.CompletableFuture io.trickle.task.sites.hibbett.HibbettAPI java.util.Map io.vertx.core.json.JsonArray io.vertx.core.json.JsonArray int java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Hibbett)var0, (CompletableFuture)var6_14, null, (Map)var1_1, null, null, (int)0, null, (int)4));
                            }
lbl37:
                            // 3 sources

                            while (true) {
                                var2_2 = (JsonArray)v4.join();
                                block19: while (true) {
                                    if (var2_2.toString().contains("\"isAvailable\":true")) ** GOTO lbl46
                                    v5 = VertxUtil.sleep((long)var0.task.getMonitorDelay());
                                    if (!v5.isDone()) {
                                        var6_15 = v5;
                                        return var6_15.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$runNormal(io.trickle.task.sites.hibbett.Hibbett java.util.concurrent.CompletableFuture io.trickle.task.sites.hibbett.HibbettAPI java.util.Map io.vertx.core.json.JsonArray io.vertx.core.json.JsonArray int java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Hibbett)var0, (CompletableFuture)var6_15, null, (Map)var1_1, (JsonArray)var2_2, null, (int)0, null, (int)5));
                                    }
                                    ** GOTO lbl80
lbl46:
                                    // 1 sources

                                    var3_3 = var0.sortSizes((JsonArray)var2_2);
                                    try {
                                        v6 = var0.atc((JsonArray)var3_3, (String)var1_1.get("authorization"), (String)var1_1.get("customerId"), (String)var1_1.get("cartId"));
                                        if (!v6.isDone()) {
                                            var6_17 = v6;
                                            return var6_17.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$runNormal(io.trickle.task.sites.hibbett.Hibbett java.util.concurrent.CompletableFuture io.trickle.task.sites.hibbett.HibbettAPI java.util.Map io.vertx.core.json.JsonArray io.vertx.core.json.JsonArray int java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Hibbett)var0, (CompletableFuture)var6_17, null, (Map)var1_1, (JsonArray)var2_2, (JsonArray)var3_3, (int)0, null, (int)7));
                                        }
lbl52:
                                        // 3 sources

                                        while (true) {
                                            var4_5 = (Integer)v6.join();
                                            if (!var0.griefMode) ** GOTO lbl66
                                            var0.submitPayment((String)var1_1.get("authorization"), (String)var1_1.get("customerId"), (String)var1_1.get("cartId"), var4_5, (String)var1_1.get("encryptedCVNValue"), (String)var1_1.get("ccToken"));
                                            var5_8 /* !! */  = var0.api.getWebClient().windowUpdateCallback();
                                            v7 = VertxUtil.handleEagerFuture((CompletableFuture)var5_8 /* !! */ );
                                            if (!v7.isDone()) {
                                                var6_18 = v7;
                                                return var6_18.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$runNormal(io.trickle.task.sites.hibbett.Hibbett java.util.concurrent.CompletableFuture io.trickle.task.sites.hibbett.HibbettAPI java.util.Map io.vertx.core.json.JsonArray io.vertx.core.json.JsonArray int java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Hibbett)var0, (CompletableFuture)var5_8 /* !! */ , null, (Map)var1_1, (JsonArray)var2_2, (JsonArray)var3_3, (int)var4_5, (CompletableFuture)var6_18, (int)8));
                                            }
lbl62:
                                            // 3 sources

                                            while (true) {
                                                v7.join();
                                                ** GOTO lbl73
                                                break;
                                            }
lbl66:
                                            // 1 sources

                                            v8 = var0.submitPayment((String)var1_1.get("authorization"), (String)var1_1.get("customerId"), (String)var1_1.get("cartId"), var4_5, (String)var1_1.get("encryptedCVNValue"), (String)var1_1.get("ccToken"));
                                            if (!v8.isDone()) {
                                                var6_19 = v8;
                                                return var6_19.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$runNormal(io.trickle.task.sites.hibbett.Hibbett java.util.concurrent.CompletableFuture io.trickle.task.sites.hibbett.HibbettAPI java.util.Map io.vertx.core.json.JsonArray io.vertx.core.json.JsonArray int java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Hibbett)var0, (CompletableFuture)var6_19, null, (Map)var1_1, (JsonArray)var2_2, (JsonArray)var3_3, (int)var4_5, null, (int)9));
                                            }
lbl70:
                                            // 3 sources

                                            while (true) {
                                                v8.join();
lbl73:
                                                // 2 sources

                                                if (!(v9 = var0.processPayment((String)var1_1.get("authorization"), (String)var1_1.get("customerId"), (String)var1_1.get("cartId"))).isDone()) {
                                                    var6_20 = v9;
                                                    return var6_20.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$runNormal(io.trickle.task.sites.hibbett.Hibbett java.util.concurrent.CompletableFuture io.trickle.task.sites.hibbett.HibbettAPI java.util.Map io.vertx.core.json.JsonArray io.vertx.core.json.JsonArray int java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Hibbett)var0, (CompletableFuture)var6_20, null, (Map)var1_1, (JsonArray)var2_2, (JsonArray)var3_3, (int)var4_5, null, (int)10));
                                                }
                                                ** GOTO lbl148
                                                break;
                                            }
                                            break;
                                        }
                                    }
                                    catch (Throwable var4_6) {
                                        var4_6.printStackTrace();
                                        ** GOTO lbl91
                                    }
lbl80:
                                    // 2 sources

                                    while (true) {
                                        v5.join();
                                        v10 = var0.getSizes((String)var1_1.get("authorization"), (String)var1_1.get("customerId"));
                                        if (!v10.isDone()) {
                                            var6_16 = v10;
                                            return var6_16.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$runNormal(io.trickle.task.sites.hibbett.Hibbett java.util.concurrent.CompletableFuture io.trickle.task.sites.hibbett.HibbettAPI java.util.Map io.vertx.core.json.JsonArray io.vertx.core.json.JsonArray int java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Hibbett)var0, (CompletableFuture)var6_16, null, (Map)var1_1, (JsonArray)var2_2, null, (int)0, null, (int)6));
                                        }
lbl87:
                                        // 3 sources

                                        while (true) {
                                            var2_2 = (JsonArray)v10.join();
                                            continue block19;
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
lbl90:
                    // 1 sources

                    var0.logger.warn("Failed to initialise and configure task. Stopping...");
lbl91:
                    // 2 sources

                    return CompletableFuture.completedFuture(false);
                }
            }
            case 1: {
                v0 = var1_1;
                ** continue;
            }
            case 2: {
                v1 = var2_2;
                v2 = var1_1;
                ** continue;
            }
            case 3: {
                v3 = var1_1;
                ** continue;
            }
            case 4: {
                v4 = var1_1;
                var1_1 = var3_3;
                ** continue;
            }
            case 5: {
                v5 = var1_1;
                var2_2 = var4_4;
                var1_1 = var3_3;
                ** continue;
            }
            case 6: {
                v10 = var1_1;
                var2_2 = var4_4;
                var1_1 = var3_3;
                ** continue;
            }
            case 7: {
                v6 = var1_1;
                v11 = var3_3;
                var3_3 = var5_8 /* !! */ ;
                var2_2 = var4_4;
                var1_1 = v11;
                ** continue;
            }
            case 8: {
                v7 = var7_21;
                v12 = var3_3;
                v13 = var5_8 /* !! */ ;
                var5_8 /* !! */  = var1_1;
                var4_5 = var6_10;
                var3_3 = v13;
                var2_2 = var4_4;
                var1_1 = v12;
                ** continue;
            }
            case 9: {
                v8 = var1_1;
                v14 = var3_3;
                var4_5 = var6_10;
                var3_3 = var5_8 /* !! */ ;
                var2_2 = var4_4;
                var1_1 = v14;
                ** continue;
            }
            case 10: {
                v9 = var1_1;
                v15 = var3_3;
                var4_7 = var6_10;
                var3_3 = var5_8 /* !! */ ;
                var2_2 = var4_4;
                var1_1 = v15;
lbl148:
                // 2 sources

                var5_9 = (Boolean)v9.join();
                return CompletableFuture.completedFuture(var5_9);
            }
        }
        throw new IllegalArgumentException();
    }

    public JsonArray sortSizes(JsonArray jsonArray) {
        JsonArray jsonArray2 = new JsonArray();
        while (jsonArray.size() != 0) {
            int n = ThreadLocalRandom.current().nextInt(jsonArray.size());
            JsonObject jsonObject = jsonArray.getJsonObject(n);
            if (jsonObject.getBoolean("isAvailable").booleanValue()) {
                jsonArray2.add(0, (Object)jsonObject);
            } else {
                jsonArray2.add((Object)jsonObject);
            }
            jsonArray.remove(n);
        }
        return jsonArray2;
    }

    public String selectSpecificSize(JsonArray jsonArray) {
        int n = 0;
        while (n < jsonArray.size()) {
            JsonObject jsonObject = jsonArray.getJsonObject(n);
            if (jsonObject.getString("size").replace(".0", "").equals(this.task.getSize())) {
                return jsonObject.getString("id");
            }
            ++n;
        }
        throw new Exception("Size not found");
    }

    public CompletableFuture submitPayment(String string, String string2, String string3, Integer n, String string4, String string5) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("amount", (Object)n);
        jsonObject.put("encryptedCVNValue", (Object)string4);
        jsonObject.put("paymentObject", (Object)new JsonObject().put("cardType", (Object)this.getProfile().getPaymentMethod().getFirstLetterUppercase()).put("creditCardToken", (Object)string5).put("expirationMonth", (Object)Integer.parseInt(this.getProfile().getExpiryMonth())).put("expirationYear", (Object)Integer.parseInt(this.getProfile().getExpiryYear())).put("nameOnCard", (Object)(this.getProfile().getFirstName() + " " + this.getProfile().getLastName())).put("number", (Object)("************" + this.getProfile().getCardNumber().substring(12))));
        jsonObject.put("type", (Object)"CREDIT_CARD");
        this.logger.info("Submitting payment");
        while (this.running) {
            if (!this.shouldRunOnSchedule()) return CompletableFuture.completedFuture(null);
            try {
                HttpRequest httpRequest = this.api.submitPayment(string, string2, string3);
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest, (Buffer)jsonObject.toBuffer());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Hibbett.async$submitPayment(this, string, string2, string3, n, string4, string5, jsonObject, httpRequest, completableFuture2, null, null, 0, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 200) {
                    return CompletableFuture.completedFuture(null);
                }
                this.logger.warn("Submitting billing: status:'{}'", (Object)httpResponse.statusCode());
                JsonObject jsonObject2 = httpResponse.bodyAsJsonObject();
                CompletableFuture completableFuture3 = this.api.handleBadResponse(httpResponse.statusCode(), jsonObject2.containsKey("vid") ? jsonObject2.getString("vid") : this.api.getPXToken().getVid(), jsonObject2.containsKey("uuid") ? jsonObject2.getString("uuid") : null);
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Hibbett.async$submitPayment(this, string, string2, string3, n, string4, string5, jsonObject, httpRequest, completableFuture4, httpResponse, jsonObject2, 0, null, 2, arg_0));
                }
                int n2 = ((Boolean)completableFuture3.join()).booleanValue() ? 1 : 0;
                if (n2 != 0 || !this.api.isSkip()) continue;
                CompletableFuture completableFuture5 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Hibbett.async$submitPayment(this, string, string2, string3, n, string4, string5, jsonObject, httpRequest, completableFuture6, httpResponse, jsonObject2, n2, null, 3, arg_0));
                }
                completableFuture5.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error occurred billing: {}", (Object)throwable.getMessage());
                if (!this.shouldRunOnSchedule()) {
                    return CompletableFuture.completedFuture(null);
                }
                CompletableFuture completableFuture = super.randomSleep(5000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> Hibbett.async$submitPayment(this, string, string2, string3, n, string4, string5, jsonObject, null, completableFuture7, null, null, 0, throwable, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture atc(JsonArray jsonArray, String string, String string2, String string3) {
        int n = -1;
        this.logger.info("Adding to cart");
        while (this.running) {
            if (!this.shouldRunOnSchedule()) return CompletableFuture.completedFuture(null);
            try {
                JsonObject jsonObject = this.atcPayload(jsonArray, string2, ++n);
                String string4 = jsonObject.getJsonArray("cartItems").getJsonObject(0).getJsonObject("sku").getString("id");
                HttpRequest httpRequest = this.api.atc(string, string2, string3, string4);
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest, (Buffer)jsonObject.toBuffer());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Hibbett.async$atc(this, jsonArray, string, string2, string3, n, jsonObject, string4, httpRequest, completableFuture2, null, null, 0, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                JsonObject jsonObject2 = httpResponse.bodyAsJsonObject();
                if (httpResponse.statusCode() == 200) {
                    if (!jsonObject2.toString().contains("quantity restriction") && !jsonObject2.toString().contains("isn't available") && jsonObject2.containsKey("itemCount") && jsonObject2.getInteger("itemCount") == 1) {
                        VertxUtil.sendSignal((String)this.instanceSignal, (Object)string4);
                        return CompletableFuture.completedFuture(jsonObject2.getNumber("total") == null ? jsonObject2.getInteger("subTotal") : jsonObject2.getInteger("total"));
                    }
                    this.logger.info("Failed to ATC (empty cart)");
                    CompletableFuture completableFuture3 = VertxUtil.signalSleep((String)this.instanceSignal, (long)this.task.getMonitorDelay());
                    if (!completableFuture3.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture3;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Hibbett.async$atc(this, jsonArray, string, string2, string3, n, jsonObject, string4, httpRequest, completableFuture4, httpResponse, jsonObject2, 0, null, 2, arg_0));
                    }
                    completableFuture3.join();
                    continue;
                }
                this.logger.warn("Failed ATC: status:'{}'", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture5 = this.api.handleBadResponse(httpResponse.statusCode(), jsonObject2.containsKey("vid") ? jsonObject2.getString("vid") : this.api.getPXToken().getVid(), jsonObject2.containsKey("uuid") ? jsonObject2.getString("uuid") : null);
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Hibbett.async$atc(this, jsonArray, string, string2, string3, n, jsonObject, string4, httpRequest, completableFuture6, httpResponse, jsonObject2, 0, null, 3, arg_0));
                }
                int n2 = ((Boolean)completableFuture5.join()).booleanValue() ? 1 : 0;
                if (n2 != 0 || !this.api.isSkip()) continue;
                CompletableFuture completableFuture7 = VertxUtil.signalSleep((String)this.instanceSignal, (long)this.task.getMonitorDelay());
                if (!completableFuture7.isDone()) {
                    CompletableFuture completableFuture8 = completableFuture7;
                    return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Hibbett.async$atc(this, jsonArray, string, string2, string3, n, jsonObject, string4, httpRequest, completableFuture8, httpResponse, jsonObject2, n2, null, 4, arg_0));
                }
                completableFuture7.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error occurred ATC: {}", (Object)throwable.getMessage());
                throwable.printStackTrace();
                if (!this.shouldRunOnSchedule()) {
                    return CompletableFuture.completedFuture(null);
                }
                CompletableFuture completableFuture = super.randomSleep(5000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture9 = completableFuture;
                    return ((CompletableFuture)completableFuture9.exceptionally(Function.identity())).thenCompose(arg_0 -> Hibbett.async$atc(this, jsonArray, string, string2, string3, n, null, null, null, completableFuture9, null, null, 0, throwable, 5, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public boolean shouldRunOnSchedule() {
        if (!this.scheduledMode) return true;
        return !this.clock.isStopped();
    }
}
