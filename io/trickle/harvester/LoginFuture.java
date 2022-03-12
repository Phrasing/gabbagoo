/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.harvester;

import io.trickle.harvester.LoginToken;
import io.trickle.util.concurrent.ContextCompletableFuture;

public class LoginFuture
extends ContextCompletableFuture {
    public LoginToken loginToken;

    public LoginToken getEmptyLoginToken() {
        return this.loginToken;
    }

    public LoginFuture(LoginToken loginToken) {
        this.loginToken = loginToken;
    }
}

