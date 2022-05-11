package io.trickle.util.analytics.webhook;

import io.trickle.task.sites.Site;
import io.vertx.core.json.JsonObject;
import java.util.Optional;
import java.util.function.Function;

public class OrderDetails {
   public static Function SHOPIFY_RESPONSE_PARSER = OrderDetails::lambda$static$3;
   public static Function WALMART_RESPONSE_PARSER = OrderDetails::lambda$static$0;
   public String imageURI;
   public String orderNumber;
   public String cookie;
   public static Function YEEZY_RESPONSE_PARSER = OrderDetails::lambda$static$5;
   public static Function BESTBUY_RESPONSE_PARSER = OrderDetails::lambda$static$4;
   public String productName;
   public static Function HIBBETT_RESPONSE_PARSER = OrderDetails::lambda$static$2;
   public static Function WALMART_NEW_RESPONSE_PARSER = OrderDetails::lambda$static$1;

   public static OrderDetails lambda$static$1(JsonObject var0) {
      String var1 = null;
      String var2 = null;
      String var3 = "ORDER_ID";
      JsonObject var4 = null;

      try {
         var4 = var0.getJsonObject("data").getJsonObject("placeOrder", new JsonObject());
      } catch (Throwable var8) {
      }

      if (var4 != null) {
         try {
            JsonObject var5 = var4.getJsonArray("lineItems").getJsonObject(0).getJsonObject("product");
            var1 = var5.getString("name", (String)null);
            var2 = var5.getJsonObject("imageInfo").getString("thumbnailUrl", (String)null);
         } catch (Throwable var7) {
         }

         try {
            var3 = var4.getJsonObject("order").getString("id", "ORDER_ID");
         } catch (Throwable var6) {
         }
      }

      return new OrderDetails(var1, var2, var3);
   }

   public static OrderDetails lambda$static$3(JsonObject var0) {
      String var1 = null;
      Object var2 = null;
      String var3 = "CHECK EMAIL";

      try {
         var1 = var0.getJsonArray("payments").getJsonObject(0).getJsonObject("checkout").getJsonArray("line_items").getJsonObject(0).getString("title", (String)null);
         var2 = null;
      } catch (Throwable var5) {
      }

      return new OrderDetails(var1, (String)var2, var3);
   }

   public static OrderDetails lambda$static$0(JsonObject var0) {
      String var1 = null;
      String var2 = null;
      String var3 = "ORDER_ID";

      try {
         var1 = var0.getJsonArray("items").getJsonObject(0).getString("productName", (String)null);
      } catch (Throwable var7) {
      }

      try {
         var3 = var0.getJsonObject("order").getString("orderId", "ORDER_ID");
      } catch (Throwable var6) {
      }

      try {
         var2 = var0.getJsonArray("items").getJsonObject(0).getString("thumbnailUrl", (String)null);
      } catch (Throwable var5) {
      }

      return new OrderDetails(var1, var2, var3);
   }

   public static OrderDetails lambda$static$2(JsonObject var0) {
      String var1 = null;
      String var2 = null;
      String var3 = "ORDER_ID";

      try {
         var3 = var0.getString("id", "ORDER_ID");
         var1 = var0.getJsonArray("orderItems").getJsonObject(0).getJsonObject("product").getString("name", (String)null);
         String var4 = var0.getJsonArray("orderItems").getJsonObject(0).getJsonObject("product").getJsonArray("imageIds").getString(0);
         var2 = var0.getJsonArray("orderItems").getJsonObject(0).getJsonObject("product").getJsonObject("imageResources").getJsonArray(var4).getJsonObject(0).getString("url", (String)null);
      } catch (Throwable var5) {
      }

      return new OrderDetails(var1, var2, var3);
   }

   public static Function getDetailsParser(Site var0) {
      // $FF: Couldn't be decompiled
   }

   public static OrderDetails lambda$static$4(JsonObject var0) {
      String var1 = null;
      String var2 = null;
      String var3 = null;

      try {
         var1 = var0.getJsonArray("items").getJsonObject(0).getJsonObject("meta").getString("shortLabel", (String)null);
         var3 = var0.getString("customerOrderId", "CHECK EMAIL");
         var2 = var0.getJsonArray("items").getJsonObject(0).getJsonObject("meta").getString("imageUrl", (String)null);
      } catch (Throwable var5) {
      }

      return new OrderDetails(var1, var2, var3);
   }

   public OrderDetails(String var1, String var2, String var3, String var4) {
      this(var1, var2, var3);
      this.cookie = var4;
   }

   public static OrderDetails lambda$static$5(JsonObject var0) {
      String var1 = null;
      String var2 = null;
      String var3 = "ORDER_ID";

      try {
         var3 = var0.getString("orderId", "ORDER_ID");
         JsonObject var4 = var0.getJsonArray("shipmentList").getJsonObject(0).getJsonArray("productLineItemList").getJsonObject(0);
         var1 = var4.getString("productName", (String)null);
         var2 = var4.getString("productImage", (String)null);
      } catch (Throwable var5) {
      }

      return new OrderDetails(var1, var2, var3);
   }

   public OrderDetails(String var1, String var2, String var3) {
      this.productName = var1;
      this.imageURI = var2;
      this.orderNumber = var3;
   }

   public Optional productName() {
      return Optional.ofNullable(this.productName);
   }

   public static OrderDetails getDefault() {
      return new OrderDetails((String)null, (String)null, (String)null);
   }
}
