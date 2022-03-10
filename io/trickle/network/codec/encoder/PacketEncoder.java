/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.Handler
 *  io.vertx.core.buffer.Buffer
 */
package io.trickle.network.codec.encoder;

import io.trickle.network.codec.packet.Packet;
import io.trickle.network.codec.packet.PacketType;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;

public class PacketEncoder
implements Handler {
    public Handler<Buffer> bufferHandler;

    public PacketEncoder(Handler handler) {
        this.bufferHandler = handler;
    }

    public void handle(Object object) {
        this.handle((Packet)object);
    }

    public void handle(Packet packet) {
        PacketType packetType = packet.getType();
        int n = packet.getSize();
        try {
            Buffer buffer = Buffer.buffer();
            buffer.appendByte(packet.getOpcode());
            if (packetType == PacketType.BYTE_VARIABLE_SIZED) {
                buffer.appendByte((byte)n);
            } else if (packetType == PacketType.SHORT_VARIABLE_SIZED) {
                buffer.appendShort((short)n);
            } else {
                buffer.appendInt(n);
            }
            buffer.appendBuffer(packet.getPayload());
            this.bufferHandler.handle((Object)buffer);
            return;
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}

