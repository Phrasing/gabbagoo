/*
 * Decompiled with CFR 0.152.
 */
package io.trickle.task.antibot.impl.px.payload.token;

public enum Battery$BatteryHealth {
    GOOD;


    public static Battery$BatteryHealth get() {
        return GOOD;
    }

    public String toString() {
        return this.name().toLowerCase();
    }
}
