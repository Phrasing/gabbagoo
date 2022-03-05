/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.antibot.impl.px.payload.token;

import io.trickle.task.antibot.impl.px.payload.token.Devices;
import io.trickle.task.antibot.impl.px.payload.token.Devices$Device;
import io.trickle.task.antibot.impl.px.payload.token.Devices$DeviceImpl;

public class Devices$13
extends Devices {
    @Override
    public Devices$Device get() {
        Devices$DeviceImpl devices$DeviceImpl = new Devices$DeviceImpl(1536, 2048, "3.10.84-14953299", "SM-T813", "samsung", 24);
        devices$DeviceImpl.disableEthernet();
        devices$DeviceImpl.disableNFC();
        return devices$DeviceImpl;
    }
}

