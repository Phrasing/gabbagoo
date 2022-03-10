/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.AsyncResult
 *  io.vertx.core.Handler
 *  io.vertx.core.Promise
 *  io.vertx.core.Verticle
 *  io.vertx.core.Vertx
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package io.trickle.harvester.pooled;

import io.trickle.harvester.pooled.AbstractSharedHarvesterController;
import io.trickle.harvester.pooled.BrowserSharedHarvester;
import io.trickle.harvester.pooled.SharedHarvester;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import java.lang.invoke.LambdaMetafactory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BrowserHarvesterController
extends AbstractSharedHarvesterController {
    public static Logger logger = LogManager.getLogger(BrowserHarvesterController.class);
    public Vertx vertx;
    public static Predicate<SharedHarvester> removePredicate = BrowserHarvesterController::lambda$static$0;
    public List<SharedHarvester> passedHarvesters;
    public int count;
    public AtomicInteger passedCounter;

    public CompletableFuture initHarvesters() {
        if (this.count == this.harvesters.size()) {
            return CompletableFuture.completedFuture(null);
        }
        int n = this.count - this.harvesters.size();
        logger.info("Initialising {} harvesters", (Object)n);
        int n2 = 0;
        while (n2 < n) {
            try {
                BrowserSharedHarvester browserSharedHarvester = new BrowserSharedHarvester(n2);
                this.harvesters.add(browserSharedHarvester);
                CompletableFuture completableFuture = this.vertx.deployVerticle((Verticle)browserSharedHarvester).onComplete(arg_0 -> this.lambda$initHarvesters$3(browserSharedHarvester, arg_0)).toCompletionStage().toCompletableFuture();
                logger.info("Waiting for harvester[{}] to start...", (Object)n2);
                CompletableFuture completableFuture2 = completableFuture;
                if (!completableFuture2.isDone()) {
                    CompletableFuture completableFuture3 = completableFuture2;
                    return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> BrowserHarvesterController.async$initHarvesters(this, n, n2, browserSharedHarvester, completableFuture, completableFuture3, 1, arg_0));
                }
                completableFuture2.join();
            }
            catch (Throwable throwable) {
                logger.warn("Error occurred on initialisation stage: {}", (Object)throwable.getMessage());
                return CompletableFuture.failedFuture(throwable);
            }
            ++n2;
        }
        return CompletableFuture.completedFuture(null);
    }

    public BrowserHarvesterController(Vertx vertx, int n) {
        this.count = n;
        this.vertx = vertx;
        this.passedCounter = new AtomicInteger(0);
        this.passedHarvesters = new ArrayList<SharedHarvester>();
        this.startCounter();
    }

    public static int lambda$shouldSwap$2(int n, int n2) {
        if (++n2 >= n) return 0;
        int n3 = n2;
        return n3;
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$initHarvesters(BrowserHarvesterController var0, int var1_1, int var2_2, BrowserSharedHarvester var3_3, CompletableFuture var4_5, CompletableFuture var5_6, int var6_7, Object var7_8) {
        switch (var6_7) {
            case 0: {
                if (var0.count == var0.harvesters.size()) {
                    return CompletableFuture.completedFuture(null);
                }
                var1_1 = var0.count - var0.harvesters.size();
                BrowserHarvesterController.logger.info("Initialising {} harvesters", (Object)var1_1);
                var2_2 = 0;
                while (var2_2 < var1_1) {
                    try {
                        var3_3 = new BrowserSharedHarvester(var2_2);
                        var0.harvesters.add(var3_3);
                        var4_5 = var0.vertx.deployVerticle((Verticle)var3_3).onComplete((Handler)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)V, lambda$initHarvesters$3(io.trickle.harvester.pooled.BrowserSharedHarvester io.vertx.core.AsyncResult ), (Lio/vertx/core/AsyncResult;)V)((BrowserHarvesterController)var0, (BrowserSharedHarvester)var3_3)).toCompletionStage().toCompletableFuture();
                        BrowserHarvesterController.logger.info("Waiting for harvester[{}] to start...", (Object)var2_2);
                        v0 = var4_5;
                        if (!v0.isDone()) {
                            var5_6 = v0;
                            return var5_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$initHarvesters(io.trickle.harvester.pooled.BrowserHarvesterController int int io.trickle.harvester.pooled.BrowserSharedHarvester java.util.concurrent.CompletableFuture java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((BrowserHarvesterController)var0, (int)var1_1, (int)var2_2, (BrowserSharedHarvester)var3_3, var4_5, var5_6, (int)1));
                        }
lbl19:
                        // 3 sources

                        while (true) {
                            v0.join();
                            break;
                        }
                    }
                    catch (Throwable var3_4) {
                        BrowserHarvesterController.logger.warn("Error occurred on initialisation stage: {}", (Object)var3_4.getMessage());
                        return CompletableFuture.failedFuture(var3_4);
                    }
                    ++var2_2;
                }
                return CompletableFuture.completedFuture(null);
            }
            case 1: {
                v0 = var5_6;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Optional shouldSwap(String string) {
        Optional<Object> optional;
        int n;
        if (!this.passedHarvesters.isEmpty()) {
            try {
                for (SharedHarvester sharedHarvester : this.passedHarvesters) {
                    if (!sharedHarvester.id().equalsIgnoreCase(string)) continue;
                    return Optional.empty();
                }
            }
            catch (Throwable throwable) {
                logger.warn("Error occurred on distribution: {}", (Object)throwable.getMessage());
            }
        }
        if ((n = this.passedHarvesters.size()) == 0) {
            optional = Optional.empty();
            return optional;
        }
        optional = Optional.of(this.passedHarvesters.get(this.passedCounter.getAndUpdate(arg_0 -> BrowserHarvesterController.lambda$shouldSwap$2(n, arg_0))).id());
        return optional;
    }

    public void lambda$startCounter$1(Long l) {
        try {
            this.passedHarvesters.removeIf(removePredicate);
            Iterator<SharedHarvester> iterator = this.harvesters.iterator();
            while (iterator.hasNext()) {
                SharedHarvester sharedHarvester = iterator.next();
                if (sharedHarvester.passCount() <= 0 || this.passedHarvesters.contains(sharedHarvester)) continue;
                this.passedHarvesters.add(sharedHarvester);
            }
            return;
        }
        catch (Throwable throwable) {
            logger.error("Count assigment error: {}", (Object)throwable.getMessage());
        }
    }

    public void lambda$initHarvesters$3(BrowserSharedHarvester browserSharedHarvester, AsyncResult asyncResult) {
        if (asyncResult.succeeded()) {
            logger.info("Harvester deployed with worker id: {}", asyncResult.result());
            return;
        }
        logger.warn("Failed to launch harvester: {}", (Object)asyncResult.cause().getMessage());
        try {
            this.harvesters.remove(browserSharedHarvester);
            browserSharedHarvester.stop(Promise.promise());
            return;
        }
        catch (Throwable throwable) {
            logger.warn("Error cleaning up staled harvester: {}", (Object)throwable.getMessage());
        }
    }

    public void startCounter() {
        this.vertx.setPeriodic(5000L, this::lambda$startCounter$1);
    }

    @Override
    public CompletableFuture initialise() {
        logger.info("Waiting to start!");
        return this.initHarvesters();
    }

    public static boolean lambda$static$0(SharedHarvester sharedHarvester) {
        if ((long)sharedHarvester.passCount() > 0L) return false;
        return true;
    }
}

