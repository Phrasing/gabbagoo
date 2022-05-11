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

public class WalmartCanadaAPI extends TaskApiClient {
   public static int EXCEPTION_RETRY_DELAY = 12000;
   public static String DID = UUID.randomUUID().toString();
   public static String BYPASS_PREFIX = "";
   public Task task;
   public PXToken pxToken = null;
   public static String PX_TOKEN = "3";

   public HttpRequest initCheckout() {
      HttpRequest var1 = this.client.getAbs("https://www.walmart.ca/api/checkout-page/checkout?lang=en&availStoreId=0&postalCode=0").as(BodyCodec.buffer());
      var1.putHeader("pragma", "no-cache");
      var1.putHeader("cache-control", "no-cache");
      var1.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"89\", \"Chromium\";v=\"89\", \";Not A Brand\";v=\"99\"");
      var1.putHeader("accept", "application/json");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.128 Safari/537.36");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.walmart.ca/checkout");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-GB,en;q=0.9,en-US;q=0.8,lt;q=0.7");
      var1.putHeader("cookie", "DEFAULT_VALUE");
      var1.putHeader("x-px-authorization", this.tokenValue());
      return var1;
   }

   public WalmartCanadaAPI(Task var1) {
      super(ClientType.WALMART_PIXEL_3);
      this.task = var1;
   }

   public Buffer placeOrderForm(PaymentToken var1, String var2) {
      JsonObject var3 = new JsonObject();
      var3.put("credentialEncrypted", true);
      var3.put("paymentId", var2);
      JsonObject var4 = new JsonObject();
      var4.put("cypherTextCvv", var1.getEncryptedCvv());
      var4.put("cypherTextPan", var1.getEncryptedPan());
      var4.put("integrityCheck", var1.getIntegrityCheck());
      var4.put("keyId", var1.getKeyId());
      var4.put("phase", var1.getPhase());
      var3.put("voltageCredential", var4);
      JsonArray var5 = new JsonArray();
      var5.add(var3);
      return (new JsonObject()).put("cvv", var5).toBuffer();
   }

   public HttpRequest submitEmail() {
      HttpRequest var1 = this.client.postAbs("https://www.walmart.ca/api/checkout-page/checkout/email?availStore=0&postalCode=0").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("pragma", "no-cache");
      var1.putHeader("cache-control", "no-cache");
      var1.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"89\", \"Chromium\";v=\"89\", \";Not A Brand\";v=\"99\"");
      var1.putHeader("accept", "application/json");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.128 Safari/537.36");
      var1.putHeader("content-type", "application/json");
      var1.putHeader("origin", "https://www.walmart.ca");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.walmart.ca/checkout");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-GB,en;q=0.9,en-US;q=0.8,lt;q=0.7");
      var1.putHeader("cookie", "DEFAULT_VALUE");
      var1.putHeader("x-px-authorization", this.tokenValue());
      return var1;
   }

   public HttpRequest submitCard() {
      HttpRequest var1 = this.client.postAbs("https://www.walmart.ca/api/checkout-page/payments/summary").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("pragma", "no-cache");
      var1.putHeader("cache-control", "no-cache");
      var1.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"89\", \"Chromium\";v=\"89\", \";Not A Brand\";v=\"99\"");
      var1.putHeader("accept", "application/json");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.128 Safari/537.36");
      var1.putHeader("content-type", "application/json");
      var1.putHeader("origin", "https://www.walmart.ca");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.walmart.ca/checkout");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-GB,en;q=0.9,en-US;q=0.8,lt;q=0.7");
      var1.putHeader("cookie", "DEFAULT_VALUE");
      var1.putHeader("x-px-authorization", this.tokenValue());
      return var1;
   }

   public void close() {
      if (this.pxToken != null) {
      }

      super.close();
   }

   public Buffer paymentForm(PaymentToken var1) {
      JsonObject var2 = new JsonObject();
      var2.put("type", "CREDIT_CARD");
      Profile var3 = this.task.getProfile();
      var2.put("cardType", var3.getCardType());
      String var10002 = var1.getPiHash();
      var2.put("piBlob", "{\"payment_details\":{\"pi_hash\":\"" + var10002 + "\",\"pm_id\":\"" + var3.getCardType() + "\",\"card\":{\"last_4_digits\":\"" + var3.getLastDigits() + "\",\"type\":\"CREDIT_CARD\",\"exp_month\":\"" + var3.getExpiryMonth() + "\",\"exp_year\":\"" + var3.getExpiryYear() + "\"},\"customer\":{\"address\":{\"address1\":\"" + var3.getAddress1() + "\",\"address2\":\"" + var3.getAddress2() + "\",\"city\":\"" + var3.getCity() + "\",\"country_code\":\"CA\",\"postal_code\":\"" + var3.getZip() + "\",\"state_or_province_code\":\"" + var3.getState() + "\"},\"first_name\":\"" + var3.getFirstName() + "\",\"last_name\":\"" + var3.getLastName() + "\",\"phone\":\"" + var3.getPhone() + "\"},\"save_to_profile\":\"N\"}}");
      var2.put("cvvRequired", "Y");
      return var2.toBuffer();
   }

   public HttpRequest addToCart() {
      HttpRequest var1 = this.client.postAbs("https://www.walmart.ca/api/bsp/v2/cart").as(BodyCodec.buffer());
      var1.putHeaders(Headers$Pseudo.MSPA.get());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("pragma", "no-cache");
      var1.putHeader("cache-control", "no-cache");
      var1.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"89\", \"Chromium\";v=\"89\", \";Not A Brand\";v=\"99\"");
      var1.putHeader("accept", "application/json");
      var1.putHeader("content-type", "application/json");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.128 Safari/537.36");
      var1.putHeader("wm_qos.correlation_id", "768da70f-027-178f9d0739bb8d,768da70f-027-178f9d0739b5de,768da70f-027-178f9d0739b5de");
      var1.putHeader("origin", "https://www.walmart.ca");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-GB,en;q=0.9,en-US;q=0.8,lt;q=0.7");
      var1.putHeader("cookie", "DEFAULT_VALUE");
      var1.putHeader("x-px-authorization", this.tokenValue());
      return var1;
   }

   public HttpRequest submitShipping() {
      HttpRequest var1 = this.client.postAbs("https://www.walmart.ca/api/checkout-page/checkout/items/ship-method?lang=en&availStore=0&postalCode=0").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("pragma", "no-cache");
      var1.putHeader("cache-control", "no-cache");
      var1.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"89\", \"Chromium\";v=\"89\", \";Not A Brand\";v=\"99\"");
      var1.putHeader("accept", "application/json");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.128 Safari/537.36");
      var1.putHeader("content-type", "application/json");
      var1.putHeader("origin", "https://www.walmart.ca");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.walmart.ca/checkout");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-GB,en;q=0.9,en-US;q=0.8,lt;q=0.7");
      var1.putHeader("cookie", "DEFAULT_VALUE");
      var1.putHeader("x-px-authorization", this.tokenValue());
      return var1;
   }

   public HttpRequest submitAddress() {
      HttpRequest var1 = this.client.postAbs("https://www.walmart.ca/api/checkout-page/checkout/address?lang=en&availStore=0&slotBooked=false").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("pragma", "no-cache");
      var1.putHeader("cache-control", "no-cache");
      var1.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"89\", \"Chromium\";v=\"89\", \";Not A Brand\";v=\"99\"");
      var1.putHeader("accept", "application/json");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.128 Safari/537.36");
      var1.putHeader("content-type", "application/json");
      var1.putHeader("origin", "https://www.walmart.ca");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.walmart.ca/checkout");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-GB,en;q=0.9,en-US;q=0.8,lt;q=0.7");
      var1.putHeader("cookie", "DEFAULT_VALUE");
      var1.putHeader("x-px-authorization", this.tokenValue());
      return var1;
   }

   public JsonObject atcForm() {
      return this.atcForm(this.task.getKeywords()[0], Integer.parseInt(this.task.getSize()));
   }

   public Buffer shippingForm() {
      JsonObject var1 = new JsonObject();
      var1.put("offerId", this.task.getKeywords()[0]);
      var1.put("levelOfService", "STANDARD");
      return (new JsonObject()).put("shipMethods", (new JsonArray()).add(var1)).toBuffer();
   }

   public String tokenValue() {
      return this.pxToken == null ? "3" : (String)this.pxToken.getValue();
   }

   public HttpRequest submitPayment() {
      HttpRequest var1 = this.client.postAbs("https://www.walmart.ca/api/checkout-page/checkout/payments?lang=en&availStoreId=0&postalCode=0").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("pragma", "no-cache");
      var1.putHeader("cache-control", "no-cache");
      var1.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"89\", \"Chromium\";v=\"89\", \";Not A Brand\";v=\"99\"");
      var1.putHeader("accept", "application/json");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.128 Safari/537.36");
      var1.putHeader("content-type", "application/json");
      var1.putHeader("origin", "https://www.walmart.ca");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.walmart.ca/checkout");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-GB,en;q=0.9,en-US;q=0.8,lt;q=0.7");
      var1.putHeader("cookie", "DEFAULT_VALUE");
      var1.putHeader("x-px-authorization", this.tokenValue());
      return var1;
   }

   public CompletableFuture handleBadResponse(int var1) {
      switch (var1) {
         case 412:
         case 444:
            if (super.rotateProxy()) {
            }
         default:
            return CompletableFuture.completedFuture(true);
      }
   }

   public HttpRequest placeOrder() {
      HttpRequest var1 = this.client.postAbs("https://www.walmart.ca/api/checkout-page/checkout/place-order?lang=en&availStoreId=0&postalCode=0").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("pragma", "no-cache");
      var1.putHeader("cache-control", "no-cache");
      var1.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"89\", \"Chromium\";v=\"89\", \";Not A Brand\";v=\"99\"");
      var1.putHeader("accept", "application/json");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.128 Safari/537.36");
      var1.putHeader("content-type", "application/json");
      var1.putHeader("origin", "https://www.walmart.ca");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.walmart.ca/checkout");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-GB,en;q=0.9,en-US;q=0.8,lt;q=0.7");
      var1.putHeader("cookie", "DEFAULT_VALUE");
      var1.putHeader("x-px-authorization", this.tokenValue());
      return var1;
   }

   public JsonObject atcForm(String var1, int var2) {
      JsonObject var3 = new JsonObject();
      var3.put("postalCode", "X");
      JsonObject var4 = new JsonObject();
      var4.put("offerId", var1);
      var4.put("skuId", var1);
      var4.put("quantity", var2);
      var4.put("action", "ADD");
      var4.put("allowSubstitutions", false);
      var3.put("items", (new JsonArray()).add(var4));
      return var3;
   }

   public Buffer addressForm() {
      JsonObject var1 = new JsonObject();
      var1.put("fulfillmentType", "SHIPTOHOME");
      Profile var2 = this.task.getProfile();
      return var1.put("deliveryInfo", (new JsonObject()).put("firstName", var2.getFirstName()).put("lastName", var2.getLastName()).put("addressLine1", var2.getAddress1()).put("addressLine2", var2.getAddress2()).put("city", var2.getCity()).put("state", var2.getState()).put("postalCode", var2.getZip()).put("phone", var2.getPhone()).put("saveToProfile", true).put("verificationLevel", "VERIFIED").put("country", "CA").put("locationId", (Object)null).put("overrideAddressVerification", true)).toBuffer();
   }

   public Buffer submitEmailForm() {
      return (new JsonObject()).put("emailAddress", this.task.getProfile().getEmail()).toBuffer();
   }

   public Buffer cardForm(PaymentToken var1) {
      JsonObject var2 = (new JsonObject()).put("integrityCheck", var1.getIntegrityCheck()).put("phase", var1.getPhase()).put("keyId", var1.getKeyId());
      JsonObject var3 = new JsonObject();
      var3.put("pan", var1.getEncryptedPan());
      var3.put("cvv", var1.getEncryptedCvv());
      var3.put("encryption", var2);
      JsonObject var4 = new JsonObject();
      var4.put("piHash", var3);
      var4.put("cardType", "CREDIT_CARD");
      var4.put("pmId", this.task.getProfile().getCardType());
      var4.put("cardLast4Digits", this.task.getProfile().getLastDigits());
      var4.put("referenceId", Utils.getRandomString(5));
      return (new JsonObject()).put("orderTotal", Double.longBitsToDouble(4626322717216342016L)).put("paymentMethods", (new JsonArray()).add(var4)).toBuffer();
   }
}
