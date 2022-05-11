package io.trickle.basicgui;

import io.trickle.core.Controller;
import io.trickle.core.Engine;
import io.trickle.task.TaskController;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PasswordOverrideListener implements ActionListener, KeyListener {
   public TextField passwordField;

   public void keyPressed(KeyEvent var1) {
   }

   public PasswordOverrideListener(TextField var1) {
      this.passwordField = var1;
   }

   public void keyReleased(KeyEvent var1) {
   }

   public void keyTyped(KeyEvent var1) {
   }

   public void actionPerformed(ActionEvent var1) {
      String var2 = this.passwordField.getText();
      if (!var2.isBlank()) {
         System.out.println("Password added -> " + var2);
         ((TaskController)Engine.get().getModule(Controller.TASK)).massPassword(var2);
      }

   }
}
