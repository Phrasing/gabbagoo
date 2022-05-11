/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.core.api.LoadableAsync
 *  io.trickle.core.api.Module
 *  io.trickle.proxy.Proxy
 *  io.trickle.proxy.RandomProxyProducer
 *  io.trickle.util.FileUtils
 *  io.trickle.util.Storage
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
    public AtomicInteger idxRand;
    public String fileName;
    public AtomicInteger idx = new AtomicInteger(0);
    public Vertx vertx;
    public static Logger logger = LogManager.getLogger(ProxyController.class);
    public static int PROXY_COUNT = 0;
    public String LOCK_IDENTITY;
    public List<Proxy> proxiesRandomized;
    public List<Proxy> proxies;
    public boolean doCount;

    public static int lambda$getProxyCyclic$3(int n, int n2) {
        return ++n2 < n ? n2 : 0;
    }

    public ProxyController(Vertx vertx, String string) {
        this.idxRand = new AtomicInteger(0);
        this.proxies = new ArrayList<Proxy>();
        this.proxiesRandomized = new ArrayList<Proxy>();
        this.vertx = vertx;
        this.fileName = string;
        this.doCount = false;
        this.LOCK_IDENTITY = UUID.randomUUID().toString();
    }

    public Proxy getProxyRandomCyclic() {
        int n = this.proxiesRandomized.size();
        return n == 1 ? this.proxiesRandomized.get(0) : this.proxiesRandomized.get(this.idxRand.getAndUpdate(arg_0 -> ProxyController.lambda$getProxyRandomCyclic$2(n, arg_0)));
    }

    public Future getProxy() {
        RandomProxyProducer randomProxyProducer = new RandomProxyProducer(this.proxies);
        this.vertx.sharedData().getLock(this.LOCK_IDENTITY, (Handler)randomProxyProducer);
        return randomProxyProducer.getProduct();
    }

    public int loadedProxies() {
        return this.proxies.size();
    }

    public List parseFile(String string) {
        return Arrays.stream(string.split("\n")).filter(Objects::nonNull).map(String::trim).map(ProxyController::lambda$parseFile$4).map(Proxy::fromArray).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public Proxy getProxy(int n) {
        if (!this.proxies.isEmpty()) int n2;
        return (n2 = this.proxies.size()) == 1 ? this.proxies.get(0) : this.proxies.get(n % n2);
        return null;
    }

    public static int lambda$getProxyRandomCyclic$2(int n, int n2) {
        return ++n2 < n ? n2 : 0;
    }

    public Future load() {
        FileSystem fileSystem = this.vertx.fileSystem();
        return fileSystem.readFile(Storage.CONFIG_PATH + this.fileName).otherwise(arg_0 -> this.lambda$load$0(fileSystem, arg_0)).map(Buffer::toString).map(this::parseFile).map(this.proxies::addAll).compose(this::lambda$load$1);
    }

    public void initialise() {
        logger.debug("Initialised.");
    }

    public Buffer lambda$load$0(FileSystem fileSystem, Throwable throwable) {
        logger.warn("Failed to find '{}'. Proceeding without...", (Object)this.fileName.replace("/", ""));
        FileUtils.createAndWrite((FileSystem)fileSystem, (String)(Storage.CONFIG_PATH + this.fileName), (Buffer)Buffer.buffer());
        return Buffer.buffer((String)"");
    }

    public Optional getProxyPlain() {
        int n = this.idx.get();
        if (n < this.proxies.size()) return Optional.ofNullable(this.proxies.get(n));
        return Optional.empty();
    }

    public static String[] lambda$parseFile$4(String string) {
        return string.split(":");
    }

    public void terminate() {
        this.proxies.clear();
    }

    public Future lambda$load$1(Boolean bl) {
        if (this.doCount) {
            PROXY_COUNT = this.proxies.size();
        }
        this.proxiesRandomized.addAll(this.proxies);
        Collections.shuffle(this.proxiesRandomized);
        return Future.succeededFuture();
    }

    public Proxy getProxyCyclic() {
        if (!this.proxies.isEmpty()) int n;
        return (n = this.proxies.size()) == 1 ? this.proxies.get(0) : this.proxies.get(this.idx.getAndUpdate(arg_0 -> ProxyController.lambda$getProxyCyclic$3(n, arg_0)));
        return null;
    }

    public ProxyController(Vertx vertx) {
        this.idxRand = new AtomicInteger(0);
        this.proxies = new ArrayList<Proxy>();
        this.proxiesRandomized = new ArrayList<Proxy>();
        this.vertx = vertx;
        this.fileName = "/proxies.txt";
        this.doCount = true;
        this.LOCK_IDENTITY = UUID.randomUUID().toString();
    }
}
