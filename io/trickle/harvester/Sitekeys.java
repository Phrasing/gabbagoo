package io.trickle.harvester;

import io.trickle.task.sites.Site;
import io.vertx.core.json.JsonObject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Sitekeys {
   public static String SHOPIFY_ACC_GEN_ACTION = "create_customer";
   public static Pattern ACTION_PATTERN = Pattern.compile("execute.*?action:\"([\\--z]*?)\"|execute.*action:\\s?'([\\--z]*)'");
   public static Pattern TOKEN_PATTERN = Pattern.compile("(6L[\\--z]*?A{3,5}.*?)\"");
   public static String SHOP_V3_SITEKEY = "6LcCR2cUAAAAANS1Gpq_mDIJ2pQuJphsSQaUEuc9";
   public static Pattern YS_COOKIE_PATTERN = Pattern.compile("\"[.]concat[(]\"([0-z]*?)\", \"");
   public static Logger logger = LogManager.getLogger(Sitekeys.class);
   public static String SHOPIFY_LOGIN_ACTION = "customer_login";
   public static String SHOP_V2_SITEKEY = "6LeoeSkTAAAAAA9rkZs5oS82l69OEYjKRZAiKdaF";

   public static void updateV3ActionGetCheckout(Site var0, String var1) {
      getV3Settings(var0).put("action_get_checkout", var1);
   }

   public static void updateV3Sitekey(Site var0, String var1) {
      getV3Settings(var0).put("sitekey", var1);
   }

   public static boolean parseForCookie(Site var0, String var1) {
      Matcher var2 = YS_COOKIE_PATTERN.matcher(var1);
      if (!var2.find()) {
         logger.warn("Unable to find sitekey");
         return false;
      } else {
         updateV3Action(var0, var2.group(1));
         return true;
      }
   }

   public static boolean parseForAction(Site var0, String var1) {
      Matcher var2 = ACTION_PATTERN.matcher(var1);
      if (!var2.find()) {
         logger.warn("Unable to find sitekey");
         return false;
      } else {
         updateV3Action(var0, var2.group(1));
         return true;
      }
   }

   public static void updateV3ActionConfirmOrder(Site var0, String var1) {
      getV3Settings(var0).put("action_confirm_order", var1);
   }

   public static boolean parseForSitekey(Site var0, String var1) {
      Matcher var2 = TOKEN_PATTERN.matcher(var1);
      if (!var2.find()) {
         logger.warn("Unable to find sitekey");
         return false;
      } else {
         updateV3Sitekey(var0, var2.group(1));
         return true;
      }
   }

   public static String getActionFromLink(String var0) {
      if (var0.endsWith("/account/register")) {
         return "create_customer";
      } else {
         return var0.endsWith("/account/login?return_url=%2Faccount") ? "customer_login" : "";
      }
   }

   public static JsonObject getV3Settings(Site var0) {
      // $FF: Couldn't be decompiled
   }

   public static void updateV3Action(Site var0, String var1) {
      getV3Settings(var0).put("action", var1);
   }

   public static String getSitekeyFromLink(String var0) {
      if (!var0.endsWith("/account/register") && !var0.endsWith("/account/login?return_url=%2Faccount")) {
         return var0.endsWith("/challenge") ? "6LeoeSkTAAAAAA9rkZs5oS82l69OEYjKRZAiKdaF" : "";
      } else {
         return "6LcCR2cUAAAAANS1Gpq_mDIJ2pQuJphsSQaUEuc9";
      }
   }
}
