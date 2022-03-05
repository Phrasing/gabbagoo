/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.json.JsonObject
 */
package io.trickle.task.antibot.impl.px.payload.token;

import io.trickle.task.antibot.impl.px.payload.token.Battery;
import io.trickle.task.antibot.impl.px.payload.token.Devices$Device;
import io.vertx.core.json.JsonObject;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Devices$DeviceImpl
implements Devices$Device {
    public String cellular;
    public String deviceName;
    public String brand;
    public boolean wifi = true;
    public String connectionType;
    public boolean accelerometer = true;
    public boolean ethernet = true;
    public int width;
    public String operatingSystem;
    public boolean gps = true;
    public String[] carriers = new String[]{"T-Mobile", "Sprint", "AT&T"};
    public static Pattern API_LEVEL_PATTERN = Pattern.compile("\\(([0-9]*)?\\)");
    public Battery battery;
    public int apiLevel;
    public boolean touchscreen = true;
    public boolean nfc = true;
    public int height;
    public boolean gyroscope = true;
    public String carrier;

    public String randomConnectionType() {
        return "wifi";
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public boolean isTouchscreen() {
        return this.touchscreen;
    }

    @Override
    public String getCarrier() {
        return this.carrier;
    }

    @Override
    public boolean isWifi() {
        return this.wifi;
    }

    public String randomCarrier() {
        if (!this.cellular.equals("4G")) return this.carriers[ThreadLocalRandom.current().nextInt(0, this.carriers.length)];
        return this.carriers[ThreadLocalRandom.current().nextInt(1, this.carriers.length)];
    }

    public void disableNFC() {
        this.nfc = false;
    }

    public void disableWIFI() {
        this.wifi = false;
    }

    @Override
    public String getCellular() {
        return this.cellular;
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

    @Override
    public String getOperatingSystem() {
        return this.operatingSystem;
    }

    public void disableTouchscreen() {
        this.touchscreen = false;
    }

    @Override
    public String getConnectionType() {
        return this.connectionType;
    }

    @Override
    public boolean isGps() {
        return this.gps;
    }

    @Override
    public boolean isAccelerometer() {
        return this.accelerometer;
    }

    public void disableAccelerometer() {
        this.accelerometer = false;
    }

    @Override
    public String getBrand() {
        return this.brand;
    }

    public void disableGyroscope() {
        this.gyroscope = false;
    }

    @Override
    public int getApiLevel() {
        return this.apiLevel;
    }

    @Override
    public boolean isNfc() {
        return this.nfc;
    }

    @Override
    public boolean isGyroscope() {
        return this.gyroscope;
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

    @Override
    public String getDeviceName() {
        return this.deviceName;
    }

    public String randomCellular() {
        return "Unknown";
    }

    @Override
    public boolean isEthernet() {
        return this.ethernet;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public Battery getBattery() {
        return this.battery;
    }

    public void disableEthernet() {
        this.ethernet = false;
    }

    public void disableGPS() {
        this.gps = false;
    }
}

