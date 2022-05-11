package io.trickle.task.sites.yeezy.util;

import io.vertx.core.json.JsonObject;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Sizes {
   public static Predicate availableSizeFilter = Sizes::lambda$static$0;

   public static String findAnyAvailable(List var0) {
      return (String)var0.stream().filter(availableSizeFilter).findAny().map(Sizes::lambda$findAnyAvailable$1).orElseGet(Sizes::lambda$findAnyAvailable$2);
   }

   public static JsonObject findAnyAvailableJSON(List var0) {
      List var1 = (List)var0.stream().filter(availableSizeFilter).collect(Collectors.toList());
      if (var1.isEmpty()) {
         throw new Sizes$NoAvailableSizeException();
      } else {
         return (JsonObject)var1.get(ThreadLocalRandom.current().nextInt(var1.size()));
      }
   }

   public static Sizes$SizePair getSize(String var0) {
      Objects.requireNonNull(var0);
      return var0.contains("10") ? selectSize(var0.trim()) : selectSize(var0.replace(".0", "").replace("0", "").trim());
   }

   public static String lambda$findAnyAvailable$1(JsonObject var0) {
      return var0.getString("size");
   }

   public static JsonObject findAnyAvailableOfRangeJSON(List var0, List var1) {
      List var2 = (List)var1.stream().filter(Sizes::lambda$findAnyAvailableOfRangeJSON$3).collect(Collectors.toList());

      try {
         return findAnyAvailableJSON(var2);
      } catch (Sizes$NoAvailableSizeException var4) {
         return var2.size() == 1 ? (JsonObject)var2.get(0) : (JsonObject)var2.get(ThreadLocalRandom.current().nextInt(var2.size()));
      }
   }

   public static boolean lambda$findAnyAvailableOfRangeJSON$3(List var0, JsonObject var1) {
      return var0.contains(var1.getString("size"));
   }

   public static Sizes$SizePair selectSize(String var0) {
      switch (var0) {
         case "4":
            return new Sizes$SizePair(var0, Sizes$Size._4_0);
         case "4.5":
            return new Sizes$SizePair(var0, Sizes$Size._4_5);
         case "5":
            return new Sizes$SizePair(var0, Sizes$Size._5_0);
         case "5.5":
            return new Sizes$SizePair(var0, Sizes$Size._5_5);
         case "6":
            return new Sizes$SizePair(var0, Sizes$Size._6_0);
         case "6.5":
            return new Sizes$SizePair(var0, Sizes$Size._6_5);
         case "7":
            return new Sizes$SizePair(var0, Sizes$Size._7_0);
         case "7.5":
            return new Sizes$SizePair(var0, Sizes$Size._7_5);
         case "8":
            return new Sizes$SizePair(var0, Sizes$Size._8_0);
         case "8.5":
            return new Sizes$SizePair(var0, Sizes$Size._8_5);
         case "9":
            return new Sizes$SizePair(var0, Sizes$Size._9_0);
         case "9.5":
            return new Sizes$SizePair(var0, Sizes$Size._9_5);
         case "10":
            return new Sizes$SizePair(var0, Sizes$Size._10_0);
         case "10.5":
            return new Sizes$SizePair(var0, Sizes$Size._10_5);
         case "11":
            return new Sizes$SizePair(var0, Sizes$Size._11_0);
         case "11.5":
            return new Sizes$SizePair(var0, Sizes$Size._11_5);
         case "12":
            return new Sizes$SizePair(var0, Sizes$Size._12_0);
         case "12.5":
            return new Sizes$SizePair(var0, Sizes$Size._12_5);
         case "13":
            return new Sizes$SizePair(var0, Sizes$Size._13_0);
         case "13.5":
            return new Sizes$SizePair(var0, Sizes$Size._13_5);
         case "14":
            return new Sizes$SizePair(var0, Sizes$Size._14_0);
         case "14.5":
            return new Sizes$SizePair(var0, Sizes$Size._14_5);
         case "15":
            return new Sizes$SizePair(var0, Sizes$Size._15_0);
         case "16":
            return new Sizes$SizePair(var0, Sizes$Size._16_0);
         case "17":
            return new Sizes$SizePair(var0, Sizes$Size._17_0);
         default:
            int var3 = Sizes$Size.values().length;

            try {
               Sizes$Size var4 = Sizes$Size.values()[ThreadLocalRandom.current().nextInt(var3)];
               return new Sizes$SizePair(var4.name().substring(1).replace("_0", "").replace("_", "."), var4);
            } catch (Throwable var5) {
               return new Sizes$SizePair("10.5", Sizes$Size._10_5);
            }
      }
   }

   public static JsonObject findAnyJSON(List var0) {
      return var0.size() == 1 ? (JsonObject)var0.get(0) : (JsonObject)var0.get(ThreadLocalRandom.current().nextInt(var0.size()));
   }

   public static String lambda$findAnyAvailable$2(List var0) {
      int var1 = ThreadLocalRandom.current().nextInt(var0.size());
      return ((JsonObject)var0.get(var1)).getString("size");
   }

   public static boolean lambda$static$0(JsonObject var0) {
      return var0.getInteger("availability", 0) > 0;
   }
}
