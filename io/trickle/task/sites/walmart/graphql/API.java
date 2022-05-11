/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.util.AsciiString
 *  io.trickle.account.Account
 *  io.trickle.account.AccountController
 *  io.trickle.core.Controller
 *  io.trickle.core.Engine
 *  io.trickle.task.Task
 *  io.trickle.task.antibot.impl.px.PerimeterX
 *  io.trickle.task.sites.walmart.graphql.WalmartNewAPI
 *  io.trickle.task.sites.walmart.util.PaymentToken
 *  io.trickle.util.Utils
 *  io.trickle.webclient.ClientType
 *  io.trickle.webclient.CookieJar
 *  io.trickle.webclient.TaskApiClient
 *  io.vertx.core.buffer.Buffer
 *  io.vertx.core.json.JsonObject
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.client.HttpResponse
 */
package io.trickle.task.sites.walmart.graphql;

import io.netty.util.AsciiString;
import io.trickle.account.Account;
import io.trickle.account.AccountController;
import io.trickle.core.Controller;
import io.trickle.core.Engine;
import io.trickle.task.Task;
import io.trickle.task.antibot.impl.px.PerimeterX;
import io.trickle.task.sites.walmart.graphql.WalmartNewAPI;
import io.trickle.task.sites.walmart.util.PaymentToken;
import io.trickle.util.Utils;
import io.trickle.webclient.ClientType;
import io.trickle.webclient.CookieJar;
import io.trickle.webclient.TaskApiClient;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

public abstract class API
extends TaskApiClient {
    public static String[] crossSiteList;
    public static int EXCEPTION_RETRY_DELAY;
    public static CharSequence DEVICE_PROFILE_REF;
    public static CharSequence X_O_PLATFORM_NAME;
    public String platformVersion;
    public String referer;
    public static CharSequence X_LATENCY_TRACE;
    public static CharSequence SERVER;
    public String searchQuery;
    public boolean loggedIn = false;
    public static CharSequence X_O_GQL_QUERY;
    public Integer storeID = 5880;
    public static Pattern VID_LOCATION_PATTERN;
    public String paymentId;
    public static CharSequence X_APOLLO_OPERATION_NAME;
    public static CharSequence TRUE;
    public String contractId;
    public static DateTimeFormatter GMT_CHROME_RFC1123;
    public String cartId;
    public static CharSequence X_O_PLATFORM_VERSION;
    public static CharSequence WM_MP;
    public String crossSite;
    public static CharSequence X_ENABLE_SERVER_TIMING;
    public static CharSequence X_O_PLATFORM;
    public static CharSequence X_O_CCM;
    public static CharSequence OAOH;
    public String productReferer;
    public String addressId;
    public static String BYPASS_PREFIX;
    public Task task;
    public static CharSequence X_O_SEGMENT;
    public String tpPhase = ThreadLocalRandom.current().nextBoolean() ? "tp3" : "tp5";
    public static CharSequence X_O_CORRELATION_ID;
    public static CharSequence WM_CORRELATION_ID;
    public String accessPointId;
    public String deviceProfileId = WalmartNewAPI.zs2y((int)36);
    public static String[] searchQueries;
    public static CharSequence WM_PAGE_URL;

    public abstract JsonObject getDeliveryAddressesForm();

    public abstract HttpRequest buyNowSubmitPayment();

    public abstract HttpRequest setAddressID();

    public abstract JsonObject PCIDForm();

    public abstract HttpRequest getAccountPage();

    public abstract HttpRequest queuePage(String var1);

    public abstract Buffer getContractForm(String var1);

    public API(ClientType clientType) {
        super(clientType);
    }

    static {
        BYPASS_PREFIX = "";
        EXCEPTION_RETRY_DELAY = 12000;
        VID_LOCATION_PATTERN = Pattern.compile("vid=(.*?)&");
        GMT_CHROME_RFC1123 = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH);
        OAOH = AsciiString.cached((String)"oaoh");
        X_O_PLATFORM_NAME = AsciiString.cached((String)"rweb");
        X_O_CORRELATION_ID = AsciiString.cached((String)"x-o-correlation-id");
        DEVICE_PROFILE_REF = AsciiString.cached((String)"device_profile_ref_id");
        X_LATENCY_TRACE = AsciiString.cached((String)"x-latency-trace");
        WM_MP = AsciiString.cached((String)"wm_mp");
        TRUE = AsciiString.cached((String)"true");
        X_O_PLATFORM_VERSION = AsciiString.cached((String)"x-o-platform-version");
        X_O_SEGMENT = AsciiString.cached((String)"x-o-segment");
        X_O_GQL_QUERY = AsciiString.cached((String)"x-o-gql-query");
        WM_PAGE_URL = AsciiString.cached((String)"wm_page_url");
        X_APOLLO_OPERATION_NAME = AsciiString.cached((String)"x-apollo-operation-name");
        X_O_PLATFORM = AsciiString.cached((String)"x-o-platform");
        X_ENABLE_SERVER_TIMING = AsciiString.cached((String)"x-enable-server-timing");
        X_O_CCM = AsciiString.cached((String)"x-o-ccm");
        SERVER = AsciiString.cached((String)"server");
        WM_CORRELATION_ID = AsciiString.cached((String)"wm_qos.correlation_id");
        crossSiteList = new String[]{"www.reddit.com", "www.facebook.com", "t.co"};
        searchQueries = new String[]{"ps5", "xbox", "cleaner", "clorox", "water", "toaster", "puzzle", "game", "table", "cards", "panini", "laundry", "pokemon", "watch", "keyboard", "phone", "apple", "laptop", "guitar", "shirt", "chair", "pan", "socks", "pool", "toothpaste", "lotion"};
    }

    public abstract HttpRequest finalizeShipping();

    public abstract HttpRequest getCart();

    public abstract boolean isLoggedIn();

    public abstract HttpRequest terraFirma(String var1, boolean var2);

    public abstract JsonObject accountCreateForm();

    public abstract JsonObject finalizeShippingForm(String var1);

    public abstract HttpRequest updateTender();

    public abstract HttpRequest setPayment();

    public abstract HttpRequest getDeliveryAddresses();

    public abstract Buffer getAccountPageForm();

    public static String genTraceparent() {
        return "00-" + Utils.secureHexstring((int)16) + "-" + Utils.secureHexstring((int)8) + "-00";
    }

    public abstract void setLoggedIn(boolean var1);

    public static Account rotateAccount() {
        return ((AccountController)Engine.get().getModule(Controller.ACCOUNT)).getAccountCyclic();
    }

    public abstract JsonObject buyNowBody();

    public abstract HttpRequest validate();

    public abstract JsonObject buyNowPreloadBody();

    public abstract void setAPI(PerimeterX var1);

    public abstract CompletableFuture generatePX(boolean var1);

    public abstract JsonObject setAddressIdForm(String var1, String var2);

    public abstract JsonObject saveAddressJson();

    public abstract HttpRequest saveAddress();

    public abstract PerimeterX getPxAPI();

    public abstract HttpRequest product(String var1);

    public abstract HttpRequest getPCID();

    public abstract HttpRequest getCheckoutPage();

    public abstract HttpRequest addToCart();

    public abstract JsonObject getPaymentForm(PaymentToken var1);

    public abstract HttpRequest loginAccount();

    public abstract HttpRequest submitBilling();

    public abstract HttpRequest issueTicket(String var1);

    public abstract HttpRequest createAccount();

    public abstract JsonObject getBillingForm(PaymentToken var1);

    public abstract HttpRequest buyNow();

    public abstract HttpRequest fulfillment();

    public abstract CompletableFuture handleBadResponse(HttpResponse var1, String var2);

    public abstract JsonObject tenderUpdateForm(String var1);

    public abstract JsonObject buyNowSubmitPaymentBody(String var1);

    public abstract CookieJar cookieStore();

    public abstract CompletableFuture initialisePX();

    public abstract JsonObject createContractBody();

    public abstract HttpRequest getContract();

    public static void main(String[] stringArray) {
        System.out.println(API.genTraceparent());
    }

    public abstract HttpRequest affilCrossSite(String var1);

    public abstract Buffer setPaymentForm(String var1);

    public abstract JsonObject fulfillmentBody();

    public abstract HttpRequest createContract();

    public abstract HttpRequest homepage();

    public abstract HttpRequest submitPayment();

    public abstract JsonObject atcForm();

    public abstract JsonObject accountLoginForm(Account var1);

    public abstract JsonObject getCartQuery();
}
