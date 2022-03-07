/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.proxy;

import io.trickle.core.producers.AsyncLockingProducer;
import io.trickle.proxy.Proxy;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class RandomProxyProducer
extends AsyncLockingProducer {
    public WeakReference<List<Proxy>> reference;

    @Override
    public void handle() {
        List list = (List)this.reference.get();
        try {
            if (list == null) throw new Exception("Proxies list cannot be null");
            Proxy proxy = (Proxy)list.get(ThreadLocalRandom.current().nextInt(list.size()));
            super.produce(proxy);
            return;
        }
        catch (Exception exception) {
            super.fail(exception);
        }
    }

    public RandomProxyProducer(List list) {
        this.reference = new WeakReference<List>(list);
        Objects.requireNonNull((List)this.reference.get());
    }
}

