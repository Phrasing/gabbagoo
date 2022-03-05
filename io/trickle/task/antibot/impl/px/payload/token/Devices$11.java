/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.antibot.impl.px.payload.token;

import io.trickle.task.antibot.impl.px.payload.token.Devices;
import io.trickle.task.antibot.impl.px.payload.token.Devices$Device;
import io.trickle.task.antibot.impl.px.payload.token.Devices$DeviceImpl;

public class Devices$11
extends Devices {
    @Override
    public Devices$Device get() {
        Devices$DeviceImpl devices$DeviceImpl = new Devices$DeviceImpl(1080, 2190, "5.4.61-qgki-20700993-abG998U1UEU1AUAG", "SM-G998U1", "samsung", 30);
        devices$DeviceImpl.disableEthernet();
        return devices$DeviceImpl;
    }
}

