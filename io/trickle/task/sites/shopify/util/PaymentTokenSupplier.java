package io.trickle.task.sites.shopify.util;

import java.util.function.Supplier;

public class PaymentTokenSupplier implements Supplier {
   public boolean vaulted;
   public String token;

   public PaymentTokenSupplier(String var1) {
      this.token = var1;
      this.vaulted = false;
   }

   /** @deprecated */
   @Deprecated
   public void setSubmittedSuccessfully() {
      this.token = "";
      this.vaulted = true;
   }

   public boolean isVaulted() {
      return this.vaulted;
   }

   public Object get() {
      return this.get();
   }

   public String get() {
      return this.token;
   }
}
