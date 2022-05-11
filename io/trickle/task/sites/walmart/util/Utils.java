/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.task.antibot.impl.px.payload.captcha.util.Ww
 */
package io.trickle.task.sites.walmart.util;

import io.trickle.task.antibot.impl.px.payload.captcha.util.Ww;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

public class Utils {
    public static long[] fillArrRandInt256(long[] lArray) {
        int n = 0;
        while (n < lArray.length) {
            lArray[n] = ThreadLocalRandom.current().nextInt(256);
            ++n;
        }
        return lArray;
    }

    @Deprecated
    public static String buildWebUA() {
        StringBuilder stringBuilder = new StringBuilder("Walmart/210");
        int n = 0;
        while (true) {
            if (n >= 7) {
                stringBuilder.append(" Walmart WMTAPP v21.");
                stringBuilder.append("3.0");
                return stringBuilder.toString();
            }
            stringBuilder.append(ThreadLocalRandom.current().nextInt(10));
            ++n;
        }
    }

    public static String genUet() {
        long[] lArray = new long[10];
        long[] lArray2 = new long[16];
        Utils.fillArrRandInt256(lArray);
        long l = (Instant.now().toEpochMilli() + 12219292800000L) * 10000L + (lArray[8] + (lArray[9] << 8)) % 10000L;
        lArray2[3] = l & 0xFFL;
        lArray2[2] = Ww.rightBitwise((long)l, (long)8L) & 0xFF;
        lArray2[1] = Ww.rightBitwise((long)l, (long)16L) & 0xFF;
        lArray2[0] = Ww.rightBitwise((long)l, (long)24L) & 0xFF;
        lArray2[5] = (l /= 0x100000000L) & 0xFFL;
        lArray2[4] = Ww.rightBitwise((long)l, (long)8L) & 0xFF;
        lArray2[7] = Ww.rightBitwise((long)l, (long)16L) & 0xFF;
        lArray2[6] = Ww.rightBitwise((long)l, (long)24L) & 0xFF;
        int n = 0;
        while (true) {
            if (n >= 8) {
                lArray2[8] = lArray2[8] & 0x3FL;
                lArray2[8] = lArray2[8] | 0x80L;
                lArray2[6] = lArray2[6] & 0xFL;
                lArray2[6] = lArray2[6] | 0x10L;
                lArray2[10] = lArray2[10] | 1L;
                return Utils.st(lArray2, true);
            }
            lArray2[n + 8] = lArray[n];
            ++n;
        }
    }

    @Deprecated
    public static String buildUA() {
        int n;
        StringBuilder stringBuilder = new StringBuilder("Walmart/201");
        for (n = 0; n < 7; ++n) {
            stringBuilder.append(ThreadLocalRandom.current().nextInt(10));
        }
        stringBuilder.append(" CFNetwork/11");
        for (n = 0; n < 4; ++n) {
            if (n >= 2) {
                stringBuilder.append(".");
            }
            stringBuilder.append(ThreadLocalRandom.current().nextInt(10));
        }
        stringBuilder.append(" Darwin/1");
        n = 0;
        while (n < 3) {
            if (n >= 1) {
                stringBuilder.append(".");
            }
            stringBuilder.append(ThreadLocalRandom.current().nextInt(10));
            ++n;
        }
        return stringBuilder.toString();
    }

    public static String rot13(String string) {
        StringBuilder stringBuilder = new StringBuilder();
        int n = 0;
        while (n < string.length()) {
            char c = string.charAt(n);
            if (c >= 'a' && c <= 'm') {
                c = (char)(c + 13);
            } else if (c >= 'A' && c <= 'M') {
                c = (char)(c + 13);
            } else if (c >= 'n' && c <= 'z') {
                c = (char)(c - 13);
            } else if (c >= 'N' && c <= 'Z') {
                c = (char)(c - 13);
            }
            stringBuilder.append(c);
            ++n;
        }
        return stringBuilder.toString();
    }

    public static String st(long[] lArray, boolean bl) {
        Object object = "";
        int n = 0;
        while (n < 16) {
            if (bl && n >= 4 && n <= 10 && n % 2 == 0) {
                object = (String)object + "-";
            }
            object = (String)object + Long.toHexString(lArray[n] + 256L).substring(1, 3);
            ++n;
        }
        return object;
    }
}
