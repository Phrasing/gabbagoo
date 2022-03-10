/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task;

import io.trickle.task.sites.Site;
import io.trickle.task.sites.shopify.Mode;

public class TaskController$1 {
    public static int[] $SwitchMap$io$trickle$task$sites$shopify$Mode;
    public static int[] $SwitchMap$io$trickle$task$sites$Site;

    static {
        $SwitchMap$io$trickle$task$sites$Site = new int[Site.values().length];
        try {
            TaskController$1.$SwitchMap$io$trickle$task$sites$Site[Site.WALMART_NEW.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            TaskController$1.$SwitchMap$io$trickle$task$sites$Site[Site.WALMART_CA.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            TaskController$1.$SwitchMap$io$trickle$task$sites$Site[Site.YEEZY.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            TaskController$1.$SwitchMap$io$trickle$task$sites$Site[Site.BESTBUY.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            TaskController$1.$SwitchMap$io$trickle$task$sites$Site[Site.HIBBETT.ordinal()] = 5;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        $SwitchMap$io$trickle$task$sites$shopify$Mode = new int[Mode.values().length];
        try {
            TaskController$1.$SwitchMap$io$trickle$task$sites$shopify$Mode[Mode.HUMAN.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            TaskController$1.$SwitchMap$io$trickle$task$sites$shopify$Mode[Mode.NORMAL.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            TaskController$1.$SwitchMap$io$trickle$task$sites$shopify$Mode[Mode.FAST.ordinal()] = 3;
            return;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}

