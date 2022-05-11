package io.trickle.task.antibot.impl.px;

import io.trickle.core.Controller;
import io.trickle.core.actor.TaskActor;
import io.trickle.task.antibot.impl.AntiBotServiceBase;
import io.trickle.webclient.ClientType;
import java.util.concurrent.CompletableFuture;

public abstract class PXTokenBase extends AntiBotServiceBase {
   public PXTokenBase(TaskActor var1, ClientType var2, Controller var3) {
      super(var1, var2, var3);
   }

   public abstract CompletableFuture reInit();

   public abstract String getVid();

   public PXTokenBase(TaskActor var1) {
      super(var1);
   }

   public abstract boolean isTokenCaptcha();

   public abstract CompletableFuture solveCaptcha(String var1, String var2);

   public PXTokenBase(TaskActor var1, ClientType var2) {
      super(var1, var2);
   }

   public abstract CompletableFuture awaitInit();

   public abstract String getSid();
}
