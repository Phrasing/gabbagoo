/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.task.sites.yeezy.util.Sizes$Size
 */
package io.trickle.task.sites.yeezy.util;

import io.trickle.task.sites.yeezy.util.Sizes;

public class Sizes$SizePair {
    public String sizeNum;
    public Sizes.Size sizeVariant;

    public Sizes$SizePair(String string, Sizes.Size size) {
        this.sizeNum = string;
        this.sizeVariant = size;
    }

    public String toString() {
        return this.sizeVariant.toString();
    }
}
