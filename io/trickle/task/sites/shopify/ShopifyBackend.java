/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.task.Task
 *  io.trickle.task.sites.Site
 *  io.trickle.task.sites.shopify.Shopify
 *  io.trickle.task.sites.shopify.util.SiteParser
 *  io.trickle.task.sites.shopify.util.VariantHandler
 *  io.trickle.util.analytics.Analytics
 *  io.trickle.util.concurrent.VertxUtil
 *  io.vertx.core.json.JsonObject
 */
package io.trickle.task.sites.shopify;

import io.trickle.task.Task;
import io.trickle.task.sites.Site;
import io.trickle.task.sites.shopify.Shopify;
import io.trickle.task.sites.shopify.util.SiteParser;
import io.trickle.task.sites.shopify.util.VariantHandler;
import io.trickle.util.analytics.Analytics;
import io.trickle.util.concurrent.VertxUtil;
import io.vertx.core.json.JsonObject;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class ShopifyBackend
extends Shopify {
    public boolean isPassword = true;

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$run(ShopifyBackend var0, CompletableFuture var1_1, String var2_3, String var3_6, JsonObject var4_7, int var5_8, int var6_30, Object var7_31) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [47[DOLOOP]], but top level block is 49[UNCONDITIONALDOLOOP]
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
     * WARNING - void declaration
     */
    public CompletableFuture run() {
        CompletableFuture completableFuture = ShopifyBackend.initHarvesters();
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyBackend.async$run(this, completableFuture2, null, null, null, 0, 1, arg_0));
        }
        completableFuture.join();
        this.paymentGateway = SiteParser.getGatewayFromSite((Site)this.task.getSite(), (boolean)this.isCod);
        CompletableFuture completableFuture2 = this.initShopDetails();
        if (!completableFuture2.isDone()) {
            CompletableFuture completableFuture4 = completableFuture2;
            return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyBackend.async$run(this, completableFuture4, null, null, null, 0, 2, arg_0));
        }
        completableFuture2.join();
        if (this.task.getMode().contains("login")) {
            this.logger.info("Login required. Logging in...");
            CompletableFuture completableFuture3 = this.login();
            if (!completableFuture3.isDone()) {
                CompletableFuture completableFuture6 = completableFuture3;
                return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyBackend.async$run(this, completableFuture6, null, null, null, 0, 3, arg_0));
            }
            completableFuture3.join();
        }
        while (this.api.getWebClient().isActive()) {
            try {
                JsonObject jsonObject;
                Object object;
                String string;
                if (!this.isExtra) {
                    CompletableFuture completableFuture4 = this.genCheckoutURLIgnorePassword(true);
                    if (!completableFuture4.isDone()) {
                        CompletableFuture completableFuture8 = completableFuture4;
                        return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyBackend.async$run(this, completableFuture8, null, null, null, 0, 4, arg_0));
                    }
                    string = (String)completableFuture4.join();
                    if (string != null) {
                        this.isPassword = false;
                        CompletableFuture completableFuture5 = this.handlePreload(string);
                        if (!completableFuture5.isDone()) {
                            CompletableFuture completableFuture10 = completableFuture5;
                            return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyBackend.async$run(this, completableFuture10, string, null, null, 0, 5, arg_0));
                        }
                        string = (String)completableFuture5.join();
                        CompletableFuture completableFuture6 = this.shippingAndBillingPut(string);
                        if (!completableFuture6.isDone()) {
                            CompletableFuture completableFuture12 = completableFuture6;
                            return ((CompletableFuture)completableFuture12.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyBackend.async$run(this, completableFuture12, string, null, null, 0, 6, arg_0));
                        }
                        completableFuture6.join();
                    }
                } else {
                    this.isPassword = false;
                    CompletableFuture completableFuture7 = this.waitTilCartCookie();
                    if (!completableFuture7.isDone()) {
                        CompletableFuture completableFuture14 = completableFuture7;
                        return ((CompletableFuture)completableFuture14.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyBackend.async$run(this, completableFuture14, null, null, null, 0, 7, arg_0));
                    }
                    completableFuture7.join();
                    CompletableFuture completableFuture8 = this.walletsGenCheckout();
                    if (!completableFuture8.isDone()) {
                        CompletableFuture completableFuture16 = completableFuture8;
                        return ((CompletableFuture)completableFuture16.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyBackend.async$run(this, completableFuture16, null, null, null, 0, 8, arg_0));
                    }
                    string = (String)completableFuture8.join();
                    CompletableFuture completableFuture9 = this.handlePreload(string);
                    if (!completableFuture9.isDone()) {
                        CompletableFuture completableFuture18 = completableFuture9;
                        return ((CompletableFuture)completableFuture18.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyBackend.async$run(this, completableFuture18, string, null, null, 0, 9, arg_0));
                    }
                    string = (String)completableFuture9.join();
                    CompletableFuture completableFuture10 = this.shippingAndBillingPut(string);
                    if (!completableFuture10.isDone()) {
                        CompletableFuture completableFuture20 = completableFuture10;
                        return ((CompletableFuture)completableFuture20.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyBackend.async$run(this, completableFuture20, string, null, null, 0, 10, arg_0));
                    }
                    completableFuture10.join();
                }
                if (!this.isKeyword) {
                    if (this.isEL) {
                        CompletableFuture completableFuture11 = this.fetchELJS();
                        if (!completableFuture11.isDone()) {
                            CompletableFuture completableFuture22 = completableFuture11;
                            return ((CompletableFuture)completableFuture22.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyBackend.async$run(this, completableFuture22, string, null, null, 0, 13, arg_0));
                        }
                        JsonObject jsonObject2 = (JsonObject)completableFuture11.join();
                        this.instanceSignal = VariantHandler.selectVariantFromLink((JsonObject)jsonObject2, (String)this.task.getSize(), (Shopify)this);
                    }
                } else {
                    void n;
                    Object object2 = null;
                    while (n == null) {
                        CompletableFuture completableFuture12 = this.fetchProductsJSON(true);
                        if (!completableFuture12.isDone()) {
                            CompletableFuture completableFuture24 = completableFuture12;
                            return ((CompletableFuture)completableFuture24.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyBackend.async$run(this, completableFuture24, string, (String)n, null, 0, 11, arg_0));
                        }
                        object = (JsonObject)completableFuture12.join();
                        String jsonObject3 = VariantHandler.selectVariantFromKeyword((JsonObject)object, (String)this.task.getSize(), (List)this.positiveKeywords, (List)this.negativeKeywords, (Shopify)this);
                        if (jsonObject3 == null) {
                            CompletableFuture completableFuture13 = VertxUtil.signalSleep((String)this.instanceSignal, (long)this.task.getMonitorDelay());
                            if (!completableFuture13.isDone()) {
                                CompletableFuture completableFuture26 = completableFuture13;
                                return ((CompletableFuture)completableFuture26.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyBackend.async$run(this, completableFuture26, string, jsonObject3, (JsonObject)object, 0, 12, arg_0));
                            }
                            Object t = completableFuture13.join();
                            if (t == null) continue;
                            String string2 = VariantHandler.selectVariantFromKeyword((JsonObject)new JsonObject(t.toString()), (String)this.task.getSize(), (List)this.positiveKeywords, (List)this.negativeKeywords, (Shopify)this);
                            continue;
                        }
                        VertxUtil.sendSignal((String)this.instanceSignal, (Object)object.encode());
                    }
                    this.instanceSignal = n;
                }
                this.setProductPickupTime();
                this.genPaymentToken();
                if (this.isPassword) {
                    CompletableFuture completableFuture14 = this.shippingAndBillingPost();
                    if (!completableFuture14.isDone()) {
                        CompletableFuture completableFuture28 = completableFuture14;
                        return ((CompletableFuture)completableFuture28.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyBackend.async$run(this, completableFuture28, string, null, null, 0, 14, arg_0));
                    }
                    string = (String)completableFuture14.join();
                    Analytics.carts.increment();
                    CompletableFuture completableFuture15 = this.handlePreload(string);
                    if (!completableFuture15.isDone()) {
                        CompletableFuture completableFuture30 = completableFuture15;
                        return ((CompletableFuture)completableFuture30.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyBackend.async$run(this, completableFuture30, string, null, null, 0, 15, arg_0));
                    }
                    string = (String)completableFuture15.join();
                    CompletableFuture completableFuture16 = this.shippingAndBillingPut(string);
                    if (!completableFuture16.isDone()) {
                        CompletableFuture completableFuture32 = completableFuture16;
                        return ((CompletableFuture)completableFuture32.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyBackend.async$run(this, completableFuture32, string, null, null, 0, 16, arg_0));
                    }
                    completableFuture16.join();
                } else {
                    CompletableFuture completableFuture17 = this.walletPut(string, false, false);
                    if (!completableFuture17.isDone()) {
                        CompletableFuture completableFuture34 = completableFuture17;
                        return ((CompletableFuture)completableFuture34.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyBackend.async$run(this, completableFuture34, string, null, null, 0, 17, arg_0));
                    }
                    int n = ((Boolean)completableFuture17.join()).booleanValue();
                    Analytics.carts.increment();
                    if (n == 0) {
                        if (ThreadLocalRandom.current().nextBoolean()) {
                            CompletableFuture completableFuture18 = this.genCheckoutURL(ThreadLocalRandom.current().nextBoolean());
                            if (!completableFuture18.isDone()) {
                                CompletableFuture completableFuture36 = completableFuture18;
                                return ((CompletableFuture)completableFuture36.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyBackend.async$run(this, completableFuture36, string, null, null, n, 18, arg_0));
                            }
                            completableFuture18.join();
                        } else {
                            CompletableFuture completableFuture19 = this.genCheckoutURLViaCart();
                            if (!completableFuture19.isDone()) {
                                CompletableFuture completableFuture38 = completableFuture19;
                                return ((CompletableFuture)completableFuture38.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyBackend.async$run(this, completableFuture38, string, null, null, n, 19, arg_0));
                            }
                            completableFuture19.join();
                        }
                    }
                }
                if (!this.shippingRate.isDone()) {
                    this.getShippingRateOld();
                    this.getShippingRateAPI(string);
                }
                do {
                    CompletableFuture completableFuture20 = this.processPaymentAPI(string);
                    if (!completableFuture20.isDone()) {
                        CompletableFuture completableFuture40 = completableFuture20;
                        return ((CompletableFuture)completableFuture40.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyBackend.async$run(this, completableFuture40, string, null, null, 0, 20, arg_0));
                    }
                    completableFuture20.join();
                    CompletableFuture completableFuture21 = this.checkOrderAPI(string);
                    if (!completableFuture21.isDone()) {
                        CompletableFuture completableFuture42 = completableFuture21;
                        return ((CompletableFuture)completableFuture42.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyBackend.async$run(this, completableFuture42, string, null, null, 0, 21, arg_0));
                    }
                    jsonObject = (JsonObject)completableFuture21.join();
                    object = ShopifyBackend.parseDecline((JsonObject)jsonObject);
                    if (object != null && (((String)object).toLowerCase().contains("card was declined") || ((String)object).toLowerCase().contains("transaction has been declined") || ((String)object).toLowerCase().contains("couldn't be verified") || ((String)object).toLowerCase().contains("an issue processing your pay"))) {
                        Analytics.failure((String)object, (Task)this.task, (JsonObject)jsonObject, (String)this.api.proxyStringSafe());
                    }
                    this.logger.info("Checkout -> " + (String)object);
                } while (object != null && !((String)object).equals("success"));
                this.logger.info("Successfully checked out.");
                Analytics.success((Task)this.task, (JsonObject)jsonObject, (String)this.api.proxyStringSafe());
                break;
            }
            catch (Throwable throwable) {
                this.logger.error("Task interrupted: " + throwable.getMessage());
                this.setAttributes();
            }
        }
        return null;
    }

    public ShopifyBackend(Task task, int n) {
        super(task, n);
    }
}
