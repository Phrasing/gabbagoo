package io.trickle.core.api;

import io.vertx.core.Future;

public interface LoadableAsync {
   Future load();
}
