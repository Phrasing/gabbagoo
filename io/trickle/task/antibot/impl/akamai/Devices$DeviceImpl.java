package io.trickle.task.antibot.impl.akamai;

import io.vertx.core.json.JsonObject;

public class Devices$DeviceImpl implements Devices$Device {
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

   public Devices$DeviceImpl(int var1, int var2, int var3, int var4, int var5, int var6, int var7, String var8, String var9, String var10, boolean var11, int var12, int var13, int var14, int var15, int var16, int var17, int var18, int var19, int var20, int var21, int var22, int var23, int var24, int var25, String var26, String var27, String var28, String var29, long var30, String var32, int var33, int var34, boolean var35, boolean var36, int var37, String var38, String var39, String var40, boolean var41, boolean var42, boolean var43, int var44, boolean var45, String var46) {
      this.screenAvailWidth = var1;
      this.screenAvailHeight = var2;
      this.screenWidth = var3;
      this.screenHeight = var4;
      this.outerWidth = var5;
      this.innerHeight = var6;
      this.innerWidth = var7;
      this.useragent = var8;
      this.product = var9;
      this.productSub = var10;
      this.brave = var11;
      this.activeXObject = var12;
      this.documentMode = var13;
      this.chrome = var14;
      this.online = var15;
      this.opera = var16;
      this.installTrigger = var17;
      this.HTMLElement = var18;
      this.rtcPeerConnection = var19;
      this.mozInnerScreen = var20;
      this.vibrate = var21;
      this.battery = var22;
      this.forEach = var23;
      this.fileReader = var24;
      this.pluginLength = var25;
      this.language = var26;
      this.deviceOrientation = var27;
      this.deviceMotion = var28;
      this.touchEvent = var29;
      this.navigatorFasSettings = var30;
      this.sed = var32;
      this.colorDepth = var33;
      this.pixelDepth = var34;
      this.cookieEnabled = var35;
      this.javaEnabled = var36;
      this.doNotTrack = var37;
      this.canvasFP1 = var38;
      this.canvasFP2 = var39;
      this.pluginInfo = var40;
      this.sessionStorage = var41;
      this.localStorage = var42;
      this.indexedDB = var43;
      this.timezoneOffset = var44;
      this.webRTC = var45;
      this.windowPerms = new JsonObject(var46);
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
