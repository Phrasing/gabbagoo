/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fuzzy.aycd.autosolve.AbstractAutoSolveManager
 *  com.fuzzy.aycd.autosolve.model.AutoSolveAccount
 *  io.trickle.harvester.autosolve.AutoSolve
 *  io.trickle.harvester.pooled.AbstractSharedHarvesterController
 *  io.trickle.harvester.pooled.AutosolveHarvester
 *  io.trickle.util.Storage
 *  io.trickle.util.concurrent.ContextCompletableFuture
 *  io.vertx.core.Handler
 *  io.vertx.core.Vertx
 *  okhttp3.OkHttpClient$Builder
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package io.trickle.harvester.pooled;

import com.fuzzy.aycd.autosolve.AbstractAutoSolveManager;
import com.fuzzy.aycd.autosolve.model.AutoSolveAccount;
import io.trickle.harvester.autosolve.AutoSolve;
import io.trickle.harvester.pooled.AbstractSharedHarvesterController;
import io.trickle.harvester.pooled.AutosolveHarvester;
import io.trickle.util.Storage;
import io.trickle.util.concurrent.ContextCompletableFuture;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import java.lang.invoke.LambdaMetafactory;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import okhttp3.OkHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AutosolveHarvesterController
extends AbstractSharedHarvesterController {
    public Vertx vertx;
    public static boolean $assertionsDisabled;
    public static Logger logger;
    public AtomicInteger c = new AtomicInteger(0);
    public static int SHARED_TOKEN_RATIO;
    public ContextCompletableFuture<Void> startFuture;
    public AbstractAutoSolveManager autoSolve;

    public void addNewHarvester() {
        AutosolveHarvester autosolveHarvester = new AutosolveHarvester(this.vertx, this.autoSolve);
        this.vertx.eventBus().localConsumer(autosolveHarvester.id(), (Handler)autosolveHarvester);
        this.harvesters.add(autosolveHarvester);
        logger.info("Added new autosolve harvester: {}", (Object)this.harvesters.size());
    }

    public CompletableFuture initAutosolve() {
        if (this.autoSolve.isActive()) {
            return CompletableFuture.completedFuture(null);
        }
        logger.info("Connecting to AutoSolve");
        if (this.startFuture != null) {
            if (!this.startFuture.isDone()) return CompletableFuture.completedFuture(null);
        }
        this.startFuture = new ContextCompletableFuture();
        this.connect();
        ContextCompletableFuture<Void> contextCompletableFuture = this.startFuture;
        if (!contextCompletableFuture.toCompletableFuture().isDone()) {
            ContextCompletableFuture<Void> contextCompletableFuture2 = contextCompletableFuture;
            return contextCompletableFuture2.exceptionally(Function.identity()).thenCompose(arg_0 -> AutosolveHarvesterController.async$initAutosolve(this, contextCompletableFuture2, 1, arg_0)).toCompletableFuture();
        }
        contextCompletableFuture.toCompletableFuture().join();
        logger.info("Connected Successfully");
        return CompletableFuture.completedFuture(null);
    }

    static {
        SHARED_TOKEN_RATIO = 500;
        $assertionsDisabled = !AutosolveHarvesterController.class.desiredAssertionStatus();
        logger = LogManager.getLogger(AutosolveHarvesterController.class);
    }

    public Optional shouldSwap(String string) {
        return Optional.empty();
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$initAutosolve(AutosolveHarvesterController var0, ContextCompletableFuture var1_1, int var2_2, Object var3_3) {
        switch (var2_2) {
            case 0: {
                if (var0.autoSolve.isActive()) {
                    return CompletableFuture.completedFuture(null);
                }
                AutosolveHarvesterController.logger.info("Connecting to AutoSolve");
                if (var0.startFuture != null) {
                    if (var0.startFuture.isDone() == false) return CompletableFuture.completedFuture(null);
                }
                var0.startFuture = new ContextCompletableFuture();
                var0.connect();
                v0 = var0.startFuture;
                if (!v0.toCompletableFuture().isDone()) {
                    var1_1 = v0;
                    return var1_1.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$initAutosolve(io.trickle.harvester.pooled.AutosolveHarvesterController io.trickle.util.concurrent.ContextCompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((AutosolveHarvesterController)var0, var1_1, (int)1)).toCompletableFuture();
                }
                ** GOTO lbl17
            }
            case 1: {
                v0 = var1_1;
lbl17:
                // 2 sources

                v0.toCompletableFuture().join();
                AutosolveHarvesterController.logger.info("Connected Successfully");
                return CompletableFuture.completedFuture(null);
            }
        }
        throw new IllegalArgumentException();
    }

    public void lambda$connect$0(String string, String string2) {
        AutoSolveAccount autoSolveAccount = AutoSolveAccount.of((String)string, (String)string2);
        if (!$assertionsDisabled && !autoSolveAccount.isValid()) {
            throw new AssertionError();
        }
        int n = 0;
        while (true) {
            if (n >= 10) {
                logger.warn("Failed to connect to AutoSolve");
                this.startFuture.completeExceptionally((Throwable)new Exception("Failed to connect to AutoSolve"));
                System.exit(-1);
                return;
            }
            try {
                logger.info("Connecting AutoSolve...");
                this.autoSolve.load(autoSolveAccount);
                if (this.autoSolve.isConnected()) {
                    this.startFuture.complete(null);
                    return;
                }
                Thread.sleep(3000L);
            }
            catch (Throwable throwable) {
                logger.error("Failed to connect to autosolve: {}", (Object)throwable.getMessage());
            }
            ++n;
        }
    }

    public CompletableFuture initialise() {
        if (this.c.getAndIncrement() % 500 != 0) return this.initAutosolve();
        this.addNewHarvester();
        return this.initAutosolve();
    }

    public AutosolveHarvesterController(Vertx vertx) {
        this.vertx = vertx;
        this.autoSolve = new AutoSolve(new OkHttpClient.Builder(), "Trickle-4ae3fa8b-26fa-4001-8582-8fd27a7beb7e");
    }

    public void connect() {
        String string = Storage.AYCD_ACCESS_TOKEN;
        String string2 = Storage.AYCD_API_KEY;
        CompletableFuture.runAsync(() -> this.lambda$connect$0(string, string2));
    }
}
