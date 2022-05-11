package io.trickle.task.antibot.impl.px.payload.token;

import io.trickle.task.antibot.impl.px.payload.Payload;
import io.trickle.task.sites.Site;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

public class FirstPayload implements Payload {
   public long PX349;
   public int PX91;
   public boolean PX332;
   public boolean PX336;
   public String PX421;
   public int PX350;
   public String PX339;
   public String SID_HEADER;
   public boolean PX316;
   public Battery PX414;
   public String UUIDV4_HEADER;
   public boolean PX337;
   public Battery PX419;
   public String VID_HEADER;
   public Battery PX418;
   public Devices$Device device;
   public String PX442;
   public String PX344;
   public int PX92;
   public JsonArray PX347;
   public String PX330;
   public long PX323;
   public String PX328;
   public String PX318;
   public int PX351;
   public boolean PX333;
   public String f20431a;
   public String PX322;
   public String PX320;
   public boolean PX335;
   public Battery PX416;
   public String PX319;
   public Site SITE;
   public String PX343;
   public boolean PX331;
   public int PX345;
   public Battery PX420;
   public static char[] f18573a = "0123456789ABCDEF".toCharArray();
   public Battery PX413;
   public boolean PX334;
   public long f20430a;
   public String PX317;
   public String t;
   public String PX326;
   public String PX327;
   public long f20432b;
   public Battery PX415;

   public void initUUIDSettings() {
      new AtomicLong(Long.MIN_VALUE);
      this.f20432b = Long.MIN_VALUE;
      Object var3 = null;
      long var1;
      if (var3 != null) {
         var1 = Long.parseLong((String)var3, 16) | this.f20432b;
      } else {
         byte[] var4 = this.m30012b();
         long var5 = this.f20432b | (long)(var4[0] << 24) & 4278190080L;
         this.f20432b = var5;
         long var7 = var5 | (long)(var4[1] << 16 & 16711680);
         this.f20432b = var7;
         long var9 = var7 | (long)(var4[2] << 8 & '\uff00');
         this.f20432b = var9;
         var1 = var9 | (long)(var4[3] & 255);
      }

      this.f20432b = var1;
      this.f20432b |= (long)(ThreadLocalRandom.current().nextDouble() * Double.longBitsToDouble(4670232263827390464L)) << 48;
   }

   public void nextBytes(byte[] var1) {
      int var2 = 0;
      int var3 = var1.length;

      while(var2 < var3) {
         int var4 = ThreadLocalRandom.current().nextInt();

         for(int var5 = Math.min(var3 - var2, 4); var5-- > 0; var4 >>= 8) {
            var1[var2++] = (byte)var4;
         }
      }

   }

   public FirstPayload(Devices$Device var1, Site var2) {
      this(var1, (String)null, (String)null, 1, 1L, var2);
   }

   public FirstPayload(SecondPayload var1, int var2, long var3, Site var5) {
      this(var1.device, var1.VID_HEADER, var1.SID_HEADER, var2, var3, var5);
   }

   public MultiMap asForm() {
      // $FF: Couldn't be decompiled
   }

   public UUID genTimeBasedUUID() {
      long var1 = System.currentTimeMillis() * 10000L + 122192928000000000L;
      return new UUID(m30010d(this.m30009c(var1)), this.m30011a());
   }

   public FirstPayload(Devices$Device var1, String var2, String var3, int var4, long var5, Site var7) {
      // $FF: Couldn't be decompiled
   }

   public String getPayload() {
      return Base64.getEncoder().encodeToString(this.toString().getBytes(StandardCharsets.UTF_8));
   }

   public long m30011a() {
      return this.f20432b;
   }

   public static long m30010d(long var0) {
      return (var0 & -281474976710656L) >>> 48 | var0 << 32 | 4096L | (281470681743360L & var0) >>> 16;
   }

   public String toString() {
      // $FF: Couldn't be decompiled
   }

   public static String m4818a(byte[] var0) {
      char[] var1 = new char[var0.length * 2];

      for(int var2 = 0; var2 < var0.length; ++var2) {
         int var3 = var0[var2] & 255;
         int var4 = var2 * 2;
         char[] var5 = f18573a;
         var1[var4] = var5[var3 >>> 4];
         var1[var4 + 1] = var5[var3 & 15];
      }

      return new String(var1);
   }

   public long m30009c(long var1) {
      long var3 = this.f20430a;
      if (var1 > var3) {
         this.f20430a = var1;
         return var1;
      } else {
         long var5 = var3 + 1L;
         this.f20430a = var5;
         return var5;
      }
   }

   public static String m4817a(String var0) {
      try {
         MessageDigest var1 = MessageDigest.getInstance("SHA-1");
         byte[] var2 = var0.getBytes(StandardCharsets.UTF_8);
         var1.update(var2, 0, var2.length);
         return m4818a(var1.digest());
      } catch (NoSuchAlgorithmException var3) {
         var3.printStackTrace();
         return null;
      }
   }

   public byte[] m30012b() {
      byte[] var1 = new byte[4];
      this.nextBytes(var1);
      return var1;
   }

   public FirstPayload(InitPayload var1, Site var2) {
      this(var1.device, var2);
   }
}
