package io.trickle.task.antibot.impl.px.payload.token;

import io.trickle.task.antibot.impl.px.payload.Payload;
import io.trickle.task.sites.Site;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class InitPayload implements Payload {
   public Devices$Device device;

   public static CompletableFuture fetch() {
      try {
         CompletableFuture var10000 = getDeviceFromAPI();
         if (!var10000.isDone()) {
            CompletableFuture var1 = var10000;
            return var1.exceptionally(Function.identity()).thenCompose(InitPayload::async$fetch);
         } else {
            Devices$Device var0 = (Devices$Device)var10000.join();
            return CompletableFuture.completedFuture(new InitPayload(var0));
         }
      } catch (Throwable var2) {
         return CompletableFuture.failedFuture(var2);
      }
   }

   public InitPayload(Devices$Device var1) {
      this.device = var1;
   }

   public static CompletableFuture getDeviceFromAPI() {
      return Devices.deviceFromAPI();
   }

   public static CompletableFuture async$fetch(CompletableFuture var0, int var1, Object var2) {
      Throwable var8;
      label26: {
         CompletableFuture var10000;
         boolean var10001;
         switch (var1) {
            case 0:
               try {
                  var10000 = getDeviceFromAPI();
                  if (!var10000.isDone()) {
                     CompletableFuture var7 = var10000;
                     return var7.exceptionally(Function.identity()).thenCompose(InitPayload::async$fetch);
                  }
                  break;
               } catch (Throwable var4) {
                  var8 = var4;
                  var10001 = false;
                  break label26;
               }
            case 1:
               var10000 = var0;
               break;
            default:
               throw new IllegalArgumentException();
         }

         try {
            Devices$Device var6 = (Devices$Device)var10000.join();
            return CompletableFuture.completedFuture(new InitPayload(var6));
         } catch (Throwable var3) {
            var8 = var3;
            var10001 = false;
         }
      }

      Throwable var5 = var8;
      return CompletableFuture.failedFuture(var5);
   }

   public Buffer asBuffer(Site var1) {
      // $FF: Couldn't be decompiled
   }

   public InitPayload() {
      this.device = Devices.random();
   }

   public MultiMap asForm() {
      throw new RuntimeException("Unsupported Operation");
   }
}
