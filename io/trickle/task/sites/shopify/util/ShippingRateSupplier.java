/*
 * Decompiled with CFR 0.152.
 */
package io.trickle.task.sites.shopify.util;

import java.util.function.Supplier;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public class ShippingRateSupplier
implements Supplier {
    public String rate;

    public String get() {
        return this.rate;
    }

    public void updateRate(String string) {
        this.rate = string;
    }

    public Object get() {
        return this.get();
    }

    public ShippingRateSupplier(String string) {
        this.rate = string;
    }
}
