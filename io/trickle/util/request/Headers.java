package io.trickle.util.request;

import io.netty.util.AsciiString;

public class Headers {
   public static CharSequence GZIP_DEFLATE_BR = AsciiString.cached("gzip, deflate, br");
   public static CharSequence SEC_CH_UA_PLATFORM = AsciiString.cached("sec-ch-ua-platform");
   public static CharSequence SEC_FETCH_DEST = AsciiString.cached("sec-fetch-dest");
   public static CharSequence _Q0 = AsciiString.cached("?0");
   public static CharSequence DEFAULT = AsciiString.cached("DEFAULT_VALUE");
   public static CharSequence SEC_FETCH_MODE = AsciiString.cached("sec-fetch-mode");
   public static CharSequence SEC_FETCH_SITE = AsciiString.cached("sec-fetch-site");
   public static CharSequence SAME_ORIGIN = AsciiString.cached("same-origin");
   public static CharSequence WINDOWS = AsciiString.cached("\"Windows\"");
   public static CharSequence SEC_CH_UA_MOBILE = AsciiString.cached("sec-ch-ua-mobile");
   public static CharSequence SEC_CH_UA = AsciiString.cached("sec-ch-ua");
   public static CharSequence CORS = AsciiString.cached("cors");
   public static CharSequence EMPTY = AsciiString.cached("empty");
}
