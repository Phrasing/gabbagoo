/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.json.JsonObject
 */
package io.trickle.util.analytics;

import io.vertx.core.json.JsonObject;

public class EmbedContainer {
    public boolean isSuccess;
    public JsonObject webhook;
    public boolean isMeta;

    public EmbedContainer(JsonObject jsonObject) {
        this.isSuccess = true;
        this.isMeta = true;
        this.webhook = jsonObject;
    }

    public EmbedContainer(boolean bl, JsonObject jsonObject) {
        this.isSuccess = bl;
        this.webhook = jsonObject;
        this.isMeta = false;
    }
}

