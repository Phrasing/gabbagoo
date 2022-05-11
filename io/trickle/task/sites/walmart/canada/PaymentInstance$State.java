package io.trickle.task.sites.walmart.canada;

public enum PaymentInstance$State {
   PROCEED_CHECKOUT,
   FAILED_INIT,
   NO_STOCK;

   public static PaymentInstance$State[] $VALUES = new PaymentInstance$State[]{FAILED_INIT, PROCEED_CHECKOUT, NO_STOCK};
}
