/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.antibot.impl.px.payload.captcha;

import io.trickle.task.antibot.impl.px.payload.captcha.Devices;
import io.trickle.task.antibot.impl.px.payload.captcha.Devices$Device;
import io.trickle.task.antibot.impl.px.payload.captcha.Devices$DeviceImpl;

public class Devices$2
extends Devices {
    @Override
    public Devices$Device get() {
        return new Devices$DeviceImpl(1440, 2560, 1400, 2560, 1298, 3440, "Win32", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.128 Safari/537.36", "Gecko", "20030107", "5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.128 Safari/537.36", "Netscape", "Mozilla");
    }
}

