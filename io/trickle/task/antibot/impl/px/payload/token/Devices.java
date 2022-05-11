package io.trickle.task.antibot.impl.px.payload.token;

import io.trickle.core.VertxSingleton;
import io.vertx.core.json.JsonObject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public enum Devices {
   GALAXY_S20_FE,
   G50_Plus,
   GALAXY_S8_PLUS,
   GALAXY_S21_ULTRA,
   GALAXY_S20,
   GALAXY_A71,
   GALAXY_S10_GALAXY_TAB_S2,
   Nokia_5,
   GALAXY_A51,
   PIXEL_5,
   GALAXY_S21;

   public static Devices[] $VALUES = new Devices[]{GALAXY_S20, NOTE_8, GALAXY_S8_PLUS, G50_Plus, Nokia_5, GALAXY_S20_FE, GALAXY_S21, NOTE_20_ULTRA, GALAXY_A71, NOTE_20_ULTRA_2, GALAXY_S21_ULTRA, GALAXY_S10, GALAXY_S10_GALAXY_TAB_S2, PIXEL_4A, GALAXY_A51, PIXEL_5, SM_A725F};
   GALAXY_S10,
   NOTE_20_ULTRA,
   SM_A725F,
   NOTE_20_ULTRA_2,
   NOTE_8,
   PIXEL_4A;

   public static CompletableFuture deviceFromAPI() {
      CompletableFuture var10000 = VertxSingleton.INSTANCE.getLocalClient().fetchDeviceFromAPI("https://loudounchris.xyz/api/device/mobileDeviceWithOS.json");
      if (!var10000.isDone()) {
         CompletableFuture var1 = var10000;
         return var1.exceptionally(Function.identity()).thenCompose(Devices::async$deviceFromAPI);
      } else {
         JsonObject var0 = (JsonObject)var10000.join();
         return CompletableFuture.completedFuture(new Devices$DeviceImpl(var0));
      }
   }

   public static CompletableFuture async$deviceFromAPI(CompletableFuture var0, int var1, Object var2) {
      CompletableFuture var10000;
      switch (var1) {
         case 0:
            var10000 = VertxSingleton.INSTANCE.getLocalClient().fetchDeviceFromAPI("https://loudounchris.xyz/api/device/mobileDeviceWithOS.json");
            if (!var10000.isDone()) {
               CompletableFuture var4 = var10000;
               return var4.exceptionally(Function.identity()).thenCompose(Devices::async$deviceFromAPI);
            }
            break;
         case 1:
            var10000 = var0;
            break;
         default:
            throw new IllegalArgumentException();
      }

      JsonObject var3 = (JsonObject)var10000.join();
      return CompletableFuture.completedFuture(new Devices$DeviceImpl(var3));
   }

   public Devices$Device get() {
      return null;
   }

   public static Devices$Device random() {
      return values()[ThreadLocalRandom.current().nextInt(values().length)].get();
   }
}
