/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
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
    @Override
    public void handle(Promise promise, Packet packet) {
        PacketReader packetReader = PacketReader.create(packet);
        String string = packetReader.readString();
        byte by = packetReader.readByte();
        try {
            if (by == 1 && string != null && !string.isEmpty()) {
                promise.tryComplete();
                return;
            }
            switch (by) {
                case -1: {
                    promise.tryFail("Too many instances!");
                    return;
                }
                case 0: {
                    promise.tryFail("Invalid login details");
                    return;
                }
                case -2: {
                    if (string != null && !string.isEmpty() && !string.isBlank()) {
                        promise.tryFail(string);
                        return;
                    }
                    promise.tryFail("Error occurred on login");
                    return;
                }
            }
            return;
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    @Override
    public void handle(Packet packet) {
        throw new RuntimeException("Not-implemented");
    }

    @Override
    public PacketType getType() {
        return PacketType.INT_VARIABLE_SIZED;
    }

    @Override
    public void handle(Packet packet, Handler handler) {
        throw new RuntimeException("Not-implemented");
    }
}

