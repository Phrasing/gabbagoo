/*
 * Decompiled with CFR 0.152.
 */
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
        if (this.harvesterPassCount == null) return;
        this.harvesterPassCount.sumThenReset();
    }

    public String getDomain() {
        return this.domain;
    }

    public long getExpiryTime() {
        return this.expiryTime;
    }

    public void markPassed() {
        if (this.harvesterPassCount == null) return;
        this.harvesterPassCount.increment();
    }

    public boolean isExpired() {
        if (!this.expired.get()) return Instant.now().getEpochSecond() >= this.expiryTime;
        return this.expired.get();
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

    public SharedCaptchaToken(String string) {
        this.domain = string;
        this.expired = new AtomicBoolean(false);
    }

    public void setSolved(String string, LongAdder longAdder) {
        this.expiryTime = Instant.now().getEpochSecond() + 117L;
        this.harvesterPassCount = longAdder;
        this.token = string;
    }
}
