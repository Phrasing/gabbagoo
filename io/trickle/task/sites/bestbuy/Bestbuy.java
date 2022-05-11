package io.trickle.task.sites.bestbuy;

import io.netty.handler.codec.http.cookie.Cookie;
import io.trickle.account.Account;
import io.trickle.account.AccountController;
import io.trickle.core.Controller;
import io.trickle.core.Engine;
import io.trickle.core.VertxSingleton;
import io.trickle.core.actor.TaskActor;
import io.trickle.harvester.LoginController;
import io.trickle.harvester.LoginHarvester;
import io.trickle.imap.MailClient;
import io.trickle.imap.MessageUtils;
import io.trickle.task.Task;
import io.trickle.task.sites.yeezy.Yeezy;
import io.trickle.util.Pair;
import io.trickle.util.Utils;
import io.trickle.util.analytics.Analytics;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.codec.BodyCodec;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.Message;
import org.apache.logging.log4j.Logger;

public class Bestbuy extends TaskActor {
   public boolean isQueue;
   public String a2ctransactionwait = null;
   public static Pattern ORDER_FAIL_REASON = Pattern.compile("\"errors\":\\[\\{\"errorCode\":\"(.*?)\"");
   public String a2ctransactionreferenceid = null;
   public MailClient imapClient;
   public int successCounter = 0;
   public static Pattern VERIFICATION_CODE = Pattern.compile("Verification code:.*?([0-9]{6,7})", 32);
   public boolean browser;
   public static Pattern publicKeyPattern = Pattern.compile("\\\\r\\\\n(.*?)\\\\");
   public Task task;
   public String a2ctransactioncode = null;
   public BestbuyAPI api;
   public static Pattern orderDataPattern = Pattern.compile("var orderData = (.*?);");
   public static Pattern UUID_PATTERN = Pattern.compile("(.{8})(.{4})(.{4})(.{4})(.{12})");
   public boolean preload;

   public static CompletableFuture async$sendEnforcedReq(Bestbuy var0, HttpRequest var1, CompletableFuture var2, HttpResponse var3, int var4, Object var5) {
      CompletableFuture var10000;
      HttpResponse var6;
      CompletableFuture var7;
      switch (var4) {
         case 0:
            var10000 = Request.send(var1);
            if (!var10000.isDone()) {
               var7 = var10000;
               return var7.exceptionally(Function.identity()).thenCompose(Bestbuy::async$sendEnforcedReq);
            }

            var6 = (HttpResponse)var10000.join();
            break;
         case 1:
            var6 = (HttpResponse)var2.join();
            break;
         case 2:
            var10000 = var2;
            var6 = var3;
            var10000.join();
            var10000 = VertxUtil.hardCodedSleep(500L);
            if (!var10000.isDone()) {
               var7 = var10000;
               return var7.exceptionally(Function.identity()).thenCompose(Bestbuy::async$sendEnforcedReq);
            }

            var10000.join();
            var10000 = Request.send(var1);
            if (!var10000.isDone()) {
               var7 = var10000;
               return var7.exceptionally(Function.identity()).thenCompose(Bestbuy::async$sendEnforcedReq);
            }

            var6 = (HttpResponse)var10000.join();
            break;
         case 3:
            var10000 = var2;
            var6 = var3;
            var10000.join();
            var10000 = Request.send(var1);
            if (!var10000.isDone()) {
               var7 = var10000;
               return var7.exceptionally(Function.identity()).thenCompose(Bestbuy::async$sendEnforcedReq);
            }

            var6 = (HttpResponse)var10000.join();
            break;
         case 4:
            var6 = (HttpResponse)var2.join();
            break;
         default:
            throw new IllegalArgumentException();
      }

      while(var6 == null || var6.statusCode() == 403) {
         var0.successCounter = 0;
         var10000 = var0.sendSensor();
         if (!var10000.isDone()) {
            var7 = var10000;
            return var7.exceptionally(Function.identity()).thenCompose(Bestbuy::async$sendEnforcedReq);
         }

         var10000.join();
         var10000 = VertxUtil.hardCodedSleep(500L);
         if (!var10000.isDone()) {
            var7 = var10000;
            return var7.exceptionally(Function.identity()).thenCompose(Bestbuy::async$sendEnforcedReq);
         }

         var10000.join();
         var10000 = Request.send(var1);
         if (!var10000.isDone()) {
            var7 = var10000;
            return var7.exceptionally(Function.identity()).thenCompose(Bestbuy::async$sendEnforcedReq);
         }

         var6 = (HttpResponse)var10000.join();
      }

      return CompletableFuture.completedFuture(var6);
   }

   public void updateSensorUrlFromHTML(String var1) {
      if (var1 == null) {
         this.logger.error("Unable to update sensor URL. Continuing");
      } else {
         Matcher var2 = Yeezy.SENSOR_URL_PATTERN.matcher(var1);

         String var3;
         for(var3 = "/2yECBVJi5xf17/zRM/BInoAzUb5KA/YDGOzGht/VjtXHEtQXw/Wmt4/NTM9SV8"; var2.find(); var3 = var2.group(1)) {
         }

         this.api.setSensorUrl("https://www.bestbuy.com" + var3);
      }
   }

   public static CompletableFuture async$getLineId(Bestbuy var0, CompletableFuture var1, int var2, Object var3) {
      CompletableFuture var10000;
      switch (var2) {
         case 0:
            var10000 = var0.GETREQ("Starting checkout", var0.api.checkoutPage(), 200, "orderData = {\"id\":\"");
            if (!var10000.isDone()) {
               CompletableFuture var6 = var10000;
               return var6.exceptionally(Function.identity()).thenCompose(Bestbuy::async$getLineId);
            }
            break;
         case 1:
            var10000 = var1;
            break;
         default:
            throw new IllegalArgumentException();
      }

      String var4 = (String)var10000.join();
      JsonObject var5 = new JsonObject((String)Objects.requireNonNull(Utils.quickParseFirst(var4, orderDataPattern)));
      var0.api.cartId = var5.getJsonArray("items").getJsonObject(0).getString("id");
      var0.api.id = var5.getString("id");
      var0.api.orderId = var5.getString("customerOrderId");
      var0.api.paymentId = var5.getJsonObject("payment").getString("id");
      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$getLoginVerificationCode(Bestbuy var0, Account var1, CompletableFuture var2, Message[] var3, int var4, Object var5) {
      CompletableFuture var10000;
      Message[] var6;
      CompletableFuture var8;
      switch (var4) {
         case 0:
            if (var0.imapClient == null) {
               var0.imapClient = MailClient.create(VertxSingleton.INSTANCE.get());
            }

            var10000 = var0.imapClient.connectFut(var1.getUser(), var1.getPass());
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$getLoginVerificationCode);
            }

            var10000.join();
            var10000 = var0.imapClient.readInboxFuture(Login.SEARCH_TERM);
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$getLoginVerificationCode);
            }

            var6 = (Message[])var10000.join();
            break;
         case 1:
            var2.join();
            var10000 = var0.imapClient.readInboxFuture(Login.SEARCH_TERM);
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$getLoginVerificationCode);
            }

            var6 = (Message[])var10000.join();
            break;
         case 2:
            var6 = (Message[])var2.join();
            break;
         case 3:
            var2.join();
            var10000 = var0.imapClient.readInboxFuture(Login.SEARCH_TERM);
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$getLoginVerificationCode);
            }

            var6 = (Message[])var10000.join();
            break;
         case 4:
            var6 = (Message[])var2.join();
            break;
         default:
            throw new IllegalArgumentException();
      }

      while(var6.length == 0) {
         var0.logger.error("Waiting for email to arrive... [{}]", var1.getUser());
         var10000 = VertxUtil.randomSleep((long)var0.task.getRetryDelay());
         if (!var10000.isDone()) {
            var8 = var10000;
            return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$getLoginVerificationCode);
         }

         var10000.join();
         var10000 = var0.imapClient.readInboxFuture(Login.SEARCH_TERM);
         if (!var10000.isDone()) {
            var8 = var10000;
            return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$getLoginVerificationCode);
         }

         var6 = (Message[])var10000.join();
      }

      String var7 = MessageUtils.getTextFromMessage(var6[var6.length - 1]);
      return CompletableFuture.completedFuture(Utils.quickParseFirst(var7, VERIFICATION_CODE));
   }

   public CompletableFuture sendSensor() {
      String var1 = this.api.userAgent;
      int var2 = 0;

      while(super.running) {
         BestbuyAPI var10000 = this.api;
         CompletableFuture var10001 = this.api.hawkAPI.updateUserAgent();
         if (!var10001.isDone()) {
            CompletableFuture var9 = var10001;
            BestbuyAPI var11 = var10000;
            return var9.exceptionally(Function.identity()).thenCompose(Bestbuy::async$sendSensor);
         }

         var10000.userAgent = (String)var10001.join();
         this.logger.debug("Solving...");

         CompletableFuture var8;
         CompletableFuture var12;
         try {
            var12 = this.api.hawkAPI.getSensorPayload(this.api.getCookies().getCookieValue("_abck"));
            if (!var12.isDone()) {
               var8 = var12;
               return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$sendSensor);
            }

            String var3 = (String)var12.join();
            Buffer var4 = (new JsonObject()).put("sensor_data", var3).toBuffer();
            HttpRequest var5 = this.api.sendSensor();
            var12 = Request.send(var5, var4);
            if (!var12.isDone()) {
               var8 = var12;
               return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$sendSensor);
            }

            HttpResponse var6 = (HttpResponse)var12.join();
            if (var6 != null) {
               String var7 = var6.bodyAsString();
               if (var7.contains("false") || var2++ >= 2) {
                  this.api.userAgent = var1;
                  return CompletableFuture.completedFuture((Object)null);
               }
            }
         } catch (Throwable var10) {
            this.logger.error("Error on sensor. Retrying: {}", "unexpected response");
            var12 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
            if (!var12.isDone()) {
               var8 = var12;
               return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$sendSensor);
            }

            var12.join();
         }
      }

      this.api.userAgent = var1;
      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$placeOrder(Bestbuy var0, int var1, String var2, CompletableFuture var3, String var4, int var5, Object var6) {
      CompletableFuture var10000;
      CompletableFuture var8;
      switch (var5) {
         case 0:
            var1 = 0;
            var2 = "";
            if (var2.contains("state\":\"SUBMITTED") && !var2.contains("orderAlreadySubmitted")) {
               return CompletableFuture.completedFuture(var2);
            }

            var10000 = var0.POSTREQ("Placing order...", var0.api.placeOrder(), var0.api.placeOrderForm(), (Integer)null, (Pair)null, "{");
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
            }
            break;
         case 1:
            var10000 = var3;
            break;
         case 2:
            var3.join();
            var10000 = var0.GETREQ("Refreshing checkout", var0.api.refreshCheckout(), 200, (String[])null);
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
            }

            var10000.join();
            if (var4.contains("CC_REQ_ERROR")) {
               var10000 = var0.POSTREQ("Submitting payment", var0.api.vaultCC(), var0.api.vaultJson(), 200, (Pair)null, "{\"paymentId\":\"");
               if (!var10000.isDone()) {
                  var8 = var10000;
                  return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
               }

               var10000.join();
               var10000 = var0.POSTREQ("Checking payment", var0.api.refreshPayment(), Buffer.buffer("{}"), 200, (Pair)null, "{\"productTotal\":\"");
               if (!var10000.isDone()) {
                  var8 = var10000;
                  return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
               }

               var10000.join();
            } else if (var4.contains("CC_AUTH_FAILURE")) {
               var0.logger.error("CARD DECLINED");
               var10000 = var0.POSTREQ("Submitting payment", var0.api.vaultCC(), var0.api.vaultJson(), 200, (Pair)null, "{\"paymentId\":\"");
               if (!var10000.isDone()) {
                  var8 = var10000;
                  return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
               }

               var10000.join();
               var10000 = var0.POSTREQ("Checking payment", var0.api.refreshPayment(), Buffer.buffer("{}"), 200, (Pair)null, "{\"productTotal\":\"");
               if (!var10000.isDone()) {
                  var8 = var10000;
                  return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
               }

               var10000.join();
            } else if (var4.contains("EMPTY_CID")) {
               var0.logger.error("Resubmitting CC");
               var10000 = var0.POSTREQ("Submitting payment", var0.api.vaultCC(), var0.api.vaultJson(), 200, (Pair)null, "{\"paymentId\":\"");
               if (!var10000.isDone()) {
                  var8 = var10000;
                  return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
               }

               var10000.join();
               var10000 = var0.POSTREQ("Checking payment", var0.api.refreshPayment(), Buffer.buffer("{}"), 200, (Pair)null, "{\"productTotal\":\"");
               if (!var10000.isDone()) {
                  var8 = var10000;
                  return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
               }

               var10000.join();
            } else if (var4.contains("EMAIL_MISSING")) {
               var0.logger.error("Resubmitting EMAIL");
               var10000 = var0.POSTREQ("Submitting email", var0.api.submitEmail(), var0.api.emailJson(), 200, (Pair)null, "\"quantity\":1,");
               if (!var10000.isDone()) {
                  var8 = var10000;
                  return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
               }

               var10000.join();
            } else {
               var10000 = VertxUtil.randomSleep((long)var0.task.getMonitorDelay());
               if (!var10000.isDone()) {
                  var8 = var10000;
                  return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
               }

               var10000.join();
            }

            if (var2.contains("state\":\"SUBMITTED") && !var2.contains("orderAlreadySubmitted")) {
               return CompletableFuture.completedFuture(var2);
            }

            var10000 = var0.POSTREQ("Placing order...", var0.api.placeOrder(), var0.api.placeOrderForm(), (Integer)null, (Pair)null, "{");
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
            }
            break;
         case 3:
            var3.join();
            if (var4.contains("CC_REQ_ERROR")) {
               var10000 = var0.POSTREQ("Submitting payment", var0.api.vaultCC(), var0.api.vaultJson(), 200, (Pair)null, "{\"paymentId\":\"");
               if (!var10000.isDone()) {
                  var8 = var10000;
                  return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
               }

               var10000.join();
               var10000 = var0.POSTREQ("Checking payment", var0.api.refreshPayment(), Buffer.buffer("{}"), 200, (Pair)null, "{\"productTotal\":\"");
               if (!var10000.isDone()) {
                  var8 = var10000;
                  return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
               }

               var10000.join();
            } else if (var4.contains("CC_AUTH_FAILURE")) {
               var0.logger.error("CARD DECLINED");
               var10000 = var0.POSTREQ("Submitting payment", var0.api.vaultCC(), var0.api.vaultJson(), 200, (Pair)null, "{\"paymentId\":\"");
               if (!var10000.isDone()) {
                  var8 = var10000;
                  return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
               }

               var10000.join();
               var10000 = var0.POSTREQ("Checking payment", var0.api.refreshPayment(), Buffer.buffer("{}"), 200, (Pair)null, "{\"productTotal\":\"");
               if (!var10000.isDone()) {
                  var8 = var10000;
                  return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
               }

               var10000.join();
            } else if (var4.contains("EMPTY_CID")) {
               var0.logger.error("Resubmitting CC");
               var10000 = var0.POSTREQ("Submitting payment", var0.api.vaultCC(), var0.api.vaultJson(), 200, (Pair)null, "{\"paymentId\":\"");
               if (!var10000.isDone()) {
                  var8 = var10000;
                  return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
               }

               var10000.join();
               var10000 = var0.POSTREQ("Checking payment", var0.api.refreshPayment(), Buffer.buffer("{}"), 200, (Pair)null, "{\"productTotal\":\"");
               if (!var10000.isDone()) {
                  var8 = var10000;
                  return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
               }

               var10000.join();
            } else if (var4.contains("EMAIL_MISSING")) {
               var0.logger.error("Resubmitting EMAIL");
               var10000 = var0.POSTREQ("Submitting email", var0.api.submitEmail(), var0.api.emailJson(), 200, (Pair)null, "\"quantity\":1,");
               if (!var10000.isDone()) {
                  var8 = var10000;
                  return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
               }

               var10000.join();
            } else {
               var10000 = VertxUtil.randomSleep((long)var0.task.getMonitorDelay());
               if (!var10000.isDone()) {
                  var8 = var10000;
                  return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
               }

               var10000.join();
            }

            if (var2.contains("state\":\"SUBMITTED") && !var2.contains("orderAlreadySubmitted")) {
               return CompletableFuture.completedFuture(var2);
            }

            var10000 = var0.POSTREQ("Placing order...", var0.api.placeOrder(), var0.api.placeOrderForm(), (Integer)null, (Pair)null, "{");
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
            }
            break;
         case 4:
            var3.join();
            var10000 = var0.POSTREQ("Checking payment", var0.api.refreshPayment(), Buffer.buffer("{}"), 200, (Pair)null, "{\"productTotal\":\"");
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
            }

            var10000.join();
            if (var2.contains("state\":\"SUBMITTED") && !var2.contains("orderAlreadySubmitted")) {
               return CompletableFuture.completedFuture(var2);
            }

            var10000 = var0.POSTREQ("Placing order...", var0.api.placeOrder(), var0.api.placeOrderForm(), (Integer)null, (Pair)null, "{");
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
            }
            break;
         case 5:
            var3.join();
            if (var2.contains("state\":\"SUBMITTED") && !var2.contains("orderAlreadySubmitted")) {
               return CompletableFuture.completedFuture(var2);
            }

            var10000 = var0.POSTREQ("Placing order...", var0.api.placeOrder(), var0.api.placeOrderForm(), (Integer)null, (Pair)null, "{");
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
            }
            break;
         case 6:
            var3.join();
            var10000 = var0.POSTREQ("Checking payment", var0.api.refreshPayment(), Buffer.buffer("{}"), 200, (Pair)null, "{\"productTotal\":\"");
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
            }

            var10000.join();
            if (var2.contains("state\":\"SUBMITTED") && !var2.contains("orderAlreadySubmitted")) {
               return CompletableFuture.completedFuture(var2);
            }

            var10000 = var0.POSTREQ("Placing order...", var0.api.placeOrder(), var0.api.placeOrderForm(), (Integer)null, (Pair)null, "{");
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
            }
            break;
         case 7:
            var3.join();
            if (var2.contains("state\":\"SUBMITTED") && !var2.contains("orderAlreadySubmitted")) {
               return CompletableFuture.completedFuture(var2);
            }

            var10000 = var0.POSTREQ("Placing order...", var0.api.placeOrder(), var0.api.placeOrderForm(), (Integer)null, (Pair)null, "{");
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
            }
            break;
         case 8:
            var3.join();
            var10000 = var0.POSTREQ("Checking payment", var0.api.refreshPayment(), Buffer.buffer("{}"), 200, (Pair)null, "{\"productTotal\":\"");
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
            }

            var10000.join();
            if (var2.contains("state\":\"SUBMITTED") && !var2.contains("orderAlreadySubmitted")) {
               return CompletableFuture.completedFuture(var2);
            }

            var10000 = var0.POSTREQ("Placing order...", var0.api.placeOrder(), var0.api.placeOrderForm(), (Integer)null, (Pair)null, "{");
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
            }
            break;
         case 9:
            var3.join();
            if (var2.contains("state\":\"SUBMITTED") && !var2.contains("orderAlreadySubmitted")) {
               return CompletableFuture.completedFuture(var2);
            }

            var10000 = var0.POSTREQ("Placing order...", var0.api.placeOrder(), var0.api.placeOrderForm(), (Integer)null, (Pair)null, "{");
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
            }
            break;
         case 10:
            var3.join();
            if (var2.contains("state\":\"SUBMITTED") && !var2.contains("orderAlreadySubmitted")) {
               return CompletableFuture.completedFuture(var2);
            }

            var10000 = var0.POSTREQ("Placing order...", var0.api.placeOrder(), var0.api.placeOrderForm(), (Integer)null, (Pair)null, "{");
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
            }
            break;
         case 11:
            var3.join();
            if (var2.contains("state\":\"SUBMITTED") && !var2.contains("orderAlreadySubmitted")) {
               return CompletableFuture.completedFuture(var2);
            }

            var10000 = var0.POSTREQ("Placing order...", var0.api.placeOrder(), var0.api.placeOrderForm(), (Integer)null, (Pair)null, "{");
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
            }
            break;
         default:
            throw new IllegalArgumentException();
      }

      label484:
      do {
         do {
            var2 = (String)var10000.join();
            String var7 = Utils.quickParseFirst(var2, ORDER_FAIL_REASON);
            if (var7 != null) {
               if (!var7.equals("BAD_REQUEST")) {
                  if (var7.equals("UNAUTHORIZED")) {
                     ++var1;
                     if (var1 >= 3) {
                        throw new Throwable("Checkout expired. Restarting task...");
                     }

                     var0.logger.error("Placing order \"soft-ban\"");
                     var0.api.rotateProxy();
                  } else {
                     var1 = 0;
                     var10000 = var0.POSTREQ("Handling -> " + var7, var0.api.submitContact(), var0.api.contactJson(), 200, (Pair)null, "\"quantity\":1,");
                     if (!var10000.isDone()) {
                        var8 = var10000;
                        return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
                     }

                     var10000.join();
                  }
               } else {
                  ++var1;
                  if (var1 >= 3) {
                     throw new Throwable("Checkout expired. Restarting task...");
                  }

                  var0.logger.error("Placing order \"soft-ban\"");
                  var0.api.rotateProxy();
               }

               var10000 = var0.GETREQ("Refreshing checkout", var0.api.refreshCheckout(), 200, (String[])null);
               if (!var10000.isDone()) {
                  var8 = var10000;
                  return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
               }

               var10000.join();
               if (var7.contains("CC_REQ_ERROR")) {
                  var10000 = var0.POSTREQ("Submitting payment", var0.api.vaultCC(), var0.api.vaultJson(), 200, (Pair)null, "{\"paymentId\":\"");
                  if (!var10000.isDone()) {
                     var8 = var10000;
                     return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
                  }

                  var10000.join();
                  var10000 = var0.POSTREQ("Checking payment", var0.api.refreshPayment(), Buffer.buffer("{}"), 200, (Pair)null, "{\"productTotal\":\"");
                  if (!var10000.isDone()) {
                     var8 = var10000;
                     return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
                  }

                  var10000.join();
               } else if (var7.contains("CC_AUTH_FAILURE")) {
                  var0.logger.error("CARD DECLINED");
                  var10000 = var0.POSTREQ("Submitting payment", var0.api.vaultCC(), var0.api.vaultJson(), 200, (Pair)null, "{\"paymentId\":\"");
                  if (!var10000.isDone()) {
                     var8 = var10000;
                     return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
                  }

                  var10000.join();
                  var10000 = var0.POSTREQ("Checking payment", var0.api.refreshPayment(), Buffer.buffer("{}"), 200, (Pair)null, "{\"productTotal\":\"");
                  if (!var10000.isDone()) {
                     var8 = var10000;
                     return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
                  }

                  var10000.join();
               } else if (var7.contains("EMPTY_CID")) {
                  var0.logger.error("Resubmitting CC");
                  var10000 = var0.POSTREQ("Submitting payment", var0.api.vaultCC(), var0.api.vaultJson(), 200, (Pair)null, "{\"paymentId\":\"");
                  if (!var10000.isDone()) {
                     var8 = var10000;
                     return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
                  }

                  var10000.join();
                  var10000 = var0.POSTREQ("Checking payment", var0.api.refreshPayment(), Buffer.buffer("{}"), 200, (Pair)null, "{\"productTotal\":\"");
                  if (!var10000.isDone()) {
                     var8 = var10000;
                     return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
                  }

                  var10000.join();
               } else if (var7.contains("EMAIL_MISSING")) {
                  var0.logger.error("Resubmitting EMAIL");
                  var10000 = var0.POSTREQ("Submitting email", var0.api.submitEmail(), var0.api.emailJson(), 200, (Pair)null, "\"quantity\":1,");
                  if (!var10000.isDone()) {
                     var8 = var10000;
                     return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
                  }

                  var10000.join();
               } else {
                  var10000 = VertxUtil.randomSleep((long)var0.task.getMonitorDelay());
                  if (!var10000.isDone()) {
                     var8 = var10000;
                     return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
                  }

                  var10000.join();
               }

               if (var2.contains("state\":\"SUBMITTED") && !var2.contains("orderAlreadySubmitted")) {
                  return CompletableFuture.completedFuture(var2);
               }

               var10000 = var0.POSTREQ("Placing order...", var0.api.placeOrder(), var0.api.placeOrderForm(), (Integer)null, (Pair)null, "{");
               continue label484;
            }

            if (var2.contains("state\":\"SUBMITTED") && !var2.contains("orderAlreadySubmitted")) {
               return CompletableFuture.completedFuture(var2);
            }

            var10000 = var0.POSTREQ("Placing order...", var0.api.placeOrder(), var0.api.placeOrderForm(), (Integer)null, (Pair)null, "{");
         } while(var10000.isDone());

         var8 = var10000;
         return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
      } while(var10000.isDone());

      var8 = var10000;
      return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
   }

   public CompletableFuture atc(boolean var1) {
      CompletableFuture var10000 = this.sendSensor();
      CompletableFuture var9;
      if (!var10000.isDone()) {
         var9 = var10000;
         return var9.exceptionally(Function.identity()).thenCompose(Bestbuy::async$atc);
      } else {
         var10000.join();
         this.logger.info("Waiting for restock");

         while(super.running) {
            try {
               var10000 = this.sendEnforcedReq(this.api.atc(this.a2ctransactioncode, this.a2ctransactionreferenceid), var1 != 0 ? this.api.atcForm("4900942") : this.api.atcForm());
               if (!var10000.isDone()) {
                  var9 = var10000;
                  return var9.exceptionally(Function.identity()).thenCompose(Bestbuy::async$atc);
               }

               HttpResponse var2 = (HttpResponse)var10000.join();
               if (var2 != null) {
                  String var3 = var2.bodyAsString();
                  if (var2.statusCode() == 200 && var3.contains("\"cartCount\":1")) {
                     VertxUtil.sendSignal(this.task.getKeywords()[0]);
                     this.logger.info("Successfully atc! Cookie -> [{}]", Analytics.exportVT(this.api.getCookies()));
                     return CompletableFuture.completedFuture((Object)null);
                  }

                  if (var3.contains("redirectUrl")) {
                     return CompletableFuture.completedFuture(var2.bodyAsJsonObject().getString("redirectUrl"));
                  }

                  if (var3.contains("CONSTRAINED_ITEM")) {
                     if (this.a2ctransactioncode != null) {
                        this.logger.error("Queue looped (semi-normal)");
                        var10000 = this.freshenSession();
                        if (!var10000.isDone()) {
                           var9 = var10000;
                           return var9.exceptionally(Function.identity()).thenCompose(Bestbuy::async$atc);
                        }

                        var10000.join();
                        var10000 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
                        if (!var10000.isDone()) {
                           var9 = var10000;
                           return var9.exceptionally(Function.identity()).thenCompose(Bestbuy::async$atc);
                        }

                        var10000.join();
                     } else {
                        this.a2ctransactioncode = var2.getHeader("a2ctransactioncode");
                        this.a2ctransactionreferenceid = var2.getHeader("a2ctransactionreferenceid");
                        this.a2ctransactionwait = var2.getHeader("a2ctransactionwait");
                        int var11 = this.a2ctransactionwait == null ? decodeUuidV1(this.a2ctransactioncode) : Integer.parseInt(this.a2ctransactionwait) * 1000;
                        int var12 = 240000;
                        if (this.a2ctransactionwait != null) {
                           this.a2ctransactioncode = genCustomUuidV1(var11);
                        }

                        if (this.task.getKeywords().length == 2 && Utils.isInteger(this.task.getKeywords()[1])) {
                           var12 = Integer.parseInt(this.task.getKeywords()[1]);
                        }

                        if (var11 > var12) {
                           this.logger.info("Queue is {} minutes long. [{}] Attempting to get a lower queue time...", (double)(var11 / 1000) / Double.longBitsToDouble(4633641066610819072L), this.task.getKeywords()[0]);
                           var10000 = this.freshenSession();
                           if (!var10000.isDone()) {
                              var9 = var10000;
                              return var9.exceptionally(Function.identity()).thenCompose(Bestbuy::async$atc);
                           }

                           var10000.join();
                           var10000 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
                           if (!var10000.isDone()) {
                              var9 = var10000;
                              return var9.exceptionally(Function.identity()).thenCompose(Bestbuy::async$atc);
                           }

                           var10000.join();
                        } else {
                           this.logger.info("Queue is {} minutes long. Waiting...", (double)(var11 / 1000) / Double.longBitsToDouble(4633641066610819072L));
                           var10000 = VertxUtil.hardCodedSleep((long)(var11 + 3000));
                           if (!var10000.isDone()) {
                              var9 = var10000;
                              return var9.exceptionally(Function.identity()).thenCompose(Bestbuy::async$atc);
                           }

                           var10000.join();
                           this.logger.info("Passed queue!");
                        }
                     }
                     continue;
                  }

                  if (var3.contains("Sorry, there was a problem adding the item to your cart.")) {
                     this.logger.info("Error waiting for restock/queue. Site down or temp ban.");
                     var10000 = this.freshenSession();
                     if (!var10000.isDone()) {
                        var9 = var10000;
                        return var9.exceptionally(Function.identity()).thenCompose(Bestbuy::async$atc);
                     }

                     var10000.join();
                     this.api.rotateProxy();
                  } else if (var3.contains("ITEM_NOT_SELLABLE")) {
                     this.logger.info("Item not for online sale.");
                     if (this.browser) {
                        var10000 = this.freshenSession();
                        if (!var10000.isDone()) {
                           var9 = var10000;
                           return var9.exceptionally(Function.identity()).thenCompose(Bestbuy::async$atc);
                        }

                        var10000.join();
                     } else {
                        this.api.getCookies().removeAnyMatch("vt");
                        this.api.getCookies().put("vt", UUID.randomUUID().toString(), ".bestbuy.com");
                     }

                     if (++this.successCounter % 6 == 0) {
                        var10000 = this.sendSensor();
                        if (!var10000.isDone()) {
                           var9 = var10000;
                           return var9.exceptionally(Function.identity()).thenCompose(Bestbuy::async$atc);
                        }

                        var10000.join();
                     }
                  } else {
                     Logger var13 = this.logger;
                     String var10002 = var2.bodyAsString();
                     var13.warn("Error waiting for atc/queue: '{}'", var10002 + var2.statusCode() + var2.statusMessage());
                     DateTimeFormatter var4 = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy");
                     JsonArray var5 = new JsonArray();
                     Iterator var6 = this.api.getCookies().get(true, "bestbuy.com", "/").iterator();

                     while(var6.hasNext()) {
                        Cookie var7 = (Cookie)var6.next();
                        JsonObject var8 = new JsonObject();
                        var8.put("domain", var7.domain());
                        var8.put("name", var7.name());
                        var8.put("path", var7.path());
                        var8.put("value", var7.value());
                        var5.add(var8);
                     }
                  }
               }

               var10000 = VertxUtil.signalSleep(this.task.getKeywords()[0], (long)this.task.getMonitorDelay());
               if (!var10000.isDone()) {
                  var9 = var10000;
                  return var9.exceptionally(Function.identity()).thenCompose(Bestbuy::async$atc);
               }

               var10000.join();
            } catch (Throwable var10) {
               this.logger.error("Error waiting for restock: {}", var10.getMessage());
               var10000 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
               if (!var10000.isDone()) {
                  var9 = var10000;
                  return var9.exceptionally(Function.identity()).thenCompose(Bestbuy::async$atc);
               }

               var10000.join();
            }
         }

         return CompletableFuture.completedFuture((Object)null);
      }
   }

   public CompletableFuture clearCart() {
      Object var10001 = null;
      CompletableFuture var10002 = this.GETREQ("Checking cart items", this.api.getCartItems(), 200, "cartV2");
      CompletableFuture var6;
      if (!var10002.isDone()) {
         var6 = var10002;
         return var6.exceptionally(Function.identity()).thenCompose(Bestbuy::async$clearCart);
      } else {
         String var5 = (String)var10002.join();
         JsonObject var1 = (new JsonObject(var5)).getJsonObject("cart");
         this.api.orderId = var1.getString("id");
         JsonArray var2 = var1.getJsonArray("lineItems");

         for(int var3 = 0; var2 != null && var3 < var2.size(); ++var3) {
            String var4 = var2.getJsonObject(var3).getString("id");
            CompletableFuture var10000 = this.GETREQ("Deleting item " + var3, this.api.deleteItem(var4), 200, "order");
            if (!var10000.isDone()) {
               var6 = var10000;
               return var6.exceptionally(Function.identity()).thenCompose(Bestbuy::async$clearCart);
            }

            var10000.join();
         }

         return CompletableFuture.completedFuture((Object)null);
      }
   }

   public CompletableFuture placeOrder() {
      int var1 = 0;
      String var2 = "";

      while(true) {
         CompletableFuture var10000;
         String var3;
         CompletableFuture var4;
         do {
            if (var2.contains("state\":\"SUBMITTED") && !var2.contains("orderAlreadySubmitted")) {
               return CompletableFuture.completedFuture(var2);
            }

            var10000 = this.POSTREQ("Placing order...", this.api.placeOrder(), this.api.placeOrderForm(), (Integer)null, (Pair)null, "{");
            if (!var10000.isDone()) {
               var4 = var10000;
               return var4.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
            }

            var2 = (String)var10000.join();
            var3 = Utils.quickParseFirst(var2, ORDER_FAIL_REASON);
         } while(var3 == null);

         if (!var3.equals("BAD_REQUEST") && !var3.equals("UNAUTHORIZED")) {
            var1 = 0;
            var10000 = this.POSTREQ("Handling -> " + var3, this.api.submitContact(), this.api.contactJson(), 200, (Pair)null, "\"quantity\":1,");
            if (!var10000.isDone()) {
               var4 = var10000;
               return var4.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
            }

            var10000.join();
         } else {
            ++var1;
            if (var1 >= 3) {
               throw new Throwable("Checkout expired. Restarting task...");
            }

            this.logger.error("Placing order \"soft-ban\"");
            this.api.rotateProxy();
         }

         var10000 = this.GETREQ("Refreshing checkout", this.api.refreshCheckout(), 200, (String[])null);
         if (!var10000.isDone()) {
            var4 = var10000;
            return var4.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
         }

         var10000.join();
         if (var3.contains("CC_REQ_ERROR")) {
            var10000 = this.POSTREQ("Submitting payment", this.api.vaultCC(), this.api.vaultJson(), 200, (Pair)null, "{\"paymentId\":\"");
            if (!var10000.isDone()) {
               var4 = var10000;
               return var4.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
            }

            var10000.join();
            var10000 = this.POSTREQ("Checking payment", this.api.refreshPayment(), Buffer.buffer("{}"), 200, (Pair)null, "{\"productTotal\":\"");
            if (!var10000.isDone()) {
               var4 = var10000;
               return var4.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
            }

            var10000.join();
         } else if (var3.contains("CC_AUTH_FAILURE")) {
            this.logger.error("CARD DECLINED");
            var10000 = this.POSTREQ("Submitting payment", this.api.vaultCC(), this.api.vaultJson(), 200, (Pair)null, "{\"paymentId\":\"");
            if (!var10000.isDone()) {
               var4 = var10000;
               return var4.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
            }

            var10000.join();
            var10000 = this.POSTREQ("Checking payment", this.api.refreshPayment(), Buffer.buffer("{}"), 200, (Pair)null, "{\"productTotal\":\"");
            if (!var10000.isDone()) {
               var4 = var10000;
               return var4.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
            }

            var10000.join();
         } else if (var3.contains("EMPTY_CID")) {
            this.logger.error("Resubmitting CC");
            var10000 = this.POSTREQ("Submitting payment", this.api.vaultCC(), this.api.vaultJson(), 200, (Pair)null, "{\"paymentId\":\"");
            if (!var10000.isDone()) {
               var4 = var10000;
               return var4.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
            }

            var10000.join();
            var10000 = this.POSTREQ("Checking payment", this.api.refreshPayment(), Buffer.buffer("{}"), 200, (Pair)null, "{\"productTotal\":\"");
            if (!var10000.isDone()) {
               var4 = var10000;
               return var4.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
            }

            var10000.join();
         } else if (var3.contains("EMAIL_MISSING")) {
            this.logger.error("Resubmitting EMAIL");
            var10000 = this.POSTREQ("Submitting email", this.api.submitEmail(), this.api.emailJson(), 200, (Pair)null, "\"quantity\":1,");
            if (!var10000.isDone()) {
               var4 = var10000;
               return var4.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
            }

            var10000.join();
         } else {
            var10000 = VertxUtil.randomSleep((long)this.task.getMonitorDelay());
            if (!var10000.isDone()) {
               var4 = var10000;
               return var4.exceptionally(Function.identity()).thenCompose(Bestbuy::async$placeOrder);
            }

            var10000.join();
         }
      }
   }

   public static CompletableFuture async$freshenSession(Bestbuy var0, String var1, String var2, CompletableFuture var3, int var4, Object var5) {
      CompletableFuture var10000;
      switch (var4) {
         case 0:
            var0.a2ctransactioncode = null;
            var0.a2ctransactionreferenceid = null;
            var1 = var0.api.getCookies().getCookieValue("bm_sz");
            var2 = var0.api.getCookies().getCookieValue("_abck");
            var0.api.getCookies().clear();
            var0.api.getCookies().put("_abck", var2, ".bestbuy.com");
            var10000 = var0.GETREQ("Refreshing session", var0.api.productPage(), 200, "script type=\"text/javascript");
            if (!var10000.isDone()) {
               CompletableFuture var7 = var10000;
               return var7.exceptionally(Function.identity()).thenCompose(Bestbuy::async$freshenSession);
            }
            break;
         case 1:
            var10000 = var3;
            break;
         default:
            throw new IllegalArgumentException();
      }

      String var6 = (String)var10000.join();
      var0.updateSensorUrlFromHTML(var6);
      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$atc(Bestbuy param0, int param1, CompletableFuture param2, HttpResponse param3, String param4, int param5, int param6, Throwable param7, int param8, Object param9) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$sendSensor(Bestbuy param0, String param1, int param2, BestbuyAPI param3, CompletableFuture param4, String param5, Buffer param6, HttpRequest param7, Throwable param8, int param9, Object param10) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture freshenSession() {
      this.a2ctransactioncode = null;
      this.a2ctransactionreferenceid = null;
      String var1 = this.api.getCookies().getCookieValue("bm_sz");
      String var2 = this.api.getCookies().getCookieValue("_abck");
      this.api.getCookies().clear();
      this.api.getCookies().put("_abck", var2, ".bestbuy.com");
      CompletableFuture var10000 = this.GETREQ("Refreshing session", this.api.productPage(), 200, "script type=\"text/javascript");
      if (!var10000.isDone()) {
         CompletableFuture var4 = var10000;
         return var4.exceptionally(Function.identity()).thenCompose(Bestbuy::async$freshenSession);
      } else {
         String var3 = (String)var10000.join();
         this.updateSensorUrlFromHTML(var3);
         return CompletableFuture.completedFuture((Object)null);
      }
   }

   public CompletableFuture sendEnforcedReq(HttpRequest var1) {
      CompletableFuture var10000 = Request.send(var1);
      CompletableFuture var3;
      if (!var10000.isDone()) {
         var3 = var10000;
         return var3.exceptionally(Function.identity()).thenCompose(Bestbuy::async$sendEnforcedReq);
      } else {
         HttpResponse var2;
         for(var2 = (HttpResponse)var10000.join(); var2 == null || var2.statusCode() == 403; var2 = (HttpResponse)var10000.join()) {
            this.successCounter = 0;
            var10000 = this.sendSensor();
            if (!var10000.isDone()) {
               var3 = var10000;
               return var3.exceptionally(Function.identity()).thenCompose(Bestbuy::async$sendEnforcedReq);
            }

            var10000.join();
            var10000 = VertxUtil.hardCodedSleep(500L);
            if (!var10000.isDone()) {
               var3 = var10000;
               return var3.exceptionally(Function.identity()).thenCompose(Bestbuy::async$sendEnforcedReq);
            }

            var10000.join();
            var10000 = Request.send(var1);
            if (!var10000.isDone()) {
               var3 = var10000;
               return var3.exceptionally(Function.identity()).thenCompose(Bestbuy::async$sendEnforcedReq);
            }
         }

         return CompletableFuture.completedFuture(var2);
      }
   }

   public CompletableFuture sendEnforcedReq(HttpRequest var1, Object var2) {
      CompletableFuture var10000 = Request.send(var1, var2);
      CompletableFuture var4;
      if (!var10000.isDone()) {
         var4 = var10000;
         return var4.exceptionally(Function.identity()).thenCompose(Bestbuy::async$sendEnforcedReq$1);
      } else {
         HttpResponse var3;
         for(var3 = (HttpResponse)var10000.join(); var3 == null || var3.statusCode() == 403; var3 = (HttpResponse)var10000.join()) {
            this.successCounter = 0;
            var10000 = this.sendSensor();
            if (!var10000.isDone()) {
               var4 = var10000;
               return var4.exceptionally(Function.identity()).thenCompose(Bestbuy::async$sendEnforcedReq$1);
            }

            var10000.join();
            var10000 = VertxUtil.hardCodedSleep(500L);
            if (!var10000.isDone()) {
               var4 = var10000;
               return var4.exceptionally(Function.identity()).thenCompose(Bestbuy::async$sendEnforcedReq$1);
            }

            var10000.join();
            var10000 = Request.send(var1, var2);
            if (!var10000.isDone()) {
               var4 = var10000;
               return var4.exceptionally(Function.identity()).thenCompose(Bestbuy::async$sendEnforcedReq$1);
            }
         }

         return CompletableFuture.completedFuture(var3);
      }
   }

   public CompletableFuture encryptCard() {
      CompletableFuture var10000 = this.GETREQ("Initializing...", this.api.fetchPublicKey(), 200, "public");
      if (!var10000.isDone()) {
         CompletableFuture var4 = var10000;
         return var4.exceptionally(Function.identity()).thenCompose(Bestbuy::async$encryptCard);
      } else {
         String var1 = (String)var10000.join();
         String var2 = Utils.quickParseFirst(var1, publicKeyPattern);
         String var3 = var1.split("keyId\":\"")[1].split("\"")[0];
         this.api.encryptedCard = Encryption.getFullEncrypted(this.task.getProfile().getCardNumber(), var2, var3);
         return CompletableFuture.completedFuture((Object)null);
      }
   }

   public static CompletableFuture async$sendEnforcedReq$1(Bestbuy var0, HttpRequest var1, Object var2, CompletableFuture var3, HttpResponse var4, int var5, Object var6) {
      CompletableFuture var10000;
      HttpResponse var7;
      CompletableFuture var8;
      switch (var5) {
         case 0:
            var10000 = Request.send(var1, var2);
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$sendEnforcedReq$1);
            }

            var7 = (HttpResponse)var10000.join();
            break;
         case 1:
            var7 = (HttpResponse)var3.join();
            break;
         case 2:
            var10000 = var3;
            var7 = var4;
            var10000.join();
            var10000 = VertxUtil.hardCodedSleep(500L);
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$sendEnforcedReq$1);
            }

            var10000.join();
            var10000 = Request.send(var1, var2);
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$sendEnforcedReq$1);
            }

            var7 = (HttpResponse)var10000.join();
            break;
         case 3:
            var10000 = var3;
            var7 = var4;
            var10000.join();
            var10000 = Request.send(var1, var2);
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$sendEnforcedReq$1);
            }

            var7 = (HttpResponse)var10000.join();
            break;
         case 4:
            var7 = (HttpResponse)var3.join();
            break;
         default:
            throw new IllegalArgumentException();
      }

      while(var7 == null || var7.statusCode() == 403) {
         var0.successCounter = 0;
         var10000 = var0.sendSensor();
         if (!var10000.isDone()) {
            var8 = var10000;
            return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$sendEnforcedReq$1);
         }

         var10000.join();
         var10000 = VertxUtil.hardCodedSleep(500L);
         if (!var10000.isDone()) {
            var8 = var10000;
            return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$sendEnforcedReq$1);
         }

         var10000.join();
         var10000 = Request.send(var1, var2);
         if (!var10000.isDone()) {
            var8 = var10000;
            return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$sendEnforcedReq$1);
         }

         var7 = (HttpResponse)var10000.join();
      }

      return CompletableFuture.completedFuture(var7);
   }

   public static CompletableFuture async$encryptCard(Bestbuy var0, CompletableFuture var1, int var2, Object var3) {
      CompletableFuture var10000;
      switch (var2) {
         case 0:
            var10000 = var0.GETREQ("Initializing...", var0.api.fetchPublicKey(), 200, "public");
            if (!var10000.isDone()) {
               CompletableFuture var4 = var10000;
               return var4.exceptionally(Function.identity()).thenCompose(Bestbuy::async$encryptCard);
            }
            break;
         case 1:
            var10000 = var1;
            break;
         default:
            throw new IllegalArgumentException();
      }

      String var5 = (String)var10000.join();
      String var6 = Utils.quickParseFirst(var5, publicKeyPattern);
      String var7 = var5.split("keyId\":\"")[1].split("\"")[0];
      var0.api.encryptedCard = Encryption.getFullEncrypted(var0.task.getProfile().getCardNumber(), var6, var7);
      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$run(Bestbuy param0, CompletableFuture param1, BestbuyAPI param2, String param3, String param4, String param5, CompletableFuture param6, int param7, Object param8) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$initLoginHarvesters(Bestbuy var0, LoginHarvester[] var1, int var2, int var3, Object var4, CompletableFuture var5, int var6, Object var7) {
      switch (var6) {
         case 0:
            if (!var0.browser) {
               return CompletableFuture.completedFuture((Object)null);
            }

            var1 = LoginHarvester.LOGIN_HARVESTERS;
            var2 = var1.length;
            var3 = 0;
            break;
         case 1:
            var5.join();
            ++var3;
            break;
         default:
            throw new IllegalArgumentException();
      }

      while(var3 < var2) {
         LoginHarvester var8 = var1[var3];
         CompletableFuture var10000 = var8.start();
         if (!var10000.isDone()) {
            var5 = var10000;
            return var5.exceptionally(Function.identity()).thenCompose(Bestbuy::async$initLoginHarvesters);
         }

         var10000.join();
         ++var3;
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture run() {
      CompletableFuture var10000 = this.initLoginHarvesters();
      CompletableFuture var6;
      if (!var10000.isDone()) {
         var6 = var10000;
         return var6.exceptionally(Function.identity()).thenCompose(Bestbuy::async$run);
      } else {
         var10000.join();

         try {
            BestbuyAPI var11 = this.api;
            CompletableFuture var10001 = this.api.hawkAPI.updateUserAgent();
            if (!var10001.isDone()) {
               CompletableFuture var7 = var10001;
               BestbuyAPI var10 = var11;
               return var7.exceptionally(Function.identity()).thenCompose(Bestbuy::async$run);
            } else {
               var11.userAgent = (String)var10001.join();
               var10000 = this.encryptCard();
               if (!var10000.isDone()) {
                  var6 = var10000;
                  return var6.exceptionally(Function.identity()).thenCompose(Bestbuy::async$run);
               } else {
                  var10000.join();
                  var10000 = this.GETREQ("Visiting product", this.api.productPage(), 200, "script type=\"text/javascript");
                  if (!var10000.isDone()) {
                     var6 = var10000;
                     return var6.exceptionally(Function.identity()).thenCompose(Bestbuy::async$run);
                  } else {
                     String var1 = (String)var10000.join();
                     this.updateSensorUrlFromHTML(var1);
                     var10000 = this.POSTREQ("Getting store-ids", this.api.getStoreId(), this.api.storeIdForm(), 200, (Pair)null, "locations");
                     if (!var10000.isDone()) {
                        var6 = var10000;
                        return var6.exceptionally(Function.identity()).thenCompose(Bestbuy::async$run);
                     } else {
                        String var2 = (String)var10000.join();
                        this.api.store = (new JsonObject(var2)).getJsonObject("ispu").getJsonArray("locations").getJsonObject(0).getString("id");
                        if (this.preload) {
                           var10000 = this.atc(true);
                           if (!var10000.isDone()) {
                              var6 = var10000;
                              return var6.exceptionally(Function.identity()).thenCompose(Bestbuy::async$run);
                           }

                           var10000.join();
                           var10000 = this.getLineId();
                           if (!var10000.isDone()) {
                              var6 = var10000;
                              return var6.exceptionally(Function.identity()).thenCompose(Bestbuy::async$run);
                           }

                           var10000.join();
                           var10000 = this.POSTREQ("Submitting payment", this.api.vaultCC(), this.api.vaultJson(), 200, (Pair)null, "{\"paymentId\":\"");
                           if (!var10000.isDone()) {
                              var6 = var10000;
                              return var6.exceptionally(Function.identity()).thenCompose(Bestbuy::async$run);
                           }

                           var10000.join();
                           var10000 = this.POSTREQ("Checking payment", this.api.refreshPayment(), Buffer.buffer("{}"), 200, (Pair)null, "{\"productTotal\":\"");
                           if (!var10000.isDone()) {
                              var6 = var10000;
                              return var6.exceptionally(Function.identity()).thenCompose(Bestbuy::async$run);
                           }

                           var10000.join();
                           var10000 = this.clearCart();
                           if (!var10000.isDone()) {
                              var6 = var10000;
                              return var6.exceptionally(Function.identity()).thenCompose(Bestbuy::async$run);
                           }

                           var10000.join();
                        }

                        var10000 = this.atc(false);
                        if (!var10000.isDone()) {
                           var6 = var10000;
                           return var6.exceptionally(Function.identity()).thenCompose(Bestbuy::async$run);
                        } else {
                           String var3 = (String)var10000.join();
                           if (var3.contains("/identity")) {
                              this.api.getCookies().removeAnyMatch("bby_rdp");
                              this.api.getCookies().put("bby_rdp", "l", ".bestbuy.com");
                              this.logger.info(var3);
                              if (this.browser) {
                                 var10000 = this.login(var3);
                                 if (!var10000.isDone()) {
                                    var6 = var10000;
                                    return var6.exceptionally(Function.identity()).thenCompose(Bestbuy::async$run);
                                 }

                                 var10000.join();
                              }

                              this.api.getCookies().removeAnyMatch("bby_rdp");
                              this.api.getCookies().put("bby_rdp", "s", ".bestbuy.com");
                              var10000 = this.clearCart();
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(Bestbuy::async$run);
                              }

                              var10000.join();
                              this.logger.info("Attempting re-cart...");
                              var10000 = this.atc(false);
                              if (!var10000.isDone()) {
                                 var6 = var10000;
                                 return var6.exceptionally(Function.identity()).thenCompose(Bestbuy::async$run);
                              }

                              var10000.join();
                           }

                           while(true) {
                              try {
                                 var10000 = this.getLineId();
                                 if (!var10000.isDone()) {
                                    var6 = var10000;
                                    return var6.exceptionally(Function.identity()).thenCompose(Bestbuy::async$run);
                                 }

                                 var10000.join();
                                 var10000 = this.POSTREQ("Submitting contact", this.api.submitContact(), this.api.contactJson(), 200, (Pair)null, "\"quantity\":1,");
                                 if (!var10000.isDone()) {
                                    var6 = var10000;
                                    return var6.exceptionally(Function.identity()).thenCompose(Bestbuy::async$run);
                                 }

                                 var10000.join();
                                 CompletableFuture var4 = this.POSTREQ("Submitting email", this.api.submitEmail(), this.api.emailJson(), 200, (Pair)null, "\"quantity\":1,");
                                 var10000 = this.POSTREQ("Submitting payment", this.api.vaultCC(), this.api.vaultJson(), 200, (Pair)null, "{\"paymentId\":\"");
                                 if (!var10000.isDone()) {
                                    var6 = var10000;
                                    return var6.exceptionally(Function.identity()).thenCompose(Bestbuy::async$run);
                                 }

                                 var10000.join();
                                 if (!var4.isDone()) {
                                    return var4.exceptionally(Function.identity()).thenCompose(Bestbuy::async$run);
                                 }

                                 var4.join();
                                 var10000 = this.POSTREQ("Checking payment", this.api.refreshPayment(), Buffer.buffer("{}"), 200, (Pair)null, "{\"productTotal\":\"");
                                 if (!var10000.isDone()) {
                                    var6 = var10000;
                                    return var6.exceptionally(Function.identity()).thenCompose(Bestbuy::async$run);
                                 }

                                 var10000.join();
                                 var10000 = this.placeOrder();
                                 if (!var10000.isDone()) {
                                    var6 = var10000;
                                    return var6.exceptionally(Function.identity()).thenCompose(Bestbuy::async$run);
                                 }

                                 String var5 = (String)var10000.join();
                                 Analytics.success(this.task, new JsonObject(var5), this.api.proxyStringSafe());
                                 this.logger.info("Successfully checked out!");
                                 return CompletableFuture.completedFuture((Object)null);
                              } catch (Throwable var8) {
                                 this.logger.error("Caught checkout exception {}", var8.getMessage());
                              }
                           }
                        }
                     }
                  }
               }
            }
         } catch (Throwable var9) {
            var9.printStackTrace();
            this.logger.error("Caught exception {}", var9.getMessage());
            return CompletableFuture.completedFuture((Object)null);
         }
      }
   }

   public static CompletableFuture async$clearCart(Bestbuy var0, CompletableFuture var1, JsonObject var2, JsonArray var3, int var4, String var5, int var6, Object var7) {
      JsonObject var8;
      JsonArray var9;
      int var10;
      CompletableFuture var12;
      CompletableFuture var10000;
      label35: {
         Object var10001;
         CompletableFuture var10002;
         switch (var6) {
            case 0:
               var10001 = null;
               var10002 = var0.GETREQ("Checking cart items", var0.api.getCartItems(), 200, "cartV2");
               if (!var10002.isDone()) {
                  var12 = var10002;
                  return var12.exceptionally(Function.identity()).thenCompose(Bestbuy::async$clearCart);
               }
               break;
            case 1:
               var10000 = null;
               var10001 = null;
               var10002 = var1;
               break;
            case 2:
               var9 = var3;
               var8 = var2;
               var1.join();
               var10 = var4 + 1;
               break label35;
            default:
               throw new IllegalArgumentException();
         }

         var5 = (String)var10002.join();
         var8 = (new JsonObject(var5)).getJsonObject("cart");
         var0.api.orderId = var8.getString("id");
         var9 = var8.getJsonArray("lineItems");
      }

      for(var10 = 0; var9 != null && var10 < var9.size(); ++var10) {
         String var11 = var9.getJsonObject(var10).getString("id");
         var10000 = var0.GETREQ("Deleting item " + var10, var0.api.deleteItem(var11), 200, "order");
         if (!var10000.isDone()) {
            var12 = var10000;
            return var12.exceptionally(Function.identity()).thenCompose(Bestbuy::async$clearCart);
         }

         var10000.join();
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture getLoginVerificationCode(Account var1) {
      if (this.imapClient == null) {
         this.imapClient = MailClient.create(VertxSingleton.INSTANCE.get());
      }

      CompletableFuture var10000 = this.imapClient.connectFut(var1.getUser(), var1.getPass());
      CompletableFuture var4;
      if (!var10000.isDone()) {
         var4 = var10000;
         return var4.exceptionally(Function.identity()).thenCompose(Bestbuy::async$getLoginVerificationCode);
      } else {
         var10000.join();
         var10000 = this.imapClient.readInboxFuture(Login.SEARCH_TERM);
         if (!var10000.isDone()) {
            var4 = var10000;
            return var4.exceptionally(Function.identity()).thenCompose(Bestbuy::async$getLoginVerificationCode);
         } else {
            Message[] var2;
            for(var2 = (Message[])var10000.join(); var2.length == 0; var2 = (Message[])var10000.join()) {
               this.logger.error("Waiting for email to arrive... [{}]", var1.getUser());
               var10000 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
               if (!var10000.isDone()) {
                  var4 = var10000;
                  return var4.exceptionally(Function.identity()).thenCompose(Bestbuy::async$getLoginVerificationCode);
               }

               var10000.join();
               var10000 = this.imapClient.readInboxFuture(Login.SEARCH_TERM);
               if (!var10000.isDone()) {
                  var4 = var10000;
                  return var4.exceptionally(Function.identity()).thenCompose(Bestbuy::async$getLoginVerificationCode);
               }
            }

            String var3 = MessageUtils.getTextFromMessage(var2[var2.length - 1]);
            return CompletableFuture.completedFuture(Utils.quickParseFirst(var3, VERIFICATION_CODE));
         }
      }
   }

   public CompletableFuture POSTREQ(String var1, HttpRequest var2, Object var3, Integer var4, Pair var5, String... var6) {
      this.logger.info(var1);

      while(super.running) {
         CompletableFuture var9;
         CompletableFuture var10000;
         try {
            var10000 = this.sendEnforcedReq(var2, var3);
            if (!var10000.isDone()) {
               var9 = var10000;
               return var9.exceptionally(Function.identity()).thenCompose(Bestbuy::async$POSTREQ);
            }

            HttpResponse var7 = (HttpResponse)var10000.join();
            if (var7 != null) {
               boolean var8;
               if (var7.statusCode() == 302) {
                  var8 = var6 == null || var7.getHeader("location").contains(var6[0]);
               } else {
                  var8 = var6 == null || Utils.containsAllWords(var7.bodyAsString(), var6);
               }

               if ((var4 == null || var7.statusCode() == var4) && var8) {
                  return CompletableFuture.completedFuture(var7.statusCode() == 302 ? var7.getHeader("location") : var7.bodyAsString());
               }

               Logger var11;
               String var10001;
               int var10002;
               if (var5 != null) {
                  if (var7.statusCode() >= 400) {
                     this.logger.warn("Failed " + var1.toLowerCase(Locale.ROOT) + ": '{}'", var7.bodyAsString().replace("\n", ""));
                  } else if (var5.first instanceof Integer && (Integer)var5.first == var7.statusCode()) {
                     this.logger.warn("Failed " + var1.toLowerCase(Locale.ROOT) + ": '{}'", var5.second);
                  } else if (var7.bodyAsString().contains(var5.first.toString())) {
                     this.logger.warn("Failed " + var1.toLowerCase(Locale.ROOT) + ": '{}'", var5.second);
                  } else if (var7.statusCode() == 302 && var7.getHeader("location").contains(var5.first.toString())) {
                     this.logger.warn("Failed " + var1.toLowerCase(Locale.ROOT) + ": '{}'", var5.second);
                  } else {
                     var11 = this.logger;
                     var10001 = "Failed " + var1.toLowerCase(Locale.ROOT) + ": '{}'";
                     var10002 = var7.statusCode();
                     var11.warn(var10001, "" + var10002 + var7.statusMessage());
                  }
               } else {
                  var11 = this.logger;
                  var10001 = "Failed " + var1.toLowerCase(Locale.ROOT) + ": '{}'";
                  var10002 = var7.statusCode();
                  var11.warn(var10001, "" + var10002 + var7.statusMessage());
               }

               this.logger.debug(var7.bodyAsString().replace("\n", ""));
            }

            var10000 = VertxUtil.randomSleep((long)this.task.getMonitorDelay());
            if (!var10000.isDone()) {
               var9 = var10000;
               return var9.exceptionally(Function.identity()).thenCompose(Bestbuy::async$POSTREQ);
            }

            var10000.join();
         } catch (Throwable var10) {
            this.logger.error("Error " + var1.toLowerCase(Locale.ROOT) + ": {}", var10.getMessage());
            var10000 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
            if (!var10000.isDone()) {
               var9 = var10000;
               return var9.exceptionally(Function.identity()).thenCompose(Bestbuy::async$POSTREQ);
            }

            var10000.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$login(Bestbuy var0, String var1, CompletableFuture var2, String var3, CompletableFuture var4, String var5, String var6, AccountController var7, Account var8, String var9, JsonObject var10, String var11, int var12, Object var13) {
      CompletableFuture var10000;
      label116: {
         String var14;
         CompletableFuture var15;
         String var16;
         AccountController var17;
         Account var18;
         String var19;
         JsonObject var20;
         CompletableFuture var22;
         label99: {
            label117: {
               label118: {
                  label119: {
                     label92: {
                        label120: {
                           label89: {
                              label88: {
                                 String var10001;
                                 String var10003;
                                 String var10004;
                                 AccountController var10005;
                                 Account var10006;
                                 String var10007;
                                 switch (var12) {
                                    case 0:
                                       var10000 = var0.GETREQ("Fetching values", var0.api.loginPage(var1), 200, "identity");
                                       if (!var10000.isDone()) {
                                          var22 = var10000;
                                          return var22.exceptionally(Function.identity()).thenCompose(Bestbuy::async$login);
                                       }
                                       break;
                                    case 1:
                                       var10000 = var2;
                                       break;
                                    case 2:
                                       var10000 = var4;
                                       var10001 = var3;
                                       var15 = var2;
                                       var14 = var10001;
                                       break label88;
                                    case 3:
                                       var10000 = var4;
                                       var10001 = var3;
                                       var16 = var5;
                                       var15 = var2;
                                       var14 = var10001;
                                       break label89;
                                    case 4:
                                       var10000 = var4;
                                       var10001 = var3;
                                       var10003 = var5;
                                       var10004 = var6;
                                       var17 = var7;
                                       var5 = var10004;
                                       var16 = var10003;
                                       var15 = var2;
                                       var14 = var10001;
                                       break label120;
                                    case 5:
                                       var10000 = var4;
                                       var10001 = var3;
                                       var10003 = var5;
                                       var10004 = var6;
                                       var10005 = var7;
                                       var18 = var8;
                                       var17 = var10005;
                                       var5 = var10004;
                                       var16 = var10003;
                                       var15 = var2;
                                       var14 = var10001;
                                       break label92;
                                    case 6:
                                       var10000 = var4;
                                       var10001 = var3;
                                       var10003 = var5;
                                       var10004 = var6;
                                       var10005 = var7;
                                       var18 = var8;
                                       var17 = var10005;
                                       var5 = var10004;
                                       var16 = var10003;
                                       var15 = var2;
                                       var14 = var10001;
                                       break label119;
                                    case 7:
                                       var10000 = var4;
                                       var10001 = var3;
                                       var10003 = var5;
                                       var10004 = var6;
                                       var10005 = var7;
                                       var10006 = var8;
                                       var10007 = var9;
                                       var20 = var10;
                                       var19 = var10007;
                                       var18 = var10006;
                                       var17 = var10005;
                                       var5 = var10004;
                                       var16 = var10003;
                                       var15 = var2;
                                       var14 = var10001;
                                       break label118;
                                    case 8:
                                       var10000 = var4;
                                       var10001 = var3;
                                       var10003 = var5;
                                       var10004 = var6;
                                       var10005 = var7;
                                       var10006 = var8;
                                       var10007 = var9;
                                       var20 = var10;
                                       var19 = var10007;
                                       var18 = var10006;
                                       var17 = var10005;
                                       var5 = var10004;
                                       var16 = var10003;
                                       var15 = var2;
                                       var14 = var10001;
                                       break label99;
                                    case 9:
                                       var10000 = var4;
                                       break label116;
                                    default:
                                       throw new IllegalArgumentException();
                                 }

                                 var14 = (String)var10000.join();
                                 var14 = var14.replace("</html>", "<script>document.querySelector(\"html\").innerHTML = `<h2>Waiting for completion</h2>`</script>\n</html>");
                                 var15 = LoginController.initBrowserLogin(var1, var0.api.getCookies().get(true, ".bestbuy.com", "/"), var0.api.proxyStringFull(), var0.api.getCookies(), var14);
                                 var0.api.login = Login.loginValues(var14);
                                 var10000 = var0.GETREQ("Fetching key (1/2)", var0.api.ciaUserActivity(), 200, "keyId");
                                 if (!var10000.isDone()) {
                                    var22 = var10000;
                                    return var22.exceptionally(Function.identity()).thenCompose(Bestbuy::async$login);
                                 }
                              }

                              var16 = (String)var10000.join();
                              var10000 = var0.GETREQ("Fetching key (2/2)", var0.api.emailGrid(), 200, "keyId");
                              if (!var10000.isDone()) {
                                 var22 = var10000;
                                 return var22.exceptionally(Function.identity()).thenCompose(Bestbuy::async$login);
                              }
                           }

                           var5 = (String)var10000.join();
                           var17 = (AccountController)Engine.get().getModule(Controller.ACCOUNT);
                           var10000 = var17.findAccount(var0.task.getProfile().getEmail(), true).toCompletionStage().toCompletableFuture();
                           if (!var10000.isDone()) {
                              var22 = var10000;
                              return var22.exceptionally(Function.identity()).thenCompose(Bestbuy::async$login);
                           }
                        }

                        var18 = (Account)var10000.join();
                        var10000 = var15;
                        if (!var15.isDone()) {
                           return var15.exceptionally(Function.identity()).thenCompose(Bestbuy::async$login);
                        }
                     }

                     var10000.join();
                     var10000 = var0.POSTREQ("Logging in...", var0.api.login(), var0.api.accountLoginForm(var18, new JsonObject(var16), new JsonObject(var5)), 200, (Pair)null, "status", "{");
                     if (!var10000.isDone()) {
                        var22 = var10000;
                        return var22.exceptionally(Function.identity()).thenCompose(Bestbuy::async$login);
                     }
                  }

                  var19 = (String)var10000.join();
                  if (var19.contains("success")) {
                     return CompletableFuture.completedFuture((Object)null);
                  }

                  if (!var19.contains("stepUpRequired")) {
                     return CompletableFuture.completedFuture((Object)null);
                  }

                  var20 = new JsonObject(var19);
                  var0.api.login.flowOptions = var20.getString("flowOptions");
                  var0.api.login.challengeType = var20.getString("challengeType");
                  if (!var0.api.login.challengeType.equals("2")) {
                     break label117;
                  }

                  var0.logger.error("Account requires password reset");
                  var10000 = var0.POSTREQ("Selecting verification...", var0.api.pickVerification(), var0.api.pickVerificationJson(), 200, (Pair)null, "success");
                  if (!var10000.isDone()) {
                     var22 = var10000;
                     return var22.exceptionally(Function.identity()).thenCompose(Bestbuy::async$login);
                  }
               }

               var10000.join();
            }

            var10000 = var0.getLoginVerificationCode(var18);
            if (!var10000.isDone()) {
               var22 = var10000;
               return var22.exceptionally(Function.identity()).thenCompose(Bestbuy::async$login);
            }
         }

         String var21 = (String)var10000.join();
         var10000 = var0.POSTREQ("Verifying Account...", var0.api.verificationCode(), var0.api.verificationJson(var21), 200, (Pair)null, "status");
         if (!var10000.isDone()) {
            var22 = var10000;
            return var22.exceptionally(Function.identity()).thenCompose(Bestbuy::async$login);
         }
      }

      var10000.join();
      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture getLineId() {
      CompletableFuture var10000 = this.GETREQ("Starting checkout", this.api.checkoutPage(), 200, "orderData = {\"id\":\"");
      if (!var10000.isDone()) {
         CompletableFuture var3 = var10000;
         return var3.exceptionally(Function.identity()).thenCompose(Bestbuy::async$getLineId);
      } else {
         String var1 = (String)var10000.join();
         JsonObject var2 = new JsonObject((String)Objects.requireNonNull(Utils.quickParseFirst(var1, orderDataPattern)));
         this.api.cartId = var2.getJsonArray("items").getJsonObject(0).getString("id");
         this.api.id = var2.getString("id");
         this.api.orderId = var2.getString("customerOrderId");
         this.api.paymentId = var2.getJsonObject("payment").getString("id");
         return CompletableFuture.completedFuture((Object)null);
      }
   }

   public static String genCustomUuidV1(int var0) {
      String var1 = String.valueOf(System.currentTimeMillis() + (long)var0 + 1L);
      return String.join("-", Utils.quickParseAllGroups((var1 + var1 + var1).substring(0, 32), UUID_PATTERN));
   }

   public static CompletableFuture async$POSTREQ(Bestbuy param0, String param1, HttpRequest param2, Object param3, Integer param4, Pair param5, String[] param6, CompletableFuture param7, HttpResponse param8, Throwable param9, int param10, Object param11) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture initLoginHarvesters() {
      if (this.browser) {
         LoginHarvester[] var1 = LoginHarvester.LOGIN_HARVESTERS;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            LoginHarvester var4 = var1[var3];
            CompletableFuture var10000 = var4.start();
            if (!var10000.isDone()) {
               CompletableFuture var5 = var10000;
               return var5.exceptionally(Function.identity()).thenCompose(Bestbuy::async$initLoginHarvesters);
            }

            var10000.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture login(String var1) {
      CompletableFuture var10000 = this.GETREQ("Fetching values", this.api.loginPage(var1), 200, "identity");
      CompletableFuture var11;
      if (!var10000.isDone()) {
         var11 = var10000;
         return var11.exceptionally(Function.identity()).thenCompose(Bestbuy::async$login);
      } else {
         String var2 = (String)var10000.join();
         var2 = var2.replace("</html>", "<script>document.querySelector(\"html\").innerHTML = `<h2>Waiting for completion</h2>`</script>\n</html>");
         CompletableFuture var3 = LoginController.initBrowserLogin(var1, this.api.getCookies().get(true, ".bestbuy.com", "/"), this.api.proxyStringFull(), this.api.getCookies(), var2);
         this.api.login = Login.loginValues(var2);
         var10000 = this.GETREQ("Fetching key (1/2)", this.api.ciaUserActivity(), 200, "keyId");
         if (!var10000.isDone()) {
            var11 = var10000;
            return var11.exceptionally(Function.identity()).thenCompose(Bestbuy::async$login);
         } else {
            String var4 = (String)var10000.join();
            var10000 = this.GETREQ("Fetching key (2/2)", this.api.emailGrid(), 200, "keyId");
            if (!var10000.isDone()) {
               var11 = var10000;
               return var11.exceptionally(Function.identity()).thenCompose(Bestbuy::async$login);
            } else {
               String var5 = (String)var10000.join();
               AccountController var6 = (AccountController)Engine.get().getModule(Controller.ACCOUNT);
               var10000 = var6.findAccount(this.task.getProfile().getEmail(), true).toCompletionStage().toCompletableFuture();
               if (!var10000.isDone()) {
                  var11 = var10000;
                  return var11.exceptionally(Function.identity()).thenCompose(Bestbuy::async$login);
               } else {
                  Account var7 = (Account)var10000.join();
                  if (!var3.isDone()) {
                     return var3.exceptionally(Function.identity()).thenCompose(Bestbuy::async$login);
                  } else {
                     var3.join();
                     var10000 = this.POSTREQ("Logging in...", this.api.login(), this.api.accountLoginForm(var7, new JsonObject(var4), new JsonObject(var5)), 200, (Pair)null, "status", "{");
                     if (!var10000.isDone()) {
                        var11 = var10000;
                        return var11.exceptionally(Function.identity()).thenCompose(Bestbuy::async$login);
                     } else {
                        String var8 = (String)var10000.join();
                        if (var8.contains("success")) {
                           return CompletableFuture.completedFuture((Object)null);
                        } else {
                           if (var8.contains("stepUpRequired")) {
                              JsonObject var9 = new JsonObject(var8);
                              this.api.login.flowOptions = var9.getString("flowOptions");
                              this.api.login.challengeType = var9.getString("challengeType");
                              if (this.api.login.challengeType.equals("2")) {
                                 this.logger.error("Account requires password reset");
                                 var10000 = this.POSTREQ("Selecting verification...", this.api.pickVerification(), this.api.pickVerificationJson(), 200, (Pair)null, "success");
                                 if (!var10000.isDone()) {
                                    var11 = var10000;
                                    return var11.exceptionally(Function.identity()).thenCompose(Bestbuy::async$login);
                                 }

                                 var10000.join();
                              }

                              var10000 = this.getLoginVerificationCode(var7);
                              if (!var10000.isDone()) {
                                 var11 = var10000;
                                 return var11.exceptionally(Function.identity()).thenCompose(Bestbuy::async$login);
                              }

                              String var10 = (String)var10000.join();
                              var10000 = this.POSTREQ("Verifying Account...", this.api.verificationCode(), this.api.verificationJson(var10), 200, (Pair)null, "status");
                              if (!var10000.isDone()) {
                                 var11 = var10000;
                                 return var11.exceptionally(Function.identity()).thenCompose(Bestbuy::async$login);
                              }

                              var10000.join();
                           }

                           return CompletableFuture.completedFuture((Object)null);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public static int decodeUuidV1(String var0) {
      String[] var1 = var0.split("-");
      return 10 * Integer.parseInt(var1[2] + var1[3], 16) / Integer.parseInt(var1[1], 16) * 100;
   }

   public Bestbuy(Task var1, int var2) {
      super(var2);
      this.task = var1;
      this.api = new BestbuyAPI(this.task);
      super.setClient(this.api);
      this.browser = this.task.getMode().contains("login");
      this.preload = this.task.getMode().contains("preload");
   }

   public static CompletableFuture async$GETREQ(Bestbuy param0, String param1, HttpRequest param2, Integer param3, String[] param4, int param5, CompletableFuture param6, HttpResponse param7, Throwable param8, int param9, Object param10) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture GETREQ(String var1, HttpRequest var2, Integer var3, String... var4) {
      this.logger.info(var1);

      while(super.running) {
         CompletableFuture var8;
         CompletableFuture var10000;
         try {
            int var5 = var4 != null && (var3 == null || var3 != 302) ? 0 : 1;
            HttpResponse var10;
            if (var5 != 0) {
               var10000 = this.sendEnforcedReq(var2.as(BodyCodec.none()));
               if (!var10000.isDone()) {
                  var8 = var10000;
                  return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$GETREQ);
               }

               var10 = (HttpResponse)var10000.join();
            } else {
               var10000 = this.sendEnforcedReq(var2);
               if (!var10000.isDone()) {
                  var8 = var10000;
                  return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$GETREQ);
               }

               var10 = (HttpResponse)var10000.join();
            }

            HttpResponse var6 = var10;
            if (var6 != null) {
               boolean var7;
               if (var6.statusCode() == 302) {
                  var7 = var4 == null || var6.getHeader("location").contains(var4[0]);
               } else {
                  var7 = var4 == null || Utils.containsAllWords(var6.bodyAsString(), var4);
               }

               if ((var3 == null || var6.statusCode() == var3) && var7) {
                  return CompletableFuture.completedFuture(var5 != 0 ? var6.getHeader("location") : var6.bodyAsString());
               }

               Logger var11 = this.logger;
               String var10001 = "Failed " + var1.toLowerCase(Locale.ROOT) + ": '{}'";
               int var10002 = var6.statusCode();
               var11.warn(var10001, "" + var10002 + var6.getHeader("location"));
            }

            var10000 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$GETREQ);
            }

            var10000.join();
         } catch (Throwable var9) {
            this.logger.error("Error " + var1.toLowerCase(Locale.ROOT) + ": {}", var9.getMessage());
            var10000 = super.randomSleep(this.task.getRetryDelay());
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(Bestbuy::async$GETREQ);
            }

            var10000.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }
}
