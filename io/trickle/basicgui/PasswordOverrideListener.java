/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.basicgui;

import io.trickle.core.Controller;
import io.trickle.core.Engine;
import io.trickle.task.TaskController;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PasswordOverrideListener
implements ActionListener,
KeyListener {
    public TextField passwordField;

    @Override
    public void keyPressed(KeyEvent keyEvent) {
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
    }

    public PasswordOverrideListener(TextField textField) {
        this.passwordField = textField;
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String string = this.passwordField.getText();
        if (string.isBlank()) return;
        System.out.println("Password added -> " + string);
        ((TaskController)Engine.get().getModule(Controller.TASK)).massPassword(string);
    }
}

