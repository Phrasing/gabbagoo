/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.sites.shopify.constants;

public interface Redirects {
    public static final String CHECKPOINT;
    public static final String LOGIN_REQUIRED;
    public static final String QUEUE;
    public static final String CHALLENGE;
    public static final String OOS;
    public static final String PASSWORD;
    public static final String BAD_ACCOUNT;
    public static final String CART;
    public static final String QUEUE_OLD;

    static {
        BAD_ACCOUNT = "/account/login?return_url=%2f";
        QUEUE = "/queue";
        QUEUE_OLD = "_ctd";
        OOS = "/stock_problems";
        CHALLENGE = "/challenge";
        LOGIN_REQUIRED = "login?checkout_url=";
        CHECKPOINT = "checkpoint";
        PASSWORD = "password";
        CART = "/cart";
    }
}

