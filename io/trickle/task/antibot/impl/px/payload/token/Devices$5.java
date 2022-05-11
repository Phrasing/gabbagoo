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

public class Devices$5
extends Devices {
    public Devices.Device get() {
        Devices.DeviceImpl deviceImpl = new Devices.DeviceImpl(720, 1517, "4.14.117-perf+", "Nokia 5.3", "HMD Global", 29);
        deviceImpl.disableGyroscope();
        deviceImpl.disableEthernet();
        return deviceImpl;
    }

    public Devices$5() {
        super(string, n);
    }
}
