/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.netty.handler.codec.http.HttpHeaderValues
 *  io.vertx.core.MultiMap
 *  io.vertx.core.buffer.Buffer
 *  io.vertx.core.http.HttpHeaders
 *  io.vertx.core.json.JsonArray
 *  io.vertx.core.json.JsonObject
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.client.HttpResponse
 *  io.vertx.ext.web.codec.BodyCodec
 */
package io.trickle.task.sites.walmart.graphql;

import io.netty.handler.codec.http.HttpHeaderValues;
import io.trickle.account.Account;
import io.trickle.task.Task;
import io.trickle.task.antibot.impl.px.PerimeterX;
import io.trickle.task.sites.walmart.graphql.API;
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
import java.lang.invoke.LambdaMetafactory;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.regex.Matcher;

public class WalmartNewAPI
extends API {
    public String dateOfPrevReq;
    public int headerLogic = ThreadLocalRandom.current().nextInt(3);
    public PerimeterX<MultiMap, MultiMap> pxAPI = null;

    @Override
    public JsonObject buyNowPreloadBody() {
        JsonObject jsonObject = new JsonObject();
        JsonObject jsonObject2 = new JsonObject();
        jsonObject.put("query", (Object)"mutation CreateBuyNowContract( $buyNowContractInput:BuyNowContractInput! $promosEnable:Boolean! $wplusEnabled:Boolean! ){createBuyNowContract(input:$buyNowContractInput){...ContractFragment}}fragment ContractFragment on PurchaseContract{id associateDiscountStatus addressMode tenderPlanId papEbtAllowed allowedPaymentGroupTypes cartCustomerContext @include(if:$wplusEnabled){isMembershipOptedIn isEligibleForFreeTrial paymentData{hasCreditCard}}checkoutError{code errorData{__typename...on OutOfStock{offerId}__typename...on UnavailableOffer{offerId}__typename...on ItemExpired{offerId}__typename...on ItemQuantityAdjusted{offerId requestedQuantity adjustedQuantity}}operationalErrorCode message}checkoutableWarnings{code itemIds}allocationStatus payments{id paymentType cardType lastFour isDefault cvvRequired preferenceId paymentPreferenceId paymentHandle expiryMonth expiryYear firstName lastName email amountPaid cardImage cardImageAlt isLinkedCard capOneReward{credentialId redemptionUrl redemptionRate redemptionMethod rewardPointsBalance rewardPointsSelected rewardAmountSelected}remainingBalance{displayValue value}}order{id status orderVersion mobileNumber}terms{alcoholAccepted bagFeeAccepted smsOptInAccepted marketingEmailPrefOptIn}donationDetails{charityEIN charityName amount{displayValue value}acceptDonation}lineItems{...LineItemFields}tippingDetails{suggestedAmounts{value displayValue}maxAmount{value displayValue}selectedTippingAmount{value displayValue}}customer{id firstName lastName isGuest email phone}fulfillment{deliveryDetails{deliveryInstructions deliveryOption}pickupChoices{isSelected fulfillmentType accessType accessMode accessPointId}deliveryAddress{...AddressFields}alternatePickupPerson{...PickupPersonFields}primaryPickupPerson{...PickupPersonFields}fulfillmentItemGroups{...FulfillmentItemGroupsFields}accessPoint{...AccessPointFields}reservation{...ReservationFields}}priceDetails{subTotal{...PriceDetailRowFields}totalItemQuantity fees{...PriceDetailRowFields}taxTotal{...PriceDetailRowFields}grandTotal{...PriceDetailRowFields}belowMinimumFee{...PriceDetailRowFields}authorizationAmount{...PriceDetailRowFields}weightDebitTotal{...PriceDetailRowFields}discounts{...PriceDetailRowFields}otcDeliveryBenefit{...PriceDetailRowFields}ebtSnapMaxEligible{...PriceDetailRowFields}ebtCashMaxEligible{...PriceDetailRowFields}hasAmountUnallocated affirm{__typename message{...AffirmMessageFields}}}checkoutGiftingDetails{isCheckoutGiftingOptin isWalmartProtectionPlanPresent isAppleCarePresent isRestrictedPaymentPresent giftMessageDetails{giftingMessage recipientEmail recipientName senderName}}promotions @include(if:$promosEnable){displayValue promoId terms}showPromotions @include(if:$promosEnable) errors{code message lineItems{...LineItemFields}}}fragment LineItemFields on LineItem{id quantity quantityString quantityLabel accessibilityQuantityLabel isPreOrder fulfillmentSourcingDetails{currentSelection requestedSelection}packageQuantity priceInfo{priceDisplayCodes{showItemPrice priceDisplayCondition finalCostByWeight}itemPrice{displayValue value}linePrice{displayValue value}preDiscountedLinePrice{displayValue value}wasPrice{displayValue value}unitPrice{displayValue value}}isSubstitutionSelected isGiftEligible selectedVariants{name value}product{id name usItemId itemType imageInfo{thumbnailUrl}offerId orderLimit orderMinLimit weightIncrement weightUnit averageWeight salesUnitType availabilityStatus isSubstitutionEligible isAlcohol configuration hasSellerBadge sellerId sellerName sellerType preOrder{...preOrderFragment}addOnServices{serviceType groups{groupType services{selectedDisplayName offerId currentPrice{priceString}}}}}discounts{key label displayValue @include(if:$promosEnable) displayLabel @include(if:$promosEnable)}wirelessPlan{planId mobileNumber __typename postPaidPlan{...postpaidPlanDetailsFragment}}selectedAddOnServices{offerId quantity groupType}registryInfo{registryId registryType}}fragment postpaidPlanDetailsFragment on PostPaidPlan{__typename espOrderSummaryId espOrderId espOrderLineId warpOrderId warpSessionId devicePayment{...postpaidPlanPriceFragment}devicePlan{__typename price{...postpaidPlanPriceFragment}frequency duration annualPercentageRate}deviceDataPlan{...deviceDataPlanFragment}}fragment deviceDataPlanFragment on DeviceDataPlan{__typename carrierName planType expiryTime activationFee{...postpaidPlanPriceFragment}planDetails{__typename price{...postpaidPlanPriceFragment}frequency name}agreements{...agreementFragment}}fragment postpaidPlanPriceFragment on PriceDetailRow{__typename key label displayValue value strikeOutDisplayValue strikeOutValue info{__typename title message}}fragment agreementFragment on CarrierAgreement{__typename name type format value docTitle label}fragment preOrderFragment on PreOrder{streetDate streetDateDisplayable streetDateType isPreOrder preOrderMessage preOrderStreetDateMessage}fragment AddressFields on Address{id addressLineOne addressLineTwo city state postalCode firstName lastName phone}fragment PickupPersonFields on PickupPerson{id firstName lastName email}fragment PriceDetailRowFields on PriceDetailRow{__typename key label displayValue value strikeOutValue strikeOutDisplayValue info{__typename title message}}fragment AccessPointFields on AccessPoint{id name assortmentStoreId displayName timeZone address{id addressLineOne addressLineTwo city state postalCode firstName lastName phone}isTest allowBagFee bagFeeValue isExpressEligible fulfillmentOption instructions nodeAccessType}fragment ReservationFields on Reservation{id expiryTime isUnscheduled expired showSlotExpiredError reservedSlot{__typename...on RegularSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata slotExpiryTime endTime available supportedTimeZone}...on DynamicExpressSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime endTime fulfillmentType slotMetadata slotExpiryTime available slaInMins maxItemAllowed supportedTimeZone}...on UnscheduledSlot{price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata unscheduledHoldInDays supportedTimeZone}...on InHomeSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata slotExpiryTime endTime available supportedTimeZone}}}fragment AffirmMessageFields on AffirmMessage{__typename description termsUrl imageUrl monthlyPayment termLength isZeroAPR}fragment FulfillmentItemGroupsFields on FulfillmentItemGroup{...on SCGroup{__typename defaultMode collapsedItemIds itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}...on DigitalDeliveryGroup{__typename defaultMode collapsedItemIds itemGroups{__typename label itemIds}}...on Unscheduled{__typename defaultMode collapsedItemIds itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}...on FCGroup{__typename defaultMode collapsedItemIds startDate endDate isUnscheduledDeliveryEligible shippingOptions{__typename itemIds availableShippingOptions{__typename id shippingMethod deliveryDate price{__typename displayValue value}label{prefix suffix}isSelected isDefault}}hasMadeShippingChanges slaGroups{__typename label deliveryDate warningLabel sellerGroups{__typename id name isProSeller type shipOptionGroup{__typename deliveryPrice{__typename displayValue value}itemIds shipMethod}}}}...on AutoCareCenter{__typename defaultMode startDate endDate accBasketType collapsedItemIds itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}}");
        jsonObject2.put("buyNowContractInput", (Object)new JsonObject().put("items", (Object)new JsonArray().add((Object)new JsonObject().put("offerId", (Object)"C09DD4C3232B433C8F31FE0A94A14998").put("quantity", (Object)Double.longBitsToDouble(0x3FF0000000000000L)))));
        jsonObject2.put("promosEnable", (Object)true);
        jsonObject2.put("wplusEnabled", (Object)true);
        jsonObject.put("variables", (Object)jsonObject2);
        return jsonObject;
    }

    public static String zs2y(int n) {
        int n2;
        StringBuilder stringBuilder = new StringBuilder();
        int[] nArray = new int[n];
        for (n2 = 0; n2 < nArray.length; ++n2) {
            nArray[n2] = ThreadLocalRandom.current().nextInt(256);
        }
        while (n > 0) {
            stringBuilder.append((n2 = 0x3F & nArray[--n]) < 36 ? Integer.toString(n2, 36) : (n2 < 62 ? Integer.toString(n2 - 26, 36).toUpperCase() : (n2 < 63 ? "_" : "-")));
        }
        return stringBuilder.toString();
    }

    public HttpRequest productPageCrossSite(String string) {
        HttpRequest httpRequest = this.client.getAbs(string).as(BodyCodec.buffer());
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.pxAPI.getDeviceUA());
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "cross-site");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", this.pxAPI.getDeviceLang());
        return httpRequest;
    }

    @Override
    public JsonObject accountCreateForm() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("personName", (Object)new JsonObject().put("firstName", (Object)this.task.getProfile().getFirstName()).put("lastName", (Object)this.task.getProfile().getLastName()));
        jsonObject.put("email", (Object)(this.task.getProfile().getEmail().split("@")[0] + "+" + ThreadLocalRandom.current().nextInt(999999999) + "@" + this.task.getProfile().getEmail().split("@")[1]));
        jsonObject.put("password", (Object)Utils.generateStrongString());
        jsonObject.put("rememberme", (Object)true);
        jsonObject.put("emailNotificationAccepted", (Object)false);
        jsonObject.put("captcha", (Object)new JsonObject().put("sensorData", (Object)("2a25G2m84Vrp0o9c4230941.12-1,8,-36,-890," + this.pxAPI.getDeviceUA() + ",uaend,82457,82672914,en-GB,Gecko,3,7,0,0,500565,7072549,4549,064,9519,603,1302,003,8347,,cpen:6,i9:7,dm:3,cwen:4,non:1,opc:9,fc:2,sc:0,wrc:0,isc:8,vib:1,bat:3,x39:7,x82:2,7541,2.072809286313,234981602126,loc:-9,9,-64,-197,do_en,dm_en,t_en-5,0,-84,-415,6,8,2,2,427,830,6;7,2,1,0,586,879,6;2,-8,0,0,-1,-3,4;9,-0,7,3,-0,-7,2;1,1,7,4,1153,355,7;0,-4,0,7,7593,511,0;1,1,2,5,504,590,1;0,2,4,9,8067,193,6;6,4,1,0,472,516,6;2,-8,0,0,-1,-3,4;9,-0,7,3,-0,-7,2;1,1,7,4,678,492,1;0,7,3,1,7318,690,0;0,-1,6,6,-9,-0,7;4,-0,2,4,-2,-1,0;1,1,2,5,9508,630,0;6,-5,8,8,1026,817,6;2,3,9,8,255,146,2;2,9,7,4,1260,201,7;0,2,1,0,3856,699,7;4,0,6,7,3384,853,1;0,2,4,9,8313,193,6;6,4,1,0,480,846,6;2,3,9,8,4270,014,8;7,2,0,2,0289,3962,0;0,-1,6,6,4292,590,1;0,2,4,8,9923,193,6;6,-9,7,0,2476,146,2;2,1,7,4,2142,201,7;1,-4,0,7,8699,573,0;2,9,2,5,9669,586,0;-3,6,-01,-805,0,8,6,3,699,223,0;7,6,2,2,780,193,6;6,-9,7,0,-4,-0,2;5,-2,9,7,-2,-7,6;2,3,0,8,4390,191,8;7,-8,4,1,7339,638,0;0,3,0,3,115,210,0;2,9,3,5,9239,586,0;6,8,2,2,676,830,6;6,-9,7,0,-4,-0,2;5,-2,9,7,-2,-7,6;2,3,9,8,992,476,2;2,9,7,4,1016,201,7;0,-4,0,6,-5,-2,9;8,-2,9,2,-3,-8,0;0,3,9,3,5610,834,3;0,-3,4,9,8220,131,6;6,4,1,0,648,516,6;3,1,9,8,4407,047,8;7,2,0,2,0830,701,9;8,3,0,7,7995,573,0;2,9,2,5,9585,586,0;6,8,2,2,684,160,6;6,4,1,0,8102,712,4;8,9,0,1,2659,7573,7;0,-4,0,6,8803,210,0;2,9,2,4,0195,586,0;6,-5,8,7,2869,516,6;3,3,9,8,5389,047,8;8,-8,3,1,8435,690,0;1,1,9,3,5771,780,3;-0,4,-12,-005,3,1,8737,97,9,5,247;7,7,4639,-9,3,8,553;4,4,1892,-2,6,4,118;2,9,5997,-8,2,9,834;7,2,9275,97,9,7,247;1,7,5010,-9,3,0,553;8,4,2273,-2,6,6,118;6,9,6366,-8,2,1,834;1,1,9544,-3,9,7,247;5,9,5197,-9,3,0,553;38,9,3179,-1,2,4,744;18,4,3573,3,8,7,907;22,8,9126,6,0,0,920;39,3,4541,19,0,4,463;21,1,3282,-4,4,6,511;18,3,9136,-3,9,5,131;78,6,2128,86,1,9,085;98,1,1007,-8,6,2,990;88,2,3930,-0,7,0,852;05,9,6122,-9,3,0,447;48,9,4106,-1,2,4,638;28,4,4321,-4,1,9,524;21,5,8651,-2,0,1,783;65,3,3516,-2,6,6,002;14,1,5647,-6,8,7,891;35,9,0079,-1,7,3,817;80,9,1637,-3,9,2,249;96,0,7830,-8,2,1,728;59,1,6388,-9,0,0,920;45,3,5594,-5,0,6,463;47,3,4114,-4,4,8,511;34,2,0007,-3,9,7,131;94,6,2951,-2,1,9,085;14,0,3241,-8,6,2,699;04,4,5837,-0,7,0,551;21,8,8114,-9,3,0,146;54,8,6017,-1,2,4,337;34,6,6276,-4,1,9,223;37,4,0684,-2,0,1,482;71,2,5585,-2,6,6,701;30,3,7501,-6,8,7,590;51,7,2021,-1,7,3,516;06,1,3500,-3,9,2,948;12,9,9719,-8,2,1,427;75,1,8230,-9,0,0,629;61,3,7421,-5,0,6,162;53,3,6041,-4,4,8,210;40,2,2068,-3,9,7,830;00,5,4077,-2,1,9,784;20,2,3939,-8,6,2,699;20,3,5504,-0,7,0,551;47,7,9034,-9,3,0,146;70,0,7920,-1,2,4,337;50,5,7292,-4,1,9,223;53,3,1679,-2,0,1,482;97,4,6469,-2,6,6,701;46,1,8593,-6,8,7,590;67,9,3925,-1,7,3,516;12,0,4453,-3,9,2,948;28,9,0661,-8,2,1,427;91,0,9208,-9,0,0,629;87,5,8356,-5,0,6,162;79,2,7065,-4,4,8,210;66,1,4247,97,9,5,830;26,5,6388,-2,1,7,784;46,2,5240,-8,6,0,699;36,3,7940,-0,7,8,551;53,8,0147,13,3,0,146;86,8,8137,-1,2,4,337;66,6,8396,-4,1,9,223;79,3,2789,-2,0,1,482;13,4,7578,-2,6,6,701;62,2,9526,-6,8,7,590;83,8,4980,-1,7,3,516;38,9,5516,-3,9,2,948;44,0,1718,-8,2,1,427;07,0,0209,-9,0,0,629;93,5,9357,-5,0,6,162;85,2,8040,-4,4,8,210;72,2,4900,-3,9,7,830;42,5,6904,-2,1,9,784;62,2,5866,-8,6,2,699;52,3,7524,-0,7,0,551;79,7,1811,-9,3,0,146;02,0,9707,-1,2,4,337;82,4,9951,-4,1,9,223;85,5,3270,-2,0,1,482;29,3,8176,-2,6,6,701;78,2,0164,-6,8,7,590;99,7,5505,-1,7,3,516;54,1,6084,-3,9,2,948;60,9,2348,-8,2,1,427;23,0,1972,-9,0,0,629;19,5,0020,-5,0,6,162;01,2,9604,-4,4,8,210;98,1,72714,23,0,8,982;12,3,27131,-3,9,0,201;66,0,41220,-6,8,5,853;08,8,74683,-2,0,9,745;31,3,00152,15,2,4,690;170,2,06783,-3,9,7,193;727,5,27426,-3,9,2,201;892,2,20595,-4,1,9,586;199,7,38726,-2,1,9,047;911,3,13756,-8,2,1,780;415,7,74919,-2,0,1,745;524,0,17760,-8,6,2,952;800,1,72303,-9,0,0,982;362,9,80462,26,6,4,064;006,5,11675,34,7,0,814;-7,8,-75,-180,1,0,3704,2974,112;0,3,5352,0254,280;4,5,9257,8216,043;7,9,8102,4385,345;2,8,1924,1149,509;2,1,1548,7442,998;6,1,2241,7607,871;7,2,0896,3007,151;9,0,3820,2980,112;8,3,5585,0261,280;36,3,2012,1501,707;39,8,1167,1154,509;81,8,4580,3834,250;83,2,0016,3009,151;24,7,7923,8199,491;77,5,9690,8224,043;58,2,0507,2289,723;24,1,1865,7457,999;15,4,5056,5475,089;40,0,7845,2985,113;16,7,7037,1991,178;85,9,2134,4409,348;03,0,2284,0813,371;13,1,6266,7604,991;27,1,1441,9339,918;46,5,9854,672,621,146;66,1,5466,761,953,699;19,3,4380,956,218,830;37,7,91733,648,257,201;47,1,41131,250,463,586;78,4,52953,688,493,047;026,3,18575,945,562,-8;530,0,79769,688,065,-3;649,9,12526,643,173,-0;-1,3,-56,-398,-1,2,-93,-753,-8,2,-25,-725,-9,9,-64,-100,-5,0,-84,-413,-3,6,-01,-815,-0,4,-12,-012,170405,622230,7,3,0,6,7482873,85809,7897806955241,1595267072539,68,03630,927,292,3908,2,2,22509,245141,4,jn4o3gibsrb72myhy5q5_8868,1769,971,-2704260577,58330663-2,1,-58,-275,-1,1-0,4,-12,-60,-4676238228;7,10,46,02,72,01,18,22,33,51,54,06,08,50,05,86,9;,7;true;true;true;123;true;72;47;true;false;-9-8,2,-25,-42,0116-0,9,-04,-370,07018880-1,8,-36,-808,277510-7,4,-63,-152,;13;2;9")));
        return jsonObject;
    }

    @Override
    public HttpRequest finalizeShipping() {
        String string = WalmartNewAPI.zs2y(32);
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/orchestra/home/graphql").as(BodyCodec.buffer());
        httpRequest.headers().setAll(Headers$Pseudo.MASP.get()).set(HttpHeaders.CONTENT_LENGTH, Headers.DEFAULT).set(X_O_SEGMENT, OAOH).set(X_O_CORRELATION_ID, (CharSequence)string).set(DEVICE_PROFILE_REF, (CharSequence)this.deviceProfileId).set(X_LATENCY_TRACE, (CharSequence)"1").set(WM_MP, TRUE).set(X_O_PLATFORM_VERSION, (CharSequence)this.platformVersion).set(X_O_GQL_QUERY, (CharSequence)"mutation setShipping").set(WM_PAGE_URL, (CharSequence)this.productReferer).set(X_APOLLO_OPERATION_NAME, (CharSequence)"setShipping").set(HttpHeaders.USER_AGENT, (CharSequence)this.pxAPI.getDeviceUA()).set(X_O_PLATFORM, X_O_PLATFORM_NAME).set(HttpHeaders.CONTENT_TYPE, (CharSequence)HttpHeaderValues.APPLICATION_JSON).set(HttpHeaders.ACCEPT, (CharSequence)HttpHeaderValues.APPLICATION_JSON).set(X_ENABLE_SERVER_TIMING, (CharSequence)"1").set(X_O_CCM, SERVER).set(WM_CORRELATION_ID, (CharSequence)string).set(HttpHeaders.ORIGIN, (CharSequence)"https://www.walmart.com").set(Headers.SEC_FETCH_SITE, Headers.SAME_ORIGIN).set(Headers.SEC_FETCH_MODE, Headers.CORS).set(Headers.SEC_FETCH_DEST, Headers.EMPTY).set(HttpHeaders.REFERER, (CharSequence)this.productReferer).set(HttpHeaders.ACCEPT_ENCODING, Headers.GZIP_DEFLATE_BR).set(HttpHeaders.ACCEPT_LANGUAGE, (CharSequence)this.pxAPI.getDeviceLang()).set(HttpHeaders.COOKIE, Headers.DEFAULT);
        return httpRequest;
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public CompletableFuture handleBadResponse(HttpResponse httpResponse, String string) {
        this.pxAPI.updatePxhd(this.cookieStore().getCookieValue("_pxhd"));
        switch (httpResponse.statusCode()) {
            case 412: {
                if (this.task.getMode().contains("skip")) {
                    CompletableFuture completableFuture = this.generatePX(true);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture2 = completableFuture;
                        return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNewAPI.async$handleBadResponse(this, httpResponse, string, completableFuture2, null, null, null, null, 0, null, null, 1, arg_0));
                    }
                    completableFuture.join();
                    return CompletableFuture.completedFuture(false);
                }
                JsonObject jsonObject = httpResponse.bodyAsJsonObject();
                String string2 = jsonObject.getString("uuid");
                String string3 = jsonObject.getString("vid");
                WalmartNewAPI walmartNewAPI = this;
                CompletableFuture completableFuture = this.pxAPI.solveCaptcha(string3, string2, string);
                if (!completableFuture.isDone()) {
                    CompletableFuture completableFuture3 = completableFuture;
                    WalmartNewAPI walmartNewAPI2 = walmartNewAPI;
                    return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNewAPI.async$handleBadResponse(this, httpResponse, string, completableFuture3, jsonObject, string2, string3, walmartNewAPI2, 0, null, null, 2, arg_0));
                }
                int n = walmartNewAPI.parseMapIntoCookieJar((MultiMap)completableFuture.join());
                CompletableFuture completableFuture2 = this.generatePX(false);
                if (!completableFuture2.isDone()) {
                    CompletableFuture completableFuture5 = completableFuture2;
                    return ((CompletableFuture)completableFuture5.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNewAPI.async$handleBadResponse(this, httpResponse, string, completableFuture5, jsonObject, string2, string3, null, n, null, null, 3, arg_0));
                }
                completableFuture2.join();
                return CompletableFuture.completedFuture(n != 0);
            }
            case 307: {
                try {
                    void var6_20;
                    if (this.task.getMode().contains("skip")) {
                        CompletableFuture completableFuture = this.generatePX(true);
                        if (!completableFuture.isDone()) {
                            CompletableFuture completableFuture6 = completableFuture;
                            return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNewAPI.async$handleBadResponse(this, httpResponse, string, completableFuture6, null, null, null, null, 0, null, null, 4, arg_0));
                        }
                        completableFuture.join();
                        return CompletableFuture.completedFuture(false);
                    }
                    String string4 = "https://www.walmart.com" + httpResponse.getHeader("location");
                    String string5 = string4.split("uuid=")[1].split("&")[0];
                    Matcher matcher = VID_LOCATION_PATTERN.matcher(string4);
                    if (matcher.find()) {
                        String string6 = matcher.group(1).isBlank() ? null : matcher.group(1);
                    } else {
                        Object var6_19 = null;
                    }
                    WalmartNewAPI walmartNewAPI = this;
                    CompletableFuture completableFuture = this.pxAPI.solveCaptcha((String)var6_20, string5, string4);
                    if (!completableFuture.isDone()) {
                        CompletableFuture completableFuture7 = completableFuture;
                        WalmartNewAPI walmartNewAPI3 = walmartNewAPI;
                        return ((CompletableFuture)completableFuture7.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNewAPI.async$handleBadResponse(this, httpResponse, string, completableFuture7, null, string4, string5, walmartNewAPI3, 0, matcher, (String)var6_20, 5, arg_0));
                    }
                    boolean bl = walmartNewAPI.parseMapIntoCookieJar((MultiMap)completableFuture.join());
                    return CompletableFuture.completedFuture(bl);
                }
                catch (Throwable throwable) {
                    System.out.println("Error solving captcha [DESKTOP] " + throwable.getMessage());
                }
            }
            case 444: {
                if (!super.rotateProxy()) return CompletableFuture.completedFuture(false);
                this.pxAPI.restartClient(this.client);
                return CompletableFuture.completedFuture(false);
            }
        }
        return CompletableFuture.completedFuture(false);
    }

    public HttpRequest fetchCreditCardIds() {
        String string = WalmartNewAPI.zs2y(32);
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/orchestra/home/graphql").as(BodyCodec.buffer());
        httpRequest.headers().setAll(Headers$Pseudo.MASP.get()).set(HttpHeaders.CONTENT_LENGTH, Headers.DEFAULT).set(X_O_CORRELATION_ID, (CharSequence)string).set(DEVICE_PROFILE_REF, (CharSequence)this.deviceProfileId).set(X_LATENCY_TRACE, (CharSequence)"1").set(WM_MP, TRUE).set(X_O_PLATFORM_VERSION, (CharSequence)this.platformVersion).set(X_O_SEGMENT, OAOH).set(X_O_GQL_QUERY, (CharSequence)"query getWalletPayments").set(X_APOLLO_OPERATION_NAME, (CharSequence)"getWalletPayments").set(HttpHeaders.USER_AGENT, (CharSequence)this.pxAPI.getDeviceUA()).set(X_O_PLATFORM, X_O_PLATFORM_NAME).set(HttpHeaders.CONTENT_TYPE, (CharSequence)HttpHeaderValues.APPLICATION_JSON).set(HttpHeaders.ACCEPT, (CharSequence)HttpHeaderValues.APPLICATION_JSON).set(X_ENABLE_SERVER_TIMING, (CharSequence)"1").set(X_O_CCM, SERVER).set(WM_CORRELATION_ID, (CharSequence)string).set(HttpHeaders.ORIGIN, (CharSequence)"https://www.walmart.com").set(Headers.SEC_FETCH_SITE, Headers.SAME_ORIGIN).set(Headers.SEC_FETCH_MODE, Headers.CORS).set(Headers.SEC_FETCH_DEST, Headers.EMPTY).set(HttpHeaders.REFERER, (CharSequence)"https://www.walmart.com/wallet").set(HttpHeaders.ACCEPT_ENCODING, Headers.GZIP_DEFLATE_BR).set(HttpHeaders.ACCEPT_LANGUAGE, (CharSequence)this.pxAPI.getDeviceLang()).set(HttpHeaders.COOKIE, Headers.DEFAULT);
        return httpRequest;
    }

    public HttpRequest checkPxScore() {
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/swag/v1/ads?categoryId=5428_4089_7375787_5983914_4013061_2480235&debug=true&isDealsPage=false&isManualShelf=false&pageId=39649584&pageType=item&platform=mobile&zipCode=20147").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("pragma", "no-cache");
        httpRequest.putHeader("cache-control", "no-cache");
        httpRequest.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"95\", \"Chromium\";v=\"95\", \";Not A Brand\";v=\"99\"");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        httpRequest.putHeader("sec-ch-ua-mobile", "0?");
        httpRequest.putHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36");
        httpRequest.putHeader("correlator", "1");
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("origin", "https://www.walmart.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.walmart.com/ip/Equate-91-Isopropyl-Alcohol-Liquid-Antiseptic-32-fl-oz-Twin-Pack/979211867?athcpid=979211867&athpgid=AthenaHomepageDesktop&athcgid=null&athznid=bs&athieid=v0&athstid=CS020&athguid=SVOK3Fr5xaXlOnglHsVbfUzdxeOiTHfEZWMC&athancid=null&athena=true");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("cookie", "DEFAULT_VALUE");
        return httpRequest;
    }

    public Buffer getTendersForm() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("query", (Object)"query getTenderPlan($tenderPlanInput:TenderPlanInput!){tenderPlan(input:$tenderPlanInput){__typename tenderPlan{...TenderPlanFields}}}fragment TenderPlanFields on TenderPlan{__typename id contractId grandTotal{...PriceDetailRowFields}authorizationAmount{...PriceDetailRowFields}allocationStatus paymentGroups{...PaymentGroupFields}otcDeliveryBenefit{...PriceDetailRowFields}otherAllowedPayments{type status}addPaymentType hasAmountUnallocated weightDebitTotal{...PriceDetailRowFields}}fragment PriceDetailRowFields on PriceDetailRow{__typename key label displayValue value info{__typename title message}}fragment PaymentGroupFields on TenderPlanPaymentGroup{__typename type subTotal{__typename key label displayValue value info{__typename title message}}selectedCount allocations{...CreditCardAllocationFragment...GiftCardAllocationFragment...EbtCardAllocationFragment...DsCardAllocationFragment...PayPalAllocationFragment...AffirmAllocationFragment}statusMessage}fragment CreditCardAllocationFragment on CreditCardAllocation{__typename card{...CreditCardFragment}canEditOrDelete canDeselect isEligible isSelected allocationAmount{__typename displayValue value}capOneReward{...CapOneFields}statusMessage{__typename messageStatus messageType}paymentType}fragment CapOneFields on CapOneReward{credentialId redemptionRate redemptionUrl redemptionMethod rewardPointsBalance rewardPointsSelected rewardAmountSelected}fragment CreditCardFragment on CreditCard{__typename id isDefault cardAccountLinked needVerifyCVV cardType expiryMonth expiryYear isExpired firstName lastName lastFour isEditable phone}fragment GiftCardAllocationFragment on GiftCardAllocation{__typename card{...GiftCardFields}canEditOrDelete canDeselect isEligible isSelected allocationAmount{__typename displayValue value}statusMessage{__typename messageStatus messageType}paymentType remainingBalance{__typename displayValue value}}fragment GiftCardFields on GiftCard{__typename id balance{cardBalance}lastFour displayLabel}fragment EbtCardAllocationFragment on EbtCardAllocation{__typename card{__typename id lastFour firstName lastName}canEditOrDelete canDeselect isEligible isSelected allocationAmount{__typename displayValue value}statusMessage{__typename messageStatus messageType}paymentType ebtMaxEligibleAmount{__typename displayValue value}cardBalance{__typename displayValue value}}fragment DsCardAllocationFragment on DsCardAllocation{__typename card{...DsCardFields}canEditOrDelete canDeselect isEligible isSelected allocationAmount{__typename displayValue value}statusMessage{__typename messageStatus messageType}paymentType canApplyAmount{__typename displayValue value}remainingBalance{__typename displayValue value}paymentPromotions{__typename programName canApplyAmount{__typename displayValue value}allocationAmount{__typename displayValue value}remainingBalance{__typename displayValue value}balance{__typename displayValue value}termsLink isInvalid}otcShippingBenefit termsLink}fragment DsCardFields on DsCard{__typename id displayLabel lastFour fundingProgram balance{cardBalance}dsCardType cardName}fragment PayPalAllocationFragment on PayPalAllocation{__typename allocationAmount{__typename displayValue value}paymentHandle paymentType email}fragment AffirmAllocationFragment on AffirmAllocation{__typename allocationAmount{__typename displayValue value}paymentHandle paymentType cardType firstName lastName}");
        jsonObject.put("variables", (Object)new JsonObject().put("tenderPlanInput", (Object)new JsonObject().put("contractId", (Object)this.contractId).put("isAmendFlow", (Object)false)));
        return jsonObject.toBuffer();
    }

    @Override
    public void close() {
        if (this.pxAPI != null) {
            this.pxAPI.close();
        }
        super.close();
    }

    @Override
    public CompletableFuture generatePX(boolean bl) {
        try {
            if (bl) {
                this.pxAPI.reset();
            }
            WalmartNewAPI walmartNewAPI = this;
            CompletableFuture completableFuture = this.pxAPI.solve();
            if (completableFuture.isDone()) return CompletableFuture.completedFuture(walmartNewAPI.parseMapIntoCookieJar((MultiMap)completableFuture.join()));
            CompletableFuture completableFuture2 = completableFuture;
            WalmartNewAPI walmartNewAPI2 = walmartNewAPI;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNewAPI.async$generatePX(this, (int)(bl ? 1 : 0), walmartNewAPI2, completableFuture2, 1, arg_0));
        }
        catch (Exception exception) {
            System.out.println("Error generating desktop session: " + exception.getMessage());
            return CompletableFuture.completedFuture(false);
        }
    }

    @Override
    public Buffer getContractForm(String string) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("query", (Object)"query getPurchaseContract( $input:PurchaseContractInput! $promosEnable:Boolean! $wplusEnabled:Boolean! ){purchaseContract(input:$input){...ContractFragment}}fragment ContractFragment on PurchaseContract{id associateDiscountStatus addressMode tenderPlanId papEbtAllowed allowedPaymentGroupTypes cartCustomerContext{membershipData{isActiveMember status isActiveMember}}cartCustomerContext @include(if:$wplusEnabled){isMembershipOptedIn isEligibleForFreeTrial paymentData{hasCreditCard hasPaymentCardOnFile}membershipData{isPaidMember}}cartCustomerContext @skip(if:$wplusEnabled){paymentData{hasPaymentCardOnFile}membershipData{isPaidMember}}checkoutError{code errorData{__typename...on OutOfStock{offerId}__typename...on UnavailableOffer{offerId}__typename...on ItemExpired{offerId}__typename...on ItemQuantityAdjusted{offerId requestedQuantity adjustedQuantity}}operationalErrorCode message}checkoutableWarnings{code itemIds}allocationStatus payments{id paymentType cardType lastFour isDefault cvvRequired preferenceId paymentPreferenceId paymentHandle expiryMonth expiryYear firstName lastName email amountPaid cardImage cardImageAlt isLinkedCard capOneReward{credentialId redemptionUrl redemptionRate redemptionMethod rewardPointsBalance rewardPointsSelected rewardAmountSelected}remainingBalance{displayValue value}}order{id status orderVersion mobileNumber}terms{alcoholAccepted bagFeeAccepted smsOptInAccepted marketingEmailPrefOptIn}donationDetails{charityEIN charityName amount{displayValue value}acceptDonation}lineItems{...LineItemFields}tippingDetails{suggestedAmounts{value displayValue}maxAmount{value displayValue}selectedTippingAmount{value displayValue}}customer{id firstName lastName isGuest email phone}fulfillment{deliveryDetails{deliveryInstructions deliveryOption}pickupChoices{isSelected fulfillmentType accessType accessMode accessPointId}deliveryAddress{...AddressFields}alternatePickupPerson{...PickupPersonFields}primaryPickupPerson{...PickupPersonFields}fulfillmentItemGroups{...FulfillmentItemGroupsFields}}priceDetails{subTotal{...PriceDetailRowFields}totalItemQuantity fees{...PriceDetailRowFields}taxTotal{...PriceDetailRowFields}grandTotal{...PriceDetailRowFields}belowMinimumFee{...PriceDetailRowFields}authorizationAmount{...PriceDetailRowFields}weightDebitTotal{...PriceDetailRowFields}discounts{...PriceDetailRowFields}otcDeliveryBenefit{...PriceDetailRowFields}ebtSnapMaxEligible{...PriceDetailRowFields}ebtCashMaxEligible{...PriceDetailRowFields}hasAmountUnallocated affirm{__typename message{...AffirmMessageFields}}}checkoutGiftingDetails{isCheckoutGiftingOptin isWalmartProtectionPlanPresent isAppleCarePresent isRestrictedPaymentPresent giftMessageDetails{giftingMessage recipientEmail recipientName senderName}}promotions @include(if:$promosEnable){displayValue promoId terms}serverTime showPromotions @include(if:$promosEnable) errors{code message lineItems{...LineItemFields}}}fragment LineItemFields on LineItem{id quantity quantityString quantityLabel accessibilityQuantityLabel isPreOrder isWplusEarlyAccess isEventActive eventType fulfillmentSourcingDetails{currentSelection requestedSelection}packageQuantity priceInfo{priceDisplayCodes{showItemPrice priceDisplayCondition finalCostByWeight}itemPrice{displayValue value}linePrice{displayValue value}preDiscountedLinePrice{displayValue value}wasPrice{displayValue value}unitPrice{displayValue value}}isSubstitutionSelected isGiftEligible expiresAt showExpirationTimer selectedVariants{name value}product{...ProductFields}discounts{key label value @include(if:$promosEnable) terms subType displayValue @include(if:$promosEnable) displayLabel}wirelessPlan{planId mobileNumber __typename postPaidPlan{...postpaidPlanDetailsFragment}}selectedAddOnServices{offerId quantity groupType}registryInfo{registryId registryType}}fragment postpaidPlanDetailsFragment on PostPaidPlan{__typename espOrderSummaryId espOrderId espOrderLineId warpOrderId warpSessionId devicePayment{...postpaidPlanPriceFragment}devicePlan{__typename price{...postpaidPlanPriceFragment}frequency duration annualPercentageRate}deviceDataPlan{...deviceDataPlanFragment}}fragment deviceDataPlanFragment on DeviceDataPlan{__typename carrierName planType expiryTime activationFee{...postpaidPlanPriceFragment}planDetails{__typename price{...postpaidPlanPriceFragment}frequency name}agreements{...agreementFragment}}fragment postpaidPlanPriceFragment on PriceDetailRow{__typename key label displayValue value strikeOutDisplayValue strikeOutValue info{__typename title message}}fragment agreementFragment on CarrierAgreement{__typename name type format value docTitle label}fragment preOrderFragment on PreOrder{streetDate streetDateDisplayable streetDateType isPreOrder preOrderMessage preOrderStreetDateMessage}fragment AddressFields on Address{id addressLineOne addressLineTwo city state postalCode firstName lastName phone}fragment PickupPersonFields on PickupPerson{id firstName lastName email}fragment PriceDetailRowFields on PriceDetailRow{__typename key label displayValue value strikeOutValue strikeOutDisplayValue info{__typename title message}}fragment AccessPointFields on AccessPoint{id name assortmentStoreId displayName timeZone address{id addressLineOne addressLineTwo city state postalCode firstName lastName phone}isTest allowBagFee bagFeeValue isExpressEligible fulfillmentOption instructions nodeAccessType}fragment ReservationFields on Reservation{id expiryTime isUnscheduled expired showSlotExpiredError reservedSlot{__typename...on RegularSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata slotExpiryTime endTime available supportedTimeZone}...on DynamicExpressSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime endTime fulfillmentType slotMetadata slotExpiryTime available slaInMins sla{value displayValue}maxItemAllowed supportedTimeZone}...on UnscheduledSlot{price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata unscheduledHoldInDays supportedTimeZone}...on InHomeSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata slotExpiryTime endTime available supportedTimeZone}}}fragment AffirmMessageFields on AffirmMessage{__typename description termsUrl imageUrl monthlyPayment termLength isZeroAPR}fragment FulfillmentItemGroupsFields on FulfillmentItemGroup{...on SCGroup{__typename defaultMode collapsedItemIds itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}...on DigitalDeliveryGroup{__typename defaultMode collapsedItemIds itemGroups{__typename label itemIds}}...on Unscheduled{__typename defaultMode collapsedItemIds isSpecialEvent startDate endDate itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}...on FCGroup{__typename defaultMode collapsedItemIds startDate endDate isUnscheduledDeliveryEligible shippingOptions{__typename itemIds availableShippingOptions{__typename id shippingMethod deliveryDate price{__typename displayValue value}label{prefix suffix}isSelected isDefault}}hasMadeShippingChanges slaGroups{__typename label deliveryDate warningLabel sellerGroups{__typename id name isProSeller type shipOptionGroup{__typename deliveryPrice{__typename displayValue value}itemIds shipMethod}}}}...on AutoCareCenter{__typename defaultMode startDate endDate accBasketType collapsedItemIds itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}}fragment ProductFields on Product{id name usItemId itemType imageInfo{thumbnailUrl}category{categoryPath}offerId orderLimit orderMinLimit weightIncrement weightUnit averageWeight salesUnitType availabilityStatus isSubstitutionEligible isAlcohol configuration hasSellerBadge sellerId sellerName sellerType annualEvent preOrder{...preOrderFragment}badges{flags{__typename id key text}}addOnServices{serviceType groups{groupType services{selectedDisplayName offerId currentPrice{priceString}}}}}");
        jsonObject.put("variables", (Object)new JsonObject().put("input", (Object)new JsonObject().put("purchaseContractId", (Object)string).putNull("orderId")).put("promosEnable", (Object)true).put("wplusEnabled", (Object)true));
        return jsonObject.toBuffer();
    }

    @Override
    public JsonObject accountLoginForm(Account account) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("username", (Object)account.getUser());
        jsonObject.put("password", (Object)account.getPass());
        jsonObject.put("rememberme", (Object)true);
        jsonObject.put("captcha", (Object)new JsonObject().put("sensorData", (Object)"2a25G2m84Vrp0o9c4230971.12-1,8,-36,-890,Mozilla/9.8 (Macintosh; Intel Mac OS X 74_96_6) AppleWebKit/227.39 (KHTML, like Gecko) Chrome/91.3.4568.03 Safari/636.52,uaend,82457,82672914,en-GB,Gecko,3,7,0,0,500577,5385856,4549,952,9519,603,1302,093,8347,,cpen:6,i9:7,dm:3,cwen:4,non:1,opc:9,fc:2,sc:0,wrc:0,isc:8,vib:1,bat:3,x39:7,x82:2,7541,2.375796728065,234985759788,loc:-9,9,-64,-197,do_en,dm_en,t_en-5,0,-84,-415,6,8,2,2,427,830,6;7,2,1,0,586,879,6;2,-8,0,0,-1,-3,4;9,-0,7,3,-0,-7,2;1,1,7,4,1153,355,7;0,-4,0,7,7593,511,0;1,1,2,5,504,590,1;0,2,4,9,8067,193,6;6,4,1,0,472,516,6;2,-8,0,0,-1,-3,4;9,-0,7,3,-0,-7,2;1,1,7,4,678,492,1;0,7,3,1,7318,690,0;0,-1,6,6,-9,-0,7;4,-0,2,4,-2,-1,0;1,1,2,5,9508,630,0;6,-5,8,8,1026,817,6;2,3,9,8,255,146,2;2,9,7,4,1260,201,7;0,2,1,0,3856,699,7;4,0,6,7,3384,853,1;0,2,4,9,8313,193,6;6,4,1,0,480,846,6;2,3,9,8,4270,014,8;7,2,0,2,0289,3962,0;0,-1,6,6,4292,590,1;0,2,4,8,9923,193,6;6,-9,7,0,2476,146,2;2,1,7,4,2142,201,7;1,-4,0,7,8699,573,0;2,9,2,5,9669,586,0;-3,6,-01,-805,0,8,7,3,699,223,0;7,6,3,2,780,193,6;6,-9,7,0,-4,-0,2;5,-2,9,7,-2,-7,6;2,3,9,8,4390,191,8;7,-8,3,1,7339,638,0;0,3,9,3,115,210,0;2,9,2,5,9239,586,0;6,8,2,2,676,830,6;6,-9,7,0,-4,-0,2;5,-2,9,7,-2,-7,6;2,3,9,8,992,476,2;2,9,7,4,1016,201,7;0,-4,0,6,-5,-2,9;8,-2,9,2,-3,-8,0;0,3,9,3,5610,834,3;0,-3,4,9,8220,131,6;6,4,1,0,648,516,6;3,1,9,8,4407,047,8;7,2,0,2,0830,701,9;8,3,0,7,7995,573,0;2,9,2,5,9585,586,0;6,8,2,2,684,160,6;6,4,1,0,8102,712,4;8,9,0,1,2659,7573,7;0,-4,0,6,8803,210,0;2,9,2,4,0195,586,0;6,-5,8,7,2869,516,6;3,3,9,8,5389,047,8;8,-8,3,1,8435,690,0;1,1,9,3,5771,780,3;-0,4,-12,-005,3,1,7072,undefined,9,2,948;8,2,1134,undefined,7,3,516;8,3,2515,undefined,8,7,590;4,1,3468,undefined,6,6,701;-1,2,-93,-752,1,0,937,1145,92;2,0,953,1144,94;3,0,961,1139,96;4,0,986,1120,99;5,0,007,1190,00;6,0,011,1059,24;7,0,037,1096,521;4,1,382,0675,302;7,8,677,593,315;8,8,693,527,324;00,1,500,070,955;18,4,403,371,221;82,2,396,009,845;16,1,077,826,051;45,0,622,449,120;25,7,026,794,849;15,3,928,310,173;03,7,798,514,450;70,5,350,687,237;35,3,620,310,123;84,9,258,648,097;63,2,481,927,750;40,8,604,715,398;04,0,353,601,777;31,1,698,576,555;98,0,034,238,322,427;66,3,089,428,111,629;52,4,994,314,137,162;88,3,2648,281,697,-1;75,4,8795,472,486,-1;35,6,0607,368,402,-5;-2,1,-97,-079,-3,3,-91,-210,-7,4,-63,-130,-7,8,-75,-184,-1,8,-36,-893,-4,2,-10,-929,-8,5,-80,-533,NaN,54305,2,4,8,7,NaN,3538,7051702548101,7897814268659,39,33894,1,71,5011,6,4,0613,98345,7,gxbcdcqqvof2nukbsjdz_9007,5499,484,186058337,26425874-0,9,-04,-360,-2,9-8,5,-80,-12,-8474643896;6,31,84,86,28,82,60,36,31,66,22,91,19,30,46,43,0;,9;true;true;true;237;true;39;56;true;false;-7-7,4,-63,-83,6897-5,0,-84,-426,94852664-1,2,-93,-750,201583-2,1,-58,-290,;5;2;2"));
        return jsonObject;
    }

    @Override
    public Buffer setPaymentForm(String string) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("query", (Object)"mutation SetPayment( $payment:SetPaymentInput! $promosEnable:Boolean! $wplusEnabled:Boolean! ){setPayment(input:$payment){...ContractFragment}}fragment ContractFragment on PurchaseContract{id associateDiscountStatus addressMode tenderPlanId papEbtAllowed allowedPaymentGroupTypes cartCustomerContext{membershipData{isActiveMember status isActiveMember}}cartCustomerContext @include(if:$wplusEnabled){isMembershipOptedIn isEligibleForFreeTrial paymentData{hasCreditCard hasPaymentCardOnFile}membershipData{isPaidMember}}cartCustomerContext @skip(if:$wplusEnabled){paymentData{hasPaymentCardOnFile}membershipData{isPaidMember}}checkoutError{code errorData{__typename...on OutOfStock{offerId}__typename...on UnavailableOffer{offerId}__typename...on ItemExpired{offerId}__typename...on ItemQuantityAdjusted{offerId requestedQuantity adjustedQuantity}}operationalErrorCode message}checkoutableWarnings{code itemIds}allocationStatus payments{id paymentType cardType lastFour isDefault cvvRequired preferenceId paymentPreferenceId paymentHandle expiryMonth expiryYear firstName lastName email amountPaid cardImage cardImageAlt isLinkedCard capOneReward{credentialId redemptionUrl redemptionRate redemptionMethod rewardPointsBalance rewardPointsSelected rewardAmountSelected}remainingBalance{displayValue value}}order{id status orderVersion mobileNumber}terms{alcoholAccepted bagFeeAccepted smsOptInAccepted marketingEmailPrefOptIn}donationDetails{charityEIN charityName amount{displayValue value}acceptDonation}lineItems{...LineItemFields}tippingDetails{suggestedAmounts{value displayValue}maxAmount{value displayValue}selectedTippingAmount{value displayValue}}customer{id firstName lastName isGuest email phone}fulfillment{deliveryDetails{deliveryInstructions deliveryOption}pickupChoices{isSelected fulfillmentType accessType accessMode accessPointId}deliveryAddress{...AddressFields}alternatePickupPerson{...PickupPersonFields}primaryPickupPerson{...PickupPersonFields}fulfillmentItemGroups{...FulfillmentItemGroupsFields}}priceDetails{subTotal{...PriceDetailRowFields}totalItemQuantity fees{...PriceDetailRowFields}taxTotal{...PriceDetailRowFields}grandTotal{...PriceDetailRowFields}belowMinimumFee{...PriceDetailRowFields}authorizationAmount{...PriceDetailRowFields}weightDebitTotal{...PriceDetailRowFields}discounts{...PriceDetailRowFields}otcDeliveryBenefit{...PriceDetailRowFields}ebtSnapMaxEligible{...PriceDetailRowFields}ebtCashMaxEligible{...PriceDetailRowFields}hasAmountUnallocated affirm{__typename message{...AffirmMessageFields}}}checkoutGiftingDetails{isCheckoutGiftingOptin isWalmartProtectionPlanPresent isAppleCarePresent isRestrictedPaymentPresent giftMessageDetails{giftingMessage recipientEmail recipientName senderName}}promotions @include(if:$promosEnable){displayValue promoId terms}serverTime showPromotions @include(if:$promosEnable) errors{code message lineItems{...LineItemFields}}}fragment LineItemFields on LineItem{id quantity quantityString quantityLabel accessibilityQuantityLabel isPreOrder isWplusEarlyAccess isEventActive eventType fulfillmentSourcingDetails{currentSelection requestedSelection}packageQuantity priceInfo{priceDisplayCodes{showItemPrice priceDisplayCondition finalCostByWeight}itemPrice{displayValue value}linePrice{displayValue value}preDiscountedLinePrice{displayValue value}wasPrice{displayValue value}unitPrice{displayValue value}}isSubstitutionSelected isGiftEligible expiresAt showExpirationTimer selectedVariants{name value}product{...ProductFields}discounts{key label value @include(if:$promosEnable) terms subType displayValue @include(if:$promosEnable) displayLabel}wirelessPlan{planId mobileNumber __typename postPaidPlan{...postpaidPlanDetailsFragment}}selectedAddOnServices{offerId quantity groupType}registryInfo{registryId registryType}}fragment postpaidPlanDetailsFragment on PostPaidPlan{__typename espOrderSummaryId espOrderId espOrderLineId warpOrderId warpSessionId devicePayment{...postpaidPlanPriceFragment}devicePlan{__typename price{...postpaidPlanPriceFragment}frequency duration annualPercentageRate}deviceDataPlan{...deviceDataPlanFragment}}fragment deviceDataPlanFragment on DeviceDataPlan{__typename carrierName planType expiryTime activationFee{...postpaidPlanPriceFragment}planDetails{__typename price{...postpaidPlanPriceFragment}frequency name}agreements{...agreementFragment}}fragment postpaidPlanPriceFragment on PriceDetailRow{__typename key label displayValue value strikeOutDisplayValue strikeOutValue info{__typename title message}}fragment agreementFragment on CarrierAgreement{__typename name type format value docTitle label}fragment preOrderFragment on PreOrder{streetDate streetDateDisplayable streetDateType isPreOrder preOrderMessage preOrderStreetDateMessage}fragment AddressFields on Address{id addressLineOne addressLineTwo city state postalCode firstName lastName phone}fragment PickupPersonFields on PickupPerson{id firstName lastName email}fragment PriceDetailRowFields on PriceDetailRow{__typename key label displayValue value strikeOutValue strikeOutDisplayValue info{__typename title message}}fragment AccessPointFields on AccessPoint{id name assortmentStoreId displayName timeZone address{id addressLineOne addressLineTwo city state postalCode firstName lastName phone}isTest allowBagFee bagFeeValue isExpressEligible fulfillmentOption instructions nodeAccessType}fragment ReservationFields on Reservation{id expiryTime isUnscheduled expired showSlotExpiredError reservedSlot{__typename...on RegularSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata slotExpiryTime endTime available supportedTimeZone}...on DynamicExpressSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime endTime fulfillmentType slotMetadata slotExpiryTime available slaInMins sla{value displayValue}maxItemAllowed supportedTimeZone}...on UnscheduledSlot{price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata unscheduledHoldInDays supportedTimeZone}...on InHomeSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata slotExpiryTime endTime available supportedTimeZone}}}fragment AffirmMessageFields on AffirmMessage{__typename description termsUrl imageUrl monthlyPayment termLength isZeroAPR}fragment FulfillmentItemGroupsFields on FulfillmentItemGroup{...on SCGroup{__typename defaultMode collapsedItemIds itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}...on DigitalDeliveryGroup{__typename defaultMode collapsedItemIds itemGroups{__typename label itemIds}}...on Unscheduled{__typename defaultMode collapsedItemIds isSpecialEvent startDate endDate itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}...on FCGroup{__typename defaultMode collapsedItemIds startDate endDate isUnscheduledDeliveryEligible shippingOptions{__typename itemIds availableShippingOptions{__typename id shippingMethod deliveryDate price{__typename displayValue value}label{prefix suffix}isSelected isDefault}}hasMadeShippingChanges slaGroups{__typename label deliveryDate warningLabel sellerGroups{__typename id name isProSeller type shipOptionGroup{__typename deliveryPrice{__typename displayValue value}itemIds shipMethod}}}}...on AutoCareCenter{__typename defaultMode startDate endDate accBasketType collapsedItemIds itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}}fragment ProductFields on Product{id name usItemId itemType imageInfo{thumbnailUrl}category{categoryPath}offerId orderLimit orderMinLimit weightIncrement weightUnit averageWeight salesUnitType availabilityStatus isSubstitutionEligible isAlcohol configuration hasSellerBadge sellerId sellerName sellerType annualEvent preOrder{...preOrderFragment}badges{flags{__typename id key text}}addOnServices{serviceType groups{groupType services{selectedDisplayName offerId currentPrice{priceString}}}}}");
        jsonObject.put("variables", (Object)new JsonObject().put("payment", (Object)new JsonObject().put("contractId", (Object)string).put("preferenceId", (Object)this.paymentId).put("paymentType", (Object)"CREDITCARD")).put("promosEnable", (Object)true).put("wplusEnabled", (Object)true));
        return jsonObject.toBuffer();
    }

    @Override
    public HttpRequest buyNowSubmitPayment() {
        String string = WalmartNewAPI.zs2y(32);
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/orchestra/home/graphql").as(BodyCodec.buffer());
        httpRequest.headers().setAll(Headers$Pseudo.MASP.get()).set(HttpHeaders.CONTENT_LENGTH, Headers.DEFAULT).set(X_O_SEGMENT, OAOH).set(X_O_CORRELATION_ID, (CharSequence)string).set(DEVICE_PROFILE_REF, (CharSequence)this.deviceProfileId).set(X_LATENCY_TRACE, (CharSequence)"1").set(WM_MP, TRUE).set(X_O_PLATFORM_VERSION, (CharSequence)this.platformVersion).set(X_O_GQL_QUERY, (CharSequence)"mutation PlaceOrder").set(WM_PAGE_URL, (CharSequence)this.productReferer).set(X_APOLLO_OPERATION_NAME, (CharSequence)"PlaceOrder").set(HttpHeaders.USER_AGENT, (CharSequence)this.pxAPI.getDeviceUA()).set(X_O_PLATFORM, X_O_PLATFORM_NAME).set(HttpHeaders.CONTENT_TYPE, (CharSequence)HttpHeaderValues.APPLICATION_JSON).set(HttpHeaders.ACCEPT, (CharSequence)HttpHeaderValues.APPLICATION_JSON).set(X_ENABLE_SERVER_TIMING, (CharSequence)"1").set(X_O_CCM, SERVER).set(WM_CORRELATION_ID, (CharSequence)string).set(HttpHeaders.ORIGIN, (CharSequence)"https://www.walmart.com").set(Headers.SEC_FETCH_SITE, Headers.SAME_ORIGIN).set(Headers.SEC_FETCH_MODE, Headers.CORS).set(Headers.SEC_FETCH_DEST, Headers.EMPTY).set(HttpHeaders.REFERER, (CharSequence)this.productReferer).set(HttpHeaders.ACCEPT_ENCODING, Headers.GZIP_DEFLATE_BR).set(HttpHeaders.ACCEPT_LANGUAGE, (CharSequence)this.pxAPI.getDeviceLang()).set(HttpHeaders.COOKIE, Headers.DEFAULT);
        return httpRequest;
    }

    @Override
    public JsonObject getBillingForm(PaymentToken paymentToken) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("query", (Object)"mutation CreateCreditCard($input:AccountCreditCardInput!){createAccountCreditCard(input:$input){errors{code message}creditCard{...CreditCardFragment}}}fragment CreditCardFragment on CreditCard{__typename firstName lastName phone addressLineOne addressLineTwo city state postalCode cardType expiryYear expiryMonth lastFour id isDefault isExpired needVerifyCVV isEditable capOneProperties{shouldPromptForLink}linkedCard{availableCredit currentCreditBalance currentMinimumAmountDue minimumPaymentDueDate statementBalance statementDate rewards{rewardsBalance rewardsCurrency cashValue cashDisplayValue canRedeem}links{linkMethod linkHref linkType}}}");
        jsonObject.put("variables", (Object)new JsonObject().put("input", (Object)new JsonObject().put("firstName", (Object)this.task.getProfile().getFirstName()).put("lastName", (Object)this.task.getProfile().getLastName()).put("phone", (Object)this.task.getProfile().getPhone()).put("address", (Object)new JsonObject().put("addressLineOne", (Object)this.task.getProfile().getAddress1()).put("addressLineTwo", (Object)this.task.getProfile().getAddress2()).put("postalCode", (Object)this.task.getProfile().getZip()).put("city", (Object)this.task.getProfile().getCity()).put("state", (Object)this.task.getProfile().getState()).putNull("isApoFpo").putNull("isLoadingDockAvailable").putNull("isPoBox").putNull("businessName").putNull("addressType").putNull("sealedAddress")).put("expiryYear", (Object)Integer.parseInt(this.task.getProfile().getExpiryYear())).put("expiryMonth", (Object)Integer.parseInt(this.task.getProfile().getExpiryMonth())).put("isDefault", (Object)true).put("cardType", (Object)this.task.getProfile().getCardType().toString()).put("integrityCheck", (Object)paymentToken.getIntegrityCheck()).put("keyId", (Object)paymentToken.getKeyId()).put("phase", (Object)paymentToken.getPhase()).put("encryptedPan", (Object)paymentToken.getEncryptedPan()).put("encryptedCVV", (Object)paymentToken.getEncryptedCvv()).put("sourceFeature", (Object)"ACCOUNT_PAGE").putNull("cartId").putNull("checkoutSessionId")));
        return jsonObject;
    }

    @Override
    public HttpRequest validate() {
        String string = WalmartNewAPI.zs2y(32);
        HttpRequest httpRequest = this.client.getAbs("https://api.waiting-room.walmart.com/validateTickets").as(BodyCodec.buffer());
        httpRequest.putHeader("sec-ch-ua", this.pxAPI.getDeviceSecUA());
        httpRequest.putHeader("wm_qos.correlation_id", string);
        httpRequest.putHeader("x-o-correlation-id", string);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("device_profile_ref_id", this.deviceProfileId);
        httpRequest.putHeader("x-o-segment", OAOH.toString());
        httpRequest.putHeader("wm_mp", "true");
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("accept", "application/json");
        httpRequest.putHeader("x-o-platform-version", this.platformVersion);
        httpRequest.putHeader("x-latency-trace", "1");
        httpRequest.putHeader("x-enable-server-timing", "1");
        httpRequest.putHeader("x-o-platform", X_O_PLATFORM_NAME.toString());
        httpRequest.putHeader("user-agent", this.pxAPI.getDeviceUA());
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("origin", "https://www.walmart.com");
        httpRequest.putHeader("sec-fetch-site", "same-site");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.walmart.com/");
        httpRequest.putHeader("accept-encoding", "gzip, deflate");
        httpRequest.putHeader("accept-language", this.pxAPI.getDeviceLang());
        return httpRequest;
    }

    @Override
    public JsonObject fulfillmentBody() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("query", (Object)"mutation setFulfillment($input:SetFulfillmentInput!){setFulfillment(input:$input){...CartFragment}}fragment CartFragment on Cart{id checkoutable customer{id firstName lastName isGuest}addressMode migrationLineItems{quantity quantityLabel quantityString accessibilityQuantityLabel offerId usItemId productName thumbnailUrl addOnService priceInfo{linePrice{value displayValue}}selectedVariants{name value}}lineItems{id quantity quantityString quantityLabel isPreOrder bundleComponents{offerId quantity}registryId fulfillmentPreference selectedVariants{name value}priceInfo{priceDisplayCodes{showItemPrice priceDisplayCondition finalCostByWeight}itemPrice{...lineItemPriceInfoFragment}wasPrice{...lineItemPriceInfoFragment}unitPrice{...lineItemPriceInfoFragment}linePrice{...lineItemPriceInfoFragment}}product{name usItemId imageInfo{thumbnailUrl}itemType offerId sellerId sellerName hasSellerBadge orderLimit orderMinLimit weightUnit weightIncrement salesUnit salesUnitType sellerType isAlcohol fulfillmentType fulfillmentSpeed fulfillmentTitle classType rhPath availabilityStatus brand category{categoryPath}departmentName configuration snapEligible preOrder{isPreOrder}}registryInfo{registryId registryType}wirelessPlan{planId mobileNumber postPaidPlan{...postpaidPlanDetailsFragment}}fulfillmentSourcingDetails{currentSelection requestedSelection}}fulfillment{intent accessPoint{...accessPointFragment}reservation{...reservationFragment}storeId displayStoreSnackBarMessage homepageBookslotDetails{title subTitle expiryText}deliveryAddress{addressLineOne addressLineTwo city state postalCode firstName lastName id}fulfillmentItemGroups{...on FCGroup{__typename defaultMode collapsedItemIds startDate endDate fulfillmentSwitchInfo{fulfillmentType benefit{type price itemCount date}}shippingOptions{__typename itemIds availableShippingOptions{__typename id shippingMethod deliveryDate price{__typename displayValue value}label{prefix suffix}isSelected isDefault slaTier}}hasMadeShippingChanges slaGroups{__typename label sellerGroups{__typename id name isProSeller type catalogSellerId shipOptionGroup{__typename deliveryPrice{__typename displayValue value}itemIds shipMethod}}warningLabel}}...on SCGroup{__typename defaultMode collapsedItemIds fulfillmentSwitchInfo{fulfillmentType benefit{type price itemCount date}}itemGroups{__typename label itemIds}accessPoint{...accessPointFragment}reservation{...reservationFragment}}...on DigitalDeliveryGroup{__typename defaultMode collapsedItemIds itemGroups{__typename label itemIds}}...on Unscheduled{__typename defaultMode collapsedItemIds itemGroups{__typename label itemIds}accessPoint{...accessPointFragment}reservation{...reservationFragment}fulfillmentSwitchInfo{fulfillmentType benefit{type price itemCount date}}}...on AutoCareCenter{__typename defaultMode collapsedItemIds startDate endDate accBasketType itemGroups{__typename label itemIds}accessPoint{...accessPointFragment}reservation{...reservationFragment}fulfillmentSwitchInfo{fulfillmentType benefit{type price itemCount date}}}}suggestedSlotAvailability{isPickupAvailable isDeliveryAvailable nextPickupSlot{startTime endTime slaInMins}nextDeliverySlot{startTime endTime slaInMins}nextUnscheduledPickupSlot{startTime endTime slaInMins}nextSlot{__typename...on RegularSlot{fulfillmentOption fulfillmentType startTime}...on DynamicExpressSlot{fulfillmentOption fulfillmentType startTime slaInMins}...on UnscheduledSlot{fulfillmentOption fulfillmentType startTime unscheduledHoldInDays}}}}priceDetails{subTotal{...priceTotalFields}fees{...priceTotalFields}taxTotal{...priceTotalFields}grandTotal{...priceTotalFields}belowMinimumFee{...priceTotalFields}minimumThreshold{value displayValue}ebtSnapMaxEligible{displayValue value}}affirm{isMixedPromotionCart message{description termsUrl}nonAffirmGroup{...nonAffirmGroupFields}affirmGroups{...on AffirmItemGroup{__typename message{description termsUrl}flags{type displayLabel}name label itemCount collapsedItemIds itemIds defaultMode}}}checkoutableErrors{code shouldDisableCheckout itemIds}checkoutableWarnings{code itemIds}operationalErrors{offerId itemId requestedQuantity adjustedQuantity code upstreamErrorCode}}fragment postpaidPlanDetailsFragment on PostPaidPlan{espOrderSummaryId espOrderId espOrderLineId warpOrderId warpSessionId devicePayment{...postpaidPlanPriceFragment}devicePlan{price{...postpaidPlanPriceFragment}frequency duration annualPercentageRate}deviceDataPlan{...deviceDataPlanFragment}}fragment deviceDataPlanFragment on DeviceDataPlan{carrierName planType expiryTime activationFee{...postpaidPlanPriceFragment}planDetails{price{...postpaidPlanPriceFragment}frequency name}agreements{...agreementFragment}}fragment postpaidPlanPriceFragment on PriceDetailRow{key label displayValue value strikeOutDisplayValue strikeOutValue info{title message}}fragment agreementFragment on CarrierAgreement{name type format value docTitle label}fragment priceTotalFields on PriceDetailRow{label displayValue value key strikeOutDisplayValue strikeOutValue}fragment lineItemPriceInfoFragment on Price{displayValue value}fragment accessPointFragment on AccessPoint{id assortmentStoreId name accessType fulfillmentType fulfillmentOption displayName timeZone address{addressLineOne addressLineTwo city postalCode state phone}}fragment reservationFragment on Reservation{expiryTime isUnscheduled expired showSlotExpiredError reservedSlot{__typename...on RegularSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}nodeAccessType accessPointId fulfillmentOption startTime fulfillmentType slotMetadata endTime available supportedTimeZone isAlcoholRestricted}...on DynamicExpressSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata available slaInMins maxItemAllowed supportedTimeZone isAlcoholRestricted}...on UnscheduledSlot{price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata unscheduledHoldInDays supportedTimeZone}...on InHomeSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata endTime available supportedTimeZone isAlcoholRestricted}}}fragment nonAffirmGroupFields on NonAffirmGroup{label itemCount itemIds collapsedItemIds}");
        jsonObject.put("variables", (Object)new JsonObject().put("input", (Object)new JsonObject().put("accessPointId", (Object)this.accessPointId).put("addressId", (Object)this.addressId).put("cartId", (Object)this.cartId).putNull("registry").put("fulfillmentOption", (Object)"SHIPPING").putNull("postalCode").put("storeId", (Object)this.storeID)));
        return jsonObject;
    }

    @Override
    public JsonObject atcForm() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("query", (Object)"mutation updateItems($input:UpdateItemsInput!,$detailed:Boolean!=false){updateItems(input:$input){id checkoutable customer@include(if:$detailed){id isGuest}lineItems{id quantity quantityString quantityLabel isGiftEligible createdDateTime isPreOrder@include(if:$detailed)product{name@include(if:$detailed)usItemId imageInfo@include(if:$detailed){thumbnailUrl}itemType offerId sellerId@include(if:$detailed)sellerName@include(if:$detailed)hasSellerBadge@include(if:$detailed)orderLimit orderMinLimit}}priceDetails{subTotal{value displayValue}}checkoutableErrors{code shouldDisableCheckout itemIds}checkoutableWarnings@include(if:$detailed){code itemIds}operationalErrors{offerId itemId requestedQuantity adjustedQuantity code upstreamErrorCode}}}");
        jsonObject.put("variables", (Object)new JsonObject().put("input", (Object)new JsonObject().put("cartId", (Object)this.cartId).put("items", (Object)new JsonArray().add((Object)new JsonObject().put("offerId", (Object)this.task.getKeywords()[0]).put("quantity", (Object)Integer.parseInt(this.task.getSize()))))).put("detailed", (Object)false).put("includePartialFulfillmentSwitching", (Object)true).put("enableAEBadge", (Object)true).put("includeQueueing", (Object)true).put("includeExpressSla", (Object)true));
        return jsonObject;
    }

    @Override
    public HttpRequest getContract() {
        String string = WalmartNewAPI.zs2y(32);
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/orchestra/cartxo/graphql").as(BodyCodec.buffer());
        httpRequest.headers().setAll(Headers$Pseudo.MASP.get()).set(HttpHeaders.CONTENT_LENGTH, Headers.DEFAULT).set(Headers.SEC_CH_UA, (CharSequence)this.getPxAPI().getDeviceSecUA()).set(X_O_CORRELATION_ID, (CharSequence)string).set(DEVICE_PROFILE_REF, (CharSequence)this.deviceProfileId).set(X_LATENCY_TRACE, (CharSequence)"1").set(WM_MP, TRUE).set(X_O_PLATFORM_VERSION, (CharSequence)this.platformVersion).set(X_O_SEGMENT, OAOH).set(X_O_GQL_QUERY, (CharSequence)"query getPurchaseContract").set(X_APOLLO_OPERATION_NAME, (CharSequence)"getPurchaseContract").set(Headers.SEC_CH_UA_PLATFORM, Headers.WINDOWS).set(Headers.SEC_CH_UA_MOBILE, Headers._Q0).set(HttpHeaders.USER_AGENT, (CharSequence)this.pxAPI.getDeviceUA()).set(X_O_PLATFORM, X_O_PLATFORM_NAME).set(HttpHeaders.CONTENT_TYPE, (CharSequence)HttpHeaderValues.APPLICATION_JSON).set(HttpHeaders.ACCEPT, (CharSequence)HttpHeaderValues.APPLICATION_JSON).set(X_ENABLE_SERVER_TIMING, (CharSequence)"1").set(X_O_CCM, SERVER).set(WM_CORRELATION_ID, (CharSequence)string).set(HttpHeaders.ORIGIN, (CharSequence)"https://www.walmart.com").set(Headers.SEC_FETCH_SITE, Headers.SAME_ORIGIN).set(Headers.SEC_FETCH_MODE, Headers.CORS).set(Headers.SEC_FETCH_DEST, Headers.EMPTY).set(HttpHeaders.REFERER, (CharSequence)("https://www.walmart.com/checkout/review-order?pcid=" + this.contractId + "&buynow=1")).set(HttpHeaders.ACCEPT_ENCODING, Headers.GZIP_DEFLATE_BR).set(HttpHeaders.ACCEPT_LANGUAGE, (CharSequence)this.pxAPI.getDeviceLang()).set(HttpHeaders.COOKIE, Headers.DEFAULT);
        return httpRequest;
    }

    @Override
    public HttpRequest terraFirma(String string, boolean bl) {
        return null;
    }

    @Override
    public HttpRequest updateTender() {
        String string = WalmartNewAPI.zs2y(32);
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/orchestra/cartxo/graphql").as(BodyCodec.buffer());
        httpRequest.headers().setAll(Headers$Pseudo.MASP.get()).set(HttpHeaders.CONTENT_LENGTH, Headers.DEFAULT).set(Headers.SEC_CH_UA, (CharSequence)this.getPxAPI().getDeviceSecUA()).set(X_O_CORRELATION_ID, (CharSequence)string).set(DEVICE_PROFILE_REF, (CharSequence)this.deviceProfileId).set(X_LATENCY_TRACE, (CharSequence)"1").set(WM_MP, TRUE).set(X_O_PLATFORM_VERSION, (CharSequence)this.platformVersion).set(X_O_SEGMENT, OAOH).set(X_O_GQL_QUERY, (CharSequence)"mutation updateTenderPlan").set(X_APOLLO_OPERATION_NAME, (CharSequence)"updateTenderPlan").set(Headers.SEC_CH_UA_PLATFORM, Headers.WINDOWS).set(Headers.SEC_CH_UA_MOBILE, Headers._Q0).set(HttpHeaders.USER_AGENT, (CharSequence)this.pxAPI.getDeviceUA()).set(X_O_PLATFORM, X_O_PLATFORM_NAME).set(HttpHeaders.CONTENT_TYPE, (CharSequence)HttpHeaderValues.APPLICATION_JSON).set(HttpHeaders.ACCEPT, (CharSequence)HttpHeaderValues.APPLICATION_JSON).set(X_ENABLE_SERVER_TIMING, (CharSequence)"1").set(X_O_CCM, SERVER).set("x-o-tp-phase", this.tpPhase).set(WM_CORRELATION_ID, (CharSequence)string).set(HttpHeaders.ORIGIN, (CharSequence)"https://www.walmart.com").set(Headers.SEC_FETCH_SITE, Headers.SAME_ORIGIN).set(Headers.SEC_FETCH_MODE, Headers.CORS).set(Headers.SEC_FETCH_DEST, Headers.EMPTY).set(HttpHeaders.REFERER, (CharSequence)("https://www.walmart.com/checkout/review-order?cartId=" + this.cartId + "&wv=add_credit_debit_card")).set(HttpHeaders.ACCEPT_ENCODING, Headers.GZIP_DEFLATE_BR).set(HttpHeaders.ACCEPT_LANGUAGE, (CharSequence)this.pxAPI.getDeviceLang()).set(HttpHeaders.COOKIE, Headers.DEFAULT);
        return httpRequest;
    }

    @Override
    public JsonObject finalizeShippingForm(String string) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("query", (Object)"mutation setShipping($cartId:ID! $addressId:String!){fulfillmentMutations{setShipping(input:{cartId:$cartId addressId:$addressId}){id}}}");
        jsonObject.put("variables", (Object)new JsonObject().put("cartId", (Object)this.cartId).put("addressId", (Object)string));
        return jsonObject;
    }

    public HttpRequest deleteCard() {
        String string = WalmartNewAPI.zs2y(32);
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/orchestra/home/graphql").as(BodyCodec.buffer());
        httpRequest.headers().setAll(Headers$Pseudo.MASP.get()).set(HttpHeaders.CONTENT_LENGTH, Headers.DEFAULT).set(X_O_CORRELATION_ID, (CharSequence)string).set(DEVICE_PROFILE_REF, (CharSequence)this.deviceProfileId).set(X_O_SEGMENT, OAOH).set(WM_MP, TRUE).set(HttpHeaders.CONTENT_TYPE, (CharSequence)HttpHeaderValues.APPLICATION_JSON).set(HttpHeaders.ACCEPT, (CharSequence)HttpHeaderValues.APPLICATION_JSON).set(X_O_PLATFORM_VERSION, (CharSequence)this.platformVersion).set(X_O_GQL_QUERY, (CharSequence)"mutation deleteCard").set(X_LATENCY_TRACE, (CharSequence)"1").set(X_ENABLE_SERVER_TIMING, (CharSequence)"1").set(X_O_PLATFORM, X_O_PLATFORM_NAME).set(X_O_CCM, SERVER).set(HttpHeaders.USER_AGENT, (CharSequence)this.pxAPI.getDeviceUA()).set(X_APOLLO_OPERATION_NAME, (CharSequence)"deleteCard").set(WM_CORRELATION_ID, (CharSequence)string).set(HttpHeaders.ORIGIN, (CharSequence)"https://www.walmart.com").set(Headers.SEC_FETCH_SITE, Headers.SAME_ORIGIN).set(Headers.SEC_FETCH_MODE, Headers.CORS).set(Headers.SEC_FETCH_DEST, Headers.EMPTY).set(HttpHeaders.REFERER, (CharSequence)"https://www.walmart.com/wallet").set(HttpHeaders.ACCEPT_ENCODING, Headers.GZIP_DEFLATE_BR).set(HttpHeaders.ACCEPT_LANGUAGE, (CharSequence)this.pxAPI.getDeviceLang()).set(HttpHeaders.COOKIE, Headers.DEFAULT);
        return httpRequest;
    }

    @Override
    public HttpRequest product(String string) {
        HttpRequest httpRequest = this.client.getAbs(string).as(BodyCodec.buffer());
        httpRequest.putHeader("sec-ch-ua", this.pxAPI.getDeviceSecUA());
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.pxAPI.getDeviceUA());
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "none");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", this.pxAPI.getDeviceLang());
        return httpRequest;
    }

    @Override
    public JsonObject PCIDForm() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("query", (Object)"mutation CreateDeliveryAddress($input:AccountAddressesInput!){createAccountAddress(input:$input){...DeliveryAddressMutationResponse}}fragment DeliveryAddressMutationResponse on MutateAccountAddressResponse{...AddressMutationResponse newAddress{id accessPoint{...AccessPoint}...BaseAddressResponse}}fragment AccessPoint on AccessPointRovr{id assortmentStoreId fulfillmentType accountFulfillmentOption accountAccessType}fragment AddressMutationResponse on MutateAccountAddressResponse{errors{code}enteredAddress{...BasicAddress}suggestedAddresses{...BasicAddress sealedAddress}newAddress{id...BaseAddressResponse}allowAvsOverride}fragment BasicAddress on AccountAddressBase{addressLineOne addressLineTwo city state postalCode}fragment BaseAddressResponse on AccountAddress{...BasicAddress firstName lastName phone isDefault deliveryInstructions serviceStatus capabilities allowEditOrRemove}");
        jsonObject.put("variables", (Object)new JsonObject().put("input", (Object)new JsonObject().put("address", (Object)new JsonObject().put("addressLineOne", (Object)this.task.getProfile().getAddress1()).put("addressLineTwo", (Object)this.task.getProfile().getAddress2()).put("city", (Object)this.task.getProfile().getCity()).put("postalCode", (Object)this.task.getProfile().getZip()).put("state", (Object)this.task.getProfile().getState()).putNull("addressType").putNull("businessName").putNull("isApoFpo").putNull("isLoadingDockAvailable").putNull("isPoBox").putNull("sealedAddress")).put("firstName", (Object)this.task.getProfile().getFirstName()).put("lastName", (Object)this.task.getProfile().getLastName()).putNull("deliveryInstructions").putNull("displayLabel").put("isDefault", (Object)false).put("phone", (Object)this.task.getProfile().getPhone()).put("overrideAvs", (Object)true)));
        return jsonObject;
    }

    @Override
    public HttpRequest submitBilling() {
        String string = WalmartNewAPI.zs2y(32);
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/orchestra/cartxo/graphql").as(BodyCodec.buffer());
        httpRequest.headers().setAll(Headers$Pseudo.MASP.get()).set(HttpHeaders.CONTENT_LENGTH, Headers.DEFAULT).set(Headers.SEC_CH_UA, (CharSequence)this.getPxAPI().getDeviceSecUA()).set(X_O_CORRELATION_ID, (CharSequence)string).set(DEVICE_PROFILE_REF, (CharSequence)this.deviceProfileId).set(X_LATENCY_TRACE, (CharSequence)"1").set(WM_MP, TRUE).set(X_O_PLATFORM_VERSION, (CharSequence)this.platformVersion).set(X_O_SEGMENT, OAOH).set(X_O_GQL_QUERY, (CharSequence)"mutation CreateCreditCard").set(X_APOLLO_OPERATION_NAME, (CharSequence)"CreateCreditCard").set(Headers.SEC_CH_UA_PLATFORM, Headers.WINDOWS).set(Headers.SEC_CH_UA_MOBILE, Headers._Q0).set(HttpHeaders.USER_AGENT, (CharSequence)this.pxAPI.getDeviceUA()).set(X_O_PLATFORM, X_O_PLATFORM_NAME).set(HttpHeaders.CONTENT_TYPE, (CharSequence)HttpHeaderValues.APPLICATION_JSON).set(HttpHeaders.ACCEPT, (CharSequence)HttpHeaderValues.APPLICATION_JSON).set(X_ENABLE_SERVER_TIMING, (CharSequence)"1").set(X_O_CCM, SERVER).set(WM_CORRELATION_ID, (CharSequence)string).set(HttpHeaders.ORIGIN, (CharSequence)"https://www.walmart.com").set(Headers.SEC_FETCH_SITE, Headers.SAME_ORIGIN).set(Headers.SEC_FETCH_MODE, Headers.CORS).set(Headers.SEC_FETCH_DEST, Headers.EMPTY).set(HttpHeaders.REFERER, (CharSequence)("https://www.walmart.com/checkout/review-order?cartId=" + this.cartId + "&wv=add_credit_debit_card")).set(HttpHeaders.ACCEPT_ENCODING, Headers.GZIP_DEFLATE_BR).set(HttpHeaders.ACCEPT_LANGUAGE, (CharSequence)this.pxAPI.getDeviceLang()).set(HttpHeaders.COOKIE, Headers.DEFAULT);
        return httpRequest;
    }

    public HttpRequest blockPage() {
        HttpRequest httpRequest = this.client.getAbs("https://www.walmart.com/blocked").as(BodyCodec.buffer());
        httpRequest.putHeader("pragma", "no-cache");
        httpRequest.putHeader("cache-control", "no-cache");
        httpRequest.putHeader("sec-ch-ua", "\"Google Chrome\";v=\"95\", \"Chromium\";v=\"95\", \";Not A Brand\";v=\"99\"");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36");
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "none");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", "en-US,en;q=0.9");
        return httpRequest;
    }

    public JsonObject deleteCardForm(String string) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("query", (Object)"mutation deleteCard($id:ID!){deleteAccountPayments(id:$id){status}}");
        jsonObject.put("variables", (Object)new JsonObject().put("id", (Object)string));
        return jsonObject;
    }

    @Override
    public HttpRequest buyNow() {
        this.cookieStore().removeAnyMatch("tb-c30");
        String string = WalmartNewAPI.zs2y(32);
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/orchestra/cartxo/graphql").as(BodyCodec.buffer());
        httpRequest.headers().setAll(Headers$Pseudo.MASP.get()).set(HttpHeaders.CONTENT_LENGTH, Headers.DEFAULT).set(X_O_SEGMENT, OAOH).set(X_O_CORRELATION_ID, (CharSequence)string).set(DEVICE_PROFILE_REF, (CharSequence)this.deviceProfileId).set(X_LATENCY_TRACE, (CharSequence)"1").set(WM_MP, TRUE).set(X_O_PLATFORM_VERSION, (CharSequence)this.platformVersion).set(X_O_GQL_QUERY, (CharSequence)"mutation CreateBuyNowContract").set(WM_PAGE_URL, (CharSequence)this.referer).set(X_APOLLO_OPERATION_NAME, (CharSequence)"CreateBuyNowContract").set(HttpHeaders.USER_AGENT, (CharSequence)this.pxAPI.getDeviceUA()).set(X_O_PLATFORM, X_O_PLATFORM_NAME).set(HttpHeaders.CONTENT_TYPE, (CharSequence)HttpHeaderValues.APPLICATION_JSON).set(HttpHeaders.ACCEPT, (CharSequence)HttpHeaderValues.APPLICATION_JSON).set(X_ENABLE_SERVER_TIMING, (CharSequence)"1").set(X_O_CCM, SERVER).set(WM_CORRELATION_ID, (CharSequence)string).set(HttpHeaders.ORIGIN, (CharSequence)"https://www.walmart.com").set(Headers.SEC_FETCH_SITE, Headers.SAME_ORIGIN).set(Headers.SEC_FETCH_MODE, Headers.CORS).set(Headers.SEC_FETCH_DEST, Headers.EMPTY).set(HttpHeaders.REFERER, (CharSequence)this.referer).set(HttpHeaders.ACCEPT_ENCODING, Headers.GZIP_DEFLATE_BR).set(HttpHeaders.ACCEPT_LANGUAGE, (CharSequence)this.pxAPI.getDeviceLang()).set(HttpHeaders.COOKIE, Headers.DEFAULT);
        return httpRequest;
    }

    public HttpRequest savePayment() {
        String string = WalmartNewAPI.zs2y(32);
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/orchestra/home/graphql").as(BodyCodec.buffer());
        httpRequest.headers().setAll(Headers$Pseudo.MASP.get()).set(HttpHeaders.CONTENT_LENGTH, Headers.DEFAULT).set(X_O_SEGMENT, OAOH).set(X_O_CORRELATION_ID, (CharSequence)string).set(DEVICE_PROFILE_REF, (CharSequence)this.deviceProfileId).set(X_LATENCY_TRACE, (CharSequence)"1").set(WM_MP, TRUE).set(X_O_PLATFORM_VERSION, (CharSequence)this.platformVersion).set(X_O_GQL_QUERY, (CharSequence)"mutation AddCreditCard").set(WM_PAGE_URL, (CharSequence)"https://www.walmart.com/account/delivery-addresses").set(X_APOLLO_OPERATION_NAME, (CharSequence)"AddCreditCard").set(HttpHeaders.USER_AGENT, (CharSequence)this.pxAPI.getDeviceUA()).set(X_O_PLATFORM, X_O_PLATFORM_NAME).set(HttpHeaders.CONTENT_TYPE, (CharSequence)HttpHeaderValues.APPLICATION_JSON).set(HttpHeaders.ACCEPT, (CharSequence)HttpHeaderValues.APPLICATION_JSON).set(X_ENABLE_SERVER_TIMING, (CharSequence)"1").set(X_O_CCM, SERVER).set(WM_CORRELATION_ID, (CharSequence)string).set(HttpHeaders.ORIGIN, (CharSequence)"https://www.walmart.com").set(Headers.SEC_FETCH_SITE, Headers.SAME_ORIGIN).set(Headers.SEC_FETCH_MODE, Headers.CORS).set(Headers.SEC_FETCH_DEST, Headers.EMPTY).set(HttpHeaders.REFERER, (CharSequence)"https://www.walmart.com/account/delivery-addresses").set(HttpHeaders.ACCEPT_ENCODING, Headers.GZIP_DEFLATE_BR).set(HttpHeaders.ACCEPT_LANGUAGE, (CharSequence)this.pxAPI.getDeviceLang()).set(HttpHeaders.COOKIE, Headers.DEFAULT);
        return httpRequest;
    }

    @Override
    public HttpRequest setPayment() {
        String string = WalmartNewAPI.zs2y(32);
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/orchestra/cartxo/graphql").as(BodyCodec.buffer());
        httpRequest.headers().setAll(Headers$Pseudo.MASP.get()).set(HttpHeaders.CONTENT_LENGTH, Headers.DEFAULT).set(Headers.SEC_CH_UA, (CharSequence)this.getPxAPI().getDeviceSecUA()).set(X_O_CORRELATION_ID, (CharSequence)string).set(DEVICE_PROFILE_REF, (CharSequence)this.deviceProfileId).set(X_LATENCY_TRACE, (CharSequence)"1").set(WM_MP, TRUE).set(X_O_PLATFORM_VERSION, (CharSequence)this.platformVersion).set(X_O_SEGMENT, OAOH).set(X_O_GQL_QUERY, (CharSequence)"mutation SetPayment").set(X_APOLLO_OPERATION_NAME, (CharSequence)"SetPayment").set(Headers.SEC_CH_UA_PLATFORM, Headers.WINDOWS).set(Headers.SEC_CH_UA_MOBILE, Headers._Q0).set(HttpHeaders.USER_AGENT, (CharSequence)this.pxAPI.getDeviceUA()).set(X_O_PLATFORM, X_O_PLATFORM_NAME).set(HttpHeaders.CONTENT_TYPE, (CharSequence)HttpHeaderValues.APPLICATION_JSON).set(HttpHeaders.ACCEPT, (CharSequence)HttpHeaderValues.APPLICATION_JSON).set(X_ENABLE_SERVER_TIMING, (CharSequence)"1").set(X_O_CCM, SERVER).set(WM_CORRELATION_ID, (CharSequence)string).set(HttpHeaders.ORIGIN, (CharSequence)"https://www.walmart.com").set(Headers.SEC_FETCH_SITE, Headers.SAME_ORIGIN).set(Headers.SEC_FETCH_MODE, Headers.CORS).set(Headers.SEC_FETCH_DEST, Headers.EMPTY).set(HttpHeaders.REFERER, (CharSequence)("https://www.walmart.com/checkout/review-order?pcid=" + this.contractId + "&buynow=1")).set(HttpHeaders.ACCEPT_ENCODING, Headers.GZIP_DEFLATE_BR).set(HttpHeaders.ACCEPT_LANGUAGE, (CharSequence)this.pxAPI.getDeviceLang()).set(HttpHeaders.COOKIE, Headers.DEFAULT);
        return httpRequest;
    }

    public boolean parseMapIntoCookieJar(MultiMap multiMap) {
        if (multiMap == null) {
            return false;
        }
        for (Map.Entry entry : multiMap.entries()) {
            this.getWebClient().cookieStore().put((String)entry.getKey(), (String)entry.getValue(), ".walmart.com");
        }
        if (multiMap.isEmpty()) return false;
        return true;
    }

    public String genCorrelationId() {
        return null;
    }

    @Override
    public HttpRequest getAccountPage() {
        String string = WalmartNewAPI.zs2y(32);
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/orchestra/home/graphql").as(BodyCodec.buffer());
        httpRequest.headers().setAll(Headers$Pseudo.MASP.get()).set(HttpHeaders.CONTENT_LENGTH, Headers.DEFAULT).set(Headers.SEC_CH_UA, (CharSequence)this.pxAPI.getDeviceSecUA()).set(X_O_CORRELATION_ID, (CharSequence)string).set(DEVICE_PROFILE_REF, (CharSequence)this.deviceProfileId).set(X_LATENCY_TRACE, (CharSequence)"1").set(WM_MP, TRUE).set(X_O_PLATFORM_VERSION, (CharSequence)this.platformVersion).set(X_O_SEGMENT, OAOH).set(X_O_GQL_QUERY, (CharSequence)"query accountLandingPage").set(WM_PAGE_URL, (CharSequence)"https://www.walmart.com/").set(X_APOLLO_OPERATION_NAME, (CharSequence)"accountLandingPage").set(Headers.SEC_CH_UA_PLATFORM, Headers.WINDOWS).set(Headers.SEC_CH_UA_MOBILE, Headers._Q0).set(HttpHeaders.USER_AGENT, (CharSequence)this.pxAPI.getDeviceUA()).set(X_O_PLATFORM, X_O_PLATFORM_NAME).set(HttpHeaders.CONTENT_TYPE, (CharSequence)HttpHeaderValues.APPLICATION_JSON).set(HttpHeaders.ACCEPT, (CharSequence)HttpHeaderValues.APPLICATION_JSON).set(X_ENABLE_SERVER_TIMING, (CharSequence)"1").set(X_O_CCM, SERVER).set(WM_CORRELATION_ID, (CharSequence)string).set(HttpHeaders.ORIGIN, (CharSequence)"https://www.walmart.com").set(Headers.SEC_FETCH_SITE, Headers.SAME_ORIGIN).set(Headers.SEC_FETCH_MODE, Headers.CORS).set(Headers.SEC_FETCH_DEST, Headers.EMPTY).set(HttpHeaders.REFERER, (CharSequence)"https://www.walmart.com/wallet").set(HttpHeaders.ACCEPT_ENCODING, Headers.GZIP_DEFLATE_BR).set(HttpHeaders.ACCEPT_LANGUAGE, (CharSequence)this.pxAPI.getDeviceLang()).set(HttpHeaders.COOKIE, Headers.DEFAULT);
        return httpRequest;
    }

    @Override
    public JsonObject getCartQuery() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("query", (Object)"mutation MergeAndGetCart($input:MergeAndGetCartInput!){mergeAndGetCart(input:$input){id checkoutable customer{id isGuest}}}");
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.put("input", (Object)new JsonObject().putNull("cartId").put("strategy", (Object)"SWEEP"));
        jsonObject.put("variables", (Object)jsonObject2);
        return jsonObject;
    }

    @Override
    public Buffer getAccountPageForm() {
        return new JsonObject("{\"query\":\"query accountLandingPage{account{profile{firstName hasCCPARequest phoneNumber emailAddress isPhoneValidated isEmailValidated associateInfo{isEligibleForDiscount}accountCreatedDate{displayValue}}hasItemSubscription membership{status daysRemaining membershipType savings{money time{display accessibility timeSavedNumericValue}moneySavings{display value}}enrolledDate{displayValue}canceledDate{displayValue}freeTrialEndDate{displayValue}endDate{displayValue}plan{benefits{code}}}}}\",\"variables\":{}}").toBuffer();
    }

    @Override
    public PerimeterX getPxAPI() {
        return this.pxAPI;
    }

    @Override
    public HttpRequest getCart() {
        String string = WalmartNewAPI.zs2y(32);
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/orchestra/cartxo/graphql").as(BodyCodec.buffer());
        httpRequest.headers().setAll(Headers$Pseudo.MASP.get()).set(HttpHeaders.CONTENT_LENGTH, Headers.DEFAULT).set(Headers.SEC_CH_UA, (CharSequence)this.pxAPI.getDeviceSecUA()).set(X_O_CORRELATION_ID, (CharSequence)string).set(DEVICE_PROFILE_REF, (CharSequence)this.deviceProfileId).set(X_LATENCY_TRACE, (CharSequence)"1").set(WM_MP, TRUE).set(X_O_PLATFORM_VERSION, (CharSequence)this.platformVersion).set(X_O_SEGMENT, OAOH).set(X_O_GQL_QUERY, (CharSequence)"mutation MergeAndGetCart").set(WM_PAGE_URL, (CharSequence)"https://www.walmart.com/").set(X_APOLLO_OPERATION_NAME, (CharSequence)"MergeAndGetCart").set(Headers.SEC_CH_UA_PLATFORM, Headers.WINDOWS).set(Headers.SEC_CH_UA_MOBILE, Headers._Q0).set(HttpHeaders.USER_AGENT, (CharSequence)this.pxAPI.getDeviceUA()).set(X_O_PLATFORM, X_O_PLATFORM_NAME).set(HttpHeaders.CONTENT_TYPE, (CharSequence)HttpHeaderValues.APPLICATION_JSON).set(HttpHeaders.ACCEPT, (CharSequence)HttpHeaderValues.APPLICATION_JSON).set(X_ENABLE_SERVER_TIMING, (CharSequence)"1").set(X_O_CCM, SERVER).set(WM_CORRELATION_ID, (CharSequence)string).set(HttpHeaders.ORIGIN, (CharSequence)"https://www.walmart.com").set(Headers.SEC_FETCH_SITE, Headers.SAME_ORIGIN).set(Headers.SEC_FETCH_MODE, Headers.CORS).set(Headers.SEC_FETCH_DEST, Headers.EMPTY).set(HttpHeaders.REFERER, (CharSequence)"https://www.walmart.com/wallet").set(HttpHeaders.ACCEPT_ENCODING, Headers.GZIP_DEFLATE_BR).set(HttpHeaders.ACCEPT_LANGUAGE, (CharSequence)this.pxAPI.getDeviceLang()).set(HttpHeaders.COOKIE, Headers.DEFAULT);
        return httpRequest;
    }

    @Override
    public HttpRequest getCheckoutPage() {
        HttpRequest httpRequest = this.client.getAbs("https://www.walmart.com/checkout/review-order?cartId=" + UUID.randomUUID()).as(BodyCodec.none());
        httpRequest.putHeader("sec-ch-ua", this.pxAPI.getDeviceSecUA());
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.pxAPI.getDeviceUA());
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("service-worker-navigation-preload", "true");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("referer", "https://www.walmart.com/ip/MOOSOO-Stainless-Steel-Dishwasher-Countertop-Dishwasher-Cleaner-with-6-Large-Place-Setting-Rack/243314301");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", this.pxAPI.getDeviceLang());
        return httpRequest;
    }

    @Override
    public JsonObject tenderUpdateForm(String string) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("query", (Object)"mutation updateTenderPlan($input:UpdateTenderPlanInput!){updateTenderPlan(input:$input){__typename tenderPlan{...TenderPlanFields}}}fragment TenderPlanFields on TenderPlan{__typename id contractId grandTotal{...PriceDetailRowFields}authorizationAmount{...PriceDetailRowFields}allocationStatus paymentGroups{...PaymentGroupFields}otcDeliveryBenefit{...PriceDetailRowFields}otherAllowedPayments{type status}addPaymentType hasAmountUnallocated weightDebitTotal{...PriceDetailRowFields}}fragment PriceDetailRowFields on PriceDetailRow{__typename key label displayValue value info{__typename title message}}fragment PaymentGroupFields on TenderPlanPaymentGroup{__typename type subTotal{__typename key label displayValue value info{__typename title message}}selectedCount allocations{...CreditCardAllocationFragment...GiftCardAllocationFragment...EbtCardAllocationFragment...DsCardAllocationFragment...PayPalAllocationFragment...AffirmAllocationFragment}statusMessage}fragment CreditCardAllocationFragment on CreditCardAllocation{__typename card{...CreditCardFragment}canEditOrDelete canDeselect isEligible isSelected allocationAmount{__typename displayValue value}capOneReward{...CapOneFields}statusMessage{__typename messageStatus messageType}paymentType}fragment CapOneFields on CapOneReward{credentialId redemptionRate redemptionUrl redemptionMethod rewardPointsBalance rewardPointsSelected rewardAmountSelected}fragment CreditCardFragment on CreditCard{__typename id isDefault cardAccountLinked needVerifyCVV cardType expiryMonth expiryYear isExpired firstName lastName lastFour isEditable phone}fragment GiftCardAllocationFragment on GiftCardAllocation{__typename card{...GiftCardFields}canEditOrDelete canDeselect isEligible isSelected allocationAmount{__typename displayValue value}statusMessage{__typename messageStatus messageType}paymentType remainingBalance{__typename displayValue value}}fragment GiftCardFields on GiftCard{__typename id balance{cardBalance}lastFour displayLabel}fragment EbtCardAllocationFragment on EbtCardAllocation{__typename card{__typename id lastFour firstName lastName}canEditOrDelete canDeselect isEligible isSelected allocationAmount{__typename displayValue value}statusMessage{__typename messageStatus messageType}paymentType ebtMaxEligibleAmount{__typename displayValue value}cardBalance{__typename displayValue value}}fragment DsCardAllocationFragment on DsCardAllocation{__typename card{...DsCardFields}canEditOrDelete canDeselect isEligible isSelected allocationAmount{__typename displayValue value}statusMessage{__typename messageStatus messageType}paymentType canApplyAmount{__typename displayValue value}remainingBalance{__typename displayValue value}paymentPromotions{__typename programName canApplyAmount{__typename displayValue value}allocationAmount{__typename displayValue value}remainingBalance{__typename displayValue value}balance{__typename displayValue value}termsLink isInvalid}otcShippingBenefit termsLink}fragment DsCardFields on DsCard{__typename id displayLabel lastFour fundingProgram balance{cardBalance}dsCardType cardName}fragment PayPalAllocationFragment on PayPalAllocation{__typename allocationAmount{__typename displayValue value}paymentHandle paymentType email}fragment AffirmAllocationFragment on AffirmAllocation{__typename allocationAmount{__typename displayValue value}paymentHandle paymentType cardType firstName lastName}");
        jsonObject.put("variables", (Object)new JsonObject().put("input", (Object)new JsonObject().put("contractId", (Object)this.contractId).put("tenderPlanId", (Object)(string.isEmpty() ? null : string)).put("payments", (Object)new JsonArray().add((Object)new JsonObject().put("paymentType", (Object)"CREDITCARD").put("preferenceId", (Object)this.paymentId).putNull("amount").putNull("capOneReward").putNull("cardType").putNull("paymentHandle"))).put("accountRefresh", (Object)true).put("isAmendFlow", (Object)false)));
        return jsonObject;
    }

    public HttpRequest getTenders() {
        String string = WalmartNewAPI.zs2y(32);
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/orchestra/cartxo/graphql").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("sec-ch-ua", this.pxAPI.getDeviceSecUA());
        httpRequest.putHeader("x-o-correlation-id", string);
        httpRequest.putHeader("device_profile_ref_id", this.deviceProfileId);
        httpRequest.putHeader("x-latency-trace", "1");
        httpRequest.putHeader("wm_mp", "true");
        httpRequest.putHeader("x-o-platform-version", this.platformVersion);
        httpRequest.putHeader("x-o-segment", "oaoh");
        httpRequest.putHeader("x-o-gql-query", "query getTenderPlan");
        httpRequest.putHeader("x-apollo-operation-name", "getTenderPlan");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.pxAPI.getDeviceUA());
        httpRequest.putHeader("x-o-platform", X_O_PLATFORM_NAME.toString());
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("accept", "application/json");
        httpRequest.putHeader("x-enable-server-timing", "1");
        httpRequest.putHeader("x-o-ccm", "server");
        httpRequest.putHeader("x-o-tp-phase", this.tpPhase);
        httpRequest.putHeader("wm_qos.correlation_id", string);
        httpRequest.putHeader("origin", "https://www.walmart.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.walmart.com/checkout/review-order?cartId=" + this.cartId + "&wv=add_credit_debit_card");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", this.pxAPI.getDeviceLang());
        httpRequest.putHeader("cookie", "DEFAULT_VALUE");
        return httpRequest;
    }

    @Override
    public void setAPI(PerimeterX perimeterX) {
        this.pxAPI = perimeterX;
    }

    @Override
    public JsonObject getPaymentForm(PaymentToken paymentToken) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("query", (Object)"mutation PlaceOrder( $placeOrderInput:PlaceOrderInput! $promosEnable:Boolean! $wplusEnabled:Boolean! ){placeOrder(input:$placeOrderInput){...ContractFragment}}fragment ContractFragment on PurchaseContract{id associateDiscountStatus addressMode tenderPlanId papEbtAllowed allowedPaymentGroupTypes cartCustomerContext @include(if:$wplusEnabled){isMembershipOptedIn isEligibleForFreeTrial paymentData{hasCreditCard}membershipData{isPaidMember}}checkoutError{code errorData{__typename...on OutOfStock{offerId}__typename...on UnavailableOffer{offerId}__typename...on ItemExpired{offerId}__typename...on ItemQuantityAdjusted{offerId requestedQuantity adjustedQuantity}}operationalErrorCode message}checkoutableWarnings{code itemIds}allocationStatus payments{id paymentType cardType lastFour isDefault cvvRequired preferenceId paymentPreferenceId paymentHandle expiryMonth expiryYear firstName lastName email amountPaid cardImage cardImageAlt isLinkedCard capOneReward{credentialId redemptionUrl redemptionRate redemptionMethod rewardPointsBalance rewardPointsSelected rewardAmountSelected}remainingBalance{displayValue value}}order{id status orderVersion mobileNumber}terms{alcoholAccepted bagFeeAccepted smsOptInAccepted marketingEmailPrefOptIn}donationDetails{charityEIN charityName amount{displayValue value}acceptDonation}lineItems{...LineItemFields}tippingDetails{suggestedAmounts{value displayValue}maxAmount{value displayValue}selectedTippingAmount{value displayValue}}customer{id firstName lastName isGuest email phone}fulfillment{deliveryDetails{deliveryInstructions deliveryOption}pickupChoices{isSelected fulfillmentType accessType accessMode accessPointId}deliveryAddress{...AddressFields}alternatePickupPerson{...PickupPersonFields}primaryPickupPerson{...PickupPersonFields}fulfillmentItemGroups{...FulfillmentItemGroupsFields}}priceDetails{subTotal{...PriceDetailRowFields}totalItemQuantity fees{...PriceDetailRowFields}taxTotal{...PriceDetailRowFields}grandTotal{...PriceDetailRowFields}belowMinimumFee{...PriceDetailRowFields}authorizationAmount{...PriceDetailRowFields}weightDebitTotal{...PriceDetailRowFields}discounts{...PriceDetailRowFields}otcDeliveryBenefit{...PriceDetailRowFields}ebtSnapMaxEligible{...PriceDetailRowFields}ebtCashMaxEligible{...PriceDetailRowFields}hasAmountUnallocated affirm{__typename message{...AffirmMessageFields}}}checkoutGiftingDetails{isCheckoutGiftingOptin isWalmartProtectionPlanPresent isAppleCarePresent isRestrictedPaymentPresent giftMessageDetails{giftingMessage recipientEmail recipientName senderName}}promotions @include(if:$promosEnable){displayValue promoId terms}serverTime showPromotions @include(if:$promosEnable) errors{code message lineItems{...LineItemFields}}}fragment LineItemFields on LineItem{id quantity quantityString quantityLabel accessibilityQuantityLabel isPreOrder isWplusEarlyAccess isEventActive eventType fulfillmentSourcingDetails{currentSelection requestedSelection}packageQuantity priceInfo{priceDisplayCodes{showItemPrice priceDisplayCondition finalCostByWeight}itemPrice{displayValue value}linePrice{displayValue value}preDiscountedLinePrice{displayValue value}wasPrice{displayValue value}unitPrice{displayValue value}}isSubstitutionSelected isGiftEligible expiresAt showExpirationTimer selectedVariants{name value}product{id name usItemId itemType imageInfo{thumbnailUrl}category{categoryPath}offerId orderLimit orderMinLimit weightIncrement weightUnit averageWeight salesUnitType availabilityStatus isSubstitutionEligible isAlcohol configuration hasSellerBadge sellerId sellerName sellerType annualEvent preOrder{...preOrderFragment}badges{flags{id key text}}addOnServices{serviceType groups{groupType services{selectedDisplayName offerId currentPrice{priceString}}}}}discounts{key label value @include(if:$promosEnable) terms subType displayValue @include(if:$promosEnable) displayLabel}wirelessPlan{planId mobileNumber __typename postPaidPlan{...postpaidPlanDetailsFragment}}selectedAddOnServices{offerId quantity groupType}registryInfo{registryId registryType}}fragment postpaidPlanDetailsFragment on PostPaidPlan{__typename espOrderSummaryId espOrderId espOrderLineId warpOrderId warpSessionId devicePayment{...postpaidPlanPriceFragment}devicePlan{__typename price{...postpaidPlanPriceFragment}frequency duration annualPercentageRate}deviceDataPlan{...deviceDataPlanFragment}}fragment deviceDataPlanFragment on DeviceDataPlan{__typename carrierName planType expiryTime activationFee{...postpaidPlanPriceFragment}planDetails{__typename price{...postpaidPlanPriceFragment}frequency name}agreements{...agreementFragment}}fragment postpaidPlanPriceFragment on PriceDetailRow{__typename key label displayValue value strikeOutDisplayValue strikeOutValue info{__typename title message}}fragment agreementFragment on CarrierAgreement{__typename name type format value docTitle label}fragment preOrderFragment on PreOrder{streetDate streetDateDisplayable streetDateType isPreOrder preOrderMessage preOrderStreetDateMessage}fragment AddressFields on Address{id addressLineOne addressLineTwo city state postalCode firstName lastName phone}fragment PickupPersonFields on PickupPerson{id firstName lastName email}fragment PriceDetailRowFields on PriceDetailRow{__typename key label displayValue value strikeOutValue strikeOutDisplayValue info{__typename title message}}fragment AccessPointFields on AccessPoint{id name assortmentStoreId displayName timeZone address{id addressLineOne addressLineTwo city state postalCode firstName lastName phone}isTest allowBagFee bagFeeValue isExpressEligible fulfillmentOption instructions nodeAccessType}fragment ReservationFields on Reservation{id expiryTime isUnscheduled expired showSlotExpiredError reservedSlot{__typename...on RegularSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata slotExpiryTime endTime available supportedTimeZone}...on DynamicExpressSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime endTime fulfillmentType slotMetadata slotExpiryTime available slaInMins maxItemAllowed supportedTimeZone}...on UnscheduledSlot{price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata unscheduledHoldInDays supportedTimeZone}...on InHomeSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata slotExpiryTime endTime available supportedTimeZone}}}fragment AffirmMessageFields on AffirmMessage{__typename description termsUrl imageUrl monthlyPayment termLength isZeroAPR}fragment FulfillmentItemGroupsFields on FulfillmentItemGroup{...on SCGroup{__typename defaultMode collapsedItemIds itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}...on DigitalDeliveryGroup{__typename defaultMode collapsedItemIds itemGroups{__typename label itemIds}}...on Unscheduled{__typename defaultMode collapsedItemIds isSpecialEvent itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}...on FCGroup{__typename defaultMode collapsedItemIds startDate endDate isUnscheduledDeliveryEligible shippingOptions{__typename itemIds availableShippingOptions{__typename id shippingMethod deliveryDate price{__typename displayValue value}label{prefix suffix}isSelected isDefault}}hasMadeShippingChanges slaGroups{__typename label deliveryDate warningLabel sellerGroups{__typename id name isProSeller type shipOptionGroup{__typename deliveryPrice{__typename displayValue value}itemIds shipMethod}}}}...on AutoCareCenter{__typename defaultMode startDate endDate accBasketType collapsedItemIds itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}}");
        jsonObject.put("variables", (Object)new JsonObject().put("placeOrderInput", (Object)new JsonObject().put("contractId", (Object)this.contractId).put("substitutions", (Object)new JsonArray()).putNull("acceptBagFee").putNull("acceptAlcoholDisclosure").put("acceptSMSOptInDisclosure", (Object)false).put("marketingEmailPref", (Object)false).put("deliveryDetails", (Object)new JsonObject().putNull("deliveryInstructions").put("deliveryOption", (Object)"LEAVE_AT_DOOR")).put("mobileNumber", (Object)this.task.getProfile().getPhone()).putNull("paymentCvvInfos").putNull("paymentHandle").put("acceptDonation", (Object)false).put("emailAddress", (Object)this.task.getProfile().getEmail()).putNull("fulfillmentOptions").put("acceptedAgreements", (Object)new JsonArray())).put("promosEnable", (Object)true).put("wplusEnabled", (Object)true));
        return jsonObject;
    }

    @Override
    public HttpRequest queuePage(String string) {
        HttpRequest httpRequest = this.client.getAbs(string).as(BodyCodec.buffer());
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.pxAPI.getDeviceUA());
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.putHeader("sec-fetch-site", "none");
        httpRequest.putHeader("sec-fetch-mode", "navigate");
        httpRequest.putHeader("sec-fetch-user", "?1");
        httpRequest.putHeader("sec-fetch-dest", "document");
        httpRequest.putHeader("sec-ch-ua", this.pxAPI.getDeviceSecUA());
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", this.pxAPI.getDeviceLang());
        return httpRequest;
    }

    public static CompletableFuture async$generatePX(WalmartNewAPI walmartNewAPI, int n, WalmartNewAPI walmartNewAPI2, CompletableFuture object, int n2, Object object2) {
        switch (n2) {
            case 0: {
                CompletableFuture completableFuture;
                WalmartNewAPI walmartNewAPI3;
                try {
                    if (n != 0) {
                        walmartNewAPI.pxAPI.reset();
                    }
                    walmartNewAPI3 = walmartNewAPI;
                    CompletableFuture completableFuture2 = walmartNewAPI.pxAPI.solve();
                    completableFuture = completableFuture2;
                    if (completableFuture2.isDone()) return CompletableFuture.completedFuture(walmartNewAPI3.parseMapIntoCookieJar((MultiMap)completableFuture.join()));
                    CompletableFuture completableFuture3 = completableFuture;
                    object = walmartNewAPI3;
                    return ((CompletableFuture)completableFuture3.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNewAPI.async$generatePX(walmartNewAPI, n, (WalmartNewAPI)object, completableFuture3, 1, arg_0));
                }
                catch (Exception exception) {
                    System.out.println("Error generating desktop session: " + exception.getMessage());
                    return CompletableFuture.completedFuture(false);
                }
            }
            case 1: {
                WalmartNewAPI walmartNewAPI3 = walmartNewAPI2;
                CompletableFuture completableFuture = object;
                return CompletableFuture.completedFuture(walmartNewAPI3.parseMapIntoCookieJar((MultiMap)completableFuture.join()));
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public HttpRequest saveAddress() {
        String string = WalmartNewAPI.zs2y(32);
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/orchestra/home/graphql").as(BodyCodec.buffer());
        httpRequest.headers().setAll(Headers$Pseudo.MASP.get()).set(HttpHeaders.CONTENT_LENGTH, Headers.DEFAULT).set(X_O_SEGMENT, OAOH).set(X_O_CORRELATION_ID, (CharSequence)string).set(DEVICE_PROFILE_REF, (CharSequence)this.deviceProfileId).set(X_LATENCY_TRACE, (CharSequence)"1").set(WM_MP, TRUE).set(X_O_PLATFORM_VERSION, (CharSequence)this.platformVersion).set(X_O_GQL_QUERY, (CharSequence)"mutation CreateDeliveryAddress").set(WM_PAGE_URL, (CharSequence)"https://www.walmart.com/account/delivery-addresses").set(X_APOLLO_OPERATION_NAME, (CharSequence)"CreateDeliveryAddress").set(HttpHeaders.USER_AGENT, (CharSequence)this.pxAPI.getDeviceUA()).set(X_O_PLATFORM, X_O_PLATFORM_NAME).set(HttpHeaders.CONTENT_TYPE, (CharSequence)HttpHeaderValues.APPLICATION_JSON).set(HttpHeaders.ACCEPT, (CharSequence)HttpHeaderValues.APPLICATION_JSON).set(X_ENABLE_SERVER_TIMING, (CharSequence)"1").set(X_O_CCM, SERVER).set(WM_CORRELATION_ID, (CharSequence)string).set(HttpHeaders.ORIGIN, (CharSequence)"https://www.walmart.com").set(Headers.SEC_FETCH_SITE, Headers.SAME_ORIGIN).set(Headers.SEC_FETCH_MODE, Headers.CORS).set(Headers.SEC_FETCH_DEST, Headers.EMPTY).set(HttpHeaders.REFERER, (CharSequence)"https://www.walmart.com/account/delivery-addresses").set(HttpHeaders.ACCEPT_ENCODING, Headers.GZIP_DEFLATE_BR).set(HttpHeaders.ACCEPT_LANGUAGE, (CharSequence)this.pxAPI.getDeviceLang()).set(HttpHeaders.COOKIE, Headers.DEFAULT);
        return httpRequest;
    }

    @Override
    public JsonObject buyNowSubmitPaymentBody(String string) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("query", (Object)"mutation PlaceOrder( $placeOrderInput:PlaceOrderInput! $promosEnable:Boolean! $wplusEnabled:Boolean! ){placeOrder(input:$placeOrderInput){...ContractFragment}}fragment ContractFragment on PurchaseContract{id associateDiscountStatus addressMode tenderPlanId papEbtAllowed allowedPaymentGroupTypes cartCustomerContext @include(if:$wplusEnabled){isMembershipOptedIn isEligibleForFreeTrial paymentData{hasCreditCard}membershipData{isPaidMember}}checkoutError{code errorData{__typename...on OutOfStock{offerId}__typename...on UnavailableOffer{offerId}__typename...on ItemExpired{offerId}__typename...on ItemQuantityAdjusted{offerId requestedQuantity adjustedQuantity}}operationalErrorCode message}checkoutableWarnings{code itemIds}allocationStatus payments{id paymentType cardType lastFour isDefault cvvRequired preferenceId paymentPreferenceId paymentHandle expiryMonth expiryYear firstName lastName email amountPaid cardImage cardImageAlt isLinkedCard capOneReward{credentialId redemptionUrl redemptionRate redemptionMethod rewardPointsBalance rewardPointsSelected rewardAmountSelected}remainingBalance{displayValue value}}order{id status orderVersion mobileNumber}terms{alcoholAccepted bagFeeAccepted smsOptInAccepted marketingEmailPrefOptIn}donationDetails{charityEIN charityName amount{displayValue value}acceptDonation}lineItems{...LineItemFields}tippingDetails{suggestedAmounts{value displayValue}maxAmount{value displayValue}selectedTippingAmount{value displayValue}}customer{id firstName lastName isGuest email phone}fulfillment{deliveryDetails{deliveryInstructions deliveryOption}pickupChoices{isSelected fulfillmentType accessType accessMode accessPointId}deliveryAddress{...AddressFields}alternatePickupPerson{...PickupPersonFields}primaryPickupPerson{...PickupPersonFields}fulfillmentItemGroups{...FulfillmentItemGroupsFields}}priceDetails{subTotal{...PriceDetailRowFields}totalItemQuantity fees{...PriceDetailRowFields}taxTotal{...PriceDetailRowFields}grandTotal{...PriceDetailRowFields}belowMinimumFee{...PriceDetailRowFields}authorizationAmount{...PriceDetailRowFields}weightDebitTotal{...PriceDetailRowFields}discounts{...PriceDetailRowFields}otcDeliveryBenefit{...PriceDetailRowFields}ebtSnapMaxEligible{...PriceDetailRowFields}ebtCashMaxEligible{...PriceDetailRowFields}hasAmountUnallocated affirm{__typename message{...AffirmMessageFields}}}checkoutGiftingDetails{isCheckoutGiftingOptin isWalmartProtectionPlanPresent isAppleCarePresent isRestrictedPaymentPresent giftMessageDetails{giftingMessage recipientEmail recipientName senderName}}promotions @include(if:$promosEnable){displayValue promoId terms}serverTime showPromotions @include(if:$promosEnable) errors{code message lineItems{...LineItemFields}}}fragment LineItemFields on LineItem{id quantity quantityString quantityLabel accessibilityQuantityLabel isPreOrder isWplusEarlyAccess isEventActive eventType fulfillmentSourcingDetails{currentSelection requestedSelection}packageQuantity priceInfo{priceDisplayCodes{showItemPrice priceDisplayCondition finalCostByWeight}itemPrice{displayValue value}linePrice{displayValue value}preDiscountedLinePrice{displayValue value}wasPrice{displayValue value}unitPrice{displayValue value}}isSubstitutionSelected isGiftEligible expiresAt showExpirationTimer selectedVariants{name value}product{id name usItemId itemType imageInfo{thumbnailUrl}category{categoryPath}offerId orderLimit orderMinLimit weightIncrement weightUnit averageWeight salesUnitType availabilityStatus isSubstitutionEligible isAlcohol configuration hasSellerBadge sellerId sellerName sellerType annualEvent preOrder{...preOrderFragment}badges{flags{id key text}}addOnServices{serviceType groups{groupType services{selectedDisplayName offerId currentPrice{priceString}}}}}discounts{key label value @include(if:$promosEnable) terms subType displayValue @include(if:$promosEnable) displayLabel}wirelessPlan{planId mobileNumber __typename postPaidPlan{...postpaidPlanDetailsFragment}}selectedAddOnServices{offerId quantity groupType}registryInfo{registryId registryType}}fragment postpaidPlanDetailsFragment on PostPaidPlan{__typename espOrderSummaryId espOrderId espOrderLineId warpOrderId warpSessionId devicePayment{...postpaidPlanPriceFragment}devicePlan{__typename price{...postpaidPlanPriceFragment}frequency duration annualPercentageRate}deviceDataPlan{...deviceDataPlanFragment}}fragment deviceDataPlanFragment on DeviceDataPlan{__typename carrierName planType expiryTime activationFee{...postpaidPlanPriceFragment}planDetails{__typename price{...postpaidPlanPriceFragment}frequency name}agreements{...agreementFragment}}fragment postpaidPlanPriceFragment on PriceDetailRow{__typename key label displayValue value strikeOutDisplayValue strikeOutValue info{__typename title message}}fragment agreementFragment on CarrierAgreement{__typename name type format value docTitle label}fragment preOrderFragment on PreOrder{streetDate streetDateDisplayable streetDateType isPreOrder preOrderMessage preOrderStreetDateMessage}fragment AddressFields on Address{id addressLineOne addressLineTwo city state postalCode firstName lastName phone}fragment PickupPersonFields on PickupPerson{id firstName lastName email}fragment PriceDetailRowFields on PriceDetailRow{__typename key label displayValue value strikeOutValue strikeOutDisplayValue info{__typename title message}}fragment AccessPointFields on AccessPoint{id name assortmentStoreId displayName timeZone address{id addressLineOne addressLineTwo city state postalCode firstName lastName phone}isTest allowBagFee bagFeeValue isExpressEligible fulfillmentOption instructions nodeAccessType}fragment ReservationFields on Reservation{id expiryTime isUnscheduled expired showSlotExpiredError reservedSlot{__typename...on RegularSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata slotExpiryTime endTime available supportedTimeZone}...on DynamicExpressSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime endTime fulfillmentType slotMetadata slotExpiryTime available slaInMins maxItemAllowed supportedTimeZone}...on UnscheduledSlot{price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata unscheduledHoldInDays supportedTimeZone}...on InHomeSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata slotExpiryTime endTime available supportedTimeZone}}}fragment AffirmMessageFields on AffirmMessage{__typename description termsUrl imageUrl monthlyPayment termLength isZeroAPR}fragment FulfillmentItemGroupsFields on FulfillmentItemGroup{...on SCGroup{__typename defaultMode collapsedItemIds itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}...on DigitalDeliveryGroup{__typename defaultMode collapsedItemIds itemGroups{__typename label itemIds}}...on Unscheduled{__typename defaultMode collapsedItemIds isSpecialEvent itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}...on FCGroup{__typename defaultMode collapsedItemIds startDate endDate isUnscheduledDeliveryEligible shippingOptions{__typename itemIds availableShippingOptions{__typename id shippingMethod deliveryDate price{__typename displayValue value}label{prefix suffix}isSelected isDefault}}hasMadeShippingChanges slaGroups{__typename label deliveryDate warningLabel sellerGroups{__typename id name isProSeller type shipOptionGroup{__typename deliveryPrice{__typename displayValue value}itemIds shipMethod}}}}...on AutoCareCenter{__typename defaultMode startDate endDate accBasketType collapsedItemIds itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}}");
        jsonObject.put("variables", (Object)new JsonObject().put("placeOrderInput", (Object)new JsonObject().put("contractId", (Object)string).put("substitutions", (Object)new JsonArray()).putNull("acceptBagFee").putNull("acceptAlcoholDisclosure").putNull("acceptSMSOptInDisclosure").putNull("marketingEmailPref").putNull("deliveryDetails").put("mobileNumber", (Object)this.task.getProfile().getPhone()).putNull("paymentCvvInfos").putNull("paymentHandle").put("emailAddress", (Object)this.task.getProfile().getEmail()).putNull("fulfillmentOptions").put("acceptedAgreements", (Object)new JsonArray())).put("promosEnable", (Object)true).put("wplusEnabled", (Object)true));
        return jsonObject;
    }

    @Override
    public HttpRequest affilCrossSite(String string) {
        return null;
    }

    @Override
    public JsonObject setAddressIdForm(String string, String string2) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("query", (Object)"mutation setShippingAddress( $input:setShippingAddressInput! $promosEnable:Boolean! $wplusEnabled:Boolean! ){setShippingAddress(input:$input){...ContractFragment}}fragment ContractFragment on PurchaseContract{id associateDiscountStatus addressMode tenderPlanId papEbtAllowed allowedPaymentGroupTypes cartCustomerContext{membershipData{isActiveMember}}cartCustomerContext @include(if:$wplusEnabled){isMembershipOptedIn isEligibleForFreeTrial paymentData{hasCreditCard hasPaymentCardOnFile}membershipData{isPaidMember}}cartCustomerContext @skip(if:$wplusEnabled){paymentData{hasPaymentCardOnFile}}checkoutError{code errorData{__typename...on OutOfStock{offerId}__typename...on UnavailableOffer{offerId}__typename...on ItemExpired{offerId}__typename...on ItemQuantityAdjusted{offerId requestedQuantity adjustedQuantity}}operationalErrorCode message}checkoutableWarnings{code itemIds}allocationStatus payments{id paymentType cardType lastFour isDefault cvvRequired preferenceId paymentPreferenceId paymentHandle expiryMonth expiryYear firstName lastName email amountPaid cardImage cardImageAlt isLinkedCard capOneReward{credentialId redemptionUrl redemptionRate redemptionMethod rewardPointsBalance rewardPointsSelected rewardAmountSelected}remainingBalance{displayValue value}}order{id status orderVersion mobileNumber}terms{alcoholAccepted bagFeeAccepted smsOptInAccepted marketingEmailPrefOptIn}donationDetails{charityEIN charityName amount{displayValue value}acceptDonation}lineItems{...LineItemFields}tippingDetails{suggestedAmounts{value displayValue}maxAmount{value displayValue}selectedTippingAmount{value displayValue}}customer{id firstName lastName isGuest email phone}fulfillment{deliveryDetails{deliveryInstructions deliveryOption}pickupChoices{isSelected fulfillmentType accessType accessMode accessPointId}deliveryAddress{...AddressFields}alternatePickupPerson{...PickupPersonFields}primaryPickupPerson{...PickupPersonFields}fulfillmentItemGroups{...FulfillmentItemGroupsFields}}priceDetails{subTotal{...PriceDetailRowFields}totalItemQuantity fees{...PriceDetailRowFields}taxTotal{...PriceDetailRowFields}grandTotal{...PriceDetailRowFields}belowMinimumFee{...PriceDetailRowFields}authorizationAmount{...PriceDetailRowFields}weightDebitTotal{...PriceDetailRowFields}discounts{...PriceDetailRowFields}otcDeliveryBenefit{...PriceDetailRowFields}ebtSnapMaxEligible{...PriceDetailRowFields}ebtCashMaxEligible{...PriceDetailRowFields}hasAmountUnallocated affirm{__typename message{...AffirmMessageFields}}}checkoutGiftingDetails{isCheckoutGiftingOptin isWalmartProtectionPlanPresent isAppleCarePresent isRestrictedPaymentPresent giftMessageDetails{giftingMessage recipientEmail recipientName senderName}}promotions @include(if:$promosEnable){displayValue promoId terms}serverTime showPromotions @include(if:$promosEnable) errors{code message lineItems{...LineItemFields}}}fragment LineItemFields on LineItem{id quantity quantityString quantityLabel accessibilityQuantityLabel isPreOrder isWplusEarlyAccess isEventActive eventType fulfillmentSourcingDetails{currentSelection requestedSelection}packageQuantity priceInfo{priceDisplayCodes{showItemPrice priceDisplayCondition finalCostByWeight}itemPrice{displayValue value}linePrice{displayValue value}preDiscountedLinePrice{displayValue value}wasPrice{displayValue value}unitPrice{displayValue value}}isSubstitutionSelected isGiftEligible expiresAt showExpirationTimer selectedVariants{name value}product{id name usItemId itemType imageInfo{thumbnailUrl}category{categoryPath}offerId orderLimit orderMinLimit weightIncrement weightUnit averageWeight salesUnitType availabilityStatus isSubstitutionEligible isAlcohol configuration hasSellerBadge sellerId sellerName sellerType annualEvent preOrder{...preOrderFragment}badges{flags{__typename id key text}}addOnServices{serviceType groups{groupType services{selectedDisplayName offerId currentPrice{priceString}}}}}discounts{key label value @include(if:$promosEnable) terms subType displayValue @include(if:$promosEnable) displayLabel}wirelessPlan{planId mobileNumber __typename postPaidPlan{...postpaidPlanDetailsFragment}}selectedAddOnServices{offerId quantity groupType}registryInfo{registryId registryType}}fragment postpaidPlanDetailsFragment on PostPaidPlan{__typename espOrderSummaryId espOrderId espOrderLineId warpOrderId warpSessionId devicePayment{...postpaidPlanPriceFragment}devicePlan{__typename price{...postpaidPlanPriceFragment}frequency duration annualPercentageRate}deviceDataPlan{...deviceDataPlanFragment}}fragment deviceDataPlanFragment on DeviceDataPlan{__typename carrierName planType expiryTime activationFee{...postpaidPlanPriceFragment}planDetails{__typename price{...postpaidPlanPriceFragment}frequency name}agreements{...agreementFragment}}fragment postpaidPlanPriceFragment on PriceDetailRow{__typename key label displayValue value strikeOutDisplayValue strikeOutValue info{__typename title message}}fragment agreementFragment on CarrierAgreement{__typename name type format value docTitle label}fragment preOrderFragment on PreOrder{streetDate streetDateDisplayable streetDateType isPreOrder preOrderMessage preOrderStreetDateMessage}fragment AddressFields on Address{id addressLineOne addressLineTwo city state postalCode firstName lastName phone}fragment PickupPersonFields on PickupPerson{id firstName lastName email}fragment PriceDetailRowFields on PriceDetailRow{__typename key label displayValue value strikeOutValue strikeOutDisplayValue info{__typename title message}}fragment AccessPointFields on AccessPoint{id name assortmentStoreId displayName timeZone address{id addressLineOne addressLineTwo city state postalCode firstName lastName phone}isTest allowBagFee bagFeeValue isExpressEligible fulfillmentOption instructions nodeAccessType}fragment ReservationFields on Reservation{id expiryTime isUnscheduled expired showSlotExpiredError reservedSlot{__typename...on RegularSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata slotExpiryTime endTime available supportedTimeZone}...on DynamicExpressSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime endTime fulfillmentType slotMetadata slotExpiryTime available slaInMins sla{value displayValue}maxItemAllowed supportedTimeZone}...on UnscheduledSlot{price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata unscheduledHoldInDays supportedTimeZone}...on InHomeSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata slotExpiryTime endTime available supportedTimeZone}}}fragment AffirmMessageFields on AffirmMessage{__typename description termsUrl imageUrl monthlyPayment termLength isZeroAPR}fragment FulfillmentItemGroupsFields on FulfillmentItemGroup{...on SCGroup{__typename defaultMode collapsedItemIds itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}...on DigitalDeliveryGroup{__typename defaultMode collapsedItemIds itemGroups{__typename label itemIds}}...on Unscheduled{__typename defaultMode collapsedItemIds isSpecialEvent startDate endDate itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}...on FCGroup{__typename defaultMode collapsedItemIds startDate endDate isUnscheduledDeliveryEligible shippingOptions{__typename itemIds availableShippingOptions{__typename id shippingMethod deliveryDate price{__typename displayValue value}label{prefix suffix}isSelected isDefault}}hasMadeShippingChanges slaGroups{__typename label deliveryDate warningLabel sellerGroups{__typename id name isProSeller type shipOptionGroup{__typename deliveryPrice{__typename displayValue value}itemIds shipMethod}}}}...on AutoCareCenter{__typename defaultMode startDate endDate accBasketType collapsedItemIds itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}}");
        jsonObject.put("variables", (Object)new JsonObject().put("input", (Object)new JsonObject().put("contractId", (Object)string).put("addressId", (Object)string2)).put("promosEnable", (Object)true).put("wplusEnabled", (Object)true));
        return jsonObject;
    }

    @Override
    public HttpRequest getDeliveryAddresses() {
        String string = WalmartNewAPI.zs2y(32);
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/orchestra/home/graphql").as(BodyCodec.buffer());
        httpRequest.headers().setAll(Headers$Pseudo.MASP.get()).set(HttpHeaders.CONTENT_LENGTH, Headers.DEFAULT).set(X_O_SEGMENT, OAOH).set(X_O_CORRELATION_ID, (CharSequence)string).set(DEVICE_PROFILE_REF, (CharSequence)this.deviceProfileId).set(X_LATENCY_TRACE, (CharSequence)"1").set(WM_MP, TRUE).set(X_O_PLATFORM_VERSION, (CharSequence)this.platformVersion).set(X_O_GQL_QUERY, (CharSequence)"query GetDeliveryAddresses").set(WM_PAGE_URL, (CharSequence)this.productReferer).set(X_APOLLO_OPERATION_NAME, (CharSequence)"GetDeliveryAddresses").set(HttpHeaders.USER_AGENT, (CharSequence)this.pxAPI.getDeviceUA()).set(X_O_PLATFORM, X_O_PLATFORM_NAME).set(HttpHeaders.CONTENT_TYPE, (CharSequence)HttpHeaderValues.APPLICATION_JSON).set(HttpHeaders.ACCEPT, (CharSequence)HttpHeaderValues.APPLICATION_JSON).set(X_ENABLE_SERVER_TIMING, (CharSequence)"1").set(X_O_CCM, SERVER).set(WM_CORRELATION_ID, (CharSequence)string).set(HttpHeaders.ORIGIN, (CharSequence)"https://www.walmart.com").set(Headers.SEC_FETCH_SITE, Headers.SAME_ORIGIN).set(Headers.SEC_FETCH_MODE, Headers.CORS).set(Headers.SEC_FETCH_DEST, Headers.EMPTY).set(HttpHeaders.REFERER, (CharSequence)this.productReferer).set(HttpHeaders.ACCEPT_ENCODING, Headers.GZIP_DEFLATE_BR).set(HttpHeaders.ACCEPT_LANGUAGE, (CharSequence)this.pxAPI.getDeviceLang()).set(HttpHeaders.COOKIE, Headers.DEFAULT);
        return httpRequest;
    }

    @Override
    public HttpRequest createContract() {
        String string = WalmartNewAPI.zs2y(32);
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/orchestra/cartxo/graphql").as(BodyCodec.buffer());
        httpRequest.headers().setAll(Headers$Pseudo.MASP.get()).set(HttpHeaders.CONTENT_LENGTH, Headers.DEFAULT).set(Headers.SEC_CH_UA, (CharSequence)this.getPxAPI().getDeviceSecUA()).set(X_O_CORRELATION_ID, (CharSequence)string).set(DEVICE_PROFILE_REF, (CharSequence)this.deviceProfileId).set(X_LATENCY_TRACE, (CharSequence)"1").set(WM_MP, TRUE).set(X_O_PLATFORM_VERSION, (CharSequence)this.platformVersion).set(X_O_SEGMENT, OAOH).set(X_O_GQL_QUERY, (CharSequence)"mutation CreateContract").set(X_APOLLO_OPERATION_NAME, (CharSequence)"CreateContract").set(Headers.SEC_CH_UA_PLATFORM, Headers.WINDOWS).set(Headers.SEC_CH_UA_MOBILE, Headers._Q0).set(HttpHeaders.USER_AGENT, (CharSequence)this.pxAPI.getDeviceUA()).set(X_O_PLATFORM, X_O_PLATFORM_NAME).set(HttpHeaders.CONTENT_TYPE, (CharSequence)HttpHeaderValues.APPLICATION_JSON).set(HttpHeaders.ACCEPT, (CharSequence)HttpHeaderValues.APPLICATION_JSON).set(X_ENABLE_SERVER_TIMING, (CharSequence)"1").set(X_O_CCM, SERVER).set("x-o-tp-phase", this.tpPhase).set(WM_CORRELATION_ID, (CharSequence)string).set(HttpHeaders.ORIGIN, (CharSequence)"https://www.walmart.com").set(Headers.SEC_FETCH_SITE, Headers.SAME_ORIGIN).set(Headers.SEC_FETCH_MODE, Headers.CORS).set(Headers.SEC_FETCH_DEST, Headers.EMPTY).set(HttpHeaders.REFERER, (CharSequence)("https://www.walmart.com/checkout/review-order?cartId=" + this.cartId)).set(HttpHeaders.ACCEPT_ENCODING, Headers.GZIP_DEFLATE_BR).set(HttpHeaders.ACCEPT_LANGUAGE, (CharSequence)this.pxAPI.getDeviceLang()).set(HttpHeaders.COOKIE, Headers.DEFAULT);
        return httpRequest;
    }

    @Override
    public HttpRequest createAccount() {
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/account/electrode/api/signup?vid=oaoh").as(BodyCodec.buffer());
        switch (this.headerLogic) {
            case 0: {
                httpRequest.putHeader("content-length", "DEFAULT_VALUE");
                httpRequest.putHeader("sec-ch-ua", this.pxAPI.getDeviceSecUA());
                httpRequest.putHeader("sec-ch-ua-mobile", "?0");
                httpRequest.putHeader("user-agent", this.pxAPI.getDeviceUA());
                httpRequest.putHeader("sec-ch-ua-platform", this.pxAPI.getDeviceUA().contains("Macintosh") ? "\"macOS\"" : "\"Windows\"");
                httpRequest.putHeader("content-type", "application/json");
                httpRequest.putHeader("accept", "*/*");
                httpRequest.putHeader("origin", "https://www.walmart.com");
                httpRequest.putHeader("sec-fetch-site", "same-origin");
                httpRequest.putHeader("sec-fetch-mode", "cors");
                httpRequest.putHeader("sec-fetch-dest", "empty");
                httpRequest.putHeader("referer", "https://www.walmart.com/account/signup?vid=oaoh");
                httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
                httpRequest.putHeader("accept-language", this.pxAPI.getDeviceLang());
                return httpRequest;
            }
            case 1: {
                httpRequest.putHeader("content-length", "DEFAULT_VALUE");
                httpRequest.putHeader("accept", "*/*");
                httpRequest.putHeader("user-agent", this.pxAPI.getDeviceUA());
                httpRequest.putHeader("content-type", "application/json");
                httpRequest.putHeader("origin", "https://www.walmart.com");
                httpRequest.putHeader("sec-fetch-site", "same-origin");
                httpRequest.putHeader("sec-fetch-mode", "cors");
                httpRequest.putHeader("sec-fetch-dest", "empty");
                httpRequest.putHeader("referer", "https://www.walmart.com/account/signup?vid=oaoh");
                httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
                httpRequest.putHeader("accept-language", this.pxAPI.getDeviceLang());
                return httpRequest;
            }
        }
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("accept", "*/*");
        httpRequest.putHeader("user-agent", this.pxAPI.getDeviceUA());
        httpRequest.putHeader("device_profile_ref_id", this.deviceProfileId);
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("origin", "https://www.walmart.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.walmart.com/account/signup?vid=oaoh");
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", this.pxAPI.getDeviceLang());
        return httpRequest;
    }

    public JsonObject savePaymentForm(PaymentToken paymentToken) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("operationName", (Object)"AddCreditCard");
        jsonObject.put("variables", (Object)new JsonObject().put("input", (Object)new JsonObject().put("firstName", (Object)this.task.getProfile().getFirstName()).put("lastName", (Object)this.task.getProfile().getLastName()).put("phone", (Object)this.task.getProfile().getPhone()).put("address", (Object)new JsonObject().put("addressLineOne", (Object)this.task.getProfile().getAddress1()).put("addressLineTwo", (Object)this.task.getProfile().getAddress2()).put("city", (Object)this.task.getProfile().getCity()).put("state", (Object)this.task.getProfile().getState()).put("postalCode", (Object)this.task.getProfile().getZip())).put("expiryYear", (Object)Integer.parseInt(this.task.getProfile().getExpiryYear())).put("expiryMonth", (Object)Integer.parseInt(this.task.getProfile().getExpiryMonth())).put("cardType", (Object)this.task.getProfile().getCardType().name()).put("encryptedCVV", (Object)paymentToken.getEncryptedCvv()).put("encryptedPan", (Object)paymentToken.getEncryptedPan()).put("integrityCheck", (Object)paymentToken.getIntegrityCheck()).put("keyId", (Object)paymentToken.getKeyId()).put("phase", (Object)paymentToken.getPhase()).put("isDefault", (Object)true)));
        jsonObject.put("query", (Object)"mutation AddCreditCard($input: AccountCreditCardInput!) { createAccountCreditCard(input: $input) { __typename creditCard { __typename ...CreditCardFragment } errors { __typename ...ErrorFragment } } } fragment CreditCardFragment on CreditCard { __typename id firstName lastName lastFour isDefault isTemp cardAccountLinked needVerifyCVV addressLineOne addressLineTwo city state postalCode cardType phone expiryMonth expiryYear isExpired displayTypeAndLast4 displayExpireAndName } fragment ErrorFragment on AccountPaymentError { __typename code }");
        return jsonObject;
    }

    @Override
    public HttpRequest addToCart() {
        this.cookieStore().removeAnyMatch("tb-c30");
        String string = WalmartNewAPI.zs2y(32);
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/orchestra/home/graphql").as(BodyCodec.buffer());
        httpRequest.headers().setAll(Headers$Pseudo.MASP.get()).set(HttpHeaders.CONTENT_LENGTH, Headers.DEFAULT).set(Headers.SEC_CH_UA, (CharSequence)this.getPxAPI().getDeviceSecUA()).set(X_O_CORRELATION_ID, (CharSequence)string).set(DEVICE_PROFILE_REF, (CharSequence)this.deviceProfileId).set(X_LATENCY_TRACE, (CharSequence)"1").set(WM_MP, TRUE).set(X_O_PLATFORM_VERSION, (CharSequence)this.platformVersion).set(X_O_SEGMENT, OAOH).set(X_O_GQL_QUERY, (CharSequence)"mutation updateItems").set(WM_PAGE_URL, (CharSequence)this.referer).set(X_APOLLO_OPERATION_NAME, (CharSequence)"updateItems").set(Headers.SEC_CH_UA_PLATFORM, Headers.WINDOWS).set(Headers.SEC_CH_UA_MOBILE, Headers._Q0).set(HttpHeaders.USER_AGENT, (CharSequence)this.pxAPI.getDeviceUA()).set(X_O_PLATFORM, X_O_PLATFORM_NAME).set(HttpHeaders.CONTENT_TYPE, (CharSequence)HttpHeaderValues.APPLICATION_JSON).set(HttpHeaders.ACCEPT, (CharSequence)HttpHeaderValues.APPLICATION_JSON).set(X_ENABLE_SERVER_TIMING, (CharSequence)"1").set(X_O_CCM, SERVER).set(WM_CORRELATION_ID, (CharSequence)string).set(HttpHeaders.ORIGIN, (CharSequence)"https://www.walmart.com").set(Headers.SEC_FETCH_SITE, Headers.SAME_ORIGIN).set(Headers.SEC_FETCH_MODE, Headers.CORS).set(Headers.SEC_FETCH_DEST, Headers.EMPTY).set(HttpHeaders.REFERER, (CharSequence)this.referer).set(HttpHeaders.ACCEPT_ENCODING, Headers.GZIP_DEFLATE_BR).set(HttpHeaders.ACCEPT_LANGUAGE, (CharSequence)this.pxAPI.getDeviceLang()).set(HttpHeaders.COOKIE, Headers.DEFAULT);
        return httpRequest;
    }

    @Override
    public HttpRequest loginAccount() {
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/account/electrode/api/signin?vid=oaoh&tid=0&returnUrl=%2F").as(BodyCodec.buffer());
        httpRequest.headers().setAll(Headers$Pseudo.MASP.get()).set(HttpHeaders.CONTENT_LENGTH, Headers.DEFAULT).set(Headers.SEC_CH_UA, (CharSequence)this.getPxAPI().getDeviceSecUA()).set(HttpHeaders.CONTENT_TYPE, (CharSequence)HttpHeaderValues.APPLICATION_JSON).set(Headers.SEC_CH_UA_MOBILE, Headers._Q0).set(HttpHeaders.USER_AGENT, (CharSequence)this.pxAPI.getDeviceUA()).set(DEVICE_PROFILE_REF, (CharSequence)this.deviceProfileId).set(Headers.SEC_CH_UA_PLATFORM, Headers.WINDOWS).set(HttpHeaders.ACCEPT, (CharSequence)"*/*").set(HttpHeaders.ORIGIN, (CharSequence)"https://www.walmart.com").set(Headers.SEC_FETCH_SITE, Headers.SAME_ORIGIN).set(Headers.SEC_FETCH_MODE, Headers.CORS).set(Headers.SEC_FETCH_DEST, Headers.EMPTY).set(HttpHeaders.REFERER, (CharSequence)"https://www.walmart.com/account/login?vid=oaoh&tid=0&returnUrl=%2F").set(HttpHeaders.ACCEPT_ENCODING, Headers.GZIP_DEFLATE_BR).set(HttpHeaders.ACCEPT_LANGUAGE, (CharSequence)this.pxAPI.getDeviceLang()).set(HttpHeaders.COOKIE, Headers.DEFAULT);
        return httpRequest;
    }

    @Override
    public HttpRequest fulfillment() {
        String string = WalmartNewAPI.zs2y(32);
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/orchestra/cartxo/graphql").as(BodyCodec.buffer());
        httpRequest.headers().setAll(Headers$Pseudo.MASP.get()).set(HttpHeaders.CONTENT_LENGTH, Headers.DEFAULT).set(Headers.SEC_CH_UA, (CharSequence)this.getPxAPI().getDeviceSecUA()).set(X_O_CORRELATION_ID, (CharSequence)string).set(DEVICE_PROFILE_REF, (CharSequence)this.deviceProfileId).set(X_LATENCY_TRACE, (CharSequence)"1").set(WM_MP, TRUE).set(X_O_PLATFORM_VERSION, (CharSequence)this.platformVersion).set(X_O_SEGMENT, OAOH).set(X_O_GQL_QUERY, (CharSequence)"mutation setFulfillment").set(WM_PAGE_URL, (CharSequence)(this.referer + "?step=cart&ss=addAddress&gxo=true")).set(X_APOLLO_OPERATION_NAME, (CharSequence)"setFulfillment").set(Headers.SEC_CH_UA_PLATFORM, Headers.WINDOWS).set(Headers.SEC_CH_UA_MOBILE, Headers._Q0).set(HttpHeaders.USER_AGENT, (CharSequence)this.pxAPI.getDeviceUA()).set(X_O_PLATFORM, X_O_PLATFORM_NAME).set(HttpHeaders.CONTENT_TYPE, (CharSequence)HttpHeaderValues.APPLICATION_JSON).set(HttpHeaders.ACCEPT, (CharSequence)HttpHeaderValues.APPLICATION_JSON).set(X_ENABLE_SERVER_TIMING, (CharSequence)"1").set(X_O_CCM, SERVER).set(WM_CORRELATION_ID, (CharSequence)string).set(HttpHeaders.ORIGIN, (CharSequence)"https://www.walmart.com").set(Headers.SEC_FETCH_SITE, Headers.SAME_ORIGIN).set(Headers.SEC_FETCH_MODE, Headers.CORS).set(Headers.SEC_FETCH_DEST, Headers.EMPTY).set(HttpHeaders.REFERER, (CharSequence)(this.referer + "?step=cart&ss=addAddress&gxo=true")).set(HttpHeaders.ACCEPT_ENCODING, Headers.GZIP_DEFLATE_BR).set(HttpHeaders.ACCEPT_LANGUAGE, (CharSequence)this.pxAPI.getDeviceLang()).set(HttpHeaders.COOKIE, Headers.DEFAULT);
        return httpRequest;
    }

    @Override
    public CookieJar cookieStore() {
        return this.getWebClient().cookieStore();
    }

    @Override
    public HttpRequest submitPayment() {
        String string = WalmartNewAPI.zs2y(32);
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/orchestra/cartxo/graphql").as(BodyCodec.buffer());
        httpRequest.putHeader("content-length", "DEFAULT_VALUE");
        httpRequest.putHeader("sec-ch-ua", this.getPxAPI().getDeviceSecUA());
        httpRequest.putHeader("x-o-platform", "rweb");
        httpRequest.putHeader("x-o-correlation-id", string);
        httpRequest.putHeader("device_profile_ref_id", this.deviceProfileId);
        httpRequest.putHeader("x-latency-trace", "1");
        httpRequest.putHeader("wm_mp", "true");
        httpRequest.putHeader("x-o-market", "us");
        httpRequest.putHeader("x-o-platform-version", this.platformVersion);
        httpRequest.putHeader("x-o-gql-query", "mutation PlaceOrder");
        httpRequest.putHeader("x-apollo-operation-name", "PlaceOrder");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("user-agent", this.pxAPI.getDeviceUA());
        httpRequest.putHeader("x-o-segment", "oaoh");
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("accept", "application/json");
        httpRequest.putHeader("x-enable-server-timing", "1");
        httpRequest.putHeader("x-o-ccm", "server");
        httpRequest.putHeader("x-o-tp-phase", "tp5");
        httpRequest.putHeader("wm_qos.correlation_id", string);
        httpRequest.putHeader("origin", "https://www.walmart.com");
        httpRequest.putHeader("sec-fetch-site", "same-origin");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.walmart.com/checkout/review-order?cartId=" + this.cartId);
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", this.pxAPI.getDeviceLang());
        return httpRequest;
    }

    @Override
    public HttpRequest getPCID() {
        String string = WalmartNewAPI.zs2y(32);
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/orchestra/cartxo/graphql").as(BodyCodec.buffer());
        httpRequest.headers().setAll(Headers$Pseudo.MASP.get()).set(HttpHeaders.CONTENT_LENGTH, Headers.DEFAULT).set(Headers.SEC_CH_UA, (CharSequence)this.getPxAPI().getDeviceSecUA()).set(X_O_CORRELATION_ID, (CharSequence)string).set(DEVICE_PROFILE_REF, (CharSequence)this.deviceProfileId).set(X_LATENCY_TRACE, (CharSequence)"1").set(WM_MP, TRUE).set(X_O_PLATFORM_VERSION, (CharSequence)this.platformVersion).set(X_O_SEGMENT, OAOH).set(X_O_GQL_QUERY, (CharSequence)"mutation CreateDeliveryAddress").set(WM_PAGE_URL, (CharSequence)(this.referer + "?step=cart&ss=addAddress&gxo=true")).set(X_APOLLO_OPERATION_NAME, (CharSequence)"CreateDeliveryAddress").set(Headers.SEC_CH_UA_PLATFORM, Headers.WINDOWS).set(Headers.SEC_CH_UA_MOBILE, Headers._Q0).set(HttpHeaders.USER_AGENT, (CharSequence)this.pxAPI.getDeviceUA()).set(X_O_PLATFORM, X_O_PLATFORM_NAME).set(HttpHeaders.CONTENT_TYPE, (CharSequence)HttpHeaderValues.APPLICATION_JSON).set(HttpHeaders.ACCEPT, (CharSequence)HttpHeaderValues.APPLICATION_JSON).set(X_ENABLE_SERVER_TIMING, (CharSequence)"1").set(X_O_CCM, SERVER).set(WM_CORRELATION_ID, (CharSequence)string).set(HttpHeaders.ORIGIN, (CharSequence)"https://www.walmart.com").set(Headers.SEC_FETCH_SITE, Headers.SAME_ORIGIN).set(Headers.SEC_FETCH_MODE, Headers.CORS).set(Headers.SEC_FETCH_DEST, Headers.EMPTY).set(HttpHeaders.REFERER, (CharSequence)(this.referer + "?step=cart&ss=addAddress&gxo=true")).set(HttpHeaders.ACCEPT_ENCODING, Headers.GZIP_DEFLATE_BR).set(HttpHeaders.ACCEPT_LANGUAGE, (CharSequence)this.pxAPI.getDeviceLang()).set(HttpHeaders.COOKIE, Headers.DEFAULT);
        return httpRequest;
    }

    @Override
    public JsonObject getDeliveryAddressesForm() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("query", (Object)"query GetDeliveryAddresses( $DeliveryAddressOptionInput:DeliveryAddressOptionInput ){deliveryAddresses(input:$DeliveryAddressOptionInput){accessPoint{...AccessPoint}id...BaseAddressResponse...RegistryAddressResponse}}fragment BasicAddress on AccountAddressBase{addressLineOne addressLineTwo city state postalCode}fragment BaseAddressResponse on AccountAddress{...BasicAddress firstName lastName phone isDefault deliveryInstructions serviceStatus capabilities allowEditOrRemove}fragment AccessPoint on AccessPointRovr{id assortmentStoreId fulfillmentType accountFulfillmentOption accountAccessType}fragment RegistryAddressResponse on AccountAddress{registry{id type}addressTitle}");
        jsonObject.put("variables", (Object)new JsonObject().put("responseGroup", (Object)"storeDeliverable"));
        return jsonObject;
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    public static CompletableFuture async$handleBadResponse(WalmartNewAPI var0, HttpResponse var1_1, String var2_2, CompletableFuture var3_3, JsonObject var4_5, String var5_6, String var6_7, WalmartNewAPI var7_9, int var8_11, Matcher var9_17, String var10_18, int var11_19, Object var12_20) {
        switch (var11_19) {
            case 0: {
                var0.pxAPI.updatePxhd(var0.cookieStore().getCookieValue("_pxhd"));
                switch (var1_1.statusCode()) {
                    case 412: {
                        if (!var0.task.getMode().contains("skip")) ** GOTO lbl13
                        v0 = var0.generatePX(true);
                        if (!v0.isDone()) {
                            var8_12 = v0;
                            return var8_12.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$handleBadResponse(io.trickle.task.sites.walmart.graphql.WalmartNewAPI io.vertx.ext.web.client.HttpResponse java.lang.String java.util.concurrent.CompletableFuture io.vertx.core.json.JsonObject java.lang.String java.lang.String io.trickle.task.sites.walmart.graphql.WalmartNewAPI int java.util.regex.Matcher java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((WalmartNewAPI)var0, (HttpResponse)var1_1, (String)var2_2, (CompletableFuture)var8_12, null, null, null, null, (int)0, null, null, (int)1));
                        }
                        ** GOTO lbl59
lbl13:
                        // 1 sources

                        var3_3 = var1_1.bodyAsJsonObject();
                        var4_5 /* !! */  = var3_3.getString("uuid");
                        var5_6 = var3_3.getString("vid");
                        v1 = var0;
                        v2 = var0.pxAPI.solveCaptcha((String)var5_6, (String)var4_5 /* !! */ , var2_2);
                        if (!v2.isDone()) {
                            var9_17 = v2;
                            var8_13 = v1;
                            return var9_17.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$handleBadResponse(io.trickle.task.sites.walmart.graphql.WalmartNewAPI io.vertx.ext.web.client.HttpResponse java.lang.String java.util.concurrent.CompletableFuture io.vertx.core.json.JsonObject java.lang.String java.lang.String io.trickle.task.sites.walmart.graphql.WalmartNewAPI int java.util.regex.Matcher java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((WalmartNewAPI)var0, (HttpResponse)var1_1, (String)var2_2, (CompletableFuture)var9_17, (JsonObject)var3_3, (String)var4_5 /* !! */ , (String)var5_6, (WalmartNewAPI)var8_13, (int)0, null, null, (int)2));
                        }
                        ** GOTO lbl45
                    }
                    case 307: {
                        try {
                            if (!var0.task.getMode().contains("skip")) ** GOTO lbl31
                            v3 = var0.generatePX(true);
                            if (!v3.isDone()) {
                                var8_15 = v3;
                                return var8_15.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$handleBadResponse(io.trickle.task.sites.walmart.graphql.WalmartNewAPI io.vertx.ext.web.client.HttpResponse java.lang.String java.util.concurrent.CompletableFuture io.vertx.core.json.JsonObject java.lang.String java.lang.String io.trickle.task.sites.walmart.graphql.WalmartNewAPI int java.util.regex.Matcher java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((WalmartNewAPI)var0, (HttpResponse)var1_1, (String)var2_2, (CompletableFuture)var8_15, null, null, null, null, (int)0, null, null, (int)4));
                            }
                            ** GOTO lbl84
lbl31:
                            // 1 sources

                            var3_3 = "https://www.walmart.com" + var1_1.getHeader("location");
                            var4_5 /* !! */  = var3_3.split("uuid=")[1].split("&")[0];
                            var5_6 = WalmartNewAPI.VID_LOCATION_PATTERN.matcher((CharSequence)var3_3);
                            var6_7 = var5_6.find() != false ? (var5_6.group(1).isBlank() != false ? null : var5_6.group(1)) : null;
                            v4 = var0;
                            v5 = var0.pxAPI.solveCaptcha(var6_7, (String)var4_5 /* !! */ , (String)var3_3);
                            if (!v5.isDone()) {
                                var9_17 = v5;
                                var8_16 = v4;
                                return var9_17.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$handleBadResponse(io.trickle.task.sites.walmart.graphql.WalmartNewAPI io.vertx.ext.web.client.HttpResponse java.lang.String java.util.concurrent.CompletableFuture io.vertx.core.json.JsonObject java.lang.String java.lang.String io.trickle.task.sites.walmart.graphql.WalmartNewAPI int java.util.regex.Matcher java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((WalmartNewAPI)var0, (HttpResponse)var1_1, (String)var2_2, (CompletableFuture)var9_17, null, (String)var3_3, (String)var4_5 /* !! */ , (WalmartNewAPI)var8_16, (int)0, (Matcher)var5_6, (String)var6_7, (int)5));
                            }
                            ** GOTO lbl96
                        }
                        catch (Throwable var3_4) {
                            System.out.println("Error solving captcha [DESKTOP] " + var3_4.getMessage());
                            ** GOTO lbl52
                        }
                    }
lbl45:
                    // 2 sources

                    while (true) {
                        var6_8 = (int)v1.parseMapIntoCookieJar((MultiMap)v2.join());
                        v6 = var0.generatePX(false);
                        if (!v6.isDone()) {
                            var8_14 = v6;
                            return var8_14.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$handleBadResponse(io.trickle.task.sites.walmart.graphql.WalmartNewAPI io.vertx.ext.web.client.HttpResponse java.lang.String java.util.concurrent.CompletableFuture io.vertx.core.json.JsonObject java.lang.String java.lang.String io.trickle.task.sites.walmart.graphql.WalmartNewAPI int java.util.regex.Matcher java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((WalmartNewAPI)var0, (HttpResponse)var1_1, (String)var2_2, (CompletableFuture)var8_14, (JsonObject)var3_3, (String)var4_5 /* !! */ , (String)var5_6, null, (int)var6_8, null, null, (int)3));
                        }
                        ** GOTO lbl79
                        break;
                    }
lbl52:
                    // 2 sources

                    case 444: {
                        if (super.rotateProxy() == false) return CompletableFuture.completedFuture(false);
                        var0.pxAPI.restartClient(var0.client);
                        return CompletableFuture.completedFuture(false);
                    }
                }
                return CompletableFuture.completedFuture(false);
            }
            case 1: {
                v0 = var3_3;
lbl59:
                // 2 sources

                v0.join();
                return CompletableFuture.completedFuture(false);
            }
            case 2: {
                v1 = var7_9;
                v2 = var3_3;
                v7 = var4_5 /* !! */ ;
                v8 = var5_6;
                var5_6 = var6_7;
                var4_5 /* !! */  = v8;
                var3_3 = v7;
                ** continue;
            }
            case 3: {
                v6 = var3_3;
                v9 = var4_5 /* !! */ ;
                v10 = var5_6;
                var6_8 = var8_11;
                var5_6 = var6_7;
                var4_5 /* !! */  = v10;
                var3_3 = v9;
lbl79:
                // 2 sources

                v6.join();
                return CompletableFuture.completedFuture((boolean)var6_8);
            }
            case 4: {
                v3 = var3_3;
lbl84:
                // 2 sources

                v3.join();
                return CompletableFuture.completedFuture(false);
            }
            case 5: {
                v4 = var7_9;
                v5 = var3_3;
                v11 = var5_6;
                v12 = var6_7;
                var6_7 = var10_18;
                var5_6 = var9_17;
                var4_5 /* !! */  = v12;
                var3_3 = v11;
lbl96:
                // 2 sources

                var7_10 = v4.parseMapIntoCookieJar((MultiMap)v5.join());
                return CompletableFuture.completedFuture(var7_10);
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public JsonObject saveAddressJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("query", (Object)"mutation CreateDeliveryAddress($input:AccountAddressesInput!){createAccountAddress(input:$input){...DeliveryAddressMutationResponse}}fragment DeliveryAddressMutationResponse on MutateAccountAddressResponse{...AddressMutationResponse newAddress{id accessPoint{...AccessPoint}...BaseAddressResponse}}fragment AccessPoint on AccessPointRovr{id assortmentStoreId fulfillmentType accountFulfillmentOption accountAccessType}fragment AddressMutationResponse on MutateAccountAddressResponse{errors{code}enteredAddress{...BasicAddress}suggestedAddresses{...BasicAddress sealedAddress}newAddress{id...BaseAddressResponse}allowAvsOverride}fragment BasicAddress on AccountAddressBase{addressLineOne addressLineTwo city state postalCode}fragment BaseAddressResponse on AccountAddress{...BasicAddress firstName lastName phone isDefault deliveryInstructions serviceStatus capabilities allowEditOrRemove}");
        jsonObject.put("variables", (Object)new JsonObject().put("input", (Object)new JsonObject().put("address", (Object)new JsonObject().put("addressLineOne", (Object)this.task.getProfile().getAddress1()).put("addressLineTwo", (Object)this.task.getProfile().getAddress2()).put("city", (Object)this.task.getProfile().getCity()).put("postalCode", (Object)this.task.getProfile().getZip()).put("state", (Object)this.task.getProfile().getState()).put("addressType", null).put("businessName", null).put("isApoFpo", null).put("isLoadingDockAvailable", null).put("isPoBox", null).put("sealedAddress", null)).put("firstName", (Object)this.task.getProfile().getFirstName()).put("lastName", (Object)this.task.getProfile().getLastName()).put("deliveryInstructions", null).put("displayLabel", null).put("isDefault", (Object)true).put("phone", (Object)this.task.getProfile().getPhone()).put("overrideAvs", (Object)true)));
        return jsonObject;
    }

    @Override
    public JsonObject buyNowBody() {
        JsonObject jsonObject = new JsonObject();
        JsonObject jsonObject2 = new JsonObject();
        jsonObject.put("query", (Object)"mutation CreateBuyNowContract( $buyNowContractInput:BuyNowContractInput! $promosEnable:Boolean! $wplusEnabled:Boolean! ){createBuyNowContract(input:$buyNowContractInput){...ContractFragment}}fragment ContractFragment on PurchaseContract{id associateDiscountStatus addressMode tenderPlanId papEbtAllowed allowedPaymentGroupTypes cartCustomerContext @include(if:$wplusEnabled){isMembershipOptedIn isEligibleForFreeTrial paymentData{hasCreditCard}}checkoutError{code errorData{__typename...on OutOfStock{offerId}__typename...on UnavailableOffer{offerId}__typename...on ItemExpired{offerId}__typename...on ItemQuantityAdjusted{offerId requestedQuantity adjustedQuantity}}operationalErrorCode message}checkoutableWarnings{code itemIds}allocationStatus payments{id paymentType cardType lastFour isDefault cvvRequired preferenceId paymentPreferenceId paymentHandle expiryMonth expiryYear firstName lastName email amountPaid cardImage cardImageAlt isLinkedCard capOneReward{credentialId redemptionUrl redemptionRate redemptionMethod rewardPointsBalance rewardPointsSelected rewardAmountSelected}remainingBalance{displayValue value}}order{id status orderVersion mobileNumber}terms{alcoholAccepted bagFeeAccepted smsOptInAccepted marketingEmailPrefOptIn}donationDetails{charityEIN charityName amount{displayValue value}acceptDonation}lineItems{...LineItemFields}tippingDetails{suggestedAmounts{value displayValue}maxAmount{value displayValue}selectedTippingAmount{value displayValue}}customer{id firstName lastName isGuest email phone}fulfillment{deliveryDetails{deliveryInstructions deliveryOption}pickupChoices{isSelected fulfillmentType accessType accessMode accessPointId}deliveryAddress{...AddressFields}alternatePickupPerson{...PickupPersonFields}primaryPickupPerson{...PickupPersonFields}fulfillmentItemGroups{...FulfillmentItemGroupsFields}accessPoint{...AccessPointFields}reservation{...ReservationFields}}priceDetails{subTotal{...PriceDetailRowFields}totalItemQuantity fees{...PriceDetailRowFields}taxTotal{...PriceDetailRowFields}grandTotal{...PriceDetailRowFields}belowMinimumFee{...PriceDetailRowFields}authorizationAmount{...PriceDetailRowFields}weightDebitTotal{...PriceDetailRowFields}discounts{...PriceDetailRowFields}otcDeliveryBenefit{...PriceDetailRowFields}ebtSnapMaxEligible{...PriceDetailRowFields}ebtCashMaxEligible{...PriceDetailRowFields}hasAmountUnallocated affirm{__typename message{...AffirmMessageFields}}}checkoutGiftingDetails{isCheckoutGiftingOptin isWalmartProtectionPlanPresent isAppleCarePresent isRestrictedPaymentPresent giftMessageDetails{giftingMessage recipientEmail recipientName senderName}}promotions @include(if:$promosEnable){displayValue promoId terms}showPromotions @include(if:$promosEnable) errors{code message lineItems{...LineItemFields}}}fragment LineItemFields on LineItem{id quantity quantityString quantityLabel accessibilityQuantityLabel isPreOrder fulfillmentSourcingDetails{currentSelection requestedSelection}packageQuantity priceInfo{priceDisplayCodes{showItemPrice priceDisplayCondition finalCostByWeight}itemPrice{displayValue value}linePrice{displayValue value}preDiscountedLinePrice{displayValue value}wasPrice{displayValue value}unitPrice{displayValue value}}isSubstitutionSelected isGiftEligible selectedVariants{name value}product{id name usItemId itemType imageInfo{thumbnailUrl}offerId orderLimit orderMinLimit weightIncrement weightUnit averageWeight salesUnitType availabilityStatus isSubstitutionEligible isAlcohol configuration hasSellerBadge sellerId sellerName sellerType preOrder{...preOrderFragment}addOnServices{serviceType groups{groupType services{selectedDisplayName offerId currentPrice{priceString}}}}}discounts{key label displayValue @include(if:$promosEnable) displayLabel @include(if:$promosEnable)}wirelessPlan{planId mobileNumber __typename postPaidPlan{...postpaidPlanDetailsFragment}}selectedAddOnServices{offerId quantity groupType}registryInfo{registryId registryType}}fragment postpaidPlanDetailsFragment on PostPaidPlan{__typename espOrderSummaryId espOrderId espOrderLineId warpOrderId warpSessionId devicePayment{...postpaidPlanPriceFragment}devicePlan{__typename price{...postpaidPlanPriceFragment}frequency duration annualPercentageRate}deviceDataPlan{...deviceDataPlanFragment}}fragment deviceDataPlanFragment on DeviceDataPlan{__typename carrierName planType expiryTime activationFee{...postpaidPlanPriceFragment}planDetails{__typename price{...postpaidPlanPriceFragment}frequency name}agreements{...agreementFragment}}fragment postpaidPlanPriceFragment on PriceDetailRow{__typename key label displayValue value strikeOutDisplayValue strikeOutValue info{__typename title message}}fragment agreementFragment on CarrierAgreement{__typename name type format value docTitle label}fragment preOrderFragment on PreOrder{streetDate streetDateDisplayable streetDateType isPreOrder preOrderMessage preOrderStreetDateMessage}fragment AddressFields on Address{id addressLineOne addressLineTwo city state postalCode firstName lastName phone}fragment PickupPersonFields on PickupPerson{id firstName lastName email}fragment PriceDetailRowFields on PriceDetailRow{__typename key label displayValue value strikeOutValue strikeOutDisplayValue info{__typename title message}}fragment AccessPointFields on AccessPoint{id name assortmentStoreId displayName timeZone address{id addressLineOne addressLineTwo city state postalCode firstName lastName phone}isTest allowBagFee bagFeeValue isExpressEligible fulfillmentOption instructions nodeAccessType}fragment ReservationFields on Reservation{id expiryTime isUnscheduled expired showSlotExpiredError reservedSlot{__typename...on RegularSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata slotExpiryTime endTime available supportedTimeZone}...on DynamicExpressSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime endTime fulfillmentType slotMetadata slotExpiryTime available slaInMins maxItemAllowed supportedTimeZone}...on UnscheduledSlot{price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata unscheduledHoldInDays supportedTimeZone}...on InHomeSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata slotExpiryTime endTime available supportedTimeZone}}}fragment AffirmMessageFields on AffirmMessage{__typename description termsUrl imageUrl monthlyPayment termLength isZeroAPR}fragment FulfillmentItemGroupsFields on FulfillmentItemGroup{...on SCGroup{__typename defaultMode collapsedItemIds itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}...on DigitalDeliveryGroup{__typename defaultMode collapsedItemIds itemGroups{__typename label itemIds}}...on Unscheduled{__typename defaultMode collapsedItemIds itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}...on FCGroup{__typename defaultMode collapsedItemIds startDate endDate isUnscheduledDeliveryEligible shippingOptions{__typename itemIds availableShippingOptions{__typename id shippingMethod deliveryDate price{__typename displayValue value}label{prefix suffix}isSelected isDefault}}hasMadeShippingChanges slaGroups{__typename label deliveryDate warningLabel sellerGroups{__typename id name isProSeller type shipOptionGroup{__typename deliveryPrice{__typename displayValue value}itemIds shipMethod}}}}...on AutoCareCenter{__typename defaultMode startDate endDate accBasketType collapsedItemIds itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}}");
        jsonObject2.put("buyNowContractInput", (Object)new JsonObject().put("items", (Object)new JsonArray().add((Object)new JsonObject().put("offerId", (Object)this.task.getKeywords()[0]).put("quantity", (Object)Double.parseDouble(this.task.getSize())))));
        jsonObject2.put("promosEnable", (Object)true);
        jsonObject2.put("wplusEnabled", (Object)true);
        jsonObject.put("variables", (Object)jsonObject2);
        return jsonObject;
    }

    @Override
    public JsonObject createContractBody() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("query", (Object)"mutation CreateContract( $createContractInput:CreatePurchaseContractInput! $promosEnable:Boolean! $wplusEnabled:Boolean! ){createPurchaseContract(input:$createContractInput){...ContractFragment}}fragment ContractFragment on PurchaseContract{id associateDiscountStatus tenderPlanId papEbtAllowed cartCustomerContext @include(if:$wplusEnabled){isMembershipOptedIn isEligibleForFreeTrial paymentData{hasCreditCard}}checkoutError{code errorData{__typename...on OutOfStock{offerId}__typename...on UnavailableOffer{offerId}__typename...on ItemExpired{offerId}__typename...on ItemQuantityAdjusted{offerId requestedQuantity adjustedQuantity}}operationalErrorCode message}checkoutableWarnings{code itemIds}allocationStatus payments{id paymentType cardType lastFour isDefault cvvRequired preferenceId paymentPreferenceId expiryMonth expiryYear firstName lastName amountPaid cardImage cardImageAlt isLinkedCard capOneReward{credentialId redemptionUrl redemptionRate redemptionMethod rewardPointsBalance rewardPointsSelected rewardAmountSelected}remainingBalance{displayValue value}}order{id status orderVersion mobileNumber}terms{alcoholAccepted bagFeeAccepted smsOptInAccepted marketingEmailPrefOptIn}donationDetails{charityEIN charityName amount{displayValue value}acceptDonation}lineItems{...LineItemFields}tippingDetails{suggestedAmounts{value displayValue}maxAmount{value displayValue}selectedTippingAmount{value displayValue}}customer{id firstName lastName isGuest email phone}fulfillment{deliveryDetails{deliveryInstructions deliveryOption}pickupChoices{isSelected fulfillmentType accessType accessMode accessPointId}deliveryAddress{...AddressFields}alternatePickupPerson{...PickupPersonFields}primaryPickupPerson{...PickupPersonFields}fulfillmentItemGroups{...on SCGroup{__typename defaultMode collapsedItemIds itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}...on DigitalDeliveryGroup{__typename defaultMode collapsedItemIds itemGroups{__typename label itemIds}}...on Unscheduled{__typename defaultMode collapsedItemIds itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}...on FCGroup{__typename defaultMode collapsedItemIds startDate endDate shippingOptions{__typename itemIds availableShippingOptions{__typename id shippingMethod deliveryDate price{__typename displayValue value}label{prefix suffix}isSelected isDefault}}hasMadeShippingChanges slaGroups{__typename label deliveryDate warningLabel sellerGroups{__typename id name isProSeller type shipOptionGroup{__typename deliveryPrice{__typename displayValue value}itemIds shipMethod}}}}...on AutoCareCenter{__typename defaultMode startDate endDate accBasketType collapsedItemIds itemGroups{__typename label itemIds}accessPoint{...AccessPointFields}reservation{...ReservationFields}}}accessPoint{...AccessPointFields}reservation{...ReservationFields}}priceDetails{subTotal{...PriceDetailRowFields}totalItemQuantity fees{...PriceDetailRowFields}taxTotal{...PriceDetailRowFields}grandTotal{...PriceDetailRowFields}belowMinimumFee{...PriceDetailRowFields}authorizationAmount{...PriceDetailRowFields}weightDebitTotal{...PriceDetailRowFields}discounts{...PriceDetailRowFields}otcDeliveryBenefit{...PriceDetailRowFields}ebtSnapMaxEligible{...PriceDetailRowFields}ebtCashMaxEligible{...PriceDetailRowFields}hasAmountUnallocated}promotions @include(if:$promosEnable){displayValue promoId terms}showPromotions @include(if:$promosEnable) errors{code message lineItems{...LineItemFields}}}fragment LineItemFields on LineItem{id quantity quantityString quantityLabel accessibilityQuantityLabel isPreOrder fulfillmentSourcingDetails{currentSelection requestedSelection}priceInfo{priceDisplayCodes{showItemPrice priceDisplayCondition finalCostByWeight}itemPrice{displayValue value}linePrice{displayValue value}preDiscountedLinePrice{displayValue value}wasPrice{displayValue value}unitPrice{displayValue value}}isSubstitutionSelected selectedVariants{name value}product{id name usItemId imageInfo{thumbnailUrl}offerId orderLimit orderMinLimit weightIncrement weightUnit averageWeight salesUnitType availabilityStatus isSubstitutionEligible isAlcohol configuration hasSellerBadge sellerId sellerName sellerType preOrder{...preOrderFragment}}discounts{key label displayValue @include(if:$promosEnable) displayLabel @include(if:$promosEnable)}wirelessPlan{planId mobileNumber __typename postPaidPlan{...postpaidPlanDetailsFragment}}}fragment postpaidPlanDetailsFragment on PostPaidPlan{__typename espOrderSummaryId espOrderId espOrderLineId warpOrderId warpSessionId devicePayment{...postpaidPlanPriceFragment}devicePlan{__typename price{...postpaidPlanPriceFragment}frequency duration annualPercentageRate}deviceDataPlan{...deviceDataPlanFragment}}fragment deviceDataPlanFragment on DeviceDataPlan{__typename carrierName planType expiryTime activationFee{...postpaidPlanPriceFragment}planDetails{__typename price{...postpaidPlanPriceFragment}frequency name}agreements{...agreementFragment}}fragment postpaidPlanPriceFragment on PriceDetailRow{__typename key label displayValue value strikeOutDisplayValue strikeOutValue info{__typename title message}}fragment agreementFragment on CarrierAgreement{__typename name type format value docTitle label}fragment preOrderFragment on PreOrder{streetDate streetDateDisplayable streetDateType isPreOrder preOrderMessage preOrderStreetDateMessage}fragment AddressFields on Address{id addressLineOne addressLineTwo city state postalCode firstName lastName phone}fragment PickupPersonFields on PickupPerson{id firstName lastName email}fragment PriceDetailRowFields on PriceDetailRow{__typename key label displayValue value strikeOutValue strikeOutDisplayValue info{__typename title message}}fragment AccessPointFields on AccessPoint{id name assortmentStoreId displayName timeZone address{id addressLineOne addressLineTwo city state postalCode firstName lastName phone}isTest allowBagFee bagFeeValue isExpressEligible fulfillmentOption instructions accessType}fragment ReservationFields on Reservation{id expiryTime isUnscheduled expired showSlotExpiredError reservedSlot{__typename...on RegularSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata slotExpiryTime endTime available supportedTimeZone}...on DynamicExpressSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime endTime fulfillmentType slotMetadata slotExpiryTime available slaInMins maxItemAllowed supportedTimeZone}...on UnscheduledSlot{price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata unscheduledHoldInDays supportedTimeZone}...on InHomeSlot{id price{total{displayValue}expressFee{displayValue}baseFee{displayValue}memberBaseFee{displayValue}}accessPointId fulfillmentOption startTime fulfillmentType slotMetadata slotExpiryTime endTime available supportedTimeZone}}}");
        jsonObject.put("variables", (Object)new JsonObject().put("createContractInput", (Object)new JsonObject().put("cartId", (Object)this.cartId)).put("promosEnable", (Object)true).put("wplusEnabled", (Object)true));
        return jsonObject;
    }

    @Override
    public HttpRequest setAddressID() {
        String string = WalmartNewAPI.zs2y(32);
        HttpRequest httpRequest = this.client.postAbs("https://www.walmart.com/orchestra/home/graphql").as(BodyCodec.buffer());
        httpRequest.headers().setAll(Headers$Pseudo.MASP.get()).set(HttpHeaders.CONTENT_LENGTH, Headers.DEFAULT).set(X_O_PLATFORM, X_O_PLATFORM_NAME).set(X_O_CORRELATION_ID, (CharSequence)string).set(DEVICE_PROFILE_REF, (CharSequence)this.deviceProfileId).set(X_LATENCY_TRACE, (CharSequence)"1").set(WM_MP, TRUE).set(X_O_PLATFORM_VERSION, (CharSequence)this.platformVersion).set(X_O_GQL_QUERY, (CharSequence)"mutation setShippingAddress").set(WM_PAGE_URL, (CharSequence)this.productReferer).set(X_APOLLO_OPERATION_NAME, (CharSequence)"setShippingAddress").set(HttpHeaders.USER_AGENT, (CharSequence)this.pxAPI.getDeviceUA()).set(X_O_SEGMENT, OAOH).set(HttpHeaders.CONTENT_TYPE, (CharSequence)HttpHeaderValues.APPLICATION_JSON).set(HttpHeaders.ACCEPT, (CharSequence)HttpHeaderValues.APPLICATION_JSON).set(X_ENABLE_SERVER_TIMING, (CharSequence)"1").set(X_O_CCM, SERVER).set(WM_CORRELATION_ID, (CharSequence)string).set(HttpHeaders.ORIGIN, (CharSequence)"https://www.walmart.com").set(Headers.SEC_FETCH_SITE, Headers.SAME_ORIGIN).set(Headers.SEC_FETCH_MODE, Headers.CORS).set(Headers.SEC_FETCH_DEST, Headers.EMPTY).set(HttpHeaders.REFERER, (CharSequence)this.productReferer).set(HttpHeaders.ACCEPT_ENCODING, Headers.GZIP_DEFLATE_BR).set(HttpHeaders.ACCEPT_LANGUAGE, (CharSequence)this.pxAPI.getDeviceLang()).set(HttpHeaders.COOKIE, Headers.DEFAULT);
        return httpRequest;
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$initialisePX(WalmartNewAPI var0, CompletableFuture var1_1, int var2_2, Object var3_3) {
        switch (var2_2) {
            case 0: {
                v0 = var0.pxAPI.initialise();
                if (!v0.isDone()) {
                    var1_1 = v0;
                    return var1_1.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$initialisePX(io.trickle.task.sites.walmart.graphql.WalmartNewAPI java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((WalmartNewAPI)var0, (CompletableFuture)var1_1, (int)1));
                }
                ** GOTO lbl10
            }
            case 1: {
                v0 = var1_1;
lbl10:
                // 2 sources

                v0.join();
                return CompletableFuture.completedFuture(true);
            }
        }
        throw new IllegalArgumentException();
    }

    public WalmartNewAPI(Task task) {
        super(ClientType.CHROME);
        this.task = task;
        this.crossSite = crossSiteList[ThreadLocalRandom.current().nextInt(crossSiteList.length)];
        this.searchQuery = searchQueries[ThreadLocalRandom.current().nextInt(searchQueries.length)];
        this.referer = "https://www.walmart.com/";
    }

    @Override
    public HttpRequest issueTicket(String string) {
        String string2 = WalmartNewAPI.zs2y(32);
        HttpRequest httpRequest = this.client.getAbs(string).as(BodyCodec.buffer());
        httpRequest.putHeader("sec-ch-ua", this.pxAPI.getDeviceSecUA());
        httpRequest.putHeader("wm_qos.correlation_id", string2);
        httpRequest.putHeader("x-o-correlation-id", string2);
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("device_profile_ref_id", this.deviceProfileId);
        httpRequest.putHeader("x-o-segment", OAOH.toString());
        httpRequest.putHeader("wm_mp", "true");
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("accept", "application/json");
        httpRequest.putHeader("x-o-platform-version", this.platformVersion);
        httpRequest.putHeader("x-latency-trace", "1");
        httpRequest.putHeader("x-enable-server-timing", "1");
        httpRequest.putHeader("x-o-platform", X_O_PLATFORM_NAME.toString());
        httpRequest.putHeader("user-agent", this.pxAPI.getDeviceUA());
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("origin", "https://www.walmart.com");
        httpRequest.putHeader("sec-fetch-site", "same-site");
        httpRequest.putHeader("sec-fetch-mode", "cors");
        httpRequest.putHeader("sec-fetch-dest", "empty");
        httpRequest.putHeader("referer", "https://www.walmart.com/");
        httpRequest.putHeader("accept-encoding", "gzip, deflate");
        httpRequest.putHeader("accept-language", this.pxAPI.getDeviceLang());
        return httpRequest;
    }

    @Override
    public void setLoggedIn(boolean bl) {
        this.loggedIn = true;
    }

    @Override
    public boolean isLoggedIn() {
        return this.loggedIn;
    }

    @Override
    public HttpRequest homepage() {
        HttpRequest httpRequest = this.client.getAbs("https://www.walmart.com/").as(BodyCodec.buffer());
        boolean bl = ThreadLocalRandom.current().nextBoolean();
        if (!bl) {
            httpRequest.putHeader("cache-control", "max-age=0");
        }
        httpRequest.putHeader("sec-ch-ua", this.pxAPI.getDeviceSecUA());
        httpRequest.putHeader("sec-ch-ua-mobile", "?0");
        httpRequest.putHeader("sec-ch-ua-platform", "\"Windows\"");
        httpRequest.putHeader("upgrade-insecure-requests", "1");
        httpRequest.putHeader("user-agent", this.pxAPI.getDeviceUA());
        httpRequest.putHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        if (!bl) {
            httpRequest.putHeader("sec-fetch-site", "none");
            httpRequest.putHeader("sec-fetch-mode", "navigate");
            httpRequest.putHeader("sec-fetch-user", "?1");
            httpRequest.putHeader("sec-fetch-dest", "document");
        } else {
            httpRequest.putHeader("sec-fetch-site", "cross-site");
            httpRequest.putHeader("sec-fetch-mode", "navigate");
            httpRequest.putHeader("sec-fetch-user", "?1");
            httpRequest.putHeader("sec-fetch-dest", "document");
            httpRequest.putHeader("referer", "https://www.google.com/");
        }
        httpRequest.putHeader("accept-encoding", "gzip, deflate, br");
        httpRequest.putHeader("accept-language", this.pxAPI.getDeviceLang());
        return httpRequest;
    }

    @Override
    public CompletableFuture initialisePX() {
        CompletableFuture completableFuture = this.pxAPI.initialise();
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> WalmartNewAPI.async$initialisePX(this, completableFuture2, 1, arg_0));
        }
        completableFuture.join();
        return CompletableFuture.completedFuture(true);
    }
}

