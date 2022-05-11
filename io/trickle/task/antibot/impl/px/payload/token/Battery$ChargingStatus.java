package io.trickle.task.antibot.impl.px.payload.token;

import io.trickle.util.Utils;
import java.util.concurrent.ThreadLocalRandom;

public enum Battery$ChargingStatus {
   public static Battery$ChargingStatus[] $VALUES = new Battery$ChargingStatus[]{CHARGING, DISCHARGING};
   CHARGING,
   DISCHARGING;

   public float calculate(float var1, int var2, float var3, double var4) {
      return (float)var4;
   }

   public String chargingMethod() {
      if (this.equals(CHARGING)) {
         return ThreadLocalRandom.current().nextBoolean() ? "AC" : "USB";
      } else {
         return "None";
      }
   }

   public static Battery$ChargingStatus get() {
      return (Battery$ChargingStatus)Utils.randomFrom(values());
   }

   public String toString() {
      return this.name().toLowerCase();
   }

   public float changeVector() {
      return Float.intBitsToFloat(1065353216);
   }
}
