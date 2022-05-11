package io.trickle.task.sites.hibbett;

import io.trickle.util.Utils;
import java.util.concurrent.ThreadLocalRandom;

public class FakeIOSValueGens {
   public static String randOfLength(int var0) {
      return Utils.getString(var0);
   }

   public static String genTag() {
      String var10000 = randOfLength(3);
      return "W/\"" + var10000 + "-" + randOfLength(27) + "\"";
   }

   public static String genUA() {
      int var10000 = ThreadLocalRandom.current().nextInt(0, 4);
      return "Hibbett | CG/4.15.0 (com.hibbett.hibbett-sports; build:7993; iOS 1" + var10000 + "." + ThreadLocalRandom.current().nextInt(1, 9) + "." + ThreadLocalRandom.current().nextInt(1, 9) + ") Alamofire/5.0.0-rc.3";
   }
}
