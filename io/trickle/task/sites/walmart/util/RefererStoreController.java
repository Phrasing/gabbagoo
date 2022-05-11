/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.core.api.LoadableAsync
 *  io.trickle.core.api.Module
 *  io.trickle.task.antibot.impl.akamai.DeviceStoreController
 *  io.vertx.core.Future
 *  io.vertx.core.Vertx
 *  io.vertx.core.buffer.Buffer
 *  io.vertx.core.file.FileSystem
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
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

public class RefererStoreController
implements Module,
LoadableAsync {
    public static Logger logger;
    public static String REFERER_DEVICE_PATH;
    public Vertx vertx;
    public List<String> referers;

    public List parseFile(String string) {
        return Arrays.stream(string.split("\n")).filter(Objects::nonNull).map(String::trim).collect(Collectors.toList());
    }

    public Future load() {
        FileSystem fileSystem = this.vertx.fileSystem();
        return fileSystem.readFile("links.txt").otherwise(RefererStoreController::lambda$load$0).map(Buffer::toString).map(this::parseFile).map(this.referers::addAll).compose(RefererStoreController::lambda$load$1);
    }

    public String getRandomReferer() {
        return "https://www.walmart.com/ip/" + this.referers.get(ThreadLocalRandom.current().nextInt(this.referers.size()));
    }

    public static Future lambda$load$1(Boolean bl) {
        return Future.succeededFuture();
    }

    public void terminate() {
        logger.debug("Terminated.");
    }

    public static Buffer lambda$load$0(Throwable throwable) {
        logger.warn("Failed to find '{}' (Suggested for Walmart!). Proceeding without...", (Object)"links.txt".replace("/", ""));
        return Buffer.buffer((String)"");
    }

    public void initialise() {
        logger.debug("Initialised");
    }

    public RefererStoreController(Vertx vertx) {
        this.vertx = vertx;
        this.referers = new ArrayList<String>();
    }

    static {
        REFERER_DEVICE_PATH = "links.txt";
        logger = LogManager.getLogger(DeviceStoreController.class);
    }
}
