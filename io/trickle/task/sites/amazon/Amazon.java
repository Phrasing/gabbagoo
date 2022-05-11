package io.trickle.task.sites.amazon;

import io.netty.handler.codec.http.cookie.ClientCookieDecoder;
import io.netty.handler.codec.http.cookie.Cookie;
import io.trickle.account.Account;
import io.trickle.account.AccountController;
import io.trickle.core.Controller;
import io.trickle.core.Engine;
import io.trickle.core.actor.TaskActor;
import io.trickle.harvester.LoginHarvester;
import io.trickle.task.Task;
import io.trickle.task.sites.Site;
import io.trickle.task.sites.shopify.util.Triplet;
import io.trickle.util.Pair;
import io.trickle.util.Utils;
import io.trickle.util.analytics.Analytics;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import org.apache.logging.log4j.Logger;

public class Amazon extends TaskActor {
   public static Pattern OM_UPSELL_CNT = Pattern.compile("onmlUpsellSuppressedCount\" value=\"(.*?)\"");
   public static Pattern INVARIANT = Pattern.compile("isQuantityInvariant\" value=\"(.*?)\"");
   public static Pattern IS_SHIP_WHNEVR_VAL_2 = Pattern.compile("isShipWheneverValid0.2\" value=\"(.*?)\"");
   public static Pattern PREV_SHIP_OFF_ID = Pattern.compile("previousshippingofferingid0\" value=\"(.*?)\"");
   public static Pattern RID_PAT = Pattern.compile("request-id=\"(.*?)\"");
   public static Pattern PREV_SHIP_PRIORITY = Pattern.compile("previousshippriority0\" value=\"(.*?)\"");
   public static Pattern SHIP_TRIAL_PFX = Pattern.compile("shiptrialprefix\" value=\"(.*?)\"");
   public static Pattern PROMISE_TIME = Pattern.compile("promiseTime-0\" value=\"(.*?)\"");
   public static Pattern SHIP_SPLIT_PRIORITY_2 = Pattern.compile("shipsplitpriority0.2\" value=\"(.*?)\"");
   public static String[] successfulRedirectIndicators = new String[]{"return_to=https%3A%2F%2Fwww.amazon.com%2Fgp%2Fcheckoutportal%2Fenter-checkout.html", "/gp/cart/view.html"};
   public static Pattern SCOPE = Pattern.compile("scopeId\" value=\"(.*?)\"");
   public static CompletableFuture[] harvesterFutures = new CompletableFuture[3];
   public static Pattern ANTI_CSRF_PAT = Pattern.compile("anti-csrftoken-a2z' value='(.*?)'");
   public static Pattern TOTAL_PAT = Pattern.compile("purchaseTotal\" value=\"(.*?)\"");
   public static Pattern LINE_ITEM_IDS = Pattern.compile("lineitemids0\" value=\"(.*?)\"");
   public static Pattern SELECTED_SHIP_SPD = Pattern.compile("selectedshipspeed0.1.0\" data-shippingofferingid=\"(.*?)\"");
   public static Pattern CURRENCY_PAT = Pattern.compile("\"purchaseTotalCurrency\" value=\"(.*?)\"");
   public static Pattern FIRST_TIMER = Pattern.compile("isfirsttimecustomer\" value=\"(.*?)\"");
   public static Pattern SNS_UPSELL_CNT = Pattern.compile("snsUpsellTotalCount\" value=\"(.*?)\"");
   public static Pattern COUNTDOWN_ID = Pattern.compile("countdownId\" value=\"(.*?)\"");
   public static Pattern ANTI_CSRF_PAT_P_PAGE = Pattern.compile("anti-csrftoken-a2z\" value=\"(.*?)\"", 32);
   public static Pattern PREV_GUARENTEE_TYPE = Pattern.compile("previousguaranteetype0\" value=\"(.*?)\"");
   public Task task;
   public static Pattern ORDER_ZERO = Pattern.compile("order0\" value=\"(.*?)\"");
   public static Pattern DUP_ORDER_CHECK = Pattern.compile("dupOrderCheckArgs\" value=\"(.*?)\"");
   public static Pattern SHIP_OFFER_2 = Pattern.compile("shippingofferingid0.2\" value=\"(.*?)\"");
   public static Pattern GROUP_CNT = Pattern.compile("groupcount\" value=\"(.*?)\"");
   public static String[] successfulCartIndicators = new String[]{"item in cart", "Proceed to checkout (", "\"ok\":true,\"numActiveItemsInCart\":\"", "\"nav-cart\":{\"cartQty\":1}", "\"entity\":{\"items\":1}", "/checkout/ordersummary?ref_=", "\"cartQuantity\":\"1\""};
   public static Pattern PID_PAT = Pattern.compile("pid=([0-9].*?)&");
   public static Pattern VAS_MODEL = Pattern.compile("vasClaimBasedModel\" value=\"(.*?)\"");
   public static Pattern SELECTED_PAYMENT = Pattern.compile("selectedPaymentPaystationId\" value=\"(.*?)\"");
   public static Pattern PREV_ISSS = Pattern.compile("previousissss0\" value=\"(.*?)\"");
   public static Pattern PURCHASE_ID = Pattern.compile("purchaseID\" value=\"(.*?)\"");
   public static Pattern SIMPL_COUNTDOWN = Pattern.compile("showSimplifiedCountdown\" value=\"(.*?)\"");
   public String instanceSignal;
   public String[] successfulFinalPageIndicators = new String[]{">Shipping address<", "Amazon.com Checkout</title>"};
   public static Pattern GUARENTEE_1 = Pattern.compile("data-issss=\"1\" data-guaranteetype=\"(.*?)\"");
   public static Pattern WEBLAB_PAT = Pattern.compile("weblab=(.*?)\"");
   public static Pattern PROMISE_ASIN = Pattern.compile("promiseAsin-0\" value=\"(.*?)\"");
   public static Pattern IS_SHIP_COMPL_VAL_2 = Pattern.compile("isShipWhenCompleteValid0.2\" value=\"(.*?)\"");
   public JsonObject itemJson;
   public AmazonAPI api;
   public static Pattern ISSS_2 = Pattern.compile("issss0.2\" value=\"(.*?)\"");
   public static Pattern IS_SHIP_WHNEVR_VAL = Pattern.compile("isShipWheneverValid0.1\" value=\"(.*?)\"");
   public static Pattern CURR_SHIP_SPD = Pattern.compile("currentshippingspeed\" value=\"(.*?)\"");
   public static Pattern IS_SHIP_COMPL_VAL = Pattern.compile("isShipWhenCompleteValid0.1\" value=\"(.*?)\"");
   public static Pattern CTB = Pattern.compile("useCtb\" value=\"(.*?)\"");
   public static Pattern PREV_SHIP_SPD = Pattern.compile("previousShippingSpeed0\" value=\"(.*?)\"");
   public Boolean isTurbo;
   public static Pattern PURCHASE_CUST_ID = Pattern.compile("purchaseCustomerId\" value=\"(.*?)\"");
   public static Pattern GUARENTEE_2 = Pattern.compile("guaranteetype0.2\" value=\"(.*?)\"");
   public static Pattern COUNTDOWN_THRESH = Pattern.compile("countdownThreshold\" value=\"(.*?)\"");
   public static Pattern SHIP_PRIORITY_0_WHEN_CMPL = Pattern.compile("shippriority.0.shipWhenComplete\" value=\"(.*?)\"");
   public static Pattern CURR_SHIP_SPLIT_PREF = Pattern.compile("currentshipsplitpreference\" value=\"(.*?)\"");
   public static Pattern CSRF_PAT = Pattern.compile("\"csrfToken\" value=\"(.*?)\"");
   public static Pattern SHIP_SPLIT_PRIORITY_1 = Pattern.compile("shipsplitpriority0.1\" value=\"(.*?)\"");
   public static Pattern FAST_TRACK_EXP = Pattern.compile("fasttrackExpiration\" value=\"(.*?)\"");

   public CompletableFuture smoothLogin() {
      AccountController var1 = (AccountController)Engine.get().getModule(Controller.ACCOUNT);
      CompletableFuture var10000 = var1.findAccount(this.task.getProfile().getEmail(), true).toCompletionStage().toCompletableFuture();
      CompletableFuture var4;
      if (!var10000.isDone()) {
         var4 = var10000;
         return var4.exceptionally(Function.identity()).thenCompose(Amazon::async$smoothLogin);
      } else {
         Account var2 = (Account)var10000.join();
         if (var2 == null) {
            this.logger.warn("No accounts available. Sleeping forever...");
            var10000 = VertxUtil.hardCodedSleep(Long.MAX_VALUE);
            if (!var10000.isDone()) {
               var4 = var10000;
               return var4.exceptionally(Function.identity()).thenCompose(Amazon::async$smoothLogin);
            } else {
               var10000.join();
               return CompletableFuture.completedFuture((Object)null);
            }
         } else {
            this.logger.info("Logging in to account '{}'", var2.getUser());
            var2.setSite(String.valueOf(Site.AMAZON));
            var10000 = this.sessionLogon(var2);
            if (!var10000.isDone()) {
               var4 = var10000;
               return var4.exceptionally(Function.identity()).thenCompose(Amazon::async$smoothLogin);
            } else if ((Boolean)var10000.join()) {
               this.logger.info("Logged in successfully to account '{}'", var2.getUser());
               return CompletableFuture.completedFuture((Object)null);
            } else {
               Object var3 = null;
               if (!((CompletionStage)var3).toCompletableFuture().isDone()) {
                  return ((CompletionStage)var3).exceptionally(Function.identity()).thenCompose(Amazon::async$smoothLogin).toCompletableFuture();
               } else {
                  ((CompletionStage)var3).toCompletableFuture().join();
                  var2.setSessionString(this.api.getCookies().asJson().encode());
                  super.vertx.eventBus().send("accounts.writer.session", var2);
                  var10000 = VertxUtil.hardCodedSleep(Long.MAX_VALUE);
                  if (!var10000.isDone()) {
                     var4 = var10000;
                     return var4.exceptionally(Function.identity()).thenCompose(Amazon::async$smoothLogin);
                  } else {
                     var10000.join();
                     return CompletableFuture.completedFuture((Object)null);
                  }
               }
            }
         }
      }
   }

   public static CompletableFuture async$placeOrderBuynow(Amazon param0, String param1, String param2, String param3, String param4, int param5, CompletableFuture param6, HttpResponse param7, Throwable param8, int param9, Object param10) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$cartHtml(Amazon param0, CompletableFuture param1, String param2, String param3, String param4, Triplet param5, HttpResponse param6, String param7, int param8, Throwable param9, int param10, Object param11) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture run() {
      CompletableFuture var10000 = CompletableFuture.allOf(harvesterFutures);
      CompletableFuture var3;
      if (!var10000.isDone()) {
         var3 = var10000;
         return var3.exceptionally(Function.identity()).thenCompose(Amazon::async$run);
      } else {
         var10000.join();

         try {
            var10000 = this.smoothLogin();
            if (!var10000.isDone()) {
               var3 = var10000;
               return var3.exceptionally(Function.identity()).thenCompose(Amazon::async$run);
            }

            var10000.join();
            var10000 = this.GETREQ("Init api", this.api.corsairPage(), 200, new String[]{"amazonApiCsrfToken"});
            if (!var10000.isDone()) {
               var3 = var10000;
               return var3.exceptionally(Function.identity()).thenCompose(Amazon::async$run);
            }

            String var1 = (String)var10000.join();
            this.api.cartAPI.apiCsrf = Utils.quickParseFirst(var1, Patterns.API_CSRF);
            var10000 = this.GETREQ("Checking page...", this.api.productPage(), 200, new String[]{""});
            if (!var10000.isDone()) {
               var3 = var10000;
               return var3.exceptionally(Function.identity()).thenCompose(Amazon::async$run);
            }

            String var2 = (String)var10000.join();
            this.itemJson = (new JsonObject()).put("title", Utils.quickParseFirst(var2, Patterns.TITLE));
            var10000 = this.mixedFlow();
            if (!var10000.isDone()) {
               var3 = var10000;
               return var3.exceptionally(Function.identity()).thenCompose(Amazon::async$run);
            }

            var10000.join();
         } catch (Exception var4) {
            var4.printStackTrace();
            this.logger.error("Task interrupted: " + var4.getMessage());
         }

         return CompletableFuture.completedFuture((Object)null);
      }
   }

   public static CompletableFuture async$placeOrderNormal(Amazon param0, MultiMap param1, int param2, CompletableFuture param3, HttpResponse param4, Throwable param5, int param6, Object param7) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$mixedFlow(Amazon var0, CompletableFuture var1, String var2, CompletableFuture var3, CompletableFuture var4, CompletableFuture var5, int var6, Object var7) {
      label81: {
         CompletableFuture var10000;
         label74: {
            CompletableFuture var48;
            String var49;
            label67: {
               label82: {
                  label64: {
                     label75: {
                        switch (var6) {
                           case 0:
                              var10000 = var0.cartHtml();
                              if (!var10000.isDone()) {
                                 var48 = var10000;
                                 return var48.exceptionally(Function.identity()).thenCompose(Amazon::async$mixedFlow);
                              }
                              break;
                           case 1:
                              var10000 = var1;
                              break;
                           case 2:
                              var10000 = var5;
                              var49 = var2;
                              break label75;
                           case 3:
                              var10000 = var1;
                              var49 = var2;
                              break label82;
                           case 4:
                              var10000 = var1;
                              break label74;
                           default:
                              throw new IllegalArgumentException();
                        }

                        var49 = (String)var10000.join();
                        if (var0.isTurbo == null) {
                           break label67;
                        }

                        if (var0.isTurbo) {
                           break label64;
                        }

                        CompletableFuture var50 = var0.GETREQ("Checking cart 1", var0.api.prefetch("https://www.amazon.com/gp/cart/checkout-prefetch.html?ie=UTF8&checkAuthentication=1&checkDefaults=1&cartInitiateId=" + System.currentTimeMillis() + "&partialCheckoutCart=1"), 200, (String[])null);
                        var3 = var0.GETREQ("Checking cart 2", var0.api.naturalFinalPage(), 200, new String[]{">Shipping address<"});
                        var4 = var0.GETREQ("Proceeding...", var0.api.proceedToCheckout(), 200, new String[]{""});
                        var10000 = VertxUtil.hardCodedSleep(1000L);
                        if (!var10000.isDone()) {
                           var48 = var10000;
                           return var48.exceptionally(Function.identity()).thenCompose(Amazon::async$mixedFlow);
                        }
                     }

                     var10000.join();
                  }

                  var10000 = var0.noCartJack(var49);
                  if (!var10000.isDone()) {
                     var48 = var10000;
                     return var48.exceptionally(Function.identity()).thenCompose(Amazon::async$mixedFlow);
                  }
               }

               boolean var51 = (Boolean)var10000.join();
               if (var51) {
                  return CompletableFuture.completedFuture((Object)null);
               }
            }

            if (var0.isTurbo == null || var0.isTurbo) {
               return var0.buynow(var49);
            }

            if (var49.contains(">Shipping address<")) {
               var2 = var49;
               break label81;
            }

            var10000 = var0.GETREQ("Checking cart", var0.api.naturalFinalPage(), 200, new String[]{">Shipping address<"});
            if (!var10000.isDone()) {
               var48 = var10000;
               return var48.exceptionally(Function.identity()).thenCompose(Amazon::async$mixedFlow);
            }
         }

         var2 = (String)var10000.join();
      }

      String var52 = Utils.quickParseFirst(var2, CSRF_PAT);
      String var53 = Utils.quickParseFirst(var2, TOTAL_PAT);
      String var54 = Utils.quickParseFirst(var2, CURRENCY_PAT);
      String var55 = Utils.quickParseFirst(var2, PURCHASE_ID);
      String var56 = Utils.quickParseFirst(var2, PURCHASE_CUST_ID);
      String var8 = Utils.quickParseFirst(var2, CTB);
      String var9 = Utils.quickParseFirst(var2, SCOPE);
      String var10 = Utils.quickParseFirst(var2, INVARIANT);
      String var11 = Utils.quickParseFirst(var2, PROMISE_TIME);
      String var12 = Utils.quickParseFirst(var2, PROMISE_ASIN);
      String var13 = Utils.quickParseFirst(var2, SELECTED_PAYMENT);
      String var14 = Utils.quickParseFirst(var2, FAST_TRACK_EXP);
      String var15 = Utils.quickParseFirst(var2, COUNTDOWN_THRESH);
      String var16 = Utils.quickParseFirst(var2, COUNTDOWN_ID);
      String var17 = Utils.quickParseFirst(var2, SIMPL_COUNTDOWN);
      String var18 = Utils.quickParseFirst(var2, DUP_ORDER_CHECK);
      String var19 = Utils.quickParseFirst(var2, ORDER_ZERO);
      String var20 = Utils.quickParseFirst(var2, SELECTED_SHIP_SPD);
      String var21 = Utils.quickParseFirst(var2, GUARENTEE_1);
      String var22 = "1";
      String var23 = Utils.quickParseFirst(var2, SHIP_SPLIT_PRIORITY_1);
      String var24 = Utils.quickParseFirst(var2, IS_SHIP_COMPL_VAL);
      String var25 = Utils.quickParseFirst(var2, IS_SHIP_WHNEVR_VAL);
      String var26 = Utils.quickParseFirst(var2, SHIP_OFFER_2);
      String var27 = Utils.quickParseFirst(var2, GUARENTEE_2);
      String var28 = Utils.quickParseFirst(var2, ISSS_2);
      String var29 = Utils.quickParseFirst(var2, SHIP_SPLIT_PRIORITY_2);
      String var30 = Utils.quickParseFirst(var2, IS_SHIP_COMPL_VAL_2);
      String var31 = Utils.quickParseFirst(var2, IS_SHIP_WHNEVR_VAL_2);
      String var32 = Utils.quickParseFirst(var2, PREV_SHIP_OFF_ID);
      String var33 = Utils.quickParseFirst(var2, PREV_GUARENTEE_TYPE);
      String var34 = Utils.quickParseFirst(var2, PREV_ISSS);
      String var35 = Utils.quickParseFirst(var2, PREV_SHIP_PRIORITY);
      String var36 = Utils.quickParseFirst(var2, LINE_ITEM_IDS);
      String var37 = Utils.quickParseFirst(var2, CURR_SHIP_SPD);
      String var38 = Utils.quickParseFirst(var2, PREV_SHIP_SPD);
      String var39 = Utils.quickParseFirst(var2, CURR_SHIP_SPLIT_PREF);
      String var40 = Utils.quickParseFirst(var2, SHIP_PRIORITY_0_WHEN_CMPL);
      String var41 = Utils.quickParseFirst(var2, GROUP_CNT);
      String var42 = Utils.quickParseFirst(var2, SNS_UPSELL_CNT);
      String var43 = Utils.quickParseFirst(var2, OM_UPSELL_CNT);
      String var44 = Utils.quickParseFirst(var2, VAS_MODEL);
      String var45 = Utils.quickParseFirst(var2, SHIP_TRIAL_PFX);
      String var46 = Utils.quickParseFirst(var2, FIRST_TIMER);
      MultiMap var47 = var0.api.normalPlaceOrderForm(var52, var53, var54, var55, var56, var8, var9, var10, var11, var12, var13, var14, var15, var16, var17, var18, var19, var20, var21, "1", var23, var24, var25, var26, var27, var28, var29, var30, var31, var32, var33, var34, var35, var36, var37, var38, var39, var40, var41, var42, var43, var44, var45, var46);
      return var0.placeOrderNormal(var47);
   }

   public CompletableFuture noCartJack(String var1) {
      for(int var2 = 0; var2 < 250; ++var2) {
         CompletableFuture var10000 = this.GETREQ("Placing order [exp]", this.api.placeOrderExp(), (Integer)null, new String[]{""});
         CompletableFuture var4;
         if (!var10000.isDone()) {
            var4 = var10000;
            return var4.exceptionally(Function.identity()).thenCompose(Amazon::async$noCartJack);
         }

         String var3 = (String)var10000.join();
         if (var3.contains("a-color-success a-text-bold")) {
            this.logger.info("Successfully checked out!");
            Analytics.success(this.task, this.itemJson, this.api.proxyStringSafe());
            return CompletableFuture.completedFuture(true);
         }

         this.logger.error("Checkout failed [exp]. Retrying...");
         if (var2 > 5) {
            var10000 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
            if (!var10000.isDone()) {
               var4 = var10000;
               return var4.exceptionally(Function.identity()).thenCompose(Amazon::async$noCartJack);
            }

            var10000.join();
         }
      }

      return CompletableFuture.completedFuture(false);
   }

   public CompletableFuture mixedFlow() {
      CompletableFuture var10000 = this.cartHtml();
      CompletableFuture var48;
      if (!var10000.isDone()) {
         var48 = var10000;
         return var48.exceptionally(Function.identity()).thenCompose(Amazon::async$mixedFlow);
      } else {
         String var1 = (String)var10000.join();
         if (this.isTurbo != null) {
            if (!this.isTurbo) {
               CompletableFuture var2 = this.GETREQ("Checking cart 1", this.api.prefetch("https://www.amazon.com/gp/cart/checkout-prefetch.html?ie=UTF8&checkAuthentication=1&checkDefaults=1&cartInitiateId=" + System.currentTimeMillis() + "&partialCheckoutCart=1"), 200, (String[])null);
               CompletableFuture var3 = this.GETREQ("Checking cart 2", this.api.naturalFinalPage(), 200, new String[]{">Shipping address<"});
               CompletableFuture var4 = this.GETREQ("Proceeding...", this.api.proceedToCheckout(), 200, new String[]{""});
               var10000 = VertxUtil.hardCodedSleep(1000L);
               if (!var10000.isDone()) {
                  var48 = var10000;
                  return var48.exceptionally(Function.identity()).thenCompose(Amazon::async$mixedFlow);
               }

               var10000.join();
            }

            var10000 = this.noCartJack(var1);
            if (!var10000.isDone()) {
               var48 = var10000;
               return var48.exceptionally(Function.identity()).thenCompose(Amazon::async$mixedFlow);
            }

            boolean var49 = (Boolean)var10000.join();
            if (var49) {
               return CompletableFuture.completedFuture((Object)null);
            }
         }

         if (this.isTurbo != null && !this.isTurbo) {
            String var50;
            if (var1.contains(">Shipping address<")) {
               var50 = var1;
            } else {
               var10000 = this.GETREQ("Checking cart", this.api.naturalFinalPage(), 200, new String[]{">Shipping address<"});
               if (!var10000.isDone()) {
                  var48 = var10000;
                  return var48.exceptionally(Function.identity()).thenCompose(Amazon::async$mixedFlow);
               }

               var50 = (String)var10000.join();
            }

            String var51 = Utils.quickParseFirst(var50, CSRF_PAT);
            String var52 = Utils.quickParseFirst(var50, TOTAL_PAT);
            String var5 = Utils.quickParseFirst(var50, CURRENCY_PAT);
            String var6 = Utils.quickParseFirst(var50, PURCHASE_ID);
            String var7 = Utils.quickParseFirst(var50, PURCHASE_CUST_ID);
            String var8 = Utils.quickParseFirst(var50, CTB);
            String var9 = Utils.quickParseFirst(var50, SCOPE);
            String var10 = Utils.quickParseFirst(var50, INVARIANT);
            String var11 = Utils.quickParseFirst(var50, PROMISE_TIME);
            String var12 = Utils.quickParseFirst(var50, PROMISE_ASIN);
            String var13 = Utils.quickParseFirst(var50, SELECTED_PAYMENT);
            String var14 = Utils.quickParseFirst(var50, FAST_TRACK_EXP);
            String var15 = Utils.quickParseFirst(var50, COUNTDOWN_THRESH);
            String var16 = Utils.quickParseFirst(var50, COUNTDOWN_ID);
            String var17 = Utils.quickParseFirst(var50, SIMPL_COUNTDOWN);
            String var18 = Utils.quickParseFirst(var50, DUP_ORDER_CHECK);
            String var19 = Utils.quickParseFirst(var50, ORDER_ZERO);
            String var20 = Utils.quickParseFirst(var50, SELECTED_SHIP_SPD);
            String var21 = Utils.quickParseFirst(var50, GUARENTEE_1);
            String var22 = "1";
            String var23 = Utils.quickParseFirst(var50, SHIP_SPLIT_PRIORITY_1);
            String var24 = Utils.quickParseFirst(var50, IS_SHIP_COMPL_VAL);
            String var25 = Utils.quickParseFirst(var50, IS_SHIP_WHNEVR_VAL);
            String var26 = Utils.quickParseFirst(var50, SHIP_OFFER_2);
            String var27 = Utils.quickParseFirst(var50, GUARENTEE_2);
            String var28 = Utils.quickParseFirst(var50, ISSS_2);
            String var29 = Utils.quickParseFirst(var50, SHIP_SPLIT_PRIORITY_2);
            String var30 = Utils.quickParseFirst(var50, IS_SHIP_COMPL_VAL_2);
            String var31 = Utils.quickParseFirst(var50, IS_SHIP_WHNEVR_VAL_2);
            String var32 = Utils.quickParseFirst(var50, PREV_SHIP_OFF_ID);
            String var33 = Utils.quickParseFirst(var50, PREV_GUARENTEE_TYPE);
            String var34 = Utils.quickParseFirst(var50, PREV_ISSS);
            String var35 = Utils.quickParseFirst(var50, PREV_SHIP_PRIORITY);
            String var36 = Utils.quickParseFirst(var50, LINE_ITEM_IDS);
            String var37 = Utils.quickParseFirst(var50, CURR_SHIP_SPD);
            String var38 = Utils.quickParseFirst(var50, PREV_SHIP_SPD);
            String var39 = Utils.quickParseFirst(var50, CURR_SHIP_SPLIT_PREF);
            String var40 = Utils.quickParseFirst(var50, SHIP_PRIORITY_0_WHEN_CMPL);
            String var41 = Utils.quickParseFirst(var50, GROUP_CNT);
            String var42 = Utils.quickParseFirst(var50, SNS_UPSELL_CNT);
            String var43 = Utils.quickParseFirst(var50, OM_UPSELL_CNT);
            String var44 = Utils.quickParseFirst(var50, VAS_MODEL);
            String var45 = Utils.quickParseFirst(var50, SHIP_TRIAL_PFX);
            String var46 = Utils.quickParseFirst(var50, FIRST_TIMER);
            MultiMap var47 = this.api.normalPlaceOrderForm(var51, var52, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, var17, var18, var19, var20, var21, "1", var23, var24, var25, var26, var27, var28, var29, var30, var31, var32, var33, var34, var35, var36, var37, var38, var39, var40, var41, var42, var43, var44, var45, var46);
            return this.placeOrderNormal(var47);
         } else {
            return this.buynow(var1);
         }
      }
   }

   public CompletableFuture buynow(String var1) {
      String var2 = Utils.quickParseFirst(var1, ANTI_CSRF_PAT);
      String var3 = Utils.quickParseFirst(var1, WEBLAB_PAT);
      String var4 = Utils.quickParseFirst(var1, PID_PAT);
      String var5 = Utils.quickParseFirst(var1, RID_PAT);
      return this.placeOrderBuynow(var2, var5, var4, var3);
   }

   public static CompletableFuture async$sessionLogon(Amazon var0, Account var1, String var2, JsonArray var3, CompletableFuture var4, String var5, int var6, int var7, int var8, Object var9) {
      Throwable var21;
      label85: {
         CompletableFuture var10000;
         boolean var10001;
         label89: {
            CompletableFuture var20;
            switch (var8) {
               case 0:
                  var2 = var1.lookupSession();
                  if (var2.isBlank()) {
                     return CompletableFuture.completedFuture(false);
                  }

                  var3 = new JsonArray(var2);
                  if (var3.isEmpty()) {
                     return CompletableFuture.completedFuture(false);
                  }

                  for(int var15 = 0; var15 < var3.size(); ++var15) {
                     try {
                        var5 = var3.getString(var15);
                        Cookie var19 = ClientCookieDecoder.STRICT.decode(var5);
                        if (var19 != null) {
                           var0.api.getCookies().put(var19);
                        }
                     } catch (Throwable var10) {
                        var0.logger.warn("Error parsing session state: {}", var10.getMessage());
                     }
                  }

                  try {
                     var10000 = var0.GETREQ("Validating Session", var0.api.walletPage(), 200, new String[]{""});
                     if (!var10000.isDone()) {
                        var20 = var10000;
                        return var20.exceptionally(Function.identity()).thenCompose(Amazon::async$sessionLogon);
                     }
                     break;
                  } catch (Throwable var13) {
                     var21 = var13;
                     var10001 = false;
                     break label85;
                  }
               case 1:
                  var10000 = var4;
                  break;
               case 2:
                  var10000 = var4;
                  break label89;
               default:
                  throw new IllegalArgumentException();
            }

            String var16;
            byte var18;
            try {
               var16 = (String)var10000.join();
               var0.api.cartAPI.addressID = Utils.quickParseFirst(var16, Patterns.ADDRESS_ID);
               var18 = var16.contains("card ending in");
               var6 = var0.api.cartAPI.addressID != null ? 1 : 0;
               if (var6 != 0 && var18 != 0) {
                  return CompletableFuture.completedFuture(true);
               }
            } catch (Throwable var14) {
               var21 = var14;
               var10001 = false;
               break label85;
            }

            try {
               if (var18 == 0) {
                  var0.logger.error("Please pre-fil payment on this account [{}]", var1.getUser());
               }

               if (var6 == 0) {
                  var0.logger.error("Please pre-fil shipping on this account [{}]", var1.getUser());
               }

               var10000 = VertxUtil.hardCodedSleep(999999999L);
               if (!var10000.isDone()) {
                  var20 = var10000;
                  return var20.exceptionally(Function.identity()).thenCompose(Amazon::async$sessionLogon);
               }
            } catch (Throwable var12) {
               var21 = var12;
               var10001 = false;
               break label85;
            }
         }

         try {
            var10000.join();
            return CompletableFuture.completedFuture(false);
         } catch (Throwable var11) {
            var21 = var11;
            var10001 = false;
         }
      }

      Throwable var17 = var21;
      var0.logger.warn("Error validating session: {}", var17.getMessage());
      return CompletableFuture.completedFuture(false);
   }

   public static CompletableFuture async$genericExecute(Amazon param0, String param1, Supplier param2, Integer param3, int param4, Pair param5, CompletableFuture param6, HttpResponse param7, String param8, Throwable param9, int param10, Object param11) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$smoothLogin(Amazon var0, AccountController var1, CompletableFuture var2, Account var3, int var4, Object var5) {
      CompletableFuture var10000;
      label69: {
         label70: {
            Account var6;
            CompletableFuture var7;
            label62: {
               label63: {
                  switch (var4) {
                     case 0:
                        var1 = (AccountController)Engine.get().getModule(Controller.ACCOUNT);
                        var10000 = var1.findAccount(var0.task.getProfile().getEmail(), true).toCompletionStage().toCompletableFuture();
                        if (!var10000.isDone()) {
                           var7 = var10000;
                           return var7.exceptionally(Function.identity()).thenCompose(Amazon::async$smoothLogin);
                        }
                        break;
                     case 1:
                        var10000 = var2;
                        break;
                     case 2:
                        var10000 = var2;
                        break label70;
                     case 3:
                        var10000 = var2;
                        var6 = var3;
                        break label63;
                     case 4:
                        var10000 = null;
                        var6 = var3;
                        var3 = null;
                        break label62;
                     case 5:
                        var10000 = var2;
                        var3 = null;
                        break label69;
                     default:
                        throw new IllegalArgumentException();
                  }

                  var6 = (Account)var10000.join();
                  if (var6 == null) {
                     var0.logger.warn("No accounts available. Sleeping forever...");
                     var10000 = VertxUtil.hardCodedSleep(Long.MAX_VALUE);
                     if (!var10000.isDone()) {
                        var7 = var10000;
                        return var7.exceptionally(Function.identity()).thenCompose(Amazon::async$smoothLogin);
                     }
                     break label70;
                  }

                  var0.logger.info("Logging in to account '{}'", var6.getUser());
                  var6.setSite(String.valueOf(Site.AMAZON));
                  var10000 = var0.sessionLogon(var6);
                  if (!var10000.isDone()) {
                     var7 = var10000;
                     return var7.exceptionally(Function.identity()).thenCompose(Amazon::async$smoothLogin);
                  }
               }

               if ((Boolean)var10000.join()) {
                  var0.logger.info("Logged in successfully to account '{}'", var6.getUser());
                  return CompletableFuture.completedFuture((Object)null);
               }

               var3 = null;
               var10000 = var3;
               if (!var3.toCompletableFuture().isDone()) {
                  return var3.exceptionally(Function.identity()).thenCompose(Amazon::async$smoothLogin).toCompletableFuture();
               }
            }

            var10000.toCompletableFuture().join();
            var6.setSessionString(var0.api.getCookies().asJson().encode());
            var0.vertx.eventBus().send("accounts.writer.session", var6);
            var10000 = VertxUtil.hardCodedSleep(Long.MAX_VALUE);
            if (!var10000.isDone()) {
               var7 = var10000;
               return var7.exceptionally(Function.identity()).thenCompose(Amazon::async$smoothLogin);
            }
            break label69;
         }

         var10000.join();
         return CompletableFuture.completedFuture((Object)null);
      }

      var10000.join();
      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture sessionLogon(Account var1) {
      String var2 = var1.lookupSession();
      if (!var2.isBlank()) {
         JsonArray var3 = new JsonArray(var2);
         if (!var3.isEmpty()) {
            for(int var4 = 0; var4 < var3.size(); ++var4) {
               try {
                  String var5 = var3.getString(var4);
                  Cookie var6 = ClientCookieDecoder.STRICT.decode(var5);
                  if (var6 != null) {
                     this.api.getCookies().put(var6);
                  }
               } catch (Throwable var9) {
                  this.logger.warn("Error parsing session state: {}", var9.getMessage());
               }
            }

            try {
               CompletableFuture var10000 = this.GETREQ("Validating Session", this.api.walletPage(), 200, new String[]{""});
               CompletableFuture var7;
               if (!var10000.isDone()) {
                  var7 = var10000;
                  return var7.exceptionally(Function.identity()).thenCompose(Amazon::async$sessionLogon);
               }

               String var10 = (String)var10000.join();
               this.api.cartAPI.addressID = Utils.quickParseFirst(var10, Patterns.ADDRESS_ID);
               byte var11 = var10.contains("card ending in");
               int var12 = this.api.cartAPI.addressID != null ? 1 : 0;
               if (var12 != 0 && var11 != 0) {
                  return CompletableFuture.completedFuture(true);
               }

               if (var11 == 0) {
                  this.logger.error("Please pre-fil payment on this account [{}]", var1.getUser());
               }

               if (var12 == 0) {
                  this.logger.error("Please pre-fil shipping on this account [{}]", var1.getUser());
               }

               var10000 = VertxUtil.hardCodedSleep(999999999L);
               if (!var10000.isDone()) {
                  var7 = var10000;
                  return var7.exceptionally(Function.identity()).thenCompose(Amazon::async$sessionLogon);
               }

               var10000.join();
            } catch (Throwable var8) {
               this.logger.warn("Error validating session: {}", var8.getMessage());
            }
         }
      }

      return CompletableFuture.completedFuture(false);
   }

   public static CompletableFuture async$noCartJack(Amazon var0, String var1, int var2, CompletableFuture var3, String var4, int var5, Object var6) {
      CompletableFuture var10000;
      String var7;
      CompletableFuture var8;
      switch (var5) {
         case 0:
            var2 = 0;
            break;
         case 1:
            var7 = (String)var3.join();
            if (var7.contains("a-color-success a-text-bold")) {
               var0.logger.info("Successfully checked out!");
               Analytics.success(var0.task, var0.itemJson, var0.api.proxyStringSafe());
               return CompletableFuture.completedFuture(true);
            }

            var0.logger.error("Checkout failed [exp]. Retrying...");
            if (var2 <= 5) {
               ++var2;
            } else {
               var10000 = VertxUtil.randomSleep((long)var0.task.getRetryDelay());
               if (!var10000.isDone()) {
                  var8 = var10000;
                  return var8.exceptionally(Function.identity()).thenCompose(Amazon::async$noCartJack);
               }

               var10000.join();
               ++var2;
            }
            break;
         case 2:
            var3.join();
            ++var2;
            break;
         default:
            throw new IllegalArgumentException();
      }

      while(var2 < 250) {
         var10000 = var0.GETREQ("Placing order [exp]", var0.api.placeOrderExp(), (Integer)null, new String[]{""});
         if (!var10000.isDone()) {
            var8 = var10000;
            return var8.exceptionally(Function.identity()).thenCompose(Amazon::async$noCartJack);
         }

         var7 = (String)var10000.join();
         if (var7.contains("a-color-success a-text-bold")) {
            var0.logger.info("Successfully checked out!");
            Analytics.success(var0.task, var0.itemJson, var0.api.proxyStringSafe());
            return CompletableFuture.completedFuture(true);
         }

         var0.logger.error("Checkout failed [exp]. Retrying...");
         if (var2 <= 5) {
            ++var2;
         } else {
            var10000 = VertxUtil.randomSleep((long)var0.task.getRetryDelay());
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(Amazon::async$noCartJack);
            }

            var10000.join();
            ++var2;
         }
      }

      return CompletableFuture.completedFuture(false);
   }

   public static CompletableFuture async$run(Amazon var0, CompletableFuture var1, String var2, String var3, int var4, Object var5) {
      Exception var14;
      label82: {
         CompletableFuture var10000;
         boolean var10001;
         label83: {
            String var11;
            CompletableFuture var13;
            label84: {
               label66: {
                  label76: {
                     switch (var4) {
                        case 0:
                           var10000 = CompletableFuture.allOf(harvesterFutures);
                           if (!var10000.isDone()) {
                              var13 = var10000;
                              return var13.exceptionally(Function.identity()).thenCompose(Amazon::async$run);
                           }
                           break;
                        case 1:
                           var10000 = var1;
                           break;
                        case 2:
                           var10000 = var1;
                           break label76;
                        case 3:
                           var10000 = var1;
                           break label66;
                        case 4:
                           var10000 = var1;
                           var11 = var2;
                           break label84;
                        case 5:
                           var10000 = var1;
                           break label83;
                        default:
                           throw new IllegalArgumentException();
                     }

                     var10000.join();

                     try {
                        var10000 = var0.smoothLogin();
                        if (!var10000.isDone()) {
                           var13 = var10000;
                           return var13.exceptionally(Function.identity()).thenCompose(Amazon::async$run);
                        }
                     } catch (Exception var10) {
                        var14 = var10;
                        var10001 = false;
                        break label82;
                     }
                  }

                  try {
                     var10000.join();
                     var10000 = var0.GETREQ("Init api", var0.api.corsairPage(), 200, new String[]{"amazonApiCsrfToken"});
                     if (!var10000.isDone()) {
                        var13 = var10000;
                        return var13.exceptionally(Function.identity()).thenCompose(Amazon::async$run);
                     }
                  } catch (Exception var9) {
                     var14 = var9;
                     var10001 = false;
                     break label82;
                  }
               }

               try {
                  var11 = (String)var10000.join();
                  var0.api.cartAPI.apiCsrf = Utils.quickParseFirst(var11, Patterns.API_CSRF);
                  var10000 = var0.GETREQ("Checking page...", var0.api.productPage(), 200, new String[]{""});
                  if (!var10000.isDone()) {
                     var13 = var10000;
                     return var13.exceptionally(Function.identity()).thenCompose(Amazon::async$run);
                  }
               } catch (Exception var8) {
                  var14 = var8;
                  var10001 = false;
                  break label82;
               }
            }

            try {
               var2 = (String)var10000.join();
               var0.itemJson = (new JsonObject()).put("title", Utils.quickParseFirst(var2, Patterns.TITLE));
               var10000 = var0.mixedFlow();
               if (!var10000.isDone()) {
                  var13 = var10000;
                  return var13.exceptionally(Function.identity()).thenCompose(Amazon::async$run);
               }
            } catch (Exception var7) {
               var14 = var7;
               var10001 = false;
               break label82;
            }
         }

         try {
            var10000.join();
            return CompletableFuture.completedFuture((Object)null);
         } catch (Exception var6) {
            var14 = var6;
            var10001 = false;
         }
      }

      Exception var12 = var14;
      var12.printStackTrace();
      var0.logger.error("Task interrupted: " + var12.getMessage());
      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture placeOrderBuynow(String var1, String var2, String var3, String var4) {
      this.logger.info("Processing [buynow]");
      int var5 = 0;

      while(super.running && var5++ < Integer.MAX_VALUE) {
         CompletableFuture var8;
         CompletableFuture var10;
         try {
            var10 = Request.send(this.api.buynowPlaceOrder(var1, var2, var3), this.api.buynowPlaceOrderForm(var3, var4));
            if (!var10.isDone()) {
               var8 = var10;
               return var8.exceptionally(Function.identity()).thenCompose(Amazon::async$placeOrderBuynow);
            }

            HttpResponse var6 = (HttpResponse)var10.join();
            if (var6 != null) {
               String var7;
               if (var6.statusCode() == 200 && (var7 = var6.getHeader("location")) != null && var7.contains("thankyou")) {
                  this.logger.info("Successfully checked out!");
                  Analytics.success(this.task, this.itemJson, this.api.proxyStringSafe());
                  return CompletableFuture.completedFuture((Object)null);
               }

               if (var5 < 2) {
                  continue;
               }

               Logger var11 = this.logger;
               int var10002 = var6.statusCode();
               var11.warn("Failed processing payment: '{}'", "" + var10002 + var6.statusMessage());
            }

            var10 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
            if (!var10.isDone()) {
               var8 = var10;
               return var8.exceptionally(Function.identity()).thenCompose(Amazon::async$placeOrderBuynow);
            }

            var10.join();
         } catch (Throwable var9) {
            this.logger.error("Error processing payment : {}", var9.getMessage());
            var10 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
            if (!var10.isDone()) {
               var8 = var10;
               return var8.exceptionally(Function.identity()).thenCompose(Amazon::async$placeOrderBuynow);
            }

            var10.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public Amazon(Task var1, int var2) {
      super(var2);
      this.task = var1;
      this.api = new AmazonAPI(this.task);
      super.setClient(this.api);
   }

   static {
      for(int var0 = 0; var0 < harvesterFutures.length; ++var0) {
         harvesterFutures[var0] = (new LoginHarvester()).start();
      }

   }

   public CompletableFuture placeOrderNormal(MultiMap var1) {
      this.logger.info("Processing [normal]");
      int var2 = 0;

      while(super.running && var2++ < Integer.MAX_VALUE) {
         CompletableFuture var5;
         CompletableFuture var7;
         try {
            var7 = Request.send(this.api.normalPlaceOrder(), var1);
            if (!var7.isDone()) {
               var5 = var7;
               return var5.exceptionally(Function.identity()).thenCompose(Amazon::async$placeOrderNormal);
            }

            HttpResponse var3 = (HttpResponse)var7.join();
            if (var3 != null) {
               if (var3.bodyAsString().contains("<meta http-equiv=\"refresh\" content=\"0; url=/gp/buy/duplicate-order/handlers/display.html")) {
                  this.logger.info("Duplicate order");
                  Analytics.failure("Duplicate order", this.task, var3.bodyAsString(), this.api.proxyStringSafe());
               } else {
                  String var4;
                  if (var3.statusCode() == 200 && (var4 = var3.getHeader("x-amz-checkout-sub-page-type")) != null && var4.contains("place-your-decoupled-order")) {
                     this.logger.info("Successfully checked out!");
                     Analytics.success(this.task, this.itemJson, this.api.proxyStringSafe());
                     return CompletableFuture.completedFuture((Object)null);
                  }

                  if (var2 < 2) {
                     continue;
                  }

                  Logger var8 = this.logger;
                  int var10002 = var3.statusCode();
                  var8.warn("Failed processing payment: '{}'", "" + var10002 + var3.statusMessage());
               }
            }

            var7 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
            if (!var7.isDone()) {
               var5 = var7;
               return var5.exceptionally(Function.identity()).thenCompose(Amazon::async$placeOrderNormal);
            }

            var7.join();
         } catch (Throwable var6) {
            var6.printStackTrace();
            this.logger.error("Error processing payment : {}", var6.getMessage());
            var7 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
            if (!var7.isDone()) {
               var5 = var7;
               return var5.exceptionally(Function.identity()).thenCompose(Amazon::async$placeOrderNormal);
            }

            var7.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture genericExecute(String var1, Supplier var2, Integer var3) {
      int var4 = 0;

      while(var4++ < Integer.MAX_VALUE) {
         CompletableFuture var8;
         CompletableFuture var10;
         try {
            Pair var5 = (Pair)var2.get();
            var10 = Request.send((HttpRequest)var5.first, var5.second);
            if (!var10.isDone()) {
               var8 = var10;
               return var8.exceptionally(Function.identity()).thenCompose(Amazon::async$genericExecute);
            }

            HttpResponse var6 = (HttpResponse)var10.join();
            if (var6 != null) {
               String var7 = var6.bodyAsString();
               if (var6.statusCode() == var3) {
                  return CompletableFuture.completedFuture(var7);
               }

               Logger var11 = this.logger;
               String var10002 = var1.toLowerCase(Locale.ROOT);
               int var10003 = var6.statusCode();
               var11.warn("Failed {}: '{}'", var10002, "" + var10003 + var6.statusMessage());
               var10 = VertxUtil.randomSleep((long)this.task.getMonitorDelay());
               if (!var10.isDone()) {
                  var8 = var10;
                  return var8.exceptionally(Function.identity()).thenCompose(Amazon::async$genericExecute);
               }

               var10.join();
            } else {
               var10 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
               if (!var10.isDone()) {
                  var8 = var10;
                  return var8.exceptionally(Function.identity()).thenCompose(Amazon::async$genericExecute);
               }

               var10.join();
            }
         } catch (Throwable var9) {
            this.logger.error("Error {}: {}", var1.toLowerCase(Locale.ROOT), var9.getMessage());
            if (this.logger.isDebugEnabled()) {
               var9.printStackTrace();
            }

            var10 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
            if (!var10.isDone()) {
               var8 = var10;
               return var8.exceptionally(Function.identity()).thenCompose(Amazon::async$genericExecute);
            }

            var10.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture cartHtml() {
      CompletableFuture var10000 = this.GETREQ("Checking product", this.api.cartPage(), 200, new String[]{"CSRF"});
      CompletableFuture var8;
      if (!var10000.isDone()) {
         var8 = var10000;
         return var8.exceptionally(Function.identity()).thenCompose(Amazon::async$cartHtml);
      } else {
         String var1 = (String)var10000.join();
         var10000 = this.GETREQ("Checking coupons", this.api.promoPage(), 200, new String[]{"anti-csrf"});
         if (!var10000.isDone()) {
            var8 = var10000;
            return var8.exceptionally(Function.identity()).thenCompose(Amazon::async$cartHtml);
         } else {
            String var2 = (String)var10000.join();
            this.api.updateCsrf(Utils.quickParseFirst(var1, Patterns.CSRF));
            this.api.genCsm(Utils.quickParseFirst(var1, Patterns.CSM_PREFIX));
            this.api.updateGlow(Utils.quickParseFirst(var1, Patterns.GLOW));
            this.api.cartAPI.anti_csrf = Utils.quickParseFirstNonEmpty(var2, ANTI_CSRF_PAT_P_PAGE);
            String var3 = "Adding to cart [M]";
            this.logger.info("Adding to cart [M]");

            while(this.running) {
               try {
                  Triplet var4 = (Triplet)this.api.cartAPI.smartEndpoint().get();
                  var10000 = Request.send((HttpRequest)var4.first, var4.second);
                  if (!var10000.isDone()) {
                     var8 = var10000;
                     return var8.exceptionally(Function.identity()).thenCompose(Amazon::async$cartHtml);
                  }

                  HttpResponse var5 = (HttpResponse)var10000.join();
                  if (var5 != null) {
                     String var6 = var5.bodyAsString();
                     int var7 = !Utils.containsAnyWords(var6, successfulCartIndicators) && (var5.statusCode() != 302 || !Utils.containsAnyWords(var5.getHeader("location"), successfulRedirectIndicators)) ? 0 : 1;
                     if (var7 != 0) {
                        this.isTurbo = (Boolean)var4.third;
                        return CompletableFuture.completedFuture(var6);
                     }

                     if (Utils.containsAllWords(var6, this.successfulFinalPageIndicators)) {
                        this.isTurbo = false;
                        return CompletableFuture.completedFuture(var6);
                     }

                     Logger var10 = this.logger;
                     String var10002 = var3.toLowerCase(Locale.ROOT);
                     int var10003 = var5.statusCode();
                     var10.warn("Failed {}: '{}'", var10002, "" + var10003 + var5.statusMessage());
                     var10000 = VertxUtil.randomSleep((long)this.task.getMonitorDelay());
                     if (!var10000.isDone()) {
                        var8 = var10000;
                        return var8.exceptionally(Function.identity()).thenCompose(Amazon::async$cartHtml);
                     }

                     var10000.join();
                  }
               } catch (Throwable var9) {
                  this.logger.error("Error {}: {}", var3.toLowerCase(Locale.ROOT), var9.getMessage());
                  if (this.logger.isDebugEnabled()) {
                     var9.printStackTrace();
                  }

                  var10000 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
                  if (!var10000.isDone()) {
                     var8 = var10000;
                     return var8.exceptionally(Function.identity()).thenCompose(Amazon::async$cartHtml);
                  }

                  var10000.join();
               }
            }

            return CompletableFuture.completedFuture((Object)null);
         }
      }
   }
}
