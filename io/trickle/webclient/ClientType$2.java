/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.http.Http2Settings
 *  io.vertx.core.http.HttpVersion
 *  io.vertx.ext.web.client.WebClientOptions
 */
package io.trickle.webclient;

import io.trickle.webclient.ClientType;
import io.vertx.core.http.Http2Settings;
import io.vertx.core.http.HttpVersion;
import io.vertx.ext.web.client.WebClientOptions;

public class ClientType$2
extends ClientType {
    @Override
    public WebClientOptions options() {
        WebClientOptions webClientOptions = new WebClientOptions();
        webClientOptions.setInitialSettings(new Http2Settings().setHeaderTableSize(65536L).setPushEnabled(true).setMaxConcurrentStreams(1000L).setInitialWindowSize(0x600000).setMaxFrameSize(16384).setMaxHeaderListSize(262144L)).setProtocolVersion(HttpVersion.HTTP_1_1);
        super.baseOptions(webClientOptions);
        return webClientOptions;
    }
}

