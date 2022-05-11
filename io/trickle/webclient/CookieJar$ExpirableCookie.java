package io.trickle.webclient;

import io.netty.handler.codec.http.cookie.Cookie;

public class CookieJar$ExpirableCookie implements Comparable {
   public Cookie wrappedCookie;
   public long expiryTime;

   public int compareTo(Object var1) {
      return this.compareTo((Cookie)var1);
   }

   public void setExpiry(long var1) {
      if (this.expiryTime == 0L) {
         this.expiryTime = getEpoch() + var1;
      }

   }

   public String toString() {
      return "ExpirableCookie{expiryTime=" + this.expiryTime + ", wrappedCookie=" + this.wrappedCookie + "}";
   }

   public int compareTo(Cookie var1) {
      return this.wrappedCookie.compareTo(var1);
   }

   public static long getEpoch() {
      return System.currentTimeMillis() / 1000L;
   }

   public boolean shouldExpire() {
      if (this.expiryTime == 0L) {
         return false;
      } else {
         return getEpoch() >= this.expiryTime;
      }
   }

   public CookieJar$ExpirableCookie(Cookie var1) {
      this.wrappedCookie = var1;
   }

   public Cookie getWrappedCookie() {
      return this.wrappedCookie;
   }
}
