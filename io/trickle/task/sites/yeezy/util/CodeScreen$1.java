package io.trickle.task.sites.yeezy.util;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CodeScreen$1 extends WindowAdapter {
   public CodeScreen val$newContentPane;

   public CodeScreen$1(CodeScreen var1) {
      this.val$newContentPane = var1;
      super();
   }

   public void windowActivated(WindowEvent var1) {
      this.val$newContentPane.resetFocus();
   }
}
