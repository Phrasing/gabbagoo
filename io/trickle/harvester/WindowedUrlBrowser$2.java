package io.trickle.harvester;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;

public class WindowedUrlBrowser$2 extends KeyAdapter {
   public JTextField val$urlBar;
   public WindowedUrlBrowser this$0;

   public void keyPressed(KeyEvent var1) {
      if (var1.getKeyCode() == 10) {
         System.out.println("Navigating to " + this.val$urlBar.getText());
         this.this$0.browser.navigation().loadUrl(this.val$urlBar.getText());
      }

   }

   public WindowedUrlBrowser$2(WindowedUrlBrowser var1, JTextField var2) {
      this.this$0 = var1;
      this.val$urlBar = var2;
      super();
   }
}
