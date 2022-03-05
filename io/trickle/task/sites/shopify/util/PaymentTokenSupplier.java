/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.sites.shopify.util;

import java.util.function.Supplier;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public class PaymentTokenSupplier
implements Supplier {
    public boolean vaulted;
    public String token;

    public boolean isVaulted() {
        return this.vaulted;
    }

    @Deprecated
    public void setSubmittedSuccessfully() {
        this.token = "";
        this.vaulted = true;
    }

    public Object get() {
        return this.get();
    }

    public PaymentTokenSupplier(String string) {
        this.token = string;
        this.vaulted = false;
    }

    public String get() {
        return this.token;
    }
}

