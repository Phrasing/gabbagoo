/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.task.sites.walmart.usa.handling.BadSessionException
 *  io.trickle.task.sites.walmart.usa.handling.CheckoutExpiryException
 *  io.trickle.task.sites.walmart.usa.handling.EmptyCartException
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
            return CompletableFuture.failedFuture((Throwable)new EmptyCartException());
        }
        if (string.contains("contract has expired")) {
            return CompletableFuture.failedFuture((Throwable)new CheckoutExpiryException());
        }
        if (!string.contains("Method Not Allowed")) return CompletableFuture.completedFuture(UNKNOWN);
        return CompletableFuture.failedFuture((Throwable)new BadSessionException());
    }
}
