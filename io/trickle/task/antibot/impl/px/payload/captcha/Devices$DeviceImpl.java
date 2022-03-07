/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.json.JsonArray
 *  io.vertx.core.json.JsonObject
 */
package io.trickle.task.antibot.impl.px.payload.captcha;

import io.trickle.task.antibot.impl.px.payload.captcha.Devices$Device;
import io.trickle.task.antibot.impl.px.payload.captcha.util.DeviceHeaderParsers;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class Devices$DeviceImpl
implements Devices$Device {
    public String secUA;
    public int outerHeight;
    public String appVersion;
    public int outerWidth;
    public int screenY;
    public JsonObject mongoDocument;
    public int availWidth;
    public String acceptLanguage;
    public int screenX;
    public int innerHeight;
    public int innerWidth;
    public String platform;
    public int width;
    public String appName;
    public int height;
    public String appCodeName;
    public String secUAMobile;
    public String productSub;
    public String useragent;
    public int scrollbarWidth;
    public int availHeight;
    public String product;
    public String acceptEncoding;

    @Override
    public String getProductSub() {
        return this.productSub;
    }

    public Devices$DeviceImpl(JsonObject jsonObject) {
        try {
            this.mongoDocument = jsonObject;
            this.mongoDocument.put("PX61", (Object)"en-US");
            this.mongoDocument.put("PX313", (Object)new JsonArray("[\"en-US\",\"en\"]"));
            this.mongoDocument.put("accept-language", (Object)"en-US,en;q=0.9");
            this.screenX = jsonObject.getInteger("screenXPos");
            this.screenY = jsonObject.getInteger("screenYPos");
            this.outerHeight = jsonObject.getInteger("outerHeight");
            this.outerWidth = jsonObject.getInteger("outerWidth");
            this.acceptLanguage = jsonObject.getString("accept-language");
            this.acceptEncoding = "gzip, deflate, br";
            this.secUA = jsonObject.getString("sec-ch-ua");
            this.secUAMobile = jsonObject.getString("sec-ch-ua-mobile");
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        this.width = jsonObject.getInteger("PX91");
        this.height = jsonObject.getInteger("PX92");
        this.availWidth = jsonObject.getInteger("PX269");
        this.availHeight = jsonObject.getInteger("PX270");
        this.innerWidth = jsonObject.getInteger("PX186");
        this.innerHeight = jsonObject.getInteger("PX185");
        this.platform = jsonObject.getString("PX63");
        this.useragent = jsonObject.getString("PX59");
        this.product = jsonObject.getString("PX62");
        this.productSub = jsonObject.getString("PX69");
        this.appVersion = jsonObject.getString("PX64");
        this.appName = jsonObject.getString("PX65");
        this.appCodeName = jsonObject.getString("PX66");
        this.scrollbarWidth = jsonObject.getInteger("scrollbarWidth") == null ? 0 : jsonObject.getInteger("scrollbarWidth");
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
    public int getAvailWidth() {
        return this.availWidth;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    public String getSecUAMobile() {
        return this.secUAMobile;
    }

    public Devices$DeviceImpl(int n, int n2, int n3, int n4, int n5, int n6, String string, String string2, String string3, String string4, String string5, String string6, String string7) {
        this.height = n;
        this.width = n2;
        this.innerHeight = n3;
        this.innerWidth = n4;
        this.availHeight = n5;
        this.availWidth = n6;
        this.platform = string;
        this.useragent = string2;
        this.product = string3;
        this.productSub = string4;
        this.appVersion = string5;
        this.appName = string6;
        this.appCodeName = string7;
    }

    @Override
    public int getInnerWidth() {
        return this.innerWidth;
    }

    public String getSecUA() {
        return this.secUA;
    }

    public JsonArray getArr(String string) {
        return this.mongoDocument.getJsonArray(string);
    }

    @Override
    public int getAvailHeight() {
        return this.availHeight;
    }

    @Override
    public String getPlatform() {
        return this.platform;
    }

    public String getAcceptEncoding() {
        return this.acceptEncoding;
    }

    public Number getNumber(String string) {
        return this.mongoDocument.getNumber(string);
    }

    public int getScrollbarWidth() {
        return this.scrollbarWidth;
    }

    @Override
    public String getAppName() {
        return this.appName;
    }

    @Override
    public String getAppCodeName() {
        return this.appCodeName;
    }

    @Override
    public String getAppVersion() {
        return this.appVersion;
    }

    @Override
    public int getInnerHeight() {
        return this.innerHeight;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    public String getString(String string) {
        return this.mongoDocument.getString(string);
    }

    public Boolean getBool(String string) {
        return this.mongoDocument.getBoolean(string);
    }

    public String getAcceptLanguage() {
        String string;
        if (this.acceptLanguage == null) {
            string = DeviceHeaderParsers.getAcceptLanguage(this.getArr("PX313"));
            return string;
        }
        string = this.acceptLanguage;
        return string;
    }
}

