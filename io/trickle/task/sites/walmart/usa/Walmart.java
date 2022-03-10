/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.netty.handler.codec.http.cookie.Cookie
 *  io.vertx.core.Handler
 *  io.vertx.core.buffer.Buffer
 *  io.vertx.core.impl.ConcurrentHashSet
 *  io.vertx.core.json.JsonArray
 *  io.vertx.core.json.JsonObject
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.client.HttpResponse
 *  io.vertx.ext.web.codec.BodyCodec
 */
package io.trickle.task.sites.walmart.usa;

import io.netty.handler.codec.http.cookie.Cookie;
import io.trickle.account.Account;
import io.trickle.account.AccountController;
import io.trickle.core.Controller;
import io.trickle.core.Engine;
import io.trickle.core.VertxSingleton;
import io.trickle.core.actor.TaskActor;
import io.trickle.task.Task;
import io.trickle.task.antibot.impl.px.PerimeterX;
import io.trickle.task.antibot.impl.px.tools.Deobfuscator;
import io.trickle.task.sites.walmart.Mode;
import io.trickle.task.sites.walmart.usa.API;
import io.trickle.task.sites.walmart.usa.PaymentInstance;
import io.trickle.task.sites.walmart.usa.SessionPreload;
import io.trickle.task.sites.walmart.usa.WalmartAPI;
import io.trickle.task.sites.walmart.usa.WalmartAPIDesktop;
import io.trickle.task.sites.walmart.usa.handling.EmptyCartException;
import io.trickle.task.sites.walmart.util.PaymentToken;
import io.trickle.task.sites.walmart.util.Utils;
import io.trickle.util.analytics.Analytics;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.trickle.webclient.CookieJar;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.impl.ConcurrentHashSet;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.codec.BodyCodec;
import java.lang.invoke.LambdaMetafactory;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

public class Walmart
extends TaskActor {
    public CookieJar sessionCookies = new CookieJar();
    public long keepAliveWorker = 0L;
    public API api;
    public Task task;
    public String itemKeyword;
    public String instanceSignal;
    public String productID = "";
    public boolean bannedBefore = false;
    public boolean shared;
    public Mode mode;
    public PaymentToken token;
    public static ConcurrentHashSet<Cookie> GLOBAL_COOKIES = new ConcurrentHashSet();
    public long sessionTimestamp = 0L;

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$cartFast(Walmart var0, int var1_1, int var2_2, String var3_3, HttpRequest var4_4, CompletableFuture var5_6, Buffer var6_8, HttpResponse var7_9, int var8_10, Throwable var9_11, int var10_12, Object var11_13) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [11[CATCHBLOCK]], but top level block is 19[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:845)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1042)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:929)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:73)
         *     at org.benf.cfr.reader.Main.main(Main.java:49)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:303)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$null$5(ResourceDecompiling.java:158)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public CompletableFuture fetchHomepage(boolean bl) {
        this.logger.info("Checking homepage");
        int n = 0;
        while (this.running) {
            if (n++ >= 100) return CompletableFuture.failedFuture(new Exception());
            try {
                HttpRequest httpRequest = this.api.homepage();
                CompletableFuture completableFuture = Request.send(httpRequest);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$fetchHomepage(this, (int)(bl ? 1 : 0), n, httpRequest, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 307) {
                    this.logger.info("Blocked while visiting homepage. Handling...");
                    CompletableFuture completableFuture3 = this.api.handleBadResponse(httpResponse.statusCode(), httpResponse);
                    if (!completableFuture3.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture3;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$fetchHomepage(this, (int)(bl ? 1 : 0), n, httpRequest, completableFuture4, httpResponse, null, 2, arg_0));
                    }
                    completableFuture3.join();
                    continue;
                }
                if (httpResponse.statusCode() != 200) {
                    this.logger.warn("Failed visiting homepage: status:'{}'", (Object)httpResponse.statusCode());
                    CompletableFuture completableFuture5 = VertxUtil.randomSleep(this.task.getMonitorDelay());
                    if (!completableFuture5.isDone()) {
                        CompletableFuture completableFuture6 = completableFuture5;
                        return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$fetchHomepage(this, (int)(bl ? 1 : 0), n, httpRequest, completableFuture6, httpResponse, null, 3, arg_0));
                    }
                    completableFuture5.join();
                    continue;
                }
                this.api.cookieStore().put("viq", "walmart", "www.walmart.com");
                this.api.cookieStore().put("cart-item-count", "0", "www.walmart.com");
                this.api.cookieStore().put("_uetsid", Utils.genUet(), "www.walmart.com");
                this.api.cookieStore().put("_uetvid", Utils.genUet(), "www.walmart.com");
                this.api.cookieStore().put("s_sess_2", "prop32%3D", "www.walmart.com");
                this.api.cookieStore().put("TBV", "7", "www.walmart.com");
                this.api.cookieStore().put("TB_DC_Flap_Test", "0", "www.walmart.com");
                this.api.cookieStore().put("TB_SFOU-100", "", "www.walmart.com");
                try {
                    this.api.cookieStore().put("athrvi", "RVI~h" + Integer.parseInt(this.task.getKeywords()[0], 16), "www.walmart.com");
                }
                catch (Exception exception) {
                    // empty catch block
                }
                this.api.cookieStore().put("_gcl_au", "1.1.1957667684." + Instant.now().getEpochSecond(), "www.walmart.com");
                return CompletableFuture.completedFuture(null);
            }
            catch (Throwable throwable) {
                this.logger.error("Error occurred fetching homepage: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = super.randomSleep(12000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$fetchHomepage(this, (int)(bl ? 1 : 0), n, null, completableFuture7, null, throwable, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.failedFuture(new Exception());
    }

    public CompletableFuture cartFast() {
        int n = 0;
        int n2 = 0;
        String string = null;
        while (this.running) {
            if (n2++ > 30) return CompletableFuture.completedFuture(-1);
            try {
                Buffer buffer;
                HttpResponse httpResponse;
                HttpRequest httpRequest;
                if (!this.api.cookieStore().contains("CRT")) {
                    this.logger.warn("Starting session");
                    httpRequest = this.api.getCart();
                    CompletableFuture completableFuture = Request.send(httpRequest);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture2 = completableFuture;
                        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$cartFast(this, n, n2, string, httpRequest, completableFuture2, null, null, 0, null, 1, arg_0));
                    }
                    httpResponse = (HttpResponse)completableFuture.join();
                } else if (string == null) {
                    this.logger.warn("Creating session");
                    n = 1;
                    httpRequest = this.api.savedCart();
                    buffer = new JsonObject().put("offerId", (Object)this.task.getKeywords()[0]).put("quantity", (Object)Integer.parseInt(this.task.getSize())).toBuffer();
                    CompletableFuture completableFuture = Request.send(httpRequest, buffer);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture3 = completableFuture;
                        return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$cartFast(this, n, n2, string, httpRequest, completableFuture3, buffer, null, 0, null, 2, arg_0));
                    }
                    httpResponse = (HttpResponse)completableFuture.join();
                } else {
                    this.logger.warn("Validating session");
                    n = 2;
                    httpRequest = this.api.transferCart(string);
                    CompletableFuture completableFuture = Request.send(httpRequest, new JsonObject());
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$cartFast(this, n, n2, string, httpRequest, completableFuture4, null, null, 0, null, 3, arg_0));
                    }
                    httpResponse = (HttpResponse)completableFuture.join();
                }
                if (httpResponse == null) continue;
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Fast cart step '{}' responded[{}]: {}", (Object)n, (Object)httpResponse.statusCode(), (Object)httpResponse.bodyAsString());
                }
                if (httpResponse.statusCode() == 200) {
                    if (n == 1) {
                        httpRequest = httpResponse.bodyAsJsonObject().getJsonArray("savedItems").getJsonObject(0);
                        string = httpRequest.getString("id", null);
                        continue;
                    }
                    if (n != 2) continue;
                    this.logger.info("Session successfully initialised!");
                    httpRequest = httpResponse.bodyAsJsonObject();
                    buffer = httpRequest.getJsonObject("cart");
                    if (!buffer.containsKey("id")) return CompletableFuture.completedFuture(1);
                    if (buffer.getInteger("itemCount", Integer.valueOf(0)) < 1) return CompletableFuture.completedFuture(1);
                    if (httpRequest.getBoolean("checkoutable", Boolean.valueOf(false)) == false) return CompletableFuture.completedFuture(2);
                    return CompletableFuture.completedFuture(3);
                }
                if (httpResponse.statusCode() != 412) {
                    this.bannedBefore = false;
                    CompletableFuture completableFuture = this.handleCRTSignalSleep(this.api);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture5 = completableFuture;
                        return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$cartFast(this, n, n2, string, null, completableFuture5, null, httpResponse, 0, null, 4, arg_0));
                    }
                    if (((Boolean)completableFuture.join()).booleanValue()) {
                        return CompletableFuture.completedFuture(null);
                    }
                } else if (n2 % 10 == 0 && this.mode == Mode.DESKTOP && this.task.getMode().contains("skip")) {
                    this.api.getPxAPI().reset();
                }
                CompletableFuture completableFuture = this.api.handleBadResponse(httpResponse.statusCode(), httpResponse);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$cartFast(this, n, n2, string, null, completableFuture6, null, httpResponse, 0, null, 5, arg_0));
                }
                int n3 = ((Boolean)completableFuture.join()).booleanValue() ? 1 : 0;
                if (n3 == 0 || this.bannedBefore) {
                    CompletableFuture completableFuture7 = this.handleCRTSignalSleep(this.api);
                    if (!completableFuture7.isDone()) {
                        CompletableFuture completableFuture8 = completableFuture7;
                        return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$cartFast(this, n, n2, string, null, completableFuture8, null, httpResponse, n3, null, 6, arg_0));
                    }
                    if (((Boolean)completableFuture7.join()).booleanValue()) {
                        return CompletableFuture.completedFuture(null);
                    }
                }
                if (httpResponse.statusCode() != 412) continue;
                this.bannedBefore = true;
            }
            catch (Throwable throwable) {
                this.logger.error("Error occurred creating session: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = super.randomSleep(12000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture9 = completableFuture;
                    return ((CompletableFuture)completableFuture9.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$cartFast(this, n, n2, string, null, completableFuture9, null, null, 0, throwable, 7, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(-1);
    }

    public void lambda$run$1(Long l) {
        Request.execute(this.api.getWebClient().getAbs("https://www.walmart.com/favicon.ico"));
    }

    public CompletableFuture atcNormal() {
        this.logger.info("Attempting add to cart");
        Buffer buffer = this.api.atcForm().toBuffer();
        int n = 0;
        while (this.running) {
            if (n > 500) return CompletableFuture.failedFuture(new Exception());
            ++n;
            try {
                HttpRequest httpRequest = this.api.addToCart().as(BodyCodec.deferred());
                CompletableFuture completableFuture = Request.send(httpRequest, buffer);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$atcNormal(this, buffer, n, httpRequest, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 201 || httpResponse.statusCode() == 200 || httpResponse.statusCode() == 206) {
                    VertxUtil.sendSignal(this.instanceSignal, this.api.cookieStore().getCookieValue("CRT"));
                    this.logger.info("Successfully added to cart: status:'{}'", (Object)httpResponse.statusCode());
                    return CompletableFuture.completedFuture(null);
                }
                this.logger.warn("Waiting for restock: status:'{}'", (Object)httpResponse.statusCode());
                this.fetchGlobalCookies();
                if (httpResponse.statusCode() != 412) {
                    this.bannedBefore = false;
                    CompletableFuture completableFuture3 = this.handleCRTSignalSleep(this.api);
                    if (!completableFuture3.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture3;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$atcNormal(this, buffer, n, httpRequest, completableFuture4, httpResponse, null, 2, arg_0));
                    }
                    if (((Boolean)completableFuture3.join()).booleanValue()) {
                        return CompletableFuture.completedFuture(null);
                    }
                } else if (n % 10 == 0 && this.mode == Mode.DESKTOP && this.task.getMode().contains("skip")) {
                    this.api.getPxAPI().reset();
                }
                CompletableFuture completableFuture5 = httpResponse.deferredBody().toCompletionStage().toCompletableFuture();
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$atcNormal(this, buffer, n, httpRequest, completableFuture6, httpResponse, null, 3, arg_0));
                }
                completableFuture5.join();
                CompletableFuture completableFuture7 = this.api.handleBadResponse(httpResponse.statusCode(), httpResponse);
                if (!completableFuture7.isDone()) {
                    CompletableFuture completableFuture8 = completableFuture7;
                    return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$atcNormal(this, buffer, n, httpRequest, completableFuture8, httpResponse, null, 4, arg_0));
                }
                boolean bl = (Boolean)completableFuture7.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error occurred adding to cart: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = super.randomSleep(12000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture9 = completableFuture;
                    return ((CompletableFuture)completableFuture9.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$atcNormal(this, buffer, n, null, completableFuture9, null, throwable, 5, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.failedFuture(new Exception());
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$handleCRTSignalSleep(Walmart var0, API var1_1, CompletableFuture var2_2, int var3_3, Object var4_5) {
        switch (var3_3) {
            case 0: {
                v0 = VertxUtil.randomSignalSleep(var0.instanceSignal, var0.task.getMonitorDelay());
                if (!v0.isDone()) {
                    var3_4 = v0;
                    return var3_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$handleCRTSignalSleep(io.trickle.task.sites.walmart.usa.Walmart io.trickle.task.sites.walmart.usa.API java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Walmart)var0, (API)var1_1, (CompletableFuture)var3_4, (int)1));
                }
                ** GOTO lbl10
            }
            case 1: {
                v0 = var2_2;
lbl10:
                // 2 sources

                var2_2 = (String)v0.join();
                if (var0.shared == false) return CompletableFuture.completedFuture(false);
                if (var2_2 == null) return CompletableFuture.completedFuture(false);
                if (var2_2.isEmpty() != false) return CompletableFuture.completedFuture(false);
                var1_1.cookieStore().put("CRT", (String)var2_2, ".walmart.com");
                return CompletableFuture.completedFuture(true);
            }
        }
        throw new IllegalArgumentException();
    }

    public Walmart(Task task, int n) {
        super(n);
        this.task = task;
        this.mode = Mode.getMode(this.task.getMode());
        this.api = this.mode.equals((Object)Mode.DESKTOP) ? new WalmartAPIDesktop(this.task) : new WalmartAPI(this.task);
        super.setClient(this.api);
        this.api.getWebClient().cookieStore().setCookieFilter(Walmart::lambda$new$0);
        this.instanceSignal = this.itemKeyword = this.task.getKeywords()[0];
        this.shared = this.task.getMode().toLowerCase().contains("sync");
    }

    public void lambda$fetchGlobalCookies$3(Cookie cookie) {
        if (this.sessionCookies.contains(cookie.name())) return;
        this.sessionCookies.put(cookie);
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$run(Walmart var0, CompletableFuture var1_1, int var2_4, int var3_7, Object var4_16) {
        switch (var3_7) {
            case 0: {
                if (var0.mode.equals((Object)Mode.DESKTOP)) {
                    var0.api.setAPI(PerimeterX.createDesktopAPI(var0));
                } else {
                    var0.api.setAPI(PerimeterX.createMobile(var0));
                }
                if (!(v0 = var0.generateToken()).isDone()) {
                    var3_8 = v0;
                    return var3_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.walmart.usa.Walmart java.util.concurrent.CompletableFuture int int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Walmart)var0, (CompletableFuture)var3_8, (int)0, (int)1));
                }
lbl10:
                // 3 sources

                while (true) {
                    v0.join();
                    v1 = var0.api.initialisePX();
                    if (!v1.isDone()) {
                        var3_9 = v1;
                        return var3_9.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.walmart.usa.Walmart java.util.concurrent.CompletableFuture int int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Walmart)var0, (CompletableFuture)var3_9, (int)0, (int)2));
                    }
lbl17:
                    // 3 sources

                    while (true) {
                        var1_2 = (int)((Boolean)v1.join()).booleanValue();
                        if (var1_2 == 0) {
                            var0.logger.warn("Failed to initialise and configure task. Stopping...");
                            return CompletableFuture.completedFuture(null);
                        }
                        try {
                            if (var0.mode == Mode.DESKTOP) {
                                v2 = var0.fetchHomepage(true);
                                if (!v2.isDone()) {
                                    var3_10 = v2;
                                    return var3_10.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.walmart.usa.Walmart java.util.concurrent.CompletableFuture int int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Walmart)var0, (CompletableFuture)var3_10, (int)var1_2, (int)3));
                                }
lbl28:
                                // 3 sources

                                while (true) {
                                    v2.join();
                                    v3 = var0.api.generatePX(false);
                                    if (!v3.isDone()) {
                                        var3_11 = v3;
                                        return var3_11.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.walmart.usa.Walmart java.util.concurrent.CompletableFuture int int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Walmart)var0, (CompletableFuture)var3_11, (int)var1_2, (int)4));
                                    }
lbl35:
                                    // 3 sources

                                    while (true) {
                                        v3.join();
                                        ** GOTO lbl42
                                        break;
                                    }
                                    break;
                                }
                            }
                        }
                        catch (Throwable var2_5) {
                            // empty catch block
                        }
lbl42:
                        // 3 sources

                        if (!(v4 = var0.updateOrCreateSession()).isDone()) {
                            var3_12 = v4;
                            return var3_12.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.walmart.usa.Walmart java.util.concurrent.CompletableFuture int int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Walmart)var0, (CompletableFuture)var3_12, (int)var1_2, (int)5));
                        }
lbl45:
                        // 3 sources

                        while (true) {
                            v4.join();
                            if (!var0.task.getMode().toLowerCase().contains("fast")) ** GOTO lbl60
                            v5 = PaymentInstance.preload(var0, var0.task, var0.token, var0.mode);
                            if (!v5.isDone()) {
                                var3_13 = v5;
                                return var3_13.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.walmart.usa.Walmart java.util.concurrent.CompletableFuture int int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Walmart)var0, (CompletableFuture)var3_13, (int)var1_2, (int)6));
                            }
lbl53:
                            // 3 sources

                            while (true) {
                                var2_6 = (String)v5.join();
                                var0.token.setPiHash(var2_6);
                                var0.api.getWebClient().cookieStore().clear();
                                if (var0.task.getMode().toLowerCase().contains("grief")) {
                                    var0.api.swapClient();
                                }
lbl60:
                                // 4 sources

                                if (var0.keepAliveWorker == 0L) {
                                    var0.keepAliveWorker = var0.vertx.setPeriodic(TimeUnit.SECONDS.toMillis(100L), (Handler)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)V, lambda$run$1(java.lang.Long ), (Ljava/lang/Long;)V)((Walmart)var0));
                                }
                                if (!(v6 = var0.sleep(ThreadLocalRandom.current().nextInt(300, 1500))).isDone()) {
                                    var3_14 = v6;
                                    return var3_14.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.walmart.usa.Walmart java.util.concurrent.CompletableFuture int int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Walmart)var0, (CompletableFuture)var3_14, (int)var1_2, (int)7));
                                }
lbl65:
                                // 3 sources

                                while (true) {
                                    v6.join();
                                    v7 = var0.tryCart();
                                    if (!v7.isDone()) {
                                        var3_15 = v7;
                                        return var3_15.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.walmart.usa.Walmart java.util.concurrent.CompletableFuture int int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Walmart)var0, (CompletableFuture)var3_15, (int)var1_2, (int)8));
                                    }
                                    ** GOTO lbl102
                                    break;
                                }
                                break;
                            }
                            break;
                        }
                        break;
                    }
                    break;
                }
            }
            case 1: {
                v0 = var1_1;
                ** continue;
            }
            case 2: {
                v1 = var1_1;
                ** continue;
            }
            case 3: {
                v2 = var1_1;
                var1_2 = var2_4;
                ** continue;
            }
            case 4: {
                v3 = var1_1;
                var1_2 = var2_4;
                ** continue;
            }
            case 5: {
                v4 = var1_1;
                var1_2 = var2_4;
                ** continue;
            }
            case 6: {
                v5 = var1_1;
                var1_2 = var2_4;
                ** continue;
            }
            case 7: {
                v6 = var1_1;
                var1_2 = var2_4;
                ** continue;
            }
            case 8: {
                v7 = var1_1;
                var1_3 = var2_4;
lbl102:
                // 2 sources

                v7.join();
                if (var0.keepAliveWorker == 0L) return CompletableFuture.completedFuture(null);
                var0.vertx.cancelTimer(var0.keepAliveWorker);
                return CompletableFuture.completedFuture(null);
            }
        }
        throw new IllegalArgumentException();
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$tryCart(Walmart var0, int var1_1, int var2_2, int var3_3, CompletableFuture var4_4, int var5_10, Throwable var6_13, int var7_15, Object var8_26) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [7[CASE], 28[UNCONDITIONALDOLOOP], 29[UNCONDITIONALDOLOOP], 27[UNCONDITIONALDOLOOP], 26[UNCONDITIONALDOLOOP]], but top level block is 1[TRYBLOCK]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:845)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1042)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:929)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:73)
         *     at org.benf.cfr.reader.Main.main(Main.java:49)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:303)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$null$5(ResourceDecompiling.java:158)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$updateOrCreateSession(Walmart var0, CompletableFuture var1_1, int var2_3, Object var3_6) {
        switch (var2_3) {
            case 0: {
                try {
                    var0.sessionCookies.clear();
                    if (!var0.mode.equals((Object)Mode.MOBILE)) ** GOTO lbl15
                    v0 = SessionPreload.createSession(var0);
                    if (!v0.isDone()) {
                        var2_4 = v0;
                        return var2_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$updateOrCreateSession(io.trickle.task.sites.walmart.usa.Walmart java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Walmart)var0, (CompletableFuture)var2_4, (int)1));
                    }
lbl11:
                    // 3 sources

                    while (true) {
                        v0.join();
                        ** GOTO lbl22
                        break;
                    }
lbl15:
                    // 1 sources

                    v1 = Request.executeTillOk(var0.api.getCheckoutPage());
                    if (!v1.isDone()) {
                        var2_5 = v1;
                        return var2_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$updateOrCreateSession(io.trickle.task.sites.walmart.usa.Walmart java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Walmart)var0, (CompletableFuture)var2_5, (int)2));
                    }
lbl19:
                    // 3 sources

                    while (true) {
                        v1.join();
lbl22:
                        // 2 sources

                        var0.sessionTimestamp = System.currentTimeMillis();
                        var0.sessionCookies = new CookieJar((CookieJar)var0.api.getWebClient().cookieStore());
                        var0.sessionCookies.get(true, ".walmart.com", "/").forEach((Consumer<Cookie>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)V, lambda$updateOrCreateSession$2(io.netty.handler.codec.http.cookie.Cookie ), (Lio/netty/handler/codec/http/cookie/Cookie;)V)());
                        var0.fetchGlobalCookies();
                        var0.api.getWebClient().cookieStore().putFromOther(var0.sessionCookies);
                        return CompletableFuture.completedFuture(null);
                    }
                }
                catch (Throwable var1_2) {
                    var0.logger.error("Error occurred updating session: {}", (Object)var1_2.getMessage());
                }
                return CompletableFuture.completedFuture(null);
            }
            case 1: {
                v0 = var1_1;
                ** continue;
            }
            case 2: {
                v1 = var1_1;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$checkStockTerra(Walmart var0, String var1_1, Buffer var2_2, int var3_3, HttpRequest var4_4, CompletableFuture var5_5, HttpResponse var6_7, Throwable var7_8, int var8_9, Object var9_11) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [9[CATCHBLOCK]], but top level block is 14[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:845)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1042)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:929)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:73)
         *     at org.benf.cfr.reader.Main.main(Main.java:49)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:303)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$null$5(ResourceDecompiling.java:158)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    public static CompletableFuture async$fetchHomepage(Walmart var0, int var1_1, int var2_2, HttpRequest var3_3, CompletableFuture var4_5, HttpResponse var5_6, Throwable var6_8, int var7_9, Object var8_10) {
        switch (var7_9) {
            case 0: {
                var0.logger.info("Checking homepage");
                var2_2 = 0;
                block11: while (var0.running != false) {
                    if (var2_2++ >= 100) return CompletableFuture.failedFuture(new Exception());
                    try {
                        var3_3 /* !! */  = var0.api.homepage();
                        v0 = Request.send(var3_3 /* !! */ );
                        if (!v0.isDone()) {
                            var6_8 = v0;
                            return var6_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$fetchHomepage(io.trickle.task.sites.walmart.usa.Walmart int int io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Walmart)var0, (int)var1_1, (int)var2_2, (HttpRequest)var3_3 /* !! */ , (CompletableFuture)var6_8, null, null, (int)1));
                        }
lbl13:
                        // 3 sources

                        while (true) {
                            var4_5 = (HttpResponse)v0.join();
                            if (var4_5 == null) continue block11;
                            if (var4_5.statusCode() == 307) {
                                var0.logger.info("Blocked while visiting homepage. Handling...");
                                v1 = var0.api.handleBadResponse(var4_5.statusCode(), (HttpResponse)var4_5);
                                if (!v1.isDone()) {
                                    var6_8 = v1;
                                    return var6_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$fetchHomepage(io.trickle.task.sites.walmart.usa.Walmart int int io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Walmart)var0, (int)var1_1, (int)var2_2, (HttpRequest)var3_3 /* !! */ , (CompletableFuture)var6_8, (HttpResponse)var4_5, null, (int)2));
                                }
lbl22:
                                // 3 sources

                                while (true) {
                                    v1.join();
                                    continue block11;
                                    break;
                                }
                            }
                            if (var4_5.statusCode() != 200) {
                                var0.logger.warn("Failed visiting homepage: status:'{}'", (Object)var4_5.statusCode());
                                v2 = VertxUtil.randomSleep(var0.task.getMonitorDelay());
                                if (!v2.isDone()) {
                                    var6_8 = v2;
                                    return var6_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$fetchHomepage(io.trickle.task.sites.walmart.usa.Walmart int int io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Walmart)var0, (int)var1_1, (int)var2_2, (HttpRequest)var3_3 /* !! */ , (CompletableFuture)var6_8, (HttpResponse)var4_5, null, (int)3));
                                }
lbl32:
                                // 3 sources

                                while (true) {
                                    v2.join();
                                    continue block11;
                                    break;
                                }
                            }
                            var0.api.cookieStore().put("viq", "walmart", "www.walmart.com");
                            var0.api.cookieStore().put("cart-item-count", "0", "www.walmart.com");
                            var0.api.cookieStore().put("_uetsid", Utils.genUet(), "www.walmart.com");
                            var0.api.cookieStore().put("_uetvid", Utils.genUet(), "www.walmart.com");
                            var0.api.cookieStore().put("s_sess_2", "prop32%3D", "www.walmart.com");
                            var0.api.cookieStore().put("TBV", "7", "www.walmart.com");
                            var0.api.cookieStore().put("TB_DC_Flap_Test", "0", "www.walmart.com");
                            var0.api.cookieStore().put("TB_SFOU-100", "", "www.walmart.com");
                            try {
                                var0.api.cookieStore().put("athrvi", "RVI~h" + Integer.parseInt(var0.task.getKeywords()[0], 16), "www.walmart.com");
                            }
                            catch (Exception var5_7) {
                                // empty catch block
                            }
                            var0.api.cookieStore().put("_gcl_au", "1.1.1957667684." + Instant.now().getEpochSecond(), "www.walmart.com");
                            return CompletableFuture.completedFuture(null);
                        }
                    }
                    catch (Throwable var3_4) {
                        var0.logger.error("Error occurred fetching homepage: {}", (Object)var3_4.getMessage());
                        v3 = super.randomSleep(12000);
                        if (!v3.isDone()) {
                            var6_8 = v3;
                            return var6_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$fetchHomepage(io.trickle.task.sites.walmart.usa.Walmart int int io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Walmart)var0, (int)var1_1, (int)var2_2, null, (CompletableFuture)var6_8, null, (Throwable)var3_4, (int)4));
                        }
lbl67:
                        // 3 sources

                        while (true) {
                            v3.join();
                            continue block11;
                            break;
                        }
                    }
                }
                return CompletableFuture.failedFuture(new Exception());
            }
            case 1: {
                v0 = var4_5;
                ** continue;
            }
            case 2: {
                v1 = var4_5;
                var4_5 = var5_6;
                ** continue;
            }
            case 3: {
                v2 = var4_5;
                var4_5 = var5_6;
                ** continue;
            }
            case 4: {
                v3 = var4_5;
                var3_3 /* !! */  = var6_8;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$createAccount(Walmart var0, int var1_1, int var2_2, JsonObject var3_3, HttpRequest var4_5, CompletableFuture var5_6, HttpResponse var6_7, int var7_9, Throwable var8_14, int var9_15, Object var10_16) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [8[CATCHBLOCK]], but top level block is 13[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:845)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1042)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:929)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:73)
         *     at org.benf.cfr.reader.Main.main(Main.java:49)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:303)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$null$5(ResourceDecompiling.java:158)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Enabled unnecessary exception pruning
     */
    public CompletableFuture checkStockCart(String string) {
        int n = 0;
        while (n < 5) {
            block16: {
                HttpRequest httpRequest = this.api.getCartV3(string);
                try {
                    JsonArray jsonArray;
                    CompletableFuture completableFuture = Request.send(httpRequest);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture2 = completableFuture;
                        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$checkStockCart(this, string, n, httpRequest, completableFuture2, null, null, 1, arg_0));
                    }
                    HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                    if (httpResponse == null) break block16;
                    if (httpResponse.statusCode() == 201 || httpResponse.statusCode() == 200 || httpResponse.statusCode() == 206) {
                        JsonObject jsonObject = httpResponse.bodyAsJsonObject();
                        if (jsonObject.getBoolean("checkoutable", Boolean.valueOf(false)).booleanValue()) {
                            VertxUtil.sendSignal(this.instanceSignal, this.api.getWebClient().cookieStore().getCookieValue("CRT"));
                            return CompletableFuture.completedFuture(true);
                        }
                        if (jsonObject.containsKey("items")) {
                            jsonArray = jsonObject.getJsonArray("items", null);
                            if (jsonArray != null && jsonArray.size() > 0) {
                                break block17;
                            } else {
                                this.api.getWebClient().cookieStore().removeAnyMatch("hasCRT");
                                this.api.getWebClient().cookieStore().removeAnyMatch("CRT");
                                return CompletableFuture.failedFuture(new EmptyCartException());
                            }
                        }
                    } else {
                        block17: {
                            if (httpResponse.statusCode() == 405) {
                                return CompletableFuture.failedFuture(new EmptyCartException());
                            }
                            this.logger.warn("Waiting for restock (s): status:'{}'", (Object)httpResponse.statusCode());
                            CompletableFuture completableFuture3 = this.api.handleBadResponse(httpResponse.statusCode(), httpResponse);
                            if (!completableFuture3.isDone()) {
                                CompletableFuture completableFuture4 = completableFuture3;
                                return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$checkStockCart(this, string, n, httpRequest, completableFuture4, httpResponse, null, 2, arg_0));
                            }
                            completableFuture3.join();
                            return CompletableFuture.completedFuture(false);
                        }
                        for (int i = 0; i < jsonArray.size(); ++i) {
                            try {
                                JsonObject jsonObject = jsonArray.getJsonObject(i);
                                if (!jsonObject.getString("offerId", "").equalsIgnoreCase(this.itemKeyword)) continue;
                                this.productID = jsonObject.getString("USItemId", "");
                                break;
                            }
                            catch (Throwable throwable) {
                                // empty catch block
                            }
                        }
                    }
                    this.logger.info("Waiting for restock (s)");
                    return CompletableFuture.completedFuture(false);
                }
                catch (Throwable throwable) {
                    this.logger.error("Error occurred waiting for restock (s): {}", (Object)throwable.getMessage());
                    CompletableFuture completableFuture = super.randomSleep(12000);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture5 = completableFuture;
                        return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$checkStockCart(this, string, n, httpRequest, completableFuture5, null, throwable, 3, arg_0));
                    }
                    completableFuture.join();
                }
            }
            ++n;
        }
        return CompletableFuture.completedFuture(false);
    }

    public CompletableFuture generateToken() {
        HttpRequest httpRequest = VertxSingleton.INSTANCE.getLocalClient().getClient().getAbs("https://securedataweb.walmart.com/pie/v1/wmcom_us_vtg_pie/getkey.js?bust=" + System.currentTimeMillis()).timeout(TimeUnit.SECONDS.toMillis(15L)).as(BodyCodec.string());
        try {
            CompletableFuture completableFuture = Request.execute(httpRequest, 5);
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$generateToken(this, httpRequest, completableFuture2, 1, arg_0));
            }
            String string = (String)completableFuture.join();
            this.token = PaymentToken.prepareAndGenerate(string, this.task.getProfile().getCardNumber(), this.task.getProfile().getCvv());
            this.token.set4111Encrypted(PaymentToken.prepareAndGenerate(string, "4111111111111111", this.task.getProfile().getCvv()).getEncryptedPan());
            return CompletableFuture.completedFuture(null);
        }
        catch (Exception exception) {
            this.logger.warn("Failed to generate payment token: {}", (Object)exception.getMessage());
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Unable to fully structure code
     */
    public CompletableFuture tryCart() {
        var1_1 = 0;
        var2_2 = 0;
        var3_3 = 0;
        try {
            Integer.parseInt(this.itemKeyword);
            var2_2 = 1;
        }
        catch (Exception var4_4) {
            // empty catch block
        }
        if (this.task.getMode().contains("login")) {
            v0 = this.createAccount(false);
            if (!v0.isDone()) {
                var7_9 = v0;
                return var7_9.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$tryCart(io.trickle.task.sites.walmart.usa.Walmart int int int java.util.concurrent.CompletableFuture int java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Walmart)this, (int)var1_1, (int)var2_2, (int)var3_3, (CompletableFuture)var7_9, (int)0, null, (int)1));
            }
            v0.join();
        } else if (this.task.getMode().contains("account")) {
            v1 = this.loginAccount();
            if (!v1.isDone()) {
                var7_10 = v1;
                return var7_10.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$tryCart(io.trickle.task.sites.walmart.usa.Walmart int int int java.util.concurrent.CompletableFuture int java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Walmart)this, (int)var1_1, (int)var2_2, (int)var3_3, (CompletableFuture)var7_10, (int)0, null, (int)2));
            }
            var4_5 = (Integer)v1.join();
            if (var4_5 < 1) {
                this.logger.info("No accounts available in storage. Creating new...");
                v2 = this.createAccount(true);
                if (!v2.isDone()) {
                    var7_11 = v2;
                    return var7_11.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$tryCart(io.trickle.task.sites.walmart.usa.Walmart int int int java.util.concurrent.CompletableFuture int java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Walmart)this, (int)var1_1, (int)var2_2, (int)var3_3, (CompletableFuture)var7_11, (int)var4_5, null, (int)3));
                }
                v2.join();
            }
        }
        while (this.running != false) {
            block32: {
                if (System.currentTimeMillis() - this.sessionTimestamp >= (long)ThreadLocalRandom.current().nextInt(120000, 300000)) {
                    v3 = this.updateOrCreateSession();
                    if (!v3.isDone()) {
                        var7_12 = v3;
                        return var7_12.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$tryCart(io.trickle.task.sites.walmart.usa.Walmart int int int java.util.concurrent.CompletableFuture int java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Walmart)this, (int)var1_1, (int)var2_2, (int)var3_3, (CompletableFuture)var7_12, (int)0, null, (int)4));
                    }
                    v3.join();
                }
                if (var1_1 == 0) {
                    try {
                        v4 = var2_2 != 0 ? this.atcAffiliate() : this.atcNormal();
                        if (!v4.isDone()) {
                            var7_13 = v4;
                            return var7_13.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$tryCart(io.trickle.task.sites.walmart.usa.Walmart int int int java.util.concurrent.CompletableFuture int java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Walmart)this, (int)var1_1, (int)var2_2, (int)var3_3, (CompletableFuture)var7_13, (int)0, null, (int)5));
                        }
                        v4.join();
                        break block32;
                    }
                    catch (Exception var4_6) {
                        continue;
                    }
                }
                if (var3_3 != 0) {
                    try {
                        v5 = this.waitForRestock();
                        if (!v5.isDone()) {
                            var7_14 = v5;
                            return var7_14.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$tryCart(io.trickle.task.sites.walmart.usa.Walmart int int int java.util.concurrent.CompletableFuture int java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Walmart)this, (int)var1_1, (int)var2_2, (int)var3_3, (CompletableFuture)var7_14, (int)0, null, (int)6));
                        }
                        if (!((Boolean)v5.join()).booleanValue()) {
                        }
                        break block32;
                    }
                    catch (Throwable var4_7) {
                        var1_1 = 0;
                        var3_3 = 0;
                        this.fetchGlobalCookies();
                    }
                    continue;
                }
            }
            try {
                Analytics.carts.incrementAndGet();
                v6 = this.tryCheckout();
                if (!v6.isDone()) {
                    var7_15 = v6;
                    return var7_15.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$tryCart(io.trickle.task.sites.walmart.usa.Walmart int int int java.util.concurrent.CompletableFuture int java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Walmart)this, (int)var1_1, (int)var2_2, (int)var3_3, (CompletableFuture)var7_15, (int)0, null, (int)7));
                }
                v6.join();
                ** GOTO lbl104
            }
            catch (Exception var4_8) {
                try {
                    if (var4_8.getMessage().contains("exceeds limit") || var4_8.getMessage().contains("PCID") || var4_8.getMessage().contains("405")) {
                        var1_1 = 0;
                        var3_3 = 0;
                        this.fetchGlobalCookies();
                    }
                    var1_1 = 1;
                    if (!this.api.getWebClient().cookieStore().contains("CRT")) ** GOTO lbl112
                    try {
                        var5_19 = this.api.getCookies().getCookieValue("CRT");
                        this.api.getCookies().put("CRT", var5_19, ".walmart.com");
                    }
                    catch (Exception var5_20) {
                        var5_20.printStackTrace();
                    }
                    var3_3 = 1;
                }
                catch (Throwable var6_21) {
                    this.logger.warn("Retrying in {}ms", (Object)this.task.getRetryDelay());
                    v7 = super.sleep(this.task.getRetryDelay());
                    if (!v7.isDone()) {
                        var7_18 = v7;
                        return var7_18.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$tryCart(io.trickle.task.sites.walmart.usa.Walmart int int int java.util.concurrent.CompletableFuture int java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Walmart)this, (int)var1_1, (int)var2_2, (int)var3_3, (CompletableFuture)var7_18, (int)0, (Throwable)var6_21, (int)10));
                    }
                    v7.join();
                    throw var6_21;
                }
lbl104:
                // 1 sources

                this.logger.warn("Retrying in {}ms", (Object)this.task.getRetryDelay());
                v8 = super.sleep(this.task.getRetryDelay());
                if (!v8.isDone()) {
                    var7_16 = v8;
                    return var7_16.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$tryCart(io.trickle.task.sites.walmart.usa.Walmart int int int java.util.concurrent.CompletableFuture int java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Walmart)this, (int)var1_1, (int)var2_2, (int)var3_3, (CompletableFuture)var7_16, (int)0, null, (int)8));
                }
                v8.join();
                return CompletableFuture.completedFuture(null);
lbl112:
                // 3 sources

                this.logger.warn("Retrying in {}ms", (Object)this.task.getRetryDelay());
                v9 = super.sleep(this.task.getRetryDelay());
                if (!v9.isDone()) {
                    var7_17 = v9;
                    return var7_17.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$tryCart(io.trickle.task.sites.walmart.usa.Walmart int int int java.util.concurrent.CompletableFuture int java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Walmart)this, (int)var1_1, (int)var2_2, (int)var3_3, (CompletableFuture)var7_17, (int)0, null, (int)9));
                }
                v9.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public static boolean lambda$new$0(String string) {
        if (string.equalsIgnoreCase("g")) return false;
        return true;
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$loginAccount(Walmart var0, int var1_1, AccountController var2_2, CompletableFuture var3_3, Account var4_5, JsonObject var5_6, HttpRequest var6_7, HttpResponse var7_8, int var8_10, Throwable var9_16, int var10_17, Object var11_18) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [9[CATCHBLOCK]], but top level block is 15[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:845)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1042)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:929)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:73)
         *     at org.benf.cfr.reader.Main.main(Main.java:49)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:303)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$null$5(ResourceDecompiling.java:158)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$generateToken(Walmart var0, HttpRequest var1_1, CompletableFuture var2_2, int var3_4, Object var4_6) {
        switch (var3_4) {
            case 0: {
                var1_1 = VertxSingleton.INSTANCE.getLocalClient().getClient().getAbs("https://securedataweb.walmart.com/pie/v1/wmcom_us_vtg_pie/getkey.js?bust=" + System.currentTimeMillis()).timeout(TimeUnit.SECONDS.toMillis(15L)).as(BodyCodec.string());
                try {
                    v0 = Request.execute(var1_1, 5);
                    if (!v0.isDone()) {
                        var3_5 = v0;
                        return var3_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$generateToken(io.trickle.task.sites.walmart.usa.Walmart io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Walmart)var0, (HttpRequest)var1_1, (CompletableFuture)var3_5, (int)1));
                    }
                    ** GOTO lbl15
                }
                catch (Exception var2_3) {
                    var0.logger.warn("Failed to generate payment token: {}", (Object)var2_3.getMessage());
                }
                return CompletableFuture.completedFuture(null);
            }
            case 1: {
                v0 = var2_2;
lbl15:
                // 2 sources

                var2_2 = (String)v0.join();
                var0.token = PaymentToken.prepareAndGenerate((String)var2_2, var0.task.getProfile().getCardNumber(), var0.task.getProfile().getCvv());
                var0.token.set4111Encrypted(PaymentToken.prepareAndGenerate((String)var2_2, "4111111111111111", var0.task.getProfile().getCvv()).getEncryptedPan());
                return CompletableFuture.completedFuture(null);
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture updateOrCreateSession() {
        try {
            this.sessionCookies.clear();
            if (this.mode.equals((Object)Mode.MOBILE)) {
                CompletableFuture completableFuture = SessionPreload.createSession(this);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$updateOrCreateSession(this, completableFuture2, 1, arg_0));
                }
                completableFuture.join();
            } else {
                CompletableFuture completableFuture = Request.executeTillOk(this.api.getCheckoutPage());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture3 = completableFuture;
                    return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$updateOrCreateSession(this, completableFuture3, 2, arg_0));
                }
                completableFuture.join();
            }
            this.sessionTimestamp = System.currentTimeMillis();
            this.sessionCookies = new CookieJar((CookieJar)this.api.getWebClient().cookieStore());
            this.sessionCookies.get(true, ".walmart.com", "/").forEach(Walmart::lambda$updateOrCreateSession$2);
            this.fetchGlobalCookies();
            this.api.getWebClient().cookieStore().putFromOther(this.sessionCookies);
            return CompletableFuture.completedFuture(null);
        }
        catch (Throwable throwable) {
            this.logger.error("Error occurred updating session: {}", (Object)throwable.getMessage());
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture createAccount(boolean bl) {
        this.logger.info("Creating account");
        int n = 0;
        while (this.running) {
            if (n > 500) return CompletableFuture.failedFuture(new Exception());
            ++n;
            try {
                JsonObject jsonObject = this.api.accountCreateForm();
                HttpRequest httpRequest = this.api.createAccount();
                CompletableFuture completableFuture = Request.send(httpRequest, jsonObject);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$createAccount(this, (int)(bl ? 1 : 0), n, jsonObject, httpRequest, completableFuture2, null, 0, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 201 || httpResponse.statusCode() == 200 || httpResponse.statusCode() == 206) {
                    if (bl) {
                        this.vertx.eventBus().send("accounts.writer", (Object)(jsonObject.getString("email") + ":" + jsonObject.getString("password")));
                        if (this.logger.isDebugEnabled()) {
                            this.logger.debug("Account create responded with: [{}]{}", (Object)httpResponse.statusCode(), (Object)httpResponse.bodyAsString());
                        }
                    }
                    this.api.setLoggedIn(true);
                    this.task.getProfile().setAccountEmail(jsonObject.getString("email", null));
                    this.logger.info("Created account successfully!");
                    return CompletableFuture.completedFuture(null);
                }
                this.logger.warn("Creating account: status:'{}'", (Object)httpResponse.statusCode());
                if (httpResponse.statusCode() != 412) {
                    this.bannedBefore = false;
                } else if (n % 10 == 0 && this.mode == Mode.DESKTOP && this.task.getMode().contains("skip")) {
                    this.api.getPxAPI().reset();
                }
                CompletableFuture completableFuture3 = this.api.handleBadResponse(httpResponse.statusCode(), httpResponse);
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$createAccount(this, (int)(bl ? 1 : 0), n, jsonObject, httpRequest, completableFuture4, httpResponse, 0, null, 2, arg_0));
                }
                int n2 = ((Boolean)completableFuture3.join()).booleanValue() ? 1 : 0;
                if (n2 == 0 || this.bannedBefore) {
                    CompletableFuture completableFuture5 = VertxUtil.sleep(this.task.getRetryDelay());
                    if (!completableFuture5.isDone()) {
                        CompletableFuture completableFuture6 = completableFuture5;
                        return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$createAccount(this, (int)(bl ? 1 : 0), n, jsonObject, httpRequest, completableFuture6, httpResponse, n2, null, 3, arg_0));
                    }
                    completableFuture5.join();
                }
                if (httpResponse.statusCode() != 412) continue;
                this.bannedBefore = true;
            }
            catch (Throwable throwable) {
                this.logger.error("Error occurred creating account: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = super.randomSleep(12000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$createAccount(this, (int)(bl ? 1 : 0), n, null, null, completableFuture7, null, 0, throwable, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.failedFuture(new Exception());
    }

    public void fetchGlobalCookies() {
        GLOBAL_COOKIES.forEach(this::lambda$fetchGlobalCookies$3);
    }

    public CompletableFuture handleCRTSignalSleep(API aPI) {
        CompletableFuture completableFuture = VertxUtil.randomSignalSleep(this.instanceSignal, this.task.getMonitorDelay());
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$handleCRTSignalSleep(this, aPI, completableFuture2, 1, arg_0));
        }
        String string = (String)completableFuture.join();
        if (!this.shared) return CompletableFuture.completedFuture(false);
        if (string == null) return CompletableFuture.completedFuture(false);
        if (string.isEmpty()) return CompletableFuture.completedFuture(false);
        aPI.cookieStore().put("CRT", string, ".walmart.com");
        return CompletableFuture.completedFuture(true);
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$checkStockCart(Walmart var0, String var1_1, int var2_2, HttpRequest var3_3, CompletableFuture var4_4, HttpResponse var5_6, Throwable var6_7, int var7_8, Object var8_9) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [9[CATCHBLOCK]], but top level block is 13[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:845)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1042)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:929)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:73)
         *     at org.benf.cfr.reader.Main.main(Main.java:49)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:303)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$null$5(ResourceDecompiling.java:158)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public CompletableFuture tryCheckout() {
        try {
            CompletableFuture completableFuture = PaymentInstance.checkout(this, this.task, this.token, this.mode);
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$tryCheckout(this, completableFuture2, 1, arg_0));
            }
            if ((Integer)completableFuture.join() != -1) return CompletableFuture.completedFuture(null);
            return CompletableFuture.failedFuture(new Exception("Failed to process."));
        }
        catch (Exception exception) {
            this.logger.warn("Failed to checkout: {}", (Object)exception.getMessage());
            return CompletableFuture.failedFuture(exception);
        }
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$tryCheckout(Walmart var0, CompletableFuture var1_1, int var2_3, Object var3_5) {
        switch (var2_3) {
            case 0: {
                try {
                    v0 = PaymentInstance.checkout(var0, var0.task, var0.token, var0.mode);
                    if (!v0.isDone()) {
                        var2_4 = v0;
                        return var2_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$tryCheckout(io.trickle.task.sites.walmart.usa.Walmart java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Walmart)var0, (CompletableFuture)var2_4, (int)1));
                    }
                    ** GOTO lbl14
                }
                catch (Exception var1_2) {
                    var0.logger.warn("Failed to checkout: {}", (Object)var1_2.getMessage());
                    return CompletableFuture.failedFuture(var1_2);
                }
            }
            case 1: {
                v0 = var1_1;
lbl14:
                // 2 sources

                if ((Integer)v0.join() != -1) return CompletableFuture.completedFuture(null);
                return CompletableFuture.failedFuture(new Exception("Failed to process."));
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture loginAccount() {
        int n = 0;
        AccountController accountController = (AccountController)Engine.get().getModule(Controller.ACCOUNT);
        while (this.running && n <= 500) {
            ++n;
            try {
                CompletableFuture completableFuture = accountController.findAccount(this.task.getProfile().getEmail(), false).toCompletionStage().toCompletableFuture();
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$loginAccount(this, n, accountController, completableFuture2, null, null, null, null, 0, null, 1, arg_0));
                }
                Account account = (Account)completableFuture.join();
                if (account == null) {
                    this.logger.warn("No accounts available...");
                    return CompletableFuture.completedFuture(0);
                }
                this.logger.info("Logging in to account '{}'", (Object)account.getUser());
                JsonObject jsonObject = this.api.accountLoginForm(account);
                HttpRequest httpRequest = this.api.loginAccount();
                CompletableFuture completableFuture3 = Request.send(httpRequest, jsonObject);
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$loginAccount(this, n, accountController, completableFuture4, account, jsonObject, httpRequest, null, 0, null, 2, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture3.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 201 || httpResponse.statusCode() == 200 || httpResponse.statusCode() == 206) {
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug("Account login responded with: [{}]{}", (Object)httpResponse.statusCode(), (Object)httpResponse.bodyAsString());
                    }
                    this.logger.info("Logged in successfully to account '{}'", (Object)account.getUser());
                    this.api.setLoggedIn(true);
                    this.task.getProfile().setAccountEmail(account.getUser());
                    return CompletableFuture.completedFuture(1);
                }
                this.logger.warn("Account login: status:'{}'", (Object)httpResponse.statusCode());
                if (httpResponse.statusCode() != 412) {
                    this.bannedBefore = false;
                } else if (n % 10 == 0 && this.mode == Mode.DESKTOP && this.task.getMode().contains("skip")) {
                    this.api.getPxAPI().reset();
                }
                CompletableFuture completableFuture5 = this.api.handleBadResponse(httpResponse.statusCode(), httpResponse);
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$loginAccount(this, n, accountController, completableFuture6, account, jsonObject, httpRequest, httpResponse, 0, null, 3, arg_0));
                }
                int n2 = ((Boolean)completableFuture5.join()).booleanValue() ? 1 : 0;
                if (n2 == 0 || this.bannedBefore) {
                    CompletableFuture completableFuture7 = VertxUtil.sleep(this.task.getRetryDelay());
                    if (!completableFuture7.isDone()) {
                        CompletableFuture completableFuture8 = completableFuture7;
                        return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$loginAccount(this, n, accountController, completableFuture8, account, jsonObject, httpRequest, httpResponse, n2, null, 4, arg_0));
                    }
                    completableFuture7.join();
                }
                if (httpResponse.statusCode() != 412) continue;
                this.bannedBefore = true;
            }
            catch (Throwable throwable) {
                this.logger.error("Error occurred logging in account: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = super.randomSleep(12000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture9 = completableFuture;
                    return ((CompletableFuture)completableFuture9.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$loginAccount(this, n, accountController, completableFuture9, null, null, null, null, 0, throwable, 5, arg_0));
                }
                completableFuture.join();
            }
        }
        this.logger.warn("Failed to login to account. Max retries exceeded...");
        return CompletableFuture.completedFuture(-1);
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    public static CompletableFuture async$atcAffiliate(Walmart var0, String var1_1, int var2_2, HttpRequest var3_3, CompletableFuture var4_5, HttpResponse var5_6, Throwable var6_7, int var7_8, Object var8_9) {
        switch (var7_8) {
            case 0: {
                var1_1 = ThreadLocalRandom.current().nextBoolean() != false ? "addToCart" : "buynow";
                var0.logger.info("Attempting add to cart");
                var2_2 = 0;
                block9: while (var0.running != false) {
                    if (var2_2 > 50) return CompletableFuture.failedFuture(new Exception());
                    try {
                        ++var2_2;
                        var3_3 /* !! */  = var0.api.affilCrossSite(var1_1);
                        v0 = Request.send(var3_3 /* !! */ .as(BodyCodec.string()));
                        if (!v0.isDone()) {
                            var6_7 = v0;
                            return var6_7.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$atcAffiliate(io.trickle.task.sites.walmart.usa.Walmart java.lang.String int io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Walmart)var0, (String)var1_1, (int)var2_2, (HttpRequest)var3_3 /* !! */ , (CompletableFuture)var6_7, null, null, (int)1));
                        }
lbl15:
                        // 3 sources

                        while (true) {
                            var4_5 = (HttpResponse)v0.join();
                            if (var4_5 == null) continue block9;
                            if (var4_5.statusCode() == 302) {
                                var5_6 /* !! */  = var0.api.getWebClient().cookieStore().getCookieValue("CRT");
                                VertxUtil.sendSignal(var0.instanceSignal, (String)var5_6 /* !! */ );
                                var0.logger.info("Successfully added to cart: status:'{}'", (Object)var4_5.statusCode());
                                var0.api.getWebClient().cookieStore().putFromOther(var0.sessionCookies);
                                return CompletableFuture.completedFuture(null);
                            }
                            var0.logger.info("Waiting for restock [PID]: '{}'", (Object)var4_5.statusCode());
                            v1 = var0.api.handleBadResponse(var4_5.statusCode(), (HttpResponse)var4_5);
                            if (!v1.isDone()) {
                                var6_7 = v1;
                                return var6_7.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$atcAffiliate(io.trickle.task.sites.walmart.usa.Walmart java.lang.String int io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Walmart)var0, (String)var1_1, (int)var2_2, (HttpRequest)var3_3 /* !! */ , (CompletableFuture)var6_7, (HttpResponse)var4_5, null, (int)2));
                            }
lbl30:
                            // 3 sources

                            while (true) {
                                v1.join();
                                v2 = var0.handleCRTSignalSleep(var0.api);
                                if (!v2.isDone()) {
                                    var6_7 = v2;
                                    return var6_7.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$atcAffiliate(io.trickle.task.sites.walmart.usa.Walmart java.lang.String int io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Walmart)var0, (String)var1_1, (int)var2_2, (HttpRequest)var3_3 /* !! */ , (CompletableFuture)var6_7, (HttpResponse)var4_5, null, (int)3));
                                }
lbl37:
                                // 3 sources

                                while (true) {
                                    if (!((Boolean)v2.join()).booleanValue()) continue block9;
                                    return CompletableFuture.completedFuture(null);
                                }
                                break;
                            }
                            break;
                        }
                    }
                    catch (Throwable var3_4) {
                        var0.logger.error("Error occurred waiting for restock (s): {}", (Object)var3_4.getMessage());
                        v3 = super.randomSleep(12000);
                        if (!v3.isDone()) {
                            var6_7 = v3;
                            return var6_7.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$atcAffiliate(io.trickle.task.sites.walmart.usa.Walmart java.lang.String int io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Walmart)var0, (String)var1_1, (int)var2_2, null, (CompletableFuture)var6_7, null, (Throwable)var3_4, (int)4));
                        }
lbl46:
                        // 3 sources

                        while (true) {
                            v3.join();
                            continue block9;
                            break;
                        }
                    }
                }
                return CompletableFuture.failedFuture(new Exception());
            }
            case 1: {
                v0 = var4_5;
                ** continue;
            }
            case 2: {
                v1 = var4_5;
                var4_5 = var5_6 /* !! */ ;
                ** continue;
            }
            case 3: {
                v2 = var4_5;
                var4_5 = var5_6 /* !! */ ;
                ** continue;
            }
            case 4: {
                v3 = var4_5;
                var3_3 /* !! */  = var6_7;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture waitForRestock() {
        String string;
        int n = 0;
        try {
            string = this.api.getWebClient().cookieStore().getCookieValue("CRT");
        }
        catch (Exception exception) {
            string = "";
        }
        while (this.running) {
            if (n > 30) return CompletableFuture.completedFuture(false);
            ++n;
            try {
                if (n % 2 == 0 && !this.productID.isEmpty()) {
                    CompletableFuture completableFuture = this.checkStockTerra(this.productID);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture2 = completableFuture;
                        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$waitForRestock(this, n, string, completableFuture2, null, 1, arg_0));
                    }
                    if (((Boolean)completableFuture.join()).booleanValue()) {
                        return CompletableFuture.completedFuture(true);
                    }
                } else {
                    CompletableFuture completableFuture = this.checkStockCart(string);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture3 = completableFuture;
                        return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$waitForRestock(this, n, string, completableFuture3, null, 2, arg_0));
                    }
                    if (((Boolean)completableFuture.join()).booleanValue()) {
                        return CompletableFuture.completedFuture(true);
                    }
                }
                CompletableFuture completableFuture = VertxUtil.randomSignalSleep(this.instanceSignal, this.task.getMonitorDelay());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$waitForRestock(this, n, string, completableFuture4, null, 3, arg_0));
                }
                completableFuture.join();
            }
            catch (Throwable throwable) {
                if (throwable.getMessage().contains("CRT expired or empty")) {
                    return CompletableFuture.failedFuture(throwable);
                }
                this.logger.error("Error occurred waiting for restock: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = super.randomSleep(12000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture5 = completableFuture;
                    return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$waitForRestock(this, n, string, completableFuture5, throwable, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(false);
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$waitForRestock(Walmart var0, int var1_1, String var2_2, CompletableFuture var3_3, Throwable var4_6, int var5_7, Object var6_8) {
        switch (var5_7) {
            case 0: {
                var1_1 = 0;
                try {
                    var2_2 = var0.api.getWebClient().cookieStore().getCookieValue("CRT");
                }
                catch (Exception var3_4) {
                    var2_2 = "";
                }
                break;
            }
            case 1: {
                v0 = var3_3;
                ** GOTO lbl35
            }
            case 2: {
                v1 = var3_3;
                ** GOTO lbl43
            }
            case 3: {
                v2 = var3_3;
                ** GOTO lbl49
            }
            case 4: {
                v3 = var3_3;
                var3_3 = var4_6;
                ** GOTO lbl60
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
        while (var0.running != false) {
            if (var1_1 > 30) return CompletableFuture.completedFuture(false);
            ++var1_1;
            try {
                block21: {
                    block20: {
                        if (var1_1 % 2 == 0 && !var0.productID.isEmpty()) {
                            v0 = var0.checkStockTerra(var0.productID);
                            if (!v0.isDone()) {
                                var4_6 = v0;
                                return var4_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$waitForRestock(io.trickle.task.sites.walmart.usa.Walmart int java.lang.String java.util.concurrent.CompletableFuture java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Walmart)var0, (int)var1_1, (String)var2_2, (CompletableFuture)var4_6, null, (int)1));
                            } else {
                                ** GOTO lbl35
                            }
                        }
                        break block20;
lbl35:
                        // 3 sources

                        if (((Boolean)v0.join()).booleanValue()) {
                            return CompletableFuture.completedFuture(true);
                        }
                        break block21;
                    }
                    v1 = var0.checkStockCart(var2_2);
                    if (!v1.isDone()) {
                        var4_6 = v1;
                        return var4_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$waitForRestock(io.trickle.task.sites.walmart.usa.Walmart int java.lang.String java.util.concurrent.CompletableFuture java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Walmart)var0, (int)var1_1, (String)var2_2, (CompletableFuture)var4_6, null, (int)2));
                    }
lbl43:
                    // 3 sources

                    if (((Boolean)v1.join()).booleanValue()) {
                        return CompletableFuture.completedFuture(true);
                    }
                }
                if (!(v2 = VertxUtil.randomSignalSleep(var0.instanceSignal, var0.task.getMonitorDelay())).isDone()) {
                    var4_6 = v2;
                    return var4_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$waitForRestock(io.trickle.task.sites.walmart.usa.Walmart int java.lang.String java.util.concurrent.CompletableFuture java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Walmart)var0, (int)var1_1, (String)var2_2, (CompletableFuture)var4_6, null, (int)3));
                }
lbl49:
                // 3 sources

                v2.join();
            }
            catch (Throwable var3_5) {
                if (var3_5.getMessage().contains("CRT expired or empty")) {
                    return CompletableFuture.failedFuture(var3_5);
                }
                var0.logger.error("Error occurred waiting for restock: {}", (Object)var3_5.getMessage());
                v3 = super.randomSleep(12000);
                if (!v3.isDone()) {
                    var4_6 = v3;
                    return var4_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$waitForRestock(io.trickle.task.sites.walmart.usa.Walmart int java.lang.String java.util.concurrent.CompletableFuture java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Walmart)var0, (int)var1_1, (String)var2_2, (CompletableFuture)var4_6, (Throwable)var3_5, (int)4));
                }
lbl60:
                // 3 sources

                v3.join();
            }
        }
        return CompletableFuture.completedFuture(false);
    }

    public CompletableFuture checkStockTerra(String string) {
        Buffer buffer = new JsonObject("{\"variables\":\"{\\\"casperSlots\\\":{\\\"fulfillmentType\\\":\\\"ACC\\\",\\\"reservationType\\\":\\\"SLOTS\\\"},\\\"postalAddress\\\":{\\\"addressType\\\":\\\"RESIDENTIAL\\\",\\\"countryCode\\\":\\\"USA\\\",\\\"postalCode\\\":\\\"" + this.api.getTask().getProfile().getZip() + "\\\",\\\"stateOrProvinceCode\\\":\\\"" + this.api.getTask().getProfile().getState() + "\\\",\\\"zipLocated\\\":true},\\\"storeFrontIds\\\":[{\\\"distance\\\":2.24,\\\"inStore\\\":false,\\\"preferred\\\":false,\\\"storeId\\\":\\\"91672\\\",\\\"storeUUID\\\":null,\\\"usStoreId\\\":91672},{\\\"distance\\\":3.04,\\\"inStore\\\":false,\\\"preferred\\\":false,\\\"storeId\\\":\\\"5936\\\",\\\"storeUUID\\\":null,\\\"usStoreId\\\":5936},{\\\"distance\\\":3.31,\\\"inStore\\\":false,\\\"preferred\\\":false,\\\"storeId\\\":\\\"90563\\\",\\\"storeUUID\\\":null,\\\"usStoreId\\\":90563},{\\\"distance\\\":3.41,\\\"inStore\\\":false,\\\"preferred\\\":false,\\\"storeId\\\":\\\"91675\\\",\\\"storeUUID\\\":null,\\\"usStoreId\\\":91675},{\\\"distance\\\":5.58,\\\"inStore\\\":false,\\\"preferred\\\":false,\\\"storeId\\\":\\\"91121\\\",\\\"storeUUID\\\":null,\\\"usStoreId\\\":91121}],\\\"productId\\\":\\\"" + string + "\\\",\\\"selected\\\":false}\"}").toBuffer();
        int n = 0;
        while (n < 5) {
            block14: {
                HttpRequest httpRequest = this.api.terraFirma(string, false);
                try {
                    JsonArray jsonArray;
                    CompletableFuture completableFuture = Request.send(httpRequest, buffer);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture2 = completableFuture;
                        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$checkStockTerra(this, string, buffer, n, httpRequest, completableFuture2, null, null, 1, arg_0));
                    }
                    HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                    if (httpResponse == null) break block14;
                    if (httpResponse.statusCode() == 201 || httpResponse.statusCode() == 200 || httpResponse.statusCode() == 206) {
                        JsonObject jsonObject;
                        JsonObject jsonObject2 = httpResponse.bodyAsJsonObject();
                        if (!jsonObject2.containsKey("data") || !(jsonObject = jsonObject2.getJsonObject("data").getJsonObject("productByProductId")).containsKey("offerList") || (jsonArray = jsonObject.getJsonArray("offerList")) == null) break block14;
                    } else {
                        this.logger.warn("Waiting for restock (t): status:'{}'", (Object)httpResponse.statusCode());
                        CompletableFuture completableFuture3 = this.api.handleBadResponse(httpResponse.statusCode(), httpResponse);
                        if (!completableFuture3.isDone()) {
                            CompletableFuture completableFuture4 = completableFuture3;
                            return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$checkStockTerra(this, string, buffer, n, httpRequest, completableFuture4, httpResponse, null, 2, arg_0));
                        }
                        completableFuture3.join();
                        break block14;
                    }
                    for (int i = 0; i < jsonArray.size(); ++i) {
                        try {
                            JsonObject jsonObject = jsonArray.getJsonObject(i);
                            if (!jsonObject.getString("id", "").equalsIgnoreCase(this.itemKeyword)) continue;
                            if (jsonObject.containsKey("productAvailability") && jsonObject.getJsonObject("productAvailability").getString("availabilityStatus").equalsIgnoreCase("IN_STOCK")) {
                                JsonObject jsonObject3;
                                if (jsonObject.containsKey("offerInfo") && (jsonObject3 = jsonObject.getJsonObject("offerInfo")).containsKey("offerType")) {
                                    if (jsonObject3.getString("offerType", "").contains("ONLINE")) {
                                        VertxUtil.sendSignal(this.instanceSignal, this.api.getWebClient().cookieStore().getCookieValue("CRT"));
                                        return CompletableFuture.completedFuture(true);
                                    }
                                    this.logger.info("Waiting for restock (t)");
                                    return CompletableFuture.completedFuture(false);
                                }
                                VertxUtil.sendSignal(this.instanceSignal, this.api.getWebClient().cookieStore().getCookieValue("CRT"));
                                return CompletableFuture.completedFuture(true);
                            }
                            this.logger.info("Waiting for restock (t)");
                            return CompletableFuture.completedFuture(false);
                        }
                        catch (Throwable throwable) {
                            // empty catch block
                        }
                    }
                }
                catch (Throwable throwable) {
                    this.logger.error("Error occurred waiting for restock (t): {}", (Object)throwable.getMessage());
                    CompletableFuture completableFuture = super.randomSleep(12000);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture5 = completableFuture;
                        return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$checkStockTerra(this, string, buffer, n, httpRequest, completableFuture5, null, throwable, 3, arg_0));
                    }
                    completableFuture.join();
                }
            }
            ++n;
        }
        return CompletableFuture.completedFuture(false);
    }

    public static void lambda$updateOrCreateSession$2(Cookie cookie) {
        if (!cookie.value().equalsIgnoreCase("1")) return;
        if (cookie.name().equals("g")) return;
        GLOBAL_COOKIES.add((Object)cookie);
    }

    public void importCookies(API aPI) {
        aPI.cookieStore().clear();
        JsonArray jsonArray = new JsonArray(Deobfuscator.readJsFile("browsercookies.json"));
        int n = 0;
        while (n < jsonArray.size()) {
            JsonObject jsonObject = jsonArray.getJsonObject(n);
            aPI.cookieStore().put(jsonObject.getString("name"), jsonObject.getString("value"), "www.walmart.com", "/");
            ++n;
        }
    }

    public CompletableFuture atcAffiliate() {
        String string = ThreadLocalRandom.current().nextBoolean() ? "addToCart" : "buynow";
        this.logger.info("Attempting add to cart");
        int n = 0;
        while (this.running) {
            if (n > 50) return CompletableFuture.failedFuture(new Exception());
            try {
                ++n;
                HttpRequest httpRequest = this.api.affilCrossSite(string);
                CompletableFuture completableFuture = Request.send(httpRequest.as(BodyCodec.string()));
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$atcAffiliate(this, string, n, httpRequest, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 302) {
                    String string2 = this.api.getWebClient().cookieStore().getCookieValue("CRT");
                    VertxUtil.sendSignal(this.instanceSignal, string2);
                    this.logger.info("Successfully added to cart: status:'{}'", (Object)httpResponse.statusCode());
                    this.api.getWebClient().cookieStore().putFromOther(this.sessionCookies);
                    return CompletableFuture.completedFuture(null);
                }
                this.logger.info("Waiting for restock [PID]: '{}'", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture3 = this.api.handleBadResponse(httpResponse.statusCode(), httpResponse);
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$atcAffiliate(this, string, n, httpRequest, completableFuture4, httpResponse, null, 2, arg_0));
                }
                completableFuture3.join();
                CompletableFuture completableFuture5 = this.handleCRTSignalSleep(this.api);
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$atcAffiliate(this, string, n, httpRequest, completableFuture6, httpResponse, null, 3, arg_0));
                }
                if (!((Boolean)completableFuture5.join()).booleanValue()) continue;
                return CompletableFuture.completedFuture(null);
            }
            catch (Throwable throwable) {
                this.logger.error("Error occurred waiting for restock (s): {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = super.randomSleep(12000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$atcAffiliate(this, string, n, null, completableFuture7, null, throwable, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.failedFuture(new Exception());
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$atcNormal(Walmart var0, Buffer var1_1, int var2_2, HttpRequest var3_3, CompletableFuture var4_5, HttpResponse var5_6, Throwable var6_8, int var7_9, Object var8_10) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [9[CATCHBLOCK]], but top level block is 15[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:845)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1042)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:929)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:73)
         *     at org.benf.cfr.reader.Main.main(Main.java:49)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:303)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$null$5(ResourceDecompiling.java:158)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    @Override
    public CompletableFuture run() {
        if (this.mode.equals((Object)Mode.DESKTOP)) {
            this.api.setAPI(PerimeterX.createDesktopAPI(this));
        } else {
            this.api.setAPI(PerimeterX.createMobile(this));
        }
        CompletableFuture completableFuture = this.generateToken();
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$run(this, completableFuture2, 0, 1, arg_0));
        }
        completableFuture.join();
        CompletableFuture completableFuture3 = this.api.initialisePX();
        if (!completableFuture3.isDone()) {
            CompletableFuture completableFuture4 = completableFuture3;
            return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$run(this, completableFuture4, 0, 2, arg_0));
        }
        int n = ((Boolean)completableFuture3.join()).booleanValue() ? 1 : 0;
        if (n == 0) {
            this.logger.warn("Failed to initialise and configure task. Stopping...");
            return CompletableFuture.completedFuture(null);
        }
        try {
            if (this.mode == Mode.DESKTOP) {
                CompletableFuture completableFuture5 = this.fetchHomepage(true);
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$run(this, completableFuture6, n, 3, arg_0));
                }
                completableFuture5.join();
                CompletableFuture completableFuture7 = this.api.generatePX(false);
                if (!completableFuture7.isDone()) {
                    CompletableFuture completableFuture8 = completableFuture7;
                    return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$run(this, completableFuture8, n, 4, arg_0));
                }
                completableFuture7.join();
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        CompletableFuture completableFuture9 = this.updateOrCreateSession();
        if (!completableFuture9.isDone()) {
            CompletableFuture completableFuture10 = completableFuture9;
            return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$run(this, completableFuture10, n, 5, arg_0));
        }
        completableFuture9.join();
        if (this.task.getMode().toLowerCase().contains("fast")) {
            CompletableFuture completableFuture11 = PaymentInstance.preload(this, this.task, this.token, this.mode);
            if (!completableFuture11.isDone()) {
                CompletableFuture completableFuture12 = completableFuture11;
                return ((CompletableFuture)completableFuture12.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$run(this, completableFuture12, n, 6, arg_0));
            }
            String string = (String)completableFuture11.join();
            this.token.setPiHash(string);
            this.api.getWebClient().cookieStore().clear();
            if (this.task.getMode().toLowerCase().contains("grief")) {
                this.api.swapClient();
            }
        }
        if (this.keepAliveWorker == 0L) {
            this.keepAliveWorker = this.vertx.setPeriodic(TimeUnit.SECONDS.toMillis(100L), this::lambda$run$1);
        }
        CompletableFuture completableFuture13 = this.sleep(ThreadLocalRandom.current().nextInt(300, 1500));
        if (!completableFuture13.isDone()) {
            CompletableFuture completableFuture14 = completableFuture13;
            return ((CompletableFuture)completableFuture14.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$run(this, completableFuture14, n, 7, arg_0));
        }
        completableFuture13.join();
        CompletableFuture completableFuture15 = this.tryCart();
        if (!completableFuture15.isDone()) {
            CompletableFuture completableFuture16 = completableFuture15;
            return ((CompletableFuture)completableFuture16.exceptionally(Function.identity())).thenCompose(arg_0 -> Walmart.async$run(this, completableFuture16, n, 8, arg_0));
        }
        completableFuture15.join();
        if (this.keepAliveWorker == 0L) return CompletableFuture.completedFuture(null);
        this.vertx.cancelTimer(this.keepAliveWorker);
        return CompletableFuture.completedFuture(null);
    }
}

