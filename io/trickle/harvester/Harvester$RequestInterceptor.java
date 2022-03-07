/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.teamdev.jxbrowser.net.HttpHeader
 *  com.teamdev.jxbrowser.net.HttpStatus
 *  com.teamdev.jxbrowser.net.UrlRequestJob
 *  com.teamdev.jxbrowser.net.UrlRequestJob$Options
 *  com.teamdev.jxbrowser.net.callback.InterceptUrlRequestCallback
 *  com.teamdev.jxbrowser.net.callback.InterceptUrlRequestCallback$Params
 *  com.teamdev.jxbrowser.net.callback.InterceptUrlRequestCallback$Response
 *  io.vertx.core.buffer.Buffer
 *  io.vertx.core.http.RequestOptions
 *  io.vertx.ext.web.client.HttpResponse
 */
package io.trickle.harvester;

import com.teamdev.jxbrowser.net.HttpHeader;
import com.teamdev.jxbrowser.net.HttpStatus;
import com.teamdev.jxbrowser.net.UrlRequestJob;
import com.teamdev.jxbrowser.net.callback.InterceptUrlRequestCallback;
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

    public RequestOptions lambda$on$0(InterceptUrlRequestCallback.Params params) {
        return Request.convertToVertx(params, this.this$0.proxy);
    }

    public static void lambda$on$1(UrlRequestJob urlRequestJob, HttpResponse httpResponse) {
        urlRequestJob.write(((Buffer)httpResponse.body()).getBytes());
        urlRequestJob.complete();
    }

    public void lambda$on$2(String string, InterceptUrlRequestCallback.Params params, UrlRequestJob urlRequestJob) {
        try {
            this.this$0.transferCookies(string);
            this.this$0.send(() -> this.lambda$on$0(params), params.uploadData()).thenAccept(arg_0 -> Harvester$RequestInterceptor.lambda$on$1(urlRequestJob, arg_0));
            return;
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public InterceptUrlRequestCallback.Response on(InterceptUrlRequestCallback.Params params) {
        String string = params.urlRequest().url();
        boolean bl = string.contains(".ico");
        if (this.this$0.tokenHolder.get() != null && this.this$0.tokenHolder.get().isCheckpoint() && string.contains("captcha") && string.contains("reload")) {
            UrlRequestJob urlRequestJob = params.newUrlRequestJob(UrlRequestJob.Options.newBuilder((HttpStatus)HttpStatus.OK).addHttpHeader(HttpHeader.of((String)"Access-Control-Allow-Origin", (String)"*")).addHttpHeader(HttpHeader.of((String)"Cross-Origin-Resource-Policy", (String)"cross-origin")).build());
            Harvester.executorService.submit(() -> this.lambda$on$2(string, params, urlRequestJob));
            return InterceptUrlRequestCallback.Response.intercept((UrlRequestJob)urlRequestJob);
        }
        if (!bl && this.isSupportedV2Site(string)) {
            UrlRequestJob urlRequestJob = params.newUrlRequestJob(UrlRequestJob.Options.newBuilder((HttpStatus)HttpStatus.OK).build());
            urlRequestJob.write(Harvester.captchaPageV2().replace("%s", Sitekeys.getSitekeyFromLink(string)).getBytes(StandardCharsets.UTF_8));
            urlRequestJob.complete();
            return InterceptUrlRequestCallback.Response.intercept((UrlRequestJob)urlRequestJob);
        }
        if (!bl && this.isSupportedV3Site(string)) {
            UrlRequestJob urlRequestJob = params.newUrlRequestJob(UrlRequestJob.Options.newBuilder((HttpStatus)HttpStatus.OK).build());
            urlRequestJob.write(String.format(Harvester.captchaPageV3(), this.this$0.reloads, Sitekeys.getSitekeyFromLink(string), Sitekeys.getSitekeyFromLink(string), Sitekeys.getActionFromLink(string)).getBytes(StandardCharsets.UTF_8));
            urlRequestJob.complete();
            return InterceptUrlRequestCallback.Response.intercept((UrlRequestJob)urlRequestJob);
        }
        if (!bl && this.isCheckpoint(string)) {
            UrlRequestJob urlRequestJob = params.newUrlRequestJob(UrlRequestJob.Options.newBuilder((HttpStatus)HttpStatus.OK).build());
            urlRequestJob.write(this.this$0.tokenHolder.get().getHtml().getBytes(StandardCharsets.UTF_8));
            urlRequestJob.complete();
            return InterceptUrlRequestCallback.Response.intercept((UrlRequestJob)urlRequestJob);
        }
        if (!string.contains("recaptcha.net")) return InterceptUrlRequestCallback.Response.proceed();
        UrlRequestJob urlRequestJob = params.newUrlRequestJob(UrlRequestJob.Options.newBuilder((HttpStatus)HttpStatus.OK).build());
        return InterceptUrlRequestCallback.Response.intercept((UrlRequestJob)urlRequestJob);
    }

    public Object on(Object object) {
        return this.on((InterceptUrlRequestCallback.Params)object);
    }

    public boolean isChallenge(String string) {
        return string.contains("challenge");
    }

    public boolean isCheckpoint(String string) {
        if (!string.contains("https://www.google.com")) return string.contains("/checkpoint");
        if (!string.endsWith(".js")) return string.contains("/checkpoint");
        return true;
    }

    public boolean isSupportedV2Site(String string) {
        if (!string.contains("https://www.google.com")) return string.endsWith("/challenge");
        if (!string.endsWith(".js")) return string.endsWith("/challenge");
        return true;
    }

    public boolean isSupportedV3Site(String string) {
        if (string.contains("https://www.google.com") && string.endsWith(".js")) {
            return true;
        }
        if (string.contains("yeezysupply")) return true;
        if (string.contains("jdsports")) return true;
        if (string.contains("finishline")) return true;
        if (string.contains("/account/login")) return true;
        if (string.contains("/account/register")) return true;
        return false;
    }

    public Harvester$RequestInterceptor(Harvester harvester) {
        this.this$0 = harvester;
    }
}

