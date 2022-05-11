package io.trickle.webclient;

import io.vertx.core.net.TrustOptions;
import io.vertx.ext.web.client.WebClientOptions;
import java.security.KeyManagementException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.X509TrustManager;
import org.conscrypt.Conscrypt;

public enum ClientType {
   WALMART_PIXEL_3,
   HIBBETT_ANDROID,
   PX_SDK_IOS,
   CHROME,
   BASIC;

   public static ClientType[] $VALUES = new ClientType[]{BASIC, H1, CHROME, WALMART_PIXEL_3, HIBBETT_ANDROID, PX_SDK_PIXEL_3, PX_SDK_IOS};
   PX_SDK_PIXEL_3,
   H1;

   public List ciphers() {
      return new ArrayList();
   }

   public static TrustOptions getDefault() {
      try {
         return TrustOptions.wrap(Conscrypt.getDefaultX509TrustManager());
      } catch (KeyManagementException var1) {
         return null;
      }
   }

   public WebClientOptions options() {
      return null;
   }

   public static X509TrustManager getDefaultTrust() {
      try {
         return Conscrypt.getDefaultX509TrustManager();
      } catch (KeyManagementException var1) {
         return null;
      }
   }

   public int getWindowUpdate() {
      return -1;
   }

   public WebClientOptions baseOptions(WebClientOptions var1) {
      return var1.setLogActivity(false).setUserAgentEnabled(false).setSsl(true).setUseAlpn(true).setTrustAll(false).setVerifyHost(true).setForceSni(true).setConnectTimeout(150000).setSslHandshakeTimeoutUnit(TimeUnit.SECONDS).setSslHandshakeTimeout(150L).setIdleTimeoutUnit(TimeUnit.SECONDS).setIdleTimeout(150).setKeepAlive(true).setKeepAliveTimeout(30).setHttp2KeepAliveTimeout(100).setHttp2MaxPoolSize(10).setHttp2MultiplexingLimit(250).setPoolCleanerPeriod(15000).setMaxPoolSize(10).setTryUseCompression(true).setTcpFastOpen(true).setTcpKeepAlive(true).setTcpNoDelay(true).setTcpQuickAck(true).setFollowRedirects(false).setTrustOptions(TrustOptions.wrap(new ClientType$TrustWrapper()));
   }
}
