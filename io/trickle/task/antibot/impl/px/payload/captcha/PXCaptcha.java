/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.AsyncResult
 *  io.vertx.core.Future
 *  io.vertx.core.MultiMap
 *  io.vertx.core.Promise
 *  io.vertx.core.Vertx
 *  io.vertx.core.buffer.Buffer
 *  io.vertx.core.http.HttpHeaders
 *  io.vertx.core.http.HttpServer
 *  io.vertx.core.http.HttpServerRequest
 *  io.vertx.core.json.JsonObject
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.client.HttpResponse
 *  io.vertx.ext.web.codec.BodyCodec
 *  org.apache.logging.log4j.Logger
 *  org.conscrypt.Conscrypt
 */
package io.trickle.task.antibot.impl.px.payload.captcha;

import io.trickle.task.antibot.impl.px.Types;
import io.trickle.task.antibot.impl.px.payload.ExtendedPayload;
import io.trickle.task.antibot.impl.px.payload.captcha.Devices;
import io.trickle.task.antibot.impl.px.payload.captcha.Devices$DeviceImpl;
import io.trickle.task.antibot.impl.px.payload.captcha.PXCaptcha$1;
import io.trickle.task.antibot.impl.px.payload.captcha.util.DeviceHeaderParsers;
import io.trickle.task.sites.Site;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Headers$Pseudo;
import io.trickle.util.request.Request;
import io.trickle.webclient.ClientType;
import io.trickle.webclient.RealClient;
import io.trickle.webclient.RealClientFactory;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.codec.BodyCodec;
import java.lang.invoke.LambdaMetafactory;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.Logger;
import org.conscrypt.Conscrypt;

public class PXCaptcha {
    public static Pattern VID_UUID_PATTERN;
    public boolean mobile;
    public Devices$DeviceImpl device;
    public static Pattern CI_PATTERN;
    public static Pattern PXDE_PATTERN;
    public String parentVID;
    public long vidAge;
    public static Pattern DRC_PATTERN;
    public static Pattern CTS_PATTERN;
    public static Pattern STS_PATTERN;
    public String parentUUID;
    public static Pattern CLS_PATTERN;
    public static Pattern BAKE_PATTERN;
    public Logger logger;
    public static Pattern WCS_PATTERN;
    public RealClient client;
    public String origin;
    public static Pattern DOLLAR_SCRIPT_VAL_PATTERN;
    public static Pattern CP_PATTERN;
    public Types type;
    public Site SITE;
    public static Pattern CI_UUID_PATTERN;
    public String referer;
    public boolean isFirstTry = true;
    public int failedSolves = 0;
    public static Pattern SID_UUID_PATTERN;
    public static Pattern CS_PATTERN;
    public String sid = null;
    public static Pattern CI_TOKEN_PATTERN;
    public static Pattern SFF_SCS_PATTERN;
    public boolean needsDesktopAppID;

    public HttpRequest hibbettCSS() {
        HttpRequest httpRequest = this.client.getAbs("https://www.hibbett.com/on/demandware.static/Sites-Hibbett-US-Site/-/default/css/pxchallenge.csss").as(BodyCodec.buffer());
        httpRequest.putHeader("user-agent", this.device.getUserAgent());
        httpRequest.putHeader("accept", "text/css,*/*;q=0.1");
        httpRequest.putHeader("x-requested-with", this.getPackageName());
        httpRequest.putHeader("sec-fetch-site", "cross-site");
        httpRequest.putHeader("sec-fetch-mode", "no-cors");
        httpRequest.putHeader("sec-fetch-dest", "style");
        httpRequest.putHeader("referer", this.origin + "/");
        httpRequest.putHeader("accept-encoding", this.device.getAcceptEncoding() == null ? "gzip, deflate" : DeviceHeaderParsers.getAcceptLanguage(this.device.getArr("PX313")));
        httpRequest.putHeader("accept-language", this.device.getAcceptLanguage());
        return httpRequest;
    }

    public CompletableFuture sendPayload(ExtendedPayload extendedPayload) {
        HttpRequest httpRequest = !extendedPayload.getType().equals((Object)Types.DESKTOP) ? this.bundleReq() : this.collectorReq();
        MultiMap multiMap = extendedPayload.asForm();
        StringBuilder stringBuilder = new StringBuilder();
        multiMap.forEach(arg_0 -> PXCaptcha.lambda$sendPayload$5(stringBuilder, arg_0));
        Buffer buffer = Buffer.buffer((String)stringBuilder.toString().trim());
        while (this.client.isActive()) {
            try {
                CompletableFuture completableFuture = Request.send(httpRequest, buffer);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> PXCaptcha.async$sendPayload(this, extendedPayload, httpRequest, multiMap, stringBuilder, buffer, completableFuture2, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse != null) {
                    return CompletableFuture.completedFuture((JsonObject)httpResponse.body());
                }
                CompletableFuture completableFuture3 = VertxUtil.randomSleep(10000L);
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> PXCaptcha.async$sendPayload(this, extendedPayload, httpRequest, multiMap, stringBuilder, buffer, completableFuture4, httpResponse, 2, arg_0));
                }
                completableFuture3.join();
            }
            catch (Throwable throwable) {
                if (!this.client.isActive()) {
                    return CompletableFuture.failedFuture(new Exception("Failed to send payload. Active? " + this.client.isActive()));
                }
                this.logger.warn("Error to send payload: {}", (Object)throwable.getMessage());
            }
        }
        return CompletableFuture.failedFuture(new Exception("Failed to send payload. Active? " + this.client.isActive()));
    }

    public static CompletableFuture async$solveCaptcha(PXCaptcha pXCaptcha, String string, CompletableFuture completableFuture, int n, Object object) {
        switch (n) {
            case 0: {
                Objects.requireNonNull(pXCaptcha);
                return CompletableFuture.completedFuture(null);
            }
            case 1: {
                CompletableFuture completableFuture2 = completableFuture;
                completableFuture2.join();
                return CompletableFuture.completedFuture(null);
            }
        }
        throw new IllegalArgumentException();
    }

    public HttpRequest getMainMinJS() {
        String string = "";
        switch (PXCaptcha$1.$SwitchMap$io$trickle$task$sites$Site[this.SITE.ordinal()]) {
            case 1: {
                string = "https://client.perimeterx.net/PX9Qx3Rve4/main.min.js";
                return this.getScriptRequest(string);
            }
            case 2: {
                string = "https://client.perimeterx.net/PXUArm9B04/main.min.js";
                return this.getScriptRequest(string);
            }
        }
        return this.getScriptRequest(string);
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$setBrowserType(PXCaptcha var0, PXCaptcha var1_1, CompletableFuture var2_2, int var3_3, Object var4_4) {
        switch (var3_3) {
            case 0: {
                if (var0.device != null) return CompletableFuture.completedFuture(null);
                var0.mobile = false;
                v0 = var0;
                if (!var0.mobile) ** GOTO lbl13
                v1 = Devices.randomMobileBrowser();
                if (!v1.isDone()) {
                    var2_2 = v1;
                    var1_1 = v0;
                    return var2_2.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$setBrowserType(io.trickle.task.antibot.impl.px.payload.captcha.PXCaptcha io.trickle.task.antibot.impl.px.payload.captcha.PXCaptcha java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PXCaptcha)var0, (PXCaptcha)var1_1, (CompletableFuture)var2_2, (int)1));
                }
                ** GOTO lbl22
lbl13:
                // 1 sources

                v2 = Devices.randomDesktopBrowser();
                if (!v2.isDone()) {
                    var2_2 = v2;
                    var1_1 = v0;
                    return var2_2.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$setBrowserType(io.trickle.task.antibot.impl.px.payload.captcha.PXCaptcha io.trickle.task.antibot.impl.px.payload.captcha.PXCaptcha java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PXCaptcha)var0, (PXCaptcha)var1_1, (CompletableFuture)var2_2, (int)2));
                }
                ** GOTO lbl29
            }
            case 1: {
                v0 = var1_1;
                v1 = var2_2;
lbl22:
                // 2 sources

                v3 = (Devices$DeviceImpl)v1.join();
lbl23:
                // 2 sources

                while (true) {
                    v0.device = v3;
                    return CompletableFuture.completedFuture(null);
                }
            }
            case 2: {
                v0 = var1_1;
                v2 = var2_2;
lbl29:
                // 2 sources

                v3 = (Devices$DeviceImpl)v2.join();
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public void setWebviewScriptHeaders(HttpRequest httpRequest) {
        httpRequest.putHeader("user-agent", this.device.getUserAgent());
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("x-requested-with", this.getPackageName());
        httpRequest.putHeader("sec-fetch-site", "cross-site");
        httpRequest.putHeader("sec-fetch-mode", "no-cors");
        httpRequest.putHeader("sec-fetch-dest", "script");
        httpRequest.putHeader("referer", this.origin + "/");
        httpRequest.putHeader("accept-encoding", this.device.getAcceptEncoding() == null ? "gzip, deflate" : this.device.getAcceptEncoding());
        httpRequest.putHeader("accept-language", this.device.getAcceptLanguage());
    }

    public void setWebviewHeaders(HttpRequest httpRequest) {
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("user-agent", this.device.getUserAgent());
        httpRequest.putHeader("content-type", "application/x-www-form-urlencoded");
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("origin", this.origin);
        httpRequest.putHeader("x-requested-with", this.getPackageName());
        httpRequest.putHeader("sec-fetch-site", "cross-site");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", this.origin + "/");
        httpRequest.putHeader("accept-encoding", this.device.getAcceptEncoding() == null ? "gzip, deflate" : this.device.getAcceptEncoding());
        httpRequest.putHeader("accept-language", this.device.getAcceptLanguage());
    }

    public static void main(String[] stringArray) {
        String string = ExtendedPayload.decode("aUkQRhAIEGJqABAeEFYQCEkQYmoLBBAIEFpGRkJBCB0dRUVFHEJXQFtfV0ZXQEocUV1fHRAeEGJqBAEQCBB/U1F7XEZXXhAeEGJqAwsDEAgCHhBiagoHAhAIAh4QYmoKBwMQCAMKBgEeEGJqAwICChAIAQQCAh4QYmoDAgcHEAgDBAAHAwUFAQALAgYEHhBiagMCBwQQCAMEAAcDBQUBAAsCBgoeEGJqAwIBChAIEFcLBQIAAFYCH1ZTUAofAwNXUB8KA1NUHwoFCgBUBVYBBlEFBBAeEGJqAQUDEAhUU15BVx4QYmoABwIQCBBiagcEAhAeEGJqBQIKEAgQQkpaURBPT28=", 50);
        System.setProperty("vertx.disableHttpHeadersValidation", "true");
        Conscrypt.setUseEngineSocketByDefault((boolean)false);
        Security.insertProviderAt(Conscrypt.newProvider(), 1);
        Conscrypt.setUseEngineSocketByDefault((boolean)false);
        Vertx vertx = Vertx.vertx();
        HttpServer httpServer = vertx.createHttpServer();
        RealClient realClient = RealClientFactory.buildProxied(vertx, ClientType.CHROME);
        httpServer.requestHandler(arg_0 -> PXCaptcha.lambda$main$2(realClient, arg_0));
        httpServer.listen(8890);
    }

    public void setSafariScriptHeaders(HttpRequest httpRequest) {
        httpRequest.putHeaders(Headers$Pseudo.MSPA.get());
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("accept-encoding", this.device.getAcceptEncoding() == null ? "gzip, deflate, br" : this.device.getAcceptEncoding());
        httpRequest.putHeader("user-agent", this.device.getUserAgent());
        httpRequest.putHeader("accept-language", this.device.getAcceptLanguage());
        httpRequest.putHeader("referer", this.origin + "/");
    }

    public CompletableFuture solveCaptcha(String string) {
        long l;
        Objects.requireNonNull(this);
        if (false < true) return CompletableFuture.completedFuture(null);
        if (System.currentTimeMillis() - this.vidAge > 120000L) return CompletableFuture.completedFuture(null);
        Objects.requireNonNull(this);
        if (0 <= 6) {
            Objects.requireNonNull(this);
            l = 5000L * 0L;
        } else {
            l = 30000L;
        }
        CompletableFuture completableFuture = VertxUtil.hardCodedSleep(l);
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> PXCaptcha.async$solveCaptcha(this, string, completableFuture2, 1, arg_0));
        }
        completableFuture.join();
        return CompletableFuture.completedFuture(null);
    }

    public static void lambda$sendPayload$5(StringBuilder stringBuilder, Map.Entry entry) {
        if (stringBuilder.length() > 0) {
            stringBuilder.append("&");
        }
        stringBuilder.append((String)entry.getKey()).append("=").append((String)entry.getValue());
    }

    public CompletableFuture prepareDevice() {
        return this.setBrowserType();
    }

    public static String getCITOKEN(JsonObject jsonObject) {
        return PXCaptcha.parseDO(jsonObject, CI_TOKEN_PATTERN);
    }

    public static String getCIUUID(JsonObject jsonObject) {
        return PXCaptcha.parseDO(jsonObject, CI_UUID_PATTERN);
    }

    public void setChromeImageHeaders(HttpRequest httpRequest) {
        if (this.device.getSecUA() != null && this.device.getSecUAMobile() != null) {
            httpRequest.putHeader("sec-ch-ua", this.device.getSecUA());
            httpRequest.putHeader("sec-ch-ua-mobile", this.device.getSecUAMobile());
        }
        httpRequest.putHeader("user-agent", this.device.getUserAgent());
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("origin", this.origin);
        httpRequest.putHeader("sec-fetch-site", "cross-site");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", this.origin + "/");
        httpRequest.putHeader("accept-encoding", this.device.getAcceptEncoding() == null ? "gzip, deflate, br" : this.device.getAcceptEncoding());
        httpRequest.putHeader("accept-language", this.device.getAcceptLanguage());
    }

    public HttpRequest getScriptRequest(String string) {
        HttpRequest httpRequest = this.client.getAbs(string).as(BodyCodec.buffer());
        this.setChromeScriptHeaders(httpRequest);
        return httpRequest;
    }

    public void setWebviewImageHeaders(HttpRequest httpRequest) {
        httpRequest.putHeader("user-agent", this.device.getUserAgent());
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("origin", this.origin);
        httpRequest.putHeader("x-requested-with", this.getPackageName());
        httpRequest.putHeader("sec-fetch-site", "cross-site");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", this.origin + "/");
        httpRequest.putHeader("accept-encoding", this.device.getAcceptEncoding() == null ? "gzip, deflate" : this.device.getAcceptEncoding());
        httpRequest.putHeader("accept-language", this.device.getAcceptLanguage());
    }

    public static void parseCookiesFromResp(String string, Map map) {
        try {
            Matcher matcher = PXDE_PATTERN.matcher(string);
            Matcher matcher2 = BAKE_PATTERN.matcher(string);
            if (matcher2.find()) {
                map.put("_px3", matcher2.group(1));
            }
            if (!matcher.find()) return;
            map.put("_pxde", matcher.group(1));
            return;
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public static HttpRequest atc(HttpServerRequest httpServerRequest, RealClient realClient) {
        HttpRequest httpRequest = realClient.postAbs("https://hibbett-mobileapi.prolific.io/ecommerce/cart/e6c613832fb68eac5dbe2ba856/items?skuIds=39553459&customerId=efKbcteBKcyafdIW7n6Sk3XCSi").as(BodyCodec.buffer());
        httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
        httpRequest.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
        httpRequest.putHeader("version", "4.15.0");
        httpRequest.putHeader("platform", "android");
        httpRequest.putHeader("user-agent", httpServerRequest.headers().get("user-agent"));
        httpRequest.putHeader("x-px-authorization", httpServerRequest.headers().get("x-px-authorization"));
        httpRequest.putHeader("authorization", httpServerRequest.headers().get("authorization"));
        httpRequest.putHeader("content-type", "application/json; charset=UTF-8");
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("accept-encoding", "gzip");
        return httpRequest;
    }

    public void setRefererAndOrigin(String string) {
        if (string != null) {
            string = string.replace("https://", "");
            this.origin = "https://" + string.substring(0, string.indexOf("/"));
            this.referer = "https://" + string;
            return;
        }
        if (!this.type.equals((Object)Types.DESKTOP) && !this.needsDesktopAppID) {
            this.origin = "https://www.perimeterx.com";
            this.referer = "https://www.perimeterx.com/";
            return;
        }
        switch (PXCaptcha$1.$SwitchMap$io$trickle$task$sites$Site[this.SITE.ordinal()]) {
            case 1: {
                this.origin = "https://www.hibbett.com";
                this.referer = "https://www.hibbett.com/";
                return;
            }
            case 2: {
                this.origin = "https://www.walmart.com";
                this.referer = "https://www.walmart.com/";
                return;
            }
        }
    }

    public HttpRequest getFavicon() {
        this.client.cookieStore().put("_pxvid", this.parentVID, "www.perimeterx.com");
        HttpRequest httpRequest = this.client.getAbs("https://www.perimeterx.com/favicon.ico").as(BodyCodec.buffer());
        httpRequest.putHeader("user-agent", this.device.getUserAgent());
        httpRequest.putHeader("accept", "image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8");
        httpRequest.putHeader("x-requested-with", this.getPackageName());
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "no-cors");
        httpRequest.putHeader("sec-fetch-dest", "image");
        httpRequest.putHeader("referer", this.origin + "/");
        httpRequest.putHeader("accept-encoding", this.device.getAcceptEncoding() == null ? "gzip, deflate" : this.device.getAcceptEncoding());
        httpRequest.putHeader("accept-language", this.device.getAcceptLanguage());
        return httpRequest;
    }

    public void setFirefoxImageHeaders(HttpRequest httpRequest) {
        httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
        httpRequest.putHeader("user-agent", this.device.getUserAgent());
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("accept-language", this.device.getAcceptLanguage());
        httpRequest.putHeader("accept-encoding", this.device.getAcceptEncoding() == null ? "gzip, deflate, br" : this.device.getAcceptEncoding());
        httpRequest.putHeader("referer", this.origin + "/");
        httpRequest.putHeader("origin", this.origin);
        httpRequest.putHeader("te", "trailers");
    }

    public String getParentUUID() {
        return this.parentUUID;
    }

    public void setFirefoxScriptHeaders(HttpRequest httpRequest) {
        httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
        httpRequest.putHeader("user-agent", this.device.getUserAgent());
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("accept-language", this.device.getAcceptLanguage());
        httpRequest.putHeader("accept-encoding", this.device.getAcceptEncoding() == null ? "gzip, deflate, br" : this.device.getAcceptEncoding());
        httpRequest.putHeader("referer", this.origin + "/");
        httpRequest.putHeader("te", "trailers");
    }

    public static String getCTSValue(JsonObject jsonObject) {
        return PXCaptcha.parseDO(jsonObject, CTS_PATTERN);
    }

    public HttpRequest collectorReq() {
        HttpRequest httpRequest = this.client.postAbs("https://collector-" + "PXu6b0qd2S".toLowerCase() + ".px-cloud.net/api/v2/collector").timeout(TimeUnit.SECONDS.toMillis(15L)).as(BodyCodec.jsonObject());
        this.setChromeHeaders(httpRequest);
        return httpRequest;
    }

    public HttpRequest imageReq(MultiMap multiMap) {
        String string = this.getSiteID();
        Object object = "https://collector-" + string + ".perimeterx.net/b/g?";
        StringBuilder stringBuilder = new StringBuilder();
        multiMap.forEach(arg_0 -> PXCaptcha.lambda$imageReq$4(stringBuilder, arg_0));
        object = ((String)object).concat(stringBuilder.toString().trim());
        HttpRequest httpRequest = this.client.getAbs((String)object).timeout(TimeUnit.SECONDS.toMillis(15L)).as(BodyCodec.buffer());
        this.setChromeHeaders(httpRequest);
        return httpRequest;
    }

    static {
        DOLLAR_SCRIPT_VAL_PATTERN = Pattern.compile("(\\$[0-9][0-9]*)");
        BAKE_PATTERN = Pattern.compile("bake\\|.*?\\|.*?\\|(.*?)\\|");
        PXDE_PATTERN = Pattern.compile("_pxde\\|330\\|(.*?)\\|");
        CI_PATTERN = Pattern.compile("ci\\|.*?\\|.*?\\|([0-9]*?)\\|");
        CI_UUID_PATTERN = Pattern.compile("ci\\|.*?\\|(.*?)\\|");
        CI_TOKEN_PATTERN = Pattern.compile("ci\\|.*?\\|.*?\\|.*?\\|(.*?)\"");
        VID_UUID_PATTERN = Pattern.compile("vid\\|(.*?)\\|");
        SID_UUID_PATTERN = Pattern.compile("sid\\|(.*?)\"");
        CS_PATTERN = Pattern.compile("cs\\|([0-z]*?)\"");
        CTS_PATTERN = Pattern.compile("cts\\|([\\--z]*)");
        STS_PATTERN = Pattern.compile("sts\\|([0-z]*?)\"");
        DRC_PATTERN = Pattern.compile("drc\\|([0-9]*?)\"");
        WCS_PATTERN = Pattern.compile("wcs\\|([0-z]*?)\"");
        CLS_PATTERN = Pattern.compile("cls\\|(.*?)\"");
        SFF_SCS_PATTERN = Pattern.compile("sff\\|scs\\|300\\|1,(.*?)\"");
        CP_PATTERN = Pattern.compile("cp\\|.*?\\|(.*?)\"");
    }

    public static void lambda$main$1(HttpServerRequest httpServerRequest, RealClient realClient, AsyncResult asyncResult) {
        if (!asyncResult.succeeded()) return;
        PXCaptcha.sendPayload(httpServerRequest, (Buffer)asyncResult.result(), realClient).onSuccess(arg_0 -> PXCaptcha.lambda$main$0(httpServerRequest, arg_0));
    }

    public static String getCLS(JsonObject jsonObject) {
        return PXCaptcha.parseDO(jsonObject, CLS_PATTERN);
    }

    public static void lambda$imageReq$4(StringBuilder stringBuilder, Map.Entry entry) {
        if (stringBuilder.length() > 0) {
            stringBuilder.append("&");
        }
        if (((String)entry.getValue()).contains("\udd31")) {
            stringBuilder.append((String)entry.getKey()).append("=").append(URLEncoder.encode((String)entry.getValue(), StandardCharsets.UTF_8));
            return;
        }
        stringBuilder.append((String)entry.getKey()).append("=").append((String)entry.getValue());
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$sendPayload(PXCaptcha var0, ExtendedPayload var1_1, HttpRequest var2_2, MultiMap var3_3, StringBuilder var4_4, Buffer var5_5, CompletableFuture var6_6, HttpResponse var7_8, int var8_9, Object var9_10) {
        switch (var8_9) {
            case 0: {
                var2_2 = var1_1.getType().equals((Object)Types.DESKTOP) == false ? var0.bundleReq() : var0.collectorReq();
                var3_3 = var1_1.asForm();
                var4_4 = new StringBuilder();
                var3_3.forEach((Consumer<Map.Entry>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)V, lambda$sendPayload$5(java.lang.StringBuilder java.util.Map$Entry ), (Ljava/util/Map$Entry;)V)((StringBuilder)var4_4));
                var5_5 = Buffer.buffer((String)var4_4.toString().trim());
                while (var0.client.isActive() != false) {
                    try {
                        v0 = Request.send(var2_2, var5_5);
                        if (!v0.isDone()) {
                            var7_8 = v0;
                            return var7_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$sendPayload(io.trickle.task.antibot.impl.px.payload.captcha.PXCaptcha io.trickle.task.antibot.impl.px.payload.ExtendedPayload io.vertx.ext.web.client.HttpRequest io.vertx.core.MultiMap java.lang.StringBuilder io.vertx.core.buffer.Buffer java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PXCaptcha)var0, (ExtendedPayload)var1_1, (HttpRequest)var2_2, (MultiMap)var3_3, (StringBuilder)var4_4, (Buffer)var5_5, (CompletableFuture)var7_8, null, (int)1));
                        }
lbl14:
                        // 3 sources

                        while (true) {
                            var6_6 = (HttpResponse)v0.join();
                            if (var6_6 != null) {
                                return CompletableFuture.completedFuture((JsonObject)var6_6.body());
                            }
                            v1 = VertxUtil.randomSleep(10000L);
                            if (!v1.isDone()) {
                                var7_8 = v1;
                                return var7_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$sendPayload(io.trickle.task.antibot.impl.px.payload.captcha.PXCaptcha io.trickle.task.antibot.impl.px.payload.ExtendedPayload io.vertx.ext.web.client.HttpRequest io.vertx.core.MultiMap java.lang.StringBuilder io.vertx.core.buffer.Buffer java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((PXCaptcha)var0, (ExtendedPayload)var1_1, (HttpRequest)var2_2, (MultiMap)var3_3, (StringBuilder)var4_4, (Buffer)var5_5, (CompletableFuture)var7_8, (HttpResponse)var6_6, (int)2));
                            }
lbl22:
                            // 3 sources

                            while (true) {
                                v1.join();
                                break;
                            }
                            break;
                        }
                    }
                    catch (Throwable var6_7) {
                        if (!var0.client.isActive()) {
                            return CompletableFuture.failedFuture(new Exception("Failed to send payload. Active? " + var0.client.isActive()));
                        }
                        var0.logger.warn("Error to send payload: {}", (Object)var6_7.getMessage());
                    }
                }
                return CompletableFuture.failedFuture(new Exception("Failed to send payload. Active? " + var0.client.isActive()));
            }
            case 1: {
                v0 = var6_6;
                ** continue;
            }
            case 2: {
                v1 = var6_6;
                var6_6 = var7_8;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public void reset() {
        this.parentVID = null;
        this.parentUUID = null;
    }

    public static String getVID(ExtendedPayload extendedPayload, JsonObject jsonObject) {
        if (!jsonObject.toString().contains("vid|")) return extendedPayload.getVID();
        return PXCaptcha.getVIDUUID(jsonObject);
    }

    public static void lambda$sendPayload$3(Promise promise, AsyncResult asyncResult) {
        if (asyncResult.succeeded()) {
            Buffer buffer = (Buffer)((HttpResponse)asyncResult.result()).body();
            System.out.println("Resp: " + buffer);
            promise.tryComplete((Object)((Buffer)((HttpResponse)asyncResult.result()).body()));
            return;
        }
        promise.tryFail("Failed req");
    }

    public String getPackageName() {
        switch (PXCaptcha$1.$SwitchMap$io$trickle$task$sites$Site[this.SITE.ordinal()]) {
            case 1: {
                return "com.hibbett.android";
            }
            case 2: {
                return "com.walmart.android";
            }
        }
        return "";
    }

    public static String getCP(JsonObject jsonObject) {
        return PXCaptcha.parseDO(jsonObject, CP_PATTERN);
    }

    public void setChromeScriptHeaders(HttpRequest httpRequest) {
        if (this.device.getSecUA() != null && this.device.getSecUAMobile() != null) {
            httpRequest.putHeader("sec-ch-ua", this.device.getSecUA());
            httpRequest.putHeader("sec-ch-ua-mobile", this.device.getSecUAMobile());
        }
        httpRequest.putHeader("user-agent", this.device.getUserAgent());
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("sec-fetch-site", "cross-site");
        httpRequest.putHeader("sec-fetch-mode", "no-cors");
        httpRequest.putHeader("sec-fetch-dest", "script");
        httpRequest.putHeader("referer", this.origin + "/");
        httpRequest.putHeader("accept-encoding", this.device.getAcceptEncoding() == null ? "gzip, deflate, br" : this.device.getAcceptEncoding());
        httpRequest.putHeader("accept-language", this.device.getAcceptLanguage() == null ? "en-US" : this.device.getAcceptLanguage());
    }

    public HttpRequest bundleReq() {
        String string = this.getSiteID();
        HttpRequest httpRequest = this.client.postAbs("https://collector-" + string + ".px-cloud.net/assets/js/bundle").timeout(TimeUnit.SECONDS.toMillis(15L)).as(BodyCodec.jsonObject());
        this.setChromeHeaders(httpRequest);
        return httpRequest;
    }

    public String getSiteID() {
        switch (PXCaptcha$1.$SwitchMap$io$trickle$task$sites$Site[this.SITE.ordinal()]) {
            case 1: {
                switch (this.type) {
                    case CAPTCHA_MOBILE: {
                        return "PX9Qx3Rve4".toLowerCase();
                    }
                    case CAPTCHA_DESKTOP: {
                        return "PXAJDckzHD".toLowerCase();
                    }
                }
            }
            case 2: {
                switch (this.type) {
                    case CAPTCHA_MOBILE: {
                        return "PXUArm9B04".toLowerCase();
                    }
                    case CAPTCHA_DESKTOP: {
                        return "PXu6b0qd2S".toLowerCase();
                    }
                }
                return "";
            }
        }
        return "";
    }

    public static String getDRC(JsonObject jsonObject) {
        return PXCaptcha.parseDO(jsonObject, DRC_PATTERN);
    }

    public void close() {
    }

    public static String getSID(ExtendedPayload extendedPayload, JsonObject jsonObject) {
        if (!jsonObject.toString().contains("sid|")) return extendedPayload.getSID();
        return PXCaptcha.getSIDUUID(jsonObject);
    }

    public void setSafariHeaders(HttpRequest httpRequest) {
        httpRequest.putHeaders(Headers$Pseudo.MSPA.get());
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("content-type", "application/x-www-form-urlencoded");
        httpRequest.putHeader("origin", this.origin);
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("accept-language", this.device.getAcceptLanguage());
        httpRequest.putHeader("user-agent", this.device.getUserAgent());
        httpRequest.putHeader("referer", this.origin + "/");
        httpRequest.putHeader("accept-encoding", this.device.getAcceptEncoding() == null ? "gzip, deflate, br" : this.device.getAcceptEncoding());
    }

    public void setFirefoxHeaders(HttpRequest httpRequest) {
        httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
        httpRequest.putHeader("user-agent", this.device.getUserAgent());
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("accept-language", this.device.getAcceptLanguage());
        httpRequest.putHeader("accept-encoding", this.device.getAcceptEncoding() == null ? "gzip, deflate, br" : this.device.getAcceptEncoding());
        httpRequest.putHeader("referer", this.origin + "/");
        httpRequest.putHeader("content-type", "application/x-www-form-urlencoded");
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("origin", this.origin);
        httpRequest.putHeader("te", "trailers");
    }

    public static String getSTSTOKEN(JsonObject jsonObject) {
        return PXCaptcha.parseDO(jsonObject, STS_PATTERN);
    }

    public void setNeedsDesktopAppID(boolean bl) {
        this.needsDesktopAppID = bl;
    }

    public static String getCSValue(JsonObject jsonObject) {
        return PXCaptcha.parseCS(jsonObject, CS_PATTERN);
    }

    public static void lambda$main$2(RealClient realClient, HttpServerRequest httpServerRequest) {
        httpServerRequest.body(arg_0 -> PXCaptcha.lambda$main$1(httpServerRequest, realClient, arg_0));
    }

    public HttpRequest getCaptchaJS(String string, String string2) {
        String string3 = "";
        if (string2 != null) {
            string3 = string2;
        }
        Object object = "";
        switch (PXCaptcha$1.$SwitchMap$io$trickle$task$sites$Site[this.SITE.ordinal()]) {
            case 1: {
                object = "https://captcha.px-cdn.net/PX9Qx3Rve4/captcha.js?a=c&u=" + string + "&v=" + string3 + "&m=1";
                return this.getScriptRequest((String)object);
            }
            case 2: {
                object = "https://captcha.perimeterx.net/PXUArm9B04/captcha.js?a=c&m=1&u=" + string + "&v=" + string3 + "&g=b";
                return this.getScriptRequest((String)object);
            }
        }
        return this.getScriptRequest((String)object);
    }

    public static Future sendPayload(HttpServerRequest httpServerRequest, Buffer buffer, RealClient realClient) {
        Promise promise = Promise.promise();
        HttpRequest httpRequest = PXCaptcha.atc(httpServerRequest, realClient);
        httpRequest.sendBuffer(buffer, arg_0 -> PXCaptcha.lambda$sendPayload$3(promise, arg_0));
        return promise.future();
    }

    public void setChromeHeaders(HttpRequest httpRequest) {
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        if (this.device.getSecUA() != null && this.device.getSecUAMobile() != null) {
            httpRequest.putHeader("sec-ch-ua", this.device.getSecUA());
            httpRequest.putHeader("sec-ch-ua-mobile", this.device.getSecUAMobile());
        }
        httpRequest.putHeader("user-agent", this.device.getUserAgent());
        httpRequest.putHeader("content-type", "application/x-www-form-urlencoded");
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("origin", this.origin);
        httpRequest.putHeader("sec-fetch-site", "cross-site");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", this.origin + "/");
        httpRequest.putHeader("accept-encoding", this.device.getAcceptEncoding() == null ? "gzip, deflate, br" : this.device.getAcceptEncoding());
        httpRequest.putHeader("accept-language", this.device.getAcceptLanguage());
    }

    public PXCaptcha(Logger logger, RealClient realClient, Types types, String string, String string2, Site site) {
        this.logger = logger;
        this.client = RealClientFactory.fromOther(Vertx.currentContext().owner(), realClient, ClientType.CHROME);
        this.type = types;
        this.parentVID = string;
        this.parentUUID = string2;
        this.SITE = site;
        this.vidAge = System.currentTimeMillis();
        this.needsDesktopAppID = false;
    }

    public Types getType() {
        return this.type;
    }

    public static String parseDO(JsonObject jsonObject, Pattern pattern) {
        String string = jsonObject.getJsonArray("do").toString();
        Matcher matcher = pattern.matcher(string);
        if (matcher.find()) return matcher.group(1);
        return null;
    }

    public static String parseCS(JsonObject jsonObject, Pattern pattern) {
        String string = jsonObject.getJsonArray("do").toString().replace("wcs", "");
        Matcher matcher = pattern.matcher(string);
        matcher.find();
        return matcher.group(1);
    }

    public static String getVIDUUID(JsonObject jsonObject) {
        return PXCaptcha.parseDO(jsonObject, VID_UUID_PATTERN);
    }

    public void setSafariImageHeaders(HttpRequest httpRequest) {
        httpRequest.putHeaders(Headers$Pseudo.MSPA.get());
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("origin", this.origin);
        httpRequest.putHeader("accept-encoding", this.device.getAcceptEncoding() == null ? "gzip, deflate, br" : this.device.getAcceptEncoding());
        httpRequest.putHeader("user-agent", this.device.getUserAgent());
        httpRequest.putHeader("accept-language", this.device.getAcceptLanguage());
        httpRequest.putHeader("referer", this.origin + "/");
    }

    public Devices$DeviceImpl getDevice() {
        return this.device;
    }

    public static String getSFFTOKEN(JsonObject jsonObject) {
        return PXCaptcha.parseDO(jsonObject, SFF_SCS_PATTERN);
    }

    public static HttpRequest bundleReq(String string, RealClient realClient) {
        HttpRequest httpRequest = realClient.postAbs("https://collector-" + "PX9Qx3Rve4".toLowerCase() + ".px-cloud.net/assets/js/bundle").timeout(TimeUnit.SECONDS.toMillis(15L)).as(BodyCodec.jsonObject());
        httpRequest.putHeaders(Headers$Pseudo.MASP.get());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("user-agent", string);
        httpRequest.putHeader("content-type", "application/x-www-form-urlencoded");
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("origin", "https://www.perimeterx.com");
        httpRequest.putHeader("sec-fetch-site", "cross-site");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.perimeterx.com/");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public static String getCI(JsonObject jsonObject) {
        return PXCaptcha.parseDO(jsonObject, CI_PATTERN);
    }

    public String getParentVID() {
        return this.parentVID;
    }

    public String getUseragent() {
        return this.device.getUserAgent();
    }

    public void updateVIDandUUID(RealClient realClient, String string, String string2) {
        this.client = RealClientFactory.fromOther(Vertx.currentContext().owner(), realClient, ClientType.CHROME);
        if (!(string == null || this.parentVID != null && this.parentVID.equals(string))) {
            this.vidAge = System.currentTimeMillis();
        }
        this.parentVID = string;
        this.parentUUID = string2;
    }

    public static String getWCS(JsonObject jsonObject) {
        return PXCaptcha.parseDO(jsonObject, WCS_PATTERN);
    }

    public static String getSIDUUID(JsonObject jsonObject) {
        return PXCaptcha.parseDO(jsonObject, SID_UUID_PATTERN);
    }

    public void setType(Types types) {
        this.type = types;
    }

    public Optional parseResult(String string) {
        if (!this.type.equals((Object)Types.DESKTOP) && !string.contains("cv|0")) {
            return Optional.empty();
        }
        Matcher matcher = BAKE_PATTERN.matcher(string);
        if (!matcher.find()) return Optional.empty();
        return Optional.of("3:" + matcher.group(1));
    }

    public CompletableFuture setBrowserType() {
        Devices$DeviceImpl devices$DeviceImpl;
        if (this.device != null) return CompletableFuture.completedFuture(null);
        this.mobile = false;
        PXCaptcha pXCaptcha = this;
        if (this.mobile) {
            CompletableFuture completableFuture = Devices.randomMobileBrowser();
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                PXCaptcha pXCaptcha2 = pXCaptcha;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> PXCaptcha.async$setBrowserType(this, pXCaptcha2, completableFuture2, 1, arg_0));
            }
            devices$DeviceImpl = (Devices$DeviceImpl)completableFuture.join();
        } else {
            CompletableFuture completableFuture = Devices.randomDesktopBrowser();
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture3 = completableFuture;
                PXCaptcha pXCaptcha3 = pXCaptcha;
                return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> PXCaptcha.async$setBrowserType(this, pXCaptcha3, completableFuture3, 2, arg_0));
            }
            devices$DeviceImpl = (Devices$DeviceImpl)completableFuture.join();
        }
        pXCaptcha.device = devices$DeviceImpl;
        return CompletableFuture.completedFuture(null);
    }

    public static void lambda$main$0(HttpServerRequest httpServerRequest, Buffer buffer) {
        httpServerRequest.response().setStatusCode(200).putHeader(HttpHeaders.CONTENT_TYPE, (CharSequence)"application/json; charset=utf-8").putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, (CharSequence)"true").putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, (CharSequence)"GET,HEAD,PUT,PATCH,POST,DELETE").putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, (CharSequence)"*").putHeader("timing-allow-origin", "*").putHeader("via", "1.1 google").putHeader("alt-svc", "clear").putHeader("expires", "0").putHeader("cache-control", "no-cache").end();
    }
}

