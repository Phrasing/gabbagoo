/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.task.sites.walmart.util.encryption.Aes
 *  io.trickle.task.sites.walmart.util.encryption.CMAC
 *  io.trickle.task.sites.walmart.util.encryption.Encryptor
 */
package io.trickle.task.sites.walmart.util.encryption;

import io.trickle.task.sites.walmart.util.encryption.Aes;
import io.trickle.task.sites.walmart.util.encryption.CMAC;
import io.trickle.task.sites.walmart.util.encryption.Encryptor;

public class SDW {
    public Aes aes;
    public static String HEX = "0123456789abcdef";

    public SDW(Aes aes) {
        this.aes = aes;
    }

    public String reformat(String string, String string2) {
        Object object = "";
        int n = 0;
        int n2 = 0;
        while (n2 < string2.length()) {
            if (n < string.length() && Encryptor.base10.indexOf(string2.charAt(n2)) >= 0) {
                object = (String)object + string.charAt(n);
                ++n;
            } else {
                object = (String)object + string2.charAt(n2);
            }
            ++n2;
        }
        return object;
    }

    public String integrity(String string, String string2, String string3) {
        String string4 = Character.toString(0) + Character.toString(string2.length()) + string2 + Character.toString(0) + Character.toString(string3.length()) + string3;
        long[] lArray = Encryptor.hexToWords((String)string);
        lArray[3] = lArray[3] ^ 1L;
        Aes aes = this.aes.cipher(lArray);
        CMAC cMAC = new CMAC();
        int[] nArray = cMAC.compute(aes, string4);
        return this.wordToHex(nArray[0]) + this.wordToHex(nArray[1]);
    }

    public String wordToHex(int n) {
        int n2 = 32;
        Object object = "";
        while (n2 > 0) {
            object = (String)object + "0123456789abcdef".charAt(n >>> (n2 -= 4) & 0xF);
        }
        return object;
    }

    public String fixluhn(String string, int n, int n2) {
        int n3 = this.luhn(string);
        n3 = n3 < n2 ? (n3 += 10 - n2) : (n3 -= n2);
        if (n3 == 0) return string;
        n3 = (string.length() - n) % 2 != 0 ? 10 - n3 : (n3 % 2 == 0 ? 5 - n3 / 2 : (9 - n3) / 2 + 5);
        return string.substring(0, n) + n3 + string.substring(n + 1);
    }

    public int luhn(String string) {
        int n;
        int n2 = 0;
        for (n = string.length() - 1; n >= 0; n2 += Integer.parseInt(String.valueOf(string.charAt(n)), 10), n -= 2) {
        }
        n = string.length() - 2;
        while (n >= 0) {
            int n3 = 2 * Integer.parseInt(String.valueOf(string.charAt(n)), 10);
            n2 = n3 < 10 ? (n2 += n3) : (n2 += n3 - 9);
            n -= 2;
        }
        return n2 % 10;
    }
}
