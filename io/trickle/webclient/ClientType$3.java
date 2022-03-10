/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.http.Http2Settings
 *  io.vertx.core.http.HttpVersion
 *  io.vertx.core.net.ConscryptSSLEngineOptions
 *  io.vertx.core.net.ConscryptSSLEngineOptions$Attribute
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

public class ClientType$3
extends ClientType {
    @Override
    public WebClientOptions options() {
        WebClientOptions webClientOptions = new WebClientOptions();
        webClientOptions.setInitialSettings(new Http2Settings().setHeaderTableSize(65536L).setPushEnabled(true).setMaxConcurrentStreams(1000L).setInitialWindowSize(0x600000).setMaxFrameSize(16384).setMaxHeaderListSize(262144L)).setProtocolVersion(HttpVersion.HTTP_2).setSplitCookies(true).setSslEngineOptions((SSLEngineOptions)new ConscryptSSLEngineOptions().setAttributeFluent(ConscryptSSLEngineOptions.Attribute.BROTLI, true).setAttributeFluent(ConscryptSSLEngineOptions.Attribute.GREASE, true).setAttributeFluent(ConscryptSSLEngineOptions.Attribute.SESSION_TICKET, true).setAttributeFluent(ConscryptSSLEngineOptions.Attribute.SIGNED_CERT_TIMESTAMPS, true).setRemovedSigAlgsFluent(new short[]{513})).addEnabledSecureTransportProtocol("TLSv1.3").addEnabledSecureTransportProtocol("TLSv1.2").addEnabledSecureTransportProtocol("TLSv1.1").addEnabledSecureTransportProtocol("TLSv1.0").addEnabledCipherSuite("TLS_AES_128_GCM_SHA256").addEnabledCipherSuite("TLS_AES_256_GCM_SHA384").addEnabledCipherSuite("TLS_CHACHA20_POLY1305_SHA256").addEnabledCipherSuite("TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256").addEnabledCipherSuite("TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256").addEnabledCipherSuite("TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384").addEnabledCipherSuite("TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384").addEnabledCipherSuite("TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256").addEnabledCipherSuite("TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256").addEnabledCipherSuite("TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA").addEnabledCipherSuite("TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA").addEnabledCipherSuite("TLS_RSA_WITH_AES_128_GCM_SHA256").addEnabledCipherSuite("TLS_RSA_WITH_AES_256_GCM_SHA384").addEnabledCipherSuite("TLS_RSA_WITH_AES_128_CBC_SHA").addEnabledCipherSuite("TLS_RSA_WITH_AES_256_CBC_SHA");
        super.baseOptions(webClientOptions);
        return webClientOptions;
    }

    @Override
    public int getWindowUpdate() {
        return 15663105;
    }
}

