package io.trickle.harvester;

import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.engine.RenderingMode;
import com.teamdev.jxbrowser.net.Scheme;
import com.teamdev.jxbrowser.net.callback.AuthenticateCallback;
import com.teamdev.jxbrowser.net.proxy.CustomProxyConfig;
import com.teamdev.jxbrowser.net.proxy.DirectProxyConfig;
import io.trickle.core.Controller;
import io.trickle.core.VertxSingleton;
import io.trickle.proxy.Proxy;
import io.trickle.proxy.ProxyController;
import io.vertx.core.Vertx;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HybridHarvester {
   public WindowedBrowser browser;
   public Engine engine;
   public Vertx vertx;
   public ExecutorService executor;

   public static void main(String[] var0) {
      HybridHarvester var1 = new HybridHarvester(VertxSingleton.INSTANCE.get());
      var1.start();
   }

   public EngineOptions.Builder baseOpts() {
      return EngineOptions.newBuilder(RenderingMode.HARDWARE_ACCELERATED).licenseKey("1BNDIEOFAZ0H665CSFR41MCR5THTYZ8ZE7J946B9XRQ2B35XEE8PDHHOC27XGDJQURKYEQ").addScheme(Scheme.HTTPS, new HybridHarvester$RequestInterceptor());
   }

   public void close() {
      if (this.engine != null && !this.engine.isClosed()) {
         if (this.browser != null) {
            this.browser.close();
         }

         Iterator var1 = this.engine.browsers().iterator();

         while(var1.hasNext()) {
            Browser var2 = (Browser)var1.next();

            try {
               if (!var2.isClosed()) {
                  var2.close();
               }
            } catch (Throwable var4) {
            }
         }

         Engine var10000 = this.engine;
         Objects.requireNonNull(var10000);
         CompletableFuture.runAsync(var10000::close);
      }

      this.executor.shutdownNow();
   }

   public CompletableFuture start() {
      this.executor.submit(this::init);
      return CompletableFuture.completedFuture(true);
   }

   public static void lambda$setProxy$0(String[] var0, AuthenticateCallback.Params var1, AuthenticateCallback.Action var2) {
      if (var1.isProxy()) {
         if (var0.length == 4) {
            var2.authenticate(var0[2], var0[3]);
         } else {
            var2.cancel();
         }
      } else {
         var2.cancel();
      }

   }

   public void init() {
      this.engine = Engine.newInstance(this.baseOpts().build());

      try {
         this.setProxy(((ProxyController)io.trickle.core.Engine.get().getModule(Controller.PROXY)).getProxy(0));
      } catch (Exception var2) {
      }

      this.browser = new WindowedUrlBrowser(this.engine);
      this.browser.createWindow();
   }

   public HybridHarvester(Vertx var1) {
      this.vertx = var1;
      this.executor = Executors.newSingleThreadExecutor();
   }

   public String setProxy(Proxy var1) {
      if (var1.isLocal()) {
         this.engine.proxy().config(DirectProxyConfig.newInstance());
         return "no-proxy";
      } else {
         String[] var2 = var1.toParams();
         this.engine.network().set(AuthenticateCallback.class, HybridHarvester::lambda$setProxy$0);
         this.engine.proxy().config(CustomProxyConfig.newInstance(String.format("http=%s:%s;https=%s:%s", var2[0], var2[1], var2[0], var2[1])));
         return Arrays.toString(var2);
      }
   }
}
