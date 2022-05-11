package io.trickle.task.antibot.impl.akamai.pixel;

import io.vertx.core.json.JsonObject;

public class Devices$DeviceImpl implements Devices$Device {
   public String fh;
   public String nap;
   public String cv;
   public boolean fc;
   public boolean ieps;
   public JsonObject bt;
   public String bp;
   public int b;
   public JsonObject sr;
   public String zh;
   public boolean fp;
   public JsonObject timing;
   public String fonts;
   public boolean av;
   public JsonObject nav;
   public String ps;
   public String lt;
   public double jsv;
   public JsonObject crc;
   public boolean sp;
   public String br;
   public JsonObject dp;
   public boolean ap;
   public int c;

   public String getLt() {
      return this.lt;
   }

   public int getB() {
      return this.b;
   }

   public boolean isAv() {
      return this.av;
   }

   public JsonObject getNav() {
      return this.nav;
   }

   public boolean isSp() {
      return this.sp;
   }

   public String getFonts() {
      return this.fonts;
   }

   public String getFh() {
      return this.fh;
   }

   public String getBr() {
      return this.br;
   }

   public String getZh() {
      return this.zh;
   }

   public JsonObject getSr() {
      return this.sr;
   }

   public String getBp() {
      return this.bp;
   }

   public int getC() {
      return this.c;
   }

   public JsonObject getBt() {
      return this.bt;
   }

   public JsonObject getCrc() {
      return this.crc;
   }

   public boolean isAp() {
      return this.ap;
   }

   public boolean isFp() {
      return this.fp;
   }

   public double getJsv() {
      return this.jsv;
   }

   public JsonObject getTiming() {
      return this.timing;
   }

   public JsonObject getDp() {
      return this.dp;
   }

   public String getCv() {
      return this.cv;
   }

   public Devices$DeviceImpl(boolean var1, JsonObject var2, String var3, String var4, JsonObject var5, String var6, JsonObject var7, JsonObject var8, String var9, String var10, String var11, boolean var12, boolean var13, String var14, boolean var15, boolean var16, int var17, int var18, String var19, double var20, JsonObject var22, JsonObject var23, String var24, boolean var25) {
      this.ap = var1;
      this.bt = var2;
      this.fonts = var3;
      this.fh = var4;
      this.timing = var5;
      this.bp = var6;
      this.sr = var7;
      this.dp = var8;
      this.lt = var9;
      this.ps = var10;
      this.cv = var11;
      this.fp = var12;
      this.sp = var13;
      this.br = var14;
      this.ieps = var15;
      this.av = var16;
      this.b = var17;
      this.c = var18;
      this.zh = var19;
      this.jsv = var20;
      this.nav = var22;
      this.crc = var23;
      this.nap = var24;
      this.fc = var25;
   }

   public String getPs() {
      return this.ps;
   }

   public String getNap() {
      return this.nap;
   }

   public boolean isFc() {
      return this.fc;
   }

   public boolean isIeps() {
      return this.ieps;
   }
}
