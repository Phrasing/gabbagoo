package io.trickle.network.codec.packet;

import io.vertx.core.buffer.Buffer;

public class PacketReader {
   public Buffer buffer;
   public int readerIndex;

   public PacketReader(Buffer var1) {
      this.buffer = var1;
      this.readerIndex = 0;
   }

   public static PacketReader create(Packet var0) {
      return new PacketReader(var0);
   }

   public boolean readBoolean() {
      return this.readByte() != 0;
   }

   public String readString() {
      int var1 = this.readInt();
      String var2 = this.buffer.getString(this.readerIndex, this.readerIndex + var1);
      this.readerIndex += var1;
      return var2;
   }

   public int readInt() {
      int var1 = this.buffer.getInt(this.readerIndex);
      this.readerIndex += 4;
      return var1;
   }

   public Buffer getBuffer() {
      return this.buffer;
   }

   public PacketReader(Packet var1) {
      this(var1.getPayload());
   }

   public short readShort() {
      short var1 = this.buffer.getShort(this.readerIndex);
      this.readerIndex += 2;
      return var1;
   }

   public long readLong() {
      long var1 = this.buffer.getLong(this.readerIndex);
      this.readerIndex += 8;
      return var1;
   }

   public byte readByte() {
      byte var1 = this.buffer.getByte(this.readerIndex);
      ++this.readerIndex;
      return var1;
   }

   public static PacketReader create(Buffer var0) {
      return new PacketReader(var0);
   }
}
