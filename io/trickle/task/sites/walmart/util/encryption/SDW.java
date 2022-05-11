package io.trickle.task.sites.walmart.util.encryption;

public class SDW {
   public Aes aes;
   public static String HEX = "0123456789abcdef";

   public SDW(Aes var1) {
      this.aes = var1;
   }

   public String reformat(String var1, String var2) {
      String var3 = "";
      int var4 = 0;

      for(int var5 = 0; var5 < var2.length(); ++var5) {
         if (var4 < var1.length() && Encryptor.base10.indexOf(var2.charAt(var5)) >= 0) {
            var3 = var3 + var1.charAt(var4);
            ++var4;
         } else {
            var3 = var3 + var2.charAt(var5);
         }
      }

      return var3;
   }

   public String integrity(String var1, String var2, String var3) {
      String var4 = Character.toString(0) + Character.toString(var2.length()) + var2 + Character.toString(0) + Character.toString(var3.length()) + var3;
      long[] var5 = Encryptor.hexToWords(var1);
      var5[3] ^= 1L;
      Aes var6 = this.aes.cipher(var5);
      CMAC var7 = new CMAC();
      int[] var8 = var7.compute(var6, var4);
      String var10000 = this.wordToHex(var8[0]);
      return var10000 + this.wordToHex(var8[1]);
   }

   public String wordToHex(int var1) {
      int var2 = 32;

      String var3;
      for(var3 = ""; var2 > 0; var3 = var3 + "0123456789abcdef".charAt(var1 >>> var2 & 15)) {
         var2 -= 4;
      }

      return var3;
   }

   public String fixluhn(String var1, int var2, int var3) {
      int var4 = this.luhn(var1);
      if (var4 < var3) {
         var4 += 10 - var3;
      } else {
         var4 -= var3;
      }

      if (var4 != 0) {
         if ((var1.length() - var2) % 2 != 0) {
            var4 = 10 - var4;
         } else if (var4 % 2 == 0) {
            var4 = 5 - var4 / 2;
         } else {
            var4 = (9 - var4) / 2 + 5;
         }

         return var1.substring(0, var2) + var4 + var1.substring(var2 + 1);
      } else {
         return var1;
      }
   }

   public int luhn(String var1) {
      int var2 = var1.length() - 1;

      int var3;
      for(var3 = 0; var2 >= 0; var2 -= 2) {
         var3 += Integer.parseInt(String.valueOf(var1.charAt(var2)), 10);
      }

      for(var2 = var1.length() - 2; var2 >= 0; var2 -= 2) {
         int var4 = 2 * Integer.parseInt(String.valueOf(var1.charAt(var2)), 10);
         if (var4 < 10) {
            var3 += var4;
         } else {
            var3 += var4 - 9;
         }
      }

      return var3 % 10;
   }
}
