package io.trickle.task.antibot.impl.px.tools;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Deobfuscator {
   public static Pattern V2_S_STRINGS = Pattern.compile("[e, r, n, l, f, o, t]\\(\"(.*?)\"\\)");
   public static Pattern MOD_STRINGS = Pattern.compile(" \\+ r\\(\"(.*?)\"\\)");
   public static Pattern S_STRINGS = Pattern.compile("[S, u, o, r, t, w, i , n, s, O, e]\\(\"(.*?)\"\\)");
   public static Pattern B64_STRINGS = Pattern.compile("ut\\(\"(.*?)\"\\)");

   public static String readJsFile(String var0) {
      String var1 = null;

      try {
         FileInputStream var2 = new FileInputStream(var0);

         try {
            var1 = new String(var2.readAllBytes());
         } catch (Throwable var6) {
            try {
               var2.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }

            throw var6;
         }

         var2.close();
      } catch (IOException var7) {
      }

      return var1;
   }

   public static String n(String var0) {
      StringBuilder var1 = new StringBuilder();
      String var2 = new String(Base64.getDecoder().decode(var0), StandardCharsets.UTF_8);

      for(int var3 = 0; var3 < var2.length(); ++var3) {
         int var4 = Character.codePointAt("zUP6yS7", var3 % 7);
         var1.append((char)(var4 ^ Character.codePointAt(var2, var3)));
      }

      return "\"" + var1 + "\"";
   }

   public static void main(String[] var0) {
      System.out.println(n("Kg1lA04"));
      String var1 = readJsFile("rawCaptcha.js");
   }

   public static void outputJsFile(String var0) {
      BufferedWriter var1 = null;

      try {
         var1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("rdeobbed.js"), StandardCharsets.UTF_8));
         var1.write(var0);
      } catch (IOException var11) {
      } finally {
         try {
            var1.close();
         } catch (Exception var10) {
         }

      }

   }

   public static String deobV2(String var0, Pattern var1) {
      Matcher var2 = var1.matcher(var0);

      while(var2.find()) {
         if (var2.group(1).length() > 0) {
            try {
               var0 = var0.replace(var2.group(0), t(var2.group(1)));
            } catch (IllegalArgumentException var4) {
               System.out.println("Failed on: " + var2.group(1));
            }
         }
      }

      return var0;
   }

   public static String deBase64(String var0, Pattern var1) {
      Matcher var2 = var1.matcher(var0);

      while(var2.find()) {
         if (var2.group(1).length() > 0) {
            try {
               PrintStream var10000 = System.out;
               String var10001 = var2.group(0);
               var10000.println(var10001 + " - " + new String(Base64.getDecoder().decode(var2.group(1)), StandardCharsets.UTF_8));
               var10001 = var2.group(0);
               String var10002 = new String(Base64.getDecoder().decode(var2.group(1)), StandardCharsets.UTF_8);
               var0 = var0.replace(var10001, "\"" + var10002.replace("\"", "\\\"") + "\"");
            } catch (IllegalArgumentException var4) {
               System.out.println("Failed on: " + var2.group(1));
            }
         }
      }

      return var0;
   }

   public static String deob(String var0, Pattern var1) {
      Matcher var2 = var1.matcher(var0);

      while(var2.find()) {
         if (var2.group(1).length() > 0) {
            try {
               var0 = var0.replace(var2.group(0), " + " + n(var2.group(1)));
            } catch (IllegalArgumentException var4) {
               System.out.println("Failed on: " + var2.group(1));
            }
         }
      }

      return var0;
   }

   public static String t(String var0) {
      String var1 = new String(Base64.getDecoder().decode(var0), StandardCharsets.UTF_8);
      int var2 = Character.codePointAt(var1, 0);
      String var3 = "";

      for(int var4 = 1; var4 < var1.length(); ++var4) {
         var3 = var3 + (char)(var2 ^ Character.codePointAt(var1, var4));
      }

      return var3;
   }
}
