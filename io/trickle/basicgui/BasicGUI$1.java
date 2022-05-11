/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.basicgui.BasicGUI
 */
package io.trickle.basicgui;

import io.trickle.basicgui.BasicGUI;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class BasicGUI$1
extends WindowAdapter {
    public Frame val$f;
    public BasicGUI this$0;

    public BasicGUI$1(BasicGUI basicGUI, Frame frame) {
        this.this$0 = basicGUI;
        this.val$f = frame;
    }

    @Override
    public void windowClosing(WindowEvent windowEvent) {
        this.this$0.closed = true;
        this.val$f.dispose();
    }
}
