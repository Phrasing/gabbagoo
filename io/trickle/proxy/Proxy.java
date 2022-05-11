package io.trickle.proxy;

import io.vertx.core.net.ProxyOptions;
import io.vertx.core.net.ProxyType;

public class Proxy {
   public String port;
   public String user;
   public boolean ipAuth;
   public String host;
   public String pass;
   public boolean isLocal;

   public String getHost() {
      return this.host;
   }

   public Proxy(String var1, String var2, String var3, String var4) {
      this.host = var1;
      this.port = var2;
      this.user = var3;
      this.pass = var4;
      this.ipAuth = false;
      this.isLocal = false;
   }

   public boolean isLocal() {
      return this.isLocal;
   }

   public static ProxyOptions toVertxProxy(Proxy var0) {
      if (var0.isLocal) {
         return null;
      } else {
         ProxyOptions var1 = new ProxyOptions();
         var1.setHost(var0.host).setPort(Integer.parseInt(var0.port));
         if (!var0.ipAuth) {
            var1.setUsername(var0.user);
            var1.setPassword(var0.pass);
         }

         var1.setType(ProxyType.HTTP);
         return var1;
      }
   }

   public boolean isIpAuth() {
      return this.ipAuth;
   }

   public String string() {
      return this.host + ":" + this.port;
   }

   public static Proxy fromArray(String[] var0) {
      if (var0.length == 2) {
         return new Proxy(var0[0], var0[1]);
      } else {
         return var0.length == 4 ? new Proxy(var0[0], var0[1], var0[2], var0[3]) : new Proxy();
      }
   }

   public Proxy(String var1, String var2) {
      this.host = var1;
      this.port = var2;
      this.user = null;
      this.pass = null;
      this.ipAuth = true;
      this.isLocal = false;
   }

   public ProxyOptions getAsVertx() {
      return toVertxProxy(this);
   }

   public String[] toParams() {
      String[] var1 = new String[this.ipAuth ? 2 : 4];
      var1[0] = this.host;
      var1[1] = this.port;
      if (!this.ipAuth) {
         var1[2] = this.user;
         var1[3] = this.pass;
      }

      return var1;
   }

   public Proxy() {
      this.host = null;
      this.port = null;
      this.user = null;
      this.pass = null;
      this.ipAuth = true;
      this.isLocal = true;
   }

   public static Proxy fromString(String var0) {
      return fromArray(var0.split(":"));
   }

   public String toString() {
      return "Proxy{host='" + this.host + "', port='" + this.port + "', user='" + this.user + "', pass='" + this.pass + "', ipAuth=" + this.ipAuth + "', isLocal=" + this.isLocal + "}";
   }
}
