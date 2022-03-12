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
    public String token;
    public boolean vaulted;

    public String get() {
        return this.token;
    }

    public boolean isVaulted() {
        return this.vaulted;
    }

    public Object get() {
        return this.get();
    }

    @Deprecated
    public void setSubmittedSuccessfully() {
        this.token = "";
        this.vaulted = true;
    }

    public PaymentTokenSupplier(String string) {
        this.token = string;
        this.vaulted = false;
    }
}

