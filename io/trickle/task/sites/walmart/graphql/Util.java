/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.sites.walmart.graphql;

import io.trickle.util.Utils;
import java.util.regex.Pattern;

public class Util {
    public static Pattern ERROR_MSG_PATTERN = Pattern.compile("\"message\":\"(.*?)\"");
    public static Pattern MOBILE_MSG_PATTERN = Pattern.compile("\"errors\":.*?\"code\":\"(.*?)\"");

    public static String parseErrorMessage(String string) {
        return Utils.quickParseFirst(string, ERROR_MSG_PATTERN, MOBILE_MSG_PATTERN);
    }
}

