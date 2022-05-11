package io.trickle.webclient;

import io.trickle.util.concurrent.ContextCompletableFuture;
import io.vertx.core.http.HttpConnection;
import io.vertx.core.http.impl.Http2ClientConnection;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.impl.WebClientSessionAware;
import io.vertx.ext.web.client.spi.CookieStore;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class RealClient extends WebClientSessionAware {
   public boolean active = true;
   public AtomicReference connectionRef = new AtomicReference((Object)null);
   public ClientType type;

   public CompletableFuture windowUpdateCallback() {
      Http2ClientConnection var1 = (Http2ClientConnection)this.connectionRef.get();
      if (var1 != null) {
         ContextCompletableFuture var2 = new ContextCompletableFuture();
         var1.onWindowUpdate(var2);
         return var2;
      } else {
         return CompletableFuture.completedFuture((Object)null);
      }
   }

   public boolean isActive() {
      return this.active;
   }

   public CompletableFuture headersCallback() {
      Http2ClientConnection var1 = (Http2ClientConnection)this.connectionRef.get();
      if (var1 != null) {
         ContextCompletableFuture var2 = new ContextCompletableFuture();
         var1.onHeaders(var2);
         return var2;
      } else {
         return CompletableFuture.completedFuture((Object)null);
      }
   }

   public static RealClient create(WebClient var0, ClientType var1) {
      return create(var0, new CookieJar(), var1);
   }

   public RealClient(WebClient var1, CookieJar var2, ClientType var3) {
      super(var1, var2);
      this.type = var3;
      this.getClient().connectionHandler(this::lambda$new$0);
   }

   public ClientType type() {
      return this.type;
   }

   public CookieJar cookieStore() {
      return (CookieJar)super.cookieStore();
   }

   public CookieStore cookieStore() {
      return this.cookieStore();
   }

   public static RealClient create(WebClient var0, CookieJar var1, ClientType var2) {
      return new RealClient(var0, var1, var2);
   }

   public void close() {
      this.active = false;
      super.close();
   }

   public void lambda$new$0(HttpConnection var1) {
      try {
         this.connectionRef.set((Http2ClientConnection)var1);
      } catch (Throwable var3) {
      }

      var1.setWindowSize(var1.getWindowSize() + this.type.getWindowUpdate());
   }

   public void resetCookieStore() {
      this.cookieStore().clear();
   }
}
