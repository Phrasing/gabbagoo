/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.sites.walmart.usa;

public interface WalmartConstants {
    public static final String ANDROID_OLD_UA;
    public static final String IOS_OLD_UA;
    public static final String[] DEVICE_CODES;
    public static final String ANDROID_APP_VERSION;
    public static final String[] IOS_VERS;
    public static final String ANDROID_UA;
    public static final String IOS_APP_VERSION;
    public static final String ANDROID_OLD_APP_VERSION;
    public static final String IOS_UA;
    public static final String IOS_OLD_WEB_UA;
    public static final String ANDROID_OLD_WEB_UA;
    public static final String IOS_OLD_APP_VERSION;

    static {
        IOS_APP_VERSION = "21.25.0";
        IOS_OLD_WEB_UA = "Walmart/2107091553 Walmart WMTAPP v21.18.3";
        IOS_OLD_APP_VERSION = "21.18.3";
        IOS_OLD_UA = "Walmart/2107091553 CFNetwork/1128.0.1 Darwin/19.6.0";
        ANDROID_OLD_APP_VERSION = "21.12";
        ANDROID_APP_VERSION = "21.26.2";
        ANDROID_UA = "WMT1H/21.26.2 Android/";
        IOS_VERS = new String[]{"11.0", "15.0", "14.8", "11.4", "12.3", "13.1", "12.1", "13.4", "9.3", "10.3", "14.5", "13.3", "14.4", "14.6", "14.2", "14.7", "14.3", "13.7", "14.0", "13.6", "14.1", "12.4", "12.5", "13.5"};
        DEVICE_CODES = new String[]{"iPhone7,1", "iPhone7,2", "iPhone8,1", "iPhone8,2", "iPhone8,4", "iPhone9,1", "iPhone9,2", "iPhone9,3", "iPhone9,4", "iPhone10,1", "iPhone10,2", "iPhone10,3", "iPhone10,4", "iPhone10,5", "iPhone10,6", "iPhone11,2", "iPhone11,4", "iPhone11,6", "iPhone11,8", "iPhone12,1", "iPhone12,3", "iPhone12,5", "iPhone12,8", "iPhone13,1", "iPhone13,2", "iPhone13,3", "iPhone13,4", "iPhone14,2", "iPhone14,3", "iPhone14,4", "iPhone14,5"};
        IOS_UA = "WMT1H/" + "21.25.0".substring(0, "21.25.0".lastIndexOf(".")) + " iOS/";
        ANDROID_OLD_UA = "Android v".concat("21.12");
        ANDROID_OLD_WEB_UA = "Android v".concat("21.12") + " WMTAPP";
    }
}

