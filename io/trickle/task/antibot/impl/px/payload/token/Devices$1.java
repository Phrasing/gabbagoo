/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.antibot.impl.px.payload.token;

import io.trickle.task.antibot.impl.px.payload.token.Devices;
import io.trickle.task.antibot.impl.px.payload.token.Devices$Device;
import io.trickle.task.antibot.impl.px.payload.token.Devices$DeviceImpl;

public class Devices$1
extends Devices {
    @Override
    public Devices$Device get() {
        Devices$DeviceImpl devices$DeviceImpl = new Devices$DeviceImpl(1080, 2322, "4.19.113-20642218", "SM-G981U1", "samsung", 30);
        devices$DeviceImpl.disableEthernet();
        return devices$DeviceImpl;
    }
}

