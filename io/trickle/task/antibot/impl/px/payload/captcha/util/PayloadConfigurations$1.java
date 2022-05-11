/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.task.antibot.impl.px.Types
 *  io.trickle.task.sites.Site
 */
package io.trickle.task.antibot.impl.px.payload.captcha.util;

import io.trickle.task.antibot.impl.px.Types;
import io.trickle.task.sites.Site;

public class PayloadConfigurations$1 {
    public static int[] $SwitchMap$io$trickle$task$sites$Site;
    public static int[] $SwitchMap$io$trickle$task$antibot$impl$px$Types;

    static {
        $SwitchMap$io$trickle$task$antibot$impl$px$Types = new int[Types.values().length];
        try {
            PayloadConfigurations$1.$SwitchMap$io$trickle$task$antibot$impl$px$Types[Types.DESKTOP.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            PayloadConfigurations$1.$SwitchMap$io$trickle$task$antibot$impl$px$Types[Types.CAPTCHA_MOBILE.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            PayloadConfigurations$1.$SwitchMap$io$trickle$task$antibot$impl$px$Types[Types.CAPTCHA_DESKTOP.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        $SwitchMap$io$trickle$task$sites$Site = new int[Site.values().length];
        try {
            PayloadConfigurations$1.$SwitchMap$io$trickle$task$sites$Site[Site.HIBBETT.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            PayloadConfigurations$1.$SwitchMap$io$trickle$task$sites$Site[Site.WALMART.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}
