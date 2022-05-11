package io.trickle.webclient;

import io.vertx.core.http.Http2Settings;
import io.vertx.core.http.HttpVersion;
import io.vertx.core.net.ConscryptSSLEngineOptions;
import io.vertx.core.net.ConscryptSSLEngineOptions.Attribute;
import io.vertx.ext.web.client.WebClientOptions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum ClientType$4 {
   public List ciphersRandomised() {
      List var1 = this.ciphers();
      Collections.shuffle(var1);
      return var1;
   }

   public WebClientOptions options() {
      WebClientOptions var1 = new WebClientOptions();
      var1.setInitialSettings((new Http2Settings()).setInitialWindowSize(16777216)).setProtocolVersion(HttpVersion.HTTP_2).setSslEngineOptions((new ConscryptSSLEngineOptions()).setAttributeFluent(Attribute.GREASE, false).setAttributeFluent(Attribute.SESSION_TICKET, true)).addEnabledSecureTransportProtocol("TLSv1.3").addEnabledSecureTransportProtocol("TLSv1.2").addEnabledSecureTransportProtocol("TLSv1.1").addEnabledSecureTransportProtocol("TLSv1.0").getEnabledCipherSuites().addAll(this.ciphersRandomised());
      super.baseOptions(var1);
      return var1;
   }

   public int getWindowUpdate() {
      return 16711681;
   }

   public List ciphers() {
      ArrayList var1 = new ArrayList();
      var1.add("TLS_AES_128_GCM_SHA256");
      var1.add("TLS_AES_256_GCM_SHA384");
      var1.add("TLS_CHACHA20_POLY1305_SHA256");
      var1.add("TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256");
      var1.add("TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256");
      var1.add("TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA");
      var1.add("TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA");
      var1.add("TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA");
      var1.add("TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA");
      var1.add("TLS_RSA_WITH_AES_128_GCM_SHA256");
      var1.add("TLS_RSA_WITH_AES_128_CBC_SHA");
      var1.add("TLS_RSA_WITH_AES_256_CBC_SHA");
      return var1;
   }
}
