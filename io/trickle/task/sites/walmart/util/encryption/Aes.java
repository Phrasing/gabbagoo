package io.trickle.task.sites.walmart.util.encryption;

import java.util.Arrays;

public class Aes {
   public int[][][] tables;
   public int b;
   public int[] n;
   public int m;
   public int f;
   public int e;
   public int[][] key;
   public int[] c;
   public int l;
   public int[] k;
   public int a;
   public int o;
   public int[][] j;
   public int g;
   public int[] h;
   public int[][] q;
   public int p;

   public static int lambda$cipher$0(long var0) {
      return (int)var0;
   }

   public Aes precompute() {
      this.tables = new int[2][5][256];
      this.j = this.tables[0];
      this.q = this.tables[1];
      this.h = this.j[4];
      this.n = this.q[4];
      this.k = new int[256];
      this.c = new int[256];
      this.b = -1;

      for(this.g = 0; this.g < 256; this.c[(this.k[this.g] = this.g << 1 ^ (this.g >> 7) * 283) ^ this.g] = this.g++) {
      }

      for(this.l = this.f = 0; this.h[this.l] == 0; this.f = this.c[this.f] == 0 ? 1 : this.c[this.f]) {
         this.o = this.f ^ this.f << 1 ^ this.f << 2 ^ this.f << 3 ^ this.f << 4;
         this.o = this.o >> 8 ^ this.o & 255 ^ 99;
         this.h[this.l] = this.o;
         this.n[this.o] = this.l;
         this.m = this.k[this.p = this.k[this.b = this.k[this.l]]];
         this.a = this.m * 16843009 ^ this.p * 65537 ^ this.b * 257 ^ this.l * 16843008;
         this.e = this.k[this.o] * 257 ^ this.o * 16843008;

         for(this.g = 0; this.g < 4; ++this.g) {
            this.j[this.g][this.l] = this.e = this.e << 24 ^ this.e >>> 8;
            this.q[this.g][this.o] = this.a = this.a << 24 ^ this.a >>> 8;
         }

         this.l ^= this.b == 0 ? 1 : this.b;
      }

      for(this.g = 0; this.g < 5; ++this.g) {
         this.j[this.g] = Arrays.copyOf(this.j[this.g], this.j[this.g].length);
         this.q[this.g] = Arrays.copyOf(this.q[this.g], this.q[this.g].length);
      }

      return this;
   }

   public static int[] removeElements(int[] var0, int var1) {
      int var2 = 0;

      for(int var3 = 0; var3 < var0.length; ++var3) {
         if (var0[var3] != var1) {
            var0[var2++] = var0[var3];
         }
      }

      return Arrays.copyOf(var0, var2);
   }

   public int[] encrypt(int[] var1) {
      return this._crypt(var1, 0);
   }

   public static long[] removeElements(long[] var0, int var1) {
      int var2 = 0;

      for(int var3 = 0; var3 < var0.length; ++var3) {
         if (var0[var3] != (long)var1) {
            var0[var2++] = var0[var3];
         }
      }

      return Arrays.copyOf(var0, var2);
   }

   public int[] _crypt(int[] var1, int var2) {
      if (var1.length != 4) {
         System.err.println("Something went wrong with crypting profile.");
      }

      int[] var3 = this.key[var2];
      int var4 = var1[0] ^ var3[0];
      int var5 = var1[var2 != 0 ? 3 : 1] ^ var3[1];
      int var6 = var1[2] ^ var3[2];
      int var7 = var1[var2 != 0 ? 1 : 3] ^ var3[3];
      int var11 = var3.length / 4 - 2;
      int var13 = 4;
      int[] var14 = new int[]{0, 0, 0, 0};
      int[][] var15 = this.tables[var2];
      int[] var16 = var15[0];
      int[] var17 = var15[1];
      int[] var18 = var15[2];
      int[] var19 = var15[3];
      int[] var20 = var15[4];

      int var8;
      int var12;
      for(var12 = 0; var12 < var11; ++var12) {
         var8 = var16[var4 >>> 24] ^ var17[var5 >> 16 & 255] ^ var18[var6 >> 8 & 255] ^ var19[var7 & 255] ^ var3[var13];
         int var9 = var16[var5 >>> 24] ^ var17[var6 >> 16 & 255] ^ var18[var7 >> 8 & 255] ^ var19[var4 & 255] ^ var3[var13 + 1];
         int var10 = var16[var6 >>> 24] ^ var17[var7 >> 16 & 255] ^ var18[var4 >> 8 & 255] ^ var19[var5 & 255] ^ var3[var13 + 2];
         var7 = var16[var7 >>> 24] ^ var17[var4 >> 16 & 255] ^ var18[var5 >> 8 & 255] ^ var19[var6 & 255] ^ var3[var13 + 3];
         var13 += 4;
         var4 = var8;
         var5 = var9;
         var6 = var10;
      }

      for(var12 = 0; var12 < 4; ++var12) {
         var14[var2 != 0 ? 3 & -var12 : var12] = var20[var4 >>> 24] << 24 ^ var20[var5 >> 16 & 255] << 16 ^ var20[var6 >> 8 & 255] << 8 ^ var20[var7 & 255] ^ var3[var13++];
         var8 = var4;
         var4 = var5;
         var5 = var6;
         var6 = var7;
         var7 = var8;
      }

      return var14;
   }

   public Aes cipher(long[] var1) {
      int[] var7 = this.tables[0][4];
      int[][] var8 = this.tables[1];
      this.a = var1.length;
      this.b = 1;
      if (this.a != 4 && this.a != 6 && this.a != 8) {
         System.err.println("invalid aes key size");
      }

      long[] var5 = Arrays.copyOf(var1, 256);
      int[] var6 = new int[256];

      int var2;
      int var4;
      for(var2 = this.a; var2 < 4 * this.a + 28; ++var2) {
         var4 = (int)var5[var2 - 1];
         if (var2 % this.a == 0 || this.a == 8 && var2 % this.a == 4) {
            var4 = var7[var4 >>> 24] << 24 ^ var7[var4 >> 16 & 255] << 16 ^ var7[var4 >> 8 & 255] << 8 ^ var7[var4 & 255];
            if (var2 % this.a == 0) {
               var4 = var4 << 8 ^ var4 >>> 24 ^ this.b << 24;
               this.b = this.b << 1 ^ (this.b >> 7) * 283;
            }
         }

         var5[var2] = var5[var2 - this.a] ^ (long)var4;
      }

      var5 = removeElements((long[])var5, 0);

      for(int var3 = 0; var2 != 0; --var2) {
         var4 = (int)var5[(var3 & 3) != 0 ? var2 : var2 - 4];
         if (var2 > 4 && var3 >= 4) {
            var6[var3] = var8[0][var7[var4 >>> 24]] ^ var8[1][var7[var4 >> 16 & 255]] ^ var8[2][var7[var4 >> 8 & 255]] ^ var8[3][var7[var4 & 255]];
         } else {
            var6[var3] = var4;
         }

         ++var3;
      }

      var6 = removeElements((int[])var6, 0);
      this.key = new int[][]{Arrays.stream(var5).mapToInt(Aes::lambda$cipher$0).toArray(), var6};
      return this;
   }
}
