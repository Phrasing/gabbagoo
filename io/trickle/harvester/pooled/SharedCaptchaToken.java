package io.trickle.harvester.pooled;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.LongAdder;

public class SharedCaptchaToken {
   public String domain;
   public AtomicBoolean expired;
   public long expiryTime = -1L;
   public static int CAPTCHA_EXPIRY_TIME = 117;
   public LongAdder harvesterPassCount;
   public String token;
   public boolean fakePassed = false;

   public void markFakePass() {
      this.fakePassed = true;
      if (this.harvesterPassCount != null) {
         this.harvesterPassCount.sumThenReset();
      }

   }

   public String getDomain() {
      return this.domain;
   }

   public long getExpiryTime() {
      return this.expiryTime;
   }

   public void markPassed() {
      if (this.harvesterPassCount != null) {
         this.harvesterPassCount.increment();
      }

   }

   public boolean isExpired() {
      if (this.expired.get()) {
         return this.expired.get();
      } else {
         return Instant.now().getEpochSecond() >= this.expiryTime;
      }
   }

   public boolean isFakePassed() {
      return this.fakePassed;
   }

   public String toString() {
      return "SharedCaptchaToken{expired=" + this.expired + ", domain='" + this.domain + "', expiryTime=" + this.expiryTime + ", token='" + this.token + "', solved=" + (this.token != null) + "'}";
   }

   public String getToken() {
      return this.token;
   }

   public void expire() {
      this.expired.set(true);
   }

   public SharedCaptchaToken(String var1) {
      this.domain = var1;
      this.expired = new AtomicBoolean(false);
   }

   public void setSolved(String var1, LongAdder var2) {
      this.expiryTime = Instant.now().getEpochSecond() + 117L;
      this.harvesterPassCount = var2;
      this.token = var1;
   }
}
