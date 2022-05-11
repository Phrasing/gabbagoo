/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.harvester.CaptchaToken
 *  io.trickle.util.Pair
 *  io.trickle.util.concurrent.ContextCompletableFuture
 *  io.vertx.core.json.JsonArray
 */
package io.trickle.harvester;

import io.trickle.harvester.CaptchaToken;
import io.trickle.util.Pair;
import io.trickle.util.concurrent.ContextCompletableFuture;
import io.vertx.core.json.JsonArray;
import java.util.concurrent.CompletableFuture;

public class SolveFuture
extends ContextCompletableFuture {
    public CompletableFuture<JsonArray> clickFuture;
    public CompletableFuture<Pair<String[], String>> imageFuture = new CompletableFuture();
    public CaptchaToken captchaToken;

    public CaptchaToken getEmptyCaptchaToken() {
        return this.captchaToken;
    }

    public SolveFuture(CaptchaToken captchaToken) {
        this.clickFuture = new CompletableFuture();
        this.captchaToken = captchaToken;
    }
}
