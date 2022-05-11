package io.trickle.util;

import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.trickle.webclient.ClientType;
import io.trickle.webclient.RealClientFactory;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpVersion;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.ProxyOptions;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.ext.web.codec.BodyCodec;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LocalClient {
   public static Logger logger = LogManager.getLogger("CLIENT");
   public WebClient chromeClient;
   public WebClient client;

   public static void lambda$fetchUpdates$0(Promise var0, HttpResponse var1) {
      try {
         if (var1.statusCode() == 200) {
            Buffer var2 = (Buffer)var1.body();
            if (var2 != null) {
               var0.tryComplete(var2.toJsonObject());
            }
         }
      } catch (Throwable var3) {
         var0.fail(var3);
      }

   }

   public WebClient getClient() {
      return this.client;
   }

   public static CompletableFuture async$deleteDeviceFromAPI(LocalClient param0, String param1, JsonObject param2, int param3, CompletableFuture param4, HttpResponse param5, Throwable param6, int param7, Object param8) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture deleteDeviceFromAPI(String var1, JsonObject var2) {
      int var3 = 0;

      while(var3++ < 99999999) {
         CompletableFuture var5;
         CompletableFuture var7;
         try {
            var7 = Request.send(this.postAPI(var1).as(BodyCodec.buffer()), var2);
            if (!var7.isDone()) {
               var5 = var7;
               return var5.exceptionally(Function.identity()).thenCompose(LocalClient::async$deleteDeviceFromAPI);
            }

            HttpResponse var4 = (HttpResponse)var7.join();
            if (var4 != null) {
               if (var4.statusCode() == 200) {
                  return CompletableFuture.completedFuture((Object)null);
               }

               logger.warn("Posting device: status:'{}'", var4.statusCode());
               var7 = VertxUtil.hardCodedSleep(30000L);
               if (!var7.isDone()) {
                  var5 = var7;
                  return var5.exceptionally(Function.identity()).thenCompose(LocalClient::async$deleteDeviceFromAPI);
               }

               var7.join();
            }
         } catch (Throwable var6) {
            var7 = VertxUtil.hardCodedSleep(3000L);
            if (!var7.isDone()) {
               var5 = var7;
               return var5.exceptionally(Function.identity()).thenCompose(LocalClient::async$deleteDeviceFromAPI);
            }

            var7.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$fetchDeviceFromAPI(LocalClient param0, String param1, String param2, int param3, CompletableFuture param4, HttpResponse param5, Throwable param6, int param7, Object param8) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture fetchDeviceFromAPI(String var1) {
      return this.fetchDeviceFromAPI(var1, (String)null);
   }

   public LocalClient(Vertx var1) {
      this.chromeClient = RealClientFactory.createWebClient(var1, ClientType.CHROME, (ProxyOptions)null);
      this.client = WebClient.create(var1, (new WebClientOptions()).setLogActivity(false).setUserAgentEnabled(false).setProtocolVersion(HttpVersion.HTTP_2).setUseAlpn(true).setTrustAll(false).setConnectTimeout(150000).setSslHandshakeTimeoutUnit(TimeUnit.SECONDS).setSslHandshakeTimeout(150L).setIdleTimeoutUnit(TimeUnit.SECONDS).setIdleTimeout(150).setKeepAlive(true).setKeepAliveTimeout(30).setHttp2KeepAliveTimeout(100).setHttp2MaxPoolSize(150).setHttp2MultiplexingLimit(200).setPoolCleanerPeriod(15000).setMaxPoolSize(150).setTryUseCompression(true).setTcpFastOpen(true).setTcpKeepAlive(true).setTcpNoDelay(true).setTcpQuickAck(true).setFollowRedirects(false));
   }

   public CompletableFuture fetchDeviceFromAPI(String var1, String var2) {
      int var3 = 0;

      while(var3++ < 99999999) {
         CompletableFuture var6;
         CompletableFuture var8;
         try {
            var8 = Request.send(var2 == null ? this.fetchAPI(var1).as(BodyCodec.buffer()) : this.fetchAPI(var1).putHeader("ua", var2).as(BodyCodec.buffer()));
            if (!var8.isDone()) {
               var6 = var8;
               return var6.exceptionally(Function.identity()).thenCompose(LocalClient::async$fetchDeviceFromAPI);
            }

            HttpResponse var4 = (HttpResponse)var8.join();
            if (var4 != null) {
               if (var4.statusCode() == 200 && var4.body() != null) {
                  JsonObject var5 = var4.bodyAsJsonObject();
                  return CompletableFuture.completedFuture(var5);
               }

               logger.info(((Buffer)var4.body()).toString());
               logger.warn("Waiting for sensor: status:'{}'", var4.statusCode());
               var8 = VertxUtil.hardCodedSleep(3000L);
               if (!var8.isDone()) {
                  var6 = var8;
                  return var6.exceptionally(Function.identity()).thenCompose(LocalClient::async$fetchDeviceFromAPI);
               }

               var8.join();
            }
         } catch (Throwable var7) {
            var8 = VertxUtil.hardCodedSleep(3000L);
            if (!var8.isDone()) {
               var6 = var8;
               return var6.exceptionally(Function.identity()).thenCompose(LocalClient::async$fetchDeviceFromAPI);
            }

            var8.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public Future fetchUpdates() {
      HttpRequest var1 = this.client.getAbs("https://loudounchris.xyz/api/patch").putHeader("accept", "application/json").putHeader("content-type", "application/json").putHeader("user-agent", "tomato-agent").putHeader("key", Storage.ACCESS_KEY).as(BodyCodec.buffer());
      Promise var2 = Promise.promise();
      Future var10000 = var1.send().onSuccess(LocalClient::lambda$fetchUpdates$0);
      Objects.requireNonNull(var2);
      var10000.onFailure(var2::fail);
      return var2.future();
   }

   public HttpRequest postAPI(String var1) {
      HttpRequest var2 = this.client.postAbs(var1).as(BodyCodec.jsonObject());
      var2.putHeader("accept", "application/json");
      var2.putHeader("content-type", "application/json");
      var2.putHeader("user-agent", "tomato-agent");
      var2.putHeader("key", Storage.ACCESS_KEY);
      return var2;
   }

   public HttpRequest fetchAPI(String var1) {
      HttpRequest var2 = this.client.getAbs(var1).as(BodyCodec.jsonObject());
      var2.putHeader("accept", "application/json");
      var2.putHeader("content-type", "application/json");
      var2.putHeader("user-agent", "tomato-agent");
      var2.putHeader("key", Storage.ACCESS_KEY);
      return var2;
   }

   public WebClient getChromeClient() {
      return this.chromeClient;
   }
}
