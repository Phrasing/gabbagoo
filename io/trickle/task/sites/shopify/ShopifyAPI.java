package io.trickle.task.sites.shopify;

import io.trickle.harvester.Harvester;
import io.trickle.task.Task;
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

public class ShopifyAPI extends TaskApiClient {
   public String SITE_URL;
   public static String[] api_ua = new String[]{"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.51 Safari/537.36", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.51 Safari/537.36", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.109 Safari/537.36", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.99 Safari/537.36", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.51 Safari/537.36", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.51 Safari/537.36", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.82 Safari/537.36", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.80 Safari/537.36", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.109 Safari/537.36"};
   public String SEC_UA;
   public boolean graphUnstable;
   public static Pattern VER_PATTERN = Pattern.compile("Chrome/([0-9][0-9])");
   public String SHOP_ID;
   public Pair propertiesPair;
   public boolean isFirstProcessingPageVisit;
   public boolean isOOS;
   public String key;
   public String API_TOKEN;
   public static int EXCEPTION_RETRY_DELAY = 3000;
   public RealClient checkpointClient;
   public String UA;

   public HttpRequest paymentPage(String var1) {
      String var2 = "https://" + this.SITE_URL + "/" + this.SHOP_ID + "/checkouts/" + var1;
      if (this.getOOSDirect()) {
         var2 = var2 + "/shipping_rates";
      }

      var2 = var2 + "?previous_step=shipping_method&step=payment_method";
      if (this.key != null) {
         var2 = var2 + "&key=" + this.key;
      }

      HttpRequest var3 = this.client.getAbs(var2).as(BodyCodec.buffer());
      var3.putHeader("cache-control", "max-age=0");
      var3.putHeader("upgrade-insecure-requests", "1");
      var3.putHeader("user-agent", this.UA);
      var3.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var3.putHeader("sec-fetch-site", "same-origin");
      var3.putHeader("sec-fetch-mode", "navigate");
      var3.putHeader("sec-fetch-user", "?1");
      var3.putHeader("sec-fetch-dest", "document");
      var3.putHeader("sec-ch-ua", this.SEC_UA);
      var3.putHeader("sec-ch-ua-mobile", "?0");
      var3.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var3.putHeader("referer", "https://" + this.SITE_URL + "/" + this.SHOP_ID + "/checkouts/" + var1 + "?previous_step=shipping_method&step=payment_method");
      var3.putHeader("accept-encoding", "gzip, deflate");
      var3.putHeader("accept-language", "en-US,en;q=0.9");
      return var3;
   }

   public HttpRequest shippingRateAPI(String var1) {
      HttpRequest var2 = this.client.getAbs("https://" + this.SITE_URL + "/wallets/checkouts/" + var1 + "/shipping_rates.json").as(BodyCodec.buffer());
      var2.putHeader("x-shopify-uniquetoken", this.getCookies().getCookieValue("_shopify_y"));
      var2.putHeader("authorization", this.API_TOKEN);
      var2.putHeader("content-type", "application/json");
      var2.putHeader("accept", "application/json");
      var2.putHeader("user-agent", this.UA);
      var2.putHeader("x-shopify-visittoken", this.getCookies().getCookieValue("_shopify_s"));
      var2.putHeader("accept-encoding", "gzip, deflate");
      var2.putHeader("accept-language", "en-US,en;q=0.9");
      return var2;
   }

   public HttpRequest clearCart() {
      HttpRequest var1 = this.client.getAbs("https://" + this.SITE_URL + "/cart/clear.js").as(BodyCodec.buffer());
      var1.putHeader("sec-ch-ua", this.SEC_UA);
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("user-agent", this.UA);
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("sec-fetch-site", "none");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("accept-encoding", "gzip, deflate");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public HttpRequest submitChallenge() {
      HttpRequest var1 = this.client.postAbs("https://" + this.SITE_URL + "/account/login").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("cache-control", "max-age=0");
      var1.putHeader("sec-ch-ua", this.SEC_UA);
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("origin", "https://" + this.SITE_URL);
      var1.putHeader("content-type", "application/x-www-form-urlencoded");
      var1.putHeader("user-agent", this.UA);
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("referer", "https://" + this.SITE_URL + "/challenge");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public HttpRequest postPaymentWithOption(String var1, boolean var2) {
      String var3 = "https://" + this.SITE_URL + "/" + this.SHOP_ID + "/checkouts/" + var1;
      if (this.key != null) {
         var3 = var3 + "?key=" + this.key;
      }

      HttpRequest var4;
      if (var2) {
         var4 = this.client.patchAbs(var3).as(BodyCodec.buffer());
      } else {
         var4 = this.client.postAbs(var3).as(BodyCodec.buffer());
      }

      var4.putHeader("content-length", "DEFAULT_VALUE");
      var4.putHeader("cache-control", "max-age=0");
      var4.putHeader("upgrade-insecure-requests", "1");
      String var10002 = this.SITE_URL;
      var4.putHeader("origin", "https://" + var10002);
      var4.putHeader("content-type", !var2 ? "application/x-www-form-urlencoded" : "DONT_SET");
      var4.putHeader("user-agent", this.UA);
      var4.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var4.putHeader("sec-fetch-site", "same-origin");
      var4.putHeader("sec-fetch-mode", "navigate");
      var4.putHeader("sec-fetch-user", "?1");
      var4.putHeader("sec-fetch-dest", "document");
      var4.putHeader("referer", "https://" + this.SITE_URL + "/" + this.SHOP_ID + "/checkouts/" + var1 + "?previous_step=shipping_method&step=payment_method");
      var4.putHeader("accept-encoding", "gzip, deflate");
      var4.putHeader("accept-language", "en-US,en;q=0.9");
      return var4;
   }

   public boolean checkIsOOS() {
      if (this.isOOS) {
         this.isOOS = false;
         return true;
      } else {
         return false;
      }
   }

   public MultiMap emptyCheckoutViaCartForm() {
      MultiMap var1 = MultiMap.caseInsensitiveMultiMap();
      var1.set("updates[]", "1");
      var1.set("attributes[checkout_clicked]", "true");
      var1.set("checkout", "");
      return var1;
   }

   public HttpRequest calculateTaxesWallets(String var1) {
      HttpRequest var2 = this.client.getAbs("https://" + this.SITE_URL + "/wallets/checkouts/" + var1 + "/calculate_shipping").as(BodyCodec.buffer());
      var2.putHeader("sec-ch-ua", this.SEC_UA);
      var2.putHeader("x-shopify-uniquetoken", this.getCookies().getCookieValue("_shopify_y"));
      var2.putHeader("sec-ch-ua-mobile", "?0");
      var2.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var2.putHeader("authorization", this.API_TOKEN);
      var2.putHeader("x-shopify-checkout-version", "2018-03-05");
      var2.putHeader("x-shopify-visittoken", this.getCookies().getCookieValue("_shopify_s"));
      var2.putHeader("accept", "application/json");
      var2.putHeader("x-shopify-wallets-caller", "costanza");
      var2.putHeader("user-agent", this.UA);
      var2.putHeader("content-type", "application/json");
      var2.putHeader("sec-fetch-site", "same-origin");
      var2.putHeader("sec-fetch-mode", "cors");
      var2.putHeader("sec-fetch-dest", "empty");
      var2.putHeader("referer", "https://" + this.SITE_URL + "/");
      var2.putHeader("accept-encoding", "gzip, deflate");
      var2.putHeader("accept-language", "en-US,en;q=0.9");
      return var2;
   }

   public HttpRequest cartJS() {
      HttpRequest var1 = this.client.getAbs("https://" + this.SITE_URL + "/cart.js").as(BodyCodec.buffer());
      var1.putHeader("sec-ch-ua", this.SEC_UA);
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var1.putHeader("user-agent", this.UA);
      var1.putHeader("accept", "*/*");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      String var10002 = this.SITE_URL;
      var1.putHeader("referer", "https://" + var10002 + "/products/" + ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE));
      var1.putHeader("accept-encoding", "gzip, deflate");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public boolean getOOSDirect() {
      return this.isOOS;
   }

   public String getSiteURL() {
      return this.SITE_URL;
   }

   public HttpRequest fakePatchPayment(String var1) {
      return this.postPaymentWithOption(var1, true);
   }

   public HttpRequest shippingPage(String var1) {
      String var2 = "https://" + this.SITE_URL + "/" + this.SHOP_ID + "/checkouts/" + var1;
      if (this.checkIsOOS()) {
         var2 = var2 + "/shipping_rates";
      }

      var2 = var2 + "?previous_step=contact_information&step=shipping_method";
      if (this.key != null) {
         var2 = var2 + "&key=" + this.key;
      }

      HttpRequest var3 = this.client.getAbs(var2).as(BodyCodec.buffer());
      var3.putHeader("cache-control", "max-age=0");
      var3.putHeader("upgrade-insecure-requests", "1");
      var3.putHeader("user-agent", this.UA);
      var3.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var3.putHeader("sec-fetch-site", "same-origin");
      var3.putHeader("sec-fetch-mode", "navigate");
      var3.putHeader("sec-fetch-user", "?1");
      var3.putHeader("sec-fetch-dest", "document");
      var3.putHeader("sec-ch-ua", this.SEC_UA);
      var3.putHeader("sec-ch-ua-mobile", "?0");
      var3.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var3.putHeader("referer", "https://" + this.SITE_URL + "/");
      var3.putHeader("accept-encoding", "gzip, deflate");
      var3.putHeader("accept-language", "en-US,en;q=0.9");
      return var3;
   }

   public HttpRequest emptyCheckoutViaCart() {
      HttpRequest var1 = this.client.postAbs("https://" + this.SITE_URL + "/cart").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("cache-control", "max-age=0");
      var1.putHeader("sec-ch-ua", this.SEC_UA);
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("origin", "https://" + this.SITE_URL);
      var1.putHeader("content-type", "application/x-www-form-urlencoded");
      var1.putHeader("user-agent", this.UA);
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("origin", "https://" + this.SITE_URL + "/");
      var1.putHeader("accept-encoding", "gzip, deflate");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public HttpRequest checkpointPage(boolean var1) {
      if (!var1) {
         this.checkpointClient.cookieStore().removeAnyMatch("_secure_session_id");
      }

      HttpRequest var2 = this.checkpointClient.getAbs("https://" + this.SITE_URL + "/checkpoint?return_to=https%3A%2F%2F" + this.SITE_URL + "%2Fcheckout").as(BodyCodec.buffer());
      var2.putHeader("cache-control", "max-age=0");
      var2.putHeader("upgrade-insecure-requests", "1");
      if (Harvester.HARVESTER_UA != null) {
         var2.putHeader("user-agent", Harvester.HARVESTER_UA);
      } else {
         var2.putHeader("user-agent", this.UA);
      }

      var2.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var2.putHeader("sec-fetch-site", "same-origin");
      var2.putHeader("sec-fetch-mode", "navigate");
      var2.putHeader("sec-fetch-user", "?1");
      var2.putHeader("sec-fetch-dest", "document");
      var2.putHeader("sec-ch-ua", this.SEC_UA);
      var2.putHeader("sec-ch-ua-mobile", "?0");
      var2.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var2.putHeader("referer", "https://" + this.SITE_URL + "/checkout");
      var2.putHeader("accept-encoding", "gzip, deflate");
      var2.putHeader("accept-language", "en-US,en;q=0.9");
      return var2;
   }

   public HttpRequest getProcessingRedirect(String var1) {
      String var2 = "https://" + this.SITE_URL + "/" + this.SHOP_ID + "/checkouts/" + var1;
      if (this.checkIsOOS()) {
         var2 = var2 + "/shipping_rates";
      }

      var2 = var2 + "?previous_step=payment_method&step=";
      if (this.key != null) {
         var2 = var2 + "&key=" + this.key;
      }

      HttpRequest var3 = this.client.getAbs(var2).as(BodyCodec.buffer());
      var3.putHeader("cache-control", "max-age=0");
      var3.putHeader("upgrade-insecure-requests", "1");
      var3.putHeader("user-agent", this.UA);
      var3.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var3.putHeader("sec-fetch-site", "same-origin");
      var3.putHeader("sec-fetch-mode", "navigate");
      var3.putHeader("sec-fetch-user", "?1");
      var3.putHeader("sec-fetch-dest", "document");
      var3.putHeader("sec-ch-ua", this.SEC_UA);
      var3.putHeader("sec-ch-ua-mobile", "?0");
      var3.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var3.putHeader("referer", "https://" + this.SITE_URL + "/");
      var3.putHeader("accept-encoding", "gzip, deflate");
      var3.putHeader("accept-language", "en-US,en;q=0.9");
      return var3;
   }

   public HttpRequest elJS(String var1) {
      if (ThreadLocalRandom.current().nextBoolean()) {
         ThreadLocalRandom var10002 = ThreadLocalRandom.current();
         var1 = var1.replace("products", "collections/" + var10002.nextInt(Integer.MAX_VALUE) + "/products");
      }

      var1 = var1 + (ThreadLocalRandom.current().nextBoolean() ? ".js?variant=" : "?format=js&variant=") + ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
      HttpRequest var2 = this.client.getAbs(var1).as(BodyCodec.buffer());
      var2.putHeader("upgrade-insecure-requests", "1");
      var2.putHeader("user-agent", this.UA);
      var2.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var2.putHeader("sec-fetch-site", "none");
      var2.putHeader("sec-fetch-mode", "navigate");
      var2.putHeader("sec-fetch-user", "?1");
      var2.putHeader("sec-fetch-dest", "document");
      var2.putHeader("sec-ch-ua", this.SEC_UA);
      var2.putHeader("sec-ch-ua-mobile", "?0");
      var2.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var2.putHeader("accept-encoding", "gzip, deflate");
      var2.putHeader("accept-language", "en-US,en;q=0.9");
      return var2;
   }

   public HttpRequest fetchNewQueueUrl() {
      HttpRequest var1 = this.client.getAbs("https://" + this.SITE_URL + "/throttle/queue").as(BodyCodec.buffer());
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("sec-ch-ua", this.SEC_UA);
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("user-agent", this.UA);
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("referer", "https://" + this.SITE_URL);
      var1.putHeader("accept-encoding", "gzip, deflate");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public MultiMap challengeForm(String var1, String var2) {
      MultiMap var3 = MultiMap.caseInsensitiveMultiMap();
      var3.set("authenticity_token", var1);
      var3.set("g-recaptcha-response", var2);
      return var3;
   }

   public HttpRequest paymentStatusAPI(String var1) {
      HttpRequest var2 = this.client.getAbs("https://" + this.SITE_URL + "/api/checkouts/" + var1 + "/payments/").as(BodyCodec.buffer());
      var2.putHeader("x-shopify-uniquetoken", this.getCookies().getCookieValue("_shopify_y"));
      var2.putHeader("authorization", this.API_TOKEN);
      var2.putHeader("accept", "application/json");
      var2.putHeader("user-agent", this.UA);
      var2.putHeader("x-shopify-visittoken", this.getCookies().getCookieValue("_shopify_s"));
      var2.putHeader("accept-encoding", "gzip, deflate");
      var2.putHeader("accept-language", "en-US,en;q=0.9");
      return var2;
   }

   public HttpRequest atcAJAX(String var1) {
      String var2 = "https://" + this.SITE_URL + "/cart/add.js?id=" + var1;
      if (this.propertiesPair != null) {
         var2 = var2 + "&properties[" + (String)this.propertiesPair.first + "]=" + (String)this.propertiesPair.second;
      }

      HttpRequest var3 = this.client.getAbs(var2).as(BodyCodec.buffer());
      var3.putHeader("upgrade-insecure-requests", "1");
      var3.putHeader("user-agent", this.UA);
      var3.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var3.putHeader("sec-fetch-site", "none");
      var3.putHeader("sec-fetch-mode", "navigate");
      var3.putHeader("sec-fetch-user", "?1");
      var3.putHeader("sec-fetch-dest", "document");
      var3.putHeader("sec-ch-ua", this.SEC_UA);
      var3.putHeader("sec-ch-ua-mobile", "?0");
      var3.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var3.putHeader("accept-encoding", "gzip, deflate");
      var3.putHeader("accept-language", "en-US,en;q=0.9");
      return var3;
   }

   public HttpRequest updateJS() {
      HttpRequest var1 = this.client.postAbs("https://" + this.SITE_URL + "/cart/update.js").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("content-type", "application/json");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("user-agent", this.UA);
      var1.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
      var1.putHeader("sec-fetch-site", "none");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("sec-ch-ua", this.SEC_UA);
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var1.putHeader("referer", "https://" + this.SITE_URL + "/products/nkcu7544-400");
      var1.putHeader("accept-encoding", "gzip, deflate");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public HttpRequest rotateHeaderReq(HttpRequest var1) {
      List var2 = var1.headers().entries();
      Collections.shuffle(var2);
      var1.headers().clear();
      var2.spliterator().forEachRemaining(ShopifyAPI::lambda$rotateHeaderReq$0);
      return var1;
   }

   public HttpRequest shippingRateOld(String var1, String var2, String var3) {
      HttpRequest var4 = this.client.getAbs("https://" + this.SITE_URL + "/cart/shipping_rates.json?shipping_address[zip]=" + var1 + "&shipping_address[country]=" + var2 + "&shipping_address[province]=" + var3).as(BodyCodec.buffer());
      var4.putHeader("sec-ch-ua", this.SEC_UA);
      var4.putHeader("sec-ch-ua-mobile", "?0");
      var4.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var4.putHeader("upgrade-insecure-requests", "1");
      var4.putHeader("user-agent", this.UA);
      var4.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var4.putHeader("sec-fetch-site", "none");
      var4.putHeader("sec-fetch-mode", "navigate");
      var4.putHeader("sec-fetch-user", "?1");
      var4.putHeader("sec-fetch-dest", "document");
      var4.putHeader("accept-encoding", "gzip, deflate");
      var4.putHeader("accept-language", "en-US,en;q=0.9");
      return var4;
   }

   public HttpRequest processAPI(String var1) {
      String var2 = "https://" + this.SITE_URL + "/" + this.SHOP_ID + "/checkouts/" + var1;
      if (this.key != null) {
         var2 = var2 + "?key=" + this.key;
      }

      HttpRequest var3 = this.client.patchAbs(var2).as(BodyCodec.buffer());
      var3.putHeader("content-length", "DEFAULT_VALUE");
      var3.putHeader("x-shopify-uniquetoken", UUID.randomUUID().toString());
      var3.putHeader("authorization", this.API_TOKEN);
      var3.putHeader("content-type", "application/json");
      var3.putHeader("user-agent", this.UA);
      var3.putHeader("x-shopify-visittoken", this.getCookies().getCookieValue("_shopify_s"));
      var3.putHeader("accept-encoding", "gzip, deflate");
      var3.putHeader("accept-language", "en-US,en;q=0.9");
      return var3;
   }

   public boolean getIsOOS() {
      return this.isOOS;
   }

   public HttpRequest emptyCheckout(boolean var1) {
      HttpRequest var2;
      if (var1) {
         var2 = this.client.postAbs("https://" + this.SITE_URL + "/checkout").as(BodyCodec.buffer());
         var2.putHeader("content-length", "DEFAULT_VALUE");
         var2.putHeader("content-type", "application/json");
      } else {
         var2 = this.client.getAbs("https://" + this.SITE_URL + "/checkout").as(BodyCodec.buffer());
      }

      var2.putHeader("sec-ch-ua", this.SEC_UA);
      var2.putHeader("sec-ch-ua-mobile", "?0");
      var2.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var2.putHeader("upgrade-insecure-requests", "1");
      var2.putHeader("user-agent", this.UA);
      var2.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var2.putHeader("sec-fetch-site", "none");
      var2.putHeader("sec-fetch-mode", "navigate");
      var2.putHeader("sec-fetch-user", "?1");
      var2.putHeader("sec-fetch-dest", "document");
      var2.putHeader("accept-encoding", "gzip, deflate");
      var2.putHeader("accept-language", "en-US,en;q=0.9");
      return var2;
   }

   public HttpRequest challengePage() {
      HttpRequest var1 = this.client.getAbs("https://" + this.SITE_URL + "/challenge").as(BodyCodec.buffer());
      var1.putHeader("cache-control", "max-age=0");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("user-agent", this.UA);
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("sec-ch-ua", this.SEC_UA);
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var1.putHeader("referer", "https://" + this.SITE_URL + "/account/login");
      var1.putHeader("accept-encoding", "gzip, deflate");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public HttpRequest postContact(String var1) {
      String var2 = "https://" + this.SITE_URL + "/" + this.SHOP_ID + "/checkouts/" + var1;
      if (this.key != null) {
         var2 = var2 + "?key=" + this.key;
      }

      HttpRequest var3 = this.client.postAbs(var2).as(BodyCodec.buffer());
      var3.putHeader("content-length", "DEFAULT_VALUE");
      var3.putHeader("cache-control", "max-age=0");
      var3.putHeader("sec-ch-ua", this.SEC_UA);
      var3.putHeader("sec-ch-ua-mobile", "?0");
      var3.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var3.putHeader("upgrade-insecure-requests", "1");
      var3.putHeader("origin", "https://" + this.SITE_URL);
      var3.putHeader("content-type", "application/x-www-form-urlencoded");
      var3.putHeader("user-agent", this.UA);
      var3.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var3.putHeader("sec-fetch-site", "same-origin");
      var3.putHeader("sec-fetch-mode", "navigate");
      var3.putHeader("sec-fetch-user", "?1");
      var3.putHeader("sec-fetch-dest", "document");
      var3.putHeader("referer", "https://" + this.SITE_URL + "/");
      var3.putHeader("accept-encoding", "gzip, deflate");
      var3.putHeader("accept-language", "en-US,en;q=0.9");
      return var3;
   }

   public HttpRequest upsellATC() {
      HttpRequest var1 = this.client.postAbs("https://" + this.SITE_URL + "/cart/add.js").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("content-type", "application/x-www-form-urlencoded; charset=UTF-8");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("user-agent", this.UA);
      var1.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
      var1.putHeader("sec-fetch-site", "none");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("sec-ch-ua", this.SEC_UA);
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("sec-ch-ua-platform", "\"Windows\"");
      String var10002 = this.SITE_URL;
      var1.putHeader("referer", "https://" + var10002 + "/products/" + ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE));
      var1.putHeader("accept-encoding", "gzip, deflate");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public boolean markProcessingPageAsVisited() {
      if (this.isFirstProcessingPageVisit) {
         this.isFirstProcessingPageVisit = false;
         return true;
      } else {
         return false;
      }
   }

   public String getBaseCheckoutURL() {
      return "https://" + this.SITE_URL + "/" + this.SHOP_ID + "/checkouts/";
   }

   public HttpRequest newQueue() {
      HttpRequest var1 = this.client.postAbs("https://" + this.SITE_URL + "/queue/poll").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("sec-ch-ua", this.SEC_UA);
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var1.putHeader("user-agent", this.UA);
      var1.putHeader("content-type", "application/json");
      var1.putHeader("accept", "*/*");
      var1.putHeader("origin", "https://" + this.SITE_URL);
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://" + this.SITE_URL + "/throttle/queue");
      var1.putHeader("accept-encoding", "gzip, deflate");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public HttpRequest meta() {
      HttpRequest var1 = this.client.getAbs("https://" + this.SITE_URL + "/payments/config.json").as(BodyCodec.buffer());
      var1.putHeader("sec-ch-ua", this.SEC_UA);
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("user-agent", this.UA);
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("sec-fetch-site", "none");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("accept-encoding", "gzip, deflate");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public HttpRequest oosPage(String var1) {
      HttpRequest var2 = this.client.getAbs(var1).as(BodyCodec.buffer());
      var2.putHeader("upgrade-insecure-requests", "1");
      var2.putHeader("user-agent", this.UA);
      var2.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var2.putHeader("sec-fetch-site", "none");
      var2.putHeader("sec-fetch-mode", "navigate");
      var2.putHeader("sec-fetch-user", "?1");
      var2.putHeader("sec-fetch-dest", "document");
      var2.putHeader("sec-ch-ua", this.SEC_UA);
      var2.putHeader("sec-ch-ua-mobile", "?0");
      var2.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var2.putHeader("accept-encoding", "gzip, deflate");
      var2.putHeader("accept-language", "en-US,en;q=0.9");
      return var2;
   }

   public ShopifyAPI(Task var1) {
      this.SITE_URL = SiteParser.getURLFromSite(var1);
      this.UA = api_ua[ThreadLocalRandom.current().nextInt(api_ua.length)];
      String var2 = parseChromeVer(this.UA);
      this.SEC_UA = "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"" + var2 + "\", \"Google Chrome\";v=\"" + var2 + "\"";
      this.propertiesPair = SiteParser.getProperties(var1.getSite());
   }

   public HttpRequest genAcc() {
      HttpRequest var1 = this.client.postAbs("https://" + this.SITE_URL + "/account").as(BodyCodec.none());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("cache-control", "max-age=0");
      var1.putHeader("sec-ch-ua", this.SEC_UA);
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("origin", "https://" + this.SITE_URL);
      var1.putHeader("content-type", "application/x-www-form-urlencoded");
      var1.putHeader("user-agent", this.UA);
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("referer", "https://" + this.SITE_URL + "/account/register");
      var1.putHeader("accept-encoding", "gzip, deflate");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public HttpRequest changeJS() {
      HttpRequest var1 = this.client.postAbs("https://" + this.SITE_URL + "/cart/change.js").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("content-type", "application/x-www-form-urlencoded; charset=UTF-8");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("user-agent", this.UA);
      var1.putHeader("accept", "application/json, text/javascript, */*; q=0.01");
      var1.putHeader("sec-fetch-site", "none");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("sec-ch-ua", this.SEC_UA);
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var1.putHeader("referer", "https://" + this.SITE_URL + "/products/nkcu7544-400");
      var1.putHeader("accept-encoding", "gzip, deflate");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public void setShopID(String var1) {
      this.SHOP_ID = var1;
   }

   public void setAPIToken(String var1) {
      if (var1 == null) {
         System.out.println("No api key was found for this site, please contact the discord to add this site to the sitelist to run smoother.");
      } else {
         this.API_TOKEN = "Basic " + Base64.getEncoder().encodeToString(var1.getBytes(StandardCharsets.UTF_8));
      }
   }

   public static void lambda$rotateHeaderReq$0(HttpRequest var0, Map.Entry var1) {
      var0.putHeader((String)var1.getKey(), (String)var1.getValue());
   }

   public HttpRequest fetchQueueUrl() {
      String var1 = this.client.cookieStore().getCookieValue("_shopify_ctd");
      HttpRequest var2 = this.client.getAbs("https://" + this.SITE_URL + "/throttle/queue?_ctd=" + var1 + "&_ctd_update").as(BodyCodec.buffer());
      var2.putHeader("sec-ch-ua", this.SEC_UA);
      var2.putHeader("sec-ch-ua-mobile", "?0");
      var2.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var2.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var2.putHeader("user-agent", this.UA);
      var2.putHeader("sec-fetch-site", "same-origin");
      var2.putHeader("sec-fetch-mode", "navigate");
      var2.putHeader("sec-fetch-user", "?1");
      var2.putHeader("sec-fetch-dest", "document");
      var2.putHeader("referer", "https://" + this.SITE_URL);
      var2.putHeader("accept-encoding", "gzip, deflate");
      var2.putHeader("accept-language", "en-US,en;q=0.9");
      return var2;
   }

   public HttpRequest rawCheckoutUrl(String var1) {
      String var2 = "https://" + this.SITE_URL;
      if (this.graphUnstable) {
         var2 = var2 + "/checkouts/" + var1;
         var2 = var2 + "/information";
      } else {
         var2 = var2 + "/" + this.SHOP_ID + "/checkouts/" + var1;
      }

      if (this.key != null) {
         var2 = var2 + "?key=" + this.key;
      }

      HttpRequest var3 = this.client.getAbs(var2).as(BodyCodec.buffer());
      var3.putHeader("cache-control", "max-age=0");
      var3.putHeader("upgrade-insecure-requests", "1");
      var3.putHeader("user-agent", this.UA);
      var3.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var3.putHeader("sec-fetch-site", "same-origin");
      var3.putHeader("sec-fetch-mode", "navigate");
      var3.putHeader("sec-fetch-user", "?1");
      var3.putHeader("sec-fetch-dest", "document");
      var3.putHeader("sec-ch-ua", this.SEC_UA);
      var3.putHeader("sec-ch-ua-mobile", "?0");
      var3.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var3.putHeader("referer", "https://" + this.SITE_URL + "/checkpoint?return_to=https%3A%2F%2F" + this.SITE_URL + "%2Fcart");
      var3.putHeader("accept-encoding", "gzip, deflate");
      var3.putHeader("accept-language", "en-US,en;q=0.9");
      return var3;
   }

   public void setInstock() {
      this.isOOS = false;
   }

   public HttpRequest paymentToken(WebClient var1) {
      HttpRequest var2 = var1.postAbs("https://deposit.us.shopifycs.com/sessions").as(BodyCodec.buffer());
      var2.putHeader("Host", "deposit.us.shopifycs.com");
      var2.putHeader("Connection", "keep-alive");
      var2.putHeader("Content-Length", "DEFAULT_VALUE");
      var2.putHeader("sec-ch-ua", this.SEC_UA);
      var2.putHeader("Accept", "application/json");
      var2.putHeader("sec-ch-ua-mobile", "?0");
      var2.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var2.putHeader("User-Agent", this.UA);
      var2.putHeader("Content-Type", "application/json");
      var2.putHeader("Origin", "https://checkout.shopifycs.com");
      var2.putHeader("Sec-Fetch-Site", "same-site");
      var2.putHeader("Sec-Fetch-Mode", "cors");
      var2.putHeader("Sec-Fetch-Dest", "empty");
      var2.putHeader("Referer", "https://checkout.shopifycs.com/");
      var2.putHeader("Accept-Encoding", "gzip, deflate");
      var2.putHeader("Accept-Language", "en-US,en;q=0.9");
      return var2;
   }

   public HttpRequest postPayment(String var1) {
      return this.postPaymentWithOption(var1, false);
   }

   public HttpRequest oldQueue() {
      String var1 = this.client.cookieStore().getCookieValue("_shopify_ctd");
      HttpRequest var2 = this.client.getAbs("https://" + this.SITE_URL + "/checkout/poll?js_poll=1").as(BodyCodec.buffer());
      var2.putHeader("sec-ch-ua", this.SEC_UA);
      var2.putHeader("sec-ch-ua-mobile", "?0");
      var2.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var2.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var2.putHeader("user-agent", this.UA);
      var2.putHeader("sec-fetch-site", "same-origin");
      var2.putHeader("sec-fetch-mode", "cors");
      var2.putHeader("sec-fetch-dest", "empty");
      var2.putHeader("referer", "https://" + this.SITE_URL + "/throttle/queue?_ctd=" + var1 + "&_ctd_update");
      var2.putHeader("accept-encoding", "gzip, deflate");
      var2.putHeader("accept-language", "en-US,en;q=0.9");
      return var2;
   }

   public HttpRequest postShippingRate(String var1) {
      String var2 = "https://" + this.SITE_URL + "/" + this.SHOP_ID + "/checkouts/" + var1;
      if (this.key != null) {
         var2 = var2 + "?key=" + this.key;
      }

      HttpRequest var3 = this.client.postAbs(var2).as(BodyCodec.buffer());
      var3.putHeader("content-length", "DEFAULT_VALUE");
      var3.putHeader("cache-control", "max-age=0");
      var3.putHeader("sec-ch-ua", this.SEC_UA);
      var3.putHeader("sec-ch-ua-mobile", "?0");
      var3.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var3.putHeader("upgrade-insecure-requests", "1");
      var3.putHeader("origin", "https://" + this.SITE_URL);
      var3.putHeader("content-type", "application/x-www-form-urlencoded");
      var3.putHeader("user-agent", this.UA);
      var3.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var3.putHeader("sec-fetch-site", "same-origin");
      var3.putHeader("sec-fetch-mode", "navigate");
      var3.putHeader("sec-fetch-user", "?1");
      var3.putHeader("sec-fetch-dest", "document");
      var3.putHeader("referer", "https://" + this.SITE_URL + "/");
      var3.putHeader("accept-encoding", "gzip, deflate");
      var3.putHeader("accept-language", "en-US,en;q=0.9");
      return var3;
   }

   public HttpRequest productsJSON(boolean var1) {
      HttpRequest var2 = this.client.getAbs(var1 ? "https://" + this.SITE_URL + "/products.json?order=" + ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE) : "https://" + this.SITE_URL + "/products.json?limit=250").as(BodyCodec.buffer());
      var2.putHeader("upgrade-insecure-requests", "1");
      var2.putHeader("user-agent", this.UA);
      var2.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var2.putHeader("sec-fetch-site", "none");
      var2.putHeader("sec-fetch-mode", "navigate");
      var2.putHeader("sec-fetch-user", "?1");
      var2.putHeader("sec-fetch-dest", "document");
      var2.putHeader("sec-ch-ua", this.SEC_UA);
      var2.putHeader("sec-ch-ua-mobile", "?0");
      var2.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var2.putHeader("accept-encoding", "gzip, deflate");
      var2.putHeader("accept-language", "en-US,en;q=0.9");
      return var2;
   }

   public HttpRequest cart() {
      HttpRequest var1 = this.client.getAbs("https://" + this.SITE_URL + "/cart").as(BodyCodec.buffer());
      var1.putHeader("sec-ch-ua", this.SEC_UA);
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("user-agent", this.UA);
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("sec-fetch-site", "none");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("accept-encoding", "gzip, deflate");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public HttpRequest contactAPI(String var1) {
      HttpRequest var2 = this.client.patchAbs("https://" + this.SITE_URL + "/" + this.SHOP_ID + "/checkouts/" + var1).as(BodyCodec.buffer());
      var2.putHeader("content-length", "DEFAULT_VALUE");
      var2.putHeader("authorization", this.API_TOKEN);
      var2.putHeader("content-type", "application/json");
      var2.putHeader("user-agent", this.UA);
      var2.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
      var2.putHeader("upgrade-insecure-requests", "1");
      var2.putHeader("accept-encoding", "gzip, deflate");
      var2.putHeader("accept-language", "en-US,en;q=0.9");
      return var2;
   }

   public HttpRequest walletsPutDetails(String var1) {
      HttpRequest var2 = this.client.putAbs("https://" + this.SITE_URL + "/api/checkouts/" + var1 + ".json").as(BodyCodec.buffer());
      var2.putHeader("content-length", "DEFAULT_VALUE");
      var2.putHeader("sec-ch-ua", this.SEC_UA);
      var2.putHeader("x-shopify-uniquetoken", this.getCookies().getCookieValue("_shopify_y"));
      var2.putHeader("sec-ch-ua-mobile", "?0");
      var2.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var2.putHeader("authorization", this.API_TOKEN);
      var2.putHeader("x-shopify-checkout-version", "2018-03-05");
      var2.putHeader("content-type", "application/json");
      var2.putHeader("accept", "application/json");
      var2.putHeader("x-shopify-wallets-caller", "costanza");
      var2.putHeader("user-agent", this.UA);
      var2.putHeader("x-shopify-visittoken", this.getCookies().getCookieValue("_shopify_s"));
      var2.putHeader("origin", "https://" + this.SITE_URL);
      var2.putHeader("sec-fetch-site", "same-origin");
      var2.putHeader("sec-fetch-mode", "cors");
      var2.putHeader("sec-fetch-dest", "empty");
      var2.putHeader("referer", "https://" + this.SITE_URL + "/collections/frontpage");
      var2.putHeader("accept-encoding", "gzip, deflate");
      var2.putHeader("accept-language", "en-US,en;q=0.9");
      return var2;
   }

   public HttpRequest atcWallets() {
      HttpRequest var1 = this.client.postAbs("https://" + this.SITE_URL + "/wallets/checkouts.json").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("sec-ch-ua", this.SEC_UA);
      var1.putHeader("x-shopify-uniquetoken", this.getCookies().getCookieValue("_shopify_y"));
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var1.putHeader("authorization", this.API_TOKEN);
      var1.putHeader("x-shopify-checkout-version", "2018-03-05");
      var1.putHeader("content-type", "application/json");
      var1.putHeader("accept", "application/json");
      var1.putHeader("x-shopify-wallets-caller", "costanza");
      var1.putHeader("user-agent", this.UA);
      var1.putHeader("x-shopify-visittoken", this.getCookies().getCookieValue("_shopify_s"));
      var1.putHeader("origin", "https://" + this.SITE_URL);
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://" + this.SITE_URL + "/collections/frontpage");
      var1.putHeader("accept-encoding", "gzip, deflate");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public HttpRequest submitCheckpoint() {
      HttpRequest var1 = this.checkpointClient.postAbs("https://" + this.SITE_URL + "/checkpoint").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("cache-control", "max-age=0");
      var1.putHeader("sec-ch-ua", this.SEC_UA);
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("origin", "https://" + this.SITE_URL);
      var1.putHeader("content-type", "application/x-www-form-urlencoded");
      var1.putHeader("user-agent", this.UA);
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("referer", "https://" + this.SITE_URL + "/checkpoint?return_to=https%3A%2F%2F" + this.SITE_URL + "%2Fcheckout");
      var1.putHeader("accept-encoding", "gzip, deflate");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public HttpRequest submitPassword() {
      HttpRequest var1 = this.client.postAbs("https://" + this.SITE_URL + "/password").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("cache-control", "max-age=0");
      var1.putHeader("sec-ch-ua", this.SEC_UA);
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("origin", "https://" + this.SITE_URL);
      var1.putHeader("content-type", "application/x-www-form-urlencoded");
      var1.putHeader("user-agent", this.UA);
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("referer", "https://" + this.SITE_URL + "/password");
      var1.putHeader("accept-encoding", "gzip, deflate");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public ShopifyAPI(Task var1, RealClient var2) {
      super(var2);
      this.SITE_URL = SiteParser.getURLFromSite(var1);
      this.UA = api_ua[ThreadLocalRandom.current().nextInt(api_ua.length)];
      String var3 = parseChromeVer(this.UA);
      this.SEC_UA = "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"" + var3 + "\", \"Google Chrome\";v=\"" + var3 + "\"";
      this.propertiesPair = SiteParser.getProperties(var1.getSite());
   }

   public HttpRequest homepage() {
      HttpRequest var1 = this.client.getAbs("https://" + this.SITE_URL + "/").as(BodyCodec.buffer());
      var1.putHeader("sec-ch-ua", this.SEC_UA);
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("user-agent", this.UA);
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("sec-fetch-site", "none");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("accept-encoding", "gzip, deflate");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public HttpRequest basicATC() {
      HttpRequest var1 = this.client.postAbs("https://" + this.SITE_URL + "/cart/add").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("content-type", "application/x-www-form-urlencoded; charset=UTF-8");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("user-agent", this.UA);
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("sec-fetch-site", "none");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("sec-ch-ua", this.SEC_UA);
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("sec-ch-ua-platform", "\"Windows\"");
      String var10002 = this.SITE_URL;
      var1.putHeader("referer", "https://" + var10002 + "/products/" + ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE));
      var1.putHeader("accept-encoding", "gzip, deflate");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public String fakeCheckoutPath() {
      String var1 = "abcdefghijklmnopqrstuvwxyz0123456789";
      StringBuilder var2 = new StringBuilder();

      while(var2.length() < 32) {
         int var3 = (int)(ThreadLocalRandom.current().nextFloat() * (float)"abcdefghijklmnopqrstuvwxyz0123456789".length());
         var2.append("abcdefghijklmnopqrstuvwxyz0123456789".charAt(var3));
      }

      return var2.toString();
   }

   public HttpRequest login() {
      HttpRequest var1 = this.client.postAbs("https://" + this.SITE_URL + "/account/login").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("cache-control", "max-age=0");
      var1.putHeader("sec-ch-ua", this.SEC_UA);
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("origin", "https://" + this.SITE_URL);
      var1.putHeader("content-type", "application/x-www-form-urlencoded");
      var1.putHeader("user-agent", this.UA);
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("referer", "https://" + this.SITE_URL + "/account/login?return_url=%2Faccount");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return this.rotateHeaderReq(var1);
   }

   public void setOOS() {
      this.isOOS = true;
   }

   public HttpRequest accountConfirmPage() {
      HttpRequest var1 = this.client.getAbs("https://" + this.SITE_URL + "/account").as(BodyCodec.buffer());
      var1.putHeader("sec-ch-ua", this.SEC_UA);
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("sec-ch-ua-platform", "\"macOS\"");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("user-agent", this.UA);
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("sec-fetch-site", "none");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("accept-encoding", "gzip, deflate");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return this.rotateHeaderReq(var1);
   }

   public HttpRequest contactPage(String var1) {
      String var2 = "https://" + this.SITE_URL;
      if (this.graphUnstable) {
         var2 = var2 + "/checkouts/" + var1;
         var2 = var2 + "/information";
      } else {
         var2 = var2 + "/" + this.SHOP_ID + "/checkouts/" + var1;
         if (this.checkIsOOS()) {
            var2 = var2 + "/stock_problems";
         }

         if (this.key != null) {
            var2 = var2 + "?key=" + this.key;
         } else {
            var2 = var2 + "?step=contact_information";
         }
      }

      HttpRequest var3 = this.client.getAbs(var2).as(BodyCodec.buffer());
      var3.putHeader("cache-control", "max-age=0");
      var3.putHeader("upgrade-insecure-requests", "1");
      var3.putHeader("user-agent", this.UA);
      var3.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var3.putHeader("sec-fetch-site", "same-origin");
      var3.putHeader("sec-fetch-mode", "navigate");
      var3.putHeader("sec-fetch-user", "?1");
      var3.putHeader("sec-fetch-dest", "document");
      var3.putHeader("sec-ch-ua", this.SEC_UA);
      var3.putHeader("sec-ch-ua-mobile", "?0");
      var3.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var3.putHeader("referer", "https://" + this.SITE_URL + "/checkpoint?return_to=https%3A%2F%2F" + this.SITE_URL + "%2Fcart");
      var3.putHeader("accept-encoding", "gzip, deflate");
      var3.putHeader("accept-language", "en-US,en;q=0.9");
      return var3;
   }

   public static String parseChromeVer(String var0) {
      Matcher var1 = VER_PATTERN.matcher(var0);
      return var1.find() ? var1.group(1) : "88";
   }

   public HttpRequest refreshLink(String var1) {
      String var2 = "https://" + this.SITE_URL + "/" + this.SHOP_ID + "/checkouts/" + var1;
      var2 = var2 + "?previous_step=shipping_method&step=payment_method";
      if (this.key != null) {
         var2 = var2 + "&key=" + this.key;
      }

      HttpRequest var3 = this.client.getAbs(var2).as(BodyCodec.buffer());
      var3.putHeader("cache-control", "max-age=0");
      var3.putHeader("upgrade-insecure-requests", "1");
      var3.putHeader("user-agent", this.UA);
      var3.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var3.putHeader("sec-fetch-site", "same-origin");
      var3.putHeader("sec-fetch-mode", "navigate");
      var3.putHeader("sec-fetch-user", "?1");
      var3.putHeader("sec-fetch-dest", "document");
      var3.putHeader("sec-ch-ua", this.SEC_UA);
      var3.putHeader("sec-ch-ua-mobile", "?0");
      var3.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var3.putHeader("referer", "https://" + this.SITE_URL + "/");
      var3.putHeader("accept-encoding", "gzip, deflate");
      var3.putHeader("accept-language", "en-US,en;q=0.9");
      return var3;
   }
}
