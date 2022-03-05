/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.sites.walmart.util.encryption;

import io.trickle.task.sites.walmart.util.encryption.Aes;

public class FFX {
    public Aes aesObject;
    public static String[] alphabet = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    public String encryptWithCipher(String string, String string2, Aes aes, int n) {
        int n2 = string.length();
        int n3 = (int)Math.floor(n2 / 2);
        int n4 = 5;
        int[] nArray = this.precompF(aes, n2, string2, n);
        int n5 = this.precompb(n, n2);
        int[] nArray2 = this.digitToVal(string, n3, n);
        int[] nArray3 = this.digitToVal(string.substring(n3), n2 - n3, n);
        if (nArray2 == null) return "";
        if (nArray3 == null) {
            return "";
        }
        int n6 = 0;
        while (n6 < n4) {
            int n7;
            int n8;
            int[] nArray4 = this.f(aes, n6 * 2, string2, nArray3, nArray3.length, nArray2.length, nArray, n, n5);
            int n9 = 0;
            for (n8 = nArray2.length - 1; n8 >= 0; --n8) {
                n7 = nArray2[n8] + nArray4[n8] + n9;
                if (n7 < n) {
                    nArray2[n8] = n7;
                    n9 = 0;
                    continue;
                }
                nArray2[n8] = n7 - n;
                n9 = 1;
            }
            nArray4 = this.f(aes, n6 * 2 + 1, string2, nArray2, nArray2.length, nArray3.length, nArray, n, n5);
            n9 = 0;
            for (n8 = nArray3.length - 1; n8 >= 0; --n8) {
                n7 = nArray3[n8] + nArray4[n8] + n9;
                if (n7 < n) {
                    nArray3[n8] = n7;
                    n9 = 0;
                    continue;
                }
                nArray3[n8] = n7 - n;
                n9 = 1;
            }
            ++n6;
        }
        return this.valToDigit(nArray2, n) + this.valToDigit(nArray3, n);
    }

    public int precompb(int n, int n2) {
        int n3 = (int)Math.ceil((double)n2 / Double.longBitsToDouble(0x4000000000000000L));
        int n4 = 0;
        int n5 = 1;
        while (true) {
            if (n3 <= 0) {
                if (n5 <= 1) return n4;
                ++n4;
                return n4;
            }
            --n3;
            if ((n5 *= n) < 256) continue;
            n5 /= 256;
            ++n4;
        }
    }

    public String valToDigit(int[] nArray, int n) {
        Object object = "";
        if (n == 256) {
            int n2 = 0;
            while (n2 < nArray.length) {
                object = (String)object + Character.toString(nArray[n2]);
                ++n2;
            }
            return object;
        }
        int n3 = 0;
        while (n3 < nArray.length) {
            object = (String)object + alphabet[nArray[n3]];
            ++n3;
        }
        return object;
    }

    public FFX(Aes aes) {
        this.aesObject = aes;
    }

    public int[] convertRadix(int[] nArray, int n, int n2, int n3, int n4) {
        int[] nArray2 = new int[n3];
        for (int i = 0; i < n3; ++i) {
            nArray2[i] = 0;
        }
        int n5 = 0;
        while (n5 < n) {
            this.bnMultiply(nArray2, n4, n2);
            this.bnAdd(nArray2, n4, nArray[n5]);
            ++n5;
        }
        return nArray2;
    }

    public int[] precompF(Aes aes, int n, String string, int n2) {
        int n3 = 4;
        int[] nArray = new int[n3];
        int n4 = string.length();
        int n5 = 10;
        nArray[0] = 0x1020100 | n2 >> 16 & 0xFF;
        nArray[1] = (n2 >> 8 & 0xFF) << 24 | (n2 & 0xFF) << 16 | n5 << 8 | (int)Math.floor(n / 2) & 0xFF;
        nArray[2] = n;
        nArray[3] = n4;
        return aes.encrypt(nArray);
    }

    public void bnAdd(int[] nArray, int n, int n2) {
        int n3 = nArray.length - 1;
        int n4 = n2;
        while (n3 >= 0) {
            if (n4 <= 0) return;
            int n5 = nArray[n3] + n4;
            nArray[n3] = n5 % n;
            n4 = (n5 - nArray[n3]) / n;
            --n3;
        }
    }

    public int[] cbcmacq(int[] nArray, int[] nArray2, int n, Aes aes) {
        int n2;
        int n3 = 4;
        int[] nArray3 = new int[n3];
        for (n2 = 0; n2 < n3; ++n2) {
            nArray3[n2] = nArray[n2];
        }
        n2 = 0;
        while (4 * n2 < n) {
            for (int i = 0; i < n3; ++i) {
                nArray3[i] = nArray3[i] ^ (nArray2[4 * (n2 + i)] << 24 | nArray2[4 * (n2 + i) + 1] << 16 | nArray2[4 * (n2 + i) + 2] << 8 | nArray2[4 * (n2 + i) + 3]);
            }
            nArray3 = aes.encrypt(nArray3);
            n2 += n3;
        }
        return nArray3;
    }

    public int[] f(Aes aes, int n, String string, int[] nArray, int n2, int n3, int[] nArray2, int n4, int n5) {
        int[] nArray3;
        int n6;
        int n7 = 16;
        int n8 = (int)(Math.ceil((double)n5 / Double.longBitsToDouble(0x4010000000000000L)) + Double.longBitsToDouble(0x3FF0000000000000L));
        int n9 = string.length() + n5 + 1 & 0xF;
        if (n9 > 0) {
            n9 = 16 - n9;
        }
        int[] nArray4 = new int[string.length() + n9 + n5 + 1];
        for (n6 = 0; n6 < string.length(); ++n6) {
            nArray4[n6] = Character.codePointAt(string, n6);
        }
        while (n6 < n9 + string.length()) {
            nArray4[n6] = 0;
            ++n6;
        }
        nArray4[nArray4.length - n5 - 1] = n;
        int[] nArray5 = this.convertRadix(nArray, n2, n4, n5, 256);
        for (int i = 0; i < n5; ++i) {
            nArray4[nArray4.length - n5 + i] = nArray5[i];
        }
        int[] nArray6 = nArray3 = this.cbcmacq(nArray2, nArray4, nArray4.length, aes);
        int[] nArray7 = new int[2 * n8];
        n6 = 0;
        while (n6 < n8) {
            if (n6 > 0 && (n6 & 3) == 0) {
                int n10 = n6 >> 2;
                nArray6 = aes.encrypt(new int[]{nArray3[0], nArray3[1], nArray3[2], nArray3[3] ^ n10});
            }
            nArray7[2 * n6] = nArray6[n6 & 3] >>> 16;
            nArray7[2 * n6 + 1] = nArray6[n6 & 3] & 0xFFFF;
            ++n6;
        }
        return this.convertRadix(nArray7, 2 * n8, 65536, n3, n4);
    }

    public void bnMultiply(int[] nArray, int n, int n2) {
        int n3 = 0;
        int n4 = nArray.length - 1;
        while (n4 >= 0) {
            int n5 = nArray[n4] * n2 + n3;
            nArray[n4] = n5 % n;
            n3 = (n5 - nArray[n4]) / n;
            --n4;
        }
    }

    public int[] digitToVal(String string, int n, int n2) {
        int[] nArray = new int[n];
        if (n2 == 256) {
            int n3 = 0;
            while (n3 < n) {
                nArray[n3] = Character.codePointAt(string, n3);
                ++n3;
            }
            return nArray;
        }
        int n4 = 0;
        while (n4 < n) {
            int n5 = Integer.parseInt(String.valueOf(string.charAt(n4)), n2);
            if (n5 >= n2) {
                return null;
            }
            nArray[n4] = n5;
            ++n4;
        }
        return nArray;
    }
}

