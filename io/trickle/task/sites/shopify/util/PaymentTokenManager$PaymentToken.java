package io.trickle.task.sites.shopify.util;

import java.time.Instant;

public class PaymentTokenManager$PaymentToken {
   public String value;
   public long expiry;

   public PaymentTokenManager$PaymentToken(String var1) {
      this.value = var1;
      this.expiry = Instant.now().getEpochSecond() + 1740L;
   }

   public boolean isExpired() {
      return Instant.now().getEpochSecond() - this.expiry > 0L;
   }
}
