/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.json.JsonObject
 */
package io.trickle.task.antibot.impl.akamai.pixel;

import io.trickle.task.antibot.impl.akamai.pixel.Devices$Device;
import io.vertx.core.json.JsonObject;

public class Devices$DeviceImpl
implements Devices$Device {
    public String br;
    public boolean ieps;
    public boolean fc;
    public boolean av;
    public boolean sp;
    public JsonObject dp;
    public boolean fp;
    public String zh;
    public boolean ap;
    public int b;
    public JsonObject timing;
    public String cv;
    public JsonObject sr;
    public JsonObject crc;
    public String fh;
    public int c;
    public JsonObject bt;
    public String nap;
    public String ps;
    public String fonts;
    public double jsv;
    public String lt;
    public String bp;
    public JsonObject nav;

    @Override
    public JsonObject getBt() {
        return this.bt;
    }

    @Override
    public boolean isIeps() {
        return this.ieps;
    }

    @Override
    public String getBp() {
        return this.bp;
    }

    @Override
    public boolean isSp() {
        return this.sp;
    }

    @Override
    public String getFh() {
        return this.fh;
    }

    @Override
    public JsonObject getCrc() {
        return this.crc;
    }

    @Override
    public String getBr() {
        return this.br;
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

    @Override
    public String getNap() {
        return this.nap;
    }

    @Override
    public boolean isFp() {
        return this.fp;
    }

    @Override
    public String getLt() {
        return this.lt;
    }

    @Override
    public boolean isAp() {
        return this.ap;
    }

    @Override
    public String getFonts() {
        return this.fonts;
    }

    @Override
    public int getC() {
        return this.c;
    }

    @Override
    public String getZh() {
        return this.zh;
    }

    @Override
    public String getPs() {
        return this.ps;
    }

    @Override
    public boolean isFc() {
        return this.fc;
    }

    @Override
    public boolean isAv() {
        return this.av;
    }

    @Override
    public JsonObject getNav() {
        return this.nav;
    }

    @Override
    public JsonObject getTiming() {
        return this.timing;
    }

    @Override
    public int getB() {
        return this.b;
    }

    @Override
    public JsonObject getSr() {
        return this.sr;
    }

    @Override
    public double getJsv() {
        return this.jsv;
    }

    @Override
    public String getCv() {
        return this.cv;
    }

    @Override
    public JsonObject getDp() {
        return this.dp;
    }
}

