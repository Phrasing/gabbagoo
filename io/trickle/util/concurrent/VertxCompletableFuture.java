/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.AsyncResult
 *  io.vertx.core.Context
 *  io.vertx.core.Future
 *  io.vertx.core.Promise
 *  io.vertx.core.Vertx
 *  io.vertx.core.WorkerExecutor
 */
package io.trickle.util.concurrent;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.WorkerExecutor;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public class VertxCompletableFuture
extends CompletableFuture
implements CompletionStage {
    public Executor executor;
    public Context context;

    public VertxCompletableFuture withContext(Context context) {
        VertxCompletableFuture vertxCompletableFuture = new VertxCompletableFuture(Objects.requireNonNull(context));
        this.whenComplete((arg_0, arg_1) -> VertxCompletableFuture.lambda$withContext$19(vertxCompletableFuture, arg_0, arg_1));
        return vertxCompletableFuture;
    }

    public static void lambda$runBlockingAsync$10(VertxCompletableFuture vertxCompletableFuture, AsyncResult asyncResult) {
        if (asyncResult.failed()) {
            vertxCompletableFuture.completeExceptionally(asyncResult.cause());
            return;
        }
        vertxCompletableFuture.complete((Void)asyncResult.result());
    }

    @Override
    public CompletableFuture runAfterEitherAsync(CompletionStage completionStage, Runnable runnable, Executor executor) {
        return this.runAfterEitherAsync(completionStage, runnable, executor);
    }

    @Override
    public CompletionStage thenApplyAsync(Function function, Executor executor) {
        return this.thenApplyAsync(function, executor);
    }

    public VertxCompletableFuture(Vertx vertx) {
        this(Objects.requireNonNull(vertx).getOrCreateContext());
    }

    @Override
    public VertxCompletableFuture runAfterBothAsync(CompletionStage completionStage, Runnable runnable) {
        return new VertxCompletableFuture(this.context, (CompletableFuture)super.runAfterBothAsync(completionStage, runnable, this.executor));
    }

    @Override
    public CompletionStage thenComposeAsync(Function function) {
        return this.thenComposeAsync(function);
    }

    @Override
    public VertxCompletableFuture acceptEitherAsync(CompletionStage completionStage, Consumer consumer) {
        return new VertxCompletableFuture(this.context, (CompletableFuture)super.acceptEitherAsync(completionStage, consumer, this.executor));
    }

    @Override
    public VertxCompletableFuture runAfterBoth(CompletionStage completionStage, Runnable runnable) {
        return new VertxCompletableFuture(this.context, (CompletableFuture)super.runAfterBoth(completionStage, runnable));
    }

    @Override
    public CompletableFuture thenCombineAsync(CompletionStage completionStage, BiFunction biFunction, Executor executor) {
        return this.thenCombineAsync(completionStage, biFunction, executor);
    }

    @Override
    public CompletionStage thenApply(Function function) {
        return this.thenApply(function);
    }

    public static VertxCompletableFuture runAsync(Vertx vertx, Runnable runnable) {
        return VertxCompletableFuture.runAsync(Objects.requireNonNull(vertx).getOrCreateContext(), runnable);
    }

    public static void lambda$supplyBlockingAsyncOn$16(VertxCompletableFuture vertxCompletableFuture, AsyncResult asyncResult) {
        if (asyncResult.failed()) {
            vertxCompletableFuture.completeExceptionally(asyncResult.cause());
            return;
        }
        vertxCompletableFuture.complete(asyncResult.result());
    }

    public VertxCompletableFuture withContext(Vertx vertx) {
        return this.withContext(Objects.requireNonNull(vertx).getOrCreateContext());
    }

    @Override
    public CompletableFuture handleAsync(BiFunction biFunction, Executor executor) {
        return this.handleAsync(biFunction, executor);
    }

    @Override
    public CompletionStage acceptEitherAsync(CompletionStage completionStage, Consumer consumer) {
        return this.acceptEitherAsync(completionStage, consumer);
    }

    @Override
    public CompletionStage handle(BiFunction biFunction) {
        return this.handle(biFunction);
    }

    public static VertxCompletableFuture supplyAsync(Vertx vertx, Supplier supplier) {
        return VertxCompletableFuture.supplyAsync(Objects.requireNonNull(vertx).getOrCreateContext(), supplier);
    }

    public static void lambda$supplyBlockingAsyncOn$15(Supplier supplier, Promise promise) {
        try {
            promise.complete(supplier.get());
            return;
        }
        catch (Throwable throwable) {
            promise.fail(throwable);
        }
    }

    @Override
    public CompletionStage applyToEitherAsync(CompletionStage completionStage, Function function) {
        return this.applyToEitherAsync(completionStage, function);
    }

    public static VertxCompletableFuture runAsync(Context context, Runnable runnable) {
        Objects.requireNonNull(runnable);
        VertxCompletableFuture vertxCompletableFuture = new VertxCompletableFuture(context);
        context.runOnContext(arg_0 -> VertxCompletableFuture.lambda$runAsync$8(runnable, vertxCompletableFuture, arg_0));
        return vertxCompletableFuture;
    }

    @Override
    public CompletableFuture whenComplete(BiConsumer biConsumer) {
        return this.whenComplete(biConsumer);
    }

    @Override
    public VertxCompletableFuture acceptEitherAsync(CompletionStage completionStage, Consumer consumer, Executor executor) {
        return new VertxCompletableFuture(this.context, (CompletableFuture)super.acceptEitherAsync(completionStage, consumer, executor));
    }

    @Override
    public CompletionStage acceptEitherAsync(CompletionStage completionStage, Consumer consumer, Executor executor) {
        return this.acceptEitherAsync(completionStage, consumer, executor);
    }

    @Override
    public VertxCompletableFuture handleAsync(BiFunction biFunction, Executor executor) {
        return new VertxCompletableFuture(this.context, (CompletableFuture)super.handleAsync(biFunction, executor));
    }

    @Override
    public VertxCompletableFuture acceptEither(CompletionStage completionStage, Consumer consumer) {
        return new VertxCompletableFuture(this.context, (CompletableFuture)super.acceptEither(completionStage, consumer));
    }

    @Override
    public VertxCompletableFuture thenCompose(Function function) {
        return new VertxCompletableFuture(this.context, (CompletableFuture)super.thenCompose(function));
    }

    @Override
    public CompletionStage thenAcceptBothAsync(CompletionStage completionStage, BiConsumer biConsumer) {
        return this.thenAcceptBothAsync(completionStage, biConsumer);
    }

    @Override
    public CompletableFuture thenCombine(CompletionStage completionStage, BiFunction biFunction) {
        return this.thenCombine(completionStage, biFunction);
    }

    @Override
    public VertxCompletableFuture whenComplete(BiConsumer biConsumer) {
        return new VertxCompletableFuture(this.context, (CompletableFuture)super.whenComplete(biConsumer));
    }

    public static void lambda$from$5(VertxCompletableFuture vertxCompletableFuture, AsyncResult asyncResult, Void void_) {
        vertxCompletableFuture.completeFromAsyncResult(asyncResult);
    }

    @Override
    public VertxCompletableFuture thenAcceptAsync(Consumer consumer, Executor executor) {
        return new VertxCompletableFuture(this.context, (CompletableFuture)super.thenAcceptAsync(consumer, executor));
    }

    public static Future toFuture(CompletableFuture completableFuture) {
        Promise promise = Promise.promise();
        Objects.requireNonNull(completableFuture).whenComplete((arg_0, arg_1) -> VertxCompletableFuture.lambda$toFuture$17(promise, arg_0, arg_1));
        Future future = promise.future();
        future.onComplete(arg_0 -> VertxCompletableFuture.lambda$toFuture$18(completableFuture, arg_0));
        return future;
    }

    @Override
    public VertxCompletableFuture thenCombineAsync(CompletionStage completionStage, BiFunction biFunction) {
        return new VertxCompletableFuture(this.context, (CompletableFuture)super.thenCombineAsync(completionStage, biFunction, this.executor));
    }

    public void lambda$new$2(Object object, Throwable throwable) {
        if (throwable != null) {
            this.completeExceptionally(throwable);
            return;
        }
        this.complete(object);
    }

    public static void lambda$toFuture$17(Promise promise, Object object, Throwable throwable) {
        if (throwable != null) {
            promise.fail(throwable);
            return;
        }
        promise.complete(object);
    }

    public static void lambda$from$4(Context context, VertxCompletableFuture vertxCompletableFuture, Object object, Throwable throwable) {
        if (context == Vertx.currentContext()) {
            vertxCompletableFuture.complete(object, throwable);
            return;
        }
        vertxCompletableFuture.context.runOnContext(arg_0 -> VertxCompletableFuture.lambda$from$3(vertxCompletableFuture, object, throwable, arg_0));
    }

    @Override
    public CompletionStage acceptEither(CompletionStage completionStage, Consumer consumer) {
        return this.acceptEither(completionStage, consumer);
    }

    public static void lambda$runBlockingAsyncOn$12(VertxCompletableFuture vertxCompletableFuture, AsyncResult asyncResult) {
        if (asyncResult.failed()) {
            vertxCompletableFuture.completeExceptionally(asyncResult.cause());
            return;
        }
        vertxCompletableFuture.complete((Void)asyncResult.result());
    }

    public static VertxCompletableFuture supplyBlockingAsyncOn(Vertx vertx, WorkerExecutor workerExecutor, Supplier supplier) {
        return VertxCompletableFuture.supplyBlockingAsyncOn(Objects.requireNonNull(vertx).getOrCreateContext(), workerExecutor, supplier);
    }

    @Override
    public CompletionStage thenAcceptBothAsync(CompletionStage completionStage, BiConsumer biConsumer, Executor executor) {
        return this.thenAcceptBothAsync(completionStage, biConsumer, executor);
    }

    @Override
    public CompletionStage whenCompleteAsync(BiConsumer biConsumer) {
        return this.whenCompleteAsync(biConsumer);
    }

    @Override
    public VertxCompletableFuture thenAcceptAsync(Consumer consumer) {
        return new VertxCompletableFuture(this.context, (CompletableFuture)super.thenAcceptAsync(consumer, this.executor));
    }

    @Override
    public CompletionStage thenApplyAsync(Function function) {
        return this.thenApplyAsync(function);
    }

    public static void lambda$supplyBlockingAsync$14(VertxCompletableFuture vertxCompletableFuture, AsyncResult asyncResult) {
        if (asyncResult.failed()) {
            vertxCompletableFuture.completeExceptionally(asyncResult.cause());
            return;
        }
        vertxCompletableFuture.complete(asyncResult.result());
    }

    public Context context() {
        return this.context;
    }

    @Override
    public CompletionStage thenRun(Runnable runnable) {
        return this.thenRun(runnable);
    }

    @Override
    public CompletableFuture whenCompleteAsync(BiConsumer biConsumer) {
        return this.whenCompleteAsync(biConsumer);
    }

    @Override
    public CompletableFuture thenComposeAsync(Function function) {
        return this.thenComposeAsync(function);
    }

    @Override
    public VertxCompletableFuture thenRunAsync(Runnable runnable) {
        return new VertxCompletableFuture(this.context, (CompletableFuture)super.thenRunAsync(runnable, this.executor));
    }

    @Override
    public CompletionStage thenCompose(Function function) {
        return this.thenCompose(function);
    }

    public VertxCompletableFuture() {
        this(Vertx.currentContext());
    }

    @Override
    public VertxCompletableFuture thenRun(Runnable runnable) {
        return new VertxCompletableFuture(this.context, (CompletableFuture)super.thenRun(runnable));
    }

    public static void lambda$runBlockingAsyncOn$11(Runnable runnable, Promise promise) {
        try {
            runnable.run();
            promise.complete(null);
            return;
        }
        catch (Throwable throwable) {
            promise.fail(throwable);
        }
    }

    public static VertxCompletableFuture supplyBlockingAsyncOn(Context context, WorkerExecutor workerExecutor, Supplier supplier) {
        Objects.requireNonNull(supplier);
        VertxCompletableFuture vertxCompletableFuture = new VertxCompletableFuture(context);
        Objects.requireNonNull(workerExecutor).executeBlocking(arg_0 -> VertxCompletableFuture.lambda$supplyBlockingAsyncOn$15((Supplier)supplier, arg_0), false, arg_0 -> VertxCompletableFuture.lambda$supplyBlockingAsyncOn$16(vertxCompletableFuture, arg_0));
        return vertxCompletableFuture;
    }

    @Override
    public CompletionStage thenComposeAsync(Function function, Executor executor) {
        return this.thenComposeAsync(function, executor);
    }

    @Override
    public VertxCompletableFuture thenAcceptBoth(CompletionStage completionStage, BiConsumer biConsumer) {
        return new VertxCompletableFuture(this.context, (CompletableFuture)super.thenAcceptBoth(completionStage, biConsumer));
    }

    @Override
    public CompletionStage handleAsync(BiFunction biFunction, Executor executor) {
        return this.handleAsync(biFunction, executor);
    }

    @Override
    public CompletableFuture thenRun(Runnable runnable) {
        return this.thenRun(runnable);
    }

    @Override
    public CompletionStage thenAcceptAsync(Consumer consumer, Executor executor) {
        return this.thenAcceptAsync(consumer, executor);
    }

    @Override
    public CompletionStage runAfterBothAsync(CompletionStage completionStage, Runnable runnable, Executor executor) {
        return this.runAfterBothAsync(completionStage, runnable, executor);
    }

    @Override
    public VertxCompletableFuture thenApplyAsync(Function function) {
        return new VertxCompletableFuture(this.context, (CompletableFuture)super.thenApplyAsync(function, this.executor));
    }

    @Override
    public CompletableFuture thenApplyAsync(Function function) {
        return this.thenApplyAsync(function);
    }

    @Override
    public CompletableFuture acceptEither(CompletionStage completionStage, Consumer consumer) {
        return this.acceptEither(completionStage, consumer);
    }

    @Override
    public VertxCompletableFuture runAfterEitherAsync(CompletionStage completionStage, Runnable runnable, Executor executor) {
        return new VertxCompletableFuture(this.context, (CompletableFuture)super.runAfterEitherAsync(completionStage, runnable, executor));
    }

    public static void lambda$new$1(Context context, Runnable runnable) {
        context.runOnContext(arg_0 -> VertxCompletableFuture.lambda$new$0(runnable, arg_0));
    }

    @Override
    public VertxCompletableFuture thenAccept(Consumer consumer) {
        return new VertxCompletableFuture(this.context, (CompletableFuture)super.thenAccept(consumer));
    }

    @Override
    public VertxCompletableFuture thenApply(Function function) {
        return new VertxCompletableFuture(this.context, (CompletableFuture)super.thenApply(function));
    }

    @Override
    public CompletionStage whenCompleteAsync(BiConsumer biConsumer, Executor executor) {
        return this.whenCompleteAsync(biConsumer, executor);
    }

    public static VertxCompletableFuture from(Vertx vertx, Future future) {
        return VertxCompletableFuture.from(vertx.getOrCreateContext(), future);
    }

    @Override
    public CompletableFuture thenCombineAsync(CompletionStage completionStage, BiFunction biFunction) {
        return this.thenCombineAsync(completionStage, biFunction);
    }

    @Override
    public CompletionStage thenCombine(CompletionStage completionStage, BiFunction biFunction) {
        return this.thenCombine(completionStage, biFunction);
    }

    @Override
    public VertxCompletableFuture whenCompleteAsync(BiConsumer biConsumer) {
        return new VertxCompletableFuture(this.context, (CompletableFuture)super.whenCompleteAsync(biConsumer, this.executor));
    }

    @Override
    public CompletableFuture thenAcceptAsync(Consumer consumer) {
        return this.thenAcceptAsync(consumer);
    }

    public VertxCompletableFuture withContext() {
        Context context = Objects.requireNonNull(Vertx.currentContext());
        return this.withContext(context);
    }

    public static void lambda$from$3(VertxCompletableFuture vertxCompletableFuture, Object object, Throwable throwable, Void void_) {
        vertxCompletableFuture.complete(object, throwable);
    }

    @Override
    public CompletionStage thenRunAsync(Runnable runnable) {
        return this.thenRunAsync(runnable);
    }

    public static VertxCompletableFuture supplyAsync(Context context, Supplier supplier) {
        Objects.requireNonNull(supplier);
        VertxCompletableFuture vertxCompletableFuture = new VertxCompletableFuture(Objects.requireNonNull(context));
        context.runOnContext(arg_0 -> VertxCompletableFuture.lambda$supplyAsync$7(vertxCompletableFuture, (Supplier)supplier, arg_0));
        return vertxCompletableFuture;
    }

    public static VertxCompletableFuture from(CompletableFuture completableFuture) {
        return VertxCompletableFuture.from(Vertx.currentContext(), completableFuture);
    }

    @Override
    public CompletionStage thenAccept(Consumer consumer) {
        return this.thenAccept(consumer);
    }

    @Override
    public CompletableFuture thenComposeAsync(Function function, Executor executor) {
        return this.thenComposeAsync(function, executor);
    }

    @Override
    public CompletableFuture thenRunAsync(Runnable runnable) {
        return this.thenRunAsync(runnable);
    }

    @Override
    public CompletableFuture runAfterBoth(CompletionStage completionStage, Runnable runnable) {
        return this.runAfterBoth(completionStage, runnable);
    }

    @Override
    public CompletableFuture thenAcceptBothAsync(CompletionStage completionStage, BiConsumer biConsumer, Executor executor) {
        return this.thenAcceptBothAsync(completionStage, biConsumer, executor);
    }

    @Override
    public CompletableFuture runAfterBothAsync(CompletionStage completionStage, Runnable runnable) {
        return this.runAfterBothAsync(completionStage, runnable);
    }

    @Override
    public CompletableFuture applyToEither(CompletionStage completionStage, Function function) {
        return this.applyToEither(completionStage, function);
    }

    public static void lambda$runBlockingAsync$9(Runnable runnable, Promise promise) {
        try {
            runnable.run();
            promise.complete(null);
            return;
        }
        catch (Throwable throwable) {
            promise.fail(throwable);
        }
    }

    @Override
    public CompletionStage applyToEitherAsync(CompletionStage completionStage, Function function, Executor executor) {
        return this.applyToEitherAsync(completionStage, function, executor);
    }

    public VertxCompletableFuture toCompletableFuture() {
        return this;
    }

    @Override
    public VertxCompletableFuture applyToEitherAsync(CompletionStage completionStage, Function function) {
        return new VertxCompletableFuture(this.context, (CompletableFuture)super.applyToEitherAsync(completionStage, function, this.executor));
    }

    @Override
    public CompletionStage runAfterEither(CompletionStage completionStage, Runnable runnable) {
        return this.runAfterEither(completionStage, runnable);
    }

    @Override
    public CompletableFuture handle(BiFunction biFunction) {
        return this.handle(biFunction);
    }

    public static void lambda$new$0(Runnable runnable, Void void_) {
        runnable.run();
    }

    @Override
    public CompletableFuture runAfterEitherAsync(CompletionStage completionStage, Runnable runnable) {
        return this.runAfterEitherAsync(completionStage, runnable);
    }

    @Override
    public CompletableFuture applyToEitherAsync(CompletionStage completionStage, Function function) {
        return this.applyToEitherAsync(completionStage, function);
    }

    @Override
    public CompletionStage thenCombineAsync(CompletionStage completionStage, BiFunction biFunction, Executor executor) {
        return this.thenCombineAsync(completionStage, biFunction, executor);
    }

    @Override
    public VertxCompletableFuture whenCompleteAsync(BiConsumer biConsumer, Executor executor) {
        return new VertxCompletableFuture(this.context, (CompletableFuture)super.whenCompleteAsync(biConsumer, executor));
    }

    @Override
    public CompletableFuture applyToEitherAsync(CompletionStage completionStage, Function function, Executor executor) {
        return this.applyToEitherAsync(completionStage, function, executor);
    }

    public static void lambda$from$6(Context context, VertxCompletableFuture vertxCompletableFuture, AsyncResult asyncResult) {
        if (context == Vertx.currentContext()) {
            vertxCompletableFuture.completeFromAsyncResult(asyncResult);
            return;
        }
        vertxCompletableFuture.context.runOnContext(arg_0 -> VertxCompletableFuture.lambda$from$5(vertxCompletableFuture, asyncResult, arg_0));
    }

    public static void lambda$supplyAsync$7(VertxCompletableFuture vertxCompletableFuture, Supplier supplier, Void void_) {
        try {
            vertxCompletableFuture.complete(supplier.get());
            return;
        }
        catch (Throwable throwable) {
            vertxCompletableFuture.completeExceptionally(throwable);
        }
    }

    @Override
    public VertxCompletableFuture thenComposeAsync(Function function, Executor executor) {
        return new VertxCompletableFuture(this.context, (CompletableFuture)super.thenComposeAsync(function, executor));
    }

    @Override
    public CompletableFuture whenCompleteAsync(BiConsumer biConsumer, Executor executor) {
        return this.whenCompleteAsync(biConsumer, executor);
    }

    public VertxCompletableFuture(Context context) {
        this.context = Objects.requireNonNull(context);
        this.executor = arg_0 -> VertxCompletableFuture.lambda$new$1(context, arg_0);
    }

    @Override
    public CompletableFuture runAfterEither(CompletionStage completionStage, Runnable runnable) {
        return this.runAfterEither(completionStage, runnable);
    }

    @Override
    public CompletableFuture thenAcceptBothAsync(CompletionStage completionStage, BiConsumer biConsumer) {
        return this.thenAcceptBothAsync(completionStage, biConsumer);
    }

    @Override
    public VertxCompletableFuture applyToEitherAsync(CompletionStage completionStage, Function function, Executor executor) {
        return new VertxCompletableFuture(this.context, (CompletableFuture)super.applyToEitherAsync(completionStage, function, executor));
    }

    @Override
    public VertxCompletableFuture thenApplyAsync(Function function, Executor executor) {
        return new VertxCompletableFuture(this.context, (CompletableFuture)super.thenApplyAsync(function, executor));
    }

    @Override
    public VertxCompletableFuture thenCombineAsync(CompletionStage completionStage, BiFunction biFunction, Executor executor) {
        return new VertxCompletableFuture(this.context, (CompletableFuture)super.thenCombineAsync(completionStage, biFunction, executor));
    }

    @Override
    public CompletableFuture acceptEitherAsync(CompletionStage completionStage, Consumer consumer, Executor executor) {
        return this.acceptEitherAsync(completionStage, consumer, executor);
    }

    public static VertxCompletableFuture supplyBlockingAsync(Context context, Supplier supplier) {
        Objects.requireNonNull(supplier);
        VertxCompletableFuture vertxCompletableFuture = new VertxCompletableFuture(context);
        context.executeBlocking(arg_0 -> VertxCompletableFuture.lambda$supplyBlockingAsync$13((Supplier)supplier, arg_0), false, arg_0 -> VertxCompletableFuture.lambda$supplyBlockingAsync$14(vertxCompletableFuture, arg_0));
        return vertxCompletableFuture;
    }

    public void complete(Object object, Throwable throwable) {
        if (throwable == null) {
            super.complete(object);
            return;
        }
        super.completeExceptionally(throwable);
    }

    @Override
    public CompletableFuture thenApplyAsync(Function function, Executor executor) {
        return this.thenApplyAsync(function, executor);
    }

    @Override
    public VertxCompletableFuture handle(BiFunction biFunction) {
        return new VertxCompletableFuture(this.context, (CompletableFuture)super.handle(biFunction));
    }

    @Override
    public VertxCompletableFuture runAfterEither(CompletionStage completionStage, Runnable runnable) {
        return new VertxCompletableFuture(this.context, (CompletableFuture)super.runAfterEither(completionStage, runnable));
    }

    public Future toFuture() {
        return VertxCompletableFuture.toFuture(this);
    }

    @Override
    public CompletionStage thenAcceptAsync(Consumer consumer) {
        return this.thenAcceptAsync(consumer);
    }

    @Override
    public CompletableFuture thenApply(Function function) {
        return this.thenApply(function);
    }

    @Override
    public CompletionStage handleAsync(BiFunction biFunction) {
        return this.handleAsync(biFunction);
    }

    @Override
    public CompletionStage runAfterEitherAsync(CompletionStage completionStage, Runnable runnable) {
        return this.runAfterEitherAsync(completionStage, runnable);
    }

    public static VertxCompletableFuture anyOf(Vertx vertx, CompletableFuture ... completableFutureArray) {
        CompletableFuture<Object> completableFuture = CompletableFuture.anyOf(completableFutureArray);
        return VertxCompletableFuture.from(vertx, completableFuture);
    }

    public static VertxCompletableFuture from(Context context, CompletableFuture completableFuture) {
        VertxCompletableFuture vertxCompletableFuture = new VertxCompletableFuture(Objects.requireNonNull(context));
        Objects.requireNonNull(completableFuture).whenComplete((arg_0, arg_1) -> VertxCompletableFuture.lambda$from$4(context, vertxCompletableFuture, arg_0, arg_1));
        return vertxCompletableFuture;
    }

    @Override
    public VertxCompletableFuture runAfterEitherAsync(CompletionStage completionStage, Runnable runnable) {
        return new VertxCompletableFuture(this.context, (CompletableFuture)super.runAfterEitherAsync(completionStage, runnable, this.executor));
    }

    @Override
    public VertxCompletableFuture thenAcceptBothAsync(CompletionStage completionStage, BiConsumer biConsumer, Executor executor) {
        return new VertxCompletableFuture(this.context, (CompletableFuture)super.thenAcceptBothAsync(completionStage, biConsumer, executor));
    }

    @Override
    public CompletionStage runAfterBoth(CompletionStage completionStage, Runnable runnable) {
        return this.runAfterBoth(completionStage, runnable);
    }

    public static VertxCompletableFuture from(CompletionStage completionStage) {
        return VertxCompletableFuture.from(Vertx.currentContext(), completionStage);
    }

    @Override
    public CompletableFuture runAfterBothAsync(CompletionStage completionStage, Runnable runnable, Executor executor) {
        return this.runAfterBothAsync(completionStage, runnable, executor);
    }

    public VertxCompletableFuture(Context context, CompletableFuture completableFuture) {
        this(context);
        Objects.requireNonNull(completableFuture).whenComplete(this::lambda$new$2);
    }

    public static VertxCompletableFuture runBlockingAsync(Vertx vertx, Runnable runnable) {
        return VertxCompletableFuture.runBlockingAsync(Objects.requireNonNull(vertx).getOrCreateContext(), runnable);
    }

    public void completeFromAsyncResult(AsyncResult asyncResult) {
        if (asyncResult.succeeded()) {
            super.complete(asyncResult.result());
            return;
        }
        super.completeExceptionally(asyncResult.cause());
    }

    @Override
    public VertxCompletableFuture thenRunAsync(Runnable runnable, Executor executor) {
        return new VertxCompletableFuture(this.context, (CompletableFuture)super.thenRunAsync(runnable, executor));
    }

    public static VertxCompletableFuture allOf(Context context, CompletableFuture ... completableFutureArray) {
        CompletableFuture<Void> completableFuture = CompletableFuture.allOf(completableFutureArray);
        return VertxCompletableFuture.from(context, completableFuture);
    }

    @Override
    public CompletionStage runAfterEitherAsync(CompletionStage completionStage, Runnable runnable, Executor executor) {
        return this.runAfterEitherAsync(completionStage, runnable, executor);
    }

    @Override
    public CompletionStage whenComplete(BiConsumer biConsumer) {
        return this.whenComplete(biConsumer);
    }

    @Override
    public CompletableFuture thenAcceptBoth(CompletionStage completionStage, BiConsumer biConsumer) {
        return this.thenAcceptBoth(completionStage, biConsumer);
    }

    public static void lambda$withContext$19(VertxCompletableFuture vertxCompletableFuture, Object object, Throwable throwable) {
        if (throwable != null) {
            vertxCompletableFuture.completeExceptionally(throwable);
            return;
        }
        vertxCompletableFuture.complete(object);
    }

    @Override
    public CompletionStage thenAcceptBoth(CompletionStage completionStage, BiConsumer biConsumer) {
        return this.thenAcceptBoth(completionStage, biConsumer);
    }

    public static VertxCompletableFuture supplyBlockingAsync(Vertx vertx, Supplier supplier) {
        return VertxCompletableFuture.supplyBlockingAsync(Objects.requireNonNull(vertx).getOrCreateContext(), supplier);
    }

    @Override
    public CompletionStage thenCombineAsync(CompletionStage completionStage, BiFunction biFunction) {
        return this.thenCombineAsync(completionStage, biFunction);
    }

    public static VertxCompletableFuture runBlockingAsyncOn(Vertx vertx, WorkerExecutor workerExecutor, Runnable runnable) {
        return VertxCompletableFuture.runBlockingAsyncOn(Objects.requireNonNull(vertx).getOrCreateContext(), workerExecutor, runnable);
    }

    public static VertxCompletableFuture runBlockingAsync(Context context, Runnable runnable) {
        Objects.requireNonNull(runnable);
        VertxCompletableFuture vertxCompletableFuture = new VertxCompletableFuture(Objects.requireNonNull(context));
        context.executeBlocking(arg_0 -> VertxCompletableFuture.lambda$runBlockingAsync$9(runnable, arg_0), false, arg_0 -> VertxCompletableFuture.lambda$runBlockingAsync$10(vertxCompletableFuture, arg_0));
        return vertxCompletableFuture;
    }

    @Override
    public CompletableFuture thenCompose(Function function) {
        return this.thenCompose(function);
    }

    public static VertxCompletableFuture allOf(Vertx vertx, CompletableFuture ... completableFutureArray) {
        CompletableFuture<Void> completableFuture = CompletableFuture.allOf(completableFutureArray);
        return VertxCompletableFuture.from(vertx, completableFuture);
    }

    @Override
    public CompletionStage thenRunAsync(Runnable runnable, Executor executor) {
        return this.thenRunAsync(runnable, executor);
    }

    public static VertxCompletableFuture from(Context context, Future future) {
        VertxCompletableFuture vertxCompletableFuture = new VertxCompletableFuture(Objects.requireNonNull(context));
        Objects.requireNonNull(future).onComplete(arg_0 -> VertxCompletableFuture.lambda$from$6(context, vertxCompletableFuture, arg_0));
        return vertxCompletableFuture;
    }

    public static void lambda$toFuture$18(CompletableFuture completableFuture, AsyncResult asyncResult) {
        if (asyncResult.succeeded()) {
            completableFuture.complete(asyncResult.result());
            return;
        }
        completableFuture.completeExceptionally(asyncResult.cause());
    }

    public static VertxCompletableFuture anyOf(Context context, CompletableFuture ... completableFutureArray) {
        CompletableFuture<Object> completableFuture = CompletableFuture.anyOf(completableFutureArray);
        return VertxCompletableFuture.from(context, completableFuture);
    }

    @Override
    public CompletableFuture thenAcceptAsync(Consumer consumer, Executor executor) {
        return this.thenAcceptAsync(consumer, executor);
    }

    @Override
    public CompletionStage runAfterBothAsync(CompletionStage completionStage, Runnable runnable) {
        return this.runAfterBothAsync(completionStage, runnable);
    }

    public static VertxCompletableFuture from(Vertx vertx, CompletionStage completionStage) {
        return VertxCompletableFuture.from(vertx.getOrCreateContext(), completionStage);
    }

    @Override
    public CompletableFuture handleAsync(BiFunction biFunction) {
        return this.handleAsync(biFunction);
    }

    @Override
    public VertxCompletableFuture applyToEither(CompletionStage completionStage, Function function) {
        return new VertxCompletableFuture(this.context, (CompletableFuture)super.applyToEither(completionStage, function));
    }

    @Override
    public CompletableFuture thenRunAsync(Runnable runnable, Executor executor) {
        return this.thenRunAsync(runnable, executor);
    }

    public static void lambda$supplyBlockingAsync$13(Supplier supplier, Promise promise) {
        try {
            promise.complete(supplier.get());
            return;
        }
        catch (Throwable throwable) {
            promise.fail(throwable);
        }
    }

    @Override
    public VertxCompletableFuture runAfterBothAsync(CompletionStage completionStage, Runnable runnable, Executor executor) {
        return new VertxCompletableFuture(this.context, (CompletableFuture)super.runAfterBothAsync(completionStage, runnable, executor));
    }

    @Override
    public VertxCompletableFuture thenCombine(CompletionStage completionStage, BiFunction biFunction) {
        return new VertxCompletableFuture(this.context, (CompletableFuture)super.thenCombine(completionStage, biFunction));
    }

    @Override
    public CompletableFuture acceptEitherAsync(CompletionStage completionStage, Consumer consumer) {
        return this.acceptEitherAsync(completionStage, consumer);
    }

    @Override
    public VertxCompletableFuture handleAsync(BiFunction biFunction) {
        return new VertxCompletableFuture(this.context, (CompletableFuture)super.handleAsync(biFunction, this.executor));
    }

    public CompletableFuture toCompletableFuture() {
        return this.toCompletableFuture();
    }

    public static VertxCompletableFuture from(Future future) {
        return VertxCompletableFuture.from(Vertx.currentContext(), future);
    }

    @Override
    public VertxCompletableFuture thenComposeAsync(Function function) {
        return new VertxCompletableFuture(this.context, (CompletableFuture)super.thenComposeAsync(function, this.executor));
    }

    public static VertxCompletableFuture from(Vertx vertx, CompletableFuture completableFuture) {
        return VertxCompletableFuture.from(vertx.getOrCreateContext(), completableFuture);
    }

    public static void lambda$runAsync$8(Runnable runnable, VertxCompletableFuture vertxCompletableFuture, Void void_) {
        try {
            runnable.run();
            vertxCompletableFuture.complete(null);
            return;
        }
        catch (Throwable throwable) {
            vertxCompletableFuture.completeExceptionally(throwable);
        }
    }

    public static VertxCompletableFuture from(Context context, CompletionStage completionStage) {
        return VertxCompletableFuture.from(context, completionStage.toCompletableFuture());
    }

    @Override
    public CompletableFuture thenAccept(Consumer consumer) {
        return this.thenAccept(consumer);
    }

    @Override
    public CompletionStage applyToEither(CompletionStage completionStage, Function function) {
        return this.applyToEither(completionStage, function);
    }

    @Override
    public VertxCompletableFuture thenAcceptBothAsync(CompletionStage completionStage, BiConsumer biConsumer) {
        return new VertxCompletableFuture(this.context, (CompletableFuture)super.thenAcceptBothAsync(completionStage, biConsumer, this.executor));
    }

    public static VertxCompletableFuture runBlockingAsyncOn(Context context, WorkerExecutor workerExecutor, Runnable runnable) {
        Objects.requireNonNull(runnable);
        VertxCompletableFuture vertxCompletableFuture = new VertxCompletableFuture(Objects.requireNonNull(context));
        Objects.requireNonNull(workerExecutor).executeBlocking(arg_0 -> VertxCompletableFuture.lambda$runBlockingAsyncOn$11(runnable, arg_0), false, arg_0 -> VertxCompletableFuture.lambda$runBlockingAsyncOn$12(vertxCompletableFuture, arg_0));
        return vertxCompletableFuture;
    }
}

