/*
 * Decompiled with CFR 0.152.
 */
package io.trickle.task.sites.walmart.usa.handling;

public class CheckoutExpiryException
extends Exception {
    public CheckoutExpiryException() {
        super("PCID Expired");
    }
}
