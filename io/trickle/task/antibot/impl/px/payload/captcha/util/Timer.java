/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.antibot.impl.px.payload.captcha.util;

import java.time.Instant;
import java.util.Stack;

public class Timer {
    public long creationTime = Instant.now().toEpochMilli();
    public Stack<Integer> performanceList = new Stack();

    public int performanceChange() {
        int n = this.performanceList.pop();
        int n2 = this.performanceList.isEmpty() ? 0 : this.performanceList.peek();
        this.performanceList.push(n);
        return n - n2;
    }

    public int totalChange() {
        int n = (Integer)this.performanceList.firstElement();
        int n2 = this.performanceList.peek();
        return n2 - n;
    }

    public long gennedTimestamp() {
        return this.creationTime + (long)this.totalChange();
    }

    public int performanceNow(int n) {
        int n2 = this.performanceList.isEmpty() ? 0 : this.performanceList.peek();
        return this.performanceList.push(n2 + n);
    }

    public int currentPerformance() {
        return this.performanceList.peek();
    }
}

