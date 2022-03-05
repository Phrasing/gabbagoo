/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.http.HttpVersion
 *  io.vertx.ext.web.client.WebClientOptions
 */
package io.trickle.webclient;

import io.trickle.webclient.ClientType;
import io.vertx.core.http.HttpVersion;
import io.vertx.ext.web.client.WebClientOptions;

public class ClientType$1
extends ClientType {
    @Override
    public WebClientOptions options() {
        WebClientOptions webClientOptions = new WebClientOptions();
        webClientOptions.setProtocolVersion(HttpVersion.HTTP_2);
        super.baseOptions(webClientOptions);
        return webClientOptions;
    }
}

