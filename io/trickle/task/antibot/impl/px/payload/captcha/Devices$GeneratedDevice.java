/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.antibot.impl.px.payload.captcha;

import io.trickle.task.antibot.impl.px.payload.captcha.Devices;
import io.trickle.task.antibot.impl.px.payload.captcha.Devices$Device;
import java.util.concurrent.ThreadLocalRandom;

public class Devices$GeneratedDevice
implements Devices$Device {
    public int width;
    public int height;
    public String platform;
    public String userAgent;
    public static String[] resolutions = new String[]{"412x732", "412x869", "412x824", "412x846", "412x847", "412x732", "360x740", "480x853", "360x740", "360x640", "375x667", "414x896", "412x869", "360x780", "393x851", "375x812", "360x720", "414x736", "480x853"};

    @Override
    public int getInnerWidth() {
        return this.width;
    }

    @Override
    public int getInnerHeight() {
        return this.height;
    }

    @Override
    public String getProductSub() {
        return "20030107";
    }

    @Override
    public String getAppCodeName() {
        return "Mozilla";
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public String getProduct() {
        return "Gecko";
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public int getAvailWidth() {
        return this.width;
    }

    @Override
    public String getAppName() {
        return "Netscape";
    }

    @Override
    public String getPlatform() {
        return this.platform;
    }

    public Devices$GeneratedDevice() {
        String[] stringArray = resolutions[ThreadLocalRandom.current().nextInt(resolutions.length)].split("x");
        this.width = Integer.parseInt(stringArray[0]);
        this.height = Integer.parseInt(stringArray[1]);
        this.userAgent = Devices$GeneratedDevice.getRandomUA() + " PerimeterX Android SDK/" + "v1.13.2".substring(1);
        this.platform = "Linux armv8l";
    }

    @Override
    public String getUserAgent() {
        return this.userAgent;
    }

    @Override
    public String getAppVersion() {
        try {
            String string = this.getUserAgent().split("/")[0].concat("/");
            return this.getUserAgent().replace(string, "");
        }
        catch (Throwable throwable) {
            return this.getUserAgent().replace("Mozilla/", "");
        }
    }

    public static String getRandomUA() {
        return Devices.userAgents[ThreadLocalRandom.current().nextInt(Devices.userAgents.length)];
    }

    @Override
    public int getAvailHeight() {
        return this.height;
    }
}

