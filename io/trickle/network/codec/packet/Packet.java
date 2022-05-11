package io.trickle.network.codec.packet;

import io.vertx.core.buffer.Buffer;

public class Packet {
   public Buffer payload;
   public PacketType type;
   public byte opcode;

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof Packet)) {
         return false;
      } else {
         Packet var2 = (Packet)var1;
         if (this.opcode != var2.opcode) {
            return false;
         } else {
            return this.getSize() != var2.getSize() ? false : this.payload.equals(var2.payload);
         }
      }
   }

   public PacketType getType() {
      return this.type;
   }

   public Buffer getPayload() {
      return this.payload;
   }

   public Packet(byte var1, PacketType var2, Buffer var3) {
      this.opcode = var1;
      this.type = var2;
      this.payload = var3;
   }

   public byte getOpcode() {
      return this.opcode;
   }

   public int hashCode() {
      int var1 = this.opcode;
      var1 = 31 * var1 + this.type.hashCode();
      var1 = 31 * var1 + this.payload.hashCode();
      return var1;
   }

   public int getSize() {
      return this.payload.length();
   }
}
