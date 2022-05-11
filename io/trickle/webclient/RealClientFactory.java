package io.trickle.webclient;

import io.trickle.core.Controller;
import io.trickle.core.Engine;
import io.trickle.proxy.ProxyController;
import io.vertx.core.Vertx;
import io.vertx.core.net.ProxyOptions;
import io.vertx.ext.web.client.WebClient;
import java.util.Objects;

public class RealClientFactory {
   public static WebClient createWebClient(Vertx var0, ProxyOptions var1) {
      return createWebClient(var0, ClientType.CHROME, var1);
   }

   public static RealClient build(Vertx var0, ClientType var1) {
      Objects.requireNonNull(var0);
      Objects.requireNonNull(var1);
      return RealClient.create(createWebClient(var0, var1, (ProxyOptions)null), var1);
   }

   public static RealClient fromOther(Vertx var0, RealClient var1, ProxyOptions var2) {
      Objects.requireNonNull(var0);
      Objects.requireNonNull(var1);
      Objects.requireNonNull(var2);
      CookieJar var3 = var1.cookieStore();
      return RealClient.create(createWebClient(var0, var1.type(), var2), var3, var1.type());
   }

   public static RealClient buildProxied(Vertx var0, ClientType var1) {
      Objects.requireNonNull(var0);
      Objects.requireNonNull(var1);
      ProxyOptions var2 = ((ProxyController)Engine.get().getModule(Controller.PROXY)).getProxyCyclic().getAsVertx();
      return RealClient.create(createWebClient(var0, var1, var2), var1);
   }

   public static WebClient createWebClient(Vertx var0, ClientType var1, ProxyOptions var2) {
      return WebClient.create(var0, var1.options().setProxyOptions(var2));
   }

   public static RealClient fromOther(Vertx var0, RealClient var1, ClientType var2) {
      Objects.requireNonNull(var0);
      Objects.requireNonNull(var1);
      Objects.requireNonNull(var2);
      ProxyOptions var3 = var1.getOptions().getProxyOptions();
      CookieJar var4 = var1.cookieStore();
      return RealClient.create(createWebClient(var0, var2, var3), var4, var2);
   }

   public static RealClient fromOtherFreshCookie(Vertx var0, RealClient var1) {
      Objects.requireNonNull(var0);
      Objects.requireNonNull(var1);
      ProxyOptions var2 = var1.getOptions().getProxyOptions();
      CookieJar var3 = new CookieJar(var1.cookieStore());
      return RealClient.create(createWebClient(var0, var1.type(), var2), var3, var1.type());
   }

   public static RealClient createChild(Vertx var0, RealClient var1) {
      Objects.requireNonNull(var0);
      Objects.requireNonNull(var1);
      return RealClient.create(WebClient.wrap(var1.getClient(), var1.getOptions()), new CookieJar(var1.cookieStore()), var1.type());
   }

   public static RealClient buildProxied(Vertx var0, ClientType var1, Controller var2) {
      Objects.requireNonNull(var0);
      Objects.requireNonNull(var1);
      ProxyOptions var3 = ((ProxyController)Engine.get().getModule(var2)).getProxyCyclic().getAsVertx();
      return RealClient.create(createWebClient(var0, var1, var3), var1);
   }

   public static RealClient buildRandomProxied(Vertx var0) {
      Objects.requireNonNull(var0);
      ProxyOptions var1 = ((ProxyController)Engine.get().getModule(Controller.PROXY)).getProxyRandomCyclic().getAsVertx();
      return RealClient.create(createWebClient(var0, var1), ClientType.CHROME);
   }

   public static RealClient buildProxied(Vertx var0) {
      Objects.requireNonNull(var0);
      ProxyOptions var1 = ((ProxyController)Engine.get().getModule(Controller.PROXY)).getProxyCyclic().getAsVertx();
      return RealClient.create(createWebClient(var0, var1), ClientType.CHROME);
   }

   public static RealClient rotateProxy(Vertx var0, RealClient var1) {
      ProxyOptions var2 = ((ProxyController)Engine.get().getModule(Controller.PROXY)).getProxyCyclic().getAsVertx();
      Objects.requireNonNull(var0);
      Objects.requireNonNull(var1);
      CookieJar var3 = var1.cookieStore();
      return RealClient.create(createWebClient(var0, var1.type(), var2), var3, var1.type());
   }

   public static RealClient rotateProxy(Vertx var0, RealClient var1, Controller var2) {
      ProxyOptions var3 = ((ProxyController)Engine.get().getModule(var2)).getProxyCyclic().getAsVertx();
      Objects.requireNonNull(var0);
      Objects.requireNonNull(var1);
      CookieJar var4 = var1.cookieStore();
      return RealClient.create(createWebClient(var0, var1.type(), var3), var4, var1.type());
   }

   public static RealClient buildProxied(Vertx var0, ClientType var1, ProxyOptions var2) {
      Objects.requireNonNull(var0);
      Objects.requireNonNull(var1);
      return RealClient.create(createWebClient(var0, var1, var2), var1);
   }

   public static RealClient fromOther(Vertx var0, RealClient var1) {
      Objects.requireNonNull(var0);
      Objects.requireNonNull(var1);
      return fromOther(var0, var1, var1.type());
   }
}
