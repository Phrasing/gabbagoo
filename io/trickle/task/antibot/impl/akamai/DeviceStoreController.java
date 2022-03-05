/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.Future
 *  io.vertx.core.Vertx
 *  io.vertx.core.buffer.Buffer
 *  io.vertx.core.file.FileSystem
 *  io.vertx.core.json.JsonArray
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
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

public class DeviceStoreController
implements Module,
LoadableAsync {
    public JsonArray akDevices;
    public static String AK_DEVICE_PATH = "/akDevices.json";
    public static Logger logger = LogManager.getLogger(DeviceStoreController.class);
    public Vertx vertx;

    @Override
    public Future load() {
        FileSystem fileSystem = this.vertx.fileSystem();
        return fileSystem.readFile(Storage.CONFIG_PATH + "/akDevices.json").map(Buffer::toJsonArray).compose(this::lambda$load$0);
    }

    @Override
    public void terminate() {
        logger.debug("Terminated.");
    }

    public DeviceStoreController(Vertx vertx) {
        this.vertx = vertx;
    }

    public JsonArray getAkDevices() {
        return this.akDevices;
    }

    @Override
    public void initialise() {
        logger.debug("Initialised");
    }

    public Future lambda$load$0(JsonArray jsonArray) {
        this.akDevices = jsonArray;
        if (this.akDevices == null) return Future.failedFuture((String)"Failed to read /akDevices.json");
        if (this.akDevices.isEmpty()) {
            return Future.failedFuture((String)"Failed to read /akDevices.json");
        }
        logger.info("Loaded {} akamai devices", (Object)this.akDevices.size());
        return Future.succeededFuture();
    }
}

