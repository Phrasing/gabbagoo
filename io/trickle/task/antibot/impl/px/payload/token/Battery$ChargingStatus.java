/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.task.antibot.impl.px.payload.token.Battery$ChargingStatus$1
 *  io.trickle.task.antibot.impl.px.payload.token.Battery$ChargingStatus$2
 *  io.trickle.util.Utils
 */
package io.trickle.task.antibot.impl.px.payload.token;

import io.trickle.task.antibot.impl.px.payload.token.Battery$ChargingStatus;
import io.trickle.util.Utils;
import java.util.concurrent.ThreadLocalRandom;

public class Battery$ChargingStatus
extends Enum {
    public static Battery$ChargingStatus[] $VALUES;
    public static /* enum */ Battery$ChargingStatus CHARGING;
    public static /* enum */ Battery$ChargingStatus DISCHARGING;

    public float calculate(float f, int n, float f2, double d) {
        return (float)d;
    }

    public String chargingMethod() {
        if (!this.equals((Object)CHARGING)) return "None";
        return ThreadLocalRandom.current().nextBoolean() ? "AC" : "USB";
    }

    public static Battery$ChargingStatus[] values() {
        return (Battery$ChargingStatus[])$VALUES.clone();
    }

    static {
        CHARGING = new 1("CHARGING", 0);
        DISCHARGING = new 2("DISCHARGING", 1);
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

    public static Battery$ChargingStatus valueOf(String string) {
        return Enum.valueOf(Battery$ChargingStatus.class, string);
    }

    public static Battery$ChargingStatus get() {
        return (Battery$ChargingStatus)((Object)Utils.randomFrom((Object[])Battery$ChargingStatus.values()));
    }

    public String toString() {
        return this.name().toLowerCase();
    }

    public float changeVector() {
        return Float.intBitsToFloat(1065353216);
    }
}
