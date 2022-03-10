/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.sites.walmart.util.encryption;

import java.util.Arrays;

public class Aes {
    public int l;
    public int b;
    public int[][][] tables;
    public int[] h;
    public int[] k;
    public int p;
    public int m;
    public int e;
    public int[][] key;
    public int[] n;
    public int[][] j;
    public int[] c;
    public int g;
    public int o;
    public int[][] q;
    public int a;
    public int f;

    public Aes precompute() {
        this.tables = new int[2][5][256];
        this.j = this.tables[0];
        this.q = this.tables[1];
        this.h = this.j[4];
        this.n = this.q[4];
        this.k = new int[256];
        this.c = new int[256];
        this.b = -1;
        this.g = 0;
        while (this.g < 256) {
            this.k[this.g] = this.g << 1 ^ (this.g >> 7) * 283;
            this.c[this.k[this.g] ^ this.g] = this.g;
            ++this.g;
        }
        this.f = 0;
        this.l = 0;
        while (this.h[this.l] == 0) {
            this.o = this.f ^ this.f << 1 ^ this.f << 2 ^ this.f << 3 ^ this.f << 4;
            this.h[this.l] = this.o = this.o >> 8 ^ this.o & 0xFF ^ 0x63;
            this.n[this.o] = this.l;
            this.b = this.k[this.l];
            this.p = this.k[this.b];
            this.m = this.k[this.p];
            this.a = this.m * 0x1010101 ^ this.p * 65537 ^ this.b * 257 ^ this.l * 0x1010100;
            this.e = this.k[this.o] * 257 ^ this.o * 0x1010100;
            this.g = 0;
            while (this.g < 4) {
                this.j[this.g][this.l] = this.e = this.e << 24 ^ this.e >>> 8;
                this.q[this.g][this.o] = this.a = this.a << 24 ^ this.a >>> 8;
                ++this.g;
            }
            this.l ^= this.b == 0 ? 1 : this.b;
            this.f = this.c[this.f] == 0 ? 1 : this.c[this.f];
        }
        this.g = 0;
        while (this.g < 5) {
            this.j[this.g] = Arrays.copyOf(this.j[this.g], this.j[this.g].length);
            this.q[this.g] = Arrays.copyOf(this.q[this.g], this.q[this.g].length);
            ++this.g;
        }
        return this;
    }

    public Aes cipher(long[] lArray) {
        int n;
        int n2;
        int[] nArray = this.tables[0][4];
        int[][] nArray2 = this.tables[1];
        this.a = lArray.length;
        this.b = 1;
        if (this.a != 4 && this.a != 6 && this.a != 8) {
            System.err.println("invalid aes key size");
        }
        long[] lArray2 = Arrays.copyOf(lArray, 256);
        int[] nArray3 = new int[256];
        for (n2 = this.a; n2 < 4 * this.a + 28; ++n2) {
            n = (int)lArray2[n2 - 1];
            if (n2 % this.a == 0 || this.a == 8 && n2 % this.a == 4) {
                n = nArray[n >>> 24] << 24 ^ nArray[n >> 16 & 0xFF] << 16 ^ nArray[n >> 8 & 0xFF] << 8 ^ nArray[n & 0xFF];
                if (n2 % this.a == 0) {
                    n = n << 8 ^ n >>> 24 ^ this.b << 24;
                    this.b = this.b << 1 ^ (this.b >> 7) * 283;
                }
            }
            lArray2[n2] = lArray2[n2 - this.a] ^ (long)n;
        }
        lArray2 = Aes.removeElements(lArray2, 0);
        int n3 = 0;
        while (true) {
            if (n2 == 0) {
                nArray3 = Aes.removeElements(nArray3, 0);
                this.key = new int[][]{Arrays.stream(lArray2).mapToInt(Aes::lambda$cipher$0).toArray(), nArray3};
                return this;
            }
            n = (int)lArray2[(n3 & 3) != 0 ? n2 : n2 - 4];
            nArray3[n3] = n2 <= 4 || n3 < 4 ? n : nArray2[0][nArray[n >>> 24]] ^ nArray2[1][nArray[n >> 16 & 0xFF]] ^ nArray2[2][nArray[n >> 8 & 0xFF]] ^ nArray2[3][nArray[n & 0xFF]];
            ++n3;
            --n2;
        }
    }

    public int[] encrypt(int[] nArray) {
        return this._crypt(nArray, 0);
    }

    public static int[] removeElements(int[] nArray, int n) {
        int n2 = 0;
        int n3 = 0;
        while (n3 < nArray.length) {
            if (nArray[n3] != n) {
                nArray[n2++] = nArray[n3];
            }
            ++n3;
        }
        return Arrays.copyOf(nArray, n2);
    }

    public static int lambda$cipher$0(long l) {
        return (int)l;
    }

    public static long[] removeElements(long[] lArray, int n) {
        int n2 = 0;
        int n3 = 0;
        while (n3 < lArray.length) {
            if (lArray[n3] != (long)n) {
                lArray[n2++] = lArray[n3];
            }
            ++n3;
        }
        return Arrays.copyOf(lArray, n2);
    }

    public int[] _crypt(int[] nArray, int n) {
        int n2;
        int n3;
        if (nArray.length != 4) {
            System.err.println("Something went wrong with crypting profile.");
        }
        int[] nArray2 = this.key[n];
        int n4 = nArray[0] ^ nArray2[0];
        int n5 = nArray[n != 0 ? 3 : 1] ^ nArray2[1];
        int n6 = nArray[2] ^ nArray2[2];
        int n7 = nArray[n != 0 ? 1 : 3] ^ nArray2[3];
        int n8 = nArray2.length / 4 - 2;
        int n9 = 4;
        int[] nArray3 = new int[]{0, 0, 0, 0};
        int[][] nArray4 = this.tables[n];
        int[] nArray5 = nArray4[0];
        int[] nArray6 = nArray4[1];
        int[] nArray7 = nArray4[2];
        int[] nArray8 = nArray4[3];
        int[] nArray9 = nArray4[4];
        for (n3 = 0; n3 < n8; n9 += 4, ++n3) {
            n2 = nArray5[n4 >>> 24] ^ nArray6[n5 >> 16 & 0xFF] ^ nArray7[n6 >> 8 & 0xFF] ^ nArray8[n7 & 0xFF] ^ nArray2[n9];
            int n10 = nArray5[n5 >>> 24] ^ nArray6[n6 >> 16 & 0xFF] ^ nArray7[n7 >> 8 & 0xFF] ^ nArray8[n4 & 0xFF] ^ nArray2[n9 + 1];
            int n11 = nArray5[n6 >>> 24] ^ nArray6[n7 >> 16 & 0xFF] ^ nArray7[n4 >> 8 & 0xFF] ^ nArray8[n5 & 0xFF] ^ nArray2[n9 + 2];
            n7 = nArray5[n7 >>> 24] ^ nArray6[n4 >> 16 & 0xFF] ^ nArray7[n5 >> 8 & 0xFF] ^ nArray8[n6 & 0xFF] ^ nArray2[n9 + 3];
            n4 = n2;
            n5 = n10;
            n6 = n11;
        }
        n3 = 0;
        while (n3 < 4) {
            nArray3[n != 0 ? 3 & -n3 : n3] = nArray9[n4 >>> 24] << 24 ^ nArray9[n5 >> 16 & 0xFF] << 16 ^ nArray9[n6 >> 8 & 0xFF] << 8 ^ nArray9[n7 & 0xFF] ^ nArray2[n9++];
            n2 = n4;
            n4 = n5;
            n5 = n6;
            n6 = n7;
            n7 = n2;
            ++n3;
        }
        return nArray3;
    }
}

