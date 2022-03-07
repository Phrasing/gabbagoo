/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.teamdev.jxbrowser.browser.Browser
 *  com.teamdev.jxbrowser.browser.event.ConsoleMessageReceived
 *  com.teamdev.jxbrowser.callback.Callback
 *  com.teamdev.jxbrowser.engine.Engine
 *  com.teamdev.jxbrowser.engine.EngineOptions
 *  com.teamdev.jxbrowser.engine.EngineOptions$Builder
 *  com.teamdev.jxbrowser.engine.RenderingMode
 *  com.teamdev.jxbrowser.engine.event.EngineCrashed
 *  com.teamdev.jxbrowser.frame.Frame
 *  com.teamdev.jxbrowser.js.ConsoleMessage
 *  com.teamdev.jxbrowser.net.HttpStatus
 *  com.teamdev.jxbrowser.net.Scheme
 *  com.teamdev.jxbrowser.net.UrlRequestJob
 *  com.teamdev.jxbrowser.net.UrlRequestJob$Options
 *  com.teamdev.jxbrowser.net.callback.AuthenticateCallback
 *  com.teamdev.jxbrowser.net.callback.AuthenticateCallback$Action
 *  com.teamdev.jxbrowser.net.callback.AuthenticateCallback$Params
 *  com.teamdev.jxbrowser.net.callback.InterceptUrlRequestCallback$Params
 *  com.teamdev.jxbrowser.net.callback.InterceptUrlRequestCallback$Response
 *  com.teamdev.jxbrowser.net.proxy.CustomProxyConfig
 *  com.teamdev.jxbrowser.net.proxy.DirectProxyConfig
 *  io.vertx.core.AbstractVerticle
 *  io.vertx.core.AsyncResult
 *  io.vertx.core.Promise
 *  io.vertx.core.eventbus.Message
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package io.trickle.harvester.pooled;

import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.browser.event.ConsoleMessageReceived;
import com.teamdev.jxbrowser.callback.Callback;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.engine.RenderingMode;
import com.teamdev.jxbrowser.engine.event.EngineCrashed;
import com.teamdev.jxbrowser.frame.Frame;
import com.teamdev.jxbrowser.js.ConsoleMessage;
import com.teamdev.jxbrowser.net.HttpStatus;
import com.teamdev.jxbrowser.net.Scheme;
import com.teamdev.jxbrowser.net.UrlRequestJob;
import com.teamdev.jxbrowser.net.callback.AuthenticateCallback;
import com.teamdev.jxbrowser.net.callback.InterceptUrlRequestCallback;
import com.teamdev.jxbrowser.net.proxy.CustomProxyConfig;
import com.teamdev.jxbrowser.net.proxy.DirectProxyConfig;
import io.trickle.core.Controller;
import io.trickle.harvester.WindowedBrowser;
import io.trickle.harvester.pooled.SharedCaptchaToken;
import io.trickle.harvester.pooled.SharedHarvester;
import io.trickle.proxy.Proxy;
import io.trickle.proxy.ProxyController;
import io.trickle.util.Storage;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BrowserSharedHarvester
extends AbstractVerticle
implements SharedHarvester {
    public LongAdder passCounter;
    public Promise<SharedCaptchaToken> solvePromise;
    public static Logger logger = LogManager.getLogger(BrowserSharedHarvester.class);
    public boolean ready = false;
    public ExecutorService executor;
    public long timerId = -1L;
    public int reloads = 0;
    public String currentSiteURL = null;
    public Engine browserEngine;
    public WindowedBrowser browser;
    public HashMap<String, SharedCaptchaToken> referenceMap;
    public int indexedId;
    public String action;
    public LinkedHashMap<String, List<String>> requests;
    public String sitekey;
    public Proxy proxy;
    public SharedCaptchaToken currentToken = null;
    public String harvesterId;
    public List<String> deferredReplies;

    public void setInterceptors() {
        this.browser.browser().on(ConsoleMessageReceived.class, this::lambda$setInterceptors$9);
    }

    public static void lambda$waitForLogin$7(Frame frame) {
        frame.executeJavaScript("location.href = \"https://www.google.com/\"");
    }

    public InterceptUrlRequestCallback.Response lambda$initialiseBrowser$0(InterceptUrlRequestCallback.Params params) {
        String string = params.urlRequest().url();
        boolean bl = string.contains(".ico");
        if (string.contains("https://www.google.com")) {
            return InterceptUrlRequestCallback.Response.proceed();
        }
        if (bl) return InterceptUrlRequestCallback.Response.proceed();
        if (!this.isSupportedV3Site(string)) return InterceptUrlRequestCallback.Response.proceed();
        UrlRequestJob urlRequestJob = params.newUrlRequestJob(UrlRequestJob.Options.newBuilder((HttpStatus)HttpStatus.OK).build());
        urlRequestJob.write(String.format(BrowserSharedHarvester.captchaPageV3(), this.reloads, this.sitekey, this.sitekey, this.action).getBytes(StandardCharsets.UTF_8));
        urlRequestJob.complete();
        return InterceptUrlRequestCallback.Response.intercept((UrlRequestJob)urlRequestJob);
    }

    public void start(Promise promise) {
        this.vertx.eventBus().localConsumer(this.harvesterId, this::captchaRequestHandler);
        this.loadSitekey();
        this.vertx.executeBlocking(this::initialiseBrowser).onFailure(arg_0 -> ((Promise)promise).tryFail(arg_0)).onSuccess(arg_0 -> ((Promise)promise).tryComplete(arg_0));
    }

    public void stop(Promise promise) {
        this.vertx.executeBlocking(this::closeBrowsers).onComplete(arg_0 -> BrowserSharedHarvester.lambda$stop$10(promise, arg_0));
    }

    public void lambda$waitForLogin$8(Promise promise) {
        boolean bl = false;
        while (!Thread.currentThread().isInterrupted() && !bl) {
            try {
                Optional optional = this.browser.browser().mainFrame();
                if (optional.isPresent()) {
                    bl = ((Frame)optional.get()).html().contains("compose");
                }
                Thread.sleep(2222L);
            }
            catch (Throwable throwable) {
                if (this.browserEngine.isClosed() || this.browser.browser().isClosed()) {
                    this.closeBrowsers(Promise.promise());
                    continue;
                }
                throwable.printStackTrace();
            }
        }
        this.browser.browser().navigation().loadUrl("https://www.google.com/search?q=funny+youtube+videos");
        try {
            Thread.sleep(3000L);
            this.browser.browser().mainFrame().ifPresent(BrowserSharedHarvester::lambda$waitForLogin$7);
            Thread.sleep(2000L);
            this.ready = true;
            logger.warn("Starting captcha poll");
            this.startSolveLoop();
            promise.tryComplete();
            return;
        }
        catch (Exception exception) {
            logger.error("Failed to start harvester-ys-{}: {}", (Object)this.indexedId, (Object)exception.getMessage());
            promise.tryFail((Throwable)exception);
        }
    }

    public static void lambda$stop$10(Promise promise, AsyncResult asyncResult) {
        promise.complete();
    }

    public static List lambda$captchaRequestHandler$4(Message message, String string, List list) {
        list.add(message.replyAddress());
        return list;
    }

    public void closeBrowsers(Promise promise) {
        try {
            if (this.browserEngine != null && !this.browserEngine.isClosed()) {
                if (this.browser != null) {
                    this.browser.close();
                }
                for (Browser browser : this.browserEngine.browsers()) {
                    if (browser == null || browser.isClosed()) continue;
                    browser.close();
                }
                this.browserEngine.close();
            }
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        this.executor.shutdownNow();
        promise.tryComplete();
    }

    public void setProxy() {
        try {
            this.proxy = ((ProxyController)io.trickle.core.Engine.get().getModule(Controller.PROXY_CAPTCHA)).getProxyCyclic();
            if (this.proxy != null && !this.proxy.isLocal()) {
                this.setProxy(this.proxy.toParams());
                this.browser.setTitle(this.indexedId, this.proxy.string());
                return;
            }
            logger.info("Running harvester[{}] locally", (Object)this.indexedId);
            this.browser.setTitle(this.indexedId, "local-ip");
            return;
        }
        catch (Throwable throwable) {
            this.browserEngine.proxy().config(DirectProxyConfig.newInstance());
            this.browser.setTitle(this.indexedId, "local-ip");
        }
    }

    public void setProxy(String[] stringArray) {
        this.browserEngine.network().set(AuthenticateCallback.class, (Callback)((AuthenticateCallback)(arg_0, arg_1) -> BrowserSharedHarvester.lambda$setProxy$11(stringArray, arg_0, arg_1)));
        this.browserEngine.proxy().config(CustomProxyConfig.newInstance((String)String.format("http=%s:%s;https=%s:%s", stringArray[0], stringArray[1], stringArray[0], stringArray[1])));
        logger.info("Using proxy: {}", (Object)Arrays.toString(stringArray));
    }

    public void startSolveLoop() {
        this.vertx.setPeriodic(150L, this::lambda$startSolveLoop$5);
    }

    public void captchaRequestHandler(Message message) {
        String string = (String)message.body();
        if (string == null) return;
        if (string.isEmpty()) return;
        SharedCaptchaToken sharedCaptchaToken = this.referenceMap.get(string);
        if (sharedCaptchaToken != null && !sharedCaptchaToken.isExpired()) {
            message.reply((Object)sharedCaptchaToken);
            return;
        }
        this.requests.putIfAbsent(string, new ArrayList());
        this.requests.computeIfPresent(string, (arg_0, arg_1) -> BrowserSharedHarvester.lambda$captchaRequestHandler$4(message, arg_0, arg_1));
    }

    public void checkAndSolve() {
        Iterator<String> iterator;
        if (this.solvePromise != null) {
            if (!this.solvePromise.future().isComplete()) return;
        }
        if (!(iterator = this.requests.keySet().iterator()).hasNext()) return;
        this.currentSiteURL = iterator.next();
        this.solvePromise = Promise.promise();
        this.vertx.executeBlocking(this::solve).onSuccess(BrowserSharedHarvester::lambda$checkAndSolve$6);
        this.solvePromise.future().onSuccess(this::handleSolved);
    }

    public void lambda$initialiseBrowser$1() {
        this.closeBrowsers(Promise.promise());
        this.initialiseBrowser(Promise.promise());
    }

    public static void lambda$checkAndSolve$6(Void void_) {
        logger.info("Solving requested successfully!");
    }

    @Override
    public int passCount() {
        return this.passCounter.intValue();
    }

    public static EngineOptions.Builder baseOpts() {
        return EngineOptions.newBuilder((RenderingMode)RenderingMode.HARDWARE_ACCELERATED).licenseKey("1BNDIEOFAZ0H665CSFR41MCR5THTYZ8ZE7J946B9XRQ2B35XEE8PDHHOC27XGDJQURKYEQ");
    }

    public void loadSitekey() {
        this.sitekey = io.trickle.core.Engine.get().getClientConfiguration().getString("sitekeyV3", "6Lf34M8ZAAAAANgE72rhfideXH21Lab333mdd2d-");
        this.action = io.trickle.core.Engine.get().getClientConfiguration().getString("actionV3", "yzysply_wr_pageview");
        logger.info("Loaded captcha config: sitekey:'{}'; action:'{}'", (Object)this.sitekey, (Object)this.action);
    }

    public void lambda$solve$3(Frame frame) {
        frame.executeJavaScript("location.href = \"" + this.currentSiteURL + "\"");
    }

    public BrowserSharedHarvester(int n) {
        this.indexedId = n;
        this.harvesterId = UUID.randomUUID().toString();
        this.deferredReplies = new ArrayList<String>();
        this.executor = Executors.newSingleThreadExecutor();
        this.requests = new LinkedHashMap();
        this.referenceMap = new HashMap();
        this.passCounter = new LongAdder();
    }

    public void waitForLogin(Promise promise) {
        this.executor.submit(() -> this.lambda$waitForLogin$8(promise));
        this.browser.browser().navigation().loadUrl("https://accounts.google.com/ServiceLogin?continue=https%3A%2F%2Fmail.google.com%2Fmail%2F&service=mail&sacu=1&rip=1");
    }

    public void lambda$startSolveLoop$5(Long l) {
        this.checkAndSolve();
    }

    public static String captchaPageV3() {
        return "<html>\n<body style=\"background-color:#002240;\">\n<header>\n    <h1 style=\"color:#FFFFFF;\">Trickle V3 ~ %d</span> </h1>\n</header>\n<main>\n    <script src=\"https://www.google.com/recaptcha/enterprise.js?render=%s\"></script>\n    <script>\n        grecaptcha.enterprise.ready(function() {\n            grecaptcha.enterprise.execute('%s', {action: '%s'}).then(function(token) {\n                console.log(token);\n            });\n        });\n    </script>\n</main>\n</body>\n</html>";
    }

    public void handleSolved(SharedCaptchaToken sharedCaptchaToken) {
        try {
            Iterator<String> iterator = this.requests.get(sharedCaptchaToken.getDomain()).iterator();
            this.requests.remove(sharedCaptchaToken.getDomain());
            this.referenceMap.put(sharedCaptchaToken.getDomain(), sharedCaptchaToken);
            while (iterator.hasNext()) {
                String string = iterator.next();
                if (string != null && !string.isEmpty()) {
                    this.vertx.eventBus().send(string, (Object)sharedCaptchaToken);
                }
                iterator.remove();
            }
            return;
        }
        catch (Throwable throwable) {
            logger.error("Error occurred handing solves: {}", (Object)throwable.getMessage());
        }
    }

    @Override
    public String id() {
        return this.harvesterId;
    }

    public void initialiseBrowser(Promise promise) {
        logger.info("Initialising harvester instance...");
        try {
            this.browserEngine = Engine.newInstance((EngineOptions)BrowserSharedHarvester.baseOpts().userDataDir(Paths.get(Storage.CONFIG_PATH + "/harvester-ys-" + this.indexedId, new String[0])).addScheme(Scheme.HTTPS, this::lambda$initialiseBrowser$0).build());
        }
        catch (Throwable throwable) {
            logger.warn("Error loading cached data: {}", (Object)throwable.getMessage());
            if (this.browserEngine != null) {
                this.browserEngine.close();
                this.browserEngine = null;
            }
            promise.tryFail(throwable);
        }
        this.browserEngine.on(EngineCrashed.class, this::lambda$initialiseBrowser$2);
        this.browser = new WindowedBrowser(this.browserEngine);
        this.browser.createWindow();
        this.setInterceptors();
        this.setProxy();
        this.waitForLogin(promise);
    }

    public static void lambda$setProxy$11(String[] stringArray, AuthenticateCallback.Params params, AuthenticateCallback.Action action) {
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

    public void lambda$setInterceptors$9(ConsoleMessageReceived consoleMessageReceived) {
        ConsoleMessage consoleMessage = consoleMessageReceived.consoleMessage();
        String string = consoleMessage.message();
        if (string.indexOf("03") != 0) return;
        try {
            SharedCaptchaToken sharedCaptchaToken = new SharedCaptchaToken(this.currentSiteURL);
            sharedCaptchaToken.setSolved(string, this.passCounter);
            if (this.solvePromise != null) {
                this.solvePromise.complete((Object)sharedCaptchaToken);
            }
            logger.info("Received token [V3]: {}", (Object)string);
            return;
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public void solve(Promise promise) {
        try {
            if (this.reloads != 0 && this.reloads % 200 == 0) {
                this.setProxy();
                Thread.sleep(3000L);
            }
            ++this.reloads;
            Optional optional = this.browser.browser().mainFrame();
            optional.ifPresent(this::lambda$solve$3);
            return;
        }
        catch (Throwable throwable) {
            logger.warn("Error occurred solving: {}", (Object)throwable.getMessage());
            promise.tryFail(throwable);
        }
    }

    public boolean isSupportedV3Site(String string) {
        if (string.contains("https://www.google.com") && string.endsWith(".js")) {
            return true;
        }
        if (string.contains("yeezysupply")) return true;
        if (string.contains("jdsports")) return true;
        if (string.contains("finishline")) return true;
        if (string.endsWith("/account/register")) return true;
        return false;
    }

    public void lambda$initialiseBrowser$2(EngineCrashed engineCrashed) {
        int n = engineCrashed.exitCode();
        logger.warn("Harvester crashed with code: {}. Restarting...", (Object)n);
        this.ready = false;
        CompletableFuture.runAsync(this::lambda$initialiseBrowser$1);
    }
}

