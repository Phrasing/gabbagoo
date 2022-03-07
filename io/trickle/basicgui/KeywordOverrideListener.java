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

public class KeywordOverrideListener
implements ActionListener,
KeyListener {
    public TextField keywordField;

    public KeywordOverrideListener(TextField textField) {
        this.keywordField = textField;
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String string = this.keywordField.getText();
        if (string.isBlank()) return;
        System.out.println("Mass keyword changed -> " + string);
        ((TaskController)Engine.get().getModule(Controller.TASK)).massEditLinkOrKeyword(string);
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
    }
}

