package io.trickle.util.request;

import io.netty.util.AsciiString;
import io.vertx.core.MultiMap;

public enum Headers$Pseudo {
   public static CharSequence SCHEME = AsciiString.cached(":scheme");
   public static CharSequence PATH = AsciiString.cached(":path");
   MPSA(new String[]{"m", "p", "s", "a"}),
   MASP(new String[]{"m", "a", "s", "p"});

   public String[] headerOrder;
   public static Headers$Pseudo[] $VALUES = new Headers$Pseudo[]{MPAS, MSPA, MASP, MPSA};
   MSPA(new String[]{"m", "s", "p", "a"});

   public static CharSequence METHOD = AsciiString.cached(":method");
   public static CharSequence AUTHORITY = AsciiString.cached(":authority");
   MPAS(new String[]{"m", "p", "a", "s"});

   public Headers$Pseudo(String... var3) {
      if (var3.length != 4) {
         throw new RuntimeException("Invalid Header Length");
      } else {
         this.headerOrder = var3;
      }
   }

   public static MultiMap build(String... var0) {
      MultiMap var1 = MultiMap.caseInsensitiveMultiMap();
      String[] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         switch (var2[var4]) {
            case "m":
               var1.add(METHOD, Headers.DEFAULT);
               break;
            case "p":
               var1.add(PATH, Headers.DEFAULT);
               break;
            case "a":
               var1.add(AUTHORITY, Headers.DEFAULT);
               break;
            case "s":
               var1.add(SCHEME, Headers.DEFAULT);
               break;
            default:
               throw new RuntimeException("Illegal header detected: " + var5);
         }
      }

      return var1;
   }

   public static MultiMap fromOrder(String... var0) {
      return build(var0);
   }

   public MultiMap get() {
      return build(this.headerOrder);
   }
}
