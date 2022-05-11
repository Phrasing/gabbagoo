/*
 * Decompiled with CFR 0.152.
 */
package io.trickle.proxy.exeptions;

public class ProxyFormatException
extends Exception {
    public ProxyFormatException() {
        super("Invalid Proxy String array format. Must be of length 2 or 4");
    }
}
