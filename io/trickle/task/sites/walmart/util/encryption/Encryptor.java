/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.sites.walmart.util.encryption;

import io.trickle.task.sites.walmart.util.encryption.Aes;
import io.trickle.task.sites.walmart.util.encryption.FFX;
import io.trickle.task.sites.walmart.util.encryption.SDW;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class Encryptor {
    public String PIE_KEY_ID;
    public int PIE_E;
    public String PIE_K;
    public Aes encryptionObj;
    public int PIE_L;
    public int PIE_PHASE;
    public static String base10 = "0123456789";

    public static long[] hexToWords(String string) {
        int n = 4;
        long[] lArray = new long[n];
        if (string.length() != n * 8) {
            return null;
        }
        int n2 = 0;
        while (n2 < n) {
            lArray[n2] = Long.parseLong(Encryptor.substr(string, n2 * 8, 8), 16);
            ++n2;
        }
        return lArray;
    }

    public static String substr(String string, int n, int n2) {
        if (n > string.length()) {
            return "";
        }
        if (n2 <= (string = string.substring(n)).length()) return string.substring(0, n2);
        return string;
    }

    public static String strip(String string, String string2) {
        return string.replace("PIE." + string2, "").replace("=", "").replace("\"", "");
    }

    public static String lambda$parse$1(String string) {
        return string.replace(" ", "");
    }

    public static void main(String[] stringArray) {
        int n = 0;
        while (n < 100) {
            Object[] objectArray = new Encryptor().protectPANandCVV("1122334455667788", "224", true);
            System.out.println(Arrays.toString(objectArray));
            ++n;
        }
    }

    public static boolean lambda$parse$0(String string) {
        return string.contains("PIE");
    }

    public Aes hexToKey(String string) {
        long[] lArray = Encryptor.hexToWords(string);
        this.encryptionObj = new Aes();
        this.encryptionObj.precompute();
        return this.encryptionObj.cipher(lArray);
    }

    public static String distill(String string) {
        StringBuilder stringBuilder = new StringBuilder();
        int n = 0;
        while (n < string.length()) {
            if (base10.contains("" + string.charAt(n))) {
                stringBuilder.append(string.charAt(n));
            }
            ++n;
        }
        return stringBuilder.toString();
    }

    public String[] protectPANandCVV(String string, String string2, boolean bl) {
        String string3 = Encryptor.distill(string);
        String string4 = Encryptor.distill(string2);
        String string5 = string3.substring(0, this.PIE_L) + string3.substring(string3.length() - this.PIE_E);
        if (!bl) return null;
        int n = Encryptor.luhn(string3);
        String string6 = string3.substring(this.PIE_L + 1, string3.length() - this.PIE_E);
        String string7 = this.encrypt(string6 + string4, string5, this.PIE_K, 10);
        String string8 = string3.substring(0, this.PIE_L) + "0" + string7.substring(0, string7.length() - string4.length()) + string3.substring(string3.length() - this.PIE_E);
        SDW sDW = new SDW(this.encryptionObj);
        String string9 = sDW.fixluhn(string8, this.PIE_L, n);
        String string10 = sDW.reformat(string9, string);
        String string11 = sDW.reformat(string7.substring(string7.length() - string4.length()), string2);
        return new String[]{string10, string11, sDW.integrity(this.PIE_K, string10, string11), this.PIE_KEY_ID, String.valueOf(this.PIE_PHASE), UUID.randomUUID().toString()};
    }

    public static String[] prepareAndEncrypt(String string, String string2, String string3) {
        Objects.requireNonNull(string2);
        Objects.requireNonNull(string3);
        String[] stringArray = Encryptor.parse(string);
        if (stringArray.length != 5) return Encryptor.encrypt(string2, string3);
        try {
            return new Encryptor(stringArray[0], stringArray[1], stringArray[2], stringArray[3], stringArray[4]).protectPANandCVV(string2, string3, true);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public String encrypt(String string, String string2, String string3, int n) {
        Aes aes = this.hexToKey(string3);
        if (aes == null) {
            return "";
        }
        FFX fFX = new FFX(aes);
        return fFX.encryptWithCipher(string, string2, aes, n);
    }

    public static String[] parse(String string) {
        List list = string.lines().filter(Encryptor::lambda$parse$0).map(Encryptor::lambda$parse$1).map(Encryptor::lambda$parse$2).collect(Collectors.toList());
        String[] stringArray = new String[5];
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            String string2;
            String string3 = (String)iterator.next();
            if (string3.contains("PIE.L")) {
                stringArray[0] = string2 = Encryptor.strip(string3, "L");
                continue;
            }
            if (string3.contains("PIE.E")) {
                stringArray[1] = string2 = Encryptor.strip(string3, "E");
                continue;
            }
            if (string3.contains("PIE.K")) {
                stringArray[2] = string2 = Encryptor.strip(string3, "K");
                continue;
            }
            if (string3.contains("PIE.key_id")) {
                stringArray[3] = string2 = Encryptor.strip(string3, "key_id");
                continue;
            }
            if (!string3.contains("PIE.phase")) continue;
            stringArray[4] = string2 = Encryptor.strip(string3, "phase");
        }
        return stringArray;
    }

    public Encryptor(String string, String string2, String string3, String string4, String string5) {
        this.PIE_L = Integer.parseInt(string);
        this.PIE_E = Integer.parseInt(string2);
        this.PIE_K = string3;
        this.PIE_KEY_ID = string4;
        this.PIE_PHASE = Integer.parseInt(string5);
    }

    public static String lambda$parse$2(String string) {
        return string.replace(";", "");
    }

    public Encryptor() {
        this.PIE_L = 6;
        this.PIE_E = 4;
        this.PIE_K = "70E7D02F58E2D0091D2AC30D65103EAE";
        this.PIE_KEY_ID = "1bb96826";
        this.PIE_PHASE = 0;
    }

    public static int luhn(String string) {
        int n;
        int n2 = 0;
        for (n = string.length() - 1; n >= 0; n2 += Integer.parseInt(String.valueOf(string.charAt(n))), n -= 2) {
        }
        n = string.length() - 2;
        while (n >= 0) {
            int n3 = 2 * Integer.parseInt(String.valueOf(string.charAt(n)));
            n2 = n3 < 10 ? (n2 += n3) : (n2 += n3 - 9);
            n -= 2;
        }
        return n2 % 10;
    }

    public static String[] encrypt(String string, String string2) {
        Objects.requireNonNull(string);
        Objects.requireNonNull(string2);
        return new Encryptor().protectPANandCVV(string, string2, true);
    }
}

