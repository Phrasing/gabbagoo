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

public class Devices$13
extends Devices {
    public Devices.Device get() {
        Devices.DeviceImpl deviceImpl = new Devices.DeviceImpl(1536, 2048, "3.10.84-14953299", "SM-T813", "samsung", 24);
        deviceImpl.disableEthernet();
        deviceImpl.disableNFC();
        return deviceImpl;
    }

    public Devices$13() {
        super(string, n);
    }
}
