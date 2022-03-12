/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
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
import io.trickle.webclient.CookieJar;
import io.trickle.webclient.RealClient;
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
    public static Logger logger = LogManager.getLogger(TokenController.class);
    public AtomicInteger solveCount;
    public AtomicReference<ArrayBlockingQueue<SolveFuture>> waitingList = new AtomicReference();

    public TokenController() {
        this.waitingList.set(new ArrayBlockingQueue(1500));
        this.solveCount = new AtomicInteger(0);
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$solveCaptcha(String var0, int var1_1, Iterable var2_2, String var3_3, CookieJar var4_4, RealClient var5_5, CaptchaToken var6_6, SolveFuture var7_8, SolveFuture var8_9, int var9_10, Object var10_11) {
        switch (var9_10) {
            case 0: {
                try {
                    TokenController.logger.info("Captcha needs solving");
                    var6_6 = new CaptchaToken(var0, (boolean)var1_1, var2_2, var3_3, var4_4, null, var5_5);
                    var7_8 = ((TokenController)Engine.get().getModule(Controller.TOKEN)).solve(var6_6);
                    v0 = var7_8;
                    if (!v0.toCompletableFuture().isDone()) {
                        var8_9 = v0;
                        return var8_9.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solveCaptcha(java.lang.String int java.lang.Iterable java.lang.String io.trickle.webclient.CookieJar io.trickle.webclient.RealClient io.trickle.harvester.CaptchaToken io.trickle.harvester.SolveFuture io.trickle.harvester.SolveFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((String)var0, (int)var1_1, (Iterable)var2_2, (String)var3_3, (CookieJar)var4_4, (RealClient)var5_5, (CaptchaToken)var6_6, (SolveFuture)var7_8, (SolveFuture)var8_9, (int)1)).toCompletableFuture();
                    }
                    ** GOTO lbl17
                }
                catch (Throwable var6_7) {
                    TokenController.logger.error("HARVEST ERR: {}", (Object)var6_7.getMessage());
                    return CompletableFuture.completedFuture(null);
                }
            }
            case 1: {
                v0 = var8_9;
lbl17:
                // 2 sources

                v0.toCompletableFuture().join();
                return CompletableFuture.completedFuture(var6_6.getToken());
            }
        }
        throw new IllegalArgumentException();
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

    @Override
    public void initialise() {
        logger.debug("Initialised.");
    }

    public static CompletableFuture solveCaptcha(String string, boolean bl, Iterable iterable, String string2, CookieJar cookieJar, RealClient realClient) {
        try {
            SolveFuture solveFuture;
            logger.info("Captcha needs solving");
            CaptchaToken captchaToken = new CaptchaToken(string, bl, iterable, string2, cookieJar, null, realClient);
            SolveFuture solveFuture2 = solveFuture = ((TokenController)Engine.get().getModule(Controller.TOKEN)).solve(captchaToken);
            if (!solveFuture2.toCompletableFuture().isDone()) {
                SolveFuture solveFuture3 = solveFuture2;
                return solveFuture3.exceptionally(Function.identity()).thenCompose(arg_0 -> TokenController.async$solveCaptcha(string, (int)(bl ? 1 : 0), iterable, string2, cookieJar, realClient, captchaToken, solveFuture, solveFuture3, 1, arg_0)).toCompletableFuture();
            }
            solveFuture2.toCompletableFuture().join();
            return CompletableFuture.completedFuture(captchaToken.getToken());
        }
        catch (Throwable throwable) {
            logger.error("HARVEST ERR: {}", (Object)throwable.getMessage());
            return CompletableFuture.completedFuture(null);
        }
    }

    @Override
    public Future load() {
        return Future.succeededFuture();
    }

    @Override
    public void terminate() {
    }
}

