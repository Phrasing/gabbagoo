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

public class Devices$4
extends Devices {
    public Devices.Device get() {
        Devices.DeviceImpl deviceImpl = new Devices.DeviceImpl(720, 1356, "4.14.133", "G50 Plus", "BLU", 29);
        deviceImpl.disableGyroscope();
        deviceImpl.disableEthernet();
        deviceImpl.disableNFC();
        return deviceImpl;
    }

    public Devices$4() {
        super(string, n);
    }
}
