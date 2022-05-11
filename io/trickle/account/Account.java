/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.account.AccountController
 *  net.openhft.hashing.LongHashFunction
 */
package io.trickle.account;

import io.trickle.account.AccountController;
import net.openhft.hashing.LongHashFunction;

public class Account {
    public String cacheKey;
    public String sessionString;
    public String user;
    public String site;
    public String pass;

    public String sessionCacheKey() {
        if (this.cacheKey != null) return this.cacheKey;
        this.cacheKey = String.valueOf(LongHashFunction.wy_3().hashChars(this.getUser() + this.getSite()));
        return this.cacheKey;
    }

    public String toString() {
        return this.user + ":" + this.pass;
    }

    public String getPass() {
        return this.pass;
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

    public void setSite(String string) {
        this.site = string;
    }

    public String getSessionString() {
        return this.sessionString;
    }

    public String getSite() {
        return this.site;
    }

    public String getUser() {
        return this.user;
    }

    public Account(String string, String string2) {
        this.user = string;
        this.pass = string2;
    }

    public Account(String string, String string2, String string3) {
        this.user = string;
        this.pass = string2;
        this.site = string3;
    }

    public static Account fromArray(String[] stringArray) {
        if (stringArray.length != 2) return null;
        return new Account(stringArray[0], stringArray[1]);
    }

    public static Account fromString(String string) {
        return Account.fromArray(string.split(":"));
    }

    public void setSessionString(String string) {
        this.sessionString = string;
    }
}
