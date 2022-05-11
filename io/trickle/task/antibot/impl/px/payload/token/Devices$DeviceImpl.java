package io.trickle.task.antibot.impl.px.payload.token;

import io.vertx.core.json.JsonObject;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Devices$DeviceImpl implements Devices$Device {
   public boolean gyroscope = true;
   public static Pattern API_LEVEL_PATTERN = Pattern.compile("\\(([0-9]*)?\\)");
   public int apiLevel;
   public String brand;
   public String connectionType;
   public String[] carriers = new String[]{"T-Mobile", "Sprint", "AT&T"};
   public String deviceName;
   public boolean gps = true;
   public boolean touchscreen = true;
   public boolean wifi = true;
   public int width;
   public String carrier;
   public String operatingSystem;
   public boolean accelerometer = true;
   public int height;
   public Battery battery;
   public boolean nfc = true;
   public boolean ethernet = true;
   public String cellular;

   public String getCellular() {
      return this.cellular;
   }

   public String getBrand() {
      return this.brand;
   }

   public Devices$DeviceImpl(int var1, int var2, String var3, String var4, String var5, int var6) {
      this.width = var1;
      this.height = var2;
      this.connectionType = this.randomConnectionType();
      this.operatingSystem = var3;
      this.deviceName = var4;
      this.brand = var5;
      this.cellular = this.randomCellular();
      this.carrier = this.randomCarrier();
      this.apiLevel = var6;
      this.battery = Battery.get();
   }

   public int getApiLevel() {
      return this.apiLevel;
   }

   public void disableAccelerometer() {
      this.accelerometer = false;
   }

   public String randomCellular() {
      return "Unknown";
   }

   public int getHeight() {
      return this.height;
   }

   public String getOperatingSystem() {
      return this.operatingSystem;
   }

   public String randomCarrier() {
      return this.cellular.equals("4G") ? this.carriers[ThreadLocalRandom.current().nextInt(1, this.carriers.length)] : this.carriers[ThreadLocalRandom.current().nextInt(0, this.carriers.length)];
   }

   public boolean isGps() {
      return this.gps;
   }

   public boolean isEthernet() {
      return this.ethernet;
   }

   public void disableEthernet() {
      this.ethernet = false;
   }

   public boolean isAccelerometer() {
      return this.accelerometer;
   }

   public void disableGyroscope() {
      this.gyroscope = false;
   }

   public boolean isGyroscope() {
      return this.gyroscope;
   }

   public int getWidth() {
      return this.width;
   }

   public String getConnectionType() {
      return this.connectionType;
   }

   public String getCarrier() {
      return this.carrier;
   }

   public String randomConnectionType() {
      return "wifi";
   }

   public void disableNFC() {
      this.nfc = false;
   }

   public void disableWIFI() {
      this.wifi = false;
   }

   public boolean isNfc() {
      return this.nfc;
   }

   public Devices$DeviceImpl(JsonObject var1) {
      String[] var2 = var1.getString("dis").split("x");
      this.width = Integer.parseInt(var2[1]);
      this.height = Integer.parseInt(var2[0]);
      this.connectionType = this.randomConnectionType();
      this.operatingSystem = var1.getString("os");
      this.deviceName = var1.getString("model");
      this.brand = var1.getString("manu");
      this.cellular = this.randomCellular();
      this.carrier = this.randomCarrier();
      Matcher var3 = API_LEVEL_PATTERN.matcher(var1.getString("api"));
      var3.find();
      this.apiLevel = Integer.parseInt(var3.group(1));
      this.battery = Battery.get();
      this.disableEthernet();
   }

   public void disableGPS() {
      this.gps = false;
   }

   public Battery getBattery() {
      return this.battery;
   }

   public boolean isWifi() {
      return this.wifi;
   }

   public boolean isTouchscreen() {
      return this.touchscreen;
   }

   public void disableTouchscreen() {
      this.touchscreen = false;
   }

   public String getDeviceName() {
      return this.deviceName;
   }
}
