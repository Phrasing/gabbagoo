/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.task.sites.Site
 *  io.trickle.task.sites.shopify.util.REDIRECT_STATUS
 */
package io.trickle.task.sites.shopify;

import io.trickle.task.sites.Site;
import io.trickle.task.sites.shopify.util.REDIRECT_STATUS;

public class Shopify$1 {
    public static int[] $SwitchMap$io$trickle$task$sites$shopify$util$REDIRECT_STATUS;
    public static int[] $SwitchMap$io$trickle$task$sites$Site;

    static {
        $SwitchMap$io$trickle$task$sites$Site = new int[Site.values().length];
        try {
            Shopify$1.$SwitchMap$io$trickle$task$sites$Site[Site.HUMANMADE.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Shopify$1.$SwitchMap$io$trickle$task$sites$Site[Site.KAWS_TOKYO.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Shopify$1.$SwitchMap$io$trickle$task$sites$Site[Site.ZINGARO.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Shopify$1.$SwitchMap$io$trickle$task$sites$Site[Site.NEIGHBORHOOD.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Shopify$1.$SwitchMap$io$trickle$task$sites$Site[Site.WINDANDSEA.ordinal()] = 5;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Shopify$1.$SwitchMap$io$trickle$task$sites$Site[Site.BAPE.ordinal()] = 6;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Shopify$1.$SwitchMap$io$trickle$task$sites$Site[Site.CALIF.ordinal()] = 7;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Shopify$1.$SwitchMap$io$trickle$task$sites$Site[Site.DSM_JP_ESHOP.ordinal()] = 8;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Shopify$1.$SwitchMap$io$trickle$task$sites$Site[Site.MCT.ordinal()] = 9;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        $SwitchMap$io$trickle$task$sites$shopify$util$REDIRECT_STATUS = new int[REDIRECT_STATUS.values().length];
        try {
            Shopify$1.$SwitchMap$io$trickle$task$sites$shopify$util$REDIRECT_STATUS[REDIRECT_STATUS.OOS.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Shopify$1.$SwitchMap$io$trickle$task$sites$shopify$util$REDIRECT_STATUS[REDIRECT_STATUS.PASSWORD.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Shopify$1.$SwitchMap$io$trickle$task$sites$shopify$util$REDIRECT_STATUS[REDIRECT_STATUS.QUEUE_OLD.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Shopify$1.$SwitchMap$io$trickle$task$sites$shopify$util$REDIRECT_STATUS[REDIRECT_STATUS.QUEUE_NEW.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Shopify$1.$SwitchMap$io$trickle$task$sites$shopify$util$REDIRECT_STATUS[REDIRECT_STATUS.CHECKPOINT.ordinal()] = 5;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Shopify$1.$SwitchMap$io$trickle$task$sites$shopify$util$REDIRECT_STATUS[REDIRECT_STATUS.LOGIN_REQUIRED.ordinal()] = 6;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Shopify$1.$SwitchMap$io$trickle$task$sites$shopify$util$REDIRECT_STATUS[REDIRECT_STATUS.BAD_ACCOUNT.ordinal()] = 7;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Shopify$1.$SwitchMap$io$trickle$task$sites$shopify$util$REDIRECT_STATUS[REDIRECT_STATUS.CHALLENGE.ordinal()] = 8;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Shopify$1.$SwitchMap$io$trickle$task$sites$shopify$util$REDIRECT_STATUS[REDIRECT_STATUS.HOMEPAGE.ordinal()] = 9;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Shopify$1.$SwitchMap$io$trickle$task$sites$shopify$util$REDIRECT_STATUS[REDIRECT_STATUS.CART.ordinal()] = 10;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}
