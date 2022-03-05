/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.netty.handler.codec.http.cookie.Cookie
 *  io.vertx.core.buffer.Buffer
 *  io.vertx.core.json.JsonArray
 *  io.vertx.core.json.JsonObject
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.client.HttpResponse
 *  io.vertx.ext.web.codec.BodyCodec
 *  javax.mail.Message
 */
package io.trickle.task.sites.bestbuy;

import io.netty.handler.codec.http.cookie.Cookie;
import io.trickle.account.Account;
import io.trickle.account.AccountController;
import io.trickle.core.Controller;
import io.trickle.core.Engine;
import io.trickle.core.VertxSingleton;
import io.trickle.core.actor.TaskActor;
import io.trickle.harvester.LoginController;
import io.trickle.harvester.LoginHarvester;
import io.trickle.imap.MailClient;
import io.trickle.imap.MessageUtils;
import io.trickle.task.Task;
import io.trickle.task.sites.bestbuy.BestbuyAPI;
import io.trickle.task.sites.bestbuy.Encryption;
import io.trickle.task.sites.bestbuy.Login;
import io.trickle.task.sites.yeezy.Yeezy;
import io.trickle.util.Pair;
import io.trickle.util.Utils;
import io.trickle.util.analytics.Analytics;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.codec.BodyCodec;
import java.lang.invoke.LambdaMetafactory;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.Message;

public class Bestbuy
extends TaskActor {
    public Task task;
    public String a2ctransactioncode = null;
    public String a2ctransactionwait = null;
    public int successCounter = 0;
    public boolean browser;
    public static Pattern VERIFICATION_CODE;
    public boolean isQueue;
    public String a2ctransactionreferenceid = null;
    public static Pattern ORDER_FAIL_REASON;
    public BestbuyAPI api;
    public static Pattern orderDataPattern;
    public static Pattern UUID_PATTERN;
    public MailClient imapClient;
    public boolean preload;
    public static Pattern publicKeyPattern;

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$encryptCard(Bestbuy var0, CompletableFuture var1_1, int var2_2, Object var3_4) {
        switch (var2_2) {
            case 0: {
                v0 = var0.GETREQ("Initializing...", var0.api.fetchPublicKey(), 200, new String[]{"public"});
                if (!v0.isDone()) {
                    var4_5 = v0;
                    return var4_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$encryptCard(io.trickle.task.sites.bestbuy.Bestbuy java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (CompletableFuture)var4_5, (int)1));
                }
                ** GOTO lbl10
            }
            case 1: {
                v0 = var1_1;
lbl10:
                // 2 sources

                var1_1 = (String)v0.join();
                var2_3 = Utils.quickParseFirst((String)var1_1, new Pattern[]{Bestbuy.publicKeyPattern});
                var3_4 = var1_1.split("keyId\":\"")[1].split("\"")[0];
                var0.api.encryptedCard = Encryption.getFullEncrypted(var0.task.getProfile().getCardNumber(), var2_3, (String)var3_4);
                return CompletableFuture.completedFuture(null);
            }
        }
        throw new IllegalArgumentException();
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$POSTREQ(Bestbuy var0, String var1_1, HttpRequest var2_2, Object var3_3, Integer var4_4, Pair var5_5, String[] var6_6, CompletableFuture var7_7, HttpResponse var8_9, Throwable var9_11, int var10_12, Object var11_13) {
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

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$clearCart(Bestbuy var0, CompletableFuture var1_1, JsonObject var2_2, JsonArray var3_3, int var4_5, String var5_7, int var6_8, Object var7_9) {
        switch (var6_8) {
            case 0: {
                v0 = null;
                v1 = null;
                v2 = var0.GETREQ("Checking cart items", var0.api.getCartItems(), 200, new String[]{"cartV2"});
                if (!v2.isDone()) {
                    var5_7 = v2;
                    return var5_7.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$clearCart(io.trickle.task.sites.bestbuy.Bestbuy java.util.concurrent.CompletableFuture io.vertx.core.json.JsonObject io.vertx.core.json.JsonArray int java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (CompletableFuture)var5_7, null, null, (int)0, null, (int)1));
                }
                ** GOTO lbl14
            }
            case 1: {
                v1 = null;
                v0 = null;
                v2 = var1_1;
lbl14:
                // 2 sources

                var5_7 = (String)v2.join();
                var1_1 = new JsonObject((String)var5_7).getJsonObject("cart");
                var0.api.orderId = var1_1.getString("id");
                var2_2 = var1_1.getJsonArray("lineItems");
                var3_4 = 0;
lbl19:
                // 2 sources

                while (true) {
                    if (var2_2 == null) return CompletableFuture.completedFuture(null);
                    if (var3_4 >= var2_2.size()) return CompletableFuture.completedFuture(null);
                    var4_6 = var2_2.getJsonObject(var3_4).getString("id");
                    v3 = var0.GETREQ("Deleting item " + var3_4, var0.api.deleteItem(var4_6), 200, new String[]{"order"});
                    if (!v3.isDone()) {
                        var5_7 = v3;
                        return var5_7.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$clearCart(io.trickle.task.sites.bestbuy.Bestbuy java.util.concurrent.CompletableFuture io.vertx.core.json.JsonObject io.vertx.core.json.JsonArray int java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (CompletableFuture)var5_7, (JsonObject)var1_1, (JsonArray)var2_2, (int)var3_4, (String)var4_6, (int)2));
                    }
                    ** GOTO lbl35
                    break;
                }
            }
            case 2: {
                v3 = var1_1;
                v4 = var2_2;
                var4_6 = var5_7;
                var3_4 = var4_5;
                var2_2 = var3_3;
                var1_1 = v4;
lbl35:
                // 2 sources

                v3.join();
                ++var3_4;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public static int decodeUuidV1(String string) {
        String[] stringArray = string.split("-");
        return 10 * Integer.parseInt(stringArray[2] + stringArray[3], 16) / Integer.parseInt(stringArray[1], 16) * 100;
    }

    public CompletableFuture getLoginVerificationCode(Account account) {
        if (this.imapClient == null) {
            this.imapClient = MailClient.create(VertxSingleton.INSTANCE.get());
        }
        CompletableFuture completableFuture = this.imapClient.connectFut(account.getUser(), account.getPass());
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$getLoginVerificationCode(this, account, completableFuture2, null, 1, arg_0));
        }
        completableFuture.join();
        CompletableFuture completableFuture3 = this.imapClient.readInboxFuture(Login.SEARCH_TERM);
        if (!completableFuture3.isDone()) {
            CompletableFuture completableFuture4 = completableFuture3;
            return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$getLoginVerificationCode(this, account, completableFuture4, null, 2, arg_0));
        }
        Message[] messageArray = (Message[])completableFuture3.join();
        while (true) {
            if (messageArray.length != 0) {
                String string = MessageUtils.getTextFromMessage(messageArray[messageArray.length - 1]);
                return CompletableFuture.completedFuture(Utils.quickParseFirst(string, VERIFICATION_CODE));
            }
            this.logger.error("Waiting for email to arrive... [{}]", (Object)account.getUser());
            CompletableFuture completableFuture5 = VertxUtil.randomSleep(this.task.getRetryDelay());
            if (!completableFuture5.isDone()) {
                CompletableFuture completableFuture6 = completableFuture5;
                return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$getLoginVerificationCode(this, account, completableFuture6, messageArray, 3, arg_0));
            }
            completableFuture5.join();
            CompletableFuture completableFuture7 = this.imapClient.readInboxFuture(Login.SEARCH_TERM);
            if (!completableFuture7.isDone()) {
                CompletableFuture completableFuture8 = completableFuture7;
                return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$getLoginVerificationCode(this, account, completableFuture8, messageArray, 4, arg_0));
            }
            messageArray = (Message[])completableFuture7.join();
        }
    }

    public CompletableFuture freshenSession() {
        this.a2ctransactioncode = null;
        this.a2ctransactionreferenceid = null;
        String string = this.api.getCookies().getCookieValue("bm_sz");
        String string2 = this.api.getCookies().getCookieValue("_abck");
        this.api.getCookies().clear();
        this.api.getCookies().put("_abck", string2, ".bestbuy.com");
        CompletableFuture completableFuture = this.GETREQ("Refreshing session", this.api.productPage(), 200, "script type=\"text/javascript");
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$freshenSession(this, string, string2, completableFuture2, 1, arg_0));
        }
        String string3 = (String)completableFuture.join();
        this.updateSensorUrlFromHTML(string3);
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$GETREQ(Bestbuy var0, String var1_1, HttpRequest var2_2, Integer var3_3, String[] var4_4, int var5_5, CompletableFuture var6_8, HttpResponse var7_9, Throwable var8_11, int var9_12, Object var10_13) {
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
     * Could not resolve type clashes
     */
    public static CompletableFuture async$login(Bestbuy var0, String var1_1, CompletableFuture var2_2, String var3_3, CompletableFuture var4_4, String var5_5, String var6_6, AccountController var7_7, Account var8_8, String var9_9, JsonObject var10_10, String var11_11, int var12_12, Object var13_13) {
        switch (var12_12) {
            case 0: {
                v0 = var0.GETREQ("Fetching values", var0.api.loginPage(var1_1), 200, new String[]{"identity"});
                if (!v0.isDone()) {
                    var11_11 = v0;
                    return var11_11.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$login(io.trickle.task.sites.bestbuy.Bestbuy java.lang.String java.util.concurrent.CompletableFuture java.lang.String java.util.concurrent.CompletableFuture java.lang.String java.lang.String io.trickle.account.AccountController io.trickle.account.Account java.lang.String io.vertx.core.json.JsonObject java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (String)var1_1, var11_11, null, null, null, null, null, null, null, null, null, (int)1));
                }
                ** GOTO lbl10
            }
            case 1: {
                v0 = var2_2;
lbl10:
                // 2 sources

                var2_2 = (String)v0.join();
                var2_2 = var2_2.replace("</html>", "<script>document.querySelector(\"html\").innerHTML = `<h2>Waiting for completion</h2>`</script>\n</html>");
                var3_3 = LoginController.initBrowserLogin(var1_1, var0.api.getCookies().get(true, ".bestbuy.com", "/"), var0.api.proxyString(), var0.api.getCookies(), (String)var2_2);
                var0.api.login = Login.loginValues((String)var2_2);
                v1 = var0.GETREQ("Fetching key (1/2)", var0.api.ciaUserActivity(), 200, new String[]{"keyId"});
                if (!v1.isDone()) {
                    var11_11 = v1;
                    return var11_11.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$login(io.trickle.task.sites.bestbuy.Bestbuy java.lang.String java.util.concurrent.CompletableFuture java.lang.String java.util.concurrent.CompletableFuture java.lang.String java.lang.String io.trickle.account.AccountController io.trickle.account.Account java.lang.String io.vertx.core.json.JsonObject java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (String)var1_1, (CompletableFuture)var3_3, (String)var2_2, (CompletableFuture)var11_11, null, null, null, null, null, null, null, (int)2));
                }
                ** GOTO lbl24
            }
            case 2: {
                v1 = var4_4;
                v2 = var3_3;
                var3_3 = var2_2;
                var2_2 = v2;
lbl24:
                // 2 sources

                var4_4 = (String)v1.join();
                v3 = var0.GETREQ("Fetching key (2/2)", var0.api.emailGrid(), 200, new String[]{"keyId"});
                if (!v3.isDone()) {
                    var11_11 = v3;
                    return var11_11.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$login(io.trickle.task.sites.bestbuy.Bestbuy java.lang.String java.util.concurrent.CompletableFuture java.lang.String java.util.concurrent.CompletableFuture java.lang.String java.lang.String io.trickle.account.AccountController io.trickle.account.Account java.lang.String io.vertx.core.json.JsonObject java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (String)var1_1, (CompletableFuture)var3_3, (String)var2_2, (CompletableFuture)var11_11, (String)var4_4, null, null, null, null, null, null, (int)3));
                }
                ** GOTO lbl36
            }
            case 3: {
                v3 = var4_4;
                v4 = var3_3;
                var4_4 = var5_5;
                var3_3 = var2_2;
                var2_2 = v4;
lbl36:
                // 2 sources

                var5_5 = (String)v3.join();
                var6_6 = (AccountController)Engine.get().getModule(Controller.ACCOUNT);
                v5 = var6_6.findAccount(var0.task.getProfile().getEmail(), true).toCompletionStage().toCompletableFuture();
                if (!v5.isDone()) {
                    var11_11 = v5;
                    return var11_11.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$login(io.trickle.task.sites.bestbuy.Bestbuy java.lang.String java.util.concurrent.CompletableFuture java.lang.String java.util.concurrent.CompletableFuture java.lang.String java.lang.String io.trickle.account.AccountController io.trickle.account.Account java.lang.String io.vertx.core.json.JsonObject java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (String)var1_1, (CompletableFuture)var3_3, (String)var2_2, (CompletableFuture)var11_11, (String)var4_4, (String)var5_5, (AccountController)var6_6, null, null, null, null, (int)4));
                }
                ** GOTO lbl53
            }
            case 4: {
                v5 = var4_4;
                v6 = var3_3;
                v7 = var5_5;
                v8 = var6_6;
                var6_6 = var7_7;
                var5_5 = v8;
                var4_4 = v7;
                var3_3 = var2_2;
                var2_2 = v6;
lbl53:
                // 2 sources

                var7_7 = (Account)v5.join();
                v9 = var3_3;
                if (!v9.isDone()) {
                    var11_11 = v9;
                    return var11_11.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$login(io.trickle.task.sites.bestbuy.Bestbuy java.lang.String java.util.concurrent.CompletableFuture java.lang.String java.util.concurrent.CompletableFuture java.lang.String java.lang.String io.trickle.account.AccountController io.trickle.account.Account java.lang.String io.vertx.core.json.JsonObject java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (String)var1_1, (CompletableFuture)var3_3, (String)var2_2, var11_11, (String)var4_4, (String)var5_5, (AccountController)var6_6, (Account)var7_7, null, null, null, (int)5));
                }
                ** GOTO lbl71
            }
            case 5: {
                v9 = var4_4;
                v10 = var3_3;
                v11 = var5_5;
                v12 = var6_6;
                v13 = var7_7;
                var7_7 = var8_8;
                var6_6 = v13;
                var5_5 = v12;
                var4_4 = v11;
                var3_3 = var2_2;
                var2_2 = v10;
lbl71:
                // 2 sources

                v9.join();
                v14 = var0.POSTREQ("Logging in...", var0.api.login(), var0.api.accountLoginForm((Account)var7_7, new JsonObject((String)var4_4), new JsonObject(var5_5)), 200, null, new String[]{"status", "{"});
                if (!v14.isDone()) {
                    var11_11 = v14;
                    return var11_11.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$login(io.trickle.task.sites.bestbuy.Bestbuy java.lang.String java.util.concurrent.CompletableFuture java.lang.String java.util.concurrent.CompletableFuture java.lang.String java.lang.String io.trickle.account.AccountController io.trickle.account.Account java.lang.String io.vertx.core.json.JsonObject java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (String)var1_1, (CompletableFuture)var3_3, (String)var2_2, (CompletableFuture)var11_11, (String)var4_4, (String)var5_5, (AccountController)var6_6, (Account)var7_7, null, null, null, (int)6));
                }
                ** GOTO lbl90
            }
            case 6: {
                v14 = var4_4;
                v15 = var3_3;
                v16 = var5_5;
                v17 = var6_6;
                v18 = var7_7;
                var7_7 = var8_8;
                var6_6 = v18;
                var5_5 = v17;
                var4_4 = v16;
                var3_3 = var2_2;
                var2_2 = v15;
lbl90:
                // 2 sources

                if ((var8_8 = (String)v14.join()).contains("success")) {
                    return CompletableFuture.completedFuture(null);
                }
                if (var8_8.contains("stepUpRequired") == false) return CompletableFuture.completedFuture(null);
                var9_9 = new JsonObject((String)var8_8);
                var0.api.login.flowOptions = var9_9.getString("flowOptions");
                var0.api.login.challengeType = var9_9.getString("challengeType");
                if (!var0.api.login.challengeType.equals("2")) ** GOTO lbl121
                var0.logger.error("Account requires password reset");
                v19 = var0.POSTREQ("Selecting verification...", var0.api.pickVerification(), var0.api.pickVerificationJson(), 200, null, new String[]{"success"});
                if (!v19.isDone()) {
                    var11_11 = v19;
                    return var11_11.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$login(io.trickle.task.sites.bestbuy.Bestbuy java.lang.String java.util.concurrent.CompletableFuture java.lang.String java.util.concurrent.CompletableFuture java.lang.String java.lang.String io.trickle.account.AccountController io.trickle.account.Account java.lang.String io.vertx.core.json.JsonObject java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (String)var1_1, (CompletableFuture)var3_3, (String)var2_2, (CompletableFuture)var11_11, (String)var4_4, (String)var5_5, (AccountController)var6_6, (Account)var7_7, (String)var8_8, (JsonObject)var9_9, null, (int)7));
                }
                ** GOTO lbl119
            }
            case 7: {
                v19 = var4_4;
                v20 = var3_3;
                v21 = var5_5;
                v22 = var6_6;
                v23 = var7_7;
                v24 = var8_8;
                v25 = var9_9;
                var9_9 = var10_10 /* !! */ ;
                var8_8 = v25;
                var7_7 = v24;
                var6_6 = v23;
                var5_5 = v22;
                var4_4 = v21;
                var3_3 = var2_2;
                var2_2 = v20;
lbl119:
                // 2 sources

                v19.join();
lbl121:
                // 2 sources

                if (!(v26 = var0.getLoginVerificationCode((Account)var7_7)).isDone()) {
                    var11_11 = v26;
                    return var11_11.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$login(io.trickle.task.sites.bestbuy.Bestbuy java.lang.String java.util.concurrent.CompletableFuture java.lang.String java.util.concurrent.CompletableFuture java.lang.String java.lang.String io.trickle.account.AccountController io.trickle.account.Account java.lang.String io.vertx.core.json.JsonObject java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (String)var1_1, (CompletableFuture)var3_3, (String)var2_2, (CompletableFuture)var11_11, (String)var4_4, (String)var5_5, (AccountController)var6_6, (Account)var7_7, (String)var8_8, (JsonObject)var9_9, null, (int)8));
                }
                ** GOTO lbl141
            }
            case 8: {
                v26 = var4_4;
                v27 = var3_3;
                v28 = var5_5;
                v29 = var6_6;
                v30 = var7_7;
                v31 = var8_8;
                v32 = var9_9;
                var9_9 = var10_10 /* !! */ ;
                var8_8 = v32;
                var7_7 = v31;
                var6_6 = v30;
                var5_5 = v29;
                var4_4 = v28;
                var3_3 = var2_2;
                var2_2 = v27;
lbl141:
                // 2 sources

                var10_10 /* !! */  = (String)v26.join();
                v33 = var0.POSTREQ("Verifying Account...", var0.api.verificationCode(), var0.api.verificationJson((String)var10_10 /* !! */ ), 200, null, new String[]{"status"});
                if (!v33.isDone()) {
                    var11_11 = v33;
                    return var11_11.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$login(io.trickle.task.sites.bestbuy.Bestbuy java.lang.String java.util.concurrent.CompletableFuture java.lang.String java.util.concurrent.CompletableFuture java.lang.String java.lang.String io.trickle.account.AccountController io.trickle.account.Account java.lang.String io.vertx.core.json.JsonObject java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (String)var1_1, (CompletableFuture)var3_3, (String)var2_2, (CompletableFuture)var11_11, (String)var4_4, (String)var5_5, (AccountController)var6_6, (Account)var7_7, (String)var8_8, (JsonObject)var9_9, (String)var10_10 /* !! */ , (int)9));
                }
                ** GOTO lbl165
            }
            case 9: {
                v33 = var4_4;
                v34 = var3_3;
                v35 = var5_5;
                v36 = var6_6;
                v37 = var7_7;
                v38 = var8_8;
                v39 = var9_9;
                v40 = var10_10 /* !! */ ;
                var10_10 /* !! */  = var11_11;
                var9_9 = v40;
                var8_8 = v39;
                var7_7 = v38;
                var6_6 = v37;
                var5_5 = v36;
                var4_4 = v35;
                var3_3 = var2_2;
                var2_2 = v34;
lbl165:
                // 2 sources

                v33.join();
                return CompletableFuture.completedFuture(null);
            }
        }
        throw new IllegalArgumentException();
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$atc(Bestbuy var0, int var1_1, CompletableFuture var2_2, HttpResponse var3_4, String var4_5, int var5_7, int var6_9, Throwable var7_11, int var8_12, Object var9_14) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [16[CATCHBLOCK]], but top level block is 30[UNCONDITIONALDOLOOP]
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

    public Bestbuy(Task task, int n) {
        super(n);
        this.task = task;
        this.api = new BestbuyAPI(this.task);
        super.setClient(this.api);
        this.browser = this.task.getMode().contains("login");
        this.preload = this.task.getMode().contains("preload");
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$initLoginHarvesters(Bestbuy var0, LoginHarvester[] var1_1, int var2_2, int var3_3, Object var4_4, CompletableFuture var5_5, int var6_6, Object var7_7) {
        switch (var6_6) {
            case 0: {
                if (var0.browser == false) return CompletableFuture.completedFuture(null);
                var1_1 = LoginHarvester.LOGIN_HARVESTERS;
                var2_2 = var1_1.length;
                var3_3 = 0;
lbl7:
                // 2 sources

                while (true) {
                    if (var3_3 >= var2_2) return CompletableFuture.completedFuture(null);
                    var4_4 = var1_1[var3_3];
                    v0 = var4_4.start();
                    if (!v0.isDone()) {
                        var5_5 = v0;
                        return var5_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$initLoginHarvesters(io.trickle.task.sites.bestbuy.Bestbuy io.trickle.harvester.LoginHarvester[] int int java.lang.Object java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (LoginHarvester[])var1_1, (int)var2_2, (int)var3_3, (Object)var4_4, (CompletableFuture)var5_5, (int)1));
                    }
                    ** GOTO lbl17
                    break;
                }
            }
            case 1: {
                v0 = var5_5;
lbl17:
                // 2 sources

                v0.join();
                ++var3_3;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture placeOrder() {
        int n = 0;
        String string = "";
        while (true) {
            if (string.contains("state\":\"SUBMITTED")) {
                if (!string.contains("orderAlreadySubmitted")) return CompletableFuture.completedFuture(string);
            }
            CompletableFuture completableFuture = this.POSTREQ("Placing order...", this.api.placeOrder(), this.api.placeOrderForm(), null, null, "{");
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$placeOrder(this, n, string, completableFuture2, null, 1, arg_0));
            }
            string = (String)completableFuture.join();
            String string2 = Utils.quickParseFirst(string, ORDER_FAIL_REASON);
            if (string2 == null) continue;
            if (string2.equals("BAD_REQUEST") || string2.equals("UNAUTHORIZED")) {
                if (++n >= 3) {
                    throw new Throwable("Checkout expired. Restarting task...");
                }
                this.logger.error("Placing order \"soft-ban\"");
                this.api.rotateProxy();
            } else {
                n = 0;
                CompletableFuture completableFuture3 = this.POSTREQ("Handling -> " + string2, this.api.submitContact(), this.api.contactJson(), 200, null, "\"quantity\":1,");
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$placeOrder(this, n, string, completableFuture4, string2, 2, arg_0));
                }
                completableFuture3.join();
            }
            CompletableFuture completableFuture5 = this.GETREQ("Refreshing checkout", this.api.refreshCheckout(), 200, null);
            if (!completableFuture5.isDone()) {
                CompletableFuture completableFuture6 = completableFuture5;
                return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$placeOrder(this, n, string, completableFuture6, string2, 3, arg_0));
            }
            completableFuture5.join();
            if (string2.contains("CC_REQ_ERROR")) {
                CompletableFuture completableFuture7 = this.POSTREQ("Submitting payment", this.api.vaultCC(), this.api.vaultJson(), 200, null, "{\"paymentId\":\"");
                if (!completableFuture7.isDone()) {
                    CompletableFuture completableFuture8 = completableFuture7;
                    return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$placeOrder(this, n, string, completableFuture8, string2, 4, arg_0));
                }
                completableFuture7.join();
                CompletableFuture completableFuture9 = this.POSTREQ("Checking payment", this.api.refreshPayment(), Buffer.buffer((String)"{}"), 200, null, "{\"productTotal\":\"");
                if (!completableFuture9.isDone()) {
                    CompletableFuture completableFuture10 = completableFuture9;
                    return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$placeOrder(this, n, string, completableFuture10, string2, 5, arg_0));
                }
                completableFuture9.join();
                continue;
            }
            if (string2.contains("CC_AUTH_FAILURE")) {
                this.logger.error("CARD DECLINED");
                CompletableFuture completableFuture11 = this.POSTREQ("Submitting payment", this.api.vaultCC(), this.api.vaultJson(), 200, null, "{\"paymentId\":\"");
                if (!completableFuture11.isDone()) {
                    CompletableFuture completableFuture12 = completableFuture11;
                    return ((CompletableFuture)completableFuture12.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$placeOrder(this, n, string, completableFuture12, string2, 6, arg_0));
                }
                completableFuture11.join();
                CompletableFuture completableFuture13 = this.POSTREQ("Checking payment", this.api.refreshPayment(), Buffer.buffer((String)"{}"), 200, null, "{\"productTotal\":\"");
                if (!completableFuture13.isDone()) {
                    CompletableFuture completableFuture14 = completableFuture13;
                    return ((CompletableFuture)completableFuture14.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$placeOrder(this, n, string, completableFuture14, string2, 7, arg_0));
                }
                completableFuture13.join();
                continue;
            }
            if (string2.contains("EMPTY_CID")) {
                this.logger.error("Resubmitting CC");
                CompletableFuture completableFuture15 = this.POSTREQ("Submitting payment", this.api.vaultCC(), this.api.vaultJson(), 200, null, "{\"paymentId\":\"");
                if (!completableFuture15.isDone()) {
                    CompletableFuture completableFuture16 = completableFuture15;
                    return ((CompletableFuture)completableFuture16.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$placeOrder(this, n, string, completableFuture16, string2, 8, arg_0));
                }
                completableFuture15.join();
                CompletableFuture completableFuture17 = this.POSTREQ("Checking payment", this.api.refreshPayment(), Buffer.buffer((String)"{}"), 200, null, "{\"productTotal\":\"");
                if (!completableFuture17.isDone()) {
                    CompletableFuture completableFuture18 = completableFuture17;
                    return ((CompletableFuture)completableFuture18.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$placeOrder(this, n, string, completableFuture18, string2, 9, arg_0));
                }
                completableFuture17.join();
                continue;
            }
            if (string2.contains("EMAIL_MISSING")) {
                this.logger.error("Resubmitting EMAIL");
                CompletableFuture completableFuture19 = this.POSTREQ("Submitting email", this.api.submitEmail(), this.api.emailJson(), 200, null, "\"quantity\":1,");
                if (!completableFuture19.isDone()) {
                    CompletableFuture completableFuture20 = completableFuture19;
                    return ((CompletableFuture)completableFuture20.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$placeOrder(this, n, string, completableFuture20, string2, 10, arg_0));
                }
                completableFuture19.join();
                continue;
            }
            CompletableFuture completableFuture21 = VertxUtil.randomSleep(this.task.getMonitorDelay());
            if (!completableFuture21.isDone()) {
                CompletableFuture completableFuture22 = completableFuture21;
                return ((CompletableFuture)completableFuture22.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$placeOrder(this, n, string, completableFuture22, string2, 11, arg_0));
            }
            completableFuture21.join();
        }
    }

    public CompletableFuture encryptCard() {
        CompletableFuture completableFuture = this.GETREQ("Initializing...", this.api.fetchPublicKey(), 200, "public");
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$encryptCard(this, completableFuture2, 1, arg_0));
        }
        String string = (String)completableFuture.join();
        String string2 = Utils.quickParseFirst(string, publicKeyPattern);
        String string3 = string.split("keyId\":\"")[1].split("\"")[0];
        this.api.encryptedCard = Encryption.getFullEncrypted(this.task.getProfile().getCardNumber(), string2, string3);
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture atc(boolean bl) {
        CompletableFuture completableFuture = this.sendSensor();
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$atc(this, (int)(bl ? 1 : 0), completableFuture2, null, null, 0, 0, null, 1, arg_0));
        }
        completableFuture.join();
        this.logger.info("Waiting for restock");
        while (this.running) {
            try {
                CompletableFuture completableFuture3 = this.sendEnforcedReq(this.api.atc(this.a2ctransactioncode, this.a2ctransactionreferenceid), bl ? this.api.atcForm("4900942") : this.api.atcForm());
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$atc(this, (int)(bl ? 1 : 0), completableFuture4, null, null, 0, 0, null, 2, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture3.join();
                if (httpResponse != null) {
                    String string = httpResponse.bodyAsString();
                    if (httpResponse.statusCode() == 200 && string.contains("\"cartCount\":1")) {
                        VertxUtil.sendSignal(this.task.getKeywords()[0]);
                        this.logger.info("Successfully atc! Cookie -> [{}]", (Object)Analytics.exportVT(this.api.getCookies()));
                        return CompletableFuture.completedFuture(null);
                    }
                    if (string.contains("redirectUrl")) {
                        return CompletableFuture.completedFuture(httpResponse.bodyAsJsonObject().getString("redirectUrl"));
                    }
                    if (string.contains("CONSTRAINED_ITEM")) {
                        if (this.a2ctransactioncode != null) {
                            this.logger.error("Queue looped (semi-normal)");
                            CompletableFuture completableFuture5 = this.freshenSession();
                            if (!completableFuture5.isDone()) {
                                CompletableFuture completableFuture6 = completableFuture5;
                                return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$atc(this, (int)(bl ? 1 : 0), completableFuture6, httpResponse, string, 0, 0, null, 3, arg_0));
                            }
                            completableFuture5.join();
                            CompletableFuture completableFuture7 = VertxUtil.randomSleep(this.task.getRetryDelay());
                            if (!completableFuture7.isDone()) {
                                CompletableFuture completableFuture8 = completableFuture7;
                                return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$atc(this, (int)(bl ? 1 : 0), completableFuture8, httpResponse, string, 0, 0, null, 4, arg_0));
                            }
                            completableFuture7.join();
                            continue;
                        }
                        this.a2ctransactioncode = httpResponse.getHeader("a2ctransactioncode");
                        this.a2ctransactionreferenceid = httpResponse.getHeader("a2ctransactionreferenceid");
                        this.a2ctransactionwait = httpResponse.getHeader("a2ctransactionwait");
                        int n = this.a2ctransactionwait == null ? Bestbuy.decodeUuidV1(this.a2ctransactioncode) : Integer.parseInt(this.a2ctransactionwait) * 1000;
                        int n2 = 240000;
                        if (this.a2ctransactionwait != null) {
                            this.a2ctransactioncode = Bestbuy.genCustomUuidV1(n);
                        }
                        if (this.task.getKeywords().length == 2 && Utils.isInteger(this.task.getKeywords()[1])) {
                            n2 = Integer.parseInt(this.task.getKeywords()[1]);
                        }
                        if (n > n2) {
                            this.logger.info("Queue is {} minutes long. [{}] Attempting to get a lower queue time...", (Object)((double)(n / 1000) / Double.longBitsToDouble(0x404E000000000000L)), (Object)this.task.getKeywords()[0]);
                            CompletableFuture completableFuture9 = this.freshenSession();
                            if (!completableFuture9.isDone()) {
                                CompletableFuture completableFuture10 = completableFuture9;
                                return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$atc(this, (int)(bl ? 1 : 0), completableFuture10, httpResponse, string, n, n2, null, 5, arg_0));
                            }
                            completableFuture9.join();
                            CompletableFuture completableFuture11 = VertxUtil.randomSleep(this.task.getRetryDelay());
                            if (!completableFuture11.isDone()) {
                                CompletableFuture completableFuture12 = completableFuture11;
                                return ((CompletableFuture)completableFuture12.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$atc(this, (int)(bl ? 1 : 0), completableFuture12, httpResponse, string, n, n2, null, 6, arg_0));
                            }
                            completableFuture11.join();
                            continue;
                        }
                        this.logger.info("Queue is {} minutes long. Waiting...", (Object)((double)(n / 1000) / Double.longBitsToDouble(0x404E000000000000L)));
                        CompletableFuture completableFuture13 = VertxUtil.hardCodedSleep(n + 3000);
                        if (!completableFuture13.isDone()) {
                            CompletableFuture completableFuture14 = completableFuture13;
                            return ((CompletableFuture)completableFuture14.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$atc(this, (int)(bl ? 1 : 0), completableFuture14, httpResponse, string, n, n2, null, 7, arg_0));
                        }
                        completableFuture13.join();
                        this.logger.info("Passed queue!");
                        continue;
                    }
                    if (string.contains("Sorry, there was a problem adding the item to your cart.")) {
                        this.logger.info("Error waiting for restock/queue. Site down or temp ban.");
                        CompletableFuture completableFuture15 = this.freshenSession();
                        if (!completableFuture15.isDone()) {
                            CompletableFuture completableFuture16 = completableFuture15;
                            return ((CompletableFuture)completableFuture16.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$atc(this, (int)(bl ? 1 : 0), completableFuture16, httpResponse, string, 0, 0, null, 8, arg_0));
                        }
                        completableFuture15.join();
                        this.api.rotateProxy();
                    } else if (string.contains("ITEM_NOT_SELLABLE")) {
                        this.logger.info("Item not for online sale.");
                        if (this.browser) {
                            CompletableFuture completableFuture17 = this.freshenSession();
                            if (!completableFuture17.isDone()) {
                                CompletableFuture completableFuture18 = completableFuture17;
                                return ((CompletableFuture)completableFuture18.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$atc(this, (int)(bl ? 1 : 0), completableFuture18, httpResponse, string, 0, 0, null, 9, arg_0));
                            }
                            completableFuture17.join();
                        } else {
                            this.api.getCookies().removeAnyMatch("vt");
                            this.api.getCookies().put("vt", UUID.randomUUID().toString(), ".bestbuy.com");
                        }
                        if (++this.successCounter % 6 == 0) {
                            CompletableFuture completableFuture19 = this.sendSensor();
                            if (!completableFuture19.isDone()) {
                                CompletableFuture completableFuture20 = completableFuture19;
                                return ((CompletableFuture)completableFuture20.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$atc(this, (int)(bl ? 1 : 0), completableFuture20, httpResponse, string, 0, 0, null, 10, arg_0));
                            }
                            completableFuture19.join();
                        }
                    } else {
                        this.logger.warn("Error waiting for atc/queue: '{}'", (Object)(httpResponse.bodyAsString() + httpResponse.statusCode() + httpResponse.statusMessage()));
                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy");
                        JsonArray jsonArray = new JsonArray();
                        for (Cookie cookie : this.api.getCookies().get(true, "bestbuy.com", "/")) {
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.put("domain", (Object)cookie.domain());
                            jsonObject.put("name", (Object)cookie.name());
                            jsonObject.put("path", (Object)cookie.path());
                            jsonObject.put("value", (Object)cookie.value());
                            jsonArray.add((Object)jsonObject);
                        }
                    }
                }
                CompletableFuture completableFuture21 = VertxUtil.signalSleep(this.task.getKeywords()[0], this.task.getMonitorDelay());
                if (!completableFuture21.isDone()) {
                    CompletableFuture completableFuture22 = completableFuture21;
                    return ((CompletableFuture)completableFuture22.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$atc(this, (int)(bl ? 1 : 0), completableFuture22, httpResponse, null, 0, 0, null, 11, arg_0));
                }
                completableFuture21.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error waiting for restock: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture23 = VertxUtil.randomSleep(this.task.getRetryDelay());
                if (!completableFuture23.isDone()) {
                    CompletableFuture completableFuture24 = completableFuture23;
                    return ((CompletableFuture)completableFuture24.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$atc(this, (int)(bl ? 1 : 0), completableFuture24, null, null, 0, 0, throwable, 12, arg_0));
                }
                completableFuture23.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$getLineId(Bestbuy var0, CompletableFuture var1_1, int var2_2, Object var3_4) {
        switch (var2_2) {
            case 0: {
                v0 = var0.GETREQ("Starting checkout", var0.api.checkoutPage(), 200, new String[]{"orderData = {\"id\":\""});
                if (!v0.isDone()) {
                    var3_4 = v0;
                    return var3_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$getLineId(io.trickle.task.sites.bestbuy.Bestbuy java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (CompletableFuture)var3_4, (int)1));
                }
                ** GOTO lbl10
            }
            case 1: {
                v0 = var1_1;
lbl10:
                // 2 sources

                var1_1 = (String)v0.join();
                var2_3 = new JsonObject(Objects.requireNonNull(Utils.quickParseFirst((String)var1_1, new Pattern[]{Bestbuy.orderDataPattern})));
                var0.api.cartId = var2_3.getJsonArray("items").getJsonObject(0).getString("id");
                var0.api.id = var2_3.getString("id");
                var0.api.orderId = var2_3.getString("customerOrderId");
                var0.api.paymentId = var2_3.getJsonObject("payment").getString("id");
                return CompletableFuture.completedFuture(null);
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture getLineId() {
        CompletableFuture completableFuture = this.GETREQ("Starting checkout", this.api.checkoutPage(), 200, "orderData = {\"id\":\"");
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$getLineId(this, completableFuture2, 1, arg_0));
        }
        String string = (String)completableFuture.join();
        JsonObject jsonObject = new JsonObject(Objects.requireNonNull(Utils.quickParseFirst(string, orderDataPattern)));
        this.api.cartId = jsonObject.getJsonArray("items").getJsonObject(0).getString("id");
        this.api.id = jsonObject.getString("id");
        this.api.orderId = jsonObject.getString("customerOrderId");
        this.api.paymentId = jsonObject.getJsonObject("payment").getString("id");
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture sendEnforcedReq(HttpRequest httpRequest, Object object) {
        CompletableFuture completableFuture = Request.send(httpRequest, object);
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$sendEnforcedReq$1(this, httpRequest, object, completableFuture2, null, 1, arg_0));
        }
        HttpResponse httpResponse = (HttpResponse)completableFuture.join();
        while (true) {
            if (httpResponse != null) {
                if (httpResponse.statusCode() != 403) return CompletableFuture.completedFuture(httpResponse);
            }
            this.successCounter = 0;
            CompletableFuture completableFuture3 = this.sendSensor();
            if (!completableFuture3.isDone()) {
                CompletableFuture completableFuture4 = completableFuture3;
                return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$sendEnforcedReq$1(this, httpRequest, object, completableFuture4, httpResponse, 2, arg_0));
            }
            completableFuture3.join();
            CompletableFuture completableFuture5 = VertxUtil.hardCodedSleep(500L);
            if (!completableFuture5.isDone()) {
                CompletableFuture completableFuture6 = completableFuture5;
                return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$sendEnforcedReq$1(this, httpRequest, object, completableFuture6, httpResponse, 3, arg_0));
            }
            completableFuture5.join();
            CompletableFuture completableFuture7 = Request.send(httpRequest, object);
            if (!completableFuture7.isDone()) {
                CompletableFuture completableFuture8 = completableFuture7;
                return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$sendEnforcedReq$1(this, httpRequest, object, completableFuture8, httpResponse, 4, arg_0));
            }
            httpResponse = (HttpResponse)completableFuture7.join();
        }
    }

    public void updateSensorUrlFromHTML(String string) {
        if (string == null) {
            this.logger.error("Unable to update sensor URL. Continuing");
            return;
        }
        Matcher matcher = Yeezy.SENSOR_URL_PATTERN.matcher(string);
        String string2 = "/2yECBVJi5xf17/zRM/BInoAzUb5KA/YDGOzGht/VjtXHEtQXw/Wmt4/NTM9SV8";
        while (true) {
            if (!matcher.find()) {
                this.api.setSensorUrl("https://www.bestbuy.com" + string2);
                return;
            }
            string2 = matcher.group(1);
        }
    }

    public static String genCustomUuidV1(int n) {
        String string = String.valueOf(System.currentTimeMillis() + (long)n + 1L);
        return String.join((CharSequence)"-", Utils.quickParseAllGroups((string + string + string).substring(0, 32), UUID_PATTERN));
    }

    public CompletableFuture POSTREQ(String string, HttpRequest httpRequest, Object object, Integer n, Pair pair, String ... stringArray) {
        this.logger.info(string);
        while (this.running) {
            try {
                CompletableFuture completableFuture = this.sendEnforcedReq(httpRequest, object);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$POSTREQ(this, string, httpRequest, object, n, pair, stringArray, completableFuture2, null, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse != null) {
                    boolean bl;
                    if (httpResponse.statusCode() == 302) {
                        bl = stringArray == null || httpResponse.getHeader("location").contains(stringArray[0]);
                    } else {
                        boolean bl2 = bl = stringArray == null || Utils.containsAllWords(httpResponse.bodyAsString(), stringArray);
                    }
                    if ((n == null || httpResponse.statusCode() == n.intValue()) && bl) {
                        String string2;
                        if (httpResponse.statusCode() == 302) {
                            string2 = httpResponse.getHeader("location");
                            return CompletableFuture.completedFuture(string2);
                        }
                        string2 = httpResponse.bodyAsString();
                        return CompletableFuture.completedFuture(string2);
                    }
                    if (pair != null) {
                        if (httpResponse.statusCode() >= 400) {
                            this.logger.warn("Failed " + string.toLowerCase(Locale.ROOT) + ": '{}'", (Object)httpResponse.bodyAsString().replace("\n", ""));
                        } else if (pair.first instanceof Integer && ((Integer)pair.first).intValue() == httpResponse.statusCode()) {
                            this.logger.warn("Failed " + string.toLowerCase(Locale.ROOT) + ": '{}'", pair.second);
                        } else if (httpResponse.bodyAsString().contains(pair.first.toString())) {
                            this.logger.warn("Failed " + string.toLowerCase(Locale.ROOT) + ": '{}'", pair.second);
                        } else if (httpResponse.statusCode() == 302 && httpResponse.getHeader("location").contains(pair.first.toString())) {
                            this.logger.warn("Failed " + string.toLowerCase(Locale.ROOT) + ": '{}'", pair.second);
                        } else {
                            this.logger.warn("Failed " + string.toLowerCase(Locale.ROOT) + ": '{}'", (Object)(httpResponse.statusCode() + httpResponse.statusMessage()));
                        }
                    } else {
                        this.logger.warn("Failed " + string.toLowerCase(Locale.ROOT) + ": '{}'", (Object)(httpResponse.statusCode() + httpResponse.statusMessage()));
                    }
                    this.logger.debug(httpResponse.bodyAsString().replace("\n", ""));
                }
                CompletableFuture completableFuture3 = VertxUtil.randomSleep(this.task.getMonitorDelay());
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$POSTREQ(this, string, httpRequest, object, n, pair, stringArray, completableFuture4, httpResponse, null, 2, arg_0));
                }
                completableFuture3.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error " + string.toLowerCase(Locale.ROOT) + ": {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = VertxUtil.randomSleep(this.task.getRetryDelay());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture5 = completableFuture;
                    return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$POSTREQ(this, string, httpRequest, object, n, pair, stringArray, completableFuture5, null, throwable, 3, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture sendEnforcedReq(HttpRequest httpRequest) {
        CompletableFuture completableFuture = Request.send(httpRequest);
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$sendEnforcedReq(this, httpRequest, completableFuture2, null, 1, arg_0));
        }
        HttpResponse httpResponse = (HttpResponse)completableFuture.join();
        while (true) {
            if (httpResponse != null) {
                if (httpResponse.statusCode() != 403) return CompletableFuture.completedFuture(httpResponse);
            }
            this.successCounter = 0;
            CompletableFuture completableFuture3 = this.sendSensor();
            if (!completableFuture3.isDone()) {
                CompletableFuture completableFuture4 = completableFuture3;
                return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$sendEnforcedReq(this, httpRequest, completableFuture4, httpResponse, 2, arg_0));
            }
            completableFuture3.join();
            CompletableFuture completableFuture5 = VertxUtil.hardCodedSleep(500L);
            if (!completableFuture5.isDone()) {
                CompletableFuture completableFuture6 = completableFuture5;
                return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$sendEnforcedReq(this, httpRequest, completableFuture6, httpResponse, 3, arg_0));
            }
            completableFuture5.join();
            CompletableFuture completableFuture7 = Request.send(httpRequest);
            if (!completableFuture7.isDone()) {
                CompletableFuture completableFuture8 = completableFuture7;
                return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$sendEnforcedReq(this, httpRequest, completableFuture8, httpResponse, 4, arg_0));
            }
            httpResponse = (HttpResponse)completableFuture7.join();
        }
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$placeOrder(Bestbuy var0, int var1_1, String var2_2, CompletableFuture var3_3, String var4_4, int var5_5, Object var6_6) {
        switch (var5_5) {
            case 0: {
                var1_1 = 0;
                var2_2 = "";
lbl5:
                // 7 sources

                while (true) {
                    if (var2_2.contains("state\":\"SUBMITTED")) {
                        if (var2_2.contains("orderAlreadySubmitted") == false) return CompletableFuture.completedFuture(var2_2);
                    }
                    if (!(v0 = var0.POSTREQ("Placing order...", var0.api.placeOrder(), var0.api.placeOrderForm(), null, null, new String[]{"{"})).isDone()) {
                        var4_4 = v0;
                        return var4_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$placeOrder(io.trickle.task.sites.bestbuy.Bestbuy int java.lang.String java.util.concurrent.CompletableFuture java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (int)var1_1, (String)var2_2, (CompletableFuture)var4_4, null, (int)1));
                    }
                    ** GOTO lbl14
                    break;
                }
            }
            case 1: {
                v0 = var3_3;
lbl14:
                // 2 sources

                if ((var3_3 = Utils.quickParseFirst(var2_2 = (String)v0.join(), new Pattern[]{Bestbuy.ORDER_FAIL_REASON})) == null) ** GOTO lbl5
                if (!var3_3.equals("BAD_REQUEST") && !var3_3.equals("UNAUTHORIZED")) ** GOTO lbl22
                if (++var1_1 >= 3) {
                    throw new Throwable("Checkout expired. Restarting task...");
                }
                var0.logger.error("Placing order \"soft-ban\"");
                var0.api.rotateProxy();
                ** GOTO lbl33
lbl22:
                // 1 sources

                var1_1 = 0;
                v1 = var0.POSTREQ("Handling -> " + (String)var3_3, var0.api.submitContact(), var0.api.contactJson(), 200, null, new String[]{"\"quantity\":1,"});
                if (!v1.isDone()) {
                    var4_4 = v1;
                    return var4_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$placeOrder(io.trickle.task.sites.bestbuy.Bestbuy int java.lang.String java.util.concurrent.CompletableFuture java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (int)var1_1, (String)var2_2, (CompletableFuture)var4_4, (String)var3_3, (int)2));
                }
                ** GOTO lbl31
            }
            case 2: {
                v1 = var3_3;
                var3_3 = var4_4;
lbl31:
                // 2 sources

                v1.join();
lbl33:
                // 2 sources

                if (!(v2 = var0.GETREQ("Refreshing checkout", var0.api.refreshCheckout(), 200, null)).isDone()) {
                    var4_4 = v2;
                    return var4_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$placeOrder(io.trickle.task.sites.bestbuy.Bestbuy int java.lang.String java.util.concurrent.CompletableFuture java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (int)var1_1, (String)var2_2, (CompletableFuture)var4_4, (String)var3_3, (int)3));
                }
                ** GOTO lbl40
            }
            case 3: {
                v2 = var3_3;
                var3_3 = var4_4;
lbl40:
                // 2 sources

                v2.join();
                if (!var3_3.contains("CC_REQ_ERROR")) ** GOTO lbl48
                v3 = var0.POSTREQ("Submitting payment", var0.api.vaultCC(), var0.api.vaultJson(), 200, null, new String[]{"{\"paymentId\":\""});
                if (!v3.isDone()) {
                    var4_4 = v3;
                    return var4_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$placeOrder(io.trickle.task.sites.bestbuy.Bestbuy int java.lang.String java.util.concurrent.CompletableFuture java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (int)var1_1, (String)var2_2, (CompletableFuture)var4_4, (String)var3_3, (int)4));
                }
                ** GOTO lbl77
lbl48:
                // 1 sources

                if (!var3_3.contains("CC_AUTH_FAILURE")) ** GOTO lbl55
                var0.logger.error("CARD DECLINED");
                v4 = var0.POSTREQ("Submitting payment", var0.api.vaultCC(), var0.api.vaultJson(), 200, null, new String[]{"{\"paymentId\":\""});
                if (!v4.isDone()) {
                    var4_4 = v4;
                    return var4_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$placeOrder(io.trickle.task.sites.bestbuy.Bestbuy int java.lang.String java.util.concurrent.CompletableFuture java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (int)var1_1, (String)var2_2, (CompletableFuture)var4_4, (String)var3_3, (int)6));
                }
                ** GOTO lbl93
lbl55:
                // 1 sources

                if (!var3_3.contains("EMPTY_CID")) ** GOTO lbl62
                var0.logger.error("Resubmitting CC");
                v5 = var0.POSTREQ("Submitting payment", var0.api.vaultCC(), var0.api.vaultJson(), 200, null, new String[]{"{\"paymentId\":\""});
                if (!v5.isDone()) {
                    var4_4 = v5;
                    return var4_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$placeOrder(io.trickle.task.sites.bestbuy.Bestbuy int java.lang.String java.util.concurrent.CompletableFuture java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (int)var1_1, (String)var2_2, (CompletableFuture)var4_4, (String)var3_3, (int)8));
                }
                ** GOTO lbl109
lbl62:
                // 1 sources

                if (!var3_3.contains("EMAIL_MISSING")) ** GOTO lbl69
                var0.logger.error("Resubmitting EMAIL");
                v6 = var0.POSTREQ("Submitting email", var0.api.submitEmail(), var0.api.emailJson(), 200, null, new String[]{"\"quantity\":1,"});
                if (!v6.isDone()) {
                    var4_4 = v6;
                    return var4_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$placeOrder(io.trickle.task.sites.bestbuy.Bestbuy int java.lang.String java.util.concurrent.CompletableFuture java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (int)var1_1, (String)var2_2, (CompletableFuture)var4_4, (String)var3_3, (int)10));
                }
                ** GOTO lbl125
lbl69:
                // 1 sources

                v7 = VertxUtil.randomSleep(var0.task.getMonitorDelay());
                if (!v7.isDone()) {
                    var4_4 = v7;
                    return var4_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$placeOrder(io.trickle.task.sites.bestbuy.Bestbuy int java.lang.String java.util.concurrent.CompletableFuture java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (int)var1_1, (String)var2_2, (CompletableFuture)var4_4, (String)var3_3, (int)11));
                }
                ** GOTO lbl131
            }
            case 4: {
                v3 = var3_3;
                var3_3 = var4_4;
lbl77:
                // 2 sources

                v3.join();
                v8 = var0.POSTREQ("Checking payment", var0.api.refreshPayment(), Buffer.buffer((String)"{}"), 200, null, new String[]{"{\"productTotal\":\""});
                if (!v8.isDone()) {
                    var4_4 = v8;
                    return var4_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$placeOrder(io.trickle.task.sites.bestbuy.Bestbuy int java.lang.String java.util.concurrent.CompletableFuture java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (int)var1_1, (String)var2_2, (CompletableFuture)var4_4, (String)var3_3, (int)5));
                }
                ** GOTO lbl87
            }
            case 5: {
                v8 = var3_3;
                var3_3 = var4_4;
lbl87:
                // 2 sources

                v8.join();
                ** GOTO lbl5
            }
            case 6: {
                v4 = var3_3;
                var3_3 = var4_4;
lbl93:
                // 2 sources

                v4.join();
                v9 = var0.POSTREQ("Checking payment", var0.api.refreshPayment(), Buffer.buffer((String)"{}"), 200, null, new String[]{"{\"productTotal\":\""});
                if (!v9.isDone()) {
                    var4_4 = v9;
                    return var4_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$placeOrder(io.trickle.task.sites.bestbuy.Bestbuy int java.lang.String java.util.concurrent.CompletableFuture java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (int)var1_1, (String)var2_2, (CompletableFuture)var4_4, (String)var3_3, (int)7));
                }
                ** GOTO lbl103
            }
            case 7: {
                v9 = var3_3;
                var3_3 = var4_4;
lbl103:
                // 2 sources

                v9.join();
                ** GOTO lbl5
            }
            case 8: {
                v5 = var3_3;
                var3_3 = var4_4;
lbl109:
                // 2 sources

                v5.join();
                v10 = var0.POSTREQ("Checking payment", var0.api.refreshPayment(), Buffer.buffer((String)"{}"), 200, null, new String[]{"{\"productTotal\":\""});
                if (!v10.isDone()) {
                    var4_4 = v10;
                    return var4_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$placeOrder(io.trickle.task.sites.bestbuy.Bestbuy int java.lang.String java.util.concurrent.CompletableFuture java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (int)var1_1, (String)var2_2, (CompletableFuture)var4_4, (String)var3_3, (int)9));
                }
                ** GOTO lbl119
            }
            case 9: {
                v10 = var3_3;
                var3_3 = var4_4;
lbl119:
                // 2 sources

                v10.join();
                ** GOTO lbl5
            }
            case 10: {
                v6 = var3_3;
                var3_3 = var4_4;
lbl125:
                // 2 sources

                v6.join();
                ** GOTO lbl5
            }
            case 11: {
                v7 = var3_3;
                var3_3 = var4_4;
lbl131:
                // 2 sources

                v7.join();
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$sendSensor(Bestbuy var0, String var1_1, int var2_2, BestbuyAPI var3_3, CompletableFuture var4_5, String var5_6, Buffer var6_7, HttpRequest var7_8, Throwable var8_9, int var9_10, Object var10_12) {
        switch (var9_10) {
            case 0: {
                var1_1 = var0.api.userAgent;
                var2_2 = 0;
                block9: while (true) {
                    if (!var0.running) {
                        var0.api.userAgent = var1_1;
                        return CompletableFuture.completedFuture(null);
                    }
                    v0 = var0.api;
                    v1 = var0.api.hawkAPI.updateUserAgent();
                    if (!v1.isDone()) {
                        var9_11 = v1;
                        var8_9 = v0;
                        return var9_11.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$sendSensor(io.trickle.task.sites.bestbuy.Bestbuy java.lang.String int io.trickle.task.sites.bestbuy.BestbuyAPI java.util.concurrent.CompletableFuture java.lang.String io.vertx.core.buffer.Buffer io.vertx.ext.web.client.HttpRequest java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (String)var1_1, (int)var2_2, (BestbuyAPI)var8_9, (CompletableFuture)var9_11, null, null, null, null, (int)1));
                    }
lbl15:
                    // 3 sources

                    while (true) {
                        v0.userAgent = (String)v1.join();
                        var0.logger.debug("Solving...");
                        try {
                            v2 = var0.api.hawkAPI.getSensorPayload(var0.api.getCookies().getCookieValue("_abck"));
                            if (!v2.isDone()) {
                                var8_9 = v2;
                                return var8_9.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$sendSensor(io.trickle.task.sites.bestbuy.Bestbuy java.lang.String int io.trickle.task.sites.bestbuy.BestbuyAPI java.util.concurrent.CompletableFuture java.lang.String io.vertx.core.buffer.Buffer io.vertx.ext.web.client.HttpRequest java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (String)var1_1, (int)var2_2, null, (CompletableFuture)var8_9, null, null, null, null, (int)2));
                            }
lbl23:
                            // 3 sources

                            while (true) {
                                var3_3 = (String)v2.join();
                                var4_5 = new JsonObject().put("sensor_data", var3_3).toBuffer();
                                var5_6 = var0.api.sendSensor();
                                v3 = Request.send((HttpRequest)var5_6, (Buffer)var4_5);
                                if (!v3.isDone()) {
                                    var8_9 = v3;
                                    return var8_9.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$sendSensor(io.trickle.task.sites.bestbuy.Bestbuy java.lang.String int io.trickle.task.sites.bestbuy.BestbuyAPI java.util.concurrent.CompletableFuture java.lang.String io.vertx.core.buffer.Buffer io.vertx.ext.web.client.HttpRequest java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (String)var1_1, (int)var2_2, null, (CompletableFuture)var8_9, (String)var3_3, (Buffer)var4_5, (HttpRequest)var5_6, null, (int)3));
                                }
lbl31:
                                // 3 sources

                                while (true) {
                                    var6_7 = (HttpResponse)v3.join();
                                    if (var6_7 == null || !(var7_8 = var6_7.bodyAsString()).contains("false") && var2_2++ < 2) continue block9;
                                    var0.api.userAgent = var1_1;
                                    return CompletableFuture.completedFuture(null);
                                }
                                break;
                            }
                        }
                        catch (Throwable var3_4) {
                            var0.logger.error("Error on sensor. Retrying: {}", (Object)"unexpected response");
                            v4 = VertxUtil.randomSleep(var0.task.getRetryDelay());
                            if (!v4.isDone()) {
                                var8_9 = v4;
                                return var8_9.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$sendSensor(io.trickle.task.sites.bestbuy.Bestbuy java.lang.String int io.trickle.task.sites.bestbuy.BestbuyAPI java.util.concurrent.CompletableFuture java.lang.String io.vertx.core.buffer.Buffer io.vertx.ext.web.client.HttpRequest java.lang.Throwable int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (String)var1_1, (int)var2_2, null, (CompletableFuture)var8_9, null, null, null, (Throwable)var3_4, (int)4));
                            }
lbl42:
                            // 3 sources

                            while (true) {
                                v4.join();
                                continue block9;
                                break;
                            }
                        }
                        break;
                    }
                    break;
                }
            }
            case 1: {
                v0 = var3_3;
                v1 = var4_5;
                ** continue;
            }
            case 2: {
                v2 = var4_5;
                ** continue;
            }
            case 3: {
                v3 = var4_5;
                v5 = var5_6;
                var5_6 = var7_8;
                var4_5 = var6_7;
                var3_3 = v5;
                ** continue;
            }
            case 4: {
                v4 = var4_5;
                var3_3 = var8_9;
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$sendEnforcedReq$1(Bestbuy var0, HttpRequest var1_1, Object var2_2, CompletableFuture var3_3, HttpResponse var4_4, int var5_5, Object var6_6) {
        switch (var5_5) {
            case 0: {
                v0 = Request.send(var1_1, var2_2);
                if (!v0.isDone()) {
                    var4_4 = v0;
                    return var4_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$sendEnforcedReq$1(io.trickle.task.sites.bestbuy.Bestbuy io.vertx.ext.web.client.HttpRequest java.lang.Object java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (HttpRequest)var1_1, (Object)var2_2, (CompletableFuture)var4_4, null, (int)1));
                }
                ** GOTO lbl10
            }
            case 1: {
                v0 = var3_3;
lbl10:
                // 2 sources

                var3_3 = (HttpResponse)v0.join();
lbl11:
                // 2 sources

                while (true) {
                    if (var3_3 != null) {
                        if (var3_3.statusCode() != 403) return CompletableFuture.completedFuture(var3_3);
                    }
                    var0.successCounter = 0;
                    v1 = var0.sendSensor();
                    if (!v1.isDone()) {
                        var4_4 = v1;
                        return var4_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$sendEnforcedReq$1(io.trickle.task.sites.bestbuy.Bestbuy io.vertx.ext.web.client.HttpRequest java.lang.Object java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (HttpRequest)var1_1, (Object)var2_2, (CompletableFuture)var4_4, (HttpResponse)var3_3, (int)2));
                    }
                    ** GOTO lbl23
                    break;
                }
            }
            case 2: {
                v1 = var3_3;
                var3_3 = var4_4;
lbl23:
                // 2 sources

                v1.join();
                v2 = VertxUtil.hardCodedSleep(500L);
                if (!v2.isDone()) {
                    var4_4 = v2;
                    return var4_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$sendEnforcedReq$1(io.trickle.task.sites.bestbuy.Bestbuy io.vertx.ext.web.client.HttpRequest java.lang.Object java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (HttpRequest)var1_1, (Object)var2_2, (CompletableFuture)var4_4, (HttpResponse)var3_3, (int)3));
                }
                ** GOTO lbl33
            }
            case 3: {
                v2 = var3_3;
                var3_3 = var4_4;
lbl33:
                // 2 sources

                v2.join();
                v3 = Request.send(var1_1, var2_2);
                if (!v3.isDone()) {
                    var4_4 = v3;
                    return var4_4.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$sendEnforcedReq$1(io.trickle.task.sites.bestbuy.Bestbuy io.vertx.ext.web.client.HttpRequest java.lang.Object java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (HttpRequest)var1_1, (Object)var2_2, (CompletableFuture)var4_4, (HttpResponse)var3_3, (int)4));
                }
                ** GOTO lbl43
            }
            case 4: {
                v3 = var3_3;
                var3_3 = var4_4;
lbl43:
                // 2 sources

                var3_3 = (HttpResponse)v3.join();
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    public static CompletableFuture async$getLoginVerificationCode(Bestbuy var0, Account var1_1, CompletableFuture var2_2, Message[] var3_3, int var4_4, Object var5_9) {
        switch (var4_4) {
            case 0: {
                if (var0.imapClient == null) {
                    var0.imapClient = MailClient.create(VertxSingleton.INSTANCE.get());
                }
                if (!(v0 = var0.imapClient.connectFut(var1_1.getUser(), var1_1.getPass())).isDone()) {
                    var4_5 = v0;
                    return var4_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$getLoginVerificationCode(io.trickle.task.sites.bestbuy.Bestbuy io.trickle.account.Account java.util.concurrent.CompletableFuture javax.mail.Message[] int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (Account)var1_1, (CompletableFuture)var4_5, null, (int)1));
                }
                ** GOTO lbl11
            }
            case 1: {
                v0 = var2_2;
lbl11:
                // 2 sources

                v0.join();
                v1 = var0.imapClient.readInboxFuture(Login.SEARCH_TERM);
                if (!v1.isDone()) {
                    var4_6 = v1;
                    return var4_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$getLoginVerificationCode(io.trickle.task.sites.bestbuy.Bestbuy io.trickle.account.Account java.util.concurrent.CompletableFuture javax.mail.Message[] int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (Account)var1_1, (CompletableFuture)var4_6, null, (int)2));
                }
                ** GOTO lbl20
            }
            case 2: {
                v1 = var2_2;
lbl20:
                // 2 sources

                var2_2 = (Message[])v1.join();
lbl21:
                // 2 sources

                while (true) {
                    if (var2_2.length != 0) {
                        var3_3 /* !! */  = MessageUtils.getTextFromMessage(var2_2[var2_2.length - 1]);
                        return CompletableFuture.completedFuture(Utils.quickParseFirst((String)var3_3 /* !! */ , new Pattern[]{Bestbuy.VERIFICATION_CODE}));
                    }
                    var0.logger.error("Waiting for email to arrive... [{}]", (Object)var1_1.getUser());
                    v2 = VertxUtil.randomSleep(var0.task.getRetryDelay());
                    if (!v2.isDone()) {
                        var4_7 = v2;
                        return var4_7.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$getLoginVerificationCode(io.trickle.task.sites.bestbuy.Bestbuy io.trickle.account.Account java.util.concurrent.CompletableFuture javax.mail.Message[] int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (Account)var1_1, (CompletableFuture)var4_7, (Message[])var2_2, (int)3));
                    }
                    ** GOTO lbl34
                    break;
                }
            }
            case 3: {
                v2 = var2_2;
                var2_2 = var3_3 /* !! */ ;
lbl34:
                // 2 sources

                v2.join();
                v3 = var0.imapClient.readInboxFuture(Login.SEARCH_TERM);
                if (!v3.isDone()) {
                    var4_8 = v3;
                    return var4_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$getLoginVerificationCode(io.trickle.task.sites.bestbuy.Bestbuy io.trickle.account.Account java.util.concurrent.CompletableFuture javax.mail.Message[] int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (Account)var1_1, (CompletableFuture)var4_8, (Message[])var2_2, (int)4));
                }
                ** GOTO lbl44
            }
            case 4: {
                v3 = var2_2;
                var2_2 = var3_3 /* !! */ ;
lbl44:
                // 2 sources

                var2_2 = (Message[])v3.join();
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture sendSensor() {
        String string = this.api.userAgent;
        int n = 0;
        while (true) {
            if (!this.running) {
                this.api.userAgent = string;
                return CompletableFuture.completedFuture(null);
            }
            CompletableFuture completableFuture = this.api.hawkAPI.updateUserAgent();
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                BestbuyAPI bestbuyAPI = this.api;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$sendSensor(this, string, n, bestbuyAPI, completableFuture2, null, null, null, null, 1, arg_0));
            }
            this.api.userAgent = (String)completableFuture.join();
            this.logger.debug("Solving...");
            try {
                String string2;
                CompletableFuture completableFuture3 = this.api.hawkAPI.getSensorPayload(this.api.getCookies().getCookieValue("_abck"));
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$sendSensor(this, string, n, null, completableFuture4, null, null, null, null, 2, arg_0));
                }
                String string3 = (String)completableFuture3.join();
                Buffer buffer = new JsonObject().put("sensor_data", (Object)string3).toBuffer();
                HttpRequest httpRequest = this.api.sendSensor();
                CompletableFuture completableFuture5 = Request.send(httpRequest, buffer);
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$sendSensor(this, string, n, null, completableFuture6, string3, buffer, httpRequest, null, 3, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture5.join();
                if (httpResponse == null || !(string2 = httpResponse.bodyAsString()).contains("false") && n++ < 2) continue;
                this.api.userAgent = string;
                return CompletableFuture.completedFuture(null);
            }
            catch (Throwable throwable) {
                this.logger.error("Error on sensor. Retrying: {}", (Object)"unexpected response");
                CompletableFuture completableFuture7 = VertxUtil.randomSleep(this.task.getRetryDelay());
                if (!completableFuture7.isDone()) {
                    CompletableFuture completableFuture8 = completableFuture7;
                    return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$sendSensor(this, string, n, null, completableFuture8, null, null, null, throwable, 4, arg_0));
                }
                completableFuture7.join();
                continue;
            }
            break;
        }
    }

    static {
        publicKeyPattern = Pattern.compile("\\\\r\\\\n(.*?)\\\\");
        orderDataPattern = Pattern.compile("var orderData = (.*?);");
        UUID_PATTERN = Pattern.compile("(.{8})(.{4})(.{4})(.{4})(.{12})");
        ORDER_FAIL_REASON = Pattern.compile("\"errors\":\\[\\{\"errorCode\":\"(.*?)\"");
        VERIFICATION_CODE = Pattern.compile("Verification code:.*?([0-9]{6,7})", 32);
    }

    public CompletableFuture login(String string) {
        CompletableFuture completableFuture = this.GETREQ("Fetching values", this.api.loginPage(string), 200, "identity");
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$login(this, string, completableFuture2, null, null, null, null, null, null, null, null, null, 1, arg_0));
        }
        String string2 = (String)completableFuture.join();
        string2 = string2.replace("</html>", "<script>document.querySelector(\"html\").innerHTML = `<h2>Waiting for completion</h2>`</script>\n</html>");
        CompletableFuture completableFuture3 = LoginController.initBrowserLogin(string, this.api.getCookies().get(true, ".bestbuy.com", "/"), this.api.proxyString(), this.api.getCookies(), string2);
        this.api.login = Login.loginValues(string2);
        CompletableFuture completableFuture4 = this.GETREQ("Fetching key (1/2)", this.api.ciaUserActivity(), 200, "keyId");
        if (!completableFuture4.isDone()) {
            CompletableFuture completableFuture5 = completableFuture4;
            return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$login(this, string, completableFuture3, string2, completableFuture5, null, null, null, null, null, null, null, 2, arg_0));
        }
        String string3 = (String)completableFuture4.join();
        CompletableFuture completableFuture6 = this.GETREQ("Fetching key (2/2)", this.api.emailGrid(), 200, "keyId");
        if (!completableFuture6.isDone()) {
            CompletableFuture completableFuture7 = completableFuture6;
            return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$login(this, string, completableFuture3, string2, completableFuture7, string3, null, null, null, null, null, null, 3, arg_0));
        }
        String string4 = (String)completableFuture6.join();
        AccountController accountController = (AccountController)Engine.get().getModule(Controller.ACCOUNT);
        CompletableFuture completableFuture8 = accountController.findAccount(this.task.getProfile().getEmail(), true).toCompletionStage().toCompletableFuture();
        if (!completableFuture8.isDone()) {
            CompletableFuture completableFuture9 = completableFuture8;
            return ((CompletableFuture)completableFuture9.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$login(this, string, completableFuture3, string2, completableFuture9, string3, string4, accountController, null, null, null, null, 4, arg_0));
        }
        Account account = (Account)completableFuture8.join();
        CompletableFuture completableFuture10 = completableFuture3;
        if (!completableFuture10.isDone()) {
            CompletableFuture completableFuture11 = completableFuture10;
            return ((CompletableFuture)completableFuture11.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$login(this, string, completableFuture3, string2, completableFuture11, string3, string4, accountController, account, null, null, null, 5, arg_0));
        }
        completableFuture10.join();
        CompletableFuture completableFuture12 = this.POSTREQ("Logging in...", this.api.login(), this.api.accountLoginForm(account, new JsonObject(string3), new JsonObject(string4)), 200, null, "status", "{");
        if (!completableFuture12.isDone()) {
            CompletableFuture completableFuture13 = completableFuture12;
            return ((CompletableFuture)completableFuture13.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$login(this, string, completableFuture3, string2, completableFuture13, string3, string4, accountController, account, null, null, null, 6, arg_0));
        }
        String string5 = (String)completableFuture12.join();
        if (string5.contains("success")) {
            return CompletableFuture.completedFuture(null);
        }
        if (!string5.contains("stepUpRequired")) return CompletableFuture.completedFuture(null);
        JsonObject jsonObject = new JsonObject(string5);
        this.api.login.flowOptions = jsonObject.getString("flowOptions");
        this.api.login.challengeType = jsonObject.getString("challengeType");
        if (this.api.login.challengeType.equals("2")) {
            this.logger.error("Account requires password reset");
            CompletableFuture completableFuture14 = this.POSTREQ("Selecting verification...", this.api.pickVerification(), this.api.pickVerificationJson(), 200, null, "success");
            if (!completableFuture14.isDone()) {
                CompletableFuture completableFuture15 = completableFuture14;
                return ((CompletableFuture)completableFuture15.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$login(this, string, completableFuture3, string2, completableFuture15, string3, string4, accountController, account, string5, jsonObject, null, 7, arg_0));
            }
            completableFuture14.join();
        }
        CompletableFuture completableFuture16 = this.getLoginVerificationCode(account);
        if (!completableFuture16.isDone()) {
            CompletableFuture completableFuture17 = completableFuture16;
            return ((CompletableFuture)completableFuture17.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$login(this, string, completableFuture3, string2, completableFuture17, string3, string4, accountController, account, string5, jsonObject, null, 8, arg_0));
        }
        String string6 = (String)completableFuture16.join();
        CompletableFuture completableFuture18 = this.POSTREQ("Verifying Account...", this.api.verificationCode(), this.api.verificationJson(string6), 200, null, "status");
        if (!completableFuture18.isDone()) {
            CompletableFuture completableFuture19 = completableFuture18;
            return ((CompletableFuture)completableFuture19.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$login(this, string, completableFuture3, string2, completableFuture19, string3, string4, accountController, account, string5, jsonObject, string6, 9, arg_0));
        }
        completableFuture18.join();
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture run() {
        CompletableFuture completableFuture = this.initLoginHarvesters();
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$run(this, completableFuture2, null, null, null, null, null, 1, arg_0));
        }
        completableFuture.join();
        try {
            CompletableFuture completableFuture3 = this.api.hawkAPI.updateUserAgent();
            if (!completableFuture3.isDone()) {
                CompletableFuture completableFuture4 = completableFuture3;
                BestbuyAPI bestbuyAPI = this.api;
                return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$run(this, completableFuture4, bestbuyAPI, null, null, null, null, 2, arg_0));
            }
            this.api.userAgent = (String)completableFuture3.join();
            CompletableFuture completableFuture5 = this.encryptCard();
            if (!completableFuture5.isDone()) {
                CompletableFuture completableFuture6 = completableFuture5;
                return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$run(this, completableFuture6, null, null, null, null, null, 3, arg_0));
            }
            completableFuture5.join();
            CompletableFuture completableFuture7 = this.GETREQ("Visiting product", this.api.productPage(), 200, "script type=\"text/javascript");
            if (!completableFuture7.isDone()) {
                CompletableFuture completableFuture8 = completableFuture7;
                return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$run(this, completableFuture8, null, null, null, null, null, 4, arg_0));
            }
            String string = (String)completableFuture7.join();
            this.updateSensorUrlFromHTML(string);
            CompletableFuture completableFuture9 = this.POSTREQ("Getting store-ids", this.api.getStoreId(), this.api.storeIdForm(), 200, null, "locations");
            if (!completableFuture9.isDone()) {
                CompletableFuture completableFuture10 = completableFuture9;
                return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$run(this, completableFuture10, null, string, null, null, null, 5, arg_0));
            }
            String string2 = (String)completableFuture9.join();
            this.api.store = new JsonObject(string2).getJsonObject("ispu").getJsonArray("locations").getJsonObject(0).getString("id");
            if (this.preload) {
                CompletableFuture completableFuture11 = this.atc(true);
                if (!completableFuture11.isDone()) {
                    CompletableFuture completableFuture12 = completableFuture11;
                    return ((CompletableFuture)completableFuture12.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$run(this, completableFuture12, null, string, string2, null, null, 6, arg_0));
                }
                completableFuture11.join();
                CompletableFuture completableFuture13 = this.getLineId();
                if (!completableFuture13.isDone()) {
                    CompletableFuture completableFuture14 = completableFuture13;
                    return ((CompletableFuture)completableFuture14.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$run(this, completableFuture14, null, string, string2, null, null, 7, arg_0));
                }
                completableFuture13.join();
                CompletableFuture completableFuture15 = this.POSTREQ("Submitting payment", this.api.vaultCC(), this.api.vaultJson(), 200, null, "{\"paymentId\":\"");
                if (!completableFuture15.isDone()) {
                    CompletableFuture completableFuture16 = completableFuture15;
                    return ((CompletableFuture)completableFuture16.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$run(this, completableFuture16, null, string, string2, null, null, 8, arg_0));
                }
                completableFuture15.join();
                CompletableFuture completableFuture17 = this.POSTREQ("Checking payment", this.api.refreshPayment(), Buffer.buffer((String)"{}"), 200, null, "{\"productTotal\":\"");
                if (!completableFuture17.isDone()) {
                    CompletableFuture completableFuture18 = completableFuture17;
                    return ((CompletableFuture)completableFuture18.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$run(this, completableFuture18, null, string, string2, null, null, 9, arg_0));
                }
                completableFuture17.join();
                CompletableFuture completableFuture19 = this.clearCart();
                if (!completableFuture19.isDone()) {
                    CompletableFuture completableFuture20 = completableFuture19;
                    return ((CompletableFuture)completableFuture20.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$run(this, completableFuture20, null, string, string2, null, null, 10, arg_0));
                }
                completableFuture19.join();
            }
            CompletableFuture completableFuture21 = this.atc(false);
            if (!completableFuture21.isDone()) {
                CompletableFuture completableFuture22 = completableFuture21;
                return ((CompletableFuture)completableFuture22.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$run(this, completableFuture22, null, string, string2, null, null, 11, arg_0));
            }
            String string3 = (String)completableFuture21.join();
            if (string3.contains("/identity")) {
                this.api.getCookies().removeAnyMatch("bby_rdp");
                this.api.getCookies().put("bby_rdp", "l", ".bestbuy.com");
                this.logger.info(string3);
                if (this.browser) {
                    CompletableFuture completableFuture23 = this.login(string3);
                    if (!completableFuture23.isDone()) {
                        CompletableFuture completableFuture24 = completableFuture23;
                        return ((CompletableFuture)completableFuture24.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$run(this, completableFuture24, null, string, string2, string3, null, 12, arg_0));
                    }
                    completableFuture23.join();
                }
                this.api.getCookies().removeAnyMatch("bby_rdp");
                this.api.getCookies().put("bby_rdp", "s", ".bestbuy.com");
                CompletableFuture completableFuture25 = this.clearCart();
                if (!completableFuture25.isDone()) {
                    CompletableFuture completableFuture26 = completableFuture25;
                    return ((CompletableFuture)completableFuture26.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$run(this, completableFuture26, null, string, string2, string3, null, 13, arg_0));
                }
                completableFuture25.join();
                this.logger.info("Attempting re-cart...");
                CompletableFuture completableFuture27 = this.atc(false);
                if (!completableFuture27.isDone()) {
                    CompletableFuture completableFuture28 = completableFuture27;
                    return ((CompletableFuture)completableFuture28.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$run(this, completableFuture28, null, string, string2, string3, null, 14, arg_0));
                }
                completableFuture27.join();
            }
            while (true) {
                try {
                    CompletableFuture completableFuture29 = this.getLineId();
                    if (!completableFuture29.isDone()) {
                        CompletableFuture completableFuture30 = completableFuture29;
                        return ((CompletableFuture)completableFuture30.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$run(this, completableFuture30, null, string, string2, string3, null, 15, arg_0));
                    }
                    completableFuture29.join();
                    CompletableFuture completableFuture31 = this.POSTREQ("Submitting contact", this.api.submitContact(), this.api.contactJson(), 200, null, "\"quantity\":1,");
                    if (!completableFuture31.isDone()) {
                        CompletableFuture completableFuture32 = completableFuture31;
                        return ((CompletableFuture)completableFuture32.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$run(this, completableFuture32, null, string, string2, string3, null, 16, arg_0));
                    }
                    completableFuture31.join();
                    CompletableFuture completableFuture33 = this.POSTREQ("Submitting email", this.api.submitEmail(), this.api.emailJson(), 200, null, "\"quantity\":1,");
                    CompletableFuture completableFuture34 = this.POSTREQ("Submitting payment", this.api.vaultCC(), this.api.vaultJson(), 200, null, "{\"paymentId\":\"");
                    if (!completableFuture34.isDone()) {
                        CompletableFuture completableFuture35 = completableFuture34;
                        return ((CompletableFuture)completableFuture35.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$run(this, completableFuture33, null, string, string2, string3, completableFuture35, 17, arg_0));
                    }
                    completableFuture34.join();
                    CompletableFuture completableFuture36 = completableFuture33;
                    if (!completableFuture36.isDone()) {
                        CompletableFuture completableFuture37 = completableFuture36;
                        return ((CompletableFuture)completableFuture37.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$run(this, completableFuture33, null, string, string2, string3, completableFuture37, 18, arg_0));
                    }
                    completableFuture36.join();
                    CompletableFuture completableFuture38 = this.POSTREQ("Checking payment", this.api.refreshPayment(), Buffer.buffer((String)"{}"), 200, null, "{\"productTotal\":\"");
                    if (!completableFuture38.isDone()) {
                        CompletableFuture completableFuture39 = completableFuture38;
                        return ((CompletableFuture)completableFuture39.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$run(this, completableFuture33, null, string, string2, string3, completableFuture39, 19, arg_0));
                    }
                    completableFuture38.join();
                    CompletableFuture completableFuture40 = this.placeOrder();
                    if (!completableFuture40.isDone()) {
                        CompletableFuture completableFuture41 = completableFuture40;
                        return ((CompletableFuture)completableFuture41.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$run(this, completableFuture33, null, string, string2, string3, completableFuture41, 20, arg_0));
                    }
                    String string4 = (String)completableFuture40.join();
                    Analytics.success(this.task, new JsonObject(string4), this.api.proxyString());
                    this.logger.info("Successfully checked out!");
                    return CompletableFuture.completedFuture(null);
                }
                catch (Throwable throwable) {
                    this.logger.error("Caught checkout exception {}", (Object)throwable.getMessage());
                    continue;
                }
                break;
            }
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
            this.logger.error("Caught exception {}", (Object)throwable.getMessage());
            return CompletableFuture.completedFuture(null);
        }
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$run(Bestbuy var0, CompletableFuture var1_1, BestbuyAPI var2_3, String var3_4, String var4_5, String var5_7, CompletableFuture var6_8, int var7_9, Object var8_11) {
        switch (var7_9) {
            case 0: {
                v0 = var0.initLoginHarvesters();
                if (!v0.isDone()) {
                    var6_8 = v0;
                    return var6_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.bestbuy.Bestbuy java.util.concurrent.CompletableFuture io.trickle.task.sites.bestbuy.BestbuyAPI java.lang.String java.lang.String java.lang.String java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (CompletableFuture)var6_8, null, null, null, null, null, (int)1));
                }
lbl7:
                // 3 sources

                while (true) {
                    v0.join();
                    try {
                        v1 = var0.api;
                        v2 = var0.api.hawkAPI.updateUserAgent();
                        if (!v2.isDone()) {
                            var7_10 = v2;
                            var6_8 = v1;
                            return var7_10.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.bestbuy.Bestbuy java.util.concurrent.CompletableFuture io.trickle.task.sites.bestbuy.BestbuyAPI java.lang.String java.lang.String java.lang.String java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (CompletableFuture)var7_10, (BestbuyAPI)var6_8, null, null, null, null, (int)2));
                        }
lbl17:
                        // 3 sources

                        while (true) {
                            v1.userAgent = (String)v2.join();
                            v3 = var0.encryptCard();
                            if (!v3.isDone()) {
                                var6_8 = v3;
                                return var6_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.bestbuy.Bestbuy java.util.concurrent.CompletableFuture io.trickle.task.sites.bestbuy.BestbuyAPI java.lang.String java.lang.String java.lang.String java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (CompletableFuture)var6_8, null, null, null, null, null, (int)3));
                            }
lbl23:
                            // 3 sources

                            while (true) {
                                v3.join();
                                v4 = var0.GETREQ("Visiting product", var0.api.productPage(), 200, new String[]{"script type=\"text/javascript"});
                                if (!v4.isDone()) {
                                    var6_8 = v4;
                                    return var6_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.bestbuy.Bestbuy java.util.concurrent.CompletableFuture io.trickle.task.sites.bestbuy.BestbuyAPI java.lang.String java.lang.String java.lang.String java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (CompletableFuture)var6_8, null, null, null, null, null, (int)4));
                                }
lbl30:
                                // 3 sources

                                while (true) {
                                    var1_1 = (String)v4.join();
                                    var0.updateSensorUrlFromHTML((String)var1_1);
                                    v5 = var0.POSTREQ("Getting store-ids", var0.api.getStoreId(), var0.api.storeIdForm(), 200, null, new String[]{"locations"});
                                    if (!v5.isDone()) {
                                        var6_8 = v5;
                                        return var6_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.bestbuy.Bestbuy java.util.concurrent.CompletableFuture io.trickle.task.sites.bestbuy.BestbuyAPI java.lang.String java.lang.String java.lang.String java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (CompletableFuture)var6_8, null, (String)var1_1, null, null, null, (int)5));
                                    }
lbl37:
                                    // 3 sources

                                    while (true) {
                                        var2_3 = (String)v5.join();
                                        var0.api.store = new JsonObject((String)var2_3).getJsonObject("ispu").getJsonArray("locations").getJsonObject(0).getString("id");
                                        if (!var0.preload) ** GOTO lbl76
                                        v6 = var0.atc(true);
                                        if (!v6.isDone()) {
                                            var6_8 = v6;
                                            return var6_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.bestbuy.Bestbuy java.util.concurrent.CompletableFuture io.trickle.task.sites.bestbuy.BestbuyAPI java.lang.String java.lang.String java.lang.String java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (CompletableFuture)var6_8, null, (String)var1_1, (String)var2_3, null, null, (int)6));
                                        }
lbl45:
                                        // 3 sources

                                        while (true) {
                                            v6.join();
                                            v7 = var0.getLineId();
                                            if (!v7.isDone()) {
                                                var6_8 = v7;
                                                return var6_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.bestbuy.Bestbuy java.util.concurrent.CompletableFuture io.trickle.task.sites.bestbuy.BestbuyAPI java.lang.String java.lang.String java.lang.String java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (CompletableFuture)var6_8, null, (String)var1_1, (String)var2_3, null, null, (int)7));
                                            }
lbl52:
                                            // 3 sources

                                            while (true) {
                                                v7.join();
                                                v8 = var0.POSTREQ("Submitting payment", var0.api.vaultCC(), var0.api.vaultJson(), 200, null, new String[]{"{\"paymentId\":\""});
                                                if (!v8.isDone()) {
                                                    var6_8 = v8;
                                                    return var6_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.bestbuy.Bestbuy java.util.concurrent.CompletableFuture io.trickle.task.sites.bestbuy.BestbuyAPI java.lang.String java.lang.String java.lang.String java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (CompletableFuture)var6_8, null, (String)var1_1, (String)var2_3, null, null, (int)8));
                                                }
lbl59:
                                                // 3 sources

                                                while (true) {
                                                    v8.join();
                                                    v9 = var0.POSTREQ("Checking payment", var0.api.refreshPayment(), Buffer.buffer((String)"{}"), 200, null, new String[]{"{\"productTotal\":\""});
                                                    if (!v9.isDone()) {
                                                        var6_8 = v9;
                                                        return var6_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.bestbuy.Bestbuy java.util.concurrent.CompletableFuture io.trickle.task.sites.bestbuy.BestbuyAPI java.lang.String java.lang.String java.lang.String java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (CompletableFuture)var6_8, null, (String)var1_1, (String)var2_3, null, null, (int)9));
                                                    }
lbl66:
                                                    // 3 sources

                                                    while (true) {
                                                        v9.join();
                                                        v10 = var0.clearCart();
                                                        if (!v10.isDone()) {
                                                            var6_8 = v10;
                                                            return var6_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.bestbuy.Bestbuy java.util.concurrent.CompletableFuture io.trickle.task.sites.bestbuy.BestbuyAPI java.lang.String java.lang.String java.lang.String java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (CompletableFuture)var6_8, null, (String)var1_1, (String)var2_3, null, null, (int)10));
                                                        }
lbl73:
                                                        // 3 sources

                                                        while (true) {
                                                            v10.join();
lbl76:
                                                            // 2 sources

                                                            if (!(v11 = var0.atc(false)).isDone()) {
                                                                var6_8 = v11;
                                                                return var6_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.bestbuy.Bestbuy java.util.concurrent.CompletableFuture io.trickle.task.sites.bestbuy.BestbuyAPI java.lang.String java.lang.String java.lang.String java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (CompletableFuture)var6_8, null, (String)var1_1, (String)var2_3, null, null, (int)11));
                                                            }
lbl79:
                                                            // 3 sources

                                                            while (true) {
                                                                var3_4 = (String)v11.join();
                                                                if (!var3_4.contains("/identity")) ** GOTO lbl112
                                                                var0.api.getCookies().removeAnyMatch("bby_rdp");
                                                                var0.api.getCookies().put("bby_rdp", "l", ".bestbuy.com");
                                                                var0.logger.info(var3_4);
                                                                if (!var0.browser) ** GOTO lbl94
                                                                v12 = var0.login(var3_4);
                                                                if (!v12.isDone()) {
                                                                    var6_8 = v12;
                                                                    return var6_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.bestbuy.Bestbuy java.util.concurrent.CompletableFuture io.trickle.task.sites.bestbuy.BestbuyAPI java.lang.String java.lang.String java.lang.String java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (CompletableFuture)var6_8, null, (String)var1_1, (String)var2_3, (String)var3_4, null, (int)12));
                                                                }
lbl91:
                                                                // 3 sources

                                                                while (true) {
                                                                    v12.join();
lbl94:
                                                                    // 2 sources

                                                                    var0.api.getCookies().removeAnyMatch("bby_rdp");
                                                                    var0.api.getCookies().put("bby_rdp", "s", ".bestbuy.com");
                                                                    v13 = var0.clearCart();
                                                                    if (!v13.isDone()) {
                                                                        var6_8 = v13;
                                                                        return var6_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.bestbuy.Bestbuy java.util.concurrent.CompletableFuture io.trickle.task.sites.bestbuy.BestbuyAPI java.lang.String java.lang.String java.lang.String java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (CompletableFuture)var6_8, null, (String)var1_1, (String)var2_3, (String)var3_4, null, (int)13));
                                                                    }
lbl101:
                                                                    // 3 sources

                                                                    while (true) {
                                                                        v13.join();
                                                                        var0.logger.info("Attempting re-cart...");
                                                                        v14 = var0.atc(false);
                                                                        if (!v14.isDone()) {
                                                                            var6_8 = v14;
                                                                            return var6_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.bestbuy.Bestbuy java.util.concurrent.CompletableFuture io.trickle.task.sites.bestbuy.BestbuyAPI java.lang.String java.lang.String java.lang.String java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (CompletableFuture)var6_8, null, (String)var1_1, (String)var2_3, (String)var3_4, null, (int)14));
                                                                        }
lbl109:
                                                                        // 3 sources

                                                                        while (true) {
                                                                            v14.join();
lbl112:
                                                                            // 2 sources

                                                                            while (true) {
                                                                                try {
                                                                                    v15 = var0.getLineId();
                                                                                    if (!v15.isDone()) {
                                                                                        var6_8 = v15;
                                                                                        return var6_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.bestbuy.Bestbuy java.util.concurrent.CompletableFuture io.trickle.task.sites.bestbuy.BestbuyAPI java.lang.String java.lang.String java.lang.String java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (CompletableFuture)var6_8, null, (String)var1_1, (String)var2_3, (String)var3_4, null, (int)15));
                                                                                    }
lbl118:
                                                                                    // 3 sources

                                                                                    while (true) {
                                                                                        v15.join();
                                                                                        v16 = var0.POSTREQ("Submitting contact", var0.api.submitContact(), var0.api.contactJson(), 200, null, new String[]{"\"quantity\":1,"});
                                                                                        if (!v16.isDone()) {
                                                                                            var6_8 = v16;
                                                                                            return var6_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.bestbuy.Bestbuy java.util.concurrent.CompletableFuture io.trickle.task.sites.bestbuy.BestbuyAPI java.lang.String java.lang.String java.lang.String java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (CompletableFuture)var6_8, null, (String)var1_1, (String)var2_3, (String)var3_4, null, (int)16));
                                                                                        }
lbl125:
                                                                                        // 3 sources

                                                                                        while (true) {
                                                                                            v16.join();
                                                                                            var4_5 = var0.POSTREQ("Submitting email", var0.api.submitEmail(), var0.api.emailJson(), 200, null, new String[]{"\"quantity\":1,"});
                                                                                            v17 = var0.POSTREQ("Submitting payment", var0.api.vaultCC(), var0.api.vaultJson(), 200, null, new String[]{"{\"paymentId\":\""});
                                                                                            if (!v17.isDone()) {
                                                                                                var6_8 = v17;
                                                                                                return var6_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.bestbuy.Bestbuy java.util.concurrent.CompletableFuture io.trickle.task.sites.bestbuy.BestbuyAPI java.lang.String java.lang.String java.lang.String java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (CompletableFuture)var4_5, null, (String)var1_1, (String)var2_3, (String)var3_4, (CompletableFuture)var6_8, (int)17));
                                                                                            }
lbl133:
                                                                                            // 3 sources

                                                                                            while (true) {
                                                                                                v17.join();
                                                                                                v18 = var4_5;
                                                                                                if (!v18.isDone()) {
                                                                                                    var6_8 = v18;
                                                                                                    return var6_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.bestbuy.Bestbuy java.util.concurrent.CompletableFuture io.trickle.task.sites.bestbuy.BestbuyAPI java.lang.String java.lang.String java.lang.String java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (CompletableFuture)var4_5, null, (String)var1_1, (String)var2_3, (String)var3_4, (CompletableFuture)var6_8, (int)18));
                                                                                                }
lbl140:
                                                                                                // 3 sources

                                                                                                while (true) {
                                                                                                    v18.join();
                                                                                                    v19 = var0.POSTREQ("Checking payment", var0.api.refreshPayment(), Buffer.buffer((String)"{}"), 200, null, new String[]{"{\"productTotal\":\""});
                                                                                                    if (!v19.isDone()) {
                                                                                                        var6_8 = v19;
                                                                                                        return var6_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.bestbuy.Bestbuy java.util.concurrent.CompletableFuture io.trickle.task.sites.bestbuy.BestbuyAPI java.lang.String java.lang.String java.lang.String java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (CompletableFuture)var4_5, null, (String)var1_1, (String)var2_3, (String)var3_4, (CompletableFuture)var6_8, (int)19));
                                                                                                    }
lbl147:
                                                                                                    // 3 sources

                                                                                                    while (true) {
                                                                                                        v19.join();
                                                                                                        v20 = var0.placeOrder();
                                                                                                        if (!v20.isDone()) {
                                                                                                            var6_8 = v20;
                                                                                                            return var6_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$run(io.trickle.task.sites.bestbuy.Bestbuy java.util.concurrent.CompletableFuture io.trickle.task.sites.bestbuy.BestbuyAPI java.lang.String java.lang.String java.lang.String java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (CompletableFuture)var4_5, null, (String)var1_1, (String)var2_3, (String)var3_4, (CompletableFuture)var6_8, (int)20));
                                                                                                        }
                                                                                                        ** GOTO lbl279
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
                                                                                catch (Throwable var4_6) {
                                                                                    var0.logger.error("Caught checkout exception {}", (Object)var4_6.getMessage());
                                                                                    continue;
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
                                    break;
                                }
                                break;
                            }
                            break;
                        }
                    }
                    catch (Throwable var1_2) {
                        var1_2.printStackTrace();
                        var0.logger.error("Caught exception {}", (Object)var1_2.getMessage());
                        return CompletableFuture.completedFuture(null);
                    }
                    break;
                }
            }
            case 1: {
                v0 = var1_1;
                ** continue;
            }
            case 2: {
                v1 = var2_3;
                v2 = var1_1;
                ** continue;
            }
            case 3: {
                v3 = var1_1;
                ** continue;
            }
            case 4: {
                v4 = var1_1;
                ** continue;
            }
            case 5: {
                v5 = var1_1;
                var1_1 = var3_4;
                ** continue;
            }
            case 6: {
                v6 = var1_1;
                var2_3 = var4_5;
                var1_1 = var3_4;
                ** continue;
            }
            case 7: {
                v7 = var1_1;
                var2_3 = var4_5;
                var1_1 = var3_4;
                ** continue;
            }
            case 8: {
                v8 = var1_1;
                var2_3 = var4_5;
                var1_1 = var3_4;
                ** continue;
            }
            case 9: {
                v9 = var1_1;
                var2_3 = var4_5;
                var1_1 = var3_4;
                ** continue;
            }
            case 10: {
                v10 = var1_1;
                var2_3 = var4_5;
                var1_1 = var3_4;
                ** continue;
            }
            case 11: {
                v11 = var1_1;
                var2_3 = var4_5;
                var1_1 = var3_4;
                ** continue;
            }
            case 12: {
                v12 = var1_1;
                v21 = var3_4;
                var3_4 = var5_7;
                var2_3 = var4_5;
                var1_1 = v21;
                ** continue;
            }
            case 13: {
                v13 = var1_1;
                v22 = var3_4;
                var3_4 = var5_7;
                var2_3 = var4_5;
                var1_1 = v22;
                ** continue;
            }
            case 14: {
                v14 = var1_1;
                v23 = var3_4;
                var3_4 = var5_7;
                var2_3 = var4_5;
                var1_1 = v23;
                ** continue;
            }
            case 15: {
                v15 = var1_1;
                v24 = var3_4;
                var3_4 = var5_7;
                var2_3 = var4_5;
                var1_1 = v24;
                ** continue;
            }
            case 16: {
                v16 = var1_1;
                v25 = var3_4;
                var3_4 = var5_7;
                var2_3 = var4_5;
                var1_1 = v25;
                ** continue;
            }
            case 17: {
                v17 = var6_8;
                v26 = var3_4;
                v27 = var4_5;
                var4_5 = var1_1;
                var3_4 = var5_7;
                var2_3 = v27;
                var1_1 = v26;
                ** continue;
            }
            case 18: {
                v18 = var6_8;
                v28 = var3_4;
                v29 = var4_5;
                var4_5 = var1_1;
                var3_4 = var5_7;
                var2_3 = v29;
                var1_1 = v28;
                ** continue;
            }
            case 19: {
                v19 = var6_8;
                v30 = var3_4;
                v31 = var4_5;
                var4_5 = var1_1;
                var3_4 = var5_7;
                var2_3 = v31;
                var1_1 = v30;
                ** continue;
            }
            case 20: {
                v20 = var6_8;
                v32 = var3_4;
                v33 = var4_5;
                var4_5 = var1_1;
                var3_4 = var5_7;
                var2_3 = v33;
                var1_1 = v32;
lbl279:
                // 2 sources

                var5_7 = (String)v20.join();
                Analytics.success(var0.task, new JsonObject(var5_7), var0.api.proxyString());
                var0.logger.info("Successfully checked out!");
                return CompletableFuture.completedFuture(null);
            }
        }
        throw new IllegalArgumentException();
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$freshenSession(Bestbuy var0, String var1_1, String var2_2, CompletableFuture var3_3, int var4_4, Object var5_6) {
        switch (var4_4) {
            case 0: {
                var0.a2ctransactioncode = null;
                var0.a2ctransactionreferenceid = null;
                var1_1 = var0.api.getCookies().getCookieValue("bm_sz");
                var2_2 = var0.api.getCookies().getCookieValue("_abck");
                var0.api.getCookies().clear();
                var0.api.getCookies().put("_abck", var2_2, ".bestbuy.com");
                v0 = var0.GETREQ("Refreshing session", var0.api.productPage(), 200, new String[]{"script type=\"text/javascript"});
                if (!v0.isDone()) {
                    var4_5 = v0;
                    return var4_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$freshenSession(io.trickle.task.sites.bestbuy.Bestbuy java.lang.String java.lang.String java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (String)var1_1, (String)var2_2, (CompletableFuture)var4_5, (int)1));
                }
                ** GOTO lbl18
            }
            case 1: {
                v0 = var3_3;
lbl18:
                // 2 sources

                var3_3 = (String)v0.join();
                var0.updateSensorUrlFromHTML((String)var3_3);
                return CompletableFuture.completedFuture(null);
            }
        }
        throw new IllegalArgumentException();
    }

    public CompletableFuture clearCart() {
        CompletableFuture completableFuture = this.GETREQ("Checking cart items", this.api.getCartItems(), 200, "cartV2");
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$clearCart(this, completableFuture2, null, null, 0, null, 1, arg_0));
        }
        Object object = (String)completableFuture.join();
        JsonObject jsonObject = new JsonObject((String)object).getJsonObject("cart");
        this.api.orderId = jsonObject.getString("id");
        JsonArray jsonArray = jsonObject.getJsonArray("lineItems");
        int n = 0;
        while (jsonArray != null) {
            if (n >= jsonArray.size()) return CompletableFuture.completedFuture(null);
            String string = jsonArray.getJsonObject(n).getString("id");
            CompletableFuture completableFuture3 = this.GETREQ("Deleting item " + n, this.api.deleteItem(string), 200, "order");
            if (!completableFuture3.isDone()) {
                object = completableFuture3;
                return ((CompletableFuture)((CompletableFuture)object).exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$clearCart(this, (CompletableFuture)object, jsonObject, jsonArray, n, string, 2, arg_0));
            }
            completableFuture3.join();
            ++n;
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$sendEnforcedReq(Bestbuy var0, HttpRequest var1_1, CompletableFuture var2_2, HttpResponse var3_3, int var4_4, Object var5_5) {
        switch (var4_4) {
            case 0: {
                v0 = Request.send(var1_1);
                if (!v0.isDone()) {
                    var3_3 = v0;
                    return var3_3.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$sendEnforcedReq(io.trickle.task.sites.bestbuy.Bestbuy io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (HttpRequest)var1_1, (CompletableFuture)var3_3, null, (int)1));
                }
                ** GOTO lbl10
            }
            case 1: {
                v0 = var2_2;
lbl10:
                // 2 sources

                var2_2 = (HttpResponse)v0.join();
lbl11:
                // 2 sources

                while (true) {
                    if (var2_2 != null) {
                        if (var2_2.statusCode() != 403) return CompletableFuture.completedFuture(var2_2);
                    }
                    var0.successCounter = 0;
                    v1 = var0.sendSensor();
                    if (!v1.isDone()) {
                        var3_3 = v1;
                        return var3_3.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$sendEnforcedReq(io.trickle.task.sites.bestbuy.Bestbuy io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (HttpRequest)var1_1, (CompletableFuture)var3_3, (HttpResponse)var2_2, (int)2));
                    }
                    ** GOTO lbl23
                    break;
                }
            }
            case 2: {
                v1 = var2_2;
                var2_2 = var3_3;
lbl23:
                // 2 sources

                v1.join();
                v2 = VertxUtil.hardCodedSleep(500L);
                if (!v2.isDone()) {
                    var3_3 = v2;
                    return var3_3.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$sendEnforcedReq(io.trickle.task.sites.bestbuy.Bestbuy io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (HttpRequest)var1_1, (CompletableFuture)var3_3, (HttpResponse)var2_2, (int)3));
                }
                ** GOTO lbl33
            }
            case 3: {
                v2 = var2_2;
                var2_2 = var3_3;
lbl33:
                // 2 sources

                v2.join();
                v3 = Request.send(var1_1);
                if (!v3.isDone()) {
                    var3_3 = v3;
                    return var3_3.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$sendEnforcedReq(io.trickle.task.sites.bestbuy.Bestbuy io.vertx.ext.web.client.HttpRequest java.util.concurrent.CompletableFuture io.vertx.ext.web.client.HttpResponse int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((Bestbuy)var0, (HttpRequest)var1_1, (CompletableFuture)var3_3, (HttpResponse)var2_2, (int)4));
                }
                ** GOTO lbl43
            }
            case 4: {
                v3 = var2_2;
                var2_2 = var3_3;
lbl43:
                // 2 sources

                var2_2 = (HttpResponse)v3.join();
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public CompletableFuture GETREQ(String string, HttpRequest httpRequest, Integer n, String ... stringArray) {
        this.logger.info(string);
        while (this.running) {
            try {
                HttpResponse httpResponse;
                HttpResponse httpResponse2;
                int n2;
                int n3 = n2 = stringArray == null || n != null && n == 302 ? 1 : 0;
                if (n2 != 0) {
                    CompletableFuture completableFuture = this.sendEnforcedReq(httpRequest.as(BodyCodec.none()));
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture2 = completableFuture;
                        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$GETREQ(this, string, httpRequest, n, stringArray, n2, completableFuture2, null, null, 1, arg_0));
                    }
                    httpResponse2 = (HttpResponse)completableFuture.join();
                } else {
                    CompletableFuture completableFuture = this.sendEnforcedReq(httpRequest);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture3 = completableFuture;
                        return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$GETREQ(this, string, httpRequest, n, stringArray, n2, completableFuture3, null, null, 2, arg_0));
                    }
                    httpResponse2 = httpResponse = (HttpResponse)completableFuture.join();
                }
                if (httpResponse != null) {
                    boolean bl;
                    if (httpResponse.statusCode() == 302) {
                        bl = stringArray == null || httpResponse.getHeader("location").contains(stringArray[0]);
                    } else {
                        boolean bl2 = bl = stringArray == null || Utils.containsAllWords(httpResponse.bodyAsString(), stringArray);
                    }
                    if ((n == null || httpResponse.statusCode() == n.intValue()) && bl) {
                        String string2;
                        if (n2 != 0) {
                            string2 = httpResponse.getHeader("location");
                            return CompletableFuture.completedFuture(string2);
                        }
                        string2 = httpResponse.bodyAsString();
                        return CompletableFuture.completedFuture(string2);
                    }
                    this.logger.warn("Failed " + string.toLowerCase(Locale.ROOT) + ": '{}'", (Object)(httpResponse.statusCode() + httpResponse.getHeader("location")));
                }
                CompletableFuture completableFuture = VertxUtil.randomSleep(this.task.getRetryDelay());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$GETREQ(this, string, httpRequest, n, stringArray, n2, completableFuture4, httpResponse, null, 3, arg_0));
                }
                completableFuture.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error " + string.toLowerCase(Locale.ROOT) + ": {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = super.randomSleep(this.task.getRetryDelay());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture5 = completableFuture;
                    return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$GETREQ(this, string, httpRequest, n, stringArray, 0, completableFuture5, null, throwable, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture initLoginHarvesters() {
        if (!this.browser) return CompletableFuture.completedFuture(null);
        LoginHarvester[] loginHarvesterArray = LoginHarvester.LOGIN_HARVESTERS;
        int n = loginHarvesterArray.length;
        int n2 = 0;
        while (n2 < n) {
            LoginHarvester loginHarvester = loginHarvesterArray[n2];
            CompletableFuture completableFuture = loginHarvester.start();
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> Bestbuy.async$initLoginHarvesters(this, loginHarvesterArray, n, n2, loginHarvester, completableFuture2, 1, arg_0));
            }
            completableFuture.join();
            ++n2;
        }
        return CompletableFuture.completedFuture(null);
    }
}

