/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.core.actor.TaskActor
 *  io.trickle.task.Task
 *  io.trickle.task.sites.finishline.FinishlineAPI
 *  io.trickle.util.request.Request
 *  io.trickle.webclient.TaskApiClient
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.client.HttpResponse
 */
package io.trickle.task.sites.finishline;

import io.trickle.core.actor.TaskActor;
import io.trickle.task.Task;
import io.trickle.task.sites.finishline.FinishlineAPI;
import io.trickle.util.request.Request;
import io.trickle.webclient.TaskApiClient;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import java.lang.invoke.LambdaMetafactory;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class Finishline
extends TaskActor {
    public FinishlineAPI api;
    public Task task;

    public CompletableFuture getCartSession() {
        int n = 0;
        while (this.running) {
            if (n >= 5) return CompletableFuture.completedFuture(null);
            ++n;
            CompletableFuture completableFuture = super.sleep(3000);
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Finishline.async$getCartSession(this, n, completableFuture2, 1, arg_0));
            }
            completableFuture.join();
            System.out.println("Donezo");
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$getCartSession(Finishline var0, int var1_1, CompletableFuture var2_2, int var3_3, Object var4_4) {
        switch (var3_3) {
            case 0: {
                var1_1 = 0;
lbl4:
                // 2 sources

                while (true) {
                    if (var0.running == false) return CompletableFuture.completedFuture(null);
                    if (var1_1 >= 5) return CompletableFuture.completedFuture(null);
                    ++var1_1;
                    v0 = super.sleep(3000);
                    if (!v0.isDone()) {
                        var2_2 = v0;
                        return var2_2.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$getCartSession(io.trickle.task.sites.finishline.Finishline int java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Finishline)var0, (int)var1_1, (CompletableFuture)var2_2, (int)1));
                    }
                    ** GOTO lbl15
                    break;
                }
            }
            case 1: {
                v0 = var2_2;
lbl15:
                // 2 sources

                v0.join();
                System.out.println("Donezo");
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture run() {
        CompletableFuture completableFuture = super.randomSleep(5000);
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Finishline.async$run(this, completableFuture2, 1, arg_0));
        }
        completableFuture.join();
        CompletableFuture completableFuture3 = this.visitHomePage();
        if (!completableFuture3.isDone()) {
            CompletableFuture completableFuture4 = completableFuture3;
            return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Finishline.async$run(this, completableFuture4, 2, arg_0));
        }
        completableFuture3.join();
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$visitHomePage(Finishline var0, int var1_1, HttpRequest var2_2, CompletableFuture var3_4, int var4_5, Object var5_7) {
        switch (var4_5) {
            case 0: {
                var1_1 = 0;
                block7: while (var0.running != false) {
                    if (var1_1 >= 10000) return CompletableFuture.completedFuture(null);
                    try {
                        var2_2 = var0.api.homePage();
                        v0 = Request.send((HttpRequest)var2_2);
                        if (!v0.isDone()) {
                            var5_7 = v0;
                            return var5_7.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$visitHomePage(io.trickle.task.sites.finishline.Finishline int io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Finishline)var0, (int)var1_1, (HttpRequest)var2_2, (CompletableFuture)var5_7, (int)1));
                        }
lbl12:
                        // 3 sources

                        while (true) {
                            var3_4 = (HttpResponse)v0.join();
                            if (var3_4 == null) break;
                            var4_6 = (String)var3_4.body();
                            if (!var4_6.contains("<title>WAITING</title>")) ** GOTO lbl19
                            var0.logger.warn("Waiting in Queue!");
                            break;
lbl19:
                            // 1 sources

                            if (!var4_6.contains("ACCESS DENIED")) ** GOTO lbl22
                            var0.logger.error("Banned!");
                            break;
lbl22:
                            // 1 sources

                            var0.logger.info("Passed queue (?) {}", (Object)var3_4.statusCode());
                            break block7;
                            break;
                        }
                    }
                    catch (Exception var2_3) {
                        var0.logger.error("Error occurred on queue {}", (Object)var2_3.getMessage());
                    }
                    ++var1_1;
                    v1 = var0.sleep(31000);
                    if (!v1.isDone()) {
                        var5_7 = v1;
                        return var5_7.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$visitHomePage(io.trickle.task.sites.finishline.Finishline int io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Finishline)var0, (int)var1_1, null, (CompletableFuture)var5_7, (int)2));
                    }
lbl31:
                    // 3 sources

                    while (true) {
                        v1.join();
                        continue block7;
                        break;
                    }
                }
                return CompletableFuture.completedFuture(null);
                return CompletableFuture.completedFuture(null);
            }
            case 1: {
                v0 = var3_4;
                ** continue;
            }
            case 2: {
                v1 = var3_4;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public Finishline(Task task, int n) {
        super(n);
        this.task = task;
        this.api = new FinishlineAPI(this.task, this.task.getSite());
        super.setClient((TaskApiClient)this.api);
    }

    public CompletableFuture visitHomePage() {
        int n = 0;
        while (this.running) {
            block7: {
                if (n >= 10000) return CompletableFuture.completedFuture(null);
                try {
                    HttpRequest httpRequest = this.api.homePage();
                    CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture2 = completableFuture;
                        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Finishline.async$visitHomePage(this, n, httpRequest, completableFuture2, 1, arg_0));
                    }
                    HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                    if (httpResponse == null) break block7;
                    String string = (String)httpResponse.body();
                    if (string.contains("<title>WAITING</title>")) {
                        this.logger.warn("Waiting in Queue!");
                        break block7;
                    }
                    if (string.contains("ACCESS DENIED")) {
                        this.logger.error("Banned!");
                        break block7;
                    }
                    this.logger.info("Passed queue (?) {}", (Object)httpResponse.statusCode());
                    break;
                }
                catch (Exception exception) {
                    this.logger.error("Error occurred on queue {}", (Object)exception.getMessage());
                }
            }
            ++n;
            CompletableFuture completableFuture = this.sleep(31000);
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture3 = completableFuture;
                return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> Finishline.async$visitHomePage(this, n, null, completableFuture3, 2, arg_0));
            }
            completableFuture.join();
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$run(Finishline var0, CompletableFuture var1_1, int var2_2, Object var3_3) {
        switch (var2_2) {
            case 0: {
                v0 = super.randomSleep(5000);
                if (!v0.isDone()) {
                    var1_1 = v0;
                    return var1_1.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.finishline.Finishline java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Finishline)var0, (CompletableFuture)var1_1, (int)1));
                }
                ** GOTO lbl10
            }
            case 1: {
                v0 = var1_1;
lbl10:
                // 2 sources

                v0.join();
                v1 = var0.visitHomePage();
                if (!v1.isDone()) {
                    var1_1 = v1;
                    return var1_1.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.finishline.Finishline java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Finishline)var0, (CompletableFuture)var1_1, (int)2));
                }
                ** GOTO lbl19
            }
            case 2: {
                v1 = var1_1;
lbl19:
                // 2 sources

                v1.join();
                return CompletableFuture.completedFuture(null);
            }
        }
        throw new IllegalArgumentException();
    }
}
