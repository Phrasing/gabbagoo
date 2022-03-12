/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.buffer.Buffer
 *  io.vertx.core.json.DecodeException
 *  io.vertx.core.json.JsonObject
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.client.HttpResponse
 *  org.apache.logging.log4j.Logger
 */
package io.trickle.task.sites.walmart.usa;

import io.trickle.core.actor.TaskActor;
import io.trickle.task.Task;
import io.trickle.task.sites.walmart.Mode;
import io.trickle.task.sites.walmart.usa.API;
import io.trickle.task.sites.walmart.usa.Walmart;
import io.trickle.task.sites.walmart.util.PaymentToken;
import io.trickle.util.analytics.Analytics;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import java.lang.invoke.LambdaMetafactory;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import org.apache.logging.log4j.Logger;

public class PaymentInstance {
    public TaskActor<?> parent;
    public Task task;
    public int previousResponseHash = 0;
    public JsonObject cartInfo;
    public API api;
    public Logger logger;
    public int previousResponseLen = 0;
    public PaymentToken token;
    public boolean griefAlt;
    public String instanceSignal;
    public boolean griefMode;
    public boolean isCheckout;
    public Mode mode;

    public static PaymentInstance get(Walmart walmart, Task task, PaymentToken paymentToken, Mode mode, boolean bl) {
        return new PaymentInstance(walmart, task, paymentToken, mode, bl);
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$checkout(PaymentInstance var0, int var1_1, PaymentInstance var2_2, CompletableFuture var3_9, double var4_10, CompletableFuture var6_17, CompletableFuture var7_20, long var8_22, int var10_43, Object var11_44) {
        switch (var10_43) {
            case 0: {
                var0.instanceSignal = var0.task.getKeywords()[0];
                try {
                    var1_1 = Integer.parseInt(var0.task.getKeywords()[1]);
                }
                catch (NumberFormatException var2_3) {
                    var0.logger.warn("Missing price-check (limit) for product: '{}'. Skipping...", (Object)var0.instanceSignal);
                    var1_1 = 0x7FFFFFFF;
                }
                try {
                    v0 = var0;
                    v1 = var0.getPCID();
                    if (!v1.isDone()) {
                        var9_45 = v1;
                        var8_23 = v0;
                        return var9_45.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$checkout(io.trickle.task.sites.walmart.usa.PaymentInstance int io.trickle.task.sites.walmart.usa.PaymentInstance java.util.concurrent.CompletableFuture double java.util.concurrent.CompletableFuture java.util.concurrent.CompletableFuture long int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PaymentInstance)var0, (int)var1_1, (PaymentInstance)var8_23, (CompletableFuture)var9_45, (double)Double.longBitsToDouble(0L), null, null, (long)0L, (int)1));
                    }
lbl17:
                    // 3 sources

                    while (true) {
                        v0.cartInfo = (JsonObject)v1.join();
                        break;
                    }
                }
                catch (Throwable var2_4) {
                    return CompletableFuture.completedFuture(-1);
                }
                if (var0.cartInfo == null) return CompletableFuture.failedFuture(new Exception("Failed to get PCID"));
                if (var0.cartInfo.containsKey("items") == false) return CompletableFuture.failedFuture(new Exception("Failed to get PCID"));
                var2_5 = var0.cartInfo.getJsonArray("items").getJsonObject(0).getDouble("unitPrice");
                if (var2_5 > (double)var1_1) {
                    return CompletableFuture.failedFuture(new Exception("Price exceeds limit of $" + var1_1));
                }
                var0.logger.info("Attempting to checkout");
                if (!var0.griefMode) ** GOTO lbl157
                if (!var0.griefAlt) ** GOTO lbl90
                var4_11 = var0.processPayment();
                var5_51 = var0.api.getWebClient().windowUpdateCallback();
                if (var5_51 == null) ** GOTO lbl43
                var0.selectShipping();
                v2 = VertxUtil.handleEagerFuture(var5_51);
                if (!v2.isDone()) {
                    var8_24 = v2;
                    return var8_24.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$checkout(io.trickle.task.sites.walmart.usa.PaymentInstance int io.trickle.task.sites.walmart.usa.PaymentInstance java.util.concurrent.CompletableFuture double java.util.concurrent.CompletableFuture java.util.concurrent.CompletableFuture long int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PaymentInstance)var0, (int)var1_1, null, (CompletableFuture)var4_11, (double)var2_5, (CompletableFuture)var5_51, (CompletableFuture)var8_24, (long)0L, (int)2));
                }
lbl39:
                // 3 sources

                while (true) {
                    v2.join();
                    ** GOTO lbl50
                    break;
                }
lbl43:
                // 1 sources

                v3 = var0.selectShipping();
                if (!v3.isDone()) {
                    var8_25 = v3;
                    return var8_25.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$checkout(io.trickle.task.sites.walmart.usa.PaymentInstance int io.trickle.task.sites.walmart.usa.PaymentInstance java.util.concurrent.CompletableFuture double java.util.concurrent.CompletableFuture java.util.concurrent.CompletableFuture long int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PaymentInstance)var0, (int)var1_1, null, (CompletableFuture)var4_11, (double)var2_5, (CompletableFuture)var5_51, (CompletableFuture)var8_25, (long)0L, (int)3));
                }
lbl47:
                // 3 sources

                while (true) {
                    v3.join();
lbl50:
                    // 2 sources

                    var5_51 = var0.api.getWebClient().windowUpdateCallback();
                    if (var5_51 == null) ** GOTO lbl70
                    var6_18 = Instant.now().toEpochMilli();
                    var0.submitShipping();
                    v4 = VertxUtil.handleEagerFuture(var5_51);
                    if (!v4.isDone()) {
                        var8_26 = v4;
                        return var8_26.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$checkout(io.trickle.task.sites.walmart.usa.PaymentInstance int io.trickle.task.sites.walmart.usa.PaymentInstance java.util.concurrent.CompletableFuture double java.util.concurrent.CompletableFuture java.util.concurrent.CompletableFuture long int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PaymentInstance)var0, (int)var1_1, null, (CompletableFuture)var4_11, (double)var2_5, (CompletableFuture)var5_51, (CompletableFuture)var8_26, (long)var6_18, (int)4));
                    }
lbl59:
                    // 3 sources

                    while (true) {
                        v4.join();
                        v5 = VertxUtil.hardCodedSleep(300L - (Instant.now().toEpochMilli() - var6_18));
                        if (!v5.isDone()) {
                            var8_27 = v5;
                            return var8_27.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$checkout(io.trickle.task.sites.walmart.usa.PaymentInstance int io.trickle.task.sites.walmart.usa.PaymentInstance java.util.concurrent.CompletableFuture double java.util.concurrent.CompletableFuture java.util.concurrent.CompletableFuture long int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PaymentInstance)var0, (int)var1_1, null, (CompletableFuture)var4_11, (double)var2_5, (CompletableFuture)var5_51, (CompletableFuture)var8_27, (long)var6_18, (int)5));
                        }
lbl66:
                        // 3 sources

                        while (true) {
                            v5.join();
                            ** GOTO lbl77
                            break;
                        }
                        break;
                    }
lbl70:
                    // 1 sources

                    v6 = var0.submitShipping();
                    if (!v6.isDone()) {
                        var8_28 = v6;
                        return var8_28.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$checkout(io.trickle.task.sites.walmart.usa.PaymentInstance int io.trickle.task.sites.walmart.usa.PaymentInstance java.util.concurrent.CompletableFuture double java.util.concurrent.CompletableFuture java.util.concurrent.CompletableFuture long int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PaymentInstance)var0, (int)var1_1, null, (CompletableFuture)var4_11, (double)var2_5, (CompletableFuture)var5_51, (CompletableFuture)var8_28, (long)0L, (int)6));
                    }
lbl74:
                    // 3 sources

                    while (true) {
                        v6.join();
lbl77:
                        // 2 sources

                        v7 = var0;
                        v8 = var0.submitPayment();
                        if (!v8.isDone()) {
                            var9_46 = v8;
                            var8_29 = v7;
                            return var9_46.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$checkout(io.trickle.task.sites.walmart.usa.PaymentInstance int io.trickle.task.sites.walmart.usa.PaymentInstance java.util.concurrent.CompletableFuture double java.util.concurrent.CompletableFuture java.util.concurrent.CompletableFuture long int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PaymentInstance)var0, (int)var1_1, (PaymentInstance)var8_29, (CompletableFuture)var4_11, (double)var2_5, (CompletableFuture)var5_51, (CompletableFuture)var9_46, (long)0L, (int)7));
                        }
lbl83:
                        // 3 sources

                        while (true) {
                            v7.cartInfo = (JsonObject)v8.join();
                            v9 = var4_11;
                            if (!v9.isDone()) {
                                var8_30 = v9;
                                return var8_30.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$checkout(io.trickle.task.sites.walmart.usa.PaymentInstance int io.trickle.task.sites.walmart.usa.PaymentInstance java.util.concurrent.CompletableFuture double java.util.concurrent.CompletableFuture java.util.concurrent.CompletableFuture long int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PaymentInstance)var0, (int)var1_1, null, (CompletableFuture)var4_11, (double)var2_5, (CompletableFuture)var5_51, (CompletableFuture)var8_30, (long)0L, (int)8));
                            }
                            ** GOTO lbl234
                            break;
                        }
                        break;
                    }
                    break;
                }
lbl90:
                // 1 sources

                var6_17 = var0.api.getWebClient().windowUpdateCallback();
                if (var6_17 == null) ** GOTO lbl110
                var4_12 = Instant.now().toEpochMilli();
                var0.selectShipping();
                v10 = VertxUtil.handleEagerFuture(var6_17);
                if (!v10.isDone()) {
                    var8_31 = v10;
                    return var8_31.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$checkout(io.trickle.task.sites.walmart.usa.PaymentInstance int io.trickle.task.sites.walmart.usa.PaymentInstance java.util.concurrent.CompletableFuture double java.util.concurrent.CompletableFuture java.util.concurrent.CompletableFuture long int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PaymentInstance)var0, (int)var1_1, null, (CompletableFuture)var6_17, (double)var2_5, (CompletableFuture)var8_31, null, (long)var4_12, (int)9));
                }
lbl99:
                // 3 sources

                while (true) {
                    v10.join();
                    v11 = VertxUtil.hardCodedSleep(50L - (Instant.now().toEpochMilli() - var4_12));
                    if (!v11.isDone()) {
                        var8_32 = v11;
                        return var8_32.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$checkout(io.trickle.task.sites.walmart.usa.PaymentInstance int io.trickle.task.sites.walmart.usa.PaymentInstance java.util.concurrent.CompletableFuture double java.util.concurrent.CompletableFuture java.util.concurrent.CompletableFuture long int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PaymentInstance)var0, (int)var1_1, null, (CompletableFuture)var6_17, (double)var2_5, (CompletableFuture)var8_32, null, (long)var4_12, (int)10));
                    }
lbl106:
                    // 3 sources

                    while (true) {
                        v11.join();
                        ** GOTO lbl117
                        break;
                    }
                    break;
                }
lbl110:
                // 1 sources

                v12 = var0.selectShipping();
                if (!v12.isDone()) {
                    var8_33 = v12;
                    return var8_33.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$checkout(io.trickle.task.sites.walmart.usa.PaymentInstance int io.trickle.task.sites.walmart.usa.PaymentInstance java.util.concurrent.CompletableFuture double java.util.concurrent.CompletableFuture java.util.concurrent.CompletableFuture long int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PaymentInstance)var0, (int)var1_1, null, (CompletableFuture)var6_17, (double)var2_5, (CompletableFuture)var8_33, null, (long)0L, (int)11));
                }
lbl114:
                // 3 sources

                while (true) {
                    v12.join();
lbl117:
                    // 2 sources

                    var6_17 = var0.api.getWebClient().windowUpdateCallback();
                    if (var6_17 == null) ** GOTO lbl137
                    var4_13 = Instant.now().toEpochMilli();
                    var0.submitShipping();
                    v13 = VertxUtil.handleEagerFuture(var6_17);
                    if (!v13.isDone()) {
                        var8_34 = v13;
                        return var8_34.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$checkout(io.trickle.task.sites.walmart.usa.PaymentInstance int io.trickle.task.sites.walmart.usa.PaymentInstance java.util.concurrent.CompletableFuture double java.util.concurrent.CompletableFuture java.util.concurrent.CompletableFuture long int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PaymentInstance)var0, (int)var1_1, null, (CompletableFuture)var6_17, (double)var2_5, (CompletableFuture)var8_34, null, (long)var4_13, (int)12));
                    }
lbl126:
                    // 3 sources

                    while (true) {
                        v13.join();
                        v14 = VertxUtil.hardCodedSleep(300L - (Instant.now().toEpochMilli() - var4_13));
                        if (!v14.isDone()) {
                            var8_35 = v14;
                            return var8_35.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$checkout(io.trickle.task.sites.walmart.usa.PaymentInstance int io.trickle.task.sites.walmart.usa.PaymentInstance java.util.concurrent.CompletableFuture double java.util.concurrent.CompletableFuture java.util.concurrent.CompletableFuture long int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PaymentInstance)var0, (int)var1_1, null, (CompletableFuture)var6_17, (double)var2_5, (CompletableFuture)var8_35, null, (long)var4_13, (int)13));
                        }
lbl133:
                        // 3 sources

                        while (true) {
                            v14.join();
                            ** GOTO lbl144
                            break;
                        }
                        break;
                    }
lbl137:
                    // 1 sources

                    v15 = var0.submitShipping();
                    if (!v15.isDone()) {
                        var8_36 = v15;
                        return var8_36.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$checkout(io.trickle.task.sites.walmart.usa.PaymentInstance int io.trickle.task.sites.walmart.usa.PaymentInstance java.util.concurrent.CompletableFuture double java.util.concurrent.CompletableFuture java.util.concurrent.CompletableFuture long int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PaymentInstance)var0, (int)var1_1, null, (CompletableFuture)var6_17, (double)var2_5, (CompletableFuture)var8_36, null, (long)0L, (int)14));
                    }
lbl141:
                    // 3 sources

                    while (true) {
                        v15.join();
lbl144:
                        // 2 sources

                        v16 = var0;
                        v17 = var0.submitPayment();
                        if (!v17.isDone()) {
                            var9_47 = v17;
                            var8_37 = v16;
                            return var9_47.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$checkout(io.trickle.task.sites.walmart.usa.PaymentInstance int io.trickle.task.sites.walmart.usa.PaymentInstance java.util.concurrent.CompletableFuture double java.util.concurrent.CompletableFuture java.util.concurrent.CompletableFuture long int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PaymentInstance)var0, (int)var1_1, (PaymentInstance)var8_37, (CompletableFuture)var6_17, (double)var2_5, (CompletableFuture)var9_47, null, (long)0L, (int)15));
                        }
lbl150:
                        // 3 sources

                        while (true) {
                            v16.cartInfo = (JsonObject)v17.join();
                            v18 = var0.processPayment();
                            if (!v18.isDone()) {
                                var8_38 = v18;
                                return var8_38.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$checkout(io.trickle.task.sites.walmart.usa.PaymentInstance int io.trickle.task.sites.walmart.usa.PaymentInstance java.util.concurrent.CompletableFuture double java.util.concurrent.CompletableFuture java.util.concurrent.CompletableFuture long int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PaymentInstance)var0, (int)var1_1, null, (CompletableFuture)var6_17, (double)var2_5, (CompletableFuture)var8_38, null, (long)0L, (int)16));
                            }
                            ** GOTO lbl280
                            break;
                        }
                        break;
                    }
                    break;
                }
lbl157:
                // 1 sources

                v19 = var0;
                v20 = var0.selectShipping();
                if (!v20.isDone()) {
                    var9_48 = v20;
                    var8_39 = v19;
                    return var9_48.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$checkout(io.trickle.task.sites.walmart.usa.PaymentInstance int io.trickle.task.sites.walmart.usa.PaymentInstance java.util.concurrent.CompletableFuture double java.util.concurrent.CompletableFuture java.util.concurrent.CompletableFuture long int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PaymentInstance)var0, (int)var1_1, (PaymentInstance)var8_39, (CompletableFuture)var9_48, (double)var2_5, null, null, (long)0L, (int)17));
                }
lbl163:
                // 3 sources

                while (true) {
                    v19.cartInfo = (JsonObject)v20.join();
                    v21 = var0;
                    v22 = var0.submitShipping();
                    if (!v22.isDone()) {
                        var9_49 = v22;
                        var8_40 = v21;
                        return var9_49.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$checkout(io.trickle.task.sites.walmart.usa.PaymentInstance int io.trickle.task.sites.walmart.usa.PaymentInstance java.util.concurrent.CompletableFuture double java.util.concurrent.CompletableFuture java.util.concurrent.CompletableFuture long int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PaymentInstance)var0, (int)var1_1, (PaymentInstance)var8_40, (CompletableFuture)var9_49, (double)var2_5, null, null, (long)0L, (int)18));
                    }
lbl171:
                    // 3 sources

                    while (true) {
                        v21.cartInfo = (JsonObject)v22.join();
                        v23 = var0;
                        v24 = var0.submitPayment();
                        if (!v24.isDone()) {
                            var9_50 = v24;
                            var8_41 = v23;
                            return var9_50.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$checkout(io.trickle.task.sites.walmart.usa.PaymentInstance int io.trickle.task.sites.walmart.usa.PaymentInstance java.util.concurrent.CompletableFuture double java.util.concurrent.CompletableFuture java.util.concurrent.CompletableFuture long int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PaymentInstance)var0, (int)var1_1, (PaymentInstance)var8_41, (CompletableFuture)var9_50, (double)var2_5, null, null, (long)0L, (int)19));
                        }
lbl179:
                        // 3 sources

                        while (true) {
                            v23.cartInfo = (JsonObject)v24.join();
                            v25 = var0.processPayment();
                            if (!v25.isDone()) {
                                var8_42 = v25;
                                return var8_42.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$checkout(io.trickle.task.sites.walmart.usa.PaymentInstance int io.trickle.task.sites.walmart.usa.PaymentInstance java.util.concurrent.CompletableFuture double java.util.concurrent.CompletableFuture java.util.concurrent.CompletableFuture long int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PaymentInstance)var0, (int)var1_1, null, (CompletableFuture)var8_42, (double)var2_5, null, null, (long)0L, (int)20));
                            }
                            ** GOTO lbl300
                            break;
                        }
                        break;
                    }
                    break;
                }
            }
            case 1: {
                v0 = var2_2;
                v1 = var3_9;
                ** continue;
            }
            case 2: {
                v2 = var7_20;
                var5_51 = var6_17;
                var4_11 = var3_9;
                var2_5 = var4_10;
                ** continue;
            }
            case 3: {
                v3 = var7_20;
                var5_51 = var6_17;
                var4_11 = var3_9;
                var2_5 = var4_10;
                ** continue;
            }
            case 4: {
                v4 = var7_20;
                var6_18 = var8_22;
                var5_51 = var6_17;
                var4_11 = var3_9;
                var2_5 = var4_10;
                ** continue;
            }
            case 5: {
                v5 = var7_20;
                var6_18 = var8_22;
                var5_51 = var6_17;
                var4_11 = var3_9;
                var2_5 = var4_10;
                ** continue;
            }
            case 6: {
                v6 = var7_20;
                var5_51 = var6_17;
                var4_11 = var3_9;
                var2_5 = var4_10;
                ** continue;
            }
            case 7: {
                v7 = var2_2;
                v8 = var7_20;
                var5_51 = var6_17;
                var4_11 = var3_9;
                var2_5 = var4_10;
                ** continue;
            }
            case 8: {
                v9 = var7_20;
                var5_52 = var6_17;
                var4_15 = var3_9;
                var2_6 = var4_10;
lbl234:
                // 2 sources

                var6_19 = (Integer)v9.join();
                return CompletableFuture.completedFuture(var6_19);
            }
            case 9: {
                v10 = var6_17;
                var6_17 = var3_9;
                var4_12 = var8_22;
                var2_5 = var4_10;
                ** continue;
            }
            case 10: {
                v11 = var6_17;
                var6_17 = var3_9;
                var4_12 = var8_22;
                var2_5 = var4_10;
                ** continue;
            }
            case 11: {
                v12 = var6_17;
                var6_17 = var3_9;
                var2_5 = var4_10;
                ** continue;
            }
            case 12: {
                v13 = var6_17;
                var6_17 = var3_9;
                var4_13 = var8_22;
                var2_5 = var4_10;
                ** continue;
            }
            case 13: {
                v14 = var6_17;
                var6_17 = var3_9;
                var4_16 = var8_22;
                var2_5 = var4_10;
                ** continue;
            }
            case 14: {
                v15 = var6_17;
                var6_17 = var3_9;
                var2_5 = var4_10;
                ** continue;
            }
            case 15: {
                v16 = var2_2;
                v17 = var6_17;
                var6_17 = var3_9;
                var2_5 = var4_10;
                ** continue;
            }
            case 16: {
                v18 = var6_17;
                var6_17 = var3_9;
                var2_7 = var4_10;
lbl280:
                // 2 sources

                var7_21 = (Integer)v18.join();
                return CompletableFuture.completedFuture(var7_21);
            }
            case 17: {
                v19 = var2_2;
                v20 = var3_9;
                var2_5 = var4_10;
                ** continue;
            }
            case 18: {
                v21 = var2_2;
                v22 = var3_9;
                var2_5 = var4_10;
                ** continue;
            }
            case 19: {
                v23 = var2_2;
                v24 = var3_9;
                var2_5 = var4_10;
                ** continue;
            }
            case 20: {
                v25 = var3_9;
                var2_8 = var4_10;
lbl300:
                // 2 sources

                var4_14 = (Integer)v25.join();
                return CompletableFuture.completedFuture(var4_14);
            }
        }
        throw new IllegalArgumentException();
    }

    public void checkBadStatus(int n) {
        switch (n) {
            case 412: {
                this.logger.warn("PX Block with status:'412'. Retrying...");
                return;
            }
            case 307: {
                this.logger.warn("PX Block with status:'307'. Retrying...");
                return;
            }
            case 444: {
                this.logger.warn("Failed to execute due to status '444': PROXY_BAN");
                return;
            }
        }
        this.logger.error("Failed to execute due to status '{}'", (Object)n);
    }

    public CompletableFuture submitShipping() {
        Buffer buffer = this.api.getShippingForm(this.cartInfo).toBuffer();
        this.logger.info("Generating checkout step #3");
        while (this.api.getWebClient().isActive()) {
            HttpRequest httpRequest = this.api.submitShipping();
            try {
                CompletableFuture completableFuture = Request.send(httpRequest, buffer);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$submitShipping(this, buffer, httpRequest, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse != null) {
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug("submitShipping responded with: [{}] {}", (Object)httpResponse.statusCode(), (Object)httpResponse.bodyAsString());
                    }
                    if (httpResponse.statusCode() == 200) {
                        return CompletableFuture.completedFuture(httpResponse.bodyAsJsonObject());
                    }
                    this.logger.warn("Failed to generate checkout step #3: status:'{}'", (Object)httpResponse.statusCode());
                    if (!this.isCheckout) {
                        return CompletableFuture.completedFuture(null);
                    }
                    CompletableFuture completableFuture3 = this.api.handleBadResponse(httpResponse.statusCode(), httpResponse);
                    if (!completableFuture3.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture3;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$submitShipping(this, buffer, httpRequest, completableFuture4, httpResponse, null, 2, arg_0));
                    }
                    completableFuture3.join();
                }
                if (this.griefMode || httpResponse == null || httpResponse.statusCode() == 412 || httpResponse.statusCode() == 307) continue;
                CompletableFuture completableFuture5 = VertxUtil.randomSleep(this.task.getRetryDelay());
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$submitShipping(this, buffer, httpRequest, completableFuture6, httpResponse, null, 3, arg_0));
                }
                completableFuture5.join();
            }
            catch (Exception exception) {
                this.logger.error("Error occurred on checkout step #3: '{}'", (Object)exception.getMessage());
                if (!this.isCheckout) return CompletableFuture.completedFuture(null);
                if (!exception.getMessage().contains("Unexpected character") && !(exception instanceof DecodeException)) continue;
                CompletableFuture completableFuture = VertxUtil.randomSleep(12000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$submitShipping(this, buffer, httpRequest, completableFuture7, null, exception, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$submitPayment$1(PaymentInstance var0, int var1_1, Buffer var2_2, HttpRequest var3_3, CompletableFuture var4_4, HttpResponse var5_6, Exception var6_7, int var7_8, Object var8_9) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [2[CASE], 10[UNCONDITIONALDOLOOP]], but top level block is 0[TRYBLOCK]
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
    public static CompletableFuture async$submitPayment(PaymentInstance var0, Buffer var1_1, HttpRequest var2_2, CompletableFuture var3_3, HttpResponse var4_5, Exception var5_6, int var6_7, Object var7_8) {
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
    public static CompletableFuture async$getPCID(PaymentInstance var0, int var1_1, Buffer var2_2, HttpRequest var3_3, CompletableFuture var4_4, HttpResponse var5_6, Exception var6_7, int var7_8, Object var8_9) {
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

    public static CompletableFuture checkout(Walmart walmart, Task task, PaymentToken paymentToken, Mode mode) {
        return PaymentInstance.get(walmart, task, paymentToken, mode, true).checkout();
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$addToCart(PaymentInstance var0, Buffer var1_1, int var2_2, HttpRequest var3_3, CompletableFuture var4_4, HttpResponse var5_6, int var6_8, Exception var7_13, int var8_14, Object var9_15) {
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
    public static CompletableFuture async$selectShipping(PaymentInstance var0, Buffer var1_1, HttpRequest var2_2, CompletableFuture var3_3, HttpResponse var4_5, Exception var5_6, int var6_7, Object var7_8) {
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

    public CompletableFuture processPayment(boolean bl) {
        Buffer buffer = this.api.getProcessingForm(this.token).toBuffer();
        int n = 0;
        int n2 = bl ? 0 : 47;
        while (this.api.getWebClient().isActive()) {
            if (n2 > 50) return CompletableFuture.completedFuture(-1);
            HttpRequest httpRequest = this.api.processPayment(this.token);
            this.logger.info("Processing...");
            try {
                CompletableFuture completableFuture = Request.send(httpRequest, buffer);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$processPayment(this, (int)(bl ? 1 : 0), buffer, n, n2, httpRequest, completableFuture2, null, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse != null) {
                    String string = httpResponse.bodyAsString().toLowerCase();
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug("processPayment responded with: [{}] {}", (Object)httpResponse.statusCode(), (Object)httpResponse.bodyAsString());
                    }
                    if (httpResponse.statusCode() == 200) {
                        if (!string.contains("orderid")) {
                            this.logger.warn("Something went wrong while processing: status'{}'", (Object)httpResponse.bodyAsString().toLowerCase());
                            this.handleFailureWebhooks("Misc Error", (Buffer)httpResponse.body());
                            return CompletableFuture.completedFuture(-1);
                        }
                        if (n != 0) {
                            VertxUtil.sendSignal(this.instanceSignal);
                        }
                        this.logger.info("Successfully checked out with status '{}' ", (Object)httpResponse.statusCode());
                        Analytics.success(this.task, httpResponse.bodyAsJsonObject(), this.api.proxyString());
                        return CompletableFuture.completedFuture(200);
                    }
                    if (string.contains("missing")) {
                        this.logger.warn("Missing payment info. Re-submitting...");
                        if (this.griefAlt && ++n2 <= 15 || this.griefMode && n2 <= 8) {
                            continue;
                        }
                    } else if (string.contains("different payment")) {
                        this.logger.warn("Card Decline[Invalid/FailedCharge] with status '{}'. Retrying...", (Object)httpResponse.statusCode());
                        this.handleFailureWebhooks("Card decline (Invalid/FailedCharge)", (Buffer)httpResponse.body());
                        if (this.griefMode && ++n2 <= 3) continue;
                        this.handleFailureWebhooks("Card decline (FailedCharge)", (Buffer)httpResponse.body());
                    } else if (string.contains("different card")) {
                        this.logger.warn("Card Decline[FailedCharge] with status '{}'. Retrying..", (Object)httpResponse.statusCode());
                        if (this.griefMode && ++n2 <= 3) continue;
                        this.handleFailureWebhooks("Card decline (FailedCharge)", (Buffer)httpResponse.body());
                    } else if (string.contains("stock")) {
                        n = 1;
                        this.logger.info("OOS on checkout with status '{}'. Retrying...", (Object)httpResponse.statusCode());
                        if (this.griefMode && n2++ <= 6) continue;
                        this.handleFailureWebhooks("Out Of Stock", (Buffer)httpResponse.body());
                    } else {
                        if (httpResponse.statusCode() == 405 || string.contains("contract has expired")) {
                            this.logger.warn("Cart has expired with status'{}'. Re-submitting.", (Object)httpResponse.bodyAsString().toLowerCase());
                            return CompletableFuture.completedFuture(-1);
                        }
                        this.checkBadStatus(httpResponse.statusCode());
                        CompletableFuture completableFuture3 = this.api.handleBadResponse(httpResponse.statusCode(), httpResponse);
                        if (!completableFuture3.isDone()) {
                            CompletableFuture completableFuture4 = completableFuture3;
                            return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$processPayment(this, (int)(bl ? 1 : 0), buffer, n, n2, httpRequest, completableFuture4, httpResponse, string, null, 2, arg_0));
                        }
                        completableFuture3.join();
                    }
                }
                if (httpResponse == null || httpResponse.statusCode() == 412) continue;
                CompletableFuture completableFuture5 = VertxUtil.randomSignalSleep(this.instanceSignal, this.task.getRetryDelay());
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$processPayment(this, (int)(bl ? 1 : 0), buffer, n, n2, httpRequest, completableFuture6, httpResponse, null, null, 3, arg_0));
                }
                completableFuture5.join();
            }
            catch (Exception exception) {
                this.logger.error("Error occurred while processing payment: '{}'", (Object)exception.getMessage());
                if (!exception.getMessage().contains("Unexpected character")) continue;
                CompletableFuture completableFuture = VertxUtil.randomSleep(12000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$processPayment(this, (int)(bl ? 1 : 0), buffer, n, n2, httpRequest, completableFuture7, null, null, exception, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(-1);
    }

    public CompletableFuture checkout() {
        int n;
        this.instanceSignal = this.task.getKeywords()[0];
        try {
            n = Integer.parseInt(this.task.getKeywords()[1]);
        }
        catch (NumberFormatException numberFormatException) {
            this.logger.warn("Missing price-check (limit) for product: '{}'. Skipping...", (Object)this.instanceSignal);
            n = Integer.MAX_VALUE;
        }
        try {
            CompletableFuture completableFuture = this.getPCID();
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                PaymentInstance paymentInstance = this;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$checkout(this, n, paymentInstance, completableFuture2, Double.longBitsToDouble(0L), null, null, 0L, 1, arg_0));
            }
            this.cartInfo = (JsonObject)completableFuture.join();
        }
        catch (Throwable throwable) {
            return CompletableFuture.completedFuture(-1);
        }
        if (this.cartInfo == null) return CompletableFuture.failedFuture(new Exception("Failed to get PCID"));
        if (!this.cartInfo.containsKey("items")) return CompletableFuture.failedFuture(new Exception("Failed to get PCID"));
        double d = this.cartInfo.getJsonArray("items").getJsonObject(0).getDouble("unitPrice");
        if (d > (double)n) {
            return CompletableFuture.failedFuture(new Exception("Price exceeds limit of $" + n));
        }
        this.logger.info("Attempting to checkout");
        if (this.griefMode) {
            long l;
            if (this.griefAlt) {
                CompletableFuture completableFuture = this.processPayment();
                CompletableFuture completableFuture3 = this.api.getWebClient().windowUpdateCallback();
                if (completableFuture3 != null) {
                    this.selectShipping();
                    CompletableFuture completableFuture4 = VertxUtil.handleEagerFuture(completableFuture3);
                    if (!completableFuture4.isDone()) {
                        CompletableFuture completableFuture5 = completableFuture4;
                        return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$checkout(this, n, null, completableFuture, d, completableFuture3, completableFuture5, 0L, 2, arg_0));
                    }
                    completableFuture4.join();
                } else {
                    CompletableFuture completableFuture6 = this.selectShipping();
                    if (!completableFuture6.isDone()) {
                        CompletableFuture completableFuture7 = completableFuture6;
                        return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$checkout(this, n, null, completableFuture, d, completableFuture3, completableFuture7, 0L, 3, arg_0));
                    }
                    completableFuture6.join();
                }
                completableFuture3 = this.api.getWebClient().windowUpdateCallback();
                if (completableFuture3 != null) {
                    long l2 = Instant.now().toEpochMilli();
                    this.submitShipping();
                    CompletableFuture completableFuture8 = VertxUtil.handleEagerFuture(completableFuture3);
                    if (!completableFuture8.isDone()) {
                        CompletableFuture completableFuture9 = completableFuture8;
                        return ((CompletableFuture)completableFuture9.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$checkout(this, n, null, completableFuture, d, completableFuture3, completableFuture9, l2, 4, arg_0));
                    }
                    completableFuture8.join();
                    CompletableFuture completableFuture10 = VertxUtil.hardCodedSleep(300L - (Instant.now().toEpochMilli() - l2));
                    if (!completableFuture10.isDone()) {
                        CompletableFuture completableFuture11 = completableFuture10;
                        return ((CompletableFuture)completableFuture11.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$checkout(this, n, null, completableFuture, d, completableFuture3, completableFuture11, l2, 5, arg_0));
                    }
                    completableFuture10.join();
                } else {
                    CompletableFuture completableFuture12 = this.submitShipping();
                    if (!completableFuture12.isDone()) {
                        CompletableFuture completableFuture13 = completableFuture12;
                        return ((CompletableFuture)completableFuture13.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$checkout(this, n, null, completableFuture, d, completableFuture3, completableFuture13, 0L, 6, arg_0));
                    }
                    completableFuture12.join();
                }
                CompletableFuture completableFuture14 = this.submitPayment();
                if (!completableFuture14.isDone()) {
                    CompletableFuture completableFuture15 = completableFuture14;
                    PaymentInstance paymentInstance = this;
                    return ((CompletableFuture)completableFuture15.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$checkout(this, n, paymentInstance, completableFuture, d, completableFuture3, completableFuture15, 0L, 7, arg_0));
                }
                this.cartInfo = (JsonObject)completableFuture14.join();
                CompletableFuture completableFuture16 = completableFuture;
                if (!completableFuture16.isDone()) {
                    CompletableFuture completableFuture17 = completableFuture16;
                    return ((CompletableFuture)completableFuture17.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$checkout(this, n, null, completableFuture, d, completableFuture3, completableFuture17, 0L, 8, arg_0));
                }
                int n2 = (Integer)completableFuture16.join();
                return CompletableFuture.completedFuture(n2);
            }
            CompletableFuture completableFuture = this.api.getWebClient().windowUpdateCallback();
            if (completableFuture != null) {
                l = Instant.now().toEpochMilli();
                this.selectShipping();
                CompletableFuture completableFuture18 = VertxUtil.handleEagerFuture(completableFuture);
                if (!completableFuture18.isDone()) {
                    CompletableFuture completableFuture19 = completableFuture18;
                    return ((CompletableFuture)completableFuture19.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$checkout(this, n, null, completableFuture, d, completableFuture19, null, l, 9, arg_0));
                }
                completableFuture18.join();
                CompletableFuture completableFuture20 = VertxUtil.hardCodedSleep(50L - (Instant.now().toEpochMilli() - l));
                if (!completableFuture20.isDone()) {
                    CompletableFuture completableFuture21 = completableFuture20;
                    return ((CompletableFuture)completableFuture21.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$checkout(this, n, null, completableFuture, d, completableFuture21, null, l, 10, arg_0));
                }
                completableFuture20.join();
            } else {
                CompletableFuture completableFuture22 = this.selectShipping();
                if (!completableFuture22.isDone()) {
                    CompletableFuture completableFuture23 = completableFuture22;
                    return ((CompletableFuture)completableFuture23.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$checkout(this, n, null, completableFuture, d, completableFuture23, null, 0L, 11, arg_0));
                }
                completableFuture22.join();
            }
            completableFuture = this.api.getWebClient().windowUpdateCallback();
            if (completableFuture != null) {
                l = Instant.now().toEpochMilli();
                this.submitShipping();
                CompletableFuture completableFuture24 = VertxUtil.handleEagerFuture(completableFuture);
                if (!completableFuture24.isDone()) {
                    CompletableFuture completableFuture25 = completableFuture24;
                    return ((CompletableFuture)completableFuture25.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$checkout(this, n, null, completableFuture, d, completableFuture25, null, l, 12, arg_0));
                }
                completableFuture24.join();
                CompletableFuture completableFuture26 = VertxUtil.hardCodedSleep(300L - (Instant.now().toEpochMilli() - l));
                if (!completableFuture26.isDone()) {
                    CompletableFuture completableFuture27 = completableFuture26;
                    return ((CompletableFuture)completableFuture27.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$checkout(this, n, null, completableFuture, d, completableFuture27, null, l, 13, arg_0));
                }
                completableFuture26.join();
            } else {
                CompletableFuture completableFuture28 = this.submitShipping();
                if (!completableFuture28.isDone()) {
                    CompletableFuture completableFuture29 = completableFuture28;
                    return ((CompletableFuture)completableFuture29.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$checkout(this, n, null, completableFuture, d, completableFuture29, null, 0L, 14, arg_0));
                }
                completableFuture28.join();
            }
            CompletableFuture completableFuture30 = this.submitPayment();
            if (!completableFuture30.isDone()) {
                CompletableFuture completableFuture31 = completableFuture30;
                PaymentInstance paymentInstance = this;
                return ((CompletableFuture)completableFuture31.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$checkout(this, n, paymentInstance, completableFuture, d, completableFuture31, null, 0L, 15, arg_0));
            }
            this.cartInfo = (JsonObject)completableFuture30.join();
            CompletableFuture completableFuture32 = this.processPayment();
            if (!completableFuture32.isDone()) {
                CompletableFuture completableFuture33 = completableFuture32;
                return ((CompletableFuture)completableFuture33.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$checkout(this, n, null, completableFuture, d, completableFuture33, null, 0L, 16, arg_0));
            }
            int n3 = (Integer)completableFuture32.join();
            return CompletableFuture.completedFuture(n3);
        }
        CompletableFuture completableFuture = this.selectShipping();
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture34 = completableFuture;
            PaymentInstance paymentInstance = this;
            return ((CompletableFuture)completableFuture34.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$checkout(this, n, paymentInstance, completableFuture34, d, null, null, 0L, 17, arg_0));
        }
        this.cartInfo = (JsonObject)completableFuture.join();
        CompletableFuture completableFuture35 = this.submitShipping();
        if (!completableFuture35.isDone()) {
            CompletableFuture completableFuture36 = completableFuture35;
            PaymentInstance paymentInstance = this;
            return ((CompletableFuture)completableFuture36.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$checkout(this, n, paymentInstance, completableFuture36, d, null, null, 0L, 18, arg_0));
        }
        this.cartInfo = (JsonObject)completableFuture35.join();
        CompletableFuture completableFuture37 = this.submitPayment();
        if (!completableFuture37.isDone()) {
            CompletableFuture completableFuture38 = completableFuture37;
            PaymentInstance paymentInstance = this;
            return ((CompletableFuture)completableFuture38.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$checkout(this, n, paymentInstance, completableFuture38, d, null, null, 0L, 19, arg_0));
        }
        this.cartInfo = (JsonObject)completableFuture37.join();
        CompletableFuture completableFuture39 = this.processPayment();
        if (!completableFuture39.isDone()) {
            CompletableFuture completableFuture40 = completableFuture39;
            return ((CompletableFuture)completableFuture40.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$checkout(this, n, null, completableFuture40, d, null, null, 0L, 20, arg_0));
        }
        int n4 = (Integer)completableFuture39.join();
        return CompletableFuture.completedFuture(n4);
    }

    public static CompletableFuture preload(Walmart walmart, Task task, PaymentToken paymentToken, Mode mode) {
        return PaymentInstance.get(walmart, task, paymentToken, mode, false).preload();
    }

    public CompletableFuture submitPayment() {
        Buffer buffer = this.api.getPaymentForm(this.token).toBuffer();
        while (this.api.getWebClient().isActive()) {
            HttpRequest httpRequest = this.api.submitPayment();
            this.logger.info("Submitting payment");
            try {
                CompletableFuture completableFuture = Request.send(httpRequest, buffer);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$submitPayment(this, buffer, httpRequest, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse != null) {
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug("submitPayment responded with: [{}] {}", (Object)httpResponse.statusCode(), (Object)httpResponse.bodyAsString());
                    }
                    if (httpResponse.statusCode() == 200) {
                        return CompletableFuture.completedFuture(httpResponse.bodyAsJsonObject());
                    }
                    this.logger.warn("Failed to submit payment: status'{}'", (Object)httpResponse.statusCode());
                    CompletableFuture completableFuture3 = this.api.handleBadResponse(httpResponse.statusCode(), httpResponse);
                    if (!completableFuture3.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture3;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$submitPayment(this, buffer, httpRequest, completableFuture4, httpResponse, null, 2, arg_0));
                    }
                    completableFuture3.join();
                }
                if (this.griefMode || httpResponse == null || httpResponse.statusCode() == 412 || httpResponse.statusCode() == 307) continue;
                CompletableFuture completableFuture5 = VertxUtil.randomSleep(this.task.getRetryDelay());
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$submitPayment(this, buffer, httpRequest, completableFuture6, httpResponse, null, 3, arg_0));
                }
                completableFuture5.join();
            }
            catch (Exception exception) {
                this.logger.error("Error occurred submitting payment: '{}'", (Object)exception.getMessage());
                if (!exception.getMessage().contains("Unexpected character")) continue;
                CompletableFuture completableFuture = VertxUtil.randomSleep(12000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$submitPayment(this, buffer, httpRequest, completableFuture7, null, exception, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public PaymentInstance(Walmart walmart, Task task, PaymentToken paymentToken, Mode mode, boolean bl) {
        this.parent = walmart;
        this.api = (API)walmart.getClient();
        this.logger = walmart.getLogger();
        this.task = task;
        this.token = paymentToken;
        this.isCheckout = bl;
        this.mode = mode;
        if (this.mode == Mode.DESKTOP) {
            this.griefAlt = false;
            this.griefMode = false;
            return;
        }
        this.griefMode = this.task.getMode().toLowerCase().contains("grief");
        this.griefAlt = this.task.getMode().toLowerCase().contains("griefalt");
    }

    public CompletableFuture getPCID() {
        int n = 0;
        Buffer buffer = this.api.PCIDForm().toBuffer();
        this.logger.info("Generating checkout step #1");
        while (this.api.getWebClient().isActive()) {
            if (n > 50) return CompletableFuture.completedFuture(null);
            HttpRequest httpRequest = this.api.getPCID(this.token);
            try {
                CompletableFuture completableFuture = Request.send(httpRequest, buffer);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$getPCID(this, n, buffer, httpRequest, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse != null) {
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug("PCID responded with: [{}] {}", (Object)httpResponse.statusCode(), (Object)httpResponse.bodyAsString());
                    }
                    if (httpResponse.statusCode() == 201) {
                        if (!this.isCheckout) return CompletableFuture.completedFuture(httpResponse.bodyAsJsonObject());
                        VertxUtil.sendSignal(this.instanceSignal);
                        return CompletableFuture.completedFuture(httpResponse.bodyAsJsonObject());
                    }
                    if (httpResponse.statusCode() == 405) {
                        throw new Throwable("405 error. Restarting session...");
                    }
                    this.logger.warn("Failed to generate checkout step #1: status:'{}'", (Object)httpResponse.statusCode());
                    if (!this.isCheckout) return CompletableFuture.completedFuture(null);
                    if (httpResponse.bodyAsString().contains("cart_empty")) return CompletableFuture.completedFuture(null);
                    if (n++ > 5) return CompletableFuture.completedFuture(null);
                    CompletableFuture completableFuture3 = this.api.handleBadResponse(httpResponse.statusCode(), httpResponse);
                    if (!completableFuture3.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture3;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$getPCID(this, n, buffer, httpRequest, completableFuture4, httpResponse, null, 2, arg_0));
                    }
                    completableFuture3.join();
                    if (httpResponse.statusCode() == 412 || httpResponse.statusCode() == 307) continue;
                    CompletableFuture completableFuture5 = VertxUtil.randomSignalSleep(this.instanceSignal, this.task.getRetryDelay());
                    if (!completableFuture5.isDone()) {
                        CompletableFuture completableFuture6 = completableFuture5;
                        return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$getPCID(this, n, buffer, httpRequest, completableFuture6, httpResponse, null, 3, arg_0));
                    }
                    completableFuture5.join();
                    continue;
                }
                ++n;
            }
            catch (Exception exception) {
                if (!this.isCheckout) return CompletableFuture.completedFuture(null);
                this.logger.error("Error occurred on checkout step #1: '{}'", (Object)exception.getMessage());
                if (!exception.getMessage().contains("Unexpected character") && !(exception instanceof DecodeException)) continue;
                CompletableFuture completableFuture = VertxUtil.randomSleep(12000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$getPCID(this, n, buffer, httpRequest, completableFuture7, null, exception, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture preload() {
        block13: {
            this.logger.info("Generating...");
            this.instanceSignal = this.task.getKeywords()[0];
            try {
                CompletableFuture completableFuture = this.addToCart();
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$preload(this, completableFuture2, null, 1, arg_0));
                }
                completableFuture.join();
                CompletableFuture completableFuture3 = this.getPCID();
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    PaymentInstance paymentInstance = this;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$preload(this, completableFuture4, paymentInstance, 2, arg_0));
                }
                this.cartInfo = (JsonObject)completableFuture3.join();
                if (this.cartInfo == null || !this.cartInfo.containsKey("items")) break block13;
                try {
                    CompletableFuture completableFuture5 = this.selectShipping();
                    if (!completableFuture5.isDone()) {
                        CompletableFuture completableFuture6 = completableFuture5;
                        PaymentInstance paymentInstance = this;
                        return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$preload(this, completableFuture6, paymentInstance, 3, arg_0));
                    }
                    this.cartInfo = (JsonObject)completableFuture5.join();
                    if (this.cartInfo != null) {
                        CompletableFuture completableFuture7 = this.submitShipping();
                        if (!completableFuture7.isDone()) {
                            CompletableFuture completableFuture8 = completableFuture7;
                            PaymentInstance paymentInstance = this;
                            return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$preload(this, completableFuture8, paymentInstance, 4, arg_0));
                        }
                        this.cartInfo = (JsonObject)completableFuture7.join();
                        if (this.cartInfo != null) {
                            CompletableFuture completableFuture9 = this.submitBilling();
                            if (!completableFuture9.isDone()) {
                                CompletableFuture completableFuture10 = completableFuture9;
                                PaymentInstance paymentInstance = this;
                                return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$preload(this, completableFuture10, paymentInstance, 5, arg_0));
                            }
                            this.cartInfo = (JsonObject)completableFuture9.join();
                            if (this.cartInfo != null) {
                                if (this.cartInfo.containsKey("piHash")) {
                                    return CompletableFuture.completedFuture(this.cartInfo.getString("piHash"));
                                }
                                this.cartInfo = null;
                            }
                        }
                    }
                }
                catch (Exception exception) {
                    this.logger.error("Error while generating checkout: {}", (Object)exception.getMessage());
                    return CompletableFuture.completedFuture("");
                }
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
        this.logger.warn("Failed to generate checkout.");
        return CompletableFuture.completedFuture("");
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$submitShipping(PaymentInstance var0, Buffer var1_1, HttpRequest var2_2, CompletableFuture var3_3, HttpResponse var4_5, Exception var5_6, int var6_7, Object var7_8) {
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

    public CompletableFuture submitBilling() {
        Buffer buffer = this.api.getBillingForm(this.token).toBuffer();
        this.logger.info("Generating checkout step #4");
        while (this.api.getWebClient().isActive()) {
            HttpRequest httpRequest = this.api.submitBilling();
            try {
                CompletableFuture completableFuture = Request.send(httpRequest, buffer);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$submitBilling(this, buffer, httpRequest, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse != null) {
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug("submitBilling responded with: [{}] {}", (Object)httpResponse.statusCode(), (Object)httpResponse.bodyAsString());
                    }
                    if (httpResponse.statusCode() == 200) {
                        return CompletableFuture.completedFuture(httpResponse.bodyAsJsonObject());
                    }
                    this.logger.warn("Failed to generate checkout step #4: status:'{}'", (Object)httpResponse.statusCode());
                    if (!this.isCheckout) {
                        return CompletableFuture.completedFuture(null);
                    }
                    CompletableFuture completableFuture3 = this.api.handleBadResponse(httpResponse.statusCode(), httpResponse);
                    if (!completableFuture3.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture3;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$submitBilling(this, buffer, httpRequest, completableFuture4, httpResponse, null, 2, arg_0));
                    }
                    completableFuture3.join();
                }
                if (this.griefMode || httpResponse == null || httpResponse.statusCode() == 412 || httpResponse.statusCode() == 307) continue;
                CompletableFuture completableFuture5 = VertxUtil.randomSleep(this.task.getRetryDelay());
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$submitBilling(this, buffer, httpRequest, completableFuture6, httpResponse, null, 3, arg_0));
                }
                completableFuture5.join();
            }
            catch (Exception exception) {
                this.logger.error("Error occurred on checkout step #4: '{}'", (Object)exception.getMessage());
                if (!this.isCheckout) return CompletableFuture.completedFuture(null);
                if (!exception.getMessage().contains("Unexpected character") && !(exception instanceof DecodeException)) continue;
                CompletableFuture completableFuture = VertxUtil.randomSleep(12000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$submitBilling(this, buffer, httpRequest, completableFuture7, null, exception, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture selectShipping() {
        Buffer buffer = this.api.getShippingRateForm(this.cartInfo).toBuffer();
        this.logger.info("Generating checkout step #2");
        while (this.api.getWebClient().isActive()) {
            HttpRequest httpRequest = this.api.selectShipping();
            try {
                CompletableFuture completableFuture = Request.send(httpRequest, buffer);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$selectShipping(this, buffer, httpRequest, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse != null) {
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug("selectShipping responded with: [{}] {}", (Object)httpResponse.statusCode(), (Object)httpResponse.bodyAsString());
                    }
                    if (httpResponse.statusCode() == 200) {
                        return CompletableFuture.completedFuture(httpResponse.bodyAsJsonObject());
                    }
                    this.logger.warn("Failed to generate checkout step #2: status:'{}'", (Object)httpResponse.statusCode());
                    if (!this.isCheckout) {
                        return CompletableFuture.completedFuture(null);
                    }
                    CompletableFuture completableFuture3 = this.api.handleBadResponse(httpResponse.statusCode(), httpResponse);
                    if (!completableFuture3.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture3;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$selectShipping(this, buffer, httpRequest, completableFuture4, httpResponse, null, 2, arg_0));
                    }
                    completableFuture3.join();
                }
                if (this.griefMode || httpResponse == null || httpResponse.statusCode() == 412 || httpResponse.statusCode() == 307) continue;
                CompletableFuture completableFuture5 = VertxUtil.randomSleep(this.task.getRetryDelay());
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$selectShipping(this, buffer, httpRequest, completableFuture6, httpResponse, null, 3, arg_0));
                }
                completableFuture5.join();
            }
            catch (Exception exception) {
                this.logger.error("Error occurred on checkout step #2: '{}'", (Object)exception.getMessage());
                if (!this.isCheckout) return CompletableFuture.completedFuture(null);
                if (!exception.getMessage().contains("Unexpected character") && !(exception instanceof DecodeException)) continue;
                CompletableFuture completableFuture = VertxUtil.randomSleep(12000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$selectShipping(this, buffer, httpRequest, completableFuture7, null, exception, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$submitBilling(PaymentInstance var0, Buffer var1_1, HttpRequest var2_2, CompletableFuture var3_3, HttpResponse var4_5, Exception var5_6, int var6_7, Object var7_8) {
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

    public CompletableFuture submitPayment(boolean bl) {
        Buffer buffer = this.api.getPaymentForm(this.token).toBuffer();
        while (this.api.getWebClient().isActive()) {
            HttpRequest httpRequest = this.api.submitPayment();
            this.logger.info("Submitting payment");
            try {
                CompletableFuture completableFuture = Request.send(httpRequest, buffer);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$submitPayment$1(this, (int)(bl ? 1 : 0), buffer, httpRequest, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse != null) {
                    if (httpResponse.statusCode() == 200) {
                        CompletableFuture completableFuture3 = this.processPayment(bl);
                        if (completableFuture3.isDone()) return CompletableFuture.completedFuture((Integer)completableFuture3.join());
                        CompletableFuture completableFuture4 = completableFuture3;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$submitPayment$1(this, (int)(bl ? 1 : 0), buffer, httpRequest, completableFuture4, httpResponse, null, 2, arg_0));
                    }
                    this.logger.warn("Failed to submit payment: status'{}'", (Object)httpResponse.statusCode());
                    CompletableFuture completableFuture5 = this.api.handleBadResponse(httpResponse.statusCode(), httpResponse);
                    if (!completableFuture5.isDone()) {
                        CompletableFuture completableFuture6 = completableFuture5;
                        return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$submitPayment$1(this, (int)(bl ? 1 : 0), buffer, httpRequest, completableFuture6, httpResponse, null, 3, arg_0));
                    }
                    completableFuture5.join();
                }
                if (this.griefMode || httpResponse == null || httpResponse.statusCode() == 412 || httpResponse.statusCode() == 307) continue;
                CompletableFuture completableFuture7 = VertxUtil.randomSleep(this.task.getRetryDelay());
                if (!completableFuture7.isDone()) {
                    CompletableFuture completableFuture8 = completableFuture7;
                    return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$submitPayment$1(this, (int)(bl ? 1 : 0), buffer, httpRequest, completableFuture8, httpResponse, null, 4, arg_0));
                }
                completableFuture7.join();
            }
            catch (Exception exception) {
                this.logger.error("Error occurred submitting payment: '{}'", (Object)exception.getMessage());
                if (!exception.getMessage().contains("Unexpected character")) continue;
                CompletableFuture completableFuture = VertxUtil.randomSleep(12000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture9 = completableFuture;
                    return ((CompletableFuture)completableFuture9.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$submitPayment$1(this, (int)(bl ? 1 : 0), buffer, httpRequest, completableFuture9, null, exception, 5, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(-1);
    }

    public CompletableFuture addToCart() {
        Buffer buffer = this.api.getAtcForm("47CDCD2F7ED24EC2BA9F74BEAE3C151B", 1).put("unitPrice", (Object)5).toBuffer();
        int n = 0;
        while (this.api.getWebClient().isActive()) {
            if (n >= 3) return CompletableFuture.failedFuture(new Exception("Failed to cart preload"));
            HttpRequest httpRequest = this.api.addToCart();
            try {
                CompletableFuture completableFuture = Request.send(httpRequest, buffer);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$addToCart(this, buffer, n, httpRequest, completableFuture2, null, 0, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 201) return CompletableFuture.completedFuture(null);
                if (httpResponse.statusCode() == 200) return CompletableFuture.completedFuture(null);
                if (httpResponse.statusCode() == 206) {
                    return CompletableFuture.completedFuture(null);
                }
                ++n;
                CompletableFuture completableFuture3 = this.api.handleBadResponse(httpResponse.statusCode(), httpResponse);
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$addToCart(this, buffer, n, httpRequest, completableFuture4, httpResponse, 0, null, 2, arg_0));
                }
                int n2 = ((Boolean)completableFuture3.join()).booleanValue() ? 1 : 0;
                if (n2 != 0) continue;
                CompletableFuture completableFuture5 = VertxUtil.randomSleep(this.task.getMonitorDelay());
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$addToCart(this, buffer, n, httpRequest, completableFuture6, httpResponse, n2, null, 3, arg_0));
                }
                completableFuture5.join();
            }
            catch (Exception exception) {
                this.logger.error("Error occurred on preload: '{}'", (Object)exception.getMessage());
                CompletableFuture completableFuture = VertxUtil.randomSleep(12000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> PaymentInstance.async$addToCart(this, buffer, n, httpRequest, completableFuture7, null, 0, exception, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.failedFuture(new Exception("Failed to cart preload"));
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$processPayment(PaymentInstance var0, int var1_1, Buffer var2_2, int var3_3, int var4_4, HttpRequest var5_5, CompletableFuture var6_6, HttpResponse var7_8, String var8_9, Exception var9_10, int var10_11, Object var11_12) {
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

    public CompletableFuture processPayment() {
        return this.processPayment(true);
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

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$preload(PaymentInstance var0, CompletableFuture var1_1, PaymentInstance var2_4, int var3_5, Object var4_10) {
        switch (var3_5) {
            case 0: {
                var0.logger.info("Generating...");
                var0.instanceSignal = var0.task.getKeywords()[0];
                v0 = var0.addToCart();
                if (!v0.isDone()) {
                    var2_4 = v0;
                    return var2_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preload(io.trickle.task.sites.walmart.usa.PaymentInstance java.util.concurrent.CompletableFuture io.trickle.task.sites.walmart.usa.PaymentInstance int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PaymentInstance)var0, (CompletableFuture)var2_4, null, (int)1));
                }
lbl10:
                // 3 sources

                while (true) {
                    v0.join();
                    v1 = var0;
                    v2 = var0.getPCID();
                    if (!v2.isDone()) {
                        var3_6 = v2;
                        var2_4 = v1;
                        return var3_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preload(io.trickle.task.sites.walmart.usa.PaymentInstance java.util.concurrent.CompletableFuture io.trickle.task.sites.walmart.usa.PaymentInstance int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PaymentInstance)var0, (CompletableFuture)var3_6, (PaymentInstance)var2_4, (int)2));
                    }
lbl19:
                    // 3 sources

                    while (true) {
                        v1.cartInfo = (JsonObject)v2.join();
                        if (var0.cartInfo == null || !var0.cartInfo.containsKey("items")) ** GOTO lbl62
                        try {
                            v3 = var0;
                            v4 = var0.selectShipping();
                            if (!v4.isDone()) {
                                var3_7 = v4;
                                var2_4 = v3;
                                return var3_7.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preload(io.trickle.task.sites.walmart.usa.PaymentInstance java.util.concurrent.CompletableFuture io.trickle.task.sites.walmart.usa.PaymentInstance int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PaymentInstance)var0, (CompletableFuture)var3_7, (PaymentInstance)var2_4, (int)3));
                            }
lbl29:
                            // 3 sources

                            while (true) {
                                v3.cartInfo = (JsonObject)v4.join();
                                if (var0.cartInfo != null) {
                                    v5 = var0;
                                    v6 = var0.submitShipping();
                                    if (!v6.isDone()) {
                                        var3_8 = v6;
                                        var2_4 = v5;
                                        return var3_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preload(io.trickle.task.sites.walmart.usa.PaymentInstance java.util.concurrent.CompletableFuture io.trickle.task.sites.walmart.usa.PaymentInstance int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PaymentInstance)var0, (CompletableFuture)var3_8, (PaymentInstance)var2_4, (int)4));
                                    }
lbl38:
                                    // 3 sources

                                    while (true) {
                                        v5.cartInfo = (JsonObject)v6.join();
                                        if (var0.cartInfo != null) {
                                            v7 = var0;
                                            v8 = var0.submitBilling();
                                            if (!v8.isDone()) {
                                                var3_9 = v8;
                                                var2_4 = v7;
                                                return var3_9.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preload(io.trickle.task.sites.walmart.usa.PaymentInstance java.util.concurrent.CompletableFuture io.trickle.task.sites.walmart.usa.PaymentInstance int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PaymentInstance)var0, (CompletableFuture)var3_9, (PaymentInstance)var2_4, (int)5));
                                            }
lbl47:
                                            // 3 sources

                                            while (true) {
                                                v7.cartInfo = (JsonObject)v8.join();
                                                if (var0.cartInfo != null) {
                                                    if (var0.cartInfo.containsKey("piHash")) {
                                                        return CompletableFuture.completedFuture(var0.cartInfo.getString("piHash"));
                                                    }
                                                    var0.cartInfo = null;
                                                }
                                                ** GOTO lbl62
                                                break;
                                            }
                                        }
                                        ** GOTO lbl62
                                        break;
                                    }
                                }
                                ** GOTO lbl62
                                break;
                            }
                        }
                        catch (Exception var1_2) {
                            var0.logger.error("Error while generating checkout: {}", (Object)var1_2.getMessage());
                            return CompletableFuture.completedFuture("");
                        }
                        break;
                    }
                    break;
                }
            }
            catch (Throwable var1_3) {
                // empty catch block
            }
lbl62:
            // 5 sources

            var0.logger.warn("Failed to generate checkout.");
            return CompletableFuture.completedFuture("");
            case 1: {
                v0 = var1_1;
                ** continue;
            }
            case 2: {
                v1 = var2_4;
                v2 = var1_1;
                ** continue;
            }
            case 3: {
                v3 = var2_4;
                v4 = var1_1;
                ** continue;
            }
            case 4: {
                v5 = var2_4;
                v6 = var1_1;
                ** continue;
            }
            case 5: {
                v7 = var2_4;
                v8 = var1_1;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }
}

