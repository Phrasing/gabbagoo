package io.trickle.basicgui;

import io.trickle.core.Controller;
import io.trickle.core.Engine;
import io.trickle.task.TaskController;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeywordOverrideListener implements ActionListener, KeyListener {
   public TextField keywordField;

   public KeywordOverrideListener(TextField var1) {
      this.keywordField = var1;
   }

   public void actionPerformed(ActionEvent var1) {
      String var2 = this.keywordField.getText();
      if (!var2.isBlank()) {
         System.out.println("Mass keyword changed -> " + var2);
         ((TaskController)Engine.get().getModule(Controller.TASK)).massEditLinkOrKeyword(var2);
      }

   }

   public void keyReleased(KeyEvent var1) {
   }

   public void keyTyped(KeyEvent var1) {
   }

   public void keyPressed(KeyEvent var1) {
   }
}
