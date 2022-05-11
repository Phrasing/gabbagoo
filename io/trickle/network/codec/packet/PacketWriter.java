package io.trickle.network.codec.packet;

import io.vertx.core.buffer.Buffer;

public class PacketWriter {
   public PacketType type;
   public byte opcode;
   public Buffer buffer;

   public void writeString(String var1) {
      byte[] var2 = var1.getBytes();
      this.writeInt(var2.length);
      this.buffer.appendBytes(var2);
   }

   public void writeShort(int var1) {
      this.writeShort((short)var1);
   }

   public void writeBoolean(boolean var1) {
      this.writeByte(var1 ? 1 : 0);
   }

   public Packet build() {
      return new Packet(this.opcode, this.type, this.buffer);
   }

   public static PacketWriter create(byte var0, PacketType var1) {
      return new PacketWriter(var0, var1);
   }

   public void writeByte(int var1) {
      this.writeByte((byte)var1);
   }

   public void writeShort(short var1) {
      this.buffer.appendShort(var1);
   }

   public void writeLong(long var1) {
      this.buffer.appendLong(var1);
   }

   public static PacketWriter create(int var0, PacketType var1) {
      return new PacketWriter((byte)var0, var1);
   }

   public PacketWriter(byte var1, PacketType var2) {
      this.opcode = var1;
      this.type = var2;
      this.buffer = Buffer.buffer();
   }

   public void writeByte(byte var1) {
      this.buffer.appendByte(var1);
   }

   public void writeInt(int var1) {
      this.buffer.appendInt(var1);
   }
}
