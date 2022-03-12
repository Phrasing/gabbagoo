/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.json.JsonArray
 *  io.vertx.core.json.JsonObject
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.codec.BodyCodec
 */
package io.trickle.task.sites.bestbuy;

import io.trickle.account.Account;
import io.trickle.profile.Profile;
import io.trickle.task.Task;
import io.trickle.task.antibot.impl.akamai.HawkAPI;
import io.trickle.task.sites.bestbuy.Encryption;
import io.trickle.task.sites.bestbuy.Login;
import io.trickle.util.Utils;
import io.trickle.webclient.TaskApiClient;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.codec.BodyCodec;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class BestbuyAPI
extends TaskApiClient {
    public static DateTimeFormatter BASIC_ISO_DATE;
    public String cartId;
    public String SKU;
    public String userAgent;
    public String id;
    public String encryptedCard;
    public static String QUEUE_URL;
    public Login login;
    public Task task;
    public String sensorUrl = "https://www.bestbuy.com/2yECBVJi5xf17/zRM/BInoAzUb5KA/YDGOzGht/VjtXHEtQXw/Wmt4/NTM9SV8";
    public String orderId;
    public String store;
    public boolean isInstore;
    public String paymentId;
    public HawkAPI hawkAPI;

    public JsonObject accountLoginForm(Account account, JsonObject jsonObject, JsonObject jsonObject2) {
        String string = Encryption.encryptGeneral(jsonObject.getString("publicKey"), "{\"mouseMoved\":true,\"keyboardUsed\":false,\"fieldReceivedInput\":true,\"fieldReceivedFocus\":true,\"timestamp\":\"" + BASIC_ISO_DATE.format(Instant.now()) + "\",\"email\":\"" + account.getUser() + "\"}");
        JsonObject jsonObject3 = new JsonObject();
        jsonObject3.put("token", (Object)this.login.initData.getString("token"));
        jsonObject3.put("activity", (Object)("1:" + jsonObject.getString("keyId") + ":" + string));
        jsonObject3.put("loginMethod", (Object)"UID_PASSWORD");
        jsonObject3.put("flowOptions", (Object)this.login.initData.getString("flowOptions"));
        jsonObject3.put("alpha", (Object)this.login.getAlpha());
        jsonObject3.put("Salmon", (Object)this.login.initData.getString("Salmon"));
        jsonObject3.put("encryptedEmail", (Object)("1:" + jsonObject2.getString("keyId") + ":" + Encryption.encryptGeneral(jsonObject2.getString("publicKey"), account.getUser())));
        jsonObject3.put(this.login.getCode(), (Object)account.getPass());
        jsonObject3.put("info", (Object)("1:" + jsonObject.getString("keyId") + ":" + Encryption.encryptGeneral(jsonObject.getString("publicKey"), "{\"userAgent\":\"" + this.userAgent + "\"}")));
        jsonObject3.put(this.login.initData.getString("emailFieldName"), (Object)account.getUser());
        return jsonObject3;
    }

    public HttpRequest refreshCheckout() {
        HttpRequest httpRequest = this.client.getAbs("https://www.bestbuy.com/checkout/orders/refreshToken/").as(BodyCodec.buffer());
        httpRequest.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"96\", \"Google Chrome\";v=\"96\"");
        httpRequest.putHeader("x-user-interface", "DotCom-Optimized");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
        httpRequest.putHeader("x-native-checkout-version", "__VERSION__");
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.bestbuy.com/checkout/r/payment");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest pickVerification() {
        HttpRequest httpRequest = this.client.postAbs("https://www.bestbuy.com/identity/account/recovery/code").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("sec-ch-ua", "\" Not;A Brand\";v=\"99\", \"Google Chrome\";v=\"97\", \"Chromium\";v=\"97\"");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("accept", "application/json");
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("origin", "https://www.bestbuy.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.bestbuy.com/identity/signin/recoveryOptions?token=" + this.login.initData.getString("token"));
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest submitContact() {
        HttpRequest httpRequest = this.client.patchAbs(this.isInstore ? "https://www.bestbuy.com/checkout/orders/" + this.id + "/items" : "https://www.bestbuy.com/checkout/orders/" + this.id).as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"93\", \" Not;A Brand\";v=\"99\", \"Chromium\";v=\"93\"");
        httpRequest.putHeader("accept", "application/com.bestbuy.order+json");
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("x-user-interface", "DotCom-Optimized");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("origin", "https://www.bestbuy.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.bestbuy.com/checkout/r/fulfillment");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest loginPage(String string) {
        HttpRequest httpRequest = this.client.getAbs(string).as(BodyCodec.buffer());
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Safari/537.36");
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("sec-ch-ua", "\"Chromium\";v=\"94\", \"Google Chrome\";v=\"94\", \";Not A Brand\";v=\"99\"");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("referer", "https://www.bestbuy.com/");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        httpRequest.putHeader("cookie", "DEFAULT_VALUE");
        return httpRequest;
    }

    public HttpRequest setAsDelivery() {
        HttpRequest httpRequest = this.client.putAbs("https://www.bestbuy.com/cart/item/" + this.id + "/fulfillment").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"93\", \" Not;A Brand\";v=\"99\", \"Chromium\";v=\"93\"");
        httpRequest.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("x-order-id", this.orderId);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("origin", "https://www.bestbuy.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.bestbuy.com/cart");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest accountGen() {
        HttpRequest httpRequest = this.client.postAbs("https://www.bestbuy.com/identity/createAccount").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"96\", \"Google Chrome\";v=\"96\"");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("accept", "application/json");
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("origin", "https://www.bestbuy.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.bestbuy.com/identity/newAccount?token=" + this.login.initData.getString("token"));
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    static {
        QUEUE_URL = "https://www.bestbuy.com/cart/api/v1/addToCart";
        BASIC_ISO_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX").withZone(ZoneOffset.UTC);
    }

    public HttpRequest fetchPublicKey() {
        HttpRequest httpRequest = this.client.getAbs("https://www.bestbuy.com/api/csiservice/v2/key/tas").as(BodyCodec.buffer());
        httpRequest.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"93\", \" Not;A Brand\";v=\"99\", \"Chromium\";v=\"93\"");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.bestbuy.com/checkout/r/payment");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest atc(String string, String string2) {
        HttpRequest httpRequest = this.client.postAbs("https://www.bestbuy.com/cart/api/v1/addToCart").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("sec-ch-ua", "\"Chromium\";v=\"92\", \" Not A;Brand\";v=\"99\", \"Google Chrome\";v=\"92\"");
        httpRequest.putHeader("accept", "application/json");
        httpRequest.putHeader("a2ctransactioncode", string);
        httpRequest.putHeader("a2ctransactionreferenceid", string2);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("content-type", "application/json; charset=UTF-8");
        httpRequest.putHeader("origin", "https://www.bestbuy.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", this.task.getKeywords()[0]);
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest ciaGrid(String string) {
        HttpRequest httpRequest = this.client.getAbs("https://www.bestbuy.com/api/csiservice/v2/key/cia-grid").as(BodyCodec.buffer());
        httpRequest.putHeader("sec-ch-ua", "\"Chromium\";v=\"94\", \"Google Chrome\";v=\"94\", \";Not A Brand\";v=\"99\"");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", string);
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest initCheckout() {
        HttpRequest httpRequest = this.client.postAbs("https://www.bestbuy.com/cart/checkout").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"93\", \" Not;A Brand\";v=\"99\", \"Chromium\";v=\"93\"");
        httpRequest.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("x-order-id", "b160dbe0-fc6a-11eb-9e46-a593e4b8bf6a");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("origin", "https://www.bestbuy.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.bestbuy.com/cart");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public void setSensorUrl(String string) {
        this.sensorUrl = string;
    }

    public JsonObject fetch3dJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("binNumber", (Object)this.task.getProfile().getCardNumber().substring(0, 6));
        jsonObject.put("orderId", (Object)this.orderId);
        jsonObject.put("paymentId", (Object)this.paymentId);
        jsonObject.put("browserInfo", (Object)new JsonObject().put("javaEnabled", (Object)false).put("language", (Object)"en-US").put("userAgent", (Object)this.userAgent).put("height", (Object)"1097").put("width", (Object)"1097").put("timeZone", (Object)"240").put("colorDepth", (Object)"30"));
        return jsonObject;
    }

    public HttpRequest productPage() {
        HttpRequest httpRequest = this.client.getAbs(this.task.getKeywords()[0]).as(BodyCodec.buffer());
        httpRequest.putHeader("sec-ch-ua", "\"Chromium\";v=\"92\", \" Not A;Brand\";v=\"99\", \"Google Chrome\";v=\"92\"");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "none");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public JsonObject emailJson() {
        Profile profile = this.task.getProfile();
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("phoneNumber", (Object)profile.getPhone());
        jsonObject.put("smsNotifyNumber", (Object)"");
        jsonObject.put("smsOptIn", (Object)false);
        jsonObject.put("emailAddress", (Object)profile.getEmail());
        return jsonObject;
    }

    public JsonObject placeOrderForm() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("browserInfo", (Object)new JsonObject().put("javaEnabled", (Object)false).put("language", (Object)"en-US").put("userAgent", (Object)this.userAgent).put("height", (Object)"1097").put("width", (Object)"1097").put("timeZone", (Object)"240").put("colorDepth", (Object)"30"));
        return jsonObject;
    }

    public HttpRequest checkoutPage() {
        HttpRequest httpRequest = this.client.getAbs("https://www.bestbuy.com/checkout/r/fast-track").as(BodyCodec.buffer());
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"93\", \" Not;A Brand\";v=\"99\", \"Chromium\";v=\"93\"");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("referer", "https://www.bestbuy.com/identity/signin?token=tid%3Ad800c595-102b-11ec-98b5-005056aea131");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest getCartItems() {
        HttpRequest httpRequest = this.client.getAbs("https://www.bestbuy.com/cart/json?isDeviceApplePayEligible=false").as(BodyCodec.buffer());
        httpRequest.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"96\", \"Google Chrome\";v=\"96\"");
        httpRequest.putHeader("dnt", "1");
        httpRequest.putHeader("x-order-id", "");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.bestbuy.com/cart");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest vaultCC() {
        HttpRequest httpRequest = this.client.putAbs("https://www.bestbuy.com/payment/api/v1/payment/" + this.paymentId + "/creditCard").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"93\", \" Not;A Brand\";v=\"99\", \"Chromium\";v=\"93\"");
        httpRequest.putHeader("x-context-id", this.orderId);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
        httpRequest.putHeader("x-client", "CHECKOUT");
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("origin", "https://www.bestbuy.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.bestbuy.com/checkout/r/payment");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest loginRedirectFetch() {
        HttpRequest httpRequest = this.client.getAbs("https://www.bestbuy.com/identity/global/signin").as(BodyCodec.buffer());
        httpRequest.putHeader("sec-ch-ua", "\"Chromium\";v=\"94\", \"Google Chrome\";v=\"94\", \";Not A Brand\";v=\"99\"");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Safari/537.36");
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("referer", "https://www.bestbuy.com/");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        httpRequest.putHeader("cookie", "DEFAULT_VALUE");
        return httpRequest;
    }

    public HttpRequest submit3dAuth() {
        HttpRequest httpRequest = this.client.postAbs("https://www.bestbuy.com/checkout/api/1.0/paysecure/submitCardAuthentication").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"93\", \" Not;A Brand\";v=\"99\", \"Chromium\";v=\"93\"");
        httpRequest.putHeader("x-user-interface", "DotCom-Optimized");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
        httpRequest.putHeader("x-native-checkout-version", "__VERSION__");
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("origin", "https://www.bestbuy.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.bestbuy.com/checkout/r/payment");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public JsonObject atcForm() {
        JsonObject jsonObject = new JsonObject();
        if (this.isInstore) {
            jsonObject.put("items", (Object)new JsonArray().add((Object)new JsonObject().put("skuId", (Object)this.SKU).put("storeId", (Object)this.store).put("quantity", (Object)1)));
            return jsonObject;
        }
        jsonObject.put("items", (Object)new JsonArray().add((Object)new JsonObject().put("skuId", (Object)this.SKU)));
        return jsonObject;
    }

    public JsonObject accountGenForm() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("firstName", (Object)this.task.getProfile().getFirstName());
        jsonObject.put("lastName", (Object)this.task.getProfile().getLastName());
        jsonObject.put("phone", (Object)this.task.getProfile().getPhone());
        jsonObject.put("token", (Object)this.login.initData.getString("token"));
        jsonObject.put("memberId", (Object)"");
        jsonObject.put("isRecoveryPhone", (Object)false);
        jsonObject.put("addressLine1", null);
        jsonObject.put("addressLine2", null);
        jsonObject.put("city", null);
        jsonObject.put("state", null);
        jsonObject.put("zip", null);
        jsonObject.put("businessName", null);
        jsonObject.put("businessPhone", null);
        jsonObject.put("alpha", (Object)this.login.getAlpha());
        jsonObject.put("Roe", (Object)this.login.initData.getString("Roe"));
        jsonObject.put(this.login.initData.getString("emailFieldName"), (Object)(Utils.generateStrongString() + "@" + this.task.getProfile().getEmail().split("@")[1]));
        String string = Utils.generateStrongString();
        jsonObject.put(this.login.initData.getString("hash"), (Object)string);
        jsonObject.put(this.login.initData.getString("reenterHash"), (Object)string);
        return jsonObject;
    }

    public JsonObject vaultJson() {
        Profile profile = this.task.getProfile();
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("billingAddress", (Object)new JsonObject().put("country", (Object)"US").put("useAddressAsBilling", (Object)true).put("middleInitial", (Object)"").put("lastName", (Object)profile.getLastName()).put("isWishListAddress", (Object)false).put("city", (Object)profile.getCity()).put("state", (Object)profile.getState()).put("firstName", (Object)profile.getFirstName()).put("addressLine1", (Object)profile.getAddress1()).put("addressLine2", (Object)profile.getAddress2()).put("dayPhone", (Object)profile.getPhone()).put("postalCode", (Object)profile.getZip()).put("standardized", (Object)true).put("userOverridden", (Object)true));
        jsonObject.put("creditCard", (Object)new JsonObject().put("hasCID", (Object)false).put("invalidCard", (Object)false).put("isCustomerCard", (Object)false).put("isNewCard", (Object)true).put("isVisaCheckout", (Object)false).put("govPurchaseCard", (Object)false).put("number", (Object)this.encryptedCard).put("binNumber", (Object)profile.getCardNumber().substring(0, 6)).put("isPWPRegistered", (Object)false).put("expMonth", (Object)profile.getExpiryMonth()).put("expYear", (Object)profile.getExpiryYear()).put("cvv", (Object)profile.getCvv()).put("orderId", (Object)this.orderId).put("saveToProfile", (Object)false).put("type", (Object)profile.getCardType().name()).put("international", (Object)false).put("virtualCard", (Object)false));
        return jsonObject;
    }

    public HttpRequest submitEmail() {
        HttpRequest httpRequest = this.client.patchAbs("https://www.bestbuy.com/checkout/orders/" + this.id).as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"93\", \" Not;A Brand\";v=\"99\", \"Chromium\";v=\"93\"");
        httpRequest.putHeader("accept", "application/com.bestbuy.order+json");
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("x-user-interface", "DotCom-Optimized");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("origin", "https://www.bestbuy.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.bestbuy.com/checkout/r/fulfillment");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public JsonObject verificationJson(String string) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("token", (Object)this.login.initData.getString("token"));
        jsonObject.put("isResetFlow", (Object)false);
        jsonObject.put("challengeType", (Object)this.login.challengeType);
        jsonObject.put("flowOptions", (Object)this.login.flowOptions);
        jsonObject.put(this.login.initData.getString("emailFieldName"), (Object)this.task.getProfile().getEmail());
        jsonObject.put(this.login.initData.getString("verificationCodeFieldName"), (Object)string);
        return jsonObject;
    }

    public HttpRequest login() {
        HttpRequest httpRequest = this.client.postAbs("https://www.bestbuy.com/identity/authenticate").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"96\", \"Google Chrome\";v=\"96\"");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("accept", "application/json");
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("origin", "https://www.bestbuy.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.bestbuy.com/identity/signin?token=" + this.login.initData.getString("token"));
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public JsonObject atcForm(String string) {
        JsonObject jsonObject = new JsonObject();
        if (this.isInstore) {
            jsonObject.put("items", (Object)new JsonArray().add((Object)new JsonObject().put("skuId", (Object)string).put("storeId", (Object)this.store).put("quantity", (Object)1)));
            return jsonObject;
        }
        jsonObject.put("items", (Object)new JsonArray().add((Object)new JsonObject().put("skuId", (Object)string)));
        return jsonObject;
    }

    public HttpRequest placeOrder() {
        HttpRequest httpRequest = this.client.postAbs("https://www.bestbuy.com/checkout/orders/" + this.id + "/").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"93\", \" Not;A Brand\";v=\"99\", \"Chromium\";v=\"93\"");
        httpRequest.putHeader("accept", "application/com.bestbuy.order+json");
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("x-user-interface", "DotCom-Optimized");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("origin", "https://www.bestbuy.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.bestbuy.com/checkout/r/payment");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest emailGrid() {
        HttpRequest httpRequest = this.client.getAbs("https://www.bestbuy.com/api/csiservice/v2/key/cia-email").as(BodyCodec.buffer());
        httpRequest.putHeader("sec-ch-ua", "\"Chromium\";v=\"94\", \"Google Chrome\";v=\"94\", \";Not A Brand\";v=\"99\"");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.bestbuy.com/identity/signin?token=tid%3A05a6a146-287a-11ec-93ba-0ac72817b4c3");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest deleteItem(String string) {
        HttpRequest httpRequest = this.client.deleteAbs("https://www.bestbuy.com/cart/item/" + string).as(BodyCodec.buffer());
        httpRequest.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"96\", \"Google Chrome\";v=\"96\"");
        httpRequest.putHeader("dnt", "1");
        httpRequest.putHeader("x-order-id", this.orderId);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("origin", "https://www.bestbuy.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.bestbuy.com/cart");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest getStoreId() {
        HttpRequest httpRequest = this.client.postAbs("https://www.bestbuy.com/productfulfillment/com/api/2.0/storeAvailability").as(BodyCodec.buffer());
        httpRequest.putHeader("Content-Length", "DEFAULT_VALUE");
        httpRequest.putHeader("cache-control", "no-cache");
        httpRequest.putHeader("sec-ch-ua", "\" Not;A Brand\";v=\"99\", \"Google Chrome\";v=\"91\", \"Chromium\";v=\"91\"");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.bestbuy.com/");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        httpRequest.putHeader("Cookie", "DEFAULT_VALUE");
        httpRequest.putHeader("Content-Type", "application/json");
        return httpRequest;
    }

    public HttpRequest fetch3dsParams() {
        HttpRequest httpRequest = this.client.postAbs("https://www.bestbuy.com/payment/api/v1/payment/" + this.paymentId + "/threeDSecure/preLookup").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"93\", \" Not;A Brand\";v=\"99\", \"Chromium\";v=\"93\"");
        httpRequest.putHeader("x-client", "CHECKOUT");
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("origin", "https://www.bestbuy.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.bestbuy.com/checkout/r/payment");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest cartDetails() {
        HttpRequest httpRequest = this.client.getAbs("https://www.bestbuy.com/cart/json?isDeviceApplePayEligible=false").as(BodyCodec.buffer());
        httpRequest.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"93\", \" Not;A Brand\";v=\"99\", \"Chromium\";v=\"93\"");
        httpRequest.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("x-order-id", "");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.bestbuy.com/cart");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public JsonObject submit3dJson(String string) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("orderId", (Object)this.id);
        jsonObject.put("threeDSecureStatus", (Object)new JsonObject().put("threeDSReferenceId", (Object)string));
        return jsonObject;
    }

    public HttpRequest sendSensor() {
        HttpRequest httpRequest = this.client.postAbs(this.sensorUrl).as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("sec-ch-ua", "\"Chromium\";v=\"92\", \" Not A;Brand\";v=\"99\", \"Google Chrome\";v=\"92\"");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("content-type", "text/plain;charset=UTF-8");
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("origin", "https://www.bestbuy.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", this.task.getKeywords()[0]);
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest verificationCode() {
        HttpRequest httpRequest = this.client.postAbs("https://www.bestbuy.com/identity/unlock").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"96\", \"Google Chrome\";v=\"96\"");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("accept", "application/json");
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("origin", "https://www.bestbuy.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.bestbuy.com/identity/signin/verificationCode?token=" + this.login.initData.getString("token"));
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public Object contactJson() {
        Profile profile = this.task.getProfile();
        if (this.isInstore) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.put("id", (Object)this.cartId);
            jsonObject.put("type", (Object)"DEFAULT");
            jsonObject.put("storeFulfillmentType", (Object)"InStore");
            jsonObject.put("selectedFulfillment", (Object)new JsonObject().put("inStorePickup", (Object)new JsonObject().put("pickupStoreId", (Object)this.store)));
            return new JsonArray().add((Object)jsonObject);
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("id", (Object)this.cartId);
        jsonObject.put("type", (Object)"DEFAULT");
        jsonObject.put("selectedFulfillment", (Object)new JsonObject().put("shipping", (Object)new JsonObject().put("address", (Object)new JsonObject().put("country", (Object)"US").put("saveToProfile", (Object)false).put("street2", (Object)profile.getAddress2()).put("useAddressAsBilling", (Object)true).put("middleInitial", (Object)"").put("lastName", (Object)profile.getLastName()).put("street", (Object)profile.getAddress1()).put("city", (Object)profile.getCity()).put("override", (Object)true).put("zipcode", (Object)profile.getZip()).put("state", (Object)profile.getState()).put("firstName", (Object)profile.getFirstName()))));
        JsonArray jsonArray = new JsonArray().add((Object)jsonObject);
        return new JsonObject().put("items", (Object)jsonArray);
    }

    public HttpRequest ciaUserActivity() {
        HttpRequest httpRequest = this.client.getAbs("https://www.bestbuy.com/api/csiservice/v2/key/cia-user-activity").as(BodyCodec.buffer());
        httpRequest.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"96\", \"Google Chrome\";v=\"96\"");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.bestbuy.com/identity/signin?token=" + this.login.initData.getString("token"));
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest signupRedirectFetch() {
        HttpRequest httpRequest = this.client.getAbs("https://www.bestbuy.com/identity/global/createAccount").as(BodyCodec.buffer());
        httpRequest.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"96\", \"Google Chrome\";v=\"96\"");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("referer", "https://www.bestbuy.com/");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest homepage() {
        HttpRequest httpRequest = this.client.getAbs("https://www.bestbuy.com/").as(BodyCodec.buffer());
        httpRequest.putHeader("cache-control", "max-age=0");
        httpRequest.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"96\", \"Google Chrome\";v=\"96\"");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "none");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest refreshPayment() {
        HttpRequest httpRequest = this.client.postAbs("https://www.bestbuy.com/checkout/orders/" + this.id + "/paymentMethods/refreshPayment").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"93\", \" Not;A Brand\";v=\"99\", \"Chromium\";v=\"93\"");
        httpRequest.putHeader("x-user-interface", "DotCom-Optimized");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.userAgent);
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
        httpRequest.putHeader("x-native-checkout-version", "__VERSION__");
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("origin", "https://www.bestbuy.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.bestbuy.com/checkout/r/payment");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public JsonObject deliveryJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("selected", (Object)"SHIPPING");
        return jsonObject;
    }

    public BestbuyAPI(Task task) {
        this.task = task;
        this.hawkAPI = new HawkAPI();
        this.SKU = this.task.getKeywords()[0].split("skuId=")[1].split("&")[0];
        this.isInstore = task.getMode().contains("store");
    }

    public JsonObject storeIdForm() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("zipCode", (Object)this.task.getProfile().getZip());
        jsonObject.put("showOnShelf", (Object)true);
        jsonObject.put("lookupInStoreQuantity", (Object)false);
        jsonObject.put("xboxAllAccess", (Object)false);
        jsonObject.put("consolidated", (Object)false);
        jsonObject.put("showOnlyOnShelf", (Object)false);
        jsonObject.put("showInStore", (Object)false);
        jsonObject.put("onlyBestBuyLocations", (Object)true);
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.put("sku", (Object)this.SKU);
        jsonObject2.put("condition", null);
        jsonObject2.put("quantity", (Object)1);
        jsonObject2.put("selectedServices", (Object)new JsonArray());
        jsonObject2.put("requiredAccessories", (Object)new JsonArray());
        jsonObject2.put("isTradeIn", (Object)false);
        jsonObject2.put("isLeased", (Object)false);
        jsonObject.put("items", (Object)new JsonArray().add((Object)jsonObject2));
        return jsonObject;
    }

    public JsonObject pickVerificationJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("token", (Object)this.login.initData.getString("token"));
        jsonObject.put("recoveryOptionType", (Object)"email");
        jsonObject.put("email", (Object)this.task.getProfile().getEmail());
        jsonObject.put("smsDigits", (Object)"");
        jsonObject.put("isResetFlow", (Object)false);
        jsonObject.put("challengeType", (Object)this.login.challengeType);
        jsonObject.put("flowOptions", (Object)this.login.flowOptions);
        return jsonObject;
    }
}

