/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.netty.handler.codec.http.cookie.Cookie
 *  io.netty.handler.codec.http.cookie.DefaultCookie
 *  io.vertx.core.http.impl.HttpUtils
 *  io.vertx.ext.web.client.spi.CookieStore
 */
package io.trickle.webclient;

import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.DefaultCookie;
import io.trickle.webclient.CookieJar$ExpirableCookie;
import io.trickle.webclient.CookieJar$Key;
import io.vertx.core.http.impl.HttpUtils;
import io.vertx.ext.web.client.spi.CookieStore;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CookieJar
implements CookieStore {
    public boolean obeyExpiryTimes;
    public static Predicate<String> DEFAULT;
    public static boolean $assertionsDisabled;
    public ConcurrentHashMap<CookieJar$Key, CookieJar$ExpirableCookie> noDomainCookies;
    public ConcurrentSkipListMap<CookieJar$Key, CookieJar$ExpirableCookie> domainCookies;
    public Predicate<String> cookieFilter;

    public static void lambda$asString$3(StringBuilder stringBuilder, CookieJar$ExpirableCookie cookieJar$ExpirableCookie) {
        stringBuilder.append(cookieJar$ExpirableCookie.wrappedCookie.name()).append("=").append(cookieJar$ExpirableCookie.wrappedCookie.value()).append(";");
    }

    public Cookie putAndGet(String string, String string2) {
        return this.putAndGet(string, string2, null);
    }

    public static ConcurrentHashMap deepCopy(ConcurrentHashMap concurrentHashMap) {
        ConcurrentHashMap concurrentHashMap2 = new ConcurrentHashMap();
        concurrentHashMap.forEach(concurrentHashMap2::put);
        return concurrentHashMap2;
    }

    public static void lambda$asString$2(StringBuilder stringBuilder, CookieJar$ExpirableCookie cookieJar$ExpirableCookie) {
        stringBuilder.append(cookieJar$ExpirableCookie.wrappedCookie.name()).append("=").append(cookieJar$ExpirableCookie.wrappedCookie.value()).append(";");
    }

    public boolean contains(String string) {
        CookieJar$ExpirableCookie cookieJar$ExpirableCookie2;
        Objects.requireNonNull(string);
        for (CookieJar$ExpirableCookie cookieJar$ExpirableCookie2 : this.domainCookies.values()) {
            if (!cookieJar$ExpirableCookie2.wrappedCookie.name().equalsIgnoreCase(string)) continue;
            return true;
        }
        Iterator<CookieJar$ExpirableCookie> iterator = this.noDomainCookies.values().iterator();
        do {
            if (!iterator.hasNext()) return false;
            cookieJar$ExpirableCookie2 = iterator.next();
        } while (!cookieJar$ExpirableCookie2.wrappedCookie.name().equalsIgnoreCase(string));
        return true;
    }

    public CookieStore put(String string, String string2, String string3, String string4) {
        DefaultCookie defaultCookie = new DefaultCookie(string, string2);
        if (string3 != null) {
            defaultCookie.setDomain(string3);
        }
        defaultCookie.setPath(Objects.requireNonNullElse(string4, "/"));
        return this.put((Cookie)defaultCookie);
    }

    public CookieStore put(Cookie cookie) {
        CookieJar$Key cookieJar$Key = new CookieJar$Key(cookie.domain(), cookie.path(), cookie.name());
        CookieJar$ExpirableCookie cookieJar$ExpirableCookie = new CookieJar$ExpirableCookie(cookie);
        if (this.obeyExpiryTimes && cookie.maxAge() != Long.MIN_VALUE) {
            cookieJar$ExpirableCookie.setExpiry(cookie.maxAge());
        }
        if (cookieJar$Key.domain.equals("")) {
            this.noDomainCookies.put(cookieJar$Key, cookieJar$ExpirableCookie);
            return this;
        }
        this.domainCookies.put(cookieJar$Key, cookieJar$ExpirableCookie);
        return this;
    }

    public static void lambda$print$4(CookieJar$ExpirableCookie cookieJar$ExpirableCookie) {
        System.out.println("NoDomain: " + cookieJar$ExpirableCookie);
    }

    public Iterable get(Boolean bl, String string, String string2) {
        Object object2;
        int n;
        if (!$assertionsDisabled) {
            if (string == null) throw new AssertionError();
            if (string.length() <= 0) {
                throw new AssertionError();
            }
        }
        if ((n = ((String)(object2 = HttpUtils.removeDots((CharSequence)string2))).indexOf(63)) > -1) {
            object2 = ((String)object2).substring(0, n);
        }
        if ((n = ((String)object2).indexOf(35)) > -1) {
            object2 = ((String)object2).substring(0, n);
        }
        String string3 = object2;
        object2 = new TreeMap();
        Consumer<Cookie> consumer = arg_0 -> CookieJar.lambda$get$1(bl, string3, (TreeMap)object2, arg_0);
        if (!this.obeyExpiryTimes) {
            for (CookieJar$ExpirableCookie cookieJar$ExpirableCookie : this.noDomainCookies.values()) {
                consumer.accept(cookieJar$ExpirableCookie.wrappedCookie);
            }
        } else {
            for (Map.Entry object3 : this.noDomainCookies.entrySet()) {
                if (((CookieJar$ExpirableCookie)object3.getValue()).shouldExpire()) {
                    this.noDomainCookies.remove(object3.getKey());
                    continue;
                }
                consumer.accept(((CookieJar$ExpirableCookie)object3.getValue()).wrappedCookie);
            }
        }
        CookieJar$Key cookieJar$Key = new CookieJar$Key(string, "", "");
        String string4 = cookieJar$Key.domain.substring(0, 1);
        Iterator iterator = this.domainCookies.tailMap((Object)new CookieJar$Key(string4, "", ""), true).entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = iterator.next();
            if (((CookieJar$Key)entry.getKey()).domain.compareTo(cookieJar$Key.domain) > 0) {
                return ((TreeMap)object2).values();
            }
            if (!cookieJar$Key.domain.startsWith(((CookieJar$Key)entry.getKey()).domain)) continue;
            if (this.obeyExpiryTimes && ((CookieJar$ExpirableCookie)entry.getValue()).shouldExpire()) {
                this.domainCookies.remove(entry.getKey());
                continue;
            }
            consumer.accept(((CookieJar$ExpirableCookie)entry.getValue()).wrappedCookie);
        }
        return ((TreeMap)object2).values();
    }

    public CookieStore remove(Cookie cookie) {
        CookieJar$Key cookieJar$Key = new CookieJar$Key(cookie.domain(), cookie.path(), cookie.name());
        if (cookieJar$Key.domain.equals("")) {
            this.noDomainCookies.remove(cookieJar$Key);
            return this;
        }
        this.domainCookies.remove(cookieJar$Key);
        return this;
    }

    public static boolean lambda$static$0(String string) {
        return true;
    }

    public CookieStore put(String string, String string2, String string3) {
        return this.put(string, string2, string3, "/");
    }

    public void printDeep() {
        this.domainCookies.forEach(CookieJar::lambda$printDeep$6);
    }

    public CookieJar(CookieJar cookieJar) {
        this.noDomainCookies = CookieJar.deepCopy(cookieJar.noDomainCookies);
        this.domainCookies = CookieJar.deepCopy(cookieJar.domainCookies);
        this.cookieFilter = cookieJar.cookieFilter;
        this.obeyExpiryTimes = cookieJar.obeyExpiryTimes;
    }

    public static void lambda$print$5(CookieJar$ExpirableCookie cookieJar$ExpirableCookie) {
        System.out.println("Domain: " + cookieJar$ExpirableCookie);
    }

    static {
        $assertionsDisabled = !CookieJar.class.desiredAssertionStatus();
        DEFAULT = CookieJar::lambda$static$0;
    }

    public CookieStore putFromOther(CookieJar cookieJar) {
        this.noDomainCookies.putAll(cookieJar.noDomainCookies);
        this.domainCookies.putAll(cookieJar.domainCookies);
        return this;
    }

    public String getCookieValue(String string) {
        CookieJar$ExpirableCookie cookieJar$ExpirableCookie;
        Iterator<CookieJar$ExpirableCookie> iterator = this.domainCookies.values().iterator();
        do {
            if (!iterator.hasNext()) return null;
            cookieJar$ExpirableCookie = iterator.next();
        } while (!cookieJar$ExpirableCookie.wrappedCookie.name().equalsIgnoreCase(string));
        return cookieJar$ExpirableCookie.wrappedCookie.value();
    }

    public void remove(String string, String string2, String string3) {
        CookieJar$Key cookieJar$Key = new CookieJar$Key(string3, string2, string);
        if (cookieJar$Key.domain.equals("")) {
            this.noDomainCookies.remove(cookieJar$Key);
            return;
        }
        this.domainCookies.remove(cookieJar$Key);
    }

    public void setCookieFilter(Predicate predicate) {
        this.cookieFilter = predicate;
    }

    public static void lambda$get$1(Boolean bl, String string, TreeMap treeMap, Cookie cookie) {
        if (bl != Boolean.TRUE && cookie.isSecure()) {
            return;
        }
        if (cookie.path() != null && !string.equals(cookie.path())) {
            Object object = cookie.path();
            if (!((String)object).endsWith("/")) {
                object = (String)object + "/";
            }
            if (!string.startsWith((String)object)) {
                return;
            }
        }
        treeMap.put(cookie.name(), cookie);
    }

    public CookieJar(Predicate predicate) {
        this.noDomainCookies = new ConcurrentHashMap();
        this.domainCookies = new ConcurrentSkipListMap();
        this.cookieFilter = predicate;
        this.obeyExpiryTimes = false;
    }

    public boolean contains(Cookie cookie) {
        Objects.requireNonNull(cookie);
        return this.contains(cookie.name());
    }

    public void removeAnyMatch(String string) {
        this.removeMatchFromIter(string, this.noDomainCookies.keys().asIterator());
        this.removeMatchFromIter(string, this.domainCookies.keySet().iterator());
    }

    public String getCookieValue(String string, String string2) {
        String string3;
        String string4 = this.getCookieValue(string);
        if (string4 == null) {
            string3 = string2;
            return string3;
        }
        string3 = string4;
        return string3;
    }

    public void removeMatchFromIter(String string, Iterator iterator) {
        while (iterator.hasNext()) {
            try {
                CookieJar$Key cookieJar$Key = (CookieJar$Key)iterator.next();
                if (cookieJar$Key == null || cookieJar$Key.name == null || cookieJar$Key.name.isEmpty() || !cookieJar$Key.name.equalsIgnoreCase(string)) continue;
                iterator.remove();
            }
            catch (Throwable throwable) {}
        }
    }

    public void print() {
        this.noDomainCookies.values().forEach(CookieJar::lambda$print$4);
        this.domainCookies.values().forEach(CookieJar::lambda$print$5);
    }

    public Cookie putAndGet(String string, String string2, String string3) {
        DefaultCookie defaultCookie = new DefaultCookie(string, string2);
        if (string3 != null) {
            defaultCookie.setDomain(string3);
        }
        this.put((Cookie)defaultCookie);
        return defaultCookie;
    }

    public Predicate getCookieFilter() {
        return this.cookieFilter;
    }

    public static void lambda$printDeep$6(CookieJar$Key cookieJar$Key, CookieJar$ExpirableCookie cookieJar$ExpirableCookie) {
        System.out.println(cookieJar$Key + " " + cookieJar$ExpirableCookie);
    }

    public CookieJar() {
        this.noDomainCookies = new ConcurrentHashMap();
        this.domainCookies = new ConcurrentSkipListMap();
        this.cookieFilter = DEFAULT;
        this.obeyExpiryTimes = true;
    }

    public int getCookieValueLength(String string) {
        CookieJar$ExpirableCookie cookieJar$ExpirableCookie;
        Iterator<CookieJar$ExpirableCookie> iterator = this.domainCookies.values().iterator();
        do {
            if (!iterator.hasNext()) return -1;
            cookieJar$ExpirableCookie = iterator.next();
        } while (!cookieJar$ExpirableCookie.wrappedCookie.name().equalsIgnoreCase(string));
        return cookieJar$ExpirableCookie.wrappedCookie.value().length();
    }

    public void remove(String string, String string2) {
        CookieJar$Key cookieJar$Key = new CookieJar$Key(string2, null, string);
        if (cookieJar$Key.domain.equals("")) {
            this.noDomainCookies.remove(cookieJar$Key);
            return;
        }
        this.domainCookies.remove(cookieJar$Key);
    }

    public int size() {
        return this.noDomainCookies.size() + this.domainCookies.size();
    }

    public void remove(String string) {
        this.remove(string, null);
    }

    public CookieJar(boolean bl) {
        this.noDomainCookies = new ConcurrentHashMap();
        this.domainCookies = new ConcurrentSkipListMap();
        this.cookieFilter = DEFAULT;
        this.obeyExpiryTimes = bl;
    }

    public CookieStore clear() {
        this.noDomainCookies.clear();
        this.domainCookies.clear();
        return this;
    }

    public static ConcurrentSkipListMap deepCopy(ConcurrentSkipListMap concurrentSkipListMap) {
        ConcurrentSkipListMap concurrentSkipListMap2 = new ConcurrentSkipListMap();
        concurrentSkipListMap.forEach(concurrentSkipListMap2::put);
        return concurrentSkipListMap2;
    }

    public String asString() {
        if (this.domainCookies.isEmpty()) {
            if (this.noDomainCookies.isEmpty()) return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        this.domainCookies.values().forEach(arg_0 -> CookieJar.lambda$asString$2(stringBuilder, arg_0));
        this.noDomainCookies.values().forEach(arg_0 -> CookieJar.lambda$asString$3(stringBuilder, arg_0));
        return stringBuilder.toString().trim();
    }

    public CookieStore put(String string, String string2) {
        return this.put(string, string2, null);
    }
}

