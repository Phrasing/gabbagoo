/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.util.AsciiString
 *  io.trickle.util.request.Headers
 *  io.vertx.core.MultiMap
 */
package io.trickle.util.request;

import io.netty.util.AsciiString;
import io.trickle.util.request.Headers;
import io.vertx.core.MultiMap;

public enum Headers$Pseudo {
    MPAS(new String[]{"m", "p", "a", "s"}),
    MSPA(new String[]{"m", "s", "p", "a"}),
    MASP(new String[]{"m", "a", "s", "p"}),
    MPSA(new String[]{"m", "p", "s", "a"});

    public static CharSequence SCHEME;
    public static CharSequence PATH;
    public String[] headerOrder;
    public static CharSequence METHOD;
    public static CharSequence AUTHORITY;

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    public Headers$Pseudo() {
        void var3_1;
        if (((void)var3_1).length != 4) {
            throw new RuntimeException("Invalid Header Length");
        }
        this.headerOrder = var3_1;
    }

    public static MultiMap build(String ... stringArray) {
        MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
        String[] stringArray2 = stringArray;
        int n = stringArray2.length;
        int n2 = 0;
        while (n2 < n) {
            String string;
            switch (string = stringArray2[n2]) {
                case "m": {
                    multiMap.add(METHOD, Headers.DEFAULT);
                    break;
                }
                case "p": {
                    multiMap.add(PATH, Headers.DEFAULT);
                    break;
                }
                case "a": {
                    multiMap.add(AUTHORITY, Headers.DEFAULT);
                    break;
                }
                case "s": {
                    multiMap.add(SCHEME, Headers.DEFAULT);
                    break;
                }
                default: {
                    throw new RuntimeException("Illegal header detected: " + string);
                }
            }
            ++n2;
        }
        return multiMap;
    }

    static {
        METHOD = AsciiString.cached((String)":method");
        PATH = AsciiString.cached((String)":path");
        AUTHORITY = AsciiString.cached((String)":authority");
        SCHEME = AsciiString.cached((String)":scheme");
    }

    public static MultiMap fromOrder(String ... stringArray) {
        return Headers$Pseudo.build(stringArray);
    }

    public MultiMap get() {
        return Headers$Pseudo.build(this.headerOrder);
    }
}
