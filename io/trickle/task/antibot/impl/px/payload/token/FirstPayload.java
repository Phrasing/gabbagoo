/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.MultiMap
 *  io.vertx.core.json.JsonArray
 *  io.vertx.core.json.JsonObject
 */
package io.trickle.task.antibot.impl.px.payload.token;

import io.trickle.task.antibot.impl.px.payload.Payload;
import io.trickle.task.antibot.impl.px.payload.token.Battery;
import io.trickle.task.antibot.impl.px.payload.token.Devices$Device;
import io.trickle.task.antibot.impl.px.payload.token.FirstPayload$1;
import io.trickle.task.antibot.impl.px.payload.token.InitPayload;
import io.trickle.task.antibot.impl.px.payload.token.SecondPayload;
import io.trickle.task.sites.Site;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

public class FirstPayload
implements Payload {
    public int PX92;
    public boolean PX332;
    public String PX320;
    public String PX421;
    public String PX318;
    public Battery PX415;
    public String PX327;
    public boolean PX337;
    public int PX351;
    public String PX344;
    public String PX317;
    public long f20430a;
    public long f20432b;
    public boolean PX316;
    public boolean PX334;
    public long PX349;
    public boolean PX336;
    public String PX442;
    public String PX343;
    public JsonArray PX347;
    public String t;
    public Battery PX420;
    public String PX326;
    public Battery PX418;
    public Battery PX419;
    public String PX322;
    public int PX345;
    public String PX328;
    public String PX319;
    public Battery PX413;
    public String PX330;
    public String SID_HEADER;
    public int PX350;
    public String f20431a = null;
    public boolean PX333;
    public String VID_HEADER;
    public Devices$Device device;
    public long PX323;
    public String UUIDV4_HEADER = UUID.randomUUID().toString();
    public String PX339;
    public Battery PX416;
    public boolean PX331;
    public Site SITE;
    public boolean PX335;
    public int PX91;
    public static char[] f18573a = "0123456789ABCDEF".toCharArray();
    public Battery PX414;

    public FirstPayload(Devices$Device devices$Device, String string, String string2, int n, long l, Site site) {
        this.f20430a = Long.MIN_VALUE;
        this.device = devices$Device;
        this.VID_HEADER = string;
        this.SID_HEADER = string2;
        this.PX349 = l;
        this.SITE = site;
        this.t = "PX315";
        this.PX91 = this.device.getWidth();
        this.PX92 = this.device.getHeight();
        this.PX316 = true;
        this.PX345 = n == 1 ? 1 : ++n;
        this.PX351 = n == 1 ? 0 : ThreadLocalRandom.current().nextInt(0, 999);
        this.PX317 = this.device.getConnectionType();
        this.PX318 = String.valueOf(this.device.getApiLevel());
        this.PX319 = this.device.getOperatingSystem();
        this.PX320 = this.device.getDeviceName();
        this.PX323 = Instant.now().getEpochSecond();
        this.initUUIDSettings();
        switch (FirstPayload$1.$SwitchMap$io$trickle$task$sites$Site[this.SITE.ordinal()]) {
            case 1: {
                this.PX326 = UUID.randomUUID().toString();
                this.PX327 = UUID.randomUUID().toString().split("-")[0].toUpperCase();
                this.PX328 = FirstPayload.m4817a(this.PX320 + UUID.randomUUID() + this.PX327);
                break;
            }
            case 2: {
                this.PX326 = this.genTimeBasedUUID().toString();
                this.PX327 = this.genTimeBasedUUID().toString().split("-")[0].toUpperCase();
                this.PX328 = FirstPayload.m4817a(this.PX320 + this.PX326 + this.PX327);
                break;
            }
            default: {
                this.PX326 = null;
                this.PX327 = null;
                this.PX328 = null;
            }
        }
        this.PX337 = this.device.isGps();
        this.PX336 = this.device.isGyroscope();
        this.PX335 = this.device.isAccelerometer();
        this.PX334 = this.device.isEthernet();
        this.PX333 = this.device.isTouchscreen();
        this.PX331 = this.device.isNfc();
        this.PX332 = this.device.isWifi();
        this.PX330 = "new_session";
        this.PX421 = "false";
        this.PX442 = "false";
        this.PX339 = this.device.getBrand();
        this.PX322 = "Android";
        this.PX343 = this.device.getCellular();
        this.PX344 = this.device.getCarrier();
        this.PX347 = new JsonArray().add((Object)"en_US");
        this.PX413 = this.device.getBattery();
        this.PX414 = this.device.getBattery();
        this.PX415 = this.device.getBattery();
        this.PX416 = this.device.getBattery();
        this.PX419 = this.device.getBattery();
        this.PX418 = this.device.getBattery();
        this.PX420 = this.device.getBattery();
        this.PX350 = ThreadLocalRandom.current().nextInt(1, 5);
    }

    public long m30009c(long l) {
        long l2;
        long l3 = this.f20430a;
        if (l > l3) {
            this.f20430a = l;
            return l;
        }
        this.f20430a = l2 = l3 + 1L;
        return l2;
    }

    public long m30011a() {
        return this.f20432b;
    }

    public void initUUIDSettings() {
        long l;
        new AtomicLong(Long.MIN_VALUE);
        this.f20432b = Long.MIN_VALUE;
        String string = null;
        if (string != null) {
            l = Long.parseLong(string, 16) | this.f20432b;
        } else {
            long l2;
            long l3;
            long l4;
            byte[] byArray = this.m30012b();
            this.f20432b = l4 = this.f20432b | (long)(byArray[0] << 24) & 0xFF000000L;
            this.f20432b = l3 = l4 | (long)(byArray[1] << 16 & 0xFF0000);
            this.f20432b = l2 = l3 | (long)(byArray[2] << 8 & 0xFF00);
            l = l2 | (long)(byArray[3] & 0xFF);
        }
        this.f20432b = l;
        this.f20432b |= (long)(ThreadLocalRandom.current().nextDouble() * Double.longBitsToDouble(4670232263827390464L)) << 48;
    }

    public byte[] m30012b() {
        byte[] byArray = new byte[4];
        this.nextBytes(byArray);
        return byArray;
    }

    @Override
    public MultiMap asForm() {
        MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
        if (this.VID_HEADER != null) {
            multiMap.set("vid", this.VID_HEADER);
        }
        multiMap.set("ftag", "22");
        multiMap.set("payload", this.getPayload());
        switch (FirstPayload$1.$SwitchMap$io$trickle$task$sites$Site[this.SITE.ordinal()]) {
            case 2: {
                multiMap.set("appId", "PX9Qx3Rve4");
                this.UUIDV4_HEADER = this.genTimeBasedUUID().toString();
                break;
            }
            case 1: {
                multiMap.set("appId", "PXUArm9B04");
                break;
            }
        }
        multiMap.set("tag", "mobile");
        multiMap.set("uuid", this.UUIDV4_HEADER);
        if (this.SID_HEADER == null) return multiMap;
        multiMap.set("sid", this.SID_HEADER);
        return multiMap;
    }

    public String getPayload() {
        return Base64.getEncoder().encodeToString(this.toString().getBytes(StandardCharsets.UTF_8));
    }

    public static long m30010d(long l) {
        return (l & 0xFFFF000000000000L) >>> 48 | l << 32 | 0x1000L | (0xFFFF00000000L & l) >>> 16;
    }

    public String toString() {
        JsonObject jsonObject = new JsonObject();
        if (this.SID_HEADER != null && this.PX349 != -1L) {
            jsonObject.put("PX349", (Object)this.PX349);
        } else if (this.PX345 > 1) {
            jsonObject.put("PX350", (Object)this.PX350);
        }
        jsonObject.put("PX91", (Object)this.PX91);
        jsonObject.put("PX92", (Object)this.PX92);
        jsonObject.put("PX316", (Object)this.PX316);
        jsonObject.put("PX345", (Object)this.PX345);
        jsonObject.put("PX351", (Object)this.PX351);
        jsonObject.put("PX317", (Object)this.PX317);
        jsonObject.put("PX318", (Object)this.PX318);
        jsonObject.put("PX319", (Object)this.PX319);
        jsonObject.put("PX320", (Object)this.PX320);
        jsonObject.put("PX323", (Object)this.PX323);
        jsonObject.put("PX326", (Object)this.PX326);
        jsonObject.put("PX327", (Object)this.PX327);
        jsonObject.put("PX328", (Object)this.PX328);
        jsonObject.put("PX337", (Object)this.PX337);
        jsonObject.put("PX336", (Object)this.PX336);
        jsonObject.put("PX335", (Object)this.PX335);
        jsonObject.put("PX334", (Object)this.PX334);
        jsonObject.put("PX333", (Object)this.PX333);
        jsonObject.put("PX331", (Object)this.PX331);
        jsonObject.put("PX332", (Object)this.PX332);
        jsonObject.put("PX330", (Object)this.PX330);
        jsonObject.put("PX421", (Object)this.PX421);
        jsonObject.put("PX442", (Object)this.PX442);
        jsonObject.put("PX339", (Object)this.PX339);
        jsonObject.put("PX322", (Object)this.PX322);
        switch (FirstPayload$1.$SwitchMap$io$trickle$task$sites$Site[this.SITE.ordinal()]) {
            case 2: {
                jsonObject.put("PX340", (Object)"v1.13.2");
                jsonObject.put("PX341", (Object)"Hibbett | City Gear");
                jsonObject.put("PX342", (Object)"4.15.0");
                jsonObject.put("PX348", (Object)"com.hibbett.android");
                break;
            }
            case 1: {
                jsonObject.put("PX340", (Object)"v1.8.0");
                jsonObject.put("PX341", (Object)"Walmart");
                jsonObject.put("PX342", (Object)"21.12");
                jsonObject.put("PX348", (Object)"com.walmart.android");
                break;
            }
        }
        jsonObject.put("PX343", (Object)this.PX343);
        jsonObject.put("PX344", (Object)this.PX344);
        jsonObject.put("PX347", (Object)this.PX347);
        jsonObject.put("PX413", (Object)this.PX413.getBatteryHealth());
        jsonObject.put("PX414", (Object)this.PX414.getChargingStatus());
        jsonObject.put("PX415", (Object)this.PX415.getBatteryPercent());
        jsonObject.put("PX416", (Object)this.PX416.getChargingMethod());
        jsonObject.put("PX419", (Object)this.PX419.getBatteryType());
        jsonObject.put("PX418", (Object)Battery.roundTemperature(this.PX418.getTemperature()));
        jsonObject.put("PX420", (Object)Battery.roundVoltage(this.PX420.getVoltage()));
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.put("t", (Object)this.t);
        jsonObject2.put("d", (Object)jsonObject);
        return new JsonArray().add((Object)jsonObject2).encode();
    }

    public FirstPayload(Devices$Device devices$Device, Site site) {
        this(devices$Device, null, null, 1, 1L, site);
    }

    public static String m4818a(byte[] byArray) {
        char[] cArray = new char[byArray.length * 2];
        int n = 0;
        while (n < byArray.length) {
            int n2 = byArray[n] & 0xFF;
            int n3 = n * 2;
            char[] cArray2 = f18573a;
            cArray[n3] = cArray2[n2 >>> 4];
            cArray[n3 + 1] = cArray2[n2 & 0xF];
            ++n;
        }
        return new String(cArray);
    }

    public FirstPayload(SecondPayload secondPayload, int n, long l, Site site) {
        this(secondPayload.device, secondPayload.VID_HEADER, secondPayload.SID_HEADER, n, l, site);
    }

    public static String m4817a(String string) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] byArray = string.getBytes(StandardCharsets.UTF_8);
            messageDigest.update(byArray, 0, byArray.length);
            return FirstPayload.m4818a(messageDigest.digest());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            noSuchAlgorithmException.printStackTrace();
            return null;
        }
    }

    public void nextBytes(byte[] byArray) {
        int n = 0;
        int n2 = byArray.length;
        block0: while (n < n2) {
            int n3 = ThreadLocalRandom.current().nextInt();
            int n4 = Math.min(n2 - n, 4);
            while (true) {
                if (n4-- <= 0) continue block0;
                byArray[n++] = (byte)n3;
                n3 >>= 8;
            }
            break;
        }
        return;
    }

    public FirstPayload(InitPayload initPayload, Site site) {
        this(initPayload.device, site);
    }

    public UUID genTimeBasedUUID() {
        long l = System.currentTimeMillis() * 10000L + 122192928000000000L;
        return new UUID(FirstPayload.m30010d(this.m30009c(l)), this.m30011a());
    }
}

