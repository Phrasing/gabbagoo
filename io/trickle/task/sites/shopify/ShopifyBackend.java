/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.json.JsonObject
 */
package io.trickle.task.sites.shopify;

import io.trickle.task.Task;
import io.trickle.task.sites.shopify.Shopify;
import io.trickle.task.sites.shopify.util.SiteParser;
import io.trickle.task.sites.shopify.util.VariantHandler;
import io.trickle.util.analytics.Analytics;
import io.trickle.util.concurrent.VertxUtil;
import io.vertx.core.json.JsonObject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class ShopifyBackend
extends Shopify {
    public boolean isPassword = true;

    @Override
    public CompletableFuture run() {
        CompletableFuture completableFuture = ShopifyBackend.initHarvesters();
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyBackend.async$run(this, completableFuture2, null, null, null, 0, 1, arg_0));
        }
        completableFuture.join();
        this.paymentGateway = SiteParser.getGatewayFromSite(this.task.getSite(), this.isCod);
        CompletableFuture completableFuture3 = this.initShopDetails();
        if (!completableFuture3.isDone()) {
            CompletableFuture completableFuture4 = completableFuture3;
            return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyBackend.async$run(this, completableFuture4, null, null, null, 0, 2, arg_0));
        }
        completableFuture3.join();
        if (this.task.getMode().contains("login")) {
            this.logger.info("Login required. Logging in...");
            CompletableFuture completableFuture5 = this.login();
            if (!completableFuture5.isDone()) {
                CompletableFuture completableFuture6 = completableFuture5;
                return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyBackend.async$run(this, completableFuture6, null, null, null, 0, 3, arg_0));
            }
            completableFuture5.join();
        }
        while (this.api.getWebClient().isActive()) {
            try {
                JsonObject jsonObject;
                Object object;
                Object object2;
                this.genPaymentToken();
                CompletableFuture completableFuture7 = this.genCheckoutURLIgnorePassword(true);
                if (!completableFuture7.isDone()) {
                    CompletableFuture completableFuture8 = completableFuture7;
                    return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyBackend.async$run(this, completableFuture8, null, null, null, 0, 4, arg_0));
                }
                String string = (String)completableFuture7.join();
                if (string != null) {
                    this.isPassword = false;
                    CompletableFuture completableFuture9 = this.handlePreload(string);
                    if (!completableFuture9.isDone()) {
                        CompletableFuture completableFuture10 = completableFuture9;
                        return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyBackend.async$run(this, completableFuture10, string, null, null, 0, 5, arg_0));
                    }
                    string = (String)completableFuture9.join();
                    CompletableFuture completableFuture11 = this.shippingAndBillingPut(string);
                    if (!completableFuture11.isDone()) {
                        CompletableFuture completableFuture12 = completableFuture11;
                        return ((CompletableFuture)completableFuture12.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyBackend.async$run(this, completableFuture12, string, null, null, 0, 6, arg_0));
                    }
                    completableFuture11.join();
                }
                if (!this.isKeyword) {
                    if (this.isEL) {
                        CompletableFuture completableFuture13 = this.fetchELJS();
                        if (!completableFuture13.isDone()) {
                            CompletableFuture completableFuture14 = completableFuture13;
                            return ((CompletableFuture)completableFuture14.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyBackend.async$run(this, completableFuture14, string, null, null, 0, 9, arg_0));
                        }
                        object2 = (JsonObject)completableFuture13.join();
                        this.instanceSignal = VariantHandler.selectVariantFromLink(object2, this.task.getSize(), this);
                    }
                } else {
                    object2 = null;
                    while (object2 == null) {
                        CompletableFuture completableFuture15 = this.fetchProductsJSON(true);
                        if (!completableFuture15.isDone()) {
                            CompletableFuture completableFuture16 = completableFuture15;
                            return ((CompletableFuture)completableFuture16.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyBackend.async$run(this, completableFuture16, string, (String)object2, null, 0, 7, arg_0));
                        }
                        object = (JsonObject)completableFuture15.join();
                        object2 = VariantHandler.selectVariantFromKeyword((JsonObject)object, this.task.getSize(), this.positiveKeywords, this.negativeKeywords, this);
                        if (object2 == null) {
                            CompletableFuture completableFuture17 = VertxUtil.signalSleep(this.instanceSignal, this.task.getMonitorDelay());
                            if (!completableFuture17.isDone()) {
                                CompletableFuture completableFuture18 = completableFuture17;
                                return ((CompletableFuture)completableFuture18.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyBackend.async$run(this, completableFuture18, string, (String)object2, (JsonObject)object, 0, 8, arg_0));
                            }
                            String string2 = (String)completableFuture17.join();
                            if (string2 == null) continue;
                            object2 = VariantHandler.selectVariantFromKeyword(new JsonObject(string2), this.task.getSize(), this.positiveKeywords, this.negativeKeywords, this);
                            continue;
                        }
                        VertxUtil.sendSignal(this.instanceSignal, object.encode());
                    }
                    this.instanceSignal = object2;
                }
                if (this.isPassword) {
                    CompletableFuture completableFuture19 = this.shippingAndBillingPost();
                    if (!completableFuture19.isDone()) {
                        CompletableFuture completableFuture20 = completableFuture19;
                        return ((CompletableFuture)completableFuture20.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyBackend.async$run(this, completableFuture20, string, null, null, 0, 10, arg_0));
                    }
                    string = (String)completableFuture19.join();
                    CompletableFuture completableFuture21 = this.handlePreload(string);
                    if (!completableFuture21.isDone()) {
                        CompletableFuture completableFuture22 = completableFuture21;
                        return ((CompletableFuture)completableFuture22.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyBackend.async$run(this, completableFuture22, string, null, null, 0, 11, arg_0));
                    }
                    string = (String)completableFuture21.join();
                    CompletableFuture completableFuture23 = this.shippingAndBillingPut(string);
                    if (!completableFuture23.isDone()) {
                        CompletableFuture completableFuture24 = completableFuture23;
                        return ((CompletableFuture)completableFuture24.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyBackend.async$run(this, completableFuture24, string, null, null, 0, 12, arg_0));
                    }
                    completableFuture23.join();
                } else {
                    CompletableFuture completableFuture25 = this.walletPut(string, false);
                    if (!completableFuture25.isDone()) {
                        CompletableFuture completableFuture26 = completableFuture25;
                        return ((CompletableFuture)completableFuture26.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyBackend.async$run(this, completableFuture26, string, null, null, 0, 13, arg_0));
                    }
                    int n = ((Boolean)completableFuture25.join()).booleanValue() ? 1 : 0;
                    if (n == 0) {
                        CompletableFuture completableFuture27 = this.atcAJAX(this.instanceSignal);
                        if (!completableFuture27.isDone()) {
                            CompletableFuture completableFuture28 = completableFuture27;
                            return ((CompletableFuture)completableFuture28.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyBackend.async$run(this, completableFuture28, string, null, null, n, 14, arg_0));
                        }
                        completableFuture27.join();
                        if (ThreadLocalRandom.current().nextBoolean()) {
                            CompletableFuture completableFuture29 = this.genCheckoutURL(ThreadLocalRandom.current().nextBoolean());
                            if (!completableFuture29.isDone()) {
                                CompletableFuture completableFuture30 = completableFuture29;
                                return ((CompletableFuture)completableFuture30.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyBackend.async$run(this, completableFuture30, string, null, null, n, 15, arg_0));
                            }
                            completableFuture29.join();
                        } else {
                            CompletableFuture completableFuture31 = this.genCheckoutURLViaCart();
                            if (!completableFuture31.isDone()) {
                                CompletableFuture completableFuture32 = completableFuture31;
                                return ((CompletableFuture)completableFuture32.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyBackend.async$run(this, completableFuture32, string, null, null, n, 16, arg_0));
                            }
                            completableFuture31.join();
                        }
                    }
                }
                if (!this.shippingRate.isDone()) {
                    this.getShippingRateOld();
                    this.getShippingRateAPI(string);
                }
                do {
                    CompletableFuture completableFuture33 = this.processPaymentAPI(string);
                    if (!completableFuture33.isDone()) {
                        CompletableFuture completableFuture34 = completableFuture33;
                        return ((CompletableFuture)completableFuture34.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyBackend.async$run(this, completableFuture34, string, null, null, 0, 17, arg_0));
                    }
                    completableFuture33.join();
                    CompletableFuture completableFuture35 = this.checkOrderAPI(string);
                    if (!completableFuture35.isDone()) {
                        CompletableFuture completableFuture36 = completableFuture35;
                        return ((CompletableFuture)completableFuture36.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifyBackend.async$run(this, completableFuture36, string, null, null, 0, 18, arg_0));
                    }
                    jsonObject = (JsonObject)completableFuture35.join();
                    object = ShopifyBackend.parseDecline(jsonObject);
                    if (object != null && (((String)object).toLowerCase().contains("card was declined") || ((String)object).toLowerCase().contains("transaction has been declined") || ((String)object).toLowerCase().contains("couldn't be verified") || ((String)object).toLowerCase().contains("an issue processing your pay"))) {
                        Analytics.failure((String)object, this.task, jsonObject, this.api.proxyString());
                    }
                    this.logger.info("Checkout -> " + (String)object);
                } while (object != null && !((String)object).equals("success"));
                this.logger.info("Successfully checked out.");
                Analytics.success(this.task, jsonObject, this.api.proxyString());
                return null;
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

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$run(ShopifyBackend var0, CompletableFuture var1_1, String var2_3, String var3_6, JsonObject var4_7, int var5_8, int var6_27, Object var7_28) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [41[DOLOOP]], but top level block is 43[UNCONDITIONALDOLOOP]
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

