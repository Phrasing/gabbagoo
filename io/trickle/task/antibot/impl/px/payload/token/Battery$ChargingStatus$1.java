package io.trickle.task.antibot.impl.px.payload.token;

public enum Battery$ChargingStatus$1 {
   public static float VOLT_CHARGE_PER_MIN = Float.intBitsToFloat(1009339684);
   public static float MISS_CHARGE_RATIO = Float.intBitsToFloat(933443473);

   public float changeVector() {
      return Battery.jitter(Float.intBitsToFloat(1009339684), Float.intBitsToFloat(957937084));
   }

   public float calculate(float var1, int var2, float var3, double var4) {
      return (float)var4 + var3 * var1 + Float.intBitsToFloat(933443473) * (float)var2 * var1;
   }
}
