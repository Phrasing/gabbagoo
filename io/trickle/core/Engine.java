package io.trickle.core;

import io.trickle.account.AccountController;
import io.trickle.core.api.Flushable;
import io.trickle.core.api.Loadable;
import io.trickle.core.api.LoadableAsync;
import io.trickle.core.api.Module;
import io.trickle.harvester.LoginController;
import io.trickle.harvester.TokenController;
import io.trickle.network.SocketClient;
import io.trickle.proxy.ProxyController;
import io.trickle.task.TaskController;
import io.trickle.util.FileUtils;
import io.trickle.util.Storage;
import io.trickle.util.analytics.MetricsWorker;
import io.trickle.util.analytics.WebhookWorker;
import io.trickle.util.analytics.cli.CLIMonitor;
import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Engine implements Module {
   public SocketClient client;
   public static boolean initialised = false;
   public static Engine INSTANCE;
   public CompletableFuture startPromise;
   public Vertx vertx;
   public JsonObject clientConfiguration;
   public Map modules;

   public void loadModules() {
      this.modules.values().stream().filter(Engine::lambda$loadModules$11).map(Engine::lambda$loadModules$12).forEach(Loadable::load);
   }

   public void unloadModules() {
      this.modules.values().stream().filter(Engine::lambda$unloadModules$13).map(Engine::lambda$unloadModules$14).forEach(Flushable::flush);
   }

   public static LoadableAsync lambda$loadAsyncModules$16(Module var0) {
      return (LoadableAsync)var0;
   }

   public void terminateModules() {
      this.modules.values().forEach(Module::terminate);
   }

   public void lambda$initialise$6(String var1) {
      this.client.login().onComplete(this::lambda$initialise$5);
   }

   public CompletableFuture initialisePromise() {
      this.startPromise = new CompletableFuture();
      this.initialise();
      return this.startPromise;
   }

   public static Engine get() {
      if (INSTANCE == null) {
         Class var0 = Engine.class;
         synchronized(Engine.class) {
            INSTANCE = new Engine();
         }
      }

      return INSTANCE;
   }

   public CompositeFuture loadAsyncModules() {
      List var1 = (List)this.modules.values().stream().filter(Engine::lambda$loadAsyncModules$15).map(Engine::lambda$loadAsyncModules$16).map(LoadableAsync::load).collect(Collectors.toList());
      return CompositeFuture.all(var1);
   }

   public static boolean lambda$loadAsyncModules$15(Module var0) {
      return var0 instanceof LoadableAsync;
   }

   public void lambda$initialise$2(AsyncResult var1) {
      JsonObject var10001 = this.clientConfiguration;
      System.out.println("Fetched client patches of version: " + var10001.getString("version", String.valueOf(this.clientConfiguration.hashCode())));
      this.scheduleUpdates();
      if (this.startPromise != null) {
         this.startPromise.complete((Object)null);
      }

   }

   public void lambda$initialise$3(CompositeFuture var1) {
      this.vertx.deployVerticle(new WebhookWorker());
      this.vertx.deployVerticle(new MetricsWorker());
      this.vertx.deployVerticle(new CLIMonitor());
      this.loadClientConfig().onComplete(this::lambda$initialise$2);
   }

   public SocketClient getClient() {
      return this.client;
   }

   public static Flushable lambda$unloadModules$14(Module var0) {
      return (Flushable)var0;
   }

   public void scheduleUpdates() {
      VertxSingleton.INSTANCE.get().setPeriodic(TimeUnit.SECONDS.toMillis(30L), this::lambda$scheduleUpdates$8);
   }

   public static Loadable lambda$loadModules$12(Module var0) {
      return (Loadable)var0;
   }

   public static void lambda$initialise$7(Throwable var0) {
      System.out.println("Failed connecting to trickle server");
      System.exit(-1);
   }

   public void initModules() {
      this.modules.values().forEach(Module::initialise);
   }

   public void lambda$scheduleUpdates$8(Long var1) {
      this.loadClientConfig();
   }

   public void lambda$initialise$4() {
      if (!initialised) {
         initialised = true;
         this.build();
         this.loadModules();
         this.loadAsyncModules().onSuccess(this::lambda$initialise$1).onSuccess(this::lambda$initialise$3).onFailure(Throwable::printStackTrace);
      }

   }

   public Engine() {
      this.vertx = VertxSingleton.INSTANCE.get();
      this.modules = new HashMap();
      this.client = new SocketClient();
      this.clientConfiguration = (new JsonObject()).put("version", "default");
   }

   public static Future lambda$loadClientConfig$10(JsonObject var0) {
      return Future.succeededFuture();
   }

   public void terminate() {
      this.terminateModules();
   }

   public void lambda$initialise$5(AsyncResult var1) {
      if (var1.succeeded()) {
         if (FileUtils.call()) {
            return;
         }

         CompletableFuture.runAsync(this::lambda$initialise$4);
      } else {
         if (var1.cause() != null && var1.cause().getMessage().equalsIgnoreCase("Invalid login details")) {
            Storage.setAccessKey("");
         }

         System.exit(1);
      }

   }

   public Future loadClientConfig() {
      return VertxSingleton.INSTANCE.getLocalClient().fetchUpdates().onSuccess(this::lambda$loadClientConfig$9).compose(Engine::lambda$loadClientConfig$10);
   }

   public Module getModule(Controller var1) {
      return (Module)this.modules.get(var1);
   }

   public JsonObject getClientConfiguration() {
      return this.clientConfiguration;
   }

   public static boolean lambda$unloadModules$13(Module var0) {
      return var0 instanceof Flushable;
   }

   public static boolean lambda$loadModules$11(Module var0) {
      return var0 instanceof Loadable;
   }

   public void lambda$initialise$0(Promise var1) {
      this.initModules();
   }

   public void initialise() {
      CompletableFuture.runAsync(this::lambda$initialise$4);
   }

   public void install(Controller var1, Module var2) {
      this.modules.put(var1, var2);
   }

   public void lambda$initialise$1(CompositeFuture var1) {
      this.vertx.executeBlocking(this::lambda$initialise$0);
   }

   public void lambda$loadClientConfig$9(JsonObject var1) {
      if (var1 != null) {
         this.clientConfiguration = var1;
      }

   }

   public void build() {
      this.install(Controller.TASK, new TaskController(this.vertx));
      this.install(Controller.PROXY, new ProxyController(this.vertx));
      this.install(Controller.PROXY_CAPTCHA, new ProxyController(this.vertx, "/harvester_proxies.txt"));
      this.install(Controller.ACCOUNT, new AccountController(this.vertx));
      this.install(Controller.TOKEN, new TokenController());
      this.install(Controller.LOGIN, new LoginController(this.vertx));
   }
}
