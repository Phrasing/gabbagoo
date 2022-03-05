/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.teamdev.jxbrowser.browser.Browser
 *  com.teamdev.jxbrowser.callback.Callback
 *  com.teamdev.jxbrowser.cookie.Cookie
 *  com.teamdev.jxbrowser.cookie.CookieStore
 *  com.teamdev.jxbrowser.engine.Engine
 *  com.teamdev.jxbrowser.engine.EngineOptions
 *  com.teamdev.jxbrowser.engine.EngineOptions$Builder
 *  com.teamdev.jxbrowser.engine.RenderingMode
 *  com.teamdev.jxbrowser.frame.Frame
 *  com.teamdev.jxbrowser.net.HttpStatus
 *  com.teamdev.jxbrowser.net.Scheme
 *  com.teamdev.jxbrowser.net.UrlRequestJob
 *  com.teamdev.jxbrowser.net.UrlRequestJob$Options
 *  com.teamdev.jxbrowser.net.callback.AuthenticateCallback
 *  com.teamdev.jxbrowser.net.callback.AuthenticateCallback$Action
 *  com.teamdev.jxbrowser.net.callback.AuthenticateCallback$Params
 *  com.teamdev.jxbrowser.net.callback.BeforeUrlRequestCallback
 *  com.teamdev.jxbrowser.net.callback.BeforeUrlRequestCallback$Params
 *  com.teamdev.jxbrowser.net.callback.BeforeUrlRequestCallback$Response
 *  com.teamdev.jxbrowser.net.callback.InterceptUrlRequestCallback$Params
 *  com.teamdev.jxbrowser.net.callback.InterceptUrlRequestCallback$Response
 *  com.teamdev.jxbrowser.net.event.RequestCompleted
 *  com.teamdev.jxbrowser.net.proxy.CustomProxyConfig
 *  io.netty.handler.codec.http.cookie.Cookie
 *  io.vertx.core.Vertx
 *  io.vertx.core.impl.ConcurrentHashSet
 *  io.vertx.core.shareddata.Lock
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package io.trickle.harvester;

import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.callback.Callback;
import com.teamdev.jxbrowser.cookie.Cookie;
import com.teamdev.jxbrowser.cookie.CookieStore;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.engine.RenderingMode;
import com.teamdev.jxbrowser.frame.Frame;
import com.teamdev.jxbrowser.net.HttpStatus;
import com.teamdev.jxbrowser.net.Scheme;
import com.teamdev.jxbrowser.net.UrlRequestJob;
import com.teamdev.jxbrowser.net.callback.AuthenticateCallback;
import com.teamdev.jxbrowser.net.callback.BeforeUrlRequestCallback;
import com.teamdev.jxbrowser.net.callback.InterceptUrlRequestCallback;
import com.teamdev.jxbrowser.net.event.RequestCompleted;
import com.teamdev.jxbrowser.net.proxy.CustomProxyConfig;
import io.trickle.core.Controller;
import io.trickle.core.VertxSingleton;
import io.trickle.harvester.LoginController;
import io.trickle.harvester.LoginFuture;
import io.trickle.harvester.LoginToken;
import io.trickle.harvester.WindowedBrowser;
import io.trickle.util.Storage;
import io.trickle.webclient.CookieJar;
import io.vertx.core.Vertx;
import io.vertx.core.impl.ConcurrentHashSet;
import io.vertx.core.shareddata.Lock;
import java.lang.invoke.LambdaMetafactory;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginHarvester {
    public AtomicBoolean started;
    public ConcurrentHashSet<String> urlsIncompleted;
    public AtomicReference<CountDownLatch> latch = new AtomicReference();
    public AtomicInteger referenceCount = new AtomicInteger(0);
    public static Logger logger = LogManager.getLogger(LoginHarvester.class);
    public Engine engine;
    public static LoginHarvester[] LOGIN_HARVESTERS;
    public LoginFuture loginFuture;
    public ExecutorService executor;
    public static AtomicInteger harvesterCount;
    public static CountDownLatch isInstantiated;
    public WindowedBrowser browser;
    public static String[] LOGIN_EPS;

    public void lambda$setInterceptor$4(String string, Long l) {
        this.urlsIncompleted.remove((Object)string);
        if (!this.urlsIncompleted.isEmpty()) return;
        this.latch.get().countDown();
    }

    public InterceptUrlRequestCallback.Response lambda$baseOpts$0(InterceptUrlRequestCallback.Params params) {
        String string = params.urlRequest().url();
        boolean bl = string.contains(".ico");
        if (bl) return InterceptUrlRequestCallback.Response.proceed();
        if (!this.isLoginPage(string)) return InterceptUrlRequestCallback.Response.proceed();
        if (this.loginFuture.getEmptyLoginToken().getHtml() == null) return InterceptUrlRequestCallback.Response.proceed();
        UrlRequestJob urlRequestJob = params.newUrlRequestJob(UrlRequestJob.Options.newBuilder((HttpStatus)HttpStatus.OK).build());
        urlRequestJob.write(this.loginFuture.getEmptyLoginToken().getHtml().getBytes(StandardCharsets.UTF_8));
        urlRequestJob.complete();
        return InterceptUrlRequestCallback.Response.intercept((UrlRequestJob)urlRequestJob);
    }

    public void close() {
        this.referenceCount.decrementAndGet();
        if (this.referenceCount.get() != 0) return;
        if (this.engine != null && !this.engine.isClosed()) {
            if (this.browser != null) {
                this.browser.close();
            }
            for (Browser browser : this.engine.browsers()) {
                try {
                    if (browser.isClosed()) continue;
                    browser.close();
                }
                catch (Throwable throwable) {}
            }
            this.engine.close();
        }
        this.executor.shutdownNow();
    }

    static {
        LOGIN_EPS = new String[]{"https://www.amazon.com/ap/signin?openid.pape.max_auth_age=0&openid.return_to=https%3A%2F%2Fwww.amazon.com%2F%3Fref_%3Dnav_signin&openid.identity=http%3A%2F%2Fspecs.openid.net%2Fauth%2F2.0%2Fidentifier_select&openid.assoc_handle=usflex&openid.mode=checkid_setup&openid.claimed_id=http%3A%2F%2Fspecs.openid.net%2Fauth%2F2.0%2Fidentifier_select&openid.ns=http%3A%2F%2Fspecs.openid.net%2Fauth%2F2.0&", "https://www.bestbuy.com/identity/signin?token="};
        harvesterCount = new AtomicInteger(0);
        isInstantiated = new CountDownLatch(1);
        LOGIN_HARVESTERS = new LoginHarvester[Storage.HARVESTER_COUNT_YS];
        int n = 0;
        while (n < LOGIN_HARVESTERS.length) {
            LoginHarvester.LOGIN_HARVESTERS[n] = new LoginHarvester();
            ++n;
        }
    }

    public boolean checkIfBrowserHasCookie(String string) {
        Cookie cookie;
        CookieStore cookieStore = this.engine.cookieStore();
        Iterator iterator = cookieStore.cookies().iterator();
        do {
            if (!iterator.hasNext()) return false;
        } while ((cookie = (Cookie)iterator.next()).name().isEmpty() || !cookie.name().equals(string));
        return true;
    }

    public void setProxy(String[] stringArray) {
        this.engine.network().set(AuthenticateCallback.class, (Callback)((AuthenticateCallback)(arg_0, arg_1) -> LoginHarvester.lambda$setProxy$6(stringArray, arg_0, arg_1)));
        this.engine.proxy().config(CustomProxyConfig.newInstance((String)String.format("http=%s:%s;https=%s:%s", stringArray[0], stringArray[1], stringArray[0], stringArray[1])));
    }

    public void lambda$startSolver$2() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                this.urlsIncompleted = new ConcurrentHashSet();
                this.latch.set(new CountDownLatch(1));
                this.loginFuture = ((LoginController)io.trickle.core.Engine.get().getModule(Controller.LOGIN)).pollWaitingList();
                logger.debug("[startSolver()] Got token {}", (Object)this.loginFuture);
                this.configureLoginDetails();
                this.browser.browser().navigation().loadUrlAndWait("https://www.bestbuy.com/favicon.ico");
                LoginToken loginToken = this.loginFuture.getEmptyLoginToken();
                this.browser.browser().navigation().loadUrl(loginToken.getDomain());
                this.browser.enlarge();
                this.latch.get().await();
                this.exportCookiesFromHarvester();
                this.browser.browser().mainFrame().ifPresent(LoginHarvester::lambda$startSolver$1);
                this.loginFuture.complete(this.loginFuture.getEmptyLoginToken());
            }
            catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }
    }

    public void init() {
        int n = harvesterCount.incrementAndGet();
        while (true) {
            if (this.engine != null) {
                this.browser = new WindowedBrowser(this.engine);
                this.browser.createWindow();
                this.setInterceptor();
                return;
            }
            try {
                this.engine = Engine.newInstance((EngineOptions)this.baseOpts().build());
                isInstantiated.countDown();
            }
            catch (Throwable throwable) {
                logger.warn("Updating harvester. Please wait: {}", (Object)throwable.getMessage());
                try {
                    if (this.engine != null) {
                        this.engine.close();
                    }
                    if (n == 1) continue;
                    isInstantiated.await();
                    continue;
                }
                catch (Throwable throwable2) {
                    logger.warn("Error loading cached data backup: {}", (Object)throwable.getMessage());
                    try {
                        Thread.sleep(5000L);
                        continue;
                    }
                    catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                        continue;
                    }
                }
            }
            break;
        }
    }

    public static void lambda$setProxy$6(String[] stringArray, AuthenticateCallback.Params params, AuthenticateCallback.Action action) {
        if (!params.isProxy()) {
            action.cancel();
            return;
        }
        logger.info("Enabling proxy");
        if (stringArray.length == 4) {
            action.authenticate(stringArray[2], stringArray[3]);
            return;
        }
        action.cancel();
    }

    public EngineOptions.Builder baseOpts() {
        return EngineOptions.newBuilder((RenderingMode)RenderingMode.HARDWARE_ACCELERATED).licenseKey("1BNDIEOFAZ0H665CSFR41MCR5THTYZ8ZE7J946B9XRQ2B35XEE8PDHHOC27XGDJQURKYEQ").addScheme(Scheme.HTTPS, this::lambda$baseOpts$0);
    }

    public void setInterceptor() {
        this.engine.network().set(BeforeUrlRequestCallback.class, (Callback)((BeforeUrlRequestCallback)this::lambda$setInterceptor$3));
        this.engine.network().on(RequestCompleted.class, this::lambda$setInterceptor$5);
    }

    public LoginHarvester() {
        this.started = new AtomicBoolean(false);
        this.executor = Executors.newSingleThreadExecutor();
    }

    public static void lambda$startSolver$1(Frame frame) {
        frame.executeJavaScript("document.querySelector(\"html\").innerHTML = `<h2>Waiting for token</h2>`");
    }

    public void configureLoginDetails() {
        LoginToken loginToken = this.loginFuture.getEmptyLoginToken();
        CookieStore cookieStore = this.engine.cookieStore();
        cookieStore.deleteAll();
        cookieStore.persist();
        Iterator iterator = loginToken.getCookies().iterator();
        while (true) {
            if (!iterator.hasNext()) {
                cookieStore.persist();
                this.setProxy(loginToken.getProxyStr().split(":"));
                return;
            }
            io.netty.handler.codec.http.cookie.Cookie cookie = (io.netty.handler.codec.http.cookie.Cookie)iterator.next();
            cookieStore.set(Cookie.newBuilder((String)cookie.domain()).name(cookie.name()).value(cookie.value()).path(cookie.path()).httpOnly(cookie.isHttpOnly()).secure(cookie.isSecure()).build());
        }
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$start(LoginHarvester var0, CompletableFuture var1_1, int var2_3, Object var3_5) {
        switch (var2_3) {
            case 0: {
                var0.referenceCount.incrementAndGet();
                try {
                    v0 = Vertx.currentContext().owner().sharedData().getLocalLockWithTimeout(String.valueOf(var0.executor.hashCode()), TimeUnit.HOURS.toMillis(1L)).toCompletionStage().toCompletableFuture();
                    if (!v0.isDone()) {
                        var4_7 = v0;
                        return var4_7.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$start(io.trickle.harvester.LoginHarvester java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((LoginHarvester)var0, var4_7, (int)1));
                    }
lbl10:
                    // 3 sources

                    while (true) {
                        var1_1 = (Lock)v0.join();
                        try {
                            if (var0.started.get() != false) return CompletableFuture.completedFuture(true);
                            var0.executor.submit((Runnable)LambdaMetafactory.metafactory(null, null, null, ()V, init(), ()V)((LoginHarvester)var0));
                            var0.executor.submit((Runnable)LambdaMetafactory.metafactory(null, null, null, ()V, startSolver(), ()V)((LoginHarvester)var0));
                            var0.started.set(true);
                            LoginHarvester.logger.debug("Started!");
                            return CompletableFuture.completedFuture(true);
                        }
                        catch (Throwable var2_4) {
                            LoginHarvester.logger.error("Start error on harvester {}", (Object)var2_4.getMessage());
                            return CompletableFuture.completedFuture(true);
                        }
                        finally {
                            var1_1.release();
                        }
                        break;
                    }
                }
                catch (Throwable var1_2) {
                    LoginHarvester.logger.error("Lock error on harvester {}", (Object)var1_2.getMessage());
                }
                return CompletableFuture.completedFuture(true);
            }
            case 1: {
                v0 = var1_1;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public void lambda$setInterceptor$5(RequestCompleted requestCompleted) {
        String string = requestCompleted.urlRequest().url();
        if (requestCompleted.urlRequest().url().contains("https://www.amazon.com/?ref_=nav_signin&")) {
            logger.info("Login complete!");
            this.latch.get().countDown();
            return;
        }
        if (!string.contains("tmx.bestbuy.com")) return;
        VertxSingleton.INSTANCE.get().setTimer(5000L, arg_0 -> this.lambda$setInterceptor$4(string, arg_0));
    }

    public void exportCookiesFromHarvester() {
        CookieJar cookieJar = this.loginFuture.getEmptyLoginToken().getCookieJar();
        CookieStore cookieStore = this.engine.cookieStore();
        Iterator iterator = cookieStore.cookies().iterator();
        while (iterator.hasNext()) {
            Cookie cookie = (Cookie)iterator.next();
            if (cookie.name().isEmpty() || cookie.name().equals("vt")) continue;
            cookieJar.put(cookie.name(), cookie.value(), cookie.domain());
        }
    }

    public CompletableFuture start() {
        this.referenceCount.incrementAndGet();
        try {
            CompletableFuture completableFuture = Vertx.currentContext().owner().sharedData().getLocalLockWithTimeout(String.valueOf(this.executor.hashCode()), TimeUnit.HOURS.toMillis(1L)).toCompletionStage().toCompletableFuture();
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> LoginHarvester.async$start(this, completableFuture2, 1, arg_0));
            }
            Lock lock = (Lock)completableFuture.join();
            try {
                if (this.started.get()) return CompletableFuture.completedFuture(true);
                this.executor.submit(this::init);
                this.executor.submit(this::startSolver);
                this.started.set(true);
                logger.debug("Started!");
                return CompletableFuture.completedFuture(true);
            }
            catch (Throwable throwable) {
                logger.error("Start error on harvester {}", (Object)throwable.getMessage());
                return CompletableFuture.completedFuture(true);
            }
            finally {
                lock.release();
            }
        }
        catch (Throwable throwable) {
            logger.error("Lock error on harvester {}", (Object)throwable.getMessage());
        }
        return CompletableFuture.completedFuture(true);
    }

    public BeforeUrlRequestCallback.Response lambda$setInterceptor$3(BeforeUrlRequestCallback.Params params) {
        String string = params.urlRequest().url();
        if (string.contains("addchv=true")) {
            this.latch.get().countDown();
            return BeforeUrlRequestCallback.Response.cancel();
        }
        if (!string.contains("tmx.bestbuy.com")) return BeforeUrlRequestCallback.Response.proceed();
        this.urlsIncompleted.add((Object)string);
        return BeforeUrlRequestCallback.Response.proceed();
    }

    public void startSolver() {
        this.executor.submit(this::lambda$startSolver$2);
    }

    public boolean isLoginPage(String string) {
        String[] stringArray = LOGIN_EPS;
        int n = stringArray.length;
        int n2 = 0;
        while (n2 < n) {
            String string2 = stringArray[n2];
            if (string.startsWith(string2)) {
                return true;
            }
            ++n2;
        }
        return false;
    }
}

