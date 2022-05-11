/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.task.sites.Site
 *  io.trickle.task.sites.shopify.Flow$1
 *  io.trickle.task.sites.shopify.Flow$2
 *  io.trickle.task.sites.shopify.Flow$3
 *  io.trickle.task.sites.shopify.Flow$4
 *  io.trickle.task.sites.shopify.Flow$Actions
 */
package io.trickle.task.sites.shopify;

import io.trickle.task.sites.Site;
import io.trickle.task.sites.shopify.Flow;

public class Flow
extends Enum {
    public static /* enum */ Flow KITH;
    public static Flow[] $VALUES;
    public static /* enum */ Flow CUSTOM;
    public static /* enum */ Flow MCT;

    public static Flow getFlow(Site site) {
        switch (4.$SwitchMap$io$trickle$task$sites$Site[site.ordinal()]) {
            case 1: {
                return KITH;
            }
            case 2: {
                return MCT;
            }
        }
        return CUSTOM;
    }

    public static Flow valueOf(String string) {
        return Enum.valueOf(Flow.class, string);
    }

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    public Flow() {
        void var2_-1;
        void var1_-1;
    }

    static {
        CUSTOM = new 1("CUSTOM", 0);
        KITH = new 2("KITH", 1);
        MCT = new 3("MCT", 2);
        $VALUES = new Flow[]{CUSTOM, KITH, MCT};
    }

    public Actions get() {
        return null;
    }

    public static Flow[] values() {
        return (Flow[])$VALUES.clone();
    }
}
