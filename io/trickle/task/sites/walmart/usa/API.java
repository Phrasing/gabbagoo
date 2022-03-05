/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.json.JsonObject
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.client.HttpResponse
 */
package io.trickle.task.sites.walmart.usa;

import io.trickle.account.Account;
import io.trickle.account.AccountController;
import io.trickle.core.Controller;
import io.trickle.core.Engine;
import io.trickle.task.Task;
import io.trickle.task.antibot.impl.px.PerimeterX;
import io.trickle.task.sites.walmart.util.PaymentToken;
import io.trickle.webclient.ClientType;
import io.trickle.webclient.CookieJar;
import io.trickle.webclient.TaskApiClient;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import java.util.concurrent.CompletableFuture;

public abstract class API
extends TaskApiClient {
    public static String[] searchQueries;
    public static String BYPASS_PREFIX;
    public static String[] crossSiteList;

    static {
        BYPASS_PREFIX = "";
        crossSiteList = new String[]{"www.reddit.com", "www.facebook.com", "t.co"};
        searchQueries = new String[]{"ps5", "xbox", "cleaner", "clorox", "water", "toaster", "puzzle", "game", "table", "cards", "panini", "laundry", "pokemon", "watch", "keyboard", "phone", "apple", "laptop", "guitar", "shirt", "chair", "pan", "socks", "pool", "toothpaste", "lotion"};
    }

    public abstract HttpRequest getCheckoutPage();

    public abstract JsonObject getShippingForm(JsonObject var1);

    public abstract void swapClient();

    public abstract JsonObject getShippingRateForm(JsonObject var1);

    public abstract HttpRequest terraFirma(String var1, boolean var2);

    public abstract HttpRequest getCart();

    public abstract Task getTask();

    public abstract HttpRequest selectShipping();

    public abstract JsonObject getPaymentForm(PaymentToken var1);

    public abstract JsonObject accountLoginForm(Account var1);

    public abstract HttpRequest submitBilling();

    public abstract void setLoggedIn(boolean var1);

    public abstract HttpRequest submitShipping();

    public abstract CookieJar cookieStore();

    public abstract PerimeterX getPxAPI();

    public abstract HttpRequest submitPayment();

    public abstract HttpRequest savedCart();

    public abstract JsonObject getProcessingForm();

    public abstract CompletableFuture generatePX(boolean var1);

    public API(ClientType clientType) {
        super(clientType);
    }

    public abstract HttpRequest processPayment(PaymentToken var1);

    public abstract HttpRequest addToCart();

    public abstract HttpRequest createAccount();

    public abstract HttpRequest getCartV3(String var1);

    public abstract HttpRequest homepage();

    public abstract HttpRequest getPCID(PaymentToken var1);

    public abstract HttpRequest loginAccount();

    public abstract JsonObject accountCreateForm();

    public abstract JsonObject atcForm();

    public abstract void setAPI(PerimeterX var1);

    public abstract JsonObject PCIDForm();

    public static Account rotateAccount() {
        return ((AccountController)Engine.get().getModule(Controller.ACCOUNT)).getAccountCyclic();
    }

    public abstract JsonObject getAtcForm(String var1, int var2);

    public abstract CompletableFuture initialisePX();

    public abstract JsonObject getProcessingForm(PaymentToken var1);

    public abstract HttpRequest affilCrossSite(String var1);

    public abstract boolean isLoggedIn();

    public abstract HttpRequest transferCart(String var1);

    public abstract CompletableFuture handleBadResponse(int var1, HttpResponse var2);

    public abstract JsonObject getBillingForm(PaymentToken var1);
}

