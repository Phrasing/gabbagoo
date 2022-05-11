package io.trickle.task.sites.hibbett;

import io.trickle.core.VertxSingleton;
import io.trickle.profile.Profile;
import io.trickle.util.concurrent.VertxUtil;
import io.trickle.util.request.Request;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import org.apache.logging.log4j.Logger;

public class SessionPreload {
   public HibbettAPI client;
   public Hibbett actor;
   public Profile profile;
   public Logger logger;

   public CompletableFuture createCart(String var1) {
      int var2 = 0;

      while(var2++ < 99999 && this.actor.shouldRunOnSchedule()) {
         HttpRequest var3 = this.client.createCart(var1);

         CompletableFuture var7;
         CompletableFuture var9;
         try {
            var9 = Request.send(var3, Buffer.buffer(""));
            if (!var9.isDone()) {
               var7 = var9;
               return var7.exceptionally(Function.identity()).thenCompose(SessionPreload::async$createCart);
            }

            HttpResponse var4 = (HttpResponse)var9.join();
            if (var4 != null) {
               JsonObject var5 = (JsonObject)var4.body();
               if (var5.containsKey("basketId")) {
                  return CompletableFuture.completedFuture(var5.getString("basketId"));
               }

               this.logger.error("Error booting up 5. Retrying - {}", var4.statusCode());
               var9 = this.client.handleBadResponse(var4.statusCode(), var5.containsKey("vid") ? var5.getString("vid") : null, var5.containsKey("uuid") ? var5.getString("uuid") : null);
               if (!var9.isDone()) {
                  var7 = var9;
                  return var7.exceptionally(Function.identity()).thenCompose(SessionPreload::async$createCart);
               }

               byte var6 = (Boolean)var9.join();
               if (var6 == 0 && this.client.isSkip()) {
                  var9 = VertxUtil.randomSleep(5000L);
                  if (!var9.isDone()) {
                     var7 = var9;
                     return var7.exceptionally(Function.identity()).thenCompose(SessionPreload::async$createCart);
                  }

                  var9.join();
               }
            }
         } catch (Throwable var8) {
            this.logger.error("Error occurred at basket: {}", var8.getMessage());
            var9 = VertxUtil.randomSleep(5000L);
            if (!var9.isDone()) {
               var7 = var9;
               return var7.exceptionally(Function.identity()).thenCompose(SessionPreload::async$createCart);
            }

            var9.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$submitEmail(SessionPreload param0, String param1, String param2, JsonObject param3, int param4, HttpRequest param5, CompletableFuture param6, HttpResponse param7, JsonObject param8, int param9, Throwable param10, int param11, Object param12) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture fetch() {
      CompletableFuture var10000 = this.fetchSession();
      CompletableFuture var14;
      if (!var10000.isDone()) {
         var14 = var10000;
         return var14.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetch);
      } else {
         JsonArray var1 = (JsonArray)var10000.join();
         String var2 = "Bearer " + var1.getString(0);
         String var3 = var1.getString(1);
         this.logger.info("Fetching home");
         var10000 = Request.execute(this.client.shopView(var2));
         if (!var10000.isDone()) {
            var14 = var10000;
            return var14.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetch);
         } else {
            var10000.join();
            byte var4 = 3;
            ArrayList var5 = new ArrayList();

            for(int var6 = 1; var6 <= var4; ++var6) {
               var5.add(var6);
            }

            String var15 = null;
            var10000 = this.fetchNonce(var2);
            if (!var10000.isDone()) {
               var14 = var10000;
               return var14.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetch);
            } else {
               String var7 = (String)var10000.join();
               String var8 = null;
               String var9 = null;
               String var10 = null;
               Collections.shuffle(var5);
               Iterator var11 = var5.iterator();

               Integer var12;
               while(var11.hasNext()) {
                  var12 = (Integer)var11.next();
                  switch (var12) {
                     case 1:
                        var10000 = this.createCart(var2);
                        if (!var10000.isDone()) {
                           var14 = var10000;
                           return var14.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetch);
                        }

                        var15 = (String)var10000.join();
                        break;
                     case 2:
                        var10000 = this.submitCard(var7);
                        if (!var10000.isDone()) {
                           var14 = var10000;
                           return var14.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetch);
                        }

                        var8 = (String)var10000.join();
                        break;
                     case 3:
                        var10000 = this.submitCvv(var7);
                        if (!var10000.isDone()) {
                           var14 = var10000;
                           return var14.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetch);
                        }

                        JsonArray var13 = (JsonArray)var10000.join();
                        var9 = var13.getString(0);
                        var10 = var13.getString(1);
                  }
               }

               var4 = 3;
               var5 = new ArrayList();

               for(int var16 = 1; var16 <= var4; ++var16) {
                  var5.add(var16);
               }

               Collections.shuffle(var5);
               var11 = var5.iterator();

               while(var11.hasNext()) {
                  var12 = (Integer)var11.next();
                  switch (var12) {
                     case 1:
                        var10000 = this.submitShipping(var2, var3, var15);
                        if (!var10000.isDone()) {
                           var14 = var10000;
                           return var14.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetch);
                        }

                        var10000.join();
                        break;
                     case 2:
                        var10000 = this.submitShippingRate(var2, var15);
                        if (!var10000.isDone()) {
                           var14 = var10000;
                           return var14.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetch);
                        }

                        var10000.join();
                        break;
                     case 3:
                        var10000 = this.submitEmail(var2, var15);
                        if (!var10000.isDone()) {
                           var14 = var10000;
                           return var14.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetch);
                        }

                        var10000.join();
                  }
               }

               HashMap var17 = new HashMap();
               var17.put("authorization", var2);
               var17.put("customerId", var3);
               var17.put("ccToken", var8);
               var17.put("encryptedCVNValue", var9);
               var17.put("encryptedSecCode", var10);
               var17.put("cartId", var15);
               return CompletableFuture.completedFuture(var17);
            }
         }
      }
   }

   public static CompletableFuture async$fetchNonce(SessionPreload param0, String param1, int param2, HttpRequest param3, CompletableFuture param4, HttpResponse param5, JsonObject param6, int param7, Throwable param8, int param9, Object param10) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture submitCard(String var1) {
      JsonObject var2 = new JsonObject();
      var2.put("paymentAccountNumber", this.profile.getCardNumber());
      int var3 = 0;

      while(var3++ < 99 && this.actor.shouldRunOnSchedule()) {
         HttpRequest var4 = this.client.submitCard(var1);

         CompletableFuture var8;
         CompletableFuture var10;
         try {
            var10 = Request.send(var4, var2.toBuffer());
            if (!var10.isDone()) {
               var8 = var10;
               return var8.exceptionally(Function.identity()).thenCompose(SessionPreload::async$submitCard);
            }

            HttpResponse var5 = (HttpResponse)var10.join();
            if (var5 != null) {
               JsonObject var6 = (JsonObject)var5.body();
               if (var6.containsKey("account_token")) {
                  return CompletableFuture.completedFuture(var6.getString("account_token"));
               }

               this.logger.error("Error booting up 3. Retrying - {}", var5.statusCode());
               var10 = this.client.handleBadResponse(var5.statusCode(), var6.containsKey("vid") ? var6.getString("vid") : null, var6.containsKey("uuid") ? var6.getString("uuid") : null);
               if (!var10.isDone()) {
                  var8 = var10;
                  return var8.exceptionally(Function.identity()).thenCompose(SessionPreload::async$submitCard);
               }

               byte var7 = (Boolean)var10.join();
               if (var7 == 0 && this.client.isSkip()) {
                  var10 = VertxUtil.randomSleep(5000L);
                  if (!var10.isDone()) {
                     var8 = var10;
                     return var8.exceptionally(Function.identity()).thenCompose(SessionPreload::async$submitCard);
                  }

                  var10.join();
               }
            }
         } catch (Throwable var9) {
            this.logger.error("Error occurred step (c): {}", var9.getMessage());
            var10 = VertxUtil.randomSleep(5000L);
            if (!var10.isDone()) {
               var8 = var10;
               return var8.exceptionally(Function.identity()).thenCompose(SessionPreload::async$submitCard);
            }

            var10.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture submitShipping(String var1, String var2, String var3) {
      JsonObject var4 = new JsonObject();
      var4.put("address1", this.profile.getAddress1());
      var4.put("address2", this.profile.getAddress2());
      String var10002 = this.profile.getCity().substring(0, 1).toUpperCase();
      var4.put("city", var10002 + this.profile.getCity().substring(1).toLowerCase());
      var4.put("country", "US");
      var10002 = this.profile.getFirstName().substring(0, 1).toUpperCase();
      var4.put("firstName", var10002 + this.profile.getFirstName().substring(1).toLowerCase());
      var10002 = this.profile.getLastName().substring(0, 1).toUpperCase();
      var4.put("lastName", var10002 + this.profile.getLastName().substring(1).toLowerCase());
      var4.put("phone", this.profile.getPhone());
      var4.put("save", true);
      var4.put("state", this.profile.getState());
      var4.put("zip", this.profile.getZip());
      int var5 = 0;

      while(var5++ < 99 && this.actor.shouldRunOnSchedule()) {
         HttpRequest var6 = this.client.submitShipping(var1, var2, var3);

         CompletableFuture var10;
         CompletableFuture var14;
         try {
            var14 = Request.send(var6, var4.toBuffer());
            if (!var14.isDone()) {
               var10 = var14;
               return var10.exceptionally(Function.identity()).thenCompose(SessionPreload::async$submitShipping);
            }

            HttpResponse var7 = (HttpResponse)var14.join();
            if (var7 != null) {
               JsonObject var8 = (JsonObject)var7.body();
               if (var8.containsKey("shipments") && var8.getJsonArray("shipments").size() != 0 && var8.getJsonArray("shipments").getJsonObject(0).containsKey("shippingAddress") && var8.getJsonArray("shipments").getJsonObject(0).getValue("shippingAddress") != null) {
                  return CompletableFuture.completedFuture((Object)null);
               }

               this.logger.error("Error booting up 7. Retrying - {}", var7.statusCode());
               HibbettAPI var15 = this.client;
               CompletableFuture var10001 = VertxSingleton.INSTANCE.getLocalClient().fetchDeviceFromAPI("https://loudounchris.xyz/api/device/mobileDeviceWithOS.json");
               if (!var10001.isDone()) {
                  CompletableFuture var11 = var10001;
                  HibbettAPI var13 = var15;
                  return var11.exceptionally(Function.identity()).thenCompose(SessionPreload::async$submitShipping);
               }

               var15.setDevice((JsonObject)var10001.join());
               if (ThreadLocalRandom.current().nextBoolean()) {
                  var14 = this.client.handleBadResponse(var7.statusCode(), var8.containsKey("vid") ? var8.getString("vid") : null, var8.containsKey("uuid") ? var8.getString("uuid") : null);
                  if (!var14.isDone()) {
                     var10 = var14;
                     return var10.exceptionally(Function.identity()).thenCompose(SessionPreload::async$submitShipping);
                  }

                  byte var9 = (Boolean)var14.join();
                  if (var9 == 0 && this.client.isSkip()) {
                     var14 = VertxUtil.randomSleep(5000L);
                     if (!var14.isDone()) {
                        var10 = var14;
                        return var10.exceptionally(Function.identity()).thenCompose(SessionPreload::async$submitShipping);
                     }

                     var14.join();
                  }
               }
            }
         } catch (Throwable var12) {
            this.logger.error("Error occurred at (ps): {}", var12.getMessage());
            var14 = VertxUtil.randomSleep(5000L);
            if (!var14.isDone()) {
               var10 = var14;
               return var10.exceptionally(Function.identity()).thenCompose(SessionPreload::async$submitShipping);
            }

            var14.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$fetchSession(SessionPreload param0, int param1, HttpRequest param2, CompletableFuture param3, HttpResponse param4, JsonObject param5, int param6, Throwable param7, int param8, Object param9) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$createCart(SessionPreload param0, String param1, int param2, HttpRequest param3, CompletableFuture param4, HttpResponse param5, JsonObject param6, int param7, Throwable param8, int param9, Object param10) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture submitEmail(String var1, String var2) {
      JsonObject var3 = new JsonObject();
      var3.put("email", this.profile.getEmail());
      int var4 = 0;

      while(var4++ < 99 && this.actor.shouldRunOnSchedule()) {
         HttpRequest var5 = this.client.submitEmail(var1, var2);

         CompletableFuture var9;
         CompletableFuture var11;
         try {
            var11 = Request.send(var5, var3.toBuffer());
            if (!var11.isDone()) {
               var9 = var11;
               return var9.exceptionally(Function.identity()).thenCompose(SessionPreload::async$submitEmail);
            }

            HttpResponse var6 = (HttpResponse)var11.join();
            if (var6 != null) {
               JsonObject var7 = (JsonObject)var6.body();
               if (var7.containsKey("customerInformation") && var7.getJsonObject("customerInformation").containsKey("email")) {
                  return CompletableFuture.completedFuture((Object)null);
               }

               this.logger.error("Error booting up 6. Retrying - {}", var6.statusCode());
               var11 = this.client.handleBadResponse(var6.statusCode(), var7.containsKey("vid") ? var7.getString("vid") : null, var7.containsKey("uuid") ? var7.getString("uuid") : null);
               if (!var11.isDone()) {
                  var9 = var11;
                  return var9.exceptionally(Function.identity()).thenCompose(SessionPreload::async$submitEmail);
               }

               byte var8 = (Boolean)var11.join();
               if (var8 == 0 && this.client.isSkip()) {
                  var11 = VertxUtil.randomSleep(5000L);
                  if (!var11.isDone()) {
                     var9 = var11;
                     return var9.exceptionally(Function.identity()).thenCompose(SessionPreload::async$submitEmail);
                  }

                  var11.join();
               }
            }
         } catch (Throwable var10) {
            this.logger.error("Error occurred at basket: {}", var10.getMessage());
            var11 = VertxUtil.randomSleep(5000L);
            if (!var11.isDone()) {
               var9 = var11;
               return var9.exceptionally(Function.identity()).thenCompose(SessionPreload::async$submitEmail);
            }

            var11.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$submitCvv(SessionPreload param0, String param1, JsonObject param2, int param3, HttpRequest param4, CompletableFuture param5, HttpResponse param6, JsonObject param7, int param8, Throwable param9, int param10, Object param11) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture submitShippingRate(String var1, String var2) {
      JsonObject var3 = new JsonObject();
      var3.put("id", "ANY_GND");
      int var4 = 0;

      while(var4++ < 99 && this.actor.shouldRunOnSchedule()) {
         HttpRequest var5 = this.client.submitShippingRate(var1, var2);

         CompletableFuture var9;
         CompletableFuture var11;
         try {
            var11 = Request.send(var5, var3.toBuffer());
            if (!var11.isDone()) {
               var9 = var11;
               return var9.exceptionally(Function.identity()).thenCompose(SessionPreload::async$submitShippingRate);
            }

            HttpResponse var6 = (HttpResponse)var11.join();
            if (var6 != null) {
               JsonObject var7 = (JsonObject)var6.body();
               if (var7.containsKey("shipments") && var7.getJsonArray("shipments").size() != 0 && var7.getJsonArray("shipments").getJsonObject(0).containsKey("shippingOption") && var7.getJsonArray("shipments").getJsonObject(0).getValue("shippingOption") != null) {
                  return CompletableFuture.completedFuture((Object)null);
               }

               this.logger.error("Error booting up 8. Retrying - {}", var6.statusCode());
               var11 = this.client.handleBadResponse(var6.statusCode(), var7.containsKey("vid") ? var7.getString("vid") : null, var7.containsKey("uuid") ? var7.getString("uuid") : null);
               if (!var11.isDone()) {
                  var9 = var11;
                  return var9.exceptionally(Function.identity()).thenCompose(SessionPreload::async$submitShippingRate);
               }

               byte var8 = (Boolean)var11.join();
               if (var8 == 0 && this.client.isSkip()) {
                  var11 = VertxUtil.randomSleep(5000L);
                  if (!var11.isDone()) {
                     var9 = var11;
                     return var9.exceptionally(Function.identity()).thenCompose(SessionPreload::async$submitShippingRate);
                  }

                  var11.join();
               }
            }
         } catch (Throwable var10) {
            this.logger.error("Error occurred at (ps1): {}", var10.getMessage());
            var11 = VertxUtil.randomSleep(5000L);
            if (!var11.isDone()) {
               var9 = var11;
               return var9.exceptionally(Function.identity()).thenCompose(SessionPreload::async$submitShippingRate);
            }

            var11.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$submitShipping(SessionPreload param0, String param1, String param2, String param3, JsonObject param4, int param5, HttpRequest param6, CompletableFuture param7, HttpResponse param8, JsonObject param9, HibbettAPI param10, int param11, Throwable param12, int param13, Object param14) {
      // $FF: Couldn't be decompiled
   }

   public static CompletableFuture async$fetch(SessionPreload var0, CompletableFuture var1, JsonArray var2, String var3, String var4, int var5, List var6, String var7, String var8, String var9, String var10, String var11, Iterator var12, Integer var13, int var14, Object var15) {
      JsonArray var16;
      CompletableFuture var10000;
      String var17;
      int var18;
      Object var19;
      String var20;
      Iterator var22;
      Integer var23;
      CompletableFuture var26;
      label166: {
         JsonArray var24;
         label139: {
            label138: {
               int var21;
               label153: {
                  String var10002;
                  String var10006;
                  String var10007;
                  String var10008;
                  String var10009;
                  switch (var14) {
                     case 0:
                        var10000 = var0.fetchSession();
                        if (!var10000.isDone()) {
                           var26 = var10000;
                           return var26.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetch);
                        }
                        break;
                     case 1:
                        var10000 = var1;
                        break;
                     case 2:
                        var10002 = var3;
                        var3 = var4;
                        var17 = var10002;
                        var16 = var2;
                        var1.join();
                        var18 = 3;
                        var19 = new ArrayList();
                        var21 = 1;
                        break label153;
                     case 3:
                        var10000 = var1;
                        var10002 = var3;
                        var19 = var6;
                        var18 = var5;
                        var3 = var4;
                        var17 = var10002;
                        var16 = var2;
                        var20 = null;
                        break label138;
                     case 4:
                        var10002 = var3;
                        var10007 = var8;
                        var10008 = var9;
                        var10009 = var10;
                        var22 = var12;
                        var10 = var11;
                        var9 = var10009;
                        var8 = var10008;
                        var7 = var10007;
                        var19 = var6;
                        var18 = var5;
                        var3 = var4;
                        var17 = var10002;
                        var16 = var2;
                        var20 = (String)var1.join();
                        break label139;
                     case 5:
                        var10002 = var3;
                        var10006 = var7;
                        var10009 = var10;
                        var22 = var12;
                        var10 = var11;
                        var9 = var10009;
                        var7 = var8;
                        var20 = var10006;
                        var19 = var6;
                        var18 = var5;
                        var3 = var4;
                        var17 = var10002;
                        var16 = var2;
                        var8 = (String)var1.join();
                        break label139;
                     case 6:
                        var10002 = var3;
                        var10006 = var7;
                        var10007 = var8;
                        var22 = var12;
                        var8 = var9;
                        var7 = var10007;
                        var20 = var10006;
                        var19 = var6;
                        var18 = var5;
                        var3 = var4;
                        var17 = var10002;
                        var16 = var2;
                        var24 = (JsonArray)var1.join();
                        var9 = var24.getString(0);
                        var10 = var24.getString(1);
                        break label139;
                     case 7:
                        var10002 = var3;
                        var10006 = var7;
                        var10007 = var8;
                        var10008 = var9;
                        var10009 = var10;
                        var22 = var12;
                        var10 = var11;
                        var9 = var10009;
                        var8 = var10008;
                        var7 = var10007;
                        var20 = var10006;
                        var19 = var6;
                        var18 = var5;
                        var3 = var4;
                        var17 = var10002;
                        var16 = var2;
                        var1.join();
                        break label166;
                     case 8:
                        var10002 = var3;
                        var10006 = var7;
                        var10007 = var8;
                        var10008 = var9;
                        var10009 = var10;
                        var22 = var12;
                        var10 = var11;
                        var9 = var10009;
                        var8 = var10008;
                        var7 = var10007;
                        var20 = var10006;
                        var19 = var6;
                        var18 = var5;
                        var3 = var4;
                        var17 = var10002;
                        var16 = var2;
                        var1.join();
                        break label166;
                     case 9:
                        var10002 = var3;
                        var10006 = var7;
                        var10007 = var8;
                        var10008 = var9;
                        var10009 = var10;
                        var22 = var12;
                        var10 = var11;
                        var9 = var10009;
                        var8 = var10008;
                        var7 = var10007;
                        var20 = var10006;
                        var19 = var6;
                        var18 = var5;
                        var3 = var4;
                        var17 = var10002;
                        var16 = var2;
                        var1.join();
                        break label166;
                     default:
                        throw new IllegalArgumentException();
                  }

                  var16 = (JsonArray)var10000.join();
                  var17 = "Bearer " + var16.getString(0);
                  var3 = var16.getString(1);
                  var0.logger.info("Fetching home");
                  var10000 = Request.execute(var0.client.shopView(var17));
                  if (!var10000.isDone()) {
                     var26 = var10000;
                     return var26.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetch);
                  }

                  var10000.join();
                  var18 = 3;
                  var19 = new ArrayList();
                  var21 = 1;
               }

               while(true) {
                  if (var21 > var18) {
                     var20 = null;
                     var10000 = var0.fetchNonce(var17);
                     if (!var10000.isDone()) {
                        var26 = var10000;
                        return var26.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetch);
                     }
                     break;
                  }

                  ((List)var19).add(var21);
                  ++var21;
               }
            }

            var7 = (String)var10000.join();
            var8 = null;
            var9 = null;
            var10 = null;
            Collections.shuffle((List)var19);
            var22 = ((List)var19).iterator();
         }

         while(var22.hasNext()) {
            var23 = (Integer)var22.next();
            switch (var23) {
               case 1:
                  var10000 = var0.createCart(var17);
                  if (!var10000.isDone()) {
                     var26 = var10000;
                     return var26.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetch);
                  }

                  var20 = (String)var10000.join();
                  break;
               case 2:
                  var10000 = var0.submitCard(var7);
                  if (!var10000.isDone()) {
                     var26 = var10000;
                     return var26.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetch);
                  }

                  var8 = (String)var10000.join();
                  break;
               case 3:
                  var10000 = var0.submitCvv(var7);
                  if (!var10000.isDone()) {
                     var26 = var10000;
                     return var26.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetch);
                  }

                  var24 = (JsonArray)var10000.join();
                  var9 = var24.getString(0);
                  var10 = var24.getString(1);
            }
         }

         var18 = 3;
         var19 = new ArrayList();

         for(int var25 = 1; var25 <= var18; ++var25) {
            ((List)var19).add(var25);
         }

         Collections.shuffle((List)var19);
         var22 = ((List)var19).iterator();
      }

      while(var22.hasNext()) {
         var23 = (Integer)var22.next();
         switch (var23) {
            case 1:
               var10000 = var0.submitShipping(var17, var3, var20);
               if (!var10000.isDone()) {
                  var26 = var10000;
                  return var26.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetch);
               }

               var10000.join();
               break;
            case 2:
               var10000 = var0.submitShippingRate(var17, var20);
               if (!var10000.isDone()) {
                  var26 = var10000;
                  return var26.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetch);
               }

               var10000.join();
               break;
            case 3:
               var10000 = var0.submitEmail(var17, var20);
               if (!var10000.isDone()) {
                  var26 = var10000;
                  return var26.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetch);
               }

               var10000.join();
         }
      }

      HashMap var27 = new HashMap();
      var27.put("authorization", var17);
      var27.put("customerId", var3);
      var27.put("ccToken", var8);
      var27.put("encryptedCVNValue", var9);
      var27.put("encryptedSecCode", var10);
      var27.put("cartId", var20);
      return CompletableFuture.completedFuture(var27);
   }

   public static CompletableFuture createSession(Hibbett var0) {
      return (new SessionPreload(var0)).fetch();
   }

   public SessionPreload(Hibbett var1) {
      this.actor = var1;
      this.client = (HibbettAPI)var1.getClient();
      this.profile = var1.getProfile();
      this.logger = var1.getLogger();
   }

   public CompletableFuture fetchNonce(String var1) {
      int var2 = 0;

      while(var2++ < 99 && this.actor.shouldRunOnSchedule()) {
         HttpRequest var3 = this.client.nonce(var1);

         CompletableFuture var7;
         CompletableFuture var9;
         try {
            var9 = Request.send(var3);
            if (!var9.isDone()) {
               var7 = var9;
               return var7.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetchNonce);
            }

            HttpResponse var4 = (HttpResponse)var9.join();
            if (var4 != null) {
               JsonObject var5 = (JsonObject)var4.body();
               if (var5.containsKey("nonce")) {
                  return CompletableFuture.completedFuture(var5.getString("nonce"));
               }

               this.logger.error("Error booting up 2. Retrying - {}", var4.statusCode());
               var9 = this.client.handleBadResponse(var4.statusCode(), var5.containsKey("vid") ? var5.getString("vid") : null, var5.containsKey("uuid") ? var5.getString("uuid") : null);
               if (!var9.isDone()) {
                  var7 = var9;
                  return var7.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetchNonce);
               }

               byte var6 = (Boolean)var9.join();
               if (var6 == 0 && this.client.isSkip()) {
                  var9 = VertxUtil.randomSleep(5000L);
                  if (!var9.isDone()) {
                     var7 = var9;
                     return var7.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetchNonce);
                  }

                  var9.join();
               }
            }
         } catch (Throwable var8) {
            this.logger.error("Error occurred step (n): {}", var8.getMessage());
            var9 = VertxUtil.randomSleep(5000L);
            if (!var9.isDone()) {
               var7 = var9;
               return var7.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetchNonce);
            }

            var9.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public CompletableFuture submitCvv(String var1) {
      JsonObject var2 = new JsonObject();
      var2.put("cardSecurityCode", this.profile.getCvv());
      var2.put("paymentAccountNumber", this.profile.getCardNumber());
      int var3 = 0;

      while(var3++ < 99 && this.actor.shouldRunOnSchedule()) {
         HttpRequest var4 = this.client.submitCvv(var1);

         CompletableFuture var8;
         CompletableFuture var10;
         try {
            var10 = Request.send(var4, var2.toBuffer());
            if (!var10.isDone()) {
               var8 = var10;
               return var8.exceptionally(Function.identity()).thenCompose(SessionPreload::async$submitCvv);
            }

            HttpResponse var5 = (HttpResponse)var10.join();
            if (var5 != null) {
               JsonObject var6 = (JsonObject)var5.body();
               if (var6.containsKey("encryptedPaymentAccountNumber") && var6.containsKey("encryptedCardSecurityCode")) {
                  return CompletableFuture.completedFuture((new JsonArray()).add(var6.getString("encryptedPaymentAccountNumber")).add(var6.getString("encryptedCardSecurityCode")));
               }

               this.logger.error("Error booting up 4. Retrying - {}", var5.statusCode());
               var10 = this.client.handleBadResponse(var5.statusCode(), var6.containsKey("vid") ? var6.getString("vid") : null, var6.containsKey("uuid") ? var6.getString("uuid") : null);
               if (!var10.isDone()) {
                  var8 = var10;
                  return var8.exceptionally(Function.identity()).thenCompose(SessionPreload::async$submitCvv);
               }

               byte var7 = (Boolean)var10.join();
               if (var7 == 0 && this.client.isSkip()) {
                  var10 = VertxUtil.randomSleep(5000L);
                  if (!var10.isDone()) {
                     var8 = var10;
                     return var8.exceptionally(Function.identity()).thenCompose(SessionPreload::async$submitCvv);
                  }

                  var10.join();
               }
            }
         } catch (Throwable var9) {
            this.logger.error("Error occurred step (2c): {}", var9.getMessage());
            var10 = VertxUtil.randomSleep(5000L);
            if (!var10.isDone()) {
               var8 = var10;
               return var8.exceptionally(Function.identity()).thenCompose(SessionPreload::async$submitCvv);
            }

            var10.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$submitCard(SessionPreload param0, String param1, JsonObject param2, int param3, HttpRequest param4, CompletableFuture param5, HttpResponse param6, JsonObject param7, int param8, Throwable param9, int param10, Object param11) {
      // $FF: Couldn't be decompiled
   }

   public CompletableFuture fetchSession() {
      int var1 = 0;

      while(var1++ < 99 && this.actor.shouldRunOnSchedule()) {
         HttpRequest var2 = this.client.session();

         CompletableFuture var6;
         CompletableFuture var8;
         try {
            var8 = Request.send(var2, Buffer.buffer(""));
            if (!var8.isDone()) {
               var6 = var8;
               return var6.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetchSession);
            }

            HttpResponse var3 = (HttpResponse)var8.join();
            if (var3 != null) {
               JsonObject var4 = (JsonObject)var3.body();
               if (var4.containsKey("sessionId") && var4.containsKey("customerId")) {
                  return CompletableFuture.completedFuture((new JsonArray()).add(var4.getString("sessionId")).add(var4.getString("customerId")));
               }

               this.logger.info("Error occurred at warming: '{}'", var3.statusCode());
               var8 = this.client.handleBadResponse(var3.statusCode(), var4.containsKey("vid") ? var4.getString("vid") : null, var4.containsKey("uuid") ? var4.getString("uuid") : null);
               if (!var8.isDone()) {
                  var6 = var8;
                  return var6.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetchSession);
               }

               byte var5 = (Boolean)var8.join();
               if (var5 == 0 && this.client.isSkip()) {
                  var8 = VertxUtil.randomSleep(5000L);
                  if (!var8.isDone()) {
                     var6 = var8;
                     return var6.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetchSession);
                  }

                  var8.join();
               }
            }
         } catch (Throwable var7) {
            this.logger.error("Error occurred at warming: {}", var7.getMessage());
            var8 = VertxUtil.randomSleep(5000L);
            if (!var8.isDone()) {
               var6 = var8;
               return var6.exceptionally(Function.identity()).thenCompose(SessionPreload::async$fetchSession);
            }

            var8.join();
         }
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   public static CompletableFuture async$submitShippingRate(SessionPreload param0, String param1, String param2, JsonObject param3, int param4, HttpRequest param5, CompletableFuture param6, HttpResponse param7, JsonObject param8, int param9, Throwable param10, int param11, Object param12) {
      // $FF: Couldn't be decompiled
   }
}
