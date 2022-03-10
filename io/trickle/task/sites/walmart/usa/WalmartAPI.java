/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.Vertx
 *  io.vertx.core.json.JsonArray
 *  io.vertx.core.json.JsonObject
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.client.HttpResponse
 *  io.vertx.ext.web.codec.BodyCodec
 */
package io.trickle.task.sites.walmart.usa;

import io.trickle.account.Account;
import io.trickle.profile.Profile;
import io.trickle.task.Task;
import io.trickle.task.antibot.impl.px.PerimeterX;
import io.trickle.task.sites.walmart.usa.API;
import io.trickle.task.sites.walmart.usa.WalmartConstants;
import io.trickle.task.sites.walmart.util.PaymentToken;
import io.trickle.util.Utils;
import io.trickle.util.request.Headers$Pseudo;
import io.trickle.webclient.ClientType;
import io.trickle.webclient.CookieJar;
import io.trickle.webclient.RealClient;
import io.trickle.webclient.RealClientFactory;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.codec.BodyCodec;
import java.lang.invoke.LambdaMetafactory;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class WalmartAPI
extends API {
    public boolean loggedIn;
    public String dateOfPrevReq;
    public static String BYPASS_PREFIX = "";
    public static String DID;
    public static DateTimeFormatter GMT_CHROME_RFC1123;
    public static int EXCEPTION_RETRY_DELAY;
    public Task task;
    public PerimeterX<String, Boolean> pxAPI = null;
    public String crossSite;
    public boolean ios;
    public String searchQuery;
    public static String PX_TOKEN;

    public HttpRequest getCartMobile(String string) {
        HttpRequest httpRequest = this.client.getAbs(string).timeout(TimeUnit.SECONDS.toMillis(10L)).as(BodyCodec.buffer());
        if (this.ios) {
            httpRequest.putHeaders(Headers$Pseudo.MSPA.get());
            httpRequest.putHeader("accept", "*/*");
            httpRequest.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
            httpRequest.putHeader("did", DID.replace("-", "").concat("00000000"));
            httpRequest.putHeader("mobile-app-version", "21.18.3");
            httpRequest.putHeader("mobile-platform", "ios");
            httpRequest.putHeader("cookie", "DEFAULT_VALUE");
            httpRequest.putHeader("accept-language", "en-us");
            httpRequest.putHeader("user-agent", "Walmart/2107091553 CFNetwork/1128.0.1 Darwin/19.6.0");
            httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
            return httpRequest;
        }
        httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
        httpRequest.putHeader("User-Agent", WalmartConstants.ANDROID_OLD_UA);
        httpRequest.putHeader("did", DID);
        httpRequest.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
        httpRequest.putHeader("Accept-Encoding", "gzip");
        httpRequest.putHeader("Cookie", "DEFAULT_VALUE");
        return httpRequest;
    }

    @Override
    public JsonObject getPaymentForm(PaymentToken paymentToken) {
        JsonObject jsonObject = new JsonObject().put("payments", (Object)new JsonArray());
        JsonObject jsonObject2 = new JsonObject();
        Profile profile = this.task.getProfile();
        jsonObject2.put("addressLineOne", (Object)profile.getAddress1());
        jsonObject2.put("addressLineTwo", (Object)profile.getAddress2());
        jsonObject2.put("cardType", (Object)profile.getPaymentMethod().get());
        jsonObject2.put("city", (Object)profile.getCity());
        jsonObject2.put("encryptedCvv", (Object)paymentToken.getEncryptedCvv());
        jsonObject2.put("encryptedPan", (Object)paymentToken.getEncryptedPan());
        jsonObject2.put("expiryMonth", (Object)profile.getExpiryMonth());
        if (profile.getExpiryYear().length() > 2) {
            jsonObject2.put("expiryYear", (Object)profile.getExpiryYear());
        } else {
            jsonObject2.put("expiryYear", (Object)("20" + profile.getExpiryYear()));
        }
        jsonObject2.put("firstName", (Object)profile.getFirstName());
        jsonObject2.put("integrityCheck", (Object)paymentToken.getIntegrityCheck());
        jsonObject2.put("keyId", (Object)paymentToken.getKeyId());
        jsonObject2.put("lastName", (Object)profile.getLastName());
        jsonObject2.put("paymentType", (Object)"CREDITCARD");
        jsonObject2.put("phase", (Object)paymentToken.getPhase());
        jsonObject2.put("phone", (Object)profile.getPhone());
        jsonObject2.put("piHash", (Object)paymentToken.getPiHash());
        jsonObject2.put("postalCode", (Object)profile.getZip());
        jsonObject2.put("state", (Object)profile.getState().toUpperCase());
        jsonObject2.put("email", (Object)profile.getEmail());
        jsonObject.getJsonArray("payments").add((Object)jsonObject2);
        jsonObject.put("cvvInSession", (Object)true);
        return jsonObject;
    }

    @Override
    public PerimeterX getPxAPI() {
        return this.pxAPI;
    }

    @Override
    public void setAPI(PerimeterX perimeterX) {
        this.pxAPI = perimeterX;
    }

    @Override
    public JsonObject getShippingForm(JsonObject jsonObject) {
        String string = jsonObject.getJsonArray("items").getJsonObject(0).getString("id");
        JsonObject jsonObject2 = new JsonObject().put("changedFields", (Object)new JsonArray());
        Profile profile = this.task.getProfile();
        jsonObject2.put("addressLineOne", (Object)profile.getAddress1());
        jsonObject2.put("addressLineTwo", (Object)profile.getAddress2());
        jsonObject2.put("addressType", (Object)"RESIDENTIAL");
        jsonObject2.put("city", (Object)profile.getCity());
        jsonObject2.put("firstName", (Object)profile.getFirstName());
        jsonObject2.put("lastName", (Object)profile.getLastName());
        jsonObject2.put("phone", (Object)profile.getPhone());
        jsonObject2.put("postalCode", (Object)profile.getZip());
        jsonObject2.put("countryCode", (Object)"USA");
        jsonObject2.put("marketingEmailPref", (Object)false);
        jsonObject2.put("preferenceId", (Object)string);
        jsonObject2.put("state", (Object)profile.getState());
        jsonObject2.put("email", (Object)profile.getEmail());
        return jsonObject2;
    }

    @Override
    public HttpRequest loginAccount() {
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/account/electrode/api/identity/password").as(BodyCodec.buffer());
        if (this.ios) {
            httpRequest.putHeaders(Headers$Pseudo.MSPA.get());
            httpRequest.putHeader("content-type", "text/plain;charset=UTF-8");
            httpRequest.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
            httpRequest.putHeader("x-wm-auth", "null");
            httpRequest.putHeader("accept", "*/*");
            httpRequest.putHeader("x-wm-xpa", "null");
            httpRequest.putHeader("mobile-app-version", "21.18.3");
            httpRequest.putHeader("x-wm-cid", "");
            httpRequest.putHeader("accept-language", "en-us");
            httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
            httpRequest.putHeader("mobile-platform", "ios");
            httpRequest.putHeader("content-length", "DEFAULT_VALUE");
            httpRequest.putHeader("did", DID.replace("-", "").concat("00000000"));
            httpRequest.putHeader("x-wm-spid", "");
            httpRequest.putHeader("user-agent", "Walmart/2107091553 CFNetwork/1128.0.1 Darwin/19.6.0");
            return httpRequest;
        }
        httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
        httpRequest.putHeader("x-wm-auth", "null");
        httpRequest.putHeader("x-wm-cid", "null");
        httpRequest.putHeader("x-wm-spid", "null");
        httpRequest.putHeader("x-wm-xpa", "null");
        httpRequest.putHeader("user-agent", WalmartConstants.ANDROID_OLD_UA);
        httpRequest.putHeader("did", DID);
        httpRequest.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
        httpRequest.putHeader("content-type", "text/plain;charset=UTF-8");
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("accept-encoding", "gzip");
        return httpRequest;
    }

    @Override
    public HttpRequest submitShipping() {
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/api/checkout/v3/contract/:PCID/shipping-address").as(BodyCodec.buffer());
        if (this.ios) {
            httpRequest.putHeaders(Headers$Pseudo.MSPA.get());
            httpRequest.putHeader("content-type", "application/json");
            httpRequest.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
            httpRequest.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
            httpRequest.putHeader("mobile-app-version", "21.18.3");
            httpRequest.putHeader("accept-language", "en-us");
            httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
            httpRequest.putHeader("mobile-platform", "ios");
            httpRequest.putHeader("content-length", "DEFAULT_VALUE");
            httpRequest.putHeader("did", DID.replace("-", "").concat("00000000"));
            httpRequest.putHeader("user-agent", "Walmart/2107091553 Walmart WMTAPP v21.18.3");
            httpRequest.putHeader("cookie", "DEFAULT_VALUE");
            httpRequest.putHeader("x-px-ab", "2");
            return httpRequest;
        }
        httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
        httpRequest.putHeader("Accept", "application/json, text/javascript, */*; q=0.01");
        httpRequest.putHeader("wm_vertical_id", "0");
        httpRequest.putHeader("inkiru_precedence", "false");
        httpRequest.putHeader("wm_cvv_in_session", "true");
        httpRequest.putHeader("x-px-ab", "2");
        httpRequest.putHeader("User-Agent", WalmartConstants.ANDROID_OLD_WEB_UA);
        httpRequest.putHeader("did", DID);
        httpRequest.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
        httpRequest.putHeader("Content-Type", "application/json");
        httpRequest.putHeader("Content-Length", "DEFAULT_VALUE");
        httpRequest.putHeader("Accept-Encoding", "gzip");
        httpRequest.putHeader("Cookie", "DEFAULT_VALUE");
        return httpRequest;
    }

    @Override
    public JsonObject getShippingRateForm(JsonObject jsonObject) {
        String string = jsonObject.getJsonArray("items").getJsonObject(0).getString("id");
        JsonArray jsonArray = jsonObject.getJsonArray("items");
        String string2 = jsonArray.getJsonObject(0).getJsonObject("fulfillmentSelection").getString("shipMethod");
        JsonObject jsonObject2 = new JsonObject().put("fulfillmentOption", (Object)"S2H").put("itemIds", (Object)new JsonArray().add((Object)string)).put("shipMethod", (Object)string2);
        JsonArray jsonArray2 = new JsonArray().add((Object)jsonObject2);
        return new JsonObject().put("groups", (Object)jsonArray2);
    }

    public HttpRequest addToCart(String string) {
        HttpRequest httpRequest = this.client.postAbs(string).as(BodyCodec.buffer());
        if (this.ios) {
            httpRequest.putHeaders(Headers$Pseudo.MSPA.get());
            httpRequest.putHeader("content-type", "application/json");
            httpRequest.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
            httpRequest.putHeader("accept", "*/*");
            httpRequest.putHeader("mobile-app-version", "21.18.3");
            httpRequest.putHeader("accept-language", "en-us");
            httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
            httpRequest.putHeader("mobile-platform", "ios");
            httpRequest.putHeader("content-length", "DEFAULT_VALUE");
            httpRequest.putHeader("did", DID.replace("-", "").concat("00000000"));
            httpRequest.putHeader("user-agent", "Walmart/2107091553 CFNetwork/1128.0.1 Darwin/19.6.0");
            httpRequest.putHeader("cookie", "DEFAULT_VALUE");
            return httpRequest;
        }
        httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
        httpRequest.putHeader("user-agent", WalmartConstants.ANDROID_OLD_UA);
        httpRequest.putHeader("did", DID);
        httpRequest.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
        httpRequest.putHeader("content-type", "application/json; charset=utf-8");
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("accept-encoding", "gzip");
        httpRequest.putHeader("cookie", "DEFAULT_VALUE");
        return httpRequest;
    }

    static {
        PX_TOKEN = "3";
        EXCEPTION_RETRY_DELAY = 12000;
        DID = UUID.randomUUID().toString();
        GMT_CHROME_RFC1123 = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH);
    }

    public HttpRequest getCartV2(String string) {
        return this.getCartMobile("https://www.walmart.com/api/v2/cart/" + string);
    }

    public HttpRequest presoSearch() {
        HttpRequest httpRequest = this.client.getAbs("https://www.walmart.com/preso/search?prg=mWeb&wpm=0&assetProtocol=secure&query=panini&page=1&spelling=true&preciseSearch=true&vertical_whitelist=home%2Cfanatics%2Cfashion&pref_store=5880%2C2015%2C5936%2C5969%2C5227&zipcode=22124&applyDealsRedirect=true").timeout(TimeUnit.SECONDS.toMillis(10L)).as(BodyCodec.none());
        if (this.ios) {
            httpRequest.putHeaders(Headers$Pseudo.MSPA.get());
            httpRequest.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
            httpRequest.putHeader("accept", "*/*");
            httpRequest.putHeader("mobile-app-version", "21.18.3");
            httpRequest.putHeader("wm_site_mode", "0");
            httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
            httpRequest.putHeader("accept-language", "en-us");
            httpRequest.putHeader("access_key", "532c28d5412dd75bf975fb951c740a30");
            httpRequest.putHeader("mobile-platform", "ios");
            httpRequest.putHeader("cid", "");
            httpRequest.putHeader("did", DID.replace("-", "").concat("00000000"));
            httpRequest.putHeader("user-agent", "Walmart/2107091553 CFNetwork/1128.0.1 Darwin/19.6.0");
            httpRequest.putHeader("cookie", "DEFAULT_VALUE");
            return httpRequest;
        }
        httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
        httpRequest.putHeader("access_key", "532c28d5412dd75bf975fb951c740a30");
        httpRequest.putHeader("cid", "");
        httpRequest.putHeader("client-ip", "192.168.0." + ThreadLocalRandom.current().nextInt(1, 255));
        httpRequest.putHeader("wm_site_mode", "0");
        httpRequest.putHeader("User-Agent", WalmartConstants.ANDROID_OLD_UA);
        httpRequest.putHeader("did", DID);
        httpRequest.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
        httpRequest.putHeader("Accept-Encoding", "gzip");
        httpRequest.putHeader("Cookie", "DEFAULT_VALUE");
        return httpRequest;
    }

    @Override
    public void setLoggedIn(boolean bl) {
        this.loggedIn = bl;
    }

    public HttpRequest mobileAtcAffiliate() {
        return this.atcAffiliate("https://affil.walmart.com/cart/addToCart?items=" + this.task.getKeywords()[0] + "|" + this.task.getSize());
    }

    public HttpRequest otherAtc() {
        return this.addToCart("https://www.walmart.com/api/v2/cart/guest/:CID/items");
    }

    @Override
    public void close() {
        if (this.pxAPI != null) {
            this.pxAPI.close();
        }
        super.close();
    }

    public HttpRequest login() {
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/account/electrode/api/identity/password").as(BodyCodec.buffer());
        httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
        httpRequest.putHeader("x-wm-auth", "null");
        httpRequest.putHeader("x-wm-cid", "null");
        httpRequest.putHeader("x-wm-spid", "null");
        httpRequest.putHeader("x-wm-xpa", "GNsCQ|PPWSy");
        httpRequest.putHeader("user-agent", WalmartConstants.ANDROID_OLD_UA);
        httpRequest.putHeader("did", DID);
        httpRequest.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
        httpRequest.putHeader("content-type", "text/plain;charset=UTF-8");
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("accept-encoding", "gzip");
        return httpRequest;
    }

    public HttpRequest terraFirmaDesktop(String string) {
        HttpRequest httpRequest = this.client.getAbs("https://www.walmart.com/terra-firma/item/" + string).as(BodyCodec.buffer());
        httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
        httpRequest.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"89\", \"Chromium\";v=\"89\", \";Not A Brand\";v=\"99\"");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.114 Safari/537.36");
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("service-worker-navigation-preload", "true");
        httpRequest.putHeader("sec-fetch-site", "none");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        httpRequest.putHeader("cookie", "DEFAULT_VALUE");
        return httpRequest;
    }

    public HttpRequest atcAffiliate() {
        if (!ThreadLocalRandom.current().nextBoolean()) return this.desktopAtcAffiliate();
        return this.mobileAtcAffiliate();
    }

    public HttpRequest putCart() {
        return this.putCartAndroid("476e4599-389d-458a-94dd-c18c583686d0");
    }

    @Override
    public HttpRequest transferCart(String string) {
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/api//saved/:CID/items/" + string + "/transfer").as(BodyCodec.buffer());
        if (this.ios) {
            httpRequest.putHeaders(Headers$Pseudo.MSPA.get());
            httpRequest.putHeader("content-type", "application/json");
            httpRequest.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
            httpRequest.putHeader("accept", "*/*");
            httpRequest.putHeader("mobile-app-version", "21.18.3");
            httpRequest.putHeader("accept-language", "en-us");
            httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
            httpRequest.putHeader("mobile-platform", "ios");
            httpRequest.putHeader("content-length", "DEFAULT_VALUE");
            httpRequest.putHeader("did", DID.replace("-", "").concat("00000000"));
            httpRequest.putHeader("credentials", "include");
            httpRequest.putHeader("omitcsrfjwt", "true");
            httpRequest.putHeader("user-agent", "Walmart/2107091553 CFNetwork/1128.0.1 Darwin/19.6.0");
            httpRequest.putHeader("cookie", "DEFAULT_VALUE");
            return httpRequest;
        }
        httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
        httpRequest.putHeader("user-agent", WalmartConstants.ANDROID_OLD_UA);
        httpRequest.putHeader("did", DID);
        httpRequest.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
        httpRequest.putHeader("content-type", "application/json; charset=utf-8");
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("credentials", "include");
        httpRequest.putHeader("omitcsrfjwt", "true");
        httpRequest.putHeader("accept-encoding", "gzip");
        httpRequest.putHeader("cookie", "DEFAULT_VALUE");
        return httpRequest;
    }

    @Override
    public JsonObject accountCreateForm() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("email", (Object)(this.task.getProfile().getEmail().split("@")[0] + "+" + ThreadLocalRandom.current().nextInt(999999) + "@" + this.task.getProfile().getEmail().split("@")[1]));
        jsonObject.put("emailAccepted", (Object)false);
        jsonObject.put("firstName", (Object)this.task.getProfile().getFirstName());
        jsonObject.put("lastName", (Object)this.task.getProfile().getLastName());
        jsonObject.put("password", (Object)Utils.generateStrongString());
        return jsonObject;
    }

    @Override
    public void swapClient() {
        try {
            RealClient realClient = RealClientFactory.fromOther(Vertx.currentContext().owner(), this.client, this.client.type());
            this.client.close();
            this.client = realClient;
            return;
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    @Override
    public HttpRequest getPCID(PaymentToken paymentToken) {
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/api/checkout/v3/contract").as(BodyCodec.buffer());
        if (this.ios) {
            httpRequest.putHeaders(Headers$Pseudo.MSPA.get());
            httpRequest.putHeader("content-type", "application/json");
            httpRequest.putHeader("x-px-ab", "2");
            httpRequest.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
            httpRequest.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
            httpRequest.putHeader("wm_vertical_id", "0");
            httpRequest.putHeader("mobile-app-version", "21.18.3");
            httpRequest.putHeader("vid", paymentToken.getVid().toUpperCase());
            httpRequest.putHeader("wm_cvv_in_session", "true");
            httpRequest.putHeader("accept-language", "en-us");
            httpRequest.putHeader("inkiru_precedence", "false");
            httpRequest.putHeader("sid", paymentToken.getSid().toUpperCase());
            httpRequest.putHeader("mobile-platform", "ios");
            httpRequest.putHeader("did", DID.replace("-", "").concat("00000000"));
            httpRequest.putHeader("user-agent", "Walmart/2107091553 Walmart WMTAPP v21.18.3");
            httpRequest.putHeader("content-length", "DEFAULT_VALUE");
            httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
            httpRequest.putHeader("cookie", "DEFAULT_VALUE");
            return httpRequest;
        }
        httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
        httpRequest.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
        httpRequest.putHeader("wm_vertical_id", "0");
        httpRequest.putHeader("inkiru_precedence", "false");
        httpRequest.putHeader("wm_cvv_in_session", "true");
        httpRequest.putHeader("x-px-ab", "2");
        httpRequest.putHeader("sid", paymentToken.getSid());
        httpRequest.putHeader("vid", paymentToken.getVid());
        httpRequest.putHeader("user-agent", WalmartConstants.ANDROID_OLD_WEB_UA);
        httpRequest.putHeader("did", DID);
        httpRequest.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("accept-encoding", "gzip");
        httpRequest.putHeader("cookie", "DEFAULT_VALUE");
        return httpRequest;
    }

    public WalmartAPI(Task task) {
        super(ClientType.WALMART_PIXEL_3);
        this.task = task;
        this.ios = this.task.getMode().contains("desktop") ? false : task.getMode().contains("2");
        this.crossSite = crossSiteList[ThreadLocalRandom.current().nextInt(crossSiteList.length)];
        this.searchQuery = searchQueries[ThreadLocalRandom.current().nextInt(searchQueries.length)];
        this.loggedIn = false;
    }

    public HttpRequest terraFirma(boolean bl) {
        if (!this.task.getMode().toLowerCase().contains("desktop")) return this.terraFirma("152481472", bl);
        return this.terraFirmaDesktop("152481472");
    }

    public HttpRequest getLocation() {
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/account/api/location?skipCache=true").timeout(TimeUnit.SECONDS.toMillis(10L)).as(BodyCodec.none());
        if (!this.ios) {
            httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
            httpRequest.putHeader("User-Agent", WalmartConstants.ANDROID_OLD_UA);
            httpRequest.putHeader("did", DID);
            httpRequest.putHeader("Content-Type", "application/json; charset=utf-8");
            httpRequest.putHeader("Content-Length", "DEFAULT_VALUE");
            httpRequest.putHeader("Accept-Encoding", "gzip");
            httpRequest.putHeader("Cookie", "DEFAULT_VALUE");
            return httpRequest;
        }
        httpRequest.putHeaders(Headers$Pseudo.MSPA.get());
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("app-source-event", "");
        httpRequest.putHeader("x-o-platform", "ios");
        httpRequest.putHeader("mobile-app-version", "21.18.3");
        httpRequest.putHeader("accept-language", "en-us");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("x-o-platform-version", "21.18.3");
        httpRequest.putHeader("mobile-platform", "ios");
        httpRequest.putHeader("did", DID.replace("-", "").concat("00000000"));
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("user-agent", "Walmart/2107091553 CFNetwork/1128.0.1 Darwin/19.6.0");
        httpRequest.putHeader("Cookie", "DEFAULT_VALUE");
        return httpRequest;
    }

    public HttpRequest putLocation() {
        HttpRequest httpRequest = this.client.putAbs("https://www.walmart.com/account/api/location?skipCache=true").timeout(TimeUnit.SECONDS.toMillis(10L)).as(BodyCodec.none());
        httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
        httpRequest.putHeader("User-Agent", WalmartConstants.ANDROID_OLD_UA);
        httpRequest.putHeader("did", DID);
        httpRequest.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
        httpRequest.putHeader("Content-Type", "application/json; charset=utf-8");
        httpRequest.putHeader("Content-Length", "DEFAULT_VALUE");
        httpRequest.putHeader("Accept-Encoding", "gzip");
        httpRequest.putHeader("Cookie", "DEFAULT_VALUE");
        return httpRequest;
    }

    @Override
    public HttpRequest savedCart() {
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/api/v3/saved/:CRT/items").as(BodyCodec.buffer());
        if (this.ios) {
            httpRequest.putHeaders(Headers$Pseudo.MSPA.get());
            httpRequest.putHeader("content-type", "application/json");
            httpRequest.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
            httpRequest.putHeader("accept", "*/*");
            httpRequest.putHeader("mobile-app-version", "21.18.3");
            httpRequest.putHeader("accept-language", "en-us");
            httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
            httpRequest.putHeader("mobile-platform", "ios");
            httpRequest.putHeader("content-length", "DEFAULT_VALUE");
            httpRequest.putHeader("did", DID.replace("-", "").concat("00000000"));
            httpRequest.putHeader("credentials", "include");
            httpRequest.putHeader("omitcsrfjwt", "true");
            httpRequest.putHeader("user-agent", "Walmart/2107091553 CFNetwork/1128.0.1 Darwin/19.6.0");
            httpRequest.putHeader("cookie", "DEFAULT_VALUE");
            return httpRequest;
        }
        httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
        httpRequest.putHeader("user-agent", WalmartConstants.ANDROID_OLD_UA);
        httpRequest.putHeader("did", DID);
        httpRequest.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
        httpRequest.putHeader("content-type", "application/json; charset=utf-8");
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("credentials", "include");
        httpRequest.putHeader("omitcsrfjwt", "true");
        httpRequest.putHeader("accept-encoding", "gzip");
        httpRequest.putHeader("cookie", "DEFAULT_VALUE");
        return httpRequest;
    }

    @Override
    public JsonObject getBillingForm(PaymentToken paymentToken) {
        JsonObject jsonObject = new JsonObject();
        Profile profile = this.task.getProfile();
        jsonObject.put("addressLineOne", (Object)profile.getAddress1());
        jsonObject.put("addressLineTwo", (Object)profile.getAddress2());
        jsonObject.put("cardType", (Object)profile.getPaymentMethod().get());
        jsonObject.put("city", (Object)profile.getCity());
        jsonObject.put("encryptedCvv", (Object)paymentToken.getEncryptedCvv());
        jsonObject.put("encryptedPan", (Object)paymentToken.getEncryptedPan());
        jsonObject.put("expiryMonth", (Object)profile.getExpiryMonth());
        jsonObject.put("expiryYear", (Object)profile.getExpiryYear());
        jsonObject.put("firstName", (Object)profile.getFirstName());
        jsonObject.put("integrityCheck", (Object)paymentToken.getIntegrityCheck());
        jsonObject.put("keyId", (Object)paymentToken.getKeyId());
        jsonObject.put("lastName", (Object)profile.getLastName());
        jsonObject.put("phase", (Object)paymentToken.getPhase());
        jsonObject.put("phone", (Object)profile.getPhone());
        jsonObject.put("postalCode", (Object)profile.getZip());
        jsonObject.put("state", (Object)profile.getState().toUpperCase());
        jsonObject.put("isGuest", (Object)true);
        return jsonObject;
    }

    @Override
    public JsonObject getProcessingForm(PaymentToken paymentToken) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("paymentType", (Object)"CREDITCARD");
        jsonObject.put("encryptedCvv", (Object)paymentToken.getEncryptedCvv());
        jsonObject.put("encryptedPan", (Object)paymentToken.getEncryptedPan());
        jsonObject.put("integrityCheck", (Object)paymentToken.getIntegrityCheck());
        jsonObject.put("keyId", (Object)paymentToken.getKeyId());
        jsonObject.put("phase", (Object)paymentToken.getPhase());
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.put("cvvInSession", (Object)true);
        jsonObject2.put("voltagePayments", (Object)new JsonArray().add((Object)jsonObject));
        return jsonObject2;
    }

    @Override
    public HttpRequest getCheckoutPage() {
        HttpRequest httpRequest = this.client.getAbs("https://www.walmart.com/checkout/").timeout(TimeUnit.SECONDS.toMillis(10L)).as(BodyCodec.none());
        if (this.ios) {
            httpRequest.putHeaders(Headers$Pseudo.MSPA.get());
            httpRequest.putHeader("accept", "*/*");
            httpRequest.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
            httpRequest.putHeader("did", DID.replace("-", "").concat("00000000"));
            httpRequest.putHeader("mobile-app-version", "21.18.3");
            httpRequest.putHeader("mobile-platform", "ios");
            httpRequest.putHeader("cookie", "DEFAULT_VALUE");
            httpRequest.putHeader("accept-language", "en-us");
            httpRequest.putHeader("user-agent", "Walmart/2107091553 CFNetwork/1128.0.1 Darwin/19.6.0");
            httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
            return httpRequest;
        }
        httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
        httpRequest.putHeader("User-Agent", WalmartConstants.ANDROID_OLD_UA);
        httpRequest.putHeader("did", DID);
        httpRequest.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
        httpRequest.putHeader("Accept-Encoding", "gzip");
        httpRequest.putHeader("Cookie", "DEFAULT_VALUE");
        return httpRequest;
    }

    @Override
    public CompletableFuture handleBadResponse(int n, HttpResponse httpResponse) {
        switch (n) {
            case 412: {
                try {
                    if (!this.task.getMode().contains("skip")) {
                        JsonObject jsonObject = httpResponse.bodyAsJsonObject();
                        String string = jsonObject.getString("uuid");
                        String string2 = this.pxAPI.getVid();
                        return this.pxAPI.solveCaptcha(string2, string, "https://www.perimeterx.com/");
                    }
                    CompletableFuture completableFuture = this.generatePX(false);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture2 = completableFuture;
                        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartAPI.async$handleBadResponse(this, n, httpResponse, completableFuture2, 1, arg_0));
                    }
                    completableFuture.join();
                    return CompletableFuture.completedFuture(false);
                }
                catch (Throwable throwable) {
                    return this.pxAPI.solveCaptcha(this.pxAPI.getVid(), null, "https://www.perimeterx.com/");
                }
            }
            case 444: {
                if (!super.rotateProxy()) return CompletableFuture.completedFuture(true);
                this.pxAPI.restartClient(this.client);
                return CompletableFuture.completedFuture(true);
            }
        }
        return CompletableFuture.completedFuture(true);
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$handleBadResponse(WalmartAPI var0, int var1_1, HttpResponse var2_2, CompletableFuture var3_3, int var4_5, Object var5_7) {
        switch (var4_5) {
            case 0: {
                switch (var1_1) {
                    case 412: {
                        try {
                            if (!var0.task.getMode().contains("skip")) {
                                var3_3 = var2_2.bodyAsJsonObject();
                                var4_6 = var3_3.getString("uuid");
                                var5_7 = var0.pxAPI.getVid();
                                return var0.pxAPI.solveCaptcha((String)var5_7, var4_6, "https://www.perimeterx.com/");
                            }
                            v0 = var0.generatePX(false);
                            if (!v0.isDone()) {
                                var6_8 = v0;
                                return var6_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$handleBadResponse(io.trickle.task.sites.walmart.usa.WalmartAPI int io.vertx.ext.web.client.HttpResponse java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((WalmartAPI)var0, (int)var1_1, (HttpResponse)var2_2, (CompletableFuture)var6_8, (int)1));
                            }
                            ** GOTO lbl26
                        }
                        catch (Throwable var3_4) {
                            return var0.pxAPI.solveCaptcha(var0.pxAPI.getVid(), null, "https://www.perimeterx.com/");
                        }
                    }
                    case 444: {
                        if (super.rotateProxy() == false) return CompletableFuture.completedFuture(true);
                        var0.pxAPI.restartClient(var0.client);
                        return CompletableFuture.completedFuture(true);
                    }
                    default: {
                        return CompletableFuture.completedFuture(true);
                    }
                }
            }
            case 1: {
                v0 = var3_3;
lbl26:
                // 2 sources

                v0.join();
                return CompletableFuture.completedFuture(false);
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public HttpRequest affilCrossSite(String string) {
        this.getCookies().removeAnyMatch("CRT");
        this.getCookies().removeAnyMatch("cart-item-count");
        this.getCookies().removeAnyMatch("hasCRT");
        HttpRequest httpRequest = this.client.getAbs("https://affil.walmart.com/cart/" + string + "?items=" + this.task.getKeywords()[0] + "|" + this.task.getSize()).as(BodyCodec.none());
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", WalmartConstants.ANDROID_OLD_UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        if (this.crossSite != null) {
            httpRequest.putHeader("referer", "https://" + this.crossSite + "/");
            if (this.crossSite.equals("t.co")) {
                httpRequest.headers().remove("sec-fetch-user");
            }
        }
        httpRequest.putHeader("accept-encoding", "gzip");
        httpRequest.putHeader("cookie", "DEFAULT_VALUE");
        if (this.dateOfPrevReq != null) {
            httpRequest.putHeader("if-modified-since", this.dateOfPrevReq);
        }
        this.dateOfPrevReq = GMT_CHROME_RFC1123.format(ZonedDateTime.now(ZoneOffset.UTC));
        return httpRequest;
    }

    @Override
    public HttpRequest selectShipping() {
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/api/checkout/v3/contract/:PCID/fulfillment").as(BodyCodec.buffer());
        if (this.ios) {
            httpRequest.putHeaders(Headers$Pseudo.MSPA.get());
            httpRequest.putHeader("content-type", "application/json");
            httpRequest.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
            httpRequest.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
            httpRequest.putHeader("wm_vertical_id", "0");
            httpRequest.putHeader("mobile-app-version", "21.18.3");
            httpRequest.putHeader("wm_cvv_in_session", "true");
            httpRequest.putHeader("accept-language", "en-us");
            httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
            httpRequest.putHeader("inkiru_precedence", "false");
            httpRequest.putHeader("mobile-platform", "ios");
            httpRequest.putHeader("content-length", "DEFAULT_VALUE");
            httpRequest.putHeader("did", DID.replace("-", "").concat("00000000"));
            httpRequest.putHeader("user-agent", "Walmart/2107091553 Walmart WMTAPP v21.18.3");
            httpRequest.putHeader("cookie", "DEFAULT_VALUE");
            httpRequest.putHeader("x-px-ab", "2");
            return httpRequest;
        }
        httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
        httpRequest.putHeader("Accept", "application/json, text/javascript, */*; q=0.01");
        httpRequest.putHeader("wm_vertical_id", "0");
        httpRequest.putHeader("inkiru_precedence", "false");
        httpRequest.putHeader("wm_cvv_in_session", "true");
        httpRequest.putHeader("x-px-ab", "2");
        httpRequest.putHeader("User-Agent", WalmartConstants.ANDROID_OLD_WEB_UA);
        httpRequest.putHeader("did", DID);
        httpRequest.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
        httpRequest.putHeader("Content-Type", "application/json");
        httpRequest.putHeader("Content-Length", "DEFAULT_VALUE");
        httpRequest.putHeader("Accept-Encoding", "gzip");
        httpRequest.putHeader("Cookie", "DEFAULT_VALUE");
        return httpRequest;
    }

    @Override
    public HttpRequest homepage() {
        HttpRequest httpRequest = this.client.getAbs("https://www.walmart.com/").as(BodyCodec.buffer());
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", WalmartConstants.ANDROID_OLD_WEB_UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("referer", "https://www.google.com/");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-us");
        return httpRequest;
    }

    public HttpRequest desktopAtcAffiliate() {
        return this.atcAffiliate("https://affil.walmart.com/cart/buynow?items=" + this.task.getKeywords()[0] + "|" + this.task.getSize());
    }

    @Override
    public HttpRequest terraFirma(String string, boolean bl) {
        String string2 = "https://www.walmart.com/terra-firma/graphql?v=2&options=timing%2Cnonnull%2Cerrors%2Ccontext&id=FullProductHolidaysRoute-" + (this.ios ? "ios" : "android");
        if (bl) {
            string2 = "https://www.walmart.com/terra-firma/graphql?options=timing,nonnull,context&v=2&id=FullProductRoute-" + (this.ios ? "ios" : "android");
        }
        HttpRequest httpRequest = this.client.postAbs(string2).as(BodyCodec.buffer());
        if (this.ios) {
            httpRequest.putHeaders(Headers$Pseudo.MSPA.get());
            httpRequest.putHeader("content-type", "application/json");
            httpRequest.putHeader("wm_ul_plus", "0");
            httpRequest.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
            httpRequest.putHeader("wm_consumer.id", UUID.randomUUID().toString());
            httpRequest.putHeader("accept", "*/*");
            httpRequest.putHeader("mobile-app-version", "21.18.3");
            httpRequest.putHeader("wm_site_mode", "0");
            httpRequest.putHeader("accept-language", "en-us");
            httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
            httpRequest.putHeader("mobile-platform", "ios");
            httpRequest.putHeader("content-length", "DEFAULT_VALUE");
            httpRequest.putHeader("itemid", string);
            httpRequest.putHeader("did", DID.replace("-", "").concat("00000000"));
            httpRequest.putHeader("user-agent", "Walmart/2107091553 CFNetwork/1128.0.1 Darwin/19.6.0");
            httpRequest.putHeader("cookie", "DEFAULT_VALUE");
            return httpRequest;
        }
        httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
        httpRequest.putHeader("wm_ul_plus", "0");
        httpRequest.putHeader("wm_consumer.id", UUID.randomUUID().toString());
        httpRequest.putHeader("wm_site_mode", "0");
        httpRequest.putHeader("itemid", string);
        httpRequest.putHeader("user-agent", WalmartConstants.ANDROID_OLD_UA);
        httpRequest.putHeader("did", DID);
        httpRequest.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
        httpRequest.putHeader("content-type", "application/json; charset=utf-8");
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("accept-encoding", "gzip");
        httpRequest.putHeader("cookie", "DEFAULT_VALUE");
        return httpRequest;
    }

    @Override
    public Task getTask() {
        return this.task;
    }

    public JsonObject loginForm() {
        try {
            Account account = WalmartAPI.rotateAccount();
            if (account == null) {
                return null;
            }
            JsonObject jsonObject = new JsonObject();
            jsonObject.put("email", (Object)account.getUser());
            jsonObject.put("password", (Object)account.getPass());
            return jsonObject;
        }
        catch (Throwable throwable) {
            return null;
        }
    }

    @Override
    public HttpRequest submitBilling() {
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com//api/checkout-customer/:CID/credit-card").as(BodyCodec.buffer());
        if (this.ios) {
            httpRequest.putHeaders(Headers$Pseudo.MSPA.get());
            httpRequest.putHeader("content-type", "application/json");
            httpRequest.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
            httpRequest.putHeader("accept", "application/json");
            httpRequest.putHeader("mobile-app-version", "21.18.3");
            httpRequest.putHeader("accept-language", "en-us");
            httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
            httpRequest.putHeader("mobile-platform", "ios");
            httpRequest.putHeader("content-length", "DEFAULT_VALUE");
            httpRequest.putHeader("did", DID.replace("-", "").concat("00000000"));
            httpRequest.putHeader("user-agent", "Walmart/2107091553 Walmart WMTAPP v21.18.3");
            httpRequest.putHeader("cookie", "DEFAULT_VALUE");
            httpRequest.putHeader("x-px-ab", "2");
            return httpRequest;
        }
        httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
        httpRequest.putHeader("Accept", "application/json");
        httpRequest.putHeader("x-px-ab", "2");
        httpRequest.putHeader("User-Agent", WalmartConstants.ANDROID_OLD_WEB_UA);
        httpRequest.putHeader("did", DID);
        httpRequest.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
        httpRequest.putHeader("Content-Type", "application/json");
        httpRequest.putHeader("Content-Length", "DEFAULT_VALUE");
        httpRequest.putHeader("Accept-Encoding", "gzip");
        httpRequest.putHeader("Cookie", "DEFAULT_VALUE");
        return httpRequest;
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$initialisePX(WalmartAPI var0, CompletableFuture var1_1, int var2_2, Object var3_3) {
        switch (var2_2) {
            case 0: {
                v0 = var0.pxAPI.initialise();
                if (!v0.isDone()) {
                    var1_1 = v0;
                    return var1_1.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$initialisePX(io.trickle.task.sites.walmart.usa.WalmartAPI java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((WalmartAPI)var0, (CompletableFuture)var1_1, (int)1));
                }
                ** GOTO lbl10
            }
            case 1: {
                v0 = var1_1;
lbl10:
                // 2 sources

                if ((Boolean)v0.join() == false) return CompletableFuture.completedFuture(false);
                return CompletableFuture.completedFuture(true);
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public JsonObject getAtcForm(String string, int n) {
        return new JsonObject().put("offerId", (Object)string).put("quantity", (Object)n);
    }

    @Override
    public JsonObject PCIDForm() {
        Profile profile = this.task.getProfile();
        return new JsonObject().put("city", (Object)profile.getCity()).put("crt:CRT", (Object)"").put("customerId:CID", (Object)"").put("customerType:type", (Object)"").put("isZipLocated", (Object)true).put("postalCode", (Object)profile.getZip()).put("state", (Object)profile.getState()).put("storeList", (Object)new JsonArray());
    }

    public HttpRequest desktopAtc() {
        String string;
        double d = ThreadLocalRandom.current().nextDouble();
        if (d < Double.longBitsToDouble(4598175219545276416L)) {
            string = "https://www.walmart.com/api/v3/cart/guest/:CID/items";
            return this.addToCart(string);
        }
        if (d < Double.longBitsToDouble(4602678819172646912L)) {
            string = "https://www.walmart.com/api/v3/cart/guest/:CRT/items";
            return this.addToCart(string);
        }
        if (d < Double.longBitsToDouble(4604930618986332160L)) {
            string = "https://www.walmart.com/api/v3/cart/:CRT/items";
            return this.addToCart(string);
        }
        string = "https://www.walmart.com/api/v3/cart/:CID/items";
        return this.addToCart(string);
    }

    @Override
    public CompletableFuture initialisePX() {
        CompletableFuture completableFuture = this.pxAPI.initialise();
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartAPI.async$initialisePX(this, completableFuture2, 1, arg_0));
        }
        if ((Boolean)completableFuture.join() == false) return CompletableFuture.completedFuture(false);
        return CompletableFuture.completedFuture(true);
    }

    @Override
    public JsonObject accountLoginForm(Account account) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("email", (Object)account.getUser());
        jsonObject.put("password", (Object)account.getPass());
        return jsonObject;
    }

    @Override
    public CookieJar cookieStore() {
        return this.getWebClient().cookieStore();
    }

    @Override
    public HttpRequest submitPayment() {
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/api/checkout/v3/contract/:PCID/payment").as(BodyCodec.buffer());
        if (this.ios) {
            httpRequest.putHeaders(Headers$Pseudo.MSPA.get());
            httpRequest.putHeader("content-type", "application/json");
            httpRequest.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
            httpRequest.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
            httpRequest.putHeader("wm_vertical_id", "0");
            httpRequest.putHeader("mobile-app-version", "21.18.3");
            httpRequest.putHeader("wm_cvv_in_session", "true");
            httpRequest.putHeader("accept-language", "en-us");
            httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
            httpRequest.putHeader("inkiru_precedence", "false");
            httpRequest.putHeader("mobile-platform", "ios");
            httpRequest.putHeader("content-length", "DEFAULT_VALUE");
            httpRequest.putHeader("did", DID.replace("-", "").concat("00000000"));
            httpRequest.putHeader("user-agent", "Walmart/2107091553 Walmart WMTAPP v21.18.3");
            httpRequest.putHeader("cookie", "DEFAULT_VALUE");
            httpRequest.putHeader("x-px-ab", "2");
            return httpRequest;
        }
        httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
        httpRequest.putHeader("Accept", "application/json, text/javascript, */*; q=0.01");
        httpRequest.putHeader("wm_vertical_id", "0");
        httpRequest.putHeader("inkiru_precedence", "false");
        httpRequest.putHeader("wm_cvv_in_session", "true");
        httpRequest.putHeader("x-px-ab", "2");
        httpRequest.putHeader("User-Agent", WalmartConstants.ANDROID_OLD_WEB_UA);
        httpRequest.putHeader("did", DID);
        httpRequest.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
        httpRequest.putHeader("Content-Type", "application/json");
        httpRequest.putHeader("Content-Length", "DEFAULT_VALUE");
        httpRequest.putHeader("Accept-Encoding", "gzip");
        httpRequest.putHeader("Cookie", "DEFAULT_VALUE");
        return httpRequest;
    }

    @Override
    public JsonObject atcForm() {
        return this.getAtcForm(this.task.getKeywords()[0], Integer.parseInt(this.task.getSize()));
    }

    public HttpRequest atcAffiliate(String string) {
        HttpRequest httpRequest = this.client.getAbs(string).as(BodyCodec.none());
        httpRequest.putHeader("Pragma", "no-cache");
        httpRequest.putHeader("Cache-Control", "no-cache");
        httpRequest.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"88\", \" Not;A Brand\";v=\"99\", \"Chromium\";v=\"88\"");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("Upgrade-Insecure-Requests", "1");
        httpRequest.putHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.182 Safari/537.36");
        httpRequest.putHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("Sec-Fetch-Site", "none");
        httpRequest.putHeader("Sec-Fetch-Mode", "navigate");
        httpRequest.putHeader("Sec-Fetch-User", "?1");
        httpRequest.putHeader("Sec-Fetch-Dest", "document");
        httpRequest.putHeader("Accept-Encoding", "gzip, deflate, br");
        httpRequest.putHeader("Accept-Language", "en-US,en;q=0.9");
        if (this.dateOfPrevReq != null) {
            httpRequest.putHeader("if-modified-since", this.dateOfPrevReq);
        }
        this.dateOfPrevReq = GMT_CHROME_RFC1123.format(ZonedDateTime.now(ZoneOffset.UTC));
        return httpRequest;
    }

    @Override
    public HttpRequest getCartV3(String string) {
        return this.getCartMobile("https://www.walmart.com/api/v3/cart/" + string);
    }

    public HttpRequest updateCheck() {
        HttpRequest httpRequest = this.client.getAbs("https://ota.walmart.com/v0.1/public/codepush/update_check?deployment_key=rULtKsvrPnyMXbxEHQnxoRPJJWnTOxlUbjxIOSAt&app_version=21.5.5&client_unique_id=99155d91e8fc6bf5").timeout(TimeUnit.SECONDS.toMillis(10L)).as(BodyCodec.none());
        if (this.ios) {
            httpRequest.putHeaders(Headers$Pseudo.MSPA.get());
            httpRequest.putHeader("content-type", "application/json");
            httpRequest.putHeader("accept", "application/json");
            httpRequest.putHeader("mobile-app-version", "21.18.3");
            httpRequest.putHeader("accept-language", "en-us");
            httpRequest.putHeader("x-codepush-plugin-name", "react-native-code-push");
            httpRequest.putHeader("x-codepush-sdk-version", "^3.1.0");
            httpRequest.putHeader("mobile-platform", "ios");
            httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
            httpRequest.putHeader("did", DID.replace("-", "").concat("00000000"));
            httpRequest.putHeader("user-agent", "Walmart/2107091553 CFNetwork/1128.0.1 Darwin/19.6.0");
            httpRequest.putHeader("cookie", "DEFAULT_VALUE");
            httpRequest.putHeader("x-codepush-plugin-version", "6.2.1");
            return httpRequest;
        }
        httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
        httpRequest.putHeader("User-Agent", WalmartConstants.ANDROID_OLD_UA);
        httpRequest.putHeader("did", DID);
        httpRequest.putHeader("Accept-Encoding", "gzip");
        httpRequest.putHeader("Cookie", "DEFAULT_VALUE");
        return httpRequest;
    }

    @Override
    public HttpRequest getCart() {
        return this.getCartMobile("https://api.mobile.walmart.com/v1/cart/items");
    }

    public HttpRequest putCartAndroid(String string) {
        HttpRequest httpRequest = this.client.putAbs("https://www.walmart.com/api/v3/cart/" + string).timeout(TimeUnit.SECONDS.toMillis(10L)).as(BodyCodec.buffer());
        httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
        httpRequest.putHeader("User-Agent", WalmartConstants.ANDROID_OLD_UA);
        httpRequest.putHeader("did", DID);
        httpRequest.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
        httpRequest.putHeader("Content-Type", "application/json; charset=utf-8");
        httpRequest.putHeader("Content-Length", "DEFAULT_VALUE");
        httpRequest.putHeader("Accept-Encoding", "gzip");
        httpRequest.putHeader("Cookie", "DEFAULT_VALUE");
        return httpRequest;
    }

    @Override
    public HttpRequest createAccount() {
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/account/electrode/api/identity/sign-up").as(BodyCodec.buffer());
        if (this.ios) {
            httpRequest.putHeaders(Headers$Pseudo.MSPA.get());
            httpRequest.putHeader("content-type", "application/json");
            httpRequest.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
            httpRequest.putHeader("mobile-app-version", "21.18.3");
            httpRequest.putHeader("accept-language", "en-us");
            httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
            httpRequest.putHeader("mobile-platform", "ios");
            httpRequest.putHeader("content-length", "DEFAULT_VALUE");
            httpRequest.putHeader("did", DID.replace("-", "").concat("00000000"));
            httpRequest.putHeader("user-agent", "Walmart/2107091553 Walmart WMTAPP v21.18.3");
            httpRequest.putHeader("cookie", "DEFAULT_VALUE");
            httpRequest.putHeader("x-px-ab", "2");
            return httpRequest;
        }
        httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
        httpRequest.putHeader("x-wm-auth", "null");
        httpRequest.putHeader("x-wm-cid", "null");
        httpRequest.putHeader("x-wm-spid", "null");
        httpRequest.putHeader("x-wm-xpa", "null");
        httpRequest.putHeader("user-agent", WalmartConstants.ANDROID_OLD_UA);
        httpRequest.putHeader("did", DID);
        httpRequest.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
        httpRequest.putHeader("content-type", "text/plain;charset=UTF-8");
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("accept-encoding", "gzip");
        return httpRequest;
    }

    public HttpRequest mobileAtc() {
        if (!this.task.getMode().contains("other")) return this.addToCart("https://api.mobile.walmart.com/v1/cart/items");
        return this.addToCart("https://www.walmart.com/api/v1/cart/items");
    }

    @Override
    public HttpRequest addToCart() {
        return this.mobileAtc();
    }

    @Override
    public boolean isLoggedIn() {
        return this.loggedIn;
    }

    @Override
    public HttpRequest processPayment(PaymentToken paymentToken) {
        HttpRequest httpRequest = this.client.putAbs("https://www.walmart.com/api/checkout/v3/contract/:PCID/order").as(BodyCodec.buffer());
        if (this.ios) {
            httpRequest.putHeader("cookie", "DEFAULT_VALUE");
            httpRequest.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
            httpRequest.putHeader("mobile-app-version", "21.18.3");
            httpRequest.putHeader("inkiru_precedence", "false");
            httpRequest.putHeader("mobile-platform", "ios");
            httpRequest.putHeader("vid", paymentToken.getVid().toUpperCase());
            httpRequest.putHeader("did", DID.replace("-", "").concat("00000000"));
            httpRequest.putHeader("user-agent", "Walmart/2107091553 Walmart WMTAPP v21.18.3");
            httpRequest.putHeader("sid", paymentToken.getSid().toUpperCase());
            httpRequest.putHeader("content-length", "DEFAULT_VALUE");
            httpRequest.putHeader("x-px-ab", "2");
            httpRequest.putHeader("x-o-segment", "blue");
            httpRequest.putHeader("accept-language", "en-us");
            httpRequest.putHeader("wm_cvv_in_session", "true");
            httpRequest.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
            httpRequest.putHeader("content-type", "application/json");
            httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
            httpRequest.putHeader("wm_vertical_id", "0");
            return httpRequest;
        }
        httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
        httpRequest.putHeader("Accept", "application/json, text/javascript, */*; q=0.01");
        httpRequest.putHeader("wm_vertical_id", "0");
        httpRequest.putHeader("inkiru_precedence", "false");
        httpRequest.putHeader("wm_cvv_in_session", "true");
        httpRequest.putHeader("x-o-segment", "blue");
        httpRequest.putHeader("x-px-ab", "2");
        httpRequest.putHeader("sid", paymentToken.getSid());
        httpRequest.putHeader("vid", paymentToken.getVid());
        httpRequest.putHeader("User-Agent", WalmartConstants.ANDROID_OLD_WEB_UA);
        httpRequest.putHeader("did", DID);
        httpRequest.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
        httpRequest.putHeader("Content-Type", "application/json");
        httpRequest.putHeader("Content-Length", "DEFAULT_VALUE");
        httpRequest.putHeader("Accept-Encoding", "gzip");
        httpRequest.putHeader("Cookie", "DEFAULT_VALUE");
        return httpRequest;
    }

    @Override
    public JsonObject getProcessingForm() {
        return new JsonObject();
    }

    @Override
    public CompletableFuture generatePX(boolean bl) {
        try {
            if (!bl) return this.pxAPI.solve();
            this.pxAPI.reset();
            return this.pxAPI.solve();
        }
        catch (Exception exception) {
            System.out.println("Error generating mobile session: " + exception.getMessage());
            return CompletableFuture.completedFuture(false);
        }
    }

    public HttpRequest midasScan() {
        HttpRequest httpRequest = this.client.getAbs("https://www.walmart.com/midas/srv/v3/hl/item/506574645?type=product&pageType=item&platform=app&module=wpa&mloc=middle&min=2&max=20&placementId=480x212_B-C-OG_TI_2-20_PDP-m-app-" + (this.ios ? "iPhone" : "android") + "&pageId=506574645&taxonomy=0%3A4171%3A4191%3A9807313%3A6249075%3A1619679&keyword=20%20PANINI%20PLAYBOOK%20FOOTBALL%20HANGER%20BOX%20(%2030%20cards%20per%20box)&zipCode=" + this.getTask().getProfile().getZip() + "&isZipLocated=false").timeout(TimeUnit.SECONDS.toMillis(10L)).as(BodyCodec.none());
        if (this.ios) {
            httpRequest.putHeaders(Headers$Pseudo.MSPA.get());
            httpRequest.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
            httpRequest.putHeader("accept", "*/*");
            httpRequest.putHeader("mobile-app-version", "21.18.3");
            httpRequest.putHeader("wm_site_mode", "0");
            httpRequest.putHeader("accept-language", "en-us");
            httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
            httpRequest.putHeader("mobile-platform", "ios");
            httpRequest.putHeader("did", DID.replace("-", "").concat("00000000"));
            httpRequest.putHeader("user-agent", "Walmart/2107091553 CFNetwork/1128.0.1 Darwin/19.6.0");
            httpRequest.putHeader("cookie", "DEFAULT_VALUE");
            return httpRequest;
        }
        httpRequest.putHeaders(Headers$Pseudo.MPAS.get());
        httpRequest.putHeader("wm_site_mode", "0");
        httpRequest.putHeader("user-agent", WalmartConstants.ANDROID_OLD_UA);
        httpRequest.putHeader("did", DID);
        httpRequest.putHeader("x-px-authorization", this.pxAPI.tokenValue() == null ? "3" : (String)this.pxAPI.tokenValue());
        httpRequest.putHeader("accept-encoding", "gzip");
        httpRequest.putHeader("cookie", "DEFAULT_VALUE");
        return httpRequest;
    }
}

