package io.trickle.task.antibot.impl.px.payload.captcha.util;

import io.vertx.core.json.JsonArray;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeviceHeaderParsers {
   public static Pattern CHROME_VERSION_PATTERN = Pattern.compile("\\sChrome/([0-9][0-9])");
   public String[] SEPERATORS = new String[]{"\\", "\"", ";", " "};

   public static String getSecUA(String var0) {
      Matcher var1 = CHROME_VERSION_PATTERN.matcher(var0);
      if (!var1.find()) {
         return null;
      } else {
         String var2 = var1.group(1);
         return "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"" + var2 + "\", \"Google Chrome\";v=\"" + var2 + "\"";
      }
   }

   public static String getAcceptLanguage(JsonArray var0) {
      String var10000 = var0.toString().replace("[", "").replace("]", "");
      return var10000.replace("\"", "") + ";q=0.9";
   }
}
