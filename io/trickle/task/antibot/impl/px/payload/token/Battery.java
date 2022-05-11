package io.trickle.task.antibot.impl.px.payload.token;

import java.util.concurrent.ThreadLocalRandom;

public class Battery {
   public long lastCheckTime;
   public String batteryType;
   public static float MIN_VOLT = Float.intBitsToFloat(1080386962);
   public double voltage;
   public Battery$ChargingStatus chargingStatus;
   public float VOLT_CONVERT_RATIO = jitter(Float.intBitsToFloat(1003713950), Float.intBitsToFloat(917327619));
   public String batteryHealth;
   public float voltageVector;
   public String chargingMethod;
   public int batteryPercent;
   public static float VOLT_TO_PERCENT_RATIO = Float.intBitsToFloat(1003713950);
   public double temperature;
   public static float MAX_VOLT = Float.intBitsToFloat(1082689629);

   public static double randTemp() {
      return ThreadLocalRandom.current().nextDouble(Double.longBitsToDouble(4626041242239631360L), Double.longBitsToDouble(4629841154425225216L));
   }

   public Battery(String var1, Battery$ChargingStatus var2, String var3) {
      this.batteryHealth = var1;
      this.chargingStatus = var2;
      this.chargingMethod = this.chargingStatus.chargingMethod();
      this.batteryType = var3;
      this.temperature = randTemp();
      this.voltage = (double)randVoltage();
      this.voltageVector = this.chargingStatus.changeVector();
      this.lastCheckTime = System.currentTimeMillis();
      this.recalculate();
   }

   public void recalculate() {
      long var1 = System.currentTimeMillis();
      float var3 = (float)(var1 - this.lastCheckTime) / Float.intBitsToFloat(1148846080) / Float.intBitsToFloat(1114636288);
      this.lastCheckTime = var1;
      this.voltage = (double)this.chargingStatus.calculate(var3, this.batteryPercent, this.voltageVector, this.voltage);
      this.batteryPercent = Math.min((int)((this.voltage - Double.longBitsToDouble(4615253599725813760L)) / (double)this.VOLT_CONVERT_RATIO), 100);
      if (this.chargingStatus.equals(Battery$ChargingStatus.CHARGING) && this.batteryPercent >= 93 + ThreadLocalRandom.current().nextInt(-3, 5)) {
         this.chargingStatus = Battery$ChargingStatus.DISCHARGING;
         this.updateValuesAfterStatusChange();
      } else if (this.chargingStatus.equals(Battery$ChargingStatus.DISCHARGING) && this.batteryPercent <= 5 + ThreadLocalRandom.current().nextInt(-4, 8)) {
         this.chargingStatus = Battery$ChargingStatus.CHARGING;
         this.updateValuesAfterStatusChange();
      }

      if (ThreadLocalRandom.current().nextBoolean()) {
         if (this.temperature >= Double.longBitsToDouble(4629841154425225216L)) {
            this.temperature -= ThreadLocalRandom.current().nextDouble(Double.longBitsToDouble(4617315517961601024L));
         } else if (this.temperature <= Double.longBitsToDouble(4626041242239631360L)) {
            this.temperature += ThreadLocalRandom.current().nextDouble(Double.longBitsToDouble(4617315517961601024L));
         } else {
            this.temperature += ThreadLocalRandom.current().nextDouble(Double.longBitsToDouble(-4616189618054758400L), Double.longBitsToDouble(4607182418800017408L));
         }
      }

   }

   public String getBatteryHealth() {
      return this.batteryHealth;
   }

   public static double roundTemperature(double var0) {
      double var2 = Math.pow(Double.longBitsToDouble(4621819117588971520L), Double.longBitsToDouble(4607182418800017408L));
      return (double)Math.round(var0 * var2) / var2;
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

   public static double roundVoltage(double var0) {
      double var2 = Math.pow(Double.longBitsToDouble(4621819117588971520L), Double.longBitsToDouble(4613937818241073152L));
      return (double)Math.round(var0 * var2) / var2;
   }

   public static Battery get() {
      Battery$ChargingStatus var0 = Battery$ChargingStatus.get();
      Battery$BatteryHealth var1 = Battery$BatteryHealth.get();
      return new Battery(var1.toString(), var0, ThreadLocalRandom.current().nextBoolean() ? "Li-ion" : "Unknown");
   }

   public int getBatteryPercent() {
      return this.batteryPercent;
   }

   public static float jitter(float var0, float var1) {
      float var2 = var1 * ThreadLocalRandom.current().nextFloat();
      return ThreadLocalRandom.current().nextBoolean() ? var0 + var2 : var0 - var2;
   }

   public double getVoltage() {
      return this.voltage;
   }

   public String getChargingMethod() {
      return this.chargingMethod;
   }
}
