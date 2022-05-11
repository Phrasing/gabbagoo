package io.trickle.util;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

public class DirectObjectCodec implements MessageCodec {
   public String name;

   public Object decodeFromWire(int var1, Buffer var2) {
      return null;
   }

   public byte systemCodecID() {
      return -1;
   }

   public void encodeToWire(Buffer var1, Object var2) {
   }

   public String name() {
      return this.name + "Codec";
   }

   public Object transform(Object var1) {
      return var1;
   }

   public DirectObjectCodec(Class var1) {
      this.name = var1.getName();
   }
}
