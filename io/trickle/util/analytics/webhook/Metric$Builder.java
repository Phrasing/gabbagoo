package io.trickle.util.analytics.webhook;

import io.trickle.task.sites.Site;

public class Metric$Builder {
   public String sku;
   public String delays;
   public String mode;
   public String email;
   public String account = "none";
   public String product;
   public String site;
   public String sizeQty;
   public String proxy;
   public String orderNo;

   public Metric$Builder setAccount(String var1) {
      this.account = var1;
      return this;
   }

   public Metric$Builder setProxy(String var1) {
      this.proxy = var1;
      return this;
   }

   public Metric$Builder setMode(String var1) {
      this.mode = var1;
      return this;
   }

   public Metric$Builder setSite(String var1) {
      this.site = var1;
      return this;
   }

   public Metric$Builder setSizeQty(String var1) {
      this.sizeQty = var1;
      return this;
   }

   public Metric$Builder setEmail(String var1) {
      this.email = var1;
      return this;
   }

   public Metric$Builder setProduct(String var1) {
      this.product = var1;
      return this;
   }

   public Metric$Builder setSite(Site var1) {
      this.site = var1.toString();
      return this;
   }

   public Metric$Builder setDelays(String var1) {
      this.delays = var1;
      return this;
   }

   public Metric$Builder setOrderNumber(String var1) {
      if (var1 != null && !var1.isEmpty()) {
         this.orderNo = var1;
      } else {
         this.orderNo = "NOT_FOUND";
      }

      return this;
   }

   public Metric$Builder setSku(String var1) {
      this.sku = var1;
      return this;
   }

   public Metric build() {
      return new Metric(this.product, this.sku, this.sizeQty, this.delays, this.mode, this.proxy, this.email, this.site, this.account, this.orderNo);
   }
}
