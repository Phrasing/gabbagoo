package io.trickle.webclient;

import io.vertx.core.http.Http2Settings;
import io.vertx.core.http.HttpVersion;
import io.vertx.core.net.ConscryptSSLEngineOptions;
import io.vertx.core.net.ConscryptSSLEngineOptions.Attribute;
import io.vertx.ext.web.client.WebClientOptions;
import java.util.LinkedHashSet;
import java.util.Set;

public enum ClientType$3 {
   public WebClientOptions options() {
      WebClientOptions var1 = new WebClientOptions();
      var1.setInitialSettings((new Http2Settings()).setHeaderTableSize(65536L).setPushEnabled(true).setMaxConcurrentStreams(1000L).setInitialWindowSize(6291456).setMaxFrameSize(16384).setMaxHeaderListSize(262144L).setSettingsAutoACK(false)).setProtocolVersion(HttpVersion.HTTP_2).setSplitCookies(true).setSslEngineOptions((new ConscryptSSLEngineOptions()).setAttributeFluent(Attribute.BROTLI, true).setAttributeFluent(Attribute.GREASE, true).setAttributeFluent(Attribute.SESSION_TICKET, true).setAttributeFluent(Attribute.SIGNED_CERT_TIMESTAMPS, true).setRemovedSigAlgsFluent(new short[]{513})).setEnabledSecureTransportProtocols(this.supportedVersions()).addEnabledCipherSuite("TLS_AES_128_GCM_SHA256").addEnabledCipherSuite("TLS_AES_256_GCM_SHA384").addEnabledCipherSuite("TLS_CHACHA20_POLY1305_SHA256").addEnabledCipherSuite("TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256").addEnabledCipherSuite("TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256").addEnabledCipherSuite("TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384").addEnabledCipherSuite("TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384").addEnabledCipherSuite("TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256").addEnabledCipherSuite("TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256").addEnabledCipherSuite("TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA").addEnabledCipherSuite("TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA").addEnabledCipherSuite("TLS_RSA_WITH_AES_128_GCM_SHA256").addEnabledCipherSuite("TLS_RSA_WITH_AES_256_GCM_SHA384").addEnabledCipherSuite("TLS_RSA_WITH_AES_128_CBC_SHA").addEnabledCipherSuite("TLS_RSA_WITH_AES_256_CBC_SHA");
      super.baseOptions(var1);
      return var1;
   }

   public Set supportedVersions() {
      LinkedHashSet var1 = new LinkedHashSet();
      var1.add("TLSv1.3");
      var1.add("TLSv1.2");
      return var1;
   }

   public int getWindowUpdate() {
      return 15663105;
   }
}
