/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.core.Controller
 *  io.trickle.core.Engine
 *  io.trickle.network.codec.packet.Packet
 *  io.trickle.network.codec.packet.PacketReader
 *  io.trickle.network.codec.packet.PacketType
 *  io.trickle.network.codec.packet.handler.PacketHandler
 *  io.trickle.task.Task
 *  io.trickle.task.TaskController
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
    public void handle(Packet packet, Handler handler) {
        PacketReader packetReader = PacketReader.create((Packet)packet);
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

    public PacketType getType() {
        return PacketType.INT_VARIABLE_SIZED;
    }

    public void handle(Packet packet) {
        throw new RuntimeException("Not-implemented");
    }

    public void handle(Promise promise, Packet packet) {
        throw new RuntimeException("Not-implemented");
    }
}
