/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.MultiMap
 *  io.vertx.core.buffer.Buffer
 */
package io.trickle.task.antibot.impl.px.payload.token;

import io.trickle.task.antibot.impl.px.payload.Payload;
import io.trickle.task.antibot.impl.px.payload.token.Devices;
import io.trickle.task.antibot.impl.px.payload.token.Devices$Device;
import io.trickle.task.antibot.impl.px.payload.token.InitPayload$1;
import io.trickle.task.sites.Site;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import java.lang.invoke.LambdaMetafactory;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class InitPayload
implements Payload {
    public Devices$Device device;

    public static CompletableFuture fetch() {
        try {
            CompletableFuture completableFuture = InitPayload.getDeviceFromAPI();
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> InitPayload.async$fetch(completableFuture2, 1, arg_0));
            }
            Devices$Device devices$Device = (Devices$Device)completableFuture.join();
            return CompletableFuture.completedFuture(new InitPayload(devices$Device));
        }
        catch (Throwable throwable) {
            return CompletableFuture.failedFuture(throwable);
        }
    }

    public Buffer asBuffer(Site site) {
        switch (InitPayload$1.$SwitchMap$io$trickle$task$sites$Site[site.ordinal()]) {
            case 1: {
                return Buffer.buffer((String)("{\"app_id\":\"PX9Qx3Rve4\",\"device_os_name\":\"Android\",\"device_os_version\":\"" + this.device.getApiLevel() + "\",\"sdk_version\":\"v1.13.2\",\"app_version\":\"4.15.0\"}"));
            }
            case 2: {
                return Buffer.buffer((String)("{\"app_id\":\"PXUArm9B04\",\"device_os_name\":\"Android\",\"device_os_version\":\"" + this.device.getApiLevel() + "\",\"sdk_version\":\"v1.8.0\",\"app_version\":\"21.12\"}"));
            }
        }
        throw new Exception("Invalid PX site initialized");
    }

    public static CompletableFuture getDeviceFromAPI() {
        return Devices.deviceFromAPI();
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$fetch(CompletableFuture var0, int var1_2, Object var2_4) {
        switch (var1_2) {
            case 0: {
                try {
                    v0 = InitPayload.getDeviceFromAPI();
                    if (!v0.isDone()) {
                        var1_3 = v0;
                        return var1_3.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$fetch(java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((CompletableFuture)var1_3, (int)1));
                    }
                    ** GOTO lbl13
                }
                catch (Throwable var0_1) {
                    return CompletableFuture.failedFuture(var0_1);
                }
            }
            case 1: {
                v0 = var0;
lbl13:
                // 2 sources

                var0 = (Devices$Device)v0.join();
                return CompletableFuture.completedFuture(new InitPayload((Devices$Device)var0));
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public MultiMap asForm() {
        throw new RuntimeException("Unsupported Operation");
    }

    public InitPayload() {
        this.device = Devices.random();
    }

    public InitPayload(Devices$Device devices$Device) {
        this.device = devices$Device;
    }
}

