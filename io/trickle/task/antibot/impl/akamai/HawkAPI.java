/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.buffer.Buffer
 *  io.vertx.core.json.JsonObject
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.client.HttpResponse
 *  io.vertx.ext.web.client.WebClient
 *  io.vertx.ext.web.codec.BodyCodec
 */
package io.trickle.task.antibot.impl.akamai;

import io.trickle.core.VertxSingleton;
import io.trickle.task.antibot.impl.akamai.pixel.Pixel;
import io.trickle.util.Utils;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import java.lang.invoke.LambdaMetafactory;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.regex.Pattern;

public class HawkAPI
implements Pixel {
    public static Pattern UA_MAJOR_VERSION_PATTERN;
    public static String[] api_ua;
    public String useragent = null;

    public void setUseragent(String string) {
        this.useragent = string;
    }

    public CompletableFuture getSensorPayload(String string, String string2, String string3, boolean bl) {
        HttpRequest httpRequest = this.hawkSensor();
        JsonObject jsonObject = this.hawkSensorForm(string, string2, string3, bl);
        int n = 0;
        while (n++ <= 1000) {
            try {
                CompletableFuture completableFuture = Request.send(httpRequest, jsonObject);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> HawkAPI.async$getSensorPayload$1(this, string, string2, string3, (int)(bl ? 1 : 0), httpRequest, jsonObject, n, completableFuture2, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse != null) {
                    String string4 = httpResponse.bodyAsString("UTF-8").split("\\*\\*\\*")[0];
                    return CompletableFuture.completedFuture(string4);
                }
                CompletableFuture completableFuture3 = VertxUtil.randomSleep(10000L);
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> HawkAPI.async$getSensorPayload$1(this, string, string2, string3, (int)(bl ? 1 : 0), httpRequest, jsonObject, n, completableFuture4, httpResponse, 2, arg_0));
                }
                completableFuture3.join();
            }
            catch (Exception exception) {}
        }
        return CompletableFuture.completedFuture(null);
    }

    static {
        api_ua = new String[]{"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.51 Safari/537.36", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.82 Safari/537.36", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.99 Safari/537.36", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36"};
        UA_MAJOR_VERSION_PATTERN = Pattern.compile("Chrome/[0-9]([0-9])");
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    public static CompletableFuture async$getSensorPayload$1(HawkAPI var0, String var1_1, String var2_2, String var3_3, int var4_4, HttpRequest var5_5, JsonObject var6_6, int var7_7, CompletableFuture var8_8, HttpResponse var9_10, int var10_11, Object var11_14) {
        switch (var10_11) {
            case 0: {
                var5_5 = var0.hawkSensor();
                var6_6 = var0.hawkSensorForm(var1_1, var2_2, var3_3, (boolean)var4_4);
                var7_7 = 0;
                while (var7_7++ <= 1000) {
                    try {
                        v0 = Request.send(var5_5, var6_6);
                        if (!v0.isDone()) {
                            var10_12 = v0;
                            return var10_12.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$getSensorPayload$1(io.trickle.task.antibot.impl.akamai.HawkAPI java.lang.String java.lang.String java.lang.String int io.vertx.ext.web.client.HttpRequest io.vertx.core.json.JsonObject int java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((HawkAPI)var0, (String)var1_1, (String)var2_2, (String)var3_3, (int)var4_4, (HttpRequest)var5_5, (JsonObject)var6_6, (int)var7_7, (CompletableFuture)var10_12, null, (int)1));
                        }
lbl12:
                        // 3 sources

                        while (true) {
                            var8_8 = (HttpResponse)v0.join();
                            if (var8_8 != null) {
                                var9_10 /* !! */  = var8_8.bodyAsString("UTF-8").split("\\*\\*\\*")[0];
                                return CompletableFuture.completedFuture(var9_10 /* !! */ );
                            }
                            v1 = VertxUtil.randomSleep(10000L);
                            if (!v1.isDone()) {
                                var10_13 = v1;
                                return var10_13.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$getSensorPayload$1(io.trickle.task.antibot.impl.akamai.HawkAPI java.lang.String java.lang.String java.lang.String int io.vertx.ext.web.client.HttpRequest io.vertx.core.json.JsonObject int java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((HawkAPI)var0, (String)var1_1, (String)var2_2, (String)var3_3, (int)var4_4, (HttpRequest)var5_5, (JsonObject)var6_6, (int)var7_7, (CompletableFuture)var10_13, (HttpResponse)var8_8, (int)2));
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

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    public static CompletableFuture async$getPixelReqString(HawkAPI var0, String var1_1, String var2_2, String var3_3, HttpRequest var4_4, Buffer var5_5, int var6_6, CompletableFuture var7_7, HttpResponse var8_9, int var9_10, Object var10_13) {
        switch (var9_10) {
            case 0: {
                var4_4 = var0.hawkPixel();
                var5_5 = var0.hawkPixelForm(var1_1, var3_3);
                var6_6 = 0;
                while (var6_6++ <= 1000) {
                    try {
                        v0 = Request.send(var4_4, var5_5);
                        if (!v0.isDone()) {
                            var9_11 = v0;
                            return var9_11.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$getPixelReqString(io.trickle.task.antibot.impl.akamai.HawkAPI java.lang.String java.lang.String java.lang.String io.vertx.ext.web.client.HttpRequest io.vertx.core.buffer.Buffer int java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((HawkAPI)var0, (String)var1_1, (String)var2_2, (String)var3_3, (HttpRequest)var4_4, (Buffer)var5_5, (int)var6_6, (CompletableFuture)var9_11, null, (int)1));
                        }
lbl12:
                        // 3 sources

                        while (true) {
                            var7_7 = (HttpResponse)v0.join();
                            if (var7_7 != null) {
                                var8_9 /* !! */  = var7_7.bodyAsString();
                                return CompletableFuture.completedFuture(var8_9 /* !! */ );
                            }
                            v1 = VertxUtil.randomSleep(10000L);
                            if (!v1.isDone()) {
                                var9_12 = v1;
                                return var9_12.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$getPixelReqString(io.trickle.task.antibot.impl.akamai.HawkAPI java.lang.String java.lang.String java.lang.String io.vertx.ext.web.client.HttpRequest io.vertx.core.buffer.Buffer int java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((HawkAPI)var0, (String)var1_1, (String)var2_2, (String)var3_3, (HttpRequest)var4_4, (Buffer)var5_5, (int)var6_6, (CompletableFuture)var9_12, (HttpResponse)var7_7, (int)2));
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
                        if (var7_8.getMessage().toLowerCase().contains("timeout")) continue;
                        System.out.println("API[H] err: " + var7_8.getMessage().replace("ak01-eu.hwkapi.com", "remote"));
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

    public Buffer hawkPixelForm(String string, String string2) {
        return new JsonObject().put("user_agent", (Object)this.useragent).put("script_id", (Object)string).put("script_secret", (Object)string2).toBuffer();
    }

    public HttpRequest hawkPixel() {
        return this.client().postAbs("https://ak01-eu.hwkapi.com/akamai/pixel").putHeader("X-Api-Key", "ce3cabed-e10f-43b1-a2a2-8d2a9c2a212f").putHeader("X-Sec", "high").timeout(50000L).as(BodyCodec.buffer());
    }

    @Override
    public CompletableFuture getPixelReqForm(String string, String string2, String string3) {
        return CompletableFuture.failedFuture(new Exception("Unsupported method"));
    }

    public JsonObject hawkSensorForm(String string) {
        return new JsonObject().put("site", (Object)"https://www.bestbuy.com/").put("abck", (Object)string).put("type", (Object)"sensor").put("events", (Object)"1,1").put("user_agent", (Object)this.useragent);
    }

    public HttpRequest hawkSensor() {
        return this.client().postAbs("https://ak01-eu.hwkapi.com/akamai/generate").putHeader("X-Api-Key", "ce3cabed-e10f-43b1-a2a2-8d2a9c2a212f").putHeader("X-Sec", "high").timeout(50000L).as(BodyCodec.buffer());
    }

    public HttpRequest hawkUserAgent() {
        return this.client().getAbs("https://ak01-eu.hwkapi.com/akamai/ua").putHeader("X-Api-Key", "ce3cabed-e10f-43b1-a2a2-8d2a9c2a212f").putHeader("X-Sec", "high").timeout(50000L).as(BodyCodec.string());
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    public static CompletableFuture async$getSensorPayload(HawkAPI var0, String var1_1, HttpRequest var2_2, JsonObject var3_3, int var4_4, CompletableFuture var5_5, HttpResponse var6_7, int var7_8, Object var8_11) {
        switch (var7_8) {
            case 0: {
                var2_2 = var0.hawkSensor();
                var3_3 = var0.hawkSensorForm(var1_1);
                var4_4 = 0;
                while (var4_4++ <= 1000) {
                    try {
                        v0 = Request.send(var2_2, var3_3);
                        if (!v0.isDone()) {
                            var7_9 = v0;
                            return var7_9.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$getSensorPayload(io.trickle.task.antibot.impl.akamai.HawkAPI java.lang.String io.vertx.ext.web.client.HttpRequest io.vertx.core.json.JsonObject int java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((HawkAPI)var0, (String)var1_1, (HttpRequest)var2_2, (JsonObject)var3_3, (int)var4_4, (CompletableFuture)var7_9, null, (int)1));
                        }
lbl12:
                        // 3 sources

                        while (true) {
                            var5_5 = (HttpResponse)v0.join();
                            if (var5_5 != null) {
                                var6_7 /* !! */  = var5_5.bodyAsString("UTF-8").split("\\*")[0];
                                return CompletableFuture.completedFuture(var6_7 /* !! */ );
                            }
                            v1 = VertxUtil.randomSleep(10000L);
                            if (!v1.isDone()) {
                                var7_10 = v1;
                                return var7_10.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$getSensorPayload(io.trickle.task.antibot.impl.akamai.HawkAPI java.lang.String io.vertx.ext.web.client.HttpRequest io.vertx.core.json.JsonObject int java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((HawkAPI)var0, (String)var1_1, (HttpRequest)var2_2, (JsonObject)var3_3, (int)var4_4, (CompletableFuture)var7_10, (HttpResponse)var5_5, (int)2));
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
                    catch (Exception var5_6) {}
                }
                return CompletableFuture.completedFuture(null);
            }
            case 1: {
                v0 = var5_5;
                ** continue;
            }
            case 2: {
                v1 = var5_5;
                var5_5 = var6_7 /* !! */ ;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture updateUserAgent() {
        int n = 0;
        while (n++ <= 100) {
            try {
                if (ThreadLocalRandom.current().nextBoolean()) {
                    return CompletableFuture.completedFuture(api_ua[ThreadLocalRandom.current().nextInt(api_ua.length)]);
                }
                CompletableFuture completableFuture = Request.executeTillOk(this.hawkUserAgent(), 5000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> HawkAPI.async$updateUserAgent(this, n, completableFuture2, null, 1, arg_0));
                }
                String string = (String)completableFuture.join();
                if (Integer.parseInt(Objects.requireNonNull(Utils.quickParseFirst(string, UA_MAJOR_VERSION_PATTERN))) >= 7) {
                    this.useragent = string;
                    return CompletableFuture.completedFuture(this.useragent);
                }
                CompletableFuture completableFuture3 = VertxUtil.sleep(300L);
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> HawkAPI.async$updateUserAgent(this, n, completableFuture4, string, 2, arg_0));
                }
                completableFuture3.join();
            }
            catch (Throwable throwable) {}
        }
        return CompletableFuture.completedFuture(this.useragent);
    }

    public JsonObject hawkSensorForm(String string, String string2, String string3, boolean bl) {
        String string4;
        JsonObject jsonObject = new JsonObject().put("site", (Object)("https://www.yeezysupply.com/products/" + string3)).put("abck", (Object)string).put("bm_sz", (Object)string2).put("type", (Object)"sensor");
        if (bl) {
            string4 = "1,1";
            return jsonObject.put("events", (Object)string4).put("user_agent", (Object)this.useragent);
        }
        string4 = "0,0";
        return jsonObject.put("events", (Object)string4).put("user_agent", (Object)this.useragent);
    }

    public String getUseragent() {
        return this.useragent;
    }

    public CompletableFuture getSensorPayload(String string) {
        HttpRequest httpRequest = this.hawkSensor();
        JsonObject jsonObject = this.hawkSensorForm(string);
        int n = 0;
        while (n++ <= 1000) {
            try {
                CompletableFuture completableFuture = Request.send(httpRequest, jsonObject);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> HawkAPI.async$getSensorPayload(this, string, httpRequest, jsonObject, n, completableFuture2, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse != null) {
                    String string2 = httpResponse.bodyAsString("UTF-8").split("\\*")[0];
                    return CompletableFuture.completedFuture(string2);
                }
                CompletableFuture completableFuture3 = VertxUtil.randomSleep(10000L);
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> HawkAPI.async$getSensorPayload(this, string, httpRequest, jsonObject, n, completableFuture4, httpResponse, 2, arg_0));
                }
                completableFuture3.join();
            }
            catch (Exception exception) {}
        }
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture getPixelReqString(String string, String string2, String string3) {
        HttpRequest httpRequest = this.hawkPixel();
        Buffer buffer = this.hawkPixelForm(string, string3);
        int n = 0;
        while (n++ <= 1000) {
            try {
                CompletableFuture completableFuture = Request.send(httpRequest, buffer);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> HawkAPI.async$getPixelReqString(this, string, string2, string3, httpRequest, buffer, n, completableFuture2, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse != null) {
                    String string4 = httpResponse.bodyAsString();
                    return CompletableFuture.completedFuture(string4);
                }
                CompletableFuture completableFuture3 = VertxUtil.randomSleep(10000L);
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> HawkAPI.async$getPixelReqString(this, string, string2, string3, httpRequest, buffer, n, completableFuture4, httpResponse, 2, arg_0));
                }
                completableFuture3.join();
            }
            catch (Exception exception) {
                if (exception.getMessage().toLowerCase().contains("timeout")) continue;
                System.out.println("API[H] err: " + exception.getMessage().replace("ak01-eu.hwkapi.com", "remote"));
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public WebClient client() {
        return VertxSingleton.INSTANCE.getLocalClient().getClient();
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$updateUserAgent(HawkAPI var0, int var1_1, CompletableFuture var2_2, String var3_4, int var4_5, Object var5_6) {
        switch (var4_5) {
            case 0: {
                var1_1 = 0;
                while (var1_1++ <= 100) {
                    try {
                        if (ThreadLocalRandom.current().nextBoolean()) {
                            return CompletableFuture.completedFuture(HawkAPI.api_ua[ThreadLocalRandom.current().nextInt(HawkAPI.api_ua.length)]);
                        }
                        v0 = Request.executeTillOk(var0.hawkUserAgent(), 5000);
                        if (!v0.isDone()) {
                            var3_4 = v0;
                            return var3_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$updateUserAgent(io.trickle.task.antibot.impl.akamai.HawkAPI int java.util.concurrent.CompletableFuture java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((HawkAPI)var0, (int)var1_1, (CompletableFuture)var3_4, null, (int)1));
                        }
lbl12:
                        // 3 sources

                        while (true) {
                            var2_2 = (String)v0.join();
                            if (Integer.parseInt(Objects.requireNonNull(Utils.quickParseFirst((String)var2_2, new Pattern[]{HawkAPI.UA_MAJOR_VERSION_PATTERN}))) >= 7) {
                                var0.useragent = var2_2;
                                return CompletableFuture.completedFuture(var0.useragent);
                            }
                            v1 = VertxUtil.sleep(300L);
                            if (!v1.isDone()) {
                                var3_4 = v1;
                                return var3_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$updateUserAgent(io.trickle.task.antibot.impl.akamai.HawkAPI int java.util.concurrent.CompletableFuture java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((HawkAPI)var0, (int)var1_1, (CompletableFuture)var3_4, (String)var2_2, (int)2));
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
                    catch (Throwable var2_3) {}
                }
                return CompletableFuture.completedFuture(var0.useragent);
            }
            case 1: {
                v0 = var2_2;
                ** continue;
            }
            case 2: {
                v1 = var2_2;
                var2_2 = var3_4;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }
}

