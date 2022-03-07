/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.antibot.impl.px.payload;

import io.trickle.task.antibot.impl.px.Types;
import io.trickle.task.antibot.impl.px.payload.Payload;
import io.trickle.task.sites.Site;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public interface ExtendedPayload
extends Payload {
    public Types getType();

    public String getVID();

    public static String encode(String string, String string2) {
        return new String(Base64.getEncoder().encode("".getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8).replace(" ", "+");
    }

    public String getUUID();

    public String getUserAgent();

    public String getSID();

    public Site getSite();

    public static String decode(String string, int n) {
        string = string.replace(" ", "+");
        String string2 = new String(Base64.getDecoder().decode(string.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        StringBuilder stringBuilder = new StringBuilder();
        int n2 = 0;
        while (n2 < string2.length()) {
            stringBuilder.append((char)(n ^ Character.codePointAt(string2, n2)));
            ++n2;
        }
        return stringBuilder.toString();
    }

    public String desktopString();

    public String toString();

    public static String encode(String string, int n) {
        StringBuilder stringBuilder = new StringBuilder();
        int n2 = 0;
        while (n2 < string.length()) {
            stringBuilder.append((char)(n ^ Character.codePointAt(string, n2)));
            ++n2;
        }
        return new String(Base64.getEncoder().encode(stringBuilder.toString().getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8).replace(" ", "+");
    }
}

