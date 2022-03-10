/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.MultiMap
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.client.HttpResponse
 *  io.vertx.ext.web.client.WebClient
 *  io.vertx.ext.web.codec.BodyCodec
 */
package io.trickle.task.antibot.impl.akamai;

import io.trickle.core.VertxSingleton;
import io.trickle.task.antibot.impl.akamai.pixel.Pixel;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.vertx.core.MultiMap;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import java.lang.invoke.LambdaMetafactory;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class GaneshAPI
implements Pixel {
    public static String[] api_ua = new String[]{"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.80 Safari/537.36", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.51 Safari/537.36", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.82 Safari/537.36", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.82 Safari/537.36", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36"};
    public String useragent = null;

    public String getUseragent() {
        return this.useragent;
    }

    public MultiMap getPixelForm(String string, String string2) {
        return MultiMap.caseInsensitiveMultiMap().set("scriptVal", string).set("key", "dwayn-hrrth56JH%^JNHRTTHtjrtj56jhrthrtwhrthr").set("mode", "PIXEL").set("ua", this.useragent).set("pixelID", string2);
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    public static CompletableFuture async$getPixelReqString(GaneshAPI var0, String var1_1, String var2_2, String var3_3, HttpRequest var4_4, MultiMap var5_5, int var6_6, CompletableFuture var7_7, HttpResponse var8_9, int var9_10, Object var10_13) {
        switch (var9_10) {
            case 0: {
                var4_4 = var0.ganeshAPI();
                var5_5 = var0.getPixelForm(var3_3, var1_1);
                var6_6 = 0;
                while (var6_6++ <= 1000) {
                    try {
                        v0 = Request.send(var4_4, var5_5);
                        if (!v0.isDone()) {
                            var9_11 = v0;
                            return var9_11.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$getPixelReqString(io.trickle.task.antibot.impl.akamai.GaneshAPI java.lang.String java.lang.String java.lang.String io.vertx.ext.web.client.HttpRequest io.vertx.core.MultiMap int java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((GaneshAPI)var0, (String)var1_1, (String)var2_2, (String)var3_3, (HttpRequest)var4_4, (MultiMap)var5_5, (int)var6_6, (CompletableFuture)var9_11, null, (int)1));
                        }
lbl12:
                        // 3 sources

                        while (true) {
                            var7_7 = (HttpResponse)v0.join();
                            if (var7_7 != null) {
                                var8_9 /* !! */  = var7_7.bodyAsString().split("\\*")[0];
                                return CompletableFuture.completedFuture(var8_9 /* !! */ );
                            }
                            v1 = VertxUtil.randomSleep(10000L);
                            if (!v1.isDone()) {
                                var9_12 = v1;
                                return var9_12.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$getPixelReqString(io.trickle.task.antibot.impl.akamai.GaneshAPI java.lang.String java.lang.String java.lang.String io.vertx.ext.web.client.HttpRequest io.vertx.core.MultiMap int java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((GaneshAPI)var0, (String)var1_1, (String)var2_2, (String)var3_3, (HttpRequest)var4_4, (MultiMap)var5_5, (int)var6_6, (CompletableFuture)var9_12, (HttpResponse)var7_7, (int)2));
                            }
lbl21:
                            // 3 sources

                            while (true) {
                                v1.join();
                                break;
                            }
                            break;
                        }
                    }
                    catch (Exception var7_8) {
                        if (var7_8.getMessage().toLowerCase().contains("ganesh")) continue;
                        System.out.println("API[G] err: " + var7_8.getMessage());
                    }
                }
                return CompletableFuture.completedFuture(null);
            }
            case 1: {
                v0 = var7_7;
                ** continue;
            }
            case 2: {
                v1 = var7_7;
                var7_7 = var8_9 /* !! */ ;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public WebClient client() {
        return VertxSingleton.INSTANCE.getLocalClient().getClient();
    }

    @Override
    public CompletableFuture getPixelReqForm(String string, String string2, String string3) {
        return CompletableFuture.failedFuture(new Exception("Unsupported method"));
    }

    public MultiMap getSensorForm(String string, String string2, String string3, boolean bl) {
        MultiMap multiMap = MultiMap.caseInsensitiveMultiMap().set("site", "https://www.yeezysupply.com/products/" + string3).set("key", "dwayn-hrrth56JH%^JNHRTTHtjrtj56jhrthrtwhrthr").set("mode", "API").set("ua", this.useragent).set("abck", string).set("bmsz", string2);
        if (bl) return multiMap;
        multiMap.set("events", "false");
        return multiMap;
    }

    public CompletableFuture updateUserAgent() {
        this.useragent = api_ua[ThreadLocalRandom.current().nextInt(api_ua.length)];
        return CompletableFuture.completedFuture(this.useragent);
    }

    public CompletableFuture getSensorPayload(String string, String string2, String string3, boolean bl) {
        HttpRequest httpRequest = this.ganeshAPI();
        MultiMap multiMap = this.getSensorForm(string, string2, string3, bl);
        int n = 0;
        while (n++ <= 1000) {
            try {
                CompletableFuture completableFuture = Request.send(httpRequest, multiMap);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> GaneshAPI.async$getSensorPayload(this, string, string2, string3, (int)(bl ? 1 : 0), httpRequest, multiMap, n, completableFuture2, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse != null) {
                    String string4 = GaneshAPI.decode(httpResponse.bodyAsString("UTF-8"));
                    return CompletableFuture.completedFuture(string4);
                }
                CompletableFuture completableFuture3 = VertxUtil.randomSleep(10000L);
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> GaneshAPI.async$getSensorPayload(this, string, string2, string3, (int)(bl ? 1 : 0), httpRequest, multiMap, n, completableFuture4, httpResponse, 2, arg_0));
                }
                completableFuture3.join();
            }
            catch (Exception exception) {}
        }
        return CompletableFuture.completedFuture(null);
    }

    public static String decode(String string) {
        String string2 = "56h56hERH%^\u00a3H$%H%\u00a3^YHGTERherthrtwh";
        StringBuilder stringBuilder = new StringBuilder();
        int n = 0;
        while (n < string.length()) {
            stringBuilder.append((char)(string.charAt(n) ^ string2.charAt(n % string2.length())));
            ++n;
        }
        return stringBuilder.toString();
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    public static CompletableFuture async$getSensorPayload(GaneshAPI var0, String var1_1, String var2_2, String var3_3, int var4_4, HttpRequest var5_5, MultiMap var6_6, int var7_7, CompletableFuture var8_8, HttpResponse var9_10, int var10_11, Object var11_14) {
        switch (var10_11) {
            case 0: {
                var5_5 = var0.ganeshAPI();
                var6_6 = var0.getSensorForm(var1_1, var2_2, var3_3, (boolean)var4_4);
                var7_7 = 0;
                while (var7_7++ <= 1000) {
                    try {
                        v0 = Request.send(var5_5, var6_6);
                        if (!v0.isDone()) {
                            var10_12 = v0;
                            return var10_12.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$getSensorPayload(io.trickle.task.antibot.impl.akamai.GaneshAPI java.lang.String java.lang.String java.lang.String int io.vertx.ext.web.client.HttpRequest io.vertx.core.MultiMap int java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((GaneshAPI)var0, (String)var1_1, (String)var2_2, (String)var3_3, (int)var4_4, (HttpRequest)var5_5, (MultiMap)var6_6, (int)var7_7, (CompletableFuture)var10_12, null, (int)1));
                        }
lbl12:
                        // 3 sources

                        while (true) {
                            var8_8 = (HttpResponse)v0.join();
                            if (var8_8 != null) {
                                var9_10 /* !! */  = GaneshAPI.decode(var8_8.bodyAsString("UTF-8"));
                                return CompletableFuture.completedFuture(var9_10 /* !! */ );
                            }
                            v1 = VertxUtil.randomSleep(10000L);
                            if (!v1.isDone()) {
                                var10_13 = v1;
                                return var10_13.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$getSensorPayload(io.trickle.task.antibot.impl.akamai.GaneshAPI java.lang.String java.lang.String java.lang.String int io.vertx.ext.web.client.HttpRequest io.vertx.core.MultiMap int java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((GaneshAPI)var0, (String)var1_1, (String)var2_2, (String)var3_3, (int)var4_4, (HttpRequest)var5_5, (MultiMap)var6_6, (int)var7_7, (CompletableFuture)var10_13, (HttpResponse)var8_8, (int)2));
                            }
lbl21:
                            // 3 sources

                            while (true) {
                                v1.join();
                                break;
                            }
                            break;
                        }
                    }
                    catch (Exception var8_9) {}
                }
                return CompletableFuture.completedFuture(null);
            }
            case 1: {
                v0 = var8_8;
                ** continue;
            }
            case 2: {
                v1 = var8_8;
                var8_8 = var9_10 /* !! */ ;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public void setUseragent(String string) {
        this.useragent = string;
    }

    public HttpRequest ganeshAPI() {
        return this.client().postAbs("https://akam-b429.ganeshbot.cloud/Akamai").putHeader("accept", "*/*").putHeader("accept-encoding", "gzip, deflate, br").putHeader("content-type", "application/x-www-form-urlencoded").timeout(50000L).as(BodyCodec.buffer());
    }

    @Override
    public CompletableFuture getPixelReqString(String string, String string2, String string3) {
        HttpRequest httpRequest = this.ganeshAPI();
        MultiMap multiMap = this.getPixelForm(string3, string);
        int n = 0;
        while (n++ <= 1000) {
            try {
                CompletableFuture completableFuture = Request.send(httpRequest, multiMap);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> GaneshAPI.async$getPixelReqString(this, string, string2, string3, httpRequest, multiMap, n, completableFuture2, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse != null) {
                    String string4 = httpResponse.bodyAsString().split("\\*")[0];
                    return CompletableFuture.completedFuture(string4);
                }
                CompletableFuture completableFuture3 = VertxUtil.randomSleep(10000L);
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> GaneshAPI.async$getPixelReqString(this, string, string2, string3, httpRequest, multiMap, n, completableFuture4, httpResponse, 2, arg_0));
                }
                completableFuture3.join();
            }
            catch (Exception exception) {
                if (exception.getMessage().toLowerCase().contains("ganesh")) continue;
                System.out.println("API[G] err: " + exception.getMessage());
            }
        }
        return CompletableFuture.completedFuture(null);
    }
}

