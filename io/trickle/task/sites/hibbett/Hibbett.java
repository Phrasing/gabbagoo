package io.trickle.task.sites.hibbett;

import io.trickle.core.VertxSingleton;
import io.trickle.core.actor.TaskActor;
import io.trickle.profile.Profile;
import io.trickle.task.Task;
import io.trickle.task.antibot.impl.px.PXToken;
import io.trickle.task.antibot.impl.px.PXTokenAPI;
import io.trickle.task.antibot.impl.px.PXTokenBase;
import io.trickle.task.sites.Site;
import io.trickle.util.RunClock;
import io.trickle.util.analytics.Analytics;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Hibbett extends TaskActor {
   public List tokens;
   public int previousResponseLen = 0;
   public int previousResponseHash = 0;
   public String instanceSignal;
   public boolean griefMode;
   public RunClock clock;
   public boolean shardMode;
   public Task task;
   public boolean scheduledMode;
   public String itemKeyword;
   public HibbettAPI api;
   public static Pattern ITEM_ID_PATTERN = Pattern.compile("([0-z]*?)\\.HTML");

   public static CompletableFuture async$processPayment(Hibbett param0, String param1, String param2, String param3, int param4, HttpRequest param5, CompletableFuture param6, HttpResponse param7, JsonObject param8, int param9, Throwable param10, int param11, Object param12) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture getSizes(String var1, String var2) {
      int var3 = 0;
      this.logger.info("Waiting for restock");

      while(super.running && var3++ < 99999999 && this.shouldRunOnSchedule()) {
         CompletableFuture var8;
         CompletableFuture var11;
         try {
            HttpRequest var4 = this.api.checkStock(var1, var2, this.itemKeyword);
            var11 = Request.send(var4);
            if (!var11.isDone()) {
               var8 = var11;
               return var8.exceptionally(Function.identity()).thenCompose(Hibbett::async$getSizes);
            }

            HttpResponse var5 = (HttpResponse)var11.join();
            if (var5 != null) {
               JsonObject var6 = var5.bodyAsJsonObject();
               if (var5.statusCode() == 200) {
                  if (var6.containsKey("skus")) {
                     JsonArray var7 = var6.getJsonArray("skus");
                     if (var7.size() != 0) {
                        return CompletableFuture.completedFuture(var7);
                     }
                  } else {
                     this.logger.info("No sizes available (p)");
                     var11 = VertxUtil.sleep((long)this.task.getMonitorDelay());
                     if (!var11.isDone()) {
                        var8 = var11;
                        return var8.exceptionally(Function.identity()).thenCompose(Hibbett::async$getSizes);
                     }

                     var11.join();
                  }
               } else {
                  this.logger.warn("Waiting for restock (p): status:'{}'", var5.statusCode());
                  var11 = this.api.handleBadResponse(var5.statusCode(), var6.containsKey("vid") ? var6.getString("vid") : this.api.getPXToken().getVid(), var6.containsKey("uuid") ? var6.getString("uuid") : null);
                  if (!var11.isDone()) {
                     var8 = var11;
                     return var8.exceptionally(Function.identity()).thenCompose(Hibbett::async$getSizes);
                  }

                  byte var10 = (Boolean)var11.join();
                  if (var10 == 0 && this.api.isSkip()) {
                     var11 = VertxUtil.randomSleep(5000L);
                     if (!var11.isDone()) {
                        var8 = var11;
                        return var8.exceptionally(Function.identity()).thenCompose(Hibbett::async$getSizes);
                     }

                     var11.join();
                  }
               }
            }
         } catch (Throwable var9) {
            this.logger.error("Error occurred waiting for restock (p): {}", var9.getMessage());
            if (!this.shouldRunOnSchedule()) {
               return CompletableFuture.completedFuture((Object)null);
            }

            var11 = super.randomSleep(5000);
            if (!var11.isDone()) {
               var8 = var11;
               return var8.exceptionally(Function.identity()).thenCompose(Hibbett::async$getSizes);
            }

            var11.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$run(Hibbett var0, CompletableFuture var1, int var2, Object var3) {
      CompletableFuture var10000;
      label77: {
         boolean var6;
         label62: {
            Throwable var10;
            label61: {
               boolean var10001;
               label68: {
                  CompletableFuture var9;
                  switch (var2) {
                     case 0:
                        if (!var0.scheduledMode) {
                           var10000 = var0.runNormal();
                           if (!var10000.isDone()) {
                              var9 = var10000;
                              return var9.exceptionally(Function.identity()).thenCompose(Hibbett::async$run);
                           }
                           break label77;
                        }

                        var0.clock = RunClock.create();
                        var0.logger.info("Scheduled run planned in {}minute(s)", TimeUnit.MINUTES.convert(var0.clock.getTimeTillRun(), TimeUnit.MILLISECONDS));
                        var10000 = VertxUtil.hardCodedSleep(var0.clock.getTimeTillRun());
                        if (!var10000.isDone()) {
                           var9 = var10000;
                           return var9.exceptionally(Function.identity()).thenCompose(Hibbett::async$run);
                        }
                        break;
                     case 1:
                        var10000 = var1;
                        break;
                     case 2:
                        var10000 = var1;
                        break label68;
                     case 3:
                        var10000 = var1;
                        break label77;
                     default:
                        throw new IllegalArgumentException();
                  }

                  var10000.join();
                  var0.clock.start();
                  var0.logger.info("Running on schedule...");

                  try {
                     var10000 = var0.runNormal();
                     if (!var10000.isDone()) {
                        var9 = var10000;
                        return var9.exceptionally(Function.identity()).thenCompose(Hibbett::async$run);
                     }
                  } catch (Throwable var5) {
                     var10 = var5;
                     var10001 = false;
                     break label61;
                  }
               }

               try {
                  var6 = (Boolean)var10000.join();
                  break label62;
               } catch (Throwable var4) {
                  var10 = var4;
                  var10001 = false;
               }
            }

            Throwable var7 = var10;
            var7.printStackTrace();
            var6 = false;
         }

         if (!var6) {
            PXTokenBase var8 = var0.api.getPXToken();
            if (var8 != null && var8.deploymentID() != null && !var8.deploymentID().isEmpty()) {
               var0.vertx.undeploy(var8.deploymentID());
            }

            var0.api.setPxToken((PXToken)null);
            return var0.run();
         }

         return CompletableFuture.completedFuture((Object)null);
      }

      var10000.join();
      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$getSizes(Hibbett param0, String param1, String param2, int param3, HttpRequest param4, CompletableFuture param5, HttpResponse param6, JsonObject param7, int param8, Throwable param9, int param10, Object param11) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$submitPayment(Hibbett param0, String param1, String param2, String param3, Integer param4, String param5, String param6, JsonObject param7, HttpRequest param8, CompletableFuture param9, HttpResponse param10, JsonObject param11, int param12, Throwable param13, int param14, Object param15) {
      // $FF: Couldn't be decompiled
   }

   public void handleFailureWebhooks(String var1, Buffer var2) {
      if (this.previousResponseHash == 0 || this.previousResponseLen != var2.length() || this.previousResponseHash != var2.hashCode()) {
         try {
            Analytics.failure(var1, this.task, var2.toJsonObject(), this.api.proxyStringSafe());
            this.previousResponseHash = var2.hashCode();
            this.previousResponseLen = var2.length();
         } catch (Throwable var4) {
         }
      }

   }

   public CompletableFuture processPayment(String var1, String var2, String var3) {
      int var4 = 0;
      this.logger.info("Processing...");

      while(super.running && var4++ < 99999 && this.shouldRunOnSchedule()) {
         CompletableFuture var9;
         CompletableFuture var11;
         try {
            HttpRequest var5 = this.api.processPayment(var1, var2, var3);
            var11 = Request.send(var5, Buffer.buffer(""));
            if (!var11.isDone()) {
               var9 = var11;
               return var9.exceptionally(Function.identity()).thenCompose(Hibbett::async$processPayment);
            }

            HttpResponse var6 = (HttpResponse)var11.join();
            if (var6 != null) {
               JsonObject var7 = var6.bodyAsJsonObject();
               this.logger.info("Payment resp: {}", var7.toString().contains("uuid") ? "CAPTCHA" : var7);
               if (var7.toString().contains("orderDate")) {
                  this.logger.info("Successfully checked out!");
                  this.logger.info(var7.toString());
                  Analytics.success(this.task, var6.bodyAsJsonObject(), this.api.proxyStringSafe());
                  return CompletableFuture.completedFuture(true);
               }

               if (var7.toString().contains("has been declined")) {
                  this.logger.info("Card declined. Retrying...");
                  this.handleFailureWebhooks("Card decline", (Buffer)var6.body());
                  return CompletableFuture.completedFuture(false);
               }

               if (!var7.toString().contains("captcha") && !var7.toString().contains("block")) {
                  this.logger.warn("Processing payment failed: status:'{}'", ((Buffer)var6.body()).toString());
                  this.handleFailureWebhooks("Misc failure", (Buffer)var6.body());
               } else {
                  this.logger.warn("Processing payment failed: status: CAPTCHA");
               }

               var11 = this.api.handleBadResponse(var6.statusCode(), var7.containsKey("vid") ? var7.getString("vid") : this.api.getPXToken().getVid(), var7.containsKey("uuid") ? var7.getString("uuid") : null);
               if (!var11.isDone()) {
                  var9 = var11;
                  return var9.exceptionally(Function.identity()).thenCompose(Hibbett::async$processPayment);
               }

               byte var8 = (Boolean)var11.join();
               if (var8 == 0 && this.api.isSkip()) {
                  var11 = VertxUtil.sleep((long)this.task.getRetryDelay());
                  if (!var11.isDone()) {
                     var9 = var11;
                     return var9.exceptionally(Function.identity()).thenCompose(Hibbett::async$processPayment);
                  }

                  var11.join();
               }
            }
         } catch (Throwable var10) {
            this.logger.error("Error occurred processing payment: {}", var10.getMessage());
            if (!this.shouldRunOnSchedule()) {
               return CompletableFuture.completedFuture(false);
            }

            var11 = super.randomSleep(5000);
            if (!var11.isDone()) {
               var9 = var11;
               return var9.exceptionally(Function.identity()).thenCompose(Hibbett::async$processPayment);
            }

            var11.join();
         }
      }

      return CompletableFuture.completedFuture(false);
   }

   public CompletableFuture runNormal() {
      if (this.task.getMode().contains("api")) {
         PXTokenAPI var1 = new PXTokenAPI(this);
         this.api.setS(var1);
         super.vertx.deployVerticle(var1);
      }

      this.api.setPxToken(new PXToken(this, Site.HIBBETT));
      super.vertx.deployVerticle(this.api.getPXToken());
      CompletableFuture var10000 = this.api.getPXToken().awaitInit();
      CompletableFuture var6;
      if (!var10000.isDone()) {
         var6 = var10000;
         return var6.exceptionally(Function.identity()).thenCompose(Hibbett::async$runNormal);
      } else {
         if ((Boolean)var10000.join()) {
            HibbettAPI var12 = this.api;
            CompletableFuture var10001 = VertxSingleton.INSTANCE.getLocalClient().fetchDeviceFromAPI("https://loudounchris.xyz/api/device/mobileDeviceWithOS.json");
            if (!var10001.isDone()) {
               CompletableFuture var7 = var10001;
               HibbettAPI var11 = var12;
               return var7.exceptionally(Function.identity()).thenCompose(Hibbett::async$runNormal);
            }

            var12.setDevice((JsonObject)var10001.join());
            this.logger.info("Getting pages");
            var10000 = SessionPreload.createSession(this);
            if (!var10000.isDone()) {
               var6 = var10000;
               return var6.exceptionally(Function.identity()).thenCompose(Hibbett::async$runNormal);
            }

            Map var9 = (Map)var10000.join();
            if (this.griefMode) {
               this.api.swapClient();
            }

            var10000 = this.getSizes((String)var9.get("authorization"), (String)var9.get("customerId"));
            if (!var10000.isDone()) {
               var6 = var10000;
               return var6.exceptionally(Function.identity()).thenCompose(Hibbett::async$runNormal);
            }

            JsonArray var2;
            for(var2 = (JsonArray)var10000.join(); !var2.toString().contains("\"isAvailable\":true"); var2 = (JsonArray)var10000.join()) {
               var10000 = VertxUtil.sleep((long)this.task.getMonitorDelay());
               if (!var10000.isDone()) {
                  var6 = var10000;
                  return var6.exceptionally(Function.identity()).thenCompose(Hibbett::async$runNormal);
               }

               var10000.join();
               var10000 = this.getSizes((String)var9.get("authorization"), (String)var9.get("customerId"));
               if (!var10000.isDone()) {
                  var6 = var10000;
                  return var6.exceptionally(Function.identity()).thenCompose(Hibbett::async$runNormal);
               }
            }

            JsonArray var3 = this.sortSizes(var2);

            try {
               var10000 = this.atc(var3, (String)var9.get("authorization"), (String)var9.get("customerId"), (String)var9.get("cartId"));
               if (!var10000.isDone()) {
                  var6 = var10000;
                  return var6.exceptionally(Function.identity()).thenCompose(Hibbett::async$runNormal);
               }

               int var4 = (Integer)var10000.join();
               if (this.griefMode) {
                  this.submitPayment((String)var9.get("authorization"), (String)var9.get("customerId"), (String)var9.get("cartId"), var4, (String)var9.get("encryptedCVNValue"), (String)var9.get("ccToken"));
                  CompletableFuture var5 = this.api.getWebClient().windowUpdateCallback();
                  var10000 = VertxUtil.handleEagerFuture(var5);
                  if (!var10000.isDone()) {
                     var6 = var10000;
                     return var6.exceptionally(Function.identity()).thenCompose(Hibbett::async$runNormal);
                  }

                  var10000.join();
               } else {
                  var10000 = this.submitPayment((String)var9.get("authorization"), (String)var9.get("customerId"), (String)var9.get("cartId"), var4, (String)var9.get("encryptedCVNValue"), (String)var9.get("ccToken"));
                  if (!var10000.isDone()) {
                     var6 = var10000;
                     return var6.exceptionally(Function.identity()).thenCompose(Hibbett::async$runNormal);
                  }

                  var10000.join();
               }

               var10000 = this.processPayment((String)var9.get("authorization"), (String)var9.get("customerId"), (String)var9.get("cartId"));
               if (!var10000.isDone()) {
                  var6 = var10000;
                  return var6.exceptionally(Function.identity()).thenCompose(Hibbett::async$runNormal);
               }

               boolean var10 = (Boolean)var10000.join();
               return CompletableFuture.completedFuture(var10);
            } catch (Throwable var8) {
               var8.printStackTrace();
            }
         } else {
            this.logger.warn("Failed to initialise and configure task. Stopping...");
         }

         return CompletableFuture.completedFuture(false);
      }
   }

   public Hibbett(Task var1, int var2) {
      super(var2);
      this.task = var1;
      this.api = new HibbettAPI(this.task);
      super.setClient(this.api);
      Matcher var4 = ITEM_ID_PATTERN.matcher(this.task.getKeywords()[0]);
      String var3;
      if (var4.find()) {
         var3 = var4.group(1);
      } else {
         this.logger.error("Error parsing keyword: {}", this.task.getKeywords()[0]);
         this.logger.warn("Defaulting to '{}' as keyword", this.task.getKeywords()[0]);
         var3 = this.task.getKeywords()[0];
      }

      this.instanceSignal = var3;
      this.itemKeyword = this.instanceSignal;
      this.griefMode = false;
      this.scheduledMode = this.task.getMode().contains("schedule");
      this.shardMode = false;
   }

   public CompletableFuture run() {
      CompletableFuture var10000;
      CompletableFuture var3;
      if (this.scheduledMode) {
         this.clock = RunClock.create();
         this.logger.info("Scheduled run planned in {}minute(s)", TimeUnit.MINUTES.convert(this.clock.getTimeTillRun(), TimeUnit.MILLISECONDS));
         var10000 = VertxUtil.hardCodedSleep(this.clock.getTimeTillRun());
         if (!var10000.isDone()) {
            var3 = var10000;
            return var3.exceptionally(Function.identity()).thenCompose(Hibbett::async$run);
         }

         var10000.join();
         this.clock.start();
         this.logger.info("Running on schedule...");

         boolean var1;
         try {
            var10000 = this.runNormal();
            if (!var10000.isDone()) {
               var3 = var10000;
               return var3.exceptionally(Function.identity()).thenCompose(Hibbett::async$run);
            }

            var1 = (Boolean)var10000.join();
         } catch (Throwable var4) {
            var4.printStackTrace();
            var1 = false;
         }

         if (!var1) {
            PXTokenBase var2 = this.api.getPXToken();
            if (var2 != null && var2.deploymentID() != null && !var2.deploymentID().isEmpty()) {
               super.vertx.undeploy(var2.deploymentID());
            }

            this.api.setPxToken((PXToken)null);
            return this.run();
         }
      } else {
         var10000 = this.runNormal();
         if (!var10000.isDone()) {
            var3 = var10000;
            return var3.exceptionally(Function.identity()).thenCompose(Hibbett::async$run);
         }

         var10000.join();
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public Profile getProfile() {
      return this.task.getProfile();
   }

   public JsonObject atcPayload(JsonArray var1, String var2, int var3) {
      String var4;
      if (this.task.getSize().equals("random")) {
         var4 = var1.getJsonObject(var3 % var1.size()).getString("id");
      } else {
         var4 = this.selectSpecificSize(var1);
      }

      JsonObject var5 = new JsonObject();
      var5.put("customerId", var2);
      var5.put("personalizations", new JsonArray());
      var5.put("product", (new JsonObject()).put("id", this.itemKeyword).put("isRaffle", false));
      var5.put("quantity", 1);
      var5.put("sku", (new JsonObject()).put("id", var4));
      JsonObject var6 = new JsonObject();
      var6.put("cartItems", (new JsonArray()).add(var5));
      return var6;
   }

   public static CompletableFuture async$atc(Hibbett param0, JsonArray param1, String param2, String param3, String param4, int param5, JsonObject param6, String param7, HttpRequest param8, CompletableFuture param9, HttpResponse param10, JsonObject param11, int param12, Throwable param13, int param14, Object param15) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$runNormal(Hibbett var0, CompletableFuture var1, HibbettAPI var2, Map var3, JsonArray var4, JsonArray var5, int var6, CompletableFuture var7, int var8, Object var9) {
      Throwable var27;
      label204: {
         CompletableFuture var10000;
         boolean var28;
         label179: {
            Map var17;
            JsonArray var18;
            JsonArray var19;
            int var21;
            CompletableFuture var25;
            label178: {
               label205: {
                  label206: {
                     CompletableFuture var22;
                     label189: {
                        label190: {
                           Map var10001;
                           JsonArray var10002;
                           switch (var8) {
                              case 0:
                                 if (var0.task.getMode().contains("api")) {
                                    PXTokenAPI var20 = new PXTokenAPI(var0);
                                    var0.api.setS(var20);
                                    var0.vertx.deployVerticle(var20);
                                 }

                                 var0.api.setPxToken(new PXToken(var0, Site.HIBBETT));
                                 var0.vertx.deployVerticle(var0.api.getPXToken());
                                 var10000 = var0.api.getPXToken().awaitInit();
                                 if (!var10000.isDone()) {
                                    var25 = var10000;
                                    return var25.exceptionally(Function.identity()).thenCompose(Hibbett::async$runNormal);
                                 }
                                 break;
                              case 1:
                                 var10000 = var1;
                                 break;
                              case 2:
                                 var2.setDevice((JsonObject)var1.join());
                                 var0.logger.info("Getting pages");
                                 var10000 = SessionPreload.createSession(var0);
                                 if (!var10000.isDone()) {
                                    var25 = var10000;
                                    return var25.exceptionally(Function.identity()).thenCompose(Hibbett::async$runNormal);
                                 }
                                 break label190;
                              case 3:
                                 var10000 = var1;
                                 break label190;
                              case 4:
                                 var10000 = var1;
                                 var17 = var3;
                                 var18 = (JsonArray)var10000.join();
                                 break label189;
                              case 5:
                                 var10000 = var1;
                                 var17 = var3;
                                 var10000.join();
                                 var10000 = var0.getSizes((String)var3.get("authorization"), (String)var3.get("customerId"));
                                 if (!var10000.isDone()) {
                                    var25 = var10000;
                                    return var25.exceptionally(Function.identity()).thenCompose(Hibbett::async$runNormal);
                                 }
                                 break label189;
                              case 6:
                                 var10000 = var1;
                                 var17 = var3;
                                 var18 = (JsonArray)var10000.join();
                                 break label189;
                              case 7:
                                 var10000 = var1;
                                 var10001 = var3;
                                 var19 = var5;
                                 var18 = var4;
                                 var17 = var10001;

                                 try {
                                    var21 = (Integer)var10000.join();
                                    if (!var0.griefMode) {
                                       var10000 = var0.submitPayment((String)var17.get("authorization"), (String)var17.get("customerId"), (String)var17.get("cartId"), var21, (String)var17.get("encryptedCVNValue"), (String)var17.get("ccToken"));
                                       if (!var10000.isDone()) {
                                          var25 = var10000;
                                          return var25.exceptionally(Function.identity()).thenCompose(Hibbett::async$runNormal);
                                       }
                                       break label205;
                                    }

                                    var0.submitPayment((String)var17.get("authorization"), (String)var17.get("customerId"), (String)var17.get("cartId"), var21, (String)var17.get("encryptedCVNValue"), (String)var17.get("ccToken"));
                                    var22 = var0.api.getWebClient().windowUpdateCallback();
                                    var10000 = VertxUtil.handleEagerFuture(var22);
                                    if (!var10000.isDone()) {
                                       var25 = var10000;
                                       return var25.exceptionally(Function.identity()).thenCompose(Hibbett::async$runNormal);
                                    }
                                    break label206;
                                 } catch (Throwable var16) {
                                    var27 = var16;
                                    var28 = false;
                                    break label204;
                                 }
                              case 8:
                                 var10000 = var7;
                                 var10001 = var3;
                                 var10002 = var4;
                                 var21 = var6;
                                 var19 = var5;
                                 var18 = var10002;
                                 var17 = var10001;
                                 break label206;
                              case 9:
                                 var10000 = var1;
                                 var10001 = var3;
                                 var10002 = var4;
                                 var21 = var6;
                                 var19 = var5;
                                 var18 = var10002;
                                 var17 = var10001;
                                 break label205;
                              case 10:
                                 var10000 = var1;
                                 break label179;
                              default:
                                 throw new IllegalArgumentException();
                           }

                           if (!(Boolean)var10000.join()) {
                              var0.logger.warn("Failed to initialise and configure task. Stopping...");
                              return CompletableFuture.completedFuture(false);
                           }

                           HibbettAPI var30 = var0.api;
                           CompletableFuture var29 = VertxSingleton.INSTANCE.getLocalClient().fetchDeviceFromAPI("https://loudounchris.xyz/api/device/mobileDeviceWithOS.json");
                           if (!var29.isDone()) {
                              var7 = var29;
                              HibbettAPI var26 = var30;
                              return var7.exceptionally(Function.identity()).thenCompose(Hibbett::async$runNormal);
                           }

                           var30.setDevice((JsonObject)var29.join());
                           var0.logger.info("Getting pages");
                           var10000 = SessionPreload.createSession(var0);
                           if (!var10000.isDone()) {
                              var25 = var10000;
                              return var25.exceptionally(Function.identity()).thenCompose(Hibbett::async$runNormal);
                           }
                        }

                        var17 = (Map)var10000.join();
                        if (var0.griefMode) {
                           var0.api.swapClient();
                        }

                        var10000 = var0.getSizes((String)var17.get("authorization"), (String)var17.get("customerId"));
                        if (!var10000.isDone()) {
                           var25 = var10000;
                           return var25.exceptionally(Function.identity()).thenCompose(Hibbett::async$runNormal);
                        }

                        var18 = (JsonArray)var10000.join();
                     }

                     for(var18 = (JsonArray)var10000.join(); !var18.toString().contains("\"isAvailable\":true"); var18 = (JsonArray)var10000.join()) {
                        var10000 = VertxUtil.sleep((long)var0.task.getMonitorDelay());
                        if (!var10000.isDone()) {
                           var25 = var10000;
                           return var25.exceptionally(Function.identity()).thenCompose(Hibbett::async$runNormal);
                        }

                        var10000.join();
                        var10000 = var0.getSizes((String)var17.get("authorization"), (String)var17.get("customerId"));
                        if (!var10000.isDone()) {
                           var25 = var10000;
                           return var25.exceptionally(Function.identity()).thenCompose(Hibbett::async$runNormal);
                        }
                     }

                     var19 = var0.sortSizes(var18);

                     try {
                        var10000 = var0.atc(var19, (String)var17.get("authorization"), (String)var17.get("customerId"), (String)var17.get("cartId"));
                        if (!var10000.isDone()) {
                           var25 = var10000;
                           return var25.exceptionally(Function.identity()).thenCompose(Hibbett::async$runNormal);
                        }
                     } catch (Throwable var14) {
                        var27 = var14;
                        var28 = false;
                        break label204;
                     }

                     try {
                        var21 = (Integer)var10000.join();
                        if (!var0.griefMode) {
                           var10000 = var0.submitPayment((String)var17.get("authorization"), (String)var17.get("customerId"), (String)var17.get("cartId"), var21, (String)var17.get("encryptedCVNValue"), (String)var17.get("ccToken"));
                           if (!var10000.isDone()) {
                              var25 = var10000;
                              return var25.exceptionally(Function.identity()).thenCompose(Hibbett::async$runNormal);
                           }
                           break label205;
                        }

                        var0.submitPayment((String)var17.get("authorization"), (String)var17.get("customerId"), (String)var17.get("cartId"), var21, (String)var17.get("encryptedCVNValue"), (String)var17.get("ccToken"));
                        var22 = var0.api.getWebClient().windowUpdateCallback();
                        var10000 = VertxUtil.handleEagerFuture(var22);
                        if (!var10000.isDone()) {
                           var25 = var10000;
                           return var25.exceptionally(Function.identity()).thenCompose(Hibbett::async$runNormal);
                        }
                     } catch (Throwable var15) {
                        var27 = var15;
                        var28 = false;
                        break label204;
                     }
                  }

                  try {
                     var10000.join();
                     break label178;
                  } catch (Throwable var13) {
                     var27 = var13;
                     var28 = false;
                     break label204;
                  }
               }

               try {
                  var10000.join();
               } catch (Throwable var12) {
                  var27 = var12;
                  var28 = false;
                  break label204;
               }
            }

            try {
               var10000 = var0.processPayment((String)var17.get("authorization"), (String)var17.get("customerId"), (String)var17.get("cartId"));
               if (!var10000.isDone()) {
                  var25 = var10000;
                  return var25.exceptionally(Function.identity()).thenCompose(Hibbett::async$runNormal);
               }
            } catch (Throwable var11) {
               var27 = var11;
               var28 = false;
               break label204;
            }
         }

         try {
            boolean var24 = (Boolean)var10000.join();
            return CompletableFuture.completedFuture(var24);
         } catch (Throwable var10) {
            var27 = var10;
            var28 = false;
         }
      }

      Throwable var23 = var27;
      var23.printStackTrace();
      return CompletableFuture.completedFuture(false);
   }

   public JsonArray sortSizes(JsonArray var1) {
      JsonArray var2;
      int var3;
      for(var2 = new JsonArray(); var1.size() != 0; var1.remove(var3)) {
         var3 = ThreadLocalRandom.current().nextInt(var1.size());
         JsonObject var4 = var1.getJsonObject(var3);
         if (var4.getBoolean("isAvailable")) {
            var2.add(0, var4);
         } else {
            var2.add(var4);
         }
      }

      return var2;
   }

   public String selectSpecificSize(JsonArray var1) {
      for(int var2 = 0; var2 < var1.size(); ++var2) {
         JsonObject var3 = var1.getJsonObject(var2);
         if (var3.getString("size").replace(".0", "").equals(this.task.getSize())) {
            return var3.getString("id");
         }
      }

      throw new Exception("Size not found");
   }

   public CompletableFuture submitPayment(String var1, String var2, String var3, Integer var4, String var5, String var6) {
      JsonObject var7 = new JsonObject();
      var7.put("amount", var4);
      var7.put("encryptedCVNValue", var5);
      JsonObject var10002 = (new JsonObject()).put("cardType", this.getProfile().getPaymentMethod().getFirstLetterUppercase()).put("creditCardToken", var6).put("expirationMonth", Integer.parseInt(this.getProfile().getExpiryMonth())).put("expirationYear", Integer.parseInt(this.getProfile().getExpiryYear()));
      String var10004 = this.getProfile().getFirstName();
      var10002 = var10002.put("nameOnCard", var10004 + " " + this.getProfile().getLastName());
      var10004 = this.getProfile().getCardNumber();
      var7.put("paymentObject", var10002.put("number", "************" + var10004.substring(12)));
      var7.put("type", "CREDIT_CARD");
      this.logger.info("Submitting payment");

      while(super.running && this.shouldRunOnSchedule()) {
         CompletableFuture var12;
         CompletableFuture var10000;
         try {
            HttpRequest var8 = this.api.submitPayment(var1, var2, var3);
            var10000 = Request.send(var8, var7.toBuffer());
            if (!var10000.isDone()) {
               var12 = var10000;
               return var12.exceptionally(Function.identity()).thenCompose(Hibbett::async$submitPayment);
            }

            HttpResponse var9 = (HttpResponse)var10000.join();
            if (var9 != null) {
               if (var9.statusCode() == 200) {
                  return CompletableFuture.completedFuture((Object)null);
               }

               this.logger.warn("Submitting billing: status:'{}'", var9.statusCode());
               JsonObject var10 = var9.bodyAsJsonObject();
               var10000 = this.api.handleBadResponse(var9.statusCode(), var10.containsKey("vid") ? var10.getString("vid") : this.api.getPXToken().getVid(), var10.containsKey("uuid") ? var10.getString("uuid") : null);
               if (!var10000.isDone()) {
                  var12 = var10000;
                  return var12.exceptionally(Function.identity()).thenCompose(Hibbett::async$submitPayment);
               }

               byte var11 = (Boolean)var10000.join();
               if (var11 == 0 && this.api.isSkip()) {
                  var10000 = VertxUtil.randomSleep((long)this.task.getRetryDelay());
                  if (!var10000.isDone()) {
                     var12 = var10000;
                     return var12.exceptionally(Function.identity()).thenCompose(Hibbett::async$submitPayment);
                  }

                  var10000.join();
               }
            }
         } catch (Throwable var13) {
            this.logger.error("Error occurred billing: {}", var13.getMessage());
            if (!this.shouldRunOnSchedule()) {
               return CompletableFuture.completedFuture((Object)null);
            }

            var10000 = super.randomSleep(5000);
            if (!var10000.isDone()) {
               var12 = var10000;
               return var12.exceptionally(Function.identity()).thenCompose(Hibbett::async$submitPayment);
            }

            var10000.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture atc(JsonArray var1, String var2, String var3, String var4) {
      int var5 = -1;
      this.logger.info("Adding to cart");

      while(super.running && this.shouldRunOnSchedule()) {
         CompletableFuture var12;
         CompletableFuture var10000;
         try {
            ++var5;
            JsonObject var6 = this.atcPayload(var1, var3, var5);
            String var7 = var6.getJsonArray("cartItems").getJsonObject(0).getJsonObject("sku").getString("id");
            HttpRequest var8 = this.api.atc(var2, var3, var4, var7);
            var10000 = Request.send(var8, var6.toBuffer());
            if (!var10000.isDone()) {
               var12 = var10000;
               return var12.exceptionally(Function.identity()).thenCompose(Hibbett::async$atc);
            }

            HttpResponse var9 = (HttpResponse)var10000.join();
            if (var9 != null) {
               JsonObject var10 = var9.bodyAsJsonObject();
               if (var9.statusCode() == 200) {
                  if (!var10.toString().contains("quantity restriction") && !var10.toString().contains("isn't available") && var10.containsKey("itemCount") && var10.getInteger("itemCount") == 1) {
                     VertxUtil.sendSignal(this.instanceSignal, var7);
                     return CompletableFuture.completedFuture(var10.getNumber("total") == null ? var10.getInteger("subTotal") : var10.getInteger("total"));
                  }

                  this.logger.info("Failed to ATC (empty cart)");
                  var10000 = VertxUtil.signalSleep(this.instanceSignal, (long)this.task.getMonitorDelay());
                  if (!var10000.isDone()) {
                     var12 = var10000;
                     return var12.exceptionally(Function.identity()).thenCompose(Hibbett::async$atc);
                  }

                  var10000.join();
               } else {
                  this.logger.warn("Failed ATC: status:'{}'", var9.statusCode());
                  var10000 = this.api.handleBadResponse(var9.statusCode(), var10.containsKey("vid") ? var10.getString("vid") : this.api.getPXToken().getVid(), var10.containsKey("uuid") ? var10.getString("uuid") : null);
                  if (!var10000.isDone()) {
                     var12 = var10000;
                     return var12.exceptionally(Function.identity()).thenCompose(Hibbett::async$atc);
                  }

                  byte var11 = (Boolean)var10000.join();
                  if (var11 == 0 && this.api.isSkip()) {
                     var10000 = VertxUtil.signalSleep(this.instanceSignal, (long)this.task.getMonitorDelay());
                     if (!var10000.isDone()) {
                        var12 = var10000;
                        return var12.exceptionally(Function.identity()).thenCompose(Hibbett::async$atc);
                     }

                     var10000.join();
                  }
               }
            }
         } catch (Throwable var13) {
            this.logger.error("Error occurred ATC: {}", var13.getMessage());
            var13.printStackTrace();
            if (!this.shouldRunOnSchedule()) {
               return CompletableFuture.completedFuture((Object)null);
            }

            var10000 = super.randomSleep(5000);
            if (!var10000.isDone()) {
               var12 = var10000;
               return var12.exceptionally(Function.identity()).thenCompose(Hibbett::async$atc);
            }

            var10000.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public boolean shouldRunOnSchedule() {
      if (this.scheduledMode) {
         return !this.clock.isStopped();
      } else {
         return true;
      }
   }
}
