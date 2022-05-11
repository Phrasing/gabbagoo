/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.handler.codec.http.cookie.Cookie
 *  io.netty.handler.codec.http.cookie.DefaultCookie
 *  io.trickle.webclient.CookieJar$ExpirableCookie
 *  io.trickle.webclient.CookieJar$Key
 *  io.vertx.core.http.impl.HttpUtils
 *  io.vertx.core.json.JsonArray
 *  io.vertx.core.json.JsonObject
 *  io.vertx.ext.web.client.spi.CookieStore
 */
package io.trickle.webclient;

import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.DefaultCookie;
import io.trickle.webclient.CookieJar;
import io.vertx.core.http.impl.HttpUtils;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.spi.CookieStore;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CookieJar
implements CookieStore {
    public ConcurrentSkipListMap<Key, ExpirableCookie> domainCookies;
    public boolean obeyExpiryTimes;
    public Predicate<String> cookieFilter;
    public ConcurrentHashMap<Key, ExpirableCookie> noDomainCookies;
    public static boolean $assertionsDisabled = !CookieJar.class.desiredAssertionStatus();
    public static Predicate<String> DEFAULT = CookieJar::lambda$static$0;

    public void print() {
        this.noDomainCookies.values().forEach(CookieJar::lambda$print$6);
        this.domainCookies.values().forEach(CookieJar::lambda$print$7);
    }

    public void setCookieFilter(Predicate predicate) {
        this.cookieFilter = predicate;
    }

    public String asString() {
        if (this.domainCookies.isEmpty()) {
            if (this.noDomainCookies.isEmpty()) return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        this.domainCookies.values().forEach(arg_0 -> CookieJar.lambda$asString$4(stringBuilder, arg_0));
        this.noDomainCookies.values().forEach(arg_0 -> CookieJar.lambda$asString$5(stringBuilder, arg_0));
        return stringBuilder.toString().trim();
    }

    public CookieStore remove(Cookie cookie) {
        Key key = new Key(cookie.domain(), cookie.path(), cookie.name());
        if (key.domain.equals("")) {
            this.noDomainCookies.remove(key);
        } else {
            this.domainCookies.remove(key);
        }
        return this;
    }

    public static boolean lambda$static$0(String string) {
        return true;
    }

    public JsonArray asJson() {
        if (this.domainCookies.isEmpty()) {
            if (this.noDomainCookies.isEmpty()) return new JsonArray();
        }
        JsonArray jsonArray = new JsonArray();
        this.domainCookies.values().forEach(arg_0 -> CookieJar.lambda$asJson$1(jsonArray, arg_0));
        this.noDomainCookies.values().forEach(arg_0 -> CookieJar.lambda$asJson$2(jsonArray, arg_0));
        return jsonArray;
    }

    public Cookie putAndGet(String string, String string2) {
        return this.putAndGet(string, string2, null);
    }

    public boolean contains(String string) {
        ExpirableCookie expirableCookie2;
        Objects.requireNonNull(string);
        for (ExpirableCookie expirableCookie2 : this.domainCookies.values()) {
            if (!expirableCookie2.wrappedCookie.name().equalsIgnoreCase(string)) continue;
            return true;
        }
        Iterator<ExpirableCookie> iterator = this.noDomainCookies.values().iterator();
        do {
            if (!iterator.hasNext()) return false;
            expirableCookie2 = iterator.next();
        } while (!expirableCookie2.wrappedCookie.name().equalsIgnoreCase(string));
        return true;
    }

    public static void lambda$asString$5(StringBuilder stringBuilder, ExpirableCookie expirableCookie) {
        stringBuilder.append(expirableCookie.wrappedCookie.name()).append("=").append(expirableCookie.wrappedCookie.value()).append(";");
    }

    public CookieStore put(String string, String string2, String string3, String string4) {
        DefaultCookie defaultCookie = new DefaultCookie(string, string2);
        if (string3 != null) {
            defaultCookie.setDomain(string3);
        }
        defaultCookie.setPath(Objects.requireNonNullElse(string4, "/"));
        return this.put((Cookie)defaultCookie);
    }

    public Cookie putAndGet(String string, String string2, String string3) {
        DefaultCookie defaultCookie = new DefaultCookie(string, string2);
        if (string3 != null) {
            defaultCookie.setDomain(string3);
        }
        this.put((Cookie)defaultCookie);
        return defaultCookie;
    }

    public CookieStore put(JsonArray jsonArray) {
        int n = 0;
        while (n < jsonArray.size()) {
            JsonObject jsonObject = jsonArray.getJsonObject(n);
            this.put(jsonObject.getString("name"), jsonObject.getString("value"), jsonObject.getString("domain", null), jsonObject.getString("path", "/"));
            ++n;
        }
        return this;
    }

    public List getAll() {
        ArrayList<ExpirableCookie> arrayList = new ArrayList<ExpirableCookie>(this.noDomainCookies.values());
        arrayList.addAll(this.domainCookies.values());
        return arrayList;
    }

    public CookieJar(Predicate predicate) {
        this.noDomainCookies = new ConcurrentHashMap();
        this.domainCookies = new ConcurrentSkipListMap();
        this.cookieFilter = predicate;
        this.obeyExpiryTimes = false;
    }

    public void removeMatchFromIter(String string, Iterator iterator) {
        while (iterator.hasNext()) {
            try {
                Key key = (Key)iterator.next();
                if (key == null || key.name == null || key.name.isEmpty() || !key.name.equalsIgnoreCase(string)) continue;
                iterator.remove();
            }
            catch (Throwable throwable) {}
        }
    }

    public static void lambda$asString$4(StringBuilder stringBuilder, ExpirableCookie expirableCookie) {
        stringBuilder.append(expirableCookie.wrappedCookie.name()).append("=").append(expirableCookie.wrappedCookie.value()).append(";");
    }

    public CookieStore put(Cookie cookie) {
        Key key = new Key(cookie.domain(), cookie.path(), cookie.name());
        ExpirableCookie expirableCookie = new ExpirableCookie(cookie);
        if (this.obeyExpiryTimes && cookie.maxAge() != Long.MIN_VALUE) {
            expirableCookie.setExpiry(cookie.maxAge());
        }
        if (key.domain.equals("")) {
            this.noDomainCookies.put(key, expirableCookie);
            return this;
        }
        this.domainCookies.put(key, expirableCookie);
        return this;
    }

    public int getCookieValueLength(String string) {
        ExpirableCookie expirableCookie;
        Iterator<ExpirableCookie> iterator = this.domainCookies.values().iterator();
        do {
            if (!iterator.hasNext()) return -1;
            expirableCookie = iterator.next();
        } while (!expirableCookie.wrappedCookie.name().equalsIgnoreCase(string));
        return expirableCookie.wrappedCookie.value().length();
    }

    public static void lambda$printDeep$8(Key key, ExpirableCookie expirableCookie) {
        System.out.println(key + " " + expirableCookie);
    }

    public CookieJar(boolean bl) {
        this.noDomainCookies = new ConcurrentHashMap();
        this.domainCookies = new ConcurrentSkipListMap();
        this.cookieFilter = DEFAULT;
        this.obeyExpiryTimes = bl;
    }

    public static void lambda$print$7(ExpirableCookie expirableCookie) {
        System.out.println("Domain: " + expirableCookie);
    }

    public CookieStore clear() {
        this.noDomainCookies.clear();
        this.domainCookies.clear();
        return this;
    }

    public void printDeep() {
        this.domainCookies.forEach(CookieJar::lambda$printDeep$8);
    }

    public void remove(String string, String string2) {
        Key key = new Key(string2, null, string);
        if (key.domain.equals("")) {
            this.noDomainCookies.remove(key);
        } else {
            this.domainCookies.remove(key);
        }
    }

    public void removeAnyMatch(String string) {
        this.removeMatchFromIter(string, this.noDomainCookies.keys().asIterator());
        this.removeMatchFromIter(string, this.domainCookies.keySet().iterator());
    }

    public static void lambda$get$3(Boolean bl, String string, TreeMap treeMap, Cookie cookie) {
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

    public String getCookieValue(String string, String string2) {
        String string3 = this.getCookieValue(string);
        return string3 == null ? string2 : string3;
    }

    public static ConcurrentSkipListMap deepCopy(ConcurrentSkipListMap concurrentSkipListMap) {
        ConcurrentSkipListMap concurrentSkipListMap2 = new ConcurrentSkipListMap();
        concurrentSkipListMap.forEach(concurrentSkipListMap2::put);
        return concurrentSkipListMap2;
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
        Consumer<Cookie> consumer = arg_0 -> CookieJar.lambda$get$3(bl, string3, (TreeMap)object2, arg_0);
        if (!this.obeyExpiryTimes) {
            for (ExpirableCookie expirableCookie : this.noDomainCookies.values()) {
                consumer.accept(expirableCookie.wrappedCookie);
            }
        } else {
            for (Map.Entry<Key, ExpirableCookie> object3 : this.noDomainCookies.entrySet()) {
                if (object3.getValue().shouldExpire()) {
                    this.noDomainCookies.remove(object3.getKey());
                    continue;
                }
                consumer.accept(object3.getValue().wrappedCookie);
            }
        }
        Key key = new Key(string, "", "");
        String string4 = key.domain.substring(0, 1);
        Iterator iterator = this.domainCookies.tailMap((Object)new Key(string4, "", ""), true).entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = iterator.next();
            if (((Key)entry.getKey()).domain.compareTo(key.domain) > 0) {
                return ((TreeMap)object2).values();
            }
            if (!key.domain.startsWith(((Key)entry.getKey()).domain)) continue;
            if (this.obeyExpiryTimes && ((ExpirableCookie)entry.getValue()).shouldExpire()) {
                this.domainCookies.remove(entry.getKey());
                continue;
            }
            consumer.accept(((ExpirableCookie)entry.getValue()).wrappedCookie);
        }
        return ((TreeMap)object2).values();
    }

    public static void lambda$asJson$2(JsonArray jsonArray, ExpirableCookie expirableCookie) {
        jsonArray.add((Object)(expirableCookie.wrappedCookie.name() + "=" + expirableCookie.wrappedCookie.value() + "; domain=" + expirableCookie.wrappedCookie.domain() + "; path=/; Secure"));
    }

    public CookieJar(CookieJar cookieJar) {
        this.noDomainCookies = CookieJar.deepCopy(cookieJar.noDomainCookies);
        this.domainCookies = CookieJar.deepCopy(cookieJar.domainCookies);
        this.cookieFilter = cookieJar.cookieFilter;
        this.obeyExpiryTimes = cookieJar.obeyExpiryTimes;
    }

    public static ConcurrentHashMap deepCopy(ConcurrentHashMap concurrentHashMap) {
        ConcurrentHashMap concurrentHashMap2 = new ConcurrentHashMap();
        concurrentHashMap.forEach(concurrentHashMap2::put);
        return concurrentHashMap2;
    }

    public String getCookieValue(String string) {
        ExpirableCookie expirableCookie;
        Iterator<ExpirableCookie> iterator = this.domainCookies.values().iterator();
        do {
            if (!iterator.hasNext()) return null;
            expirableCookie = iterator.next();
        } while (!expirableCookie.wrappedCookie.name().equalsIgnoreCase(string));
        return expirableCookie.wrappedCookie.value();
    }

    public int size() {
        return this.noDomainCookies.size() + this.domainCookies.size();
    }

    public CookieJar() {
        this.noDomainCookies = new ConcurrentHashMap();
        this.domainCookies = new ConcurrentSkipListMap();
        this.cookieFilter = DEFAULT;
        this.obeyExpiryTimes = true;
    }

    public Predicate getCookieFilter() {
        return this.cookieFilter;
    }

    public boolean contains(Cookie cookie) {
        Objects.requireNonNull(cookie);
        return this.contains(cookie.name());
    }

    public CookieStore putFromOther(CookieJar cookieJar) {
        this.noDomainCookies.putAll(cookieJar.noDomainCookies);
        this.domainCookies.putAll(cookieJar.domainCookies);
        return this;
    }

    public static void lambda$print$6(ExpirableCookie expirableCookie) {
        System.out.println("NoDomain: " + expirableCookie);
    }

    public CookieStore put(String string, String string2) {
        return this.put(string, string2, null);
    }

    public static void lambda$asJson$1(JsonArray jsonArray, ExpirableCookie expirableCookie) {
        jsonArray.add((Object)(expirableCookie.wrappedCookie.name() + "=" + expirableCookie.wrappedCookie.value() + "; domain=" + expirableCookie.wrappedCookie.domain() + "; path=/; Secure"));
    }

    public CookieStore put(String string, String string2, String string3) {
        return this.put(string, string2, string3, "/");
    }

    public void remove(String string, String string2, String string3) {
        Key key = new Key(string3, string2, string);
        if (key.domain.equals("")) {
            this.noDomainCookies.remove(key);
        } else {
            this.domainCookies.remove(key);
        }
    }

    public void remove(String string) {
        this.remove(string, null);
    }
}
