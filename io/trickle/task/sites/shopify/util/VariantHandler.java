/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.task.sites.shopify.Shopify
 *  io.trickle.task.sites.shopify.util.Triplet
 *  io.vertx.core.json.JsonArray
 *  io.vertx.core.json.JsonObject
 */
package io.trickle.task.sites.shopify.util;

import io.trickle.task.sites.shopify.Shopify;
import io.trickle.task.sites.shopify.util.Triplet;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public class VariantHandler {
    public static boolean matchesKeyword(JsonObject jsonObject, List list, List list2) {
        String string3;
        String string2 = jsonObject.getString("title").toLowerCase();
        for (String string3 : list) {
            if (string2.contains(string3)) continue;
            return false;
        }
        Iterator iterator = list2.iterator();
        do {
            if (!iterator.hasNext()) return true;
        } while (!string2.contains(string3 = (String)iterator.next()));
        return false;
    }

    public static Triplet lambda$findPrecartVariant$0(JsonObject jsonObject, JsonObject jsonObject2) {
        return new Triplet((Object)jsonObject.getNumber("id").toString(), (Object)false, (Object)jsonObject2);
    }

    public static String findVariantFromProduct(JsonObject jsonObject, String string, Shopify shopify) {
        int n;
        JsonArray jsonArray = jsonObject.getJsonArray("variants");
        if (string.equalsIgnoreCase("random")) {
            return jsonArray.getJsonObject(ThreadLocalRandom.current().nextInt(jsonArray.size())).getNumber("id").toString();
        }
        if (!string.contains("&")) {
            n = 0;
        } else {
            List<String> list = Arrays.asList(string.split("&"));
            Collections.shuffle(list);
            Iterator<String> iterator = list.iterator();
            block0: while (true) {
                if (!iterator.hasNext()) throw new Exception("Size not found [" + string + "]");
                String string2 = iterator.next();
                int n2 = 0;
                while (true) {
                    if (n2 >= jsonArray.size()) continue block0;
                    JsonObject jsonObject2 = jsonArray.getJsonObject(n2);
                    if (jsonObject2.getString("title").equalsIgnoreCase(string2) || jsonObject2.getString("option1") != null && jsonObject2.getString("option1").equalsIgnoreCase(string2) || jsonObject2.getString("option2") != null && jsonObject2.getString("option2").equalsIgnoreCase(string2)) {
                        shopify.size = jsonObject2.getString("title");
                        return jsonObject2.getNumber("id").toString();
                    }
                    ++n2;
                }
                break;
            }
        }
        while (n < jsonArray.size()) {
            JsonObject jsonObject3 = jsonArray.getJsonObject(n);
            if (jsonObject3.getString("title").equalsIgnoreCase(string) || jsonObject3.getString("option1") != null && jsonObject3.getString("option1").equalsIgnoreCase(string) || jsonObject3.getString("option2") != null && jsonObject3.getString("option2").equalsIgnoreCase(string)) {
                shopify.size = jsonObject3.getString("title");
                return jsonObject3.getNumber("id").toString();
            }
            ++n;
        }
        throw new Exception("Size not found [" + string + "]");
    }

    public static String selectVariantFromLink(JsonObject jsonObject, String string, Shopify shopify) {
        return VariantHandler.findVariantFromProduct(jsonObject, string, shopify);
    }

    public static String selectVariantFromKeyword(JsonObject jsonObject, String string, List list, List list2, Shopify shopify) {
        JsonArray jsonArray = jsonObject.getJsonArray("products");
        if (jsonArray == null) {
            return null;
        }
        int n = 0;
        while (n < jsonArray.size()) {
            JsonObject jsonObject2 = jsonArray.getJsonObject(n);
            if (VariantHandler.matchesKeyword(jsonObject2, list, list2)) return VariantHandler.findVariantFromProduct(jsonObject2, string, shopify);
            ++n;
        }
        return null;
    }

    public static Triplet lambda$findPrecartVariantOOS$1(JsonObject jsonObject, JsonObject jsonObject2) {
        return new Triplet((Object)jsonObject.getNumber("id").toString(), (Object)false, (Object)jsonObject2);
    }

    public static CompletableFuture findPrecartVariant(JsonArray jsonArray) {
        Triplet triplet = null;
        if (jsonArray == null) return CompletableFuture.completedFuture(null);
        int n = jsonArray.size() - 1;
        while (n >= 0) {
            JsonObject jsonObject = jsonArray.getJsonObject(n);
            JsonArray jsonArray2 = jsonObject.getJsonArray("variants");
            for (int i = 0; i < jsonArray2.size(); ++i) {
                JsonObject jsonObject2 = jsonArray2.getJsonObject(i);
                if (jsonObject2.getBoolean("available").booleanValue()) {
                    String string;
                    if (triplet == null) {
                        triplet = new Triplet((Object)jsonObject2.getNumber("id").toString(), (Object)true, (Object)jsonObject);
                    }
                    if ((string = jsonObject.encode()).contains("sneaker")) return CompletableFuture.completedFuture(new Triplet((Object)jsonObject2.getNumber("id").toString(), (Object)true, (Object)jsonObject));
                    if (string.contains("BE@RBRICK")) return CompletableFuture.completedFuture(new Triplet((Object)jsonObject2.getNumber("id").toString(), (Object)true, (Object)jsonObject));
                    if (string.contains("footwear")) {
                        return CompletableFuture.completedFuture(new Triplet((Object)jsonObject2.getNumber("id").toString(), (Object)true, (Object)jsonObject));
                    }
                }
                if (n != 0) continue;
                return CompletableFuture.completedFuture(Objects.requireNonNullElseGet(triplet, () -> VariantHandler.lambda$findPrecartVariant$0(jsonObject2, jsonObject)));
            }
            --n;
        }
        return CompletableFuture.completedFuture(null);
    }

    public static CompletableFuture findPrecartVariantOOS(JsonArray jsonArray) {
        Object t = null;
        if (jsonArray == null) return CompletableFuture.completedFuture(null);
        int n = jsonArray.size() - 1;
        while (n >= 0) {
            JsonObject jsonObject = jsonArray.getJsonObject(n);
            JsonArray jsonArray2 = jsonObject.getJsonArray("variants");
            for (int i = 0; i < jsonArray2.size(); ++i) {
                JsonObject jsonObject2 = jsonArray2.getJsonObject(i);
                if (!jsonObject2.getBoolean("available").booleanValue()) {
                    return CompletableFuture.completedFuture(new Triplet((Object)jsonObject2.getNumber("id").toString(), (Object)true, (Object)jsonObject));
                }
                if (n != 0) continue;
                return CompletableFuture.completedFuture(Objects.requireNonNullElseGet(t, () -> VariantHandler.lambda$findPrecartVariantOOS$1(jsonObject2, jsonObject)));
            }
            --n;
        }
        return CompletableFuture.completedFuture(null);
    }
}
