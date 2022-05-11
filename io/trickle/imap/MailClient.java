/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.imap.MailClientImpl
 *  io.vertx.core.Future
 *  io.vertx.core.Vertx
 *  javax.mail.search.SearchTerm
 */
package io.trickle.imap;

import io.trickle.imap.MailClientImpl;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import java.util.concurrent.CompletableFuture;
import javax.mail.search.SearchTerm;

public interface MailClient {
    public CompletableFuture connectFut(String var1, String var2);

    public Future connect(String var1, String var2);

    public CompletableFuture readInboxFuture(SearchTerm var1);

    public static MailClient create(Vertx vertx) {
        return new MailClientImpl(vertx);
    }

    public Future readInbox(SearchTerm var1);
}
