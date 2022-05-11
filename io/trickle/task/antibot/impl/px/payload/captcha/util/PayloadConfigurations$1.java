package io.trickle.task.antibot.impl.px.payload.captcha.util;

import io.trickle.task.antibot.impl.px.Types;
import io.trickle.task.sites.Site;

public class PayloadConfigurations$1 {
   public static int[] $SwitchMap$io$trickle$task$sites$Site;
   public static int[] $SwitchMap$io$trickle$task$antibot$impl$px$Types = new int[Types.values().length];

   static {
      try {
         $SwitchMap$io$trickle$task$antibot$impl$px$Types[Types.DESKTOP.ordinal()] = 1;
      } catch (NoSuchFieldError var5) {
      }

      try {
         $SwitchMap$io$trickle$task$antibot$impl$px$Types[Types.CAPTCHA_MOBILE.ordinal()] = 2;
      } catch (NoSuchFieldError var4) {
      }

      try {
         $SwitchMap$io$trickle$task$antibot$impl$px$Types[Types.CAPTCHA_DESKTOP.ordinal()] = 3;
      } catch (NoSuchFieldError var3) {
      }

      $SwitchMap$io$trickle$task$sites$Site = new int[Site.values().length];

      try {
         $SwitchMap$io$trickle$task$sites$Site[Site.HIBBETT.ordinal()] = 1;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$io$trickle$task$sites$Site[Site.WALMART.ordinal()] = 2;
      } catch (NoSuchFieldError var1) {
      }

   }
}
