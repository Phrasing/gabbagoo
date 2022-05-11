package io.trickle.task.sites.yeezy.util;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import org.bouncycastle.util.encoders.Hex;

public class RT {
   public static String getByteHex() {
      byte[] var0 = new byte[4];
      ThreadLocalRandom.current().nextBytes(var0);
      return Hex.toHexString(var0);
   }

   public static String getRT() {
      UUID var10000 = UUID.randomUUID();
      return "\"z=1&dm=yeezysupply.com&si=" + var10000 + "&ss=" + Long.toString(System.currentTimeMillis(), 36) + "&sl=1&tt=" + Integer.toString(ThreadLocalRandom.current().nextInt(890, 5800), 36) + "&bcn=%2F%2F" + getByteHex() + ".akstat.io%2F\"";
   }
}
