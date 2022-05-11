/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.task.antibot.impl.akamai.pixel.Devices$1
 *  io.trickle.task.antibot.impl.akamai.pixel.Devices$Device
 *  io.trickle.task.antibot.impl.akamai.pixel.Devices$DeviceImpl
 *  io.vertx.core.json.JsonObject
 */
package io.trickle.task.antibot.impl.akamai.pixel;

import io.trickle.task.antibot.impl.akamai.pixel.Devices;
import io.vertx.core.json.JsonObject;

public class Devices
extends Enum {
    public static Devices[] $VALUES;
    public static /* enum */ Devices BRAVE_89_1;

    static {
        BRAVE_89_1 = new 1("BRAVE_89_1", 0);
        $VALUES = new Devices[]{BRAVE_89_1};
    }

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    public Devices() {
        void var2_-1;
        void var1_-1;
    }

    public static Devices[] values() {
        return (Devices[])$VALUES.clone();
    }

    public static Device genFromJson(JsonObject jsonObject) {
        return new DeviceImpl(jsonObject.getBoolean("ap").booleanValue(), jsonObject.getJsonObject("bt", null), jsonObject.getString("fonts"), jsonObject.getString("fh"), null, jsonObject.getString("bp"), jsonObject.getJsonObject("sr"), jsonObject.getJsonObject("dp"), jsonObject.getString("lt"), jsonObject.getString("ps"), jsonObject.getString("cv"), jsonObject.getBoolean("fp").booleanValue(), jsonObject.getBoolean("sp").booleanValue(), jsonObject.getString("br"), jsonObject.getBoolean("ieps").booleanValue(), jsonObject.getBoolean("av").booleanValue(), jsonObject.getInteger("b").intValue(), jsonObject.getInteger("c").intValue(), "", Double.parseDouble(jsonObject.getString("jsv")), jsonObject.getJsonObject("nav"), jsonObject.getJsonObject("crc"), jsonObject.getString("nap"), jsonObject.getBoolean("fc").booleanValue());
    }

    public static Devices valueOf(String string) {
        return Enum.valueOf(Devices.class, string);
    }

    public Device get() {
        return null;
    }
}
