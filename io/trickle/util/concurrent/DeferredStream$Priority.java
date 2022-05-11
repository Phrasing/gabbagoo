/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.util.concurrent.DeferredStream$1
 */
package io.trickle.util.concurrent;

import io.trickle.util.concurrent.DeferredStream;

public enum DeferredStream$Priority {
    INSTANT,
    HIGH,
    MEDIUM,
    LOW;


    public static int convert(DeferredStream$Priority deferredStream$Priority) {
        switch (DeferredStream.1.$SwitchMap$io$trickle$util$concurrent$DeferredStream$Priority[deferredStream$Priority.ordinal()]) {
            case 1: {
                return Integer.MAX_VALUE;
            }
            case 2: {
                return 100;
            }
            case 3: {
                return 10;
            }
            case 4: {
                return 4;
            }
        }
        return 10;
    }
}
