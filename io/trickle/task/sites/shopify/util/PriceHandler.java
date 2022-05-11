package io.trickle.task.sites.shopify.util;

import io.trickle.task.sites.Site;

public class PriceHandler {
   public static int calculateTax(Site var0, int var1) {
      // $FF: Couldn't be decompiled
   }

   public static boolean isCalculatingTaxes(String var0) {
      boolean var1 = false;
      return var0.contains("hidden\" data-checkout-taxes>");
   }

   public static int calculateShippingPrice(String var0) {
      String[] var1 = var0.split("-");
      return Integer.parseInt(var1[var1.length - 1].replace(".", ""));
   }
}
