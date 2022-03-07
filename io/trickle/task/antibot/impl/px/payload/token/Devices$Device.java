/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.antibot.impl.px.payload.token;

import io.trickle.task.antibot.impl.px.payload.token.Battery;

public interface Devices$Device {
    public boolean isGyroscope();

    public int getApiLevel();

    public String getConnectionType();

    public String getOperatingSystem();

    public Battery getBattery();

    public String getDeviceName();

    public String getBrand();

    public boolean isAccelerometer();

    public int getHeight();

    public int getWidth();

    public boolean isNfc();

    public boolean isWifi();

    public boolean isTouchscreen();

    public String getCarrier();

    public boolean isEthernet();

    public String getCellular();

    public boolean isGps();
}

