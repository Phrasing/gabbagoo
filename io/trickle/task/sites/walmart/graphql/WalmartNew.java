/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.netty.handler.codec.http.cookie.ClientCookieDecoder
 *  io.netty.handler.codec.http.cookie.Cookie
 *  io.vertx.core.buffer.Buffer
 *  io.vertx.core.http.HttpMethod
 *  io.vertx.core.impl.ConcurrentHashSet
 *  io.vertx.core.json.JsonArray
 *  io.vertx.core.json.JsonObject
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.client.HttpResponse
 *  io.vertx.ext.web.client.impl.HttpRequestImpl
 *  io.vertx.ext.web.codec.BodyCodec
 */
package io.trickle.task.sites.walmart.graphql;

import io.netty.handler.codec.http.cookie.ClientCookieDecoder;
import io.netty.handler.codec.http.cookie.Cookie;
import io.trickle.account.Account;
import io.trickle.account.AccountController;
import io.trickle.core.Controller;
import io.trickle.core.Engine;
import io.trickle.core.VertxSingleton;
import io.trickle.core.actor.TaskActor;
import io.trickle.task.Task;
import io.trickle.task.antibot.impl.px.PerimeterX;
import io.trickle.task.antibot.impl.px.payload.captcha.DesktopPXAPI3;
import io.trickle.task.sites.Site;
import io.trickle.task.sites.walmart.Mode;
import io.trickle.task.sites.walmart.graphql.API;
import io.trickle.task.sites.walmart.graphql.Util;
import io.trickle.task.sites.walmart.graphql.WalmartNewAPI;
import io.trickle.task.sites.walmart.graphql.WalmartNewAPIMobile;
import io.trickle.task.sites.walmart.util.PaymentToken;
import io.trickle.util.Utils;
import io.trickle.util.analytics.Analytics;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.trickle.webclient.CookieJar;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.impl.ConcurrentHashSet;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.impl.HttpRequestImpl;
import io.vertx.ext.web.codec.BodyCodec;
import java.lang.invoke.LambdaMetafactory;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class WalmartNew
extends TaskActor {
    public static Pattern PRODUCTS_HOME_PATTERN;
    public long keepAliveWorker;
    public static Pattern STORE_ID_PATTERN;
    public static Pattern APPL_VER_PATTERN;
    public static Pattern PRICE_PATTERN;
    public API api;
    public static ConcurrentHashSet<Cookie> GLOBAL_COOKIES;
    public int desiredUnitPrice = -1;
    public static Pattern TICKET_PATTERN;
    public Mode mode;
    public String instanceSignal;
    public static Pattern REFRESH_TIME_PATTERN;
    public static Pattern ID_PATTERN;
    public String lastReason = "";
    public boolean buyNow;
    public static Pattern TENDER_PATTERN;
    public static Pattern CART_ID_PATTERN;
    public String lineItemId;
    public static Pattern KEYWORD_PID_PATTERN;
    public static Pattern PAYMENT_PREFERENCE_PATTERN;
    public static Pattern ADDRESS_ID_PATTERN;
    public CookieJar sessionCookies;
    public static Predicate<String> DESKTOP_CHECKOUT_FLAG;
    public static Pattern PAYMENT_ID_PATTERN;
    public static Pattern ADDRESS_PATTERN;
    public static Pattern EXPECTED_TIME_PATTERN;
    public boolean queue;
    public static Predicate<String> MOBILE_CHECKOUT_FLAG;
    public long sessionTimestamp = 0L;
    public Task task;
    public PaymentToken token;

    public String lambda$run$4(HttpResponse httpResponse) {
        if (!this.api.cookieStore().contains("xptwg")) return null;
        return httpResponse.bodyAsString();
    }

    public CompletableFuture loginAccount() {
        int n = 0;
        AccountController accountController = (AccountController)Engine.get().getModule(Controller.ACCOUNT);
        Account account = null;
        while (this.running && n <= 500) {
            ++n;
            try {
                if (account == null) {
                    CompletableFuture completableFuture = accountController.findAccount(this.task.getProfile().getEmail(), false).toCompletionStage().toCompletableFuture();
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture2 = completableFuture;
                        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$loginAccount(this, n, accountController, account, completableFuture2, null, null, null, 0, null, 1, arg_0));
                    }
                    account = (Account)completableFuture.join();
                }
                if (account == null) {
                    this.logger.warn("No accounts available...");
                    return CompletableFuture.completedFuture(0);
                }
                this.logger.info("Logging in to account '{}'", (Object)account.getUser());
                account.setSite(Site.WALMART);
                CompletableFuture completableFuture = this.sessionLogon(account);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture3 = completableFuture;
                    return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$loginAccount(this, n, accountController, account, completableFuture3, null, null, null, 0, null, 2, arg_0));
                }
                if (((Boolean)completableFuture.join()).booleanValue()) {
                    this.logger.info("Logged in successfully to account[-] '{}'", (Object)account.getUser());
                    this.task.getProfile().setAccountEmail(account.getUser());
                    this.task.getProfile().setAccountPassword(account.getPass());
                    this.api.setLoggedIn(true);
                    return CompletableFuture.completedFuture(1);
                }
                JsonObject jsonObject = this.api.accountLoginForm(account);
                HttpRequest httpRequest = this.api.loginAccount();
                CompletableFuture completableFuture4 = Request.send(httpRequest, jsonObject);
                if (!completableFuture4.isDone()) {
                    CompletableFuture completableFuture5 = completableFuture4;
                    return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$loginAccount(this, n, accountController, account, completableFuture5, jsonObject, httpRequest, null, 0, null, 3, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture4.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 201 || httpResponse.statusCode() == 200 || httpResponse.statusCode() == 206) {
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug("Account login responded with: [{}]{}", (Object)httpResponse.statusCode(), (Object)httpResponse.bodyAsString());
                    }
                    this.logger.info("Logged in successfully to account '{}'", (Object)account.getUser());
                    this.task.getProfile().setAccountEmail(account.getUser());
                    this.task.getProfile().setAccountPassword(account.getPass());
                    this.api.setLoggedIn(true);
                    if (!httpResponse.cookies().isEmpty()) {
                        JsonArray jsonArray = new JsonArray();
                        for (String string : httpResponse.cookies()) {
                            jsonArray.add((Object)string);
                        }
                        account.setSessionString(jsonArray.encode());
                    }
                    this.vertx.eventBus().send("accounts.writer.session", (Object)account);
                    return CompletableFuture.completedFuture(1);
                }
                this.logger.warn("Account login: status:'{}'", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture6 = this.api.handleBadResponse(httpResponse, Request.getURI(httpRequest));
                if (!completableFuture6.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture6;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$loginAccount(this, n, accountController, account, completableFuture7, jsonObject, httpRequest, httpResponse, 0, null, 4, arg_0));
                }
                int n2 = ((Boolean)completableFuture6.join()).booleanValue() ? 1 : 0;
                if (n2 != 0) continue;
                CompletableFuture completableFuture8 = VertxUtil.sleep(this.task.getRetryDelay());
                if (!completableFuture8.isDone()) {
                    CompletableFuture completableFuture9 = completableFuture8;
                    return ((CompletableFuture)completableFuture9.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$loginAccount(this, n, accountController, account, completableFuture9, jsonObject, httpRequest, httpResponse, n2, null, 5, arg_0));
                }
                completableFuture8.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error occurred logging in account: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = super.randomSleep(12000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture10 = completableFuture;
                    return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$loginAccount(this, n, accountController, account, completableFuture10, null, null, null, 0, throwable, 6, arg_0));
                }
                completableFuture.join();
            }
        }
        this.logger.warn("Failed to login to account. Max retries exceeded...");
        return CompletableFuture.completedFuture(-1);
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$createAccount(WalmartNew var0, int var1_1, int var2_2, JsonObject var3_3, HttpRequest var4_5, CompletableFuture var5_6, HttpResponse var6_7, int var7_9, Throwable var8_11, int var9_12, Object var10_14) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [8[CATCHBLOCK]], but top level block is 14[UNCONDITIONALDOLOOP]
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

    public HttpRequest lambda$queue$14(String string) {
        return this.api.issueTicket(string);
    }

    public void handleFailureWebhooks(String string, JsonObject jsonObject) {
        if (string == null) return;
        if (this.lastReason.equalsIgnoreCase(string)) return;
        try {
            Analytics.failure(string, this.task, jsonObject, this.api.proxyString());
            this.lastReason = string;
            return;
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public CompletableFuture submitBilling() {
        try {
            boolean bl;
            CompletableFuture completableFuture = this.execute("Submitting billing", WalmartNew.quickFunctionParse(PAYMENT_ID_PATTERN, 200), this.api::submitBilling, this.api.getBillingForm(this.token));
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                API aPI = this.api;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$submitBilling(this, aPI, completableFuture2, 1, arg_0));
            }
            this.api.paymentId = (String)completableFuture.join();
            if (!this.api.paymentId.isBlank()) {
                bl = true;
                return CompletableFuture.completedFuture(bl);
            }
            bl = false;
            return CompletableFuture.completedFuture(bl);
        }
        catch (Throwable throwable) {
            this.logger.error("Error updating payment id: {}", (Object)throwable.getMessage());
            return CompletableFuture.failedFuture(new Exception("payment_update_failed"));
        }
    }

    public void fetchGlobalCookies() {
        GLOBAL_COOKIES.forEach(this::lambda$fetchGlobalCookies$12);
    }

    public CompletableFuture processBuyNow() {
        Function<HttpResponse, Boolean> function = this::lambda$processBuyNow$8;
        return this.execute("Processing...", function, this.api::buyNowSubmitPayment, this.api.buyNowSubmitPaymentBody(this.api.contractId));
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$checkoutBuyNow(WalmartNew var0, CompletableFuture var1_1, int var2_2, Object var3_4) {
        switch (var2_2) {
            case 0: {
                v0 = var0.buyNow();
                if (!v0.isDone()) {
                    var2_3 = v0;
                    return var2_3.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$checkoutBuyNow(io.trickle.task.sites.walmart.graphql.WalmartNew java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((WalmartNew)var0, (CompletableFuture)var2_3, (int)1));
                }
                ** GOTO lbl10
            }
            case 1: {
                v0 = var1_1;
lbl10:
                // 2 sources

                if ((var1_1 = (String)v0.join()) == null) return var0.processBuyNow();
                if (var1_1.isBlank() != false) return var0.processBuyNow();
                var0.lineItemId = var1_1.split("\"itemIds\":\\[\"")[1].split("\"")[0];
                var0.api.contractId = var1_1.split("\"id\":\"")[1].split("\"")[0];
                return var0.processBuyNow();
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture checkoutBuyNow() {
        CompletableFuture completableFuture = this.buyNow();
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$checkoutBuyNow(this, completableFuture2, 1, arg_0));
        }
        String string = (String)completableFuture.join();
        if (string == null) return this.processBuyNow();
        if (string.isBlank()) return this.processBuyNow();
        this.lineItemId = string.split("\"itemIds\":\\[\"")[1].split("\"")[0];
        this.api.contractId = string.split("\"id\":\"")[1].split("\"")[0];
        return this.processBuyNow();
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$buyNow(WalmartNew var0, int var1_1, String var2_2, int var3_3, API var4_4, CompletableFuture var5_6, HttpResponse var6_7, String var7_9, int var8_12, int var9_14, CompletableFuture var10_16, CompletableFuture var11_17, String var12_18, Throwable var13_19, int var14_20, Object var15_21) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [16[CATCHBLOCK]], but top level block is 29[UNCONDITIONALDOLOOP]
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

    public CompletableFuture createCart() {
        Function<HttpResponse, String> function = WalmartNew::lambda$createCart$6;
        return this.execute("Fetching cart", function, this.api.getCartQuery(), this.api::getCart);
    }

    public CompletableFuture submitShipping() {
        if (this.api.addressId != null && !this.api.addressId.isBlank() && this.api.accessPointId != null) {
            if (!this.api.accessPointId.isBlank()) return CompletableFuture.completedFuture(true);
        }
        CompletableFuture completableFuture = this.doPost("Submitting shipping", this.api::getPCID, this.api.PCIDForm(), 200, "newAddress");
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$submitShipping(this, completableFuture2, null, null, null, 1, arg_0));
        }
        String string = (String)completableFuture.join();
        JsonObject jsonObject = new JsonObject(string).getJsonObject("data").getJsonObject("createAccountAddress").getJsonObject("newAddress");
        if (!jsonObject.containsKey("id")) return CompletableFuture.completedFuture(false);
        this.api.addressId = jsonObject.getString("id");
        if (!jsonObject.containsKey("accessPoint")) return CompletableFuture.failedFuture(new Exception("shipping_update_failed"));
        JsonObject jsonObject2 = jsonObject.getJsonObject("accessPoint");
        if (!jsonObject2.containsKey("id")) return CompletableFuture.failedFuture(new Exception("shipping_update_failed"));
        this.api.accessPointId = jsonObject2.getString("id");
        CompletableFuture completableFuture3 = this.doPost("Submitting fulfilment", this.api::fulfillment, this.api.fulfillmentBody(), 200, "\"intent\":\"SHIPPING\"");
        if (!completableFuture3.isDone()) {
            CompletableFuture completableFuture4 = completableFuture3;
            return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$submitShipping(this, completableFuture4, string, jsonObject, jsonObject2, 2, arg_0));
        }
        completableFuture3.join();
        return CompletableFuture.completedFuture(true);
    }

    public boolean priceCheck(String string) {
        if (this.desiredUnitPrice == -1) {
            try {
                this.desiredUnitPrice = Integer.parseInt(this.task.getKeywords()[1]);
            }
            catch (NumberFormatException numberFormatException) {
                this.logger.warn("Missing price-check (limit) for product: '{}'. Skipping...", (Object)this.instanceSignal);
                this.desiredUnitPrice = Integer.MAX_VALUE;
            }
        }
        try {
            double d = Double.parseDouble(Utils.quickParseFirst(string, PRICE_PATTERN)) / (double)Integer.parseInt(this.task.getSize());
            if (d <= (double)this.desiredUnitPrice) {
                return true;
            }
            this.logger.warn("Price exceeds limit of ${}", (Object)this.desiredUnitPrice);
            return false;
        }
        catch (Throwable throwable) {
            this.logger.error("Error occurred on price-check: {}", (Object)throwable.getMessage());
        }
        return false;
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$doGet(WalmartNew var0, String var1_1, Supplier var2_2, Integer var3_3, String[] var4_4, HttpRequest var5_5, CompletableFuture var6_7, HttpResponse var7_8, String var8_9, int var9_11, int var10_12, Throwable var11_19, int var12_20, Object var13_21) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [10[CATCHBLOCK]], but top level block is 17[UNCONDITIONALDOLOOP]
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
     * Exception decompiling
     */
    public static CompletableFuture async$queue(WalmartNew var0, String var1_1, String var2_2, String var3_3, int var4_4, int var5_5, CompletableFuture var6_6, String var7_8, String var8_10, long var9_11, Throwable var11_14, int var12_15, Object var13_22) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [12[CATCHBLOCK]], but top level block is 19[UNCONDITIONALDOLOOP]
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
    public CompletableFuture execute(String string, Function function, Supplier supplier, Object object) {
        this.logger.info(string);
        while (this.running) {
            try {
                HttpResponse httpResponse;
                HttpRequest httpRequest = (HttpRequest)supplier.get();
                if (((HttpRequestImpl)httpRequest).method().equals((Object)HttpMethod.POST)) {
                    CompletableFuture completableFuture = Request.send(httpRequest, object);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture2 = completableFuture;
                        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$execute(this, string, function, (Supplier)supplier, object, httpRequest, completableFuture2, null, 0, 0, null, null, null, 1, arg_0));
                    }
                    httpResponse = (HttpResponse)completableFuture.join();
                } else {
                    CompletableFuture completableFuture = Request.send(httpRequest);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture3 = completableFuture;
                        return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$execute(this, string, function, (Supplier)supplier, object, httpRequest, completableFuture3, null, 0, 0, null, null, null, 2, arg_0));
                    }
                    httpResponse = (HttpResponse)completableFuture.join();
                }
                if (httpResponse != null) {
                    int n = httpResponse.statusCode();
                    if (n == 412 || n == 444 || n == 307) {
                        this.logger.info("Failed {}. Blocked!", (Object)string.toLowerCase(Locale.ROOT));
                        CompletableFuture completableFuture = this.api.handleBadResponse(httpResponse, Request.getURI(httpRequest));
                        if (!completableFuture.isDone()) {
                            CompletableFuture completableFuture4 = completableFuture;
                            return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$execute(this, string, function, (Supplier)supplier, object, httpRequest, completableFuture4, httpResponse, n, 0, null, null, null, 3, arg_0));
                        }
                        int n2 = ((Boolean)completableFuture.join()).booleanValue() ? 1 : 0;
                        if (n2 != 0) continue;
                        CompletableFuture completableFuture5 = VertxUtil.sleep(this.task.getMonitorDelay());
                        if (!completableFuture5.isDone()) {
                            CompletableFuture completableFuture6 = completableFuture5;
                            return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$execute(this, string, function, (Supplier)supplier, object, httpRequest, completableFuture6, httpResponse, n, n2, null, null, null, 4, arg_0));
                        }
                        completableFuture5.join();
                        continue;
                    }
                    Optional optional = Optional.ofNullable(function.apply(httpResponse));
                    if (optional.isPresent()) {
                        return CompletableFuture.completedFuture(optional.get());
                    }
                    Object object2 = Util.parseErrorMessage(httpResponse.bodyAsString());
                    if (object2 == null) {
                        object2 = httpResponse.statusCode() + httpResponse.statusMessage();
                    }
                    this.logger.warn("Failed {}: '{}'", (Object)string.toLowerCase(Locale.ROOT), object2);
                    CompletableFuture completableFuture = VertxUtil.randomSleep(this.task.getMonitorDelay());
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture7 = completableFuture;
                        return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$execute(this, string, function, (Supplier)supplier, object, httpRequest, completableFuture7, httpResponse, n, 0, optional, (String)object2, null, 5, arg_0));
                    }
                    completableFuture.join();
                    continue;
                }
                CompletableFuture completableFuture = VertxUtil.randomSleep(this.task.getRetryDelay());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture8 = completableFuture;
                    return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$execute(this, string, function, (Supplier)supplier, object, httpRequest, completableFuture8, httpResponse, 0, 0, null, null, null, 6, arg_0));
                }
                completableFuture.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error " + string.toLowerCase(Locale.ROOT) + ": {}", (Object)throwable.getMessage());
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug((Object)throwable);
                }
                CompletableFuture completableFuture = VertxUtil.randomSleep(this.task.getRetryDelay());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture9 = completableFuture;
                    return ((CompletableFuture)completableFuture9.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$execute(this, string, function, (Supplier)supplier, object, null, completableFuture9, null, 0, 0, null, null, throwable, 7, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.failedFuture(new Exception("Failed to execute " + string));
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$loginAccount(WalmartNew var0, int var1_1, AccountController var2_2, Account var3_3, CompletableFuture var4_4, JsonObject var5_6, HttpRequest var6_7, HttpResponse var7_8, int var8_10, Throwable var9_12, int var10_13, Object var11_20) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [10[CATCHBLOCK]], but top level block is 18[UNCONDITIONALDOLOOP]
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
     * Exception decompiling
     */
    public static CompletableFuture async$execute(WalmartNew var0, String var1_1, Function var2_2, Supplier var3_3, Object var4_4, HttpRequest var5_5, CompletableFuture var6_7, HttpResponse var7_8, int var8_10, int var9_12, Optional var10_14, String var11_15, Throwable var12_16, int var13_17, Object var14_18) {
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

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$addToCart(WalmartNew var0, int var1_1, API var2_2, CompletableFuture var3_4, CompletableFuture var4_5, CompletableFuture var5_6, Buffer var6_8, HttpRequest var7_9, HttpResponse var8_10, String var9_11, JsonArray var10_12, JsonObject var11_13, String var12_14, int var13_15, Throwable var14_16, int var15_17, Object var16_18) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [15[CATCHBLOCK]], but top level block is 27[UNCONDITIONALDOLOOP]
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

    public static boolean lambda$static$1(String string) {
        if (!string.contains("{\"data\":{\"placeOrder\":{\"id\"")) return false;
        if (!string.contains("\"order\":{\"id\"")) return false;
        return true;
    }

    public static String lambda$quickFunctionParse$2(Integer n, Pattern pattern, HttpResponse httpResponse) {
        if (n != null) {
            if (httpResponse.statusCode() != n.intValue()) return null;
        }
        String string = httpResponse.bodyAsString();
        return Utils.quickParseFirst(string, pattern);
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$updateOrCreateSession(WalmartNew var0, Function var1_1, String var2_3, CompletableFuture var3_4, int var4_5, Object var5_6) {
        switch (var4_5) {
            case 0: {
                try {
                    if (var0.sessionCookies != null) {
                        var0.sessionCookies.clear();
                    }
                    var1_1 = (Function<HttpResponse, String>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, lambda$updateOrCreateSession$9(io.vertx.ext.web.client.HttpResponse ), (Lio/vertx/ext/web/client/HttpResponse;)Ljava/lang/String;)();
                    if (!var0.mode.equals((Object)Mode.MOBILE)) ** GOTO lbl18
                    var2_3 = var0.task.getKeywords().length > 2 ? var0.task.getKeywords()[2] : "https://www.walmart.com/ip/Mario-Kart-8-Deluxe-Edition-Nintendo-Switch/55432571";
                    v0 = var0.execute("Configuring...", var1_1, ((WalmartNewAPIMobile)var0.api).terraFirmaForm(Utils.quickParseFirst(var2_3, new Pattern[]{WalmartNew.KEYWORD_PID_PATTERN})), (Supplier<HttpRequest>)LambdaMetafactory.metafactory(null, null, null, ()Ljava/lang/Object;, lambda$updateOrCreateSession$10(java.lang.String ), ()Lio/vertx/ext/web/client/HttpRequest;)((WalmartNew)var0, (String)var2_3));
                    if (!v0.isDone()) {
                        var3_4 = v0;
                        return var3_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$updateOrCreateSession(io.trickle.task.sites.walmart.graphql.WalmartNew java.util.function.Function java.lang.String java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((WalmartNew)var0, var1_1, (String)var2_3, (CompletableFuture)var3_4, (int)1));
                    }
lbl14:
                    // 3 sources

                    while (true) {
                        v0.join();
                        ** GOTO lbl25
                        break;
                    }
lbl18:
                    // 1 sources

                    v1 = var0.execute("Configuring...", (Function)var1_1, (Object)null, (Supplier<HttpRequest>)LambdaMetafactory.metafactory(null, null, null, ()Ljava/lang/Object;, getCheckoutPage(), ()Lio/vertx/ext/web/client/HttpRequest;)((API)var0.api));
                    if (!v1.isDone()) {
                        var3_4 = v1;
                        return var3_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$updateOrCreateSession(io.trickle.task.sites.walmart.graphql.WalmartNew java.util.function.Function java.lang.String java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((WalmartNew)var0, var1_1, null, (CompletableFuture)var3_4, (int)2));
                    }
lbl22:
                    // 3 sources

                    while (true) {
                        v1.join();
lbl25:
                        // 2 sources

                        var0.sessionTimestamp = System.currentTimeMillis();
                        var0.sessionCookies = new CookieJar((CookieJar)var0.api.getWebClient().cookieStore());
                        var0.sessionCookies.get(true, ".walmart.com", "/").forEach((Consumer<Cookie>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)V, lambda$updateOrCreateSession$11(io.netty.handler.codec.http.cookie.Cookie ), (Lio/netty/handler/codec/http/cookie/Cookie;)V)());
                        var0.fetchGlobalCookies();
                        var0.api.getWebClient().cookieStore().putFromOther(var0.sessionCookies);
                        var0.logger.info("Updated session...");
                        return CompletableFuture.completedFuture(null);
                    }
                }
                catch (Throwable var1_2) {
                    var0.logger.error("Error occurred updating session: {}", (Object)var1_2.getMessage());
                }
                return CompletableFuture.completedFuture(null);
            }
            case 1: {
                v0 = var3_4;
                ** continue;
            }
            case 2: {
                v1 = var3_4;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public String lambda$checkout$7(HttpResponse httpResponse) {
        String string = httpResponse.bodyAsString();
        if (!Utils.containsIgnoreCase(string, "created")) return null;
        if (!this.api.loggedIn) return "";
        return Utils.quickParseFirst(string, TENDER_PATTERN);
    }

    public CompletableFuture updateOrCreateSession() {
        try {
            if (this.sessionCookies != null) {
                this.sessionCookies.clear();
            }
            Function<HttpResponse, String> function = WalmartNew::lambda$updateOrCreateSession$9;
            if (this.mode.equals((Object)Mode.MOBILE)) {
                String string = this.task.getKeywords().length > 2 ? this.task.getKeywords()[2] : "https://www.walmart.com/ip/Mario-Kart-8-Deluxe-Edition-Nintendo-Switch/55432571";
                CompletableFuture completableFuture = this.execute("Configuring...", function, ((WalmartNewAPIMobile)this.api).terraFirmaForm(Utils.quickParseFirst(string, KEYWORD_PID_PATTERN)), () -> this.lambda$updateOrCreateSession$10(string));
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$updateOrCreateSession(this, function, string, completableFuture2, 1, arg_0));
                }
                completableFuture.join();
            } else {
                CompletableFuture completableFuture = this.execute("Configuring...", (Function)function, (Object)null, this.api::getCheckoutPage);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture3 = completableFuture;
                    return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$updateOrCreateSession(this, function, null, completableFuture3, 2, arg_0));
                }
                completableFuture.join();
            }
            this.sessionTimestamp = System.currentTimeMillis();
            this.sessionCookies = new CookieJar((CookieJar)this.api.getWebClient().cookieStore());
            this.sessionCookies.get(true, ".walmart.com", "/").forEach(WalmartNew::lambda$updateOrCreateSession$11);
            this.fetchGlobalCookies();
            this.api.getWebClient().cookieStore().putFromOther(this.sessionCookies);
            this.logger.info("Updated session...");
            return CompletableFuture.completedFuture(null);
        }
        catch (Throwable throwable) {
            this.logger.error("Error occurred updating session: {}", (Object)throwable.getMessage());
        }
        return CompletableFuture.completedFuture(null);
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
                if (cookie == null) continue;
                this.api.getCookies().put(cookie);
                continue;
            }
            catch (Throwable throwable) {
                this.logger.warn("Error parsing session state: {}", (Object)throwable.getMessage());
            }
        }
        try {
            CompletableFuture completableFuture = this.doPost("Validating Session", this.api::getAccountPage, this.api.getAccountPageForm(), 200, null);
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$sessionLogon(this, account, string, jsonArray, completableFuture2, 1, arg_0));
            }
            String string3 = (String)completableFuture.join();
            if (!string3.contains(account.getUser())) return CompletableFuture.completedFuture(false);
            return CompletableFuture.completedFuture(true);
        }
        catch (Throwable throwable) {
            this.logger.warn("Error validating session: {}", (Object)throwable.getMessage());
        }
        return CompletableFuture.completedFuture(false);
    }

    public CompletableFuture queue() {
        String string = null;
        String string2 = null;
        Object object = "";
        int n = 0;
        int n2 = 0;
        while (this.running) {
            if (n2 != 0) return CompletableFuture.completedFuture(true);
            try {
                long l;
                String string3;
                block18: {
                    if (string == null || string.isBlank()) {
                        this.logger.info("Fetching queue details...");
                        CompletableFuture completableFuture = this.waitForQueue();
                        if (!completableFuture.isDone()) {
                            CompletableFuture completableFuture2 = completableFuture;
                            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$queue(this, string, string2, (String)object, n, n2, completableFuture2, null, null, 0L, null, 1, arg_0));
                        }
                        string = (String)completableFuture.join();
                        continue;
                    }
                    if (!this.api.getWebClient().cookieStore().contains("wr")) {
                        if (this.mode.equals((Object)Mode.DESKTOP)) {
                            string3 = URLDecoder.decode(string, StandardCharsets.UTF_8);
                            String string4 = Utils.quickParseFirst(string3, TICKET_PATTERN);
                            CompletableFuture completableFuture = this.doGet("Fetching queue", () -> this.lambda$queue$13(string4), 200, "\"queue\":\"");
                            if (!completableFuture.isDone()) {
                                CompletableFuture completableFuture3 = completableFuture;
                                return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$queue(this, string, string2, (String)object, n, n2, completableFuture3, string3, string4, 0L, null, 2, arg_0));
                            }
                            completableFuture.join();
                            continue;
                        }
                        string3 = string;
                        CompletableFuture completableFuture = this.doGet("Fetching queue", () -> this.lambda$queue$14(string3), 200, "\"queue\":\"");
                        if (!completableFuture.isDone()) {
                            CompletableFuture completableFuture4 = completableFuture;
                            return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$queue(this, string, string2, (String)object, n, n2, completableFuture4, string3, null, 0L, null, 3, arg_0));
                        }
                        completableFuture.join();
                        continue;
                    }
                    CompletableFuture completableFuture = this.doGet("Waiting in queue. Salepaused -> " + (n != 0) + " " + (String)object, this.api::validate, 200, "\"state\"");
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture5 = completableFuture;
                        return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$queue(this, string, string2, (String)object, n, n2, completableFuture5, null, null, 0L, null, 4, arg_0));
                    }
                    string2 = (String)completableFuture.join();
                    if (string2 == null) {
                        this.logger.warn("Failed to load queue. Retrying...");
                        continue;
                    }
                    if (string2.contains("\"state\":\"valid\"")) {
                        n2 = 1;
                        this.logger.info("Passed queue! Proceeding");
                        return CompletableFuture.completedFuture(true);
                    }
                    string3 = Utils.quickParseFirst(string2, REFRESH_TIME_PATTERN);
                    if (string3 == null) {
                        this.logger.info("Queue finished.");
                        return CompletableFuture.completedFuture(false);
                    }
                    l = Long.parseLong(string3);
                    try {
                        String string5 = Utils.quickParseFirst(string2, EXPECTED_TIME_PATTERN);
                        long l2 = Long.parseLong(string5);
                        object = "Expected exit time: " + Instant.ofEpochMilli(l2).atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                        n = string2.contains("state\":\"paused") ? 1 : 0;
                    }
                    catch (Exception exception) {
                        if (!this.logger.isDebugEnabled()) break block18;
                        this.logger.debug((Object)exception);
                    }
                }
                CompletableFuture completableFuture = VertxUtil.hardCodedSleep(l - System.currentTimeMillis());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$queue(this, string, string2, (String)object, n, n2, completableFuture6, string3, null, l, null, 5, arg_0));
                }
                completableFuture.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error in queue: {}", (Object)throwable.getMessage());
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug((Object)throwable);
                }
                CompletableFuture completableFuture = VertxUtil.randomSleep(this.task.getRetryDelay());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$queue(this, string, string2, (String)object, n, n2, completableFuture7, null, null, 0L, throwable, 6, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(true);
    }

    public CompletableFuture doGet(String string, Supplier supplier, Integer n, String ... stringArray) {
        this.logger.info(string);
        while (this.running) {
            try {
                HttpRequest httpRequest = (HttpRequest)supplier.get();
                CompletableFuture completableFuture = Request.send(httpRequest);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$doGet(this, string, (Supplier)supplier, n, stringArray, httpRequest, completableFuture2, null, null, 0, 0, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse != null) {
                    int n2;
                    String string2;
                    if (httpResponse.statusCode() == 210) {
                        this.api.getPxAPI().reset();
                        CompletableFuture completableFuture3 = this.api.initialisePX();
                        if (!completableFuture3.isDone()) {
                            CompletableFuture completableFuture4 = completableFuture3;
                            return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$doGet(this, string, (Supplier)supplier, n, stringArray, httpRequest, completableFuture4, httpResponse, null, 0, 0, null, 2, arg_0));
                        }
                        completableFuture3.join();
                    }
                    if (httpResponse.statusCode() >= 300 && httpResponse.statusCode() < 400) {
                        string2 = httpResponse.getHeader("location");
                        n2 = stringArray == null || Utils.containsAllWords(string2, stringArray) ? 1 : 0;
                    } else {
                        string2 = httpResponse.bodyAsString();
                        int n3 = n2 = stringArray == null || Utils.containsAllWords(string2, stringArray) ? 1 : 0;
                    }
                    if ((n == null || httpResponse.statusCode() == n.intValue()) && n2 != 0) {
                        return CompletableFuture.completedFuture(string2);
                    }
                    this.logger.warn("Failed {}: '{}'", (Object)string.toLowerCase(Locale.ROOT), (Object)(httpResponse.statusCode() + httpResponse.statusMessage()));
                    CompletableFuture completableFuture5 = this.api.handleBadResponse(httpResponse, Request.getURI(httpRequest));
                    if (!completableFuture5.isDone()) {
                        CompletableFuture completableFuture6 = completableFuture5;
                        return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$doGet(this, string, (Supplier)supplier, n, stringArray, httpRequest, completableFuture6, httpResponse, string2, n2, 0, null, 3, arg_0));
                    }
                    int n4 = ((Boolean)completableFuture5.join()).booleanValue() ? 1 : 0;
                    if (n4 != 0) continue;
                    CompletableFuture completableFuture7 = VertxUtil.sleep(this.task.getMonitorDelay());
                    if (!completableFuture7.isDone()) {
                        CompletableFuture completableFuture8 = completableFuture7;
                        return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$doGet(this, string, (Supplier)supplier, n, stringArray, httpRequest, completableFuture8, httpResponse, string2, n2, n4, null, 4, arg_0));
                    }
                    completableFuture7.join();
                    continue;
                }
                CompletableFuture completableFuture9 = VertxUtil.sleep(this.task.getRetryDelay());
                if (!completableFuture9.isDone()) {
                    CompletableFuture completableFuture10 = completableFuture9;
                    return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$doGet(this, string, (Supplier)supplier, n, stringArray, httpRequest, completableFuture10, httpResponse, null, 0, 0, null, 5, arg_0));
                }
                completableFuture9.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error {}: {}", (Object)string.toLowerCase(Locale.ROOT), (Object)throwable.getMessage());
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug((Object)throwable);
                }
                CompletableFuture completableFuture = super.randomSleep(this.task.getRetryDelay());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture11 = completableFuture;
                    return ((CompletableFuture)completableFuture11.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$doGet(this, string, (Supplier)supplier, n, stringArray, null, completableFuture11, null, null, 0, 0, throwable, 6, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public HttpRequest lambda$updateOrCreateSession$10(String string) {
        return this.api.terraFirma(Utils.quickParseFirst(string, KEYWORD_PID_PATTERN), false);
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$sessionLogon(WalmartNew var0, Account var1_1, String var2_2, JsonArray var3_3, CompletableFuture var4_4, int var5_7, Object var6_10) {
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
                        if (var6_10 == null) continue;
                        var0.api.getCookies().put((Cookie)var6_10);
                        continue;
                    }
                    catch (Throwable var5_9) {
                        var0.logger.warn("Error parsing session state: {}", (Object)var5_9.getMessage());
                    }
                }
                try {
                    v0 = var0.doPost("Validating Session", (Supplier<HttpRequest>)LambdaMetafactory.metafactory(null, null, null, ()Ljava/lang/Object;, getAccountPage(), ()Lio/vertx/ext/web/client/HttpRequest;)((API)var0.api), var0.api.getAccountPageForm(), 200, null);
                    if (!v0.isDone()) {
                        var7_11 = v0;
                        return var7_11.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$sessionLogon(io.trickle.task.sites.walmart.graphql.WalmartNew io.trickle.account.Account java.lang.String io.vertx.core.json.JsonArray java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((WalmartNew)var0, (Account)var1_1, (String)var2_2, (JsonArray)var3_3, (CompletableFuture)var7_11, (int)1));
                    }
                    ** GOTO lbl32
                }
                catch (Throwable var4_6) {
                    var0.logger.warn("Error validating session: {}", (Object)var4_6.getMessage());
                }
                return CompletableFuture.completedFuture(false);
            }
            case 1: {
                v0 = var4_4;
lbl32:
                // 2 sources

                if ((var4_4 = (String)v0.join()).contains(var1_1.getUser()) == false) return CompletableFuture.completedFuture(false);
                return CompletableFuture.completedFuture(true);
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture waitForQueue() {
        String string = this.instanceSignal + "queue";
        while (this.running) {
            try {
                Object object;
                HttpResponse httpResponse;
                HttpRequest httpRequest;
                if (this.mode.equals((Object)Mode.DESKTOP)) {
                    httpRequest = this.api.product(this.task.getKeywords()[2].toLowerCase(Locale.ROOT));
                    CompletableFuture completableFuture = Request.send(httpRequest);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture2 = completableFuture;
                        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$waitForQueue(this, string, httpRequest, completableFuture2, null, null, 0, null, 1, arg_0));
                    }
                    httpResponse = (HttpResponse)completableFuture.join();
                } else {
                    object = ((WalmartNewAPIMobile)this.api).terraFirmaForm(Utils.quickParseFirst(this.task.getKeywords()[2], KEYWORD_PID_PATTERN));
                    httpRequest = this.api.terraFirma(Utils.quickParseFirst(this.task.getKeywords()[2], KEYWORD_PID_PATTERN), false);
                    CompletableFuture completableFuture = Request.send(httpRequest, (JsonObject)object);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture3 = completableFuture;
                        return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$waitForQueue(this, string, httpRequest, completableFuture3, (JsonObject)object, null, 0, null, 2, arg_0));
                    }
                    httpResponse = (HttpResponse)completableFuture.join();
                }
                if (httpResponse != null) {
                    if (httpResponse.statusCode() == 210) {
                        this.api.getPxAPI().reset();
                        CompletableFuture completableFuture = this.api.initialisePX();
                        if (!completableFuture.isDone()) {
                            CompletableFuture completableFuture4 = completableFuture;
                            return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$waitForQueue(this, string, httpRequest, completableFuture4, null, httpResponse, 0, null, 3, arg_0));
                        }
                        completableFuture.join();
                    }
                    if (this.mode.equals((Object)Mode.DESKTOP)) {
                        if (httpResponse.statusCode() >= 300 && httpResponse.statusCode() < 400 && httpResponse.headers().contains("location") && ((String)(object = httpResponse.getHeader("location"))).contains("qpdata=")) {
                            VertxUtil.sendSignal(string);
                            this.logger.info("Queue started! Proceeding...");
                            return CompletableFuture.completedFuture(object);
                        }
                    } else if (httpResponse.bodyAsString().contains("api.waiting-room")) {
                        VertxUtil.sendSignal(string);
                        this.logger.info("Queue started! Proceeding...");
                        return CompletableFuture.completedFuture(httpResponse.bodyAsJsonObject().getString("url", null));
                    }
                    CompletableFuture completableFuture = this.api.handleBadResponse(httpResponse, Request.getURI(httpRequest));
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture5 = completableFuture;
                        return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$waitForQueue(this, string, httpRequest, completableFuture5, null, httpResponse, 0, null, 4, arg_0));
                    }
                    int n = ((Boolean)completableFuture.join()).booleanValue() ? 1 : 0;
                    if (n == 0) {
                        CompletableFuture completableFuture6 = VertxUtil.signalSleep(string, this.task.getMonitorDelay());
                        if (!completableFuture6.isDone()) {
                            CompletableFuture completableFuture7 = completableFuture6;
                            return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$waitForQueue(this, string, httpRequest, completableFuture7, null, httpResponse, n, null, 5, arg_0));
                        }
                        completableFuture6.join();
                    }
                    this.logger.info("Waiting for queue to start [{}]", (Object)httpResponse.statusCode());
                    continue;
                }
                this.logger.warn("Failed to wait for queue. Retrying...");
                CompletableFuture completableFuture = VertxUtil.signalSleep(string, this.task.getRetryDelay());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture8 = completableFuture;
                    return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$waitForQueue(this, string, httpRequest, completableFuture8, null, httpResponse, 0, null, 6, arg_0));
                }
                completableFuture.join();
            }
            catch (Throwable throwable) {
                this.logger.warn("Error occurred waiting for queue: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = VertxUtil.randomSleep(10000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture9 = completableFuture;
                    return ((CompletableFuture)completableFuture9.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$waitForQueue(this, string, null, completableFuture9, null, null, 0, throwable, 7, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public static void lambda$updateOrCreateSession$11(Cookie cookie) {
        if (!cookie.value().equalsIgnoreCase("1")) return;
        if (cookie.name().equals("g")) return;
        GLOBAL_COOKIES.add((Object)cookie);
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$doPost(WalmartNew var0, String var1_1, Supplier var2_2, Object var3_3, Integer var4_4, String[] var5_5, HttpRequest var6_6, CompletableFuture var7_8, HttpResponse var8_9, String var9_10, int var10_12, String var11_14, int var12_15, Throwable var13_16, int var14_17, Object var15_18) {
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

    public HttpRequest lambda$queue$13(String string) {
        return this.api.issueTicket(string);
    }

    public CompletableFuture createAccount(boolean bl) {
        this.logger.info("Creating account");
        int n = 0;
        while (this.running) {
            if (n > 500) return CompletableFuture.failedFuture(new Exception());
            ++n;
            this.api.setLoggedIn(false);
            this.task.getProfile().setAccountEmail(null);
            this.task.getProfile().setAccountPassword(null);
            try {
                JsonObject jsonObject = this.api.accountCreateForm();
                HttpRequest httpRequest = this.api.createAccount();
                CompletableFuture completableFuture = Request.send(httpRequest, jsonObject);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$createAccount(this, (int)(bl ? 1 : 0), n, jsonObject, httpRequest, completableFuture2, null, 0, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                if (httpResponse.statusCode() == 201 || httpResponse.statusCode() == 200 || httpResponse.statusCode() == 206) {
                    if (bl) {
                        Account account = new Account(jsonObject.getString("email"), jsonObject.getString("password"), Site.WALMART);
                        if (!httpResponse.cookies().isEmpty()) {
                            JsonArray jsonArray = new JsonArray();
                            for (String string : httpResponse.cookies()) {
                                jsonArray.add((Object)string);
                            }
                            account.setSessionString(jsonArray.encode());
                        }
                        this.vertx.eventBus().send("accounts.writer", (Object)account);
                        this.vertx.eventBus().send("accounts.writer.session", (Object)account);
                        if (this.logger.isDebugEnabled()) {
                            this.logger.debug("Account create responded with: [{}]{}", (Object)httpResponse.statusCode(), (Object)httpResponse.bodyAsString());
                        }
                    }
                    this.task.getProfile().setAccountEmail(jsonObject.getString("email", null));
                    this.task.getProfile().setAccountPassword(jsonObject.getString("password", null));
                    this.api.setLoggedIn(true);
                    this.logger.info("Created account successfully!");
                    return CompletableFuture.completedFuture(null);
                }
                this.logger.warn("Creating account: status:'{}'", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture3 = this.api.handleBadResponse(httpResponse, Request.getURI(httpRequest));
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$createAccount(this, (int)(bl ? 1 : 0), n, jsonObject, httpRequest, completableFuture4, httpResponse, 0, null, 2, arg_0));
                }
                int n2 = ((Boolean)completableFuture3.join()).booleanValue() ? 1 : 0;
                if (n2 != 0) continue;
                CompletableFuture completableFuture5 = VertxUtil.sleep(this.task.getRetryDelay());
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$createAccount(this, (int)(bl ? 1 : 0), n, jsonObject, httpRequest, completableFuture6, httpResponse, n2, null, 3, arg_0));
                }
                completableFuture5.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error occurred creating account: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = super.randomSleep(12000);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$createAccount(this, (int)(bl ? 1 : 0), n, null, null, completableFuture7, null, 0, throwable, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.failedFuture(new Exception());
    }

    public void lambda$run$5(Long l) {
        Request.execute(this.api.getWebClient().getAbs("https://www.walmart.com/favicon.ico").putHeader("user-agent", this.api.getPxAPI().getDeviceUA()));
    }

    public void reset() {
        this.api.addressId = null;
        this.api.accessPointId = null;
        this.api.paymentId = null;
    }

    public static boolean lambda$static$0(String string) {
        if (!string.contains("{\"data\":{\"placeOrder\":{\"__typename\":\"PurchaseContract\",\"id\"")) return false;
        if (!string.contains("\"order\":{\"__typename\":\"OrderInfo\",\"id\"")) return false;
        return true;
    }

    public void lambda$fetchGlobalCookies$12(Cookie cookie) {
        if (this.sessionCookies.contains(cookie.name())) return;
        this.sessionCookies.put(cookie);
    }

    public CompletableFuture addToCart() {
        int n = 0;
        while (this.running) {
            if (n++ > 50) return CompletableFuture.completedFuture(false);
            try {
                Object object;
                Object object2;
                if (this.api.cartId == null || this.api.cartId.isBlank()) {
                    CompletableFuture completableFuture = this.createCart();
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture2 = completableFuture;
                        API aPI = this.api;
                        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$addToCart(this, n, aPI, completableFuture2, null, null, null, null, null, null, null, null, null, 0, null, 1, arg_0));
                    }
                    this.api.cartId = (String)completableFuture.join();
                    if (!this.task.getMode().contains("fast")) continue;
                    if (this.api.isLoggedIn()) {
                        object2 = this.submitShipping();
                        object = this.submitBilling();
                        CompletableFuture<Void> completableFuture3 = CompletableFuture.allOf(new CompletableFuture[]{object2, object});
                        if (!completableFuture3.isDone()) {
                            CompletableFuture<Void> completableFuture4 = completableFuture3;
                            return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$addToCart(this, n, null, (CompletableFuture)object2, (CompletableFuture)object, completableFuture4, null, null, null, null, null, null, null, 0, null, 2, arg_0));
                        }
                        completableFuture3.join();
                        continue;
                    }
                    CompletableFuture completableFuture5 = this.submitShipping();
                    if (!completableFuture5.isDone()) {
                        CompletableFuture completableFuture6 = completableFuture5;
                        return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$addToCart(this, n, null, completableFuture6, null, null, null, null, null, null, null, null, null, 0, null, 3, arg_0));
                    }
                    completableFuture5.join();
                    continue;
                }
                this.fetchGlobalCookies();
                object2 = this.api.atcForm().toBuffer();
                object = this.api.addToCart();
                CompletableFuture completableFuture = Request.send(object, object2);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$addToCart(this, n, null, completableFuture7, null, null, (Buffer)object2, (HttpRequest)object, null, null, null, null, null, 0, null, 4, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse != null) {
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug("ATC Responded: {}", (Object)httpResponse.bodyAsJsonObject().encodePrettily());
                    }
                    if (httpResponse.statusCode() >= 200 && httpResponse.statusCode() <= 300) {
                        String string = httpResponse.bodyAsString();
                        if (string.contains("\"lineItems\":[{\"")) {
                            VertxUtil.sendSignal(this.instanceSignal);
                            if (this.priceCheck(httpResponse.bodyAsString())) {
                                this.logger.debug("Successfully added to cart!");
                                return CompletableFuture.completedFuture(true);
                            }
                            CompletableFuture completableFuture8 = VertxUtil.randomSleep(this.task.getRetryDelay());
                            if (!completableFuture8.isDone()) {
                                CompletableFuture completableFuture9 = completableFuture8;
                                return ((CompletableFuture)completableFuture9.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$addToCart(this, n, null, completableFuture9, null, null, (Buffer)object2, (HttpRequest)object, httpResponse, string, null, null, null, 0, null, 5, arg_0));
                            }
                            completableFuture8.join();
                            continue;
                        }
                        if (string.contains("\"code\":\"out_of_stock\"") || string.contains("\"lineItems\":[]")) {
                            this.logger.warn("Waiting for restock: '{}'", (Object)"OOS");
                            CompletableFuture completableFuture10 = VertxUtil.randomSignalSleep(this.instanceSignal, this.task.getMonitorDelay());
                            if (!completableFuture10.isDone()) {
                                CompletableFuture completableFuture11 = completableFuture10;
                                return ((CompletableFuture)completableFuture11.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$addToCart(this, n, null, completableFuture11, null, null, (Buffer)object2, (HttpRequest)object, httpResponse, string, null, null, null, 0, null, 6, arg_0));
                            }
                            completableFuture10.join();
                            continue;
                        }
                        if (!string.contains("{\"errors\":[{")) continue;
                        JsonArray jsonArray = httpResponse.bodyAsJsonObject().getJsonArray("errors");
                        JsonObject jsonObject = jsonArray.getJsonObject(0);
                        String string2 = jsonObject.getString("message", "").toLowerCase();
                        if (string2.contains("unauthorized access to cart")) {
                            this.logger.info("Invalid cart. Resetting...");
                            this.reset();
                            this.api.cartId = null;
                            continue;
                        }
                        if (string2.contains("item unavailable")) {
                            this.logger.warn("Waiting for product to be live: '{}'", (Object)httpResponse.statusCode());
                        } else if (string2.contains("unauthenticated")) {
                            this.logger.warn("Unauthenticated product access. Likely account required: '{}'", (Object)httpResponse.statusCode());
                        } else {
                            this.logger.warn("Failed to atc: {}", (Object)string2);
                        }
                        CompletableFuture completableFuture12 = VertxUtil.randomSignalSleep(this.instanceSignal, this.task.getMonitorDelay());
                        if (!completableFuture12.isDone()) {
                            CompletableFuture completableFuture13 = completableFuture12;
                            return ((CompletableFuture)completableFuture13.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$addToCart(this, n, null, completableFuture13, null, null, (Buffer)object2, (HttpRequest)object, httpResponse, string, jsonArray, jsonObject, string2, 0, null, 7, arg_0));
                        }
                        completableFuture12.join();
                        continue;
                    }
                    this.logger.warn("Handling status on atc: {}", (Object)httpResponse.statusCode());
                    CompletableFuture completableFuture14 = this.api.handleBadResponse(httpResponse, Request.getURI(object));
                    if (!completableFuture14.isDone()) {
                        CompletableFuture completableFuture15 = completableFuture14;
                        return ((CompletableFuture)completableFuture15.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$addToCart(this, n, null, completableFuture15, null, null, (Buffer)object2, (HttpRequest)object, httpResponse, null, null, null, null, 0, null, 8, arg_0));
                    }
                    int n2 = ((Boolean)completableFuture14.join()).booleanValue() ? 1 : 0;
                    if (n2 != 0) continue;
                    CompletableFuture completableFuture16 = VertxUtil.sleep(this.task.getMonitorDelay());
                    if (!completableFuture16.isDone()) {
                        CompletableFuture completableFuture17 = completableFuture16;
                        return ((CompletableFuture)completableFuture17.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$addToCart(this, n, null, completableFuture17, null, null, (Buffer)object2, (HttpRequest)object, httpResponse, null, null, null, null, n2, null, 9, arg_0));
                    }
                    completableFuture16.join();
                    continue;
                }
                this.logger.warn("Failed to add to cart. Retrying...");
                CompletableFuture completableFuture18 = VertxUtil.sleep(this.task.getRetryDelay());
                if (!completableFuture18.isDone()) {
                    CompletableFuture completableFuture19 = completableFuture18;
                    return ((CompletableFuture)completableFuture19.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$addToCart(this, n, null, completableFuture19, null, null, (Buffer)object2, (HttpRequest)object, httpResponse, null, null, null, null, 0, null, 10, arg_0));
                }
                completableFuture18.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error adding to cart: {}", (Object)throwable.getMessage());
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug((Object)throwable);
                }
                CompletableFuture completableFuture = VertxUtil.randomSleep(this.task.getRetryDelay());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture20 = completableFuture;
                    return ((CompletableFuture)completableFuture20.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$addToCart(this, n, null, completableFuture20, null, null, null, null, null, null, null, null, null, 0, throwable, 11, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(false);
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$checkout(WalmartNew var0, int var1_1, int var2_2, String var3_3, CompletableFuture var4_4, Function var5_6, String var6_7, JsonObject var7_8, JsonObject var8_9, JsonObject var9_10, String var10_11, int var11_12, Object var12_13) {
        block31: {
            switch (var11_12) {
                case 0: {
                    var1_1 = 0;
                    var2_2 = 0;
                    var3_3 = null;
lbl6:
                    // 5 sources

                    while (var0.running && var1_1 <= 10) {
                        if (var2_2 != 0) ** GOTO lbl14
                        v0 = var0.submitShipping();
                        if (!v0.isDone()) {
                            var9_10 = v0;
                            return var9_10.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$checkout(io.trickle.task.sites.walmart.graphql.WalmartNew int int java.lang.String java.util.concurrent.CompletableFuture java.util.function.Function java.lang.String io.vertx.core.json.JsonObject io.vertx.core.json.JsonObject io.vertx.core.json.JsonObject java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((WalmartNew)var0, (int)var1_1, (int)var2_2, (String)var3_3, (CompletableFuture)var9_10, null, null, null, null, null, null, (int)1));
                        }
                        ** GOTO lbl22
lbl14:
                        // 1 sources

                        v1 = var0.doPost("Processing...", (Supplier<HttpRequest>)LambdaMetafactory.metafactory(null, null, null, ()Ljava/lang/Object;, submitPayment(), ()Lio/vertx/ext/web/client/HttpRequest;)((API)var0.api), var0.api.getPaymentForm(var0.token), 200, null);
                        if (!v1.isDone()) {
                            var9_10 = v1;
                            return var9_10.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$checkout(io.trickle.task.sites.walmart.graphql.WalmartNew int int java.lang.String java.util.concurrent.CompletableFuture java.util.function.Function java.lang.String io.vertx.core.json.JsonObject io.vertx.core.json.JsonObject io.vertx.core.json.JsonObject java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((WalmartNew)var0, (int)var1_1, (int)var2_2, (String)var3_3, (CompletableFuture)var9_10, null, null, null, null, null, null, (int)5));
                        }
                        ** GOTO lbl56
                    }
                    break block31;
                }
                {
                    default: {
                        throw new IllegalArgumentException();
                    }
lbl22:
                    // 2 sources

                    while (true) {
                        v0.join();
                        var4_4 = (Function<HttpResponse, String>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, lambda$checkout$7(io.vertx.ext.web.client.HttpResponse ), (Lio/vertx/ext/web/client/HttpResponse;)Ljava/lang/String;)((WalmartNew)var0);
                        v2 = var0.execute("Creating contract", (Function)var4_4, (Supplier<HttpRequest>)LambdaMetafactory.metafactory(null, null, null, ()Ljava/lang/Object;, createContract(), ()Lio/vertx/ext/web/client/HttpRequest;)((API)var0.api), var0.api.createContractBody());
                        if (!v2.isDone()) {
                            var9_10 = v2;
                            return var9_10.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$checkout(io.trickle.task.sites.walmart.graphql.WalmartNew int int java.lang.String java.util.concurrent.CompletableFuture java.util.function.Function java.lang.String io.vertx.core.json.JsonObject io.vertx.core.json.JsonObject io.vertx.core.json.JsonObject java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((WalmartNew)var0, (int)var1_1, (int)var2_2, (String)var3_3, (CompletableFuture)var9_10, (Function)var4_4, null, null, null, null, null, (int)2));
                        }
lbl33:
                        // 3 sources

                        while (true) {
                            var3_3 = (String)v2.join();
                            var0.api.contractId = var0.api.cookieStore().getCookieValue("AZ_ST_PC").split("%7C")[0];
                            if (var0.api.paymentId != null && var0.api.isLoggedIn()) ** GOTO lbl44
                            v3 = var0.submitBilling();
                            if (!v3.isDone()) {
                                var9_10 = v3;
                                return var9_10.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$checkout(io.trickle.task.sites.walmart.graphql.WalmartNew int int java.lang.String java.util.concurrent.CompletableFuture java.util.function.Function java.lang.String io.vertx.core.json.JsonObject io.vertx.core.json.JsonObject io.vertx.core.json.JsonObject java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((WalmartNew)var0, (int)var1_1, (int)var2_2, (String)var3_3, (CompletableFuture)var9_10, (Function)var4_4, null, null, null, null, null, (int)3));
                            }
lbl41:
                            // 3 sources

                            while (true) {
                                v3.join();
lbl44:
                                // 2 sources

                                var2_2 = 1;
                                v4 = var0.doPost("Updating billing", (Supplier<HttpRequest>)LambdaMetafactory.metafactory(null, null, null, ()Ljava/lang/Object;, updateTender(), ()Lio/vertx/ext/web/client/HttpRequest;)((API)var0.api), var0.api.tenderUpdateForm(var3_3), 200, new String[]{"{\"data\":{\"updateTenderPlan"});
                                if (!v4.isDone()) {
                                    var9_10 = v4;
                                    return var9_10.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$checkout(io.trickle.task.sites.walmart.graphql.WalmartNew int int java.lang.String java.util.concurrent.CompletableFuture java.util.function.Function java.lang.String io.vertx.core.json.JsonObject io.vertx.core.json.JsonObject io.vertx.core.json.JsonObject java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((WalmartNew)var0, (int)var1_1, (int)var2_2, (String)var3_3, (CompletableFuture)var9_10, (Function)var4_4, null, null, null, null, null, (int)4));
                                }
lbl52:
                                // 3 sources

                                while (true) {
                                    v4.join();
                                    ** GOTO lbl6
                                    break;
                                }
                                break;
                            }
                            break;
                        }
                        break;
                    }
lbl56:
                    // 2 sources

                    while (true) {
                        var4_4 = (String)v1.join();
                        if (var0.logger.isDebugEnabled()) {
                            var0.logger.debug("processPayment responded with: {}", var4_4);
                        }
                        if (var4_4 == null) ** GOTO lbl101
                        if (var0.mode.equals((Object)Mode.DESKTOP) != false ? WalmartNew.DESKTOP_CHECKOUT_FLAG.test((String)var4_4) != false : WalmartNew.MOBILE_CHECKOUT_FLAG.test((String)var4_4) != false) {
                            VertxUtil.sendSignal(var0.instanceSignal);
                            var0.logger.info("Successfully checked out!");
                            Analytics.success(var0.task, new JsonObject((String)var4_4), var0.api.proxyString());
                            return CompletableFuture.completedFuture(true);
                        }
                        if (!var4_4.contains("{\"errors\":[{\"message\":\"")) ** GOTO lbl101
                        var5_6 = new JsonObject((String)var4_4);
                        var6_7 = var5_6.getJsonArray("errors").getJsonObject(0);
                        var0.logger.info(var6_7.getString("message"));
                        if (!var6_7.containsKey("extensions")) ** GOTO lbl94
                        var7_8 /* !! */  = var6_7.getJsonObject("extensions");
                        if (!var7_8 /* !! */ .containsKey("code")) ** GOTO lbl101
                        var8_9 = var7_8 /* !! */ .getString("code");
                        if (!var8_9.contains("pc_expired") && !var8_9.contains("Purchase Contract has expired")) ** GOTO lbl78
                        var0.logger.info("Session expiry detected. Resetting!");
                        var2_2 = 0;
                        ** GOTO lbl101
lbl78:
                        // 1 sources

                        if (!var8_9.contains("unexpected_error") && !var8_9.contains("INTERNAL_SERVER_ERROR") && !var8_9.contains("Something went wrong while processing the query.")) ** GOTO lbl81
                        var0.logger.info("Error on checkout with code: {}", var8_9);
                        ** GOTO lbl6
lbl81:
                        // 1 sources

                        if (var8_9.contains("contract_done")) {
                            return CompletableFuture.completedFuture(true);
                        }
                        var0.logger.info("Failed with code: {}", var8_9);
                        var0.handleFailureWebhooks((String)var8_9, (JsonObject)var5_6);
                        if (var8_9.contains("out_of_stock") || var8_9.contains("Item is no longer in stock.")) ** GOTO lbl101
                        v5 = var0.doPost("Updating billing", (Supplier<HttpRequest>)LambdaMetafactory.metafactory(null, null, null, ()Ljava/lang/Object;, updateTender(), ()Lio/vertx/ext/web/client/HttpRequest;)((API)var0.api), var0.api.tenderUpdateForm(var3_3), 200, new String[]{"{\"data\":{\"updateTenderPlan"});
                        if (!v5.isDone()) {
                            var9_10 = v5;
                            return var9_10.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$checkout(io.trickle.task.sites.walmart.graphql.WalmartNew int int java.lang.String java.util.concurrent.CompletableFuture java.util.function.Function java.lang.String io.vertx.core.json.JsonObject io.vertx.core.json.JsonObject io.vertx.core.json.JsonObject java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((WalmartNew)var0, (int)var1_1, (int)var2_2, (String)var3_3, (CompletableFuture)var9_10, null, (String)var4_4, (JsonObject)var5_6, (JsonObject)var6_7, (JsonObject)var7_8 /* !! */ , (String)var8_9, (int)6));
                        }
                        ** GOTO lbl98
lbl94:
                        // 1 sources

                        if (var6_7.containsKey("already been completed for this contract")) {
                            Analytics.success(var0.task, new JsonObject((String)var4_4), var0.api.proxyString());
                            return CompletableFuture.completedFuture(true);
                        }
                        ** GOTO lbl101
lbl98:
                        // 2 sources

                        while (true) {
                            v5.join();
lbl101:
                            // 7 sources

                            if (!(v6 = VertxUtil.sleep(var0.task.getRetryDelay())).isDone()) {
                                var9_10 = v6;
                                return var9_10.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$checkout(io.trickle.task.sites.walmart.graphql.WalmartNew int int java.lang.String java.util.concurrent.CompletableFuture java.util.function.Function java.lang.String io.vertx.core.json.JsonObject io.vertx.core.json.JsonObject io.vertx.core.json.JsonObject java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((WalmartNew)var0, (int)var1_1, (int)var2_2, (String)var3_3, (CompletableFuture)var9_10, null, null, null, null, null, null, (int)7));
                            }
lbl104:
                            // 3 sources

                            while (true) {
                                v6.join();
                                break;
                            }
                            break;
                        }
                        break;
                    }
                }
                catch (Throwable var4_5) {
                    var2_2 = 0;
                    var0.reset();
                }
                ** GOTO lbl6
                case 1: {
                    v0 = var4_4;
                    ** continue;
                }
                case 2: {
                    v2 = var4_4;
                    var4_4 = var5_6;
                    ** continue;
                }
                case 3: {
                    v3 = var4_4;
                    var4_4 = var5_6;
                    ** continue;
                }
                case 4: {
                    v4 = var4_4;
                    var4_4 = var5_6;
                    ** continue;
                }
                case 5: {
                    v1 = var4_4;
                    ** continue;
                }
                case 6: {
                    v5 = var4_4;
                    v7 = var6_7;
                    v8 = var7_8 /* !! */ ;
                    v9 = var8_9;
                    var8_9 = var10_11;
                    var7_8 /* !! */  = var9_10;
                    var6_7 = v9;
                    var5_6 = v8;
                    var4_4 = v7;
                    ** continue;
                }
                case 7: 
            }
            v6 = var4_4;
            ** while (true)
        }
        var0.reset();
        return CompletableFuture.completedFuture(false);
    }

    public CompletableFuture execute(String string, Function function, Object object, Supplier supplier) {
        return this.execute(string, function, supplier, object);
    }

    public CompletableFuture buyNow() {
        this.logger.info("Waiting for restock");
        int n = 1;
        String string = null;
        int n2 = 0;
        while (this.running) {
            if (n2++ > Integer.MAX_VALUE) return CompletableFuture.completedFuture(null);
            try {
                int n3;
                HttpResponse httpResponse;
                if (this.api.cartId == null || this.api.cartId.isBlank()) {
                    CompletableFuture completableFuture = this.createCart();
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture2 = completableFuture;
                        API aPI = this.api;
                        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$buyNow(this, n, string, n2, aPI, completableFuture2, null, null, 0, 0, null, null, null, null, 1, arg_0));
                    }
                    this.api.cartId = (String)completableFuture.join();
                }
                this.fetchGlobalCookies();
                if (n == 0 && string != null) {
                    CompletableFuture completableFuture = Request.send(this.api.getContract(), this.api.getContractForm(string));
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture3 = completableFuture;
                        return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$buyNow(this, n, string, n2, null, completableFuture3, null, null, 0, 0, null, null, null, null, 2, arg_0));
                    }
                    httpResponse = (HttpResponse)completableFuture.join();
                } else {
                    CompletableFuture completableFuture = Request.send(this.api.buyNow(), n != 0 ? this.api.buyNowPreloadBody() : this.api.buyNowBody());
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$buyNow(this, n, string, n2, null, completableFuture4, null, null, 0, 0, null, null, null, null, 3, arg_0));
                    }
                    httpResponse = (HttpResponse)completableFuture.join();
                }
                if (httpResponse == null) continue;
                String string2 = httpResponse.bodyAsString();
                if (n == 0 && Utils.containsIgnoreCase(string2, "mario")) {
                    if (string != null) {
                        string = null;
                    } else {
                        this.logger.error("Preload error");
                    }
                } else if (httpResponse.statusCode() == 200 && !Utils.containsAnyWords(string2, "no longer in stock", "item unavailable", "expired") && (Utils.containsAllWords(string2, "contract", "created") || Utils.containsAllWords(string2, "contract", "set_payment"))) {
                    if (n == 0) {
                        VertxUtil.sendSignal(this.instanceSignal);
                    }
                    string = Utils.quickParseFirst(string2, ID_PATTERN);
                    int n4 = !string2.contains("\"cvvRequired\":false") ? 1 : 0;
                    int n5 = n3 = !Utils.hasPattern(string2, ADDRESS_PATTERN) ? 1 : 0;
                    if (n4 != 0 || n3 != 0) {
                        if (n3 != 0) {
                            CompletableFuture completableFuture = this.execute("Adding shipping method", WalmartNew.quickFunctionParse(ADDRESS_ID_PATTERN, 200), this.api::saveAddress, this.api.saveAddressJson());
                            if (!completableFuture.isDone()) {
                                CompletableFuture completableFuture5 = completableFuture;
                                API aPI = this.api;
                                return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$buyNow(this, n, string, n2, aPI, completableFuture5, httpResponse, string2, n4, n3, null, null, null, null, 4, arg_0));
                            }
                            this.api.addressId = (String)completableFuture.join();
                            CompletableFuture completableFuture6 = this.doPost("Setting preferred method", this.api::setAddressID, this.api.setAddressIdForm(string, this.api.addressId), 200, "UPDATE_SHIPPING_ADDRESS");
                            CompletableFuture completableFuture7 = this.doPost("Submitting fulfilment", this.api::fulfillment, this.api.fulfillmentBody(), 200, "\"intent\":\"SHIPPING\"");
                            CompletableFuture<Void> completableFuture8 = CompletableFuture.allOf(completableFuture6, completableFuture7);
                            if (!completableFuture8.isDone()) {
                                CompletableFuture<Void> completableFuture9 = completableFuture8;
                                return ((CompletableFuture)completableFuture9.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$buyNow(this, n, string, n2, null, completableFuture6, httpResponse, string2, n4, n3, completableFuture7, completableFuture9, null, null, 5, arg_0));
                            }
                            completableFuture8.join();
                        }
                        if (n4 == 0) continue;
                        CompletableFuture completableFuture = this.submitBilling();
                        if (!completableFuture.isDone()) {
                            CompletableFuture completableFuture10 = completableFuture;
                            return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$buyNow(this, n, string, n2, null, completableFuture10, httpResponse, string2, n4, n3, null, null, null, null, 6, arg_0));
                        }
                        completableFuture.join();
                        if (this.api.paymentId == null || string2.contains("\"payments\":[]") || string2.contains("\"paymentPreferenceId\":\"" + this.api.paymentId + "\"")) continue;
                        CompletableFuture completableFuture11 = this.doPost("Updating payment preference", this.api::setPayment, this.api.setPaymentForm(string), 200, this.api.paymentId);
                        if (!completableFuture11.isDone()) {
                            CompletableFuture completableFuture12 = completableFuture11;
                            return ((CompletableFuture)completableFuture12.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$buyNow(this, n, string, n2, null, completableFuture12, httpResponse, string2, n4, n3, null, null, null, null, 7, arg_0));
                        }
                        completableFuture11.join();
                        n = 0;
                        continue;
                    }
                    if (n != 0) {
                        n = 0;
                        continue;
                    }
                    if (this.priceCheck(string2)) {
                        return CompletableFuture.completedFuture(string2);
                    }
                }
                Object object = Util.parseErrorMessage(string2);
                if (object == null) {
                    object = httpResponse.statusCode() + httpResponse.statusMessage();
                }
                this.logger.warn("Waiting for restock: '{}'", object);
                if (httpResponse.statusCode() == 400) {
                    CompletableFuture completableFuture = VertxUtil.hardCodedSleep(this.task.getRetryDelay());
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture13 = completableFuture;
                        return ((CompletableFuture)completableFuture13.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$buyNow(this, n, string, n2, null, completableFuture13, httpResponse, string2, 0, 0, null, null, (String)object, null, 8, arg_0));
                    }
                    completableFuture.join();
                    continue;
                }
                if (httpResponse.statusCode() >= 307) {
                    CompletableFuture completableFuture = this.api.handleBadResponse(httpResponse, null);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture14 = completableFuture;
                        return ((CompletableFuture)completableFuture14.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$buyNow(this, n, string, n2, null, completableFuture14, httpResponse, string2, 0, 0, null, null, (String)object, null, 9, arg_0));
                    }
                    n3 = ((Boolean)completableFuture.join()).booleanValue() ? 1 : 0;
                    if (n3 != 0) continue;
                    CompletableFuture completableFuture15 = VertxUtil.sleep(this.task.getMonitorDelay());
                    if (!completableFuture15.isDone()) {
                        CompletableFuture completableFuture16 = completableFuture15;
                        return ((CompletableFuture)completableFuture16.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$buyNow(this, n, string, n2, null, completableFuture16, httpResponse, string2, n3, 0, null, null, (String)object, null, 10, arg_0));
                    }
                    completableFuture15.join();
                    continue;
                }
                CompletableFuture completableFuture = VertxUtil.signalSleep(this.instanceSignal, this.task.getMonitorDelay());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture17 = completableFuture;
                    return ((CompletableFuture)completableFuture17.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$buyNow(this, n, string, n2, null, completableFuture17, httpResponse, string2, 0, 0, null, null, (String)object, null, 11, arg_0));
                }
                completableFuture.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error waiting for restock: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = VertxUtil.randomSleep(this.task.getRetryDelay());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture18 = completableFuture;
                    return ((CompletableFuture)completableFuture18.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$buyNow(this, n, string, n2, null, completableFuture18, null, null, 0, 0, null, null, null, throwable, 12, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public static String lambda$run$3(HttpResponse httpResponse) {
        Boolean bl = true;
        if (bl == null) return null;
        if (!httpResponse.bodyAsString().contains("pageProps")) return null;
        return httpResponse.bodyAsString();
    }

    public CompletableFuture doPost(String string, Supplier supplier, Object object, Integer n, String ... stringArray) {
        this.logger.info(string);
        while (this.running) {
            try {
                HttpRequest httpRequest = (HttpRequest)supplier.get();
                CompletableFuture completableFuture = Request.send(httpRequest, object);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$doPost(this, string, (Supplier)supplier, object, n, stringArray, httpRequest, completableFuture2, null, null, 0, null, 0, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse != null) {
                    int n2;
                    String string2;
                    if (httpResponse.statusCode() >= 300 && httpResponse.statusCode() < 400) {
                        string2 = httpResponse.getHeader("location");
                        n2 = stringArray == null || Utils.containsAllWords(string2, stringArray) ? 1 : 0;
                    } else {
                        string2 = httpResponse.bodyAsString();
                        int n3 = n2 = stringArray == null || Utils.containsAllWords(string2, stringArray) ? 1 : 0;
                    }
                    if ((n == null || httpResponse.statusCode() == n.intValue()) && n2 != 0) {
                        return CompletableFuture.completedFuture(string2);
                    }
                    Object object2 = Util.parseErrorMessage(string2);
                    if (object2 == null) {
                        object2 = httpResponse.statusCode() + httpResponse.statusMessage();
                    }
                    this.logger.warn("Failed {}: '{}'", (Object)string.toLowerCase(Locale.ROOT), object2);
                    CompletableFuture completableFuture3 = this.api.handleBadResponse(httpResponse, Request.getURI(httpRequest));
                    if (!completableFuture3.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture3;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$doPost(this, string, (Supplier)supplier, object, n, stringArray, httpRequest, completableFuture4, httpResponse, string2, n2, (String)object2, 0, null, 2, arg_0));
                    }
                    int n4 = ((Boolean)completableFuture3.join()).booleanValue() ? 1 : 0;
                    if (n4 != 0) continue;
                    CompletableFuture completableFuture5 = VertxUtil.sleep(this.task.getMonitorDelay());
                    if (!completableFuture5.isDone()) {
                        CompletableFuture completableFuture6 = completableFuture5;
                        return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$doPost(this, string, (Supplier)supplier, object, n, stringArray, httpRequest, completableFuture6, httpResponse, string2, n2, null, n4, null, 3, arg_0));
                    }
                    completableFuture5.join();
                    continue;
                }
                CompletableFuture completableFuture7 = VertxUtil.randomSleep(this.task.getRetryDelay());
                if (!completableFuture7.isDone()) {
                    CompletableFuture completableFuture8 = completableFuture7;
                    return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$doPost(this, string, (Supplier)supplier, object, n, stringArray, httpRequest, completableFuture8, httpResponse, null, 0, null, 0, null, 4, arg_0));
                }
                completableFuture7.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error {}: {}", (Object)string.toLowerCase(Locale.ROOT), (Object)throwable.getMessage());
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug((Object)throwable);
                }
                CompletableFuture completableFuture = super.randomSleep(this.task.getRetryDelay());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture9 = completableFuture;
                    return ((CompletableFuture)completableFuture9.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$doPost(this, string, (Supplier)supplier, object, n, stringArray, null, completableFuture9, null, null, 0, null, 0, throwable, 5, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture checkout() {
        int n = 0;
        int n2 = 0;
        String string = null;
        while (this.running && n <= 10) {
            try {
                Object object;
                if (n2 == 0) {
                    CompletableFuture completableFuture = this.submitShipping();
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture2 = completableFuture;
                        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$checkout(this, n, n2, string, completableFuture2, null, null, null, null, null, null, 1, arg_0));
                    }
                    completableFuture.join();
                    object = this::lambda$checkout$7;
                    CompletableFuture completableFuture3 = this.execute("Creating contract", (Function)object, this.api::createContract, this.api.createContractBody());
                    if (!completableFuture3.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture3;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$checkout(this, n, n2, string, completableFuture4, (Function)object, null, null, null, null, null, 2, arg_0));
                    }
                    string = (String)completableFuture3.join();
                    this.api.contractId = this.api.cookieStore().getCookieValue("AZ_ST_PC").split("%7C")[0];
                    if (this.api.paymentId == null || !this.api.isLoggedIn()) {
                        CompletableFuture completableFuture5 = this.submitBilling();
                        if (!completableFuture5.isDone()) {
                            CompletableFuture completableFuture6 = completableFuture5;
                            return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$checkout(this, n, n2, string, completableFuture6, (Function)object, null, null, null, null, null, 3, arg_0));
                        }
                        completableFuture5.join();
                    }
                    n2 = 1;
                    CompletableFuture completableFuture7 = this.doPost("Updating billing", this.api::updateTender, this.api.tenderUpdateForm(string), 200, "{\"data\":{\"updateTenderPlan");
                    if (!completableFuture7.isDone()) {
                        CompletableFuture completableFuture8 = completableFuture7;
                        return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$checkout(this, n, n2, string, completableFuture8, (Function)object, null, null, null, null, null, 4, arg_0));
                    }
                    completableFuture7.join();
                    continue;
                }
                CompletableFuture completableFuture = this.doPost("Processing...", this.api::submitPayment, this.api.getPaymentForm(this.token), 200, null);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture9 = completableFuture;
                    return ((CompletableFuture)completableFuture9.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$checkout(this, n, n2, string, completableFuture9, null, null, null, null, null, null, 5, arg_0));
                }
                object = (String)completableFuture.join();
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("processPayment responded with: {}", object);
                }
                if (object != null) {
                    if (this.mode.equals((Object)Mode.DESKTOP) ? DESKTOP_CHECKOUT_FLAG.test((String)object) : MOBILE_CHECKOUT_FLAG.test((String)object)) {
                        VertxUtil.sendSignal(this.instanceSignal);
                        this.logger.info("Successfully checked out!");
                        Analytics.success(this.task, new JsonObject((String)object), this.api.proxyString());
                        return CompletableFuture.completedFuture(true);
                    }
                    if (((String)object).contains("{\"errors\":[{\"message\":\"")) {
                        JsonObject jsonObject = new JsonObject((String)object);
                        JsonObject jsonObject2 = jsonObject.getJsonArray("errors").getJsonObject(0);
                        this.logger.info(jsonObject2.getString("message"));
                        if (jsonObject2.containsKey("extensions")) {
                            JsonObject jsonObject3 = jsonObject2.getJsonObject("extensions");
                            if (jsonObject3.containsKey("code")) {
                                String string2 = jsonObject3.getString("code");
                                if (string2.contains("pc_expired") || string2.contains("Purchase Contract has expired")) {
                                    this.logger.info("Session expiry detected. Resetting!");
                                    n2 = 0;
                                } else {
                                    if (string2.contains("unexpected_error") || string2.contains("INTERNAL_SERVER_ERROR") || string2.contains("Something went wrong while processing the query.")) {
                                        this.logger.info("Error on checkout with code: {}", (Object)string2);
                                        continue;
                                    }
                                    if (string2.contains("contract_done")) {
                                        return CompletableFuture.completedFuture(true);
                                    }
                                    this.logger.info("Failed with code: {}", (Object)string2);
                                    this.handleFailureWebhooks(string2, jsonObject);
                                    if (!string2.contains("out_of_stock") && !string2.contains("Item is no longer in stock.")) {
                                        CompletableFuture completableFuture10 = this.doPost("Updating billing", this.api::updateTender, this.api.tenderUpdateForm(string), 200, "{\"data\":{\"updateTenderPlan");
                                        if (!completableFuture10.isDone()) {
                                            CompletableFuture completableFuture11 = completableFuture10;
                                            return ((CompletableFuture)completableFuture11.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$checkout(this, n, n2, string, completableFuture11, null, (String)object, jsonObject, jsonObject2, jsonObject3, string2, 6, arg_0));
                                        }
                                        completableFuture10.join();
                                    }
                                }
                            }
                        } else if (jsonObject2.containsKey("already been completed for this contract")) {
                            Analytics.success(this.task, new JsonObject((String)object), this.api.proxyString());
                            return CompletableFuture.completedFuture(true);
                        }
                    }
                }
                CompletableFuture completableFuture12 = VertxUtil.sleep(this.task.getRetryDelay());
                if (!completableFuture12.isDone()) {
                    CompletableFuture completableFuture13 = completableFuture12;
                    return ((CompletableFuture)completableFuture13.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$checkout(this, n, n2, string, completableFuture13, null, null, null, null, null, null, 7, arg_0));
                }
                completableFuture12.join();
            }
            catch (Throwable throwable) {
                n2 = 0;
                this.reset();
            }
        }
        this.reset();
        return CompletableFuture.completedFuture(false);
    }

    static {
        GLOBAL_COOKIES = new ConcurrentHashSet();
        CART_ID_PATTERN = Pattern.compile("Cart\".*?\"id\":\"(.*?)\"");
        APPL_VER_PATTERN = Pattern.compile("appVersion\":\"(.*?)\"");
        STORE_ID_PATTERN = Pattern.compile("storeId\":\"([0-9]*)");
        ADDRESS_ID_PATTERN = Pattern.compile("newAddress.*?\"id\":\"(.*?)\"", 32);
        PRODUCTS_HOME_PATTERN = Pattern.compile("canonicalUrl\":\"/ip/(.*?/[0-9]*)");
        TICKET_PATTERN = Pattern.compile("\"url\":\"(.*?)\"");
        REFRESH_TIME_PATTERN = Pattern.compile("\"nextRefreshUnixTimestamp\":([0-9]*)");
        EXPECTED_TIME_PATTERN = Pattern.compile("\"expectedTurnTimeUnixTimestamp\":([0-9]*)");
        PRICE_PATTERN = Pattern.compile("subTotal.*?\"value\":([0-9]*)");
        PAYMENT_ID_PATTERN = Pattern.compile("CreditCard\".*?\"id\":\"(.*?)\"");
        TENDER_PATTERN = Pattern.compile("\"tenderPlanId\":\"(.*?)\"");
        ID_PATTERN = Pattern.compile("\"id\":\"(.*?-.*?-.*?-.*?-.*?)\"");
        ADDRESS_PATTERN = Pattern.compile("\"addressLineOne\":\".*?\"");
        PAYMENT_PREFERENCE_PATTERN = Pattern.compile("\"paymentPreferenceId\":\"(.*?)\"");
        KEYWORD_PID_PATTERN = Pattern.compile("/([0-9][0-9][0-9]*)");
        MOBILE_CHECKOUT_FLAG = WalmartNew::lambda$static$0;
        DESKTOP_CHECKOUT_FLAG = WalmartNew::lambda$static$1;
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$waitForQueue(WalmartNew var0, String var1_1, HttpRequest var2_2, CompletableFuture var3_4, JsonObject var4_5, HttpResponse var5_7, int var6_8, Throwable var7_9, int var8_10, Object var9_11) {
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

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$submitShipping(WalmartNew var0, CompletableFuture var1_1, String var2_2, JsonObject var3_3, JsonObject var4_4, int var5_5, Object var6_6) {
        switch (var5_5) {
            case 0: {
                if (var0.api.addressId != null && !var0.api.addressId.isBlank() && var0.api.accessPointId != null) {
                    if (var0.api.accessPointId.isBlank() == false) return CompletableFuture.completedFuture(true);
                }
                if (!(v0 = var0.doPost("Submitting shipping", (Supplier<HttpRequest>)LambdaMetafactory.metafactory(null, null, null, ()Ljava/lang/Object;, getPCID(), ()Lio/vertx/ext/web/client/HttpRequest;)((API)var0.api), var0.api.PCIDForm(), 200, new String[]{"newAddress"})).isDone()) {
                    var4_4 = v0;
                    return var4_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$submitShipping(io.trickle.task.sites.walmart.graphql.WalmartNew java.util.concurrent.CompletableFuture java.lang.String io.vertx.core.json.JsonObject io.vertx.core.json.JsonObject int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((WalmartNew)var0, (CompletableFuture)var4_4, null, null, null, (int)1));
                }
                ** GOTO lbl14
            }
            case 1: {
                v0 = var1_1;
lbl14:
                // 2 sources

                if ((var2_2 = new JsonObject((String)(var1_1 = (String)v0.join())).getJsonObject("data").getJsonObject("createAccountAddress").getJsonObject("newAddress")).containsKey("id") == false) return CompletableFuture.completedFuture(false);
                var0.api.addressId = var2_2.getString("id");
                if (var2_2.containsKey("accessPoint") == false) return CompletableFuture.failedFuture(new Exception("shipping_update_failed"));
                var3_3 /* !! */  = var2_2.getJsonObject("accessPoint");
                if (var3_3 /* !! */ .containsKey("id") == false) return CompletableFuture.failedFuture(new Exception("shipping_update_failed"));
                var0.api.accessPointId = var3_3 /* !! */ .getString("id");
                v1 = var0.doPost("Submitting fulfilment", (Supplier<HttpRequest>)LambdaMetafactory.metafactory(null, null, null, ()Ljava/lang/Object;, fulfillment(), ()Lio/vertx/ext/web/client/HttpRequest;)((API)var0.api), var0.api.fulfillmentBody(), 200, new String[]{"\"intent\":\"SHIPPING\""});
                if (!v1.isDone()) {
                    var4_4 = v1;
                    return var4_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$submitShipping(io.trickle.task.sites.walmart.graphql.WalmartNew java.util.concurrent.CompletableFuture java.lang.String io.vertx.core.json.JsonObject io.vertx.core.json.JsonObject int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((WalmartNew)var0, (CompletableFuture)var4_4, (String)var1_1, (JsonObject)var2_2, (JsonObject)var3_3 /* !! */ , (int)2));
                }
                ** GOTO lbl35
            }
            case 2: {
                v1 = var1_1;
                v2 = var2_2;
                v3 = var3_3 /* !! */ ;
                var3_3 /* !! */  = var4_4;
                var2_2 = v3;
                var1_1 = v2;
lbl35:
                // 2 sources

                v1.join();
                return CompletableFuture.completedFuture(true);
            }
        }
        throw new IllegalArgumentException();
    }

    public static Function quickFunctionParse(Pattern pattern, Integer n) {
        return arg_0 -> WalmartNew.lambda$quickFunctionParse$2(n, pattern, arg_0);
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$submitBilling(WalmartNew var0, API var1_1, CompletableFuture var2_3, int var3_4, Object var4_6) {
        switch (var3_4) {
            case 0: {
                try {
                    v0 = var0.api;
                    v1 = var0.execute("Submitting billing", WalmartNew.quickFunctionParse(WalmartNew.PAYMENT_ID_PATTERN, 200), (Supplier<HttpRequest>)LambdaMetafactory.metafactory(null, null, null, ()Ljava/lang/Object;, submitBilling(), ()Lio/vertx/ext/web/client/HttpRequest;)((API)var0.api), var0.api.getBillingForm(var0.token));
                    if (!v1.isDone()) {
                        var3_5 = v1;
                        var2_3 = v0;
                        return var3_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$submitBilling(io.trickle.task.sites.walmart.graphql.WalmartNew io.trickle.task.sites.walmart.graphql.API java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((WalmartNew)var0, (API)var2_3, (CompletableFuture)var3_5, (int)1));
                    }
lbl13:
                    // 3 sources

                    while (true) {
                        v0.paymentId = (String)v1.join();
                        if (!var0.api.paymentId.isBlank()) {
                            v2 = true;
                            return CompletableFuture.completedFuture(v2);
                        }
                        v2 = false;
                        return CompletableFuture.completedFuture(v2);
                    }
                }
                catch (Throwable var1_2) {
                    var0.logger.error("Error updating payment id: {}", (Object)var1_2.getMessage());
                    return CompletableFuture.failedFuture(new Exception("payment_update_failed"));
                }
            }
            case 1: {
                v0 = var1_1;
                v1 = var2_3;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$run(WalmartNew var0, CompletableFuture var1_1, int var2_4, Function var3_6, Function var4_7, int var5_11, int var6_13, int var7_15, Object var8_30) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [32[WHILELOOP]], but top level block is 34[UNCONDITIONALDOLOOP]
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

    public static String lambda$updateOrCreateSession$9(HttpResponse httpResponse) {
        if (httpResponse != null && httpResponse.statusCode() == 200) {
            return "";
        }
        if (httpResponse == null) return null;
        if (httpResponse.statusCode() != 442) return null;
        return httpResponse.bodyAsJsonObject().getString("url");
    }

    public static String lambda$createCart$6(HttpResponse httpResponse) {
        String string = httpResponse.bodyAsString();
        return Utils.quickParseFirst(string, CART_ID_PATTERN);
    }

    public CompletableFuture generatePaymentToken() {
        HttpRequest httpRequest = VertxSingleton.INSTANCE.getLocalClient().getClient().getAbs("https://securedataweb.walmart.com/pie/v1/wmcom_us_vtg_pie/getkey.js?bust=" + System.currentTimeMillis()).timeout(TimeUnit.SECONDS.toMillis(15L)).as(BodyCodec.string());
        try {
            CompletableFuture completableFuture = Request.execute(httpRequest, 5);
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$generatePaymentToken(this, httpRequest, completableFuture2, 1, arg_0));
            }
            String string = (String)completableFuture.join();
            this.token = PaymentToken.prepareAndGenerate(string, this.task.getProfile().getCardNumber(), this.task.getProfile().getCvv());
            this.token.set4111Encrypted(PaymentToken.prepareAndGenerate(string, "4111111111111111", this.task.getProfile().getCvv()).getEncryptedCvv());
            return CompletableFuture.completedFuture(null);
        }
        catch (Exception exception) {
            this.logger.warn("Failed to generate payment token: {}", (Object)exception.getMessage());
        }
        return CompletableFuture.completedFuture(null);
    }

    public Boolean lambda$processBuyNow$8(HttpResponse httpResponse) {
        JsonObject jsonObject = httpResponse.bodyAsJsonObject();
        String string = Util.parseErrorMessage(httpResponse.bodyAsString());
        if (string == null) {
            if (!Utils.containsAnyWords(httpResponse.bodyAsString(), "\"OrderInfo\",\"id\":\"", "{\"data\":{\"placeOrder\":{\"id\"")) return null;
            this.logger.info("Successfully checked out [ALT]");
            Analytics.success(this.task, jsonObject, this.api.proxyString());
            return true;
        }
        if (string.contains("pc_expired")) {
            this.logger.info("Checkout session expired. Handling...");
            return false;
        }
        this.logger.error("Alt processing failed [{}]", (Object)string);
        this.handleFailureWebhooks(string, httpResponse.bodyAsJsonObject());
        return null;
    }

    public WalmartNew(Task task, int n) {
        super(n);
        this.task = task;
        this.mode = Mode.getMode(this.task.getMode());
        this.api = this.mode.equals((Object)Mode.DESKTOP) ? new WalmartNewAPI(this.task) : new WalmartNewAPIMobile(this.task);
        super.setClient(this.api);
        this.instanceSignal = this.task.getKeywords()[0];
        this.keepAliveWorker = 0L;
        this.buyNow = task.getMode().contains("alt");
        this.queue = task.getMode().contains("queue");
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$generatePaymentToken(WalmartNew var0, HttpRequest var1_1, CompletableFuture var2_2, int var3_4, Object var4_6) {
        switch (var3_4) {
            case 0: {
                var1_1 = VertxSingleton.INSTANCE.getLocalClient().getClient().getAbs("https://securedataweb.walmart.com/pie/v1/wmcom_us_vtg_pie/getkey.js?bust=" + System.currentTimeMillis()).timeout(TimeUnit.SECONDS.toMillis(15L)).as(BodyCodec.string());
                try {
                    v0 = Request.execute(var1_1, 5);
                    if (!v0.isDone()) {
                        var3_5 = v0;
                        return var3_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$generatePaymentToken(io.trickle.task.sites.walmart.graphql.WalmartNew io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((WalmartNew)var0, (HttpRequest)var1_1, (CompletableFuture)var3_5, (int)1));
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
                var0.token.set4111Encrypted(PaymentToken.prepareAndGenerate((String)var2_2, "4111111111111111", var0.task.getProfile().getCvv()).getEncryptedCvv());
                return CompletableFuture.completedFuture(null);
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public CompletableFuture run() {
        try {
            if (this.mode.equals((Object)Mode.DESKTOP)) {
                if (this.task.getMode().contains("testing")) {
                    this.api.setAPI(PerimeterX.createDesktop(this));
                } else {
                    this.api.setAPI(new DesktopPXAPI3(this));
                }
            } else {
                this.api.setAPI(PerimeterX.createMobile(this));
            }
            CompletableFuture completableFuture = this.generatePaymentToken();
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$run(this, completableFuture2, 0, null, null, 0, 0, 1, arg_0));
            }
            completableFuture.join();
            CompletableFuture completableFuture3 = this.api.initialisePX();
            if (!completableFuture3.isDone()) {
                CompletableFuture completableFuture4 = completableFuture3;
                return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$run(this, completableFuture4, 0, null, null, 0, 0, 2, arg_0));
            }
            int n = ((Boolean)completableFuture3.join()).booleanValue() ? 1 : 0;
            if (n == 0) {
                this.logger.warn("Failed to initialise and configure task. Stopping...");
                return CompletableFuture.completedFuture(null);
            }
            Function<HttpResponse, String> function = WalmartNew::lambda$run$3;
            Function<HttpResponse, String> function2 = this::lambda$run$4;
            try {
                if (this.mode == Mode.DESKTOP) {
                    CompletableFuture completableFuture5 = this.api.generatePX(false);
                    if (!completableFuture5.isDone()) {
                        CompletableFuture completableFuture6 = completableFuture5;
                        return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$run(this, completableFuture6, n, function, function2, 0, 0, 3, arg_0));
                    }
                    completableFuture5.join();
                    CompletableFuture completableFuture7 = this.execute("Checking homepage", function, this.api::homepage, 200);
                    if (!completableFuture7.isDone()) {
                        CompletableFuture completableFuture8 = completableFuture7;
                        return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$run(this, completableFuture8, n, function, function2, 0, 0, 4, arg_0));
                    }
                    String string = (String)completableFuture7.join();
                    this.api.platformVersion = Utils.quickParseFirst(string, APPL_VER_PATTERN);
                    this.api.storeID = Integer.valueOf(Utils.quickParseFirst(string, STORE_ID_PATTERN));
                    List list = Utils.quickParseAll(string, PRODUCTS_HOME_PATTERN);
                    this.api.productReferer = "https://www.walmart.com/" + (String)list.get(ThreadLocalRandom.current().nextInt(list.size()));
                    if (this.api.platformVersion == null) {
                        this.api.platformVersion = "main-110-891adc";
                    }
                    if (this.api.storeID == null) {
                        this.api.storeID = 5880;
                    }
                } else {
                    this.api.cookieStore().put("WLM", "1", ".walmart.com");
                    CompletableFuture completableFuture9 = this.execute("Checking homepage", function2, ((WalmartNewAPIMobile)this.api).homepageJson(), this.api::homepage);
                    if (!completableFuture9.isDone()) {
                        CompletableFuture completableFuture10 = completableFuture9;
                        return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$run(this, completableFuture10, n, function, function2, 0, 0, 5, arg_0));
                    }
                    completableFuture9.join();
                }
            }
            catch (Throwable throwable) {
                this.logger.warn("Failed to parse session vars: {}", (Object)throwable.getMessage());
            }
            if (this.keepAliveWorker == 0L) {
                this.keepAliveWorker = this.vertx.setPeriodic(TimeUnit.SECONDS.toMillis(100L), this::lambda$run$5);
            }
            CompletableFuture completableFuture11 = this.sleep(ThreadLocalRandom.current().nextInt(300, 1500));
            if (!completableFuture11.isDone()) {
                CompletableFuture completableFuture12 = completableFuture11;
                return ((CompletableFuture)completableFuture12.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$run(this, completableFuture12, n, function, function2, 0, 0, 6, arg_0));
            }
            completableFuture11.join();
            if (this.task.getMode().contains("login")) {
                CompletableFuture completableFuture13 = this.createAccount(false);
                if (!completableFuture13.isDone()) {
                    CompletableFuture completableFuture14 = completableFuture13;
                    return ((CompletableFuture)completableFuture14.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$run(this, completableFuture14, n, function, function2, 0, 0, 7, arg_0));
                }
                completableFuture13.join();
            } else if (this.task.getMode().contains("account")) {
                CompletableFuture completableFuture15 = this.loginAccount();
                if (!completableFuture15.isDone()) {
                    CompletableFuture completableFuture16 = completableFuture15;
                    return ((CompletableFuture)completableFuture16.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$run(this, completableFuture16, n, function, function2, 0, 0, 8, arg_0));
                }
                int n2 = (Integer)completableFuture15.join();
                if (n2 < 1) {
                    this.logger.info("No accounts available in storage. Creating new...");
                    CompletableFuture completableFuture17 = this.createAccount(true);
                    if (!completableFuture17.isDone()) {
                        CompletableFuture completableFuture18 = completableFuture17;
                        return ((CompletableFuture)completableFuture18.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$run(this, completableFuture18, n, function, function2, n2, 0, 9, arg_0));
                    }
                    completableFuture17.join();
                }
            }
            int n3 = 0;
            int n4 = 0;
            while (this.running) {
                if (this.sessionTimestamp == 0L || System.currentTimeMillis() - this.sessionTimestamp >= (long)ThreadLocalRandom.current().nextInt(120000, 300000)) {
                    CompletableFuture completableFuture19 = this.updateOrCreateSession();
                    if (!completableFuture19.isDone()) {
                        CompletableFuture completableFuture20 = completableFuture19;
                        return ((CompletableFuture)completableFuture20.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$run(this, completableFuture20, n, function, function2, n3, n4, 10, arg_0));
                    }
                    completableFuture19.join();
                }
                if (this.api.isLoggedIn() && this.queue && n4 == 0) {
                    if (this.task.getKeywords().length <= 2) {
                        this.logger.error("You need to include the item link in your keyword for queue mode.");
                        continue;
                    }
                    CompletableFuture completableFuture21 = this.queue();
                    if (!completableFuture21.isDone()) {
                        CompletableFuture completableFuture22 = completableFuture21;
                        return ((CompletableFuture)completableFuture22.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$run(this, completableFuture22, n, function, function2, n3, n4, 11, arg_0));
                    }
                    n4 = ((Boolean)completableFuture21.join()).booleanValue() ? 1 : 0;
                }
                if (n3 == 0) {
                    if (this.api.isLoggedIn() && this.buyNow) {
                        CompletableFuture completableFuture23 = this.checkoutBuyNow();
                        if (!completableFuture23.isDone()) {
                            CompletableFuture completableFuture24 = completableFuture23;
                            return ((CompletableFuture)completableFuture24.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$run(this, completableFuture24, n, function, function2, n3, n4, 12, arg_0));
                        }
                        boolean bl = (Boolean)completableFuture23.join();
                        if (!bl) continue;
                        return CompletableFuture.completedFuture(null);
                    }
                    CompletableFuture completableFuture25 = this.addToCart();
                    if (!completableFuture25.isDone()) {
                        CompletableFuture completableFuture26 = completableFuture25;
                        return ((CompletableFuture)completableFuture26.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$run(this, completableFuture26, n, function, function2, n3, n4, 13, arg_0));
                    }
                    n3 = ((Boolean)completableFuture25.join()).booleanValue() ? 1 : 0;
                    continue;
                }
                try {
                    CompletableFuture completableFuture27 = this.checkout();
                    if (!completableFuture27.isDone()) {
                        CompletableFuture completableFuture28 = completableFuture27;
                        return ((CompletableFuture)completableFuture28.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNew.async$run(this, completableFuture28, n, function, function2, n3, n4, 14, arg_0));
                    }
                    boolean bl = (Boolean)completableFuture27.join();
                    if (!bl) continue;
                    break;
                }
                catch (Throwable throwable) {
                }
            }
            if (this.keepAliveWorker == 0L) return CompletableFuture.completedFuture(null);
            this.vertx.cancelTimer(this.keepAliveWorker);
            return CompletableFuture.completedFuture(null);
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return CompletableFuture.completedFuture(null);
    }
}

