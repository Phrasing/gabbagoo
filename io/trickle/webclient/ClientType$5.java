/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.http.Http2Settings
 *  io.vertx.core.http.HttpVersion
 *  io.vertx.core.net.ConscryptSSLEngineOptions
 *  io.vertx.core.net.SSLEngineOptions
 *  io.vertx.ext.web.client.WebClientOptions
 */
package io.trickle.webclient;

import io.trickle.webclient.ClientType;
import io.vertx.core.http.Http2Settings;
import io.vertx.core.http.HttpVersion;
import io.vertx.core.net.ConscryptSSLEngineOptions;
import io.vertx.core.net.SSLEngineOptions;
import io.vertx.ext.web.client.WebClientOptions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClientType$5
extends ClientType {
    @Override
    public List ciphers() {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("TLS_AES_128_GCM_SHA256");
        arrayList.add("TLS_AES_256_GCM_SHA384");
        arrayList.add("TLS_CHACHA20_POLY1305_SHA256");
        arrayList.add("TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256");
        arrayList.add("TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384");
        arrayList.add("TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256");
        arrayList.add("TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256");
        arrayList.add("TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384");
        arrayList.add("TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256");
        arrayList.add("TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA");
        arrayList.add("TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA");
        arrayList.add("TLS_RSA_WITH_AES_128_GCM_SHA256");
        arrayList.add("TLS_RSA_WITH_AES_256_GCM_SHA384");
        arrayList.add("TLS_RSA_WITH_AES_128_CBC_SHA");
        arrayList.add("TLS_RSA_WITH_AES_256_CBC_SHA");
        return arrayList;
    }

    @Override
    public WebClientOptions options() {
        WebClientOptions webClientOptions = new WebClientOptions();
        webClientOptions.setInitialSettings(new Http2Settings().setInitialWindowSize(0x1000000)).setProtocolVersion(HttpVersion.HTTP_2).setSslEngineOptions((SSLEngineOptions)new ConscryptSSLEngineOptions()).addEnabledSecureTransportProtocol("TLSv1.3").addEnabledSecureTransportProtocol("TLSv1.2").addEnabledSecureTransportProtocol("TLSv1.1").addEnabledSecureTransportProtocol("TLSv1.0").getEnabledCipherSuites().addAll(this.ciphers());
        super.baseOptions(webClientOptions);
        return webClientOptions;
    }

    @Override
    public int getWindowUpdate() {
        return 0xFF0001;
    }

    public List ciphersRandomised() {
        List list = this.ciphers();
        Collections.shuffle(list);
        return list;
    }
}

