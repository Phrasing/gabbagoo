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

public class Devices$16
extends Devices {
    public Devices.Device get() {
        Devices.DeviceImpl deviceImpl = new Devices.DeviceImpl(1080, 2160, "4.19.110-g9ceb3bf92e0a-ab6790968", "Pixel 5", "Google", 30);
        deviceImpl.disableEthernet();
        return deviceImpl;
    }

    public Devices$16() {
        super(string, n);
    }
}
