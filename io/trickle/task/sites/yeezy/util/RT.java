/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bouncycastle.util.encoders.Hex
 */
package io.trickle.task.sites.yeezy.util;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import org.bouncycastle.util.encoders.Hex;

public class RT {
    public static String getByteHex() {
        byte[] byArray = new byte[4];
        ThreadLocalRandom.current().nextBytes(byArray);
        return Hex.toHexString((byte[])byArray);
    }

    public static String getRT() {
        return "\"z=1&dm=yeezysupply.com&si=" + UUID.randomUUID() + "&ss=" + Long.toString(System.currentTimeMillis(), 36) + "&sl=1&tt=" + Integer.toString(ThreadLocalRandom.current().nextInt(890, 5800), 36) + "&bcn=%2F%2F" + RT.getByteHex() + ".akstat.io%2F\"";
    }
}
