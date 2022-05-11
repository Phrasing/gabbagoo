/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.util.concurrent.DeferredStream$Priority
 */
package io.trickle.util.concurrent;

import io.trickle.util.concurrent.DeferredStream;

public class DeferredStream$1 {
    public static int[] $SwitchMap$io$trickle$util$concurrent$DeferredStream$Priority = new int[DeferredStream.Priority.values().length];

    static {
        try {
            DeferredStream$1.$SwitchMap$io$trickle$util$concurrent$DeferredStream$Priority[DeferredStream.Priority.INSTANT.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            DeferredStream$1.$SwitchMap$io$trickle$util$concurrent$DeferredStream$Priority[DeferredStream.Priority.HIGH.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            DeferredStream$1.$SwitchMap$io$trickle$util$concurrent$DeferredStream$Priority[DeferredStream.Priority.MEDIUM.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            DeferredStream$1.$SwitchMap$io$trickle$util$concurrent$DeferredStream$Priority[DeferredStream.Priority.LOW.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}
