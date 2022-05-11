/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.network.codec.packet.Packet
 *  io.trickle.network.codec.packet.PacketReader
 *  io.trickle.network.codec.packet.PacketType
 *  io.trickle.network.codec.packet.handler.PacketHandler
 *  io.vertx.core.Handler
 *  io.vertx.core.Promise
 */
package io.trickle.network.codec.packet.handler;

import io.trickle.network.codec.packet.Packet;
import io.trickle.network.codec.packet.PacketReader;
import io.trickle.network.codec.packet.PacketType;
import io.trickle.network.codec.packet.handler.PacketHandler;
import io.vertx.core.Handler;
import io.vertx.core.Promise;

public class LoginHandler
implements PacketHandler {
    public void handle(Promise promise, Packet packet) {
        PacketReader packetReader = PacketReader.create((Packet)packet);
        String string = packetReader.readString();
        byte by = packetReader.readByte();
        try {
            if (by == 1 && string != null && !string.isEmpty()) {
                promise.tryComplete();
            } else {
                switch (by) {
                    case -1: {
                        promise.tryFail("Too many instances!");
                        break;
                    }
                    case 0: {
                        promise.tryFail("Invalid login details");
                        break;
                    }
                    case -2: {
                        if (string != null && !string.isEmpty() && !string.isBlank()) {
                            promise.tryFail(string);
                            break;
                        }
                        promise.tryFail("Error occurred on login");
                        break;
                    }
                }
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public void handle(Packet packet) {
        throw new RuntimeException("Not-implemented");
    }

    public PacketType getType() {
        return PacketType.INT_VARIABLE_SIZED;
    }

    public void handle(Packet packet, Handler handler) {
        throw new RuntimeException("Not-implemented");
    }
}
