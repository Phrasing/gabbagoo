/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.profile.Profile
 *  io.trickle.task.sites.Site
 *  io.trickle.task.sites.yeezy.util.rotator.ProfileRotator
 *  io.trickle.util.Storage
 *  net.openhft.hashing.LongHashFunction
 */
package io.trickle.task;

import io.trickle.profile.Profile;
import io.trickle.task.sites.Site;
import io.trickle.task.sites.yeezy.util.rotator.ProfileRotator;
import io.trickle.util.Storage;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import net.openhft.hashing.LongHashFunction;

public class Task {
    public static final ProfileRotator profileRotator = new ProfileRotator();
    private final Site site;
    private final String siteUserEntry;
    private final Profile profile;
    private final String size;
    private final String qty;
    private final String mode;
    private final int taskQuantity;
    private String captchaKey;
    private String[] keywords;
    private int retryDelay;
    private int monitorDelay;
    private String shippingRate;
    private Integer monitorDelayMass;
    private String password;
    private String hash = "";

    public Task(Task other) {
        this.site = other.site;
        this.siteUserEntry = other.siteUserEntry;
        this.profile = other.profile.copy();
        this.size = other.size;
        this.qty = other.qty;
        this.mode = other.mode;
        this.captchaKey = other.captchaKey;
        this.taskQuantity = other.taskQuantity;
        this.keywords = other.keywords;
        this.retryDelay = other.retryDelay;
        this.monitorDelay = other.monitorDelay;
        this.shippingRate = other.shippingRate;
        this.monitorDelayMass = other.monitorDelayMass;
        if (this.site != Site.YEEZY) return;
        if (this.taskQuantity == 0) return;
        try {
            profileRotator.put(this);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public Task(String[] row) {
        this.site = Site.getSite((String)row[19]);
        this.keywords = this.site == Site.BESTBUY ? row[0].replace("  ", " ").split(" ") : row[0].toUpperCase().replace("  ", " ").split(" ");
        this.qty = this.size = row[1];
        this.profile = new Profile(row);
        this.taskQuantity = Integer.parseInt(row[16]);
        this.retryDelay = Integer.parseInt(row[17]);
        this.monitorDelay = Integer.parseInt(row[18]);
        this.siteUserEntry = row[19];
        this.mode = row[20].toLowerCase();
        if (row.length <= 22) {
            if (row.length == 22) {
                this.captchaKey = row[21];
            }
            this.shippingRate = "";
        } else {
            this.captchaKey = row[21];
            this.shippingRate = row[22];
        }
        this.shippingRate = this.shippingRate.replace("\r", "").replace("\n", "");
        this.enforceDelayLimits();
    }

    public static String randomizeCase(String str) {
        StringBuilder sb = new StringBuilder(str.length());
        char[] cArray = str.toCharArray();
        int n = cArray.length;
        int n2 = 0;
        while (n2 < n) {
            char c = cArray[n2];
            sb.append(ThreadLocalRandom.current().nextBoolean() ? Character.toLowerCase(c) : Character.toUpperCase(c));
            ++n2;
        }
        return sb.toString();
    }

    private void enforceDelayLimits() {
        if (!this.site.equals((Object)Site.WALMART)) {
            if (!this.site.equals((Object)Site.WALMART_CA)) return;
        }
        if (this.mode.contains("desktop")) {
            this.retryDelay = Math.max(this.retryDelay, 2000);
            this.monitorDelay = Math.max(this.monitorDelay, 500);
        } else {
            this.retryDelay = Math.max(this.retryDelay, 4000);
            this.monitorDelay = Math.max(this.monitorDelay, 5000);
        }
    }

    public Site getSite() {
        return this.site;
    }

    public String getSiteUserEntry() {
        return this.siteUserEntry;
    }

    public Profile getProfile() {
        return this.profile;
    }

    public String[] getKeywords() {
        return this.keywords;
    }

    public void setKeywords(String[] keywords) {
        this.keywords = keywords;
    }

    public String getSize() {
        return this.size;
    }

    public String getQty() {
        return this.qty;
    }

    public int getRetryDelay() {
        return this.retryDelay;
    }

    public int getMonitorDelay() {
        return this.monitorDelayMass == null ? this.monitorDelay : this.monitorDelayMass;
    }

    public void setMonitorDelay(Integer newDelay) {
        this.monitorDelayMass = newDelay;
    }

    public String getMode() {
        return this.mode;
    }

    public String getCaptchaKey() {
        return this.captchaKey;
    }

    public int getTaskQuantity() {
        return this.taskQuantity;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toString() {
        return "Task{site=" + this.site + ", siteUserEntry='" + this.siteUserEntry + "', profile=" + this.profile + ", size='" + this.size + "', qty='" + this.qty + "', mode='" + this.mode + "', captchaKey='" + this.captchaKey + "', taskQuantity=" + this.taskQuantity + ", keywords=" + Arrays.toString(this.keywords) + ", retryDelay=" + this.retryDelay + ", monitorDelay=" + this.monitorDelay + ", shippingRate='" + this.shippingRate == null ? "NaN" : this.shippingRate + "', monitorDelayMass=" + this.monitorDelayMass + "}";
    }

    public String getShippingRate() {
        return this.shippingRate;
    }

    public Task copy() {
        return new Task(this);
    }

    public String session() {
        if (!this.hash.isBlank()) return this.hash;
        this.hash = String.valueOf(LongHashFunction.wy_3().hashChars(Storage.ACCESS_KEY != null ? Storage.ACCESS_KEY : "") + (long)super.hashCode());
        return this.hash;
    }
}
