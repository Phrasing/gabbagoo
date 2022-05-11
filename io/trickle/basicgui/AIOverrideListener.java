/*
 * Decompiled with CFR 0.152.
 */
package io.trickle.basicgui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JCheckBox;

public class AIOverrideListener
implements ActionListener {
    public JCheckBox checkBox;
    public static AtomicBoolean AI_ENABLED = new AtomicBoolean(false);

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (this.checkBox.isSelected()) {
            this.checkBox.setText("ON");
            AI_ENABLED.set(true);
        } else {
            this.checkBox.setText("OFF");
            AI_ENABLED.set(false);
        }
    }

    public AIOverrideListener(JCheckBox jCheckBox) {
        this.checkBox = jCheckBox;
    }
}
