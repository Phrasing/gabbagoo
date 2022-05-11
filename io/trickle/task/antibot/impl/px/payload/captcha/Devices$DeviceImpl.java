package io.trickle.task.antibot.impl.px.payload.captcha;

import io.trickle.task.antibot.impl.px.payload.captcha.util.DeviceHeaderParsers;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class Devices$DeviceImpl implements Devices$Device {
   public int outerWidth;
   public int scrollbarWidth;
   public int innerWidth;
   public int innerHeight;
   public String appCodeName;
   public String secUAMobile;
   public String useragent;
   public int height;
   public JsonObject mongoDocument;
   public String productSub;
   public int outerHeight;
   public String appVersion;
   public String acceptLanguage;
   public String product;
   public int availWidth;
   public int width;
   public String appName;
   public String secUA;
   public String acceptEncoding;
   public int screenY;
   public int screenX;
   public int availHeight;
   public String platform;

   public String getUserAgent() {
      return this.useragent;
   }

   public Boolean getBool(String var1) {
      return this.mongoDocument.getBoolean(var1);
   }

   public String getAcceptLanguage() {
      return this.acceptLanguage == null ? DeviceHeaderParsers.getAcceptLanguage(this.getArr("PX313")) : this.acceptLanguage;
   }

   public int getAvailHeight() {
      return this.availHeight;
   }

   public String getProductSub() {
      return this.productSub;
   }

   public Devices$DeviceImpl(int var1, int var2, int var3, int var4, int var5, int var6, String var7, String var8, String var9, String var10, String var11, String var12, String var13) {
      this.height = var1;
      this.width = var2;
      this.innerHeight = var3;
      this.innerWidth = var4;
      this.availHeight = var5;
      this.availWidth = var6;
      this.platform = var7;
      this.useragent = var8;
      this.product = var9;
      this.productSub = var10;
      this.appVersion = var11;
      this.appName = var12;
      this.appCodeName = var13;
   }

   public String getProduct() {
      return this.product;
   }

   public Number getNumber(String var1) {
      return this.mongoDocument.getNumber(var1);
   }

   public int getInnerHeight() {
      return this.innerHeight;
   }

   public JsonArray getArr(String var1) {
      return this.mongoDocument.getJsonArray(var1);
   }

   public String getAppName() {
      return this.appName;
   }

   public int getAvailWidth() {
      return this.availWidth;
   }

   public String getString(String var1) {
      return this.mongoDocument.getString(var1);
   }

   public String getPlatform() {
      return this.platform;
   }

   public String getSecUA() {
      return this.secUA;
   }

   public String getAppVersion() {
      return this.appVersion;
   }

   public int getHeight() {
      return this.height;
   }

   public String getAppCodeName() {
      return this.appCodeName;
   }

   public String getAcceptEncoding() {
      return this.acceptEncoding;
   }

   public int getScrollbarWidth() {
      return this.scrollbarWidth;
   }

   public Devices$DeviceImpl(JsonObject var1) {
      try {
         this.mongoDocument = var1;
         this.mongoDocument.put("PX61", "en-US");
         this.mongoDocument.put("PX313", new JsonArray("[\"en-US\",\"en\"]"));
         this.mongoDocument.put("accept-language", "en-US,en;q=0.9");
         this.screenX = var1.getInteger("screenXPos");
         this.screenY = var1.getInteger("screenYPos");
         this.outerHeight = var1.getInteger("outerHeight");
         this.outerWidth = var1.getInteger("outerWidth");
         this.acceptLanguage = var1.getString("accept-language");
         this.acceptEncoding = "gzip, deflate, br";
         this.secUA = var1.getString("sec-ch-ua");
         this.secUAMobile = var1.getString("sec-ch-ua-mobile");
      } catch (Throwable var3) {
         var3.printStackTrace();
      }

      this.width = var1.getInteger("PX91");
      this.height = var1.getInteger("PX92");
      this.availWidth = var1.getInteger("PX269");
      this.availHeight = var1.getInteger("PX270");
      this.innerWidth = var1.getInteger("PX186");
      this.innerHeight = var1.getInteger("PX185");
      this.platform = var1.getString("PX63");
      this.useragent = var1.getString("PX59");
      this.product = var1.getString("PX62");
      this.productSub = var1.getString("PX69");
      this.appVersion = var1.getString("PX64");
      this.appName = var1.getString("PX65");
      this.appCodeName = var1.getString("PX66");
      this.scrollbarWidth = var1.getInteger("scrollbarWidth") == null ? 0 : var1.getInteger("scrollbarWidth");
   }

   public int getWidth() {
      return this.width;
   }

   public String getSecUAMobile() {
      return this.secUAMobile;
   }

   public int getInnerWidth() {
      return this.innerWidth;
   }
}
