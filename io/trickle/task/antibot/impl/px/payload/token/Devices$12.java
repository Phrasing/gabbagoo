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

public class Devices$12
extends Devices {
    public Devices.Device get() {
        Devices.DeviceImpl deviceImpl = new Devices.DeviceImpl(1080, 2042, "4.14.117-19529858", "SM-G973U", "samsung", 29);
        deviceImpl.disableEthernet();
        return deviceImpl;
    }

    public Devices$12() {
        super(string, n);
    }
}
