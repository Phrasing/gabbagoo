/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.task.sites.shopify.util.PaymentTokenManager$PaymentToken
 */
package io.trickle.task.sites.shopify.util;

import io.trickle.task.sites.shopify.util.PaymentTokenManager;
import java.util.LinkedList;
import java.util.Queue;

public class PaymentTokenManager {
    public Queue<PaymentToken> tokenQueue = new LinkedList<PaymentToken>();
}
