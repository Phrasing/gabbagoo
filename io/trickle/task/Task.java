/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.impl.ConcurrentHashSet
 *  net.openhft.hashing.LongHashFunction
 */
package io.trickle.task;

import io.trickle.profile.Profile;
import io.trickle.task.sites.Site;
import io.trickle.util.Storage;
import io.vertx.core.impl.ConcurrentHashSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import net.openhft.hashing.LongHashFunction;

public class Task {
    public Site site;
    public static ConcurrentHashSet<String> bannedZipcodes;
    public Profile profile;
    public String captchaKey;
    public String hash = "";
    public String[] keywords;
    public Integer monitorDelayMass;
    public static Hashtable<String, Hashtable<String, List<Task>>> ysTaskTable;
    public int taskQuantity;
    public String password;
    public String mode;
    public int retryDelay;
    public String shippingRate;
    public String size;
    public int monitorDelay;
    public String siteUserEntry;
    public String qty;

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
        try {
            Hashtable<String, List<Task>> hashtable;
            if (this.site != Site.YEEZY) return;
            if (!ysTaskTable.containsKey(this.profile.getZip())) {
                ysTaskTable.put(this.profile.getZip(), new Hashtable());
            }
            if (!(hashtable = ysTaskTable.get(this.profile.getZip())).containsKey(this.keywords[0])) {
                hashtable.put(this.keywords[0], new ArrayList());
            }
            List<Task> list = hashtable.get(this.keywords[0]);
            list.add(this);
            return;
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public void setBannedZipCode(String string) {
        bannedZipcodes.add((Object)string);
        ysTaskTable.remove(string);
    }

    public Task checkYSTaskAndRotateIfNeeded(String string) {
        if (!bannedZipcodes.contains((Object)string)) return null;
        String string2 = new ArrayList<String>(ysTaskTable.keySet()).get(ThreadLocalRandom.current().nextInt(ysTaskTable.size()));
        List<Task> list = ysTaskTable.get(string2).get(this.keywords[0]);
        return list.get(ThreadLocalRandom.current().nextInt(list.size()));
    }

    public String getCaptchaKey() {
        return this.captchaKey;
    }

    public Profile getProfile() {
        return this.profile;
    }

    public String session() {
        if (!this.hash.isBlank()) return this.hash;
        this.hash = String.valueOf(LongHashFunction.wy_3().hashChars(Storage.ACCESS_KEY != null ? Storage.ACCESS_KEY : "") + (long)super.hashCode());
        return this.hash;
    }

    public String getMode() {
        return this.mode;
    }

    public String toString() {
        if ("Task{site=" + this.site + ", siteUserEntry='" + this.siteUserEntry + "', profile=" + this.profile + ", size='" + this.size + "', qty='" + this.qty + "', mode='" + this.mode + "', captchaKey='" + this.captchaKey + "', taskQuantity=" + this.taskQuantity + ", keywords=" + Arrays.toString(this.keywords) + ", retryDelay=" + this.retryDelay + ", monitorDelay=" + this.monitorDelay + ", shippingRate='" + this.shippingRate == null) {
            return "NaN";
        }
        String string = this.shippingRate + "', monitorDelayMass=" + this.monitorDelayMass + "}";
        return string;
    }

    static {
        ysTaskTable = new Hashtable(5);
        bannedZipcodes = new ConcurrentHashSet();
    }

    public void setMonitorDelay(Integer n) {
        this.monitorDelayMass = n;
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

    public String getSiteUserEntry() {
        return this.siteUserEntry;
    }

    public int getRetryDelay() {
        return this.retryDelay;
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

    public Site getSite() {
        return this.site;
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

    public String getShippingRate() {
        return this.shippingRate;
    }

    public void setKeywords(String[] stringArray) {
        this.keywords = stringArray;
    }

    public String[] getKeywords() {
        return this.keywords;
    }

    public Task copy() {
        return new Task(this);
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

    public String getPassword() {
        return this.password;
    }

    public int getTaskQuantity() {
        return this.taskQuantity;
    }

    public void setPassword(String string) {
        this.password = string;
    }
}

