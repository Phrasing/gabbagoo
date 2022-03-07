/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.teamdev.jxbrowser.engine.Engine
 */
package io.trickle.harvester;

import com.teamdev.jxbrowser.engine.Engine;
import io.trickle.harvester.WindowedBrowser;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.CompletableFuture;

public class WindowedBrowser$1
extends WindowAdapter {
    public WindowedBrowser this$0;

    @Override
    public void windowClosing(WindowEvent windowEvent) {
        if (this.this$0.browserEngine.isClosed()) return;
        CompletableFuture.runAsync(() -> ((Engine)this.this$0.browserEngine).close());
    }

    public WindowedBrowser$1(WindowedBrowser windowedBrowser) {
        this.this$0 = windowedBrowser;
    }
}

