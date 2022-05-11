package io.trickle.harvester;

import com.teamdev.jxbrowser.navigation.event.NavigationStarted;
import com.teamdev.jxbrowser.net.event.ResponseBytesReceived;
import com.teamdev.jxbrowser.view.swing.BrowserView;
import io.trickle.basicgui.LinkOverrideListener;
import io.trickle.core.Controller;
import io.trickle.core.Engine;
import io.trickle.core.VertxSingleton;
import io.trickle.task.TaskController;
import io.vertx.core.impl.ConcurrentHashSet;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.concurrent.CompletableFuture;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class WindowedUrlBrowser extends WindowedBrowser {
   public ConcurrentHashSet productList = new ConcurrentHashSet();

   public static void lambda$createWindow$0(JTextField var0, NavigationStarted var1) {
      if (var1.isInMainFrame() && var1.url() != null) {
         var0.setText(var1.url());
      }

   }

   public void lambda$createWindow$4() {
      String var1 = this.browser.url();
      if (var1 != null && !var1.isBlank() && var1.contains("http")) {
         System.out.println("Mass link changed -> " + var1);
         ((TaskController)Engine.get().getModule(Controller.TASK)).massEditLinkOrKeyword(var1);
         VertxSingleton.INSTANCE.get().eventBus().publish(LinkOverrideListener.MASS_LINK_CHANGE_ADDRESS, var1);
      }

   }

   public void lambda$createWindow$2(ActionEvent var1) {
      if (this.browser.navigation().canGoForward()) {
         this.browser.navigation().goForward();
      }

   }

   public void lambda$createWindow$5(ActionEvent var1) {
      System.out.println("Sending current link to bot");
      CompletableFuture.runAsync(this::lambda$createWindow$4);
   }

   public void createWindow() {
      if (!this.browserEngine.isClosed()) {
         if (this.browser == null) {
            this.browser = this.browserEngine.newBrowser();
         }

         this.frame = new JFrame("Trickle Harvester");
         this.frame.addWindowListener(new WindowedUrlBrowser$1(this));
         this.frame.setDefaultCloseOperation(2);
         JTextField var1 = new JTextField();
         var1.setPreferredSize(new Dimension(400, 30));
         this.browser.navigation().on(NavigationStarted.class, WindowedUrlBrowser::lambda$createWindow$0);
         var1.addKeyListener(new WindowedUrlBrowser$2(this, var1));
         JButton var2 = new JButton("<");
         var2.setPreferredSize(new Dimension(50, 30));
         var2.addActionListener(this::lambda$createWindow$1);
         JButton var3 = new JButton(">");
         var3.setPreferredSize(new Dimension(50, 30));
         var3.addActionListener(this::lambda$createWindow$2);
         JButton var4 = new JButton("R");
         var4.setPreferredSize(new Dimension(50, 30));
         var4.addActionListener(this::lambda$createWindow$3);
         JButton var5 = new JButton("T");
         var5.setPreferredSize(new Dimension(50, 30));
         var5.addActionListener(this::lambda$createWindow$5);
         JPanel var6 = new JPanel(new FlowLayout(0, 0, 5));
         var6.add(var1, Float.intBitsToFloat(1056964608));
         var6.add(var2, Float.intBitsToFloat(1056964608));
         var6.add(var3, Float.intBitsToFloat(1056964608));
         var6.add(var4, Float.intBitsToFloat(1056964608));
         var6.add(var5, Float.intBitsToFloat(1056964608));
         this.frame.add(var6, "North");
         BrowserView var7 = BrowserView.newInstance(this.browser);
         this.frame.add(var7, "Center");
         short var8 = 600;
         short var9 = 667;
         int var10001 = var8 * (this.currentHarvesterNumber - 1);
         int var10 = this.frame.getLocation().x + var10001;
         int var11 = var10 / SCREEN_DIMENSION.width;
         var10001 = var8 * (this.currentHarvesterNumber - 1);
         int var12 = (this.frame.getLocation().x + var10001) % SCREEN_DIMENSION.width;
         int var13 = (this.frame.getLocation().y + var9 * var11) % SCREEN_DIMENSION.height;
         this.frame.setSize(var8, var9);
         this.frame.setLocation(var12, var13);
         this.frame.setVisible(true);
         this.frame.setAlwaysOnTop(true);
         this.frame.setAlwaysOnTop(false);
         this.browser.navigation().loadUrl("https://time.is/");
         this.browser.engine().network().on(ResponseBytesReceived.class, this::lambda$createWindow$6);
      }
   }

   public void lambda$createWindow$1(ActionEvent var1) {
      if (this.browser.navigation().canGoBack()) {
         this.browser.navigation().goBack();
      }

   }

   public void lambda$createWindow$3(JTextField var1, ActionEvent var2) {
      System.out.println("Reloading page");
      this.browser.navigation().loadUrl(var1.getText().replace("password", ""));
   }

   public void lambda$createWindow$6(ResponseBytesReceived var1) {
      String var2 = var1.urlRequest().url();
      if (var2.contains("/products/") && (new String(var1.data())).contains("cdn.shopify.com") && !this.productList.contains(var2)) {
         this.productList.add(var2);
         System.out.println("Located new domain product");
         ((TaskController)Engine.get().getModule(Controller.TASK)).massEditLinkOrKeyword(var2);
         VertxSingleton.INSTANCE.get().eventBus().publish(LinkOverrideListener.MASS_LINK_CHANGE_ADDRESS, var2);
      }

   }

   public WindowedUrlBrowser(com.teamdev.jxbrowser.engine.Engine var1) {
      super(var1);
   }
}
