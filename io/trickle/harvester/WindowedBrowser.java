package io.trickle.harvester;

import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.view.swing.BrowserView;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JFrame;

public class WindowedBrowser {
   public int currentHarvesterNumber;
   public JFrame frame;
   public static AtomicInteger harvesterCounter = new AtomicInteger(0);
   public Browser browser;
   public static Dimension SCREEN_DIMENSION = Toolkit.getDefaultToolkit().getScreenSize();
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

      if (!this.browser.isClosed()) {
         this.browser.close();
      }

   }

   public void setTitle(String var1) {
      this.frame.setTitle("Trickle Harvester-" + var1);
   }

   public JFrame frame() {
      return this.frame;
   }

   public void createWindow() {
      if (!this.browserEngine.isClosed()) {
         if (this.browser == null) {
            this.browser = this.browserEngine.newBrowser();
         }

         this.frame = new JFrame("Trickle Harvester");
         this.frame.addWindowListener(new WindowedBrowser$1(this));
         this.frame.setDefaultCloseOperation(2);
         BrowserView var1 = BrowserView.newInstance(this.browser);
         this.frame.add(var1, "Center");
         short var2 = 375;
         short var3 = 667;
         int var10001 = var2 * (this.currentHarvesterNumber - 1);
         int var4 = this.frame.getLocation().x + var10001;
         int var5 = var4 / SCREEN_DIMENSION.width;
         var10001 = var2 * (this.currentHarvesterNumber - 1);
         int var6 = (this.frame.getLocation().x + var10001) % SCREEN_DIMENSION.width;
         int var7 = (this.frame.getLocation().y + var3 * var5) % SCREEN_DIMENSION.height;
         this.frame.setSize(var2, var3);
         this.frame.setLocation(var6, var7);
         this.frame.setVisible(true);
         this.frame.setAlwaysOnTop(true);
         this.frame.setAlwaysOnTop(false);
      }
   }

   public WindowedBrowser(Engine var1) {
      this.browserEngine = var1;
      this.currentHarvesterNumber = harvesterCounter.incrementAndGet();
   }

   public void setTitle(int var1, String var2) {
      this.frame.setTitle("Trickle Harvester-YS-" + var1 + " " + var2);
   }
}
