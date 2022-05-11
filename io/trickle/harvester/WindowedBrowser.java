/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.teamdev.jxbrowser.browser.Browser
 *  com.teamdev.jxbrowser.engine.Engine
 *  com.teamdev.jxbrowser.view.swing.BrowserView
 *  io.trickle.harvester.WindowedBrowser$1
 */
package io.trickle.harvester;

import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.view.swing.BrowserView;
import io.trickle.harvester.WindowedBrowser;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JFrame;

public class WindowedBrowser {
    public int currentHarvesterNumber;
    public JFrame frame;
    public static AtomicInteger harvesterCounter;
    public Browser browser;
    public static Dimension SCREEN_DIMENSION;
    public Engine browserEngine;

    public void enlarge() {
        this.frame.setSize(700, 700);
        this.frame.setAlwaysOnTop(true);
        this.frame.setAlwaysOnTop(false);
    }

    public Browser browser() {
        return this.browser;
    }

    public void close() {
        if (this.frame != null) {
            this.frame.dispatchEvent(new WindowEvent(this.frame, 201));
        }
        if (this.browser.isClosed()) return;
        this.browser.close();
    }

    public void setTitle(String string) {
        this.frame.setTitle("Trickle Harvester-" + string);
    }

    public JFrame frame() {
        return this.frame;
    }

    public void createWindow() {
        if (this.browserEngine.isClosed()) {
            return;
        }
        if (this.browser == null) {
            this.browser = this.browserEngine.newBrowser();
        }
        this.frame = new JFrame("Trickle Harvester");
        this.frame.addWindowListener((WindowListener)new 1(this));
        this.frame.setDefaultCloseOperation(2);
        BrowserView browserView = BrowserView.newInstance((Browser)this.browser);
        this.frame.add((Component)browserView, "Center");
        int n = 375;
        int n2 = 667;
        int n3 = this.frame.getLocation().x + n * (this.currentHarvesterNumber - 1);
        int n4 = n3 / WindowedBrowser.SCREEN_DIMENSION.width;
        int n5 = (this.frame.getLocation().x + n * (this.currentHarvesterNumber - 1)) % WindowedBrowser.SCREEN_DIMENSION.width;
        int n6 = (this.frame.getLocation().y + n2 * n4) % WindowedBrowser.SCREEN_DIMENSION.height;
        this.frame.setSize(n, n2);
        this.frame.setLocation(n5, n6);
        this.frame.setVisible(true);
        this.frame.setAlwaysOnTop(true);
        this.frame.setAlwaysOnTop(false);
    }

    public WindowedBrowser(Engine engine) {
        this.browserEngine = engine;
        this.currentHarvesterNumber = harvesterCounter.incrementAndGet();
    }

    static {
        SCREEN_DIMENSION = Toolkit.getDefaultToolkit().getScreenSize();
        harvesterCounter = new AtomicInteger(0);
    }

    public void setTitle(int n, String string) {
        this.frame.setTitle("Trickle Harvester-YS-" + n + " " + string);
    }
}
