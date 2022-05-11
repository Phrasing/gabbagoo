package io.trickle.task.sites.yeezy.util;

public class Sizes$SizePair {
   public String sizeNum;
   public Sizes$Size sizeVariant;

   public Sizes$SizePair(String var1, Sizes$Size var2) {
      this.sizeNum = var1;
      this.sizeVariant = var2;
   }

   public String toString() {
      return this.sizeVariant.toString();
   }
}
