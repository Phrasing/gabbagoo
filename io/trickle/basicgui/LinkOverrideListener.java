/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.core.Controller
 *  io.trickle.core.Engine
 *  io.trickle.core.VertxSingleton
 *  io.trickle.task.TaskController
 */
package io.trickle.basicgui;

import io.trickle.core.Controller;
import io.trickle.core.Engine;
import io.trickle.core.VertxSingleton;
import io.trickle.task.TaskController;
import java.awt.TextField;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class LinkOverrideListener
implements KeyListener {
    public static String CART_ADDRESS;
    public static String MASS_LINK_CHANGE_ADDRESS;

    @Override
    public void keyPressed(KeyEvent keyEvent) {
    }

    static {
        MASS_LINK_CHANGE_ADDRESS = "io.trickle.harvester.shopify.masschange";
        CART_ADDRESS = "io.trickle.harvester.shopify.cart";
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        String string = ((TextField)keyEvent.getSource()).getText();
        if (string == null) return;
        if (string.isBlank()) return;
        if (!string.contains("http")) return;
        System.out.println("Mass link changed -> " + string);
        ((TaskController)Engine.get().getModule(Controller.TASK)).massEditLinkOrKeyword(string);
        VertxSingleton.INSTANCE.get().eventBus().publish(MASS_LINK_CHANGE_ADDRESS, (Object)string);
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }
}
