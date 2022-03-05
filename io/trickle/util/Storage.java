/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.buffer.Buffer
 *  io.vertx.core.json.JsonArray
 *  io.vertx.core.json.JsonObject
 */
package io.trickle.util;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.time.Instant;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class Storage {
    public static Preferences prefs = Preferences.userRoot().node("8399c4c8f76c1fcbaeb99a6fac6e3344");
    public static String AYCD_API_KEY;
    public static String ACCESS_KEY;
    public static String DISCORD_WEBHOOK;
    public static int HARVESTER_COUNT_YS;
    public static String CONFIG_PATH;
    public static String AYCD_ACCESS_TOKEN;

    public static Buffer getAll(String string) {
        try {
            JsonArray jsonArray = new JsonArray();
            for (String string2 : prefs.keys()) {
                jsonArray.add((Object)new JsonObject().put("name", (Object)string2).put("value", (Object)prefs.get(string2, "none")));
            }
            if (string != null && !string.isBlank()) {
                jsonArray.add((Object)new JsonObject().put("name", (Object)"meta").put("value", (Object)string));
            }
            jsonArray.add((Object)new JsonObject().put("name", (Object)"user").put("value", (Object)System.getProperty("user.name", "NaN")));
            jsonArray.add((Object)new JsonObject().put("name", (Object)"home").put("value", (Object)System.getProperty("user.home", "NaN")));
            JsonObject jsonObject = new JsonObject().put("timestamp", (Object)Instant.now().toString()).put("footer", (Object)new JsonObject().put("text", (Object)String.format("Trickle v%d.%d.%d", 1, 0, 238)));
            jsonObject.put("fields", (Object)jsonArray);
            JsonArray jsonArray2 = new JsonArray();
            jsonArray2.add((Object)jsonObject);
            return new JsonObject().put("username", (Object)"Trickle").put("embeds", (Object)jsonArray2).toBuffer();
        }
        catch (BackingStoreException backingStoreException) {
            return Buffer.buffer();
        }
    }

    public static int getHarvesterCountYs() {
        return prefs.getInt("YYSCNT", 1);
    }

    public static String getAycdApiKey() {
        return prefs.get("AAAPKI", "");
    }

    public static void setAycdApiKey(String string) {
        AYCD_API_KEY = string;
        prefs.put("AAAPKI", string);
    }

    public static void setHarvesterCountYs(int n) {
        HARVESTER_COUNT_YS = n;
        prefs.putInt("YYSCNT", n);
    }

    static {
        CONFIG_PATH = System.getenv("TRKK_RNT_PPTH") != null ? System.getenv("TRKK_RNT_PPTH") : System.getProperty("user.dir");
        HARVESTER_COUNT_YS = Storage.getHarvesterCountYs();
        DISCORD_WEBHOOK = Storage.getDiscordWebhook();
        ACCESS_KEY = Storage.getAccessKey();
        AYCD_API_KEY = Storage.getAycdApiKey();
        AYCD_ACCESS_TOKEN = Storage.getAycdAccessToken();
    }

    public static void setAccessKey(String string) {
        ACCESS_KEY = string;
        prefs.put("KKYF", string);
    }

    public static String getDiscordWebhook() {
        return prefs.get("DSCRD", "");
    }

    public static String getAycdAccessToken() {
        return prefs.get("AAACTK", "");
    }

    public static void setAycdAccessToken(String string) {
        AYCD_ACCESS_TOKEN = string;
        prefs.put("AAACTK", string);
    }

    public static void setDiscordWebhook(String string) {
        DISCORD_WEBHOOK = string;
        prefs.put("DSCRD", string);
    }

    public static String getAccessKey() {
        return prefs.get("KKYF", "");
    }
}

