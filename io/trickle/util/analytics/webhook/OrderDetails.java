/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.json.JsonObject
 */
package io.trickle.util.analytics.webhook;

import io.trickle.task.sites.Site;
import io.trickle.util.analytics.webhook.OrderDetails$1;
import io.vertx.core.json.JsonObject;
import java.util.Optional;
import java.util.function.Function;

public class OrderDetails {
    public String cookie;
    public String orderNumber;
    public String productName;
    public static Function<JsonObject, OrderDetails> YEEZY_RESPONSE_PARSER;
    public static Function<JsonObject, OrderDetails> SHOPIFY_RESPONSE_PARSER;
    public static Function<JsonObject, OrderDetails> HIBBETT_RESPONSE_PARSER;
    public String imageURI;
    public static Function<JsonObject, OrderDetails> BESTBUY_RESPONSE_PARSER;
    public static Function<JsonObject, OrderDetails> WALMART_NEW_RESPONSE_PARSER;
    public static Function<JsonObject, OrderDetails> WALMART_RESPONSE_PARSER;

    public static OrderDetails lambda$static$5(JsonObject jsonObject) {
        String string = null;
        String string2 = null;
        String string3 = "ORDER_ID";
        try {
            string3 = jsonObject.getString("orderId", "ORDER_ID");
            JsonObject jsonObject2 = jsonObject.getJsonArray("shipmentList").getJsonObject(0).getJsonArray("productLineItemList").getJsonObject(0);
            string = jsonObject2.getString("productName", null);
            string2 = jsonObject2.getString("productImage", null);
            return new OrderDetails(string, string2, string3);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return new OrderDetails(string, string2, string3);
    }

    public OrderDetails(String string, String string2, String string3, String string4) {
        this(string, string2, string3);
        this.cookie = string4;
    }

    public static OrderDetails lambda$static$0(JsonObject jsonObject) {
        String string = null;
        String string2 = null;
        String string3 = "ORDER_ID";
        try {
            string = jsonObject.getJsonArray("items").getJsonObject(0).getString("productName", null);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        try {
            string3 = jsonObject.getJsonObject("order").getString("orderId", "ORDER_ID");
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        try {
            string2 = jsonObject.getJsonArray("items").getJsonObject(0).getString("thumbnailUrl", null);
            return new OrderDetails(string, string2, string3);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return new OrderDetails(string, string2, string3);
    }

    public static OrderDetails lambda$static$2(JsonObject jsonObject) {
        String string = null;
        String string2 = null;
        String string3 = "ORDER_ID";
        try {
            string3 = jsonObject.getString("id", "ORDER_ID");
            string = jsonObject.getJsonArray("orderItems").getJsonObject(0).getJsonObject("product").getString("name", null);
            String string4 = jsonObject.getJsonArray("orderItems").getJsonObject(0).getJsonObject("product").getJsonArray("imageIds").getString(0);
            string2 = jsonObject.getJsonArray("orderItems").getJsonObject(0).getJsonObject("product").getJsonObject("imageResources").getJsonArray(string4).getJsonObject(0).getString("url", null);
            return new OrderDetails(string, string2, string3);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return new OrderDetails(string, string2, string3);
    }

    public Optional productName() {
        return Optional.ofNullable(this.productName);
    }

    public static OrderDetails lambda$static$3(JsonObject jsonObject) {
        String string = null;
        Object var2_2 = null;
        String string2 = "CHECK EMAIL";
        try {
            string = jsonObject.getJsonArray("payments").getJsonObject(0).getJsonObject("checkout").getJsonArray("line_items").getJsonObject(0).getString("title", null);
            var2_2 = null;
            return new OrderDetails(string, var2_2, string2);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return new OrderDetails(string, var2_2, string2);
    }

    static {
        WALMART_RESPONSE_PARSER = OrderDetails::lambda$static$0;
        WALMART_NEW_RESPONSE_PARSER = OrderDetails::lambda$static$1;
        HIBBETT_RESPONSE_PARSER = OrderDetails::lambda$static$2;
        SHOPIFY_RESPONSE_PARSER = OrderDetails::lambda$static$3;
        BESTBUY_RESPONSE_PARSER = OrderDetails::lambda$static$4;
        YEEZY_RESPONSE_PARSER = OrderDetails::lambda$static$5;
    }

    public static Function getDetailsParser(Site site) {
        switch (OrderDetails$1.$SwitchMap$io$trickle$task$sites$Site[site.ordinal()]) {
            case 1: {
                return WALMART_RESPONSE_PARSER;
            }
            case 2: {
                return WALMART_NEW_RESPONSE_PARSER;
            }
            case 3: {
                return HIBBETT_RESPONSE_PARSER;
            }
            case 4: {
                return YEEZY_RESPONSE_PARSER;
            }
            case 5: {
                return BESTBUY_RESPONSE_PARSER;
            }
        }
        return SHOPIFY_RESPONSE_PARSER;
    }

    public OrderDetails(String string, String string2, String string3) {
        this.productName = string;
        this.imageURI = string2;
        this.orderNumber = string3;
    }

    public static OrderDetails lambda$static$4(JsonObject jsonObject) {
        String string = null;
        String string2 = null;
        String string3 = null;
        try {
            string = jsonObject.getJsonArray("items").getJsonObject(0).getJsonObject("meta").getString("shortLabel", null);
            string3 = jsonObject.getString("customerOrderId", "CHECK EMAIL");
            string2 = jsonObject.getJsonArray("items").getJsonObject(0).getJsonObject("meta").getString("imageUrl", null);
            return new OrderDetails(string, string2, string3);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return new OrderDetails(string, string2, string3);
    }

    public static OrderDetails lambda$static$1(JsonObject jsonObject) {
        String string = null;
        String string2 = null;
        String string3 = "ORDER_ID";
        JsonObject jsonObject2 = null;
        try {
            jsonObject2 = jsonObject.getJsonObject("data").getJsonObject("placeOrder", new JsonObject());
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        if (jsonObject2 == null) return new OrderDetails(string, string2, string3);
        try {
            JsonObject jsonObject3 = jsonObject2.getJsonArray("lineItems").getJsonObject(0).getJsonObject("product");
            string = jsonObject3.getString("name", null);
            string2 = jsonObject3.getJsonObject("imageInfo").getString("thumbnailUrl", null);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        try {
            string3 = jsonObject2.getJsonObject("order").getString("id", "ORDER_ID");
            return new OrderDetails(string, string2, string3);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return new OrderDetails(string, string2, string3);
    }

    public static OrderDetails getDefault() {
        return new OrderDetails(null, null, null);
    }
}

