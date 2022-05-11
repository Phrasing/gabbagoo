package io.trickle.network.codec.packet.handler;

import io.trickle.network.codec.packet.Packet;
import io.trickle.network.codec.packet.PacketReader;
import io.trickle.network.codec.packet.PacketType;
import io.vertx.core.Handler;
import io.vertx.core.Promise;

public class LoginHandler implements PacketHandler {
   public void handle(Promise var1, Packet var2) {
      PacketReader var3 = PacketReader.create(var2);
      String var4 = var3.readString();
      byte var5 = var3.readByte();

      try {
         if (var5 == 1 && var4 != null && !var4.isEmpty()) {
            var1.tryComplete();
         } else {
            switch (var5) {
               case -2:
                  if (var4 != null && !var4.isEmpty() && !var4.isBlank()) {
                     var1.tryFail(var4);
                  } else {
                     var1.tryFail("Error occurred on login");
                  }
                  break;
               case -1:
                  var1.tryFail("Too many instances!");
                  break;
               case 0:
                  var1.tryFail("Invalid login details");
            }
         }
      } catch (Throwable var7) {
      }

   }

   public void handle(Packet var1) {
      throw new RuntimeException("Not-implemented");
   }

   public PacketType getType() {
      return PacketType.INT_VARIABLE_SIZED;
   }

   public void handle(Packet var1, Handler var2) {
      throw new RuntimeException("Not-implemented");
   }
}
