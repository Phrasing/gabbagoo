/*
 * Decompiled with CFR 0.152.
 */
package io.trickle.task.sites.shopify.util;

import java.time.Instant;

public class PaymentTokenManager$PaymentToken {
    public String value;
    public long expiry;

    public PaymentTokenManager$PaymentToken(String string) {
        this.value = string;
        this.expiry = Instant.now().getEpochSecond() + 1740L;
    }

    public boolean isExpired() {
        return Instant.now().getEpochSecond() - this.expiry > 0L;
    }
}
