package io.trickle.task.antibot.impl.px.payload.captcha.util;

import java.time.Instant;
import java.util.Stack;

public class Timer {
   public Stack performanceList = new Stack();
   public long creationTime = Instant.now().toEpochMilli();

   public int performanceChange() {
      int var1 = (Integer)this.performanceList.pop();
      int var2 = this.performanceList.isEmpty() ? 0 : (Integer)this.performanceList.peek();
      this.performanceList.push(var1);
      return var1 - var2;
   }

   public int currentPerformance() {
      return (Integer)this.performanceList.peek();
   }

   public long gennedTimestamp() {
      return this.creationTime + (long)this.totalChange();
   }

   public int totalChange() {
      int var1 = (Integer)this.performanceList.firstElement();
      int var2 = (Integer)this.performanceList.peek();
      return var2 - var1;
   }

   public int performanceNow(int var1) {
      int var2 = this.performanceList.isEmpty() ? 0 : (Integer)this.performanceList.peek();
      return (Integer)this.performanceList.push(var2 + var1);
   }
}
