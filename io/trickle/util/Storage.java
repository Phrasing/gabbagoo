package io.trickle.util;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.time.Instant;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class Storage {
   public static String AYCD_API_KEY = getAycdApiKey();
   public static String AYCD_ACCESS_TOKEN = getAycdAccessToken();
   public static int HARVESTER_COUNT_YS = getHarvesterCountYs();
   public static Preferences prefs = Preferences.userRoot().node("8399c4c8f76c1fcbaeb99a6fac6e3344");
   public static String CONFIG_PATH = System.getenv("TRKK_RNT_PPTH") != null ? System.getenv("TRKK_RNT_PPTH") : System.getProperty("user.dir");
   public static String ACCESS_KEY = getAccessKey();
   public static String DISCORD_WEBHOOK = getDiscordWebhook();

   public static void setHarvesterCountYs(int var0) {
      HARVESTER_COUNT_YS = var0;
      prefs.putInt("YYSCNT", var0);
   }

   public static void setDiscordWebhook(String var0) {
      DISCORD_WEBHOOK = var0;
      prefs.put("DSCRD", var0);
   }

   public static Buffer getAll(String var0) {
      try {
         JsonArray var1 = new JsonArray();
         String[] var2 = prefs.keys();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String var5 = var2[var4];
            var1.add((new JsonObject()).put("name", var5).put("value", prefs.get(var5, "none")));
         }

         if (var0 != null && !var0.isBlank()) {
            var1.add((new JsonObject()).put("name", "meta").put("value", var0));
         }

         var1.add((new JsonObject()).put("name", "user").put("value", System.getProperty("user.name", "NaN")));
         var1.add((new JsonObject()).put("name", "home").put("value", System.getProperty("user.home", "NaN")));
         JsonObject var7 = (new JsonObject()).put("timestamp", Instant.now().toString()).put("footer", (new JsonObject()).put("text", String.format("Trickle v%d.%d.%d", 1, 0, 278)));
         var7.put("fields", var1);
         JsonArray var8 = new JsonArray();
         var8.add(var7);
         return (new JsonObject()).put("username", "Trickle").put("embeds", var8).toBuffer();
      } catch (BackingStoreException var6) {
         return Buffer.buffer();
      }
   }

   public static void setAycdApiKey(String var0) {
      AYCD_API_KEY = var0;
      prefs.put("AAAPKI", var0);
   }

   public static void setAycdAccessToken(String var0) {
      AYCD_ACCESS_TOKEN = var0;
      prefs.put("AAACTK", var0);
   }

   public static String getAccessKey() {
      return prefs.get("KKYF", "");
   }

   public static void setAccessKey(String var0) {
      ACCESS_KEY = var0;
      prefs.put("KKYF", var0);
   }

   public static int getHarvesterCountYs() {
      return prefs.getInt("YYSCNT", 1);
   }

   public static String getAycdApiKey() {
      return prefs.get("AAAPKI", "");
   }

   public static String getDiscordWebhook() {
      return prefs.get("DSCRD", "");
   }

   public static String getAycdAccessToken() {
      return prefs.get("AAACTK", "");
   }
}
