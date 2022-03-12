/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.netty.util.AsciiString
 *  io.vertx.core.MultiMap
 *  io.vertx.core.buffer.Buffer
 *  io.vertx.core.http.HttpMethod
 *  io.vertx.core.json.JsonArray
 *  io.vertx.core.json.JsonObject
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.client.HttpResponse
 *  io.vertx.ext.web.client.impl.HttpRequestImpl
 *  io.vertx.ext.web.codec.BodyCodec
 */
package io.trickle.task.antibot.impl.px.payload.captcha;

import io.netty.util.AsciiString;
import io.trickle.core.VertxSingleton;
import io.trickle.core.actor.TaskActor;
import io.trickle.task.antibot.impl.px.PerimeterX;
import io.trickle.task.sites.Site;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.trickle.webclient.ClientType;
import io.trickle.webclient.TaskApiClient;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.impl.HttpRequestImpl;
import io.vertx.ext.web.codec.BodyCodec;
import java.lang.invoke.LambdaMetafactory;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;

public class DesktopPX
extends PerimeterX {
    public String userAgent;
    public static CharSequence VID_COOKIE;
    public Function<HttpResponse<?>, Buffer> setupDevice = this::lambda$new$1;
    public JsonObject cookieSession;
    public String uuid;
    public JsonArray performance;
    public Function<HttpResponse<?>, Buffer> okayAndConversionFunc = this::lambda$new$2;
    public static CharSequence DEFAULT_SEC_UA;
    public String secUA;
    public MultiMap cachedResponse = null;
    public static CharSequence CFP_VALUE;
    public TaskApiClient delegate;
    public static Site site;
    public static CharSequence FP_VALUE;
    public static CharSequence RF_VALUE;
    public static CharSequence PXDE_VALUE;
    public static Function<HttpResponse<?>, String> okayStatusFunc;
    public static CharSequence PXHD_VALUE;
    public static CharSequence DEFAULT_UA;
    public String deviceNumber = "undefined";
    public long lockoutTiming = -1L;
    public String vid;
    public static CharSequence PX3_VALUE;
    public static CharSequence ONE_VALUE;
    public static CharSequence CTS_VALUE;
    public String sid;
    public long waitTime;

    @Override
    public String getDeviceLang() {
        return "en-US,en;q=0.9";
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$initialise(DesktopPX var0, CompletableFuture var1_1, int var2_2, Object var3_3) {
        switch (var2_2) {
            case 0: {
                v0 = var0.execute("Initiating...", var0.setupDevice, (Supplier<HttpRequest>)LambdaMetafactory.metafactory(null, null, null, ()Ljava/lang/Object;, lambda$initialise$10(), ()Lio/vertx/ext/web/client/HttpRequest;)((DesktopPX)var0), null);
                if (!v0.isDone()) {
                    var1_1 = v0;
                    return var1_1.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$initialise(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPX java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPX)var0, (CompletableFuture)var1_1, (int)1));
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

    public HttpRequest apiRequest(String string, Boolean bl) {
        HttpRequest httpRequest = VertxSingleton.INSTANCE.getLocalClient().getClient().postAbs("https://trickle-px-oygn7nn37q-uc.a.run.app/gen/" + string + ".json").as(BodyCodec.buffer()).addQueryParam("mobile", "false");
        if (bl == null) return httpRequest;
        httpRequest.addQueryParam("captcha", bl != false ? "hold" : "recaptcha");
        return httpRequest;
    }

    public boolean isBool(JsonObject jsonObject) {
        Object object = jsonObject.getValue("error", null);
        if (object == null) return false;
        return object instanceof Boolean;
    }

    @Override
    public CompletableFuture initialise() {
        CompletableFuture completableFuture = this.execute("Initiating...", this.setupDevice, this::lambda$initialise$10, null);
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPX.async$initialise(this, completableFuture2, 1, arg_0));
        }
        completableFuture.join();
        return CompletableFuture.completedFuture(true);
    }

    static {
        DEFAULT_UA = AsciiString.cached((String)"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.45 Safari/537.36");
        DEFAULT_SEC_UA = AsciiString.cached((String)"\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"96\", \"Google Chrome\";v=\"96\"");
        VID_COOKIE = AsciiString.cached((String)"_pxvid");
        RF_VALUE = AsciiString.cached((String)"_pxff_rf");
        FP_VALUE = AsciiString.cached((String)"_pxff_fp");
        ONE_VALUE = AsciiString.cached((String)"1");
        CFP_VALUE = AsciiString.cached((String)"_pxff_cfp");
        CTS_VALUE = AsciiString.cached((String)"pxcts");
        PXHD_VALUE = AsciiString.cached((String)"_pxhd");
        PX3_VALUE = AsciiString.cached((String)"_px3");
        PXDE_VALUE = AsciiString.cached((String)"_pxde");
        site = Site.WALMART;
        okayStatusFunc = DesktopPX::lambda$static$0;
    }

    public HttpRequest imageReq(Buffer buffer) {
        HttpRequest httpRequest = this.client.getAbs("https://collector-pxu6b0qd2s.px-client.net/b/g?" + buffer.toString()).as(BodyCodec.buffer());
        httpRequest.putHeader("sec-ch-ua", this.getDeviceSecUA());
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.getDeviceUA());
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("origin", "https://www.walmart.com");
        httpRequest.putHeader("sec-fetch-site", "cross-site");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.walmart.com/");
        httpRequest.putHeader("accept-encoding", this.getDeviceAcceptEncoding());
        httpRequest.putHeader("accept-language", this.getDeviceLang());
        return httpRequest;
    }

    public HttpRequest lambda$solveCaptcha$7() {
        return this.apiRequest("img", true);
    }

    public HttpRequest lambda$solveCaptcha$6() {
        return this.apiRequest("2", true);
    }

    @Override
    public String getDeviceUA() {
        if (this.userAgent != null) return this.userAgent;
        return DEFAULT_UA.toString();
    }

    public JsonObject buildReqBody(String string, JsonArray jsonArray) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("pxhd", (Object)this.delegate.getCookies().getCookieValue("_pxhd"));
        jsonObject.put("uuid", (Object)this.uuid);
        jsonObject.put("sid", (Object)this.sid);
        jsonObject.put("vid", (Object)this.vid);
        jsonObject.put("firstResponse", (Object)string);
        jsonObject.put("performance", (Object)jsonArray);
        jsonObject.put("cts", (Object)this.delegate.getCookies().getCookieValue(String.valueOf(CTS_VALUE)));
        jsonObject.put("device", (Object)this.deviceNumber);
        return jsonObject;
    }

    @Override
    public String getDeviceAcceptEncoding() {
        return "gzip, deflate, br";
    }

    public HttpRequest lambda$solveCaptcha$8(Buffer buffer) {
        return this.imageReq(buffer);
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    public static CompletableFuture async$solve(DesktopPX var0, int var1_1, CompletableFuture var2_2, Buffer var3_3, String var4_4, Buffer var5_5, int var6_6, Object var7_8) {
        switch (var6_6) {
            case 0: {
                var1_1 = 1;
                if (var1_1 > 30) return CompletableFuture.completedFuture(MultiMap.caseInsensitiveMultiMap());
                v0 = var0.execute("API 1", var0.okayAndConversionFunc, (Supplier<HttpRequest>)LambdaMetafactory.metafactory(null, null, null, ()Ljava/lang/Object;, lambda$solve$3(), ()Lio/vertx/ext/web/client/HttpRequest;)((DesktopPX)var0), var0.buildReqBody(null, null));
                if (!v0.isDone()) {
                    var7_8 = v0;
                    return var7_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solve(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPX int java.util.concurrent.CompletableFuture io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPX)var0, (int)var1_1, (CompletableFuture)var7_8, null, null, null, (int)1));
                }
                ** GOTO lbl12
            }
            case 1: {
                v0 = var2_2;
lbl12:
                // 2 sources

                var2_2 = (Buffer)v0.join();
                v1 = VertxUtil.hardCodedSleep(var0.waitTime);
                if (!v1.isDone()) {
                    var7_8 = v1;
                    return var7_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solve(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPX int java.util.concurrent.CompletableFuture io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPX)var0, (int)var1_1, (CompletableFuture)var7_8, (Buffer)var2_2, null, null, (int)2));
                }
                ** GOTO lbl21
            }
            case 2: {
                v1 = var2_2;
                var2_2 = var3_3 /* !! */ ;
lbl21:
                // 2 sources

                v1.join();
                v2 = var0.execute("Sensor 1/2", DesktopPX.okayStatusFunc, (Supplier<HttpRequest>)LambdaMetafactory.metafactory(null, null, null, ()Ljava/lang/Object;, collectorReq(), ()Lio/vertx/ext/web/client/HttpRequest;)((DesktopPX)var0), var2_2);
                if (!v2.isDone()) {
                    var7_8 = v2;
                    return var7_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solve(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPX int java.util.concurrent.CompletableFuture io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPX)var0, (int)var1_1, (CompletableFuture)var7_8, (Buffer)var2_2, null, null, (int)3));
                }
                ** GOTO lbl31
            }
            case 3: {
                v2 = var2_2;
                var2_2 = var3_3 /* !! */ ;
lbl31:
                // 2 sources

                if (!(v3 = var0.execute("API 2", var0.okayAndConversionFunc, (Supplier<HttpRequest>)LambdaMetafactory.metafactory(null, null, null, ()Ljava/lang/Object;, lambda$solve$4(), ()Lio/vertx/ext/web/client/HttpRequest;)((DesktopPX)var0), var0.buildReqBody((String)(var3_3 /* !! */  = (String)v2.join()), var0.performance))).isDone()) {
                    var7_8 = v3;
                    return var7_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solve(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPX int java.util.concurrent.CompletableFuture io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPX)var0, (int)var1_1, (CompletableFuture)var7_8, (Buffer)var2_2, (String)var3_3 /* !! */ , null, (int)4));
                }
                ** GOTO lbl40
            }
            case 4: {
                v3 = var2_2;
                v4 = var3_3 /* !! */ ;
                var3_3 /* !! */  = var4_4;
                var2_2 = v4;
lbl40:
                // 2 sources

                var4_4 = (Buffer)v3.join();
                v5 = VertxUtil.hardCodedSleep(var0.waitTime);
                if (!v5.isDone()) {
                    var7_8 = v5;
                    return var7_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solve(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPX int java.util.concurrent.CompletableFuture io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPX)var0, (int)var1_1, (CompletableFuture)var7_8, (Buffer)var2_2, (String)var3_3 /* !! */ , (Buffer)var4_4, (int)5));
                }
                ** GOTO lbl53
            }
            case 5: {
                v5 = var2_2;
                v6 = var3_3 /* !! */ ;
                v7 = var4_4;
                var4_4 = var5_5 /* !! */ ;
                var3_3 /* !! */  = v7;
                var2_2 = v6;
lbl53:
                // 2 sources

                v5.join();
                v8 = var0.execute("Sensor 2/2", DesktopPX.okayStatusFunc, (Supplier<HttpRequest>)LambdaMetafactory.metafactory(null, null, null, ()Ljava/lang/Object;, collectorReq(), ()Lio/vertx/ext/web/client/HttpRequest;)((DesktopPX)var0), var4_4);
                if (!v8.isDone()) {
                    var7_8 = v8;
                    return var7_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solve(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPX int java.util.concurrent.CompletableFuture io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPX)var0, (int)var1_1, (CompletableFuture)var7_8, (Buffer)var2_2, (String)var3_3 /* !! */ , (Buffer)var4_4, (int)6));
                }
                ** GOTO lbl67
            }
            case 6: {
                v8 = var2_2;
                v9 = var3_3 /* !! */ ;
                v10 = var4_4;
                var4_4 = var5_5 /* !! */ ;
                var3_3 /* !! */  = v10;
                var2_2 = v9;
lbl67:
                // 2 sources

                var5_5 /* !! */  = (String)v8.join();
                var6_7 = MultiMap.caseInsensitiveMultiMap();
                if (var0.getVid() != null && !var0.getVid().isBlank()) {
                    var6_7.add(DesktopPX.VID_COOKIE, (CharSequence)var0.getVid());
                }
                var6_7.add(DesktopPX.RF_VALUE, DesktopPX.ONE_VALUE);
                var6_7.add(DesktopPX.FP_VALUE, DesktopPX.ONE_VALUE);
                var6_7.add(DesktopPX.CFP_VALUE, DesktopPX.ONE_VALUE);
                var0.parseResultCookies((String)var5_5 /* !! */ , var6_7);
                return CompletableFuture.completedFuture(var6_7);
            }
        }
        throw new IllegalArgumentException();
    }

    public HttpRequest lambda$solveCaptcha$9() {
        return this.apiRequest("3", true);
    }

    @Override
    public CompletableFuture solve() {
        int n = 1;
        if (n > 30) return CompletableFuture.completedFuture(MultiMap.caseInsensitiveMultiMap());
        CompletableFuture completableFuture = this.execute("API 1", this.okayAndConversionFunc, this::lambda$solve$3, this.buildReqBody(null, null));
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPX.async$solve(this, n, completableFuture2, null, null, null, 1, arg_0));
        }
        Buffer buffer = (Buffer)completableFuture.join();
        CompletableFuture completableFuture3 = VertxUtil.hardCodedSleep(this.waitTime);
        if (!completableFuture3.isDone()) {
            CompletableFuture completableFuture4 = completableFuture3;
            return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPX.async$solve(this, n, completableFuture4, buffer, null, null, 2, arg_0));
        }
        completableFuture3.join();
        CompletableFuture completableFuture5 = this.execute("Sensor 1/2", okayStatusFunc, this::collectorReq, buffer);
        if (!completableFuture5.isDone()) {
            CompletableFuture completableFuture6 = completableFuture5;
            return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPX.async$solve(this, n, completableFuture6, buffer, null, null, 3, arg_0));
        }
        String string = (String)completableFuture5.join();
        CompletableFuture completableFuture7 = this.execute("API 2", this.okayAndConversionFunc, this::lambda$solve$4, this.buildReqBody(string, this.performance));
        if (!completableFuture7.isDone()) {
            CompletableFuture completableFuture8 = completableFuture7;
            return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPX.async$solve(this, n, completableFuture8, buffer, string, null, 4, arg_0));
        }
        Buffer buffer2 = (Buffer)completableFuture7.join();
        CompletableFuture completableFuture9 = VertxUtil.hardCodedSleep(this.waitTime);
        if (!completableFuture9.isDone()) {
            CompletableFuture completableFuture10 = completableFuture9;
            return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPX.async$solve(this, n, completableFuture10, buffer, string, buffer2, 5, arg_0));
        }
        completableFuture9.join();
        CompletableFuture completableFuture11 = this.execute("Sensor 2/2", okayStatusFunc, this::collectorReq, buffer2);
        if (!completableFuture11.isDone()) {
            CompletableFuture completableFuture12 = completableFuture11;
            return ((CompletableFuture)completableFuture12.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPX.async$solve(this, n, completableFuture12, buffer, string, buffer2, 6, arg_0));
        }
        String string2 = (String)completableFuture11.join();
        MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
        if (this.getVid() != null && !this.getVid().isBlank()) {
            multiMap.add(VID_COOKIE, (CharSequence)this.getVid());
        }
        multiMap.add(RF_VALUE, ONE_VALUE);
        multiMap.add(FP_VALUE, ONE_VALUE);
        multiMap.add(CFP_VALUE, ONE_VALUE);
        this.parseResultCookies(string2, multiMap);
        return CompletableFuture.completedFuture(multiMap);
    }

    public static String lambda$static$0(HttpResponse httpResponse) {
        if (httpResponse.statusCode() != 200) return null;
        return httpResponse.bodyAsString();
    }

    public CompletableFuture execute(String string, Function function, Supplier supplier, Object object) {
        int n = 0;
        while (n++ < Integer.MAX_VALUE) {
            try {
                HttpResponse httpResponse;
                HttpRequest httpRequest = (HttpRequest)supplier.get();
                if (((HttpRequestImpl)httpRequest).method().equals((Object)HttpMethod.POST)) {
                    CompletableFuture completableFuture = Request.send(httpRequest, object);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture2 = completableFuture;
                        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPX.async$execute(this, string, function, (Supplier)supplier, object, n, httpRequest, completableFuture2, null, 0, null, null, 1, arg_0));
                    }
                    httpResponse = (HttpResponse)completableFuture.join();
                } else {
                    CompletableFuture completableFuture = Request.send(httpRequest);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture3 = completableFuture;
                        return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPX.async$execute(this, string, function, (Supplier)supplier, object, n, httpRequest, completableFuture3, null, 0, null, null, 2, arg_0));
                    }
                    httpResponse = (HttpResponse)completableFuture.join();
                }
                if (httpResponse != null) {
                    int n2 = httpResponse.statusCode();
                    Optional optional = Optional.ofNullable(function.apply(httpResponse));
                    if (optional.isPresent()) {
                        return CompletableFuture.completedFuture(optional.get());
                    }
                    this.logger.warn("Failed {}: '{}'", (Object)string.toLowerCase(Locale.ROOT), (Object)n2);
                    CompletableFuture completableFuture = VertxUtil.randomSleep(5000L);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPX.async$execute(this, string, function, (Supplier)supplier, object, n, httpRequest, completableFuture4, httpResponse, n2, optional, null, 3, arg_0));
                    }
                    completableFuture.join();
                    continue;
                }
                CompletableFuture completableFuture = VertxUtil.randomSleep(5000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture5 = completableFuture;
                    return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPX.async$execute(this, string, function, (Supplier)supplier, object, n, httpRequest, completableFuture5, httpResponse, 0, null, null, 4, arg_0));
                }
                completableFuture.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error " + string.toLowerCase(Locale.ROOT) + ": {}", (Object)throwable.getMessage());
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug((Object)throwable);
                }
                CompletableFuture completableFuture = VertxUtil.randomSleep(5000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPX.async$execute(this, string, function, (Supplier)supplier, object, n, null, completableFuture6, null, 0, null, throwable, 5, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.failedFuture(new Exception("Failed to execute " + string));
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$execute(DesktopPX var0, String var1_1, Function var2_2, Supplier var3_3, Object var4_4, int var5_5, HttpRequest var6_6, CompletableFuture var7_8, HttpResponse var8_9, int var9_11, Optional var10_13, Throwable var11_14, int var12_15, Object var13_16) {
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

    public DesktopPX(TaskActor taskActor) {
        super(taskActor, ClientType.CHROME);
        this.cookieSession = new JsonObject();
        this.delegate = taskActor.getClient();
    }

    @Override
    public void reset() {
        this.cookieSession = new JsonObject();
        this.userAgent = null;
        this.deviceNumber = "undefined";
    }

    @Override
    public CompletableFuture solveCaptcha(String string, String string2, String string3) {
        this.uuid = string2;
        this.vid = string;
        int n = 1;
        while (true) {
            if (n > ThreadLocalRandom.current().nextInt(1, 4)) {
                this.delegate.getCookies().removeAnyMatch("_pxhd");
                return CompletableFuture.completedFuture(MultiMap.caseInsensitiveMultiMap());
            }
            this.performance = null;
            CompletableFuture completableFuture = this.execute("API 1", this.okayAndConversionFunc, this::lambda$solveCaptcha$5, this.buildReqBody(null, null));
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPX.async$solveCaptcha(this, string, string2, string3, n, completableFuture2, null, null, null, null, null, null, null, null, 1, arg_0));
            }
            Buffer buffer = (Buffer)completableFuture.join();
            CompletableFuture completableFuture3 = VertxUtil.hardCodedSleep(this.waitTime);
            if (!completableFuture3.isDone()) {
                CompletableFuture completableFuture4 = completableFuture3;
                return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPX.async$solveCaptcha(this, string, string2, string3, n, completableFuture4, buffer, null, null, null, null, null, null, null, 2, arg_0));
            }
            completableFuture3.join();
            CompletableFuture completableFuture5 = this.execute("Sensor 1/4", okayStatusFunc, this::bundleReq, buffer);
            if (!completableFuture5.isDone()) {
                CompletableFuture completableFuture6 = completableFuture5;
                return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPX.async$solveCaptcha(this, string, string2, string3, n, completableFuture6, buffer, null, null, null, null, null, null, null, 3, arg_0));
            }
            String string4 = (String)completableFuture5.join();
            CompletableFuture completableFuture7 = this.execute("API 2", this.okayAndConversionFunc, this::lambda$solveCaptcha$6, this.buildReqBody(string4, this.performance));
            if (!completableFuture7.isDone()) {
                CompletableFuture completableFuture8 = completableFuture7;
                return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPX.async$solveCaptcha(this, string, string2, string3, n, completableFuture8, buffer, string4, null, null, null, null, null, null, 4, arg_0));
            }
            Buffer buffer2 = (Buffer)completableFuture7.join();
            CompletableFuture completableFuture9 = VertxUtil.hardCodedSleep(this.waitTime);
            if (!completableFuture9.isDone()) {
                CompletableFuture completableFuture10 = completableFuture9;
                return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPX.async$solveCaptcha(this, string, string2, string3, n, completableFuture10, buffer, string4, buffer2, null, null, null, null, null, 5, arg_0));
            }
            completableFuture9.join();
            CompletableFuture completableFuture11 = this.execute("Sensor 2/4", okayStatusFunc, this::bundleReq, buffer2);
            if (!completableFuture11.isDone()) {
                CompletableFuture completableFuture12 = completableFuture11;
                return ((CompletableFuture)completableFuture12.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPX.async$solveCaptcha(this, string, string2, string3, n, completableFuture12, buffer, string4, buffer2, null, null, null, null, null, 6, arg_0));
            }
            String string5 = (String)completableFuture11.join();
            CompletableFuture completableFuture13 = this.execute("API 3", this.okayAndConversionFunc, this::lambda$solveCaptcha$7, this.buildReqBody(string4, this.performance));
            if (!completableFuture13.isDone()) {
                CompletableFuture completableFuture14 = completableFuture13;
                return ((CompletableFuture)completableFuture14.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPX.async$solveCaptcha(this, string, string2, string3, n, completableFuture14, buffer, string4, buffer2, string5, null, null, null, null, 7, arg_0));
            }
            Buffer buffer3 = (Buffer)completableFuture13.join();
            CompletableFuture completableFuture15 = VertxUtil.hardCodedSleep(this.waitTime);
            if (!completableFuture15.isDone()) {
                CompletableFuture completableFuture16 = completableFuture15;
                return ((CompletableFuture)completableFuture16.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPX.async$solveCaptcha(this, string, string2, string3, n, completableFuture16, buffer, string4, buffer2, string5, buffer3, null, null, null, 8, arg_0));
            }
            completableFuture15.join();
            CompletableFuture completableFuture17 = this.execute("Sensor 3/4", okayStatusFunc, () -> this.lambda$solveCaptcha$8(buffer3), null);
            if (!completableFuture17.isDone()) {
                CompletableFuture completableFuture18 = completableFuture17;
                return ((CompletableFuture)completableFuture18.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPX.async$solveCaptcha(this, string, string2, string3, n, completableFuture18, buffer, string4, buffer2, string5, buffer3, null, null, null, 9, arg_0));
            }
            String string6 = (String)completableFuture17.join();
            CompletableFuture completableFuture19 = this.execute("API 4", this.okayAndConversionFunc, this::lambda$solveCaptcha$9, this.buildReqBody(string4, this.performance));
            if (!completableFuture19.isDone()) {
                CompletableFuture completableFuture20 = completableFuture19;
                return ((CompletableFuture)completableFuture20.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPX.async$solveCaptcha(this, string, string2, string3, n, completableFuture20, buffer, string4, buffer2, string5, buffer3, string6, null, null, 10, arg_0));
            }
            Buffer buffer4 = (Buffer)completableFuture19.join();
            CompletableFuture completableFuture21 = VertxUtil.hardCodedSleep(this.waitTime);
            if (!completableFuture21.isDone()) {
                CompletableFuture completableFuture22 = completableFuture21;
                return ((CompletableFuture)completableFuture22.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPX.async$solveCaptcha(this, string, string2, string3, n, completableFuture22, buffer, string4, buffer2, string5, buffer3, string6, buffer4, null, 11, arg_0));
            }
            completableFuture21.join();
            CompletableFuture completableFuture23 = this.execute("Sensor 4/4", okayStatusFunc, this::bundleReq, buffer4);
            if (!completableFuture23.isDone()) {
                CompletableFuture completableFuture24 = completableFuture23;
                return ((CompletableFuture)completableFuture24.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPX.async$solveCaptcha(this, string, string2, string3, n, completableFuture24, buffer, string4, buffer2, string5, buffer3, string6, buffer4, null, 12, arg_0));
            }
            String string7 = (String)completableFuture23.join();
            if (string7.contains("cv|0")) {
                this.logger.info("Successfully solved captcha");
                MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
                if (this.getVid() != null && !this.getVid().isBlank()) {
                    multiMap.add(VID_COOKIE, (CharSequence)this.getVid());
                }
                multiMap.add(RF_VALUE, ONE_VALUE);
                multiMap.add(FP_VALUE, ONE_VALUE);
                multiMap.add(CFP_VALUE, ONE_VALUE);
                this.parseResultCookies(string7, multiMap);
                return CompletableFuture.completedFuture(multiMap);
            }
            this.logger.error("Retrying captcha solve...");
            this.sid = null;
            CompletableFuture completableFuture25 = VertxUtil.randomSleep(1000L);
            if (!completableFuture25.isDone()) {
                CompletableFuture completableFuture26 = completableFuture25;
                return ((CompletableFuture)completableFuture26.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPX.async$solveCaptcha(this, string, string2, string3, n, completableFuture26, buffer, string4, buffer2, string5, buffer3, string6, buffer4, string7, 13, arg_0));
            }
            completableFuture25.join();
            ++n;
        }
    }

    @Override
    public String getVid() {
        return this.cookieSession.getString("vid", null);
    }

    public HttpRequest lambda$solve$4() {
        return this.apiRequest("2", null);
    }

    public void parseResultCookies(String string, MultiMap multiMap) {
        try {
            Matcher matcher = PXDE_PATTERN.matcher(string);
            Matcher matcher2 = BAKE_PATTERN.matcher(string);
            if (matcher2.find()) {
                multiMap.add(PX3_VALUE, (CharSequence)matcher2.group(1));
            }
            if (!matcher.find()) return;
            multiMap.add(PXDE_VALUE, (CharSequence)matcher.group(1));
            return;
        }
        catch (Throwable throwable) {
            if (!this.logger.isDebugEnabled()) return;
            this.logger.debug("Failed to find px3 or pxde ", throwable);
        }
    }

    public Buffer lambda$new$1(HttpResponse httpResponse) {
        if (httpResponse.statusCode() != 200) return null;
        this.userAgent = httpResponse.bodyAsString();
        this.secUA = httpResponse.getHeader("sec-ua");
        this.deviceNumber = httpResponse.getHeader("device");
        return httpResponse.bodyAsBuffer();
    }

    public HttpRequest lambda$solveCaptcha$5() {
        return this.apiRequest("1", true);
    }

    public HttpRequest collectorReq() {
        HttpRequest httpRequest = this.client.postAbs("https://collector-pxu6b0qd2s.px-cloud.net/api/v2/collector").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("sec-ch-ua", this.getDeviceSecUA());
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.getDeviceUA());
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("content-type", "application/x-www-form-urlencoded");
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("origin", "https://www.walmart.com");
        httpRequest.putHeader("sec-fetch-site", "cross-site");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.walmart.com/");
        httpRequest.putHeader("accept-encoding", this.getDeviceAcceptEncoding());
        httpRequest.putHeader("accept-language", this.getDeviceLang());
        return httpRequest;
    }

    @Override
    public String getDeviceSecUA() {
        if (this.secUA != null) return this.secUA;
        return DEFAULT_SEC_UA.toString();
    }

    public HttpRequest lambda$solve$3() {
        return this.apiRequest("1", null);
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    public static CompletableFuture async$solveCaptcha(DesktopPX var0, String var1_1, String var2_2, String var3_3, int var4_4, CompletableFuture var5_5, Buffer var6_6, String var7_7, Buffer var8_8, String var9_9, Buffer var10_10, String var11_11, Buffer var12_12, String var13_13, int var14_14, Object var15_28) {
        switch (var14_14) {
            case 0: {
                var0.uuid = var2_2;
                var0.vid = var1_1;
                var4_4 = 1;
lbl6:
                // 2 sources

                while (true) {
                    if (var4_4 > ThreadLocalRandom.current().nextInt(1, 4)) {
                        var0.delegate.getCookies().removeAnyMatch("_pxhd");
                        return CompletableFuture.completedFuture(MultiMap.caseInsensitiveMultiMap());
                    }
                    var0.performance = null;
                    v0 = var0.execute("API 1", var0.okayAndConversionFunc, (Supplier<HttpRequest>)LambdaMetafactory.metafactory(null, null, null, ()Ljava/lang/Object;, lambda$solveCaptcha$5(), ()Lio/vertx/ext/web/client/HttpRequest;)((DesktopPX)var0), var0.buildReqBody(null, null));
                    if (!v0.isDone()) {
                        var14_15 = v0;
                        return var14_15.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solveCaptcha(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPX java.lang.String java.lang.String java.lang.String int java.util.concurrent.CompletableFuture io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPX)var0, (String)var1_1, (String)var2_2, (String)var3_3, (int)var4_4, (CompletableFuture)var14_15, null, null, null, null, null, null, null, null, (int)1));
                    }
                    ** GOTO lbl18
                    break;
                }
            }
            case 1: {
                v0 = var5_5;
lbl18:
                // 2 sources

                var5_5 = (Buffer)v0.join();
                v1 = VertxUtil.hardCodedSleep(var0.waitTime);
                if (!v1.isDone()) {
                    var14_16 = v1;
                    return var14_16.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solveCaptcha(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPX java.lang.String java.lang.String java.lang.String int java.util.concurrent.CompletableFuture io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPX)var0, (String)var1_1, (String)var2_2, (String)var3_3, (int)var4_4, (CompletableFuture)var14_16, (Buffer)var5_5, null, null, null, null, null, null, null, (int)2));
                }
                ** GOTO lbl27
            }
            case 2: {
                v1 = var5_5;
                var5_5 = var6_6 /* !! */ ;
lbl27:
                // 2 sources

                v1.join();
                v2 = var0.execute("Sensor 1/4", DesktopPX.okayStatusFunc, (Supplier<HttpRequest>)LambdaMetafactory.metafactory(null, null, null, ()Ljava/lang/Object;, bundleReq(), ()Lio/vertx/ext/web/client/HttpRequest;)((DesktopPX)var0), var5_5);
                if (!v2.isDone()) {
                    var14_17 = v2;
                    return var14_17.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solveCaptcha(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPX java.lang.String java.lang.String java.lang.String int java.util.concurrent.CompletableFuture io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPX)var0, (String)var1_1, (String)var2_2, (String)var3_3, (int)var4_4, (CompletableFuture)var14_17, (Buffer)var5_5, null, null, null, null, null, null, null, (int)3));
                }
                ** GOTO lbl37
            }
            case 3: {
                v2 = var5_5;
                var5_5 = var6_6 /* !! */ ;
lbl37:
                // 2 sources

                if (!(v3 = var0.execute("API 2", var0.okayAndConversionFunc, (Supplier<HttpRequest>)LambdaMetafactory.metafactory(null, null, null, ()Ljava/lang/Object;, lambda$solveCaptcha$6(), ()Lio/vertx/ext/web/client/HttpRequest;)((DesktopPX)var0), var0.buildReqBody((String)(var6_6 /* !! */  = (String)v2.join()), var0.performance))).isDone()) {
                    var14_18 = v3;
                    return var14_18.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solveCaptcha(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPX java.lang.String java.lang.String java.lang.String int java.util.concurrent.CompletableFuture io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPX)var0, (String)var1_1, (String)var2_2, (String)var3_3, (int)var4_4, (CompletableFuture)var14_18, (Buffer)var5_5, (String)var6_6 /* !! */ , null, null, null, null, null, null, (int)4));
                }
                ** GOTO lbl46
            }
            case 4: {
                v3 = var5_5;
                v4 = var6_6 /* !! */ ;
                var6_6 /* !! */  = var7_7;
                var5_5 = v4;
lbl46:
                // 2 sources

                var7_7 = (Buffer)v3.join();
                v5 = VertxUtil.hardCodedSleep(var0.waitTime);
                if (!v5.isDone()) {
                    var14_19 = v5;
                    return var14_19.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solveCaptcha(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPX java.lang.String java.lang.String java.lang.String int java.util.concurrent.CompletableFuture io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPX)var0, (String)var1_1, (String)var2_2, (String)var3_3, (int)var4_4, (CompletableFuture)var14_19, (Buffer)var5_5, (String)var6_6 /* !! */ , (Buffer)var7_7, null, null, null, null, null, (int)5));
                }
                ** GOTO lbl59
            }
            case 5: {
                v5 = var5_5;
                v6 = var6_6 /* !! */ ;
                v7 = var7_7;
                var7_7 = var8_8 /* !! */ ;
                var6_6 /* !! */  = v7;
                var5_5 = v6;
lbl59:
                // 2 sources

                v5.join();
                v8 = var0.execute("Sensor 2/4", DesktopPX.okayStatusFunc, (Supplier<HttpRequest>)LambdaMetafactory.metafactory(null, null, null, ()Ljava/lang/Object;, bundleReq(), ()Lio/vertx/ext/web/client/HttpRequest;)((DesktopPX)var0), var7_7);
                if (!v8.isDone()) {
                    var14_20 = v8;
                    return var14_20.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solveCaptcha(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPX java.lang.String java.lang.String java.lang.String int java.util.concurrent.CompletableFuture io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPX)var0, (String)var1_1, (String)var2_2, (String)var3_3, (int)var4_4, (CompletableFuture)var14_20, (Buffer)var5_5, (String)var6_6 /* !! */ , (Buffer)var7_7, null, null, null, null, null, (int)6));
                }
                ** GOTO lbl73
            }
            case 6: {
                v8 = var5_5;
                v9 = var6_6 /* !! */ ;
                v10 = var7_7;
                var7_7 = var8_8 /* !! */ ;
                var6_6 /* !! */  = v10;
                var5_5 = v9;
lbl73:
                // 2 sources

                var8_8 /* !! */  = (String)v8.join();
                v11 = var0.execute("API 3", var0.okayAndConversionFunc, (Supplier<HttpRequest>)LambdaMetafactory.metafactory(null, null, null, ()Ljava/lang/Object;, lambda$solveCaptcha$7(), ()Lio/vertx/ext/web/client/HttpRequest;)((DesktopPX)var0), var0.buildReqBody((String)var6_6 /* !! */ , var0.performance));
                if (!v11.isDone()) {
                    var14_21 = v11;
                    return var14_21.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solveCaptcha(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPX java.lang.String java.lang.String java.lang.String int java.util.concurrent.CompletableFuture io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPX)var0, (String)var1_1, (String)var2_2, (String)var3_3, (int)var4_4, (CompletableFuture)var14_21, (Buffer)var5_5, (String)var6_6 /* !! */ , (Buffer)var7_7, (String)var8_8 /* !! */ , null, null, null, null, (int)7));
                }
                ** GOTO lbl88
            }
            case 7: {
                v11 = var5_5;
                v12 = var6_6 /* !! */ ;
                v13 = var7_7;
                v14 = var8_8 /* !! */ ;
                var8_8 /* !! */  = var9_9;
                var7_7 = v14;
                var6_6 /* !! */  = v13;
                var5_5 = v12;
lbl88:
                // 2 sources

                var9_9 = (Buffer)v11.join();
                v15 = VertxUtil.hardCodedSleep(var0.waitTime);
                if (!v15.isDone()) {
                    var14_22 = v15;
                    return var14_22.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solveCaptcha(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPX java.lang.String java.lang.String java.lang.String int java.util.concurrent.CompletableFuture io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPX)var0, (String)var1_1, (String)var2_2, (String)var3_3, (int)var4_4, (CompletableFuture)var14_22, (Buffer)var5_5, (String)var6_6 /* !! */ , (Buffer)var7_7, (String)var8_8 /* !! */ , (Buffer)var9_9, null, null, null, (int)8));
                }
                ** GOTO lbl105
            }
            case 8: {
                v15 = var5_5;
                v16 = var6_6 /* !! */ ;
                v17 = var7_7;
                v18 = var8_8 /* !! */ ;
                v19 = var9_9;
                var9_9 = var10_10 /* !! */ ;
                var8_8 /* !! */  = v19;
                var7_7 = v18;
                var6_6 /* !! */  = v17;
                var5_5 = v16;
lbl105:
                // 2 sources

                v15.join();
                v20 = var0.execute("Sensor 3/4", DesktopPX.okayStatusFunc, (Supplier<HttpRequest>)LambdaMetafactory.metafactory(null, null, null, ()Ljava/lang/Object;, lambda$solveCaptcha$8(io.vertx.core.buffer.Buffer ), ()Lio/vertx/ext/web/client/HttpRequest;)((DesktopPX)var0, (Buffer)var9_9), null);
                if (!v20.isDone()) {
                    var14_23 = v20;
                    return var14_23.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solveCaptcha(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPX java.lang.String java.lang.String java.lang.String int java.util.concurrent.CompletableFuture io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPX)var0, (String)var1_1, (String)var2_2, (String)var3_3, (int)var4_4, (CompletableFuture)var14_23, (Buffer)var5_5, (String)var6_6 /* !! */ , (Buffer)var7_7, (String)var8_8 /* !! */ , (Buffer)var9_9, null, null, null, (int)9));
                }
                ** GOTO lbl123
            }
            case 9: {
                v20 = var5_5;
                v21 = var6_6 /* !! */ ;
                v22 = var7_7;
                v23 = var8_8 /* !! */ ;
                v24 = var9_9;
                var9_9 = var10_10 /* !! */ ;
                var8_8 /* !! */  = v24;
                var7_7 = v23;
                var6_6 /* !! */  = v22;
                var5_5 = v21;
lbl123:
                // 2 sources

                var10_10 /* !! */  = (String)v20.join();
                v25 = var0.execute("API 4", var0.okayAndConversionFunc, (Supplier<HttpRequest>)LambdaMetafactory.metafactory(null, null, null, ()Ljava/lang/Object;, lambda$solveCaptcha$9(), ()Lio/vertx/ext/web/client/HttpRequest;)((DesktopPX)var0), var0.buildReqBody((String)var6_6 /* !! */ , var0.performance));
                if (!v25.isDone()) {
                    var14_24 = v25;
                    return var14_24.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solveCaptcha(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPX java.lang.String java.lang.String java.lang.String int java.util.concurrent.CompletableFuture io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPX)var0, (String)var1_1, (String)var2_2, (String)var3_3, (int)var4_4, (CompletableFuture)var14_24, (Buffer)var5_5, (String)var6_6 /* !! */ , (Buffer)var7_7, (String)var8_8 /* !! */ , (Buffer)var9_9, (String)var10_10 /* !! */ , null, null, (int)10));
                }
                ** GOTO lbl142
            }
            case 10: {
                v25 = var5_5;
                v26 = var6_6 /* !! */ ;
                v27 = var7_7;
                v28 = var8_8 /* !! */ ;
                v29 = var9_9;
                v30 = var10_10 /* !! */ ;
                var10_10 /* !! */  = var11_11;
                var9_9 = v30;
                var8_8 /* !! */  = v29;
                var7_7 = v28;
                var6_6 /* !! */  = v27;
                var5_5 = v26;
lbl142:
                // 2 sources

                var11_11 = (Buffer)v25.join();
                v31 = VertxUtil.hardCodedSleep(var0.waitTime);
                if (!v31.isDone()) {
                    var14_25 = v31;
                    return var14_25.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solveCaptcha(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPX java.lang.String java.lang.String java.lang.String int java.util.concurrent.CompletableFuture io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPX)var0, (String)var1_1, (String)var2_2, (String)var3_3, (int)var4_4, (CompletableFuture)var14_25, (Buffer)var5_5, (String)var6_6 /* !! */ , (Buffer)var7_7, (String)var8_8 /* !! */ , (Buffer)var9_9, (String)var10_10 /* !! */ , (Buffer)var11_11, null, (int)11));
                }
                ** GOTO lbl163
            }
            case 11: {
                v31 = var5_5;
                v32 = var6_6 /* !! */ ;
                v33 = var7_7;
                v34 = var8_8 /* !! */ ;
                v35 = var9_9;
                v36 = var10_10 /* !! */ ;
                v37 = var11_11;
                var11_11 = var12_12;
                var10_10 /* !! */  = v37;
                var9_9 = v36;
                var8_8 /* !! */  = v35;
                var7_7 = v34;
                var6_6 /* !! */  = v33;
                var5_5 = v32;
lbl163:
                // 2 sources

                v31.join();
                v38 = var0.execute("Sensor 4/4", DesktopPX.okayStatusFunc, (Supplier<HttpRequest>)LambdaMetafactory.metafactory(null, null, null, ()Ljava/lang/Object;, bundleReq(), ()Lio/vertx/ext/web/client/HttpRequest;)((DesktopPX)var0), var11_11);
                if (!v38.isDone()) {
                    var14_26 = v38;
                    return var14_26.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solveCaptcha(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPX java.lang.String java.lang.String java.lang.String int java.util.concurrent.CompletableFuture io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPX)var0, (String)var1_1, (String)var2_2, (String)var3_3, (int)var4_4, (CompletableFuture)var14_26, (Buffer)var5_5, (String)var6_6 /* !! */ , (Buffer)var7_7, (String)var8_8 /* !! */ , (Buffer)var9_9, (String)var10_10 /* !! */ , (Buffer)var11_11, null, (int)12));
                }
                ** GOTO lbl185
            }
            case 12: {
                v38 = var5_5;
                v39 = var6_6 /* !! */ ;
                v40 = var7_7;
                v41 = var8_8 /* !! */ ;
                v42 = var9_9;
                v43 = var10_10 /* !! */ ;
                v44 = var11_11;
                var11_11 = var12_12;
                var10_10 /* !! */  = v44;
                var9_9 = v43;
                var8_8 /* !! */  = v42;
                var7_7 = v41;
                var6_6 /* !! */  = v40;
                var5_5 = v39;
lbl185:
                // 2 sources

                if ((var12_12 = (String)v38.join()).contains("cv|0")) {
                    var0.logger.info("Successfully solved captcha");
                    var13_13 = MultiMap.caseInsensitiveMultiMap();
                    if (var0.getVid() != null && !var0.getVid().isBlank()) {
                        var13_13.add(DesktopPX.VID_COOKIE, (CharSequence)var0.getVid());
                    }
                    var13_13.add(DesktopPX.RF_VALUE, DesktopPX.ONE_VALUE);
                    var13_13.add(DesktopPX.FP_VALUE, DesktopPX.ONE_VALUE);
                    var13_13.add(DesktopPX.CFP_VALUE, DesktopPX.ONE_VALUE);
                    var0.parseResultCookies((String)var12_12, (MultiMap)var13_13);
                    return CompletableFuture.completedFuture(var13_13);
                }
                var0.logger.error("Retrying captcha solve...");
                var0.sid = null;
                v45 = VertxUtil.randomSleep(1000L);
                if (!v45.isDone()) {
                    var14_27 = v45;
                    return var14_27.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solveCaptcha(io.trickle.task.antibot.impl.px.payload.captcha.DesktopPX java.lang.String java.lang.String java.lang.String int java.util.concurrent.CompletableFuture io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.String io.vertx.core.buffer.Buffer java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((DesktopPX)var0, (String)var1_1, (String)var2_2, (String)var3_3, (int)var4_4, (CompletableFuture)var14_27, (Buffer)var5_5, (String)var6_6 /* !! */ , (Buffer)var7_7, (String)var8_8 /* !! */ , (Buffer)var9_9, (String)var10_10 /* !! */ , (Buffer)var11_11, (String)var12_12, (int)13));
                }
                ** GOTO lbl223
            }
            case 13: {
                v45 = var5_5;
                v46 = var6_6 /* !! */ ;
                v47 = var7_7;
                v48 = var8_8 /* !! */ ;
                v49 = var9_9;
                v50 = var10_10 /* !! */ ;
                v51 = var11_11;
                v52 = var12_12;
                var12_12 = var13_13;
                var11_11 = v52;
                var10_10 /* !! */  = v51;
                var9_9 = v50;
                var8_8 /* !! */  = v49;
                var7_7 = v48;
                var6_6 /* !! */  = v47;
                var5_5 = v46;
lbl223:
                // 2 sources

                v45.join();
                ++var4_4;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public HttpRequest bundleReq() {
        HttpRequest httpRequest = this.client.postAbs("https://collector-pxu6b0qd2s.px-cloud.net/assets/js/bundle").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("sec-ch-ua", this.getDeviceSecUA());
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.getDeviceUA());
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("content-type", "application/x-www-form-urlencoded");
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("origin", "https://www.walmart.com");
        httpRequest.putHeader("sec-fetch-site", "cross-site");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.walmart.com/");
        httpRequest.putHeader("accept-encoding", this.getDeviceAcceptEncoding());
        httpRequest.putHeader("accept-language", this.getDeviceLang());
        return httpRequest;
    }

    public Buffer lambda$new$2(HttpResponse httpResponse) {
        if (httpResponse.statusCode() != 200) return null;
        if (httpResponse.getHeader("performance") == null) return null;
        this.uuid = httpResponse.getHeader("uuid");
        this.sid = httpResponse.getHeader("sid");
        this.vid = httpResponse.getHeader("vid");
        this.waitTime = Long.parseLong(httpResponse.getHeader("completionEpoch")) - System.currentTimeMillis();
        this.performance = new JsonArray(httpResponse.getHeader("performance"));
        if (httpResponse.getHeader("cts") != null) {
            this.delegate.getCookies().put(String.valueOf(CTS_VALUE), httpResponse.getHeader("cts"), ".walmart.com");
        }
        this.deviceNumber = httpResponse.getHeader("device");
        return httpResponse.bodyAsBuffer();
    }

    public HttpRequest lambda$initialise$10() {
        return this.apiRequest("ua", null).method(HttpMethod.GET);
    }
}

