/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.json.JsonObject
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package io.trickle.harvester;

import io.trickle.core.Controller;
import io.trickle.core.Engine;
import io.trickle.harvester.SitekeyController;
import io.trickle.harvester.Sitekeys$1;
import io.trickle.task.sites.Site;
import io.vertx.core.json.JsonObject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Sitekeys {
    public static Pattern ACTION_PATTERN;
    public static String SHOP_V3_SITEKEY;
    public static Logger logger;
    public static String SHOPIFY_ACC_GEN_ACTION;
    public static Pattern YS_COOKIE_PATTERN;
    public static Pattern TOKEN_PATTERN;
    public static String SHOP_V2_SITEKEY;
    public static String SHOPIFY_LOGIN_ACTION;

    public static boolean parseForSitekey(Site site, String string) {
        Matcher matcher = TOKEN_PATTERN.matcher(string);
        if (!matcher.find()) {
            logger.warn("Unable to find sitekey");
            return false;
        }
        Sitekeys.updateV3Sitekey(site, matcher.group(1));
        return true;
    }

    public static void updateV3Action(Site site, String string) {
        Sitekeys.getV3Settings(site).put("action", (Object)string);
    }

    public static boolean parseForAction(Site site, String string) {
        Matcher matcher = ACTION_PATTERN.matcher(string);
        if (!matcher.find()) {
            logger.warn("Unable to find sitekey");
            return false;
        }
        Sitekeys.updateV3Action(site, matcher.group(1));
        return true;
    }

    public static void updateV3Sitekey(Site site, String string) {
        Sitekeys.getV3Settings(site).put("sitekey", (Object)string);
    }

    static {
        SHOP_V3_SITEKEY = "6LcCR2cUAAAAANS1Gpq_mDIJ2pQuJphsSQaUEuc9";
        SHOPIFY_ACC_GEN_ACTION = "create_customer";
        SHOP_V2_SITEKEY = "6LeoeSkTAAAAAA9rkZs5oS82l69OEYjKRZAiKdaF";
        SHOPIFY_LOGIN_ACTION = "customer_login";
        logger = LogManager.getLogger(Sitekeys.class);
        TOKEN_PATTERN = Pattern.compile("(6L[\\--z]*?A{3,5}.*?)\"");
        ACTION_PATTERN = Pattern.compile("execute.*?action:\"([\\--z]*?)\"|execute.*action:\\s?'([\\--z]*)'");
        YS_COOKIE_PATTERN = Pattern.compile("\"[.]concat[(]\"([0-z]*?)\", \"");
    }

    public static boolean parseForCookie(Site site, String string) {
        Matcher matcher = YS_COOKIE_PATTERN.matcher(string);
        if (!matcher.find()) {
            logger.warn("Unable to find sitekey");
            return false;
        }
        Sitekeys.updateV3Action(site, matcher.group(1));
        return true;
    }

    public static void updateV3ActionGetCheckout(Site site, String string) {
        Sitekeys.getV3Settings(site).put("action_get_checkout", (Object)string);
    }

    public static void updateV3ActionConfirmOrder(Site site, String string) {
        Sitekeys.getV3Settings(site).put("action_confirm_order", (Object)string);
    }

    public static JsonObject getV3Settings(Site site) {
        JsonObject jsonObject = ((SitekeyController)Engine.get().getModule(Controller.SITEKEYS)).getSitekeys();
        switch (Sitekeys$1.$SwitchMap$io$trickle$task$sites$Site[site.ordinal()]) {
            case 1: {
                return jsonObject.getJsonObject("https://www.walmart.com");
            }
            case 2: {
                return jsonObject.getJsonObject("https://www.yeezysupply.com");
            }
            case 3: {
                return jsonObject.getJsonObject("https://www.jdsports.com");
            }
            case 4: {
                return jsonObject.getJsonObject("https://www.finishline.com");
            }
        }
        return null;
    }

    public static String getActionFromLink(String string) {
        if (string.endsWith("/account/register")) {
            return "create_customer";
        }
        if (!string.endsWith("/account/login?return_url=%2Faccount")) return "";
        return "customer_login";
    }

    public static String getSitekeyFromLink(String string) {
        if (string.endsWith("/account/register")) return "6LcCR2cUAAAAANS1Gpq_mDIJ2pQuJphsSQaUEuc9";
        if (string.endsWith("/account/login?return_url=%2Faccount")) {
            return "6LcCR2cUAAAAANS1Gpq_mDIJ2pQuJphsSQaUEuc9";
        }
        if (!string.endsWith("/challenge")) return "";
        return "6LeoeSkTAAAAAA9rkZs5oS82l69OEYjKRZAiKdaF";
    }
}

