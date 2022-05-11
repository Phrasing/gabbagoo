package io.trickle.task.sites.shopify;

public enum Mode {
   NORMAL,
   FAST,
   HYBRID,
   HUMAN;

   public static Mode[] $VALUES = new Mode[]{FAST, NORMAL, HUMAN, HYBRID};
}
