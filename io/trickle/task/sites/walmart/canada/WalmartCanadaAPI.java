/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.buffer.Buffer
 *  io.vertx.core.json.JsonArray
 *  io.vertx.core.json.JsonObject
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.codec.BodyCodec
 */
package io.trickle.task.sites.walmart.canada;

import io.trickle.profile.Profile;
import io.trickle.task.Task;
import io.trickle.task.antibot.impl.px.PXToken;
import io.trickle.task.sites.walmart.util.PaymentToken;
import io.trickle.util.Utils;
import io.trickle.util.request.Headers$Pseudo;
import io.trickle.webclient.ClientType;
import io.trickle.webclient.TaskApiClient;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.codec.BodyCodec;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class WalmartCanadaAPI
extends TaskApiClient {
    public static String BYPASS_PREFIX = "";
    public PXToken pxToken = null;
    public static String DID;
    public static String PX_TOKEN;
    public static int EXCEPTION_RETRY_DELAY;
    public Task task;

    public Buffer submitEmailForm() {
        return new JsonObject().put("emailAddress", (Object)this.task.getProfile().getEmail()).toBuffer();
    }

    public HttpRequest submitCard() {
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.ca/api/checkout-page/payments/summary").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("pragma", "no-cache");
        httpRequest.putHeader("cache-control", "no-cache");
        httpRequest.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"89\", \"Chromium\";v=\"89\", \";Not A Brand\";v=\"99\"");
        httpRequest.putHeader("accept", "application/json");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.128 Safari/537.36");
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("origin", "https://www.walmart.ca");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.walmart.ca/checkout");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-GB,en;q=0.9,en-US;q=0.8,lt;q=0.7");
        httpRequest.putHeader("cookie", "DEFAULT_VALUE");
        httpRequest.putHeader("x-px-authorization", this.tokenValue());
        return httpRequest;
    }

    public Buffer addressForm() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("fulfillmentType", (Object)"SHIPTOHOME");
        Profile profile = this.task.getProfile();
        return jsonObject.put("deliveryInfo", (Object)new JsonObject().put("firstName", (Object)profile.getFirstName()).put("lastName", (Object)profile.getLastName()).put("addressLine1", (Object)profile.getAddress1()).put("addressLine2", (Object)profile.getAddress2()).put("city", (Object)profile.getCity()).put("state", (Object)profile.getState()).put("postalCode", (Object)profile.getZip()).put("phone", (Object)profile.getPhone()).put("saveToProfile", (Object)true).put("verificationLevel", (Object)"VERIFIED").put("country", (Object)"CA").put("locationId", null).put("overrideAddressVerification", (Object)true)).toBuffer();
    }

    public Buffer shippingForm() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("offerId", (Object)this.task.getKeywords()[0]);
        jsonObject.put("levelOfService", (Object)"STANDARD");
        return new JsonObject().put("shipMethods", (Object)new JsonArray().add((Object)jsonObject)).toBuffer();
    }

    public HttpRequest placeOrder() {
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.ca/api/checkout-page/checkout/place-order?lang=en&availStoreId=0&postalCode=0").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("pragma", "no-cache");
        httpRequest.putHeader("cache-control", "no-cache");
        httpRequest.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"89\", \"Chromium\";v=\"89\", \";Not A Brand\";v=\"99\"");
        httpRequest.putHeader("accept", "application/json");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.128 Safari/537.36");
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("origin", "https://www.walmart.ca");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.walmart.ca/checkout");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-GB,en;q=0.9,en-US;q=0.8,lt;q=0.7");
        httpRequest.putHeader("cookie", "DEFAULT_VALUE");
        httpRequest.putHeader("x-px-authorization", this.tokenValue());
        return httpRequest;
    }

    public JsonObject atcForm(String string, int n) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("postalCode", (Object)"X");
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.put("offerId", (Object)string);
        jsonObject2.put("skuId", (Object)string);
        jsonObject2.put("quantity", (Object)n);
        jsonObject2.put("action", (Object)"ADD");
        jsonObject2.put("allowSubstitutions", (Object)false);
        jsonObject.put("items", (Object)new JsonArray().add((Object)jsonObject2));
        return jsonObject;
    }

    public HttpRequest addToCart() {
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.ca/api/bsp/v2/cart").as(BodyCodec.buffer());
        httpRequest.putHeaders(Headers$Pseudo.MSPA.get());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("pragma", "no-cache");
        httpRequest.putHeader("cache-control", "no-cache");
        httpRequest.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"89\", \"Chromium\";v=\"89\", \";Not A Brand\";v=\"99\"");
        httpRequest.putHeader("accept", "application/json");
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.128 Safari/537.36");
        httpRequest.putHeader("wm_qos.correlation_id", "768da70f-027-178f9d0739bb8d,768da70f-027-178f9d0739b5de,768da70f-027-178f9d0739b5de");
        httpRequest.putHeader("origin", "https://www.walmart.ca");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-GB,en;q=0.9,en-US;q=0.8,lt;q=0.7");
        httpRequest.putHeader("cookie", "DEFAULT_VALUE");
        httpRequest.putHeader("x-px-authorization", this.tokenValue());
        return httpRequest;
    }

    public HttpRequest submitShipping() {
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.ca/api/checkout-page/checkout/items/ship-method?lang=en&availStore=0&postalCode=0").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("pragma", "no-cache");
        httpRequest.putHeader("cache-control", "no-cache");
        httpRequest.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"89\", \"Chromium\";v=\"89\", \";Not A Brand\";v=\"99\"");
        httpRequest.putHeader("accept", "application/json");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.128 Safari/537.36");
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("origin", "https://www.walmart.ca");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.walmart.ca/checkout");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-GB,en;q=0.9,en-US;q=0.8,lt;q=0.7");
        httpRequest.putHeader("cookie", "DEFAULT_VALUE");
        httpRequest.putHeader("x-px-authorization", this.tokenValue());
        return httpRequest;
    }

    public HttpRequest submitPayment() {
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.ca/api/checkout-page/checkout/payments?lang=en&availStoreId=0&postalCode=0").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("pragma", "no-cache");
        httpRequest.putHeader("cache-control", "no-cache");
        httpRequest.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"89\", \"Chromium\";v=\"89\", \";Not A Brand\";v=\"99\"");
        httpRequest.putHeader("accept", "application/json");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.128 Safari/537.36");
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("origin", "https://www.walmart.ca");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.walmart.ca/checkout");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-GB,en;q=0.9,en-US;q=0.8,lt;q=0.7");
        httpRequest.putHeader("cookie", "DEFAULT_VALUE");
        httpRequest.putHeader("x-px-authorization", this.tokenValue());
        return httpRequest;
    }

    public Buffer cardForm(PaymentToken paymentToken) {
        JsonObject jsonObject = new JsonObject().put("integrityCheck", (Object)paymentToken.getIntegrityCheck()).put("phase", (Object)paymentToken.getPhase()).put("keyId", (Object)paymentToken.getKeyId());
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.put("pan", (Object)paymentToken.getEncryptedPan());
        jsonObject2.put("cvv", (Object)paymentToken.getEncryptedCvv());
        jsonObject2.put("encryption", (Object)jsonObject);
        JsonObject jsonObject3 = new JsonObject();
        jsonObject3.put("piHash", (Object)jsonObject2);
        jsonObject3.put("cardType", (Object)"CREDIT_CARD");
        jsonObject3.put("pmId", (Object)this.task.getProfile().getCardType());
        jsonObject3.put("cardLast4Digits", (Object)this.task.getProfile().getLastDigits());
        jsonObject3.put("referenceId", (Object)Utils.getRandomString(5));
        return new JsonObject().put("orderTotal", (Object)Double.longBitsToDouble(0x4034000000000000L)).put("paymentMethods", (Object)new JsonArray().add((Object)jsonObject3)).toBuffer();
    }

    public Buffer paymentForm(PaymentToken paymentToken) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("type", (Object)"CREDIT_CARD");
        Profile profile = this.task.getProfile();
        jsonObject.put("cardType", (Object)profile.getCardType());
        jsonObject.put("piBlob", (Object)("{\"payment_details\":{\"pi_hash\":\"" + paymentToken.getPiHash() + "\",\"pm_id\":\"" + profile.getCardType() + "\",\"card\":{\"last_4_digits\":\"" + profile.getLastDigits() + "\",\"type\":\"CREDIT_CARD\",\"exp_month\":\"" + profile.getExpiryMonth() + "\",\"exp_year\":\"" + profile.getExpiryYear() + "\"},\"customer\":{\"address\":{\"address1\":\"" + profile.getAddress1() + "\",\"address2\":\"" + profile.getAddress2() + "\",\"city\":\"" + profile.getCity() + "\",\"country_code\":\"CA\",\"postal_code\":\"" + profile.getZip() + "\",\"state_or_province_code\":\"" + profile.getState() + "\"},\"first_name\":\"" + profile.getFirstName() + "\",\"last_name\":\"" + profile.getLastName() + "\",\"phone\":\"" + profile.getPhone() + "\"},\"save_to_profile\":\"N\"}}"));
        jsonObject.put("cvvRequired", (Object)"Y");
        return jsonObject.toBuffer();
    }

    public WalmartCanadaAPI(Task task) {
        super(ClientType.WALMART_PIXEL_3);
        this.task = task;
    }

    static {
        EXCEPTION_RETRY_DELAY = 12000;
        PX_TOKEN = "3";
        DID = UUID.randomUUID().toString();
    }

    public HttpRequest submitEmail() {
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.ca/api/checkout-page/checkout/email?availStore=0&postalCode=0").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("pragma", "no-cache");
        httpRequest.putHeader("cache-control", "no-cache");
        httpRequest.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"89\", \"Chromium\";v=\"89\", \";Not A Brand\";v=\"99\"");
        httpRequest.putHeader("accept", "application/json");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.128 Safari/537.36");
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("origin", "https://www.walmart.ca");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.walmart.ca/checkout");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-GB,en;q=0.9,en-US;q=0.8,lt;q=0.7");
        httpRequest.putHeader("cookie", "DEFAULT_VALUE");
        httpRequest.putHeader("x-px-authorization", this.tokenValue());
        return httpRequest;
    }

    @Override
    public void close() {
        if (this.pxToken != null) {
            // empty if block
        }
        super.close();
    }

    public JsonObject atcForm() {
        return this.atcForm(this.task.getKeywords()[0], Integer.parseInt(this.task.getSize()));
    }

    public CompletableFuture handleBadResponse(int n) {
        switch (n) {
            case 412: 
            case 444: {
                if (!super.rotateProxy()) return CompletableFuture.completedFuture(true);
                return CompletableFuture.completedFuture(true);
            }
        }
        return CompletableFuture.completedFuture(true);
    }

    public String tokenValue() {
        if (this.pxToken != null) return (String)this.pxToken.getValue();
        return "3";
    }

    public HttpRequest initCheckout() {
        HttpRequest httpRequest = this.client.getAbs("https://www.walmart.ca/api/checkout-page/checkout?lang=en&availStoreId=0&postalCode=0").as(BodyCodec.buffer());
        httpRequest.putHeader("pragma", "no-cache");
        httpRequest.putHeader("cache-control", "no-cache");
        httpRequest.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"89\", \"Chromium\";v=\"89\", \";Not A Brand\";v=\"99\"");
        httpRequest.putHeader("accept", "application/json");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.128 Safari/537.36");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.walmart.ca/checkout");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-GB,en;q=0.9,en-US;q=0.8,lt;q=0.7");
        httpRequest.putHeader("cookie", "DEFAULT_VALUE");
        httpRequest.putHeader("x-px-authorization", this.tokenValue());
        return httpRequest;
    }

    public Buffer placeOrderForm(PaymentToken paymentToken, String string) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("credentialEncrypted", (Object)true);
        jsonObject.put("paymentId", (Object)string);
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.put("cypherTextCvv", (Object)paymentToken.getEncryptedCvv());
        jsonObject2.put("cypherTextPan", (Object)paymentToken.getEncryptedPan());
        jsonObject2.put("integrityCheck", (Object)paymentToken.getIntegrityCheck());
        jsonObject2.put("keyId", (Object)paymentToken.getKeyId());
        jsonObject2.put("phase", (Object)paymentToken.getPhase());
        jsonObject.put("voltageCredential", (Object)jsonObject2);
        JsonArray jsonArray = new JsonArray();
        jsonArray.add((Object)jsonObject);
        return new JsonObject().put("cvv", (Object)jsonArray).toBuffer();
    }

    public HttpRequest submitAddress() {
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.ca/api/checkout-page/checkout/address?lang=en&availStore=0&slotBooked=false").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("pragma", "no-cache");
        httpRequest.putHeader("cache-control", "no-cache");
        httpRequest.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"89\", \"Chromium\";v=\"89\", \";Not A Brand\";v=\"99\"");
        httpRequest.putHeader("accept", "application/json");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.128 Safari/537.36");
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("origin", "https://www.walmart.ca");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.walmart.ca/checkout");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-GB,en;q=0.9,en-US;q=0.8,lt;q=0.7");
        httpRequest.putHeader("cookie", "DEFAULT_VALUE");
        httpRequest.putHeader("x-px-authorization", this.tokenValue());
        return httpRequest;
    }
}

