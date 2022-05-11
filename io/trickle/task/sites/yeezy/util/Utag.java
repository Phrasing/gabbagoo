package io.trickle.task.sites.yeezy.util;

import io.trickle.task.antibot.impl.akamai.sensor.Bmak;
import io.trickle.task.sites.yeezy.YeezyAPI;
import io.trickle.util.Utils;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

public class Utag {
   public long ses_id = -1L;
   public String userAgent;
   public long exp;
   public String documentUrl;
   public String sku;
   public int updateCookieCounter = 0;
   public long st;
   public Bmak bmak;
   public String v_id;
   public static int session_timeout = 1800000;
   public String productName;

   public String vi(long var1) {
      String var3;
      String var4;
      if (this.bmak != null && this.v_id == null) {
         var3 = this.pad("" + var1, 12);
         var4 = "" + ThreadLocalRandom.current().nextDouble();
         var3 = var3 + this.pad(var4.substring(2), 16);
         var3 = var3 + this.pad("" + this.bmak.getDevice().getPluginLength(), 2);
         var3 = var3 + this.pad("" + this.bmak.getDevice().getUserAgent().length(), 3);
         var3 = var3 + this.pad("" + this.documentUrl.length(), 4);
         var3 = var3 + this.pad("" + this.bmak.getDevice().getUserAgent().replace("Mozilla/", "").length(), 3);
         var3 = var3 + this.pad("" + (this.bmak.getDevice().getScreenWidth() + this.bmak.getDevice().getScreenHeight() + this.bmak.getDevice().getColorDepth()), 5);
         this.v_id = var3;
      } else if (this.userAgent != null && this.v_id == null) {
         var3 = this.pad("" + var1, 12);
         var4 = "" + Utils.smartNextDouble();
         var3 = var3 + this.pad(var4.substring(2), 16);
         var3 = var3 + this.pad("3", 2);
         var3 = var3 + this.pad("" + this.userAgent.length(), 3);
         var3 = var3 + this.pad("" + this.documentUrl.length(), 4);
         var3 = var3 + this.pad("" + this.userAgent.length(), 3);
         int var10002 = (int)Math.floor(Math.random() * Double.longBitsToDouble(4655459775352406016L)) + 800;
         var3 = var3 + this.pad("" + (var10002 + (int)Math.floor(Math.random() * Double.longBitsToDouble(4650608730050658304L)) + 600 - 24 - 1), 5);
         this.v_id = var3;
      }

      return this.v_id;
   }

   public String getPrevPage() {
      if (!this.documentUrl.contains("product") && !this.documentUrl.contains("archive")) {
         if (this.documentUrl.equals(YeezyAPI.QUEUE_URL)) {
            return this.getEncodedWR();
         } else if (this.documentUrl.contains("/delivery")) {
            return this.getEncodedShipping();
         } else {
            return this.documentUrl.contains("/payment") ? this.getEncodedProcessing() : "HOME";
         }
      } else {
         return this.getEncodedProductName();
      }
   }

   public void setName(String var1) {
      this.productName = URLEncoder.encode(var1, StandardCharsets.UTF_8).replace("+", "%20");
   }

   public Utag(String var1, String var2) {
      this.bmak = null;
      this.userAgent = var1;
      this.documentUrl = "https://www.yeezysupply.com/";
      this.sku = var2;
   }

   public String getEncodedProcessing() {
      return "CHECKOUT%7CPAYMENT";
   }

   public String genUtagMain() {
      this.vi(Instant.now().toEpochMilli());
      if (this.ses_id == -1L) {
         this.ses_id = Instant.now().toEpochMilli();
      }

      this.st = Instant.now().toEpochMilli() + 1800000L;
      this.exp = Instant.now().toEpochMilli() + (long)ThreadLocalRandom.current().nextInt(470, 480) + 3600000L;
      ++this.updateCookieCounter;
      String var10000 = this.v_id;
      return "v_id:" + var10000 + "$_se:" + this.updateCookieCounter + "$_ss:" + (this.updateCookieCounter == 1 ? 1 : 0) + "$_st:" + this.st + "$ses_id:" + this.ses_id + "%3Bexp-session$_pn:" + this.updateCookieCounter + "%3Bexp-session$_prevpage:" + this.getPrevPage() + "%3Bexp-" + this.exp;
   }

   public String pad(String var1, int var2) {
      long var10000 = Long.parseLong(var1);
      var1 = "" + Long.toString(var10000, 16);
      String var3 = "";
      if (var2 > var1.length()) {
         for(int var4 = 0; var4 < var2 - var1.length(); ++var4) {
            var3 = var3 + "0";
         }
      }

      return var3 + var1;
   }

   public String getEncodedShipping() {
      return "CHECKOUT%7CSHIPPING";
   }

   public static int H() {
      return (int)Math.ceil((double)Instant.now().toEpochMilli() / Double.longBitsToDouble(4725570615333879808L));
   }

   public String getEncodedProductName() {
      return "PRODUCT%7C" + this.productName + "%20(" + this.sku + ")";
   }

   public String getEncodedWR() {
      return "WAITING%20ROOM%7C" + this.productName + "%7C" + this.productName + "%20(" + this.sku + ")";
   }

   public void updateDocumentUrl(String var1) {
      this.documentUrl = var1;
   }

   public Utag(Bmak var1, String var2) {
      this.bmak = var1;
      this.userAgent = null;
      this.documentUrl = "https://www.yeezysupply.com/";
      this.sku = var2;
   }

   public static String r() {
      String var0 = "0123456789";
      String var1 = "";
      String var2 = "";
      byte var3 = 10;
      byte var4 = 10;

      for(int var5 = 0; 19 > var5; ++var5) {
         int var6 = ThreadLocalRandom.current().nextInt(var3);
         var1 = var1 + "0123456789".substring(var6, var6 + 1);
         if (0 == var5 && 9 == var6) {
            var3 = 3;
         } else if ((1 == var5 || 2 == var5) && 10 != var3 && 2 > var6) {
            var3 = 10;
         } else if (2 < var5) {
            var3 = 10;
         }

         var6 = ThreadLocalRandom.current().nextInt(var4);
         var2 = var2 + "0123456789".substring(var6, var6 + 1);
         if (0 == var5 && 9 == var6) {
            var4 = 3;
         } else if ((1 == var5 || 2 == var5) && 10 != var4 && 2 > var6) {
            var4 = 10;
         } else if (2 < var5) {
            var4 = 10;
         }
      }

      return var1 + var2;
   }
}
