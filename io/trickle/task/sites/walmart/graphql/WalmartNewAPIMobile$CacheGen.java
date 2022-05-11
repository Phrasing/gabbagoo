package io.trickle.task.sites.walmart.graphql;

import io.trickle.util.Utils;
import io.vertx.core.json.JsonObject;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class WalmartNewAPIMobile$CacheGen {
   public static char[] cArr2 = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

   public static String hashForm(JsonObject var0, String var1) {
      JsonObject var2 = new JsonObject();
      var2.put("operationName", var0.getString("operationName"));
      var2.put("variables", var0.getJsonObject("variables"));
      var2.put("extensions", (new JsonObject()).put("persistedQuery", (new JsonObject()).put("version", 1).put("sha256Hash", var1)));
      var2.put("query", var0.getString("query"));
      return hash(var2.toString());
   }

   public static String mo17628e(byte[] var0) {
      char[] var1 = new char[var0.length * 2];
      int var2 = 0;
      byte[] var3 = var0;
      int var4 = var0.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         byte var6 = var3[var5];
         int var7 = var2 + 1;
         var1[var2] = cArr2[var6 >> 4 & 15];
         var2 = var7 + 1;
         var1[var7] = cArr2[var6 & 15];
      }

      return new String(var1);
   }

   public static String hash(String var0) {
      byte[] var1 = var0.getBytes(StandardCharsets.UTF_8);

      try {
         MessageDigest var2 = MessageDigest.getInstance("MD5");
         var2.update(var1, 0, var1.length);
         return mo17628e(var2.digest());
      } catch (NoSuchAlgorithmException var3) {
         var3.printStackTrace();
         return Utils.secureHexstring(16);
      }
   }
}
