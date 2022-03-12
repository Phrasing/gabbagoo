/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.netty.handler.codec.http.cookie.Cookie
 */
package io.trickle.harvester;

import io.netty.handler.codec.http.cookie.Cookie;
import io.trickle.webclient.CookieJar;

public class LoginToken {
    public Iterable<Cookie> cookies;
    public String html;
    public String proxyStr;
    public CookieJar cookieJar;
    public String domain;

    public CookieJar getCookieJar() {
        return this.cookieJar;
    }

    public String getHtml() {
        return this.html;
    }

    public String getDomain() {
        return this.domain;
    }

    public Iterable getCookies() {
        return this.cookies;
    }

    public LoginToken(String string, Iterable iterable, String string2, CookieJar cookieJar, String string3) {
        this.domain = string;
        this.cookies = iterable;
        this.proxyStr = string2;
        this.cookieJar = cookieJar;
        this.html = string3;
    }

    public String getProxyStr() {
        return this.proxyStr;
    }
}

