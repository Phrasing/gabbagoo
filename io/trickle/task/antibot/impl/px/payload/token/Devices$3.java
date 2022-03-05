/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.antibot.impl.px.payload.token;

import io.trickle.task.antibot.impl.px.payload.token.Devices;
import io.trickle.task.antibot.impl.px.payload.token.Devices$Device;
import io.trickle.task.antibot.impl.px.payload.token.Devices$DeviceImpl;

public class Devices$3
extends Devices {
    @Override
    public Devices$Device get() {
        Devices$DeviceImpl devices$DeviceImpl = new Devices$DeviceImpl(1080, 2094, "4.4.153-17214672", "SM-G955U", "samsung", 28);
        devices$DeviceImpl.disableEthernet();
        return devices$DeviceImpl;
    }
}

