/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.task.antibot.impl.px.payload.token.Battery
 */
package io.trickle.task.antibot.impl.px.payload.token;

import io.trickle.task.antibot.impl.px.payload.token.Battery;

public interface Devices$Device {
    public String getCarrier();

    public boolean isNfc();

    public boolean isGps();

    public String getConnectionType();

    public boolean isTouchscreen();

    public String getCellular();

    public int getHeight();

    public int getApiLevel();

    public String getDeviceName();

    public boolean isWifi();

    public boolean isGyroscope();

    public String getOperatingSystem();

    public Battery getBattery();

    public boolean isAccelerometer();

    public String getBrand();

    public int getWidth();

    public boolean isEthernet();
}
