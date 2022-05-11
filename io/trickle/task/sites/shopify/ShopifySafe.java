/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.task.Task
 *  io.trickle.task.sites.Site
 *  io.trickle.task.sites.shopify.Presolver
 *  io.trickle.task.sites.shopify.Shopify
 *  io.trickle.task.sites.shopify.util.ShippingRateSupplier
 *  io.trickle.task.sites.shopify.util.SiteParser
 *  io.trickle.task.sites.shopify.util.Triplet
 *  io.trickle.task.sites.shopify.util.VariantHandler
 *  io.trickle.util.analytics.Analytics
 *  io.trickle.util.concurrent.VertxUtil
 *  io.trickle.util.request.Request
 *  io.vertx.core.json.JsonArray
 *  io.vertx.core.json.JsonObject
 *  io.vertx.ext.web.client.HttpRequest
 *  org.apache.logging.log4j.Logger
 */
package io.trickle.task.sites.shopify;

import io.trickle.task.Task;
import io.trickle.task.sites.Site;
import io.trickle.task.sites.shopify.Presolver;
import io.trickle.task.sites.shopify.Shopify;
import io.trickle.task.sites.shopify.util.ShippingRateSupplier;
import io.trickle.task.sites.shopify.util.SiteParser;
import io.trickle.task.sites.shopify.util.Triplet;
import io.trickle.task.sites.shopify.util.VariantHandler;
import io.trickle.util.analytics.Analytics;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import java.lang.invoke.LambdaMetafactory;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import org.apache.logging.log4j.Logger;

public class ShopifySafe
extends Shopify {
    public boolean isPreload;
    public boolean fastPreload;
    public int id;
    public boolean isContactpreload;

    public Triplet findPreset() {
        if (this.task.getSite() != Site.HUMANMADE) return null;
        return new Triplet((Object)"40044994953252", (Object)true, null);
    }

    public CompletableFuture preloadContactOnly() {
        String string;
        CompletableFuture completableFuture = this.fetchProductsJSON(false);
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preloadContactOnly(this, completableFuture2, null, null, null, null, 1, arg_0));
        }
        JsonArray jsonArray = ((JsonObject)completableFuture.join()).getJsonArray("products");
        CompletableFuture completableFuture3 = VariantHandler.findPrecartVariant((JsonArray)jsonArray);
        if (!completableFuture3.isDone()) {
            CompletableFuture completableFuture4 = completableFuture3;
            return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preloadContactOnly(this, completableFuture4, jsonArray, null, null, null, 2, arg_0));
        }
        Triplet triplet = (Triplet)completableFuture3.join();
        if (triplet == null) {
            this.isContactpreload = false;
            this.logger.error("There is no item to preload. Handling...");
            return CompletableFuture.completedFuture(null);
        }
        this.shippingRate = new CompletableFuture();
        this.precartItemName = string = (String)triplet.first;
        if (this.task.getSite() == Site.KITH) {
            CompletableFuture completableFuture5 = this.atcBasic(string);
            if (!completableFuture5.isDone()) {
                CompletableFuture completableFuture6 = completableFuture5;
                return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preloadContactOnly(this, completableFuture6, jsonArray, triplet, string, null, 3, arg_0));
            }
            completableFuture5.join();
        } else {
            CompletableFuture completableFuture7 = this.atcAJAX(string, false);
            if (!completableFuture7.isDone()) {
                CompletableFuture completableFuture8 = completableFuture7;
                return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preloadContactOnly(this, completableFuture8, jsonArray, triplet, string, null, 4, arg_0));
            }
            completableFuture7.join();
        }
        CompletableFuture completableFuture9 = this.genCheckoutURL(false);
        if (!completableFuture9.isDone()) {
            CompletableFuture completableFuture10 = completableFuture9;
            return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preloadContactOnly(this, completableFuture10, jsonArray, triplet, string, null, 5, arg_0));
        }
        String string2 = (String)completableFuture9.join();
        CompletableFuture completableFuture11 = this.handlePreload(string2);
        if (!completableFuture11.isDone()) {
            CompletableFuture completableFuture12 = completableFuture11;
            return ((CompletableFuture)completableFuture12.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preloadContactOnly(this, completableFuture12, jsonArray, triplet, string, string2, 6, arg_0));
        }
        string2 = (String)completableFuture11.join();
        if (!((Boolean)triplet.second).booleanValue()) {
            this.api.setOOS();
            this.isContactpreload = false;
        } else {
            CompletableFuture completableFuture13 = this.getCheckoutURL(string2);
            if (!completableFuture13.isDone()) {
                CompletableFuture completableFuture14 = completableFuture13;
                return ((CompletableFuture)completableFuture14.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preloadContactOnly(this, completableFuture14, jsonArray, triplet, string, string2, 7, arg_0));
            }
            completableFuture13.join();
            if (this.api.getCookies().contains("_shopify_checkpoint")) {
                CompletableFuture completableFuture15 = this.checkCaptcha(string2);
                if (!completableFuture15.isDone()) {
                    CompletableFuture completableFuture16 = completableFuture15;
                    return ((CompletableFuture)completableFuture16.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preloadContactOnly(this, completableFuture16, jsonArray, triplet, string, string2, 8, arg_0));
                }
                completableFuture15.join();
            }
            CompletableFuture completableFuture17 = this.submitContact(string2);
            if (!completableFuture17.isDone()) {
                CompletableFuture completableFuture18 = completableFuture17;
                return ((CompletableFuture)completableFuture18.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preloadContactOnly(this, completableFuture18, jsonArray, triplet, string, string2, 9, arg_0));
            }
            completableFuture17.join();
            this.isContactpreload = true;
        }
        CompletableFuture completableFuture19 = this.clearWithChangeJS();
        if (!completableFuture19.isDone()) {
            CompletableFuture completableFuture20 = completableFuture19;
            return ((CompletableFuture)completableFuture20.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preloadContactOnly(this, completableFuture20, jsonArray, triplet, string, string2, 10, arg_0));
        }
        completableFuture19.join();
        CompletableFuture completableFuture21 = this.checkCart();
        if (!completableFuture21.isDone()) {
            CompletableFuture completableFuture22 = completableFuture21;
            return ((CompletableFuture)completableFuture22.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preloadContactOnly(this, completableFuture22, jsonArray, triplet, string, string2, 11, arg_0));
        }
        completableFuture21.join();
        CompletableFuture completableFuture23 = this.confirmClear(string2);
        if (!completableFuture23.isDone()) {
            CompletableFuture completableFuture24 = completableFuture23;
            return ((CompletableFuture)completableFuture24.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preloadContactOnly(this, completableFuture24, jsonArray, triplet, string, string2, 12, arg_0));
        }
        completableFuture23.join();
        this.api.checkIsOOS();
        this.configureShippingRate();
        return CompletableFuture.completedFuture(string2);
    }

    public CompletableFuture preload() {
        CompletableFuture completableFuture = this.fetchProductsJSON(false);
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preload(this, completableFuture2, null, null, 0, null, null, null, null, null, 1, arg_0));
        }
        JsonArray jsonArray = ((JsonObject)completableFuture.join()).getJsonArray("products");
        String string = null;
        int n = 0;
        while (this.api.getWebClient().isActive() && n++ < 5000000) {
            String string2;
            Triplet triplet;
            Triplet triplet2;
            if (n == 1 && this.findPreset() != null) {
                triplet2 = this.findPreset();
            } else {
                CompletableFuture completableFuture3 = VariantHandler.findPrecartVariant((JsonArray)jsonArray);
                if (!completableFuture3.isDone()) {
                    CompletableFuture completableFuture4 = completableFuture3;
                    return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preload(this, completableFuture4, jsonArray, string, n, null, null, null, null, null, 2, arg_0));
                }
                triplet2 = triplet = (Triplet)completableFuture3.join();
            }
            if (triplet == null) {
                this.isPreload = false;
                this.logger.error("There is no item to preload. Handling...");
                return CompletableFuture.completedFuture(null);
            }
            this.shippingRate = new CompletableFuture();
            this.precartItemName = string2 = (String)triplet.first;
            if (this.task.getSite() == Site.KITH) {
                CompletableFuture completableFuture5 = this.atcBasic(string2);
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preload(this, completableFuture6, jsonArray, string, n, triplet, string2, null, null, null, 3, arg_0));
                }
                completableFuture5.join();
            } else {
                CompletableFuture completableFuture7 = this.atcAJAX(string2, false);
                if (!completableFuture7.isDone()) {
                    CompletableFuture completableFuture8 = completableFuture7;
                    return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preload(this, completableFuture8, jsonArray, string, n, triplet, string2, null, null, null, 4, arg_0));
                }
                completableFuture7.join();
            }
            CompletableFuture completableFuture9 = this.genCheckoutURLViaCart();
            if (!completableFuture9.isDone()) {
                CompletableFuture completableFuture10 = completableFuture9;
                return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preload(this, completableFuture10, jsonArray, string, n, triplet, string2, null, null, null, 5, arg_0));
            }
            string = (String)completableFuture9.join();
            CompletableFuture completableFuture11 = this.handlePreload(string);
            if (!completableFuture11.isDone()) {
                CompletableFuture completableFuture12 = completableFuture11;
                return ((CompletableFuture)completableFuture12.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preload(this, completableFuture12, jsonArray, string, n, triplet, string2, null, null, null, 6, arg_0));
            }
            string = (String)completableFuture11.join();
            if (!((Boolean)triplet.second).booleanValue()) {
                this.isPreload = false;
                break;
            }
            CompletableFuture completableFuture13 = this.getCheckoutURL(string);
            if (!completableFuture13.isDone()) {
                CompletableFuture completableFuture14 = completableFuture13;
                return ((CompletableFuture)completableFuture14.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preload(this, completableFuture14, jsonArray, string, n, triplet, string2, null, null, null, 7, arg_0));
            }
            completableFuture13.join();
            if (this.api.getCookies().contains("_shopify_checkpoint")) {
                CompletableFuture completableFuture15 = this.checkCaptcha(string);
                if (!completableFuture15.isDone()) {
                    CompletableFuture completableFuture16 = completableFuture15;
                    return ((CompletableFuture)completableFuture16.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preload(this, completableFuture16, jsonArray, string, n, triplet, string2, null, null, null, 8, arg_0));
                }
                completableFuture15.join();
            }
            CompletableFuture completableFuture17 = this.submitContact(string);
            if (!completableFuture17.isDone()) {
                CompletableFuture completableFuture18 = completableFuture17;
                return ((CompletableFuture)completableFuture18.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preload(this, completableFuture18, jsonArray, string, n, triplet, string2, null, null, null, 9, arg_0));
            }
            completableFuture17.join();
            CompletableFuture completableFuture19 = this.getShippingRateOld();
            CompletableFuture<Void> completableFuture20 = CompletableFuture.allOf(completableFuture19, this.getShippingPage(string));
            if (!completableFuture20.isDone()) {
                CompletableFuture<Void> completableFuture21 = completableFuture20;
                return ((CompletableFuture)completableFuture21.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preload(this, completableFuture19, jsonArray, string, n, triplet, string2, completableFuture21, null, null, 10, arg_0));
            }
            completableFuture20.join();
            CompletableFuture completableFuture22 = this.shippingRate;
            if (!completableFuture22.isDone()) {
                CompletableFuture completableFuture23 = completableFuture22;
                return ((CompletableFuture)completableFuture23.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preload(this, completableFuture19, jsonArray, string, n, triplet, string2, completableFuture23, null, null, 11, arg_0));
            }
            if (!((ShippingRateSupplier)completableFuture22.join()).get().equals(this.task.getShippingRate()) && this.task.getShippingRate().length() > 3) {
                Logger logger = this.logger;
                CompletableFuture completableFuture24 = this.shippingRate;
                if (!completableFuture24.isDone()) {
                    CompletableFuture completableFuture25 = completableFuture24;
                    String string3 = "The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]";
                    Logger logger2 = logger;
                    return ((CompletableFuture)completableFuture25.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preload(this, completableFuture19, jsonArray, string, n, triplet, string2, completableFuture25, logger2, string3, 12, arg_0));
                }
                logger.error("The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]", (Object)((ShippingRateSupplier)completableFuture24.join()).get(), (Object)this.task.getShippingRate());
                CompletableFuture completableFuture26 = this.clearWithChangeJS();
                if (!completableFuture26.isDone()) {
                    CompletableFuture completableFuture27 = completableFuture26;
                    return ((CompletableFuture)completableFuture27.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preload(this, completableFuture19, jsonArray, string, n, triplet, string2, completableFuture27, null, null, 13, arg_0));
                }
                completableFuture26.join();
                this.shippingRate = new CompletableFuture();
                if (jsonArray.isEmpty()) {
                    this.logger.info("There are no items with matching shipping rate. Handling...");
                    this.api.checkIsOOS();
                    this.configureShippingRate();
                    this.isPreload = false;
                    this.isContactpreload = true;
                    return CompletableFuture.completedFuture(string);
                }
                try {
                    jsonArray.remove(triplet.third);
                }
                catch (Exception exception) {}
                continue;
            }
            CompletableFuture completableFuture28 = this.submitShipping(string);
            if (!completableFuture28.isDone()) {
                CompletableFuture completableFuture29 = completableFuture28;
                return ((CompletableFuture)completableFuture29.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preload(this, completableFuture19, jsonArray, string, n, triplet, string2, completableFuture29, null, null, 14, arg_0));
            }
            completableFuture28.join();
            break;
        }
        CompletableFuture completableFuture30 = this.clearWithChangeJS();
        if (!completableFuture30.isDone()) {
            CompletableFuture completableFuture31 = completableFuture30;
            return ((CompletableFuture)completableFuture31.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preload(this, completableFuture31, jsonArray, string, n, null, null, null, null, null, 15, arg_0));
        }
        completableFuture30.join();
        CompletableFuture completableFuture32 = this.checkCart();
        if (!completableFuture32.isDone()) {
            CompletableFuture completableFuture33 = completableFuture32;
            return ((CompletableFuture)completableFuture33.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preload(this, completableFuture33, jsonArray, string, n, null, null, null, null, null, 16, arg_0));
        }
        completableFuture32.join();
        this.api.checkIsOOS();
        this.configureShippingRate();
        return CompletableFuture.completedFuture(string);
    }

    public CompletableFuture preloadWallets() {
        CompletableFuture completableFuture = this.fetchProductsJSON(false);
        if (!completableFuture.isDone()) {
            CompletableFuture completableFuture2 = completableFuture;
            return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preloadWallets(this, completableFuture2, null, null, null, null, null, null, null, 1, arg_0));
        }
        JsonArray jsonArray = ((JsonObject)completableFuture.join()).getJsonArray("products");
        String string = null;
        while (this.api.getWebClient().isActive()) {
            String string2;
            CompletableFuture completableFuture3 = VariantHandler.findPrecartVariant((JsonArray)jsonArray);
            if (!completableFuture3.isDone()) {
                CompletableFuture completableFuture4 = completableFuture3;
                return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preloadWallets(this, completableFuture4, jsonArray, string, null, null, null, null, null, 2, arg_0));
            }
            Triplet triplet = (Triplet)completableFuture3.join();
            if (triplet == null) {
                CompletableFuture completableFuture5 = this.waitTilCartCookie();
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preloadWallets(this, completableFuture6, jsonArray, string, triplet, null, null, null, null, 3, arg_0));
                }
                completableFuture5.join();
                CompletableFuture completableFuture7 = this.walletsGenCheckout();
                if (!completableFuture7.isDone()) {
                    CompletableFuture completableFuture8 = completableFuture7;
                    return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preloadWallets(this, completableFuture8, jsonArray, string, triplet, null, null, null, null, 4, arg_0));
                }
                string = (String)completableFuture7.join();
                CompletableFuture completableFuture9 = this.handlePreload(string);
                if (!completableFuture9.isDone()) {
                    CompletableFuture completableFuture10 = completableFuture9;
                    return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preloadWallets(this, completableFuture10, jsonArray, string, triplet, null, null, null, null, 5, arg_0));
                }
                string = (String)completableFuture9.join();
                this.isPreload = false;
                this.isContactpreload = false;
                return CompletableFuture.completedFuture(string);
            }
            this.shippingRate = new CompletableFuture();
            this.precartItemName = string2 = (String)triplet.first;
            if (this.task.getSite() == Site.KITH) {
                CompletableFuture completableFuture11 = this.atcBasic(string2);
                if (!completableFuture11.isDone()) {
                    CompletableFuture completableFuture12 = completableFuture11;
                    return ((CompletableFuture)completableFuture12.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preloadWallets(this, completableFuture12, jsonArray, string, triplet, string2, null, null, null, 6, arg_0));
                }
                completableFuture11.join();
            } else {
                CompletableFuture completableFuture13 = this.atcAJAX(string2, false);
                if (!completableFuture13.isDone()) {
                    CompletableFuture completableFuture14 = completableFuture13;
                    return ((CompletableFuture)completableFuture14.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preloadWallets(this, completableFuture14, jsonArray, string, triplet, string2, null, null, null, 7, arg_0));
                }
                completableFuture13.join();
            }
            CompletableFuture completableFuture15 = this.walletsGenCheckout();
            if (!completableFuture15.isDone()) {
                CompletableFuture completableFuture16 = completableFuture15;
                return ((CompletableFuture)completableFuture16.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preloadWallets(this, completableFuture16, jsonArray, string, triplet, string2, null, null, null, 8, arg_0));
            }
            string = (String)completableFuture15.join();
            CompletableFuture completableFuture17 = this.handlePreload(string);
            if (!completableFuture17.isDone()) {
                CompletableFuture completableFuture18 = completableFuture17;
                return ((CompletableFuture)completableFuture18.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preloadWallets(this, completableFuture18, jsonArray, string, triplet, string2, null, null, null, 9, arg_0));
            }
            string = (String)completableFuture17.join();
            if (!((Boolean)triplet.second).booleanValue()) {
                this.isPreload = false;
                break;
            }
            CompletableFuture completableFuture19 = this.getCheckoutURL(string);
            if (!completableFuture19.isDone()) {
                CompletableFuture completableFuture20 = completableFuture19;
                return ((CompletableFuture)completableFuture20.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preloadWallets(this, completableFuture20, jsonArray, string, triplet, string2, null, null, null, 10, arg_0));
            }
            completableFuture19.join();
            if (this.api.getCookies().contains("_shopify_checkpoint")) {
                CompletableFuture completableFuture21 = this.checkCaptcha(string);
                if (!completableFuture21.isDone()) {
                    CompletableFuture completableFuture22 = completableFuture21;
                    return ((CompletableFuture)completableFuture22.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preloadWallets(this, completableFuture22, jsonArray, string, triplet, string2, null, null, null, 11, arg_0));
                }
                completableFuture21.join();
            }
            CompletableFuture completableFuture23 = this.submitContact(string);
            if (!completableFuture23.isDone()) {
                CompletableFuture completableFuture24 = completableFuture23;
                return ((CompletableFuture)completableFuture24.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preloadWallets(this, completableFuture24, jsonArray, string, triplet, string2, null, null, null, 12, arg_0));
            }
            completableFuture23.join();
            CompletableFuture completableFuture25 = this.getShippingRateOld();
            CompletableFuture<Void> completableFuture26 = CompletableFuture.allOf(completableFuture25, this.getShippingPage(string));
            if (!completableFuture26.isDone()) {
                CompletableFuture<Void> completableFuture27 = completableFuture26;
                return ((CompletableFuture)completableFuture27.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preloadWallets(this, completableFuture25, jsonArray, string, triplet, string2, completableFuture27, null, null, 13, arg_0));
            }
            completableFuture26.join();
            CompletableFuture completableFuture28 = this.shippingRate;
            if (!completableFuture28.isDone()) {
                CompletableFuture completableFuture29 = completableFuture28;
                return ((CompletableFuture)completableFuture29.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preloadWallets(this, completableFuture25, jsonArray, string, triplet, string2, completableFuture29, null, null, 14, arg_0));
            }
            if (!((ShippingRateSupplier)completableFuture28.join()).get().equals(this.task.getShippingRate()) && this.task.getShippingRate().length() > 3) {
                Logger logger = this.logger;
                CompletableFuture completableFuture30 = this.shippingRate;
                if (!completableFuture30.isDone()) {
                    CompletableFuture completableFuture31 = completableFuture30;
                    String string3 = "The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]";
                    Logger logger2 = logger;
                    return ((CompletableFuture)completableFuture31.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preloadWallets(this, completableFuture25, jsonArray, string, triplet, string2, completableFuture31, logger2, string3, 15, arg_0));
                }
                logger.error("The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]", (Object)((ShippingRateSupplier)completableFuture30.join()).get(), (Object)this.task.getShippingRate());
                CompletableFuture completableFuture32 = this.clearWithChangeJS();
                if (!completableFuture32.isDone()) {
                    CompletableFuture completableFuture33 = completableFuture32;
                    return ((CompletableFuture)completableFuture33.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preloadWallets(this, completableFuture25, jsonArray, string, triplet, string2, completableFuture33, null, null, 16, arg_0));
                }
                completableFuture32.join();
                this.shippingRate = new CompletableFuture();
                if (jsonArray.isEmpty()) {
                    this.logger.info("There are no items with matching shipping rate. Handling...");
                    this.api.checkIsOOS();
                    this.configureShippingRate();
                    this.isPreload = false;
                    this.isContactpreload = true;
                    return CompletableFuture.completedFuture(string);
                }
                jsonArray.remove(triplet.third);
                continue;
            }
            CompletableFuture completableFuture34 = this.submitShipping(string);
            if (!completableFuture34.isDone()) {
                CompletableFuture completableFuture35 = completableFuture34;
                return ((CompletableFuture)completableFuture35.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preloadWallets(this, completableFuture25, jsonArray, string, triplet, string2, completableFuture35, null, null, 17, arg_0));
            }
            completableFuture34.join();
            break;
        }
        CompletableFuture completableFuture36 = this.clearWithChangeJS();
        if (!completableFuture36.isDone()) {
            CompletableFuture completableFuture37 = completableFuture36;
            return ((CompletableFuture)completableFuture37.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preloadWallets(this, completableFuture37, jsonArray, string, null, null, null, null, null, 18, arg_0));
        }
        completableFuture36.join();
        CompletableFuture completableFuture38 = this.checkCart();
        if (!completableFuture38.isDone()) {
            CompletableFuture completableFuture39 = completableFuture38;
            return ((CompletableFuture)completableFuture39.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$preloadWallets(this, completableFuture39, jsonArray, string, null, null, null, null, null, 19, arg_0));
        }
        completableFuture38.join();
        this.api.checkIsOOS();
        this.configureShippingRate();
        return CompletableFuture.completedFuture(string);
    }

    public CompletableFuture run() {
        int n = 1;
        String string = null;
        if (!this.isRestockMode) {
            try {
                this.vertx.setPeriodic(TimeUnit.SECONDS.toMillis(this.isPreload ? 8L : 15L), this::lambda$run$0);
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        try {
            Object object;
            Object object2;
            CompletableFuture completableFuture = ShopifySafe.initHarvesters();
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, string, completableFuture2, null, null, null, 1, arg_0));
            }
            completableFuture.join();
            this.paymentGateway = SiteParser.getGatewayFromSite((Site)this.task.getSite(), (boolean)this.isCod);
            this.fastPreload = this.task.getMode().contains("fast") && this.paymentGateway != null;
            CompletableFuture completableFuture3 = this.initShopDetails();
            if (!completableFuture3.isDone()) {
                CompletableFuture completableFuture4 = completableFuture3;
                return ((CompletableFuture)completableFuture4.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, string, completableFuture4, null, null, null, 2, arg_0));
            }
            completableFuture3.join();
            if (this.task.getMode().contains("login")) {
                this.logger.info("Login required. Logging in...");
                CompletableFuture completableFuture5 = this.login();
                if (!completableFuture5.isDone()) {
                    CompletableFuture completableFuture6 = completableFuture5;
                    return ((CompletableFuture)completableFuture6.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, string, completableFuture6, null, null, null, 3, arg_0));
                }
                completableFuture5.join();
            }
            this.presolver = new Presolver((Shopify)this);
            if (!this.isExtra) {
                if (this.isPreload) {
                    CompletableFuture completableFuture7 = this.preload();
                    if (!completableFuture7.isDone()) {
                        CompletableFuture completableFuture8 = completableFuture7;
                        return ((CompletableFuture)completableFuture8.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, string, completableFuture8, null, null, null, 4, arg_0));
                    }
                    string = (String)completableFuture7.join();
                } else {
                    CompletableFuture completableFuture9 = this.preloadContactOnly();
                    if (!completableFuture9.isDone()) {
                        CompletableFuture completableFuture10 = completableFuture9;
                        return ((CompletableFuture)completableFuture10.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, string, completableFuture10, null, null, null, 5, arg_0));
                    }
                    string = (String)completableFuture9.join();
                }
            } else if (this.isPreload) {
                CompletableFuture completableFuture11 = this.preloadWallets();
                if (!completableFuture11.isDone()) {
                    CompletableFuture completableFuture12 = completableFuture11;
                    return ((CompletableFuture)completableFuture12.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, string, completableFuture12, null, null, null, 6, arg_0));
                }
                string = (String)completableFuture11.join();
            }
            if (!this.api.getCookies().contains("_shopify_checkpoint") && HARVESTERS.length > 0) {
                this.cpMonitor = this.presolver.run();
            }
            if ((this.isPreload || this.isContactpreload) && this.isSmart) {
                this.monitorQueue(string);
            }
            if (!this.isKeyword) {
                if (this.isEL) {
                    CompletableFuture completableFuture13 = this.fetchELJS();
                    if (!completableFuture13.isDone()) {
                        CompletableFuture completableFuture14 = completableFuture13;
                        return ((CompletableFuture)completableFuture14.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, string, completableFuture14, null, null, null, 9, arg_0));
                    }
                    object2 = (JsonObject)completableFuture13.join();
                    this.instanceSignal = VariantHandler.selectVariantFromLink((JsonObject)object2, (String)this.task.getSize(), (Shopify)this);
                }
            } else {
                object2 = null;
                while (object2 == null) {
                    CompletableFuture completableFuture15 = this.fetchProductsJSON(true);
                    if (!completableFuture15.isDone()) {
                        CompletableFuture completableFuture16 = completableFuture15;
                        return ((CompletableFuture)completableFuture16.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, string, completableFuture16, (String)object2, null, null, 7, arg_0));
                    }
                    object = (JsonObject)completableFuture15.join();
                    object2 = VariantHandler.selectVariantFromKeyword((JsonObject)object, (String)this.task.getSize(), (List)this.positiveKeywords, (List)this.negativeKeywords, (Shopify)this);
                    if (object2 == null) {
                        CompletableFuture completableFuture17 = VertxUtil.signalSleep((String)this.instanceSignal, (long)this.task.getMonitorDelay());
                        if (!completableFuture17.isDone()) {
                            CompletableFuture completableFuture18 = completableFuture17;
                            return ((CompletableFuture)completableFuture18.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, string, completableFuture18, (String)object2, (JsonObject)object, null, 8, arg_0));
                        }
                        Object t = completableFuture17.join();
                        if (t == null) continue;
                        object2 = VariantHandler.selectVariantFromKeyword((JsonObject)((JsonObject)t), (String)this.task.getSize(), (List)this.positiveKeywords, (List)this.negativeKeywords, (Shopify)this);
                        continue;
                    }
                    VertxUtil.sendSignal((String)this.instanceSignal, (Object)object);
                }
                this.instanceSignal = object2;
            }
            this.isSmart = false;
            this.setProductPickupTime();
            this.genPaymentToken();
            while (this.api.getWebClient().isActive()) {
                try {
                    if (string == null && (this.isExtra || n != 0)) {
                        CompletableFuture completableFuture19 = this.shippingAndBillingPost();
                        if (!completableFuture19.isDone()) {
                            CompletableFuture completableFuture20 = completableFuture19;
                            return ((CompletableFuture)completableFuture20.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, string, completableFuture20, null, null, null, 10, arg_0));
                        }
                        string = (String)completableFuture19.join();
                        Analytics.carts.increment();
                        CompletableFuture completableFuture21 = this.handlePreload(string);
                        if (!completableFuture21.isDone()) {
                            CompletableFuture completableFuture22 = completableFuture21;
                            return ((CompletableFuture)completableFuture22.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, string, completableFuture22, null, null, null, 11, arg_0));
                        }
                        string = (String)completableFuture21.join();
                        CompletableFuture completableFuture23 = this.getCheckoutURL(string);
                        if (!completableFuture23.isDone()) {
                            CompletableFuture completableFuture24 = completableFuture23;
                            return ((CompletableFuture)completableFuture24.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, string, completableFuture24, null, null, null, 12, arg_0));
                        }
                        completableFuture23.join();
                        this.shutOffPresolver(false);
                        CompletableFuture completableFuture25 = this.submitContact(string);
                        if (!completableFuture25.isDone()) {
                            CompletableFuture completableFuture26 = completableFuture25;
                            return ((CompletableFuture)completableFuture26.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, string, completableFuture26, null, null, null, 13, arg_0));
                        }
                        completableFuture25.join();
                        CompletableFuture completableFuture27 = this.walletsSubmitShipping(string);
                        if (!completableFuture27.isDone()) {
                            CompletableFuture completableFuture28 = completableFuture27;
                            return ((CompletableFuture)completableFuture28.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, string, completableFuture28, null, null, null, 14, arg_0));
                        }
                        completableFuture27.join();
                    } else {
                        CompletableFuture completableFuture29 = this.smoothCart();
                        if (!completableFuture29.isDone()) {
                            CompletableFuture completableFuture30 = completableFuture29;
                            return ((CompletableFuture)completableFuture30.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, string, completableFuture30, null, null, null, 15, arg_0));
                        }
                        completableFuture29.join();
                        Analytics.carts.increment();
                        if (string == null) {
                            this.genPaymentToken();
                            CompletableFuture completableFuture31 = this.genCheckoutURLViaCart();
                            if (!completableFuture31.isDone()) {
                                CompletableFuture completableFuture32 = completableFuture31;
                                return ((CompletableFuture)completableFuture32.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, string, completableFuture32, null, null, null, 16, arg_0));
                            }
                            string = (String)completableFuture31.join();
                            CompletableFuture completableFuture33 = this.handlePreload(string);
                            if (!completableFuture33.isDone()) {
                                CompletableFuture completableFuture34 = completableFuture33;
                                return ((CompletableFuture)completableFuture34.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, string, completableFuture34, null, null, null, 17, arg_0));
                            }
                            string = (String)completableFuture33.join();
                            CompletableFuture completableFuture35 = this.getCheckoutURL(string);
                            if (!completableFuture35.isDone()) {
                                CompletableFuture completableFuture36 = completableFuture35;
                                return ((CompletableFuture)completableFuture36.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, string, completableFuture36, null, null, null, 18, arg_0));
                            }
                            completableFuture35.join();
                            this.shutOffPresolver(false);
                            CompletableFuture completableFuture37 = this.submitContact(string);
                            if (!completableFuture37.isDone()) {
                                CompletableFuture completableFuture38 = completableFuture37;
                                return ((CompletableFuture)completableFuture38.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, string, completableFuture38, null, null, null, 19, arg_0));
                            }
                            completableFuture37.join();
                            CompletableFuture completableFuture39 = this.walletsSubmitShipping(string);
                            if (!completableFuture39.isDone()) {
                                CompletableFuture completableFuture40 = completableFuture39;
                                return ((CompletableFuture)completableFuture40.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, string, completableFuture40, null, null, null, 20, arg_0));
                            }
                            completableFuture39.join();
                        } else if (!this.isPreload && !this.isContactpreload) {
                            CompletableFuture completableFuture41 = this.getCheckoutURL(string);
                            if (!completableFuture41.isDone()) {
                                CompletableFuture completableFuture42 = completableFuture41;
                                return ((CompletableFuture)completableFuture42.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, string, completableFuture42, null, null, null, 21, arg_0));
                            }
                            object2 = (String)completableFuture41.join();
                            this.shutOffPresolver(false);
                            if (this.precartItemName != null && !this.precartItemName.equals(this.instanceSignal) && ((String)object2).contains(this.precartItemName)) {
                                this.logger.error("Please notify the developer of cart bug. Thank you [{}]!", (Object)this.precartItemName);
                                CompletableFuture completableFuture43 = VertxUtil.hardCodedSleep((long)Integer.MAX_VALUE);
                                if (!completableFuture43.isDone()) {
                                    CompletableFuture completableFuture44 = completableFuture43;
                                    return ((CompletableFuture)completableFuture44.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, string, completableFuture44, (String)object2, null, null, 22, arg_0));
                                }
                                completableFuture43.join();
                                CompletableFuture completableFuture45 = VertxUtil.hardCodedSleep((long)Integer.MAX_VALUE);
                                if (!completableFuture45.isDone()) {
                                    CompletableFuture completableFuture46 = completableFuture45;
                                    return ((CompletableFuture)completableFuture46.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, string, completableFuture46, (String)object2, null, null, 23, arg_0));
                                }
                                completableFuture45.join();
                            }
                            CompletableFuture completableFuture47 = this.submitContact(string);
                            if (!completableFuture47.isDone()) {
                                CompletableFuture completableFuture48 = completableFuture47;
                                return ((CompletableFuture)completableFuture48.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, string, completableFuture48, (String)object2, null, null, 24, arg_0));
                            }
                            completableFuture47.join();
                            CompletableFuture completableFuture49 = this.walletsSubmitShipping(string);
                            if (!completableFuture49.isDone()) {
                                CompletableFuture completableFuture50 = completableFuture49;
                                return ((CompletableFuture)completableFuture50.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, string, completableFuture50, (String)object2, null, null, 25, arg_0));
                            }
                            completableFuture49.join();
                        }
                    }
                    if (this.isContactpreload) {
                        CompletableFuture completableFuture51 = this.genCheckoutURLViaCart();
                        if (!completableFuture51.isDone()) {
                            CompletableFuture completableFuture52 = completableFuture51;
                            return ((CompletableFuture)completableFuture52.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, string, completableFuture52, null, null, null, 26, arg_0));
                        }
                        completableFuture51.join();
                        this.shutOffPresolver(false);
                        CompletableFuture completableFuture53 = this.walletsSubmitShipping(string);
                        if (!completableFuture53.isDone()) {
                            CompletableFuture completableFuture54 = completableFuture53;
                            return ((CompletableFuture)completableFuture54.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, string, completableFuture54, null, null, null, 27, arg_0));
                        }
                        object2 = (String)completableFuture53.join();
                        if (!this.precartItemName.equals(this.instanceSignal) && ((String)object2).contains(this.precartItemName)) {
                            this.logger.error("Please notify the developer of cart bug. Thank you [{}]!", (Object)this.precartItemName);
                            CompletableFuture completableFuture55 = VertxUtil.hardCodedSleep((long)Integer.MAX_VALUE);
                            if (!completableFuture55.isDone()) {
                                CompletableFuture completableFuture56 = completableFuture55;
                                return ((CompletableFuture)completableFuture56.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, string, completableFuture56, (String)object2, null, null, 28, arg_0));
                            }
                            completableFuture55.join();
                            CompletableFuture completableFuture57 = VertxUtil.hardCodedSleep((long)Integer.MAX_VALUE);
                            if (!completableFuture57.isDone()) {
                                CompletableFuture completableFuture58 = completableFuture57;
                                return ((CompletableFuture)completableFuture58.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, string, completableFuture58, (String)object2, null, null, 29, arg_0));
                            }
                            completableFuture57.join();
                        }
                    }
                    if (this.isPreload || this.paymentGateway == null || this.api.getCookies().contains("_shopify_checkpoint") || this.isEarly || !this.isPreload && !this.isContactpreload || this.isRestockMode) {
                        CompletableFuture completableFuture59 = this.getProcessingPage(string, false);
                        if (!completableFuture59.isDone()) {
                            CompletableFuture completableFuture60 = completableFuture59;
                            return ((CompletableFuture)completableFuture60.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, string, completableFuture60, null, null, null, 30, arg_0));
                        }
                        object2 = (String)completableFuture59.join();
                        if (!this.precartItemName.equals(this.instanceSignal) && ((String)object2).contains(this.precartItemName)) {
                            this.logger.error("Please notify the developer of cart bug. Thank you [{}]!", (Object)this.precartItemName);
                            CompletableFuture completableFuture61 = VertxUtil.hardCodedSleep((long)Integer.MAX_VALUE);
                            if (!completableFuture61.isDone()) {
                                CompletableFuture completableFuture62 = completableFuture61;
                                return ((CompletableFuture)completableFuture62.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, string, completableFuture62, (String)object2, null, null, 31, arg_0));
                            }
                            completableFuture61.join();
                            CompletableFuture completableFuture63 = VertxUtil.hardCodedSleep((long)Integer.MAX_VALUE);
                            if (!completableFuture63.isDone()) {
                                CompletableFuture completableFuture64 = completableFuture63;
                                return ((CompletableFuture)completableFuture64.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, string, completableFuture64, (String)object2, null, null, 32, arg_0));
                            }
                            completableFuture63.join();
                        }
                    }
                    do {
                        CompletableFuture completableFuture65 = this.processPayment(string);
                        if (!completableFuture65.isDone()) {
                            CompletableFuture completableFuture66 = completableFuture65;
                            return ((CompletableFuture)completableFuture66.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, string, completableFuture66, null, null, null, 33, arg_0));
                        }
                        completableFuture65.join();
                        CompletableFuture completableFuture67 = this.checkOrderAPI(string);
                        if (!completableFuture67.isDone()) {
                            CompletableFuture completableFuture68 = completableFuture67;
                            return ((CompletableFuture)completableFuture68.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, string, completableFuture68, null, null, null, 34, arg_0));
                        }
                        object2 = (JsonObject)completableFuture67.join();
                        object = ShopifySafe.parseDecline((JsonObject)object2);
                        if (object == null || ((String)object).equals("success")) continue;
                        Analytics.failure((String)object, (Task)this.task, (JsonObject)object2, (String)this.api.proxyStringSafe());
                        this.logger.info("Checkout fail -> " + (String)object);
                    } while (object != null && !((String)object).equals("success"));
                    this.logger.info("Successfully checked out.");
                    Analytics.success((Task)this.task, (JsonObject)object2, (String)this.api.proxyStringSafe());
                    break;
                }
                catch (Throwable throwable) {
                    this.logger.error(throwable.getMessage());
                    this.setAttributes();
                    this.isPreload = false;
                    this.isContactpreload = false;
                    n = 0;
                    string = null;
                }
            }
            return null;
        }
        catch (Throwable throwable) {
            this.logger.error("Task interrupted: " + throwable.getMessage());
            this.setAttributes();
            this.isPreload = false;
            this.isContactpreload = false;
            CompletableFuture completableFuture = VertxUtil.hardCodedSleep((long)1000L);
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture69 = completableFuture;
                return ((CompletableFuture)completableFuture69.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$run(this, n, string, completableFuture69, null, null, throwable, 35, arg_0));
            }
            completableFuture.join();
            return this.run();
        }
        return null;
    }

    public void lambda$run$0(Long l) {
        Request.execute((HttpRequest)this.api.getWebClient().getAbs("https://www.google.com/favicon.ico").putHeader("user-agent", this.api.UA));
    }

    public CompletableFuture createQueue() {
        int n = 0;
        while (n++ < Integer.MAX_VALUE) {
            CompletableFuture completableFuture = this.attemptGenCheckoutUrl(true);
            if (!completableFuture.isDone()) {
                CompletableFuture completableFuture2 = completableFuture;
                return ((CompletableFuture)completableFuture2.exceptionally(Function.identity())).thenCompose(arg_0 -> ShopifySafe.async$createQueue(this, n, completableFuture2, 1, arg_0));
            }
            completableFuture.join();
            this.api.getCookies().clear();
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * Exception decompiling
     */
    public static CompletableFuture async$run(ShopifySafe var0, int var1_1, String var2_2, CompletableFuture var3_3, String var4_7, JsonObject var5_8, Throwable var6_9, int var7_10, Object var8_11) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [78[DOLOOP]], but top level block is 80[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doClass(Driver.java:84)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:78)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompile(CFRDecompiler.java:91)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:122)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.decompileSaveAll(ResourceDecompiling.java:262)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$decompileSaveAll$0(ResourceDecompiling.java:127)
         *     at java.base/java.lang.Thread.run(Thread.java:833)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public ShopifySafe(Task task, int n) {
        super(task, n);
        this.id = n;
        this.isPreload = this.task.getMode().contains("preload");
        this.isSmart = this.task.getMode().contains("smart");
        this.isContactpreload = false;
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    public static CompletableFuture async$preload(ShopifySafe var0, CompletableFuture var1_1, JsonArray var2_2, String var3_3, int var4_6, Triplet var5_9, String var6_10, CompletableFuture var7_11, Logger var8_13, String var9_14, int var10_15, Object var11_17) {
        switch (var10_15) {
            case 0: {
                v0 = var0.fetchProductsJSON(false);
                if (!v0.isDone()) {
                    var8_13 = v0;
                    return var8_13.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preload(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String int io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var8_13, null, null, (int)0, null, null, null, null, null, (int)1));
                }
                ** GOTO lbl10
            }
            case 1: {
                v0 = var1_1;
lbl10:
                // 2 sources

                var1_1 = ((JsonObject)v0.join()).getJsonArray("products");
                var2_2 /* !! */  = null;
                var3_4 = 0;
lbl13:
                // 3 sources

                while (var0.api.getWebClient().isActive() && var3_4++ < 5000000) {
                    if (var3_4 != 1 || var0.findPreset() == null) ** GOTO lbl17
                    v1 = var0.findPreset();
                    ** GOTO lbl30
lbl17:
                    // 1 sources

                    v2 = VariantHandler.findPrecartVariant((JsonArray)var1_1);
                    if (!v2.isDone()) {
                        var8_13 = v2;
                        return var8_13.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preload(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String int io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var8_13, (JsonArray)var1_1, (String)var2_2 /* !! */ , (int)var3_4, null, null, null, null, null, (int)2));
                    }
                    ** GOTO lbl29
                }
                ** GOTO lbl259
            }
            case 2: {
                v2 = var1_1;
                v3 = var2_2 /* !! */ ;
                var3_4 = var4_6;
                var2_2 /* !! */  = var3_3;
                var1_1 = v3;
lbl29:
                // 2 sources

                v1 = var4_7 = (Triplet)v2.join();
lbl30:
                // 2 sources

                if (var4_7 == null) {
                    var0.isPreload = false;
                    var0.logger.error("There is no item to preload. Handling...");
                    return CompletableFuture.completedFuture(null);
                }
                var0.shippingRate = new CompletableFuture<T>();
                var5_9 /* !! */  = (String)var4_7.first;
                var0.precartItemName = var5_9 /* !! */ ;
                if (var0.task.getSite() != Site.KITH) ** GOTO lbl43
                v4 = var0.atcBasic((String)var5_9 /* !! */ );
                if (!v4.isDone()) {
                    var8_13 = v4;
                    return var8_13.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preload(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String int io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var8_13, (JsonArray)var1_1, (String)var2_2 /* !! */ , (int)var3_4, (Triplet)var4_7, (String)var5_9 /* !! */ , null, null, null, (int)3));
                }
                ** GOTO lbl57
lbl43:
                // 1 sources

                v5 = var0.atcAJAX((String)var5_9 /* !! */ , false);
                if (!v5.isDone()) {
                    var8_13 = v5;
                    return var8_13.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preload(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String int io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var8_13, (JsonArray)var1_1, (String)var2_2 /* !! */ , (int)var3_4, (Triplet)var4_7, (String)var5_9 /* !! */ , null, null, null, (int)4));
                }
                ** GOTO lbl72
            }
            case 3: {
                v4 = var1_1;
                v6 = var2_2 /* !! */ ;
                v7 = var5_9 /* !! */ ;
                var5_9 /* !! */  = var6_10;
                var4_7 = v7;
                var3_4 = var4_6;
                var2_2 /* !! */  = var3_3;
                var1_1 = v6;
lbl57:
                // 2 sources

                v4.join();
lbl59:
                // 2 sources

                while (!(v8 = var0.genCheckoutURLViaCart()).isDone()) {
                    var8_13 = v8;
                    return var8_13.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preload(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String int io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var8_13, (JsonArray)var1_1, (String)var2_2 /* !! */ , (int)var3_4, (Triplet)var4_7, (String)var5_9 /* !! */ , null, null, null, (int)5));
                }
                ** GOTO lbl84
            }
            case 4: {
                v5 = var1_1;
                v9 = var2_2 /* !! */ ;
                v10 = var5_9 /* !! */ ;
                var5_9 /* !! */  = var6_10;
                var4_7 = v10;
                var3_4 = var4_6;
                var2_2 /* !! */  = var3_3;
                var1_1 = v9;
lbl72:
                // 2 sources

                v5.join();
                ** GOTO lbl59
            }
            case 5: {
                v8 = var1_1;
                v11 = var2_2 /* !! */ ;
                v12 = var5_9 /* !! */ ;
                var5_9 /* !! */  = var6_10;
                var4_7 = v12;
                var3_4 = var4_6;
                var2_2 /* !! */  = var3_3;
                var1_1 = v11;
lbl84:
                // 2 sources

                if (!(v13 = var0.handlePreload((String)(var2_2 /* !! */  = (String)v8.join()))).isDone()) {
                    var8_13 = v13;
                    return var8_13.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preload(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String int io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var8_13, (JsonArray)var1_1, (String)var2_2 /* !! */ , (int)var3_4, (Triplet)var4_7, (String)var5_9 /* !! */ , null, null, null, (int)6));
                }
                ** GOTO lbl97
            }
            case 6: {
                v13 = var1_1;
                v14 = var2_2 /* !! */ ;
                v15 = var5_9 /* !! */ ;
                var5_9 /* !! */  = var6_10;
                var4_7 = v15;
                var3_4 = var4_6;
                var2_2 /* !! */  = var3_3;
                var1_1 = v14;
lbl97:
                // 2 sources

                var2_2 /* !! */  = (String)v13.join();
                if (((Boolean)var4_7.second).booleanValue()) ** GOTO lbl101
                var0.isPreload = false;
                ** GOTO lbl259
lbl101:
                // 1 sources

                v16 = var0.getCheckoutURL((String)var2_2 /* !! */ );
                if (!v16.isDone()) {
                    var8_13 = v16;
                    return var8_13.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preload(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String int io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var8_13, (JsonArray)var1_1, (String)var2_2 /* !! */ , (int)var3_4, (Triplet)var4_7, (String)var5_9 /* !! */ , null, null, null, (int)7));
                }
                ** GOTO lbl115
            }
            case 7: {
                v16 = var1_1;
                v17 = var2_2 /* !! */ ;
                v18 = var5_9 /* !! */ ;
                var5_9 /* !! */  = var6_10;
                var4_7 = v18;
                var3_4 = var4_6;
                var2_2 /* !! */  = var3_3;
                var1_1 = v17;
lbl115:
                // 2 sources

                v16.join();
                if (!var0.api.getCookies().contains("_shopify_checkpoint")) ** GOTO lbl134
                v19 = var0.checkCaptcha((String)var2_2 /* !! */ );
                if (!v19.isDone()) {
                    var8_13 = v19;
                    return var8_13.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preload(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String int io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var8_13, (JsonArray)var1_1, (String)var2_2 /* !! */ , (int)var3_4, (Triplet)var4_7, (String)var5_9 /* !! */ , null, null, null, (int)8));
                }
                ** GOTO lbl132
            }
            case 8: {
                v19 = var1_1;
                v20 = var2_2 /* !! */ ;
                v21 = var5_9 /* !! */ ;
                var5_9 /* !! */  = var6_10;
                var4_7 = v21;
                var3_4 = var4_6;
                var2_2 /* !! */  = var3_3;
                var1_1 = v20;
lbl132:
                // 2 sources

                v19.join();
lbl134:
                // 2 sources

                if (!(v22 = var0.submitContact((String)var2_2 /* !! */ )).isDone()) {
                    var8_13 = v22;
                    return var8_13.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preload(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String int io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var8_13, (JsonArray)var1_1, (String)var2_2 /* !! */ , (int)var3_4, (Triplet)var4_7, (String)var5_9 /* !! */ , null, null, null, (int)9));
                }
                ** GOTO lbl147
            }
            case 9: {
                v22 = var1_1;
                v23 = var2_2 /* !! */ ;
                v24 = var5_9 /* !! */ ;
                var5_9 /* !! */  = var6_10;
                var4_7 = v24;
                var3_4 = var4_6;
                var2_2 /* !! */  = var3_3;
                var1_1 = v23;
lbl147:
                // 2 sources

                v22.join();
                var6_10 = var0.getShippingRateOld();
                v25 = CompletableFuture.allOf(new CompletableFuture[]{var6_10, var0.getShippingPage((String)var2_2 /* !! */ )});
                if (!v25.isDone()) {
                    var8_13 = v25;
                    return var8_13.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preload(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String int io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var6_10, (JsonArray)var1_1, (String)var2_2 /* !! */ , (int)var3_4, (Triplet)var4_7, (String)var5_9 /* !! */ , (CompletableFuture)var8_13, null, null, (int)10));
                }
                ** GOTO lbl166
            }
            case 10: {
                v25 = var7_11;
                v26 = var2_2 /* !! */ ;
                v27 = var5_9 /* !! */ ;
                v28 = var6_10;
                var6_10 = var1_1;
                var5_9 /* !! */  = v28;
                var4_7 = v27;
                var3_4 = var4_6;
                var2_2 /* !! */  = var3_3;
                var1_1 = v26;
lbl166:
                // 2 sources

                v25.join();
                v29 = var0.shippingRate;
                if (!v29.isDone()) {
                    var8_13 = v29;
                    return var8_13.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preload(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String int io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var6_10, (JsonArray)var1_1, (String)var2_2 /* !! */ , (int)var3_4, (Triplet)var4_7, (String)var5_9 /* !! */ , (CompletableFuture)var8_13, null, null, (int)11));
                }
                ** GOTO lbl184
            }
            case 11: {
                v29 = var7_11;
                v30 = var2_2 /* !! */ ;
                v31 = var5_9 /* !! */ ;
                v32 = var6_10;
                var6_10 = var1_1;
                var5_9 /* !! */  = v32;
                var4_7 = v31;
                var3_4 = var4_6;
                var2_2 /* !! */  = var3_3;
                var1_1 = v30;
lbl184:
                // 2 sources

                if (((ShippingRateSupplier)v29.join()).get().equals(var0.task.getShippingRate()) || var0.task.getShippingRate().length() <= 3) ** GOTO lbl194
                v33 = var0.logger;
                v34 = "The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]";
                v35 = var0.shippingRate;
                if (!v35.isDone()) {
                    var10_16 = v35;
                    var9_14 = v34;
                    var8_13 = v33;
                    return var10_16.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preload(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String int io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var6_10, (JsonArray)var1_1, (String)var2_2 /* !! */ , (int)var3_4, (Triplet)var4_7, (String)var5_9 /* !! */ , (CompletableFuture)var10_16, (Logger)var8_13, (String)var9_14, (int)12));
                }
                ** GOTO lbl212
lbl194:
                // 1 sources

                v36 = var0.submitShipping((String)var2_2 /* !! */ );
                if (!v36.isDone()) {
                    var8_13 = v36;
                    return var8_13.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preload(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String int io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var6_10, (JsonArray)var1_1, (String)var2_2 /* !! */ , (int)var3_4, (Triplet)var4_7, (String)var5_9 /* !! */ , (CompletableFuture)var8_13, null, null, (int)14));
                }
                ** GOTO lbl257
            }
            case 12: {
                v33 = var8_13;
                v34 = var9_14;
                v35 = var7_11;
                v37 = var2_2 /* !! */ ;
                v38 /* !! */  = var5_9 /* !! */ ;
                v39 = var6_10;
                var6_10 = var1_1;
                var5_9 /* !! */  = v39;
                var4_7 = v38 /* !! */ ;
                var3_4 = var4_6;
                var2_2 /* !! */  = var3_3;
                var1_1 = v37;
lbl212:
                // 2 sources

                v33.error(v34, (Object)((ShippingRateSupplier)v35.join()).get(), (Object)var0.task.getShippingRate());
                v40 = var0.clearWithChangeJS();
                if (!v40.isDone()) {
                    var8_13 = v40;
                    return var8_13.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preload(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String int io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var6_10, (JsonArray)var1_1, (String)var2_2 /* !! */ , (int)var3_4, (Triplet)var4_7, (String)var5_9 /* !! */ , (CompletableFuture)var8_13, null, null, (int)13));
                }
                ** GOTO lbl229
            }
            case 13: {
                v40 = var7_11;
                v41 = var2_2 /* !! */ ;
                v42 = var5_9 /* !! */ ;
                v43 = var6_10;
                var6_10 = var1_1;
                var5_9 /* !! */  = v43;
                var4_7 = v42;
                var3_4 = var4_6;
                var2_2 /* !! */  = var3_3;
                var1_1 = v41;
lbl229:
                // 2 sources

                v40.join();
                var0.shippingRate = new CompletableFuture<T>();
                if (var1_1.isEmpty()) {
                    var0.logger.info("There are no items with matching shipping rate. Handling...");
                    var0.api.checkIsOOS();
                    var0.configureShippingRate();
                    var0.isPreload = false;
                    var0.isContactpreload = true;
                    return CompletableFuture.completedFuture(var2_2 /* !! */ );
                }
                try {
                    var1_1.remove(var4_7.third);
                }
                catch (Exception var7_12) {}
                ** GOTO lbl13
            }
            case 14: {
                v36 = var7_11;
                v44 = var2_2 /* !! */ ;
                v45 = var5_9 /* !! */ ;
                v46 = var6_10;
                var6_10 = var1_1;
                var5_9 /* !! */  = v46;
                var4_8 = v45;
                var3_4 = var4_6;
                var2_2 /* !! */  = var3_3;
                var1_1 = v44;
lbl257:
                // 2 sources

                v36.join();
lbl259:
                // 3 sources

                if (!(v47 = var0.clearWithChangeJS()).isDone()) {
                    var8_13 = v47;
                    return var8_13.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preload(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String int io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var8_13, (JsonArray)var1_1, (String)var2_2 /* !! */ , (int)var3_4, null, null, null, null, null, (int)15));
                }
                ** GOTO lbl269
            }
            case 15: {
                v47 = var1_1;
                v48 = var2_2 /* !! */ ;
                var3_4 = var4_6;
                var2_2 /* !! */  = var3_3;
                var1_1 = v48;
lbl269:
                // 2 sources

                v47.join();
                v49 = var0.checkCart();
                if (!v49.isDone()) {
                    var8_13 = v49;
                    return var8_13.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preload(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String int io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var8_13, (JsonArray)var1_1, (String)var2_2 /* !! */ , (int)var3_4, null, null, null, null, null, (int)16));
                }
                ** GOTO lbl282
            }
            case 16: {
                v49 = var1_1;
                v50 = var2_2 /* !! */ ;
                var3_5 = var4_6;
                var2_2 /* !! */  = var3_3;
                var1_1 = v50;
lbl282:
                // 2 sources

                v49.join();
                var0.api.checkIsOOS();
                var0.configureShippingRate();
                return CompletableFuture.completedFuture(var2_2 /* !! */ );
            }
        }
        throw new IllegalArgumentException();
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    public static CompletableFuture async$preloadWallets(ShopifySafe var0, CompletableFuture var1_1, JsonArray var2_2, String var3_3, Triplet var4_4, String var5_5, CompletableFuture var6_6, Logger var7_7, String var8_8, int var9_9, Object var10_10) {
        switch (var9_9) {
            case 0: {
                v0 = var0.fetchProductsJSON(false);
                if (!v0.isDone()) {
                    var6_6 = v0;
                    return var6_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preloadWallets(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var6_6, null, null, null, null, null, null, null, (int)1));
                }
                ** GOTO lbl10
            }
            case 1: {
                v0 = var1_1;
lbl10:
                // 2 sources

                var1_1 = ((JsonObject)v0.join()).getJsonArray("products");
                var2_2 /* !! */  = null;
lbl12:
                // 2 sources

                while (var0.api.getWebClient().isActive()) {
                    v1 = VariantHandler.findPrecartVariant((JsonArray)var1_1);
                    if (!v1.isDone()) {
                        var6_6 = v1;
                        return var6_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preloadWallets(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var6_6, (JsonArray)var1_1, (String)var2_2 /* !! */ , null, null, null, null, null, (int)2));
                    }
                    ** GOTO lbl24
                }
                ** GOTO lbl288
            }
            case 2: {
                v1 = var1_1;
                v2 = var2_2 /* !! */ ;
                var2_2 /* !! */  = var3_3;
                var1_1 = v2;
lbl24:
                // 2 sources

                if ((var3_3 = (Triplet)v1.join()) != null) ** GOTO lbl30
                v3 = var0.waitTilCartCookie();
                if (!v3.isDone()) {
                    var6_6 = v3;
                    return var6_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preloadWallets(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, var6_6, (JsonArray)var1_1, (String)var2_2 /* !! */ , (Triplet)var3_3, null, null, null, null, (int)3));
                }
                ** GOTO lbl51
lbl30:
                // 1 sources

                var0.shippingRate = new CompletableFuture<T>();
                var4_4 /* !! */  = (String)var3_3.first;
                var0.precartItemName = var4_4 /* !! */ ;
                if (var0.task.getSite() != Site.KITH) ** GOTO lbl39
                v4 = var0.atcBasic((String)var4_4 /* !! */ );
                if (!v4.isDone()) {
                    var6_6 = v4;
                    return var6_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preloadWallets(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var6_6, (JsonArray)var1_1, (String)var2_2 /* !! */ , (Triplet)var3_3, (String)var4_4 /* !! */ , null, null, null, (int)6));
                }
                ** GOTO lbl89
lbl39:
                // 1 sources

                v5 = var0.atcAJAX((String)var4_4 /* !! */ , false);
                if (!v5.isDone()) {
                    var6_6 = v5;
                    return var6_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preloadWallets(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var6_6, (JsonArray)var1_1, (String)var2_2 /* !! */ , (Triplet)var3_3, (String)var4_4 /* !! */ , null, null, null, (int)7));
                }
                ** GOTO lbl104
            }
            case 3: {
                v3 = var1_1;
                v6 = var2_2 /* !! */ ;
                v7 = var3_3;
                var3_3 = var4_4 /* !! */ ;
                var2_2 /* !! */  = v7;
                var1_1 = v6;
lbl51:
                // 2 sources

                v3.join();
                v8 = var0.walletsGenCheckout();
                if (!v8.isDone()) {
                    var6_6 = v8;
                    return var6_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preloadWallets(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, var6_6, (JsonArray)var1_1, (String)var2_2 /* !! */ , (Triplet)var3_3, null, null, null, null, (int)4));
                }
                ** GOTO lbl65
            }
            case 4: {
                v8 = var1_1;
                v9 = var2_2 /* !! */ ;
                v10 = var3_3;
                var3_3 = var4_4 /* !! */ ;
                var2_2 /* !! */  = v10;
                var1_1 = v9;
lbl65:
                // 2 sources

                if (!(v11 = var0.handlePreload((String)(var2_2 /* !! */  = (String)v8.join()))).isDone()) {
                    var6_6 = v11;
                    return var6_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preloadWallets(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, var6_6, (JsonArray)var1_1, (String)var2_2 /* !! */ , (Triplet)var3_3, null, null, null, null, (int)5));
                }
                ** GOTO lbl76
            }
            case 5: {
                v11 = var1_1;
                v12 = var2_2 /* !! */ ;
                v13 = var3_3;
                var3_3 = var4_4 /* !! */ ;
                var2_2 /* !! */  = v13;
                var1_1 = v12;
lbl76:
                // 2 sources

                var2_2 /* !! */  = (String)v11.join();
                var0.isPreload = false;
                var0.isContactpreload = false;
                return CompletableFuture.completedFuture(var2_2 /* !! */ );
            }
            case 6: {
                v4 = var1_1;
                v14 = var2_2 /* !! */ ;
                v15 = var3_3;
                v16 = var4_4 /* !! */ ;
                var4_4 /* !! */  = var5_5;
                var3_3 = v16;
                var2_2 /* !! */  = v15;
                var1_1 = v14;
lbl89:
                // 2 sources

                v4.join();
lbl91:
                // 2 sources

                while (!(v17 = var0.walletsGenCheckout()).isDone()) {
                    var6_6 = v17;
                    return var6_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preloadWallets(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, var6_6, (JsonArray)var1_1, (String)var2_2 /* !! */ , (Triplet)var3_3, (String)var4_4 /* !! */ , null, null, null, (int)8));
                }
                ** GOTO lbl116
            }
            case 7: {
                v5 = var1_1;
                v18 = var2_2 /* !! */ ;
                v19 = var3_3;
                v20 = var4_4 /* !! */ ;
                var4_4 /* !! */  = var5_5;
                var3_3 = v20;
                var2_2 /* !! */  = v19;
                var1_1 = v18;
lbl104:
                // 2 sources

                v5.join();
                ** GOTO lbl91
            }
            case 8: {
                v17 = var1_1;
                v21 = var2_2 /* !! */ ;
                v22 = var3_3;
                v23 = var4_4 /* !! */ ;
                var4_4 /* !! */  = var5_5;
                var3_3 = v23;
                var2_2 /* !! */  = v22;
                var1_1 = v21;
lbl116:
                // 2 sources

                if (!(v24 = var0.handlePreload((String)(var2_2 /* !! */  = (String)v17.join()))).isDone()) {
                    var6_6 = v24;
                    return var6_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preloadWallets(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var6_6, (JsonArray)var1_1, (String)var2_2 /* !! */ , (Triplet)var3_3, (String)var4_4 /* !! */ , null, null, null, (int)9));
                }
                ** GOTO lbl129
            }
            case 9: {
                v24 = var1_1;
                v25 = var2_2 /* !! */ ;
                v26 = var3_3;
                v27 = var4_4 /* !! */ ;
                var4_4 /* !! */  = var5_5;
                var3_3 = v27;
                var2_2 /* !! */  = v26;
                var1_1 = v25;
lbl129:
                // 2 sources

                var2_2 /* !! */  = (String)v24.join();
                if (((Boolean)var3_3.second).booleanValue()) ** GOTO lbl133
                var0.isPreload = false;
                ** GOTO lbl288
lbl133:
                // 1 sources

                v28 = var0.getCheckoutURL((String)var2_2 /* !! */ );
                if (!v28.isDone()) {
                    var6_6 = v28;
                    return var6_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preloadWallets(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var6_6, (JsonArray)var1_1, (String)var2_2 /* !! */ , (Triplet)var3_3, (String)var4_4 /* !! */ , null, null, null, (int)10));
                }
                ** GOTO lbl147
            }
            case 10: {
                v28 = var1_1;
                v29 = var2_2 /* !! */ ;
                v30 = var3_3;
                v31 = var4_4 /* !! */ ;
                var4_4 /* !! */  = var5_5;
                var3_3 = v31;
                var2_2 /* !! */  = v30;
                var1_1 = v29;
lbl147:
                // 2 sources

                v28.join();
                if (!var0.api.getCookies().contains("_shopify_checkpoint")) ** GOTO lbl166
                v32 = var0.checkCaptcha((String)var2_2 /* !! */ );
                if (!v32.isDone()) {
                    var6_6 = v32;
                    return var6_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preloadWallets(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var6_6, (JsonArray)var1_1, (String)var2_2 /* !! */ , (Triplet)var3_3, (String)var4_4 /* !! */ , null, null, null, (int)11));
                }
                ** GOTO lbl164
            }
            case 11: {
                v32 = var1_1;
                v33 = var2_2 /* !! */ ;
                v34 = var3_3;
                v35 = var4_4 /* !! */ ;
                var4_4 /* !! */  = var5_5;
                var3_3 = v35;
                var2_2 /* !! */  = v34;
                var1_1 = v33;
lbl164:
                // 2 sources

                v32.join();
lbl166:
                // 2 sources

                if (!(v36 = var0.submitContact((String)var2_2 /* !! */ )).isDone()) {
                    var6_6 = v36;
                    return var6_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preloadWallets(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var6_6, (JsonArray)var1_1, (String)var2_2 /* !! */ , (Triplet)var3_3, (String)var4_4 /* !! */ , null, null, null, (int)12));
                }
                ** GOTO lbl179
            }
            case 12: {
                v36 = var1_1;
                v37 = var2_2 /* !! */ ;
                v38 = var3_3;
                v39 = var4_4 /* !! */ ;
                var4_4 /* !! */  = var5_5;
                var3_3 = v39;
                var2_2 /* !! */  = v38;
                var1_1 = v37;
lbl179:
                // 2 sources

                v36.join();
                var5_5 = var0.getShippingRateOld();
                v40 = CompletableFuture.allOf(new CompletableFuture[]{var5_5, var0.getShippingPage((String)var2_2 /* !! */ )});
                if (!v40.isDone()) {
                    var6_6 = v40;
                    return var6_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preloadWallets(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var5_5, (JsonArray)var1_1, (String)var2_2 /* !! */ , (Triplet)var3_3, (String)var4_4 /* !! */ , (CompletableFuture)var6_6, null, null, (int)13));
                }
                ** GOTO lbl198
            }
            case 13: {
                v40 = var6_6;
                v41 = var2_2 /* !! */ ;
                v42 = var3_3;
                v43 = var4_4 /* !! */ ;
                v44 = var5_5;
                var5_5 = var1_1;
                var4_4 /* !! */  = v44;
                var3_3 = v43;
                var2_2 /* !! */  = v42;
                var1_1 = v41;
lbl198:
                // 2 sources

                v40.join();
                v45 = var0.shippingRate;
                if (!v45.isDone()) {
                    var6_6 = v45;
                    return var6_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preloadWallets(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var5_5, (JsonArray)var1_1, (String)var2_2 /* !! */ , (Triplet)var3_3, (String)var4_4 /* !! */ , (CompletableFuture)var6_6, null, null, (int)14));
                }
                ** GOTO lbl216
            }
            case 14: {
                v45 = var6_6;
                v46 = var2_2 /* !! */ ;
                v47 = var3_3;
                v48 = var4_4 /* !! */ ;
                v49 = var5_5;
                var5_5 = var1_1;
                var4_4 /* !! */  = v49;
                var3_3 = v48;
                var2_2 /* !! */  = v47;
                var1_1 = v46;
lbl216:
                // 2 sources

                if (((ShippingRateSupplier)v45.join()).get().equals(var0.task.getShippingRate()) || var0.task.getShippingRate().length() <= 3) ** GOTO lbl226
                v50 = var0.logger;
                v51 = "The shipping rate could not be preloaded. Attempting another item current: [{}] user: [{}]";
                v52 = var0.shippingRate;
                if (!v52.isDone()) {
                    var8_8 = v52;
                    var7_7 /* !! */  = v51;
                    var6_6 = v50;
                    return var8_8.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preloadWallets(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var5_5, (JsonArray)var1_1, (String)var2_2 /* !! */ , (Triplet)var3_3, (String)var4_4 /* !! */ , (CompletableFuture)var8_8, (Logger)var6_6, (String)var7_7 /* !! */ , (int)15));
                }
                ** GOTO lbl244
lbl226:
                // 1 sources

                v53 = var0.submitShipping((String)var2_2 /* !! */ );
                if (!v53.isDone()) {
                    var6_6 = v53;
                    return var6_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preloadWallets(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var5_5, (JsonArray)var1_1, (String)var2_2 /* !! */ , (Triplet)var3_3, (String)var4_4 /* !! */ , (CompletableFuture)var6_6, null, null, (int)17));
                }
                ** GOTO lbl286
            }
            case 15: {
                v50 = var7_7 /* !! */ ;
                v51 = var8_8;
                v52 = var6_6;
                v54 /* !! */  = var2_2 /* !! */ ;
                v55 = var3_3;
                v56 /* !! */  = var4_4 /* !! */ ;
                v57 = var5_5;
                var5_5 = var1_1;
                var4_4 /* !! */  = v57;
                var3_3 = v56 /* !! */ ;
                var2_2 /* !! */  = v55;
                var1_1 = v54 /* !! */ ;
lbl244:
                // 2 sources

                v50.error(v51, (Object)((ShippingRateSupplier)v52.join()).get(), (Object)var0.task.getShippingRate());
                v58 = var0.clearWithChangeJS();
                if (!v58.isDone()) {
                    var6_6 = v58;
                    return var6_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preloadWallets(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var5_5, (JsonArray)var1_1, (String)var2_2 /* !! */ , (Triplet)var3_3, (String)var4_4 /* !! */ , (CompletableFuture)var6_6, null, null, (int)16));
                }
                ** GOTO lbl261
            }
            case 16: {
                v58 = var6_6;
                v59 = var2_2 /* !! */ ;
                v60 = var3_3;
                v61 = var4_4 /* !! */ ;
                v62 = var5_5;
                var5_5 = var1_1;
                var4_4 /* !! */  = v62;
                var3_3 = v61;
                var2_2 /* !! */  = v60;
                var1_1 = v59;
lbl261:
                // 2 sources

                v58.join();
                var0.shippingRate = new CompletableFuture<T>();
                if (var1_1.isEmpty()) {
                    var0.logger.info("There are no items with matching shipping rate. Handling...");
                    var0.api.checkIsOOS();
                    var0.configureShippingRate();
                    var0.isPreload = false;
                    var0.isContactpreload = true;
                    return CompletableFuture.completedFuture(var2_2 /* !! */ );
                }
                var1_1.remove(var3_3.third);
                ** GOTO lbl12
            }
            case 17: {
                v53 = var6_6;
                v63 = var2_2 /* !! */ ;
                v64 = var3_3;
                v65 = var4_4 /* !! */ ;
                v66 = var5_5;
                var5_5 = var1_1;
                var4_4 /* !! */  = v66;
                var3_3 = v65;
                var2_2 /* !! */  = v64;
                var1_1 = v63;
lbl286:
                // 2 sources

                v53.join();
lbl288:
                // 3 sources

                if (!(v67 = var0.clearWithChangeJS()).isDone()) {
                    var6_6 = v67;
                    return var6_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preloadWallets(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var6_6, (JsonArray)var1_1, (String)var2_2 /* !! */ , null, null, null, null, null, (int)18));
                }
                ** GOTO lbl297
            }
            case 18: {
                v67 = var1_1;
                v68 = var2_2 /* !! */ ;
                var2_2 /* !! */  = var3_3;
                var1_1 = v68;
lbl297:
                // 2 sources

                v67.join();
                v69 = var0.checkCart();
                if (!v69.isDone()) {
                    var6_6 = v69;
                    return var6_6.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preloadWallets(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray java.lang.String io.trickle.task.sites.shopify.util.Triplet java.lang.String java.util.concurrent.CompletableFuture org.apache.logging.log4j.Logger java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var6_6, (JsonArray)var1_1, (String)var2_2 /* !! */ , null, null, null, null, null, (int)19));
                }
                ** GOTO lbl309
            }
            case 19: {
                v69 = var1_1;
                v70 = var2_2 /* !! */ ;
                var2_2 /* !! */  = var3_3;
                var1_1 = v70;
lbl309:
                // 2 sources

                v69.join();
                var0.api.checkIsOOS();
                var0.configureShippingRate();
                return CompletableFuture.completedFuture(var2_2 /* !! */ );
            }
        }
        throw new IllegalArgumentException();
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    public static CompletableFuture async$preloadContactOnly(ShopifySafe var0, CompletableFuture var1_1, JsonArray var2_2, Triplet var3_3, String var4_4, String var5_5, int var6_6, Object var7_7) {
        switch (var6_6) {
            case 0: {
                v0 = var0.fetchProductsJSON(false);
                if (!v0.isDone()) {
                    var5_5 = v0;
                    return var5_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preloadContactOnly(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray io.trickle.task.sites.shopify.util.Triplet java.lang.String java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var5_5, null, null, null, null, (int)1));
                }
                ** GOTO lbl10
            }
            case 1: {
                v0 = var1_1;
lbl10:
                // 2 sources

                if (!(v1 = VariantHandler.findPrecartVariant((JsonArray)(var1_1 = ((JsonObject)v0.join()).getJsonArray("products")))).isDone()) {
                    var5_5 = v1;
                    return var5_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preloadContactOnly(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray io.trickle.task.sites.shopify.util.Triplet java.lang.String java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var5_5, (JsonArray)var1_1, null, null, null, (int)2));
                }
                ** GOTO lbl17
            }
            case 2: {
                v1 = var1_1;
                var1_1 = var2_2;
lbl17:
                // 2 sources

                if ((var2_2 = (Triplet)v1.join()) == null) {
                    var0.isContactpreload = false;
                    var0.logger.error("There is no item to preload. Handling...");
                    return CompletableFuture.completedFuture(null);
                }
                var0.shippingRate = new CompletableFuture<T>();
                var3_3 /* !! */  = (String)var2_2.first;
                var0.precartItemName = var3_3 /* !! */ ;
                if (var0.task.getSite() != Site.KITH) ** GOTO lbl30
                v2 = var0.atcBasic((String)var3_3 /* !! */ );
                if (!v2.isDone()) {
                    var5_5 = v2;
                    return var5_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preloadContactOnly(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray io.trickle.task.sites.shopify.util.Triplet java.lang.String java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var5_5, (JsonArray)var1_1, (Triplet)var2_2, (String)var3_3 /* !! */ , null, (int)3));
                }
                ** GOTO lbl42
lbl30:
                // 1 sources

                v3 = var0.atcAJAX((String)var3_3 /* !! */ , false);
                if (!v3.isDone()) {
                    var5_5 = v3;
                    return var5_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preloadContactOnly(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray io.trickle.task.sites.shopify.util.Triplet java.lang.String java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var5_5, (JsonArray)var1_1, (Triplet)var2_2, (String)var3_3 /* !! */ , null, (int)4));
                }
                ** GOTO lbl55
            }
            case 3: {
                v2 = var1_1;
                v4 = var2_2;
                v5 = var3_3 /* !! */ ;
                var3_3 /* !! */  = var4_4;
                var2_2 = v5;
                var1_1 = v4;
lbl42:
                // 2 sources

                v2.join();
lbl44:
                // 2 sources

                while (!(v6 = var0.genCheckoutURL(false)).isDone()) {
                    var5_5 = v6;
                    return var5_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preloadContactOnly(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray io.trickle.task.sites.shopify.util.Triplet java.lang.String java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var5_5, (JsonArray)var1_1, (Triplet)var2_2, (String)var3_3 /* !! */ , null, (int)5));
                }
                ** GOTO lbl65
            }
            case 4: {
                v3 = var1_1;
                v7 = var2_2;
                v8 = var3_3 /* !! */ ;
                var3_3 /* !! */  = var4_4;
                var2_2 = v8;
                var1_1 = v7;
lbl55:
                // 2 sources

                v3.join();
                ** GOTO lbl44
            }
            case 5: {
                v6 = var1_1;
                v9 = var2_2;
                v10 = var3_3 /* !! */ ;
                var3_3 /* !! */  = var4_4;
                var2_2 = v10;
                var1_1 = v9;
lbl65:
                // 2 sources

                if (!(v11 = var0.handlePreload(var4_4 = (String)v6.join())).isDone()) {
                    var5_5 = v11;
                    return var5_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preloadContactOnly(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray io.trickle.task.sites.shopify.util.Triplet java.lang.String java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var5_5, (JsonArray)var1_1, (Triplet)var2_2, (String)var3_3 /* !! */ , (String)var4_4, (int)6));
                }
                ** GOTO lbl78
            }
            case 6: {
                v11 = var1_1;
                v12 = var2_2;
                v13 = var3_3 /* !! */ ;
                v14 = var4_4;
                var4_4 = var5_5;
                var3_3 /* !! */  = v14;
                var2_2 = v13;
                var1_1 = v12;
lbl78:
                // 2 sources

                var4_4 = (String)v11.join();
                if (((Boolean)var2_2.second).booleanValue()) ** GOTO lbl83
                var0.api.setOOS();
                var0.isContactpreload = false;
                ** GOTO lbl132
lbl83:
                // 1 sources

                v15 = var0.getCheckoutURL(var4_4);
                if (!v15.isDone()) {
                    var5_5 = v15;
                    return var5_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preloadContactOnly(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray io.trickle.task.sites.shopify.util.Triplet java.lang.String java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var5_5, (JsonArray)var1_1, (Triplet)var2_2, (String)var3_3 /* !! */ , (String)var4_4, (int)7));
                }
                ** GOTO lbl97
            }
            case 7: {
                v15 = var1_1;
                v16 = var2_2;
                v17 = var3_3 /* !! */ ;
                v18 = var4_4;
                var4_4 = var5_5;
                var3_3 /* !! */  = v18;
                var2_2 = v17;
                var1_1 = v16;
lbl97:
                // 2 sources

                v15.join();
                if (!var0.api.getCookies().contains("_shopify_checkpoint")) ** GOTO lbl116
                v19 = var0.checkCaptcha(var4_4);
                if (!v19.isDone()) {
                    var5_5 = v19;
                    return var5_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preloadContactOnly(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray io.trickle.task.sites.shopify.util.Triplet java.lang.String java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var5_5, (JsonArray)var1_1, (Triplet)var2_2, (String)var3_3 /* !! */ , (String)var4_4, (int)8));
                }
                ** GOTO lbl114
            }
            case 8: {
                v19 = var1_1;
                v20 = var2_2;
                v21 = var3_3 /* !! */ ;
                v22 = var4_4;
                var4_4 = var5_5;
                var3_3 /* !! */  = v22;
                var2_2 = v21;
                var1_1 = v20;
lbl114:
                // 2 sources

                v19.join();
lbl116:
                // 2 sources

                if (!(v23 = var0.submitContact(var4_4)).isDone()) {
                    var5_5 = v23;
                    return var5_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preloadContactOnly(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray io.trickle.task.sites.shopify.util.Triplet java.lang.String java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var5_5, (JsonArray)var1_1, (Triplet)var2_2, (String)var3_3 /* !! */ , (String)var4_4, (int)9));
                }
                ** GOTO lbl129
            }
            case 9: {
                v23 = var1_1;
                v24 = var2_2;
                v25 = var3_3 /* !! */ ;
                v26 = var4_4;
                var4_4 = var5_5;
                var3_3 /* !! */  = v26;
                var2_2 = v25;
                var1_1 = v24;
lbl129:
                // 2 sources

                v23.join();
                var0.isContactpreload = true;
lbl132:
                // 2 sources

                if (!(v27 = var0.clearWithChangeJS()).isDone()) {
                    var5_5 = v27;
                    return var5_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preloadContactOnly(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray io.trickle.task.sites.shopify.util.Triplet java.lang.String java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var5_5, (JsonArray)var1_1, (Triplet)var2_2, (String)var3_3 /* !! */ , (String)var4_4, (int)10));
                }
                ** GOTO lbl145
            }
            case 10: {
                v27 = var1_1;
                v28 = var2_2;
                v29 = var3_3 /* !! */ ;
                v30 = var4_4;
                var4_4 = var5_5;
                var3_3 /* !! */  = v30;
                var2_2 = v29;
                var1_1 = v28;
lbl145:
                // 2 sources

                v27.join();
                v31 = var0.checkCart();
                if (!v31.isDone()) {
                    var5_5 = v31;
                    return var5_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preloadContactOnly(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray io.trickle.task.sites.shopify.util.Triplet java.lang.String java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var5_5, (JsonArray)var1_1, (Triplet)var2_2, (String)var3_3 /* !! */ , (String)var4_4, (int)11));
                }
                ** GOTO lbl161
            }
            case 11: {
                v31 = var1_1;
                v32 = var2_2;
                v33 = var3_3 /* !! */ ;
                v34 = var4_4;
                var4_4 = var5_5;
                var3_3 /* !! */  = v34;
                var2_2 = v33;
                var1_1 = v32;
lbl161:
                // 2 sources

                v31.join();
                v35 = var0.confirmClear(var4_4);
                if (!v35.isDone()) {
                    var5_5 = v35;
                    return var5_5.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$preloadContactOnly(io.trickle.task.sites.shopify.ShopifySafe java.util.concurrent.CompletableFuture io.vertx.core.json.JsonArray io.trickle.task.sites.shopify.util.Triplet java.lang.String java.lang.String int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (CompletableFuture)var5_5, (JsonArray)var1_1, (Triplet)var2_2, (String)var3_3 /* !! */ , (String)var4_4, (int)12));
                }
                ** GOTO lbl177
            }
            case 12: {
                v35 = var1_1;
                v36 = var2_2;
                v37 = var3_3 /* !! */ ;
                v38 = var4_4;
                var4_4 = var5_5;
                var3_3 /* !! */  = v38;
                var2_2 = v37;
                var1_1 = v36;
lbl177:
                // 2 sources

                v35.join();
                var0.api.checkIsOOS();
                var0.configureShippingRate();
                return CompletableFuture.completedFuture(var4_4);
            }
        }
        throw new IllegalArgumentException();
    }

    /*
     * Unable to fully structure code
     */
    public static CompletableFuture async$createQueue(ShopifySafe var0, int var1_1, CompletableFuture var2_2, int var3_3, Object var4_4) {
        switch (var3_3) {
            case 0: {
                var1_1 = 0;
lbl4:
                // 2 sources

                while (true) {
                    if (var1_1++ >= 0x7FFFFFFF) return CompletableFuture.completedFuture(null);
                    v0 = var0.attemptGenCheckoutUrl(true);
                    if (!v0.isDone()) {
                        var2_2 = v0;
                        return var2_2.exceptionally(Function.<T>identity()).thenCompose((Function<Object, CompletableFuture>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, async$createQueue(io.trickle.task.sites.shopify.ShopifySafe int java.util.concurrent.CompletableFuture int java.lang.Object ), (Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;)((ShopifySafe)var0, (int)var1_1, (CompletableFuture)var2_2, (int)1));
                    }
                    ** GOTO lbl13
                    break;
                }
            }
            case 1: {
                v0 = var2_2;
lbl13:
                // 2 sources

                v0.join();
                var0.api.getCookies().clear();
                ** continue;
            }
        }
        throw new IllegalArgumentException();
    }
}
