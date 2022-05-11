/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.task.sites.shopify.Flow$Actions
 */
package io.trickle.task.sites.shopify;

import io.trickle.task.sites.shopify.Flow;
import java.util.Map;

public class Flow$FlowImpl
implements Flow.Actions {
    public boolean needsPrice;
    public Map<String, String> requiredCookies;
    public boolean needsAuthTokens;

    public boolean needsPrice() {
        return this.needsPrice;
    }

    public boolean needsAuthTokens() {
        return this.needsAuthTokens;
    }

    public Map requiredCookies() {
        return this.requiredCookies;
    }

    public Flow$FlowImpl(boolean bl, boolean bl2, Map map) {
        this.needsPrice = bl;
        this.needsAuthTokens = bl2;
        this.requiredCookies = map;
    }
}
