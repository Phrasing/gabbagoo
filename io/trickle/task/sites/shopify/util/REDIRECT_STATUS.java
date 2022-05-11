package io.trickle.task.sites.shopify.util;

public enum REDIRECT_STATUS {
   public static REDIRECT_STATUS[] $VALUES = new REDIRECT_STATUS[]{OOS, CHECKPOINT, LOGIN_REQUIRED, BAD_ACCOUNT, QUEUE_OLD, QUEUE_NEW, PASSWORD, CHALLENGE, HOMEPAGE, CART, UNKNOWN};
   LOGIN_REQUIRED,
   UNKNOWN,
   HOMEPAGE,
   OOS,
   PASSWORD,
   BAD_ACCOUNT,
   QUEUE_NEW,
   CHECKPOINT,
   QUEUE_OLD,
   CART,
   CHALLENGE;

   public static REDIRECT_STATUS checkRedirectStatus(String var0) {
      String var1 = var0.toLowerCase();
      if (var1.contains("/stock_problems")) {
         return OOS;
      } else if (var1.contains("checkpoint")) {
         return CHECKPOINT;
      } else if (var1.contains("login?checkout_url=")) {
         return LOGIN_REQUIRED;
      } else if (var1.contains("/account/login?return_url=%2f")) {
         return BAD_ACCOUNT;
      } else if (var1.contains("/queue")) {
         return var1.contains("_ctd") ? QUEUE_OLD : QUEUE_NEW;
      } else if (var1.contains("password")) {
         return PASSWORD;
      } else if (var1.contains("/challenge")) {
         return CHALLENGE;
      } else if (var1.endsWith("/cart")) {
         return CART;
      } else {
         return var1.replace("https://", "").endsWith("/") ? HOMEPAGE : UNKNOWN;
      }
   }
}
