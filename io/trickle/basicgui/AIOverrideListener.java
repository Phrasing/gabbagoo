/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.basicgui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JCheckBox;

public class AIOverrideListener
implements ActionListener {
    public static AtomicBoolean AI_ENABLED = new AtomicBoolean(false);
    public JCheckBox checkBox;

    public AIOverrideListener(JCheckBox jCheckBox) {
        this.checkBox = jCheckBox;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (this.checkBox.isSelected()) {
            this.checkBox.setText("ON");
            AI_ENABLED.set(true);
            return;
        }
        this.checkBox.setText("OFF");
        AI_ENABLED.set(false);
    }
}

