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

public class VertxCompletableFuture extends CompletableFuture implements CompletionStage {
   public Executor executor;
   public Context context;

   public VertxCompletableFuture runAfterBothAsync(CompletionStage var1, Runnable var2, Executor var3) {
      return new VertxCompletableFuture(this.context, super.runAfterBothAsync(var1, var2, var3));
   }

   public VertxCompletableFuture thenComposeAsync(Function var1) {
      return new VertxCompletableFuture(this.context, super.thenComposeAsync(var1, this.executor));
   }

   public static void lambda$from$3(VertxCompletableFuture var0, Object var1, Throwable var2, Void var3) {
      var0.complete(var1, var2);
   }

   public CompletionStage applyToEitherAsync(CompletionStage var1, Function var2, Executor var3) {
      return this.applyToEitherAsync(var1, var2, var3);
   }

   public CompletionStage thenAcceptAsync(Consumer var1) {
      return this.thenAcceptAsync(var1);
   }

   public CompletionStage thenAcceptBothAsync(CompletionStage var1, BiConsumer var2, Executor var3) {
      return this.thenAcceptBothAsync(var1, var2, var3);
   }

   public VertxCompletableFuture toCompletableFuture() {
      return this;
   }

   public CompletableFuture applyToEitherAsync(CompletionStage var1, Function var2, Executor var3) {
      return this.applyToEitherAsync(var1, var2, var3);
   }

   public static void lambda$runBlockingAsync$9(Runnable var0, Promise var1) {
      try {
         var0.run();
         var1.complete((Object)null);
      } catch (Throwable var3) {
         var1.fail(var3);
      }

   }

   public VertxCompletableFuture thenRunAsync(Runnable var1, Executor var2) {
      return new VertxCompletableFuture(this.context, super.thenRunAsync(var1, var2));
   }

   public static VertxCompletableFuture allOf(Context var0, CompletableFuture... var1) {
      CompletableFuture var2 = CompletableFuture.allOf(var1);
      return from(var0, var2);
   }

   public void complete(Object var1, Throwable var2) {
      if (var2 == null) {
         super.complete(var1);
      } else {
         super.completeExceptionally(var2);
      }

   }

   public CompletableFuture whenCompleteAsync(BiConsumer var1, Executor var2) {
      return this.whenCompleteAsync(var1, var2);
   }

   public VertxCompletableFuture thenRunAsync(Runnable var1) {
      return new VertxCompletableFuture(this.context, super.thenRunAsync(var1, this.executor));
   }

   public static void lambda$supplyBlockingAsync$13(Supplier var0, Promise var1) {
      try {
         var1.complete(var0.get());
      } catch (Throwable var3) {
         var1.fail(var3);
      }

   }

   public CompletableFuture runAfterBothAsync(CompletionStage var1, Runnable var2) {
      return this.runAfterBothAsync(var1, var2);
   }

   public CompletableFuture thenCombineAsync(CompletionStage var1, BiFunction var2, Executor var3) {
      return this.thenCombineAsync(var1, var2, var3);
   }

   public static VertxCompletableFuture supplyBlockingAsyncOn(Vertx var0, WorkerExecutor var1, Supplier var2) {
      return supplyBlockingAsyncOn(((Vertx)Objects.requireNonNull(var0)).getOrCreateContext(), var1, var2);
   }

   public VertxCompletableFuture thenAcceptAsync(Consumer var1) {
      return new VertxCompletableFuture(this.context, super.thenAcceptAsync(var1, this.executor));
   }

   public VertxCompletableFuture thenAccept(Consumer var1) {
      return new VertxCompletableFuture(this.context, super.thenAccept(var1));
   }

   public CompletableFuture thenApply(Function var1) {
      return this.thenApply(var1);
   }

   public VertxCompletableFuture thenAcceptAsync(Consumer var1, Executor var2) {
      return new VertxCompletableFuture(this.context, super.thenAcceptAsync(var1, var2));
   }

   public VertxCompletableFuture thenApplyAsync(Function var1) {
      return new VertxCompletableFuture(this.context, super.thenApplyAsync(var1, this.executor));
   }

   public static void lambda$supplyBlockingAsyncOn$15(Supplier var0, Promise var1) {
      try {
         var1.complete(var0.get());
      } catch (Throwable var3) {
         var1.fail(var3);
      }

   }

   public CompletableFuture applyToEither(CompletionStage var1, Function var2) {
      return this.applyToEither(var1, var2);
   }

   public CompletionStage thenRunAsync(Runnable var1) {
      return this.thenRunAsync(var1);
   }

   public static void lambda$from$5(VertxCompletableFuture var0, AsyncResult var1, Void var2) {
      var0.completeFromAsyncResult(var1);
   }

   public CompletableFuture runAfterBoth(CompletionStage var1, Runnable var2) {
      return this.runAfterBoth(var1, var2);
   }

   public CompletableFuture thenAcceptBoth(CompletionStage var1, BiConsumer var2) {
      return this.thenAcceptBoth(var1, var2);
   }

   public VertxCompletableFuture handleAsync(BiFunction var1, Executor var2) {
      return new VertxCompletableFuture(this.context, super.handleAsync(var1, var2));
   }

   public VertxCompletableFuture() {
      this(Vertx.currentContext());
   }

   public VertxCompletableFuture applyToEitherAsync(CompletionStage var1, Function var2, Executor var3) {
      return new VertxCompletableFuture(this.context, super.applyToEitherAsync(var1, var2, var3));
   }

   public VertxCompletableFuture runAfterBothAsync(CompletionStage var1, Runnable var2) {
      return new VertxCompletableFuture(this.context, super.runAfterBothAsync(var1, var2, this.executor));
   }

   public VertxCompletableFuture thenApply(Function var1) {
      return new VertxCompletableFuture(this.context, super.thenApply(var1));
   }

   public CompletableFuture thenAcceptAsync(Consumer var1) {
      return this.thenAcceptAsync(var1);
   }

   public VertxCompletableFuture applyToEitherAsync(CompletionStage var1, Function var2) {
      return new VertxCompletableFuture(this.context, super.applyToEitherAsync(var1, var2, this.executor));
   }

   public CompletableFuture handleAsync(BiFunction var1, Executor var2) {
      return this.handleAsync(var1, var2);
   }

   public CompletionStage applyToEitherAsync(CompletionStage var1, Function var2) {
      return this.applyToEitherAsync(var1, var2);
   }

   public CompletableFuture thenRunAsync(Runnable var1) {
      return this.thenRunAsync(var1);
   }

   public CompletionStage runAfterEitherAsync(CompletionStage var1, Runnable var2) {
      return this.runAfterEitherAsync(var1, var2);
   }

   public static void lambda$toFuture$18(CompletableFuture var0, AsyncResult var1) {
      if (var1.succeeded()) {
         var0.complete(var1.result());
      } else {
         var0.completeExceptionally(var1.cause());
      }

   }

   public CompletionStage thenComposeAsync(Function var1, Executor var2) {
      return this.thenComposeAsync(var1, var2);
   }

   public static void lambda$supplyBlockingAsyncOn$16(VertxCompletableFuture var0, AsyncResult var1) {
      if (var1.failed()) {
         var0.completeExceptionally(var1.cause());
      } else {
         var0.complete(var1.result());
      }

   }

   public CompletionStage thenApplyAsync(Function var1, Executor var2) {
      return this.thenApplyAsync(var1, var2);
   }

   public static VertxCompletableFuture supplyBlockingAsync(Vertx var0, Supplier var1) {
      return supplyBlockingAsync(((Vertx)Objects.requireNonNull(var0)).getOrCreateContext(), var1);
   }

   public Future toFuture() {
      return toFuture(this);
   }

   public CompletableFuture toCompletableFuture() {
      return this.toCompletableFuture();
   }

   public CompletableFuture handleAsync(BiFunction var1) {
      return this.handleAsync(var1);
   }

   public CompletionStage thenAcceptBothAsync(CompletionStage var1, BiConsumer var2) {
      return this.thenAcceptBothAsync(var1, var2);
   }

   public static void lambda$withContext$19(VertxCompletableFuture var0, Object var1, Throwable var2) {
      if (var2 != null) {
         var0.completeExceptionally(var2);
      } else {
         var0.complete(var1);
      }

   }

   public CompletableFuture thenAcceptBothAsync(CompletionStage var1, BiConsumer var2) {
      return this.thenAcceptBothAsync(var1, var2);
   }

   public static VertxCompletableFuture anyOf(Vertx var0, CompletableFuture... var1) {
      CompletableFuture var2 = CompletableFuture.anyOf(var1);
      return from(var0, var2);
   }

   public static void lambda$runAsync$8(Runnable var0, VertxCompletableFuture var1, Void var2) {
      try {
         var0.run();
         var1.complete((Object)null);
      } catch (Throwable var4) {
         var1.completeExceptionally(var4);
      }

   }

   public CompletableFuture thenComposeAsync(Function var1) {
      return this.thenComposeAsync(var1);
   }

   public CompletableFuture thenAcceptBothAsync(CompletionStage var1, BiConsumer var2, Executor var3) {
      return this.thenAcceptBothAsync(var1, var2, var3);
   }

   public static VertxCompletableFuture from(CompletionStage var0) {
      return from(Vertx.currentContext(), var0);
   }

   public CompletionStage thenApplyAsync(Function var1) {
      return this.thenApplyAsync(var1);
   }

   public CompletableFuture thenCombine(CompletionStage var1, BiFunction var2) {
      return this.thenCombine(var1, var2);
   }

   public CompletableFuture thenRunAsync(Runnable var1, Executor var2) {
      return this.thenRunAsync(var1, var2);
   }

   public static VertxCompletableFuture supplyBlockingAsync(Context var0, Supplier var1) {
      Objects.requireNonNull(var1);
      VertxCompletableFuture var2 = new VertxCompletableFuture(var0);
      var0.executeBlocking(VertxCompletableFuture::lambda$supplyBlockingAsync$13, false, VertxCompletableFuture::lambda$supplyBlockingAsync$14);
      return var2;
   }

   public static VertxCompletableFuture supplyAsync(Vertx var0, Supplier var1) {
      return supplyAsync(((Vertx)Objects.requireNonNull(var0)).getOrCreateContext(), var1);
   }

   public CompletableFuture applyToEitherAsync(CompletionStage var1, Function var2) {
      return this.applyToEitherAsync(var1, var2);
   }

   public VertxCompletableFuture whenCompleteAsync(BiConsumer var1) {
      return new VertxCompletableFuture(this.context, super.whenCompleteAsync(var1, this.executor));
   }

   public static VertxCompletableFuture from(Vertx var0, Future var1) {
      return from(var0.getOrCreateContext(), var1);
   }

   public void completeFromAsyncResult(AsyncResult var1) {
      if (var1.succeeded()) {
         super.complete(var1.result());
      } else {
         super.completeExceptionally(var1.cause());
      }

   }

   public CompletableFuture runAfterEitherAsync(CompletionStage var1, Runnable var2, Executor var3) {
      return this.runAfterEitherAsync(var1, var2, var3);
   }

   public CompletableFuture thenApplyAsync(Function var1, Executor var2) {
      return this.thenApplyAsync(var1, var2);
   }

   public CompletionStage acceptEitherAsync(CompletionStage var1, Consumer var2) {
      return this.acceptEitherAsync(var1, var2);
   }

   public VertxCompletableFuture runAfterEitherAsync(CompletionStage var1, Runnable var2) {
      return new VertxCompletableFuture(this.context, super.runAfterEitherAsync(var1, var2, this.executor));
   }

   public CompletionStage runAfterEitherAsync(CompletionStage var1, Runnable var2, Executor var3) {
      return this.runAfterEitherAsync(var1, var2, var3);
   }

   public CompletionStage thenComposeAsync(Function var1) {
      return this.thenComposeAsync(var1);
   }

   public VertxCompletableFuture thenCombineAsync(CompletionStage var1, BiFunction var2) {
      return new VertxCompletableFuture(this.context, super.thenCombineAsync(var1, var2, this.executor));
   }

   public CompletionStage runAfterBoth(CompletionStage var1, Runnable var2) {
      return this.runAfterBoth(var1, var2);
   }

   public CompletableFuture whenComplete(BiConsumer var1) {
      return this.whenComplete(var1);
   }

   public CompletionStage thenCombineAsync(CompletionStage var1, BiFunction var2, Executor var3) {
      return this.thenCombineAsync(var1, var2, var3);
   }

   public static void lambda$from$6(Context var0, VertxCompletableFuture var1, AsyncResult var2) {
      if (var0 == Vertx.currentContext()) {
         var1.completeFromAsyncResult(var2);
      } else {
         var1.context.runOnContext(VertxCompletableFuture::lambda$from$5);
      }

   }

   public VertxCompletableFuture(Context var1) {
      this.context = (Context)Objects.requireNonNull(var1);
      this.executor = VertxCompletableFuture::lambda$new$1;
   }

   public CompletableFuture thenRun(Runnable var1) {
      return this.thenRun(var1);
   }

   public static void lambda$supplyAsync$7(VertxCompletableFuture var0, Supplier var1, Void var2) {
      try {
         var0.complete(var1.get());
      } catch (Throwable var4) {
         var0.completeExceptionally(var4);
      }

   }

   public static void lambda$new$0(Runnable var0, Void var1) {
      var0.run();
   }

   public CompletionStage thenCompose(Function var1) {
      return this.thenCompose(var1);
   }

   public CompletableFuture acceptEitherAsync(CompletionStage var1, Consumer var2, Executor var3) {
      return this.acceptEitherAsync(var1, var2, var3);
   }

   public CompletionStage whenCompleteAsync(BiConsumer var1, Executor var2) {
      return this.whenCompleteAsync(var1, var2);
   }

   public VertxCompletableFuture handleAsync(BiFunction var1) {
      return new VertxCompletableFuture(this.context, super.handleAsync(var1, this.executor));
   }

   public CompletionStage whenComplete(BiConsumer var1) {
      return this.whenComplete(var1);
   }

   public CompletionStage thenRun(Runnable var1) {
      return this.thenRun(var1);
   }

   public CompletableFuture thenAcceptAsync(Consumer var1, Executor var2) {
      return this.thenAcceptAsync(var1, var2);
   }

   public void lambda$new$2(Object var1, Throwable var2) {
      if (var2 != null) {
         this.completeExceptionally(var2);
      } else {
         this.complete(var1);
      }

   }

   public VertxCompletableFuture thenRun(Runnable var1) {
      return new VertxCompletableFuture(this.context, super.thenRun(var1));
   }

   public CompletionStage handleAsync(BiFunction var1, Executor var2) {
      return this.handleAsync(var1, var2);
   }

   public VertxCompletableFuture acceptEither(CompletionStage var1, Consumer var2) {
      return new VertxCompletableFuture(this.context, super.acceptEither(var1, var2));
   }

   public static void lambda$runBlockingAsync$10(VertxCompletableFuture var0, AsyncResult var1) {
      if (var1.failed()) {
         var0.completeExceptionally(var1.cause());
      } else {
         var0.complete((Void)var1.result());
      }

   }

   public static VertxCompletableFuture from(Future var0) {
      return from(Vertx.currentContext(), var0);
   }

   public VertxCompletableFuture acceptEitherAsync(CompletionStage var1, Consumer var2) {
      return new VertxCompletableFuture(this.context, super.acceptEitherAsync(var1, var2, this.executor));
   }

   public CompletableFuture thenAccept(Consumer var1) {
      return this.thenAccept(var1);
   }

   public static VertxCompletableFuture runBlockingAsync(Vertx var0, Runnable var1) {
      return runBlockingAsync(((Vertx)Objects.requireNonNull(var0)).getOrCreateContext(), var1);
   }

   public CompletionStage whenCompleteAsync(BiConsumer var1) {
      return this.whenCompleteAsync(var1);
   }

   public static void lambda$new$1(Context var0, Runnable var1) {
      var0.runOnContext(VertxCompletableFuture::lambda$new$0);
   }

   public static VertxCompletableFuture from(Context var0, CompletableFuture var1) {
      VertxCompletableFuture var2 = new VertxCompletableFuture((Context)Objects.requireNonNull(var0));
      ((CompletableFuture)Objects.requireNonNull(var1)).whenComplete(VertxCompletableFuture::lambda$from$4);
      return var2;
   }

   public VertxCompletableFuture(Context var1, CompletableFuture var2) {
      this(var1);
      ((CompletableFuture)Objects.requireNonNull(var2)).whenComplete(this::lambda$new$2);
   }

   public static VertxCompletableFuture supplyAsync(Context var0, Supplier var1) {
      Objects.requireNonNull(var1);
      VertxCompletableFuture var2 = new VertxCompletableFuture((Context)Objects.requireNonNull(var0));
      var0.runOnContext(VertxCompletableFuture::lambda$supplyAsync$7);
      return var2;
   }

   public CompletionStage thenAcceptAsync(Consumer var1, Executor var2) {
      return this.thenAcceptAsync(var1, var2);
   }

   public static VertxCompletableFuture from(CompletableFuture var0) {
      return from(Vertx.currentContext(), var0);
   }

   public VertxCompletableFuture acceptEitherAsync(CompletionStage var1, Consumer var2, Executor var3) {
      return new VertxCompletableFuture(this.context, super.acceptEitherAsync(var1, var2, var3));
   }

   public Context context() {
      return this.context;
   }

   public CompletionStage thenApply(Function var1) {
      return this.thenApply(var1);
   }

   public VertxCompletableFuture whenComplete(BiConsumer var1) {
      return new VertxCompletableFuture(this.context, super.whenComplete(var1));
   }

   public VertxCompletableFuture thenComposeAsync(Function var1, Executor var2) {
      return new VertxCompletableFuture(this.context, super.thenComposeAsync(var1, var2));
   }

   public CompletableFuture acceptEitherAsync(CompletionStage var1, Consumer var2) {
      return this.acceptEitherAsync(var1, var2);
   }

   public static void lambda$runBlockingAsyncOn$12(VertxCompletableFuture var0, AsyncResult var1) {
      if (var1.failed()) {
         var0.completeExceptionally(var1.cause());
      } else {
         var0.complete((Void)var1.result());
      }

   }

   public CompletableFuture thenCompose(Function var1) {
      return this.thenCompose(var1);
   }

   public VertxCompletableFuture applyToEither(CompletionStage var1, Function var2) {
      return new VertxCompletableFuture(this.context, super.applyToEither(var1, var2));
   }

   public CompletableFuture thenApplyAsync(Function var1) {
      return this.thenApplyAsync(var1);
   }

   public CompletableFuture thenComposeAsync(Function var1, Executor var2) {
      return this.thenComposeAsync(var1, var2);
   }

   public CompletionStage runAfterEither(CompletionStage var1, Runnable var2) {
      return this.runAfterEither(var1, var2);
   }

   public CompletionStage handleAsync(BiFunction var1) {
      return this.handleAsync(var1);
   }

   public static void lambda$supplyBlockingAsync$14(VertxCompletableFuture var0, AsyncResult var1) {
      if (var1.failed()) {
         var0.completeExceptionally(var1.cause());
      } else {
         var0.complete(var1.result());
      }

   }

   public static void lambda$runBlockingAsyncOn$11(Runnable var0, Promise var1) {
      try {
         var0.run();
         var1.complete((Object)null);
      } catch (Throwable var3) {
         var1.fail(var3);
      }

   }

   public VertxCompletableFuture thenCombineAsync(CompletionStage var1, BiFunction var2, Executor var3) {
      return new VertxCompletableFuture(this.context, super.thenCombineAsync(var1, var2, var3));
   }

   public static VertxCompletableFuture from(Context var0, CompletionStage var1) {
      return from(var0, var1.toCompletableFuture());
   }

   public VertxCompletableFuture runAfterEither(CompletionStage var1, Runnable var2) {
      return new VertxCompletableFuture(this.context, super.runAfterEither(var1, var2));
   }

   public VertxCompletableFuture whenCompleteAsync(BiConsumer var1, Executor var2) {
      return new VertxCompletableFuture(this.context, super.whenCompleteAsync(var1, var2));
   }

   public CompletionStage acceptEitherAsync(CompletionStage var1, Consumer var2, Executor var3) {
      return this.acceptEitherAsync(var1, var2, var3);
   }

   public CompletableFuture runAfterBothAsync(CompletionStage var1, Runnable var2, Executor var3) {
      return this.runAfterBothAsync(var1, var2, var3);
   }

   public VertxCompletableFuture thenCombine(CompletionStage var1, BiFunction var2) {
      return new VertxCompletableFuture(this.context, super.thenCombine(var1, var2));
   }

   public VertxCompletableFuture handle(BiFunction var1) {
      return new VertxCompletableFuture(this.context, super.handle(var1));
   }

   public CompletableFuture runAfterEither(CompletionStage var1, Runnable var2) {
      return this.runAfterEither(var1, var2);
   }

   public CompletionStage handle(BiFunction var1) {
      return this.handle(var1);
   }

   public static void lambda$from$4(Context var0, VertxCompletableFuture var1, Object var2, Throwable var3) {
      if (var0 == Vertx.currentContext()) {
         var1.complete(var2, var3);
      } else {
         var1.context.runOnContext(VertxCompletableFuture::lambda$from$3);
      }

   }

   public static VertxCompletableFuture runBlockingAsync(Context var0, Runnable var1) {
      Objects.requireNonNull(var1);
      VertxCompletableFuture var2 = new VertxCompletableFuture((Context)Objects.requireNonNull(var0));
      var0.executeBlocking(VertxCompletableFuture::lambda$runBlockingAsync$9, false, VertxCompletableFuture::lambda$runBlockingAsync$10);
      return var2;
   }

   public VertxCompletableFuture thenCompose(Function var1) {
      return new VertxCompletableFuture(this.context, super.thenCompose(var1));
   }

   public static VertxCompletableFuture supplyBlockingAsyncOn(Context var0, WorkerExecutor var1, Supplier var2) {
      Objects.requireNonNull(var2);
      VertxCompletableFuture var3 = new VertxCompletableFuture(var0);
      ((WorkerExecutor)Objects.requireNonNull(var1)).executeBlocking(VertxCompletableFuture::lambda$supplyBlockingAsyncOn$15, false, VertxCompletableFuture::lambda$supplyBlockingAsyncOn$16);
      return var3;
   }

   public static VertxCompletableFuture runAsync(Vertx var0, Runnable var1) {
      return runAsync(((Vertx)Objects.requireNonNull(var0)).getOrCreateContext(), var1);
   }

   public VertxCompletableFuture withContext() {
      Context var1 = (Context)Objects.requireNonNull(Vertx.currentContext());
      return this.withContext(var1);
   }

   public CompletableFuture whenCompleteAsync(BiConsumer var1) {
      return this.whenCompleteAsync(var1);
   }

   public VertxCompletableFuture withContext(Vertx var1) {
      return this.withContext(((Vertx)Objects.requireNonNull(var1)).getOrCreateContext());
   }

   public CompletionStage thenCombineAsync(CompletionStage var1, BiFunction var2) {
      return this.thenCombineAsync(var1, var2);
   }

   public static VertxCompletableFuture from(Context var0, Future var1) {
      VertxCompletableFuture var2 = new VertxCompletableFuture((Context)Objects.requireNonNull(var0));
      ((Future)Objects.requireNonNull(var1)).onComplete(VertxCompletableFuture::lambda$from$6);
      return var2;
   }

   public CompletionStage thenCombine(CompletionStage var1, BiFunction var2) {
      return this.thenCombine(var1, var2);
   }

   public CompletionStage thenRunAsync(Runnable var1, Executor var2) {
      return this.thenRunAsync(var1, var2);
   }

   public VertxCompletableFuture(Vertx var1) {
      this(((Vertx)Objects.requireNonNull(var1)).getOrCreateContext());
   }

   public CompletionStage thenAccept(Consumer var1) {
      return this.thenAccept(var1);
   }

   public VertxCompletableFuture thenAcceptBothAsync(CompletionStage var1, BiConsumer var2) {
      return new VertxCompletableFuture(this.context, super.thenAcceptBothAsync(var1, var2, this.executor));
   }

   public static VertxCompletableFuture runBlockingAsyncOn(Context var0, WorkerExecutor var1, Runnable var2) {
      Objects.requireNonNull(var2);
      VertxCompletableFuture var3 = new VertxCompletableFuture((Context)Objects.requireNonNull(var0));
      ((WorkerExecutor)Objects.requireNonNull(var1)).executeBlocking(VertxCompletableFuture::lambda$runBlockingAsyncOn$11, false, VertxCompletableFuture::lambda$runBlockingAsyncOn$12);
      return var3;
   }

   public static VertxCompletableFuture allOf(Vertx var0, CompletableFuture... var1) {
      CompletableFuture var2 = CompletableFuture.allOf(var1);
      return from(var0, var2);
   }

   public static VertxCompletableFuture from(Vertx var0, CompletableFuture var1) {
      return from(var0.getOrCreateContext(), var1);
   }

   public CompletionStage runAfterBothAsync(CompletionStage var1, Runnable var2, Executor var3) {
      return this.runAfterBothAsync(var1, var2, var3);
   }

   public static VertxCompletableFuture anyOf(Context var0, CompletableFuture... var1) {
      CompletableFuture var2 = CompletableFuture.anyOf(var1);
      return from(var0, var2);
   }

   public VertxCompletableFuture thenAcceptBothAsync(CompletionStage var1, BiConsumer var2, Executor var3) {
      return new VertxCompletableFuture(this.context, super.thenAcceptBothAsync(var1, var2, var3));
   }

   public VertxCompletableFuture runAfterEitherAsync(CompletionStage var1, Runnable var2, Executor var3) {
      return new VertxCompletableFuture(this.context, super.runAfterEitherAsync(var1, var2, var3));
   }

   public VertxCompletableFuture withContext(Context var1) {
      VertxCompletableFuture var2 = new VertxCompletableFuture((Context)Objects.requireNonNull(var1));
      this.whenComplete(VertxCompletableFuture::lambda$withContext$19);
      return var2;
   }

   public static Future toFuture(CompletableFuture var0) {
      Promise var1 = Promise.promise();
      ((CompletableFuture)Objects.requireNonNull(var0)).whenComplete(VertxCompletableFuture::lambda$toFuture$17);
      Future var2 = var1.future();
      var2.onComplete(VertxCompletableFuture::lambda$toFuture$18);
      return var2;
   }

   public static VertxCompletableFuture runAsync(Context var0, Runnable var1) {
      Objects.requireNonNull(var1);
      VertxCompletableFuture var2 = new VertxCompletableFuture(var0);
      var0.runOnContext(VertxCompletableFuture::lambda$runAsync$8);
      return var2;
   }

   public CompletableFuture acceptEither(CompletionStage var1, Consumer var2) {
      return this.acceptEither(var1, var2);
   }

   public CompletionStage runAfterBothAsync(CompletionStage var1, Runnable var2) {
      return this.runAfterBothAsync(var1, var2);
   }

   public CompletableFuture thenCombineAsync(CompletionStage var1, BiFunction var2) {
      return this.thenCombineAsync(var1, var2);
   }

   public CompletableFuture handle(BiFunction var1) {
      return this.handle(var1);
   }

   public static VertxCompletableFuture from(Vertx var0, CompletionStage var1) {
      return from(var0.getOrCreateContext(), var1);
   }

   public VertxCompletableFuture runAfterBoth(CompletionStage var1, Runnable var2) {
      return new VertxCompletableFuture(this.context, super.runAfterBoth(var1, var2));
   }

   public static void lambda$toFuture$17(Promise var0, Object var1, Throwable var2) {
      if (var2 != null) {
         var0.fail(var2);
      } else {
         var0.complete(var1);
      }

   }

   public CompletionStage thenAcceptBoth(CompletionStage var1, BiConsumer var2) {
      return this.thenAcceptBoth(var1, var2);
   }

   public CompletionStage acceptEither(CompletionStage var1, Consumer var2) {
      return this.acceptEither(var1, var2);
   }

   public VertxCompletableFuture thenAcceptBoth(CompletionStage var1, BiConsumer var2) {
      return new VertxCompletableFuture(this.context, super.thenAcceptBoth(var1, var2));
   }

   public CompletionStage applyToEither(CompletionStage var1, Function var2) {
      return this.applyToEither(var1, var2);
   }

   public static VertxCompletableFuture runBlockingAsyncOn(Vertx var0, WorkerExecutor var1, Runnable var2) {
      return runBlockingAsyncOn(((Vertx)Objects.requireNonNull(var0)).getOrCreateContext(), var1, var2);
   }

   public VertxCompletableFuture thenApplyAsync(Function var1, Executor var2) {
      return new VertxCompletableFuture(this.context, super.thenApplyAsync(var1, var2));
   }

   public CompletableFuture runAfterEitherAsync(CompletionStage var1, Runnable var2) {
      return this.runAfterEitherAsync(var1, var2);
   }
}
