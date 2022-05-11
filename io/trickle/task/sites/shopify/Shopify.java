/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.handler.codec.http.cookie.ClientCookieDecoder
 *  io.netty.handler.codec.http.cookie.Cookie
 *  io.trickle.account.Account
 *  io.trickle.account.AccountController
 *  io.trickle.core.Controller
 *  io.trickle.core.Engine
 *  io.trickle.core.VertxSingleton
 *  io.trickle.core.actor.TaskActor
 *  io.trickle.harvester.CaptchaToken
 *  io.trickle.harvester.Harvester
 *  io.trickle.harvester.SolveFuture
 *  io.trickle.harvester.TokenController
 *  io.trickle.task.Task
 *  io.trickle.task.sites.Site
 *  io.trickle.task.sites.shopify.Flow
 *  io.trickle.task.sites.shopify.Flow$Actions
 *  io.trickle.task.sites.shopify.Mode
 *  io.trickle.task.sites.shopify.Presolver
 *  io.trickle.task.sites.shopify.Shopify$1
 *  io.trickle.task.sites.shopify.ShopifyAPI
 *  io.trickle.task.sites.shopify.util.PaymentTokenSupplier
 *  io.trickle.task.sites.shopify.util.REDIRECT_STATUS
 *  io.trickle.task.sites.shopify.util.ShippingRateSupplier
 *  io.trickle.task.sites.shopify.util.SiteParser
 *  io.trickle.task.sites.shopify.util.VariantHandler
 *  io.trickle.util.Pair
 *  io.trickle.util.Storage
 *  io.trickle.util.Utils
 *  io.trickle.util.analytics.Analytics
 *  io.trickle.util.concurrent.VertxUtil
 *  io.trickle.util.request.Request
 *  io.trickle.webclient.RealClient
 *  io.trickle.webclient.RealClientFactory
 *  io.trickle.webclient.TaskApiClient
 *  io.vertx.core.MultiMap
 *  io.vertx.core.Vertx
 *  io.vertx.core.buffer.Buffer
 *  io.vertx.core.json.JsonArray
 *  io.vertx.core.json.JsonObject
 *  io.vertx.core.shareddata.Lock
 *  io.vertx.core.shareddata.SharedData
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.client.HttpResponse
 *  io.vertx.ext.web.client.WebClient
 *  io.vertx.ext.web.codec.BodyCodec
 *  io.vertx.ext.web.multipart.MultipartForm
 */
package io.trickle.task.sites.shopify;

import io.netty.handler.codec.http.cookie.ClientCookieDecoder;
import io.netty.handler.codec.http.cookie.Cookie;
import io.trickle.account.Account;
import io.trickle.account.AccountController;
import io.trickle.core.Controller;
import io.trickle.core.Engine;
import io.trickle.core.VertxSingleton;
import io.trickle.core.actor.TaskActor;
import io.trickle.harvester.CaptchaToken;
import io.trickle.harvester.Harvester;
import io.trickle.harvester.SolveFuture;
import io.trickle.harvester.TokenController;
import io.trickle.task.Task;
import io.trickle.task.sites.Site;
import io.trickle.task.sites.shopify.Flow;
import io.trickle.task.sites.shopify.Mode;
import io.trickle.task.sites.shopify.Presolver;
import io.trickle.task.sites.shopify.Shopify;
import io.trickle.task.sites.shopify.ShopifyAPI;
import io.trickle.task.sites.shopify.util.PaymentTokenSupplier;
import io.trickle.task.sites.shopify.util.REDIRECT_STATUS;
import io.trickle.task.sites.shopify.util.ShippingRateSupplier;
import io.trickle.task.sites.shopify.util.SiteParser;
import io.trickle.task.sites.shopify.util.VariantHandler;
import io.trickle.util.Pair;
import io.trickle.util.Storage;
import io.trickle.util.Utils;
import io.trickle.util.analytics.Analytics;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.trickle.webclient.RealClient;
import io.trickle.webclient.RealClientFactory;
import io.trickle.webclient.TaskApiClient;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.Lock;
import io.vertx.core.shareddata.SharedData;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.ext.web.multipart.MultipartForm;
import java.lang.invoke.LambdaMetafactory;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Shopify
extends TaskActor {
    public static Pattern SHIPPINGRATE_PATTERN;
    public Pair<String, String> propertiesPair;
    public List<String> TEXTAREA_PARAMS;
    public static boolean queueLogic;
    public CompletableFuture<ShippingRateSupplier> shippingRate;
    public boolean isSmart;
    public Presolver presolver;
    public String size;
    public AtomicBoolean isCPMonitorTriggered;
    public boolean isRestockMode;
    public Long productPickupTime;
    public static Pattern FIELD_ERR_PATTERN;
    public List<String> negativeKeywords;
    public static Harvester[] HARVESTERS;
    public static Pattern AUTHENTICITY_CHECKOUT_TOKEN_PATTERN;
    public boolean isKeyword = false;
    public static Pattern GATEWAY_CARD_PATTERN;
    public String instanceSignal;
    public boolean noProcessOnFirstTry = true;
    public String lockKey;
    public static Pattern TEXTAREA_PARAMS_PATTERN;
    public boolean isExtra;
    public CompletableFuture<PaymentTokenSupplier> paymentToken;
    public boolean isCod;
    public ShopifyAPI api;
    public static Pattern CHECK_VARIANT_PATTERN;
    public long prevTime;
    public static AtomicBoolean isHarvesterInited;
    public boolean isShippingSubmittedWithWallets;
    public static Pattern TOTAL_PRICE_PATTERN;
    public static Pattern CHECK_EL_PATTERN;
    public String BROWSER_HEIGHT;
    public Task task;
    public CompletableFuture<Void> cpMonitor;
    public static Pattern AUTHENTICITY_TOKEN_PATTERN;
    public Mode MODE;
    public static ConcurrentHashMap<String, CompletionStage<Lock>> lockMap;
    public static CompletableFuture<?>[] harvesterCFs;
    public List<String> positiveKeywords = new LinkedList<String>();
    public static Pattern SITE_URL_PATTERN;
    public String precartItemName = "123456789";
    public boolean isEarly;
    public Flow.Actions FLOW_ACTIONS;
    public String BROWSER_WIDTH;
    public int counter429;
    public Integer price;
    public boolean hasShippingAlreadySubmittedAPI;
    public static Pattern TOTAL_PRICE_BACKUP_PATTERN;
    public String authenticity;
    public String paymentGateway;
    public boolean isEL = false;
    public static Instant instanceTime;
    public static int TIMEZONE_OFFSET;

    public void shutOffPresolver(boolean bl) {
        if (!bl) {
            if (this.isEarly) return;
        }
        if (this.presolver != null) {
            this.presolver.close();
        }
        if (this.cpMonitor == null) return;
        this.cpMonitor.complete(null);
    }

    public boolean updatePrice(String string) {
        Matcher matcher = TOTAL_PRICE_PATTERN.matcher(string);
        Matcher matcher2 = TOTAL_PRICE_BACKUP_PATTERN.matcher(string);
        if (string.contains("Calculating taxes")) return false;
        if (matcher.find()) {
            this.price = Integer.parseInt(matcher.group(1));
        } else if (matcher2.find()) {
            this.price = Integer.parseInt(matcher2.group(1));
        } else {
            this.logger.info("No price found");
        }
        return true;
    }

    public CompletableFuture attemptGenCheckoutUrl(boolean bl) {
        int n = 0;
        this.logger.info("Generating checkout URL [l]...");
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            try {
                HttpRequest httpRequest = this.api.emptyCheckoutViaCart();
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest, (MultiMap)this.api.emptyCheckoutViaCartForm());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$attemptGenCheckoutUrl(this, (int)(bl ? 1 : 0), n, httpRequest, completableFuture2, null, null, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse != null && (httpResponse.statusCode() == 302 || httpResponse.statusCode() == 301)) {
                    String string = httpResponse.getHeader("location");
                    REDIRECT_STATUS rEDIRECT_STATUS = REDIRECT_STATUS.checkRedirectStatus((String)string);
                    if (rEDIRECT_STATUS == REDIRECT_STATUS.PASSWORD) {
                        return CompletableFuture.completedFuture(null);
                    }
                    if (bl && rEDIRECT_STATUS == REDIRECT_STATUS.CHECKPOINT) {
                        return CompletableFuture.completedFuture(null);
                    }
                    CompletableFuture completableFuture3 = this.handleRedirect(httpResponse, true);
                    if (!completableFuture3.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture3;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$attemptGenCheckoutUrl(this, (int)(bl ? 1 : 0), n, httpRequest, completableFuture4, httpResponse, string, rEDIRECT_STATUS, null, 2, arg_0));
                    }
                    completableFuture3.join();
                    return CompletableFuture.completedFuture(string);
                }
                if (httpResponse == null) continue;
                this.logger.info("Unable to generate checkout url. Handling - {}", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture5 = VertxUtil.sleep((long)this.task.getRetryDelay());
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$attemptGenCheckoutUrl(this, (int)(bl ? 1 : 0), n, httpRequest, completableFuture6, httpResponse, null, null, null, 3, arg_0));
                }
                completableFuture5.join();
            }
            catch (Throwable throwable) {
                if (!throwable.getMessage().contains("Connection was closed")) {
                    this.logger.error("Failed genning checkoutURL: {}", (Object)throwable.getMessage());
                }
                CompletableFuture completableFuture = VertxUtil.randomSleep((long)this.task.getRetryDelay());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$attemptGenCheckoutUrl(this, (int)(bl ? 1 : 0), n, null, completableFuture7, null, null, null, throwable, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture atcAJAX(String string, boolean bl) {
        String string2 = this.api.getCookies().getCookieValue("cart_sig");
        int n = 0;
        this.logger.info("Attempting ATC [AJAX]");
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            if ((this.isKeyword || this.isEL) && Utils.isInteger((String)this.task.getKeywords()[0])) {
                string = this.task.getKeywords()[0];
            }
            try {
                HttpRequest httpRequest = this.api.atcAJAX(string);
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$atcAJAX(this, string, (int)(bl ? 1 : 0), string2, n, httpRequest, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 200) {
                    VertxUtil.sendSignal((String)this.api.getSiteURL());
                    if (this.hasTrulyCarted(string2)) {
                        VertxUtil.sendSignal((String)string);
                        JsonObject jsonObject = httpResponse.bodyAsJsonObject();
                        return CompletableFuture.completedFuture(jsonObject);
                    }
                    this.logger.warn("Item is not yet cartable! Please wait for sale to go live");
                } else {
                    if (bl && httpResponse.statusCode() == 401) {
                        return CompletableFuture.completedFuture(null);
                    }
                    if (httpResponse.statusCode() == 302 || httpResponse.statusCode() == 301 || httpResponse.statusCode() == 401) {
                        CompletableFuture completableFuture3 = this.handleRedirect(httpResponse, false);
                        if (!completableFuture3.isDone()) {
                            CompletableFuture completableFuture4 = completableFuture3;
                            return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$atcAJAX(this, string, (int)(bl ? 1 : 0), string2, n, httpRequest, completableFuture4, httpResponse, null, 2, arg_0));
                        }
                        boolean bl2 = (Boolean)completableFuture3.join();
                        if (!bl2) continue;
                    }
                    this.logger.warn("Failed attempting ATC [AJAX]: status: '{}'", (Object)httpResponse.statusCode());
                }
                CompletableFuture completableFuture5 = VertxUtil.signalSleep((String)string, (long)this.task.getMonitorDelay());
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$atcAJAX(this, string, (int)(bl ? 1 : 0), string2, n, httpRequest, completableFuture6, httpResponse, null, 3, arg_0));
                }
                completableFuture5.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Failed attempting ATC [AJAX]]: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = super.randomSleep(3000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$atcAJAX(this, string, (int)(bl ? 1 : 0), string2, n, null, completableFuture7, null, throwable, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture clearWithChangeJS() {
        int n = 0;
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            try {
                JsonObject jsonObject;
                HttpRequest httpRequest = this.api.changeJS();
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest, (MultipartForm)this.changeForm());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$clearWithChangeJS(this, n, httpRequest, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 200 && (jsonObject = httpResponse.bodyAsJsonObject()).getInteger("item_count") == 0) {
                    return CompletableFuture.completedFuture(jsonObject);
                }
                if (httpResponse.statusCode() == 302 || httpResponse.statusCode() == 301) {
                    CompletableFuture completableFuture3 = this.handleRedirect(httpResponse, false);
                    if (!completableFuture3.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture3;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$clearWithChangeJS(this, n, httpRequest, completableFuture4, httpResponse, null, 2, arg_0));
                    }
                    completableFuture3.join();
                }
                this.logger.warn("Failed changing cart: status: '{}'", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture5 = VertxUtil.sleep((long)this.task.getRetryDelay());
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$clearWithChangeJS(this, n, httpRequest, completableFuture6, httpResponse, null, 3, arg_0));
                }
                completableFuture5.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Failed changing cart: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = super.randomSleep(3000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$clearWithChangeJS(this, n, null, completableFuture7, null, throwable, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$shippingAndBillingPostAlreadyCarted(Shopify var0, int var1_1, HttpRequest var2_2, CompletableFuture var3_4, HttpResponse var4_5, Throwable var5_6, int var6_7, Object var7_8) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [9[CATCHBLOCK]], but top level block is 15[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$handleOOS(Shopify var0, String var1_1, int var2_2, HttpRequest var3_3, CompletableFuture var4_5, HttpResponse var5_6, Throwable var6_7, int var7_8, Object var8_9) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [8[CATCHBLOCK]], but top level block is 13[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public CompletableFuture submitCheckpoint(CaptchaToken captchaToken) {
        this.logger.info("Submitting checkpoint...");
        int n = 0;
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            try {
                HttpRequest httpRequest = this.api.submitCheckpoint();
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest, (MultiMap)Shopify.checkpointForm(captchaToken));
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$submitCheckpoint(this, captchaToken, n, httpRequest, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if ((httpResponse.statusCode() == 302 || httpResponse.statusCode() == 301) && this.api.checkpointClient.cookieStore().contains("_shopify_checkpoint")) {
                    this.api.getCookies().put("_shopify_checkpoint", this.api.checkpointClient.cookieStore().getCookieValue("_shopify_checkpoint"), this.api.getSiteURL());
                    return CompletableFuture.completedFuture(null);
                }
                this.logger.warn("Retrying submitting checkpoint: status: '{}'", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture3 = VertxUtil.sleep((long)this.task.getRetryDelay());
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$submitCheckpoint(this, captchaToken, n, httpRequest, completableFuture4, httpResponse, null, 2, arg_0));
                }
                completableFuture3.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error with checkpoint submission: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = super.randomSleep(3000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture5 = completableFuture;
                    return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$submitCheckpoint(this, captchaToken, n, null, completableFuture5, null, throwable, 3, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture fetchProductsJSON(boolean bl) {
        int n = 0;
        if (bl) {
            this.logger.info("Monitoring [KEYWORD]");
        } else {
            this.logger.info("Preparing precart...");
        }
        Lock lock = null;
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            try {
                HttpResponse httpResponse;
                HttpRequest httpRequest;
                if (!bl) {
                    CompletionStage<Lock> completionStage = lockMap.get(this.lockKey);
                    if (!completionStage.toCompletableFuture().isDone()) {
                        CompletionStage<Lock> completionStage2 = completionStage;
                        return completionStage2.exceptionally(Function.identity()).thenCompose(arg_0 -> Shopify.async$fetchProductsJSON(this, (int)(bl ? 1 : 0), n, lock, completionStage2, null, null, null, null, null, null, null, null, 1, arg_0)).toCompletableFuture();
                    }
                    lock = completionStage.toCompletableFuture().join();
                    httpRequest = this.vertx.sharedData();
                    httpResponse = httpRequest.getLocalMap(this.task.getSiteUserEntry());
                    if (httpResponse.containsKey((Object)"products")) {
                        lock.release();
                        return CompletableFuture.completedFuture((JsonObject)httpResponse.get((Object)"products"));
                    }
                }
                httpRequest = this.api.productsJSON(bl);
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$fetchProductsJSON(this, (int)(bl ? 1 : 0), n, lock, null, httpRequest, completableFuture2, null, null, null, null, null, null, 2, arg_0));
                }
                httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() != 200) {
                    if (!bl && httpResponse.statusCode() == 401) {
                        return CompletableFuture.completedFuture(new JsonObject());
                    }
                    if (httpResponse.statusCode() == 302 || httpResponse.statusCode() == 301 || httpResponse.statusCode() == 401) {
                        CompletableFuture completableFuture3 = this.handleRedirect(httpResponse, false);
                        if (!completableFuture3.isDone()) {
                            CompletableFuture completableFuture4 = completableFuture3;
                            return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$fetchProductsJSON(this, (int)(bl ? 1 : 0), n, lock, null, httpRequest, completableFuture4, httpResponse, null, null, null, null, null, 4, arg_0));
                        }
                        completableFuture3.join();
                        continue;
                    }
                    this.logger.warn("Failed waiting for restock [KEYWORD]: status: '{}'", (Object)httpResponse.statusCode());
                    CompletableFuture completableFuture5 = VertxUtil.signalSleep((String)this.instanceSignal, (long)this.task.getRetryDelay());
                    if (!completableFuture5.isDone()) {
                        CompletableFuture completableFuture6 = completableFuture5;
                        return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$fetchProductsJSON(this, (int)(bl ? 1 : 0), n, lock, null, httpRequest, completableFuture6, httpResponse, null, null, null, null, null, 5, arg_0));
                    }
                    completableFuture5.join();
                }
                this.initKeywords();
                VertxUtil.sendSignal((String)this.api.getSiteURL());
                JsonObject jsonObject = httpResponse.bodyAsJsonObject();
                if (bl) return CompletableFuture.completedFuture(jsonObject);
                SharedData sharedData = this.vertx.sharedData();
                sharedData.getLocalMap(this.task.getSiteUserEntry()).put((Object)"products", (Object)jsonObject);
                if (lock == null) return CompletableFuture.completedFuture(jsonObject);
                Iterator<String> iterator = lockMap.keys().asIterator();
                while (iterator.hasNext()) {
                    String string = iterator.next();
                    CompletionStage<Lock> completionStage = lockMap.get(string);
                    if (!completionStage.toCompletableFuture().isDone()) {
                        CompletionStage<Lock> completionStage3 = completionStage;
                        return completionStage3.exceptionally(Function.identity()).thenCompose(arg_0 -> Shopify.async$fetchProductsJSON(this, (int)(bl ? 1 : 0), n, lock, completionStage3, httpRequest, null, httpResponse, jsonObject, sharedData, iterator, string, null, 3, arg_0)).toCompletableFuture();
                    }
                    completionStage.toCompletableFuture().join().release();
                }
                return CompletableFuture.completedFuture(jsonObject);
            }
            catch (Throwable throwable) {
                if (lock != null) {
                    lock.release();
                }
                this.logger.error("Failed waiting for restock [KEYWORD]: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = super.randomSleep(3000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$fetchProductsJSON(this, (int)(bl ? 1 : 0), n, lock, null, null, completableFuture7, null, null, null, null, null, throwable, 6, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture processPayment(String string) {
        this.logger.info("Processing...");
        int n = 0;
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            try {
                Object object;
                String string2;
                MultiMap multiMap;
                if (this.isRestockMode) {
                    CompletableFuture completableFuture = this.processingFormRestockMode(string);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture2 = completableFuture;
                        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$processPayment(this, string, n, completableFuture2, null, 0L, null, null, null, null, null, 1, arg_0));
                    }
                    multiMap = (MultiMap)completableFuture.join();
                } else {
                    CompletableFuture completableFuture = this.processingForm(string);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture3 = completableFuture;
                        return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$processPayment(this, string, n, completableFuture3, null, 0L, null, null, null, null, null, 2, arg_0));
                    }
                    multiMap = (MultiMap)completableFuture.join();
                }
                MultiMap multiMap2 = multiMap;
                long l = System.currentTimeMillis();
                HttpRequest httpRequest = this.api.postPayment(string);
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest, (MultiMap)multiMap2);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$processPayment(this, string, n, completableFuture4, multiMap2, l, httpRequest, null, null, null, null, 3, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if (!(this.api.getCookies().contains("_shopify_checkpoint") || System.currentTimeMillis() - l <= 30000L && httpResponse.statusCode() != 403)) {
                    this.logger.info("Rotating proxy");
                    this.api.rotateProxy();
                }
                if (this.isRestockMode && httpResponse.statusCode() == 302) {
                    CompletableFuture<PaymentTokenSupplier> completableFuture5 = this.paymentToken;
                    if (!completableFuture5.isDone()) {
                        CompletableFuture<PaymentTokenSupplier> completableFuture6 = completableFuture5;
                        return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$processPayment(this, string, n, completableFuture6, multiMap2, l, httpRequest, httpResponse, null, null, null, 4, arg_0));
                    }
                    completableFuture5.join().setSubmittedSuccessfully();
                } else {
                    this.genPaymentToken();
                }
                if (httpResponse.statusCode() == 302 || httpResponse.statusCode() == 301) {
                    string2 = httpResponse.getHeader("location");
                    object = REDIRECT_STATUS.checkRedirectStatus((String)string2);
                    CompletableFuture completableFuture7 = this.handleRedirect(httpResponse, true);
                    if (!completableFuture7.isDone()) {
                        CompletableFuture completableFuture8 = completableFuture7;
                        return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$processPayment(this, string, n, completableFuture8, multiMap2, l, httpRequest, httpResponse, string2, (REDIRECT_STATUS)object, null, 5, arg_0));
                    }
                    Boolean bl = (Boolean)completableFuture7.join();
                    this.shutOffPresolver(true);
                    if (bl != null && !bl.booleanValue()) continue;
                    if (string2.contains("/processing")) {
                        VertxUtil.sendSignal((String)("processing" + this.instanceSignal));
                        return CompletableFuture.completedFuture(string2);
                    }
                    if (object != REDIRECT_STATUS.QUEUE_NEW) {
                        if (!string2.contains("/stock_problems?previous_step=payment_method")) {
                            this.logger.warn("Error processing: status: '{}'", (Object)string2);
                            CompletableFuture completableFuture9 = this.getProcessingPage(string, false);
                            if (!completableFuture9.isDone()) {
                                CompletableFuture completableFuture10 = completableFuture9;
                                return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$processPayment(this, string, n, completableFuture10, multiMap2, l, httpRequest, httpResponse, string2, (REDIRECT_STATUS)object, null, 6, arg_0));
                            }
                            completableFuture9.join();
                            if (this.noProcessOnFirstTry) {
                                this.noProcessOnFirstTry = false;
                                continue;
                            }
                        } else {
                            this.logger.info("Processing. " + (string2.contains("stock_problem") ? "OKAY" : "Possible error."));
                            this.counter429 = 0;
                        }
                    }
                    if (string2.contains("stock_problem")) {
                        if (!this.isShippingSubmittedWithWallets) {
                            CompletableFuture completableFuture11 = this.walletsSubmitShipping(string);
                            if (!completableFuture11.isDone()) {
                                CompletableFuture completableFuture12 = completableFuture11;
                                return ((CompletableFuture)completableFuture12.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$processPayment(this, string, n, completableFuture12, multiMap2, l, httpRequest, httpResponse, string2, (REDIRECT_STATUS)object, null, 7, arg_0));
                            }
                            completableFuture11.join();
                            CompletableFuture completableFuture13 = this.getProcessingPage(string, true);
                            if (!completableFuture13.isDone()) {
                                CompletableFuture completableFuture14 = completableFuture13;
                                return ((CompletableFuture)completableFuture14.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$processPayment(this, string, n, completableFuture14, multiMap2, l, httpRequest, httpResponse, string2, (REDIRECT_STATUS)object, null, 8, arg_0));
                            }
                            completableFuture13.join();
                        } else if (this.api.getCookies().contains("_shopify_checkpoint") || this.price == null) {
                            CompletableFuture completableFuture15 = this.getProcessingPage(string, true);
                            if (!completableFuture15.isDone()) {
                                CompletableFuture completableFuture16 = completableFuture15;
                                return ((CompletableFuture)completableFuture16.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$processPayment(this, string, n, completableFuture16, multiMap2, l, httpRequest, httpResponse, string2, (REDIRECT_STATUS)object, null, 9, arg_0));
                            }
                            completableFuture15.join();
                        }
                    }
                    CompletableFuture completableFuture17 = VertxUtil.signalSleep((String)("processing" + this.instanceSignal), (long)this.task.getMonitorDelay());
                    if (!completableFuture17.isDone()) {
                        CompletableFuture completableFuture18 = completableFuture17;
                        return ((CompletableFuture)completableFuture18.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$processPayment(this, string, n, completableFuture18, multiMap2, l, httpRequest, httpResponse, string2, (REDIRECT_STATUS)object, null, 10, arg_0));
                    }
                    completableFuture17.join();
                    continue;
                }
                if (httpResponse.statusCode() == 429 && ++this.counter429 > 8) {
                    throw new Throwable("Checkout expired. Restarting task...");
                }
                if (httpResponse.statusCode() == 200) {
                    string2 = httpResponse.bodyAsString();
                    object = Analytics.parseOrderId((String)string2);
                    if (object != null) {
                        this.logger.error("Processing error: {}", object);
                    }
                    if (this.task.getSite() != Site.KITH) {
                        this.updateGateway(string2);
                    }
                    this.updateCheckoutParams(string2);
                    this.updatePrice(string2);
                } else {
                    this.logger.warn("Error processing: status: '{}'", (Object)httpResponse.statusCode());
                }
                if (this.noProcessOnFirstTry) {
                    this.noProcessOnFirstTry = false;
                    continue;
                }
                CompletableFuture completableFuture19 = VertxUtil.sleep((long)this.task.getRetryDelay());
                if (!completableFuture19.isDone()) {
                    CompletableFuture completableFuture20 = completableFuture19;
                    return ((CompletableFuture)completableFuture20.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$processPayment(this, string, n, completableFuture20, multiMap2, l, httpRequest, httpResponse, null, null, null, 11, arg_0));
                }
                completableFuture19.join();
            }
            catch (Exception exception) {
                if (exception.getMessage().equals("io.vertx.core.VertxException: Connection was closed")) continue;
                this.logger.error("Error processing: {}", (Object)exception.getMessage());
                CompletableFuture completableFuture = super.randomSleep(3000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture21 = completableFuture;
                    return ((CompletableFuture)completableFuture21.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$processPayment(this, string, n, completableFuture21, null, 0L, null, null, null, null, exception, 12, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture handlePreload(String string) {
        if (!string.contains("checkout")) throw new Exception("Bad checkout URL.");
        boolean bl = string.contains("?");
        String[] stringArray = string.split("\\?");
        if (bl && stringArray[1].contains("key")) {
            this.api.key = stringArray[1].split("key=")[1];
            this.api.key = this.api.key.split("&")[0];
        }
        string = bl ? stringArray[0] : string;
        string = string.split("checkouts/")[1];
        if (!string.startsWith("c/")) return CompletableFuture.completedFuture(string);
        this.api.graphUnstable = true;
        throw new Exception("Restarting task.");
    }

    public JsonObject walletsGenCheckoutUrlForm() {
        JsonObject jsonObject = new JsonObject();
        JsonObject jsonObject2 = new JsonObject().put("is_upstream_button", (Object)false).put("page_type", (Object)"product").put("presentment_currency", (Object)this.api.getCookies().getCookieValue("cart_currency")).put("secret", (Object)true).put("wallet_name", (Object)"Checkout").put("cart_token", (Object)this.api.getCookies().getCookieValue("cart"));
        jsonObject.put("checkout", (Object)jsonObject2);
        return jsonObject;
    }

    public CompletableFuture run() {
        CompletableFuture completableFuture = Shopify.initHarvesters();
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$run(this, completableFuture2, 0, null, null, null, null, 0, 1, arg_0));
        }
        completableFuture.join();
        try {
            this.paymentGateway = SiteParser.getGatewayFromSite((Site)this.task.getSite(), (boolean)this.isCod);
            CompletableFuture completableFuture3 = this.initShopDetails();
            if (!completableFuture3.isDone()) {
                CompletableFuture completableFuture4 = completableFuture3;
                return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$run(this, completableFuture4, 0, null, null, null, null, 0, 2, arg_0));
            }
            completableFuture3.join();
            if (this.task.getMode().contains("login")) {
                this.logger.info("Login required. Logging in...");
                CompletableFuture completableFuture5 = this.login();
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$run(this, completableFuture6, 0, null, null, null, null, 0, 3, arg_0));
                }
                completableFuture5.join();
            }
            while (this.api.getWebClient().isActive()) {
                int n = 0;
                try {
                    Object object;
                    JsonObject jsonObject;
                    Object object2;
                    String string;
                    if (!this.isExtra) {
                        CompletableFuture completableFuture7 = this.genCheckoutURL(ThreadLocalRandom.current().nextBoolean());
                        if (!completableFuture7.isDone()) {
                            CompletableFuture completableFuture8 = completableFuture7;
                            return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$run(this, completableFuture8, n, null, null, null, null, 0, 4, arg_0));
                        }
                        string = (String)completableFuture7.join();
                        CompletableFuture completableFuture9 = this.handlePreload(string);
                        if (!completableFuture9.isDone()) {
                            CompletableFuture completableFuture10 = completableFuture9;
                            return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$run(this, completableFuture10, n, string, null, null, null, 0, 5, arg_0));
                        }
                        string = (String)completableFuture9.join();
                    } else {
                        CompletableFuture completableFuture11 = this.waitTilCartCookie();
                        if (!completableFuture11.isDone()) {
                            CompletableFuture completableFuture12 = completableFuture11;
                            return ((CompletableFuture)completableFuture12.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$run(this, completableFuture12, n, null, null, null, null, 0, 6, arg_0));
                        }
                        n = ((Boolean)completableFuture11.join()).booleanValue();
                        CompletableFuture completableFuture13 = this.walletsGenCheckout();
                        if (!completableFuture13.isDone()) {
                            CompletableFuture completableFuture14 = completableFuture13;
                            return ((CompletableFuture)completableFuture14.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$run(this, completableFuture14, n, null, null, null, null, 0, 7, arg_0));
                        }
                        string = (String)completableFuture13.join();
                        CompletableFuture completableFuture15 = this.handlePreload(string);
                        if (!completableFuture15.isDone()) {
                            CompletableFuture completableFuture16 = completableFuture15;
                            return ((CompletableFuture)completableFuture16.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$run(this, completableFuture16, n, string, null, null, null, 0, 8, arg_0));
                        }
                        string = (String)completableFuture15.join();
                    }
                    CompletableFuture completableFuture17 = this.checkCaptcha(string);
                    if (ThreadLocalRandom.current().nextBoolean()) {
                        CompletableFuture completableFuture18 = completableFuture17;
                        if (!completableFuture18.isDone()) {
                            CompletableFuture completableFuture19 = completableFuture18;
                            return ((CompletableFuture)completableFuture19.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$run(this, completableFuture17, n, string, completableFuture19, null, null, 0, 9, arg_0));
                        }
                        completableFuture18.join();
                    } else {
                        CompletableFuture completableFuture20 = VertxUtil.hardCodedSleep((long)ThreadLocalRandom.current().nextInt(75, 150));
                        if (!completableFuture20.isDone()) {
                            CompletableFuture completableFuture21 = completableFuture20;
                            return ((CompletableFuture)completableFuture21.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$run(this, completableFuture17, n, string, completableFuture21, null, null, 0, 10, arg_0));
                        }
                        completableFuture20.join();
                    }
                    CompletableFuture completableFuture22 = this.shippingAndBillingPut(string);
                    if (!completableFuture22.isDone()) {
                        CompletableFuture completableFuture23 = completableFuture22;
                        return ((CompletableFuture)completableFuture23.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$run(this, completableFuture17, n, string, completableFuture23, null, null, 0, 11, arg_0));
                    }
                    completableFuture22.join();
                    if (!this.isKeyword) {
                        if (this.isEL) {
                            CompletableFuture completableFuture24 = this.fetchELJS();
                            if (!completableFuture24.isDone()) {
                                CompletableFuture completableFuture25 = completableFuture24;
                                return ((CompletableFuture)completableFuture25.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$run(this, completableFuture17, n, string, completableFuture25, null, null, 0, 14, arg_0));
                            }
                            object2 = (JsonObject)completableFuture24.join();
                            this.instanceSignal = VariantHandler.selectVariantFromLink((JsonObject)object2, (String)this.task.getSize(), (Shopify)this);
                        }
                    } else {
                        object2 = null;
                        while (object2 == null) {
                            CompletableFuture completableFuture26 = this.fetchProductsJSON(true);
                            if (!completableFuture26.isDone()) {
                                CompletableFuture completableFuture27 = completableFuture26;
                                return ((CompletableFuture)completableFuture27.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$run(this, completableFuture17, n, string, completableFuture27, (String)object2, null, 0, 12, arg_0));
                            }
                            jsonObject = (JsonObject)completableFuture26.join();
                            object2 = VariantHandler.selectVariantFromKeyword((JsonObject)jsonObject, (String)this.task.getSize(), this.positiveKeywords, this.negativeKeywords, (Shopify)this);
                            if (object2 == null) {
                                CompletableFuture completableFuture28 = VertxUtil.signalSleep((String)this.instanceSignal, (long)this.task.getMonitorDelay());
                                if (!completableFuture28.isDone()) {
                                    CompletableFuture completableFuture29 = completableFuture28;
                                    return ((CompletableFuture)completableFuture29.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$run(this, completableFuture17, n, string, completableFuture29, (String)object2, jsonObject, 0, 13, arg_0));
                                }
                                object = completableFuture28.join();
                                if (object == null) continue;
                                object2 = VariantHandler.selectVariantFromKeyword((JsonObject)new JsonObject(object.toString()), (String)this.task.getSize(), this.positiveKeywords, this.negativeKeywords, (Shopify)this);
                                continue;
                            }
                            VertxUtil.sendSignal((String)this.instanceSignal, (Object)jsonObject.encode());
                        }
                        this.instanceSignal = object2;
                    }
                    this.setProductPickupTime();
                    this.genPaymentToken();
                    int n2 = 1;
                    if (!this.shippingRate.isDone()) {
                        if (this.isExtra) {
                            if (n == 0) {
                                CompletableFuture completableFuture30 = this.atcAJAX(this.instanceSignal, false);
                                if (!completableFuture30.isDone()) {
                                    CompletableFuture completableFuture31 = completableFuture30;
                                    return ((CompletableFuture)completableFuture31.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$run(this, completableFuture17, n, string, completableFuture31, null, null, n2, 15, arg_0));
                                }
                                completableFuture30.join();
                                Analytics.carts.increment();
                                CompletableFuture completableFuture32 = this.getShippingRateOld();
                                if (!completableFuture32.isDone()) {
                                    CompletableFuture completableFuture33 = completableFuture32;
                                    return ((CompletableFuture)completableFuture33.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$run(this, completableFuture17, n, string, completableFuture33, null, null, n2, 16, arg_0));
                                }
                                completableFuture32.join();
                                CompletableFuture completableFuture34 = this.walletPut(string, true, true);
                                if (!completableFuture34.isDone()) {
                                    CompletableFuture completableFuture35 = completableFuture34;
                                    return ((CompletableFuture)completableFuture35.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$run(this, completableFuture17, n, string, completableFuture35, null, null, n2, 17, arg_0));
                                }
                                n2 = ((Boolean)completableFuture34.join()).booleanValue() ? 1 : 0;
                            } else {
                                CompletableFuture completableFuture36 = this.walletPut(string, false, false);
                                if (!completableFuture36.isDone()) {
                                    CompletableFuture completableFuture37 = completableFuture36;
                                    return ((CompletableFuture)completableFuture37.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$run(this, completableFuture17, n, string, completableFuture37, null, null, n2, 18, arg_0));
                                }
                                n2 = ((Boolean)completableFuture36.join()).booleanValue();
                                CompletableFuture completableFuture38 = this.walletsSubmitShipping(string);
                                if (!completableFuture38.isDone()) {
                                    CompletableFuture completableFuture39 = completableFuture38;
                                    return ((CompletableFuture)completableFuture39.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$run(this, completableFuture17, n, string, completableFuture39, null, null, n2, 19, arg_0));
                                }
                                completableFuture38.join();
                            }
                        } else {
                            CompletableFuture completableFuture40 = this.atcAJAX(this.instanceSignal, false);
                            if (!completableFuture40.isDone()) {
                                CompletableFuture completableFuture41 = completableFuture40;
                                return ((CompletableFuture)completableFuture41.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$run(this, completableFuture17, n, string, completableFuture41, null, null, n2, 20, arg_0));
                            }
                            completableFuture40.join();
                            if (ThreadLocalRandom.current().nextBoolean()) {
                                CompletableFuture completableFuture42 = this.genCheckoutURL(ThreadLocalRandom.current().nextBoolean());
                                if (!completableFuture42.isDone()) {
                                    CompletableFuture completableFuture43 = completableFuture42;
                                    return ((CompletableFuture)completableFuture43.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$run(this, completableFuture17, n, string, completableFuture43, null, null, n2, 21, arg_0));
                                }
                                completableFuture42.join();
                            } else {
                                CompletableFuture completableFuture44 = this.genCheckoutURLViaCart();
                                if (!completableFuture44.isDone()) {
                                    CompletableFuture completableFuture45 = completableFuture44;
                                    return ((CompletableFuture)completableFuture45.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$run(this, completableFuture17, n, string, completableFuture45, null, null, n2, 22, arg_0));
                                }
                                completableFuture44.join();
                            }
                            Analytics.carts.increment();
                            CompletableFuture completableFuture46 = this.walletsSubmitShipping(string);
                            if (!completableFuture46.isDone()) {
                                CompletableFuture completableFuture47 = completableFuture46;
                                return ((CompletableFuture)completableFuture47.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$run(this, completableFuture17, n, string, completableFuture47, null, null, n2, 23, arg_0));
                            }
                            completableFuture46.join();
                        }
                    } else {
                        CompletableFuture completableFuture48 = this.walletPut(string, true, false);
                        if (!completableFuture48.isDone()) {
                            CompletableFuture completableFuture49 = completableFuture48;
                            return ((CompletableFuture)completableFuture49.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$run(this, completableFuture17, n, string, completableFuture49, null, null, n2, 24, arg_0));
                        }
                        n2 = ((Boolean)completableFuture48.join()).booleanValue();
                        Analytics.carts.increment();
                    }
                    if (n2 == 0) {
                        CompletableFuture completableFuture50 = this.getShippingPage(string);
                        if (!completableFuture50.isDone()) {
                            CompletableFuture completableFuture51 = completableFuture50;
                            return ((CompletableFuture)completableFuture51.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$run(this, completableFuture17, n, string, completableFuture51, null, null, n2, 25, arg_0));
                        }
                        completableFuture50.join();
                        CompletableFuture completableFuture52 = this.submitShipping(string);
                        if (!completableFuture52.isDone()) {
                            CompletableFuture completableFuture53 = completableFuture52;
                            return ((CompletableFuture)completableFuture53.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$run(this, completableFuture17, n, string, completableFuture53, null, null, n2, 26, arg_0));
                        }
                        completableFuture52.join();
                        CompletableFuture completableFuture54 = this.getProcessingPage(string, false);
                        if (!completableFuture54.isDone()) {
                            CompletableFuture completableFuture55 = completableFuture54;
                            return ((CompletableFuture)completableFuture55.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$run(this, completableFuture17, n, string, completableFuture55, null, null, n2, 27, arg_0));
                        }
                        completableFuture54.join();
                    } else {
                        CompletableFuture completableFuture56 = completableFuture17;
                        if (!completableFuture56.isDone()) {
                            CompletableFuture completableFuture57 = completableFuture56;
                            return ((CompletableFuture)completableFuture57.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$run(this, completableFuture17, n, string, completableFuture57, null, null, n2, 28, arg_0));
                        }
                        completableFuture56.join();
                    }
                    do {
                        CompletableFuture completableFuture58 = this.processPayment(string);
                        if (!completableFuture58.isDone()) {
                            CompletableFuture completableFuture59 = completableFuture58;
                            return ((CompletableFuture)completableFuture59.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$run(this, completableFuture17, n, string, completableFuture59, null, null, n2, 29, arg_0));
                        }
                        completableFuture58.join();
                        CompletableFuture completableFuture60 = this.checkOrderAPI(string);
                        if (!completableFuture60.isDone()) {
                            CompletableFuture completableFuture61 = completableFuture60;
                            return ((CompletableFuture)completableFuture61.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$run(this, completableFuture17, n, string, completableFuture61, null, null, n2, 30, arg_0));
                        }
                        jsonObject = (JsonObject)completableFuture60.join();
                        object = Shopify.parseDecline(jsonObject);
                        if (object == null || ((String)object).equals("success")) continue;
                        Analytics.failure((String)object, (Task)this.task, (JsonObject)jsonObject, (String)this.api.proxyStringSafe());
                        this.logger.info("Checkout fail -> " + (String)object);
                    } while (object != null && !((String)object).equals("success"));
                    this.logger.info("Successfully checked out.");
                    Analytics.success((Task)this.task, (JsonObject)jsonObject, (String)this.api.proxyStringSafe());
                    break;
                }
                catch (Throwable throwable) {
                    this.logger.error("Error handling creation: {}", (Object)throwable.getMessage());
                    this.setAttributes();
                }
            }
            return null;
        }
        catch (Exception exception) {
            this.logger.error("Task interrupted: " + exception.getMessage());
        }
        return null;
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$send(Shopify var0, HttpRequest var1_1, Object var2_2, int var3_3, CompletableFuture var4_4, int var5_6, Object var6_8) {
        switch (var5_6) {
            case 0: {
                var3_3 = 0;
                block7: while (var3_3++ < 0x7FFFFFFF) {
                    if (var0.running == false) return CompletableFuture.completedFuture(null);
                    try {
                        if (var2_2 != null) ** GOTO lbl15
                        v0 = Request.send((HttpRequest)var1_1);
                        if (!v0.isDone()) {
                            var6_8 = v0;
                            return var6_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$send(io.trickle.task.sites.shopify.Shopify io.vertx.ext.web.client.HttpRequest java.lang.Object int java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (HttpRequest)var1_1, (Object)var2_2, (int)var3_3, (CompletableFuture)var6_8, (int)1));
                        }
lbl12:
                        // 3 sources

                        while (true) {
                            v1 = (HttpResponse)v0.join();
                            ** GOTO lbl21
                            break;
                        }
lbl15:
                        // 1 sources

                        v2 = Request.send((HttpRequest)var1_1, (Object)var2_2);
                        if (!v2.isDone()) {
                            var6_8 = v2;
                            return var6_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$send(io.trickle.task.sites.shopify.Shopify io.vertx.ext.web.client.HttpRequest java.lang.Object int java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (HttpRequest)var1_1, (Object)var2_2, (int)var3_3, (CompletableFuture)var6_8, (int)2));
                        }
lbl19:
                        // 3 sources

                        while (true) {
                            v1 = (HttpResponse)v2.join();
lbl21:
                            // 2 sources

                            if ((var4_4 = v1) == null) continue block7;
                            return CompletableFuture.completedFuture(var4_4);
                        }
                    }
                    catch (Exception var4_5) {
                        var5_7 = var4_5.getMessage();
                        if (var5_7.contains("timeout period")) {
                            var0.logger.error("Request timeout. No response");
                            continue;
                        }
                        var0.logger.error("Network Error [{}]", (Object)var4_5.getMessage());
                    }
                }
                return CompletableFuture.completedFuture(null);
            }
            case 1: {
                v0 = var4_4;
                ** continue;
            }
            case 2: {
                v2 = var4_4;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    public static CompletableFuture async$visitHomepage(Shopify var0, int var1_1, HttpRequest var2_2, CompletableFuture var3_4, Throwable var4_5, int var5_6, Object var6_7) {
        switch (var5_6) {
            case 0: {
                var1_1 = 0;
                var0.logger.info("Visiting homepage");
                block7: while (var0.running != false) {
                    if (var1_1++ >= 0x7FFFFFFF) return CompletableFuture.completedFuture(null);
                    try {
                        var2_2 /* !! */  = var0.api.homepage();
                        v0 = Request.send((HttpRequest)var2_2 /* !! */ );
                        if (!v0.isDone()) {
                            var4_5 = v0;
                            return var4_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$visitHomepage(io.trickle.task.sites.shopify.Shopify int io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (int)var1_1, (HttpRequest)var2_2 /* !! */ , (CompletableFuture)var4_5, null, (int)1));
                        }
lbl13:
                        // 3 sources

                        while (true) {
                            var3_4 = (HttpResponse)v0.join();
                            if (var3_4 == null) continue block7;
                            return CompletableFuture.completedFuture(var3_4.bodyAsString());
                        }
                    }
                    catch (Throwable var2_3) {
                        var0.logger.error("Failed visiting homepage: {}", (Object)var2_3.getMessage());
                        v1 = super.randomSleep(3000);
                        if (!v1.isDone()) {
                            var4_5 = v1;
                            return var4_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$visitHomepage(io.trickle.task.sites.shopify.Shopify int io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (int)var1_1, null, (CompletableFuture)var4_5, (Throwable)var2_3, (int)2));
                        }
lbl23:
                        // 3 sources

                        while (true) {
                            v1.join();
                            continue block7;
                            break;
                        }
                    }
                }
                return CompletableFuture.completedFuture(null);
            }
            case 1: {
                v0 = var3_4;
                ** continue;
            }
            case 2: {
                v1 = var3_4;
                var2_2 /* !! */  = var4_5;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture waitTilCartCookie() {
        this.logger.info("Setting up cart...");
        int n = 0;
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            HttpRequest httpRequest = this.api.updateJS();
            CompletableFuture completableFuture = this.send(httpRequest, Buffer.buffer((String)"{}"));
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$waitTilCartCookie(this, n, httpRequest, completableFuture2, null, 1, arg_0));
            }
            HttpResponse httpResponse = (HttpResponse)completableFuture.join();
            if (this.api.getCookies().contains("cart")) {
                return CompletableFuture.completedFuture(httpResponse.statusCode() == 401);
            }
            this.logger.info("Unable to properly setup cart {}", (Object)httpResponse.statusCode());
            CompletableFuture completableFuture3 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
            if (!completableFuture3.isDone()) {
                CompletableFuture completableFuture4 = completableFuture3;
                return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$waitTilCartCookie(this, n, httpRequest, completableFuture4, httpResponse, 2, arg_0));
            }
            completableFuture3.join();
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture getProcessingPage(String string, boolean bl) {
        if (this.api.isFirstProcessingPageVisit) {
            this.api.setInstock();
        }
        if (!bl) {
            this.logger.info("Checking billing...");
        }
        int n = 0;
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            try {
                int n2;
                HttpRequest httpRequest = this.api.paymentPage(string);
                CompletableFuture completableFuture = this.safeCompleteRequest(httpRequest, "checking billing");
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$getProcessingPage(this, string, (int)(bl ? 1 : 0), n, httpRequest, completableFuture2, null, 0, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 200) {
                    n2 = this.api.checkIsOOS();
                    boolean bl2 = this.api.markProcessingPageAsVisited();
                    String string2 = httpResponse.bodyAsString();
                    if (this.paymentGateway == null) {
                        this.updateGateway(string2);
                    }
                    this.updateCheckoutParams(string2);
                    if (this.updatePrice(string2)) return CompletableFuture.completedFuture(string2);
                    if (n2 != 0) return CompletableFuture.completedFuture(string2);
                    if (!this.api.getCookies().contains("_shopify_checkpoint")) return CompletableFuture.completedFuture(string2);
                    this.logger.info("Calculating taxes...");
                    continue;
                }
                if (httpResponse.statusCode() != 302 && httpResponse.statusCode() != 301) continue;
                n2 = this.isEarly && !this.api.getCookies().contains("_shopify_checkpoint") ? 1 : 0;
                CompletableFuture completableFuture2 = this.handleRedirect(httpResponse, n2 != 0);
                if (!completableFuture2.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture2;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$getProcessingPage(this, string, (int)(bl ? 1 : 0), n, httpRequest, completableFuture4, httpResponse, n2, null, null, 2, arg_0));
                }
                Boolean bl3 = (Boolean)completableFuture2.join();
                if (n2 != 0 && bl3 == null) {
                    bl3 = false;
                }
                if (bl3 == null) {
                    return CompletableFuture.completedFuture(null);
                }
                if (bl3.booleanValue()) {
                    this.logger.info("Unknown redirect. Handling..");
                    CompletableFuture completableFuture3 = VertxUtil.randomSleep((long)this.task.getMonitorDelay());
                    if (!completableFuture3.isDone()) {
                        CompletableFuture completableFuture6 = completableFuture3;
                        return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$getProcessingPage(this, string, (int)(bl ? 1 : 0), n, httpRequest, completableFuture6, httpResponse, n2, bl3, null, 3, arg_0));
                    }
                    completableFuture3.join();
                    continue;
                }
                if (n2 == 0 || this.cpMonitor.isDone()) continue;
                if (!this.isShippingSubmittedWithWallets) {
                    CompletableFuture completableFuture4 = this.walletsSubmitShipping(string);
                    if (!completableFuture4.isDone()) {
                        CompletableFuture completableFuture8 = completableFuture4;
                        return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$getProcessingPage(this, string, (int)(bl ? 1 : 0), n, httpRequest, completableFuture8, httpResponse, n2, bl3, null, 4, arg_0));
                    }
                    completableFuture4.join();
                }
                this.logger.info("Waiting for release [early]");
                CompletableFuture<Object> completableFuture5 = CompletableFuture.anyOf(VertxUtil.randomSignalSleep((String)"waitForCPPreload", (long)this.task.getMonitorDelay()), this.cpMonitor);
                if (!completableFuture5.isDone()) {
                    CompletableFuture<Object> completableFuture10 = completableFuture5;
                    return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$getProcessingPage(this, string, (int)(bl ? 1 : 0), n, httpRequest, completableFuture10, httpResponse, n2, bl3, null, 5, arg_0));
                }
                completableFuture5.join();
                if (this.isCPMonitorTriggered.compareAndSet(true, false) && !this.cpMonitor.isDone()) {
                    this.logger.info("Checkpoint detected. Waiting for presolve [EARLY-PRE]");
                    CompletableFuture<Void> completableFuture6 = this.cpMonitor;
                    if (!completableFuture6.isDone()) {
                        CompletableFuture<Void> completableFuture12 = completableFuture6;
                        return ((CompletableFuture)completableFuture12.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$getProcessingPage(this, string, (int)(bl ? 1 : 0), n, httpRequest, completableFuture12, httpResponse, n2, bl3, null, 6, arg_0));
                    }
                    completableFuture6.join();
                }
                this.api.setInstock();
            }
            catch (Throwable throwable) {
                this.logger.error("Error checking billing: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = VertxUtil.randomSleep((long)3000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture13 = completableFuture;
                    return ((CompletableFuture)completableFuture13.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$getProcessingPage(this, string, (int)(bl ? 1 : 0), n, null, completableFuture13, null, 0, null, throwable, 7, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$handleQueueOld(Shopify var0, int var1_1, HttpRequest var2_2, CompletableFuture var3_4, HttpResponse var4_5, Throwable var5_6, int var6_7, Object var7_8) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [7[CATCHBLOCK]], but top level block is 11[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public MultipartForm changeForm() {
        MultipartForm multipartForm = MultipartForm.create();
        multipartForm.attribute("quantity", "0");
        multipartForm.attribute("line", "1");
        return multipartForm;
    }

    public CompletableFuture genAcc() {
        Account account = Shopify.rotateAccount();
        CompletableFuture completableFuture = Shopify.initHarvesters();
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$genAcc(this, account, completableFuture2, null, 0, null, null, null, null, null, 1, arg_0));
        }
        completableFuture.join();
        MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
        multiMap.set("form_type", "create_customer");
        multiMap.set("utf8", "\u2713");
        multiMap.set("customer[first_name]", this.task.getProfile().getFirstName());
        multiMap.set("customer[last_name]", this.task.getProfile().getLastName());
        multiMap.set("customer[email]", account.getUser().replace("@", "+" + ThreadLocalRandom.current().nextInt(Short.MAX_VALUE) + "@"));
        multiMap.set("customer[password]", account.getPass());
        this.logger.info("Creating account");
        int n = 0;
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            MultiMap multiMap2 = multiMap;
            CompletableFuture completableFuture3 = TokenController.solveBasicCaptcha((String)("https://" + this.api.getSiteURL() + "/account/register"));
            if (!completableFuture3.isDone()) {
                CompletableFuture completableFuture4 = completableFuture3;
                String string = "recaptcha-v3-token";
                MultiMap multiMap3 = multiMap2;
                return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$genAcc(this, account, completableFuture4, multiMap, n, multiMap3, string, null, null, null, 2, arg_0));
            }
            multiMap2.set("recaptcha-v3-token", ((CaptchaToken)completableFuture3.join()).getToken());
            try {
                HttpRequest httpRequest = this.api.genAcc();
                CompletableFuture completableFuture5 = Request.send((HttpRequest)httpRequest, (MultiMap)multiMap);
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$genAcc(this, account, completableFuture6, multiMap, n, null, null, httpRequest, null, null, 3, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture5.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 302 || httpResponse.statusCode() == 301) {
                    CompletableFuture completableFuture7 = this.handleRedirect(httpResponse, false);
                    if (!completableFuture7.isDone()) {
                        CompletableFuture completableFuture8 = completableFuture7;
                        return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$genAcc(this, account, completableFuture8, multiMap, n, null, null, httpRequest, httpResponse, null, 4, arg_0));
                    }
                    if (((Boolean)completableFuture7.join()).booleanValue()) {
                        return CompletableFuture.completedFuture(null);
                    }
                }
                CompletableFuture completableFuture9 = VertxUtil.sleep((long)this.task.getRetryDelay());
                if (!completableFuture9.isDone()) {
                    CompletableFuture completableFuture10 = completableFuture9;
                    return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$genAcc(this, account, completableFuture10, multiMap, n, null, null, httpRequest, httpResponse, null, 5, arg_0));
                }
                completableFuture9.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error genning account: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture11 = super.randomSleep(3000);
                if (!completableFuture11.isDone()) {
                    CompletableFuture completableFuture12 = completableFuture11;
                    return ((CompletableFuture)completableFuture12.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$genAcc(this, account, completableFuture12, multiMap, n, null, null, null, null, throwable, 6, arg_0));
                }
                completableFuture11.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$submitPassword(Shopify var0, int var1_1, HttpRequest var2_2, CompletableFuture var3_4, HttpResponse var4_5, int var5_8, Exception var6_13, int var7_14, Object var8_15) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [8[CATCHBLOCK]], but top level block is 12[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$checkOrderAPI(Shopify var0, String var1_1, CompletableFuture var2_2, int var3_4, HttpRequest var4_8, HttpResponse var5_9, Throwable var6_10, int var7_11, Object var8_12) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [8[CATCHBLOCK]], but top level block is 13[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$waitTilCartCookie(Shopify var0, int var1_1, HttpRequest var2_2, CompletableFuture var3_3, HttpResponse var4_4, int var5_5, Object var6_6) {
        switch (var5_5) {
            case 0: {
                var0.logger.info("Setting up cart...");
                var1_1 = 0;
lbl5:
                // 2 sources

                while (true) {
                    if (var0.running == false) return CompletableFuture.completedFuture(null);
                    if (var1_1++ >= 0x7FFFFFFF) return CompletableFuture.completedFuture(null);
                    var2_2 = var0.api.updateJS();
                    v0 = var0.send(var2_2, Buffer.buffer((String)"{}"));
                    if (!v0.isDone()) {
                        var4_4 = v0;
                        return var4_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$waitTilCartCookie(io.trickle.task.sites.shopify.Shopify int io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (int)var1_1, (HttpRequest)var2_2, (CompletableFuture)var4_4, null, (int)1));
                    }
                    ** GOTO lbl16
                    break;
                }
            }
            case 1: {
                v0 = var3_3;
lbl16:
                // 2 sources

                var3_3 = (HttpResponse)v0.join();
                if (var0.api.getCookies().contains("cart")) {
                    return CompletableFuture.completedFuture(var3_3.statusCode() == 401);
                }
                var0.logger.info("Unable to properly setup cart {}", (Object)var3_3.statusCode());
                v1 = VertxUtil.randomSleep((long)var0.task.getRetryDelay());
                if (!v1.isDone()) {
                    var4_4 = v1;
                    return var4_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$waitTilCartCookie(io.trickle.task.sites.shopify.Shopify int io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (int)var1_1, (HttpRequest)var2_2, (CompletableFuture)var4_4, (HttpResponse)var3_3, (int)2));
                }
                ** GOTO lbl28
            }
            case 2: {
                v1 = var3_3;
                var3_3 = var4_4;
lbl28:
                // 2 sources

                v1.join();
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture handleOOS(String string) {
        int n = 0;
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            try {
                Object object;
                HttpRequest httpRequest = this.api.oosPage(string);
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$handleOOS(this, string, n, httpRequest, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 200) {
                    object = httpResponse.bodyAsString();
                    if (this.task.getSite() != Site.KITH) {
                        this.updateGateway((String)object);
                    }
                    this.updateShippingRate((String)object);
                    this.updateCheckoutParams((String)object);
                    return CompletableFuture.completedFuture(object);
                }
                if (httpResponse.statusCode() == 302 || httpResponse.statusCode() == 301) {
                    CompletableFuture completableFuture3 = this.handleRedirect(httpResponse, true);
                    if (!completableFuture3.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture3;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$handleOOS(this, string, n, httpRequest, completableFuture4, httpResponse, null, 2, arg_0));
                    }
                    object = (Boolean)completableFuture3.join();
                    if (object == null) {
                        return CompletableFuture.completedFuture(null);
                    }
                    if (((Boolean)object).booleanValue()) return CompletableFuture.completedFuture(null);
                    continue;
                }
                this.logger.warn("Error handling: status: '{}'", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture5 = VertxUtil.sleep((long)this.task.getRetryDelay());
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$handleOOS(this, string, n, httpRequest, completableFuture6, httpResponse, null, 3, arg_0));
                }
                completableFuture5.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error handling: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = super.randomSleep(3000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$handleOOS(this, string, n, null, completableFuture7, null, throwable, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture shippingAndBillingPut(String string) {
        this.logger.info("Submitting contact [API]...");
        int n = 0;
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            try {
                HttpRequest httpRequest = this.api.walletsPutDetails(string);
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest, (Buffer)this.contactAPIForm().toBuffer());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$shippingAndBillingPut(this, string, n, httpRequest, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 200) {
                    return CompletableFuture.completedFuture((Buffer)httpResponse.body());
                }
                if (httpResponse.statusCode() == 202) {
                    if (!httpResponse.bodyAsString().contains(this.task.getProfile().getAddress1())) continue;
                    return CompletableFuture.completedFuture((Buffer)httpResponse.body());
                }
                if (httpResponse.statusCode() == 409 && n < 5) continue;
                this.logger.warn("Retrying submitting contact [API]: status: '{}'", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture3 = VertxUtil.sleep((long)this.task.getRetryDelay());
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$shippingAndBillingPut(this, string, n, httpRequest, completableFuture4, httpResponse, null, 2, arg_0));
                }
                completableFuture3.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error submitting contact [API]: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = super.randomSleep(3000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture5 = completableFuture;
                    return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$shippingAndBillingPut(this, string, n, null, completableFuture5, null, throwable, 3, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture genPaymentToken() {
        this.paymentToken = new CompletableFuture();
        if (this.isCod) {
            this.paymentToken.complete(new PaymentTokenSupplier(""));
            return CompletableFuture.completedFuture(null);
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("credit_card", (Object)new JsonObject().put("number", (Object)this.task.getProfile().splitCard()).put("name", (Object)(Task.randomizeCase((String)this.task.getProfile().getFirstName()) + " " + Task.randomizeCase((String)this.task.getProfile().getLastName()))).put("month", (Object)Integer.parseInt(this.task.getProfile().getExpiryMonth())).put("year", (Object)Integer.parseInt(this.task.getProfile().getExpiryYear())).put("verification_value", (Object)this.task.getProfile().getCvv()));
        jsonObject.put("payment_session_scope", (Object)this.api.getSiteURL());
        int n = 0;
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            WebClient webClient = VertxSingleton.INSTANCE.getProxyPoolClient().getClient();
            try {
                HttpRequest httpRequest = this.api.paymentToken(webClient).timeout((long)ThreadLocalRandom.current().nextInt(3500, 4250));
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest, (Buffer)jsonObject.toBuffer());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$genPaymentToken(this, jsonObject, n, webClient, httpRequest, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 200) {
                    this.paymentToken.complete(new PaymentTokenSupplier(httpResponse.bodyAsJsonObject().getString("id")));
                    return CompletableFuture.completedFuture(null);
                }
                CompletableFuture completableFuture3 = VertxUtil.hardCodedSleep((long)100L);
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$genPaymentToken(this, jsonObject, n, webClient, httpRequest, completableFuture4, httpResponse, null, 2, arg_0));
                }
                completableFuture3.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Failed generating payment token: {}", (Object)(throwable.getMessage().contains("timeout period of") ? "Timeout" : throwable.getMessage()));
                VertxSingleton.INSTANCE.getProxyPoolClient().removeBad(webClient);
                CompletableFuture completableFuture = VertxUtil.hardCodedSleep((long)100L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture5 = completableFuture;
                    return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$genPaymentToken(this, jsonObject, n, webClient, null, completableFuture5, null, throwable, 3, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture getCPHtml(boolean bl) {
        this.logger.info("Visiting checkpoint");
        int n = 0;
        while (n++ < Integer.MAX_VALUE) {
            if (!this.api.getWebClient().isActive()) return CompletableFuture.completedFuture(null);
            try {
                HttpRequest httpRequest = this.api.checkpointPage(bl);
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$getCPHtml(this, (int)(bl ? 1 : 0), n, httpRequest, completableFuture2, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null || httpResponse.statusCode() != 200) continue;
                return CompletableFuture.completedFuture(httpResponse.bodyAsString());
            }
            catch (Throwable throwable) {
                if (!throwable.getMessage().contains("Connection was closed")) {
                    this.logger.error("Failed hitting CP: {}", (Object)throwable.getMessage());
                }
                CompletableFuture completableFuture = VertxUtil.randomSleep((long)this.task.getRetryDelay());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture3 = completableFuture;
                    return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$getCPHtml(this, (int)(bl ? 1 : 0), n, null, completableFuture3, throwable, 2, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    public static CompletableFuture async$genCheckoutURLIgnorePassword(Shopify var0, int var1_1, int var2_2, HttpRequest var3_3, CompletableFuture var4_5, HttpResponse var5_6, Throwable var6_7, int var7_8, Object var8_9) {
        switch (var7_8) {
            case 0: {
                var2_2 = 0;
                var0.logger.info("Generating checkout URL...");
                block10: while (var0.running != false) {
                    if (var2_2++ >= 0x7FFFFFFF) return CompletableFuture.completedFuture(null);
                    try {
                        var3_3 /* !! */  = var0.api.emptyCheckout((boolean)var1_1);
                        if (var1_1 == 0) ** GOTO lbl17
                        v0 = Request.send((HttpRequest)var3_3 /* !! */ , (Buffer)Buffer.buffer((String)"{}"));
                        if (!v0.isDone()) {
                            var5_6 = v0;
                            return var5_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$genCheckoutURLIgnorePassword(io.trickle.task.sites.shopify.Shopify int int io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (int)var1_1, (int)var2_2, (HttpRequest)var3_3 /* !! */ , (CompletableFuture)var5_6, null, null, (int)1));
                        }
lbl14:
                        // 3 sources

                        while (true) {
                            v1 = (HttpResponse)v0.join();
                            ** GOTO lbl23
                            break;
                        }
lbl17:
                        // 1 sources

                        v2 = Request.send((HttpRequest)var3_3 /* !! */ );
                        if (!v2.isDone()) {
                            var5_6 = v2;
                            return var5_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$genCheckoutURLIgnorePassword(io.trickle.task.sites.shopify.Shopify int int io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (int)var1_1, (int)var2_2, (HttpRequest)var3_3 /* !! */ , (CompletableFuture)var5_6, null, null, (int)2));
                        }
lbl21:
                        // 3 sources

                        while (true) {
                            v1 = (HttpResponse)v2.join();
lbl23:
                            // 2 sources

                            if ((var4_5 = v1) == null) continue block10;
                            if (var4_5.statusCode() != 302 && var4_5.statusCode() != 301) ** GOTO lbl32
                            v3 = var0.handleRedirect((HttpResponse)var4_5, true);
                            if (!v3.isDone()) {
                                var5_6 = v3;
                                return var5_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$genCheckoutURLIgnorePassword(io.trickle.task.sites.shopify.Shopify int int io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (int)var1_1, (int)var2_2, (HttpRequest)var3_3 /* !! */ , (CompletableFuture)var5_6, (HttpResponse)var4_5, null, (int)3));
                            }
lbl29:
                            // 3 sources

                            while (true) {
                                if (((Boolean)v3.join()).booleanValue()) {
                                    return CompletableFuture.completedFuture(var4_5.getHeader("location"));
                                }
lbl32:
                                // 3 sources

                                if (var4_5.statusCode() != 302) {
                                    var0.logger.warn("Failed generating checkout URL: status: '{}'", (Object)var4_5.statusCode());
                                    v4 = VertxUtil.sleep((long)var0.task.getRetryDelay());
                                    if (!v4.isDone()) {
                                        var5_6 = v4;
                                        return var5_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$genCheckoutURLIgnorePassword(io.trickle.task.sites.shopify.Shopify int int io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (int)var1_1, (int)var2_2, (HttpRequest)var3_3 /* !! */ , (CompletableFuture)var5_6, (HttpResponse)var4_5, null, (int)4));
                                    }
lbl38:
                                    // 3 sources

                                    while (true) {
                                        v4.join();
                                        continue block10;
                                        break;
                                    }
                                }
                                if (!var4_5.getHeader("location").contains("password")) continue block10;
                                return CompletableFuture.completedFuture(null);
                            }
                            break;
                        }
                    }
                    catch (Throwable var3_4) {
                        var0.logger.error("Failed generating checkout URL: {}", (Object)var3_4.getMessage());
                        v5 = super.randomSleep(3000);
                        if (!v5.isDone()) {
                            var5_6 = v5;
                            return var5_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$genCheckoutURLIgnorePassword(io.trickle.task.sites.shopify.Shopify int int io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (int)var1_1, (int)var2_2, null, (CompletableFuture)var5_6, null, (Throwable)var3_4, (int)5));
                        }
lbl50:
                        // 3 sources

                        while (true) {
                            v5.join();
                            continue block10;
                            break;
                        }
                    }
                }
                return CompletableFuture.completedFuture(null);
            }
            case 1: {
                v0 = var4_5;
                ** continue;
            }
            case 2: {
                v2 = var4_5;
                ** continue;
            }
            case 3: {
                v3 = var4_5;
                var4_5 = var5_6;
                ** continue;
            }
            case 4: {
                v4 = var4_5;
                var4_5 = var5_6;
                ** continue;
            }
            case 5: {
                v5 = var4_5;
                var3_3 /* !! */  = var6_7;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture aiAPIReq(HttpRequest httpRequest, JsonObject jsonObject) {
        int n = 0;
        do {
            try {
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest, (JsonObject)jsonObject);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$aiAPIReq(this, httpRequest, jsonObject, n, completableFuture2, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse != null && httpResponse.statusCode() == 200) {
                    return CompletableFuture.completedFuture(httpResponse);
                }
            }
            catch (Exception exception) {
                System.out.println("AI req poll err " + exception.getMessage().replace("34.66.51.234", "remote"));
            }
            CompletableFuture completableFuture = VertxUtil.sleep((long)250L);
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture3 = completableFuture;
                return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$aiAPIReq(this, httpRequest, jsonObject, n, completableFuture3, 2, arg_0));
            }
            completableFuture.join();
        } while (++n < Integer.MAX_VALUE);
        return CompletableFuture.completedFuture(null);
    }

    public void updateGateway(String string) {
        Matcher matcher = GATEWAY_CARD_PATTERN.matcher(string);
        if (matcher.find()) {
            this.paymentGateway = matcher.group(1);
        } else {
            this.logger.debug("No gateway found");
        }
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    public static CompletableFuture async$processingAPIForm(Shopify var0, JsonObject var1_1, JsonObject var2_2, String var3_3, CompletableFuture var4_4, JsonObject var5_5, String var6_6, JsonObject var7_7, String var8_8, int var9_9, Object var10_10) {
        switch (var9_9) {
            case 0: {
                var1_1 = new JsonObject();
                var1_1.put("complete", (Object)"1");
                var1_1.put("field_type", (Object)"hidden");
                v0 = var1_1;
                v1 = "s";
                v2 = var0.paymentToken;
                if (!v2.isDone()) {
                    var4_4 = v2;
                    var3_3 = v1;
                    var2_2 = v0;
                    return var4_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$processingAPIForm(io.trickle.task.sites.shopify.Shopify io.vertx.core.json.JsonObject io.vertx.core.json.JsonObject java.lang.String java.util.concurrent.CompletableFuture io.vertx.core.json.JsonObject java.lang.String io.vertx.core.json.JsonObject java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (JsonObject)var1_1, (JsonObject)var2_2, (String)var3_3, (CompletableFuture)var4_4, null, null, null, null, (int)1));
                }
                ** GOTO lbl21
            }
            case 1: {
                v0 = var2_2;
                v1 = var3_3;
                v2 = var4_4;
lbl21:
                // 2 sources

                v0.put(v1, (Object)((PaymentTokenSupplier)v2.join()).get());
                if (var0.hasShippingAlreadySubmittedAPI != false) return CompletableFuture.completedFuture(var1_1);
                v3 = var1_1;
                v4 = "checkout";
                v5 = new JsonObject();
                v6 = "shipping_rate";
                v7 = new JsonObject();
                v8 = "id";
                v9 = var0.shippingRate;
                if (!v9.isDone()) {
                    var8_8 = v9;
                    var7_7 /* !! */  = v8;
                    var6_6 = v7;
                    var5_5 /* !! */  = v6;
                    var4_4 = v5;
                    var3_3 = v4;
                    var2_2 = v3;
                    return var8_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$processingAPIForm(io.trickle.task.sites.shopify.Shopify io.vertx.core.json.JsonObject io.vertx.core.json.JsonObject java.lang.String java.util.concurrent.CompletableFuture io.vertx.core.json.JsonObject java.lang.String io.vertx.core.json.JsonObject java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (JsonObject)var1_1, (JsonObject)var2_2, (String)var3_3, (CompletableFuture)var8_8, (JsonObject)var4_4, (String)var5_5 /* !! */ , (JsonObject)var6_6, (String)var7_7 /* !! */ , (int)2));
                }
                ** GOTO lbl49
            }
            case 2: {
                v3 = var2_2;
                v4 = var3_3;
                v5 = var5_5 /* !! */ ;
                v6 = var6_6;
                v7 = var7_7 /* !! */ ;
                v8 = var8_8;
                v9 = var4_4;
lbl49:
                // 2 sources

                v3.put(v4, (Object)v5.put(v6, (Object)v7.put(v8, (Object)((ShippingRateSupplier)v9.join()).get())));
                return CompletableFuture.completedFuture(var1_1);
            }
        }
        throw new IllegalArgumentException();
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$getShippingRateAPI(Shopify var0, String var1_1, int var2_2, HttpRequest var3_3, CompletableFuture var4_5, HttpResponse var5_6, JsonObject var6_7, JsonArray var7_8, Exception var8_9, int var9_10, Object var10_11) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [9[CATCHBLOCK]], but top level block is 15[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$getCheckoutURL(Shopify var0, String var1_1, int var2_2, HttpRequest var3_3, CompletableFuture var4_5, HttpResponse var5_6, Boolean var6_7, Exception var7_8, int var8_9, Object var9_10) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [12[CATCHBLOCK]], but top level block is 20[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$checkQueueThrottle(Shopify var0, int var1_1, HttpRequest var2_2, CompletableFuture var3_4, HttpResponse var4_5, Throwable var5_6, int var6_7, Object var7_8) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [7[CATCHBLOCK]], but top level block is 11[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$processPaymentFakeToken(Shopify var0, String var1_1, int var2_2, CompletableFuture var3_3, MultiMap var4_5, HttpRequest var5_6, HttpResponse var6_7, Exception var7_8, int var8_9, Object var9_10) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [9[CATCHBLOCK]], but top level block is 15[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public CompletableFuture parseNewQueueJson(JsonObject jsonObject) {
        String string = jsonObject.getJsonObject("data").getJsonObject("poll").getString("__typename");
        String string2 = jsonObject.getJsonObject("data").getJsonObject("poll").getString("token");
        this.api.getWebClient().cookieStore().removeAnyMatch("_checkout_queue_token");
        this.api.getWebClient().cookieStore().put("_checkout_queue_token", string2, this.api.getSiteURL());
        if (string.equals("PollContinue")) {
            long l = jsonObject.getJsonObject("data").getJsonObject("poll").getLong("queueEtaSeconds");
            String string3 = Instant.ofEpochSecond(Instant.now().getEpochSecond() + l).atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            String string4 = jsonObject.getJsonObject("data").getJsonObject("poll").getString("pollAfter");
            this.logger.info("Waiting in new queue. Expected wait time -> ({}) [{}][{}]", (Object)l, (Object)string3, (Object)true);
            if (this.prevTime == l) {
                CompletableFuture completableFuture = VertxUtil.hardCodedSleep((long)(this.calculatePollTime(string4) + 500L));
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$parseNewQueueJson(this, jsonObject, string, string2, l, string3, string4, completableFuture2, 1, arg_0));
                }
                completableFuture.join();
            } else {
                this.prevTime = l;
                CompletableFuture completableFuture = VertxUtil.hardCodedSleep((long)this.calculatePollTime(string4));
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture3 = completableFuture;
                    return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$parseNewQueueJson(this, jsonObject, string, string2, l, string3, string4, completableFuture3, 2, arg_0));
                }
                completableFuture.join();
            }
            CompletableFuture completableFuture = VertxUtil.hardCodedSleep((long)1250L);
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture4 = completableFuture;
                return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$parseNewQueueJson(this, jsonObject, string, string2, l, string3, string4, completableFuture4, 3, arg_0));
            }
            completableFuture.join();
            return CompletableFuture.completedFuture(false);
        }
        if (string.equals("PollComplete")) {
            this.logger.info("Passed new queue.");
            Analytics.queuePasses.increment();
            return CompletableFuture.completedFuture(true);
        }
        this.logger.info("Error with new queue. Please wait.");
        CompletableFuture completableFuture = VertxUtil.randomSleep((long)3000L);
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture5 = completableFuture;
            return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$parseNewQueueJson(this, jsonObject, string, string2, 0L, null, null, completableFuture5, 4, arg_0));
        }
        completableFuture.join();
        return CompletableFuture.completedFuture(false);
    }

    public boolean hasTrulyCarted(String string) {
        String string2 = this.api.getCookies().getCookieValue("cart_sig");
        if (string2 == null) {
            return false;
        }
        if (string != null) return !string.equals(string2);
        return true;
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$confirmClear(Shopify var0, String var1_1, int var2_2, HttpRequest var3_3, CompletableFuture var4_5, HttpResponse var5_6, REDIRECT_STATUS var6_7, Exception var7_8, int var8_9, Object var9_10) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [10[CATCHBLOCK]], but top level block is 17[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$submitShipping(Shopify var0, String var1_1, int var2_2, int var3_3, CompletableFuture var4_4, MultiMap var5_5, HttpRequest var6_7, HttpResponse var7_8, String var8_9, Throwable var9_10, int var10_11, Object var11_12) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [9[CATCHBLOCK]], but top level block is 15[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public void setProductPickupTime() {
        if (this.productPickupTime != null) return;
        this.productPickupTime = System.currentTimeMillis();
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$processFakeAsPatch(Shopify var0, String var1_1, int var2_2, CompletableFuture var3_3, MultiMap var4_5, HttpRequest var5_6, HttpResponse var6_7, Exception var7_8, int var8_9, Object var9_10) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [9[CATCHBLOCK]], but top level block is 14[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public CompletableFuture checkOrderAPI(String string) {
        this.logger.info("Checking order [API]");
        CompletableFuture completableFuture = VertxUtil.randomSleep((long)3000L);
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$checkOrderAPI(this, string, completableFuture2, 0, null, null, null, 1, arg_0));
        }
        completableFuture.join();
        int n = 0;
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            try {
                HttpRequest httpRequest = this.api.paymentStatusAPI(string);
                CompletableFuture completableFuture3 = Request.send((HttpRequest)httpRequest);
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$checkOrderAPI(this, string, completableFuture4, n, httpRequest, null, null, 2, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture3.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 200) {
                    JsonArray jsonArray = httpResponse.bodyAsJsonObject().getJsonArray("payments");
                    if (jsonArray.getJsonObject(jsonArray.size() - 1).getString("payment_processing_error_message") != null) return CompletableFuture.completedFuture(httpResponse.bodyAsJsonObject());
                    if (jsonArray.getJsonObject(jsonArray.size() - 1).getValue("transaction") != null && jsonArray.getJsonObject(jsonArray.size() - 1).getJsonObject("transaction").getValue("message") != null) {
                        return CompletableFuture.completedFuture(httpResponse.bodyAsJsonObject());
                    }
                }
                this.logger.warn("Waiting to check order [API]: status: '{}'", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture5 = VertxUtil.hardCodedSleep((long)5000L);
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$checkOrderAPI(this, string, completableFuture6, n, httpRequest, httpResponse, null, 3, arg_0));
                }
                completableFuture5.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error checking order: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture7 = VertxUtil.randomSleep((long)3000L);
                if (!completableFuture7.isDone()) {
                    CompletableFuture completableFuture8 = completableFuture7;
                    return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$checkOrderAPI(this, string, completableFuture8, n, null, null, throwable, 4, arg_0));
                }
                completableFuture7.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture checkNewQueueThrottle() {
        this.logger.info("Checking queue [NEW]...");
        int n = 0;
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            try {
                HttpRequest httpRequest = this.api.fetchNewQueueUrl();
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$checkNewQueueThrottle(this, n, httpRequest, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 200) {
                    return CompletableFuture.completedFuture(true);
                }
                if ((httpResponse.statusCode() == 302 || httpResponse.statusCode() == 301) && httpResponse.getHeader("location").contains("/checkout")) {
                    return CompletableFuture.completedFuture(false);
                }
                if (httpResponse.statusCode() == 302) {
                    this.logger.info(httpResponse.getHeader("location"));
                }
                this.logger.warn("Waiting to check queue [NEW]: status: '{}'", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture3 = VertxUtil.hardCodedSleep((long)3000L);
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$checkNewQueueThrottle(this, n, httpRequest, completableFuture4, httpResponse, null, 2, arg_0));
                }
                completableFuture3.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error checking queue: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = super.randomSleep(3000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture5 = completableFuture;
                    return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$checkNewQueueThrottle(this, n, null, completableFuture5, null, throwable, 3, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture smoothCart() {
        if (this.propertiesPair != null) {
            CompletableFuture completableFuture = this.atcUpsell(this.instanceSignal);
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$smoothCart(this, completableFuture2, 1, arg_0));
            }
            completableFuture.join();
        } else {
            CompletableFuture completableFuture = this.atcAJAX(this.instanceSignal, false);
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture3 = completableFuture;
                return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$smoothCart(this, completableFuture3, 2, arg_0));
            }
            completableFuture.join();
        }
        return CompletableFuture.completedFuture(null);
    }

    public Shopify(Task task, int n) {
        super(n);
        this.negativeKeywords = new LinkedList<String>();
        this.TEXTAREA_PARAMS = new LinkedList<String>();
        this.isCPMonitorTriggered = new AtomicBoolean(false);
        this.task = task;
        ((TaskActor)this).task = task;
        this.api = new ShopifyAPI(this.task);
        super.setClient((TaskApiClient)this.api);
        this.instanceSignal = this.task.getKeywords()[0];
        Matcher matcher = CHECK_VARIANT_PATTERN.matcher(this.instanceSignal);
        Matcher matcher2 = CHECK_EL_PATTERN.matcher(this.instanceSignal);
        if (matcher.find()) {
            this.logger.warn("Using variant -> " + this.instanceSignal);
        } else if (matcher2.find()) {
            this.isEL = true;
            this.task.getKeywords()[0] = (this.instanceSignal.contains("?") ? this.instanceSignal.split("\\?")[0] : this.instanceSignal).toLowerCase();
            this.task.getKeywords()[0] = this.task.getKeywords()[0].replaceAll("collections/.*?/", "");
            this.instanceSignal = this.task.getKeywords()[0];
            this.logger.warn("Using EL -> " + this.instanceSignal);
        } else {
            this.isKeyword = true;
            this.instanceSignal = Arrays.toString(this.task.getKeywords()).replace("[", "").replace("]", "").replace(",", "");
            this.logger.warn("Using keywords -> " + this.instanceSignal);
        }
        this.BROWSER_HEIGHT = String.valueOf(ThreadLocalRandom.current().nextInt(720, 1800));
        this.BROWSER_WIDTH = String.valueOf(ThreadLocalRandom.current().nextInt(1280, 3200));
        this.FLOW_ACTIONS = Flow.getFlow((Site)this.task.getSite()).get();
        this.MODE = SiteParser.getMode((String)this.task.getMode());
        this.setCurrency();
        this.isCod = this.task.getMode().contains("cod");
        this.isRestockMode = this.task.getMode().contains("restock");
        this.isEarly = this.task.getMode().contains("early");
        this.isExtra = this.task.getMode().contains("extra");
        this.propertiesPair = SiteParser.getProperties((Site)this.task.getSite());
        this.setAttributes();
        this.lockKey = task.getSiteUserEntry() + n % 50;
        if (lockMap.containsKey(this.lockKey)) return;
        lockMap.put(this.lockKey, VertxSingleton.INSTANCE.get().sharedData().getLocalLockWithTimeout(this.lockKey, Integer.MAX_VALUE).toCompletionStage());
    }

    public CompletableFuture checkCart() {
        int n = 0;
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            try {
                HttpRequest httpRequest = this.api.cartJS();
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$checkCart(this, n, httpRequest, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 200) {
                    if (httpResponse.bodyAsString().contains("\"item_count\":0")) {
                        return CompletableFuture.completedFuture(null);
                    }
                    this.logger.error("Please notify the developer of cart bug. Thank you!");
                    CompletableFuture completableFuture3 = VertxUtil.hardCodedSleep((long)Integer.MAX_VALUE);
                    if (!completableFuture3.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture3;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$checkCart(this, n, httpRequest, completableFuture4, httpResponse, null, 2, arg_0));
                    }
                    completableFuture3.join();
                    continue;
                }
                if (httpResponse.statusCode() == 302 || httpResponse.statusCode() == 301) {
                    CompletableFuture completableFuture5 = this.handleRedirect(httpResponse, false);
                    if (!completableFuture5.isDone()) {
                        CompletableFuture completableFuture6 = completableFuture5;
                        return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$checkCart(this, n, httpRequest, completableFuture6, httpResponse, null, 3, arg_0));
                    }
                    completableFuture5.join();
                }
                this.logger.warn("Failed checking cart: status: '{}'", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture7 = VertxUtil.sleep((long)this.task.getRetryDelay());
                if (!completableFuture7.isDone()) {
                    CompletableFuture completableFuture8 = completableFuture7;
                    return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$checkCart(this, n, httpRequest, completableFuture8, httpResponse, null, 4, arg_0));
                }
                completableFuture7.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Failed checking cart: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = super.randomSleep(3000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture9 = completableFuture;
                    return ((CompletableFuture)completableFuture9.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$checkCart(this, n, null, completableFuture9, null, throwable, 5, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture handleRedirect(HttpResponse httpResponse, boolean bl) {
        if (httpResponse.statusCode() == 401) {
            return this.handleRedirect("/password", bl);
        }
        if (httpResponse.statusCode() == 301) {
            this.api.SITE_URL = Utils.quickParseFirst((String)httpResponse.getHeader("location"), (Pattern[])new Pattern[]{SITE_URL_PATTERN});
            return CompletableFuture.completedFuture(false);
        }
        if (httpResponse.statusCode() == 302) return this.handleRedirect(httpResponse.getHeader("location"), bl);
        return CompletableFuture.completedFuture(false);
    }

    public static String parseDecline(JsonObject jsonObject) {
        if (jsonObject.encode().contains("ending the Cash on Delivery")) {
            return "success";
        }
        JsonArray jsonArray = jsonObject.getJsonArray("payments");
        JsonObject jsonObject2 = jsonArray.getJsonObject(jsonArray.size() - 1);
        JsonObject jsonObject3 = jsonObject2.getJsonObject("transaction");
        if (jsonObject3 == null) {
            String string = jsonObject2.getString("payment_processing_error_message");
            if (string == null) return "Unknown Payment Failure";
            return string;
        }
        if (!jsonObject3.getString("status").equals("success")) return jsonObject2.getJsonObject("transaction").getString("message");
        return "success";
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$checkCart(Shopify var0, int var1_1, HttpRequest var2_2, CompletableFuture var3_4, HttpResponse var4_5, Throwable var5_6, int var6_7, Object var7_8) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [9[CATCHBLOCK]], but top level block is 15[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$safeCompleteRequest(Shopify var0, HttpRequest var1_1, String var2_2, int var3_3, CompletableFuture var4_4, HttpResponse var5_6, int var6_8, Throwable var7_12, int var8_13, Object var9_14) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [7[CATCHBLOCK]], but top level block is 11[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public CompletableFuture handleQueueNew() {
        this.logger.info("Waiting in queue [NEW]...");
        int n = 0;
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            try {
                HttpRequest httpRequest = this.api.newQueue().as(BodyCodec.buffer());
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest, (Buffer)this.newQueueBody());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$handleQueueNew(this, n, httpRequest, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 200) {
                    CompletableFuture completableFuture3 = this.parseNewQueueJson(httpResponse.bodyAsJsonObject());
                    if (!completableFuture3.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture3;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$handleQueueNew(this, n, httpRequest, completableFuture4, httpResponse, null, 2, arg_0));
                    }
                    if (((Boolean)completableFuture3.join()).booleanValue()) {
                        return CompletableFuture.completedFuture(null);
                    }
                }
                if (httpResponse.statusCode() == 200) continue;
                this.logger.warn("Waiting in queue [NEW]: status: '{}'", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture5 = VertxUtil.randomSleep((long)3000L);
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$handleQueueNew(this, n, httpRequest, completableFuture6, httpResponse, null, 3, arg_0));
                }
                completableFuture5.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error waiting in queue [NEW]: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = VertxUtil.randomSleep((long)3000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$handleQueueNew(this, n, null, completableFuture7, null, throwable, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture getShippingRateAPI(String string) {
        this.logger.info("Fetching rate... [API]");
        int n = 0;
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            if (this.shippingRate.isDone()) return CompletableFuture.completedFuture(null);
            try {
                HttpRequest httpRequest = this.api.shippingRateAPI(string);
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$getShippingRateAPI(this, string, n, httpRequest, completableFuture2, null, null, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 200) {
                    JsonObject jsonObject = httpResponse.bodyAsJsonObject();
                    JsonArray jsonArray = jsonObject.getJsonArray("shipping_rates");
                    if (jsonArray.isEmpty()) {
                        throw new Exception("No shipping available. Check your shipping address / country!");
                    }
                    if (!this.shippingRate.isDone()) {
                        this.shippingRate.complete(new ShippingRateSupplier(jsonArray.getJsonObject(0).getString("id")));
                    } else {
                        CompletableFuture<ShippingRateSupplier> completableFuture3 = this.shippingRate;
                        if (!completableFuture3.isDone()) {
                            CompletableFuture<ShippingRateSupplier> completableFuture4 = completableFuture3;
                            return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$getShippingRateAPI(this, string, n, httpRequest, completableFuture4, httpResponse, jsonObject, jsonArray, null, 2, arg_0));
                        }
                        completableFuture3.join().updateRate(jsonArray.getJsonObject(0).getString("id"));
                    }
                    return CompletableFuture.completedFuture(jsonObject);
                }
                if (httpResponse.statusCode() == 202) {
                    CompletableFuture completableFuture5 = VertxUtil.randomSleep((long)100L);
                    if (!completableFuture5.isDone()) {
                        CompletableFuture completableFuture6 = completableFuture5;
                        return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$getShippingRateAPI(this, string, n, httpRequest, completableFuture6, httpResponse, null, null, null, 3, arg_0));
                    }
                    completableFuture5.join();
                    continue;
                }
                this.logger.warn("Waiting to fetch rate [API]: status: '{}'", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture7 = VertxUtil.sleep((long)this.task.getRetryDelay());
                if (!completableFuture7.isDone()) {
                    CompletableFuture completableFuture8 = completableFuture7;
                    return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$getShippingRateAPI(this, string, n, httpRequest, completableFuture8, httpResponse, null, null, null, 4, arg_0));
                }
                completableFuture7.join();
            }
            catch (Exception exception) {
                this.logger.error("Error fetching rate [API]: {}", (Object)exception.getMessage());
                CompletableFuture completableFuture = super.randomSleep(3000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture9 = completableFuture;
                    return ((CompletableFuture)completableFuture9.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$getShippingRateAPI(this, string, n, null, completableFuture9, null, null, null, exception, 5, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public void configureShippingRate() {
        if (this.shippingRate == null || this.shippingRate.isDone()) {
            this.shippingRate = new CompletableFuture();
        }
        if (this.task.getShippingRate().length() <= 3) return;
        this.shippingRate.complete(new ShippingRateSupplier(this.task.getShippingRate()));
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$initShopDetails(Shopify var0, CompletableFuture var1_1, int var2_2, Object var3_4) {
        switch (var2_2) {
            case 0: {
                v0 = var0.fetchMeta();
                if (!v0.isDone()) {
                    var2_3 = v0;
                    return var2_3.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$initShopDetails(io.trickle.task.sites.shopify.Shopify java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (CompletableFuture)var2_3, (int)1));
                }
                ** GOTO lbl10
            }
            case 1: {
                v0 = var1_1;
lbl10:
                // 2 sources

                if ((var1_1 = ((JsonObject)v0.join()).getJsonObject("paymentInstruments")) == null) return CompletableFuture.completedFuture(null);
                var0.api.setShopID("" + var1_1.getJsonObject("checkoutConfig").getNumber("shopId"));
                var0.api.setAPIToken(var1_1.getString("accessToken"));
                return CompletableFuture.completedFuture(null);
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture handleQueueOld() {
        this.logger.info("Waiting in queue [OLD]...");
        int n = 0;
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            try {
                HttpRequest httpRequest = this.api.oldQueue();
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$handleQueueOld(this, n, httpRequest, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 200) {
                    return CompletableFuture.completedFuture(null);
                }
                this.logger.warn("Waiting in queue [OLD]: status: '{}'", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture3 = VertxUtil.hardCodedSleep((long)5000L);
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$handleQueueOld(this, n, httpRequest, completableFuture4, httpResponse, null, 2, arg_0));
                }
                completableFuture3.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error waiting in queue [OLD]: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = super.randomSleep(3000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture5 = completableFuture;
                    return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$handleQueueOld(this, n, null, completableFuture5, null, throwable, 3, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture walletPut(String string, boolean bl, Boolean bl2) {
        this.logger.info("Adjusting checkout...");
        int n = 0;
        if (bl2 == null) {
            bl2 = false;
        }
        block9: while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(false);
            try {
                HttpRequest httpRequest;
                HttpRequest httpRequest2 = httpRequest = this.api.walletsPutDetails(string);
                CompletableFuture completableFuture = this.walletsCart(bl, bl2);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    HttpRequest httpRequest3 = httpRequest2;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$walletPut(this, string, (int)(bl ? 1 : 0), bl2, n, httpRequest, httpRequest3, completableFuture2, null, null, 1, arg_0));
                }
                CompletableFuture completableFuture3 = Request.send((HttpRequest)httpRequest2, (Buffer)((JsonObject)completableFuture.join()).toBuffer());
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$walletPut(this, string, (int)(bl ? 1 : 0), bl2, n, httpRequest, null, completableFuture4, null, null, 2, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture3.join();
                if (httpResponse == null) continue;
                switch (httpResponse.statusCode()) {
                    case 200: {
                        return CompletableFuture.completedFuture(true);
                    }
                    case 202: {
                        if (!httpResponse.bodyAsString().contains(this.task.getProfile().getAddress1())) continue block9;
                        return CompletableFuture.completedFuture(true);
                    }
                    case 422: {
                        if (bl2 != false) return CompletableFuture.completedFuture(false);
                        CompletableFuture completableFuture5 = this.smoothCart();
                        if (!completableFuture5.isDone()) {
                            CompletableFuture completableFuture6 = completableFuture5;
                            return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$walletPut(this, string, (int)(bl ? 1 : 0), bl2, n, httpRequest, null, completableFuture6, httpResponse, null, 3, arg_0));
                        }
                        completableFuture5.join();
                        bl2 = true;
                        continue block9;
                    }
                    case 429: {
                        if (!this.api.getCookies().contains("_checkout_queue_token")) continue block9;
                        CompletableFuture completableFuture7 = this.handleQueueNew();
                        if (!completableFuture7.isDone()) {
                            CompletableFuture completableFuture8 = completableFuture7;
                            return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$walletPut(this, string, (int)(bl ? 1 : 0), bl2, n, httpRequest, null, completableFuture8, httpResponse, null, 4, arg_0));
                        }
                        completableFuture7.join();
                        continue block9;
                    }
                    case 409: {
                        if (n >= 10) break;
                        continue block9;
                    }
                }
                this.logger.warn("Retrying adjusting checkout [API]: status: '{}'", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture9 = VertxUtil.sleep((long)this.task.getRetryDelay());
                if (!completableFuture9.isDone()) {
                    CompletableFuture completableFuture10 = completableFuture9;
                    return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$walletPut(this, string, (int)(bl ? 1 : 0), bl2, n, httpRequest, null, completableFuture10, httpResponse, null, 5, arg_0));
                }
                completableFuture9.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error adjusting checkout: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = super.randomSleep(3000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture11 = completableFuture;
                    return ((CompletableFuture)completableFuture11.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$walletPut(this, string, (int)(bl ? 1 : 0), bl2, n, null, null, completableFuture11, null, throwable, 6, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(false);
    }

    public CompletableFuture checkCaptcha(String string) {
        this.logger.info("Creating session");
        int n = 0;
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            MultiMap multiMap = this.checkCaptchaForm();
            try {
                HttpRequest httpRequest = this.api.postContact(string);
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest, (MultiMap)multiMap);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$checkCaptcha(this, string, n, multiMap, httpRequest, completableFuture2, null, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse != null) {
                    if (httpResponse.statusCode() == 302 || httpResponse.statusCode() == 301) {
                        String string2 = httpResponse.getHeader("location");
                        CompletableFuture completableFuture3 = this.handleRedirect(httpResponse, true);
                        if (!completableFuture3.isDone()) {
                            CompletableFuture completableFuture4 = completableFuture3;
                            return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$checkCaptcha(this, string, n, multiMap, httpRequest, completableFuture4, httpResponse, string2, null, 2, arg_0));
                        }
                        Boolean bl = (Boolean)completableFuture3.join();
                        if (bl == null) {
                            return CompletableFuture.completedFuture(string2);
                        }
                        if (!bl.booleanValue()) continue;
                        this.logger.error("Unknown redirect! - " + httpResponse);
                    } else if (httpResponse.statusCode() == 200) {
                        this.updateCheckoutParams(httpResponse.bodyAsString());
                        return CompletableFuture.completedFuture(httpResponse.bodyAsString());
                    }
                }
                CompletableFuture completableFuture5 = VertxUtil.sleep((long)this.task.getRetryDelay());
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$checkCaptcha(this, string, n, multiMap, httpRequest, completableFuture6, httpResponse, null, null, 3, arg_0));
                }
                completableFuture5.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error checking checkout captcha: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = super.randomSleep(3000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$checkCaptcha(this, string, n, multiMap, null, completableFuture7, null, null, throwable, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$atcUpsell(Shopify var0, String var1_1, int var2_2, HttpRequest var3_3, CompletableFuture var4_5, HttpResponse var5_6, Throwable var6_7, int var7_8, Object var8_9) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [8[CATCHBLOCK]], but top level block is 13[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$checkCaptcha(Shopify var0, String var1_1, int var2_2, MultiMap var3_3, HttpRequest var4_4, CompletableFuture var5_6, HttpResponse var6_7, String var7_8, Throwable var8_9, int var9_10, Object var10_11) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [8[CATCHBLOCK]], but top level block is 13[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public CompletableFuture fakeProcessingForm(String string) {
        MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
        multiMap.add("_method", "patch");
        multiMap.add("authenticity_token", this.authenticity);
        multiMap.add("previous_step", "payment_method");
        multiMap.add("step", "");
        multiMap.add("s", "west-" + this.getFakeTokenSalt());
        for (String string2 : this.TEXTAREA_PARAMS) {
            multiMap.add(string2, "");
        }
        if (!this.TEXTAREA_PARAMS.isEmpty()) {
            multiMap.add(string + "-count", "" + this.TEXTAREA_PARAMS.size());
            multiMap.add(string + "-count", "fs_count");
        }
        multiMap.add("checkout[payment_gateway]", this.paymentGateway);
        multiMap.add("checkout[credit_card][vault]", "false");
        multiMap.add("checkout[different_billing_address]", "false");
        if (this.task.getSite() == Site.SHOPNICEKICKS) {
            multiMap.add("checkout[attributes][I-agree-to-the-Terms-and-Conditions]", "Yes");
        }
        if (this.task.getSite() == Site.KITH) {
            multiMap.add("checkout[remember_me]", "false");
        }
        if (this.task.getSite() != Site.SHOPNICEKICKS) {
            multiMap.add("checkout[remember_me]", "0");
        }
        multiMap.add("checkout[vault_phone]", this.task.getProfile().getCountry().equals("JAPAN") ? "+81" + this.task.getProfile().getPhone() : "+1" + this.task.getProfile().getPhone());
        if (this.task.getSite() == Site.SHOEPALACE) {
            multiMap.add("checkout[attributes][I-agree-to-the-Terms-and-Conditions]", "Yes");
        }
        multiMap.add("checkout[total_price]", ThreadLocalRandom.current().nextInt(50, 300) + "00");
        multiMap.add("complete", "1");
        multiMap.add("checkout[client_details][browser_width]", this.BROWSER_WIDTH);
        multiMap.add("checkout[client_details][browser_height]", this.BROWSER_HEIGHT);
        multiMap.add("checkout[client_details][javascript_enabled]", "1");
        multiMap.add("checkout[client_details][color_depth]", "24");
        multiMap.add("checkout[client_details][java_enabled]", "false");
        multiMap.add("checkout[client_details][browser_tz]", "" + TIMEZONE_OFFSET);
        return CompletableFuture.completedFuture(multiMap);
    }

    /*
     * Could not resolve type clashes
     * Unable to fully structure code
     */
    public static CompletableFuture async$processingFormRestockMode(Shopify var0, String var1_1, MultiMap var2_2, CompletableFuture var3_3, int var4_4, Object var5_6) {
        switch (var4_4) {
            case 0: {
                var2_2 = MultiMap.caseInsensitiveMultiMap();
                var2_2.add("_method", "patch");
                var2_2.add("authenticity_token", var0.authenticity);
                var2_2.add("previous_step", "payment_method");
                var2_2.add("step", "");
                v0 = var0.paymentToken;
                if (!v0.isDone()) {
                    var6_7 = v0;
                    return var6_7.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$processingFormRestockMode(io.trickle.task.sites.shopify.Shopify java.lang.String io.vertx.core.MultiMap java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (String)var1_1, (MultiMap)var2_2, var6_7, (int)1));
                }
                ** GOTO lbl19
            }
            case 1: {
                v0 = var3_3;
lbl19:
                // 2 sources

                var3_3 = (PaymentTokenSupplier)v0.join();
                var2_2.add("s", var3_3.get());
                for (Object var5_6 : var0.TEXTAREA_PARAMS) {
                    var2_2.add((String)var5_6, "");
                }
                if (!var0.TEXTAREA_PARAMS.isEmpty()) {
                    var2_2.add(var1_1 + "-count", "" + var0.TEXTAREA_PARAMS.size());
                    var2_2.add(var1_1 + "-count", "fs_count");
                }
                if (var3_3.isVaulted()) {
                    var2_2.add("checkout[payment_gateway]", var0.paymentGateway);
                    var2_2.add("checkout[credit_card][vault]", "false");
                    var2_2.add("checkout[different_billing_address]", "false");
                    if (var0.task.getSite() == Site.SHOPNICEKICKS) {
                        var2_2.add("checkout[attributes][I-agree-to-the-Terms-and-Conditions]", "Yes");
                    }
                    if (var0.task.getSite() == Site.KITH) {
                        var2_2.add("checkout[remember_me]", "false");
                    }
                    if (var0.task.getSite() != Site.SHOPNICEKICKS) {
                        var2_2.add("checkout[remember_me]", "0");
                    }
                    var2_2.add("checkout[vault_phone]", var0.task.getProfile().getCountry().equals("JAPAN") != false ? "+81" + var0.task.getProfile().getPhone() : "+1" + var0.task.getProfile().getPhone());
                    if (var0.task.getSite() == Site.SHOEPALACE) {
                        var2_2.add("checkout[attributes][I-agree-to-the-Terms-and-Conditions]", "Yes");
                    }
                    if (var0.price != null) {
                        var2_2.add("checkout[total_price]", String.valueOf(var0.price));
                    }
                    var2_2.add("complete", "1");
                }
                var2_2.add("checkout[client_details][browser_width]", var0.BROWSER_WIDTH);
                var2_2.add("checkout[client_details][browser_height]", var0.BROWSER_HEIGHT);
                var2_2.add("checkout[client_details][javascript_enabled]", "1");
                var2_2.add("checkout[client_details][color_depth]", "24");
                var2_2.add("checkout[client_details][java_enabled]", "false");
                var2_2.add("checkout[client_details][browser_tz]", "" + Shopify.TIMEZONE_OFFSET);
                return CompletableFuture.completedFuture(var2_2);
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture walletsCart(boolean bl, boolean bl2) {
        JsonObject jsonObject = new JsonObject();
        if (bl2) {
            jsonObject.put("cart_token", (Object)this.api.getCookies().getCookieValue("cart"));
        } else {
            jsonObject.put("line_items", (Object)new JsonArray().add((Object)new JsonObject().put("variant_id", (Object)Long.parseLong(this.instanceSignal)).put("quantity", (Object)1)));
        }
        jsonObject.put("secret", (Object)true);
        if (!bl) return CompletableFuture.completedFuture(new JsonObject().put("checkout", (Object)jsonObject));
        JsonObject jsonObject2 = jsonObject;
        JsonObject jsonObject3 = new JsonObject();
        CompletableFuture<ShippingRateSupplier> completableFuture = this.shippingRate;
        if (!completableFuture.isDone()) {
            CompletableFuture<ShippingRateSupplier> completableFuture2 = completableFuture;
            String string = "handle";
            JsonObject jsonObject4 = jsonObject3;
            String string2 = "shipping_line";
            JsonObject jsonObject5 = jsonObject2;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$walletsCart(this, (int)(bl ? 1 : 0), (int)(bl2 ? 1 : 0), jsonObject, jsonObject5, string2, jsonObject4, string, completableFuture2, 1, arg_0));
        }
        jsonObject2.put("shipping_line", (Object)jsonObject3.put("handle", (Object)completableFuture.join().get()));
        return CompletableFuture.completedFuture(new JsonObject().put("checkout", (Object)jsonObject));
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$submitCheckpoint(Shopify var0, CaptchaToken var1_1, int var2_2, HttpRequest var3_3, CompletableFuture var4_5, HttpResponse var5_6, Throwable var6_7, int var7_8, Object var8_9) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [7[CATCHBLOCK]], but top level block is 11[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public CompletableFuture submitPassword() {
        this.logger.info("Submitting password");
        int n = 0;
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            try {
                HttpRequest httpRequest = this.api.submitPassword();
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest, (MultiMap)this.passwordForm());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$submitPassword(this, n, httpRequest, completableFuture2, null, 0, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                int n2 = httpResponse.statusCode();
                if (n2 == 302 && REDIRECT_STATUS.checkRedirectStatus((String)httpResponse.getHeader("location")) == REDIRECT_STATUS.HOMEPAGE) {
                    return CompletableFuture.completedFuture(null);
                }
                if (n2 == 302 && REDIRECT_STATUS.checkRedirectStatus((String)httpResponse.getHeader("location")) == REDIRECT_STATUS.PASSWORD) {
                    this.logger.error("Wrong password: {}", (Object)this.task.getPassword());
                    CompletableFuture completableFuture3 = VertxUtil.hardCodedSleep((long)8000L);
                    if (!completableFuture3.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture3;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$submitPassword(this, n, httpRequest, completableFuture4, httpResponse, n2, null, 2, arg_0));
                    }
                    completableFuture3.join();
                    return CompletableFuture.completedFuture(null);
                }
                this.logger.warn("Failed submitting password: status: '{}'", (Object)n2);
                CompletableFuture completableFuture5 = VertxUtil.randomSleep((long)((long)this.task.getRetryDelay() * 2L));
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$submitPassword(this, n, httpRequest, completableFuture6, httpResponse, n2, null, 3, arg_0));
                }
                completableFuture5.join();
            }
            catch (Exception exception) {
                this.logger.error("Error submitting password: {}", (Object)exception.getMessage());
                CompletableFuture completableFuture = VertxUtil.randomSleep((long)3000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$submitPassword(this, n, null, completableFuture7, null, 0, exception, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public JsonObject contactAPIForm() {
        JsonObject jsonObject = new JsonObject();
        JsonObject jsonObject2 = new JsonObject().put("secret", (Object)true).put("field_type", (Object)"hidden").put("email", (Object)this.task.getProfile().getEmail()).put("shipping_address", (Object)new JsonObject().put("first_name", (Object)this.task.getProfile().getFirstName()).put("last_name", (Object)this.task.getProfile().getLastName()).put("address1", (Object)this.task.getProfile().getAddress1()).put("address2", (Object)this.task.getProfile().getAddress2()).put("city", (Object)this.task.getProfile().getCity()).put("country", (Object)this.task.getProfile().getFullCountry()).put("province", (Object)this.task.getProfile().getFullState()).put("state", (Object)this.task.getProfile().getFullState()).put("zip", (Object)this.task.getProfile().getZip()).put("phone", (Object)this.task.getProfile().getPhone())).put("billing_address", (Object)new JsonObject().put("first_name", (Object)this.task.getProfile().getFirstName()).put("last_name", (Object)this.task.getProfile().getLastName()).put("address1", (Object)this.task.getProfile().getAddress1()).put("address2", (Object)this.task.getProfile().getAddress2()).put("city", (Object)this.task.getProfile().getCity()).put("country", (Object)this.task.getProfile().getFullCountry()).put("province", (Object)this.task.getProfile().getFullState()).put("state", (Object)this.task.getProfile().getFullState()).put("zip", (Object)this.task.getProfile().getZip()).put("phone", (Object)this.task.getProfile().getPhone()));
        jsonObject.put("checkout", (Object)jsonObject2);
        return jsonObject;
    }

    public void updateShippingRate(String string) {
        Matcher matcher = SHIPPINGRATE_PATTERN.matcher(string);
        if (matcher.find()) {
            if (!this.shippingRate.isDone()) {
                this.shippingRate.complete(new ShippingRateSupplier(matcher.group(1)));
            } else {
                try {
                    this.shippingRate.get().updateRate(matcher.group(1));
                }
                catch (Exception exception) {
                    this.logger.info("Error updating rate: {}", (Object)exception.getMessage());
                }
            }
        } else {
            if (!this.logger.isDebugEnabled()) return;
            this.logger.debug("No shipping rate found");
        }
    }

    public CompletableFuture processingFormRestockMode(String string) {
        MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
        multiMap.add("_method", "patch");
        multiMap.add("authenticity_token", this.authenticity);
        multiMap.add("previous_step", "payment_method");
        multiMap.add("step", "");
        CompletableFuture<PaymentTokenSupplier> completableFuture = this.paymentToken;
        if (!completableFuture.isDone()) {
            CompletableFuture<PaymentTokenSupplier> completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$processingFormRestockMode(this, string, multiMap, completableFuture2, 1, arg_0));
        }
        PaymentTokenSupplier paymentTokenSupplier = completableFuture.join();
        multiMap.add("s", paymentTokenSupplier.get());
        for (String string2 : this.TEXTAREA_PARAMS) {
            multiMap.add(string2, "");
        }
        if (!this.TEXTAREA_PARAMS.isEmpty()) {
            multiMap.add(string + "-count", "" + this.TEXTAREA_PARAMS.size());
            multiMap.add(string + "-count", "fs_count");
        }
        if (paymentTokenSupplier.isVaulted()) {
            multiMap.add("checkout[payment_gateway]", this.paymentGateway);
            multiMap.add("checkout[credit_card][vault]", "false");
            multiMap.add("checkout[different_billing_address]", "false");
            if (this.task.getSite() == Site.SHOPNICEKICKS) {
                multiMap.add("checkout[attributes][I-agree-to-the-Terms-and-Conditions]", "Yes");
            }
            if (this.task.getSite() == Site.KITH) {
                multiMap.add("checkout[remember_me]", "false");
            }
            if (this.task.getSite() != Site.SHOPNICEKICKS) {
                multiMap.add("checkout[remember_me]", "0");
            }
            multiMap.add("checkout[vault_phone]", this.task.getProfile().getCountry().equals("JAPAN") ? "+81" + this.task.getProfile().getPhone() : "+1" + this.task.getProfile().getPhone());
            if (this.task.getSite() == Site.SHOEPALACE) {
                multiMap.add("checkout[attributes][I-agree-to-the-Terms-and-Conditions]", "Yes");
            }
            if (this.price != null) {
                multiMap.add("checkout[total_price]", String.valueOf(this.price));
            }
            multiMap.add("complete", "1");
        }
        multiMap.add("checkout[client_details][browser_width]", this.BROWSER_WIDTH);
        multiMap.add("checkout[client_details][browser_height]", this.BROWSER_HEIGHT);
        multiMap.add("checkout[client_details][javascript_enabled]", "1");
        multiMap.add("checkout[client_details][color_depth]", "24");
        multiMap.add("checkout[client_details][java_enabled]", "false");
        multiMap.add("checkout[client_details][browser_tz]", "" + TIMEZONE_OFFSET);
        return CompletableFuture.completedFuture(multiMap);
    }

    public CompletableFuture atcUpsell(String string) {
        int n = 0;
        this.logger.info("Attempting ATC [Upsell]");
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            try {
                HttpRequest httpRequest = this.api.upsellATC();
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest, (MultiMap)this.upsellForm(string));
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$atcUpsell(this, string, n, httpRequest, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 200) {
                    JsonObject jsonObject = httpResponse.bodyAsJsonObject();
                    return CompletableFuture.completedFuture(jsonObject);
                }
                if (httpResponse.statusCode() == 302 || httpResponse.statusCode() == 301) {
                    CompletableFuture completableFuture3 = this.handleRedirect(httpResponse, false);
                    if (!completableFuture3.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture3;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$atcUpsell(this, string, n, httpRequest, completableFuture4, httpResponse, null, 2, arg_0));
                    }
                    completableFuture3.join();
                }
                this.logger.warn("Failed attempting ATC [Upsell]: status: '{}'", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture5 = VertxUtil.sleep((long)this.task.getRetryDelay());
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$atcUpsell(this, string, n, httpRequest, completableFuture6, httpResponse, null, 3, arg_0));
                }
                completableFuture5.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Failed attempting ATC [Upsell]]: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = super.randomSleep(3000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$atcUpsell(this, string, n, null, completableFuture7, null, throwable, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture processPaymentAPI(String string) {
        this.logger.info("Processing [API]...");
        int n = 0;
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            try {
                Object object;
                HttpRequest httpRequest;
                HttpRequest httpRequest2 = httpRequest = this.api.processAPI(string);
                CompletableFuture completableFuture = this.processingAPIForm();
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    HttpRequest httpRequest3 = httpRequest2;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$processPaymentAPI(this, string, n, httpRequest, httpRequest3, completableFuture2, null, null, 0, null, 1, arg_0));
                }
                CompletableFuture completableFuture3 = Request.send((HttpRequest)httpRequest2, (Buffer)((JsonObject)completableFuture.join()).toBuffer());
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$processPaymentAPI(this, string, n, httpRequest, null, completableFuture4, null, null, 0, null, 2, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture3.join();
                if (httpResponse == null) continue;
                this.genPaymentToken();
                if (httpResponse.statusCode() == 302 || httpResponse.statusCode() == 301) {
                    CompletableFuture completableFuture5 = this.handleRedirect(httpResponse, true);
                    if (!completableFuture5.isDone()) {
                        CompletableFuture completableFuture6 = completableFuture5;
                        return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$processPaymentAPI(this, string, n, httpRequest, null, completableFuture6, httpResponse, null, 0, null, 3, arg_0));
                    }
                    object = (Boolean)completableFuture5.join();
                    if (object != null && !((Boolean)object).booleanValue()) continue;
                    String string2 = httpResponse.getHeader("location");
                    if (string2.contains("/stock_problem")) {
                        this.logger.warn("Retrying process payment [API]: status: '{}'", (Object)"OOS");
                    } else if (string2.contains("processing")) {
                        this.hasShippingAlreadySubmittedAPI = true;
                        return CompletableFuture.completedFuture((Buffer)httpResponse.body());
                    }
                } else if (httpResponse.statusCode() == 429) {
                    int n2;
                    object = httpResponse.bodyAsString();
                    int n3 = n2 = object != null && ((String)object).contains("Too many attempts: Please try again in a few minutes") ? 1 : 0;
                    if (n2 != 0 && ++this.counter429 > 8) {
                        throw new Throwable("Checkout expired. Restarting task...");
                    }
                    if (n2 == 0) {
                        if (n <= 4) continue;
                        CompletableFuture completableFuture7 = VertxUtil.randomSleep((long)this.task.getMonitorDelay());
                        if (!completableFuture7.isDone()) {
                            CompletableFuture completableFuture8 = completableFuture7;
                            return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$processPaymentAPI(this, string, n, httpRequest, null, completableFuture8, httpResponse, (String)object, n2, null, 4, arg_0));
                        }
                        completableFuture7.join();
                        continue;
                    }
                } else {
                    this.logger.warn("Retrying process payment [API]: status: '{}'", (Object)httpResponse.statusCode());
                    if (this.noProcessOnFirstTry) {
                        this.noProcessOnFirstTry = false;
                        continue;
                    }
                }
                CompletableFuture completableFuture9 = VertxUtil.sleep((long)this.task.getMonitorDelay());
                if (!completableFuture9.isDone()) {
                    CompletableFuture completableFuture10 = completableFuture9;
                    return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$processPaymentAPI(this, string, n, httpRequest, null, completableFuture10, httpResponse, null, 0, null, 5, arg_0));
                }
                completableFuture9.join();
            }
            catch (Exception exception) {
                this.logger.error("Error processing payment [API]: {}", (Object)exception.getMessage());
                CompletableFuture completableFuture = super.randomSleep(3000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture11 = completableFuture;
                    return ((CompletableFuture)completableFuture11.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$processPaymentAPI(this, string, n, null, null, completableFuture11, null, null, 0, exception, 6, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    static {
        queueLogic = true;
        AUTHENTICITY_TOKEN_PATTERN = Pattern.compile("authenticity_token\" value=\"(.*?)\"");
        AUTHENTICITY_CHECKOUT_TOKEN_PATTERN = Pattern.compile("data-payment-form=\"\" action=\".*?\" accept-charset=\".*?\" method=\"post\"><input type=\"hidden\" name=\"_method\" value=\"patch\" /><input type=\"hidden\" name=\"authenticity_token\" value=\"(.*?)\"");
        TEXTAREA_PARAMS_PATTERN = Pattern.compile("textarea name=\"(.*?)\" id=\"");
        SHIPPINGRATE_PATTERN = Pattern.compile("data-shipping-method=\"(.*?)\"");
        TOTAL_PRICE_PATTERN = Pattern.compile("checkout_total_price\" value=\"(.*?)\"");
        TOTAL_PRICE_BACKUP_PATTERN = Pattern.compile("data-checkout-payment-due-target=\"(.*?)\"");
        GATEWAY_CARD_PATTERN = Pattern.compile("data-gateway-name=\"credit_card\".*?data-select-gateway=\"(.*?)\"", 32);
        CHECK_VARIANT_PATTERN = Pattern.compile("^[0-9]+$");
        CHECK_EL_PATTERN = Pattern.compile("(https.*)", 2);
        instanceTime = Instant.now().plus((long)ThreadLocalRandom.current().nextInt(-20, 1), ChronoUnit.DAYS);
        isHarvesterInited = new AtomicBoolean(false);
        FIELD_ERR_PATTERN = Pattern.compile("field__message--error.*?>(.*?)<");
        SITE_URL_PATTERN = Pattern.compile("://(.*?)/");
        lockMap = new ConcurrentHashMap();
        ZoneId zoneId = ZoneId.systemDefault();
        String string = zoneId.getRules().getOffset(Instant.now()).toString().replace(":", "");
        TimeZone timeZone = TimeZone.getDefault();
        TIMEZONE_OFFSET = string.contains("+") ? timeZone.getOffset(Instant.now().toEpochMilli()) / 1000 / 60 : -timeZone.getOffset(Instant.now().toEpochMilli()) / 1000 / 60;
        int n = Storage.getHarvesterCountYs();
        harvesterCFs = new CompletableFuture[n];
        HARVESTERS = new Harvester[n];
        int n2 = 0;
        while (n2 < HARVESTERS.length) {
            Shopify.HARVESTERS[n2] = new Harvester();
            ++n2;
        }
    }

    public JsonObject ajaxJsonForm(String string) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("quantity", (Object)1);
        jsonObject.put("id", (Object)string);
        jsonObject.put("properties", (Object)new JsonObject());
        return jsonObject;
    }

    public CompletableFuture visitHomepage() {
        int n = 0;
        this.logger.info("Visiting homepage");
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            try {
                HttpRequest httpRequest = this.api.homepage();
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$visitHomepage(this, n, httpRequest, completableFuture2, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                return CompletableFuture.completedFuture(httpResponse.bodyAsString());
            }
            catch (Throwable throwable) {
                this.logger.error("Failed visiting homepage: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = super.randomSleep(3000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture3 = completableFuture;
                    return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$visitHomepage(this, n, null, completableFuture3, throwable, 2, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$walletsSubmitShipping(Shopify var0, String var1_1, int var2_2, HttpRequest var3_3, CompletableFuture var4_5, HttpResponse var5_6, JsonObject var6_7, JsonObject var7_8, Exception var8_9, int var9_10, Object var10_11) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [9[CATCHBLOCK]], but top level block is 15[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$handleQueueNew(Shopify var0, int var1_1, HttpRequest var2_2, CompletableFuture var3_4, HttpResponse var4_5, Throwable var5_6, int var6_7, Object var7_8) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [8[CATCHBLOCK]], but top level block is 13[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$submitContact(Shopify var0, String var1_1, int var2_2, MultiMap var3_3, HttpRequest var4_4, CompletableFuture var5_6, HttpResponse var6_7, String var7_8, Throwable var8_9, int var9_10, Object var10_11) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [8[CATCHBLOCK]], but top level block is 13[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$solveCheckpointCaptcha(Shopify var0, String var1_1, CaptchaToken var2_2, SolveFuture var3_4, SolveFuture var4_5, int var5_6, Object var6_7) {
        switch (var5_6) {
            case 0: {
                try {
                    var0.logger.info("Checkpoint needs solving");
                    var2_2 = new CaptchaToken("https://" + var0.api.getSiteURL() + "/checkpoint?return_to=https%3A%2F%2F" + var0.api.getSiteURL() + "%2Fcheckout", true, var0.api.proxyStringFull(), var1_1, var0);
                    var3_4 = ((TokenController)Engine.get().getModule(Controller.TOKEN)).solve(var2_2);
                    v0 = var3_4;
                    if (!v0.toCompletableFuture().isDone()) {
                        var4_5 = v0;
                        return var4_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$solveCheckpointCaptcha(io.trickle.task.sites.shopify.Shopify java.lang.String io.trickle.harvester.CaptchaToken io.trickle.harvester.SolveFuture io.trickle.harvester.SolveFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (String)var1_1, (CaptchaToken)var2_2, (SolveFuture)var3_4, (SolveFuture)var4_5, (int)1)).toCompletableFuture();
                    }
                    ** GOTO lbl17
                }
                catch (Throwable var2_3) {
                    var0.logger.error("HARVEST ERR: {}", (Object)var2_3.getMessage());
                    return CompletableFuture.completedFuture(null);
                }
            }
            case 1: {
                v0 = var4_5;
lbl17:
                // 2 sources

                v0.toCompletableFuture().join();
                return CompletableFuture.completedFuture(var2_2);
            }
        }
        throw new IllegalArgumentException();
    }

    public MultiMap passwordForm() {
        MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
        multiMap.set("form_type", "storefront_password");
        multiMap.set("utf8", "\u2713");
        multiMap.set("password", this.task.getPassword());
        multiMap.set("commit", "");
        return multiMap;
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$genCheckoutURL(Shopify var0, int var1_1, int var2_2, HttpRequest var3_3, CompletableFuture var4_5, HttpResponse var5_6, Throwable var6_7, int var7_8, Object var8_9) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [9[CATCHBLOCK]], but top level block is 15[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$getShippingRateOld(Shopify var0, int var1_1, HttpRequest var2_2, CompletableFuture var3_4, HttpResponse var4_5, Throwable var5_6, int var6_7, Object var7_9) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [10[CATCHBLOCK]], but top level block is 14[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public CompletableFuture confirmClear(String string) {
        this.logger.info("Confirming cart...");
        int n = 0;
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            try {
                HttpRequest httpRequest = this.api.rawCheckoutUrl(string);
                CompletableFuture completableFuture = this.safeCompleteRequest(httpRequest, "checking checkout session");
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$confirmClear(this, string, n, httpRequest, completableFuture2, null, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 302 || httpResponse.statusCode() == 301) {
                    REDIRECT_STATUS rEDIRECT_STATUS = REDIRECT_STATUS.checkRedirectStatus((String)httpResponse.getHeader("location"));
                    if (rEDIRECT_STATUS == REDIRECT_STATUS.CART) {
                        return CompletableFuture.completedFuture(null);
                    }
                    if (rEDIRECT_STATUS == REDIRECT_STATUS.CHECKPOINT) {
                        CompletableFuture completableFuture3 = VertxUtil.hardCodedSleep((long)Integer.MAX_VALUE);
                        if (!completableFuture3.isDone()) {
                            CompletableFuture completableFuture4 = completableFuture3;
                            return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$confirmClear(this, string, n, httpRequest, completableFuture4, httpResponse, rEDIRECT_STATUS, null, 2, arg_0));
                        }
                        completableFuture3.join();
                        continue;
                    }
                    CompletableFuture completableFuture5 = this.handleRedirect(httpResponse, true);
                    if (!completableFuture5.isDone()) {
                        CompletableFuture completableFuture6 = completableFuture5;
                        return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$confirmClear(this, string, n, httpRequest, completableFuture6, httpResponse, rEDIRECT_STATUS, null, 3, arg_0));
                    }
                    completableFuture5.join();
                    CompletableFuture completableFuture7 = VertxUtil.sleep((long)this.task.getMonitorDelay());
                    if (!completableFuture7.isDone()) {
                        CompletableFuture completableFuture8 = completableFuture7;
                        return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$confirmClear(this, string, n, httpRequest, completableFuture8, httpResponse, rEDIRECT_STATUS, null, 4, arg_0));
                    }
                    completableFuture7.join();
                    continue;
                }
                this.logger.info("Unable to properly confirm cart {}", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture9 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
                if (!completableFuture9.isDone()) {
                    CompletableFuture completableFuture10 = completableFuture9;
                    return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$confirmClear(this, string, n, httpRequest, completableFuture10, httpResponse, null, null, 5, arg_0));
                }
                completableFuture9.join();
            }
            catch (Exception exception) {
                this.logger.error("Error checking checkout session: {}", (Object)exception.getMessage());
                CompletableFuture completableFuture = VertxUtil.randomSleep((long)3000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture11 = completableFuture;
                    return ((CompletableFuture)completableFuture11.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$confirmClear(this, string, n, null, completableFuture11, null, null, exception, 6, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$processPaymentAPI(Shopify var0, String var1_1, int var2_2, HttpRequest var3_3, HttpRequest var4_5, CompletableFuture var5_6, HttpResponse var6_7, String var7_9, int var8_10, Exception var9_12, int var10_13, Object var11_14) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [10[CATCHBLOCK]], but top level block is 17[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public long calculatePollTime(String string) {
        try {
            Instant instant = OffsetDateTime.parse(string, Utils.ISO_8901_US).toInstant();
            long l = Duration.between(instant, Instant.now()).toMillis();
            return l > 0L ? l : 0L;
        }
        catch (NullPointerException | DateTimeParseException runtimeException) {
            return 0L;
        }
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$walletsCart(Shopify var0, int var1_1, int var2_2, JsonObject var3_3, JsonObject var4_4, String var5_5, JsonObject var6_6, String var7_7, CompletableFuture var8_8, int var9_9, Object var10_10) {
        switch (var9_9) {
            case 0: {
                var3_3 = new JsonObject();
                if (var2_2 != 0) {
                    var3_3.put("cart_token", (Object)var0.api.getCookies().getCookieValue("cart"));
                } else {
                    var3_3.put("line_items", (Object)new JsonArray().add((Object)new JsonObject().put("variant_id", (Object)Long.parseLong(var0.instanceSignal)).put("quantity", (Object)1)));
                }
                var3_3.put("secret", (Object)true);
                if (var1_1 == 0) return CompletableFuture.completedFuture(new JsonObject().put("checkout", (Object)var3_3));
                v0 = var3_3;
                v1 = "shipping_line";
                v2 = new JsonObject();
                v3 = "handle";
                v4 = var0.shippingRate;
                if (!v4.isDone()) {
                    var8_8 = v4;
                    var7_7 = v3;
                    var6_6 = v2;
                    var5_5 = v1;
                    var4_4 = v0;
                    return var8_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$walletsCart(io.trickle.task.sites.shopify.Shopify int int io.vertx.core.json.JsonObject io.vertx.core.json.JsonObject java.lang.String io.vertx.core.json.JsonObject java.lang.String java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (int)var1_1, (int)var2_2, (JsonObject)var3_3, (JsonObject)var4_4, (String)var5_5, (JsonObject)var6_6, (String)var7_7, var8_8, (int)1));
                }
                ** GOTO lbl32
            }
            case 1: {
                v0 = var4_4;
                v1 = var5_5;
                v2 = var6_6;
                v3 = var7_7;
                v4 = var8_8;
lbl32:
                // 2 sources

                v0.put(v1, (Object)v2.put(v3, (Object)((ShippingRateSupplier)v4.join()).get()));
                return CompletableFuture.completedFuture(new JsonObject().put("checkout", (Object)var3_3));
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture send(HttpRequest httpRequest, Object object) {
        int n = 0;
        while (n++ < Integer.MAX_VALUE) {
            if (!this.running) return CompletableFuture.completedFuture(null);
            try {
                HttpResponse httpResponse;
                HttpResponse httpResponse2;
                if (object == null) {
                    CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture2 = completableFuture;
                        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$send(this, httpRequest, object, n, completableFuture2, 1, arg_0));
                    }
                    httpResponse2 = (HttpResponse)completableFuture.join();
                } else {
                    CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest, (Object)object);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture3 = completableFuture;
                        return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$send(this, httpRequest, object, n, completableFuture3, 2, arg_0));
                    }
                    httpResponse2 = (HttpResponse)completableFuture.join();
                }
                if ((httpResponse = httpResponse2) == null) continue;
                return CompletableFuture.completedFuture(httpResponse);
            }
            catch (Exception exception) {
                String string = exception.getMessage();
                if (string.contains("timeout period")) {
                    this.logger.error("Request timeout. No response");
                    continue;
                }
                this.logger.error("Network Error [{}]", (Object)exception.getMessage());
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$submitContactAPI(Shopify var0, String var1_1, int var2_2, HttpRequest var3_3, CompletableFuture var4_5, HttpResponse var5_6, Throwable var6_7, int var7_8, Object var8_9) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [7[CATCHBLOCK]], but top level block is 11[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public CompletableFuture getShippingRateOld() {
        this.logger.info("Fetching rate... [OLD]");
        int n = 0;
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            if (this.shippingRate.isDone()) return CompletableFuture.completedFuture(null);
            try {
                HttpRequest httpRequest = this.api.shippingRateOld(this.task.getProfile().getZip(), this.task.getProfile().getCountry(), this.task.getProfile().getCountry().equals("US") ? this.task.getProfile().getState() : this.task.getProfile().getFullState());
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$getShippingRateOld(this, n, httpRequest, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 200) {
                    JsonObject jsonObject = httpResponse.bodyAsJsonObject();
                    JsonArray jsonArray = jsonObject.getJsonArray("shipping_rates");
                    if (jsonArray == null) return CompletableFuture.completedFuture(jsonObject);
                    if (jsonArray.isEmpty()) {
                        return CompletableFuture.completedFuture(jsonObject);
                    }
                    JsonObject jsonObject2 = jsonArray.getJsonObject(0);
                    if (this.shippingRate.isDone()) return CompletableFuture.completedFuture(jsonObject);
                    String string = jsonObject2.getString("source") + "-" + jsonObject2.getString("code").replace(" ", "%20").replace("\u00ae", "%C2%AE") + "-" + jsonObject2.getString("price");
                    switch (1.$SwitchMap$io$trickle$task$sites$Site[this.task.getSite().ordinal()]) {
                        case 1: 
                        case 2: 
                        case 3: 
                        case 4: 
                        case 5: 
                        case 6: 
                        case 7: 
                        case 8: 
                        case 9: {
                            string = URLEncoder.encode(string, StandardCharsets.UTF_8) + ".00";
                            break;
                        }
                    }
                    this.shippingRate.complete(new ShippingRateSupplier(string));
                    return CompletableFuture.completedFuture(jsonObject);
                }
                this.logger.warn("Waiting to fetch rate [OLD]: status: '{}'", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture3 = VertxUtil.sleep((long)this.task.getRetryDelay());
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$getShippingRateOld(this, n, httpRequest, completableFuture4, httpResponse, null, 2, arg_0));
                }
                completableFuture3.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error fetching rate [OLD]: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = super.randomSleep(3000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture5 = completableFuture;
                    return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$getShippingRateOld(this, n, null, completableFuture5, null, throwable, 3, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture getCheckoutURL(String string) {
        this.logger.info("Checking checkout session...");
        int n = 0;
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            try {
                HttpRequest httpRequest = this.api.contactPage(string);
                CompletableFuture completableFuture = this.safeCompleteRequest(httpRequest, "checking checkout session");
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$getCheckoutURL(this, string, n, httpRequest, completableFuture2, null, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 200) {
                    VertxUtil.sendSignal((String)"waitForCP");
                    this.updateCheckoutParams(httpResponse.bodyAsString());
                    if (!this.authenticity.isBlank()) return CompletableFuture.completedFuture(httpResponse.bodyAsString());
                    this.logger.info("Checkout expired. Handling...");
                    if (n >= 10) throw new Throwable("Checkout expired. Restarting task...");
                    continue;
                }
                if (httpResponse.statusCode() != 302 && httpResponse.statusCode() != 301) continue;
                CompletableFuture completableFuture3 = this.handleRedirect(httpResponse, true);
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$getCheckoutURL(this, string, n, httpRequest, completableFuture4, httpResponse, null, null, 2, arg_0));
                }
                Boolean bl = (Boolean)completableFuture3.join();
                if (bl == null) {
                    if (this.isEarly && !this.api.getCookies().contains("_shopify_checkpoint")) {
                        this.logger.info("Waiting for checkpoint");
                        if (this.cpMonitor != null) {
                            CompletableFuture<Object> completableFuture5 = CompletableFuture.anyOf(VertxUtil.randomSignalSleep((String)"waitForCP", (long)this.task.getMonitorDelay()), this.cpMonitor);
                            if (!completableFuture5.isDone()) {
                                CompletableFuture<Object> completableFuture6 = completableFuture5;
                                return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$getCheckoutURL(this, string, n, httpRequest, completableFuture6, httpResponse, bl, null, 3, arg_0));
                            }
                            completableFuture5.join();
                            if (this.isCPMonitorTriggered.compareAndSet(true, false) && !this.cpMonitor.isDone()) {
                                this.logger.info("Checkpoint detected. Waiting for presolve [EARLY]");
                                CompletableFuture<Void> completableFuture7 = this.cpMonitor;
                                if (!completableFuture7.isDone()) {
                                    CompletableFuture<Void> completableFuture8 = completableFuture7;
                                    return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$getCheckoutURL(this, string, n, httpRequest, completableFuture8, httpResponse, bl, null, 4, arg_0));
                                }
                                completableFuture7.join();
                            }
                        } else {
                            CompletableFuture completableFuture9 = VertxUtil.randomSignalSleep((String)"waitForCP", (long)this.task.getMonitorDelay());
                            if (!completableFuture9.isDone()) {
                                CompletableFuture completableFuture10 = completableFuture9;
                                return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$getCheckoutURL(this, string, n, httpRequest, completableFuture10, httpResponse, bl, null, 5, arg_0));
                            }
                            completableFuture9.join();
                        }
                        this.api.setInstock();
                        continue;
                    }
                    VertxUtil.sendSignal((String)"waitForCP");
                    CompletableFuture completableFuture11 = this.checkCaptcha(string);
                    if (!completableFuture11.isDone()) {
                        CompletableFuture completableFuture12 = completableFuture11;
                        return ((CompletableFuture)completableFuture12.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$getCheckoutURL(this, string, n, httpRequest, completableFuture12, httpResponse, bl, null, 6, arg_0));
                    }
                    String string2 = (String)completableFuture11.join();
                    return CompletableFuture.completedFuture(string2);
                }
                if (!bl.booleanValue()) continue;
                this.logger.info("Unknown redirect. Handling..");
                CompletableFuture completableFuture13 = VertxUtil.randomSleep((long)this.task.getMonitorDelay());
                if (!completableFuture13.isDone()) {
                    CompletableFuture completableFuture14 = completableFuture13;
                    return ((CompletableFuture)completableFuture14.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$getCheckoutURL(this, string, n, httpRequest, completableFuture14, httpResponse, bl, null, 7, arg_0));
                }
                completableFuture13.join();
            }
            catch (Exception exception) {
                this.logger.error("Error checking checkout session: {}", (Object)exception.getMessage());
                CompletableFuture completableFuture = VertxUtil.randomSleep((long)3000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture15 = completableFuture;
                    return ((CompletableFuture)completableFuture15.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$getCheckoutURL(this, string, n, null, completableFuture15, null, null, exception, 8, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public static MultiMap checkpointForm(CaptchaToken captchaToken) {
        JsonObject jsonObject = new JsonObject(captchaToken.getToken());
        MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
        Iterator iterator = jsonObject.fieldNames().iterator();
        while (iterator.hasNext()) {
            String string = (String)iterator.next();
            multiMap.add(string, jsonObject.getString(string));
        }
        return multiMap;
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$atcAJAX(Shopify var0, String var1_1, int var2_2, String var3_3, int var4_4, HttpRequest var5_5, CompletableFuture var6_7, HttpResponse var7_8, Throwable var8_10, int var9_11, Object var10_12) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [8[CATCHBLOCK]], but top level block is 13[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public CompletableFuture genCheckoutURLIgnorePassword(boolean bl) {
        int n = 0;
        this.logger.info("Generating checkout URL...");
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            try {
                HttpResponse httpResponse;
                HttpResponse httpResponse2;
                HttpRequest httpRequest = this.api.emptyCheckout(bl);
                if (bl) {
                    CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest, (Buffer)Buffer.buffer((String)"{}"));
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture2 = completableFuture;
                        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$genCheckoutURLIgnorePassword(this, (int)(bl ? 1 : 0), n, httpRequest, completableFuture2, null, null, 1, arg_0));
                    }
                    httpResponse2 = (HttpResponse)completableFuture.join();
                } else {
                    CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture3 = completableFuture;
                        return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$genCheckoutURLIgnorePassword(this, (int)(bl ? 1 : 0), n, httpRequest, completableFuture3, null, null, 2, arg_0));
                    }
                    httpResponse2 = (HttpResponse)completableFuture.join();
                }
                if ((httpResponse = httpResponse2) == null) continue;
                if (httpResponse.statusCode() == 302 || httpResponse.statusCode() == 301) {
                    CompletableFuture completableFuture = this.handleRedirect(httpResponse, true);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$genCheckoutURLIgnorePassword(this, (int)(bl ? 1 : 0), n, httpRequest, completableFuture4, httpResponse, null, 3, arg_0));
                    }
                    if (((Boolean)completableFuture.join()).booleanValue()) {
                        return CompletableFuture.completedFuture(httpResponse.getHeader("location"));
                    }
                }
                if (httpResponse.statusCode() != 302) {
                    this.logger.warn("Failed generating checkout URL: status: '{}'", (Object)httpResponse.statusCode());
                    CompletableFuture completableFuture = VertxUtil.sleep((long)this.task.getRetryDelay());
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture5 = completableFuture;
                        return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$genCheckoutURLIgnorePassword(this, (int)(bl ? 1 : 0), n, httpRequest, completableFuture5, httpResponse, null, 4, arg_0));
                    }
                    completableFuture.join();
                    continue;
                }
                if (!httpResponse.getHeader("location").contains("password")) continue;
                return CompletableFuture.completedFuture(null);
            }
            catch (Throwable throwable) {
                this.logger.error("Failed generating checkout URL: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = super.randomSleep(3000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$genCheckoutURLIgnorePassword(this, (int)(bl ? 1 : 0), n, null, completableFuture6, null, throwable, 5, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture processFakeAsPatch(String string) {
        this.logger.info("Adjusting for restock #2...");
        int n = 0;
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            try {
                CompletableFuture completableFuture = this.fakeProcessingForm(string);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$processFakeAsPatch(this, string, n, completableFuture2, null, null, null, null, 1, arg_0));
                }
                MultiMap multiMap = (MultiMap)completableFuture.join();
                HttpRequest httpRequest = this.api.fakePatchPayment(string);
                CompletableFuture completableFuture3 = Request.send((HttpRequest)httpRequest, (MultiMap)multiMap);
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$processFakeAsPatch(this, string, n, completableFuture4, multiMap, httpRequest, null, null, 2, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture3.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 302 || httpResponse.statusCode() == 301) {
                    CompletableFuture completableFuture5 = this.handleRedirect(httpResponse, true);
                    if (!completableFuture5.isDone()) {
                        CompletableFuture completableFuture6 = completableFuture5;
                        return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$processFakeAsPatch(this, string, n, completableFuture6, multiMap, httpRequest, httpResponse, null, 3, arg_0));
                    }
                    completableFuture5.join();
                    return CompletableFuture.completedFuture(null);
                }
                this.logger.warn("Error adjusting for restock #2: status: '{}'", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture7 = VertxUtil.sleep((long)this.task.getRetryDelay());
                if (!completableFuture7.isDone()) {
                    CompletableFuture completableFuture8 = completableFuture7;
                    return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$processFakeAsPatch(this, string, n, completableFuture8, multiMap, httpRequest, httpResponse, null, 4, arg_0));
                }
                completableFuture7.join();
            }
            catch (Exception exception) {
                if (exception.getMessage().equals("io.vertx.core.VertxException: Connection was closed")) continue;
                this.logger.error("Error adjusting for restock #2: {}", (Object)exception.getMessage());
                CompletableFuture completableFuture = super.randomSleep(3000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture9 = completableFuture;
                    return ((CompletableFuture)completableFuture9.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$processFakeAsPatch(this, string, n, completableFuture9, null, null, null, exception, 5, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture processingForm(String string) {
        MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
        multiMap.add("_method", "patch");
        multiMap.add("authenticity_token", this.authenticity);
        multiMap.add("previous_step", "payment_method");
        multiMap.add("step", "");
        CompletableFuture<PaymentTokenSupplier> completableFuture = this.paymentToken;
        if (!completableFuture.isDone()) {
            CompletableFuture<PaymentTokenSupplier> completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$processingForm(this, string, multiMap, completableFuture2, null, 1, arg_0));
        }
        PaymentTokenSupplier paymentTokenSupplier = completableFuture.join();
        multiMap.add("s", paymentTokenSupplier.get());
        for (String string2 : this.TEXTAREA_PARAMS) {
            multiMap.add(string2, "");
        }
        if (!this.TEXTAREA_PARAMS.isEmpty()) {
            multiMap.add(string + "-count", "" + this.TEXTAREA_PARAMS.size());
            multiMap.add(string + "-count", "fs_count");
        }
        if (this.paymentGateway == null) {
            CompletableFuture completableFuture3 = this.getProcessingPage(string, false);
            if (!completableFuture3.isDone()) {
                CompletableFuture completableFuture4 = completableFuture3;
                return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$processingForm(this, string, multiMap, completableFuture4, paymentTokenSupplier, 2, arg_0));
            }
            completableFuture3.join();
        }
        multiMap.add("checkout[payment_gateway]", this.paymentGateway);
        multiMap.add("checkout[credit_card][vault]", "false");
        multiMap.add("checkout[different_billing_address]", "false");
        if (this.task.getSite() == Site.SHOPNICEKICKS) {
            multiMap.add("checkout[attributes][I-agree-to-the-Terms-and-Conditions]", "Yes");
        }
        if (this.task.getSite() == Site.KITH) {
            multiMap.add("checkout[remember_me]", "false");
        }
        if (this.task.getSite() != Site.SHOPNICEKICKS) {
            multiMap.add("checkout[remember_me]", "0");
        }
        multiMap.add("checkout[vault_phone]", this.task.getProfile().getCountry().equals("JAPAN") ? "+81" + this.task.getProfile().getPhone() : "+1" + this.task.getProfile().getPhone());
        if (this.task.getSite() == Site.SHOEPALACE) {
            multiMap.add("checkout[attributes][I-agree-to-the-Terms-and-Conditions]", "Yes");
        }
        if (this.price != null) {
            multiMap.add("checkout[total_price]", String.valueOf(this.price));
        }
        multiMap.add("complete", "1");
        multiMap.add("checkout[client_details][browser_width]", this.BROWSER_WIDTH);
        multiMap.add("checkout[client_details][browser_height]", this.BROWSER_HEIGHT);
        multiMap.add("checkout[client_details][javascript_enabled]", "1");
        multiMap.add("checkout[client_details][color_depth]", "24");
        multiMap.add("checkout[client_details][java_enabled]", "false");
        multiMap.add("checkout[client_details][browser_tz]", "" + TIMEZONE_OFFSET);
        return CompletableFuture.completedFuture(multiMap);
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$clearWithChangeJS(Shopify var0, int var1_1, HttpRequest var2_2, CompletableFuture var3_4, HttpResponse var4_5, Throwable var5_6, int var6_7, Object var7_8) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [8[CATCHBLOCK]], but top level block is 13[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public void setCurrency() {
        switch (this.task.getProfile().getCountry()) {
            case "JP": {
                this.api.getWebClient().cookieStore().put("cart_currency", "JPY", this.api.getSiteURL());
                break;
            }
            case "CA": {
                this.api.getWebClient().cookieStore().put("cart_currency", "CAD", this.api.getSiteURL());
                break;
            }
            case "US": {
                this.api.getWebClient().cookieStore().put("cart_currency", "USD", this.api.getSiteURL());
                break;
            }
        }
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$genCheckoutURLViaCart(Shopify var0, int var1_1, HttpRequest var2_2, CompletableFuture var3_4, HttpResponse var4_5, Throwable var5_6, int var6_7, Object var7_8) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [8[CATCHBLOCK]], but top level block is 13[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    public static CompletableFuture async$getCPHtml(Shopify var0, int var1_1, int var2_2, HttpRequest var3_3, CompletableFuture var4_5, Throwable var5_6, int var6_7, Object var7_8) {
        switch (var6_7) {
            case 0: {
                var0.logger.info("Visiting checkpoint");
                var2_2 = 0;
                block7: while (var2_2++ < 0x7FFFFFFF) {
                    if (var0.api.getWebClient().isActive() == false) return CompletableFuture.completedFuture(null);
                    try {
                        var3_3 /* !! */  = var0.api.checkpointPage((boolean)var1_1);
                        v0 = Request.send((HttpRequest)var3_3 /* !! */ );
                        if (!v0.isDone()) {
                            var5_6 = v0;
                            return var5_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$getCPHtml(io.trickle.task.sites.shopify.Shopify int int io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (int)var1_1, (int)var2_2, (HttpRequest)var3_3 /* !! */ , (CompletableFuture)var5_6, null, (int)1));
                        }
lbl13:
                        // 3 sources

                        while (true) {
                            var4_5 = (HttpResponse)v0.join();
                            if (var4_5 == null || var4_5.statusCode() != 200) continue block7;
                            return CompletableFuture.completedFuture(var4_5.bodyAsString());
                        }
                    }
                    catch (Throwable var3_4) {
                        if (!var3_4.getMessage().contains("Connection was closed")) {
                            var0.logger.error("Failed hitting CP: {}", (Object)var3_4.getMessage());
                        }
                        if (!(v1 = VertxUtil.randomSleep((long)var0.task.getRetryDelay())).isDone()) {
                            var5_6 = v1;
                            return var5_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$getCPHtml(io.trickle.task.sites.shopify.Shopify int int io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (int)var1_1, (int)var2_2, null, (CompletableFuture)var5_6, (Throwable)var3_4, (int)2));
                        }
lbl23:
                        // 3 sources

                        while (true) {
                            v1.join();
                            continue block7;
                            break;
                        }
                    }
                }
                return CompletableFuture.completedFuture(null);
            }
            case 1: {
                v0 = var4_5;
                ** continue;
            }
            case 2: {
                v1 = var4_5;
                var3_3 /* !! */  = var5_6;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture sessionLogon(Account account) {
        String string = account.lookupSession();
        if (string.isBlank()) return CompletableFuture.completedFuture(false);
        JsonArray jsonArray = new JsonArray(string);
        if (jsonArray.isEmpty()) return CompletableFuture.completedFuture(false);
        for (int i = 0; i < jsonArray.size(); ++i) {
            try {
                String string2 = jsonArray.getString(i);
                Cookie cookie = ClientCookieDecoder.STRICT.decode(string2);
                if (cookie == null || !cookie.name().equals("_secure_session_id")) continue;
                this.api.getCookies().put(cookie);
                continue;
            }
            catch (Throwable throwable) {
                this.logger.warn("Error parsing session state: {}", (Object)throwable.getMessage());
            }
        }
        try {
            CompletableFuture completableFuture = this.GETREQ("Validating Session", this.api.accountConfirmPage(), new Integer[]{200, 302, 401}, new String[]{""});
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$sessionLogon(this, account, string, jsonArray, completableFuture2, 1, arg_0));
            }
            String string3 = (String)completableFuture.join();
            if (string3.contains("order-history")) return CompletableFuture.completedFuture(true);
            if (string3.contains("/account/logout")) return CompletableFuture.completedFuture(true);
            if (!string3.endsWith("/password")) return CompletableFuture.completedFuture(false);
            return CompletableFuture.completedFuture(true);
        }
        catch (Throwable throwable) {
            this.logger.warn("Error validating session: {}", (Object)throwable.getMessage());
        }
        return CompletableFuture.completedFuture(false);
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$walletPut(Shopify var0, String var1_1, int var2_2, Boolean var3_3, int var4_4, HttpRequest var5_5, HttpRequest var6_7, CompletableFuture var7_8, HttpResponse var8_9, Throwable var9_10, int var10_11, Object var11_12) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [17[CATCHBLOCK]], but top level block is 24[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$initHarvesters(CompletionStage var0, Lock var1_1, CompletableFuture var2_3, int var3_4, Object var4_5) {
        switch (var3_4) {
            case 0: {
                if (Shopify.harvesterCFs.length == 0) {
                    return CompletableFuture.completedFuture(null);
                }
                v0 = Vertx.currentContext().owner().sharedData().getLockWithTimeout("harvesterinitshopify", 1800000L).toCompletionStage();
                if (!v0.toCompletableFuture().isDone()) {
                    var2_3 = v0;
                    return var2_3.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$initHarvesters(java.util.concurrent.CompletionStage io.vertx.core.shareddata.Lock java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)(var2_3, null, null, (int)1)).toCompletableFuture();
                }
                ** GOTO lbl12
            }
            case 1: {
                v0 = var0;
lbl12:
                // 2 sources

                var0 = (Lock)v0.toCompletableFuture().join();
                for (var1_2 = 0; var1_2 < Shopify.HARVESTERS.length && !Shopify.isHarvesterInited.get(); ++var1_2) {
                    Shopify.harvesterCFs[var1_2] = Shopify.HARVESTERS[var1_2].start();
                }
                v1 = CompletableFuture.allOf(Shopify.harvesterCFs);
                if (!v1.isDone()) {
                    var2_3 = v1;
                    return var2_3.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$initHarvesters(java.util.concurrent.CompletionStage io.vertx.core.shareddata.Lock java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)(null, (Lock)var0, var2_3, (int)2));
                }
                ** GOTO lbl24
            }
            case 2: {
                v1 = var2_3;
                var0 = var1_1;
lbl24:
                // 2 sources

                v1.join();
                Shopify.isHarvesterInited.set(true);
                var0.release();
                return CompletableFuture.completedFuture(null);
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture handleRedirect(String string, boolean bl) {
        REDIRECT_STATUS rEDIRECT_STATUS = REDIRECT_STATUS.checkRedirectStatus((String)string);
        switch (1.$SwitchMap$io$trickle$task$sites$shopify$util$REDIRECT_STATUS[rEDIRECT_STATUS.ordinal()]) {
            case 1: {
                if (!string.contains("key=")) {
                    this.api.key = null;
                }
                this.api.setOOS();
                if (bl) return CompletableFuture.completedFuture(null);
                CompletableFuture completableFuture = this.handleOOS(string);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$handleRedirect(this, string, (int)(bl ? 1 : 0), rEDIRECT_STATUS, completableFuture2, 0, null, null, 1, arg_0));
                }
                completableFuture.join();
                return CompletableFuture.completedFuture(false);
            }
            case 2: {
                if (this.task.getPassword() != null) {
                    CompletableFuture completableFuture = this.submitPassword();
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture3 = completableFuture;
                        return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$handleRedirect(this, string, (int)(bl ? 1 : 0), rEDIRECT_STATUS, completableFuture3, 0, null, null, 2, arg_0));
                    }
                    completableFuture.join();
                } else {
                    this.logger.info("Password page detected. Waiting for drop.");
                    CompletableFuture completableFuture = VertxUtil.randomSignalSleep((String)this.api.getSiteURL(), (long)this.task.getMonitorDelay());
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$handleRedirect(this, string, (int)(bl ? 1 : 0), rEDIRECT_STATUS, completableFuture4, 0, null, null, 3, arg_0));
                    }
                    completableFuture.join();
                }
                return CompletableFuture.completedFuture(false);
            }
            case 3: {
                this.logger.info("Handling 'old' queue...");
                CompletableFuture completableFuture = this.checkQueueThrottle();
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture5 = completableFuture;
                    return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$handleRedirect(this, string, (int)(bl ? 1 : 0), rEDIRECT_STATUS, completableFuture5, 0, null, null, 4, arg_0));
                }
                completableFuture.join();
                CompletableFuture completableFuture6 = this.handleQueueOld();
                if (!completableFuture6.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture6;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$handleRedirect(this, string, (int)(bl ? 1 : 0), rEDIRECT_STATUS, completableFuture7, 0, null, null, 5, arg_0));
                }
                completableFuture6.join();
                return CompletableFuture.completedFuture(false);
            }
            case 4: {
                CompletableFuture completableFuture = this.checkNewQueueThrottle();
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture8 = completableFuture;
                    return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$handleRedirect(this, string, (int)(bl ? 1 : 0), rEDIRECT_STATUS, completableFuture8, 0, null, null, 6, arg_0));
                }
                int n = ((Boolean)completableFuture.join()).booleanValue() ? 1 : 0;
                if (n == 0) return CompletableFuture.completedFuture(false);
                CompletableFuture completableFuture9 = this.handleQueueNew();
                if (!completableFuture9.isDone()) {
                    CompletableFuture completableFuture10 = completableFuture9;
                    return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$handleRedirect(this, string, (int)(bl ? 1 : 0), rEDIRECT_STATUS, completableFuture10, n, null, null, 7, arg_0));
                }
                completableFuture9.join();
                return CompletableFuture.completedFuture(false);
            }
            case 5: {
                if (this.task.getMode().contains("human") || this.task.getMode().contains("hybrid")) {
                    if (this.isCPMonitorTriggered.compareAndSet(true, false)) {
                        this.logger.info("Checkpoint detected. Waiting for presolve");
                        CompletableFuture<Void> completableFuture = this.cpMonitor;
                        if (!completableFuture.isDone()) {
                            CompletableFuture<Void> completableFuture11 = completableFuture;
                            return ((CompletableFuture)completableFuture11.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$handleRedirect(this, string, (int)(bl ? 1 : 0), rEDIRECT_STATUS, completableFuture11, 0, null, null, 8, arg_0));
                        }
                        completableFuture.join();
                    } else {
                        this.logger.info("Checkpoint detected. Handling...");
                        this.shutOffPresolver(true);
                        CompletableFuture completableFuture = this.getCPHtml(false);
                        if (!completableFuture.isDone()) {
                            CompletableFuture completableFuture12 = completableFuture;
                            return ((CompletableFuture)completableFuture12.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$handleRedirect(this, string, (int)(bl ? 1 : 0), rEDIRECT_STATUS, completableFuture12, 0, null, null, 9, arg_0));
                        }
                        String string2 = (String)completableFuture.join();
                        CompletableFuture completableFuture13 = this.solveCheckpointCaptcha(string2);
                        if (!completableFuture13.isDone()) {
                            CompletableFuture completableFuture14 = completableFuture13;
                            return ((CompletableFuture)completableFuture14.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$handleRedirect(this, string, (int)(bl ? 1 : 0), rEDIRECT_STATUS, completableFuture14, 0, string2, null, 10, arg_0));
                        }
                        CaptchaToken captchaToken = (CaptchaToken)completableFuture13.join();
                        CompletableFuture completableFuture15 = this.submitCheckpoint(captchaToken);
                        if (!completableFuture15.isDone()) {
                            CompletableFuture completableFuture16 = completableFuture15;
                            return ((CompletableFuture)completableFuture16.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$handleRedirect(this, string, (int)(bl ? 1 : 0), rEDIRECT_STATUS, completableFuture16, 0, string2, captchaToken, 11, arg_0));
                        }
                        completableFuture15.join();
                    }
                } else {
                    this.logger.error("Checkpoint is enforced but this task is running [{}]. Sleeping...", (Object)this.task.getMode());
                    CompletableFuture completableFuture = VertxUtil.hardCodedSleep((long)30000L);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture17 = completableFuture;
                        return ((CompletableFuture)completableFuture17.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$handleRedirect(this, string, (int)(bl ? 1 : 0), rEDIRECT_STATUS, completableFuture17, 0, null, null, 12, arg_0));
                    }
                    completableFuture.join();
                }
                return CompletableFuture.completedFuture(false);
            }
            case 6: {
                this.logger.info("Login required. Logging in...");
                CompletableFuture completableFuture = this.login();
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture18 = completableFuture;
                    return ((CompletableFuture)completableFuture18.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$handleRedirect(this, string, (int)(bl ? 1 : 0), rEDIRECT_STATUS, completableFuture18, 0, null, null, 13, arg_0));
                }
                completableFuture.join();
                return CompletableFuture.completedFuture(false);
            }
            case 7: {
                this.logger.info("Bad account. Wrong password? Generating a fresh account.");
                CompletableFuture completableFuture = this.genAcc();
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture19 = completableFuture;
                    return ((CompletableFuture)completableFuture19.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$handleRedirect(this, string, (int)(bl ? 1 : 0), rEDIRECT_STATUS, completableFuture19, 0, null, null, 14, arg_0));
                }
                completableFuture.join();
                return CompletableFuture.completedFuture(true);
            }
            case 8: {
                this.logger.info("Login captcha, using V2.");
                CompletableFuture completableFuture = this.loginV2();
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture20 = completableFuture;
                    return ((CompletableFuture)completableFuture20.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$handleRedirect(this, string, (int)(bl ? 1 : 0), rEDIRECT_STATUS, completableFuture20, 0, null, null, 15, arg_0));
                }
                completableFuture.join();
                return CompletableFuture.completedFuture(true);
            }
            case 9: {
                return CompletableFuture.completedFuture(true);
            }
            case 10: {
                this.logger.info("Product is not yet cartable.");
                CompletableFuture completableFuture = this.atcAJAX(this.instanceSignal, false);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture21 = completableFuture;
                    return ((CompletableFuture)completableFuture21.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$handleRedirect(this, string, (int)(bl ? 1 : 0), rEDIRECT_STATUS, completableFuture21, 0, null, null, 16, arg_0));
                }
                completableFuture.join();
                return CompletableFuture.completedFuture(false);
            }
        }
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("signal sent");
        }
        VertxUtil.sendSignal((String)this.api.getSiteURL());
        return CompletableFuture.completedFuture(true);
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$processPayment(Shopify var0, String var1_1, int var2_2, CompletableFuture var3_3, MultiMap var4_5, long var5_7, HttpRequest var7_8, HttpResponse var8_9, String var9_10, REDIRECT_STATUS var10_11, Exception var11_12, int var12_13, Object var13_14) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [16[CATCHBLOCK]], but top level block is 29[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public CompletableFuture submitContactAPI(String string) {
        this.logger.info("Submitting contact...");
        int n = 0;
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            try {
                HttpRequest httpRequest = this.api.contactAPI(string);
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest, (Buffer)this.contactAPIForm().toBuffer());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$submitContactAPI(this, string, n, httpRequest, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 302) return CompletableFuture.completedFuture((Buffer)httpResponse.body());
                if (httpResponse.statusCode() == 301) {
                    return CompletableFuture.completedFuture((Buffer)httpResponse.body());
                }
                if (httpResponse.statusCode() == 429) {
                    if (httpResponse.bodyAsString().contains("Too many attempts: Please try again in a few minutes") && ++this.counter429 > 8) {
                        throw new Throwable("Checkout expired. Restarting task...");
                    }
                } else {
                    this.logger.warn("Waiting to submit contact: status: '{}'", (Object)httpResponse.statusCode());
                }
                CompletableFuture completableFuture3 = VertxUtil.sleep((long)this.task.getRetryDelay());
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$submitContactAPI(this, string, n, httpRequest, completableFuture4, httpResponse, null, 2, arg_0));
                }
                completableFuture3.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error submitting contact: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = super.randomSleep(3000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture5 = completableFuture;
                    return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$submitContactAPI(this, string, n, null, completableFuture5, null, throwable, 3, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    public static CompletableFuture async$fetchELJS(Shopify var0, int var1_1, HttpRequest var2_2, CompletableFuture var3_4, HttpResponse var4_5, Throwable var5_6, int var6_7, Object var7_8) {
        switch (var6_7) {
            case 0: {
                var1_1 = 0;
                var0.logger.info("Monitoring [EL]");
                block9: while (var0.running != false) {
                    if (var1_1++ >= 0x7FFFFFFF) return CompletableFuture.completedFuture(null);
                    var0.instanceSignal = var0.task.getKeywords()[0].replaceAll("collections/.*?/", "");
                    try {
                        var2_2 /* !! */  = var0.api.elJS(var0.instanceSignal);
                        v0 = Request.send((HttpRequest)var2_2 /* !! */ );
                        if (!v0.isDone()) {
                            var5_6 = v0;
                            return var5_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$fetchELJS(io.trickle.task.sites.shopify.Shopify int io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (int)var1_1, (HttpRequest)var2_2 /* !! */ , (CompletableFuture)var5_6, null, null, (int)1));
                        }
lbl14:
                        // 3 sources

                        while (true) {
                            var3_4 = (HttpResponse)v0.join();
                            if (var3_4 == null) continue block9;
                            if (var3_4.statusCode() == 200) {
                                VertxUtil.sendSignal((String)var0.api.getSiteURL());
                                VertxUtil.sendSignal((String)var0.instanceSignal, (Object)var3_4.bodyAsString());
                                return CompletableFuture.completedFuture(var3_4.bodyAsJsonObject());
                            }
                            if (var3_4.statusCode() == 302 || var3_4.statusCode() == 301 || var3_4.statusCode() == 401) {
                                v1 = var0.handleRedirect((HttpResponse)var3_4, false);
                                if (!v1.isDone()) {
                                    var5_6 = v1;
                                    return var5_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$fetchELJS(io.trickle.task.sites.shopify.Shopify int io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (int)var1_1, (HttpRequest)var2_2 /* !! */ , (CompletableFuture)var5_6, (HttpResponse)var3_4, null, (int)2));
                                }
lbl26:
                                // 3 sources

                                while (true) {
                                    v1.join();
                                    continue block9;
                                    break;
                                }
                            }
                            var0.logger.warn("Failed waiting for restock [EL]: status: '{}'", (Object)var3_4.statusCode());
                            v2 = VertxUtil.signalSleep((String)var0.instanceSignal, (long)var0.task.getMonitorDelay());
                            if (!v2.isDone()) {
                                var5_6 = v2;
                                return var5_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$fetchELJS(io.trickle.task.sites.shopify.Shopify int io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (int)var1_1, (HttpRequest)var2_2 /* !! */ , (CompletableFuture)var5_6, (HttpResponse)var3_4, null, (int)3));
                            }
lbl35:
                            // 3 sources

                            while (true) {
                                var4_5 /* !! */  = v2.join();
                                if (var4_5 /* !! */  == null) continue block9;
                                return CompletableFuture.completedFuture(new JsonObject(var4_5 /* !! */ .toString()));
                            }
                            break;
                        }
                    }
                    catch (Throwable var2_3) {
                        var0.logger.error("Failed waiting for restock [EL]: {}", (Object)var2_3.getMessage());
                        v3 = super.randomSleep(3000);
                        if (!v3.isDone()) {
                            var5_6 = v3;
                            return var5_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$fetchELJS(io.trickle.task.sites.shopify.Shopify int io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (int)var1_1, null, (CompletableFuture)var5_6, null, (Throwable)var2_3, (int)4));
                        }
lbl45:
                        // 3 sources

                        while (true) {
                            v3.join();
                            continue block9;
                            break;
                        }
                    }
                }
                return CompletableFuture.completedFuture(null);
            }
            case 1: {
                v0 = var3_4;
                ** continue;
            }
            case 2: {
                v1 = var3_4;
                var3_4 = var4_5 /* !! */ ;
                ** continue;
            }
            case 3: {
                v2 = var3_4;
                var3_4 = var4_5 /* !! */ ;
                ** continue;
            }
            case 4: {
                v3 = var3_4;
                var2_2 /* !! */  = var5_6;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture loginV2() {
        CompletableFuture completableFuture = this.GETREQ("Checking login page", this.api.challengePage(), 200, new String[]{"authenticity"});
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$loginV2(this, completableFuture2, null, null, null, null, 0, null, null, null, 1, arg_0));
        }
        String string = (String)completableFuture.join();
        String string2 = Utils.quickParseFirst((String)string, (Pattern[])new Pattern[]{AUTHENTICITY_TOKEN_PATTERN});
        CompletableFuture completableFuture3 = TokenController.solveBasicCaptcha((String)("https://" + this.api.getSiteURL() + "/challenge"));
        if (!completableFuture3.isDone()) {
            CompletableFuture completableFuture4 = completableFuture3;
            return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$loginV2(this, completableFuture4, string, string2, null, null, 0, null, null, null, 2, arg_0));
        }
        String string3 = ((CaptchaToken)completableFuture3.join()).getToken();
        MultiMap multiMap = this.api.challengeForm(string2, string3);
        int n = 0;
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            try {
                HttpRequest httpRequest = this.api.submitChallenge();
                CompletableFuture completableFuture5 = Request.send((HttpRequest)httpRequest, (MultiMap)multiMap);
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$loginV2(this, completableFuture6, string, string2, string3, multiMap, n, httpRequest, null, null, 3, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture5.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 302 || httpResponse.statusCode() == 301) {
                    CompletableFuture completableFuture7 = this.handleRedirect(httpResponse, false);
                    if (!completableFuture7.isDone()) {
                        CompletableFuture completableFuture8 = completableFuture7;
                        return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$loginV2(this, completableFuture8, string, string2, string3, multiMap, n, httpRequest, httpResponse, null, 4, arg_0));
                    }
                    if (((Boolean)completableFuture7.join()).booleanValue()) {
                        return CompletableFuture.completedFuture((Buffer)httpResponse.body());
                    }
                }
                if (httpResponse.statusCode() == 302) continue;
                this.logger.warn("Retrying login V2 challenge: status: '{}'", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture9 = VertxUtil.sleep((long)this.task.getRetryDelay());
                if (!completableFuture9.isDone()) {
                    CompletableFuture completableFuture10 = completableFuture9;
                    return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$loginV2(this, completableFuture10, string, string2, string3, multiMap, n, httpRequest, httpResponse, null, 5, arg_0));
                }
                completableFuture9.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error logging in V2: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture11 = super.randomSleep(3000);
                if (!completableFuture11.isDone()) {
                    CompletableFuture completableFuture12 = completableFuture11;
                    return ((CompletableFuture)completableFuture12.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$loginV2(this, completableFuture12, string, string2, string3, multiMap, n, null, null, throwable, 6, arg_0));
                }
                completableFuture11.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$getPaymentStatus(Shopify var0, String var1_1, int var2_2, HttpRequest var3_3, CompletableFuture var4_5, HttpResponse var5_6, Throwable var6_7, int var7_8, Object var8_9) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [8[CATCHBLOCK]], but top level block is 13[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public void initKeywords() {
        this.negativeKeywords.clear();
        this.positiveKeywords.clear();
        String[] stringArray = this.task.getKeywords();
        int n = stringArray.length;
        int n2 = 0;
        while (n2 < n) {
            String string = stringArray[n2];
            if ((string = string.toLowerCase()).charAt(0) == '-') {
                this.negativeKeywords.add(string.substring(1));
            } else {
                this.positiveKeywords.add(string);
            }
            ++n2;
        }
    }

    public CompletableFuture shippingForm(String string) {
        MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
        multiMap.add("_method", "patch");
        multiMap.add("authenticity_token", this.authenticity);
        multiMap.add("previous_step", "shipping_method");
        multiMap.add("step", "payment_method");
        MultiMap multiMap2 = multiMap;
        CompletableFuture<ShippingRateSupplier> completableFuture = this.shippingRate;
        if (!completableFuture.isDone()) {
            CompletableFuture<ShippingRateSupplier> completableFuture2 = completableFuture;
            String string2 = "checkout[shipping_rate][id]";
            MultiMap multiMap3 = multiMap2;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$shippingForm(this, string, multiMap, multiMap3, string2, completableFuture2, 1, arg_0));
        }
        multiMap2.add("checkout[shipping_rate][id]", completableFuture.join().get());
        for (String string3 : this.TEXTAREA_PARAMS) {
            multiMap.add(string3, "");
        }
        if (!this.TEXTAREA_PARAMS.isEmpty()) {
            multiMap.add(string + "-count", "" + this.TEXTAREA_PARAMS.size());
            multiMap.add(string + "-count", "fs_count");
        }
        multiMap.add("checkout[client_details][browser_width]", this.BROWSER_WIDTH);
        multiMap.add("checkout[client_details][browser_height]", this.BROWSER_HEIGHT);
        multiMap.add("checkout[client_details][javascript_enabled]", "1");
        multiMap.add("checkout[client_details][color_depth]", "24");
        multiMap.add("checkout[client_details][java_enabled]", "false");
        multiMap.add("checkout[client_details][browser_tz]", "" + TIMEZONE_OFFSET);
        return CompletableFuture.completedFuture(multiMap);
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$run(Shopify var0, CompletableFuture var1_1, int var2_4, String var3_7, CompletableFuture var4_8, String var5_10, JsonObject var6_11, int var7_12, int var8_43, Object var9_44) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [67[DOLOOP]], but top level block is 69[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public CompletableFuture fetchELJS() {
        int n = 0;
        this.logger.info("Monitoring [EL]");
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            this.instanceSignal = this.task.getKeywords()[0].replaceAll("collections/.*?/", "");
            try {
                HttpRequest httpRequest = this.api.elJS(this.instanceSignal);
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$fetchELJS(this, n, httpRequest, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 200) {
                    VertxUtil.sendSignal((String)this.api.getSiteURL());
                    VertxUtil.sendSignal((String)this.instanceSignal, (Object)httpResponse.bodyAsString());
                    return CompletableFuture.completedFuture(httpResponse.bodyAsJsonObject());
                }
                if (httpResponse.statusCode() == 302 || httpResponse.statusCode() == 301 || httpResponse.statusCode() == 401) {
                    CompletableFuture completableFuture3 = this.handleRedirect(httpResponse, false);
                    if (!completableFuture3.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture3;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$fetchELJS(this, n, httpRequest, completableFuture4, httpResponse, null, 2, arg_0));
                    }
                    completableFuture3.join();
                    continue;
                }
                this.logger.warn("Failed waiting for restock [EL]: status: '{}'", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture5 = VertxUtil.signalSleep((String)this.instanceSignal, (long)this.task.getMonitorDelay());
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$fetchELJS(this, n, httpRequest, completableFuture6, httpResponse, null, 3, arg_0));
                }
                Object t = completableFuture5.join();
                if (t == null) continue;
                return CompletableFuture.completedFuture(new JsonObject(t.toString()));
            }
            catch (Throwable throwable) {
                this.logger.error("Failed waiting for restock [EL]: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = super.randomSleep(3000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$fetchELJS(this, n, null, completableFuture7, null, throwable, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$loginV2(Shopify var0, CompletableFuture var1_1, String var2_2, String var3_3, String var4_4, MultiMap var5_5, int var6_7, HttpRequest var7_11, HttpResponse var8_12, Throwable var9_13, int var10_14, Object var11_15) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [10[CATCHBLOCK]], but top level block is 17[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public CompletableFuture getShippingPage(String string) {
        this.logger.info("Checking shipping...");
        int n = 0;
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            try {
                HttpRequest httpRequest = this.api.shippingPage(string);
                CompletableFuture completableFuture = this.safeCompleteRequest(httpRequest, "checking shipping");
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$getShippingPage(this, string, n, httpRequest, completableFuture2, null, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 200) {
                    this.updateCheckoutParams(httpResponse.bodyAsString());
                    this.updateShippingRate(httpResponse.bodyAsString());
                    return CompletableFuture.completedFuture(httpResponse.bodyAsString());
                }
                if (httpResponse.statusCode() != 302 && httpResponse.statusCode() != 301) continue;
                CompletableFuture completableFuture3 = this.handleRedirect(httpResponse, false);
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$getShippingPage(this, string, n, httpRequest, completableFuture4, httpResponse, null, null, 2, arg_0));
                }
                Boolean bl = (Boolean)completableFuture3.join();
                if (bl == null) {
                    return CompletableFuture.completedFuture(null);
                }
                if (!bl.booleanValue()) continue;
                this.logger.info("Unknown redirect. Handling..");
                CompletableFuture completableFuture5 = VertxUtil.randomSleep((long)this.task.getMonitorDelay());
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$getShippingPage(this, string, n, httpRequest, completableFuture6, httpResponse, bl, null, 3, arg_0));
                }
                completableFuture5.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Checking shipping: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = super.randomSleep(3000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$getShippingPage(this, string, n, null, completableFuture7, null, null, throwable, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$getProcessingPage(Shopify var0, String var1_1, int var2_2, int var3_3, HttpRequest var4_4, CompletableFuture var5_6, HttpResponse var6_7, int var7_10, Boolean var8_12, Throwable var9_13, int var10_14, Object var11_15) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [11[CATCHBLOCK]], but top level block is 19[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$monitorQueue(Shopify var0, String var1_1, CompletableFuture var2_2, HttpRequest var3_4, HttpResponse var4_5, REDIRECT_STATUS var5_6, Exception var6_7, int var7_8, Object var8_9) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [11[CATCHBLOCK]], but top level block is 19[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public CompletableFuture monitorQueue(String string) {
        this.logger.info("Monitoring queue...");
        while (this.running) {
            if (Utils.calculateMSLeftUntilHour() <= 15000) return CompletableFuture.completedFuture(null);
            if (this.cpMonitor != null) {
                if (this.cpMonitor.isDone()) return CompletableFuture.completedFuture(null);
            }
            if (!this.isSmart) return CompletableFuture.completedFuture(null);
            try {
                CompletableFuture completableFuture = this.atcAJAX(this.precartItemName, false);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$monitorQueue(this, string, completableFuture2, null, null, null, null, 1, arg_0));
                }
                completableFuture.join();
                HttpRequest httpRequest = this.api.emptyCheckout(false);
                CompletableFuture completableFuture3 = this.safeCompleteRequest(httpRequest, "monitoring queue");
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$monitorQueue(this, string, completableFuture4, httpRequest, null, null, null, 2, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture3.join();
                CompletableFuture completableFuture5 = this.clearCart();
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$monitorQueue(this, string, completableFuture6, httpRequest, httpResponse, null, null, 3, arg_0));
                }
                completableFuture5.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 302 || httpResponse.statusCode() == 301) {
                    REDIRECT_STATUS rEDIRECT_STATUS = REDIRECT_STATUS.checkRedirectStatus((String)httpResponse.getHeader("location"));
                    if (rEDIRECT_STATUS == REDIRECT_STATUS.CHECKPOINT) return CompletableFuture.completedFuture(null);
                    if (rEDIRECT_STATUS == REDIRECT_STATUS.PASSWORD) {
                        return CompletableFuture.completedFuture(null);
                    }
                    if (rEDIRECT_STATUS != REDIRECT_STATUS.CART) {
                        CompletableFuture completableFuture7 = this.handleRedirect(httpResponse, true);
                        if (!completableFuture7.isDone()) {
                            CompletableFuture completableFuture8 = completableFuture7;
                            return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$monitorQueue(this, string, completableFuture8, httpRequest, httpResponse, rEDIRECT_STATUS, null, 4, arg_0));
                        }
                        completableFuture7.join();
                    }
                    this.logger.info("Monitoring queue...");
                    CompletableFuture completableFuture9 = VertxUtil.randomSleep((long)this.task.getMonitorDelay());
                    if (!completableFuture9.isDone()) {
                        CompletableFuture completableFuture10 = completableFuture9;
                        return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$monitorQueue(this, string, completableFuture10, httpRequest, httpResponse, rEDIRECT_STATUS, null, 5, arg_0));
                    }
                    completableFuture9.join();
                    continue;
                }
                this.logger.info("Unable to properly monitoring queue {}", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture11 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
                if (!completableFuture11.isDone()) {
                    CompletableFuture completableFuture12 = completableFuture11;
                    return ((CompletableFuture)completableFuture12.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$monitorQueue(this, string, completableFuture12, httpRequest, httpResponse, null, null, 6, arg_0));
                }
                completableFuture11.join();
            }
            catch (Exception exception) {
                this.logger.error("Error monitoring queue: {}", (Object)exception.getMessage());
                CompletableFuture completableFuture = VertxUtil.randomSleep((long)3000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture13 = completableFuture;
                    return ((CompletableFuture)completableFuture13.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$monitorQueue(this, string, completableFuture13, null, null, null, exception, 7, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture shippingAndBillingPost() {
        this.logger.info("Generating checkout [API]...");
        int n = 0;
        int n2 = 0;
        while (this.running) {
            if (n2++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            try {
                HttpRequest httpRequest = this.api.atcWallets();
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest, (JsonObject)this.contactAPIAndCartForm(n != 0));
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$shippingAndBillingPost(this, n, n2, httpRequest, completableFuture2, null, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 200) return CompletableFuture.completedFuture(httpResponse.bodyAsJsonObject().getJsonObject("checkout").getString("web_url"));
                if (httpResponse.statusCode() == 201) {
                    return CompletableFuture.completedFuture(httpResponse.bodyAsJsonObject().getJsonObject("checkout").getString("web_url"));
                }
                if (httpResponse.statusCode() == 202) {
                    if (!httpResponse.bodyAsString().contains(this.task.getProfile().getAddress1())) continue;
                    return CompletableFuture.completedFuture(httpResponse.bodyAsJsonObject().getJsonObject("checkout").getString("web_url"));
                }
                if (httpResponse.statusCode() == 409 && n2 < 5) continue;
                if (httpResponse.statusCode() == 429 && this.api.getCookies().contains("_checkout_queue_token")) {
                    CompletableFuture completableFuture3 = null;
                    if (n == 0) {
                        completableFuture3 = this.atcAJAX(this.instanceSignal, true);
                    }
                    CompletableFuture completableFuture4 = this.handleQueueNew();
                    if (!completableFuture4.isDone()) {
                        CompletableFuture completableFuture5 = completableFuture4;
                        return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$shippingAndBillingPost(this, n, n2, httpRequest, completableFuture3, httpResponse, completableFuture5, null, 2, arg_0));
                    }
                    completableFuture4.join();
                    if (completableFuture3 == null) continue;
                    CompletableFuture completableFuture6 = completableFuture3;
                    if (!completableFuture6.isDone()) {
                        CompletableFuture completableFuture7 = completableFuture6;
                        return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$shippingAndBillingPost(this, n, n2, httpRequest, completableFuture3, httpResponse, completableFuture7, null, 3, arg_0));
                    }
                    JsonObject jsonObject = (JsonObject)completableFuture6.join();
                    if (jsonObject == null) continue;
                    n = 1;
                    continue;
                }
                if (httpResponse.statusCode() == 422) {
                    if (n == 0) {
                        n = 1;
                        CompletableFuture completableFuture8 = this.smoothCart();
                        if (!completableFuture8.isDone()) {
                            CompletableFuture completableFuture9 = completableFuture8;
                            return ((CompletableFuture)completableFuture9.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$shippingAndBillingPost(this, n, n2, httpRequest, completableFuture9, httpResponse, null, null, 4, arg_0));
                        }
                        completableFuture8.join();
                        continue;
                    }
                    this.logger.warn("Retrying generating checkout [API]: status: '{}'", (Object)httpResponse.statusCode());
                    CompletableFuture completableFuture10 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
                    if (!completableFuture10.isDone()) {
                        CompletableFuture completableFuture11 = completableFuture10;
                        return ((CompletableFuture)completableFuture11.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$shippingAndBillingPost(this, n, n2, httpRequest, completableFuture11, httpResponse, null, null, 5, arg_0));
                    }
                    completableFuture10.join();
                    continue;
                }
                this.logger.warn("Retrying generating checkout [API]: status: '{}'", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture12 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
                if (!completableFuture12.isDone()) {
                    CompletableFuture completableFuture13 = completableFuture12;
                    return ((CompletableFuture)completableFuture13.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$shippingAndBillingPost(this, n, n2, httpRequest, completableFuture13, httpResponse, null, null, 6, arg_0));
                }
                completableFuture12.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error generating checkout [API]: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = super.randomSleep(3000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture14 = completableFuture;
                    return ((CompletableFuture)completableFuture14.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$shippingAndBillingPost(this, n, n2, null, completableFuture14, null, null, throwable, 7, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public void updateCheckoutParams(String string) {
        Matcher matcher = AUTHENTICITY_TOKEN_PATTERN.matcher(string);
        if (matcher.find()) {
            this.authenticity = matcher.group(1);
        } else {
            this.logger.info("No checkout token found");
        }
        this.TEXTAREA_PARAMS.clear();
        Matcher matcher2 = TEXTAREA_PARAMS_PATTERN.matcher(string);
        while (matcher2.find()) {
            this.TEXTAREA_PARAMS.add(matcher2.group(1));
        }
    }

    public CompletableFuture initShopDetails() {
        CompletableFuture completableFuture = this.fetchMeta();
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$initShopDetails(this, completableFuture2, 1, arg_0));
        }
        JsonObject jsonObject = ((JsonObject)completableFuture.join()).getJsonObject("paymentInstruments");
        if (jsonObject == null) return CompletableFuture.completedFuture(null);
        this.api.setShopID("" + jsonObject.getJsonObject("checkoutConfig").getNumber("shopId"));
        this.api.setAPIToken(jsonObject.getString("accessToken"));
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$fetchProductsJSON(Shopify var0, int var1_1, int var2_2, Lock var3_3, CompletionStage var4_4, HttpRequest var5_6, CompletableFuture var6_7, HttpResponse var7_8, JsonObject var8_9, SharedData var9_10, Iterator var10_11, String var11_12, Throwable var12_13, int var13_14, Object var14_15) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [10[CATCHBLOCK]], but top level block is 18[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$atcBasic(Shopify var0, String var1_1, int var2_2, HttpRequest var3_3, CompletableFuture var4_5, HttpResponse var5_6, Throwable var6_7, int var7_8, Object var8_9) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [8[CATCHBLOCK]], but top level block is 13[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public Buffer newQueueBody() {
        String string = this.api.getWebClient().cookieStore().getCookieValue("_checkout_queue_token");
        return Buffer.buffer((String)("{\"query\":\"\\n      {\\n        poll(token: $token) {\\n          token\\n          pollAfter\\n          queueEtaSeconds\\n          productVariantAvailability {\\n            id\\n            available\\n          }\\n        }\\n      }\\n    \",\"variables\":{\"token\":\"" + string.replace("%3D", "=") + "\"}}"));
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$parseNewQueueJson(Shopify var0, JsonObject var1_1, String var2_2, String var3_3, long var4_4, String var6_5, String var7_6, CompletableFuture var8_7, int var9_8, Object var10_9) {
        switch (var9_8) {
            case 0: {
                var2_2 = var1_1.getJsonObject("data").getJsonObject("poll").getString("__typename");
                var3_3 = var1_1.getJsonObject("data").getJsonObject("poll").getString("token");
                var0.api.getWebClient().cookieStore().removeAnyMatch("_checkout_queue_token");
                var0.api.getWebClient().cookieStore().put("_checkout_queue_token", var3_3, var0.api.getSiteURL());
                if (!var2_2.equals("PollContinue")) ** GOTO lbl25
                var4_4 = var1_1.getJsonObject("data").getJsonObject("poll").getLong("queueEtaSeconds");
                var6_5 = Instant.ofEpochSecond(Instant.now().getEpochSecond() + var4_4).atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                var7_6 = var1_1.getJsonObject("data").getJsonObject("poll").getString("pollAfter");
                var0.logger.info("Waiting in new queue. Expected wait time -> ({}) [{}][{}]", (Object)var4_4, (Object)var6_5, (Object)true);
                if (var0.prevTime != var4_4) ** GOTO lbl19
                v0 = VertxUtil.hardCodedSleep((long)(var0.calculatePollTime(var7_6) + 500L));
                if (!v0.isDone()) {
                    var8_7 = v0;
                    return var8_7.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$parseNewQueueJson(io.trickle.task.sites.shopify.Shopify io.vertx.core.json.JsonObject java.lang.String java.lang.String long java.lang.String java.lang.String java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (JsonObject)var1_1, (String)var2_2, (String)var3_3, (long)var4_4, (String)var6_5, (String)var7_6, (CompletableFuture)var8_7, (int)1));
                }
                ** GOTO lbl37
lbl19:
                // 1 sources

                var0.prevTime = var4_4;
                v1 = VertxUtil.hardCodedSleep((long)var0.calculatePollTime(var7_6));
                if (!v1.isDone()) {
                    var8_7 = v1;
                    return var8_7.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$parseNewQueueJson(io.trickle.task.sites.shopify.Shopify io.vertx.core.json.JsonObject java.lang.String java.lang.String long java.lang.String java.lang.String java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (JsonObject)var1_1, (String)var2_2, (String)var3_3, (long)var4_4, (String)var6_5, (String)var7_6, (CompletableFuture)var8_7, (int)2));
                }
                ** GOTO lbl45
lbl25:
                // 1 sources

                if (var2_2.equals("PollComplete")) {
                    var0.logger.info("Passed new queue.");
                    Analytics.queuePasses.increment();
                    return CompletableFuture.completedFuture(true);
                }
                var0.logger.info("Error with new queue. Please wait.");
                v2 = VertxUtil.randomSleep((long)3000L);
                if (!v2.isDone()) {
                    var8_7 = v2;
                    return var8_7.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$parseNewQueueJson(io.trickle.task.sites.shopify.Shopify io.vertx.core.json.JsonObject java.lang.String java.lang.String long java.lang.String java.lang.String java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (JsonObject)var1_1, (String)var2_2, (String)var3_3, (long)0L, null, null, (CompletableFuture)var8_7, (int)4));
                }
                ** GOTO lbl55
            }
            case 1: {
                v0 = var8_7;
lbl37:
                // 2 sources

                v0.join();
lbl39:
                // 2 sources

                while (!(v3 = VertxUtil.hardCodedSleep((long)1250L)).isDone()) {
                    var8_7 = v3;
                    return var8_7.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$parseNewQueueJson(io.trickle.task.sites.shopify.Shopify io.vertx.core.json.JsonObject java.lang.String java.lang.String long java.lang.String java.lang.String java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (JsonObject)var1_1, (String)var2_2, (String)var3_3, (long)var4_4, (String)var6_5, (String)var7_6, (CompletableFuture)var8_7, (int)3));
                }
                ** GOTO lbl50
            }
            case 2: {
                v1 = var8_7;
lbl45:
                // 2 sources

                v1.join();
                ** GOTO lbl39
            }
            case 3: {
                v3 = var8_7;
lbl50:
                // 2 sources

                v3.join();
                return CompletableFuture.completedFuture(false);
            }
            case 4: {
                v2 = var8_7;
lbl55:
                // 2 sources

                v2.join();
                return CompletableFuture.completedFuture(false);
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture login() {
        AccountController accountController = (AccountController)Engine.get().getModule(Controller.ACCOUNT);
        CompletableFuture completableFuture = accountController.findAccount(this.task.getProfile().getEmail(), true).toCompletionStage().toCompletableFuture();
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$login(this, accountController, completableFuture2, null, null, 0, null, null, null, null, null, 1, arg_0));
        }
        Account account = (Account)completableFuture.join();
        if (account != null) {
            account.setSite(this.api.getSiteURL());
            CompletableFuture completableFuture3 = this.sessionLogon(account);
            if (!completableFuture3.isDone()) {
                CompletableFuture completableFuture4 = completableFuture3;
                return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$login(this, accountController, completableFuture4, account, null, 0, null, null, null, null, null, 2, arg_0));
            }
            boolean bl = (Boolean)completableFuture3.join();
            if (bl) {
                return CompletableFuture.completedFuture(null);
            }
        } else {
            account = Shopify.rotateAccount();
            account.setSite(this.api.getSiteURL());
        }
        MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
        multiMap.set("form_type", "customer_login");
        multiMap.set("utf8", "\u2713");
        multiMap.set("customer[email]", account.getUser());
        multiMap.set("customer[password]", account.getPass());
        multiMap.set("return_url", "/account");
        this.logger.info("Logging in.");
        int n = 0;
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            MultiMap multiMap2 = multiMap;
            CompletableFuture completableFuture5 = TokenController.solveBasicCaptcha((String)("https://" + this.api.getSiteURL() + "/account/login?return_url=%2Faccount"));
            if (!completableFuture5.isDone()) {
                CompletableFuture completableFuture6 = completableFuture5;
                String string = "recaptcha-v3-token";
                MultiMap multiMap3 = multiMap2;
                return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$login(this, accountController, completableFuture6, account, multiMap, n, multiMap3, string, null, null, null, 3, arg_0));
            }
            multiMap2.set("recaptcha-v3-token", ((CaptchaToken)completableFuture5.join()).getToken());
            try {
                HttpRequest httpRequest = this.api.login();
                CompletableFuture completableFuture7 = Request.send((HttpRequest)httpRequest, (MultiMap)multiMap);
                if (!completableFuture7.isDone()) {
                    CompletableFuture completableFuture8 = completableFuture7;
                    return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$login(this, accountController, completableFuture8, account, multiMap, n, null, null, httpRequest, null, null, 4, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture7.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 302 || httpResponse.statusCode() == 301) {
                    CompletableFuture completableFuture9 = this.handleRedirect(httpResponse, false);
                    if (!completableFuture9.isDone()) {
                        CompletableFuture completableFuture10 = completableFuture9;
                        return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$login(this, accountController, completableFuture10, account, multiMap, n, null, null, httpRequest, httpResponse, null, 5, arg_0));
                    }
                    if (((Boolean)completableFuture9.join()).booleanValue()) {
                        account.setSessionString(this.api.getCookies().asJson().encode());
                        this.vertx.eventBus().send("accounts.writer.session", (Object)account);
                        return CompletableFuture.completedFuture((Buffer)httpResponse.body());
                    }
                }
                if (httpResponse.statusCode() == 403) {
                    this.logger.warn("Cloudflare block! Retrying...");
                    CompletableFuture completableFuture11 = VertxUtil.sleep((long)this.task.getRetryDelay());
                    if (!completableFuture11.isDone()) {
                        CompletableFuture completableFuture12 = completableFuture11;
                        return ((CompletableFuture)completableFuture12.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$login(this, accountController, completableFuture12, account, multiMap, n, null, null, httpRequest, httpResponse, null, 6, arg_0));
                    }
                    completableFuture11.join();
                    continue;
                }
                if (httpResponse.statusCode() == 302) continue;
                this.logger.warn("Retrying login: status: '{}'", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture13 = VertxUtil.sleep((long)this.task.getRetryDelay());
                if (!completableFuture13.isDone()) {
                    CompletableFuture completableFuture14 = completableFuture13;
                    return ((CompletableFuture)completableFuture14.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$login(this, accountController, completableFuture14, account, multiMap, n, null, null, httpRequest, httpResponse, null, 7, arg_0));
                }
                completableFuture13.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error logging in: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture15 = super.randomSleep(3000);
                if (!completableFuture15.isDone()) {
                    CompletableFuture completableFuture16 = completableFuture15;
                    return ((CompletableFuture)completableFuture16.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$login(this, accountController, completableFuture16, account, multiMap, n, null, null, null, null, throwable, 8, arg_0));
                }
                completableFuture15.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public MultiMap upsellForm(String string) {
        MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
        multiMap.set("properties[" + (String)this.propertiesPair.first + "]", (String)this.propertiesPair.second);
        if (((String)this.propertiesPair.first).contains("upsell")) {
            multiMap.set("option-0", this.size);
        }
        multiMap.set("id", string);
        multiMap.set("quantity", "1");
        return multiMap;
    }

    public CompletableFuture solveCheckpointCaptcha(String string) {
        try {
            SolveFuture solveFuture;
            this.logger.info("Checkpoint needs solving");
            CaptchaToken captchaToken = new CaptchaToken("https://" + this.api.getSiteURL() + "/checkpoint?return_to=https%3A%2F%2F" + this.api.getSiteURL() + "%2Fcheckout", true, this.api.proxyStringFull(), string, this);
            SolveFuture solveFuture2 = solveFuture = ((TokenController)Engine.get().getModule(Controller.TOKEN)).solve(captchaToken);
            if (!solveFuture2.toCompletableFuture().isDone()) {
                SolveFuture solveFuture3 = solveFuture2;
                return solveFuture3.exceptionally(Function.identity()).thenCompose(arg_0 -> Shopify.async$solveCheckpointCaptcha(this, string, captchaToken, solveFuture, solveFuture3, 1, arg_0)).toCompletableFuture();
            }
            solveFuture2.toCompletableFuture().join();
            return CompletableFuture.completedFuture(captchaToken);
        }
        catch (Throwable throwable) {
            this.logger.error("HARVEST ERR: {}", (Object)throwable.getMessage());
            return CompletableFuture.completedFuture(null);
        }
    }

    public CompletableFuture atcBasic(String string) {
        int n = 0;
        this.logger.info("Attempting ATC [Basic]");
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            try {
                HttpRequest httpRequest = this.api.basicATC();
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest, (MultiMap)this.atcBasicForm(string));
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$atcBasic(this, string, n, httpRequest, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if ((httpResponse.statusCode() == 302 || httpResponse.statusCode() == 301) && httpResponse.getHeader("location").endsWith("/cart")) {
                    return CompletableFuture.completedFuture(null);
                }
                if (httpResponse.statusCode() == 302 || httpResponse.statusCode() == 301) {
                    CompletableFuture completableFuture3 = this.handleRedirect(httpResponse, false);
                    if (!completableFuture3.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture3;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$atcBasic(this, string, n, httpRequest, completableFuture4, httpResponse, null, 2, arg_0));
                    }
                    completableFuture3.join();
                }
                this.logger.warn("Failed attempting ATC [Basic]: status: '{}'", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture5 = VertxUtil.sleep((long)this.task.getRetryDelay());
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$atcBasic(this, string, n, httpRequest, completableFuture6, httpResponse, null, 3, arg_0));
                }
                completableFuture5.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Failed attempting ATC [Basic]: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = super.randomSleep(3000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$atcBasic(this, string, n, null, completableFuture7, null, throwable, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public String getFakeTokenSalt() {
        String string = "abcde0123456789";
        StringBuilder stringBuilder = new StringBuilder();
        while (stringBuilder.length() < 32) {
            int n = (int)(ThreadLocalRandom.current().nextFloat() * (float)"abcde0123456789".length());
            stringBuilder.append("abcde0123456789".charAt(n));
        }
        return stringBuilder.toString();
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$shippingAndBillingPut(Shopify var0, String var1_1, int var2_2, HttpRequest var3_3, CompletableFuture var4_5, HttpResponse var5_6, Throwable var6_7, int var7_8, Object var8_9) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [7[CATCHBLOCK]], but top level block is 11[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public CompletableFuture genCheckoutURLViaCart() {
        int n = 0;
        this.logger.info("Generating checkout URL [c]...");
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            try {
                HttpRequest httpRequest = this.api.emptyCheckoutViaCart();
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest, (MultiMap)this.api.emptyCheckoutViaCartForm());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$genCheckoutURLViaCart(this, n, httpRequest, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 302 || httpResponse.statusCode() == 301) {
                    CompletableFuture completableFuture3 = this.handleRedirect(httpResponse, true);
                    if (!completableFuture3.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture3;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$genCheckoutURLViaCart(this, n, httpRequest, completableFuture4, httpResponse, null, 2, arg_0));
                    }
                    if (((Boolean)completableFuture3.join()).booleanValue()) {
                        return CompletableFuture.completedFuture(httpResponse.getHeader("location"));
                    }
                }
                if (httpResponse.statusCode() == 302) continue;
                this.logger.warn("Failed generating checkout URL [c]: status: '{}'", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture5 = VertxUtil.sleep((long)this.task.getRetryDelay());
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$genCheckoutURLViaCart(this, n, httpRequest, completableFuture6, httpResponse, null, 3, arg_0));
                }
                completableFuture5.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Failed generating checkout URL [c]: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = VertxUtil.randomSleep((long)3000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$genCheckoutURLViaCart(this, n, null, completableFuture7, null, throwable, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$genPaymentToken(Shopify var0, JsonObject var1_1, int var2_2, WebClient var3_3, HttpRequest var4_4, CompletableFuture var5_6, HttpResponse var6_7, Throwable var7_8, int var8_9, Object var9_10) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [7[CATCHBLOCK]], but top level block is 11[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$clearCart(Shopify var0, int var1_1, HttpRequest var2_2, CompletableFuture var3_4, HttpResponse var4_5, Throwable var5_6, int var6_7, Object var7_8) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [8[CATCHBLOCK]], but top level block is 13[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$processingForm(Shopify var0, String var1_1, MultiMap var2_2, CompletableFuture var3_3, PaymentTokenSupplier var4_4, int var5_5, Object var6_7) {
        switch (var5_5) {
            case 0: {
                var2_2 = MultiMap.caseInsensitiveMultiMap();
                var2_2.add("_method", "patch");
                var2_2.add("authenticity_token", var0.authenticity);
                var2_2.add("previous_step", "payment_method");
                var2_2.add("step", "");
                v0 = var0.paymentToken;
                if (!v0.isDone()) {
                    var6_7 = v0;
                    return var6_7.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$processingForm(io.trickle.task.sites.shopify.Shopify java.lang.String io.vertx.core.MultiMap java.util.concurrent.CompletableFuture io.trickle.task.sites.shopify.util.PaymentTokenSupplier int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (String)var1_1, (MultiMap)var2_2, (CompletableFuture)var6_7, null, (int)1));
                }
                ** GOTO lbl19
            }
            case 1: {
                v0 = var3_3;
lbl19:
                // 2 sources

                var3_3 = (PaymentTokenSupplier)v0.join();
                var2_2.add("s", var3_3.get());
                for (String var5_6 : var0.TEXTAREA_PARAMS) {
                    var2_2.add(var5_6, "");
                }
                if (!var0.TEXTAREA_PARAMS.isEmpty()) {
                    var2_2.add(var1_1 + "-count", "" + var0.TEXTAREA_PARAMS.size());
                    var2_2.add(var1_1 + "-count", "fs_count");
                }
                if (var0.paymentGateway != null) ** GOTO lbl42
                v1 = var0.getProcessingPage(var1_1, false);
                if (!v1.isDone()) {
                    var6_7 = v1;
                    return var6_7.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$processingForm(io.trickle.task.sites.shopify.Shopify java.lang.String io.vertx.core.MultiMap java.util.concurrent.CompletableFuture io.trickle.task.sites.shopify.util.PaymentTokenSupplier int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (String)var1_1, (MultiMap)var2_2, (CompletableFuture)var6_7, (PaymentTokenSupplier)var3_3, (int)2));
                }
                ** GOTO lbl40
            }
            case 2: {
                v1 = var3_3;
                var3_3 = var4_4;
lbl40:
                // 2 sources

                v1.join();
lbl42:
                // 2 sources

                var2_2.add("checkout[payment_gateway]", var0.paymentGateway);
                var2_2.add("checkout[credit_card][vault]", "false");
                var2_2.add("checkout[different_billing_address]", "false");
                if (var0.task.getSite() == Site.SHOPNICEKICKS) {
                    var2_2.add("checkout[attributes][I-agree-to-the-Terms-and-Conditions]", "Yes");
                }
                if (var0.task.getSite() == Site.KITH) {
                    var2_2.add("checkout[remember_me]", "false");
                }
                if (var0.task.getSite() != Site.SHOPNICEKICKS) {
                    var2_2.add("checkout[remember_me]", "0");
                }
                var2_2.add("checkout[vault_phone]", var0.task.getProfile().getCountry().equals("JAPAN") != false ? "+81" + var0.task.getProfile().getPhone() : "+1" + var0.task.getProfile().getPhone());
                if (var0.task.getSite() == Site.SHOEPALACE) {
                    var2_2.add("checkout[attributes][I-agree-to-the-Terms-and-Conditions]", "Yes");
                }
                if (var0.price != null) {
                    var2_2.add("checkout[total_price]", String.valueOf(var0.price));
                }
                var2_2.add("complete", "1");
                var2_2.add("checkout[client_details][browser_width]", var0.BROWSER_WIDTH);
                var2_2.add("checkout[client_details][browser_height]", var0.BROWSER_HEIGHT);
                var2_2.add("checkout[client_details][javascript_enabled]", "1");
                var2_2.add("checkout[client_details][color_depth]", "24");
                var2_2.add("checkout[client_details][java_enabled]", "false");
                var2_2.add("checkout[client_details][browser_tz]", "" + Shopify.TIMEZONE_OFFSET);
                return CompletableFuture.completedFuture(var2_2);
            }
        }
        throw new IllegalArgumentException();
    }

    public static Account rotateAccount() {
        return ((AccountController)Engine.get().getModule(Controller.ACCOUNT)).getAccountCyclic();
    }

    public MultiMap contactForm(String string) {
        MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
        multiMap.add("_method", "patch");
        multiMap.add("authenticity_token", this.authenticity);
        multiMap.add("previous_step", "contact_information");
        multiMap.add("step", "shipping_method");
        multiMap.add("checkout[email]", this.task.getProfile().getEmail());
        multiMap.add("checkout[buyer_accepts_marketing]", "0");
        multiMap.add("checkout[shipping_address][first_name]", "");
        multiMap.add("checkout[shipping_address][last_name]", "");
        if (this.task.getSite() == Site.SHOPNICEKICKS) {
            multiMap.add("checkout[shipping_address][company]", "");
        }
        multiMap.add("checkout[shipping_address][address1]", "");
        multiMap.add("checkout[shipping_address][address2]", "");
        multiMap.add("checkout[shipping_address][city]", "");
        multiMap.add("checkout[shipping_address][country]", "");
        multiMap.add("checkout[shipping_address][province]", "");
        multiMap.add("checkout[shipping_address][zip]", "");
        multiMap.add("checkout[shipping_address][phone]", "");
        multiMap.add("checkout[shipping_address][first_name]", this.task.getProfile().getFirstName());
        multiMap.add("checkout[shipping_address][last_name]", this.task.getProfile().getLastName());
        if (this.task.getSite() == Site.SHOPNICEKICKS) {
            multiMap.add("checkout[shipping_address][company]", "");
        }
        multiMap.add("checkout[shipping_address][address1]", this.task.getProfile().getAddress1());
        multiMap.add("checkout[shipping_address][address2]", this.task.getProfile().getAddress2());
        multiMap.add("checkout[shipping_address][city]", this.task.getProfile().getCity());
        multiMap.add("checkout[shipping_address][country]", this.task.getProfile().getFullCountry());
        multiMap.add("checkout[shipping_address][province]", this.task.getProfile().getCountry().equals("US") ? this.task.getProfile().getState() : this.task.getProfile().getFullState());
        multiMap.add("checkout[shipping_address][zip]", this.task.getProfile().getZip());
        multiMap.add("checkout[shipping_address][phone]", this.task.getProfile().getPhone());
        if (this.task.getSite() == Site.SHOPNICEKICKS) {
            multiMap.add("checkout[remember_me]", "");
            multiMap.add("checkout[remember_me]", "0");
        }
        for (String string2 : this.TEXTAREA_PARAMS) {
            multiMap.add(string2, "");
        }
        if (!this.TEXTAREA_PARAMS.isEmpty()) {
            multiMap.add(string + "-count", "" + this.TEXTAREA_PARAMS.size());
            multiMap.add(string + "-count", "fs_count");
        }
        multiMap.add("checkout[client_details][browser_width]", this.BROWSER_WIDTH);
        multiMap.add("checkout[client_details][browser_height]", this.BROWSER_HEIGHT);
        multiMap.add("checkout[client_details][javascript_enabled]", "1");
        multiMap.add("checkout[client_details][color_depth]", "24");
        multiMap.add("checkout[client_details][java_enabled]", "false");
        multiMap.add("checkout[client_details][browser_tz]", "" + TIMEZONE_OFFSET);
        return multiMap;
    }

    public CompletableFuture processingAPIForm() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("complete", (Object)"1");
        jsonObject.put("field_type", (Object)"hidden");
        JsonObject jsonObject2 = jsonObject;
        CompletableFuture<PaymentTokenSupplier> completableFuture = this.paymentToken;
        if (!completableFuture.isDone()) {
            CompletableFuture<PaymentTokenSupplier> completableFuture2 = completableFuture;
            String string = "s";
            JsonObject jsonObject3 = jsonObject2;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$processingAPIForm(this, jsonObject, jsonObject3, string, completableFuture2, null, null, null, null, 1, arg_0));
        }
        jsonObject2.put("s", (Object)completableFuture.join().get());
        if (this.hasShippingAlreadySubmittedAPI) return CompletableFuture.completedFuture(jsonObject);
        JsonObject jsonObject4 = jsonObject;
        JsonObject jsonObject5 = new JsonObject();
        JsonObject jsonObject6 = new JsonObject();
        CompletableFuture<ShippingRateSupplier> completableFuture3 = this.shippingRate;
        if (!completableFuture3.isDone()) {
            CompletableFuture<ShippingRateSupplier> completableFuture4 = completableFuture3;
            String string = "id";
            JsonObject jsonObject7 = jsonObject6;
            String string2 = "shipping_rate";
            JsonObject jsonObject8 = jsonObject5;
            String string3 = "checkout";
            JsonObject jsonObject9 = jsonObject4;
            return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$processingAPIForm(this, jsonObject, jsonObject9, string3, completableFuture4, jsonObject8, string2, jsonObject7, string, 2, arg_0));
        }
        jsonObject4.put("checkout", (Object)jsonObject5.put("shipping_rate", (Object)jsonObject6.put("id", (Object)completableFuture3.join().get())));
        return CompletableFuture.completedFuture(jsonObject);
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$smoothCart(Shopify var0, CompletableFuture var1_1, int var2_2, Object var3_3) {
        switch (var2_2) {
            case 0: {
                if (var0.propertiesPair == null) ** GOTO lbl9
                v0 = var0.atcUpsell(var0.instanceSignal);
                if (!v0.isDone()) {
                    var1_1 = v0;
                    return var1_1.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$smoothCart(io.trickle.task.sites.shopify.Shopify java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (CompletableFuture)var1_1, (int)1));
                }
                ** GOTO lbl16
lbl9:
                // 1 sources

                v1 = var0.atcAJAX(var0.instanceSignal, false);
                if (!v1.isDone()) {
                    var1_1 = v1;
                    return var1_1.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$smoothCart(io.trickle.task.sites.shopify.Shopify java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (CompletableFuture)var1_1, (int)2));
                }
                ** GOTO lbl22
            }
            case 1: {
                v0 = var1_1;
lbl16:
                // 2 sources

                v0.join();
lbl18:
                // 2 sources

                return CompletableFuture.completedFuture(null);
            }
            case 2: {
                v1 = var1_1;
lbl22:
                // 2 sources

                v1.join();
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public MultiMap atcBasicForm(String string) {
        MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
        multiMap.set("id", string);
        multiMap.set("add", "");
        return multiMap;
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$shippingAndBillingPost(Shopify var0, int var1_1, int var2_2, HttpRequest var3_3, CompletableFuture var4_5, HttpResponse var5_6, CompletableFuture var6_7, Throwable var7_8, int var8_9, Object var9_10) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [11[CATCHBLOCK]], but top level block is 19[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$sessionLogon(Shopify var0, Account var1_1, String var2_2, JsonArray var3_3, CompletableFuture var4_4, int var5_7, Object var6_10) {
        switch (var5_7) {
            case 0: {
                var2_2 = var1_1.lookupSession();
                if (var2_2.isBlank() != false) return CompletableFuture.completedFuture(false);
                var3_3 = new JsonArray(var2_2);
                if (var3_3.isEmpty() != false) return CompletableFuture.completedFuture(false);
                for (var4_5 = 0; var4_5 < var3_3.size(); ++var4_5) {
                    try {
                        var5_8 = var3_3.getString(var4_5);
                        var6_10 = ClientCookieDecoder.STRICT.decode(var5_8);
                        if (var6_10 == null || !var6_10.name().equals("_secure_session_id")) continue;
                        var0.api.getCookies().put((Cookie)var6_10);
                        continue;
                    }
                    catch (Throwable var5_9) {
                        var0.logger.warn("Error parsing session state: {}", (Object)var5_9.getMessage());
                    }
                }
                try {
                    v0 = var0.GETREQ("Validating Session", var0.api.accountConfirmPage(), new Integer[]{200, 302, 401}, new String[]{""});
                    if (!v0.isDone()) {
                        var7_11 = v0;
                        return var7_11.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$sessionLogon(io.trickle.task.sites.shopify.Shopify io.trickle.account.Account java.lang.String io.vertx.core.json.JsonArray java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (Account)var1_1, (String)var2_2, (JsonArray)var3_3, (CompletableFuture)var7_11, (int)1));
                    }
                    ** GOTO lbl29
                }
                catch (Throwable var4_6) {
                    var0.logger.warn("Error validating session: {}", (Object)var4_6.getMessage());
                }
                return CompletableFuture.completedFuture(false);
            }
            case 1: {
                v0 = var4_4;
lbl29:
                // 2 sources

                if ((var4_4 = (String)v0.join()).contains("order-history") != false) return CompletableFuture.completedFuture(true);
                if (var4_4.contains("/account/logout") != false) return CompletableFuture.completedFuture(true);
                if (var4_4.endsWith("/password") == false) return CompletableFuture.completedFuture(false);
                return CompletableFuture.completedFuture(true);
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture walletsSubmitShipping(String string) {
        this.logger.info("Loading shipping... [API]");
        int n = 0;
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            try {
                HttpRequest httpRequest = this.api.calculateTaxesWallets(string);
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$walletsSubmitShipping(this, string, n, httpRequest, completableFuture2, null, null, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 200 || httpResponse.statusCode() == 202) {
                    JsonObject jsonObject = httpResponse.bodyAsJsonObject().getJsonObject("checkout");
                    JsonObject jsonObject2 = jsonObject;
                    if (jsonObject2 != null) {
                        jsonObject2 = jsonObject2.getJsonObject("shipping_line");
                    }
                    if (jsonObject2 != null && !jsonObject2.isEmpty()) {
                        this.isShippingSubmittedWithWallets = true;
                        return CompletableFuture.completedFuture(jsonObject.getJsonArray("line_items").encode());
                    }
                    this.logger.info("Refetching shipping rate [API]");
                    if (n <= 5) continue;
                    CompletableFuture completableFuture3 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
                    if (!completableFuture3.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture3;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$walletsSubmitShipping(this, string, n, httpRequest, completableFuture4, httpResponse, jsonObject, jsonObject2, null, 2, arg_0));
                    }
                    completableFuture3.join();
                    continue;
                }
                if (httpResponse.statusCode() == 429 && this.api.getCookies().contains("_checkout_queue_token")) {
                    CompletableFuture completableFuture5 = this.handleQueueNew();
                    if (!completableFuture5.isDone()) {
                        CompletableFuture completableFuture6 = completableFuture5;
                        return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$walletsSubmitShipping(this, string, n, httpRequest, completableFuture6, httpResponse, null, null, null, 3, arg_0));
                    }
                    completableFuture5.join();
                    continue;
                }
                this.logger.warn("Waiting to load shipping [API]: status: '{}'", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture7 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
                if (!completableFuture7.isDone()) {
                    CompletableFuture completableFuture8 = completableFuture7;
                    return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$walletsSubmitShipping(this, string, n, httpRequest, completableFuture8, httpResponse, null, null, null, 4, arg_0));
                }
                completableFuture7.join();
            }
            catch (Exception exception) {
                this.logger.error("Error loading shipping [API]: {}", (Object)exception.getMessage());
                CompletableFuture completableFuture = VertxUtil.randomSleep((long)3000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture9 = completableFuture;
                    return ((CompletableFuture)completableFuture9.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$walletsSubmitShipping(this, string, n, null, completableFuture9, null, null, null, exception, 5, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture submitShipping(String string) {
        int n = 1;
        this.logger.info("Submitting shipping...");
        int n2 = 0;
        while (this.running) {
            if (n2++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            CompletableFuture completableFuture = this.shippingForm(string);
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$submitShipping(this, string, n, n2, completableFuture2, null, null, null, null, null, 1, arg_0));
            }
            MultiMap multiMap = (MultiMap)completableFuture.join();
            try {
                HttpRequest httpRequest = this.api.postShippingRate(string);
                CompletableFuture completableFuture3 = Request.send((HttpRequest)httpRequest, (MultiMap)multiMap);
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$submitShipping(this, string, n, n2, completableFuture4, multiMap, httpRequest, null, null, null, 2, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture3.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 302 || httpResponse.statusCode() == 301) {
                    String string2 = httpResponse.getHeader("location");
                    CompletableFuture completableFuture5 = this.handleRedirect(httpResponse, true);
                    if (!completableFuture5.isDone()) {
                        CompletableFuture completableFuture6 = completableFuture5;
                        return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$submitShipping(this, string, n, n2, completableFuture6, multiMap, httpRequest, httpResponse, string2, null, 3, arg_0));
                    }
                    Boolean bl = (Boolean)completableFuture5.join();
                    if (bl == null) {
                        return CompletableFuture.completedFuture(string2);
                    }
                    if (!bl.booleanValue()) continue;
                    if (string2.contains("previous_step=shipping_method")) {
                        return CompletableFuture.completedFuture(string2);
                    }
                    this.logger.error("Unknown redirect! - " + httpResponse);
                } else if (httpResponse.statusCode() == 200) {
                    this.updateShippingRate(httpResponse.bodyAsString());
                    this.updateCheckoutParams(httpResponse.bodyAsString());
                    this.logger.info("Invalid previous shipping. Updating rates.");
                    if (n != 0) {
                        n = 0;
                        continue;
                    }
                } else {
                    this.logger.warn("Submitting shipping: status: '{}'", (Object)httpResponse.statusCode());
                    if (n2 < 2) continue;
                }
                CompletableFuture completableFuture7 = VertxUtil.sleep((long)this.task.getRetryDelay());
                if (!completableFuture7.isDone()) {
                    CompletableFuture completableFuture8 = completableFuture7;
                    return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$submitShipping(this, string, n, n2, completableFuture8, multiMap, httpRequest, httpResponse, null, null, 4, arg_0));
                }
                completableFuture7.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error submitting shipping: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture9 = super.randomSleep(3000);
                if (!completableFuture9.isDone()) {
                    CompletableFuture completableFuture10 = completableFuture9;
                    return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$submitShipping(this, string, n, n2, completableFuture10, multiMap, null, null, null, throwable, 5, arg_0));
                }
                completableFuture9.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$genAcc(Shopify var0, Account var1_1, CompletableFuture var2_2, MultiMap var3_3, int var4_5, MultiMap var5_9, String var6_10, HttpRequest var7_11, HttpResponse var8_12, Throwable var9_13, int var10_14, Object var11_15) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [10[CATCHBLOCK]], but top level block is 17[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public void setAttributes() {
        this.configureShippingRate();
        this.TEXTAREA_PARAMS.clear();
        this.api.getCookies().removeAnyMatch("cart");
        this.api.getCookies().removeAnyMatch("cart_sig");
        this.api.getCookies().put("_landing_page", "%2Fproducts%2F" + ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE), this.api.getSiteURL());
        this.api.getCookies().put("_orig_referrer", "", this.api.getSiteURL());
        this.api.getCookies().put("_shopify_sa_p", "", this.api.getSiteURL());
        this.api.getCookies().put("_shopify_country", "United+States", this.api.getSiteURL());
        this.api.getCookies().put("_shopify_sa_t", Utils.encodedDateISO((Instant)Instant.now()), this.api.getSiteURL());
        this.api.getCookies().put("_shopify_fs", Utils.encodedDateISO((Instant)instanceTime), this.api.getSiteURL());
        this.api.getCookies().put("GlobalE_Welcome_Data", "%7B%22showWelcome%22%3Afalse%7D", "kith.com");
        this.api.getCookies().put("GlobalE_CT_Data", "%7B%22CUID%22%3A%22561354664.541010022.583%22%2C%22CHKCUID%22%3Anull%7D", "kith.com");
        this.api.getCookies().put("GlobalE_SupportThirdPartCookies", "true", "kith.com");
        this.api.getCookies().put("GlobalE_Full_Redirect", "false", "kith.com");
        this.api.getCookies().put("GlobalE_Data", "%7B%22countryISO%22%3A%22US%22%2C%22currencyCode%22%3A%22USD%22%2C%22cultureCode%22%3A%22en-US%22%7D", "kith.com");
        this.api.getCookies().put("dynamic_checkout_shown_on_cart", "1", "kith.com");
        this.api.getCookies().put("acceptedCookies", "yes", "kith.com");
        this.size = null;
        this.authenticity = "";
        this.price = null;
        this.paymentToken = new CompletableFuture();
        this.prevTime = -1L;
        this.noProcessOnFirstTry = true;
        this.counter429 = 0;
        this.hasShippingAlreadySubmittedAPI = false;
        this.isShippingSubmittedWithWallets = false;
        this.api.isOOS = false;
        this.api.isFirstProcessingPageVisit = true;
        this.api.key = null;
        this.api.graphUnstable = false;
    }

    public CompletableFuture submitContact(String string) {
        this.logger.info("Submitting contact info...");
        int n = 0;
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            MultiMap multiMap = this.contactForm(string);
            try {
                HttpRequest httpRequest = this.api.postContact(string);
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest, (MultiMap)multiMap);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$submitContact(this, string, n, multiMap, httpRequest, completableFuture2, null, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse != null) {
                    if (httpResponse.statusCode() == 302 || httpResponse.statusCode() == 301) {
                        String string2 = httpResponse.getHeader("location");
                        CompletableFuture completableFuture3 = this.handleRedirect(httpResponse, true);
                        if (!completableFuture3.isDone()) {
                            CompletableFuture completableFuture4 = completableFuture3;
                            return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$submitContact(this, string, n, multiMap, httpRequest, completableFuture4, httpResponse, string2, null, 2, arg_0));
                        }
                        Boolean bl = (Boolean)completableFuture3.join();
                        if (bl == null) {
                            return CompletableFuture.completedFuture(string2);
                        }
                        if (!bl.booleanValue() && string2.contains("stock_problem")) {
                            return CompletableFuture.completedFuture(string2);
                        }
                        if (!bl.booleanValue()) continue;
                        if (string2.contains("previous_step=contact_information")) {
                            return CompletableFuture.completedFuture(string2);
                        }
                        this.logger.error("Unknown redirect! - " + httpResponse);
                    } else if (httpResponse.statusCode() != 302) {
                        if (httpResponse.statusCode() == 200) {
                            this.logger.error("Submitting contact info: err: '{}'", (Object)Utils.quickParseFirst((String)httpResponse.bodyAsString(), (Pattern[])new Pattern[]{FIELD_ERR_PATTERN}));
                        } else {
                            this.logger.warn("Submitting contact info: status: '{}'", (Object)httpResponse.statusCode());
                        }
                        if (n < 2) continue;
                    }
                }
                CompletableFuture completableFuture5 = VertxUtil.sleep((long)this.task.getRetryDelay());
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$submitContact(this, string, n, multiMap, httpRequest, completableFuture6, httpResponse, null, null, 3, arg_0));
                }
                completableFuture5.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error submitting contact info: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = super.randomSleep(3000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$submitContact(this, string, n, multiMap, null, completableFuture7, null, null, throwable, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public MultiMap checkCaptchaForm() {
        MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
        multiMap.add("_method", "patch");
        multiMap.add("checkout[email]", "53");
        return multiMap;
    }

    public CompletableFuture clearCart() {
        int n = 0;
        this.logger.info("Preparing preload...");
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            try {
                HttpRequest httpRequest = this.api.clearCart();
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$clearCart(this, n, httpRequest, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 200) {
                    VertxUtil.sendSignal((String)this.api.getSiteURL());
                    return CompletableFuture.completedFuture(null);
                }
                if (httpResponse.statusCode() == 302 || httpResponse.statusCode() == 301) {
                    CompletableFuture completableFuture3 = this.handleRedirect(httpResponse, false);
                    if (!completableFuture3.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture3;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$clearCart(this, n, httpRequest, completableFuture4, httpResponse, null, 2, arg_0));
                    }
                    completableFuture3.join();
                }
                this.logger.warn("Failed preparing preload: status: '{}'", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture5 = VertxUtil.sleep((long)this.task.getRetryDelay());
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$clearCart(this, n, httpRequest, completableFuture6, httpResponse, null, 3, arg_0));
                }
                completableFuture5.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Failed preparing preload: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = super.randomSleep(3000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$clearCart(this, n, null, completableFuture7, null, throwable, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture fetchMeta() {
        int n = 0;
        this.logger.info("Getting details [BACKUP]");
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            try {
                HttpRequest httpRequest = this.api.meta();
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$fetchMeta(this, n, httpRequest, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 200) {
                    this.api.checkpointClient = RealClientFactory.createChild((Vertx)this.vertx, (RealClient)this.api.getWebClient());
                    return CompletableFuture.completedFuture(httpResponse.bodyAsJsonObject());
                }
                if (httpResponse.statusCode() == 302 || httpResponse.statusCode() == 301) {
                    CompletableFuture completableFuture3 = this.handleRedirect(httpResponse, false);
                    if (!completableFuture3.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture3;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$fetchMeta(this, n, httpRequest, completableFuture4, httpResponse, null, 2, arg_0));
                    }
                    if (!((Boolean)completableFuture3.join()).booleanValue()) continue;
                }
                if (httpResponse.bodyAsString().contains("You do not have permission to access this website")) {
                    this.logger.warn("Proxy is permanently banned from the site.");
                    this.api.rotateProxy();
                } else {
                    this.logger.warn("Getting details [BACKUP]: status: '{}'", (Object)httpResponse.statusCode());
                }
                CompletableFuture completableFuture5 = VertxUtil.sleep((long)this.task.getRetryDelay());
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$fetchMeta(this, n, httpRequest, completableFuture6, httpResponse, null, 3, arg_0));
                }
                completableFuture5.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Getting details [BACKUP]: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = super.randomSleep(3000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$fetchMeta(this, n, null, completableFuture7, null, throwable, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture shippingAndBillingPostAlreadyCarted() {
        this.logger.info("Generating checkout [API]...");
        int n = 0;
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            try {
                HttpRequest httpRequest = this.api.atcWallets();
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest, (JsonObject)this.contactAPIAndCartForm(true));
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$shippingAndBillingPostAlreadyCarted(this, n, httpRequest, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 200) return CompletableFuture.completedFuture(httpResponse.bodyAsJsonObject().getJsonObject("checkout").getString("web_url"));
                if (httpResponse.statusCode() == 201) {
                    return CompletableFuture.completedFuture(httpResponse.bodyAsJsonObject().getJsonObject("checkout").getString("web_url"));
                }
                if (httpResponse.statusCode() == 202) {
                    if (!httpResponse.bodyAsString().contains(this.task.getProfile().getAddress1())) continue;
                    return CompletableFuture.completedFuture(httpResponse.bodyAsJsonObject().getJsonObject("checkout").getString("web_url"));
                }
                if (httpResponse.statusCode() == 409 && n < 5) continue;
                if (httpResponse.statusCode() == 429 && this.api.getCookies().contains("_checkout_queue_token")) {
                    CompletableFuture completableFuture3 = this.handleQueueNew();
                    if (!completableFuture3.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture3;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$shippingAndBillingPostAlreadyCarted(this, n, httpRequest, completableFuture4, httpResponse, null, 2, arg_0));
                    }
                    completableFuture3.join();
                    continue;
                }
                if (httpResponse.statusCode() == 422) {
                    this.logger.warn("Retrying generating checkout [API]: status: '{}'", (Object)httpResponse.statusCode());
                    CompletableFuture completableFuture5 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
                    if (!completableFuture5.isDone()) {
                        CompletableFuture completableFuture6 = completableFuture5;
                        return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$shippingAndBillingPostAlreadyCarted(this, n, httpRequest, completableFuture6, httpResponse, null, 3, arg_0));
                    }
                    completableFuture5.join();
                    continue;
                }
                this.logger.warn("Retrying generating checkout [API]: status: '{}'", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture7 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
                if (!completableFuture7.isDone()) {
                    CompletableFuture completableFuture8 = completableFuture7;
                    return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$shippingAndBillingPostAlreadyCarted(this, n, httpRequest, completableFuture8, httpResponse, null, 4, arg_0));
                }
                completableFuture7.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error generating checkout [API]: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = super.randomSleep(3000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture9 = completableFuture;
                    return ((CompletableFuture)completableFuture9.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$shippingAndBillingPostAlreadyCarted(this, n, null, completableFuture9, null, throwable, 5, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture getPaymentStatus(String string) {
        this.logger.info("Checking order...");
        int n = 0;
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            try {
                HttpRequest httpRequest = this.api.getProcessingRedirect(string);
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$getPaymentStatus(this, string, n, httpRequest, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 200) {
                    String string2 = httpResponse.bodyAsString();
                    this.updateGateway(string2);
                    this.updateCheckoutParams(string2);
                    this.updatePrice(string2);
                    return CompletableFuture.completedFuture(string2);
                }
                if (httpResponse.statusCode() == 302 || httpResponse.statusCode() == 301) {
                    CompletableFuture completableFuture3 = this.handleRedirect(httpResponse, false);
                    if (!completableFuture3.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture3;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$getPaymentStatus(this, string, n, httpRequest, completableFuture4, httpResponse, null, 2, arg_0));
                    }
                    completableFuture3.join();
                }
                if ((httpResponse.statusCode() == 302 || httpResponse.statusCode() == 301) && httpResponse.getHeader("location").contains("thank_you")) {
                    string = httpResponse.getHeader("location");
                }
                this.logger.warn("Waiting for order status: status: '{}'", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture5 = VertxUtil.sleep((long)this.task.getRetryDelay());
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$getPaymentStatus(this, string, n, httpRequest, completableFuture6, httpResponse, null, 3, arg_0));
                }
                completableFuture5.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error checking payment status: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = super.randomSleep(3000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$getPaymentStatus(this, string, n, null, completableFuture7, null, throwable, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public JsonObject contactAPIAndCartForm(boolean bl) {
        JsonObject jsonObject = new JsonObject();
        JsonObject jsonObject2 = new JsonObject().put("is_upstream_button", (Object)false).put("page_type", (Object)"product").put("presentment_currency", (Object)this.api.getCookies().getCookieValue("cart_currency")).put("secret", (Object)true).put("wallet_name", (Object)"Checkout");
        if (bl) {
            jsonObject2.put("cart_token", (Object)this.api.getCookies().getCookieValue("cart"));
        } else {
            JsonObject jsonObject3 = new JsonObject().put("variant_id", (Object)Long.parseLong(this.instanceSignal)).put("quantity", (Object)1);
            if (this.propertiesPair != null) {
                jsonObject3.put("properties", (Object)new JsonObject().put((String)this.propertiesPair.first, this.propertiesPair.second));
            }
            jsonObject2.put("line_items", (Object)new JsonArray().add((Object)jsonObject3));
        }
        jsonObject.put("checkout", (Object)jsonObject2);
        return jsonObject;
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$fetchMeta(Shopify var0, int var1_1, HttpRequest var2_2, CompletableFuture var3_4, HttpResponse var4_5, Throwable var5_6, int var6_7, Object var7_8) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [8[CATCHBLOCK]], but top level block is 13[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$checkNewQueueThrottle(Shopify var0, int var1_1, HttpRequest var2_2, CompletableFuture var3_4, HttpResponse var4_5, Throwable var5_6, int var6_7, Object var7_8) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [7[CATCHBLOCK]], but top level block is 11[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$login(Shopify var0, AccountController var1_1, CompletableFuture var2_2, Account var3_3, MultiMap var4_5, int var5_7, MultiMap var6_11, String var7_12, HttpRequest var8_13, HttpResponse var9_14, Throwable var10_15, int var11_16, Object var12_17) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [12[CATCHBLOCK]], but top level block is 21[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$attemptGenCheckoutUrl(Shopify var0, int var1_1, int var2_2, HttpRequest var3_3, CompletableFuture var4_5, HttpResponse var5_6, String var6_7, REDIRECT_STATUS var7_8, Throwable var8_9, int var9_10, Object var10_11) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [8[CATCHBLOCK]], but top level block is 12[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public CompletableFuture safeCompleteRequest(HttpRequest httpRequest, String string) {
        int n = 0;
        while (n++ < Integer.MAX_VALUE) {
            if (!this.api.getWebClient().isActive()) return CompletableFuture.completedFuture(null);
            try {
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$safeCompleteRequest(this, httpRequest, string, n, completableFuture2, null, 0, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                int n2 = httpResponse.statusCode();
                if (n2 == 200) return CompletableFuture.completedFuture(httpResponse);
                if (httpResponse.statusCode() == 302) return CompletableFuture.completedFuture(httpResponse);
                if (httpResponse.statusCode() == 301) {
                    return CompletableFuture.completedFuture(httpResponse);
                }
                if (n2 == 202 || n <= 3) continue;
                this.logger.error("Failed " + string + ": status: {}", (Object)n2);
                CompletableFuture completableFuture3 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$safeCompleteRequest(this, httpRequest, string, n, completableFuture4, httpResponse, n2, null, 2, arg_0));
                }
                completableFuture3.join();
            }
            catch (Throwable throwable) {
                if (!throwable.getMessage().contains("Connection was closed")) {
                    this.logger.error("Failed " + string + ": status: {}", (Object)throwable.getMessage());
                }
                CompletableFuture completableFuture = VertxUtil.randomSleep((long)this.task.getRetryDelay());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture5 = completableFuture;
                    return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$safeCompleteRequest(this, httpRequest, string, n, completableFuture5, null, 0, throwable, 3, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture checkQueueThrottle() {
        this.logger.info("Checking queue...");
        int n = 0;
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            try {
                HttpRequest httpRequest = this.api.fetchQueueUrl();
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$checkQueueThrottle(this, n, httpRequest, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 200) {
                    return CompletableFuture.completedFuture(true);
                }
                this.logger.warn("Waiting to check queue: status: '{}'", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture3 = VertxUtil.randomSleep((long)3000L);
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$checkQueueThrottle(this, n, httpRequest, completableFuture4, httpResponse, null, 2, arg_0));
                }
                completableFuture3.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error checking queue: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = super.randomSleep(3000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture5 = completableFuture;
                    return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$checkQueueThrottle(this, n, null, completableFuture5, null, throwable, 3, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$getShippingPage(Shopify var0, String var1_1, int var2_2, HttpRequest var3_3, CompletableFuture var4_5, HttpResponse var5_6, Boolean var6_7, Throwable var7_8, int var8_9, Object var9_10) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [8[CATCHBLOCK]], but top level block is 13[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public CompletableFuture processPaymentFakeToken(String string) {
        this.logger.info("Adjusting for restock...");
        int n = 0;
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            try {
                CompletableFuture completableFuture = this.fakeProcessingForm(string);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$processPaymentFakeToken(this, string, n, completableFuture2, null, null, null, null, 1, arg_0));
                }
                MultiMap multiMap = (MultiMap)completableFuture.join();
                HttpRequest httpRequest = this.api.postPayment(string);
                CompletableFuture completableFuture3 = Request.send((HttpRequest)httpRequest, (MultiMap)multiMap);
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$processPaymentFakeToken(this, string, n, completableFuture4, multiMap, httpRequest, null, null, 2, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture3.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 200) {
                    return CompletableFuture.completedFuture(null);
                }
                if (httpResponse.statusCode() == 302 || httpResponse.statusCode() == 301) {
                    CompletableFuture completableFuture5 = this.handleRedirect(httpResponse, false);
                    if (!completableFuture5.isDone()) {
                        CompletableFuture completableFuture6 = completableFuture5;
                        return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$processPaymentFakeToken(this, string, n, completableFuture6, multiMap, httpRequest, httpResponse, null, 3, arg_0));
                    }
                    completableFuture5.join();
                } else {
                    this.logger.warn("Error adjusting for restock: status: '{}'", (Object)httpResponse.statusCode());
                }
                CompletableFuture completableFuture7 = VertxUtil.sleep((long)this.task.getRetryDelay());
                if (!completableFuture7.isDone()) {
                    CompletableFuture completableFuture8 = completableFuture7;
                    return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$processPaymentFakeToken(this, string, n, completableFuture8, multiMap, httpRequest, httpResponse, null, 4, arg_0));
                }
                completableFuture7.join();
            }
            catch (Exception exception) {
                if (exception.getMessage().equals("io.vertx.core.VertxException: Connection was closed")) continue;
                this.logger.error("Error adjusting for restock: {}", (Object)exception.getMessage());
                CompletableFuture completableFuture = super.randomSleep(3000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture9 = completableFuture;
                    return ((CompletableFuture)completableFuture9.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$processPaymentFakeToken(this, string, n, completableFuture9, null, null, null, exception, 5, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture walletsGenCheckout() {
        this.logger.info("Generating checkout [API-CART]...");
        int n = 0;
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            try {
                HttpRequest httpRequest = this.api.atcWallets();
                CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest, (JsonObject)this.walletsGenCheckoutUrlForm());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$walletsGenCheckout(this, n, httpRequest, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 200) return CompletableFuture.completedFuture(httpResponse.bodyAsJsonObject().getJsonObject("checkout").getString("web_url"));
                if (httpResponse.statusCode() == 201) {
                    return CompletableFuture.completedFuture(httpResponse.bodyAsJsonObject().getJsonObject("checkout").getString("web_url"));
                }
                if (httpResponse.statusCode() == 202) {
                    if (!httpResponse.bodyAsString().contains(this.task.getProfile().getAddress1())) continue;
                    return CompletableFuture.completedFuture(httpResponse.bodyAsJsonObject().getJsonObject("checkout").getString("web_url"));
                }
                if (httpResponse.statusCode() == 409 && n < 5) continue;
                if (httpResponse.statusCode() == 429 && this.api.getCookies().contains("_checkout_queue_token")) {
                    CompletableFuture completableFuture3 = this.handleQueueNew();
                    if (!completableFuture3.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture3;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$walletsGenCheckout(this, n, httpRequest, completableFuture4, httpResponse, null, 2, arg_0));
                    }
                    completableFuture3.join();
                    continue;
                }
                this.logger.warn("Retrying generating checkout [API-CART]: status: '{}'", httpResponse.statusCode() == 422 ? "OOS" : Integer.valueOf(httpResponse.statusCode()));
                CompletableFuture completableFuture5 = VertxUtil.sleep((long)this.task.getRetryDelay());
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$walletsGenCheckout(this, n, httpRequest, completableFuture6, httpResponse, null, 3, arg_0));
                }
                completableFuture5.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error generating checkout [API-CART]: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = super.randomSleep(3000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$walletsGenCheckout(this, n, null, completableFuture7, null, throwable, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$walletsGenCheckout(Shopify var0, int var1_1, HttpRequest var2_2, CompletableFuture var3_4, HttpResponse var4_5, Throwable var5_6, int var6_7, Object var7_8) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [8[CATCHBLOCK]], but top level block is 13[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$handleRedirect(Shopify var0, String var1_1, int var2_2, REDIRECT_STATUS var3_3, CompletableFuture var4_4, int var5_7, String var6_10, CaptchaToken var7_11, int var8_12, Object var9_13) {
        switch (var8_12) {
            case 0: {
                var3_3 = REDIRECT_STATUS.checkRedirectStatus((String)var1_1);
                switch (1.$SwitchMap$io$trickle$task$sites$shopify$util$REDIRECT_STATUS[var3_3.ordinal()]) {
                    case 1: {
                        if (!var1_1.contains("key=")) {
                            var0.api.key = null;
                        }
                        var0.api.setOOS();
                        if (var2_2 != 0) return CompletableFuture.completedFuture(null);
                        v0 = var0.handleOOS(var1_1);
                        if (!v0.isDone()) {
                            var7_11 = v0;
                            return var7_11.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$handleRedirect(io.trickle.task.sites.shopify.Shopify java.lang.String int io.trickle.task.sites.shopify.util.REDIRECT_STATUS java.util.concurrent.CompletableFuture int java.lang.String io.trickle.harvester.CaptchaToken int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (String)var1_1, (int)var2_2, (REDIRECT_STATUS)var3_3, (CompletableFuture)var7_11, (int)0, null, null, (int)1));
                        }
                        ** GOTO lbl100
                    }
                    case 2: {
                        if (var0.task.getPassword() == null) ** GOTO lbl22
                        v1 = var0.submitPassword();
                        if (!v1.isDone()) {
                            var7_11 = v1;
                            return var7_11.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$handleRedirect(io.trickle.task.sites.shopify.Shopify java.lang.String int io.trickle.task.sites.shopify.util.REDIRECT_STATUS java.util.concurrent.CompletableFuture int java.lang.String io.trickle.harvester.CaptchaToken int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (String)var1_1, (int)var2_2, (REDIRECT_STATUS)var3_3, (CompletableFuture)var7_11, (int)0, null, null, (int)2));
                        }
                        ** GOTO lbl105
lbl22:
                        // 1 sources

                        var0.logger.info("Password page detected. Waiting for drop.");
                        v2 = VertxUtil.randomSignalSleep((String)var0.api.getSiteURL(), (long)var0.task.getMonitorDelay());
                        if (!v2.isDone()) {
                            var7_11 = v2;
                            return var7_11.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$handleRedirect(io.trickle.task.sites.shopify.Shopify java.lang.String int io.trickle.task.sites.shopify.util.REDIRECT_STATUS java.util.concurrent.CompletableFuture int java.lang.String io.trickle.harvester.CaptchaToken int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (String)var1_1, (int)var2_2, (REDIRECT_STATUS)var3_3, (CompletableFuture)var7_11, (int)0, null, null, (int)3));
                        }
                        ** GOTO lbl111
                    }
                    case 3: {
                        var0.logger.info("Handling 'old' queue...");
                        v3 = var0.checkQueueThrottle();
                        if (!v3.isDone()) {
                            var7_11 = v3;
                            return var7_11.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$handleRedirect(io.trickle.task.sites.shopify.Shopify java.lang.String int io.trickle.task.sites.shopify.util.REDIRECT_STATUS java.util.concurrent.CompletableFuture int java.lang.String io.trickle.harvester.CaptchaToken int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (String)var1_1, (int)var2_2, (REDIRECT_STATUS)var3_3, (CompletableFuture)var7_11, (int)0, null, null, (int)4));
                        }
                        ** GOTO lbl116
                    }
                    case 4: {
                        v4 = var0.checkNewQueueThrottle();
                        if (!v4.isDone()) {
                            var7_11 = v4;
                            return var7_11.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$handleRedirect(io.trickle.task.sites.shopify.Shopify java.lang.String int io.trickle.task.sites.shopify.util.REDIRECT_STATUS java.util.concurrent.CompletableFuture int java.lang.String io.trickle.harvester.CaptchaToken int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (String)var1_1, (int)var2_2, (REDIRECT_STATUS)var3_3, (CompletableFuture)var7_11, (int)0, null, null, (int)6));
                        }
                        ** GOTO lbl130
                    }
                    case 5: {
                        if (!var0.task.getMode().contains("human") && !var0.task.getMode().contains("hybrid")) ** GOTO lbl57
                        if (!var0.isCPMonitorTriggered.compareAndSet(true, false)) ** GOTO lbl50
                        var0.logger.info("Checkpoint detected. Waiting for presolve");
                        v5 = var0.cpMonitor;
                        if (!v5.isDone()) {
                            var7_11 = v5;
                            return var7_11.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$handleRedirect(io.trickle.task.sites.shopify.Shopify java.lang.String int io.trickle.task.sites.shopify.util.REDIRECT_STATUS java.util.concurrent.CompletableFuture int java.lang.String io.trickle.harvester.CaptchaToken int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (String)var1_1, (int)var2_2, (REDIRECT_STATUS)var3_3, (CompletableFuture)var7_11, (int)0, null, null, (int)8));
                        }
                        ** GOTO lbl144
lbl50:
                        // 1 sources

                        var0.logger.info("Checkpoint detected. Handling...");
                        var0.shutOffPresolver(true);
                        v6 = var0.getCPHtml(false);
                        if (!v6.isDone()) {
                            var7_11 = v6;
                            return var7_11.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$handleRedirect(io.trickle.task.sites.shopify.Shopify java.lang.String int io.trickle.task.sites.shopify.util.REDIRECT_STATUS java.util.concurrent.CompletableFuture int java.lang.String io.trickle.harvester.CaptchaToken int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (String)var1_1, (int)var2_2, (REDIRECT_STATUS)var3_3, (CompletableFuture)var7_11, (int)0, null, null, (int)9));
                        }
                        ** GOTO lbl150
lbl57:
                        // 1 sources

                        var0.logger.error("Checkpoint is enforced but this task is running [{}]. Sleeping...", (Object)var0.task.getMode());
                        v7 = VertxUtil.hardCodedSleep((long)30000L);
                        if (!v7.isDone()) {
                            var7_11 = v7;
                            return var7_11.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$handleRedirect(io.trickle.task.sites.shopify.Shopify java.lang.String int io.trickle.task.sites.shopify.util.REDIRECT_STATUS java.util.concurrent.CompletableFuture int java.lang.String io.trickle.harvester.CaptchaToken int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (String)var1_1, (int)var2_2, (REDIRECT_STATUS)var3_3, (CompletableFuture)var7_11, (int)0, null, null, (int)12));
                        }
                        ** GOTO lbl171
                    }
                    case 6: {
                        var0.logger.info("Login required. Logging in...");
                        v8 = var0.login();
                        if (!v8.isDone()) {
                            var7_11 = v8;
                            return var7_11.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$handleRedirect(io.trickle.task.sites.shopify.Shopify java.lang.String int io.trickle.task.sites.shopify.util.REDIRECT_STATUS java.util.concurrent.CompletableFuture int java.lang.String io.trickle.harvester.CaptchaToken int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (String)var1_1, (int)var2_2, (REDIRECT_STATUS)var3_3, (CompletableFuture)var7_11, (int)0, null, null, (int)13));
                        }
                        ** GOTO lbl176
                    }
                    case 7: {
                        var0.logger.info("Bad account. Wrong password? Generating a fresh account.");
                        v9 = var0.genAcc();
                        if (!v9.isDone()) {
                            var7_11 = v9;
                            return var7_11.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$handleRedirect(io.trickle.task.sites.shopify.Shopify java.lang.String int io.trickle.task.sites.shopify.util.REDIRECT_STATUS java.util.concurrent.CompletableFuture int java.lang.String io.trickle.harvester.CaptchaToken int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (String)var1_1, (int)var2_2, (REDIRECT_STATUS)var3_3, (CompletableFuture)var7_11, (int)0, null, null, (int)14));
                        }
                        ** GOTO lbl181
                    }
                    case 8: {
                        var0.logger.info("Login captcha, using V2.");
                        v10 = var0.loginV2();
                        if (!v10.isDone()) {
                            var7_11 = v10;
                            return var7_11.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$handleRedirect(io.trickle.task.sites.shopify.Shopify java.lang.String int io.trickle.task.sites.shopify.util.REDIRECT_STATUS java.util.concurrent.CompletableFuture int java.lang.String io.trickle.harvester.CaptchaToken int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (String)var1_1, (int)var2_2, (REDIRECT_STATUS)var3_3, (CompletableFuture)var7_11, (int)0, null, null, (int)15));
                        }
                        ** GOTO lbl186
                    }
                    case 9: {
                        return CompletableFuture.completedFuture(true);
                    }
                    case 10: {
                        var0.logger.info("Product is not yet cartable.");
                        v11 = var0.atcAJAX(var0.instanceSignal, false);
                        if (!v11.isDone()) {
                            var7_11 = v11;
                            return var7_11.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$handleRedirect(io.trickle.task.sites.shopify.Shopify java.lang.String int io.trickle.task.sites.shopify.util.REDIRECT_STATUS java.util.concurrent.CompletableFuture int java.lang.String io.trickle.harvester.CaptchaToken int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (String)var1_1, (int)var2_2, (REDIRECT_STATUS)var3_3, (CompletableFuture)var7_11, (int)0, null, null, (int)16));
                        }
                        ** GOTO lbl191
                    }
                    default: {
                        if (var0.logger.isDebugEnabled()) {
                            var0.logger.debug("signal sent");
                        }
                        VertxUtil.sendSignal((String)var0.api.getSiteURL());
                        return CompletableFuture.completedFuture(true);
                    }
                }
            }
            case 1: {
                v0 = var4_4;
lbl100:
                // 2 sources

                v0.join();
                return CompletableFuture.completedFuture(false);
            }
            case 2: {
                v1 = var4_4;
lbl105:
                // 2 sources

                v1.join();
lbl107:
                // 2 sources

                return CompletableFuture.completedFuture(false);
            }
            case 3: {
                v2 = var4_4;
lbl111:
                // 2 sources

                v2.join();
                ** continue;
            }
            case 4: {
                v3 = var4_4;
lbl116:
                // 2 sources

                v3.join();
                v12 = var0.handleQueueOld();
                if (!v12.isDone()) {
                    var7_11 = v12;
                    return var7_11.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$handleRedirect(io.trickle.task.sites.shopify.Shopify java.lang.String int io.trickle.task.sites.shopify.util.REDIRECT_STATUS java.util.concurrent.CompletableFuture int java.lang.String io.trickle.harvester.CaptchaToken int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (String)var1_1, (int)var2_2, (REDIRECT_STATUS)var3_3, (CompletableFuture)var7_11, (int)0, null, null, (int)5));
                }
                ** GOTO lbl125
            }
            case 5: {
                v12 = var4_4;
lbl125:
                // 2 sources

                v12.join();
                return CompletableFuture.completedFuture(false);
            }
            case 6: {
                v4 = var4_4;
lbl130:
                // 2 sources

                if ((var4_5 = ((Boolean)v4.join()).booleanValue()) == 0) return CompletableFuture.completedFuture(false);
                v13 = var0.handleQueueNew();
                if (!v13.isDone()) {
                    var7_11 = v13;
                    return var7_11.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$handleRedirect(io.trickle.task.sites.shopify.Shopify java.lang.String int io.trickle.task.sites.shopify.util.REDIRECT_STATUS java.util.concurrent.CompletableFuture int java.lang.String io.trickle.harvester.CaptchaToken int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (String)var1_1, (int)var2_2, (REDIRECT_STATUS)var3_3, (CompletableFuture)var7_11, (int)var4_5, null, null, (int)7));
                }
                ** GOTO lbl139
            }
            case 7: {
                v13 = var4_4;
                var4_6 = var5_7;
lbl139:
                // 2 sources

                v13.join();
                return CompletableFuture.completedFuture(false);
            }
            case 8: {
                v5 = var4_4;
lbl144:
                // 2 sources

                v5.join();
lbl146:
                // 3 sources

                return CompletableFuture.completedFuture(false);
            }
            case 9: {
                v6 = var4_4;
lbl150:
                // 2 sources

                if (!(v14 = var0.solveCheckpointCaptcha(var5_8 = (String)v6.join())).isDone()) {
                    var7_11 = v14;
                    return var7_11.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$handleRedirect(io.trickle.task.sites.shopify.Shopify java.lang.String int io.trickle.task.sites.shopify.util.REDIRECT_STATUS java.util.concurrent.CompletableFuture int java.lang.String io.trickle.harvester.CaptchaToken int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (String)var1_1, (int)var2_2, (REDIRECT_STATUS)var3_3, (CompletableFuture)var7_11, (int)0, (String)var5_8, null, (int)10));
                }
                ** GOTO lbl157
            }
            case 10: {
                v14 = var4_4;
                var5_8 = var6_10;
lbl157:
                // 2 sources

                if (!(v15 = var0.submitCheckpoint((CaptchaToken)(var6_10 = (CaptchaToken)v14.join()))).isDone()) {
                    var7_11 = v15;
                    return var7_11.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$handleRedirect(io.trickle.task.sites.shopify.Shopify java.lang.String int io.trickle.task.sites.shopify.util.REDIRECT_STATUS java.util.concurrent.CompletableFuture int java.lang.String io.trickle.harvester.CaptchaToken int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (String)var1_1, (int)var2_2, (REDIRECT_STATUS)var3_3, (CompletableFuture)var7_11, (int)0, (String)var5_8, (CaptchaToken)var6_10, (int)11));
                }
                ** GOTO lbl166
            }
            case 11: {
                v15 = var4_4;
                v16 = var6_10;
                var6_10 = var7_11;
                var5_9 = v16;
lbl166:
                // 2 sources

                v15.join();
                ** GOTO lbl146
            }
            case 12: {
                v7 = var4_4;
lbl171:
                // 2 sources

                v7.join();
                ** continue;
            }
            case 13: {
                v8 = var4_4;
lbl176:
                // 2 sources

                v8.join();
                return CompletableFuture.completedFuture(false);
            }
            case 14: {
                v9 = var4_4;
lbl181:
                // 2 sources

                v9.join();
                return CompletableFuture.completedFuture(true);
            }
            case 15: {
                v10 = var4_4;
lbl186:
                // 2 sources

                v10.join();
                return CompletableFuture.completedFuture(true);
            }
            case 16: {
                v11 = var4_4;
lbl191:
                // 2 sources

                v11.join();
                return CompletableFuture.completedFuture(false);
            }
        }
        throw new IllegalArgumentException();
    }

    public static CompletableFuture initHarvesters() {
        if (harvesterCFs.length == 0) {
            return CompletableFuture.completedFuture(null);
        }
        CompletionStage completionStage = Vertx.currentContext().owner().sharedData().getLockWithTimeout("harvesterinitshopify", 1800000L).toCompletionStage();
        if (!completionStage.toCompletableFuture().isDone()) {
            CompletionStage completionStage2 = completionStage;
            return completionStage2.exceptionally(Function.identity()).thenCompose(arg_0 -> Shopify.async$initHarvesters(completionStage2, null, null, 1, arg_0)).toCompletableFuture();
        }
        Lock lock = (Lock)completionStage.toCompletableFuture().join();
        for (int i = 0; i < HARVESTERS.length && !isHarvesterInited.get(); ++i) {
            Shopify.harvesterCFs[i] = HARVESTERS[i].start();
        }
        CompletableFuture<Void> completableFuture = CompletableFuture.allOf(harvesterCFs);
        if (!completableFuture.isDone()) {
            CompletableFuture<Void> completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$initHarvesters(null, lock, completableFuture2, 2, arg_0));
        }
        completableFuture.join();
        isHarvesterInited.set(true);
        lock.release();
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$shippingForm(Shopify var0, String var1_1, MultiMap var2_2, MultiMap var3_3, String var4_4, CompletableFuture var5_5, int var6_6, Object var7_8) {
        switch (var6_6) {
            case 0: {
                var2_2 = MultiMap.caseInsensitiveMultiMap();
                var2_2.add("_method", "patch");
                var2_2.add("authenticity_token", var0.authenticity);
                var2_2.add("previous_step", "shipping_method");
                var2_2.add("step", "payment_method");
                v0 = var2_2;
                v1 = "checkout[shipping_rate][id]";
                v2 = var0.shippingRate;
                if (!v2.isDone()) {
                    var7_8 = v2;
                    var6_7 = v1;
                    var5_5 = v0;
                    return var7_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$shippingForm(io.trickle.task.sites.shopify.Shopify java.lang.String io.vertx.core.MultiMap io.vertx.core.MultiMap java.lang.String java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Shopify)var0, (String)var1_1, (MultiMap)var2_2, (MultiMap)var5_5, (String)var6_7, (CompletableFuture)var7_8, (int)1));
                }
                ** GOTO lbl25
            }
            case 1: {
                v0 = var3_3;
                v1 = var4_4;
                v2 = var5_5;
lbl25:
                // 2 sources

                v0.add(v1, ((ShippingRateSupplier)v2.join()).get());
                for (String var4_4 : var0.TEXTAREA_PARAMS) {
                    var2_2.add(var4_4, "");
                }
                if (!var0.TEXTAREA_PARAMS.isEmpty()) {
                    var2_2.add(var1_1 + "-count", "" + var0.TEXTAREA_PARAMS.size());
                    var2_2.add(var1_1 + "-count", "fs_count");
                }
                var2_2.add("checkout[client_details][browser_width]", var0.BROWSER_WIDTH);
                var2_2.add("checkout[client_details][browser_height]", var0.BROWSER_HEIGHT);
                var2_2.add("checkout[client_details][javascript_enabled]", "1");
                var2_2.add("checkout[client_details][color_depth]", "24");
                var2_2.add("checkout[client_details][java_enabled]", "false");
                var2_2.add("checkout[client_details][browser_tz]", "" + Shopify.TIMEZONE_OFFSET);
                return CompletableFuture.completedFuture(var2_2);
            }
        }
        throw new IllegalArgumentException();
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$aiAPIReq(Shopify var0, HttpRequest var1_1, JsonObject var2_2, int var3_3, CompletableFuture var4_4, int var5_6, Object var6_9) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [2[CASE]], but top level block is 7[DOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public CompletableFuture genCheckoutURL(boolean bl) {
        int n = 0;
        this.logger.info("Generating checkout URL...");
        while (this.running) {
            if (n++ >= Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            try {
                HttpResponse httpResponse;
                HttpResponse httpResponse2;
                HttpRequest httpRequest = this.api.emptyCheckout(bl);
                if (bl) {
                    CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest, (Buffer)Buffer.buffer((String)"{}"));
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture2 = completableFuture;
                        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$genCheckoutURL(this, (int)(bl ? 1 : 0), n, httpRequest, completableFuture2, null, null, 1, arg_0));
                    }
                    httpResponse2 = (HttpResponse)completableFuture.join();
                } else {
                    CompletableFuture completableFuture = Request.send((HttpRequest)httpRequest);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture3 = completableFuture;
                        return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$genCheckoutURL(this, (int)(bl ? 1 : 0), n, httpRequest, completableFuture3, null, null, 2, arg_0));
                    }
                    httpResponse2 = (HttpResponse)completableFuture.join();
                }
                if ((httpResponse = httpResponse2) == null) continue;
                if (httpResponse.statusCode() == 302 || httpResponse.statusCode() == 301) {
                    CompletableFuture completableFuture = this.handleRedirect(httpResponse, true);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$genCheckoutURL(this, (int)(bl ? 1 : 0), n, httpRequest, completableFuture4, httpResponse, null, 3, arg_0));
                    }
                    if (((Boolean)completableFuture.join()).booleanValue()) {
                        return CompletableFuture.completedFuture(httpResponse.getHeader("location"));
                    }
                }
                if (httpResponse.statusCode() == 302) continue;
                this.logger.warn("Failed generating checkout URL: status: '{}'", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture = VertxUtil.sleep((long)this.task.getRetryDelay());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture5 = completableFuture;
                    return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$genCheckoutURL(this, (int)(bl ? 1 : 0), n, httpRequest, completableFuture5, httpResponse, null, 4, arg_0));
                }
                completableFuture.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Failed generating checkout URL: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = super.randomSleep(3000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Shopify.async$genCheckoutURL(this, (int)(bl ? 1 : 0), n, null, completableFuture6, null, throwable, 5, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }
}
