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

public class Devices$17
extends Devices {
    public Devices$17() {
        super(string, n);
    }

    public Devices.Device get() {
        Devices.DeviceImpl deviceImpl = new Devices.DeviceImpl(1080, 2400, "4.14.190-perf-20962855-abA725FXXU1AUB4", "SM-A725F", "samsung", 30);
        deviceImpl.disableEthernet();
        return deviceImpl;
    }
}
