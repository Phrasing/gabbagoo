/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.MultiMap
 *  io.vertx.core.buffer.Buffer
 *  io.vertx.core.json.JsonObject
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.client.HttpResponse
 *  io.vertx.ext.web.client.impl.HttpRequestImpl
 *  io.vertx.ext.web.codec.BodyCodec
 */
package io.trickle.task.antibot.impl.px;

import io.trickle.core.actor.TaskActor;
import io.trickle.task.antibot.impl.px.PXTokenBase;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Headers$Pseudo;
import io.trickle.util.request.Request;
import io.trickle.webclient.ClientType;
import io.trickle.webclient.RealClient;
import io.trickle.webclient.RealClientFactory;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.impl.HttpRequestImpl;
import io.vertx.ext.web.codec.BodyCodec;
import java.lang.invoke.LambdaMetafactory;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PXTokenAPI
extends PXTokenBase {
    public String sid;
    public static Pattern BAKE_PATTERN = Pattern.compile("bake\\|.*?\\|.*?\\|(.*?)\\|");
    public long requestTime;
    public String vid;
    public String uuid;
    public RealClient apiClient;

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$requestExecutor(PXTokenAPI var0, Supplier var1_1, Object var2_2, int var3_3, CompletableFuture var4_4, HttpResponse var5_5, int var6_6, Object var7_7) {
        switch (var6_6) {
            case 0: {
                var3_3 = 0;
lbl4:
                // 2 sources

                while (true) {
                    if (var0.client.isActive() == false) return CompletableFuture.completedFuture(null);
                    if (var3_3++ > 100) return CompletableFuture.completedFuture(null);
                    if (var2_2 != null) ** GOTO lbl13
                    v0 = Request.send((HttpRequest)var1_1.get());
                    if (!v0.isDone()) {
                        var5_5 = v0;
                        return var5_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$requestExecutor(io.trickle.task.antibot.impl.px.PXTokenAPI java.util.function.Supplier java.lang.Object int java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PXTokenAPI)var0, (Supplier)var1_1, (Object)var2_2, (int)var3_3, (CompletableFuture)var5_5, null, (int)1));
                    }
                    ** GOTO lbl38
lbl13:
                    // 1 sources

                    if (!(var2_2 instanceof Buffer)) ** GOTO lbl19
                    v1 = Request.send((HttpRequest)var1_1.get(), (Buffer)var2_2);
                    if (!v1.isDone()) {
                        var5_5 = v1;
                        return var5_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$requestExecutor(io.trickle.task.antibot.impl.px.PXTokenAPI java.util.function.Supplier java.lang.Object int java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PXTokenAPI)var0, (Supplier)var1_1, (Object)var2_2, (int)var3_3, (CompletableFuture)var5_5, null, (int)2));
                    }
                    ** GOTO lbl42
lbl19:
                    // 1 sources

                    if (!(var2_2 instanceof MultiMap)) ** GOTO lbl25
                    v2 = Request.send((HttpRequest)var1_1.get(), (MultiMap)var2_2);
                    if (!v2.isDone()) {
                        var5_5 = v2;
                        return var5_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$requestExecutor(io.trickle.task.antibot.impl.px.PXTokenAPI java.util.function.Supplier java.lang.Object int java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PXTokenAPI)var0, (Supplier)var1_1, (Object)var2_2, (int)var3_3, (CompletableFuture)var5_5, null, (int)3));
                    }
                    ** GOTO lbl46
lbl25:
                    // 1 sources

                    var4_4 = null;
lbl26:
                    // 4 sources

                    while (true) {
                        if (var4_4 != null) {
                            if (((HttpRequestImpl)var1_1.get()).uri().contains("b/g") == false) return CompletableFuture.completedFuture(var4_4.bodyAsJsonObject());
                            return CompletableFuture.completedFuture(null);
                        }
                        var0.logger.warn("Retrying...");
                        v3 = VertxUtil.randomSleep(2500L);
                        if (!v3.isDone()) {
                            var5_5 = v3;
                            return var5_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$requestExecutor(io.trickle.task.antibot.impl.px.PXTokenAPI java.util.function.Supplier java.lang.Object int java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PXTokenAPI)var0, (Supplier)var1_1, (Object)var2_2, (int)var3_3, (CompletableFuture)var5_5, (HttpResponse)var4_4, (int)4));
                        }
                        ** GOTO lbl51
                        break;
                    }
                    break;
                }
            }
            case 1: {
                v0 = var4_4;
lbl38:
                // 2 sources

                var4_4 = (HttpResponse)v0.join();
                ** GOTO lbl26
            }
            case 2: {
                v1 = var4_4;
lbl42:
                // 2 sources

                var4_4 = (HttpResponse)v1.join();
                ** GOTO lbl26
            }
            case 3: {
                v2 = var4_4;
lbl46:
                // 2 sources

                var4_4 = (HttpResponse)v2.join();
                ** continue;
            }
            case 4: {
                v3 = var4_4;
                var4_4 = var5_5;
lbl51:
                // 2 sources

                v3.join();
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public HttpRequest lambda$solveCaptcha$8() {
        return this.apiCaptchaRequest(2, "https://www.hibbett.com/");
    }

    public Optional parseResult(String string) {
        Matcher matcher = BAKE_PATTERN.matcher(string);
        if (!matcher.find()) return Optional.empty();
        return Optional.of("3:" + matcher.group(1));
    }

    public static JsonObject lambda$solveCaptcha$7(JsonObject jsonObject) {
        return jsonObject;
    }

    public static JsonObject lambda$solveCaptcha$9(JsonObject jsonObject) {
        return jsonObject.getJsonObject("result");
    }

    public static JsonObject lambda$solveCaptcha$4(JsonObject jsonObject) {
        return jsonObject;
    }

    public CompletableFuture handleBundle(Supplier supplier) {
        MultiMap multiMap = this.jsonToForm(supplier);
        return this.requestExecutor(this::bundleReq, multiMap);
    }

    @Override
    public CompletableFuture initialize() {
        return CompletableFuture.completedFuture(true);
    }

    @Override
    public String getVid() {
        return "null";
    }

    @Override
    public CompletableFuture solveCaptcha(String string, String string2) {
        return CompletableFuture.completedFuture(true);
    }

    public MultiMap jsonToForm(Supplier supplier) {
        JsonObject jsonObject = (JsonObject)supplier.get();
        MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
        Iterator iterator = jsonObject.fieldNames().iterator();
        while (iterator.hasNext()) {
            String string = (String)iterator.next();
            String string2 = jsonObject.getString(string, null);
            if (string2 == null) continue;
            if (this.uuid == null && string.equals("uuid")) {
                this.uuid = string2;
                this.logger.info("UUID -> {}", (Object)this.uuid);
            }
            if (string.equals("appId")) {
                multiMap.add(string, "PXAJDckzHD");
                continue;
            }
            multiMap.add(string, string2);
        }
        return multiMap;
    }

    public HttpRequest imageReq(MultiMap multiMap) {
        StringBuilder stringBuilder = new StringBuilder();
        multiMap.forEach(arg_0 -> PXTokenAPI.lambda$imageReq$0(stringBuilder, arg_0));
        HttpRequest httpRequest = this.client.getAbs("https://collector-" + "PXAJDckzHD".toLowerCase() + ".perimeterx.net/b/g?" + stringBuilder).as(BodyCodec.buffer());
        httpRequest.putHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.190 Safari/537.36");
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("origin", "https://www.hibbett.com");
        httpRequest.putHeader("sec-fetch-site", "cross-site");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.hibbett.com/");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9,de-DE;q=0.8,de;q=0.7");
        return httpRequest;
    }

    public static JsonObject lambda$solveCaptcha$2(JsonObject jsonObject) {
        return jsonObject.getJsonObject("result");
    }

    public CompletableFuture requestExecutor(Supplier supplier, Object object) {
        int n = 0;
        while (this.client.isActive()) {
            HttpResponse httpResponse;
            if (n++ > 100) return CompletableFuture.completedFuture(null);
            if (object == null) {
                CompletableFuture completableFuture = Request.send((HttpRequest)supplier.get());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> PXTokenAPI.async$requestExecutor(this, (Supplier)supplier, object, n, completableFuture2, null, 1, arg_0));
                }
                httpResponse = (HttpResponse)completableFuture.join();
            } else if (object instanceof Buffer) {
                CompletableFuture completableFuture = Request.send((HttpRequest)supplier.get(), (Buffer)object);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture3 = completableFuture;
                    return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> PXTokenAPI.async$requestExecutor(this, (Supplier)supplier, object, n, completableFuture3, null, 2, arg_0));
                }
                httpResponse = (HttpResponse)completableFuture.join();
            } else if (object instanceof MultiMap) {
                CompletableFuture completableFuture = Request.send((HttpRequest)supplier.get(), (MultiMap)object);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> PXTokenAPI.async$requestExecutor(this, (Supplier)supplier, object, n, completableFuture4, null, 3, arg_0));
                }
                httpResponse = (HttpResponse)completableFuture.join();
            } else {
                httpResponse = null;
            }
            if (httpResponse != null) {
                if (!((HttpRequestImpl)supplier.get()).uri().contains("b/g")) return CompletableFuture.completedFuture(httpResponse.bodyAsJsonObject());
                return CompletableFuture.completedFuture(null);
            }
            this.logger.warn("Retrying...");
            CompletableFuture completableFuture = VertxUtil.randomSleep(2500L);
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture5 = completableFuture;
                return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> PXTokenAPI.async$requestExecutor(this, (Supplier)supplier, object, n, completableFuture5, httpResponse, 4, arg_0));
            }
            completableFuture.join();
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture handleToken(Supplier supplier) {
        MultiMap multiMap = this.jsonToForm(supplier);
        return this.requestExecutor(this::collectorRequest, multiMap);
    }

    public HttpRequest lambda$solveCaptcha$5(MultiMap multiMap) {
        return this.imageReq(multiMap);
    }

    public PXTokenAPI(TaskActor taskActor) {
        super(taskActor, ClientType.PX_SDK_PIXEL_3);
        this.apiClient = RealClientFactory.build(taskActor.getVertx(), ClientType.PX_SDK_PIXEL_3);
        this.requestTime = 0L;
    }

    @Override
    public CompletableFuture reInit() {
        return CompletableFuture.completedFuture(true);
    }

    @Override
    public String getSid() {
        return "null";
    }

    @Override
    public boolean isTokenCaptcha() {
        return false;
    }

    public HttpRequest collectorRequest() {
        String string = "PerimeterX Android SDK/" + "v1.13.2".substring(1);
        HttpRequest httpRequest = this.client.postAbs("https://collector-" + "PX9Qx3Rve4".toLowerCase() + ".perimeterx.net/api/v1/collector/mobile").timeout(TimeUnit.SECONDS.toMillis(15L)).as(BodyCodec.buffer());
        httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
        httpRequest.putHeader("user-agent", string);
        httpRequest.putHeader("content-type", "application/x-www-form-urlencoded");
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("accept-encoding", "gzip");
        return httpRequest;
    }

    @Override
    public CompletableFuture awaitInit() {
        return this.initFuture;
    }

    public HttpRequest lambda$solveCaptcha$1() {
        return this.apiCaptchaRequest(1, "https://www.hibbett.com/");
    }

    public HttpRequest lambda$solveCaptcha$3() {
        return this.apiCaptchaRequest(15, "https://www.hibbett.com/");
    }

    public CompletableFuture solveCaptcha(String string, String string2, String string3) {
        this.logger.info("Solving captcha via api");
        int n = 0;
        while (n < 5) {
            try {
                CompletableFuture completableFuture = this.requestExecutor(this::lambda$solveCaptcha$1, new JsonObject().put("vid", (Object)string).put("sid", (Object)string2).put("uuid", (Object)string3).toBuffer());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> PXTokenAPI.async$solveCaptcha(this, string, string2, string3, n, completableFuture2, null, null, null, null, null, null, null, null, 0, null, 1, arg_0));
                }
                JsonObject jsonObject = (JsonObject)completableFuture.join();
                if (jsonObject != null && jsonObject.containsKey("result")) {
                    this.requestTime = jsonObject.getLong("ts");
                    CompletableFuture completableFuture3 = this.handleBundle(() -> PXTokenAPI.lambda$solveCaptcha$2(jsonObject));
                    if (!completableFuture3.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture3;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> PXTokenAPI.async$solveCaptcha(this, string, string2, string3, n, completableFuture4, jsonObject, null, null, null, null, null, null, null, 0, null, 2, arg_0));
                    }
                    JsonObject jsonObject2 = (JsonObject)completableFuture3.join();
                    if (jsonObject2 != null && jsonObject2.containsKey("do")) {
                        JsonObject jsonObject3 = new JsonObject().put("a", (Object)jsonObject2).put("b", (Object)string3).put("c", (Object)this.requestTime).put("vid", (Object)string).put("sid", (Object)string2).put("uuid", (Object)string3);
                        CompletableFuture completableFuture5 = this.requestExecutor(this::lambda$solveCaptcha$3, jsonObject3.toBuffer());
                        if (!completableFuture5.isDone()) {
                            CompletableFuture completableFuture6 = completableFuture5;
                            return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> PXTokenAPI.async$solveCaptcha(this, string, string2, string3, n, completableFuture6, jsonObject, jsonObject2, jsonObject3, null, null, null, null, null, 0, null, 3, arg_0));
                        }
                        JsonObject jsonObject4 = (JsonObject)completableFuture5.join();
                        if (jsonObject4 != null) {
                            MultiMap multiMap = this.jsonToForm(() -> PXTokenAPI.lambda$solveCaptcha$4(jsonObject4));
                            CompletableFuture completableFuture7 = this.requestExecutor(() -> this.lambda$solveCaptcha$5(multiMap), null);
                            if (!completableFuture7.isDone()) {
                                CompletableFuture completableFuture8 = completableFuture7;
                                return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> PXTokenAPI.async$solveCaptcha(this, string, string2, string3, n, completableFuture8, jsonObject, jsonObject2, jsonObject3, jsonObject4, multiMap, null, null, null, 0, null, 4, arg_0));
                            }
                            completableFuture7.join();
                            CompletableFuture completableFuture9 = this.requestExecutor(this::lambda$solveCaptcha$6, jsonObject3.toBuffer());
                            if (!completableFuture9.isDone()) {
                                CompletableFuture completableFuture10 = completableFuture9;
                                return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> PXTokenAPI.async$solveCaptcha(this, string, string2, string3, n, completableFuture10, jsonObject, jsonObject2, jsonObject3, jsonObject4, multiMap, null, null, null, 0, null, 5, arg_0));
                            }
                            JsonObject jsonObject5 = (JsonObject)completableFuture9.join();
                            if (jsonObject5 != null) {
                                CompletableFuture completableFuture11 = this.handleBundle(() -> PXTokenAPI.lambda$solveCaptcha$7(jsonObject5));
                                if (!completableFuture11.isDone()) {
                                    CompletableFuture completableFuture12 = completableFuture11;
                                    return ((CompletableFuture)completableFuture12.exceptionally(Function.identity())).thenCompose(arg_0 -> PXTokenAPI.async$solveCaptcha(this, string, string2, string3, n, completableFuture12, jsonObject, jsonObject2, jsonObject3, jsonObject4, multiMap, jsonObject5, null, null, 0, null, 6, arg_0));
                                }
                                JsonObject jsonObject6 = (JsonObject)completableFuture11.join();
                                if (jsonObject6 != null && jsonObject6.containsKey("do")) {
                                    CompletableFuture completableFuture13 = this.requestExecutor(this::lambda$solveCaptcha$8, jsonObject3.toBuffer());
                                    if (!completableFuture13.isDone()) {
                                        CompletableFuture completableFuture14 = completableFuture13;
                                        return ((CompletableFuture)completableFuture14.exceptionally(Function.identity())).thenCompose(arg_0 -> PXTokenAPI.async$solveCaptcha(this, string, string2, string3, n, completableFuture14, jsonObject, jsonObject2, jsonObject3, jsonObject4, multiMap, jsonObject5, jsonObject6, null, 0, null, 7, arg_0));
                                    }
                                    JsonObject jsonObject7 = (JsonObject)completableFuture13.join();
                                    if (jsonObject7 != null) {
                                        int n2 = (int)(jsonObject7.getDouble("delay") * Double.longBitsToDouble(4652007308841189376L));
                                        this.logger.info("Sleep for {}", (Object)n2);
                                        CompletableFuture completableFuture15 = VertxUtil.hardCodedSleep(n2);
                                        if (!completableFuture15.isDone()) {
                                            CompletableFuture completableFuture16 = completableFuture15;
                                            return ((CompletableFuture)completableFuture16.exceptionally(Function.identity())).thenCompose(arg_0 -> PXTokenAPI.async$solveCaptcha(this, string, string2, string3, n, completableFuture16, jsonObject, jsonObject2, jsonObject3, jsonObject4, multiMap, jsonObject5, jsonObject6, jsonObject7, n2, null, 8, arg_0));
                                        }
                                        completableFuture15.join();
                                        CompletableFuture completableFuture17 = this.handleBundle(() -> PXTokenAPI.lambda$solveCaptcha$9(jsonObject7));
                                        if (!completableFuture17.isDone()) {
                                            CompletableFuture completableFuture18 = completableFuture17;
                                            return ((CompletableFuture)completableFuture18.exceptionally(Function.identity())).thenCompose(arg_0 -> PXTokenAPI.async$solveCaptcha(this, string, string2, string3, n, completableFuture18, jsonObject, jsonObject2, jsonObject3, jsonObject4, multiMap, jsonObject5, jsonObject6, jsonObject7, n2, null, 9, arg_0));
                                        }
                                        JsonObject jsonObject8 = (JsonObject)completableFuture17.join();
                                        if (jsonObject8 != null && jsonObject8.containsKey("do")) {
                                            System.out.println(jsonObject8);
                                            Optional optional = this.parseCapResult(jsonObject8.toString());
                                            if (optional.isPresent()) {
                                                this.value = optional.get();
                                                System.out.println(jsonObject8);
                                                System.out.println((String)this.value);
                                                return CompletableFuture.completedFuture(true);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            catch (Throwable throwable) {
                throwable.printStackTrace();
                this.logger.error("Error occurred solving cap: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = VertxUtil.hardCodedSleep(5000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture19 = completableFuture;
                    return ((CompletableFuture)completableFuture19.exceptionally(Function.identity())).thenCompose(arg_0 -> PXTokenAPI.async$solveCaptcha(this, string, string2, string3, n, completableFuture19, null, null, null, null, null, null, null, null, 0, throwable, 10, arg_0));
                }
                completableFuture.join();
            }
            ++n;
        }
        return CompletableFuture.completedFuture(true);
    }

    public HttpRequest bundleReq() {
        HttpRequest httpRequest = this.client.postAbs("https://collector-" + "PXAJDckzHD".toLowerCase() + ".px-cloud.net/assets/js/bundle").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"88\", \"Chromium\";v=\"88\", \";Not A Brand\";v=\"99\"");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.190 Safari/537.36");
        httpRequest.putHeader("content-type", "application/x-www-form-urlencoded");
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("origin", "https://www.hibbett.com");
        httpRequest.putHeader("sec-fetch-site", "cross-site");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.hibbett.com/");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9,de-DE;q=0.8,de;q=0.7");
        return httpRequest;
    }

    public static void lambda$imageReq$0(StringBuilder stringBuilder, Map.Entry entry) {
        if (stringBuilder.length() > 0) {
            stringBuilder.append("&");
        }
        if (((String)entry.getValue()).contains("\udd31")) {
            stringBuilder.append((String)entry.getKey()).append("=").append(URLEncoder.encode((String)entry.getValue(), StandardCharsets.UTF_8));
            return;
        }
        stringBuilder.append((String)entry.getKey()).append("=").append((String)entry.getValue());
    }

    public HttpRequest lambda$solveCaptcha$6() {
        return this.apiRequest(2, "https://www.hibbett.com/");
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$solveCaptcha(PXTokenAPI var0, String var1_1, String var2_2, String var3_3, int var4_4, CompletableFuture var5_5, JsonObject var6_7, JsonObject var7_8, JsonObject var8_9, JsonObject var9_10, MultiMap var10_11, JsonObject var11_12, JsonObject var12_13, JsonObject var13_14, int var14_16, Throwable var15_18, int var16_19, Object var17_30) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [2[CASE]], but top level block is 15[WHILELOOP]
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

    public HttpRequest apiRequest(int n, String string) {
        HttpRequest httpRequest = this.client.postAbs("https://px.hwkapi.com/px/" + n).addQueryParam("domain", string).addQueryParam("appId", "PXAJDckzHD").addQueryParam("auth", "test_67b74d34-d55b-4193-aa89-3dde8e1713a6").as(BodyCodec.buffer());
        httpRequest.putHeader("User-Agent", "python-requests/2.24.0");
        httpRequest.putHeader("Accept-Encoding", "gzip, deflate");
        httpRequest.putHeader("Accept", "*/*");
        httpRequest.putHeader("Connection", "keep-alive");
        httpRequest.putHeader("Content-Length", "DEFAULT_VALUE");
        httpRequest.putHeader("Content-Type", "application/json");
        return httpRequest;
    }

    public HttpRequest apiCaptchaRequest(int n, String string) {
        HttpRequest httpRequest = this.client.postAbs("https://px.hwkapi.com/px/captcha/" + n).addQueryParam("domain", string).addQueryParam("appId", "PXAJDckzHD").addQueryParam("auth", "test_67b74d34-d55b-4193-aa89-3dde8e1713a6").as(BodyCodec.buffer());
        httpRequest.putHeader("User-Agent", "python-requests/2.24.0");
        httpRequest.putHeader("Accept-Encoding", "gzip, deflate");
        httpRequest.putHeader("Accept", "*/*");
        httpRequest.putHeader("Connection", "keep-alive");
        httpRequest.putHeader("Content-Length", "DEFAULT_VALUE");
        httpRequest.putHeader("Content-Type", "application/json");
        return httpRequest;
    }

    public Optional parseCapResult(String string) {
        if (!string.contains("cv|0")) {
            return Optional.empty();
        }
        Matcher matcher = BAKE_PATTERN.matcher(string);
        if (!matcher.find()) return Optional.empty();
        return Optional.of("3:" + matcher.group(1));
    }
}

