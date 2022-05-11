package io.trickle.task.sites.walmart.util;

import io.trickle.core.api.LoadableAsync;
import io.trickle.core.api.Module;
import io.trickle.task.antibot.impl.akamai.DeviceStoreController;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RefererStoreController implements Module, LoadableAsync {
   public static Logger logger = LogManager.getLogger(DeviceStoreController.class);
   public static String REFERER_DEVICE_PATH = "links.txt";
   public Vertx vertx;
   public List referers;

   public List parseFile(String var1) {
      return (List)Arrays.stream(var1.split("\n")).filter(Objects::nonNull).map(String::trim).collect(Collectors.toList());
   }

   public Future load() {
      FileSystem var1 = this.vertx.fileSystem();
      Future var10000 = var1.readFile("links.txt").otherwise(RefererStoreController::lambda$load$0).map(Buffer::toString).map(this::parseFile);
      List var10001 = this.referers;
      Objects.requireNonNull(var10001);
      return var10000.map(var10001::addAll).compose(RefererStoreController::lambda$load$1);
   }

   public String getRandomReferer() {
      Object var10000 = this.referers.get(ThreadLocalRandom.current().nextInt(this.referers.size()));
      return "https://www.walmart.com/ip/" + (String)var10000;
   }

   public static Future lambda$load$1(Boolean var0) {
      return Future.succeededFuture();
   }

   public void terminate() {
      logger.debug("Terminated.");
   }

   public static Buffer lambda$load$0(Throwable var0) {
      logger.warn("Failed to find '{}' (Suggested for Walmart!). Proceeding without...", "links.txt".replace("/", ""));
      return Buffer.buffer("");
   }

   public void initialise() {
      logger.debug("Initialised");
   }

   public RefererStoreController(Vertx var1) {
      this.vertx = var1;
      this.referers = new ArrayList();
   }
}
