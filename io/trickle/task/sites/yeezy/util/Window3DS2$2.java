package io.trickle.task.sites.yeezy.util;

import com.teamdev.jxbrowser.engine.Engine;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class Window3DS2$2 extends WindowAdapter {
   public Window3DS2 this$0;
   public Engine val$engine;

   public Window3DS2$2(Window3DS2 var1, Engine var2) {
      this.this$0 = var1;
      this.val$engine = var2;
      super();
   }

   public void windowClosing(WindowEvent var1) {
      try {
         if (!this.val$engine.isClosed()) {
            Engine var10000 = this.val$engine;
            Objects.requireNonNull(var10000);
            CompletableFuture.runAsync(var10000::close);
         }
      } catch (Throwable var3) {
      }

   }
}
