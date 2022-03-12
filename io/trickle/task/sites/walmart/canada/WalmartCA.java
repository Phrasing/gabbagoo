/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.buffer.Buffer
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.client.HttpResponse
 *  io.vertx.ext.web.codec.BodyCodec
 */
package io.trickle.task.sites.walmart.canada;

import io.trickle.core.actor.TaskActor;
import io.trickle.task.Task;
import io.trickle.task.sites.walmart.canada.PaymentInstance;
import io.trickle.task.sites.walmart.canada.PaymentInstance$State;
import io.trickle.task.sites.walmart.canada.WalmartCanadaAPI;
import io.trickle.task.sites.walmart.util.PaymentToken;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.codec.BodyCodec;
import java.lang.invoke.LambdaMetafactory;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class WalmartCA
extends TaskActor {
    public Task task;
    public String instanceSignal;
    public PaymentInstance payment;
    public WalmartCanadaAPI api;
    public PaymentToken token;

    @Override
    public CompletableFuture run() {
        CompletableFuture completableFuture = this.generateToken();
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartCA.async$run(this, completableFuture2, 1, arg_0));
        }
        if (!((Boolean)completableFuture.join()).booleanValue()) {
            this.logger.error("Failed to initialize...");
            return CompletableFuture.completedFuture(null);
        }
        CompletableFuture completableFuture3 = this.tryCart();
        if (!completableFuture3.isDone()) {
            CompletableFuture completableFuture4 = completableFuture3;
            return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartCA.async$run(this, completableFuture4, 2, arg_0));
        }
        completableFuture3.join();
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture initCheckout() {
        this.payment = PaymentInstance.get(this, this.task, this.token);
        return this.payment.init();
    }

    public WalmartCA(Task task, int n) {
        super(n);
        this.task = task;
        this.api = new WalmartCanadaAPI(this.task);
        super.setClient(this.api);
        this.instanceSignal = this.task.getKeywords()[0];
    }

    public CompletableFuture tryCart() {
        int n = 0;
        Enum enum_ = null;
        while (this.running) {
            if (n == 0) {
                try {
                    CompletableFuture completableFuture = this.addToCart();
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture2 = completableFuture;
                        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartCA.async$tryCart(this, n, (PaymentInstance$State)enum_, completableFuture2, 1, arg_0));
                    }
                    n = ((Boolean)completableFuture.join()).booleanValue();
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                    continue;
                }
            }
            if (n == 0) continue;
            if (enum_ == null || enum_.equals((Object)PaymentInstance$State.FAILED_INIT)) {
                CompletableFuture completableFuture = this.initCheckout();
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture3 = completableFuture;
                    return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartCA.async$tryCart(this, n, (PaymentInstance$State)enum_, completableFuture3, 2, arg_0));
                }
                enum_ = (PaymentInstance$State)((Object)completableFuture.join());
                continue;
            }
            CompletableFuture completableFuture = this.payment.processOrder();
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture4 = completableFuture;
                return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartCA.async$tryCart(this, n, (PaymentInstance$State)enum_, completableFuture4, 3, arg_0));
            }
            int n2 = (Integer)completableFuture.join();
            if (n2 == 200) {
                return CompletableFuture.completedFuture(null);
            }
            if (n2 != 405) continue;
            this.api.getWebClient().cookieStore().clear();
            n = 0;
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$addToCart(WalmartCA var0, Buffer var1_1, int var2_2, HttpRequest var3_3, CompletableFuture var4_4, HttpResponse var5_6, int var6_8, Throwable var7_14, int var8_15, Object var9_16) {
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

    public CompletableFuture generateToken() {
        HttpRequest httpRequest = this.api.getWebClient().getAbs("https://securedataweb.walmart.com/pie/v1/wmcom_us_vtg_pie/getkey.js?bust=" + System.currentTimeMillis()).timeout(TimeUnit.SECONDS.toMillis(15L)).as(BodyCodec.string());
        try {
            CompletableFuture completableFuture = Request.execute(httpRequest, 5);
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartCA.async$generateToken(this, httpRequest, completableFuture2, 1, arg_0));
            }
            String string = (String)completableFuture.join();
            this.token = PaymentToken.prepareAndGenerate(string, this.task.getProfile().getCardNumber(), this.task.getProfile().getCvv());
            return CompletableFuture.completedFuture(true);
        }
        catch (Exception exception) {
            this.logger.warn("Failed to generate payment token: {}", (Object)exception.getMessage());
            return CompletableFuture.completedFuture(false);
        }
    }

    public CompletableFuture addToCart() {
        this.logger.info("Attempting add to cart");
        Buffer buffer = this.api.atcForm().toBuffer();
        int n = 0;
        while (this.running) {
            if (n++ > 50) return CompletableFuture.completedFuture(false);
            HttpRequest httpRequest = this.api.addToCart();
            try {
                CompletableFuture completableFuture = Request.send(httpRequest, buffer);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartCA.async$addToCart(this, buffer, n, httpRequest, completableFuture2, null, 0, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse != null) {
                    if (httpResponse.statusCode() == 200) {
                        this.logger.info("Successfully added to cart: status:'{}'", (Object)httpResponse.statusCode());
                        return CompletableFuture.completedFuture(true);
                    }
                    this.logger.warn("Failed to ATC: status:'{}'", (Object)httpResponse.statusCode());
                    CompletableFuture completableFuture2 = this.api.handleBadResponse(httpResponse.statusCode());
                    if (!completableFuture2.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture2;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartCA.async$addToCart(this, buffer, n, httpRequest, completableFuture4, httpResponse, 0, null, 2, arg_0));
                    }
                    int n2 = ((Boolean)completableFuture2.join()).booleanValue();
                    if (httpResponse.statusCode() != 412) {
                        CompletableFuture completableFuture3 = VertxUtil.randomSignalSleep(this.instanceSignal, this.task.getMonitorDelay());
                        if (!completableFuture3.isDone()) {
                            CompletableFuture completableFuture6 = completableFuture3;
                            return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartCA.async$addToCart(this, buffer, n, httpRequest, completableFuture6, httpResponse, n2, null, 3, arg_0));
                        }
                        completableFuture3.join();
                        continue;
                    }
                    if (n2 != 0) continue;
                    CompletableFuture completableFuture4 = super.randomSleep(12000);
                    if (!completableFuture4.isDone()) {
                        CompletableFuture completableFuture8 = completableFuture4;
                        return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartCA.async$addToCart(this, buffer, n, httpRequest, completableFuture8, httpResponse, n2, null, 4, arg_0));
                    }
                    completableFuture4.join();
                    continue;
                }
                this.logger.error("Retrying to ATC");
            }
            catch (Throwable throwable) {
                CompletableFuture completableFuture = super.randomSleep(12000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture9 = completableFuture;
                    return ((CompletableFuture)completableFuture9.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartCA.async$addToCart(this, buffer, n, httpRequest, completableFuture9, null, 0, throwable, 5, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(false);
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$run(WalmartCA var0, CompletableFuture var1_1, int var2_2, Object var3_3) {
        switch (var2_2) {
            case 0: {
                v0 = var0.generateToken();
                if (!v0.isDone()) {
                    var1_1 = v0;
                    return var1_1.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.walmart.canada.WalmartCA java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((WalmartCA)var0, (CompletableFuture)var1_1, (int)1));
                }
                ** GOTO lbl10
            }
            case 1: {
                v0 = var1_1;
lbl10:
                // 2 sources

                if (!((Boolean)v0.join()).booleanValue()) {
                    var0.logger.error("Failed to initialize...");
                    return CompletableFuture.completedFuture(null);
                }
                v1 = var0.tryCart();
                if (!v1.isDone()) {
                    var1_1 = v1;
                    return var1_1.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.walmart.canada.WalmartCA java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((WalmartCA)var0, (CompletableFuture)var1_1, (int)2));
                }
                ** GOTO lbl20
            }
            case 2: {
                v1 = var1_1;
lbl20:
                // 2 sources

                v1.join();
                return CompletableFuture.completedFuture(null);
            }
        }
        throw new IllegalArgumentException();
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$tryCart(WalmartCA var0, int var1_1, PaymentInstance$State var2_2, CompletableFuture var3_3, int var4_6, Object var5_10) {
        switch (var4_6) {
            case 0: {
                var1_1 = 0;
                var2_2 = null;
                block8: while (var0.running != false) {
                    if (var1_1 == 0) {
                        try {
                            v0 = var0.addToCart();
                            if (!v0.isDone()) {
                                var4_7 = v0;
                                return var4_7.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$tryCart(io.trickle.task.sites.walmart.canada.WalmartCA int io.trickle.task.sites.walmart.canada.PaymentInstance$State java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((WalmartCA)var0, (int)var1_1, (PaymentInstance$State)var2_2, (CompletableFuture)var4_7, (int)1));
                            }
lbl12:
                            // 3 sources

                            while (true) {
                                var1_1 = (int)((Boolean)v0.join()).booleanValue();
                                break;
                            }
                        }
                        catch (Exception var3_4) {
                            var3_4.printStackTrace();
                            continue;
                        }
                    }
                    if (var1_1 == 0) continue;
                    if (var2_2 == null || var2_2.equals((Object)PaymentInstance$State.FAILED_INIT)) {
                        v1 = var0.initCheckout();
                        if (!v1.isDone()) {
                            var4_8 = v1;
                            return var4_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$tryCart(io.trickle.task.sites.walmart.canada.WalmartCA int io.trickle.task.sites.walmart.canada.PaymentInstance$State java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((WalmartCA)var0, (int)var1_1, (PaymentInstance$State)var2_2, (CompletableFuture)var4_8, (int)2));
                        }
lbl24:
                        // 3 sources

                        while (true) {
                            var2_2 = (PaymentInstance$State)v1.join();
                            continue block8;
                            break;
                        }
                    }
                    v2 = var0.payment.processOrder();
                    if (!v2.isDone()) {
                        var4_9 = v2;
                        return var4_9.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$tryCart(io.trickle.task.sites.walmart.canada.WalmartCA int io.trickle.task.sites.walmart.canada.PaymentInstance$State java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((WalmartCA)var0, (int)var1_1, (PaymentInstance$State)var2_2, (CompletableFuture)var4_9, (int)3));
                    }
lbl31:
                    // 3 sources

                    while (true) {
                        var3_5 = (Integer)v2.join();
                        if (var3_5 == 200) {
                            return CompletableFuture.completedFuture(null);
                        }
                        if (var3_5 != 405) continue block8;
                        var0.api.getWebClient().cookieStore().clear();
                        var1_1 = 0;
                        continue block8;
                        break;
                    }
                }
                return CompletableFuture.completedFuture(null);
            }
            case 1: {
                v0 = var3_3;
                ** continue;
            }
            case 2: {
                v1 = var3_3;
                ** continue;
            }
            case 3: {
                v2 = var3_3;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$generateToken(WalmartCA var0, HttpRequest var1_1, CompletableFuture var2_2, int var3_4, Object var4_6) {
        switch (var3_4) {
            case 0: {
                var1_1 = var0.api.getWebClient().getAbs("https://securedataweb.walmart.com/pie/v1/wmcom_us_vtg_pie/getkey.js?bust=" + System.currentTimeMillis()).timeout(TimeUnit.SECONDS.toMillis(15L)).as(BodyCodec.string());
                try {
                    v0 = Request.execute(var1_1, 5);
                    if (!v0.isDone()) {
                        var3_5 = v0;
                        return var3_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$generateToken(io.trickle.task.sites.walmart.canada.WalmartCA io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((WalmartCA)var0, (HttpRequest)var1_1, (CompletableFuture)var3_5, (int)1));
                    }
                    ** GOTO lbl15
                }
                catch (Exception var2_3) {
                    var0.logger.warn("Failed to generate payment token: {}", (Object)var2_3.getMessage());
                    return CompletableFuture.completedFuture(false);
                }
            }
            case 1: {
                v0 = var2_2;
lbl15:
                // 2 sources

                var2_2 = (String)v0.join();
                var0.token = PaymentToken.prepareAndGenerate((String)var2_2, var0.task.getProfile().getCardNumber(), var0.task.getProfile().getCvv());
                return CompletableFuture.completedFuture(true);
            }
        }
        throw new IllegalArgumentException();
    }
}

