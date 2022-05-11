/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.network.codec.packet.PacketType
 *  io.vertx.core.buffer.Buffer
 */
package io.trickle.network.codec.packet;

import io.trickle.network.codec.packet.PacketType;
import io.vertx.core.buffer.Buffer;

public class Packet {
    public Buffer payload;
    public PacketType type;
    public byte opcode;

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Packet)) {
            return false;
        }
        Packet packet = (Packet)object;
        if (this.opcode != packet.opcode) {
            return false;
        }
        if (this.getSize() == packet.getSize()) return this.payload.equals(packet.payload);
        return false;
    }

    public PacketType getType() {
        return this.type;
    }

    public Buffer getPayload() {
        return this.payload;
    }

    public Packet(byte by, PacketType packetType, Buffer buffer) {
        this.opcode = by;
        this.type = packetType;
        this.payload = buffer;
    }

    public byte getOpcode() {
        return this.opcode;
    }

    public int hashCode() {
        int n = this.opcode;
        n = 31 * n + this.type.hashCode();
        n = 31 * n + this.payload.hashCode();
        return n;
    }

    public int getSize() {
        return this.payload.length();
    }
}
