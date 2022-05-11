package io.trickle.task.sites.walmart.usa;

import io.netty.handler.codec.http.cookie.Cookie;
import io.trickle.account.Account;
import io.trickle.account.AccountController;
import io.trickle.core.Controller;
import io.trickle.core.Engine;
import io.trickle.core.VertxSingleton;
import io.trickle.core.actor.TaskActor;
import io.trickle.task.Task;
import io.trickle.task.antibot.impl.px.PerimeterX;
import io.trickle.task.antibot.impl.px.tools.Deobfuscator;
import io.trickle.task.sites.walmart.Mode;
import io.trickle.task.sites.walmart.usa.handling.EmptyCartException;
import io.trickle.task.sites.walmart.util.PaymentToken;
import io.trickle.task.sites.walmart.util.Utils;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.trickle.webclient.CookieJar;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.impl.ConcurrentHashSet;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.codec.BodyCodec;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class Walmart extends TaskActor {
   public Task task;
   public String itemKeyword;
   public PaymentToken token;
   public long keepAliveWorker = 0L;
   public API api;
   public String productID = "";
   public String instanceSignal;
   public CookieJar sessionCookies = new CookieJar();
   public boolean shared;
   public boolean bannedBefore = false;
   public static ConcurrentHashSet GLOBAL_COOKIES = new ConcurrentHashSet();
   public Mode mode;
   public long sessionTimestamp = 0L;

   public static CompletableFuture async$atcNormal(Walmart param0, Buffer param1, int param2, HttpRequest param3, CompletableFuture param4, HttpResponse param5, Throwable param6, int param7, Object param8) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$updateOrCreateSession(Walmart var0, CompletableFuture var1, int var2, Object var3) {
      Throwable var10;
      label45: {
         boolean var10001;
         label44: {
            CompletableFuture var10000;
            label43: {
               switch (var2) {
                  case 0:
                     try {
                        var0.sessionCookies.clear();
                        CompletableFuture var9;
                        if (!var0.mode.equals(Mode.MOBILE)) {
                           var10000 = Request.executeTillOk(var0.api.getCheckoutPage());
                           if (!var10000.isDone()) {
                              var9 = var10000;
                              return var9.exceptionally(Function.identity()).thenCompose(Walmart::async$updateOrCreateSession);
                           }
                           break label43;
                        }

                        var10000 = SessionPreload.createSession(var0);
                        if (!var10000.isDone()) {
                           var9 = var10000;
                           return var9.exceptionally(Function.identity()).thenCompose(Walmart::async$updateOrCreateSession);
                        }
                        break;
                     } catch (Throwable var7) {
                        var10 = var7;
                        var10001 = false;
                        break label45;
                     }
                  case 1:
                     var10000 = var1;
                     break;
                  case 2:
                     var10000 = var1;
                     break label43;
                  default:
                     throw new IllegalArgumentException();
               }

               try {
                  var10000.join();
                  break label44;
               } catch (Throwable var6) {
                  var10 = var6;
                  var10001 = false;
                  break label45;
               }
            }

            try {
               var10000.join();
            } catch (Throwable var5) {
               var10 = var5;
               var10001 = false;
               break label45;
            }
         }

         try {
            var0.sessionTimestamp = System.currentTimeMillis();
            var0.sessionCookies = new CookieJar(var0.api.getWebClient().cookieStore());
            var0.sessionCookies.get(true, ".walmart.com", "/").forEach(Walmart::lambda$updateOrCreateSession$2);
            var0.fetchGlobalCookies();
            var0.api.getWebClient().cookieStore().putFromOther(var0.sessionCookies);
            return CompletableFuture.completedFuture((Object)null);
         } catch (Throwable var4) {
            var10 = var4;
            var10001 = false;
         }
      }

      Throwable var8 = var10;
      var0.logger.error("Error occurred updating session: {}", var8.getMessage());
      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$tryCart(Walmart param0, int param1, int param2, int param3, CompletableFuture param4, int param5, Throwable param6, int param7, Object param8) {
      // $FF: Couldn't be decompiled
   }

   public void importCookies(API var1) {
      var1.cookieStore().clear();
      JsonArray var2 = new JsonArray(Deobfuscator.readJsFile("browsercookies.json"));

      for(int var3 = 0; var3 < var2.size(); ++var3) {
         JsonObject var4 = var2.getJsonObject(var3);
         var1.cookieStore().put(var4.getString("name"), var4.getString("value"), "www.walmart.com", "/");
      }

   }

   public CompletableFuture fetchHomepage(boolean var1) {
      this.logger.info("Checking homepage");
      int var2 = 0;

      while(super.running && var2++ < 100) {
         CompletableFuture var6;
         CompletableFuture var9;
         try {
            HttpRequest var3 = this.api.homepage();
            var9 = Request.send(var3);
            if (!var9.isDone()) {
               var6 = var9;
               return var6.exceptionally(Function.identity()).thenCompose(Walmart::async$fetchHomepage);
            }

            HttpResponse var4 = (HttpResponse)var9.join();
            if (var4 != null) {
               if (var4.statusCode() == 307) {
                  this.logger.info("Blocked while visiting homepage. Handling...");
                  var9 = this.api.handleBadResponse(var4.statusCode(), var4);
                  if (!var9.isDone()) {
                     var6 = var9;
                     return var6.exceptionally(Function.identity()).thenCompose(Walmart::async$fetchHomepage);
                  }

                  var9.join();
               } else {
                  if (var4.statusCode() == 200) {
                     this.api.cookieStore().put("viq", "walmart", "www.walmart.com");
                     this.api.cookieStore().put("cart-item-count", "0", "www.walmart.com");
                     this.api.cookieStore().put("_uetsid", Utils.genUet(), "www.walmart.com");
                     this.api.cookieStore().put("_uetvid", Utils.genUet(), "www.walmart.com");
                     this.api.cookieStore().put("s_sess_2", "prop32%3D", "www.walmart.com");
                     this.api.cookieStore().put("TBV", "7", "www.walmart.com");
                     this.api.cookieStore().put("TB_DC_Flap_Test", "0", "www.walmart.com");
                     this.api.cookieStore().put("TB_SFOU-100", "", "www.walmart.com");

                     try {
                        this.api.cookieStore().put("athrvi", "RVI~h" + Integer.parseInt(this.task.getKeywords()[0], 16), "www.walmart.com");
                     } catch (Exception var7) {
                     }

                     this.api.cookieStore().put("_gcl_au", "1.1.1957667684." + Instant.now().getEpochSecond(), "www.walmart.com");
                     return CompletableFuture.completedFuture((Object)null);
                  }

                  this.logger.warn("Failed visiting homepage: status:'{}'", var4.statusCode());
                  var9 = VertxUtil.randomSleep((long)this.task.getMonitorDelay());
                  if (!var9.isDone()) {
                     var6 = var9;
                     return var6.exceptionally(Function.identity()).thenCompose(Walmart::async$fetchHomepage);
                  }

                  var9.join();
               }
            }
         } catch (Throwable var8) {
            this.logger.error("Error occurred fetching homepage: {}", var8.getMessage());
            var9 = super.randomSleep(12000);
            if (!var9.isDone()) {
               var6 = var9;
               return var6.exceptionally(Function.identity()).thenCompose(Walmart::async$fetchHomepage);
            }

            var9.join();
         }
      }

      return CompletableFuture.failedFuture(new Exception());
   }

   public CompletableFuture updateOrCreateSession() {
      try {
         this.sessionCookies.clear();
         CompletableFuture var10000;
         CompletableFuture var2;
         if (this.mode.equals(Mode.MOBILE)) {
            var10000 = SessionPreload.createSession(this);
            if (!var10000.isDone()) {
               var2 = var10000;
               return var2.exceptionally(Function.identity()).thenCompose(Walmart::async$updateOrCreateSession);
            }

            var10000.join();
         } else {
            var10000 = Request.executeTillOk(this.api.getCheckoutPage());
            if (!var10000.isDone()) {
               var2 = var10000;
               return var2.exceptionally(Function.identity()).thenCompose(Walmart::async$updateOrCreateSession);
            }

            var10000.join();
         }

         this.sessionTimestamp = System.currentTimeMillis();
         this.sessionCookies = new CookieJar(this.api.getWebClient().cookieStore());
         this.sessionCookies.get(true, ".walmart.com", "/").forEach(Walmart::lambda$updateOrCreateSession$2);
         this.fetchGlobalCookies();
         this.api.getWebClient().cookieStore().putFromOther(this.sessionCookies);
      } catch (Throwable var3) {
         this.logger.error("Error occurred updating session: {}", var3.getMessage());
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture waitForRestock() {
      int var1 = 0;

      String var2;
      try {
         var2 = this.api.getWebClient().cookieStore().getCookieValue("CRT");
      } catch (Exception var5) {
         var2 = "";
      }

      while(super.running && var1 <= 30) {
         ++var1;

         CompletableFuture var10000;
         CompletableFuture var4;
         try {
            if (var1 % 2 == 0 && !this.productID.isEmpty()) {
               var10000 = this.checkStockTerra(this.productID);
               if (!var10000.isDone()) {
                  var4 = var10000;
                  return var4.exceptionally(Function.identity()).thenCompose(Walmart::async$waitForRestock);
               }

               if ((Boolean)var10000.join()) {
                  return CompletableFuture.completedFuture(true);
               }
            } else {
               var10000 = this.checkStockCart(var2);
               if (!var10000.isDone()) {
                  var4 = var10000;
                  return var4.exceptionally(Function.identity()).thenCompose(Walmart::async$waitForRestock);
               }

               if ((Boolean)var10000.join()) {
                  return CompletableFuture.completedFuture(true);
               }
            }

            var10000 = VertxUtil.randomSignalSleep(this.instanceSignal, (long)this.task.getMonitorDelay());
            if (!var10000.isDone()) {
               var4 = var10000;
               return var4.exceptionally(Function.identity()).thenCompose(Walmart::async$waitForRestock);
            }

            var10000.join();
         } catch (Throwable var6) {
            if (var6.getMessage().contains("CRT expired or empty")) {
               return CompletableFuture.failedFuture(var6);
            }

            this.logger.error("Error occurred waiting for restock: {}", var6.getMessage());
            var10000 = super.randomSleep(12000);
            if (!var10000.isDone()) {
               var4 = var10000;
               return var4.exceptionally(Function.identity()).thenCompose(Walmart::async$waitForRestock);
            }

            var10000.join();
         }
      }

      return CompletableFuture.completedFuture(false);
   }

   public void fetchGlobalCookies() {
      GLOBAL_COOKIES.forEach(this::lambda$fetchGlobalCookies$3);
   }

   public void lambda$run$1(Long var1) {
      Request.execute(this.api.getWebClient().getAbs("https://www.walmart.com/favicon.ico"));
   }

   public static CompletableFuture async$tryCheckout(Walmart var0, CompletableFuture var1, int var2, Object var3) {
      Exception var8;
      label36: {
         CompletableFuture var10000;
         boolean var10001;
         switch (var2) {
            case 0:
               try {
                  var10000 = PaymentInstance.checkout(var0, var0.task, var0.token, var0.mode);
                  if (!var10000.isDone()) {
                     CompletableFuture var7 = var10000;
                     return var7.exceptionally(Function.identity()).thenCompose(Walmart::async$tryCheckout);
                  }
                  break;
               } catch (Exception var5) {
                  var8 = var5;
                  var10001 = false;
                  break label36;
               }
            case 1:
               var10000 = var1;
               break;
            default:
               throw new IllegalArgumentException();
         }

         try {
            if ((Integer)var10000.join() == -1) {
               return CompletableFuture.failedFuture(new Exception("Failed to process."));
            }
         } catch (Exception var4) {
            var8 = var4;
            var10001 = false;
            break label36;
         }

         return CompletableFuture.completedFuture((Object)null);
      }

      Exception var6 = var8;
      var0.logger.warn("Failed to checkout: {}", var6.getMessage());
      return CompletableFuture.failedFuture(var6);
   }

   public CompletableFuture tryCart() {
      byte var1 = 0;
      byte var2 = 0;
      byte var3 = 0;

      try {
         Integer.parseInt(this.itemKeyword);
         var2 = 1;
      } catch (Exception var15) {
      }

      CompletableFuture var10000;
      CompletableFuture var7;
      if (this.task.getMode().contains("login")) {
         var10000 = this.createAccount(false);
         if (!var10000.isDone()) {
            var7 = var10000;
            return var7.exceptionally(Function.identity()).thenCompose(Walmart::async$tryCart);
         }

         var10000.join();
      } else if (this.task.getMode().contains("account")) {
         var10000 = this.loginAccount();
         if (!var10000.isDone()) {
            var7 = var10000;
            return var7.exceptionally(Function.identity()).thenCompose(Walmart::async$tryCart);
         }

         int var4 = (Integer)var10000.join();
         if (var4 < 1) {
            this.logger.info("No accounts available in storage. Creating new...");
            var10000 = this.createAccount(true);
            if (!var10000.isDone()) {
               var7 = var10000;
               return var7.exceptionally(Function.identity()).thenCompose(Walmart::async$tryCart);
            }

            var10000.join();
         }
      }

      while(true) {
         label252: {
            while(super.running) {
               if (System.currentTimeMillis() - this.sessionTimestamp >= (long)ThreadLocalRandom.current().nextInt(120000, 300000)) {
                  var10000 = this.updateOrCreateSession();
                  if (!var10000.isDone()) {
                     var7 = var10000;
                     return var7.exceptionally(Function.identity()).thenCompose(Walmart::async$tryCart);
                  }

                  var10000.join();
               }

               if (var1 == 0) {
                  try {
                     var10000 = var2 != 0 ? this.atcAffiliate() : this.atcNormal();
                     if (!var10000.isDone()) {
                        var7 = var10000;
                        return var7.exceptionally(Function.identity()).thenCompose(Walmart::async$tryCart);
                     }

                     var10000.join();
                  } catch (Exception var16) {
                     continue;
                  }
               } else if (var3 != 0) {
                  try {
                     var10000 = this.waitForRestock();
                     if (!var10000.isDone()) {
                        var7 = var10000;
                        return var7.exceptionally(Function.identity()).thenCompose(Walmart::async$tryCart);
                     }

                     if (!(Boolean)var10000.join()) {
                        continue;
                     }
                  } catch (Throwable var17) {
                     var1 = 0;
                     var3 = 0;
                     this.fetchGlobalCookies();
                     continue;
                  }
               }

               try {
                  try {
                     var10000 = this.tryCheckout();
                     if (!var10000.isDone()) {
                        var7 = var10000;
                        return var7.exceptionally(Function.identity()).thenCompose(Walmart::async$tryCart);
                     }

                     var10000.join();
                  } catch (Exception var18) {
                     if (!var18.getMessage().contains("exceeds limit") && !var18.getMessage().contains("PCID") && !var18.getMessage().contains("405")) {
                        var1 = 1;
                        if (this.api.getWebClient().cookieStore().contains("CRT")) {
                           try {
                              String var5 = this.api.getCookies().getCookieValue("CRT");
                              this.api.getCookies().put("CRT", var5, ".walmart.com");
                           } catch (Exception var14) {
                              var14.printStackTrace();
                           }

                           var3 = 1;
                        }
                        break label252;
                     }

                     var1 = 0;
                     var3 = 0;
                     this.fetchGlobalCookies();
                     break label252;
                  }
               } catch (Throwable var19) {
                  this.logger.warn("Retrying in {}ms", this.task.getRetryDelay());
                  var10000 = super.sleep(this.task.getRetryDelay());
                  if (!var10000.isDone()) {
                     var7 = var10000;
                     return var7.exceptionally(Function.identity()).thenCompose(Walmart::async$tryCart);
                  }

                  var10000.join();
                  throw var19;
               }

               this.logger.warn("Retrying in {}ms", this.task.getRetryDelay());
               var10000 = super.sleep(this.task.getRetryDelay());
               if (!var10000.isDone()) {
                  var7 = var10000;
                  return var7.exceptionally(Function.identity()).thenCompose(Walmart::async$tryCart);
               }

               var10000.join();
               break;
            }

            return CompletableFuture.completedFuture((Object)null);
         }

         this.logger.warn("Retrying in {}ms", this.task.getRetryDelay());
         var10000 = super.sleep(this.task.getRetryDelay());
         if (!var10000.isDone()) {
            var7 = var10000;
            return var7.exceptionally(Function.identity()).thenCompose(Walmart::async$tryCart);
         }

         var10000.join();
      }
   }

   public CompletableFuture atcAffiliate() {
      String var1 = ThreadLocalRandom.current().nextBoolean() ? "addToCart" : "buynow";
      this.logger.info("Attempting add to cart");
      int var2 = 0;

      while(super.running && var2 <= 50) {
         CompletableFuture var10000;
         CompletableFuture var6;
         try {
            ++var2;
            HttpRequest var3 = this.api.affilCrossSite(var1);
            var10000 = Request.send(var3.as(BodyCodec.string()));
            if (!var10000.isDone()) {
               var6 = var10000;
               return var6.exceptionally(Function.identity()).thenCompose(Walmart::async$atcAffiliate);
            }

            HttpResponse var4 = (HttpResponse)var10000.join();
            if (var4 != null) {
               if (var4.statusCode() == 302) {
                  String var5 = this.api.getWebClient().cookieStore().getCookieValue("CRT");
                  VertxUtil.sendSignal(this.instanceSignal, var5);
                  this.logger.info("Successfully added to cart: status:'{}'", var4.statusCode());
                  this.api.getWebClient().cookieStore().putFromOther(this.sessionCookies);
                  return CompletableFuture.completedFuture((Object)null);
               }

               this.logger.info("Waiting for restock [PID]: '{}'", var4.statusCode());
               var10000 = this.api.handleBadResponse(var4.statusCode(), var4);
               if (!var10000.isDone()) {
                  var6 = var10000;
                  return var6.exceptionally(Function.identity()).thenCompose(Walmart::async$atcAffiliate);
               }

               var10000.join();
               var10000 = this.handleCRTSignalSleep(this.api);
               if (!var10000.isDone()) {
                  var6 = var10000;
                  return var6.exceptionally(Function.identity()).thenCompose(Walmart::async$atcAffiliate);
               }

               if ((Boolean)var10000.join()) {
                  return CompletableFuture.completedFuture((Object)null);
               }
            }
         } catch (Throwable var7) {
            this.logger.error("Error occurred waiting for restock (s): {}", var7.getMessage());
            var10000 = super.randomSleep(12000);
            if (!var10000.isDone()) {
               var6 = var10000;
               return var6.exceptionally(Function.identity()).thenCompose(Walmart::async$atcAffiliate);
            }

            var10000.join();
         }
      }

      return CompletableFuture.failedFuture(new Exception());
   }

   public CompletableFuture checkStockCart(String var1) {
      int var2 = 0;

      while(true) {
         label106: {
            if (var2 < 5) {
               HttpRequest var3 = this.api.getCartV3(var1);

               CompletableFuture var10000;
               CompletableFuture var9;
               try {
                  var10000 = Request.send(var3);
                  if (!var10000.isDone()) {
                     var9 = var10000;
                     return var9.exceptionally(Function.identity()).thenCompose(Walmart::async$checkStockCart);
                  }

                  HttpResponse var4 = (HttpResponse)var10000.join();
                  if (var4 == null) {
                     break label106;
                  }

                  if (var4.statusCode() != 201 && var4.statusCode() != 200 && var4.statusCode() != 206) {
                     if (var4.statusCode() == 405) {
                        return CompletableFuture.failedFuture(new EmptyCartException());
                     }

                     this.logger.warn("Waiting for restock (s): status:'{}'", var4.statusCode());
                     var10000 = this.api.handleBadResponse(var4.statusCode(), var4);
                     if (!var10000.isDone()) {
                        var9 = var10000;
                        return var9.exceptionally(Function.identity()).thenCompose(Walmart::async$checkStockCart);
                     }

                     var10000.join();
                  } else {
                     JsonObject var5 = var4.bodyAsJsonObject();
                     if (var5.getBoolean("checkoutable", false)) {
                        VertxUtil.sendSignal(this.instanceSignal, this.api.getWebClient().cookieStore().getCookieValue("CRT"));
                        return CompletableFuture.completedFuture(true);
                     }

                     if (var5.containsKey("items")) {
                        JsonArray var6 = var5.getJsonArray("items", (JsonArray)null);
                        if (var6 == null || var6.size() <= 0) {
                           this.api.getWebClient().cookieStore().removeAnyMatch("hasCRT");
                           this.api.getWebClient().cookieStore().removeAnyMatch("CRT");
                           return CompletableFuture.failedFuture(new EmptyCartException());
                        }

                        for(int var7 = 0; var7 < var6.size(); ++var7) {
                           try {
                              JsonObject var8 = var6.getJsonObject(var7);
                              if (var8.getString("offerId", "").equalsIgnoreCase(this.itemKeyword)) {
                                 this.productID = var8.getString("USItemId", "");
                                 break;
                              }
                           } catch (Throwable var10) {
                           }
                        }
                     }

                     this.logger.info("Waiting for restock (s)");
                  }
               } catch (Throwable var11) {
                  this.logger.error("Error occurred waiting for restock (s): {}", var11.getMessage());
                  var10000 = super.randomSleep(12000);
                  if (!var10000.isDone()) {
                     var9 = var10000;
                     return var9.exceptionally(Function.identity()).thenCompose(Walmart::async$checkStockCart);
                  }

                  var10000.join();
                  break label106;
               }
            }

            return CompletableFuture.completedFuture(false);
         }

         ++var2;
      }
   }

   public CompletableFuture loginAccount() {
      int var1 = 0;
      AccountController var2 = (AccountController)Engine.get().getModule(Controller.ACCOUNT);

      while(super.running && var1 <= 500) {
         ++var1;

         CompletableFuture var10000;
         CompletableFuture var8;
         try {
            var10000 = var2.findAccount(this.task.getProfile().getEmail(), false).toCompletionStage().toCompletableFuture();
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(Walmart::async$loginAccount);
            }

            Account var3 = (Account)var10000.join();
            if (var3 == null) {
               this.logger.warn("No accounts available...");
               return CompletableFuture.completedFuture(0);
            }

            this.logger.info("Logging in to account '{}'", var3.getUser());
            JsonObject var4 = this.api.accountLoginForm(var3);
            HttpRequest var5 = this.api.loginAccount();
            var10000 = Request.send(var5, var4);
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(Walmart::async$loginAccount);
            }

            HttpResponse var6 = (HttpResponse)var10000.join();
            if (var6 != null) {
               if (var6.statusCode() == 201 || var6.statusCode() == 200 || var6.statusCode() == 206) {
                  if (this.logger.isDebugEnabled()) {
                     this.logger.debug("Account login responded with: [{}]{}", var6.statusCode(), var6.bodyAsString());
                  }

                  this.logger.info("Logged in successfully to account '{}'", var3.getUser());
                  this.api.setLoggedIn(true);
                  this.task.getProfile().setAccountEmail(var3.getUser());
                  return CompletableFuture.completedFuture(1);
               }

               this.logger.warn("Account login: status:'{}'", var6.statusCode());
               if (var6.statusCode() != 412) {
                  this.bannedBefore = false;
               } else if (var1 % 10 == 0 && this.mode == Mode.DESKTOP && this.task.getMode().contains("skip")) {
                  this.api.getPxAPI().reset();
               }

               var10000 = this.api.handleBadResponse(var6.statusCode(), var6);
               if (!var10000.isDone()) {
                  var8 = var10000;
                  return var8.exceptionally(Function.identity()).thenCompose(Walmart::async$loginAccount);
               }

               byte var7 = (Boolean)var10000.join();
               if (var7 == 0 || this.bannedBefore) {
                  var10000 = VertxUtil.sleep((long)this.task.getRetryDelay());
                  if (!var10000.isDone()) {
                     var8 = var10000;
                     return var8.exceptionally(Function.identity()).thenCompose(Walmart::async$loginAccount);
                  }

                  var10000.join();
               }

               if (var6.statusCode() == 412) {
                  this.bannedBefore = true;
               }
            }
         } catch (Throwable var9) {
            this.logger.error("Error occurred logging in account: {}", var9.getMessage());
            var10000 = super.randomSleep(12000);
            if (!var10000.isDone()) {
               var8 = var10000;
               return var8.exceptionally(Function.identity()).thenCompose(Walmart::async$loginAccount);
            }

            var10000.join();
         }
      }

      this.logger.warn("Failed to login to account. Max retries exceeded...");
      return CompletableFuture.completedFuture(-1);
   }

   public static CompletableFuture async$atcAffiliate(Walmart param0, String param1, int param2, HttpRequest param3, CompletableFuture param4, HttpResponse param5, Throwable param6, int param7, Object param8) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture run() {
      if (this.mode.equals(Mode.DESKTOP)) {
         this.api.setAPI(PerimeterX.createDesktopAPI(this));
      } else {
         this.api.setAPI(PerimeterX.createMobile(this));
      }

      CompletableFuture var10000 = this.generateToken();
      CompletableFuture var3;
      if (!var10000.isDone()) {
         var3 = var10000;
         return var3.exceptionally(Function.identity()).thenCompose(Walmart::async$run);
      } else {
         var10000.join();
         var10000 = this.api.initialisePX();
         if (!var10000.isDone()) {
            var3 = var10000;
            return var3.exceptionally(Function.identity()).thenCompose(Walmart::async$run);
         } else {
            byte var1 = (Boolean)var10000.join();
            if (var1 == 0) {
               this.logger.warn("Failed to initialise and configure task. Stopping...");
               return CompletableFuture.completedFuture((Object)null);
            } else {
               try {
                  if (this.mode == Mode.DESKTOP) {
                     var10000 = this.fetchHomepage(true);
                     if (!var10000.isDone()) {
                        var3 = var10000;
                        return var3.exceptionally(Function.identity()).thenCompose(Walmart::async$run);
                     }

                     var10000.join();
                     var10000 = this.api.generatePX(false);
                     if (!var10000.isDone()) {
                        var3 = var10000;
                        return var3.exceptionally(Function.identity()).thenCompose(Walmart::async$run);
                     }

                     var10000.join();
                  }
               } catch (Throwable var4) {
               }

               var10000 = this.updateOrCreateSession();
               if (!var10000.isDone()) {
                  var3 = var10000;
                  return var3.exceptionally(Function.identity()).thenCompose(Walmart::async$run);
               } else {
                  var10000.join();
                  if (this.task.getMode().toLowerCase().contains("fast")) {
                     var10000 = PaymentInstance.preload(this, this.task, this.token, this.mode);
                     if (!var10000.isDone()) {
                        var3 = var10000;
                        return var3.exceptionally(Function.identity()).thenCompose(Walmart::async$run);
                     }

                     String var2 = (String)var10000.join();
                     this.token.setPiHash(var2);
                     this.api.getWebClient().cookieStore().clear();
                     if (this.task.getMode().toLowerCase().contains("grief")) {
                        this.api.swapClient();
                     }
                  }

                  if (this.keepAliveWorker == 0L) {
                     this.keepAliveWorker = super.vertx.setPeriodic(TimeUnit.SECONDS.toMillis(100L), this::lambda$run$1);
                  }

                  var10000 = this.sleep(ThreadLocalRandom.current().nextInt(300, 1500));
                  if (!var10000.isDone()) {
                     var3 = var10000;
                     return var3.exceptionally(Function.identity()).thenCompose(Walmart::async$run);
                  } else {
                     var10000.join();
                     var10000 = this.tryCart();
                     if (!var10000.isDone()) {
                        var3 = var10000;
                        return var3.exceptionally(Function.identity()).thenCompose(Walmart::async$run);
                     } else {
                        var10000.join();
                        if (this.keepAliveWorker != 0L) {
                           super.vertx.cancelTimer(this.keepAliveWorker);
                        }

                        return CompletableFuture.completedFuture((Object)null);
                     }
                  }
               }
            }
         }
      }
   }

   public static CompletableFuture async$checkStockTerra(Walmart param0, String param1, Buffer param2, int param3, HttpRequest param4, CompletableFuture param5, HttpResponse param6, Throwable param7, int param8, Object param9) {
      // $FF: Couldn't be decompiled
   }

   public Walmart(Task var1, int var2) {
      super(var2);
      this.task = var1;
      this.mode = Mode.getMode(this.task.getMode());
      if (this.mode.equals(Mode.DESKTOP)) {
         this.api = new WalmartAPIDesktop(this.task);
      } else {
         this.api = new WalmartAPI(this.task);
      }

      super.setClient(this.api);
      this.api.getWebClient().cookieStore().setCookieFilter(Walmart::lambda$new$0);
      this.itemKeyword = this.task.getKeywords()[0];
      this.instanceSignal = this.itemKeyword;
      this.shared = this.task.getMode().toLowerCase().contains("sync");
   }

   public static CompletableFuture async$cartFast(Walmart param0, int param1, int param2, String param3, HttpRequest param4, CompletableFuture param5, Buffer param6, HttpResponse param7, int param8, Throwable param9, int param10, Object param11) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$handleCRTSignalSleep(Walmart var0, API var1, CompletableFuture var2, int var3, Object var4) {
      CompletableFuture var10000;
      switch (var3) {
         case 0:
            var10000 = VertxUtil.randomSignalSleep(var0.instanceSignal, (long)var0.task.getMonitorDelay());
            if (!var10000.isDone()) {
               CompletableFuture var6 = var10000;
               return var6.exceptionally(Function.identity()).thenCompose(Walmart::async$handleCRTSignalSleep);
            }
            break;
         case 1:
            var10000 = var2;
            break;
         default:
            throw new IllegalArgumentException();
      }

      Object var5 = var10000.join();
      if (var0.shared && var5 != null) {
         var1.cookieStore().put("CRT", (String)var5, ".walmart.com");
         return CompletableFuture.completedFuture(true);
      } else {
         return CompletableFuture.completedFuture(false);
      }
   }

   public static CompletableFuture async$run(Walmart var0, CompletableFuture var1, int var2, int var3, Object var4) {
      CompletableFuture var10000;
      label141: {
         int var8;
         CompletableFuture var10;
         label129: {
            label142: {
               label143: {
                  label132: {
                     label115: {
                        boolean var10001;
                        label144: {
                           label131: {
                              label110: {
                                 switch (var3) {
                                    case 0:
                                       if (var0.mode.equals(Mode.DESKTOP)) {
                                          var0.api.setAPI(PerimeterX.createDesktopAPI(var0));
                                       } else {
                                          var0.api.setAPI(PerimeterX.createMobile(var0));
                                       }

                                       var10000 = var0.generateToken();
                                       if (!var10000.isDone()) {
                                          var10 = var10000;
                                          return var10.exceptionally(Function.identity()).thenCompose(Walmart::async$run);
                                       }
                                       break;
                                    case 1:
                                       var10000 = var1;
                                       break;
                                    case 2:
                                       var10000 = var1;
                                       break label110;
                                    case 3:
                                       var10000 = var1;
                                       var8 = var2;
                                       break label131;
                                    case 4:
                                       var10000 = var1;
                                       var8 = var2;
                                       break label144;
                                    case 5:
                                       var10000 = var1;
                                       var8 = var2;
                                       break label132;
                                    case 6:
                                       var10000 = var1;
                                       var8 = var2;
                                       break label143;
                                    case 7:
                                       var10000 = var1;
                                       var8 = var2;
                                       break label129;
                                    case 8:
                                       var10000 = var1;
                                       break label141;
                                    default:
                                       throw new IllegalArgumentException();
                                 }

                                 var10000.join();
                                 var10000 = var0.api.initialisePX();
                                 if (!var10000.isDone()) {
                                    var10 = var10000;
                                    return var10.exceptionally(Function.identity()).thenCompose(Walmart::async$run);
                                 }
                              }

                              var8 = (Boolean)var10000.join();
                              if (var8 == 0) {
                                 var0.logger.warn("Failed to initialise and configure task. Stopping...");
                                 return CompletableFuture.completedFuture((Object)null);
                              }

                              try {
                                 if (var0.mode != Mode.DESKTOP) {
                                    break label115;
                                 }

                                 var10000 = var0.fetchHomepage(true);
                                 if (!var10000.isDone()) {
                                    var10 = var10000;
                                    return var10.exceptionally(Function.identity()).thenCompose(Walmart::async$run);
                                 }
                              } catch (Throwable var7) {
                                 var10001 = false;
                                 break label115;
                              }
                           }

                           try {
                              var10000.join();
                              var10000 = var0.api.generatePX(false);
                              if (!var10000.isDone()) {
                                 var10 = var10000;
                                 return var10.exceptionally(Function.identity()).thenCompose(Walmart::async$run);
                              }
                           } catch (Throwable var6) {
                              var10001 = false;
                              break label115;
                           }
                        }

                        try {
                           var10000.join();
                        } catch (Throwable var5) {
                           var10001 = false;
                        }
                     }

                     var10000 = var0.updateOrCreateSession();
                     if (!var10000.isDone()) {
                        var10 = var10000;
                        return var10.exceptionally(Function.identity()).thenCompose(Walmart::async$run);
                     }
                  }

                  var10000.join();
                  if (!var0.task.getMode().toLowerCase().contains("fast")) {
                     break label142;
                  }

                  var10000 = PaymentInstance.preload(var0, var0.task, var0.token, var0.mode);
                  if (!var10000.isDone()) {
                     var10 = var10000;
                     return var10.exceptionally(Function.identity()).thenCompose(Walmart::async$run);
                  }
               }

               String var9 = (String)var10000.join();
               var0.token.setPiHash(var9);
               var0.api.getWebClient().cookieStore().clear();
               if (var0.task.getMode().toLowerCase().contains("grief")) {
                  var0.api.swapClient();
               }
            }

            if (var0.keepAliveWorker == 0L) {
               var0.keepAliveWorker = var0.vertx.setPeriodic(TimeUnit.SECONDS.toMillis(100L), var0::lambda$run$1);
            }

            var10000 = var0.sleep(ThreadLocalRandom.current().nextInt(300, 1500));
            if (!var10000.isDone()) {
               var10 = var10000;
               return var10.exceptionally(Function.identity()).thenCompose(Walmart::async$run);
            }
         }

         var10000.join();
         var10000 = var0.tryCart();
         if (!var10000.isDone()) {
            var10 = var10000;
            return var10.exceptionally(Function.identity()).thenCompose(Walmart::async$run);
         }
      }

      var10000.join();
      if (var0.keepAliveWorker != 0L) {
         var0.vertx.cancelTimer(var0.keepAliveWorker);
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$fetchHomepage(Walmart param0, int param1, int param2, HttpRequest param3, CompletableFuture param4, HttpResponse param5, Throwable param6, int param7, Object param8) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$loginAccount(Walmart param0, int param1, AccountController param2, CompletableFuture param3, Account param4, JsonObject param5, HttpRequest param6, HttpResponse param7, int param8, Throwable param9, int param10, Object param11) {
      // $FF: Couldn't be decompiled
   }

   public void lambda$fetchGlobalCookies$3(Cookie var1) {
      if (!this.sessionCookies.contains(var1.name())) {
         this.sessionCookies.put(var1);
      }

   }

   public static CompletableFuture async$generateToken(Walmart var0, HttpRequest var1, CompletableFuture var2, int var3, Object var4) {
      Exception var10;
      label30: {
         CompletableFuture var10000;
         boolean var10001;
         switch (var3) {
            case 0:
               var1 = VertxSingleton.INSTANCE.getLocalClient().getClient().getAbs("https://securedataweb.walmart.com/pie/v1/wmcom_us_vtg_pie/getkey.js?bust=" + System.currentTimeMillis()).timeout(TimeUnit.SECONDS.toMillis(15L)).as(BodyCodec.string());

               try {
                  var10000 = Request.execute(var1, 5);
                  if (!var10000.isDone()) {
                     CompletableFuture var9 = var10000;
                     return var9.exceptionally(Function.identity()).thenCompose(Walmart::async$generateToken);
                  }
                  break;
               } catch (Exception var6) {
                  var10 = var6;
                  var10001 = false;
                  break label30;
               }
            case 1:
               var10000 = var2;
               break;
            default:
               throw new IllegalArgumentException();
         }

         try {
            String var8 = (String)var10000.join();
            var0.token = PaymentToken.prepareAndGenerate(var8, var0.task.getProfile().getCardNumber(), var0.task.getProfile().getCvv());
            var0.token.set4111Encrypted(PaymentToken.prepareAndGenerate(var8, "4111111111111111", var0.task.getProfile().getCvv()).getEncryptedPan());
            return CompletableFuture.completedFuture((Object)null);
         } catch (Exception var5) {
            var10 = var5;
            var10001 = false;
         }
      }

      Exception var7 = var10;
      var0.logger.warn("Failed to generate payment token: {}", var7.getMessage());
      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$checkStockCart(Walmart param0, String param1, int param2, HttpRequest param3, CompletableFuture param4, HttpResponse param5, Throwable param6, int param7, Object param8) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture tryCheckout() {
      try {
         CompletableFuture var10000 = PaymentInstance.checkout(this, this.task, this.token, this.mode);
         if (!var10000.isDone()) {
            CompletableFuture var2 = var10000;
            return var2.exceptionally(Function.identity()).thenCompose(Walmart::async$tryCheckout);
         }

         if ((Integer)var10000.join() == -1) {
            return CompletableFuture.failedFuture(new Exception("Failed to process."));
         }
      } catch (Exception var3) {
         this.logger.warn("Failed to checkout: {}", var3.getMessage());
         return CompletableFuture.failedFuture(var3);
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture atcNormal() {
      this.logger.info("Attempting add to cart");
      Buffer var1 = this.api.atcForm().toBuffer();
      int var2 = 0;

      while(super.running && var2 <= 500) {
         ++var2;

         CompletableFuture var10000;
         CompletableFuture var6;
         try {
            HttpRequest var3 = this.api.addToCart().as(BodyCodec.deferred());
            var10000 = Request.send(var3, var1);
            if (!var10000.isDone()) {
               var6 = var10000;
               return var6.exceptionally(Function.identity()).thenCompose(Walmart::async$atcNormal);
            }

            HttpResponse var4 = (HttpResponse)var10000.join();
            if (var4 != null) {
               if (var4.statusCode() == 201 || var4.statusCode() == 200 || var4.statusCode() == 206) {
                  VertxUtil.sendSignal(this.instanceSignal, this.api.cookieStore().getCookieValue("CRT"));
                  this.logger.info("Successfully added to cart: status:'{}'", var4.statusCode());
                  return CompletableFuture.completedFuture((Object)null);
               }

               this.logger.warn("Waiting for restock: status:'{}'", var4.statusCode());
               this.fetchGlobalCookies();
               if (var4.statusCode() != 412) {
                  this.bannedBefore = false;
                  var10000 = this.handleCRTSignalSleep(this.api);
                  if (!var10000.isDone()) {
                     var6 = var10000;
                     return var6.exceptionally(Function.identity()).thenCompose(Walmart::async$atcNormal);
                  }

                  if ((Boolean)var10000.join()) {
                     return CompletableFuture.completedFuture((Object)null);
                  }
               } else if (var2 % 10 == 0 && this.mode == Mode.DESKTOP && this.task.getMode().contains("skip")) {
                  this.api.getPxAPI().reset();
               }

               var10000 = var4.deferredBody().toCompletionStage().toCompletableFuture();
               if (!var10000.isDone()) {
                  var6 = var10000;
                  return var6.exceptionally(Function.identity()).thenCompose(Walmart::async$atcNormal);
               }

               var10000.join();
               var10000 = this.api.handleBadResponse(var4.statusCode(), var4);
               if (!var10000.isDone()) {
                  var6 = var10000;
                  return var6.exceptionally(Function.identity()).thenCompose(Walmart::async$atcNormal);
               }

               boolean var5 = (Boolean)var10000.join();
            }
         } catch (Throwable var7) {
            this.logger.error("Error occurred adding to cart: {}", var7.getMessage());
            var10000 = super.randomSleep(12000);
            if (!var10000.isDone()) {
               var6 = var10000;
               return var6.exceptionally(Function.identity()).thenCompose(Walmart::async$atcNormal);
            }

            var10000.join();
         }
      }

      return CompletableFuture.failedFuture(new Exception());
   }

   public CompletableFuture generateToken() {
      HttpRequest var1 = VertxSingleton.INSTANCE.getLocalClient().getClient().getAbs("https://securedataweb.walmart.com/pie/v1/wmcom_us_vtg_pie/getkey.js?bust=" + System.currentTimeMillis()).timeout(TimeUnit.SECONDS.toMillis(15L)).as(BodyCodec.string());

      try {
         CompletableFuture var10000 = Request.execute(var1, 5);
         if (!var10000.isDone()) {
            CompletableFuture var3 = var10000;
            return var3.exceptionally(Function.identity()).thenCompose(Walmart::async$generateToken);
         }

         String var2 = (String)var10000.join();
         this.token = PaymentToken.prepareAndGenerate(var2, this.task.getProfile().getCardNumber(), this.task.getProfile().getCvv());
         this.token.set4111Encrypted(PaymentToken.prepareAndGenerate(var2, "4111111111111111", this.task.getProfile().getCvv()).getEncryptedPan());
      } catch (Exception var4) {
         this.logger.warn("Failed to generate payment token: {}", var4.getMessage());
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$createAccount(Walmart param0, int param1, int param2, JsonObject param3, HttpRequest param4, CompletableFuture param5, HttpResponse param6, int param7, Throwable param8, int param9, Object param10) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture checkStockTerra(String var1) {
      String var10002 = this.api.getTask().getProfile().getZip();
      Buffer var2 = (new JsonObject("{\"variables\":\"{\\\"casperSlots\\\":{\\\"fulfillmentType\\\":\\\"ACC\\\",\\\"reservationType\\\":\\\"SLOTS\\\"},\\\"postalAddress\\\":{\\\"addressType\\\":\\\"RESIDENTIAL\\\",\\\"countryCode\\\":\\\"USA\\\",\\\"postalCode\\\":\\\"" + var10002 + "\\\",\\\"stateOrProvinceCode\\\":\\\"" + this.api.getTask().getProfile().getState() + "\\\",\\\"zipLocated\\\":true},\\\"storeFrontIds\\\":[{\\\"distance\\\":2.24,\\\"inStore\\\":false,\\\"preferred\\\":false,\\\"storeId\\\":\\\"91672\\\",\\\"storeUUID\\\":null,\\\"usStoreId\\\":91672},{\\\"distance\\\":3.04,\\\"inStore\\\":false,\\\"preferred\\\":false,\\\"storeId\\\":\\\"5936\\\",\\\"storeUUID\\\":null,\\\"usStoreId\\\":5936},{\\\"distance\\\":3.31,\\\"inStore\\\":false,\\\"preferred\\\":false,\\\"storeId\\\":\\\"90563\\\",\\\"storeUUID\\\":null,\\\"usStoreId\\\":90563},{\\\"distance\\\":3.41,\\\"inStore\\\":false,\\\"preferred\\\":false,\\\"storeId\\\":\\\"91675\\\",\\\"storeUUID\\\":null,\\\"usStoreId\\\":91675},{\\\"distance\\\":5.58,\\\"inStore\\\":false,\\\"preferred\\\":false,\\\"storeId\\\":\\\"91121\\\",\\\"storeUUID\\\":null,\\\"usStoreId\\\":91121}],\\\"productId\\\":\\\"" + var1 + "\\\",\\\"selected\\\":false}\"}")).toBuffer();

      for(int var3 = 0; var3 < 5; ++var3) {
         HttpRequest var4 = this.api.terraFirma(var1, false);

         CompletableFuture var12;
         CompletableFuture var10000;
         try {
            var10000 = Request.send(var4, var2);
            if (!var10000.isDone()) {
               var12 = var10000;
               return var12.exceptionally(Function.identity()).thenCompose(Walmart::async$checkStockTerra);
            }

            HttpResponse var5 = (HttpResponse)var10000.join();
            if (var5 != null) {
               if (var5.statusCode() != 201 && var5.statusCode() != 200 && var5.statusCode() != 206) {
                  this.logger.warn("Waiting for restock (t): status:'{}'", var5.statusCode());
                  var10000 = this.api.handleBadResponse(var5.statusCode(), var5);
                  if (!var10000.isDone()) {
                     var12 = var10000;
                     return var12.exceptionally(Function.identity()).thenCompose(Walmart::async$checkStockTerra);
                  }

                  var10000.join();
               } else {
                  JsonObject var6 = var5.bodyAsJsonObject();
                  if (var6.containsKey("data")) {
                     JsonObject var7 = var6.getJsonObject("data").getJsonObject("productByProductId");
                     if (var7.containsKey("offerList")) {
                        JsonArray var8 = var7.getJsonArray("offerList");
                        if (var8 != null) {
                           for(int var9 = 0; var9 < var8.size(); ++var9) {
                              try {
                                 JsonObject var10 = var8.getJsonObject(var9);
                                 if (var10.getString("id", "").equalsIgnoreCase(this.itemKeyword)) {
                                    if (var10.containsKey("productAvailability") && var10.getJsonObject("productAvailability").getString("availabilityStatus").equalsIgnoreCase("IN_STOCK")) {
                                       if (var10.containsKey("offerInfo")) {
                                          JsonObject var11 = var10.getJsonObject("offerInfo");
                                          if (var11.containsKey("offerType")) {
                                             if (var11.getString("offerType", "").contains("ONLINE")) {
                                                VertxUtil.sendSignal(this.instanceSignal, this.api.getWebClient().cookieStore().getCookieValue("CRT"));
                                                return CompletableFuture.completedFuture(true);
                                             }

                                             this.logger.info("Waiting for restock (t)");
                                             return CompletableFuture.completedFuture(false);
                                          }
                                       }

                                       VertxUtil.sendSignal(this.instanceSignal, this.api.getWebClient().cookieStore().getCookieValue("CRT"));
                                       return CompletableFuture.completedFuture(true);
                                    }

                                    this.logger.info("Waiting for restock (t)");
                                    return CompletableFuture.completedFuture(false);
                                 }
                              } catch (Throwable var13) {
                              }
                           }
                        }
                     }
                  }
               }
            }
         } catch (Throwable var14) {
            this.logger.error("Error occurred waiting for restock (t): {}", var14.getMessage());
            var10000 = super.randomSleep(12000);
            if (!var10000.isDone()) {
               var12 = var10000;
               return var12.exceptionally(Function.identity()).thenCompose(Walmart::async$checkStockTerra);
            }

            var10000.join();
         }
      }

      return CompletableFuture.completedFuture(false);
   }

   public CompletableFuture handleCRTSignalSleep(API var1) {
      CompletableFuture var10000 = VertxUtil.randomSignalSleep(this.instanceSignal, (long)this.task.getMonitorDelay());
      if (!var10000.isDone()) {
         CompletableFuture var3 = var10000;
         return var3.exceptionally(Function.identity()).thenCompose(Walmart::async$handleCRTSignalSleep);
      } else {
         Object var2 = var10000.join();
         if (this.shared && var2 != null) {
            var1.cookieStore().put("CRT", (String)var2, ".walmart.com");
            return CompletableFuture.completedFuture(true);
         } else {
            return CompletableFuture.completedFuture(false);
         }
      }
   }

   public static CompletableFuture async$waitForRestock(Walmart param0, int param1, String param2, CompletableFuture param3, Throwable param4, int param5, Object param6) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture createAccount(boolean var1) {
      this.logger.info("Creating account");
      int var2 = 0;

      while(super.running && var2 <= 500) {
         ++var2;

         CompletableFuture var10000;
         CompletableFuture var7;
         try {
            JsonObject var3 = this.api.accountCreateForm();
            HttpRequest var4 = this.api.createAccount();
            var10000 = Request.send(var4, var3);
            if (!var10000.isDone()) {
               var7 = var10000;
               return var7.exceptionally(Function.identity()).thenCompose(Walmart::async$createAccount);
            }

            HttpResponse var5 = (HttpResponse)var10000.join();
            if (var5 != null) {
               if (var5.statusCode() == 201 || var5.statusCode() == 200 || var5.statusCode() == 206) {
                  if (var1 != 0) {
                     EventBus var9 = super.vertx.eventBus();
                     String var10002 = var3.getString("email");
                     var9.send("accounts.writer", var10002 + ":" + var3.getString("password"));
                     if (this.logger.isDebugEnabled()) {
                        this.logger.debug("Account create responded with: [{}]{}", var5.statusCode(), var5.bodyAsString());
                     }
                  }

                  this.api.setLoggedIn(true);
                  this.task.getProfile().setAccountEmail(var3.getString("email", (String)null));
                  this.logger.info("Created account successfully!");
                  return CompletableFuture.completedFuture((Object)null);
               }

               this.logger.warn("Creating account: status:'{}'", var5.statusCode());
               if (var5.statusCode() != 412) {
                  this.bannedBefore = false;
               } else if (var2 % 10 == 0 && this.mode == Mode.DESKTOP && this.task.getMode().contains("skip")) {
                  this.api.getPxAPI().reset();
               }

               var10000 = this.api.handleBadResponse(var5.statusCode(), var5);
               if (!var10000.isDone()) {
                  var7 = var10000;
                  return var7.exceptionally(Function.identity()).thenCompose(Walmart::async$createAccount);
               }

               byte var6 = (Boolean)var10000.join();
               if (var6 == 0 || this.bannedBefore) {
                  var10000 = VertxUtil.sleep((long)this.task.getRetryDelay());
                  if (!var10000.isDone()) {
                     var7 = var10000;
                     return var7.exceptionally(Function.identity()).thenCompose(Walmart::async$createAccount);
                  }

                  var10000.join();
               }

               if (var5.statusCode() == 412) {
                  this.bannedBefore = true;
               }
            }
         } catch (Throwable var8) {
            this.logger.error("Error occurred creating account: {}", var8.getMessage());
            var10000 = super.randomSleep(12000);
            if (!var10000.isDone()) {
               var7 = var10000;
               return var7.exceptionally(Function.identity()).thenCompose(Walmart::async$createAccount);
            }

            var10000.join();
         }
      }

      return CompletableFuture.failedFuture(new Exception());
   }

   public static void lambda$updateOrCreateSession$2(Cookie var0) {
      if (var0.value().equalsIgnoreCase("1") && !var0.name().equals("g")) {
         GLOBAL_COOKIES.add(var0);
      }

   }

   public static boolean lambda$new$0(String var0) {
      return !var0.equalsIgnoreCase("g");
   }

   public CompletableFuture cartFast() {
      byte var1 = 0;
      int var2 = 0;
      String var3 = null;

      while(super.running && var2++ <= 30) {
         CompletableFuture var7;
         CompletableFuture var12;
         try {
            HttpResponse var4;
            HttpRequest var5;
            if (!this.api.cookieStore().contains("CRT")) {
               this.logger.warn("Starting session");
               var5 = this.api.getCart();
               var12 = Request.send(var5);
               if (!var12.isDone()) {
                  var7 = var12;
                  return var7.exceptionally(Function.identity()).thenCompose(Walmart::async$cartFast);
               }

               var4 = (HttpResponse)var12.join();
            } else if (var3 == null) {
               this.logger.warn("Creating session");
               var1 = 1;
               var5 = this.api.savedCart();
               Buffer var6 = (new JsonObject()).put("offerId", this.task.getKeywords()[0]).put("quantity", Integer.parseInt(this.task.getSize())).toBuffer();
               var12 = Request.send(var5, var6);
               if (!var12.isDone()) {
                  var7 = var12;
                  return var7.exceptionally(Function.identity()).thenCompose(Walmart::async$cartFast);
               }

               var4 = (HttpResponse)var12.join();
            } else {
               this.logger.warn("Validating session");
               var1 = 2;
               var5 = this.api.transferCart(var3);
               var12 = Request.send(var5, new JsonObject());
               if (!var12.isDone()) {
                  var7 = var12;
                  return var7.exceptionally(Function.identity()).thenCompose(Walmart::async$cartFast);
               }

               var4 = (HttpResponse)var12.join();
            }

            if (var4 != null) {
               if (this.logger.isDebugEnabled()) {
                  this.logger.debug("Fast cart step '{}' responded[{}]: {}", Integer.valueOf(var1), var4.statusCode(), var4.bodyAsString());
               }

               if (var4.statusCode() == 200) {
                  JsonObject var10;
                  if (var1 == 1) {
                     var10 = var4.bodyAsJsonObject().getJsonArray("savedItems").getJsonObject(0);
                     var3 = var10.getString("id", (String)null);
                  } else if (var1 == 2) {
                     this.logger.info("Session successfully initialised!");
                     var10 = var4.bodyAsJsonObject();
                     JsonObject var11 = var10.getJsonObject("cart");
                     if (var11.containsKey("id") && var11.getInteger("itemCount", 0) >= 1) {
                        if (var10.getBoolean("checkoutable", false)) {
                           return CompletableFuture.completedFuture(3);
                        }

                        return CompletableFuture.completedFuture(2);
                     }

                     return CompletableFuture.completedFuture(1);
                  }
               } else {
                  if (var4.statusCode() != 412) {
                     this.bannedBefore = false;
                     var12 = this.handleCRTSignalSleep(this.api);
                     if (!var12.isDone()) {
                        var7 = var12;
                        return var7.exceptionally(Function.identity()).thenCompose(Walmart::async$cartFast);
                     }

                     if ((Boolean)var12.join()) {
                        return CompletableFuture.completedFuture((Object)null);
                     }
                  } else if (var2 % 10 == 0 && this.mode == Mode.DESKTOP && this.task.getMode().contains("skip")) {
                     this.api.getPxAPI().reset();
                  }

                  var12 = this.api.handleBadResponse(var4.statusCode(), var4);
                  if (!var12.isDone()) {
                     var7 = var12;
                     return var7.exceptionally(Function.identity()).thenCompose(Walmart::async$cartFast);
                  }

                  byte var9 = (Boolean)var12.join();
                  if (var9 == 0 || this.bannedBefore) {
                     var12 = this.handleCRTSignalSleep(this.api);
                     if (!var12.isDone()) {
                        var7 = var12;
                        return var7.exceptionally(Function.identity()).thenCompose(Walmart::async$cartFast);
                     }

                     if ((Boolean)var12.join()) {
                        return CompletableFuture.completedFuture((Object)null);
                     }
                  }

                  if (var4.statusCode() == 412) {
                     this.bannedBefore = true;
                  }
               }
            }
         } catch (Throwable var8) {
            this.logger.error("Error occurred creating session: {}", var8.getMessage());
            var12 = super.randomSleep(12000);
            if (!var12.isDone()) {
               var7 = var12;
               return var7.exceptionally(Function.identity()).thenCompose(Walmart::async$cartFast);
            }

            var12.join();
         }
      }

      return CompletableFuture.completedFuture(-1);
   }
}
