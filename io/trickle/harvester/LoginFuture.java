package io.trickle.harvester;

import io.trickle.util.concurrent.ContextCompletableFuture;

public class LoginFuture extends ContextCompletableFuture {
   public LoginToken loginToken;

   public LoginFuture(LoginToken var1) {
      this.loginToken = var1;
   }

   public LoginToken getEmptyLoginToken() {
      return this.loginToken;
   }
}
