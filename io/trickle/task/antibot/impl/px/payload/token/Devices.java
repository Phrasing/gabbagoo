/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.core.VertxSingleton
 *  io.trickle.task.antibot.impl.px.payload.token.Devices$1
 *  io.trickle.task.antibot.impl.px.payload.token.Devices$10
 *  io.trickle.task.antibot.impl.px.payload.token.Devices$11
 *  io.trickle.task.antibot.impl.px.payload.token.Devices$12
 *  io.trickle.task.antibot.impl.px.payload.token.Devices$13
 *  io.trickle.task.antibot.impl.px.payload.token.Devices$14
 *  io.trickle.task.antibot.impl.px.payload.token.Devices$15
 *  io.trickle.task.antibot.impl.px.payload.token.Devices$16
 *  io.trickle.task.antibot.impl.px.payload.token.Devices$17
 *  io.trickle.task.antibot.impl.px.payload.token.Devices$2
 *  io.trickle.task.antibot.impl.px.payload.token.Devices$3
 *  io.trickle.task.antibot.impl.px.payload.token.Devices$4
 *  io.trickle.task.antibot.impl.px.payload.token.Devices$5
 *  io.trickle.task.antibot.impl.px.payload.token.Devices$6
 *  io.trickle.task.antibot.impl.px.payload.token.Devices$7
 *  io.trickle.task.antibot.impl.px.payload.token.Devices$8
 *  io.trickle.task.antibot.impl.px.payload.token.Devices$9
 *  io.trickle.task.antibot.impl.px.payload.token.Devices$Device
 *  io.trickle.task.antibot.impl.px.payload.token.Devices$DeviceImpl
 *  io.vertx.core.json.JsonObject
 */
package io.trickle.task.antibot.impl.px.payload.token;

import io.trickle.core.VertxSingleton;
import io.trickle.task.antibot.impl.px.payload.token.Devices;
import io.vertx.core.json.JsonObject;
import java.lang.invoke.LambdaMetafactory;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class Devices
extends Enum {
    public static /* enum */ Devices GALAXY_S20_FE;
    public static /* enum */ Devices G50_Plus;
    public static /* enum */ Devices GALAXY_S8_PLUS;
    public static /* enum */ Devices GALAXY_S21_ULTRA;
    public static /* enum */ Devices GALAXY_S20;
    public static /* enum */ Devices GALAXY_A71;
    public static /* enum */ Devices GALAXY_S10_GALAXY_TAB_S2;
    public static /* enum */ Devices Nokia_5;
    public static /* enum */ Devices GALAXY_A51;
    public static /* enum */ Devices PIXEL_5;
    public static /* enum */ Devices GALAXY_S21;
    public static Devices[] $VALUES;
    public static /* enum */ Devices GALAXY_S10;
    public static /* enum */ Devices NOTE_20_ULTRA;
    public static /* enum */ Devices SM_A725F;
    public static /* enum */ Devices NOTE_20_ULTRA_2;
    public static /* enum */ Devices NOTE_8;
    public static /* enum */ Devices PIXEL_4A;

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    public Devices() {
        void var2_-1;
        void var1_-1;
    }

    static {
        GALAXY_S20 = new 1("GALAXY_S20", 0);
        NOTE_8 = new 2("NOTE_8", 1);
        GALAXY_S8_PLUS = new 3("GALAXY_S8_PLUS", 2);
        G50_Plus = new 4("G50_Plus", 3);
        Nokia_5 = new 5("Nokia_5", 4);
        GALAXY_S20_FE = new 6("GALAXY_S20_FE", 5);
        GALAXY_S21 = new 7("GALAXY_S21", 6);
        NOTE_20_ULTRA = new 8("NOTE_20_ULTRA", 7);
        GALAXY_A71 = new 9("GALAXY_A71", 8);
        NOTE_20_ULTRA_2 = new 10("NOTE_20_ULTRA_2", 9);
        GALAXY_S21_ULTRA = new 11("GALAXY_S21_ULTRA", 10);
        GALAXY_S10 = new 12("GALAXY_S10", 11);
        GALAXY_S10_GALAXY_TAB_S2 = new 13("GALAXY_S10_GALAXY_TAB_S2", 12);
        PIXEL_4A = new 14("PIXEL_4A", 13);
        GALAXY_A51 = new 15("GALAXY_A51", 14);
        PIXEL_5 = new 16("PIXEL_5", 15);
        SM_A725F = new 17("SM_A725F", 16);
        $VALUES = new Devices[]{GALAXY_S20, NOTE_8, GALAXY_S8_PLUS, G50_Plus, Nokia_5, GALAXY_S20_FE, GALAXY_S21, NOTE_20_ULTRA, GALAXY_A71, NOTE_20_ULTRA_2, GALAXY_S21_ULTRA, GALAXY_S10, GALAXY_S10_GALAXY_TAB_S2, PIXEL_4A, GALAXY_A51, PIXEL_5, SM_A725F};
    }

    public static CompletableFuture deviceFromAPI() {
        CompletableFuture completableFuture = VertxSingleton.INSTANCE.getLocalClient().fetchDeviceFromAPI("https://loudounchris.xyz/api/device/mobileDeviceWithOS.json");
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Devices.async$deviceFromAPI(completableFuture2, 1, arg_0));
        }
        JsonObject jsonObject = (JsonObject)completableFuture.join();
        return CompletableFuture.completedFuture(new DeviceImpl(jsonObject));
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$deviceFromAPI(CompletableFuture var0, int var1_1, Object var2_3) {
        switch (var1_1) {
            case 0: {
                v0 = VertxSingleton.INSTANCE.getLocalClient().fetchDeviceFromAPI("https://loudounchris.xyz/api/device/mobileDeviceWithOS.json");
                if (!v0.isDone()) {
                    var1_2 = v0;
                    return var1_2.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$deviceFromAPI(java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((CompletableFuture)var1_2, (int)1));
                }
                ** GOTO lbl10
            }
            case 1: {
                v0 = var0;
lbl10:
                // 2 sources

                var0 = (JsonObject)v0.join();
                return CompletableFuture.completedFuture(new DeviceImpl((JsonObject)var0));
            }
        }
        throw new IllegalArgumentException();
    }

    public Device get() {
        return null;
    }

    public static Devices valueOf(String string) {
        return Enum.valueOf(Devices.class, string);
    }

    public static Devices[] values() {
        return (Devices[])$VALUES.clone();
    }

    public static Device random() {
        return Devices.values()[ThreadLocalRandom.current().nextInt(Devices.values().length)].get();
    }
}
