package io.trickle.task.antibot.impl.px.payload;

import io.vertx.core.MultiMap;

public interface Payload {
   MultiMap asForm();
}
