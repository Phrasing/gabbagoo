/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.json.JsonArray
 */
package io.trickle.task.antibot.impl.px.payload.captcha.util;

import io.vertx.core.json.JsonArray;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeviceHeaderParsers {
    public String[] SEPERATORS = new String[]{"\\", "\"", ";", " "};
    public static Pattern CHROME_VERSION_PATTERN = Pattern.compile("\\sChrome/([0-9][0-9])");

    public static String getSecUA(String string) {
        Matcher matcher = CHROME_VERSION_PATTERN.matcher(string);
        if (!matcher.find()) {
            return null;
        }
        String string2 = matcher.group(1);
        return "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"" + string2 + "\", \"Google Chrome\";v=\"" + string2 + "\"";
    }

    public static String getAcceptLanguage(JsonArray jsonArray) {
        return jsonArray.toString().replace("[", "").replace("]", "").replace("\"", "") + ";q=0.9";
    }
}

