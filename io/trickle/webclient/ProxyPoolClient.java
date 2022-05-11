package io.trickle.webclient;

import io.trickle.core.Controller;
import io.trickle.core.Engine;
import io.trickle.core.VertxSingleton;
import io.trickle.proxy.ProxyController;
import io.vertx.core.impl.utils.ConcurrentCyclicSequence;
import io.vertx.ext.web.client.WebClient;
import java.util.concurrent.CompletableFuture;

public class ProxyPoolClient {
   public ConcurrentCyclicSequence clientSequence = new ConcurrentCyclicSequence();

   public CompletableFuture removeBad(WebClient var1) {
      this.clientSequence = this.clientSequence.remove(var1);
      return CompletableFuture.completedFuture((Object)null);
   }

   public WebClient getClient() {
      while((double)this.clientSequence.size() < Math.ceil((double)((ProxyController)Engine.get().getModule(Controller.PROXY)).loadedProxies() / Double.longBitsToDouble(4629137466983448576L))) {
         this.clientSequence = this.clientSequence.add(RealClientFactory.buildRandomProxied(VertxSingleton.INSTANCE.get()));
      }

      return (WebClient)this.clientSequence.next();
   }
}
