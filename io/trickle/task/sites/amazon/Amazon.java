/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.handler.codec.http.cookie.ClientCookieDecoder
 *  io.netty.handler.codec.http.cookie.Cookie
 *  io.trickle.account.Account
 *  io.trickle.account.AccountController
 *  io.trickle.core.Controller
 *  io.trickle.core.Engine
 *  io.trickle.core.actor.TaskActor
 *  io.trickle.harvester.LoginHarvester
 *  io.trickle.task.Task
 *  io.trickle.task.sites.Site
 *  io.trickle.task.sites.amazon.AmazonAPI
 *  io.trickle.task.sites.amazon.Patterns
 *  io.trickle.task.sites.shopify.util.Triplet
 *  io.trickle.util.Pair
 *  io.trickle.util.Utils
 *  io.trickle.util.analytics.Analytics
 *  io.trickle.util.concurrent.VertxUtil
 *  io.trickle.util.request.Request
 *  io.trickle.webclient.TaskApiClient
 *  io.vertx.core.MultiMap
 *  io.vertx.core.json.JsonArray
 *  io.vertx.core.json.JsonObject
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.client.HttpResponse
 */
package io.trickle.task.sites.amazon;

import io.netty.handler.codec.http.cookie.ClientCookieDecoder;
import io.netty.handler.codec.http.cookie.Cookie;
import io.trickle.account.Account;
import io.trickle.account.AccountController;
import io.trickle.core.Controller;
import io.trickle.core.Engine;
import io.trickle.core.actor.TaskActor;
import io.trickle.harvester.LoginHarvester;
import io.trickle.task.Task;
import io.trickle.task.sites.Site;
import io.trickle.task.sites.amazon.AmazonAPI;
import io.trickle.task.sites.amazon.Patterns;
import io.trickle.task.sites.shopify.util.Triplet;
import io.trickle.util.Pair;
import io.trickle.util.Utils;
import io.trickle.util.analytics.Analytics;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.trickle.webclient.TaskApiClient;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import java.lang.invoke.LambdaMetafactory;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class Amazon
extends TaskActor {
    public static Pattern OM_UPSELL_CNT;
    public static Pattern INVARIANT;
    public static Pattern IS_SHIP_WHNEVR_VAL_2;
    public static Pattern PREV_SHIP_OFF_ID;
    public static Pattern RID_PAT;
    public static Pattern PREV_SHIP_PRIORITY;
    public static Pattern SHIP_TRIAL_PFX;
    public static Pattern PROMISE_TIME;
    public static Pattern SHIP_SPLIT_PRIORITY_2;
    public static String[] successfulRedirectIndicators;
    public static Pattern SCOPE;
    public static CompletableFuture[] harvesterFutures;
    public static Pattern ANTI_CSRF_PAT;
    public static Pattern TOTAL_PAT;
    public static Pattern LINE_ITEM_IDS;
    public static Pattern SELECTED_SHIP_SPD;
    public static Pattern CURRENCY_PAT;
    public static Pattern FIRST_TIMER;
    public static Pattern SNS_UPSELL_CNT;
    public static Pattern COUNTDOWN_ID;
    public static Pattern ANTI_CSRF_PAT_P_PAGE;
    public static Pattern PREV_GUARENTEE_TYPE;
    public Task task;
    public static Pattern ORDER_ZERO;
    public static Pattern DUP_ORDER_CHECK;
    public static Pattern SHIP_OFFER_2;
    public static Pattern GROUP_CNT;
    public static String[] successfulCartIndicators;
    public static Pattern PID_PAT;
    public static Pattern VAS_MODEL;
    public static Pattern SELECTED_PAYMENT;
    public static Pattern PREV_ISSS;
    public static Pattern PURCHASE_ID;
    public static Pattern SIMPL_COUNTDOWN;
    public String instanceSignal;
    public String[] successfulFinalPageIndicators = new String[]{">Shipping address<", "Amazon.com Checkout</title>"};
    public static Pattern GUARENTEE_1;
    public static Pattern WEBLAB_PAT;
    public static Pattern PROMISE_ASIN;
    public static Pattern IS_SHIP_COMPL_VAL_2;
    public JsonObject itemJson;
    public AmazonAPI api;
    public static Pattern ISSS_2;
    public static Pattern IS_SHIP_WHNEVR_VAL;
    public static Pattern CURR_SHIP_SPD;
    public static Pattern IS_SHIP_COMPL_VAL;
    public static Pattern CTB;
    public static Pattern PREV_SHIP_SPD;
    public Boolean isTurbo;
    public static Pattern PURCHASE_CUST_ID;
    public static Pattern GUARENTEE_2;
    public static Pattern COUNTDOWN_THRESH;
    public static Pattern SHIP_PRIORITY_0_WHEN_CMPL;
    public static Pattern CURR_SHIP_SPLIT_PREF;
    public static Pattern CSRF_PAT;
    public static Pattern SHIP_SPLIT_PRIORITY_1;
    public static Pattern FAST_TRACK_EXP;

    public CompletableFuture smoothLogin() {
        CompletionStage completionStage;
        AccountController accountController = (AccountController)Engine.get().getModule(Controller.ACCOUNT);
        CompletableFuture completableFuture = accountController.findAccount(this.task.getProfile().getEmail(), true).toCompletionStage().toCompletableFuture();
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Amazon.async$smoothLogin(this, accountController, completableFuture2, null, 1, arg_0));
        }
        Account account = (Account)completableFuture.join();
        if (account == null) {
            this.logger.warn("No accounts available. Sleeping forever...");
            CompletableFuture completableFuture3 = VertxUtil.hardCodedSleep((long)Long.MAX_VALUE);
            if (!completableFuture3.isDone()) {
                CompletableFuture completableFuture4 = completableFuture3;
                return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Amazon.async$smoothLogin(this, accountController, completableFuture4, account, 2, arg_0));
            }
            completableFuture3.join();
            return CompletableFuture.completedFuture(null);
        }
        this.logger.info("Logging in to account '{}'", (Object)account.getUser());
        account.setSite(String.valueOf(Site.AMAZON));
        CompletableFuture completableFuture5 = this.sessionLogon(account);
        if (!completableFuture5.isDone()) {
            CompletableFuture completableFuture6 = completableFuture5;
            return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Amazon.async$smoothLogin(this, accountController, completableFuture6, account, 3, arg_0));
        }
        if (((Boolean)completableFuture5.join()).booleanValue()) {
            this.logger.info("Logged in successfully to account '{}'", (Object)account.getUser());
            return CompletableFuture.completedFuture(null);
        }
        CompletionStage completionStage2 = completionStage = null;
        if (!completionStage2.toCompletableFuture().isDone()) {
            CompletionStage completionStage3 = completionStage2;
            return completionStage3.exceptionally(Function.identity()).thenCompose(arg_0 -> Amazon.async$smoothLogin(this, accountController, null, account, 4, arg_0)).toCompletableFuture();
        }
        completionStage2.toCompletableFuture().join();
        account.setSessionString(this.api.getCookies().asJson().encode());
        this.vertx.eventBus().send("accounts.writer.session", (Object)account);
        CompletableFuture completableFuture7 = VertxUtil.hardCodedSleep((long)Long.MAX_VALUE);
        if (!completableFuture7.isDone()) {
            CompletableFuture completableFuture8 = completableFuture7;
            return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Amazon.async$smoothLogin(this, accountController, completableFuture8, account, 5, arg_0));
        }
        completableFuture7.join();
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$placeOrderBuynow(Amazon var0, String var1_1, String var2_2, String var3_3, String var4_4, int var5_5, CompletableFuture var6_6, HttpResponse var7_8, Throwable var8_9, int var9_10, Object var10_11) {
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
    public static CompletableFuture async$cartHtml(Amazon var0, CompletableFuture var1_1, String var2_2, String var3_3, String var4_4, Triplet var5_6, HttpResponse var6_7, String var7_8, int var8_10, Throwable var9_16, int var10_17, Object var11_18) {
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

    public CompletableFuture run() {
        CompletableFuture<Void> completableFuture = CompletableFuture.allOf(harvesterFutures);
        if (!completableFuture.isDone()) {
            CompletableFuture<Void> completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Amazon.async$run(this, completableFuture2, null, null, 1, arg_0));
        }
        completableFuture.join();
        try {
            CompletableFuture completableFuture3 = this.smoothLogin();
            if (!completableFuture3.isDone()) {
                CompletableFuture completableFuture4 = completableFuture3;
                return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Amazon.async$run(this, completableFuture4, null, null, 2, arg_0));
            }
            completableFuture3.join();
            CompletableFuture completableFuture5 = this.GETREQ("Init api", this.api.corsairPage(), 200, new String[]{"amazonApiCsrfToken"});
            if (!completableFuture5.isDone()) {
                CompletableFuture completableFuture6 = completableFuture5;
                return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Amazon.async$run(this, completableFuture6, null, null, 3, arg_0));
            }
            String string = (String)completableFuture5.join();
            this.api.cartAPI.apiCsrf = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{Patterns.API_CSRF});
            CompletableFuture completableFuture7 = this.GETREQ("Checking page...", this.api.productPage(), 200, new String[]{""});
            if (!completableFuture7.isDone()) {
                CompletableFuture completableFuture8 = completableFuture7;
                return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Amazon.async$run(this, completableFuture8, string, null, 4, arg_0));
            }
            String string2 = (String)completableFuture7.join();
            this.itemJson = new JsonObject().put("title", (Object)Utils.quickParseFirst((String)string2, (Pattern[])new Pattern[]{Patterns.TITLE}));
            CompletableFuture completableFuture9 = this.mixedFlow();
            if (!completableFuture9.isDone()) {
                CompletableFuture completableFuture10 = completableFuture9;
                return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> Amazon.async$run(this, completableFuture10, string, string2, 5, arg_0));
            }
            completableFuture9.join();
        }
        catch (Exception exception) {
            exception.printStackTrace();
            this.logger.error("Task interrupted: " + exception.getMessage());
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$placeOrderNormal(Amazon var0, MultiMap var1_1, int var2_2, CompletableFuture var3_3, HttpResponse var4_5, Throwable var5_6, int var6_7, Object var7_8) {
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
     * Unable to fully structure code
     */
    public static CompletableFuture async$mixedFlow(Amazon var0, CompletableFuture var1_1, String var2_2, CompletableFuture var3_4, CompletableFuture var4_5, CompletableFuture var5_6, int var6_7, Object var7_9) {
        switch (var6_7) {
            case 0: {
                v0 = var0.cartHtml();
                if (!v0.isDone()) {
                    var48_10 = v0;
                    return var48_10.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$mixedFlow(io.trickle.task.sites.amazon.Amazon java.util.concurrent.CompletableFuture java.lang.String java.util.concurrent.CompletableFuture java.util.concurrent.CompletableFuture java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Amazon)var0, (CompletableFuture)var48_10, null, null, null, null, (int)1));
                }
                ** GOTO lbl10
            }
            case 1: {
                v0 = var1_1;
lbl10:
                // 2 sources

                var1_1 = (String)v0.join();
                if (var0.isTurbo == null) ** GOTO lbl37
                if (var0.isTurbo.booleanValue()) ** GOTO lbl28
                var2_2 = var0.GETREQ("Checking cart 1", var0.api.prefetch("https://www.amazon.com/gp/cart/checkout-prefetch.html?ie=UTF8&checkAuthentication=1&checkDefaults=1&cartInitiateId=" + System.currentTimeMillis() + "&partialCheckoutCart=1"), 200, null);
                var3_4 = var0.GETREQ("Checking cart 2", var0.api.naturalFinalPage(), 200, new String[]{">Shipping address<"});
                var4_5 = var0.GETREQ("Proceeding...", var0.api.proceedToCheckout(), 200, new String[]{""});
                v1 = VertxUtil.hardCodedSleep((long)1000L);
                if (!v1.isDone()) {
                    var48_11 = v1;
                    return var48_11.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$mixedFlow(io.trickle.task.sites.amazon.Amazon java.util.concurrent.CompletableFuture java.lang.String java.util.concurrent.CompletableFuture java.util.concurrent.CompletableFuture java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Amazon)var0, (CompletableFuture)var2_2, (String)var1_1, (CompletableFuture)var3_4, (CompletableFuture)var4_5, (CompletableFuture)var48_11, (int)2));
                }
                ** GOTO lbl26
            }
            case 2: {
                v1 = var5_6;
                v2 = var2_2;
                var2_2 = var1_1;
                var1_1 = v2;
lbl26:
                // 2 sources

                v1.join();
lbl28:
                // 2 sources

                if (!(v3 = var0.noCartJack((String)var1_1)).isDone()) {
                    var48_12 = v3;
                    return var48_12.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$mixedFlow(io.trickle.task.sites.amazon.Amazon java.util.concurrent.CompletableFuture java.lang.String java.util.concurrent.CompletableFuture java.util.concurrent.CompletableFuture java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Amazon)var0, (CompletableFuture)var48_12, (String)var1_1, null, null, null, (int)3));
                }
                ** GOTO lbl35
            }
            case 3: {
                v3 = var1_1;
                var1_1 = var2_2;
lbl35:
                // 2 sources

                if (var2_3 = ((Boolean)v3.join()).booleanValue()) {
                    return CompletableFuture.completedFuture(null);
                }
lbl37:
                // 3 sources

                if (var0.isTurbo == null) return var0.buynow((String)var1_1);
                if (var0.isTurbo.booleanValue()) {
                    return var0.buynow((String)var1_1);
                }
                if (!var1_1.contains(">Shipping address<")) ** GOTO lbl43
                var2_2 = var1_1;
                ** GOTO lbl52
lbl43:
                // 1 sources

                v4 = var0.GETREQ("Checking cart", var0.api.naturalFinalPage(), 200, new String[]{">Shipping address<"});
                if (!v4.isDone()) {
                    var48_13 = v4;
                    return var48_13.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$mixedFlow(io.trickle.task.sites.amazon.Amazon java.util.concurrent.CompletableFuture java.lang.String java.util.concurrent.CompletableFuture java.util.concurrent.CompletableFuture java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Amazon)var0, (CompletableFuture)var48_13, (String)var1_1, null, null, null, (int)4));
                }
                ** GOTO lbl51
            }
            case 4: {
                v4 = var1_1;
                var1_1 = var2_2;
lbl51:
                // 2 sources

                var2_2 = (String)v4.join();
lbl52:
                // 2 sources

                var3_4 = Utils.quickParseFirst((String)var2_2, (Pattern[])new Pattern[]{Amazon.CSRF_PAT});
                var4_5 = Utils.quickParseFirst((String)var2_2, (Pattern[])new Pattern[]{Amazon.TOTAL_PAT});
                var5_6 = Utils.quickParseFirst((String)var2_2, (Pattern[])new Pattern[]{Amazon.CURRENCY_PAT});
                var6_8 = Utils.quickParseFirst((String)var2_2, (Pattern[])new Pattern[]{Amazon.PURCHASE_ID});
                var7_9 = Utils.quickParseFirst((String)var2_2, (Pattern[])new Pattern[]{Amazon.PURCHASE_CUST_ID});
                var8_14 = Utils.quickParseFirst((String)var2_2, (Pattern[])new Pattern[]{Amazon.CTB});
                var9_15 = Utils.quickParseFirst((String)var2_2, (Pattern[])new Pattern[]{Amazon.SCOPE});
                var10_16 = Utils.quickParseFirst((String)var2_2, (Pattern[])new Pattern[]{Amazon.INVARIANT});
                var11_17 = Utils.quickParseFirst((String)var2_2, (Pattern[])new Pattern[]{Amazon.PROMISE_TIME});
                var12_18 = Utils.quickParseFirst((String)var2_2, (Pattern[])new Pattern[]{Amazon.PROMISE_ASIN});
                var13_19 = Utils.quickParseFirst((String)var2_2, (Pattern[])new Pattern[]{Amazon.SELECTED_PAYMENT});
                var14_20 = Utils.quickParseFirst((String)var2_2, (Pattern[])new Pattern[]{Amazon.FAST_TRACK_EXP});
                var15_21 = Utils.quickParseFirst((String)var2_2, (Pattern[])new Pattern[]{Amazon.COUNTDOWN_THRESH});
                var16_22 = Utils.quickParseFirst((String)var2_2, (Pattern[])new Pattern[]{Amazon.COUNTDOWN_ID});
                var17_23 = Utils.quickParseFirst((String)var2_2, (Pattern[])new Pattern[]{Amazon.SIMPL_COUNTDOWN});
                var18_24 = Utils.quickParseFirst((String)var2_2, (Pattern[])new Pattern[]{Amazon.DUP_ORDER_CHECK});
                var19_25 = Utils.quickParseFirst((String)var2_2, (Pattern[])new Pattern[]{Amazon.ORDER_ZERO});
                var20_26 = Utils.quickParseFirst((String)var2_2, (Pattern[])new Pattern[]{Amazon.SELECTED_SHIP_SPD});
                var21_27 = Utils.quickParseFirst((String)var2_2, (Pattern[])new Pattern[]{Amazon.GUARENTEE_1});
                var22_28 = "1";
                var23_29 = Utils.quickParseFirst((String)var2_2, (Pattern[])new Pattern[]{Amazon.SHIP_SPLIT_PRIORITY_1});
                var24_30 = Utils.quickParseFirst((String)var2_2, (Pattern[])new Pattern[]{Amazon.IS_SHIP_COMPL_VAL});
                var25_31 = Utils.quickParseFirst((String)var2_2, (Pattern[])new Pattern[]{Amazon.IS_SHIP_WHNEVR_VAL});
                var26_32 = Utils.quickParseFirst((String)var2_2, (Pattern[])new Pattern[]{Amazon.SHIP_OFFER_2});
                var27_33 = Utils.quickParseFirst((String)var2_2, (Pattern[])new Pattern[]{Amazon.GUARENTEE_2});
                var28_34 = Utils.quickParseFirst((String)var2_2, (Pattern[])new Pattern[]{Amazon.ISSS_2});
                var29_35 = Utils.quickParseFirst((String)var2_2, (Pattern[])new Pattern[]{Amazon.SHIP_SPLIT_PRIORITY_2});
                var30_36 = Utils.quickParseFirst((String)var2_2, (Pattern[])new Pattern[]{Amazon.IS_SHIP_COMPL_VAL_2});
                var31_37 = Utils.quickParseFirst((String)var2_2, (Pattern[])new Pattern[]{Amazon.IS_SHIP_WHNEVR_VAL_2});
                var32_38 = Utils.quickParseFirst((String)var2_2, (Pattern[])new Pattern[]{Amazon.PREV_SHIP_OFF_ID});
                var33_39 = Utils.quickParseFirst((String)var2_2, (Pattern[])new Pattern[]{Amazon.PREV_GUARENTEE_TYPE});
                var34_40 = Utils.quickParseFirst((String)var2_2, (Pattern[])new Pattern[]{Amazon.PREV_ISSS});
                var35_41 = Utils.quickParseFirst((String)var2_2, (Pattern[])new Pattern[]{Amazon.PREV_SHIP_PRIORITY});
                var36_42 = Utils.quickParseFirst((String)var2_2, (Pattern[])new Pattern[]{Amazon.LINE_ITEM_IDS});
                var37_43 = Utils.quickParseFirst((String)var2_2, (Pattern[])new Pattern[]{Amazon.CURR_SHIP_SPD});
                var38_44 = Utils.quickParseFirst((String)var2_2, (Pattern[])new Pattern[]{Amazon.PREV_SHIP_SPD});
                var39_45 = Utils.quickParseFirst((String)var2_2, (Pattern[])new Pattern[]{Amazon.CURR_SHIP_SPLIT_PREF});
                var40_46 = Utils.quickParseFirst((String)var2_2, (Pattern[])new Pattern[]{Amazon.SHIP_PRIORITY_0_WHEN_CMPL});
                var41_47 = Utils.quickParseFirst((String)var2_2, (Pattern[])new Pattern[]{Amazon.GROUP_CNT});
                var42_48 = Utils.quickParseFirst((String)var2_2, (Pattern[])new Pattern[]{Amazon.SNS_UPSELL_CNT});
                var43_49 = Utils.quickParseFirst((String)var2_2, (Pattern[])new Pattern[]{Amazon.OM_UPSELL_CNT});
                var44_50 = Utils.quickParseFirst((String)var2_2, (Pattern[])new Pattern[]{Amazon.VAS_MODEL});
                var45_51 = Utils.quickParseFirst((String)var2_2, (Pattern[])new Pattern[]{Amazon.SHIP_TRIAL_PFX});
                var46_52 = Utils.quickParseFirst((String)var2_2, (Pattern[])new Pattern[]{Amazon.FIRST_TIMER});
                var47_53 = var0.api.normalPlaceOrderForm((String)var3_4, (String)var4_5, (String)var5_6, var6_8, (String)var7_9, var8_14, var9_15, var10_16, var11_17, var12_18, var13_19, var14_20, var15_21, var16_22, var17_23, var18_24, var19_25, var20_26, var21_27, "1", var23_29, var24_30, var25_31, var26_32, var27_33, var28_34, var29_35, var30_36, var31_37, var32_38, var33_39, var34_40, var35_41, var36_42, var37_43, var38_44, var39_45, var40_46, var41_47, var42_48, var43_49, var44_50, var45_51, var46_52);
                return var0.placeOrderNormal(var47_53);
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture noCartJack(String string) {
        int n = 0;
        while (n < 250) {
            CompletableFuture completableFuture = this.GETREQ("Placing order [exp]", this.api.placeOrderExp(), null, new String[]{""});
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Amazon.async$noCartJack(this, string, n, completableFuture2, null, 1, arg_0));
            }
            String string2 = (String)completableFuture.join();
            if (string2.contains("a-color-success a-text-bold")) {
                this.logger.info("Successfully checked out!");
                Analytics.success((Task)this.task, (JsonObject)this.itemJson, (String)this.api.proxyStringSafe());
                return CompletableFuture.completedFuture(true);
            }
            this.logger.error("Checkout failed [exp]. Retrying...");
            if (n > 5) {
                CompletableFuture completableFuture3 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Amazon.async$noCartJack(this, string, n, completableFuture4, string2, 2, arg_0));
                }
                completableFuture3.join();
            }
            ++n;
        }
        return CompletableFuture.completedFuture(false);
    }

    public CompletableFuture mixedFlow() {
        String string;
        Object object;
        Object object2;
        CompletableFuture completableFuture = this.cartHtml();
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Amazon.async$mixedFlow(this, completableFuture2, null, null, null, null, 1, arg_0));
        }
        String string2 = (String)completableFuture.join();
        if (this.isTurbo != null) {
            if (!this.isTurbo.booleanValue()) {
                CompletableFuture completableFuture3 = this.GETREQ("Checking cart 1", this.api.prefetch("https://www.amazon.com/gp/cart/checkout-prefetch.html?ie=UTF8&checkAuthentication=1&checkDefaults=1&cartInitiateId=" + System.currentTimeMillis() + "&partialCheckoutCart=1"), 200, null);
                object2 = this.GETREQ("Checking cart 2", this.api.naturalFinalPage(), 200, new String[]{">Shipping address<"});
                object = this.GETREQ("Proceeding...", this.api.proceedToCheckout(), 200, new String[]{""});
                CompletableFuture completableFuture4 = VertxUtil.hardCodedSleep((long)1000L);
                if (!completableFuture4.isDone()) {
                    CompletableFuture completableFuture5 = completableFuture4;
                    return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> Amazon.async$mixedFlow(this, completableFuture3, string2, (CompletableFuture)object2, (CompletableFuture)object, completableFuture5, 2, arg_0));
                }
                completableFuture4.join();
            }
            CompletableFuture completableFuture6 = this.noCartJack(string2);
            if (!completableFuture6.isDone()) {
                CompletableFuture completableFuture7 = completableFuture6;
                return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> Amazon.async$mixedFlow(this, completableFuture7, string2, null, null, null, 3, arg_0));
            }
            boolean bl = (Boolean)completableFuture6.join();
            if (bl) {
                return CompletableFuture.completedFuture(null);
            }
        }
        if (this.isTurbo == null) return this.buynow(string2);
        if (this.isTurbo.booleanValue()) {
            return this.buynow(string2);
        }
        if (string2.contains(">Shipping address<")) {
            string = string2;
        } else {
            CompletableFuture completableFuture8 = this.GETREQ("Checking cart", this.api.naturalFinalPage(), 200, new String[]{">Shipping address<"});
            if (!completableFuture8.isDone()) {
                CompletableFuture completableFuture9 = completableFuture8;
                return ((CompletableFuture)completableFuture9.exceptionally(Function.identity())).thenCompose(arg_0 -> Amazon.async$mixedFlow(this, completableFuture9, string2, null, null, null, 4, arg_0));
            }
            string = (String)completableFuture8.join();
        }
        object2 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{CSRF_PAT});
        object = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{TOTAL_PAT});
        String string3 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{CURRENCY_PAT});
        String string4 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{PURCHASE_ID});
        String string5 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{PURCHASE_CUST_ID});
        String string6 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{CTB});
        String string7 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{SCOPE});
        String string8 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{INVARIANT});
        String string9 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{PROMISE_TIME});
        String string10 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{PROMISE_ASIN});
        String string11 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{SELECTED_PAYMENT});
        String string12 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{FAST_TRACK_EXP});
        String string13 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{COUNTDOWN_THRESH});
        String string14 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{COUNTDOWN_ID});
        String string15 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{SIMPL_COUNTDOWN});
        String string16 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{DUP_ORDER_CHECK});
        String string17 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{ORDER_ZERO});
        String string18 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{SELECTED_SHIP_SPD});
        String string19 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{GUARENTEE_1});
        String string20 = "1";
        String string21 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{SHIP_SPLIT_PRIORITY_1});
        String string22 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{IS_SHIP_COMPL_VAL});
        String string23 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{IS_SHIP_WHNEVR_VAL});
        String string24 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{SHIP_OFFER_2});
        String string25 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{GUARENTEE_2});
        String string26 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{ISSS_2});
        String string27 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{SHIP_SPLIT_PRIORITY_2});
        String string28 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{IS_SHIP_COMPL_VAL_2});
        String string29 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{IS_SHIP_WHNEVR_VAL_2});
        String string30 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{PREV_SHIP_OFF_ID});
        String string31 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{PREV_GUARENTEE_TYPE});
        String string32 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{PREV_ISSS});
        String string33 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{PREV_SHIP_PRIORITY});
        String string34 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{LINE_ITEM_IDS});
        String string35 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{CURR_SHIP_SPD});
        String string36 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{PREV_SHIP_SPD});
        String string37 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{CURR_SHIP_SPLIT_PREF});
        String string38 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{SHIP_PRIORITY_0_WHEN_CMPL});
        String string39 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{GROUP_CNT});
        String string40 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{SNS_UPSELL_CNT});
        String string41 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{OM_UPSELL_CNT});
        String string42 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{VAS_MODEL});
        String string43 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{SHIP_TRIAL_PFX});
        String string44 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{FIRST_TIMER});
        MultiMap multiMap = this.api.normalPlaceOrderForm((String)object2, (String)object, string3, string4, string5, string6, string7, string8, string9, string10, string11, string12, string13, string14, string15, string16, string17, string18, string19, "1", string21, string22, string23, string24, string25, string26, string27, string28, string29, string30, string31, string32, string33, string34, string35, string36, string37, string38, string39, string40, string41, string42, string43, string44);
        return this.placeOrderNormal(multiMap);
    }

    public CompletableFuture buynow(String string) {
        String string2 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{ANTI_CSRF_PAT});
        String string3 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{WEBLAB_PAT});
        String string4 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{PID_PAT});
        String string5 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{RID_PAT});
        return this.placeOrderBuynow(string2, string5, string4, string3);
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    public static CompletableFuture async$sessionLogon(Amazon var0, Account var1_1, String var2_2, JsonArray var3_3, CompletableFuture var4_4, String var5_7, int var6_11, int var7_13, int var8_16, Object var9_17) {
        switch (var8_16) {
            case 0: {
                var2_2 = var1_1.lookupSession();
                if (var2_2.isBlank() != false) return CompletableFuture.completedFuture(false);
                var3_3 = new JsonArray(var2_2);
                if (var3_3.isEmpty() != false) return CompletableFuture.completedFuture(false);
                for (var4_5 = 0; var4_5 < var3_3.size(); ++var4_5) {
                    try {
                        var5_7 = var3_3.getString(var4_5);
                        var6_12 = ClientCookieDecoder.STRICT.decode(var5_7);
                        if (var6_12 == null) continue;
                        var0.api.getCookies().put(var6_12);
                        continue;
                    }
                    catch (Throwable var5_8) {
                        var0.logger.warn("Error parsing session state: {}", (Object)var5_8.getMessage());
                    }
                }
                try {
                    v0 = var0.GETREQ("Validating Session", var0.api.walletPage(), 200, new String[]{""});
                    if (!v0.isDone()) {
                        var7_14 = v0;
                        return var7_14.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$sessionLogon(io.trickle.task.sites.amazon.Amazon io.trickle.account.Account java.lang.String io.vertx.core.json.JsonArray java.util.concurrent.CompletableFuture java.lang.String int int int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Amazon)var0, (Account)var1_1, (String)var2_2, (JsonArray)var3_3, (CompletableFuture)var7_14, null, (int)0, (int)0, (int)1));
                    }
lbl23:
                    // 3 sources

                    while (true) {
                        var4_4 = (String)v0.join();
                        var0.api.cartAPI.addressID = Utils.quickParseFirst((String)var4_4, (Pattern[])new Pattern[]{Patterns.ADDRESS_ID});
                        var5_9 = var4_4.contains("card ending in");
                        v1 = var6_11 = var0.api.cartAPI.addressID != null ? 1 : 0;
                        if (var6_11 != 0 && var5_9 != 0) {
                            return CompletableFuture.completedFuture(true);
                        }
                        if (var5_9 == 0) {
                            var0.logger.error("Please pre-fil payment on this account [{}]", (Object)var1_1.getUser());
                        }
                        if (var6_11 == 0) {
                            var0.logger.error("Please pre-fil shipping on this account [{}]", (Object)var1_1.getUser());
                        }
                        if (!(v2 = VertxUtil.hardCodedSleep((long)999999999L)).isDone()) {
                            var7_15 = v2;
                            return var7_15.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$sessionLogon(io.trickle.task.sites.amazon.Amazon io.trickle.account.Account java.lang.String io.vertx.core.json.JsonArray java.util.concurrent.CompletableFuture java.lang.String int int int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Amazon)var0, (Account)var1_1, (String)var2_2, (JsonArray)var3_3, (CompletableFuture)var7_15, (String)var4_4, (int)var5_9, (int)var6_11, (int)2));
                        }
lbl37:
                        // 3 sources

                        while (true) {
                            v2.join();
                            break;
                        }
                        break;
                    }
                }
                catch (Throwable var4_6) {
                    var0.logger.warn("Error validating session: {}", (Object)var4_6.getMessage());
                }
                return CompletableFuture.completedFuture(false);
            }
            case 1: {
                v0 = var4_4;
                ** continue;
            }
            case 2: {
                v2 = var4_4;
                v3 = var6_11;
                var6_11 = var7_13;
                var5_10 = v3;
                var4_4 = var5_7;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$genericExecute(Amazon var0, String var1_1, Supplier var2_2, Integer var3_3, int var4_4, Pair var5_5, CompletableFuture var6_7, HttpResponse var7_8, String var8_9, Throwable var9_10, int var10_11, Object var11_12) {
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
    public static CompletableFuture async$smoothLogin(Amazon var0, AccountController var1_1, CompletableFuture var2_2, Account var3_3, int var4_4, Object var5_10) {
        switch (var4_4) {
            case 0: {
                var1_1 = (AccountController)Engine.get().getModule(Controller.ACCOUNT);
                v0 = var1_1.findAccount(var0.task.getProfile().getEmail(), true).toCompletionStage().toCompletableFuture();
                if (!v0.isDone()) {
                    var4_5 = v0;
                    return var4_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$smoothLogin(io.trickle.task.sites.amazon.Amazon io.trickle.account.AccountController java.util.concurrent.CompletableFuture io.trickle.account.Account int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Amazon)var0, (AccountController)var1_1, (CompletableFuture)var4_5, null, (int)1));
                }
                ** GOTO lbl11
            }
            case 1: {
                v0 = var2_2;
lbl11:
                // 2 sources

                if ((var2_2 = (Account)v0.join()) != null) ** GOTO lbl18
                var0.logger.warn("No accounts available. Sleeping forever...");
                v1 = VertxUtil.hardCodedSleep((long)0x7FFFFFFFFFFFFFFFL);
                if (!v1.isDone()) {
                    var4_6 = v1;
                    return var4_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$smoothLogin(io.trickle.task.sites.amazon.Amazon io.trickle.account.AccountController java.util.concurrent.CompletableFuture io.trickle.account.Account int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Amazon)var0, (AccountController)var1_1, (CompletableFuture)var4_6, (Account)var2_2, (int)2));
                }
                ** GOTO lbl28
lbl18:
                // 1 sources

                var0.logger.info("Logging in to account '{}'", (Object)var2_2.getUser());
                var2_2.setSite(String.valueOf(Site.AMAZON));
                v2 = var0.sessionLogon((Account)var2_2);
                if (!v2.isDone()) {
                    var4_7 = v2;
                    return var4_7.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$smoothLogin(io.trickle.task.sites.amazon.Amazon io.trickle.account.AccountController java.util.concurrent.CompletableFuture io.trickle.account.Account int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Amazon)var0, (AccountController)var1_1, (CompletableFuture)var4_7, (Account)var2_2, (int)3));
                }
                ** GOTO lbl34
            }
            case 2: {
                v1 = var2_2;
                var2_2 = var3_3;
lbl28:
                // 2 sources

                v1.join();
                return CompletableFuture.completedFuture(null);
            }
            case 3: {
                v2 = var2_2;
                var2_2 = var3_3;
lbl34:
                // 2 sources

                if (((Boolean)v2.join()).booleanValue()) {
                    var0.logger.info("Logged in successfully to account '{}'", (Object)var2_2.getUser());
                    return CompletableFuture.completedFuture(null);
                }
                var3_3 = null;
                v3 = var3_3;
                if (!v3.toCompletableFuture().isDone()) {
                    var4_8 = v3;
                    return var4_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$smoothLogin(io.trickle.task.sites.amazon.Amazon io.trickle.account.AccountController java.util.concurrent.CompletableFuture io.trickle.account.Account int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Amazon)var0, (AccountController)var1_1, null, (Account)var2_2, (int)4)).toCompletableFuture();
                }
                ** GOTO lbl47
            }
            case 4: {
                v3 = null;
                var2_2 = var3_3;
                var3_3 = null;
lbl47:
                // 2 sources

                v3.toCompletableFuture().join();
                var2_2.setSessionString(var0.api.getCookies().asJson().encode());
                var0.vertx.eventBus().send("accounts.writer.session", (Object)var2_2);
                v4 = VertxUtil.hardCodedSleep((long)0x7FFFFFFFFFFFFFFFL);
                if (!v4.isDone()) {
                    var4_9 = v4;
                    return var4_9.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$smoothLogin(io.trickle.task.sites.amazon.Amazon io.trickle.account.AccountController java.util.concurrent.CompletableFuture io.trickle.account.Account int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Amazon)var0, (AccountController)var1_1, (CompletableFuture)var4_9, (Account)var2_2, (int)5));
                }
                ** GOTO lbl61
            }
            case 5: {
                v4 = var2_2;
                var2_2 = var3_3;
                var3_3 = null;
lbl61:
                // 2 sources

                v4.join();
                return CompletableFuture.completedFuture(null);
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture sessionLogon(Account account) {
        String string = account.lookupSession();
        if (string.isBlank()) return CompletableFuture.completedFuture(false);
        JsonArray jsonArray = new JsonArray(string);
        if (jsonArray.isEmpty()) return CompletableFuture.completedFuture(false);
        for (int i = 0; i < jsonArray.size(); ++i) {
            try {
                String throwable = jsonArray.getString(i);
                Cookie n = ClientCookieDecoder.STRICT.decode(throwable);
                if (n == null) continue;
                this.api.getCookies().put(n);
                continue;
            }
            catch (Throwable n2) {
                this.logger.warn("Error parsing session state: {}", (Object)n2.getMessage());
            }
        }
        try {
            int n;
            CompletableFuture completableFuture = this.GETREQ("Validating Session", this.api.walletPage(), 200, new String[]{""});
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture4 = completableFuture;
                return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Amazon.async$sessionLogon(this, account, string, jsonArray, completableFuture4, null, 0, 0, 1, arg_0));
            }
            String string3 = (String)completableFuture.join();
            this.api.cartAPI.addressID = Utils.quickParseFirst((String)string3, (Pattern[])new Pattern[]{Patterns.ADDRESS_ID});
            int n2 = string3.contains("card ending in");
            int n3 = n = this.api.cartAPI.addressID != null ? 1 : 0;
            if (n != 0 && n2 != 0) {
                return CompletableFuture.completedFuture(true);
            }
            if (n2 == 0) {
                this.logger.error("Please pre-fil payment on this account [{}]", (Object)account.getUser());
            }
            if (n == 0) {
                this.logger.error("Please pre-fil shipping on this account [{}]", (Object)account.getUser());
            }
            CompletableFuture completableFuture2 = VertxUtil.hardCodedSleep((long)999999999L);
            if (!completableFuture2.isDone()) {
                CompletableFuture completableFuture3 = completableFuture2;
                return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> Amazon.async$sessionLogon(this, account, string, jsonArray, completableFuture3, string3, n2, n, 2, arg_0));
            }
            completableFuture2.join();
        }
        catch (Throwable throwable) {
            this.logger.warn("Error validating session: {}", (Object)throwable.getMessage());
        }
        return CompletableFuture.completedFuture(false);
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$noCartJack(Amazon var0, String var1_1, int var2_2, CompletableFuture var3_3, String var4_4, int var5_5, Object var6_6) {
        switch (var5_5) {
            case 0: {
                var2_2 = 0;
lbl4:
                // 2 sources

                while (true) {
                    if (var2_2 >= 250) return CompletableFuture.completedFuture(false);
                    v0 = var0.GETREQ("Placing order [exp]", var0.api.placeOrderExp(), null, new String[]{""});
                    if (!v0.isDone()) {
                        var4_4 = v0;
                        return var4_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$noCartJack(io.trickle.task.sites.amazon.Amazon java.lang.String int java.util.concurrent.CompletableFuture java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Amazon)var0, (String)var1_1, (int)var2_2, (CompletableFuture)var4_4, null, (int)1));
                    }
                    ** GOTO lbl13
                    break;
                }
            }
            case 1: {
                v0 = var3_3;
lbl13:
                // 2 sources

                if ((var3_3 = (String)v0.join()).contains("a-color-success a-text-bold")) {
                    var0.logger.info("Successfully checked out!");
                    Analytics.success((Task)var0.task, (JsonObject)var0.itemJson, (String)var0.api.proxyStringSafe());
                    return CompletableFuture.completedFuture(true);
                }
                var0.logger.error("Checkout failed [exp]. Retrying...");
                if (var2_2 <= 5) ** GOTO lbl29
                v1 = VertxUtil.randomSleep((long)var0.task.getRetryDelay());
                if (!v1.isDone()) {
                    var4_4 = v1;
                    return var4_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$noCartJack(io.trickle.task.sites.amazon.Amazon java.lang.String int java.util.concurrent.CompletableFuture java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Amazon)var0, (String)var1_1, (int)var2_2, (CompletableFuture)var4_4, (String)var3_3, (int)2));
                }
                ** GOTO lbl27
            }
            case 2: {
                v1 = var3_3;
                var3_3 = var4_4;
lbl27:
                // 2 sources

                v1.join();
lbl29:
                // 2 sources

                ++var2_2;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$run(Amazon var0, CompletableFuture var1_1, String var2_3, String var3_4, int var4_5, Object var5_6) {
        switch (var4_5) {
            case 0: {
                v0 = CompletableFuture.allOf(Amazon.harvesterFutures);
                if (!v0.isDone()) {
                    var3_4 = v0;
                    return var3_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.amazon.Amazon java.util.concurrent.CompletableFuture java.lang.String java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Amazon)var0, (CompletableFuture)var3_4, null, null, (int)1));
                }
lbl7:
                // 3 sources

                while (true) {
                    v0.join();
                    try {
                        v1 = var0.smoothLogin();
                        if (!v1.isDone()) {
                            var3_4 = v1;
                            return var3_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.amazon.Amazon java.util.concurrent.CompletableFuture java.lang.String java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Amazon)var0, (CompletableFuture)var3_4, null, null, (int)2));
                        }
lbl15:
                        // 3 sources

                        while (true) {
                            v1.join();
                            v2 = var0.GETREQ("Init api", var0.api.corsairPage(), 200, new String[]{"amazonApiCsrfToken"});
                            if (!v2.isDone()) {
                                var3_4 = v2;
                                return var3_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.amazon.Amazon java.util.concurrent.CompletableFuture java.lang.String java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Amazon)var0, (CompletableFuture)var3_4, null, null, (int)3));
                            }
lbl22:
                            // 3 sources

                            while (true) {
                                var1_1 = (String)v2.join();
                                var0.api.cartAPI.apiCsrf = Utils.quickParseFirst((String)var1_1, (Pattern[])new Pattern[]{Patterns.API_CSRF});
                                v3 = var0.GETREQ("Checking page...", var0.api.productPage(), 200, new String[]{""});
                                if (!v3.isDone()) {
                                    var3_4 = v3;
                                    return var3_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.amazon.Amazon java.util.concurrent.CompletableFuture java.lang.String java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Amazon)var0, (CompletableFuture)var3_4, (String)var1_1, null, (int)4));
                                }
lbl29:
                                // 3 sources

                                while (true) {
                                    var2_3 = (String)v3.join();
                                    var0.itemJson = new JsonObject().put("title", (Object)Utils.quickParseFirst((String)var2_3, (Pattern[])new Pattern[]{Patterns.TITLE}));
                                    v4 = var0.mixedFlow();
                                    if (!v4.isDone()) {
                                        var3_4 = v4;
                                        return var3_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.amazon.Amazon java.util.concurrent.CompletableFuture java.lang.String java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Amazon)var0, (CompletableFuture)var3_4, (String)var1_1, (String)var2_3, (int)5));
                                    }
lbl36:
                                    // 3 sources

                                    while (true) {
                                        v4.join();
                                        break;
                                    }
                                    break;
                                }
                                break;
                            }
                            break;
                        }
                    }
                    catch (Exception var1_2) {
                        var1_2.printStackTrace();
                        var0.logger.error("Task interrupted: " + var1_2.getMessage());
                    }
                    return CompletableFuture.completedFuture(null);
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
                ** continue;
            }
            case 4: {
                v3 = var1_1;
                var1_1 = var2_3;
                ** continue;
            }
            case 5: {
                v4 = var1_1;
                v5 = var2_3;
                var2_3 = var3_4;
                var1_1 = v5;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture placeOrderBuynow(String string, String string2, String string3, String string4) {
        this.logger.info("Processing [buynow]");
        int n = 0;
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            try {
                CompletableFuture completableFuture = Request.send((HttpRequest)this.api.buynowPlaceOrder(string, string2, string3), (MultiMap)this.api.buynowPlaceOrderForm(string3, string4));
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Amazon.async$placeOrderBuynow(this, string, string2, string3, string4, n, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse != null) {
                    String string5;
                    if (httpResponse.statusCode() == 200 && (string5 = httpResponse.getHeader("location")) != null && string5.contains("thankyou")) {
                        this.logger.info("Successfully checked out!");
                        Analytics.success((Task)this.task, (JsonObject)this.itemJson, (String)this.api.proxyStringSafe());
                        return CompletableFuture.completedFuture(null);
                    }
                    if (n < 2) continue;
                    this.logger.warn("Failed processing payment: '{}'", (Object)(httpResponse.statusCode() + httpResponse.statusMessage()));
                }
                CompletableFuture completableFuture3 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Amazon.async$placeOrderBuynow(this, string, string2, string3, string4, n, completableFuture4, httpResponse, null, 2, arg_0));
                }
                completableFuture3.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error processing payment : {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = VertxUtil.randomSleep((long)this.task.getRetryDelay());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture5 = completableFuture;
                    return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> Amazon.async$placeOrderBuynow(this, string, string2, string3, string4, n, completableFuture5, null, throwable, 3, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public Amazon(Task task, int n) {
        super(n);
        this.task = task;
        this.api = new AmazonAPI(this.task);
        super.setClient((TaskApiClient)this.api);
    }

    static {
        harvesterFutures = new CompletableFuture[3];
        FIRST_TIMER = Pattern.compile("isfirsttimecustomer\" value=\"(.*?)\"");
        SHIP_TRIAL_PFX = Pattern.compile("shiptrialprefix\" value=\"(.*?)\"");
        VAS_MODEL = Pattern.compile("vasClaimBasedModel\" value=\"(.*?)\"");
        OM_UPSELL_CNT = Pattern.compile("onmlUpsellSuppressedCount\" value=\"(.*?)\"");
        SNS_UPSELL_CNT = Pattern.compile("snsUpsellTotalCount\" value=\"(.*?)\"");
        GROUP_CNT = Pattern.compile("groupcount\" value=\"(.*?)\"");
        SHIP_PRIORITY_0_WHEN_CMPL = Pattern.compile("shippriority.0.shipWhenComplete\" value=\"(.*?)\"");
        CURR_SHIP_SPLIT_PREF = Pattern.compile("currentshipsplitpreference\" value=\"(.*?)\"");
        PREV_SHIP_SPD = Pattern.compile("previousShippingSpeed0\" value=\"(.*?)\"");
        CURR_SHIP_SPD = Pattern.compile("currentshippingspeed\" value=\"(.*?)\"");
        LINE_ITEM_IDS = Pattern.compile("lineitemids0\" value=\"(.*?)\"");
        PREV_SHIP_PRIORITY = Pattern.compile("previousshippriority0\" value=\"(.*?)\"");
        PREV_ISSS = Pattern.compile("previousissss0\" value=\"(.*?)\"");
        PREV_GUARENTEE_TYPE = Pattern.compile("previousguaranteetype0\" value=\"(.*?)\"");
        PREV_SHIP_OFF_ID = Pattern.compile("previousshippingofferingid0\" value=\"(.*?)\"");
        IS_SHIP_WHNEVR_VAL_2 = Pattern.compile("isShipWheneverValid0.2\" value=\"(.*?)\"");
        IS_SHIP_COMPL_VAL_2 = Pattern.compile("isShipWhenCompleteValid0.2\" value=\"(.*?)\"");
        SHIP_SPLIT_PRIORITY_2 = Pattern.compile("shipsplitpriority0.2\" value=\"(.*?)\"");
        ISSS_2 = Pattern.compile("issss0.2\" value=\"(.*?)\"");
        GUARENTEE_2 = Pattern.compile("guaranteetype0.2\" value=\"(.*?)\"");
        SHIP_OFFER_2 = Pattern.compile("shippingofferingid0.2\" value=\"(.*?)\"");
        IS_SHIP_WHNEVR_VAL = Pattern.compile("isShipWheneverValid0.1\" value=\"(.*?)\"");
        IS_SHIP_COMPL_VAL = Pattern.compile("isShipWhenCompleteValid0.1\" value=\"(.*?)\"");
        SHIP_SPLIT_PRIORITY_1 = Pattern.compile("shipsplitpriority0.1\" value=\"(.*?)\"");
        GUARENTEE_1 = Pattern.compile("data-issss=\"1\" data-guaranteetype=\"(.*?)\"");
        SELECTED_SHIP_SPD = Pattern.compile("selectedshipspeed0.1.0\" data-shippingofferingid=\"(.*?)\"");
        ORDER_ZERO = Pattern.compile("order0\" value=\"(.*?)\"");
        DUP_ORDER_CHECK = Pattern.compile("dupOrderCheckArgs\" value=\"(.*?)\"");
        SIMPL_COUNTDOWN = Pattern.compile("showSimplifiedCountdown\" value=\"(.*?)\"");
        COUNTDOWN_ID = Pattern.compile("countdownId\" value=\"(.*?)\"");
        COUNTDOWN_THRESH = Pattern.compile("countdownThreshold\" value=\"(.*?)\"");
        FAST_TRACK_EXP = Pattern.compile("fasttrackExpiration\" value=\"(.*?)\"");
        SELECTED_PAYMENT = Pattern.compile("selectedPaymentPaystationId\" value=\"(.*?)\"");
        PROMISE_ASIN = Pattern.compile("promiseAsin-0\" value=\"(.*?)\"");
        PROMISE_TIME = Pattern.compile("promiseTime-0\" value=\"(.*?)\"");
        INVARIANT = Pattern.compile("isQuantityInvariant\" value=\"(.*?)\"");
        SCOPE = Pattern.compile("scopeId\" value=\"(.*?)\"");
        CTB = Pattern.compile("useCtb\" value=\"(.*?)\"");
        PURCHASE_CUST_ID = Pattern.compile("purchaseCustomerId\" value=\"(.*?)\"");
        PURCHASE_ID = Pattern.compile("purchaseID\" value=\"(.*?)\"");
        CURRENCY_PAT = Pattern.compile("\"purchaseTotalCurrency\" value=\"(.*?)\"");
        TOTAL_PAT = Pattern.compile("purchaseTotal\" value=\"(.*?)\"");
        CSRF_PAT = Pattern.compile("\"csrfToken\" value=\"(.*?)\"");
        ANTI_CSRF_PAT = Pattern.compile("anti-csrftoken-a2z' value='(.*?)'");
        ANTI_CSRF_PAT_P_PAGE = Pattern.compile("anti-csrftoken-a2z\" value=\"(.*?)\"", 32);
        WEBLAB_PAT = Pattern.compile("weblab=(.*?)\"");
        PID_PAT = Pattern.compile("pid=([0-9].*?)&");
        RID_PAT = Pattern.compile("request-id=\"(.*?)\"");
        successfulCartIndicators = new String[]{"item in cart", "Proceed to checkout (", "\"ok\":true,\"numActiveItemsInCart\":\"", "\"nav-cart\":{\"cartQty\":1}", "\"entity\":{\"items\":1}", "/checkout/ordersummary?ref_=", "\"cartQuantity\":\"1\""};
        successfulRedirectIndicators = new String[]{"return_to=https%3A%2F%2Fwww.amazon.com%2Fgp%2Fcheckoutportal%2Fenter-checkout.html", "/gp/cart/view.html"};
        int n = 0;
        while (n < harvesterFutures.length) {
            Amazon.harvesterFutures[n] = new LoginHarvester().start();
            ++n;
        }
    }

    public CompletableFuture placeOrderNormal(MultiMap multiMap) {
        this.logger.info("Processing [normal]");
        int n = 0;
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            try {
                CompletableFuture completableFuture = Request.send((HttpRequest)this.api.normalPlaceOrder(), (MultiMap)multiMap);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Amazon.async$placeOrderNormal(this, multiMap, n, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse != null) {
                    if (httpResponse.bodyAsString().contains("<meta http-equiv=\"refresh\" content=\"0; url=/gp/buy/duplicate-order/handlers/display.html")) {
                        this.logger.info("Duplicate order");
                        Analytics.failure((String)"Duplicate order", (Task)this.task, (String)httpResponse.bodyAsString(), (String)this.api.proxyStringSafe());
                    } else {
                        String string;
                        if (httpResponse.statusCode() == 200 && (string = httpResponse.getHeader("x-amz-checkout-sub-page-type")) != null && string.contains("place-your-decoupled-order")) {
                            this.logger.info("Successfully checked out!");
                            Analytics.success((Task)this.task, (JsonObject)this.itemJson, (String)this.api.proxyStringSafe());
                            return CompletableFuture.completedFuture(null);
                        }
                        if (n < 2) continue;
                        this.logger.warn("Failed processing payment: '{}'", (Object)(httpResponse.statusCode() + httpResponse.statusMessage()));
                    }
                }
                CompletableFuture completableFuture3 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Amazon.async$placeOrderNormal(this, multiMap, n, completableFuture4, httpResponse, null, 2, arg_0));
                }
                completableFuture3.join();
            }
            catch (Throwable throwable) {
                throwable.printStackTrace();
                this.logger.error("Error processing payment : {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = VertxUtil.randomSleep((long)this.task.getRetryDelay());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture5 = completableFuture;
                    return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> Amazon.async$placeOrderNormal(this, multiMap, n, completableFuture5, null, throwable, 3, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture genericExecute(String string, Supplier supplier, Integer n) {
        int n2 = 0;
        while (n2++ < Integer.MAX_VALUE) {
            try {
                Pair pair = (Pair)supplier.get();
                CompletableFuture completableFuture = Request.send((HttpRequest)((HttpRequest)pair.first), (Object)pair.second);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Amazon.async$genericExecute(this, string, (Supplier)supplier, n, n2, pair, completableFuture2, null, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse != null) {
                    String string2 = httpResponse.bodyAsString();
                    if (httpResponse.statusCode() == n.intValue()) {
                        return CompletableFuture.completedFuture(string2);
                    }
                    this.logger.warn("Failed {}: '{}'", (Object)string.toLowerCase(Locale.ROOT), (Object)(httpResponse.statusCode() + httpResponse.statusMessage()));
                    CompletableFuture completableFuture3 = VertxUtil.randomSleep((long)this.task.getMonitorDelay());
                    if (!completableFuture3.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture3;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Amazon.async$genericExecute(this, string, (Supplier)supplier, n, n2, pair, completableFuture4, httpResponse, string2, null, 2, arg_0));
                    }
                    completableFuture3.join();
                    continue;
                }
                CompletableFuture completableFuture5 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Amazon.async$genericExecute(this, string, (Supplier)supplier, n, n2, pair, completableFuture6, httpResponse, null, null, 3, arg_0));
                }
                completableFuture5.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error {}: {}", (Object)string.toLowerCase(Locale.ROOT), (Object)throwable.getMessage());
                if (this.logger.isDebugEnabled()) {
                    throwable.printStackTrace();
                }
                CompletableFuture completableFuture = VertxUtil.randomSleep((long)this.task.getRetryDelay());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> Amazon.async$genericExecute(this, string, (Supplier)supplier, n, n2, null, completableFuture7, null, null, throwable, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture cartHtml() {
        CompletableFuture completableFuture = this.GETREQ("Checking product", this.api.cartPage(), 200, new String[]{"CSRF"});
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Amazon.async$cartHtml(this, completableFuture2, null, null, null, null, null, null, 0, null, 1, arg_0));
        }
        String string = (String)completableFuture.join();
        CompletableFuture completableFuture3 = this.GETREQ("Checking coupons", this.api.promoPage(), 200, new String[]{"anti-csrf"});
        if (!completableFuture3.isDone()) {
            CompletableFuture completableFuture4 = completableFuture3;
            return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Amazon.async$cartHtml(this, completableFuture4, string, null, null, null, null, null, 0, null, 2, arg_0));
        }
        String string2 = (String)completableFuture3.join();
        this.api.updateCsrf(Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{Patterns.CSRF}));
        this.api.genCsm(Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{Patterns.CSM_PREFIX}));
        this.api.updateGlow(Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{Patterns.GLOW}));
        this.api.cartAPI.anti_csrf = Utils.quickParseFirstNonEmpty((String)string2, (Pattern)ANTI_CSRF_PAT_P_PAGE);
        String string3 = "Adding to cart [M]";
        this.logger.info("Adding to cart [M]");
        while (this.running) {
            try {
                int n;
                Triplet triplet = (Triplet)this.api.cartAPI.smartEndpoint().get();
                CompletableFuture completableFuture5 = Request.send((HttpRequest)((HttpRequest)triplet.first), (Object)triplet.second);
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Amazon.async$cartHtml(this, completableFuture6, string, string2, string3, triplet, null, null, 0, null, 3, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture5.join();
                if (httpResponse == null) continue;
                String string4 = httpResponse.bodyAsString();
                int n2 = n = Utils.containsAnyWords((String)string4, (String[])successfulCartIndicators) || httpResponse.statusCode() == 302 && Utils.containsAnyWords((String)httpResponse.getHeader("location"), (String[])successfulRedirectIndicators) ? 1 : 0;
                if (n != 0) {
                    this.isTurbo = (Boolean)triplet.third;
                    return CompletableFuture.completedFuture(string4);
                }
                if (Utils.containsAllWords((String)string4, (String[])this.successfulFinalPageIndicators)) {
                    this.isTurbo = false;
                    return CompletableFuture.completedFuture(string4);
                }
                this.logger.warn("Failed {}: '{}'", (Object)string3.toLowerCase(Locale.ROOT), (Object)(httpResponse.statusCode() + httpResponse.statusMessage()));
                CompletableFuture completableFuture7 = VertxUtil.randomSleep((long)this.task.getMonitorDelay());
                if (!completableFuture7.isDone()) {
                    CompletableFuture completableFuture8 = completableFuture7;
                    return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Amazon.async$cartHtml(this, completableFuture8, string, string2, string3, triplet, httpResponse, string4, n, null, 4, arg_0));
                }
                completableFuture7.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error {}: {}", (Object)string3.toLowerCase(Locale.ROOT), (Object)throwable.getMessage());
                if (this.logger.isDebugEnabled()) {
                    throwable.printStackTrace();
                }
                CompletableFuture completableFuture9 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
                if (!completableFuture9.isDone()) {
                    CompletableFuture completableFuture10 = completableFuture9;
                    return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> Amazon.async$cartHtml(this, completableFuture10, string, string2, string3, null, null, null, 0, throwable, 5, arg_0));
                }
                completableFuture9.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }
}
