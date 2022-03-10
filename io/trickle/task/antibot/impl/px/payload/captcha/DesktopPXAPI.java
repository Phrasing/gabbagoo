/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.netty.util.AsciiString
 *  io.vertx.core.MultiMap
 *  io.vertx.core.json.JsonObject
 *  io.vertx.core.net.ProxyOptions
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.client.HttpResponse
 *  io.vertx.ext.web.codec.BodyCodec
 */
package io.trickle.task.antibot.impl.px.payload.captcha;

import io.netty.util.AsciiString;
import io.trickle.core.actor.TaskActor;
import io.trickle.task.antibot.impl.px.PerimeterX;
import io.trickle.task.sites.Site;
import io.trickle.util.Utils;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.trickle.webclient.TaskApiClient;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.ProxyOptions;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.codec.BodyCodec;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class DesktopPXAPI
extends PerimeterX {
    public static CharSequence DEFAULT_UA = AsciiString.cached((String)"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.54 Safari/537.36");
    public static Site site;
    public static CharSequence ONE_VALUE;
    public long lockoutTiming = -1L;
    public MultiMap cachedResponse = null;
    public static CharSequence FP_VALUE;
    public static CharSequence DEFAULT_SEC_UA;
    public String deviceNumber = "undefined";
    public static CharSequence RF_VALUE;
    public TaskApiClient delegate;
    public JsonObject cookieSession = new JsonObject();
    public static CharSequence CFP_VALUE;
    public String userAgent;
    public String secUA;
    public static CharSequence PXHD_VALUE;
    public static CharSequence VID_COOKIE;

    @Override
    public CompletableFuture solveCaptcha(String string, String string2, String string3) {
        int n = 1;
        while (n <= 30) {
            try {
                if (this.deviceNumber.equalsIgnoreCase("undefined")) {
                    return this.solve();
                }
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Solving PX captcha via API attempt {}", (Object)n);
                }
                CompletableFuture completableFuture = Request.send(this.captchaRequest());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXAPI.async$solveCaptcha(this, string, string2, string3, n, completableFuture2, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse != null) {
                    boolean bl;
                    JsonObject jsonObject = (JsonObject)httpResponse.body();
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug("API captcha response received: {}", (Object)jsonObject.encodePrettily());
                    }
                    String string4 = jsonObject.getString("cookie", null);
                    boolean bl2 = bl = this.isBool(jsonObject) ? jsonObject.getBoolean("error", Boolean.valueOf(false)).booleanValue() : this.validateError(string4);
                    if (bl || string4 == null || string4.isEmpty()) {
                        if (this.logger.isDebugEnabled()) {
                            this.logger.debug("Invalid sensor api response: {}", (Object)jsonObject.encodePrettily());
                        }
                    } else if (jsonObject.containsKey("data")) {
                        this.cookieSession = jsonObject.getJsonObject("data");
                        this.lockoutTiming = System.currentTimeMillis();
                        this.logger.info("Solved captcha successfully!");
                        this.cachedResponse = this.handleResponse(string4);
                        return CompletableFuture.completedFuture(this.cachedResponse);
                    }
                } else if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Solving sensor via API failed. No response received. Retrying...");
                } else {
                    this.logger.warn("Failed to solve captcha. Retrying...");
                }
            }
            catch (Throwable throwable) {
                this.logger.warn("Error solving sensor: {}. Retrying...", (Object)throwable.getMessage());
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug((Object)throwable);
                }
                CompletableFuture completableFuture = VertxUtil.randomSleep(3000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture3 = completableFuture;
                    return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXAPI.async$solveCaptcha(this, string, string2, string3, n, completableFuture3, throwable, 2, arg_0));
                }
                completableFuture.join();
            }
            ++n;
        }
        return CompletableFuture.completedFuture(MultiMap.caseInsensitiveMultiMap());
    }

    public HttpRequest solveRequest() {
        return this.client.getAbs("https://pxgen-bmii2thzea-uc.a.run.app/gen").as(BodyCodec.jsonObject()).addQueryParam("authToken", "PX-8E7DF802-E106-44D7-8E32-C26E7F8FE976").addQueryParam("site", site.toString().toLowerCase()).addQueryParam("region", "com").addQueryParam("proxy", this.getProxyString()).addQueryParam("deviceNumber", this.deviceNumber);
    }

    @Override
    public String getDeviceAcceptEncoding() {
        return "gzip, deflate";
    }

    public String getProxyString() {
        ProxyOptions proxyOptions = this.delegate.getWebClient().getOptions().getProxyOptions();
        if (proxyOptions != null) return "http://" + proxyOptions.getUsername() + ":" + proxyOptions.getPassword() + "@" + proxyOptions.getHost() + ":" + proxyOptions.getPort();
        return "";
    }

    public boolean validateError(String string) {
        if (string == null) {
            return true;
        }
        String string2 = string.toLowerCase(Locale.ROOT);
        if (string2.contains("not valid")) return true;
        if (string2.contains("proxy")) return true;
        return false;
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$solveCaptcha(DesktopPXAPI var0, String var1_1, String var2_2, String var3_3, int var4_4, CompletableFuture var5_5, Throwable var6_7, int var7_8, Object var8_10) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [6[CATCHBLOCK]], but top level block is 9[UNCONDITIONALDOLOOP]
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

    public MultiMap handleResponse(String string) {
        if (!string.contains("=")) return MultiMap.caseInsensitiveMultiMap();
        String string2 = string.split("=")[0];
        String string3 = string.replace(string2 + "=", "");
        return MultiMap.caseInsensitiveMultiMap().add(string2, string3);
    }

    @Override
    public CompletableFuture initialise() {
        return CompletableFuture.completedFuture(true);
    }

    public boolean isBool(JsonObject jsonObject) {
        Object object = jsonObject.getValue("error", null);
        if (object == null) return false;
        return object instanceof Boolean;
    }

    @Override
    public CompletableFuture solve() {
        if (this.cachedResponse != null || System.currentTimeMillis() - this.lockoutTiming <= 5000L) {
            MultiMap multiMap = this.cachedResponse;
            this.lockoutTiming = 0L;
            this.cachedResponse = null;
            if (!this.logger.isDebugEnabled()) return CompletableFuture.completedFuture(multiMap);
            this.logger.debug("Double api sensor solving lockout hit. Returning cached response: {}", (Object)multiMap);
            return CompletableFuture.completedFuture(multiMap);
        }
        int n = 1;
        while (n <= 30) {
            try {
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Generating PX session via API attempt {}", (Object)n);
                }
                CompletableFuture completableFuture = Request.send(this.solveRequest());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXAPI.async$solve(this, n, completableFuture2, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse != null) {
                    boolean bl;
                    JsonObject jsonObject = (JsonObject)httpResponse.body();
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug("API sensor response received: {}", (Object)jsonObject.encodePrettily());
                    }
                    String string = jsonObject.getString("cookie", null);
                    boolean bl2 = bl = this.isBool(jsonObject) ? jsonObject.getBoolean("error", Boolean.valueOf(false)).booleanValue() : this.validateError(string);
                    if (bl || string == null || string.isEmpty()) {
                        if (this.logger.isDebugEnabled()) {
                            this.logger.debug("Invalid sensor api response: {}", (Object)jsonObject.encodePrettily());
                        }
                    } else if (jsonObject.containsKey("data")) {
                        this.cookieSession = jsonObject.getJsonObject("data");
                        this.userAgent = this.cookieSession.getString("UserAgent");
                        String string2 = Utils.parseChromeVer(this.userAgent);
                        this.secUA = "\"Google Chrome\";v=\"" + string2 + "\", \"Chromium\";v=\"" + string2 + "\", \";Not A Brand\";v=\"99\"";
                        this.deviceNumber = this.cookieSession.getString("deviceNumber");
                        MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
                        if (this.getVid() != null && !this.getVid().isBlank()) {
                            multiMap.add(VID_COOKIE, (CharSequence)this.getVid());
                        }
                        multiMap.add(RF_VALUE, ONE_VALUE);
                        multiMap.add(FP_VALUE, ONE_VALUE);
                        multiMap.add(CFP_VALUE, ONE_VALUE);
                        String string3 = this.cookieSession.getString("pxhdCookie", null);
                        if (string3 != null && !string3.isBlank()) {
                            multiMap.add(PXHD_VALUE, (CharSequence)string3);
                        }
                        if (!string.contains("=")) return CompletableFuture.completedFuture(this.handleResponse(string));
                        String string4 = string.split("=")[0];
                        String string5 = string.replace(string4 + "=", "");
                        multiMap.add(string4, string5);
                        return CompletableFuture.completedFuture(this.handleResponse(string));
                    }
                } else if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Solving sensor via API failed. No response received. Retrying...");
                } else {
                    this.logger.warn("Failed to solve sensor. Retrying...");
                }
            }
            catch (Throwable throwable) {
                this.logger.warn("Error solving sensor: {}. Retrying...", (Object)throwable.getMessage());
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug((Object)throwable);
                }
                CompletableFuture completableFuture = VertxUtil.randomSleep(3000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture3 = completableFuture;
                    return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> DesktopPXAPI.async$solve(this, n, completableFuture3, throwable, 2, arg_0));
                }
                completableFuture.join();
            }
            ++n;
        }
        return CompletableFuture.completedFuture(MultiMap.caseInsensitiveMultiMap());
    }

    public DesktopPXAPI(TaskActor taskActor) {
        super(taskActor, null);
        this.delegate = taskActor.getClient();
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$solve(DesktopPXAPI var0, int var1_1, CompletableFuture var2_3, Throwable var3_5, int var4_6, Object var5_8) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [6[CATCHBLOCK]], but top level block is 9[UNCONDITIONALDOLOOP]
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
    public String getDeviceLang() {
        return "en-GB,en;q=0.9";
    }

    @Override
    public void reset() {
        this.cookieSession = new JsonObject();
        this.userAgent = null;
        this.deviceNumber = "undefined";
    }

    @Override
    public String getVid() {
        return this.cookieSession.getString("vid", null);
    }

    static {
        DEFAULT_SEC_UA = AsciiString.cached((String)"\"Google Chrome\";v=\"95\", \"Chromium\";v=\"95\", \";Not A Brand\";v=\"99\"");
        VID_COOKIE = AsciiString.cached((String)"_pxvid");
        RF_VALUE = AsciiString.cached((String)"_pxff_rf");
        FP_VALUE = AsciiString.cached((String)"_pxff_fp");
        ONE_VALUE = AsciiString.cached((String)"1");
        CFP_VALUE = AsciiString.cached((String)"_pxff_cfp");
        PXHD_VALUE = AsciiString.cached((String)"_pxhd");
        site = Site.WALMART;
    }

    @Override
    public String getDeviceSecUA() {
        if (this.secUA != null) return this.secUA;
        return DEFAULT_SEC_UA.toString();
    }

    public HttpRequest captchaRequest() {
        return this.client.getAbs("https://pxgen-bmii2thzea-uc.a.run.app/holdcaptcha").as(BodyCodec.jsonObject()).addQueryParam("authToken", "PX-8E7DF802-E106-44D7-8E32-C26E7F8FE976").addQueryParam("site", site.toString().toLowerCase()).addQueryParam("region", "com").addQueryParam("proxy", this.getProxyString()).addQueryParam("deviceNumber", this.deviceNumber).addQueryParam("captchaData", this.cookieSession.encode());
    }

    @Override
    public String getDeviceUA() {
        if (this.userAgent != null) return this.userAgent;
        return DEFAULT_UA.toString();
    }
}

