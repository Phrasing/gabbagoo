/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.sites.shopify.util;

import java.util.function.Supplier;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public class ShippingRateSupplier
implements Supplier {
    public String rate;

    public Object get() {
        return this.get();
    }

    public ShippingRateSupplier(String string) {
        this.rate = string;
    }

    public String get() {
        return this.rate;
    }

    public void updateRate(String string) {
        this.rate = string;
    }
}

