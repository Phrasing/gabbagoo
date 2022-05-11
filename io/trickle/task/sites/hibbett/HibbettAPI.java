package io.trickle.task.sites.hibbett;

import io.trickle.task.Task;
import io.trickle.task.antibot.impl.px.PXToken;
import io.trickle.task.antibot.impl.px.PXTokenAPI;
import io.trickle.task.antibot.impl.px.PXTokenBase;
import io.trickle.util.request.Headers$Pseudo;
import io.trickle.webclient.ClientType;
import io.trickle.webclient.RealClient;
import io.trickle.webclient.RealClientFactory;
import io.trickle.webclient.TaskApiClient;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.codec.BodyCodec;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class HibbettAPI extends TaskApiClient {
   public boolean ios;
   public static String PX_TOKEN = "3";
   public int cartTries = 0;
   public boolean api;
   public PXToken pxToken = null;
   public boolean isSkip;
   public static int EXCEPTION_RETRY_DELAY = 5000;
   public String dynamicUA;
   public PXTokenAPI s;

   public HttpRequest createCart(String var1) {
      HttpRequest var2 = this.client.postAbs("https://hibbett-mobileapi.prolific.io/ecommerce/cart/create").as(BodyCodec.jsonObject());
      if (!this.ios && !this.pxToken.isTokenCaptcha()) {
         var2.putHeaders(Headers$Pseudo.MPAS.get());
         var2.putHeader("x-px-authorization", ++this.cartTries == 1 ? "1" : (this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue()));
         var2.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
         var2.putHeader("content-type", "application/json");
         var2.putHeader("version", "4.15.0");
         var2.putHeader("platform", "android");
         var2.putHeader("user-agent", this.dynamicUA);
         var2.putHeader("authorization", var1);
         var2.putHeader("content-length", "DEFAULT_VALUE");
         var2.putHeader("accept-encoding", "gzip");
      } else if (!this.ios && this.pxToken.isTokenCaptcha()) {
         this.pxToken.setTokenCaptcha(false);
         var2.putHeaders(Headers$Pseudo.MPAS.get());
         var2.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
         var2.putHeader("content-type", "application/json");
         var2.putHeader("version", "4.15.0");
         var2.putHeader("platform", "android");
         var2.putHeader("user-agent", this.dynamicUA);
         var2.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
         var2.putHeader("authorization", var1);
         var2.putHeader("content-length", "DEFAULT_VALUE");
         var2.putHeader("accept-encoding", "gzip");
      } else {
         var2.putHeaders(Headers$Pseudo.MSPA.get());
         var2.putHeader("content-type", "application/json; charset=utf-8");
         var2.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
         var2.putHeader("accept", "*/*");
         var2.putHeader("version", "4.15.0");
         var2.putHeader("authorization", var1);
         var2.putHeader("accept-encoding", "br;q=1.0, gzip;q=0.9, deflate;q=0.8");
         var2.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
         var2.putHeader("platform", "ios");
         var2.putHeader("accept-language", "en-US;q=1.0, fa-US;q=0.9");
         var2.putHeader("content-length", "DEFAULT_VALUE");
         var2.putHeader("user-agent", this.dynamicUA);
      }

      return var2;
   }

   public HttpRequest checkStock(String var1, String var2, String var3) {
      HttpRequest var4 = this.client.getAbs("https://hibbett-mobileapi.prolific.io/ecommerce/products/" + var3 + "?customerId=" + var2).as(BodyCodec.buffer());
      if (!this.ios && !this.pxToken.isTokenCaptcha()) {
         var4.putHeaders(Headers$Pseudo.MPAS.get());
         var4.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
         var4.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
         var4.putHeader("content-type", "application/json");
         var4.putHeader("version", "4.15.0");
         var4.putHeader("platform", "android");
         var4.putHeader("user-agent", this.dynamicUA);
         var4.putHeader("authorization", var1);
         var4.putHeader("accept-encoding", "gzip");
      } else if (!this.ios && this.pxToken.isTokenCaptcha()) {
         this.pxToken.setTokenCaptcha(false);
         var4.putHeaders(Headers$Pseudo.MPAS.get());
         var4.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
         var4.putHeader("content-type", "application/json");
         var4.putHeader("version", "4.15.0");
         var4.putHeader("platform", "android");
         var4.putHeader("user-agent", this.dynamicUA);
         var4.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
         var4.putHeader("authorization", var1);
         var4.putHeader("accept-encoding", "gzip");
      } else {
         var4.putHeaders(Headers$Pseudo.MSPA.get());
         var4.putHeader("content-type", "application/json; charset=utf-8");
         var4.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
         var4.putHeader("accept", "*/*");
         var4.putHeader("version", "4.15.0");
         var4.putHeader("authorization", var1);
         var4.putHeader("accept-encoding", "br;q=1.0, gzip;q=0.9, deflate;q=0.8");
         var4.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
         var4.putHeader("platform", "ios");
         var4.putHeader("accept-language", "en-US;q=1.0, fa-US;q=0.9");
         var4.putHeader("user-agent", this.dynamicUA);
      }

      return var4;
   }

   public void close() {
      if (this.pxToken != null) {
      }

      super.close();
   }

   public HttpRequest submitCvv(String var1) {
      HttpRequest var2 = this.client.postAbs("https://hostedpayments.radial.com/hosted-payments/encrypt/pancsc?access_token=" + var1).as(BodyCodec.jsonObject());
      var2.putHeader("Content-Type", "application/json; charset=UTF-8");
      var2.putHeader("Content-Length", "DEFAULT_VALUE");
      var2.putHeader("Host", "hostedpayments.radial.com");
      var2.putHeader("Connection", "Keep-Alive");
      var2.putHeader("Accept-Encoding", "gzip");
      var2.putHeader("User-Agent", "okhttp/4.9.0");
      return var2;
   }

   public void setDynamicUA(String var1) {
      this.dynamicUA = var1;
   }

   public void setS(PXTokenAPI var1) {
      this.s = var1;
   }

   public static CompletableFuture async$handleBadResponse(HibbettAPI var0, int var1, String var2, String var3, CompletableFuture var4, int var5, Object var6) {
      CompletableFuture var10000;
      label88: {
         label89: {
            Throwable var11;
            label65: {
               boolean var10001;
               label58:
               switch (var5) {
                  case 0:
                     CompletableFuture var10;
                     if (var1 == 403 && var0.isSkip) {
                        var10000 = var0.pxToken.reInit();
                        if (!var10000.isDone()) {
                           var10 = var10000;
                           return var10.exceptionally(Function.identity()).thenCompose(HibbettAPI::async$handleBadResponse);
                        }
                        break label89;
                     }

                     switch (var1) {
                        case 403:
                           if (var0.api) {
                              var10000 = var0.s.solveCaptcha(var2, var3, var0.pxToken.getSid());
                              if (!var10000.isDone()) {
                                 var10 = var10000;
                                 return var10.exceptionally(Function.identity()).thenCompose(HibbettAPI::async$handleBadResponse);
                              }
                              break label88;
                           }

                           try {
                              var10000 = var0.pxToken.solveCaptchaDesktop(var2, var3, "https://www.hibbett.com/");
                              if (!var10000.isDone()) {
                                 var10 = var10000;
                                 return var10.exceptionally(Function.identity()).thenCompose(HibbettAPI::async$handleBadResponse);
                              }
                              break label58;
                           } catch (Throwable var8) {
                              var11 = var8;
                              var10001 = false;
                              break label65;
                           }
                        default:
                           return CompletableFuture.completedFuture(false);
                     }
                  case 1:
                     var10000 = var4;
                     break label89;
                  case 2:
                     var10000 = var4;
                     break label88;
                  case 3:
                     var10000 = var4;
                     break;
                  default:
                     throw new IllegalArgumentException();
               }

               try {
                  var10000.join();
                  return CompletableFuture.completedFuture(true);
               } catch (Throwable var7) {
                  var11 = var7;
                  var10001 = false;
               }
            }

            Throwable var9 = var11;
            var9.printStackTrace();
            return CompletableFuture.completedFuture(true);
         }

         var10000.join();
         return CompletableFuture.completedFuture(true);
      }

      if ((Boolean)var10000.join()) {
         var0.pxToken.setValue((String)var0.s.getValue());
         var0.pxToken.setTokenCaptcha(true);
         return CompletableFuture.completedFuture(true);
      } else {
         return CompletableFuture.completedFuture(false);
      }
   }

   public HttpRequest shopView(String var1) {
      HttpRequest var2 = this.client.getAbs("https://hibbett-mobileapi.prolific.io/ecommerce/shopview").as(BodyCodec.buffer());
      var2.putHeaders(Headers$Pseudo.MPAS.get());
      var2.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
      var2.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
      var2.putHeader("content-type", "application/json");
      var2.putHeader("version", "4.15.0");
      var2.putHeader("platform", "android");
      var2.putHeader("user-agent", this.dynamicUA);
      var2.putHeader("authorization", var1);
      var2.putHeader("accept-encoding", "gzip");
      return var2;
   }

   public HttpRequest session() {
      HttpRequest var1 = this.client.postAbs("https://hibbett-mobileapi.prolific.io/users/guest").as(BodyCodec.jsonObject());
      var1.putHeader("x-px-authorization", "1");
      if (!this.ios) {
         var1.putHeaders(Headers$Pseudo.MPAS.get());
         var1.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
         var1.putHeader("content-type", "application/json");
         var1.putHeader("version", "4.15.0");
         var1.putHeader("platform", "android");
         var1.putHeader("user-agent", this.dynamicUA);
         var1.putHeader("content-length", "DEFAULT_VALUE");
         var1.putHeader("accept-encoding", "gzip");
      } else {
         var1.putHeaders(Headers$Pseudo.MSPA.get());
         var1.putHeader("accept", "*/*");
         var1.putHeader("version", "4.15.0");
         var1.putHeader("if-none-match", FakeIOSValueGens.genTag());
         var1.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
         var1.putHeader("platform", "ios");
         var1.putHeader("accept-language", "en-US;q=1.0, fa-US;q=0.9");
         var1.putHeader("accept-encoding", "br;q=1.0, gzip;q=0.9, deflate;q=0.8");
         var1.putHeader("user-agent", this.dynamicUA);
         var1.putHeader("content-length", "DEFAULT_VALUE");
      }

      return var1;
   }

   public HttpRequest atc(String var1, String var2, String var3, String var4) {
      HttpRequest var5 = this.client.postAbs("https://hibbett-mobileapi.prolific.io/ecommerce/cart/" + var3 + "/items?skuIds=" + var4 + "&customerId=" + var2).as(BodyCodec.buffer());
      if (!this.ios && !this.pxToken.isTokenCaptcha()) {
         var5.putHeaders(Headers$Pseudo.MPAS.get());
         var5.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
         var5.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
         var5.putHeader("version", "4.15.0");
         var5.putHeader("platform", "android");
         var5.putHeader("user-agent", this.dynamicUA);
         var5.putHeader("authorization", var1);
         var5.putHeader("content-type", "application/json; charset=UTF-8");
         var5.putHeader("content-length", "DEFAULT_VALUE");
         var5.putHeader("accept-encoding", "gzip");
         var5.putHeader("if-none-match", FakeIOSValueGens.genTag());
      } else if (!this.ios && this.pxToken.isTokenCaptcha()) {
         var5.putHeaders(Headers$Pseudo.MPAS.get());
         var5.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
         var5.putHeader("version", "4.15.0");
         var5.putHeader("platform", "android");
         var5.putHeader("user-agent", this.dynamicUA);
         var5.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
         var5.putHeader("authorization", var1);
         var5.putHeader("content-type", "application/json; charset=UTF-8");
         var5.putHeader("content-length", "DEFAULT_VALUE");
         var5.putHeader("accept-encoding", "gzip");
      } else {
         var5.putHeaders(Headers$Pseudo.MSPA.get());
         var5.putHeader("content-type", "application/json; charset=utf-8");
         var5.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
         var5.putHeader("accept", "*/*");
         var5.putHeader("version", "4.15.0");
         var5.putHeader("authorization", var1);
         var5.putHeader("accept-encoding", "br;q=1.0, gzip;q=0.9, deflate;q=0.8");
         var5.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
         var5.putHeader("platform", "ios");
         var5.putHeader("accept-language", "en-US;q=1.0, fa-US;q=0.9");
         var5.putHeader("content-length", "DEFAULT_VALUE");
         var5.putHeader("user-agent", this.dynamicUA);
      }

      return var5;
   }

   public void setPxToken(PXToken var1) {
      this.pxToken = var1;
   }

   public PXTokenBase getPXToken() {
      return this.pxToken;
   }

   public HttpRequest submitCard(String var1) {
      HttpRequest var2 = this.client.postAbs("https://hostedpayments.radial.com/hosted-payments/pan/tokenize?access_token=" + var1).as(BodyCodec.jsonObject());
      var2.putHeader("Content-Type", "application/json; charset=UTF-8");
      var2.putHeader("Content-Length", "DEFAULT_VALUE");
      var2.putHeader("Host", "hostedpayments.radial.com");
      var2.putHeader("Connection", "Keep-Alive");
      var2.putHeader("Accept-Encoding", "gzip");
      var2.putHeader("User-Agent", "okhttp/4.9.0");
      return var2;
   }

   public boolean isSkip() {
      return this.isSkip;
   }

   public void setDevice(JsonObject var1) {
      if (this.ios) {
         this.dynamicUA = FakeIOSValueGens.genUA();
      } else {
         this.dynamicUA = "Hibbett Sports/4.15.0 ";
         String var10001 = this.dynamicUA;
         this.dynamicUA = var10001 + "(" + var1.getString("model") + "; android ";
         int var2 = ThreadLocalRandom.current().nextInt(10);
         if (var2 <= 4) {
            this.dynamicUA = this.dynamicUA + "10";
         } else {
            this.dynamicUA = this.dynamicUA + "11";
         }

         this.dynamicUA = this.dynamicUA + ")";
      }
   }

   public HttpRequest processPayment(String var1, String var2, String var3) {
      String var4;
      if (!this.ios) {
         var4 = "https://hibbett-mobileapi.prolific.io/ecommerce/cart/" + var3 + "/place_order?customerId=" + var2;
      } else {
         var4 = "https://hibbett-mobileapi.prolific.io/ecommerce/cart/" + var3 + "/place_order?optIn=false&customerId=&phone=&firstName=";
      }

      HttpRequest var5 = this.client.postAbs(var4).as(BodyCodec.buffer());
      if (!this.ios && !this.pxToken.isTokenCaptcha()) {
         var5.putHeaders(Headers$Pseudo.MPAS.get());
         var5.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
         var5.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
         var5.putHeader("content-type", "application/json");
         var5.putHeader("version", "4.15.0");
         var5.putHeader("platform", "android");
         var5.putHeader("user-agent", this.dynamicUA);
         var5.putHeader("authorization", var1);
         var5.putHeader("content-length", "DEFAULT_VALUE");
         var5.putHeader("accept-encoding", "gzip");
      } else if (!this.ios && this.pxToken.isTokenCaptcha()) {
         this.pxToken.setTokenCaptcha(false);
         var5.putHeaders(Headers$Pseudo.MPAS.get());
         var5.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
         var5.putHeader("content-type", "application/json");
         var5.putHeader("version", "4.15.0");
         var5.putHeader("platform", "android");
         var5.putHeader("user-agent", this.dynamicUA);
         var5.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
         var5.putHeader("authorization", var1);
         var5.putHeader("content-length", "DEFAULT_VALUE");
         var5.putHeader("accept-encoding", "gzip");
      } else {
         var5.putHeaders(Headers$Pseudo.MSPA.get());
         var5.putHeader("content-type", "application/json; charset=utf-8");
         var5.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
         var5.putHeader("accept", "*/*");
         var5.putHeader("version", "4.15.0");
         var5.putHeader("authorization", var1);
         var5.putHeader("accept-encoding", "br;q=1.0, gzip;q=0.9, deflate;q=0.8");
         var5.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
         var5.putHeader("platform", "ios");
         var5.putHeader("accept-language", "en-US;q=1.0, fa-US;q=0.9");
         var5.putHeader("content-length", "DEFAULT_VALUE");
         var5.putHeader("user-agent", this.dynamicUA);
      }

      return var5;
   }

   public HttpRequest submitEmail(String var1, String var2) {
      HttpRequest var3 = this.client.putAbs("https://hibbett-mobileapi.prolific.io/ecommerce/cart/" + var2 + "/customer").as(BodyCodec.jsonObject());
      if (!this.ios && !this.pxToken.isTokenCaptcha()) {
         var3.putHeaders(Headers$Pseudo.MPAS.get());
         var3.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
         var3.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
         var3.putHeader("version", "4.15.0");
         var3.putHeader("platform", "android");
         var3.putHeader("user-agent", this.dynamicUA);
         var3.putHeader("authorization", var1);
         var3.putHeader("content-type", "application/json; charset=UTF-8");
         var3.putHeader("content-length", "DEFAULT_VALUE");
         var3.putHeader("accept-encoding", "gzip");
      } else if (!this.ios && this.pxToken.isTokenCaptcha()) {
         this.pxToken.setTokenCaptcha(false);
         var3.putHeaders(Headers$Pseudo.MPAS.get());
         var3.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
         var3.putHeader("version", "4.15.0");
         var3.putHeader("platform", "android");
         var3.putHeader("user-agent", this.dynamicUA);
         var3.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
         var3.putHeader("authorization", var1);
         var3.putHeader("content-type", "application/json; charset=UTF-8");
         var3.putHeader("content-length", "DEFAULT_VALUE");
         var3.putHeader("accept-encoding", "gzip");
      } else {
         var3.putHeaders(Headers$Pseudo.MSPA.get());
         var3.putHeader("content-type", "application/json; charset=utf-8");
         var3.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
         var3.putHeader("accept", "*/*");
         var3.putHeader("version", "4.15.0");
         var3.putHeader("authorization", var1);
         var3.putHeader("accept-encoding", "br;q=1.0, gzip;q=0.9, deflate;q=0.8");
         var3.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
         var3.putHeader("platform", "ios");
         var3.putHeader("accept-language", "en-US;q=1.0, fa-US;q=0.9");
         var3.putHeader("content-length", "DEFAULT_VALUE");
         var3.putHeader("user-agent", this.dynamicUA);
      }

      return var3;
   }

   public CompletableFuture handleBadResponse(int var1, String var2, String var3) {
      CompletableFuture var10000;
      CompletableFuture var5;
      if (var1 == 403 && this.isSkip) {
         var10000 = this.pxToken.reInit();
         if (!var10000.isDone()) {
            var5 = var10000;
            return var5.exceptionally(Function.identity()).thenCompose(HibbettAPI::async$handleBadResponse);
         } else {
            var10000.join();
            return CompletableFuture.completedFuture(true);
         }
      } else {
         switch (var1) {
            case 403:
               if (this.api) {
                  var10000 = this.s.solveCaptcha(var2, var3, this.pxToken.getSid());
                  if (!var10000.isDone()) {
                     var5 = var10000;
                     return var5.exceptionally(Function.identity()).thenCompose(HibbettAPI::async$handleBadResponse);
                  } else {
                     if ((Boolean)var10000.join()) {
                        this.pxToken.setValue((String)this.s.getValue());
                        this.pxToken.setTokenCaptcha(true);
                        return CompletableFuture.completedFuture(true);
                     }

                     return CompletableFuture.completedFuture(false);
                  }
               } else {
                  try {
                     var10000 = this.pxToken.solveCaptchaDesktop(var2, var3, "https://www.hibbett.com/");
                     if (!var10000.isDone()) {
                        var5 = var10000;
                        return var5.exceptionally(Function.identity()).thenCompose(HibbettAPI::async$handleBadResponse);
                     }

                     var10000.join();
                  } catch (Throwable var6) {
                     var6.printStackTrace();
                  }

                  return CompletableFuture.completedFuture(true);
               }
            default:
               return CompletableFuture.completedFuture(false);
         }
      }
   }

   public HttpRequest submitShipping(String var1, String var2, String var3) {
      HttpRequest var4 = this.client.putAbs("https://hibbett-mobileapi.prolific.io/ecommerce/cart/" + var3 + "/shipments/me/shipping_address?useAsBilling=true&customerId=" + var2).as(BodyCodec.jsonObject());
      if (!this.ios && !this.pxToken.isTokenCaptcha()) {
         var4.putHeaders(Headers$Pseudo.MPAS.get());
         var4.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
         var4.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
         var4.putHeader("version", "4.15.0");
         var4.putHeader("platform", "android");
         var4.putHeader("user-agent", this.dynamicUA);
         var4.putHeader("authorization", var1);
         var4.putHeader("content-type", "application/json; charset=UTF-8");
         var4.putHeader("content-length", "DEFAULT_VALUE");
         var4.putHeader("accept-encoding", "gzip");
      } else if (!this.ios && this.pxToken.isTokenCaptcha()) {
         this.pxToken.setTokenCaptcha(false);
         var4.putHeaders(Headers$Pseudo.MPAS.get());
         var4.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
         var4.putHeader("version", "4.15.0");
         var4.putHeader("platform", "android");
         var4.putHeader("user-agent", this.dynamicUA);
         var4.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
         var4.putHeader("authorization", var1);
         var4.putHeader("content-type", "application/json; charset=UTF-8");
         var4.putHeader("content-length", "DEFAULT_VALUE");
         var4.putHeader("accept-encoding", "gzip");
      } else {
         var4.putHeaders(Headers$Pseudo.MSPA.get());
         var4.putHeader("content-type", "application/json; charset=utf-8");
         var4.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
         var4.putHeader("accept", "*/*");
         var4.putHeader("version", "4.15.0");
         var4.putHeader("authorization", var1);
         var4.putHeader("accept-encoding", "br;q=1.0, gzip;q=0.9, deflate;q=0.8");
         var4.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
         var4.putHeader("platform", "ios");
         var4.putHeader("accept-language", "en-US;q=1.0, fa-US;q=0.9");
         var4.putHeader("content-length", "DEFAULT_VALUE");
      }

      return var4;
   }

   public void swapClient() {
      try {
         RealClient var1 = RealClientFactory.fromOther(Vertx.currentContext().owner(), super.client, this.client.type());
         super.client.close();
         super.client = var1;
      } catch (Throwable var2) {
      }

   }

   public HibbettAPI(Task var1) {
      super(ClientType.HIBBETT_ANDROID);
      this.ios = var1.getMode().contains("test");
      this.isSkip = var1.getMode().contains("skip");
      this.api = var1.getMode().contains("api");
   }

   public HttpRequest submitPayment(String var1, String var2, String var3) {
      HttpRequest var4 = this.client.postAbs("https://hibbett-mobileapi.prolific.io/ecommerce/cart/" + var3 + "/payment_methods?customerId=" + var2).as(BodyCodec.buffer());
      if (!this.ios && !this.pxToken.isTokenCaptcha()) {
         var4.putHeaders(Headers$Pseudo.MPAS.get());
         var4.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
         var4.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
         var4.putHeader("version", "4.15.0");
         var4.putHeader("platform", "android");
         var4.putHeader("user-agent", this.dynamicUA);
         var4.putHeader("authorization", var1);
         var4.putHeader("content-type", "application/json; charset=UTF-8");
         var4.putHeader("content-length", "DEFAULT_VALUE");
         var4.putHeader("accept-encoding", "gzip");
      } else if (!this.ios && this.pxToken.isTokenCaptcha()) {
         this.pxToken.setTokenCaptcha(false);
         var4.putHeaders(Headers$Pseudo.MPAS.get());
         var4.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
         var4.putHeader("version", "4.15.0");
         var4.putHeader("platform", "android");
         var4.putHeader("user-agent", this.dynamicUA);
         var4.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
         var4.putHeader("authorization", var1);
         var4.putHeader("content-type", "application/json; charset=UTF-8");
         var4.putHeader("content-length", "DEFAULT_VALUE");
         var4.putHeader("accept-encoding", "gzip");
      } else {
         var4.putHeaders(Headers$Pseudo.MSPA.get());
         var4.putHeader("content-type", "application/json; charset=utf-8");
         var4.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
         var4.putHeader("accept", "*/*");
         var4.putHeader("version", "4.15.0");
         var4.putHeader("authorization", var1);
         var4.putHeader("accept-encoding", "br;q=1.0, gzip;q=0.9, deflate;q=0.8");
         var4.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
         var4.putHeader("platform", "ios");
         var4.putHeader("accept-language", "en-US;q=1.0, fa-US;q=0.9");
         var4.putHeader("content-length", "DEFAULT_VALUE");
         var4.putHeader("user-agent", this.dynamicUA);
      }

      return var4;
   }

   public HttpRequest submitShippingRate(String var1, String var2) {
      HttpRequest var3 = this.client.putAbs("https://hibbett-mobileapi.prolific.io/ecommerce/cart/" + var2 + "/shipments/me/shipping_options").as(BodyCodec.jsonObject());
      if (!this.ios && !this.pxToken.isTokenCaptcha()) {
         var3.putHeaders(Headers$Pseudo.MPAS.get());
         var3.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
         var3.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
         var3.putHeader("version", "4.15.0");
         var3.putHeader("platform", "android");
         var3.putHeader("user-agent", this.dynamicUA);
         var3.putHeader("authorization", var1);
         var3.putHeader("content-type", "application/json; charset=UTF-8");
         var3.putHeader("content-length", "DEFAULT_VALUE");
         var3.putHeader("accept-encoding", "gzip");
         var3.putHeader("if-none-match", FakeIOSValueGens.genTag());
      } else if (!this.ios && this.pxToken.isTokenCaptcha()) {
         this.pxToken.setTokenCaptcha(false);
         var3.putHeaders(Headers$Pseudo.MPAS.get());
         var3.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
         var3.putHeader("version", "4.15.0");
         var3.putHeader("platform", "android");
         var3.putHeader("user-agent", this.dynamicUA);
         var3.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
         var3.putHeader("authorization", var1);
         var3.putHeader("content-type", "application/json; charset=UTF-8");
         var3.putHeader("content-length", "DEFAULT_VALUE");
         var3.putHeader("accept-encoding", "gzip");
         var3.putHeader("if-none-match", FakeIOSValueGens.genTag());
      } else {
         var3.putHeaders(Headers$Pseudo.MSPA.get());
         var3.putHeader("content-type", "application/json; charset=utf-8");
         var3.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
         var3.putHeader("accept", "*/*");
         var3.putHeader("version", "4.15.0");
         var3.putHeader("authorization", var1);
         var3.putHeader("accept-encoding", "br;q=1.0, gzip;q=0.9, deflate;q=0.8");
         var3.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
         var3.putHeader("platform", "ios");
         var3.putHeader("accept-language", "en-US;q=1.0, fa-US;q=0.9");
         var3.putHeader("content-length", "DEFAULT_VALUE");
         var3.putHeader("user-agent", this.dynamicUA);
         var3.putHeader("if-none-match", FakeIOSValueGens.genTag());
      }

      return var3;
   }

   public HttpRequest nonce(String var1) {
      HttpRequest var2 = this.client.getAbs("https://hibbett-mobileapi.prolific.io/users/radial/nonce").as(BodyCodec.jsonObject());
      if (!this.ios && !this.pxToken.isTokenCaptcha()) {
         var2.putHeaders(Headers$Pseudo.MPAS.get());
         var2.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
         var2.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
         var2.putHeader("content-type", "application/json");
         var2.putHeader("version", "4.15.0");
         var2.putHeader("platform", "android");
         var2.putHeader("user-agent", this.dynamicUA);
         var2.putHeader("authorization", var1);
         var2.putHeader("accept-encoding", "gzip");
      } else if (!this.ios && this.pxToken.isTokenCaptcha()) {
         this.pxToken.setTokenCaptcha(false);
         var2.putHeaders(Headers$Pseudo.MPAS.get());
         var2.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
         var2.putHeader("content-type", "application/json");
         var2.putHeader("version", "4.15.0");
         var2.putHeader("platform", "android");
         var2.putHeader("user-agent", this.dynamicUA);
         var2.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
         var2.putHeader("authorization", var1);
         var2.putHeader("accept-encoding", "gzip");
      } else {
         var2.putHeaders(Headers$Pseudo.MSPA.get());
         var2.putHeader("content-type", "application/json; charset=utf-8");
         var2.putHeader("x-px-authorization", this.pxToken.getValue() == null ? "3" : (String)this.pxToken.getValue());
         var2.putHeader("accept", "*/*");
         var2.putHeader("version", "4.15.0");
         var2.putHeader("authorization", var1);
         var2.putHeader("accept-encoding", "br;q=1.0, gzip;q=0.9, deflate;q=0.8");
         var2.putHeader("x-api-key", "0PutYAUfHz8ozEeqTFlF014LMJji6Rsc8bpRBGB0");
         var2.putHeader("platform", "ios");
         var2.putHeader("accept-language", "en-US;q=1.0, fa-US;q=0.9");
         var2.putHeader("user-agent", this.dynamicUA);
      }

      return var2;
   }
}
