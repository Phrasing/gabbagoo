/*
 * Decompiled with CFR 0.151.
 */
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

public class DelayOverrideListener
implements ActionListener,
KeyListener {
    public JCheckBox checkBox;
    public TextField delayField;

    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }

    public DelayOverrideListener(TextField textField, JCheckBox jCheckBox) {
        this.delayField = textField;
        this.checkBox = jCheckBox;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String string = this.delayField.getText();
        if (this.checkBox.isSelected() && !string.isBlank() && Utils.isInteger(string)) {
            this.checkBox.setText("ON");
            System.out.println("Mass delay changed -> " + this.delayField.getText());
            ((TaskController)Engine.get().getModule(Controller.TASK)).massEditDelay(Long.parseLong(string));
            return;
        }
        this.checkBox.setSelected(false);
        this.checkBox.setText("OFF");
        System.out.println("Delays set to default.");
        ((TaskController)Engine.get().getModule(Controller.TASK)).switchToDefaultDelay();
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        String string = this.delayField.getText();
        if (!this.checkBox.isSelected()) return;
        if (string.isBlank()) return;
        if (!Utils.isInteger(string)) return;
        System.out.println("Mass delay changed -> " + this.delayField.getText());
        ((TaskController)Engine.get().getModule(Controller.TASK)).massEditDelay(Long.parseLong(string));
    }
}

