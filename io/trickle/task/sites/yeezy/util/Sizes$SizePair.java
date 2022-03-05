/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.sites.yeezy.util;

import io.trickle.task.sites.yeezy.util.Sizes$Size;

public class Sizes$SizePair {
    public Sizes$Size sizeVariant;
    public String sizeNum;

    public Sizes$SizePair(String string, Sizes$Size sizes$Size) {
        this.sizeNum = string;
        this.sizeVariant = sizes$Size;
    }

    public String toString() {
        return this.sizeVariant.toString();
    }
}

