/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.codec.BodyCodec
 */
package io.trickle.task.sites.finishline;

import io.trickle.task.Task;
import io.trickle.task.sites.Site;
import io.trickle.util.request.Headers$Pseudo;
import io.trickle.webclient.TaskApiClient;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.codec.BodyCodec;
import java.util.concurrent.TimeUnit;

public class FinishlineAPI
extends TaskApiClient {
    public String site;
    public Task task;

    public HttpRequest homePage() {
        HttpRequest httpRequest = this.client.getAbs("https://www.finishline.com/").timeout(TimeUnit.SECONDS.toMillis(30L)).as(BodyCodec.string());
        httpRequest.putHeaders(Headers$Pseudo.MASP.get());
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("Upgrade-Insecure-Requests", "1");
        httpRequest.putHeader("User-Agent", "Mozilla/5.0 (compatible; Googlebot/2.1; startmebot/1.0; +https://start.me/bot)");
        httpRequest.putHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("Sec-Fetch-Site", "none");
        httpRequest.putHeader("Sec-Fetch-Mode", "navigate");
        httpRequest.putHeader("Sec-Fetch-User", "?1");
        httpRequest.putHeader("Sec-Fetch-Dest", "document");
        httpRequest.putHeader("Accept-Encoding", "gzip, deflate, br");
        httpRequest.putHeader("Accept-Language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest cartSession() {
        HttpRequest httpRequest = this.client.getAbs("https://www." + this.site + ".com/store/cart/cartSlide.jsp?stage=pdp").as(BodyCodec.string());
        httpRequest.putHeaders(Headers$Pseudo.MASP.get());
        httpRequest.putHeader("accept", "text/html, */*; q=0.01");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        httpRequest.putHeader("User-Agent", "Mozilla/5.0 (compatible; Googlebot/2.1; startmebot/1.0; +https://start.me/bot)");
        httpRequest.putHeader("x-requested-with", "XMLHttpRequest");
        httpRequest.putHeader("Sec-Fetch-Site", "none");
        httpRequest.putHeader("Sec-Fetch-Mode", "no-cors");
        httpRequest.putHeader("Sec-Fetch-Dest", "empty");
        httpRequest.putHeader("Referer", "https://www." + this.site + ".com/store/product/~/prod2823438");
        return httpRequest;
    }

    public FinishlineAPI(Task task, Site site) {
        this.task = task;
        this.site = site.toString().toLowerCase();
    }
}

