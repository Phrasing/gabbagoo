/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.buffer.Buffer
 *  io.vertx.core.json.JsonArray
 *  io.vertx.core.json.JsonObject
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.client.HttpResponse
 *  org.apache.logging.log4j.Logger
 */
package io.trickle.task.sites.hibbett;

import io.trickle.core.VertxSingleton;
import io.trickle.profile.Profile;
import io.trickle.task.sites.hibbett.Hibbett;
import io.trickle.task.sites.hibbett.HibbettAPI;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import java.lang.invoke.LambdaMetafactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import org.apache.logging.log4j.Logger;

public class SessionPreload {
    public Profile profile;
    public HibbettAPI client;
    public Logger logger;
    public Hibbett actor;

    public SessionPreload(Hibbett hibbett) {
        this.actor = hibbett;
        this.client = (HibbettAPI)hibbett.getClient();
        this.profile = hibbett.getProfile();
        this.logger = hibbett.getLogger();
    }

    public CompletableFuture fetch() {
        Integer n;
        CompletableFuture completableFuture = this.fetchSession();
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$fetch(this, completableFuture2, null, null, null, 0, null, null, null, null, null, null, null, null, 1, arg_0));
        }
        JsonArray jsonArray = (JsonArray)completableFuture.join();
        String string = "Bearer " + jsonArray.getString(0);
        String string2 = jsonArray.getString(1);
        this.logger.info("Fetching home");
        CompletableFuture completableFuture3 = Request.execute(this.client.shopView(string));
        if (!completableFuture3.isDone()) {
            CompletableFuture completableFuture4 = completableFuture3;
            return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$fetch(this, completableFuture4, jsonArray, string, string2, 0, null, null, null, null, null, null, null, null, 2, arg_0));
        }
        completableFuture3.join();
        int n2 = 3;
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        for (int i = 1; i <= n2; ++i) {
            arrayList.add(i);
        }
        String string3 = null;
        CompletableFuture completableFuture5 = this.fetchNonce(string);
        if (!completableFuture5.isDone()) {
            CompletableFuture completableFuture6 = completableFuture5;
            return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$fetch(this, completableFuture6, jsonArray, string, string2, n2, arrayList, null, null, null, null, null, null, null, 3, arg_0));
        }
        String string4 = (String)completableFuture5.join();
        String string5 = null;
        String string6 = null;
        String string7 = null;
        Collections.shuffle(arrayList);
        Iterator iterator = arrayList.iterator();
        block11: while (iterator.hasNext()) {
            n = (Integer)iterator.next();
            switch (n) {
                case 1: {
                    CompletableFuture completableFuture7 = this.createCart(string);
                    if (!completableFuture7.isDone()) {
                        CompletableFuture completableFuture8 = completableFuture7;
                        return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$fetch(this, completableFuture8, jsonArray, string, string2, n2, arrayList, string3, string4, string5, string6, string7, iterator, n, 4, arg_0));
                    }
                    string3 = (String)completableFuture7.join();
                    break;
                }
                case 2: {
                    CompletableFuture completableFuture9 = this.submitCard(string4);
                    if (!completableFuture9.isDone()) {
                        CompletableFuture completableFuture10 = completableFuture9;
                        return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$fetch(this, completableFuture10, jsonArray, string, string2, n2, arrayList, string3, string4, string5, string6, string7, iterator, n, 5, arg_0));
                    }
                    string5 = (String)completableFuture9.join();
                    break;
                }
                case 3: {
                    CompletableFuture completableFuture11 = this.submitCvv(string4);
                    if (!completableFuture11.isDone()) {
                        CompletableFuture completableFuture12 = completableFuture11;
                        return ((CompletableFuture)completableFuture12.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$fetch(this, completableFuture12, jsonArray, string, string2, n2, arrayList, string3, string4, string5, string6, string7, iterator, n, 6, arg_0));
                    }
                    JsonArray jsonArray2 = (JsonArray)completableFuture11.join();
                    string6 = jsonArray2.getString(0);
                    string7 = jsonArray2.getString(1);
                    continue block11;
                }
            }
        }
        n2 = 3;
        arrayList = new ArrayList();
        for (int i = 1; i <= n2; ++i) {
            arrayList.add(i);
        }
        Collections.shuffle(arrayList);
        Object object = arrayList.iterator();
        block13: while (true) {
            if (!object.hasNext()) {
                object = new HashMap();
                object.put("authorization", string);
                object.put("customerId", string2);
                object.put("ccToken", string5);
                object.put("encryptedCVNValue", string6);
                object.put("encryptedSecCode", string7);
                object.put("cartId", string3);
                return CompletableFuture.completedFuture(object);
            }
            n = (Integer)object.next();
            switch (n) {
                case 1: {
                    CompletableFuture completableFuture13 = this.submitShipping(string, string2, string3);
                    if (!completableFuture13.isDone()) {
                        CompletableFuture completableFuture14 = completableFuture13;
                        return ((CompletableFuture)completableFuture14.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$fetch(this, completableFuture14, jsonArray, string, string2, n2, arrayList, string3, string4, string5, string6, string7, (Iterator)object, n, 7, arg_0));
                    }
                    completableFuture13.join();
                    break;
                }
                case 2: {
                    CompletableFuture completableFuture15 = this.submitShippingRate(string, string3);
                    if (!completableFuture15.isDone()) {
                        CompletableFuture completableFuture16 = completableFuture15;
                        return ((CompletableFuture)completableFuture16.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$fetch(this, completableFuture16, jsonArray, string, string2, n2, arrayList, string3, string4, string5, string6, string7, (Iterator)object, n, 8, arg_0));
                    }
                    completableFuture15.join();
                    break;
                }
                case 3: {
                    CompletableFuture completableFuture17 = this.submitEmail(string, string3);
                    if (!completableFuture17.isDone()) {
                        CompletableFuture completableFuture18 = completableFuture17;
                        return ((CompletableFuture)completableFuture18.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$fetch(this, completableFuture18, jsonArray, string, string2, n2, arrayList, string3, string4, string5, string6, string7, (Iterator)object, n, 9, arg_0));
                    }
                    completableFuture17.join();
                    continue block13;
                }
            }
        }
    }

    public CompletableFuture fetchNonce(String string) {
        int n = 0;
        while (n++ < 99) {
            if (!this.actor.shouldRunOnSchedule()) return CompletableFuture.completedFuture(null);
            HttpRequest httpRequest = this.client.nonce(string);
            try {
                CompletableFuture completableFuture = Request.send(httpRequest);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$fetchNonce(this, string, n, httpRequest, completableFuture2, null, null, 0, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                JsonObject jsonObject = (JsonObject)httpResponse.body();
                if (jsonObject.containsKey("nonce")) {
                    return CompletableFuture.completedFuture(jsonObject.getString("nonce"));
                }
                this.logger.error("Error booting up 2. Retrying - {}", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture3 = this.client.handleBadResponse(httpResponse.statusCode(), jsonObject.containsKey("vid") ? jsonObject.getString("vid") : null, jsonObject.containsKey("uuid") ? jsonObject.getString("uuid") : null);
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$fetchNonce(this, string, n, httpRequest, completableFuture4, httpResponse, jsonObject, 0, null, 2, arg_0));
                }
                int n2 = ((Boolean)completableFuture3.join()).booleanValue() ? 1 : 0;
                if (n2 != 0 || !this.client.isSkip()) continue;
                CompletableFuture completableFuture5 = VertxUtil.randomSleep(5000L);
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$fetchNonce(this, string, n, httpRequest, completableFuture6, httpResponse, jsonObject, n2, null, 3, arg_0));
                }
                completableFuture5.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error occurred step (n): {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = VertxUtil.randomSleep(5000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$fetchNonce(this, string, n, httpRequest, completableFuture7, null, null, 0, throwable, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$fetchNonce(SessionPreload var0, String var1_1, int var2_2, HttpRequest var3_3, CompletableFuture var4_4, HttpResponse var5_6, JsonObject var6_7, int var7_9, Throwable var8_14, int var9_15, Object var10_16) {
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
     * Exception decompiling
     */
    public static CompletableFuture async$submitCvv(SessionPreload var0, String var1_1, JsonObject var2_2, int var3_3, HttpRequest var4_4, CompletableFuture var5_5, HttpResponse var6_7, JsonObject var7_8, int var8_10, Throwable var9_15, int var10_16, Object var11_17) {
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

    public CompletableFuture submitCvv(String string) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("cardSecurityCode", (Object)this.profile.getCvv());
        jsonObject.put("paymentAccountNumber", (Object)this.profile.getCardNumber());
        int n = 0;
        while (n++ < 99) {
            if (!this.actor.shouldRunOnSchedule()) return CompletableFuture.completedFuture(null);
            HttpRequest httpRequest = this.client.submitCvv(string);
            try {
                CompletableFuture completableFuture = Request.send(httpRequest, jsonObject.toBuffer());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$submitCvv(this, string, jsonObject, n, httpRequest, completableFuture2, null, null, 0, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                JsonObject jsonObject2 = (JsonObject)httpResponse.body();
                if (jsonObject2.containsKey("encryptedPaymentAccountNumber") && jsonObject2.containsKey("encryptedCardSecurityCode")) {
                    return CompletableFuture.completedFuture(new JsonArray().add((Object)jsonObject2.getString("encryptedPaymentAccountNumber")).add((Object)jsonObject2.getString("encryptedCardSecurityCode")));
                }
                this.logger.error("Error booting up 4. Retrying - {}", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture3 = this.client.handleBadResponse(httpResponse.statusCode(), jsonObject2.containsKey("vid") ? jsonObject2.getString("vid") : null, jsonObject2.containsKey("uuid") ? jsonObject2.getString("uuid") : null);
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$submitCvv(this, string, jsonObject, n, httpRequest, completableFuture4, httpResponse, jsonObject2, 0, null, 2, arg_0));
                }
                int n2 = ((Boolean)completableFuture3.join()).booleanValue() ? 1 : 0;
                if (n2 != 0 || !this.client.isSkip()) continue;
                CompletableFuture completableFuture5 = VertxUtil.randomSleep(5000L);
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$submitCvv(this, string, jsonObject, n, httpRequest, completableFuture6, httpResponse, jsonObject2, n2, null, 3, arg_0));
                }
                completableFuture5.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error occurred step (2c): {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = VertxUtil.randomSleep(5000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$submitCvv(this, string, jsonObject, n, httpRequest, completableFuture7, null, null, 0, throwable, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture submitEmail(String string, String string2) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("email", (Object)this.profile.getEmail());
        int n = 0;
        while (n++ < 99) {
            if (!this.actor.shouldRunOnSchedule()) return CompletableFuture.completedFuture(null);
            HttpRequest httpRequest = this.client.submitEmail(string, string2);
            try {
                CompletableFuture completableFuture = Request.send(httpRequest, jsonObject.toBuffer());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$submitEmail(this, string, string2, jsonObject, n, httpRequest, completableFuture2, null, null, 0, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                JsonObject jsonObject2 = (JsonObject)httpResponse.body();
                if (jsonObject2.containsKey("customerInformation") && jsonObject2.getJsonObject("customerInformation").containsKey("email")) {
                    return CompletableFuture.completedFuture(null);
                }
                this.logger.error("Error booting up 6. Retrying - {}", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture3 = this.client.handleBadResponse(httpResponse.statusCode(), jsonObject2.containsKey("vid") ? jsonObject2.getString("vid") : null, jsonObject2.containsKey("uuid") ? jsonObject2.getString("uuid") : null);
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$submitEmail(this, string, string2, jsonObject, n, httpRequest, completableFuture4, httpResponse, jsonObject2, 0, null, 2, arg_0));
                }
                int n2 = ((Boolean)completableFuture3.join()).booleanValue() ? 1 : 0;
                if (n2 != 0 || !this.client.isSkip()) continue;
                CompletableFuture completableFuture5 = VertxUtil.randomSleep(5000L);
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$submitEmail(this, string, string2, jsonObject, n, httpRequest, completableFuture6, httpResponse, jsonObject2, n2, null, 3, arg_0));
                }
                completableFuture5.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error occurred at basket: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = VertxUtil.randomSleep(5000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$submitEmail(this, string, string2, jsonObject, n, httpRequest, completableFuture7, null, null, 0, throwable, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture createCart(String string) {
        int n = 0;
        while (n++ < 99999) {
            if (!this.actor.shouldRunOnSchedule()) return CompletableFuture.completedFuture(null);
            HttpRequest httpRequest = this.client.createCart(string);
            try {
                CompletableFuture completableFuture = Request.send(httpRequest, Buffer.buffer((String)""));
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$createCart(this, string, n, httpRequest, completableFuture2, null, null, 0, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                JsonObject jsonObject = (JsonObject)httpResponse.body();
                if (jsonObject.containsKey("basketId")) {
                    return CompletableFuture.completedFuture(jsonObject.getString("basketId"));
                }
                this.logger.error("Error booting up 5. Retrying - {}", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture3 = this.client.handleBadResponse(httpResponse.statusCode(), jsonObject.containsKey("vid") ? jsonObject.getString("vid") : null, jsonObject.containsKey("uuid") ? jsonObject.getString("uuid") : null);
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$createCart(this, string, n, httpRequest, completableFuture4, httpResponse, jsonObject, 0, null, 2, arg_0));
                }
                int n2 = ((Boolean)completableFuture3.join()).booleanValue() ? 1 : 0;
                if (n2 != 0 || !this.client.isSkip()) continue;
                CompletableFuture completableFuture5 = VertxUtil.randomSleep(5000L);
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$createCart(this, string, n, httpRequest, completableFuture6, httpResponse, jsonObject, n2, null, 3, arg_0));
                }
                completableFuture5.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error occurred at basket: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = VertxUtil.randomSleep(5000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$createCart(this, string, n, httpRequest, completableFuture7, null, null, 0, throwable, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$submitCard(SessionPreload var0, String var1_1, JsonObject var2_2, int var3_3, HttpRequest var4_4, CompletableFuture var5_5, HttpResponse var6_7, JsonObject var7_8, int var8_10, Throwable var9_15, int var10_16, Object var11_17) {
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

    public CompletableFuture submitShippingRate(String string, String string2) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("id", (Object)"ANY_GND");
        int n = 0;
        while (n++ < 99) {
            if (!this.actor.shouldRunOnSchedule()) return CompletableFuture.completedFuture(null);
            HttpRequest httpRequest = this.client.submitShippingRate(string, string2);
            try {
                CompletableFuture completableFuture = Request.send(httpRequest, jsonObject.toBuffer());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$submitShippingRate(this, string, string2, jsonObject, n, httpRequest, completableFuture2, null, null, 0, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                JsonObject jsonObject2 = (JsonObject)httpResponse.body();
                if (jsonObject2.containsKey("shipments") && jsonObject2.getJsonArray("shipments").size() != 0 && jsonObject2.getJsonArray("shipments").getJsonObject(0).containsKey("shippingOption") && jsonObject2.getJsonArray("shipments").getJsonObject(0).getValue("shippingOption") != null) {
                    return CompletableFuture.completedFuture(null);
                }
                this.logger.error("Error booting up 8. Retrying - {}", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture3 = this.client.handleBadResponse(httpResponse.statusCode(), jsonObject2.containsKey("vid") ? jsonObject2.getString("vid") : null, jsonObject2.containsKey("uuid") ? jsonObject2.getString("uuid") : null);
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$submitShippingRate(this, string, string2, jsonObject, n, httpRequest, completableFuture4, httpResponse, jsonObject2, 0, null, 2, arg_0));
                }
                int n2 = ((Boolean)completableFuture3.join()).booleanValue() ? 1 : 0;
                if (n2 != 0 || !this.client.isSkip()) continue;
                CompletableFuture completableFuture5 = VertxUtil.randomSleep(5000L);
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$submitShippingRate(this, string, string2, jsonObject, n, httpRequest, completableFuture6, httpResponse, jsonObject2, n2, null, 3, arg_0));
                }
                completableFuture5.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error occurred at (ps1): {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = VertxUtil.randomSleep(5000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$submitShippingRate(this, string, string2, jsonObject, n, httpRequest, completableFuture7, null, null, 0, throwable, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture submitCard(String string) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("paymentAccountNumber", (Object)this.profile.getCardNumber());
        int n = 0;
        while (n++ < 99) {
            if (!this.actor.shouldRunOnSchedule()) return CompletableFuture.completedFuture(null);
            HttpRequest httpRequest = this.client.submitCard(string);
            try {
                CompletableFuture completableFuture = Request.send(httpRequest, jsonObject.toBuffer());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$submitCard(this, string, jsonObject, n, httpRequest, completableFuture2, null, null, 0, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                JsonObject jsonObject2 = (JsonObject)httpResponse.body();
                if (jsonObject2.containsKey("account_token")) {
                    return CompletableFuture.completedFuture(jsonObject2.getString("account_token"));
                }
                this.logger.error("Error booting up 3. Retrying - {}", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture3 = this.client.handleBadResponse(httpResponse.statusCode(), jsonObject2.containsKey("vid") ? jsonObject2.getString("vid") : null, jsonObject2.containsKey("uuid") ? jsonObject2.getString("uuid") : null);
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$submitCard(this, string, jsonObject, n, httpRequest, completableFuture4, httpResponse, jsonObject2, 0, null, 2, arg_0));
                }
                int n2 = ((Boolean)completableFuture3.join()).booleanValue() ? 1 : 0;
                if (n2 != 0 || !this.client.isSkip()) continue;
                CompletableFuture completableFuture5 = VertxUtil.randomSleep(5000L);
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$submitCard(this, string, jsonObject, n, httpRequest, completableFuture6, httpResponse, jsonObject2, n2, null, 3, arg_0));
                }
                completableFuture5.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error occurred step (c): {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = VertxUtil.randomSleep(5000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$submitCard(this, string, jsonObject, n, httpRequest, completableFuture7, null, null, 0, throwable, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$createCart(SessionPreload var0, String var1_1, int var2_2, HttpRequest var3_3, CompletableFuture var4_4, HttpResponse var5_6, JsonObject var6_7, int var7_9, Throwable var8_14, int var9_15, Object var10_16) {
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
     * Exception decompiling
     */
    public static CompletableFuture async$submitShipping(SessionPreload var0, String var1_1, String var2_2, String var3_3, JsonObject var4_4, int var5_5, HttpRequest var6_6, CompletableFuture var7_7, HttpResponse var8_9, JsonObject var9_10, HibbettAPI var10_12, int var11_13, Throwable var12_15, int var13_16, Object var14_17) {
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
     * Exception decompiling
     */
    public static CompletableFuture async$submitShippingRate(SessionPreload var0, String var1_1, String var2_2, JsonObject var3_3, int var4_4, HttpRequest var5_5, CompletableFuture var6_6, HttpResponse var7_8, JsonObject var8_9, int var9_11, Throwable var10_16, int var11_17, Object var12_18) {
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
     * Exception decompiling
     */
    public static CompletableFuture async$fetchSession(SessionPreload var0, int var1_1, HttpRequest var2_2, CompletableFuture var3_3, HttpResponse var4_5, JsonObject var5_6, int var6_8, Throwable var7_13, int var8_14, Object var9_15) {
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

    public CompletableFuture submitShipping(String string, String string2, String string3) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("address1", (Object)this.profile.getAddress1());
        jsonObject.put("address2", (Object)this.profile.getAddress2());
        jsonObject.put("city", (Object)(this.profile.getCity().substring(0, 1).toUpperCase() + this.profile.getCity().substring(1).toLowerCase()));
        jsonObject.put("country", (Object)"US");
        jsonObject.put("firstName", (Object)(this.profile.getFirstName().substring(0, 1).toUpperCase() + this.profile.getFirstName().substring(1).toLowerCase()));
        jsonObject.put("lastName", (Object)(this.profile.getLastName().substring(0, 1).toUpperCase() + this.profile.getLastName().substring(1).toLowerCase()));
        jsonObject.put("phone", (Object)this.profile.getPhone());
        jsonObject.put("save", (Object)true);
        jsonObject.put("state", (Object)this.profile.getState());
        jsonObject.put("zip", (Object)this.profile.getZip());
        int n = 0;
        while (n++ < 99) {
            if (!this.actor.shouldRunOnSchedule()) return CompletableFuture.completedFuture(null);
            HttpRequest httpRequest = this.client.submitShipping(string, string2, string3);
            try {
                CompletableFuture completableFuture = Request.send(httpRequest, jsonObject.toBuffer());
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$submitShipping(this, string, string2, string3, jsonObject, n, httpRequest, completableFuture2, null, null, null, 0, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                JsonObject jsonObject2 = (JsonObject)httpResponse.body();
                if (jsonObject2.containsKey("shipments") && jsonObject2.getJsonArray("shipments").size() != 0 && jsonObject2.getJsonArray("shipments").getJsonObject(0).containsKey("shippingAddress") && jsonObject2.getJsonArray("shipments").getJsonObject(0).getValue("shippingAddress") != null) {
                    return CompletableFuture.completedFuture(null);
                }
                this.logger.error("Error booting up 7. Retrying - {}", (Object)httpResponse.statusCode());
                HibbettAPI hibbettAPI = this.client;
                CompletableFuture completableFuture3 = VertxSingleton.INSTANCE.getLocalClient().fetchDeviceFromAPI("https://loudounchris.xyz/api/device/mobileDeviceWithOS.json");
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    HibbettAPI hibbettAPI2 = hibbettAPI;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$submitShipping(this, string, string2, string3, jsonObject, n, httpRequest, completableFuture4, httpResponse, jsonObject2, hibbettAPI2, 0, null, 2, arg_0));
                }
                hibbettAPI.setDevice((JsonObject)completableFuture3.join());
                if (!ThreadLocalRandom.current().nextBoolean()) continue;
                CompletableFuture completableFuture5 = this.client.handleBadResponse(httpResponse.statusCode(), jsonObject2.containsKey("vid") ? jsonObject2.getString("vid") : null, jsonObject2.containsKey("uuid") ? jsonObject2.getString("uuid") : null);
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$submitShipping(this, string, string2, string3, jsonObject, n, httpRequest, completableFuture6, httpResponse, jsonObject2, null, 0, null, 3, arg_0));
                }
                int n2 = ((Boolean)completableFuture5.join()).booleanValue() ? 1 : 0;
                if (n2 != 0 || !this.client.isSkip()) continue;
                CompletableFuture completableFuture7 = VertxUtil.randomSleep(5000L);
                if (!completableFuture7.isDone()) {
                    CompletableFuture completableFuture8 = completableFuture7;
                    return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$submitShipping(this, string, string2, string3, jsonObject, n, httpRequest, completableFuture8, httpResponse, jsonObject2, null, n2, null, 4, arg_0));
                }
                completableFuture7.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error occurred at (ps): {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = VertxUtil.randomSleep(5000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture9 = completableFuture;
                    return ((CompletableFuture)completableFuture9.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$submitShipping(this, string, string2, string3, jsonObject, n, httpRequest, completableFuture9, null, null, null, 0, throwable, 5, arg_0));
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
    public static CompletableFuture async$fetch(SessionPreload var0, CompletableFuture var1_1, JsonArray var2_2, String var3_3, String var4_4, int var5_6, List var6_8, String var7_10, String var8_11, String var9_12, String var10_13, String var11_14, Iterator var12_16, Integer var13_17, int var14_18, Object var15_28) {
        switch (var14_18) {
            case 0: {
                v0 = var0.fetchSession();
                if (!v0.isDone()) {
                    var14_19 = v0;
                    return var14_19.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$fetch(io.trickle.task.sites.hibbett.SessionPreload java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String java.lang.String int java.util.List java.lang.String java.lang.String java.lang.String java.lang.String java.lang.String java.util.Iterator java.lang.Integer int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((SessionPreload)var0, (CompletableFuture)var14_19, null, null, null, (int)0, null, null, null, null, null, null, null, null, (int)1));
                }
                ** GOTO lbl10
            }
            case 1: {
                v0 = var1_1;
lbl10:
                // 2 sources

                var1_1 = (JsonArray)v0.join();
                var2_2 /* !! */  = "Bearer " + var1_1.getString(0);
                var3_3 = var1_1.getString(1);
                var0.logger.info("Fetching home");
                v1 = Request.execute(var0.client.shopView((String)var2_2 /* !! */ ));
                if (!v1.isDone()) {
                    var14_20 = v1;
                    return var14_20.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$fetch(io.trickle.task.sites.hibbett.SessionPreload java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String java.lang.String int java.util.List java.lang.String java.lang.String java.lang.String java.lang.String java.lang.String java.util.Iterator java.lang.Integer int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((SessionPreload)var0, (CompletableFuture)var14_20, (JsonArray)var1_1, (String)var2_2 /* !! */ , (String)var3_3, (int)0, null, null, null, null, null, null, null, null, (int)2));
                }
                ** GOTO lbl26
            }
            case 2: {
                v1 = var1_1;
                v2 = var2_2 /* !! */ ;
                v3 = var3_3;
                var3_3 = var4_4;
                var2_2 /* !! */  = v3;
                var1_1 = v2;
lbl26:
                // 2 sources

                v1.join();
                var4_5 = 3;
                var5_7 = new ArrayList<Integer>();
                for (var6_9 = 1; var6_9 <= var4_5; ++var6_9) {
                    var5_7.add(var6_9);
                }
                var6_8 = null;
                v4 = var0.fetchNonce((String)var2_2 /* !! */ );
                if (!v4.isDone()) {
                    var14_21 = v4;
                    return var14_21.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$fetch(io.trickle.task.sites.hibbett.SessionPreload java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String java.lang.String int java.util.List java.lang.String java.lang.String java.lang.String java.lang.String java.lang.String java.util.Iterator java.lang.Integer int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((SessionPreload)var0, (CompletableFuture)var14_21, (JsonArray)var1_1, (String)var2_2 /* !! */ , (String)var3_3, (int)var4_5, var5_7, null, null, null, null, null, null, null, (int)3));
                }
                ** GOTO lbl50
            }
            case 3: {
                v4 = var1_1;
                v5 = var2_2 /* !! */ ;
                v6 = var3_3;
                var5_7 = var6_8;
                var4_5 = var5_6;
                var3_3 = var4_4;
                var2_2 /* !! */  = v6;
                var1_1 = v5;
                var6_8 = null;
lbl50:
                // 2 sources

                var7_10 = (String)v4.join();
                var8_11 = null;
                var9_12 = null;
                var10_13 = null;
                Collections.shuffle(var5_7);
                var11_14 = var5_7.iterator();
lbl56:
                // 2 sources

                while (true) {
                    if (!var11_14.hasNext()) ** GOTO lbl79
                    var12_16 = (Integer)var11_14.next();
                    switch (var12_16.intValue()) {
                        case 1: {
                            v7 = var0.createCart((String)var2_2 /* !! */ );
                            if (!v7.isDone()) {
                                var14_22 = v7;
                                return var14_22.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$fetch(io.trickle.task.sites.hibbett.SessionPreload java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String java.lang.String int java.util.List java.lang.String java.lang.String java.lang.String java.lang.String java.lang.String java.util.Iterator java.lang.Integer int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((SessionPreload)var0, (CompletableFuture)var14_22, (JsonArray)var1_1, (String)var2_2 /* !! */ , (String)var3_3, (int)var4_5, var5_7, (String)var6_8, (String)var7_10, (String)var8_11, (String)var9_12, (String)var10_13, var11_14, (Integer)var12_16, (int)4));
                            }
                            ** GOTO lbl147
                        }
                        case 2: {
                            v8 = var0.submitCard(var7_10);
                            if (!v8.isDone()) {
                                var14_23 = v8;
                                return var14_23.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$fetch(io.trickle.task.sites.hibbett.SessionPreload java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String java.lang.String int java.util.List java.lang.String java.lang.String java.lang.String java.lang.String java.lang.String java.util.Iterator java.lang.Integer int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((SessionPreload)var0, (CompletableFuture)var14_23, (JsonArray)var1_1, (String)var2_2 /* !! */ , (String)var3_3, (int)var4_5, var5_7, (String)var6_8, (String)var7_10, (String)var8_11, (String)var9_12, (String)var10_13, var11_14, (Integer)var12_16, (int)5));
                            }
                            ** GOTO lbl172
                        }
                        case 3: {
                            v9 = var0.submitCvv(var7_10);
                            if (!v9.isDone()) {
                                var14_24 = v9;
                                return var14_24.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$fetch(io.trickle.task.sites.hibbett.SessionPreload java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String java.lang.String int java.util.List java.lang.String java.lang.String java.lang.String java.lang.String java.lang.String java.util.Iterator java.lang.Integer int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((SessionPreload)var0, (CompletableFuture)var14_24, (JsonArray)var1_1, (String)var2_2 /* !! */ , (String)var3_3, (int)var4_5, var5_7, (String)var6_8, (String)var7_10, (String)var8_11, (String)var9_12, (String)var10_13, var11_14, (Integer)var12_16, (int)6));
                            }
                            ** GOTO lbl197
                        }
                    }
                    continue;
lbl79:
                    // 1 sources

                    var4_5 = 3;
                    var5_7 = new ArrayList<E>();
                    for (var11_15 = 1; var11_15 <= var4_5; ++var11_15) {
                        var5_7.add(var11_15);
                    }
                    Collections.shuffle(var5_7);
                    var11_14 = var5_7.iterator();
lbl87:
                    // 2 sources

                    while (true) {
                        if (!var11_14.hasNext()) {
                            var11_14 = new HashMap<K, V>();
                            var11_14.put("authorization", var2_2 /* !! */ );
                            var11_14.put("customerId", var3_3);
                            var11_14.put("ccToken", var8_11);
                            var11_14.put("encryptedCVNValue", var9_12);
                            var11_14.put("encryptedSecCode", var10_13);
                            var11_14.put("cartId", var6_8);
                            return CompletableFuture.completedFuture(var11_14);
                        }
                        var12_16 = (Integer)var11_14.next();
                        switch (var12_16.intValue()) {
                            case 1: {
                                v10 = var0.submitShipping((String)var2_2 /* !! */ , var3_3, (String)var6_8);
                                if (!v10.isDone()) {
                                    var14_25 = v10;
                                    return var14_25.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$fetch(io.trickle.task.sites.hibbett.SessionPreload java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String java.lang.String int java.util.List java.lang.String java.lang.String java.lang.String java.lang.String java.lang.String java.util.Iterator java.lang.Integer int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((SessionPreload)var0, (CompletableFuture)var14_25, (JsonArray)var1_1, (String)var2_2 /* !! */ , (String)var3_3, (int)var4_5, var5_7, (String)var6_8, (String)var7_10, (String)var8_11, (String)var9_12, (String)var10_13, var11_14, (Integer)var12_16, (int)7));
                                }
                                ** GOTO lbl224
                            }
                            case 2: {
                                v11 = var0.submitShippingRate((String)var2_2 /* !! */ , (String)var6_8);
                                if (!v11.isDone()) {
                                    var14_26 = v11;
                                    return var14_26.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$fetch(io.trickle.task.sites.hibbett.SessionPreload java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String java.lang.String int java.util.List java.lang.String java.lang.String java.lang.String java.lang.String java.lang.String java.util.Iterator java.lang.Integer int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((SessionPreload)var0, (CompletableFuture)var14_26, (JsonArray)var1_1, (String)var2_2 /* !! */ , (String)var3_3, (int)var4_5, var5_7, (String)var6_8, (String)var7_10, (String)var8_11, (String)var9_12, (String)var10_13, var11_14, (Integer)var12_16, (int)8));
                                }
                                ** GOTO lbl250
                            }
                            case 3: {
                                v12 = var0.submitEmail((String)var2_2 /* !! */ , (String)var6_8);
                                if (!v12.isDone()) {
                                    var14_27 = v12;
                                    return var14_27.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$fetch(io.trickle.task.sites.hibbett.SessionPreload java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String java.lang.String int java.util.List java.lang.String java.lang.String java.lang.String java.lang.String java.lang.String java.util.Iterator java.lang.Integer int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((SessionPreload)var0, (CompletableFuture)var14_27, (JsonArray)var1_1, (String)var2_2 /* !! */ , (String)var3_3, (int)var4_5, var5_7, (String)var6_8, (String)var7_10, (String)var8_11, (String)var9_12, (String)var10_13, var11_14, (Integer)var12_16, (int)9));
                                }
                                ** GOTO lbl276
                            }
                        }
                        continue;
                        break;
                    }
                    break;
                }
            }
            case 4: {
                v7 = var1_1;
                v13 = var2_2 /* !! */ ;
                v14 = var3_3;
                v15 = var6_8;
                v16 = var7_10;
                v17 = var8_11;
                v18 = var9_12;
                v19 = var10_13;
                v20 = var11_14;
                v21 = var12_16;
                var12_16 = var13_17;
                var11_14 = v21;
                var10_13 = v20;
                var9_12 = v19;
                var8_11 = v18;
                var7_10 = v17;
                var6_8 = v16;
                var5_7 = v15;
                var4_5 = var5_6;
                var3_3 = var4_4;
                var2_2 /* !! */  = v14;
                var1_1 = v13;
lbl147:
                // 2 sources

                var6_8 = (String)v7.join();
                ** break;
            }
            case 5: {
                v8 = var1_1;
                v22 = var2_2 /* !! */ ;
                v23 = var3_3;
                v24 = var6_8;
                v25 = var7_10;
                v26 = var8_11;
                v27 = var9_12;
                v28 = var10_13;
                v29 = var11_14;
                v30 = var12_16;
                var12_16 = var13_17;
                var11_14 = v30;
                var10_13 = v29;
                var9_12 = v28;
                var8_11 = v27;
                var7_10 = v26;
                var6_8 = v25;
                var5_7 = v24;
                var4_5 = var5_6;
                var3_3 = var4_4;
                var2_2 /* !! */  = v23;
                var1_1 = v22;
lbl172:
                // 2 sources

                var8_11 = (String)v8.join();
                ** break;
            }
            case 6: {
                v9 = var1_1;
                v31 = var2_2 /* !! */ ;
                v32 = var3_3;
                v33 = var6_8;
                v34 = var7_10;
                v35 = var8_11;
                v36 = var9_12;
                v37 = var10_13;
                v38 = var11_14;
                v39 = var12_16;
                var12_16 = var13_17;
                var11_14 = v39;
                var10_13 = v38;
                var9_12 = v37;
                var8_11 = v36;
                var7_10 = v35;
                var6_8 = v34;
                var5_7 = v33;
                var4_5 = var5_6;
                var3_3 = var4_4;
                var2_2 /* !! */  = v32;
                var1_1 = v31;
lbl197:
                // 2 sources

                var13_17 = (JsonArray)v9.join();
                var9_12 = var13_17.getString(0);
                var10_13 = var13_17.getString(1);
lbl200:
                // 3 sources

                ** continue;
            }
            case 7: {
                v10 = var1_1;
                v40 = var2_2 /* !! */ ;
                v41 = var3_3;
                v42 = var6_8;
                v43 = var7_10;
                v44 = var8_11;
                v45 = var9_12;
                v46 = var10_13;
                v47 = var11_14;
                v48 = var12_16;
                var12_16 = var13_17;
                var11_14 = v48;
                var10_13 = v47;
                var9_12 = v46;
                var8_11 = v45;
                var7_10 = v44;
                var6_8 = v43;
                var5_7 = v42;
                var4_5 = var5_6;
                var3_3 = var4_4;
                var2_2 /* !! */  = v41;
                var1_1 = v40;
lbl224:
                // 2 sources

                v10.join();
                ** break;
            }
            case 8: {
                v11 = var1_1;
                v49 = var2_2 /* !! */ ;
                v50 = var3_3;
                v51 = var6_8;
                v52 = var7_10;
                v53 = var8_11;
                v54 = var9_12;
                v55 = var10_13;
                v56 = var11_14;
                v57 = var12_16;
                var12_16 = var13_17;
                var11_14 = v57;
                var10_13 = v56;
                var9_12 = v55;
                var8_11 = v54;
                var7_10 = v53;
                var6_8 = v52;
                var5_7 = v51;
                var4_5 = var5_6;
                var3_3 = var4_4;
                var2_2 /* !! */  = v50;
                var1_1 = v49;
lbl250:
                // 2 sources

                v11.join();
                ** break;
            }
            case 9: {
                v12 = var1_1;
                v58 = var2_2 /* !! */ ;
                v59 = var3_3;
                v60 = var6_8;
                v61 = var7_10;
                v62 = var8_11;
                v63 = var9_12;
                v64 = var10_13;
                v65 = var11_14;
                v66 = var12_16;
                var12_16 = var13_17;
                var11_14 = v66;
                var10_13 = v65;
                var9_12 = v64;
                var8_11 = v63;
                var7_10 = v62;
                var6_8 = v61;
                var5_7 = v60;
                var4_5 = var5_6;
                var3_3 = var4_4;
                var2_2 /* !! */  = v59;
                var1_1 = v58;
lbl276:
                // 2 sources

                v12.join();
lbl278:
                // 3 sources

                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }

    public static CompletableFuture createSession(Hibbett hibbett) {
        return new SessionPreload(hibbett).fetch();
    }

    public CompletableFuture fetchSession() {
        int n = 0;
        while (n++ < 99) {
            if (!this.actor.shouldRunOnSchedule()) return CompletableFuture.completedFuture(null);
            HttpRequest httpRequest = this.client.session();
            try {
                CompletableFuture completableFuture = Request.send(httpRequest, Buffer.buffer((String)""));
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture2 = completableFuture;
                    return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$fetchSession(this, n, httpRequest, completableFuture2, null, null, 0, null, 1, arg_0));
                }
                HttpResponse httpResponse = (HttpResponse)completableFuture.join();
                if (httpResponse == null) continue;
                JsonObject jsonObject = (JsonObject)httpResponse.body();
                if (jsonObject.containsKey("sessionId") && jsonObject.containsKey("customerId")) {
                    return CompletableFuture.completedFuture(new JsonArray().add((Object)jsonObject.getString("sessionId")).add((Object)jsonObject.getString("customerId")));
                }
                this.logger.info("Error occurred at warming: '{}'", (Object)httpResponse.statusCode());
                CompletableFuture completableFuture3 = this.client.handleBadResponse(httpResponse.statusCode(), jsonObject.containsKey("vid") ? jsonObject.getString("vid") : null, jsonObject.containsKey("uuid") ? jsonObject.getString("uuid") : null);
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$fetchSession(this, n, httpRequest, completableFuture4, httpResponse, jsonObject, 0, null, 2, arg_0));
                }
                int n2 = ((Boolean)completableFuture3.join()).booleanValue() ? 1 : 0;
                if (n2 != 0 || !this.client.isSkip()) continue;
                CompletableFuture completableFuture5 = VertxUtil.randomSleep(5000L);
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$fetchSession(this, n, httpRequest, completableFuture6, httpResponse, jsonObject, n2, null, 3, arg_0));
                }
                completableFuture5.join();
            }
            catch (Throwable throwable) {
                this.logger.error("Error occurred at warming: {}", (Object)throwable.getMessage());
                CompletableFuture completableFuture = VertxUtil.randomSleep(5000L);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture7 = completableFuture;
                    return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> SessionPreload.async$fetchSession(this, n, httpRequest, completableFuture7, null, null, 0, throwable, 4, arg_0));
                }
                completableFuture.join();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$submitEmail(SessionPreload var0, String var1_1, String var2_2, JsonObject var3_3, int var4_4, HttpRequest var5_5, CompletableFuture var6_6, HttpResponse var7_8, JsonObject var8_9, int var9_11, Throwable var10_16, int var11_17, Object var12_18) {
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
}

