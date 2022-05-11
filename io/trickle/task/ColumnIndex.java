package io.trickle.task;

public enum ColumnIndex {
   MONITOR_DELAY(18),
   SITE(19),
   PHONE_NUMBER(5),
   EXPIRY_MO(13),
   KEYWORDS(0),
   CVV(15);

   public static ColumnIndex[] $VALUES = new ColumnIndex[]{KEYWORDS, SIZE, FIRST_NAME, LAST_NAME, EMAIL, PHONE_NUMBER, ADDRESS_1, ADDRESS_2, STATE, CITY, ZIP_CODE, COUNTRY, CARD_NO, EXPIRY_MO, EXPIRY_YR, CVV, TASK_QTY, RETRY_DELAY, MONITOR_DELAY, SITE, MODE, CAPTCHA_KEY, SHIPPING_RATE};
   ZIP_CODE(10),
   ADDRESS_2(7),
   SHIPPING_RATE(22),
   FIRST_NAME(2),
   SIZE(1),
   LAST_NAME(3),
   CITY(9);

   public int index;
   CAPTCHA_KEY(21),
   TASK_QTY(16),
   MODE(20),
   COUNTRY(11),
   CARD_NO(12),
   RETRY_DELAY(17),
   ADDRESS_1(6),
   STATE(8),
   EXPIRY_YR(14),
   EMAIL(4);

   public ColumnIndex(int var3) {
      this.index = var3;
   }
}
