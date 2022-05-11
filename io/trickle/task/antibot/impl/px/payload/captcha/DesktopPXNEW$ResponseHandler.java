package io.trickle.task.antibot.impl.px.payload.captcha;

import io.vertx.ext.web.client.HttpResponse;

@FunctionalInterface
public interface DesktopPXNEW$ResponseHandler {
   Object handle(HttpResponse var1);
}
