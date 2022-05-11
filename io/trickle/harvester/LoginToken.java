package io.trickle.harvester;

import io.trickle.webclient.CookieJar;

public class LoginToken {
   public Iterable cookies;
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

   public LoginToken(String var1, Iterable var2, String var3, CookieJar var4, String var5) {
      this.domain = var1;
      this.cookies = var2;
      this.proxyStr = var3;
      this.cookieJar = var4;
      this.html = var5;
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
