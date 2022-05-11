/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.task.antibot.impl.px.payload.token.Battery$BatteryHealth
 *  io.trickle.task.antibot.impl.px.payload.token.Battery$ChargingStatus
 */
package io.trickle.task.antibot.impl.px.payload.token;

import io.trickle.task.antibot.impl.px.payload.token.Battery;
import java.util.concurrent.ThreadLocalRandom;

/*
 * Exception performing whole class analysis ignored.
 */
public class Battery {
    public long lastCheckTime;
    public String batteryType;
    public static float MIN_VOLT;
    public double voltage;
    public ChargingStatus chargingStatus;
    public float VOLT_CONVERT_RATIO = Battery.jitter(Float.intBitsToFloat(1003713950), Float.intBitsToFloat(917327619));
    public String batteryHealth;
    public float voltageVector;
    public String chargingMethod;
    public int batteryPercent;
    public static float VOLT_TO_PERCENT_RATIO;
    public double temperature;
    public static float MAX_VOLT;

    static {
        MAX_VOLT = Float.intBitsToFloat(1082689629);
        VOLT_TO_PERCENT_RATIO = Float.intBitsToFloat(1003713950);
        MIN_VOLT = Float.intBitsToFloat(1080386962);
    }

    public static double randTemp() {
        return ThreadLocalRandom.current().nextDouble(Double.longBitsToDouble(0x4033000000000000L), Double.longBitsToDouble(0x4040800000000000L));
    }

    public Battery(String string, ChargingStatus chargingStatus, String string2) {
        this.batteryHealth = string;
        this.chargingStatus = chargingStatus;
        this.chargingMethod = this.chargingStatus.chargingMethod();
        this.batteryType = string2;
        this.temperature = Battery.randTemp();
        this.voltage = Battery.randVoltage();
        this.voltageVector = this.chargingStatus.changeVector();
        this.lastCheckTime = System.currentTimeMillis();
        this.recalculate();
    }

    public void recalculate() {
        long l = System.currentTimeMillis();
        float f = (float)(l - this.lastCheckTime) / Float.intBitsToFloat(1148846080) / Float.intBitsToFloat(1114636288);
        this.lastCheckTime = l;
        this.voltage = this.chargingStatus.calculate(f, this.batteryPercent, this.voltageVector, this.voltage);
        this.batteryPercent = Math.min((int)((this.voltage - Double.longBitsToDouble(4615253599725813760L)) / (double)this.VOLT_CONVERT_RATIO), 100);
        if (this.chargingStatus.equals((Object)ChargingStatus.CHARGING) && this.batteryPercent >= 93 + ThreadLocalRandom.current().nextInt(-3, 5)) {
            this.chargingStatus = ChargingStatus.DISCHARGING;
            this.updateValuesAfterStatusChange();
        } else if (this.chargingStatus.equals((Object)ChargingStatus.DISCHARGING) && this.batteryPercent <= 5 + ThreadLocalRandom.current().nextInt(-4, 8)) {
            this.chargingStatus = ChargingStatus.CHARGING;
            this.updateValuesAfterStatusChange();
        }
        if (!ThreadLocalRandom.current().nextBoolean()) return;
        this.temperature = this.temperature >= Double.longBitsToDouble(0x4040800000000000L) ? (this.temperature -= ThreadLocalRandom.current().nextDouble(Double.longBitsToDouble(0x4014000000000000L))) : (this.temperature <= Double.longBitsToDouble(0x4033000000000000L) ? (this.temperature += ThreadLocalRandom.current().nextDouble(Double.longBitsToDouble(0x4014000000000000L))) : (this.temperature += ThreadLocalRandom.current().nextDouble(Double.longBitsToDouble(-4616189618054758400L), Double.longBitsToDouble(0x3FF0000000000000L))));
    }

    public String getBatteryHealth() {
        return this.batteryHealth;
    }

    public static double roundTemperature(double d) {
        double d2 = Math.pow(Double.longBitsToDouble(0x4024000000000000L), Double.longBitsToDouble(0x3FF0000000000000L));
        return (double)Math.round(d * d2) / d2;
    }

    public static float randVoltage() {
        return (float)ThreadLocalRandom.current().nextDouble(Double.longBitsToDouble(4615253599725813760L), Double.longBitsToDouble(4616489834658136064L));
    }

    public String getBatteryType() {
        return this.batteryType;
    }

    public double getTemperature() {
        return this.temperature;
    }

    public void updateValuesAfterStatusChange() {
        this.voltageVector = this.chargingStatus.changeVector();
        this.chargingMethod = this.chargingStatus.chargingMethod();
    }

    public String getChargingStatus() {
        return this.chargingStatus.toString();
    }

    public String toString() {
        return "batteryPercent=" + this.batteryPercent + "%, voltage=" + this.voltage + ", method=" + this.chargingStatus;
    }

    public static double roundVoltage(double d) {
        double d2 = Math.pow(Double.longBitsToDouble(0x4024000000000000L), Double.longBitsToDouble(0x4008000000000000L));
        return (double)Math.round(d * d2) / d2;
    }

    public static Battery get() {
        ChargingStatus chargingStatus = ChargingStatus.get();
        BatteryHealth batteryHealth = BatteryHealth.get();
        return new Battery(batteryHealth.toString(), chargingStatus, ThreadLocalRandom.current().nextBoolean() ? "Li-ion" : "Unknown");
    }

    public int getBatteryPercent() {
        return this.batteryPercent;
    }

    public static float jitter(float f, float f2) {
        float f3 = f2 * ThreadLocalRandom.current().nextFloat();
        if (!ThreadLocalRandom.current().nextBoolean()) return f - f3;
        return f + f3;
    }

    public double getVoltage() {
        return this.voltage;
    }

    public String getChargingMethod() {
        return this.chargingMethod;
    }
}
