/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.antibot.impl.px.payload.token;

import io.trickle.task.antibot.impl.px.payload.token.Battery;

public interface Devices$Device {
    public Battery getBattery();

    public String getDeviceName();

    public boolean isEthernet();

    public String getCarrier();

    public String getConnectionType();

    public String getBrand();

    public int getApiLevel();

    public String getCellular();

    public boolean isAccelerometer();

    public boolean isWifi();

    public int getWidth();

    public boolean isGps();

    public int getHeight();

    public boolean isGyroscope();

    public boolean isTouchscreen();

    public boolean isNfc();

    public String getOperatingSystem();
}

