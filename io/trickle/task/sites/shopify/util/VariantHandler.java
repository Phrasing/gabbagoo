package io.trickle.task.sites.shopify.util;

import io.trickle.task.sites.shopify.Shopify;
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
   public static boolean matchesKeyword(JsonObject var0, List var1, List var2) {
      String var3 = var0.getString("title").toLowerCase();
      Iterator var4 = var1.iterator();

      String var5;
      do {
         if (!var4.hasNext()) {
            var4 = var2.iterator();

            do {
               if (!var4.hasNext()) {
                  return true;
               }

               var5 = (String)var4.next();
            } while(!var3.contains(var5));

            return false;
         }

         var5 = (String)var4.next();
      } while(var3.contains(var5));

      return false;
   }

   public static Triplet lambda$findPrecartVariant$0(JsonObject var0, JsonObject var1) {
      return new Triplet(var0.getNumber("id").toString(), false, var1);
   }

   public static String findVariantFromProduct(JsonObject var0, String var1, Shopify var2) {
      JsonArray var3 = var0.getJsonArray("variants");
      if (var1.equalsIgnoreCase("random")) {
         return var3.getJsonObject(ThreadLocalRandom.current().nextInt(var3.size())).getNumber("id").toString();
      } else {
         if (!var1.contains("&")) {
            for(int var9 = 0; var9 < var3.size(); ++var9) {
               JsonObject var10 = var3.getJsonObject(var9);
               if (var10.getString("title").equalsIgnoreCase(var1) || var10.getString("option1") != null && var10.getString("option1").equalsIgnoreCase(var1) || var10.getString("option2") != null && var10.getString("option2").equalsIgnoreCase(var1)) {
                  var2.size = var10.getString("title");
                  return var10.getNumber("id").toString();
               }
            }
         } else {
            List var4 = Arrays.asList(var1.split("&"));
            Collections.shuffle(var4);
            Iterator var5 = var4.iterator();

            while(var5.hasNext()) {
               String var6 = (String)var5.next();

               for(int var7 = 0; var7 < var3.size(); ++var7) {
                  JsonObject var8 = var3.getJsonObject(var7);
                  if (var8.getString("title").equalsIgnoreCase(var6) || var8.getString("option1") != null && var8.getString("option1").equalsIgnoreCase(var6) || var8.getString("option2") != null && var8.getString("option2").equalsIgnoreCase(var6)) {
                     var2.size = var8.getString("title");
                     return var8.getNumber("id").toString();
                  }
               }
            }
         }

         throw new Exception("Size not found [" + var1 + "]");
      }
   }

   public static String selectVariantFromLink(JsonObject var0, String var1, Shopify var2) {
      return findVariantFromProduct(var0, var1, var2);
   }

   public static String selectVariantFromKeyword(JsonObject var0, String var1, List var2, List var3, Shopify var4) {
      JsonArray var5 = var0.getJsonArray("products");
      if (var5 == null) {
         return null;
      } else {
         for(int var6 = 0; var6 < var5.size(); ++var6) {
            JsonObject var7 = var5.getJsonObject(var6);
            if (matchesKeyword(var7, var2, var3)) {
               return findVariantFromProduct(var7, var1, var4);
            }
         }

         return null;
      }
   }

   public static Triplet lambda$findPrecartVariantOOS$1(JsonObject var0, JsonObject var1) {
      return new Triplet(var0.getNumber("id").toString(), false, var1);
   }

   public static CompletableFuture findPrecartVariant(JsonArray var0) {
      Triplet var1 = null;
      if (var0 != null) {
         for(int var2 = var0.size() - 1; var2 >= 0; --var2) {
            JsonObject var3 = var0.getJsonObject(var2);
            JsonArray var4 = var3.getJsonArray("variants");

            for(int var5 = 0; var5 < var4.size(); ++var5) {
               JsonObject var6 = var4.getJsonObject(var5);
               if (var6.getBoolean("available")) {
                  if (var1 == null) {
                     var1 = new Triplet(var6.getNumber("id").toString(), true, var3);
                  }

                  String var7 = var3.encode();
                  if (var7.contains("sneaker") || var7.contains("BE@RBRICK") || var7.contains("footwear")) {
                     return CompletableFuture.completedFuture(new Triplet(var6.getNumber("id").toString(), true, var3));
                  }
               }

               if (var2 == 0) {
                  return CompletableFuture.completedFuture((Triplet)Objects.requireNonNullElseGet(var1, VariantHandler::lambda$findPrecartVariant$0));
               }
            }
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture findPrecartVariantOOS(JsonArray var0) {
      Object var1 = null;
      if (var0 != null) {
         for(int var2 = var0.size() - 1; var2 >= 0; --var2) {
            JsonObject var3 = var0.getJsonObject(var2);
            JsonArray var4 = var3.getJsonArray("variants");

            for(int var5 = 0; var5 < var4.size(); ++var5) {
               JsonObject var6 = var4.getJsonObject(var5);
               if (!var6.getBoolean("available")) {
                  return CompletableFuture.completedFuture(new Triplet(var6.getNumber("id").toString(), true, var3));
               }

               if (var2 == 0) {
                  return CompletableFuture.completedFuture((Triplet)Objects.requireNonNullElseGet(var1, VariantHandler::lambda$findPrecartVariantOOS$1));
               }
            }
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }
}
