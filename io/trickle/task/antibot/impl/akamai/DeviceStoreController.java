package io.trickle.task.antibot.impl.akamai;

import io.trickle.core.api.LoadableAsync;
import io.trickle.core.api.Module;
import io.trickle.util.Storage;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;
import io.vertx.core.json.JsonArray;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DeviceStoreController implements Module, LoadableAsync {
   public JsonArray akDevices;
   public Vertx vertx;
   public static String AK_DEVICE_PATH = "/akDevices.json";
   public static Logger logger = LogManager.getLogger(DeviceStoreController.class);

   public JsonArray getAkDevices() {
      return this.akDevices;
   }

   public void initialise() {
      logger.debug("Initialised");
   }

   public Future lambda$load$0(JsonArray var1) {
      this.akDevices = var1;
      if (this.akDevices != null && !this.akDevices.isEmpty()) {
         logger.info("Loaded {} akamai devices", this.akDevices.size());
         return Future.succeededFuture();
      } else {
         return Future.failedFuture("Failed to read /akDevices.json");
      }
   }

   public void terminate() {
      logger.debug("Terminated.");
   }

   public Future load() {
      FileSystem var1 = this.vertx.fileSystem();
      return var1.readFile(Storage.CONFIG_PATH + "/akDevices.json").map(Buffer::toJsonArray).compose(this::lambda$load$0);
   }

   public DeviceStoreController(Vertx var1) {
      this.vertx = var1;
   }
}
