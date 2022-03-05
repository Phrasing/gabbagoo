/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.Future
 *  io.vertx.core.Vertx
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package io.trickle.harvester;

import io.trickle.core.Controller;
import io.trickle.core.Engine;
import io.trickle.core.api.LoadableAsync;
import io.trickle.core.api.Module;
import io.trickle.harvester.LoginFuture;
import io.trickle.harvester.LoginToken;
import io.trickle.harvester.TokenController;
import io.trickle.webclient.CookieJar;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import java.lang.invoke.LambdaMetafactory;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginController
implements Module,
LoadableAsync {
    public AtomicReference<ArrayBlockingQueue<LoginFuture>> waitingList = new AtomicReference();
    public static Logger logger = LogManager.getLogger(TokenController.class);
    public Vertx vertx;
    public String LOCK_IDENTITY;
    public AtomicInteger solveCount;

    @Override
    public void initialise() {
        logger.debug("Initialised.");
    }

    public LoginFuture solve(LoginToken loginToken) {
        LoginFuture loginFuture = new LoginFuture(loginToken);
        if (this.waitingList.get().offer(loginFuture)) return loginFuture;
        throw new Exception("Too many tokens to solve!!!");
    }

    public static CompletableFuture initBrowserLogin(String string, Iterable iterable, String string2, CookieJar cookieJar, String string3) {
        try {
            LoginFuture loginFuture;
            logger.info("Browser needs attention.");
            LoginToken loginToken = new LoginToken(string, iterable, string2, cookieJar, string3);
            LoginFuture loginFuture2 = loginFuture = ((LoginController)Engine.get().getModule(Controller.LOGIN)).solve(loginToken);
            if (!loginFuture2.toCompletableFuture().isDone()) {
                LoginFuture loginFuture3 = loginFuture2;
                return loginFuture3.exceptionally(Function.identity()).thenCompose(arg_0 -> LoginController.async$initBrowserLogin(string, iterable, string2, cookieJar, string3, loginToken, loginFuture, loginFuture3, 1, arg_0)).toCompletableFuture();
            }
            loginFuture2.toCompletableFuture().join();
            return CompletableFuture.completedFuture(loginToken.getCookieJar());
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
            logger.error("HARVEST ERR: {}", (Object)throwable.getMessage());
            return CompletableFuture.completedFuture(null);
        }
    }

    @Override
    public void terminate() {
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$initBrowserLogin(String var0, Iterable var1_1, String var2_2, CookieJar var3_3, String var4_4, LoginToken var5_5, LoginFuture var6_7, LoginFuture var7_8, int var8_9, Object var9_10) {
        switch (var8_9) {
            case 0: {
                try {
                    LoginController.logger.info("Browser needs attention.");
                    var5_5 = new LoginToken(var0, var1_1, var2_2, var3_3, var4_4);
                    var6_7 = ((LoginController)Engine.get().getModule(Controller.LOGIN)).solve(var5_5);
                    v0 = var6_7;
                    if (!v0.toCompletableFuture().isDone()) {
                        var7_8 = v0;
                        return var7_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$initBrowserLogin(java.lang.String java.lang.Iterable java.lang.String io.trickle.webclient.CookieJar java.lang.String io.trickle.harvester.LoginToken io.trickle.harvester.LoginFuture io.trickle.harvester.LoginFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((String)var0, (Iterable)var1_1, (String)var2_2, (CookieJar)var3_3, (String)var4_4, (LoginToken)var5_5, (LoginFuture)var6_7, (LoginFuture)var7_8, (int)1)).toCompletableFuture();
                    }
                    ** GOTO lbl18
                }
                catch (Throwable var5_6) {
                    var5_6.printStackTrace();
                    LoginController.logger.error("HARVEST ERR: {}", (Object)var5_6.getMessage());
                    return CompletableFuture.completedFuture(null);
                }
            }
            case 1: {
                v0 = var7_8;
lbl18:
                // 2 sources

                v0.toCompletableFuture().join();
                return CompletableFuture.completedFuture(var5_5.getCookieJar());
            }
        }
        throw new IllegalArgumentException();
    }

    public LoginFuture pollWaitingList() {
        this.solveCount.incrementAndGet();
        return this.waitingList.get().take();
    }

    @Override
    public Future load() {
        return Future.succeededFuture();
    }

    public LoginController(Vertx vertx) {
        this.waitingList.set(new ArrayBlockingQueue(300));
        this.vertx = vertx;
        this.LOCK_IDENTITY = UUID.randomUUID().toString();
        this.solveCount = new AtomicInteger(0);
    }
}

