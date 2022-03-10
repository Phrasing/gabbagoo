/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.openhft.hashing.LongHashFunction
 */
package io.trickle.task;

import io.trickle.profile.Profile;
import io.trickle.task.sites.Site;
import io.trickle.task.sites.yeezy.util.profile.ProfileMap;
import io.trickle.util.Storage;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import net.openhft.hashing.LongHashFunction;

public class Task {
    public Site site;
    public String siteUserEntry;
    public Profile profile;
    public int taskQuantity;
    public String size;
    public String[] keywords;
    public int monitorDelay;
    public String shippingRate;
    public String mode;
    public Integer monitorDelayMass;
    public String captchaKey;
    public String password;
    public static ProfileMap ysProfileMap = new ProfileMap();
    public String qty;
    public int retryDelay;
    public String hash = "";

    public Task(String[] stringArray) {
        this.site = Site.getSite(stringArray[19]);
        this.keywords = this.site == Site.BESTBUY ? stringArray[0].replace("  ", " ").split(" ") : stringArray[0].toUpperCase().replace("  ", " ").split(" ");
        this.qty = this.size = stringArray[1];
        this.profile = new Profile(stringArray);
        this.taskQuantity = Integer.parseInt(stringArray[16]);
        this.retryDelay = Integer.parseInt(stringArray[17]);
        this.monitorDelay = Integer.parseInt(stringArray[18]);
        this.siteUserEntry = stringArray[19];
        this.mode = stringArray[20].toLowerCase();
        if (stringArray.length <= 22) {
            if (stringArray.length == 22) {
                this.captchaKey = stringArray[21];
            }
            this.shippingRate = "";
        } else {
            this.captchaKey = stringArray[21];
            this.shippingRate = stringArray[22];
        }
        this.shippingRate = this.shippingRate.replace("\r", "").replace("\n", "");
        this.enforceDelayLimits();
        if (this.site != Site.YEEZY) return;
        try {
            ysProfileMap.put(this);
            return;
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public Task copy() {
        return new Task(this);
    }

    public void setKeywords(String[] stringArray) {
        this.keywords = stringArray;
    }

    public int getMonitorDelay() {
        int n;
        if (this.monitorDelayMass == null) {
            n = this.monitorDelay;
            return n;
        }
        n = this.monitorDelayMass;
        return n;
    }

    public String[] getKeywords() {
        return this.keywords;
    }

    public String getShippingRate() {
        return this.shippingRate;
    }

    public String toString() {
        if ("Task{site=" + this.site + ", siteUserEntry='" + this.siteUserEntry + "', profile=" + this.profile + ", size='" + this.size + "', qty='" + this.qty + "', mode='" + this.mode + "', captchaKey='" + this.captchaKey + "', taskQuantity=" + this.taskQuantity + ", keywords=" + Arrays.toString(this.keywords) + ", retryDelay=" + this.retryDelay + ", monitorDelay=" + this.monitorDelay + ", shippingRate='" + this.shippingRate == null) {
            return "NaN";
        }
        String string = this.shippingRate + "', monitorDelayMass=" + this.monitorDelayMass + "}";
        return string;
    }

    public String getCaptchaKey() {
        return this.captchaKey;
    }

    public String getQty() {
        return this.qty;
    }

    public static String randomizeCase(String string) {
        StringBuilder stringBuilder = new StringBuilder(string.length());
        char[] cArray = string.toCharArray();
        int n = cArray.length;
        int n2 = 0;
        while (n2 < n) {
            char c2 = cArray[n2];
            stringBuilder.append(ThreadLocalRandom.current().nextBoolean() ? Character.toLowerCase(c2) : Character.toUpperCase(c2));
            ++n2;
        }
        return stringBuilder.toString();
    }

    public void setPassword(String string) {
        this.password = string;
    }

    public String session() {
        if (!this.hash.isBlank()) return this.hash;
        this.hash = String.valueOf(LongHashFunction.wy_3().hashChars(Storage.ACCESS_KEY != null ? Storage.ACCESS_KEY : "") + (long)super.hashCode());
        return this.hash;
    }

    public Site getSite() {
        return this.site;
    }

    public void enforceDelayLimits() {
        if (!this.site.equals((Object)Site.WALMART)) {
            if (!this.site.equals((Object)Site.WALMART_CA)) return;
        }
        if (this.mode.contains("desktop")) {
            this.retryDelay = Math.max(this.retryDelay, 2000);
            this.monitorDelay = Math.max(this.monitorDelay, 500);
            return;
        }
        this.retryDelay = Math.max(this.retryDelay, 4000);
        this.monitorDelay = Math.max(this.monitorDelay, 5000);
    }

    public String getMode() {
        return this.mode;
    }

    public String getSiteUserEntry() {
        return this.siteUserEntry;
    }

    public int getRetryDelay() {
        return this.retryDelay;
    }

    public Profile getProfile() {
        return this.profile;
    }

    public int getTaskQuantity() {
        return this.taskQuantity;
    }

    public String getSize() {
        return this.size;
    }

    public Task(Task task) {
        this.site = task.site;
        this.siteUserEntry = task.siteUserEntry;
        this.profile = task.profile.copy();
        this.size = task.size;
        this.qty = task.qty;
        this.mode = task.mode;
        this.captchaKey = task.captchaKey;
        this.taskQuantity = task.taskQuantity;
        this.keywords = task.keywords;
        this.retryDelay = task.retryDelay;
        this.monitorDelay = task.monitorDelay;
        this.shippingRate = task.shippingRate;
        this.monitorDelayMass = task.monitorDelayMass;
    }

    public void setMonitorDelay(Integer n) {
        this.monitorDelayMass = n;
    }

    public String getPassword() {
        return this.password;
    }
}

