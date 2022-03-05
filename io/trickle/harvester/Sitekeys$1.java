/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.harvester;

import io.trickle.task.sites.Site;

public class Sitekeys$1 {
    public static int[] $SwitchMap$io$trickle$task$sites$Site = new int[Site.values().length];

    static {
        try {
            Sitekeys$1.$SwitchMap$io$trickle$task$sites$Site[Site.WALMART.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Sitekeys$1.$SwitchMap$io$trickle$task$sites$Site[Site.YEEZY.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Sitekeys$1.$SwitchMap$io$trickle$task$sites$Site[Site.JDSPORTS.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Sitekeys$1.$SwitchMap$io$trickle$task$sites$Site[Site.FINISHLINE.ordinal()] = 4;
            return;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}

