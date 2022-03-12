/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.MultiMap
 *  io.vertx.core.Vertx
 *  io.vertx.core.json.JsonArray
 *  io.vertx.core.json.JsonObject
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.client.HttpResponse
 *  io.vertx.ext.web.codec.BodyCodec
 */
package io.trickle.task.sites.walmart.usa;

import io.trickle.account.Account;
import io.trickle.core.Controller;
import io.trickle.core.Engine;
import io.trickle.profile.Profile;
import io.trickle.task.Task;
import io.trickle.task.antibot.impl.px.PerimeterX;
import io.trickle.task.sites.walmart.usa.API;
import io.trickle.task.sites.walmart.util.PaymentToken;
import io.trickle.task.sites.walmart.util.RefererStoreController;
import io.trickle.util.Utils;
import io.trickle.util.request.Headers$Pseudo;
import io.trickle.webclient.ClientType;
import io.trickle.webclient.CookieJar;
import io.trickle.webclient.RealClient;
import io.trickle.webclient.RealClientFactory;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.codec.BodyCodec;
import java.lang.invoke.LambdaMetafactory;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WalmartAPIDesktop
extends API {
    public String searchQuery;
    public boolean isZipLocated;
    public int cartLogic;
    public String referer;
    public PerimeterX<MultiMap, MultiMap> pxAPI = null;
    public Task task;
    public boolean isLoggedIn;
    public static DateTimeFormatter GMT_CHROME_RFC1123 = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH);
    public String crossSite;
    public static Pattern VID_LOCATION_PATTERN = Pattern.compile("vid=(.*?)&");
    public JsonArray storeIds;
    public String dateOfPrevReq;

    /*
     * WARNING - void declaration
     */
    @Override
    public CompletableFuture handleBadResponse(int n, HttpResponse httpResponse) {
        switch (n) {
            case 412: {
                if (this.task.getMode().contains("skip") || httpResponse == null) {
                    CompletableFuture completableFuture = this.generatePX(true);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture2 = completableFuture;
                        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartAPIDesktop.async$handleBadResponse(this, n, httpResponse, completableFuture2, null, null, null, null, 0, null, null, 1, arg_0));
                    }
                    completableFuture.join();
                    return CompletableFuture.completedFuture(false);
                }
                JsonObject jsonObject = httpResponse.bodyAsJsonObject();
                String string = jsonObject.getString("uuid");
                String string2 = jsonObject.getString("vid");
                WalmartAPIDesktop walmartAPIDesktop = this;
                CompletableFuture completableFuture = this.pxAPI.solveCaptcha(string2, string, null);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture3 = completableFuture;
                    WalmartAPIDesktop walmartAPIDesktop2 = walmartAPIDesktop;
                    return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartAPIDesktop.async$handleBadResponse(this, n, httpResponse, completableFuture3, jsonObject, string, string2, walmartAPIDesktop2, 0, null, null, 2, arg_0));
                }
                int n2 = walmartAPIDesktop.parseMapIntoCookieJar((MultiMap)completableFuture.join());
                WalmartAPIDesktop walmartAPIDesktop2 = this;
                CompletableFuture completableFuture2 = this.pxAPI.solve();
                if (!completableFuture2.isDone()) {
                    CompletableFuture completableFuture5 = completableFuture2;
                    WalmartAPIDesktop walmartAPIDesktop4 = walmartAPIDesktop2;
                    return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartAPIDesktop.async$handleBadResponse(this, n, httpResponse, completableFuture5, jsonObject, string, string2, walmartAPIDesktop4, n2, null, null, 3, arg_0));
                }
                walmartAPIDesktop2.parseMapIntoCookieJar((MultiMap)completableFuture2.join());
                return CompletableFuture.completedFuture(n2 != 0);
            }
            case 307: {
                try {
                    void var6_23;
                    if (this.task.getMode().contains("skip")) {
                        CompletableFuture completableFuture = this.generatePX(true);
                        if (!completableFuture.isDone()) {
                            CompletableFuture completableFuture6 = completableFuture;
                            return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartAPIDesktop.async$handleBadResponse(this, n, httpResponse, completableFuture6, null, null, null, null, 0, null, null, 4, arg_0));
                        }
                        completableFuture.join();
                        return CompletableFuture.completedFuture(false);
                    }
                    String string = "https://affil.walmart.com" + httpResponse.getHeader("location");
                    String string3 = string.split("uuid=")[1].split("&")[0];
                    Matcher matcher = VID_LOCATION_PATTERN.matcher(string);
                    if (matcher.find()) {
                        String string4 = matcher.group(1).isBlank() ? null : matcher.group(1);
                    } else {
                        Object var6_22 = null;
                    }
                    WalmartAPIDesktop walmartAPIDesktop = this;
                    CompletableFuture completableFuture = this.pxAPI.solveCaptcha((String)var6_23, string3, string);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture7 = completableFuture;
                        WalmartAPIDesktop walmartAPIDesktop5 = walmartAPIDesktop;
                        return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartAPIDesktop.async$handleBadResponse(this, n, httpResponse, completableFuture7, null, string, string3, walmartAPIDesktop5, 0, matcher, (String)var6_23, 5, arg_0));
                    }
                    int n2 = walmartAPIDesktop.parseMapIntoCookieJar((MultiMap)completableFuture.join());
                    WalmartAPIDesktop walmartAPIDesktop3 = this;
                    CompletableFuture completableFuture3 = this.pxAPI.solve();
                    if (!completableFuture3.isDone()) {
                        CompletableFuture completableFuture9 = completableFuture3;
                        WalmartAPIDesktop walmartAPIDesktop7 = walmartAPIDesktop3;
                        return ((CompletableFuture)completableFuture9.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartAPIDesktop.async$handleBadResponse(this, n, httpResponse, completableFuture9, null, string, string3, walmartAPIDesktop7, n2, matcher, (String)var6_23, 6, arg_0));
                    }
                    walmartAPIDesktop3.parseMapIntoCookieJar((MultiMap)completableFuture3.join());
                    return CompletableFuture.completedFuture(n2 != 0);
                }
                catch (Throwable throwable) {
                    System.out.println("Error solving captcha [DESKTOP] " + throwable.getMessage());
                }
            }
            case 444: {
                if (!super.rotateProxy()) return CompletableFuture.completedFuture(true);
                this.pxAPI.restartClient(this.client);
                return CompletableFuture.completedFuture(true);
            }
        }
        return CompletableFuture.completedFuture(true);
    }

    public WalmartAPIDesktop(Task task) {
        super(ClientType.CHROME);
        this.task = task;
        this.crossSite = crossSiteList[ThreadLocalRandom.current().nextInt(crossSiteList.length)];
        this.searchQuery = searchQueries[ThreadLocalRandom.current().nextInt(searchQueries.length)];
        this.storeIds = new JsonArray().add((Object)ThreadLocalRandom.current().nextInt(1904, 5969)).add((Object)ThreadLocalRandom.current().nextInt(1904, 5969)).add((Object)ThreadLocalRandom.current().nextInt(1904, 5969)).add((Object)ThreadLocalRandom.current().nextInt(1904, 5969)).add((Object)ThreadLocalRandom.current().nextInt(1904, 5969));
        this.referer = ((RefererStoreController)Engine.get().getModule(Controller.REFERER)).getRandomReferer();
        this.cartLogic = ThreadLocalRandom.current().nextInt(2);
        this.isLoggedIn = false;
    }

    @Override
    public JsonObject atcForm() {
        switch (this.cartLogic) {
            case 0: {
                return this.productPageAtcForm(this.task.getKeywords()[0], Integer.parseInt(this.task.getSize()));
            }
            case 1: {
                return this.getAtcForm(this.task.getKeywords()[0], Integer.parseInt(this.task.getSize()));
            }
        }
        return this.getAtcForm(this.task.getKeywords()[0], Integer.parseInt(this.task.getSize()));
    }

    @Override
    public void setAPI(PerimeterX perimeterX) {
        this.pxAPI = perimeterX;
    }

    @Override
    public HttpRequest loginAccount() {
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/account/electrode/api/signin?tid=0&returnUrl=%2F").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
        httpRequest.putHeader("sec-ch-ua-mobile", "0?");
        httpRequest.putHeader("user-agent", this.pxAPI.getDeviceUA());
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("origin", "https://www.walmart.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.walmart.com/account/login?tid=0&returnUrl=%2F");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", this.pxAPI.getDeviceLang());
        return httpRequest;
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    public static CompletableFuture async$handleBadResponse(WalmartAPIDesktop var0, int var1_1, HttpResponse var2_2, CompletableFuture var3_3, JsonObject var4_5, String var5_6, String var6_7, WalmartAPIDesktop var7_9, int var8_11, Matcher var9_18, String var10_19, int var11_20, Object var12_21) {
        switch (var11_20) {
            case 0: {
                switch (var1_1) {
                    case 412: {
                        if (!var0.task.getMode().contains("skip") && var2_2 != null) ** GOTO lbl11
                        v0 = var0.generatePX(true);
                        if (!v0.isDone()) {
                            var8_12 = v0;
                            return var8_12.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$handleBadResponse(io.trickle.task.sites.walmart.usa.WalmartAPIDesktop int io.vertx.ext.web.client.HttpResponse java.util.concurrent.CompletableFuture io.vertx.core.json.JsonObject java.lang.String java.lang.String io.trickle.task.sites.walmart.usa.WalmartAPIDesktop int java.util.regex.Matcher java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((WalmartAPIDesktop)var0, (int)var1_1, (HttpResponse)var2_2, (CompletableFuture)var8_12, null, null, null, null, (int)0, null, null, (int)1));
                        }
                        ** GOTO lbl67
lbl11:
                        // 1 sources

                        var3_3 = var2_2.bodyAsJsonObject();
                        var4_5 /* !! */  = var3_3.getString("uuid");
                        var5_6 = var3_3.getString("vid");
                        v1 = var0;
                        v2 = var0.pxAPI.solveCaptcha((String)var5_6, (String)var4_5 /* !! */ , null);
                        if (!v2.isDone()) {
                            var9_18 = v2;
                            var8_13 = v1;
                            return var9_18.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$handleBadResponse(io.trickle.task.sites.walmart.usa.WalmartAPIDesktop int io.vertx.ext.web.client.HttpResponse java.util.concurrent.CompletableFuture io.vertx.core.json.JsonObject java.lang.String java.lang.String io.trickle.task.sites.walmart.usa.WalmartAPIDesktop int java.util.regex.Matcher java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((WalmartAPIDesktop)var0, (int)var1_1, (HttpResponse)var2_2, (CompletableFuture)var9_18, (JsonObject)var3_3, (String)var4_5 /* !! */ , (String)var5_6, (WalmartAPIDesktop)var8_13, (int)0, null, null, (int)2));
                        }
                        ** GOTO lbl51
                    }
                    case 307: {
                        try {
                            if (!var0.task.getMode().contains("skip")) ** GOTO lbl29
                            v3 = var0.generatePX(true);
                            if (!v3.isDone()) {
                                var8_15 = v3;
                                return var8_15.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$handleBadResponse(io.trickle.task.sites.walmart.usa.WalmartAPIDesktop int io.vertx.ext.web.client.HttpResponse java.util.concurrent.CompletableFuture io.vertx.core.json.JsonObject java.lang.String java.lang.String io.trickle.task.sites.walmart.usa.WalmartAPIDesktop int java.util.regex.Matcher java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((WalmartAPIDesktop)var0, (int)var1_1, (HttpResponse)var2_2, (CompletableFuture)var8_15, null, null, null, null, (int)0, null, null, (int)4));
                            }
                            ** GOTO lbl93
lbl29:
                            // 1 sources

                            var3_3 = "https://affil.walmart.com" + var2_2.getHeader("location");
                            var4_5 /* !! */  = var3_3.split("uuid=")[1].split("&")[0];
                            var5_6 = WalmartAPIDesktop.VID_LOCATION_PATTERN.matcher((CharSequence)var3_3);
                            var6_7 = var5_6.find() != false ? (var5_6.group(1).isBlank() != false ? null : var5_6.group(1)) : null;
                            v4 = var0;
                            v5 = var0.pxAPI.solveCaptcha(var6_7, (String)var4_5 /* !! */ , (String)var3_3);
                            if (!v5.isDone()) {
                                var9_18 = v5;
                                var8_16 = v4;
                                return var9_18.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$handleBadResponse(io.trickle.task.sites.walmart.usa.WalmartAPIDesktop int io.vertx.ext.web.client.HttpResponse java.util.concurrent.CompletableFuture io.vertx.core.json.JsonObject java.lang.String java.lang.String io.trickle.task.sites.walmart.usa.WalmartAPIDesktop int java.util.regex.Matcher java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((WalmartAPIDesktop)var0, (int)var1_1, (HttpResponse)var2_2, (CompletableFuture)var9_18, null, (String)var3_3, (String)var4_5 /* !! */ , (WalmartAPIDesktop)var8_16, (int)0, (Matcher)var5_6, (String)var6_7, (int)5));
                            }
lbl39:
                            // 3 sources

                            while (true) {
                                var7_10 = (int)v4.parseMapIntoCookieJar((MultiMap)v5.join());
                                v6 = var0;
                                v7 = var0.pxAPI.solve();
                                if (!v7.isDone()) {
                                    var9_18 = v7;
                                    var8_17 = v6;
                                    return var9_18.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$handleBadResponse(io.trickle.task.sites.walmart.usa.WalmartAPIDesktop int io.vertx.ext.web.client.HttpResponse java.util.concurrent.CompletableFuture io.vertx.core.json.JsonObject java.lang.String java.lang.String io.trickle.task.sites.walmart.usa.WalmartAPIDesktop int java.util.regex.Matcher java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((WalmartAPIDesktop)var0, (int)var1_1, (HttpResponse)var2_2, (CompletableFuture)var9_18, null, (String)var3_3, (String)var4_5 /* !! */ , (WalmartAPIDesktop)var8_17, (int)var7_10, (Matcher)var5_6, (String)var6_7, (int)6));
                                }
                                ** GOTO lbl116
                                break;
                            }
                        }
                        catch (Throwable var3_4) {
                            System.out.println("Error solving captcha [DESKTOP] " + var3_4.getMessage());
                            ** GOTO lbl60
                        }
                    }
lbl51:
                    // 2 sources

                    while (true) {
                        var6_8 = (int)v1.parseMapIntoCookieJar((MultiMap)v2.join());
                        v8 = var0;
                        v9 = var0.pxAPI.solve();
                        if (!v9.isDone()) {
                            var9_18 = v9;
                            var8_14 = v8;
                            return var9_18.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$handleBadResponse(io.trickle.task.sites.walmart.usa.WalmartAPIDesktop int io.vertx.ext.web.client.HttpResponse java.util.concurrent.CompletableFuture io.vertx.core.json.JsonObject java.lang.String java.lang.String io.trickle.task.sites.walmart.usa.WalmartAPIDesktop int java.util.regex.Matcher java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((WalmartAPIDesktop)var0, (int)var1_1, (HttpResponse)var2_2, (CompletableFuture)var9_18, (JsonObject)var3_3, (String)var4_5 /* !! */ , (String)var5_6, (WalmartAPIDesktop)var8_14, (int)var6_8, null, null, (int)3));
                        }
                        ** GOTO lbl88
                        break;
                    }
lbl60:
                    // 2 sources

                    case 444: {
                        if (super.rotateProxy() == false) return CompletableFuture.completedFuture(true);
                        var0.pxAPI.restartClient(var0.client);
                        return CompletableFuture.completedFuture(true);
                    }
                }
                return CompletableFuture.completedFuture(true);
            }
            case 1: {
                v0 = var3_3;
lbl67:
                // 2 sources

                v0.join();
                return CompletableFuture.completedFuture(false);
            }
            case 2: {
                v1 = var7_9;
                v2 = var3_3;
                v10 = var4_5 /* !! */ ;
                v11 = var5_6;
                var5_6 = var6_7;
                var4_5 /* !! */  = v11;
                var3_3 = v10;
                ** continue;
            }
            case 3: {
                v8 = var7_9;
                v9 = var3_3;
                v12 = var4_5 /* !! */ ;
                v13 = var5_6;
                var6_8 = var8_11;
                var5_6 = var6_7;
                var4_5 /* !! */  = v13;
                var3_3 = v12;
lbl88:
                // 2 sources

                v8.parseMapIntoCookieJar((MultiMap)v9.join());
                return CompletableFuture.completedFuture((boolean)var6_8);
            }
            case 4: {
                v3 = var3_3;
lbl93:
                // 2 sources

                v3.join();
                return CompletableFuture.completedFuture(false);
            }
            case 5: {
                v4 = var7_9;
                v5 = var3_3;
                v14 = var5_6;
                v15 = var6_7;
                var6_7 = var10_19;
                var5_6 = var9_18;
                var4_5 /* !! */  = v15;
                var3_3 = v14;
                ** continue;
            }
            case 6: {
                v6 = var7_9;
                v7 = var3_3;
                v16 = var5_6;
                v17 = var6_7;
                var7_10 = var8_11;
                var6_7 = var10_19;
                var5_6 = var9_18;
                var4_5 /* !! */  = v17;
                var3_3 = v16;
lbl116:
                // 2 sources

                v6.parseMapIntoCookieJar((MultiMap)v7.join());
                return CompletableFuture.completedFuture((boolean)var7_10);
            }
        }
        throw new IllegalArgumentException();
    }

    public HttpRequest electrode() {
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/electrode/api/logger").as(BodyCodec.none());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("accept", "application/json");
        httpRequest.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
        httpRequest.putHeader("sec-ch-ua-mobile", "0?");
        httpRequest.putHeader("user-agent", this.pxAPI.getDeviceUA());
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("origin", "https://www.walmart.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.walmart.com/");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", this.pxAPI.getDeviceLang());
        return httpRequest;
    }

    @Override
    public HttpRequest transferCart(String string) {
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/api//saved/:CID/items/" + string + "/transfer").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("accept", "application/json");
        httpRequest.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
        httpRequest.putHeader("sec-ch-ua-mobile", "0?");
        httpRequest.putHeader("user-agent", this.pxAPI.getDeviceUA());
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("origin", "https://www.walmart.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.walmart.com/checkout/");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", this.pxAPI.getDeviceLang());
        return httpRequest;
    }

    public HttpRequest pxScript() {
        HttpRequest httpRequest = this.client.getAbs("https://www.walmart.com/px/PXu6b0qd2S/init.js").as(BodyCodec.buffer());
        httpRequest.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
        httpRequest.putHeader("sec-ch-ua-mobile", "0?");
        httpRequest.putHeader("user-agent", this.pxAPI.getDeviceUA());
        httpRequest.putHeader("accept", "application/signed-exchange;v=b3;q=0.9,*/*;q=0.8");
        httpRequest.putHeader("purpose", "prefetch");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "no-cors");
        httpRequest.putHeader("sec-fetch-dest", "script");
        httpRequest.putHeader("referer", "https://www.walmart.com/");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", this.pxAPI.getDeviceLang());
        return httpRequest;
    }

    public boolean parseMapIntoCookieJar(MultiMap multiMap) {
        if (multiMap == null) {
            return false;
        }
        for (Map.Entry entry : multiMap.entries()) {
            this.getWebClient().cookieStore().put((String)entry.getKey(), (String)entry.getValue(), ".walmart.com");
        }
        if (multiMap.isEmpty()) return false;
        return true;
    }

    @Override
    public JsonObject getProcessingForm() {
        return new JsonObject();
    }

    @Override
    public JsonObject PCIDForm() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("storeList", (Object)new JsonArray());
        jsonObject.put("postalCode", (Object)this.task.getProfile().getZip());
        jsonObject.put("city", (Object)this.task.getProfile().getCity());
        jsonObject.put("state", (Object)this.task.getProfile().getState());
        jsonObject.put("isZipLocated", (Object)this.isZipLocated);
        jsonObject.put("crt:CRT", (Object)"");
        jsonObject.put("customerId:CID", (Object)"");
        jsonObject.put("customerType:type", (Object)"");
        jsonObject.put("affiliateInfo:com.wm.reflector", (Object)"");
        return jsonObject;
    }

    @Override
    public JsonObject getBillingForm(PaymentToken paymentToken) {
        Profile profile = this.task.getProfile();
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("encryptedPan", (Object)paymentToken.getEncryptedPan());
        jsonObject.put("encryptedCvv", (Object)paymentToken.getEncryptedCvv());
        jsonObject.put("integrityCheck", (Object)paymentToken.getIntegrityCheck());
        jsonObject.put("keyId", (Object)paymentToken.getKeyId());
        jsonObject.put("phase", (Object)paymentToken.getPhase());
        jsonObject.put("state", (Object)profile.getState());
        jsonObject.put("postalCode", (Object)profile.getZip());
        jsonObject.put("addressLineOne", (Object)profile.getAddress1());
        jsonObject.put("addressLineTwo", (Object)profile.getAddress2());
        jsonObject.put("city", (Object)profile.getCity().toUpperCase());
        jsonObject.put("firstName", (Object)profile.getFirstName());
        jsonObject.put("lastName", (Object)profile.getLastName());
        jsonObject.put("expiryMonth", (Object)profile.getExpiryMonth());
        jsonObject.put("expiryYear", (Object)profile.getExpiryYear());
        jsonObject.put("phone", (Object)profile.getPhone());
        jsonObject.put("cardType", (Object)profile.getPaymentMethod().get());
        jsonObject.put("isGuest", (Object)true);
        return jsonObject;
    }

    @Override
    public HttpRequest getCartV3(String string) {
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/api/v3/cart/" + string).as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
        httpRequest.putHeader("sec-ch-ua-mobile", "0?");
        httpRequest.putHeader("user-agent", this.pxAPI.getDeviceUA());
        httpRequest.putHeader("credentials", "include");
        httpRequest.putHeader("omitcorrelationid", "true");
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
        httpRequest.putHeader("omitcsrfjwt", "true");
        httpRequest.putHeader("origin", "https://www.walmart.com");
        httpRequest.putHeader("referer", "https://www.walmart.com/search/?query=" + this.searchQuery);
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", this.pxAPI.getDeviceLang());
        return httpRequest;
    }

    public int getCartLogic() {
        return this.cartLogic;
    }

    @Override
    public JsonObject getShippingRateForm(JsonObject jsonObject) {
        String string = jsonObject.getJsonArray("items").getJsonObject(0).getString("id");
        JsonArray jsonArray = jsonObject.getJsonArray("items");
        String string2 = jsonArray.getJsonObject(0).getJsonObject("fulfillmentSelection").getString("shipMethod");
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.put("fulfillmentOption", (Object)"S2H");
        jsonObject2.put("itemIds", (Object)new JsonArray().add((Object)string));
        jsonObject2.put("shipMethod", (Object)string2);
        JsonObject jsonObject3 = new JsonObject();
        jsonObject3.put("groups", (Object)new JsonArray().add((Object)jsonObject2));
        return jsonObject3;
    }

    public HttpRequest cartSuggestedCartPage() {
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/api/v3/cart/:CRT/items").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
        httpRequest.putHeader("sec-ch-ua-mobile", "0?");
        httpRequest.putHeader("user-agent", this.pxAPI.getDeviceUA());
        httpRequest.putHeader("credentials", "include");
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
        httpRequest.putHeader("omitcsrfjwt", "true");
        httpRequest.putHeader("wm_qos.correlation_id", "df2291f5-73f5-494a-bfa8-dd0dbdc3f585");
        httpRequest.putHeader("origin", "https://www.walmart.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.walmart.com/cart");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", this.pxAPI.getDeviceLang());
        return httpRequest;
    }

    public HttpRequest googlePrefetch() {
        HttpRequest httpRequest = this.client.getAbs("https://www.walmart.com/").as(BodyCodec.none());
        httpRequest.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
        httpRequest.putHeader("sec-ch-ua-mobile", "0?");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.pxAPI.getDeviceUA());
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("purpose", "prefetch");
        httpRequest.putHeader("sec-fetch-site", "cross-site");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("referer", "https://www.google.com/");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", this.pxAPI.getDeviceLang());
        return httpRequest;
    }

    @Override
    public HttpRequest terraFirma(String string, boolean bl) {
        String string2 = "https://www.walmart.com/terra-firma/graphql?v=2&options=timing%2Cnonnull%2Cerrors%2Ccontext&id=FullProductHolidaysRoute-web";
        if (bl) {
            string2 = "https://www.walmart.com/terra-firma/graphql?options=timing,nonnull,context&v=2&id=FullProductRoute-web";
        }
        HttpRequest httpRequest = this.client.postAbs(string2).as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
        httpRequest.putHeader("sec-ch-ua-mobile", "0?");
        httpRequest.putHeader("user-agent", this.pxAPI.getDeviceUA());
        httpRequest.putHeader("credentials", "include");
        httpRequest.putHeader("omitcorrelationid", "true");
        httpRequest.putHeader("content-type", "application/json; charset=utf-8");
        httpRequest.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
        httpRequest.putHeader("omitcsrfjwt", "true");
        httpRequest.putHeader("origin", "https://www.walmart.com");
        httpRequest.putHeader("referer", "https://www.walmart.com/search/?query=" + this.searchQuery);
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", this.pxAPI.getDeviceLang());
        return httpRequest;
    }

    @Override
    public JsonObject getProcessingForm(PaymentToken paymentToken) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("paymentType", (Object)"CREDITCARD");
        jsonObject.put("encryptedCvv", (Object)paymentToken.getEncryptedCvv());
        jsonObject.put("encryptedPan", (Object)paymentToken.getEncryptedPan());
        jsonObject.put("integrityCheck", (Object)paymentToken.getIntegrityCheck());
        jsonObject.put("keyId", (Object)paymentToken.getKeyId());
        jsonObject.put("phase", (Object)paymentToken.getPhase());
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.put("cvvInSession", (Object)true);
        jsonObject2.put("voltagePayments", (Object)new JsonArray().add((Object)jsonObject));
        return jsonObject2;
    }

    public HttpRequest cartProdGuestCRT() {
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/api/v3/cart/:CRT/items").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("accept", "application/json");
        httpRequest.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
        httpRequest.putHeader("sec-ch-ua-mobile", "0?");
        httpRequest.putHeader("user-agent", this.pxAPI.getDeviceUA());
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("origin", "https://www.walmart.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.walmart.com/ip/2020-21-Panini-Hoops-NBA-Basketball-Trading-Cards-Holiday-Blaster-Box-88-Cards-Retail-Exclusives/377461077");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", this.pxAPI.getDeviceLang());
        return httpRequest;
    }

    @Override
    public HttpRequest submitShipping() {
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/api/checkout/v3/contract/:PCID/shipping-address").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
        httpRequest.putHeader("inkiru_precedence", "false");
        httpRequest.putHeader("wm_cvv_in_session", "true");
        httpRequest.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
        httpRequest.putHeader("sec-ch-ua-mobile", "0?");
        httpRequest.putHeader("user-agent", this.pxAPI.getDeviceUA());
        httpRequest.putHeader("wm_vertical_id", "0");
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("origin", "https://www.walmart.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.walmart.com/checkout/");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", this.pxAPI.getDeviceLang());
        return httpRequest;
    }

    @Override
    public HttpRequest homepage() {
        HttpRequest httpRequest = this.client.getAbs("https://www.walmart.com/").as(BodyCodec.buffer());
        httpRequest.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
        httpRequest.putHeader("sec-ch-ua-mobile", "0?");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.pxAPI.getDeviceUA());
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "cross-site");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("referer", "https://www.google.com/");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", this.pxAPI.getDeviceLang());
        return httpRequest;
    }

    public HttpRequest affilRefresh(String string) {
        HttpRequest httpRequest = this.client.getAbs("https://affil.walmart.com/cart/" + string + "?items=" + this.task.getKeywords()[0] + "|" + this.task.getSize()).as(BodyCodec.none());
        httpRequest.putHeader("cache-control", "max-age=0");
        httpRequest.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
        httpRequest.putHeader("sec-ch-ua-mobile", "0?");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.pxAPI.getDeviceUA());
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "cross-site");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", this.pxAPI.getDeviceLang());
        httpRequest.putHeader("cookie", "DEFAULT_VALUE");
        if (this.dateOfPrevReq != null) {
            httpRequest.putHeader("if-modified-since", this.dateOfPrevReq);
        }
        this.dateOfPrevReq = GMT_CHROME_RFC1123.format(ZonedDateTime.now(ZoneOffset.UTC));
        return httpRequest;
    }

    @Override
    public CookieJar cookieStore() {
        return this.getWebClient().cookieStore();
    }

    @Override
    public JsonObject getPaymentForm(PaymentToken paymentToken) {
        Profile profile = this.task.getProfile();
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("paymentType", (Object)"CREDITCARD");
        jsonObject.put("cardType", (Object)profile.getPaymentMethod().get());
        jsonObject.put("firstName", (Object)profile.getFirstName());
        jsonObject.put("lastName", (Object)profile.getLastName());
        jsonObject.put("addressLineOne", (Object)profile.getAddress1());
        jsonObject.put("addressLineTwo", (Object)profile.getAddress2());
        jsonObject.put("city", (Object)profile.getCity().toUpperCase());
        jsonObject.put("state", (Object)profile.getState());
        jsonObject.put("postalCode", (Object)profile.getZip());
        jsonObject.put("expiryMonth", (Object)profile.getExpiryMonth());
        jsonObject.put("expiryYear", (Object)profile.getExpiryYear());
        jsonObject.put("email", (Object)profile.getEmail());
        jsonObject.put("phone", (Object)profile.getPhone());
        jsonObject.put("encryptedPan", (Object)paymentToken.getEncryptedPan());
        jsonObject.put("encryptedCvv", (Object)paymentToken.getEncryptedCvv());
        jsonObject.put("integrityCheck", (Object)paymentToken.getIntegrityCheck());
        jsonObject.put("keyId", (Object)paymentToken.getKeyId());
        jsonObject.put("phase", (Object)paymentToken.getPhase());
        jsonObject.put("piHash", (Object)paymentToken.getPiHash());
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.put("payments", (Object)new JsonArray().add((Object)jsonObject));
        jsonObject2.put("cvvInSession", (Object)true);
        return jsonObject2;
    }

    @Override
    public HttpRequest selectShipping() {
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/api/checkout/v3/contract/:PCID/fulfillment").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
        httpRequest.putHeader("inkiru_precedence", "false");
        httpRequest.putHeader("wm_cvv_in_session", "true");
        httpRequest.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
        httpRequest.putHeader("sec-ch-ua-mobile", "0?");
        httpRequest.putHeader("user-agent", this.pxAPI.getDeviceUA());
        httpRequest.putHeader("wm_vertical_id", "0");
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("origin", "https://www.walmart.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.walmart.com/checkout/");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", this.pxAPI.getDeviceLang());
        return httpRequest;
    }

    @Override
    public JsonObject getAtcForm(String string, int n) {
        this.isZipLocated = this.client.cookieStore().asString().contains(this.task.getProfile().getZip());
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("quantity", (Object)n);
        jsonObject.put("actionType", (Object)"INCREASE");
        jsonObject.put("customAttributes", (Object)new JsonArray("[{\"type\":\"NON_DISPLAY\",\"name\":\"pita\",\"value\":0}]"));
        jsonObject.put("location", (Object)new JsonObject().put("postalCode", (Object)Integer.parseInt(this.task.getProfile().getZip())).put("city", (Object)this.task.getProfile().getCity()).put("state", (Object)this.task.getProfile().getState()).put("isZipLocated", (Object)this.isZipLocated));
        jsonObject.put("storeIds", (Object)this.storeIds);
        jsonObject.put("offerId", (Object)string);
        return jsonObject;
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$initialisePX(WalmartAPIDesktop var0, CompletableFuture var1_1, int var2_2, Object var3_3) {
        switch (var2_2) {
            case 0: {
                v0 = var0.pxAPI.initialise();
                if (!v0.isDone()) {
                    var1_1 = v0;
                    return var1_1.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$initialisePX(io.trickle.task.sites.walmart.usa.WalmartAPIDesktop java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((WalmartAPIDesktop)var0, (CompletableFuture)var1_1, (int)1));
                }
                ** GOTO lbl10
            }
            case 1: {
                v0 = var1_1;
lbl10:
                // 2 sources

                v0.join();
                return CompletableFuture.completedFuture(true);
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public CompletableFuture initialisePX() {
        CompletableFuture completableFuture = this.pxAPI.initialise();
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartAPIDesktop.async$initialisePX(this, completableFuture2, 1, arg_0));
        }
        completableFuture.join();
        return CompletableFuture.completedFuture(true);
    }

    public static CompletableFuture async$generatePX(WalmartAPIDesktop walmartAPIDesktop, int n, WalmartAPIDesktop walmartAPIDesktop2, CompletableFuture object, int n2, Object object2) {
        switch (n2) {
            case 0: {
                CompletableFuture completableFuture;
                WalmartAPIDesktop walmartAPIDesktop3;
                try {
                    if (n != 0) {
                        walmartAPIDesktop.pxAPI.reset();
                    }
                    walmartAPIDesktop3 = walmartAPIDesktop;
                    CompletableFuture completableFuture2 = walmartAPIDesktop.pxAPI.solve();
                    completableFuture = completableFuture2;
                    if (completableFuture2.isDone()) return CompletableFuture.completedFuture(walmartAPIDesktop3.parseMapIntoCookieJar((MultiMap)completableFuture.join()));
                    CompletableFuture completableFuture3 = completableFuture;
                    object = walmartAPIDesktop3;
                    return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartAPIDesktop.async$generatePX(walmartAPIDesktop, n, (WalmartAPIDesktop)object, completableFuture3, 1, arg_0));
                }
                catch (Exception exception) {
                    System.out.println("Error generating desktop session: " + exception.getMessage());
                    return CompletableFuture.completedFuture(true);
                }
            }
            case 1: {
                WalmartAPIDesktop walmartAPIDesktop3 = walmartAPIDesktop2;
                CompletableFuture completableFuture = object;
                return CompletableFuture.completedFuture(walmartAPIDesktop3.parseMapIntoCookieJar((MultiMap)completableFuture.join()));
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public JsonObject getShippingForm(JsonObject jsonObject) {
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.put("addressLineOne", (Object)this.task.getProfile().getAddress1());
        jsonObject2.put("addressLineTwo", (Object)this.task.getProfile().getAddress2());
        jsonObject2.put("city", (Object)this.task.getProfile().getCity().toUpperCase());
        jsonObject2.put("firstName", (Object)this.task.getProfile().getFirstName());
        jsonObject2.put("lastName", (Object)this.task.getProfile().getLastName());
        jsonObject2.put("phone", (Object)this.task.getProfile().getPhone());
        jsonObject2.put("email", (Object)this.task.getProfile().getEmail());
        jsonObject2.put("marketingEmailPref", (Object)false);
        jsonObject2.put("postalCode", (Object)this.task.getProfile().getZip());
        jsonObject2.put("state", (Object)this.task.getProfile().getState());
        jsonObject2.put("countryCode", (Object)"USA");
        jsonObject2.put("changedFields", (Object)new JsonArray());
        jsonObject2.put("storeList", (Object)new JsonArray());
        return jsonObject2;
    }

    @Override
    public HttpRequest affilCrossSite(String string) {
        this.getCookies().removeAnyMatch("CRT");
        this.getCookies().removeAnyMatch("cart-item-count");
        this.getCookies().removeAnyMatch("hasCRT");
        HttpRequest httpRequest = this.client.getAbs("https://affil.walmart.com/cart/" + string + "?items=" + this.task.getKeywords()[0] + "|" + this.task.getSize()).as(BodyCodec.none());
        httpRequest.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
        httpRequest.putHeader("sec-ch-ua-mobile", "0?");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.pxAPI.getDeviceUA());
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "cross-site");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        if (this.crossSite != null) {
            httpRequest.putHeader("referer", "https://" + this.crossSite + "/");
            if (this.crossSite.equals("t.co")) {
                httpRequest.headers().remove("sec-fetch-user");
            }
        }
        httpRequest.putHeader("accept-encoding", this.pxAPI.getDeviceAcceptEncoding());
        httpRequest.putHeader("accept-language", this.pxAPI.getDeviceLang());
        httpRequest.putHeader("cookie", "DEFAULT_VALUE");
        if (this.dateOfPrevReq != null) {
            httpRequest.putHeader("if-modified-since", this.dateOfPrevReq);
        }
        this.dateOfPrevReq = GMT_CHROME_RFC1123.format(ZonedDateTime.now(ZoneOffset.UTC));
        return httpRequest;
    }

    public HttpRequest cartSearchGuestCRT() {
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/api/v3/cart/lite/:CRT/items").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
        httpRequest.putHeader("omitcsrfjwt", "true");
        httpRequest.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
        httpRequest.putHeader("sec-ch-ua-mobile", "0?");
        httpRequest.putHeader("user-agent", this.pxAPI.getDeviceUA());
        httpRequest.putHeader("credentials", "include");
        httpRequest.putHeader("omitcorrelationid", "true");
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("origin", "https://www.walmart.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.walmart.com/search/?query=panini");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", this.pxAPI.getDeviceLang());
        return httpRequest;
    }

    @Override
    public HttpRequest submitPayment() {
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/api/checkout/v3/contract/:PCID/payment").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
        httpRequest.putHeader("inkiru_precedence", "false");
        httpRequest.putHeader("wm_cvv_in_session", "true");
        httpRequest.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
        httpRequest.putHeader("sec-ch-ua-mobile", "0?");
        httpRequest.putHeader("user-agent", this.pxAPI.getDeviceUA());
        httpRequest.putHeader("wm_vertical_id", "0");
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("origin", "https://www.walmart.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.walmart.com/checkout/");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", this.pxAPI.getDeviceLang());
        return httpRequest;
    }

    public HttpRequest cart() {
        HttpRequest httpRequest = this.client.getAbs("https://www.walmart.com/cart").as(BodyCodec.none());
        httpRequest.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
        httpRequest.putHeader("sec-ch-ua-mobile", "0?");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.pxAPI.getDeviceUA());
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("service-worker-navigation-preload", "true");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("referer", "https://www.walmart.com/");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", this.pxAPI.getDeviceLang());
        return httpRequest;
    }

    @Override
    public CompletableFuture generatePX(boolean bl) {
        try {
            if (bl) {
                this.pxAPI.reset();
            }
            WalmartAPIDesktop walmartAPIDesktop = this;
            CompletableFuture completableFuture = this.pxAPI.solve();
            if (completableFuture.isDone()) return CompletableFuture.completedFuture(walmartAPIDesktop.parseMapIntoCookieJar((MultiMap)completableFuture.join()));
            CompletableFuture completableFuture2 = completableFuture;
            WalmartAPIDesktop walmartAPIDesktop2 = walmartAPIDesktop;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartAPIDesktop.async$generatePX(this, (int)(bl ? 1 : 0), walmartAPIDesktop2, completableFuture2, 1, arg_0));
        }
        catch (Exception exception) {
            System.out.println("Error generating desktop session: " + exception.getMessage());
            return CompletableFuture.completedFuture(true);
        }
    }

    @Override
    public Task getTask() {
        return this.task;
    }

    @Override
    public JsonObject accountCreateForm() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("personName", (Object)new JsonObject().put("firstName", (Object)this.task.getProfile().getFirstName()).put("lastName", (Object)this.task.getProfile().getLastName()));
        jsonObject.put("email", (Object)(this.task.getProfile().getEmail().split("@")[0] + "+" + ThreadLocalRandom.current().nextInt(999999999) + "@" + this.task.getProfile().getEmail().split("@")[1]));
        jsonObject.put("password", (Object)Utils.generateStrongString());
        jsonObject.put("rememberme", (Object)true);
        jsonObject.put("showRememberme", (Object)"true");
        jsonObject.put("emailNotificationAccepted", (Object)true);
        jsonObject.put("captcha", (Object)new JsonObject().put("sensorData", (Object)"2a25G2m84Vrp0o9c4229201.12-1,8,-36,-890,Mozilla/9.8 (Macintosh; Intel Mac OS X 74_96_3) AppleWebKit/227.39 (KHTML, like Gecko) Chrome/99.3.4477.577 Safari/523.90,uaend,15246,46458276,en-US,Gecko,4,9,7,3,496163,5670088,2788,7415,8699,4229,3176,055,8565,,cpen:8,i0:7,dm:9,cwen:1,non:4,opc:6,fc:7,sc:3,wrc:7,isc:9,vib:1,bat:3,x80:7,x22:7,4393,7.763700514938,983668060469.9,loc:-8,5,-80,-529,do_en,dm_en,t_en-0,9,-04,-369,8,9,0,1,629,784,8;8,0,0,2,745,201,7;0,-4,0,6,-5,-2,9;8,-2,9,2,-3,-8,0;0,3,9,3,5472,834,3;0,-3,4,9,8072,131,6;6,4,1,0,490,516,6;3,1,9,8,4269,047,8;7,2,0,2,631,948,7;0,-4,0,6,-5,-2,9;8,-2,9,2,-3,-8,0;0,3,9,3,000,540,0;2,9,2,5,9223,586,0;6,-5,8,7,-8,-2,9;3,-3,1,9,-1,-1,6;6,4,1,0,8460,899,4;8,-0,7,4,1285,249,7;0,2,1,0,109,337,0;1,1,9,3,5589,780,3;0,8,6,3,2904,551,9;3,4,8,8,1270,879,6;3,1,9,8,4515,047,8;7,2,0,2,649,278,7;0,2,1,0,3713,929,7;3,2,6,7,3470,1858,0;6,-5,8,7,2188,516,6;3,1,9,7,5125,047,8;7,-8,3,0,8320,337,0;1,3,9,3,6461,780,3;1,-3,4,9,9178,193,6;7,2,1,0,8521,745,4;-2,1,-97,-064,4,0,7,1,551,482,4;9,7,0,1,982,047,8;7,-8,3,0,-3,-3,1;0,-1,1,9,-7,-9,7;0,2,2,0,3833,006,7;3,-0,3,5,9244,524,0;6,8,3,2,694,830,6;7,2,2,0,8191,745,4;8,9,0,1,878,784,8;7,-8,3,0,-3,-3,1;0,-1,1,9,-7,-9,7;0,2,1,0,846,667,0;1,1,9,3,5335,780,3;0,-3,4,8,-0,-1,1;0,-7,2,1,-1,-4,0;6,8,2,2,0409,036,2;4,-2,9,8,4422,085,8;7,2,0,2,807,948,7;1,0,1,0,3940,952,7;3,2,6,7,3021,590,1;0,2,4,9,8474,193,6;7,2,1,0,8447,745,4;8,9,0,1,886,014,8;7,2,0,2,0797,031,9;7,5,0,7,7081,8052,3;0,-3,4,8,9382,830,6;7,2,1,9,9057,745,4;8,-0,7,3,2028,948,7;1,2,1,0,4822,952,7;4,-0,2,5,0340,586,0;7,6,2,2,0560,982,2;-3,3,-91,-207,2,5,0123,-2,1,9,191;9,0,2757,-1,2,4,744;2,1,3079,-6,8,7,907;4,2,4625,-9,0,0,036;6,5,0285,-2,1,9,191;3,0,2819,-1,2,4,744;6,2,3128,-6,8,7,907;8,1,4847,-9,0,0,036;0,5,0386,-2,1,9,191;7,0,2911,-1,2,4,744;17,5,2182,-4,1,9,630;10,3,6412,-2,0,1,899;54,4,1201,-2,6,6,118;03,2,3262,-6,8,7,907;24,7,8607,-1,7,3,923;79,1,9186,-3,9,2,355;85,9,5448,-8,2,1,834;48,1,4923,-9,0,0,036;34,3,3164,9,1,9,191;90,1,9579,8,2,4,638;27,4,2616,-4,1,9,524;20,5,6935,-2,0,1,783;64,2,1884,-2,6,6,002;13,3,3800,-6,8,7,891;34,8,8284,-1,7,3,817;89,9,9764,-3,9,2,249;95,0,5966,-8,2,1,728;58,0,4400,-9,0,0,920;44,5,3558,-5,0,6,463;36,1,2183,-4,4,8,511;33,3,8038,-3,9,7,131;93,6,0950,-2,1,9,085;13,1,9957,-8,6,2,990;03,3,1555,-0,7,0,852;20,8,4759,-9,3,0,447;53,8,2687,-1,2,4,638;33,6,2846,-4,1,9,524;36,3,6170,-2,0,1,783;70,4,2070,-2,6,6,002;29,1,4097,-6,8,7,891;50,9,9429,-1,7,3,817;05,9,0912,-3,9,2,249;11,0,6114,-8,2,1,728;74,0,5665,-9,0,0,920;60,5,4713,-5,0,6,463;52,2,3378,-4,4,8,511;49,2,9222,-3,9,7,131;09,6,1149,-2,1,9,085;29,1,0014,-8,6,2,990;19,3,2603,-0,7,0,852;46,7,5138,9,0,1,783;93,3,2474,0,9,2,948;21,8,6805,-8,2,1,427;84,2,5348,-9,0,0,629;70,3,4447,-5,0,6,162;62,3,3067,-4,4,8,210;59,1,9930,-3,9,7,830;19,7,1845,-2,1,9,784;39,0,0718,-8,6,2,699;29,4,2304,-0,7,0,551;56,8,5610,-9,3,0,146;89,8,3518,-1,2,4,337;69,6,3777,-4,1,9,223;62,4,7012,-2,0,1,482;06,3,2805,-2,6,6,701;55,2,4842,-6,8,7,590;76,8,9277,-1,7,3,516;21,9,0829,-3,9,2,948;37,0,6021,-8,2,1,427;90,0,5575,-9,0,0,629;96,5,4623,-5,0,6,162;88,1,3258,-4,4,8,210;75,3,9102,-3,9,7,830;35,5,1018,-2,1,9,784;55,2,0970,-8,6,2,699;45,2,2573,-0,7,0,551;62,9,5764,-9,3,0,146;95,9,3682,-1,2,4,337;75,5,3841,-4,1,9,223;78,4,7171,-2,0,1,482;22,3,2967,-2,6,6,701;71,2,4983,-6,8,7,590;92,7,0485,-1,7,3,516;47,1,1964,-3,9,2,948;53,8,7173,-8,2,1,427;16,2,6617,-9,0,0,629;02,3,5765,-5,0,6,162;94,3,4385,-4,4,8,210;81,1,0246,-3,9,7,830;41,7,2151,-2,1,9,784;71,0,1021,-8,6,2,699;61,4,3616,-0,7,0,551;88,8,6925,-9,3,0,146;11,9,4825,-1,2,4,337;91,5,4004,-4,1,9,223;94,4,8326,-2,0,1,482;38,3,3119,-2,6,6,701;87,1,5323,70,8,5,590;08,7,0795,-1,7,1,516;53,1,1274,-3,9,0,948;890,2,5416,70,8,7,590;200,4,8621,-2,0,1,482;520,8,4303,-1,2,4,337;173,4,3227,-0,7,0,551;066,6,2879,-2,1,9,784;912,1,4019,-4,4,8,210;137,2,6341,-9,0,0,629;361,0,1672,-3,9,2,948;898,1,5834,-6,8,7,590;208,5,8029,-2,0,1,482;538,8,4608,-1,2,4,337;181,4,3521,-0,7,0,551;074,6,2017,-2,1,9,784;920,2,4285,-4,4,8,210;145,0,7789,-9,0,0,629;379,1,2044,-3,9,2,948;806,2,6266,-6,8,7,590;216,3,9829,-2,0,1,482;536,0,5341,-1,2,4,337;189,3,4340,-0,7,0,551;082,5,3864,-2,1,9,784;938,3,5005,-4,4,8,210;153,1,7407,-9,0,0,629;387,9,2803,-3,9,2,948;814,3,6992,-6,8,7,590;224,3,9178,-2,0,1,482;544,0,5690,-1,2,4,337;197,3,5687,-0,7,0,551;080,6,4271,-2,1,9,784;936,1,6529,5,6,2,699;833,2,2446,7,7,0,814;093,5,4634,86,1,7,047;949,1,6115,-4,4,6,573;164,2,8447,-9,0,8,982;398,9,3759,-3,9,0,201;825,3,7848,-6,8,5,853;235,3,0041,-2,0,9,745;555,0,6563,-1,2,2,690;108,3,5403,-0,7,8,814;091,6,4968,-2,1,7,047;957,2,6213,78,4,8,573;172,1,8563,-9,0,0,982;306,9,3849,-3,9,2,201;-0,9,-04,-374,8,8,387,2282,747;2,0,020,1150,523;9,1,375,0926,386;2,8,670,7564,913;4,1,476,3838,281;2,4,380,7647,850;6,2,372,5331,119;0,1,043,3808,222;9,0,607,9192,992;9,7,003,057,028;19,3,895,658,373;07,7,674,858,669;74,5,328,911,479;39,3,696,637,377;78,9,234,958,258;57,2,468,230,036;34,8,547,014,689;98,0,297,995,083;25,1,642,866,876;88,8,937,528,648;10,1,742,165,289;28,4,657,542,545;92,2,539,217,168;26,1,210,079,388;55,0,975,602,454;47,9,563,916,249,-0;55,6,061,549,532,-5;21,1,695,832,188,-8;93,6,8393,117,469,-4;97,6,2675,696,089,-3;31,3,7464,898,933,-2;-1,2,-93,-759,-8,2,-25,-737,-9,9,-64,-195,-5,0,-84,-424,-3,6,-01,-806,-0,4,-12,-019,-2,1,-58,-284,387567,00139,7,0,0,1,621711,9189,1939259142701,1322496127433,99,87740,769,381,1975,5,9,0143,787515,2,fqyq1a9pf0du6kpbu6e0_3626,2828,892,189335680,61257857-9,9,-64,-192,-5,8-8,2,-25,-32,-3086997364;9,39,33,85,88,30,80,61,41,32,67,23,92,21,33,47,6,3,4;,5,5;true;true;true;147;true;56;58;true;false;-5-9,9,-64,-89,9874-2,1,-97,-078,09316063-4,2,-10,-925,245534-7,8,-75,-191,;13;2;9"));
        return jsonObject;
    }

    @Override
    public void setLoggedIn(boolean bl) {
        this.isLoggedIn = bl;
    }

    @Override
    public HttpRequest savedCart() {
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/api/v3/saved/:CID/items").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("accept", "application/json");
        httpRequest.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
        httpRequest.putHeader("sec-ch-ua-mobile", "0?");
        httpRequest.putHeader("user-agent", this.pxAPI.getDeviceUA());
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("origin", "https://www.walmart.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.walmart.com/checkout/");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", this.pxAPI.getDeviceLang());
        return httpRequest;
    }

    @Override
    public HttpRequest addToCart() {
        if (this.isLoggedIn && !this.cookieStore().contains("CRT")) {
            this.cookieStore().put("CRT", UUID.randomUUID().toString(), ".walmart.com");
            this.cookieStore().put("hasCRT", "1", ".walmart.com");
        }
        switch (this.cartLogic) {
            case 0: {
                return this.cartProdGuestCID();
            }
            case 1: {
                return this.cartSearchLiteATC();
            }
        }
        return this.cartSearchLiteATC();
    }

    @Override
    public void swapClient() {
        try {
            RealClient realClient = RealClientFactory.fromOther(Vertx.currentContext().owner(), this.client, this.client.type());
            this.client.close();
            this.client = realClient;
            return;
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public HttpRequest cartSearchLiteATC() {
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/api/v3/cart/lite/" + (this.isLoggedIn ? "customer/:CRT/items" : "guest/:CID/items")).as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
        httpRequest.putHeader("omitcsrfjwt", "true");
        httpRequest.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
        httpRequest.putHeader("sec-ch-ua-mobile", "0?");
        httpRequest.putHeader("user-agent", this.pxAPI.getDeviceUA());
        httpRequest.putHeader("credentials", "include");
        httpRequest.putHeader("omitcorrelationid", "true");
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("origin", "https://www.walmart.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.walmart.com/search/?query=" + this.searchQuery);
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", this.pxAPI.getDeviceLang());
        return httpRequest;
    }

    @Override
    public JsonObject accountLoginForm(Account account) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("username", (Object)account.getUser());
        jsonObject.put("password", (Object)account.getPass());
        jsonObject.put("rememberme", (Object)true);
        jsonObject.put("showRememberme", (Object)"true");
        jsonObject.put("captcha", (Object)new JsonObject().put("sensorData", (Object)""));
        return jsonObject;
    }

    @Override
    public HttpRequest getPCID(PaymentToken paymentToken) {
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/api/checkout/v3/contract?page=CHECKOUT_VIEW").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
        httpRequest.putHeader("wm_cvv_in_session", "true");
        httpRequest.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
        httpRequest.putHeader("sec-ch-ua-mobile", "0?");
        httpRequest.putHeader("user-agent", this.pxAPI.getDeviceUA());
        httpRequest.putHeader("wm_vertical_id", "0");
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("origin", "https://www.walmart.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.walmart.com/checkout/");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", this.pxAPI.getDeviceLang());
        return httpRequest;
    }

    @Override
    public HttpRequest getCheckoutPage() {
        HttpRequest httpRequest = this.client.getAbs("https://www.walmart.com/checkout/").as(BodyCodec.none());
        httpRequest.putHeaders(Headers$Pseudo.MASP.get());
        httpRequest.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
        httpRequest.putHeader("sec-ch-ua-mobile", "0?");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.pxAPI.getDeviceUA());
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("service-worker-navigation-preload", "true");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("referer", "https://www.walmart.com/");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", this.pxAPI.getDeviceLang());
        return httpRequest;
    }

    @Override
    public void close() {
        if (this.pxAPI != null) {
            this.pxAPI.close();
        }
        super.close();
    }

    @Override
    public HttpRequest createAccount() {
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/account/electrode/api/signup?tid=0&returnUrl=%2F").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
        httpRequest.putHeader("sec-ch-ua-mobile", "0?");
        httpRequest.putHeader("user-agent", this.pxAPI.getDeviceUA());
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("origin", "https://www.walmart.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.walmart.com/account/signup?tid=0&returnUrl=%2Fcart%3Faction%3DSignIn%26rm%3Dtrue");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", this.pxAPI.getDeviceLang());
        return httpRequest;
    }

    public HttpRequest cartProdGuestCID() {
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/api/v3/cart/" + (this.isLoggedIn ? "customer/:CRT/items" : "guest/:CID/items")).as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("accept", "application/json");
        httpRequest.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
        httpRequest.putHeader("sec-ch-ua-mobile", "0?");
        httpRequest.putHeader("user-agent", this.pxAPI.getDeviceUA());
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("origin", "https://www.walmart.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", this.referer);
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", this.pxAPI.getDeviceLang());
        return httpRequest;
    }

    @Override
    public HttpRequest processPayment(PaymentToken paymentToken) {
        HttpRequest httpRequest = this.client.putAbs("https://www.walmart.com/api/checkout/v3/contract/:PCID/order").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
        httpRequest.putHeader("inkiru_precedence", "false");
        httpRequest.putHeader("wm_cvv_in_session", "true");
        httpRequest.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
        httpRequest.putHeader("sec-ch-ua-mobile", "0?");
        httpRequest.putHeader("user-agent", this.pxAPI.getDeviceUA());
        httpRequest.putHeader("wm_vertical_id", "0");
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("origin", "https://www.walmart.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.walmart.com/checkout/");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", this.pxAPI.getDeviceLang());
        return httpRequest;
    }

    @Override
    public HttpRequest submitBilling() {
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/api/checkout-customer/:CID/credit-card").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("accept", "application/json");
        httpRequest.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
        httpRequest.putHeader("sec-ch-ua-mobile", "0?");
        httpRequest.putHeader("user-agent", this.pxAPI.getDeviceUA());
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("origin", "https://www.walmart.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.walmart.com/checkout/");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", this.pxAPI.getDeviceLang());
        return httpRequest;
    }

    public HttpRequest productPage() {
        HttpRequest httpRequest = this.client.getAbs(this.referer).as(BodyCodec.buffer());
        httpRequest.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
        httpRequest.putHeader("sec-ch-ua-mobile", "0?");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.pxAPI.getDeviceUA());
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "cross-site");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", this.pxAPI.getDeviceLang());
        return httpRequest;
    }

    public JsonObject productPageAtcForm(String string, int n) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("offerId", (Object)string);
        jsonObject.put("quantity", (Object)n);
        jsonObject.put("storeIds", (Object)this.storeIds);
        jsonObject.put("location", (Object)new JsonObject().put("postalCode", (Object)Integer.parseInt(this.task.getProfile().getZip())).put("city", (Object)this.task.getProfile().getCity()).put("state", (Object)this.task.getProfile().getState()).put("isZipLocated", (Object)this.isZipLocated));
        jsonObject.put("shipMethodDefaultRule", (Object)"SHIP_RULE_1");
        return jsonObject;
    }

    public JsonObject electrodeJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("0", (Object)new JsonObject().put("tags", (Object)"[\"info\",\"home-app, scus-prod-a6, PROD\"]").put("data", (Object)new JsonObject().put("_type", (Object)"fetch").put("extras", (Object)new JsonObject().put("response", (Object)new JsonObject().put("status", null)))));
        jsonObject.put("1", (Object)new JsonObject().put("tags", (Object)"[\"info\",\"home-app, scus-prod-a6, PROD\"]").put("data", (Object)new JsonObject().put("_type", (Object)"fetch").put("extras", (Object)new JsonObject().put("response", (Object)new JsonObject().put("status", null)))));
        return jsonObject;
    }

    public HttpRequest fetchAffiliateCaptcha(String string) {
        HttpRequest httpRequest = this.client.getAbs(string).as(BodyCodec.buffer());
        httpRequest.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
        httpRequest.putHeader("sec-ch-ua-mobile", "0?");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.pxAPI.getDeviceUA());
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "none");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", this.pxAPI.getDeviceLang());
        return httpRequest;
    }

    @Override
    public PerimeterX getPxAPI() {
        return this.pxAPI;
    }

    @Override
    public boolean isLoggedIn() {
        return this.isLoggedIn;
    }

    @Override
    public HttpRequest getCart() {
        HttpRequest httpRequest = this.client.getAbs("https://www.walmart.com/cart").as(BodyCodec.buffer());
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
        httpRequest.putHeader("sec-ch-ua-mobile", "0?");
        httpRequest.putHeader("user-agent", this.pxAPI.getDeviceUA());
        httpRequest.putHeader("origin", "https://www.walmart.com");
        httpRequest.putHeader("referer", "https://www.walmart.com/");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", this.pxAPI.getDeviceLang());
        return httpRequest;
    }

    public HttpRequest affilBuyNowHardRefresh(String string) {
        HttpRequest httpRequest = this.client.getAbs(string + "?items=" + this.task.getKeywords()[0] + "|" + this.task.getSize()).as(BodyCodec.none());
        httpRequest.putHeader("pragma", "no-cache");
        httpRequest.putHeader("cache-control", "no-cache");
        httpRequest.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
        httpRequest.putHeader("sec-ch-ua-mobile", "0?");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.pxAPI.getDeviceUA());
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "cross-site");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", this.pxAPI.getDeviceLang());
        return httpRequest;
    }
}

