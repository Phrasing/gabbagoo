/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.task.antibot.impl.akamai.pixel.Devices
 *  io.trickle.task.antibot.impl.akamai.pixel.Devices$Device
 *  io.trickle.task.antibot.impl.akamai.pixel.Pixel
 *  io.vertx.core.MultiMap
 *  io.vertx.core.json.JsonObject
 */
package io.trickle.task.antibot.impl.akamai.pixel;

import io.trickle.task.antibot.impl.akamai.pixel.Devices;
import io.trickle.task.antibot.impl.akamai.pixel.Pixel;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public class TrickleAPI
implements Pixel {
    public Devices.Device device;

    public JsonObject getSR() {
        return this.device.getSr();
    }

    public String getBR() {
        return this.device.getBr();
    }

    public String getBT() {
        if (this.device.getBt() != null) return this.device.getBt().toString();
        return "0";
    }

    public JsonObject getCRC() {
        return this.device.getCrc();
    }

    public JsonObject getDP() {
        return this.device.getDp();
    }

    public double getJSV() {
        return this.device.getJsv();
    }

    public String getCV() {
        return this.device.getCv();
    }

    public boolean getSP() {
        return this.device.isSp();
    }

    public boolean getIEPS() {
        return this.device.isIeps();
    }

    public String getFontHash() {
        return this.device.getFh();
    }

    public boolean getAP() {
        return this.device.isAp();
    }

    public String getPS() {
        return this.device.getPs();
    }

    public String getTiming() {
        int n = 37 + ThreadLocalRandom.current().nextInt(-3, 4);
        int n2 = 270 + ThreadLocalRandom.current().nextInt(-5, 5);
        int n3 = 392 + ThreadLocalRandom.current().nextInt(-5, 6);
        int n4 = 510 + ThreadLocalRandom.current().nextInt(-5, 5);
        int n5 = 616 + ThreadLocalRandom.current().nextInt(-5, 6);
        int n6 = ThreadLocalRandom.current().nextInt(9, 12);
        return "{\"1\":" + n + ",\"2\":" + n2 + ",\"3\":" + n3 + ",\"4\":" + n4 + ",\"5\":" + n5 + ",\"profile\":{\"bp\":" + ThreadLocalRandom.current().nextInt(0, 1) + ",\"sr\":" + ThreadLocalRandom.current().nextInt(0, 1) + ",\"dp\":0,\"lt\":0,\"ps\":" + ThreadLocalRandom.current().nextInt(0, 2) + ",\"cv\":" + ThreadLocalRandom.current().nextInt(22, 24) + ",\"fp\":0,\"sp\":" + ThreadLocalRandom.current().nextInt(0, 1) + ",\"br\":0,\"ieps\":0,\"av\":0,\"z1\":" + ThreadLocalRandom.current().nextInt(10, 11) + ",\"jsv\":" + ThreadLocalRandom.current().nextInt(0, 1) + ",\"nav\":" + ThreadLocalRandom.current().nextInt(0, 1) + ",\"nap\":" + ThreadLocalRandom.current().nextInt(1, 2) + ",\"crc\":0,\"z2\":" + ThreadLocalRandom.current().nextInt(1, 2) + ",\"z3\":1,\"z4\":0,\"z5\":" + ThreadLocalRandom.current().nextInt(0, 1) + ",\"fonts\":" + n6 + "},\"main\":" + ThreadLocalRandom.current().nextInt(101, 104) + ",\"compute\":" + n + ",\"send\":" + (n5 + n6) + "}";
    }

    public boolean getFC() {
        return this.device.isFc();
    }

    public String getNap() {
        return this.device.getNap();
    }

    public TrickleAPI(JsonObject jsonObject) {
        this.device = Devices.genFromJson((JsonObject)jsonObject);
    }

    public CompletableFuture getPixelReqString(String string, String string2, String string3) {
        return CompletableFuture.failedFuture(new Exception("Unsupported method"));
    }

    public String getZ(String string) {
        return "{\"a\":" + string + ",\"b\":" + this.device.getB() + ",\"c\":" + this.device.getC() + "}";
    }

    public boolean getAV() {
        return this.device.isAv();
    }

    public CompletableFuture getPixelReqForm(String string, String string2, String string3) {
        MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
        try {
            multiMap.set("ap", String.valueOf(this.getAP()));
            multiMap.set("bt", this.getBT());
            multiMap.set("fonts", this.getFonts());
            multiMap.set("fh", this.getFontHash());
            multiMap.set("timing", this.getTiming());
            multiMap.set("bp", this.getBP());
            multiMap.set("sr", this.getSR().toString());
            multiMap.set("dp", this.getDP().toString());
            multiMap.set("lt", this.getLT());
            multiMap.set("ps", this.getPS());
            multiMap.set("cv", this.getCV());
            multiMap.set("fp", String.valueOf(this.getFP()));
            multiMap.set("sp", String.valueOf(this.getSP()));
            multiMap.set("br", this.getBR());
            multiMap.set("ieps", String.valueOf(this.getIEPS()));
            multiMap.set("av", String.valueOf(this.getAV()));
            multiMap.set("z", this.getZ(string));
            multiMap.set("zh", this.getZH());
            multiMap.set("jsv", "" + this.getJSV());
            multiMap.set("nav", this.getNav().toString());
            multiMap.set("crc", this.getCRC().toString());
            multiMap.set("t", string2);
            multiMap.set("u", string3);
            multiMap.set("nap", this.getNap());
            multiMap.set("fc", String.valueOf(this.getFC()));
        }
        catch (Throwable throwable) {
            System.out.println("Error building FW: " + throwable.getMessage());
        }
        return CompletableFuture.completedFuture(multiMap);
    }

    public boolean getFP() {
        return this.device.isFp();
    }

    public String getFonts() {
        return this.device.getFonts();
    }

    public String getLT() {
        String string = this.device.getLt();
        if (string.contains("-")) {
            string = this.device.getLt().split("-")[1];
            return Instant.now().toEpochMilli() + "-" + string;
        }
        if (!string.contains("\\+")) return "" + Instant.now().toEpochMilli();
        string = this.device.getLt().split("\\+")[1];
        return Instant.now().toEpochMilli() + "+" + string;
    }

    public JsonObject getNav() {
        return this.device.getNav();
    }

    public String getBP() {
        return this.device.getBp();
    }

    public String getZH() {
        return this.device.getZh();
    }
}
