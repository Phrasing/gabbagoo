/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.sites.shopify;

import io.trickle.task.sites.shopify.Flow;
import io.trickle.task.sites.shopify.Flow$Actions;
import io.trickle.task.sites.shopify.Flow$FlowImpl;
import java.util.WeakHashMap;

public class Flow$3
extends Flow {
    @Override
    public Flow$Actions get() {
        WeakHashMap weakHashMap = new WeakHashMap();
        return new Flow$FlowImpl(false, false, weakHashMap);
    }
}

