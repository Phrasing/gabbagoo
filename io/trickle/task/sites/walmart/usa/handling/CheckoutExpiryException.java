package io.trickle.task.sites.walmart.usa.handling;

public class CheckoutExpiryException extends Exception {
   public CheckoutExpiryException() {
      super("PCID Expired");
   }
}
