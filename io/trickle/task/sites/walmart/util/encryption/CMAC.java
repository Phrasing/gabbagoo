/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.sites.walmart.util.encryption;

import io.trickle.task.sites.walmart.util.encryption.Aes;

public class CMAC {
    public static int const_Rb = 135;

    public void leftShift(int[] nArray) {
        nArray[0] = (nArray[0] & Integer.MAX_VALUE) << 1 | nArray[1] >>> 31;
        nArray[1] = (nArray[1] & Integer.MAX_VALUE) << 1 | nArray[2] >>> 31;
        nArray[2] = (nArray[2] & Integer.MAX_VALUE) << 1 | nArray[3] >>> 31;
        nArray[3] = (nArray[3] & Integer.MAX_VALUE) << 1;
    }

    public int[] compute(Aes aes, String string) {
        int[] nArray = new int[]{0, 0, 0, 0};
        int[] nArray2 = aes.encrypt(nArray);
        int n = nArray2[0];
        this.leftShift(nArray2);
        if (this.msbNotZero(n)) {
            nArray2[3] = nArray2[3] ^ 0x87;
        }
        int n2 = 0;
        while (n2 < string.length()) {
            int n3 = n2 >> 2 & 3;
            nArray[n3] = nArray[n3] ^ (Character.codePointAt(string, n2) & 0xFF) << 8 * (3 - (n2 & 3));
            if ((++n2 & 0xF) != 0 || n2 >= string.length()) continue;
            nArray = aes.encrypt(nArray);
        }
        if (n2 == 0 || (n2 & 0xF) != 0) {
            n = nArray2[0];
            this.leftShift(nArray2);
            if (this.msbNotZero(n)) {
                nArray2[3] = nArray2[3] ^ 0x87;
            }
            int n4 = n2 >> 2 & 3;
            nArray[n4] = nArray[n4] ^ 128 << 8 * (3 - (n2 & 3));
        }
        nArray[0] = nArray[0] ^ nArray2[0];
        nArray[1] = nArray[1] ^ nArray2[1];
        nArray[2] = nArray[2] ^ nArray2[2];
        nArray[3] = nArray[3] ^ nArray2[3];
        return aes.encrypt(nArray);
    }

    public boolean msbNotZero(int n) {
        if ((n | Integer.MAX_VALUE) == Integer.MAX_VALUE) return false;
        return true;
    }
}

