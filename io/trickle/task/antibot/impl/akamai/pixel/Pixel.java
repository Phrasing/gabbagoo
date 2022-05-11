package io.trickle.task.antibot.impl.akamai.pixel;

import java.util.Base64;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Pixel {
   Pattern BAZA_PATTERN = Pattern.compile("baz[A-z]*=?\"([0-9]*?)\"");
   Pattern T_PATTERN = Pattern.compile("t=([0-z]*)");
   Pattern G_INDEX = Pattern.compile("g=_\\[([0-9]*?)]");
   Pattern AKAM_PATTERN = Pattern.compile("(https://www.yeezysupply.com/akam.*?)\"");
   Pattern HEX_ARRAY = Pattern.compile("=\\[(\".*\\\\x.*?)\\];");

   CompletableFuture getPixelReqForm(String var1, String var2, String var3);

   static String[] parseAkam(String var0) {
      Matcher var1 = AKAM_PATTERN.matcher(var0);
      String[] var2 = new String[2];

      for(int var3 = 0; var1.find(); ++var3) {
         var2[var3] = var1.group(1);
      }

      if (var2[1] != null) {
         return var2;
      } else {
         throw new Exception("No AKAM");
      }
   }

   static String[] parseHexArray(String var0) {
      Matcher var1 = HEX_ARRAY.matcher(var0);
      if (var1.find()) {
         String[] var2 = var1.group(1).split(",");
         return decodeArray(var2);
      } else {
         throw new Exception("No HEX ARR");
      }
   }

   static int parseGIndex(String var0) {
      Matcher var1 = G_INDEX.matcher(var0);
      if (var1.find()) {
         return Integer.parseInt(var1.group(1));
      } else {
         throw new Exception("No G INDEX");
      }
   }

   static String parseTValue(String var0) {
      String var1 = var0.split("\\?")[1];
      var1 = var1.substring(var1.indexOf("=") + 1);
      if (var1.contains("&")) {
         var1 = var1.split("&")[0];
      }

      var1 = new String(Base64.getDecoder().decode(var1));
      Matcher var2 = T_PATTERN.matcher(var1);
      if (var2.find()) {
         return var2.group(1);
      } else {
         throw new Exception("No T val");
      }
   }

   static String decodeHexString(String var0) {
      var0 = var0.replace("\\x", "").replace("\"", "");
      StringBuilder var1 = new StringBuilder();

      for(int var2 = 0; var2 < var0.length(); var2 += 2) {
         String var3 = var0.substring(var2, var2 + 2);
         var1.append((char)Integer.parseInt(var3, 16));
      }

      return var1.toString();
   }

   CompletableFuture getPixelReqString(String var1, String var2, String var3);

   static String parseBaza(String var0) {
      Matcher var1 = BAZA_PATTERN.matcher(var0);
      if (var1.find()) {
         return var1.group(1);
      } else {
         throw new Exception("No BAZA");
      }
   }

   static String[] decodeArray(String[] var0) {
      for(int var1 = 0; var1 < var0.length; ++var1) {
         var0[var1] = decodeHexString(var0[var1]);
      }

      return var0;
   }
}
