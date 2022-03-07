/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.net.ProxyOptions
 *  io.vertx.core.net.ProxyType
 */
package io.trickle.proxy;

import io.vertx.core.net.ProxyOptions;
import io.vertx.core.net.ProxyType;

public class Proxy {
    public boolean isLocal;
    public String host;
    public String user;
    public String pass;
    public boolean ipAuth;
    public String port;

    public static Proxy fromArray(String[] stringArray) {
        if (stringArray.length == 2) {
            return new Proxy(stringArray[0], stringArray[1]);
        }
        if (stringArray.length != 4) return new Proxy();
        return new Proxy(stringArray[0], stringArray[1], stringArray[2], stringArray[3]);
    }

    public ProxyOptions getAsVertx() {
        return Proxy.toVertxProxy(this);
    }

    public String[] toParams() {
        String[] stringArray = new String[this.ipAuth ? 2 : 4];
        stringArray[0] = this.host;
        stringArray[1] = this.port;
        if (this.ipAuth) return stringArray;
        stringArray[2] = this.user;
        stringArray[3] = this.pass;
        return stringArray;
    }

    public Proxy(String string, String string2, String string3, String string4) {
        this.host = string;
        this.port = string2;
        this.user = string3;
        this.pass = string4;
        this.ipAuth = false;
        this.isLocal = false;
    }

    public boolean isIpAuth() {
        return this.ipAuth;
    }

    public boolean isLocal() {
        return this.isLocal;
    }

    public static Proxy fromString(String string) {
        return Proxy.fromArray(string.split(":"));
    }

    public String toString() {
        return "Proxy{host='" + this.host + "', port='" + this.port + "', user='" + this.user + "', pass='" + this.pass + "', ipAuth=" + this.ipAuth + "', isLocal=" + this.isLocal + "}";
    }

    public Proxy(String string, String string2) {
        this.host = string;
        this.port = string2;
        this.user = null;
        this.pass = null;
        this.ipAuth = true;
        this.isLocal = false;
    }

    public String string() {
        return this.host + ":" + this.port;
    }

    public static ProxyOptions toVertxProxy(Proxy proxy) {
        if (proxy.isLocal) {
            return null;
        }
        ProxyOptions proxyOptions = new ProxyOptions();
        proxyOptions.setHost(proxy.host).setPort(Integer.parseInt(proxy.port));
        if (!proxy.ipAuth) {
            proxyOptions.setUsername(proxy.user);
            proxyOptions.setPassword(proxy.pass);
        }
        proxyOptions.setType(ProxyType.HTTP);
        return proxyOptions;
    }

    public Proxy() {
        this.host = null;
        this.port = null;
        this.user = null;
        this.pass = null;
        this.ipAuth = true;
        this.isLocal = true;
    }

    public String getHost() {
        return this.host;
    }
}

