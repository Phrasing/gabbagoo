package io.trickle.task.antibot.impl.px.payload.token;

public enum Battery$ChargingStatus$2 {
   public static float VOLT_DROP_PER_MIN = Float.intBitsToFloat(983815946);
   public static float VOLTAGE_DEPLETION_FLUX = Float.intBitsToFloat(943080854);

   public float changeVector() {
      return Battery.jitter(Float.intBitsToFloat(983815946), Float.intBitsToFloat(968184054));
   }

   public float calculate(float var1, int var2, float var3, double var4) {
      return (float)(var4 - (double)(var3 * var1 + Float.intBitsToFloat(943080854) * (float)var2 * var1));
   }
}
