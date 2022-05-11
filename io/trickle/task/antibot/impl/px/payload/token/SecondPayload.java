package io.trickle.task.antibot.impl.px.payload.token;

import io.trickle.task.antibot.impl.px.payload.Payload;
import io.trickle.task.sites.Site;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SecondPayload implements Payload {
   public String PX320;
   public JsonArray PX347;
   public String PX257;
   public int PX204;
   public Battery PX418;
   public String PX256;
   public Battery PX414;
   public String PX344;
   public static Logger logger = LogManager.getLogger(SecondPayload.class);
   public Battery PX419;
   public long PX349;
   public Battery PX415;
   public String[] responseValues;
   public String PX343;
   public Battery PX420;
   public String PX339;
   public String UUIDV4_HEADER;
   public Devices$Device device;
   public Battery PX416;
   public String t;
   public String PX322;
   public String VID_HEADER;
   public Site SITE;
   public int sdkInitCount;
   public long PX259;
   public String SID_HEADER;
   public Battery PX413;

   public MultiMap asKeepAliveForm() {
      // $FF: Couldn't be decompiled
   }

   public int m4765a(int var1, int var2, int var3, int var4, int var5, int var6) {
      int var7 = m4764a(var1, var2, var4, var6);
      int var8 = m4764a(var7, var3, var5, var6);
      return var8 ^ this.m4767d();
   }

   public long parseAppc2Timestamp() {
      return Long.parseLong(this.responseValues[1]);
   }

   public String getKeepAlivePayload() {
      return Base64.getEncoder().encodeToString(this.toStringFollowUpRequests().getBytes(StandardCharsets.UTF_8));
   }

   public String toStringFollowUpRequests() {
      // $FF: Couldn't be decompiled
   }

   public String toString() {
      // $FF: Couldn't be decompiled
   }

   public String parseAppc2PX256() {
      return this.responseValues[2];
   }

   public String getPayload() {
      return Base64.getEncoder().encodeToString(this.toString().getBytes(StandardCharsets.UTF_8));
   }

   public int m4767d() {
      byte[] var1 = new byte[4];
      var1 = this.PX320.getBytes(StandardCharsets.UTF_8);
      return this.m4766a(var1);
   }

   public MultiMap asForm() {
      // $FF: Couldn't be decompiled
   }

   public void updatePX349(long var1) {
      this.PX349 = var1;
   }

   public SecondPayload(FirstPayload var1, JsonObject var2, long var3, Site var5) {
      this.device = var1.device;
      this.sdkInitCount = var1.PX345;
      this.SITE = var5;
      JsonArray var6 = var2.getJsonArray("do");
      if (var6.size() < 3 && var1.SID_HEADER == null) {
         logger.error("Could not find P-TS!");
         logger.error(var6.toString());
         throw new Exception("Invalid do arr");
      } else {
         if (var1.SID_HEADER != null) {
            this.SID_HEADER = var1.SID_HEADER;
         } else {
            this.SID_HEADER = var6.getString(0).split("\\|")[1];
         }

         if (var1.VID_HEADER != null) {
            this.VID_HEADER = var1.VID_HEADER;
         } else {
            this.VID_HEADER = var6.getString(1).split("\\|")[1];
         }

         this.UUIDV4_HEADER = var1.UUIDV4_HEADER;
         this.t = "PX329";
         this.PX349 = var3;
         this.PX320 = var1.PX320;
         String var7 = var6.getString(var6.size() - 1);
         this.responseValues = var7.replace("appc|", "").split("\\|");
         if (this.responseValues.length != 9) {
            logger.error("Could not find P-TS!");
            logger.error(Arrays.toString(this.responseValues));
            throw new Exception("Invalid do arr");
         } else {
            this.PX259 = this.parseAppc2Timestamp();
            this.PX256 = this.parseAppc2PX256();
            this.PX257 = "" + this.m4765a(Integer.parseInt(this.responseValues[5]), Integer.parseInt(this.responseValues[6]), Integer.parseInt(this.responseValues[7]), Integer.parseInt(this.responseValues[3]), Integer.parseInt(this.responseValues[4]), Integer.parseInt(this.responseValues[8]));
            this.PX339 = var1.PX339;
            this.PX322 = var1.PX322;
            this.PX343 = var1.PX343;
            this.PX344 = var1.PX344;
            this.PX347 = var1.PX347;
            this.PX413 = var1.PX413;
            this.PX414 = var1.PX414;
            this.PX415 = var1.PX415;
            this.PX416 = var1.PX416;
            this.PX419 = var1.PX419;
            this.PX418 = var1.PX418;
            this.PX420 = var1.PX420;
         }
      }
   }

   public int m4766a(byte[] var1) {
      return var1.length < 4 ? 0 : ByteBuffer.wrap(var1).getInt();
   }

   public static int m4764a(int var0, int var1, int var2, int var3) {
      int var4 = var3 % 10;
      int var5 = var4 != 0 ? var2 % var4 : var2 % 10;
      int var6 = var0 * var0;
      int var7 = var1 * var1;
      switch (var5) {
         case 0:
            return var6 + var1;
         case 1:
            return var0 + var7;
         case 2:
            return var6 * var1;
         case 3:
            return var0 ^ var1;
         case 4:
            return var0 - var7;
         case 5:
            int var8 = var0 + 783;
            return var8 * var8 + var7;
         case 6:
            return (var0 ^ var1) + var1;
         case 7:
            return var6 - var7;
         case 8:
            return var0 * var1;
         case 9:
            return var1 * var0 - var0;
         default:
            return -1;
      }
   }
}
