package io.trickle.task.sites.bestbuy;

import io.trickle.util.Utils;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.Base64;
import java.util.Objects;
import java.util.regex.Pattern;
import javax.mail.search.SearchTerm;

public class Login {
   public static Pattern initDataPattern = Pattern.compile("var initData = (.*?});");
   public String challengeType;
   public JsonObject initData;
   public static Pattern ALPHA_PATTERN = Pattern.compile("^[0-9]+_A_.+$");
   public static SearchTerm SEARCH_TERM = new Login$1();
   public String flowOptions;
   public static Pattern CODE_PATTERN = Pattern.compile("^\\d+_X_.+$");

   public static Login loginValues(String var0) {
      return new Login(var0);
   }

   public String getAlpha() {
      JsonArray var1 = this.initData.getJsonArray("alpha");

      for(int var2 = 0; var2 < var1.size(); ++var2) {
         String var3 = new String(Base64.getDecoder().decode(Utils.reverseString(var1.getString(var2))));
         if (Utils.hasPattern(var3, ALPHA_PATTERN)) {
            return var1.getString(var2);
         }
      }

      return "no-alpha";
   }

   public Login(String var1) {
      this.initData = new JsonObject((String)Objects.requireNonNull(Utils.quickParseFirst(var1, initDataPattern)));
   }

   public String getCode() {
      JsonArray var1 = this.initData.getJsonArray("codeList");

      for(int var2 = 0; var2 < var1.size(); ++var2) {
         String var3 = new String(Base64.getDecoder().decode(var1.getString(var2)));
         if (Utils.hasPattern(var3, CODE_PATTERN)) {
            return var1.getString(var2);
         }
      }

      return "no-code";
   }
}
