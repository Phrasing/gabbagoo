/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.sites.shopify;

import io.trickle.task.sites.Site;
import io.trickle.task.sites.shopify.Flow$1;
import io.trickle.task.sites.shopify.Flow$2;
import io.trickle.task.sites.shopify.Flow$3;
import io.trickle.task.sites.shopify.Flow$4;
import io.trickle.task.sites.shopify.Flow$Actions;

public class Flow
extends Enum {
    public static /* enum */ Flow KITH;
    public static Flow[] $VALUES;
    public static /* enum */ Flow MCT;
    public static /* enum */ Flow CUSTOM;

    public static Flow[] values() {
        return (Flow[])$VALUES.clone();
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
        CUSTOM = new Flow$1();
        KITH = new Flow$2();
        MCT = new Flow$3();
        $VALUES = new Flow[]{CUSTOM, KITH, MCT};
    }

    public static Flow getFlow(Site site) {
        switch (Flow$4.$SwitchMap$io$trickle$task$sites$Site[site.ordinal()]) {
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

    public Flow$Actions get() {
        return null;
    }
}

