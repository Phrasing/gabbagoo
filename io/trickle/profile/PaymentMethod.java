package io.trickle.profile;

public enum PaymentMethod {
   public static PaymentMethod[] $VALUES = new PaymentMethod[]{VISA, AMEX, DISCOVER, MASTERCARD};
   DISCOVER("DISCOVER");

   public String method;
   MASTERCARD("MASTERCARD"),
   AMEX("AMEX"),
   VISA("VISA");

   public PaymentMethod(String var3) {
      this.method = var3;
   }

   public String getFirstLetterUppercase() {
      char var10000 = this.method.charAt(0);
      return "" + var10000 + this.method.substring(1).toLowerCase();
   }

   public static PaymentMethod detectMethod(String var0) {
      switch (var0.charAt(0)) {
         case '3':
            return AMEX;
         case '4':
            return VISA;
         case '5':
         default:
            return MASTERCARD;
         case '6':
            return DISCOVER;
      }
   }

   public String get() {
      return this.method;
   }
}
