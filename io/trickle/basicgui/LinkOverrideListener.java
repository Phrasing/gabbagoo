/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.basicgui;

import io.trickle.core.Controller;
import io.trickle.core.Engine;
import io.trickle.task.TaskController;
import java.awt.TextField;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class LinkOverrideListener
implements KeyListener {
    @Override
    public void keyReleased(KeyEvent keyEvent) {
        String string = ((TextField)keyEvent.getSource()).getText();
        if (string.isBlank()) return;
        if (!string.contains("http")) return;
        System.out.println("Mass link changed -> " + string);
        ((TaskController)Engine.get().getModule(Controller.TASK)).massEditLinkOrKeyword(string);
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
    }
}

