/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.json.JsonObject
 */
package io.trickle.task.antibot.impl.akamai.pixel;

import io.trickle.task.antibot.impl.akamai.pixel.Devices$1;
import io.trickle.task.antibot.impl.akamai.pixel.Devices$Device;
import io.trickle.task.antibot.impl.akamai.pixel.Devices$DeviceImpl;
import io.vertx.core.json.JsonObject;

public class Devices
extends Enum {
    public static /* enum */ Devices BRAVE_89_1 = new Devices$1();
    public static Devices[] $VALUES = new Devices[]{BRAVE_89_1};

    public static Devices$Device genFromJson(JsonObject jsonObject) {
        return new Devices$DeviceImpl(jsonObject.getBoolean("ap"), jsonObject.getJsonObject("bt", null), jsonObject.getString("fonts"), jsonObject.getString("fh"), null, jsonObject.getString("bp"), jsonObject.getJsonObject("sr"), jsonObject.getJsonObject("dp"), jsonObject.getString("lt"), jsonObject.getString("ps"), jsonObject.getString("cv"), jsonObject.getBoolean("fp"), jsonObject.getBoolean("sp"), jsonObject.getString("br"), jsonObject.getBoolean("ieps"), jsonObject.getBoolean("av"), jsonObject.getInteger("b"), jsonObject.getInteger("c"), "", Double.parseDouble(jsonObject.getString("jsv")), jsonObject.getJsonObject("nav"), jsonObject.getJsonObject("crc"), jsonObject.getString("nap"), jsonObject.getBoolean("fc"));
    }

    public static Devices[] values() {
        return (Devices[])$VALUES.clone();
    }

    public static Devices valueOf(String string) {
        return Enum.valueOf(Devices.class, string);
    }

    public Devices$Device get() {
        return null;
    }

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    public Devices() {
        void var2_-1;
        void var1_-1;
    }
}

