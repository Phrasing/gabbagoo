/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.antibot.impl.px.payload.token;

import io.trickle.task.antibot.impl.px.payload.token.Devices;
import io.trickle.task.antibot.impl.px.payload.token.Devices$Device;
import io.trickle.task.antibot.impl.px.payload.token.Devices$DeviceImpl;

public class Devices$17
extends Devices {
    @Override
    public Devices$Device get() {
        Devices$DeviceImpl devices$DeviceImpl = new Devices$DeviceImpl(1080, 2400, "4.14.190-perf-20962855-abA725FXXU1AUB4", "SM-A725F", "samsung", 30);
        devices$DeviceImpl.disableEthernet();
        return devices$DeviceImpl;
    }
}

