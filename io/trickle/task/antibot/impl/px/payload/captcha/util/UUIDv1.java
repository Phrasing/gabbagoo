package io.trickle.task.antibot.impl.px.payload.captcha.util;

import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

public class UUIDv1 {
   public static String[] gg = fillHexStringArr256();

   public static String[] fillHexStringArr256() {
      String[] var0 = new String[256];

      for(int var1 = 0; var1 < var0.length; ++var1) {
         var0[var1] = Integer.toHexString(var1);
      }

      return var0;
   }

   public static String genUUIDv1() {
      long var0 = 0L;
      String var2 = "";
      int[] var3 = fillArrRandInt256(new int[16]);
      int var4 = 0;
      int[] var5 = new int[16];
      int[] var6 = new int[0];
      int var7 = 16383 & (Ww.leftBitwise((long)var3[6], 8L) | var3[7]);
      long var8 = Instant.now().toEpochMilli();
      long var10 = 1L;
      long var12 = var8 - 0L + (var10 - 0L) / 10000L;
      if (var12 < 0L || var8 > var0) {
         var10 = 0L;
      }

      if ((double)var10 >= Double.longBitsToDouble(4666723172467343360L)) {
         throw new Error("uuid.v1(): Can't create more than 10M uuids/sec");
      } else {
         long var16 = (long)var7;
         var8 += 12219292800000L;
         long var18 = (10000L * (268435455L & var8) + var10) % 4294967296L;
         var5[var4++] = Ww.rightTripleBitwise(var18, 24L) & 255;
         var5[var4++] = Ww.rightTripleBitwise(var18, 16L) & 255;
         var5[var4++] = Ww.rightTripleBitwise(var18, 8L) & 255;
         var5[var4++] = (int)(255L & var18);
         int var20 = (int)((double)var8 / Double.longBitsToDouble(4751297606875873280L) * Double.longBitsToDouble(4666723172467343360L)) & 268435455;
         var5[var4++] = Ww.rightTripleBitwise((long)var20, 8L) & 255;
         var5[var4++] = 255 & var20;
         var5[var4++] = Ww.rightTripleBitwise((long)var20, 24L) & 15 | 16;
         var5[var4++] = Ww.rightTripleBitwise((long)var20, 16L) & 255;
         var5[var4++] = Ww.rightTripleBitwise((long)var7, 8L) | 128;
         var5[var4++] = 255 & var7;
         int[] var21 = new int[]{1 | var3[0], var3[1], var3[2], var3[3], var3[4], var3[5]};
         int[] var22 = var21;

         for(int var23 = 0; var23 < 6; ++var23) {
            var5[var4 + var23] = var22[var23];
         }

         String var24 = Cn(var5, 0);
         return var24;
      }
   }

   public static String Cn(int[] var0, int var1) {
      int var2 = 0;
      String[] var3 = gg;
      String var10000 = var3[var0[var2++]];
      return var10000 + var3[var0[var2++]] + var3[var0[var2++]] + var3[var0[var2++]] + "-" + var3[var0[var2++]] + var3[var0[var2++]] + "-" + var3[var0[var2++]] + var3[var0[var2++]] + "-" + var3[var0[var2++]] + var3[var0[var2++]] + "-" + var3[var0[var2++]] + var3[var0[var2++]] + var3[var0[var2++]] + var3[var0[var2++]] + var3[var0[var2++]] + var3[var0[var2++]];
   }

   public static int[] fillArrRandInt256(int[] var0) {
      for(int var1 = 0; var1 < var0.length; ++var1) {
         var0[var1] = ThreadLocalRandom.current().nextInt(256);
      }

      return var0;
   }
}
