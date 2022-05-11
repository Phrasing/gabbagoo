package io.trickle.profile;

import java.util.regex.Pattern;

public enum CardType {
   VISA("^4[0-9]{12}(?:[0-9]{3}){0,2}$");

   public static CardType[] $VALUES = new CardType[]{UNKNOWN, VISA, MASTERCARD, AMEX, DINERS_CLUB, DISCOVER, JCB, CHINA_UNION_PAY};
   DISCOVER("^6(?:011|[45][0-9]{2})[0-9]{12}$"),
   CHINA_UNION_PAY("^62[0-9]{14,17}$"),
   MASTERCARD("^(?:5[1-5]|2(?!2([01]|20)|7(2[1-9]|3))[2-7])\\d{14}$"),
   UNKNOWN;

   public Pattern pattern;
   DINERS_CLUB("^3(?:0[0-5]\\d|095|6\\d{0,2}|[89]\\d{2})\\d{12,15}$"),
   AMEX("^3[47][0-9]{13}$"),
   JCB("^(?:2131|1800|35\\d{3})\\d{11}$");

   public CardType() {
      this.pattern = null;
   }

   public static CardType detect(String var0) {
      CardType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         CardType var4 = var1[var3];
         if (null != var4.pattern && var4.pattern.matcher(var0).matches()) {
            return var4;
         }
      }

      return UNKNOWN;
   }

   public CardType(String var3) {
      this.pattern = Pattern.compile(var3);
   }
}
