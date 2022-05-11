/*
 * Decompiled with CFR 0.152.
 */
package io.trickle.task.sites.yeezy.util;

public enum Sizes$Size {
    _4_0("530"),
    _4_5("540"),
    _5_0("550"),
    _5_5("560"),
    _6_0("570"),
    _6_5("580"),
    _7_0("590"),
    _7_5("600"),
    _8_0("610"),
    _8_5("620"),
    _9_0("630"),
    _9_5("640"),
    _10_0("650"),
    _10_5("660"),
    _11_0("670"),
    _11_5("680"),
    _12_0("690"),
    _12_5("700"),
    _13_0("710"),
    _13_5("720"),
    _14_0("730"),
    _14_5("740"),
    _15_0("750"),
    _16_0("760"),
    _17_0("780");

    public String text;

    public String toString() {
        return this.text;
    }

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    public Sizes$Size() {
        void var3_1;
        this.text = var3_1.contains("_") ? var3_1 : "_" + (String)var3_1;
    }
}
