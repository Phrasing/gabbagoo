/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.harvester.Harvester
 *  io.trickle.task.Task
 *  io.trickle.task.sites.Site
 *  io.trickle.task.sites.shopify.util.SiteParser
 *  io.trickle.util.Pair
 *  io.trickle.webclient.RealClient
 *  io.trickle.webclient.TaskApiClient
 *  io.vertx.core.MultiMap
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.client.WebClient
 *  io.vertx.ext.web.codec.BodyCodec
 */
package io.trickle.task.sites.shopify;

import io.trickle.harvester.Harvester;
import io.trickle.task.Task;
import io.trickle.task.sites.Site;
import io.trickle.task.sites.shopify.util.SiteParser;
import io.trickle.util.Pair;
import io.trickle.webclient.RealClient;
import io.trickle.webclient.TaskApiClient;
import io.vertx.core.MultiMap;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShopifyAPI
extends TaskApiClient {
    public String SITE_URL;
    public static String[] api_ua;
    public String SEC_UA;
    public boolean graphUnstable;
    public static Pattern VER_PATTERN;
    public String SHOP_ID;
    public Pair<String, String> propertiesPair;
    public boolean isFirstProcessingPageVisit;
    public boolean isOOS;
    public String key;
    public String API_TOKEN;
    public static int EXCEPTION_RETRY_DELAY;
    public RealClient checkpointClient;
    public String UA;

    public HttpRequest paymentPage(String string) {
        String string2 = "https://" + this.SITE_URL + "/" + this.SHOP_ID + "/checkouts/" + string;
        if (this.getOOSDirect()) {
            string2 = string2 + "/shipping_rates";
        }
        string2 = string2 + "?previous_step=shipping_method&step=payment_method";
        if (this.key != null) {
            string2 = string2 + "&key=" + this.key;
        }
        HttpRequest httpRequest = this.client.getAbs(string2).as(BodyCodec.buffer());
        httpRequest.putHeader("cache-control", "max-age=0");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("referer", "https://" + this.SITE_URL + "/" + this.SHOP_ID + "/checkouts/" + string + "?previous_step=shipping_method&step=payment_method");
        httpRequest.putHeader("accept-encoding", "gzip, deflate");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest shippingRateAPI(String string) {
        HttpRequest httpRequest = this.client.getAbs("https://" + this.SITE_URL + "/wallets/checkouts/" + string + "/shipping_rates.json").as(BodyCodec.buffer());
        httpRequest.putHeader("x-shopify-uniquetoken", this.getCookies().getCookieValue("_shopify_y"));
        httpRequest.putHeader("authorization", this.API_TOKEN);
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("accept", "application/json");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("x-shopify-visittoken", this.getCookies().getCookieValue("_shopify_s"));
        httpRequest.putHeader("accept-encoding", "gzip, deflate");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest clearCart() {
        HttpRequest httpRequest = this.client.getAbs("https://" + this.SITE_URL + "/cart/clear.js").as(BodyCodec.buffer());
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "none");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("accept-encoding", "gzip, deflate");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest submitChallenge() {
        HttpRequest httpRequest = this.client.postAbs("https://" + this.SITE_URL + "/account/login").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("cache-control", "max-age=0");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("origin", "https://" + this.SITE_URL);
        httpRequest.putHeader("content-type", "application/x-www-form-urlencoded");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("referer", "https://" + this.SITE_URL + "/challenge");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest postPaymentWithOption(String string, boolean bl) {
        String string2 = "https://" + this.SITE_URL + "/" + this.SHOP_ID + "/checkouts/" + string;
        if (this.key != null) {
            string2 = string2 + "?key=" + this.key;
        }
        HttpRequest httpRequest = bl ? this.client.patchAbs(string2).as(BodyCodec.buffer()) : this.client.postAbs(string2).as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("cache-control", "max-age=0");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("origin", "https://" + this.SITE_URL);
        httpRequest.putHeader("content-type", !bl ? "application/x-www-form-urlencoded" : "DONT_SET");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("referer", "https://" + this.SITE_URL + "/" + this.SHOP_ID + "/checkouts/" + string + "?previous_step=shipping_method&step=payment_method");
        httpRequest.putHeader("accept-encoding", "gzip, deflate");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public boolean checkIsOOS() {
        if (!this.isOOS) return false;
        this.isOOS = false;
        return true;
    }

    public MultiMap emptyCheckoutViaCartForm() {
        MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
        multiMap.set("updates[]", "1");
        multiMap.set("attributes[checkout_clicked]", "true");
        multiMap.set("checkout", "");
        return multiMap;
    }

    public HttpRequest calculateTaxesWallets(String string) {
        HttpRequest httpRequest = this.client.getAbs("https://" + this.SITE_URL + "/wallets/checkouts/" + string + "/calculate_shipping").as(BodyCodec.buffer());
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("x-shopify-uniquetoken", this.getCookies().getCookieValue("_shopify_y"));
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("authorization", this.API_TOKEN);
        httpRequest.putHeader("x-shopify-checkout-version", "2018-03-05");
        httpRequest.putHeader("x-shopify-visittoken", this.getCookies().getCookieValue("_shopify_s"));
        httpRequest.putHeader("accept", "application/json");
        httpRequest.putHeader("x-shopify-wallets-caller", "costanza");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://" + this.SITE_URL + "/");
        httpRequest.putHeader("accept-encoding", "gzip, deflate");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest cartJS() {
        HttpRequest httpRequest = this.client.getAbs("https://" + this.SITE_URL + "/cart.js").as(BodyCodec.buffer());
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://" + this.SITE_URL + "/products/" + ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE));
        httpRequest.putHeader("accept-encoding", "gzip, deflate");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public boolean getOOSDirect() {
        return this.isOOS;
    }

    public String getSiteURL() {
        return this.SITE_URL;
    }

    public HttpRequest fakePatchPayment(String string) {
        return this.postPaymentWithOption(string, true);
    }

    public HttpRequest shippingPage(String string) {
        String string2 = "https://" + this.SITE_URL + "/" + this.SHOP_ID + "/checkouts/" + string;
        if (this.checkIsOOS()) {
            string2 = string2 + "/shipping_rates";
        }
        string2 = string2 + "?previous_step=contact_information&step=shipping_method";
        if (this.key != null) {
            string2 = string2 + "&key=" + this.key;
        }
        HttpRequest httpRequest = this.client.getAbs(string2).as(BodyCodec.buffer());
        httpRequest.putHeader("cache-control", "max-age=0");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("referer", "https://" + this.SITE_URL + "/");
        httpRequest.putHeader("accept-encoding", "gzip, deflate");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest emptyCheckoutViaCart() {
        HttpRequest httpRequest = this.client.postAbs("https://" + this.SITE_URL + "/cart").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("cache-control", "max-age=0");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("origin", "https://" + this.SITE_URL);
        httpRequest.putHeader("content-type", "application/x-www-form-urlencoded");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("origin", "https://" + this.SITE_URL + "/");
        httpRequest.putHeader("accept-encoding", "gzip, deflate");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest checkpointPage(boolean bl) {
        if (!bl) {
            this.checkpointClient.cookieStore().removeAnyMatch("_secure_session_id");
        }
        HttpRequest httpRequest = this.checkpointClient.getAbs("https://" + this.SITE_URL + "/checkpoint?return_to=https%3A%2F%2F" + this.SITE_URL + "%2Fcheckout").as(BodyCodec.buffer());
        httpRequest.putHeader("cache-control", "max-age=0");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        if (Harvester.HARVESTER_UA != null) {
            httpRequest.putHeader("user-agent", Harvester.HARVESTER_UA);
        } else {
            httpRequest.putHeader("user-agent", this.UA);
        }
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("referer", "https://" + this.SITE_URL + "/checkout");
        httpRequest.putHeader("accept-encoding", "gzip, deflate");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest getProcessingRedirect(String string) {
        String string2 = "https://" + this.SITE_URL + "/" + this.SHOP_ID + "/checkouts/" + string;
        if (this.checkIsOOS()) {
            string2 = string2 + "/shipping_rates";
        }
        string2 = string2 + "?previous_step=payment_method&step=";
        if (this.key != null) {
            string2 = string2 + "&key=" + this.key;
        }
        HttpRequest httpRequest = this.client.getAbs(string2).as(BodyCodec.buffer());
        httpRequest.putHeader("cache-control", "max-age=0");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("referer", "https://" + this.SITE_URL + "/");
        httpRequest.putHeader("accept-encoding", "gzip, deflate");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest elJS(String object) {
        if (ThreadLocalRandom.current().nextBoolean()) {
            object = ((String)object).replace("products", "collections/" + ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE) + "/products");
        }
        object = (String)object + (ThreadLocalRandom.current().nextBoolean() ? ".js?variant=" : "?format=js&variant=") + ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
        HttpRequest httpRequest = this.client.getAbs((String)object).as(BodyCodec.buffer());
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "none");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("accept-encoding", "gzip, deflate");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest fetchNewQueueUrl() {
        HttpRequest httpRequest = this.client.getAbs("https://" + this.SITE_URL + "/throttle/queue").as(BodyCodec.buffer());
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("referer", "https://" + this.SITE_URL);
        httpRequest.putHeader("accept-encoding", "gzip, deflate");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public MultiMap challengeForm(String string, String string2) {
        MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
        multiMap.set("authenticity_token", string);
        multiMap.set("g-recaptcha-response", string2);
        return multiMap;
    }

    public HttpRequest paymentStatusAPI(String string) {
        HttpRequest httpRequest = this.client.getAbs("https://" + this.SITE_URL + "/api/checkouts/" + string + "/payments/").as(BodyCodec.buffer());
        httpRequest.putHeader("x-shopify-uniquetoken", this.getCookies().getCookieValue("_shopify_y"));
        httpRequest.putHeader("authorization", this.API_TOKEN);
        httpRequest.putHeader("accept", "application/json");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("x-shopify-visittoken", this.getCookies().getCookieValue("_shopify_s"));
        httpRequest.putHeader("accept-encoding", "gzip, deflate");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest atcAJAX(String string) {
        String string2 = "https://" + this.SITE_URL + "/cart/add.js?id=" + string;
        if (this.propertiesPair != null) {
            string2 = string2 + "&properties[" + (String)this.propertiesPair.first + "]=" + (String)this.propertiesPair.second;
        }
        HttpRequest httpRequest = this.client.getAbs(string2).as(BodyCodec.buffer());
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "none");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("accept-encoding", "gzip, deflate");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest updateJS() {
        HttpRequest httpRequest = this.client.postAbs("https://" + this.SITE_URL + "/cart/update.js").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
        httpRequest.putHeader("sec-fetch-site", "none");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("referer", "https://" + this.SITE_URL + "/products/nkcu7544-400");
        httpRequest.putHeader("accept-encoding", "gzip, deflate");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest rotateHeaderReq(HttpRequest httpRequest) {
        List list = httpRequest.headers().entries();
        Collections.shuffle(list);
        httpRequest.headers().clear();
        list.spliterator().forEachRemaining(arg_0 -> ShopifyAPI.lambda$rotateHeaderReq$0(httpRequest, arg_0));
        return httpRequest;
    }

    public HttpRequest shippingRateOld(String string, String string2, String string3) {
        HttpRequest httpRequest = this.client.getAbs("https://" + this.SITE_URL + "/cart/shipping_rates.json?shipping_address[zip]=" + string + "&shipping_address[country]=" + string2 + "&shipping_address[province]=" + string3).as(BodyCodec.buffer());
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "none");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("accept-encoding", "gzip, deflate");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest processAPI(String string) {
        String string2 = "https://" + this.SITE_URL + "/" + this.SHOP_ID + "/checkouts/" + string;
        if (this.key != null) {
            string2 = string2 + "?key=" + this.key;
        }
        HttpRequest httpRequest = this.client.patchAbs(string2).as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("x-shopify-uniquetoken", UUID.randomUUID().toString());
        httpRequest.putHeader("authorization", this.API_TOKEN);
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("x-shopify-visittoken", this.getCookies().getCookieValue("_shopify_s"));
        httpRequest.putHeader("accept-encoding", "gzip, deflate");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public boolean getIsOOS() {
        return this.isOOS;
    }

    public HttpRequest emptyCheckout(boolean bl) {
        HttpRequest httpRequest;
        if (bl) {
            httpRequest = this.client.postAbs("https://" + this.SITE_URL + "/checkout").as(BodyCodec.buffer());
            httpRequest.putHeader("content-length", "DEFAULT_VALUE");
            httpRequest.putHeader("content-type", "application/json");
        } else {
            httpRequest = this.client.getAbs("https://" + this.SITE_URL + "/checkout").as(BodyCodec.buffer());
        }
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "none");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("accept-encoding", "gzip, deflate");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest challengePage() {
        HttpRequest httpRequest = this.client.getAbs("https://" + this.SITE_URL + "/challenge").as(BodyCodec.buffer());
        httpRequest.putHeader("cache-control", "max-age=0");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("referer", "https://" + this.SITE_URL + "/account/login");
        httpRequest.putHeader("accept-encoding", "gzip, deflate");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest postContact(String string) {
        String string2 = "https://" + this.SITE_URL + "/" + this.SHOP_ID + "/checkouts/" + string;
        if (this.key != null) {
            string2 = string2 + "?key=" + this.key;
        }
        HttpRequest httpRequest = this.client.postAbs(string2).as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("cache-control", "max-age=0");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("origin", "https://" + this.SITE_URL);
        httpRequest.putHeader("content-type", "application/x-www-form-urlencoded");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("referer", "https://" + this.SITE_URL + "/");
        httpRequest.putHeader("accept-encoding", "gzip, deflate");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest upsellATC() {
        HttpRequest httpRequest = this.client.postAbs("https://" + this.SITE_URL + "/cart/add.js").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("content-type", "application/x-www-form-urlencoded; charset=UTF-8");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
        httpRequest.putHeader("sec-fetch-site", "none");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("referer", "https://" + this.SITE_URL + "/products/" + ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE));
        httpRequest.putHeader("accept-encoding", "gzip, deflate");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public boolean markProcessingPageAsVisited() {
        if (!this.isFirstProcessingPageVisit) return false;
        this.isFirstProcessingPageVisit = false;
        return true;
    }

    public String getBaseCheckoutURL() {
        return "https://" + this.SITE_URL + "/" + this.SHOP_ID + "/checkouts/";
    }

    public HttpRequest newQueue() {
        HttpRequest httpRequest = this.client.postAbs("https://" + this.SITE_URL + "/queue/poll").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("origin", "https://" + this.SITE_URL);
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://" + this.SITE_URL + "/throttle/queue");
        httpRequest.putHeader("accept-encoding", "gzip, deflate");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest meta() {
        HttpRequest httpRequest = this.client.getAbs("https://" + this.SITE_URL + "/payments/config.json").as(BodyCodec.buffer());
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "none");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("accept-encoding", "gzip, deflate");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest oosPage(String string) {
        HttpRequest httpRequest = this.client.getAbs(string).as(BodyCodec.buffer());
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "none");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("accept-encoding", "gzip, deflate");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public ShopifyAPI(Task task) {
        this.SITE_URL = SiteParser.getURLFromSite((Task)task);
        this.UA = api_ua[ThreadLocalRandom.current().nextInt(api_ua.length)];
        String string = ShopifyAPI.parseChromeVer(this.UA);
        this.SEC_UA = "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"" + string + "\", \"Google Chrome\";v=\"" + string + "\"";
        this.propertiesPair = SiteParser.getProperties((Site)task.getSite());
    }

    public HttpRequest genAcc() {
        HttpRequest httpRequest = this.client.postAbs("https://" + this.SITE_URL + "/account").as(BodyCodec.none());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("cache-control", "max-age=0");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("origin", "https://" + this.SITE_URL);
        httpRequest.putHeader("content-type", "application/x-www-form-urlencoded");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("referer", "https://" + this.SITE_URL + "/account/register");
        httpRequest.putHeader("accept-encoding", "gzip, deflate");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest changeJS() {
        HttpRequest httpRequest = this.client.postAbs("https://" + this.SITE_URL + "/cart/change.js").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("content-type", "application/x-www-form-urlencoded; charset=UTF-8");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
        httpRequest.putHeader("sec-fetch-site", "none");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("referer", "https://" + this.SITE_URL + "/products/nkcu7544-400");
        httpRequest.putHeader("accept-encoding", "gzip, deflate");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public void setShopID(String string) {
        this.SHOP_ID = string;
    }

    public void setAPIToken(String string) {
        if (string == null) {
            System.out.println("No api key was found for this site, please contact the discord to add this site to the sitelist to run smoother.");
            return;
        }
        this.API_TOKEN = "Basic " + Base64.getEncoder().encodeToString(string.getBytes(StandardCharsets.UTF_8));
    }

    public static void lambda$rotateHeaderReq$0(HttpRequest httpRequest, Map.Entry entry) {
        httpRequest.putHeader((String)entry.getKey(), (String)entry.getValue());
    }

    public HttpRequest fetchQueueUrl() {
        String string = this.client.cookieStore().getCookieValue("_shopify_ctd");
        HttpRequest httpRequest = this.client.getAbs("https://" + this.SITE_URL + "/throttle/queue?_ctd=" + string + "&_ctd_update").as(BodyCodec.buffer());
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("referer", "https://" + this.SITE_URL);
        httpRequest.putHeader("accept-encoding", "gzip, deflate");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest rawCheckoutUrl(String string) {
        String string2 = "https://" + this.SITE_URL;
        if (this.graphUnstable) {
            string2 = string2 + "/checkouts/" + string;
            string2 = string2 + "/information";
        } else {
            string2 = string2 + "/" + this.SHOP_ID + "/checkouts/" + string;
        }
        if (this.key != null) {
            string2 = string2 + "?key=" + this.key;
        }
        HttpRequest httpRequest = this.client.getAbs(string2).as(BodyCodec.buffer());
        httpRequest.putHeader("cache-control", "max-age=0");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("referer", "https://" + this.SITE_URL + "/checkpoint?return_to=https%3A%2F%2F" + this.SITE_URL + "%2Fcart");
        httpRequest.putHeader("accept-encoding", "gzip, deflate");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public void setInstock() {
        this.isOOS = false;
    }

    public HttpRequest paymentToken(WebClient webClient) {
        HttpRequest httpRequest = webClient.postAbs("https://deposit.us.shopifycs.com/sessions").as(BodyCodec.buffer());
        httpRequest.putHeader("Host", "deposit.us.shopifycs.com");
        httpRequest.putHeader("Connection", "keep-alive");
        httpRequest.putHeader("Content-Length", "DEFAULT_VALUE");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("Accept", "application/json");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("User-Agent", this.UA);
        httpRequest.putHeader("Content-Type", "application/json");
        httpRequest.putHeader("Origin", "https://checkout.shopifycs.com");
        httpRequest.putHeader("Sec-Fetch-Site", "same-site");
        httpRequest.putHeader("Sec-Fetch-Mode", "cors");
        httpRequest.putHeader("Sec-Fetch-Dest", "empty");
        httpRequest.putHeader("Referer", "https://checkout.shopifycs.com/");
        httpRequest.putHeader("Accept-Encoding", "gzip, deflate");
        httpRequest.putHeader("Accept-Language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest postPayment(String string) {
        return this.postPaymentWithOption(string, false);
    }

    static {
        EXCEPTION_RETRY_DELAY = 3000;
        VER_PATTERN = Pattern.compile("Chrome/([0-9][0-9])");
        api_ua = new String[]{"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.51 Safari/537.36", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.51 Safari/537.36", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.109 Safari/537.36", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.99 Safari/537.36", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.51 Safari/537.36", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.51 Safari/537.36", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.82 Safari/537.36", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.80 Safari/537.36", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.109 Safari/537.36"};
    }

    public HttpRequest oldQueue() {
        String string = this.client.cookieStore().getCookieValue("_shopify_ctd");
        HttpRequest httpRequest = this.client.getAbs("https://" + this.SITE_URL + "/checkout/poll?js_poll=1").as(BodyCodec.buffer());
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://" + this.SITE_URL + "/throttle/queue?_ctd=" + string + "&_ctd_update");
        httpRequest.putHeader("accept-encoding", "gzip, deflate");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest postShippingRate(String string) {
        String string2 = "https://" + this.SITE_URL + "/" + this.SHOP_ID + "/checkouts/" + string;
        if (this.key != null) {
            string2 = string2 + "?key=" + this.key;
        }
        HttpRequest httpRequest = this.client.postAbs(string2).as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("cache-control", "max-age=0");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("origin", "https://" + this.SITE_URL);
        httpRequest.putHeader("content-type", "application/x-www-form-urlencoded");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("referer", "https://" + this.SITE_URL + "/");
        httpRequest.putHeader("accept-encoding", "gzip, deflate");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest productsJSON(boolean bl) {
        HttpRequest httpRequest = this.client.getAbs(bl ? "https://" + this.SITE_URL + "/products.json?order=" + ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE) : "https://" + this.SITE_URL + "/products.json?limit=250").as(BodyCodec.buffer());
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "none");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("accept-encoding", "gzip, deflate");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest cart() {
        HttpRequest httpRequest = this.client.getAbs("https://" + this.SITE_URL + "/cart").as(BodyCodec.buffer());
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "none");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("accept-encoding", "gzip, deflate");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest contactAPI(String string) {
        HttpRequest httpRequest = this.client.patchAbs("https://" + this.SITE_URL + "/" + this.SHOP_ID + "/checkouts/" + string).as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("authorization", this.API_TOKEN);
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("accept-encoding", "gzip, deflate");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest walletsPutDetails(String string) {
        HttpRequest httpRequest = this.client.putAbs("https://" + this.SITE_URL + "/api/checkouts/" + string + ".json").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("x-shopify-uniquetoken", this.getCookies().getCookieValue("_shopify_y"));
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("authorization", this.API_TOKEN);
        httpRequest.putHeader("x-shopify-checkout-version", "2018-03-05");
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("accept", "application/json");
        httpRequest.putHeader("x-shopify-wallets-caller", "costanza");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("x-shopify-visittoken", this.getCookies().getCookieValue("_shopify_s"));
        httpRequest.putHeader("origin", "https://" + this.SITE_URL);
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://" + this.SITE_URL + "/collections/frontpage");
        httpRequest.putHeader("accept-encoding", "gzip, deflate");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest atcWallets() {
        HttpRequest httpRequest = this.client.postAbs("https://" + this.SITE_URL + "/wallets/checkouts.json").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("x-shopify-uniquetoken", this.getCookies().getCookieValue("_shopify_y"));
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("authorization", this.API_TOKEN);
        httpRequest.putHeader("x-shopify-checkout-version", "2018-03-05");
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("accept", "application/json");
        httpRequest.putHeader("x-shopify-wallets-caller", "costanza");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("x-shopify-visittoken", this.getCookies().getCookieValue("_shopify_s"));
        httpRequest.putHeader("origin", "https://" + this.SITE_URL);
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://" + this.SITE_URL + "/collections/frontpage");
        httpRequest.putHeader("accept-encoding", "gzip, deflate");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest submitCheckpoint() {
        HttpRequest httpRequest = this.checkpointClient.postAbs("https://" + this.SITE_URL + "/checkpoint").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("cache-control", "max-age=0");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("origin", "https://" + this.SITE_URL);
        httpRequest.putHeader("content-type", "application/x-www-form-urlencoded");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("referer", "https://" + this.SITE_URL + "/checkpoint?return_to=https%3A%2F%2F" + this.SITE_URL + "%2Fcheckout");
        httpRequest.putHeader("accept-encoding", "gzip, deflate");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest submitPassword() {
        HttpRequest httpRequest = this.client.postAbs("https://" + this.SITE_URL + "/password").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("cache-control", "max-age=0");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("origin", "https://" + this.SITE_URL);
        httpRequest.putHeader("content-type", "application/x-www-form-urlencoded");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("referer", "https://" + this.SITE_URL + "/password");
        httpRequest.putHeader("accept-encoding", "gzip, deflate");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public ShopifyAPI(Task task, RealClient realClient) {
        super(realClient);
        this.SITE_URL = SiteParser.getURLFromSite((Task)task);
        this.UA = api_ua[ThreadLocalRandom.current().nextInt(api_ua.length)];
        String string = ShopifyAPI.parseChromeVer(this.UA);
        this.SEC_UA = "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"" + string + "\", \"Google Chrome\";v=\"" + string + "\"";
        this.propertiesPair = SiteParser.getProperties((Site)task.getSite());
    }

    public HttpRequest homepage() {
        HttpRequest httpRequest = this.client.getAbs("https://" + this.SITE_URL + "/").as(BodyCodec.buffer());
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "none");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("accept-encoding", "gzip, deflate");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest basicATC() {
        HttpRequest httpRequest = this.client.postAbs("https://" + this.SITE_URL + "/cart/add").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("content-type", "application/x-www-form-urlencoded; charset=UTF-8");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "none");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("referer", "https://" + this.SITE_URL + "/products/" + ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE));
        httpRequest.putHeader("accept-encoding", "gzip, deflate");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public String fakeCheckoutPath() {
        String string = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder stringBuilder = new StringBuilder();
        while (stringBuilder.length() < 32) {
            int n = (int)(ThreadLocalRandom.current().nextFloat() * (float)"abcdefghijklmnopqrstuvwxyz0123456789".length());
            stringBuilder.append("abcdefghijklmnopqrstuvwxyz0123456789".charAt(n));
        }
        return stringBuilder.toString();
    }

    public HttpRequest login() {
        HttpRequest httpRequest = this.client.postAbs("https://" + this.SITE_URL + "/account/login").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("cache-control", "max-age=0");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("origin", "https://" + this.SITE_URL);
        httpRequest.putHeader("content-type", "application/x-www-form-urlencoded");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("referer", "https://" + this.SITE_URL + "/account/login?return_url=%2Faccount");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return this.rotateHeaderReq(httpRequest);
    }

    public void setOOS() {
        this.isOOS = true;
    }

    public HttpRequest accountConfirmPage() {
        HttpRequest httpRequest = this.client.getAbs("https://" + this.SITE_URL + "/account").as(BodyCodec.buffer());
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "none");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("accept-encoding", "gzip, deflate");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return this.rotateHeaderReq(httpRequest);
    }

    public HttpRequest contactPage(String string) {
        String string2 = "https://" + this.SITE_URL;
        if (this.graphUnstable) {
            string2 = string2 + "/checkouts/" + string;
            string2 = string2 + "/information";
        } else {
            string2 = string2 + "/" + this.SHOP_ID + "/checkouts/" + string;
            if (this.checkIsOOS()) {
                string2 = string2 + "/stock_problems";
            }
            string2 = this.key != null ? string2 + "?key=" + this.key : string2 + "?step=contact_information";
        }
        HttpRequest httpRequest = this.client.getAbs(string2).as(BodyCodec.buffer());
        httpRequest.putHeader("cache-control", "max-age=0");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("referer", "https://" + this.SITE_URL + "/checkpoint?return_to=https%3A%2F%2F" + this.SITE_URL + "%2Fcart");
        httpRequest.putHeader("accept-encoding", "gzip, deflate");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public static String parseChromeVer(String string) {
        Matcher matcher = VER_PATTERN.matcher(string);
        return matcher.find() ? matcher.group(1) : "88";
    }

    public HttpRequest refreshLink(String string) {
        String string2 = "https://" + this.SITE_URL + "/" + this.SHOP_ID + "/checkouts/" + string;
        string2 = string2 + "?previous_step=shipping_method&step=payment_method";
        if (this.key != null) {
            string2 = string2 + "&key=" + this.key;
        }
        HttpRequest httpRequest = this.client.getAbs(string2).as(BodyCodec.buffer());
        httpRequest.putHeader("cache-control", "max-age=0");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("referer", "https://" + this.SITE_URL + "/");
        httpRequest.putHeader("accept-encoding", "gzip, deflate");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }
}
