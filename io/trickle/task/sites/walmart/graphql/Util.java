package io.trickle.task.sites.walmart.graphql;

import io.trickle.util.Utils;
import java.util.regex.Pattern;

public class Util {
   public static Pattern ERROR_MSG_PATTERN = Pattern.compile("\"message\":\"(.*?)\"");
   public static Pattern MOBILE_MSG_PATTERN = Pattern.compile("\"errors\":.*?\"code\":\"(.*?)\"");

   public static String parseErrorMessage(String var0) {
      return Utils.quickParseFirst(var0, ERROR_MSG_PATTERN, MOBILE_MSG_PATTERN);
   }
}
