package io.trickle.harvester;

import io.trickle.task.sites.Site;

public class Sitekeys$1 {
   public static int[] $SwitchMap$io$trickle$task$sites$Site = new int[Site.values().length];

   static {
      try {
         $SwitchMap$io$trickle$task$sites$Site[Site.WALMART.ordinal()] = 1;
      } catch (NoSuchFieldError var4) {
      }

      try {
         $SwitchMap$io$trickle$task$sites$Site[Site.YEEZY.ordinal()] = 2;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$io$trickle$task$sites$Site[Site.JDSPORTS.ordinal()] = 3;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$io$trickle$task$sites$Site[Site.FINISHLINE.ordinal()] = 4;
      } catch (NoSuchFieldError var1) {
      }

   }
}
