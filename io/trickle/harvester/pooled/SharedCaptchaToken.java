/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.harvester.pooled;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.LongAdder;

public class SharedCaptchaToken {
    public AtomicBoolean expired;
    public long expiryTime = -1L;
    public LongAdder harvesterPassCount;
    public static int CAPTCHA_EXPIRY_TIME = 117;
    public boolean fakePassed = false;
    public String domain;
    public String token;

    public void expire() {
        this.expired.set(true);
    }

    public boolean isFakePassed() {
        return this.fakePassed;
    }

    public boolean isExpired() {
        if (this.expired.get()) {
            return this.expired.get();
        }
        if (Instant.now().getEpochSecond() < this.expiryTime) return false;
        return true;
    }

    public String toString() {
        boolean bl;
        if (this.token != null) {
            bl = true;
            return "SharedCaptchaToken{expired=" + this.expired + ", domain='" + this.domain + "', expiryTime=" + this.expiryTime + ", token='" + this.token + "', solved=" + bl + "'}";
        }
        bl = false;
        return "SharedCaptchaToken{expired=" + this.expired + ", domain='" + this.domain + "', expiryTime=" + this.expiryTime + ", token='" + this.token + "', solved=" + bl + "'}";
    }

    public String getDomain() {
        return this.domain;
    }

    public SharedCaptchaToken(String string) {
        this.domain = string;
        this.expired = new AtomicBoolean(false);
    }

    public long getExpiryTime() {
        return this.expiryTime;
    }

    public String getToken() {
        return this.token;
    }

    public void markPassed() {
        if (this.harvesterPassCount == null) return;
        this.harvesterPassCount.increment();
    }

    public void setSolved(String string, LongAdder longAdder) {
        this.expiryTime = Instant.now().getEpochSecond() + 117L;
        this.harvesterPassCount = longAdder;
        this.token = string;
    }

    public void markFakePass() {
        this.fakePassed = true;
        if (this.harvesterPassCount == null) return;
        this.harvesterPassCount.sumThenReset();
    }
}

