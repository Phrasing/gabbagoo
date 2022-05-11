/*
 * Decompiled with CFR 0.152.
 */
package io.trickle.task.sites.shopify.util;

public enum REDIRECT_STATUS {
    OOS,
    CHECKPOINT,
    LOGIN_REQUIRED,
    BAD_ACCOUNT,
    QUEUE_OLD,
    QUEUE_NEW,
    PASSWORD,
    CHALLENGE,
    HOMEPAGE,
    CART,
    UNKNOWN;


    public static REDIRECT_STATUS checkRedirectStatus(String string) {
        String string2 = string.toLowerCase();
        if (string2.contains("/stock_problems")) {
            return OOS;
        }
        if (string2.contains("checkpoint")) {
            return CHECKPOINT;
        }
        if (string2.contains("login?checkout_url=")) {
            return LOGIN_REQUIRED;
        }
        if (string2.contains("/account/login?return_url=%2f")) {
            return BAD_ACCOUNT;
        }
        if (string2.contains("/queue")) {
            if (!string2.contains("_ctd")) return QUEUE_NEW;
            return QUEUE_OLD;
        }
        if (string2.contains("password")) {
            return PASSWORD;
        }
        if (string2.contains("/challenge")) {
            return CHALLENGE;
        }
        if (string2.endsWith("/cart")) {
            return CART;
        }
        if (!string2.replace("https://", "").endsWith("/")) return UNKNOWN;
        return HOMEPAGE;
    }
}
