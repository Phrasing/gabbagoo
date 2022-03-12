/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.netty.util.AsciiString
 */
package io.trickle.util.request;

import io.netty.util.AsciiString;

public class Headers {
    public static CharSequence SAME_ORIGIN;
    public static CharSequence CORS;
    public static CharSequence SEC_CH_UA;
    public static CharSequence GZIP_DEFLATE_BR;
    public static CharSequence DEFAULT;
    public static CharSequence EMPTY;
    public static CharSequence WINDOWS;
    public static CharSequence SEC_FETCH_SITE;
    public static CharSequence _Q0;
    public static CharSequence SEC_FETCH_DEST;
    public static CharSequence SEC_FETCH_MODE;
    public static CharSequence SEC_CH_UA_PLATFORM;
    public static CharSequence SEC_CH_UA_MOBILE;

    static {
        DEFAULT = AsciiString.cached((String)"DEFAULT_VALUE");
        SEC_CH_UA = AsciiString.cached((String)"sec-ch-ua");
        SEC_CH_UA_PLATFORM = AsciiString.cached((String)"sec-ch-ua-platform");
        WINDOWS = AsciiString.cached((String)"\"Windows\"");
        SEC_CH_UA_MOBILE = AsciiString.cached((String)"sec-ch-ua-mobile");
        SEC_FETCH_SITE = AsciiString.cached((String)"sec-fetch-site");
        SEC_FETCH_MODE = AsciiString.cached((String)"sec-fetch-mode");
        SEC_FETCH_DEST = AsciiString.cached((String)"sec-fetch-dest");
        _Q0 = AsciiString.cached((String)"?0");
        SAME_ORIGIN = AsciiString.cached((String)"same-origin");
        CORS = AsciiString.cached((String)"cors");
        EMPTY = AsciiString.cached((String)"empty");
        GZIP_DEFLATE_BR = AsciiString.cached((String)"gzip, deflate, br");
    }
}

