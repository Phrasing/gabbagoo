package io.trickle.task.antibot.impl.akamai.pixel;

import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public class TrickleAPI implements Pixel {
   public Devices$Device device;

   public JsonObject getSR() {
      return this.device.getSr();
   }

   public String getBR() {
      return this.device.getBr();
   }

   public String getBT() {
      return this.device.getBt() == null ? "0" : this.device.getBt().toString();
   }

   public JsonObject getCRC() {
      return this.device.getCrc();
   }

   public JsonObject getDP() {
      return this.device.getDp();
   }

   public double getJSV() {
      return this.device.getJsv();
   }

   public String getCV() {
      return this.device.getCv();
   }

   public boolean getSP() {
      return this.device.isSp();
   }

   public boolean getIEPS() {
      return this.device.isIeps();
   }

   public String getFontHash() {
      return this.device.getFh();
   }

   public boolean getAP() {
      return this.device.isAp();
   }

   public String getPS() {
      return this.device.getPs();
   }

   public String getTiming() {
      int var1 = 37 + ThreadLocalRandom.current().nextInt(-3, 4);
      int var2 = 270 + ThreadLocalRandom.current().nextInt(-5, 5);
      int var3 = 392 + ThreadLocalRandom.current().nextInt(-5, 6);
      int var4 = 510 + ThreadLocalRandom.current().nextInt(-5, 5);
      int var5 = 616 + ThreadLocalRandom.current().nextInt(-5, 6);
      int var6 = ThreadLocalRandom.current().nextInt(9, 12);
      return "{\"1\":" + var1 + ",\"2\":" + var2 + ",\"3\":" + var3 + ",\"4\":" + var4 + ",\"5\":" + var5 + ",\"profile\":{\"bp\":" + ThreadLocalRandom.current().nextInt(0, 1) + ",\"sr\":" + ThreadLocalRandom.current().nextInt(0, 1) + ",\"dp\":0,\"lt\":0,\"ps\":" + ThreadLocalRandom.current().nextInt(0, 2) + ",\"cv\":" + ThreadLocalRandom.current().nextInt(22, 24) + ",\"fp\":0,\"sp\":" + ThreadLocalRandom.current().nextInt(0, 1) + ",\"br\":0,\"ieps\":0,\"av\":0,\"z1\":" + ThreadLocalRandom.current().nextInt(10, 11) + ",\"jsv\":" + ThreadLocalRandom.current().nextInt(0, 1) + ",\"nav\":" + ThreadLocalRandom.current().nextInt(0, 1) + ",\"nap\":" + ThreadLocalRandom.current().nextInt(1, 2) + ",\"crc\":0,\"z2\":" + ThreadLocalRandom.current().nextInt(1, 2) + ",\"z3\":1,\"z4\":0,\"z5\":" + ThreadLocalRandom.current().nextInt(0, 1) + ",\"fonts\":" + var6 + "},\"main\":" + ThreadLocalRandom.current().nextInt(101, 104) + ",\"compute\":" + var1 + ",\"send\":" + (var5 + var6) + "}";
   }

   public boolean getFC() {
      return this.device.isFc();
   }

   public String getNap() {
      return this.device.getNap();
   }

   public TrickleAPI(JsonObject var1) {
      this.device = Devices.genFromJson(var1);
   }

   public CompletableFuture getPixelReqString(String var1, String var2, String var3) {
      return CompletableFuture.failedFuture(new Exception("Unsupported method"));
   }

   public String getZ(String var1) {
      return "{\"a\":" + var1 + ",\"b\":" + this.device.getB() + ",\"c\":" + this.device.getC() + "}";
   }

   public boolean getAV() {
      return this.device.isAv();
   }

   public CompletableFuture getPixelReqForm(String var1, String var2, String var3) {
      MultiMap var4 = MultiMap.caseInsensitiveMultiMap();

      try {
         var4.set("ap", String.valueOf(this.getAP()));
         var4.set("bt", this.getBT());
         var4.set("fonts", this.getFonts());
         var4.set("fh", this.getFontHash());
         var4.set("timing", this.getTiming());
         var4.set("bp", this.getBP());
         var4.set("sr", this.getSR().toString());
         var4.set("dp", this.getDP().toString());
         var4.set("lt", this.getLT());
         var4.set("ps", this.getPS());
         var4.set("cv", this.getCV());
         var4.set("fp", String.valueOf(this.getFP()));
         var4.set("sp", String.valueOf(this.getSP()));
         var4.set("br", this.getBR());
         var4.set("ieps", String.valueOf(this.getIEPS()));
         var4.set("av", String.valueOf(this.getAV()));
         var4.set("z", this.getZ(var1));
         var4.set("zh", this.getZH());
         var4.set("jsv", "" + this.getJSV());
         var4.set("nav", this.getNav().toString());
         var4.set("crc", this.getCRC().toString());
         var4.set("t", var2);
         var4.set("u", var3);
         var4.set("nap", this.getNap());
         var4.set("fc", String.valueOf(this.getFC()));
      } catch (Throwable var6) {
         System.out.println("Error building FW: " + var6.getMessage());
      }

      return CompletableFuture.completedFuture(var4);
   }

   public boolean getFP() {
      return this.device.isFp();
   }

   public String getFonts() {
      return this.device.getFonts();
   }

   public String getLT() {
      String var1 = this.device.getLt();
      long var10000;
      if (var1.contains("-")) {
         var1 = this.device.getLt().split("-")[1];
         var10000 = Instant.now().toEpochMilli();
         return "" + var10000 + "-" + var1;
      } else if (var1.contains("\\+")) {
         var1 = this.device.getLt().split("\\+")[1];
         var10000 = Instant.now().toEpochMilli();
         return "" + var10000 + "+" + var1;
      } else {
         return "" + Instant.now().toEpochMilli();
      }
   }

   public JsonObject getNav() {
      return this.device.getNav();
   }

   public String getBP() {
      return this.device.getBp();
   }

   public String getZH() {
      return this.device.getZh();
   }
}
