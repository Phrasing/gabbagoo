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
    public String html;
    public String domain;
    public CookieJar cookieJar;
    public Iterable<Cookie> cookies;
    public String proxyStr;

    public LoginToken(String string, Iterable iterable, String string2, CookieJar cookieJar, String string3) {
        this.domain = string;
        this.cookies = iterable;
        this.proxyStr = string2;
        this.cookieJar = cookieJar;
        this.html = string3;
    }

    public String getDomain() {
        return this.domain;
    }

    public String getProxyStr() {
        return this.proxyStr;
    }

    public String getHtml() {
        return this.html;
    }

    public Iterable getCookies() {
        return this.cookies;
    }

    public CookieJar getCookieJar() {
        return this.cookieJar;
    }
}

