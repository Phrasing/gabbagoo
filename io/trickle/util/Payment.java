package io.trickle.util;

import java.util.concurrent.ThreadLocalRandom;

public class Payment {
   public static String generateCard(String var0, int var1) {
      int var2 = var1 - (var0.length() + 1);
      StringBuilder var3 = new StringBuilder(var0);

      int var4;
      for(var4 = 0; var4 < var2; ++var4) {
         int var5 = ThreadLocalRandom.current().nextInt(10);
         var3.append(var5);
      }

      var4 = getCheckDigit(var3.toString());
      var3.append(var4);
      return var3.toString();
   }

   public static int getCheckDigit(String var0) {
      int var1 = 0;

      int var2;
      for(var2 = 0; var2 < var0.length(); ++var2) {
         int var3 = Integer.parseInt(var0.substring(var2, var2 + 1));
         if (var2 % 2 == 0) {
            var3 *= 2;
            if (var3 > 9) {
               var3 = var3 / 10 + var3 % 10;
            }
         }

         var1 += var3;
      }

      var2 = var1 % 10;
      return var2 == 0 ? 0 : 10 - var2;
   }
}
