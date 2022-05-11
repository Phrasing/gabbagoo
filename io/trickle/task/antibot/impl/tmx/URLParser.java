package io.trickle.task.antibot.impl.tmx;

import io.trickle.util.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLParser {
   public static Pattern FUNCTION_VAR_PAT = Pattern.compile("(.*?) {0,2}= {0,2}new.*\\(\"(.*?)\"");
   public static Pattern ENCODED_STR_PAT = Pattern.compile("([.-z]*)\\(([0-9]*),([0-9]*)\\)");
   public Map decodingMap;
   public List urls;

   public void parseUrls(String var1) {
      Matcher var2 = ENCODED_STR_PAT.matcher(var1);
      System.out.println(this.decodingMap.keySet());

      while(var2.find()) {
         try {
            String[] var3 = var2.group(1).split("\\.");
            String var4 = var3.length == 2 ? var3[0] : var3[1];
            var4 = var4.replace("?", "").trim();
            String var5 = ((Decoding)this.decodingMap.get(var4)).td_f(Integer.parseInt(var2.group(2)), Integer.parseInt(var2.group(3)));
            if (var5.contains("http")) {
               this.urls.add(var5);
            }
         } catch (Exception var6) {
         }
      }

   }

   public void parseFuncVars(String var1) {
      Matcher var2 = FUNCTION_VAR_PAT.matcher(var1);

      while(var2.find()) {
         String[] var3 = var2.group(1).split("\\.");
         String var4 = var3.length == 1 ? var3[0] : var3[1];
         var4 = var4.replace("var", "").trim();
         String var5 = var2.group(2);
         this.decodingMap.put(var4, new Decoding(var5));
      }

   }

   public static void main(String[] var0) {
      URLParser var1 = new URLParser(Utils.readFileAsString("/Users/bayanrasooly/Documents/GitHub/TrickleV1.0/dev/bestbuy/tmx_raw.js"));
      System.out.println(var1.urls);
   }

   public URLParser(String var1) {
      var1 = var1.replace("{", "\n").replace("}", "\n").replace(";", "\n");
      this.decodingMap = new HashMap();
      this.urls = new ArrayList();
      this.parseFuncVars(var1);
      this.parseUrls(var1);
   }
}
