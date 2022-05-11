package io.trickle.webclient;

import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.DefaultCookie;
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

public class CookieJar implements CookieStore {
   public ConcurrentSkipListMap domainCookies;
   public boolean obeyExpiryTimes;
   public Predicate cookieFilter;
   public ConcurrentHashMap noDomainCookies;
   public static boolean $assertionsDisabled = !CookieJar.class.desiredAssertionStatus();
   public static Predicate DEFAULT = CookieJar::lambda$static$0;

   public void print() {
      this.noDomainCookies.values().forEach(CookieJar::lambda$print$6);
      this.domainCookies.values().forEach(CookieJar::lambda$print$7);
   }

   public void setCookieFilter(Predicate var1) {
      this.cookieFilter = var1;
   }

   public String asString() {
      if (this.domainCookies.isEmpty() && this.noDomainCookies.isEmpty()) {
         return "";
      } else {
         StringBuilder var1 = new StringBuilder();
         this.domainCookies.values().forEach(CookieJar::lambda$asString$4);
         this.noDomainCookies.values().forEach(CookieJar::lambda$asString$5);
         return var1.toString().trim();
      }
   }

   public CookieStore remove(Cookie var1) {
      CookieJar$Key var2 = new CookieJar$Key(var1.domain(), var1.path(), var1.name());
      if (var2.domain.equals("")) {
         this.noDomainCookies.remove(var2);
      } else {
         this.domainCookies.remove(var2);
      }

      return this;
   }

   public static boolean lambda$static$0(String var0) {
      return true;
   }

   public JsonArray asJson() {
      if (this.domainCookies.isEmpty() && this.noDomainCookies.isEmpty()) {
         return new JsonArray();
      } else {
         JsonArray var1 = new JsonArray();
         this.domainCookies.values().forEach(CookieJar::lambda$asJson$1);
         this.noDomainCookies.values().forEach(CookieJar::lambda$asJson$2);
         return var1;
      }
   }

   public Cookie putAndGet(String var1, String var2) {
      return this.putAndGet(var1, var2, (String)null);
   }

   public boolean contains(String var1) {
      Objects.requireNonNull(var1);
      Iterator var2 = this.domainCookies.values().iterator();

      CookieJar$ExpirableCookie var3;
      do {
         if (!var2.hasNext()) {
            var2 = this.noDomainCookies.values().iterator();

            do {
               if (!var2.hasNext()) {
                  return false;
               }

               var3 = (CookieJar$ExpirableCookie)var2.next();
            } while(!var3.wrappedCookie.name().equalsIgnoreCase(var1));

            return true;
         }

         var3 = (CookieJar$ExpirableCookie)var2.next();
      } while(!var3.wrappedCookie.name().equalsIgnoreCase(var1));

      return true;
   }

   public static void lambda$asString$5(StringBuilder var0, CookieJar$ExpirableCookie var1) {
      var0.append(var1.wrappedCookie.name()).append("=").append(var1.wrappedCookie.value()).append(";");
   }

   public CookieStore put(String var1, String var2, String var3, String var4) {
      DefaultCookie var5 = new DefaultCookie(var1, var2);
      if (var3 != null) {
         var5.setDomain(var3);
      }

      var5.setPath((String)Objects.requireNonNullElse(var4, "/"));
      return this.put((Cookie)var5);
   }

   public Cookie putAndGet(String var1, String var2, String var3) {
      DefaultCookie var4 = new DefaultCookie(var1, var2);
      if (var3 != null) {
         var4.setDomain(var3);
      }

      this.put((Cookie)var4);
      return var4;
   }

   public CookieStore put(JsonArray var1) {
      for(int var2 = 0; var2 < var1.size(); ++var2) {
         JsonObject var3 = var1.getJsonObject(var2);
         this.put(var3.getString("name"), var3.getString("value"), var3.getString("domain", (String)null), var3.getString("path", "/"));
      }

      return this;
   }

   public List getAll() {
      ArrayList var1 = new ArrayList(this.noDomainCookies.values());
      var1.addAll(this.domainCookies.values());
      return var1;
   }

   public CookieJar(Predicate var1) {
      this.noDomainCookies = new ConcurrentHashMap();
      this.domainCookies = new ConcurrentSkipListMap();
      this.cookieFilter = var1;
      this.obeyExpiryTimes = false;
   }

   public void removeMatchFromIter(String var1, Iterator var2) {
      while(var2.hasNext()) {
         try {
            CookieJar$Key var3 = (CookieJar$Key)var2.next();
            if (var3 != null && var3.name != null && !var3.name.isEmpty() && var3.name.equalsIgnoreCase(var1)) {
               var2.remove();
            }
         } catch (Throwable var4) {
         }
      }

   }

   public static void lambda$asString$4(StringBuilder var0, CookieJar$ExpirableCookie var1) {
      var0.append(var1.wrappedCookie.name()).append("=").append(var1.wrappedCookie.value()).append(";");
   }

   public CookieStore put(Cookie var1) {
      CookieJar$Key var2 = new CookieJar$Key(var1.domain(), var1.path(), var1.name());
      CookieJar$ExpirableCookie var3 = new CookieJar$ExpirableCookie(var1);
      if (this.obeyExpiryTimes && var1.maxAge() != Long.MIN_VALUE) {
         var3.setExpiry(var1.maxAge());
      }

      if (var2.domain.equals("")) {
         this.noDomainCookies.put(var2, var3);
         return this;
      } else {
         this.domainCookies.put(var2, var3);
         return this;
      }
   }

   public int getCookieValueLength(String var1) {
      Iterator var2 = this.domainCookies.values().iterator();

      CookieJar$ExpirableCookie var3;
      do {
         if (!var2.hasNext()) {
            return -1;
         }

         var3 = (CookieJar$ExpirableCookie)var2.next();
      } while(!var3.wrappedCookie.name().equalsIgnoreCase(var1));

      return var3.wrappedCookie.value().length();
   }

   public static void lambda$printDeep$8(CookieJar$Key var0, CookieJar$ExpirableCookie var1) {
      System.out.println("" + var0 + " " + var1);
   }

   public CookieJar(boolean var1) {
      this.noDomainCookies = new ConcurrentHashMap();
      this.domainCookies = new ConcurrentSkipListMap();
      this.cookieFilter = DEFAULT;
      this.obeyExpiryTimes = var1;
   }

   public static void lambda$print$7(CookieJar$ExpirableCookie var0) {
      System.out.println("Domain: " + var0);
   }

   public CookieStore clear() {
      this.noDomainCookies.clear();
      this.domainCookies.clear();
      return this;
   }

   public void printDeep() {
      this.domainCookies.forEach(CookieJar::lambda$printDeep$8);
   }

   public void remove(String var1, String var2) {
      CookieJar$Key var3 = new CookieJar$Key(var2, (String)null, var1);
      if (var3.domain.equals("")) {
         this.noDomainCookies.remove(var3);
      } else {
         this.domainCookies.remove(var3);
      }

   }

   public void removeAnyMatch(String var1) {
      this.removeMatchFromIter(var1, this.noDomainCookies.keys().asIterator());
      this.removeMatchFromIter(var1, this.domainCookies.keySet().iterator());
   }

   public static void lambda$get$3(Boolean var0, String var1, TreeMap var2, Cookie var3) {
      if (var0 == Boolean.TRUE || !var3.isSecure()) {
         if (var3.path() != null && !var1.equals(var3.path())) {
            String var4 = var3.path();
            if (!var4.endsWith("/")) {
               var4 = var4 + "/";
            }

            if (!var1.startsWith(var4)) {
               return;
            }
         }

         var2.put(var3.name(), var3);
      }
   }

   public String getCookieValue(String var1, String var2) {
      String var3 = this.getCookieValue(var1);
      return var3 == null ? var2 : var3;
   }

   public static ConcurrentSkipListMap deepCopy(ConcurrentSkipListMap var0) {
      ConcurrentSkipListMap var1 = new ConcurrentSkipListMap();
      Objects.requireNonNull(var1);
      var0.forEach(var1::put);
      return var1;
   }

   public Iterable get(Boolean var1, String var2, String var3) {
      if (!$assertionsDisabled && (var2 == null || var2.length() <= 0)) {
         throw new AssertionError();
      } else {
         String var5 = HttpUtils.removeDots(var3);
         int var6 = var5.indexOf(63);
         if (var6 > -1) {
            var5 = var5.substring(0, var6);
         }

         var6 = var5.indexOf(35);
         if (var6 > -1) {
            var5 = var5.substring(0, var6);
         }

         String var4 = var5;
         TreeMap var11 = new TreeMap();
         Consumer var12 = CookieJar::lambda$get$3;
         Iterator var7;
         if (this.obeyExpiryTimes) {
            var7 = this.noDomainCookies.entrySet().iterator();

            while(var7.hasNext()) {
               Map.Entry var8 = (Map.Entry)var7.next();
               if (((CookieJar$ExpirableCookie)var8.getValue()).shouldExpire()) {
                  this.noDomainCookies.remove(var8.getKey());
               } else {
                  var12.accept(((CookieJar$ExpirableCookie)var8.getValue()).wrappedCookie);
               }
            }
         } else {
            var7 = this.noDomainCookies.values().iterator();

            while(var7.hasNext()) {
               CookieJar$ExpirableCookie var14 = (CookieJar$ExpirableCookie)var7.next();
               var12.accept(var14.wrappedCookie);
            }
         }

         CookieJar$Key var13 = new CookieJar$Key(var2, "", "");
         String var15 = var13.domain.substring(0, 1);
         Iterator var9 = this.domainCookies.tailMap(new CookieJar$Key(var15, "", ""), true).entrySet().iterator();

         while(var9.hasNext()) {
            Map.Entry var10 = (Map.Entry)var9.next();
            if (((CookieJar$Key)var10.getKey()).domain.compareTo(var13.domain) > 0) {
               break;
            }

            if (var13.domain.startsWith(((CookieJar$Key)var10.getKey()).domain)) {
               if (this.obeyExpiryTimes && ((CookieJar$ExpirableCookie)var10.getValue()).shouldExpire()) {
                  this.domainCookies.remove(var10.getKey());
               } else {
                  var12.accept(((CookieJar$ExpirableCookie)var10.getValue()).wrappedCookie);
               }
            }
         }

         return var11.values();
      }
   }

   public static void lambda$asJson$2(JsonArray var0, CookieJar$ExpirableCookie var1) {
      String var10001 = var1.wrappedCookie.name();
      var0.add(var10001 + "=" + var1.wrappedCookie.value() + "; domain=" + var1.wrappedCookie.domain() + "; path=/; Secure");
   }

   public CookieJar(CookieJar var1) {
      this.noDomainCookies = deepCopy(var1.noDomainCookies);
      this.domainCookies = deepCopy(var1.domainCookies);
      this.cookieFilter = var1.cookieFilter;
      this.obeyExpiryTimes = var1.obeyExpiryTimes;
   }

   public static ConcurrentHashMap deepCopy(ConcurrentHashMap var0) {
      ConcurrentHashMap var1 = new ConcurrentHashMap();
      Objects.requireNonNull(var1);
      var0.forEach(var1::put);
      return var1;
   }

   public String getCookieValue(String var1) {
      Iterator var2 = this.domainCookies.values().iterator();

      CookieJar$ExpirableCookie var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (CookieJar$ExpirableCookie)var2.next();
      } while(!var3.wrappedCookie.name().equalsIgnoreCase(var1));

      return var3.wrappedCookie.value();
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

   public boolean contains(Cookie var1) {
      Objects.requireNonNull(var1);
      return this.contains(var1.name());
   }

   public CookieStore putFromOther(CookieJar var1) {
      this.noDomainCookies.putAll(var1.noDomainCookies);
      this.domainCookies.putAll(var1.domainCookies);
      return this;
   }

   public static void lambda$print$6(CookieJar$ExpirableCookie var0) {
      System.out.println("NoDomain: " + var0);
   }

   public CookieStore put(String var1, String var2) {
      return this.put(var1, var2, (String)null);
   }

   public static void lambda$asJson$1(JsonArray var0, CookieJar$ExpirableCookie var1) {
      String var10001 = var1.wrappedCookie.name();
      var0.add(var10001 + "=" + var1.wrappedCookie.value() + "; domain=" + var1.wrappedCookie.domain() + "; path=/; Secure");
   }

   public CookieStore put(String var1, String var2, String var3) {
      return this.put(var1, var2, var3, "/");
   }

   public void remove(String var1, String var2, String var3) {
      CookieJar$Key var4 = new CookieJar$Key(var3, var2, var1);
      if (var4.domain.equals("")) {
         this.noDomainCookies.remove(var4);
      } else {
         this.domainCookies.remove(var4);
      }

   }

   public void remove(String var1) {
      this.remove(var1, (String)null);
   }
}
