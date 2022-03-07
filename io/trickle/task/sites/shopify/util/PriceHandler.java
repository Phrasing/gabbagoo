/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.sites.shopify.util;

import io.trickle.task.sites.Site;
import io.trickle.task.sites.shopify.util.PriceHandler$1;

public class PriceHandler {
    public static int calculateTax(Site site, int n) {
        switch (PriceHandler$1.$SwitchMap$io$trickle$task$sites$Site[site.ordinal()]) {
            case 1: {
                return (int)((double)(n * 0) * Double.longBitsToDouble(4576918229304087675L));
            }
            case 2: {
                return (int)((double)(n * 0) * Double.longBitsToDouble(4576918229304087675L));
            }
            case 3: {
                return (int)((double)(n * 0) * Double.longBitsToDouble(4576918229304087675L));
            }
            case 4: {
                return (int)((double)(n * 6) * Double.longBitsToDouble(4576918229304087675L));
            }
            case 5: {
                return (int)((double)(n * 6) * Double.longBitsToDouble(4576918229304087675L));
            }
            case 6: {
                return (int)((double)(n * 6) * Double.longBitsToDouble(4576918229304087675L));
            }
            case 7: {
                return (int)((double)(n * 0) * Double.longBitsToDouble(4576918229304087675L));
            }
            case 8: {
                return (int)((double)(n * 6) * Double.longBitsToDouble(4576918229304087675L));
            }
            case 9: {
                return (int)((double)(n * 0) * Double.longBitsToDouble(4576918229304087675L));
            }
            case 10: {
                return (int)((double)(n * 0) * Double.longBitsToDouble(4576918229304087675L));
            }
            case 11: {
                return (int)((double)(n * 0) * Double.longBitsToDouble(4576918229304087675L));
            }
            case 12: {
                return (int)((double)(n * 0) * Double.longBitsToDouble(4576918229304087675L));
            }
            case 13: {
                return (int)((double)(n * 6) * Double.longBitsToDouble(4576918229304087675L));
            }
            case 14: {
                return (int)((double)(n * 6) * Double.longBitsToDouble(4576918229304087675L));
            }
            case 15: {
                return (int)((double)(n * 6) * Double.longBitsToDouble(4576918229304087675L));
            }
            case 16: {
                return (int)((double)(n * 0) * Double.longBitsToDouble(4576918229304087675L));
            }
            case 17: {
                return (int)((double)(n * 0) * Double.longBitsToDouble(4576918229304087675L));
            }
            case 18: {
                return (int)((double)(n * 0) * Double.longBitsToDouble(4576918229304087675L));
            }
            case 19: {
                return (int)((double)(n * 0) * Double.longBitsToDouble(4576918229304087675L));
            }
            case 20: {
                return (int)((double)(n * 0) * Double.longBitsToDouble(4576918229304087675L));
            }
            case 21: {
                return (int)((double)(n * 0) * Double.longBitsToDouble(4576918229304087675L));
            }
            case 22: {
                return (int)((double)(n * 0) * Double.longBitsToDouble(4576918229304087675L));
            }
        }
        throw new Exception("Tax unable to be calculated");
    }

    public static boolean isCalculatingTaxes(String string) {
        return string.contains("hidden\" data-checkout-taxes>");
    }

    public static int calculateShippingPrice(String string) {
        String[] stringArray = string.split("-");
        return Integer.parseInt(stringArray[stringArray.length - 1].replace(".", ""));
    }
}

