package io.trickle.util.concurrent;

public class DeferredStream$1 {
   public static int[] $SwitchMap$io$trickle$util$concurrent$DeferredStream$Priority = new int[DeferredStream$Priority.values().length];

   static {
      try {
         $SwitchMap$io$trickle$util$concurrent$DeferredStream$Priority[DeferredStream$Priority.INSTANT.ordinal()] = 1;
      } catch (NoSuchFieldError var4) {
      }

      try {
         $SwitchMap$io$trickle$util$concurrent$DeferredStream$Priority[DeferredStream$Priority.HIGH.ordinal()] = 2;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$io$trickle$util$concurrent$DeferredStream$Priority[DeferredStream$Priority.MEDIUM.ordinal()] = 3;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$io$trickle$util$concurrent$DeferredStream$Priority[DeferredStream$Priority.LOW.ordinal()] = 4;
      } catch (NoSuchFieldError var1) {
      }

   }
}
