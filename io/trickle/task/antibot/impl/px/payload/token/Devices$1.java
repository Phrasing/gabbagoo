/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.task.antibot.impl.px.payload.token.Devices
 *  io.trickle.task.antibot.impl.px.payload.token.Devices$Device
 *  io.trickle.task.antibot.impl.px.payload.token.Devices$DeviceImpl
 */
package io.trickle.task.antibot.impl.px.payload.token;

import io.trickle.task.antibot.impl.px.payload.token.Devices;

public class Devices$1
extends Devices {
    public Devices$1() {
        super(string, n);
    }

    public Devices.Device get() {
        Devices.DeviceImpl deviceImpl = new Devices.DeviceImpl(1080, 2322, "4.19.113-20642218", "SM-G981U1", "samsung", 30);
        deviceImpl.disableEthernet();
        return deviceImpl;
    }
}
