/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.AsyncResult
 *  io.vertx.core.CompositeFuture
 *  io.vertx.core.Future
 *  io.vertx.core.Promise
 *  io.vertx.core.Verticle
 *  io.vertx.core.Vertx
 *  io.vertx.core.json.JsonObject
 */
package io.trickle.core;

import io.trickle.account.AccountController;
import io.trickle.core.Controller;
import io.trickle.core.VertxSingleton;
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
import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Engine
implements Module {
    public Map<Controller, Module> modules;
    public Vertx vertx = VertxSingleton.INSTANCE.get();
    public JsonObject clientConfiguration;
    public SocketClient client;
    public CompletableFuture<Void> startPromise;
    public static boolean initialised = false;
    public static Engine INSTANCE;

    @Override
    public void terminate() {
        this.terminateModules();
        this.client.disconnect();
        this.vertx.undeploy(this.client.deploymentID());
    }

    public static boolean lambda$loadAsyncModules$15(Module module) {
        return module instanceof LoadableAsync;
    }

    public void lambda$initialise$3(CompositeFuture compositeFuture) {
        this.vertx.deployVerticle((Verticle)new WebhookWorker());
        this.vertx.deployVerticle((Verticle)new MetricsWorker());
        this.loadClientConfig().onComplete(this::lambda$initialise$2);
    }

    public void lambda$scheduleUpdates$8(Long l) {
        this.loadClientConfig();
    }

    public void lambda$initialise$0(Promise promise) {
        this.initModules();
    }

    public void scheduleUpdates() {
        VertxSingleton.INSTANCE.get().setPeriodic(TimeUnit.SECONDS.toMillis(30L), this::lambda$scheduleUpdates$8);
    }

    public void lambda$initialise$5(AsyncResult asyncResult) {
        if (asyncResult.succeeded()) {
            if (FileUtils.call()) {
                return;
            }
            CompletableFuture.runAsync(this::lambda$initialise$4);
            return;
        }
        if (asyncResult.cause() != null && asyncResult.cause().getMessage().equalsIgnoreCase("Invalid login details")) {
            Storage.setAccessKey("");
        }
        System.exit(1);
    }

    public void lambda$loadClientConfig$9(JsonObject jsonObject) {
        if (jsonObject == null) return;
        this.clientConfiguration = jsonObject;
    }

    @Override
    public void initialise() {
        this.vertx.deployVerticle((Verticle)this.client).onSuccess(this::lambda$initialise$6).onFailure(Engine::lambda$initialise$7);
    }

    public static boolean lambda$unloadModules$13(Module module) {
        return module instanceof Flushable;
    }

    public void install(Controller controller, Module module) {
        this.modules.put(controller, module);
    }

    public void lambda$initialise$4() {
        if (initialised) return;
        initialised = true;
        this.build();
        this.loadModules();
        this.loadAsyncModules().onSuccess(this::lambda$initialise$1).onSuccess(this::lambda$initialise$3).onFailure(Throwable::printStackTrace);
    }

    public CompletableFuture initialisePromise() {
        this.startPromise = new CompletableFuture();
        this.initialise();
        return this.startPromise;
    }

    public void unloadModules() {
        this.modules.values().stream().filter(Engine::lambda$unloadModules$13).map(Engine::lambda$unloadModules$14).forEach(Flushable::flush);
    }

    public JsonObject getClientConfiguration() {
        return this.clientConfiguration;
    }

    public SocketClient getClient() {
        return this.client;
    }

    public void lambda$initialise$2(AsyncResult asyncResult) {
        System.out.println("Fetched client patches of version: " + this.clientConfiguration.getString("version", String.valueOf(this.clientConfiguration.hashCode())));
        this.scheduleUpdates();
        if (this.startPromise == null) return;
        this.startPromise.complete(null);
    }

    public CompositeFuture loadAsyncModules() {
        List list = this.modules.values().stream().filter(Engine::lambda$loadAsyncModules$15).map(Engine::lambda$loadAsyncModules$16).map(LoadableAsync::load).collect(Collectors.toList());
        return CompositeFuture.all(list);
    }

    public void build() {
        this.install(Controller.TASK, new TaskController(this.vertx));
        this.install(Controller.PROXY, new ProxyController(this.vertx));
        this.install(Controller.PROXY_CAPTCHA, new ProxyController(this.vertx, "/harvester_proxies.txt"));
        this.install(Controller.ACCOUNT, new AccountController(this.vertx));
        this.install(Controller.TOKEN, new TokenController());
        this.install(Controller.LOGIN, new LoginController(this.vertx));
    }

    public static Loadable lambda$loadModules$12(Module module) {
        return (Loadable)((Object)module);
    }

    public void initModules() {
        this.modules.values().forEach(Module::initialise);
    }

    public void lambda$initialise$6(String string) {
        this.client.login().onComplete(this::lambda$initialise$5);
    }

    public static Flushable lambda$unloadModules$14(Module module) {
        return (Flushable)((Object)module);
    }

    public static Future lambda$loadClientConfig$10(JsonObject jsonObject) {
        return Future.succeededFuture();
    }

    public static boolean lambda$loadModules$11(Module module) {
        return module instanceof Loadable;
    }

    public Future loadClientConfig() {
        return VertxSingleton.INSTANCE.getLocalClient().fetchUpdates().onSuccess(this::lambda$loadClientConfig$9).compose(Engine::lambda$loadClientConfig$10);
    }

    public Module getModule(Controller controller) {
        return this.modules.get((Object)controller);
    }

    public static LoadableAsync lambda$loadAsyncModules$16(Module module) {
        return (LoadableAsync)((Object)module);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static Engine get() {
        if (INSTANCE != null) return INSTANCE;
        Class<Engine> clazz = Engine.class;
        synchronized (Engine.class) {
            INSTANCE = new Engine();
            // ** MonitorExit[var0] (shouldn't be in output)
            return INSTANCE;
        }
    }

    public void loadModules() {
        this.modules.values().stream().filter(Engine::lambda$loadModules$11).map(Engine::lambda$loadModules$12).forEach(Loadable::load);
    }

    public void lambda$initialise$1(CompositeFuture compositeFuture) {
        this.vertx.executeBlocking(this::lambda$initialise$0);
    }

    public static void lambda$initialise$7(Throwable throwable) {
        System.out.println("Failed connecting to trickle server");
        System.exit(-1);
    }

    public Engine() {
        this.modules = new HashMap<Controller, Module>();
        this.client = new SocketClient();
        this.clientConfiguration = new JsonObject().put("version", (Object)"default");
    }

    public void terminateModules() {
        this.modules.values().forEach(Module::terminate);
    }
}

