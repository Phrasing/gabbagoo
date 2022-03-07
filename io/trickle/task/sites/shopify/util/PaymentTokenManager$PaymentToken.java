/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.sites.shopify.util;

import java.time.Instant;

public class PaymentTokenManager$PaymentToken {
    public long expiry;
    public String value;

    public boolean isExpired() {
        if (Instant.now().getEpochSecond() - this.expiry <= 0L) return false;
        return true;
    }

    public PaymentTokenManager$PaymentToken(String string) {
        this.value = string;
        this.expiry = Instant.now().getEpochSecond() + 1740L;
    }
}

