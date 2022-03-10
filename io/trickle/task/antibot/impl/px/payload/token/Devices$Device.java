/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.antibot.impl.px.payload.token;

import io.trickle.task.antibot.impl.px.payload.token.Battery;

public interface Devices$Device {
    public boolean isNfc();

    public String getDeviceName();

    public boolean isGps();

    public boolean isGyroscope();

    public boolean isAccelerometer();

    public boolean isWifi();

    public int getApiLevel();

    public String getConnectionType();

    public int getWidth();

    public String getCellular();

    public String getOperatingSystem();

    public String getBrand();

    public boolean isEthernet();

    public int getHeight();

    public Battery getBattery();

    public boolean isTouchscreen();

    public String getCarrier();
}

