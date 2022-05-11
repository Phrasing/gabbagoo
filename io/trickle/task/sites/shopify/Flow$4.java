/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.task.sites.Site
 */
package io.trickle.task.sites.shopify;

import io.trickle.task.sites.Site;

public class Flow$4 {
    public static int[] $SwitchMap$io$trickle$task$sites$Site = new int[Site.values().length];

    static {
        try {
            Flow$4.$SwitchMap$io$trickle$task$sites$Site[Site.KITH.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Flow$4.$SwitchMap$io$trickle$task$sites$Site[Site.MCT.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}
