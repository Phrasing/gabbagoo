/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.handler.codec.http.cookie.Cookie
 *  io.trickle.webclient.CookieJar
 */
package io.trickle.harvester;

import io.netty.handler.codec.http.cookie.Cookie;
import io.trickle.webclient.CookieJar;

public class LoginToken {
    public Iterable<Cookie> cookies;
    public String html;
    public CookieJar cookieJar;
    public String domain;
    public String proxyStr;

    public Iterable getCookies() {
        return this.cookies;
    }

    public String getProxyStr() {
        return this.proxyStr;
    }

    public LoginToken(String string, Iterable iterable, String string2, CookieJar cookieJar, String string3) {
        this.domain = string;
        this.cookies = iterable;
        this.proxyStr = string2;
        this.cookieJar = cookieJar;
        this.html = string3;
    }

    public CookieJar getCookieJar() {
        return this.cookieJar;
    }

    public String getHtml() {
        return this.html;
    }

    public String getDomain() {
        return this.domain;
    }
}
