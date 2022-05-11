package io.trickle.core.actor;

import java.util.concurrent.CompletableFuture;

public interface Actor {
   CompletableFuture run();
}
