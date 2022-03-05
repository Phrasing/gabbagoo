/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.sites.shopify.util;

import io.trickle.task.sites.shopify.util.PaymentTokenManager$PaymentToken;
import java.util.LinkedList;
import java.util.Queue;

public class PaymentTokenManager {
    public Queue<PaymentTokenManager$PaymentToken> tokenQueue = new LinkedList<PaymentTokenManager$PaymentToken>();
}

