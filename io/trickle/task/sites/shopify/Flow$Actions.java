/*
 * Decompiled with CFR 0.152.
 */
package io.trickle.task.sites.shopify;

import java.util.Map;

public interface Flow$Actions {
    public boolean needsPrice();

    public Map requiredCookies();

    public boolean needsAuthTokens();
}
