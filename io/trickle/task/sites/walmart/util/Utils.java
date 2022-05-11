package io.trickle.task.sites.walmart.util;

import io.trickle.task.antibot.impl.px.payload.captcha.util.Ww;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

public class Utils {
   public static long[] fillArrRandInt256(long[] var0) {
      for(int var1 = 0; var1 < var0.length; ++var1) {
         var0[var1] = (long)ThreadLocalRandom.current().nextInt(256);
      }

      return var0;
   }

   /** @deprecated */
   @Deprecated
   public static String buildWebUA() {
      StringBuilder var0 = new StringBuilder("Walmart/210");

      for(int var1 = 0; var1 < 7; ++var1) {
         var0.append(ThreadLocalRandom.current().nextInt(10));
      }

      var0.append(" Walmart WMTAPP v21.");
      var0.append("3.0");
      return var0.toString();
   }

   public static String genUet() {
      long[] var0 = new long[10];
      long[] var1 = new long[16];
      fillArrRandInt256(var0);
      long var2 = (Instant.now().toEpochMilli() + 12219292800000L) * 10000L + (var0[8] + (var0[9] << 8)) % 10000L;
      var1[3] = var2 & 255L;
      var1[2] = (long)(Ww.rightBitwise(var2, 8L) & 255);
      var1[1] = (long)(Ww.rightBitwise(var2, 16L) & 255);
      var1[0] = (long)(Ww.rightBitwise(var2, 24L) & 255);
      var2 /= 4294967296L;
      var1[5] = var2 & 255L;
      var1[4] = (long)(Ww.rightBitwise(var2, 8L) & 255);
      var1[7] = (long)(Ww.rightBitwise(var2, 16L) & 255);
      var1[6] = (long)(Ww.rightBitwise(var2, 24L) & 255);

      for(int var4 = 0; var4 < 8; ++var4) {
         var1[var4 + 8] = var0[var4];
      }

      var1[8] &= 63L;
      var1[8] |= 128L;
      var1[6] &= 15L;
      var1[6] |= 16L;
      var1[10] |= 1L;
      return st(var1, true);
   }

   /** @deprecated */
   @Deprecated
   public static String buildUA() {
      StringBuilder var0 = new StringBuilder("Walmart/201");

      int var1;
      for(var1 = 0; var1 < 7; ++var1) {
         var0.append(ThreadLocalRandom.current().nextInt(10));
      }

      var0.append(" CFNetwork/11");

      for(var1 = 0; var1 < 4; ++var1) {
         if (var1 >= 2) {
            var0.append(".");
         }

         var0.append(ThreadLocalRandom.current().nextInt(10));
      }

      var0.append(" Darwin/1");

      for(var1 = 0; var1 < 3; ++var1) {
         if (var1 >= 1) {
            var0.append(".");
         }

         var0.append(ThreadLocalRandom.current().nextInt(10));
      }

      return var0.toString();
   }

   public static String rot13(String var0) {
      StringBuilder var1 = new StringBuilder();

      for(int var2 = 0; var2 < var0.length(); ++var2) {
         char var3 = var0.charAt(var2);
         if (var3 >= 'a' && var3 <= 'm') {
            var3 = (char)(var3 + 13);
         } else if (var3 >= 'A' && var3 <= 'M') {
            var3 = (char)(var3 + 13);
         } else if (var3 >= 'n' && var3 <= 'z') {
            var3 = (char)(var3 - 13);
         } else if (var3 >= 'N' && var3 <= 'Z') {
            var3 = (char)(var3 - 13);
         }

         var1.append(var3);
      }

      return var1.toString();
   }

   public static String st(long[] var0, boolean var1) {
      String var2 = "";

      for(int var3 = 0; var3 < 16; ++var3) {
         if (var1 && var3 >= 4 && var3 <= 10 && var3 % 2 == 0) {
            var2 = var2 + "-";
         }

         var2 = var2 + Long.toHexString(var0[var3] + 256L).substring(1, 3);
      }

      return var2;
   }
}
