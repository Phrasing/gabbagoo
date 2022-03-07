/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.antibot.impl.px.payload.captcha.util;

import io.trickle.task.antibot.impl.px.payload.captcha.util.Ww;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

public class UUIDv1 {
    public static String[] gg = UUIDv1.fillHexStringArr256();

    public static int[] fillArrRandInt256(int[] nArray) {
        int n = 0;
        while (n < nArray.length) {
            nArray[n] = ThreadLocalRandom.current().nextInt(256);
            ++n;
        }
        return nArray;
    }

    public static String[] fillHexStringArr256() {
        String[] stringArray = new String[256];
        int n = 0;
        while (n < stringArray.length) {
            stringArray[n] = Integer.toHexString(n);
            ++n;
        }
        return stringArray;
    }

    public static String genUUIDv1() {
        int[] nArray;
        long l;
        long l2 = 0L;
        String string = "";
        int[] nArray2 = UUIDv1.fillArrRandInt256(new int[16]);
        int n = 0;
        int[] nArray3 = new int[16];
        int[] nArray4 = new int[]{};
        int n2 = 0x3FFF & (Ww.leftBitwise(nArray2[6], 8L) | nArray2[7]);
        long l3 = Instant.now().toEpochMilli();
        long l4 = l3 - 0L + ((l = 1L) - 0L) / 10000L;
        if (l4 < 0L || l3 > l2) {
            l = 0L;
        }
        if ((double)l >= Double.longBitsToDouble(4666723172467343360L)) {
            throw new Error("uuid.v1(): Can't create more than 10M uuids/sec");
        }
        l2 = l3;
        long l5 = l;
        long l6 = n2;
        long l7 = (10000L * (0xFFFFFFFL & (l3 += 12219292800000L)) + l) % 0x100000000L;
        nArray3[n++] = Ww.rightTripleBitwise(l7, 24L) & 0xFF;
        nArray3[n++] = Ww.rightTripleBitwise(l7, 16L) & 0xFF;
        nArray3[n++] = Ww.rightTripleBitwise(l7, 8L) & 0xFF;
        nArray3[n++] = (int)(0xFFL & l7);
        int n3 = (int)((double)l3 / Double.longBitsToDouble(4751297606875873280L) * Double.longBitsToDouble(4666723172467343360L)) & 0xFFFFFFF;
        nArray3[n++] = Ww.rightTripleBitwise(n3, 8L) & 0xFF;
        nArray3[n++] = 0xFF & n3;
        nArray3[n++] = Ww.rightTripleBitwise(n3, 24L) & 0xF | 0x10;
        nArray3[n++] = Ww.rightTripleBitwise(n3, 16L) & 0xFF;
        nArray3[n++] = Ww.rightTripleBitwise(n2, 8L) | 0x80;
        nArray3[n++] = 0xFF & n2;
        int[] nArray5 = nArray = new int[]{1 | nArray2[0], nArray2[1], nArray2[2], nArray2[3], nArray2[4], nArray2[5]};
        int n4 = 0;
        while (n4 < 6) {
            nArray3[n + n4] = nArray5[n4];
            ++n4;
        }
        return UUIDv1.Cn(nArray3, 0);
    }

    public static String Cn(int[] nArray, int n) {
        int n2 = 0;
        String[] stringArray = gg;
        return stringArray[nArray[n2++]] + stringArray[nArray[n2++]] + stringArray[nArray[n2++]] + stringArray[nArray[n2++]] + "-" + stringArray[nArray[n2++]] + stringArray[nArray[n2++]] + "-" + stringArray[nArray[n2++]] + stringArray[nArray[n2++]] + "-" + stringArray[nArray[n2++]] + stringArray[nArray[n2++]] + "-" + stringArray[nArray[n2++]] + stringArray[nArray[n2++]] + stringArray[nArray[n2++]] + stringArray[nArray[n2++]] + stringArray[nArray[n2++]] + stringArray[nArray[n2++]];
    }
}

