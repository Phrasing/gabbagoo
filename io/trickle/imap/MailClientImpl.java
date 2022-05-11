package io.trickle.imap;

import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import java.util.Date;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.SearchTerm;

public class MailClientImpl implements MailClient {
   public Session session;
   public Date openTime;
   public Vertx vertx;
   public static Properties mailProperties = new Properties();
   public Folder inbox;
   public Store store;

   public void _close() {
      if (this.inbox != null && this.inbox.isOpen()) {
         this.inbox.close();
      }

      if (this.store != null && this.store.isConnected()) {
         this.store.close();
      }

   }

   static {
      mailProperties.setProperty("mail.store.protocol", "imaps");
   }

   public void close() {
      Context var1 = this.vertx.getOrCreateContext();
      var1.executeBlocking(this::lambda$close$2);
   }

   public Future readInbox(SearchTerm var1) {
      Context var2 = this.vertx.getOrCreateContext();
      return var2.executeBlocking(this::lambda$readInbox$0);
   }

   public SearchTerm getDefault() {
      return new MailClientImpl$1(this);
   }

   public void lambda$readInbox$0(SearchTerm var1, Promise var2) {
      try {
         Message[] var3 = this._readInbox(var1);
         var2.tryComplete(var3);
      } catch (MessagingException var4) {
         var2.tryFail(var4);
      }

   }

   public Message[] _readInbox(SearchTerm var1) {
      return this.getInbox().search((SearchTerm)Objects.requireNonNullElseGet(var1, this::getDefault), this.getReversedMessages());
   }

   public Folder getInbox() {
      if (this.inbox == null) {
         this.inbox = this.store.getFolder("Inbox");
      }

      if (!this.inbox.isOpen()) {
         this.inbox.open(1);
         this.openTime = new Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(5L));
      }

      return this.inbox;
   }

   public Future connect(String var1, String var2) {
      Objects.requireNonNull(var1, "Email can not be null");
      Objects.requireNonNull(var2, "Password can not be null");
      Context var3 = this.vertx.getOrCreateContext();
      return var3.executeBlocking(this::lambda$connect$1);
   }

   public void lambda$connect$1(String var1, String var2, Promise var3) {
      try {
         this._connect(var1, var2);
         var3.tryComplete();
      } catch (MessagingException var5) {
         var3.tryFail(var5);
      }

   }

   public CompletableFuture connectFut(String var1, String var2) {
      return this.connect(var1, var2).toCompletionStage().toCompletableFuture();
   }

   public Message[] getReversedMessages() {
      int var1 = this.inbox.getMessageCount();
      Message[] var2 = var1 > 10 ? this.inbox.getMessages(this.inbox.getMessageCount() - 10, this.inbox.getMessageCount()) : this.inbox.getMessages();

      for(int var3 = var2.length - 1; var3 < var2.length / 2; ++var3) {
         Message var4 = var2[var3];
         var2[var3] = var2[var2.length - 1 - var3];
         var2[var2.length - 1 - var3] = var4;
      }

      return var2;
   }

   public void lambda$close$2(Promise var1) {
      try {
         this._close();
      } catch (MessagingException var3) {
      }

      var1.tryComplete();
   }

   public void _connect(String var1, String var2) {
      if (this.store == null) {
         this.store = this.session.getStore("imaps");
      }

      if (!this.store.isConnected()) {
         this.store.connect("imap.gmail.com", var1, var2);
      }

   }

   public CompletableFuture readInboxFuture(SearchTerm var1) {
      return this.readInbox(var1).toCompletionStage().toCompletableFuture();
   }

   public MailClientImpl(Vertx var1) {
      this.vertx = var1;
      this.session = Session.getDefaultInstance(mailProperties, (Authenticator)null);
   }
}
