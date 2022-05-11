package io.trickle.util.analytics.webhook;

import io.trickle.task.Task;
import io.trickle.util.Storage;
import io.vertx.core.json.JsonObject;
import java.util.Arrays;

public class Metric {
   public String email;
   public String site;
   public String orderNumber;
   public String sku;
   public String account;
   public String sizeQty;
   public String proxy;
   public String mode;
   public String delays;
   public String product;

   public JsonObject asApiForm() {
      return (new JsonObject()).put("name", this.product).put("sku", this.sku).put("licenseKey", "trickle-" + Storage.ACCESS_KEY).put("site", this.site).put("size", this.sizeQty).put("ts", System.currentTimeMillis());
   }

   public static Metric create(Task var0, JsonObject var1, String var2) {
      Metric$Builder var3 = builder();
      OrderDetails var4 = (OrderDetails)OrderDetails.getDetailsParser(var0.getSite()).apply(var1);
      var3.setProduct((String)var4.productName().orElseGet(Metric::lambda$create$0));
      var3.setOrderNumber(var4.orderNumber);
      var3.setSite(var0.getSite()).setSku(var0.getKeywords()[0]).setEmail(var0.getProfile().getEmail()).setSizeQty(var0.getSize()).setMode(var0.getMode()).setDelays(String.format("%d:%d", var0.getRetryDelay(), var0.getMonitorDelay())).setProxy(var2);
      if (var0.getProfile().getAccountEmail() != null && var0.getProfile().getAccountPassword() != null) {
         String var10001 = var0.getProfile().getAccountEmail();
         var3.setAccount(var10001 + ":" + var0.getProfile().getAccountPassword());
      }

      return var3.build();
   }

   public Metric(String var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, String var9, String var10) {
      this.product = var1;
      this.sku = var2;
      this.sizeQty = var3;
      this.delays = var4;
      this.mode = var5;
      this.proxy = var6;
      this.email = var7;
      this.site = var8;
      this.account = var9;
      this.orderNumber = var10;
   }

   public static Metric$Builder builder() {
      return new Metric$Builder();
   }

   public static String lambda$create$0(Task var0) {
      return Arrays.toString(var0.getKeywords());
   }

   public String asCsvEntry() {
      String var10000 = this.site;
      String var1 = var10000 + "," + String.format("\"%s\"", this.product) + "," + this.sku + "," + this.sizeQty + "," + this.delays + "," + this.mode + "," + this.proxy + "," + this.email + "," + this.account + "," + this.orderNumber;
      return var1.trim() + "\n";
   }
}
