/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.core.Controller
 *  io.trickle.core.Engine
 *  io.trickle.core.api.LoadableAsync
 *  io.trickle.core.api.Module
 *  io.trickle.harvester.CaptchaToken
 *  io.trickle.harvester.SolveFuture
 *  io.vertx.core.Future
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package io.trickle.harvester;

import io.trickle.core.Controller;
import io.trickle.core.Engine;
import io.trickle.core.api.LoadableAsync;
import io.trickle.core.api.Module;
import io.trickle.harvester.CaptchaToken;
import io.trickle.harvester.SolveFuture;
import io.vertx.core.Future;
import java.lang.invoke.LambdaMetafactory;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TokenController
implements Module,
LoadableAsync {
    public AtomicReference<ArrayBlockingQueue<SolveFuture>> waitingList = new AtomicReference();
    public AtomicInteger solveCount;
    public static Logger logger = LogManager.getLogger(TokenController.class);

    public TokenController() {
        this.waitingList.set(new ArrayBlockingQueue(1500));
        this.solveCount = new AtomicInteger(0);
    }

    public void initialise() {
        logger.debug("Initialised.");
    }

    public void terminate() {
    }

    public SolveFuture solve(CaptchaToken captchaToken) {
        SolveFuture solveFuture = new SolveFuture(captchaToken);
        if (this.waitingList.get().offer(solveFuture)) return solveFuture;
        throw new Exception("Too many tokens to solve!!!");
    }

    public SolveFuture pollWaitingList() {
        this.solveCount.incrementAndGet();
        return this.waitingList.get().take();
    }

    public static CompletableFuture solveBasicCaptcha(String string) {
        try {
            SolveFuture solveFuture;
            logger.info("Captcha needs solving");
            CaptchaToken captchaToken = new CaptchaToken(string);
            SolveFuture solveFuture2 = solveFuture = ((TokenController)Engine.get().getModule(Controller.TOKEN)).solve(captchaToken);
            if (!solveFuture2.toCompletableFuture().isDone()) {
                SolveFuture solveFuture3 = solveFuture2;
                return solveFuture3.exceptionally(Function.identity()).thenCompose(arg_0 -> TokenController.async$solveBasicCaptcha(string, captchaToken, solveFuture, solveFuture3, 1, arg_0)).toCompletableFuture();
            }
            solveFuture2.toCompletableFuture().join();
            return CompletableFuture.completedFuture(captchaToken);
        }
        catch (Throwable throwable) {
            logger.error("HARVEST ERR: {}", (Object)throwable.getMessage());
            return CompletableFuture.completedFuture(null);
        }
    }

    public Future load() {
        return Future.succeededFuture();
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$solveBasicCaptcha(String var0, CaptchaToken var1_1, SolveFuture var2_3, SolveFuture var3_4, int var4_5, Object var5_6) {
        switch (var4_5) {
            case 0: {
                try {
                    TokenController.logger.info("Captcha needs solving");
                    var1_1 = new CaptchaToken(var0);
                    var2_3 = ((TokenController)Engine.get().getModule(Controller.TOKEN)).solve(var1_1);
                    v0 = var2_3;
                    if (!v0.toCompletableFuture().isDone()) {
                        var3_4 = v0;
                        return var3_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solveBasicCaptcha(java.lang.String io.trickle.harvester.CaptchaToken io.trickle.harvester.SolveFuture io.trickle.harvester.SolveFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((String)var0, (CaptchaToken)var1_1, (SolveFuture)var2_3, (SolveFuture)var3_4, (int)1)).toCompletableFuture();
                    }
                    ** GOTO lbl17
                }
                catch (Throwable var1_2) {
                    TokenController.logger.error("HARVEST ERR: {}", (Object)var1_2.getMessage());
                    return CompletableFuture.completedFuture(null);
                }
            }
            case 1: {
                v0 = var3_4;
lbl17:
                // 2 sources

                v0.toCompletableFuture().join();
                return CompletableFuture.completedFuture(var1_1);
            }
        }
        throw new IllegalArgumentException();
    }
}
