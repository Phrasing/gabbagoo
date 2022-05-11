package io.trickle.account;

import net.openhft.hashing.LongHashFunction;

public class Account {
   public String cacheKey;
   public String sessionString;
   public String user;
   public String site;
   public String pass;

   public String sessionCacheKey() {
      if (this.cacheKey == null) {
         LongHashFunction var10001 = LongHashFunction.wy_3();
         String var10002 = this.getUser();
         this.cacheKey = String.valueOf(var10001.hashChars(var10002 + this.getSite()));
      }

      return this.cacheKey;
   }

   public String toString() {
      return this.user + ":" + this.pass;
   }

   public String getPass() {
      return this.pass;
   }

   public String lookupSession() {
      try {
         return AccountController.ACCOUNT_STORE.get(this.sessionCacheKey(), "");
      } catch (Throwable var2) {
         var2.printStackTrace();
         return "";
      }
   }

   public void setSite(String var1) {
      this.site = var1;
   }

   public String getSessionString() {
      return this.sessionString;
   }

   public String getSite() {
      return this.site;
   }

   public String getUser() {
      return this.user;
   }

   public Account(String var1, String var2) {
      this.user = var1;
      this.pass = var2;
   }

   public Account(String var1, String var2, String var3) {
      this.user = var1;
      this.pass = var2;
      this.site = var3;
   }

   public static Account fromArray(String[] var0) {
      return var0.length == 2 ? new Account(var0[0], var0[1]) : null;
   }

   public static Account fromString(String var0) {
      return fromArray(var0.split(":"));
   }

   public void setSessionString(String var1) {
      this.sessionString = var1;
   }
}
