/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.task.sites.Site
 */
package io.trickle.util.analytics.webhook;

import io.trickle.task.sites.Site;

public class OrderDetails$1 {
    public static int[] $SwitchMap$io$trickle$task$sites$Site = new int[Site.values().length];

    static {
        try {
            OrderDetails$1.$SwitchMap$io$trickle$task$sites$Site[Site.WALMART.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            OrderDetails$1.$SwitchMap$io$trickle$task$sites$Site[Site.WALMART_NEW.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            OrderDetails$1.$SwitchMap$io$trickle$task$sites$Site[Site.HIBBETT.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            OrderDetails$1.$SwitchMap$io$trickle$task$sites$Site[Site.YEEZY.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            OrderDetails$1.$SwitchMap$io$trickle$task$sites$Site[Site.BESTBUY.ordinal()] = 5;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}
