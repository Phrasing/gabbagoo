package io.trickle.task.sites.shopify;

import io.trickle.task.sites.Site;
import io.trickle.task.sites.shopify.util.REDIRECT_STATUS;

public class Shopify$1 {
   public static int[] $SwitchMap$io$trickle$task$sites$shopify$util$REDIRECT_STATUS;
   public static int[] $SwitchMap$io$trickle$task$sites$Site = new int[Site.values().length];

   static {
      try {
         $SwitchMap$io$trickle$task$sites$Site[Site.HUMANMADE.ordinal()] = 1;
      } catch (NoSuchFieldError var19) {
      }

      try {
         $SwitchMap$io$trickle$task$sites$Site[Site.KAWS_TOKYO.ordinal()] = 2;
      } catch (NoSuchFieldError var18) {
      }

      try {
         $SwitchMap$io$trickle$task$sites$Site[Site.ZINGARO.ordinal()] = 3;
      } catch (NoSuchFieldError var17) {
      }

      try {
         $SwitchMap$io$trickle$task$sites$Site[Site.NEIGHBORHOOD.ordinal()] = 4;
      } catch (NoSuchFieldError var16) {
      }

      try {
         $SwitchMap$io$trickle$task$sites$Site[Site.WINDANDSEA.ordinal()] = 5;
      } catch (NoSuchFieldError var15) {
      }

      try {
         $SwitchMap$io$trickle$task$sites$Site[Site.BAPE.ordinal()] = 6;
      } catch (NoSuchFieldError var14) {
      }

      try {
         $SwitchMap$io$trickle$task$sites$Site[Site.CALIF.ordinal()] = 7;
      } catch (NoSuchFieldError var13) {
      }

      try {
         $SwitchMap$io$trickle$task$sites$Site[Site.DSM_JP_ESHOP.ordinal()] = 8;
      } catch (NoSuchFieldError var12) {
      }

      try {
         $SwitchMap$io$trickle$task$sites$Site[Site.MCT.ordinal()] = 9;
      } catch (NoSuchFieldError var11) {
      }

      $SwitchMap$io$trickle$task$sites$shopify$util$REDIRECT_STATUS = new int[REDIRECT_STATUS.values().length];

      try {
         $SwitchMap$io$trickle$task$sites$shopify$util$REDIRECT_STATUS[REDIRECT_STATUS.OOS.ordinal()] = 1;
      } catch (NoSuchFieldError var10) {
      }

      try {
         $SwitchMap$io$trickle$task$sites$shopify$util$REDIRECT_STATUS[REDIRECT_STATUS.PASSWORD.ordinal()] = 2;
      } catch (NoSuchFieldError var9) {
      }

      try {
         $SwitchMap$io$trickle$task$sites$shopify$util$REDIRECT_STATUS[REDIRECT_STATUS.QUEUE_OLD.ordinal()] = 3;
      } catch (NoSuchFieldError var8) {
      }

      try {
         $SwitchMap$io$trickle$task$sites$shopify$util$REDIRECT_STATUS[REDIRECT_STATUS.QUEUE_NEW.ordinal()] = 4;
      } catch (NoSuchFieldError var7) {
      }

      try {
         $SwitchMap$io$trickle$task$sites$shopify$util$REDIRECT_STATUS[REDIRECT_STATUS.CHECKPOINT.ordinal()] = 5;
      } catch (NoSuchFieldError var6) {
      }

      try {
         $SwitchMap$io$trickle$task$sites$shopify$util$REDIRECT_STATUS[REDIRECT_STATUS.LOGIN_REQUIRED.ordinal()] = 6;
      } catch (NoSuchFieldError var5) {
      }

      try {
         $SwitchMap$io$trickle$task$sites$shopify$util$REDIRECT_STATUS[REDIRECT_STATUS.BAD_ACCOUNT.ordinal()] = 7;
      } catch (NoSuchFieldError var4) {
      }

      try {
         $SwitchMap$io$trickle$task$sites$shopify$util$REDIRECT_STATUS[REDIRECT_STATUS.CHALLENGE.ordinal()] = 8;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$io$trickle$task$sites$shopify$util$REDIRECT_STATUS[REDIRECT_STATUS.HOMEPAGE.ordinal()] = 9;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$io$trickle$task$sites$shopify$util$REDIRECT_STATUS[REDIRECT_STATUS.CART.ordinal()] = 10;
      } catch (NoSuchFieldError var1) {
      }

   }
}
