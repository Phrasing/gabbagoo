/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.buffer.Buffer
 *  io.vertx.core.file.FileSystem
 */
package io.trickle.util;

import io.trickle.core.VertxSingleton;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

public class FileUtils {
    public static void createAndWrite(FileSystem fileSystem, String string, Buffer buffer) {
        fileSystem.writeFile(string, buffer).onFailure(Throwable::printStackTrace);
    }

    public static boolean call() {
        int n = 0;
        HashMap<String, String> hashMap = new HashMap<String, String>();
        Object[] objectArray = new Object[8];
        HashMap<String, String> hashMap2 = new HashMap<String, String>();
        boolean bl = true;
        boolean bl2 = true;
        hashMap2.put("mapValOolpppzncut", "mapKeyDibtwvciolc");
        int n2 = 425;
        long l = 7856675763029226691L;
        hashMap2.put("mapValFkmcpyjhjru", "mapKeyPsshjcxaaxe");
        objectArray[0] = hashMap2;
        for (int i = 1; i < 8; ++i) {
            objectArray[i] = ThreadLocalRandom.current().nextInt(1000);
        }
        HashSet hashSet = new HashSet();
        HashMap<String, String> hashMap3 = new HashMap<String, String>();
        boolean bl3 = true;
        long l2 = 8453697578458084073L;
        hashMap3.put("mapValBycbfwzudpw", "mapKeyAqjsprqkgwj");
        hashSet.add(hashMap3);
        hashMap.put("mapValIosjbggmccg", "mapKeyRvscmxltikh");
        int n3 = ThreadLocalRandom.current().nextInt(5);
        try {
            switch (n3) {
                case 0: {
                    Class.forName(String.valueOf((double)n3 + Math.random())).getMethod(String.valueOf(n3 ^ n - 33), new Class[0]);
                }
                case 1: {
                    Class.forName(String.valueOf((double)("lLlliIILl".toCharArray()[9] + n3) + Math.random())).getMethod(String.valueOf(n3 ^ n - 33), new Class[0]);
                }
                case 2: {
                    Class.forName(String.valueOf((double)(3453453 - n3) + Math.random())).getMethod(String.valueOf(n3 ^ n - 33), new Class[0]);
                }
                case 3: {
                    Class.forName(String.valueOf((double)n3 + Math.random() * Double.longBitsToDouble(4734571678053433344L))).getMethod(String.valueOf(n3 ^ n - FileUtils.class.hashCode()), new Class[0]);
                }
                case 4: {
                    Class.forName(String.valueOf((double)n3 + (Double.longBitsToDouble(0L) + Double.longBitsToDouble(0x4014000000000000L) * ThreadLocalRandom.current().nextGaussian() % Double.longBitsToDouble(0x4008000000000000L)) + Math.random())).getMethod(String.valueOf(n3 ^ n - 99999), new Class[0]);
                    break;
                }
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        if (!FileUtils.class.getClassLoader().toString().toLowerCase().contains("native")) {
            VertxSingleton.INSTANCE.get().eventBus().send("login.loader", (Object)"Logged in!");
            return true;
        }
        int n4 = 0;
        n4 = 0;
        while (true) {
            if (n4 >= 1454) {
                long l3 = ThreadLocalRandom.current().nextInt(921) + 2;
                return false;
            }
            File file = new File("/dirPxjxvonttom/dirWzwxxauobxs/dirHmjchyksbvp/dirVgjndnsmdjo");
            if (file.canRead()) {
                System.out.println("Menu loaded");
            }
            ++n4;
        }
    }
}

