/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.json.JsonObject
 */
package io.trickle.task.antibot.impl.akamai;

import io.trickle.task.antibot.impl.akamai.Devices$Device;
import io.vertx.core.json.JsonObject;

public class Devices$DeviceImpl
implements Devices$Device {
    public int installTrigger;
    public String language;
    public int chrome;
    public String deviceMotion;
    public int pixelDepth;
    public int vibrate;
    public String product;
    public int mozInnerScreen;
    public int pluginLength;
    public int battery;
    public int colorDepth;
    public int fileReader;
    public int opera;
    public String useragent;
    public boolean javaEnabled;
    public String deviceOrientation;
    public int screenAvailWidth;
    public boolean cookieEnabled;
    public boolean webRTC;
    public int HTMLElement;
    public int screenWidth;
    public String sed;
    public int rtcPeerConnection;
    public int innerWidth;
    public String productSub;
    public int documentMode;
    public int outerWidth;
    public JsonObject windowPerms;
    public boolean sessionStorage;
    public int screenAvailHeight;
    public String touchEvent;
    public String canvasFP1;
    public boolean indexedDB;
    public int timezoneOffset;
    public int activeXObject;
    public int screenHeight;
    public long navigatorFasSettings;
    public int forEach;
    public int online;
    public String pluginInfo;
    public boolean brave;
    public boolean localStorage;
    public String canvasFP2;
    public int doNotTrack;
    public int innerHeight;

    @Override
    public int getPluginLength() {
        return this.pluginLength;
    }

    @Override
    public int isOpera() {
        return this.opera;
    }

    @Override
    public boolean webrtcKey() {
        return this.webRTC;
    }

    @Override
    public int getScreenAvailHeight() {
        return this.screenAvailHeight;
    }

    @Override
    public int hasRTCPeerConnection() {
        return this.rtcPeerConnection;
    }

    @Override
    public String getCanvas1() {
        return this.canvasFP1;
    }

    @Override
    public int getScreenHeight() {
        return this.screenHeight;
    }

    @Override
    public String getDeviceMotion() {
        return this.deviceMotion;
    }

    @Override
    public boolean indexedDBKey() {
        return this.indexedDB;
    }

    @Override
    public String getDeviceOrientation() {
        return this.deviceOrientation;
    }

    @Override
    public String getPluginInfo() {
        return this.pluginInfo;
    }

    @Override
    public boolean isJavaEnabled() {
        return this.javaEnabled;
    }

    @Override
    public int getDocumentMode() {
        return this.documentMode;
    }

    @Override
    public String getUserAgent() {
        return this.useragent;
    }

    @Override
    public String getProduct() {
        return this.product;
    }

    @Override
    public int hasActiveXObject() {
        return this.activeXObject;
    }

    @Override
    public int getColorDepth() {
        return this.colorDepth;
    }

    @Override
    public String getCanvas2() {
        return this.canvasFP2;
    }

    @Override
    public String getTouchEvent() {
        return this.touchEvent;
    }

    @Override
    public int getInnerHeight() {
        return this.innerHeight;
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

    @Override
    public JsonObject getWindowPerms() {
        return this.windowPerms;
    }

    @Override
    public int hasVibrate() {
        return this.vibrate;
    }

    @Override
    public boolean localStorageKey() {
        return this.localStorage;
    }

    @Override
    public String getProductSub() {
        return this.productSub;
    }

    @Override
    public String getLanguage() {
        return this.language;
    }

    @Override
    public int hasHTMLElement() {
        return this.HTMLElement;
    }

    @Override
    public int hasInstallTrigger() {
        return this.installTrigger;
    }

    @Override
    public int getOuterWidth() {
        return this.outerWidth;
    }

    @Override
    public int hasForEach() {
        return this.forEach;
    }

    @Override
    public long getNavigatorFasSettings() {
        return this.navigatorFasSettings;
    }

    @Override
    public String getSed() {
        return this.sed;
    }

    @Override
    public boolean sessionStorageKey() {
        return this.sessionStorage;
    }

    @Override
    public int isOnline() {
        return this.online;
    }

    @Override
    public boolean isBrave() {
        return this.brave;
    }

    @Override
    public int hasFileReader() {
        return this.fileReader;
    }

    @Override
    public int getDoNotTrack() {
        return this.doNotTrack;
    }

    @Override
    public int getTimezoneOffset() {
        return this.timezoneOffset;
    }

    @Override
    public int getScreenAvailWidth() {
        return this.screenAvailWidth;
    }

    @Override
    public int hasBattery() {
        return this.battery;
    }

    @Override
    public int hasMozInnerScreen() {
        return this.mozInnerScreen;
    }

    @Override
    public int isChrome() {
        return this.chrome;
    }

    @Override
    public int getPixelDepth() {
        return this.pixelDepth;
    }

    @Override
    public int getScreenWidth() {
        return this.screenWidth;
    }

    @Override
    public int getInnerWidth() {
        return this.innerWidth;
    }

    @Override
    public boolean isCookieEnabled() {
        return this.cookieEnabled;
    }
}

