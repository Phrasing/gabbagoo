package io.trickle.util;

import com.aayushatharva.brotli4j.Brotli4jLoader;
import com.aayushatharva.brotli4j.decoder.Decoder;
import com.aayushatharva.brotli4j.decoder.DirectDecompress;
import com.aayushatharva.brotli4j.decoder.DecoderJNI.Status;
import com.aayushatharva.brotli4j.encoder.Encoder;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.util.AsciiString;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.spi.CookieStore;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bouncycastle.util.encoders.Hex;

public class Utils {
   public static Pattern VER_PATTERN;
   public static DateTimeFormatter ISO_8901_US;
   public static CharSequence[] headers;
   public static DateTimeFormatter ISO_8901_JS;

   public static String leftpad(String var0, int var1) {
      return String.format("%" + var1 + "." + var1 + "s", var0);
   }

   public static boolean containsAllWords(String var0, String... var1) {
      if (var0 == null) {
         return false;
      } else if (var1.length == 0) {
         return true;
      } else {
         String[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String var5 = var2[var4];
            if (!containsIgnoreCase(var0, var5)) {
               return false;
            }
         }

         return true;
      }
   }

   public static double round(double var0, int var2) {
      double var3 = Math.pow(Double.longBitsToDouble(4621819117588971520L), (double)var2);
      return (double)Math.round(var0 * var3) / var3;
   }

   public static boolean hasPattern(String var0, Pattern var1) {
      return var1.matcher(var0).find();
   }

   public static String getRandomString() {
      int var0 = ThreadLocalRandom.current().nextInt(1, 32);
      return getString(var0);
   }

   public static String parseSafe(String var0, String var1, Supplier var2) {
      try {
         JsonObject var3 = new JsonObject(var0);
         if (var3.containsKey(var1)) {
            return var3.getString(var1, (String)var2.get());
         }
      } catch (Throwable var4) {
         return (String)var2.get();
      }

      return (String)var2.get();
   }

   public static String getRandomHeader() {
      return headers[ThreadLocalRandom.current().nextInt(headers.length)].toString();
   }

   public static String readFileAsString(String var0) {
      return new String(Files.readAllBytes(Paths.get(var0)));
   }

   public static String getString(int var0) {
      StringBuilder var1 = new StringBuilder();

      for(int var2 = 0; var2 < var0; ++var2) {
         if (ThreadLocalRandom.current().nextBoolean()) {
            var1.append(getRandomNumber());
         } else {
            var1.append(getRandomChar());
         }
      }

      return var1.toString();
   }

   public static String getMacAddress() {
      try {
         NetworkInterface var0 = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
         if (var0 == null) {
            return "NaN";
         } else {
            byte[] var1 = var0.getHardwareAddress();
            if (var1 == null) {
               return "NaN";
            } else {
               StringBuilder var2 = new StringBuilder(18);
               byte[] var3 = var1;
               int var4 = var1.length;

               for(int var5 = 0; var5 < var4; ++var5) {
                  byte var6 = var3[var5];
                  if (var2.length() > 0) {
                     var2.append(':');
                  }

                  var2.append(String.format("%02x", var6));
               }

               return var2.toString();
            }
         }
      } catch (Throwable var7) {
         return "NaN";
      }
   }

   public static String getLocalAddress() {
      try {
         return InetAddress.getLocalHost().toString();
      } catch (UnknownHostException var1) {
         return "NaN";
      }
   }

   public static List quickParseAll(String var0, Pattern var1) {
      Matcher var2 = var1.matcher(var0);

      ArrayList var3;
      for(var3 = null; var2.find(); var3.add(var2.group(1))) {
         if (var3 == null) {
            var3 = new ArrayList();
         }
      }

      return var3;
   }

   public static String fixedLenString(String var0, int var1) {
      return String.format("%1$" + var1 + "s", var0);
   }

   public static boolean containsAnyWords(String var0, String... var1) {
      if (var0 == null) {
         return false;
      } else if (var1.length == 0) {
         return true;
      } else {
         String[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String var5 = var2[var4];
            if (containsIgnoreCase(var0, var5)) {
               return true;
            }
         }

         return false;
      }
   }

   public static String rightpad(String var0, int var1) {
      return String.format("%-" + var1 + "." + var1 + "s", var0);
   }

   public static boolean containsIgnoreCase(String var0, String var1) {
      if (var0 != null && var1 != null) {
         int var2 = var1.length();
         if (var2 == 0) {
            return true;
         } else {
            for(int var3 = var0.length() - var2; var3 >= 0; --var3) {
               if (var0.regionMatches(true, var3, var1, 0, var2)) {
                  return true;
               }
            }

            return false;
         }
      } else {
         return false;
      }
   }

   public static char getRandomNumber() {
      return "1234567890".charAt(ThreadLocalRandom.current().nextInt("1234567890".length()));
   }

   public static void threadSleep(long var0) {
      try {
         Thread.sleep(var0);
      } catch (InterruptedException var3) {
         var3.printStackTrace();
      }

   }

   public static String readFile(String var0) {
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

   public static String getRandomNumber(int var0) {
      String var1 = "1234567890";
      StringBuilder var2 = new StringBuilder();

      for(int var3 = 0; var3 < var0; ++var3) {
         var2.append("1234567890".charAt(ThreadLocalRandom.current().nextInt("1234567890".length())));
      }

      return var2.toString();
   }

   static {
      ISO_8901_US = DateTimeFormatter.ISO_OFFSET_DATE_TIME.localizedBy(Locale.US);
      ISO_8901_JS = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX").withZone(ZoneId.of("UTC"));
      headers = new CharSequence[]{HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderNames.ACCEPT_RANGES, HttpHeaderNames.ACCESS_CONTROL_ALLOW_CREDENTIALS, HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS, HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS, HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, HttpHeaderNames.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaderNames.ACCESS_CONTROL_MAX_AGE, HttpHeaderNames.ACCESS_CONTROL_REQUEST_HEADERS, HttpHeaderNames.ACCESS_CONTROL_REQUEST_METHOD, HttpHeaderNames.AGE, HttpHeaderNames.ALLOW, HttpHeaderNames.CACHE_CONTROL, HttpHeaderNames.CONTENT_BASE, HttpHeaderNames.CONTENT_DISPOSITION, HttpHeaderNames.CONTENT_LANGUAGE, HttpHeaderNames.CONTENT_LOCATION, HttpHeaderNames.CONTENT_MD5, HttpHeaderNames.CONTENT_RANGE, HttpHeaderNames.DATE, HttpHeaderNames.ETAG, HttpHeaderNames.EXPECT, HttpHeaderNames.EXPIRES, HttpHeaderNames.IF_MATCH, HttpHeaderNames.IF_MODIFIED_SINCE, HttpHeaderNames.IF_NONE_MATCH, HttpHeaderNames.LAST_MODIFIED, HttpHeaderNames.LOCATION, HttpHeaderNames.ORIGIN, AsciiString.cached("vary")};
      VER_PATTERN = Pattern.compile("Chrome/([0-9][0-9])");
   }

   @SafeVarargs
   public static Object randomFrom(Object... var0) {
      return var0[ThreadLocalRandom.current().nextInt(var0.length)];
   }

   public static List quickParseAllGroups(String var0, Pattern var1) {
      Matcher var2 = var1.matcher(var0);
      ArrayList var3 = null;

      while(var2.find()) {
         if (var3 == null) {
            var3 = new ArrayList();
         }

         for(int var4 = 0; var4 < var2.groupCount(); ++var4) {
            var3.add(var2.group(var4 + 1));
         }
      }

      return var3;
   }

   public static int calculateMSLeftUntilHour() {
      LocalDateTime var0 = LocalDateTime.now();
      LocalDateTime var1 = var0.plusHours(1L).truncatedTo(ChronoUnit.HOURS);
      Duration var2 = Duration.between(var0, var1);
      return (int)var2.toMillis();
   }

   public static String encodedDateISO(Instant var0) {
      return ISO_8901_JS.format(var0).replace(":", "%3A");
   }

   public static String centerString(int var0, String var1) {
      return String.format("%-" + var0 + "s", String.format("%" + (var1.length() + (var0 - var1.length()) / 2) + "s", var1));
   }

   public static String quickParseFirst(String var0, Pattern... var1) {
      Pattern[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Pattern var5 = var2[var4];
         Matcher var6 = var5.matcher(var0);
         if (var6.find()) {
            return var6.group(1);
         }
      }

      return null;
   }

   public static String parseChromeVer(String var0) {
      Matcher var1 = VER_PATTERN.matcher(var0);
      return var1.find() ? var1.group(1) : "88";
   }

   public static char getRandomChar() {
      char var0 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt(ThreadLocalRandom.current().nextInt("ABCDEFGHIJKLMNOPQRSTUVWXYZ".length()));
      return ThreadLocalRandom.current().nextBoolean() ? Character.toLowerCase(var0) : var0;
   }

   public static String generateStrongString() {
      String var10000 = getRandomString(11);
      return var10000 + getRandomNumber(4);
   }

   public static JsonArray exportCookies(CookieStore var0) {
      DateTimeFormatter var1 = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy");
      JsonArray var2 = new JsonArray();
      Iterator var3 = var0.get(true, ".yeezysupply.com", "/").iterator();

      while(var3.hasNext()) {
         Cookie var4 = (Cookie)var3.next();
         JsonObject var5 = new JsonObject();
         var5.put("domain", var4.domain());
         var5.put("name", var4.name());
         var5.put("path", var4.path());
         var5.put("value", var4.value());
         var2.add(var5);
      }

      return var2;
   }

   public static String getStringCharacterOnly(int var0) {
      StringBuilder var1 = new StringBuilder();

      for(int var2 = 0; var2 < var0; ++var2) {
         var1.append(getRandomChar());
      }

      return var1.toString().toUpperCase(Locale.ROOT);
   }

   public static String centerStringBraced(int var0, String var1) {
      return String.format("[%-" + var0 + "s]", String.format("%" + (var1.length() + (var0 - var1.length()) / 2) + "s", var1));
   }

   public static boolean isInteger(String var0) {
      if (var0 == null) {
         return false;
      } else {
         int var1 = var0.length();
         if (var1 == 0) {
            return false;
         } else {
            int var2 = 0;
            if (var0.charAt(0) == '-') {
               if (var1 == 1) {
                  return false;
               }

               var2 = 1;
            }

            while(var2 < var1) {
               char var3 = var0.charAt(var2);
               if (var3 < '0' || var3 > '9') {
                  return false;
               }

               ++var2;
            }

            return true;
         }
      }
   }

   public static void ensureBrotli() {
      try {
         Brotli4jLoader.ensureAvailability();
         byte[] var0 = Encoder.compress("Meow".getBytes());
         DirectDecompress var1 = Decoder.decompress(var0);
         if (var1.getResultStatus() == Status.DONE) {
            return;
         }
      } catch (Throwable var2) {
      }

      System.out.println("Missing Windows libraries: Please download & install:  https://aka.ms/vs/17/release/vc_redist.x64.exe.  Failing to do so might yield unexpected results in some cases");
      System.out.println("Missing Windows libraries: Please download & install:  https://aka.ms/vs/17/release/vc_redist.x64.exe.  Failing to do so might yield unexpected results in some cases");
      System.out.println("Missing Windows libraries: Please download & install:  https://aka.ms/vs/17/release/vc_redist.x64.exe.  Failing to do so might yield unexpected results in some cases");
      System.out.println("Missing Windows libraries: Please download & install:  https://aka.ms/vs/17/release/vc_redist.x64.exe.  Failing to do so might yield unexpected results in some cases");
      System.out.println("Missing Windows libraries: Please download & install:  https://aka.ms/vs/17/release/vc_redist.x64.exe.  Failing to do so might yield unexpected results in some cases");
   }

   public static String getRandomString(int var0) {
      String var1 = "abcdefghijklmnopqrstuvwxyz0123456789";
      StringBuilder var2 = new StringBuilder();

      for(int var3 = 0; var3 < var0; ++var3) {
         var2.append("abcdefghijklmnopqrstuvwxyz0123456789".charAt(ThreadLocalRandom.current().nextInt("abcdefghijklmnopqrstuvwxyz0123456789".length())));
      }

      return var2.toString();
   }

   public static String secureHexstring(int var0) {
      byte[] var1 = new byte[var0];
      ThreadLocalRandom.current().nextBytes(var1);
      return Hex.toHexString(var1);
   }

   public static String reverseString(String var0) {
      return (new StringBuilder(var0)).reverse().toString();
   }

   public static double smartNextDouble() {
      double var0 = ThreadLocalRandom.current().nextDouble();
      if (("" + var0).contains("E")) {
         var0 = var0 * Double.longBitsToDouble(4611686018427387904L) + Double.longBitsToDouble(4587366580439587226L);
      }

      return var0;
   }

   public static String quickParseFirstNonEmpty(String var0, Pattern var1) {
      Matcher var2 = var1.matcher(var0);

      String var3;
      do {
         if (!var2.find()) {
            return null;
         }

         var3 = var2.group(1);
      } while(var3.isBlank());

      return var3;
   }

   public static String decodeHexString(String var0) {
      if (!var0.contains("\\x")) {
         return var0;
      } else {
         var0 = var0.replace("\\x", "").replace("\"", "");
         StringBuilder var1 = new StringBuilder();

         for(int var2 = 0; var2 < var0.length(); var2 += 2) {
            String var3 = var0.substring(var2, var2 + 2);
            var1.append((char)Integer.parseInt(var3, 16));
         }

         return var1.toString();
      }
   }
}
