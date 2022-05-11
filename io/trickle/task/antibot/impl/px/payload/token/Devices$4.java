package io.trickle.task.antibot.impl.px.payload.token;

public enum Devices$4 {
   public Devices$Device get() {
      Devices$DeviceImpl var1 = new Devices$DeviceImpl(720, 1356, "4.14.133", "G50 Plus", "BLU", 29);
      var1.disableGyroscope();
      var1.disableEthernet();
      var1.disableNFC();
      return var1;
   }
}
