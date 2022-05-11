package io.trickle.network.codec.packet.handler;

import io.trickle.network.codec.packet.Packet;
import io.trickle.network.codec.packet.PacketType;
import io.vertx.core.Handler;
import io.vertx.core.Promise;

public interface PacketHandler {
   PacketHandler[] HANDLERS = new PacketHandler[]{null, new LoginHandler()};

   void handle(Packet var1, Handler var2);

   void handle(Promise var1, Packet var2);

   void handle(Packet var1);

   PacketType getType();
}
