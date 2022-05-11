package io.trickle.task.sites.shopify.util;

import java.util.function.Supplier;

public class ShippingRateSupplier implements Supplier {
   public String rate;

   public String get() {
      return this.rate;
   }

   public void updateRate(String var1) {
      this.rate = var1;
   }

   public Object get() {
      return this.get();
   }

   public ShippingRateSupplier(String var1) {
      this.rate = var1;
   }
}
