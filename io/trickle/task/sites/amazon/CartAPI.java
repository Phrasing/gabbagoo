package io.trickle.task.sites.amazon;

import io.trickle.task.sites.shopify.util.Triplet;
import io.trickle.util.Pair;
import io.trickle.util.Utils;
import io.trickle.webclient.RealClient;
import io.vertx.core.MultiMap;
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
   public Supplier[] endpoints = new Supplier[]{this::promo};
   public String rid;
   public String apiCsrf;
   public RealClient client;
   public String csrf;
   public String anti_csrf;
   public String addressID;
   public int endpointPosition = 0;
   public String offerid;

   public Triplet basics() {
      HttpRequest var1 = this.client.postAbs("https://data.amazon.com/api/marketplaces/ATVPDKIKX0DER/cart/carts/retail/items?ref_=ast_sto_atc").as(BodyCodec.buffer());
      var1.putHeader("Host", "data.amazon.com");
      var1.putHeader("Connection", "keep-alive");
      var1.putHeader("Content-Length", "DEFAULT_VALUE");
      var1.putHeader("sec-ch-ua", this.SEC_UA);
      var1.putHeader("x-api-csrf-token", this.apiCsrf);
      var1.putHeader("Accept-Language", "en-US");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("User-Agent", this.UA);
      var1.putHeader("Content-Type", "application/vnd.com.amazon.api+json; type=\"cart.add-items.request/v1\"");
      var1.putHeader("Accept", "application/vnd.com.amazon.api+json; type=\"cart.add-items/v1\"");
      var1.putHeader("sec-ch-ua-platform", "\"macOS\"");
      var1.putHeader("Origin", "https://www.amazon.com");
      var1.putHeader("Sec-Fetch-Site", "same-site");
      var1.putHeader("Sec-Fetch-Mode", "cors");
      var1.putHeader("Sec-Fetch-Dest", "empty");
      var1.putHeader("Referer", "https://www.amazon.com/");
      var1.putHeader("Accept-Encoding", "gzip, deflate, br");
      var1.putHeader("Cookie", "DEFAULT_VALUE");
      JsonObject var2 = new JsonObject();
      JsonObject var3 = new JsonObject();
      var3.put("asin", this.asin);
      var3.put("offerListingId", this.offerid);
      var3.put("quantity", 1);
      var2.put("items", (new JsonArray()).add(var3));
      return new Triplet(var1, var2, false);
   }

   public Pair clearItem(String var1, String var2, String var3, String var4) {
      HttpRequest var5 = this.client.postAbs("https://www.amazon.com/gp/cart/ajax-update.html/ref=ox_sc_cart_actions_1").as(BodyCodec.buffer());
      var5.putHeader("content-length", "DEFAULT_VALUE");
      var5.putHeader("sec-ch-ua", this.SEC_UA);
      var5.putHeader("x-aui-view", "Desktop");
      var5.putHeader("rtt", "50");
      var5.putHeader("sec-ch-ua-mobile", "?0");
      var5.putHeader("user-agent", this.UA);
      var5.putHeader("content-type", "application/x-www-form-urlencoded; charset=UTF-8;");
      var5.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
      var5.putHeader("x-requested-with", "XMLHttpRequest");
      var5.putHeader("downlink", "10");
      var5.putHeader("ect", "4g");
      var5.putHeader("sec-ch-ua-platform", "\"macOS\"");
      var5.putHeader("origin", "https://www.amazon.com");
      var5.putHeader("sec-fetch-site", "same-origin");
      var5.putHeader("sec-fetch-mode", "cors");
      var5.putHeader("sec-fetch-dest", "empty");
      var5.putHeader("referer", "https://www.amazon.com/gp/cart/view.html?ref_=nav_cart");
      var5.putHeader("accept-encoding", "gzip, deflate, br");
      var5.putHeader("accept-language", "en-US,en;q=0.9");
      MultiMap var6 = MultiMap.caseInsensitiveMultiMap();
      var6.set("hasMoreItems", "false");
      var6.set("timeStamp", String.valueOf(System.currentTimeMillis() / 1000L));
      var6.set("requestID", this.rid);
      var6.set("token", this.glow);
      var6.set("activeItems", var1 + "|1|0|" + var3 + "|" + var4 + "|||0|||1");
      var6.set("addressId", "");
      var6.set("addressZip", "");
      var6.set("closeAddonUpsell", "1");
      var6.set("submit.cart-actions", "1");
      var6.set("pageAction", "cart-actions");
      var6.set("actionPayload", "[{\"type\":\"DELETE_START\",\"payload\":{\"itemId\":\"" + var2 + "\",\"list\":\"activeItems\",\"relatedItemIds\":[],\"isPrimeAsin\":false}}]");
      var6.set("displayedSavedItemNum", "0");
      return new Pair(var5, var6);
   }

   public Triplet buynowPortal() {
      String var10001 = this.asin;
      HttpRequest var1 = this.client.getAbs("https://www.amazon.com/gp/checkoutportal/enter-checkout.html?ie=UTF8&asin=" + var10001 + "&buyNow=1&cartCustomerID=0&fromSignIn=1&fulfillmentType=&isBuyBack=&isGift=0&offeringID=" + this.offerid + "&pickupAddressId=&pickupStoreChainId=&purchaseInputs=HASH%280xaa21e618%29&quantity=1&sessionID=" + this.client.cookieStore().getCookieValue("session-id") + "&pageId=amazon_checkout_us&showRmrMe=0&siteState=IMBMsgs.&suppressSignInRadioButtons=0").as(BodyCodec.buffer());
      var1.putHeader("cache-control", "max-age=0");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("user-agent", this.UA);
      var1.putHeader("accept", "text/html,application/xhtml xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("rtt", "50");
      var1.putHeader("downlink", "10");
      var1.putHeader("ect", "4g");
      var1.putHeader("sec-ch-ua", this.SEC_UA);
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("sec-ch-ua-platform", "\"macOS\"");
      var1.putHeader("referer", "https://www.amazon.com/ap/signin");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return new Triplet(var1, (Object)null, true);
   }

   public Triplet csrf() {
      RealClient var10000 = this.client;
      String var10001 = this.client.cookieStore().getCookieValue("session-id");
      HttpRequest var1 = var10000.postAbs("https://www.amazon.com/gp/huc/csrf-add.html?ref_=pd_luc_rh_crh_rh_cps_03_04_atc_lh&ie=UTF8&from=HUC&quantity.1=1&session-id=" + var10001 + "&offeringID.1=" + this.offerid).as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("cache-control", "max-age=0");
      var1.putHeader("rtt", "50");
      var1.putHeader("downlink", "10");
      var1.putHeader("ect", "4g");
      var1.putHeader("sec-ch-ua", this.SEC_UA);
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("sec-ch-ua-platform", "\"macOS\"");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("origin", "https://www.amazon.com");
      var1.putHeader("content-type", "application/x-www-form-urlencoded");
      var1.putHeader("user-agent", this.UA);
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      UUID var10002 = UUID.randomUUID();
      var1.putHeader("referer", "https://www.amazon.com/gp/huc/view.html?ie=UTF8&increasedItems=" + var10002 + "&newItems=" + UUID.randomUUID() + "%2C1");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      MultiMap var2 = MultiMap.caseInsensitiveMultiMap();
      var2.set("asin", this.asin);
      var2.set("clickType", "AddToCart");
      var2.set("col", "4");
      var2.set("row", "3");
      var2.set("discoveredAsins.1", this.asin);
      var2.set("previousRID", this.rid);
      var2.set("widgetName", "desktop-huc-carousels_huc-crossproduct-sims-scf");
      var2.set("token", this.glow);
      String var3 = this.offerid;
      var2.set("currentArgs", "nodeID=&offeringID.1=" + var3 + "&CSRF=" + this.csrf + "&itemCount=1&session-id=" + this.client.cookieStore().getCookieValue("session-id") + "&signInToHUC=0&ref_=psdc_" + System.currentTimeMillis() / 1000L + "_a0_" + this.asin + "&asin=" + this.asin + "&submit.addToCart=addToCart");
      var2.set("submit.addToCart", "Submit");
      return new Triplet(var1, var2, false);
   }

   public CartAPI(AmazonAPI var1) {
      this.client = var1.getWebClient();
      this.offerid = var1.offerid;
      this.asin = var1.asin;
      this.UA = var1.UA;
      this.SEC_UA = var1.SEC_UA;
   }

   public Triplet stel() {
      RealClient var10000 = this.client;
      int var10001 = ThreadLocalRandom.current().nextInt(100, 2000);
      HttpRequest var1 = var10000.getAbs("https://www.amazon.com/checkout/initiate?referrer=" + var10001 + "&pipelineType=" + (ThreadLocalRandom.current().nextBoolean() ? "spc" : "turbo") + "&clientId=retailsite&temporaryAddToCart=1&isAsync=1&addressID=nhlgqunorokq&offerListing.1=" + this.offerid + "&quantity.1=1").as(BodyCodec.buffer());
      var1.putHeader("sec-ch-ua", this.SEC_UA);
      var1.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var1.putHeader("rtt", "0");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", this.UA);
      var1.putHeader("accept", "*/*");
      var1.putHeader("downlink", "10");
      var1.putHeader("ect", "4g");
      var1.putHeader("origin", "https://www.amazon.com");
      var1.putHeader("sec-fetch-site", "none");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("referer", "https://www.amazon.com/");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return new Triplet(var1, (Object)null, true);
   }

   public Triplet mobileRecommended() {
      HttpRequest var1 = this.client.postAbs("https://www.amazon.com/gp/aw/detail/ajax/add-to-cart/ref=mw_pa_dp_buy_crt").as(BodyCodec.buffer());
      var1.putHeader("content-type", "application/x-www-form-urlencoded");
      var1.putHeader("accept", "text/html,*/*");
      var1.putHeader("x-requested-with", "XMLHttpRequest");
      var1.putHeader("accept-language", "en-us");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("origin", "https://www.amazon.com");
      var1.putHeader("user-agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 13_6_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148");
      String var10002 = this.asin;
      var1.putHeader("referer", "https://www.amazon.com/gp/product/" + var10002 + "/ref=sr_1_4?dchild=1&keywords=ps5&qid=" + System.currentTimeMillis() / 1000L + "&sr=8-4");
      var1.putHeader("content-length", "DEFAULT_VALUE");
      MultiMap var2 = MultiMap.caseInsensitiveMultiMap();
      var2.set("verificationSessionId", this.client.cookieStore().getCookieValue("session-id"));
      var2.set("a", "asin");
      var2.set("quantity", "1");
      var2.set("canShowHighUpsellCart", "1");
      var2.set("oid", this.offerid);
      var2.set("verificationSessionID", this.client.cookieStore().getCookieValue("session-id"));
      return new Triplet(var1, var2, false);
   }

   public Triplet buyBoxRegular() {
      HttpRequest var1 = this.client.postAbs("https://www.amazon.com/gp/product/handle-buy-box/ref=dp_start-bbf_1_glance").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("cache-control", "max-age=0");
      var1.putHeader("rtt", "0");
      var1.putHeader("downlink", "10");
      var1.putHeader("ect", "4g");
      var1.putHeader("sec-ch-ua", this.SEC_UA);
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("sec-ch-ua-platform", "\"macOS\"");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("origin", "https://www.amazon.com");
      var1.putHeader("content-type", "application/x-www-form-urlencoded");
      var1.putHeader("user-agent", this.UA);
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("referer", "https://www.amazon.com/ORIbox-Protective-Case-iPhone-pro/dp/" + this.asin + "/");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      MultiMap var2 = MultiMap.caseInsensitiveMultiMap();
      var2.set("CSRF", this.csrf);
      var2.set("anti-csrftoken-a2z", "");
      var2.set("offerListingID", this.offerid);
      var2.set("session-id", this.client.cookieStore().getCookieValue("session-id"));
      var2.set("ASIN", this.asin);
      var2.set("isMerchantExclusive", "0");
      var2.set("merchantID", "ATVPDKIKX0DER");
      var2.set("isAddon", "0");
      var2.set("nodeID", "");
      var2.set("sellingCustomerID", "");
      var2.set("qid", String.valueOf(System.currentTimeMillis() / 1000L));
      var2.set("sr", "8-1-spons");
      var2.set("storeID", "");
      var2.set("tagActionCode", "");
      var2.set("viewID", "glance");
      var2.set("rebateId", "");
      var2.set("ctaDeviceType", "desktop");
      var2.set("ctaPageType", "detail");
      var2.set("usePrimeHandler", "0");
      var2.set("rsid", this.client.cookieStore().getCookieValue("session-id"));
      var2.set("sourceCustomerOrgListID", "");
      var2.set("sourceCustomerOrgListItemID", "");
      var2.set("wlPopCommand", "");
      var2.set("quantity", "1");
      var2.set("submit.add-to-cart", "Add+to+Cart");
      var2.set("dropdown-selection", "add-new");
      var2.set("dropdown-selection-ubb", "add-new");
      return new Triplet(var1, var2, false);
   }

   public Supplier smartEndpoint() {
      int var1 = this.endpointPosition++;
      return this.endpoints[var1 % this.endpoints.length];
   }

   public Triplet reOrder() {
      String var10001 = this.asin;
      HttpRequest var1 = this.client.getAbs("https://www.amazon.com/gp/add-to-cart/json/ref=pd_bap_d_rp_atc_1?ie=UTF8&pd_rd_i=" + var10001 + "&pd_rd_w=83xRG&pf_rd_p=" + UUID.randomUUID() + "&pf_rd_r=9XFJ0RNPFCASXYRPZ0NK&pd_rd_r=" + UUID.randomUUID() + "&pd_rd_wg=1GltB&ASIN=" + this.asin + "&clientName=P13NRecommendations&discoveredAsins.1=" + this.asin + "&offerListingID=" + this.offerid + "&quantity=1&verificationSessionID=" + this.client.cookieStore().getCookieValue("session-id")).as(BodyCodec.buffer());
      var1.putHeader("sec-ch-ua", this.SEC_UA);
      var1.putHeader("rtt", "50");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", this.UA);
      var1.putHeader("accept", "text/html,*/*");
      var1.putHeader("x-requested-with", "XMLHttpRequest");
      var1.putHeader("downlink", "10");
      var1.putHeader("ect", "4g");
      var1.putHeader("sec-ch-ua-platform", "\"macOS\"");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.amazon.com/gp/buyagain/ref=ppx_yo_dt_b_bia");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return new Triplet(var1, (Object)null, false);
   }

   public void setWorkingEndpoint(int var1) {
      this.endpointPosition = var1 - 1;
   }

   public Triplet buyAgain() {
      HttpRequest var1 = this.client.postAbs("https://www.amazon.com/gp/cart/ajax-update.html/ref=ox_sc_rp_item_rp_atc_1").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("sec-ch-ua", this.SEC_UA);
      var1.putHeader("x-aui-view", "Desktop");
      var1.putHeader("rtt", "0");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", this.UA);
      var1.putHeader("content-type", "application/x-www-form-urlencoded; charset=UTF-8;");
      var1.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
      var1.putHeader("x-requested-with", "XMLHttpRequest");
      var1.putHeader("downlink", "10");
      var1.putHeader("ect", "4g");
      var1.putHeader("sec-ch-ua-platform", "\"macOS\"");
      var1.putHeader("origin", "https://www.amazon.com");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.amazon.com/gp/cart/view.html?ref_=nav_cart");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      MultiMap var2 = MultiMap.caseInsensitiveMultiMap();
      var2.set("hasMoreItems", "false");
      var2.set("timeStamp", String.valueOf(System.currentTimeMillis() / 1000L));
      var2.set("requestID", this.rid);
      var2.set("token", this.glow);
      var2.set("addressId", "");
      var2.set("addressZip", "");
      var2.set("reloadAddonUpsellWidget", "1");
      var2.set("submit.add-to-cart", "1");
      var2.set("encodedOffering", this.offerid);
      var2.set("pageAction", "AddToCart");
      return new Triplet(var1, var2, false);
   }

   public Triplet mobile() {
      HttpRequest var1 = this.client.postAbs("https://www.amazon.com/gp/product/cta/add-to-cart/ref=mw_pa_dp_buy_crt").as(BodyCodec.buffer());
      var1.putHeader("content-type", "application/x-www-form-urlencoded");
      var1.putHeader("accept", "text/html,*/*");
      var1.putHeader("x-requested-with", "XMLHttpRequest");
      var1.putHeader("accept-language", "en-us");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("origin", "https://www.amazon.com");
      var1.putHeader("user-agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 13_6_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148");
      String var10002 = this.asin;
      var1.putHeader("referer", "https://www.amazon.com/gp/product/" + var10002 + "/ref=sr_1_3?dchild=1&keywords=toy&qid=" + System.currentTimeMillis() / 1000L + "&sr=8-3");
      var1.putHeader("content-length", "DEFAULT_VALUE");
      String var2 = Utils.getRandomString(13).toUpperCase(Locale.ROOT);
      MultiMap var3 = MultiMap.caseInsensitiveMultiMap();
      var3.set("verificationSessionID", this.client.cookieStore().getCookieValue("session-id"));
      var3.set("log-buybox-metrics-quantity.0", "1");
      var3.set("log-page-type", "Cart");
      var3.set("custom-name.1", "UNIQ-vgcToken");
      var3.set("oid", this.offerid);
      var3.set("log-is-prime-customer", "N");
      var3.set("log-qid", String.valueOf(System.currentTimeMillis() / 1000L));
      var3.set("log-buybox-metrics-product-group-id.0", "0");
      var3.set("ctaDeviceType", "mobileApp");
      var3.set("log-hitType", "pageTouch");
      ThreadLocalRandom var4 = ThreadLocalRandom.current();
      var3.set("log-buybox-metrics-price.0", var4.nextInt(4, 150) + ".0");
      var3.set("log-buybox-metrics-currency.0", "USD");
      var3.set("log-buybox-metrics-merchant.0", var2);
      var3.set("log-buybox-metrics-asin.0", this.asin);
      var3.set("log-sr", "8-4");
      var3.set("a", this.asin);
      var3.set("atcCartWeblab", "");
      var3.set("log-page-type-id", this.asin);
      var3.set("ctaPageType", "detail");
      var3.set("quantity", "1");
      var3.set("custom-value.1", "");
      var3.set("log-buybox-metrics-seller-customer-id.0", var2);
      var3.set("o", "add");
      var3.set("log-is-prime-asin", "N");
      var3.set("coliid", "");
      var3.set("colid", "");
      var3.set("rebateId", "");
      var3.set("log-page-action", "AddToCart");
      var3.set("canShowHighUpsellCart", "1");
      return new Triplet(var1, var3, false);
   }

   public Triplet buynow() {
      HttpRequest var1 = this.client.postAbs("https://www.amazon.com/gp/product/handle-buy-box/ref=dp_start-bbf_1_glance").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("cache-control", "max-age=0");
      var1.putHeader("rtt", "0");
      var1.putHeader("downlink", "10");
      var1.putHeader("ect", "4g");
      var1.putHeader("sec-ch-ua", this.SEC_UA);
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("origin", "https://www.amazon.com");
      var1.putHeader("content-type", "application/x-www-form-urlencoded");
      var1.putHeader("user-agent", this.UA);
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("referer", "https://www.amazon.com/gp/product/" + this.asin + "/ref=ox_sc_act_image_1?smid=ATVPDKIKX0DER&psc=1");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      MultiMap var2 = MultiMap.caseInsensitiveMultiMap();
      var2.set("CSRF", this.csrf);
      var2.set("offerListingID", this.offerid);
      var2.set("session-id", this.client.cookieStore().getCookieValue("session-id"));
      var2.set("ASIN", this.asin);
      var2.set("isMerchantExclusive", "0");
      var2.set("merchantID", "ATVPDKIKX0DER");
      var2.set("isAddon", "0");
      var2.set("nodeID", "");
      var2.set("sellingCustomerID", "");
      var2.set("qid", "");
      var2.set("sr", "");
      var2.set("storeID", "");
      var2.set("tagActionCode", "");
      var2.set("viewID", "glance");
      var2.set("rebateId", "");
      var2.set("ctaDeviceType", "desktop");
      var2.set("ctaPageType", "detail");
      var2.set("usePrimeHandler", "0");
      var2.set("rsid", this.client.cookieStore().getCookieValue("session-id"));
      var2.set("sourceCustomerOrgListID", "");
      var2.set("sourceCustomerOrgListItemID", "");
      var2.set("wlPopCommand", "");
      var2.set("quantity", "1");
      var2.set("submit.buy-now", "Submit");
      var2.set("dropdown-selection", "add-new");
      var2.set("dropdown-selection-ubb", "add-new");
      return new Triplet(var1, var2, true);
   }

   public Triplet aloha() {
      HttpRequest var1 = this.client.postAbs("https://www.amazon.com/gp/product/features/aloha-ppd/udp-ajax-handler/attach-add-to-cart.html").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("sec-ch-ua", this.SEC_UA);
      var1.putHeader("rtt", "50");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", this.UA);
      var1.putHeader("content-type", "application/x-www-form-urlencoded");
      var1.putHeader("accept", "*/*");
      var1.putHeader("x-requested-with", "XMLHttpRequest");
      var1.putHeader("downlink", "10");
      var1.putHeader("ect", "4g");
      var1.putHeader("sec-ch-ua-platform", "\"macOS\"");
      var1.putHeader("origin", "https://www.amazon.com");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.amazon.com/Nerf-Zombie-Strike-Hammershot-Blaster/dp/" + this.asin + "/ref=sr_1_1_sspa");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      String var2 = Utils.getRandomString(13).toUpperCase(Locale.ROOT);
      MultiMap var3 = MultiMap.caseInsensitiveMultiMap();
      var3.set("marketplaceId", var2);
      var3.set("asin", this.asin);
      var3.set("customerId", "");
      var3.set("sessionId", this.client.cookieStore().getCookieValue("session-id"));
      var3.set("accessoryItemAsin", this.asin);
      var3.set("accessoryItemOfferingId", this.offerid);
      var3.set("languageOfPreference", "en_US");
      var3.set("accessoryItemQuantity", "1");
      int var10002 = ThreadLocalRandom.current().nextInt(3, 100);
      var3.set("accessoryItemPrice", "" + var10002 + "." + ThreadLocalRandom.current().nextInt(10, 100));
      var3.set("accessoryMerchantId", var2);
      var3.set("accessoryProductGroupId", "2107000");
      return new Triplet(var1, var3, false);
   }

   public Triplet promo() {
      HttpRequest var1 = this.client.postAbs("https://www.amazon.com/promotion/redeem_and_add_both").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("cache-control", "max-age=0");
      var1.putHeader("rtt", "50");
      var1.putHeader("downlink", "10");
      var1.putHeader("ect", "4g");
      var1.putHeader("sec-ch-ua", this.SEC_UA);
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("origin", "https://www.amazon.com");
      var1.putHeader("content-type", "application/x-www-form-urlencoded");
      var1.putHeader("user-agent", this.UA);
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("referer", "https://www.amazon.com/Trconk-Screen-Protector-Compatible-Nintendo-Tempered/dp/B09GM7K378/ref=rvi_4/147-7537293-1503560?pd_rd_w=aTk94&pf_rd_p=f5690a4d-f2bb-45d9-9d1b-736fee412437&pf_rd_r=ADYWY1JNPA9R4KY2SMJM&pd_rd_r=024f351d-7b89-4491-8b70-c9ebe7f9ec37&pd_rd_wg=B1RTv&pd_rd_i=B09GM7K378&psc=1");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      MultiMap var2 = MultiMap.caseInsensitiveMultiMap();
      var2.set("asin1", this.asin);
      var2.set("asin2", Utils.getRandomString(10).toUpperCase(Locale.ROOT));
      var2.set("offerListingId1", this.offerid);
      var2.set("offerListingId2", Utils.getRandomString(162) + "%3D%3D");
      var2.set("quantity1", "1");
      var2.set("quantity2", "0");
      var2.set("anti-csrftoken-a2z", this.anti_csrf);
      var2.set("session-id", this.client.cookieStore().getCookieValue("session-id"));
      var2.set("submit.addToCart", "Submit");
      return new Triplet(var1, var2, false);
   }

   public Triplet recommendedMulti() {
      String var1 = Utils.getRandomString(20).toUpperCase(Locale.ROOT);
      RealClient var10000 = this.client;
      String var10001 = this.client.cookieStore().getCookieValue("session-id");
      HttpRequest var2 = var10000.postAbs("https://www.amazon.com/gp/item-dispatch/ref=pd_cart_ppd_1_atc_a_1/" + var10001 + "?ie=UTF8&pd_rd_r=" + UUID.randomUUID() + "&pd_rd_w=rPq02&pd_rd_wg=K5PnO&pf_rd_p=" + UUID.randomUUID() + "&pf_rd_r=" + var1 + "&refRID=" + var1).as(BodyCodec.buffer());
      var2.putHeader("content-length", "DEFAULT_VALUE");
      var2.putHeader("cache-control", "max-age=0");
      var2.putHeader("rtt", "0");
      var2.putHeader("downlink", "10");
      var2.putHeader("ect", "4g");
      var2.putHeader("sec-ch-ua", this.SEC_UA);
      var2.putHeader("sec-ch-ua-mobile", "?0");
      var2.putHeader("upgrade-insecure-requests", "1");
      var2.putHeader("origin", "https://www.amazon.com");
      var2.putHeader("content-type", "application/x-www-form-urlencoded");
      var2.putHeader("user-agent", this.UA);
      var2.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var2.putHeader("sec-fetch-site", "same-origin");
      var2.putHeader("sec-fetch-mode", "navigate");
      var2.putHeader("sec-fetch-user", "?1");
      var2.putHeader("sec-fetch-dest", "document");
      var2.putHeader("referer", "https://www.amazon.com/gp/cart/view.html?ref_=nav_cart");
      var2.putHeader("accept-encoding", "gzip, deflate, br");
      var2.putHeader("accept-language", "en-US,en;q=0.9");
      MultiMap var3 = MultiMap.caseInsensitiveMultiMap();
      var3.set("session-id", this.client.cookieStore().getCookieValue("session-id"));
      var3.set("CSRF", this.csrf);
      var3.set("submit.addToCart", "Submit");
      var3.set("offeringID.1", this.offerid);
      return new Triplet(var2, var3, false);
   }

   public Triplet buynowTurbo() {
      HttpRequest var1 = this.client.postAbs("https://www.amazon.com/checkout/turbo-initiate?ref_=dp_start-bbf_1_glance_buyNow_4-2&referrer=detail&pipelineType=turbo&clientId=retailwebsite&weblab=RCX_CHECKOUT_TURBO_DESKTOP_NONPRIME_87784&temporaryAddToCart=1&asin.1=" + this.asin).as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("sec-ch-ua", this.SEC_UA);
      var1.putHeader("x-amz-checkout-entry-referer-url", "https://www.amazon.com/Streamlight-22104-Rechargable-Lithium-Flashlights/dp/" + this.asin + "/ref=grr_4/136-5228532-9776240?pd_rd_w=x5TBY&pf_rd_p=4a328350-1c5b-419b-a0a0-4a8c3bfdf6bb&pf_rd_r=RRD0Z1QBX10QPR3847NN&pd_rd_r=b24c8502-87eb-4150-85ca-19727592f03c&pd_rd_wg=LlfH2&pd_rd_i=B088NGD584&psc=1");
      var1.putHeader("x-amz-turbo-checkout-dp-url", "https://www.amazon.com/Streamlight-22104-Rechargable-Lithium-Flashlights/dp/" + this.asin + "/ref=grr_4/136-5228532-9776240?pd_rd_w=x5TBY&pf_rd_p=4a328350-1c5b-419b-a0a0-4a8c3bfdf6bb&pf_rd_r=RRD0Z1QBX10QPR3847NN&pd_rd_r=b24c8502-87eb-4150-85ca-19727592f03c&pd_rd_wg=LlfH2&pd_rd_i=B088NGD584&psc=1");
      var1.putHeader("rtt", "0");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", this.UA);
      var1.putHeader("content-type", "application/x-www-form-urlencoded");
      var1.putHeader("x-amz-support-custom-signin", "1");
      var1.putHeader("accept", "*/*");
      var1.putHeader("x-requested-with", "XMLHttpRequest");
      var1.putHeader("x-amz-checkout-csrf-token", this.client.cookieStore().getCookieValue("session-id"));
      var1.putHeader("downlink", "10");
      var1.putHeader("ect", "4g");
      var1.putHeader("origin", "https://www.amazon.com");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.amazon.com/Streamlight-22104-Rechargable-Lithium-Flashlights/dp/" + this.asin + "/ref=grr_4");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      MultiMap var2 = MultiMap.caseInsensitiveMultiMap();
      var2.set("isAsync", "1");
      var2.set("addressID", this.addressID);
      var2.set("offerListing.1", this.offerid);
      var2.set("quantity.1", "1");
      return new Triplet(var1, var2, (Object)null);
   }
}
