package io.trickle.task.antibot.impl.px.payload.token;

public interface Devices$Device {
   String getCarrier();

   boolean isNfc();

   boolean isGps();

   String getConnectionType();

   boolean isTouchscreen();

   String getCellular();

   int getHeight();

   int getApiLevel();

   String getDeviceName();

   boolean isWifi();

   boolean isGyroscope();

   String getOperatingSystem();

   Battery getBattery();

   boolean isAccelerometer();

   String getBrand();

   int getWidth();

   boolean isEthernet();
}
