/*
 * Decompiled with CFR 0.152.
 */
package io.trickle.util;

import java.util.concurrent.ThreadLocalRandom;

public class Payment {
    public static String generateCard(String string, int n) {
        int n2 = n - (string.length() + 1);
        StringBuilder stringBuilder = new StringBuilder(string);
        int n3 = 0;
        while (true) {
            if (n3 >= n2) {
                n3 = Payment.getCheckDigit(stringBuilder.toString());
                stringBuilder.append(n3);
                return stringBuilder.toString();
            }
            int n4 = ThreadLocalRandom.current().nextInt(10);
            stringBuilder.append(n4);
            ++n3;
        }
    }

    public static int getCheckDigit(String string) {
        int n;
        int n2;
        int n3 = 0;
        for (n = 0; n < string.length(); n3 += n2, ++n) {
            n2 = Integer.parseInt(string.substring(n, n + 1));
            if (n % 2 != 0 || (n2 *= 2) <= 9) continue;
            n2 = n2 / 10 + n2 % 10;
        }
        n = n3 % 10;
        return n == 0 ? 0 : 10 - n;
    }
}
