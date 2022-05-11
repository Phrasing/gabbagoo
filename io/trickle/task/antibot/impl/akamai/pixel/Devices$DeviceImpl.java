/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.task.antibot.impl.akamai.pixel.Devices$Device
 *  io.vertx.core.json.JsonObject
 */
package io.trickle.task.antibot.impl.akamai.pixel;

import io.trickle.task.antibot.impl.akamai.pixel.Devices;
import io.vertx.core.json.JsonObject;

public class Devices$DeviceImpl
implements Devices.Device {
    public String fh;
    public String nap;
    public String cv;
    public boolean fc;
    public boolean ieps;
    public JsonObject bt;
    public String bp;
    public int b;
    public JsonObject sr;
    public String zh;
    public boolean fp;
    public JsonObject timing;
    public String fonts;
    public boolean av;
    public JsonObject nav;
    public String ps;
    public String lt;
    public double jsv;
    public JsonObject crc;
    public boolean sp;
    public String br;
    public JsonObject dp;
    public boolean ap;
    public int c;

    public String getLt() {
        return this.lt;
    }

    public int getB() {
        return this.b;
    }

    public boolean isAv() {
        return this.av;
    }

    public JsonObject getNav() {
        return this.nav;
    }

    public boolean isSp() {
        return this.sp;
    }

    public String getFonts() {
        return this.fonts;
    }

    public String getFh() {
        return this.fh;
    }

    public String getBr() {
        return this.br;
    }

    public String getZh() {
        return this.zh;
    }

    public JsonObject getSr() {
        return this.sr;
    }

    public String getBp() {
        return this.bp;
    }

    public int getC() {
        return this.c;
    }

    public JsonObject getBt() {
        return this.bt;
    }

    public JsonObject getCrc() {
        return this.crc;
    }

    public boolean isAp() {
        return this.ap;
    }

    public boolean isFp() {
        return this.fp;
    }

    public double getJsv() {
        return this.jsv;
    }

    public JsonObject getTiming() {
        return this.timing;
    }

    public JsonObject getDp() {
        return this.dp;
    }

    public String getCv() {
        return this.cv;
    }

    public Devices$DeviceImpl(boolean bl, JsonObject jsonObject, String string, String string2, JsonObject jsonObject2, String string3, JsonObject jsonObject3, JsonObject jsonObject4, String string4, String string5, String string6, boolean bl2, boolean bl3, String string7, boolean bl4, boolean bl5, int n, int n2, String string8, double d, JsonObject jsonObject5, JsonObject jsonObject6, String string9, boolean bl6) {
        this.ap = bl;
        this.bt = jsonObject;
        this.fonts = string;
        this.fh = string2;
        this.timing = jsonObject2;
        this.bp = string3;
        this.sr = jsonObject3;
        this.dp = jsonObject4;
        this.lt = string4;
        this.ps = string5;
        this.cv = string6;
        this.fp = bl2;
        this.sp = bl3;
        this.br = string7;
        this.ieps = bl4;
        this.av = bl5;
        this.b = n;
        this.c = n2;
        this.zh = string8;
        this.jsv = d;
        this.nav = jsonObject5;
        this.crc = jsonObject6;
        this.nap = string9;
        this.fc = bl6;
    }

    public String getPs() {
        return this.ps;
    }

    public String getNap() {
        return this.nap;
    }

    public boolean isFc() {
        return this.fc;
    }

    public boolean isIeps() {
        return this.ieps;
    }
}
