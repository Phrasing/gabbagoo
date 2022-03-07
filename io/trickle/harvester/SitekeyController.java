/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.AsyncResult
 *  io.vertx.core.Future
 *  io.vertx.core.Vertx
 *  io.vertx.core.buffer.Buffer
 *  io.vertx.core.file.FileSystem
 *  io.vertx.core.json.JsonObject
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
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

public class SitekeyController
implements Module,
LoadableAsync {
    public static Logger logger;
    public JsonObject SITEKEY_STORAGE;
    public Vertx vertx;
    public static String SITEKEY_PATH;

    @Override
    public void initialise() {
        logger.debug("Initialised.");
    }

    public Future lambda$load$1(JsonObject jsonObject) {
        logger.debug("Loaded sitekeys");
        this.SITEKEY_STORAGE = jsonObject;
        return Future.succeededFuture();
    }

    @Override
    public void terminate() {
        FileSystem fileSystem = this.vertx.fileSystem();
        fileSystem.writeFile("/sitekeys.json", this.SITEKEY_STORAGE.toBuffer(), SitekeyController::lambda$terminate$2);
    }

    public JsonObject parseFile(String string) {
        return new JsonObject(string);
    }

    public static Buffer lambda$load$0(Throwable throwable) {
        logger.warn("Failed to find '{}' (Required for Shopify!). Proceeding without...", (Object)"/sitekeys.json".replace("/", ""));
        return Buffer.buffer((String)"{}");
    }

    public SitekeyController(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public Future load() {
        FileSystem fileSystem = this.vertx.fileSystem();
        return fileSystem.readFile(Storage.CONFIG_PATH + "/sitekeys.json").otherwise(SitekeyController::lambda$load$0).map(Buffer::toString).map(this::parseFile).compose(this::lambda$load$1);
    }

    public static void lambda$terminate$2(AsyncResult asyncResult) {
        if (asyncResult.succeeded()) {
            logger.debug("Updated sitekeys.json");
            return;
        }
        logger.error("Unable to update sitekeys.json");
    }

    static {
        SITEKEY_PATH = "/sitekeys.json";
        logger = LogManager.getLogger(SitekeyController.class);
    }

    public JsonObject getSitekeys() {
        return this.SITEKEY_STORAGE;
    }
}

