/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.sites.shopify;

import io.trickle.task.sites.shopify.Flow$Actions;
import java.util.Map;

public class Flow$FlowImpl
implements Flow$Actions {
    public boolean needsPrice;
    public boolean needsAuthTokens;
    public Map<String, String> requiredCookies;

    public Flow$FlowImpl(boolean bl, boolean bl2, Map map) {
        this.needsPrice = bl;
        this.needsAuthTokens = bl2;
        this.requiredCookies = map;
    }

    @Override
    public boolean needsPrice() {
        return this.needsPrice;
    }

    @Override
    public boolean needsAuthTokens() {
        return this.needsAuthTokens;
    }

    @Override
    public Map requiredCookies() {
        return this.requiredCookies;
    }
}

