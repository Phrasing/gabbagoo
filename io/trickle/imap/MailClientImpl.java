/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.Context
 *  io.vertx.core.Future
 *  io.vertx.core.Promise
 *  io.vertx.core.Vertx
 *  javax.mail.Folder
 *  javax.mail.Message
 *  javax.mail.MessagingException
 *  javax.mail.Session
 *  javax.mail.Store
 *  javax.mail.search.SearchTerm
 */
package io.trickle.imap;

import io.trickle.imap.MailClient;
import io.trickle.imap.MailClientImpl$1;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import java.util.Date;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.SearchTerm;

public class MailClientImpl
implements MailClient {
    public Folder inbox;
    public Session session;
    public static Properties mailProperties = new Properties();
    public Vertx vertx;
    public Date openTime;
    public Store store;

    @Override
    public Future readInbox(SearchTerm searchTerm) {
        Context context = this.vertx.getOrCreateContext();
        return context.executeBlocking(arg_0 -> this.lambda$readInbox$0(searchTerm, arg_0));
    }

    public void _close() {
        if (this.inbox != null && this.inbox.isOpen()) {
            this.inbox.close();
        }
        if (this.store == null) return;
        if (!this.store.isConnected()) return;
        this.store.close();
    }

    @Override
    public CompletableFuture connectFut(String string, String string2) {
        return this.connect(string, string2).toCompletionStage().toCompletableFuture();
    }

    @Override
    public CompletableFuture readInboxFuture(SearchTerm searchTerm) {
        return this.readInbox(searchTerm).toCompletionStage().toCompletableFuture();
    }

    public Message[] _readInbox(SearchTerm searchTerm) {
        return this.getInbox().search(Objects.requireNonNullElseGet(searchTerm, this::getDefault), this.getReversedMessages());
    }

    public SearchTerm getDefault() {
        return new MailClientImpl$1(this);
    }

    @Override
    public Future connect(String string, String string2) {
        Objects.requireNonNull(string, "Email can not be null");
        Objects.requireNonNull(string2, "Password can not be null");
        Context context = this.vertx.getOrCreateContext();
        return context.executeBlocking(arg_0 -> this.lambda$connect$1(string, string2, arg_0));
    }

    public void _connect(String string, String string2) {
        if (this.store == null) {
            this.store = this.session.getStore("imaps");
        }
        if (this.store.isConnected()) return;
        this.store.connect("imap.gmail.com", string, string2);
    }

    public MailClientImpl(Vertx vertx) {
        this.vertx = vertx;
        this.session = Session.getDefaultInstance((Properties)mailProperties, null);
    }

    public Folder getInbox() {
        if (this.inbox == null) {
            this.inbox = this.store.getFolder("Inbox");
        }
        if (this.inbox.isOpen()) return this.inbox;
        this.inbox.open(1);
        this.openTime = new Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(5L));
        return this.inbox;
    }

    public void lambda$readInbox$0(SearchTerm searchTerm, Promise promise) {
        try {
            Message[] messageArray = this._readInbox(searchTerm);
            promise.tryComplete((Object)messageArray);
            return;
        }
        catch (MessagingException messagingException) {
            promise.tryFail((Throwable)messagingException);
        }
    }

    public void close() {
        Context context = this.vertx.getOrCreateContext();
        context.executeBlocking(this::lambda$close$2);
    }

    static {
        mailProperties.setProperty("mail.store.protocol", "imaps");
    }

    public Message[] getReversedMessages() {
        int n = this.inbox.getMessageCount();
        Message[] messageArray = n > 10 ? this.inbox.getMessages(this.inbox.getMessageCount() - 10, this.inbox.getMessageCount()) : this.inbox.getMessages();
        int n2 = messageArray.length - 1;
        while (n2 < messageArray.length / 2) {
            Message message = messageArray[n2];
            messageArray[n2] = messageArray[messageArray.length - 1 - n2];
            messageArray[messageArray.length - 1 - n2] = message;
            ++n2;
        }
        return messageArray;
    }

    public void lambda$connect$1(String string, String string2, Promise promise) {
        try {
            this._connect(string, string2);
            promise.tryComplete();
            return;
        }
        catch (MessagingException messagingException) {
            promise.tryFail((Throwable)messagingException);
        }
    }

    public void lambda$close$2(Promise promise) {
        try {
            this._close();
        }
        catch (MessagingException messagingException) {
            // empty catch block
        }
        promise.tryComplete();
    }
}

