/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.antibot.impl.px.payload.token;

import io.trickle.task.antibot.impl.px.payload.token.Devices;
import io.trickle.task.antibot.impl.px.payload.token.Devices$Device;
import io.trickle.task.antibot.impl.px.payload.token.Devices$DeviceImpl;

public class Devices$12
extends Devices {
    @Override
    public Devices$Device get() {
        Devices$DeviceImpl devices$DeviceImpl = new Devices$DeviceImpl(1080, 2042, "4.14.117-19529858", "SM-G973U", "samsung", 29);
        devices$DeviceImpl.disableEthernet();
        return devices$DeviceImpl;
    }
}

