package io.trickle.harvester;

import com.teamdev.jxbrowser.engine.Engine;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class WindowedUrlBrowser$1 extends WindowAdapter {
   public WindowedUrlBrowser this$0;

   public WindowedUrlBrowser$1(WindowedUrlBrowser var1) {
      this.this$0 = var1;
      super();
   }

   public void windowClosing(WindowEvent var1) {
      if (!this.this$0.browserEngine.isClosed()) {
         Engine var10000 = this.this$0.browserEngine;
         Objects.requireNonNull(var10000);
         CompletableFuture.runAsync(var10000::close);
      }

   }
}
