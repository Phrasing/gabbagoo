/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.teamdev.jxbrowser.net.HttpHeader
 *  com.teamdev.jxbrowser.net.callback.InterceptUrlRequestCallback$Params
 *  io.trickle.util.concurrent.VertxUtil
 *  io.vertx.core.MultiMap
 *  io.vertx.core.buffer.Buffer
 *  io.vertx.core.http.HttpMethod
 *  io.vertx.core.http.RequestOptions
 *  io.vertx.core.json.Json
 *  io.vertx.core.json.JsonArray
 *  io.vertx.core.json.JsonObject
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.client.HttpResponse
 *  io.vertx.ext.web.client.impl.HttpRequestImpl
 *  io.vertx.ext.web.multipart.MultipartForm
 */
package io.trickle.util.request;

import com.teamdev.jxbrowser.net.HttpHeader;
import com.teamdev.jxbrowser.net.callback.InterceptUrlRequestCallback;
import io.trickle.util.concurrent.VertxUtil;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.RequestOptions;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.impl.HttpRequestImpl;
import io.vertx.ext.web.multipart.MultipartForm;
import java.lang.invoke.LambdaMetafactory;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class Request {
    public static int DEFAULT_DELAY = 3000;

    public static CompletableFuture executeTillOk(HttpRequest httpRequest, int n) {
        return Request.execute(httpRequest, true, n);
    }

    public static CompletableFuture executeWithResponse(HttpRequest httpRequest, boolean bl, int n) {
        int n2 = 0;
        do {
            try {
                CompletableFuture completableFuture = Request.send(httpRequest);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Request.async$executeWithResponse(httpRequest, (int)(bl ? 1 : 0), n, n2, completableFuture2, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse != null) {
                    return CompletableFuture.completedFuture(httpResponse);
                }
                if (bl) {
                    CompletableFuture completableFuture3 = VertxUtil.sleep((long)n);
                    if (!completableFuture3.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture3;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Request.async$executeWithResponse(httpRequest, (int)(bl ? 1 : 0), n, n2, completableFuture4, httpResponse, 2, arg_0));
                    }
                    completableFuture3.join();
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
            if (!bl) return CompletableFuture.completedFuture(null);
        } while (++n2 < 3);
        return CompletableFuture.completedFuture(null);
    }

    public static CompletableFuture executeTillOk(HttpRequest httpRequest, Buffer buffer) {
        return Request.execute(httpRequest, true, 3000, buffer);
    }

    public static CompletableFuture executeTillOk(HttpRequest httpRequest) {
        return Request.execute(httpRequest, true, 3000);
    }

    public static CompletableFuture send(HttpRequest httpRequest) {
        return httpRequest.send().toCompletionStage().toCompletableFuture();
    }

    public static CompletableFuture send(HttpRequest httpRequest, JsonObject jsonObject) {
        return httpRequest.sendJson((Object)jsonObject).toCompletionStage().toCompletableFuture();
    }

    public static CompletableFuture execute(HttpRequest httpRequest, boolean bl, int n, Buffer buffer) {
        int n2 = 0;
        do {
            try {
                CompletableFuture completableFuture = buffer == null ? Request.send(httpRequest) : Request.send(httpRequest, buffer);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Request.async$execute$1(httpRequest, (int)(bl ? 1 : 0), n, buffer, n2, completableFuture2, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse != null) {
                    return CompletableFuture.completedFuture(httpResponse.body());
                }
                if (bl) {
                    CompletableFuture completableFuture3 = VertxUtil.sleep((long)n);
                    if (!completableFuture3.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture3;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Request.async$execute$1(httpRequest, (int)(bl ? 1 : 0), n, buffer, n2, completableFuture4, httpResponse, 2, arg_0));
                    }
                    completableFuture3.join();
                }
            }
            catch (Throwable throwable) {
                ++n2;
            }
            if (!bl) return CompletableFuture.completedFuture(null);
        } while (n2 < 5);
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$executeWithResponse(HttpRequest var0, int var1_1, int var2_2, int var3_3, CompletableFuture var4_4, HttpResponse var5_6, int var6_7, Object var7_8) {
        switch (var6_7) {
            case 0: {
                var3_3 = 0;
                do {
                    try {
                        v0 = Request.send(var0);
                        if (!v0.isDone()) {
                            var5_6 = v0;
                            return var5_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$executeWithResponse(io.vertx.ext.web.client.HttpRequest int int int java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((HttpRequest)var0, (int)var1_1, (int)var2_2, (int)var3_3, (CompletableFuture)var5_6, null, (int)1));
                        }
lbl10:
                        // 3 sources

                        while (true) {
                            var4_4 = (HttpResponse)v0.join();
                            if (var4_4 != null) {
                                return CompletableFuture.completedFuture(var4_4);
                            }
                            if (var1_1 != 0) {
                                v1 = VertxUtil.sleep((long)var2_2);
                                if (!v1.isDone()) {
                                    var5_6 = v1;
                                    return var5_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$executeWithResponse(io.vertx.ext.web.client.HttpRequest int int int java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((HttpRequest)var0, (int)var1_1, (int)var2_2, (int)var3_3, (CompletableFuture)var5_6, (HttpResponse)var4_4, (int)2));
                                }
lbl19:
                                // 3 sources

                                while (true) {
                                    v1.join();
                                    ** GOTO lbl26
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    catch (Exception var4_5) {
                        // empty catch block
                    }
lbl26:
                    // 3 sources

                    if (var1_1 == 0) return CompletableFuture.completedFuture(null);
                } while (++var3_3 < 3);
                return CompletableFuture.completedFuture(null);
            }
            case 1: {
                v0 = var4_4;
                ** continue;
            }
            case 2: {
                v1 = var4_4;
                var4_4 = var5_6;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public static String getURI(HttpRequest httpRequest) {
        try {
            if (httpRequest.headers().contains("referer")) {
                return httpRequest.headers().get("referer");
            }
            HttpRequestImpl httpRequestImpl = (HttpRequestImpl)httpRequest;
            return String.format("https://%s%s", httpRequestImpl.host(), httpRequestImpl.uri());
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

    public static CompletableFuture send(HttpRequest httpRequest, MultipartForm multipartForm) {
        return httpRequest.sendMultipartForm(multipartForm).toCompletionStage().toCompletableFuture();
    }

    public static CompletableFuture execute(HttpRequest httpRequest, boolean bl, int n) {
        return Request.execute(httpRequest, bl, n, null);
    }

    public static CompletableFuture execute(HttpRequest httpRequest, int n) {
        int n2 = 1;
        while (n2 <= n) {
            block5: {
                try {
                    CompletableFuture completableFuture = Request.send(httpRequest);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture2 = completableFuture;
                        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Request.async$execute(httpRequest, n, n2, completableFuture2, 1, arg_0));
                    }
                    HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                    if (httpResponse != null) {
                        return CompletableFuture.completedFuture(httpResponse.body());
                    }
                }
                catch (Exception exception) {
                    if (n2 != n || !exception.getMessage().contains("HttpProxyConnectException")) break block5;
                    return CompletableFuture.failedFuture(new Exception("Invalid Proxy!"));
                }
            }
            ++n2;
        }
        return CompletableFuture.failedFuture(new Exception("Failed to execute request"));
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$execute$1(HttpRequest var0, int var1_1, int var2_2, Buffer var3_3, int var4_4, CompletableFuture var5_5, HttpResponse var6_7, int var7_8, Object var8_9) {
        switch (var7_8) {
            case 0: {
                var4_4 = 0;
                do {
                    try {
                        v0 = var3_3 == null ? Request.send(var0) : Request.send(var0, var3_3);
                        if (!v0.isDone()) {
                            var6_7 = v0;
                            return var6_7.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$execute$1(io.vertx.ext.web.client.HttpRequest int int io.vertx.core.buffer.Buffer int java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((HttpRequest)var0, (int)var1_1, (int)var2_2, (Buffer)var3_3, (int)var4_4, (CompletableFuture)var6_7, null, (int)1));
                        }
lbl10:
                        // 3 sources

                        while (true) {
                            var5_5 = (HttpResponse)v0.join();
                            if (var5_5 != null) {
                                return CompletableFuture.completedFuture(var5_5.body());
                            }
                            if (var1_1 != 0) {
                                v1 = VertxUtil.sleep((long)var2_2);
                                if (!v1.isDone()) {
                                    var6_7 = v1;
                                    return var6_7.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$execute$1(io.vertx.ext.web.client.HttpRequest int int io.vertx.core.buffer.Buffer int java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((HttpRequest)var0, (int)var1_1, (int)var2_2, (Buffer)var3_3, (int)var4_4, (CompletableFuture)var6_7, (HttpResponse)var5_5, (int)2));
                                }
lbl19:
                                // 3 sources

                                while (true) {
                                    v1.join();
                                    ** GOTO lbl26
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    catch (Throwable var5_6) {
                        ++var4_4;
                    }
lbl26:
                    // 3 sources

                    if (var1_1 == 0) return CompletableFuture.completedFuture(null);
                } while (var4_4 < 5);
                return CompletableFuture.completedFuture(null);
            }
            case 1: {
                v0 = var5_5;
                ** continue;
            }
            case 2: {
                v1 = var5_5;
                var5_5 = var6_7;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public static RequestOptions convertToVertx(InterceptUrlRequestCallback.Params params) {
        MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
        for (HttpHeader httpHeader : params.httpHeaders()) {
            multiMap.add(httpHeader.name(), httpHeader.value());
        }
        return new RequestOptions().setMethod(params.urlRequest().method().equalsIgnoreCase("get") ? HttpMethod.GET : HttpMethod.POST).setAbsoluteURI(params.urlRequest().url()).setHeaders(multiMap);
    }

    public static CompletableFuture send(HttpRequest httpRequest, Buffer buffer) {
        return httpRequest.sendBuffer(buffer).toCompletionStage().toCompletableFuture();
    }

    public static CompletableFuture send(HttpRequest httpRequest, JsonArray jsonArray) {
        return httpRequest.sendJson((Object)jsonArray).toCompletionStage().toCompletableFuture();
    }

    public static CompletableFuture send(HttpRequest httpRequest, MultiMap multiMap) {
        return httpRequest.sendForm(multiMap).toCompletionStage().toCompletableFuture();
    }

    public static CompletableFuture send(HttpRequest httpRequest, Object object) {
        if (object instanceof Buffer) {
            return Request.send(httpRequest, (Buffer)object);
        }
        if (object instanceof JsonObject) {
            return Request.send(httpRequest, (JsonObject)object);
        }
        if (object instanceof JsonArray) {
            return Request.send(httpRequest, (JsonArray)object);
        }
        if (object instanceof MultiMap) {
            return Request.send(httpRequest, (MultiMap)object);
        }
        if (!(object instanceof MultipartForm)) return CompletableFuture.failedFuture(new Exception("Bad body type."));
        return Request.send(httpRequest, (MultipartForm)object);
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$execute(HttpRequest var0, int var1_1, int var2_2, CompletableFuture var3_3, int var4_5, Object var5_7) {
        switch (var4_5) {
            case 0: {
                var2_2 = 1;
                while (var2_2 <= var1_1) {
                    try {
                        v0 = Request.send(var0);
                        if (!v0.isDone()) {
                            var4_6 = v0;
                            return var4_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$execute(io.vertx.ext.web.client.HttpRequest int int java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((HttpRequest)var0, (int)var1_1, (int)var2_2, (CompletableFuture)var4_6, (int)1));
                        }
lbl10:
                        // 3 sources

                        while (true) {
                            var3_3 = (HttpResponse)v0.join();
                            if (var3_3 != null) {
                                return CompletableFuture.completedFuture(var3_3.body());
                            }
                            break;
                        }
                    }
                    catch (Exception var3_4) {
                        if (var2_2 != var1_1 || !var3_4.getMessage().contains("HttpProxyConnectException")) ** GOTO lbl18
                        return CompletableFuture.failedFuture(new Exception("Invalid Proxy!"));
                    }
lbl18:
                    // 2 sources

                    ++var2_2;
                }
                return CompletableFuture.failedFuture(new Exception("Failed to execute request"));
            }
            case 1: {
                v0 = var3_3;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public static CompletableFuture send(HttpRequest httpRequest, Json json) {
        return httpRequest.sendJson((Object)json).toCompletionStage().toCompletableFuture();
    }

    public static CompletableFuture execute(HttpRequest httpRequest) {
        return Request.execute(httpRequest, false, 3000);
    }
}
