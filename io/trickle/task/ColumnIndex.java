/*
 * Decompiled with CFR 0.152.
 */
package io.trickle.task;

public enum ColumnIndex {
    KEYWORDS(0),
    SIZE(1),
    FIRST_NAME(2),
    LAST_NAME(3),
    EMAIL(4),
    PHONE_NUMBER(5),
    ADDRESS_1(6),
    ADDRESS_2(7),
    STATE(8),
    CITY(9),
    ZIP_CODE(10),
    COUNTRY(11),
    CARD_NO(12),
    EXPIRY_MO(13),
    EXPIRY_YR(14),
    CVV(15),
    TASK_QTY(16),
    RETRY_DELAY(17),
    MONITOR_DELAY(18),
    SITE(19),
    MODE(20),
    CAPTCHA_KEY(21),
    SHIPPING_RATE(22);

    public int index;

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    public ColumnIndex() {
        void var3_1;
        this.index = var3_1;
    }
}
