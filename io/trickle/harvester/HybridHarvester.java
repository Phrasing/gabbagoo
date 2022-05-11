/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.teamdev.jxbrowser.browser.Browser
 *  com.teamdev.jxbrowser.callback.Callback
 *  com.teamdev.jxbrowser.engine.Engine
 *  com.teamdev.jxbrowser.engine.EngineOptions
 *  com.teamdev.jxbrowser.engine.EngineOptions$Builder
 *  com.teamdev.jxbrowser.engine.RenderingMode
 *  com.teamdev.jxbrowser.net.Scheme
 *  com.teamdev.jxbrowser.net.callback.AuthenticateCallback
 *  com.teamdev.jxbrowser.net.callback.AuthenticateCallback$Action
 *  com.teamdev.jxbrowser.net.callback.AuthenticateCallback$Params
 *  com.teamdev.jxbrowser.net.callback.InterceptUrlRequestCallback
 *  com.teamdev.jxbrowser.net.proxy.CustomProxyConfig
 *  com.teamdev.jxbrowser.net.proxy.DirectProxyConfig
 *  io.trickle.core.Controller
 *  io.trickle.core.Engine
 *  io.trickle.core.VertxSingleton
 *  io.trickle.harvester.HybridHarvester$RequestInterceptor
 *  io.trickle.harvester.WindowedBrowser
 *  io.trickle.harvester.WindowedUrlBrowser
 *  io.trickle.proxy.Proxy
 *  io.trickle.proxy.ProxyController
 *  io.vertx.core.Vertx
 */
package io.trickle.harvester;

import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.callback.Callback;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.engine.RenderingMode;
import com.teamdev.jxbrowser.net.Scheme;
import com.teamdev.jxbrowser.net.callback.AuthenticateCallback;
import com.teamdev.jxbrowser.net.callback.InterceptUrlRequestCallback;
import com.teamdev.jxbrowser.net.proxy.CustomProxyConfig;
import com.teamdev.jxbrowser.net.proxy.DirectProxyConfig;
import io.trickle.core.Controller;
import io.trickle.core.Engine;
import io.trickle.core.VertxSingleton;
import io.trickle.harvester.HybridHarvester;
import io.trickle.harvester.WindowedBrowser;
import io.trickle.harvester.WindowedUrlBrowser;
import io.trickle.proxy.Proxy;
import io.trickle.proxy.ProxyController;
import io.vertx.core.Vertx;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HybridHarvester {
    public WindowedBrowser browser;
    public com.teamdev.jxbrowser.engine.Engine engine;
    public Vertx vertx;
    public ExecutorService executor;

    public static void main(String[] stringArray) {
        HybridHarvester hybridHarvester = new HybridHarvester(VertxSingleton.INSTANCE.get());
        hybridHarvester.start();
    }

    public EngineOptions.Builder baseOpts() {
        return EngineOptions.newBuilder((RenderingMode)RenderingMode.HARDWARE_ACCELERATED).licenseKey("1BNDIEOFAZ0H665CSFR41MCR5THTYZ8ZE7J946B9XRQ2B35XEE8PDHHOC27XGDJQURKYEQ").addScheme(Scheme.HTTPS, (InterceptUrlRequestCallback)new RequestInterceptor());
    }

    public void close() {
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
            CompletableFuture.runAsync(() -> ((com.teamdev.jxbrowser.engine.Engine)this.engine).close());
        }
        this.executor.shutdownNow();
    }

    public CompletableFuture start() {
        this.executor.submit(this::init);
        return CompletableFuture.completedFuture(true);
    }

    public static void lambda$setProxy$0(String[] stringArray, AuthenticateCallback.Params params, AuthenticateCallback.Action action) {
        if (params.isProxy()) {
            if (stringArray.length == 4) {
                action.authenticate(stringArray[2], stringArray[3]);
            } else {
                action.cancel();
            }
        } else {
            action.cancel();
        }
    }

    public void init() {
        this.engine = com.teamdev.jxbrowser.engine.Engine.newInstance((EngineOptions)this.baseOpts().build());
        try {
            this.setProxy(((ProxyController)Engine.get().getModule(Controller.PROXY)).getProxy(0));
        }
        catch (Exception exception) {
            // empty catch block
        }
        this.browser = new WindowedUrlBrowser(this.engine);
        this.browser.createWindow();
    }

    public HybridHarvester(Vertx vertx) {
        this.vertx = vertx;
        this.executor = Executors.newSingleThreadExecutor();
    }

    public String setProxy(Proxy proxy) {
        if (proxy.isLocal()) {
            this.engine.proxy().config(DirectProxyConfig.newInstance());
            return "no-proxy";
        }
        Object[] objectArray = proxy.toParams();
        this.engine.network().set(AuthenticateCallback.class, (Callback)((AuthenticateCallback)(arg_0, arg_1) -> HybridHarvester.lambda$setProxy$0((String[])objectArray, arg_0, arg_1)));
        this.engine.proxy().config(CustomProxyConfig.newInstance((String)String.format("http=%s:%s;https=%s:%s", objectArray[0], objectArray[1], objectArray[0], objectArray[1])));
        return Arrays.toString(objectArray);
    }
}
