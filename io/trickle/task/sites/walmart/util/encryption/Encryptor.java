package io.trickle.task.sites.walmart.util.encryption;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class Encryptor {
   public String PIE_KEY_ID;
   public int PIE_E;
   public Aes encryptionObj;
   public int PIE_L;
   public static String base10 = "0123456789";
   public String PIE_K;
   public int PIE_PHASE;

   public static String lambda$parse$1(String var0) {
      return var0.replace(" ", "");
   }

   public Aes hexToKey(String var1) {
      long[] var2 = hexToWords(var1);
      this.encryptionObj = new Aes();
      this.encryptionObj.precompute();
      return this.encryptionObj.cipher(var2);
   }

   public static String strip(String var0, String var1) {
      return var0.replace("PIE." + var1, "").replace("=", "").replace("\"", "");
   }

   public static String[] prepareAndEncrypt(String var0, String var1, String var2) {
      Objects.requireNonNull(var1);
      Objects.requireNonNull(var2);
      String[] var3 = parse(var0);
      if (var3.length == 5) {
         try {
            return (new Encryptor(var3[0], var3[1], var3[2], var3[3], var3[4])).protectPANandCVV(var1, var2, true);
         } catch (Exception var5) {
            var5.printStackTrace();
            return null;
         }
      } else {
         return encrypt(var1, var2);
      }
   }

   public Encryptor() {
      this.PIE_L = 6;
      this.PIE_E = 4;
      this.PIE_K = "70E7D02F58E2D0091D2AC30D65103EAE";
      this.PIE_KEY_ID = "1bb96826";
      this.PIE_PHASE = 0;
   }

   public static boolean lambda$parse$0(String var0) {
      return var0.contains("PIE");
   }

   public static void main(String[] var0) {
      for(int var1 = 0; var1 < 100; ++var1) {
         String[] var2 = (new Encryptor()).protectPANandCVV("1122334455667788", "224", true);
         System.out.println(Arrays.toString(var2));
      }

   }

   public String encrypt(String var1, String var2, String var3, int var4) {
      Aes var5 = this.hexToKey(var3);
      if (var5 == null) {
         return "";
      } else {
         FFX var6 = new FFX(var5);
         return var6.encryptWithCipher(var1, var2, var5, var4);
      }
   }

   public static String distill(String var0) {
      StringBuilder var1 = new StringBuilder();

      for(int var2 = 0; var2 < var0.length(); ++var2) {
         String var10000 = base10;
         char var10001 = var0.charAt(var2);
         if (var10000.contains("" + var10001)) {
            var1.append(var0.charAt(var2));
         }
      }

      return var1.toString();
   }

   public static String[] encrypt(String var0, String var1) {
      Objects.requireNonNull(var0);
      Objects.requireNonNull(var1);
      return (new Encryptor()).protectPANandCVV(var0, var1, true);
   }

   public static int luhn(String var0) {
      int var1 = var0.length() - 1;

      int var2;
      for(var2 = 0; var1 >= 0; var1 -= 2) {
         var2 += Integer.parseInt(String.valueOf(var0.charAt(var1)));
      }

      for(var1 = var0.length() - 2; var1 >= 0; var1 -= 2) {
         int var3 = 2 * Integer.parseInt(String.valueOf(var0.charAt(var1)));
         if (var3 < 10) {
            var2 += var3;
         } else {
            var2 += var3 - 9;
         }
      }

      return var2 % 10;
   }

   public Encryptor(String var1, String var2, String var3, String var4, String var5) {
      this.PIE_L = Integer.parseInt(var1);
      this.PIE_E = Integer.parseInt(var2);
      this.PIE_K = var3;
      this.PIE_KEY_ID = var4;
      this.PIE_PHASE = Integer.parseInt(var5);
   }

   public static String[] parse(String var0) {
      List var1 = (List)var0.lines().filter(Encryptor::lambda$parse$0).map(Encryptor::lambda$parse$1).map(Encryptor::lambda$parse$2).collect(Collectors.toList());
      String[] var2 = new String[5];
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         String var5;
         if (var4.contains("PIE.L")) {
            var5 = strip(var4, "L");
            var2[0] = var5;
         } else if (var4.contains("PIE.E")) {
            var5 = strip(var4, "E");
            var2[1] = var5;
         } else if (var4.contains("PIE.K")) {
            var5 = strip(var4, "K");
            var2[2] = var5;
         } else if (var4.contains("PIE.key_id")) {
            var5 = strip(var4, "key_id");
            var2[3] = var5;
         } else if (var4.contains("PIE.phase")) {
            var5 = strip(var4, "phase");
            var2[4] = var5;
         }
      }

      return var2;
   }

   public static long[] hexToWords(String var0) {
      byte var1 = 4;
      long[] var2 = new long[var1];
      if (var0.length() != var1 * 8) {
         return null;
      } else {
         for(int var3 = 0; var3 < var1; ++var3) {
            var2[var3] = Long.parseLong(substr(var0, var3 * 8, 8), 16);
         }

         return var2;
      }
   }

   public static String substr(String var0, int var1, int var2) {
      if (var1 > var0.length()) {
         return "";
      } else {
         var0 = var0.substring(var1);
         if (var2 > var0.length()) {
            return var0;
         } else {
            var0 = var0.substring(0, var2);
            return var0;
         }
      }
   }

   public static String lambda$parse$2(String var0) {
      return var0.replace(";", "");
   }

   public String[] protectPANandCVV(String var1, String var2, boolean var3) {
      String var4 = distill(var1);
      String var5 = distill(var2);
      String var10000 = var4.substring(0, this.PIE_L);
      String var6 = var10000 + var4.substring(var4.length() - this.PIE_E);
      if (var3) {
         int var7 = luhn(var4);
         String var8 = var4.substring(this.PIE_L + 1, var4.length() - this.PIE_E);
         String var9 = this.encrypt(var8 + var5, var6, this.PIE_K, 10);
         var10000 = var4.substring(0, this.PIE_L);
         String var10 = var10000 + "0" + var9.substring(0, var9.length() - var5.length()) + var4.substring(var4.length() - this.PIE_E);
         SDW var11 = new SDW(this.encryptionObj);
         String var12 = var11.fixluhn(var10, this.PIE_L, var7);
         String var13 = var11.reformat(var12, var1);
         String var14 = var11.reformat(var9.substring(var9.length() - var5.length()), var2);
         return new String[]{var13, var14, var11.integrity(this.PIE_K, var13, var14), this.PIE_KEY_ID, String.valueOf(this.PIE_PHASE), UUID.randomUUID().toString()};
      } else {
         return null;
      }
   }
}
