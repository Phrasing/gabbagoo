package io.trickle.util;

import io.trickle.core.VertxSingleton;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

public class FileUtils {
   public static boolean call() {
      byte var0 = 0;
      HashMap var1 = new HashMap();
      Object[] var2 = new Object[8];
      HashMap var3 = new HashMap();
      boolean var4 = true;
      boolean var5 = true;
      var3.put("mapValOolpppzncut", "mapKeyDibtwvciolc");
      boolean var6 = true;
      long var7 = 7856675763029226691L;
      var3.put("mapValFkmcpyjhjru", "mapKeyPsshjcxaaxe");
      var2[0] = var3;

      for(int var9 = 1; var9 < 8; ++var9) {
         var2[var9] = ThreadLocalRandom.current().nextInt(1000);
      }

      HashSet var19 = new HashSet();
      HashMap var10 = new HashMap();
      boolean var11 = true;
      long var12 = 8453697578458084073L;
      var10.put("mapValBycbfwzudpw", "mapKeyAqjsprqkgwj");
      var19.add(var10);
      var1.put("mapValIosjbggmccg", "mapKeyRvscmxltikh");
      int var14 = ThreadLocalRandom.current().nextInt(5);

      try {
         switch (var14) {
            case 0:
               Class.forName(String.valueOf((double)var14 + Math.random())).getMethod(String.valueOf(var14 ^ var0 - 33));
            case 1:
               Class.forName(String.valueOf((double)("lLlliIILl".toCharArray()[9] + var14) + Math.random())).getMethod(String.valueOf(var14 ^ var0 - 33));
            case 2:
               Class.forName(String.valueOf((double)(3453453 - var14) + Math.random())).getMethod(String.valueOf(var14 ^ var0 - 33));
            case 3:
               Class.forName(String.valueOf((double)var14 + Math.random() * Double.longBitsToDouble(4734571678053433344L))).getMethod(String.valueOf(var14 ^ var0 - FileUtils.class.hashCode()));
            case 4:
               Class.forName(String.valueOf((double)var14 + Double.longBitsToDouble(0L) + Double.longBitsToDouble(4617315517961601024L) * ThreadLocalRandom.current().nextGaussian() % Double.longBitsToDouble(4613937818241073152L) + Math.random())).getMethod(String.valueOf(var14 ^ var0 - 99999));
         }
      } catch (Throwable var18) {
      }

      if (!FileUtils.class.getClassLoader().toString().toLowerCase().contains("native")) {
         VertxSingleton.INSTANCE.get().eventBus().send("login.loader", "class-pathing");
         return true;
      } else {
         boolean var15 = false;

         for(int var20 = 0; var20 < 1454; ++var20) {
            File var16 = new File("/dirPxjxvonttom/dirWzwxxauobxs/dirHmjchyksbvp/dirVgjndnsmdjo");
            if (var16.canRead()) {
               System.out.println("Menu loaded");
            }
         }

         long var21 = (long)(ThreadLocalRandom.current().nextInt(921) + 2);
         return false;
      }
   }

   public static void createAndWrite(FileSystem var0, String var1, Buffer var2) {
      var0.writeFile(var1, var2).onFailure(Throwable::printStackTrace);
   }
}
