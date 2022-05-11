package io.trickle.task.antibot.impl.px.payload;

import io.trickle.task.antibot.impl.px.Types;
import io.trickle.task.sites.Site;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public interface ExtendedPayload extends Payload {
   String getSID();

   Site getSite();

   Types getType();

   String getUUID();

   static String decode(String var0, int var1) {
      var0 = var0.replace(" ", "+");
      String var2 = new String(Base64.getDecoder().decode(var0.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
      StringBuilder var3 = new StringBuilder();

      for(int var4 = 0; var4 < var2.length(); ++var4) {
         var3.append((char)(var1 ^ Character.codePointAt(var2, var4)));
      }

      return var3.toString();
   }

   String getVID();

   String desktopString();

   static String encode(String var0, int var1) {
      StringBuilder var2 = new StringBuilder();

      for(int var3 = 0; var3 < var0.length(); ++var3) {
         var2.append((char)(var1 ^ Character.codePointAt(var0, var3)));
      }

      return (new String(Base64.getEncoder().encode(var2.toString().getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8)).replace(" ", "+");
   }

   String toString();

   String getUserAgent();

   static String encode(String var0, String var1) {
      return (new String(Base64.getEncoder().encode("".getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8)).replace(" ", "+");
   }
}
