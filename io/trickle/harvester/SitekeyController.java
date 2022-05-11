package io.trickle.harvester;

import io.trickle.core.api.LoadableAsync;
import io.trickle.core.api.Module;
import io.trickle.util.Storage;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SitekeyController implements Module, LoadableAsync {
   public JsonObject SITEKEY_STORAGE;
   public static Logger logger = LogManager.getLogger(SitekeyController.class);
   public static String SITEKEY_PATH = "/sitekeys.json";
   public Vertx vertx;

   public Future load() {
      FileSystem var1 = this.vertx.fileSystem();
      return var1.readFile(Storage.CONFIG_PATH + "/sitekeys.json").otherwise(SitekeyController::lambda$load$0).map(Buffer::toString).map(this::parseFile).compose(this::lambda$load$1);
   }

   public Future lambda$load$1(JsonObject var1) {
      logger.debug("Loaded sitekeys");
      this.SITEKEY_STORAGE = var1;
      return Future.succeededFuture();
   }

   public static Buffer lambda$load$0(Throwable var0) {
      logger.warn("Failed to find '{}' (Required for Shopify!). Proceeding without...", "/sitekeys.json".replace("/", ""));
      return Buffer.buffer("{}");
   }

   public void terminate() {
      FileSystem var1 = this.vertx.fileSystem();
      var1.writeFile("/sitekeys.json", this.SITEKEY_STORAGE.toBuffer(), SitekeyController::lambda$terminate$2);
   }

   public JsonObject parseFile(String var1) {
      return new JsonObject(var1);
   }

   public static void lambda$terminate$2(AsyncResult var0) {
      if (var0.succeeded()) {
         logger.debug("Updated sitekeys.json");
      } else {
         logger.error("Unable to update sitekeys.json");
      }

   }

   public void initialise() {
      logger.debug("Initialised.");
   }

   public SitekeyController(Vertx var1) {
      this.vertx = var1;
   }

   public JsonObject getSitekeys() {
      return this.SITEKEY_STORAGE;
   }
}
