package io.trickle.util;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

public class RunClock {
   public long timeTillRun;
   public boolean stopped;
   public long finishTime;

   public long getTimeTillRun() {
      return this.timeTillRun;
   }

   public RunClock() {
      LocalTime var1 = LocalTime.now();
      int var2 = var1.getMinute();
      if (var2 != 45 && var2 != 15) {
         if (var2 > 45) {
            this.timeTillRun = TimeUnit.MINUTES.toMillis((long)(74 - var2));
         } else if (var2 < 15) {
            this.timeTillRun = TimeUnit.MINUTES.toMillis((long)(14 - var2));
         } else {
            this.timeTillRun = TimeUnit.MINUTES.toMillis((long)(44 - var2));
         }
      } else {
         this.timeTillRun = 2L;
      }

      this.stopped = true;
      this.finishTime = 0L;
   }

   public boolean isStopped() {
      if (!this.stopped && System.currentTimeMillis() >= this.finishTime) {
         this.stopped = true;
      }

      return this.stopped;
   }

   public void start() {
      this.finishTime = System.currentTimeMillis() + 300000L;
      this.stopped = false;
   }

   public static RunClock create() {
      return new RunClock();
   }
}
