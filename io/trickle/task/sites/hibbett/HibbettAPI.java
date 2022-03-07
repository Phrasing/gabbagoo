/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.Vertx
 *  io.vertx.core.json.JsonObject
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.codec.BodyCodec
 */
package io.trickle.task.sites.hibbett;

import io.trickle.task.Task;
import io.trickle.task.antibot.impl.px.PXToken;
import io.trickle.task.antibot.impl.px.PXTokenAPI;
import io.trickle.task.antibot.impl.px.PXTokenBase;
import io.trickle.task.sites.hibbett.FakeIOSValueGens;
import io.trickle.util.request.Headers$Pseudo;
import io.trickle.webclient.ClientType;
import io.trickle.webclient.RealClient;
import io.trickle.webclient.RealClientFactory;
import io.trickle.webclient.TaskApiClient;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.codec.BodyCodec;
import java.lang.invoke.LambdaMetafactory;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class HibbettAPI
extends TaskApiClient {
    public PXToken pxToken = null;
    public static int EXCEPTION_RETRY_DELAY;
    public boolean ios;
    public boolean api;
    public boolean isSkip;
    public String dynamicUA;
    public int cartTries = 0;
    public static String PX_TOKEN;
    public PXTokenAPI s;

    public void setPxToken(PXToken pXToken) {
        this.pxToken = pXToken;
    }

    @Override
    public void close() {
        if (this.pxToken != null) {
            // empty if block
        }
        super.close();
    }

    public HttpRequest shopView(String string) {
        HttpRequest httpRequest = this.client.getAbs("https://hibbett-mobileapi.prolific.io/ecommerce/shopview").as(BodyCodec.buffer());
        httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
        httpRequest.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
        httpRequest.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("version", "4.15.0");
        httpRequest.putHeader("platform", "android");
        httpRequest.putHeader("user-agent", this.dynamicUA);
        httpRequest.putHeader("authorization", string);
        httpRequest.putHeader("accept-encoding", "gzip");
        return httpRequest;
    }

    public HttpRequest nonce(String string) {
        HttpRequest httpRequest = this.client.getAbs("https://hibbett-mobileapi.prolific.io/users/radial/nonce").as(BodyCodec.jsonObject());
        if (!this.ios && !this.pxToken.isTokenCaptcha()) {
            httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
            httpRequest.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
            httpRequest.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
            httpRequest.putHeader("content-type", "application/json");
            httpRequest.putHeader("version", "4.15.0");
            httpRequest.putHeader("platform", "android");
            httpRequest.putHeader("user-agent", this.dynamicUA);
            httpRequest.putHeader("authorization", string);
            httpRequest.putHeader("accept-encoding", "gzip");
            return httpRequest;
        }
        if (!this.ios && this.pxToken.isTokenCaptcha()) {
            this.pxToken.setTokenCaptcha(false);
            httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
            httpRequest.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
            httpRequest.putHeader("content-type", "application/json");
            httpRequest.putHeader("version", "4.15.0");
            httpRequest.putHeader("platform", "android");
            httpRequest.putHeader("user-agent", this.dynamicUA);
            httpRequest.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
            httpRequest.putHeader("authorization", string);
            httpRequest.putHeader("accept-encoding", "gzip");
            return httpRequest;
        }
        httpRequest.putHeaders(Headers$Pseudo.MSPA.get());
        httpRequest.putHeader("content-type", "application/json; charset=utf-8");
        httpRequest.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("version", "4.15.0");
        httpRequest.putHeader("authorization", string);
        httpRequest.putHeader("accept-encoding", "br;q=1.0, gzip;q=0.9, deflate;q=0.8");
        httpRequest.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
        httpRequest.putHeader("platform", "ios");
        httpRequest.putHeader("accept-language", "en-US;q=1.0, fa-US;q=0.9");
        httpRequest.putHeader("user-agent", this.dynamicUA);
        return httpRequest;
    }

    public HttpRequest createCart(String string) {
        HttpRequest httpRequest = this.client.postAbs("https://hibbett-mobileapi.prolific.io/ecommerce/cart/create").as(BodyCodec.jsonObject());
        if (!this.ios && !this.pxToken.isTokenCaptcha()) {
            httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
            httpRequest.putHeader("x-px-authorization", ++this.cartTries == 1 ? "1" : (this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue()));
            httpRequest.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
            httpRequest.putHeader("content-type", "application/json");
            httpRequest.putHeader("version", "4.15.0");
            httpRequest.putHeader("platform", "android");
            httpRequest.putHeader("user-agent", this.dynamicUA);
            httpRequest.putHeader("authorization", string);
            httpRequest.putHeader("content-length", "DEFAULT_VALUE");
            httpRequest.putHeader("accept-encoding", "gzip");
            return httpRequest;
        }
        if (!this.ios && this.pxToken.isTokenCaptcha()) {
            this.pxToken.setTokenCaptcha(false);
            httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
            httpRequest.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
            httpRequest.putHeader("content-type", "application/json");
            httpRequest.putHeader("version", "4.15.0");
            httpRequest.putHeader("platform", "android");
            httpRequest.putHeader("user-agent", this.dynamicUA);
            httpRequest.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
            httpRequest.putHeader("authorization", string);
            httpRequest.putHeader("content-length", "DEFAULT_VALUE");
            httpRequest.putHeader("accept-encoding", "gzip");
            return httpRequest;
        }
        httpRequest.putHeaders(Headers$Pseudo.MSPA.get());
        httpRequest.putHeader("content-type", "application/json; charset=utf-8");
        httpRequest.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("version", "4.15.0");
        httpRequest.putHeader("authorization", string);
        httpRequest.putHeader("accept-encoding", "br;q=1.0, gzip;q=0.9, deflate;q=0.8");
        httpRequest.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
        httpRequest.putHeader("platform", "ios");
        httpRequest.putHeader("accept-language", "en-US;q=1.0, fa-US;q=0.9");
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("user-agent", this.dynamicUA);
        return httpRequest;
    }

    public HttpRequest submitPayment(String string, String string2, String string3) {
        HttpRequest httpRequest = this.client.postAbs("https://hibbett-mobileapi.prolific.io/ecommerce/cart/" + string3 + "/payment_methods?customerId=" + string2).as(BodyCodec.buffer());
        if (!this.ios && !this.pxToken.isTokenCaptcha()) {
            httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
            httpRequest.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
            httpRequest.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
            httpRequest.putHeader("version", "4.15.0");
            httpRequest.putHeader("platform", "android");
            httpRequest.putHeader("user-agent", this.dynamicUA);
            httpRequest.putHeader("authorization", string);
            httpRequest.putHeader("content-type", "application/json; charset=UTF-8");
            httpRequest.putHeader("content-length", "DEFAULT_VALUE");
            httpRequest.putHeader("accept-encoding", "gzip");
            return httpRequest;
        }
        if (!this.ios && this.pxToken.isTokenCaptcha()) {
            this.pxToken.setTokenCaptcha(false);
            httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
            httpRequest.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
            httpRequest.putHeader("version", "4.15.0");
            httpRequest.putHeader("platform", "android");
            httpRequest.putHeader("user-agent", this.dynamicUA);
            httpRequest.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
            httpRequest.putHeader("authorization", string);
            httpRequest.putHeader("content-type", "application/json; charset=UTF-8");
            httpRequest.putHeader("content-length", "DEFAULT_VALUE");
            httpRequest.putHeader("accept-encoding", "gzip");
            return httpRequest;
        }
        httpRequest.putHeaders(Headers$Pseudo.MSPA.get());
        httpRequest.putHeader("content-type", "application/json; charset=utf-8");
        httpRequest.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("version", "4.15.0");
        httpRequest.putHeader("authorization", string);
        httpRequest.putHeader("accept-encoding", "br;q=1.0, gzip;q=0.9, deflate;q=0.8");
        httpRequest.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
        httpRequest.putHeader("platform", "ios");
        httpRequest.putHeader("accept-language", "en-US;q=1.0, fa-US;q=0.9");
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("user-agent", this.dynamicUA);
        return httpRequest;
    }

    public CompletableFuture handleBadResponse(int n, String string, String string2) {
        if (n == 403 && this.isSkip) {
            CompletableFuture completableFuture = this.pxToken.reInit();
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> HibbettAPI.async$handleBadResponse(this, n, string, string2, completableFuture2, 1, arg_0));
            }
            completableFuture.join();
            return CompletableFuture.completedFuture(true);
        }
        switch (n) {
            case 403: {
                if (this.api) {
                    CompletableFuture completableFuture = this.s.solveCaptcha(string, string2, this.pxToken.getSid());
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture3 = completableFuture;
                        return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> HibbettAPI.async$handleBadResponse(this, n, string, string2, completableFuture3, 2, arg_0));
                    }
                    if ((Boolean)completableFuture.join() == false) return CompletableFuture.completedFuture(false);
                    this.pxToken.setValue((String)this.s.getValue());
                    this.pxToken.setTokenCaptcha(true);
                    return CompletableFuture.completedFuture(true);
                }
                try {
                    CompletableFuture completableFuture = this.pxToken.solveCaptchaDesktop(string, string2, "https://www.hibbett.com/");
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> HibbettAPI.async$handleBadResponse(this, n, string, string2, completableFuture4, 3, arg_0));
                    }
                    completableFuture.join();
                    return CompletableFuture.completedFuture(true);
                }
                catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                return CompletableFuture.completedFuture(true);
            }
        }
        return CompletableFuture.completedFuture(false);
    }

    public HttpRequest submitShipping(String string, String string2, String string3) {
        HttpRequest httpRequest = this.client.putAbs("https://hibbett-mobileapi.prolific.io/ecommerce/cart/" + string3 + "/shipments/me/shipping_address?useAsBilling=true&customerId=" + string2).as(BodyCodec.jsonObject());
        if (!this.ios && !this.pxToken.isTokenCaptcha()) {
            httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
            httpRequest.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
            httpRequest.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
            httpRequest.putHeader("version", "4.15.0");
            httpRequest.putHeader("platform", "android");
            httpRequest.putHeader("user-agent", this.dynamicUA);
            httpRequest.putHeader("authorization", string);
            httpRequest.putHeader("content-type", "application/json; charset=UTF-8");
            httpRequest.putHeader("content-length", "DEFAULT_VALUE");
            httpRequest.putHeader("accept-encoding", "gzip");
            return httpRequest;
        }
        if (!this.ios && this.pxToken.isTokenCaptcha()) {
            this.pxToken.setTokenCaptcha(false);
            httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
            httpRequest.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
            httpRequest.putHeader("version", "4.15.0");
            httpRequest.putHeader("platform", "android");
            httpRequest.putHeader("user-agent", this.dynamicUA);
            httpRequest.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
            httpRequest.putHeader("authorization", string);
            httpRequest.putHeader("content-type", "application/json; charset=UTF-8");
            httpRequest.putHeader("content-length", "DEFAULT_VALUE");
            httpRequest.putHeader("accept-encoding", "gzip");
            return httpRequest;
        }
        httpRequest.putHeaders(Headers$Pseudo.MSPA.get());
        httpRequest.putHeader("content-type", "application/json; charset=utf-8");
        httpRequest.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("version", "4.15.0");
        httpRequest.putHeader("authorization", string);
        httpRequest.putHeader("accept-encoding", "br;q=1.0, gzip;q=0.9, deflate;q=0.8");
        httpRequest.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
        httpRequest.putHeader("platform", "ios");
        httpRequest.putHeader("accept-language", "en-US;q=1.0, fa-US;q=0.9");
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        return httpRequest;
    }

    public PXTokenBase getPXToken() {
        return this.pxToken;
    }

    public HttpRequest submitCvv(String string) {
        HttpRequest httpRequest = this.client.postAbs("https://hostedpayments.radial.com/hosted-payments/encrypt/pancsc?access_token=" + string).as(BodyCodec.jsonObject());
        httpRequest.putHeader("Content-Type", "application/json; charset=UTF-8");
        httpRequest.putHeader("Content-Length", "DEFAULT_VALUE");
        httpRequest.putHeader("Host", "hostedpayments.radial.com");
        httpRequest.putHeader("Connection", "Keep-Alive");
        httpRequest.putHeader("Accept-Encoding", "gzip");
        httpRequest.putHeader("User-Agent", "okhttp/4.9.0");
        return httpRequest;
    }

    public HttpRequest session() {
        HttpRequest httpRequest = this.client.postAbs("https://hibbett-mobileapi.prolific.io/users/guest").as(BodyCodec.jsonObject());
        httpRequest.putHeader("x-px-authorization", "1");
        if (!this.ios) {
            httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
            httpRequest.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
            httpRequest.putHeader("content-type", "application/json");
            httpRequest.putHeader("version", "4.15.0");
            httpRequest.putHeader("platform", "android");
            httpRequest.putHeader("user-agent", this.dynamicUA);
            httpRequest.putHeader("content-length", "DEFAULT_VALUE");
            httpRequest.putHeader("accept-encoding", "gzip");
            return httpRequest;
        }
        httpRequest.putHeaders(Headers$Pseudo.MSPA.get());
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("version", "4.15.0");
        httpRequest.putHeader("if-none-match", FakeIOSValueGens.genTag());
        httpRequest.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
        httpRequest.putHeader("platform", "ios");
        httpRequest.putHeader("accept-language", "en-US;q=1.0, fa-US;q=0.9");
        httpRequest.putHeader("accept-encoding", "br;q=1.0, gzip;q=0.9, deflate;q=0.8");
        httpRequest.putHeader("user-agent", this.dynamicUA);
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        return httpRequest;
    }

    public HttpRequest atc(String string, String string2, String string3, String string4) {
        HttpRequest httpRequest = this.client.postAbs("https://hibbett-mobileapi.prolific.io/ecommerce/cart/" + string3 + "/items?skuIds=" + string4 + "&customerId=" + string2).as(BodyCodec.buffer());
        if (!this.ios && !this.pxToken.isTokenCaptcha()) {
            httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
            httpRequest.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
            httpRequest.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
            httpRequest.putHeader("version", "4.15.0");
            httpRequest.putHeader("platform", "android");
            httpRequest.putHeader("user-agent", this.dynamicUA);
            httpRequest.putHeader("authorization", string);
            httpRequest.putHeader("content-type", "application/json; charset=UTF-8");
            httpRequest.putHeader("content-length", "DEFAULT_VALUE");
            httpRequest.putHeader("accept-encoding", "gzip");
            httpRequest.putHeader("if-none-match", FakeIOSValueGens.genTag());
            return httpRequest;
        }
        if (!this.ios && this.pxToken.isTokenCaptcha()) {
            httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
            httpRequest.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
            httpRequest.putHeader("version", "4.15.0");
            httpRequest.putHeader("platform", "android");
            httpRequest.putHeader("user-agent", this.dynamicUA);
            httpRequest.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
            httpRequest.putHeader("authorization", string);
            httpRequest.putHeader("content-type", "application/json; charset=UTF-8");
            httpRequest.putHeader("content-length", "DEFAULT_VALUE");
            httpRequest.putHeader("accept-encoding", "gzip");
            return httpRequest;
        }
        httpRequest.putHeaders(Headers$Pseudo.MSPA.get());
        httpRequest.putHeader("content-type", "application/json; charset=utf-8");
        httpRequest.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("version", "4.15.0");
        httpRequest.putHeader("authorization", string);
        httpRequest.putHeader("accept-encoding", "br;q=1.0, gzip;q=0.9, deflate;q=0.8");
        httpRequest.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
        httpRequest.putHeader("platform", "ios");
        httpRequest.putHeader("accept-language", "en-US;q=1.0, fa-US;q=0.9");
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("user-agent", this.dynamicUA);
        return httpRequest;
    }

    public void setDevice(JsonObject jsonObject) {
        if (this.ios) {
            this.dynamicUA = FakeIOSValueGens.genUA();
            return;
        }
        this.dynamicUA = "Hibbett Sports/4.15.0 ";
        this.dynamicUA = this.dynamicUA + "(" + jsonObject.getString("model") + "; android ";
        int n = ThreadLocalRandom.current().nextInt(10);
        this.dynamicUA = n <= 4 ? this.dynamicUA + "10" : this.dynamicUA + "11";
        this.dynamicUA = this.dynamicUA + ")";
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$handleBadResponse(HibbettAPI var0, int var1_1, String var2_2, String var3_3, CompletableFuture var4_4, int var5_6, Object var6_10) {
        switch (var5_6) {
            case 0: {
                if (var1_1 != 403 || !var0.isSkip) ** GOTO lbl9
                v0 = var0.pxToken.reInit();
                if (!v0.isDone()) {
                    var5_7 = v0;
                    return var5_7.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$handleBadResponse(io.trickle.task.sites.hibbett.HibbettAPI int java.lang.String java.lang.String java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((HibbettAPI)var0, (int)var1_1, (String)var2_2, (String)var3_3, (CompletableFuture)var5_7, (int)1));
                }
                ** GOTO lbl30
lbl9:
                // 1 sources

                switch (var1_1) {
                    case 403: {
                        if (!var0.api) ** GOTO lbl17
                        v1 = var0.s.solveCaptcha(var2_2, var3_3, var0.pxToken.getSid());
                        if (!v1.isDone()) {
                            var5_8 = v1;
                            return var5_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$handleBadResponse(io.trickle.task.sites.hibbett.HibbettAPI int java.lang.String java.lang.String java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((HibbettAPI)var0, (int)var1_1, (String)var2_2, (String)var3_3, (CompletableFuture)var5_8, (int)2));
                        }
                        ** GOTO lbl35
lbl17:
                        // 1 sources

                        try {
                            v2 = var0.pxToken.solveCaptchaDesktop(var2_2, var3_3, "https://www.hibbett.com/");
                            if (!v2.isDone()) {
                                var5_9 = v2;
                                return var5_9.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$handleBadResponse(io.trickle.task.sites.hibbett.HibbettAPI int java.lang.String java.lang.String java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((HibbettAPI)var0, (int)var1_1, (String)var2_2, (String)var3_3, (CompletableFuture)var5_9, (int)3));
                            }
                            ** GOTO lbl41
                        }
                        catch (Throwable var4_5) {
                            var4_5.printStackTrace();
                        }
                        return CompletableFuture.completedFuture(true);
                    }
                    default: {
                        return CompletableFuture.completedFuture(false);
                    }
                }
            }
            case 1: {
                v0 = var4_4;
lbl30:
                // 2 sources

                v0.join();
                return CompletableFuture.completedFuture(true);
            }
            case 2: {
                v1 = var4_4;
lbl35:
                // 2 sources

                if ((Boolean)v1.join() == false) return CompletableFuture.completedFuture(false);
                var0.pxToken.setValue((String)var0.s.getValue());
                var0.pxToken.setTokenCaptcha(true);
                return CompletableFuture.completedFuture(true);
            }
            case 3: {
                v2 = var4_4;
lbl41:
                // 2 sources

                v2.join();
                return CompletableFuture.completedFuture(true);
            }
        }
        throw new IllegalArgumentException();
    }

    static {
        PX_TOKEN = "3";
        EXCEPTION_RETRY_DELAY = 5000;
    }

    public HttpRequest processPayment(String string, String string2, String string3) {
        String string4 = !this.ios ? "https://hibbett-mobileapi.prolific.io/ecommerce/cart/" + string3 + "/place_order?customerId=" + string2 : "https://hibbett-mobileapi.prolific.io/ecommerce/cart/" + string3 + "/place_order?optIn=false&customerId=&phone=&firstName=";
        HttpRequest httpRequest = this.client.postAbs(string4).as(BodyCodec.buffer());
        if (!this.ios && !this.pxToken.isTokenCaptcha()) {
            httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
            httpRequest.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
            httpRequest.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
            httpRequest.putHeader("content-type", "application/json");
            httpRequest.putHeader("version", "4.15.0");
            httpRequest.putHeader("platform", "android");
            httpRequest.putHeader("user-agent", this.dynamicUA);
            httpRequest.putHeader("authorization", string);
            httpRequest.putHeader("content-length", "DEFAULT_VALUE");
            httpRequest.putHeader("accept-encoding", "gzip");
            return httpRequest;
        }
        if (!this.ios && this.pxToken.isTokenCaptcha()) {
            this.pxToken.setTokenCaptcha(false);
            httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
            httpRequest.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
            httpRequest.putHeader("content-type", "application/json");
            httpRequest.putHeader("version", "4.15.0");
            httpRequest.putHeader("platform", "android");
            httpRequest.putHeader("user-agent", this.dynamicUA);
            httpRequest.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
            httpRequest.putHeader("authorization", string);
            httpRequest.putHeader("content-length", "DEFAULT_VALUE");
            httpRequest.putHeader("accept-encoding", "gzip");
            return httpRequest;
        }
        httpRequest.putHeaders(Headers$Pseudo.MSPA.get());
        httpRequest.putHeader("content-type", "application/json; charset=utf-8");
        httpRequest.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("version", "4.15.0");
        httpRequest.putHeader("authorization", string);
        httpRequest.putHeader("accept-encoding", "br;q=1.0, gzip;q=0.9, deflate;q=0.8");
        httpRequest.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
        httpRequest.putHeader("platform", "ios");
        httpRequest.putHeader("accept-language", "en-US;q=1.0, fa-US;q=0.9");
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("user-agent", this.dynamicUA);
        return httpRequest;
    }

    public void setS(PXTokenAPI pXTokenAPI) {
        this.s = pXTokenAPI;
    }

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

    public HttpRequest submitEmail(String string, String string2) {
        HttpRequest httpRequest = this.client.putAbs("https://hibbett-mobileapi.prolific.io/ecommerce/cart/" + string2 + "/customer").as(BodyCodec.jsonObject());
        if (!this.ios && !this.pxToken.isTokenCaptcha()) {
            httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
            httpRequest.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
            httpRequest.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
            httpRequest.putHeader("version", "4.15.0");
            httpRequest.putHeader("platform", "android");
            httpRequest.putHeader("user-agent", this.dynamicUA);
            httpRequest.putHeader("authorization", string);
            httpRequest.putHeader("content-type", "application/json; charset=UTF-8");
            httpRequest.putHeader("content-length", "DEFAULT_VALUE");
            httpRequest.putHeader("accept-encoding", "gzip");
            return httpRequest;
        }
        if (!this.ios && this.pxToken.isTokenCaptcha()) {
            this.pxToken.setTokenCaptcha(false);
            httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
            httpRequest.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
            httpRequest.putHeader("version", "4.15.0");
            httpRequest.putHeader("platform", "android");
            httpRequest.putHeader("user-agent", this.dynamicUA);
            httpRequest.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
            httpRequest.putHeader("authorization", string);
            httpRequest.putHeader("content-type", "application/json; charset=UTF-8");
            httpRequest.putHeader("content-length", "DEFAULT_VALUE");
            httpRequest.putHeader("accept-encoding", "gzip");
            return httpRequest;
        }
        httpRequest.putHeaders(Headers$Pseudo.MSPA.get());
        httpRequest.putHeader("content-type", "application/json; charset=utf-8");
        httpRequest.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("version", "4.15.0");
        httpRequest.putHeader("authorization", string);
        httpRequest.putHeader("accept-encoding", "br;q=1.0, gzip;q=0.9, deflate;q=0.8");
        httpRequest.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
        httpRequest.putHeader("platform", "ios");
        httpRequest.putHeader("accept-language", "en-US;q=1.0, fa-US;q=0.9");
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("user-agent", this.dynamicUA);
        return httpRequest;
    }

    public HibbettAPI(Task task) {
        super(ClientType.HIBBETT_ANDROID);
        this.ios = task.getMode().contains("test");
        this.isSkip = task.getMode().contains("skip");
        this.api = task.getMode().contains("api");
    }

    public HttpRequest checkStock(String string, String string2, String string3) {
        HttpRequest httpRequest = this.client.getAbs("https://hibbett-mobileapi.prolific.io/ecommerce/products/" + string3 + "?customerId=" + string2).as(BodyCodec.buffer());
        if (!this.ios && !this.pxToken.isTokenCaptcha()) {
            httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
            httpRequest.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
            httpRequest.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
            httpRequest.putHeader("content-type", "application/json");
            httpRequest.putHeader("version", "4.15.0");
            httpRequest.putHeader("platform", "android");
            httpRequest.putHeader("user-agent", this.dynamicUA);
            httpRequest.putHeader("authorization", string);
            httpRequest.putHeader("accept-encoding", "gzip");
            return httpRequest;
        }
        if (!this.ios && this.pxToken.isTokenCaptcha()) {
            this.pxToken.setTokenCaptcha(false);
            httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
            httpRequest.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
            httpRequest.putHeader("content-type", "application/json");
            httpRequest.putHeader("version", "4.15.0");
            httpRequest.putHeader("platform", "android");
            httpRequest.putHeader("user-agent", this.dynamicUA);
            httpRequest.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
            httpRequest.putHeader("authorization", string);
            httpRequest.putHeader("accept-encoding", "gzip");
            return httpRequest;
        }
        httpRequest.putHeaders(Headers$Pseudo.MSPA.get());
        httpRequest.putHeader("content-type", "application/json; charset=utf-8");
        httpRequest.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("version", "4.15.0");
        httpRequest.putHeader("authorization", string);
        httpRequest.putHeader("accept-encoding", "br;q=1.0, gzip;q=0.9, deflate;q=0.8");
        httpRequest.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
        httpRequest.putHeader("platform", "ios");
        httpRequest.putHeader("accept-language", "en-US;q=1.0, fa-US;q=0.9");
        httpRequest.putHeader("user-agent", this.dynamicUA);
        return httpRequest;
    }

    public HttpRequest submitShippingRate(String string, String string2) {
        HttpRequest httpRequest = this.client.putAbs("https://hibbett-mobileapi.prolific.io/ecommerce/cart/" + string2 + "/shipments/me/shipping_options").as(BodyCodec.jsonObject());
        if (!this.ios && !this.pxToken.isTokenCaptcha()) {
            httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
            httpRequest.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
            httpRequest.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
            httpRequest.putHeader("version", "4.15.0");
            httpRequest.putHeader("platform", "android");
            httpRequest.putHeader("user-agent", this.dynamicUA);
            httpRequest.putHeader("authorization", string);
            httpRequest.putHeader("content-type", "application/json; charset=UTF-8");
            httpRequest.putHeader("content-length", "DEFAULT_VALUE");
            httpRequest.putHeader("accept-encoding", "gzip");
            httpRequest.putHeader("if-none-match", FakeIOSValueGens.genTag());
            return httpRequest;
        }
        if (!this.ios && this.pxToken.isTokenCaptcha()) {
            this.pxToken.setTokenCaptcha(false);
            httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
            httpRequest.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
            httpRequest.putHeader("version", "4.15.0");
            httpRequest.putHeader("platform", "android");
            httpRequest.putHeader("user-agent", this.dynamicUA);
            httpRequest.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
            httpRequest.putHeader("authorization", string);
            httpRequest.putHeader("content-type", "application/json; charset=UTF-8");
            httpRequest.putHeader("content-length", "DEFAULT_VALUE");
            httpRequest.putHeader("accept-encoding", "gzip");
            httpRequest.putHeader("if-none-match", FakeIOSValueGens.genTag());
            return httpRequest;
        }
        httpRequest.putHeaders(Headers$Pseudo.MSPA.get());
        httpRequest.putHeader("content-type", "application/json; charset=utf-8");
        httpRequest.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("version", "4.15.0");
        httpRequest.putHeader("authorization", string);
        httpRequest.putHeader("accept-encoding", "br;q=1.0, gzip;q=0.9, deflate;q=0.8");
        httpRequest.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
        httpRequest.putHeader("platform", "ios");
        httpRequest.putHeader("accept-language", "en-US;q=1.0, fa-US;q=0.9");
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("user-agent", this.dynamicUA);
        httpRequest.putHeader("if-none-match", FakeIOSValueGens.genTag());
        return httpRequest;
    }

    public boolean isSkip() {
        return this.isSkip;
    }

    public HttpRequest submitCard(String string) {
        HttpRequest httpRequest = this.client.postAbs("https://hostedpayments.radial.com/hosted-payments/pan/tokenize?access_token=" + string).as(BodyCodec.jsonObject());
        httpRequest.putHeader("Content-Type", "application/json; charset=UTF-8");
        httpRequest.putHeader("Content-Length", "DEFAULT_VALUE");
        httpRequest.putHeader("Host", "hostedpayments.radial.com");
        httpRequest.putHeader("Connection", "Keep-Alive");
        httpRequest.putHeader("Accept-Encoding", "gzip");
        httpRequest.putHeader("User-Agent", "okhttp/4.9.0");
        return httpRequest;
    }

    public void setDynamicUA(String string) {
        this.dynamicUA = string;
    }
}

