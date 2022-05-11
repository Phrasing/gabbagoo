package io.trickle.proxy;

import io.trickle.core.producers.AsyncLockingProducer;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class RandomProxyProducer extends AsyncLockingProducer {
   public WeakReference reference;

   public void handle() {
      List var1 = (List)this.reference.get();

      try {
         if (var1 == null) {
            throw new Exception("Proxies list cannot be null");
         }

         Proxy var2 = (Proxy)var1.get(ThreadLocalRandom.current().nextInt(var1.size()));
         super.produce(var2);
      } catch (Exception var3) {
         super.fail(var3);
      }

   }

   public RandomProxyProducer(List var1) {
      this.reference = new WeakReference(var1);
      Objects.requireNonNull((List)this.reference.get());
   }
}
