package io.trickle.task.sites.amazon;

import io.trickle.harvester.Harvester;
import io.trickle.task.Task;
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

public class AmazonAPI extends TaskApiClient {
   public String SEC_UA;
   public String offerid;
   public String UA;
   public CartAPI cartAPI;
   public String asin;

   public MultiMap normalPlaceOrderForm(String var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, String var9, String var10, String var11, String var12, String var13, String var14, String var15, String var16, String var17, String var18, String var19, String var20, String var21, String var22, String var23, String var24, String var25, String var26, String var27, String var28, String var29, String var30, String var31, String var32, String var33, String var34, String var35, String var36, String var37, String var38, String var39, String var40, String var41, String var42, String var43, String var44) {
      MultiMap var45 = MultiMap.caseInsensitiveMultiMap();
      var45.set("csrfToken", var1);
      var45.set("fromAnywhere", "0");
      var45.set("redirectOnSuccess", "0");
      var45.set("purchaseTotal", var2);
      var45.set("purchaseTotalCurrency", var3);
      var45.set("purchaseID", var4);
      var45.set("purchaseCustomerId", var5);
      var45.set("useCtb", var6);
      var45.set("scopeId", var7);
      var45.set("isQuantityInvariant", var8);
      var45.set("promiseTime-0", var9);
      var45.set("promiseAsin-0", var10);
      var45.set("selectedPaymentPaystationId", var11);
      var45.set("submitFromSPC", "1");
      var45.set("pickupType", "All");
      var45.set("searchCriterion", "storeZip");
      var45.set("storeZip", "Example: 98144");
      var45.set("storeZip2", "0034");
      var45.set("searchLockerFormAction", "/gp/buy/storeaddress/handlers/search.html/ref=ox_spc_shipaddr_pickupsearch_popover");
      var45.set("claimCode", "");
      var45.set("primeMembershipTestData", "NULL");
      var45.set("fasttrackExpiration", var12);
      var45.set("countdownThreshold", var13);
      var45.set("countdownId", var14);
      var45.set("showSimplifiedCountdown", var15);
      var45.set("gift-message-text", "");
      var45.set("dupOrderCheckArgs", var16);
      var45.set("order0", var17);
      var45.set("shippingofferingid0.1", var18);
      var45.set("guaranteetype0.1", var19);
      var45.set("issss0.1", var20);
      var45.set("shipsplitpriority0.1", var21);
      var45.set("isShipWhenCompleteValid0.1", var22);
      var45.set("isShipWheneverValid0.1", var23);
      var45.set("shippingofferingid0.2", var24);
      var45.set("guaranteetype0.2", var25);
      var45.set("issss0.2", var26);
      var45.set("shipsplitpriority0.2", var27);
      var45.set("isShipWhenCompleteValid0.2", var28);
      var45.set("isShipWheneverValid0.2", var29);
      var45.set("previousshippingofferingid0", var30);
      var45.set("previousguaranteetype0", var31);
      var45.set("previousissss0", var32);
      var45.set("previousshippriority0", var33);
      var45.set("lineitemids0", var34);
      var45.set("currentshippingspeed", var35);
      var45.set("previousShippingSpeed0", var36);
      var45.set("currentshipsplitpreference", var37);
      var45.set("shippriority.0.shipWhenComplete", var38);
      var45.set("groupcount", var39);
      var45.set("snsUpsellTotalCount", var40);
      var45.set("onmlUpsellSuppressedCount", var41);
      var45.set("vasClaimBasedModel", var42);
      var45.set("shiptrialprefix", var43);
      var45.set("isfirsttimecustomer", var44);
      var45.set("isTFXEligible", "");
      var45.set("isFxEnabled", "");
      var45.set("isFXTncShown", "");
      var45.set("hasWorkingJavascript", "1");
      var45.set("placeYourOrder1", "1");
      return var45;
   }

   public HttpRequest normalPlaceOrder() {
      HttpRequest var1 = this.client.postAbs("https://www.amazon.com/gp/buy/spc/handlers/static-submit-decoupled.html/ref=ox_spc_place_order?ie=UTF8&hasWorkingJavascript=").as(BodyCodec.buffer());
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
      var1.putHeader("referer", "https://www.amazon.com/gp/buy/spc/handlers/display.html?hasWorkingJavascript=1");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public HttpRequest checkPayments() {
      HttpRequest var1 = this.client.getAbs("https://www.amazon.com/cpe/yourpayments/wallet?ref_=ya_d_c_pmt_mpo").as(BodyCodec.buffer());
      var1.putHeader("rtt", "50");
      var1.putHeader("downlink", "10");
      var1.putHeader("ect", "4g");
      var1.putHeader("sec-ch-ua", this.SEC_UA);
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("user-agent", this.UA);
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public MultiMap buynowPlaceOrderForm(String var1, String var2) {
      MultiMap var3 = MultiMap.caseInsensitiveMultiMap();
      var3.set("x-amz-checkout-csrf-token", this.getCookies().getCookieValue("session-id"));
      var3.set("ref_", "chk_spc_placeOrder");
      var3.set("referrer", "spc");
      var3.set("pid", var1);
      var3.set("pipelineType", "turbo");
      var3.set("clientId", "retailwebsite");
      var3.set("temporaryAddToCart", "1");
      var3.set("hostPage", "detail");
      var3.set("weblab", var2);
      var3.set("isClientTimeBased", "1");
      return var3;
   }

   public HttpRequest productPage() {
      HttpRequest var1 = this.client.getAbs("https://www.amazon.com/dp/" + this.asin).as(BodyCodec.buffer());
      var1.putHeader("rtt", "0");
      var1.putHeader("downlink", "10");
      var1.putHeader("ect", "4g");
      var1.putHeader("sec-ch-ua", this.SEC_UA);
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("user-agent", this.UA);
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("sec-fetch-site", "none");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public HttpRequest proceedToCheckout() {
      HttpRequest var1 = this.client.getAbs("https://www.amazon.com/gp/cart/desktop/go-to-checkout.html/ref=ox_sw_proceed?proceedToRetailCheckout=Proceed+to+checkout&proceedToCheckout=1").as(BodyCodec.buffer());
      var1.putHeader("rtt", "0");
      var1.putHeader("downlink", "10");
      var1.putHeader("ect", "4g");
      var1.putHeader("sec-ch-ua", this.SEC_UA);
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("user-agent", this.UA);
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("referer", "https://www.amazon.com/gp/cart/view.html?ref_=nav_cart");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public HttpRequest oneClickSettings() {
      HttpRequest var1 = this.client.getAbs("https://www.amazon.com/cpe/yourpayments/settings/manageoneclick?ref_=ya_d_l_change_1_click").as(BodyCodec.buffer());
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("user-agent", this.UA);
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("rtt", "50");
      var1.putHeader("downlink", "10");
      var1.putHeader("ect", "4g");
      var1.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"96\", \"Google Chrome\";v=\"96\"");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("sec-ch-ua-platform", "\"macOS\"");
      var1.putHeader("referer", "https://www.amazon.com/gp/css/homepage.html?ref_=nav_AccountFlyout_ya");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public void updateGlow(String var1) {
      this.cartAPI.glow = var1;
   }

   public HttpRequest homepage() {
      HttpRequest var1 = this.client.getAbs("https://www.amazon.com/").as(BodyCodec.buffer());
      var1.putHeader("cache-control", "max-age=0");
      var1.putHeader("sec-ch-ua", this.SEC_UA);
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("user-agent", this.UA);
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("sec-fetch-site", "none");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public Pair switchOneClick(boolean var1) {
      HttpRequest var2 = this.client.postAbs("https://www.amazon.com/payments-portal/data/widgets2/v1/customer/A20OQ6KNAZ121S/continueWidget").as(BodyCodec.buffer());
      var2.putHeader("content-length", "DEFAULT_VALUE");
      var2.putHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"96\", \"Google Chrome\";v=\"96\"");
      var2.putHeader("widget-ajax-attempt-count", "0");
      var2.putHeader("rtt", "50");
      var2.putHeader("sec-ch-ua-mobile", "?0");
      var2.putHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.45 Safari/537.36");
      var2.putHeader("content-type", "application/x-www-form-urlencoded; charset=UTF-8");
      var2.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
      var2.putHeader("x-requested-with", "XMLHttpRequest");
      var2.putHeader("downlink", "10");
      var2.putHeader("ect", "4g");
      var2.putHeader("apx-widget-info", "YA:OneClick/desktop/aEIgBZpv2WA4");
      var2.putHeader("sec-ch-ua-platform", "\"macOS\"");
      var2.putHeader("origin", "https://www.amazon.com");
      var2.putHeader("sec-fetch-site", "same-origin");
      var2.putHeader("sec-fetch-mode", "cors");
      var2.putHeader("sec-fetch-dest", "empty");
      var2.putHeader("referer", "https://www.amazon.com/cpe/yourpayments/settings/manageoneclick?ref_=ya_d_l_change_1_click");
      var2.putHeader("accept-encoding", "gzip, deflate, br");
      var2.putHeader("accept-language", "en-US,en;q=0.9");
      MultiMap var3 = MultiMap.caseInsensitiveMultiMap();
      var3.set("ppw-jsEnabled", "true");
      var3.set("ppw-ubid", "134-4537862-7254723");
      var3.set("ppw-isOneClickEnabled", "" + var1);
      var3.set("ppw-widgetEvent", "UpdateBrowserOneClickStatusEvent");
      var3.set("ppw-widgetState", "4-MS0kQ5IIeTNT3tYvVeZAeZ1ICOdgjvL_w5PxLFrgsrvSHdg1JrSwljfFuZUp1b8ynJNfTL1OVnY0pVD2DM_baAweH5twH1b7-hL9ry3cFnz0iLaoMP3igREgNUa2ZlL_wF8qeI7VmtMiEyw3xhQ1s-1yhgi5rY5sW5NRBwzgqEYAxW9LOYUFjSeY60fn8TE2Twaxb3R3IHitDmNOUxmdgbS7FmSRw5bHazONjIRVQm_jdD8m9-MJ57DCcaUfcrk64XNgiEy2rbANb5zmUhaSkm9tJA0TZQckcHxqTicUhLixV8HvSl6QGrhwZSBg1dXUcjgtPHRBjLNSz_aGWjD9SoM8HiwaEbFGK4nJ5nZ5khVhKXZezPyOtoDuyC8Kqj-o6-_PmIERnC4QRFwpqRFUu89IeS9OxkqRt34c-uyuW6zBRIuVWiLJQEOyMoRKA6szFsGaMC8RIirS_LgNQzU2Y4vW8Z_rP6IGJWCubNSg8yj4JtE3sVoYXdYjz26wk1qkM0Z_itMlX9nOmEJLWlxm0Q-0uJmGpPSn7VlqLx4lHCAkrZiEdUBs74q_h08h_WBbm17SwypB8DN3Gzq7k8QFoqWl6aBsR7kOYmofXlkaJ_te22TcFibVyjdhKgQ70RlhpHufYuoUZsMZBxfilEP4oqcUIX2STlEuIUd-RuX_pRWkbCU3DAEt6PPPJ21-FCymQLwWc4xMMFIOQf0zElE-6GYb0YeEabOwCCE4XsxHa3qd6rYCRifS2LA5AyJDONZZMH76FhFErCYor9xG76hqkCMTUezekObK1j5tw_71eihvYEx8doGfXemQYDYA4pDqoiArWTLHnN8NS-DffzjduR8WyL-fvE8cwP_suoIgX2FZc2KSNptCPo4_XMmU9y3aleTJjjF6lfTm2c6M7aljAWhW4CcztENILD3Ik8TRel-3akY51WpLZ6p9Ql5O-7Uuq1rm62-LfeFdwesXseS43piRFzAtE6dwjLJ7BGVIjQKHhVlDgsXQIs8uvboDr4oCIMHOc9iHMfZ-_e_igRYWnYrLhpMAutikbOb8TN9dArHpy7sA_vwGar8AJVLp");
      return new Pair(var2, var3);
   }

   public void genCsm(String var1) {
      this.cartAPI.rid = var1;
      this.getCookies().put("csm-hit", "tb:s-" + var1 + "|" + System.currentTimeMillis() + "&t:" + (System.currentTimeMillis() + (long)ThreadLocalRandom.current().nextInt(1800, 2500)) + "&adb:adblk_no", "www.amazon.com");
   }

   public HttpRequest loginPage() {
      HttpRequest var1 = this.client.getAbs("https://www.amazon.com/ap/signin?openid.pape.max_auth_age=0&openid.return_to=https://www.amazon.com/?ref_=nav_signin&openid.identity=http://specs.openid.net/auth/2.0/identifier_select&openid.assoc_handle=usflex&openid.mode=checkid_setup&openid.claimed_id=http://specs.openid.net/auth/2.0/identifier_select&openid.ns=http://specs.openid.net/auth/2.0&").as(BodyCodec.buffer());
      var1.putHeader("rtt", "50");
      var1.putHeader("downlink", "10");
      var1.putHeader("ect", "4g");
      var1.putHeader("sec-ch-ua", this.SEC_UA);
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("user-agent", this.UA);
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("referer", "https://www.amazon.com/");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public HttpRequest buynowPlaceOrder(String var1, String var2, String var3) {
      HttpRequest var4 = this.client.postAbs("https://www.amazon.com/checkout/spc/place-order?ref_=chk_spc_placeOrder&_srcRID=" + var2 + "&clientId=retailwebsite&pipelineType=turbo&cachebuster=" + Instant.now().toEpochMilli() + "&pid=" + var3).as(BodyCodec.none());
      var4.putHeader("content-length", "DEFAULT_VALUE");
      var4.putHeader("sec-ch-ua", this.SEC_UA);
      String var10002 = this.asin;
      var4.putHeader("x-amz-checkout-entry-referer-url", "https://www.amazon.com/Geilienergy-600mAh-Rechargeable-Batteries-Remotes/dp/" + var10002 + "/ref=pd_rhf_sc_s_sspa_dk_rhf_cart_pt_sub_1_5/" + this.getCookies().getCookieValue("session-id") + "?_encoding=UTF8&pd_rd_i=" + this.asin + "&pd_rd_r=" + UUID.randomUUID());
      var4.putHeader("anti-csrftoken-a2z", var1);
      var4.putHeader("sec-ch-ua-mobile", "?0");
      var4.putHeader("user-agent", this.UA);
      var4.putHeader("content-type", "application/x-www-form-urlencoded");
      var4.putHeader("accept", "*/*");
      var4.putHeader("x-requested-with", "XMLHttpRequest");
      var4.putHeader("origin", "https://www.amazon.com");
      var4.putHeader("sec-fetch-site", "same-origin");
      var4.putHeader("sec-fetch-mode", "cors");
      var4.putHeader("sec-fetch-dest", "empty");
      var4.putHeader("referer", "https://www.amazon.com/checkout/spc?pid=" + var3 + "&pipelineType=turbo&clientId=retailwebsite&temporaryAddToCart=1&hostPage=detail&weblab=RCX_CHECKOUT_TURBO_DESKTOP_NONPRIME_87784");
      var4.putHeader("accept-encoding", "gzip, deflate, br");
      var4.putHeader("accept-language", "en-US,en;q=0.9");
      return var4;
   }

   public JsonObject mobileHomepageBody() {
      return (new JsonObject()).put("input", (new JsonObject()).put("applicationStartCount", 1).put("applicationStarted", true).put("crashInfo", (Object)null));
   }

   public HttpRequest submitDecoupled() {
      HttpRequest var1 = this.client.postAbs("https://www.amazon.com/gp/buy/spc/handlers/static-submit-decoupled.html").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
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
      var1.putHeader("referer", "https://www.amazon.com/gp/buy/spc/handlers/display.html?hasWorkingJavascript=1");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public HttpRequest prefetch(String var1) {
      HttpRequest var2 = this.client.getAbs(var1).as(BodyCodec.buffer());
      var2.putHeader("rtt", "0");
      var2.putHeader("downlink", "10");
      var2.putHeader("ect", "4g");
      var2.putHeader("sec-ch-ua", this.SEC_UA);
      var2.putHeader("sec-ch-ua-mobile", "?0");
      var2.putHeader("upgrade-insecure-requests", "1");
      var2.putHeader("user-agent", this.UA);
      var2.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var2.putHeader("sec-fetch-site", "same-origin");
      var2.putHeader("sec-fetch-mode", "navigate");
      var2.putHeader("sec-fetch-dest", "iframe");
      var2.putHeader("referer", "https://www.amazon.com/gp/cart/view.html?ref_=nav_cart");
      var2.putHeader("accept-encoding", "gzip, deflate, br");
      var2.putHeader("accept-language", "en-US,en;q=0.9");
      return var2;
   }

   public HttpRequest finalPage() {
      HttpRequest var1 = this.client.getAbs("https://www.amazon.com/gp/buy/spc/handlers/display.html?hasWorkingJavascript=1").as(BodyCodec.buffer());
      var1.putHeader("rtt", "0");
      var1.putHeader("downlink", "10");
      var1.putHeader("ect", "4g");
      var1.putHeader("sec-ch-ua", this.SEC_UA);
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("user-agent", this.UA);
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("sec-fetch-site", "none");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public HttpRequest corsairPage() {
      HttpRequest var1 = this.client.getAbs("https://www.amazon.com/stores/page/FD66DB94-4F25-4C7F-A441-A4F5A73390D2?ingress=0&visitId=" + UUID.randomUUID() + "&channel=discovbar?field-lbr_brands_browse-bin=AmazonBasics&ref_=nav_cs_amazonbasics_e625fdf6288e43d49a4983e612a8360d").as(BodyCodec.buffer());
      var1.putHeader("rtt", "50");
      var1.putHeader("downlink", "10");
      var1.putHeader("ect", "4g");
      var1.putHeader("sec-ch-ua", this.SEC_UA);
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("sec-ch-ua-platform", "\"macOS\"");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("user-agent", this.UA);
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("sec-fetch-site", "cross-site");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("referer", "https://www.google.com/");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public HttpRequest mobileHomepage() {
      this.client.cookieStore().put("lc-main", "en_US", ".amazon.com");
      HttpRequest var1 = this.client.postAbs("https://mag-na.amazon.com/mShop/mj/home").as(BodyCodec.buffer());
      var1.putHeader("Host", "mag-na.amazon.com");
      var1.putHeader("Accept", "*/*");
      var1.putHeader("Content-Type", "application/json; charset=utf-8");
      var1.putHeader("Connection", "keep-alive");
      var1.putHeader("x-amz-msh-appid", "name=Amazon;ver=17.15.0;device=iPhone;os=iPhone OS 13.6.1;UDID=" + UUID.randomUUID().toString().toUpperCase() + ";mp=US;locale=en_US");
      var1.putHeader("Cookie", "DEFAULT_VALUE");
      var1.putHeader("Accept-Language", "en-us");
      var1.putHeader("Content-Length", "DEFAULT_VALUE");
      var1.putHeader("Accept-Encoding", "gzip, deflate, br");
      var1.putHeader("User-Agent", "Amazon/367489.0 CFNetwork/1128.0.1 Darwin/19.6.0");
      return var1;
   }

   public void updateCsrf(String var1) {
      this.cartAPI.csrf = var1;
   }

   public HttpRequest promoPage() {
      HttpRequest var1 = this.client.getAbs("https://www.amazon.com/Trconk-Screen-Protector-Compatible-Nintendo-Tempered/dp/B09GM7K378/ref=rvi_4/147-7537293-1503560?pd_rd_w=aTk94&pf_rd_p=f5690a4d-f2bb-45d9-9d1b-736fee412437&pf_rd_r=ADYWY1JNPA9R4KY2SMJM&pd_rd_r=024f351d-7b89-4491-8b70-c9ebe7f9ec37&pd_rd_wg=B1RTv&pd_rd_i=B09GM7K378&th=1").as(BodyCodec.buffer());
      var1.putHeader("rtt", "0");
      var1.putHeader("downlink", "10");
      var1.putHeader("ect", "4g");
      var1.putHeader("sec-ch-ua", this.SEC_UA);
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("user-agent", this.UA);
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("sec-fetch-site", "none");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public HttpRequest cartPage() {
      HttpRequest var1 = this.client.getAbs("https://www.amazon.com/gp/cart/view.html?ref_=nav_cart").as(BodyCodec.buffer());
      var1.putHeader("rtt", "0");
      var1.putHeader("downlink", "10");
      var1.putHeader("ect", "4g");
      var1.putHeader("sec-ch-ua", this.SEC_UA);
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("sec-ch-ua-platform", "\"macOS\"");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("user-agent", this.UA);
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("referer", "https://www.amazon.com/s?k=toy&ref=nb_sb_noss_2");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public HttpRequest walletPage() {
      HttpRequest var1 = this.client.getAbs("https://www.amazon.com/cpe/yourpayments/wallet?ref_=ya_d_c_pmt_mpo").as(BodyCodec.buffer());
      var1.putHeader("rtt", "50");
      var1.putHeader("downlink", "10");
      var1.putHeader("ect", "4g");
      var1.putHeader("sec-ch-ua", this.SEC_UA);
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("user-agent", this.UA);
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public AmazonAPI(Task var1) {
      this.UA = Harvester.HARVESTER_UA;
      String var2 = ShopifyAPI.parseChromeVer(this.UA);
      this.SEC_UA = "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"" + var2 + "\", \"Google Chrome\";v=\"" + var2 + "\"";
      this.asin = var1.getKeywords()[0];
      this.offerid = var1.getKeywords()[1];
      this.cartAPI = new CartAPI(this);
   }

   public HttpRequest placeOrderExp() {
      HttpRequest var1 = this.client.getAbs("https://www.amazon.com/gp/buy/spc/handlers/place-your-decoupled-order.html").as(BodyCodec.buffer());
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
      var1.putHeader("referer", "https://www.amazon.com/gp/buy/spc/handlers/display.html?hasWorkingJavascript=1");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public MultiMap submitDecoupledForm(String var1) {
      MultiMap var2 = MultiMap.caseInsensitiveMultiMap();
      var2.set("csrfToken", this.cartAPI.csrf);
      var2.set("purchaseID", var1);
      var2.set("forcePlaceOrder", "Place this duplicate order");
      var2.set("pipelineType", "spc");
      return var2;
   }

   public HttpRequest naturalFinalPage() {
      HttpRequest var1 = this.client.getAbs("https://www.amazon.com/gp/cart/view.html/ref=lh_co?ie=UTF8&proceedToCheckout.x=129&cartInitiateId=" + System.currentTimeMillis() + "&hasWorkingJavascript=1").as(BodyCodec.buffer());
      var1.putHeader("rtt", "0");
      var1.putHeader("downlink", "10");
      var1.putHeader("ect", "4g");
      var1.putHeader("sec-ch-ua", this.SEC_UA);
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("user-agent", this.UA);
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("referer", "https://www.amazon.com/gp/huc/view.html?ie=UTF8&newItems=" + UUID.randomUUID() + "%2C1");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }
}
