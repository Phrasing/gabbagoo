package io.trickle.webclient;

import io.vertx.core.http.Http2Settings;
import io.vertx.core.http.HttpVersion;
import io.vertx.ext.web.client.WebClientOptions;

public enum ClientType$2 {
   public WebClientOptions options() {
      WebClientOptions var1 = new WebClientOptions();
      var1.setInitialSettings((new Http2Settings()).setHeaderTableSize(65536L).setPushEnabled(true).setMaxConcurrentStreams(1000L).setInitialWindowSize(6291456).setMaxFrameSize(16384).setMaxHeaderListSize(262144L)).setProtocolVersion(HttpVersion.HTTP_1_1);
      super.baseOptions(var1);
      return var1;
   }
}
