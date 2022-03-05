/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.sites.walmart;

public enum Mode {
    DESKTOP,
    MOBILE;


    public static Mode getMode(String string) {
        if (!string.contains("desktop")) return MOBILE;
        return DESKTOP;
    }
}

