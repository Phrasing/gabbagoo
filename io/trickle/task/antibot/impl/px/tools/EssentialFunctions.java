/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.json.JsonArray
 */
package io.trickle.task.antibot.impl.px.tools;

import io.trickle.task.antibot.impl.px.payload.captcha.util.TimezoneResource;
import io.vertx.core.json.JsonArray;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.TimeZone;

public class EssentialFunctions {
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss");

    public static String encodeURIComponent(String string) {
        return URLEncoder.encode(string, StandardCharsets.UTF_8).replaceAll("\\+", "%20").replaceAll("\\%21", "!").replaceAll("\\%27", "'").replaceAll("\\%28", "(").replaceAll("\\%29", ")").replaceAll("\\%7E", "~");
    }

    public static String dateNow(String string) {
        Instant instant = Instant.now();
        ZoneId zoneId = ZoneId.systemDefault();
        String string2 = zoneId.getRules().getOffset(instant).toString().replace(":", "");
        TimeZone timeZone = TimeZone.getDefault();
        return simpleDateFormat.format(instant.toEpochMilli()) + " GMT" + string2 + " (" + timeZone.getDisplayName(false, 1, TimezoneResource.getLocalFromLanguage(string)) + ")";
    }

    public static double roundTwoDecimals(double d) {
        return (double)Math.round(d * Double.longBitsToDouble(4636737291354636288L)) / Double.longBitsToDouble(4636737291354636288L);
    }

    public static JsonArray convertArrToJson(String[] stringArray) {
        String string = Arrays.toString(stringArray).replace("[", "[\"").replace(", ", "\", \"").replace("]", "\"]");
        if (stringArray.length != 0) return new JsonArray(string);
        return new JsonArray();
    }
}

