package io.trickle.task.antibot.impl.px.payload.token;

public enum Devices$13 {
   public Devices$Device get() {
      Devices$DeviceImpl var1 = new Devices$DeviceImpl(1536, 2048, "3.10.84-14953299", "SM-T813", "samsung", 24);
      var1.disableEthernet();
      var1.disableNFC();
      return var1;
   }
}
