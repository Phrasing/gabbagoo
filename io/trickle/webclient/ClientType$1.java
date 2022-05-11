/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.webclient.ClientType
 *  io.vertx.core.http.HttpVersion
 *  io.vertx.ext.web.client.WebClientOptions
 */
package io.trickle.webclient;

import io.trickle.webclient.ClientType;
import io.vertx.core.http.HttpVersion;
import io.vertx.ext.web.client.WebClientOptions;

public class ClientType$1
extends ClientType {
    public ClientType$1() {
        super(string, n);
    }

    public WebClientOptions options() {
        WebClientOptions webClientOptions = new WebClientOptions();
        webClientOptions.setProtocolVersion(HttpVersion.HTTP_2);
        super.baseOptions(webClientOptions);
        return webClientOptions;
    }
}
