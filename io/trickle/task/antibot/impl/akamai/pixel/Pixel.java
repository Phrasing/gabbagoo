/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.antibot.impl.akamai.pixel;

import java.util.Base64;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Pixel {
    public static final Pattern G_INDEX;
    public static final Pattern BAZA_PATTERN;
    public static final Pattern AKAM_PATTERN;
    public static final Pattern T_PATTERN;
    public static final Pattern HEX_ARRAY;

    public static String[] parseHexArray(String string) {
        Matcher matcher = HEX_ARRAY.matcher(string);
        if (!matcher.find()) throw new Exception("No HEX ARR");
        String[] stringArray = matcher.group(1).split(",");
        return Pixel.decodeArray(stringArray);
    }

    static {
        HEX_ARRAY = Pattern.compile("=\\[(\".*\\\\x.*?)\\];");
        G_INDEX = Pattern.compile("g=_\\[([0-9]*?)]");
        T_PATTERN = Pattern.compile("t=([0-z]*)");
        BAZA_PATTERN = Pattern.compile("baz[A-z]*=?\"([0-9]*?)\"");
        AKAM_PATTERN = Pattern.compile("(https://www.yeezysupply.com/akam.*?)\"");
    }

    public static String decodeHexString(String string) {
        string = string.replace("\\x", "").replace("\"", "");
        StringBuilder stringBuilder = new StringBuilder();
        int n = 0;
        while (n < string.length()) {
            String string2 = string.substring(n, n + 2);
            stringBuilder.append((char)Integer.parseInt(string2, 16));
            n += 2;
        }
        return stringBuilder.toString();
    }

    public static String[] parseAkam(String string) {
        Matcher matcher = AKAM_PATTERN.matcher(string);
        String[] stringArray = new String[2];
        int n = 0;
        while (true) {
            if (!matcher.find()) {
                if (stringArray[1] == null) throw new Exception("No AKAM");
                return stringArray;
            }
            stringArray[n] = matcher.group(1);
            ++n;
        }
    }

    public CompletableFuture getPixelReqString(String var1, String var2, String var3);

    public static String parseBaza(String string) {
        Matcher matcher = BAZA_PATTERN.matcher(string);
        if (!matcher.find()) throw new Exception("No BAZA");
        return matcher.group(1);
    }

    public static String parseTValue(String string) {
        Matcher matcher;
        String string2 = string.split("\\?")[1];
        if ((string2 = string2.substring(string2.indexOf("=") + 1)).contains("&")) {
            string2 = string2.split("&")[0];
        }
        if (!(matcher = T_PATTERN.matcher(string2 = new String(Base64.getDecoder().decode(string2)))).find()) throw new Exception("No T val");
        return matcher.group(1);
    }

    public CompletableFuture getPixelReqForm(String var1, String var2, String var3);

    public static int parseGIndex(String string) {
        Matcher matcher = G_INDEX.matcher(string);
        if (!matcher.find()) throw new Exception("No G INDEX");
        return Integer.parseInt(matcher.group(1));
    }

    public static String[] decodeArray(String[] stringArray) {
        int n = 0;
        while (n < stringArray.length) {
            stringArray[n] = Pixel.decodeHexString(stringArray[n]);
            ++n;
        }
        return stringArray;
    }
}

