package io.trickle.network.codec.packet.handler;

import io.trickle.core.Controller;
import io.trickle.core.Engine;
import io.trickle.network.codec.packet.Packet;
import io.trickle.network.codec.packet.PacketReader;
import io.trickle.network.codec.packet.PacketType;
import io.trickle.task.Task;
import io.trickle.task.TaskController;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import java.util.Iterator;
import java.util.List;

public class StatsHandler implements PacketHandler {
   public void handle(Packet var1, Handler var2) {
      PacketReader var3 = PacketReader.create(var1);
      int var4 = var3.readInt();

      try {
         List var5 = ((TaskController)Engine.get().getModule(Controller.TASK)).getTasks();

         Task var7;
         for(Iterator var6 = var5.iterator(); var6.hasNext(); var7 = (Task)var6.next()) {
         }
      } catch (Throwable var8) {
      }

   }

   public PacketType getType() {
      return PacketType.INT_VARIABLE_SIZED;
   }

   public void handle(Packet var1) {
      throw new RuntimeException("Not-implemented");
   }

   public void handle(Promise var1, Packet var2) {
      throw new RuntimeException("Not-implemented");
   }
}
