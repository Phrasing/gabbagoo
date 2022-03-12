/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.buffer.Buffer
 *  io.vertx.core.json.JsonObject
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.client.HttpResponse
 *  org.apache.logging.log4j.Logger
 */
package io.trickle.task.sites.walmart.canada;

import io.trickle.core.actor.TaskActor;
import io.trickle.task.Task;
import io.trickle.task.sites.walmart.canada.PaymentInstance$State;
import io.trickle.task.sites.walmart.canada.WalmartCA;
import io.trickle.task.sites.walmart.canada.WalmartCanadaAPI;
import io.trickle.task.sites.walmart.util.PaymentToken;
import io.trickle.util.analytics.Analytics;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;
import org.apache.logging.log4j.Logger;

public class PaymentInstance {
    public Task task;
    public PaymentToken token;
    public int previousResponseLen = 0;
    public String instanceSignal;
    public String paymentId;
    public WalmartCanadaAPI api;
    public TaskActor<?> parent;
    public int previousResponseHash = 0;
    public Logger logger;

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$init(PaymentInstance var0, CompletableFuture var1_1, JsonObject var2_3, JsonObject var3_5, JsonObject var4_6, int var5_7, Object var6_13) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [3[CASE]], but top level block is 12[UNCONDITIONALDOLOOP]
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
    public static CompletableFuture async$sendAndHandle(PaymentInstance var0, Supplier var1_1, Buffer var2_2, int var3_3, CompletableFuture var4_4, HttpResponse var5_6, Throwable var6_7, int var7_8, Object var8_9) {
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

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$processOrder(PaymentInstance var0, Buffer var1_1, int var2_2, int var3_3, HttpRequest var4_4, CompletableFuture var5_5, HttpResponse var6_7, String var7_8, Throwable var8_9, int var9_10, Object var10_11) {
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

    public static PaymentInstance get(WalmartCA walmartCA, Task task, PaymentToken paymentToken) {
        return new PaymentInstance(walmartCA, task, paymentToken);
    }

    public CompletableFuture processOrder() {
        Buffer buffer = this.api.placeOrderForm(this.token, this.paymentId);
        int n = 0;
        int n2 = 0;
        while (this.api.getWebClient().isActive()) {
            if (n2 > 100) return CompletableFuture.completedFuture(null);
            this.logger.info("Processing...");
            HttpRequest httpRequest = this.api.placeOrder();
            try {
                CompletableFuture completableFuture = Request.send(httpRequest, buffer);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$processOrder(this, buffer, n, n2, httpRequest, completableFuture2, null, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 200) {
                    if (n != 0) {
                        VertxUtil.sendSignal(this.instanceSignal);
                    }
                    this.logger.info("Successfully checked out with status '{}' ", (Object)httpResponse.statusCode());
                    Analytics.success(this.task, httpResponse.bodyAsJsonObject(), this.api.proxyString());
                    return CompletableFuture.completedFuture(200);
                }
                if (httpResponse.statusCode() == 405) {
                    this.logger.warn("Cart has expired with status'{}'. Re-submitting.", (Object)httpResponse.bodyAsString().toLowerCase());
                    return CompletableFuture.completedFuture(405);
                }
                String string = httpResponse.bodyAsString();
                if (string.contains("Availability for some of the items")) {
                    if (n != 0) {
                        this.logger.info("Waiting for restock with status '{}'", (Object)httpResponse.statusCode());
                    } else {
                        n = 1;
                        this.logger.warn("OOS on checkout with status '{}'", (Object)httpResponse.statusCode());
                        this.handleFailureWebhooks("Out Of Stock", (Buffer)httpResponse.body());
                    }
                    CompletableFuture completableFuture3 = VertxUtil.randomSignalSleep(this.instanceSignal, this.task.getMonitorDelay());
                    if (!completableFuture3.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture3;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$processOrder(this, buffer, n, n2, httpRequest, completableFuture4, httpResponse, string, null, 2, arg_0));
                    }
                    completableFuture3.join();
                    continue;
                }
                if (string.contains("Auth Declined") || string.contains("Invalid Card") || string.contains("payment_service_authorization_decline")) {
                    ++n2;
                    this.logger.warn("Card Decline with status: '{}'", (Object)httpResponse.statusCode());
                    this.handleFailureWebhooks("Card decline (FailedCharge)", (Buffer)httpResponse.body());
                } else {
                    this.logger.info("Failed to submit order with status: '{}'", (Object)httpResponse.statusCode());
                    CompletableFuture completableFuture5 = this.api.handleBadResponse(httpResponse.statusCode());
                    if (!completableFuture5.isDone()) {
                        CompletableFuture completableFuture6 = completableFuture5;
                        return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$processOrder(this, buffer, n, n2, httpRequest, completableFuture6, httpResponse, string, null, 3, arg_0));
                    }
                    completableFuture5.join();
                }
                CompletableFuture completableFuture7 = VertxUtil.randomSignalSleep(this.instanceSignal, this.task.getRetryDelay());
                if (!completableFuture7.isDone()) {
                    CompletableFuture completableFuture8 = completableFuture7;
                    return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$processOrder(this, buffer, n, n2, httpRequest, completableFuture8, httpResponse, null, null, 4, arg_0));
                }
                completableFuture7.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error occurred while processing payment: '{}'", (Object)throwable.getMessage());
                if (!throwable.getMessage().contains("Unexpected character")) continue;
                CompletableFuture completableFuture = VertxUtil.randomSleep(12000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture9 = completableFuture;
                    return ((CompletableFuture)completableFuture9.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$processOrder(this, buffer, n, n2, httpRequest, completableFuture9, null, null, throwable, 5, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public void handleFailureWebhooks(String string, Buffer buffer) {
        if (this.previousResponseHash != 0 && this.previousResponseLen == buffer.length()) {
            if (this.previousResponseHash == buffer.hashCode()) return;
        }
        try {
            Analytics.failure(string, this.task, buffer.toJsonObject(), this.api.proxyString());
            this.previousResponseHash = buffer.hashCode();
            this.previousResponseLen = buffer.length();
            return;
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public CompletableFuture sendAndHandle(Supplier supplier) {
        return this.sendAndHandle(supplier, null);
    }

    public CompletableFuture sendAndHandle(Supplier supplier, Buffer buffer) {
        int n = 0;
        while (this.api.getWebClient().isActive()) {
            if (n++ > 100) return CompletableFuture.completedFuture(null);
            try {
                HttpResponse httpResponse;
                if (buffer == null) {
                    CompletableFuture completableFuture = Request.send((HttpRequest)supplier.get());
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture2 = completableFuture;
                        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$sendAndHandle(this, (Supplier)supplier, buffer, n, completableFuture2, null, null, 1, arg_0));
                    }
                    httpResponse = (HttpResponse)completableFuture.join();
                } else {
                    CompletableFuture completableFuture = Request.send((HttpRequest)supplier.get(), buffer);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture3 = completableFuture;
                        return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$sendAndHandle(this, (Supplier)supplier, buffer, n, completableFuture3, null, null, 2, arg_0));
                    }
                    httpResponse = (HttpResponse)completableFuture.join();
                }
                if (httpResponse != null) {
                    if (httpResponse.statusCode() != 412) {
                        if (httpResponse.statusCode() != 444) return CompletableFuture.completedFuture(httpResponse.bodyAsJsonObject());
                    }
                    CompletableFuture completableFuture = this.api.handleBadResponse(httpResponse.statusCode());
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$sendAndHandle(this, (Supplier)supplier, buffer, n, completableFuture4, httpResponse, null, 3, arg_0));
                    }
                    completableFuture.join();
                    continue;
                }
                this.logger.warn("Retrying...");
                CompletableFuture completableFuture = VertxUtil.randomSleep(this.task.getRetryDelay());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture5 = completableFuture;
                    return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$sendAndHandle(this, (Supplier)supplier, buffer, n, completableFuture5, httpResponse, null, 4, arg_0));
                }
                completableFuture.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error occurred executing: {}", (Object)throwable.getMessage());
                if (!throwable.getMessage().contains("Unexpected character")) continue;
                CompletableFuture completableFuture = VertxUtil.randomSleep(12000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$sendAndHandle(this, (Supplier)supplier, buffer, n, completableFuture6, null, throwable, 5, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public PaymentInstance(WalmartCA walmartCA, Task task, PaymentToken paymentToken) {
        this.parent = walmartCA;
        this.api = (WalmartCanadaAPI)walmartCA.getClient();
        this.logger = walmartCA.getLogger();
        this.task = task;
        this.token = paymentToken;
        this.instanceSignal = this.task.getKeywords()[0];
    }

    public CompletableFuture init() {
        block13: {
            try {
                CompletableFuture completableFuture = this.sendAndHandle(this.api::initCheckout);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$init(this, completableFuture2, null, null, null, 1, arg_0));
                }
                JsonObject jsonObject = (JsonObject)completableFuture.join();
                if (jsonObject == null || jsonObject.getString("status", "error").contains("error")) break block13;
                try {
                    CompletableFuture completableFuture3 = this.sendAndHandle(this.api::submitEmail, this.api.submitEmailForm());
                    if (!completableFuture3.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture3;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$init(this, completableFuture4, jsonObject, null, null, 2, arg_0));
                    }
                    jsonObject = (JsonObject)completableFuture3.join();
                    if (jsonObject != null) {
                        CompletableFuture completableFuture5 = this.sendAndHandle(this.api::submitAddress, this.api.addressForm());
                        if (!completableFuture5.isDone()) {
                            CompletableFuture completableFuture6 = completableFuture5;
                            return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$init(this, completableFuture6, jsonObject, null, null, 3, arg_0));
                        }
                        jsonObject = (JsonObject)completableFuture5.join();
                        if (jsonObject != null) {
                            CompletableFuture completableFuture7 = this.sendAndHandle(this.api::submitCard, this.api.cardForm(this.token));
                            if (!completableFuture7.isDone()) {
                                CompletableFuture completableFuture8 = completableFuture7;
                                return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$init(this, completableFuture8, jsonObject, null, null, 4, arg_0));
                            }
                            JsonObject jsonObject2 = (JsonObject)completableFuture7.join();
                            if (jsonObject2 != null && jsonObject2.getBoolean("isSuccess", Boolean.valueOf(false)).booleanValue() && jsonObject2.containsKey("paymentMethods")) {
                                JsonObject jsonObject3;
                                JsonObject jsonObject4 = jsonObject2.getJsonArray("paymentMethods").getJsonObject(0);
                                this.token.setPiHash(jsonObject4.getString("piHash"));
                                CompletableFuture completableFuture9 = this.sendAndHandle(this.api::submitPayment, this.api.paymentForm(this.token));
                                if (!completableFuture9.isDone()) {
                                    CompletableFuture completableFuture10 = completableFuture9;
                                    return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$init(this, completableFuture10, jsonObject, jsonObject2, jsonObject4, 5, arg_0));
                                }
                                jsonObject = (JsonObject)completableFuture9.join();
                                if (jsonObject != null && jsonObject.containsKey("paymentInfo") && (jsonObject3 = jsonObject.getJsonArray("paymentInfo").getJsonObject(0)).containsKey("paymentId") && !jsonObject3.getString("paymentId", "").isBlank()) {
                                    this.paymentId = jsonObject3.getString("paymentId");
                                    if (!jsonObject.toString().contains("IN_STOCK")) return CompletableFuture.completedFuture(PaymentInstance$State.NO_STOCK);
                                    return CompletableFuture.completedFuture(PaymentInstance$State.PROCEED_CHECKOUT);
                                }
                            }
                        }
                    }
                }
                catch (Throwable throwable) {
                    this.logger.error("Error while generating checkout: {}", (Object)throwable.getMessage());
                    return CompletableFuture.completedFuture(PaymentInstance$State.FAILED_INIT);
                }
            }
            catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        this.logger.warn("Failed to generate checkout");
        return CompletableFuture.completedFuture(PaymentInstance$State.FAILED_INIT);
    }
}

