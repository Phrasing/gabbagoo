package io.trickle.core.actor;

import io.trickle.App;
import io.trickle.task.Task;
import io.trickle.util.Storage;
import io.trickle.util.Utils;
import io.trickle.util.concurrent.ContextCompletableFuture;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.trickle.webclient.TaskApiClient;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.impl.HttpRequestImpl;
import io.vertx.ext.web.codec.BodyCodec;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Supplier;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MDC;

public abstract class TaskActor extends AbstractVerticle implements Actor {
   public static Logger netLogger = LogManager.getLogger("NET_LOGGER");
   public Task task;
   public boolean running = false;
   public int id;
   public Logger logger;
   public CompletableFuture sleepFuture = null;
   public TaskApiClient apiClient;

   public static CompletableFuture async$GETREQ$1(TaskActor param0, String param1, HttpRequest param2, Integer[] param3, String[] param4, CompletableFuture param5, HttpResponse param6, Throwable param7, int param8, Object param9) {
      // $FF: Couldn't be decompiled
   }

   public void netLogWarn(String var1) {
      this.netLog(Level.WARN, var1);
   }

   public void stop() {
      this.running = false;

      try {
         this.apiClient.close();
      } catch (Exception var2) {
         this.logger.error("Error on stop: {}", var2.getMessage());
      }

      this.logger.warn("Stopped.");
      super.stop();
   }

   public CompletableFuture execute(String var1, Function var2, Supplier var3, Object var4) {
      this.logger.info(var1);

      while(this.running) {
         CompletableFuture var10;
         CompletableFuture var10000;
         try {
            HttpRequest var6 = (HttpRequest)var3.get();
            HttpResponse var5;
            if (((HttpRequestImpl)var6).method().equals(HttpMethod.POST)) {
               var10000 = Request.send(var6, var4);
               if (!var10000.isDone()) {
                  var10 = var10000;
                  return var10.exceptionally(Function.identity()).thenCompose(TaskActor::async$execute);
               }

               var5 = (HttpResponse)var10000.join();
            } else {
               var10000 = Request.send(var6);
               if (!var10000.isDone()) {
                  var10 = var10000;
                  return var10.exceptionally(Function.identity()).thenCompose(TaskActor::async$execute);
               }

               var5 = (HttpResponse)var10000.join();
            }

            if (var5 != null) {
               int var7 = var5.statusCode();
               if (var2 == null) {
                  return CompletableFuture.completedFuture((Object)null);
               }

               Optional var8 = Optional.ofNullable(var2.apply(var5));
               if (var8.isPresent()) {
                  return CompletableFuture.completedFuture(var8.get());
               }

               int var12 = var5.statusCode();
               String var9 = "" + var12 + var5.statusMessage();
               this.logger.warn("Failed " + var1.toLowerCase(Locale.ROOT) + ": '{}'", var9);
               var10000 = VertxUtil.randomSleep((long)this.task.getMonitorDelay());
               if (!var10000.isDone()) {
                  var10 = var10000;
                  return var10.exceptionally(Function.identity()).thenCompose(TaskActor::async$execute);
               }

               var10000.join();
            } else {
               var10000 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
               if (!var10000.isDone()) {
                  var10 = var10000;
                  return var10.exceptionally(Function.identity()).thenCompose(TaskActor::async$execute);
               }

               var10000.join();
            }
         } catch (Throwable var11) {
            this.logger.error("Error " + var1.toLowerCase(Locale.ROOT) + ": {}", var11.getMessage());
            if (this.logger.isDebugEnabled()) {
               this.logger.debug(var11);
            }

            var10000 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
            if (!var10000.isDone()) {
               var10 = var10000;
               return var10.exceptionally(Function.identity()).thenCompose(TaskActor::async$execute);
            }

            var10000.join();
         }
      }

      return CompletableFuture.failedFuture(new Exception("Failed to execute " + var1));
   }

   public static boolean lambda$GETREQ$5(int var0, Integer var1) {
      return var1 == var0;
   }

   public void netLog(Level var1, String var2) {
      if (!var2.contains("Client is closed") && !var2.contains("VertxException") && !var2.contains("SSLHandshakeException") && !var2.contains("SslClosedEngineException") && !var2.contains("ProxyConnectException") && !var2.contains("HttpProxyConnectException") && !var2.contains("UnknownHostException") && !var2.contains("AnnotatedSocketException") && !var2.contains("AnnotatedConnectException")) {
         netLogger.log(var1, "{} {}", this.logger.getName(), var2);
      }
   }

   public Logger getLogger() {
      return this.logger;
   }

   public CompletableFuture GETREQ(String var1, HttpRequest var2, Integer var3, String... var4) {
      this.logger.info(var1);

      while(this.running) {
         CompletableFuture var8;
         CompletableFuture var10000;
         try {
            int var5 = var4 != null && (var3 == null || var3 != 302) ? 0 : 1;
            HttpResponse var10;
            if (var5 != 0) {
               var10000 = Request.send(var2.as(BodyCodec.none()));
               if (!var10000.isDone()) {
                  var8 = var10000;
                  return var8.exceptionally(Function.identity()).thenCompose(TaskActor::async$GETREQ);
               }

               var10 = (HttpResponse)var10000.join();
            } else {
               var10000 = Request.send(var2);
               if (!var10000.isDone()) {
                  var8 = var10000;
                  return var8.exceptionally(Function.identity()).thenCompose(TaskActor::async$GETREQ);
               }

               var10 = (HttpResponse)var10000.join();
            }

            HttpResponse var6 = var10;
            if (var6 != null) {
               boolean var7;
               if (var6.statusCode() == 302) {
                  var7 = var4 == null || var6.getHeader("location").contains(var4[0]);
               } else {
                  var7 = var4 == null || Utils.containsAllWords(var6.bodyAsString(), var4);
               }

               if ((var3 == null || var6.statusCode() == var3) && var7) {
                  return CompletableFuture.completedFuture(var5 != 0 ? var6.getHeader("location") : var6.bodyAsString());
               }

               Logger var11 = this.logger;
               String var10001 = "Failed " + var1.toLowerCase(Locale.ROOT) + ": '{}'";
               int var10002 = var6.statusCode();
               var11.warn(var10001, "" + var10002 + var6.statusMessage());
            }

            var10000 = VertxUtil.randomSleep((long)this.task.getMonitorDelay());
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(TaskActor::async$GETREQ);
            }

            var10000.join();
         } catch (Throwable var9) {
            this.logger.error("Error " + var1.toLowerCase(Locale.ROOT) + ": {}", var9.getMessage());
            var10000 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(TaskActor::async$GETREQ);
            }

            var10000.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public TaskActor(int var1) {
      this.id = var1;
      this.logger = LogManager.getLogger(String.format("[%s][TASK-%s]", this.getClass().getSimpleName().toUpperCase(), String.format("%04d", this.id)));
   }

   public CompletableFuture GETREQ(String var1, HttpRequest var2, Integer[] var3, String... var4) {
      this.logger.info(var1);

      while(this.running) {
         CompletableFuture var8;
         CompletableFuture var10000;
         try {
            var10000 = Request.send(var2);
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(TaskActor::async$GETREQ$1);
            }

            HttpResponse var5 = (HttpResponse)var10000.join();
            if (var5 != null) {
               int var7 = var5.statusCode();
               boolean var6;
               if (var7 == 302) {
                  var6 = var4 == null || Arrays.stream(var4).anyMatch(TaskActor::lambda$GETREQ$4);
               } else {
                  var6 = var4 == null || Utils.containsAllWords(var5.bodyAsString(), var4);
               }

               if ((var3 == null || Arrays.stream(var3).anyMatch(TaskActor::lambda$GETREQ$5)) && var6) {
                  return CompletableFuture.completedFuture(var7 == 302 ? var5.getHeader("location") : var5.bodyAsString());
               }

               Logger var10 = this.logger;
               String var10001 = "Failed " + var1.toLowerCase(Locale.ROOT) + ": '{}'";
               int var10002 = var5.statusCode();
               var10.warn(var10001, "" + var10002 + var5.statusMessage());
            }

            var10000 = VertxUtil.randomSleep((long)this.task.getMonitorDelay());
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(TaskActor::async$GETREQ$1);
            }

            var10000.join();
         } catch (Throwable var9) {
            this.logger.error("Error " + var1.toLowerCase(Locale.ROOT) + ": {}", var9.getMessage());
            var10000 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(TaskActor::async$GETREQ$1);
            }

            var10000.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public TaskApiClient getClient() {
      return this.apiClient;
   }

   public CompletableFuture sleep(int var1) {
      if (this.sleepFuture != null) {
         this.sleepFuture = null;
      }

      this.sleepFuture = new ContextCompletableFuture();
      super.vertx.setTimer((long)var1, this::lambda$sleep$3);
      return this.sleepFuture;
   }

   public void lambda$start$2(Void var1) {
      this.run().whenComplete(this::lambda$start$0).exceptionally(TaskActor::lambda$start$1);
   }

   public static CompletableFuture async$execute(TaskActor param0, String param1, Function param2, Supplier param3, Object param4, HttpRequest param5, CompletableFuture param6, HttpResponse param7, int param8, Optional param9, String param10, Throwable param11, int param12, Object param13) {
      // $FF: Couldn't be decompiled
   }

   public static boolean lambda$GETREQ$4(HttpResponse var0, String var1) {
      return var0.getHeader("location").contains(var1);
   }

   public void netLogError(String var1) {
      this.netLog(Level.ERROR, var1);
   }

   public static CompletableFuture async$GETREQ(TaskActor param0, String param1, HttpRequest param2, Integer param3, String[] param4, int param5, CompletableFuture param6, HttpResponse param7, Throwable param8, int param9, Object param10) {
      // $FF: Couldn't be decompiled
   }

   public static Void lambda$start$1(Throwable var0) {
      return null;
   }

   public CompletableFuture randomSleep(int var1) {
      try {
         if (var1 > 0) {
            int var2 = ThreadLocalRandom.current().nextInt((int)Math.min((double)var1, (double)var1 / Double.longBitsToDouble(4608308318706860032L)), (int)((double)var1 * Double.longBitsToDouble(4608308318706860032L)));
            return this.sleep(var2);
         }
      } catch (Throwable var3) {
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public void lambda$sleep$3(Long var1) {
      try {
         this.sleepFuture.complete((Object)null);
      } catch (Throwable var3) {
      }

   }

   public void start() {
      try {
         MDC.put("version", "1.0.278");
         MDC.put("user", Storage.ACCESS_KEY);
         MDC.put("session", App.SESSION_HASH);
         this.logger.info("Starting.");
         this.running = true;
         super.context.runOnContext(this::lambda$start$2);
      } catch (Throwable var2) {
      }

   }

   public void setClient(TaskApiClient var1) {
      this.apiClient = var1;
   }

   public void netLogInfo(String var1) {
      this.netLog(Level.INFO, var1);
   }

   public void lambda$start$0(Void var1, Throwable var2) {
      super.vertx.undeploy(super.deploymentID());
   }
}
