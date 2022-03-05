/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.antibot.impl.px.payload.captcha;

import io.trickle.task.antibot.impl.px.Types;
import io.trickle.task.sites.Site;

public class PXCaptcha$1 {
    public static int[] $SwitchMap$io$trickle$task$sites$Site;
    public static int[] $SwitchMap$io$trickle$task$antibot$impl$px$Types;

    static {
        $SwitchMap$io$trickle$task$antibot$impl$px$Types = new int[Types.values().length];
        try {
            PXCaptcha$1.$SwitchMap$io$trickle$task$antibot$impl$px$Types[Types.CAPTCHA_MOBILE.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            PXCaptcha$1.$SwitchMap$io$trickle$task$antibot$impl$px$Types[Types.CAPTCHA_DESKTOP.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        $SwitchMap$io$trickle$task$sites$Site = new int[Site.values().length];
        try {
            PXCaptcha$1.$SwitchMap$io$trickle$task$sites$Site[Site.HIBBETT.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            PXCaptcha$1.$SwitchMap$io$trickle$task$sites$Site[Site.WALMART.ordinal()] = 2;
            return;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}

