package io.trickle.basicgui;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class BasicGUI$1 extends WindowAdapter {
   public Frame val$f;
   public BasicGUI this$0;

   public BasicGUI$1(BasicGUI var1, Frame var2) {
      this.this$0 = var1;
      this.val$f = var2;
      super();
   }

   public void windowClosing(WindowEvent var1) {
      this.this$0.closed = true;
      this.val$f.dispose();
   }
}
