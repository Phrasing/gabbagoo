package io.trickle.webclient;

import io.vertx.core.http.HttpVersion;
import io.vertx.ext.web.client.WebClientOptions;

public enum ClientType$1 {
   public WebClientOptions options() {
      WebClientOptions var1 = new WebClientOptions();
      var1.setProtocolVersion(HttpVersion.HTTP_2);
      super.baseOptions(var1);
      return var1;
   }
}
