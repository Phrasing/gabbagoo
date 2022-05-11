/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.teamdev.jxbrowser.net.UploadData
 *  io.trickle.basicgui.LinkOverrideListener
 *  io.trickle.harvester.HybridHarvester
 *  io.trickle.task.Task
 *  io.trickle.task.sites.shopify.Presolver
 *  io.trickle.task.sites.shopify.Shopify
 *  io.trickle.util.Pair
 *  io.trickle.util.Utils
 *  io.trickle.util.analytics.Analytics
 *  io.trickle.util.concurrent.ContextCompletableFuture
 *  io.trickle.util.concurrent.VertxUtil
 *  io.trickle.util.request.Request
 *  io.vertx.core.buffer.Buffer
 *  io.vertx.core.eventbus.Message
 *  io.vertx.core.eventbus.MessageConsumer
 *  io.vertx.core.http.RequestOptions
 *  io.vertx.core.json.JsonObject
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.client.HttpResponse
 */
package io.trickle.task.sites.shopify;

import com.teamdev.jxbrowser.net.UploadData;
import io.trickle.basicgui.LinkOverrideListener;
import io.trickle.harvester.HybridHarvester;
import io.trickle.task.Task;
import io.trickle.task.sites.shopify.Presolver;
import io.trickle.task.sites.shopify.Shopify;
import io.trickle.util.Pair;
import io.trickle.util.Utils;
import io.trickle.util.analytics.Analytics;
import io.trickle.util.concurrent.ContextCompletableFuture;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.http.RequestOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import java.lang.invoke.LambdaMetafactory;
import java.time.Instant;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class ShopifyHybrid
extends Shopify {
    public int id;
    public MessageConsumer<Pair<RequestOptions, Optional<UploadData>>> cartDataConsumer;
    public MessageConsumer<String> linkChangeConsumer;

    public CompletableFuture waitForLink() {
        ContextCompletableFuture contextCompletableFuture = new ContextCompletableFuture();
        this.linkChangeConsumer = this.vertx.eventBus().localConsumer(LinkOverrideListener.MASS_LINK_CHANGE_ADDRESS, arg_0 -> ShopifyHybrid.lambda$waitForLink$0(contextCompletableFuture, arg_0));
        return contextCompletableFuture;
    }

    public CompletableFuture send(RequestOptions requestOptions, Optional optional) {
        this.logger.info("Adding to cart");
        int n = 0;
        while (n++ < Integer.MAX_VALUE) {
            String string;
            try {
                HttpResponse httpResponse;
                HttpResponse httpResponse2;
                RequestOptions requestOptions2 = requestOptions;
                string = this.api.getWebClient().request(requestOptions2.getMethod(), requestOptions2);
                if (optional.isEmpty()) {
                    CompletableFuture completableFuture = Request.send((HttpRequest)string);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture2 = completableFuture;
                        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyHybrid.async$send(this, requestOptions, optional, n, requestOptions2, (HttpRequest)string, completableFuture2, 1, arg_0));
                    }
                    httpResponse2 = (HttpResponse)completableFuture.join();
                } else {
                    CompletableFuture completableFuture = Request.send((HttpRequest)string, (Buffer)Buffer.buffer((byte[])((UploadData)optional.get()).bytes()));
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture3 = completableFuture;
                        return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyHybrid.async$send(this, requestOptions, optional, n, requestOptions2, (HttpRequest)string, completableFuture3, 2, arg_0));
                    }
                    httpResponse2 = httpResponse = (HttpResponse)completableFuture.join();
                }
                if (httpResponse != null && this.api.getCookies().contains("cart")) {
                    return CompletableFuture.completedFuture(httpResponse);
                }
                if (httpResponse != null) {
                    this.logger.error("Bad atc response {}", (Object)httpResponse.statusCode());
                } else {
                    this.logger.error("No response {}", (Object)requestOptions2.getURI());
                }
            }
            catch (Throwable throwable) {
                string = throwable.getMessage();
                if (string.contains("timeout period")) {
                    this.logger.error("request timeout. no response");
                }
                if (string.contains("proxy")) {
                    this.logger.error("unable to connect to proxy - {}", (Object)string);
                }
                this.logger.error("network error " + throwable.getMessage());
            }
            CompletableFuture completableFuture = VertxUtil.randomSleep((long)1000L);
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture4 = completableFuture;
                return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyHybrid.async$send(this, requestOptions, optional, n, null, null, completableFuture4, 3, arg_0));
            }
            completableFuture.join();
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture waitForCartData() {
        ContextCompletableFuture contextCompletableFuture = new ContextCompletableFuture();
        this.cartDataConsumer = this.vertx.eventBus().localConsumer(LinkOverrideListener.CART_ADDRESS, arg_0 -> ShopifyHybrid.lambda$waitForCartData$1(contextCompletableFuture, arg_0));
        return contextCompletableFuture;
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$run(ShopifyHybrid var0, int var1_1, CompletableFuture var2_2, CompletableFuture var3_3, HybridHarvester var4_5, CompletableFuture var5_6, String var6_7, String[] var7_8, int var8_9, Pair var9_26, int var10_28, Object var11_30) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [35[DOLOOP]], but top level block is 37[UNCONDITIONALDOLOOP]
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
        int n = 1;
        String string = null;
        try {
            JsonObject jsonObject;
            String string2;
            HybridHarvester hybridHarvester;
            CompletableFuture completableFuture = this.waitForLink();
            CompletableFuture completableFuture2 = this.waitForCartData();
            if (this.id == 1) {
                hybridHarvester = new HybridHarvester(this.vertx);
                CompletableFuture completableFuture3 = hybridHarvester.start();
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyHybrid.async$run(this, n, completableFuture, completableFuture2, hybridHarvester, completableFuture4, null, null, 0, null, 1, arg_0));
                }
                completableFuture3.join();
            }
            CompletableFuture completableFuture5 = ShopifyHybrid.initHarvesters();
            if (!completableFuture5.isDone()) {
                CompletableFuture completableFuture6 = completableFuture5;
                return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyHybrid.async$run(this, n, completableFuture, completableFuture2, null, completableFuture6, string, null, 0, null, 2, arg_0));
            }
            completableFuture5.join();
            String[] stringArray = this.task.getKeywords();
            CompletableFuture completableFuture7 = completableFuture;
            if (!completableFuture7.isDone()) {
                CompletableFuture completableFuture8 = completableFuture7;
                int n2 = 0;
                String[] stringArray2 = stringArray;
                return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyHybrid.async$run(this, n, completableFuture, completableFuture2, null, completableFuture8, string, stringArray2, n2, null, 3, arg_0));
            }
            stringArray[0] = (String)completableFuture7.join();
            this.task.getKeywords()[0] = this.task.getKeywords()[0].toLowerCase(Locale.ROOT);
            this.linkChangeConsumer.unregister();
            this.linkChangeConsumer = null;
            this.api.SITE_URL = this.task.getKeywords()[0].toLowerCase(Locale.ROOT).split("https://")[1].split("/")[0];
            CompletableFuture completableFuture9 = this.initShopDetails();
            if (!completableFuture9.isDone()) {
                CompletableFuture completableFuture10 = completableFuture9;
                return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyHybrid.async$run(this, n, completableFuture, completableFuture2, null, completableFuture10, string, null, 0, null, 4, arg_0));
            }
            completableFuture9.join();
            this.api.getCookies().put("_landing_page", "%2Fproducts%2F" + ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE), this.api.getSiteURL());
            this.api.getCookies().put("_orig_referrer", "", this.api.getSiteURL());
            this.api.getCookies().put("_shopify_sa_p", "", this.api.getSiteURL());
            this.api.getCookies().put("_shopify_country", "United+States", this.api.getSiteURL());
            this.api.getCookies().put("_shopify_sa_t", Utils.encodedDateISO((Instant)Instant.now()), this.api.getSiteURL());
            this.api.getCookies().put("_shopify_fs", Utils.encodedDateISO((Instant)instanceTime), this.api.getSiteURL());
            this.setProductPickupTime();
            this.genPaymentToken();
            this.presolver = new Presolver((Shopify)this);
            this.cpMonitor = this.presolver.run();
            if (this.task.getMode().contains("login")) {
                this.logger.info("Login required. Logging in...");
                CompletableFuture completableFuture11 = this.login();
                if (!completableFuture11.isDone()) {
                    CompletableFuture completableFuture12 = completableFuture11;
                    return ((CompletableFuture)completableFuture12.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyHybrid.async$run(this, n, completableFuture, completableFuture2, null, completableFuture12, string, null, 0, null, 5, arg_0));
                }
                completableFuture11.join();
            }
            CompletableFuture completableFuture13 = this.waitTilCartCookie();
            if (!completableFuture13.isDone()) {
                CompletableFuture completableFuture14 = completableFuture13;
                return ((CompletableFuture)completableFuture14.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyHybrid.async$run(this, n, completableFuture, completableFuture2, null, completableFuture14, string, null, 0, null, 6, arg_0));
            }
            completableFuture13.join();
            CompletableFuture completableFuture15 = this.walletsGenCheckout();
            if (!completableFuture15.isDone()) {
                CompletableFuture completableFuture16 = completableFuture15;
                return ((CompletableFuture)completableFuture16.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyHybrid.async$run(this, n, completableFuture, completableFuture2, null, completableFuture16, string, null, 0, null, 7, arg_0));
            }
            string = (String)completableFuture15.join();
            CompletableFuture completableFuture17 = this.handlePreload(string);
            if (!completableFuture17.isDone()) {
                CompletableFuture completableFuture18 = completableFuture17;
                return ((CompletableFuture)completableFuture18.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyHybrid.async$run(this, n, completableFuture, completableFuture2, null, completableFuture18, string, null, 0, null, 8, arg_0));
            }
            string = (String)completableFuture17.join();
            CompletableFuture completableFuture19 = completableFuture2;
            if (!completableFuture19.isDone()) {
                CompletableFuture completableFuture20 = completableFuture19;
                return ((CompletableFuture)completableFuture20.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyHybrid.async$run(this, n, completableFuture, completableFuture2, null, completableFuture20, string, null, 0, null, 9, arg_0));
            }
            hybridHarvester = (Pair)completableFuture19.join();
            this.cartDataConsumer.unregister();
            this.cartDataConsumer = null;
            CompletableFuture completableFuture21 = this.send((RequestOptions)hybridHarvester.first, (Optional)hybridHarvester.second);
            if (!completableFuture21.isDone()) {
                CompletableFuture completableFuture22 = completableFuture21;
                return ((CompletableFuture)completableFuture22.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyHybrid.async$run(this, n, completableFuture, completableFuture2, null, completableFuture22, string, null, 0, (Pair)hybridHarvester, 10, arg_0));
            }
            completableFuture21.join();
            CompletableFuture completableFuture23 = this.getCheckoutURL(string);
            if (!completableFuture23.isDone()) {
                CompletableFuture completableFuture24 = completableFuture23;
                return ((CompletableFuture)completableFuture24.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyHybrid.async$run(this, n, completableFuture, completableFuture2, null, completableFuture24, string, null, 0, (Pair)hybridHarvester, 11, arg_0));
            }
            completableFuture23.join();
            this.shutOffPresolver(false);
            CompletableFuture completableFuture25 = this.submitContact(string);
            if (!completableFuture25.isDone()) {
                CompletableFuture completableFuture26 = completableFuture25;
                return ((CompletableFuture)completableFuture26.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyHybrid.async$run(this, n, completableFuture, completableFuture2, null, completableFuture26, string, null, 0, (Pair)hybridHarvester, 12, arg_0));
            }
            completableFuture25.join();
            CompletableFuture completableFuture27 = this.walletsSubmitShipping(string);
            if (!completableFuture27.isDone()) {
                CompletableFuture completableFuture28 = completableFuture27;
                return ((CompletableFuture)completableFuture28.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyHybrid.async$run(this, n, completableFuture, completableFuture2, null, completableFuture28, string, null, 0, (Pair)hybridHarvester, 13, arg_0));
            }
            completableFuture27.join();
            CompletableFuture completableFuture29 = this.getProcessingPage(string, false);
            if (!completableFuture29.isDone()) {
                CompletableFuture completableFuture30 = completableFuture29;
                return ((CompletableFuture)completableFuture30.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyHybrid.async$run(this, n, completableFuture, completableFuture2, null, completableFuture30, string, null, 0, (Pair)hybridHarvester, 14, arg_0));
            }
            completableFuture29.join();
            do {
                CompletableFuture completableFuture31 = this.processPayment(string);
                if (!completableFuture31.isDone()) {
                    CompletableFuture completableFuture32 = completableFuture31;
                    return ((CompletableFuture)completableFuture32.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyHybrid.async$run(this, n, completableFuture, completableFuture2, null, completableFuture32, string, null, 0, (Pair)hybridHarvester, 15, arg_0));
                }
                completableFuture31.join();
                CompletableFuture completableFuture33 = this.checkOrderAPI(string);
                if (!completableFuture33.isDone()) {
                    CompletableFuture completableFuture34 = completableFuture33;
                    return ((CompletableFuture)completableFuture34.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyHybrid.async$run(this, n, completableFuture, completableFuture2, null, completableFuture34, string, null, 0, (Pair)hybridHarvester, 16, arg_0));
                }
                jsonObject = (JsonObject)completableFuture33.join();
                string2 = ShopifyHybrid.parseDecline((JsonObject)jsonObject);
                if (string2 == null || string2.equals("success")) continue;
                Analytics.failure((String)string2, (Task)this.task, (JsonObject)jsonObject, (String)this.api.proxyStringSafe());
                this.logger.info("Checkout fail -> " + string2);
            } while (string2 != null && !string2.equals("success"));
            this.logger.info("Successfully checked out.");
            Analytics.success((Task)this.task, (JsonObject)jsonObject, (String)this.api.proxyStringSafe());
        }
        catch (Throwable throwable) {
            this.logger.error("Task interrupted: " + throwable.getMessage());
            this.setAttributes();
            return this.run();
        }
        return null;
    }

    public ShopifyHybrid(Task task, int n) {
        super(task, n);
        this.id = n;
    }

    public static void lambda$waitForCartData$1(ContextCompletableFuture contextCompletableFuture, Message message) {
        if (contextCompletableFuture.isDone()) return;
        if (message.body() == null) return;
        contextCompletableFuture.complete((Object)((Pair)message.body()));
    }

    public static void lambda$waitForLink$0(ContextCompletableFuture contextCompletableFuture, Message message) {
        if (contextCompletableFuture.isDone()) return;
        if (message.body() == null) return;
        if (((String)message.body()).isBlank()) return;
        contextCompletableFuture.complete((Object)((String)message.body()));
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    public static CompletableFuture async$send(ShopifyHybrid var0, RequestOptions var1_1, Optional var2_2, int var3_3, RequestOptions var4_4, HttpRequest var5_6, CompletableFuture var6_7, int var7_8, Object var8_12) {
        switch (var7_8) {
            case 0: {
                var0.logger.info("Adding to cart");
                var3_3 = 0;
                block8: while (var3_3++ < 0x7FFFFFFF) {
                    try {
                        var4_4 = var1_1;
                        var5_6 = var0.api.getWebClient().request(var4_4.getMethod(), var4_4);
                        if (!var2_2.isEmpty()) ** GOTO lbl17
                        v0 = Request.send((HttpRequest)var5_6);
                        if (!v0.isDone()) {
                            var7_9 = v0;
                            return var7_9.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$send(io.trickle.task.sites.shopify.ShopifyHybrid io.vertx.core.http.RequestOptions java.util.Optional int io.vertx.core.http.RequestOptions io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifyHybrid)var0, (RequestOptions)var1_1, (Optional)var2_2, (int)var3_3, (RequestOptions)var4_4, (HttpRequest)var5_6, (CompletableFuture)var7_9, (int)1));
                        }
lbl14:
                        // 3 sources

                        while (true) {
                            v1 /* !! */  = (HttpResponse)v0.join();
                            ** GOTO lbl23
                            break;
                        }
lbl17:
                        // 1 sources

                        v2 = Request.send((HttpRequest)var5_6, (Buffer)Buffer.buffer((byte[])((UploadData)var2_2.get()).bytes()));
                        if (!v2.isDone()) {
                            var7_10 = v2;
                            return var7_10.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$send(io.trickle.task.sites.shopify.ShopifyHybrid io.vertx.core.http.RequestOptions java.util.Optional int io.vertx.core.http.RequestOptions io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifyHybrid)var0, (RequestOptions)var1_1, (Optional)var2_2, (int)var3_3, (RequestOptions)var4_4, (HttpRequest)var5_6, (CompletableFuture)var7_10, (int)2));
                        }
lbl21:
                        // 3 sources

                        while (true) {
                            v1 /* !! */  = var6_7 = (HttpResponse)v2.join();
lbl23:
                            // 2 sources

                            if (var6_7 != null && var0.api.getCookies().contains("cart")) {
                                return CompletableFuture.completedFuture(var6_7);
                            }
                            if (var6_7 != null) {
                                var0.logger.error("Bad atc response {}", (Object)var6_7.statusCode());
                            } else {
                                var0.logger.error("No response {}", (Object)var4_4.getURI());
                            }
                            break;
                        }
                    }
                    catch (Throwable var4_5) {
                        var5_6 = var4_5.getMessage();
                        if (var5_6.contains("timeout period")) {
                            var0.logger.error("request timeout. no response");
                        }
                        if (var5_6.contains("proxy")) {
                            var0.logger.error("unable to connect to proxy - {}", var5_6);
                        }
                        var0.logger.error("network error " + var4_5.getMessage());
                    }
                    if (!(v3 = VertxUtil.randomSleep((long)1000L)).isDone()) {
                        var7_11 = v3;
                        return var7_11.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$send(io.trickle.task.sites.shopify.ShopifyHybrid io.vertx.core.http.RequestOptions java.util.Optional int io.vertx.core.http.RequestOptions io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifyHybrid)var0, (RequestOptions)var1_1, (Optional)var2_2, (int)var3_3, null, null, (CompletableFuture)var7_11, (int)3));
                    }
lbl42:
                    // 3 sources

                    while (true) {
                        v3.join();
                        continue block8;
                        break;
                    }
                }
                return CompletableFuture.completedFuture(null);
            }
            case 1: {
                v0 = var6_7;
                ** continue;
            }
            case 2: {
                v2 = var6_7;
                ** continue;
            }
            case 3: {
                v3 = var6_7;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }
}
