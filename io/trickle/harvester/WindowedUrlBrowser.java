/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.teamdev.jxbrowser.browser.Browser
 *  com.teamdev.jxbrowser.engine.Engine
 *  com.teamdev.jxbrowser.navigation.event.NavigationStarted
 *  com.teamdev.jxbrowser.net.event.ResponseBytesReceived
 *  com.teamdev.jxbrowser.view.swing.BrowserView
 *  io.trickle.basicgui.LinkOverrideListener
 *  io.trickle.core.Controller
 *  io.trickle.core.Engine
 *  io.trickle.core.VertxSingleton
 *  io.trickle.harvester.WindowedBrowser
 *  io.trickle.harvester.WindowedUrlBrowser$1
 *  io.trickle.harvester.WindowedUrlBrowser$2
 *  io.trickle.task.TaskController
 *  io.vertx.core.impl.ConcurrentHashSet
 */
package io.trickle.harvester;

import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.navigation.event.NavigationStarted;
import com.teamdev.jxbrowser.net.event.ResponseBytesReceived;
import com.teamdev.jxbrowser.view.swing.BrowserView;
import io.trickle.basicgui.LinkOverrideListener;
import io.trickle.core.Controller;
import io.trickle.core.Engine;
import io.trickle.core.VertxSingleton;
import io.trickle.harvester.WindowedBrowser;
import io.trickle.harvester.WindowedUrlBrowser;
import io.trickle.task.TaskController;
import io.vertx.core.impl.ConcurrentHashSet;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowListener;
import java.util.concurrent.CompletableFuture;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class WindowedUrlBrowser
extends WindowedBrowser {
    public ConcurrentHashSet<String> productList = new ConcurrentHashSet();

    public static void lambda$createWindow$0(JTextField jTextField, NavigationStarted navigationStarted) {
        if (!navigationStarted.isInMainFrame()) return;
        if (navigationStarted.url() == null) return;
        jTextField.setText(navigationStarted.url());
    }

    public void lambda$createWindow$4() {
        String string = this.browser.url();
        if (string == null) return;
        if (string.isBlank()) return;
        if (!string.contains("http")) return;
        System.out.println("Mass link changed -> " + string);
        ((TaskController)Engine.get().getModule(Controller.TASK)).massEditLinkOrKeyword(string);
        VertxSingleton.INSTANCE.get().eventBus().publish(LinkOverrideListener.MASS_LINK_CHANGE_ADDRESS, (Object)string);
    }

    public void lambda$createWindow$2(ActionEvent actionEvent) {
        if (!this.browser.navigation().canGoForward()) return;
        this.browser.navigation().goForward();
    }

    public void lambda$createWindow$5(ActionEvent actionEvent) {
        System.out.println("Sending current link to bot");
        CompletableFuture.runAsync(this::lambda$createWindow$4);
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
        JTextField jTextField = new JTextField();
        jTextField.setPreferredSize(new Dimension(400, 30));
        this.browser.navigation().on(NavigationStarted.class, arg_0 -> WindowedUrlBrowser.lambda$createWindow$0(jTextField, arg_0));
        jTextField.addKeyListener((KeyListener)new 2(this, jTextField));
        JButton jButton = new JButton("<");
        jButton.setPreferredSize(new Dimension(50, 30));
        jButton.addActionListener(this::lambda$createWindow$1);
        JButton jButton2 = new JButton(">");
        jButton2.setPreferredSize(new Dimension(50, 30));
        jButton2.addActionListener(this::lambda$createWindow$2);
        JButton jButton3 = new JButton("R");
        jButton3.setPreferredSize(new Dimension(50, 30));
        jButton3.addActionListener(arg_0 -> this.lambda$createWindow$3(jTextField, arg_0));
        JButton jButton4 = new JButton("T");
        jButton4.setPreferredSize(new Dimension(50, 30));
        jButton4.addActionListener(this::lambda$createWindow$5);
        JPanel jPanel = new JPanel(new FlowLayout(0, 0, 5));
        jPanel.add((Component)jTextField, Float.valueOf(Float.intBitsToFloat(0x3F000000)));
        jPanel.add((Component)jButton, Float.valueOf(Float.intBitsToFloat(0x3F000000)));
        jPanel.add((Component)jButton2, Float.valueOf(Float.intBitsToFloat(0x3F000000)));
        jPanel.add((Component)jButton3, Float.valueOf(Float.intBitsToFloat(0x3F000000)));
        jPanel.add((Component)jButton4, Float.valueOf(Float.intBitsToFloat(0x3F000000)));
        this.frame.add((Component)jPanel, "North");
        BrowserView browserView = BrowserView.newInstance((Browser)this.browser);
        this.frame.add((Component)browserView, "Center");
        int n = 600;
        int n2 = 667;
        int n3 = this.frame.getLocation().x + n * (this.currentHarvesterNumber - 1);
        int n4 = n3 / WindowedUrlBrowser.SCREEN_DIMENSION.width;
        int n5 = (this.frame.getLocation().x + n * (this.currentHarvesterNumber - 1)) % WindowedUrlBrowser.SCREEN_DIMENSION.width;
        int n6 = (this.frame.getLocation().y + n2 * n4) % WindowedUrlBrowser.SCREEN_DIMENSION.height;
        this.frame.setSize(n, n2);
        this.frame.setLocation(n5, n6);
        this.frame.setVisible(true);
        this.frame.setAlwaysOnTop(true);
        this.frame.setAlwaysOnTop(false);
        this.browser.navigation().loadUrl("https://time.is/");
        this.browser.engine().network().on(ResponseBytesReceived.class, this::lambda$createWindow$6);
    }

    public void lambda$createWindow$1(ActionEvent actionEvent) {
        if (!this.browser.navigation().canGoBack()) return;
        this.browser.navigation().goBack();
    }

    public void lambda$createWindow$3(JTextField jTextField, ActionEvent actionEvent) {
        System.out.println("Reloading page");
        this.browser.navigation().loadUrl(jTextField.getText().replace("password", ""));
    }

    public void lambda$createWindow$6(ResponseBytesReceived responseBytesReceived) {
        String string = responseBytesReceived.urlRequest().url();
        if (!string.contains("/products/")) return;
        if (!new String(responseBytesReceived.data()).contains("cdn.shopify.com")) return;
        if (this.productList.contains((Object)string)) return;
        this.productList.add((Object)string);
        System.out.println("Located new domain product");
        ((TaskController)Engine.get().getModule(Controller.TASK)).massEditLinkOrKeyword(string);
        VertxSingleton.INSTANCE.get().eventBus().publish(LinkOverrideListener.MASS_LINK_CHANGE_ADDRESS, (Object)string);
    }

    public WindowedUrlBrowser(com.teamdev.jxbrowser.engine.Engine engine) {
        super(engine);
    }
}
