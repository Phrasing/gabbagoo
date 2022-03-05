/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.teamdev.jxbrowser.js.JsAccessible
 */
package io.trickle.harvester;

import com.teamdev.jxbrowser.js.JsAccessible;
import io.trickle.harvester.CaptchaToken;
import io.trickle.harvester.Harvester;
import io.trickle.harvester.SolveFuture;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class Harvester$SolveFunction {
    public AtomicReference<CountDownLatch> latch;
    public SolveFuture callback;

    public Harvester$SolveFunction(SolveFuture solveFuture, AtomicReference atomicReference) {
        this.callback = solveFuture;
        this.latch = atomicReference;
    }

    @JsAccessible
    public void completed(String string) {
        CaptchaToken captchaToken = this.callback.getEmptyCaptchaToken();
        if (captchaToken == null) return;
        captchaToken.setTokenValues(string);
        SolveFuture solveFuture = this.callback;
        if (solveFuture == null) return;
        if (solveFuture.isDone()) return;
        Harvester.logger.info("Received valid token [V2][CALLBACK]");
        ((CompletableFuture)solveFuture).complete(captchaToken);
        this.latch.get().countDown();
        this.callback.imageFuture.complete(null);
    }
}

