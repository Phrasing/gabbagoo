/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.json.JsonObject
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.client.HttpResponse
 */
package io.trickle.task.sites.walmart.usa;

import io.trickle.task.sites.walmart.usa.API;
import io.trickle.task.sites.walmart.usa.Walmart;
import io.trickle.task.sites.walmart.usa.WalmartAPI;
import io.trickle.util.request.Request;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import java.lang.invoke.LambdaMetafactory;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class SessionPreload {
    public WalmartAPI client;

    public CompletableFuture updateLocation() {
        HttpRequest httpRequest = this.client.getLocation();
        try {
            CompletableFuture completableFuture = Request.send(httpRequest, new JsonObject("{\"clientName\":\"android\",\"includePickUpLocation\":true,\"persistLocation\":true,\"responseGroup\":\"STOREMETAPLUS\"}").toBuffer());
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$updateLocation(this, httpRequest, completableFuture2, 1, arg_0));
            }
            HttpResponse httpResponse = (HttpResponse)completableFuture.join();
            if (httpResponse == null) return CompletableFuture.completedFuture(null);
            return CompletableFuture.completedFuture(null);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$zyDid(SessionPreload var0, HttpRequest var1_1, CompletableFuture var2_2, int var3_4, Object var4_6) {
        switch (var3_4) {
            case 0: {
                var1_1 = var0.client.updateCheck();
                try {
                    v0 = Request.send(var1_1);
                    if (!v0.isDone()) {
                        var3_5 = v0;
                        return var3_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$zyDid(io.trickle.task.sites.walmart.usa.SessionPreload io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((SessionPreload)var0, (HttpRequest)var1_1, (CompletableFuture)var3_5, (int)1));
                    }
lbl9:
                    // 3 sources

                    while (true) {
                        var2_2 = (HttpResponse)v0.join();
                        if (var2_2 == null) return CompletableFuture.completedFuture(null);
                        return CompletableFuture.completedFuture(null);
                    }
                }
                catch (Throwable var2_3) {
                    // empty catch block
                }
                return CompletableFuture.completedFuture(null);
            }
            case 1: {
                v0 = var2_2;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$fetch(SessionPreload var0, CompletableFuture var1_1, int var2_3, Object var3_11) {
        switch (var2_3) {
            case 0: {
                try {
                    v0 = var0.zyDid();
                    if (!v0.isDone()) {
                        var2_4 = v0;
                        return var2_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$fetch(io.trickle.task.sites.walmart.usa.SessionPreload java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((SessionPreload)var0, (CompletableFuture)var2_4, (int)1));
                    }
lbl8:
                    // 3 sources

                    while (true) {
                        v0.join();
                        v1 = var0.fetchCart();
                        if (!v1.isDone()) {
                            var2_5 = v1;
                            return var2_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$fetch(io.trickle.task.sites.walmart.usa.SessionPreload java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((SessionPreload)var0, (CompletableFuture)var2_5, (int)2));
                        }
lbl15:
                        // 3 sources

                        while (true) {
                            v1.join();
                            v2 = var0.updateLocation();
                            if (!v2.isDone()) {
                                var2_6 = v2;
                                return var2_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$fetch(io.trickle.task.sites.walmart.usa.SessionPreload java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((SessionPreload)var0, (CompletableFuture)var2_6, (int)3));
                            }
lbl22:
                            // 3 sources

                            while (true) {
                                v2.join();
                                v3 = var0.putLocation();
                                if (!v3.isDone()) {
                                    var2_7 = v3;
                                    return var2_7.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$fetch(io.trickle.task.sites.walmart.usa.SessionPreload java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((SessionPreload)var0, (CompletableFuture)var2_7, (int)4));
                                }
lbl29:
                                // 3 sources

                                while (true) {
                                    v3.join();
                                    v4 = var0.doPressoSearch();
                                    if (!v4.isDone()) {
                                        var2_8 = v4;
                                        return var2_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$fetch(io.trickle.task.sites.walmart.usa.SessionPreload java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((SessionPreload)var0, (CompletableFuture)var2_8, (int)5));
                                    }
lbl36:
                                    // 3 sources

                                    while (true) {
                                        v4.join();
                                        var0.client.getWebClient().cookieStore().put("test_cookie", "CheckForPermission", ".walmart.com");
                                        v5 = var0.fetchTerraFirma();
                                        if (!v5.isDone()) {
                                            var2_9 = v5;
                                            return var2_9.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$fetch(io.trickle.task.sites.walmart.usa.SessionPreload java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((SessionPreload)var0, (CompletableFuture)var2_9, (int)6));
                                        }
lbl45:
                                        // 3 sources

                                        while (true) {
                                            v5.join();
                                            v6 = var0.fetchMidas();
                                            if (!v6.isDone()) {
                                                var2_10 = v6;
                                                return var2_10.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$fetch(io.trickle.task.sites.walmart.usa.SessionPreload java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((SessionPreload)var0, (CompletableFuture)var2_10, (int)7));
                                            }
lbl52:
                                            // 3 sources

                                            while (true) {
                                                v6.join();
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
                        break;
                    }
                }
                catch (Throwable var1_2) {
                    // empty catch block
                }
                var0.client.getWebClient().cookieStore().removeAnyMatch("hasCRT");
                var0.client.getWebClient().cookieStore().removeAnyMatch("CRT");
                return CompletableFuture.completedFuture(null);
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
                ** continue;
            }
            case 5: {
                v4 = var1_1;
                ** continue;
            }
            case 6: {
                v5 = var1_1;
                ** continue;
            }
            case 7: {
                v6 = var1_1;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$putLocation(SessionPreload var0, HttpRequest var1_1, CompletableFuture var2_2, int var3_4, Object var4_6) {
        switch (var3_4) {
            case 0: {
                var1_1 = var0.client.putLocation();
                try {
                    v0 = Request.send(var1_1, new JsonObject("{\"clientName\":\"android\",\"includePickUpLocation\":true,\"postalCode\":\"" + var0.client.getTask().getProfile().getZip() + "\",\"persistLocation\":true,\"responseGroup\":\"STOREMETAPLUS\",\"serviceTypes\":\"ALL\"}").toBuffer());
                    if (!v0.isDone()) {
                        var3_5 = v0;
                        return var3_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$putLocation(io.trickle.task.sites.walmart.usa.SessionPreload io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((SessionPreload)var0, (HttpRequest)var1_1, (CompletableFuture)var3_5, (int)1));
                    }
lbl9:
                    // 3 sources

                    while (true) {
                        var2_2 = (HttpResponse)v0.join();
                        if (var2_2 == null) return CompletableFuture.completedFuture(null);
                        return CompletableFuture.completedFuture(null);
                    }
                }
                catch (Throwable var2_3) {
                    // empty catch block
                }
                return CompletableFuture.completedFuture(null);
            }
            case 1: {
                v0 = var2_2;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture fetchMidas() {
        HttpRequest httpRequest = this.client.midasScan();
        try {
            CompletableFuture completableFuture = Request.send(httpRequest);
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$fetchMidas(this, httpRequest, completableFuture2, 1, arg_0));
            }
            HttpResponse httpResponse = (HttpResponse)completableFuture.join();
            if (httpResponse == null) return CompletableFuture.completedFuture(null);
            return CompletableFuture.completedFuture(null);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$doPressoSearch(SessionPreload var0, HttpRequest var1_1, CompletableFuture var2_2, int var3_4, Object var4_6) {
        switch (var3_4) {
            case 0: {
                var1_1 = var0.client.presoSearch();
                try {
                    v0 = Request.send(var1_1);
                    if (!v0.isDone()) {
                        var3_5 = v0;
                        return var3_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$doPressoSearch(io.trickle.task.sites.walmart.usa.SessionPreload io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((SessionPreload)var0, (HttpRequest)var1_1, (CompletableFuture)var3_5, (int)1));
                    }
lbl9:
                    // 3 sources

                    while (true) {
                        var2_2 = (HttpResponse)v0.join();
                        if (var2_2 == null) return CompletableFuture.completedFuture(null);
                        return CompletableFuture.completedFuture(null);
                    }
                }
                catch (Throwable var2_3) {
                    // empty catch block
                }
                return CompletableFuture.completedFuture(null);
            }
            case 1: {
                v0 = var2_2;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture doPressoSearch() {
        HttpRequest httpRequest = this.client.presoSearch();
        try {
            CompletableFuture completableFuture = Request.send(httpRequest);
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$doPressoSearch(this, httpRequest, completableFuture2, 1, arg_0));
            }
            HttpResponse httpResponse = (HttpResponse)completableFuture.join();
            if (httpResponse == null) return CompletableFuture.completedFuture(null);
            return CompletableFuture.completedFuture(null);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$fetchMidas(SessionPreload var0, HttpRequest var1_1, CompletableFuture var2_2, int var3_4, Object var4_6) {
        switch (var3_4) {
            case 0: {
                var1_1 = var0.client.midasScan();
                try {
                    v0 = Request.send(var1_1);
                    if (!v0.isDone()) {
                        var3_5 = v0;
                        return var3_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$fetchMidas(io.trickle.task.sites.walmart.usa.SessionPreload io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((SessionPreload)var0, (HttpRequest)var1_1, (CompletableFuture)var3_5, (int)1));
                    }
lbl9:
                    // 3 sources

                    while (true) {
                        var2_2 = (HttpResponse)v0.join();
                        if (var2_2 == null) return CompletableFuture.completedFuture(null);
                        return CompletableFuture.completedFuture(null);
                    }
                }
                catch (Throwable var2_3) {
                    // empty catch block
                }
                return CompletableFuture.completedFuture(null);
            }
            case 1: {
                v0 = var2_2;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture zyDid() {
        HttpRequest httpRequest = this.client.updateCheck();
        try {
            CompletableFuture completableFuture = Request.send(httpRequest);
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$zyDid(this, httpRequest, completableFuture2, 1, arg_0));
            }
            HttpResponse httpResponse = (HttpResponse)completableFuture.join();
            if (httpResponse == null) return CompletableFuture.completedFuture(null);
            return CompletableFuture.completedFuture(null);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture fetch() {
        try {
            CompletableFuture completableFuture = this.zyDid();
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$fetch(this, completableFuture2, 1, arg_0));
            }
            completableFuture.join();
            CompletableFuture completableFuture3 = this.fetchCart();
            if (!completableFuture3.isDone()) {
                CompletableFuture completableFuture4 = completableFuture3;
                return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$fetch(this, completableFuture4, 2, arg_0));
            }
            completableFuture3.join();
            CompletableFuture completableFuture5 = this.updateLocation();
            if (!completableFuture5.isDone()) {
                CompletableFuture completableFuture6 = completableFuture5;
                return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$fetch(this, completableFuture6, 3, arg_0));
            }
            completableFuture5.join();
            CompletableFuture completableFuture7 = this.putLocation();
            if (!completableFuture7.isDone()) {
                CompletableFuture completableFuture8 = completableFuture7;
                return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$fetch(this, completableFuture8, 4, arg_0));
            }
            completableFuture7.join();
            CompletableFuture completableFuture9 = this.doPressoSearch();
            if (!completableFuture9.isDone()) {
                CompletableFuture completableFuture10 = completableFuture9;
                return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$fetch(this, completableFuture10, 5, arg_0));
            }
            completableFuture9.join();
            this.client.getWebClient().cookieStore().put("test_cookie", "CheckForPermission", ".walmart.com");
            CompletableFuture completableFuture11 = this.fetchTerraFirma();
            if (!completableFuture11.isDone()) {
                CompletableFuture completableFuture12 = completableFuture11;
                return ((CompletableFuture)completableFuture12.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$fetch(this, completableFuture12, 6, arg_0));
            }
            completableFuture11.join();
            CompletableFuture completableFuture13 = this.fetchMidas();
            if (!completableFuture13.isDone()) {
                CompletableFuture completableFuture14 = completableFuture13;
                return ((CompletableFuture)completableFuture14.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$fetch(this, completableFuture14, 7, arg_0));
            }
            completableFuture13.join();
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        this.client.getWebClient().cookieStore().removeAnyMatch("hasCRT");
        this.client.getWebClient().cookieStore().removeAnyMatch("CRT");
        return CompletableFuture.completedFuture(null);
    }

    public static CompletableFuture createSession(Walmart walmart) {
        return new SessionPreload((API)walmart.getClient()).fetch();
    }

    public CompletableFuture fetchCart() {
        HttpRequest httpRequest = this.client.getCart();
        try {
            CompletableFuture completableFuture = Request.send(httpRequest);
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$fetchCart(this, httpRequest, completableFuture2, 1, arg_0));
            }
            HttpResponse httpResponse = (HttpResponse)completableFuture.join();
            if (httpResponse == null) return CompletableFuture.completedFuture(null);
            return CompletableFuture.completedFuture(null);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$putCart(SessionPreload var0, HttpRequest var1_1, CompletableFuture var2_2, int var3_4, Object var4_6) {
        switch (var3_4) {
            case 0: {
                var1_1 = var0.client.putCart();
                try {
                    v0 = Request.send(var1_1, new JsonObject("{\"currencyCode\":\"USD\",\"location\":{\"postalCode\":\"" + var0.client.getTask().getProfile().getZip() + "\",\"state\":\"" + var0.client.getTask().getProfile().getState() + "\",\"country\":\"USA\",\"isZipLocated\":false},\"storeIds\":[2648,5434,2031,2280,5426]}").toBuffer());
                    if (!v0.isDone()) {
                        var3_5 = v0;
                        return var3_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$putCart(io.trickle.task.sites.walmart.usa.SessionPreload io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((SessionPreload)var0, (HttpRequest)var1_1, (CompletableFuture)var3_5, (int)1));
                    }
lbl9:
                    // 3 sources

                    while (true) {
                        var2_2 = (HttpResponse)v0.join();
                        if (var2_2 == null) {
                            // empty if block
                        }
                        break;
                    }
                }
                catch (Throwable var2_3) {
                    // empty catch block
                }
                var0.client.getWebClient().cookieStore().removeAnyMatch("hasCRT");
                var0.client.getWebClient().cookieStore().removeAnyMatch("CRT");
                return CompletableFuture.completedFuture(null);
            }
            case 1: {
                v0 = var2_2;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture fetchTerraFirma() {
        try {
            JsonObject jsonObject = new JsonObject("{\"variables\":\"{\\\"casperSlots\\\":{\\\"fulfillmentType\\\":\\\"ACC\\\",\\\"reservationType\\\":\\\"SLOTS\\\"},\\\"postalAddress\\\":{\\\"addressType\\\":\\\"RESIDENTIAL\\\",\\\"countryCode\\\":\\\"USA\\\",\\\"postalCode\\\":\\\"" + this.client.getTask().getProfile().getZip() + "\\\",\\\"stateOrProvinceCode\\\":\\\"" + this.client.getTask().getProfile().getState() + "\\\",\\\"zipLocated\\\":true},\\\"storeFrontIds\\\":[{\\\"distance\\\":2.24,\\\"inStore\\\":false,\\\"preferred\\\":false,\\\"storeId\\\":\\\"91672\\\",\\\"storeUUID\\\":null,\\\"usStoreId\\\":91672},{\\\"distance\\\":3.04,\\\"inStore\\\":false,\\\"preferred\\\":false,\\\"storeId\\\":\\\"5936\\\",\\\"storeUUID\\\":null,\\\"usStoreId\\\":5936},{\\\"distance\\\":3.31,\\\"inStore\\\":false,\\\"preferred\\\":false,\\\"storeId\\\":\\\"90563\\\",\\\"storeUUID\\\":null,\\\"usStoreId\\\":90563},{\\\"distance\\\":3.41,\\\"inStore\\\":false,\\\"preferred\\\":false,\\\"storeId\\\":\\\"91675\\\",\\\"storeUUID\\\":null,\\\"usStoreId\\\":91675},{\\\"distance\\\":5.58,\\\"inStore\\\":false,\\\"preferred\\\":false,\\\"storeId\\\":\\\"91121\\\",\\\"storeUUID\\\":null,\\\"usStoreId\\\":91121}],\\\"productId\\\":\\\"152481472\\\",\\\"selected\\\":false}\"}");
            int n = 0;
            while (n < 5) {
                HttpRequest httpRequest = this.client.terraFirma("152481472", ThreadLocalRandom.current().nextBoolean());
                CompletableFuture completableFuture = Request.send(httpRequest, jsonObject.toBuffer());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$fetchTerraFirma(this, jsonObject, n, httpRequest, completableFuture2, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse != null && httpResponse.statusCode() != 429) {
                    return CompletableFuture.completedFuture(null);
                }
                ++n;
            }
            return CompletableFuture.completedFuture(null);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$fetchTerraFirma(SessionPreload var0, JsonObject var1_1, int var2_3, HttpRequest var3_4, CompletableFuture var4_5, int var5_6, Object var6_8) {
        switch (var5_6) {
            case 0: {
                try {
                    var1_1 = new JsonObject("{\"variables\":\"{\\\"casperSlots\\\":{\\\"fulfillmentType\\\":\\\"ACC\\\",\\\"reservationType\\\":\\\"SLOTS\\\"},\\\"postalAddress\\\":{\\\"addressType\\\":\\\"RESIDENTIAL\\\",\\\"countryCode\\\":\\\"USA\\\",\\\"postalCode\\\":\\\"" + var0.client.getTask().getProfile().getZip() + "\\\",\\\"stateOrProvinceCode\\\":\\\"" + var0.client.getTask().getProfile().getState() + "\\\",\\\"zipLocated\\\":true},\\\"storeFrontIds\\\":[{\\\"distance\\\":2.24,\\\"inStore\\\":false,\\\"preferred\\\":false,\\\"storeId\\\":\\\"91672\\\",\\\"storeUUID\\\":null,\\\"usStoreId\\\":91672},{\\\"distance\\\":3.04,\\\"inStore\\\":false,\\\"preferred\\\":false,\\\"storeId\\\":\\\"5936\\\",\\\"storeUUID\\\":null,\\\"usStoreId\\\":5936},{\\\"distance\\\":3.31,\\\"inStore\\\":false,\\\"preferred\\\":false,\\\"storeId\\\":\\\"90563\\\",\\\"storeUUID\\\":null,\\\"usStoreId\\\":90563},{\\\"distance\\\":3.41,\\\"inStore\\\":false,\\\"preferred\\\":false,\\\"storeId\\\":\\\"91675\\\",\\\"storeUUID\\\":null,\\\"usStoreId\\\":91675},{\\\"distance\\\":5.58,\\\"inStore\\\":false,\\\"preferred\\\":false,\\\"storeId\\\":\\\"91121\\\",\\\"storeUUID\\\":null,\\\"usStoreId\\\":91121}],\\\"productId\\\":\\\"152481472\\\",\\\"selected\\\":false}\"}");
                    var2_3 = 0;
                    block6: while (var2_3 < 5) {
                        var3_4 = var0.client.terraFirma("152481472", ThreadLocalRandom.current().nextBoolean());
                        v0 = Request.send(var3_4, var1_1.toBuffer());
                        if (!v0.isDone()) {
                            var5_7 = v0;
                            return var5_7.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$fetchTerraFirma(io.trickle.task.sites.walmart.usa.SessionPreload io.vertx.core.json.JsonObject int io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((SessionPreload)var0, (JsonObject)var1_1, (int)var2_3, (HttpRequest)var3_4, (CompletableFuture)var5_7, (int)1));
                        }
lbl12:
                        // 3 sources

                        while (true) {
                            var4_5 = (HttpResponse)v0.join();
                            if (var4_5 != null && var4_5.statusCode() != 429) {
                                return CompletableFuture.completedFuture(null);
                            }
                            ++var2_3;
                            continue block6;
                            break;
                        }
                    }
                    return CompletableFuture.completedFuture(null);
                }
                catch (Throwable var1_2) {
                    // empty catch block
                }
                return CompletableFuture.completedFuture(null);
            }
            case 1: {
                v0 = var4_5;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture putLocation() {
        HttpRequest httpRequest = this.client.putLocation();
        try {
            CompletableFuture completableFuture = Request.send(httpRequest, new JsonObject("{\"clientName\":\"android\",\"includePickUpLocation\":true,\"postalCode\":\"" + this.client.getTask().getProfile().getZip() + "\",\"persistLocation\":true,\"responseGroup\":\"STOREMETAPLUS\",\"serviceTypes\":\"ALL\"}").toBuffer());
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$putLocation(this, httpRequest, completableFuture2, 1, arg_0));
            }
            HttpResponse httpResponse = (HttpResponse)completableFuture.join();
            if (httpResponse == null) return CompletableFuture.completedFuture(null);
            return CompletableFuture.completedFuture(null);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return CompletableFuture.completedFuture(null);
    }

    public SessionPreload(API aPI) {
        this.client = (WalmartAPI)aPI;
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$updateLocation(SessionPreload var0, HttpRequest var1_1, CompletableFuture var2_2, int var3_4, Object var4_6) {
        switch (var3_4) {
            case 0: {
                var1_1 = var0.client.getLocation();
                try {
                    v0 = Request.send(var1_1, new JsonObject("{\"clientName\":\"android\",\"includePickUpLocation\":true,\"persistLocation\":true,\"responseGroup\":\"STOREMETAPLUS\"}").toBuffer());
                    if (!v0.isDone()) {
                        var3_5 = v0;
                        return var3_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$updateLocation(io.trickle.task.sites.walmart.usa.SessionPreload io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((SessionPreload)var0, (HttpRequest)var1_1, (CompletableFuture)var3_5, (int)1));
                    }
lbl9:
                    // 3 sources

                    while (true) {
                        var2_2 = (HttpResponse)v0.join();
                        if (var2_2 == null) return CompletableFuture.completedFuture(null);
                        return CompletableFuture.completedFuture(null);
                    }
                }
                catch (Throwable var2_3) {
                    // empty catch block
                }
                return CompletableFuture.completedFuture(null);
            }
            case 1: {
                v0 = var2_2;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$fetchCart(SessionPreload var0, HttpRequest var1_1, CompletableFuture var2_2, int var3_4, Object var4_6) {
        switch (var3_4) {
            case 0: {
                var1_1 = var0.client.getCart();
                try {
                    v0 = Request.send(var1_1);
                    if (!v0.isDone()) {
                        var3_5 = v0;
                        return var3_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$fetchCart(io.trickle.task.sites.walmart.usa.SessionPreload io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((SessionPreload)var0, (HttpRequest)var1_1, (CompletableFuture)var3_5, (int)1));
                    }
lbl9:
                    // 3 sources

                    while (true) {
                        var2_2 = (HttpResponse)v0.join();
                        if (var2_2 == null) return CompletableFuture.completedFuture(null);
                        return CompletableFuture.completedFuture(null);
                    }
                }
                catch (Throwable var2_3) {
                    // empty catch block
                }
                return CompletableFuture.completedFuture(null);
            }
            case 1: {
                v0 = var2_2;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture putCart() {
        HttpRequest httpRequest = this.client.putCart();
        try {
            CompletableFuture completableFuture = Request.send(httpRequest, new JsonObject("{\"currencyCode\":\"USD\",\"location\":{\"postalCode\":\"" + this.client.getTask().getProfile().getZip() + "\",\"state\":\"" + this.client.getTask().getProfile().getState() + "\",\"country\":\"USA\",\"isZipLocated\":false},\"storeIds\":[2648,5434,2031,2280,5426]}").toBuffer());
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$putCart(this, httpRequest, completableFuture2, 1, arg_0));
            }
            HttpResponse httpResponse = (HttpResponse)completableFuture.join();
            if (httpResponse == null) {
                // empty if block
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        this.client.getWebClient().cookieStore().removeAnyMatch("hasCRT");
        this.client.getWebClient().cookieStore().removeAnyMatch("CRT");
        return CompletableFuture.completedFuture(null);
    }
}

