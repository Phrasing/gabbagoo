/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.buffer.Buffer
 */
package io.trickle.network.codec.packet;

import io.trickle.network.codec.packet.PacketType;
import io.vertx.core.buffer.Buffer;

public class Packet {
    public Buffer payload;
    public byte opcode;
    public PacketType type;

    public int hashCode() {
        int n = this.opcode;
        n = 31 * n + this.type.hashCode();
        return 31 * n + this.payload.hashCode();
    }

    public int getSize() {
        return this.payload.length();
    }

    public Buffer getPayload() {
        return this.payload;
    }

    public PacketType getType() {
        return this.type;
    }

    public byte getOpcode() {
        return this.opcode;
    }

    public Packet(byte by, PacketType packetType, Buffer buffer) {
        this.opcode = by;
        this.type = packetType;
        this.payload = buffer;
    }

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
}

