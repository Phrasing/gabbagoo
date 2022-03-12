/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.antibot.impl.px.payload.token;

public enum Battery$BatteryHealth {
    GOOD;


    public String toString() {
        return this.name().toLowerCase();
    }

    public static Battery$BatteryHealth get() {
        return GOOD;
    }
}

