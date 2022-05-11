/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.network.codec.packet.Packet
 *  io.trickle.network.codec.packet.PacketType
 *  io.vertx.core.buffer.Buffer
 */
package io.trickle.network.codec.packet;

import io.trickle.network.codec.packet.Packet;
import io.trickle.network.codec.packet.PacketType;
import io.vertx.core.buffer.Buffer;

public class PacketWriter {
    public PacketType type;
    public byte opcode;
    public Buffer buffer;

    public void writeString(String string) {
        byte[] byArray = string.getBytes();
        this.writeInt(byArray.length);
        this.buffer.appendBytes(byArray);
    }

    public void writeShort(int n) {
        this.writeShort((short)n);
    }

    public void writeBoolean(boolean bl) {
        this.writeByte(bl ? 1 : 0);
    }

    public Packet build() {
        return new Packet(this.opcode, this.type, this.buffer);
    }

    public static PacketWriter create(byte by, PacketType packetType) {
        return new PacketWriter(by, packetType);
    }

    public void writeByte(int n) {
        this.writeByte((byte)n);
    }

    public void writeShort(short s) {
        this.buffer.appendShort(s);
    }

    public void writeLong(long l) {
        this.buffer.appendLong(l);
    }

    public static PacketWriter create(int n, PacketType packetType) {
        return new PacketWriter((byte)n, packetType);
    }

    public PacketWriter(byte by, PacketType packetType) {
        this.opcode = by;
        this.type = packetType;
        this.buffer = Buffer.buffer();
    }

    public void writeByte(byte by) {
        this.buffer.appendByte(by);
    }

    public void writeInt(int n) {
        this.buffer.appendInt(n);
    }
}
