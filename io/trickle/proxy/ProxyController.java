package io.trickle.proxy;

import io.trickle.core.api.LoadableAsync;
import io.trickle.core.api.Module;
import io.trickle.util.FileUtils;
import io.trickle.util.Storage;
import io.vertx.core.Future;
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

public class ProxyController implements Module, LoadableAsync {
   public AtomicInteger idxRand = new AtomicInteger(0);
   public String fileName;
   public AtomicInteger idx = new AtomicInteger(0);
   public Vertx vertx;
   public static Logger logger = LogManager.getLogger(ProxyController.class);
   public static int PROXY_COUNT = 0;
   public String LOCK_IDENTITY;
   public List proxiesRandomized = new ArrayList();
   public List proxies = new ArrayList();
   public boolean doCount;

   public static int lambda$getProxyCyclic$3(int var0, int var1) {
      ++var1;
      return var1 < var0 ? var1 : 0;
   }

   public ProxyController(Vertx var1, String var2) {
      this.vertx = var1;
      this.fileName = var2;
      this.doCount = false;
      this.LOCK_IDENTITY = UUID.randomUUID().toString();
   }

   public Proxy getProxyRandomCyclic() {
      int var1 = this.proxiesRandomized.size();
      return var1 == 1 ? (Proxy)this.proxiesRandomized.get(0) : (Proxy)this.proxiesRandomized.get(this.idxRand.getAndUpdate(ProxyController::lambda$getProxyRandomCyclic$2));
   }

   public Future getProxy() {
      RandomProxyProducer var1 = new RandomProxyProducer(this.proxies);
      this.vertx.sharedData().getLock(this.LOCK_IDENTITY, var1);
      return var1.getProduct();
   }

   public int loadedProxies() {
      return this.proxies.size();
   }

   public List parseFile(String var1) {
      return (List)Arrays.stream(var1.split("\n")).filter(Objects::nonNull).map(String::trim).map(ProxyController::lambda$parseFile$4).map(Proxy::fromArray).filter(Objects::nonNull).collect(Collectors.toList());
   }

   public Proxy getProxy(int var1) {
      if (this.proxies.isEmpty()) {
         return null;
      } else {
         int var2 = this.proxies.size();
         return var2 == 1 ? (Proxy)this.proxies.get(0) : (Proxy)this.proxies.get(var1 % var2);
      }
   }

   public static int lambda$getProxyRandomCyclic$2(int var0, int var1) {
      ++var1;
      return var1 < var0 ? var1 : 0;
   }

   public Future load() {
      FileSystem var1 = this.vertx.fileSystem();
      Future var10000 = var1.readFile(Storage.CONFIG_PATH + this.fileName).otherwise(this::lambda$load$0).map(Buffer::toString).map(this::parseFile);
      List var10001 = this.proxies;
      Objects.requireNonNull(var10001);
      return var10000.map(var10001::addAll).compose(this::lambda$load$1);
   }

   public void initialise() {
      logger.debug("Initialised.");
   }

   public Buffer lambda$load$0(FileSystem var1, Throwable var2) {
      logger.warn("Failed to find '{}'. Proceeding without...", this.fileName.replace("/", ""));
      FileUtils.createAndWrite(var1, Storage.CONFIG_PATH + this.fileName, Buffer.buffer());
      return Buffer.buffer("");
   }

   public Optional getProxyPlain() {
      int var1 = this.idx.get();
      return var1 >= this.proxies.size() ? Optional.empty() : Optional.ofNullable((Proxy)this.proxies.get(var1));
   }

   public static String[] lambda$parseFile$4(String var0) {
      return var0.split(":");
   }

   public void terminate() {
      this.proxies.clear();
   }

   public Future lambda$load$1(Boolean var1) {
      if (this.doCount) {
         PROXY_COUNT = this.proxies.size();
      }

      this.proxiesRandomized.addAll(this.proxies);
      Collections.shuffle(this.proxiesRandomized);
      return Future.succeededFuture();
   }

   public Proxy getProxyCyclic() {
      if (this.proxies.isEmpty()) {
         return null;
      } else {
         int var1 = this.proxies.size();
         return var1 == 1 ? (Proxy)this.proxies.get(0) : (Proxy)this.proxies.get(this.idx.getAndUpdate(ProxyController::lambda$getProxyCyclic$3));
      }
   }

   public ProxyController(Vertx var1) {
      this.vertx = var1;
      this.fileName = "/proxies.txt";
      this.doCount = true;
      this.LOCK_IDENTITY = UUID.randomUUID().toString();
   }
}
