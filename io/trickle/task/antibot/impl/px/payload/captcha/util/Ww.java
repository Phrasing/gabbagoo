package io.trickle.task.antibot.impl.px.payload.captcha.util;

import java.util.Arrays;

public class Ww {
   public long h2 = 1013904242L;
   public boolean first = true;
   public int[] Pt = new int[]{Integer.MIN_VALUE, 8388608, 32768, 128};
   public String[] At = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
   public boolean finalized = false;
   public int[] Lt = new int[]{24, 16, 8, 0};
   public int start = 0;
   public long h7 = 1541459225L;
   public long h5 = 2600822924L;
   public int[] blocks = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
   public int block = 0;
   public long h4 = 1359893119L;
   public long h3 = 2773480762L;
   public int hBytes = 0;
   public int lastByteIndex;
   public long bytes = 0L;
   public boolean hashed = false;
   public long h0 = 1779033703L;
   public long[] Jt = new long[]{1116352408L, 1899447441L, 3049323471L, 3921009573L, 961987163L, 1508970993L, 2453635748L, 2870763221L, 3624381080L, 310598401L, 607225278L, 1426881987L, 1925078388L, 2162078206L, 2614888103L, 3248222580L, 3835390401L, 4022224774L, 264347078L, 604807628L, 770255983L, 1249150122L, 1555081692L, 1996064986L, 2554220882L, 2821834349L, 2952996808L, 3210313671L, 3336571891L, 3584528711L, 113926993L, 338241895L, 666307205L, 773529912L, 1294757372L, 1396182291L, 1695183700L, 1986661051L, 2177026350L, 2456956037L, 2730485921L, 2820302411L, 3259730800L, 3345764771L, 3516065817L, 3600352804L, 4094571909L, 275423344L, 430227734L, 506948616L, 659060556L, 883997877L, 958139571L, 1322822218L, 1537002063L, 1747873779L, 1955562222L, 2024104815L, 2227730452L, 2361852424L, 2428436474L, 2756734187L, 3204031479L, 3329325298L};
   public long h1 = 3144134277L;
   public long h6 = 528734635L;

   public void hash() {
      long var1 = this.h0;
      long var3 = this.h1;
      long var5 = this.h2;
      long var7 = this.h3;
      long var9 = this.h4;
      long var11 = this.h5;
      long var13 = this.h6;
      long var15 = this.h7;
      long var18 = 0L;
      long var20 = 0L;
      long var22 = 0L;
      long var24 = 0L;
      long var26 = 0L;
      long var28 = 0L;
      long var30 = 0L;
      long var32 = 0L;
      int[] var34 = this.blocks;

      int var17;
      for(var17 = 16; var17 < 64; ++var17) {
         var18 = (long)((rightTripleBitwise(var24 = (long)var34[var17 - 15], 7L) | leftBitwise(var24, 25L)) ^ (rightTripleBitwise(var24, 18L) | leftBitwise(var24, 14L)) ^ rightTripleBitwise(var24, 3L));
         var20 = (long)((rightTripleBitwise(var24 = (long)var34[var17 - 2], 17L) | leftBitwise(var24, 15L)) ^ (rightTripleBitwise(var24, 19L) | leftBitwise(var24, 13L)) ^ rightTripleBitwise(var24, 10L));
         if (var17 == var34.length) {
            var34 = Arrays.copyOf(var34, var34.length + 1);
         }

         var34[var17] = (int)((long)var34[var17 - 16] + var18 + (long)var34[var17 - 7] + var20);
      }

      this.blocks = var34;
      var32 = (long)((int)var3 & (int)var5);

      for(var17 = 0; var17 < 64; var17 += 4) {
         if (this.first) {
            var26 = 704751109L;
            var15 = (long)((int)((var24 = (long)(var34[0] - 210244248)) - 1521486534L));
            var7 = (long)((int)(var24 + 143694565L));
            this.first = false;
         } else {
            var18 = (long)((rightTripleBitwise(var1, 2L) | leftBitwise(var1, 30L)) ^ (rightTripleBitwise(var1, 13L) | leftBitwise(var1, 19L)) ^ (rightTripleBitwise(var1, 22L) | leftBitwise(var1, 10L)));
            var22 = (var26 = (long)((int)var1 & (int)var3)) ^ (long)((int)var1 & (int)var5) ^ (long)((int)var32);
            var15 = (long)((int)(var7 + (var24 = var15 + (long)((rightTripleBitwise(var9, 6L) | leftBitwise(var9, 26L)) ^ (rightTripleBitwise(var9, 11L) | leftBitwise(var9, 21L)) ^ (rightTripleBitwise(var9, 25L) | leftBitwise(var9, 7L))) + (long)((int)var9 & (int)var11 ^ (int)(~var9) & (int)var13) + this.Jt[var17] + (long)var34[var17])));
            var7 = (long)((int)(var24 + var18 + var22));
         }

         var18 = (long)((rightTripleBitwise(var7, 2L) | leftBitwise(var7, 30L)) ^ (rightTripleBitwise(var7, 13L) | leftBitwise(var7, 19L)) ^ (rightTripleBitwise(var7, 22L) | leftBitwise(var7, 10L)));
         var22 = (var28 = (long)((int)var7 & (int)var1)) ^ (long)((int)var7 & (int)var3) ^ (long)((int)var26);
         var13 = (long)((int)(var5 + (var24 = var13 + (long)((rightTripleBitwise(var15, 6L) | leftBitwise(var15, 26L)) ^ (rightTripleBitwise(var15, 11L) | leftBitwise(var15, 21L)) ^ (rightTripleBitwise(var15, 25L) | leftBitwise(var15, 7L))) + (long)((int)var15 & (int)var9 ^ (int)(~var15) & (int)var11) + this.Jt[var17 + 1] + (long)var34[var17 + 1])));
         var18 = (long)((rightTripleBitwise(var5 = (long)((int)(var24 + var18 + var22)), 2L) | leftBitwise(var5, 30L)) ^ (rightTripleBitwise(var5, 13L) | leftBitwise(var5, 19L)) ^ (rightTripleBitwise(var5, 22L) | leftBitwise(var5, 10L)));
         var22 = (var30 = (long)((int)var5 & (int)var7)) ^ (long)((int)var5 & (int)var1) ^ (long)((int)var28);
         var11 = (long)((int)(var3 + (var24 = var11 + (long)((rightTripleBitwise(var13, 6L) | leftBitwise(var13, 26L)) ^ (rightTripleBitwise(var13, 11L) | leftBitwise(var13, 21L)) ^ (rightTripleBitwise(var13, 25L) | leftBitwise(var13, 7L))) + (long)((int)var13 & (int)var15 ^ (int)(~var13) & (int)var9) + this.Jt[var17 + 2] + (long)var34[var17 + 2])));
         var18 = (long)((rightTripleBitwise(var3 = (long)((int)(var24 + var18 + var22)), 2L) | leftBitwise(var3, 30L)) ^ (rightTripleBitwise(var3, 13L) | leftBitwise(var3, 19L)) ^ (rightTripleBitwise(var3, 22L) | leftBitwise(var3, 10L)));
         var22 = (var32 = (long)((int)var3 & (int)var5)) ^ (long)((int)var3 & (int)var7) ^ (long)((int)var30);
         var9 = (long)((int)(var1 + (var24 = var9 + (long)((rightTripleBitwise(var11, 6L) | leftBitwise(var11, 26L)) ^ (rightTripleBitwise(var11, 11L) | leftBitwise(var11, 21L)) ^ (rightTripleBitwise(var11, 25L) | leftBitwise(var11, 7L))) + (long)((int)var11 & (int)var13 ^ (int)(~var11) & (int)var15) + this.Jt[var17 + 3] + (long)var34[var17 + 3])));
         var1 = (long)((int)(var24 + var18 + var22));
      }

      this.h0 = (long)((int)(this.h0 + var1));
      this.h1 = (long)((int)(this.h1 + var3));
      this.h2 = (long)((int)(this.h2 + var5));
      this.h3 = (long)((int)(this.h3 + var7));
      this.h4 = (long)((int)(this.h4 + var9));
      this.h5 = (long)((int)(this.h5 + var11));
      this.h6 = (long)((int)(this.h6 + var13));
      this.h7 = (long)((int)(this.h7 + var15));
   }

   public String hex() {
      this.finalizeEncryption();
      long var1 = this.h0;
      long var3 = this.h1;
      long var5 = this.h2;
      long var7 = this.h3;
      long var9 = this.h4;
      long var11 = this.h5;
      long var13 = this.h6;
      long var15 = this.h7;
      String var10000 = this.At[rightBitwise(var1, 28L) & 15];
      return var10000 + this.At[rightBitwise(var1, 24L) & 15] + this.At[rightBitwise(var1, 20L) & 15] + this.At[rightBitwise(var1, 16L) & 15] + this.At[rightBitwise(var1, 12L) & 15] + this.At[rightBitwise(var1, 8L) & 15] + this.At[rightBitwise(var1, 4L) & 15] + this.At[(int)(15L & var1)] + this.At[rightBitwise(var3, 28L) & 15] + this.At[(int)var3 >> 24 & 15] + this.At[(int)var3 >> 20 & 15] + this.At[(int)var3 >> 16 & 15] + this.At[(int)var3 >> 12 & 15] + this.At[rightBitwise(var3, 8L) & 15] + this.At[rightBitwise(var3, 4L) & 15] + this.At[(int)(15L & var3)] + this.At[rightBitwise(var5, 28L) & 15] + this.At[rightBitwise(var5, 24L) & 15] + this.At[rightBitwise(var5, 20L) & 15] + this.At[rightBitwise(var5, 16L) & 15] + this.At[rightBitwise(var5, 12L) & 15] + this.At[rightBitwise(var5, 8L) & 15] + this.At[rightBitwise(var5, 4L) & 15] + this.At[(int)(15L & var5)] + this.At[(int)var7 >> 28 & 15] + this.At[(int)var7 >> 24 & 15] + this.At[(int)var7 >> 20 & 15] + this.At[(int)var7 >> 16 & 15] + this.At[(int)var7 >> 12 & 15] + this.At[(int)var7 >> 8 & 15] + this.At[(int)var7 >> 4 & 15] + this.At[15 & (int)var7] + this.At[rightBitwise(var9, 28L) & 15] + this.At[rightBitwise(var9, 24L) & 15] + this.At[rightBitwise(var9, 20L) & 15] + this.At[rightBitwise(var9, 16L) & 15] + this.At[rightBitwise(var9, 12L) & 15] + this.At[rightBitwise(var9, 8L) & 15] + this.At[rightBitwise(var9, 4L) & 15] + this.At[(int)(15L & var9)] + this.At[(int)var11 >> 28 & 15] + this.At[(int)var11 >> 24 & 15] + this.At[(int)var11 >> 20 & 15] + this.At[(int)var11 >> 16 & 15] + this.At[(int)var11 >> 12 & 15] + this.At[(int)var11 >> 8 & 15] + this.At[(int)var11 >> 4 & 15] + this.At[15 & (int)var11] + this.At[rightBitwise(var13, 28L) & 15] + this.At[rightBitwise(var13, 24L) & 15] + this.At[rightBitwise(var13, 20L) & 15] + this.At[rightBitwise(var13, 16L) & 15] + this.At[rightBitwise(var13, 12L) & 15] + this.At[rightBitwise(var13, 8L) & 15] + this.At[rightBitwise(var13, 4L) & 15] + this.At[(int)(15L & var13)] + this.At[rightBitwise(var15, 28L) & 15] + this.At[rightBitwise(var15, 24L) & 15] + this.At[rightBitwise(var15, 20L) & 15] + this.At[rightBitwise(var15, 16L) & 15] + this.At[rightBitwise(var15, 12L) & 15] + this.At[rightBitwise(var15, 8L) & 15] + this.At[rightBitwise(var15, 4L) & 15] + this.At[(int)(15L & var15)];
   }

   public static int rightBitwise(long var0, long var2) {
      if (var0 < 2147483647L && var2 < 2147483647L) {
         return (int)var0 >> (int)var2;
      } else if (var0 < 2147483647L) {
         return (int)var0 >> (int)var2;
      } else {
         return var2 < 2147483647L ? (int)(var0 >> (int)var2) : (int)(var0 >> (int)var2);
      }
   }

   public Ww update(String var1) {
      int var3 = 0;
      boolean var4 = false;
      int var5 = var1.length();
      int[] var6 = this.blocks;

      while(var3 < var5) {
         if (this.hashed) {
            this.hashed = false;
            var6[0] = this.block;

            for(int var7 = 0; var7 < 17; ++var7) {
               var6[var7] = 0;
            }
         }

         int var8;
         for(var8 = this.start; var3 < var5 && var8 < 64; ++var3) {
            int var2 = Character.codePointAt(var1, var3);
            int var10001;
            if (var2 < 128) {
               var10001 = rightBitwise((long)var8, 2L);
               var6[var10001] |= leftBitwise((long)var2, (long)this.Lt[3 & var8++]);
            } else if (var2 < 2048) {
               var6[var8 >> 2] |= (192 | var2 >> 6) << this.Lt[3 & var8++];
               var6[var8 >> 2] |= (128 | 63 & var2) << this.Lt[3 & var8++];
            } else if (var2 >= 55296 && var2 < 57344) {
               var10001 = (1023 & var2) << 10;
               ++var3;
               var2 = 65536 + (var10001 | 1023 & Character.codePointAt(var1, var3));
               var6[var8 >> 2] |= (240 | var2 >> 18) << this.Lt[3 & var8++];
               var6[var8 >> 2] |= (128 | var2 >> 12 & 63) << this.Lt[3 & var8++];
               var6[var8 >> 2] |= (128 | var2 >> 6 & 63) << this.Lt[3 & var8++];
               var6[var8 >> 2] |= (128 | 63 & var2) << this.Lt[3 & var8++];
            } else {
               var6[var8 >> 2] |= (224 | var2 >> 12) << this.Lt[3 & var8++];
               var6[var8 >> 2] |= (128 | var2 >> 6 & 63) << this.Lt[3 & var8++];
               var6[var8 >> 2] |= (128 | 63 & var2) << this.Lt[3 & var8++];
            }
         }

         this.lastByteIndex = var8;
         this.bytes += (long)(var8 - this.start);
         if (var8 >= 64) {
            this.block = var6[16];
            this.start = var8 - 64;
            this.hash();
            this.hashed = true;
         } else {
            this.start = var8;
         }
      }

      if (this.bytes > 4294967295L) {
         this.hBytes = (int)((long)this.hBytes + this.bytes / 4294967296L);
         this.bytes %= 4294967296L;
      }

      return this;
   }

   public static int rightTripleBitwise(long var0, long var2) {
      if (var0 < 2147483647L && var2 < 2147483647L) {
         return (int)var0 >>> (int)var2;
      } else if (var0 < 2147483647L) {
         return (int)var0 >>> (int)var2;
      } else {
         return var2 < 2147483647L ? (int)(var0 >>> (int)var2) : (int)(var0 >>> (int)var2);
      }
   }

   public static int leftBitwise(long var0, long var2) {
      if (var0 < 2147483647L && var2 < 2147483647L) {
         return (int)var0 << (int)var2;
      } else if (var0 < 2147483647L) {
         return (int)var0 << (int)var2;
      } else {
         return var2 < 2147483647L ? (int)(var0 << (int)var2) : (int)(var0 << (int)var2);
      }
   }

   public void finalizeEncryption() {
      if (!this.finalized) {
         this.finalized = true;
         int[] var1 = this.blocks;
         int var2 = this.lastByteIndex;
         var1[16] = this.block;
         var1[var2 >> 2] |= this.Pt[3 & var2];
         this.block = var1[16];
         if (var2 >= 56) {
            if (!this.hashed) {
               this.hash();
            }

            var1[0] = this.block;
            var1[16] = var1[1] = var1[2] = var1[3] = var1[4] = var1[5] = var1[6] = var1[7] = var1[8] = var1[9] = var1[10] = var1[11] = var1[12] = var1[13] = var1[14] = var1[15] = 0;
         }

         var1[14] = leftBitwise((long)this.hBytes, 3L) | rightTripleBitwise(this.bytes, 29L);
         var1[15] = leftBitwise(this.bytes, 3L);
         this.hash();
      }

   }
}
