package io.trickle.util.concurrent;

import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.streams.ReadStream;
import io.vertx.core.streams.StreamBase;
import java.util.Objects;

public class DeferredStream implements ReadStream {
   public boolean stopped;
   public Context context;
   public int priority;
   public ReadStream stream;

   public void lambda$handler$0(Handler var1, Object var2) {
      var1.handle(var2);
      if (!this.stopped) {
         this.scheduleNext();
      }

   }

   public ReadStream pause() {
      this.stopped = true;
      return this.stream.pause();
   }

   public ReadStream fetch(long var1) {
      return this.fetch(var1);
   }

   public ReadStream handler(Handler var1) {
      return this.stream.handler(this::lambda$handler$0);
   }

   public DeferredStream(ReadStream var1) {
      this(var1, Vertx.currentContext());
   }

   public ReadStream exceptionHandler(Handler var1) {
      return this.stream.exceptionHandler(var1);
   }

   public DeferredStream lazyFetch() {
      this.stopped = false;
      this.scheduleNext();
      return this;
   }

   public ReadStream endHandler(Handler var1) {
      return this.stream.endHandler(var1);
   }

   public ReadStream resume() {
      this.stopped = false;
      this.scheduleNext();
      return this;
   }

   public DeferredStream fetch(long var1) {
      this.stopped = false;
      this.stream.fetch(var1);
      return this;
   }

   public DeferredStream priority(DeferredStream$Priority var1) {
      this.priority(DeferredStream$Priority.convert(var1));
      return this;
   }

   public void lambda$scheduleNext$1(Void var1) {
      this.stream.fetch((long)this.priority);
   }

   public StreamBase exceptionHandler(Handler var1) {
      return this.exceptionHandler(var1);
   }

   public DeferredStream(ReadStream var1, Context var2) {
      Objects.requireNonNull(var2, "Must be on Vert.x context");
      Objects.requireNonNull(var1, "Stream can not be null");
      this.stream = var1;
      this.stream.pause();
      this.context = var2;
      this.priority = DeferredStream$Priority.convert(DeferredStream$Priority.MEDIUM);
      this.stopped = false;
   }

   public void scheduleNext() {
      this.context.runOnContext(this::lambda$scheduleNext$1);
   }

   public DeferredStream priority(int var1) {
      this.priority = var1;
      return this;
   }
}
