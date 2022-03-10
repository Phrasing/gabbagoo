/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.antibot.impl.px.payload.token;

import io.trickle.task.antibot.impl.px.payload.token.Battery$ChargingStatus$1;
import io.trickle.task.antibot.impl.px.payload.token.Battery$ChargingStatus$2;
import io.trickle.util.Utils;
import java.util.concurrent.ThreadLocalRandom;

public class Battery$ChargingStatus
extends Enum {
    public static /* enum */ Battery$ChargingStatus DISCHARGING;
    public static Battery$ChargingStatus[] $VALUES;
    public static /* enum */ Battery$ChargingStatus CHARGING;

    public static Battery$ChargingStatus[] values() {
        return (Battery$ChargingStatus[])$VALUES.clone();
    }

    public float calculate(float f, int n, float f2, double d) {
        return (float)d;
    }

    public float changeVector() {
        return Float.intBitsToFloat(1065353216);
    }

    public static Battery$ChargingStatus valueOf(String string) {
        return Enum.valueOf(Battery$ChargingStatus.class, string);
    }

    static {
        CHARGING = new Battery$ChargingStatus$1();
        DISCHARGING = new Battery$ChargingStatus$2();
        $VALUES = new Battery$ChargingStatus[]{CHARGING, DISCHARGING};
    }

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    public Battery$ChargingStatus() {
        void var2_-1;
        void var1_-1;
    }

    public static Battery$ChargingStatus get() {
        return (Battery$ChargingStatus)((Object)Utils.randomFrom((Object[])Battery$ChargingStatus.values()));
    }

    public String toString() {
        return this.name().toLowerCase();
    }

    public String chargingMethod() {
        if (!this.equals((Object)CHARGING)) return "None";
        if (!ThreadLocalRandom.current().nextBoolean()) return "USB";
        return "AC";
    }
}

