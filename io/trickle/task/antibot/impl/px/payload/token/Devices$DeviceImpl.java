/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.task.antibot.impl.px.payload.token.Battery
 *  io.trickle.task.antibot.impl.px.payload.token.Devices$Device
 *  io.vertx.core.json.JsonObject
 */
package io.trickle.task.antibot.impl.px.payload.token;

import io.trickle.task.antibot.impl.px.payload.token.Battery;
import io.trickle.task.antibot.impl.px.payload.token.Devices;
import io.vertx.core.json.JsonObject;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Devices$DeviceImpl
implements Devices.Device {
    public boolean gyroscope = true;
    public static Pattern API_LEVEL_PATTERN = Pattern.compile("\\(([0-9]*)?\\)");
    public int apiLevel;
    public String brand;
    public String connectionType;
    public String[] carriers = new String[]{"T-Mobile", "Sprint", "AT&T"};
    public String deviceName;
    public boolean gps = true;
    public boolean touchscreen = true;
    public boolean wifi = true;
    public int width;
    public String carrier;
    public String operatingSystem;
    public boolean accelerometer = true;
    public int height;
    public Battery battery;
    public boolean nfc = true;
    public boolean ethernet = true;
    public String cellular;

    public String getCellular() {
        return this.cellular;
    }

    public String getBrand() {
        return this.brand;
    }

    public Devices$DeviceImpl(int n, int n2, String string, String string2, String string3, int n3) {
        this.width = n;
        this.height = n2;
        this.connectionType = this.randomConnectionType();
        this.operatingSystem = string;
        this.deviceName = string2;
        this.brand = string3;
        this.cellular = this.randomCellular();
        this.carrier = this.randomCarrier();
        this.apiLevel = n3;
        this.battery = Battery.get();
    }

    public int getApiLevel() {
        return this.apiLevel;
    }

    public void disableAccelerometer() {
        this.accelerometer = false;
    }

    public String randomCellular() {
        return "Unknown";
    }

    public int getHeight() {
        return this.height;
    }

    public String getOperatingSystem() {
        return this.operatingSystem;
    }

    public String randomCarrier() {
        if (!this.cellular.equals("4G")) return this.carriers[ThreadLocalRandom.current().nextInt(0, this.carriers.length)];
        return this.carriers[ThreadLocalRandom.current().nextInt(1, this.carriers.length)];
    }

    public boolean isGps() {
        return this.gps;
    }

    public boolean isEthernet() {
        return this.ethernet;
    }

    public void disableEthernet() {
        this.ethernet = false;
    }

    public boolean isAccelerometer() {
        return this.accelerometer;
    }

    public void disableGyroscope() {
        this.gyroscope = false;
    }

    public boolean isGyroscope() {
        return this.gyroscope;
    }

    public int getWidth() {
        return this.width;
    }

    public String getConnectionType() {
        return this.connectionType;
    }

    public String getCarrier() {
        return this.carrier;
    }

    public String randomConnectionType() {
        return "wifi";
    }

    public void disableNFC() {
        this.nfc = false;
    }

    public void disableWIFI() {
        this.wifi = false;
    }

    public boolean isNfc() {
        return this.nfc;
    }

    public Devices$DeviceImpl(JsonObject jsonObject) {
        String[] stringArray = jsonObject.getString("dis").split("x");
        this.width = Integer.parseInt(stringArray[1]);
        this.height = Integer.parseInt(stringArray[0]);
        this.connectionType = this.randomConnectionType();
        this.operatingSystem = jsonObject.getString("os");
        this.deviceName = jsonObject.getString("model");
        this.brand = jsonObject.getString("manu");
        this.cellular = this.randomCellular();
        this.carrier = this.randomCarrier();
        Matcher matcher = API_LEVEL_PATTERN.matcher(jsonObject.getString("api"));
        matcher.find();
        this.apiLevel = Integer.parseInt(matcher.group(1));
        this.battery = Battery.get();
        this.disableEthernet();
    }

    public void disableGPS() {
        this.gps = false;
    }

    public Battery getBattery() {
        return this.battery;
    }

    public boolean isWifi() {
        return this.wifi;
    }

    public boolean isTouchscreen() {
        return this.touchscreen;
    }

    public void disableTouchscreen() {
        this.touchscreen = false;
    }

    public String getDeviceName() {
        return this.deviceName;
    }
}
