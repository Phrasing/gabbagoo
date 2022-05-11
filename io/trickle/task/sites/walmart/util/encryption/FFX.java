package io.trickle.task.sites.walmart.util.encryption;

public class FFX {
   public Aes aesObject;
   public static String[] alphabet = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

   public int precompb(int var1, int var2) {
      int var3 = (int)Math.ceil((double)var2 / Double.longBitsToDouble(4611686018427387904L));
      int var4 = 0;
      int var5 = 1;

      while(var3 > 0) {
         var5 *= var1;
         --var3;
         if (var5 >= 256) {
            var5 /= 256;
            ++var4;
         }
      }

      if (var5 > 1) {
         ++var4;
      }

      return var4;
   }

   public int[] convertRadix(int[] var1, int var2, int var3, int var4, int var5) {
      int[] var6 = new int[var4];

      for(int var7 = 0; var7 < var4; ++var7) {
         var6[var7] = 0;
      }

      for(int var8 = 0; var8 < var2; ++var8) {
         this.bnMultiply(var6, var5, var3);
         this.bnAdd(var6, var5, var1[var8]);
      }

      return var6;
   }

   public FFX(Aes var1) {
      this.aesObject = var1;
   }

   public int[] f(Aes var1, int var2, String var3, int[] var4, int var5, int var6, int[] var7, int var8, int var9) {
      boolean var10 = true;
      int var11 = (int)(Math.ceil((double)var9 / Double.longBitsToDouble(4616189618054758400L)) + Double.longBitsToDouble(4607182418800017408L));
      int var12 = var3.length() + var9 + 1 & 15;
      if (var12 > 0) {
         var12 = 16 - var12;
      }

      int[] var13 = new int[var3.length() + var12 + var9 + 1];

      int var14;
      for(var14 = 0; var14 < var3.length(); ++var14) {
         var13[var14] = Character.codePointAt(var3, var14);
      }

      while(var14 < var12 + var3.length()) {
         var13[var14] = 0;
         ++var14;
      }

      var13[var13.length - var9 - 1] = var2;
      int[] var15 = this.convertRadix(var4, var5, var8, var9, 256);

      for(int var16 = 0; var16 < var9; ++var16) {
         var13[var13.length - var9 + var16] = var15[var16];
      }

      int[] var20 = this.cbcmacq(var7, var13, var13.length, var1);
      int[] var17 = var20;
      int[] var19 = new int[2 * var11];

      for(var14 = 0; var14 < var11; ++var14) {
         if (var14 > 0 && (var14 & 3) == 0) {
            int var18 = var14 >> 2;
            var17 = var1.encrypt(new int[]{var20[0], var20[1], var20[2], var20[3] ^ var18});
         }

         var19[2 * var14] = var17[var14 & 3] >>> 16;
         var19[2 * var14 + 1] = var17[var14 & 3] & '\uffff';
      }

      return this.convertRadix(var19, 2 * var11, 65536, var6, var8);
   }

   public String valToDigit(int[] var1, int var2) {
      String var3 = "";
      int var4;
      if (var2 == 256) {
         for(var4 = 0; var4 < var1.length; ++var4) {
            var3 = var3 + Character.toString(var1[var4]);
         }
      } else {
         for(var4 = 0; var4 < var1.length; ++var4) {
            var3 = var3 + alphabet[var1[var4]];
         }
      }

      return var3;
   }

   public void bnMultiply(int[] var1, int var2, int var3) {
      int var5 = 0;

      for(int var4 = var1.length - 1; var4 >= 0; --var4) {
         int var6 = var1[var4] * var3 + var5;
         var1[var4] = var6 % var2;
         var5 = (var6 - var1[var4]) / var2;
      }

   }

   public int[] precompF(Aes var1, int var2, String var3, int var4) {
      byte var5 = 4;
      int[] var6 = new int[var5];
      int var7 = var3.length();
      byte var8 = 10;
      var6[0] = 16908544 | var4 >> 16 & 255;
      var6[1] = (var4 >> 8 & 255) << 24 | (var4 & 255) << 16 | var8 << 8 | (int)Math.floor((double)(var2 / 2)) & 255;
      var6[2] = var2;
      var6[3] = var7;
      return var1.encrypt(var6);
   }

   public int[] digitToVal(String var1, int var2, int var3) {
      int[] var4 = new int[var2];
      int var5;
      if (var3 != 256) {
         for(var5 = 0; var5 < var2; ++var5) {
            int var6 = Integer.parseInt(String.valueOf(var1.charAt(var5)), var3);
            if (var6 >= var3) {
               return null;
            }

            var4[var5] = var6;
         }

         return var4;
      } else {
         for(var5 = 0; var5 < var2; ++var5) {
            var4[var5] = Character.codePointAt(var1, var5);
         }

         return var4;
      }
   }

   public int[] cbcmacq(int[] var1, int[] var2, int var3, Aes var4) {
      byte var5 = 4;
      int[] var6 = new int[var5];

      int var7;
      for(var7 = 0; var7 < var5; ++var7) {
         var6[var7] = var1[var7];
      }

      for(var7 = 0; 4 * var7 < var3; var7 += var5) {
         for(int var8 = 0; var8 < var5; ++var8) {
            var6[var8] ^= var2[4 * (var7 + var8)] << 24 | var2[4 * (var7 + var8) + 1] << 16 | var2[4 * (var7 + var8) + 2] << 8 | var2[4 * (var7 + var8) + 3];
         }

         var6 = var4.encrypt(var6);
      }

      return var6;
   }

   public String encryptWithCipher(String var1, String var2, Aes var3, int var4) {
      int var5 = var1.length();
      int var6 = (int)Math.floor((double)(var5 / 2));
      byte var7 = 5;
      int[] var8 = this.precompF(var3, var5, var2, var4);
      int var9 = this.precompb(var4, var5);
      int[] var10 = this.digitToVal(var1, var6, var4);
      int[] var11 = this.digitToVal(var1.substring(var6), var5 - var6, var4);
      if (var10 != null && var11 != null) {
         for(int var12 = 0; var12 < var7; ++var12) {
            int[] var14 = this.f(var3, var12 * 2, var2, var11, var11.length, var10.length, var8, var4, var9);
            byte var13 = 0;

            int var15;
            int var16;
            for(var15 = var10.length - 1; var15 >= 0; --var15) {
               var16 = var10[var15] + var14[var15] + var13;
               if (var16 < var4) {
                  var10[var15] = var16;
                  var13 = 0;
               } else {
                  var10[var15] = var16 - var4;
                  var13 = 1;
               }
            }

            var14 = this.f(var3, var12 * 2 + 1, var2, var10, var10.length, var11.length, var8, var4, var9);
            var13 = 0;

            for(var15 = var11.length - 1; var15 >= 0; --var15) {
               var16 = var11[var15] + var14[var15] + var13;
               if (var16 < var4) {
                  var11[var15] = var16;
                  var13 = 0;
               } else {
                  var11[var15] = var16 - var4;
                  var13 = 1;
               }
            }
         }

         String var10000 = this.valToDigit(var10, var4);
         return var10000 + this.valToDigit(var11, var4);
      } else {
         return "";
      }
   }

   public void bnAdd(int[] var1, int var2, int var3) {
      int var4 = var1.length - 1;

      for(int var5 = var3; var4 >= 0 && var5 > 0; --var4) {
         int var6 = var1[var4] + var5;
         var1[var4] = var6 % var2;
         var5 = (var6 - var1[var4]) / var2;
      }

   }
}
