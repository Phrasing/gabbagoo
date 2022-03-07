/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.Handler
 *  io.vertx.core.Promise
 */
package io.trickle.network.codec.packet.handler;

import io.trickle.core.Controller;
import io.trickle.core.Engine;
import io.trickle.network.codec.packet.Packet;
import io.trickle.network.codec.packet.PacketReader;
import io.trickle.network.codec.packet.PacketType;
import io.trickle.network.codec.packet.handler.PacketHandler;
import io.trickle.task.Task;
import io.trickle.task.TaskController;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import java.util.Iterator;
import java.util.List;

public class StatsHandler
implements PacketHandler {
    @Override
    public PacketType getType() {
        return PacketType.INT_VARIABLE_SIZED;
    }

    @Override
    public void handle(Packet packet, Handler handler) {
        PacketReader packetReader = PacketReader.create(packet);
        int n = packetReader.readInt();
        try {
            List list = ((TaskController)Engine.get().getModule(Controller.TASK)).getTasks();
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                Task task = (Task)iterator.next();
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
    public void handle(Promise promise, Packet packet) {
        throw new RuntimeException("Not-implemented");
    }
}

