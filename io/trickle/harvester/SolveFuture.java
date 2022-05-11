package io.trickle.harvester;

import io.trickle.util.concurrent.ContextCompletableFuture;
import java.util.concurrent.CompletableFuture;

public class SolveFuture extends ContextCompletableFuture {
   public CompletableFuture clickFuture = new CompletableFuture();
   public CompletableFuture imageFuture = new CompletableFuture();
   public CaptchaToken captchaToken;

   public CaptchaToken getEmptyCaptchaToken() {
      return this.captchaToken;
   }

   public SolveFuture(CaptchaToken var1) {
      this.captchaToken = var1;
   }
}
