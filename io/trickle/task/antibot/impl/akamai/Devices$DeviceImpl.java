/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.task.antibot.impl.akamai.Devices$Device
 *  io.vertx.core.json.JsonObject
 */
package io.trickle.task.antibot.impl.akamai;

import io.trickle.task.antibot.impl.akamai.Devices;
import io.vertx.core.json.JsonObject;

public class Devices$DeviceImpl
implements Devices.Device {
    public int HTMLElement;
    public int screenHeight;
    public int colorDepth;
    public int screenWidth;
    public int mozInnerScreen;
    public int pixelDepth;
    public int chrome;
    public String deviceOrientation;
    public int activeXObject;
    public JsonObject windowPerms;
    public String productSub;
    public String canvasFP2;
    public boolean webRTC;
    public int documentMode;
    public boolean localStorage;
    public int screenAvailHeight;
    public boolean brave;
    public String product;
    public String sed;
    public int online;
    public int installTrigger;
    public int opera;
    public String touchEvent;
    public int pluginLength;
    public int timezoneOffset;
    public int vibrate;
    public long navigatorFasSettings;
    public int forEach;
    public String useragent;
    public int outerWidth;
    public int fileReader;
    public boolean sessionStorage;
    public int doNotTrack;
    public boolean indexedDB;
    public int screenAvailWidth;
    public int innerWidth;
    public int battery;
    public String pluginInfo;
    public int innerHeight;
    public boolean javaEnabled;
    public String language;
    public String deviceMotion;
    public String canvasFP1;
    public boolean cookieEnabled;
    public int rtcPeerConnection;

    public String getSed() {
        return this.sed;
    }

    public int getColorDepth() {
        return this.colorDepth;
    }

    public int hasBattery() {
        return this.battery;
    }

    public int hasInstallTrigger() {
        return this.installTrigger;
    }

    public boolean isCookieEnabled() {
        return this.cookieEnabled;
    }

    public int getScreenWidth() {
        return this.screenWidth;
    }

    public int hasRTCPeerConnection() {
        return this.rtcPeerConnection;
    }

    public int hasFileReader() {
        return this.fileReader;
    }

    public String getUserAgent() {
        return this.useragent;
    }

    public String getProduct() {
        return this.product;
    }

    public int hasVibrate() {
        return this.vibrate;
    }

    public boolean webrtcKey() {
        return this.webRTC;
    }

    public int isOpera() {
        return this.opera;
    }

    public int hasForEach() {
        return this.forEach;
    }

    public boolean isJavaEnabled() {
        return this.javaEnabled;
    }

    public String getDeviceMotion() {
        return this.deviceMotion;
    }

    public int getScreenAvailHeight() {
        return this.screenAvailHeight;
    }

    public boolean sessionStorageKey() {
        return this.sessionStorage;
    }

    public JsonObject getWindowPerms() {
        return this.windowPerms;
    }

    public int isOnline() {
        return this.online;
    }

    public String getCanvas1() {
        return this.canvasFP1;
    }

    public int getInnerHeight() {
        return this.innerHeight;
    }

    public int getOuterWidth() {
        return this.outerWidth;
    }

    public int getTimezoneOffset() {
        return this.timezoneOffset;
    }

    public long getNavigatorFasSettings() {
        return this.navigatorFasSettings;
    }

    public int getPluginLength() {
        return this.pluginLength;
    }

    public String getCanvas2() {
        return this.canvasFP2;
    }

    public int hasHTMLElement() {
        return this.HTMLElement;
    }

    public boolean isBrave() {
        return this.brave;
    }

    public boolean localStorageKey() {
        return this.localStorage;
    }

    public int getPixelDepth() {
        return this.pixelDepth;
    }

    public String getDeviceOrientation() {
        return this.deviceOrientation;
    }

    public Devices$DeviceImpl(int n, int n2, int n3, int n4, int n5, int n6, int n7, String string, String string2, String string3, boolean bl, int n8, int n9, int n10, int n11, int n12, int n13, int n14, int n15, int n16, int n17, int n18, int n19, int n20, int n21, String string4, String string5, String string6, String string7, long l, String string8, int n22, int n23, boolean bl2, boolean bl3, int n24, String string9, String string10, String string11, boolean bl4, boolean bl5, boolean bl6, int n25, boolean bl7, String string12) {
        this.screenAvailWidth = n;
        this.screenAvailHeight = n2;
        this.screenWidth = n3;
        this.screenHeight = n4;
        this.outerWidth = n5;
        this.innerHeight = n6;
        this.innerWidth = n7;
        this.useragent = string;
        this.product = string2;
        this.productSub = string3;
        this.brave = bl;
        this.activeXObject = n8;
        this.documentMode = n9;
        this.chrome = n10;
        this.online = n11;
        this.opera = n12;
        this.installTrigger = n13;
        this.HTMLElement = n14;
        this.rtcPeerConnection = n15;
        this.mozInnerScreen = n16;
        this.vibrate = n17;
        this.battery = n18;
        this.forEach = n19;
        this.fileReader = n20;
        this.pluginLength = n21;
        this.language = string4;
        this.deviceOrientation = string5;
        this.deviceMotion = string6;
        this.touchEvent = string7;
        this.navigatorFasSettings = l;
        this.sed = string8;
        this.colorDepth = n22;
        this.pixelDepth = n23;
        this.cookieEnabled = bl2;
        this.javaEnabled = bl3;
        this.doNotTrack = n24;
        this.canvasFP1 = string9;
        this.canvasFP2 = string10;
        this.pluginInfo = string11;
        this.sessionStorage = bl4;
        this.localStorage = bl5;
        this.indexedDB = bl6;
        this.timezoneOffset = n25;
        this.webRTC = bl7;
        this.windowPerms = new JsonObject(string12);
    }

    public int getInnerWidth() {
        return this.innerWidth;
    }

    public int getDoNotTrack() {
        return this.doNotTrack;
    }

    public int getScreenAvailWidth() {
        return this.screenAvailWidth;
    }

    public int hasActiveXObject() {
        return this.activeXObject;
    }

    public int getScreenHeight() {
        return this.screenHeight;
    }

    public int hasMozInnerScreen() {
        return this.mozInnerScreen;
    }

    public String getProductSub() {
        return this.productSub;
    }

    public int isChrome() {
        return this.chrome;
    }

    public String getPluginInfo() {
        return this.pluginInfo;
    }

    public boolean indexedDBKey() {
        return this.indexedDB;
    }

    public int getDocumentMode() {
        return this.documentMode;
    }

    public String getLanguage() {
        return this.language;
    }

    public String getTouchEvent() {
        return this.touchEvent;
    }
}
