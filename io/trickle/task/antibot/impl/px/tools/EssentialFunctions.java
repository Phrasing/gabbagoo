package io.trickle.task.antibot.impl.px.tools;

import io.trickle.task.antibot.impl.px.payload.captcha.util.TimezoneResource;
import io.vertx.core.json.JsonArray;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.TimeZone;

public class EssentialFunctions {
   public static String dateNow(String var0) {
      Instant var1 = Instant.now();
      ZoneId var2 = ZoneId.systemDefault();
      String var3 = var2.getRules().getOffset(var1).toString().replace(":", "");
      TimeZone var4 = TimeZone.getDefault();
      return (new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss")).format(var1.toEpochMilli()) + " GMT" + var3 + " (" + var4.getDisplayName(false, 1, TimezoneResource.getLocalFromLanguage(var0)) + ")";
   }

   public static String encodeURIComponent(String var0) {
      String var1 = URLEncoder.encode(var0, StandardCharsets.UTF_8).replaceAll("\\+", "%20").replaceAll("\\%21", "!").replaceAll("\\%27", "'").replaceAll("\\%28", "(").replaceAll("\\%29", ")").replaceAll("\\%7E", "~");
      return var1;
   }

   public static double roundTwoDecimals(double var0) {
      return (double)Math.round(var0 * Double.longBitsToDouble(4636737291354636288L)) / Double.longBitsToDouble(4636737291354636288L);
   }

   public static JsonArray convertArrToJson(String[] var0) {
      String var1 = Arrays.toString(var0).replace("[", "[\"").replace(", ", "\", \"").replace("]", "\"]");
      return var0.length == 0 ? new JsonArray() : new JsonArray(var1);
   }
}
