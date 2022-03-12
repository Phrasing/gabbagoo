/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.sites.shopify;

import java.util.Map;

public interface Flow$Actions {
    public boolean needsAuthTokens();

    public Map requiredCookies();

    public boolean needsPrice();
}

