/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.netty.handler.codec.http.cookie.Cookie
 */
package io.trickle.webclient;

import io.netty.handler.codec.http.cookie.Cookie;

public class CookieJar$ExpirableCookie
implements Comparable {
    public long expiryTime;
    public Cookie wrappedCookie;

    public CookieJar$ExpirableCookie(Cookie cookie) {
        this.wrappedCookie = cookie;
    }

    public int compareTo(Object object) {
        return this.compareTo((Cookie)object);
    }

    public int compareTo(Cookie cookie) {
        return this.wrappedCookie.compareTo((Object)cookie);
    }

    public boolean shouldExpire() {
        if (this.expiryTime == 0L) {
            return false;
        }
        if (CookieJar$ExpirableCookie.getEpoch() < this.expiryTime) return false;
        return true;
    }

    public void setExpiry(long l) {
        if (this.expiryTime != 0L) return;
        this.expiryTime = CookieJar$ExpirableCookie.getEpoch() + l;
    }

    public String toString() {
        return "ExpirableCookie{expiryTime=" + this.expiryTime + ", wrappedCookie=" + this.wrappedCookie + "}";
    }

    public static long getEpoch() {
        return System.currentTimeMillis() / 1000L;
    }
}

