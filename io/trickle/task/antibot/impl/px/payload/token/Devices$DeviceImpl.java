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
    public String connectionType;
    public boolean nfc = true;
    public boolean gyroscope = true;
    public static Pattern API_LEVEL_PATTERN = Pattern.compile("\\(([0-9]*)?\\)");
    public String[] carriers = new String[]{"T-Mobile", "Sprint", "AT&T"};
    public String deviceName;
    public boolean accelerometer = true;
    public boolean ethernet = true;
    public int height;
    public boolean wifi = true;
    public String operatingSystem;
    public String brand;
    public int apiLevel;
    public int width;
    public Battery battery;
    public boolean gps = true;
    public String cellular;
    public boolean touchscreen = true;
    public String carrier;

    public String randomCarrier() {
        if (!this.cellular.equals("4G")) return this.carriers[ThreadLocalRandom.current().nextInt(0, this.carriers.length)];
        return this.carriers[ThreadLocalRandom.current().nextInt(1, this.carriers.length)];
    }

    @Override
    public String getOperatingSystem() {
        return this.operatingSystem;
    }

    @Override
    public String getBrand() {
        return this.brand;
    }

    public String randomConnectionType() {
        return "wifi";
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    public void disableGyroscope() {
        this.gyroscope = false;
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

    public void disableWIFI() {
        this.wifi = false;
    }

    @Override
    public String getCarrier() {
        return this.carrier;
    }

    public String randomCellular() {
        return "Unknown";
    }

    public void disableEthernet() {
        this.ethernet = false;
    }

    @Override
    public int getApiLevel() {
        return this.apiLevel;
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
    public String getConnectionType() {
        return this.connectionType;
    }

    public void disableNFC() {
        this.nfc = false;
    }

    public void disableTouchscreen() {
        this.touchscreen = false;
    }

    @Override
    public boolean isWifi() {
        return this.wifi;
    }

    @Override
    public String getCellular() {
        return this.cellular;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public boolean isNfc() {
        return this.nfc;
    }

    @Override
    public Battery getBattery() {
        return this.battery;
    }

    public void disableAccelerometer() {
        this.accelerometer = false;
    }

    @Override
    public String getDeviceName() {
        return this.deviceName;
    }

    @Override
    public boolean isAccelerometer() {
        return this.accelerometer;
    }

    @Override
    public boolean isTouchscreen() {
        return this.touchscreen;
    }

    public void disableGPS() {
        this.gps = false;
    }

    @Override
    public boolean isGps() {
        return this.gps;
    }

    @Override
    public boolean isGyroscope() {
        return this.gyroscope;
    }

    @Override
    public boolean isEthernet() {
        return this.ethernet;
    }
}

