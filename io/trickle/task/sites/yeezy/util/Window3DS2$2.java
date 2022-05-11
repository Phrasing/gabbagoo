/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.teamdev.jxbrowser.engine.Engine
 *  io.trickle.task.sites.yeezy.util.Window3DS2
 */
package io.trickle.task.sites.yeezy.util;

import com.teamdev.jxbrowser.engine.Engine;
import io.trickle.task.sites.yeezy.util.Window3DS2;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.CompletableFuture;

public class Window3DS2$2
extends WindowAdapter {
    public Window3DS2 this$0;
    public Engine val$engine;

    public Window3DS2$2(Window3DS2 window3DS2, Engine engine) {
        this.this$0 = window3DS2;
        this.val$engine = engine;
    }

    @Override
    public void windowClosing(WindowEvent windowEvent) {
        try {
            if (this.val$engine.isClosed()) return;
            CompletableFuture.runAsync(() -> ((Engine)this.val$engine).close());
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }
}
