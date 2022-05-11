package io.trickle.task.sites.walmart.graphql;

import io.netty.handler.codec.http.HttpHeaderValues;
import io.trickle.account.Account;
import io.trickle.task.Task;
import io.trickle.task.antibot.impl.px.PerimeterX;
import io.trickle.task.sites.walmart.util.PaymentToken;
import io.trickle.util.Utils;
import io.trickle.util.request.Headers;
import io.trickle.util.request.Headers$Pseudo;
import io.trickle.webclient.ClientType;
import io.trickle.webclient.CookieJar;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.codec.BodyCodec;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.regex.Matcher;

public class WalmartNewAPI extends API {
   public int headerLogic = ThreadLocalRandom.current().nextInt(3);
   public PerimeterX pxAPI = null;
   public String dateOfPrevReq;

   public void setLoggedIn(boolean var1) {
      this.loggedIn = true;
   }

   public JsonObject tenderUpdateForm(String var1) {
      JsonObject var2 = new JsonObject();
      var2.put("query", "mutation updateTenderPlan($input:UpdateTenderPlanInput!){updateTenderPlan(input:$input){__typename tenderPlan{...TenderPlanFields}}}fragment TenderPlanFields on TenderPlan{__typename id contractId grandTotal{...PriceDetailRowFields}authorizationAmount{...PriceDetailRowFields}allocationStatus paymentGroups{...PaymentGroupFields}otcDeliveryBenefit{...PriceDetailRowFields}otherAllowedPayments{type status}addPaymentType hasAmountUnallocated weightDebitTotal{...PriceDetailRowFields}}fragment PriceDetailRowFields on PriceDetailRow{__typename key label displayValue value info{__typename title message}}fragment PaymentGroupFields on TenderPlanPaymentGroup{__typename type subTotal{__typename key label displayValue value info{__typename title message}}selectedCount allocations{...CreditCardAllocationFragment...GiftCardAllocationFragment...EbtCardAllocationFragment...DsCardAllocationFragment...PayPalAllocationFragment...AffirmAllocationFragment}statusMessage}fragment CreditCardAllocationFragment on CreditCardAllocation{__typename card{...CreditCardFragment}canEditOrDelete canDeselect isEligible isSelected allocationAmount{__typename displayValue value}capOneReward{...CapOneFields}statusMessage{__typename messageStatus messageType}paymentType}fragment CapOneFields on CapOneReward{credentialId redemptionRate redemptionUrl redemptionMethod rewardPointsBalance rewardPointsSelected rewardAmountSelected}fragment CreditCardFragment on CreditCard{__typename id isDefault cardAccountLinked needVerifyCVV cardType expiryMonth expiryYear isExpired firstName lastName lastFour isEditable phone}fragment GiftCardAllocationFragment on GiftCardAllocation{__typename card{...GiftCardFields}canEditOrDelete canDeselect isEligible isSelected allocationAmount{__typename displayValue value}statusMessage{__typename messageStatus messageType}paymentType remainingBalance{__typename displayValue value}}fragment GiftCardFields on GiftCard{__typename id balance{cardBalance}lastFour displayLabel}fragment EbtCardAllocationFragment on EbtCardAllocation{__typename card{__typename id lastFour firstName lastName}canEditOrDelete canDeselect isEligible isSelected allocationAmount{__typename displayValue value}statusMessage{__typename messageStatus messageType}paymentType ebtMaxEligibleAmount{__typename displayValue value}cardBalance{__typename displayValue value}}fragment DsCardAllocationFragment on DsCardAllocation{__typename card{...DsCardFields}canEditOrDelete canDeselect isEligible isSelected allocationAmount{__typename displayValue value}statusMessage{__typename messageStatus messageType}paymentType canApplyAmount{__typename displayValue value}remainingBalance{__typename displayValue value}paymentPromotions{__typename programName canApplyAmount{__typename displayValue value}allocationAmount{__typename displayValue value}remainingBalance{__typename displayValue value}balance{__typename displayValue value}termsLink isInvalid}otcShippingBenefit termsLink}fragment DsCardFields on DsCard{__typename id displayLabel lastFour fundingProgram balance{cardBalance}dsCardType cardName}fragment PayPalAllocationFragment on PayPalAllocation{__typename allocationAmount{__typename displayValue value}paymentHandle paymentType email}fragment AffirmAllocationFragment on AffirmAllocation{__typename allocationAmount{__typename displayValue value}paymentHandle paymentType cardType firstName lastName}");
      var2.put("variables", (new JsonObject()).put("input", (new JsonObject()).put("contractId", this.contractId).put("tenderPlanId", var1.isEmpty() ? null : var1).put("payments", (new JsonArray()).add((new JsonObject()).put("paymentType", "CREDITCARD").put("preferenceId", this.paymentId).putNull("amount").putNull("capOneReward").putNull("cardType").putNull("paymentHandle"))).put("accountRefresh", true).put("isAmendFlow", false)));
      return var2;
   }

   public JsonObject getBillingForm(PaymentToken var1) {
      JsonObject var2 = new JsonObject();
      var2.put("query", "mutation CreateCreditCard($input:AccountCreditCardInput!){createAccountCreditCard(input:$input){errors{code message}creditCard{...CreditCardFragment}}}fragment CreditCardFragment on CreditCard{__typename firstName lastName phone addressLineOne addressLineTwo city state postalCode cardType expiryYear expiryMonth lastFour id isDefault isExpired needVerifyCVV isEditable capOneProperties{shouldPromptForLink}linkedCard{availableCredit currentCreditBalance currentMinimumAmountDue minimumPaymentDueDate statementBalance statementDate rewards{rewardsBalance rewardsCurrency cashValue cashDisplayValue canRedeem}links{linkMethod linkHref linkType}}}");
      var2.put("variables", (new JsonObject()).put("input", (new JsonObject()).put("firstName", this.task.getProfile().getFirstName()).put("lastName", this.task.getProfile().getLastName()).put("phone", this.task.getProfile().getPhone()).put("address", (new JsonObject()).put("addressLineOne", this.task.getProfile().getAddress1()).put("addressLineTwo", this.task.getProfile().getAddress2()).put("postalCode", this.task.getProfile().getZip()).put("city", this.task.getProfile().getCity()).put("state", this.task.getProfile().getState()).putNull("isApoFpo").putNull("isLoadingDockAvailable").putNull("isPoBox").putNull("businessName").putNull("addressType").putNull("sealedAddress")).put("expiryYear", Integer.parseInt(this.task.getProfile().getExpiryYear())).put("expiryMonth", Integer.parseInt(this.task.getProfile().getExpiryMonth())).put("isDefault", true).put("cardType", this.task.getProfile().getCardType().toString()).put("integrityCheck", var1.getIntegrityCheck()).put("keyId", var1.getKeyId()).put("phase", var1.getPhase()).put("encryptedPan", var1.getEncryptedPan()).put("encryptedCVV", var1.getEncryptedCvv()).put("sourceFeature", "ACCOUNT_PAGE").putNull("cartId").putNull("checkoutSessionId")));
      return var2;
   }

   public JsonObject accountCreateForm() {
      JsonObject var1 = new JsonObject();
      var1.put("personName", (new JsonObject()).put("firstName", this.task.getProfile().getFirstName()).put("lastName", this.task.getProfile().getLastName()));
      String var10002 = this.task.getProfile().getEmail().split("@")[0];
      var1.put("email", var10002 + "+" + ThreadLocalRandom.current().nextInt(999999999) + "@" + this.task.getProfile().getEmail().split("@")[1]);
      var1.put("password", Utils.generateStrongString());
      var1.put("rememberme", true);
      var1.put("emailNotificationAccepted", false);
      var1.put("captcha", (new JsonObject()).put("sensorData", "2a25G2m84Vrp0o9c4230941.12-1,8,-36,-890," + this.pxAPI.getDeviceUA() + ",uaend,82457,82672914,en-GB,Gecko,3,7,0,0,500565,7072549,4549,064,9519,603,1302,003,8347,,cpen:6,i9:7,dm:3,cwen:4,non:1,opc:9,fc:2,sc:0,wrc:0,isc:8,vib:1,bat:3,x39:7,x82:2,7541,2.072809286313,234981602126,loc:-9,9,-64,-197,do_en,dm_en,t_en-5,0,-84,-415,6,8,2,2,427,830,6;7,2,1,0,586,879,6;2,-8,0,0,-1,-3,4;9,-0,7,3,-0,-7,2;1,1,7,4,1153,355,7;0,-4,0,7,7593,511,0;1,1,2,5,504,590,1;0,2,4,9,8067,193,6;6,4,1,0,472,516,6;2,-8,0,0,-1,-3,4;9,-0,7,3,-0,-7,2;1,1,7,4,678,492,1;0,7,3,1,7318,690,0;0,-1,6,6,-9,-0,7;4,-0,2,4,-2,-1,0;1,1,2,5,9508,630,0;6,-5,8,8,1026,817,6;2,3,9,8,255,146,2;2,9,7,4,1260,201,7;0,2,1,0,3856,699,7;4,0,6,7,3384,853,1;0,2,4,9,8313,193,6;6,4,1,0,480,846,6;2,3,9,8,4270,014,8;7,2,0,2,0289,3962,0;0,-1,6,6,4292,590,1;0,2,4,8,9923,193,6;6,-9,7,0,2476,146,2;2,1,7,4,2142,201,7;1,-4,0,7,8699,573,0;2,9,2,5,9669,586,0;-3,6,-01,-805,0,8,6,3,699,223,0;7,6,2,2,780,193,6;6,-9,7,0,-4,-0,2;5,-2,9,7,-2,-7,6;2,3,0,8,4390,191,8;7,-8,4,1,7339,638,0;0,3,0,3,115,210,0;2,9,3,5,9239,586,0;6,8,2,2,676,830,6;6,-9,7,0,-4,-0,2;5,-2,9,7,-2,-7,6;2,3,9,8,992,476,2;2,9,7,4,1016,201,7;0,-4,0,6,-5,-2,9;8,-2,9,2,-3,-8,0;0,3,9,3,5610,834,3;0,-3,4,9,8220,131,6;6,4,1,0,648,516,6;3,1,9,8,4407,047,8;7,2,0,2,0830,701,9;8,3,0,7,7995,573,0;2,9,2,5,9585,586,0;6,8,2,2,684,160,6;6,4,1,0,8102,712,4;8,9,0,1,2659,7573,7;0,-4,0,6,8803,210,0;2,9,2,4,0195,586,0;6,-5,8,7,2869,516,6;3,3,9,8,5389,047,8;8,-8,3,1,8435,690,0;1,1,9,3,5771,780,3;-0,4,-12,-005,3,1,8737,97,9,5,247;7,7,4639,-9,3,8,553;4,4,1892,-2,6,4,118;2,9,5997,-8,2,9,834;7,2,9275,97,9,7,247;1,7,5010,-9,3,0,553;8,4,2273,-2,6,6,118;6,9,6366,-8,2,1,834;1,1,9544,-3,9,7,247;5,9,5197,-9,3,0,553;38,9,3179,-1,2,4,744;18,4,3573,3,8,7,907;22,8,9126,6,0,0,920;39,3,4541,19,0,4,463;21,1,3282,-4,4,6,511;18,3,9136,-3,9,5,131;78,6,2128,86,1,9,085;98,1,1007,-8,6,2,990;88,2,3930,-0,7,0,852;05,9,6122,-9,3,0,447;48,9,4106,-1,2,4,638;28,4,4321,-4,1,9,524;21,5,8651,-2,0,1,783;65,3,3516,-2,6,6,002;14,1,5647,-6,8,7,891;35,9,0079,-1,7,3,817;80,9,1637,-3,9,2,249;96,0,7830,-8,2,1,728;59,1,6388,-9,0,0,920;45,3,5594,-5,0,6,463;47,3,4114,-4,4,8,511;34,2,0007,-3,9,7,131;94,6,2951,-2,1,9,085;14,0,3241,-8,6,2,699;04,4,5837,-0,7,0,551;21,8,8114,-9,3,0,146;54,8,6017,-1,2,4,337;34,6,6276,-4,1,9,223;37,4,0684,-2,0,1,482;71,2,5585,-2,6,6,701;30,3,7501,-6,8,7,590;51,7,2021,-1,7,3,516;06,1,3500,-3,9,2,948;12,9,9719,-8,2,1,427;75,1,8230,-9,0,0,629;61,3,7421,-5,0,6,162;53,3,6041,-4,4,8,210;40,2,2068,-3,9,7,830;00,5,4077,-2,1,9,784;20,2,3939,-8,6,2,699;20,3,5504,-0,7,0,551;47,7,9034,-9,3,0,146;70,0,7920,-1,2,4,337;50,5,7292,-4,1,9,223;53,3,1679,-2,0,1,482;97,4,6469,-2,6,6,701;46,1,8593,-6,8,7,590;67,9,3925,-1,7,3,516;12,0,4453,-3,9,2,948;28,9,0661,-8,2,1,427;91,0,9208,-9,0,0,629;87,5,8356,-5,0,6,162;79,2,7065,-4,4,8,210;66,1,4247,97,9,5,830;26,5,6388,-2,1,7,784;46,2,5240,-8,6,0,699;36,3,7940,-0,7,8,551;53,8,0147,13,3,0,146;86,8,8137,-1,2,4,337;66,6,8396,-4,1,9,223;79,3,2789,-2,0,1,482;13,4,7578,-2,6,6,701;62,2,9526,-6,8,7,590;83,8,4980,-1,7,3,516;38,9,5516,-3,9,2,948;44,0,1718,-8,2,1,427;07,0,0209,-9,0,0,629;93,5,9357,-5,0,6,162;85,2,8040,-4,4,8,210;72,2,4900,-3,9,7,830;42,5,6904,-2,1,9,784;62,2,5866,-8,6,2,699;52,3,7524,-0,7,0,551;79,7,1811,-9,3,0,146;02,0,9707,-1,2,4,337;82,4,9951,-4,1,9,223;85,5,3270,-2,0,1,482;29,3,8176,-2,6,6,701;78,2,0164,-6,8,7,590;99,7,5505,-1,7,3,516;54,1,6084,-3,9,2,948;60,9,2348,-8,2,1,427;23,0,1972,-9,0,0,629;19,5,0020,-5,0,6,162;01,2,9604,-4,4,8,210;98,1,72714,23,0,8,982;12,3,27131,-3,9,0,201;66,0,41220,-6,8,5,853;08,8,74683,-2,0,9,745;31,3,00152,15,2,4,690;170,2,06783,-3,9,7,193;727,5,27426,-3,9,2,201;892,2,20595,-4,1,9,586;199,7,38726,-2,1,9,047;911,3,13756,-8,2,1,780;415,7,74919,-2,0,1,745;524,0,17760,-8,6,2,952;800,1,72303,-9,0,0,982;362,9,80462,26,6,4,064;006,5,11675,34,7,0,814;-7,8,-75,-180,1,0,3704,2974,112;0,3,5352,0254,280;4,5,9257,8216,043;7,9,8102,4385,345;2,8,1924,1149,509;2,1,1548,7442,998;6,1,2241,7607,871;7,2,0896,3007,151;9,0,3820,2980,112;8,3,5585,0261,280;36,3,2012,1501,707;39,8,1167,1154,509;81,8,4580,3834,250;83,2,0016,3009,151;24,7,7923,8199,491;77,5,9690,8224,043;58,2,0507,2289,723;24,1,1865,7457,999;15,4,5056,5475,089;40,0,7845,2985,113;16,7,7037,1991,178;85,9,2134,4409,348;03,0,2284,0813,371;13,1,6266,7604,991;27,1,1441,9339,918;46,5,9854,672,621,146;66,1,5466,761,953,699;19,3,4380,956,218,830;37,7,91733,648,257,201;47,1,41131,250,463,586;78,4,52953,688,493,047;026,3,18575,945,562,-8;530,0,79769,688,065,-3;649,9,12526,643,173,-0;-1,3,-56,-398,-1,2,-93,-753,-8,2,-25,-725,-9,9,-64,-100,-5,0,-84,-413,-3,6,-01,-815,-0,4,-12,-012,170405,622230,7,3,0,6,7482873,85809,7897806955241,1595267072539,68,03630,927,292,3908,2,2,22509,245141,4,jn4o3gibsrb72myhy5q5_8868,1769,971,-2704260577,58330663-2,1,-58,-275,-1,1-0,4,-12,-60,-4676238228;7,10,46,02,72,01,18,22,33,51,54,06,08,50,05,86,9;,7;true;true;true;123;true;72;47;true;false;-9-8,2,-25,-42,0116-0,9,-04,-370,07018880-1,8,-36,-808,277510-7,4,-63,-152,;13;2;9"));
      return var1;
   }

   public HttpRequest terraFirma(String var1, boolean var2) {
      return null;
   }

   public HttpRequest getDeliveryAddresses() {
      String var1 = zs2y(32);
      HttpRequest var2 = this.client.postAbs("https://www.walmart.com/orchestra/home/graphql").as(BodyCodec.buffer());
      var2.headers().setAll(Headers$Pseudo.MASP.get()).set(HttpHeaders.CONTENT_LENGTH, Headers.DEFAULT).set(X_O_SEGMENT, OAOH).set(X_O_CORRELATION_ID, var1).set(DEVICE_PROFILE_REF, this.deviceProfileId).set(X_LATENCY_TRACE, "1").set(WM_MP, TRUE).set(X_O_PLATFORM_VERSION, this.platformVersion).set(X_O_GQL_QUERY, "query GetDeliveryAddresses").set(WM_PAGE_URL, this.productReferer).set(X_APOLLO_OPERATION_NAME, "GetDeliveryAddresses").set(HttpHeaders.USER_AGENT, this.pxAPI.getDeviceUA()).set(X_O_PLATFORM, X_O_PLATFORM_NAME).set(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON).set(HttpHeaders.ACCEPT, HttpHeaderValues.APPLICATION_JSON).set(X_ENABLE_SERVER_TIMING, "1").set(X_O_CCM, SERVER).set(WM_CORRELATION_ID, var1).set(HttpHeaders.ORIGIN, "https://www.walmart.com").set(Headers.SEC_FETCH_SITE, Headers.SAME_ORIGIN).set(Headers.SEC_FETCH_MODE, Headers.CORS).set(Headers.SEC_FETCH_DEST, Headers.EMPTY).set(HttpHeaders.REFERER, this.productReferer).set(HttpHeaders.ACCEPT_ENCODING, Headers.GZIP_DEFLATE_BR).set(HttpHeaders.ACCEPT_LANGUAGE, this.pxAPI.getDeviceLang()).set(HttpHeaders.COOKIE, Headers.DEFAULT);
      return var2;
   }

   public HttpRequest submitPayment() {
      String var1 = zs2y(32);
      HttpRequest var2 = this.client.postAbs("https://www.walmart.com/orchestra/cartxo/graphql").as(BodyCodec.buffer());
      var2.putHeader("content-length", "DEFAULT_VALUE");
      var2.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
      var2.putHeader("x-o-platform", "rweb");
      var2.putHeader("x-o-correlation-id", var1);
      var2.putHeader("device_profile_ref_id", this.deviceProfileId);
      var2.putHeader("x-latency-trace", "1");
      var2.putHeader("wm_mp", "true");
      var2.putHeader("x-o-market", "us");
      var2.putHeader("x-o-platform-version", this.platformVersion);
      var2.putHeader("x-o-gql-query", "mutation PlaceOrder");
      var2.putHeader("x-apollo-operation-name", "PlaceOrder");
      var2.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var2.putHeader("sec-ch-ua-mobile", "?0");
      var2.putHeader("user-agent", this.pxAPI.getDeviceUA());
      var2.putHeader("x-o-segment", "oaoh");
      var2.putHeader("content-type", "application/json");
      var2.putHeader("accept", "application/json");
      var2.putHeader("x-enable-server-timing", "1");
      var2.putHeader("x-o-ccm", "server");
      var2.putHeader("x-o-tp-phase", "tp5");
      var2.putHeader("wm_qos.correlation_id", var1);
      var2.putHeader("origin", "https://www.walmart.com");
      var2.putHeader("sec-fetch-site", "same-origin");
      var2.putHeader("sec-fetch-mode", "cors");
      var2.putHeader("sec-fetch-dest", "empty");
      var2.putHeader("referer", "https://www.walmart.com/checkout/review-order?cartId=" + this.cartId);
      var2.putHeader("accept-encoding", "gzip, deflate, br");
      var2.putHeader("accept-language", this.pxAPI.getDeviceLang());
      return var2;
   }

   public HttpRequest validate() {
      String var1 = zs2y(32);
      HttpRequest var2 = this.client.getAbs("https://api.waiting-room.walmart.com/validateTickets").as(BodyCodec.buffer());
      var2.putHeader("sec-ch-ua", this.pxAPI.getDeviceSecUA());
      var2.putHeader("wm_qos.correlation_id", var1);
      var2.putHeader("x-o-correlation-id", var1);
      var2.putHeader("sec-ch-ua-mobile", "?0");
      var2.putHeader("device_profile_ref_id", this.deviceProfileId);
      var2.putHeader("x-o-segment", OAOH.toString());
      var2.putHeader("wm_mp", "true");
      var2.putHeader("content-type", "application/json");
      var2.putHeader("accept", "application/json");
      var2.putHeader("x-o-platform-version", this.platformVersion);
      var2.putHeader("x-latency-trace", "1");
      var2.putHeader("x-enable-server-timing", "1");
      var2.putHeader("x-o-platform", X_O_PLATFORM_NAME.toString());
      var2.putHeader("user-agent", this.pxAPI.getDeviceUA());
      var2.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var2.putHeader("origin", "https://www.walmart.com");
      var2.putHeader("sec-fetch-site", "same-site");
      var2.putHeader("sec-fetch-mode", "cors");
      var2.putHeader("sec-fetch-dest", "empty");
      var2.putHeader("referer", "https://www.walmart.com/");
      var2.putHeader("accept-encoding", "gzip, deflate");
      var2.putHeader("accept-language", this.pxAPI.getDeviceLang());
      return var2;
   }

   public HttpRequest fulfillment() {
      String var1 = zs2y(32);
      HttpRequest var2 = this.client.postAbs("https://www.walmart.com/orchestra/cartxo/graphql").as(BodyCodec.buffer());
      var2.headers().setAll(Headers$Pseudo.MASP.get()).set(HttpHeaders.CONTENT_LENGTH, Headers.DEFAULT).set(Headers.SEC_CH_UA, this.getPxAPI().getDeviceSecUA()).set(X_O_CORRELATION_ID, var1).set(DEVICE_PROFILE_REF, this.deviceProfileId).set(X_LATENCY_TRACE, "1").set(WM_MP, TRUE).set(X_O_PLATFORM_VERSION, this.platformVersion).set(X_O_SEGMENT, OAOH).set(X_O_GQL_QUERY, "mutation setFulfillment").set(WM_PAGE_URL, this.referer + "?step=cart&ss=addAddress&gxo=true").set(X_APOLLO_OPERATION_NAME, "setFulfillment").set(Headers.SEC_CH_UA_PLATFORM, Headers.WINDOWS).set(Headers.SEC_CH_UA_MOBILE, Headers._Q0).set(HttpHeaders.USER_AGENT, this.pxAPI.getDeviceUA()).set(X_O_PLATFORM, X_O_PLATFORM_NAME).set(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON).set(HttpHeaders.ACCEPT, HttpHeaderValues.APPLICATION_JSON).set(X_ENABLE_SERVER_TIMING, "1").set(X_O_CCM, SERVER).set(WM_CORRELATION_ID, var1).set(HttpHeaders.ORIGIN, "https://www.walmart.com").set(Headers.SEC_FETCH_SITE, Headers.SAME_ORIGIN).set(Headers.SEC_FETCH_MODE, Headers.CORS).set(Headers.SEC_FETCH_DEST, Headers.EMPTY).set(HttpHeaders.REFERER, this.referer + "?step=cart&ss=addAddress&gxo=true").set(HttpHeaders.ACCEPT_ENCODING, Headers.GZIP_DEFLATE_BR).set(HttpHeaders.ACCEPT_LANGUAGE, this.pxAPI.getDeviceLang()).set(HttpHeaders.COOKIE, Headers.DEFAULT);
      return var2;
   }

   public HttpRequest setAddressID() {
      String var1 = zs2y(32);
      HttpRequest var2 = this.client.postAbs("https://www.walmart.com/orchestra/home/graphql").as(BodyCodec.buffer());
      var2.headers().setAll(Headers$Pseudo.MASP.get()).set(HttpHeaders.CONTENT_LENGTH, Headers.DEFAULT).set(X_O_PLATFORM, X_O_PLATFORM_NAME).set(X_O_CORRELATION_ID, var1).set(DEVICE_PROFILE_REF, this.deviceProfileId).set(X_LATENCY_TRACE, "1").set(WM_MP, TRUE).set(X_O_PLATFORM_VERSION, this.platformVersion).set(X_O_GQL_QUERY, "mutation setShippingAddress").set(WM_PAGE_URL, this.productReferer).set(X_APOLLO_OPERATION_NAME, "setShippingAddress").set(HttpHeaders.USER_AGENT, this.pxAPI.getDeviceUA()).set(X_O_SEGMENT, OAOH).set(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON).set(HttpHeaders.ACCEPT, HttpHeaderValues.APPLICATION_JSON).set(X_ENABLE_SERVER_TIMING, "1").set(X_O_CCM, SERVER).set(WM_CORRELATION_ID, var1).set(HttpHeaders.ORIGIN, "https://www.walmart.com").set(Headers.SEC_FETCH_SITE, Headers.SAME_ORIGIN).set(Headers.SEC_FETCH_MODE, Headers.CORS).set(Headers.SEC_FETCH_DEST, Headers.EMPTY).set(HttpHeaders.REFERER, this.productReferer).set(HttpHeaders.ACCEPT_ENCODING, Headers.GZIP_DEFLATE_BR).set(HttpHeaders.ACCEPT_LANGUAGE, this.pxAPI.getDeviceLang()).set(HttpHeaders.COOKIE, Headers.DEFAULT);
      return var2;
   }

   public HttpRequest checkPxScore() {
      HttpRequest var1 = this.client.postAbs("https://www.walmart.com/swag/v1/ads?categoryId=5428_4089_7375787_5983914_4013061_2480235&debug=true&isDealsPage=false&isManualShelf=false&pageId=39649584&pageType=item&platform=mobile&zipCode=20147").as(BodyCodec.buffer());
      var1.putHeader("content-length", "DEFAULT_VALUE");
      var1.putHeader("pragma", "no-cache");
      var1.putHeader("cache-control", "no-cache");
      var1.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"95\", \"Chromium\";v=\"95\", \";Not A Brand\";v=\"99\"");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      var1.putHeader("sec-ch-ua-mobile", "0?");
      var1.putHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36");
      var1.putHeader("correlator", "1");
      var1.putHeader("content-type", "application/json");
      var1.putHeader("accept", "*/*");
      var1.putHeader("sec-ch-ua-platform", "\"macOS\"");
      var1.putHeader("origin", "https://www.walmart.com");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "cors");
      var1.putHeader("sec-fetch-dest", "empty");
      var1.putHeader("referer", "https://www.walmart.com/ip/Equate-91-Isopropyl-Alcohol-Liquid-Antiseptic-32-fl-oz-Twin-Pack/979211867?athcpid=979211867&athpgid=AthenaHomepageDesktop&athcgid=null&athznid=bs&athieid=v0&athstid=CS020&athguid=SVOK3Fr5xaXlOnglHsVbfUzdxeOiTHfEZWMC&athancid=null&athena=true");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("cookie", "DEFAULT_VALUE");
      return var1;
   }

   public Buffer setPaymentForm(String var1) {
      JsonObject var2 = new JsonObject();
      var2.put("query", "mutation SetPayment( $payment:SetPaymentInput! $promosEnable:Boolean! $wplusEnabled:Boolean! ){setPayment(input:$payment){...ContractFragment}}fragment ContractFragment on PurchaseContract{id associateDiscountStatus addressMode tenderPlanId papEbtAllowed allowedPaymentGroupTypes cartCustomerContext{membershipData{isActiveMember status isActiveMember}}cartCustomerContext @include(if:$wplusEnabled){isMembershipOptedIn isEligibleForFreeTrial paymentData{hasCreditCard hasPaymentCardOnFile}membershipData{isPaidMember}}cartCustomerContext @skip(if:$wplusEnabled){paymentData{hasPaymentCardOnFile}membershipData{isPaidMember}}checkoutError{code errorData{__typename...on OutOfStock{offerId}__typename...on UnavailableOffer{offerId}__typename...on ItemExpired{offerId}__typename...on ItemQuantityAdjusted{offerId requestedQuantity adjustedQuantity}}operationalErrorCode message}checkoutableWarnings{code itemIds}allocationStatus payments{id paymentType cardType lastFour isDefault cvvRequired preferenceId paymentPreferenceId paymentHandle expiryMonth expiryYear firstName lastName email amountPaid cardImage cardImageAlt isLinkedCard capOneReward{credentialId redemptionUrl redemptionRate redemptionMethod rewardPointsBalance rewardPointsSelected rewardAmountSelected}remainingBalance{displayValue value}}order{id status orderVersion mobileNumber}terms{alcoholAccepted bagFeeAccepted smsOptInAccepted marketingEmailPrefOptIn}donationDetails{charityEIN charityName amount{displayValue value}acceptDonation}lineItems{...LineItemFields}tippingDetails{suggestedAmounts{value displayValue}maxAmount{value displayValue}selectedTippingAmount{value displayValue}}customer{id firstName lastName isGuest email phone}fulfillment{deliveryDetails{deliveryInstructions deliveryOption}pickupChoices{isSelected fulfillmentType accessType accessMode accessPointId}deliveryAddress{...AddressFields}alternatePickupPerson{...PickupPersonFields}primaryPickupPerson{...PickupPersonFields}fulfillmentItemGroups{...FulfillmentItemGroupsFields}}priceDetails{subTotal{...PriceDetailRowFields}totalItemQuantity fees{...PriceDetailRowFields}taxTotal{...PriceDetailRowFields}grandTotal{...PriceDetailRowFields}belowMinimumFee{...PriceDetailRowFields}authorizationAmount{...PriceDetailRowFields}weightDebitTotal{...PriceDetailRowFields}discounts{...PriceDetailRowFields}otcDeliveryBenefit{...PriceDetailRowFields}ebtSnapMaxEligible{...PriceDetailRowFields}ebtCashMaxEligible{...PriceDetailRowFields}hasAmountUnallocated affirm{__typename message{...AffirmMessageFields}}}checkoutGiftingDetails{isCheckoutGiftingOptin isWalmartProtectionPlanPresent isAppleCarePresent isRestrictedPaymentPresent giftMessageDetails{giftingMessage recipientEmail recipientName senderName}}promotions @include(if:$promosEnable){displayValue promoId terms}serverTime showPromotions @include(if:$promosEnable) errors{code message lineItems{...LineItemFields}}}fragment LineItemFields on LineItem{id quantity quantityString quantityLabel accessibilityQuantityLabel isPreOrder isWplusEarlyAccess isEventActive eventType fulfillmentSourcingDetails{currentSelection requestedSelection}packageQuantity priceInfo{priceDisplayCodes{showItemPrice priceDisplayCondition finalCostByWeight}itemPrice{displayValue value}linePrice{displayValue value}preDiscountedLinePrice{displayValue value}wasPrice{displayValue value}unitPrice{displayValue value}}isSubstitutionSelected isGiftEligible expiresAt showExpirationTimer selectedVariants{name value}product{...ProductFields}discounts{key label value @include(if:$promosEnable) terms subType displayValue @include(if:$promosEnable) displayLabel}wirelessPlan{planId mobileNumber __typename postPaidPlan{...postpaidPlanDetailsFragment}}selectedAddOnServices{offerId quantity groupType}registryInfo{registryId registryType}}fragment postpaidPlanDetailsFragment on PostPaidPlan{__typename espOrderSummaryId espOrderId espOrderLineId warpOrderId warpSessionId devicePayment{...postpaidPlanPriceFragment}devicePlan{__typename price{...postpaidPlanPriceFragment}frequency duration annualPercentageRate}deviceDataPlan{...deviceDataPlanFragment}}fragment deviceDataPlanFragment on DeviceDataPlan{__typename carrierName planType expiryTime activationFee{...postpaidPlanPriceFragment}planDetails{__typename price{...postpaidPlanPriceFragment}frequency name}agreements{...agreementFragment}}fragment postpaidPlanPriceFragment on PriceDetailRow{__typename key label displayValue value strikeOutDisplayValue strikeOutValue info{__typename title message}}fragment agreementFragment on CarrierAgreement{__typename name type format value docTitle label}fragment preOrderFragment on PreOrder{streetDate streetDateDisplayable streetDateType isPreOrder preOrderMessage preOrderStreetDateMessage}fragment AddressFields on Address{id addressLineOne addressLineTwo city state postalCode firstName lastName phone}fragment PickupPersonFields on PickupPerson{id firstName lastName email}fragment PriceDetailRowFields on PriceDetailRow{__typename key label displayValue value strikeOutValue strikeOutDisplayValue info{__typename title message}}fragment AccessPointFields on AccessPoint{id name assortmentStoreId displayName timeZone address{id addressLineOne addressLineTwo city state postalCode firstName lastName phone}isTest allowBagFee bagFeeValue isExpressEligible fulfillmentOption instructions nodeAccessType}fragment ReservationFields on Reservation{id expiryTime isUnscheduled expired showSlotExpiredError reservedSlot{__typename...on RegularSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata slotExpiryTime endTime available supportedTimeZone}...on DynamicExpressSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime endTime fulfillmentType slotMetadata slotExpiryTime available slaInMins sla{value displayValue}maxItemAllowed supportedTimeZone}...on UnscheduledSlot{price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata unscheduledHoldInDays supportedTimeZone}...on InHomeSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata slotExpiryTime endTime available supportedTimeZone}}}fragment AffirmMessageFields on AffirmMessage{__typename description termsUrl imageUrl monthlyPayment termLength isZeroAPR}fragment FulfillmentItemGroupsFields on FulfillmentItemGroup{...on SCGroup{__typename defaultMode collapsedItemIds itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}...on DigitalDeliveryGroup{__typename defaultMode collapsedItemIds itemGroups{__typename label itemIds}}...on Unscheduled{__typename defaultMode collapsedItemIds isSpecialEvent startDate endDate itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}...on FCGroup{__typename defaultMode collapsedItemIds startDate endDate isUnscheduledDeliveryEligible shippingOptions{__typename itemIds availableShippingOptions{__typename id shippingMethod deliveryDate price{__typename displayValue value}label{prefix suffix}isSelected isDefault}}hasMadeShippingChanges slaGroups{__typename label deliveryDate warningLabel sellerGroups{__typename id name isProSeller type shipOptionGroup{__typename deliveryPrice{__typename displayValue value}itemIds shipMethod}}}}...on AutoCareCenter{__typename defaultMode startDate endDate accBasketType collapsedItemIds itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}}fragment ProductFields on Product{id name usItemId itemType imageInfo{thumbnailUrl}category{categoryPath}offerId orderLimit orderMinLimit weightIncrement weightUnit averageWeight salesUnitType availabilityStatus isSubstitutionEligible isAlcohol configuration hasSellerBadge sellerId sellerName sellerType annualEvent preOrder{...preOrderFragment}badges{flags{__typename id key text}}addOnServices{serviceType groups{groupType services{selectedDisplayName offerId currentPrice{priceString}}}}}");
      var2.put("variables", (new JsonObject()).put("payment", (new JsonObject()).put("contractId", var1).put("preferenceId", this.paymentId).put("paymentType", "CREDITCARD")).put("promosEnable", true).put("wplusEnabled", true));
      return var2.toBuffer();
   }

   public Buffer getAccountPageForm() {
      return (new JsonObject("{\"query\":\"query accountLandingPage{account{profile{firstName hasCCPARequest phoneNumber emailAddress isPhoneValidated isEmailValidated associateInfo{isEligibleForDiscount}accountCreatedDate{displayValue}}hasItemSubscription membership{status daysRemaining membershipType savings{money time{display accessibility timeSavedNumericValue}moneySavings{display value}}enrolledDate{displayValue}canceledDate{displayValue}freeTrialEndDate{displayValue}endDate{displayValue}plan{benefits{code}}}}}\",\"variables\":{}}")).toBuffer();
   }

   public HttpRequest blockPage() {
      HttpRequest var1 = this.client.getAbs("https://www.walmart.com/blocked").as(BodyCodec.buffer());
      var1.putHeader("pragma", "no-cache");
      var1.putHeader("cache-control", "no-cache");
      var1.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"95\", \"Chromium\";v=\"95\", \";Not A Brand\";v=\"99\"");
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("sec-ch-ua-platform", "\"macOS\"");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36");
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("sec-fetch-site", "none");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", "en-US,en;q=0.9");
      return var1;
   }

   public CookieJar cookieStore() {
      return this.getWebClient().cookieStore();
   }

   public HttpRequest createContract() {
      String var1 = zs2y(32);
      HttpRequest var2 = this.client.postAbs("https://www.walmart.com/orchestra/cartxo/graphql").as(BodyCodec.buffer());
      var2.headers().setAll(Headers$Pseudo.MASP.get()).set(HttpHeaders.CONTENT_LENGTH, Headers.DEFAULT).set(Headers.SEC_CH_UA, this.getPxAPI().getDeviceSecUA()).set(X_O_CORRELATION_ID, var1).set(DEVICE_PROFILE_REF, this.deviceProfileId).set(X_LATENCY_TRACE, "1").set(WM_MP, TRUE).set(X_O_PLATFORM_VERSION, this.platformVersion).set(X_O_SEGMENT, OAOH).set(X_O_GQL_QUERY, "mutation CreateContract").set(X_APOLLO_OPERATION_NAME, "CreateContract").set(Headers.SEC_CH_UA_PLATFORM, Headers.WINDOWS).set(Headers.SEC_CH_UA_MOBILE, Headers._Q0).set(HttpHeaders.USER_AGENT, this.pxAPI.getDeviceUA()).set(X_O_PLATFORM, X_O_PLATFORM_NAME).set(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON).set(HttpHeaders.ACCEPT, HttpHeaderValues.APPLICATION_JSON).set(X_ENABLE_SERVER_TIMING, "1").set(X_O_CCM, SERVER).set("x-o-tp-phase", this.tpPhase).set(WM_CORRELATION_ID, var1).set(HttpHeaders.ORIGIN, "https://www.walmart.com").set(Headers.SEC_FETCH_SITE, Headers.SAME_ORIGIN).set(Headers.SEC_FETCH_MODE, Headers.CORS).set(Headers.SEC_FETCH_DEST, Headers.EMPTY).set(HttpHeaders.REFERER, "https://www.walmart.com/checkout/review-order?cartId=" + this.cartId).set(HttpHeaders.ACCEPT_ENCODING, Headers.GZIP_DEFLATE_BR).set(HttpHeaders.ACCEPT_LANGUAGE, this.pxAPI.getDeviceLang()).set(HttpHeaders.COOKIE, Headers.DEFAULT);
      return var2;
   }

   public HttpRequest buyNow() {
      this.cookieStore().removeAnyMatch("tb-c30");
      String var1 = zs2y(32);
      HttpRequest var2 = this.client.postAbs("https://www.walmart.com/orchestra/cartxo/graphql").as(BodyCodec.buffer());
      var2.headers().setAll(Headers$Pseudo.MASP.get()).set(HttpHeaders.CONTENT_LENGTH, Headers.DEFAULT).set(X_O_SEGMENT, OAOH).set(X_O_CORRELATION_ID, var1).set(DEVICE_PROFILE_REF, this.deviceProfileId).set(X_LATENCY_TRACE, "1").set(WM_MP, TRUE).set(X_O_PLATFORM_VERSION, this.platformVersion).set(X_O_GQL_QUERY, "mutation CreateBuyNowContract").set(WM_PAGE_URL, this.referer).set(X_APOLLO_OPERATION_NAME, "CreateBuyNowContract").set(HttpHeaders.USER_AGENT, this.pxAPI.getDeviceUA()).set(X_O_PLATFORM, X_O_PLATFORM_NAME).set(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON).set(HttpHeaders.ACCEPT, HttpHeaderValues.APPLICATION_JSON).set(X_ENABLE_SERVER_TIMING, "1").set(X_O_CCM, SERVER).set(WM_CORRELATION_ID, var1).set(HttpHeaders.ORIGIN, "https://www.walmart.com").set(Headers.SEC_FETCH_SITE, Headers.SAME_ORIGIN).set(Headers.SEC_FETCH_MODE, Headers.CORS).set(Headers.SEC_FETCH_DEST, Headers.EMPTY).set(HttpHeaders.REFERER, this.referer).set(HttpHeaders.ACCEPT_ENCODING, Headers.GZIP_DEFLATE_BR).set(HttpHeaders.ACCEPT_LANGUAGE, this.pxAPI.getDeviceLang()).set(HttpHeaders.COOKIE, Headers.DEFAULT);
      return var2;
   }

   public HttpRequest productPageCrossSite(String var1) {
      HttpRequest var2 = this.client.getAbs(var1).as(BodyCodec.buffer());
      var2.putHeader("upgrade-insecure-requests", "1");
      var2.putHeader("user-agent", this.pxAPI.getDeviceUA());
      var2.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var2.putHeader("sec-fetch-site", "cross-site");
      var2.putHeader("sec-fetch-mode", "navigate");
      var2.putHeader("sec-fetch-user", "?1");
      var2.putHeader("sec-fetch-dest", "document");
      var2.putHeader("accept-encoding", "gzip, deflate, br");
      var2.putHeader("accept-language", this.pxAPI.getDeviceLang());
      return var2;
   }

   public CompletableFuture handleBadResponse(HttpResponse var1, String var2) {
      this.pxAPI.updatePxhd(this.cookieStore().getCookieValue("_pxhd"));
      String var4;
      CompletableFuture var8;
      CompletableFuture var9;
      CompletableFuture var10000;
      CompletableFuture var10001;
      switch (var1.statusCode()) {
         case 307:
            try {
               if (this.task.getMode().contains("skip")) {
                  var10000 = this.generatePX(true);
                  if (!var10000.isDone()) {
                     var8 = var10000;
                     return var8.exceptionally(Function.identity()).thenCompose(WalmartNewAPI::async$handleBadResponse);
                  }

                  var10000.join();
                  return CompletableFuture.completedFuture(false);
               } else {
                  String var11 = "https://www.walmart.com" + var1.getHeader("location");
                  var4 = var11.split("uuid=")[1].split("&")[0];
                  Matcher var12 = VID_LOCATION_PATTERN.matcher(var11);
                  String var13;
                  if (var12.find()) {
                     var13 = var12.group(1).isBlank() ? null : var12.group(1);
                  } else {
                     var13 = null;
                  }

                  var10001 = this.pxAPI.solveCaptcha(var13, var4, var11);
                  if (!var10001.isDone()) {
                     var9 = var10001;
                     return var9.exceptionally(Function.identity()).thenCompose(WalmartNewAPI::async$handleBadResponse);
                  }

                  boolean var7 = this.parseMapIntoCookieJar((MultiMap)var10001.join());
                  return CompletableFuture.completedFuture(var7);
               }
            } catch (Throwable var10) {
               System.out.println("Error solving captcha [DESKTOP] " + var10.getMessage());
            }
         case 444:
            if (super.rotateProxy()) {
               this.pxAPI.restartClient(super.client);
            }
         default:
            return CompletableFuture.completedFuture(false);
         case 412:
            if (this.task.getMode().contains("skip")) {
               var10000 = this.generatePX(true);
               if (!var10000.isDone()) {
                  var8 = var10000;
                  return var8.exceptionally(Function.identity()).thenCompose(WalmartNewAPI::async$handleBadResponse);
               } else {
                  var10000.join();
                  return CompletableFuture.completedFuture(false);
               }
            } else {
               JsonObject var3 = var1.bodyAsJsonObject();
               var4 = var3.getString("uuid");
               String var5 = var3.getString("vid");
               var10001 = this.pxAPI.solveCaptcha(var5, var4, var2);
               if (!var10001.isDone()) {
                  var9 = var10001;
                  return var9.exceptionally(Function.identity()).thenCompose(WalmartNewAPI::async$handleBadResponse);
               } else {
                  byte var6 = this.parseMapIntoCookieJar((MultiMap)var10001.join());
                  var10000 = this.generatePX(false);
                  if (!var10000.isDone()) {
                     var8 = var10000;
                     return var8.exceptionally(Function.identity()).thenCompose(WalmartNewAPI::async$handleBadResponse);
                  } else {
                     var10000.join();
                     return CompletableFuture.completedFuture(Boolean.valueOf((boolean)var6));
                  }
               }
            }
      }
   }

   public HttpRequest queuePage(String var1) {
      HttpRequest var2 = this.client.getAbs(var1).as(BodyCodec.buffer());
      var2.putHeader("upgrade-insecure-requests", "1");
      var2.putHeader("user-agent", this.pxAPI.getDeviceUA());
      var2.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var2.putHeader("sec-fetch-site", "none");
      var2.putHeader("sec-fetch-mode", "navigate");
      var2.putHeader("sec-fetch-user", "?1");
      var2.putHeader("sec-fetch-dest", "document");
      var2.putHeader("sec-ch-ua", this.pxAPI.getDeviceSecUA());
      var2.putHeader("sec-ch-ua-mobile", "?0");
      var2.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var2.putHeader("accept-encoding", "gzip, deflate, br");
      var2.putHeader("accept-language", this.pxAPI.getDeviceLang());
      return var2;
   }

   public HttpRequest issueTicket(String var1) {
      String var2 = zs2y(32);
      HttpRequest var3 = this.client.getAbs(var1).as(BodyCodec.buffer());
      var3.putHeader("sec-ch-ua", this.pxAPI.getDeviceSecUA());
      var3.putHeader("wm_qos.correlation_id", var2);
      var3.putHeader("x-o-correlation-id", var2);
      var3.putHeader("sec-ch-ua-mobile", "?0");
      var3.putHeader("device_profile_ref_id", this.deviceProfileId);
      var3.putHeader("x-o-segment", OAOH.toString());
      var3.putHeader("wm_mp", "true");
      var3.putHeader("content-type", "application/json");
      var3.putHeader("accept", "application/json");
      var3.putHeader("x-o-platform-version", this.platformVersion);
      var3.putHeader("x-latency-trace", "1");
      var3.putHeader("x-enable-server-timing", "1");
      var3.putHeader("x-o-platform", X_O_PLATFORM_NAME.toString());
      var3.putHeader("user-agent", this.pxAPI.getDeviceUA());
      var3.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var3.putHeader("origin", "https://www.walmart.com");
      var3.putHeader("sec-fetch-site", "same-site");
      var3.putHeader("sec-fetch-mode", "cors");
      var3.putHeader("sec-fetch-dest", "empty");
      var3.putHeader("referer", "https://www.walmart.com/");
      var3.putHeader("accept-encoding", "gzip, deflate");
      var3.putHeader("accept-language", this.pxAPI.getDeviceLang());
      return var3;
   }

   public JsonObject createContractBody() {
      JsonObject var1 = new JsonObject();
      var1.put("query", "mutation CreateContract( $createContractInput:CreatePurchaseContractInput! $promosEnable:Boolean! $wplusEnabled:Boolean! ){createPurchaseContract(input:$createContractInput){...ContractFragment}}fragment ContractFragment on PurchaseContract{id associateDiscountStatus tenderPlanId papEbtAllowed cartCustomerContext @include(if:$wplusEnabled){isMembershipOptedIn isEligibleForFreeTrial paymentData{hasCreditCard}}checkoutError{code errorData{__typename...on OutOfStock{offerId}__typename...on UnavailableOffer{offerId}__typename...on ItemExpired{offerId}__typename...on ItemQuantityAdjusted{offerId requestedQuantity adjustedQuantity}}operationalErrorCode message}checkoutableWarnings{code itemIds}allocationStatus payments{id paymentType cardType lastFour isDefault cvvRequired preferenceId paymentPreferenceId expiryMonth expiryYear firstName lastName amountPaid cardImage cardImageAlt isLinkedCard capOneReward{credentialId redemptionUrl redemptionRate redemptionMethod rewardPointsBalance rewardPointsSelected rewardAmountSelected}remainingBalance{displayValue value}}order{id status orderVersion mobileNumber}terms{alcoholAccepted bagFeeAccepted smsOptInAccepted marketingEmailPrefOptIn}donationDetails{charityEIN charityName amount{displayValue value}acceptDonation}lineItems{...LineItemFields}tippingDetails{suggestedAmounts{value displayValue}maxAmount{value displayValue}selectedTippingAmount{value displayValue}}customer{id firstName lastName isGuest email phone}fulfillment{deliveryDetails{deliveryInstructions deliveryOption}pickupChoices{isSelected fulfillmentType accessType accessMode accessPointId}deliveryAddress{...AddressFields}alternatePickupPerson{...PickupPersonFields}primaryPickupPerson{...PickupPersonFields}fulfillmentItemGroups{...on SCGroup{__typename defaultMode collapsedItemIds itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}...on DigitalDeliveryGroup{__typename defaultMode collapsedItemIds itemGroups{__typename label itemIds}}...on Unscheduled{__typename defaultMode collapsedItemIds itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}...on FCGroup{__typename defaultMode collapsedItemIds startDate endDate shippingOptions{__typename itemIds availableShippingOptions{__typename id shippingMethod deliveryDate price{__typename displayValue value}label{prefix suffix}isSelected isDefault}}hasMadeShippingChanges slaGroups{__typename label deliveryDate warningLabel sellerGroups{__typename id name isProSeller type shipOptionGroup{__typename deliveryPrice{__typename displayValue value}itemIds shipMethod}}}}...on AutoCareCenter{__typename defaultMode startDate endDate accBasketType collapsedItemIds itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}}accessPoint{...AccessPointFields}reservation{...ReservationFields}}priceDetails{subTotal{...PriceDetailRowFields}totalItemQuantity fees{...PriceDetailRowFields}taxTotal{...PriceDetailRowFields}grandTotal{...PriceDetailRowFields}belowMinimumFee{...PriceDetailRowFields}authorizationAmount{...PriceDetailRowFields}weightDebitTotal{...PriceDetailRowFields}discounts{...PriceDetailRowFields}otcDeliveryBenefit{...PriceDetailRowFields}ebtSnapMaxEligible{...PriceDetailRowFields}ebtCashMaxEligible{...PriceDetailRowFields}hasAmountUnallocated}promotions @include(if:$promosEnable){displayValue promoId terms}showPromotions @include(if:$promosEnable) errors{code message lineItems{...LineItemFields}}}fragment LineItemFields on LineItem{id quantity quantityString quantityLabel accessibilityQuantityLabel isPreOrder fulfillmentSourcingDetails{currentSelection requestedSelection}priceInfo{priceDisplayCodes{showItemPrice priceDisplayCondition finalCostByWeight}itemPrice{displayValue value}linePrice{displayValue value}preDiscountedLinePrice{displayValue value}wasPrice{displayValue value}unitPrice{displayValue value}}isSubstitutionSelected selectedVariants{name value}product{id name usItemId imageInfo{thumbnailUrl}offerId orderLimit orderMinLimit weightIncrement weightUnit averageWeight salesUnitType availabilityStatus isSubstitutionEligible isAlcohol configuration hasSellerBadge sellerId sellerName sellerType preOrder{...preOrderFragment}}discounts{key label displayValue @include(if:$promosEnable) displayLabel @include(if:$promosEnable)}wirelessPlan{planId mobileNumber __typename postPaidPlan{...postpaidPlanDetailsFragment}}}fragment postpaidPlanDetailsFragment on PostPaidPlan{__typename espOrderSummaryId espOrderId espOrderLineId warpOrderId warpSessionId devicePayment{...postpaidPlanPriceFragment}devicePlan{__typename price{...postpaidPlanPriceFragment}frequency duration annualPercentageRate}deviceDataPlan{...deviceDataPlanFragment}}fragment deviceDataPlanFragment on DeviceDataPlan{__typename carrierName planType expiryTime activationFee{...postpaidPlanPriceFragment}planDetails{__typename price{...postpaidPlanPriceFragment}frequency name}agreements{...agreementFragment}}fragment postpaidPlanPriceFragment on PriceDetailRow{__typename key label displayValue value strikeOutDisplayValue strikeOutValue info{__typename title message}}fragment agreementFragment on CarrierAgreement{__typename name type format value docTitle label}fragment preOrderFragment on PreOrder{streetDate streetDateDisplayable streetDateType isPreOrder preOrderMessage preOrderStreetDateMessage}fragment AddressFields on Address{id addressLineOne addressLineTwo city state postalCode firstName lastName phone}fragment PickupPersonFields on PickupPerson{id firstName lastName email}fragment PriceDetailRowFields on PriceDetailRow{__typename key label displayValue value strikeOutValue strikeOutDisplayValue info{__typename title message}}fragment AccessPointFields on AccessPoint{id name assortmentStoreId displayName timeZone address{id addressLineOne addressLineTwo city state postalCode firstName lastName phone}isTest allowBagFee bagFeeValue isExpressEligible fulfillmentOption instructions accessType}fragment ReservationFields on Reservation{id expiryTime isUnscheduled expired showSlotExpiredError reservedSlot{__typename...on RegularSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata slotExpiryTime endTime available supportedTimeZone}...on DynamicExpressSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime endTime fulfillmentType slotMetadata slotExpiryTime available slaInMins maxItemAllowed supportedTimeZone}...on UnscheduledSlot{price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata unscheduledHoldInDays supportedTimeZone}...on InHomeSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata slotExpiryTime endTime available supportedTimeZone}}}");
      var1.put("variables", (new JsonObject()).put("createContractInput", (new JsonObject()).put("cartId", this.cartId)).put("promosEnable", true).put("wplusEnabled", true));
      return var1;
   }

   public static CompletableFuture async$initialisePX(WalmartNewAPI var0, CompletableFuture var1, int var2, Object var3) {
      CompletableFuture var10000;
      switch (var2) {
         case 0:
            var10000 = var0.pxAPI.initialise();
            if (!var10000.isDone()) {
               var1 = var10000;
               return var1.exceptionally(Function.identity()).thenCompose(WalmartNewAPI::async$initialisePX);
            }
            break;
         case 1:
            var10000 = var1;
            break;
         default:
            throw new IllegalArgumentException();
      }

      var10000.join();
      return CompletableFuture.completedFuture(true);
   }

   public HttpRequest getContract() {
      String var1 = zs2y(32);
      HttpRequest var2 = this.client.postAbs("https://www.walmart.com/orchestra/cartxo/graphql").as(BodyCodec.buffer());
      var2.headers().setAll(Headers$Pseudo.MASP.get()).set(HttpHeaders.CONTENT_LENGTH, Headers.DEFAULT).set(Headers.SEC_CH_UA, this.getPxAPI().getDeviceSecUA()).set(X_O_CORRELATION_ID, var1).set(DEVICE_PROFILE_REF, this.deviceProfileId).set(X_LATENCY_TRACE, "1").set(WM_MP, TRUE).set(X_O_PLATFORM_VERSION, this.platformVersion).set(X_O_SEGMENT, OAOH).set(X_O_GQL_QUERY, "query getPurchaseContract").set(X_APOLLO_OPERATION_NAME, "getPurchaseContract").set(Headers.SEC_CH_UA_PLATFORM, Headers.WINDOWS).set(Headers.SEC_CH_UA_MOBILE, Headers._Q0).set(HttpHeaders.USER_AGENT, this.pxAPI.getDeviceUA()).set(X_O_PLATFORM, X_O_PLATFORM_NAME).set(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON).set(HttpHeaders.ACCEPT, HttpHeaderValues.APPLICATION_JSON).set(X_ENABLE_SERVER_TIMING, "1").set(X_O_CCM, SERVER).set(WM_CORRELATION_ID, var1).set(HttpHeaders.ORIGIN, "https://www.walmart.com").set(Headers.SEC_FETCH_SITE, Headers.SAME_ORIGIN).set(Headers.SEC_FETCH_MODE, Headers.CORS).set(Headers.SEC_FETCH_DEST, Headers.EMPTY).set(HttpHeaders.REFERER, "https://www.walmart.com/checkout/review-order?pcid=" + this.contractId + "&buynow=1").set(HttpHeaders.ACCEPT_ENCODING, Headers.GZIP_DEFLATE_BR).set(HttpHeaders.ACCEPT_LANGUAGE, this.pxAPI.getDeviceLang()).set(HttpHeaders.COOKIE, Headers.DEFAULT);
      return var2;
   }

   public JsonObject getDeliveryAddressesForm() {
      JsonObject var1 = new JsonObject();
      var1.put("query", "query GetDeliveryAddresses( $DeliveryAddressOptionInput:DeliveryAddressOptionInput ){deliveryAddresses(input:$DeliveryAddressOptionInput){accessPoint{...AccessPoint}id...BaseAddressResponse...RegistryAddressResponse}}fragment BasicAddress on AccountAddressBase{addressLineOne addressLineTwo city state postalCode}fragment BaseAddressResponse on AccountAddress{...BasicAddress firstName lastName phone isDefault deliveryInstructions serviceStatus capabilities allowEditOrRemove}fragment AccessPoint on AccessPointRovr{id assortmentStoreId fulfillmentType accountFulfillmentOption accountAccessType}fragment RegistryAddressResponse on AccountAddress{registry{id type}addressTitle}");
      var1.put("variables", (new JsonObject()).put("responseGroup", "storeDeliverable"));
      return var1;
   }

   public HttpRequest updateTender() {
      String var1 = zs2y(32);
      HttpRequest var2 = this.client.postAbs("https://www.walmart.com/orchestra/cartxo/graphql").as(BodyCodec.buffer());
      var2.headers().setAll(Headers$Pseudo.MASP.get()).set(HttpHeaders.CONTENT_LENGTH, Headers.DEFAULT).set(Headers.SEC_CH_UA, this.getPxAPI().getDeviceSecUA()).set(X_O_CORRELATION_ID, var1).set(DEVICE_PROFILE_REF, this.deviceProfileId).set(X_LATENCY_TRACE, "1").set(WM_MP, TRUE).set(X_O_PLATFORM_VERSION, this.platformVersion).set(X_O_SEGMENT, OAOH).set(X_O_GQL_QUERY, "mutation updateTenderPlan").set(X_APOLLO_OPERATION_NAME, "updateTenderPlan").set(Headers.SEC_CH_UA_PLATFORM, Headers.WINDOWS).set(Headers.SEC_CH_UA_MOBILE, Headers._Q0).set(HttpHeaders.USER_AGENT, this.pxAPI.getDeviceUA()).set(X_O_PLATFORM, X_O_PLATFORM_NAME).set(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON).set(HttpHeaders.ACCEPT, HttpHeaderValues.APPLICATION_JSON).set(X_ENABLE_SERVER_TIMING, "1").set(X_O_CCM, SERVER).set("x-o-tp-phase", this.tpPhase).set(WM_CORRELATION_ID, var1).set(HttpHeaders.ORIGIN, "https://www.walmart.com").set(Headers.SEC_FETCH_SITE, Headers.SAME_ORIGIN).set(Headers.SEC_FETCH_MODE, Headers.CORS).set(Headers.SEC_FETCH_DEST, Headers.EMPTY).set(HttpHeaders.REFERER, "https://www.walmart.com/checkout/review-order?cartId=" + this.cartId + "&wv=add_credit_debit_card").set(HttpHeaders.ACCEPT_ENCODING, Headers.GZIP_DEFLATE_BR).set(HttpHeaders.ACCEPT_LANGUAGE, this.pxAPI.getDeviceLang()).set(HttpHeaders.COOKIE, Headers.DEFAULT);
      return var2;
   }

   public static String zs2y(int var0) {
      StringBuilder var1 = new StringBuilder();
      int[] var2 = new int[var0];

      int var3;
      for(var3 = 0; var3 < var2.length; ++var3) {
         var2[var3] = ThreadLocalRandom.current().nextInt(256);
      }

      while(var0 > 0) {
         --var0;
         var3 = 63 & var2[var0];
         var1.append(var3 < 36 ? Integer.toString(var3, 36) : (var3 < 62 ? Integer.toString(var3 - 26, 36).toUpperCase() : (var3 < 63 ? "_" : "-")));
      }

      return var1.toString();
   }

   public HttpRequest getAccountPage() {
      String var1 = zs2y(32);
      HttpRequest var2 = this.client.postAbs("https://www.walmart.com/orchestra/home/graphql").as(BodyCodec.buffer());
      var2.headers().setAll(Headers$Pseudo.MASP.get()).set(HttpHeaders.CONTENT_LENGTH, Headers.DEFAULT).set(Headers.SEC_CH_UA, this.pxAPI.getDeviceSecUA()).set(X_O_CORRELATION_ID, var1).set(DEVICE_PROFILE_REF, this.deviceProfileId).set(X_LATENCY_TRACE, "1").set(WM_MP, TRUE).set(X_O_PLATFORM_VERSION, this.platformVersion).set(X_O_SEGMENT, OAOH).set(X_O_GQL_QUERY, "query accountLandingPage").set(WM_PAGE_URL, "https://www.walmart.com/").set(X_APOLLO_OPERATION_NAME, "accountLandingPage").set(Headers.SEC_CH_UA_PLATFORM, Headers.WINDOWS).set(Headers.SEC_CH_UA_MOBILE, Headers._Q0).set(HttpHeaders.USER_AGENT, this.pxAPI.getDeviceUA()).set(X_O_PLATFORM, X_O_PLATFORM_NAME).set(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON).set(HttpHeaders.ACCEPT, HttpHeaderValues.APPLICATION_JSON).set(X_ENABLE_SERVER_TIMING, "1").set(X_O_CCM, SERVER).set(WM_CORRELATION_ID, var1).set(HttpHeaders.ORIGIN, "https://www.walmart.com").set(Headers.SEC_FETCH_SITE, Headers.SAME_ORIGIN).set(Headers.SEC_FETCH_MODE, Headers.CORS).set(Headers.SEC_FETCH_DEST, Headers.EMPTY).set(HttpHeaders.REFERER, "https://www.walmart.com/wallet").set(HttpHeaders.ACCEPT_ENCODING, Headers.GZIP_DEFLATE_BR).set(HttpHeaders.ACCEPT_LANGUAGE, this.pxAPI.getDeviceLang()).set(HttpHeaders.COOKIE, Headers.DEFAULT);
      return var2;
   }

   public HttpRequest affilCrossSite(String var1) {
      return null;
   }

   public JsonObject atcForm() {
      JsonObject var1 = new JsonObject();
      var1.put("query", "mutation updateItems($input:UpdateItemsInput!,$detailed:Boolean!=false){updateItems(input:$input){id checkoutable customer@include(if:$detailed){id isGuest}lineItems{id quantity quantityString quantityLabel isGiftEligible createdDateTime isPreOrder@include(if:$detailed)product{name@include(if:$detailed)usItemId imageInfo@include(if:$detailed){thumbnailUrl}itemType offerId sellerId@include(if:$detailed)sellerName@include(if:$detailed)hasSellerBadge@include(if:$detailed)orderLimit orderMinLimit}}priceDetails{subTotal{value displayValue}}checkoutableErrors{code shouldDisableCheckout itemIds}checkoutableWarnings@include(if:$detailed){code itemIds}operationalErrors{offerId itemId requestedQuantity adjustedQuantity code upstreamErrorCode}}}");
      var1.put("variables", (new JsonObject()).put("input", (new JsonObject()).put("cartId", this.cartId).put("items", (new JsonArray()).add((new JsonObject()).put("offerId", this.task.getKeywords()[0]).put("quantity", Integer.parseInt(this.task.getSize()))))).put("detailed", false).put("includePartialFulfillmentSwitching", true).put("enableAEBadge", true).put("includeQueueing", true).put("includeExpressSla", true));
      return var1;
   }

   public HttpRequest getCheckoutPage() {
      HttpRequest var1 = this.client.getAbs("https://www.walmart.com/checkout/review-order?cartId=" + UUID.randomUUID()).as(BodyCodec.none());
      var1.putHeader("sec-ch-ua", this.pxAPI.getDeviceSecUA());
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("sec-ch-ua-platform", "\"macOS\"");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("user-agent", this.pxAPI.getDeviceUA());
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var1.putHeader("service-worker-navigation-preload", "true");
      var1.putHeader("sec-fetch-site", "same-origin");
      var1.putHeader("sec-fetch-mode", "navigate");
      var1.putHeader("sec-fetch-user", "?1");
      var1.putHeader("sec-fetch-dest", "document");
      var1.putHeader("referer", "https://www.walmart.com/ip/MOOSOO-Stainless-Steel-Dishwasher-Countertop-Dishwasher-Cleaner-with-6-Large-Place-Setting-Rack/243314301");
      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", this.pxAPI.getDeviceLang());
      return var1;
   }

   public HttpRequest finalizeShipping() {
      String var1 = zs2y(32);
      HttpRequest var2 = this.client.postAbs("https://www.walmart.com/orchestra/home/graphql").as(BodyCodec.buffer());
      var2.headers().setAll(Headers$Pseudo.MASP.get()).set(HttpHeaders.CONTENT_LENGTH, Headers.DEFAULT).set(X_O_SEGMENT, OAOH).set(X_O_CORRELATION_ID, var1).set(DEVICE_PROFILE_REF, this.deviceProfileId).set(X_LATENCY_TRACE, "1").set(WM_MP, TRUE).set(X_O_PLATFORM_VERSION, this.platformVersion).set(X_O_GQL_QUERY, "mutation setShipping").set(WM_PAGE_URL, this.productReferer).set(X_APOLLO_OPERATION_NAME, "setShipping").set(HttpHeaders.USER_AGENT, this.pxAPI.getDeviceUA()).set(X_O_PLATFORM, X_O_PLATFORM_NAME).set(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON).set(HttpHeaders.ACCEPT, HttpHeaderValues.APPLICATION_JSON).set(X_ENABLE_SERVER_TIMING, "1").set(X_O_CCM, SERVER).set(WM_CORRELATION_ID, var1).set(HttpHeaders.ORIGIN, "https://www.walmart.com").set(Headers.SEC_FETCH_SITE, Headers.SAME_ORIGIN).set(Headers.SEC_FETCH_MODE, Headers.CORS).set(Headers.SEC_FETCH_DEST, Headers.EMPTY).set(HttpHeaders.REFERER, this.productReferer).set(HttpHeaders.ACCEPT_ENCODING, Headers.GZIP_DEFLATE_BR).set(HttpHeaders.ACCEPT_LANGUAGE, this.pxAPI.getDeviceLang()).set(HttpHeaders.COOKIE, Headers.DEFAULT);
      return var2;
   }

   public HttpRequest homepage() {
      HttpRequest var1 = this.client.getAbs("https://www.walmart.com/").as(BodyCodec.buffer());
      boolean var2 = ThreadLocalRandom.current().nextBoolean();
      if (!var2) {
         var1.putHeader("cache-control", "max-age=0");
      }

      var1.putHeader("sec-ch-ua", this.pxAPI.getDeviceSecUA());
      var1.putHeader("sec-ch-ua-mobile", "?0");
      var1.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var1.putHeader("upgrade-insecure-requests", "1");
      var1.putHeader("user-agent", this.pxAPI.getDeviceUA());
      var1.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      if (!var2) {
         var1.putHeader("sec-fetch-site", "none");
         var1.putHeader("sec-fetch-mode", "navigate");
         var1.putHeader("sec-fetch-user", "?1");
         var1.putHeader("sec-fetch-dest", "document");
      } else {
         var1.putHeader("sec-fetch-site", "cross-site");
         var1.putHeader("sec-fetch-mode", "navigate");
         var1.putHeader("sec-fetch-user", "?1");
         var1.putHeader("sec-fetch-dest", "document");
         var1.putHeader("referer", "https://www.google.com/");
      }

      var1.putHeader("accept-encoding", "gzip, deflate, br");
      var1.putHeader("accept-language", this.pxAPI.getDeviceLang());
      return var1;
   }

   public CompletableFuture initialisePX() {
      CompletableFuture var10000 = this.pxAPI.initialise();
      if (!var10000.isDone()) {
         CompletableFuture var1 = var10000;
         return var1.exceptionally(Function.identity()).thenCompose(WalmartNewAPI::async$initialisePX);
      } else {
         var10000.join();
         return CompletableFuture.completedFuture(true);
      }
   }

   public HttpRequest deleteCard() {
      String var1 = zs2y(32);
      HttpRequest var2 = this.client.postAbs("https://www.walmart.com/orchestra/home/graphql").as(BodyCodec.buffer());
      var2.headers().setAll(Headers$Pseudo.MASP.get()).set(HttpHeaders.CONTENT_LENGTH, Headers.DEFAULT).set(X_O_CORRELATION_ID, var1).set(DEVICE_PROFILE_REF, this.deviceProfileId).set(X_O_SEGMENT, OAOH).set(WM_MP, TRUE).set(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON).set(HttpHeaders.ACCEPT, HttpHeaderValues.APPLICATION_JSON).set(X_O_PLATFORM_VERSION, this.platformVersion).set(X_O_GQL_QUERY, "mutation deleteCard").set(X_LATENCY_TRACE, "1").set(X_ENABLE_SERVER_TIMING, "1").set(X_O_PLATFORM, X_O_PLATFORM_NAME).set(X_O_CCM, SERVER).set(HttpHeaders.USER_AGENT, this.pxAPI.getDeviceUA()).set(X_APOLLO_OPERATION_NAME, "deleteCard").set(WM_CORRELATION_ID, var1).set(HttpHeaders.ORIGIN, "https://www.walmart.com").set(Headers.SEC_FETCH_SITE, Headers.SAME_ORIGIN).set(Headers.SEC_FETCH_MODE, Headers.CORS).set(Headers.SEC_FETCH_DEST, Headers.EMPTY).set(HttpHeaders.REFERER, "https://www.walmart.com/wallet").set(HttpHeaders.ACCEPT_ENCODING, Headers.GZIP_DEFLATE_BR).set(HttpHeaders.ACCEPT_LANGUAGE, this.pxAPI.getDeviceLang()).set(HttpHeaders.COOKIE, Headers.DEFAULT);
      return var2;
   }

   public void setAPI(PerimeterX var1) {
      this.pxAPI = var1;
   }

   public JsonObject buyNowPreloadBody() {
      JsonObject var1 = new JsonObject();
      JsonObject var2 = new JsonObject();
      var1.put("query", "mutation CreateBuyNowContract( $buyNowContractInput:BuyNowContractInput! $promosEnable:Boolean! $wplusEnabled:Boolean! ){createBuyNowContract(input:$buyNowContractInput){...ContractFragment}}fragment ContractFragment on PurchaseContract{id associateDiscountStatus addressMode tenderPlanId papEbtAllowed allowedPaymentGroupTypes cartCustomerContext @include(if:$wplusEnabled){isMembershipOptedIn isEligibleForFreeTrial paymentData{hasCreditCard}}checkoutError{code errorData{__typename...on OutOfStock{offerId}__typename...on UnavailableOffer{offerId}__typename...on ItemExpired{offerId}__typename...on ItemQuantityAdjusted{offerId requestedQuantity adjustedQuantity}}operationalErrorCode message}checkoutableWarnings{code itemIds}allocationStatus payments{id paymentType cardType lastFour isDefault cvvRequired preferenceId paymentPreferenceId paymentHandle expiryMonth expiryYear firstName lastName email amountPaid cardImage cardImageAlt isLinkedCard capOneReward{credentialId redemptionUrl redemptionRate redemptionMethod rewardPointsBalance rewardPointsSelected rewardAmountSelected}remainingBalance{displayValue value}}order{id status orderVersion mobileNumber}terms{alcoholAccepted bagFeeAccepted smsOptInAccepted marketingEmailPrefOptIn}donationDetails{charityEIN charityName amount{displayValue value}acceptDonation}lineItems{...LineItemFields}tippingDetails{suggestedAmounts{value displayValue}maxAmount{value displayValue}selectedTippingAmount{value displayValue}}customer{id firstName lastName isGuest email phone}fulfillment{deliveryDetails{deliveryInstructions deliveryOption}pickupChoices{isSelected fulfillmentType accessType accessMode accessPointId}deliveryAddress{...AddressFields}alternatePickupPerson{...PickupPersonFields}primaryPickupPerson{...PickupPersonFields}fulfillmentItemGroups{...FulfillmentItemGroupsFields}accessPoint{...AccessPointFields}reservation{...ReservationFields}}priceDetails{subTotal{...PriceDetailRowFields}totalItemQuantity fees{...PriceDetailRowFields}taxTotal{...PriceDetailRowFields}grandTotal{...PriceDetailRowFields}belowMinimumFee{...PriceDetailRowFields}authorizationAmount{...PriceDetailRowFields}weightDebitTotal{...PriceDetailRowFields}discounts{...PriceDetailRowFields}otcDeliveryBenefit{...PriceDetailRowFields}ebtSnapMaxEligible{...PriceDetailRowFields}ebtCashMaxEligible{...PriceDetailRowFields}hasAmountUnallocated affirm{__typename message{...AffirmMessageFields}}}checkoutGiftingDetails{isCheckoutGiftingOptin isWalmartProtectionPlanPresent isAppleCarePresent isRestrictedPaymentPresent giftMessageDetails{giftingMessage recipientEmail recipientName senderName}}promotions @include(if:$promosEnable){displayValue promoId terms}showPromotions @include(if:$promosEnable) errors{code message lineItems{...LineItemFields}}}fragment LineItemFields on LineItem{id quantity quantityString quantityLabel accessibilityQuantityLabel isPreOrder fulfillmentSourcingDetails{currentSelection requestedSelection}packageQuantity priceInfo{priceDisplayCodes{showItemPrice priceDisplayCondition finalCostByWeight}itemPrice{displayValue value}linePrice{displayValue value}preDiscountedLinePrice{displayValue value}wasPrice{displayValue value}unitPrice{displayValue value}}isSubstitutionSelected isGiftEligible selectedVariants{name value}product{id name usItemId itemType imageInfo{thumbnailUrl}offerId orderLimit orderMinLimit weightIncrement weightUnit averageWeight salesUnitType availabilityStatus isSubstitutionEligible isAlcohol configuration hasSellerBadge sellerId sellerName sellerType preOrder{...preOrderFragment}addOnServices{serviceType groups{groupType services{selectedDisplayName offerId currentPrice{priceString}}}}}discounts{key label displayValue @include(if:$promosEnable) displayLabel @include(if:$promosEnable)}wirelessPlan{planId mobileNumber __typename postPaidPlan{...postpaidPlanDetailsFragment}}selectedAddOnServices{offerId quantity groupType}registryInfo{registryId registryType}}fragment postpaidPlanDetailsFragment on PostPaidPlan{__typename espOrderSummaryId espOrderId espOrderLineId warpOrderId warpSessionId devicePayment{...postpaidPlanPriceFragment}devicePlan{__typename price{...postpaidPlanPriceFragment}frequency duration annualPercentageRate}deviceDataPlan{...deviceDataPlanFragment}}fragment deviceDataPlanFragment on DeviceDataPlan{__typename carrierName planType expiryTime activationFee{...postpaidPlanPriceFragment}planDetails{__typename price{...postpaidPlanPriceFragment}frequency name}agreements{...agreementFragment}}fragment postpaidPlanPriceFragment on PriceDetailRow{__typename key label displayValue value strikeOutDisplayValue strikeOutValue info{__typename title message}}fragment agreementFragment on CarrierAgreement{__typename name type format value docTitle label}fragment preOrderFragment on PreOrder{streetDate streetDateDisplayable streetDateType isPreOrder preOrderMessage preOrderStreetDateMessage}fragment AddressFields on Address{id addressLineOne addressLineTwo city state postalCode firstName lastName phone}fragment PickupPersonFields on PickupPerson{id firstName lastName email}fragment PriceDetailRowFields on PriceDetailRow{__typename key label displayValue value strikeOutValue strikeOutDisplayValue info{__typename title message}}fragment AccessPointFields on AccessPoint{id name assortmentStoreId displayName timeZone address{id addressLineOne addressLineTwo city state postalCode firstName lastName phone}isTest allowBagFee bagFeeValue isExpressEligible fulfillmentOption instructions nodeAccessType}fragment ReservationFields on Reservation{id expiryTime isUnscheduled expired showSlotExpiredError reservedSlot{__typename...on RegularSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata slotExpiryTime endTime available supportedTimeZone}...on DynamicExpressSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime endTime fulfillmentType slotMetadata slotExpiryTime available slaInMins maxItemAllowed supportedTimeZone}...on UnscheduledSlot{price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata unscheduledHoldInDays supportedTimeZone}...on InHomeSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata slotExpiryTime endTime available supportedTimeZone}}}fragment AffirmMessageFields on AffirmMessage{__typename description termsUrl imageUrl monthlyPayment termLength isZeroAPR}fragment FulfillmentItemGroupsFields on FulfillmentItemGroup{...on SCGroup{__typename defaultMode collapsedItemIds itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}...on DigitalDeliveryGroup{__typename defaultMode collapsedItemIds itemGroups{__typename label itemIds}}...on Unscheduled{__typename defaultMode collapsedItemIds itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}...on FCGroup{__typename defaultMode collapsedItemIds startDate endDate isUnscheduledDeliveryEligible shippingOptions{__typename itemIds availableShippingOptions{__typename id shippingMethod deliveryDate price{__typename displayValue value}label{prefix suffix}isSelected isDefault}}hasMadeShippingChanges slaGroups{__typename label deliveryDate warningLabel sellerGroups{__typename id name isProSeller type shipOptionGroup{__typename deliveryPrice{__typename displayValue value}itemIds shipMethod}}}}...on AutoCareCenter{__typename defaultMode startDate endDate accBasketType collapsedItemIds itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}}");
      var2.put("buyNowContractInput", (new JsonObject()).put("items", (new JsonArray()).add((new JsonObject()).put("offerId", "C09DD4C3232B433C8F31FE0A94A14998").put("quantity", Double.longBitsToDouble(4607182418800017408L)))));
      var2.put("promosEnable", true);
      var2.put("wplusEnabled", true);
      var1.put("variables", var2);
      return var1;
   }

   public JsonObject setAddressIdForm(String var1, String var2) {
      JsonObject var3 = new JsonObject();
      var3.put("query", "mutation setShippingAddress( $input:setShippingAddressInput! $promosEnable:Boolean! $wplusEnabled:Boolean! ){setShippingAddress(input:$input){...ContractFragment}}fragment ContractFragment on PurchaseContract{id associateDiscountStatus addressMode tenderPlanId papEbtAllowed allowedPaymentGroupTypes cartCustomerContext{membershipData{isActiveMember}}cartCustomerContext @include(if:$wplusEnabled){isMembershipOptedIn isEligibleForFreeTrial paymentData{hasCreditCard hasPaymentCardOnFile}membershipData{isPaidMember}}cartCustomerContext @skip(if:$wplusEnabled){paymentData{hasPaymentCardOnFile}}checkoutError{code errorData{__typename...on OutOfStock{offerId}__typename...on UnavailableOffer{offerId}__typename...on ItemExpired{offerId}__typename...on ItemQuantityAdjusted{offerId requestedQuantity adjustedQuantity}}operationalErrorCode message}checkoutableWarnings{code itemIds}allocationStatus payments{id paymentType cardType lastFour isDefault cvvRequired preferenceId paymentPreferenceId paymentHandle expiryMonth expiryYear firstName lastName email amountPaid cardImage cardImageAlt isLinkedCard capOneReward{credentialId redemptionUrl redemptionRate redemptionMethod rewardPointsBalance rewardPointsSelected rewardAmountSelected}remainingBalance{displayValue value}}order{id status orderVersion mobileNumber}terms{alcoholAccepted bagFeeAccepted smsOptInAccepted marketingEmailPrefOptIn}donationDetails{charityEIN charityName amount{displayValue value}acceptDonation}lineItems{...LineItemFields}tippingDetails{suggestedAmounts{value displayValue}maxAmount{value displayValue}selectedTippingAmount{value displayValue}}customer{id firstName lastName isGuest email phone}fulfillment{deliveryDetails{deliveryInstructions deliveryOption}pickupChoices{isSelected fulfillmentType accessType accessMode accessPointId}deliveryAddress{...AddressFields}alternatePickupPerson{...PickupPersonFields}primaryPickupPerson{...PickupPersonFields}fulfillmentItemGroups{...FulfillmentItemGroupsFields}}priceDetails{subTotal{...PriceDetailRowFields}totalItemQuantity fees{...PriceDetailRowFields}taxTotal{...PriceDetailRowFields}grandTotal{...PriceDetailRowFields}belowMinimumFee{...PriceDetailRowFields}authorizationAmount{...PriceDetailRowFields}weightDebitTotal{...PriceDetailRowFields}discounts{...PriceDetailRowFields}otcDeliveryBenefit{...PriceDetailRowFields}ebtSnapMaxEligible{...PriceDetailRowFields}ebtCashMaxEligible{...PriceDetailRowFields}hasAmountUnallocated affirm{__typename message{...AffirmMessageFields}}}checkoutGiftingDetails{isCheckoutGiftingOptin isWalmartProtectionPlanPresent isAppleCarePresent isRestrictedPaymentPresent giftMessageDetails{giftingMessage recipientEmail recipientName senderName}}promotions @include(if:$promosEnable){displayValue promoId terms}serverTime showPromotions @include(if:$promosEnable) errors{code message lineItems{...LineItemFields}}}fragment LineItemFields on LineItem{id quantity quantityString quantityLabel accessibilityQuantityLabel isPreOrder isWplusEarlyAccess isEventActive eventType fulfillmentSourcingDetails{currentSelection requestedSelection}packageQuantity priceInfo{priceDisplayCodes{showItemPrice priceDisplayCondition finalCostByWeight}itemPrice{displayValue value}linePrice{displayValue value}preDiscountedLinePrice{displayValue value}wasPrice{displayValue value}unitPrice{displayValue value}}isSubstitutionSelected isGiftEligible expiresAt showExpirationTimer selectedVariants{name value}product{id name usItemId itemType imageInfo{thumbnailUrl}category{categoryPath}offerId orderLimit orderMinLimit weightIncrement weightUnit averageWeight salesUnitType availabilityStatus isSubstitutionEligible isAlcohol configuration hasSellerBadge sellerId sellerName sellerType annualEvent preOrder{...preOrderFragment}badges{flags{__typename id key text}}addOnServices{serviceType groups{groupType services{selectedDisplayName offerId currentPrice{priceString}}}}}discounts{key label value @include(if:$promosEnable) terms subType displayValue @include(if:$promosEnable) displayLabel}wirelessPlan{planId mobileNumber __typename postPaidPlan{...postpaidPlanDetailsFragment}}selectedAddOnServices{offerId quantity groupType}registryInfo{registryId registryType}}fragment postpaidPlanDetailsFragment on PostPaidPlan{__typename espOrderSummaryId espOrderId espOrderLineId warpOrderId warpSessionId devicePayment{...postpaidPlanPriceFragment}devicePlan{__typename price{...postpaidPlanPriceFragment}frequency duration annualPercentageRate}deviceDataPlan{...deviceDataPlanFragment}}fragment deviceDataPlanFragment on DeviceDataPlan{__typename carrierName planType expiryTime activationFee{...postpaidPlanPriceFragment}planDetails{__typename price{...postpaidPlanPriceFragment}frequency name}agreements{...agreementFragment}}fragment postpaidPlanPriceFragment on PriceDetailRow{__typename key label displayValue value strikeOutDisplayValue strikeOutValue info{__typename title message}}fragment agreementFragment on CarrierAgreement{__typename name type format value docTitle label}fragment preOrderFragment on PreOrder{streetDate streetDateDisplayable streetDateType isPreOrder preOrderMessage preOrderStreetDateMessage}fragment AddressFields on Address{id addressLineOne addressLineTwo city state postalCode firstName lastName phone}fragment PickupPersonFields on PickupPerson{id firstName lastName email}fragment PriceDetailRowFields on PriceDetailRow{__typename key label displayValue value strikeOutValue strikeOutDisplayValue info{__typename title message}}fragment AccessPointFields on AccessPoint{id name assortmentStoreId displayName timeZone address{id addressLineOne addressLineTwo city state postalCode firstName lastName phone}isTest allowBagFee bagFeeValue isExpressEligible fulfillmentOption instructions nodeAccessType}fragment ReservationFields on Reservation{id expiryTime isUnscheduled expired showSlotExpiredError reservedSlot{__typename...on RegularSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata slotExpiryTime endTime available supportedTimeZone}...on DynamicExpressSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime endTime fulfillmentType slotMetadata slotExpiryTime available slaInMins sla{value displayValue}maxItemAllowed supportedTimeZone}...on UnscheduledSlot{price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata unscheduledHoldInDays supportedTimeZone}...on InHomeSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata slotExpiryTime endTime available supportedTimeZone}}}fragment AffirmMessageFields on AffirmMessage{__typename description termsUrl imageUrl monthlyPayment termLength isZeroAPR}fragment FulfillmentItemGroupsFields on FulfillmentItemGroup{...on SCGroup{__typename defaultMode collapsedItemIds itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}...on DigitalDeliveryGroup{__typename defaultMode collapsedItemIds itemGroups{__typename label itemIds}}...on Unscheduled{__typename defaultMode collapsedItemIds isSpecialEvent startDate endDate itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}...on FCGroup{__typename defaultMode collapsedItemIds startDate endDate isUnscheduledDeliveryEligible shippingOptions{__typename itemIds availableShippingOptions{__typename id shippingMethod deliveryDate price{__typename displayValue value}label{prefix suffix}isSelected isDefault}}hasMadeShippingChanges slaGroups{__typename label deliveryDate warningLabel sellerGroups{__typename id name isProSeller type shipOptionGroup{__typename deliveryPrice{__typename displayValue value}itemIds shipMethod}}}}...on AutoCareCenter{__typename defaultMode startDate endDate accBasketType collapsedItemIds itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}}");
      var3.put("variables", (new JsonObject()).put("input", (new JsonObject()).put("contractId", var1).put("addressId", var2)).put("promosEnable", true).put("wplusEnabled", true));
      return var3;
   }

   public JsonObject accountLoginForm(Account var1) {
      JsonObject var2 = new JsonObject();
      var2.put("username", var1.getUser());
      var2.put("password", var1.getPass());
      var2.put("rememberme", true);
      var2.put("captcha", (new JsonObject()).put("sensorData", "2a25G2m84Vrp0o9c4230971.12-1,8,-36,-890,Mozilla/9.8 (Macintosh; Intel Mac OS X 74_96_6) AppleWebKit/227.39 (KHTML, like Gecko) Chrome/91.3.4568.03 Safari/636.52,uaend,82457,82672914,en-GB,Gecko,3,7,0,0,500577,5385856,4549,952,9519,603,1302,093,8347,,cpen:6,i9:7,dm:3,cwen:4,non:1,opc:9,fc:2,sc:0,wrc:0,isc:8,vib:1,bat:3,x39:7,x82:2,7541,2.375796728065,234985759788,loc:-9,9,-64,-197,do_en,dm_en,t_en-5,0,-84,-415,6,8,2,2,427,830,6;7,2,1,0,586,879,6;2,-8,0,0,-1,-3,4;9,-0,7,3,-0,-7,2;1,1,7,4,1153,355,7;0,-4,0,7,7593,511,0;1,1,2,5,504,590,1;0,2,4,9,8067,193,6;6,4,1,0,472,516,6;2,-8,0,0,-1,-3,4;9,-0,7,3,-0,-7,2;1,1,7,4,678,492,1;0,7,3,1,7318,690,0;0,-1,6,6,-9,-0,7;4,-0,2,4,-2,-1,0;1,1,2,5,9508,630,0;6,-5,8,8,1026,817,6;2,3,9,8,255,146,2;2,9,7,4,1260,201,7;0,2,1,0,3856,699,7;4,0,6,7,3384,853,1;0,2,4,9,8313,193,6;6,4,1,0,480,846,6;2,3,9,8,4270,014,8;7,2,0,2,0289,3962,0;0,-1,6,6,4292,590,1;0,2,4,8,9923,193,6;6,-9,7,0,2476,146,2;2,1,7,4,2142,201,7;1,-4,0,7,8699,573,0;2,9,2,5,9669,586,0;-3,6,-01,-805,0,8,7,3,699,223,0;7,6,3,2,780,193,6;6,-9,7,0,-4,-0,2;5,-2,9,7,-2,-7,6;2,3,9,8,4390,191,8;7,-8,3,1,7339,638,0;0,3,9,3,115,210,0;2,9,2,5,9239,586,0;6,8,2,2,676,830,6;6,-9,7,0,-4,-0,2;5,-2,9,7,-2,-7,6;2,3,9,8,992,476,2;2,9,7,4,1016,201,7;0,-4,0,6,-5,-2,9;8,-2,9,2,-3,-8,0;0,3,9,3,5610,834,3;0,-3,4,9,8220,131,6;6,4,1,0,648,516,6;3,1,9,8,4407,047,8;7,2,0,2,0830,701,9;8,3,0,7,7995,573,0;2,9,2,5,9585,586,0;6,8,2,2,684,160,6;6,4,1,0,8102,712,4;8,9,0,1,2659,7573,7;0,-4,0,6,8803,210,0;2,9,2,4,0195,586,0;6,-5,8,7,2869,516,6;3,3,9,8,5389,047,8;8,-8,3,1,8435,690,0;1,1,9,3,5771,780,3;-0,4,-12,-005,3,1,7072,undefined,9,2,948;8,2,1134,undefined,7,3,516;8,3,2515,undefined,8,7,590;4,1,3468,undefined,6,6,701;-1,2,-93,-752,1,0,937,1145,92;2,0,953,1144,94;3,0,961,1139,96;4,0,986,1120,99;5,0,007,1190,00;6,0,011,1059,24;7,0,037,1096,521;4,1,382,0675,302;7,8,677,593,315;8,8,693,527,324;00,1,500,070,955;18,4,403,371,221;82,2,396,009,845;16,1,077,826,051;45,0,622,449,120;25,7,026,794,849;15,3,928,310,173;03,7,798,514,450;70,5,350,687,237;35,3,620,310,123;84,9,258,648,097;63,2,481,927,750;40,8,604,715,398;04,0,353,601,777;31,1,698,576,555;98,0,034,238,322,427;66,3,089,428,111,629;52,4,994,314,137,162;88,3,2648,281,697,-1;75,4,8795,472,486,-1;35,6,0607,368,402,-5;-2,1,-97,-079,-3,3,-91,-210,-7,4,-63,-130,-7,8,-75,-184,-1,8,-36,-893,-4,2,-10,-929,-8,5,-80,-533,NaN,54305,2,4,8,7,NaN,3538,7051702548101,7897814268659,39,33894,1,71,5011,6,4,0613,98345,7,gxbcdcqqvof2nukbsjdz_9007,5499,484,186058337,26425874-0,9,-04,-360,-2,9-8,5,-80,-12,-8474643896;6,31,84,86,28,82,60,36,31,66,22,91,19,30,46,43,0;,9;true;true;true;237;true;39;56;true;false;-7-7,4,-63,-83,6897-5,0,-84,-426,94852664-1,2,-93,-750,201583-2,1,-58,-290,;5;2;2"));
      return var2;
   }

   public CompletableFuture generatePX(boolean var1) {
      try {
         if (var1 != 0) {
            this.pxAPI.reset();
         }

         CompletableFuture var10001 = this.pxAPI.solve();
         if (!var10001.isDone()) {
            CompletableFuture var4 = var10001;
            return var4.exceptionally(Function.identity()).thenCompose(WalmartNewAPI::async$generatePX);
         } else {
            return CompletableFuture.completedFuture(this.parseMapIntoCookieJar((MultiMap)var10001.join()));
         }
      } catch (Exception var5) {
         System.out.println("Error generating desktop session: " + var5.getMessage());
         return CompletableFuture.completedFuture(false);
      }
   }

   public WalmartNewAPI(Task var1) {
      super(ClientType.CHROME);
      this.task = var1;
      this.crossSite = crossSiteList[ThreadLocalRandom.current().nextInt(crossSiteList.length)];
      this.searchQuery = searchQueries[ThreadLocalRandom.current().nextInt(searchQueries.length)];
      this.referer = "https://www.walmart.com/";
   }

   public HttpRequest getTenders() {
      String var1 = zs2y(32);
      HttpRequest var2 = this.client.postAbs("https://www.walmart.com/orchestra/cartxo/graphql").as(BodyCodec.buffer());
      var2.putHeader("content-length", "DEFAULT_VALUE");
      var2.putHeader("sec-ch-ua", this.pxAPI.getDeviceSecUA());
      var2.putHeader("x-o-correlation-id", var1);
      var2.putHeader("device_profile_ref_id", this.deviceProfileId);
      var2.putHeader("x-latency-trace", "1");
      var2.putHeader("wm_mp", "true");
      var2.putHeader("x-o-platform-version", this.platformVersion);
      var2.putHeader("x-o-segment", "oaoh");
      var2.putHeader("x-o-gql-query", "query getTenderPlan");
      var2.putHeader("x-apollo-operation-name", "getTenderPlan");
      var2.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var2.putHeader("sec-ch-ua-mobile", "?0");
      var2.putHeader("user-agent", this.pxAPI.getDeviceUA());
      var2.putHeader("x-o-platform", X_O_PLATFORM_NAME.toString());
      var2.putHeader("content-type", "application/json");
      var2.putHeader("accept", "application/json");
      var2.putHeader("x-enable-server-timing", "1");
      var2.putHeader("x-o-ccm", "server");
      var2.putHeader("x-o-tp-phase", this.tpPhase);
      var2.putHeader("wm_qos.correlation_id", var1);
      var2.putHeader("origin", "https://www.walmart.com");
      var2.putHeader("sec-fetch-site", "same-origin");
      var2.putHeader("sec-fetch-mode", "cors");
      var2.putHeader("sec-fetch-dest", "empty");
      var2.putHeader("referer", "https://www.walmart.com/checkout/review-order?cartId=" + this.cartId + "&wv=add_credit_debit_card");
      var2.putHeader("accept-encoding", "gzip, deflate, br");
      var2.putHeader("accept-language", this.pxAPI.getDeviceLang());
      var2.putHeader("cookie", "DEFAULT_VALUE");
      return var2;
   }

   public JsonObject getPaymentForm(PaymentToken var1) {
      JsonObject var2 = new JsonObject();
      var2.put("query", "mutation PlaceOrder( $placeOrderInput:PlaceOrderInput! $promosEnable:Boolean! $wplusEnabled:Boolean! ){placeOrder(input:$placeOrderInput){...ContractFragment}}fragment ContractFragment on PurchaseContract{id associateDiscountStatus addressMode tenderPlanId papEbtAllowed allowedPaymentGroupTypes cartCustomerContext @include(if:$wplusEnabled){isMembershipOptedIn isEligibleForFreeTrial paymentData{hasCreditCard}membershipData{isPaidMember}}checkoutError{code errorData{__typename...on OutOfStock{offerId}__typename...on UnavailableOffer{offerId}__typename...on ItemExpired{offerId}__typename...on ItemQuantityAdjusted{offerId requestedQuantity adjustedQuantity}}operationalErrorCode message}checkoutableWarnings{code itemIds}allocationStatus payments{id paymentType cardType lastFour isDefault cvvRequired preferenceId paymentPreferenceId paymentHandle expiryMonth expiryYear firstName lastName email amountPaid cardImage cardImageAlt isLinkedCard capOneReward{credentialId redemptionUrl redemptionRate redemptionMethod rewardPointsBalance rewardPointsSelected rewardAmountSelected}remainingBalance{displayValue value}}order{id status orderVersion mobileNumber}terms{alcoholAccepted bagFeeAccepted smsOptInAccepted marketingEmailPrefOptIn}donationDetails{charityEIN charityName amount{displayValue value}acceptDonation}lineItems{...LineItemFields}tippingDetails{suggestedAmounts{value displayValue}maxAmount{value displayValue}selectedTippingAmount{value displayValue}}customer{id firstName lastName isGuest email phone}fulfillment{deliveryDetails{deliveryInstructions deliveryOption}pickupChoices{isSelected fulfillmentType accessType accessMode accessPointId}deliveryAddress{...AddressFields}alternatePickupPerson{...PickupPersonFields}primaryPickupPerson{...PickupPersonFields}fulfillmentItemGroups{...FulfillmentItemGroupsFields}}priceDetails{subTotal{...PriceDetailRowFields}totalItemQuantity fees{...PriceDetailRowFields}taxTotal{...PriceDetailRowFields}grandTotal{...PriceDetailRowFields}belowMinimumFee{...PriceDetailRowFields}authorizationAmount{...PriceDetailRowFields}weightDebitTotal{...PriceDetailRowFields}discounts{...PriceDetailRowFields}otcDeliveryBenefit{...PriceDetailRowFields}ebtSnapMaxEligible{...PriceDetailRowFields}ebtCashMaxEligible{...PriceDetailRowFields}hasAmountUnallocated affirm{__typename message{...AffirmMessageFields}}}checkoutGiftingDetails{isCheckoutGiftingOptin isWalmartProtectionPlanPresent isAppleCarePresent isRestrictedPaymentPresent giftMessageDetails{giftingMessage recipientEmail recipientName senderName}}promotions @include(if:$promosEnable){displayValue promoId terms}serverTime showPromotions @include(if:$promosEnable) errors{code message lineItems{...LineItemFields}}}fragment LineItemFields on LineItem{id quantity quantityString quantityLabel accessibilityQuantityLabel isPreOrder isWplusEarlyAccess isEventActive eventType fulfillmentSourcingDetails{currentSelection requestedSelection}packageQuantity priceInfo{priceDisplayCodes{showItemPrice priceDisplayCondition finalCostByWeight}itemPrice{displayValue value}linePrice{displayValue value}preDiscountedLinePrice{displayValue value}wasPrice{displayValue value}unitPrice{displayValue value}}isSubstitutionSelected isGiftEligible expiresAt showExpirationTimer selectedVariants{name value}product{id name usItemId itemType imageInfo{thumbnailUrl}category{categoryPath}offerId orderLimit orderMinLimit weightIncrement weightUnit averageWeight salesUnitType availabilityStatus isSubstitutionEligible isAlcohol configuration hasSellerBadge sellerId sellerName sellerType annualEvent preOrder{...preOrderFragment}badges{flags{id key text}}addOnServices{serviceType groups{groupType services{selectedDisplayName offerId currentPrice{priceString}}}}}discounts{key label value @include(if:$promosEnable) terms subType displayValue @include(if:$promosEnable) displayLabel}wirelessPlan{planId mobileNumber __typename postPaidPlan{...postpaidPlanDetailsFragment}}selectedAddOnServices{offerId quantity groupType}registryInfo{registryId registryType}}fragment postpaidPlanDetailsFragment on PostPaidPlan{__typename espOrderSummaryId espOrderId espOrderLineId warpOrderId warpSessionId devicePayment{...postpaidPlanPriceFragment}devicePlan{__typename price{...postpaidPlanPriceFragment}frequency duration annualPercentageRate}deviceDataPlan{...deviceDataPlanFragment}}fragment deviceDataPlanFragment on DeviceDataPlan{__typename carrierName planType expiryTime activationFee{...postpaidPlanPriceFragment}planDetails{__typename price{...postpaidPlanPriceFragment}frequency name}agreements{...agreementFragment}}fragment postpaidPlanPriceFragment on PriceDetailRow{__typename key label displayValue value strikeOutDisplayValue strikeOutValue info{__typename title message}}fragment agreementFragment on CarrierAgreement{__typename name type format value docTitle label}fragment preOrderFragment on PreOrder{streetDate streetDateDisplayable streetDateType isPreOrder preOrderMessage preOrderStreetDateMessage}fragment AddressFields on Address{id addressLineOne addressLineTwo city state postalCode firstName lastName phone}fragment PickupPersonFields on PickupPerson{id firstName lastName email}fragment PriceDetailRowFields on PriceDetailRow{__typename key label displayValue value strikeOutValue strikeOutDisplayValue info{__typename title message}}fragment AccessPointFields on AccessPoint{id name assortmentStoreId displayName timeZone address{id addressLineOne addressLineTwo city state postalCode firstName lastName phone}isTest allowBagFee bagFeeValue isExpressEligible fulfillmentOption instructions nodeAccessType}fragment ReservationFields on Reservation{id expiryTime isUnscheduled expired showSlotExpiredError reservedSlot{__typename...on RegularSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata slotExpiryTime endTime available supportedTimeZone}...on DynamicExpressSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime endTime fulfillmentType slotMetadata slotExpiryTime available slaInMins maxItemAllowed supportedTimeZone}...on UnscheduledSlot{price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata unscheduledHoldInDays supportedTimeZone}...on InHomeSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata slotExpiryTime endTime available supportedTimeZone}}}fragment AffirmMessageFields on AffirmMessage{__typename description termsUrl imageUrl monthlyPayment termLength isZeroAPR}fragment FulfillmentItemGroupsFields on FulfillmentItemGroup{...on SCGroup{__typename defaultMode collapsedItemIds itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}...on DigitalDeliveryGroup{__typename defaultMode collapsedItemIds itemGroups{__typename label itemIds}}...on Unscheduled{__typename defaultMode collapsedItemIds isSpecialEvent itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}...on FCGroup{__typename defaultMode collapsedItemIds startDate endDate isUnscheduledDeliveryEligible shippingOptions{__typename itemIds availableShippingOptions{__typename id shippingMethod deliveryDate price{__typename displayValue value}label{prefix suffix}isSelected isDefault}}hasMadeShippingChanges slaGroups{__typename label deliveryDate warningLabel sellerGroups{__typename id name isProSeller type shipOptionGroup{__typename deliveryPrice{__typename displayValue value}itemIds shipMethod}}}}...on AutoCareCenter{__typename defaultMode startDate endDate accBasketType collapsedItemIds itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}}");
      var2.put("variables", (new JsonObject()).put("placeOrderInput", (new JsonObject()).put("contractId", this.contractId).put("substitutions", new JsonArray()).putNull("acceptBagFee").putNull("acceptAlcoholDisclosure").put("acceptSMSOptInDisclosure", false).put("marketingEmailPref", false).put("deliveryDetails", (new JsonObject()).putNull("deliveryInstructions").put("deliveryOption", "LEAVE_AT_DOOR")).put("mobileNumber", this.task.getProfile().getPhone()).putNull("paymentCvvInfos").putNull("paymentHandle").put("acceptDonation", false).put("emailAddress", this.task.getProfile().getEmail()).putNull("fulfillmentOptions").put("acceptedAgreements", new JsonArray())).put("promosEnable", true).put("wplusEnabled", true));
      return var2;
   }

   public HttpRequest buyNowSubmitPayment() {
      String var1 = zs2y(32);
      HttpRequest var2 = this.client.postAbs("https://www.walmart.com/orchestra/home/graphql").as(BodyCodec.buffer());
      var2.headers().setAll(Headers$Pseudo.MASP.get()).set(HttpHeaders.CONTENT_LENGTH, Headers.DEFAULT).set(X_O_SEGMENT, OAOH).set(X_O_CORRELATION_ID, var1).set(DEVICE_PROFILE_REF, this.deviceProfileId).set(X_LATENCY_TRACE, "1").set(WM_MP, TRUE).set(X_O_PLATFORM_VERSION, this.platformVersion).set(X_O_GQL_QUERY, "mutation PlaceOrder").set(WM_PAGE_URL, this.productReferer).set(X_APOLLO_OPERATION_NAME, "PlaceOrder").set(HttpHeaders.USER_AGENT, this.pxAPI.getDeviceUA()).set(X_O_PLATFORM, X_O_PLATFORM_NAME).set(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON).set(HttpHeaders.ACCEPT, HttpHeaderValues.APPLICATION_JSON).set(X_ENABLE_SERVER_TIMING, "1").set(X_O_CCM, SERVER).set(WM_CORRELATION_ID, var1).set(HttpHeaders.ORIGIN, "https://www.walmart.com").set(Headers.SEC_FETCH_SITE, Headers.SAME_ORIGIN).set(Headers.SEC_FETCH_MODE, Headers.CORS).set(Headers.SEC_FETCH_DEST, Headers.EMPTY).set(HttpHeaders.REFERER, this.productReferer).set(HttpHeaders.ACCEPT_ENCODING, Headers.GZIP_DEFLATE_BR).set(HttpHeaders.ACCEPT_LANGUAGE, this.pxAPI.getDeviceLang()).set(HttpHeaders.COOKIE, Headers.DEFAULT);
      return var2;
   }

   public JsonObject buyNowBody() {
      JsonObject var1 = new JsonObject();
      JsonObject var2 = new JsonObject();
      var1.put("query", "mutation CreateBuyNowContract( $buyNowContractInput:BuyNowContractInput! $promosEnable:Boolean! $wplusEnabled:Boolean! ){createBuyNowContract(input:$buyNowContractInput){...ContractFragment}}fragment ContractFragment on PurchaseContract{id associateDiscountStatus addressMode tenderPlanId papEbtAllowed allowedPaymentGroupTypes cartCustomerContext @include(if:$wplusEnabled){isMembershipOptedIn isEligibleForFreeTrial paymentData{hasCreditCard}}checkoutError{code errorData{__typename...on OutOfStock{offerId}__typename...on UnavailableOffer{offerId}__typename...on ItemExpired{offerId}__typename...on ItemQuantityAdjusted{offerId requestedQuantity adjustedQuantity}}operationalErrorCode message}checkoutableWarnings{code itemIds}allocationStatus payments{id paymentType cardType lastFour isDefault cvvRequired preferenceId paymentPreferenceId paymentHandle expiryMonth expiryYear firstName lastName email amountPaid cardImage cardImageAlt isLinkedCard capOneReward{credentialId redemptionUrl redemptionRate redemptionMethod rewardPointsBalance rewardPointsSelected rewardAmountSelected}remainingBalance{displayValue value}}order{id status orderVersion mobileNumber}terms{alcoholAccepted bagFeeAccepted smsOptInAccepted marketingEmailPrefOptIn}donationDetails{charityEIN charityName amount{displayValue value}acceptDonation}lineItems{...LineItemFields}tippingDetails{suggestedAmounts{value displayValue}maxAmount{value displayValue}selectedTippingAmount{value displayValue}}customer{id firstName lastName isGuest email phone}fulfillment{deliveryDetails{deliveryInstructions deliveryOption}pickupChoices{isSelected fulfillmentType accessType accessMode accessPointId}deliveryAddress{...AddressFields}alternatePickupPerson{...PickupPersonFields}primaryPickupPerson{...PickupPersonFields}fulfillmentItemGroups{...FulfillmentItemGroupsFields}accessPoint{...AccessPointFields}reservation{...ReservationFields}}priceDetails{subTotal{...PriceDetailRowFields}totalItemQuantity fees{...PriceDetailRowFields}taxTotal{...PriceDetailRowFields}grandTotal{...PriceDetailRowFields}belowMinimumFee{...PriceDetailRowFields}authorizationAmount{...PriceDetailRowFields}weightDebitTotal{...PriceDetailRowFields}discounts{...PriceDetailRowFields}otcDeliveryBenefit{...PriceDetailRowFields}ebtSnapMaxEligible{...PriceDetailRowFields}ebtCashMaxEligible{...PriceDetailRowFields}hasAmountUnallocated affirm{__typename message{...AffirmMessageFields}}}checkoutGiftingDetails{isCheckoutGiftingOptin isWalmartProtectionPlanPresent isAppleCarePresent isRestrictedPaymentPresent giftMessageDetails{giftingMessage recipientEmail recipientName senderName}}promotions @include(if:$promosEnable){displayValue promoId terms}showPromotions @include(if:$promosEnable) errors{code message lineItems{...LineItemFields}}}fragment LineItemFields on LineItem{id quantity quantityString quantityLabel accessibilityQuantityLabel isPreOrder fulfillmentSourcingDetails{currentSelection requestedSelection}packageQuantity priceInfo{priceDisplayCodes{showItemPrice priceDisplayCondition finalCostByWeight}itemPrice{displayValue value}linePrice{displayValue value}preDiscountedLinePrice{displayValue value}wasPrice{displayValue value}unitPrice{displayValue value}}isSubstitutionSelected isGiftEligible selectedVariants{name value}product{id name usItemId itemType imageInfo{thumbnailUrl}offerId orderLimit orderMinLimit weightIncrement weightUnit averageWeight salesUnitType availabilityStatus isSubstitutionEligible isAlcohol configuration hasSellerBadge sellerId sellerName sellerType preOrder{...preOrderFragment}addOnServices{serviceType groups{groupType services{selectedDisplayName offerId currentPrice{priceString}}}}}discounts{key label displayValue @include(if:$promosEnable) displayLabel @include(if:$promosEnable)}wirelessPlan{planId mobileNumber __typename postPaidPlan{...postpaidPlanDetailsFragment}}selectedAddOnServices{offerId quantity groupType}registryInfo{registryId registryType}}fragment postpaidPlanDetailsFragment on PostPaidPlan{__typename espOrderSummaryId espOrderId espOrderLineId warpOrderId warpSessionId devicePayment{...postpaidPlanPriceFragment}devicePlan{__typename price{...postpaidPlanPriceFragment}frequency duration annualPercentageRate}deviceDataPlan{...deviceDataPlanFragment}}fragment deviceDataPlanFragment on DeviceDataPlan{__typename carrierName planType expiryTime activationFee{...postpaidPlanPriceFragment}planDetails{__typename price{...postpaidPlanPriceFragment}frequency name}agreements{...agreementFragment}}fragment postpaidPlanPriceFragment on PriceDetailRow{__typename key label displayValue value strikeOutDisplayValue strikeOutValue info{__typename title message}}fragment agreementFragment on CarrierAgreement{__typename name type format value docTitle label}fragment preOrderFragment on PreOrder{streetDate streetDateDisplayable streetDateType isPreOrder preOrderMessage preOrderStreetDateMessage}fragment AddressFields on Address{id addressLineOne addressLineTwo city state postalCode firstName lastName phone}fragment PickupPersonFields on PickupPerson{id firstName lastName email}fragment PriceDetailRowFields on PriceDetailRow{__typename key label displayValue value strikeOutValue strikeOutDisplayValue info{__typename title message}}fragment AccessPointFields on AccessPoint{id name assortmentStoreId displayName timeZone address{id addressLineOne addressLineTwo city state postalCode firstName lastName phone}isTest allowBagFee bagFeeValue isExpressEligible fulfillmentOption instructions nodeAccessType}fragment ReservationFields on Reservation{id expiryTime isUnscheduled expired showSlotExpiredError reservedSlot{__typename...on RegularSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata slotExpiryTime endTime available supportedTimeZone}...on DynamicExpressSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime endTime fulfillmentType slotMetadata slotExpiryTime available slaInMins maxItemAllowed supportedTimeZone}...on UnscheduledSlot{price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata unscheduledHoldInDays supportedTimeZone}...on InHomeSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata slotExpiryTime endTime available supportedTimeZone}}}fragment AffirmMessageFields on AffirmMessage{__typename description termsUrl imageUrl monthlyPayment termLength isZeroAPR}fragment FulfillmentItemGroupsFields on FulfillmentItemGroup{...on SCGroup{__typename defaultMode collapsedItemIds itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}...on DigitalDeliveryGroup{__typename defaultMode collapsedItemIds itemGroups{__typename label itemIds}}...on Unscheduled{__typename defaultMode collapsedItemIds itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}...on FCGroup{__typename defaultMode collapsedItemIds startDate endDate isUnscheduledDeliveryEligible shippingOptions{__typename itemIds availableShippingOptions{__typename id shippingMethod deliveryDate price{__typename displayValue value}label{prefix suffix}isSelected isDefault}}hasMadeShippingChanges slaGroups{__typename label deliveryDate warningLabel sellerGroups{__typename id name isProSeller type shipOptionGroup{__typename deliveryPrice{__typename displayValue value}itemIds shipMethod}}}}...on AutoCareCenter{__typename defaultMode startDate endDate accBasketType collapsedItemIds itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}}");
      var2.put("buyNowContractInput", (new JsonObject()).put("items", (new JsonArray()).add((new JsonObject()).put("offerId", this.task.getKeywords()[0]).put("quantity", Double.parseDouble(this.task.getSize())))));
      var2.put("promosEnable", true);
      var2.put("wplusEnabled", true);
      var1.put("variables", var2);
      return var1;
   }

   public HttpRequest loginAccount() {
      HttpRequest var1 = this.client.postAbs("https://www.walmart.com/account/electrode/api/signin?vid=oaoh&tid=0&returnUrl=%2F").as(BodyCodec.buffer());
      var1.headers().setAll(Headers$Pseudo.MASP.get()).set(HttpHeaders.CONTENT_LENGTH, Headers.DEFAULT).set(Headers.SEC_CH_UA, this.getPxAPI().getDeviceSecUA()).set(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON).set(Headers.SEC_CH_UA_MOBILE, Headers._Q0).set(HttpHeaders.USER_AGENT, this.pxAPI.getDeviceUA()).set(DEVICE_PROFILE_REF, this.deviceProfileId).set(Headers.SEC_CH_UA_PLATFORM, Headers.WINDOWS).set(HttpHeaders.ACCEPT, "*/*").set(HttpHeaders.ORIGIN, "https://www.walmart.com").set(Headers.SEC_FETCH_SITE, Headers.SAME_ORIGIN).set(Headers.SEC_FETCH_MODE, Headers.CORS).set(Headers.SEC_FETCH_DEST, Headers.EMPTY).set(HttpHeaders.REFERER, "https://www.walmart.com/account/login?vid=oaoh&tid=0&returnUrl=%2F").set(HttpHeaders.ACCEPT_ENCODING, Headers.GZIP_DEFLATE_BR).set(HttpHeaders.ACCEPT_LANGUAGE, this.pxAPI.getDeviceLang()).set(HttpHeaders.COOKIE, Headers.DEFAULT);
      return var1;
   }

   public HttpRequest savePayment() {
      String var1 = zs2y(32);
      HttpRequest var2 = this.client.postAbs("https://www.walmart.com/orchestra/home/graphql").as(BodyCodec.buffer());
      var2.headers().setAll(Headers$Pseudo.MASP.get()).set(HttpHeaders.CONTENT_LENGTH, Headers.DEFAULT).set(X_O_SEGMENT, OAOH).set(X_O_CORRELATION_ID, var1).set(DEVICE_PROFILE_REF, this.deviceProfileId).set(X_LATENCY_TRACE, "1").set(WM_MP, TRUE).set(X_O_PLATFORM_VERSION, this.platformVersion).set(X_O_GQL_QUERY, "mutation AddCreditCard").set(WM_PAGE_URL, "https://www.walmart.com/account/delivery-addresses").set(X_APOLLO_OPERATION_NAME, "AddCreditCard").set(HttpHeaders.USER_AGENT, this.pxAPI.getDeviceUA()).set(X_O_PLATFORM, X_O_PLATFORM_NAME).set(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON).set(HttpHeaders.ACCEPT, HttpHeaderValues.APPLICATION_JSON).set(X_ENABLE_SERVER_TIMING, "1").set(X_O_CCM, SERVER).set(WM_CORRELATION_ID, var1).set(HttpHeaders.ORIGIN, "https://www.walmart.com").set(Headers.SEC_FETCH_SITE, Headers.SAME_ORIGIN).set(Headers.SEC_FETCH_MODE, Headers.CORS).set(Headers.SEC_FETCH_DEST, Headers.EMPTY).set(HttpHeaders.REFERER, "https://www.walmart.com/account/delivery-addresses").set(HttpHeaders.ACCEPT_ENCODING, Headers.GZIP_DEFLATE_BR).set(HttpHeaders.ACCEPT_LANGUAGE, this.pxAPI.getDeviceLang()).set(HttpHeaders.COOKIE, Headers.DEFAULT);
      return var2;
   }

   public JsonObject buyNowSubmitPaymentBody(String var1) {
      JsonObject var2 = new JsonObject();
      var2.put("query", "mutation PlaceOrder( $placeOrderInput:PlaceOrderInput! $promosEnable:Boolean! $wplusEnabled:Boolean! ){placeOrder(input:$placeOrderInput){...ContractFragment}}fragment ContractFragment on PurchaseContract{id associateDiscountStatus addressMode tenderPlanId papEbtAllowed allowedPaymentGroupTypes cartCustomerContext @include(if:$wplusEnabled){isMembershipOptedIn isEligibleForFreeTrial paymentData{hasCreditCard}membershipData{isPaidMember}}checkoutError{code errorData{__typename...on OutOfStock{offerId}__typename...on UnavailableOffer{offerId}__typename...on ItemExpired{offerId}__typename...on ItemQuantityAdjusted{offerId requestedQuantity adjustedQuantity}}operationalErrorCode message}checkoutableWarnings{code itemIds}allocationStatus payments{id paymentType cardType lastFour isDefault cvvRequired preferenceId paymentPreferenceId paymentHandle expiryMonth expiryYear firstName lastName email amountPaid cardImage cardImageAlt isLinkedCard capOneReward{credentialId redemptionUrl redemptionRate redemptionMethod rewardPointsBalance rewardPointsSelected rewardAmountSelected}remainingBalance{displayValue value}}order{id status orderVersion mobileNumber}terms{alcoholAccepted bagFeeAccepted smsOptInAccepted marketingEmailPrefOptIn}donationDetails{charityEIN charityName amount{displayValue value}acceptDonation}lineItems{...LineItemFields}tippingDetails{suggestedAmounts{value displayValue}maxAmount{value displayValue}selectedTippingAmount{value displayValue}}customer{id firstName lastName isGuest email phone}fulfillment{deliveryDetails{deliveryInstructions deliveryOption}pickupChoices{isSelected fulfillmentType accessType accessMode accessPointId}deliveryAddress{...AddressFields}alternatePickupPerson{...PickupPersonFields}primaryPickupPerson{...PickupPersonFields}fulfillmentItemGroups{...FulfillmentItemGroupsFields}}priceDetails{subTotal{...PriceDetailRowFields}totalItemQuantity fees{...PriceDetailRowFields}taxTotal{...PriceDetailRowFields}grandTotal{...PriceDetailRowFields}belowMinimumFee{...PriceDetailRowFields}authorizationAmount{...PriceDetailRowFields}weightDebitTotal{...PriceDetailRowFields}discounts{...PriceDetailRowFields}otcDeliveryBenefit{...PriceDetailRowFields}ebtSnapMaxEligible{...PriceDetailRowFields}ebtCashMaxEligible{...PriceDetailRowFields}hasAmountUnallocated affirm{__typename message{...AffirmMessageFields}}}checkoutGiftingDetails{isCheckoutGiftingOptin isWalmartProtectionPlanPresent isAppleCarePresent isRestrictedPaymentPresent giftMessageDetails{giftingMessage recipientEmail recipientName senderName}}promotions @include(if:$promosEnable){displayValue promoId terms}serverTime showPromotions @include(if:$promosEnable) errors{code message lineItems{...LineItemFields}}}fragment LineItemFields on LineItem{id quantity quantityString quantityLabel accessibilityQuantityLabel isPreOrder isWplusEarlyAccess isEventActive eventType fulfillmentSourcingDetails{currentSelection requestedSelection}packageQuantity priceInfo{priceDisplayCodes{showItemPrice priceDisplayCondition finalCostByWeight}itemPrice{displayValue value}linePrice{displayValue value}preDiscountedLinePrice{displayValue value}wasPrice{displayValue value}unitPrice{displayValue value}}isSubstitutionSelected isGiftEligible expiresAt showExpirationTimer selectedVariants{name value}product{id name usItemId itemType imageInfo{thumbnailUrl}category{categoryPath}offerId orderLimit orderMinLimit weightIncrement weightUnit averageWeight salesUnitType availabilityStatus isSubstitutionEligible isAlcohol configuration hasSellerBadge sellerId sellerName sellerType annualEvent preOrder{...preOrderFragment}badges{flags{id key text}}addOnServices{serviceType groups{groupType services{selectedDisplayName offerId currentPrice{priceString}}}}}discounts{key label value @include(if:$promosEnable) terms subType displayValue @include(if:$promosEnable) displayLabel}wirelessPlan{planId mobileNumber __typename postPaidPlan{...postpaidPlanDetailsFragment}}selectedAddOnServices{offerId quantity groupType}registryInfo{registryId registryType}}fragment postpaidPlanDetailsFragment on PostPaidPlan{__typename espOrderSummaryId espOrderId espOrderLineId warpOrderId warpSessionId devicePayment{...postpaidPlanPriceFragment}devicePlan{__typename price{...postpaidPlanPriceFragment}frequency duration annualPercentageRate}deviceDataPlan{...deviceDataPlanFragment}}fragment deviceDataPlanFragment on DeviceDataPlan{__typename carrierName planType expiryTime activationFee{...postpaidPlanPriceFragment}planDetails{__typename price{...postpaidPlanPriceFragment}frequency name}agreements{...agreementFragment}}fragment postpaidPlanPriceFragment on PriceDetailRow{__typename key label displayValue value strikeOutDisplayValue strikeOutValue info{__typename title message}}fragment agreementFragment on CarrierAgreement{__typename name type format value docTitle label}fragment preOrderFragment on PreOrder{streetDate streetDateDisplayable streetDateType isPreOrder preOrderMessage preOrderStreetDateMessage}fragment AddressFields on Address{id addressLineOne addressLineTwo city state postalCode firstName lastName phone}fragment PickupPersonFields on PickupPerson{id firstName lastName email}fragment PriceDetailRowFields on PriceDetailRow{__typename key label displayValue value strikeOutValue strikeOutDisplayValue info{__typename title message}}fragment AccessPointFields on AccessPoint{id name assortmentStoreId displayName timeZone address{id addressLineOne addressLineTwo city state postalCode firstName lastName phone}isTest allowBagFee bagFeeValue isExpressEligible fulfillmentOption instructions nodeAccessType}fragment ReservationFields on Reservation{id expiryTime isUnscheduled expired showSlotExpiredError reservedSlot{__typename...on RegularSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata slotExpiryTime endTime available supportedTimeZone}...on DynamicExpressSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime endTime fulfillmentType slotMetadata slotExpiryTime available slaInMins maxItemAllowed supportedTimeZone}...on UnscheduledSlot{price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata unscheduledHoldInDays supportedTimeZone}...on InHomeSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata slotExpiryTime endTime available supportedTimeZone}}}fragment AffirmMessageFields on AffirmMessage{__typename description termsUrl imageUrl monthlyPayment termLength isZeroAPR}fragment FulfillmentItemGroupsFields on FulfillmentItemGroup{...on SCGroup{__typename defaultMode collapsedItemIds itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}...on DigitalDeliveryGroup{__typename defaultMode collapsedItemIds itemGroups{__typename label itemIds}}...on Unscheduled{__typename defaultMode collapsedItemIds isSpecialEvent itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}...on FCGroup{__typename defaultMode collapsedItemIds startDate endDate isUnscheduledDeliveryEligible shippingOptions{__typename itemIds availableShippingOptions{__typename id shippingMethod deliveryDate price{__typename displayValue value}label{prefix suffix}isSelected isDefault}}hasMadeShippingChanges slaGroups{__typename label deliveryDate warningLabel sellerGroups{__typename id name isProSeller type shipOptionGroup{__typename deliveryPrice{__typename displayValue value}itemIds shipMethod}}}}...on AutoCareCenter{__typename defaultMode startDate endDate accBasketType collapsedItemIds itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}}");
      var2.put("variables", (new JsonObject()).put("placeOrderInput", (new JsonObject()).put("contractId", var1).put("substitutions", new JsonArray()).putNull("acceptBagFee").putNull("acceptAlcoholDisclosure").putNull("acceptSMSOptInDisclosure").putNull("marketingEmailPref").putNull("deliveryDetails").put("mobileNumber", this.task.getProfile().getPhone()).putNull("paymentCvvInfos").putNull("paymentHandle").put("emailAddress", this.task.getProfile().getEmail()).putNull("fulfillmentOptions").put("acceptedAgreements", new JsonArray())).put("promosEnable", true).put("wplusEnabled", true));
      return var2;
   }

   public Buffer getContractForm(String var1) {
      JsonObject var2 = new JsonObject();
      var2.put("query", "query getPurchaseContract( $input:PurchaseContractInput! $promosEnable:Boolean! $wplusEnabled:Boolean! ){purchaseContract(input:$input){...ContractFragment}}fragment ContractFragment on PurchaseContract{id associateDiscountStatus addressMode tenderPlanId papEbtAllowed allowedPaymentGroupTypes cartCustomerContext{membershipData{isActiveMember status isActiveMember}}cartCustomerContext @include(if:$wplusEnabled){isMembershipOptedIn isEligibleForFreeTrial paymentData{hasCreditCard hasPaymentCardOnFile}membershipData{isPaidMember}}cartCustomerContext @skip(if:$wplusEnabled){paymentData{hasPaymentCardOnFile}membershipData{isPaidMember}}checkoutError{code errorData{__typename...on OutOfStock{offerId}__typename...on UnavailableOffer{offerId}__typename...on ItemExpired{offerId}__typename...on ItemQuantityAdjusted{offerId requestedQuantity adjustedQuantity}}operationalErrorCode message}checkoutableWarnings{code itemIds}allocationStatus payments{id paymentType cardType lastFour isDefault cvvRequired preferenceId paymentPreferenceId paymentHandle expiryMonth expiryYear firstName lastName email amountPaid cardImage cardImageAlt isLinkedCard capOneReward{credentialId redemptionUrl redemptionRate redemptionMethod rewardPointsBalance rewardPointsSelected rewardAmountSelected}remainingBalance{displayValue value}}order{id status orderVersion mobileNumber}terms{alcoholAccepted bagFeeAccepted smsOptInAccepted marketingEmailPrefOptIn}donationDetails{charityEIN charityName amount{displayValue value}acceptDonation}lineItems{...LineItemFields}tippingDetails{suggestedAmounts{value displayValue}maxAmount{value displayValue}selectedTippingAmount{value displayValue}}customer{id firstName lastName isGuest email phone}fulfillment{deliveryDetails{deliveryInstructions deliveryOption}pickupChoices{isSelected fulfillmentType accessType accessMode accessPointId}deliveryAddress{...AddressFields}alternatePickupPerson{...PickupPersonFields}primaryPickupPerson{...PickupPersonFields}fulfillmentItemGroups{...FulfillmentItemGroupsFields}}priceDetails{subTotal{...PriceDetailRowFields}totalItemQuantity fees{...PriceDetailRowFields}taxTotal{...PriceDetailRowFields}grandTotal{...PriceDetailRowFields}belowMinimumFee{...PriceDetailRowFields}authorizationAmount{...PriceDetailRowFields}weightDebitTotal{...PriceDetailRowFields}discounts{...PriceDetailRowFields}otcDeliveryBenefit{...PriceDetailRowFields}ebtSnapMaxEligible{...PriceDetailRowFields}ebtCashMaxEligible{...PriceDetailRowFields}hasAmountUnallocated affirm{__typename message{...AffirmMessageFields}}}checkoutGiftingDetails{isCheckoutGiftingOptin isWalmartProtectionPlanPresent isAppleCarePresent isRestrictedPaymentPresent giftMessageDetails{giftingMessage recipientEmail recipientName senderName}}promotions @include(if:$promosEnable){displayValue promoId terms}serverTime showPromotions @include(if:$promosEnable) errors{code message lineItems{...LineItemFields}}}fragment LineItemFields on LineItem{id quantity quantityString quantityLabel accessibilityQuantityLabel isPreOrder isWplusEarlyAccess isEventActive eventType fulfillmentSourcingDetails{currentSelection requestedSelection}packageQuantity priceInfo{priceDisplayCodes{showItemPrice priceDisplayCondition finalCostByWeight}itemPrice{displayValue value}linePrice{displayValue value}preDiscountedLinePrice{displayValue value}wasPrice{displayValue value}unitPrice{displayValue value}}isSubstitutionSelected isGiftEligible expiresAt showExpirationTimer selectedVariants{name value}product{...ProductFields}discounts{key label value @include(if:$promosEnable) terms subType displayValue @include(if:$promosEnable) displayLabel}wirelessPlan{planId mobileNumber __typename postPaidPlan{...postpaidPlanDetailsFragment}}selectedAddOnServices{offerId quantity groupType}registryInfo{registryId registryType}}fragment postpaidPlanDetailsFragment on PostPaidPlan{__typename espOrderSummaryId espOrderId espOrderLineId warpOrderId warpSessionId devicePayment{...postpaidPlanPriceFragment}devicePlan{__typename price{...postpaidPlanPriceFragment}frequency duration annualPercentageRate}deviceDataPlan{...deviceDataPlanFragment}}fragment deviceDataPlanFragment on DeviceDataPlan{__typename carrierName planType expiryTime activationFee{...postpaidPlanPriceFragment}planDetails{__typename price{...postpaidPlanPriceFragment}frequency name}agreements{...agreementFragment}}fragment postpaidPlanPriceFragment on PriceDetailRow{__typename key label displayValue value strikeOutDisplayValue strikeOutValue info{__typename title message}}fragment agreementFragment on CarrierAgreement{__typename name type format value docTitle label}fragment preOrderFragment on PreOrder{streetDate streetDateDisplayable streetDateType isPreOrder preOrderMessage preOrderStreetDateMessage}fragment AddressFields on Address{id addressLineOne addressLineTwo city state postalCode firstName lastName phone}fragment PickupPersonFields on PickupPerson{id firstName lastName email}fragment PriceDetailRowFields on PriceDetailRow{__typename key label displayValue value strikeOutValue strikeOutDisplayValue info{__typename title message}}fragment AccessPointFields on AccessPoint{id name assortmentStoreId displayName timeZone address{id addressLineOne addressLineTwo city state postalCode firstName lastName phone}isTest allowBagFee bagFeeValue isExpressEligible fulfillmentOption instructions nodeAccessType}fragment ReservationFields on Reservation{id expiryTime isUnscheduled expired showSlotExpiredError reservedSlot{__typename...on RegularSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata slotExpiryTime endTime available supportedTimeZone}...on DynamicExpressSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime endTime fulfillmentType slotMetadata slotExpiryTime available slaInMins sla{value displayValue}maxItemAllowed supportedTimeZone}...on UnscheduledSlot{price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata unscheduledHoldInDays supportedTimeZone}...on InHomeSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata slotExpiryTime endTime available supportedTimeZone}}}fragment AffirmMessageFields on AffirmMessage{__typename description termsUrl imageUrl monthlyPayment termLength isZeroAPR}fragment FulfillmentItemGroupsFields on FulfillmentItemGroup{...on SCGroup{__typename defaultMode collapsedItemIds itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}...on DigitalDeliveryGroup{__typename defaultMode collapsedItemIds itemGroups{__typename label itemIds}}...on Unscheduled{__typename defaultMode collapsedItemIds isSpecialEvent startDate endDate itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}...on FCGroup{__typename defaultMode collapsedItemIds startDate endDate isUnscheduledDeliveryEligible shippingOptions{__typename itemIds availableShippingOptions{__typename id shippingMethod deliveryDate price{__typename displayValue value}label{prefix suffix}isSelected isDefault}}hasMadeShippingChanges slaGroups{__typename label deliveryDate warningLabel sellerGroups{__typename id name isProSeller type shipOptionGroup{__typename deliveryPrice{__typename displayValue value}itemIds shipMethod}}}}...on AutoCareCenter{__typename defaultMode startDate endDate accBasketType collapsedItemIds itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}}fragment ProductFields on Product{id name usItemId itemType imageInfo{thumbnailUrl}category{categoryPath}offerId orderLimit orderMinLimit weightIncrement weightUnit averageWeight salesUnitType availabilityStatus isSubstitutionEligible isAlcohol configuration hasSellerBadge sellerId sellerName sellerType annualEvent preOrder{...preOrderFragment}badges{flags{__typename id key text}}addOnServices{serviceType groups{groupType services{selectedDisplayName offerId currentPrice{priceString}}}}}");
      var2.put("variables", (new JsonObject()).put("input", (new JsonObject()).put("purchaseContractId", var1).putNull("orderId")).put("promosEnable", true).put("wplusEnabled", true));
      return var2.toBuffer();
   }

   public HttpRequest getPCID() {
      String var1 = zs2y(32);
      HttpRequest var2 = this.client.postAbs("https://www.walmart.com/orchestra/cartxo/graphql").as(BodyCodec.buffer());
      var2.headers().setAll(Headers$Pseudo.MASP.get()).set(HttpHeaders.CONTENT_LENGTH, Headers.DEFAULT).set(Headers.SEC_CH_UA, this.getPxAPI().getDeviceSecUA()).set(X_O_CORRELATION_ID, var1).set(DEVICE_PROFILE_REF, this.deviceProfileId).set(X_LATENCY_TRACE, "1").set(WM_MP, TRUE).set(X_O_PLATFORM_VERSION, this.platformVersion).set(X_O_SEGMENT, OAOH).set(X_O_GQL_QUERY, "mutation CreateDeliveryAddress").set(WM_PAGE_URL, this.referer + "?step=cart&ss=addAddress&gxo=true").set(X_APOLLO_OPERATION_NAME, "CreateDeliveryAddress").set(Headers.SEC_CH_UA_PLATFORM, Headers.WINDOWS).set(Headers.SEC_CH_UA_MOBILE, Headers._Q0).set(HttpHeaders.USER_AGENT, this.pxAPI.getDeviceUA()).set(X_O_PLATFORM, X_O_PLATFORM_NAME).set(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON).set(HttpHeaders.ACCEPT, HttpHeaderValues.APPLICATION_JSON).set(X_ENABLE_SERVER_TIMING, "1").set(X_O_CCM, SERVER).set(WM_CORRELATION_ID, var1).set(HttpHeaders.ORIGIN, "https://www.walmart.com").set(Headers.SEC_FETCH_SITE, Headers.SAME_ORIGIN).set(Headers.SEC_FETCH_MODE, Headers.CORS).set(Headers.SEC_FETCH_DEST, Headers.EMPTY).set(HttpHeaders.REFERER, this.referer + "?step=cart&ss=addAddress&gxo=true").set(HttpHeaders.ACCEPT_ENCODING, Headers.GZIP_DEFLATE_BR).set(HttpHeaders.ACCEPT_LANGUAGE, this.pxAPI.getDeviceLang()).set(HttpHeaders.COOKIE, Headers.DEFAULT);
      return var2;
   }

   public boolean parseMapIntoCookieJar(MultiMap var1) {
      if (var1 == null) {
         return false;
      } else {
         Iterator var2 = var1.entries().iterator();

         while(var2.hasNext()) {
            Map.Entry var3 = (Map.Entry)var2.next();
            this.getWebClient().cookieStore().put((String)var3.getKey(), (String)var3.getValue(), ".walmart.com");
         }

         return !var1.isEmpty();
      }
   }

   public static CompletableFuture async$handleBadResponse(WalmartNewAPI var0, HttpResponse var1, String var2, CompletableFuture var3, JsonObject var4, String var5, String var6, WalmartNewAPI var7, int var8, Matcher var9, String var10, int var11, Object var12) {
      CompletableFuture var26;
      label109: {
         int var21;
         label130: {
            WalmartNewAPI var10000;
            JsonObject var17;
            CompletableFuture var10001;
            String var18;
            CompletableFuture var24;
            label131: {
               label102: {
                  Throwable var28;
                  label101: {
                     boolean var27;
                     label100: {
                        label91:
                        switch (var11) {
                           case 0:
                              var0.pxAPI.updatePxhd(var0.cookieStore().getCookieValue("_pxhd"));
                              CompletableFuture var25;
                              switch (var1.statusCode()) {
                                 case 307:
                                    try {
                                       if (var0.task.getMode().contains("skip")) {
                                          var26 = var0.generatePX(true);
                                          if (!var26.isDone()) {
                                             var24 = var26;
                                             return var24.exceptionally(Function.identity()).thenCompose(WalmartNewAPI::async$handleBadResponse);
                                          }
                                          break label100;
                                       }
                                    } catch (Throwable var16) {
                                       var28 = var16;
                                       var27 = false;
                                       break label101;
                                    }

                                    try {
                                       String var19 = "https://www.walmart.com" + var1.getHeader("location");
                                       var18 = var19.split("uuid=")[1].split("&")[0];
                                       Matcher var22 = VID_LOCATION_PATTERN.matcher(var19);
                                       if (var22.find()) {
                                          var6 = var22.group(1).isBlank() ? null : var22.group(1);
                                       } else {
                                          var6 = null;
                                       }

                                       var10000 = var0;
                                       var10001 = var0.pxAPI.solveCaptcha(var6, var18, var19);
                                       if (!var10001.isDone()) {
                                          var25 = var10001;
                                          return var25.exceptionally(Function.identity()).thenCompose(WalmartNewAPI::async$handleBadResponse);
                                       }
                                       break label91;
                                    } catch (Throwable var15) {
                                       var28 = var15;
                                       var27 = false;
                                       break label101;
                                    }
                                 case 412:
                                    if (var0.task.getMode().contains("skip")) {
                                       var26 = var0.generatePX(true);
                                       if (!var26.isDone()) {
                                          var24 = var26;
                                          return var24.exceptionally(Function.identity()).thenCompose(WalmartNewAPI::async$handleBadResponse);
                                       }
                                       break label109;
                                    }

                                    var17 = var1.bodyAsJsonObject();
                                    var18 = var17.getString("uuid");
                                    var5 = var17.getString("vid");
                                    var10000 = var0;
                                    var10001 = var0.pxAPI.solveCaptcha(var5, var18, var2);
                                    if (!var10001.isDone()) {
                                       var25 = var10001;
                                       return var25.exceptionally(Function.identity()).thenCompose(WalmartNewAPI::async$handleBadResponse);
                                    }
                                    break label131;
                                 case 444:
                                    break label102;
                                 default:
                                    return CompletableFuture.completedFuture(false);
                              }
                           case 1:
                              var26 = var3;
                              break label109;
                           case 2:
                              var10000 = var7;
                              var10001 = var3;
                              JsonObject var10002 = var4;
                              String var10003 = var5;
                              var5 = var6;
                              var18 = var10003;
                              var17 = var10002;
                              break label131;
                           case 3:
                              var26 = var3;
                              var21 = var8;
                              break label130;
                           case 4:
                              var26 = var3;
                              break label100;
                           case 5:
                              var10000 = var7;
                              var10001 = var3;
                              break;
                           default:
                              throw new IllegalArgumentException();
                        }

                        try {
                           boolean var23 = var10000.parseMapIntoCookieJar((MultiMap)var10001.join());
                           return CompletableFuture.completedFuture(var23);
                        } catch (Throwable var14) {
                           var28 = var14;
                           var27 = false;
                           break label101;
                        }
                     }

                     try {
                        var26.join();
                        return CompletableFuture.completedFuture(false);
                     } catch (Throwable var13) {
                        var28 = var13;
                        var27 = false;
                     }
                  }

                  Throwable var20 = var28;
                  System.out.println("Error solving captcha [DESKTOP] " + var20.getMessage());
               }

               if (var0.rotateProxy()) {
                  var0.pxAPI.restartClient(var0.client);
               }

               return CompletableFuture.completedFuture(false);
            }

            var21 = var10000.parseMapIntoCookieJar((MultiMap)var10001.join());
            var26 = var0.generatePX(false);
            if (!var26.isDone()) {
               var24 = var26;
               return var24.exceptionally(Function.identity()).thenCompose(WalmartNewAPI::async$handleBadResponse);
            }
         }

         var26.join();
         return CompletableFuture.completedFuture(Boolean.valueOf((boolean)var21));
      }

      var26.join();
      return CompletableFuture.completedFuture(false);
   }

   public JsonObject saveAddressJson() {
      JsonObject var1 = new JsonObject();
      var1.put("query", "mutation CreateDeliveryAddress($input:AccountAddressesInput!){createAccountAddress(input:$input){...DeliveryAddressMutationResponse}}fragment DeliveryAddressMutationResponse on MutateAccountAddressResponse{...AddressMutationResponse newAddress{id accessPoint{...AccessPoint}...BaseAddressResponse}}fragment AccessPoint on AccessPointRovr{id assortmentStoreId fulfillmentType accountFulfillmentOption accountAccessType}fragment AddressMutationResponse on MutateAccountAddressResponse{errors{code}enteredAddress{...BasicAddress}suggestedAddresses{...BasicAddress sealedAddress}newAddress{id...BaseAddressResponse}allowAvsOverride}fragment BasicAddress on AccountAddressBase{addressLineOne addressLineTwo city state postalCode}fragment BaseAddressResponse on AccountAddress{...BasicAddress firstName lastName phone isDefault deliveryInstructions serviceStatus capabilities allowEditOrRemove}");
      var1.put("variables", (new JsonObject()).put("input", (new JsonObject()).put("address", (new JsonObject()).put("addressLineOne", this.task.getProfile().getAddress1()).put("addressLineTwo", this.task.getProfile().getAddress2()).put("city", this.task.getProfile().getCity()).put("postalCode", this.task.getProfile().getZip()).put("state", this.task.getProfile().getState()).put("addressType", (Object)null).put("businessName", (Object)null).put("isApoFpo", (Object)null).put("isLoadingDockAvailable", (Object)null).put("isPoBox", (Object)null).put("sealedAddress", (Object)null)).put("firstName", this.task.getProfile().getFirstName()).put("lastName", this.task.getProfile().getLastName()).put("deliveryInstructions", (Object)null).put("displayLabel", (Object)null).put("isDefault", true).put("phone", this.task.getProfile().getPhone()).put("overrideAvs", true)));
      return var1;
   }

   public JsonObject savePaymentForm(PaymentToken var1) {
      JsonObject var2 = new JsonObject();
      var2.put("operationName", "AddCreditCard");
      var2.put("variables", (new JsonObject()).put("input", (new JsonObject()).put("firstName", this.task.getProfile().getFirstName()).put("lastName", this.task.getProfile().getLastName()).put("phone", this.task.getProfile().getPhone()).put("address", (new JsonObject()).put("addressLineOne", this.task.getProfile().getAddress1()).put("addressLineTwo", this.task.getProfile().getAddress2()).put("city", this.task.getProfile().getCity()).put("state", this.task.getProfile().getState()).put("postalCode", this.task.getProfile().getZip())).put("expiryYear", Integer.parseInt(this.task.getProfile().getExpiryYear())).put("expiryMonth", Integer.parseInt(this.task.getProfile().getExpiryMonth())).put("cardType", this.task.getProfile().getCardType().name()).put("encryptedCVV", var1.getEncryptedCvv()).put("encryptedPan", var1.getEncryptedPan()).put("integrityCheck", var1.getIntegrityCheck()).put("keyId", var1.getKeyId()).put("phase", var1.getPhase()).put("isDefault", true)));
      var2.put("query", "mutation AddCreditCard($input: AccountCreditCardInput!) { createAccountCreditCard(input: $input) { __typename creditCard { __typename ...CreditCardFragment } errors { __typename ...ErrorFragment } } } fragment CreditCardFragment on CreditCard { __typename id firstName lastName lastFour isDefault isTemp cardAccountLinked needVerifyCVV addressLineOne addressLineTwo city state postalCode cardType phone expiryMonth expiryYear isExpired displayTypeAndLast4 displayExpireAndName } fragment ErrorFragment on AccountPaymentError { __typename code }");
      return var2;
   }

   public JsonObject deleteCardForm(String var1) {
      JsonObject var2 = new JsonObject();
      var2.put("query", "mutation deleteCard($id:ID!){deleteAccountPayments(id:$id){status}}");
      var2.put("variables", (new JsonObject()).put("id", var1));
      return var2;
   }

   public HttpRequest getCart() {
      String var1 = zs2y(32);
      HttpRequest var2 = this.client.postAbs("https://www.walmart.com/orchestra/cartxo/graphql").as(BodyCodec.buffer());
      var2.headers().setAll(Headers$Pseudo.MASP.get()).set(HttpHeaders.CONTENT_LENGTH, Headers.DEFAULT).set(Headers.SEC_CH_UA, this.pxAPI.getDeviceSecUA()).set(X_O_CORRELATION_ID, var1).set(DEVICE_PROFILE_REF, this.deviceProfileId).set(X_LATENCY_TRACE, "1").set(WM_MP, TRUE).set(X_O_PLATFORM_VERSION, this.platformVersion).set(X_O_SEGMENT, OAOH).set(X_O_GQL_QUERY, "mutation MergeAndGetCart").set(WM_PAGE_URL, "https://www.walmart.com/").set(X_APOLLO_OPERATION_NAME, "MergeAndGetCart").set(Headers.SEC_CH_UA_PLATFORM, Headers.WINDOWS).set(Headers.SEC_CH_UA_MOBILE, Headers._Q0).set(HttpHeaders.USER_AGENT, this.pxAPI.getDeviceUA()).set(X_O_PLATFORM, X_O_PLATFORM_NAME).set(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON).set(HttpHeaders.ACCEPT, HttpHeaderValues.APPLICATION_JSON).set(X_ENABLE_SERVER_TIMING, "1").set(X_O_CCM, SERVER).set(WM_CORRELATION_ID, var1).set(HttpHeaders.ORIGIN, "https://www.walmart.com").set(Headers.SEC_FETCH_SITE, Headers.SAME_ORIGIN).set(Headers.SEC_FETCH_MODE, Headers.CORS).set(Headers.SEC_FETCH_DEST, Headers.EMPTY).set(HttpHeaders.REFERER, "https://www.walmart.com/wallet").set(HttpHeaders.ACCEPT_ENCODING, Headers.GZIP_DEFLATE_BR).set(HttpHeaders.ACCEPT_LANGUAGE, this.pxAPI.getDeviceLang()).set(HttpHeaders.COOKIE, Headers.DEFAULT);
      return var2;
   }

   public HttpRequest createAccount() {
      HttpRequest var1 = this.client.postAbs("https://www.walmart.com/account/electrode/api/signup?vid=oaoh").as(BodyCodec.buffer());
      switch (this.headerLogic) {
         case 0:
            var1.putHeader("content-length", "DEFAULT_VALUE");
            var1.putHeader("sec-ch-ua", this.pxAPI.getDeviceSecUA());
            var1.putHeader("sec-ch-ua-mobile", "?0");
            var1.putHeader("user-agent", this.pxAPI.getDeviceUA());
            var1.putHeader("sec-ch-ua-platform", this.pxAPI.getDeviceUA().contains("Macintosh") ? "\"macOS\"" : "\"Windows\"");
            var1.putHeader("content-type", "application/json");
            var1.putHeader("accept", "*/*");
            var1.putHeader("origin", "https://www.walmart.com");
            var1.putHeader("sec-fetch-site", "same-origin");
            var1.putHeader("sec-fetch-mode", "cors");
            var1.putHeader("sec-fetch-dest", "empty");
            var1.putHeader("referer", "https://www.walmart.com/account/signup?vid=oaoh");
            var1.putHeader("accept-encoding", "gzip, deflate, br");
            var1.putHeader("accept-language", this.pxAPI.getDeviceLang());
            break;
         case 1:
            var1.putHeader("content-length", "DEFAULT_VALUE");
            var1.putHeader("accept", "*/*");
            var1.putHeader("user-agent", this.pxAPI.getDeviceUA());
            var1.putHeader("content-type", "application/json");
            var1.putHeader("origin", "https://www.walmart.com");
            var1.putHeader("sec-fetch-site", "same-origin");
            var1.putHeader("sec-fetch-mode", "cors");
            var1.putHeader("sec-fetch-dest", "empty");
            var1.putHeader("referer", "https://www.walmart.com/account/signup?vid=oaoh");
            var1.putHeader("accept-encoding", "gzip, deflate, br");
            var1.putHeader("accept-language", this.pxAPI.getDeviceLang());
            break;
         default:
            var1.putHeader("content-length", "DEFAULT_VALUE");
            var1.putHeader("accept", "*/*");
            var1.putHeader("user-agent", this.pxAPI.getDeviceUA());
            var1.putHeader("device_profile_ref_id", this.deviceProfileId);
            var1.putHeader("content-type", "application/json");
            var1.putHeader("origin", "https://www.walmart.com");
            var1.putHeader("sec-fetch-site", "same-origin");
            var1.putHeader("sec-fetch-mode", "cors");
            var1.putHeader("sec-fetch-dest", "empty");
            var1.putHeader("referer", "https://www.walmart.com/account/signup?vid=oaoh");
            var1.putHeader("accept-encoding", "gzip, deflate, br");
            var1.putHeader("accept-language", this.pxAPI.getDeviceLang());
      }

      return var1;
   }

   public boolean isLoggedIn() {
      return this.loggedIn;
   }

   public JsonObject fulfillmentBody() {
      JsonObject var1 = new JsonObject();
      var1.put("query", "mutation setFulfillment($input:SetFulfillmentInput!){setFulfillment(input:$input){...CartFragment}}fragment CartFragment on Cart{id checkoutable customer{id firstName lastName isGuest}addressMode migrationLineItems{quantity quantityLabel quantityString accessibilityQuantityLabel offerId usItemId productName thumbnailUrl addOnService priceInfo{linePrice{value displayValue}}selectedVariants{name value}}lineItems{id quantity quantityString quantityLabel isPreOrder bundleComponents{offerId quantity}registryId fulfillmentPreference selectedVariants{name value}priceInfo{priceDisplayCodes{showItemPrice priceDisplayCondition finalCostByWeight}itemPrice{...lineItemPriceInfoFragment}wasPrice{...lineItemPriceInfoFragment}unitPrice{...lineItemPriceInfoFragment}linePrice{...lineItemPriceInfoFragment}}product{name usItemId imageInfo{thumbnailUrl}itemType offerId sellerId sellerName hasSellerBadge orderLimit orderMinLimit weightUnit weightIncrement salesUnit salesUnitType sellerType isAlcohol fulfillmentType fulfillmentSpeed fulfillmentTitle classType rhPath availabilityStatus brand category{categoryPath}departmentName configuration snapEligible preOrder{isPreOrder}}registryInfo{registryId registryType}wirelessPlan{planId mobileNumber postPaidPlan{...postpaidPlanDetailsFragment}}fulfillmentSourcingDetails{currentSelection requestedSelection}}fulfillment{intent accessPoint{...accessPointFragment}reservation{...reservationFragment}storeId displayStoreSnackBarMessage homepageBookslotDetails{title subTitle expiryText}deliveryAddress{addressLineOne addressLineTwo city state postalCode firstName lastName id}fulfillmentItemGroups{...on FCGroup{__typename defaultMode collapsedItemIds startDate endDate fulfillmentSwitchInfo{fulfillmentType benefit{type price itemCount date}}shippingOptions{__typename itemIds availableShippingOptions{__typename id shippingMethod deliveryDate price{__typename displayValue value}label{prefix suffix}isSelected isDefault slaTier}}hasMadeShippingChanges slaGroups{__typename label sellerGroups{__typename id name isProSeller type catalogSellerId shipOptionGroup{__typename deliveryPrice{__typename displayValue value}itemIds shipMethod}}warningLabel}}...on SCGroup{__typename defaultMode collapsedItemIds fulfillmentSwitchInfo{fulfillmentType benefit{type price itemCount date}}itemGroups{__typename label itemIds}accessPoint{...accessPointFragment}reservation{...reservationFragment}}...on DigitalDeliveryGroup{__typename defaultMode collapsedItemIds itemGroups{__typename label itemIds}}...on Unscheduled{__typename defaultMode collapsedItemIds itemGroups{__typename label itemIds}accessPoint{...accessPointFragment}reservation{...reservationFragment}fulfillmentSwitchInfo{fulfillmentType benefit{type price itemCount date}}}...on AutoCareCenter{__typename defaultMode collapsedItemIds startDate endDate accBasketType itemGroups{__typename label itemIds}accessPoint{...accessPointFragment}reservation{...reservationFragment}fulfillmentSwitchInfo{fulfillmentType benefit{type price itemCount date}}}}suggestedSlotAvailability{isPickupAvailable isDeliveryAvailable nextPickupSlot{startTime endTime slaInMins}nextDeliverySlot{startTime endTime slaInMins}nextUnscheduledPickupSlot{startTime endTime slaInMins}nextSlot{__typename...on RegularSlot{fulfillmentOption fulfillmentType startTime}...on DynamicExpressSlot{fulfillmentOption fulfillmentType startTime slaInMins}...on UnscheduledSlot{fulfillmentOption fulfillmentType startTime unscheduledHoldInDays}}}}priceDetails{subTotal{...priceTotalFields}fees{...priceTotalFields}taxTotal{...priceTotalFields}grandTotal{...priceTotalFields}belowMinimumFee{...priceTotalFields}minimumThreshold{value displayValue}ebtSnapMaxEligible{displayValue value}}affirm{isMixedPromotionCart message{description termsUrl}nonAffirmGroup{...nonAffirmGroupFields}affirmGroups{...on AffirmItemGroup{__typename message{description termsUrl}flags{type displayLabel}name label itemCount collapsedItemIds itemIds defaultMode}}}checkoutableErrors{code shouldDisableCheckout itemIds}checkoutableWarnings{code itemIds}operationalErrors{offerId itemId requestedQuantity adjustedQuantity code upstreamErrorCode}}fragment postpaidPlanDetailsFragment on PostPaidPlan{espOrderSummaryId espOrderId espOrderLineId warpOrderId warpSessionId devicePayment{...postpaidPlanPriceFragment}devicePlan{price{...postpaidPlanPriceFragment}frequency duration annualPercentageRate}deviceDataPlan{...deviceDataPlanFragment}}fragment deviceDataPlanFragment on DeviceDataPlan{carrierName planType expiryTime activationFee{...postpaidPlanPriceFragment}planDetails{price{...postpaidPlanPriceFragment}frequency name}agreements{...agreementFragment}}fragment postpaidPlanPriceFragment on PriceDetailRow{key label displayValue value strikeOutDisplayValue strikeOutValue info{title message}}fragment agreementFragment on CarrierAgreement{name type format value docTitle label}fragment priceTotalFields on PriceDetailRow{label displayValue value key strikeOutDisplayValue strikeOutValue}fragment lineItemPriceInfoFragment on Price{displayValue value}fragment accessPointFragment on AccessPoint{id assortmentStoreId name accessType fulfillmentType fulfillmentOption displayName timeZone address{addressLineOne addressLineTwo city postalCode state phone}}fragment reservationFragment on Reservation{expiryTime isUnscheduled expired showSlotExpiredError reservedSlot{__typename...on RegularSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}nodeAccessType accessPointId fulfillmentOption startTime fulfillmentType slotMetadata endTime available supportedTimeZone isAlcoholRestricted}...on DynamicExpressSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata available slaInMins maxItemAllowed supportedTimeZone isAlcoholRestricted}...on UnscheduledSlot{price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata unscheduledHoldInDays supportedTimeZone}...on InHomeSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata endTime available supportedTimeZone isAlcoholRestricted}}}fragment nonAffirmGroupFields on NonAffirmGroup{label itemCount itemIds collapsedItemIds}");
      var1.put("variables", (new JsonObject()).put("input", (new JsonObject()).put("accessPointId", this.accessPointId).put("addressId", this.addressId).put("cartId", this.cartId).putNull("registry").put("fulfillmentOption", "SHIPPING").putNull("postalCode").put("storeId", this.storeID)));
      return var1;
   }

   public String genCorrelationId() {
      return null;
   }

   public JsonObject PCIDForm() {
      JsonObject var1 = new JsonObject();
      var1.put("query", "mutation CreateDeliveryAddress($input:AccountAddressesInput!){createAccountAddress(input:$input){...DeliveryAddressMutationResponse}}fragment DeliveryAddressMutationResponse on MutateAccountAddressResponse{...AddressMutationResponse newAddress{id accessPoint{...AccessPoint}...BaseAddressResponse}}fragment AccessPoint on AccessPointRovr{id assortmentStoreId fulfillmentType accountFulfillmentOption accountAccessType}fragment AddressMutationResponse on MutateAccountAddressResponse{errors{code}enteredAddress{...BasicAddress}suggestedAddresses{...BasicAddress sealedAddress}newAddress{id...BaseAddressResponse}allowAvsOverride}fragment BasicAddress on AccountAddressBase{addressLineOne addressLineTwo city state postalCode}fragment BaseAddressResponse on AccountAddress{...BasicAddress firstName lastName phone isDefault deliveryInstructions serviceStatus capabilities allowEditOrRemove}");
      var1.put("variables", (new JsonObject()).put("input", (new JsonObject()).put("address", (new JsonObject()).put("addressLineOne", this.task.getProfile().getAddress1()).put("addressLineTwo", this.task.getProfile().getAddress2()).put("city", this.task.getProfile().getCity()).put("postalCode", this.task.getProfile().getZip()).put("state", this.task.getProfile().getState()).putNull("addressType").putNull("businessName").putNull("isApoFpo").putNull("isLoadingDockAvailable").putNull("isPoBox").putNull("sealedAddress")).put("firstName", this.task.getProfile().getFirstName()).put("lastName", this.task.getProfile().getLastName()).putNull("deliveryInstructions").putNull("displayLabel").put("isDefault", false).put("phone", this.task.getProfile().getPhone()).put("overrideAvs", true)));
      return var1;
   }

   public HttpRequest fetchCreditCardIds() {
      String var1 = zs2y(32);
      HttpRequest var2 = this.client.postAbs("https://www.walmart.com/orchestra/home/graphql").as(BodyCodec.buffer());
      var2.headers().setAll(Headers$Pseudo.MASP.get()).set(HttpHeaders.CONTENT_LENGTH, Headers.DEFAULT).set(X_O_CORRELATION_ID, var1).set(DEVICE_PROFILE_REF, this.deviceProfileId).set(X_LATENCY_TRACE, "1").set(WM_MP, TRUE).set(X_O_PLATFORM_VERSION, this.platformVersion).set(X_O_SEGMENT, OAOH).set(X_O_GQL_QUERY, "query getWalletPayments").set(X_APOLLO_OPERATION_NAME, "getWalletPayments").set(HttpHeaders.USER_AGENT, this.pxAPI.getDeviceUA()).set(X_O_PLATFORM, X_O_PLATFORM_NAME).set(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON).set(HttpHeaders.ACCEPT, HttpHeaderValues.APPLICATION_JSON).set(X_ENABLE_SERVER_TIMING, "1").set(X_O_CCM, SERVER).set(WM_CORRELATION_ID, var1).set(HttpHeaders.ORIGIN, "https://www.walmart.com").set(Headers.SEC_FETCH_SITE, Headers.SAME_ORIGIN).set(Headers.SEC_FETCH_MODE, Headers.CORS).set(Headers.SEC_FETCH_DEST, Headers.EMPTY).set(HttpHeaders.REFERER, "https://www.walmart.com/wallet").set(HttpHeaders.ACCEPT_ENCODING, Headers.GZIP_DEFLATE_BR).set(HttpHeaders.ACCEPT_LANGUAGE, this.pxAPI.getDeviceLang()).set(HttpHeaders.COOKIE, Headers.DEFAULT);
      return var2;
   }

   public void close() {
      if (this.pxAPI != null) {
         this.pxAPI.close();
      }

      super.close();
   }

   public HttpRequest submitBilling() {
      String var1 = zs2y(32);
      HttpRequest var2 = this.client.postAbs("https://www.walmart.com/orchestra/cartxo/graphql").as(BodyCodec.buffer());
      var2.headers().setAll(Headers$Pseudo.MASP.get()).set(HttpHeaders.CONTENT_LENGTH, Headers.DEFAULT).set(Headers.SEC_CH_UA, this.getPxAPI().getDeviceSecUA()).set(X_O_CORRELATION_ID, var1).set(DEVICE_PROFILE_REF, this.deviceProfileId).set(X_LATENCY_TRACE, "1").set(WM_MP, TRUE).set(X_O_PLATFORM_VERSION, this.platformVersion).set(X_O_SEGMENT, OAOH).set(X_O_GQL_QUERY, "mutation CreateCreditCard").set(X_APOLLO_OPERATION_NAME, "CreateCreditCard").set(Headers.SEC_CH_UA_PLATFORM, Headers.WINDOWS).set(Headers.SEC_CH_UA_MOBILE, Headers._Q0).set(HttpHeaders.USER_AGENT, this.pxAPI.getDeviceUA()).set(X_O_PLATFORM, X_O_PLATFORM_NAME).set(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON).set(HttpHeaders.ACCEPT, HttpHeaderValues.APPLICATION_JSON).set(X_ENABLE_SERVER_TIMING, "1").set(X_O_CCM, SERVER).set(WM_CORRELATION_ID, var1).set(HttpHeaders.ORIGIN, "https://www.walmart.com").set(Headers.SEC_FETCH_SITE, Headers.SAME_ORIGIN).set(Headers.SEC_FETCH_MODE, Headers.CORS).set(Headers.SEC_FETCH_DEST, Headers.EMPTY).set(HttpHeaders.REFERER, "https://www.walmart.com/checkout/review-order?cartId=" + this.cartId + "&wv=add_credit_debit_card").set(HttpHeaders.ACCEPT_ENCODING, Headers.GZIP_DEFLATE_BR).set(HttpHeaders.ACCEPT_LANGUAGE, this.pxAPI.getDeviceLang()).set(HttpHeaders.COOKIE, Headers.DEFAULT);
      return var2;
   }

   public Buffer getTendersForm() {
      JsonObject var1 = new JsonObject();
      var1.put("query", "query getTenderPlan($tenderPlanInput:TenderPlanInput!){tenderPlan(input:$tenderPlanInput){__typename tenderPlan{...TenderPlanFields}}}fragment TenderPlanFields on TenderPlan{__typename id contractId grandTotal{...PriceDetailRowFields}authorizationAmount{...PriceDetailRowFields}allocationStatus paymentGroups{...PaymentGroupFields}otcDeliveryBenefit{...PriceDetailRowFields}otherAllowedPayments{type status}addPaymentType hasAmountUnallocated weightDebitTotal{...PriceDetailRowFields}}fragment PriceDetailRowFields on PriceDetailRow{__typename key label displayValue value info{__typename title message}}fragment PaymentGroupFields on TenderPlanPaymentGroup{__typename type subTotal{__typename key label displayValue value info{__typename title message}}selectedCount allocations{...CreditCardAllocationFragment...GiftCardAllocationFragment...EbtCardAllocationFragment...DsCardAllocationFragment...PayPalAllocationFragment...AffirmAllocationFragment}statusMessage}fragment CreditCardAllocationFragment on CreditCardAllocation{__typename card{...CreditCardFragment}canEditOrDelete canDeselect isEligible isSelected allocationAmount{__typename displayValue value}capOneReward{...CapOneFields}statusMessage{__typename messageStatus messageType}paymentType}fragment CapOneFields on CapOneReward{credentialId redemptionRate redemptionUrl redemptionMethod rewardPointsBalance rewardPointsSelected rewardAmountSelected}fragment CreditCardFragment on CreditCard{__typename id isDefault cardAccountLinked needVerifyCVV cardType expiryMonth expiryYear isExpired firstName lastName lastFour isEditable phone}fragment GiftCardAllocationFragment on GiftCardAllocation{__typename card{...GiftCardFields}canEditOrDelete canDeselect isEligible isSelected allocationAmount{__typename displayValue value}statusMessage{__typename messageStatus messageType}paymentType remainingBalance{__typename displayValue value}}fragment GiftCardFields on GiftCard{__typename id balance{cardBalance}lastFour displayLabel}fragment EbtCardAllocationFragment on EbtCardAllocation{__typename card{__typename id lastFour firstName lastName}canEditOrDelete canDeselect isEligible isSelected allocationAmount{__typename displayValue value}statusMessage{__typename messageStatus messageType}paymentType ebtMaxEligibleAmount{__typename displayValue value}cardBalance{__typename displayValue value}}fragment DsCardAllocationFragment on DsCardAllocation{__typename card{...DsCardFields}canEditOrDelete canDeselect isEligible isSelected allocationAmount{__typename displayValue value}statusMessage{__typename messageStatus messageType}paymentType canApplyAmount{__typename displayValue value}remainingBalance{__typename displayValue value}paymentPromotions{__typename programName canApplyAmount{__typename displayValue value}allocationAmount{__typename displayValue value}remainingBalance{__typename displayValue value}balance{__typename displayValue value}termsLink isInvalid}otcShippingBenefit termsLink}fragment DsCardFields on DsCard{__typename id displayLabel lastFour fundingProgram balance{cardBalance}dsCardType cardName}fragment PayPalAllocationFragment on PayPalAllocation{__typename allocationAmount{__typename displayValue value}paymentHandle paymentType email}fragment AffirmAllocationFragment on AffirmAllocation{__typename allocationAmount{__typename displayValue value}paymentHandle paymentType cardType firstName lastName}");
      var1.put("variables", (new JsonObject()).put("tenderPlanInput", (new JsonObject()).put("contractId", this.contractId).put("isAmendFlow", false)));
      return var1.toBuffer();
   }

   public HttpRequest setPayment() {
      String var1 = zs2y(32);
      HttpRequest var2 = this.client.postAbs("https://www.walmart.com/orchestra/cartxo/graphql").as(BodyCodec.buffer());
      var2.headers().setAll(Headers$Pseudo.MASP.get()).set(HttpHeaders.CONTENT_LENGTH, Headers.DEFAULT).set(Headers.SEC_CH_UA, this.getPxAPI().getDeviceSecUA()).set(X_O_CORRELATION_ID, var1).set(DEVICE_PROFILE_REF, this.deviceProfileId).set(X_LATENCY_TRACE, "1").set(WM_MP, TRUE).set(X_O_PLATFORM_VERSION, this.platformVersion).set(X_O_SEGMENT, OAOH).set(X_O_GQL_QUERY, "mutation SetPayment").set(X_APOLLO_OPERATION_NAME, "SetPayment").set(Headers.SEC_CH_UA_PLATFORM, Headers.WINDOWS).set(Headers.SEC_CH_UA_MOBILE, Headers._Q0).set(HttpHeaders.USER_AGENT, this.pxAPI.getDeviceUA()).set(X_O_PLATFORM, X_O_PLATFORM_NAME).set(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON).set(HttpHeaders.ACCEPT, HttpHeaderValues.APPLICATION_JSON).set(X_ENABLE_SERVER_TIMING, "1").set(X_O_CCM, SERVER).set(WM_CORRELATION_ID, var1).set(HttpHeaders.ORIGIN, "https://www.walmart.com").set(Headers.SEC_FETCH_SITE, Headers.SAME_ORIGIN).set(Headers.SEC_FETCH_MODE, Headers.CORS).set(Headers.SEC_FETCH_DEST, Headers.EMPTY).set(HttpHeaders.REFERER, "https://www.walmart.com/checkout/review-order?pcid=" + this.contractId + "&buynow=1").set(HttpHeaders.ACCEPT_ENCODING, Headers.GZIP_DEFLATE_BR).set(HttpHeaders.ACCEPT_LANGUAGE, this.pxAPI.getDeviceLang()).set(HttpHeaders.COOKIE, Headers.DEFAULT);
      return var2;
   }

   public HttpRequest addToCart() {
      this.cookieStore().removeAnyMatch("tb-c30");
      String var1 = zs2y(32);
      HttpRequest var2 = this.client.postAbs("https://www.walmart.com/orchestra/home/graphql").as(BodyCodec.buffer());
      var2.headers().setAll(Headers$Pseudo.MASP.get()).set(HttpHeaders.CONTENT_LENGTH, Headers.DEFAULT).set(Headers.SEC_CH_UA, this.getPxAPI().getDeviceSecUA()).set(X_O_CORRELATION_ID, var1).set(DEVICE_PROFILE_REF, this.deviceProfileId).set(X_LATENCY_TRACE, "1").set(WM_MP, TRUE).set(X_O_PLATFORM_VERSION, this.platformVersion).set(X_O_SEGMENT, OAOH).set(X_O_GQL_QUERY, "mutation updateItems").set(WM_PAGE_URL, this.referer).set(X_APOLLO_OPERATION_NAME, "updateItems").set(Headers.SEC_CH_UA_PLATFORM, Headers.WINDOWS).set(Headers.SEC_CH_UA_MOBILE, Headers._Q0).set(HttpHeaders.USER_AGENT, this.pxAPI.getDeviceUA()).set(X_O_PLATFORM, X_O_PLATFORM_NAME).set(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON).set(HttpHeaders.ACCEPT, HttpHeaderValues.APPLICATION_JSON).set(X_ENABLE_SERVER_TIMING, "1").set(X_O_CCM, SERVER).set(WM_CORRELATION_ID, var1).set(HttpHeaders.ORIGIN, "https://www.walmart.com").set(Headers.SEC_FETCH_SITE, Headers.SAME_ORIGIN).set(Headers.SEC_FETCH_MODE, Headers.CORS).set(Headers.SEC_FETCH_DEST, Headers.EMPTY).set(HttpHeaders.REFERER, this.referer).set(HttpHeaders.ACCEPT_ENCODING, Headers.GZIP_DEFLATE_BR).set(HttpHeaders.ACCEPT_LANGUAGE, this.pxAPI.getDeviceLang()).set(HttpHeaders.COOKIE, Headers.DEFAULT);
      return var2;
   }

   public PerimeterX getPxAPI() {
      return this.pxAPI;
   }

   public HttpRequest product(String var1) {
      HttpRequest var2 = this.client.getAbs(var1).as(BodyCodec.buffer());
      var2.putHeader("sec-ch-ua", this.pxAPI.getDeviceSecUA());
      var2.putHeader("sec-ch-ua-mobile", "?0");
      var2.putHeader("sec-ch-ua-platform", "\"Windows\"");
      var2.putHeader("upgrade-insecure-requests", "1");
      var2.putHeader("user-agent", this.pxAPI.getDeviceUA());
      var2.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      var2.putHeader("sec-fetch-site", "none");
      var2.putHeader("sec-fetch-mode", "navigate");
      var2.putHeader("sec-fetch-user", "?1");
      var2.putHeader("sec-fetch-dest", "document");
      var2.putHeader("accept-encoding", "gzip, deflate, br");
      var2.putHeader("accept-language", this.pxAPI.getDeviceLang());
      return var2;
   }

   public JsonObject getCartQuery() {
      JsonObject var1 = new JsonObject();
      var1.put("query", "mutation MergeAndGetCart($input:MergeAndGetCartInput!){mergeAndGetCart(input:$input){id checkoutable customer{id isGuest}}}");
      JsonObject var2 = new JsonObject();
      var2.put("input", (new JsonObject()).putNull("cartId").put("strategy", "SWEEP"));
      var1.put("variables", var2);
      return var1;
   }

   public JsonObject finalizeShippingForm(String var1) {
      JsonObject var2 = new JsonObject();
      var2.put("query", "mutation setShipping($cartId:ID! $addressId:String!){fulfillmentMutations{setShipping(input:{cartId:$cartId addressId:$addressId}){id}}}");
      var2.put("variables", (new JsonObject()).put("cartId", this.cartId).put("addressId", var1));
      return var2;
   }

   public static CompletableFuture async$generatePX(WalmartNewAPI var0, int var1, WalmartNewAPI var2, CompletableFuture var3, int var4, Object var5) {
      Exception var10;
      label30: {
         boolean var11;
         WalmartNewAPI var10000;
         CompletableFuture var10001;
         switch (var4) {
            case 0:
               try {
                  if (var1 != 0) {
                     var0.pxAPI.reset();
                  }

                  var10000 = var0;
                  var10001 = var0.pxAPI.solve();
                  if (!var10001.isDone()) {
                     CompletableFuture var9 = var10001;
                     return var9.exceptionally(Function.identity()).thenCompose(WalmartNewAPI::async$generatePX);
                  }
                  break;
               } catch (Exception var7) {
                  var10 = var7;
                  var11 = false;
                  break label30;
               }
            case 1:
               var10000 = var2;
               var10001 = var3;
               break;
            default:
               throw new IllegalArgumentException();
         }

         try {
            return CompletableFuture.completedFuture(var10000.parseMapIntoCookieJar((MultiMap)var10001.join()));
         } catch (Exception var6) {
            var10 = var6;
            var11 = false;
         }
      }

      Exception var8 = var10;
      System.out.println("Error generating desktop session: " + var8.getMessage());
      return CompletableFuture.completedFuture(false);
   }

   public HttpRequest saveAddress() {
      String var1 = zs2y(32);
      HttpRequest var2 = this.client.postAbs("https://www.walmart.com/orchestra/home/graphql").as(BodyCodec.buffer());
      var2.headers().setAll(Headers$Pseudo.MASP.get()).set(HttpHeaders.CONTENT_LENGTH, Headers.DEFAULT).set(X_O_SEGMENT, OAOH).set(X_O_CORRELATION_ID, var1).set(DEVICE_PROFILE_REF, this.deviceProfileId).set(X_LATENCY_TRACE, "1").set(WM_MP, TRUE).set(X_O_PLATFORM_VERSION, this.platformVersion).set(X_O_GQL_QUERY, "mutation CreateDeliveryAddress").set(WM_PAGE_URL, "https://www.walmart.com/account/delivery-addresses").set(X_APOLLO_OPERATION_NAME, "CreateDeliveryAddress").set(HttpHeaders.USER_AGENT, this.pxAPI.getDeviceUA()).set(X_O_PLATFORM, X_O_PLATFORM_NAME).set(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON).set(HttpHeaders.ACCEPT, HttpHeaderValues.APPLICATION_JSON).set(X_ENABLE_SERVER_TIMING, "1").set(X_O_CCM, SERVER).set(WM_CORRELATION_ID, var1).set(HttpHeaders.ORIGIN, "https://www.walmart.com").set(Headers.SEC_FETCH_SITE, Headers.SAME_ORIGIN).set(Headers.SEC_FETCH_MODE, Headers.CORS).set(Headers.SEC_FETCH_DEST, Headers.EMPTY).set(HttpHeaders.REFERER, "https://www.walmart.com/account/delivery-addresses").set(HttpHeaders.ACCEPT_ENCODING, Headers.GZIP_DEFLATE_BR).set(HttpHeaders.ACCEPT_LANGUAGE, this.pxAPI.getDeviceLang()).set(HttpHeaders.COOKIE, Headers.DEFAULT);
      return var2;
   }
}
