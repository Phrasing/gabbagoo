/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.task.antibot.impl.px.Types
 *  io.trickle.task.antibot.impl.px.payload.Payload
 *  io.trickle.task.sites.Site
 */
package io.trickle.task.antibot.impl.px.payload;

import io.trickle.task.antibot.impl.px.Types;
import io.trickle.task.antibot.impl.px.payload.Payload;
import io.trickle.task.sites.Site;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public interface ExtendedPayload
extends Payload {
    public String getSID();

    public Site getSite();

    public Types getType();

    public String getUUID();

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

    public String getVID();

    public String desktopString();

    public static String encode(String string, int n) {
        StringBuilder stringBuilder = new StringBuilder();
        int n2 = 0;
        while (n2 < string.length()) {
            stringBuilder.append((char)(n ^ Character.codePointAt(string, n2)));
            ++n2;
        }
        return new String(Base64.getEncoder().encode(stringBuilder.toString().getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8).replace(" ", "+");
    }

    public String toString();

    public String getUserAgent();

    public static String encode(String string, String string2) {
        return new String(Base64.getEncoder().encode("".getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8).replace(" ", "+");
    }
}
