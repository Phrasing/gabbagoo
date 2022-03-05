/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.MultiMap
 */
package io.trickle.task.antibot.impl.px.payload.captcha.util;

import io.trickle.task.antibot.impl.px.Types;
import io.trickle.task.antibot.impl.px.payload.ExtendedPayload;
import io.trickle.task.antibot.impl.px.payload.captcha.util.PayloadConfigurations$1;
import io.trickle.task.sites.Site;
import io.vertx.core.MultiMap;

public class PayloadConfigurations {
    public static void configurePayload(MultiMap multiMap, ExtendedPayload extendedPayload) {
        Types types = extendedPayload.getType();
        Site site = extendedPayload.getSite();
        switch (PayloadConfigurations$1.$SwitchMap$io$trickle$task$antibot$impl$px$Types[types.ordinal()]) {
            case 1: {
                multiMap.set("payload", ExtendedPayload.encode(extendedPayload.desktopString(), 50));
                switch (PayloadConfigurations$1.$SwitchMap$io$trickle$task$sites$Site[site.ordinal()]) {
                    case 1: {
                        multiMap.set("appId", "PXAJDckzHD");
                        multiMap.set("tag", "v6.5.5");
                        multiMap.set("uuid", extendedPayload.getUUID());
                        multiMap.set("ft", "202");
                        return;
                    }
                    case 2: {
                        multiMap.set("appId", "PXu6b0qd2S");
                        multiMap.set("tag", "v7.2.4");
                        multiMap.set("uuid", extendedPayload.getUUID());
                        multiMap.set("ft", "245");
                        return;
                    }
                }
                return;
            }
            case 2: {
                multiMap.set("payload", ExtendedPayload.encode(extendedPayload.toString(), 50));
                switch (PayloadConfigurations$1.$SwitchMap$io$trickle$task$sites$Site[site.ordinal()]) {
                    case 1: {
                        multiMap.set("appId", "PX9Qx3Rve4");
                        multiMap.set("tag", "v6.5.5");
                        multiMap.set("uuid", extendedPayload.getUUID());
                        multiMap.set("ft", "202");
                        return;
                    }
                    case 2: {
                        multiMap.set("appId", "PXUArm9B04");
                        multiMap.set("tag", "v7.2.4");
                        multiMap.set("uuid", extendedPayload.getUUID());
                        multiMap.set("ft", "245");
                        return;
                    }
                }
                return;
            }
            case 3: {
                multiMap.set("payload", ExtendedPayload.encode(extendedPayload.toString(), 50));
                switch (PayloadConfigurations$1.$SwitchMap$io$trickle$task$sites$Site[site.ordinal()]) {
                    case 1: {
                        multiMap.set("appId", "PXAJDckzHD");
                        multiMap.set("tag", "v6.5.5");
                        multiMap.set("uuid", extendedPayload.getUUID());
                        multiMap.set("ft", "202");
                        return;
                    }
                    case 2: {
                        multiMap.set("appId", "PXu6b0qd2S");
                        multiMap.set("tag", "v7.2.4");
                        multiMap.set("uuid", extendedPayload.getUUID());
                        multiMap.set("ft", "245");
                    }
                }
                return;
            }
        }
    }
}

