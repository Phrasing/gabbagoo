package io.trickle.task.antibot.impl.px.payload.token;

import io.trickle.task.sites.Site;

public class SecondPayload$1 {
   public static int[] $SwitchMap$io$trickle$task$sites$Site = new int[Site.values().length];

   static {
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
