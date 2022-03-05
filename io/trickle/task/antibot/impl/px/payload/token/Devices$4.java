/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.antibot.impl.px.payload.token;

import io.trickle.task.antibot.impl.px.payload.token.Devices;
import io.trickle.task.antibot.impl.px.payload.token.Devices$Device;
import io.trickle.task.antibot.impl.px.payload.token.Devices$DeviceImpl;

public class Devices$4
extends Devices {
    @Override
    public Devices$Device get() {
        Devices$DeviceImpl devices$DeviceImpl = new Devices$DeviceImpl(720, 1356, "4.14.133", "G50 Plus", "BLU", 29);
        devices$DeviceImpl.disableGyroscope();
        devices$DeviceImpl.disableEthernet();
        devices$DeviceImpl.disableNFC();
        return devices$DeviceImpl;
    }
}

