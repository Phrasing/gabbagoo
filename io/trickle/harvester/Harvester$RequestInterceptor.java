/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.teamdev.jxbrowser.net.HttpHeader
 *  com.teamdev.jxbrowser.net.HttpStatus
 *  com.teamdev.jxbrowser.net.UrlRequestJob
 *  com.teamdev.jxbrowser.net.UrlRequestJob$Options
 *  com.teamdev.jxbrowser.net.callback.InterceptUrlRequestCallback
 *  com.teamdev.jxbrowser.net.callback.InterceptUrlRequestCallback$Params
 *  com.teamdev.jxbrowser.net.callback.InterceptUrlRequestCallback$Response
 *  io.trickle.harvester.CaptchaToken
 *  io.trickle.harvester.Harvester
 *  io.trickle.harvester.Sitekeys
 *  io.trickle.util.request.Request
 *  io.vertx.core.buffer.Buffer
 *  io.vertx.core.http.RequestOptions
 *  io.vertx.ext.web.client.HttpResponse
 */
package io.trickle.harvester;

import com.teamdev.jxbrowser.net.HttpHeader;
import com.teamdev.jxbrowser.net.HttpStatus;
import com.teamdev.jxbrowser.net.UrlRequestJob;
import com.teamdev.jxbrowser.net.callback.InterceptUrlRequestCallback;
import io.trickle.harvester.CaptchaToken;
import io.trickle.harvester.Harvester;
import io.trickle.harvester.Sitekeys;
import io.trickle.util.request.Request;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.RequestOptions;
import io.vertx.ext.web.client.HttpResponse;
import java.nio.charset.StandardCharsets;

public class Harvester$RequestInterceptor
implements InterceptUrlRequestCallback {
    public Harvester this$0;

    public boolean isSupportedV3Site(String string) {
        if (!string.contains("https://www.google.com") || !string.endsWith(".js")) return string.contains("yeezysupply") || string.contains("jdsports") || string.contains("finishline") || string.contains("/account/login") || string.contains("/account/register") || string.equals("https://www.abc-mart.net/shop/order/method.aspx") || string.startsWith("https://www.abc-mart.net/shop/order/estimate.aspx?estimate=");
        return true;
    }

    public static void lambda$on$1(UrlRequestJob urlRequestJob, HttpResponse httpResponse) {
        urlRequestJob.write(((Buffer)httpResponse.body()).getBytes());
        urlRequestJob.complete();
    }

    public InterceptUrlRequestCallback.Response on(InterceptUrlRequestCallback.Params params) {
        String string = params.urlRequest().url();
        boolean bl = string.contains(".ico");
        if (this.this$0.tokenHolder.get() != null && ((CaptchaToken)this.this$0.tokenHolder.get()).isCheckpoint() && string.contains("captcha") && (string.contains("reload") || string.contains("replaceimage"))) {
            this.this$0.resetClickCounter();
            UrlRequestJob urlRequestJob = params.newUrlRequestJob(UrlRequestJob.Options.newBuilder((HttpStatus)HttpStatus.OK).addHttpHeader(HttpHeader.of((String)"Access-Control-Allow-Origin", (String)"*")).addHttpHeader(HttpHeader.of((String)"Cross-Origin-Resource-Policy", (String)"cross-origin")).build());
            Harvester.executorService.submit(() -> this.lambda$on$2(string, params, urlRequestJob));
            return InterceptUrlRequestCallback.Response.intercept((UrlRequestJob)urlRequestJob);
        }
        if (string.contains("https://www.google.com")) {
            return InterceptUrlRequestCallback.Response.proceed();
        }
        if (!bl && this.isSupportedV2Site(string)) {
            UrlRequestJob urlRequestJob = params.newUrlRequestJob(UrlRequestJob.Options.newBuilder((HttpStatus)HttpStatus.OK).build());
            urlRequestJob.write(Harvester.captchaPageV2().replace("%s", Sitekeys.getSitekeyFromLink((String)string)).getBytes(StandardCharsets.UTF_8));
            urlRequestJob.complete();
            return InterceptUrlRequestCallback.Response.intercept((UrlRequestJob)urlRequestJob);
        }
        if (!bl && this.isSupportedV3Site(string)) {
            UrlRequestJob urlRequestJob = params.newUrlRequestJob(UrlRequestJob.Options.newBuilder((HttpStatus)HttpStatus.OK).build());
            urlRequestJob.write(String.format(Harvester.captchaPageV3(), this.this$0.reloads, Sitekeys.getSitekeyFromLink((String)string), Sitekeys.getSitekeyFromLink((String)string), Sitekeys.getActionFromLink((String)string)).getBytes(StandardCharsets.UTF_8));
            urlRequestJob.complete();
            return InterceptUrlRequestCallback.Response.intercept((UrlRequestJob)urlRequestJob);
        }
        if (!bl && this.isCheckpoint(string)) {
            UrlRequestJob urlRequestJob = params.newUrlRequestJob(UrlRequestJob.Options.newBuilder((HttpStatus)HttpStatus.OK).build());
            CaptchaToken captchaToken = this.this$0.solveFuture.getEmptyCaptchaToken();
            if (((CaptchaToken)this.this$0.tokenHolder.get()).getToken() == null) {
                if (captchaToken.isHcaptcha) {
                    this.this$0.hotkey = null;
                    if (System.currentTimeMillis() - captchaToken.creationTime > 8000L) {
                        captchaToken.shopify.getCPHtml(true).thenAccept(arg_0 -> Harvester$RequestInterceptor.lambda$on$3(urlRequestJob, captchaToken, arg_0));
                    } else {
                        urlRequestJob.write(((CaptchaToken)this.this$0.tokenHolder.get()).getHtml().getBytes(StandardCharsets.UTF_8));
                        urlRequestJob.complete();
                    }
                } else {
                    this.this$0.hotkey = Character.valueOf('r');
                    urlRequestJob.write(((CaptchaToken)this.this$0.tokenHolder.get()).getHtml().getBytes(StandardCharsets.UTF_8));
                    urlRequestJob.complete();
                }
            } else {
                urlRequestJob.write("<html>Waiting for captcha</html>".getBytes(StandardCharsets.UTF_8));
                urlRequestJob.complete();
            }
            return InterceptUrlRequestCallback.Response.intercept((UrlRequestJob)urlRequestJob);
        }
        if (!string.contains("recaptcha.net")) return InterceptUrlRequestCallback.Response.proceed();
        UrlRequestJob urlRequestJob = params.newUrlRequestJob(UrlRequestJob.Options.newBuilder((HttpStatus)HttpStatus.OK).build());
        return InterceptUrlRequestCallback.Response.intercept((UrlRequestJob)urlRequestJob);
    }

    public static RequestOptions lambda$on$0(InterceptUrlRequestCallback.Params params) {
        return Request.convertToVertx((InterceptUrlRequestCallback.Params)params);
    }

    public boolean isSupportedV2Site(String string) {
        if (!string.contains("https://www.google.com")) return string.endsWith("/challenge");
        if (!string.endsWith(".js")) return string.endsWith("/challenge");
        return true;
    }

    public boolean isCheckpoint(String string) {
        if (!string.contains("https://www.google.com")) return string.contains("/checkpoint");
        if (!string.endsWith(".js")) return string.contains("/checkpoint");
        return true;
    }

    public Object on(Object object) {
        return this.on((InterceptUrlRequestCallback.Params)object);
    }

    public static void lambda$on$3(UrlRequestJob urlRequestJob, CaptchaToken captchaToken, String string) {
        urlRequestJob.write(captchaToken.v2Html(string).getBytes(StandardCharsets.UTF_8));
        urlRequestJob.complete();
    }

    public Harvester$RequestInterceptor(Harvester harvester) {
        this.this$0 = harvester;
    }

    public void lambda$on$2(String string, InterceptUrlRequestCallback.Params params, UrlRequestJob urlRequestJob) {
        try {
            this.this$0.transferCookies(string);
            this.this$0.send(() -> Harvester$RequestInterceptor.lambda$on$0(params), params.uploadData()).thenAccept(arg_0 -> Harvester$RequestInterceptor.lambda$on$1(urlRequestJob, arg_0));
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
