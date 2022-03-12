/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.Handler
 *  io.vertx.core.Promise
 */
package io.trickle.network.codec.packet.handler;

import io.trickle.network.codec.packet.Packet;
import io.trickle.network.codec.packet.PacketType;
import io.trickle.network.codec.packet.handler.LoginHandler;
import io.vertx.core.Handler;
import io.vertx.core.Promise;

public interface PacketHandler {
    public static final PacketHandler[] HANDLERS = new PacketHandler[]{null, new LoginHandler()};

    public void handle(Promise var1, Packet var2);

    public void handle(Packet var1);

    public PacketType getType();

    public void handle(Packet var1, Handler var2);
}

