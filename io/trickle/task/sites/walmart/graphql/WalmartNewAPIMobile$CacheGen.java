/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.json.JsonObject
 */
package io.trickle.task.sites.walmart.graphql;

import io.trickle.util.Utils;
import io.vertx.core.json.JsonObject;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class WalmartNewAPIMobile$CacheGen {
    public static char[] cArr2 = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String mo17628e(byte[] byArray) {
        char[] cArray = new char[byArray.length * 2];
        int n = 0;
        byte[] byArray2 = byArray;
        int n2 = byArray2.length;
        int n3 = 0;
        while (n3 < n2) {
            byte by = byArray2[n3];
            int n4 = n + 1;
            cArray[n] = cArr2[by >> 4 & 0xF];
            n = n4 + 1;
            cArray[n4] = cArr2[by & 0xF];
            ++n3;
        }
        return new String(cArray);
    }

    public static String hash(String string) {
        byte[] byArray = string.getBytes(StandardCharsets.UTF_8);
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(byArray, 0, byArray.length);
            return WalmartNewAPIMobile$CacheGen.mo17628e(messageDigest.digest());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            noSuchAlgorithmException.printStackTrace();
            return Utils.secureHexstring(16);
        }
    }

    public static String hashForm(JsonObject jsonObject, String string) {
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.put("operationName", (Object)jsonObject.getString("operationName"));
        jsonObject2.put("variables", (Object)jsonObject.getJsonObject("variables"));
        jsonObject2.put("extensions", (Object)new JsonObject().put("persistedQuery", (Object)new JsonObject().put("version", (Object)1).put("sha256Hash", (Object)string)));
        jsonObject2.put("query", (Object)jsonObject.getString("query"));
        return WalmartNewAPIMobile$CacheGen.hash(jsonObject2.toString());
    }
}

