/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.teamdev.jxbrowser.engine.Engine
 *  io.trickle.harvester.WindowedUrlBrowser
 */
package io.trickle.harvester;

import com.teamdev.jxbrowser.engine.Engine;
import io.trickle.harvester.WindowedUrlBrowser;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.CompletableFuture;

public class WindowedUrlBrowser$1
extends WindowAdapter {
    public WindowedUrlBrowser this$0;

    public WindowedUrlBrowser$1(WindowedUrlBrowser windowedUrlBrowser) {
        this.this$0 = windowedUrlBrowser;
    }

    @Override
    public void windowClosing(WindowEvent windowEvent) {
        if (this.this$0.browserEngine.isClosed()) return;
        CompletableFuture.runAsync(() -> ((Engine)this.this$0.browserEngine).close());
    }
}
