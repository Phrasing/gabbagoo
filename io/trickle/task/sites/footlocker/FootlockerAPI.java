/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.codec.BodyCodec
 */
package io.trickle.task.sites.footlocker;

import io.trickle.task.Task;
import io.trickle.util.request.Headers$Pseudo;
import io.trickle.webclient.TaskApiClient;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.codec.BodyCodec;
import java.util.concurrent.TimeUnit;

public class FootlockerAPI
extends TaskApiClient {
    public Task task;

    public FootlockerAPI(Task task) {
        this.task = task;
    }

    public HttpRequest homePage() {
        HttpRequest httpRequest = this.client.getAbs("https://www.finishline.com/").timeout(TimeUnit.SECONDS.toMillis(15L)).as(BodyCodec.string());
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
}

