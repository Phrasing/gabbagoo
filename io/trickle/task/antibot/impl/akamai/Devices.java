/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.task.antibot.impl.akamai.Devices$1
 *  io.trickle.task.antibot.impl.akamai.Devices$2
 *  io.trickle.task.antibot.impl.akamai.Devices$3
 *  io.trickle.task.antibot.impl.akamai.Devices$4
 *  io.trickle.task.antibot.impl.akamai.Devices$5
 *  io.trickle.task.antibot.impl.akamai.Devices$6
 *  io.trickle.task.antibot.impl.akamai.Devices$7
 *  io.trickle.task.antibot.impl.akamai.Devices$8
 *  io.trickle.task.antibot.impl.akamai.Devices$9
 *  io.trickle.task.antibot.impl.akamai.Devices$Device
 *  io.trickle.task.antibot.impl.akamai.Devices$DeviceImpl
 *  io.vertx.core.json.JsonObject
 */
package io.trickle.task.antibot.impl.akamai;

import io.trickle.task.antibot.impl.akamai.Devices;
import io.vertx.core.json.JsonObject;
import java.util.concurrent.ThreadLocalRandom;

public class Devices
extends Enum {
    public static /* enum */ Devices CHROME_87_SAFARI;
    public static /* enum */ Devices CHROME_89_MAC_PERSONAL;
    public static /* enum */ Devices BRAVE_89_1;
    public static /* enum */ Devices BRAVE_89_2;
    public static /* enum */ Devices BRAVE_89_3;
    public static /* enum */ Devices CHROME_89_Z3;
    public static /* enum */ Devices CHROME_89_SAFARI;
    public static Devices[] $VALUES;
    public static /* enum */ Devices BRAVE_89;
    public static /* enum */ Devices BRAVE_89_4;

    static {
        CHROME_89_MAC_PERSONAL = new 1("CHROME_89_MAC_PERSONAL", 0);
        CHROME_89_Z3 = new 2("CHROME_89_Z3", 1);
        CHROME_89_SAFARI = new 3("CHROME_89_SAFARI", 2);
        CHROME_87_SAFARI = new 4("CHROME_87_SAFARI", 3);
        BRAVE_89 = new 5("BRAVE_89", 4);
        BRAVE_89_1 = new 6("BRAVE_89_1", 5);
        BRAVE_89_2 = new 7("BRAVE_89_2", 6);
        BRAVE_89_3 = new 8("BRAVE_89_3", 7);
        BRAVE_89_4 = new 9("BRAVE_89_4", 8);
        $VALUES = new Devices[]{CHROME_89_MAC_PERSONAL, CHROME_89_Z3, CHROME_89_SAFARI, CHROME_87_SAFARI, BRAVE_89, BRAVE_89_1, BRAVE_89_2, BRAVE_89_3, BRAVE_89_4};
    }

    public static Device random() {
        return Devices.values()[ThreadLocalRandom.current().nextInt(Devices.values().length)].get();
    }

    public Device get() {
        return null;
    }

    public static Device genFromJson(JsonObject jsonObject) {
        try {
            DeviceImpl deviceImpl = new DeviceImpl(jsonObject.getInteger("availWidth").intValue(), jsonObject.getInteger("availHeight").intValue(), jsonObject.getInteger("width").intValue(), jsonObject.getInteger("height").intValue(), jsonObject.getInteger("outerWidth").intValue(), jsonObject.getInteger("innerHeight").intValue(), jsonObject.getInteger("innerWidth").intValue(), jsonObject.getString("ua"), jsonObject.getString("product"), jsonObject.getJsonObject("nav").getString("productSub"), jsonObject.getBoolean("brave").booleanValue(), jsonObject.getInteger("activeXObject").intValue(), jsonObject.getInteger("documentMode").intValue(), jsonObject.getInteger("isChrome").intValue(), jsonObject.getInteger("isOnline").intValue(), jsonObject.getInteger("isOpera").intValue(), jsonObject.getInteger("hasInstallTrigger").intValue(), jsonObject.getInteger("hasHTMLElement").intValue(), jsonObject.getInteger("hasRTCPeerConnection").intValue(), jsonObject.getInteger("hasMozInnerScreen").intValue(), jsonObject.getInteger("hasVibrate").intValue(), jsonObject.getInteger("hasBattery").intValue(), jsonObject.getInteger("hasForEach").intValue(), jsonObject.getInteger("hasFileReader").intValue(), jsonObject.getInteger("pluginsLength").intValue(), jsonObject.getString("languages"), jsonObject.getString("deviceOrientation"), jsonObject.getString("deviceMotion"), jsonObject.getString("touchEvent"), jsonObject.getLong("navigatorFasSettings").longValue(), jsonObject.getString("sed"), jsonObject.getInteger("colorDepth").intValue(), jsonObject.getInteger("pixelDepth").intValue(), jsonObject.getBoolean("cookieEnabled").booleanValue(), jsonObject.getBoolean("javaEnabled").booleanValue(), Integer.parseInt(jsonObject.getValue("doNotTrack").toString()), jsonObject.getString("canvasFP1"), jsonObject.getString("canvasFP2"), jsonObject.getString("pluginInfo"), jsonObject.getBoolean("sessionStorage").booleanValue(), jsonObject.getBoolean("localStorage").booleanValue(), jsonObject.getBoolean("indexedDB").booleanValue(), jsonObject.getInteger("timezoneOffset").intValue(), jsonObject.getBoolean("webRTC").booleanValue(), jsonObject.getJsonObject("windowPerms").toString());
            return deviceImpl;
        }
        catch (Throwable throwable) {
            return null;
        }
    }

    public static Devices valueOf(String string) {
        return Enum.valueOf(Devices.class, string);
    }

    public static Devices[] values() {
        return (Devices[])$VALUES.clone();
    }

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    public Devices() {
        void var2_-1;
        void var1_-1;
    }
}
