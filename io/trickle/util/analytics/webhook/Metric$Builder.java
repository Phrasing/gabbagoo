/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.task.sites.Site
 *  io.trickle.util.analytics.webhook.Metric
 */
package io.trickle.util.analytics.webhook;

import io.trickle.task.sites.Site;
import io.trickle.util.analytics.webhook.Metric;

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

    public Metric$Builder setAccount(String string) {
        this.account = string;
        return this;
    }

    public Metric$Builder setProxy(String string) {
        this.proxy = string;
        return this;
    }

    public Metric$Builder setMode(String string) {
        this.mode = string;
        return this;
    }

    public Metric$Builder setSite(String string) {
        this.site = string;
        return this;
    }

    public Metric$Builder setSizeQty(String string) {
        this.sizeQty = string;
        return this;
    }

    public Metric$Builder setEmail(String string) {
        this.email = string;
        return this;
    }

    public Metric$Builder setProduct(String string) {
        this.product = string;
        return this;
    }

    public Metric$Builder setSite(Site site) {
        this.site = site.toString();
        return this;
    }

    public Metric$Builder setDelays(String string) {
        this.delays = string;
        return this;
    }

    public Metric$Builder setOrderNumber(String string) {
        this.orderNo = string == null || string.isEmpty() ? "NOT_FOUND" : string;
        return this;
    }

    public Metric$Builder setSku(String string) {
        this.sku = string;
        return this;
    }

    public Metric build() {
        return new Metric(this.product, this.sku, this.sizeQty, this.delays, this.mode, this.proxy, this.email, this.site, this.account, this.orderNo);
    }
}
