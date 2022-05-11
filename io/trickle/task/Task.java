package io.trickle.task;

import io.trickle.profile.Profile;
import io.trickle.task.sites.Site;
import io.trickle.task.sites.yeezy.util.rotator.ProfileRotator;
import io.trickle.util.Storage;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import net.openhft.hashing.LongHashFunction;

public class Task {
   public static final ProfileRotator profileRotator = new ProfileRotator();
   private final Site site;
   private final String siteUserEntry;
   private final Profile profile;
   private final String size;
   private final String qty;
   private final String mode;
   private final int taskQuantity;
   private String captchaKey;
   private String[] keywords;
   private int retryDelay;
   private int monitorDelay;
   private String shippingRate;
   private Integer monitorDelayMass;
   private String password;
   private String hash = "";

   public Task(Task other) {
      this.site = other.site;
      this.siteUserEntry = other.siteUserEntry;
      this.profile = other.profile.copy();
      this.size = other.size;
      this.qty = other.qty;
      this.mode = other.mode;
      this.captchaKey = other.captchaKey;
      this.taskQuantity = other.taskQuantity;
      this.keywords = other.keywords;
      this.retryDelay = other.retryDelay;
      this.monitorDelay = other.monitorDelay;
      this.shippingRate = other.shippingRate;
      this.monitorDelayMass = other.monitorDelayMass;
      if (this.site == Site.YEEZY && this.taskQuantity != 0) {
         try {
            profileRotator.put(this);
         } catch (Throwable var3) {
         }
      }

   }

   public Task(String[] row) {
      this.site = Site.getSite(row[19]);
      if (this.site == Site.BESTBUY) {
         this.keywords = row[0].replace("  ", " ").split(" ");
      } else {
         this.keywords = row[0].toUpperCase().replace("  ", " ").split(" ");
      }

      this.size = row[1];
      this.qty = this.size;
      this.profile = new Profile(row);
      this.taskQuantity = Integer.parseInt(row[16]);
      this.retryDelay = Integer.parseInt(row[17]);
      this.monitorDelay = Integer.parseInt(row[18]);
      this.siteUserEntry = row[19];
      this.mode = row[20].toLowerCase();
      if (row.length <= 22) {
         if (row.length == 22) {
            this.captchaKey = row[21];
         }

         this.shippingRate = "";
      } else {
         this.captchaKey = row[21];
         this.shippingRate = row[22];
      }

      this.shippingRate = this.shippingRate.replace("\r", "").replace("\n", "");
      this.enforceDelayLimits();
   }

   public static String randomizeCase(String str) {
      StringBuilder sb = new StringBuilder(str.length());
      char[] var2 = str.toCharArray();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         char c = var2[var4];
         sb.append(ThreadLocalRandom.current().nextBoolean() ? Character.toLowerCase(c) : Character.toUpperCase(c));
      }

      return sb.toString();
   }

   private void enforceDelayLimits() {
      if (this.site.equals(Site.WALMART) || this.site.equals(Site.WALMART_CA)) {
         if (this.mode.contains("desktop")) {
            this.retryDelay = Math.max(this.retryDelay, 2000);
            this.monitorDelay = Math.max(this.monitorDelay, 500);
         } else {
            this.retryDelay = Math.max(this.retryDelay, 4000);
            this.monitorDelay = Math.max(this.monitorDelay, 5000);
         }
      }

   }

   public Site getSite() {
      return this.site;
   }

   public String getSiteUserEntry() {
      return this.siteUserEntry;
   }

   public Profile getProfile() {
      return this.profile;
   }

   public String[] getKeywords() {
      return this.keywords;
   }

   public void setKeywords(String[] keywords) {
      this.keywords = keywords;
   }

   public String getSize() {
      return this.size;
   }

   public String getQty() {
      return this.qty;
   }

   public int getRetryDelay() {
      return this.retryDelay;
   }

   public int getMonitorDelay() {
      return this.monitorDelayMass == null ? this.monitorDelay : this.monitorDelayMass;
   }

   public void setMonitorDelay(Integer newDelay) {
      this.monitorDelayMass = newDelay;
   }

   public String getMode() {
      return this.mode;
   }

   public String getCaptchaKey() {
      return this.captchaKey;
   }

   public int getTaskQuantity() {
      return this.taskQuantity;
   }

   public String getPassword() {
      return this.password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String toString() {
      Site var10000 = this.site;
      return "Task{site=" + var10000 + ", siteUserEntry='" + this.siteUserEntry + "', profile=" + this.profile + ", size='" + this.size + "', qty='" + this.qty + "', mode='" + this.mode + "', captchaKey='" + this.captchaKey + "', taskQuantity=" + this.taskQuantity + ", keywords=" + Arrays.toString(this.keywords) + ", retryDelay=" + this.retryDelay + ", monitorDelay=" + this.monitorDelay + ", shippingRate='" + this.shippingRate == null ? "NaN" : this.shippingRate + "', monitorDelayMass=" + this.monitorDelayMass + "}";
   }

   public String getShippingRate() {
      return this.shippingRate;
   }

   public Task copy() {
      return new Task(this);
   }

   public String session() {
      if (this.hash.isBlank()) {
         this.hash = String.valueOf(LongHashFunction.wy_3().hashChars(Storage.ACCESS_KEY != null ? Storage.ACCESS_KEY : "") + (long)super.hashCode());
      }

      return this.hash;
   }
}
