package io.trickle.task.antibot.impl.akamai.pixel;

import io.vertx.core.json.JsonObject;

public enum Devices {
   public static Devices[] $VALUES = new Devices[]{BRAVE_89_1};
   BRAVE_89_1;

   public static Devices$Device genFromJson(JsonObject var0) {
      return new Devices$DeviceImpl(var0.getBoolean("ap"), var0.getJsonObject("bt", (JsonObject)null), var0.getString("fonts"), var0.getString("fh"), (JsonObject)null, var0.getString("bp"), var0.getJsonObject("sr"), var0.getJsonObject("dp"), var0.getString("lt"), var0.getString("ps"), var0.getString("cv"), var0.getBoolean("fp"), var0.getBoolean("sp"), var0.getString("br"), var0.getBoolean("ieps"), var0.getBoolean("av"), var0.getInteger("b"), var0.getInteger("c"), "", Double.parseDouble(var0.getString("jsv")), var0.getJsonObject("nav"), var0.getJsonObject("crc"), var0.getString("nap"), var0.getBoolean("fc"));
   }

   public Devices$Device get() {
      return null;
   }
}
