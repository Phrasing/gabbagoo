/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.json.JsonObject
 */
package io.trickle.task.antibot.impl.akamai;

import io.trickle.task.antibot.impl.akamai.Devices$1;
import io.trickle.task.antibot.impl.akamai.Devices$2;
import io.trickle.task.antibot.impl.akamai.Devices$3;
import io.trickle.task.antibot.impl.akamai.Devices$4;
import io.trickle.task.antibot.impl.akamai.Devices$5;
import io.trickle.task.antibot.impl.akamai.Devices$6;
import io.trickle.task.antibot.impl.akamai.Devices$7;
import io.trickle.task.antibot.impl.akamai.Devices$8;
import io.trickle.task.antibot.impl.akamai.Devices$9;
import io.trickle.task.antibot.impl.akamai.Devices$Device;
import io.trickle.task.antibot.impl.akamai.Devices$DeviceImpl;
import io.vertx.core.json.JsonObject;
import java.util.concurrent.ThreadLocalRandom;

public class Devices
extends Enum {
    public static /* enum */ Devices BRAVE_89;
    public static /* enum */ Devices BRAVE_89_4;
    public static /* enum */ Devices BRAVE_89_1;
    public static /* enum */ Devices CHROME_89_Z3;
    public static /* enum */ Devices CHROME_89_MAC_PERSONAL;
    public static /* enum */ Devices CHROME_87_SAFARI;
    public static /* enum */ Devices BRAVE_89_3;
    public static Devices[] $VALUES;
    public static /* enum */ Devices CHROME_89_SAFARI;
    public static /* enum */ Devices BRAVE_89_2;

    public static Devices valueOf(String string) {
        return Enum.valueOf(Devices.class, string);
    }

    public Devices$Device get() {
        return null;
    }

    public static Devices$Device random() {
        return Devices.values()[ThreadLocalRandom.current().nextInt(Devices.values().length)].get();
    }

    static {
        CHROME_89_MAC_PERSONAL = new Devices$1();
        CHROME_89_Z3 = new Devices$2();
        CHROME_89_SAFARI = new Devices$3();
        CHROME_87_SAFARI = new Devices$4();
        BRAVE_89 = new Devices$5();
        BRAVE_89_1 = new Devices$6();
        BRAVE_89_2 = new Devices$7();
        BRAVE_89_3 = new Devices$8();
        BRAVE_89_4 = new Devices$9();
        $VALUES = new Devices[]{CHROME_89_MAC_PERSONAL, CHROME_89_Z3, CHROME_89_SAFARI, CHROME_87_SAFARI, BRAVE_89, BRAVE_89_1, BRAVE_89_2, BRAVE_89_3, BRAVE_89_4};
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

    public static Devices$Device genFromJson(JsonObject jsonObject) {
        try {
            return new Devices$DeviceImpl(jsonObject.getInteger("availWidth"), jsonObject.getInteger("availHeight"), jsonObject.getInteger("width"), jsonObject.getInteger("height"), jsonObject.getInteger("outerWidth"), jsonObject.getInteger("innerHeight"), jsonObject.getInteger("innerWidth"), jsonObject.getString("ua"), jsonObject.getString("product"), jsonObject.getJsonObject("nav").getString("productSub"), jsonObject.getBoolean("brave"), jsonObject.getInteger("activeXObject"), jsonObject.getInteger("documentMode"), jsonObject.getInteger("isChrome"), jsonObject.getInteger("isOnline"), jsonObject.getInteger("isOpera"), jsonObject.getInteger("hasInstallTrigger"), jsonObject.getInteger("hasHTMLElement"), jsonObject.getInteger("hasRTCPeerConnection"), jsonObject.getInteger("hasMozInnerScreen"), jsonObject.getInteger("hasVibrate"), jsonObject.getInteger("hasBattery"), jsonObject.getInteger("hasForEach"), jsonObject.getInteger("hasFileReader"), jsonObject.getInteger("pluginsLength"), jsonObject.getString("languages"), jsonObject.getString("deviceOrientation"), jsonObject.getString("deviceMotion"), jsonObject.getString("touchEvent"), jsonObject.getLong("navigatorFasSettings"), jsonObject.getString("sed"), jsonObject.getInteger("colorDepth"), jsonObject.getInteger("pixelDepth"), jsonObject.getBoolean("cookieEnabled"), jsonObject.getBoolean("javaEnabled"), Integer.parseInt(jsonObject.getValue("doNotTrack").toString()), jsonObject.getString("canvasFP1"), jsonObject.getString("canvasFP2"), jsonObject.getString("pluginInfo"), jsonObject.getBoolean("sessionStorage"), jsonObject.getBoolean("localStorage"), jsonObject.getBoolean("indexedDB"), jsonObject.getInteger("timezoneOffset"), jsonObject.getBoolean("webRTC"), jsonObject.getJsonObject("windowPerms").toString());
        }
        catch (Throwable throwable) {
            return null;
        }
    }
}

