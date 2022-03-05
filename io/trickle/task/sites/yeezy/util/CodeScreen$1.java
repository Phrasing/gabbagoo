/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.sites.yeezy.util;

import io.trickle.task.sites.yeezy.util.CodeScreen;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CodeScreen$1
extends WindowAdapter {
    public CodeScreen val$newContentPane;

    @Override
    public void windowActivated(WindowEvent windowEvent) {
        this.val$newContentPane.resetFocus();
    }

    public CodeScreen$1(CodeScreen codeScreen) {
        this.val$newContentPane = codeScreen;
    }
}

