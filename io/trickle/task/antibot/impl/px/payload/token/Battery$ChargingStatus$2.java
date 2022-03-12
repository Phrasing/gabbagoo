/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.antibot.impl.px.payload.token;

import io.trickle.task.antibot.impl.px.payload.token.Battery;
import io.trickle.task.antibot.impl.px.payload.token.Battery$ChargingStatus;

public class Battery$ChargingStatus$2
extends Battery$ChargingStatus {
    public static float VOLTAGE_DEPLETION_FLUX;
    public static float VOLT_DROP_PER_MIN;

    static {
        VOLT_DROP_PER_MIN = Float.intBitsToFloat(983815946);
        VOLTAGE_DEPLETION_FLUX = Float.intBitsToFloat(943080854);
    }

    @Override
    public float changeVector() {
        return Battery.jitter(Float.intBitsToFloat(983815946), Float.intBitsToFloat(968184054));
    }

    @Override
    public float calculate(float f, int n, float f2, double d) {
        return (float)(d - (double)(f2 * f + Float.intBitsToFloat(943080854) * (float)n * f));
    }
}

