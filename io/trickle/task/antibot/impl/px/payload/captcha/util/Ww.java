/*
 * Decompiled with CFR 0.152.
 */
package io.trickle.task.antibot.impl.px.payload.captcha.util;

import java.util.Arrays;

public class Ww {
    public long h2 = 1013904242L;
    public boolean first = true;
    public int[] Pt;
    public String[] At;
    public boolean finalized = false;
    public int[] Lt = new int[]{24, 16, 8, 0};
    public int start = 0;
    public long h7 = 1541459225L;
    public long h5 = 2600822924L;
    public int[] blocks;
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
        int n;
        long l = this.h0;
        long l2 = this.h1;
        long l3 = this.h2;
        long l4 = this.h3;
        long l5 = this.h4;
        long l6 = this.h5;
        long l7 = this.h6;
        long l8 = this.h7;
        long l9 = 0L;
        long l10 = 0L;
        long l11 = 0L;
        long l12 = 0L;
        long l13 = 0L;
        long l14 = 0L;
        long l15 = 0L;
        long l16 = 0L;
        int[] nArray = this.blocks;
        for (n = 16; n < 64; ++n) {
            l12 = nArray[n - 15];
            l9 = (Ww.rightTripleBitwise(l12, 7L) | Ww.leftBitwise(l12, 25L)) ^ (Ww.rightTripleBitwise(l12, 18L) | Ww.leftBitwise(l12, 14L)) ^ Ww.rightTripleBitwise(l12, 3L);
            l12 = nArray[n - 2];
            l10 = (Ww.rightTripleBitwise(l12, 17L) | Ww.leftBitwise(l12, 15L)) ^ (Ww.rightTripleBitwise(l12, 19L) | Ww.leftBitwise(l12, 13L)) ^ Ww.rightTripleBitwise(l12, 10L);
            if (n == nArray.length) {
                nArray = Arrays.copyOf(nArray, nArray.length + 1);
            }
            nArray[n] = (int)((long)nArray[n - 16] + l9 + (long)nArray[n - 7] + l10);
        }
        this.blocks = nArray;
        l16 = (int)l2 & (int)l3;
        n = 0;
        while (true) {
            if (n >= 64) {
                this.h0 = (int)(this.h0 + l);
                this.h1 = (int)(this.h1 + l2);
                this.h2 = (int)(this.h2 + l3);
                this.h3 = (int)(this.h3 + l4);
                this.h4 = (int)(this.h4 + l5);
                this.h5 = (int)(this.h5 + l6);
                this.h6 = (int)(this.h6 + l7);
                this.h7 = (int)(this.h7 + l8);
                return;
            }
            if (this.first) {
                l13 = 704751109L;
                l12 = nArray[0] - 210244248;
                l8 = (int)(l12 - 1521486534L);
                l4 = (int)(l12 + 143694565L);
                this.first = false;
            } else {
                l9 = (Ww.rightTripleBitwise(l, 2L) | Ww.leftBitwise(l, 30L)) ^ (Ww.rightTripleBitwise(l, 13L) | Ww.leftBitwise(l, 19L)) ^ (Ww.rightTripleBitwise(l, 22L) | Ww.leftBitwise(l, 10L));
                l13 = (int)l & (int)l2;
                l11 = l13 ^ (long)((int)l & (int)l3) ^ (long)((int)l16);
                l10 = (Ww.rightTripleBitwise(l5, 6L) | Ww.leftBitwise(l5, 26L)) ^ (Ww.rightTripleBitwise(l5, 11L) | Ww.leftBitwise(l5, 21L)) ^ (Ww.rightTripleBitwise(l5, 25L) | Ww.leftBitwise(l5, 7L));
                l12 = l8 + l10 + (long)((int)l5 & (int)l6 ^ (int)(l5 ^ 0xFFFFFFFFFFFFFFFFL) & (int)l7) + this.Jt[n] + (long)nArray[n];
                l8 = (int)(l4 + l12);
                l4 = (int)(l12 + (l9 + l11));
            }
            l9 = (Ww.rightTripleBitwise(l4, 2L) | Ww.leftBitwise(l4, 30L)) ^ (Ww.rightTripleBitwise(l4, 13L) | Ww.leftBitwise(l4, 19L)) ^ (Ww.rightTripleBitwise(l4, 22L) | Ww.leftBitwise(l4, 10L));
            l14 = (int)l4 & (int)l;
            l11 = l14 ^ (long)((int)l4 & (int)l2) ^ (long)((int)l13);
            l10 = (Ww.rightTripleBitwise(l8, 6L) | Ww.leftBitwise(l8, 26L)) ^ (Ww.rightTripleBitwise(l8, 11L) | Ww.leftBitwise(l8, 21L)) ^ (Ww.rightTripleBitwise(l8, 25L) | Ww.leftBitwise(l8, 7L));
            l12 = l7 + l10 + (long)((int)l8 & (int)l5 ^ (int)(l8 ^ 0xFFFFFFFFFFFFFFFFL) & (int)l6) + this.Jt[n + 1] + (long)nArray[n + 1];
            l7 = (int)(l3 + l12);
            l3 = (int)(l12 + (l9 + l11));
            l9 = (Ww.rightTripleBitwise(l3, 2L) | Ww.leftBitwise(l3, 30L)) ^ (Ww.rightTripleBitwise(l3, 13L) | Ww.leftBitwise(l3, 19L)) ^ (Ww.rightTripleBitwise(l3, 22L) | Ww.leftBitwise(l3, 10L));
            l15 = (int)l3 & (int)l4;
            l11 = l15 ^ (long)((int)l3 & (int)l) ^ (long)((int)l14);
            l12 = l6 + (long)((Ww.rightTripleBitwise(l7, 6L) | Ww.leftBitwise(l7, 26L)) ^ (Ww.rightTripleBitwise(l7, 11L) | Ww.leftBitwise(l7, 21L)) ^ (Ww.rightTripleBitwise(l7, 25L) | Ww.leftBitwise(l7, 7L))) + (long)((int)l7 & (int)l8 ^ (int)(l7 ^ 0xFFFFFFFFFFFFFFFFL) & (int)l5) + this.Jt[n + 2] + (long)nArray[n + 2];
            l6 = (int)(l2 + l12);
            l2 = (int)(l12 + (l9 + l11));
            l9 = (Ww.rightTripleBitwise(l2, 2L) | Ww.leftBitwise(l2, 30L)) ^ (Ww.rightTripleBitwise(l2, 13L) | Ww.leftBitwise(l2, 19L)) ^ (Ww.rightTripleBitwise(l2, 22L) | Ww.leftBitwise(l2, 10L));
            l16 = (int)l2 & (int)l3;
            l11 = l16 ^ (long)((int)l2 & (int)l4) ^ (long)((int)l15);
            l10 = (Ww.rightTripleBitwise(l6, 6L) | Ww.leftBitwise(l6, 26L)) ^ (Ww.rightTripleBitwise(l6, 11L) | Ww.leftBitwise(l6, 21L)) ^ (Ww.rightTripleBitwise(l6, 25L) | Ww.leftBitwise(l6, 7L));
            l12 = l5 + l10 + (long)((int)l6 & (int)l7 ^ (int)(l6 ^ 0xFFFFFFFFFFFFFFFFL) & (int)l8) + this.Jt[n + 3] + (long)nArray[n + 3];
            l5 = (int)(l + l12);
            l = (int)(l12 + (l9 + l11));
            n += 4;
        }
    }

    public String hex() {
        this.finalizeEncryption();
        long l = this.h0;
        long l2 = this.h1;
        long l3 = this.h2;
        long l4 = this.h3;
        long l5 = this.h4;
        long l6 = this.h5;
        long l7 = this.h6;
        long l8 = this.h7;
        return this.At[Ww.rightBitwise(l, 28L) & 0xF] + this.At[Ww.rightBitwise(l, 24L) & 0xF] + this.At[Ww.rightBitwise(l, 20L) & 0xF] + this.At[Ww.rightBitwise(l, 16L) & 0xF] + this.At[Ww.rightBitwise(l, 12L) & 0xF] + this.At[Ww.rightBitwise(l, 8L) & 0xF] + this.At[Ww.rightBitwise(l, 4L) & 0xF] + this.At[(int)(0xFL & l)] + this.At[Ww.rightBitwise(l2, 28L) & 0xF] + this.At[(int)l2 >> 24 & 0xF] + this.At[(int)l2 >> 20 & 0xF] + this.At[(int)l2 >> 16 & 0xF] + this.At[(int)l2 >> 12 & 0xF] + this.At[Ww.rightBitwise(l2, 8L) & 0xF] + this.At[Ww.rightBitwise(l2, 4L) & 0xF] + this.At[(int)(0xFL & l2)] + this.At[Ww.rightBitwise(l3, 28L) & 0xF] + this.At[Ww.rightBitwise(l3, 24L) & 0xF] + this.At[Ww.rightBitwise(l3, 20L) & 0xF] + this.At[Ww.rightBitwise(l3, 16L) & 0xF] + this.At[Ww.rightBitwise(l3, 12L) & 0xF] + this.At[Ww.rightBitwise(l3, 8L) & 0xF] + this.At[Ww.rightBitwise(l3, 4L) & 0xF] + this.At[(int)(0xFL & l3)] + this.At[(int)l4 >> 28 & 0xF] + this.At[(int)l4 >> 24 & 0xF] + this.At[(int)l4 >> 20 & 0xF] + this.At[(int)l4 >> 16 & 0xF] + this.At[(int)l4 >> 12 & 0xF] + this.At[(int)l4 >> 8 & 0xF] + this.At[(int)l4 >> 4 & 0xF] + this.At[0xF & (int)l4] + this.At[Ww.rightBitwise(l5, 28L) & 0xF] + this.At[Ww.rightBitwise(l5, 24L) & 0xF] + this.At[Ww.rightBitwise(l5, 20L) & 0xF] + this.At[Ww.rightBitwise(l5, 16L) & 0xF] + this.At[Ww.rightBitwise(l5, 12L) & 0xF] + this.At[Ww.rightBitwise(l5, 8L) & 0xF] + this.At[Ww.rightBitwise(l5, 4L) & 0xF] + this.At[(int)(0xFL & l5)] + this.At[(int)l6 >> 28 & 0xF] + this.At[(int)l6 >> 24 & 0xF] + this.At[(int)l6 >> 20 & 0xF] + this.At[(int)l6 >> 16 & 0xF] + this.At[(int)l6 >> 12 & 0xF] + this.At[(int)l6 >> 8 & 0xF] + this.At[(int)l6 >> 4 & 0xF] + this.At[0xF & (int)l6] + this.At[Ww.rightBitwise(l7, 28L) & 0xF] + this.At[Ww.rightBitwise(l7, 24L) & 0xF] + this.At[Ww.rightBitwise(l7, 20L) & 0xF] + this.At[Ww.rightBitwise(l7, 16L) & 0xF] + this.At[Ww.rightBitwise(l7, 12L) & 0xF] + this.At[Ww.rightBitwise(l7, 8L) & 0xF] + this.At[Ww.rightBitwise(l7, 4L) & 0xF] + this.At[(int)(0xFL & l7)] + this.At[Ww.rightBitwise(l8, 28L) & 0xF] + this.At[Ww.rightBitwise(l8, 24L) & 0xF] + this.At[Ww.rightBitwise(l8, 20L) & 0xF] + this.At[Ww.rightBitwise(l8, 16L) & 0xF] + this.At[Ww.rightBitwise(l8, 12L) & 0xF] + this.At[Ww.rightBitwise(l8, 8L) & 0xF] + this.At[Ww.rightBitwise(l8, 4L) & 0xF] + this.At[(int)(0xFL & l8)];
    }

    public Ww() {
        this.At = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
        this.Pt = new int[]{Integer.MIN_VALUE, 0x800000, 32768, 128};
        this.blocks = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public static int rightBitwise(long l, long l2) {
        if (l < Integer.MAX_VALUE && l2 < Integer.MAX_VALUE) {
            return (int)l >> (int)l2;
        }
        if (l < Integer.MAX_VALUE) {
            return (int)l >> (int)l2;
        }
        if (l2 >= Integer.MAX_VALUE) return (int)(l >> (int)l2);
        return (int)(l >> (int)l2);
    }

    public Ww update(String string) {
        int n = 0;
        int n2 = 0;
        int n3 = string.length();
        int[] nArray = this.blocks;
        while (true) {
            if (n >= n3) {
                if (this.bytes <= 0xFFFFFFFFL) return this;
                this.hBytes = (int)((long)this.hBytes + this.bytes / 0x100000000L);
                this.bytes %= 0x100000000L;
                return this;
            }
            if (this.hashed) {
                this.hashed = false;
                nArray[0] = this.block;
                for (int i = 0; i < 17; ++i) {
                    nArray[i] = 0;
                }
            }
            n2 = this.start;
            while (n < n3 && n2 < 64) {
                int n4 = Character.codePointAt(string, n);
                if (n4 < 128) {
                    int n5 = Ww.rightBitwise(n2, 2L);
                    nArray[n5] = nArray[n5] | Ww.leftBitwise(n4, this.Lt[3 & n2++]);
                } else if (n4 < 2048) {
                    int n6 = n2 >> 2;
                    nArray[n6] = nArray[n6] | (0xC0 | n4 >> 6) << this.Lt[3 & n2++];
                    int n7 = n2 >> 2;
                    nArray[n7] = nArray[n7] | (0x80 | 0x3F & n4) << this.Lt[3 & n2++];
                } else if (n4 < 55296 || n4 >= 57344) {
                    int n8 = n2 >> 2;
                    nArray[n8] = nArray[n8] | (0xE0 | n4 >> 12) << this.Lt[3 & n2++];
                    int n9 = n2 >> 2;
                    nArray[n9] = nArray[n9] | (0x80 | n4 >> 6 & 0x3F) << this.Lt[3 & n2++];
                    int n10 = n2 >> 2;
                    nArray[n10] = nArray[n10] | (0x80 | 0x3F & n4) << this.Lt[3 & n2++];
                } else {
                    n4 = 65536 + ((0x3FF & n4) << 10 | 0x3FF & Character.codePointAt(string, ++n));
                    int n11 = n2 >> 2;
                    nArray[n11] = nArray[n11] | (0xF0 | n4 >> 18) << this.Lt[3 & n2++];
                    int n12 = n2 >> 2;
                    nArray[n12] = nArray[n12] | (0x80 | n4 >> 12 & 0x3F) << this.Lt[3 & n2++];
                    int n13 = n2 >> 2;
                    nArray[n13] = nArray[n13] | (0x80 | n4 >> 6 & 0x3F) << this.Lt[3 & n2++];
                    int n14 = n2 >> 2;
                    nArray[n14] = nArray[n14] | (0x80 | 0x3F & n4) << this.Lt[3 & n2++];
                }
                ++n;
            }
            this.lastByteIndex = n2;
            this.bytes += (long)(n2 - this.start);
            if (n2 >= 64) {
                this.block = nArray[16];
                this.start = n2 - 64;
                this.hash();
                this.hashed = true;
                continue;
            }
            this.start = n2;
        }
    }

    public static int rightTripleBitwise(long l, long l2) {
        if (l < Integer.MAX_VALUE && l2 < Integer.MAX_VALUE) {
            return (int)l >>> (int)l2;
        }
        if (l < Integer.MAX_VALUE) {
            return (int)l >>> (int)l2;
        }
        if (l2 >= Integer.MAX_VALUE) return (int)(l >>> (int)l2);
        return (int)(l >>> (int)l2);
    }

    public static int leftBitwise(long l, long l2) {
        if (l < Integer.MAX_VALUE && l2 < Integer.MAX_VALUE) {
            return (int)l << (int)l2;
        }
        if (l < Integer.MAX_VALUE) {
            return (int)l << (int)l2;
        }
        if (l2 >= Integer.MAX_VALUE) return (int)(l << (int)l2);
        return (int)(l << (int)l2);
    }

    public void finalizeEncryption() {
        if (this.finalized) return;
        this.finalized = true;
        int[] nArray = this.blocks;
        int n = this.lastByteIndex;
        nArray[16] = this.block;
        int n2 = n >> 2;
        nArray[n2] = nArray[n2] | this.Pt[3 & n];
        this.block = nArray[16];
        if (n >= 56) {
            if (!this.hashed) {
                this.hash();
            }
            nArray[0] = this.block;
            nArray[15] = 0;
            nArray[14] = 0;
            nArray[13] = 0;
            nArray[12] = 0;
            nArray[11] = 0;
            nArray[10] = 0;
            nArray[9] = 0;
            nArray[8] = 0;
            nArray[7] = 0;
            nArray[6] = 0;
            nArray[5] = 0;
            nArray[4] = 0;
            nArray[3] = 0;
            nArray[2] = 0;
            nArray[1] = 0;
            nArray[16] = 0;
        }
        nArray[14] = Ww.leftBitwise(this.hBytes, 3L) | Ww.rightTripleBitwise(this.bytes, 29L);
        nArray[15] = Ww.leftBitwise(this.bytes, 3L);
        this.hash();
    }
}
