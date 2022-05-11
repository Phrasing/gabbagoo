package io.trickle.task.sites.walmart;

public enum Mode {
   DESKTOP;

   public static Mode[] $VALUES = new Mode[]{DESKTOP, MOBILE};
   MOBILE;

   public static Mode getMode(String var0) {
      return var0.contains("desktop") ? DESKTOP : MOBILE;
   }
}
