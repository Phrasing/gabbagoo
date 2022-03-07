/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.sites.walmart.usa.handling;

public interface Errors {
    public static final String BAD_SESSION = "Method Not Allowed";
    public static final String CHECKOUT_EXPIRED;
    public static final String BAD_CART;

    static {
        BAD_CART = "CRT expired or empty";
        CHECKOUT_EXPIRED = "contract has expired";
    }
}

