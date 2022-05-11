/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.core.Controller
 *  io.trickle.core.actor.TaskActor
 *  io.trickle.task.antibot.impl.AntiBotServiceBase
 *  io.trickle.webclient.ClientType
 */
package io.trickle.task.antibot.impl.px;

import io.trickle.core.Controller;
import io.trickle.core.actor.TaskActor;
import io.trickle.task.antibot.impl.AntiBotServiceBase;
import io.trickle.webclient.ClientType;
import java.util.concurrent.CompletableFuture;

public abstract class PXTokenBase
extends AntiBotServiceBase {
    public PXTokenBase(TaskActor taskActor, ClientType clientType, Controller controller) {
        super(taskActor, clientType, controller);
    }

    public abstract CompletableFuture reInit();

    public abstract String getVid();

    public PXTokenBase(TaskActor taskActor) {
        super(taskActor);
    }

    public abstract boolean isTokenCaptcha();

    public abstract CompletableFuture solveCaptcha(String var1, String var2);

    public PXTokenBase(TaskActor taskActor, ClientType clientType) {
        super(taskActor, clientType);
    }

    public abstract CompletableFuture awaitInit();

    public abstract String getSid();
}
