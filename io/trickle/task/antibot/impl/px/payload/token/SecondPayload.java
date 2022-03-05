/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.MultiMap
 *  io.vertx.core.json.JsonArray
 *  io.vertx.core.json.JsonObject
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package io.trickle.task.antibot.impl.px.payload.token;

import io.trickle.task.antibot.impl.px.payload.Payload;
import io.trickle.task.antibot.impl.px.payload.token.Battery;
import io.trickle.task.antibot.impl.px.payload.token.Devices$Device;
import io.trickle.task.antibot.impl.px.payload.token.FirstPayload;
import io.trickle.task.antibot.impl.px.payload.token.SecondPayload$1;
import io.trickle.task.sites.Site;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SecondPayload
implements Payload {
    public static Logger logger = LogManager.getLogger(SecondPayload.class);
    public String VID_HEADER;
    public JsonArray PX347;
    public Battery PX413;
    public String[] responseValues;
    public Devices.Device device;
    public String PX339;
    public Battery PX419;
    public String PX257;
    public String PX344;
    public long PX349;
    public String PX256;
    public Site SITE;
    public Battery PX420;
    public Battery PX414;
    public long PX259;
    public String PX320;
    public String t;
    public String SID_HEADER;
    public String UUIDV4_HEADER;
    public String PX343;
    public int sdkInitCount;
    public Battery PX418;
    public Battery PX416;
    public String PX322;
    public int PX204;
    public Battery PX415;

    public long parseAppc2Timestamp() {
        return Long.parseLong(this.responseValues[1]);
    }

    public int m4767d() {
        byte[] byArray = new byte[4];
        byArray = this.PX320.getBytes(StandardCharsets.UTF_8);
        return this.m4766a(byArray);
    }

    public MultiMap asKeepAliveForm() {
        MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
        multiMap.set("vid", this.VID_HEADER);
        multiMap.set("ftag", "22");
        multiMap.set("payload", this.getKeepAlivePayload());
        switch (SecondPayload$1.$SwitchMap$io$trickle$task$sites$Site[this.SITE.ordinal()]) {
            case 1: {
                multiMap.set("appId", "PX9Qx3Rve4");
                break;
            }
            case 2: {
                multiMap.set("appId", "PXUArm9B04");
                break;
            }
        }
        multiMap.set("tag", "mobile");
        multiMap.set("uuid", this.UUIDV4_HEADER);
        multiMap.set("sid", this.SID_HEADER);
        return multiMap;
    }

    public int m4765a(int n, int n2, int n3, int n4, int n5, int n6) {
        int n7 = SecondPayload.m4764a(n, n2, n4, n6);
        int n8 = SecondPayload.m4764a(n7, n3, n5, n6);
        return n8 ^ this.m4767d();
    }

    public void updatePX349(long l) {
        this.PX349 = l;
    }

    @Override
    public MultiMap asForm() {
        MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
        multiMap.set("vid", this.VID_HEADER);
        multiMap.set("ftag", "22");
        multiMap.set("payload", this.getPayload());
        switch (SecondPayload$1.$SwitchMap$io$trickle$task$sites$Site[this.SITE.ordinal()]) {
            case 1: {
                multiMap.set("appId", "PX9Qx3Rve4");
                break;
            }
            case 2: {
                multiMap.set("appId", "PXUArm9B04");
                break;
            }
        }
        multiMap.set("tag", "mobile");
        multiMap.set("uuid", this.UUIDV4_HEADER);
        multiMap.set("sid", this.SID_HEADER);
        return multiMap;
    }

    public int m4766a(byte[] byArray) {
        if (byArray.length >= 4) return ByteBuffer.wrap(byArray).getInt();
        return 0;
    }

    public String toString() {
        JsonObject jsonObject = new JsonObject();
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.put("PX349", (Object)this.PX349);
        jsonObject2.put("PX320", (Object)this.PX320);
        jsonObject2.put("PX259", (Object)this.PX259);
        jsonObject2.put("PX256", (Object)this.PX256);
        jsonObject2.put("PX257", (Object)this.PX257);
        jsonObject2.put("PX339", (Object)this.PX339);
        jsonObject2.put("PX322", (Object)this.PX322);
        switch (SecondPayload$1.$SwitchMap$io$trickle$task$sites$Site[this.SITE.ordinal()]) {
            case 1: {
                jsonObject2.put("PX340", (Object)"v1.13.2");
                jsonObject2.put("PX341", (Object)"Hibbett | City Gear");
                jsonObject2.put("PX342", (Object)"4.15.0");
                jsonObject2.put("PX348", (Object)"com.hibbett.android");
                break;
            }
            case 2: {
                jsonObject2.put("PX340", (Object)"v1.8.0");
                jsonObject2.put("PX341", (Object)"Walmart");
                jsonObject2.put("PX342", (Object)"21.12");
                jsonObject2.put("PX348", (Object)"com.walmart.android");
                break;
            }
        }
        jsonObject2.put("PX343", (Object)this.PX343);
        jsonObject2.put("PX344", (Object)this.PX344);
        jsonObject2.put("PX347", (Object)this.PX347);
        jsonObject2.put("PX413", (Object)this.PX413.getBatteryHealth());
        jsonObject2.put("PX414", (Object)this.PX414.getChargingStatus());
        jsonObject2.put("PX415", (Object)this.PX415.getBatteryPercent());
        jsonObject2.put("PX416", (Object)this.PX416.getChargingMethod());
        jsonObject2.put("PX419", (Object)this.PX419.getBatteryType());
        jsonObject2.put("PX418", (Object)Battery.roundTemperature(this.PX418.getTemperature()));
        jsonObject2.put("PX420", (Object)Battery.roundVoltage(this.PX420.getVoltage()));
        jsonObject.put("t", (Object)this.t);
        jsonObject.put("d", (Object)jsonObject2);
        return new JsonArray().add((Object)jsonObject).encode();
    }

    public static int m4764a(int n, int n2, int n3, int n4) {
        int n5 = n4 % 10;
        int n6 = n5 != 0 ? n3 % n5 : n3 % 10;
        int n7 = n * n;
        int n8 = n2 * n2;
        switch (n6) {
            case 0: {
                return n7 + n2;
            }
            case 1: {
                return n + n8;
            }
            case 2: {
                return n7 * n2;
            }
            case 3: {
                return n ^ n2;
            }
            case 4: {
                return n - n8;
            }
            case 5: {
                int n9 = n + 783;
                return n9 * n9 + n8;
            }
            case 6: {
                return (n ^ n2) + n2;
            }
            case 7: {
                return n7 - n8;
            }
            case 8: {
                return n * n2;
            }
            case 9: {
                return n2 * n - n;
            }
        }
        return -1;
    }

    public SecondPayload(FirstPayload firstPayload, JsonObject jsonObject, long l, Site site) {
        this.device = firstPayload.device;
        this.sdkInitCount = firstPayload.PX345;
        this.SITE = site;
        JsonArray jsonArray = jsonObject.getJsonArray("do");
        if (jsonArray.size() < 3 && firstPayload.SID_HEADER == null) {
            logger.error("Could not find P-TS!");
            logger.error(jsonArray.toString());
            throw new Exception("Invalid do arr");
        }
        this.SID_HEADER = firstPayload.SID_HEADER != null ? firstPayload.SID_HEADER : jsonArray.getString(0).split("\\|")[1];
        this.VID_HEADER = firstPayload.VID_HEADER != null ? firstPayload.VID_HEADER : jsonArray.getString(1).split("\\|")[1];
        this.UUIDV4_HEADER = firstPayload.UUIDV4_HEADER;
        this.t = "PX329";
        this.PX349 = l;
        this.PX320 = firstPayload.PX320;
        String string = jsonArray.getString(jsonArray.size() - 1);
        this.responseValues = string.replace("appc|", "").split("\\|");
        if (this.responseValues.length != 9) {
            logger.error("Could not find P-TS!");
            logger.error(Arrays.toString(this.responseValues));
            throw new Exception("Invalid do arr");
        }
        this.PX259 = this.parseAppc2Timestamp();
        this.PX256 = this.parseAppc2PX256();
        this.PX257 = "" + this.m4765a(Integer.parseInt(this.responseValues[5]), Integer.parseInt(this.responseValues[6]), Integer.parseInt(this.responseValues[7]), Integer.parseInt(this.responseValues[3]), Integer.parseInt(this.responseValues[4]), Integer.parseInt(this.responseValues[8]));
        this.PX339 = firstPayload.PX339;
        this.PX322 = firstPayload.PX322;
        this.PX343 = firstPayload.PX343;
        this.PX344 = firstPayload.PX344;
        this.PX347 = firstPayload.PX347;
        this.PX413 = firstPayload.PX413;
        this.PX414 = firstPayload.PX414;
        this.PX415 = firstPayload.PX415;
        this.PX416 = firstPayload.PX416;
        this.PX419 = firstPayload.PX419;
        this.PX418 = firstPayload.PX418;
        this.PX420 = firstPayload.PX420;
    }

    public String getPayload() {
        return Base64.getEncoder().encodeToString(this.toString().getBytes(StandardCharsets.UTF_8));
    }

    public String getKeepAlivePayload() {
        return Base64.getEncoder().encodeToString(this.toStringFollowUpRequests().getBytes(StandardCharsets.UTF_8));
    }

    public String parseAppc2PX256() {
        return this.responseValues[2];
    }

    public String toStringFollowUpRequests() {
        JsonObject jsonObject = new JsonObject();
        JsonObject jsonObject2 = new JsonObject();
        this.PX413.recalculate();
        jsonObject2.put("PX349", (Object)this.PX349);
        jsonObject2.put("PX204", (Object)(++this.PX204));
        jsonObject2.put("PX339", (Object)this.PX339);
        jsonObject2.put("PX322", (Object)this.PX322);
        switch (SecondPayload$1.$SwitchMap$io$trickle$task$sites$Site[this.SITE.ordinal()]) {
            case 1: {
                jsonObject2.put("PX340", (Object)"v1.13.2");
                jsonObject2.put("PX341", (Object)"Hibbett | City Gear");
                jsonObject2.put("PX342", (Object)"4.15.0");
                jsonObject2.put("PX348", (Object)"com.hibbett.android");
                break;
            }
            case 2: {
                jsonObject2.put("PX340", (Object)"v1.8.0");
                jsonObject2.put("PX341", (Object)"Walmart");
                jsonObject2.put("PX342", (Object)"21.12");
                jsonObject2.put("PX348", (Object)"com.walmart.android");
                break;
            }
        }
        jsonObject2.put("PX343", (Object)this.PX343);
        jsonObject2.put("PX344", (Object)this.PX344);
        jsonObject2.put("PX347", (Object)this.PX347);
        jsonObject2.put("PX413", (Object)this.PX413.getBatteryHealth());
        jsonObject2.put("PX414", (Object)this.PX414.getChargingStatus());
        jsonObject2.put("PX415", (Object)this.PX415.getBatteryPercent());
        jsonObject2.put("PX416", (Object)this.PX416.getChargingMethod());
        jsonObject2.put("PX419", (Object)this.PX419.getBatteryType());
        jsonObject2.put("PX418", (Object)Battery.roundTemperature(this.PX418.getTemperature()));
        jsonObject2.put("PX420", (Object)Battery.roundVoltage(this.PX420.getVoltage()));
        jsonObject.put("t", (Object)"PX325");
        jsonObject.put("d", (Object)jsonObject2);
        return new JsonArray().add((Object)jsonObject).encode();
    }
}

