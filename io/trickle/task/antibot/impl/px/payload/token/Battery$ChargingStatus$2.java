/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.task.antibot.impl.px.payload.token.Battery
 *  io.trickle.task.antibot.impl.px.payload.token.Battery$ChargingStatus
 */
package io.trickle.task.antibot.impl.px.payload.token;

import io.trickle.task.antibot.impl.px.payload.token.Battery;

public class Battery$ChargingStatus$2
extends Battery.ChargingStatus {
    public static float VOLT_DROP_PER_MIN = Float.intBitsToFloat(983815946);
    public static float VOLTAGE_DEPLETION_FLUX = Float.intBitsToFloat(943080854);

    public Battery$ChargingStatus$2() {
        super(string, n);
    }

    public float changeVector() {
        return Battery.jitter((float)Float.intBitsToFloat(983815946), (float)Float.intBitsToFloat(968184054));
    }

    public float calculate(float f, int n, float f2, double d) {
        return (float)(d - (double)(f2 * f + Float.intBitsToFloat(943080854) * (float)n * f));
    }
}
