/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.antibot.impl.px.payload.token;

import io.trickle.task.antibot.impl.px.payload.token.Battery;
import io.trickle.task.antibot.impl.px.payload.token.Battery$ChargingStatus;

public class Battery$ChargingStatus$1
extends Battery$ChargingStatus {
    public static float VOLT_CHARGE_PER_MIN = Float.intBitsToFloat(1009339684);
    public static float MISS_CHARGE_RATIO = Float.intBitsToFloat(933443473);

    @Override
    public float changeVector() {
        return Battery.jitter(Float.intBitsToFloat(1009339684), Float.intBitsToFloat(957937084));
    }

    @Override
    public float calculate(float f, int n, float f2, double d) {
        return (float)d + f2 * f + Float.intBitsToFloat(933443473) * (float)n * f;
    }
}

