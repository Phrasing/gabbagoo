/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.harvester.Harvester
 *  io.trickle.task.Task
 *  io.trickle.task.sites.amazon.CartAPI
 *  io.trickle.task.sites.shopify.ShopifyAPI
 *  io.trickle.util.Pair
 *  io.trickle.webclient.TaskApiClient
 *  io.vertx.core.MultiMap
 *  io.vertx.core.json.JsonObject
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.codec.BodyCodec
 */
package io.trickle.task.sites.amazon;

import io.trickle.harvester.Harvester;
import io.trickle.task.Task;
import io.trickle.task.sites.amazon.CartAPI;
import io.trickle.task.sites.shopify.ShopifyAPI;
import io.trickle.util.Pair;
import io.trickle.webclient.TaskApiClient;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.codec.BodyCodec;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class AmazonAPI
extends TaskApiClient {
    public String SEC_UA;
    public String offerid;
    public String UA = Harvester.HARVESTER_UA;
    public CartAPI cartAPI;
    public String asin;

    public MultiMap normalPlaceOrderForm(String string, String string2, String string3, String string4, String string5, String string6, String string7, String string8, String string9, String string10, String string11, String string12, String string13, String string14, String string15, String string16, String string17, String string18, String string19, String string20, String string21, String string22, String string23, String string24, String string25, String string26, String string27, String string28, String string29, String string30, String string31, String string32, String string33, String string34, String string35, String string36, String string37, String string38, String string39, String string40, String string41, String string42, String string43, String string44) {
        MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
        multiMap.set("csrfToken", string);
        multiMap.set("fromAnywhere", "0");
        multiMap.set("redirectOnSuccess", "0");
        multiMap.set("purchaseTotal", string2);
        multiMap.set("purchaseTotalCurrency", string3);
        multiMap.set("purchaseID", string4);
        multiMap.set("purchaseCustomerId", string5);
        multiMap.set("useCtb", string6);
        multiMap.set("scopeId", string7);
        multiMap.set("isQuantityInvariant", string8);
        multiMap.set("promiseTime-0", string9);
        multiMap.set("promiseAsin-0", string10);
        multiMap.set("selectedPaymentPaystationId", string11);
        multiMap.set("submitFromSPC", "1");
        multiMap.set("pickupType", "All");
        multiMap.set("searchCriterion", "storeZip");
        multiMap.set("storeZip", "Example: 98144");
        multiMap.set("storeZip2", "0034");
        multiMap.set("searchLockerFormAction", "/gp/buy/storeaddress/handlers/search.html/ref=ox_spc_shipaddr_pickupsearch_popover");
        multiMap.set("claimCode", "");
        multiMap.set("primeMembershipTestData", "NULL");
        multiMap.set("fasttrackExpiration", string12);
        multiMap.set("countdownThreshold", string13);
        multiMap.set("countdownId", string14);
        multiMap.set("showSimplifiedCountdown", string15);
        multiMap.set("gift-message-text", "");
        multiMap.set("dupOrderCheckArgs", string16);
        multiMap.set("order0", string17);
        multiMap.set("shippingofferingid0.1", string18);
        multiMap.set("guaranteetype0.1", string19);
        multiMap.set("issss0.1", string20);
        multiMap.set("shipsplitpriority0.1", string21);
        multiMap.set("isShipWhenCompleteValid0.1", string22);
        multiMap.set("isShipWheneverValid0.1", string23);
        multiMap.set("shippingofferingid0.2", string24);
        multiMap.set("guaranteetype0.2", string25);
        multiMap.set("issss0.2", string26);
        multiMap.set("shipsplitpriority0.2", string27);
        multiMap.set("isShipWhenCompleteValid0.2", string28);
        multiMap.set("isShipWheneverValid0.2", string29);
        multiMap.set("previousshippingofferingid0", string30);
        multiMap.set("previousguaranteetype0", string31);
        multiMap.set("previousissss0", string32);
        multiMap.set("previousshippriority0", string33);
        multiMap.set("lineitemids0", string34);
        multiMap.set("currentshippingspeed", string35);
        multiMap.set("previousShippingSpeed0", string36);
        multiMap.set("currentshipsplitpreference", string37);
        multiMap.set("shippriority.0.shipWhenComplete", string38);
        multiMap.set("groupcount", string39);
        multiMap.set("snsUpsellTotalCount", string40);
        multiMap.set("onmlUpsellSuppressedCount", string41);
        multiMap.set("vasClaimBasedModel", string42);
        multiMap.set("shiptrialprefix", string43);
        multiMap.set("isfirsttimecustomer", string44);
        multiMap.set("isTFXEligible", "");
        multiMap.set("isFxEnabled", "");
        multiMap.set("isFXTncShown", "");
        multiMap.set("hasWorkingJavascript", "1");
        multiMap.set("placeYourOrder1", "1");
        return multiMap;
    }

    public HttpRequest normalPlaceOrder() {
        HttpRequest httpRequest = this.client.postAbs("https://www.amazon.com/gp/buy/spc/handlers/static-submit-decoupled.html/ref=ox_spc_place_order?ie=UTF8&hasWorkingJavascript=").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("cache-control", "max-age=0");
        httpRequest.putHeader("rtt", "0");
        httpRequest.putHeader("downlink", "10");
        httpRequest.putHeader("ect", "4g");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("origin", "https://www.amazon.com");
        httpRequest.putHeader("content-type", "application/x-www-form-urlencoded");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("referer", "https://www.amazon.com/gp/buy/spc/handlers/display.html?hasWorkingJavascript=1");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest checkPayments() {
        HttpRequest httpRequest = this.client.getAbs("https://www.amazon.com/cpe/yourpayments/wallet?ref_=ya_d_c_pmt_mpo").as(BodyCodec.buffer());
        httpRequest.putHeader("rtt", "50");
        httpRequest.putHeader("downlink", "10");
        httpRequest.putHeader("ect", "4g");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public MultiMap buynowPlaceOrderForm(String string, String string2) {
        MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
        multiMap.set("x-amz-checkout-csrf-token", this.getCookies().getCookieValue("session-id"));
        multiMap.set("ref_", "chk_spc_placeOrder");
        multiMap.set("referrer", "spc");
        multiMap.set("pid", string);
        multiMap.set("pipelineType", "turbo");
        multiMap.set("clientId", "retailwebsite");
        multiMap.set("temporaryAddToCart", "1");
        multiMap.set("hostPage", "detail");
        multiMap.set("weblab", string2);
        multiMap.set("isClientTimeBased", "1");
        return multiMap;
    }

    public HttpRequest productPage() {
        HttpRequest httpRequest = this.client.getAbs("https://www.amazon.com/dp/" + this.asin).as(BodyCodec.buffer());
        httpRequest.putHeader("rtt", "0");
        httpRequest.putHeader("downlink", "10");
        httpRequest.putHeader("ect", "4g");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "none");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest proceedToCheckout() {
        HttpRequest httpRequest = this.client.getAbs("https://www.amazon.com/gp/cart/desktop/go-to-checkout.html/ref=ox_sw_proceed?proceedToRetailCheckout=Proceed+to+checkout&proceedToCheckout=1").as(BodyCodec.buffer());
        httpRequest.putHeader("rtt", "0");
        httpRequest.putHeader("downlink", "10");
        httpRequest.putHeader("ect", "4g");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("referer", "https://www.amazon.com/gp/cart/view.html?ref_=nav_cart");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest oneClickSettings() {
        HttpRequest httpRequest = this.client.getAbs("https://www.amazon.com/cpe/yourpayments/settings/manageoneclick?ref_=ya_d_l_change_1_click").as(BodyCodec.buffer());
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("rtt", "50");
        httpRequest.putHeader("downlink", "10");
        httpRequest.putHeader("ect", "4g");
        httpRequest.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"96\", \"Google Chrome\";v=\"96\"");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("referer", "https://www.amazon.com/gp/css/homepage.html?ref_=nav_AccountFlyout_ya");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public void updateGlow(String string) {
        this.cartAPI.glow = string;
    }

    public HttpRequest homepage() {
        HttpRequest httpRequest = this.client.getAbs("https://www.amazon.com/").as(BodyCodec.buffer());
        httpRequest.putHeader("cache-control", "max-age=0");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "none");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public Pair switchOneClick(boolean bl) {
        HttpRequest httpRequest = this.client.postAbs("https://www.amazon.com/payments-portal/data/widgets2/v1/customer/A20OQ6KNAZ121S/continueWidget").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"96\", \"Google Chrome\";v=\"96\"");
        httpRequest.putHeader("widget-ajax-attempt-count", "0");
        httpRequest.putHeader("rtt", "50");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.45 Safari/537.36");
        httpRequest.putHeader("content-type", "application/x-www-form-urlencoded; charset=UTF-8");
        httpRequest.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
        httpRequest.putHeader("x-requested-with", "XMLHttpRequest");
        httpRequest.putHeader("downlink", "10");
        httpRequest.putHeader("ect", "4g");
        httpRequest.putHeader("apx-widget-info", "YA:OneClick/desktop/aEIgBZpv2WA4");
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("origin", "https://www.amazon.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.amazon.com/cpe/yourpayments/settings/manageoneclick?ref_=ya_d_l_change_1_click");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
        multiMap.set("ppw-jsEnabled", "true");
        multiMap.set("ppw-ubid", "134-4537862-7254723");
        multiMap.set("ppw-isOneClickEnabled", "" + bl);
        multiMap.set("ppw-widgetEvent", "UpdateBrowserOneClickStatusEvent");
        multiMap.set("ppw-widgetState", "4-MS0kQ5IIeTNT3tYvVeZAeZ1ICOdgjvL_w5PxLFrgsrvSHdg1JrSwljfFuZUp1b8ynJNfTL1OVnY0pVD2DM_baAweH5twH1b7-hL9ry3cFnz0iLaoMP3igREgNUa2ZlL_wF8qeI7VmtMiEyw3xhQ1s-1yhgi5rY5sW5NRBwzgqEYAxW9LOYUFjSeY60fn8TE2Twaxb3R3IHitDmNOUxmdgbS7FmSRw5bHazONjIRVQm_jdD8m9-MJ57DCcaUfcrk64XNgiEy2rbANb5zmUhaSkm9tJA0TZQckcHxqTicUhLixV8HvSl6QGrhwZSBg1dXUcjgtPHRBjLNSz_aGWjD9SoM8HiwaEbFGK4nJ5nZ5khVhKXZezPyOtoDuyC8Kqj-o6-_PmIERnC4QRFwpqRFUu89IeS9OxkqRt34c-uyuW6zBRIuVWiLJQEOyMoRKA6szFsGaMC8RIirS_LgNQzU2Y4vW8Z_rP6IGJWCubNSg8yj4JtE3sVoYXdYjz26wk1qkM0Z_itMlX9nOmEJLWlxm0Q-0uJmGpPSn7VlqLx4lHCAkrZiEdUBs74q_h08h_WBbm17SwypB8DN3Gzq7k8QFoqWl6aBsR7kOYmofXlkaJ_te22TcFibVyjdhKgQ70RlhpHufYuoUZsMZBxfilEP4oqcUIX2STlEuIUd-RuX_pRWkbCU3DAEt6PPPJ21-FCymQLwWc4xMMFIOQf0zElE-6GYb0YeEabOwCCE4XsxHa3qd6rYCRifS2LA5AyJDONZZMH76FhFErCYor9xG76hqkCMTUezekObK1j5tw_71eihvYEx8doGfXemQYDYA4pDqoiArWTLHnN8NS-DffzjduR8WyL-fvE8cwP_suoIgX2FZc2KSNptCPo4_XMmU9y3aleTJjjF6lfTm2c6M7aljAWhW4CcztENILD3Ik8TRel-3akY51WpLZ6p9Ql5O-7Uuq1rm62-LfeFdwesXseS43piRFzAtE6dwjLJ7BGVIjQKHhVlDgsXQIs8uvboDr4oCIMHOc9iHMfZ-_e_igRYWnYrLhpMAutikbOb8TN9dArHpy7sA_vwGar8AJVLp");
        return new Pair((Object)httpRequest, (Object)multiMap);
    }

    public void genCsm(String string) {
        this.cartAPI.rid = string;
        this.getCookies().put("csm-hit", "tb:s-" + string + "|" + System.currentTimeMillis() + "&t:" + (System.currentTimeMillis() + (long)ThreadLocalRandom.current().nextInt(1800, 2500)) + "&adb:adblk_no", "www.amazon.com");
    }

    public HttpRequest loginPage() {
        HttpRequest httpRequest = this.client.getAbs("https://www.amazon.com/ap/signin?openid.pape.max_auth_age=0&openid.return_to=https://www.amazon.com/?ref_=nav_signin&openid.identity=http://specs.openid.net/auth/2.0/identifier_select&openid.assoc_handle=usflex&openid.mode=checkid_setup&openid.claimed_id=http://specs.openid.net/auth/2.0/identifier_select&openid.ns=http://specs.openid.net/auth/2.0&").as(BodyCodec.buffer());
        httpRequest.putHeader("rtt", "50");
        httpRequest.putHeader("downlink", "10");
        httpRequest.putHeader("ect", "4g");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("referer", "https://www.amazon.com/");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest buynowPlaceOrder(String string, String string2, String string3) {
        HttpRequest httpRequest = this.client.postAbs("https://www.amazon.com/checkout/spc/place-order?ref_=chk_spc_placeOrder&_srcRID=" + string2 + "&clientId=retailwebsite&pipelineType=turbo&cachebuster=" + Instant.now().toEpochMilli() + "&pid=" + string3).as(BodyCodec.none());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("x-amz-checkout-entry-referer-url", "https://www.amazon.com/Geilienergy-600mAh-Rechargeable-Batteries-Remotes/dp/" + this.asin + "/ref=pd_rhf_sc_s_sspa_dk_rhf_cart_pt_sub_1_5/" + this.getCookies().getCookieValue("session-id") + "?_encoding=UTF8&pd_rd_i=" + this.asin + "&pd_rd_r=" + UUID.randomUUID());
        httpRequest.putHeader("anti-csrftoken-a2z", string);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("content-type", "application/x-www-form-urlencoded");
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("x-requested-with", "XMLHttpRequest");
        httpRequest.putHeader("origin", "https://www.amazon.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.amazon.com/checkout/spc?pid=" + string3 + "&pipelineType=turbo&clientId=retailwebsite&temporaryAddToCart=1&hostPage=detail&weblab=RCX_CHECKOUT_TURBO_DESKTOP_NONPRIME_87784");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public JsonObject mobileHomepageBody() {
        return new JsonObject().put("input", (Object)new JsonObject().put("applicationStartCount", (Object)1).put("applicationStarted", (Object)true).put("crashInfo", null));
    }

    public HttpRequest submitDecoupled() {
        HttpRequest httpRequest = this.client.postAbs("https://www.amazon.com/gp/buy/spc/handlers/static-submit-decoupled.html").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("rtt", "50");
        httpRequest.putHeader("downlink", "10");
        httpRequest.putHeader("ect", "4g");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("origin", "https://www.amazon.com");
        httpRequest.putHeader("content-type", "application/x-www-form-urlencoded");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("referer", "https://www.amazon.com/gp/buy/spc/handlers/display.html?hasWorkingJavascript=1");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest prefetch(String string) {
        HttpRequest httpRequest = this.client.getAbs(string).as(BodyCodec.buffer());
        httpRequest.putHeader("rtt", "0");
        httpRequest.putHeader("downlink", "10");
        httpRequest.putHeader("ect", "4g");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-dest", "iframe");
        httpRequest.putHeader("referer", "https://www.amazon.com/gp/cart/view.html?ref_=nav_cart");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest finalPage() {
        HttpRequest httpRequest = this.client.getAbs("https://www.amazon.com/gp/buy/spc/handlers/display.html?hasWorkingJavascript=1").as(BodyCodec.buffer());
        httpRequest.putHeader("rtt", "0");
        httpRequest.putHeader("downlink", "10");
        httpRequest.putHeader("ect", "4g");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "none");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest corsairPage() {
        HttpRequest httpRequest = this.client.getAbs("https://www.amazon.com/stores/page/FD66DB94-4F25-4C7F-A441-A4F5A73390D2?ingress=0&visitId=" + UUID.randomUUID() + "&channel=discovbar?field-lbr_brands_browse-bin=AmazonBasics&ref_=nav_cs_amazonbasics_e625fdf6288e43d49a4983e612a8360d").as(BodyCodec.buffer());
        httpRequest.putHeader("rtt", "50");
        httpRequest.putHeader("downlink", "10");
        httpRequest.putHeader("ect", "4g");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "cross-site");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("referer", "https://www.google.com/");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest mobileHomepage() {
        this.client.cookieStore().put("lc-main", "en_US", ".amazon.com");
        HttpRequest httpRequest = this.client.postAbs("https://mag-na.amazon.com/mShop/mj/home").as(BodyCodec.buffer());
        httpRequest.putHeader("Host", "mag-na.amazon.com");
        httpRequest.putHeader("Accept", "*/*");
        httpRequest.putHeader("Content-Type", "application/json; charset=utf-8");
        httpRequest.putHeader("Connection", "keep-alive");
        httpRequest.putHeader("x-amz-msh-appid", "name=Amazon;ver=17.15.0;device=iPhone;os=iPhone OS 13.6.1;UDID=" + UUID.randomUUID().toString().toUpperCase() + ";mp=US;locale=en_US");
        httpRequest.putHeader("Cookie", "DEFAULT_VALUE");
        httpRequest.putHeader("Accept-Language", "en-us");
        httpRequest.putHeader("Content-Length", "DEFAULT_VALUE");
        httpRequest.putHeader("Accept-Encoding", "gzip, deflate, br");
        httpRequest.putHeader("User-Agent", "Amazon/367489.0 CFNetwork/1128.0.1 Darwin/19.6.0");
        return httpRequest;
    }

    public void updateCsrf(String string) {
        this.cartAPI.csrf = string;
    }

    public HttpRequest promoPage() {
        HttpRequest httpRequest = this.client.getAbs("https://www.amazon.com/Trconk-Screen-Protector-Compatible-Nintendo-Tempered/dp/B09GM7K378/ref=rvi_4/147-7537293-1503560?pd_rd_w=aTk94&pf_rd_p=f5690a4d-f2bb-45d9-9d1b-736fee412437&pf_rd_r=ADYWY1JNPA9R4KY2SMJM&pd_rd_r=024f351d-7b89-4491-8b70-c9ebe7f9ec37&pd_rd_wg=B1RTv&pd_rd_i=B09GM7K378&th=1").as(BodyCodec.buffer());
        httpRequest.putHeader("rtt", "0");
        httpRequest.putHeader("downlink", "10");
        httpRequest.putHeader("ect", "4g");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "none");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest cartPage() {
        HttpRequest httpRequest = this.client.getAbs("https://www.amazon.com/gp/cart/view.html?ref_=nav_cart").as(BodyCodec.buffer());
        httpRequest.putHeader("rtt", "0");
        httpRequest.putHeader("downlink", "10");
        httpRequest.putHeader("ect", "4g");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("referer", "https://www.amazon.com/s?k=toy&ref=nb_sb_noss_2");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public HttpRequest walletPage() {
        HttpRequest httpRequest = this.client.getAbs("https://www.amazon.com/cpe/yourpayments/wallet?ref_=ya_d_c_pmt_mpo").as(BodyCodec.buffer());
        httpRequest.putHeader("rtt", "50");
        httpRequest.putHeader("downlink", "10");
        httpRequest.putHeader("ect", "4g");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public AmazonAPI(Task task) {
        String string = ShopifyAPI.parseChromeVer((String)this.UA);
        this.SEC_UA = "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"" + string + "\", \"Google Chrome\";v=\"" + string + "\"";
        this.asin = task.getKeywords()[0];
        this.offerid = task.getKeywords()[1];
        this.cartAPI = new CartAPI(this);
    }

    public HttpRequest placeOrderExp() {
        HttpRequest httpRequest = this.client.getAbs("https://www.amazon.com/gp/buy/spc/handlers/place-your-decoupled-order.html").as(BodyCodec.buffer());
        httpRequest.putHeader("rtt", "50");
        httpRequest.putHeader("downlink", "10");
        httpRequest.putHeader("ect", "4g");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("origin", "https://www.amazon.com");
        httpRequest.putHeader("content-type", "application/x-www-form-urlencoded");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("referer", "https://www.amazon.com/gp/buy/spc/handlers/display.html?hasWorkingJavascript=1");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public MultiMap submitDecoupledForm(String string) {
        MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
        multiMap.set("csrfToken", this.cartAPI.csrf);
        multiMap.set("purchaseID", string);
        multiMap.set("forcePlaceOrder", "Place this duplicate order");
        multiMap.set("pipelineType", "spc");
        return multiMap;
    }

    public HttpRequest naturalFinalPage() {
        HttpRequest httpRequest = this.client.getAbs("https://www.amazon.com/gp/cart/view.html/ref=lh_co?ie=UTF8&proceedToCheckout.x=129&cartInitiateId=" + System.currentTimeMillis() + "&hasWorkingJavascript=1").as(BodyCodec.buffer());
        httpRequest.putHeader("rtt", "0");
        httpRequest.putHeader("downlink", "10");
        httpRequest.putHeader("ect", "4g");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("referer", "https://www.amazon.com/gp/huc/view.html?ie=UTF8&newItems=" + UUID.randomUUID() + "%2C1");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }
}
