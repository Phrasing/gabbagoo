/*
 * Decompiled with CFR 0.152.
 */
package io.trickle.task.sites.amazon;

import java.util.regex.Pattern;

public class Patterns {
    public static Pattern GLOW;
    public static Pattern ADDRESS_ID;
    public static Pattern TITLE;
    public static Pattern CSRF;
    public static Pattern API_CSRF;
    public static Pattern CSM_PREFIX;
    public static Pattern PURCHASE_ID;
    public static Pattern CART_ITEM_ID;

    static {
        TITLE = Pattern.compile("<title>Amazon.com: (.*?) : Everything Else");
        CSRF = Pattern.compile("name=\"CSRF\" value=\"(.*?)\"");
        CSM_PREFIX = Pattern.compile("\"requestId\":\"(.*?)\"");
        GLOW = Pattern.compile("token' value='(.*?)'");
        API_CSRF = Pattern.compile("\"amazonApiCsrfToken\":\"(.*?)\"");
        CART_ITEM_ID = Pattern.compile("submit\\.delete\\.(.*?)\"");
        ADDRESS_ID = Pattern.compile("input data-addnewaddress=\"add-new\" id=\"ubbShipTo\" name=\"dropdown-selection-ubb\" type=\"hidden\" value=\"(.*?)\"");
        PURCHASE_ID = Pattern.compile("amp;pid=(.*?)&");
    }
}
