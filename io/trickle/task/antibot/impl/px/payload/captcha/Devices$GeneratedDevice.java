/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.task.antibot.impl.px.payload.captcha.Devices
 *  io.trickle.task.antibot.impl.px.payload.captcha.Devices$Device
 */
package io.trickle.task.antibot.impl.px.payload.captcha;

import io.trickle.task.antibot.impl.px.payload.captcha.Devices;
import java.util.concurrent.ThreadLocalRandom;

public class Devices$GeneratedDevice
implements Devices.Device {
    public int width;
    public int height;
    public String platform;
    public static String[] resolutions = new String[]{"412x732", "412x869", "412x824", "412x846", "412x847", "412x732", "360x740", "480x853", "360x740", "360x640", "375x667", "414x896", "412x869", "360x780", "393x851", "375x812", "360x720", "414x736", "480x853"};
    public String userAgent;

    public int getAvailHeight() {
        return this.height;
    }

    public static String getRandomUA() {
        return Devices.userAgents[ThreadLocalRandom.current().nextInt(Devices.userAgents.length)];
    }

    public int getInnerHeight() {
        return this.height;
    }

    public int getAvailWidth() {
        return this.width;
    }

    public Devices$GeneratedDevice() {
        String[] stringArray = resolutions[ThreadLocalRandom.current().nextInt(resolutions.length)].split("x");
        this.width = Integer.parseInt(stringArray[0]);
        this.height = Integer.parseInt(stringArray[1]);
        this.userAgent = Devices$GeneratedDevice.getRandomUA() + " PerimeterX Android SDK/" + "v1.13.2".substring(1);
        this.platform = "Linux armv8l";
    }

    public String getProductSub() {
        return "20030107";
    }

    public String getAppVersion() {
        try {
            String string = this.getUserAgent().split("/")[0].concat("/");
            return this.getUserAgent().replace(string, "");
        }
        catch (Throwable throwable) {
            return this.getUserAgent().replace("Mozilla/", "");
        }
    }

    public int getHeight() {
        return this.height;
    }

    public String getAppCodeName() {
        return "Mozilla";
    }

    public String getPlatform() {
        return this.platform;
    }

    public String getProduct() {
        return "Gecko";
    }

    public String getUserAgent() {
        return this.userAgent;
    }

    public String getAppName() {
        return "Netscape";
    }

    public int getWidth() {
        return this.width;
    }

    public int getInnerWidth() {
        return this.width;
    }
}
