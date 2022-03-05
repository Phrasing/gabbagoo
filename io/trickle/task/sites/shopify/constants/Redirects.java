/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.sites.shopify.constants;

public interface Redirects {
    public static final String QUEUE;
    public static final String LOGIN_REQUIRED;
    public static final String QUEUE_OLD;
    public static final String CHECKPOINT;
    public static final String OOS;
    public static final String CHALLENGE;
    public static final String PASSWORD;
    public static final String BAD_ACCOUNT;
    public static final String CART;

    static {
        OOS = "/stock_problems";
        QUEUE_OLD = "_ctd";
        CART = "/cart";
        BAD_ACCOUNT = "/account/login?return_url=%2f";
        PASSWORD = "password";
        QUEUE = "/queue";
        LOGIN_REQUIRED = "login?checkout_url=";
        CHECKPOINT = "checkpoint";
        CHALLENGE = "/challenge";
    }
}

