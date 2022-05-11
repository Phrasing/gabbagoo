package io.trickle.util.analytics.cli;

import io.netty.util.internal.PlatformDependent;
import io.trickle.util.analytics.Analytics;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;

public class CLIMonitor extends AbstractVerticle {
   public long timerID;

   public static void lambda$run$0(Throwable var0) {
   }

   public void stop() {
      super.vertx.cancelTimer(this.timerID);
   }

   public String version() {
      return String.format("%d.%d.%d", 1, 0, 278);
   }

   public void start() {
      this.timerID = super.vertx.setPeriodic(4500L, this::run);
      this.run(-1L);
   }

   public void run(long var1) {
      long var3 = Analytics.carts.sum();
      long var5 = Analytics.success.sum();
      long var7 = Analytics.fails.sum();
      long var9 = Analytics.queuePasses.sum();
      long var11 = Analytics.fakePasses.sum();
      String var13;
      if (var9 == 0L && var11 == 0L) {
         var13 = String.format("Trickle %s || Carts %d || Checkouts %d || Fails %d", this.version(), var3, var5, var7);
      } else if (var9 == 0L) {
         var13 = String.format("Trickle %s || Carts %d || Checkouts %d || Fails %d || Fake Passes %d", this.version(), var3, var5, var7, var11);
      } else if (var11 == 0L) {
         var13 = String.format("Trickle %s || Carts %d || Checkouts %d || Fails %d || Queue Passes %d", this.version(), var3, var5, var7, var9);
      } else {
         var13 = String.format("Trickle %s || Carts %d || Checkouts %d || Fails %d || Queue Passes %d || Fake Passes %d", this.version(), var3, var5, var7, var9, var11);
      }

      super.vertx.fileSystem().writeFile(PlatformDependent.tmpdir().getPath() + "/.jp/state.lib", Buffer.buffer(var13)).onFailure(CLIMonitor::lambda$run$0);
   }
}
