/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.netty.handler.codec.http.cookie.Cookie
 *  io.vertx.core.AsyncResult
 *  io.vertx.core.MultiMap
 *  io.vertx.core.buffer.Buffer
 *  io.vertx.core.eventbus.Message
 *  io.vertx.core.json.JsonArray
 *  io.vertx.core.json.JsonObject
 *  io.vertx.core.shareddata.Lock
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.client.HttpResponse
 *  io.vertx.ext.web.codec.BodyCodec
 *  io.vertx.ext.web.multipart.MultipartForm
 */
package io.trickle.task.sites.yeezy;

import io.netty.handler.codec.http.cookie.Cookie;
import io.trickle.core.Controller;
import io.trickle.core.Engine;
import io.trickle.core.VertxSingleton;
import io.trickle.core.actor.TaskActor;
import io.trickle.harvester.CaptchaToken;
import io.trickle.harvester.SolveFuture;
import io.trickle.harvester.TokenController;
import io.trickle.harvester.pooled.AbstractSharedHarvesterController;
import io.trickle.harvester.pooled.AutosolveHarvesterController;
import io.trickle.harvester.pooled.BrowserHarvesterController;
import io.trickle.harvester.pooled.SharedCaptchaToken;
import io.trickle.profile.Profile;
import io.trickle.task.Task;
import io.trickle.task.antibot.impl.akamai.GaneshAPI;
import io.trickle.task.antibot.impl.akamai.HawkAPI;
import io.trickle.task.antibot.impl.akamai.pixel.Pixel;
import io.trickle.task.sites.yeezy.YeezyAPI;
import io.trickle.task.sites.yeezy.util.Adyen12;
import io.trickle.task.sites.yeezy.util.CodeScreen;
import io.trickle.task.sites.yeezy.util.RT;
import io.trickle.task.sites.yeezy.util.Sizes;
import io.trickle.task.sites.yeezy.util.Sizes$NoAvailableSizeException;
import io.trickle.task.sites.yeezy.util.Utag;
import io.trickle.task.sites.yeezy.util.Window3DS2;
import io.trickle.task.sites.yeezy.util.profile.ZipCodeGroup;
import io.trickle.util.Storage;
import io.trickle.util.Utils;
import io.trickle.util.analytics.Analytics;
import io.trickle.util.concurrent.ContextCompletableFuture;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.trickle.webclient.CookieJar;
import io.vertx.core.AsyncResult;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.Lock;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.ext.web.multipart.MultipartForm;
import java.lang.invoke.LambdaMetafactory;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Yeezy
extends TaskActor {
    public static String V3_COOKIE_NAME;
    public boolean isPassedFW = true;
    public boolean isFirstIteration = true;
    public static int successfulCartCount;
    public AbstractSharedHarvesterController harvesters;
    public int sensorPosts = 0;
    public static Pattern SIGNUP_ID_PATTERN;
    public Profile instanceProfile;
    public String authorization;
    public YeezyAPI api;
    public String instanceSignal;
    public String basketID;
    public ZipCodeGroup profilesOfZipCode;
    public static AtomicLong clock;
    public AtomicReference<SharedCaptchaToken> tokenRef;
    public boolean hasPostedSensorFully = false;
    public static Pattern SENSOR_URL_PATTERN;
    public static Pattern POW_PAGE_PATTERN;
    public String harvesterID = null;
    public static int QUEUE_POOL_INTERVAL;
    public static AtomicReference<CaptchaToken> holder;
    public boolean visitedBaskets = false;
    public boolean shouldRotate = false;
    public String orderID;
    public int previousResponseLen = 0;
    public Task task;
    public boolean isSent = false;
    public long pixelPostTs;
    public List<JsonObject> availableSizes = new ArrayList<JsonObject>();
    public static int RETRY_BOUND;
    public static AbstractSharedHarvesterController browserHarvesters;
    public boolean isBannedOnShipping = false;
    public int previousResponseHash = 0;
    public static AbstractSharedHarvesterController autoSolveHarvesters;
    public boolean isTurboQueue;

    public Profile getOrLoadProfile() {
        try {
            if (!this.shouldRotate) return this.task.getProfile();
            if (!this.smartSwapEnabled()) return this.task.getProfile();
            this.shouldRotate = false;
            if (Objects.equals(this.instanceSignal, this.task.getKeywords()[0])) {
                Optional optional = Task.ysProfileMap.get(this.instanceSignal, this.task.getSize());
                if (!optional.isPresent()) return this.task.getProfile();
                this.profilesOfZipCode = (ZipCodeGroup)optional.get();
                return this.profilesOfZipCode.getProfile();
            }
            Optional optional = Task.ysProfileMap.getAnySku(this.task.getSize());
            if (!optional.isPresent()) return this.task.getProfile();
            this.profilesOfZipCode = (ZipCodeGroup)optional.get();
            return this.profilesOfZipCode.getProfile();
        }
        catch (Throwable throwable) {
            this.netLogError("Failed to load instance swap profile message='" + throwable.getMessage() + "'");
            this.shouldRotate = false;
            return this.task.getProfile();
        }
    }

    public HttpRequest lambda$handlePOW$2() {
        return this.api.postSensor(true);
    }

    public CompletableFuture processPayment(HttpRequest httpRequest, String string) {
        int n = 0;
        while (n < 250) {
            this.checkHmac();
            try {
                this.api.updateUtagUrl("https://www.yeezysupply.com/payment");
                this.api.getCookies().put("utag_main", this.api.fetchUtag(), ".yeezysupply.com");
                String string2 = Adyen12.getCSEToken(this.instanceProfile);
                if (string2 == null || string2.isBlank()) {
                    this.logger.warn("Crypt error. Retrying...");
                    CompletableFuture completableFuture = VertxUtil.sleep(300L);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture2 = completableFuture;
                        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$processPayment(this, httpRequest, string, n, string2, completableFuture2, null, null, null, null, null, null, 1, arg_0));
                    }
                    completableFuture.join();
                    continue;
                }
                String string3 = "{\"basketId\":\"" + string + "\",\"encryptedInstrument\":\"" + string2 + "\",\"paymentInstrument\":{\"holder\":\"" + this.instanceProfile.getFirstName().substring(0, 1).toUpperCase() + this.instanceProfile.getFirstName().substring(1).toLowerCase() + " " + this.instanceProfile.getLastName().substring(0, 1).toUpperCase() + this.instanceProfile.getLastName().substring(1).toLowerCase() + "\",\"expirationMonth\":" + (this.instanceProfile.getExpiryMonth().charAt(0) == '0' ? this.instanceProfile.getExpiryMonth().substring(1) : this.instanceProfile.getExpiryMonth()) + ",\"expirationYear\":" + this.instanceProfile.getExpiryYear() + ",\"lastFour\":\"" + this.instanceProfile.getCardNumber().substring(12) + "\",\"paymentMethodId\":\"CREDIT_CARD\",\"cardType\":\"" + this.instanceProfile.getPaymentMethod().get().replace("MASTERCARD", "MASTER") + "\"},\"fingerprint\":\"ryEGX8eZpJ0030000000000000bsx09CX6tD0119707416cVB94iKzBGa0zW3qWYPi5S16Goh5Mk004NMWer10CgS00000YVxEr00000F2e2nkLQB0DGiMVSwn2S:40\"}";
                CompletableFuture completableFuture = Request.send(httpRequest, Buffer.buffer((String)string3));
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture3 = completableFuture;
                    return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$processPayment(this, httpRequest, string, n, string2, completableFuture3, string3, null, null, null, null, null, 2, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse != null) {
                    this.logger.info("Processing payment [{}]", (Object)httpResponse.statusCode());
                    if (httpResponse.body() != null) {
                        Object object;
                        String string4;
                        if (httpResponse.statusCode() != 201) {
                            string4 = ((String)httpResponse.body()).replace("\n", "");
                            this.logger.info(string4);
                            if (string4.contains("HTTP 403 - Forbidden")) {
                                this.logger.warn("Failed to process payment: state='{}' - status={}", (Object)"Blocked by 403 body", (Object)httpResponse.statusCode());
                                this.netLogWarn("Failed to process payment: state='Blocked by 403 body' status=" + httpResponse.statusCode());
                            } else if (string4.length() == 0) {
                                this.logger.warn("Failed to process payment: state='{}' - status={}", (Object)"Blocked by FW", (Object)httpResponse.statusCode());
                                this.netLogWarn("Failed to process payment: state='Blocked by FW' status=" + httpResponse.statusCode());
                            } else if (httpResponse.statusCode() == 403) {
                                this.logger.warn("Failed to process payment: state='{}' - status={}", (Object)"Bad cookie", (Object)httpResponse.statusCode());
                                this.netLogWarn("Failed to process payment: state='Bad cookie' status=" + httpResponse.statusCode());
                            } else if (string4.contains("confirm.error.paymentdeclined.fraud") || string4.contains("hook_status_exception")) {
                                this.handleFailureWebhooks("Fraud", (String)httpResponse.body());
                                this.logger.warn("Failed to process payment: state='{}' - status={}", (Object)"Fraud Decline", (Object)httpResponse.statusCode());
                                this.netLogWarn("Failed to process payment: state='Fraud Decline' status=" + httpResponse.statusCode());
                                ++n;
                            } else {
                                if (string4.contains("basket_not_found_exception") || string4.contains("{\"invalidFields\":[\"Product items\"]")) {
                                    if (string4.contains("basket_not_found_exception")) {
                                        this.logger.warn("Failed to process payment: state='{}' - status={}", (Object)"Cart expiry", (Object)httpResponse.statusCode());
                                        this.netLogWarn("Failed to process payment: state='Cart expiry' status=" + httpResponse.statusCode());
                                    } else {
                                        this.logger.warn("Failed to process payment: state='{}' - status={}", (Object)"Cart jacked", (Object)httpResponse.statusCode());
                                        this.netLogWarn("Failed to process payment: state='Cart Jacked' status=" + httpResponse.statusCode());
                                    }
                                    CompletableFuture completableFuture4 = this.prepareCart();
                                    if (!completableFuture4.isDone()) {
                                        CompletableFuture completableFuture5 = completableFuture4;
                                        return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$processPayment(this, httpRequest, string, n, string2, completableFuture5, string3, httpResponse, string4, null, null, null, 3, arg_0));
                                    }
                                    completableFuture4.join();
                                    CompletableFuture completableFuture6 = this.cart();
                                    if (!completableFuture6.isDone()) {
                                        CompletableFuture completableFuture7 = completableFuture6;
                                        return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$processPayment(this, httpRequest, string, n, string2, completableFuture7, string3, httpResponse, string4, null, null, null, 4, arg_0));
                                    }
                                    completableFuture6.join();
                                    this.logger.info("Recovered cart jack");
                                    this.netLogInfo("Recovered cart jack");
                                    Analytics.log("Recovered cart jack", this.task, this.api.getCookies().getCookieValueLength("_abck"), this.sensorPosts, this.visitedBaskets, this.api.userAgent);
                                    CompletableFuture completableFuture8 = this.checkout();
                                    if (!completableFuture8.isDone()) {
                                        CompletableFuture completableFuture9 = completableFuture8;
                                        return ((CompletableFuture)completableFuture9.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$processPayment(this, httpRequest, string, n, string2, completableFuture9, string3, httpResponse, string4, null, null, null, 5, arg_0));
                                    }
                                    completableFuture8.join();
                                    return CompletableFuture.completedFuture(null);
                                }
                                if (string4.contains("confirm.error.paymentdeclined.not_enough_balance")) {
                                    this.handleFailureWebhooks("Card Decline (balance)", (String)httpResponse.body());
                                    this.logger.warn("Failed to process payment: state='{}' - status={}", (Object)"Card Decline (balance)", (Object)httpResponse.statusCode());
                                    this.netLogWarn("Failed to process payment: state='Card Decline (balance)' status=" + httpResponse.statusCode());
                                    ++n;
                                } else if (string4.contains("paymentdeclined")) {
                                    this.logger.warn("Failed to process payment: state='{}' - status={}", (Object)"Card Decline", (Object)httpResponse.statusCode());
                                    this.netLogWarn("Failed to process payment: state='Card Decline' status=" + httpResponse.statusCode());
                                    this.handleFailureWebhooks("Card Decline", (String)httpResponse.body());
                                    ++n;
                                } else if (string4.contains("missing properties")) {
                                    this.logger.warn("Failed to process payment: state='{}' - status={}", (Object)"Invalid shipping or billing", (Object)httpResponse.statusCode());
                                    this.netLogWarn("Failed to process payment: state='Invalid shipping or billing' status=" + httpResponse.statusCode());
                                    this.handleFailureWebhooks("Invalid shipping or billing", (String)httpResponse.body());
                                    ++n;
                                } else if (string4.contains("<H1>Invalid URL</H1>")) {
                                    this.logger.warn("Failed to process payment: state='{}'", (Object)"Blocked by 400 (bad session)");
                                    this.netLogWarn("Failed to process payment: state='Blocked by 400 (bad session)'");
                                    this.handleFailureWebhooks("Bad session", (String)httpResponse.body());
                                } else if (httpResponse.statusCode() == 200 && httpResponse.getHeader("authorization") == null) {
                                    this.logger.warn("Fake processing (animal)");
                                    this.netLogWarn("Fake processing (animal)");
                                    Analytics.log("Fake processing (animal)", this.task, this.api.getCookies().getCookieValueLength("_abck"), this.sensorPosts, this.visitedBaskets, this.api.userAgent);
                                } else if (!string4.contains("Product item not available") && !string4.contains("Basket has been removed")) {
                                    object = Utils.parseSafe((String)httpResponse.body(), "message", Yeezy::lambda$processPayment$7);
                                    this.logger.warn("Failed to process payment: state='{}' - r={}", object, (Object)string4);
                                    this.netLogWarn("Failed to process payment: state='" + (String)object + "' body='" + string4 + "'");
                                    this.handleFailureWebhooks((String)object, (String)httpResponse.body());
                                    ++n;
                                } else if (httpResponse.statusCode() >= 500) {
                                    this.logger.warn("Failed to process payment: state='{}' - status={}", (Object)"Site down", (Object)httpResponse.statusCode());
                                    this.netLogWarn("Failed to process payment: state='Site down' status=" + httpResponse.statusCode());
                                } else if (string4.contains("invalidFields\":[\"Product items\"")) {
                                    this.handleFailureWebhooks("Out of stock", (String)httpResponse.body());
                                    this.logger.warn("Failed to process payment: state='{}' - status={}", (Object)"Out of stock", (Object)httpResponse.statusCode());
                                    this.netLogWarn("Failed to process payment: state='Out of stock' status=" + httpResponse.statusCode());
                                    ++n;
                                } else if (string4.contains("<H1>Invalid URL</H1>")) {
                                    this.logger.warn("Failed to process payment: state='{}' - status={}", (Object)"URL Block (cookies)", (Object)httpResponse.statusCode());
                                    this.netLogWarn("Failed to process payment: state='URL Block (cookies)' status=" + httpResponse.statusCode());
                                } else {
                                    this.logger.warn("Failed to process payment: state='{}' - status={}", (Object)"Unknown Error", (Object)httpResponse.statusCode());
                                    this.netLogWarn("Failed to process payment: state='Unknown Error' status=" + httpResponse.statusCode());
                                }
                            }
                        } else {
                            string4 = new JsonObject((String)httpResponse.body());
                            if (string4.containsKey("orderToken") && !string4.getString("orderToken").isEmpty()) {
                                this.logger.info("Successfully checked out!: r={}", httpResponse.body());
                                this.netLogInfo("Successfully checked out!: body='" + (String)httpResponse.body() + "'");
                                Analytics.success(this.task, new JsonObject((String)httpResponse.body()), this.api.proxyString());
                                Analytics.log("Successfully Checked Out", this.task, this.api.getCookies().getCookieValueLength("_abck"), this.sensorPosts, this.visitedBaskets, this.api.userAgent);
                                return CompletableFuture.completedFuture(httpResponse.bodyAsString());
                            }
                            if (string4.getString("paymentStatus").equalsIgnoreCase("not_paid") && string4.getString("authorizationType").contains("3ds")) {
                                this.logger.info("Payment authentication required: r={}", httpResponse.body());
                                this.netLogInfo("Payment authentication required: body='" + (String)httpResponse.body() + "'");
                                CompletableFuture completableFuture10 = this.get3DS2Page((JsonObject)string4);
                                if (!completableFuture10.isDone()) {
                                    CompletableFuture completableFuture11 = completableFuture10;
                                    return ((CompletableFuture)completableFuture11.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$processPayment(this, httpRequest, string, n, string2, completableFuture11, string3, httpResponse, null, (JsonObject)string4, null, null, 6, arg_0));
                                }
                                object = (Window3DS2)completableFuture10.join();
                                if (object != null) {
                                    Analytics.warning("Starting 3DS Processing!", this.task);
                                    this.logger.info("Awaiting confirmation...");
                                    CompletableFuture completableFuture12 = ((Window3DS2)object).invoke();
                                    if (!completableFuture12.isDone()) {
                                        CompletableFuture completableFuture13 = completableFuture12;
                                        return ((CompletableFuture)completableFuture13.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$processPayment(this, httpRequest, string, n, string2, completableFuture13, string3, httpResponse, null, (JsonObject)string4, (Window3DS2)object, null, 7, arg_0));
                                    }
                                    completableFuture12.join();
                                    Analytics.warning("3DS Instance Closed. Proceeding?", this.task);
                                    CompletableFuture completableFuture14 = this.handleAfter3DS((Window3DS2)object);
                                    if (!completableFuture14.isDone()) {
                                        CompletableFuture completableFuture15 = completableFuture14;
                                        return ((CompletableFuture)completableFuture15.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$processPayment(this, httpRequest, string, n, string2, completableFuture15, string3, httpResponse, null, (JsonObject)string4, (Window3DS2)object, null, 8, arg_0));
                                    }
                                    boolean bl = (Boolean)completableFuture14.join();
                                    if (bl) {
                                        return CompletableFuture.completedFuture("");
                                    }
                                }
                            } else {
                                this.logger.info("Successfully checked out!");
                                this.netLogInfo("Successfully checked out!: body='" + (String)httpResponse.body() + "'");
                                Analytics.success(this.task, new JsonObject((String)httpResponse.body()), this.api.proxyString());
                                Analytics.log("Successfully Checked Out", this.task, this.api.getCookies().getCookieValueLength("_abck"), this.sensorPosts, this.visitedBaskets, this.api.userAgent);
                                return CompletableFuture.completedFuture(httpResponse.bodyAsString());
                            }
                        }
                    }
                }
                CompletableFuture completableFuture16 = VertxUtil.randomSleep(this.task.getRetryDelay());
                if (!completableFuture16.isDone()) {
                    CompletableFuture completableFuture17 = completableFuture16;
                    return ((CompletableFuture)completableFuture17.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$processPayment(this, httpRequest, string, n, string2, completableFuture17, string3, httpResponse, null, null, null, null, 9, arg_0));
                }
                completableFuture16.join();
                if (httpResponse != null && httpResponse.statusCode() == 403) {
                    CompletableFuture completableFuture18 = this.prepareCart();
                    if (!completableFuture18.isDone()) {
                        CompletableFuture completableFuture19 = completableFuture18;
                        return ((CompletableFuture)completableFuture19.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$processPayment(this, httpRequest, string, n, string2, completableFuture19, string3, httpResponse, null, null, null, null, 10, arg_0));
                    }
                    completableFuture18.join();
                    continue;
                }
                CompletableFuture completableFuture20 = this.postSensors();
                if (!completableFuture20.isDone()) {
                    CompletableFuture completableFuture21 = completableFuture20;
                    return ((CompletableFuture)completableFuture21.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$processPayment(this, httpRequest, string, n, string2, completableFuture21, string3, httpResponse, null, null, null, null, 11, arg_0));
                }
                completableFuture20.join();
            }
            catch (Exception exception) {
                this.logger.error("Error occurred on processing payment: '{}'", (Object)exception.getMessage());
                this.netLogError("Error occurred on processing payment: message=" + exception.getMessage());
                CompletableFuture completableFuture = VertxUtil.randomSleep(3000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture22 = completableFuture;
                    return ((CompletableFuture)completableFuture22.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$processPayment(this, httpRequest, string, n, null, completableFuture22, null, null, null, null, null, exception, 12, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.failedFuture(new Exception("Failed to process payment"));
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$cart(Yeezy var0, int var1_1, CompletableFuture var2_3, Buffer var3_4, HttpRequest var4_6, HttpResponse var5_7, String var6_8, int var7_9, Object var8_15) {
        switch (var7_9) {
            case 0: {
                var0.logger.info("Proceeding to cart...");
                try {
                    var0.logger.info("Using available sizes for sku={} : sizes={}", (Object)var0.api.getSKU(), var0.availableSizes);
                }
                catch (Throwable var1_2) {
                    // empty catch block
                }
                var1_1 = 0;
                var0.netLogInfo("Adding to cart");
                block12: while (var0.running != false) {
                    if (var0.instanceSignal.equals(var0.api.getSKU())) ** GOTO lbl22
                    var0.instanceSignal = var0.api.getSKU();
                    var0.logger.info("Fetching updated item {}", (Object)var0.instanceSignal);
                    v0 = var0.fillSizes();
                    if (!v0.isDone()) {
                        var7_10 = v0;
                        return var7_10.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$cart(io.trickle.task.sites.yeezy.Yeezy int java.util.concurrent.CompletableFuture io.vertx.core.buffer.Buffer io.vertx.ext.web.client.HttpRequest io.vertx.ext.web.client.HttpResponse java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (int)var1_1, (CompletableFuture)var7_10, null, null, null, null, (int)1));
                    }
lbl19:
                    // 3 sources

                    while (true) {
                        v0.join();
lbl22:
                        // 2 sources

                        var2_3 = var0.atcForm();
                        var0.checkHmac();
                        var0.logger.info("Adding to cart");
                        try {
                            var3_4 = var0.api.addToCart();
                            v1 = Request.send((HttpRequest)var3_4, (Buffer)var2_3);
                            if (!v1.isDone()) {
                                var7_11 = v1;
                                return var7_11.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$cart(io.trickle.task.sites.yeezy.Yeezy int java.util.concurrent.CompletableFuture io.vertx.core.buffer.Buffer io.vertx.ext.web.client.HttpRequest io.vertx.ext.web.client.HttpResponse java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (int)var1_1, (CompletableFuture)var7_11, (Buffer)var2_3, (HttpRequest)var3_4, null, null, (int)2));
                            }
lbl31:
                            // 3 sources

                            while (true) {
                                var4_6 /* !! */  = (HttpResponse)v1.join();
                                if (var4_6 /* !! */  != null) {
                                    var0.logger.info("ATC Responded with status={}", (Object)var4_6 /* !! */ .statusCode());
                                    if (var4_6 /* !! */ .statusCode() == 403) {
                                        var0.isPassedFW = false;
                                        var1_1 = 0;
                                        var0.logger.warn("FW Blocked on ATC");
                                        var0.netLogWarn("Failed to ATC: state='FW' status=" + var4_6 /* !! */ .statusCode());
                                        Analytics.log("ATC FW Blocked", var0.task, new Object[]{var0.api.getCookies().getCookieValueLength("_abck"), var0.sensorPosts, var0.visitedBaskets, var0.api.userAgent});
                                    } else {
                                        var0.isPassedFW = true;
                                        var5_7 = ((String)var4_6 /* !! */ .body()).replace("\n", "");
                                        var1_1 = 1;
                                        if (var5_7.contains("branding_url_content")) {
                                            v2 = var0.handlePOW((String)var5_7);
                                            if (!v2.isDone()) {
                                                var7_12 = v2;
                                                return var7_12.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$cart(io.trickle.task.sites.yeezy.Yeezy int java.util.concurrent.CompletableFuture io.vertx.core.buffer.Buffer io.vertx.ext.web.client.HttpRequest io.vertx.ext.web.client.HttpResponse java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (int)var1_1, (CompletableFuture)var7_12, (Buffer)var2_3, (HttpRequest)var3_4, (HttpResponse)var4_6 /* !! */ , (String)var5_7, (int)3));
                                            }
lbl50:
                                            // 3 sources

                                            while (true) {
                                                v2.join();
                                                continue block12;
                                                break;
                                            }
                                        }
                                        if (var5_7.contains("basketId")) {
                                            var0.authorization = var4_6 /* !! */ .getHeader("authorization");
                                            if (var0.authorization == null) {
                                                var0.logger.error("Fake cart (animal)");
                                                var0.netLogWarn("Fake cart (animal) body='" + (String)var5_7 + "'");
                                                Analytics.log("Fake cart (animal)", var0.task, new Object[]{var0.api.getCookies().getCookieValueLength("_abck"), var0.sensorPosts, var0.visitedBaskets, var0.api.userAgent});
                                            } else {
                                                if (!var5_7.contains("\"total\":0") || var0.task.getMode().contains("noqueue")) {
                                                    var0.logger.info("ATC SUCCESSFUL count={} time={}: {}", (Object)Yeezy.successfulCartCount++, (Object)(System.currentTimeMillis() - var0.pixelPostTs), var5_7);
                                                    var0.basketID = new JsonObject((String)var5_7).getString("basketId");
                                                    var0.netLogInfo("ATC SUCCESSFUL body='" + (String)var5_7 + "'");
                                                    Analytics.log("ATC OK", var0.task, new Object[]{var0.api.getCookies().getCookieValueLength("_abck"), var0.sensorPosts, var0.visitedBaskets, var0.api.userAgent});
                                                    if (var0.tokenRef.get() == null) return CompletableFuture.completedFuture(null);
                                                    var6_8 = var0.tokenRef.get();
                                                    var6_8.markPassed();
                                                    var0.tokenRef.set(null);
                                                    return CompletableFuture.completedFuture(null);
                                                }
                                                var0.logger.info("EMPTY_CART (OOS): {}", var5_7);
                                                var0.netLogInfo("EMPTY_CART (OOS): body='" + (String)var5_7 + "'");
                                            }
                                        } else if (var5_7.length() == 0) {
                                            var0.logger.warn("Failed to ATC: state={} status={}", (Object)"Blocked by FW", (Object)var4_6 /* !! */ .statusCode());
                                            var0.netLogWarn("Failed to ATC: state='FW Blocked' status=" + var4_6 /* !! */ .statusCode());
                                        } else if (var5_7.contains("HTTP 403 - Forbidden")) {
                                            var1_1 = 0;
                                            var0.logger.warn("Failed to ATC: state={} status={}", (Object)"Blocked by 403 body", (Object)var4_6 /* !! */ .statusCode());
                                            var0.netLogWarn("Failed to ATC: state='Blocked by 403 body' status=" + var4_6 /* !! */ .statusCode());
                                        } else if (var4_6 /* !! */ .statusCode() == 400) {
                                            var0.logger.warn("Failed to ATC: state={} status={}", (Object)"Blocked by 400 (bad session)", (Object)var4_6 /* !! */ .statusCode());
                                            var0.netLogWarn("Failed to ATC: state='Blocked by 400 (bad session)' status=" + var4_6 /* !! */ .statusCode());
                                        } else {
                                            var0.logger.warn("Failed to ATC: state={} status={} body={}", (Object)"No Basket", (Object)var4_6 /* !! */ .statusCode(), var4_6 /* !! */ .body());
                                            var0.netLogWarn("Failed to ATC: state='No Basket' status=" + var4_6 /* !! */ .statusCode() + " body='" + (String)var5_7 + "'");
                                        }
                                    }
                                    if (var1_1 != 0 && var0.tokenRef.get() != null) {
                                        var5_7 = var0.tokenRef.get();
                                        var5_7.markPassed();
                                        var0.tokenRef.set(null);
                                    }
                                } else {
                                    var0.logger.warn("Failed to ATC: state=NO_RESPONSE");
                                }
                                break;
                            }
                        }
                        catch (Throwable var3_5) {
                            var0.logger.error("Error adding to cart: {}", (Object)var3_5.getMessage());
                            var0.netLogError("Error adding to cart: message=" + var3_5.getMessage());
                        }
                        if (!(v3 = var0.smartSleep(var0.task.getRetryDelay())).isDone()) {
                            var7_13 = v3;
                            return var7_13.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$cart(io.trickle.task.sites.yeezy.Yeezy int java.util.concurrent.CompletableFuture io.vertx.core.buffer.Buffer io.vertx.ext.web.client.HttpRequest io.vertx.ext.web.client.HttpResponse java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (int)var1_1, (CompletableFuture)var7_13, (Buffer)var2_3, null, null, null, (int)4));
                        }
lbl102:
                        // 3 sources

                        while (true) {
                            v3.join();
                            v4 = var0.prepareCart();
                            if (!v4.isDone()) {
                                var7_14 = v4;
                                return var7_14.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$cart(io.trickle.task.sites.yeezy.Yeezy int java.util.concurrent.CompletableFuture io.vertx.core.buffer.Buffer io.vertx.ext.web.client.HttpRequest io.vertx.ext.web.client.HttpResponse java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (int)var1_1, (CompletableFuture)var7_14, (Buffer)var2_3, null, null, null, (int)5));
                            }
lbl109:
                            // 3 sources

                            while (true) {
                                v4.join();
                                continue block12;
                                break;
                            }
                            break;
                        }
                        break;
                    }
                }
                return CompletableFuture.completedFuture(null);
            }
            case 1: {
                v0 = var2_3;
                ** continue;
            }
            case 2: {
                v1 = var2_3;
                v5 = var3_4;
                var3_4 = var4_6 /* !! */ ;
                var2_3 = v5;
                ** continue;
            }
            case 3: {
                v2 = var2_3;
                v6 = var3_4;
                v7 = var4_6 /* !! */ ;
                v8 = var5_7;
                var5_7 = var6_8;
                var4_6 /* !! */  = v8;
                var3_4 = v7;
                var2_3 = v6;
                ** continue;
            }
            case 4: {
                v3 = var2_3;
                var2_3 = var3_4;
                ** continue;
            }
            case 5: {
                v4 = var2_3;
                var2_3 = var3_4;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$handleCode(Yeezy var0, HttpRequest var1_1, String var2_2, int var3_3, CompletableFuture var4_4, Buffer var5_6, HttpResponse var6_7, Throwable var7_8, int var8_9, Object var9_10) {
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

    public void updateSensorUrlFromHTML(String string) {
        if (string == null) {
            this.logger.error("Unable to update sensor URL. Continuing");
            return;
        }
        Matcher matcher = SENSOR_URL_PATTERN.matcher(string);
        String string2 = null;
        while (matcher.find()) {
            string2 = matcher.group(1);
        }
        if (string2 != null) {
            this.api.setSensorUrl("https://www.yeezysupply.com" + string2);
            this.logger.info("Found sensor_url='{}'", (Object)this.api.sensorUrl);
            return;
        }
        this.logger.warn("No sensor found on page. Using backup...");
    }

    public CompletableFuture getSizeJson(boolean bl) {
        HttpRequest httpRequest = this.api.generalProdAPI();
        this.logger.info("Fetching sizes");
        int n = 0;
        while (n < (this.isBannedOnShipping ? 1 : ThreadLocalRandom.current().nextInt(4, 10))) {
            ++n;
            CompletableFuture completableFuture = Request.send(httpRequest);
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$getSizeJson(this, (int)(bl ? 1 : 0), httpRequest, n, completableFuture2, null, null, 1, arg_0));
            }
            HttpResponse httpResponse = (HttpResponse)completableFuture.join();
            if (httpResponse != null) {
                String string = (String)httpResponse.body();
                if (bl) return CompletableFuture.completedFuture(null);
                if (this.api.getSKU().equals("BY9611")) {
                    return CompletableFuture.completedFuture(null);
                }
                if (string.contains("HTTP 403 - Forbidden")) {
                    this.logger.warn("Failed to fetch sizes: state=BAN");
                } else {
                    if (httpResponse.statusCode() == 200) {
                        this.logger.info("Fetched sizes: state=OK");
                        return CompletableFuture.completedFuture(string);
                    }
                    this.logger.warn("Failed to fetch sizes: state={}", (Object)(httpResponse.statusCode() + httpResponse.statusMessage()).replaceAll(" ", "_"));
                }
                CompletableFuture completableFuture3 = VertxUtil.hardCodedSleep(1000L);
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$getSizeJson(this, (int)(bl ? 1 : 0), httpRequest, n, completableFuture4, httpResponse, string, 2, arg_0));
                }
                completableFuture3.join();
                continue;
            }
            this.logger.warn("Failed to fetch sizes: state=NO_REPLY");
            this.netLogWarn("Failed to fetch sizes: state=NO_REPLY");
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture queue() {
        this.logger.info("Waiting in queue...");
        this.netLogInfo("Waiting in queue...");
        this.api.updateDocumentUrl(YeezyAPI.QUEUE_URL);
        this.api.getCookies().removeAnyMatch("akavpwr_ys_us");
        this.api.getCookies().put("PH0ENIX", "false", ".yeezysupply.com");
        this.api.getCookies().put("utag_main", this.api.fetchUtag(), ".yeezysupply.com");
        if (this.task.getMode().contains("noqueue")) {
            return CompletableFuture.completedFuture(null);
        }
        HttpRequest httpRequest = this.api.queue().as(BodyCodec.buffer());
        while (this.running) {
            try {
                SharedCaptchaToken sharedCaptchaToken = this.tokenRef.get();
                if (sharedCaptchaToken == null || sharedCaptchaToken.isExpired()) {
                    if (sharedCaptchaToken != null && sharedCaptchaToken.isFakePassed()) {
                        this.logger.warn("Resetting harvester={} after invalid solve", (Object)this.harvesterID);
                        this.harvesterID = null;
                    }
                    CompletableFuture completableFuture = this.requestCaptcha();
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture2 = completableFuture;
                        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$queue(this, httpRequest, sharedCaptchaToken, completableFuture2, null, 0, null, null, 1, arg_0));
                    }
                    completableFuture.join();
                    this.logger.info("Proceeding");
                    continue;
                }
                if (sharedCaptchaToken.getToken() == null || sharedCaptchaToken.getToken().isBlank()) {
                    this.logger.info("Invalid captcha solve. Resetting...");
                    sharedCaptchaToken.expire();
                    continue;
                }
                if (this.api.getCookies().contains(V3_COOKIE_NAME)) {
                    if (!this.api.getCookies().getCookieValue(V3_COOKIE_NAME).equals(sharedCaptchaToken.getToken())) {
                        this.api.getCookies().put(V3_COOKIE_NAME, sharedCaptchaToken.getToken(), ".yeezysupply.com");
                    }
                } else {
                    this.api.getCookies().put(V3_COOKIE_NAME, sharedCaptchaToken.getToken(), ".yeezysupply.com");
                }
                CompletableFuture completableFuture = Request.send(httpRequest);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture3 = completableFuture;
                    return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$queue(this, httpRequest, sharedCaptchaToken, completableFuture3, null, 0, null, null, 2, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse != null) {
                    int n = httpResponse.statusCode();
                    String string = this.api.getCookies().getCookieValue(V3_COOKIE_NAME + "_u");
                    if (string != null && string.contains("data=1~")) {
                        sharedCaptchaToken.expire();
                        sharedCaptchaToken.markFakePass();
                        this.logger.warn("Fake queue pass!");
                        this.netLogInfo("Fake queue pass!");
                        Analytics.log("Fake queue pass!", this.task, this.api.getCookies().getCookieValueLength("_abck"), this.sensorPosts, this.visitedBaskets, "harvester=" + this.harvesterID);
                        Analytics.warning("Fake queue pass!", this.task);
                        this.api.getCookies().removeAnyMatch(V3_COOKIE_NAME + "_u");
                    }
                    if (this.api.getCookies().contains(V3_COOKIE_NAME + "_u") && this.api.getCookies().asString().contains("hmac")) {
                        sharedCaptchaToken.expire();
                        this.logger.info("Passed splash!");
                        this.netLogInfo("Passed splash!");
                        Analytics.log("Passed splash!", this.task, this.api.getCookies().getCookieValueLength("_abck"), this.sensorPosts, this.visitedBaskets, "harvester=" + this.harvesterID, "akavpfq_ys_us=" + this.api.getCookies().getCookieValue("akavpfq_ys_us", ""));
                        Analytics.warning("Passed splash!", this.task);
                        CompletableFuture completableFuture4 = this.logRawPage("passed-splash", (Buffer)httpResponse.body());
                        if (!completableFuture4.isDone()) {
                            CompletableFuture completableFuture5 = completableFuture4;
                            return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$queue(this, httpRequest, sharedCaptchaToken, completableFuture5, httpResponse, n, string, null, 3, arg_0));
                        }
                        completableFuture4.join();
                        this.api.getCookies().removeAnyMatch(V3_COOKIE_NAME);
                        return CompletableFuture.completedFuture(null);
                    }
                    if (n >= 300 && n < 400) {
                        this.logger.info("Redirecting!");
                        continue;
                    }
                    if (n == 200) {
                        CompletableFuture completableFuture6 = VertxUtil.sleep(this.task.getRetryDelay());
                        if (!completableFuture6.isDone()) {
                            CompletableFuture completableFuture7 = completableFuture6;
                            return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$queue(this, httpRequest, sharedCaptchaToken, completableFuture7, httpResponse, n, string, null, 4, arg_0));
                        }
                        completableFuture6.join();
                        throw new Throwable("Task banned in splash");
                    }
                    this.logger.info("Waiting in queue status=" + httpResponse.statusCode());
                    this.api.getCookies().removeAnyMatch("akavpwr_ys_us");
                    CompletableFuture completableFuture8 = VertxUtil.hardCodedSleep(this.isTurboQueue ? (long)(QUEUE_POOL_INTERVAL / 4) : (long)QUEUE_POOL_INTERVAL);
                    if (!completableFuture8.isDone()) {
                        CompletableFuture completableFuture9 = completableFuture8;
                        return ((CompletableFuture)completableFuture9.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$queue(this, httpRequest, sharedCaptchaToken, completableFuture9, httpResponse, n, string, null, 5, arg_0));
                    }
                    completableFuture8.join();
                    continue;
                }
                this.logger.error("Failed to fetch queue");
                CompletableFuture completableFuture10 = VertxUtil.sleep(this.task.getRetryDelay());
                if (!completableFuture10.isDone()) {
                    CompletableFuture completableFuture11 = completableFuture10;
                    return ((CompletableFuture)completableFuture11.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$queue(this, httpRequest, sharedCaptchaToken, completableFuture11, httpResponse, 0, null, null, 6, arg_0));
                }
                completableFuture10.join();
            }
            catch (Exception exception) {
                this.netLogError("Error in queue: message=" + exception.getMessage());
                this.logger.error("Error in queue: {}", (Object)exception.getMessage());
                CompletableFuture completableFuture = VertxUtil.sleep(this.task.getRetryDelay());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture12 = completableFuture;
                    return ((CompletableFuture)completableFuture12.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$queue(this, httpRequest, null, completableFuture12, null, 0, null, exception, 7, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public void lambda$requestCaptcha$4(ContextCompletableFuture contextCompletableFuture, AsyncResult asyncResult) {
        if (!asyncResult.succeeded()) return;
        this.tokenRef.set((SharedCaptchaToken)((Message)asyncResult.result()).body());
        contextCompletableFuture.complete((Object)null);
    }

    public void setZipAsBanned() {
        if (this.profilesOfZipCode == null) return;
        this.profilesOfZipCode.zipBanned = true;
    }

    public static String decode(String string) {
        String string2 = "fHTDGRSHTRGSEeyew5uryettuytSEJUYRTSETHTGRFE";
        StringBuilder stringBuilder = new StringBuilder();
        int n = 0;
        while (n < string.length()) {
            stringBuilder.append((char)(string.charAt(n) ^ string2.charAt(n % string2.length())));
            ++n;
        }
        return stringBuilder.toString();
    }

    public Yeezy(Task task, int n) {
        super(n);
        this.tokenRef = new AtomicReference<Object>(null);
        this.task = task;
        ((TaskActor)this).task = task;
        this.api = new YeezyAPI(this.task);
        super.setClient(this.api);
        this.instanceSignal = this.task.getKeywords()[0];
        this.authorization = "null";
        this.isTurboQueue = task.getMode().contains("turbo");
        this.harvesters = task.getMode().contains("aycd") ? autoSolveHarvesters : browserHarvesters;
    }

    public void lambda$requestCaptcha$3(String string) {
        this.logger.info("Swapped from harvester={} to new_harvester={}", (Object)this.harvesterID, (Object)string);
        this.harvesterID = string;
    }

    public boolean isCodeRequired(String string) {
        try {
            JsonObject jsonObject = new JsonObject(string);
            if (!jsonObject.containsKey("metashared")) return false;
            return jsonObject.getBoolean("metashared", Boolean.valueOf(false));
        }
        catch (Throwable throwable) {
            this.logger.warn("Failed to parse if code is requested: {}", (Object)throwable.getMessage());
        }
        return false;
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    public static CompletableFuture async$prepareSplash(Yeezy var0, CompletableFuture var1_1, String var2_2, String[] var3_3, String var4_4, String var5_5, String[] var6_6, int var7_9, String var8_12, Object var9_13, long var10_16, long var12_17, long var14_18, int var16_19, Object var17_20) {
        switch (var16_19) {
            case 0: {
                v0 = var0.getHomePage();
                if (!v0.isDone()) {
                    var15_21 = v0;
                    return var15_21.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$prepareSplash(io.trickle.task.sites.yeezy.Yeezy java.util.concurrent.CompletableFuture java.lang.String java.lang.String[] java.lang.String java.lang.String java.lang.String[] int java.lang.String java.lang.Object long long long int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (CompletableFuture)var15_21, null, null, null, null, null, (int)0, null, null, (long)0L, (long)0L, (long)0L, (int)1));
                }
                ** GOTO lbl10
            }
            case 1: {
                v0 = var1_1 /* !! */ ;
lbl10:
                // 2 sources

                var1_1 /* !! */  = (String)v0.join();
                var0.updateSensorUrlFromHTML((String)var1_1 /* !! */ );
                var2_2 = var0.getPixelParams((String)var1_1 /* !! */ );
                v1 = var0.getAkamaiScript();
                if (!v1.isDone()) {
                    var15_22 = v1;
                    return var15_22.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$prepareSplash(io.trickle.task.sites.yeezy.Yeezy java.util.concurrent.CompletableFuture java.lang.String java.lang.String[] java.lang.String java.lang.String java.lang.String[] int java.lang.String java.lang.Object long long long int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (CompletableFuture)var15_22, (String)var1_1 /* !! */ , (String[])var2_2, null, null, null, (int)0, null, null, (long)0L, (long)0L, (long)0L, (int)2));
                }
                ** GOTO lbl23
            }
            case 2: {
                v1 = var1_1 /* !! */ ;
                v2 = var2_2;
                var2_2 = var3_3 /* !! */ ;
                var1_1 /* !! */  = v2;
lbl23:
                // 2 sources

                v1.join();
                v3 = Request.executeTillOk(var0.api.getPixel(var2_2[1]));
                if (!v3.isDone()) {
                    var15_23 = v3;
                    return var15_23.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$prepareSplash(io.trickle.task.sites.yeezy.Yeezy java.util.concurrent.CompletableFuture java.lang.String java.lang.String[] java.lang.String java.lang.String java.lang.String[] int java.lang.String java.lang.Object long long long int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (CompletableFuture)var15_23, (String)var1_1 /* !! */ , (String[])var2_2, null, null, null, (int)0, null, null, (long)0L, (long)0L, (long)0L, (int)3));
                }
                ** GOTO lbl35
            }
            case 3: {
                v3 = var1_1 /* !! */ ;
                v4 = var2_2;
                var2_2 = var3_3 /* !! */ ;
                var1_1 /* !! */  = v4;
lbl35:
                // 2 sources

                var3_3 /* !! */  = (String)v3.join();
                v5 = var0.getBloom();
                if (!v5.isDone()) {
                    var15_24 = v5;
                    return var15_24.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$prepareSplash(io.trickle.task.sites.yeezy.Yeezy java.util.concurrent.CompletableFuture java.lang.String java.lang.String[] java.lang.String java.lang.String java.lang.String[] int java.lang.String java.lang.Object long long long int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (CompletableFuture)var15_24, (String)var1_1 /* !! */ , (String[])var2_2, (String)var3_3 /* !! */ , null, null, (int)0, null, null, (long)0L, (long)0L, (long)0L, (int)4));
                }
                ** GOTO lbl48
            }
            case 4: {
                v5 = var1_1 /* !! */ ;
                v6 = var2_2;
                v7 = var3_3 /* !! */ ;
                var3_3 /* !! */  = var4_4;
                var2_2 = v7;
                var1_1 /* !! */  = v6;
lbl48:
                // 2 sources

                var4_4 = (String)v5.join();
                var0.api.setUtagProductName(var4_4.replace("\n", ""));
                var0.api.getCookies().put("UserSignUpAndSave", "1", ".yeezysupply.com");
                var0.api.getCookies().put("UserSignUpAndSaveOverlay", "0", ".yeezysupply.com");
                var0.api.getCookies().put("default_searchTerms_CustomizeSearch", "%5B%5D", ".yeezysupply.com");
                var0.api.getCookies().put("geoRedirectionAlreadySuggested", "false", ".yeezysupply.com");
                var0.api.getCookies().put("wishlist", "%5B%5D", ".yeezysupply.com");
                var0.api.getCookies().put("persistentBasketCount", "0", ".yeezysupply.com");
                var0.api.getCookies().put("userBasketCount", "0", ".yeezysupply.com");
                var5_5 = Pixel.parseHexArray((String)var3_3 /* !! */ );
                var6_7 = Pixel.parseGIndex((String)var3_3 /* !! */ );
                var7_10 = Pixel.parseTValue(var2_2[2]);
                var8_12 = var5_5[var6_7];
                v8 = var0.sendPixel(var0.api.postPixel(var2_2[2].split("\\?")[0]), var2_2[0], var7_10, (String)var8_12);
                if (!v8.isDone()) {
                    var15_25 = v8;
                    return var15_25.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$prepareSplash(io.trickle.task.sites.yeezy.Yeezy java.util.concurrent.CompletableFuture java.lang.String java.lang.String[] java.lang.String java.lang.String java.lang.String[] int java.lang.String java.lang.Object long long long int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (CompletableFuture)var15_25, (String)var1_1 /* !! */ , (String[])var2_2, (String)var3_3 /* !! */ , (String)var4_4, (String[])var5_5, (int)var6_7, (String)var7_10, (Object)var8_12, (long)0L, (long)0L, (long)0L, (int)5));
                }
                ** GOTO lbl88
            }
            case 5: {
                v8 = var1_1 /* !! */ ;
                v9 = var2_2;
                v10 = var3_3 /* !! */ ;
                v11 = var4_4;
                v12 = var5_5;
                v13 = var8_12;
                var8_12 = var9_13;
                var7_10 = v13;
                var6_7 = var7_9;
                var5_5 = var6_6;
                var4_4 = v12;
                var3_3 /* !! */  = v11;
                var2_2 = v10;
                var1_1 /* !! */  = v9;
lbl88:
                // 2 sources

                v8.join();
                var0.pixelPostTs = System.currentTimeMillis();
                var0.api.getCookies().put("geo_country", "US", ".yeezysupply.com");
                var0.api.getCookies().put("utag_main", var0.api.fetchUtag(), ".yeezysupply.com");
                var0.api.getCookies().put("_ga", "GA1.2." + ThreadLocalRandom.current().nextInt(1207338862, 1992599043) + "." + System.currentTimeMillis(), ".yeezysupply.com");
                var0.api.getCookies().put("_gid", "GA1.2." + ThreadLocalRandom.current().nextInt(120016221, 190016221) + "." + System.currentTimeMillis(), ".yeezysupply.com");
                var0.api.getCookies().put("_gat_tealium_0", "1", ".yeezysupply.com");
                var0.api.getCookies().put("_fbp", "fb.1." + Instant.now().toEpochMilli() + ThreadLocalRandom.current().nextInt(1000) + "." + Instant.now().toEpochMilli(), ".yeezysupply.com");
                var0.api.getCookies().put("_gcl_au", "1.1." + System.currentTimeMillis() + "." + System.currentTimeMillis(), ".yeezysupply.com");
                var0.api.getCookies().put("AMCVS_7ADA401053CCF9130A490D4C%40AdobeOrg", "1", ".yeezysupply.com");
                var9_14 = Instant.now().getEpochSecond() + 7200L;
                var11_30 = var9_14 + 597600L;
                var0.api.getCookies().put("AMCV_7ADA401053CCF9130A490D4C%40AdobeOrg", "-227196251%7CMCIDTS%7C" + Utag.H() + "%7CMCMID%7C" + Utag.r() + "%7CMCAAMLH-" + var11_30 + "%7C7%7CMCAAMB-" + var11_30 + "%7CRKhpRz8krg2tLO6pguXWp5olkAcUniQYPHaMWWgdJ3xzPWQmdj0y%7CMCOPTOUT-" + var9_14 + "s%7CNONE%7CMCAID%7CNONE", ".yeezysupply.com");
                var0.api.getCookies().put("s_cc", "true", ".yeezysupply.com");
                var13_32 = ThreadLocalRandom.current().nextLong(1625112000771L, 1625112000876L);
                var0.api.getCookies().put("s_pers", "%20s_vnum%3D" + var13_32 + "%2526vn%253D1%7C" + var13_32 + "%3B%20s_invisit%3Dtrue%7C" + (Instant.now().toEpochMilli() + 1800000L) + "%3B", ".yeezysupply.com");
                v14 = var0.getProductPage(false);
                if (!v14.isDone()) {
                    var15_26 = v14;
                    return var15_26.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$prepareSplash(io.trickle.task.sites.yeezy.Yeezy java.util.concurrent.CompletableFuture java.lang.String java.lang.String[] java.lang.String java.lang.String java.lang.String[] int java.lang.String java.lang.Object long long long int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (CompletableFuture)var15_26, (String)var1_1 /* !! */ , (String[])var2_2, (String)var3_3 /* !! */ , (String)var4_4, (String[])var5_5, (int)var6_7, (String)var7_10, (Object)var8_12, (long)var9_14, (long)var11_30, (long)var13_32, (int)6));
                }
                ** GOTO lbl139
            }
            case 6: {
                v14 = var1_1 /* !! */ ;
                v15 = var2_2;
                v16 = var3_3 /* !! */ ;
                v17 = var4_4;
                v18 = var5_5;
                v19 = var8_12;
                var13_32 = var14_18;
                var11_30 = var12_17;
                var9_14 = var10_16;
                var8_12 = var9_13;
                var7_10 = v19;
                var6_7 = var7_9;
                var5_5 = var6_6;
                var4_4 = v18;
                var3_3 /* !! */  = v17;
                var2_2 = v16;
                var1_1 /* !! */  = v15;
lbl139:
                // 2 sources

                v14.join();
                v20 = Request.executeTillOk(var0.api.sharedHpl());
                if (!v20.isDone()) {
                    var15_27 = v20;
                    return var15_27.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$prepareSplash(io.trickle.task.sites.yeezy.Yeezy java.util.concurrent.CompletableFuture java.lang.String java.lang.String[] java.lang.String java.lang.String java.lang.String[] int java.lang.String java.lang.Object long long long int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (CompletableFuture)var15_27, (String)var1_1 /* !! */ , (String[])var2_2, (String)var3_3 /* !! */ , (String)var4_4, (String[])var5_5, (int)var6_7, (String)var7_10, (Object)var8_12, (long)var9_14, (long)var11_30, (long)var13_32, (int)7));
                }
                ** GOTO lbl164
            }
            case 7: {
                v20 = var1_1 /* !! */ ;
                v21 = var2_2;
                v22 = var3_3 /* !! */ ;
                v23 = var4_4;
                v24 = var5_5;
                v25 = var8_12;
                var13_32 = var14_18;
                var11_30 = var12_17;
                var9_14 = var10_16;
                var8_12 = var9_13;
                var7_10 = v25;
                var6_7 = var7_9;
                var5_5 = var6_6;
                var4_4 = v24;
                var3_3 /* !! */  = v23;
                var2_2 = v22;
                var1_1 /* !! */  = v21;
lbl164:
                // 2 sources

                v20.join();
                v26 = Request.executeTillOk(var0.api.ushpl());
                if (!v26.isDone()) {
                    var15_28 = v26;
                    return var15_28.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$prepareSplash(io.trickle.task.sites.yeezy.Yeezy java.util.concurrent.CompletableFuture java.lang.String java.lang.String[] java.lang.String java.lang.String java.lang.String[] int java.lang.String java.lang.Object long long long int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (CompletableFuture)var15_28, (String)var1_1 /* !! */ , (String[])var2_2, (String)var3_3 /* !! */ , (String)var4_4, (String[])var5_5, (int)var6_7, (String)var7_10, (Object)var8_12, (long)var9_14, (long)var11_30, (long)var13_32, (int)8));
                }
                ** GOTO lbl189
            }
            case 8: {
                v26 = var1_1 /* !! */ ;
                v27 = var2_2;
                v28 = var3_3 /* !! */ ;
                v29 = var4_4;
                v30 = var5_5;
                v31 = var8_12;
                var13_32 = var14_18;
                var11_30 = var12_17;
                var9_14 = var10_16;
                var8_12 = var9_13;
                var7_10 = v31;
                var6_7 = var7_9;
                var5_5 = var6_6;
                var4_4 = v30;
                var3_3 /* !! */  = v29;
                var2_2 = v28;
                var1_1 /* !! */  = v27;
lbl189:
                // 2 sources

                v26.join();
                v32 = var0.waitForSale();
                if (!v32.isDone()) {
                    var15_29 = v32;
                    return var15_29.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$prepareSplash(io.trickle.task.sites.yeezy.Yeezy java.util.concurrent.CompletableFuture java.lang.String java.lang.String[] java.lang.String java.lang.String java.lang.String[] int java.lang.String java.lang.Object long long long int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (CompletableFuture)var15_29, (String)var1_1 /* !! */ , (String[])var2_2, (String)var3_3 /* !! */ , (String)var4_4, (String[])var5_5, (int)var6_7, (String)var7_10, (Object)var8_12, (long)var9_14, (long)var11_30, (long)var13_32, (int)9));
                }
                ** GOTO lbl214
            }
            case 9: {
                v32 = var1_1 /* !! */ ;
                v33 = var2_2;
                v34 = var3_3 /* !! */ ;
                v35 = var4_4;
                v36 = var5_5;
                v37 = var8_12;
                var13_33 = var14_18;
                var11_31 = var12_17;
                var9_15 = var10_16;
                var8_12 = var9_13;
                var7_11 = v37;
                var6_8 = var7_9;
                var5_5 = var6_6;
                var4_4 = v36;
                var3_3 /* !! */  = v35;
                var2_2 = v34;
                var1_1 /* !! */  = v33;
lbl214:
                // 2 sources

                v32.join();
                return CompletableFuture.completedFuture(null);
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture postSensors() {
        String string = this.api.userAgent;
        CookieJar cookieJar = this.api.getCookies();
        if (!this.isFirewallOn()) {
            this.api.getWebClient().freshenCookieStore();
            for (Cookie cookie : cookieJar.get(true, ".yeezysupply.com", "/")) {
                if (!cookie.name().contains("_abck") && !cookie.name().startsWith("bm_") && !cookie.name().equals("ak_bmsc") && !cookie.name().equalsIgnoreCase("RT")) continue;
                this.api.getCookies().put(cookie);
                cookieJar.remove(cookie);
            }
        }
        for (int i = 0; i < 2; ++i) {
            CompletableFuture completableFuture = this.sendSensor(this.api.postSensor(true), false);
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$postSensors(this, string, cookieJar, i, completableFuture2, null, 0, 1, arg_0));
            }
            completableFuture.join();
            CompletableFuture completableFuture3 = this.smartSleep(500L);
            if (!completableFuture3.isDone()) {
                CompletableFuture completableFuture4 = completableFuture3;
                return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$postSensors(this, string, cookieJar, i, completableFuture4, null, 0, 2, arg_0));
            }
            completableFuture3.join();
        }
        String string2 = "";
        int n = 0;
        int n2 = ThreadLocalRandom.current().nextInt(9, 13);
        while (n++ < n2) {
            CompletableFuture completableFuture = this.smartSleep(700L);
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture5 = completableFuture;
                return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$postSensors(this, string, cookieJar, n, completableFuture5, string2, n2, 3, arg_0));
            }
            completableFuture.join();
            CompletableFuture completableFuture6 = this.smartUA();
            if (!completableFuture6.isDone()) {
                CompletableFuture completableFuture7 = completableFuture6;
                return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$postSensors(this, string, cookieJar, n, completableFuture7, string2, n2, 4, arg_0));
            }
            completableFuture6.join();
            CompletableFuture completableFuture8 = this.sendSensor(this.api.postSensor(true), true);
            if (!completableFuture8.isDone()) {
                CompletableFuture completableFuture9 = completableFuture8;
                return ((CompletableFuture)completableFuture9.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$postSensors(this, string, cookieJar, n, completableFuture9, string2, n2, 5, arg_0));
            }
            string2 = (String)completableFuture8.join();
            if (n > 6 && string2 != null && string2.contains("false")) {
                this.logger.info("YS site down!");
                break;
            }
            if (this.api.getCookies().getCookieValue("_abck").length() >= 561 && !this.api.getCookies().getCookieValue("_abck").contains("||")) {
                this.logger.info("Passed sensor validation step -> Handling sensor step='{}/{}' - len={}", (Object)n, (Object)n2, (Object)this.api.getCookies().getCookieValue("_abck").length());
                if (n > 4 && this.isBannedOnShipping) {
                    break;
                }
            } else {
                this.logger.info("Handling sensor step='{}/{}' len={}", (Object)n, (Object)n2, (Object)this.api.getCookies().getCookieValue("_abck").length());
            }
            ++this.sensorPosts;
        }
        if (string2 != null) {
            this.logger.info("Posted sensor: c={} l={} val={}", (Object)n, (Object)this.api.getCookies().getCookieValue("_abck").length(), (Object)string2.replace("\n", " "));
        } else {
            this.logger.info("Sensor not posted properly. Continuing.");
        }
        if (this.isFirewallOn()) return CompletableFuture.completedFuture(null);
        this.api.getCookies().putFromOther(cookieJar);
        this.api.userAgent = string;
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$handleAfter3DS(Yeezy var0, Window3DS2 var1_1, int var2_2, JsonObject var3_3, HttpRequest var4_5, CompletableFuture var5_6, HttpResponse var6_7, String var7_8, Throwable var8_9, int var9_10, Object var10_11) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [13[CATCHBLOCK]], but top level block is 22[UNCONDITIONALDOLOOP]
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
    public static CompletableFuture async$queue(Yeezy var0, HttpRequest var1_1, SharedCaptchaToken var2_2, CompletableFuture var3_4, HttpResponse var4_5, int var5_9, String var6_13, Exception var7_14, int var8_15, Object var9_16) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [11[CATCHBLOCK]], but top level block is 17[UNCONDITIONALDOLOOP]
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
    public static CompletableFuture async$getAkamaiScript(Yeezy var0, HttpRequest var1_1, CompletableFuture var2_2, HttpResponse var3_3, int var4_4, Object var5_5) {
        switch (var4_4) {
            case 0: {
                var1_1 = var0.api.akamaiScript(false);
                var0.logger.info("Visiting FW");
                var0.netLogInfo("Visiting FW");
lbl6:
                // 4 sources

                while (true) {
                    v0 = Request.send(var1_1);
                    if (!v0.isDone()) {
                        var3_3 = v0;
                        return var3_3.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$getAkamaiScript(io.trickle.task.sites.yeezy.Yeezy io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (HttpRequest)var1_1, (CompletableFuture)var3_3, null, (int)1));
                    }
                    ** GOTO lbl14
                    break;
                }
            }
            case 1: {
                v0 = var2_2;
lbl14:
                // 2 sources

                if ((var2_2 = (HttpResponse)v0.join()) == null) ** GOTO lbl26
                if (!((String)var2_2.body()).contains("HTTP 403 - Forbidden")) ** GOTO lbl23
                var0.logger.warn("Failed to visit FW: state=PROXY_BAN");
                var0.netLogWarn("Failed to visit FW: state=PROXY_BAN");
                v1 = VertxUtil.sleep(var0.task.getMonitorDelay());
                if (!v1.isDone()) {
                    var3_3 = v1;
                    return var3_3.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$getAkamaiScript(io.trickle.task.sites.yeezy.Yeezy io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (HttpRequest)var1_1, (CompletableFuture)var3_3, (HttpResponse)var2_2, (int)2));
                }
                ** GOTO lbl32
lbl23:
                // 1 sources

                if (var2_2.statusCode() != 200) ** GOTO lbl6
                var0.netLogWarn("Visited FW: state=OK");
                return CompletableFuture.completedFuture(null);
lbl26:
                // 1 sources

                var0.logger.warn("Failed to visit FW: state=NO_REPLY");
                var0.netLogWarn("Failed to visit FW: state=NO_REPLY");
                ** GOTO lbl6
            }
            case 2: {
                v1 = var2_2;
                var2_2 = var3_3;
lbl32:
                // 2 sources

                v1.join();
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture checkOrCaptcha() {
        try {
            CompletableFuture completableFuture = this.vertx.sharedData().getLocalLock("CAP").toCompletionStage().toCompletableFuture();
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$checkOrCaptcha(this, completableFuture2, null, null, null, 1, arg_0));
            }
            Lock lock = (Lock)completableFuture.join();
            try {
                SolveFuture solveFuture;
                if (holder.get() == null || !holder.get().isUsed()) {
                    if (clock.get() + 117L >= Instant.now().getEpochSecond()) return CompletableFuture.completedFuture(null);
                }
                this.logger.info("Captcha '{}' needs solving", (Object)holder.get());
                SolveFuture solveFuture2 = solveFuture = ((TokenController)Engine.get().getModule(Controller.TOKEN)).solve(new CaptchaToken("https://www.yeezysupply.com/products/" + this.api.getSKU()));
                if (!solveFuture2.toCompletableFuture().isDone()) {
                    SolveFuture solveFuture3 = solveFuture2;
                    return solveFuture3.exceptionally(Function.identity()).thenCompose(arg_0 -> Yeezy.async$checkOrCaptcha(this, null, lock, solveFuture, solveFuture3, 2, arg_0)).toCompletableFuture();
                }
                solveFuture2.toCompletableFuture().join();
                clock.set(Instant.now().getEpochSecond());
                holder.set((CaptchaToken)solveFuture.get());
                this.logger.info("Captcha after solve {}", (Object)holder.get());
                return CompletableFuture.completedFuture(null);
            }
            catch (Throwable throwable) {
                this.logger.error("HARVEST ERR: {}", (Object)throwable.getMessage());
                return CompletableFuture.completedFuture(null);
            }
            finally {
                lock.release();
            }
        }
        catch (Throwable throwable) {
            this.logger.error("LOCK ERR: {}", (Object)throwable.getMessage());
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$postSensors(Yeezy var0, String var1_1, CookieJar var2_2, int var3_3, CompletableFuture var4_6, String var5_8, int var6_10, int var7_16, Object var8_17) {
        switch (var7_16) {
            case 0: {
                var1_1 = var0.api.userAgent;
                var2_2 = var0.api.getCookies();
                if (!var0.isFirewallOn()) {
                    var0.api.getWebClient().freshenCookieStore();
                    for (CompletableFuture var4_6 : var2_2.get(true, ".yeezysupply.com", "/")) {
                        if (!var4_6.name().contains("_abck") && !var4_6.name().startsWith("bm_") && !var4_6.name().equals("ak_bmsc") && !var4_6.name().equalsIgnoreCase("RT")) continue;
                        var0.api.getCookies().put((Cookie)var4_6);
                        var2_2.remove((Cookie)var4_6);
                    }
                }
                var3_3 = 0;
lbl15:
                // 2 sources

                while (true) {
                    if (var3_3 >= 2) ** GOTO lbl22
                    v0 = var0.sendSensor(var0.api.postSensor(true), false);
                    if (!v0.isDone()) {
                        var6_11 = v0;
                        return var6_11.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$postSensors(io.trickle.task.sites.yeezy.Yeezy java.lang.String io.trickle.webclient.CookieJar int java.util.concurrent.CompletableFuture java.lang.String int int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (String)var1_1, (CookieJar)var2_2, (int)var3_3, (CompletableFuture)var6_11, null, (int)0, (int)1));
                    }
                    ** GOTO lbl34
lbl22:
                    // 1 sources

                    var3_5 = "";
                    var4_7 = 0;
                    var5_9 = ThreadLocalRandom.current().nextInt(9, 13);
lbl25:
                    // 2 sources

                    while (var4_7++ < var5_9) {
                        v1 = var0.smartSleep(700L);
                        if (!v1.isDone()) {
                            var6_13 = v1;
                            return var6_13.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$postSensors(io.trickle.task.sites.yeezy.Yeezy java.lang.String io.trickle.webclient.CookieJar int java.util.concurrent.CompletableFuture java.lang.String int int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (String)var1_1, (CookieJar)var2_2, (int)var4_7, (CompletableFuture)var6_13, (String)var3_5, (int)var5_9, (int)3));
                        }
                        ** GOTO lbl52
                    }
                    ** GOTO lbl87
                    break;
                }
            }
            case 1: {
                v0 = var4_6;
lbl34:
                // 2 sources

                v0.join();
                v2 = var0.smartSleep(500L);
                if (!v2.isDone()) {
                    var6_12 = v2;
                    return var6_12.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$postSensors(io.trickle.task.sites.yeezy.Yeezy java.lang.String io.trickle.webclient.CookieJar int java.util.concurrent.CompletableFuture java.lang.String int int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (String)var1_1, (CookieJar)var2_2, (int)var3_3, (CompletableFuture)var6_12, null, (int)0, (int)2));
                }
                ** GOTO lbl43
            }
            case 2: {
                v2 = var4_6;
lbl43:
                // 2 sources

                v2.join();
                ++var3_3;
                ** continue;
            }
            case 3: {
                v1 = var4_6;
                var5_9 = var6_10;
                var4_7 = var3_3;
                var3_5 = var5_8;
lbl52:
                // 2 sources

                v1.join();
                v3 = var0.smartUA();
                if (!v3.isDone()) {
                    var6_14 = v3;
                    return var6_14.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$postSensors(io.trickle.task.sites.yeezy.Yeezy java.lang.String io.trickle.webclient.CookieJar int java.util.concurrent.CompletableFuture java.lang.String int int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (String)var1_1, (CookieJar)var2_2, (int)var4_7, (CompletableFuture)var6_14, (String)var3_5, (int)var5_9, (int)4));
                }
                ** GOTO lbl64
            }
            case 4: {
                v3 = var4_6;
                var5_9 = var6_10;
                var4_7 = var3_3;
                var3_5 = var5_8;
lbl64:
                // 2 sources

                v3.join();
                v4 = var0.sendSensor(var0.api.postSensor(true), true);
                if (!v4.isDone()) {
                    var6_15 = v4;
                    return var6_15.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$postSensors(io.trickle.task.sites.yeezy.Yeezy java.lang.String io.trickle.webclient.CookieJar int java.util.concurrent.CompletableFuture java.lang.String int int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (String)var1_1, (CookieJar)var2_2, (int)var4_7, (CompletableFuture)var6_15, (String)var3_5, (int)var5_9, (int)5));
                }
                ** GOTO lbl76
            }
            case 5: {
                v4 = var4_6;
                var5_9 = var6_10;
                var4_7 = var3_3;
                var3_5 = var5_8;
lbl76:
                // 2 sources

                var3_5 = (String)v4.join();
                if (var4_7 <= 6 || var3_5 == null || !var3_5.contains("false")) ** GOTO lbl80
                var0.logger.info("YS site down!");
                ** GOTO lbl87
lbl80:
                // 1 sources

                if (var0.api.getCookies().getCookieValue("_abck").length() < 561 || var0.api.getCookies().getCookieValue("_abck").contains("||")) ** GOTO lbl84
                var0.logger.info("Passed sensor validation step -> Handling sensor step='{}/{}' - len={}", (Object)var4_7, (Object)var5_9, (Object)var0.api.getCookies().getCookieValue("_abck").length());
                if (var4_7 <= 4 || !var0.isBannedOnShipping) ** GOTO lbl85
                ** GOTO lbl87
lbl84:
                // 1 sources

                var0.logger.info("Handling sensor step='{}/{}' len={}", (Object)var4_7, (Object)var5_9, (Object)var0.api.getCookies().getCookieValue("_abck").length());
lbl85:
                // 2 sources

                ++var0.sensorPosts;
                ** GOTO lbl25
lbl87:
                // 3 sources

                if (var3_5 != null) {
                    var0.logger.info("Posted sensor: c={} l={} val={}", (Object)var4_7, (Object)var0.api.getCookies().getCookieValue("_abck").length(), (Object)var3_5.replace("\n", " "));
                } else {
                    var0.logger.info("Sensor not posted properly. Continuing.");
                }
                if (var0.isFirewallOn() != false) return CompletableFuture.completedFuture(null);
                var0.api.getCookies().putFromOther(var2_2);
                var0.api.userAgent = var1_1;
                return CompletableFuture.completedFuture(null);
            }
        }
        throw new IllegalArgumentException();
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$prepareCart(Yeezy var0, CompletableFuture var1_1, String var2_2, int var3_3, Object var4_4) {
        switch (var3_3) {
            case 0: {
                v0 = var0.getProductPage(true);
                if (!v0.isDone()) {
                    var2_2 = v0;
                    return var2_2.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$prepareCart(io.trickle.task.sites.yeezy.Yeezy java.util.concurrent.CompletableFuture java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (CompletableFuture)var2_2, null, (int)1));
                }
                ** GOTO lbl10
            }
            case 1: {
                v0 = var1_1;
lbl10:
                // 2 sources

                var1_1 = (String)v0.join();
                var0.updateSensorUrlFromHTML((String)var1_1);
                var0.api.updateDocumentUrl("https://www.yeezysupply.com/product/" + var0.api.getSKU());
                var0.api.getCookies().put("utag_main", var0.api.fetchUtag(), ".yeezysupply.com");
                v1 = Request.execute(var0.api.akamaiScript(true));
                if (!v1.isDone()) {
                    var2_2 = v1;
                    return var2_2.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$prepareCart(io.trickle.task.sites.yeezy.Yeezy java.util.concurrent.CompletableFuture java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (CompletableFuture)var2_2, (String)var1_1, (int)2));
                }
                ** GOTO lbl23
            }
            case 2: {
                v1 = var1_1;
                var1_1 = var2_2;
lbl23:
                // 2 sources

                v1.join();
                v2 = var0.sendBasic(var0.api.secondaryProdAPI(), "Prod Info #1");
                if (!v2.isDone()) {
                    var2_2 = v2;
                    return var2_2.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$prepareCart(io.trickle.task.sites.yeezy.Yeezy java.util.concurrent.CompletableFuture java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (CompletableFuture)var2_2, (String)var1_1, (int)3));
                }
                ** GOTO lbl33
            }
            case 3: {
                v2 = var1_1;
                var1_1 = var2_2;
lbl33:
                // 2 sources

                v2.join();
                if (!var0.isFirstIteration) ** GOTO lbl47
                var0.isFirstIteration = false;
                v3 = Request.executeTillOk(var0.api.newsletter(Utils.quickParseFirst((String)var1_1, new Pattern[]{Yeezy.SIGNUP_ID_PATTERN})));
                if (!v3.isDone()) {
                    var2_2 = v3;
                    return var2_2.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$prepareCart(io.trickle.task.sites.yeezy.Yeezy java.util.concurrent.CompletableFuture java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (CompletableFuture)var2_2, (String)var1_1, (int)4));
                }
                ** GOTO lbl45
            }
            case 4: {
                v3 = var1_1;
                var1_1 = var2_2;
lbl45:
                // 2 sources

                v3.join();
lbl47:
                // 2 sources

                if (!(v4 = var0.fillSizes()).isDone()) {
                    var2_2 = v4;
                    return var2_2.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$prepareCart(io.trickle.task.sites.yeezy.Yeezy java.util.concurrent.CompletableFuture java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (CompletableFuture)var2_2, (String)var1_1, (int)5));
                }
                ** GOTO lbl54
            }
            case 5: {
                v4 = var1_1;
                var1_1 = var2_2;
lbl54:
                // 2 sources

                v4.join();
                v5 = var0.postSensors();
                if (!v5.isDone()) {
                    var2_2 = v5;
                    return var2_2.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$prepareCart(io.trickle.task.sites.yeezy.Yeezy java.util.concurrent.CompletableFuture java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (CompletableFuture)var2_2, (String)var1_1, (int)6));
                }
                ** GOTO lbl64
            }
            case 6: {
                v5 = var1_1;
                var1_1 = var2_2;
lbl64:
                // 2 sources

                v5.join();
                if (var0.isPassedFW != false) return CompletableFuture.completedFuture(null);
                var0.logger.info("Preparing to cart with pre-emptive step=x00433");
                v6 = Request.executeTillOk(var0.api.emptyBasket(true, var0.authorization));
                if (!v6.isDone()) {
                    var2_2 = v6;
                    return var2_2.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$prepareCart(io.trickle.task.sites.yeezy.Yeezy java.util.concurrent.CompletableFuture java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (CompletableFuture)var2_2, (String)var1_1, (int)7));
                }
                ** GOTO lbl76
            }
            case 7: {
                v6 = var1_1;
                var1_1 = var2_2;
lbl76:
                // 2 sources

                v6.join();
                var0.visitedBaskets = true;
                return CompletableFuture.completedFuture(null);
            }
        }
        throw new IllegalArgumentException();
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$run(Yeezy var0, CompletableFuture var1_1, int var2_3, int var3_5, Object var4_13) {
        switch (var3_5) {
            case 0: {
                if (var0.task.getMode().contains("noqueue")) ** GOTO lbl-1000
                v0 = var0.harvesters.start();
                if (!v0.isDone()) {
                    var3_6 = v0;
                    return var3_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.yeezy.Yeezy java.util.concurrent.CompletableFuture int int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (CompletableFuture)var3_6, (int)0, (int)1));
                }
lbl8:
                // 3 sources

                while (true) {
                    if (((Boolean)v0.join()).booleanValue()) lbl-1000:
                    // 2 sources

                    {
                        v1 = 1;
                    } else {
                        v1 = 0;
                    }
                    var1_2 = v1;
                    while (var1_2 != 0) {
                        try {
                            v2 = var0.api.init();
                            if (!v2.isDone()) {
                                var3_7 = v2;
                                return var3_7.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.yeezy.Yeezy java.util.concurrent.CompletableFuture int int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (CompletableFuture)var3_7, (int)var1_2, (int)2));
                            }
lbl20:
                            // 3 sources

                            while (true) {
                                v2.join();
                                v3 = var0.prepareSplash();
                                if (!v3.isDone()) {
                                    var3_8 = v3;
                                    return var3_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.yeezy.Yeezy java.util.concurrent.CompletableFuture int int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (CompletableFuture)var3_8, (int)var1_2, (int)3));
                                }
lbl27:
                                // 3 sources

                                while (true) {
                                    v3.join();
                                    v4 = var0.queue();
                                    if (!v4.isDone()) {
                                        var3_9 = v4;
                                        return var3_9.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.yeezy.Yeezy java.util.concurrent.CompletableFuture int int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (CompletableFuture)var3_9, (int)var1_2, (int)4));
                                    }
lbl34:
                                    // 3 sources

                                    while (true) {
                                        v4.join();
                                        v5 = var0.prepareCart();
                                        if (!v5.isDone()) {
                                            var3_10 = v5;
                                            return var3_10.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.yeezy.Yeezy java.util.concurrent.CompletableFuture int int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (CompletableFuture)var3_10, (int)var1_2, (int)5));
                                        }
lbl41:
                                        // 3 sources

                                        while (true) {
                                            v5.join();
                                            v6 = var0.cart();
                                            if (!v6.isDone()) {
                                                var3_11 = v6;
                                                return var3_11.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.yeezy.Yeezy java.util.concurrent.CompletableFuture int int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (CompletableFuture)var3_11, (int)var1_2, (int)6));
                                            }
lbl48:
                                            // 3 sources

                                            while (true) {
                                                v6.join();
                                                var0.api.getCookies().put("userBasketCount", "1", ".yeezysupply.com");
                                                var0.api.getCookies().put("persistentBasketCount", "1", ".yeezysupply.com");
                                                var0.api.getCookies().put("restoreBasketUrl", "%2Fon%2Fdemandware.store%2FSites-ys-US-Site%2Fen_US%2FCart-UpdateItems%3Fpid_0%3D" + var0.api.getSKU() + Sizes.getSize(var0.task.getSize()) + "%26qty_0%3D1%26", ".yeezysupply.com");
                                                v7 = var0.checkout();
                                                if (!v7.isDone()) {
                                                    var3_12 = v7;
                                                    return var3_12.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.yeezy.Yeezy java.util.concurrent.CompletableFuture int int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (CompletableFuture)var3_12, (int)var1_2, (int)7));
                                                }
lbl61:
                                                // 3 sources

                                                while (true) {
                                                    v7.join();
                                                    var1_2 = 0;
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
                                break;
                            }
                        }
                        catch (Throwable var2_4) {
                            if (!var2_4.getMessage().contains("403")) {
                                var0.logger.error(var2_4.getMessage());
                            } else if (var2_4.getMessage().contains("FW")) {
                                var0.logger.error(var2_4.getMessage());
                            }
                            var0.sensorPosts = 0;
                            var0.visitedBaskets = false;
                            var0.api.close();
                            var0.api = new YeezyAPI(var0.task);
                            super.setClient(var0.api);
                        }
                    }
                    return CompletableFuture.completedFuture(null);
                }
            }
            case 1: {
                v0 = var1_1;
                ** continue;
            }
            case 2: {
                v2 = var1_1;
                var1_2 = var2_3;
                ** continue;
            }
            case 3: {
                v3 = var1_1;
                var1_2 = var2_3;
                ** continue;
            }
            case 4: {
                v4 = var1_1;
                var1_2 = var2_3;
                ** continue;
            }
            case 5: {
                v5 = var1_1;
                var1_2 = var2_3;
                ** continue;
            }
            case 6: {
                v6 = var1_1;
                var1_2 = var2_3;
                ** continue;
            }
            case 7: {
                v7 = var1_1;
                var1_2 = var2_3;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public void checkHmac() {
        boolean bl = this.api.getCookies().contains(V3_COOKIE_NAME + "_u") && this.api.getCookies().asString().contains("hmac");
        if (bl) return;
        if (this.task.getMode().contains("noqueue")) return;
        throw new Throwable("HMAC expired.");
    }

    public CompletableFuture get3DS2Page(JsonObject jsonObject) {
        JsonObject jsonObject2 = jsonObject.getJsonObject("paRedirectForm");
        this.orderID = jsonObject.getString("orderId");
        String string = jsonObject2.getString("formMethod", "POST");
        String string2 = jsonObject2.getString("formAction");
        JsonObject jsonObject3 = jsonObject2.getJsonObject("formFields");
        String string3 = jsonObject3.getString("EncodedData");
        jsonObject3.remove("EncodedData");
        MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
        for (Object object : jsonObject3.fieldNames()) {
            try {
                String string4 = jsonObject3.getString((String)object);
                if (string4 == null) continue;
                multiMap.add((String)object, string4);
            }
            catch (Throwable throwable) {
                this.logger.warn("Error parsing auth fields: {}", (Object)throwable.getMessage());
            }
        }
        String string5 = "https://www.yeezysupply.com/payment/callback/CREDIT_CARD/" + this.basketID + "/adyen?orderId=" + this.orderID + "&encodedData=" + string3 + "&result=AUTHORISED";
        multiMap.add("TermUrl", string5);
        try {
            Object object;
            object = new Window3DS2(this.api.userAgent, string2, string5, string3, string, multiMap);
            return CompletableFuture.completedFuture(object);
        }
        catch (Throwable throwable) {
            this.logger.warn("Error initialising auth session: {}", (Object)throwable.getMessage());
            return CompletableFuture.failedFuture(throwable);
        }
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$fillSizes(Yeezy var0, CompletableFuture var1_1, int var2_2, Object var3_5) {
        switch (var2_2) {
            case 0: {
                v0 = var0.getSizeJson(var0.availableSizes.isEmpty() == false);
                if (!v0.isDone()) {
                    var4_6 = v0;
                    return var4_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$fillSizes(io.trickle.task.sites.yeezy.Yeezy java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (CompletableFuture)var4_6, (int)1));
                }
                ** GOTO lbl10
            }
            case 1: {
                v0 = var1_1;
lbl10:
                // 2 sources

                var1_1 = (String)v0.join();
                try {
                    var2_3 = new JsonObject((String)var1_1).getJsonArray("variation_list");
                    var3_5 = var2_3.stream().sorted((Comparator)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;Ljava/lang/Object;)I, lambda$fillSizes$5(java.lang.Object java.lang.Object ), (Ljava/lang/Object;Ljava/lang/Object;)I)()).map((Function<Object, JsonObject>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, lambda$fillSizes$6(java.lang.Object ), (Ljava/lang/Object;)Lio/vertx/core/json/JsonObject;)()).collect(Collectors.<T>toList());
                    if (var3_5.isEmpty() != false) return CompletableFuture.completedFuture(null);
                    var0.availableSizes.clear();
                    var0.availableSizes.addAll(var3_5);
                    return CompletableFuture.completedFuture(null);
                }
                catch (Throwable var2_4) {
                    var0.logger.error("Could not order sizes (Normal. Don't panic): {}", (Object)var2_4.getMessage());
                }
                return CompletableFuture.completedFuture(null);
            }
        }
        throw new IllegalArgumentException();
    }

    public HttpRequest lambda$handlePOW$1(String string) {
        return this.api.powPage(string);
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$getSizeJson(Yeezy var0, int var1_1, HttpRequest var2_2, int var3_3, CompletableFuture var4_4, HttpResponse var5_5, String var6_6, int var7_7, Object var8_8) {
        switch (var7_7) {
            case 0: {
                var2_2 = var0.api.generalProdAPI();
                var0.logger.info("Fetching sizes");
                var3_3 = 0;
lbl6:
                // 3 sources

                while (true) {
                    if (var3_3 >= (var0.isBannedOnShipping != false ? 1 : ThreadLocalRandom.current().nextInt(4, 10))) return CompletableFuture.completedFuture(null);
                    ++var3_3;
                    v0 = Request.send(var2_2);
                    if (!v0.isDone()) {
                        var6_6 = v0;
                        return var6_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$getSizeJson(io.trickle.task.sites.yeezy.Yeezy int io.vertx.ext.web.client.HttpRequest int java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (int)var1_1, (HttpRequest)var2_2, (int)var3_3, (CompletableFuture)var6_6, null, null, (int)1));
                    }
                    ** GOTO lbl16
                    break;
                }
            }
            case 1: {
                v0 = var4_4;
lbl16:
                // 2 sources

                if ((var4_4 = (HttpResponse)v0.join()) == null) ** GOTO lbl32
                var5_5 = (String)var4_4.body();
                if (var1_1 != 0) return CompletableFuture.completedFuture(null);
                if (var0.api.getSKU().equals("BY9611")) {
                    return CompletableFuture.completedFuture(null);
                }
                if (var5_5.contains("HTTP 403 - Forbidden")) {
                    var0.logger.warn("Failed to fetch sizes: state=BAN");
                } else {
                    if (var4_4.statusCode() == 200) {
                        var0.logger.info("Fetched sizes: state=OK");
                        return CompletableFuture.completedFuture(var5_5);
                    }
                    var0.logger.warn("Failed to fetch sizes: state={}", (Object)(var4_4.statusCode() + var4_4.statusMessage()).replaceAll(" ", "_"));
                }
                if (!(v1 = VertxUtil.hardCodedSleep(1000L)).isDone()) {
                    var6_6 = v1;
                    return var6_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$getSizeJson(io.trickle.task.sites.yeezy.Yeezy int io.vertx.ext.web.client.HttpRequest int java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (int)var1_1, (HttpRequest)var2_2, (int)var3_3, (CompletableFuture)var6_6, (HttpResponse)var4_4, (String)var5_5, (int)2));
                }
                ** GOTO lbl40
lbl32:
                // 1 sources

                var0.logger.warn("Failed to fetch sizes: state=NO_REPLY");
                var0.netLogWarn("Failed to fetch sizes: state=NO_REPLY");
                ** GOTO lbl6
            }
            case 2: {
                v1 = var4_4;
                v2 = var5_5;
                var5_5 = var6_6;
                var4_4 = v2;
lbl40:
                // 2 sources

                v1.join();
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$handlePOW(Yeezy var0, String var1_1, Function var2_2, String var3_3, CompletableFuture var4_4, String var5_5, Yeezy var6_6, String var7_7, Supplier var8_8, int var9_9, Object var10_11) {
        switch (var9_9) {
            case 0: {
                var2_2 = (Function<HttpResponse, Boolean>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, lambda$handlePOW$0(io.vertx.ext.web.client.HttpResponse ), (Lio/vertx/ext/web/client/HttpResponse;)Ljava/lang/Boolean;)();
                var3_3 = "https://www.yeezysupply.com" + Utils.quickParseFirst(var1_1, new Pattern[]{Yeezy.POW_PAGE_PATTERN});
                v0 = var0.execute("POW page", var2_2, (Supplier<HttpRequest>)LambdaMetafactory.metafactory(null, null, null, ()Ljava/lang/Object;, lambda$handlePOW$1(java.lang.String ), ()Lio/vertx/ext/web/client/HttpRequest;)((Yeezy)var0, (String)var3_3), null);
                if (!v0.isDone()) {
                    var5_5 = v0;
                    return var5_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$handlePOW(io.trickle.task.sites.yeezy.Yeezy java.lang.String java.util.function.Function java.lang.String java.util.concurrent.CompletableFuture java.lang.String io.trickle.task.sites.yeezy.Yeezy java.lang.String java.util.function.Supplier int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (String)var1_1, var2_2, (String)var3_3, (CompletableFuture)var5_5, null, null, null, null, (int)1));
                }
                ** GOTO lbl12
            }
            case 1: {
                v0 = var4_4;
lbl12:
                // 2 sources

                v0.join();
                var4_4 = var0.api.getCookies().getCookieValue("sec_cpt");
                while (true) {
                    v1 = var0;
                    v2 = "sensor";
                    v3 = null;
                    v4 = (Supplier<HttpRequest>)LambdaMetafactory.metafactory(null, null, null, ()Ljava/lang/Object;, lambda$handlePOW$2(), ()Lio/vertx/ext/web/client/HttpRequest;)((Yeezy)var0);
                    v5 = var0.sensorReqBuffer(false);
                    if (!v5.isDone()) {
                        var9_10 = v5;
                        var8_8 = v4;
                        var7_7 = v3;
                        var6_6 = v2;
                        var5_5 = v1;
                        return var9_10.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$handlePOW(io.trickle.task.sites.yeezy.Yeezy java.lang.String java.util.function.Function java.lang.String java.util.concurrent.CompletableFuture java.lang.String io.trickle.task.sites.yeezy.Yeezy java.lang.String java.util.function.Supplier int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (String)var1_1, var2_2, (String)var3_3, (CompletableFuture)var9_10, (String)var4_4, (Yeezy)var5_5, (String)var6_6, var8_8, (int)2));
                    }
                    ** GOTO lbl36
                    break;
                }
            }
            case 2: {
                v1 = var6_6;
                v2 = var7_7;
                v3 = null;
                v4 = var8_8;
                v5 = var4_4;
                var4_4 = var5_5;
lbl36:
                // 2 sources

                if (!(v6 = super.execute(v2, v3, v4, v5.join())).isDone()) {
                    var5_5 = v6;
                    return var5_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$handlePOW(io.trickle.task.sites.yeezy.Yeezy java.lang.String java.util.function.Function java.lang.String java.util.concurrent.CompletableFuture java.lang.String io.trickle.task.sites.yeezy.Yeezy java.lang.String java.util.function.Supplier int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (String)var1_1, var2_2, (String)var3_3, (CompletableFuture)var5_5, (String)var4_4, null, null, null, (int)3));
                }
                ** GOTO lbl43
            }
            case 3: {
                v6 = var4_4;
                var4_4 = var5_5;
lbl43:
                // 2 sources

                v6.join();
                v7 = VertxUtil.hardCodedSleep(2000L);
                if (!v7.isDone()) {
                    var5_5 = v7;
                    return var5_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$handlePOW(io.trickle.task.sites.yeezy.Yeezy java.lang.String java.util.function.Function java.lang.String java.util.concurrent.CompletableFuture java.lang.String io.trickle.task.sites.yeezy.Yeezy java.lang.String java.util.function.Supplier int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (String)var1_1, var2_2, (String)var3_3, (CompletableFuture)var5_5, (String)var4_4, null, null, null, (int)4));
                }
                ** GOTO lbl53
            }
            case 4: {
                v7 = var4_4;
                var4_4 = var5_5;
lbl53:
                // 2 sources

                v7.join();
                if (var0.api.getCookies().getCookieValue("sec_cpt") != null) {
                    if (var0.api.getCookies().getCookieValue("sec_cpt").equals(var4_4)) ** continue;
                }
                if (!(v8 = var0.execute("POW completion", var2_2, (Supplier<HttpRequest>)LambdaMetafactory.metafactory(null, null, null, ()Ljava/lang/Object;, verifyPage(), ()Lio/vertx/ext/web/client/HttpRequest;)((YeezyAPI)var0.api), null)).isDone()) {
                    var5_5 = v8;
                    return var5_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$handlePOW(io.trickle.task.sites.yeezy.Yeezy java.lang.String java.util.function.Function java.lang.String java.util.concurrent.CompletableFuture java.lang.String io.trickle.task.sites.yeezy.Yeezy java.lang.String java.util.function.Supplier int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (String)var1_1, var2_2, (String)var3_3, (CompletableFuture)var5_5, (String)var4_4, null, null, null, (int)5));
                }
                ** GOTO lbl67
            }
            case 5: {
                v8 = var4_4;
                var4_4 = var5_5;
lbl67:
                // 2 sources

                v8.join();
                return CompletableFuture.completedFuture(null);
            }
        }
        throw new IllegalArgumentException();
    }

    public static int lambda$fillSizes$5(Object object, Object object2) {
        Integer n = ((JsonObject)object).getInteger("availability", Integer.valueOf(0));
        Integer n2 = ((JsonObject)object2).getInteger("availability", Integer.valueOf(0));
        return n2.compareTo(n);
    }

    public CompletableFuture handlePOW(String string) {
        Function<HttpResponse, Boolean> function = Yeezy::lambda$handlePOW$0;
        String string2 = "https://www.yeezysupply.com" + Utils.quickParseFirst(string, POW_PAGE_PATTERN);
        CompletableFuture completableFuture = this.execute("POW page", function, () -> this.lambda$handlePOW$1(string2), null);
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$handlePOW(this, string, function, string2, completableFuture2, null, null, null, (Supplier)null, 1, arg_0));
        }
        completableFuture.join();
        String string3 = this.api.getCookies().getCookieValue("sec_cpt");
        do {
            Yeezy yeezy = this;
            Supplier<HttpRequest> supplier = this::lambda$handlePOW$2;
            CompletableFuture completableFuture3 = this.sensorReqBuffer(false);
            if (!completableFuture3.isDone()) {
                CompletableFuture completableFuture4 = completableFuture3;
                Supplier<HttpRequest> supplier2 = supplier;
                Function function2 = null;
                String string4 = "sensor";
                Yeezy yeezy2 = yeezy;
                return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$handlePOW(this, string, function, string2, completableFuture4, string3, yeezy2, string4, supplier2, 2, arg_0));
            }
            CompletableFuture completableFuture5 = super.execute("sensor", null, supplier, completableFuture3.join());
            if (!completableFuture5.isDone()) {
                CompletableFuture completableFuture6 = completableFuture5;
                return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$handlePOW(this, string, function, string2, completableFuture6, string3, null, null, (Supplier)null, 3, arg_0));
            }
            completableFuture5.join();
            CompletableFuture completableFuture7 = VertxUtil.hardCodedSleep(2000L);
            if (!completableFuture7.isDone()) {
                CompletableFuture completableFuture8 = completableFuture7;
                return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$handlePOW(this, string, function, string2, completableFuture8, string3, null, null, (Supplier)null, 4, arg_0));
            }
            completableFuture7.join();
            if (this.api.getCookies().getCookieValue("sec_cpt") == null) break;
        } while (this.api.getCookies().getCookieValue("sec_cpt").equals(string3));
        CompletableFuture completableFuture9 = this.execute("POW completion", function, this.api::verifyPage, null);
        if (!completableFuture9.isDone()) {
            CompletableFuture completableFuture10 = completableFuture9;
            return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$handlePOW(this, string, function, string2, completableFuture10, string3, null, null, (Supplier)null, 5, arg_0));
        }
        completableFuture9.join();
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture sendBasic(HttpRequest httpRequest, String string) {
        int n = 0;
        while (this.running) {
            if (n > 100) return CompletableFuture.completedFuture(null);
            ++n;
            try {
                CompletableFuture completableFuture = Request.send(httpRequest);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$sendBasic(this, httpRequest, string, n, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse != null && httpResponse.statusCode() < 300 & !((String)httpResponse.body()).contains("HTTP 403 - Forbidden")) {
                    this.logger.info("Fetched step='{}': status={}", (Object)string, (Object)httpResponse.statusCode());
                    return CompletableFuture.completedFuture((String)httpResponse.body());
                }
                if (httpResponse != null && ((String)httpResponse.body()).contains("HTTP 403 - Forbidden")) {
                    this.logger.warn("BAN at step='{}'", (Object)string);
                    return CompletableFuture.completedFuture(null);
                }
                if (httpResponse != null && httpResponse.statusCode() == 404) {
                    this.logger.warn("Unable to find page. Will continue step='{}'", (Object)string);
                    return CompletableFuture.completedFuture(null);
                }
                this.logger.warn("Retrying to fetch step='{}'", (Object)string);
                CompletableFuture completableFuture3 = VertxUtil.randomSleep(3000L);
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$sendBasic(this, httpRequest, string, n, completableFuture4, httpResponse, null, 2, arg_0));
                }
                completableFuture3.join();
            }
            catch (Throwable throwable) {
                this.logger.warn("Error in sending {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = VertxUtil.sleep(this.task.getRetryDelay());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture5 = completableFuture;
                    return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$sendBasic(this, httpRequest, string, n, completableFuture5, null, throwable, 3, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    static {
        RETRY_BOUND = 3000;
        SENSOR_URL_PATTERN = Pattern.compile("<script type=\"text/javascript\"\\s\\ssrc=\"(.*?)\"");
        V3_COOKIE_NAME = Engine.get().getClientConfiguration().getString("cookieV3", "xhwUqgFqfW88H50");
        QUEUE_POOL_INTERVAL = Engine.get().getClientConfiguration().getInteger("queuePollTime", Integer.valueOf(1250));
        clock = new AtomicLong(0L);
        holder = new AtomicReference<Object>(null);
        browserHarvesters = new BrowserHarvesterController(VertxSingleton.INSTANCE.get(), Storage.HARVESTER_COUNT_YS);
        autoSolveHarvesters = new AutosolveHarvesterController(VertxSingleton.INSTANCE.get());
        POW_PAGE_PATTERN = Pattern.compile("branding_url_content\":\"(.*?)\"");
        SIGNUP_ID_PATTERN = Pattern.compile("yeezySupplySignupFormComponentId\\\\\":\\\\\"(.*?)\\\\\"");
        successfulCartCount = 0;
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$submitShippingAndBilling(Yeezy var0, HttpRequest var1_1, String var2_2, CompletableFuture var3_3, HttpResponse var4_5, Exception var5_6, int var6_7, Object var7_8) {
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
     * Unable to fully structure code
     */
    public static CompletableFuture async$smartUA(Yeezy var0, YeezyAPI var1_1, CompletableFuture var2_2, int var3_3, Object var4_4) {
        switch (var3_3) {
            case 0: {
                if (var0.isFirewallOn() != false) return CompletableFuture.completedFuture(null);
                if (!(var0.api.getPixelAPI() instanceof HawkAPI)) ** GOTO lbl12
                v0 = var0.api;
                v1 = ((HawkAPI)var0.api.getPixelAPI()).updateUserAgent();
                if (!v1.isDone()) {
                    var2_2 = v1;
                    var1_1 = v0;
                    return var2_2.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$smartUA(io.trickle.task.sites.yeezy.Yeezy io.trickle.task.sites.yeezy.YeezyAPI java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (YeezyAPI)var1_1, (CompletableFuture)var2_2, (int)1));
                }
                ** GOTO lbl23
lbl12:
                // 1 sources

                if (var0.api.getPixelAPI() instanceof GaneshAPI == false) return CompletableFuture.completedFuture(null);
                v2 = var0.api;
                v3 = ((GaneshAPI)var0.api.getPixelAPI()).updateUserAgent();
                if (!v3.isDone()) {
                    var2_2 = v3;
                    var1_1 = v2;
                    return var2_2.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$smartUA(io.trickle.task.sites.yeezy.Yeezy io.trickle.task.sites.yeezy.YeezyAPI java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (YeezyAPI)var1_1, (CompletableFuture)var2_2, (int)2));
                }
                ** GOTO lbl28
            }
            case 1: {
                v0 = var1_1;
                v1 = var2_2;
lbl23:
                // 2 sources

                v0.userAgent = (String)v1.join();
                return CompletableFuture.completedFuture(null);
            }
            case 2: {
                v2 = var1_1;
                v3 = var2_2;
lbl28:
                // 2 sources

                v2.userAgent = (String)v3.join();
                return CompletableFuture.completedFuture(null);
            }
        }
        throw new IllegalArgumentException();
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$sendSensor(Yeezy var0, HttpRequest var1_1, int var2_2, CompletableFuture var3_3, Buffer var4_5, HttpResponse var5_6, Exception var6_7, int var7_8, Object var8_9) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [8[CATCHBLOCK]], but top level block is 11[UNCONDITIONALDOLOOP]
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

    public CompletableFuture prepareCart() {
        CompletableFuture completableFuture = this.getProductPage(true);
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$prepareCart(this, completableFuture2, null, 1, arg_0));
        }
        String string = (String)completableFuture.join();
        this.updateSensorUrlFromHTML(string);
        this.api.updateDocumentUrl("https://www.yeezysupply.com/product/" + this.api.getSKU());
        this.api.getCookies().put("utag_main", this.api.fetchUtag(), ".yeezysupply.com");
        CompletableFuture completableFuture3 = Request.execute(this.api.akamaiScript(true));
        if (!completableFuture3.isDone()) {
            CompletableFuture completableFuture4 = completableFuture3;
            return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$prepareCart(this, completableFuture4, string, 2, arg_0));
        }
        completableFuture3.join();
        CompletableFuture completableFuture5 = this.sendBasic(this.api.secondaryProdAPI(), "Prod Info #1");
        if (!completableFuture5.isDone()) {
            CompletableFuture completableFuture6 = completableFuture5;
            return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$prepareCart(this, completableFuture6, string, 3, arg_0));
        }
        completableFuture5.join();
        if (this.isFirstIteration) {
            this.isFirstIteration = false;
            CompletableFuture completableFuture7 = Request.executeTillOk(this.api.newsletter(Utils.quickParseFirst(string, SIGNUP_ID_PATTERN)));
            if (!completableFuture7.isDone()) {
                CompletableFuture completableFuture8 = completableFuture7;
                return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$prepareCart(this, completableFuture8, string, 4, arg_0));
            }
            completableFuture7.join();
        }
        CompletableFuture completableFuture9 = this.fillSizes();
        if (!completableFuture9.isDone()) {
            CompletableFuture completableFuture10 = completableFuture9;
            return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$prepareCart(this, completableFuture10, string, 5, arg_0));
        }
        completableFuture9.join();
        CompletableFuture completableFuture11 = this.postSensors();
        if (!completableFuture11.isDone()) {
            CompletableFuture completableFuture12 = completableFuture11;
            return ((CompletableFuture)completableFuture12.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$prepareCart(this, completableFuture12, string, 6, arg_0));
        }
        completableFuture11.join();
        if (this.isPassedFW) return CompletableFuture.completedFuture(null);
        this.logger.info("Preparing to cart with pre-emptive step=x00433");
        CompletableFuture completableFuture13 = Request.executeTillOk(this.api.emptyBasket(true, this.authorization));
        if (!completableFuture13.isDone()) {
            CompletableFuture completableFuture14 = completableFuture13;
            return ((CompletableFuture)completableFuture14.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$prepareCart(this, completableFuture14, string, 7, arg_0));
        }
        completableFuture13.join();
        this.visitedBaskets = true;
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture sensorReqBuffer(boolean bl) {
        Buffer buffer;
        if (this.task.getMode().contains("2")) {
            String string;
            String string2;
            try {
                string2 = this.api.getCookies().getCookieValue("_abck");
                string = this.api.getCookies().getCookieValue("bm_sz");
            }
            catch (Throwable throwable) {
                this.logger.error("Failed to set value for sensor api: {}", (Object)throwable.getMessage());
                string2 = "";
                string = "";
            }
            CompletableFuture completableFuture = ((HawkAPI)this.api.getPixelAPI()).getSensorPayload(string2, string, this.task.getKeywords()[0].toUpperCase(), bl);
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$sensorReqBuffer(this, (int)(bl ? 1 : 0), string2, string, completableFuture2, 1, arg_0));
            }
            String string3 = (String)completableFuture.join();
            buffer = new JsonObject().put("sensor_data", (Object)string3).toBuffer();
            return CompletableFuture.completedFuture(buffer);
        }
        if (this.task.getMode().contains("3")) {
            String string;
            String string4;
            try {
                string4 = this.api.getCookies().getCookieValue("_abck");
                string = this.api.getCookies().getCookieValue("bm_sz");
            }
            catch (Throwable throwable) {
                this.logger.error("Failed to set value for sensor api: {}", (Object)throwable.getMessage());
                string4 = "";
                string = "";
            }
            CompletableFuture completableFuture = ((GaneshAPI)this.api.getPixelAPI()).getSensorPayload(string4, string, this.task.getKeywords()[0].toUpperCase(), bl);
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture3 = completableFuture;
                return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$sensorReqBuffer(this, (int)(bl ? 1 : 0), string4, string, completableFuture3, 2, arg_0));
            }
            String string5 = (String)completableFuture.join();
            buffer = new JsonObject().put("sensor_data", (Object)string5).toBuffer();
            return CompletableFuture.completedFuture(buffer);
        }
        CompletableFuture completableFuture = this.api.getTrickleSensor();
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture4 = completableFuture;
            return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$sensorReqBuffer(this, (int)(bl ? 1 : 0), null, null, completableFuture4, 3, arg_0));
        }
        String string = (String)completableFuture.join();
        buffer = new JsonObject(string).toBuffer();
        return CompletableFuture.completedFuture(buffer);
    }

    public CompletableFuture waitForSale() {
        HttpRequest httpRequest = this.api.waitingRoomConfig();
        this.logger.info("Fetching sale status.");
        this.netLogInfo("Fetching sale status.");
        while (this.running) {
            try {
                CompletableFuture completableFuture = Request.send(httpRequest);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$waitForSale(this, httpRequest, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse != null) {
                    if (((String)httpResponse.body()).contains("HTTP 403 - Forbidden")) {
                        this.logger.warn("Failed to check sale status: state=PROXY_BAN");
                        CompletableFuture completableFuture3 = VertxUtil.sleep(this.task.getMonitorDelay());
                        if (!completableFuture3.isDone()) {
                            CompletableFuture completableFuture4 = completableFuture3;
                            return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$waitForSale(this, httpRequest, completableFuture4, httpResponse, null, 2, arg_0));
                        }
                        completableFuture3.join();
                        return CompletableFuture.failedFuture(new Exception("403 ERROR"));
                    }
                    if (httpResponse.statusCode() != 200) continue;
                    if (((String)httpResponse.body()).toLowerCase().contains("sale_started")) return CompletableFuture.completedFuture(null);
                    if (this.task.getMode().contains("noqueue")) {
                        return CompletableFuture.completedFuture(null);
                    }
                    this.logger.info("Sale not live yet. Waiting...");
                    CompletableFuture completableFuture5 = VertxUtil.randomSleep(8000L);
                    if (!completableFuture5.isDone()) {
                        CompletableFuture completableFuture6 = completableFuture5;
                        return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$waitForSale(this, httpRequest, completableFuture6, httpResponse, null, 3, arg_0));
                    }
                    completableFuture5.join();
                    continue;
                }
                this.logger.warn("Failed to fetch sale status: state=NO_REPLY");
            }
            catch (Exception exception) {
                this.logger.warn("Failed to fetch sale status: {}", (Object)exception.getMessage());
                this.netLogError("Failed to fetch sale status: message='" + exception.getMessage() + "'");
                CompletableFuture completableFuture = VertxUtil.randomSleep(this.task.getRetryDelay());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$waitForSale(this, httpRequest, completableFuture7, null, exception, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.failedFuture(new Exception("403 ERROR"));
    }

    public CompletableFuture requestCaptcha() {
        if (this.harvesterID == null) {
            this.harvesterID = this.harvesters.allocate();
            this.logger.info("Got harvester id={}", (Object)this.harvesterID);
        } else {
            this.harvesters.shouldSwap(this.harvesterID).ifPresent(this::lambda$requestCaptcha$3);
        }
        this.logger.info("Requesting captcha token from harvester={}", (Object)this.harvesterID);
        ContextCompletableFuture contextCompletableFuture = new ContextCompletableFuture();
        this.vertx.eventBus().request(this.harvesterID, (Object)("https://www.yeezysupply.com/products/" + this.api.getSKU()), arg_0 -> this.lambda$requestCaptcha$4(contextCompletableFuture, arg_0));
        return contextCompletableFuture;
    }

    public CompletableFuture postSensorsTilNoChallenge() {
        String string = "";
        CompletableFuture completableFuture = this.sendSensor(this.api.postSensor(true), true);
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$postSensorsTilNoChallenge(this, string, completableFuture2, 1, arg_0));
        }
        completableFuture.join();
        CompletableFuture completableFuture3 = VertxUtil.randomSleep(1000L);
        if (!completableFuture3.isDone()) {
            CompletableFuture completableFuture4 = completableFuture3;
            return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$postSensorsTilNoChallenge(this, string, completableFuture4, 2, arg_0));
        }
        completableFuture3.join();
        CompletableFuture completableFuture5 = this.sendSensor(this.api.postSensor(true), true);
        if (!completableFuture5.isDone()) {
            CompletableFuture completableFuture6 = completableFuture5;
            return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$postSensorsTilNoChallenge(this, string, completableFuture6, 3, arg_0));
        }
        completableFuture5.join();
        while (this.api.getCookies().getCookieValue("_abck").contains("||")) {
            CompletableFuture completableFuture7 = this.smartSleep(1000L);
            if (!completableFuture7.isDone()) {
                CompletableFuture completableFuture8 = completableFuture7;
                return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$postSensorsTilNoChallenge(this, string, completableFuture8, 4, arg_0));
            }
            completableFuture7.join();
            CompletableFuture completableFuture9 = this.sendSensor(this.api.postSensor(true), true);
            if (!completableFuture9.isDone()) {
                CompletableFuture completableFuture10 = completableFuture9;
                return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$postSensorsTilNoChallenge(this, string, completableFuture10, 5, arg_0));
            }
            string = (String)completableFuture9.join();
            if (this.logger.isDebugEnabled()) {
                this.logger.debug(string);
            } else {
                if (string != null && string.contains("false")) {
                    this.logger.info("YS site down!");
                    return CompletableFuture.completedFuture(null);
                }
                if (!this.api.getCookies().getCookieValue("_abck").contains("||")) {
                    this.logger.info("Passed challenge l={}", (Object)this.api.getCookies().getCookieValue("_abck").length());
                    return CompletableFuture.completedFuture(null);
                }
                this.logger.info("Handling sensor step - l={}", (Object)this.api.getCookies().getCookieValue("_abck").length());
            }
            ++this.sensorPosts;
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture submitShippingAndBilling(HttpRequest httpRequest) {
        this.instanceProfile = this.getOrLoadProfile();
        String string = "{\"customer\":{\"email\":\"" + this.instanceProfile.getEmail() + "\",\"receiveSmsUpdates\":false},\"shippingAddress\":{\"country\":\"US\",\"firstName\":\"" + this.instanceProfile.getFirstName().substring(0, 1).toUpperCase() + this.instanceProfile.getFirstName().substring(1).toLowerCase() + "\",\"lastName\":\"" + this.instanceProfile.getLastName().substring(0, 1).toUpperCase() + this.instanceProfile.getLastName().substring(1).toLowerCase() + "\",\"address1\":\"" + this.instanceProfile.getAddress1() + "\",\"address2\":\"" + this.instanceProfile.getAddress2() + "\",\"city\":\"" + this.instanceProfile.getCity().substring(0, 1).toUpperCase() + this.instanceProfile.getCity().substring(1).toLowerCase() + "\",\"stateCode\":\"" + this.instanceProfile.getState() + "\",\"zipcode\":\"" + this.instanceProfile.getZip() + "\",\"phoneNumber\":\"" + this.instanceProfile.getPhone() + "\"},\"billingAddress\":{\"country\":\"US\",\"firstName\":\"" + this.instanceProfile.getFirstName().substring(0, 1).toUpperCase() + this.instanceProfile.getFirstName().substring(1).toLowerCase() + "\",\"lastName\":\"" + this.instanceProfile.getLastName().substring(0, 1).toUpperCase() + this.instanceProfile.getLastName().substring(1).toLowerCase() + "\",\"address1\":\"" + this.instanceProfile.getAddress1() + "\",\"address2\":\"" + this.instanceProfile.getAddress2() + "\",\"city\":\"" + this.instanceProfile.getCity().substring(0, 1).toUpperCase() + this.instanceProfile.getCity().substring(1).toLowerCase() + "\",\"stateCode\":\"" + this.instanceProfile.getState() + "\",\"zipcode\":\"" + this.instanceProfile.getZip() + "\",\"phoneNumber\":\"" + this.instanceProfile.getPhone() + "\"},\"methodList\":[{\"id\":\"2ndDay-1\",\"shipmentId\":\"me\",\"collectionPeriod\":\"\",\"deliveryPeriod\":\"\"}],\"newsletterSubscription\":true}";
        while (true) {
            this.checkHmac();
            try {
                this.api.updateUtagUrl("https://www.yeezysupply.com/delivery");
                this.api.getCookies().put("utag_main", this.api.fetchUtag(), ".yeezysupply.com");
                CompletableFuture completableFuture = Request.send(httpRequest, Buffer.buffer((String)string));
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$submitShippingAndBilling(this, httpRequest, string, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse != null && httpResponse.statusCode() == 501) {
                    this.logger.info("ShippingBilling (weak task proxy): status={} r={}", (Object)httpResponse.statusCode(), (Object)((String)httpResponse.body()).replace("\n", ""));
                    this.isPassedFW = true;
                    this.isBannedOnShipping = true;
                    CompletableFuture completableFuture3 = this.prepareCart();
                    if (!completableFuture3.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture3;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$submitShippingAndBilling(this, httpRequest, string, completableFuture4, httpResponse, null, 2, arg_0));
                    }
                    completableFuture3.join();
                    CompletableFuture completableFuture5 = VertxUtil.randomSleep(this.task.getRetryDelay());
                    if (!completableFuture5.isDone()) {
                        CompletableFuture completableFuture6 = completableFuture5;
                        return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$submitShippingAndBilling(this, httpRequest, string, completableFuture6, httpResponse, null, 3, arg_0));
                    }
                    completableFuture5.join();
                    continue;
                }
                if (httpResponse != null && httpResponse.statusCode() != 200) {
                    this.logger.warn("Failed to submit shipping & billing: status={} r={}", (Object)httpResponse.statusCode(), httpResponse.body());
                    this.netLogWarn("Failed to submit shipping & billing: status=" + httpResponse.statusCode() + " body='" + (String)httpResponse.body() + "'");
                    if (((String)httpResponse.body()).contains("The shipping address postal code is blacklisted")) {
                        this.logger.info("Banned zipcode found -> zipcode={}", (Object)this.instanceProfile.getZip());
                        this.shouldRotate = true;
                    }
                } else if (httpResponse != null) {
                    this.isBannedOnShipping = false;
                    String string2 = (String)httpResponse.body();
                    this.logger.info("ShippingBilling: status={} r={}", (Object)httpResponse.statusCode(), (Object)((String)httpResponse.body()).replace("\n", ""));
                    this.netLogInfo("ShippingBilling: status=" + httpResponse.statusCode() + " body='" + (String)httpResponse.body() + "'");
                    if (httpResponse.getHeader("authorization") == null) {
                        this.logger.warn("Fake shipping (animal)");
                        this.netLogInfo("Fake shipping (animal) body='" + (String)httpResponse.body() + "'");
                        Analytics.log("Fake shipping (animal)", this.task, this.api.getCookies().getCookieValueLength("_abck"), this.sensorPosts, this.visitedBaskets, this.api.userAgent);
                    } else {
                        if (string2.contains("basketId")) {
                            this.logger.info("Submit shipping & billing SUCCESSFUL!: r={}", (Object)string2);
                            this.netLogInfo("Submit shipping & billing SUCCESSFUL! body='" + (String)httpResponse.body() + "'");
                            return CompletableFuture.completedFuture(string2);
                        }
                        this.logger.warn("Error shipping & billing:  reason={} headers={}", (Object)string2.contains("HTTP 403 - Forbidden"), (Object)httpResponse.headers().toString());
                    }
                }
                CompletableFuture completableFuture7 = VertxUtil.randomSleep(this.task.getRetryDelay());
                if (!completableFuture7.isDone()) {
                    CompletableFuture completableFuture8 = completableFuture7;
                    return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$submitShippingAndBilling(this, httpRequest, string, completableFuture8, httpResponse, null, 4, arg_0));
                }
                completableFuture7.join();
                CompletableFuture completableFuture9 = this.postSensors();
                if (!completableFuture9.isDone()) {
                    CompletableFuture completableFuture10 = completableFuture9;
                    return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$submitShippingAndBilling(this, httpRequest, string, completableFuture10, httpResponse, null, 5, arg_0));
                }
                completableFuture9.join();
                continue;
            }
            catch (Exception exception) {
                this.logger.error("Error occurred on sending shipping and billing: '{}'", (Object)exception.getMessage());
                this.netLogError("Error occurred on sending shipping and billing: message=" + exception.getMessage());
                CompletableFuture completableFuture = VertxUtil.randomSleep(3000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture11 = completableFuture;
                    return ((CompletableFuture)completableFuture11.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$submitShippingAndBilling(this, httpRequest, string, completableFuture11, null, exception, 6, arg_0));
                }
                completableFuture.join();
                continue;
            }
            break;
        }
    }

    public CompletableFuture handleAfter3DS(Window3DS2 window3DS2) {
        int n = 0;
        while (this.running) {
            if (n > 10) return CompletableFuture.completedFuture(null);
            try {
                JsonObject jsonObject = new JsonObject();
                jsonObject.put("orderId", (Object)this.orderID);
                window3DS2.getUploadValues().forEach((arg_0, arg_1) -> ((JsonObject)jsonObject).put(arg_0, arg_1));
                HttpRequest httpRequest = this.api.handleAfter3DS(window3DS2.getEncodedData(), this.authorization, window3DS2.getTermURI());
                CompletableFuture completableFuture = Request.send(httpRequest, jsonObject.toBuffer());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$handleAfter3DS(this, window3DS2, n, jsonObject, httpRequest, completableFuture2, null, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                this.logger.info("After 3DS status={} : r={}", (Object)httpResponse.statusCode(), (Object)httpResponse.bodyAsString());
                String string = httpResponse.bodyAsString();
                if (string == null) continue;
                if (string.contains("orderToken")) {
                    this.logger.info("Successfully checked out!");
                    Analytics.success(this.task, new JsonObject(string), this.api.proxyString());
                    Analytics.log("Successfully Checked Out", this.task, this.api.getCookies().getCookieValueLength("_abck"), this.sensorPosts, this.visitedBaskets, this.api.userAgent);
                    return CompletableFuture.completedFuture(true);
                }
                if (string.contains("basket_not_found_exception") || string.contains("{\"invalidFields\":[\"Product items\"]")) {
                    if (string.contains("basket_not_found_exception")) {
                        this.logger.warn("Failed to process payment: state='{}' - status={}", (Object)"Cart expiry", (Object)httpResponse.statusCode());
                        this.netLogWarn("Failed to process payment: state='Cart expiry' status=" + httpResponse.statusCode());
                    } else {
                        this.logger.warn("Failed to process payment: state='{}' - status={}", (Object)"Cart jacked", (Object)httpResponse.statusCode());
                        this.netLogWarn("Failed to process payment: state='Cart Jacked' status=" + httpResponse.statusCode());
                    }
                    CompletableFuture completableFuture3 = this.prepareCart();
                    if (!completableFuture3.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture3;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$handleAfter3DS(this, window3DS2, n, jsonObject, httpRequest, completableFuture4, httpResponse, string, null, 2, arg_0));
                    }
                    completableFuture3.join();
                    CompletableFuture completableFuture5 = this.cart();
                    if (!completableFuture5.isDone()) {
                        CompletableFuture completableFuture6 = completableFuture5;
                        return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$handleAfter3DS(this, window3DS2, n, jsonObject, httpRequest, completableFuture6, httpResponse, string, null, 3, arg_0));
                    }
                    completableFuture5.join();
                    this.logger.info("Recovered cart jack (3DS)");
                    this.netLogInfo("Recovered cart jack (3DS)");
                    Analytics.log("Recovered cart jack (mid 3ds)", this.task, this.api.getCookies().getCookieValueLength("_abck"), this.sensorPosts, this.visitedBaskets, this.api.userAgent);
                    CompletableFuture completableFuture7 = this.checkout();
                    if (!completableFuture7.isDone()) {
                        CompletableFuture completableFuture8 = completableFuture7;
                        return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$handleAfter3DS(this, window3DS2, n, jsonObject, httpRequest, completableFuture8, httpResponse, string, null, 4, arg_0));
                    }
                    completableFuture7.join();
                    return CompletableFuture.completedFuture(null);
                }
                if (string.contains("HTTP 403 - Forbidden")) {
                    this.logger.warn("Failed to process payment: state='{}' - status={}", (Object)"Blocked by 403 body", (Object)httpResponse.statusCode());
                    this.netLogWarn("Failed to process payment: state='Blocked by 403 body' status=" + httpResponse.statusCode());
                    CompletableFuture completableFuture9 = this.postSensors();
                    if (!completableFuture9.isDone()) {
                        CompletableFuture completableFuture10 = completableFuture9;
                        return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$handleAfter3DS(this, window3DS2, n, jsonObject, httpRequest, completableFuture10, httpResponse, string, null, 5, arg_0));
                    }
                    completableFuture9.join();
                    ++n;
                } else {
                    if (string.length() == 0) {
                        this.logger.warn("Failed to process payment: state='{}' - status={}", (Object)"Blocked by FW", (Object)httpResponse.statusCode());
                        this.netLogWarn("Failed to process payment: state='Blocked by FW' status=" + httpResponse.statusCode());
                        return CompletableFuture.completedFuture(false);
                    }
                    if (httpResponse.statusCode() == 403) {
                        this.logger.warn("Failed to process payment: state='{}' - status={}", (Object)"Bad cookie", (Object)httpResponse.statusCode());
                        this.netLogWarn("Failed to process payment: state='Bad cookie' status=" + httpResponse.statusCode());
                        CompletableFuture completableFuture11 = this.postSensors();
                        if (!completableFuture11.isDone()) {
                            CompletableFuture completableFuture12 = completableFuture11;
                            return ((CompletableFuture)completableFuture12.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$handleAfter3DS(this, window3DS2, n, jsonObject, httpRequest, completableFuture12, httpResponse, string, null, 6, arg_0));
                        }
                        completableFuture11.join();
                        ++n;
                    } else {
                        if (string.contains("confirm.error.paymentdeclined.fraud") || string.contains("hook_status_exception")) {
                            this.handleFailureWebhooks("Fraud", string);
                            this.logger.warn("Failed to process payment: state='{}' - status={}", (Object)"Fraud Decline", (Object)httpResponse.statusCode());
                            this.netLogWarn("Failed to process payment: state='Fraud Decline' status=" + httpResponse.statusCode());
                            return CompletableFuture.completedFuture(false);
                        }
                        if (string.contains("confirm.error.paymentdeclined.not_enough_balance")) {
                            this.handleFailureWebhooks("Card Decline (balance)", string);
                            this.logger.warn("Failed to process payment: state='{}' - status={}", (Object)"Card Decline (balance)", (Object)httpResponse.statusCode());
                            this.netLogWarn("Failed to process payment: state='Card Decline (balance)' status=" + httpResponse.statusCode());
                            return CompletableFuture.completedFuture(false);
                        }
                        if (string.contains("paymentdeclined")) {
                            this.logger.warn("Failed to process payment: state='{}' - status={}", (Object)"Card Decline", (Object)httpResponse.statusCode());
                            this.netLogWarn("Failed to process payment: state='Card Decline' status=" + httpResponse.statusCode());
                            this.handleFailureWebhooks("Card Decline", string);
                            return CompletableFuture.completedFuture(false);
                        }
                        if (string.contains("missing properties")) {
                            this.logger.warn("Failed to process payment: state='{}' - status={}", (Object)"Invalid shipping or billing", (Object)httpResponse.statusCode());
                            this.netLogWarn("Failed to process payment: state='Invalid shipping or billing' status=" + httpResponse.statusCode());
                            this.handleFailureWebhooks("Invalid shipping or billing", string);
                            return CompletableFuture.completedFuture(false);
                        }
                        if (string.contains("Invalid payment verification request") && string.contains("InvalidDataException")) {
                            this.logger.warn("Failed to process payment: state='{}' - status={}", (Object)"3DS Error", (Object)string);
                            this.handleFailureWebhooks("3DS Auth Fail", string);
                            return CompletableFuture.completedFuture(false);
                        }
                        if (!string.contains("Product item not available") && !string.contains("Basket has been removed")) {
                            this.logger.warn("Failed to process payment: state='{}' - status={}", (Object)"Unknown payment error", (Object)string);
                            this.handleFailureWebhooks("Unknown", string);
                            return CompletableFuture.completedFuture(false);
                        }
                    }
                }
                if (httpResponse.statusCode() >= 500) {
                    this.logger.warn("Failed to process payment: state='{}' - status={}", (Object)"Site down", (Object)httpResponse.statusCode());
                    this.netLogWarn("Failed to process payment: state='Site down' status=" + httpResponse.statusCode());
                    continue;
                }
                if (httpResponse.statusCode() != 400) {
                    this.handleFailureWebhooks("Out of stock", string);
                    this.logger.warn("Failed to process payment: state='{}' - status={}", (Object)"Out of stock", (Object)httpResponse.statusCode());
                    return CompletableFuture.completedFuture(false);
                }
                this.logger.warn("Failed processing 3ds cookie");
                CompletableFuture completableFuture13 = this.postSensors();
                if (!completableFuture13.isDone()) {
                    CompletableFuture completableFuture14 = completableFuture13;
                    return ((CompletableFuture)completableFuture14.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$handleAfter3DS(this, window3DS2, n, jsonObject, httpRequest, completableFuture14, httpResponse, string, null, 7, arg_0));
                }
                completableFuture13.join();
                CompletableFuture completableFuture15 = VertxUtil.randomSleep(this.task.getRetryDelay());
                if (!completableFuture15.isDone()) {
                    CompletableFuture completableFuture16 = completableFuture15;
                    return ((CompletableFuture)completableFuture16.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$handleAfter3DS(this, window3DS2, n, jsonObject, httpRequest, completableFuture16, httpResponse, string, null, 8, arg_0));
                }
                completableFuture15.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error occurred handling after confirmation: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = VertxUtil.randomSleep(3000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture17 = completableFuture;
                    return ((CompletableFuture)completableFuture17.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$handleAfter3DS(this, window3DS2, n, null, null, completableFuture17, null, null, throwable, 9, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public static JsonObject lambda$fillSizes$6(Object object) {
        return (JsonObject)object;
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$sensorReqBuffer(Yeezy var0, int var1_1, String var2_2, String var3_3, CompletableFuture var4_4, int var5_5, Object var6_10) {
        switch (var5_5) {
            case 0: {
                if (!var0.task.getMode().contains("2")) ** GOTO lbl16
                try {
                    var3_3 = var0.api.getCookies().getCookieValue("_abck");
                    var4_4 = var0.api.getCookies().getCookieValue("bm_sz");
                }
                catch (Throwable var5_6) {
                    var0.logger.error("Failed to set value for sensor api: {}", (Object)var5_6.getMessage());
                    var3_3 = "";
                    var4_4 = "";
                }
                if (!(v0 = ((HawkAPI)var0.api.getPixelAPI()).getSensorPayload(var3_3, (String)var4_4, var0.task.getKeywords()[0].toUpperCase(), (boolean)var1_1)).isDone()) {
                    var6_10 = v0;
                    return var6_10.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$sensorReqBuffer(io.trickle.task.sites.yeezy.Yeezy int java.lang.String java.lang.String java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (int)var1_1, (String)var3_3, (String)var4_4, (CompletableFuture)var6_10, (int)1));
                }
                ** GOTO lbl38
lbl16:
                // 1 sources

                if (!var0.task.getMode().contains("3")) ** GOTO lbl29
                try {
                    var3_3 = var0.api.getCookies().getCookieValue("_abck");
                    var4_4 = var0.api.getCookies().getCookieValue("bm_sz");
                }
                catch (Throwable var5_8) {
                    var0.logger.error("Failed to set value for sensor api: {}", (Object)var5_8.getMessage());
                    var3_3 = "";
                    var4_4 = "";
                }
                if (!(v1 = ((GaneshAPI)var0.api.getPixelAPI()).getSensorPayload(var3_3, (String)var4_4, var0.task.getKeywords()[0].toUpperCase(), (boolean)var1_1)).isDone()) {
                    var6_10 = v1;
                    return var6_10.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$sensorReqBuffer(io.trickle.task.sites.yeezy.Yeezy int java.lang.String java.lang.String java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (int)var1_1, (String)var3_3, (String)var4_4, (CompletableFuture)var6_10, (int)2));
                }
                ** GOTO lbl45
lbl29:
                // 1 sources

                v2 = var0.api.getTrickleSensor();
                if (!v2.isDone()) {
                    var6_10 = v2;
                    return var6_10.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$sensorReqBuffer(io.trickle.task.sites.yeezy.Yeezy int java.lang.String java.lang.String java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (int)var1_1, null, null, (CompletableFuture)var6_10, (int)3));
                }
                ** GOTO lbl50
            }
            case 1: {
                v0 = var4_4;
                var4_4 = var3_3;
                var3_3 = var2_2;
lbl38:
                // 2 sources

                var5_7 = (String)v0.join();
                var2_2 = new JsonObject().put("sensor_data", (Object)var5_7).toBuffer();
                return CompletableFuture.completedFuture(var2_2);
            }
            case 2: {
                v1 = var4_4;
                var4_4 = var3_3;
                var3_3 = var2_2;
lbl45:
                // 2 sources

                var5_9 = (String)v1.join();
                var2_2 = new JsonObject().put("sensor_data", (Object)var5_9).toBuffer();
                return CompletableFuture.completedFuture(var2_2);
            }
            case 3: {
                v2 = var4_4;
lbl50:
                // 2 sources

                var3_3 = (String)v2.join();
                var2_2 = new JsonObject(var3_3).toBuffer();
                return CompletableFuture.completedFuture(var2_2);
            }
        }
        throw new IllegalArgumentException();
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$postSensorsTilNoChallenge(Yeezy var0, String var1_1, CompletableFuture var2_2, int var3_3, Object var4_4) {
        switch (var3_3) {
            case 0: {
                var1_1 = "";
                v0 = var0.sendSensor(var0.api.postSensor(true), true);
                if (!v0.isDone()) {
                    var2_2 = v0;
                    return var2_2.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$postSensorsTilNoChallenge(io.trickle.task.sites.yeezy.Yeezy java.lang.String java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (String)var1_1, (CompletableFuture)var2_2, (int)1));
                }
                ** GOTO lbl11
            }
            case 1: {
                v0 = var2_2;
lbl11:
                // 2 sources

                v0.join();
                v1 = VertxUtil.randomSleep(1000L);
                if (!v1.isDone()) {
                    var2_2 = v1;
                    return var2_2.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$postSensorsTilNoChallenge(io.trickle.task.sites.yeezy.Yeezy java.lang.String java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (String)var1_1, (CompletableFuture)var2_2, (int)2));
                }
                ** GOTO lbl20
            }
            case 2: {
                v1 = var2_2;
lbl20:
                // 2 sources

                v1.join();
                v2 = var0.sendSensor(var0.api.postSensor(true), true);
                if (!v2.isDone()) {
                    var2_2 = v2;
                    return var2_2.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$postSensorsTilNoChallenge(io.trickle.task.sites.yeezy.Yeezy java.lang.String java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (String)var1_1, (CompletableFuture)var2_2, (int)3));
                }
                ** GOTO lbl29
            }
            case 3: {
                v2 = var2_2;
lbl29:
                // 2 sources

                v2.join();
lbl31:
                // 2 sources

                while (true) {
                    if (var0.api.getCookies().getCookieValue("_abck").contains("||") == false) return CompletableFuture.completedFuture(null);
                    v3 = var0.smartSleep(1000L);
                    if (!v3.isDone()) {
                        var2_2 = v3;
                        return var2_2.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$postSensorsTilNoChallenge(io.trickle.task.sites.yeezy.Yeezy java.lang.String java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (String)var1_1, (CompletableFuture)var2_2, (int)4));
                    }
                    ** GOTO lbl40
                    break;
                }
            }
            case 4: {
                v3 = var2_2;
lbl40:
                // 2 sources

                v3.join();
                v4 = var0.sendSensor(var0.api.postSensor(true), true);
                if (!v4.isDone()) {
                    var2_2 = v4;
                    return var2_2.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$postSensorsTilNoChallenge(io.trickle.task.sites.yeezy.Yeezy java.lang.String java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (String)var1_1, (CompletableFuture)var2_2, (int)5));
                }
                ** GOTO lbl49
            }
            case 5: {
                v4 = var2_2;
lbl49:
                // 2 sources

                var1_1 = (String)v4.join();
                if (var0.logger.isDebugEnabled()) {
                    var0.logger.debug(var1_1);
                } else {
                    if (var1_1 != null && var1_1.contains("false")) {
                        var0.logger.info("YS site down!");
                        return CompletableFuture.completedFuture(null);
                    }
                    if (!var0.api.getCookies().getCookieValue("_abck").contains("||")) {
                        var0.logger.info("Passed challenge l={}", (Object)var0.api.getCookies().getCookieValue("_abck").length());
                        return CompletableFuture.completedFuture(null);
                    }
                    var0.logger.info("Handling sensor step - l={}", (Object)var0.api.getCookies().getCookieValue("_abck").length());
                }
                ++var0.sensorPosts;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture getBloom() {
        HttpRequest httpRequest = this.api.bloom();
        this.logger.info("Fetching bloom");
        this.netLogInfo("Fetching bloom");
        while (true) {
            CompletableFuture completableFuture = Request.send(httpRequest);
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$getBloom(this, httpRequest, completableFuture2, null, 1, arg_0));
            }
            HttpResponse httpResponse = (HttpResponse)completableFuture.join();
            if (httpResponse != null) {
                if (((String)httpResponse.body()).contains("HTTP 403 - Forbidden")) {
                    this.logger.warn("Failed to fetch bloom: state=PROXY_BAN");
                    CompletableFuture completableFuture3 = VertxUtil.randomSleep(3000L);
                    if (!completableFuture3.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture3;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$getBloom(this, httpRequest, completableFuture4, httpResponse, 2, arg_0));
                    }
                    completableFuture3.join();
                    return CompletableFuture.failedFuture(new Exception("403 BLOOM ERROR"));
                }
                if (httpResponse.statusCode() == 200) {
                    this.logger.info("Fetched bloom: state=OK");
                    try {
                        JsonArray jsonArray = new JsonArray((String)httpResponse.body());
                        int n = 0;
                        while (n < jsonArray.size()) {
                            JsonObject jsonObject = jsonArray.getJsonObject(n);
                            if (jsonObject.getString("product_id").equals(this.api.getSKU())) {
                                return CompletableFuture.completedFuture(jsonObject.getString("product_name"));
                            }
                            ++n;
                        }
                        return CompletableFuture.completedFuture("YEEZY BOOST 350 V2");
                    }
                    catch (Exception exception) {
                        this.logger.error("Unable to find tag? Continuing... " + exception.getMessage());
                    }
                    return CompletableFuture.completedFuture("YEEZY BOOST 350 V2");
                }
                this.logger.warn("Failed to fetch bloom: status={}", (Object)httpResponse.statusCode());
                this.netLogWarn("Failed to fetch bloom: status=" + httpResponse.statusCode());
                CompletableFuture completableFuture5 = VertxUtil.sleep(this.task.getRetryDelay());
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$getBloom(this, httpRequest, completableFuture6, httpResponse, 3, arg_0));
                }
                completableFuture5.join();
                continue;
            }
            this.logger.warn("Failed to fetch bloom: state=NO_REPLY");
        }
    }

    /*
     * Unable to fully structure code
     */
    @Override
    public CompletableFuture run() {
        if (this.task.getMode().contains("noqueue")) ** GOTO lbl-1000
        v0 = this.harvesters.start();
        if (!v0.isDone()) {
            var3_1 = v0;
            return var3_1.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.yeezy.Yeezy java.util.concurrent.CompletableFuture int int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)this, (CompletableFuture)var3_1, (int)0, (int)1));
        }
        if (((Boolean)v0.join()).booleanValue()) lbl-1000:
        // 2 sources

        {
            v1 = 1;
        } else {
            v1 = 0;
        }
        var1_8 = v1;
        while (var1_8 != 0) {
            try {
                v2 = this.api.init();
                if (!v2.isDone()) {
                    var3_2 = v2;
                    return var3_2.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.yeezy.Yeezy java.util.concurrent.CompletableFuture int int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)this, (CompletableFuture)var3_2, (int)var1_8, (int)2));
                }
                v2.join();
                v3 = this.prepareSplash();
                if (!v3.isDone()) {
                    var3_3 = v3;
                    return var3_3.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.yeezy.Yeezy java.util.concurrent.CompletableFuture int int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)this, (CompletableFuture)var3_3, (int)var1_8, (int)3));
                }
                v3.join();
                v4 = this.queue();
                if (!v4.isDone()) {
                    var3_4 = v4;
                    return var3_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.yeezy.Yeezy java.util.concurrent.CompletableFuture int int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)this, (CompletableFuture)var3_4, (int)var1_8, (int)4));
                }
                v4.join();
                v5 = this.prepareCart();
                if (!v5.isDone()) {
                    var3_5 = v5;
                    return var3_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.yeezy.Yeezy java.util.concurrent.CompletableFuture int int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)this, (CompletableFuture)var3_5, (int)var1_8, (int)5));
                }
                v5.join();
                v6 = this.cart();
                if (!v6.isDone()) {
                    var3_6 = v6;
                    return var3_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.yeezy.Yeezy java.util.concurrent.CompletableFuture int int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)this, (CompletableFuture)var3_6, (int)var1_8, (int)6));
                }
                v6.join();
                this.api.getCookies().put("userBasketCount", "1", ".yeezysupply.com");
                this.api.getCookies().put("persistentBasketCount", "1", ".yeezysupply.com");
                this.api.getCookies().put("restoreBasketUrl", "%2Fon%2Fdemandware.store%2FSites-ys-US-Site%2Fen_US%2FCart-UpdateItems%3Fpid_0%3D" + this.api.getSKU() + Sizes.getSize(this.task.getSize()) + "%26qty_0%3D1%26", ".yeezysupply.com");
                v7 = this.checkout();
                if (!v7.isDone()) {
                    var3_7 = v7;
                    return var3_7.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.yeezy.Yeezy java.util.concurrent.CompletableFuture int int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)this, (CompletableFuture)var3_7, (int)var1_8, (int)7));
                }
                v7.join();
                var1_8 = 0;
            }
            catch (Throwable var2_9) {
                if (!var2_9.getMessage().contains("403")) {
                    this.logger.error(var2_9.getMessage());
                } else if (var2_9.getMessage().contains("FW")) {
                    this.logger.error(var2_9.getMessage());
                }
                this.sensorPosts = 0;
                this.visitedBaskets = false;
                this.api.close();
                this.api = new YeezyAPI(this.task);
                super.setClient(this.api);
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$smartSleep(Yeezy var0, long var1_1, CompletableFuture var3_2, int var4_3, Object var5_4) {
        switch (var4_3) {
            case 0: {
                if (var0.isFirewallOn() == false) return CompletableFuture.completedFuture(null);
                v0 = VertxUtil.randomSleep(var1_1);
                if (!v0.isDone()) {
                    var3_2 = v0;
                    return var3_2.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$smartSleep(io.trickle.task.sites.yeezy.Yeezy long java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (long)var1_1, (CompletableFuture)var3_2, (int)1));
                }
                ** GOTO lbl11
            }
            case 1: {
                v0 = var3_2;
lbl11:
                // 2 sources

                v0.join();
                return CompletableFuture.completedFuture(null);
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture getAkamaiScript() {
        HttpRequest httpRequest = this.api.akamaiScript(false);
        this.logger.info("Visiting FW");
        this.netLogInfo("Visiting FW");
        while (true) {
            CompletableFuture completableFuture = Request.send(httpRequest);
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$getAkamaiScript(this, httpRequest, completableFuture2, null, 1, arg_0));
            }
            HttpResponse httpResponse = (HttpResponse)completableFuture.join();
            if (httpResponse != null) {
                if (((String)httpResponse.body()).contains("HTTP 403 - Forbidden")) {
                    this.logger.warn("Failed to visit FW: state=PROXY_BAN");
                    this.netLogWarn("Failed to visit FW: state=PROXY_BAN");
                    CompletableFuture completableFuture3 = VertxUtil.sleep(this.task.getMonitorDelay());
                    if (!completableFuture3.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture3;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$getAkamaiScript(this, httpRequest, completableFuture4, httpResponse, 2, arg_0));
                    }
                    completableFuture3.join();
                    continue;
                }
                if (httpResponse.statusCode() != 200) continue;
                this.netLogWarn("Visited FW: state=OK");
                return CompletableFuture.completedFuture(null);
            }
            this.logger.warn("Failed to visit FW: state=NO_REPLY");
            this.netLogWarn("Failed to visit FW: state=NO_REPLY");
        }
    }

    public void handleFailureWebhooks(String string, String string2) {
        if (this.previousResponseHash != 0 && this.previousResponseLen == string2.length()) {
            if (this.previousResponseHash == string2.hashCode()) return;
        }
        try {
            Analytics.failure(string, this.task, new JsonObject(string2), this.api.proxyString());
            this.previousResponseHash = string2.hashCode();
            this.previousResponseLen = string2.length();
            return;
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$checkOrCaptcha(Yeezy var0, CompletableFuture var1_1, Lock var2_3, SolveFuture var3_5, SolveFuture var4_7, int var5_8, Object var6_9) {
        switch (var5_8) {
            case 0: {
                try {
                    v0 = var0.vertx.sharedData().getLocalLock("CAP").toCompletionStage().toCompletableFuture();
                    if (!v0.isDone()) {
                        var4_7 = v0;
                        return var4_7.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$checkOrCaptcha(io.trickle.task.sites.yeezy.Yeezy java.util.concurrent.CompletableFuture io.vertx.core.shareddata.Lock io.trickle.harvester.SolveFuture io.trickle.harvester.SolveFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (CompletableFuture)var4_7, null, null, null, (int)1));
                    }
lbl8:
                    // 3 sources

                    while (true) {
                        var1_1 = (Lock)v0.join();
                        try {
                            if (Yeezy.holder.get() == null || !Yeezy.holder.get().isUsed()) {
                                if (Yeezy.clock.get() + 117L >= Instant.now().getEpochSecond()) return CompletableFuture.completedFuture(null);
                            }
                            var0.logger.info("Captcha '{}' needs solving", (Object)Yeezy.holder.get());
                            var2_3 = ((TokenController)Engine.get().getModule(Controller.TOKEN)).solve(new CaptchaToken("https://www.yeezysupply.com/products/" + var0.api.getSKU()));
                            v1 = var2_3;
                            if (!v1.toCompletableFuture().isDone()) {
                                var4_7 = v1;
                                return var4_7.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$checkOrCaptcha(io.trickle.task.sites.yeezy.Yeezy java.util.concurrent.CompletableFuture io.vertx.core.shareddata.Lock io.trickle.harvester.SolveFuture io.trickle.harvester.SolveFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, null, var1_1, (SolveFuture)var2_3, (SolveFuture)var4_7, (int)2)).toCompletableFuture();
                            }
                            ** GOTO lbl37
                        }
                        catch (Throwable var2_4) {
                            var0.logger.error("HARVEST ERR: {}", (Object)var2_4.getMessage());
                            return CompletableFuture.completedFuture(null);
                        }
                        finally {
                            var1_1.release();
                        }
                        break;
                    }
                }
                catch (Throwable var1_2) {
                    var0.logger.error("LOCK ERR: {}", (Object)var1_2.getMessage());
                }
                return CompletableFuture.completedFuture(null);
            }
            case 1: {
                v0 = var1_1;
                ** continue;
            }
            case 2: {
                v1 = var4_7;
                v2 = var2_3;
                var2_3 = var3_5;
                var1_1 = v2;
lbl37:
                // 2 sources

                v1.toCompletableFuture().join();
                Yeezy.clock.set(Instant.now().getEpochSecond());
                Yeezy.holder.set((CaptchaToken)var2_3.get());
                var0.logger.info("Captcha after solve {}", (Object)Yeezy.holder.get());
                return CompletableFuture.completedFuture(null);
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture handleCode() {
        this.logger.warn("SMS Code required. Waiting for entry...");
        this.netLogInfo("SMS Code required. Waiting for entry...");
        HttpRequest httpRequest = this.api.submitCoupon(this.basketID, this.authorization);
        String string = null;
        int n = 0;
        while (this.running) {
            try {
                if (string == null) {
                    CompletableFuture completableFuture = CodeScreen.request(this.id, this.instanceProfile.getPhone());
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture2 = completableFuture;
                        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$handleCode(this, httpRequest, string, n, completableFuture2, null, null, null, 1, arg_0));
                    }
                    string = (String)completableFuture.join();
                    continue;
                }
                if (string.equals("~SKIP~SKIP~")) {
                    this.logger.warn("Code skip requested. I hope you know why you did this");
                    return CompletableFuture.completedFuture(true);
                }
                this.logger.info("Code received: code={}. Applying!", (Object)string);
                Buffer buffer = this.api.couponForm(string).toBuffer();
                this.api.updateUtagUrl("https://www.yeezysupply.com/payment");
                this.api.getCookies().put("utag_main", this.api.fetchUtag(), ".yeezysupply.com");
                CompletableFuture completableFuture = Request.send(httpRequest, buffer);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture3 = completableFuture;
                    return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$handleCode(this, httpRequest, string, n, completableFuture3, buffer, null, null, 2, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse != null) {
                    String string2 = httpResponse.bodyAsString();
                    if (httpResponse.statusCode() == 200) {
                        this.netLogInfo("Coupon responded body='" + string2 + "'");
                        if (httpResponse.getHeader("authorization") == null) {
                            this.logger.warn("Fake Coupon (animal)");
                            Analytics.log("Fake Coupon (animal)", this.task, this.api.getCookies().getCookieValueLength("_abck"), this.sensorPosts, this.visitedBaskets, this.api.userAgent);
                        } else {
                            if (string2.contains("basketId") && string2.toLowerCase().contains(string.toLowerCase()) && (string2.toLowerCase(Locale.ROOT).contains("\"valid\": true") || string2.toLowerCase(Locale.ROOT).contains("\"valid\":true"))) {
                                this.logger.info("Submit coupon SUCCESSFUL!: r={}", (Object)string2);
                                return CompletableFuture.completedFuture(true);
                            }
                            if (string2.contains("invalid_coupon_code_exception")) {
                                this.logger.warn("Invalid code: code={} for phone number: phone={}. Retrying...", (Object)string, (Object)this.instanceProfile.getPhone());
                                if (n++ >= 20) {
                                    this.logger.warn("Exceeded re-attempts. Requesting a new one.");
                                    n = 0;
                                    string = null;
                                    continue;
                                }
                            } else {
                                this.logger.warn("Error on coupon add: blocked={} headers={}", (Object)string2.contains("HTTP 403 - Forbidden"), (Object)httpResponse.headers().toString());
                            }
                        }
                    } else {
                        this.logger.info("Failed to submit coupon: status={} r={}", (Object)httpResponse.statusCode(), httpResponse.body());
                    }
                }
                CompletableFuture completableFuture4 = VertxUtil.randomSleep(this.task.getRetryDelay());
                if (!completableFuture4.isDone()) {
                    CompletableFuture completableFuture5 = completableFuture4;
                    return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$handleCode(this, httpRequest, string, n, completableFuture5, buffer, httpResponse, null, 3, arg_0));
                }
                completableFuture4.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Failed to handle coupon: {}", (Object)throwable.getMessage());
                this.netLogError("Failed to handle coupon: message='" + throwable.getMessage() + "'");
                CompletableFuture completableFuture = VertxUtil.randomSleep(3000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$handleCode(this, httpRequest, string, n, completableFuture6, null, null, throwable, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(false);
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$sendBasic(Yeezy var0, HttpRequest var1_1, String var2_2, int var3_3, CompletableFuture var4_4, HttpResponse var5_6, Throwable var6_7, int var7_8, Object var8_9) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [7[CATCHBLOCK]], but top level block is 11[UNCONDITIONALDOLOOP]
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

    public Buffer atcForm() {
        if (this.task.getSize().equalsIgnoreCase("random") && !this.availableSizes.isEmpty()) {
            this.logger.info("Picking any available size...");
            try {
                return this.api.atcForm(Sizes.findAnyAvailableJSON(this.availableSizes)).toBuffer();
            }
            catch (Sizes$NoAvailableSizeException sizes$NoAvailableSizeException) {
                this.logger.info("All sizes reported out of stock. Trying anyways...");
                return this.api.atcForm(Sizes.findAnyJSON(this.availableSizes)).toBuffer();
            }
        }
        if (this.task.getSize().contains("&")) {
            try {
                List<String> list = Arrays.asList(this.task.getSize().toLowerCase().split("&"));
                if (this.availableSizes.isEmpty()) {
                    this.logger.info("No pre-fetched sizes available. Picking any within range...");
                    String string = list.get(ThreadLocalRandom.current().nextInt(list.size()));
                    return this.api.atcForm(string).toBuffer();
                }
                this.logger.info("Picking any available size within range...");
                return this.api.atcForm(Sizes.findAnyAvailableOfRangeJSON(list, this.availableSizes)).toBuffer();
            }
            catch (Throwable throwable) {
                this.logger.warn("Failed to pick sizes within range. Recovering: {}", (Object)throwable.getMessage());
                return this.api.atcForm().toBuffer();
            }
        }
        if (!this.task.getSize().contains("random") && !this.availableSizes.isEmpty()) {
            this.logger.info("Selecting specific size [dyn-list-iter]");
            try {
                return this.api.atcForm(Sizes.findAnyAvailableOfRangeJSON(List.of(this.task.getSize()), this.availableSizes)).toBuffer();
            }
            catch (Throwable throwable) {
                this.logger.warn("Failed to pick size. Recovering: {}", (Object)throwable.getMessage());
                return this.api.atcForm().toBuffer();
            }
        }
        this.logger.info("Using size choice of size={}", (Object)this.task.getSize());
        return this.api.atcForm().toBuffer();
    }

    public CompletableFuture cart() {
        this.logger.info("Proceeding to cart...");
        try {
            this.logger.info("Using available sizes for sku={} : sizes={}", (Object)this.api.getSKU(), this.availableSizes);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        int n = 0;
        this.netLogInfo("Adding to cart");
        while (this.running) {
            if (!this.instanceSignal.equals(this.api.getSKU())) {
                this.instanceSignal = this.api.getSKU();
                this.logger.info("Fetching updated item {}", (Object)this.instanceSignal);
                CompletableFuture completableFuture = this.fillSizes();
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$cart(this, n, completableFuture2, null, null, null, null, 1, arg_0));
                }
                completableFuture.join();
            }
            Buffer buffer = this.atcForm();
            this.checkHmac();
            this.logger.info("Adding to cart");
            try {
                HttpRequest httpRequest = this.api.addToCart();
                CompletableFuture completableFuture = Request.send(httpRequest, buffer);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture3 = completableFuture;
                    return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$cart(this, n, completableFuture3, buffer, httpRequest, null, null, 2, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse != null) {
                    Object object;
                    this.logger.info("ATC Responded with status={}", (Object)httpResponse.statusCode());
                    if (httpResponse.statusCode() == 403) {
                        this.isPassedFW = false;
                        n = 0;
                        this.logger.warn("FW Blocked on ATC");
                        this.netLogWarn("Failed to ATC: state='FW' status=" + httpResponse.statusCode());
                        Analytics.log("ATC FW Blocked", this.task, this.api.getCookies().getCookieValueLength("_abck"), this.sensorPosts, this.visitedBaskets, this.api.userAgent);
                    } else {
                        this.isPassedFW = true;
                        object = ((String)httpResponse.body()).replace("\n", "");
                        n = 1;
                        if (((String)object).contains("branding_url_content")) {
                            CompletableFuture completableFuture4 = this.handlePOW((String)object);
                            if (!completableFuture4.isDone()) {
                                CompletableFuture completableFuture5 = completableFuture4;
                                return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$cart(this, n, completableFuture5, buffer, httpRequest, httpResponse, (String)object, 3, arg_0));
                            }
                            completableFuture4.join();
                            continue;
                        }
                        if (((String)object).contains("basketId")) {
                            this.authorization = httpResponse.getHeader("authorization");
                            if (this.authorization == null) {
                                this.logger.error("Fake cart (animal)");
                                this.netLogWarn("Fake cart (animal) body='" + (String)object + "'");
                                Analytics.log("Fake cart (animal)", this.task, this.api.getCookies().getCookieValueLength("_abck"), this.sensorPosts, this.visitedBaskets, this.api.userAgent);
                            } else {
                                if (!((String)object).contains("\"total\":0") || this.task.getMode().contains("noqueue")) {
                                    this.logger.info("ATC SUCCESSFUL count={} time={}: {}", (Object)successfulCartCount++, (Object)(System.currentTimeMillis() - this.pixelPostTs), object);
                                    this.basketID = new JsonObject((String)object).getString("basketId");
                                    this.netLogInfo("ATC SUCCESSFUL body='" + (String)object + "'");
                                    Analytics.log("ATC OK", this.task, this.api.getCookies().getCookieValueLength("_abck"), this.sensorPosts, this.visitedBaskets, this.api.userAgent);
                                    if (this.tokenRef.get() == null) return CompletableFuture.completedFuture(null);
                                    SharedCaptchaToken sharedCaptchaToken = this.tokenRef.get();
                                    sharedCaptchaToken.markPassed();
                                    this.tokenRef.set(null);
                                    return CompletableFuture.completedFuture(null);
                                }
                                this.logger.info("EMPTY_CART (OOS): {}", object);
                                this.netLogInfo("EMPTY_CART (OOS): body='" + (String)object + "'");
                            }
                        } else if (((String)object).length() == 0) {
                            this.logger.warn("Failed to ATC: state={} status={}", (Object)"Blocked by FW", (Object)httpResponse.statusCode());
                            this.netLogWarn("Failed to ATC: state='FW Blocked' status=" + httpResponse.statusCode());
                        } else if (((String)object).contains("HTTP 403 - Forbidden")) {
                            n = 0;
                            this.logger.warn("Failed to ATC: state={} status={}", (Object)"Blocked by 403 body", (Object)httpResponse.statusCode());
                            this.netLogWarn("Failed to ATC: state='Blocked by 403 body' status=" + httpResponse.statusCode());
                        } else if (httpResponse.statusCode() == 400) {
                            this.logger.warn("Failed to ATC: state={} status={}", (Object)"Blocked by 400 (bad session)", (Object)httpResponse.statusCode());
                            this.netLogWarn("Failed to ATC: state='Blocked by 400 (bad session)' status=" + httpResponse.statusCode());
                        } else {
                            this.logger.warn("Failed to ATC: state={} status={} body={}", (Object)"No Basket", (Object)httpResponse.statusCode(), httpResponse.body());
                            this.netLogWarn("Failed to ATC: state='No Basket' status=" + httpResponse.statusCode() + " body='" + (String)object + "'");
                        }
                    }
                    if (n != 0 && this.tokenRef.get() != null) {
                        object = this.tokenRef.get();
                        ((SharedCaptchaToken)object).markPassed();
                        this.tokenRef.set(null);
                    }
                } else {
                    this.logger.warn("Failed to ATC: state=NO_RESPONSE");
                }
            }
            catch (Throwable throwable) {
                this.logger.error("Error adding to cart: {}", (Object)throwable.getMessage());
                this.netLogError("Error adding to cart: message=" + throwable.getMessage());
            }
            CompletableFuture completableFuture = this.smartSleep(this.task.getRetryDelay());
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture6 = completableFuture;
                return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$cart(this, n, completableFuture6, buffer, null, null, null, 4, arg_0));
            }
            completableFuture.join();
            CompletableFuture completableFuture7 = this.prepareCart();
            if (!completableFuture7.isDone()) {
                CompletableFuture completableFuture8 = completableFuture7;
                return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$cart(this, n, completableFuture8, buffer, null, null, null, 5, arg_0));
            }
            completableFuture7.join();
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$getBloom(Yeezy var0, HttpRequest var1_1, CompletableFuture var2_2, HttpResponse var3_3, int var4_5, Object var5_6) {
        switch (var4_5) {
            case 0: {
                var1_1 = var0.api.bloom();
                var0.logger.info("Fetching bloom");
                var0.netLogInfo("Fetching bloom");
lbl6:
                // 3 sources

                while (true) {
                    v0 = Request.send(var1_1);
                    if (!v0.isDone()) {
                        var6_7 = v0;
                        return var6_7.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$getBloom(io.trickle.task.sites.yeezy.Yeezy io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (HttpRequest)var1_1, (CompletableFuture)var6_7, null, (int)1));
                    }
                    ** GOTO lbl14
                    break;
                }
            }
            case 1: {
                v0 = var2_2;
lbl14:
                // 2 sources

                if ((var2_2 = (HttpResponse)v0.join()) == null) ** GOTO lbl44
                if (!((String)var2_2.body()).contains("HTTP 403 - Forbidden")) ** GOTO lbl22
                var0.logger.warn("Failed to fetch bloom: state=PROXY_BAN");
                v1 = VertxUtil.randomSleep(3000L);
                if (!v1.isDone()) {
                    var6_8 = v1;
                    return var6_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$getBloom(io.trickle.task.sites.yeezy.Yeezy io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (HttpRequest)var1_1, (CompletableFuture)var6_8, (HttpResponse)var2_2, (int)2));
                }
                ** GOTO lbl49
lbl22:
                // 1 sources

                if (var2_2.statusCode() == 200) {
                    var0.logger.info("Fetched bloom: state=OK");
                    try {
                        var3_3 = new JsonArray((String)var2_2.body());
                        var4_5 = 0;
                        while (var4_5 < var3_3.size()) {
                            var5_6 = var3_3.getJsonObject(var4_5);
                            if (var5_6.getString("product_id").equals(var0.api.getSKU())) {
                                return CompletableFuture.completedFuture(var5_6.getString("product_name"));
                            }
                            ++var4_5;
                        }
                        return CompletableFuture.completedFuture("YEEZY BOOST 350 V2");
                    }
                    catch (Exception var3_4) {
                        var0.logger.error("Unable to find tag? Continuing... " + var3_4.getMessage());
                    }
                    return CompletableFuture.completedFuture("YEEZY BOOST 350 V2");
                }
                var0.logger.warn("Failed to fetch bloom: status={}", (Object)var2_2.statusCode());
                var0.netLogWarn("Failed to fetch bloom: status=" + var2_2.statusCode());
                v2 = VertxUtil.sleep(var0.task.getRetryDelay());
                if (!v2.isDone()) {
                    var6_9 = v2;
                    return var6_9.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$getBloom(io.trickle.task.sites.yeezy.Yeezy io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (HttpRequest)var1_1, (CompletableFuture)var6_9, (HttpResponse)var2_2, (int)3));
                }
                ** GOTO lbl55
lbl44:
                // 1 sources

                var0.logger.warn("Failed to fetch bloom: state=NO_REPLY");
                ** GOTO lbl6
            }
            case 2: {
                v1 = var2_2;
                var2_2 = var3_3;
lbl49:
                // 2 sources

                v1.join();
                return CompletableFuture.failedFuture(new Exception("403 BLOOM ERROR"));
            }
            case 3: {
                v2 = var2_2;
                var2_2 = var3_3;
lbl55:
                // 2 sources

                v2.join();
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture getProductPage(boolean bl) {
        HttpRequest httpRequest = this.api.productPage(bl);
        this.logger.info("Visiting product page.");
        this.netLogInfo("Visiting product page.");
        while (this.running) {
            try {
                CompletableFuture completableFuture = Request.send(httpRequest);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$getProductPage(this, (int)(bl ? 1 : 0), httpRequest, completableFuture2, null, null, 0, 0, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse != null) {
                    int n;
                    if (bl && !this.isSent) {
                        this.isSent = true;
                        CompletableFuture completableFuture2 = this.logRawPage("after-splash-product-page", Buffer.buffer((String)((String)httpResponse.body())));
                        if (!completableFuture2.isDone()) {
                            CompletableFuture completableFuture4 = completableFuture2;
                            return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$getProductPage(this, (int)(bl ? 1 : 0), httpRequest, completableFuture4, httpResponse, null, 0, 0, null, 2, arg_0));
                        }
                        completableFuture2.join();
                    }
                    String string = (String)httpResponse.body();
                    int n2 = string.contains("HTTP 403 - Forbidden");
                    int n3 = n = string.contains("text/javascript") || string.contains(".js\">") ? 1 : 0;
                    if (n2 != 0 && n == 0) {
                        this.logger.warn("Failed to visit product page: state=PROXY_BAN");
                        CompletableFuture completableFuture3 = VertxUtil.sleep(this.task.getMonitorDelay());
                        if (!completableFuture3.isDone()) {
                            CompletableFuture completableFuture6 = completableFuture3;
                            return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$getProductPage(this, (int)(bl ? 1 : 0), httpRequest, completableFuture6, httpResponse, string, n2, n, null, 3, arg_0));
                        }
                        completableFuture3.join();
                        return CompletableFuture.failedFuture(new Exception("403 PRODPAGE ERROR"));
                    }
                    if (httpResponse.statusCode() != 200) continue;
                    if (bl && string.contains("wrgen_orig_assets/")) {
                        this.logger.warn("Fake pass WR_ORIGIN???");
                    }
                    if (bl) return CompletableFuture.completedFuture(string);
                    if (string.contains("wrgen_orig_assets/")) return CompletableFuture.completedFuture(string);
                    if (this.task.getMode().contains("noqueue")) {
                        return CompletableFuture.completedFuture(string);
                    }
                    this.logger.warn("Waiting for waiting room to be live site=product_page");
                    CompletableFuture completableFuture4 = VertxUtil.randomSleep(this.task.getMonitorDelay());
                    if (!completableFuture4.isDone()) {
                        CompletableFuture completableFuture8 = completableFuture4;
                        return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$getProductPage(this, (int)(bl ? 1 : 0), httpRequest, completableFuture8, httpResponse, string, n2, n, null, 4, arg_0));
                    }
                    completableFuture4.join();
                    continue;
                }
                this.logger.warn("Failed to visit product page: state=NO_REPLY");
            }
            catch (Exception exception) {
                this.logger.warn("Failed to visit product page: {}", (Object)exception.getMessage());
                this.netLogError("Failed to visit product page: message='" + exception.getMessage() + "'");
                CompletableFuture completableFuture = VertxUtil.randomSleep(this.task.getRetryDelay());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture9 = completableFuture;
                    return ((CompletableFuture)completableFuture9.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$getProductPage(this, (int)(bl ? 1 : 0), httpRequest, completableFuture9, null, null, 0, 0, exception, 5, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture sendPixel(HttpRequest httpRequest, String string, String string2, String string3) {
        this.logger.info("Sending pixel");
        while (this.running) {
            try {
                HttpResponse httpResponse;
                String string4;
                if (this.task.getMode().contains("2")) {
                    CompletableFuture completableFuture = this.api.getPixelAPI().getPixelReqString(string, string2, string3);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture2 = completableFuture;
                        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$sendPixel(this, httpRequest, string, string2, string3, completableFuture2, null, null, null, null, 1, arg_0));
                    }
                    string4 = (String)completableFuture.join();
                    CompletableFuture completableFuture3 = Request.send(httpRequest, Buffer.buffer((String)string4));
                    if (!completableFuture3.isDone()) {
                        CompletableFuture completableFuture4 = completableFuture3;
                        return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$sendPixel(this, httpRequest, string, string2, string3, completableFuture4, string4, null, null, null, 2, arg_0));
                    }
                    httpResponse = (HttpResponse)completableFuture3.join();
                } else if (this.task.getMode().contains("3")) {
                    CompletableFuture completableFuture = this.api.getPixelAPI().getPixelReqString(string, string2, string3);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture5 = completableFuture;
                        return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$sendPixel(this, httpRequest, string, string2, string3, completableFuture5, null, null, null, null, 3, arg_0));
                    }
                    string4 = (String)completableFuture.join();
                    CompletableFuture completableFuture6 = Request.send(httpRequest, Buffer.buffer((String)string4));
                    if (!completableFuture6.isDone()) {
                        CompletableFuture completableFuture7 = completableFuture6;
                        return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$sendPixel(this, httpRequest, string, string2, string3, completableFuture7, string4, null, null, null, 4, arg_0));
                    }
                    httpResponse = (HttpResponse)completableFuture6.join();
                } else {
                    CompletableFuture completableFuture = this.api.getPixelAPI().getPixelReqForm(string, string2, string3);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture8 = completableFuture;
                        return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$sendPixel(this, httpRequest, string, string2, string3, completableFuture8, null, null, null, null, 5, arg_0));
                    }
                    string4 = (MultiMap)completableFuture.join();
                    CompletableFuture completableFuture9 = Request.send(httpRequest, (MultiMap)string4);
                    if (!completableFuture9.isDone()) {
                        CompletableFuture completableFuture10 = completableFuture9;
                        return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$sendPixel(this, httpRequest, string, string2, string3, completableFuture10, null, (MultiMap)string4, null, null, 6, arg_0));
                    }
                    httpResponse = (HttpResponse)completableFuture9.join();
                }
                if (httpResponse != null) {
                    if (httpResponse.statusCode() == 200) {
                        this.logger.info("Handled FW ok!");
                        return CompletableFuture.completedFuture(null);
                    }
                    this.logger.warn("Failed to send FW: status={}", (Object)httpResponse.statusCode());
                }
                CompletableFuture completableFuture = VertxUtil.randomSignalSleep(this.instanceSignal, this.task.getRetryDelay());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture11 = completableFuture;
                    return ((CompletableFuture)completableFuture11.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$sendPixel(this, httpRequest, string, string2, string3, completableFuture11, null, null, httpResponse, null, 7, arg_0));
                }
                completableFuture.join();
            }
            catch (Exception exception) {
                this.logger.error("Error occurred on sending FW: '{}'", (Object)exception.getMessage());
                CompletableFuture completableFuture = VertxUtil.randomSleep(3000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture12 = completableFuture;
                    return ((CompletableFuture)completableFuture12.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$sendPixel(this, httpRequest, string, string2, string3, completableFuture12, null, null, null, exception, 8, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture checkout() {
        if (this.visitedBaskets) {
            CompletableFuture completableFuture = this.postSensors();
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$checkout(this, completableFuture2, null, 1, arg_0));
            }
            completableFuture.join();
        }
        CompletableFuture completableFuture = this.postSensorsTilNoChallenge();
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture3 = completableFuture;
            return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$checkout(this, completableFuture3, null, 2, arg_0));
        }
        completableFuture.join();
        CompletableFuture completableFuture4 = this.submitShippingAndBilling(this.api.submitShippingAndBilling(this.basketID, this.authorization));
        if (!completableFuture4.isDone()) {
            CompletableFuture completableFuture5 = completableFuture4;
            return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$checkout(this, completableFuture5, null, 3, arg_0));
        }
        String string = (String)completableFuture4.join();
        if (this.isCodeRequired(string)) {
            CompletableFuture completableFuture6 = this.handleCode();
            if (!completableFuture6.isDone()) {
                CompletableFuture completableFuture7 = completableFuture6;
                return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$checkout(this, completableFuture7, string, 4, arg_0));
            }
            completableFuture6.join();
        }
        this.api.getCookies().put("UserSignUpAndSaveOverlay", "1", ".yeezysupply.com");
        this.api.getCookies().put("pagecontext_cookies", "", ".yeezysupply.com");
        this.api.getCookies().put("pagecontext_secure_cookies", "", ".yeezysupply.com");
        CompletableFuture completableFuture8 = this.postSensorsTilNoChallenge();
        if (!completableFuture8.isDone()) {
            CompletableFuture completableFuture9 = completableFuture8;
            return ((CompletableFuture)completableFuture9.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$checkout(this, completableFuture9, string, 5, arg_0));
        }
        completableFuture8.join();
        CompletableFuture completableFuture10 = this.processPayment(this.api.processPayment(this.authorization), this.basketID);
        if (!completableFuture10.isDone()) {
            CompletableFuture completableFuture11 = completableFuture10;
            return ((CompletableFuture)completableFuture11.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$checkout(this, completableFuture11, string, 6, arg_0));
        }
        completableFuture10.join();
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture sendSensor(HttpRequest httpRequest, boolean bl) {
        try {
            CompletableFuture completableFuture = this.sensorReqBuffer(bl);
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$sendSensor(this, httpRequest, (int)(bl ? 1 : 0), completableFuture2, null, null, null, 1, arg_0));
            }
            Buffer buffer = (Buffer)completableFuture.join();
            CompletableFuture completableFuture3 = Request.send(httpRequest, buffer);
            if (!completableFuture3.isDone()) {
                CompletableFuture completableFuture4 = completableFuture3;
                return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$sendSensor(this, httpRequest, (int)(bl ? 1 : 0), completableFuture4, buffer, null, null, 2, arg_0));
            }
            HttpResponse httpResponse = (HttpResponse)completableFuture3.join();
            if (httpResponse != null && httpResponse.statusCode() != 201) {
                this.logger.warn("Failed to send sensor: status={}", (Object)httpResponse.statusCode());
                return CompletableFuture.completedFuture(null);
            }
            if (httpResponse != null) {
                return CompletableFuture.completedFuture(httpResponse.bodyAsString());
            }
            CompletableFuture completableFuture5 = VertxUtil.randomSignalSleep(this.instanceSignal, this.task.getRetryDelay());
            if (!completableFuture5.isDone()) {
                CompletableFuture completableFuture6 = completableFuture5;
                return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$sendSensor(this, httpRequest, (int)(bl ? 1 : 0), completableFuture6, buffer, httpResponse, null, 3, arg_0));
            }
            completableFuture5.join();
            return CompletableFuture.completedFuture(null);
        }
        catch (Exception exception) {
            this.logger.error("Error occurred on sending sensor: '{}'", (Object)exception.getMessage());
            CompletableFuture completableFuture = VertxUtil.randomSleep(3000L);
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture7 = completableFuture;
                return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$sendSensor(this, httpRequest, (int)(bl ? 1 : 0), completableFuture7, null, null, exception, 4, arg_0));
            }
            completableFuture.join();
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture fillSizes() {
        CompletableFuture completableFuture = this.getSizeJson(!this.availableSizes.isEmpty());
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$fillSizes(this, completableFuture2, 1, arg_0));
        }
        String string = (String)completableFuture.join();
        try {
            JsonArray jsonArray = new JsonObject(string).getJsonArray("variation_list");
            List list = jsonArray.stream().sorted(Yeezy::lambda$fillSizes$5).map(Yeezy::lambda$fillSizes$6).collect(Collectors.toList());
            if (list.isEmpty()) return CompletableFuture.completedFuture(null);
            this.availableSizes.clear();
            this.availableSizes.addAll(list);
            return CompletableFuture.completedFuture(null);
        }
        catch (Throwable throwable) {
            this.logger.error("Could not order sizes (Normal. Don't panic): {}", (Object)throwable.getMessage());
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$processPayment(Yeezy var0, HttpRequest var1_1, String var2_2, int var3_3, String var4_4, CompletableFuture var5_6, String var6_7, HttpResponse var7_8, String var8_9, JsonObject var9_10, Window3DS2 var10_12, Exception var11_13, int var12_14, Object var13_15) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [16[CATCHBLOCK]], but top level block is 28[UNCONDITIONALDOLOOP]
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

    public CompletableFuture smartUA() {
        if (this.isFirewallOn()) return CompletableFuture.completedFuture(null);
        if (this.api.getPixelAPI() instanceof HawkAPI) {
            CompletableFuture completableFuture = ((HawkAPI)this.api.getPixelAPI()).updateUserAgent();
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                YeezyAPI yeezyAPI = this.api;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$smartUA(this, yeezyAPI, completableFuture2, 1, arg_0));
            }
            this.api.userAgent = (String)completableFuture.join();
            return CompletableFuture.completedFuture(null);
        }
        if (!(this.api.getPixelAPI() instanceof GaneshAPI)) return CompletableFuture.completedFuture(null);
        CompletableFuture completableFuture = ((GaneshAPI)this.api.getPixelAPI()).updateUserAgent();
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture3 = completableFuture;
            YeezyAPI yeezyAPI = this.api;
            return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$smartUA(this, yeezyAPI, completableFuture3, 2, arg_0));
        }
        this.api.userAgent = (String)completableFuture.join();
        return CompletableFuture.completedFuture(null);
    }

    public boolean isFirewallOn() {
        return Engine.get().getClientConfiguration().getBoolean("firewall", Boolean.valueOf(true));
    }

    public static String lambda$processPayment$7() {
        return "Unknown payment error";
    }

    public static Boolean lambda$handlePOW$0(HttpResponse httpResponse) {
        if (httpResponse.statusCode() != 200) return null;
        return true;
    }

    public String[] getPixelParams(String string) {
        String[] stringArray = new String[3];
        stringArray[0] = Pixel.parseBaza(string);
        String[] stringArray2 = Pixel.parseAkam(string);
        stringArray[1] = stringArray2[0];
        stringArray[2] = stringArray2[1];
        return stringArray;
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$getHomePage(Yeezy var0, HttpRequest var1_1, int var2_2, CompletableFuture var3_3, HttpResponse var4_5, String var5_6, int var6_8, int var7_9, Throwable var8_14, int var9_15, Object var10_16) {
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
     * Unable to fully structure code
     */
    public static CompletableFuture async$checkout(Yeezy var0, CompletableFuture var1_1, String var2_2, int var3_3, Object var4_4) {
        switch (var3_3) {
            case 0: {
                if (!var0.visitedBaskets) ** GOTO lbl13
                v0 = var0.postSensors();
                if (!v0.isDone()) {
                    var2_2 = v0;
                    return var2_2.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$checkout(io.trickle.task.sites.yeezy.Yeezy java.util.concurrent.CompletableFuture java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (CompletableFuture)var2_2, null, (int)1));
                }
                ** GOTO lbl11
            }
            case 1: {
                v0 = var1_1;
lbl11:
                // 2 sources

                v0.join();
lbl13:
                // 2 sources

                if (!(v1 = var0.postSensorsTilNoChallenge()).isDone()) {
                    var2_2 = v1;
                    return var2_2.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$checkout(io.trickle.task.sites.yeezy.Yeezy java.util.concurrent.CompletableFuture java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (CompletableFuture)var2_2, null, (int)2));
                }
                ** GOTO lbl19
            }
            case 2: {
                v1 = var1_1;
lbl19:
                // 2 sources

                v1.join();
                v2 = var0.submitShippingAndBilling(var0.api.submitShippingAndBilling(var0.basketID, var0.authorization));
                if (!v2.isDone()) {
                    var2_2 = v2;
                    return var2_2.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$checkout(io.trickle.task.sites.yeezy.Yeezy java.util.concurrent.CompletableFuture java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (CompletableFuture)var2_2, null, (int)3));
                }
                ** GOTO lbl28
            }
            case 3: {
                v2 = var1_1;
lbl28:
                // 2 sources

                if (!var0.isCodeRequired((String)(var1_1 = (String)v2.join()))) ** GOTO lbl39
                v3 = var0.handleCode();
                if (!v3.isDone()) {
                    var2_2 = v3;
                    return var2_2.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$checkout(io.trickle.task.sites.yeezy.Yeezy java.util.concurrent.CompletableFuture java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (CompletableFuture)var2_2, (String)var1_1, (int)4));
                }
                ** GOTO lbl37
            }
            case 4: {
                v3 = var1_1;
                var1_1 = var2_2;
lbl37:
                // 2 sources

                v3.join();
lbl39:
                // 2 sources

                var0.api.getCookies().put("UserSignUpAndSaveOverlay", "1", ".yeezysupply.com");
                var0.api.getCookies().put("pagecontext_cookies", "", ".yeezysupply.com");
                var0.api.getCookies().put("pagecontext_secure_cookies", "", ".yeezysupply.com");
                v4 = var0.postSensorsTilNoChallenge();
                if (!v4.isDone()) {
                    var2_2 = v4;
                    return var2_2.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$checkout(io.trickle.task.sites.yeezy.Yeezy java.util.concurrent.CompletableFuture java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (CompletableFuture)var2_2, (String)var1_1, (int)5));
                }
                ** GOTO lbl53
            }
            case 5: {
                v4 = var1_1;
                var1_1 = var2_2;
lbl53:
                // 2 sources

                v4.join();
                v5 = var0.processPayment(var0.api.processPayment(var0.authorization), var0.basketID);
                if (!v5.isDone()) {
                    var2_2 = v5;
                    return var2_2.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$checkout(io.trickle.task.sites.yeezy.Yeezy java.util.concurrent.CompletableFuture java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Yeezy)var0, (CompletableFuture)var2_2, (String)var1_1, (int)6));
                }
                ** GOTO lbl63
            }
            case 6: {
                v5 = var1_1;
                var1_1 = var2_2;
lbl63:
                // 2 sources

                v5.join();
                return CompletableFuture.completedFuture(null);
            }
        }
        throw new IllegalArgumentException();
    }

    public boolean smartSwapEnabled() {
        return Engine.get().getClientConfiguration().getBoolean("smartSwap", Boolean.valueOf(false));
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$waitForSale(Yeezy var0, HttpRequest var1_1, CompletableFuture var2_2, HttpResponse var3_4, Exception var4_5, int var5_6, Object var6_7) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [8[CATCHBLOCK]], but top level block is 12[UNCONDITIONALDOLOOP]
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

    public CompletableFuture getHomePage() {
        HttpRequest httpRequest = this.api.homePage();
        this.logger.info("Visiting homepage");
        this.netLogInfo("Visiting homepage");
        int n = 0;
        while (this.running) {
            try {
                this.api.getCookies().put("RT", RT.getRT(), ".yeezysupply.com");
                CompletableFuture completableFuture = Request.send(httpRequest);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$getHomePage(this, httpRequest, n, completableFuture2, null, null, 0, 0, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse != null) {
                    int n2;
                    String string = (String)httpResponse.body();
                    int n3 = string.contains("HTTP 403 - Forbidden");
                    int n4 = n2 = string.contains("text/javascript") || string.contains(".js\">") ? 1 : 0;
                    if (n3 != 0 && n2 == 0) {
                        CompletableFuture completableFuture2 = VertxUtil.sleep(this.task.getMonitorDelay());
                        if (!completableFuture2.isDone()) {
                            CompletableFuture completableFuture4 = completableFuture2;
                            return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$getHomePage(this, httpRequest, n, completableFuture4, httpResponse, string, n3, n2, null, 2, arg_0));
                        }
                        completableFuture2.join();
                        if (n++ > 3) {
                            this.logger.warn("Failed to visit homepage: state=PROXY_BAN -> RESTARTING TASK");
                            return CompletableFuture.failedFuture(new Exception("403 ERROR"));
                        }
                        this.logger.warn("Failed to visit homepage: state=PROXY_BAN");
                        CompletableFuture completableFuture3 = this.api.init();
                        if (!completableFuture3.isDone()) {
                            CompletableFuture completableFuture6 = completableFuture3;
                            return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$getHomePage(this, httpRequest, n, completableFuture6, httpResponse, string, n3, n2, null, 3, arg_0));
                        }
                        completableFuture3.join();
                        this.api.getCookies().clear();
                        continue;
                    }
                    if (httpResponse.statusCode() != 200) continue;
                    this.logger.info("Visited home page: state=OK");
                    return CompletableFuture.completedFuture(string);
                }
                this.logger.warn("Failed to visit homepage: state=NO_REPLY");
            }
            catch (Throwable throwable) {
                this.logger.warn("Failed to visit homepage: {}", (Object)throwable.getMessage());
                this.netLogError("Failed to visit homepage: message='" + throwable.getMessage() + "'");
                CompletableFuture completableFuture = VertxUtil.randomSleep(this.task.getRetryDelay());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$getHomePage(this, httpRequest, n, completableFuture7, null, null, 0, 0, throwable, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture("");
    }

    public CompletableFuture smartSleep(long l) {
        if (!this.isFirewallOn()) return CompletableFuture.completedFuture(null);
        CompletableFuture completableFuture = VertxUtil.randomSleep(l);
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$smartSleep(this, l, completableFuture2, 1, arg_0));
        }
        completableFuture.join();
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$getProductPage(Yeezy var0, int var1_1, HttpRequest var2_2, CompletableFuture var3_3, HttpResponse var4_5, String var5_6, int var6_9, int var7_10, Exception var8_16, int var9_17, Object var10_18) {
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

    public CompletableFuture logRawPage(String string, Buffer buffer) {
        try {
            Request.send(this.api.getWebClient().postAbs("https://discord.com/api/webhooks/848841437897949205/HyPlSFQy2r7kS8h3DwCBDM-iC39S1rQfSFFFf0SyrKAevL18IbjpEfa1RuKDxngKGi4H"), MultipartForm.create().textFileUpload("file", string + ".txt", buffer, "text/plain"));
            return CompletableFuture.completedFuture(null);
        }
        catch (Throwable throwable) {
            this.logger.warn("Failed to log data: {}", (Object)throwable.getMessage());
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture prepareSplash() {
        CompletableFuture completableFuture = this.getHomePage();
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$prepareSplash(this, completableFuture2, null, null, null, null, null, 0, null, null, 0L, 0L, 0L, 1, arg_0));
        }
        String string = (String)completableFuture.join();
        this.updateSensorUrlFromHTML(string);
        String[] stringArray = this.getPixelParams(string);
        CompletableFuture completableFuture3 = this.getAkamaiScript();
        if (!completableFuture3.isDone()) {
            CompletableFuture completableFuture4 = completableFuture3;
            return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$prepareSplash(this, completableFuture4, string, stringArray, null, null, null, 0, null, null, 0L, 0L, 0L, 2, arg_0));
        }
        completableFuture3.join();
        CompletableFuture completableFuture5 = Request.executeTillOk(this.api.getPixel(stringArray[1]));
        if (!completableFuture5.isDone()) {
            CompletableFuture completableFuture6 = completableFuture5;
            return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$prepareSplash(this, completableFuture6, string, stringArray, null, null, null, 0, null, null, 0L, 0L, 0L, 3, arg_0));
        }
        String string2 = (String)completableFuture5.join();
        CompletableFuture completableFuture7 = this.getBloom();
        if (!completableFuture7.isDone()) {
            CompletableFuture completableFuture8 = completableFuture7;
            return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$prepareSplash(this, completableFuture8, string, stringArray, string2, null, null, 0, null, null, 0L, 0L, 0L, 4, arg_0));
        }
        String string3 = (String)completableFuture7.join();
        this.api.setUtagProductName(string3.replace("\n", ""));
        this.api.getCookies().put("UserSignUpAndSave", "1", ".yeezysupply.com");
        this.api.getCookies().put("UserSignUpAndSaveOverlay", "0", ".yeezysupply.com");
        this.api.getCookies().put("default_searchTerms_CustomizeSearch", "%5B%5D", ".yeezysupply.com");
        this.api.getCookies().put("geoRedirectionAlreadySuggested", "false", ".yeezysupply.com");
        this.api.getCookies().put("wishlist", "%5B%5D", ".yeezysupply.com");
        this.api.getCookies().put("persistentBasketCount", "0", ".yeezysupply.com");
        this.api.getCookies().put("userBasketCount", "0", ".yeezysupply.com");
        String[] stringArray2 = Pixel.parseHexArray(string2);
        int n = Pixel.parseGIndex(string2);
        String string4 = Pixel.parseTValue(stringArray[2]);
        String string5 = stringArray2[n];
        CompletableFuture completableFuture9 = this.sendPixel(this.api.postPixel(stringArray[2].split("\\?")[0]), stringArray[0], string4, string5);
        if (!completableFuture9.isDone()) {
            CompletableFuture completableFuture10 = completableFuture9;
            return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$prepareSplash(this, completableFuture10, string, stringArray, string2, string3, stringArray2, n, string4, string5, 0L, 0L, 0L, 5, arg_0));
        }
        completableFuture9.join();
        this.pixelPostTs = System.currentTimeMillis();
        this.api.getCookies().put("geo_country", "US", ".yeezysupply.com");
        this.api.getCookies().put("utag_main", this.api.fetchUtag(), ".yeezysupply.com");
        this.api.getCookies().put("_ga", "GA1.2." + ThreadLocalRandom.current().nextInt(1207338862, 1992599043) + "." + System.currentTimeMillis(), ".yeezysupply.com");
        this.api.getCookies().put("_gid", "GA1.2." + ThreadLocalRandom.current().nextInt(120016221, 190016221) + "." + System.currentTimeMillis(), ".yeezysupply.com");
        this.api.getCookies().put("_gat_tealium_0", "1", ".yeezysupply.com");
        this.api.getCookies().put("_fbp", "fb.1." + Instant.now().toEpochMilli() + ThreadLocalRandom.current().nextInt(1000) + "." + Instant.now().toEpochMilli(), ".yeezysupply.com");
        this.api.getCookies().put("_gcl_au", "1.1." + System.currentTimeMillis() + "." + System.currentTimeMillis(), ".yeezysupply.com");
        this.api.getCookies().put("AMCVS_7ADA401053CCF9130A490D4C%40AdobeOrg", "1", ".yeezysupply.com");
        long l = Instant.now().getEpochSecond() + 7200L;
        long l2 = l + 597600L;
        this.api.getCookies().put("AMCV_7ADA401053CCF9130A490D4C%40AdobeOrg", "-227196251%7CMCIDTS%7C" + Utag.H() + "%7CMCMID%7C" + Utag.r() + "%7CMCAAMLH-" + l2 + "%7C7%7CMCAAMB-" + l2 + "%7CRKhpRz8krg2tLO6pguXWp5olkAcUniQYPHaMWWgdJ3xzPWQmdj0y%7CMCOPTOUT-" + l + "s%7CNONE%7CMCAID%7CNONE", ".yeezysupply.com");
        this.api.getCookies().put("s_cc", "true", ".yeezysupply.com");
        long l3 = ThreadLocalRandom.current().nextLong(1625112000771L, 1625112000876L);
        this.api.getCookies().put("s_pers", "%20s_vnum%3D" + l3 + "%2526vn%253D1%7C" + l3 + "%3B%20s_invisit%3Dtrue%7C" + (Instant.now().toEpochMilli() + 1800000L) + "%3B", ".yeezysupply.com");
        CompletableFuture completableFuture11 = this.getProductPage(false);
        if (!completableFuture11.isDone()) {
            CompletableFuture completableFuture12 = completableFuture11;
            return ((CompletableFuture)completableFuture12.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$prepareSplash(this, completableFuture12, string, stringArray, string2, string3, stringArray2, n, string4, string5, l, l2, l3, 6, arg_0));
        }
        completableFuture11.join();
        CompletableFuture completableFuture13 = Request.executeTillOk(this.api.sharedHpl());
        if (!completableFuture13.isDone()) {
            CompletableFuture completableFuture14 = completableFuture13;
            return ((CompletableFuture)completableFuture14.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$prepareSplash(this, completableFuture14, string, stringArray, string2, string3, stringArray2, n, string4, string5, l, l2, l3, 7, arg_0));
        }
        completableFuture13.join();
        CompletableFuture completableFuture15 = Request.executeTillOk(this.api.ushpl());
        if (!completableFuture15.isDone()) {
            CompletableFuture completableFuture16 = completableFuture15;
            return ((CompletableFuture)completableFuture16.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$prepareSplash(this, completableFuture16, string, stringArray, string2, string3, stringArray2, n, string4, string5, l, l2, l3, 8, arg_0));
        }
        completableFuture15.join();
        CompletableFuture completableFuture17 = this.waitForSale();
        if (!completableFuture17.isDone()) {
            CompletableFuture completableFuture18 = completableFuture17;
            return ((CompletableFuture)completableFuture18.exceptionally(Function.identity())).thenCompose(arg_0 -> Yeezy.async$prepareSplash(this, completableFuture18, string, stringArray, string2, string3, stringArray2, n, string4, string5, l, l2, l3, 9, arg_0));
        }
        completableFuture17.join();
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$sendPixel(Yeezy var0, HttpRequest var1_1, String var2_2, String var3_3, String var4_4, CompletableFuture var5_5, String var6_7, MultiMap var7_8, HttpResponse var8_9, Exception var9_10, int var10_11, Object var11_12) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [12[CATCHBLOCK]], but top level block is 21[UNCONDITIONALDOLOOP]
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
}

