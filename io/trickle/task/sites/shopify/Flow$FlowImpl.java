package io.trickle.task.sites.shopify;

import java.util.Map;

public class Flow$FlowImpl implements Flow$Actions {
   public boolean needsPrice;
   public Map requiredCookies;
   public boolean needsAuthTokens;

   public boolean needsPrice() {
      return this.needsPrice;
   }

   public boolean needsAuthTokens() {
      return this.needsAuthTokens;
   }

   public Map requiredCookies() {
      return this.requiredCookies;
   }

   public Flow$FlowImpl(boolean var1, boolean var2, Map var3) {
      this.needsPrice = var1;
      this.needsAuthTokens = var2;
      this.requiredCookies = var3;
   }
}
