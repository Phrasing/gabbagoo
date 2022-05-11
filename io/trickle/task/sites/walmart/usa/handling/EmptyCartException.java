/*
 * Decompiled with CFR 0.152.
 */
package io.trickle.task.sites.walmart.usa.handling;

public class EmptyCartException
extends Exception {
    public EmptyCartException() {
        super("CRT expired or empty");
    }
}
