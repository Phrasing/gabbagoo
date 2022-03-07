/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.buffer.Buffer
 */
package io.trickle.network.codec.packet;

import io.trickle.network.codec.packet.Packet;
import io.vertx.core.buffer.Buffer;

public class PacketReader {
    public Buffer buffer;
    public int readerIndex;

    public PacketReader(Buffer buffer) {
        this.buffer = buffer;
        this.readerIndex = 0;
    }

    public static PacketReader create(Buffer buffer) {
        return new PacketReader(buffer);
    }

    public byte readByte() {
        byte by = this.buffer.getByte(this.readerIndex);
        ++this.readerIndex;
        return by;
    }

    public long readLong() {
        long l = this.buffer.getLong(this.readerIndex);
        this.readerIndex += 8;
        return l;
    }

    public boolean readBoolean() {
        if (this.readByte() == 0) return false;
        return true;
    }

    public short readShort() {
        short s = this.buffer.getShort(this.readerIndex);
        this.readerIndex += 2;
        return s;
    }

    public String readString() {
        int n = this.readInt();
        String string = this.buffer.getString(this.readerIndex, this.readerIndex + n);
        this.readerIndex += n;
        return string;
    }

    public static PacketReader create(Packet packet) {
        return new PacketReader(packet);
    }

    public int readInt() {
        int n = this.buffer.getInt(this.readerIndex);
        this.readerIndex += 4;
        return n;
    }

    public PacketReader(Packet packet) {
        this(packet.getPayload());
    }

    public Buffer getBuffer() {
        return this.buffer;
    }
}

