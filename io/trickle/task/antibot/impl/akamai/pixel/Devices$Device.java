/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.json.JsonObject
 */
package io.trickle.task.antibot.impl.akamai.pixel;

import io.vertx.core.json.JsonObject;

public interface Devices$Device {
    public String getBp();

    public JsonObject getTiming();

    public String getBr();

    public String getLt();

    public JsonObject getDp();

    public boolean isSp();

    public boolean isFp();

    public String getFonts();

    public String getCv();

    public boolean isFc();

    public int getC();

    public JsonObject getSr();

    public int getB();

    public String getPs();

    public String getNap();

    public boolean isAp();

    public double getJsv();

    public boolean isIeps();

    public JsonObject getNav();

    public boolean isAv();

    public JsonObject getBt();

    public JsonObject getCrc();

    public String getZh();

    public String getFh();
}

