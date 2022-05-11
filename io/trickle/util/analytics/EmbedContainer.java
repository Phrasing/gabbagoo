package io.trickle.util.analytics;

import io.vertx.core.json.JsonObject;

public class EmbedContainer {
   public JsonObject webhook;
   public boolean isSuccess;
   public boolean isMeta;

   public EmbedContainer(JsonObject var1) {
      this.isSuccess = true;
      this.isMeta = true;
      this.webhook = var1;
   }

   public EmbedContainer(boolean var1, JsonObject var2) {
      this.isSuccess = var1;
      this.webhook = var2;
      this.isMeta = false;
   }
}
