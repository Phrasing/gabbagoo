package io.trickle.network.codec.encoder;

import io.trickle.network.codec.packet.Packet;
import io.trickle.network.codec.packet.PacketType;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;

public class PacketEncoder implements Handler {
   public Handler bufferHandler;

   public void handle(Packet var1) {
      PacketType var2 = var1.getType();
      int var3 = var1.getSize();

      try {
         Buffer var4 = Buffer.buffer();
         var4.appendByte(var1.getOpcode());
         if (var2 == PacketType.BYTE_VARIABLE_SIZED) {
            var4.appendByte((byte)var3);
         } else if (var2 == PacketType.SHORT_VARIABLE_SIZED) {
            var4.appendShort((short)var3);
         } else {
            var4.appendInt(var3);
         }

         var4.appendBuffer(var1.getPayload());
         this.bufferHandler.handle(var4);
      } catch (Throwable var5) {
         var5.printStackTrace();
      }

   }

   public void handle(Object var1) {
      this.handle((Packet)var1);
   }

   public PacketEncoder(Handler var1) {
      this.bufferHandler = var1;
   }
}
