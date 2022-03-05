/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.json.JsonArray
 *  io.vertx.core.json.JsonObject
 *  org.apache.logging.log4j.Logger
 */
package io.trickle.task.sites.shopify;

import io.trickle.task.Task;
import io.trickle.task.sites.Site;
import io.trickle.task.sites.shopify.Presolver;
import io.trickle.task.sites.shopify.Shopify;
import io.trickle.task.sites.shopify.util.ShippingRateSupplier;
import io.trickle.task.sites.shopify.util.SiteParser;
import io.trickle.task.sites.shopify.util.Triplet;
import io.trickle.task.sites.shopify.util.VariantHandler;
import io.trickle.util.analytics.Analytics;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.lang.invoke.LambdaMetafactory;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import org.apache.logging.log4j.Logger;

public class ShopifySafe
extends Shopify {
    public boolean isContactpreload = false;
    public boolean fastPreload;
    public boolean isGraph;
    public boolean isPreload = this.task.getMode().contains("preload");
    public String precartItemName = "123456789";

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    public static CompletableFuture async$preloadContactOnly(ShopifySafe var0, CompletableFuture var1_1, JsonArray var2_2, Triplet var3_3, String var4_4, String var5_5, int var6_6, Object var7_7) {
        switch (var6_6) {
            case 0: {
                v0 = var0.fetchProductsJSON(false);
                if (!v0.isDone()) {
                    var5_5 = v0;
                    return var5_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preloadContactOnly(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray io.trickle.task.sites.shopify.util.Triplet java.lang.String java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var5_5, null, null, null, null, (int)1));
                }
                ** GOTO lbl10
            }
            case 1: {
                v0 = var1_1;
lbl10:
                // 2 sources

                if (!(v1 = VariantHandler.findPrecartVariant((JsonArray)(var1_1 = ((JsonObject)v0.join()).getJsonArray("products")))).isDone()) {
                    var5_5 = v1;
                    return var5_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preloadContactOnly(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray io.trickle.task.sites.shopify.util.Triplet java.lang.String java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var5_5, (JsonArray)var1_1, null, null, null, (int)2));
                }
                ** GOTO lbl17
            }
            case 2: {
                v1 = var1_1;
                var1_1 = var2_2 /* !! */ ;
lbl17:
                // 2 sources

                if ((var2_2 /* !! */  = (Triplet)v1.join()) == null) {
                    var0.isContactpreload = false;
                    var0.logger.error("There is no item to preload. Handling...");
                    return CompletableFuture.completedFuture(null);
                }
                var0.shippingRate = new CompletableFuture<T>();
                var0.precartItemName = var3_3 = (String)var2_2 /* !! */ .first;
                if (var0.task.getSite() != Site.KITH) ** GOTO lbl29
                v2 = var0.atcBasic((String)var3_3);
                if (!v2.isDone()) {
                    var5_5 = v2;
                    return var5_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preloadContactOnly(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray io.trickle.task.sites.shopify.util.Triplet java.lang.String java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var5_5, (JsonArray)var1_1, (Triplet)var2_2 /* !! */ , (String)var3_3, null, (int)3));
                }
                ** GOTO lbl41
lbl29:
                // 1 sources

                v3 = var0.atcAJAX((String)var3_3);
                if (!v3.isDone()) {
                    var5_5 = v3;
                    return var5_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preloadContactOnly(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray io.trickle.task.sites.shopify.util.Triplet java.lang.String java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var5_5, (JsonArray)var1_1, (Triplet)var2_2 /* !! */ , (String)var3_3, null, (int)4));
                }
                ** GOTO lbl54
            }
            case 3: {
                v2 = var1_1;
                v4 = var2_2 /* !! */ ;
                v5 = var3_3;
                var3_3 = var4_4;
                var2_2 /* !! */  = v5;
                var1_1 = v4;
lbl41:
                // 2 sources

                v2.join();
lbl43:
                // 2 sources

                while (!(v6 = var0.genCheckoutURL(false)).isDone()) {
                    var5_5 = v6;
                    return var5_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preloadContactOnly(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray io.trickle.task.sites.shopify.util.Triplet java.lang.String java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var5_5, (JsonArray)var1_1, (Triplet)var2_2 /* !! */ , (String)var3_3, null, (int)5));
                }
                ** GOTO lbl64
            }
            case 4: {
                v3 = var1_1;
                v7 = var2_2 /* !! */ ;
                v8 = var3_3;
                var3_3 = var4_4;
                var2_2 /* !! */  = v8;
                var1_1 = v7;
lbl54:
                // 2 sources

                v3.join();
                ** GOTO lbl43
            }
            case 5: {
                v6 = var1_1;
                v9 = var2_2 /* !! */ ;
                v10 = var3_3;
                var3_3 = var4_4;
                var2_2 /* !! */  = v10;
                var1_1 = v9;
lbl64:
                // 2 sources

                if (!(v11 = var0.handlePreload(var4_4 = (String)v6.join())).isDone()) {
                    var5_5 = v11;
                    return var5_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preloadContactOnly(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray io.trickle.task.sites.shopify.util.Triplet java.lang.String java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var5_5, (JsonArray)var1_1, (Triplet)var2_2 /* !! */ , (String)var3_3, (String)var4_4, (int)6));
                }
                ** GOTO lbl77
            }
            case 6: {
                v11 = var1_1;
                v12 = var2_2 /* !! */ ;
                v13 = var3_3;
                v14 = var4_4;
                var4_4 = var5_5;
                var3_3 = v14;
                var2_2 /* !! */  = v13;
                var1_1 = v12;
lbl77:
                // 2 sources

                var4_4 = (String)v11.join();
                if (((Boolean)var2_2 /* !! */ .second).booleanValue()) ** GOTO lbl82
                var0.api.setOOS();
                var0.isContactpreload = false;
                ** GOTO lbl131
lbl82:
                // 1 sources

                v15 = var0.getCheckoutURL(var4_4);
                if (!v15.isDone()) {
                    var5_5 = v15;
                    return var5_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preloadContactOnly(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray io.trickle.task.sites.shopify.util.Triplet java.lang.String java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var5_5, (JsonArray)var1_1, (Triplet)var2_2 /* !! */ , (String)var3_3, (String)var4_4, (int)7));
                }
                ** GOTO lbl96
            }
            case 7: {
                v15 = var1_1;
                v16 = var2_2 /* !! */ ;
                v17 = var3_3;
                v18 = var4_4;
                var4_4 = var5_5;
                var3_3 = v18;
                var2_2 /* !! */  = v17;
                var1_1 = v16;
lbl96:
                // 2 sources

                v15.join();
                if (!var0.api.getCookies().contains("_shopify_checkpoint")) ** GOTO lbl115
                v19 = var0.checkCaptcha(var4_4);
                if (!v19.isDone()) {
                    var5_5 = v19;
                    return var5_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preloadContactOnly(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray io.trickle.task.sites.shopify.util.Triplet java.lang.String java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var5_5, (JsonArray)var1_1, (Triplet)var2_2 /* !! */ , (String)var3_3, (String)var4_4, (int)8));
                }
                ** GOTO lbl113
            }
            case 8: {
                v19 = var1_1;
                v20 = var2_2 /* !! */ ;
                v21 = var3_3;
                v22 = var4_4;
                var4_4 = var5_5;
                var3_3 = v22;
                var2_2 /* !! */  = v21;
                var1_1 = v20;
lbl113:
                // 2 sources

                v19.join();
lbl115:
                // 2 sources

                if (!(v23 = var0.submitContact(var4_4)).isDone()) {
                    var5_5 = v23;
                    return var5_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preloadContactOnly(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray io.trickle.task.sites.shopify.util.Triplet java.lang.String java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var5_5, (JsonArray)var1_1, (Triplet)var2_2 /* !! */ , (String)var3_3, (String)var4_4, (int)9));
                }
                ** GOTO lbl128
            }
            case 9: {
                v23 = var1_1;
                v24 = var2_2 /* !! */ ;
                v25 = var3_3;
                v26 = var4_4;
                var4_4 = var5_5;
                var3_3 = v26;
                var2_2 /* !! */  = v25;
                var1_1 = v24;
lbl128:
                // 2 sources

                v23.join();
                var0.isContactpreload = true;
lbl131:
                // 2 sources

                if (!(v27 = var0.clearWithChangeJS()).isDone()) {
                    var5_5 = v27;
                    return var5_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preloadContactOnly(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray io.trickle.task.sites.shopify.util.Triplet java.lang.String java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var5_5, (JsonArray)var1_1, (Triplet)var2_2 /* !! */ , (String)var3_3, (String)var4_4, (int)10));
                }
                ** GOTO lbl144
            }
            case 10: {
                v27 = var1_1;
                v28 = var2_2 /* !! */ ;
                v29 = var3_3;
                v30 = var4_4;
                var4_4 = var5_5;
                var3_3 = v30;
                var2_2 /* !! */  = v29;
                var1_1 = v28;
lbl144:
                // 2 sources

                v27.join();
                v31 = var0.checkCart();
                if (!v31.isDone()) {
                    var5_5 = v31;
                    return var5_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preloadContactOnly(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray io.trickle.task.sites.shopify.util.Triplet java.lang.String java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var5_5, (JsonArray)var1_1, (Triplet)var2_2 /* !! */ , (String)var3_3, (String)var4_4, (int)11));
                }
                ** GOTO lbl160
            }
            case 11: {
                v31 = var1_1;
                v32 = var2_2 /* !! */ ;
                v33 = var3_3;
                v34 = var4_4;
                var4_4 = var5_5;
                var3_3 = v34;
                var2_2 /* !! */  = v33;
                var1_1 = v32;
lbl160:
                // 2 sources

                v31.join();
                v35 = var0.confirmClear(var4_4);
                if (!v35.isDone()) {
                    var5_5 = v35;
                    return var5_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preloadContactOnly(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray io.trickle.task.sites.shopify.util.Triplet java.lang.String java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var5_5, (JsonArray)var1_1, (Triplet)var2_2 /* !! */ , (String)var3_3, (String)var4_4, (int)12));
                }
                ** GOTO lbl176
            }
            case 12: {
                v35 = var1_1;
                v36 = var2_2 /* !! */ ;
                v37 = var3_3;
                v38 = var4_4;
                var4_4 = var5_5;
                var3_3 = v38;
                var2_2 /* !! */  = v37;
                var1_1 = v36;
lbl176:
                // 2 sources

                v35.join();
                var0.api.checkIsOOS();
                var0.configureShippingRate();
                return CompletableFuture.completedFuture(var4_4);
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture preloadContactOnly() {
        String string;
        CompletableFuture completableFuture = this.fetchProductsJSON(false);
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preloadContactOnly(this, completableFuture2, null, null, null, null, 1, arg_0));
        }
        JsonArray jsonArray = ((JsonObject)completableFuture.join()).getJsonArray("products");
        CompletableFuture completableFuture3 = VariantHandler.findPrecartVariant(jsonArray);
        if (!completableFuture3.isDone()) {
            CompletableFuture completableFuture4 = completableFuture3;
            return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preloadContactOnly(this, completableFuture4, jsonArray, null, null, null, 2, arg_0));
        }
        Triplet triplet = (Triplet)completableFuture3.join();
        if (triplet == null) {
            this.isContactpreload = false;
            this.logger.error("There is no item to preload. Handling...");
            return CompletableFuture.completedFuture(null);
        }
        this.shippingRate = new CompletableFuture();
        this.precartItemName = string = (String)triplet.first;
        if (this.task.getSite() == Site.KITH) {
            CompletableFuture completableFuture5 = this.atcBasic(string);
            if (!completableFuture5.isDone()) {
                CompletableFuture completableFuture6 = completableFuture5;
                return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preloadContactOnly(this, completableFuture6, jsonArray, triplet, string, null, 3, arg_0));
            }
            completableFuture5.join();
        } else {
            CompletableFuture completableFuture7 = this.atcAJAX(string);
            if (!completableFuture7.isDone()) {
                CompletableFuture completableFuture8 = completableFuture7;
                return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preloadContactOnly(this, completableFuture8, jsonArray, triplet, string, null, 4, arg_0));
            }
            completableFuture7.join();
        }
        CompletableFuture completableFuture9 = this.genCheckoutURL(false);
        if (!completableFuture9.isDone()) {
            CompletableFuture completableFuture10 = completableFuture9;
            return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preloadContactOnly(this, completableFuture10, jsonArray, triplet, string, null, 5, arg_0));
        }
        String string2 = (String)completableFuture9.join();
        CompletableFuture completableFuture11 = this.handlePreload(string2);
        if (!completableFuture11.isDone()) {
            CompletableFuture completableFuture12 = completableFuture11;
            return ((CompletableFuture)completableFuture12.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preloadContactOnly(this, completableFuture12, jsonArray, triplet, string, string2, 6, arg_0));
        }
        string2 = (String)completableFuture11.join();
        if (!((Boolean)triplet.second).booleanValue()) {
            this.api.setOOS();
            this.isContactpreload = false;
        } else {
            CompletableFuture completableFuture13 = this.getCheckoutURL(string2);
            if (!completableFuture13.isDone()) {
                CompletableFuture completableFuture14 = completableFuture13;
                return ((CompletableFuture)completableFuture14.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preloadContactOnly(this, completableFuture14, jsonArray, triplet, string, string2, 7, arg_0));
            }
            completableFuture13.join();
            if (this.api.getCookies().contains("_shopify_checkpoint")) {
                CompletableFuture completableFuture15 = this.checkCaptcha(string2);
                if (!completableFuture15.isDone()) {
                    CompletableFuture completableFuture16 = completableFuture15;
                    return ((CompletableFuture)completableFuture16.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preloadContactOnly(this, completableFuture16, jsonArray, triplet, string, string2, 8, arg_0));
                }
                completableFuture15.join();
            }
            CompletableFuture completableFuture17 = this.submitContact(string2);
            if (!completableFuture17.isDone()) {
                CompletableFuture completableFuture18 = completableFuture17;
                return ((CompletableFuture)completableFuture18.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preloadContactOnly(this, completableFuture18, jsonArray, triplet, string, string2, 9, arg_0));
            }
            completableFuture17.join();
            this.isContactpreload = true;
        }
        CompletableFuture completableFuture19 = this.clearWithChangeJS();
        if (!completableFuture19.isDone()) {
            CompletableFuture completableFuture20 = completableFuture19;
            return ((CompletableFuture)completableFuture20.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preloadContactOnly(this, completableFuture20, jsonArray, triplet, string, string2, 10, arg_0));
        }
        completableFuture19.join();
        CompletableFuture completableFuture21 = this.checkCart();
        if (!completableFuture21.isDone()) {
            CompletableFuture completableFuture22 = completableFuture21;
            return ((CompletableFuture)completableFuture22.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preloadContactOnly(this, completableFuture22, jsonArray, triplet, string, string2, 11, arg_0));
        }
        completableFuture21.join();
        CompletableFuture completableFuture23 = this.confirmClear(string2);
        if (!completableFuture23.isDone()) {
            CompletableFuture completableFuture24 = completableFuture23;
            return ((CompletableFuture)completableFuture24.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preloadContactOnly(this, completableFuture24, jsonArray, triplet, string, string2, 12, arg_0));
        }
        completableFuture23.join();
        this.api.checkIsOOS();
        this.configureShippingRate();
        return CompletableFuture.completedFuture(string2);
    }

    public ShopifySafe(Task task, int n) {
        super(task, n);
        this.isGraph = this.task.getMode().contains("graph");
    }

    public void lambda$run$0(Long l) {
        Request.execute(this.api.getWebClient().getAbs("https://www.google.com/favicon.ico").putHeader("user-agent", this.api.UA));
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$run(ShopifySafe var0, int var1_1, CompletableFuture var2_2, String var3_3, ShopifySafe var4_6, String var5_7, JsonObject var6_8, int var7_9, Object var8_11) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [81[DOLOOP]], but top level block is 83[UNCONDITIONALDOLOOP]
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
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    public static CompletableFuture async$preload(ShopifySafe var0, CompletableFuture var1_1, JsonArray var2_2, String var3_3, Triplet var4_4, String var5_5, CompletableFuture var6_6, Logger var7_7, String var8_8, int var9_9, Object var10_10) {
        switch (var9_9) {
            case 0: {
                v0 = var0.fetchProductsJSON(false);
                if (!v0.isDone()) {
                    var6_6 = v0;
                    return var6_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preload(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var6_6, null, null, null, null, null, null, null, (int)1));
                }
                ** GOTO lbl10
            }
            case 1: {
                v0 = var1_1;
lbl10:
                // 2 sources

                var1_1 = ((JsonObject)v0.join()).getJsonArray("products");
                var2_2 /* !! */  = null;
lbl12:
                // 2 sources

                while (var0.api.getWebClient().isActive()) {
                    v1 = VariantHandler.findPrecartVariant((JsonArray)var1_1);
                    if (!v1.isDone()) {
                        var6_6 = v1;
                        return var6_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preload(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var6_6, (JsonArray)var1_1, (String)var2_2 /* !! */ , null, null, null, null, null, (int)2));
                    }
                    ** GOTO lbl24
                }
                ** GOTO lbl249
            }
            case 2: {
                v1 = var1_1;
                v2 = var2_2 /* !! */ ;
                var2_2 /* !! */  = var3_3;
                var1_1 = v2;
lbl24:
                // 2 sources

                if ((var3_3 = (Triplet)v1.join()) == null) {
                    var0.isPreload = false;
                    var0.logger.error("There is no item to preload. Handling...");
                    return CompletableFuture.completedFuture(null);
                }
                var0.shippingRate = new CompletableFuture<T>();
                var0.precartItemName = var4_4 = (String)var3_3.first;
                if (var0.task.getSite() != Site.KITH) ** GOTO lbl36
                v3 = var0.atcBasic((String)var4_4);
                if (!v3.isDone()) {
                    var6_6 = v3;
                    return var6_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preload(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var6_6, (JsonArray)var1_1, (String)var2_2 /* !! */ , (Triplet)var3_3, (String)var4_4, null, null, null, (int)3));
                }
                ** GOTO lbl50
lbl36:
                // 1 sources

                v4 = var0.atcAJAX((String)var4_4);
                if (!v4.isDone()) {
                    var6_6 = v4;
                    return var6_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preload(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var6_6, (JsonArray)var1_1, (String)var2_2 /* !! */ , (Triplet)var3_3, (String)var4_4, null, null, null, (int)4));
                }
                ** GOTO lbl65
            }
            case 3: {
                v3 = var1_1;
                v5 = var2_2 /* !! */ ;
                v6 = var3_3;
                v7 = var4_4;
                var4_4 = var5_5;
                var3_3 = v7;
                var2_2 /* !! */  = v6;
                var1_1 = v5;
lbl50:
                // 2 sources

                v3.join();
lbl52:
                // 2 sources

                while (!(v8 = var0.genCheckoutURLViaCart()).isDone()) {
                    var6_6 = v8;
                    return var6_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preload(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, var6_6, (JsonArray)var1_1, (String)var2_2 /* !! */ , (Triplet)var3_3, (String)var4_4, null, null, null, (int)5));
                }
                ** GOTO lbl77
            }
            case 4: {
                v4 = var1_1;
                v9 = var2_2 /* !! */ ;
                v10 = var3_3;
                v11 = var4_4;
                var4_4 = var5_5;
                var3_3 = v11;
                var2_2 /* !! */  = v10;
                var1_1 = v9;
lbl65:
                // 2 sources

                v4.join();
                ** GOTO lbl52
            }
            case 5: {
                v8 = var1_1;
                v12 = var2_2 /* !! */ ;
                v13 = var3_3;
                v14 = var4_4;
                var4_4 = var5_5;
                var3_3 = v14;
                var2_2 /* !! */  = v13;
                var1_1 = v12;
lbl77:
                // 2 sources

                if (!(v15 = var0.handlePreload((String)(var2_2 /* !! */  = (String)v8.join()))).isDone()) {
                    var6_6 = v15;
                    return var6_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preload(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var6_6, (JsonArray)var1_1, (String)var2_2 /* !! */ , (Triplet)var3_3, (String)var4_4, null, null, null, (int)6));
                }
                ** GOTO lbl90
            }
            case 6: {
                v15 = var1_1;
                v16 = var2_2 /* !! */ ;
                v17 = var3_3;
                v18 = var4_4;
                var4_4 = var5_5;
                var3_3 = v18;
                var2_2 /* !! */  = v17;
                var1_1 = v16;
lbl90:
                // 2 sources

                var2_2 /* !! */  = (String)v15.join();
                if (((Boolean)var3_3.second).booleanValue()) ** GOTO lbl94
                var0.isPreload = false;
                ** GOTO lbl249
lbl94:
                // 1 sources

                v19 = var0.getCheckoutURL((String)var2_2 /* !! */ );
                if (!v19.isDone()) {
                    var6_6 = v19;
                    return var6_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preload(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var6_6, (JsonArray)var1_1, (String)var2_2 /* !! */ , (Triplet)var3_3, (String)var4_4, null, null, null, (int)7));
                }
                ** GOTO lbl108
            }
            case 7: {
                v19 = var1_1;
                v20 = var2_2 /* !! */ ;
                v21 = var3_3;
                v22 = var4_4;
                var4_4 = var5_5;
                var3_3 = v22;
                var2_2 /* !! */  = v21;
                var1_1 = v20;
lbl108:
                // 2 sources

                v19.join();
                if (!var0.api.getCookies().contains("_shopify_checkpoint")) ** GOTO lbl127
                v23 = var0.checkCaptcha((String)var2_2 /* !! */ );
                if (!v23.isDone()) {
                    var6_6 = v23;
                    return var6_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preload(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var6_6, (JsonArray)var1_1, (String)var2_2 /* !! */ , (Triplet)var3_3, (String)var4_4, null, null, null, (int)8));
                }
                ** GOTO lbl125
            }
            case 8: {
                v23 = var1_1;
                v24 = var2_2 /* !! */ ;
                v25 = var3_3;
                v26 = var4_4;
                var4_4 = var5_5;
                var3_3 = v26;
                var2_2 /* !! */  = v25;
                var1_1 = v24;
lbl125:
                // 2 sources

                v23.join();
lbl127:
                // 2 sources

                if (!(v27 = var0.submitContact((String)var2_2 /* !! */ )).isDone()) {
                    var6_6 = v27;
                    return var6_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preload(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var6_6, (JsonArray)var1_1, (String)var2_2 /* !! */ , (Triplet)var3_3, (String)var4_4, null, null, null, (int)9));
                }
                ** GOTO lbl140
            }
            case 9: {
                v27 = var1_1;
                v28 = var2_2 /* !! */ ;
                v29 = var3_3;
                v30 = var4_4;
                var4_4 = var5_5;
                var3_3 = v30;
                var2_2 /* !! */  = v29;
                var1_1 = v28;
lbl140:
                // 2 sources

                v27.join();
                var5_5 = var0.getShippingRateOld();
                v31 = CompletableFuture.allOf(new CompletableFuture[]{var5_5, var0.getShippingPage((String)var2_2 /* !! */ )});
                if (!v31.isDone()) {
                    var6_6 = v31;
                    return var6_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preload(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var5_5, (JsonArray)var1_1, (String)var2_2 /* !! */ , (Triplet)var3_3, (String)var4_4, (CompletableFuture)var6_6, null, null, (int)10));
                }
                ** GOTO lbl159
            }
            case 10: {
                v31 = var6_6;
                v32 = var2_2 /* !! */ ;
                v33 = var3_3;
                v34 = var4_4;
                v35 = var5_5;
                var5_5 = var1_1;
                var4_4 = v35;
                var3_3 = v34;
                var2_2 /* !! */  = v33;
                var1_1 = v32;
lbl159:
                // 2 sources

                v31.join();
                v36 = var0.shippingRate;
                if (!v36.isDone()) {
                    var6_6 = v36;
                    return var6_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preload(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var5_5, (JsonArray)var1_1, (String)var2_2 /* !! */ , (Triplet)var3_3, (String)var4_4, (CompletableFuture)var6_6, null, null, (int)11));
                }
                ** GOTO lbl177
            }
            case 11: {
                v36 = var6_6;
                v37 = var2_2 /* !! */ ;
                v38 = var3_3;
                v39 = var4_4;
                v40 = var5_5;
                var5_5 = var1_1;
                var4_4 = v40;
                var3_3 = v39;
                var2_2 /* !! */  = v38;
                var1_1 = v37;
lbl177:
                // 2 sources

                if (((ShippingRateSupplier)v36.join()).get().equals(var0.task.getShippingRate()) || var0.task.getShippingRate().length() <= 3) ** GOTO lbl187
                v41 = var0.logger;
                v42 = "The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]";
                v43 = var0.shippingRate;
                if (!v43.isDone()) {
                    var8_8 = v43;
                    var7_7 /* !! */  = v42;
                    var6_6 = v41;
                    return var8_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preload(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var5_5, (JsonArray)var1_1, (String)var2_2 /* !! */ , (Triplet)var3_3, (String)var4_4, (CompletableFuture)var8_8, (Logger)var6_6, (String)var7_7 /* !! */ , (int)12));
                }
                ** GOTO lbl205
lbl187:
                // 1 sources

                v44 = var0.submitShipping((String)var2_2 /* !! */ );
                if (!v44.isDone()) {
                    var6_6 = v44;
                    return var6_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preload(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var5_5, (JsonArray)var1_1, (String)var2_2 /* !! */ , (Triplet)var3_3, (String)var4_4, (CompletableFuture)var6_6, null, null, (int)14));
                }
                ** GOTO lbl247
            }
            case 12: {
                v41 = var7_7 /* !! */ ;
                v42 = var8_8;
                v43 = var6_6;
                v45 /* !! */  = var2_2 /* !! */ ;
                v46 = var3_3;
                v47 = var4_4;
                v48 = var5_5;
                var5_5 = var1_1;
                var4_4 = v48;
                var3_3 = v47;
                var2_2 /* !! */  = v46;
                var1_1 = v45 /* !! */ ;
lbl205:
                // 2 sources

                v41.error(v42, (Object)((ShippingRateSupplier)v43.join()).get(), (Object)var0.task.getShippingRate());
                v49 = var0.clearWithChangeJS();
                if (!v49.isDone()) {
                    var6_6 = v49;
                    return var6_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preload(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var5_5, (JsonArray)var1_1, (String)var2_2 /* !! */ , (Triplet)var3_3, (String)var4_4, (CompletableFuture)var6_6, null, null, (int)13));
                }
                ** GOTO lbl222
            }
            case 13: {
                v49 = var6_6;
                v50 = var2_2 /* !! */ ;
                v51 = var3_3;
                v52 = var4_4;
                v53 = var5_5;
                var5_5 = var1_1;
                var4_4 = v53;
                var3_3 = v52;
                var2_2 /* !! */  = v51;
                var1_1 = v50;
lbl222:
                // 2 sources

                v49.join();
                var0.shippingRate = new CompletableFuture<T>();
                if (var1_1.isEmpty()) {
                    var0.logger.info("There are no items with matching shipping rate. Handling...");
                    var0.api.checkIsOOS();
                    var0.configureShippingRate();
                    var0.isPreload = false;
                    var0.isContactpreload = true;
                    return CompletableFuture.completedFuture(var2_2 /* !! */ );
                }
                var1_1.remove(var3_3.third);
                ** GOTO lbl12
            }
            case 14: {
                v44 = var6_6;
                v54 = var2_2 /* !! */ ;
                v55 = var3_3;
                v56 = var4_4;
                v57 = var5_5;
                var5_5 = var1_1;
                var4_4 = v57;
                var3_3 = v56;
                var2_2 /* !! */  = v55;
                var1_1 = v54;
lbl247:
                // 2 sources

                v44.join();
lbl249:
                // 3 sources

                if (!(v58 = var0.clearWithChangeJS()).isDone()) {
                    var6_6 = v58;
                    return var6_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preload(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var6_6, (JsonArray)var1_1, (String)var2_2 /* !! */ , null, null, null, null, null, (int)15));
                }
                ** GOTO lbl258
            }
            case 15: {
                v58 = var1_1;
                v59 = var2_2 /* !! */ ;
                var2_2 /* !! */  = var3_3;
                var1_1 = v59;
lbl258:
                // 2 sources

                v58.join();
                v60 = var0.checkCart();
                if (!v60.isDone()) {
                    var6_6 = v60;
                    return var6_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preload(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var6_6, (JsonArray)var1_1, (String)var2_2 /* !! */ , null, null, null, null, null, (int)16));
                }
                ** GOTO lbl270
            }
            case 16: {
                v60 = var1_1;
                v61 = var2_2 /* !! */ ;
                var2_2 /* !! */  = var3_3;
                var1_1 = v61;
lbl270:
                // 2 sources

                v60.join();
                var0.api.checkIsOOS();
                var0.configureShippingRate();
                return CompletableFuture.completedFuture(var2_2 /* !! */ );
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture preload() {
        CompletableFuture completableFuture = this.fetchProductsJSON(false);
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preload(this, completableFuture2, null, null, null, null, null, null, null, 1, arg_0));
        }
        JsonArray jsonArray = ((JsonObject)completableFuture.join()).getJsonArray("products");
        String string = null;
        while (this.api.getWebClient().isActive()) {
            String string2;
            CompletableFuture completableFuture3 = VariantHandler.findPrecartVariant(jsonArray);
            if (!completableFuture3.isDone()) {
                CompletableFuture completableFuture4 = completableFuture3;
                return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preload(this, completableFuture4, jsonArray, string, null, null, null, null, null, 2, arg_0));
            }
            Triplet triplet = (Triplet)completableFuture3.join();
            if (triplet == null) {
                this.isPreload = false;
                this.logger.error("There is no item to preload. Handling...");
                return CompletableFuture.completedFuture(null);
            }
            this.shippingRate = new CompletableFuture();
            this.precartItemName = string2 = (String)triplet.first;
            if (this.task.getSite() == Site.KITH) {
                CompletableFuture completableFuture5 = this.atcBasic(string2);
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preload(this, completableFuture6, jsonArray, string, triplet, string2, null, null, null, 3, arg_0));
                }
                completableFuture5.join();
            } else {
                CompletableFuture completableFuture7 = this.atcAJAX(string2);
                if (!completableFuture7.isDone()) {
                    CompletableFuture completableFuture8 = completableFuture7;
                    return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preload(this, completableFuture8, jsonArray, string, triplet, string2, null, null, null, 4, arg_0));
                }
                completableFuture7.join();
            }
            CompletableFuture completableFuture9 = this.genCheckoutURLViaCart();
            if (!completableFuture9.isDone()) {
                CompletableFuture completableFuture10 = completableFuture9;
                return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preload(this, completableFuture10, jsonArray, string, triplet, string2, null, null, null, 5, arg_0));
            }
            string = (String)completableFuture9.join();
            CompletableFuture completableFuture11 = this.handlePreload(string);
            if (!completableFuture11.isDone()) {
                CompletableFuture completableFuture12 = completableFuture11;
                return ((CompletableFuture)completableFuture12.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preload(this, completableFuture12, jsonArray, string, triplet, string2, null, null, null, 6, arg_0));
            }
            string = (String)completableFuture11.join();
            if (!((Boolean)triplet.second).booleanValue()) {
                this.isPreload = false;
                break;
            }
            CompletableFuture completableFuture13 = this.getCheckoutURL(string);
            if (!completableFuture13.isDone()) {
                CompletableFuture completableFuture14 = completableFuture13;
                return ((CompletableFuture)completableFuture14.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preload(this, completableFuture14, jsonArray, string, triplet, string2, null, null, null, 7, arg_0));
            }
            completableFuture13.join();
            if (this.api.getCookies().contains("_shopify_checkpoint")) {
                CompletableFuture completableFuture15 = this.checkCaptcha(string);
                if (!completableFuture15.isDone()) {
                    CompletableFuture completableFuture16 = completableFuture15;
                    return ((CompletableFuture)completableFuture16.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preload(this, completableFuture16, jsonArray, string, triplet, string2, null, null, null, 8, arg_0));
                }
                completableFuture15.join();
            }
            CompletableFuture completableFuture17 = this.submitContact(string);
            if (!completableFuture17.isDone()) {
                CompletableFuture completableFuture18 = completableFuture17;
                return ((CompletableFuture)completableFuture18.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preload(this, completableFuture18, jsonArray, string, triplet, string2, null, null, null, 9, arg_0));
            }
            completableFuture17.join();
            CompletableFuture completableFuture19 = this.getShippingRateOld();
            CompletableFuture<Void> completableFuture20 = CompletableFuture.allOf(completableFuture19, this.getShippingPage(string));
            if (!completableFuture20.isDone()) {
                CompletableFuture<Void> completableFuture21 = completableFuture20;
                return ((CompletableFuture)completableFuture21.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preload(this, completableFuture19, jsonArray, string, triplet, string2, completableFuture21, null, null, 10, arg_0));
            }
            completableFuture20.join();
            CompletableFuture completableFuture22 = this.shippingRate;
            if (!completableFuture22.isDone()) {
                CompletableFuture completableFuture23 = completableFuture22;
                return ((CompletableFuture)completableFuture23.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preload(this, completableFuture19, jsonArray, string, triplet, string2, completableFuture23, null, null, 11, arg_0));
            }
            if (!((ShippingRateSupplier)completableFuture22.join()).get().equals(this.task.getShippingRate()) && this.task.getShippingRate().length() > 3) {
                Logger logger = this.logger;
                CompletableFuture completableFuture24 = this.shippingRate;
                if (!completableFuture24.isDone()) {
                    CompletableFuture completableFuture25 = completableFuture24;
                    String string3 = "The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]";
                    Logger logger2 = logger;
                    return ((CompletableFuture)completableFuture25.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preload(this, completableFuture19, jsonArray, string, triplet, string2, completableFuture25, logger2, string3, 12, arg_0));
                }
                logger.error("The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]", (Object)((ShippingRateSupplier)completableFuture24.join()).get(), (Object)this.task.getShippingRate());
                CompletableFuture completableFuture26 = this.clearWithChangeJS();
                if (!completableFuture26.isDone()) {
                    CompletableFuture completableFuture27 = completableFuture26;
                    return ((CompletableFuture)completableFuture27.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preload(this, completableFuture19, jsonArray, string, triplet, string2, completableFuture27, null, null, 13, arg_0));
                }
                completableFuture26.join();
                this.shippingRate = new CompletableFuture();
                if (jsonArray.isEmpty()) {
                    this.logger.info("There are no items with matching shipping rate. Handling...");
                    this.api.checkIsOOS();
                    this.configureShippingRate();
                    this.isPreload = false;
                    this.isContactpreload = true;
                    return CompletableFuture.completedFuture(string);
                }
                jsonArray.remove(triplet.third);
                continue;
            }
            CompletableFuture completableFuture28 = this.submitShipping(string);
            if (!completableFuture28.isDone()) {
                CompletableFuture completableFuture29 = completableFuture28;
                return ((CompletableFuture)completableFuture29.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preload(this, completableFuture19, jsonArray, string, triplet, string2, completableFuture29, null, null, 14, arg_0));
            }
            completableFuture28.join();
            break;
        }
        CompletableFuture completableFuture30 = this.clearWithChangeJS();
        if (!completableFuture30.isDone()) {
            CompletableFuture completableFuture31 = completableFuture30;
            return ((CompletableFuture)completableFuture31.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preload(this, completableFuture31, jsonArray, string, null, null, null, null, null, 15, arg_0));
        }
        completableFuture30.join();
        CompletableFuture completableFuture32 = this.checkCart();
        if (!completableFuture32.isDone()) {
            CompletableFuture completableFuture33 = completableFuture32;
            return ((CompletableFuture)completableFuture33.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preload(this, completableFuture33, jsonArray, string, null, null, null, null, null, 16, arg_0));
        }
        completableFuture32.join();
        this.api.checkIsOOS();
        this.configureShippingRate();
        return CompletableFuture.completedFuture(string);
    }

    @Override
    public CompletableFuture run() {
        int n = 1;
        String string = null;
        this.vertx.setPeriodic(TimeUnit.SECONDS.toMillis(15L), this::lambda$run$0);
        CompletableFuture completableFuture = ShopifySafe.initHarvesters();
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, completableFuture2, null, null, null, null, 1, arg_0));
        }
        completableFuture.join();
        try {
            Object object;
            Object object2;
            this.genPaymentToken();
            this.paymentGateway = SiteParser.getGatewayFromSite(this.task.getSite(), this.isCod);
            this.fastPreload = this.task.getMode().contains("fast") && this.paymentGateway != null;
            CompletableFuture completableFuture3 = this.initShopDetails();
            if (!completableFuture3.isDone()) {
                CompletableFuture completableFuture4 = completableFuture3;
                return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, completableFuture4, string, null, null, null, 2, arg_0));
            }
            completableFuture3.join();
            if (this.task.getMode().contains("login")) {
                this.logger.info("Login required. Logging in...");
                CompletableFuture completableFuture5 = this.login();
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, completableFuture6, string, null, null, null, 3, arg_0));
                }
                completableFuture5.join();
            }
            this.presolver = new Presolver(this);
            if (this.isGraph) {
                this.isPreload = false;
                this.isContactpreload = false;
                CompletableFuture completableFuture7 = this.graphMonitor();
                if (!completableFuture7.isDone()) {
                    CompletableFuture completableFuture8 = completableFuture7;
                    ShopifySafe shopifySafe = this;
                    return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, completableFuture8, string, shopifySafe, null, null, 4, arg_0));
                }
                this.instanceSignal = (String)completableFuture7.join();
                CompletableFuture completableFuture9 = this.graphGenCheckout();
                if (!completableFuture9.isDone()) {
                    CompletableFuture completableFuture10 = completableFuture9;
                    return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, completableFuture10, string, null, null, null, 5, arg_0));
                }
                string = (String)completableFuture9.join();
                string = string.replace("\\", "");
                CompletableFuture completableFuture11 = this.getGraphCheckoutURL(string);
                if (!completableFuture11.isDone()) {
                    CompletableFuture completableFuture12 = completableFuture11;
                    return ((CompletableFuture)completableFuture12.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, completableFuture12, string, null, null, null, 6, arg_0));
                }
                string = (String)completableFuture11.join();
                CompletableFuture completableFuture13 = this.handlePreload(string);
                if (!completableFuture13.isDone()) {
                    CompletableFuture completableFuture14 = completableFuture13;
                    return ((CompletableFuture)completableFuture14.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, completableFuture14, string, null, null, null, 7, arg_0));
                }
                string = (String)completableFuture13.join();
            } else if (this.isPreload) {
                CompletableFuture completableFuture15 = this.preload();
                if (!completableFuture15.isDone()) {
                    CompletableFuture completableFuture16 = completableFuture15;
                    return ((CompletableFuture)completableFuture16.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, completableFuture16, string, null, null, null, 8, arg_0));
                }
                string = (String)completableFuture15.join();
            } else {
                CompletableFuture completableFuture17 = this.preloadContactOnly();
                if (!completableFuture17.isDone()) {
                    CompletableFuture completableFuture18 = completableFuture17;
                    return ((CompletableFuture)completableFuture18.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, completableFuture18, string, null, null, null, 9, arg_0));
                }
                string = (String)completableFuture17.join();
            }
            if (!this.api.getCookies().contains("_shopify_checkpoint")) {
                this.cpMonitor = this.presolver.run();
            }
            if (this.isGraph || !this.isKeyword) {
                if (this.isEL) {
                    CompletableFuture completableFuture19 = this.fetchELJS();
                    if (!completableFuture19.isDone()) {
                        CompletableFuture completableFuture20 = completableFuture19;
                        return ((CompletableFuture)completableFuture20.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, completableFuture20, string, null, null, null, 12, arg_0));
                    }
                    object2 = (JsonObject)completableFuture19.join();
                    this.instanceSignal = VariantHandler.selectVariantFromLink((JsonObject)object2, this.task.getSize(), this);
                }
            } else {
                object2 = null;
                while (object2 == null) {
                    CompletableFuture completableFuture21 = this.fetchProductsJSON(true);
                    if (!completableFuture21.isDone()) {
                        CompletableFuture completableFuture22 = completableFuture21;
                        return ((CompletableFuture)completableFuture22.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, completableFuture22, string, null, (String)object2, null, 10, arg_0));
                    }
                    object = (JsonObject)completableFuture21.join();
                    object2 = VariantHandler.selectVariantFromKeyword((JsonObject)object, this.task.getSize(), this.positiveKeywords, this.negativeKeywords, this);
                    if (object2 == null) {
                        CompletableFuture completableFuture23 = VertxUtil.signalSleep(this.instanceSignal, this.task.getMonitorDelay());
                        if (!completableFuture23.isDone()) {
                            CompletableFuture completableFuture24 = completableFuture23;
                            return ((CompletableFuture)completableFuture24.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, completableFuture24, string, null, (String)object2, (JsonObject)object, 11, arg_0));
                        }
                        String string2 = (String)completableFuture23.join();
                        if (string2 == null) continue;
                        object2 = VariantHandler.selectVariantFromKeyword(new JsonObject(string2), this.task.getSize(), this.positiveKeywords, this.negativeKeywords, this);
                        continue;
                    }
                    VertxUtil.sendSignal(this.instanceSignal, object.encode());
                }
                this.instanceSignal = object2;
            }
            while (this.api.getWebClient().isActive()) {
                try {
                    if (string == null && n != 0 && ThreadLocalRandom.current().nextBoolean()) {
                        CompletableFuture completableFuture25 = this.shippingAndBillingPost();
                        if (!completableFuture25.isDone()) {
                            CompletableFuture completableFuture26 = completableFuture25;
                            return ((CompletableFuture)completableFuture26.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, completableFuture26, string, null, null, null, 13, arg_0));
                        }
                        string = (String)completableFuture25.join();
                        CompletableFuture completableFuture27 = this.handlePreload(string);
                        if (!completableFuture27.isDone()) {
                            CompletableFuture completableFuture28 = completableFuture27;
                            return ((CompletableFuture)completableFuture28.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, completableFuture28, string, null, null, null, 14, arg_0));
                        }
                        string = (String)completableFuture27.join();
                        CompletableFuture completableFuture29 = this.getCheckoutURL(string);
                        if (!completableFuture29.isDone()) {
                            CompletableFuture completableFuture30 = completableFuture29;
                            return ((CompletableFuture)completableFuture30.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, completableFuture30, string, null, null, null, 15, arg_0));
                        }
                        completableFuture29.join();
                        this.shutOffPresolver(false);
                        CompletableFuture completableFuture31 = this.submitContact(string);
                        if (!completableFuture31.isDone()) {
                            CompletableFuture completableFuture32 = completableFuture31;
                            return ((CompletableFuture)completableFuture32.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, completableFuture32, string, null, null, null, 16, arg_0));
                        }
                        completableFuture31.join();
                        CompletableFuture completableFuture33 = this.walletsSubmitShipping(string);
                        if (!completableFuture33.isDone()) {
                            CompletableFuture completableFuture34 = completableFuture33;
                            return ((CompletableFuture)completableFuture34.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, completableFuture34, string, null, null, null, 17, arg_0));
                        }
                        completableFuture33.join();
                    } else {
                        if (!this.isGraph) {
                            CompletableFuture completableFuture35 = this.smoothCart();
                            if (!completableFuture35.isDone()) {
                                CompletableFuture completableFuture36 = completableFuture35;
                                return ((CompletableFuture)completableFuture36.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, completableFuture36, string, null, null, null, 18, arg_0));
                            }
                            completableFuture35.join();
                        }
                        if (string == null) {
                            this.genPaymentToken();
                            CompletableFuture completableFuture37 = this.genCheckoutURLViaCart();
                            if (!completableFuture37.isDone()) {
                                CompletableFuture completableFuture38 = completableFuture37;
                                return ((CompletableFuture)completableFuture38.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, completableFuture38, string, null, null, null, 19, arg_0));
                            }
                            string = (String)completableFuture37.join();
                            CompletableFuture completableFuture39 = this.handlePreload(string);
                            if (!completableFuture39.isDone()) {
                                CompletableFuture completableFuture40 = completableFuture39;
                                return ((CompletableFuture)completableFuture40.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, completableFuture40, string, null, null, null, 20, arg_0));
                            }
                            string = (String)completableFuture39.join();
                            CompletableFuture completableFuture41 = this.getCheckoutURL(string);
                            if (!completableFuture41.isDone()) {
                                CompletableFuture completableFuture42 = completableFuture41;
                                return ((CompletableFuture)completableFuture42.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, completableFuture42, string, null, null, null, 21, arg_0));
                            }
                            completableFuture41.join();
                            this.shutOffPresolver(false);
                            CompletableFuture completableFuture43 = this.submitContact(string);
                            if (!completableFuture43.isDone()) {
                                CompletableFuture completableFuture44 = completableFuture43;
                                return ((CompletableFuture)completableFuture44.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, completableFuture44, string, null, null, null, 22, arg_0));
                            }
                            completableFuture43.join();
                            CompletableFuture completableFuture45 = this.walletsSubmitShipping(string);
                            if (!completableFuture45.isDone()) {
                                CompletableFuture completableFuture46 = completableFuture45;
                                return ((CompletableFuture)completableFuture46.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, completableFuture46, string, null, null, null, 23, arg_0));
                            }
                            completableFuture45.join();
                        } else if (!this.isPreload && !this.isContactpreload) {
                            CompletableFuture completableFuture47 = this.getCheckoutURL(string);
                            if (!completableFuture47.isDone()) {
                                CompletableFuture completableFuture48 = completableFuture47;
                                return ((CompletableFuture)completableFuture48.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, completableFuture48, string, null, null, null, 24, arg_0));
                            }
                            object2 = (String)completableFuture47.join();
                            this.shutOffPresolver(false);
                            if (this.precartItemName != null && !this.precartItemName.equals(this.instanceSignal) && ((String)object2).contains(this.precartItemName)) {
                                this.logger.error("Please notify the developer of cart bug. Thank you [{}]!", (Object)this.precartItemName);
                                CompletableFuture completableFuture49 = VertxUtil.hardCodedSleep(Integer.MAX_VALUE);
                                if (!completableFuture49.isDone()) {
                                    CompletableFuture completableFuture50 = completableFuture49;
                                    return ((CompletableFuture)completableFuture50.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, completableFuture50, string, null, (String)object2, null, 25, arg_0));
                                }
                                completableFuture49.join();
                                CompletableFuture completableFuture51 = VertxUtil.hardCodedSleep(Integer.MAX_VALUE);
                                if (!completableFuture51.isDone()) {
                                    CompletableFuture completableFuture52 = completableFuture51;
                                    return ((CompletableFuture)completableFuture52.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, completableFuture52, string, null, (String)object2, null, 26, arg_0));
                                }
                                completableFuture51.join();
                            }
                            CompletableFuture completableFuture53 = this.submitContact(string);
                            if (!completableFuture53.isDone()) {
                                CompletableFuture completableFuture54 = completableFuture53;
                                return ((CompletableFuture)completableFuture54.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, completableFuture54, string, null, (String)object2, null, 27, arg_0));
                            }
                            completableFuture53.join();
                            CompletableFuture completableFuture55 = this.walletsSubmitShipping(string);
                            if (!completableFuture55.isDone()) {
                                CompletableFuture completableFuture56 = completableFuture55;
                                return ((CompletableFuture)completableFuture56.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, completableFuture56, string, null, (String)object2, null, 28, arg_0));
                            }
                            completableFuture55.join();
                        }
                    }
                    if (this.isContactpreload) {
                        CompletableFuture completableFuture57 = this.genCheckoutURLViaCart();
                        if (!completableFuture57.isDone()) {
                            CompletableFuture completableFuture58 = completableFuture57;
                            return ((CompletableFuture)completableFuture58.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, completableFuture58, string, null, null, null, 29, arg_0));
                        }
                        completableFuture57.join();
                        this.shutOffPresolver(false);
                        CompletableFuture completableFuture59 = this.walletsSubmitShipping(string);
                        if (!completableFuture59.isDone()) {
                            CompletableFuture completableFuture60 = completableFuture59;
                            return ((CompletableFuture)completableFuture60.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, completableFuture60, string, null, null, null, 30, arg_0));
                        }
                        object2 = (String)completableFuture59.join();
                        if (!this.precartItemName.equals(this.instanceSignal) && ((String)object2).contains(this.precartItemName)) {
                            this.logger.error("Please notify the developer of cart bug. Thank you [{}]!", (Object)this.precartItemName);
                            CompletableFuture completableFuture61 = VertxUtil.hardCodedSleep(Integer.MAX_VALUE);
                            if (!completableFuture61.isDone()) {
                                CompletableFuture completableFuture62 = completableFuture61;
                                return ((CompletableFuture)completableFuture62.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, completableFuture62, string, null, (String)object2, null, 31, arg_0));
                            }
                            completableFuture61.join();
                            CompletableFuture completableFuture63 = VertxUtil.hardCodedSleep(Integer.MAX_VALUE);
                            if (!completableFuture63.isDone()) {
                                CompletableFuture completableFuture64 = completableFuture63;
                                return ((CompletableFuture)completableFuture64.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, completableFuture64, string, null, (String)object2, null, 32, arg_0));
                            }
                            completableFuture63.join();
                        }
                    }
                    if (this.isPreload || this.paymentGateway == null || this.api.getCookies().contains("_shopify_checkpoint") || this.isEarly || !this.isPreload && !this.isContactpreload || this.isRestockMode) {
                        CompletableFuture completableFuture65 = this.getProcessingPage(string, false);
                        if (!completableFuture65.isDone()) {
                            CompletableFuture completableFuture66 = completableFuture65;
                            return ((CompletableFuture)completableFuture66.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, completableFuture66, string, null, null, null, 33, arg_0));
                        }
                        object2 = (String)completableFuture65.join();
                        if (!this.precartItemName.equals(this.instanceSignal) && ((String)object2).contains(this.precartItemName)) {
                            this.logger.error("Please notify the developer of cart bug. Thank you [{}]!", (Object)this.precartItemName);
                            CompletableFuture completableFuture67 = VertxUtil.hardCodedSleep(Integer.MAX_VALUE);
                            if (!completableFuture67.isDone()) {
                                CompletableFuture completableFuture68 = completableFuture67;
                                return ((CompletableFuture)completableFuture68.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, completableFuture68, string, null, (String)object2, null, 34, arg_0));
                            }
                            completableFuture67.join();
                            CompletableFuture completableFuture69 = VertxUtil.hardCodedSleep(Integer.MAX_VALUE);
                            if (!completableFuture69.isDone()) {
                                CompletableFuture completableFuture70 = completableFuture69;
                                return ((CompletableFuture)completableFuture70.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, completableFuture70, string, null, (String)object2, null, 35, arg_0));
                            }
                            completableFuture69.join();
                        }
                    }
                    this.shutOffPresolver(true);
                    do {
                        CompletableFuture completableFuture71 = this.processPayment(string);
                        if (!completableFuture71.isDone()) {
                            CompletableFuture completableFuture72 = completableFuture71;
                            return ((CompletableFuture)completableFuture72.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, completableFuture72, string, null, null, null, 36, arg_0));
                        }
                        completableFuture71.join();
                        CompletableFuture completableFuture73 = this.checkOrderAPI(string);
                        if (!completableFuture73.isDone()) {
                            CompletableFuture completableFuture74 = completableFuture73;
                            return ((CompletableFuture)completableFuture74.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, completableFuture74, string, null, null, null, 37, arg_0));
                        }
                        object2 = (JsonObject)completableFuture73.join();
                        object = ShopifySafe.parseDecline((JsonObject)object2);
                        if (object == null || ((String)object).equals("success")) continue;
                        Analytics.failure((String)object, this.task, (JsonObject)object2, this.api.proxyString());
                        this.logger.info("Checkout fail -> " + (String)object);
                    } while (object != null && !((String)object).equals("success"));
                    this.logger.info("Successfully checked out.");
                    Analytics.success(this.task, (JsonObject)object2, this.api.proxyString());
                    return null;
                }
                catch (Throwable throwable) {
                    this.logger.error(throwable.getMessage());
                    this.setAttributes();
                    this.isPreload = false;
                    this.isContactpreload = false;
                    n = 0;
                    string = null;
                }
            }
            return null;
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
            this.logger.error("Task interrupted: " + throwable.getMessage());
        }
        return null;
    }
}

