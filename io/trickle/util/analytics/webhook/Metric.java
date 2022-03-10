/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.json.JsonObject
 */
package io.trickle.util.analytics.webhook;

import io.trickle.task.Task;
import io.trickle.util.Storage;
import io.trickle.util.analytics.webhook.Metric$Builder;
import io.trickle.util.analytics.webhook.OrderDetails;
import io.vertx.core.json.JsonObject;
import java.util.Arrays;

public class Metric {
    public String proxy;
    public String sizeQty;
    public String sku;
    public String orderNumber;
    public String email;
    public String account;
    public String mode;
    public String site;
    public String product;
    public String delays;

    public Metric(String string, String string2, String string3, String string4, String string5, String string6, String string7, String string8, String string9, String string10) {
        this.product = string;
        this.sku = string2;
        this.sizeQty = string3;
        this.delays = string4;
        this.mode = string5;
        this.proxy = string6;
        this.email = string7;
        this.site = string8;
        this.account = string9;
        this.orderNumber = string10;
    }

    public JsonObject asApiForm() {
        return new JsonObject().put("name", (Object)this.product).put("sku", (Object)this.sku).put("licenseKey", (Object)("trickle-" + Storage.ACCESS_KEY)).put("site", (Object)this.site).put("size", (Object)this.sizeQty).put("ts", (Object)System.currentTimeMillis());
    }

    public static String lambda$create$0(Task task) {
        return Arrays.toString(task.getKeywords());
    }

    public String asCsvEntry() {
        String string = this.site + "," + String.format("\"%s\"", this.product) + "," + this.sku + "," + this.sizeQty + "," + this.delays + "," + this.mode + "," + this.proxy + "," + this.email + "," + this.account + "," + this.orderNumber;
        return string.trim() + "\n";
    }

    public static Metric$Builder builder() {
        return new Metric$Builder();
    }

    public static Metric create(Task task, JsonObject jsonObject, String string) {
        Metric$Builder metric$Builder = Metric.builder();
        OrderDetails orderDetails = (OrderDetails)OrderDetails.getDetailsParser(task.getSite()).apply(jsonObject);
        metric$Builder.setProduct(orderDetails.productName().orElseGet(() -> Metric.lambda$create$0(task)));
        metric$Builder.setOrderNumber(orderDetails.orderNumber);
        metric$Builder.setSite(task.getSite()).setSku(task.getKeywords()[0]).setEmail(task.getProfile().getEmail()).setSizeQty(task.getSize()).setMode(task.getMode()).setDelays(String.format("%d:%d", task.getRetryDelay(), task.getMonitorDelay())).setProxy(string);
        if (task.getProfile().getAccountEmail() == null) return metric$Builder.build();
        if (task.getProfile().getAccountPassword() == null) return metric$Builder.build();
        metric$Builder.setAccount(task.getProfile().getAccountEmail() + ":" + task.getProfile().getAccountPassword());
        return metric$Builder.build();
    }
}

