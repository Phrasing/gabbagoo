/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.sites.walmart.usa.handling;

import io.trickle.task.sites.walmart.usa.handling.BadSessionException;
import io.trickle.task.sites.walmart.usa.handling.CheckoutExpiryException;
import io.trickle.task.sites.walmart.usa.handling.EmptyCartException;
import java.util.concurrent.CompletableFuture;

public enum ErrorStatus {
    UNKNOWN;


    public static CompletableFuture checkRedirectStatus(String string) {
        if (string.contains("CRT expired or empty")) {
            return CompletableFuture.failedFuture(new EmptyCartException());
        }
        if (string.contains("contract has expired")) {
            return CompletableFuture.failedFuture(new CheckoutExpiryException());
        }
        if (!string.contains("Method Not Allowed")) return CompletableFuture.completedFuture(UNKNOWN);
        return CompletableFuture.failedFuture(new BadSessionException());
    }
}

