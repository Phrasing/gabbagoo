package io.trickle.task.antibot.impl.px.payload.token;

public enum Battery$BatteryHealth {
   public static Battery$BatteryHealth[] $VALUES = new Battery$BatteryHealth[]{GOOD};
   GOOD;

   public static Battery$BatteryHealth get() {
      return GOOD;
   }

   public String toString() {
      return this.name().toLowerCase();
   }
}
