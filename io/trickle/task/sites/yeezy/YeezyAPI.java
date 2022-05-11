/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.handler.codec.http.cookie.ClientCookieDecoder
 *  io.netty.handler.codec.http.cookie.Cookie
 *  io.trickle.core.Engine
 *  io.trickle.core.VertxSingleton
 *  io.trickle.profile.Profile
 *  io.trickle.task.Task
 *  io.trickle.task.antibot.impl.akamai.GaneshAPI
 *  io.trickle.task.antibot.impl.akamai.HawkAPI
 *  io.trickle.task.antibot.impl.akamai.pixel.Pixel
 *  io.trickle.task.antibot.impl.akamai.pixel.TrickleAPI
 *  io.trickle.task.antibot.impl.akamai.sensor.Bmak
 *  io.trickle.task.sites.yeezy.util.Sizes
 *  io.trickle.task.sites.yeezy.util.Sizes$SizePair
 *  io.trickle.task.sites.yeezy.util.Utag
 *  io.trickle.util.concurrent.VertxUtil
 *  io.trickle.webclient.CookieJar$ExpirableCookie
 *  io.trickle.webclient.TaskApiClient
 *  io.vertx.core.buffer.Buffer
 *  io.vertx.core.http.HttpClientRequest
 *  io.vertx.core.http.HttpClientResponse
 *  io.vertx.core.http.StreamPriority
 *  io.vertx.core.http.impl.HttpClientImpl
 *  io.vertx.core.json.JsonArray
 *  io.vertx.core.json.JsonObject
 *  io.vertx.core.net.impl.NetClientImpl
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.client.impl.ClientPhase
 *  io.vertx.ext.web.client.impl.HttpContext
 *  io.vertx.ext.web.codec.BodyCodec
 */
package io.trickle.task.sites.yeezy;

import io.netty.handler.codec.http.cookie.ClientCookieDecoder;
import io.netty.handler.codec.http.cookie.Cookie;
import io.trickle.core.Engine;
import io.trickle.core.VertxSingleton;
import io.trickle.profile.Profile;
import io.trickle.task.Task;
import io.trickle.task.antibot.impl.akamai.GaneshAPI;
import io.trickle.task.antibot.impl.akamai.HawkAPI;
import io.trickle.task.antibot.impl.akamai.pixel.Pixel;
import io.trickle.task.antibot.impl.akamai.pixel.TrickleAPI;
import io.trickle.task.antibot.impl.akamai.sensor.Bmak;
import io.trickle.task.sites.yeezy.util.Sizes;
import io.trickle.task.sites.yeezy.util.Utag;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.webclient.CookieJar;
import io.trickle.webclient.TaskApiClient;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.StreamPriority;
import io.vertx.core.http.impl.HttpClientImpl;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.impl.NetClientImpl;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.impl.ClientPhase;
import io.vertx.ext.web.client.impl.HttpContext;
import io.vertx.ext.web.codec.BodyCodec;
import java.lang.invoke.LambdaMetafactory;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class YeezyAPI
extends TaskApiClient {
    public Task task;
    public String sensorUrl = "https://www.yeezysupply.com/QvDG3p/zKgz/wMSc/6Rgu/84e_NO/5t5ESppzm3fi/Qk1Q/QGIM/LzdYPyQ";
    public static String QUEUE_URL = Engine.get().getClientConfiguration().getString("queueUrl", "https://www.yeezysupply.com/__queue/yzysply");
    public String pixelURI = null;
    public Utag utag;
    public Pixel pixelAPI;
    public Bmak bmak;
    public String userAgent;
    public JsonObject taskDevice;
    public int sensorCounter;

    public HttpRequest akamaiScript(boolean bl) {
        HttpRequest httpRequest = this.client.getAbs(this.sensorUrl).as(BodyCodec.string());
        httpRequest.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"99\", \"Google Chrome\";v=\"99\"");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "no-cors");
        httpRequest.putHeader("sec-fetch-dest", "script");
        httpRequest.putHeader("referer", (String)(bl ? "https://www.yeezysupply.com/products/" + this.getSKU() : "https://www.yeezysupply.com/"));
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest processPayment(String string) {
        HttpRequest httpRequest = this.client.postAbs("https://www.yeezysupply.com/api/checkout/orders").as(BodyCodec.string());
        String string2 = this.getInstanaID();
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("x-instana-t", string2);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("x-instana-l", "1,correlationType=web;correlationId=" + string2);
        httpRequest.putHeader("x-instana-s", string2);
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("checkout-authorization", string);
        httpRequest.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"95\", \"Chromium\";v=\"95\", \";Not A Brand\";v=\"99\"");
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("origin", "https://www.yeezysupply.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.yeezysupply.com/payment");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest generalProdAPI() {
        HttpRequest httpRequest = this.client.getAbs("https://www.yeezysupply.com/api/products/" + this.getSKU() + "/availability").as(BodyCodec.string());
        String string = this.getInstanaID();
        httpRequest.putHeader("x-instana-t", string);
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("x-instana-s", string);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"99\", \"Google Chrome\";v=\"99\"");
        httpRequest.putHeader("x-instana-l", "1,correlationType=web;correlationId=" + string);
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.yeezysupply.com/product/" + this.getSKU());
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public void lambda$enableWeightAdjust$0(HttpContext httpContext) {
        if (this.pixelURI != null && httpContext.phase().equals((Object)ClientPhase.SEND_REQUEST) && httpContext.clientRequest() != null) {
            try {
                if (this.pixelURI.endsWith(httpContext.clientRequest().getURI())) {
                    httpContext.clientRequest().setStreamPriority(new StreamPriority().setWeight((short)147));
                }
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
        httpContext.next();
    }

    public HttpRequest deleteCoupon(String string, String string2, String string3) {
        HttpRequest httpRequest = this.client.deleteAbs("https://www.yeezysupply.com/api/checkout/baskets/" + string + "/coupons/" + string3).as(BodyCodec.buffer());
        String string4 = this.getInstanaID();
        httpRequest.putHeader("x-instana-t", string4);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("x-instana-l", "1,correlationType=web;correlationId=" + string4);
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("x-instana-s", string4);
        httpRequest.putHeader("checkout-authorization", string2);
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"95\", \"Chromium\";v=\"95\", \";Not A Brand\";v=\"99\"");
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("origin", "https://www.yeezysupply.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.yeezysupply.com/payment");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest getPixel(String string) {
        this.pixelURI = string;
        HttpRequest httpRequest = this.client.getAbs(string).as(BodyCodec.string());
        httpRequest.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"99\", \"Google Chrome\";v=\"99\"");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "no-cors");
        httpRequest.putHeader("sec-fetch-dest", "script");
        httpRequest.putHeader("referer", "https://www.yeezysupply.com/");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public Buffer billingBody(Profile profile) {
        String string = "{\"customer\":{\"email\":\"" + profile.getEmail() + "\",\"receiveSmsUpdates\":false},\"shippingAddress\":{\"country\":\"US\",\"firstName\":\"" + profile.getFirstName().substring(0, 1).toUpperCase() + profile.getFirstName().substring(1).toLowerCase() + "\",\"lastName\":\"" + profile.getLastName().substring(0, 1).toUpperCase() + profile.getLastName().substring(1).toLowerCase() + "\",\"address1\":\"" + profile.getAddress1() + "\",\"address2\":\"" + profile.getAddress2() + "\",\"city\":\"" + profile.getCity().substring(0, 1).toUpperCase() + profile.getCity().substring(1).toLowerCase() + "\",\"stateCode\":\"" + profile.getState() + "\",\"zipcode\":\"" + profile.getZip() + "\",\"phoneNumber\":\"" + profile.getPhone() + "\"},\"billingAddress\":{\"country\":\"US\",\"firstName\":\"" + profile.getFirstName().substring(0, 1).toUpperCase() + profile.getFirstName().substring(1).toLowerCase() + "\",\"lastName\":\"" + profile.getLastName().substring(0, 1).toUpperCase() + profile.getLastName().substring(1).toLowerCase() + "\",\"address1\":\"" + profile.getAddress1() + "\",\"address2\":\"" + profile.getAddress2() + "\",\"city\":\"" + profile.getCity().substring(0, 1).toUpperCase() + profile.getCity().substring(1).toLowerCase() + "\",\"stateCode\":\"" + profile.getState() + "\",\"zipcode\":\"" + profile.getZip() + "\",\"phoneNumber\":\"" + profile.getPhone() + "\"},\"methodList\":[{\"id\":\"2ndDay-1\",\"shipmentId\":\"me\",\"collectionPeriod\":\"\",\"deliveryPeriod\":\"\"}],\"newsletterSubscription\":true}";
        return Buffer.buffer((String)string);
    }

    public JsonArray atcForm(String string) {
        JsonObject jsonObject = new JsonObject();
        String string2 = this.getSKU() + Sizes.getSize((String)string);
        jsonObject.put("product_id", (Object)this.getSKU());
        jsonObject.put("product_variation_sku", (Object)string2);
        jsonObject.put("productId", (Object)string2);
        jsonObject.put("quantity", (Object)1);
        jsonObject.put("size", (Object)string);
        jsonObject.put("displaySize", (Object)string);
        return new JsonArray().add((Object)jsonObject);
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    public static CompletableFuture async$init(YeezyAPI var0, int var1_1, HawkAPI var2_2, YeezyAPI var3_4, CompletableFuture var4_5, GaneshAPI var5_6, Throwable var6_7, int var7_8, Object var8_9) {
        switch (var7_8) {
            case 0: {
                var1_1 = 0;
                block9: while (var1_1++ < 350000) {
                    try {
                        if (var0.task.getMode().contains("2")) {
                            var2_2 /* !! */  = new HawkAPI();
                            var0.pixelAPI = var2_2 /* !! */ ;
                            v0 = var0;
                            v1 = var2_2 /* !! */ .updateUserAgent();
                            if (!v1.isDone()) {
                                var4_5 = v1;
                                var3_4 /* !! */  = v0;
                                return var4_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$init(io.trickle.task.sites.yeezy.YeezyAPI int io.trickle.task.antibot.impl.akamai.HawkAPI io.trickle.task.sites.yeezy.YeezyAPI java.util.concurrent.CompletableFuture io.trickle.task.antibot.impl.akamai.GaneshAPI java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((YeezyAPI)var0, (int)var1_1, (HawkAPI)var2_2 /* !! */ , (YeezyAPI)var3_4 /* !! */ , (CompletableFuture)var4_5, null, null, (int)1));
                            }
lbl15:
                            // 3 sources

                            while (true) {
                                v0.updateUserAgent((String)v1.join());
                                if (var0.userAgent == null) continue block9;
                                var0.utag = new Utag(var0.userAgent, var0.getSKU());
                                return CompletableFuture.completedFuture(true);
                            }
                        }
                        if (var0.task.getMode().contains("3")) {
                            var2_2 /* !! */  = new GaneshAPI();
                            var0.pixelAPI = var2_2 /* !! */ ;
                            v2 = var0;
                            v3 = var2_2 /* !! */ .updateUserAgent();
                            if (!v3.isDone()) {
                                var4_5 = v3;
                                var3_4 /* !! */  = v2;
                                return var4_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$init(io.trickle.task.sites.yeezy.YeezyAPI int io.trickle.task.antibot.impl.akamai.HawkAPI io.trickle.task.sites.yeezy.YeezyAPI java.util.concurrent.CompletableFuture io.trickle.task.antibot.impl.akamai.GaneshAPI java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((YeezyAPI)var0, (int)var1_1, null, (YeezyAPI)var3_4 /* !! */ , (CompletableFuture)var4_5, (GaneshAPI)var2_2 /* !! */ , null, (int)2));
                            }
lbl29:
                            // 3 sources

                            while (true) {
                                v2.updateUserAgent((String)v3.join());
                                if (var0.userAgent == null) continue block9;
                                var0.utag = new Utag(var0.userAgent, var0.getSKU());
                                return CompletableFuture.completedFuture(true);
                            }
                        }
                        v4 = var0;
                        v5 = VertxSingleton.INSTANCE.getLocalClient().fetchDeviceFromAPI("https://loudounchris.xyz/api/device/browserDevice.json?mobile=false&ak=true");
                        if (!v5.isDone()) {
                            var4_5 = v5;
                            var3_4 /* !! */  = v4;
                            return var4_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$init(io.trickle.task.sites.yeezy.YeezyAPI int io.trickle.task.antibot.impl.akamai.HawkAPI io.trickle.task.sites.yeezy.YeezyAPI java.util.concurrent.CompletableFuture io.trickle.task.antibot.impl.akamai.GaneshAPI java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((YeezyAPI)var0, (int)var1_1, null, (YeezyAPI)var3_4 /* !! */ , (CompletableFuture)var4_5, null, null, (int)3));
                        }
lbl40:
                        // 3 sources

                        while (true) {
                            v4.taskDevice = (JsonObject)v5.join();
                            if (var0.taskDevice == null || var0.taskDevice.isEmpty()) continue block9;
                            var0.bmak = new Bmak(var0.taskDevice);
                            var0.utag = new Utag(var0.bmak, var0.getSKU());
                            var0.updateUserAgent(var0.bmak.getUA());
                            var0.pixelAPI = new TrickleAPI(var0.taskDevice);
                            if (var0.bmak == null || var0.userAgent == null || var0.pixelAPI == null) continue block9;
                            return CompletableFuture.completedFuture(true);
                        }
                    }
                    catch (Throwable var2_3) {
                        v6 = VertxUtil.hardCodedSleep((long)15000L);
                        if (!v6.isDone()) {
                            var3_4 /* !! */  = v6;
                            return var3_4 /* !! */ .exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$init(io.trickle.task.sites.yeezy.YeezyAPI int io.trickle.task.antibot.impl.akamai.HawkAPI io.trickle.task.sites.yeezy.YeezyAPI java.util.concurrent.CompletableFuture io.trickle.task.antibot.impl.akamai.GaneshAPI java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((YeezyAPI)var0, (int)var1_1, null, null, (CompletableFuture)var3_4 /* !! */ , null, (Throwable)var2_3, (int)4));
                        }
lbl54:
                        // 3 sources

                        while (true) {
                            v6.join();
                            continue block9;
                            break;
                        }
                    }
                }
                return CompletableFuture.completedFuture(false);
            }
            case 1: {
                v0 = var3_4 /* !! */ ;
                v1 = var4_5;
                ** continue;
            }
            case 2: {
                v2 = var3_4 /* !! */ ;
                v3 = var4_5;
                var2_2 /* !! */  = var5_6;
                ** continue;
            }
            case 3: {
                v4 = var3_4 /* !! */ ;
                v5 = var4_5;
                ** continue;
            }
            case 4: {
                v6 = var4_5;
                var2_2 /* !! */  = var6_7;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public HttpRequest secondaryProdAPI() {
        HttpRequest httpRequest = this.client.getAbs("https://www.yeezysupply.com/api/products/" + this.getSKU()).as(BodyCodec.string());
        String string = this.getInstanaID();
        httpRequest.putHeader("x-instana-t", string);
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("x-instana-s", string);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"99\", \"Google Chrome\";v=\"99\"");
        httpRequest.putHeader("x-instana-l", "1,correlationType=web;correlationId=" + string);
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.yeezysupply.com/product/" + this.getSKU());
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public void enableWeightAdjust() {
        this.client.addInterceptor(this::lambda$enableWeightAdjust$0);
    }

    public void lambda$handlePush$2(HttpClientRequest httpClientRequest, String string) {
        Cookie cookie = ClientCookieDecoder.STRICT.decode(string);
        if (cookie == null) return;
        if (cookie.domain() == null) {
            cookie.setDomain(httpClientRequest.getHost());
        }
        super.getCookies().put(cookie);
    }

    public void lambda$handlePush$3(HttpClientRequest httpClientRequest, HttpClientResponse httpClientResponse) {
        List list = httpClientResponse.cookies();
        if (list == null) return;
        if (list.isEmpty()) return;
        list.forEach(arg_0 -> this.lambda$handlePush$2(httpClientRequest, arg_0));
    }

    public HttpRequest getPaymentMethod(String string, String string2) {
        HttpRequest httpRequest = this.client.getAbs("https://www.yeezysupply.com/api/checkout/baskets/" + string + "/payment_methods").as(BodyCodec.string());
        String string3 = this.getInstanaID();
        httpRequest.putHeader("x-instana-t", string3);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("x-instana-l", "1,correlationType=web;correlationId=" + string3);
        httpRequest.putHeader("x-instana-s", string3);
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("checkout-authorization", string2);
        httpRequest.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"95\", \"Chromium\";v=\"95\", \";Not A Brand\";v=\"99\"");
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.yeezysupply.com/payment");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest productPage(boolean bl) {
        HttpRequest httpRequest = this.client.getAbs(this.getSKU().equals("BY9611") ? "https://www.yeezysupply.com/archive/" + this.getSKU() : "https://www.yeezysupply.com/product/" + this.getSKU()).as(BodyCodec.string());
        if (bl) {
            httpRequest.putHeader("cache-control", "max-age=0");
        }
        httpRequest.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"99\", \"Google Chrome\";v=\"99\"");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        if (!bl) {
            httpRequest.putHeader("sec-fetch-user", "?1");
        }
        httpRequest.putHeader("sec-fetch-dest", "document");
        if (bl) {
            httpRequest.putHeader("referer", "https://www.yeezysupply.com/product/" + this.getSKU());
        } else {
            httpRequest.putHeader("referer", "https://www.yeezysupply.com/");
        }
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$getTrickleSensor(YeezyAPI var0, String var1_1, CompletableFuture var2_2, int var3_4, Object var4_6) {
        switch (var3_4) {
            case 0: {
                var0.bmak.updateDocumentUrl("https://www.yeezysupply.com/product/" + var0.getSKU());
                var1_1 = "";
                try {
                    var1_1 = var0.getCookies().getCookieValue("_abck");
                }
                catch (Exception var2_3) {
                    var2_3.printStackTrace();
                }
                switch (var0.sensorCounter++) {
                    case 0: {
                        return CompletableFuture.completedFuture(var0.bmak.genPageLoadSensor(var1_1));
                    }
                    case 1: {
                        return CompletableFuture.completedFuture(var0.bmak.genSecondSensor(var1_1));
                    }
                }
                v0 = var0.reInit();
                if (!v0.isDone()) {
                    var3_5 = v0;
                    return var3_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$getTrickleSensor(io.trickle.task.sites.yeezy.YeezyAPI java.lang.String java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((YeezyAPI)var0, (String)var1_1, (CompletableFuture)var3_5, (int)1));
                }
                ** GOTO lbl22
            }
            case 1: {
                v0 = var2_2;
lbl22:
                // 2 sources

                v0.join();
                var0.sensorCounter = 1;
                return CompletableFuture.completedFuture(var0.bmak.genPageLoadSensor(var1_1));
            }
        }
        throw new IllegalArgumentException();
    }

    public HttpRequest submitShippingAndBilling(String string, String string2) {
        HttpRequest httpRequest = this.client.patchAbs("https://www.yeezysupply.com/api/checkout/baskets/" + string).as(BodyCodec.string());
        String string3 = this.getInstanaID();
        httpRequest.putHeader("x-instana-t", string3);
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("x-instana-l", "1,correlationType=web;correlationId=" + string3);
        httpRequest.putHeader("x-instana-s", string3);
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("checkout-authorization", string2);
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"95\", \"Chromium\";v=\"95\", \";Not A Brand\";v=\"99\"");
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("origin", "https://www.yeezysupply.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.yeezysupply.com/delivery");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public YeezyAPI(Task task) {
        this.task = task;
        this.sensorCounter = 0;
        this.enablePush();
        this.enableWeightAdjust();
    }

    public void enablePush() {
        this.client.addInterceptor(this::lambda$enablePush$1);
    }

    public HttpRequest verifyPage() {
        HttpRequest httpRequest = this.client.getAbs("https://www.yeezysupply.com/_sec/cp_challenge/verify").as(BodyCodec.buffer());
        String string = this.getInstanaID();
        httpRequest.putHeader("x-instana-t", string);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("x-instana-l", "1,correlationType=web;correlationId=" + string);
        httpRequest.putHeader("x-sec-clge-req-type", "ajax");
        httpRequest.putHeader("x-instana-s", string);
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"95\", \"Chromium\";v=\"95\", \";Not A Brand\";v=\"99\"");
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.yeezysupply.com/product/" + this.getSKU());
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest bloom() {
        HttpRequest httpRequest = this.client.getAbs("https://www.yeezysupply.com/api/yeezysupply/products/bloom").as(BodyCodec.string());
        httpRequest.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"99\", \"Google Chrome\";v=\"99\"");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.yeezysupply.com/");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public String fetchUtag() {
        return this.utag.genUtagMain();
    }

    public HttpRequest getAdiSensor() {
        HttpRequest httpRequest = this.client.getAbs("https://www.adidas.com" + this.sensorUrl).as(BodyCodec.buffer());
        httpRequest.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"95\", \"Chromium\";v=\"95\", \";Not A Brand\";v=\"99\"");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "no-cors");
        httpRequest.putHeader("sec-fetch-dest", "script");
        httpRequest.putHeader("referer", "https://www.adidas.com/us/ultraboost-21-shoes/FY0378.html");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        httpRequest.putHeader("cookie", "DEFAULT_VALUE");
        return httpRequest;
    }

    public HttpRequest postSensor(boolean bl) {
        HttpRequest httpRequest = this.client.postAbs(this.sensorUrl).as(BodyCodec.buffer());
        String string = this.getInstanaID();
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("x-instana-t", string);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("x-instana-l", "1,correlationType=web;correlationId=" + string);
        httpRequest.putHeader("x-instana-s", string);
        httpRequest.putHeader("content-type", "text/plain;charset=UTF-8");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"99\", \"Google Chrome\";v=\"99\"");
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("origin", "https://www.yeezysupply.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", (String)(bl ? "https://www.yeezysupply.com/products/" + this.getSKU() : "https://www.yeezysupply.com/"));
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public void updateDocumentUrl(String string) {
        if (this.bmak != null) {
            this.bmak.updateDocumentUrl(string);
        }
        this.updateUtagUrl(string);
    }

    public void handlePush(HttpClientRequest httpClientRequest) {
        httpClientRequest.response().onSuccess(arg_0 -> this.lambda$handlePush$3(httpClientRequest, arg_0));
    }

    public HttpRequest postSensorAdi() {
        HttpRequest httpRequest = this.client.postAbs("https://www.adidas.com" + this.sensorUrl).as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("x-instana-t", "d7ef9c8b4030bf01");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("x-instana-l", "1,correlationType=web;correlationId=d7ef9c8b4030bf01");
        httpRequest.putHeader("content-type", "text/plain;charset=UTF-8");
        httpRequest.putHeader("x-sec-clge-req-type", "ajax");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("x-instana-s", "d7ef9c8b4030bf01");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"95\", \"Chromium\";v=\"95\", \";Not A Brand\";v=\"99\"");
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("origin", "https://www.adidas.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.adidas.com/us/ultraboost-21-shoes/FY0378.html");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest ushpl() {
        HttpRequest httpRequest = this.client.getAbs("https://www.yeezysupply.com/hpl/content/yeezy-supply/releases/" + this.getSKU() + "/en_US.json").as(BodyCodec.string());
        httpRequest.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"99\", \"Google Chrome\";v=\"99\"");
        httpRequest.putHeader("accept", "application/json, text/plain, */*");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.yeezysupply.com/product/" + this.getSKU());
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public CompletableFuture init() {
        int n = 0;
        while (n++ < 350000) {
            try {
                HawkAPI hawkAPI;
                if (this.task.getMode().contains("2")) {
                    hawkAPI = new HawkAPI();
                    this.pixelAPI = hawkAPI;
                    YeezyAPI yeezyAPI = this;
                    CompletableFuture completableFuture = hawkAPI.updateUserAgent();
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture2 = completableFuture;
                        YeezyAPI yeezyAPI2 = yeezyAPI;
                        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> YeezyAPI.async$init(this, n, hawkAPI, yeezyAPI2, completableFuture2, null, null, 1, arg_0));
                    }
                    yeezyAPI.updateUserAgent((String)completableFuture.join());
                    if (this.userAgent == null) continue;
                    this.utag = new Utag(this.userAgent, this.getSKU());
                    return CompletableFuture.completedFuture(true);
                }
                if (this.task.getMode().contains("3")) {
                    hawkAPI = new GaneshAPI();
                    this.pixelAPI = hawkAPI;
                    YeezyAPI yeezyAPI = this;
                    CompletableFuture completableFuture = hawkAPI.updateUserAgent();
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture3 = completableFuture;
                        YeezyAPI yeezyAPI3 = yeezyAPI;
                        return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> YeezyAPI.async$init(this, n, null, yeezyAPI3, completableFuture3, (GaneshAPI)hawkAPI, null, 2, arg_0));
                    }
                    yeezyAPI.updateUserAgent((String)completableFuture.join());
                    if (this.userAgent == null) continue;
                    this.utag = new Utag(this.userAgent, this.getSKU());
                    return CompletableFuture.completedFuture(true);
                }
                CompletableFuture completableFuture = VertxSingleton.INSTANCE.getLocalClient().fetchDeviceFromAPI("https://loudounchris.xyz/api/device/browserDevice.json?mobile=false&ak=true");
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture;
                    YeezyAPI yeezyAPI = this;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> YeezyAPI.async$init(this, n, null, yeezyAPI, completableFuture4, null, null, 3, arg_0));
                }
                this.taskDevice = (JsonObject)completableFuture.join();
                if (this.taskDevice == null || this.taskDevice.isEmpty()) continue;
                this.bmak = new Bmak(this.taskDevice);
                this.utag = new Utag(this.bmak, this.getSKU());
                this.updateUserAgent(this.bmak.getUA());
                this.pixelAPI = new TrickleAPI(this.taskDevice);
                if (this.bmak == null || this.userAgent == null || this.pixelAPI == null) continue;
                return CompletableFuture.completedFuture(true);
            }
            catch (Throwable throwable) {
                CompletableFuture completableFuture = VertxUtil.hardCodedSleep((long)15000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture5 = completableFuture;
                    return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> YeezyAPI.async$init(this, n, null, null, completableFuture5, null, throwable, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(false);
    }

    public HttpRequest newsletter(String string) {
        HttpRequest httpRequest = this.client.getAbs("https://www.yeezysupply.com/api/signup/" + string + "?trigger=").as(BodyCodec.buffer());
        String string2 = this.getInstanaID();
        httpRequest.putHeader("x-instana-t", string2);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("x-instana-l", "1,correlationType=web;correlationId=" + string2);
        httpRequest.putHeader("x-instana-s", string2);
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"99\", \"Google Chrome\";v=\"99\"");
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.yeezysupply.com/product/" + this.getSKU());
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public CompletableFuture getTrickleSensor() {
        this.bmak.updateDocumentUrl("https://www.yeezysupply.com/product/" + this.getSKU());
        String string = "";
        try {
            string = this.getCookies().getCookieValue("_abck");
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        switch (this.sensorCounter++) {
            case 0: {
                return CompletableFuture.completedFuture(this.bmak.genPageLoadSensor(string));
            }
            case 1: {
                return CompletableFuture.completedFuture(this.bmak.genSecondSensor(string));
            }
        }
        CompletableFuture completableFuture = this.reInit();
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> YeezyAPI.async$getTrickleSensor(this, string, completableFuture2, 1, arg_0));
        }
        completableFuture.join();
        this.sensorCounter = 1;
        return CompletableFuture.completedFuture(this.bmak.genPageLoadSensor(string));
    }

    public void updateUserAgent(String string) {
        this.userAgent = string;
        try {
            ((NetClientImpl)((HttpClientImpl)this.client.getClient()).getNetClient()).getOptions().setConnectUserAgent(this.userAgent);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public JsonObject couponForm(String string) {
        return new JsonObject().put("couponCode", (Object)string);
    }

    public HttpRequest submitShippingAndBillingSpecial(String string, String string2) {
        int n = ThreadLocalRandom.current().nextInt(1, 6000);
        HttpRequest httpRequest = this.getCookies().getAll().iterator();
        while (true) {
            Object object;
            if (!httpRequest.hasNext()) {
                httpRequest = this.client.patchAbs("https://a" + n + ".g.akamai.net/api/checkout/baskets/" + string).as(BodyCodec.string());
                object = this.getInstanaID();
                httpRequest.putHeader("host", "www.yeezysupply.com");
                httpRequest.putHeader("x-instana-t", (String)object);
                httpRequest.putHeader("content-length", "DEFAULT_VALUE");
                httpRequest.putHeader("sec-ch-ua-mobile", "?0");
                httpRequest.putHeader("user-agent", this.userAgent);
                httpRequest.putHeader("x-instana-l", "1,correlationType=web;correlationId=" + (String)object);
                httpRequest.putHeader("x-instana-s", (String)object);
                httpRequest.putHeader("content-type", "application/json");
                httpRequest.putHeader("checkout-authorization", string2);
                httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
                httpRequest.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"95\", \"Chromium\";v=\"95\", \";Not A Brand\";v=\"99\"");
                httpRequest.putHeader("accept", "*/*");
                httpRequest.putHeader("origin", "https://www.yeezysupply.com");
                httpRequest.putHeader("sec-fetch-site", "same-origin");
                httpRequest.putHeader("sec-fetch-mode", "cors");
                httpRequest.putHeader("sec-fetch-dest", "empty");
                httpRequest.putHeader("referer", "https://www.yeezysupply.com/delivery");
                httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
                httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
                return httpRequest;
            }
            object = (CookieJar.ExpirableCookie)httpRequest.next();
            Cookie cookie = object.getWrappedCookie();
            if (cookie.domain() != null && !cookie.domain().isBlank()) {
                cookie.setDomain("a" + n + ".g.akamai.net");
            }
            this.getCookies().put(object.getWrappedCookie());
        }
    }

    public void lambda$enablePush$1(HttpContext httpContext) {
        if (httpContext.phase().equals((Object)ClientPhase.SEND_REQUEST) && httpContext.clientRequest() != null) {
            httpContext.clientRequest().pushHandler(this::handlePush);
        }
        httpContext.next();
    }

    public HttpRequest adiAtc() {
        HttpRequest httpRequest = this.client.postAbs("https://www.adidas.com/api/chk/baskets/-/items?sitePath=us").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("x-instana-t", "c6953e7ece2d4c44");
        httpRequest.putHeader("glassversion", "db136f4");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("x-instana-l", "1,correlationType=web;correlationId=c6953e7ece2d4c44");
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("x-sec-clge-req-type", "ajax");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("x-instana-s", "c6953e7ece2d4c44");
        httpRequest.putHeader("checkout-authorization", "null");
        httpRequest.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"95\", \"Chromium\";v=\"95\", \";Not A Brand\";v=\"99\"");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("origin", "https://www.adidas.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.adidas.com/us/ultraboost-21-shoes/FY0378.html");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        httpRequest.putHeader("cookie", "DEFAULT_VALUE");
        return httpRequest;
    }

    public Pixel getPixelAPI() {
        return this.pixelAPI;
    }

    public HttpRequest adiProductPage() {
        HttpRequest httpRequest = this.client.getAbs("https://www.adidas.com/us/ultraboost-21-shoes/FY0378.html").as(BodyCodec.buffer());
        httpRequest.putHeader("cache-control", "max-age=0");
        httpRequest.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"95\", \"Chromium\";v=\"95\", \";Not A Brand\";v=\"99\"");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "none");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        httpRequest.putHeader("cookie", "DEFAULT_VALUE");
        return httpRequest;
    }

    public HttpRequest auth3DS(String string) {
        HttpRequest httpRequest = this.client.postAbs(string).as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("cache-control", "no-cache");
        httpRequest.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"90\", \"Chromium\";v=\"90\", \";Not A Brand\";v=\"99\"");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("origin", "https://www.yeezysupply.com");
        httpRequest.putHeader("content-type", "application/x-www-form-urlencoded");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "cross-site");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("referer", "https://www.yeezysupply.com/");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        httpRequest.putHeader("pragma", "no-cache");
        return httpRequest;
    }

    public JsonArray atcForm(JsonObject jsonObject) {
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.put("product_id", (Object)this.getSKU());
        jsonObject2.put("product_variation_sku", (Object)jsonObject.getString("sku"));
        jsonObject2.put("productId", (Object)jsonObject.getString("sku"));
        jsonObject2.put("quantity", (Object)1);
        jsonObject2.put("size", (Object)jsonObject.getString("size"));
        jsonObject2.put("displaySize", (Object)jsonObject.getString("size"));
        return new JsonArray().add((Object)jsonObject2);
    }

    public HttpRequest addToCart() {
        HttpRequest httpRequest = this.client.postAbs("https://www.yeezysupply.com/api/checkout/baskets/-/items").as(BodyCodec.string());
        String string = this.getInstanaID();
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("x-instana-t", string);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("x-instana-l", "1,correlationType=web;correlationId=" + string);
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("x-instana-s", string);
        httpRequest.putHeader("checkout-authorization", "null");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"99\", \"Google Chrome\";v=\"99\"");
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("origin", "https://www.yeezysupply.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.yeezysupply.com/product/" + this.getSKU());
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest homePage() {
        HttpRequest httpRequest = this.client.getAbs("https://www.yeezysupply.com/").as(BodyCodec.string());
        httpRequest.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"99\", \"Google Chrome\";v=\"99\"");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "none");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest atcSpecial() {
        int n = ThreadLocalRandom.current().nextInt(1, 6000);
        HttpRequest httpRequest = this.getCookies().getAll().iterator();
        while (true) {
            Object object;
            if (!httpRequest.hasNext()) {
                httpRequest = this.client.postAbs("https://a" + n + ".g.akamai.net/api/checkout/baskets/-/items").as(BodyCodec.string());
                object = this.getInstanaID();
                httpRequest.putHeader("host", "www.yeezysupply.com");
                httpRequest.putHeader("content-length", "DEFAULT_VALUE");
                httpRequest.putHeader("x-instana-t", (String)object);
                httpRequest.putHeader("sec-ch-ua-mobile", "?0");
                httpRequest.putHeader("x-instana-l", "1,correlationType=web;correlationId=" + (String)object);
                httpRequest.putHeader("content-type", "application/json");
                httpRequest.putHeader("user-agent", this.userAgent);
                httpRequest.putHeader("x-instana-s", (String)object);
                httpRequest.putHeader("checkout-authorization", "null");
                httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
                httpRequest.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"99\", \"Google Chrome\";v=\"99\"");
                httpRequest.putHeader("accept", "*/*");
                httpRequest.putHeader("origin", "https://www.yeezysupply.com");
                httpRequest.putHeader("sec-fetch-site", "same-origin");
                httpRequest.putHeader("sec-fetch-mode", "cors");
                httpRequest.putHeader("sec-fetch-dest", "empty");
                httpRequest.putHeader("referer", "https://www.yeezysupply.com/product/" + this.getSKU());
                httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
                httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
                return httpRequest;
            }
            object = (CookieJar.ExpirableCookie)httpRequest.next();
            Cookie cookie = object.getWrappedCookie();
            if (cookie.domain() != null && !cookie.domain().isBlank()) {
                cookie.setDomain("a" + n + ".g.akamai.net");
            }
            this.getCookies().put(object.getWrappedCookie());
        }
    }

    public HttpRequest emptyBasket(boolean bl, String string) {
        HttpRequest httpRequest = this.client.getAbs("https://www.yeezysupply.com/api/checkout/customer/baskets").as(BodyCodec.none());
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("checkout-authorization", string);
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", (String)(bl ? "https://www.yeezysupply.com/product/" + this.getSKU() : "https://www.yeezysupply.com/"));
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public Buffer atcFormAdidas() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("product_id", (Object)"FY0378");
        jsonObject.put("quantity", (Object)1);
        jsonObject.put("product_variation_sku", (Object)"FY0378_610");
        jsonObject.put("productId", (Object)"FY0378_610");
        jsonObject.put("size", (Object)"8");
        jsonObject.put("displaySize", (Object)"8");
        jsonObject.put("specialLaunchProduct", (Object)false);
        return Buffer.buffer((String)new JsonArray().add((Object)jsonObject).toString());
    }

    public String getInstanaID() {
        String string = "abcdef0123456789";
        StringBuilder stringBuilder = new StringBuilder();
        while (stringBuilder.length() < 16) {
            int n = (int)(ThreadLocalRandom.current().nextFloat() * (float)"abcdef0123456789".length());
            stringBuilder.append("abcdef0123456789".charAt(n));
        }
        return stringBuilder.toString();
    }

    public void setSensorUrl(String string) {
        this.sensorUrl = string;
    }

    public HttpRequest handleAfter3DS(String string, String string2, String string3) {
        HttpRequest httpRequest = this.client.postAbs("https://www.yeezysupply.com/api/checkout/payment-verification/" + string).as(BodyCodec.buffer());
        String string4 = this.getInstanaID();
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("x-instana-t", string4);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("x-instana-l", "1,correlationType=web;correlationId=" + string4);
        httpRequest.putHeader("x-instana-s", string4);
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("checkout-authorization", string2);
        httpRequest.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"95\", \"Chromium\";v=\"95\", \";Not A Brand\";v=\"99\"");
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("origin", "https://www.yeezysupply.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", string3);
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        httpRequest.putHeader("cookie", "DEFAULT_VALUE");
        return httpRequest;
    }

    public HttpRequest postPixel(String string) {
        HttpRequest httpRequest = this.client.postAbs(string).as(BodyCodec.none());
        String string2 = this.getInstanaID();
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("x-instana-t", string2);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("x-instana-l", "1,correlationType=web;correlationId=" + string2);
        httpRequest.putHeader("x-instana-s", string2);
        httpRequest.putHeader("content-type", "application/x-www-form-urlencoded");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"99\", \"Google Chrome\";v=\"99\"");
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("origin", "https://www.yeezysupply.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.yeezysupply.com/");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public String getSKU() {
        return this.task.getKeywords()[0].trim();
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    public static CompletableFuture async$reInit(YeezyAPI var0, int var1_1, YeezyAPI var2_2, CompletableFuture var3_4, Throwable var4_5, int var5_6, Object var6_7) {
        switch (var5_6) {
            case 0: {
                var1_1 = 0;
                block7: while (var1_1++ < 350000) {
                    try {
                        if (var0.task.getMode().contains("2")) {
                            return CompletableFuture.completedFuture(true);
                        }
                        if (var0.task.getMode().contains("3")) {
                            return CompletableFuture.completedFuture(true);
                        }
                        v0 = var0;
                        v1 = VertxSingleton.INSTANCE.getLocalClient().fetchDeviceFromAPI("https://loudounchris.xyz/api/device/browserDeviceQueried.json?mobile=false&ak=true", var0.userAgent);
                        if (!v1.isDone()) {
                            var4_5 = v1;
                            var3_4 /* !! */  = v0;
                            return var4_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$reInit(io.trickle.task.sites.yeezy.YeezyAPI int io.trickle.task.sites.yeezy.YeezyAPI java.util.concurrent.CompletableFuture java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((YeezyAPI)var0, (int)var1_1, (YeezyAPI)var3_4 /* !! */ , (CompletableFuture)var4_5, null, (int)1));
                        }
lbl16:
                        // 3 sources

                        while (true) {
                            v0.taskDevice = (JsonObject)v1.join();
                            if (var0.taskDevice == null || var0.taskDevice.isEmpty()) continue block7;
                            var0.bmak = new Bmak(var0.taskDevice);
                            var0.pixelAPI = new TrickleAPI(var0.taskDevice);
                            if (var0.bmak == null || var0.userAgent == null) continue block7;
                            return CompletableFuture.completedFuture(true);
                        }
                    }
                    catch (Throwable var2_3) {
                        v2 = VertxUtil.hardCodedSleep((long)15000L);
                        if (!v2.isDone()) {
                            var3_4 /* !! */  = v2;
                            return var3_4 /* !! */ .exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$reInit(io.trickle.task.sites.yeezy.YeezyAPI int io.trickle.task.sites.yeezy.YeezyAPI java.util.concurrent.CompletableFuture java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((YeezyAPI)var0, (int)var1_1, null, (CompletableFuture)var3_4 /* !! */ , (Throwable)var2_3, (int)2));
                        }
lbl28:
                        // 3 sources

                        while (true) {
                            v2.join();
                            continue block7;
                            break;
                        }
                    }
                }
                return CompletableFuture.completedFuture(false);
            }
            case 1: {
                v0 = var2_2 /* !! */ ;
                v1 = var3_4 /* !! */ ;
                ** continue;
            }
            case 2: {
                v2 = var3_4 /* !! */ ;
                var2_2 /* !! */  = var4_5;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public void setUtagProductName(String string) {
        this.utag.setName(string);
    }

    public HttpRequest queue() {
        HttpRequest httpRequest = this.client.getAbs(QUEUE_URL).as(BodyCodec.none());
        httpRequest.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"99\", \"Google Chrome\";v=\"99\"");
        httpRequest.putHeader("accept", "application/json, text/plain, */*");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.yeezysupply.com/product/" + this.getSKU());
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public void updateUtagUrl(String string) {
        this.utag.updateDocumentUrl(string);
    }

    public JsonArray atcForm() {
        Sizes.SizePair sizePair = Sizes.getSize((String)this.task.getSize());
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("product_id", (Object)this.getSKU());
        jsonObject.put("product_variation_sku", (Object)(this.getSKU() + sizePair));
        jsonObject.put("productId", (Object)(this.getSKU() + sizePair));
        jsonObject.put("quantity", (Object)1);
        jsonObject.put("size", (Object)sizePair.sizeNum);
        jsonObject.put("displaySize", (Object)sizePair.sizeNum);
        return new JsonArray().add((Object)jsonObject);
    }

    public boolean rotateProxy() {
        if (!super.rotateProxy()) return false;
        this.enablePush();
        return true;
    }

    public HttpRequest sharedHpl() {
        HttpRequest httpRequest = this.client.getAbs("https://www.yeezysupply.com/hpl/content/yeezy-supply/releases/" + this.getSKU() + "/shared.json").as(BodyCodec.string());
        httpRequest.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"99\", \"Google Chrome\";v=\"99\"");
        httpRequest.putHeader("accept", "application/json, text/plain, */*");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.yeezysupply.com/product/" + this.getSKU());
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest postSensorSpecial(boolean bl) {
        Object object2;
        int n = ThreadLocalRandom.current().nextInt(1, 6000);
        for (Object object2 : this.getCookies().getAll()) {
            Cookie cookie = object2.getWrappedCookie();
            if (cookie.domain() != null && !cookie.domain().isBlank()) {
                cookie.setDomain("a" + n + ".g.akamai.net");
            }
            this.getCookies().put(object2.getWrappedCookie());
        }
        HttpRequest httpRequest = this.client.postAbs("https://a" + n + ".g.akamai.net" + this.sensorUrl.replace("https://www.yeezysupply.com", "")).as(BodyCodec.buffer());
        object2 = this.getInstanaID();
        httpRequest.putHeader("host", "www.yeezysupply.com");
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("x-instana-t", (String)object2);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("x-instana-l", "1,correlationType=web;correlationId=" + (String)object2);
        httpRequest.putHeader("x-instana-s", (String)object2);
        httpRequest.putHeader("content-type", "text/plain;charset=UTF-8");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"99\", \"Google Chrome\";v=\"99\"");
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("origin", "https://www.yeezysupply.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", (String)(bl ? "https://www.yeezysupply.com/products/" + this.getSKU() : "https://www.yeezysupply.com/"));
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest waitingRoomConfig() {
        HttpRequest httpRequest = this.client.getAbs("https://www.yeezysupply.com/hpl/content/yeezy-supply/config/US/waitingRoomConfig.json").as(BodyCodec.string());
        httpRequest.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"99\", \"Google Chrome\";v=\"99\"");
        httpRequest.putHeader("accept", "application/json, text/plain, */*");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.yeezysupply.com/product/" + this.getSKU());
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public CompletableFuture reInit() {
        int n = 0;
        while (n++ < 350000) {
            try {
                if (this.task.getMode().contains("2")) {
                    return CompletableFuture.completedFuture(true);
                }
                if (this.task.getMode().contains("3")) {
                    return CompletableFuture.completedFuture(true);
                }
                CompletableFuture completableFuture = VertxSingleton.INSTANCE.getLocalClient().fetchDeviceFromAPI("https://loudounchris.xyz/api/device/browserDeviceQueried.json?mobile=false&ak=true", this.userAgent);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    YeezyAPI yeezyAPI = this;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> YeezyAPI.async$reInit(this, n, yeezyAPI, completableFuture2, null, 1, arg_0));
                }
                this.taskDevice = (JsonObject)completableFuture.join();
                if (this.taskDevice == null || this.taskDevice.isEmpty()) continue;
                this.bmak = new Bmak(this.taskDevice);
                this.pixelAPI = new TrickleAPI(this.taskDevice);
                if (this.bmak == null || this.userAgent == null) continue;
                return CompletableFuture.completedFuture(true);
            }
            catch (Throwable throwable) {
                CompletableFuture completableFuture = VertxUtil.hardCodedSleep((long)15000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture3 = completableFuture;
                    return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> YeezyAPI.async$reInit(this, n, null, completableFuture3, throwable, 2, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(false);
    }

    public HttpRequest submitCoupon(String string, String string2) {
        HttpRequest httpRequest = this.client.postAbs("https://www.yeezysupply.com/api/checkout/baskets/" + string + "/coupons/").as(BodyCodec.buffer());
        String string3 = this.getInstanaID();
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("x-instana-t", string3);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("x-instana-l", "1,correlationType=web;correlationId=" + string3);
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("x-instana-s", string3);
        httpRequest.putHeader("checkout-authorization", string2);
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"95\", \"Chromium\";v=\"95\", \";Not A Brand\";v=\"99\"");
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("origin", "https://www.yeezysupply.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.yeezysupply.com/payment");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest getShippingMethod(String string, String string2) {
        HttpRequest httpRequest = this.client.getAbs("https://www.yeezysupply.com/api/checkout/baskets/" + string + "/shipping_methods").as(BodyCodec.string());
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("checkout-authorization", string2);
        httpRequest.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"99\", \"Google Chrome\";v=\"99\"");
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.yeezysupply.com/delivery");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest abckFromStore(String string, String string2) {
        return this.client.getAbs("https://sensor-store-oygn7nn37q-uc.a.run.app/store/get.json").addQueryParam("os", string).addQueryParam("version", string2).as(BodyCodec.jsonObject());
    }

    public HttpRequest powPage(String string) {
        HttpRequest httpRequest = this.client.getAbs(string).as(BodyCodec.buffer());
        httpRequest.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"95\", \"Chromium\";v=\"95\", \";Not A Brand\";v=\"99\"");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-dest", "iframe");
        httpRequest.putHeader("referer", "https://www.yeezysupply.com/product/" + this.getSKU());
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        httpRequest.putHeader("cookie", "DEFAULT_VALUE");
        return httpRequest;
    }

    public HttpRequest processPaymentSpecial(String string) {
        int n = ThreadLocalRandom.current().nextInt(1, 6000);
        HttpRequest httpRequest = this.getCookies().getAll().iterator();
        while (true) {
            Object object;
            if (!httpRequest.hasNext()) {
                httpRequest = this.client.postAbs("https://a" + n + ".g.akamai.net/api/checkout/orders").as(BodyCodec.string());
                object = this.getInstanaID();
                httpRequest.putHeader("host", "www.yeezysupply.com");
                httpRequest.putHeader("content-length", "DEFAULT_VALUE");
                httpRequest.putHeader("x-instana-t", (String)object);
                httpRequest.putHeader("sec-ch-ua-mobile", "?0");
                httpRequest.putHeader("user-agent", this.userAgent);
                httpRequest.putHeader("x-instana-l", "1,correlationType=web;correlationId=" + (String)object);
                httpRequest.putHeader("x-instana-s", (String)object);
                httpRequest.putHeader("content-type", "application/json");
                httpRequest.putHeader("checkout-authorization", string);
                httpRequest.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"95\", \"Chromium\";v=\"95\", \";Not A Brand\";v=\"99\"");
                httpRequest.putHeader("accept", "*/*");
                httpRequest.putHeader("origin", "https://www.yeezysupply.com");
                httpRequest.putHeader("sec-fetch-site", "same-origin");
                httpRequest.putHeader("sec-fetch-mode", "cors");
                httpRequest.putHeader("sec-fetch-dest", "empty");
                httpRequest.putHeader("referer", "https://www.yeezysupply.com/payment");
                httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
                httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
                return httpRequest;
            }
            object = (CookieJar.ExpirableCookie)httpRequest.next();
            Cookie cookie = object.getWrappedCookie();
            if (cookie.domain() != null && !cookie.domain().isBlank()) {
                cookie.setDomain("a" + n + ".g.akamai.net");
            }
            this.getCookies().put(object.getWrappedCookie());
        }
    }
}
