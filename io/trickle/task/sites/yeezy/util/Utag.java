/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.sites.yeezy.util;

import io.trickle.task.antibot.impl.akamai.sensor.Bmak;
import io.trickle.task.sites.yeezy.YeezyAPI;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

public class Utag {
    public String productName;
    public Bmak bmak;
    public long ses_id = -1L;
    public String v_id;
    public String sku;
    public String documentUrl;
    public static int session_timeout = 1800000;
    public String userAgent;
    public long exp;
    public int updateCookieCounter = 0;
    public long st;

    public static String r() {
        String string = "0123456789";
        Object object = "";
        Object object2 = "";
        int n = 10;
        int n2 = 10;
        int n3 = 0;
        while (19 > n3) {
            int n4 = ThreadLocalRandom.current().nextInt(n);
            object = (String)object + "0123456789".substring(n4, n4 + 1);
            if (0 == n3 && 9 == n4) {
                n = 3;
            } else if ((1 == n3 || 2 == n3) && 10 != n && 2 > n4) {
                n = 10;
            } else if (2 < n3) {
                n = 10;
            }
            n4 = ThreadLocalRandom.current().nextInt(n2);
            object2 = (String)object2 + "0123456789".substring(n4, n4 + 1);
            if (0 == n3 && 9 == n4) {
                n2 = 3;
            } else if ((1 == n3 || 2 == n3) && 10 != n2 && 2 > n4) {
                n2 = 10;
            } else if (2 < n3) {
                n2 = 10;
            }
            ++n3;
        }
        return (String)object + (String)object2;
    }

    public String getEncodedProductName() {
        return "PRODUCT%7C" + this.productName + "%20(" + this.sku + ")";
    }

    public String vi(long l) {
        if (this.bmak != null && this.v_id == null) {
            Object object = this.pad("" + l, 12);
            String string = "" + ThreadLocalRandom.current().nextDouble();
            object = (String)object + this.pad(string.substring(2), 16);
            object = (String)object + this.pad("" + this.bmak.getDevice().getPluginLength(), 2);
            object = (String)object + this.pad("" + this.bmak.getDevice().getUserAgent().length(), 3);
            object = (String)object + this.pad("" + this.documentUrl.length(), 4);
            object = (String)object + this.pad("" + this.bmak.getDevice().getUserAgent().replace("Mozilla/", "").length(), 3);
            this.v_id = object = (String)object + this.pad("" + (this.bmak.getDevice().getScreenWidth() + this.bmak.getDevice().getScreenHeight() + this.bmak.getDevice().getColorDepth()), 5);
            return this.v_id;
        }
        if (this.userAgent == null) return this.v_id;
        if (this.v_id != null) return this.v_id;
        Object object = this.pad("" + l, 12);
        String string = "" + ThreadLocalRandom.current().nextDouble();
        object = (String)object + this.pad(string.substring(2), 16);
        object = (String)object + this.pad("3", 2);
        object = (String)object + this.pad("" + this.userAgent.length(), 3);
        object = (String)object + this.pad("" + this.documentUrl.length(), 4);
        object = (String)object + this.pad("" + this.userAgent.length(), 3);
        this.v_id = object = (String)object + this.pad("" + ((int)Math.floor(Math.random() * Double.longBitsToDouble(4655459775352406016L)) + 800 + (int)Math.floor(Math.random() * Double.longBitsToDouble(4650608730050658304L)) + 600 + 24), 5);
        return this.v_id;
    }

    public String getPrevPage() {
        if (this.documentUrl.contains("product")) return this.getEncodedProductName();
        if (this.documentUrl.contains("archive")) {
            return this.getEncodedProductName();
        }
        if (this.documentUrl.equals(YeezyAPI.QUEUE_URL)) {
            return this.getEncodedWR();
        }
        if (this.documentUrl.contains("/delivery")) {
            return this.getEncodedShipping();
        }
        if (!this.documentUrl.contains("/payment")) return "HOME";
        return this.getEncodedProcessing();
    }

    public static int H() {
        return (int)Math.ceil((double)Instant.now().toEpochMilli() / Double.longBitsToDouble(4725570615333879808L));
    }

    public Utag(Bmak bmak, String string) {
        this.bmak = bmak;
        this.userAgent = null;
        this.documentUrl = "https://www.yeezysupply.com/";
        this.sku = string;
    }

    public void updateDocumentUrl(String string) {
        this.documentUrl = string;
    }

    public String getEncodedShipping() {
        return "CHECKOUT%7CSHIPPING";
    }

    public String getEncodedWR() {
        return "WAITING%20ROOM%7C" + this.productName + "%7C" + this.productName + "%20(" + this.sku + ")";
    }

    public void setName(String string) {
        this.productName = string.replace(" ", "%20");
    }

    public String genUtagMain() {
        int n;
        this.vi(Instant.now().toEpochMilli());
        if (this.ses_id == -1L) {
            this.ses_id = Instant.now().toEpochMilli();
        }
        this.st = Instant.now().toEpochMilli() + 1800000L;
        this.exp = Instant.now().toEpochMilli() + (long)ThreadLocalRandom.current().nextInt(470, 480) + 3600000L;
        ++this.updateCookieCounter;
        if (this.updateCookieCounter == 1) {
            n = 1;
            return "v_id:" + this.v_id + "$_se:" + this.updateCookieCounter + "$_ss:" + n + "$_st:" + this.st + "$ses_id:" + this.ses_id + "%3Bexp-session$_pn:" + this.updateCookieCounter + "%3Bexp-session$_prevpage:" + this.getPrevPage() + "%3Bexp-" + this.exp;
        }
        n = 0;
        return "v_id:" + this.v_id + "$_se:" + this.updateCookieCounter + "$_ss:" + n + "$_st:" + this.st + "$ses_id:" + this.ses_id + "%3Bexp-session$_pn:" + this.updateCookieCounter + "%3Bexp-session$_prevpage:" + this.getPrevPage() + "%3Bexp-" + this.exp;
    }

    public Utag(String string, String string2) {
        this.bmak = null;
        this.userAgent = string;
        this.documentUrl = "https://www.yeezysupply.com/";
        this.sku = string2;
    }

    public String getEncodedProcessing() {
        return "CHECKOUT%7CPAYMENT";
    }

    public String pad(String object, int n) {
        object = Long.toString(Long.parseLong((String)object), 16);
        Object object2 = "";
        if (n <= ((String)object).length()) return (String)object2 + (String)object;
        int n2 = 0;
        while (n2 < n - ((String)object).length()) {
            object2 = (String)object2 + "0";
            ++n2;
        }
        return (String)object2 + (String)object;
    }
}

