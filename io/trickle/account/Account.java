/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.openhft.hashing.LongHashFunction
 */
package io.trickle.account;

import io.trickle.account.AccountController;
import io.trickle.task.sites.Site;
import net.openhft.hashing.LongHashFunction;

public class Account {
    public String pass;
    public String user;
    public Site site;
    public String sessionString;
    public String cacheKey;

    public Account(String string, String string2, Site site) {
        this.user = string;
        this.pass = string2;
        this.site = site;
    }

    public Account(String string, String string2) {
        this.user = string;
        this.pass = string2;
    }

    public void setSessionString(String string) {
        this.sessionString = string;
    }

    public static Account fromArray(String[] stringArray) {
        if (stringArray.length != 2) return null;
        return new Account(stringArray[0], stringArray[1]);
    }

    public String sessionCacheKey() {
        if (this.cacheKey != null) return this.cacheKey;
        this.cacheKey = String.valueOf(LongHashFunction.wy_3().hashChars(this.getUser() + this.getSite()));
        return this.cacheKey;
    }

    public String toString() {
        return this.user + ":" + this.pass;
    }

    public String lookupSession() {
        try {
            return AccountController.ACCOUNT_STORE.get(this.sessionCacheKey(), "");
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
            return "";
        }
    }

    public String getPass() {
        return this.pass;
    }

    public String getUser() {
        return this.user;
    }

    public String getSessionString() {
        return this.sessionString;
    }

    public static Account fromString(String string) {
        return Account.fromArray(string.split(":"));
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public String getSite() {
        if (this.site != null) return this.site.name();
        return "";
    }
}

