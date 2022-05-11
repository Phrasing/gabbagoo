package io.trickle.task.antibot.impl.akamai;

import io.vertx.core.json.JsonObject;
import java.util.concurrent.ThreadLocalRandom;

public enum Devices {
   CHROME_87_SAFARI,
   CHROME_89_MAC_PERSONAL,
   BRAVE_89_1,
   BRAVE_89_2,
   BRAVE_89_3,
   CHROME_89_Z3,
   CHROME_89_SAFARI;

   public static Devices[] $VALUES = new Devices[]{CHROME_89_MAC_PERSONAL, CHROME_89_Z3, CHROME_89_SAFARI, CHROME_87_SAFARI, BRAVE_89, BRAVE_89_1, BRAVE_89_2, BRAVE_89_3, BRAVE_89_4};
   BRAVE_89,
   BRAVE_89_4;

   public static Devices$Device random() {
      return values()[ThreadLocalRandom.current().nextInt(values().length)].get();
   }

   public Devices$Device get() {
      return null;
   }

   public static Devices$Device genFromJson(JsonObject var0) {
      try {
         Devices$DeviceImpl var1 = new Devices$DeviceImpl(var0.getInteger("availWidth"), var0.getInteger("availHeight"), var0.getInteger("width"), var0.getInteger("height"), var0.getInteger("outerWidth"), var0.getInteger("innerHeight"), var0.getInteger("innerWidth"), var0.getString("ua"), var0.getString("product"), var0.getJsonObject("nav").getString("productSub"), var0.getBoolean("brave"), var0.getInteger("activeXObject"), var0.getInteger("documentMode"), var0.getInteger("isChrome"), var0.getInteger("isOnline"), var0.getInteger("isOpera"), var0.getInteger("hasInstallTrigger"), var0.getInteger("hasHTMLElement"), var0.getInteger("hasRTCPeerConnection"), var0.getInteger("hasMozInnerScreen"), var0.getInteger("hasVibrate"), var0.getInteger("hasBattery"), var0.getInteger("hasForEach"), var0.getInteger("hasFileReader"), var0.getInteger("pluginsLength"), var0.getString("languages"), var0.getString("deviceOrientation"), var0.getString("deviceMotion"), var0.getString("touchEvent"), var0.getLong("navigatorFasSettings"), var0.getString("sed"), var0.getInteger("colorDepth"), var0.getInteger("pixelDepth"), var0.getBoolean("cookieEnabled"), var0.getBoolean("javaEnabled"), Integer.parseInt(var0.getValue("doNotTrack").toString()), var0.getString("canvasFP1"), var0.getString("canvasFP2"), var0.getString("pluginInfo"), var0.getBoolean("sessionStorage"), var0.getBoolean("localStorage"), var0.getBoolean("indexedDB"), var0.getInteger("timezoneOffset"), var0.getBoolean("webRTC"), var0.getJsonObject("windowPerms").toString());
         return var1;
      } catch (Throwable var2) {
         return null;
      }
   }
}
