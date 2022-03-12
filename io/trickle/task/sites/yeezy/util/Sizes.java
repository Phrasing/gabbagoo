/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.json.JsonObject
 */
package io.trickle.task.sites.yeezy.util;

import io.trickle.task.sites.yeezy.util.Sizes$NoAvailableSizeException;
import io.trickle.task.sites.yeezy.util.Sizes$Size;
import io.trickle.task.sites.yeezy.util.Sizes$SizePair;
import io.vertx.core.json.JsonObject;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Sizes {
    public static Predicate<JsonObject> availableSizeFilter = Sizes::lambda$static$0;

    public static JsonObject findAnyJSON(List list) {
        if (list.size() != 1) return (JsonObject)list.get(ThreadLocalRandom.current().nextInt(list.size()));
        return (JsonObject)list.get(0);
    }

    public static boolean lambda$static$0(JsonObject jsonObject) {
        if (jsonObject.getInteger("availability", Integer.valueOf(0)) <= 0) return false;
        return true;
    }

    public static String findAnyAvailable(List list) {
        return list.stream().filter(availableSizeFilter).findAny().map(Sizes::lambda$findAnyAvailable$1).orElseGet(() -> Sizes.lambda$findAnyAvailable$2(list));
    }

    public static boolean lambda$findAnyAvailableOfRangeJSON$3(List list, JsonObject jsonObject) {
        return list.contains(jsonObject.getString("size"));
    }

    public static JsonObject findAnyAvailableJSON(List list) {
        List list2 = list.stream().filter(availableSizeFilter).collect(Collectors.toList());
        if (!list2.isEmpty()) return (JsonObject)list2.get(ThreadLocalRandom.current().nextInt(list2.size()));
        throw new Sizes$NoAvailableSizeException();
    }

    public static String lambda$findAnyAvailable$1(JsonObject jsonObject) {
        return jsonObject.getString("size");
    }

    public static Sizes$SizePair selectSize(String string) {
        switch (string) {
            case "4": {
                return new Sizes$SizePair(string, Sizes$Size._4_0);
            }
            case "4.5": {
                return new Sizes$SizePair(string, Sizes$Size._4_5);
            }
            case "5": {
                return new Sizes$SizePair(string, Sizes$Size._5_0);
            }
            case "5.5": {
                return new Sizes$SizePair(string, Sizes$Size._5_5);
            }
            case "6": {
                return new Sizes$SizePair(string, Sizes$Size._6_0);
            }
            case "6.5": {
                return new Sizes$SizePair(string, Sizes$Size._6_5);
            }
            case "7": {
                return new Sizes$SizePair(string, Sizes$Size._7_0);
            }
            case "7.5": {
                return new Sizes$SizePair(string, Sizes$Size._7_5);
            }
            case "8": {
                return new Sizes$SizePair(string, Sizes$Size._8_0);
            }
            case "8.5": {
                return new Sizes$SizePair(string, Sizes$Size._8_5);
            }
            case "9": {
                return new Sizes$SizePair(string, Sizes$Size._9_0);
            }
            case "9.5": {
                return new Sizes$SizePair(string, Sizes$Size._9_5);
            }
            case "10": {
                return new Sizes$SizePair(string, Sizes$Size._10_0);
            }
            case "10.5": {
                return new Sizes$SizePair(string, Sizes$Size._10_5);
            }
            case "11": {
                return new Sizes$SizePair(string, Sizes$Size._11_0);
            }
            case "11.5": {
                return new Sizes$SizePair(string, Sizes$Size._11_5);
            }
            case "12": {
                return new Sizes$SizePair(string, Sizes$Size._12_0);
            }
            case "12.5": {
                return new Sizes$SizePair(string, Sizes$Size._12_5);
            }
            case "13": {
                return new Sizes$SizePair(string, Sizes$Size._13_0);
            }
            case "13.5": {
                return new Sizes$SizePair(string, Sizes$Size._13_5);
            }
            case "14": {
                return new Sizes$SizePair(string, Sizes$Size._14_0);
            }
            case "14.5": {
                return new Sizes$SizePair(string, Sizes$Size._14_5);
            }
            case "15": {
                return new Sizes$SizePair(string, Sizes$Size._15_0);
            }
            case "16": {
                return new Sizes$SizePair(string, Sizes$Size._16_0);
            }
            case "17": {
                return new Sizes$SizePair(string, Sizes$Size._17_0);
            }
        }
        int n = Sizes$Size.values().length;
        try {
            Sizes$Size sizes$Size = Sizes$Size.values()[ThreadLocalRandom.current().nextInt(n)];
            return new Sizes$SizePair(sizes$Size.name().substring(1).replace("_0", "").replace("_", "."), sizes$Size);
        }
        catch (Throwable throwable) {
            return new Sizes$SizePair("10.5", Sizes$Size._10_5);
        }
    }

    public static Sizes$SizePair getSize(String string) {
        Objects.requireNonNull(string);
        if (!string.contains("10")) return Sizes.selectSize(string.replace(".0", "").replace("0", "").trim());
        return Sizes.selectSize(string.trim());
    }

    public static String lambda$findAnyAvailable$2(List list) {
        int n = ThreadLocalRandom.current().nextInt(list.size());
        return ((JsonObject)list.get(n)).getString("size");
    }

    public static JsonObject findAnyAvailableOfRangeJSON(List list, List list2) {
        List list3 = list2.stream().filter(arg_0 -> Sizes.lambda$findAnyAvailableOfRangeJSON$3(list, arg_0)).collect(Collectors.toList());
        try {
            return Sizes.findAnyAvailableJSON(list3);
        }
        catch (Sizes$NoAvailableSizeException sizes$NoAvailableSizeException) {
            if (list3.size() != 1) return (JsonObject)list3.get(ThreadLocalRandom.current().nextInt(list3.size()));
            return (JsonObject)list3.get(0);
        }
    }
}

