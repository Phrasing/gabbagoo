package io.trickle.basicgui;

import io.trickle.core.Controller;
import io.trickle.core.Engine;
import io.trickle.task.TaskController;
import io.trickle.util.Utils;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JCheckBox;

public class DelayOverrideListener implements ActionListener, KeyListener {
   public JCheckBox checkBox;
   public TextField delayField;

   public void keyPressed(KeyEvent var1) {
   }

   public void actionPerformed(ActionEvent var1) {
      String var2 = this.delayField.getText();
      if (this.checkBox.isSelected() && !var2.isBlank() && Utils.isInteger(var2)) {
         this.checkBox.setText("ON");
         System.out.println("Mass delay changed -> " + this.delayField.getText());
         ((TaskController)Engine.get().getModule(Controller.TASK)).massEditDelay(Long.parseLong(var2));
      } else {
         this.checkBox.setSelected(false);
         this.checkBox.setText("OFF");
         System.out.println("Delays set to default.");
         ((TaskController)Engine.get().getModule(Controller.TASK)).switchToDefaultDelay();
      }

   }

   public DelayOverrideListener(TextField var1, JCheckBox var2) {
      this.delayField = var1;
      this.checkBox = var2;
   }

   public void keyReleased(KeyEvent var1) {
      String var2 = this.delayField.getText();
      if (this.checkBox.isSelected() && !var2.isBlank() && Utils.isInteger(var2)) {
         System.out.println("Mass delay changed -> " + this.delayField.getText());
         ((TaskController)Engine.get().getModule(Controller.TASK)).massEditDelay(Long.parseLong(var2));
      }

   }

   public void keyTyped(KeyEvent var1) {
   }
}
