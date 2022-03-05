/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.antibot.impl.px;

import io.trickle.task.sites.Site;

public class PXTokenAPI2$1 {
    public static int[] $SwitchMap$io$trickle$task$sites$Site = new int[Site.values().length];

    static {
        try {
            PXTokenAPI2$1.$SwitchMap$io$trickle$task$sites$Site[Site.HIBBETT.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            PXTokenAPI2$1.$SwitchMap$io$trickle$task$sites$Site[Site.WALMART.ordinal()] = 2;
            return;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}

