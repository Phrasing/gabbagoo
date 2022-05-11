/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.task.antibot.impl.px.payload.token.Battery
 *  io.trickle.task.antibot.impl.px.payload.token.Battery$ChargingStatus
 */
package io.trickle.task.antibot.impl.px.payload.token;

import io.trickle.task.antibot.impl.px.payload.token.Battery;

public class Battery$ChargingStatus$1
extends Battery.ChargingStatus {
    public static float VOLT_CHARGE_PER_MIN;
    public static float MISS_CHARGE_RATIO;

    public float changeVector() {
        return Battery.jitter((float)Float.intBitsToFloat(1009339684), (float)Float.intBitsToFloat(957937084));
    }

    public float calculate(float f, int n, float f2, double d) {
        return (float)d + f2 * f + Float.intBitsToFloat(933443473) * (float)n * f;
    }

    public Battery$ChargingStatus$1() {
        super(string, n);
    }

    static {
        MISS_CHARGE_RATIO = Float.intBitsToFloat(933443473);
        VOLT_CHARGE_PER_MIN = Float.intBitsToFloat(1009339684);
    }
}
