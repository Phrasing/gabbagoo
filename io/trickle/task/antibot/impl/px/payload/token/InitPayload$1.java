/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.task.sites.Site
 */
package io.trickle.task.antibot.impl.px.payload.token;

import io.trickle.task.sites.Site;

public class InitPayload$1 {
    public static int[] $SwitchMap$io$trickle$task$sites$Site = new int[Site.values().length];

    static {
        try {
            InitPayload$1.$SwitchMap$io$trickle$task$sites$Site[Site.HIBBETT.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            InitPayload$1.$SwitchMap$io$trickle$task$sites$Site[Site.WALMART.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}
