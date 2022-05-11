/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.task.sites.amazon.AmazonAPI
 *  io.trickle.task.sites.shopify.util.Triplet
 *  io.trickle.util.Pair
 *  io.trickle.util.Utils
 *  io.trickle.webclient.RealClient
 *  io.vertx.core.MultiMap
 *  io.vertx.core.buffer.Buffer
 *  io.vertx.core.json.JsonArray
 *  io.vertx.core.json.JsonObject
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.codec.BodyCodec
 */
package io.trickle.task.sites.amazon;

import io.trickle.task.sites.amazon.AmazonAPI;
import io.trickle.task.sites.shopify.util.Triplet;
import io.trickle.util.Pair;
import io.trickle.util.Utils;
import io.trickle.webclient.RealClient;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.codec.BodyCodec;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public class CartAPI {
    public String glow;
    public String asin;
    public String SEC_UA;
    public String UA;
    public Supplier<Triplet<HttpRequest<Buffer>, Object, Boolean>>[] endpoints = new Supplier[]{this::promo};
    public String rid;
    public String apiCsrf;
    public RealClient client;
    public String csrf;
    public String anti_csrf;
    public String addressID;
    public int endpointPosition = 0;
    public String offerid;

    public Triplet basics() {
        HttpRequest httpRequest = this.client.postAbs("https://data.amazon.com/api/marketplaces/ATVPDKIKX0DER/cart/carts/retail/items?ref_=ast_sto_atc").as(BodyCodec.buffer());
        httpRequest.putHeader("Host", "data.amazon.com");
        httpRequest.putHeader("Connection", "keep-alive");
        httpRequest.putHeader("Content-Length", "DEFAULT_VALUE");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("x-api-csrf-token", this.apiCsrf);
        httpRequest.putHeader("Accept-Language", "en-US");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("User-Agent", this.UA);
        httpRequest.putHeader("Content-Type", "application/vnd.com.amazon.api+json; type=\"cart.add-items.request/v1\"");
        httpRequest.putHeader("Accept", "application/vnd.com.amazon.api+json; type=\"cart.add-items/v1\"");
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("Origin", "https://www.amazon.com");
        httpRequest.putHeader("Sec-Fetch-Site", "same-site");
        httpRequest.putHeader("Sec-Fetch-Mode", "cors");
        httpRequest.putHeader("Sec-Fetch-Dest", "empty");
        httpRequest.putHeader("Referer", "https://www.amazon.com/");
        httpRequest.putHeader("Accept-Encoding", "gzip, deflate, br");
        httpRequest.putHeader("Cookie", "DEFAULT_VALUE");
        JsonObject jsonObject = new JsonObject();
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.put("asin", (Object)this.asin);
        jsonObject2.put("offerListingId", (Object)this.offerid);
        jsonObject2.put("quantity", (Object)1);
        jsonObject.put("items", (Object)new JsonArray().add((Object)jsonObject2));
        return new Triplet((Object)httpRequest, (Object)jsonObject, (Object)false);
    }

    public Pair clearItem(String string, String string2, String string3, String string4) {
        HttpRequest httpRequest = this.client.postAbs("https://www.amazon.com/gp/cart/ajax-update.html/ref=ox_sc_cart_actions_1").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("x-aui-view", "Desktop");
        httpRequest.putHeader("rtt", "50");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("content-type", "application/x-www-form-urlencoded; charset=UTF-8;");
        httpRequest.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
        httpRequest.putHeader("x-requested-with", "XMLHttpRequest");
        httpRequest.putHeader("downlink", "10");
        httpRequest.putHeader("ect", "4g");
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("origin", "https://www.amazon.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.amazon.com/gp/cart/view.html?ref_=nav_cart");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
        multiMap.set("hasMoreItems", "false");
        multiMap.set("timeStamp", String.valueOf(System.currentTimeMillis() / 1000L));
        multiMap.set("requestID", this.rid);
        multiMap.set("token", this.glow);
        multiMap.set("activeItems", string + "|1|0|" + string3 + "|" + string4 + "|||0|||1");
        multiMap.set("addressId", "");
        multiMap.set("addressZip", "");
        multiMap.set("closeAddonUpsell", "1");
        multiMap.set("submit.cart-actions", "1");
        multiMap.set("pageAction", "cart-actions");
        multiMap.set("actionPayload", "[{\"type\":\"DELETE_START\",\"payload\":{\"itemId\":\"" + string2 + "\",\"list\":\"activeItems\",\"relatedItemIds\":[],\"isPrimeAsin\":false}}]");
        multiMap.set("displayedSavedItemNum", "0");
        return new Pair((Object)httpRequest, (Object)multiMap);
    }

    public Triplet buynowPortal() {
        HttpRequest httpRequest = this.client.getAbs("https://www.amazon.com/gp/checkoutportal/enter-checkout.html?ie=UTF8&asin=" + this.asin + "&buyNow=1&cartCustomerID=0&fromSignIn=1&fulfillmentType=&isBuyBack=&isGift=0&offeringID=" + this.offerid + "&pickupAddressId=&pickupStoreChainId=&purchaseInputs=HASH%280xaa21e618%29&quantity=1&sessionID=" + this.client.cookieStore().getCookieValue("session-id") + "&pageId=amazon_checkout_us&showRmrMe=0&siteState=IMBMsgs.&suppressSignInRadioButtons=0").as(BodyCodec.buffer());
        httpRequest.putHeader("cache-control", "max-age=0");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("rtt", "50");
        httpRequest.putHeader("downlink", "10");
        httpRequest.putHeader("ect", "4g");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("referer", "https://www.amazon.com/ap/signin");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return new Triplet((Object)httpRequest, null, (Object)true);
    }

    public Triplet csrf() {
        HttpRequest httpRequest = this.client.postAbs("https://www.amazon.com/gp/huc/csrf-add.html?ref_=pd_luc_rh_crh_rh_cps_03_04_atc_lh&ie=UTF8&from=HUC&quantity.1=1&session-id=" + this.client.cookieStore().getCookieValue("session-id") + "&offeringID.1=" + this.offerid).as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("cache-control", "max-age=0");
        httpRequest.putHeader("rtt", "50");
        httpRequest.putHeader("downlink", "10");
        httpRequest.putHeader("ect", "4g");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("origin", "https://www.amazon.com");
        httpRequest.putHeader("content-type", "application/x-www-form-urlencoded");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("referer", "https://www.amazon.com/gp/huc/view.html?ie=UTF8&increasedItems=" + UUID.randomUUID() + "&newItems=" + UUID.randomUUID() + "%2C1");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
        multiMap.set("asin", this.asin);
        multiMap.set("clickType", "AddToCart");
        multiMap.set("col", "4");
        multiMap.set("row", "3");
        multiMap.set("discoveredAsins.1", this.asin);
        multiMap.set("previousRID", this.rid);
        multiMap.set("widgetName", "desktop-huc-carousels_huc-crossproduct-sims-scf");
        multiMap.set("token", this.glow);
        multiMap.set("currentArgs", "nodeID=&offeringID.1=" + this.offerid + "&CSRF=" + this.csrf + "&itemCount=1&session-id=" + this.client.cookieStore().getCookieValue("session-id") + "&signInToHUC=0&ref_=psdc_" + System.currentTimeMillis() / 1000L + "_a0_" + this.asin + "&asin=" + this.asin + "&submit.addToCart=addToCart");
        multiMap.set("submit.addToCart", "Submit");
        return new Triplet((Object)httpRequest, (Object)multiMap, (Object)false);
    }

    public CartAPI(AmazonAPI amazonAPI) {
        this.client = amazonAPI.getWebClient();
        this.offerid = amazonAPI.offerid;
        this.asin = amazonAPI.asin;
        this.UA = amazonAPI.UA;
        this.SEC_UA = amazonAPI.SEC_UA;
    }

    public Triplet stel() {
        HttpRequest httpRequest = this.client.getAbs("https://www.amazon.com/checkout/initiate?referrer=" + ThreadLocalRandom.current().nextInt(100, 2000) + "&pipelineType=" + (ThreadLocalRandom.current().nextBoolean() ? "spc" : "turbo") + "&clientId=retailsite&temporaryAddToCart=1&isAsync=1&addressID=nhlgqunorokq&offerListing.1=" + this.offerid + "&quantity.1=1").as(BodyCodec.buffer());
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("rtt", "0");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("downlink", "10");
        httpRequest.putHeader("ect", "4g");
        httpRequest.putHeader("origin", "https://www.amazon.com");
        httpRequest.putHeader("sec-fetch-site", "none");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("referer", "https://www.amazon.com/");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return new Triplet((Object)httpRequest, null, (Object)true);
    }

    public Triplet mobileRecommended() {
        HttpRequest httpRequest = this.client.postAbs("https://www.amazon.com/gp/aw/detail/ajax/add-to-cart/ref=mw_pa_dp_buy_crt").as(BodyCodec.buffer());
        httpRequest.putHeader("content-type", "application/x-www-form-urlencoded");
        httpRequest.putHeader("accept", "text/html,*/*");
        httpRequest.putHeader("x-requested-with", "XMLHttpRequest");
        httpRequest.putHeader("accept-language", "en-us");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("origin", "https://www.amazon.com");
        httpRequest.putHeader("user-agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 13_6_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148");
        httpRequest.putHeader("referer", "https://www.amazon.com/gp/product/" + this.asin + "/ref=sr_1_4?dchild=1&keywords=ps5&qid=" + System.currentTimeMillis() / 1000L + "&sr=8-4");
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
        multiMap.set("verificationSessionId", this.client.cookieStore().getCookieValue("session-id"));
        multiMap.set("a", "asin");
        multiMap.set("quantity", "1");
        multiMap.set("canShowHighUpsellCart", "1");
        multiMap.set("oid", this.offerid);
        multiMap.set("verificationSessionID", this.client.cookieStore().getCookieValue("session-id"));
        return new Triplet((Object)httpRequest, (Object)multiMap, (Object)false);
    }

    public Triplet buyBoxRegular() {
        HttpRequest httpRequest = this.client.postAbs("https://www.amazon.com/gp/product/handle-buy-box/ref=dp_start-bbf_1_glance").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("cache-control", "max-age=0");
        httpRequest.putHeader("rtt", "0");
        httpRequest.putHeader("downlink", "10");
        httpRequest.putHeader("ect", "4g");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("origin", "https://www.amazon.com");
        httpRequest.putHeader("content-type", "application/x-www-form-urlencoded");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("referer", "https://www.amazon.com/ORIbox-Protective-Case-iPhone-pro/dp/" + this.asin + "/");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
        multiMap.set("CSRF", this.csrf);
        multiMap.set("anti-csrftoken-a2z", "");
        multiMap.set("offerListingID", this.offerid);
        multiMap.set("session-id", this.client.cookieStore().getCookieValue("session-id"));
        multiMap.set("ASIN", this.asin);
        multiMap.set("isMerchantExclusive", "0");
        multiMap.set("merchantID", "ATVPDKIKX0DER");
        multiMap.set("isAddon", "0");
        multiMap.set("nodeID", "");
        multiMap.set("sellingCustomerID", "");
        multiMap.set("qid", String.valueOf(System.currentTimeMillis() / 1000L));
        multiMap.set("sr", "8-1-spons");
        multiMap.set("storeID", "");
        multiMap.set("tagActionCode", "");
        multiMap.set("viewID", "glance");
        multiMap.set("rebateId", "");
        multiMap.set("ctaDeviceType", "desktop");
        multiMap.set("ctaPageType", "detail");
        multiMap.set("usePrimeHandler", "0");
        multiMap.set("rsid", this.client.cookieStore().getCookieValue("session-id"));
        multiMap.set("sourceCustomerOrgListID", "");
        multiMap.set("sourceCustomerOrgListItemID", "");
        multiMap.set("wlPopCommand", "");
        multiMap.set("quantity", "1");
        multiMap.set("submit.add-to-cart", "Add+to+Cart");
        multiMap.set("dropdown-selection", "add-new");
        multiMap.set("dropdown-selection-ubb", "add-new");
        return new Triplet((Object)httpRequest, (Object)multiMap, (Object)false);
    }

    public Supplier smartEndpoint() {
        int n = this.endpointPosition++;
        return this.endpoints[n % this.endpoints.length];
    }

    public Triplet reOrder() {
        HttpRequest httpRequest = this.client.getAbs("https://www.amazon.com/gp/add-to-cart/json/ref=pd_bap_d_rp_atc_1?ie=UTF8&pd_rd_i=" + this.asin + "&pd_rd_w=83xRG&pf_rd_p=" + UUID.randomUUID() + "&pf_rd_r=9XFJ0RNPFCASXYRPZ0NK&pd_rd_r=" + UUID.randomUUID() + "&pd_rd_wg=1GltB&ASIN=" + this.asin + "&clientName=P13NRecommendations&discoveredAsins.1=" + this.asin + "&offerListingID=" + this.offerid + "&quantity=1&verificationSessionID=" + this.client.cookieStore().getCookieValue("session-id")).as(BodyCodec.buffer());
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("rtt", "50");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("accept", "text/html,*/*");
        httpRequest.putHeader("x-requested-with", "XMLHttpRequest");
        httpRequest.putHeader("downlink", "10");
        httpRequest.putHeader("ect", "4g");
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.amazon.com/gp/buyagain/ref=ppx_yo_dt_b_bia");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return new Triplet((Object)httpRequest, null, (Object)false);
    }

    public void setWorkingEndpoint(int n) {
        this.endpointPosition = n - 1;
    }

    public Triplet buyAgain() {
        HttpRequest httpRequest = this.client.postAbs("https://www.amazon.com/gp/cart/ajax-update.html/ref=ox_sc_rp_item_rp_atc_1").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("x-aui-view", "Desktop");
        httpRequest.putHeader("rtt", "0");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("content-type", "application/x-www-form-urlencoded; charset=UTF-8;");
        httpRequest.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
        httpRequest.putHeader("x-requested-with", "XMLHttpRequest");
        httpRequest.putHeader("downlink", "10");
        httpRequest.putHeader("ect", "4g");
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("origin", "https://www.amazon.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.amazon.com/gp/cart/view.html?ref_=nav_cart");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
        multiMap.set("hasMoreItems", "false");
        multiMap.set("timeStamp", String.valueOf(System.currentTimeMillis() / 1000L));
        multiMap.set("requestID", this.rid);
        multiMap.set("token", this.glow);
        multiMap.set("addressId", "");
        multiMap.set("addressZip", "");
        multiMap.set("reloadAddonUpsellWidget", "1");
        multiMap.set("submit.add-to-cart", "1");
        multiMap.set("encodedOffering", this.offerid);
        multiMap.set("pageAction", "AddToCart");
        return new Triplet((Object)httpRequest, (Object)multiMap, (Object)false);
    }

    public Triplet mobile() {
        HttpRequest httpRequest = this.client.postAbs("https://www.amazon.com/gp/product/cta/add-to-cart/ref=mw_pa_dp_buy_crt").as(BodyCodec.buffer());
        httpRequest.putHeader("content-type", "application/x-www-form-urlencoded");
        httpRequest.putHeader("accept", "text/html,*/*");
        httpRequest.putHeader("x-requested-with", "XMLHttpRequest");
        httpRequest.putHeader("accept-language", "en-us");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("origin", "https://www.amazon.com");
        httpRequest.putHeader("user-agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 13_6_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148");
        httpRequest.putHeader("referer", "https://www.amazon.com/gp/product/" + this.asin + "/ref=sr_1_3?dchild=1&keywords=toy&qid=" + System.currentTimeMillis() / 1000L + "&sr=8-3");
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        String string = Utils.getRandomString((int)13).toUpperCase(Locale.ROOT);
        MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
        multiMap.set("verificationSessionID", this.client.cookieStore().getCookieValue("session-id"));
        multiMap.set("log-buybox-metrics-quantity.0", "1");
        multiMap.set("log-page-type", "Cart");
        multiMap.set("custom-name.1", "UNIQ-vgcToken");
        multiMap.set("oid", this.offerid);
        multiMap.set("log-is-prime-customer", "N");
        multiMap.set("log-qid", String.valueOf(System.currentTimeMillis() / 1000L));
        multiMap.set("log-buybox-metrics-product-group-id.0", "0");
        multiMap.set("ctaDeviceType", "mobileApp");
        multiMap.set("log-hitType", "pageTouch");
        multiMap.set("log-buybox-metrics-price.0", ThreadLocalRandom.current().nextInt(4, 150) + ".0");
        multiMap.set("log-buybox-metrics-currency.0", "USD");
        multiMap.set("log-buybox-metrics-merchant.0", string);
        multiMap.set("log-buybox-metrics-asin.0", this.asin);
        multiMap.set("log-sr", "8-4");
        multiMap.set("a", this.asin);
        multiMap.set("atcCartWeblab", "");
        multiMap.set("log-page-type-id", this.asin);
        multiMap.set("ctaPageType", "detail");
        multiMap.set("quantity", "1");
        multiMap.set("custom-value.1", "");
        multiMap.set("log-buybox-metrics-seller-customer-id.0", string);
        multiMap.set("o", "add");
        multiMap.set("log-is-prime-asin", "N");
        multiMap.set("coliid", "");
        multiMap.set("colid", "");
        multiMap.set("rebateId", "");
        multiMap.set("log-page-action", "AddToCart");
        multiMap.set("canShowHighUpsellCart", "1");
        return new Triplet((Object)httpRequest, (Object)multiMap, (Object)false);
    }

    public Triplet buynow() {
        HttpRequest httpRequest = this.client.postAbs("https://www.amazon.com/gp/product/handle-buy-box/ref=dp_start-bbf_1_glance").as(BodyCodec.buffer());
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
        httpRequest.putHeader("referer", "https://www.amazon.com/gp/product/" + this.asin + "/ref=ox_sc_act_image_1?smid=ATVPDKIKX0DER&psc=1");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
        multiMap.set("CSRF", this.csrf);
        multiMap.set("offerListingID", this.offerid);
        multiMap.set("session-id", this.client.cookieStore().getCookieValue("session-id"));
        multiMap.set("ASIN", this.asin);
        multiMap.set("isMerchantExclusive", "0");
        multiMap.set("merchantID", "ATVPDKIKX0DER");
        multiMap.set("isAddon", "0");
        multiMap.set("nodeID", "");
        multiMap.set("sellingCustomerID", "");
        multiMap.set("qid", "");
        multiMap.set("sr", "");
        multiMap.set("storeID", "");
        multiMap.set("tagActionCode", "");
        multiMap.set("viewID", "glance");
        multiMap.set("rebateId", "");
        multiMap.set("ctaDeviceType", "desktop");
        multiMap.set("ctaPageType", "detail");
        multiMap.set("usePrimeHandler", "0");
        multiMap.set("rsid", this.client.cookieStore().getCookieValue("session-id"));
        multiMap.set("sourceCustomerOrgListID", "");
        multiMap.set("sourceCustomerOrgListItemID", "");
        multiMap.set("wlPopCommand", "");
        multiMap.set("quantity", "1");
        multiMap.set("submit.buy-now", "Submit");
        multiMap.set("dropdown-selection", "add-new");
        multiMap.set("dropdown-selection-ubb", "add-new");
        return new Triplet((Object)httpRequest, (Object)multiMap, (Object)true);
    }

    public Triplet aloha() {
        HttpRequest httpRequest = this.client.postAbs("https://www.amazon.com/gp/product/features/aloha-ppd/udp-ajax-handler/attach-add-to-cart.html").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("rtt", "50");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("content-type", "application/x-www-form-urlencoded");
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("x-requested-with", "XMLHttpRequest");
        httpRequest.putHeader("downlink", "10");
        httpRequest.putHeader("ect", "4g");
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("origin", "https://www.amazon.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.amazon.com/Nerf-Zombie-Strike-Hammershot-Blaster/dp/" + this.asin + "/ref=sr_1_1_sspa");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        String string = Utils.getRandomString((int)13).toUpperCase(Locale.ROOT);
        MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
        multiMap.set("marketplaceId", string);
        multiMap.set("asin", this.asin);
        multiMap.set("customerId", "");
        multiMap.set("sessionId", this.client.cookieStore().getCookieValue("session-id"));
        multiMap.set("accessoryItemAsin", this.asin);
        multiMap.set("accessoryItemOfferingId", this.offerid);
        multiMap.set("languageOfPreference", "en_US");
        multiMap.set("accessoryItemQuantity", "1");
        multiMap.set("accessoryItemPrice", ThreadLocalRandom.current().nextInt(3, 100) + "." + ThreadLocalRandom.current().nextInt(10, 100));
        multiMap.set("accessoryMerchantId", string);
        multiMap.set("accessoryProductGroupId", "2107000");
        return new Triplet((Object)httpRequest, (Object)multiMap, (Object)false);
    }

    public Triplet promo() {
        HttpRequest httpRequest = this.client.postAbs("https://www.amazon.com/promotion/redeem_and_add_both").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("cache-control", "max-age=0");
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
        httpRequest.putHeader("referer", "https://www.amazon.com/Trconk-Screen-Protector-Compatible-Nintendo-Tempered/dp/B09GM7K378/ref=rvi_4/147-7537293-1503560?pd_rd_w=aTk94&pf_rd_p=f5690a4d-f2bb-45d9-9d1b-736fee412437&pf_rd_r=ADYWY1JNPA9R4KY2SMJM&pd_rd_r=024f351d-7b89-4491-8b70-c9ebe7f9ec37&pd_rd_wg=B1RTv&pd_rd_i=B09GM7K378&psc=1");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
        multiMap.set("asin1", this.asin);
        multiMap.set("asin2", Utils.getRandomString((int)10).toUpperCase(Locale.ROOT));
        multiMap.set("offerListingId1", this.offerid);
        multiMap.set("offerListingId2", Utils.getRandomString((int)162) + "%3D%3D");
        multiMap.set("quantity1", "1");
        multiMap.set("quantity2", "0");
        multiMap.set("anti-csrftoken-a2z", this.anti_csrf);
        multiMap.set("session-id", this.client.cookieStore().getCookieValue("session-id"));
        multiMap.set("submit.addToCart", "Submit");
        return new Triplet((Object)httpRequest, (Object)multiMap, (Object)false);
    }

    public Triplet recommendedMulti() {
        String string = Utils.getRandomString((int)20).toUpperCase(Locale.ROOT);
        HttpRequest httpRequest = this.client.postAbs("https://www.amazon.com/gp/item-dispatch/ref=pd_cart_ppd_1_atc_a_1/" + this.client.cookieStore().getCookieValue("session-id") + "?ie=UTF8&pd_rd_r=" + UUID.randomUUID() + "&pd_rd_w=rPq02&pd_rd_wg=K5PnO&pf_rd_p=" + UUID.randomUUID() + "&pf_rd_r=" + string + "&refRID=" + string).as(BodyCodec.buffer());
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
        httpRequest.putHeader("referer", "https://www.amazon.com/gp/cart/view.html?ref_=nav_cart");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
        multiMap.set("session-id", this.client.cookieStore().getCookieValue("session-id"));
        multiMap.set("CSRF", this.csrf);
        multiMap.set("submit.addToCart", "Submit");
        multiMap.set("offeringID.1", this.offerid);
        return new Triplet((Object)httpRequest, (Object)multiMap, (Object)false);
    }

    public Triplet buynowTurbo() {
        HttpRequest httpRequest = this.client.postAbs("https://www.amazon.com/checkout/turbo-initiate?ref_=dp_start-bbf_1_glance_buyNow_4-2&referrer=detail&pipelineType=turbo&clientId=retailwebsite&weblab=RCX_CHECKOUT_TURBO_DESKTOP_NONPRIME_87784&temporaryAddToCart=1&asin.1=" + this.asin).as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("sec-ch-ua", this.SEC_UA);
        httpRequest.putHeader("x-amz-checkout-entry-referer-url", "https://www.amazon.com/Streamlight-22104-Rechargable-Lithium-Flashlights/dp/" + this.asin + "/ref=grr_4/136-5228532-9776240?pd_rd_w=x5TBY&pf_rd_p=4a328350-1c5b-419b-a0a0-4a8c3bfdf6bb&pf_rd_r=RRD0Z1QBX10QPR3847NN&pd_rd_r=b24c8502-87eb-4150-85ca-19727592f03c&pd_rd_wg=LlfH2&pd_rd_i=B088NGD584&psc=1");
        httpRequest.putHeader("x-amz-turbo-checkout-dp-url", "https://www.amazon.com/Streamlight-22104-Rechargable-Lithium-Flashlights/dp/" + this.asin + "/ref=grr_4/136-5228532-9776240?pd_rd_w=x5TBY&pf_rd_p=4a328350-1c5b-419b-a0a0-4a8c3bfdf6bb&pf_rd_r=RRD0Z1QBX10QPR3847NN&pd_rd_r=b24c8502-87eb-4150-85ca-19727592f03c&pd_rd_wg=LlfH2&pd_rd_i=B088NGD584&psc=1");
        httpRequest.putHeader("rtt", "0");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.UA);
        httpRequest.putHeader("content-type", "application/x-www-form-urlencoded");
        httpRequest.putHeader("x-amz-support-custom-signin", "1");
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("x-requested-with", "XMLHttpRequest");
        httpRequest.putHeader("x-amz-checkout-csrf-token", this.client.cookieStore().getCookieValue("session-id"));
        httpRequest.putHeader("downlink", "10");
        httpRequest.putHeader("ect", "4g");
        httpRequest.putHeader("origin", "https://www.amazon.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.amazon.com/Streamlight-22104-Rechargable-Lithium-Flashlights/dp/" + this.asin + "/ref=grr_4");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
        multiMap.set("isAsync", "1");
        multiMap.set("addressID", this.addressID);
        multiMap.set("offerListing.1", this.offerid);
        multiMap.set("quantity.1", "1");
        return new Triplet((Object)httpRequest, (Object)multiMap, null);
    }
}
