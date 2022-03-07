/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.util.concurrent;

import io.trickle.core.VertxSingleton;
import io.trickle.util.concurrent.VertxUtil$SignalEntry;
import java.lang.invoke.LambdaMetafactory;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class VertxUtil {
    public static Set<VertxUtil$SignalEntry> SIGNAL_ENTRIES = ConcurrentHashMap.newKeySet();

    public static void sendSignal(String string) {
        try {
            Iterator<VertxUtil$SignalEntry> iterator = SIGNAL_ENTRIES.iterator();
            while (iterator.hasNext()) {
                VertxUtil$SignalEntry vertxUtil$SignalEntry = iterator.next();
                if (vertxUtil$SignalEntry.call.isDone() || !vertxUtil$SignalEntry.signal.equals(string)) continue;
                vertxUtil$SignalEntry.complete();
                iterator.remove();
            }
            return;
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public static CompletableFuture signalSleep(String string, long l) {
        if (l <= 1L) return CompletableFuture.completedFuture(null);
        VertxUtil$SignalEntry vertxUtil$SignalEntry = VertxUtil$SignalEntry.fromSignal(string);
        try {
            vertxUtil$SignalEntry.timerId = vertxUtil$SignalEntry.call.getCtx().owner().setTimer(l, arg_0 -> VertxUtil.lambda$signalSleep$1(vertxUtil$SignalEntry, arg_0));
            SIGNAL_ENTRIES.add(vertxUtil$SignalEntry);
            return vertxUtil$SignalEntry.call;
        }
        catch (Throwable throwable) {
            return CompletableFuture.completedFuture(null);
        }
    }

    public static CompletableFuture randomSignalSleep(String string, long l) {
        if (l <= 1L) return CompletableFuture.completedFuture(null);
        long l2 = ThreadLocalRandom.current().nextLong(l, Math.round((double)l * Double.longBitsToDouble(4609434218613702656L)));
        return VertxUtil.signalSleep(string, l2);
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$handleEagerFuture(CompletableFuture var0, CompletableFuture var1_1, int var2_2, Object var3_3) {
        switch (var2_2) {
            case 0: {
                v0 = var0;
                if (!v0.isDone()) {
                    var1_1 = v0;
                    return var1_1.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$handleEagerFuture(java.util.concurrent.CompletableFuture java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((CompletableFuture)var0, (CompletableFuture)var1_1, (int)1));
                }
                ** GOTO lbl10
            }
            case 1: {
                v0 = var1_1;
lbl10:
                // 2 sources

                v0.join();
                v1 = VertxUtil.hardCodedSleep(10L);
                if (!v1.isDone()) {
                    var1_1 = v1;
                    return var1_1.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$handleEagerFuture(java.util.concurrent.CompletableFuture java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((CompletableFuture)var0, (CompletableFuture)var1_1, (int)2));
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

    public static void lambda$signalSleep$1(VertxUtil$SignalEntry vertxUtil$SignalEntry, Long l) {
        vertxUtil$SignalEntry.complete();
        SIGNAL_ENTRIES.remove(vertxUtil$SignalEntry);
    }

    public static void sendSignal() {
        try {
            Iterator<VertxUtil$SignalEntry> iterator = SIGNAL_ENTRIES.iterator();
            while (iterator.hasNext()) {
                VertxUtil$SignalEntry vertxUtil$SignalEntry = iterator.next();
                if (vertxUtil$SignalEntry.call.isDone()) continue;
                vertxUtil$SignalEntry.complete();
                iterator.remove();
            }
            return;
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public static void sendSignal(String string, String string2) {
        try {
            Iterator<VertxUtil$SignalEntry> iterator = SIGNAL_ENTRIES.iterator();
            while (iterator.hasNext()) {
                VertxUtil$SignalEntry vertxUtil$SignalEntry = iterator.next();
                if (vertxUtil$SignalEntry.call.isDone() || !vertxUtil$SignalEntry.signal.equals(string)) continue;
                vertxUtil$SignalEntry.complete(string2);
                iterator.remove();
            }
            return;
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public static void lambda$hardCodedSleep$0(CompletableFuture completableFuture, Long l) {
        completableFuture.complete(null);
    }

    public static CompletableFuture sleep(long l) {
        return VertxUtil.signalSleep("massChange", l);
    }

    public static CompletableFuture randomSleep(long l) {
        return VertxUtil.randomSignalSleep("massChange", l);
    }

    public static CompletableFuture handleEagerFuture(CompletableFuture completableFuture) {
        CompletableFuture completableFuture2 = completableFuture;
        if (!completableFuture2.isDone()) {
            CompletableFuture completableFuture3 = completableFuture2;
            return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> VertxUtil.async$handleEagerFuture(completableFuture, completableFuture3, 1, arg_0));
        }
        completableFuture2.join();
        CompletableFuture completableFuture4 = VertxUtil.hardCodedSleep(10L);
        if (!completableFuture4.isDone()) {
            CompletableFuture completableFuture5 = completableFuture4;
            return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> VertxUtil.async$handleEagerFuture(completableFuture, completableFuture5, 2, arg_0));
        }
        completableFuture4.join();
        return CompletableFuture.completedFuture(null);
    }

    public static CompletableFuture hardCodedSleep(long l) {
        if (l <= 1L) return CompletableFuture.completedFuture(null);
        CompletableFuture completableFuture = new CompletableFuture();
        VertxSingleton.INSTANCE.get().setTimer(l, arg_0 -> VertxUtil.lambda$hardCodedSleep$0(completableFuture, arg_0));
        return completableFuture;
    }
}

