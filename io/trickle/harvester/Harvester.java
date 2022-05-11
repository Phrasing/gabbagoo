/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.teamdev.jxbrowser.browser.Browser
 *  com.teamdev.jxbrowser.browser.callback.input.EnterMouseCallback
 *  com.teamdev.jxbrowser.browser.callback.input.EnterMouseCallback$Params
 *  com.teamdev.jxbrowser.browser.callback.input.EnterMouseCallback$Response
 *  com.teamdev.jxbrowser.browser.callback.input.PressKeyCallback
 *  com.teamdev.jxbrowser.browser.callback.input.PressKeyCallback$Params
 *  com.teamdev.jxbrowser.browser.callback.input.PressKeyCallback$Response
 *  com.teamdev.jxbrowser.browser.callback.input.ReleaseMouseCallback
 *  com.teamdev.jxbrowser.browser.callback.input.ReleaseMouseCallback$Params
 *  com.teamdev.jxbrowser.browser.callback.input.ReleaseMouseCallback$Response
 *  com.teamdev.jxbrowser.browser.event.ConsoleMessageReceived
 *  com.teamdev.jxbrowser.callback.Callback
 *  com.teamdev.jxbrowser.cookie.Cookie
 *  com.teamdev.jxbrowser.engine.Engine
 *  com.teamdev.jxbrowser.engine.EngineOptions
 *  com.teamdev.jxbrowser.engine.EngineOptions$Builder
 *  com.teamdev.jxbrowser.engine.RenderingMode
 *  com.teamdev.jxbrowser.frame.Frame
 *  com.teamdev.jxbrowser.js.ConsoleMessage
 *  com.teamdev.jxbrowser.js.JsObject
 *  com.teamdev.jxbrowser.navigation.event.FrameLoadFinished
 *  com.teamdev.jxbrowser.net.Scheme
 *  com.teamdev.jxbrowser.net.UploadData
 *  com.teamdev.jxbrowser.net.callback.AuthenticateCallback
 *  com.teamdev.jxbrowser.net.callback.AuthenticateCallback$Action
 *  com.teamdev.jxbrowser.net.callback.AuthenticateCallback$Params
 *  com.teamdev.jxbrowser.net.callback.BeforeUrlRequestCallback
 *  com.teamdev.jxbrowser.net.callback.BeforeUrlRequestCallback$Params
 *  com.teamdev.jxbrowser.net.callback.BeforeUrlRequestCallback$Response
 *  com.teamdev.jxbrowser.net.callback.InterceptUrlRequestCallback
 *  com.teamdev.jxbrowser.net.proxy.CustomProxyConfig
 *  com.teamdev.jxbrowser.net.proxy.DirectProxyConfig
 *  com.teamdev.jxbrowser.ui.MouseButton
 *  com.teamdev.jxbrowser.ui.Point
 *  com.teamdev.jxbrowser.ui.event.MouseMoved
 *  com.teamdev.jxbrowser.ui.event.MousePressed
 *  com.teamdev.jxbrowser.ui.event.MouseReleased
 *  io.trickle.core.Controller
 *  io.trickle.core.Engine
 *  io.trickle.harvester.CaptchaToken
 *  io.trickle.harvester.Harvester$RequestInterceptor
 *  io.trickle.harvester.Harvester$SolveFunction
 *  io.trickle.harvester.SolveFuture
 *  io.trickle.harvester.TokenController
 *  io.trickle.harvester.WindowedBrowser
 *  io.trickle.proxy.Proxy
 *  io.trickle.proxy.ProxyController
 *  io.trickle.util.Storage
 *  io.trickle.util.concurrent.ContextCompletableFuture
 *  io.trickle.util.concurrent.VertxUtil
 *  io.trickle.util.request.Request
 *  io.trickle.webclient.CookieJar
 *  io.vertx.core.buffer.Buffer
 *  io.vertx.core.http.RequestOptions
 *  io.vertx.core.json.JsonObject
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.client.HttpResponse
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package io.trickle.harvester;

import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.browser.callback.input.EnterMouseCallback;
import com.teamdev.jxbrowser.browser.callback.input.PressKeyCallback;
import com.teamdev.jxbrowser.browser.callback.input.ReleaseMouseCallback;
import com.teamdev.jxbrowser.browser.event.ConsoleMessageReceived;
import com.teamdev.jxbrowser.callback.Callback;
import com.teamdev.jxbrowser.cookie.Cookie;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.engine.RenderingMode;
import com.teamdev.jxbrowser.frame.Frame;
import com.teamdev.jxbrowser.js.ConsoleMessage;
import com.teamdev.jxbrowser.js.JsObject;
import com.teamdev.jxbrowser.navigation.event.FrameLoadFinished;
import com.teamdev.jxbrowser.net.Scheme;
import com.teamdev.jxbrowser.net.UploadData;
import com.teamdev.jxbrowser.net.callback.AuthenticateCallback;
import com.teamdev.jxbrowser.net.callback.BeforeUrlRequestCallback;
import com.teamdev.jxbrowser.net.callback.InterceptUrlRequestCallback;
import com.teamdev.jxbrowser.net.proxy.CustomProxyConfig;
import com.teamdev.jxbrowser.net.proxy.DirectProxyConfig;
import com.teamdev.jxbrowser.ui.MouseButton;
import com.teamdev.jxbrowser.ui.Point;
import com.teamdev.jxbrowser.ui.event.MouseMoved;
import com.teamdev.jxbrowser.ui.event.MousePressed;
import com.teamdev.jxbrowser.ui.event.MouseReleased;
import io.trickle.core.Controller;
import io.trickle.harvester.CaptchaToken;
import io.trickle.harvester.Harvester;
import io.trickle.harvester.SolveFuture;
import io.trickle.harvester.TokenController;
import io.trickle.harvester.WindowedBrowser;
import io.trickle.proxy.Proxy;
import io.trickle.proxy.ProxyController;
import io.trickle.util.Storage;
import io.trickle.util.concurrent.ContextCompletableFuture;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.trickle.webclient.CookieJar;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.RequestOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import java.io.ByteArrayOutputStream;
import java.lang.invoke.LambdaMetafactory;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Harvester {
    public WindowedBrowser browser;
    public SolveFunction solveFunction;
    public static Pattern V2_TYPE_PATTERN;
    public Character hotkey = null;
    public static ExecutorService executorService;
    public SolveFuture solveFuture;
    public static AtomicInteger harvesterCount;
    public ExecutorService executor;
    public int number;
    public String siteURL;
    public AtomicBoolean started;
    public Engine engine;
    public static String HARVESTER_UA;
    public Proxy proxy;
    public AtomicInteger referenceCount;
    public ByteArrayOutputStream byteArrayInputStream;
    public JsonObject rect;
    public boolean preserved = true;
    public AtomicReference<CountDownLatch> latch = new AtomicReference();
    public AtomicInteger clickCounter;
    public static AtomicBoolean isAssistedOn;
    public int reloads = 0;
    public CompletableFuture<Boolean> ready;
    public AtomicReference<CaptchaToken> tokenHolder;
    public static Logger logger;

    public static BeforeUrlRequestCallback.Response lambda$setInterceptors$7(BeforeUrlRequestCallback.Params params) {
        if (!params.urlRequest().url().contains("recaptcha.net")) return BeforeUrlRequestCallback.Response.proceed();
        return BeforeUrlRequestCallback.Response.redirect((String)params.urlRequest().url().replace("recaptcha.net", "google.com"));
    }

    public void lambda$setInterceptors$0() {
        logger.info("Clicking box");
        if (this.rect != null) {
            this.clickLocationSlide(this.rect.getInteger("left") + 35 + ThreadLocalRandom.current().nextInt(-9, 9), this.rect.getInteger("top") + this.rect.getInteger("height") / 2 + ThreadLocalRandom.current().nextInt(-9, 9));
        } else {
            logger.error("Fallback click");
            this.clickLocationSlide(ThreadLocalRandom.current().nextInt(55, 70), ThreadLocalRandom.current().nextInt(110, 115));
        }
    }

    public void submitClick() {
        this.clickLocation(268, 518);
    }

    public EngineOptions.Builder baseOpts() {
        return EngineOptions.newBuilder((RenderingMode)RenderingMode.HARDWARE_ACCELERATED).licenseKey("1BNDIEOFAZ0H665CSFR41MCR5THTYZ8ZE7J946B9XRQ2B35XEE8PDHHOC27XGDJQURKYEQ").addScheme(Scheme.HTTPS, (InterceptUrlRequestCallback)new RequestInterceptor(this));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public CompletableFuture init() {
        this.number = harvesterCount.incrementAndGet();
        Path path = Paths.get(Storage.CONFIG_PATH + "/harvester" + this.number, new String[0]);
        String string = "harvesterinit";
        synchronized ("harvesterinit") {
            while (this.engine == null) {
                if (this.preserved) {
                    try {
                        if (!Files.exists(path, new LinkOption[0])) {
                            this.preserved = false;
                        }
                        this.engine = Engine.newInstance((EngineOptions)this.baseOpts().userDataDir(path).build());
                    }
                    catch (Throwable throwable) {
                        logger.warn("Unable to locate chrome. Please wait: {}", (Object)throwable.getMessage());
                        try {
                            if (this.engine == null) continue;
                            this.engine.close();
                        }
                        catch (Throwable throwable2) {
                            logger.warn("Error loading cached data backup: {}", (Object)throwable.getMessage());
                            this.sleep(5000);
                        }
                    }
                    continue;
                }
                this.engine = Engine.newInstance((EngineOptions)this.baseOpts().build());
            }
            // ** MonitorExit[var2_2] (shouldn't be in output)
            this.browser = new WindowedBrowser(this.engine);
            this.browser.createWindow();
            this.setInterceptors();
            this.setProxy();
            this.waitForLogin();
            this.startSolver();
            if (HARVESTER_UA != null) return CompletableFuture.completedFuture(null);
            HARVESTER_UA = this.browser.browser().userAgent();
            return CompletableFuture.completedFuture(null);
        }
    }

    public ReleaseMouseCallback.Response lambda$setInterceptors$5(ReleaseMouseCallback.Params params) {
        if (params.event().toString().contains("key_modifiers")) {
            int n = this.clickCounter.incrementAndGet();
            if (!isAssistedOn.get()) return ReleaseMouseCallback.Response.proceed();
            if (n != 3) return ReleaseMouseCallback.Response.proceed();
            executorService.submit(this::submitClick);
        } else {
            this.resetClickCounter();
        }
        return ReleaseMouseCallback.Response.proceed();
    }

    public static String captchaReadyCallback() {
        return "function elementReady(selector) {\n  return new Promise((resolve, reject) => {\n    const el = document.querySelector(selector);\n    if (el) {resolve(el);}\n    new MutationObserver((mutationRecords, observer) => {\n      // Query for elements matching the specified selector\n      Array.from(document.querySelectorAll(selector)).forEach((element) => {\n        resolve(element);\n        //Once we have resolved we don't need the observer anymore.\n        observer.disconnect();\n      });\n    })\n      .observe(document.documentElement, {\n        childList: true,\n        subtree: true\n      });\n  });\n}\n\nelementReady('#recaptcha-anchor').then(selector =>{ \n   console.log(20022002);    \n});\nelementReady('#checkbox').then(selector => { \n   console.log(20022002);    \n});";
    }

    public void transferCookies(String string) {
        List list = this.engine.cookieStore().cookies(string);
        CookieJar cookieJar = this.tokenHolder.get().getCookieJar();
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            Cookie cookie = (Cookie)iterator.next();
            try {
                cookieJar.put(cookie.name(), cookie.value(), cookie.domain());
            }
            catch (Exception exception) {}
        }
    }

    public static String captchaPageV2() {
        return "<html>\n    <div class=\"shopify-challenge__container\">\n        <script>\n            //<![CDATA[\n            var onCaptchaSuccess = function() {\n                var event;\n\n                try {\n                    event = new Event('captchaSuccess', {bubbles: true, cancelable: true});\n                } catch (e) {\n                    event = document.createEvent('Event');\n                    event.initEvent('captchaSuccess', true, true);\n                }\n\n                window.dispatchEvent(event);\n            }\n\n            //]]>\n        </script>\n        <script>\n            //<![CDATA[\n            window.addEventListener('captchaSuccess', function() {\n                var responseInput = document.querySelector('.g-recaptcha-response');\n                var submitButton = document.querySelector('.dialog-submit');\n\n                if (submitButton instanceof HTMLElement) {\n                    var needResponse = (responseInput instanceof HTMLElement);\n                    var responseValueMissing = !responseInput.value;\n                    submitButton.disabled = (needResponse && responseValueMissing);\n                }\n            }, false);\n\n            //]]>\n        </script>\n        <script>\n            //<![CDATA[\n            var recaptchaCallback = function() {\n                grecaptcha.render('g-recaptcha', {\n                    sitekey: \"%s\",\n                    size: (window.innerWidth > 320) ? 'normal' : 'compact',\n                    callback: 'onCaptchaSuccess',\n                });\n            };\n\n            //]]>\n        </script>\n        <script src=\"https://www.recaptcha.net/recaptcha/api.js?onload=recaptchaCallback&amp;render=6LcCR2cUAAAAANS1Gpq_mDIJ2pQuJphsSQaUEuc9&amp;hl=en\" async=\"async\">\n            //<![CDATA[\n\n            //]]>\n        </script>\n        <noscript><div class=\"g-recaptcha-nojs\"><iframe class=\"g-recaptcha-nojs__iframe\" frameborder=\"0\" scrolling=\"no\" src=\"https://www.google.com/recaptcha/api/fallback?k=%s\"></iframe><div class=\"g-recaptcha-nojs__input-wrapper\"><textarea id=\"g-recaptcha-response\" name=\"g-recaptcha-response\" class=\"g-recaptcha-nojs__input\">\n</textarea></div></div></noscript>\n        <script>\n            new Promise(resolve => {\n                window.addEventListener('captchaSuccess', () => {\n                    const token = grecaptcha.getResponse();\n                    window.completion.completed(token);\n                }, false);\n            });\n        </script>\n        <div id=\"g-recaptcha\" class=\"g-recaptcha\"></div>\n    </div>\n</html>";
    }

    public void clickLocation(int n, int n2) {
        Point point = Point.of((int)ThreadLocalRandom.current().nextInt(n - 2, n + 2), (int)ThreadLocalRandom.current().nextInt(n2 - 2, n2 + 2));
        MousePressed mousePressed = MousePressed.newBuilder((Point)point).button(MouseButton.PRIMARY).clickCount(1).build();
        MouseReleased mouseReleased = MouseReleased.newBuilder((Point)point).button(MouseButton.PRIMARY).clickCount(1).build();
        MouseMoved mouseMoved = MouseMoved.newBuilder((Point)point).build();
        this.browser.browser().dispatch(mouseMoved);
        this.browser.browser().dispatch(mousePressed);
        this.browser.browser().dispatch(mouseReleased);
    }

    public void resetClickCounter() {
        this.clickCounter.set(0);
    }

    public void sleep(int n) {
        try {
            Thread.sleep(n);
        }
        catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }

    public void setInterceptors() {
        this.browser.browser().on(ConsoleMessageReceived.class, this::lambda$setInterceptors$1);
        this.browser.browser().navigation().on(FrameLoadFinished.class, this::lambda$setInterceptors$3);
        this.browser.browser().set(EnterMouseCallback.class, (Callback)((EnterMouseCallback)this::lambda$setInterceptors$4));
        this.browser.browser().set(ReleaseMouseCallback.class, (Callback)((ReleaseMouseCallback)this::lambda$setInterceptors$5));
        this.browser.browser().set(PressKeyCallback.class, (Callback)((PressKeyCallback)this::lambda$setInterceptors$6));
        this.engine.network().set(BeforeUrlRequestCallback.class, (Callback)((BeforeUrlRequestCallback)Harvester::lambda$setInterceptors$7));
    }

    public CompletableFuture start() {
        this.referenceCount.incrementAndGet();
        try {
            if (this.started.get()) return CompletableFuture.completedFuture(true);
            if (this.ready == null) {
                this.ready = new ContextCompletableFuture();
            }
            this.executor.submit(this::init);
            logger.debug("Waiting to start!");
            CompletableFuture<Boolean> completableFuture = this.ready;
            if (!completableFuture.isDone()) {
                CompletableFuture<Boolean> completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Harvester.async$start(this, completableFuture2, 1, arg_0));
            }
            completableFuture.join();
            this.started.set(true);
            logger.debug("Started!");
        }
        catch (Throwable throwable) {
            logger.error("Start error on harvester {}", (Object)throwable.getMessage());
        }
        return CompletableFuture.completedFuture(true);
    }

    public void waitForLogin() {
        if (!this.preserved) {
            this.executor.submit(this::lambda$waitForLogin$9);
            this.browser.browser().navigation().loadUrl("https://accounts.google.com/signin/v2/identifier?service=mail&passive=true&rm=false&continue=https%3A%2F%2Fmail.google.com%2Fmail%2F&ss=1&scc=1&ltmpl=default&ltmplcache=2&emr=1&osid=1&flowName=GlifWebSignIn&flowEntry=ServiceLogin");
        } else {
            this.browser.browser().navigation().loadUrl("https://www.google.com/");
            this.ready.complete(true);
        }
    }

    public void setProxy() {
        this.proxy = ((ProxyController)io.trickle.core.Engine.get().getModule(Controller.PROXY_CAPTCHA)).getProxy(this.number - 1);
        if (this.proxy == null) return;
        String string = this.setProxy(this.proxy);
        logger.info("Using proxy: {}", (Object)string);
        this.browser.setTitle(this.proxy.getHost() == null ? "local" : this.proxy.toString());
    }

    public void lambda$setInterceptors$1(ConsoleMessageReceived consoleMessageReceived) {
        ConsoleMessage consoleMessage = consoleMessageReceived.consoleMessage();
        String string = consoleMessage.message();
        if (string.indexOf("03") == 0) {
            try {
                CaptchaToken captchaToken = this.tokenHolder.get();
                if (captchaToken == null) return;
                captchaToken.setTokenValues(string);
                logger.info("Received token [V3]");
                this.tokenHolder.set(null);
                SolveFuture solveFuture = this.solveFuture;
                if (solveFuture != null && !solveFuture.isDone()) {
                    solveFuture.complete(captchaToken);
                }
                this.latch.get().countDown();
            }
            catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        } else if (string.equals("20022002")) {
            executorService.submit(this::lambda$setInterceptors$0);
        } else {
            if (!string.startsWith("{\"x\":")) return;
            this.rect = new JsonObject(string);
        }
    }

    public void lambda$setInterceptors$3(FrameLoadFinished frameLoadFinished) {
        String string = frameLoadFinished.url();
        if (this.solveFuture == null || !string.contains("recaptcha/api2/anchor")) {
            if (!string.contains("hcaptcha.com/")) return;
        }
        executorService.submit(() -> this.lambda$setInterceptors$2(frameLoadFinished));
    }

    public CompletableFuture send(Supplier supplier, Optional optional) {
        int n = 0;
        while (n++ < Integer.MAX_VALUE) {
            String string;
            try {
                HttpResponse httpResponse;
                HttpResponse httpResponse2;
                RequestOptions requestOptions = (RequestOptions)supplier.get();
                string = this.tokenHolder.get().client.request(requestOptions.getMethod(), requestOptions);
                if (optional.isEmpty()) {
                    CompletableFuture completableFuture = Request.send((HttpRequest)string);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture2 = completableFuture;
                        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Harvester.async$send(this, (Supplier)supplier, optional, n, requestOptions, (HttpRequest)string, completableFuture2, 1, arg_0));
                    }
                    httpResponse2 = (HttpResponse)completableFuture.join();
                } else {
                    CompletableFuture completableFuture = Request.send((HttpRequest)string, (Buffer)Buffer.buffer((byte[])((UploadData)optional.get()).bytes()));
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture3 = completableFuture;
                        return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> Harvester.async$send(this, (Supplier)supplier, optional, n, requestOptions, (HttpRequest)string, completableFuture3, 2, arg_0));
                    }
                    httpResponse2 = httpResponse = (HttpResponse)completableFuture.join();
                }
                if (httpResponse != null) {
                    return CompletableFuture.completedFuture(httpResponse);
                }
                logger.error("No response {}", (Object)requestOptions.getURI());
            }
            catch (Throwable throwable) {
                string = throwable.getMessage();
                if (string.contains("timeout period")) {
                    logger.error("request timeout. no response");
                }
                if (string.contains("proxy")) {
                    logger.error("unable to connect to proxy - {}", (Object)string);
                }
                logger.error("network error " + throwable.getMessage());
            }
            CompletableFuture completableFuture = VertxUtil.randomSleep((long)1000L);
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture4 = completableFuture;
                return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Harvester.async$send(this, (Supplier)supplier, optional, n, null, null, completableFuture4, 3, arg_0));
            }
            completableFuture.join();
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    public static CompletableFuture async$send(Harvester var0, Supplier var1_1, Optional var2_2, int var3_3, RequestOptions var4_4, HttpRequest var5_6, CompletableFuture var6_7, int var7_8, Object var8_12) {
        switch (var7_8) {
            case 0: {
                var3_3 = 0;
                block8: while (var3_3++ < 0x7FFFFFFF) {
                    try {
                        var4_4 = (RequestOptions)var1_1.get();
                        var5_6 = var0.tokenHolder.get().client.request(var4_4.getMethod(), var4_4);
                        if (!var2_2.isEmpty()) ** GOTO lbl16
                        v0 = Request.send((HttpRequest)var5_6);
                        if (!v0.isDone()) {
                            var7_9 = v0;
                            return var7_9.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$send(io.trickle.harvester.Harvester java.util.function.Supplier java.util.Optional int io.vertx.core.http.RequestOptions io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Harvester)var0, (Supplier)var1_1, (Optional)var2_2, (int)var3_3, (RequestOptions)var4_4, (HttpRequest)var5_6, (CompletableFuture)var7_9, (int)1));
                        }
lbl13:
                        // 3 sources

                        while (true) {
                            v1 /* !! */  = (HttpResponse)v0.join();
                            ** GOTO lbl22
                            break;
                        }
lbl16:
                        // 1 sources

                        v2 = Request.send((HttpRequest)var5_6, (Buffer)Buffer.buffer((byte[])((UploadData)var2_2.get()).bytes()));
                        if (!v2.isDone()) {
                            var7_10 = v2;
                            return var7_10.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$send(io.trickle.harvester.Harvester java.util.function.Supplier java.util.Optional int io.vertx.core.http.RequestOptions io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Harvester)var0, (Supplier)var1_1, (Optional)var2_2, (int)var3_3, (RequestOptions)var4_4, (HttpRequest)var5_6, (CompletableFuture)var7_10, (int)2));
                        }
lbl20:
                        // 3 sources

                        while (true) {
                            v1 /* !! */  = var6_7 = (HttpResponse)v2.join();
lbl22:
                            // 2 sources

                            if (var6_7 != null) {
                                return CompletableFuture.completedFuture(var6_7);
                            }
                            Harvester.logger.error("No response {}", (Object)var4_4.getURI());
                            break;
                        }
                    }
                    catch (Throwable var4_5) {
                        var5_6 = var4_5.getMessage();
                        if (var5_6.contains("timeout period")) {
                            Harvester.logger.error("request timeout. no response");
                        }
                        if (var5_6.contains("proxy")) {
                            Harvester.logger.error("unable to connect to proxy - {}", var5_6);
                        }
                        Harvester.logger.error("network error " + var4_5.getMessage());
                    }
                    if (!(v3 = VertxUtil.randomSleep((long)1000L)).isDone()) {
                        var7_11 = v3;
                        return var7_11.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$send(io.trickle.harvester.Harvester java.util.function.Supplier java.util.Optional int io.vertx.core.http.RequestOptions io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Harvester)var0, (Supplier)var1_1, (Optional)var2_2, (int)var3_3, null, null, (CompletableFuture)var7_11, (int)3));
                    }
lbl38:
                    // 3 sources

                    while (true) {
                        v3.join();
                        continue block8;
                        break;
                    }
                }
                return CompletableFuture.completedFuture(null);
            }
            case 1: {
                v0 = var6_7;
                ** continue;
            }
            case 2: {
                v2 = var6_7;
                ** continue;
            }
            case 3: {
                v3 = var6_7;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public String setProxy(Proxy proxy) {
        if (proxy.isLocal()) {
            this.engine.proxy().config(DirectProxyConfig.newInstance());
            return "no-proxy";
        }
        Object[] objectArray = proxy.toParams();
        this.engine.network().set(AuthenticateCallback.class, (Callback)((AuthenticateCallback)(arg_0, arg_1) -> Harvester.lambda$setProxy$11((String[])objectArray, arg_0, arg_1)));
        this.engine.proxy().config(CustomProxyConfig.newInstance((String)String.format("http=%s:%s;https=%s:%s", objectArray[0], objectArray[1], objectArray[0], objectArray[1])));
        return Arrays.toString(objectArray);
    }

    public static void lambda$waitForLogin$8(Frame frame) {
        frame.executeJavaScript("location.href = \"https://www.youtube.com/\"");
    }

    public PressKeyCallback.Response lambda$setInterceptors$6(PressKeyCallback.Params params) {
        if (this.hotkey == null) return PressKeyCallback.Response.proceed();
        if (params.event().keyChar() != this.hotkey.charValue()) return PressKeyCallback.Response.proceed();
        executorService.submit(this::submitClick);
        return PressKeyCallback.Response.proceed();
    }

    public void lambda$setInterceptors$2(FrameLoadFinished frameLoadFinished) {
        Optional optional = this.browser.browser().mainFrame();
        frameLoadFinished.frame().executeJavaScript(Harvester.captchaReadyCallback());
        if (optional.isPresent()) {
            JsObject jsObject = (JsObject)((Frame)optional.get()).executeJavaScript("window");
            if (jsObject == null) return;
            jsObject.putProperty("completion", (Object)this.solveFunction);
        } else {
            logger.error("No browser frame available");
        }
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$start(Harvester var0, CompletableFuture var1_1, int var2_3, Object var3_5) {
        switch (var2_3) {
            case 0: {
                var0.referenceCount.incrementAndGet();
                try {
                    if (var0.started.get() != false) return CompletableFuture.completedFuture(true);
                    if (var0.ready == null) {
                        var0.ready = new ContextCompletableFuture();
                    }
                    var0.executor.submit((Callable<CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, ()Ljava/lang/Object;, init(), ()Ljava/util/concurrent/CompletableFuture;)((Harvester)var0));
                    Harvester.logger.debug("Waiting to start!");
                    v0 = var0.ready;
                    if (!v0.isDone()) {
                        var2_4 = v0;
                        return var2_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$start(io.trickle.harvester.Harvester java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Harvester)var0, var2_4, (int)1));
                    }
lbl16:
                    // 3 sources

                    while (true) {
                        v0.join();
                        var0.started.set(true);
                        Harvester.logger.debug("Started!");
                        break;
                    }
                }
                catch (Throwable var1_2) {
                    Harvester.logger.error("Start error on harvester {}", (Object)var1_2.getMessage());
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

    public void lambda$startSolver$10() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                this.latch.set(new CountDownLatch(1));
                this.solveFuture = ((TokenController)io.trickle.core.Engine.get().getModule(Controller.TOKEN)).pollWaitingList();
                this.solveFunction = new SolveFunction(this.solveFuture, this.latch);
                logger.info("Attempting solve");
                this.resetClickCounter();
                CaptchaToken captchaToken = this.solveFuture.getEmptyCaptchaToken();
                this.tokenHolder.set(captchaToken);
                if (captchaToken.isCheckpoint()) {
                    this.configureCheckpointDetails();
                } else {
                    this.setProxy(this.proxy);
                }
                ++this.reloads;
                this.siteURL = captchaToken.getDomain();
                this.browser.browser().navigation().loadUrl(this.siteURL);
                this.latch.get().await();
            }
            catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }
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

    public EnterMouseCallback.Response lambda$setInterceptors$4(EnterMouseCallback.Params params) {
        this.browser.frame().toFront();
        this.browser.frame().requestFocusInWindow();
        this.browser.browser().focus();
        return EnterMouseCallback.Response.proceed();
    }

    public static void lambda$setProxy$11(String[] stringArray, AuthenticateCallback.Params params, AuthenticateCallback.Action action) {
        if (params.isProxy()) {
            logger.info("Enabling proxy");
            if (stringArray.length == 4) {
                action.authenticate(stringArray[2], stringArray[3]);
            } else {
                action.cancel();
            }
        } else {
            action.cancel();
        }
    }

    public void lambda$waitForLogin$9() {
        boolean bl = false;
        while (!Thread.currentThread().isInterrupted() && !bl) {
            try {
                Optional optional = this.browser.browser().mainFrame();
                if (optional.isPresent()) {
                    bl = ((Frame)optional.get()).html().contains("compose");
                }
                this.sleep(2222);
            }
            catch (Throwable throwable) {
                if (this.engine.isClosed() || this.browser.browser().isClosed()) {
                    this.ready.complete(false);
                    this.close();
                    continue;
                }
                throwable.printStackTrace();
            }
        }
        this.browser.frame().setVisible(false);
        this.browser.browser().navigation().loadUrl("https://www.google.com/search?q=youtube+videos");
        try {
            this.sleep(2000);
            this.browser.browser().mainFrame().ifPresent(Harvester::lambda$waitForLogin$8);
            this.sleep(1000);
            this.browser.frame().setVisible(true);
            this.ready.complete(true);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            this.ready.complete(false);
        }
    }

    public void clickLocationSlide(int n, int n2) {
        int n3 = 0;
        while (true) {
            MouseMoved mouseMoved;
            Point point;
            if (n3 >= ThreadLocalRandom.current().nextInt(10, 30)) {
                Point point2 = Point.of((int)ThreadLocalRandom.current().nextInt(n - 2, n + 2), (int)ThreadLocalRandom.current().nextInt(n2 - 2, n2 + 2));
                point = MousePressed.newBuilder((Point)point2).button(MouseButton.PRIMARY).clickCount(1).build();
                mouseMoved = MouseReleased.newBuilder((Point)point2).button(MouseButton.PRIMARY).clickCount(1).build();
                MouseMoved mouseMoved2 = MouseMoved.newBuilder((Point)point2).build();
                this.browser.browser().dispatch(mouseMoved2);
                this.browser.browser().dispatch((MousePressed)point);
                this.sleep(ThreadLocalRandom.current().nextInt(1, 5));
                this.browser.browser().dispatch((MouseReleased)mouseMoved);
                return;
            }
            point = Point.of((int)(n + ThreadLocalRandom.current().nextInt(-20, 50)), (int)(n2 + ThreadLocalRandom.current().nextInt(-20, 20)));
            mouseMoved = MouseMoved.newBuilder((Point)point).build();
            this.browser.browser().dispatch(mouseMoved);
            this.sleep(ThreadLocalRandom.current().nextInt(1, 3));
            ++n3;
        }
    }

    public static String captchaPageV3() {
        return "<html>\n<body style=\"background-color:#002240;\">\n<header>\n    <h1 style=\"color:#FFFFFF;\">Trickle V3 ~ %d</span> </h1>\n</header>\n<main>\n    <script src=\"https://www.recaptcha.net/recaptcha/api.js?onload=storefrontContactFormsRecaptchaCallback&render=%s&hl=en\"></script>\n    <script>\n        grecaptcha.ready(function() {\n            grecaptcha.execute('%s', {action: '%s'}).then(function(token) {\n                console.log(token);\n            });\n        });\n    </script>\n</main>\n</body>\n</html>";
    }

    public void configureCheckpointDetails() {
        if (this.tokenHolder.get() != null && this.tokenHolder.get().isHcaptcha) {
            CaptchaToken captchaToken = this.solveFuture.getEmptyCaptchaToken();
            this.setProxy(Proxy.fromArray((String[])captchaToken.getProxyStr().split(":")));
        } else {
            this.engine.proxy().config(DirectProxyConfig.newInstance());
        }
    }

    public void startSolver() {
        this.executor.submit(this::lambda$startSolver$10);
    }

    static {
        logger = LogManager.getLogger(Harvester.class);
        harvesterCount = new AtomicInteger(0);
        V2_TYPE_PATTERN = Pattern.compile("<strong style=\"font-size: ..px;\">(.*?)<");
        executorService = Executors.newFixedThreadPool(Storage.getHarvesterCountYs());
        isAssistedOn = new AtomicBoolean(false);
    }

    public Harvester() {
        this.executor = Executors.newSingleThreadExecutor();
        this.clickCounter = new AtomicInteger(0);
        this.referenceCount = new AtomicInteger(0);
        this.started = new AtomicBoolean(false);
        this.tokenHolder = new AtomicReference<Object>(null);
    }
}
