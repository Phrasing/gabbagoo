/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.task.sites.shopify.Flow
 *  io.trickle.task.sites.shopify.Flow$Actions
 *  io.trickle.task.sites.shopify.Flow$FlowImpl
 */
package io.trickle.task.sites.shopify;

import io.trickle.task.sites.shopify.Flow;
import java.util.WeakHashMap;

public class Flow$2
extends Flow {
    public Flow$2() {
        super(string, n);
    }

    public Flow.Actions get() {
        WeakHashMap weakHashMap = new WeakHashMap();
        return new Flow.FlowImpl(true, true, weakHashMap);
    }
}
