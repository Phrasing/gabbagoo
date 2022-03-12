/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.sites.hibbett;

import io.trickle.util.Utils;
import java.util.concurrent.ThreadLocalRandom;

public class FakeIOSValueGens {
    public static String genUA() {
        return "Hibbett | CG/4.15.0 (com.hibbett.hibbett-sports; build:7993; iOS 1" + ThreadLocalRandom.current().nextInt(0, 4) + "." + ThreadLocalRandom.current().nextInt(1, 9) + "." + ThreadLocalRandom.current().nextInt(1, 9) + ") Alamofire/5.0.0-rc.3";
    }

    public static String randOfLength(int n) {
        return Utils.getString(n);
    }

    public static String genTag() {
        return "W/\"" + FakeIOSValueGens.randOfLength(3) + "-" + FakeIOSValueGens.randOfLength(27) + "\"";
    }
}

