/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.buffer.Buffer
 */
package io.trickle.network.codec.packet;

import io.trickle.network.codec.packet.Packet;
import io.trickle.network.codec.packet.PacketType;
import io.vertx.core.buffer.Buffer;

public class PacketWriter {
    public byte opcode;
    public PacketType type;
    public Buffer buffer;

    public void writeBoolean(boolean bl) {
        this.writeByte(bl ? 1 : 0);
    }

    public void writeLong(long l) {
        this.buffer.appendLong(l);
    }

    public void writeByte(int n) {
        this.writeByte((byte)n);
    }

    public void writeShort(int n) {
        this.writeShort((short)n);
    }

    public static PacketWriter create(byte by, PacketType packetType) {
        return new PacketWriter(by, packetType);
    }

    public static PacketWriter create(int n, PacketType packetType) {
        return new PacketWriter((byte)n, packetType);
    }

    public void writeByte(byte by) {
        this.buffer.appendByte(by);
    }

    public PacketWriter(byte by, PacketType packetType) {
        this.opcode = by;
        this.type = packetType;
        this.buffer = Buffer.buffer();
    }

    public Packet build() {
        return new Packet(this.opcode, this.type, this.buffer);
    }

    public void writeShort(short s) {
        this.buffer.appendShort(s);
    }

    public void writeInt(int n) {
        this.buffer.appendInt(n);
    }

    public void writeString(String string) {
        byte[] byArray = string.getBytes();
        this.writeInt(byArray.length);
        this.buffer.appendBytes(byArray);
    }
}

