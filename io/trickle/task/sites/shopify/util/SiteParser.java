package io.trickle.task.sites.shopify.util;

import io.trickle.task.Task;
import io.trickle.task.sites.Site;
import io.trickle.task.sites.shopify.Mode;
import io.trickle.util.Pair;

public class SiteParser {
   public static Pair getProperties(Site var0) {
      // $FF: Couldn't be decompiled
   }

   public static String getURLFromSite(Task var0) {
      // $FF: Couldn't be decompiled
   }

   public static String getWalletsAuth(Site var0) {
      // $FF: Couldn't be decompiled
   }

   public static Mode getMode(String var0) {
      if (var0.contains("normal")) {
         return Mode.NORMAL;
      } else if (var0.contains("fast")) {
         return Mode.FAST;
      } else {
         return var0.contains("hybrid") ? Mode.HYBRID : Mode.HUMAN;
      }
   }

   public static String getGatewayFromSite(Site var0, boolean var1) {
      // $FF: Couldn't be decompiled
   }
}
