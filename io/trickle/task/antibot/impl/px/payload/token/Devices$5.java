/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.antibot.impl.px.payload.token;

import io.trickle.task.antibot.impl.px.payload.token.Devices;
import io.trickle.task.antibot.impl.px.payload.token.Devices$Device;
import io.trickle.task.antibot.impl.px.payload.token.Devices$DeviceImpl;

public class Devices$5
extends Devices {
    @Override
    public Devices$Device get() {
        Devices$DeviceImpl devices$DeviceImpl = new Devices$DeviceImpl(720, 1517, "4.14.117-perf+", "Nokia 5.3", "HMD Global", 29);
        devices$DeviceImpl.disableGyroscope();
        devices$DeviceImpl.disableEthernet();
        return devices$DeviceImpl;
    }
}

