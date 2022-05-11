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

public class Devices$14
extends Devices {
    public Devices.Device get() {
        Devices.DeviceImpl deviceImpl = new Devices.DeviceImpl(1080, 2160, "4.14.191-gde9d7ec53556-ab6960306", "Pixel 4a", "Google", 30);
        deviceImpl.disableEthernet();
        return deviceImpl;
    }

    public Devices$14() {
        super(string, n);
    }
}
