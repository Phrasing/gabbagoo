package io.trickle.webclient;

import io.trickle.core.VertxSingleton;
import io.vertx.core.net.ProxyOptions;

public abstract class TaskApiClient {
   public RealClient client;

   public RealClient getWebClient() {
      return this.client;
   }

   public CookieJar getCookies() {
      return this.client.cookieStore();
   }

   public TaskApiClient() {
      this.client = RealClientFactory.buildProxied(VertxSingleton.INSTANCE.get());
   }

   public String proxyStringFull() {
      ProxyOptions var1 = this.client.getOptions().getProxyOptions();
      if (var1 == null) {
         return "no-proxy";
      } else {
         return var1.getPassword() == null ? String.format("%s:%d", var1.getHost(), var1.getPort()) : String.format("%s:%d:%s:%s", var1.getHost(), var1.getPort(), var1.getUsername(), var1.getPassword());
      }
   }

   public void close() {
      this.client.close();
   }

   public String proxyStringSafe() {
      ProxyOptions var1 = this.client.getOptions().getProxyOptions();
      return var1 == null ? "no-proxy" : String.format("%s:%d", var1.getHost(), var1.getPort());
   }

   public boolean rotateProxy() {
      if (this.client != null && this.client.isActive()) {
         try {
            RealClient var1 = RealClientFactory.rotateProxy(VertxSingleton.INSTANCE.get(), this.client);
            this.client.close();
            this.client = var1;
            return true;
         } catch (Throwable var2) {
         }
      }

      return false;
   }

   public TaskApiClient(ClientType var1) {
      this.client = RealClientFactory.buildProxied(VertxSingleton.INSTANCE.get(), var1);
   }

   public TaskApiClient(RealClient var1) {
      this.client = var1;
   }
}
