/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.util.analytics.webhook;

import io.trickle.task.sites.Site;
import io.trickle.util.analytics.webhook.Metric;

public class Metric$Builder {
    public String sku;
    public String sizeQty;
    public String email;
    public String orderNo;
    public String account = "none";
    public String site;
    public String proxy;
    public String product;
    public String mode;
    public String delays;

    public Metric$Builder setMode(String string) {
        this.mode = string;
        return this;
    }

    public Metric$Builder setProxy(String string) {
        this.proxy = string;
        return this;
    }

    public Metric build() {
        return new Metric(this.product, this.sku, this.sizeQty, this.delays, this.mode, this.proxy, this.email, this.site, this.account, this.orderNo);
    }

    public Metric$Builder setSku(String string) {
        this.sku = string;
        return this;
    }

    public Metric$Builder setAccount(String string) {
        this.account = string;
        return this;
    }

    public Metric$Builder setOrderNumber(String string) {
        if (string != null && !string.isEmpty()) {
            this.orderNo = string;
            return this;
        }
        this.orderNo = "NOT_FOUND";
        return this;
    }

    public Metric$Builder setProduct(String string) {
        this.product = string;
        return this;
    }

    public Metric$Builder setSizeQty(String string) {
        this.sizeQty = string;
        return this;
    }

    public Metric$Builder setDelays(String string) {
        this.delays = string;
        return this;
    }

    public Metric$Builder setEmail(String string) {
        this.email = string;
        return this;
    }

    public Metric$Builder setSite(String string) {
        this.site = string;
        return this;
    }

    public Metric$Builder setSite(Site site) {
        this.site = site.toString();
        return this;
    }
}

