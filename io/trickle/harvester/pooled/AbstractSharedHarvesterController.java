/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.harvester.pooled.SharedHarvester
 *  io.vertx.core.Vertx
 *  io.vertx.core.shareddata.Lock
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package io.trickle.harvester.pooled;

import io.trickle.harvester.pooled.SharedHarvester;
import io.vertx.core.Vertx;
import io.vertx.core.shareddata.Lock;
import java.lang.invoke.LambdaMetafactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractSharedHarvesterController {
    public AtomicInteger counter;
    public String identity;
    public static Logger logger = LogManager.getLogger(AbstractSharedHarvesterController.class);
    public List<SharedHarvester> harvesters = new ArrayList<SharedHarvester>();

    public String allocate() {
        int n = this.harvesters.size();
        if (n != 0) return n == 1 ? this.harvesters.get(0).id() : this.harvesters.get(this.counter.getAndUpdate(arg_0 -> AbstractSharedHarvesterController.lambda$allocate$0(n, arg_0))).id();
        throw new Exception("No harvesters available. Did you configure the harvester count correctly?");
    }

    public abstract Optional shouldSwap(String var1);

    public CompletableFuture start() {
        try {
            CompletableFuture completableFuture = Vertx.currentContext().owner().sharedData().getLocalLockWithTimeout(this.identity, TimeUnit.HOURS.toMillis(1L)).toCompletionStage().toCompletableFuture();
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> AbstractSharedHarvesterController.async$start(this, completableFuture2, null, 1, arg_0));
            }
            Lock lock = (Lock)completableFuture.join();
            try {
                logger.info("Waiting to start!");
                CompletableFuture completableFuture3 = this.initialise();
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> AbstractSharedHarvesterController.async$start(this, completableFuture4, lock, 2, arg_0));
                }
                completableFuture3.join();
                logger.info("Started!");
            }
            catch (Throwable throwable) {
                logger.error("Start error on harvester controller {}", (Object)throwable.getMessage());
            }
            finally {
                lock.release();
            }
        }
        catch (Throwable throwable) {
            logger.error("Lock error on harvester controller {}", (Object)throwable.getMessage());
        }
        return CompletableFuture.completedFuture(true);
    }

    public abstract CompletableFuture initialise();

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$start(AbstractSharedHarvesterController var0, CompletableFuture var1_1, Lock var2_3, int var3_5, Object var4_7) {
        switch (var3_5) {
            case 0: {
                v0 = Vertx.currentContext().owner().sharedData().getLocalLockWithTimeout(var0.identity, TimeUnit.HOURS.toMillis(1L)).toCompletionStage().toCompletableFuture();
                if (!v0.isDone()) {
                    var4_7 = v0;
                    return var4_7.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$start(io.trickle.harvester.pooled.AbstractSharedHarvesterController java.util.concurrent.CompletableFuture io.vertx.core.shareddata.Lock int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((AbstractSharedHarvesterController)var0, var4_7, null, (int)1));
                }
lbl8:
                // 3 sources

                while (true) {
                    var1_1 = (Lock)v0.join();
                    try {
                        AbstractSharedHarvesterController.logger.info("Waiting to start!");
                        v1 = var0.initialise();
                        if (!v1.isDone()) {
                            var4_7 = v1;
                            return var4_7.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$start(io.trickle.harvester.pooled.AbstractSharedHarvesterController java.util.concurrent.CompletableFuture io.vertx.core.shareddata.Lock int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((AbstractSharedHarvesterController)var0, var4_7, var1_1, (int)2));
                        }
lbl16:
                        // 3 sources

                        while (true) {
                            v1.join();
                            AbstractSharedHarvesterController.logger.info("Started!");
                            ** GOTO lbl29
                            break;
                        }
                    }
                    catch (Throwable var2_4) {
                        AbstractSharedHarvesterController.logger.error("Start error on harvester controller {}", (Object)var2_4.getMessage());
                        ** GOTO lbl29
                    }
                    finally {
                        var1_1.release();
                    }
                    break;
                }
            }
            catch (Throwable var1_2) {
                AbstractSharedHarvesterController.logger.error("Lock error on harvester controller {}", (Object)var1_2.getMessage());
            }
lbl29:
            // 3 sources

            return CompletableFuture.completedFuture(true);
            case 1: {
                v0 = var1_1;
                ** continue;
            }
            case 2: {
                v1 = var1_1;
                var1_1 = var2_3;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public AbstractSharedHarvesterController() {
        this.counter = new AtomicInteger(0);
        this.identity = "HARVESTER_MANAGER_SHARED_LOCK_" + UUID.randomUUID();
    }

    public static int lambda$allocate$0(int n, int n2) {
        return ++n2 < n ? n2 : 0;
    }
}
