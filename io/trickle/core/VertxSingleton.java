package io.trickle.core;

import io.trickle.account.Account;
import io.trickle.harvester.pooled.SharedCaptchaToken;
import io.trickle.util.DirectObjectCodec;
import io.trickle.util.LocalClient;
import io.trickle.util.Pair;
import io.trickle.util.analytics.Graphing;
import io.trickle.util.request.Request;
import io.trickle.webclient.ProxyPoolClient;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.dns.AddressResolverOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.codec.BodyCodec;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public enum VertxSingleton {
   INSTANCE;

   public Map cachedCaptchaBodies;
   public LocalClient client;
   public Vertx vertx;
   public String[] captchaEps = new String[0];
   public static VertxSingleton[] $VALUES = new VertxSingleton[]{INSTANCE};
   public ProxyPoolClient proxyPoolClient;

   public static void lambda$new$0(Throwable var0) {
   }

   public VertxSingleton() {
      this.vertx = Vertx.vertx((new VertxOptions()).setWorkerPoolSize(2).setAddressResolverOptions((new AddressResolverOptions()).setRoundRobinInetAddress(true).setCacheMaxTimeToLive(60)).setMaxWorkerExecuteTime(10L).setBlockedThreadCheckInterval(1L).setBlockedThreadCheckIntervalUnit(TimeUnit.HOURS).setMaxWorkerExecuteTimeUnit(TimeUnit.MINUTES).setPreferNativeTransport(true));
      this.vertx.eventBus().registerDefaultCodec(SharedCaptchaToken.class, new DirectObjectCodec(SharedCaptchaToken.class));
      this.vertx.eventBus().registerDefaultCodec(Account.class, new DirectObjectCodec(Account.class));
      this.vertx.eventBus().registerDefaultCodec(Pair.class, new DirectObjectCodec(Pair.class));
      this.vertx.exceptionHandler(VertxSingleton::lambda$new$0);
      this.register();
      this.client = new LocalClient(this.vertx);
      this.proxyPoolClient = new ProxyPoolClient();
      this.cachedCaptchaBodies = new ConcurrentHashMap();
      this.initCachedCaptchaBodies();
   }

   public void register() {
      this.vertx.eventBus().localConsumer("login.loader", this::loginHandler);
   }

   public LocalClient getLocalClient() {
      return this.client;
   }

   public ProxyPoolClient getProxyPoolClient() {
      return this.proxyPoolClient;
   }

   public void initCachedCaptchaBodies() {
      String[] var1 = this.captchaEps;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         String var4 = var1[var3];
         HttpRequest var5 = this.client.getClient().getAbs(var4).as(BodyCodec.buffer());
         var5.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"98\", \"Google Chrome\";v=\"98\"");
         var5.putHeader("sec-ch-ua-mobile", "?0");
         var5.putHeader("sec-ch-ua-platform", "\"macOS\"");
         var5.putHeader("upgrade-insecure-requests", "1");
         var5.putHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.113 Safari/537.36");
         var5.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
         var5.putHeader("sec-fetch-site", "cross-site");
         var5.putHeader("sec-fetch-mode", "navigate");
         var5.putHeader("sec-fetch-dest", "iframe");
         var5.putHeader("referer", "https://packershoes.com/");
         var5.putHeader("accept-encoding", "gzip, deflate, br");
         var5.putHeader("accept-language", "en-US,en;q=0.9");
         Request.executeTillOk(var5).thenAccept(this::lambda$initCachedCaptchaBodies$1);
      }

   }

   public void lambda$initCachedCaptchaBodies$1(String var1, Buffer var2) {
      System.out.println("put it in");
      this.cachedCaptchaBodies.put(var1, var2);
   }

   public void loginHandler(Message var1) {
      Graphing.analyse((String)var1.body());
   }

   public Vertx get() {
      return this.vertx;
   }
}
