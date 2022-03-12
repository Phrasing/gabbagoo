/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.Future
 *  io.vertx.core.Handler
 *  io.vertx.core.Vertx
 *  io.vertx.core.buffer.Buffer
 *  io.vertx.core.file.FileSystem
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package io.trickle.proxy;

import io.trickle.core.api.LoadableAsync;
import io.trickle.core.api.Module;
import io.trickle.proxy.Proxy;
import io.trickle.proxy.RandomProxyProducer;
import io.trickle.util.FileUtils;
import io.trickle.util.Storage;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProxyController
implements Module,
LoadableAsync {
    public static Logger logger = LogManager.getLogger(ProxyController.class);
    public String fileName;
    public List<Proxy> proxies;
    public List<Proxy> proxiesRandomized;
    public AtomicInteger cRandomized;
    public Vertx vertx;
    public String LOCK_IDENTITY;
    public AtomicInteger c = new AtomicInteger(0);

    public Future lambda$load$1(Boolean bl) {
        this.proxiesRandomized = new ArrayList<Proxy>(this.proxies);
        Collections.shuffle(this.proxiesRandomized);
        return Future.succeededFuture();
    }

    @Override
    public Future load() {
        FileSystem fileSystem = this.vertx.fileSystem();
        return fileSystem.readFile(Storage.CONFIG_PATH + this.fileName).otherwise(arg_0 -> this.lambda$load$0(fileSystem, arg_0)).map(Buffer::toString).map(this::parseFile).map(this.proxies::addAll).compose(this::lambda$load$1);
    }

    @Override
    public void initialise() {
        logger.debug("Initialised.");
    }

    public ProxyController(Vertx vertx) {
        this.cRandomized = new AtomicInteger(0);
        this.proxies = new ArrayList<Proxy>();
        this.vertx = vertx;
        this.fileName = "/proxies.txt";
        this.LOCK_IDENTITY = UUID.randomUUID().toString();
    }

    public int loadedProxies() {
        return this.proxies.size();
    }

    public Buffer lambda$load$0(FileSystem fileSystem, Throwable throwable) {
        logger.warn("Failed to find '{}'. Proceeding without...", (Object)this.fileName.replace("/", ""));
        FileUtils.createAndWrite(fileSystem, Storage.CONFIG_PATH + this.fileName, Buffer.buffer());
        return Buffer.buffer((String)"");
    }

    public ProxyController(Vertx vertx, String string) {
        this.cRandomized = new AtomicInteger(0);
        this.proxies = new ArrayList<Proxy>();
        this.vertx = vertx;
        this.fileName = string;
        this.LOCK_IDENTITY = UUID.randomUUID().toString();
    }

    public Proxy getProxyCyclic() {
        Proxy proxy;
        if (this.proxies.isEmpty()) {
            return null;
        }
        int n = this.proxies.size();
        if (n == 1) {
            proxy = this.proxies.get(0);
            return proxy;
        }
        proxy = this.proxies.get(this.c.getAndUpdate(arg_0 -> ProxyController.lambda$getProxyCyclic$3(n, arg_0)));
        return proxy;
    }

    public Proxy getProxyRandomCyclic() {
        Proxy proxy;
        int n = this.proxiesRandomized.size();
        if (n == 1) {
            proxy = this.proxiesRandomized.get(0);
            return proxy;
        }
        proxy = this.proxiesRandomized.get(this.cRandomized.getAndUpdate(arg_0 -> ProxyController.lambda$getProxyRandomCyclic$2(n, arg_0)));
        return proxy;
    }

    @Override
    public void terminate() {
        this.proxies.clear();
    }

    public List parseFile(String string) {
        return Arrays.stream(string.split("\n")).filter(Objects::nonNull).map(String::trim).map(ProxyController::lambda$parseFile$4).map(Proxy::fromArray).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public Future getProxy() {
        RandomProxyProducer randomProxyProducer = new RandomProxyProducer(this.proxies);
        this.vertx.sharedData().getLock(this.LOCK_IDENTITY, (Handler)randomProxyProducer);
        return randomProxyProducer.getProduct();
    }

    public static int lambda$getProxyRandomCyclic$2(int n, int n2) {
        if (++n2 >= n) return 0;
        int n3 = n2;
        return n3;
    }

    public static int lambda$getProxyCyclic$3(int n, int n2) {
        if (++n2 >= n) return 0;
        int n3 = n2;
        return n3;
    }

    public static String[] lambda$parseFile$4(String string) {
        return string.split(":");
    }

    public Optional getProxyPlain() {
        int n = this.c.get();
        if (n < this.proxies.size()) return Optional.ofNullable(this.proxies.get(n));
        return Optional.empty();
    }
}

