/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.harvester.WindowedUrlBrowser
 */
package io.trickle.harvester;

import io.trickle.harvester.WindowedUrlBrowser;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;

public class WindowedUrlBrowser$2
extends KeyAdapter {
    public JTextField val$urlBar;
    public WindowedUrlBrowser this$0;

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() != 10) return;
        System.out.println("Navigating to " + this.val$urlBar.getText());
        this.this$0.browser.navigation().loadUrl(this.val$urlBar.getText());
    }

    public WindowedUrlBrowser$2(WindowedUrlBrowser windowedUrlBrowser, JTextField jTextField) {
        this.this$0 = windowedUrlBrowser;
        this.val$urlBar = jTextField;
    }
}
