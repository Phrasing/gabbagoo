package io.trickle.imap;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import java.util.concurrent.CompletableFuture;
import javax.mail.search.SearchTerm;

public interface MailClient {
   CompletableFuture connectFut(String var1, String var2);

   Future connect(String var1, String var2);

   CompletableFuture readInboxFuture(SearchTerm var1);

   static MailClient create(Vertx var0) {
      return new MailClientImpl(var0);
   }

   Future readInbox(SearchTerm var1);
}
