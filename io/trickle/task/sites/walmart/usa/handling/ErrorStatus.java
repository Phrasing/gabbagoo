package io.trickle.task.sites.walmart.usa.handling;

import java.util.concurrent.CompletableFuture;

public enum ErrorStatus {
   UNKNOWN;

   public static ErrorStatus[] $VALUES = new ErrorStatus[]{UNKNOWN};

   public static CompletableFuture checkRedirectStatus(String var0) {
      if (var0.contains("CRT expired or empty")) {
         return CompletableFuture.failedFuture(new EmptyCartException());
      } else if (var0.contains("contract has expired")) {
         return CompletableFuture.failedFuture(new CheckoutExpiryException());
      } else {
         return var0.contains("Method Not Allowed") ? CompletableFuture.failedFuture(new BadSessionException()) : CompletableFuture.completedFuture(UNKNOWN);
      }
   }
}
