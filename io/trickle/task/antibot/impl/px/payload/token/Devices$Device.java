/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.antibot.impl.px.payload.token;

import io.trickle.task.antibot.impl.px.payload.token.Battery;

public interface Devices$Device {
    public int getHeight();

    public Battery getBattery();

    public boolean isGyroscope();

    public boolean isAccelerometer();

    public boolean isTouchscreen();

    public String getCellular();

    public boolean isWifi();

    public int getApiLevel();

    public String getBrand();

    public boolean isEthernet();

    public boolean isNfc();

    public int getWidth();

    public String getDeviceName();

    public String getOperatingSystem();

    public String getConnectionType();

    public String getCarrier();

    public boolean isGps();
}

