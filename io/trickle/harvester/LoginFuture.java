/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.harvester.LoginToken
 *  io.trickle.util.concurrent.ContextCompletableFuture
 */
package io.trickle.harvester;

import io.trickle.harvester.LoginToken;
import io.trickle.util.concurrent.ContextCompletableFuture;

public class LoginFuture
extends ContextCompletableFuture {
    public LoginToken loginToken;

    public LoginFuture(LoginToken loginToken) {
        this.loginToken = loginToken;
    }

    public LoginToken getEmptyLoginToken() {
        return this.loginToken;
    }
}
