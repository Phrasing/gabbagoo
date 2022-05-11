package io.trickle.basicgui;

import io.trickle.core.Controller;
import io.trickle.core.Engine;
import io.trickle.core.VertxSingleton;
import io.trickle.task.TaskController;
import java.awt.TextField;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class LinkOverrideListener implements KeyListener {
   public static String CART_ADDRESS = "io.trickle.harvester.shopify.cart";
   public static String MASS_LINK_CHANGE_ADDRESS = "io.trickle.harvester.shopify.masschange";

   public void keyPressed(KeyEvent var1) {
   }

   public void keyReleased(KeyEvent var1) {
      String var2 = ((TextField)var1.getSource()).getText();
      if (var2 != null && !var2.isBlank() && var2.contains("http")) {
         System.out.println("Mass link changed -> " + var2);
         ((TaskController)Engine.get().getModule(Controller.TASK)).massEditLinkOrKeyword(var2);
         VertxSingleton.INSTANCE.get().eventBus().publish(MASS_LINK_CHANGE_ADDRESS, var2);
      }

   }

   public void keyTyped(KeyEvent var1) {
   }
}
