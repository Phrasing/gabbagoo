package io.trickle.task.sites.walmart.util.encryption;

public class CMAC {
   public static int const_Rb = 135;

   public boolean msbNotZero(int var1) {
      return (var1 | Integer.MAX_VALUE) != Integer.MAX_VALUE;
   }

   public void leftShift(int[] var1) {
      var1[0] = (var1[0] & Integer.MAX_VALUE) << 1 | var1[1] >>> 31;
      var1[1] = (var1[1] & Integer.MAX_VALUE) << 1 | var1[2] >>> 31;
      var1[2] = (var1[2] & Integer.MAX_VALUE) << 1 | var1[3] >>> 31;
      var1[3] = (var1[3] & Integer.MAX_VALUE) << 1;
   }

   public int[] compute(Aes var1, String var2) {
      int[] var3 = new int[]{0, 0, 0, 0};
      int[] var4 = var1.encrypt(var3);
      int var5 = var4[0];
      this.leftShift(var4);
      if (this.msbNotZero(var5)) {
         var4[3] ^= 135;
      }

      int var6 = 0;

      while(var6 < var2.length()) {
         var3[var6 >> 2 & 3] ^= (Character.codePointAt(var2, var6) & 255) << 8 * (3 - (var6 & 3));
         ++var6;
         if ((var6 & 15) == 0 && var6 < var2.length()) {
            var3 = var1.encrypt(var3);
         }
      }

      if (var6 == 0 || (var6 & 15) != 0) {
         var5 = var4[0];
         this.leftShift(var4);
         if (this.msbNotZero(var5)) {
            var4[3] ^= 135;
         }

         var3[var6 >> 2 & 3] ^= 128 << 8 * (3 - (var6 & 3));
      }

      var3[0] ^= var4[0];
      var3[1] ^= var4[1];
      var3[2] ^= var4[2];
      var3[3] ^= var4[3];
      return var1.encrypt(var3);
   }
}
